package com.trackerforce.management;

import com.trackerforce.management.controller.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
class ApplicationTests {

	@Autowired
	AgentController agentController;

	@Autowired
	GlobalController globalController;

	@Autowired
	ProcedureController procedureController;

	@Autowired
	TaskController taskController;

	@Autowired
	TemplateController templateController;

	@Test
	void contextLoads() {
		assertNotNull(agentController);
		assertNotNull(globalController);
		assertNotNull(procedureController);
		assertNotNull(taskController);
		assertNotNull(templateController);
	}

}
