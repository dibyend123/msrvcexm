package com.simple.example.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;

@Configuration
public class WebSecurityConfigurer extends WebSecurityConfigurerAdapter {


    /*@Bean
    public PasswordEncoder passwordEncoder() {
        return new NoOpPasswordEncoder();
    }*/
    
    @Override
    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {

	System.out.println("Testing checkaa");
        return super.authenticationManagerBean();
    }

    @Override
    @Bean
    public UserDetailsService userDetailsServiceBean() throws Exception {
System.out.println("Testing checkbb");

		//System.out.println(super.userDetailsServiceBean().loadUserByUsername("tommy"));
        return super.userDetailsServiceBean();
    }

    //@Autowired
   // PasswordEncoder passwordEncoder;


    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {

	System.out.println("Testing check");
        auth
                .inMemoryAuthentication()
		.passwordEncoder(NoOpPasswordEncoder.getInstance())
                .withUser("tommy").password("password1").roles("USER")
		//.withUser("tommy").password(NoOpPasswordEncoder.getInstance().encode("password1")).roles("USER")
                .and()
                .withUser("intense").password("password2").roles("USER", "ADMIN");
		//.withUser("intense").password(NoOpPasswordEncoder.getInstance().encode("password2")).roles("USER", "ADMIN");


	



    }
}
