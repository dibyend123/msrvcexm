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
import org.springframework.cloud.netflix.zuul.filters.ProxyRequestHelper;
import org.apache.http.client.methods.HttpPatch;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.ContentType;
import org.apache.http.HttpHost;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.entity.InputStreamEntity;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicHttpRequest;
import org.apache.http.Header;

import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.HttpResponse;
import java.net.URL;
import org.apache.http.client.HttpClient;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.InputStream;


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

	private ProxyRequestHelper helper = new ProxyRequestHelper();


    @Override
    public Object run() {
        
        System.out.print("inside the SpecialRoutesFilter class");

	RequestContext ctx = RequestContext.getCurrentContext();

	AbTestingRoute ar = new AbTestingRoute();
	ar = getAbRoutingInfo(filterUtils.getServiceId());

	System.out.println("Service id "+filterUtils.getServiceId());
	System.out.println(ar);

	System.out.println("There are following data in the special route filter");
	if(ar!=null){
			System.out.println("Active = "+ar.getActive());
	System.out.println("Service Name = "+ar.getServiceName());
	System.out.println("End point = "+ar.getEndpoint());
	System.out.println("Weight = "+ar.getWeight());
	}

        System.out.println("To check whether the Forwarding is required or not ");

	//RequestContext ctx = RequestContext.getCurrentContext();

	if(ar!=null && useSpecialRoute(ar) ){
		System.out.println(" Request Forwarding is required ");
		String route = buildRouteString(ctx.getRequest().getRequestURI(), ar.getEndpoint(),ctx.get("serviceId").toString());

		System.out.println("route = "+route);
		forwardToSpecialRoute(route);
	}

        return null;
	}


	private void forwardToSpecialRoute(String route){
		System.out.println("Forwarding the request");
		
		RequestContext context = RequestContext.getCurrentContext();
		HttpServletRequest request = context.getRequest();

		MultiValueMap<String, String> headers = this.helper.buildZuulRequestHeaders(request);
                MultiValueMap<String, String> params = this.helper.buildZuulRequestQueryParams(request);	

		String verb = getVerb(request);

		InputStream requestEntity = getRequestBody(request);
			if (request.getContentLength() < 0) {
			    context.setChunkedRequestBody();
			}

			this.helper.addIgnoredHeaders();
			CloseableHttpClient httpClient = null;
			HttpResponse response = null;
		try {
            httpClient  = HttpClients.createDefault();
            response = forward(httpClient, verb, route, request, headers,
                    params, requestEntity);
            setResponse(response);
        }
        catch (Exception ex ) {
            ex.printStackTrace();

        }
        finally{
            try {
                httpClient.close();
            }
            catch(IOException ex){}
}
		
	}

 	private String getVerb(HttpServletRequest request) {
        	String sMethod = request.getMethod();
        	return sMethod.toUpperCase();
	}
private void setResponse(HttpResponse response) throws IOException {

	System.out.println("Inside the set response method  ");
	System.out.println("response.getStatusLine().getStatusCode() = "+response.getStatusLine().getStatusCode());
	System.out.println("null or not = "+response.getEntity());
       System.out.println("x = "+response.getEntity().getContent());
        this.helper.setResponse(response.getStatusLine().getStatusCode(),
                response.getEntity() == null ? null : response.getEntity().getContent(),
                revertHeaders(response.getAllHeaders()));
}

private MultiValueMap<String, String> revertHeaders(Header[] headers) {
        MultiValueMap<String, String> map = new LinkedMultiValueMap<String, String>();
        for (Header header : headers) {
            String name = header.getName();
            if (!map.containsKey(name)) {
                map.put(name, new ArrayList<String>());
            }
            map.get(name).add(header.getValue());
        }
        return map;
}
 	
	 private InputStream getRequestBody(HttpServletRequest request) {
		InputStream requestEntity = null;
		try {
		    requestEntity = request.getInputStream();
		}catch (IOException ex) {
		    System.out.println("error in forwarding the message");
		}
		return requestEntity;
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

   public boolean useSpecialRoute(AbTestingRoute testRoute){
        Random random = new Random();

        if (testRoute.getActive().equals("N")) return false;

        int value = random.nextInt((10 - 1) + 1) + 1;

        if (testRoute.getWeight()<value) return true;

        return false;
    }

	private String buildRouteString(String oldEndpoint, String newEndpoint, String serviceName){
        	int index = oldEndpoint.indexOf(serviceName);

        	String strippedRoute = oldEndpoint.substring(index + serviceName.length());
        	System.out.println("Target route: " + String.format("%s%s", newEndpoint, strippedRoute));
        	return String.format("%s%s", newEndpoint, strippedRoute);
	}


private HttpResponse forward(HttpClient httpclient, String verb, String uri,
                                 HttpServletRequest request, MultiValueMap<String, String> headers,
                                 MultiValueMap<String, String> params, InputStream requestEntity)
            throws Exception {
        Map<String, Object> info = this.helper.debug(verb, uri, headers, params,
                requestEntity);
        URL host = new URL( uri );
        HttpHost httpHost = getHttpHost(host);

        HttpRequest httpRequest;
        int contentLength = request.getContentLength();
        InputStreamEntity entity = new InputStreamEntity(requestEntity, contentLength,
                request.getContentType() != null
                        ? ContentType.create(request.getContentType()) : null);
        switch (verb.toUpperCase()) {
            case "POST":
                HttpPost httpPost = new HttpPost(uri);
                httpRequest = httpPost;
                httpPost.setEntity(entity);
                break;
            case "PUT":
                HttpPut httpPut = new HttpPut(uri);
                httpRequest = httpPut;
                httpPut.setEntity(entity);
                break;
            case "PATCH":
                HttpPatch httpPatch = new HttpPatch(uri );
                httpRequest = httpPatch;
                httpPatch.setEntity(entity);
                break;
            default:
                httpRequest = new BasicHttpRequest(verb, uri);

        }
        try {
            httpRequest.setHeaders(convertHeaders(headers));
            HttpResponse zuulResponse = forwardRequest(httpclient, httpHost, httpRequest);

            return zuulResponse;
        }catch(Exception e){
		System.out.println("Error code ");
	}finally {
        }

	return null;
    }
private Header[] convertHeaders(MultiValueMap<String, String> headers) {
        List<Header> list = new ArrayList<>();
        for (String name : headers.keySet()) {
            for (String value : headers.get(name)) {
                list.add(new BasicHeader(name, value));
            }
        }
        return list.toArray(new BasicHeader[0]);
    }

    private HttpResponse forwardRequest(HttpClient httpclient, HttpHost httpHost,
                                        HttpRequest httpRequest) throws IOException {
        return httpclient.execute(httpHost, httpRequest);
    }

private HttpHost getHttpHost(URL host) {
        HttpHost httpHost = new HttpHost(host.getHost(), host.getPort(),
                host.getProtocol());
        return httpHost;
}

	
}
