/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tuantv.niosocketserver.nioserver.repo;

import java.nio.channels.SocketChannel;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 *
 * @author tuantran
 */
public class ConnectionManagement {
    
    private static final ConnectionManagement INSTANCE = new ConnectionManagement();
    
    private ConcurrentLinkedQueue<SocketChannel> CONNECTION_IS_NOT_AUTH = new ConcurrentLinkedQueue<>();
    private ConcurrentHashMap<String, Set<SocketChannel>> USER_CONNECTIONS = new ConcurrentHashMap<>();
    
    public static ConnectionManagement getInstance() {
        return INSTANCE;
    }
    
    private ConnectionManagement() {}
    
    
}
