/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tuantv.migrationstf.worker;

import com.tuantv.migrationstf.service.base.FileService;
import com.tuantv.migrationstf.config.Config;
import com.tuantv.util.exception.ApplicationException;
/**
 *
 * @author tuantran
 */
public class MigrationSTFManagement {
    
    private static final MigrationSTFManagement INSTANCE = new MigrationSTFManagement();
    private static final int MAX_MIGRATION_THREAD_DEFAULT = 10;
    private static final int MIGRATED_FILE_NUMBER_PER_THREAD_DEFAULT = 1000;
    
    private static int FILE_NUMBER;
    private static int MIGRATED_FILE_COUNTER;
    private static int MIGRATING_FILE_NUMBER;
    
    private static int MIGRATION_THREAD_COUNTER;
    private static int MAX_MIGRATION_THREAD = MAX_MIGRATION_THREAD_DEFAULT;
    private static int MIGRATED_FILE_NUMBER_PER_THREAD = MIGRATED_FILE_NUMBER_PER_THREAD_DEFAULT;
    
    
    
    public static MigrationSTFManagement getInstance() {
        return INSTANCE;
    }
    
    private FileService fileService;
    private Config config;
    
    private MigrationSTFManagement() {}
    
    
    
    public void init(FileService xFileService, Config xConfig) {
        this.fileService = xFileService;
        this.config = xConfig;
        
        if (config != null) {
            if (config.getMaxMigrationSTFThread() > 0) {
                MAX_MIGRATION_THREAD = config.getMaxMigrationSTFThread();
            }
            if (config.getMigratedFileNumberPerThread() > 0) {
                MIGRATED_FILE_NUMBER_PER_THREAD = config.getMigratedFileNumberPerThread();
            }
        }
    }
    
    public void migrate() throws ApplicationException {
        if (fileService == null) {
            throw new ApplicationException("The FileService is not initial");
        }
        
        int fileNumber = fileService.getNumberFile();
        FILE_NUMBER = fileNumber;
        
        if (fileNumber <= MIGRATED_FILE_NUMBER_PER_THREAD) {
            new MigrationWorker(fileService, MIGRATING_FILE_NUMBER, fileNumber).start();
            MIGRATING_FILE_NUMBER = fileNumber;
        } else {
            long take = MIGRATING_FILE_NUMBER + MIGRATED_FILE_NUMBER_PER_THREAD;
            while (MIGRATION_THREAD_COUNTER < MAX_MIGRATION_THREAD
                    && take <= FILE_NUMBER) {
                new MigrationWorker(fileService, MIGRATING_FILE_NUMBER, MIGRATED_FILE_NUMBER_PER_THREAD).start();
                MIGRATING_FILE_NUMBER += MIGRATED_FILE_NUMBER_PER_THREAD;
                take += MIGRATED_FILE_NUMBER_PER_THREAD;
                MIGRATION_THREAD_COUNTER++;
            }
            
            if (MIGRATION_THREAD_COUNTER < MAX_MIGRATION_THREAD
                    && take > FILE_NUMBER) {
                new MigrationWorker(fileService, MIGRATING_FILE_NUMBER, FILE_NUMBER - MIGRATING_FILE_NUMBER).start();
                MIGRATING_FILE_NUMBER = FILE_NUMBER;
                MIGRATION_THREAD_COUNTER++;
            }
        }
    }
    
    public synchronized void completeWorker(MigrationWorker worker) {
        MIGRATED_FILE_COUNTER += worker.getTake();
        MIGRATION_THREAD_COUNTER--;
        if (MIGRATED_FILE_COUNTER == FILE_NUMBER) {
            return;
        }
        if (MIGRATION_THREAD_COUNTER < MAX_MIGRATION_THREAD
                    && MIGRATING_FILE_NUMBER < FILE_NUMBER) {
            if ( (MIGRATING_FILE_NUMBER + MIGRATED_FILE_NUMBER_PER_THREAD) > FILE_NUMBER) {
                new MigrationWorker(fileService, MIGRATING_FILE_NUMBER, FILE_NUMBER - MIGRATING_FILE_NUMBER).start();
                MIGRATING_FILE_NUMBER = FILE_NUMBER;
                MIGRATION_THREAD_COUNTER++;
            } else {
                new MigrationWorker(fileService, MIGRATING_FILE_NUMBER, MIGRATED_FILE_NUMBER_PER_THREAD).start();
                MIGRATING_FILE_NUMBER = FILE_NUMBER;
                MIGRATION_THREAD_COUNTER++;
            }
        }
    }
}
