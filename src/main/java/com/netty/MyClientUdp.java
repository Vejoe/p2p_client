package com.netty;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioDatagramChannel;

public class MyClientUdp {
    private Bootstrap bootstrap;
    private EventLoopGroup myClientUdpGroup;
    private Channel channel;
    private ChannelFuture channelFuture;
    private static  MyClientUdp instance;
    private int port = 0;
    private MyClientUdp(){
        bootstrap = new Bootstrap();
        myClientUdpGroup = new NioEventLoopGroup();
        bootstrap.group(myClientUdpGroup)
                    .channel(NioDatagramChannel.class)
                    .option(ChannelOption.RCVBUF_ALLOCATOR, new FixedRecvByteBufAllocator(65535))//加上这个，里面是最大接收、发送的长度
                    .option(ChannelOption.SO_BROADCAST, true)
                    .handler(new EchoClientUdpHandler());
    }



    public static MyClientUdp getInstance(){
        if(instance == null){
            instance = new MyClientUdp();
        }
        return MyClientUdp.instance;
    }
    public void start(){
        try {
            this.channelFuture = bootstrap.bind(port).sync();
            this.channel = channelFuture.channel();
        }catch (InterruptedException in){
            in.printStackTrace();
        }
    }

    public void close(){
        if(channelFuture != null){
            channelFuture.channel().closeFuture();
            myClientUdpGroup.shutdownGracefully();
            System.out.println("netty Udp关闭成功！");
        }
    }

    public Channel getChannel() {
        return channel;
    }

    public static void setInstance(MyClientUdp instance) {
        MyClientUdp.instance = instance;
    }
}
