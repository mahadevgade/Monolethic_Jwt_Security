package com.avecircle.filter;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.avecircle.service.JwtService;
import com.avecircle.service.MyUserDetailsService;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class AppFilter extends OncePerRequestFilter{
	
	@Autowired
	private JwtService jwt;
	@Autowired
	private MyUserDetailsService detailsService;
	
																	/*
 TODO: JWT Token validation Filter (AppFilter.java) - OncePerRequest.class  Logic:
		- check Authorization header presence
		- retrieve bearer token from header
		- validate token
		- if token is valid, update security context to process req*/

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {		
											/*
		 TODO: Authorization Token Format:
		  			Key = Authorization 
		  			Value = Bearer <token> */
	 
		String token = request.getHeader("Authorization");
		String username=null;
		
		if (token !=null && token.startsWith("Bearer ")) {
			 token = token.substring(7);
			 username = jwt.extractUsername(token);
		}
		
	    if (username != null && SecurityContextHolder.getContext().getAuthentication() == null){
	            UserDetails userDetails = detailsService.loadUserByUsername(username);
	            if(jwt.validateToken(token, userDetails)){
	                UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
	                authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
	                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
	            }
	        }
	    
	    filterChain.doFilter(request, response);
	}

}
