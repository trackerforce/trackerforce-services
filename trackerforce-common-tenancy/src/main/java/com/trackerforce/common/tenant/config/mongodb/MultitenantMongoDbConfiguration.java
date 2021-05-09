package com.trackerforce.common.tenant.config.mongodb;

import java.net.UnknownHostException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.MongoDatabaseFactory;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;

@Configuration
@ConditionalOnProperty(name = "spring.boot.multitenant.mongodb.enabled", havingValue = "true", matchIfMissing = true)
public class MultitenantMongoDbConfiguration {
	
	@Value("${spring.data.mongodb.uri}")
	private String connectionString;
	
	@Value("${spring.data.mongodb.tenant-prefix:${spring.data.mongodb.database}}-")
	private String databasePrefix;

    @Bean
    public MongoClient createMongoClient() throws UnknownHostException {
    	return MongoClients.create(connectionString);
    }

    @Bean
    public MongoDatabaseFactory mongoDbFactory() throws UnknownHostException {
        return new MultitenantMongoDbFactory(createMongoClient(), databasePrefix);
    }

}