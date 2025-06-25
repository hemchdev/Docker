package com.estuate.keyloakdemo.service;

import com.estuate.keyloakdemo.dto.KeycloakTokenResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

/**
 * Service to handle direct authentication with Keycloak using the Password Grant flow.
 */
@Service
public class KeycloakAuthService {

    @Value("${keycloak.token-uri}")
    private String keycloakTokenUri;

    @Value("${keycloak.client-id}")
    private String clientId;

    @Value("${keycloak.client-secret}")
    private String clientSecret;

    private final RestTemplate restTemplate = new RestTemplate();

    /**
     * Authenticates a user with Keycloak and retrieves access tokens.
     * @param username The user's username.
     * @param password The user's password.
     * @return A KeycloakTokenResponse containing the tokens.
     */
    public KeycloakTokenResponse authenticate(String username, String password) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("client_id", clientId);
        map.add("client_secret", clientSecret);
        map.add("grant_type", "password");
        map.add("username", username);
        map.add("password", password);

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(map, headers);

        // This will throw an exception for non-2xx responses (e.g., 401 Unauthorized for bad credentials)
        return restTemplate.postForObject(keycloakTokenUri, request, KeycloakTokenResponse.class);
    }
}
