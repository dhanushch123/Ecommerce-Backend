package com.enterprise.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Component;
import java.util.*;

import com.enterprise.models.Gender;
import com.enterprise.models.Users;
import com.enterprise.repositories.UserRepository;

@Component
public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User>{

	@Autowired
	UserRepository repo;
	
	@Override
	public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
		
		System.out.println("In loadUser method");
		
		OAuth2UserService<OAuth2UserRequest, OAuth2User> delegate = new DefaultOAuth2UserService();
		
		OAuth2User oAuth2User = delegate.loadUser(userRequest);
		
		// Extracting Attributes
		String email = oAuth2User.getAttribute("email");
		String name = oAuth2User.getAttribute("name");
		String gender = oAuth2User.getAttribute("gender");
		
		System.out.println("Email : " + email);
		System.out.println("name : " + name);
		System.out.println("gender : " + gender);
		
		Users user = repo.findByEmail(email);
		
		
		Gender genderEnum = null;
		if (gender != null) {
		    try {
		        genderEnum = Gender.valueOf(gender.toUpperCase());
		    } catch (IllegalArgumentException e) {
		        genderEnum = Gender.OTHER;
		    }
		}
		System.out.println("In OAuth2 Service");
		if(user == null) {
			// we store these details in db
			Users newUser = new Users();
			newUser.setEmail(email);
			newUser.setName(name);
			if(gender!=null)newUser.setGender(genderEnum);
			List<String> roles = new ArrayList<>();
			roles.add("ROLE_USER");
			newUser.setRoles(roles);
			newUser.setUsername(name);
			repo.save(newUser);
			System.out.println(newUser);
			
				
		}
		
		
	
		
		return oAuth2User;
	}
		
	
}

