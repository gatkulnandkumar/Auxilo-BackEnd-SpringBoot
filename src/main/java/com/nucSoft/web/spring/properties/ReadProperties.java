package com.nucSoft.web.spring.properties;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ReadProperties {

	private Logger logger = LogManager.getLogger(this.getClass());
	private Properties prop = null;
	
	public ReadProperties()
	{
		String path = "./config.properties";
		InputStream is = null;
		try
		{
			this.prop = new Properties();
			is = new FileInputStream(path);
			prop.load(is);
		}
		catch(FileNotFoundException e)
		{
			logger.error("Error caused by", e);	
		}
		catch(IOException e)
		{
			logger.error("Error caused by", e);	
		}
	}
	
	  public String getProperty(String key)
	  {
	        return this.prop.getProperty(key);
	  }
	  
}
