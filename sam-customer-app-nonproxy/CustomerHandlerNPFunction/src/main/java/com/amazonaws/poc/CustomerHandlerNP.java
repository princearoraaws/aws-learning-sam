package com.amazonaws.poc;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.RequestHandler;

public class CustomerHandlerNP implements RequestHandler<Map<String,String>, Map<String,String>> {

    @Override
    public Map<String,String> handleRequest(Map<String,String> input, Context context) {
    	LambdaLogger logger = context.getLogger();
        context.getLogger().log("ENTERED LAMBDA CustomerNPHandler: " + input);
        context.getLogger().log("Code version '2'- PROD deployment #### VERSION  #### =====> "+context.getFunctionVersion());
        
        String firstName = input.get("firstName");
        String lastName = input.get("lastName");
        String email = input.get("email");
        String secretKey = input.get("secretKey");
        
        logger.log("firstName : "+firstName);
        logger.log("lastName : "+lastName);
        logger.log("email : "+email);
        
        Map<String,String> response = new HashMap();
        response.put("userId", UUID.randomUUID().toString());
        response.put("firstName", firstName);
        response.put("lastName", lastName);
        response.put("email", email);
        response.put("secretKey", secretKey);
        
        context.getLogger().log("EXITING LAMBDA CustomerNPHandler: ");
        return response;
    }

}
