package com.nucSoft.web.spring.cache;

import java.util.Map;

public interface IServiceCache<T> {

	/**
	 * <p>
	 *    Get cache data.
	 * </p>
	 * @return
	 */
	public Map<String, Map<String, String>> getData();
	
	/**
	 * <p>
	 *   Return the time after which the cache should be refreshed. 
	 *   A value of -1 means that the cache should never be refreshed.
	 * @return
	 */
	public long cacheRefreshTime();
	
	/**
	 * <p>
	 *   Method to be invoked when the cache needs to be 
	 *   refreshed
	 * </p>
	 */
	public void refreshData();
}
