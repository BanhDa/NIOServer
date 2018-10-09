/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tuantv.migrationstf.repository;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.WriteModel;
import com.tuantv.migrationstf.config.MongoConfig;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;
import lombok.extern.slf4j.Slf4j;
import org.bson.Document;

/**
 *
 * @author tuantv
 */
@Slf4j
public class QueryMangagement {

    private static final QueryMangagement INSTANCE = new QueryMangagement();

    private LinkedBlockingQueue<WriteModel> CONTAINER = new LinkedBlockingQueue<>();
    private MongoConfig config;
    private MongoClient mongoClient;

    public static QueryMangagement getInstance() {
        return INSTANCE;
    }

    private QueryMangagement() {
    }

    public void initial(MongoConfig xConfig, MongoClient mongoClient) {
        this.config = xConfig;
    }

    public void put(WriteModel writeModel) {
        try {
            CONTAINER.put(writeModel);
        } catch (InterruptedException ex) {
        }
    }

    public void start() {
        new Thread() {
            @Override
            public void run() {
                MongoCollection collection = mongoClient.getDatabase("").getCollection("");
                List<WriteModel<Document>> writes = new ArrayList<>();
                long startTime = System.currentTimeMillis();
                while (true) {
                    try {
                        WriteModel writeModel = CONTAINER.poll();
                        if (writeModel == null) {
                            sleep(5);
                        }
                        writes.add(writeModel);
                        long duration = System.currentTimeMillis() - startTime;
                        if (writes.size() == config.getBulkWriteNumber() || duration == config.getBulkWriteTimeout()) {
                            collection.bulkWrite(writes);
                            
                            startTime = System.currentTimeMillis();
                            writes.clear();
                        }
                        
                    } catch (Exception exception) {
                        log.error("", exception);
                    }
                }
            }
        }.start();
    }
    
}
