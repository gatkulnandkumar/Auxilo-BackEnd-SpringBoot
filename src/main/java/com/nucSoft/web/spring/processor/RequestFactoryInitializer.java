package com.nucSoft.web.spring.processor;

import org.springframework.http.client.SimpleClientHttpRequestFactory;

import com.nucSoft.web.spring.executors.DefaultThreadPoolTaskExecutor;

/**
 * <p>
 *    Template for creating request factory
 *  </p>
 * @author Satish Belose
 *
 */
public class RequestFactoryInitializer {

	private SimpleClientHttpRequestFactory factory;

    /**
     * <p>
     *    Initialize the request factory
     * </p>
     * @param minThreads
     * @param maxThreads
     * @param maxIdletime
     * @param executorQueue
     * @param poolName
     */
    public RequestFactoryInitializer(DefaultThreadPoolTaskExecutor executor)
    {           
          factory = new SimpleClientHttpRequestFactory();
          factory.setTaskExecutor(executor);
    }
   
    /**
     * <p>
     *    Get the created factory
     * </p>
     * @return
     */
    public SimpleClientHttpRequestFactory getFactory() {
          return factory;
    } 
}
