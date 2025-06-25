package com.estuate.keyloakdemo.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * A DTO (Data Transfer Object) to map the JSON response from Keycloak's token endpoint.
 */
public class KeycloakTokenResponse {

    @JsonProperty("access_token")
    private String accessToken;

    @JsonProperty("expires_in")
    private int expiresIn;

    @JsonProperty("refresh_expires_in")
    private int refreshExpiresIn;

    @JsonProperty("refresh_token")
    private String refreshToken;

    @JsonProperty("token_type")
    private String tokenType;

    // Getters and Setters for all fields...
    public String getAccessToken() { return accessToken; }
    public void setAccessToken(String accessToken) { this.accessToken = accessToken; }
    public int getExpiresIn() { return expiresIn; }
    public void setExpiresIn(int expiresIn) { this.expiresIn = expiresIn; }
    public int getRefreshExpiresIn() { return refreshExpiresIn; }
    public void setRefreshExpiresIn(int refreshExpiresIn) { this.refreshExpiresIn = refreshExpiresIn; }
    public String getRefreshToken() { return refreshToken; }
    public void setRefreshToken(String refreshToken) { this.refreshToken = refreshToken; }
    public String getTokenType() { return tokenType; }
    public void setTokenType(String tokenType) { this.tokenType = tokenType; }
}

