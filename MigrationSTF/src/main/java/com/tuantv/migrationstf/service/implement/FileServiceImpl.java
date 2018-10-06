/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tuantv.migrationstf.service.implement;

import lombok.AllArgsConstructor;
import com.tuantv.migrationstf.service.base.FileService;
import com.tuantv.migrationstf.repository.base.FileRepository;
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
    public void migrationUploadTime() {
        
    }
    
    @Override
    public long getNumberFile() {
        return 0;
    }
}
