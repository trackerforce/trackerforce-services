package com.trackerforce.session.model;

import com.trackerforce.common.tenant.model.CommonTask;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
public class SessionTask extends CommonTask {

	private Object response;

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

}
