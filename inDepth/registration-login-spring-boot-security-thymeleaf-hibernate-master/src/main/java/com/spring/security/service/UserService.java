package com.spring.security.service;

import com.spring.security.model.User;
import org.springframework.security.core.userdetails.UserDetailsService;

import com.spring.security.web.dto.UserRegistrationDto;

public interface UserService extends UserDetailsService{

	User registerUser(UserRegistrationDto registrationDto);

	void saveVerificationTokenForUser(String token, User user);

	String validateVerificationToken(String token);
}
