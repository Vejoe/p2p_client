package com.netty;

import com.Service.ServiceImpl.UserServiceImpl;
import com.Service.UserService;
import com.domain.UserVO;
import com.protobuf.Message;
import com.domain.SingletonMessage;
import com.ui.listItem.OneOfListItems;
import com.util.Base64Utils;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import javafx.application.Platform;

import static com.protobuf.Message.MyMessage.ActionType.*;

public class MyClientHandler extends SimpleChannelInboundHandler<Message.MyMessage> {
    UserService userService = new UserServiceImpl();



    protected void channelRead0(ChannelHandlerContext channelHandlerContext, Message.MyMessage myMessage) {
        System.out.println(myMessage);
        if(myMessage.getActionType() == QueryFriends){
            userService.handleQueryFriendsAction(myMessage);
        }else if(myMessage.getActionType() == MakeFriendsAction){
            userService.handleMakeFriendsAction(myMessage);
        }else if(myMessage.getActionType() == GetMyFriendsListAction){
            userService.handleGetMyFriendsListAction(myMessage);
        }else if(myMessage.getActionType() == ChatMessageSendOrAccept){
            userService.handleChatMessageSendOrAcceptAction(myMessage);
        }else if(myMessage.getActionType() == CreateGroupAction){
            userService.handleGroupMessageAction(myMessage);
        }else if(myMessage.getActionType() == CameraOrVoiceRequestAction){
            userService.handleCameraOrVoiceRequestAction(myMessage);
        }else if(myMessage.getActionType() == CameraOrVoiceResponseAction){
            userService.handleCameraOrVoiceResponseAction(myMessage);
        }else if(myMessage.getActionType() == CloseCameraOrVoiceAction){
            userService.handleCloseCameraOrVoiceAction(myMessage);
        }else if(myMessage.getActionType() == DeleteFriendAction){
            userService.handleDeleteFriendAction(myMessage);
        }
    }
}
