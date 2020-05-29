package ca.uhn.fhir.jpa.starter.controller;

import ca.uhn.fhir.context.ConfigurationException;
import ca.uhn.fhir.jpa.starter.models.LoginRequest;
import ca.uhn.fhir.jpa.starter.models.LoginResponse;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException;
import org.thymeleaf.util.StringUtils;

import javax.servlet.http.HttpServletResponse;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
public class DummyAuthController {

    @Value("${clinician.username:}")
    private String username;

    @Value("${clinician.password:}")
    private String password;

    @Value("${clinician.token:}")
    private String token;

    @RequestMapping(value = "/login", consumes="application/json", produces={APPLICATION_JSON_VALUE}, method = RequestMethod.POST)
    public LoginResponse login(@RequestBody LoginRequest loginRequest, HttpServletResponse response) {
        if (credentialsMatch(loginRequest)) {
            response.setStatus(200);
            return new LoginResponse(token);
        } else {
            response.setStatus(401);
            return null;
        }
    }
    private boolean credentialsMatch(LoginRequest theLoginRequest) {
        if (StringUtils.isEmpty(username) || StringUtils.isEmpty(password) || StringUtils.isEmpty(token)) {
            throw new InternalErrorException("This server does not have clinician credentials setup!");
        }
        return StringUtils.equals(username, theLoginRequest.getUsername())
                && StringUtils.equals(password, theLoginRequest.getPassword());
    }
}
