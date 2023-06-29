package com.nucSoft.web.spring.executors;

import java.util.Queue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import com.nucSoft.web.spring.handler.RejectHandler;
import com.nucSoft.web.spring.executors.DefaultThreadPoolExecutor;
import com.nucSoft.web.spring.executors.IServiceExecutor;



public class DefaultThreadPoolExecutor  extends ThreadPoolExecutor implements ApplicationContextAware
{

	private Logger log = LogManager.getLogger(this.getClass());
	
	private Queue<IServiceExecutor> executorQueue ;
	
	private String poolName;
	
	private ApplicationContext applicationContext;
	
	public DefaultThreadPoolExecutor(int corePoolSize, int maximumPoolSize,
			long keepAliveTime, TimeUnit unit,
			BlockingQueue<Runnable> workQueue, RejectedExecutionHandler handler) {
		super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, handler);
	}
	
	public DefaultThreadPoolExecutor(int corePoolSize , int maxPoolSize , 
			long keepAliveTime,Queue<IServiceExecutor> queue,String poolName ){
		this(corePoolSize, maxPoolSize, keepAliveTime, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<Runnable>(){
			
			private static final long serialVersionUID = 1L;

			@Override
			public boolean offer(Runnable e) {
				return false;
			}
		}, new RejectHandler());
		
		this.executorQueue = queue;
		this.poolName = poolName;
	}

	/**
	 * add task to queue and notify for task execution 
	 * @param executor
	 */
	
	public <T> Future<T> addTaskToQueue(IServiceExecutor executor){
		log.info("Executing async task " + executor.getName());
		log.info("Metrics for Pool " +  DefaultThreadPoolExecutor.this.poolName + " are " + DefaultThreadPoolExecutor.this.toString());
		Future<T> ftask = null;
		try{
			ftask  = DefaultThreadPoolExecutor.this.submit(executor);
		}catch(Exception e){
			log.error("Error caused by ", e);
		}
		log.info("Executed async task " + executor.getName());
		return ftask;
	}
	
	@Override
	public void setApplicationContext(ApplicationContext applicationContext)
			throws BeansException {
		this.applicationContext = applicationContext;
		
	}
}
