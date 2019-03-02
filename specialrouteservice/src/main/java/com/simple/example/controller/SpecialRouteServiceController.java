package com.simple.example.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.PathVariable;
import com.simple.example.services.AbTestingRouteService;
import com.simple.example.model.AbTestingRoute;


@RestController
@RequestMapping(value="v1/route/")
public class SpecialRouteServiceController{

    @Autowired
    AbTestingRouteService abtestrouteSrvc;	

    @RequestMapping(value="abtesting/{serviceName}",method = RequestMethod.GET)
    public AbTestingRoute abstestings(@PathVariable("serviceName") String serviceName) {
	
	System.out.println(" Inside the SpecialRouteServiceController class ");
	

		
        return abtestrouteSrvc.getAbTestingRoute(serviceName);
    }
	
	
}
