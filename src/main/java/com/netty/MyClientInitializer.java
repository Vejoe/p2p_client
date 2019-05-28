package com.netty;

import com.protobuf.Message;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.protobuf.ProtobufDecoder;
import io.netty.handler.codec.protobuf.ProtobufEncoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32FrameDecoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32LengthFieldPrepender;
import io.netty.handler.timeout.IdleStateHandler;

import java.util.concurrent.TimeUnit;

public class MyClientInitializer extends ChannelInitializer<SocketChannel> {
    protected void initChannel(SocketChannel socketChannel) throws Exception {

        ChannelPipeline channelPipeline = socketChannel.pipeline();
        channelPipeline.addLast("ping", new IdleStateHandler(40, 20, 60 * 10, TimeUnit.SECONDS));

        channelPipeline.addLast(new ProtobufVarint32FrameDecoder());
        channelPipeline.addLast(new ProtobufDecoder(Message.MyMessage.getDefaultInstance()));
        channelPipeline.addLast(new ProtobufVarint32LengthFieldPrepender());
        channelPipeline.addLast(new ProtobufEncoder());
        channelPipeline.addLast(new ClientPoHandlerProto());
        channelPipeline.addLast(new MyClientHandler());

    }
}
