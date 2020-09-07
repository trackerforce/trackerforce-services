package com.trackerforce.management.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.trackerforce.management.model.Template;

interface TemplateRepository extends MongoRepository<Template, String> {

}
