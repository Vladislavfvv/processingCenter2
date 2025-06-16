package com.edme.processingCenter.controllers;

import com.edme.processingCenter.dto.AuthDto;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.Setter;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@SecurityRequirement(name = "JWT")
@Setter
@RestController
public class AuthController {

    @Value("${client-id}")
    private String clientId;

    @Value("${resource-url}")
    private String resourceServerUrl;

    @Value("${grant-type}")
    private String grantType;


    @PostMapping("/auth")
    // public String auth(@RequestParam("username") String username, @RequestParam("password") String password) {}
    public String auth(@RequestBody AuthDto authDto) {

        var headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        var body = "client_id=" + clientId +
                   "&username=" + authDto.login() +
                   "&password=" + authDto.password()+
                   "&grant_type=" + grantType;
        var requestEntity = new HttpEntity<Object>(body, headers);
        var restTemplate = new RestTemplate();
        var responce = restTemplate.exchange(resourceServerUrl, HttpMethod.POST, requestEntity, String.class);
       if(responce.getStatusCode().value() == 200) {
           return responce.getBody();
       }
        return null;
    }
}
