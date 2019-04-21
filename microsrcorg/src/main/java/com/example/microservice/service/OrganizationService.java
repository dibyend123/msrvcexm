package com.example.microservice.service;

import com.example.microservice.model.Organization;
import com.example.microservice.repository.OrganizationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.microservice.event.source.SimpleSourceBean;

import java.util.UUID;

@Service
public class OrganizationService {
    @Autowired
    private OrganizationRepository orgRepository;

    @Autowired
    SimpleSourceBean simpleSourceBean;

    public Organization getOrg(String organizationId) {
        return orgRepository.findByid(organizationId).get(0);
    }

    public void saveOrg(Organization org){
        org.setId( UUID.randomUUID().toString());

        orgRepository.save(org);
        
        simpleSourceBean.publishOrgChange("SAVE",org.getId());

    }

    public void updateOrg(Organization org){
        orgRepository.save(org);
    }

    /*public void deleteOrg(Organization org){
        orgRepository.delete( org.getId());
    }*/
}
