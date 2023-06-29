package com.nucSoft.web.spring.executors;

/**
 * <p>
 *    Executor interface to be implemented by all
 *    classes which want to execute action through Hubble
 *    Thread Pool
 * </p>
 * @author Satish Belose
 *
 */
public interface IExecutor
{   
	/**
	 * <p>
	 *    Execute action for the particular exectuor
	 *  </p>
	 * @param data
	 */
	public void executeAction();
	
	/**
	 * <p>
	 *    Fetch executors name
	 * </p>
	 * @return
	 */
	public String name();
	
	/**
	 * <p>
	 *    Identifier to identify who is running the thread
	 * </p>
	 * @return
	 */
	default String identifier(){
		return "";
	}
	
	
}