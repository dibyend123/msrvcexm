package com.example.microservice.services;

import com.example.microservice.config.ServiceConfig;
import com.example.microservice.model.License;
import com.example.microservice.reposiroy.LicenseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.microservice.model.Organization;
import com.example.microservice.clients.OrgDiscoveryClient;

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

    public License getLicense(String organizationId,String licenseId) {
        License license = licenseRepository.findByOrganizationIdAndLicenseId(organizationId, licenseId);
        return license.withComment(config.getExampleProperty());
    }

    public License getLicenseithClient(String organizationId,String licenseId,String Client) {
	System.out.println("The Discovery client required = "+Client);	
	
		Organization o = getOrgByClient(organizationId,Client);

		System.out.println("id = "+o.getName());

        License license = licenseRepository.findByOrganizationIdAndLicenseId(organizationId, licenseId);
        return license.withComment(config.getExampleProperty());
    }	

	private Organization getOrgByClient(String orgId,String clienttype){
		Organization od = null;
		if(clienttype.equals("od")){
			od = odc.getOrganization(orgId);
		}

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
