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
    
    protected MongoCollection getCollection() {
        return getDB().getCollection(getCollectionName());
    }
    
    protected void put(Document document, String key, Object value) {
        if (value != null) {
            document.append(key, value);
        }
    }
    
    protected abstract MongoDatabase getDB();
    
    protected abstract String getCollectionName();
    
    protected abstract T castToDomain(Document dbObject);
    
    protected abstract Document castToDBObject(T domain);
    
    protected abstract int getBulkWriteNumber();
    
    protected abstract int getBulkWriteTimeout();
    
}
