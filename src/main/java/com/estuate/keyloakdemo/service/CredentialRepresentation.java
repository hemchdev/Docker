package com.estuate.keyloakdemo.service;

// Represents the password object that Keycloak's API expects.
public class CredentialRepresentation {
    private String type;
    private String value;
    private boolean temporary;

    public static final String PASSWORD = "password";

    public CredentialRepresentation(String type, String value) {
        this.type = type;
        this.value = value;
        this.temporary = false; // So the user doesn't have to change it on first login
    }

    // Getters and Setters
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
    public String getValue() { return value; }
    public void setValue(String value) { this.value = value; }
    public boolean isTemporary() { return temporary; }
    public void setTemporary(boolean temporary) { this.temporary = temporary; }
}
