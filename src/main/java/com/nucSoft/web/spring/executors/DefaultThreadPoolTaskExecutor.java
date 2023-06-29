package com.nucSoft.web.spring.executors;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.core.task.TaskDecorator;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import com.nucSoft.web.spring.handler.RejectHandler;
import com.nucSoft.web.spring.executors.DefaultThreadPoolTaskExecutor;

/**
 * <p>
 *    This class needs to be inititalized by the caller
 * </p>
 * @author Satish Belose
 *
 */
public class DefaultThreadPoolTaskExecutor extends ThreadPoolTaskExecutor{

    private Logger logger = LogManager.getLogger(this.getClass());
    private String taskExecutorName;
    
    public DefaultThreadPoolTaskExecutor(int corePoolSize, int maximumPoolSize,int keepAliveTime,String executorName)
    {
    	 super.setRejectedExecutionHandler(new RejectHandler());
         super.setCorePoolSize(corePoolSize);
         super.setMaxPoolSize(maximumPoolSize);
         super.setKeepAliveSeconds(keepAliveTime);
         this.taskExecutorName = executorName;
         
         super.setTaskDecorator(new TaskDecorator() 
         {
         	public Runnable decorate(Runnable runnable) 
         	{
         		logger.info("Running custom thread pool executor "+ DefaultThreadPoolTaskExecutor.this.taskExecutorName + " with statistics " + DefaultThreadPoolTaskExecutor.this.getThreadPoolExecutor().toString());
         		return runnable;
         	}
         });
    }
    
    @Override
    protected BlockingQueue<Runnable> createQueue(int queueCapacity) 
    {
   	 return new LinkedBlockingQueue<Runnable>(queueCapacity) 
   	 {
   		 private static final long serialVersionUID = 1L;

      @Override
      public boolean offer(Runnable e) 
      {
   	     return false;
      }
    };
  }
}
