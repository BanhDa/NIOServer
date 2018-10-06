/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tuantv.migrationstf.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 *
 * @author tuantran
 */
@Getter
@Configuration
public class Config {
    
    @Value("${application.migrateSTF.maxMigrationSTFThread}")
    private int maxMigrationSTFThread;
    
    @Value("${application.migrateSTF.migratedFileNumberPerThread}")
    private int migratedFileNumberPerThread;
}
