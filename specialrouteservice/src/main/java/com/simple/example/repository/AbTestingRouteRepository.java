package com.simple.example.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import com.simple.example.model.AbTestingRoute;

@Repository
public interface AbTestingRouteRepository extends CrudRepository<AbTestingRoute,String> {

	public AbTestingRoute findByServiceName(String serviceName);
}
