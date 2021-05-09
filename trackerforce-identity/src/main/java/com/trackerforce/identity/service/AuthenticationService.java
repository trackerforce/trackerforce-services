package com.trackerforce.identity.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

import com.trackerforce.common.service.JwtTokenService;
import com.trackerforce.common.service.exception.ServiceException;
import com.trackerforce.identity.model.AuthAccess;
import com.trackerforce.identity.model.request.JwtRequest;
import com.trackerforce.identity.repository.AuthAccessRepository;

@Service
public class AuthenticationService extends AbstractIdentityService<AuthAccess> {
	
	@Autowired
	AuthenticationManager authenticationManager;
	
	@Autowired
	AuthAccessRepository authAccessRepository;
	
	@Autowired
	private JwtTokenService jwtTokenUtil;
	
	public AuthAccess authenticateAccess(JwtRequest authRequest) throws ServiceException {
//		authenticate(authRequest);
		
//		AuthAccess authAccess = authAccessRepository.findByUserName(authRequest.getUsername());
		AuthAccess authAccess = new AuthAccess();
		authAccess.setToken(jwtTokenUtil.generateToken(authRequest.getUsername()));
		
		return authAccess;
	}

	@Override
	protected void validate(AuthAccess entity) throws ServiceException {
		// TODO Auto-generated method stub
		
	}
	
	private void authenticate(JwtRequest authRequest) throws ServiceException {
		try {
			authenticationManager.authenticate(
					new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword()));
		} catch (DisabledException e) {
			throw new ServiceException("USER_DISABLED", e);
		} catch (BadCredentialsException e) {
			throw new ServiceException("INVALID_CREDENTIALS", e);
		}
	}

}
