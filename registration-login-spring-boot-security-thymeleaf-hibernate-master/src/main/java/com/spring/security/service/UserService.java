package com.spring.security.service;

import org.springframework.security.core.userdetails.UserDetailsService;

import com.spring.security.model.User;
import com.spring.security.web.dto.UserRegistrationDto;

public interface UserService extends UserDetailsService{
	User save(UserRegistrationDto registrationDto);
}
