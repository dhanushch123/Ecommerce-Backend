package com.enterprise.services;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import com.enterprise.models.Users;
import com.enterprise.repositories.UserRepository;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class CustomOAuth2SuccessHandler implements AuthenticationSuccessHandler{
	
	
	@Autowired
	JWTService jwtService;
	
	@Autowired
	UserRepository repo;
	
	@Value("${frontend.redirect.url}")
	private String redirectUrl;
	

	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
			Authentication authentication) throws IOException, ServletException {
		
		OAuth2User authUser = (OAuth2User) authentication.getPrincipal();
		
		String email = authUser.getAttribute("email");
		Users user = repo.findByEmail(email);
		String role = "USER";
		if(user.getRoles().contains("ROLE_ADMIN")) {
			role = "ADMIN";
		}
		String token = jwtService.generateToken(user);
		 String redirectWithToken = UriComponentsBuilder.fromUriString(redirectUrl)
	                .queryParam("token", token)
	                .queryParam("role",role)
	                .build().toUriString();

	        response.sendRedirect(redirectWithToken);
		
	}
}


  
