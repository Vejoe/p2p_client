package com;

import com.netty.MyClientUdp;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.socket.DatagramPacket;

import java.net.InetSocketAddress;

public class Test {
    public static void main(String[] args) {
        MyClientUdp.getInstance().start();
        ChannelFuture channelFuture = MyClientUdp.getInstance().getChannel().writeAndFlush(new DatagramPacket(
                Unpooled.copiedBuffer(("ServerTop2p 11111 33323").getBytes()),
                new InetSocketAddress("localhost", 7011)));
        channelFuture.addListener(new ChannelFutureListener() {
            @Override
            public void operationComplete(ChannelFuture future) throws Exception {
                if(future.isSuccess()){
                    System.out.println("11111");
                }else{
                    System.out.println(22222222);
                }
            }
        });
    }
}
