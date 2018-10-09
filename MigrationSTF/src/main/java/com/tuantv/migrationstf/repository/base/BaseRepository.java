/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tuantv.migrationstf.repository.base;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.WriteModel;
import com.tuantv.migrationstf.domain.Domain;
import static java.lang.Thread.sleep;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;
import lombok.extern.slf4j.Slf4j;
import org.bson.Document;

/**
 *
 * @author tuantran
 * @param <T>
 */
@Slf4j
public abstract class BaseRepository<T extends Domain>{
    
    protected static final String ID = "_id";
    
    private LinkedBlockingQueue<WriteModel> CONTAINER = new LinkedBlockingQueue<>();
    private BulkWritesWorker bulkWritesWorker;
    
    protected MongoCollection getCollection() {
        return getDB().getCollection(getCollectionName());
    }
    
    protected void put(Document document, String key, Object value) {
        if (value != null) {
            document.append(key, value);
        }
    }
    
    protected void putToBulkWrites(WriteModel writeModel) {
        CONTAINER.add(writeModel);
        if (bulkWritesWorker == null) {
            bulkWritesWorker = new BulkWritesWorker(getBulkWriteNumber(), getBulkWriteTimeout());
            bulkWritesWorker.start();
        }
    }
    
    protected abstract MongoDatabase getDB();
    
    protected abstract String getCollectionName();
    
    protected abstract T castToDomain(Document dbObject);
    
    protected abstract Document castToDBObject(T domain);
    
    protected abstract int getBulkWriteNumber();
    
    protected abstract int getBulkWriteTimeout();
    
    
    private class BulkWritesWorker extends Thread {

        private static final long FIVE_SECONDS = 5000;
        
        private final int bulkWritesNumber;
        private final int bulkWriteTimeout;
        
        public BulkWritesWorker(int bulkWritesNumber, int bulkWriteTimeout) {
            this.bulkWritesNumber = bulkWritesNumber;
            this.bulkWriteTimeout = bulkWriteTimeout;
        }
        
        @Override
        public void run() {
            List<WriteModel> writes = new ArrayList<>();
                long startTime = System.currentTimeMillis();
                long sleepTime = 0;
                while (true) {
                    try {
                        WriteModel writeModel = CONTAINER.poll();
                        if (writeModel == null) {
                            if (sleepTime == FIVE_SECONDS) {
                                Thread.interrupted();
                            }
                            sleepTime += 5;
                            sleep(5);
                        }
                        writes.add(writeModel);
                        long duration = System.currentTimeMillis() - startTime;
                        if (writes.size() == bulkWritesNumber || duration == bulkWriteTimeout) {
                            getCollection().bulkWrite(writes);
                            
                            startTime = System.currentTimeMillis();
                            writes.clear();
                        }
                        
                    } catch (Exception exception) {
                        log.error("", exception);
                    }
                }
        }
        
    }
}
