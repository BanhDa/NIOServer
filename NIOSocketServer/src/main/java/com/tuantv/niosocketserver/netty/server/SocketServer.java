/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tuantv.niosocketserver.netty.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import java.net.InetSocketAddress;

/**
 *
 * @author tuantran
 */
public class SocketServer {
    
    public static void main(String[] args) {
        SocketServer socketServer = new SocketServer();
        try {
            socketServer.start();
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }
    }
    
    public void start() throws InterruptedException {
        EventLoopGroup eventLoopGroup = new NioEventLoopGroup();
        try {
            
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            serverBootstrap.group(eventLoopGroup);
            serverBootstrap.channel(NioServerSocketChannel.class);
            serverBootstrap.localAddress( new InetSocketAddress("localhost", 9090));
            
            serverBootstrap.childHandler( new ChannelInitializer<SocketChannel>() {
                @Override
                protected void initChannel(SocketChannel c) throws Exception {
                    c.pipeline().addLast(new ServerHandler());
                }
            });
            
            ChannelFuture channelFuture = serverBootstrap.bind().sync();
            channelFuture.channel().closeFuture().sync();
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            eventLoopGroup.shutdownGracefully().sync();
        }
    }
}
