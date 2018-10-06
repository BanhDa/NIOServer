/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tuantv.niosocketserver.nioserver.domain;

import java.nio.channels.SocketChannel;
import lombok.Data;

/**
 *
 * @author tuantran
 */
@Data
public class UserConnection {
    
    private String userId;
    
    private SocketChannel socketChannel;
    
}
