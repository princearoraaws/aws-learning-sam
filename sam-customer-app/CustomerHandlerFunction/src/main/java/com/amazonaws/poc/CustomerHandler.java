package com.amazonaws.poc;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.google.gson.Gson;

public class CustomerHandler implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {

	@Override
	public APIGatewayProxyResponseEvent handleRequest(APIGatewayProxyRequestEvent input, Context context) {

		LambdaLogger logger = context.getLogger();
		logger.log("ENTERED LAMBDA FUNCTION CustomerHandler: " + input);

		String request = input.getBody();
		Gson gson = new Gson();
		
		Map<String,String> customerDetails = gson.fromJson(request, Map.class);
		
		customerDetails.put("customerId", UUID.randomUUID().toString());
		
		
		Map<String,String> headerValue = new HashMap();
		headerValue.put("Content-Type", "application/json");
		
		APIGatewayProxyResponseEvent response = new APIGatewayProxyResponseEvent();
		response.withBody(gson.toJson(customerDetails));
		response.withHeaders(headerValue);
		response.withStatusCode(200);

		logger.log("EXITING LAMBDA FUNCTION CustomerHandler: " + input);
		return response;
	}
}
