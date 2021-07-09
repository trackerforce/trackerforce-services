package com.trackerforce.management.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.trackerforce.management.model.Procedure;

public interface ProcedureRepository extends MongoRepository<Procedure, String> {

}
