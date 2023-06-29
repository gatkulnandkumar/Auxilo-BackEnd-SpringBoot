package com.nucSoft.web.spring.executors;

import java.util.concurrent.Callable;

/**
 * <p>
 *   Interface to be implemented by all classes
 *   which want to be asynchrnously executed
 * </p>
 * @author Satish Belose
 *
 */
public interface IServiceExecutor<T> extends Callable<T>
{
	
	public boolean getParentsStatuses();
	
	public String getName();
		
}