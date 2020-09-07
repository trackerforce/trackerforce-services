package com.trackerforce.management.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.trackerforce.common.service.exception.ServiceException;
import com.trackerforce.management.model.ComponentHelper;
import com.trackerforce.management.model.type.RenderType;
import com.trackerforce.management.repository.ComponentHelperDao;

@Service
public class ComponentHelperService extends AbstractBusinessService<ComponentHelper> {
	
	@Autowired
	private ComponentHelperDao componentHelperDao;
	
	public ComponentHelper createHelper(final String content, final String renderType) 
			throws ServiceException {
		
		ComponentHelper helper = new ComponentHelper();
		helper.setContent(content);
		helper.setRenderType(renderType);
		
		this.validate(helper);
		
		return create(helper);
	}
	
	@Override
	protected ComponentHelper create(ComponentHelper entity) {
		super.create(entity);
		return componentHelperDao.save(entity);
	}

	@Override
	protected void validate(final ComponentHelper entity) throws ServiceException {
		try {
			Assert.notNull(entity, "The class must not be null");
			Assert.hasText(entity.getContent(), "'content' must not be empty");
			Assert.hasText(entity.getRenderType(), "'renderType' must not be empty");
			RenderType.validate(entity.getRenderType());
		} catch (final Exception e) {
			throw new ServiceException(e.getMessage(), e);
		}
	}

}
