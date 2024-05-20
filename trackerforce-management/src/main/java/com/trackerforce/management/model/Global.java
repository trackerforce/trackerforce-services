package com.trackerforce.management.model;

import java.util.Map;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.mongodb.core.mapping.Document;

import com.trackerforce.common.tenant.model.AbstractBusinessDocument;
import com.trackerforce.common.tenant.model.type.GlobalKeyType;

@EqualsAndHashCode(callSuper = true)
@Document(collection = "globals")
@Data
public class Global extends AbstractBusinessDocument {
	
	private GlobalKeyType key;
	
	private Map<String, Object> attributes;
	
}
