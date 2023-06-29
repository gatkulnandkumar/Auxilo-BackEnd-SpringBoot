package com.nucSoft.web.spring.controller;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nucSoft.web.spring.model.ERole;
import com.nucSoft.web.spring.model.Role;
import com.nucSoft.web.spring.model.User;
import com.nucSoft.web.spring.payload.request.JwtResponse;
import com.nucSoft.web.spring.payload.request.LoginRequest;
import com.nucSoft.web.spring.payload.request.SignupRequest;
import com.nucSoft.web.spring.payload.response.MessageResponse;
import com.nucSoft.web.spring.security.jwt.JwtUtils;
import com.nucSoft.web.spring.security.services.UserDetailsImpl;
import com.nucSoft.web.spring.serviceRepository.RoleRepository;
import com.nucSoft.web.spring.serviceRepository.UserRepository;

@CrossOrigin(origins = "**", maxAge = 3600)
@RestController
@RequestMapping("/api/auth")
public class AuthController {
	@Autowired
	AuthenticationManager authenticationManager;

	@Autowired
	UserRepository userRepository;

	@Autowired
	RoleRepository roleRepository;

	@Autowired
	PasswordEncoder encoder;

	@Autowired
	JwtUtils jwtUtils;
	
	@PersistenceContext
	private EntityManager entityManager;

	@PostMapping("/signin")
	@Transactional
	public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
		Authentication authentication = authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

		SecurityContextHolder.getContext().setAuthentication(authentication);

		UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

		User user = userRepository.findByUsername(loginRequest.getUsername())
				.orElseThrow(() -> new RuntimeException("User not found"));

		user.setLoginTime(LocalDateTime.now());
		//userRepository.save(user);
	
	    entityManager.merge(user);

		String jwtToken = jwtUtils.generateToken(authentication);

		
		  List<String> roles = userDetails.getAuthorities().stream().map(item ->
		  item.getAuthority()) .collect(Collectors.toList());
		 

		return ResponseEntity.ok(new JwtResponse(jwtToken));
	}

	@PostMapping("/signup")
	@Transactional
	public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signUpRequest) {
		if (userRepository.existsByUsername(signUpRequest.getUsername())) {
			return ResponseEntity.badRequest().body(new MessageResponse("Error: Username is already taken!"));
		}

		if (userRepository.existsByEmail(signUpRequest.getEmail())) {
			return ResponseEntity.badRequest().body(new MessageResponse("Error: Email is already in use!"));
		}

		User user = new User(signUpRequest.getName(), signUpRequest.getUsername(), signUpRequest.getEmail(),
				encoder.encode(signUpRequest.getPassword()), signUpRequest.getContact_no());

		String strRoles = signUpRequest.getRole();
		Set<Role> roles = new HashSet<>();

		if (strRoles == null || strRoles.isEmpty()) {
			Role userRole = roleRepository.findByName(ERole.NORMAL)
					.orElseThrow(() -> new RuntimeException("Error: Role is not found."));
			roles.add(userRole);
		} else {
			switch (strRoles) {
			case "Admin":
				Role adminRole = roleRepository.findByName(ERole.ADMIN)
						.orElseThrow(() -> new RuntimeException("Error: Role is not found."));
				roles.add(adminRole);
				break;
			default:
				Role userRole = roleRepository.findByName(ERole.NORMAL)
						.orElseThrow(() -> new RuntimeException("Error: Role is not found."));
				roles.add(userRole);

			}
		}

		user.setRoles(roles);
		//userRepository.save(user);
		  entityManager.persist(user);

		return ResponseEntity.ok(new MessageResponse("User registered successfully!"));
	}

	@PostMapping("/logout/{username}")
	@Transactional
	public ResponseEntity<String> logout(@PathVariable String username) {

		User user = userRepository.findByUsername(username).orElseThrow(() -> new RuntimeException("User not found"));
		user.setLogoutTime(LocalDateTime.now());
		//userRepository.save(user);
		  entityManager.persist(user);

		return ResponseEntity.ok("Logout successful");
	}

	@GetMapping("/users/{username}")
	public Optional<User> getUserByUsername(@PathVariable String username) {

		return userRepository.findByUsername(username).filter(null);
	}

}
