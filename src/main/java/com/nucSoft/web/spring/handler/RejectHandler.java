package com.nucSoft.web.spring.handler;

import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;

import com.nucSoft.web.spring.exceptions.BaseExceptions;



/**
 * @author Satish Belose
 * <p>if any runnable is rejected by thread pool then try to put that runnable in queue again</p>
 */
public class RejectHandler implements RejectedExecutionHandler
{

	@Override
	public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
		// TODO Auto-generated method stub
		try{
			executor.getQueue().put(r);
		}catch(Exception e){
			throw new BaseExceptions(e);
		}
	}

}
