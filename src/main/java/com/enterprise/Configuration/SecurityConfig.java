package com.enterprise.Configuration;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.*;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.enterprise.filter.JwtFilter;
import com.enterprise.services.CustomOAuth2SuccessHandler;
import com.enterprise.services.CustomOAuth2UserService;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
	
	@Autowired
	AuthenticationConfiguration authConfig;
	
	@Autowired
	UserDetailsService userDetailsService;
	
	@Autowired
	JwtFilter jwtFilter;
	
	@Autowired
	CustomOAuth2UserService oAuth2UserService;
	
	@Autowired
	CustomOAuth2SuccessHandler successHandler;
	
	
	
	@Bean
	SecurityFilterChain securityFilterChain(HttpSecurity security) throws Exception {
		return security
			   .csrf((csrf)->{
				   csrf.disable();
			   })
			   .authorizeHttpRequests((request)->{
				   request
				   .requestMatchers("/user/**","/api/**","/oAuth2/**")
				   .permitAll()

				   .anyRequest().authenticated();
			   })
			   .sessionManagement((registry)->{
				   registry.sessionCreationPolicy(SessionCreationPolicy.STATELESS);
			   })
			   .addFilterBefore(jwtFilter,UsernamePasswordAuthenticationFilter.class)
			   .oauth2Login((oAuth)->{
				   oAuth.userInfoEndpoint((userInfo)->{
				   userInfo.userService(oAuth2UserService);
				   })
				   .successHandler(successHandler);
			   })
			   
			   .build();
	}
	
	@Bean
	PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	
	@Bean
	AuthenticationManager authenticationManager() throws Exception {
		
		return authConfig.getAuthenticationManager();
	}
	
	@Bean
	AuthenticationProvider authenticationProvider() {
		// we have to validate details of Database
		DaoAuthenticationProvider provider = new DaoAuthenticationProvider(userDetailsService);
		provider.setPasswordEncoder(passwordEncoder());
		return provider;
		
	}
}
