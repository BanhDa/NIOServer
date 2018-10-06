/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tuantv.migrationstf.repository.base;

import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.tuantv.migrationstf.domain.Domain;

/**
 *
 * @author tuantran
 * @param <T>
 */
public abstract class BaseRepository<T extends Domain>{
    
    protected DBCollection getCollection() {
        return getDB().getCollection(getCollectionName());
    }
    
    protected abstract DB getDB();
    
    protected abstract String getCollectionName();
    
    protected abstract T castToDomain(DBObject dbObject);
    
    protected abstract DBObject castToDBObject(T domain);
}
