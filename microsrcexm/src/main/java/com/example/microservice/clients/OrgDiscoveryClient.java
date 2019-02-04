package com.example.microservice.clients;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import com.example.microservice.model.Organization;
import java.util.List;

@Component
public class OrgDiscoveryClient{

	@Autowired
	private DiscoveryClient discoveryClient;

	 public Organization getOrganization(String organizationId) {

		System.out.println("aa");

		List<ServiceInstance> instances = discoveryClient.getInstances("MICROCONSRV");
		List<String> aa =  discoveryClient.getServices();

		System.out.println("aa"+aa.size()); 

		
		if (instances.size()==0) return null;

		
		String serviceUri = String.format("%s/v1/organizations/%s",instances.get(0).getUri().toString(), organizationId);

		System.out.println("serviceUrl = "+serviceUri);
		
		
		return null;
	 }
	
}
