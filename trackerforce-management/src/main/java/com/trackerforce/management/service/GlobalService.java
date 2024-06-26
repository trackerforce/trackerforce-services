package com.trackerforce.management.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.trackerforce.common.service.exception.ServiceException;
import com.trackerforce.management.model.Global;
import com.trackerforce.management.model.request.GlobalRequest;
import com.trackerforce.management.repository.GlobalRepository;
import com.trackerforce.management.repository.GlobalRepositoryDao;
import org.springframework.web.server.ResponseStatusException;

@Service
public class GlobalService extends AbstractBusinessService<Global, GlobalRepository> {

	private static final String[] ALLOWED_GLOBAL_UPDATE = { "key", "attributes" };

	private final GlobalRepositoryDao globalDao;

	public GlobalService(GlobalRepositoryDao globalDao) {
		super(globalDao, Global.class, "global");
		this.globalDao = globalDao;
	}

	@Override
	protected void validate(final Global entity) throws ServiceException {
		try {
			Assert.notNull(entity, "The class must not be null");
		} catch (final Exception e) {
			throw new ServiceException(e.getMessage(), e);
		}
	}

	public Global create(final GlobalRequest globalRequest) {
		try {
			var global = globalRequest.getGlobal();
			global.setDescription(global.getKey().getDescription());
			return super.create(global);
		} catch (final Exception e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage(), e);
		}
	}

	public Global update(final String id, final Map<String, Object> updates) {
		try {
			var promise = globalDao.getRepository().findById(id);
			var allowed = new HashMap<String, String[]>();
			allowed.put(entityName, ALLOWED_GLOBAL_UPDATE);

			return super.update(promise, updates, allowed);
		} catch (final Exception e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage(), e);
		}
	}

	public void delete(final String id) {
		globalDao.getRepository().deleteById(id);
	}

}
