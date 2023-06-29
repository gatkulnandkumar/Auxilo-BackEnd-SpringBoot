package com.nucSoft.web.spring.json.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.reflect.TypeToken;

/**
 * <p>
 *   Utility class for all json functionality
 * </p>
 * @author Satish Belose
 *
 */
public class JsonUtils {

	   
	/**
	 * <p>
	 *   Prepare map from json string
	 * </p>
	 * @param json
	 * @return
	 */
    public static Map<String,Object> prepareMapFromJson(String json)
    {
    	Map<String, Object> retMap = new GsonBuilder().disableHtmlEscaping().create().fromJson(json, new TypeToken<HashMap<String, Object>>() {}.getType());
    	
    	return retMap;
    }
    
    /**
     * <p>
     *   Take of property of the class labelled as @param type
     *   and create an object
     * </p>
     * @param json
     * @param type
     * @return
     */
    public static <T> T convertJsonToObjectWithoutExclusion(String json,Class<T> type)
    {
    	return new GsonBuilder().disableHtmlEscaping().create().fromJson(json, type);
    }
	/*
	 * To summarize, the convertJsonToObjectWithoutExclusion method uses Gson to
	 * parse a JSON string and convert it into an object of the specified type. It
	 * disables HTML escaping and directly deserializes the JSON string into the
	 * target object type specified by the type parameter.
	 */
    
    /**
     * <p>
     *    Convert json to object of specific type
     * </p>
     * @param json
     * @param type
     * @return
     */
    public static <T> T convertJsonToObject(String json,Class<T> type)
    {   
    	
    	Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().disableHtmlEscaping().create();
    	
    	return gson.fromJson(json, type);
    }
    
    /**
     * <p>
     *    Convert object to json
     * </p>
     * @param obj
     * @return
     */
    public static String convertObjectToJson(Object obj)
    {
    	Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().disableHtmlEscaping().create();
    	
    	return gson.toJson(obj);
    }
    
    /**
     * <p>
     *   Convert object to json without any exclusion
     * </p>
     * @param obj
     * @return
     */
    public static String convertObjectToJsonWithoutExclusion(Object obj){
    	
    	Gson gson = new GsonBuilder().disableHtmlEscaping().create();
    	
    	return gson.toJson(obj);
    }
    
    /**
     * <p>
     *    Check if a string maybe json
     * </p>
     * @param value
     * @return
     */
    public static boolean maybeJson(String value)
    {
    	return value.startsWith("{")||value.startsWith("[");
    }
    
    
    
    /**
     * <p>
     *   Convert from json to a list of a particular type
     * </p>
     * @param json
     * @param type
     * @return
     */
    
    public static <T> List<T> convertFromJsonToListOfTypeWithoutExclusion(String json,Class<T> type)
    {
    	Gson gson = new Gson();
    	
    	// final list of type @param T
    	List<T> finalDataList = new ArrayList<T>();
    	
    	JsonArray jsonArray = gson.toJsonTree(gson.fromJson(json,new TypeToken<ArrayList<T>>() {
        }.getType())).getAsJsonArray();
    	
    	@SuppressWarnings("rawtypes")
		Iterator it = jsonArray.iterator();
    	
    	while(it.hasNext()){
    		finalDataList.add(gson.fromJson(it.next().toString(), type));
    	}
    	
    	return finalDataList;  	
    	
    }
    
    /**
     * <p>
     *   Convert from json to a list of a particular type
     * </p>
     * @param json
     * @param type
     * @return
     */
    
    public static <T> List<T> convertFromJsonToListOfType(String json,Class<T> type)
    {
    	Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().disableHtmlEscaping().create();
    	
    	// final list of type @param T
    	List<T> finalDataList = new ArrayList<T>();
    	
    	JsonArray jsonArray = gson.toJsonTree(gson.fromJson(json,new TypeToken<ArrayList<T>>() 
    	{
        }.getType())).getAsJsonArray();
    	
    	@SuppressWarnings("rawtypes")
		Iterator it = jsonArray.iterator();
    	
    	while(it.hasNext())
    	{
    		finalDataList.add(gson.fromJson(it.next().toString(), type));
    	}
    	
    	return finalDataList;
    }
}
