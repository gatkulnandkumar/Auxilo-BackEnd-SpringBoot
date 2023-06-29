package com.nucSoft.web.spring.services;

import java.net.URI;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.request.async.DeferredResult;

import com.nucSoft.web.spring.cache.ConstantCache;
import com.nucSoft.web.spring.cache.ServiceDetailsCacheService;
import com.nucSoft.web.spring.executors.DefaultThreadPoolExecutor;
import com.nucSoft.web.spring.serviceRepository.ServiceRepository;
import com.nucSoft.web.spring.services.WebClientPostService;
import com.nucSoft.web.spring.json.utils.JsonUtils;
import com.nucSoft.web.spring.processor.RequestFactoryInitializer;
import com.nucSoft.web.spring.constant.Constants;
import com.nucSoft.web.spring.executors.IServiceExecutor;

@Service
public class WebClientPostService 
{

	private Logger logger = LogManager.getLogger(this.getClass());
	
	@Autowired
	private ApplicationContext context;
	
	
	@Autowired
	private ServiceRepository serviceRepository;
	
	@Autowired
	public DefaultThreadPoolExecutor defaultThreadpool;
	
	@Autowired
	private ServiceDetailsCacheService cacheService;
	
	@Autowired
	private ConstantCache cache;
	
	public DeferredResult<Object> requestProcessor(String requestBody,HttpServletRequest request,String pathVariable){
		long start_time = System.currentTimeMillis();	
		final DeferredResult<Object> deferredResult = new DeferredResult<Object>(Constants.DEFERRED_RESULT_TIMEOUT);
		
		IServiceExecutor<?> executor = new IServiceExecutor<Object>() {
						@Override
			public Object call() throws Exception {
				// TODO Auto-generated method stub
				JSONObject responseJson = new JSONObject();
				String Query = "";
				String userName = "";
				String clientId = "";
				long end_time = 0;
			
			try {			
					Map<String,Map<String,String>> constantMap = (Map<String, Map<String, String>>) cacheService.getData(cache);				
					Map<String,String> map = constantMap.get(pathVariable);
					String url = map.get(Constants.SERVICE_SOURCE_URL);
					int readTimeout = Integer.valueOf(map.get(Constants.READ_TIMEOUT));
					int connectionTimeout = Integer.valueOf(map.get(Constants.CONNECTION_TIMEOUT));
					String headersData = map.get(Constants.TARGET_HEADERS);
					
					
					logger.info("url is--->"+url);
					logger.info("Read timeout is--->"+readTimeout);
					logger.info("Connection timeout is--->"+connectionTimeout);
					logger.info("Headers data is--->"+headersData);
					logger.info("Query--->"+Query);
					
					userName = request.getHeader(Constants.USERNAME);
					clientId = request.getHeader(Constants.AUTHORIZATION_HEADER);
				
					LocalDateTime now = LocalDateTime.now();	// for date time
					SimpleDateFormat simpleDateFormat = new SimpleDateFormat("ddMMyyyyHHmmssSSS");		
			
					JSONObject headersNew = new JSONObject(headersData);

					String serviceName = headersNew.getString("SERVICENAME"); // fetching service name from json passed in constants table
					logger.info("serviceName---->"+serviceName);
		
					headersNew.put("REQUESTTIME",now.withNano(0));  // to remove milliseconds
					
					headersNew.put("MESSAGEID", "API" + simpleDateFormat.format(new Date()));
					logger.info("headersNew---->"+headersNew);
					
					Long startTime = System.currentTimeMillis();
					SimpleClientHttpRequestFactory factory = ((RequestFactoryInitializer)context.getBean(Constants.FACTORY_NAME)).getFactory();
					factory.setConnectTimeout(connectionTimeout);
					factory.setReadTimeout(readTimeout);					
					HttpEntity<String> requestEntity = new HttpEntity<String>(requestBody,prepareHeaderMap(headersNew.toString()));					
					RestTemplate restTemplate = new RestTemplate(factory);
					org.springframework.http.ResponseEntity<String> data = restTemplate.postForEntity(URI.create(url), requestEntity, String.class);
					logger.info("Response Body is--->"+ data.getBody());
					logger.info("Time taken for api call is --->",((System.currentTimeMillis())- (startTime)));
					
					logger.info("Time taken to service call is --->"+(end_time-start_time));
					JSONObject responseBody = new JSONObject(data.getBody());	
					responseJson.put("status","Success");
					responseJson.put("statusCode",101);
					responseJson.put("message","Request Completed Successfully.");
					responseJson.put("dataResponse",responseBody);
			} catch(Exception e)
				  {
					logger.error("Error caused by--->",e);
							
					responseJson.put("status","Failure");
					responseJson.put("statusCode",102);
					responseJson.put("message","Request Completed Successfully.");
					responseJson.put("dataResponse",e.getMessage());
				  }
				  finally
					{
					  long id = 0;
					  String transactionid = "";
					  end_time = System.currentTimeMillis();
					  Map<String, Object> insertRecordsMap = new HashMap<>();
					  insertRecordsMap.put("requestdata", requestBody);
                      insertRecordsMap.put("responsedata", ""+responseJson);
					  insertRecordsMap.put("updatedtime", end_time);
					  insertRecordsMap.put("createdtime", start_time);
					  insertRecordsMap.put("username",userName);
					  insertRecordsMap.put("clientid",clientId);	
					  insertRecordsMap.put("servicetype",pathVariable);

					  transactionid = serviceRepository.insertRecords(insertRecordsMap);
					  responseJson.put("transactionid", transactionid);
			          deferredResult.setResult(responseJson.toString());
					}
					return deferredResult;
				}

				@Override
				public boolean getParentsStatuses() 
				{
					return false;
				}

				@Override
				public String getName() 
				{
					return WebClientPostService.class.getName();
				}
			};
			((DefaultThreadPoolExecutor)this.context.getBean(Constants.REQUEST_ACCEPTOR_TPE)).addTaskToQueue(executor);		
			return deferredResult;
	}
	/**
	 * <p>
	 *    Given the string of headers, prepare header map
	 * </p>
	 * @param headers
	 * @return
	 */
	public static MultiValueMap<String, String> prepareHeaderMap(String headers)
	{   
		Map<String,Object> headerJsonMap = JsonUtils.prepareMapFromJson(headers);
		MultiValueMap<String, String> headerMap = new HttpHeaders();
		Map<String,String> hm = new HashMap< String,String>(); 
		headerJsonMap.entrySet().forEach((t)->hm.put(t.getKey(),t.getValue().toString()));
		headerMap.setAll(hm);
		return headerMap;
	}
	
}
