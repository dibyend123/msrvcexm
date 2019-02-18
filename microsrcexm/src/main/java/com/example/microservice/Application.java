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
import java.util.Collections;
import java.util.List;
import com.example.microservice.utils.*;

@SpringBootApplication
@EnableDiscoveryClient
@EnableCircuitBreaker
//@EnableFeignClients
public class Application{


    @LoadBalanced
    @Bean
    public RestTemplate getRestTemplate(){
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
	public static void main(String[] args) {
        	SpringApplication.run(Application.class, args);
	}
}
