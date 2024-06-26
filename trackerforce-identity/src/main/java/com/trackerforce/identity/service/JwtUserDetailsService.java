package com.trackerforce.identity.service;

import com.trackerforce.identity.model.AuthAccess;
import com.trackerforce.identity.model.dto.request.JwtRequestDTO;
import com.trackerforce.identity.repository.AuthAccessRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class JwtUserDetailsService implements UserDetailsService {

	private final AuthAccessRepository authAccessRepository;

	private final PasswordEncoder bcryptEncoder;

	public JwtUserDetailsService(AuthAccessRepository authAccessRepository, PasswordEncoder bcryptEncoder) {
		this.authAccessRepository = authAccessRepository;
		this.bcryptEncoder = bcryptEncoder;
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		final var user = authAccessRepository.findByEmail(username);
		
		if (user == null)
			throw new UsernameNotFoundException("User not found with username: " + username);
			
		return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(),
				new ArrayList<>());
	}

	public AuthAccess newUser(AuthAccess user) {
		final var jwt = new JwtRequestDTO(user.getEmail(), bcryptEncoder.encode(user.getPassword()));
		final var newUser = new AuthAccess(jwt, user.getOrganization());
		return authAccessRepository.save(newUser);
	}
}