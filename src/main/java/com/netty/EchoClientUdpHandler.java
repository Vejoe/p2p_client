package com.netty;

import com.domain.SingletonMessage;
import com.ui.javacv.CameraFrameMain;
import com.ui.javacv.RecordOrPlayVoice;
import com.util.Base64Utils;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.socket.DatagramPacket;
import io.netty.util.CharsetUtil;
import io.netty.util.ReferenceCountUtil;
import javafx.application.Platform;

import java.net.InetSocketAddress;

public class EchoClientUdpHandler extends SimpleChannelInboundHandler<DatagramPacket> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, DatagramPacket msg) throws Exception {
        System.out.println("接收到信息！");

        ByteBuf buf = (ByteBuf) msg.copy().content();
        byte[] req = new byte[buf.readableBytes()];
        String[] fiendTemp =  buf.toString(CharsetUtil.UTF_8).split(" ");
        if(CameraFrameMain.getInstance() != null){
            CameraFrameMain.getInstance().getCameraFrameController().getFrienduserName();
        }
        System.out.println("fiendTemp[]:"+fiendTemp.length);
        //str[0]消息类型，str[1]发送者,str[2]:ip,str[3]:port
        if(fiendTemp[0].equals("ServerTop2p")){

            UserUDPIPMap.put(fiendTemp[1],new InetSocketAddress(fiendTemp[2],Integer.parseInt(fiendTemp[3])));
            ctx.channel().writeAndFlush(new DatagramPacket( Unpooled.copiedBuffer(("p2p "+ SingletonMessage.getSingleton().getCurrentUser().getUsername()).getBytes())
                    ,new InetSocketAddress(fiendTemp[2],Integer.parseInt(fiendTemp[3]))));
        }
        //str[0]消息类型,str[1]对方发送者
        if(fiendTemp[0].equals("p2p")){
            System.out.println("打洞成功！");
            UserUDPIPMap.put(fiendTemp[1],msg.sender());
        }
        if(fiendTemp[0].equals("Voice_Message")||fiendTemp[0].equals("Camera_Message")){
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    if(fiendTemp[0].equals("Voice_Message")){
                        RecordOrPlayVoice.getSingleton().playingVoice(Base64Utils.getStrToBytes(fiendTemp[3]));
                    }else if(fiendTemp[0].equals("Camera_Message")){
                        CameraFrameMain.getInstance().getCameraFrameController().setFriendByte(Base64Utils.getStrToBytes(fiendTemp[3]));
                    }
                }
            });
        }
        ReferenceCountUtil.release(buf);
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        Channel channel = ctx.channel();

    }
}
