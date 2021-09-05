package com.trackerforce.management.model;

import org.springframework.data.mongodb.core.mapping.Document;

import com.trackerforce.common.tenant.model.CommonTemplate;

@Document(collection = "templates")
public class Template extends CommonTemplate<Procedure> {

}
