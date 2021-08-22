package com.trackerforce.session.model;

import com.trackerforce.common.tenant.model.CommonTask;

public class SessionTask extends CommonTask {

	private Object response;

	public SessionTask() {
	}

	private SessionTask(CommonTask input) {
		super.hidden = input.isHidden();
		super.learn = input.isLearn();
		super.options = input.getOptions();
		super.type = input.getType();

		super.setDescription(input.getDescription());
		super.setHelper(input.getHelper());
		super.setId(input.getId());
	}

	public static SessionTask createTaskResolution(CommonTask input) {
		return new SessionTask(input);
	}

	public Object getResponse() {
		return response;
	}

	public void setResponse(Object response) {
		this.response = response;
	}

}
