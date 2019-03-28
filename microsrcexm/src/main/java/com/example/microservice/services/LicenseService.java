package com.example.microservice.services;

import com.example.microservice.config.ServiceConfig;
import com.example.microservice.model.License;
import com.example.microservice.reposiroy.LicenseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.microservice.model.Organization;
import com.example.microservice.clients.OrgDiscoveryClient;
import com.example.microservice.clients.OrgRestTemplateClient;
//import com.example.microservice.clients.OrganizationFeignClient;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;

import java.util.List;
import java.util.UUID;

@Service
public class LicenseService {

    @Autowired
    private LicenseRepository licenseRepository;

    @Autowired
    ServiceConfig config;

    @Autowired
    OrgDiscoveryClient odc;

    @Autowired
    OrgRestTemplateClient orc;

	//@Autowired
	//OrganizationFeignClient ofc;

    public License getLicense(String organizationId,String licenseId) {
        License license = licenseRepository.findByOrganizationIdAndLicenseId(organizationId, licenseId);
        return license.withComment(config.getJwtSigningKey());
    }

    @HystrixCommand(fallbackMethod = "getLicenseithClientHC",
	commandProperties={@HystrixProperty(name="execution.isolation.thread.timeoutInMilliseconds", value="10000")})	
    public License getLicenseithClient(String organizationId,String licenseId,String Client) {

	
	System.out.println("The Discovery client required = "+Client);	
	
		Organization o = getOrgByClient(organizationId,Client);

		System.out.println("id = "+o.getName());

        License license = licenseRepository.findByOrganizationIdAndLicenseId(organizationId, licenseId);
        return license.withComment(config.getJwtSigningKey());
    }

   private License getLicenseithClientHC(String organizationId,String licenseId,String Client) {
		System.out.println("Inside the Hystrix callback function");

	
	License l = new License();
	l.setLicenseMax(10);
	l.setLicenseAllocated(20);
	l.setLicenseId("aaa");
		
        return l.withComment(config.getJwtSigningKey());
    }	

	private void sleep(){
		try {
		    Thread.sleep(10000);
		} catch (InterruptedException e) {
		    e.printStackTrace();
		}
	}

	private Organization getOrgByClient(String orgId,String clienttype){

		//sleep();

		Organization od = null;
		if(clienttype.equals("od")){
			od = odc.getOrganization(orgId);
		}
		if(clienttype.equals("or")){
			od = orc.getOrganization(orgId);
		}
		/*if(clienttype.equals("fc")){
			od = ofc.getOrganization(orgId);
		}*/

		return od;	
	}

    public List<License> getLicensesByOrg(String organizationId){
        return licenseRepository.findByOrganizationId( organizationId );
    }

    public void saveLicense(License license){
        license.withId( UUID.randomUUID().toString());

        licenseRepository.save(license);

    }

    public void updateLicense(License license){
      licenseRepository.save(license);
    }

    public void deleteLicense(License license){
        licenseRepository.delete(license);
    }
}
