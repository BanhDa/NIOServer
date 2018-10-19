/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tuantv.migrationstf.repository.base;

import com.mongodb.client.model.WriteModel;
import com.tuantv.migrationstf.domain.Domain;
import static java.lang.Thread.sleep;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;
import lombok.extern.slf4j.Slf4j;

/**
 *
 * @author tuantv
 * @param <T>
 */
@Slf4j
public abstract class BulkWritesRepository<T extends Domain> extends BaseRepository<T> {

    private final LinkedBlockingQueue<WriteModel> CONTAINER = new LinkedBlockingQueue<>();
    private BulkWritesWorker bulkWritesWorker;

    protected void getBulkWritesWorker() {
        System.out.println("bulkWritesWorker : " + bulkWritesWorker);
        if (bulkWritesWorker != null) {
            System.out.println("isAlive : " + bulkWritesWorker.isAlive());
        }
    }

    protected void putToBulkWrites(WriteModel writeModel) {
        if (writeModel != null) {
            CONTAINER.add(writeModel);
        }
        if (bulkWritesWorker == null) {
            bulkWritesWorker = new BulkWritesWorker(getBulkWriteNumber(), getBulkWriteTimeout());
            bulkWritesWorker.start();
        }
    }

    private class BulkWritesWorker extends Thread {

        private static final long FIVE_SECONDS = 5000;

        private final int bulkWritesNumber;
        private final int bulkWriteTimeout;

        public BulkWritesWorker(int bulkWritesNumber, int bulkWriteTimeout) {
            this.bulkWritesNumber = bulkWritesNumber;
            this.bulkWriteTimeout = bulkWriteTimeout;
        }

        @Override
        public void run() {
            List<WriteModel> writes = new ArrayList<>();
            long startTime = System.currentTimeMillis();
            long sleepTime = 0;
            while (true) {
                try {
                    WriteModel writeModel = CONTAINER.poll();
                    if (writeModel == null) {
                        if (sleepTime == FIVE_SECONDS) {
                            System.out.println("interrupted :" + this);
                            Thread.interrupted();
                        }
                        sleepTime += 5;
                        sleep(5);
                    } else {
                        writes.add(writeModel);
                        long duration = System.currentTimeMillis() - startTime;
                        if (writes.size() == bulkWritesNumber
                                || (duration == bulkWriteTimeout) && !writes.isEmpty()) {
                            getCollection().bulkWrite(writes);

                            startTime = System.currentTimeMillis();
                            writes.clear();
                        }
                    }
                } catch (Exception exception) {
                    log.error("", exception);
                }
            }
        }

    }

}
