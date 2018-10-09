/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tuantv.migrationstf;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import com.tuantv.migrationstf.worker.MigrationSTFManagement;
import com.tuantv.migrationstf.service.base.FileService;
import com.tuantv.migrationstf.config.Config;
import lombok.AllArgsConstructor;

/**
 *
 * @author tuantran
 */
@AllArgsConstructor
@SpringBootApplication
public class Application implements CommandLineRunner {

    private final FileService fileService;
    private final Config config;

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
//        insertData();
        migrate();
    }

    private void insertData() {
        fileService.createFileData(config.getCreateFileDataNumber(), config.getDocumentNumberPerAnInsert());
    }
    
    private void migrate() {
        MigrationSTFManagement.getInstance().init(fileService, config);
        MigrationSTFManagement.getInstance().migrate();
    }
}
