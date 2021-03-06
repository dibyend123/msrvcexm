package com.simple.example.filters;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import java.util.Collections;
import java.util.List;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;



@SpringBootApplication
@EnableZuulProxy
public class Application{

	@LoadBalanced
        @Bean
        public RestTemplate getRestTemplate(){
		RestTemplate template = new RestTemplate();

		return template;
        }

	public static void main(String[] args) {
        	SpringApplication.run(Application.class, args);
	}

	
}
