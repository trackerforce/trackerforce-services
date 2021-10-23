package com.trackerforce.management.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.trackerforce.management.model.Task;

public interface TaskRepository extends MongoRepository<Task, String> {
	
}
