package com.aws.user.lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.aws.user.lambda.service.CognitoUserService;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import software.amazon.awssdk.awscore.exception.AwsServiceException;
import software.amazon.awssdk.regions.Region;

public class ConfirmUserHandler implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {

	private final CognitoUserService cognitoUserService;
	private final String appClientId;

	public ConfirmUserHandler() {
		this.cognitoUserService = new CognitoUserService((System.getenv("AWS_REGION")));
		this.appClientId = System.getenv("MY_COGNITO_POOL_APP_CLIENT_ID");
	}

	@Override
	public APIGatewayProxyResponseEvent handleRequest(APIGatewayProxyRequestEvent input, Context context) {

		LambdaLogger logger = context.getLogger();
		logger.log("Input: " + input);

		APIGatewayProxyResponseEvent response = new APIGatewayProxyResponseEvent();

		try {
			String requestBody = input.getBody();
			JsonObject requestJson = JsonParser.parseString(requestBody).getAsJsonObject();
			String email = requestJson.get("email").getAsString();
			String confirmationCode = requestJson.get("code").getAsString();
			JsonObject confirmUserResult = cognitoUserService.confirmUserSignup(appClientId, confirmationCode, email);
		} catch (AwsServiceException ex) {
			logger.log(ex.awsErrorDetails().errorMessage());
			response.withStatusCode(ex.awsErrorDetails().sdkHttpResponse().statusCode());
			response.withBody(ex.awsErrorDetails().errorMessage());

		} catch (Exception ex) {
			logger.log(ex.getMessage());
			response.withStatusCode(500);
		}

		return response;
	}

}
