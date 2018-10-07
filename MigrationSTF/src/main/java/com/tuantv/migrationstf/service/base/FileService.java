/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tuantv.migrationstf.service.base;

/**
 *
 * @author tuantran
 */
public interface FileService {
    
    int getNumberFile();
    
    void updateUploadTimeByFileId(int skip, int take);
}
