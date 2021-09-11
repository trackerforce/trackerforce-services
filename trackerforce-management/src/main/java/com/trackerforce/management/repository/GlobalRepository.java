package com.trackerforce.management.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.trackerforce.management.model.Global;

public interface GlobalRepository extends MongoRepository<Global, String> {

}
