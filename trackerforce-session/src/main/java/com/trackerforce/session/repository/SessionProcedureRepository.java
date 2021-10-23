package com.trackerforce.session.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.trackerforce.session.model.SessionProcedure;

public interface SessionProcedureRepository extends MongoRepository<SessionProcedure, String> {

}
