package com.nucSoft.web.spring.cache;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import com.nucSoft.web.spring.cache.IServiceCache;
import com.nucSoft.web.spring.cache.ServiceDetailsCacheService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 *    Cache component to handle the lifecycle
 *    all caches created. Handling events of refresh,
 *    data fetching etc....
 *  </p>
 * @author Satish Belose
 *
 */
@Service
public class ServiceDetailsCacheService<T extends Object>  
{

private List<IServiceCache<T>> cacheList;
	
	private Map<IServiceCache<T>,Long> cacheRefreshMap = new HashMap<IServiceCache<T>,Long>();
	
	private Logger log = LogManager.getLogger(ServiceDetailsCacheService.class);
	
	/**
	 * <p>
	 *   Get the data for the @param cache. This method checks if
	 *   the cache has any data or needs to be refreshed. If the data
	 *   in the cache is null or the cache needs to be refreshed 
	 *   then the cache is refreshed and the data is then returned.
	 * </p>
	 * @param cache
	 * @return
	 */
	public <K extends IServiceCache<T>> T getData(K cache)
	{
		if(cache.getData() == null || (cache.cacheRefreshTime() != -1l && (cache.cacheRefreshTime() < (System.currentTimeMillis() - (cacheRefreshMap.get(cache) == null ? 0:cacheRefreshMap.get(cache))))))
		{
			synchronized(this)
			{
				if(cache.getData() == null || (cache.cacheRefreshTime() != -1l && (cache.cacheRefreshTime() < (System.currentTimeMillis() - (cacheRefreshMap.get(cache) == null ? 0:cacheRefreshMap.get(cache))))))
				{
					cache.refreshData();
					cacheRefreshMap.put(cache, System.currentTimeMillis());
					log.info("Refreshed cache " + cache.getClass().getSimpleName());
				}
			}
		}
		
		return (T) cache.getData();
	}

	public List<IServiceCache<T>> getCacheList() {
		return cacheList;
	}
}
