package com.example.microservice.reposiroy;

import com.example.microservice.model.Organization;

public interface OrganizationRedisRepository{
	
	void saveOrganization(Organization org);
        void updateOrganization(Organization org);
        void deleteOrganization(String organizationId);
        Organization findOrganization(String organizationId);
}
