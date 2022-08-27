package com.spring.security.service.impl;

import com.spring.security.model.User;
import com.spring.security.model.VerificationToken;
import com.spring.security.repository.UserRepository;
import com.spring.security.repository.VerificationTokenRepository;
import com.spring.security.service.UserService;
import com.spring.security.web.dto.UserRegistrationDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Calendar;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private VerificationTokenRepository tokenRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Override
	public User registerUser(UserRegistrationDto registrationDto) {
		User user = new User();
		user.setEmail(registrationDto.getEmail());
		user.setFirstName(registrationDto.getFirstName());
		user.setLastName(registrationDto.getLastName());
		user.setRole("USER");
		user.setPassword(passwordEncoder.encode(registrationDto.getPassword()));

		userRepository.save(user);
		return user;
	}

	@Override
	public void saveVerificationTokenForUser(String token, User user) {
		VerificationToken verificationToken = new VerificationToken(token, user);

		tokenRepository.save(verificationToken);
	}

	@Override
	public String validateVerificationToken(String token) {
		VerificationToken verificationToken = tokenRepository.findByToken(token);
		if(verificationToken == null){
			return "invalid";
		}

		User user = verificationToken.getUser();

		Calendar cal = Calendar.getInstance();
		if(verificationToken.getExpirationTime().getTime() - cal.getTime().getTime() <= 0){
			tokenRepository.delete(verificationToken);
			return "expired";
		}

		user.setEnabled(true);
		userRepository.save(user);
		return "valid";
	}

	@Override
	public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
		return null;
	}
}
