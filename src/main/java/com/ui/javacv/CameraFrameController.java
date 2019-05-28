package com.ui.javacv;

import com.domain.SingletonMessage;
import com.netty.MyClient;
import com.netty.MyClientUdp;
import com.protobuf.Message;
import com.ui.chatMain.ChatMain;
import com.util.Base64Utils;
import io.netty.buffer.Unpooled;
import io.netty.channel.socket.DatagramPacket;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.net.InetSocketAddress;
import java.net.URL;
import java.util.ResourceBundle;

import static com.protobuf.Message.CameraOrVoiceRequest.CameraOrVoiceType.Camera;
import static com.protobuf.Message.CameraOrVoiceRequest.CameraOrVoiceType.Voice;
import static com.protobuf.Message.CameraOrVoiceResponse.AcceptOrNo.Accept;
import static com.protobuf.Message.MyMessage.ActionType.*;

public class CameraFrameController implements Initializable {
    @FXML
    Pane cameraPaneFirst,cameraPaneSecond ,voicePane;
    @FXML
    ImageView myImageView,friendImageView,cameraUserImage,cameraUserImage1;
    @FXML
    ImageView acceptIcon,refuseIcon,closeIcon,closeIcon1;

    private byte[] friendByte = null;

    String frienduserName =  "";

    private boolean hasCapture = true;
    private boolean opener = true;

