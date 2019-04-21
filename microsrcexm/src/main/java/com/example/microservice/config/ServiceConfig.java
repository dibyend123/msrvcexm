package com.example.microservice.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class ServiceConfig{

        @Value("${signing.key}")
        private String jwtSigningKey="";


        public String getJwtSigningKey() {
            return jwtSigningKey;
        }

	@Value("${redis.server}")
        private String redisServer="";

        @Value("${redis.port}")
        private String redisPort="";


	public String getRedisServer(){
	    return redisServer;
	}

	public Integer getRedisPort(){
	    return new Integer( redisPort ).intValue();
	}

}
