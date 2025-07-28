package com.enterprise.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.enterprise.models.AuthResponse;
import com.enterprise.models.UserDTO;
import com.enterprise.models.Users;
import com.enterprise.services.UserService;

@RestController
@RequestMapping("/user")
public class UserController {
	
	@Autowired
	UserService service;
	
	@PostMapping("/login")
	public ResponseEntity<?> login(@RequestBody UserDTO user){
		try {
		 AuthResponse response  = service.verifyDetails(user);
		 return ResponseEntity.ok(response);
		}
		catch(Exception e) {
			return new ResponseEntity<>(e.getLocalizedMessage(),HttpStatus.BAD_REQUEST);
		}
	}
	
	@PostMapping("/register")
	public ResponseEntity<?> register(@RequestBody Users user){
		System.out.println("In register method");
		try {
			Users registeredUser = service.registerUser(user);
			if(registeredUser != null) {
				return new ResponseEntity<>("User registered Successfully",HttpStatus.CREATED);
			}
			return new ResponseEntity<>("InValid Details",HttpStatus.BAD_REQUEST);
		}
		catch(Exception e) {
			return new ResponseEntity<>(e.getLocalizedMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	
	
}
