package com.trackerforce.management.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.trackerforce.management.model.ComponentHelper;

interface ComponentHelperRepository extends MongoRepository<ComponentHelper, String> {

}
