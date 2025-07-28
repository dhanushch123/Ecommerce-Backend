package com.enterprise.filter;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.enterprise.services.JWTService;
import com.enterprise.services.MyUserDetailsService;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtFilter extends OncePerRequestFilter{

	
	JWTService jwtService;
	MyUserDetailsService myUserDetailsService;
	
	public JwtFilter(JWTService jwtService,MyUserDetailsService myUserDetailsService) {
		this.jwtService = jwtService;
		this.myUserDetailsService = myUserDetailsService;
	}
	
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		String authHeader = request.getHeader("Authorization");
		String username  = null;
		String token = null;
		
		if(authHeader!=null && authHeader.startsWith("Bearer ")) {
			token = authHeader.split(" ")[1];
			username = jwtService.extractUsername(token);
		}
		
		// we got username this executes before UsernamePasswordAuthenticationFilter
		if(username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
			UserDetails user = myUserDetailsService.loadUserByUsername(username);
			if(jwtService.validateToken(token,user)) {
				UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(user,null,user.getAuthorities());
				authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
				SecurityContextHolder.getContext().setAuthentication(authToken);
			}
		}
		filterChain.doFilter(request, response);
	}

}
