package ca.uhn.fhir.jpa.starter.models;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonProperty;

public class LoginResponse {
    @JsonProperty("token")
    private String token;

    public LoginResponse(String theToken) {
        token = theToken;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String theToken) {
        token = theToken;
    }
}
