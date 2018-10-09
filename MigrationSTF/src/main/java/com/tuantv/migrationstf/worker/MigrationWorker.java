/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tuantv.migrationstf.worker;

import com.tuantv.migrationstf.service.base.FileService;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
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
    public static final Map<String, Boolean> CONTAINER = new ConcurrentHashMap<>();
    
    private final FileService fileService;
    private final int skip;
    private final int take;

    @Override
    public void run() {
        long startTime = System.currentTimeMillis();
//        int to = skip + take;
//        System.out.println("START MIGRATE DATA FROM " + skip + " to " + to);
//        fileService.updateUploadTimeByFileId(skip, take);
//        System.out.println("migration skip " + skip + " to " + to + " completed: " + (System.currentTimeMillis() - startTime));
    
        fileService.updateUploadTimeByFileId(skip, take);

        MigrationSTFManagement.getInstance().completeWorker(this);
    }
    
    private void test() {
        int total = skip + take;
        for (int i = skip; i < total; i++) {
            CONTAINER.put(String.valueOf(i), Boolean.TRUE);
        }
    }
    
}
