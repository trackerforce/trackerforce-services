package com.trackerforce.session.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.trackerforce.session.model.SessionCase;

public interface SessionCaseRepository extends MongoRepository<SessionCase, String> {
	
	public SessionCase findByProtocol(long protocol);

}
