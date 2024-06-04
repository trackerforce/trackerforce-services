package com.trackerforce.common.tenant.service;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.http.HttpStatus;
import org.springframework.util.ReflectionUtils;
import org.springframework.web.server.ResponseStatusException;

import com.trackerforce.common.model.AbstractDocument;
import com.trackerforce.common.model.request.QueryableRequest;
import com.trackerforce.common.tenant.repository.AbstractProjectedDao;
import com.trackerforce.common.tenant.service.exception.InvalidServiceUpdateException;

/**
 * Defines document organization owner
 *
 * @author Roger Floriano
 * @since 2020-07-22
 */
public abstract class AbstractTenantService<T extends AbstractDocument, R extends MongoRepository<T, String>> {

    protected final AbstractProjectedDao<T, R> dao;

    protected final Class<T> serviceModel;

    protected AbstractTenantService(final AbstractProjectedDao<T, R> dao, final Class<T> serviceModel) {
        this.dao = dao;
        this.serviceModel = serviceModel;
    }

    /**
     * Validates whether there is invalid update attributes
     *
     * @param updates body request
     * @param allowed attributes
     */
    private void validateUpdate(Map<String, Object> updates, String[] allowed) throws InvalidServiceUpdateException {
        for (String update : updates.keySet()) {
            if (Arrays.stream(allowed).noneMatch(key -> key.equals(update))) {
                throw new InvalidServiceUpdateException(update);
            }
        }
    }

    /**
     * Update entity given a map of attributes
     *
     * @param entity  to be updated
     * @param updates declared at the requets body
     * @return updated entity
     * @throws InvalidServiceUpdateException if something went wrong while assigning
     *                                       a new value to entity
     */
    protected <Y> Y update(Y entity, Map<String, Object> updates, String[] allowed)
            throws InvalidServiceUpdateException {
        this.validateUpdate(updates, allowed);
        final var ignored = "id";

        for (Map.Entry<String, Object> entry : updates.entrySet()) {
            var field = ReflectionUtils.findField(entity.getClass(), entry.getKey());

            if (field == null || ignored.equals(entry.getKey())) {
                throw new InvalidServiceUpdateException(entry.getKey());
            }

            try {
                field.setAccessible(true);
                field.set(entity, entry.getValue());
            } catch (IllegalArgumentException | IllegalAccessException e) {
                throw new InvalidServiceUpdateException(entry.getKey(), e);
            } finally {
                field.setAccessible(false);
            }
        }

        return entity;
    }

    @SuppressWarnings("unchecked")
    protected <Y> Y update(Y entity, Object updates, String[] allowed) throws InvalidServiceUpdateException {
        var wrapper = (Map<String, Object>) updates;
        return update(entity, wrapper, allowed);
    }

    public T findByIdProjectedBy(String id, String output) {
        final var outputOptional = Optional.ofNullable(output);

        if (outputOptional.isPresent()) {
            return dao.findByIdProjectedBy(id, serviceModel, outputOptional.get().split(","));
        }

        var response = dao.findByIdProjectedBy(id, serviceModel);
        if (response == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, serviceModel.getSimpleName() + " not found");
        }

        return response;
    }

    public Map<String, Object> findByIdsProjectedBy(String[] ids, String sortBy, String output, int page,
                                                    int size) {
        final var outputOptional = Optional.ofNullable(output);
        final var sortOptional = Optional.ofNullable(sortBy);

        var pageData = dao.findByIdsProjectedBy(ids, serviceModel, page, size,
                outputOptional.map(s -> s.split(",")).orElse(null),
                sortOptional.map(string -> string.split(",")).orElse(null));

        var data = pageData.getContent();
        var response = new HashMap<String, Object>();
        response.put("data", data);
        response.put("page", pageData.getNumber());
        response.put("items", pageData.getTotalElements());
        response.put("pages", pageData.getTotalPages());

        return response;
    }

    public Map<String, Object> findAllProjectedBy(QueryableRequest queryable) {
        final var outputOptional = Optional.ofNullable(queryable.getOutput());
        final var sortOptional = Optional.ofNullable(queryable.getSortBy());

        var criteria = dao.createCriteria(queryable.getQuery());
        var pageData = dao.findByProjectedBy(serviceModel, criteria, queryable.getPage(), queryable.getSize(),
                outputOptional.map(s -> s.split(",")).orElse(null),
                sortOptional.map(string -> string.split(",")).orElse(null));

        var data = pageData.getContent();
        var response = new HashMap<String, Object>();
        response.put("data", data);
        response.put("page", pageData.getNumber());
        response.put("items", pageData.getTotalElements());
        response.put("pages", pageData.getTotalPages());

        return response;
    }

}
