package com.trackerforce.identity;

import com.trackerforce.identity.controller.AgentIdentityController;
import com.trackerforce.identity.controller.IdentityController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
class ApplicationTests {

	@Autowired
	AgentIdentityController agentIdentityController;

	@Autowired
	IdentityController identityController;

	@Test
	void contextLoads() {
		assertNotNull(agentIdentityController);
		assertNotNull(identityController);
	}

}
