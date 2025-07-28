package com.enterprise.models;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

//AuthResponse.java
@AllArgsConstructor
@NoArgsConstructor
public class AuthResponse {
 private String token;
 private String role;

 public AuthResponse(String token, String role) {
     this.token = token;
     this.role = role;
 }

 public String getToken() {
	return token;
 }

 @Override
public String toString() {
	return "AuthResponse [token=" + token + ", role=" + role + "]";
}

 public void setToken(String token) {
	this.token = token;
 }

 public String getRole() {
	return role;
 }

 public void setRole(String role) {
	this.role = role;
 }
}
 
 

