package com.Service;

import com.protobuf.Message;

public interface UserService {
    public void handleMakeFriendsAction(Message.MyMessage myMessage);
    public void handleQueryFriendsAction(Message.MyMessage myMessage);
    public void handleGetMyFriendsListAction(Message.MyMessage myMessage);
    public void handleChatMessageSendOrAcceptAction(Message.MyMessage myMessage);
    public void handleGroupMessageAction(Message.MyMessage myMessage);

    public void handleCameraOrVoiceResponseAction(Message.MyMessage myMessage);

   public void handleCameraOrVoiceRequestAction(Message.MyMessage myMessage);

    public void handleCloseCameraOrVoiceAction(Message.MyMessage myMessage);

    public void handleDeleteFriendAction(Message.MyMessage myMessage);
}
