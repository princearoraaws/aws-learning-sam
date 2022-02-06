package com.amazonaws.lambda.orderhandler;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.google.gson.Gson;

public class OrderHandler implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {

    @Override
    public APIGatewayProxyResponseEvent handleRequest(APIGatewayProxyRequestEvent input, Context context) {
        LambdaLogger logger = context.getLogger();
    	
        logger.log("Entered lambda function OrderHandler");
        logger.log("input : "+input);
        
        String request = input.getBody();
        Gson gson = new Gson();
        Random rand = new Random();
        
        Map<String,String> orderDetails = gson.fromJson(request, Map.class);
        orderDetails.put("orderNumber", new Integer(1+(rand.nextInt(1000))).toString());
        orderDetails.put("orderId", UUID.randomUUID().toString());
        
        
        Map<String,String> returnValue = new HashMap();
        returnValue.put("orderNumber", orderDetails.get("orderNumber"));
        returnValue.put("orderId", orderDetails.get("orderId"));
        returnValue.put("orderStatus", orderDetails.get("orderStatus"));
        returnValue.put("productName", orderDetails.get("productName"));
        returnValue.put("creationDate", orderDetails.get("creationDate"));
        
        Map<String,String> headerValue = new HashMap();
        headerValue.put("Content-Type", "application/json");
        
        APIGatewayProxyResponseEvent response = new APIGatewayProxyResponseEvent();
        response.withBody(gson.toJson(returnValue, Map.class));
        response.withStatusCode(200);
        response.withHeaders(headerValue);
        
        logger.log("Exiting lambda function OrderHandler");
        return response;
    }

}
