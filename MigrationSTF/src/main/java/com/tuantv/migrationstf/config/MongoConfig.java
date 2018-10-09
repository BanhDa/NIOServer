/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tuantv.migrationstf.config;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 *
 * @author tuantran
 */
@Getter
@Configuration
public class MongoConfig {

    @Value("${application.database.mongo.host}")
    private String host;

    @Value("${application.database.mongo.port}")
    private int port;
    
    @Value("${application.database.mongo.authenticationDatabase}")
    private String authenticationDatabase;
    
    @Value("${application.database.mongo.userName}")
    private String userName;
    
    @Value("${application.database.mongo.password}")
    private String password;
    
    @Value("${application.database.mongo.connectionsPerHost}")
    private int connectionsPerHost;
    
    @Value("${application.database.mongo.bulkWriteNumber}")
    private int bulkWriteNumber;
    
    @Value("${application.database.mongo.bulkWriteTimeout}")
    private int bulkWriteTimeout;

    @Bean
    public MongoClient getMongoClient() {
//        MongoCredential mongoCredential = MongoCredential.createCredential(userName, authenticationDatabase, password.toCharArray());
        MongoClientOptions mongoClientOptions = new MongoClientOptions.Builder().connectionsPerHost(connectionsPerHost).build();
        MongoClient mongoClient = new MongoClient(new ServerAddress(host, port), mongoClientOptions);
        
        return mongoClient;
    }
    
}
