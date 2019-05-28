package com.ui.addFriends;

import com.netty.MyClient;
import com.protobuf.Message;
import com.domain.SingletonMessage;
import com.ui.listItem.OneOfListViewCell;
import com.ui.listItem.OneOfListItems;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.util.Callback;

import java.net.URL;
import java.util.ResourceBundle;

import static com.protobuf.Message.MyMessage.ActionType.QueryFriends;

public class AddFriendController implements Initializable {
    @FXML
    ListView messageList;
    @FXML
    TextField userName;



    public void initialize(URL location, ResourceBundle resources) {
        messageList.setCellFactory(
                new Callback<ListView<OneOfListItems>, ListCell<OneOfListItems>>() {
                    public ListCell<OneOfListItems> call(ListView<OneOfListItems> listView) {
                        return new OneOfListViewCell();
                    }
                }
        );
        messageList.setItems(SingletonMessage.getSingleton().getFindFriendList());
    }

    public void findFriendButton(ActionEvent actionEvent) throws Exception {
        SingletonMessage.getSingleton().getFindFriendList().removeAll(SingletonMessage.getSingleton().getFindFriendList());
        Message.MyMessage myMessage = Message.MyMessage.newBuilder()
                .setActionType(QueryFriends)
                .setRequest(
                        Message.Request.newBuilder().setQueryFriendsRequest(
                                Message.Query_Friends_Request.newBuilder().setUserName(userName.getText().trim()).build()
                        ).build()
                ).build();
        MyClient.getInstance().getChannel().writeAndFlush(myMessage);
        messageList.refresh();
    }

}
