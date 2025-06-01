package com.trackerforce.common.tenant.config.mongodb;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.MongoDatabaseFactory;

@Configuration
@ConditionalOnProperty(name = "spring.boot.multitenant.mongodb.enabled", havingValue = "true", matchIfMissing = true)
public class MultitenantMongoDbConfiguration {

	private final String connectionString;

	private final String databasePrefix;

	private final HttpServletRequest request;

	public MultitenantMongoDbConfiguration(
			@Value("${spring.data.mongodb.uri}") String connectionString,
			@Value("${spring.data.mongodb.tenant-prefix:${spring.data.mongodb.database}}-") String databasePrefix,
			HttpServletRequest request) {
		this.connectionString = connectionString;
		this.databasePrefix = databasePrefix;
		this.request = request;
	}

    @Bean
    public MongoDatabaseFactory mongoDbFactory() {
        return new MultitenantMongoDbFactory(createMongoClient(), databasePrefix, request);
    }

	@Bean
	public MongoClient createMongoClient() {
		return MongoClients.create(connectionString);
	}

	@Bean
	public MongoClientSettings mongoClientSettings() {
		return MongoClientSettings.builder()
				.applyConnectionString(new ConnectionString(connectionString))
				.build();
	}

}