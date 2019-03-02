package com.example.microservice.clients;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import com.example.microservice.model.Organization;

@Component
public class OrgRestTemplateClient{

@Autowired
RestTemplate restTemplate;

    public Organization getOrganization(String organizationId){
		System.out.println("Inside the getOrganization of the restTemplate"+restTemplate);
        ResponseEntity<Organization> restExchange =
                restTemplate.exchange(
                        "http://microsrcorg/v1/organizations/{organizationId}",
                        HttpMethod.GET,
                        null, Organization.class, organizationId);

        return restExchange.getBody();
}

	
}

