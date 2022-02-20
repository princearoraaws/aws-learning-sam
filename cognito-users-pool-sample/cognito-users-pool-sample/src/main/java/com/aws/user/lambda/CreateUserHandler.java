package com.aws.user.lambda;

import java.util.HashMap;
import java.util.Map;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.aws.user.lambda.service.CognitoUserService;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import software.amazon.awssdk.awscore.exception.AwsServiceException;

public class CreateUserHandler implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {

	private final CognitoUserService cognitoUserService; 
	private final String appClientId; 
	
	public CreateUserHandler() {
		this.cognitoUserService = new CognitoUserService(System.getenv("AWS_REGION"));
		this.appClientId = System.getenv("MY_COGNITO_POOL_APP_CLIENT_ID");
	}
	
    @Override
    public APIGatewayProxyResponseEvent handleRequest(APIGatewayProxyRequestEvent input, Context context) {
    	LambdaLogger logger = context.getLogger();
    	logger.log("Entered CreateUserHandler:handleRequest: " + input);
        
    	String request = input.getBody();
    	logger.log("Original request : " +request);
    	JsonObject userDetails =  JsonParser.parseString(request).getAsJsonObject();
    	
        Map<String,String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        
        APIGatewayProxyResponseEvent response = new APIGatewayProxyResponseEvent();
        response.withHeaders(headers);

        
    	try {
    		JsonObject createUserResult = cognitoUserService.createUser(userDetails, appClientId);
            response.withBody(new Gson().toJson(createUserResult, JsonObject.class));
            response.withStatusCode(200);
    	}
    	catch(AwsServiceException e) {
    		logger.log(e.awsErrorDetails().errorMessage());
    		response.withBody(e.awsErrorDetails().errorMessage());
    		response.withStatusCode(500);
    	}
        return response;
    }

}
