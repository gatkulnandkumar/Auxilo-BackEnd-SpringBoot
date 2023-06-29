package com.nucSoft.web.spring.executors;

import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.nucSoft.web.spring.properties.ReadProperties;
import com.nucSoft.web.spring.services.WebClientPostService;
import com.nucSoft.web.spring.executors.DefaultThreadPoolExecutor;
import com.nucSoft.web.spring.executors.DefaultThreadPoolTaskExecutor;
import com.nucSoft.web.spring.executors.IServiceExecutor;
import com.nucSoft.web.spring.processor.RequestFactoryInitializer;



@Configuration
public class ThreadPoolConfiguration {

	ReadProperties environment = new ReadProperties();
	
	@Bean("requestProcessor")
	public DefaultThreadPoolExecutor defaultThreadPoolExecutor() {
		return new DefaultThreadPoolExecutor(Integer.parseInt(environment.getProperty("corePoolSize")),
				Integer.parseInt(environment.getProperty("maxPoolSize")),
				Integer.parseInt(environment.getProperty("keepAliveTime")),
				blockingQueue(), "requestAcceptor");
	}


	@Bean("blockingQueue")
	public Queue<IServiceExecutor> blockingQueue() {
		return new LinkedBlockingQueue<>();
	}
	
	
	@Bean (name="threadPoolTaskExecutor")
	public DefaultThreadPoolTaskExecutor getDefaultThreadPoolTaskExecutor(){
		return new DefaultThreadPoolTaskExecutor(Integer.parseInt(environment.getProperty("corePoolSize")),
				Integer.parseInt(environment.getProperty("maxPoolSize")),
				Integer.parseInt(environment.getProperty("keepAliveTime")), "template"); 
	}
	
	@Bean (name="requestFactory")
	public RequestFactoryInitializer getRequestFactory(){
		return new RequestFactoryInitializer(getDefaultThreadPoolTaskExecutor()); 
	}

}
