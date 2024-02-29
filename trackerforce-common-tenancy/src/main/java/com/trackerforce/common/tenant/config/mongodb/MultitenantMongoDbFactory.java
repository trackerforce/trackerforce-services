package com.trackerforce.common.tenant.config.mongodb;

import java.util.Optional;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.InvalidDataAccessResourceUsageException;
import org.springframework.data.mongodb.core.SimpleMongoClientDatabaseFactory;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoDatabase;
import com.trackerforce.common.tenant.interceptor.TenantInterceptor;

public class MultitenantMongoDbFactory extends SimpleMongoClientDatabaseFactory {

	@Autowired
	private HttpServletRequest request;

	@Value("${spring.data.mongodb.tenant-prefix:${spring.data.mongodb.database}}-")
	private String databasePrefix;

	public MultitenantMongoDbFactory(MongoClient mongoClient, String databaseName) {
		super(mongoClient, databaseName);
	}

	@Override
	public MongoDatabase getMongoDatabase() throws DataAccessException {
		final var tenantId = Optional.ofNullable(request.getAttribute(TenantInterceptor.TENANT_ID));

		if (!tenantId.isPresent())
			throw new InvalidDataAccessResourceUsageException("X-Tenant is missing");

		return super.getMongoDatabase(String.format("%s%s", databasePrefix, tenantId.get()));
	}

}