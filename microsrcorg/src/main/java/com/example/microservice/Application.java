package com.example.microservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.context.annotation.Bean;
import javax.servlet.Filter;
//import com.thoughtmechanix.organization.utils.UserContextFilter;
import com.example.microservice.utils.UserContextFilter;


@SpringBootApplication
@EnableEurekaClient
@EnableResourceServer
public class Application{

	@Bean
        public Filter userContextFilter() {
            UserContextFilter userContextFilter = new UserContextFilter();
            return userContextFilter;
        }

	public static void main(String[] args) {
        	SpringApplication.run(Application.class, args);
	}


}
