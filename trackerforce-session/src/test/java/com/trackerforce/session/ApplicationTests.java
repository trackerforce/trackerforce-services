package com.trackerforce.session;

import com.trackerforce.session.controller.SessionCaseController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
class ApplicationTests {

	@Autowired
	SessionCaseController sessionCaseController;

	@Test
	void contextLoads() {
		assertNotNull(sessionCaseController);
	}

}
