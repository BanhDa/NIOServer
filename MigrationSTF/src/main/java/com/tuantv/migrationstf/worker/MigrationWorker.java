/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tuantv.migrationstf.worker;

import com.tuantv.migrationstf.service.base.FileService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
/**
 *
 * @author tuantran
 */

@Data
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class MigrationWorker extends Thread{
    
    private final FileService fileService;
    private final long skip;
    private final long take;

    @Override
    public void run() {
        
    }
    
}
