package com.example.microservice.clients;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import com.example.microservice.model.Organization;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import com.example.microservice.reposiroy.OrganizationRedisRepository;

@Component
public class OrgRestTemplateClient{

@Autowired
RestTemplate restTemplate;

// @Autowired
//OAuth2RestTemplate restTemplate;


@Autowired
OrganizationRedisRepository orgRedisRepo;


private Organization checkRedisCache(String organizationId) {
        try {
            return orgRedisRepo.findOrganization(organizationId);
        }
        catch (Exception ex){
          ex.printStackTrace();
            return null;
        }
    }

    private void cacheOrganizationObject(Organization org) {
        try {
            orgRedisRepo.saveOrganization(org);
        }catch (Exception ex){
           // logger.error("Unable to cache organization {} in Redis. Exception {}", org.getId(), ex);
	ex.printStackTrace();
        }
}

    public Organization getOrganization(String organizationId){

		Organization org = checkRedisCache(organizationId);

        	if (org!=null){
            		System.out.println("Succesfully retrieved the value froom the redis cache");
            		return org;
        	}

		System.out.println("Unable to retrieve the value from the redis cache");




		System.out.println("Inside the getOrganization of the restTemplate"+restTemplate);
        ResponseEntity<Organization> restExchange =
                restTemplate.exchange(
                        "http://localhost:8002/v1/organizations/{organizationId}",
                        HttpMethod.GET,
                        null, Organization.class, organizationId);


	org = restExchange.getBody();

		if (org!=null) {
			    cacheOrganizationObject(org);
		}

        return org;
}

	
}

