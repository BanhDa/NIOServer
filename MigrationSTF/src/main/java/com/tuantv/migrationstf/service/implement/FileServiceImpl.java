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
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
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
        int totalFile = (int) fileRepository.getFileNumber();
        
        System.out.println("TOTAL FILE : " +  totalFile);
        
        return totalFile;
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

    @Override
    public void createFileData(int number, int documentNumberPerAnInsert) {
        List<FileInfo> datas = new ArrayList<>();
        long time = System.currentTimeMillis();
        for (int i = 0; i < number; i++) {
            FileInfo fileInfo = createFileInfo();
            datas.add(fileInfo);
            if (datas.size() == documentNumberPerAnInsert) {
                fileRepository.insertMany(datas);
                datas.clear();
            }
        }
        
        System.out.println("INSERT COMPLETED! " + (System.currentTimeMillis() - time));
    }
    
    private FileInfo createFileInfo() {
        FileInfo fileInfo = new FileInfo();
        
        ObjectId id = new ObjectId();
        fileInfo.setId(id.toString());
        
        byte[] bytes = new byte[10];
        Random random = new Random();
        random.nextBytes(bytes);
        String url = new String(bytes, Charset.forName("UTF-8"));
        fileInfo.setUrl(url);
        
        random.nextBytes(bytes);
        String userId = new String(bytes, Charset.forName("UTF-8"));
        fileInfo.setUserId(userId);
        
        return fileInfo;
    }

    @Override
    public void updateUploadTimeByFileId() {
//        fileRepository.getFileNumber()
    }
    
}
