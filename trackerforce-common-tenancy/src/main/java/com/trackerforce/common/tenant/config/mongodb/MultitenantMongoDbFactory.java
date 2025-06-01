package com.trackerforce.common.tenant.config.mongodb;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoDatabase;
import com.trackerforce.common.tenant.interceptor.TenantInterceptor;
import jakarta.servlet.http.HttpServletRequest;
import lombok.NonNull;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.InvalidDataAccessResourceUsageException;
import org.springframework.data.mongodb.core.SimpleMongoClientDatabaseFactory;

import java.util.Optional;

public class MultitenantMongoDbFactory extends SimpleMongoClientDatabaseFactory {

	private final HttpServletRequest request;

	private final String databasePrefix;

	public MultitenantMongoDbFactory(MongoClient mongoClient,
									 String databasePrefix, HttpServletRequest request) {
		super(mongoClient, databasePrefix);
		this.databasePrefix = databasePrefix;
		this.request = request;
	}

	@Override
	@NonNull
	public MongoDatabase getMongoDatabase() throws DataAccessException {
		final var tenantId = Optional.ofNullable(request.getAttribute(TenantInterceptor.TENANT_ID));

		if (tenantId.isEmpty())
			throw new InvalidDataAccessResourceUsageException("X-Tenant is missing");

		return super.getMongoDatabase(String.format("%s%s", databasePrefix, tenantId.get()));
	}

}