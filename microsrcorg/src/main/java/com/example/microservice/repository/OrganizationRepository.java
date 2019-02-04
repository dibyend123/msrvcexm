package com.example.microservice.repository;

import com.example.microservice.model.Organization;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrganizationRepository extends CrudRepository<Organization,String>  {
     public List<Organization> findByid(String organizationId);
}
