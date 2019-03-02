package com.simple.example.filters;

import com.netflix.zuul.ZuulFilter;
import org.springframework.beans.factory.annotation.Autowired;
import com.netflix.zuul.context.RequestContext;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import com.netflix.zuul.context.RequestContext;
import com.simple.example.model.AbTestingRoute;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;

@Component
public class SpecialRoutesFilter extends ZuulFilter{

    private static final int FILTER_ORDER =  1;
    private static final boolean SHOULD_FILTER =true;

    @Autowired
    FilterUtils filterUtils;

    @Autowired
    RestTemplate restTemplate;

    @Override
    public String filterType() {
        return filterUtils.ROUTE_FILTER_TYPE;
    }

    @Override
    public int filterOrder() {
        return FILTER_ORDER;
    }

    @Override
    public boolean shouldFilter() {
        return SHOULD_FILTER;
	}


    @Override
    public Object run() {
        
        System.out.print("inside the SpecialRoutesFilter class");

	RequestContext ctx = RequestContext.getCurrentContext();

	System.out.println("Service id "+filterUtils.getServiceId());
	System.out.println(getAbRoutingInfo(filterUtils.getServiceId()));
        return null;
	}


   private AbTestingRoute getAbRoutingInfo(String serviceName){
        ResponseEntity<AbTestingRoute> restExchange = null;
        try {
            restExchange = restTemplate.exchange(
			     "http://specialrouteservice/v1/route/abtesting/{serviceName}",
                             HttpMethod.GET,
                             null, AbTestingRoute.class, serviceName);
        }
        catch(HttpClientErrorException ex){
            if (ex.getStatusCode()== HttpStatus.NOT_FOUND) return null;
            throw ex;
        }
        return restExchange.getBody();
}


	
}
