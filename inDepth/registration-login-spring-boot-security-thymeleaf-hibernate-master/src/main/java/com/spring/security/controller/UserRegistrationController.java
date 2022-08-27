package com.spring.security.controller;

import com.spring.security.event.RegistrationCompleteEvent;
import com.spring.security.model.User;
import com.spring.security.service.UserService;
import com.spring.security.web.dto.UserRegistrationDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping()
public class UserRegistrationController {

	//The attributes of this class
		//userService attribute is an object that offers services to this class
		private UserService userService;
		//dependency injection of userService
		public UserRegistrationController(UserService userService) {
			super();
			this.userService = userService;
		}

		@ModelAttribute("user")
		public UserRegistrationDto userRegistrationDto(){
			return new UserRegistrationDto();
		}

		//Once the user is saved to the database we create this event that will send the user an email
		@Autowired
		private ApplicationEventPublisher publisher;

	//Controller Functions

	@GetMapping("/registration")
	public String showRegisterForm(){
		return "registration";
	}

	@PostMapping("/registration")
	public String RegisterUser(@ModelAttribute("user") UserRegistrationDto registrationDto, final HttpServletRequest request){

		User user = userService.registerUser(registrationDto);
		publisher.publishEvent(new RegistrationCompleteEvent(
				user,
				applicationUrl(request)));

		return "redirect:/registration?success";
	}

	@GetMapping("/verifyRegistration")
	public String vefifyRegistration(@RequestParam("token") String token){
		String result = userService.validateVerificationToken(token);

		if(result.equalsIgnoreCase("valid")){
			System.out.println("Validated");
			return "redirect:/login?success";
		} else if (result.equalsIgnoreCase("invalid")) {
			System.out.println("Invalid");
			return "redirect:/login?invalid";
		} else if (result.equalsIgnoreCase("expired")) {
			System.out.println("Expired");
			return "redirect:/login?expired";
		}

		return "redirect:/login?error";
	}
	private String applicationUrl(HttpServletRequest request) {
		return "http://"+request.getServerName()
				+":"+request.getServerPort()
				+request.getContextPath();
	}


}
