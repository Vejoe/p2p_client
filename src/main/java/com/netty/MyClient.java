package com.netty;

import io.netty.bootstrap.Bootstrap;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;

public class MyClient {
    private EventLoopGroup myClientGroup;
    private ChannelFuture channelFuture;
    private Bootstrap bootstrap;
    private Channel channel;

    public ChannelFuture getChannelFuture(){
        return this.channelFuture;
    }

    public Channel getChannel() {
        return channel;
    }

    private static class SingletionMyClient{
        static final MyClient instance = new MyClient();
    }

    public static MyClient getInstance(){
        return SingletionMyClient.instance;
    }

    private MyClient(){
        this.myClientGroup = new NioEventLoopGroup();
        this.bootstrap = new Bootstrap();
        this.bootstrap.group(myClientGroup)
                .channel(NioSocketChannel.class)
                .handler(new MyClientInitializer());
    }

    public void start(){
        System.out.println("hello  hi");
        this.channelFuture=bootstrap.connect("localhost",8050);
        channel = channelFuture.channel();
        System.out.println("netty启动成功");
    }

    public void close(){
        channelFuture.channel().closeFuture();
        myClientGroup.shutdownGracefully();
        System.out.println("netty关闭成功！");
    }

}
