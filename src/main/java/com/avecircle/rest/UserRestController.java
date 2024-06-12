package com.avecircle.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.avecircle.binding.AuthRequest;
import com.avecircle.entity.UserEntity;
import com.avecircle.service.JwtService;
import com.avecircle.service.MyUserDetailsService;

@RestController
@RequestMapping("/api")
public class UserRestController {

	@Autowired
	private MyUserDetailsService service;

	@Autowired
	private PasswordEncoder pwdEncoder;

	@Autowired
	private AuthenticationManager authManager;
	
	@Autowired
	private JwtService jwt;

	//TODO:  Registration functionality

	@PostMapping("/register")
	public String registerUser(@RequestBody UserEntity userEntity) {

		userEntity.setUpwd(pwdEncoder.encode(userEntity.getUpwd()));

		boolean user = service.saveUser(userEntity);
		if (user) {
			return "Registration successful...!!!";

		} else {
			return "Rgistration failled...!!!";
		}
	}

	//TODO: Login functionality

	@PostMapping("/login")
	public String userAuthentication(@RequestBody AuthRequest request) {

		UsernamePasswordAuthenticationToken token =
				new UsernamePasswordAuthenticationToken(request.getUname(),request.getPwd());
				
		try {
			Authentication auth = authManager.authenticate(token);

			if (auth.isAuthenticated()) {
				// Generate token and send to the user.
				
				// TODO: To Store token in the D/B logic is pending..
				
				String jwtToken = jwt.generateToken(request.getUname());								
				return jwtToken;
			}
		} catch (Exception e) {
			// TODO: To Write logger here.

			e.printStackTrace();
		}

		return "Authentication failled!!!";

	}

	// secured method functionality

	@GetMapping("/welcome")
	public String welcome() {
		return "Welcome to Home Page";

	}
	
	@GetMapping("/greet")
	public String greetMsg() {
		return "Good Morning...!!!";

	}

}
