/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tuantv.migrationstf.repository.base;

import java.util.List;
import com.tuantv.migrationstf.domain.FileInfo;

/**
 *
 * @author tuantran
 */
public interface FileRepository {
    
    void insertMany(List<FileInfo> fileInfos);
    
    List<FileInfo> getFiles(int skip, int take);
    
    long getFileNumber();
    
    void updateUploadTime(String fileId, long uploadTime);
    
    void showWorker();
}
