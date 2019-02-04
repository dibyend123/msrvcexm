package com.example.microservice.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseStatus;
import com.example.microservice.model.License;
import com.example.microservice.config.ServiceConfig;
import com.example.microservice.services.LicenseService;


@RestController
@RequestMapping(value="v1/organizations/{organizationId}/licenses")
public class LicenseServiceController{

    @Autowired
    private LicenseService licenseService;

    @Autowired
    private ServiceConfig serviceConfig;
	
	
	@RequestMapping(value="/{licenseId}",method = RequestMethod.GET)
    	public License getLicenses( @PathVariable("organizationId") String organizationId,
                                @PathVariable("licenseId") String licenseId) {

		


		return licenseService.getLicense(organizationId,licenseId);
		/*return new License()
		    .withId(licenseId)
		    .withOrganizationId(organizationId)
		    .withProductName("Teleco")
		    .withLicenseType("Seat");*/
	}

        @RequestMapping(value="/{licenseId}/{clientId}",method = RequestMethod.GET)
    	public License getLicensesWithClient( @PathVariable("organizationId") String organizationId,
                                @PathVariable("licenseId") String licenseId,@PathVariable("clientId") String clientId) {

		System.out.println("The Client which needs to be called  = "+clientId);
		

		return licenseService.getLicenseithClient(organizationId,licenseId,clientId);
		/*return new License()
		    .withId(licenseId)
		    .withOrganizationId(organizationId)
		    .withProductName("Teleco")
		    .withLicenseType("Seat");*/
	}

	
	
	@RequestMapping(value="{licenseId}",method = RequestMethod.PUT)
        public String updateLicenses( @PathVariable("licenseId") String licenseId) {
            	return String.format("This is the put");
        }

        @RequestMapping(value="{licenseId}",method = RequestMethod.POST)
    	public String saveLicenses( @PathVariable("licenseId") String licenseId) {
        	return String.format("This is the post");
    	}

    	@RequestMapping(value="{licenseId}",method = RequestMethod.DELETE)
    	@ResponseStatus(HttpStatus.NO_CONTENT)
    	public String deleteLicenses( @PathVariable("licenseId") String licenseId) {
        	return String.format("This is the Delete");
	}

}
