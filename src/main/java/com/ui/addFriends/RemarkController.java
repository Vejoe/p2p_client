package com.ui.addFriends;

import com.domain.SingletonMessage;
import com.netty.MyClient;
import com.protobuf.Message;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.internal.ChannelUtils;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import static com.protobuf.Message.MyMessage.ActionType.MakeFriendsAction;

public class RemarkController {
    @FXML
    TextField remarksText;
    @FXML
    Button sendButton;
    String userName;
    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void sendRequest(ActionEvent actionEvent) {
        //这里需要添加一个判断是否已是好友。
        Message.MyMessage myMessage = Message.MyMessage.newBuilder().setActionType(MakeFriendsAction).setRequest(
                Message.Request.newBuilder().setMakeFriendRequest(
                        Message.Make_Friend_Request.newBuilder().setSendUserName(SingletonMessage.getSingleton().getCurrentUser().getUsername()).setAcceptUserName(userName).setRemarks(remarksText.getText()).build()
                ).build()
        ).build();

       ChannelFuture channelFuture = MyClient.getInstance().getChannel().writeAndFlush(myMessage);
        channelFuture.addListener(
               new ChannelFutureListener() {
                   public void operationComplete(ChannelFuture channelFuture) throws Exception {
                       if(channelFuture.isSuccess()){
                           Platform.runLater(new Runnable() {
                               public void run() {
                                   Alert alert = new Alert(Alert.AlertType.INFORMATION);
                                   alert.setTitle("Success Dialog");
                                   alert.setHeaderText(null);
                                   alert.setContentText("消息发送！！");
                                   alert.showAndWait();
                                   Stage stage = (Stage)remarksText.getScene().getWindow();
                                   stage.close();
                               }
                           });
                       }
                   }
               });
    }
}
