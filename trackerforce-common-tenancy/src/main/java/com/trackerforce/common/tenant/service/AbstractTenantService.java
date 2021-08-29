package com.trackerforce.common.tenant.service;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.util.ReflectionUtils;
import org.springframework.web.server.ResponseStatusException;

import com.trackerforce.common.model.AbstractDocument;
import com.trackerforce.common.tenant.repository.AbstractProjectedDao;
import com.trackerforce.common.tenant.service.exception.InvalidServiceUpdateException;

/**
 * Defines document organization owner
 * 
 * @author Roger Floriano
 * @since 2020-07-22
 *
 * @param <T AbstractDocument>
 */
public abstract class AbstractTenantService<T extends AbstractDocument> {

	protected final AbstractProjectedDao<T> dao;

	protected final Class<T> serviceModel;

	public AbstractTenantService(final AbstractProjectedDao<T> dao, final Class<T> serviceModel) {
		this.dao = dao;
		this.serviceModel = serviceModel;
	}

	/**
	 * Validates whether there is invalid update attributes
	 * 
	 * @param updates body request
	 * @param allowed attributes
	 * @throws InvalidServiceUpdateException
	 */
	private void validateUpdate(Map<String, Object> updates, String[] allowed) throws InvalidServiceUpdateException {
		for (String update : updates.keySet())
			if (!Arrays.stream(allowed).anyMatch(key -> key.equals(update)))
				throw new InvalidServiceUpdateException(update);
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

		for (String key : updates.keySet()) {
			var field = ReflectionUtils.findField(entity.getClass(), key);

			if (field == null || ignored.equals(key))
				throw new InvalidServiceUpdateException(key);

			try {
				field.setAccessible(true);
				field.set(entity, updates.get(key));
			} catch (IllegalArgumentException | IllegalAccessException e) {
				throw new InvalidServiceUpdateException(key, e);
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

	/**
	 * {@link AbstractBusinessService#findByIdProjectedBy(String, String)}
	 */
	public T findByIdProjectedBy(String id, String output) {
		final var outputOptional = Optional.ofNullable(output);

		if (outputOptional.isPresent())
			return dao.findByIdProjectedBy(id, serviceModel, outputOptional.get().split(","));

		var response = dao.findByIdProjectedBy(id, serviceModel);
		if (response == null)
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, serviceModel.getSimpleName() + " not found");

		return response;
	}

	/**
	 * {@link AbstractBusinessService#findByIdProjectedBy(String, String)}
	 */
	public Map<String, Object> findByIdsProjectedBy(String[] ids, String sortBy, String output, int page,
			int size) {

		final var outputOptional = Optional.ofNullable(output);
		final var sortOptional = Optional.ofNullable(sortBy);

		var pageData = dao.findByIdsProjectedBy(ids, serviceModel, page, size,
				outputOptional.isPresent() ? outputOptional.get().split(",") : null,
				sortOptional.isPresent() ? sortOptional.get().split(",") : null);

		var data = pageData.getContent();
		var response = new HashMap<String, Object>();
		response.put("data", data);
		response.put("page", pageData.getNumber());
		response.put("items", pageData.getTotalElements());
		response.put("pages", pageData.getTotalPages());

		return response;
	}

	/**
	 * {@link AbstractBusinessService#findAllProjectedBy(String, String, String, int, int)}
	 */
	public Map<String, Object> findAllProjectedBy(Map<String, Object> query, String sortBy, String output, int page,
			int size) {

		final var criteriaOptional = Optional.ofNullable(query);
		final var outputOptional = Optional.ofNullable(output);
		final var sortOptional = Optional.ofNullable(sortBy);

		var criteria = dao.createCriteria(query, criteriaOptional);
		var pageData = dao.findByProjectedBy(serviceModel, criteria, page, size,
				outputOptional.isPresent() ? outputOptional.get().split(",") : null,
				sortOptional.isPresent() ? sortOptional.get().split(",") : null);

		var data = pageData.getContent();
		var response = new HashMap<String, Object>();
		response.put("data", data);
		response.put("page", pageData.getNumber());
		response.put("items", pageData.getTotalElements());
		response.put("pages", pageData.getTotalPages());

		return response;
	}

}
