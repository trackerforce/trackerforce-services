package com.trackerforce.common.tenant.repository;

import com.trackerforce.common.model.AbstractDocument;
import jakarta.annotation.Nullable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.support.PageableExecutionUtils;

import java.util.Arrays;
import java.util.Map;

public abstract class AbstractProjectedDao<T extends AbstractDocument, R extends MongoRepository<T, String>> {

    protected final MongoTemplate mongoTemplate;

    protected AbstractProjectedDao(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    /**
     * Retrieve repository from child class
     */
    public abstract R getRepository();

    /**
     * Helper operation to save documents from the ProjectedDao
     *
     * @return Saved entity
     */
    public T save(T entity) {
        return mongoTemplate.save(entity);
    }

    /**
     * Find one element by ID
     */
    public T findByIdProjectedBy(String id, Class<T> entityClass, String... fields) {
        var query = Query.query(Criteria.where("id").is(id));
        Arrays.stream(fields).forEach(field -> query.fields().include(field.trim()));
        return mongoTemplate.findOne(query, entityClass);
    }

    /**
     * Find elements by Ids
     */
    public Page<T> findByIdsProjectedBy(String[] ids, Class<T> entityClass, int page, int size, String[] fields,
                                        String[] sortBy) {
        var pageable = PageRequest.of(page, size);
        var query = Query.query(Criteria.where("id").in(Arrays.asList(ids))).with(pageable);

        if (sortBy != null) {
            Arrays.stream(sortBy).forEach(
                    field -> query.with(Sort.by(field.trim().startsWith("-") ? Sort.Direction.DESC : Sort.Direction.ASC,
                            field.substring(1).trim())));
        }

        if (fields != null) {
            Arrays.stream(fields).forEach(field -> query.fields().include(field.trim()));
        }

        final var list = mongoTemplate.find(query, entityClass);
        return PageableExecutionUtils.getPage(list, pageable,
                () -> mongoTemplate.count(Query.of(query).limit(-1).skip(-1), entityClass));
    }

    /**
     * Find elements in a collection given the following arguments.
     *
     * @param criteria    as a condition to filter elements
     * @param page        to be returned
     * @param size        of the page
     * @param entityClass as a Collection type
     * @param fields      to be projected as output separated with commas
     * @param sortBy      as a list of fields separated with commas. Starting with
     *                    "-" as DESC and "+" as ASC
     * @return Page of elements
     */
    public Page<T> findByProjectedBy(Class<T> entityClass, Criteria criteria, int page, int size, String[] fields,
                                     String[] sortBy) {

        var pageable = PageRequest.of(page, size);
        var query = criteria != null ? Query.query(criteria).with(pageable) : new Query().with(pageable);

        if (sortBy != null) {
            Arrays.stream(sortBy).forEach(
                    field -> query.with(Sort.by(field.trim().startsWith("-") ? Sort.Direction.DESC : Sort.Direction.ASC,
                            field.substring(1).trim())));
        }

        if (fields != null) {
            Arrays.stream(fields).forEach(field -> query.fields().include(field.trim()));
        }

        final var list = mongoTemplate.find(query, entityClass);
        return PageableExecutionUtils.getPage(list, pageable,
                () -> mongoTemplate.count(Query.of(query).limit(-1).skip(-1), entityClass));
    }

    @Nullable
    public Criteria createCriteria(Map<String, Object> query) {
        Criteria criteria = null;
        for (Map.Entry<String, Object> entry : query.entrySet()) {
            if (entry.getValue() == null) {
                continue;
            }

            var val = String.format(".*%s.*", entry.getValue());
            if (criteria == null) {
                criteria = Criteria.where(entry.getKey()).regex(val, "i");
            } else {
                criteria = criteria.and(entry.getKey()).regex(val, "i");
            }
        }
        return criteria;
    }

}
