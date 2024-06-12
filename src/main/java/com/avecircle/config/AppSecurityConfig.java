package com.avecircle.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.avecircle.filter.AppFilter;
import com.avecircle.service.MyUserDetailsService;

@Configuration
public class AppSecurityConfig {
	
	@Autowired
	private MyUserDetailsService detailsService;
	
	@Autowired
	private AppFilter filter;
	
	@Bean
	public PasswordEncoder pwdEncoder()
	{
		return new BCryptPasswordEncoder();		
	}
	
	@Bean
	public AuthenticationProvider authenticationProvider()
	{
		DaoAuthenticationProvider authenticationProvider=new DaoAuthenticationProvider();
		authenticationProvider.setUserDetailsService(detailsService);
		authenticationProvider.setPasswordEncoder(pwdEncoder());
		return authenticationProvider;
	}
	
	
	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception
	{
		return config.getAuthenticationManager();		
	}

	
	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception
	{
													/*
	 TODO: Customize SecurityFilterChain		  
		  	- permit /api/register & /api/login urls 
		  	- authenticate any other request 
		  	- set security context as stateless   */
		
		return http.csrf().disable()
				.authorizeHttpRequests()
				.requestMatchers("/api/register","/api/login").permitAll()
				.and()
				.authorizeHttpRequests()
				.requestMatchers("/api/**")
				.authenticated()
				.and()
				.sessionManagement()
				.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
				.and()
				.authenticationProvider(authenticationProvider())
				.addFilterBefore(filter, UsernamePasswordAuthenticationFilter.class)
				.build();

	}
	
	
}
