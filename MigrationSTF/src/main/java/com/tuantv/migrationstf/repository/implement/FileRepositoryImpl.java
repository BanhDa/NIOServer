/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tuantv.migrationstf.repository.implement;

import com.mongodb.MongoClient;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.InsertOneModel;
import com.mongodb.client.model.UpdateManyModel;
import com.mongodb.client.model.WriteModel;
import com.tuantv.migrationstf.config.MongoConfig;
import com.tuantv.migrationstf.domain.FileInfo;
import org.springframework.stereotype.Repository;
import com.tuantv.migrationstf.repository.base.BulkWritesRepository;
import com.tuantv.migrationstf.repository.base.FileRepository;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import org.bson.Document;
import org.bson.types.ObjectId;

/**
 *
 * @author tuantran
 */
@Repository
@AllArgsConstructor
public class FileRepositoryImpl extends BulkWritesRepository<FileInfo> implements FileRepository {

    private static final String DATABASE_NAME = "staticfiledb";
    private static final String COLLECTION_NAME = "dbtest";
    
    private static final String URL = "url";
    private static final String UPLOAD_TIME = "upload_time";
    
    private final MongoClient mongoClient;
    private final MongoConfig config;
    
    @Override
    protected MongoDatabase getDB() {
        return mongoClient.getDatabase(DATABASE_NAME);
    }

    @Override
    protected String getCollectionName() {
        return COLLECTION_NAME;
    }

    @Override
    protected FileInfo castToDomain(Document dbObject) {
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
    protected Document castToDBObject(FileInfo domain) {
        Document document = new Document();
        
        if (domain.getId() != null) {
            document.append(ID, new ObjectId(domain.getId()));
        }
        
        put(document, URL, domain.getUrl());
        put(document, UPLOAD_TIME, domain.getUploadTime());
        
        return document;
    }

    @Override
    public void insertMany(List<FileInfo> fileInfos) {
        fileInfos.parallelStream().forEach(fileInfo -> {
            WriteModel insertModel = new InsertOneModel(castToDBObject(fileInfo));
            putToBulkWrites(insertModel);
        });
        
    }

    @Override
    public List<FileInfo> getFiles(int skip, int take) {
        Document sortById = new Document(ID, 1);
        
        List<FileInfo> result = new ArrayList<>();
        FindIterable iterable = getCollection().find().sort(sortById).skip(skip).limit(take);
        try (MongoCursor cursor = iterable.iterator()) {
            while(cursor.hasNext()) {
                Document document = (Document) cursor.next();
                FileInfo fileInfo = castToDomain(document);
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
        Document query = new Document(ID, id);
        
        Document update = new Document("$set", new Document(UPLOAD_TIME, uploadTime));
        WriteModel updateModel = new UpdateManyModel(query, update);
        putToBulkWrites(updateModel);;
    }

    @Override
    protected int getBulkWriteNumber() {
        return config.getBulkWriteNumber();
    }

    @Override
    protected int getBulkWriteTimeout() {
        return config.getBulkWriteTimeout();
    }

    @Override
    public void showWorker() {
        getBulkWritesWorker();
    }

}
