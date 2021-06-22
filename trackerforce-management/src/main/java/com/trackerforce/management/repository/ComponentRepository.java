package com.trackerforce.management.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.trackerforce.common.tenant.model.AbstractBusinessDocument;

interface ComponentRepository extends MongoRepository<AbstractBusinessDocument, String> {

}
