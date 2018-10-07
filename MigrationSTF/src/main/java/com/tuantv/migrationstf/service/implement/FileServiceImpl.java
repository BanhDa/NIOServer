/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tuantv.migrationstf.service.implement;

import lombok.AllArgsConstructor;
import com.tuantv.migrationstf.service.base.FileService;
import com.tuantv.migrationstf.repository.base.FileRepository;
import com.tuantv.migrationstf.domain.FileInfo;
import java.util.List;
import java.util.stream.Collectors;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;

/**
 *
 * @author tuantran
 */
@Service
@AllArgsConstructor
public class FileServiceImpl implements FileService{
    
    private final FileRepository fileRepository;
    
    @Override
    public int getNumberFile() {
        return (int) fileRepository.getFileNumber();
    }

    @Override
    public void updateUploadTimeByFileId(int skip, int take) {
        List<FileInfo> fileInfos = fileRepository.getFiles(skip, take);
        
        fileInfos.parallelStream().filter(fileInfo -> {
            return fileInfo.getUploadTime() == null;
        }).forEach(fileInfo -> {
            String fileId = fileInfo.getId();
            ObjectId id = new ObjectId(fileId);
            long uploadTime = id.getDate().getTime();
            fileRepository.updateUploadTime(fileId, uploadTime);
        });
    }
    
}
