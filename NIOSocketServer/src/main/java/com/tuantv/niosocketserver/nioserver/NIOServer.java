/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tuantv.niosocketserver.nioserver;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

/**
 *
 * @author tuantran
 */
public class NIOServer {

    public static void main(String[] args) {
        try {
            InetAddress inetAddress = InetAddress.getByName("localhost");
            int port = 9090;

            Selector selector = Selector.open();

            ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
            serverSocketChannel.configureBlocking(false);
            serverSocketChannel.socket().bind(new InetSocketAddress(inetAddress, port));
            serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);

            while (true) {
                if (selector.select() > 0) {
                    processReadySet(selector.selectedKeys());
                }
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static void processReadySet(Set readySet) throws Exception {
        Iterator iterator = readySet.iterator();
        while (iterator.hasNext()) {
            SelectionKey key = (SelectionKey) iterator.next();
            iterator.remove();
            if (key.isAcceptable()) {
                ServerSocketChannel serverSocketChannel = (ServerSocketChannel) key.channel();
                SocketChannel socketChannel = serverSocketChannel.accept();
                socketChannel.configureBlocking(false);
                socketChannel.register(key.selector(), SelectionKey.OP_READ);
            }
            
            if (key.isReadable()) {
                String msg = processRead(key);
                if (msg.length() > 0) {
                    SocketChannel socketChannel = (SocketChannel) key.channel();
                    ByteBuffer buffer = ByteBuffer.wrap(msg.getBytes());
                    socketChannel.write(buffer);
                }
            }
        }
    }
    
    public static String processRead(SelectionKey key) throws IOException {
        SocketChannel socketChannel = (SocketChannel) key.channel();
        ByteBuffer buffer = ByteBuffer.allocate(1024);
        int bytesCount = socketChannel.read(buffer);
        if (bytesCount > 0) {
            buffer.flip();
            return new String(buffer.array());
        }
        return "NO_MESSAGE";
    }
    
}
