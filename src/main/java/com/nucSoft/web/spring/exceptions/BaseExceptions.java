package com.nucSoft.web.spring.exceptions;

public class BaseExceptions extends RuntimeException {

	/**
	 * <p> 
	 *   Implementing default constructor
	 * </p>
	 */
	public BaseExceptions(){
		super();
	}
	
	/**
	 * <p>
	 *    Create exception with custom message
	 * </p>
	 * @param message
	 */
	public BaseExceptions(String message){
		super(message);
	}
	
	/**
	 * <p>
	 *   Create exception with custom message and throwable
	 * </p>
	 * @param message
	 * @param e
	 */
	public BaseExceptions(String message,Throwable e){
		super(message,e);
	}
	
	/**
	 * <p> 
	 *    Create exception wrapping the cause exception
	 * </p>
	 * @param e
	 */
	public BaseExceptions(Throwable e){
		super(e);
	}
}
