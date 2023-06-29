package com.nucSoft.web.spring.cache;

import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import com.nucSoft.web.spring.serviceRepository.ServiceRepository;

/**
 * <p>
 *    Store the details of all external services 
 *    in cache
 * </p>
 * @author Satish Belose
 *
 */

@Component
public class ConstantCache implements IServiceCache<Map<String,String>>,ApplicationContextAware
{

	private ApplicationContext context;
	
	private Map<String,Map<String,String>> dataMap = null;
	
	private Logger logger = LogManager.getLogger(this.getClass());
	
	@Value(value="${cacheRefreshTimeMin}")
	String minutes;
	
	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.context = applicationContext;
		
	}

	@Override
	public Map<String, Map<String, String>> getData() {
		// TODO Auto-generated method stub
		return dataMap;
	}

	@Override
	public long cacheRefreshTime() {
		long refreshTime = -1l;		
		try {			
			long configTime = Long.parseLong(minutes);
			if(configTime > 0){
				refreshTime = configTime*60*1000;
			}	
		} catch (Exception e) {			
		}		
		return refreshTime;
	}

	@Override
	public void refreshData() {
		dataMap = context.getBean(ServiceRepository.class).fetchConstantMap();
		
	}

	
}
