package com.nucSoft.web.spring.beans;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.naming.InitialContext;

import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.nucSoft.web.spring.json.utils.JsonUtils;
import com.nucSoft.web.spring.beans.DBBaseProcessException;
import com.nucSoft.web.spring.beans.NucsoftDataSourceConfig;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;


public class NucsoftDataSourceFactory {

	private static List<NucsoftDataSourceConfig> configData = new ArrayList();
	   
	private Logger log = LogManager.getLogger(this.getClass()); 
	   
	public HikariDataSource fetchDataSource(String dsName, String dsFileName) 
	{
	      Object var3 = null;

	      try 
	      {
	         new InitialContext();
	      } catch (Exception var10) 
	      {
	         throw new DBBaseProcessException(var10);
	      }

	      String json = null;

	      try 
	      {
	         json = FileUtils.readFileToString(new File(dsFileName));
	      } catch (IOException var9) 
	      {
	         throw new DBBaseProcessException(var9);
	      }

	      configData = JsonUtils.convertFromJsonToListOfTypeWithoutExclusion(json, NucsoftDataSourceConfig.class);
	      HikariDataSource dataSource = null;
	      Iterator var6 = configData.iterator(); 
			/*
			 * this code takes a JSON string (json) and converts it into a list of
			 * NucsoftDataSourceConfig objects using the JsonUtils class. The resulting list
			 * is stored in the configData variable for further use.
			 */

	      while(var6.hasNext()) 
	      {
	    	  NucsoftDataSourceConfig config = (NucsoftDataSourceConfig)var6.next();
	         if (config.getPoolName().equals(dsName)) //pg_pff1_api 
	        	{
	                HikariConfig hikariConfig = new HikariConfig();
	                hikariConfig.setAutoCommit(config.isAutoCommit());
	                hikariConfig.addDataSourceProperty("user", config.getUser());
	                hikariConfig.addDataSourceProperty("password", config.getPassword());
	            if (config.getJdbcUrl() == null) 
	            {
	               hikariConfig.addDataSourceProperty("port", config.getPort());
	               hikariConfig.addDataSourceProperty("databaseName", config.getDatabaseName());
	               hikariConfig.addDataSourceProperty("serverName", config.getServerName());
	            } 
	            else 
	            {
	               hikariConfig.addDataSourceProperty("url", config.getJdbcUrl());
	            }

	               hikariConfig.setConnectionTestQuery(config.getConnectionTestQuery());
	               hikariConfig.setConnectionTimeout(config.getConnectionTimeout());
	               hikariConfig.setIdleTimeout(config.getIdleTimeout());
	               hikariConfig.setMaximumPoolSize(config.getMaxPoolSize());
	               hikariConfig.setMinimumIdle(config.getMinIdle());
	               hikariConfig.setPoolName(config.getPoolName());
	               hikariConfig.setDataSourceClassName(config.getDataSourceClassName());
	               dataSource = new HikariDataSource(hikariConfig);
	               break;
	         }
	      }

	      return dataSource;
	   }
}
