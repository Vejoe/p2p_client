package com.ui.listItem;

import com.domain.SingletonMessage;
import com.netty.MyClient;
import com.protobuf.Message;
import com.ui.chatMain.ChatMain;
import com.util.Base64Utils;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;

import java.io.IOException;

import static com.protobuf.Message.AcceptOrRefuseFriends.AcceptOrRefuseType.Accept;
import static com.protobuf.Message.AcceptOrRefuseFriends.AcceptOrRefuseType.Refuse;
import static com.protobuf.Message.MyMessage.ActionType.AcceptOrRefuseFriendsAction;

public class NewFriendOfRightItem {
    String userName;
    @FXML
    Label nickNameLabel;
    @FXML
    Label timeLabel;
    @FXML
    Label remarksLabel;
    @FXML
    ImageView imageView;
    @FXML
    HBox hBoxItem;

    public NewFriendOfRightItem(String userName,String nickName ,String remarks , String timeLabel){
        String photoUrl = "/Image/"+userName+".jpg";
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/NewFriendOfRightItems.fxml"));
        loader.setController(this);
        try{
            loader.load();
        }catch(IOException e) {
            throw new RuntimeException(e);
        }
        if(!Base64Utils.photoValidation(this.getClass().getResource("/").getPath()+photoUrl)){
            photoUrl = "/Image/jaychou.jpg";
        }
        this.userName = userName;
        this.timeLabel.setText(timeLabel);
        this.nickNameLabel.setText(nickName);
        this.remarksLabel.setText(remarks);
        this.imageView.setImage(new Image(photoUrl));

    }

    public HBox gethBoxItem(){
        return hBoxItem;
    }

    public void  acceptFriend(MouseEvent mouseEvent) throws Exception{
        Message.MyMessage myMessage = Message.MyMessage.newBuilder()
                .setActionType(AcceptOrRefuseFriendsAction)
                .setRequest(
                        Message.Request.newBuilder().setAcceptOrRefuseFriends(
                                Message.AcceptOrRefuseFriends.newBuilder()
                                        .setSendUserName(userName)
                                        .setAcceptUserName(SingletonMessage.getSingleton().getCurrentUser().getUsername())
                                        .setAcceptOrRefuseType(Accept)
                                        .setRemarks(remarksLabel.getText())
                                        .setRequestDateTime(timeLabel.getText())
                                        .build()
                        ).build()
                ).build();
        MyClient.getInstance().getChannel().writeAndFlush(myMessage);
        SingletonMessage.getSingleton().getNewFriendList().remove(this);
    }

    public void  refuseFriend(MouseEvent mouseEvent) throws Exception{
        Message.MyMessage myMessage = Message.MyMessage.newBuilder()
                .setActionType(AcceptOrRefuseFriendsAction)
                .setRequest(
                        Message.Request.newBuilder().setAcceptOrRefuseFriends(
                                Message.AcceptOrRefuseFriends.newBuilder()
                                        .setSendUserName(userName)
                                        .setAcceptUserName(SingletonMessage.getSingleton().getCurrentUser().getUsername())
                                        .setAcceptOrRefuseType(Refuse)
                                        .setRemarks(remarksLabel.getText())
                                        .setRequestDateTime(timeLabel.getText())
                                        .build()
                        ).build()
                ).build();
        MyClient.getInstance().getChannel().writeAndFlush(myMessage);
        SingletonMessage.getSingleton().getNewFriendList().remove(this);
    }


}
