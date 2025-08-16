package com.apnaStore.API_gateway.messages;

public interface ErrorMessages {

    String MISSING_AUTHORIZATION_HEADER = "Missing Authorization header";

    String INVALID_OR_EXPIRED_JWT_TOKEN="Invalid or expired JWT token";

    String ACCESS_DENIED_FOR_ROLE="Access Denied For Role";

    String INVALID_JWT_TOKEN="Invalid JWT token";
}
