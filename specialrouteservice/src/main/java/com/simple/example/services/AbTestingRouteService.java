package com.simple.example.services;

import com.simple.example.repository.AbTestingRouteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.simple.example.model.AbTestingRoute;

@Service
public class AbTestingRouteService{

	@Autowired
	public AbTestingRouteRepository abtestingroute;	

	public AbTestingRoute getAbTestingRoute(String servicename){
		return abtestingroute.findByServiceName(servicename);
	}

	public void saveAbTestingRoute(AbTestingRoute abtr){
		abtestingroute.save(abtr);
	}

	public void updateAbTestingRoute(AbTestingRoute abtr){
		abtestingroute.save(abtr);
	}

	public void ubdateAbTestingRoute(AbTestingRoute abtr){
		abtestingroute.save(abtr);
	}

	public void deleteAbTestingRoute(AbTestingRoute abtr){
		abtestingroute.delete(abtr);
	}

}