    public CameraFrameController(String userName,boolean opener,boolean hasCapture){
        this.opener = opener;
        this.hasCapture = hasCapture;
        this.frienduserName = userName;
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        System.out.println("hasCapture:"+hasCapture);
        closeIcon1.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                Message.MyMessage myMessage = Message.MyMessage.newBuilder()
                        .setActionType(CloseCameraOrVoiceAction)
                        .setCloseCameraOrVoice(
                                Message.CloseCameraOrVoice.newBuilder().setCloseUserName(SingletonMessage.getSingleton().getCurrentUser().getUsername())
                                        .setAcceptUserName(frienduserName)
                                        .setCloseRemark("对方关闭了音视频窗体！")
                                        .build()
                        ).build();
                MyClient.getInstance().getChannel().writeAndFlush(myMessage);
                MyClientUdp.getInstance().close();
                MyClientUdp.getInstance().setInstance(null);
                RecordOrPlayVoice.getSingleton().close();
                MyCameraUtil.getSingleton().setClosed();
                ((Stage)closeIcon.getScene().getWindow()).close();
                CameraFrameMain.getInstance().destoryInstance();
            }
        });
        //关闭音视频聊天
        closeIcon.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                Message.MyMessage myMessage = Message.MyMessage.newBuilder()
                        .setActionType(CloseCameraOrVoiceAction)
                        .setCloseCameraOrVoice(
                                Message.CloseCameraOrVoice.newBuilder().setCloseUserName(SingletonMessage.getSingleton().getCurrentUser().getUsername())
                                .setAcceptUserName(frienduserName)
                                .setCloseRemark("对方关闭了音视频窗体！")
                                .build()
                        ).build();
                MyClient.getInstance().getChannel().writeAndFlush(myMessage);
                MyClientUdp.getInstance().close();
                MyClientUdp.getInstance().setInstance(null);
                RecordOrPlayVoice.getSingleton().close();
                MyCameraUtil.getSingleton().setClosed();
                ((Stage)closeIcon.getScene().getWindow()).close();
                CameraFrameMain.getInstance().destoryInstance();
            }
        });
        //接受按钮的事件
        acceptIcon.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                System.out.println("public void acceptRequest(ActionEvent actionEvent) ");
                startUDP();
                if(hasCapture){
                    System.out.println("开始视频聊天了！");
                    cameraPaneFirst.setVisible(false);
                    cameraPaneSecond.setVisible(true);
                    voicePane.setVisible(false);
                    startCamera();
                    startVoice();
                    Message.MyMessage myMessage = Message.MyMessage.newBuilder().setActionType(CameraOrVoiceResponseAction)
                            .setResponse(Message.Response.newBuilder().setCameraOrVoiceResponse(
                                    Message.CameraOrVoiceResponse.newBuilder().setSender(SingletonMessage.getSingleton().getCurrentUser().getUsername())
                                            .setAccepter(frienduserName)
                                            .setRefuseRemark("")
                                            .setCameraOrVoiceType(Message.CameraOrVoiceResponse.CameraOrVoiceType.Camera)
                                            .setAcceptOrNo(Accept).build()
                            ).build()).build();
                    MyClient.getInstance().getChannel().writeAndFlush(myMessage);
                }else {
                    System.out.println("开始语音聊天！");
                    cameraPaneFirst.setVisible(false);
                    cameraPaneSecond.setVisible(false);
                    voicePane.setVisible(true);
                    startVoice();
                    Message.MyMessage myMessage = Message.MyMessage.newBuilder().setActionType(CameraOrVoiceResponseAction)
                            .setResponse(Message.Response.newBuilder().setCameraOrVoiceResponse(
                                    Message.CameraOrVoiceResponse.newBuilder().setSender(SingletonMessage.getSingleton().getCurrentUser().getUsername())
                                    .setAccepter(frienduserName)
                                    .setRefuseRemark("")
                                    .setCameraOrVoiceType(Message.CameraOrVoiceResponse.CameraOrVoiceType.Voice)
                                    .setAcceptOrNo(Accept).build()
                            ).build()).build();
                    MyClient.getInstance().getChannel().writeAndFlush(myMessage);
                }
            }
        });
        //拒绝请求的按钮事件
        refuseIcon.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                System.out.println("public void refuseRequest(ActionEvent actionEvent) ");
                Message.MyMessage newMessage = Message.MyMessage.newBuilder().setActionType(CameraOrVoiceResponseAction)
                        .setResponse(Message.Response.newBuilder().setCameraOrVoiceResponse(
                                Message.CameraOrVoiceResponse.newBuilder().setSender(SingletonMessage.getSingleton().getCurrentUser().getUsername()).setAccepter(frienduserName)
                                        .setRefuseRemark("拒绝通信！")
                                        .setAcceptOrNo(Message.CameraOrVoiceResponse.AcceptOrNo.Refuse)
                                        .setCameraOrVoiceType(Message.CameraOrVoiceResponse.CameraOrVoiceType.Camera)
                                        .build()
                        ).build()).build();
                MyClient.getInstance().getChannel().writeAndFlush(newMessage);
                ((Stage)(cameraPaneFirst.getScene().getWindow())).close();
                CameraFrameMain.getInstance().destoryInstance();
            }
        });

        if(opener){
            cameraPaneFirst.setVisible(false);
            cameraPaneSecond.setVisible(false);
            voicePane.setVisible(true);
            if(hasCapture){
                System.out.println("发送视频请求：sendCameraRequest()");
                sendCameraRequest();
            }else {
                sendVoiceRequest();
                System.out.println("发送语音请求：sendVoiceRequest()");
            }
        }else {
            cameraPaneFirst.setVisible(true);
            cameraPaneSecond.setVisible(false);
            voicePane.setVisible(false);
        }

        if(Base64Utils.photoValidation(this.getClass().getResource("/").getPath()+"/Image/"+ frienduserName+".jpg")){
            cameraUserImage.setImage(new Image("/Image/"+ frienduserName+".jpg"));
            cameraUserImage1.setImage(new Image("/Image/"+ frienduserName+".jpg"));
        }else{
            cameraUserImage.setImage(new Image("/Image/jaychou.jpg"));
            cameraUserImage1.setImage(new Image("/Image/jaychou.jpg"));
        }



    }

    public byte[] getFriendByte() {
        return friendByte;
    }

    public void setFriendByte(byte[] friendByte) {
        this.friendByte = friendByte;
    }

    public ImageView getMyImageView() {
        return myImageView;
    }

    public ImageView getFriendImageView() {
        return friendImageView;
    }

    public boolean isHasCapture() {
        return hasCapture;
    }

    public void setHasCapture(boolean hasCapture) {
        this.hasCapture = hasCapture;
    }

    public String getFrienduserName() {
        return frienduserName;
    }

    public boolean isOpener() {
        return opener;
    }

    public void setOpener(boolean opener) {
        this.opener = opener;
    }
    public void startUDP(){
        MyClientUdp.getInstance().start();
        MyClientUdp.getInstance().getChannel().writeAndFlush(new DatagramPacket(
                Unpooled.copiedBuffer(("ServerTop2p "+SingletonMessage.getSingleton().getCurrentUser().getUsername()+" "+frienduserName).getBytes()),
                new InetSocketAddress("localhost", 7011)));
    }
    public void startVoice(){
        RecordOrPlayVoice.getSingleton().startRecording();
    }
    public void startCamera(){
        MyCameraUtil.getSingleton().startCamera();
    }


    /**
     * 发送视频请求
     */
    public void sendCameraRequest(){
        Message.MyMessage myMessage = Message.MyMessage.newBuilder()
                .setActionType(CameraOrVoiceRequestAction)
                .setRequest(Message.Request.newBuilder().setCameraOrVoiceRequest(
                        Message.CameraOrVoiceRequest.newBuilder().setSender(SingletonMessage.getSingleton().getCurrentUser().getUsername())
                        .setAccepter(frienduserName)
                        .setCameraOrVoiceType(Camera)
                        .build()
                ).build()).build();
        MyClient.getInstance().getChannel().writeAndFlush(myMessage);
    }

    /**
     * 发送语音聊天
     */
    public void sendVoiceRequest(){
        Message.MyMessage myMessage = Message.MyMessage.newBuilder()
                .setActionType(CameraOrVoiceRequestAction)
                .setRequest(Message.Request.newBuilder().setCameraOrVoiceRequest(
                        Message.CameraOrVoiceRequest.newBuilder().setSender(SingletonMessage.getSingleton().getCurrentUser().getUsername())
                                .setAccepter(frienduserName)
                                .setCameraOrVoiceType(Voice)
                                .build()
                ).build()).build();
        MyClient.getInstance().getChannel().writeAndFlush(myMessage);
    }

    public void setCameraPane(){
        cameraPaneFirst.setVisible(false);
        cameraPaneSecond.setVisible(true);
        voicePane.setVisible(false);
    }
}
