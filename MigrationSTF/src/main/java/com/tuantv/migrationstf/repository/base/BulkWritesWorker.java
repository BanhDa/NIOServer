/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tuantv.migrationstf.repository.base;

import com.mongodb.client.model.WriteModel;
import java.util.concurrent.LinkedBlockingQueue;
import lombok.extern.slf4j.Slf4j;

/**
 *
 * @author tuantv
 */
@Slf4j
public abstract class BulkWritesWorker extends Thread{
    
    private LinkedBlockingQueue<WriteModel> CONTAINER = new LinkedBlockingQueue<>();
    
    public void put(WriteModel writeModel) {
        try {
            CONTAINER.put(writeModel);
        } catch (InterruptedException ex) {
            log.error("WriteModel : " + writeModel + " error!", ex);
        }
    }

    @Override
    public void run() {
        
    }
    
    
}
