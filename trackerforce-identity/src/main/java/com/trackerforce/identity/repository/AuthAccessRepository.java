package com.trackerforce.identity.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.trackerforce.identity.model.AuthAccess;

@Repository
public interface AuthAccessRepository extends CrudRepository<AuthAccess, String> {
	
	AuthAccess findByUsername(String username);
	
}