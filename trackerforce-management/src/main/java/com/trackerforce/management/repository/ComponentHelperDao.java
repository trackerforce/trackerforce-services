package com.trackerforce.management.repository;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import com.trackerforce.management.model.AbstractBusinessDocument;
import com.trackerforce.management.model.ComponentHelper;

@Repository
public class ComponentHelperDao {
	
	@Autowired
	private MongoTemplate mongoTemplate;
	
	@Autowired
	private ComponentRepository componentRepository;
	
	@Autowired
	private ComponentHelperRepository componentHelperRepository;
	
	public void delete(ComponentHelper entity) {
		disassociateHelperComponentByCompId(entity.getId());
		componentHelperRepository.delete(entity);
	}
	
	public ComponentHelper save(ComponentHelper entity) {
		return componentHelperRepository.save(entity);
	}
	
	public void disassociateHelperComponentByCompId(final String id) {
		final Query query = new Query();
		query.addCriteria(Criteria.where("helper").in(id));
		
		final List<AbstractBusinessDocument> parentComponent = 
				mongoTemplate.find(query, AbstractBusinessDocument.class);
		
		parentComponent.stream().forEach(component -> component.setHelper(null));
		componentRepository.saveAll(parentComponent);
	}

	public ComponentRepository getComponentRepository() {
		return componentRepository;
	}

}
