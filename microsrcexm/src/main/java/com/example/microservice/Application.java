package com.example.microservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
//import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.web.client.RestTemplate;
//import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.stream.annotation.StreamListener;
import java.util.Collections;
import java.util.List;
import com.example.microservice.utils.*;

import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.context.annotation.Bean;
import javax.servlet.Filter;
import org.springframework.security.oauth2.client.OAuth2ClientContext;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.client.resource.OAuth2ProtectedResourceDetails;
import org.springframework.security.oauth2.client.DefaultOAuth2ClientContext;
import org.springframework.security.oauth2.client.resource.OAuth2ProtectedResourceDetails;
import org.springframework.security.oauth2.client.token.grant.code.AuthorizationCodeResourceDetails;
import org.springframework.security.oauth2.client.resource.BaseOAuth2ProtectedResourceDetails;

import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.messaging.Sink;
import com.example.microservice.events.model.OrganizationChangeModel;
import com.example.microservice.config.ServiceConfig;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.beans.factory.annotation.Autowired;


@SpringBootApplication
//@EnableDiscoveryClient
//@EnableEurekaClient
//@EnableCircuitBreaker;
//@EnableFeignClients
@EnableBinding(Sink.class)
@EnableResourceServer
public class Application{

    @Autowired
    private ServiceConfig serviceConfig;

	@Bean
        public Filter userContextFilter() {
            UserContextFilter userContextFilter = new UserContextFilter();
            return userContextFilter;
        }



/*@Bean
public OAuth2ClientContext oauth2ClientContext() {
    DefaultOAuth2ClientContext defaultOAuth2ClientContext = new DefaultOAuth2ClientContext();
    return defaultOAuth2ClientContext;
}*/

/*@Bean
public OAuth2ProtectedResourceDetails oAuth2ProtectedResourceDetails() {
		
	BaseOAuth2ProtectedResourceDetails bo = new BaseOAuth2ProtectedResourceDetails();
	//bo.isClientOnly();
	return bo;
}

    @Bean
    public OAuth2RestTemplate oauth2RestTemplate(OAuth2ClientContext oauth2ClientContext,
                                                 OAuth2ProtectedResourceDetails details) {
        return new OAuth2RestTemplate(details, oauth2ClientContext);
    }*/


    @LoadBalanced
    @Bean
    public RestTemplate getRestTemplate(){
	System.out.println("Inside the getRestTemplate Class ");
        RestTemplate template = new RestTemplate();
        List interceptors = template.getInterceptors();
        if (interceptors==null){
            template.setInterceptors(Collections.singletonList(new UserContextInterceptor()));
        }
        else{
            interceptors.add(new UserContextInterceptor());
            template.setInterceptors(interceptors);
        }

        return template;
   }

    @Bean
    public JedisConnectionFactory jedisConnectionFactory() {
        JedisConnectionFactory jedisConnFactory = new JedisConnectionFactory();
        jedisConnFactory.setHostName( serviceConfig.getRedisServer());
        jedisConnFactory.setPort( serviceConfig.getRedisPort() );
        return jedisConnFactory;
    }

    @Bean
    public RedisTemplate<String, Object> redisTemplate() {
        RedisTemplate<String, Object> template = new RedisTemplate<String, Object>();
        template.setConnectionFactory(jedisConnectionFactory());
        return template;
   }
	
  @StreamListener(Sink.INPUT)
   public void loggerSink(OrganizationChangeModel orgChange) {
	System.out.println("Received an event for organization id {}"+ orgChange.getOrganizationId());
      // logger.debug("Received an event for organization id {}", orgChange.getOrganizationId());
   }

public static void main(String[] args) {
        	SpringApplication.run(Application.class, args);
	}
}
