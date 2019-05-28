package com.ui.creatGroup;

import com.domain.SingletonMessage;
import com.domain.UserVO;
import com.netty.MyClient;
import com.protobuf.Message;
import com.ui.chatMain.ChatMain;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import static com.protobuf.Message.MyMessage.ActionType.ChangeGroupAction;
import static com.protobuf.Message.MyMessage.ActionType.QuitGroupAction;

public class GroupDetailController implements Initializable {
    private  String username;
    @FXML
    TextField groupName;
    @FXML
    ListView friendListView;
    private ObservableList<CreatGroupOfItem> friendList = FXCollections.observableArrayList();
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        username = ChatMain.getInstance().getChatController().getFriendUserName();
        groupName.setText(SingletonMessage.getSingleton().getMyGroupMap().get(username).getGroupName());
        friendListView.setCellFactory(new Callback<ListView<CreatGroupOfItem>, ListCell<CreatGroupOfItem>>()
        {
            public ListCell<CreatGroupOfItem> call(ListView<CreatGroupOfItem> listView)
            {
                return new CreatGroupOfItemCell();
            }
        });
        initFriendListView();
        friendListView.setItems(friendList);
    }

    /**
     * 初始化所有信息在CreatGroupOfItem中先。
     */
    public void initFriendListView(){
        for(int i = 0 ; i < SingletonMessage.getSingleton().getMyGroupMap().get(username).getGroup_User().size();i++){
            List<UserVO> UserVOlist = SingletonMessage.getSingleton().getMyGroupMap().get(username).getGroup_User();
            CreatGroupOfItem creatGroupOfItem = new CreatGroupOfItem(UserVOlist.get(i).getUsername(),UserVOlist.get(i).getNickname());
            System.out.println(UserVOlist.get(i).getUsername());
            creatGroupOfItem.getSelectIcon().setVisible(false);
            creatGroupOfItem.setFlag(1);
            friendList.add(creatGroupOfItem);
        }
    }

    /**
     * 改变群组名字
     * @param mouseEvent
     */
    public void changeGroupName(MouseEvent mouseEvent) {
        System.out.println("    public void changeGroupName(MouseEvent mouseEvent) !");
        Message.MyMessage myMessage = Message.MyMessage.newBuilder().setActionType(ChangeGroupAction)
                .setRequest(Message.Request.newBuilder().setChangeGroupMessage(Message.ChangeGroupMessage.newBuilder().setGroupName(groupName.getText()).setGroupUserName(username).build()).build()).build();
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                ChannelFuture channelFuture = MyClient.getInstance().getChannel().writeAndFlush(myMessage);
                ((Stage)(groupName.getScene().getWindow())).close();
                GroupDetailMessage.getInstance().destoryInstance();
            }
        });

    }

    /**
     * 退出群组
     * @param mouseEvent
     */
    public void closeGroup(MouseEvent mouseEvent) {
        System.out.println("    public void changeGroupName(MouseEvent mouseEvent) !");

        Message.MyMessage myMessage = Message.MyMessage.newBuilder().setActionType(QuitGroupAction)
                .setRequest(Message.Request.newBuilder().setQuitGroupRequest(Message.QuitGroupRequest.newBuilder().setGroupId(username).setQuitUserName(SingletonMessage.getSingleton().getCurrentUser().getUsername()).build()).build()).build();
        GroupDetailMessage.getInstance().destoryInstance();

        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                ChannelFuture channelFuture = MyClient.getInstance().getChannel().writeAndFlush(myMessage);
                ((Stage)(groupName.getScene().getWindow())).close();
                GroupDetailMessage.getInstance().destoryInstance();
                for(int i= 0;i<SingletonMessage.getSingleton().getNowChattingList().size();i++){
                    if(SingletonMessage.getSingleton().getNowChattingList().get(i).getUserName().equals(username)){
                        SingletonMessage.getSingleton().getNowChattingList().remove(i);
                    }
                }
            }
        });
    }


    public TextField getGroupName() {
        return groupName;
    }


}
