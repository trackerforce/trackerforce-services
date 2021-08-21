package com.trackerforce.session.model;

import com.trackerforce.common.tenant.model.CommonTask;

public class SessionTask<T> extends CommonTask {

	private T response;

	private SessionTask(CommonTask input) {
		super.hidden = input.isHidden();
		super.learn = input.isLearn();
		super.options = input.getOptions();
		super.type = input.getType();

		super.setDescription(input.getDescription());
		super.setHelper(input.getHelper());
		super.setId(input.getId());
	}

	public static <Y> SessionTask<Y> createTaskResolution(CommonTask input, Class<Y> clazzResolution) {
		return new SessionTask<Y>(input);
	}

	public T getResponse() {
		return response;
	}

	public void setResponse(T response) {
		this.response = response;
	}

}
