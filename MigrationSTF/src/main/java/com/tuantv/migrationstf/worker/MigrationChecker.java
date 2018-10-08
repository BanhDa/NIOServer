/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tuantv.migrationstf.worker;

/**
 *
 * @author tuantv
 */
public class MigrationChecker extends Thread{

    @Override
    public void run() {
        long time = System.currentTimeMillis();
        while (!MigrationSTFManagement.getInstance().isComplete()) {
        }
        System.out.println("MIGRATE COMPLETED : " + (System.currentTimeMillis() - time));
    }
    
}
