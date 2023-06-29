package com.nucSoft.web.spring.constant;

public interface Constants {

	String SERVICE_SOURCE_URL = "sourceurl";
	long DEFERRED_RESULT_TIMEOUT = 90000;
	//String QUERY = "insert into pff_api_servicelog (requestdata,responsedata,updatedtime,createdtime,username,clientid) values(?,?,?,?,?,?)";
	String AUTHORIZATION_HEADER = "Authorization";
	String CONTENT_TYPE = "Content-Type";
	String TARGET_HEADERS = "targetheaders";
	String PARAM_KEY = "paramKey";
	String PATH_KEY = "pathKey";
	//String CONSTANT_QUERY = "select * from constants";
	String USERNAME = "userName";
	String READ_TIMEOUT = "readtimeout";
	String CONNECTION_TIMEOUT = "connectiontimeout";
	String FACTORY_NAME = "requestFactory";
	String REQUEST_ACCEPTOR_TPE = "requestProcessor";
	String ERROR_ON_SERVICE_CALL = "{\"message\":\"Error while calling service,Please try again some time.\"}";

}
