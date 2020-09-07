package com.trackerforce.common.tenant.repository;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import com.trackerforce.common.model.AbstractDocument;

public abstract class AbstractProjectedDao<T extends AbstractDocument> {
	
	@Autowired
	protected MongoTemplate mongoTemplate;
	
	public T findByIdProjectedBy(final String id, Class<T> projectedClass, String... fields) {
		Query query = Query.query(Criteria.where("id").is(id));
		Arrays.stream(fields).forEach(field -> query.fields().include(field.trim()));
		return mongoTemplate.findOne(query, projectedClass);
	}
	
}
