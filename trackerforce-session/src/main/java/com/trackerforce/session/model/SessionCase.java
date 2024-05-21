package com.trackerforce.session.model;

import com.trackerforce.common.tenant.model.AbstractBusinessDocument;
import com.trackerforce.common.tenant.model.CommonProcedure;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Document(collection = "cases")
@Data
@NoArgsConstructor
public class SessionCase extends AbstractBusinessDocument {

	private Long protocol;

	private String contextId;

	private String context;

	private List<SessionProcedure> procedures;

	private SessionCase(SessionTemplate input) {
		this.contextId = input.getId();
		this.context = input.getName();
		super.setDescription(input.getDescription());
		super.setHelper(input.getHelper());
	}

	public static SessionCase create(SessionTemplate input) {
		var sessionCase = new SessionCase(input);

		sessionCase.setProtocol(System.currentTimeMillis() / 1000);
		for (CommonProcedure<?> proc : input.getProcedures())
			sessionCase.getProcedures().add(SessionProcedure.create(proc));

		return sessionCase;
	}

	public List<SessionProcedure> getProcedures() {
		if (procedures == null)
			procedures = new ArrayList<>();
		return procedures;
	}

}
