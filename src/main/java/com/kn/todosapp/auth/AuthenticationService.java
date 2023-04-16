package com.kn.todosapp.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.kn.todosapp.jwt.JwtService;
import com.kn.todosapp.user.Role;
import com.kn.todosapp.user.User;
import com.kn.todosapp.user.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
	@Autowired
	private final UserRepository userRepository;
	@Autowired
	private final PasswordEncoder passwordEncoder;
	@Autowired
	private final JwtService jwtService;
	@Autowired
	private final AuthenticationManager authenticationManager;
	
	public String register(RegisterRequest request) {
		// TODO Auto-generated method stub
		if(userRepository.findByUsername(request.getUsername()).isPresent()) {
			return "Conflict";
		}
		var user = User.builder()
				.firstname(request.getFirstname())
				.lastname(request.getLastname())
				.username(request.getUsername())
				.password(passwordEncoder.encode(request.getPassword()))
				.role(Role.USER)
				.build();
		userRepository.save(user);
		return "Registered";
	}

	public AuthenticationResponse authenticate(AuthenticationRequest request) {
		// TODO Auto-generated method stub
		authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
		);
		
		var user = userRepository.findByUsername(request.getUsername()).orElseThrow();
		var token = jwtService.generateToken(user);
		return AuthenticationResponse.builder().token(token).build();
	}

}
