package com.simple.example.repository;


import com.simple.example.model.UserOrganization;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrgUserRepository extends CrudRepository<UserOrganization,String>{

	public UserOrganization findByUserName(String userName);
}
