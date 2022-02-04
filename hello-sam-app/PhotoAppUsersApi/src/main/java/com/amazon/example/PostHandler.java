package com.amazon.example;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.google.gson.Gson;

public class PostHandler implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {

    @Override
    public APIGatewayProxyResponseEvent handleRequest(APIGatewayProxyRequestEvent input, Context context) {

    	LambdaLogger logger = context.getLogger();
    	
    	logger.log("Entered Lambda Function for PostHandler" );
    	String requestBody = input.getBody();
    	Gson gson = new Gson();
    	
    	Map<String,String> userDetails = gson.fromJson(requestBody, Map.class);
    	userDetails.put("userId", UUID.randomUUID().toString());
    	
    	Map returnValue = new HashMap();
    	returnValue.put("firstName", userDetails.get("firstName"));
    	returnValue.put("lastName", userDetails.get("lastName"));
    	returnValue.put("userId", userDetails.get("userId"));
    	
    	APIGatewayProxyResponseEvent response = new APIGatewayProxyResponseEvent();
    	response.withStatusCode(200);
    	response.withBody(gson.toJson(returnValue,Map.class));

    	Map responseHeaders = new HashMap();
    	responseHeaders.put("Content-Type", "application/json");
    	response.withHeaders(responseHeaders);
    	
    	logger.log("Exiting Lambda Function for PostHandler" );
        return response;
    }

}
