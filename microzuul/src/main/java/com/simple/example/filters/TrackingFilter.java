package com.simple.example.filters;

import com.netflix.zuul.ZuulFilter;
import org.springframework.beans.factory.annotation.Autowired;
import com.netflix.zuul.context.RequestContext;
import org.springframework.stereotype.Component;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;

@Component
public class TrackingFilter extends ZuulFilter{

        private static final int      FILTER_ORDER =  2;
        private static final boolean SHOULD_FILTER = true;

	 @Autowired
         FilterUtils filterUtils;


	@Override
        public String filterType() {
             return FilterUtils.PRE_FILTER_TYPE;
        }

        @Override
        public int filterOrder() {
             return FILTER_ORDER;
        }

        public boolean shouldFilter() {
             return SHOULD_FILTER;
        }
         
        private boolean isCorrelationIdPresent(){
      		if (filterUtils.getCorrelationId() !=null){
          		return true;
      		}

             return false;
       } 

	private String generateCorrelationId(){
		return java.util.UUID.randomUUID().toString();
	}

	private String getOrganizationId(){

		String result="";
		if (filterUtils.getAuthToken()!=null){

		    String authToken = filterUtils.getAuthToken().replace("Bearer ","");
		    try {
		        Claims claims = Jwts.parser()
		                .setSigningKey("345345fsdfsf5345".getBytes("UTF-8"))
		                .parseClaimsJws(authToken).getBody();
		        result = (String) claims.get("organizationId");
		    }
		    catch (Exception e){
		        e.printStackTrace();
		    }
		}
		return result;
}

       public Object run() {

		String crid = generateCorrelationId();
        	if (isCorrelationIdPresent()) {
			System.out.println("The Correlation id is already present = "+filterUtils.getCorrelationId());                     
                }
                else{
                       System.out.println("The Correlation id is not present"); 
		       System.out.println("The Correlation which is going to be used = "+crid);	
		       filterUtils.setCorrelationId(crid);
                       
                }

                RequestContext ctx = RequestContext.getCurrentContext();
		System.out.println("Processing incoming request for {} = "+  ctx.getRequest().getRequestURI());
		System.out.println("The Organisationid = "+getOrganizationId());   
                return null;
       }
	
}
