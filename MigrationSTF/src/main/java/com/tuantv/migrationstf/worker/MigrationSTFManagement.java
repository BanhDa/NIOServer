/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tuantv.migrationstf.worker;

import com.tuantv.migrationstf.service.base.FileService;
import com.tuantv.migrationstf.config.Config;
import com.tuantv.migrationstf.exception.ApplicationException;

/**
 *
 * @author tuantran
 */
public class MigrationSTFManagement {

    private static final MigrationSTFManagement INSTANCE = new MigrationSTFManagement();
    private static final int MAX_MIGRATION_THREAD_DEFAULT = 10;
    private static final int MIGRATED_FILE_NUMBER_PER_THREAD_DEFAULT = 1000;

    private static long START_TIME;
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

    private MigrationSTFManagement() {
    }

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

    private synchronized void increaseMigratingFileNumber(int number) {
        MIGRATING_FILE_NUMBER += number;
    }

    private synchronized int getMigratingFileNumber() {
        return MIGRATING_FILE_NUMBER;
    }

    public void migrate() throws ApplicationException {
        if (fileService == null) {
            throw new ApplicationException("The FileService is not initial");
        }

        START_TIME = System.currentTimeMillis();
        
        FILE_NUMBER = fileService.getNumberFile();

        if (FILE_NUMBER <= MIGRATED_FILE_NUMBER_PER_THREAD) {
            new MigrationWorker(fileService, getMigratingFileNumber(), FILE_NUMBER).start();
            increaseMigratingFileNumber(FILE_NUMBER);
        } else {
            createWorker();
        }
    }

    public synchronized void completeWorker(MigrationWorker worker) {
        MIGRATED_FILE_COUNTER += worker.getTake();
        MIGRATION_THREAD_COUNTER -= 1;
        if (isComplete()) {
            return;
        }
        createWorker();
    }

    private boolean isComplete() {
        if (MIGRATED_FILE_COUNTER == FILE_NUMBER) {
            System.out.println("DONE");
            System.out.println("TIME PROCESSS: " + (System.currentTimeMillis() - START_TIME));
            return true;
        }
        
        return false;
    }
    
    private void createWorker() {
        long take = getMigratingFileNumber() + MIGRATED_FILE_NUMBER_PER_THREAD;
        while (MIGRATION_THREAD_COUNTER < MAX_MIGRATION_THREAD
                && take <= FILE_NUMBER) {
            new MigrationWorker(fileService, getMigratingFileNumber(), MIGRATED_FILE_NUMBER_PER_THREAD).start();
            increaseMigratingFileNumber(MIGRATED_FILE_NUMBER_PER_THREAD);
            take += MIGRATED_FILE_NUMBER_PER_THREAD;
            MIGRATION_THREAD_COUNTER += 1;
        }

        if (MIGRATION_THREAD_COUNTER < MAX_MIGRATION_THREAD
                && take > FILE_NUMBER) {
            new MigrationWorker(fileService, getMigratingFileNumber(), FILE_NUMBER - getMigratingFileNumber()).start();
            increaseMigratingFileNumber(FILE_NUMBER - getMigratingFileNumber());
            MIGRATION_THREAD_COUNTER += 1;
        }
    }

}
