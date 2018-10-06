/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tuantv.migrationstf.repository.implement;

import com.mongodb.DB;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.tuantv.migrationstf.domain.FileInfo;
import org.springframework.stereotype.Repository;
import com.tuantv.migrationstf.repository.base.BaseRepository;
import com.tuantv.migrationstf.repository.base.FileRepository;
import java.util.List;
import lombok.AllArgsConstructor;

/**
 *
 * @author tuantran
 */
@Repository
@AllArgsConstructor
public class FileRepositoryImpl extends BaseRepository<FileInfo> implements FileRepository {

    private static final String DATABASE_NAME = "staticfiledb";
    private static final String COLLECTION_NAME = "file";
    
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
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    protected DBObject castToDBObject(FileInfo domain) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void insertMany(List<FileInfo> fileInfos) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<FileInfo> getFiles(int skip, int take) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
