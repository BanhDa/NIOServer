/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tuantv.migrationstf.repository.implement;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.tuantv.migrationstf.domain.FileInfo;
import org.springframework.stereotype.Repository;
import com.tuantv.migrationstf.repository.base.BaseRepository;
import com.tuantv.migrationstf.repository.base.FileRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import org.bson.types.ObjectId;

/**
 *
 * @author tuantran
 */
@Repository
@AllArgsConstructor
public class FileRepositoryImpl extends BaseRepository<FileInfo> implements FileRepository {

    private static final String DATABASE_NAME = "staticfiledb";
    private static final String COLLECTION_NAME = "dbtest";
    
    private static final String URL = "url";
    private static final String UPLOAD_TIME = "upload_time";
    
    private final MongoClient mongoClient;
    
    @Override
    protected DB getDB() {
        return mongoClient.getDB(DATABASE_NAME);
    }

    @Override
    protected String getCollectionName() {
        return COLLECTION_NAME;
    }

    @Override
    protected FileInfo castToDomain(DBObject dbObject) {
        if (dbObject == null) {
            return null;
        }
        
        FileInfo fileInfo = new FileInfo();
        
        ObjectId id = (ObjectId) dbObject.get(ID);
        fileInfo.setId(id.toString());
        
        String url = (String) dbObject.get(URL);
        fileInfo.setUrl(url);
        
        Long uploadTime = (Long) dbObject.get(UPLOAD_TIME);
        fileInfo.setUploadTime(uploadTime);
        
        return fileInfo;
    }

    @Override
    protected DBObject castToDBObject(FileInfo domain) {
        BasicDBObject dBObject = new BasicDBObject();
        
        if (domain.getId() != null) {
            dBObject.append(ID, new ObjectId(domain.getId()));
        }
        
        put(dBObject, URL, domain.getUrl());
        put(dBObject, UPLOAD_TIME, domain.getUploadTime());
        
        return dBObject;
    }

    @Override
    public void insertMany(List<FileInfo> fileInfos) {
        List<DBObject> dbList = fileInfos.parallelStream().map(fileInfo -> {
            return castToDBObject(fileInfo);
        }).collect(Collectors.toList());
        
        getCollection().insert(dbList);
    }

    @Override
    public List<FileInfo> getFiles(int skip, int take) {
        BasicDBObject sortById = new BasicDBObject(ID, 1);
        
        List<FileInfo> result = new ArrayList<>();
        try (DBCursor cursor = getCollection().find().sort(sortById).skip(skip).limit(take)) {
            while(cursor.hasNext()) {
                BasicDBObject dBObject = (BasicDBObject) cursor.next();
                FileInfo fileInfo = castToDomain(dBObject);
                result.add(fileInfo);
            }
        }
        
        return result;
    }

    @Override
    public long getFileNumber() {
        return getCollection().count();
    }

    @Override
    public void updateUploadTime(String fileId, long uploadTime) {
        ObjectId id = new ObjectId(fileId);
        BasicDBObject query = new BasicDBObject(ID, id);
        
        BasicDBObject update = new BasicDBObject("$set", new BasicDBObject(UPLOAD_TIME, uploadTime));
        
        getCollection().update(query, update);
    }
    
}
