package com.trackerforce.identity.service;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.trackerforce.identity.model.AuthAccess;
import com.trackerforce.identity.model.request.JwtRequest;
import com.trackerforce.identity.repository.AuthAccessRepository;

@Service
public class JwtUserDetailsService implements UserDetailsService {

	@Autowired
	private AuthAccessRepository authAccessRepository;

	@Autowired
	private PasswordEncoder bcryptEncoder;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		final var user = authAccessRepository.findByUsername(username);
		
		if (user == null)
			throw new UsernameNotFoundException("User not found with username: " + username);
			
		return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(),
				new ArrayList<>());
	}

	public AuthAccess newUser(AuthAccess user) {
		final var jwt = new JwtRequest(user.getUsername(), bcryptEncoder.encode(user.getPassword()));
		final var newUser = new AuthAccess(jwt, user.getOrganization());
		return authAccessRepository.save(newUser);
	}
}