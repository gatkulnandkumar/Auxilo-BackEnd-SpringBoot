package com.nucSoft.web.spring.beans;

public class DBBaseProcessException extends RuntimeException{

	public DBBaseProcessException(final Throwable e)
	{
		super(e);
	}
	
	public DBBaseProcessException(final String msg)
	{
		super(msg);
	}
	
	public DBBaseProcessException(final Throwable e, final String msg)
	{
		super(msg, e);
	}
}
