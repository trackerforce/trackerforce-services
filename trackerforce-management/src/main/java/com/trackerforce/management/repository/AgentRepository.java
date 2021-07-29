package com.trackerforce.management.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.trackerforce.management.model.Agent;

public interface AgentRepository extends MongoRepository<Agent, String> {
	
	Agent findByEmail(String email);

}
