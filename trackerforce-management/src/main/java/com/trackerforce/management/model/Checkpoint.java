package com.trackerforce.management.model;

import java.util.Collections;
import java.util.List;

import org.springframework.data.mongodb.core.mapping.Document;

import com.trackerforce.management.model.type.CheckpointType;

@Document(collection = "checkpoints")
public class Checkpoint extends AbstractBusinessDocument {
	
	/**
	 * {@link CheckpointType}
	 */
	private String type;
	
	private List<String> subscribers = Collections.emptyList();

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public List<String> getSubscribers() {
		return subscribers;
	}

	public void setSubscribers(List<String> subscribers) {
		this.subscribers = subscribers;
	}

}
