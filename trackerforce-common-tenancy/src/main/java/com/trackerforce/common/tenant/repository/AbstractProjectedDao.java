package com.trackerforce.common.tenant.repository;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.support.PageableExecutionUtils;

import com.trackerforce.common.model.AbstractDocument;

public abstract class AbstractProjectedDao<T extends AbstractDocument> {

	@Autowired
	protected MongoTemplate mongoTemplate;

	public T findByIdProjectedBy(final String id, Class<T> entityClass, String... fields) {
		Query query = Query.query(Criteria.where("id").is(id));
		Arrays.stream(fields).forEach(field -> query.fields().include(field.trim()));
		return mongoTemplate.findOne(query, entityClass);
	}

	/**
	 * {@link #findByProjectedBy(Criteria, int, int, Class, String[], String[])}
	 * 
	 * @param page to be returned
	 * @param size of the page
	 * @param entityClass as a Collection type
	 * @param fields to be projected as output separetad with commas
	 * @return Page of elements
	 */
	public Page<T> findAllProjectedBy(
			final Class<T> entityClass, 
			final int page, 
			final int size, 
			final String[] fields) {

		return findByProjectedBy(entityClass, null, page, size, fields, null);
	}

	/**
	 * Find elements in a collection given the following arguments.
	 * 
	 * @param criteria as a condition to filter elements
	 * @param page to be returned
	 * @param size of the page
	 * @param entityClass as a Collection type
	 * @param fields to be projected as output separetad with commas
	 * @param sortBy as a list of fields separated with commas. Starting with "-" as DESC and "+" as ASC
	 * @return Page of elements
	 */
	public Page<T> findByProjectedBy(
			final Class<T> entityClass, 
			final Criteria criteria,
			final int page, 
			final int size, 
			final String[] fields,
			final String[] sortBy) {

		var pageable = PageRequest.of(page, size);
		var query = criteria != null ? 
				Query.query(criteria).with(pageable) : new Query().with(pageable);

		if (sortBy != null)
			Arrays.stream(sortBy).forEach(field -> 
			query.with(Sort.by(
					field.trim().startsWith("-") ? 
							Sort.Direction.DESC : Sort.Direction.ASC, field.substring(1).trim())));

		if (fields != null)
			Arrays.stream(fields).forEach(field -> 
			query.fields().include(field.trim()));

		final var list = mongoTemplate.find(query, entityClass);
		return PageableExecutionUtils
				.getPage(list, pageable, () -> mongoTemplate.count(
						Query.of(query).limit(-1).skip(-1), entityClass));
	}

}
