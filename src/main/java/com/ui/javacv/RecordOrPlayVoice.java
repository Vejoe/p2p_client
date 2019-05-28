package com.ui.javacv;

import com.domain.SingletonMessage;
import com.netty.MyClientUdp;
import com.netty.UserUDPIPMap;
import io.netty.buffer.Unpooled;
import io.netty.channel.socket.DatagramPacket;
import sun.misc.BASE64Encoder;

import javax.sound.sampled.*;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketException;

/**
 * 声音录制并发送/接受声音播放（供语音交流）
 * @author weizhihao
 * @date 2019/4/14 13:40
 */
public class RecordOrPlayVoice {
    private boolean done = false;
    //采集格式，第一个采集的频率、此格式的声音样本中的位数、声道（1、单声道，2、立体声道）、、是以 big-endian 顺序还是以 little-endian 顺序存储音频数据。
    private AudioFormat format = new AudioFormat(44100, 16, 1, true, false);
    private DataLine.Info WriteInfo = null;
    private DataLine.Info ReadInfo = null;
    //从音频捕获设备获取其数据的数据线.
    private TargetDataLine WriteLine = null;
    private SourceDataLine ReadLine =null;
    VoiceRecordThread thread = new VoiceRecordThread();


    private RecordOrPlayVoice(){
        init();
    }
    private static RecordOrPlayVoice recordOrPlayVoice;
    public static synchronized RecordOrPlayVoice getSingleton(){
        if(recordOrPlayVoice == null){
            recordOrPlayVoice = new RecordOrPlayVoice();
        }
        return recordOrPlayVoice;
    }

    /**
     * 开始麦克风收集并发送Udp给对方。
     */
    public void startRecording(){
            thread.start();
    }

    public void playingVoice(byte[] friendVoice){
        //读取声音
        ReadLine.start();
        ReadLine.write(friendVoice,0,friendVoice.length);
    }

    /**
     * @throws LineUnavailableException
     * @throws SocketException
     *
     */
    public void init()
    {
        if(WriteLine == null){
            WriteInfo = new DataLine.Info(TargetDataLine.class, format);

            try{
                WriteLine = (TargetDataLine) AudioSystem.getLine(WriteInfo);
                WriteLine.open(format);
                WriteLine.start();
            }catch(LineUnavailableException l){
                l.printStackTrace();
            }
        }

        if(ReadLine == null){
            ReadInfo = new DataLine.Info(SourceDataLine.class,format);
            new DataLine.Info(SourceDataLine.class,format);
            try{
                ReadLine = (SourceDataLine)AudioSystem.getLine(ReadInfo);
                ReadLine.open(format);
            }catch(LineUnavailableException l){
                l.printStackTrace();
            }
        }
        done = false;
    }


    public void finish()
    {
        done = true;
    }

    public void close()
    {
        thread.setOpenvoice(false);
        if(WriteLine != null && WriteLine.isOpen())
        {
            WriteLine.close();
        }
        if(ReadLine != null && ReadLine.isOpen())
        {
            ReadLine.close();
        }
        recordOrPlayVoice = null;
    }

    class VoiceRecordThread extends Thread{
        boolean openvoice = true;

           public void setOpenvoice(boolean openvoice) {
               this.openvoice = openvoice;
           }

           public void run() {
               byte[] bytes = new byte[1024];
               int len ;
               if(!done){
                   ByteArrayOutputStream buffer ;
                   while(openvoice) {
                       //写入声音
                       buffer = new ByteArrayOutputStream();
                       len = WriteLine.read(bytes, 0, bytes.length);
                       buffer.write(bytes, 0, len);
                       System.out.println("bytes.size():" + (buffer).size());
                       BASE64Encoder base64Encoder = new BASE64Encoder();
                       //利用udp发送声音给对方
                       if(UserUDPIPMap.get(CameraFrameMain.getInstance().getCameraFrameController().getFrienduserName())!=null) {
                           MyClientUdp.getInstance().getChannel().writeAndFlush(new DatagramPacket(
                                   Unpooled.copiedBuffer((("Voice_Message "+ SingletonMessage.getSingleton().getCurrentUser().getUsername()+" "+CameraFrameMain.getInstance().getCameraFrameController().frienduserName+" " + base64Encoder.encode(buffer.toByteArray()))).getBytes()),
                                   UserUDPIPMap.get(CameraFrameMain.getInstance().getCameraFrameController().getFrienduserName())));
                       }
                       try{
                           buffer.close();
                       }catch (IOException io){
                           io.printStackTrace();
                       }
                   }
               }
           }
    }
}