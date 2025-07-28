package com.enterprise.services;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;

import org.springframework.stereotype.Service;

import com.enterprise.models.AuthResponse;
import com.enterprise.models.UserDTO;
import com.enterprise.models.Users;
import com.enterprise.repositories.UserRepository;
import java.util.*;

@Service
public class UserService {
	@Autowired
	UserRepository repo;
	
	@Autowired
	JWTService jwtService;
	
	@Autowired
	AuthenticationManager authManager;
	
	@Autowired
	PasswordEncoder passwordEncoder;

	public Users registerUser(Users user) {
		
		String password = user.getPassword();
		String encodedPassword = passwordEncoder.encode(password);
		
		user.setPassword(encodedPassword);
		List<String> list = user.getRoles();
		
		List<String> roles = new ArrayList<>();
		roles.add("ROLE_USER");
		if(list.get(0).toUpperCase().equals("ADMIN")) {
			roles.add("ROLE_ADMIN");
		}
		user.setRoles(roles);
		return repo.save(user);
	}

	public AuthResponse verifyDetails(UserDTO user) {
		
		UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(user.getUsername(),user.getPassword());
		Authentication authentication = authManager.authenticate(authToken);
		Users DBuser = repo.findByUsername(user.getUsername());
		// delegates to authProvider there it has encoder and userDetailsService verifies
		
		if(authentication.isAuthenticated()) {
			String token = jwtService.generateToken(DBuser);
			String role = "USER";
			if(DBuser.getRoles().contains("ROLE_ADMIN")) {
				role = "ADMIN";
			}
			return new AuthResponse(token,role);
		}
		throw new RuntimeException("Error in Generating Token");
		
		
	}
	
	
}
