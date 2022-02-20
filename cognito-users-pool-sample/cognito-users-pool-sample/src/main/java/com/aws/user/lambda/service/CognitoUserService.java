package com.aws.user.lambda.service;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.JsonObject;

import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.cognitoidentityprovider.CognitoIdentityProviderClient;
import software.amazon.awssdk.services.cognitoidentityprovider.model.AttributeType;
import software.amazon.awssdk.services.cognitoidentityprovider.model.ConfirmSignUpRequest;
import software.amazon.awssdk.services.cognitoidentityprovider.model.ConfirmSignUpResponse;
import software.amazon.awssdk.services.cognitoidentityprovider.model.SignUpRequest;
import software.amazon.awssdk.services.cognitoidentityprovider.model.SignUpResponse;

public class CognitoUserService {
	
	public final CognitoIdentityProviderClient cognitoIdentityProviderClient;
	
	public CognitoUserService(String region) {
		this.cognitoIdentityProviderClient = CognitoIdentityProviderClient.builder()
				.region(Region.of(region))
				.build();
				
	}
	
	public JsonObject createUser(JsonObject user, String appClientId) {
		
		
		String email = user.get("email").getAsString();
		String password = user.get("password").getAsString();
		String given_name = user.get("given_name").getAsString();
		String family_name = user.get("family_name").getAsString();
		String country = user.get("country").getAsString();
		
		
		AttributeType emailAttribute = AttributeType.builder()
				.name("email")
				.value(email)
				.build();
		
		AttributeType givenNameAttribute = AttributeType.builder()
				.name("given_name")
				.value(given_name)
				.build();
		
		AttributeType familyNameAttribute = AttributeType.builder()
				.name("family_name")
				.value(family_name)
				.build();		
				
		AttributeType countryAttribute = AttributeType.builder()
				.name("custom:country")
				.value(country)
				.build();
		
		
		List<AttributeType> attributes = new ArrayList<>();
		attributes.add(emailAttribute);
		attributes.add(givenNameAttribute);
		attributes.add(familyNameAttribute);
		attributes.add(countryAttribute);
		
		
		JsonObject createdUserDetails = new JsonObject();
		SignUpRequest signUpRequest = SignUpRequest.builder()
				.username(email)
				.password(password)
				.userAttributes(attributes)
				.clientId(appClientId)
				.build();
				
		SignUpResponse signUpResponse = cognitoIdentityProviderClient.signUp(signUpRequest);
		
		createdUserDetails.addProperty("isSuccessful", signUpResponse.sdkHttpResponse().isSuccessful());
		createdUserDetails.addProperty("status", signUpResponse.sdkHttpResponse().statusCode());
		createdUserDetails.addProperty("cognitoUserId", signUpResponse.userSub());
		createdUserDetails.addProperty("isConfirmed", signUpResponse.userConfirmed());
		
				
		return createdUserDetails;
	}
	
	public JsonObject confirmUserSignup(String appClientId, String confirmationCode, String email) {
		
		ConfirmSignUpRequest confirmUserSignUpRequest = ConfirmSignUpRequest.builder()
				.clientId(appClientId)
				.confirmationCode(confirmationCode)
				.username(email)
				.build();
				
		ConfirmSignUpResponse confirmSignUpResponse = cognitoIdentityProviderClient.confirmSignUp(confirmUserSignUpRequest);
		
		JsonObject confirmUserResponse = new JsonObject();
		confirmUserResponse.addProperty("status",confirmSignUpResponse.sdkHttpResponse().statusCode());
		confirmUserResponse.addProperty("isSuccessful",confirmSignUpResponse.sdkHttpResponse().isSuccessful());
		return confirmUserResponse;
		
	}
}
