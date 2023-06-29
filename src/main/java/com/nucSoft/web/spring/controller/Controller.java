package com.nucSoft.web.spring.controller;

import javax.servlet.http.HttpServletRequest;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.async.DeferredResult;

import com.nucSoft.web.spring.services.WebClientGetService;
import com.nucSoft.web.spring.services.WebClientPostService;

@CrossOrigin(origins = "**", maxAge = 3600)
@RestController
@RequestMapping("/pffapi/api/v1")
public class Controller 
{
   private Logger logger = LogManager.getLogger(this.getClass());
	
	@Autowired
	private WebClientPostService webClientPostService;
	
	@Autowired
	private WebClientGetService webClientGetService;
	
	@PostMapping(value ="/{pathVariable}",consumes="application/json",produces="application/json")
	public @ResponseBody DeferredResult<Object> postRequestHandler(@RequestBody(required = false) String requestBody,HttpServletRequest request,@PathVariable String pathVariable){
		
		logger.info("Request received in Controller --->"+requestBody);		
		return webClientPostService.requestProcessor(requestBody,request,pathVariable);
	}
		
	@GetMapping(value ="/{pathVariable}")
	public @ResponseBody DeferredResult<Object> getRequestHandler(@RequestBody(required = false) String requestBody,HttpServletRequest request,@PathVariable String pathVariable){
		logger.info("Request received in Controller --->");	
		return webClientGetService.requestProcessor(requestBody,request,pathVariable);
	}
	
}
