package com.estuate.keyloakdemo.service;

import com.estuate.keyloakdemo.dto.KeycloakTokenResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;

/**
 * Service for interacting with the Keycloak Admin API.
 */
@Service
public class KeycloakAdminService {

    @Value("${keycloak.admin-token-uri}")
    private String adminTokenUri;

    @Value("${keycloak.admin-cli-client-id}")
    private String adminClientId;

    @Value("${keycloak.admin-cli-client-secret}")
    private String adminClientSecret;

    @Value("${keycloak.users-uri}")
    private String usersUri;

    private final RestTemplate restTemplate = new RestTemplate();

    /**
     * Obtains an access token for the admin-cli client to manage Keycloak.
     * @return The admin access token.
     */
    private String getAdminAccessToken() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("client_id", adminClientId);
        map.add("client_secret", adminClientSecret);
        map.add("grant_type", "client_credentials");

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(map, headers);
        KeycloakTokenResponse response = restTemplate.postForObject(adminTokenUri, request, KeycloakTokenResponse.class);

        return response != null ? response.getAccessToken() : null;
    }

    /**
     * Creates a new user in Keycloak.
     * @param registrationRecord The user data to register.
     * @throws HttpClientErrorException if user creation fails (e.g., user already exists).
     */
    public void createUser(UserRegistrationRecord registrationRecord) throws HttpClientErrorException {
        String adminToken = getAdminAccessToken();
        if (adminToken == null) {
            throw new RuntimeException("Could not obtain admin token");
        }

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(adminToken);

        CredentialRepresentation credential = new CredentialRepresentation(CredentialRepresentation.PASSWORD, registrationRecord.password());

        KeycloakUserRepresentation user = new KeycloakUserRepresentation();
        user.setUsername(registrationRecord.username());
        user.setEmail(registrationRecord.email());
        user.setFirstName(registrationRecord.firstName());
        user.setLastName(registrationRecord.lastName());
        user.setCredentials(Collections.singletonList(credential));
        user.setEnabled(true);

        HttpEntity<KeycloakUserRepresentation> request = new HttpEntity<>(user, headers);

        // This will throw HttpClientErrorException on failure (e.g., 409 Conflict if user exists)
        ResponseEntity<String> response = restTemplate.exchange(usersUri, HttpMethod.POST, request, String.class);

        if (!response.getStatusCode().is2xxSuccessful()) {
            throw new RuntimeException("Failed to create user in Keycloak. Status: " + response.getStatusCode());
        }
    }
}

