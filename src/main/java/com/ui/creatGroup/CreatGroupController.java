package com.ui.creatGroup;

import com.domain.SingletonMessage;
import com.netty.MyClient;
import com.protobuf.Message;
import com.ui.javacv.MyCameraUtil;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.net.URL;
import java.util.LinkedList;
import java.util.List;
import java.util.ResourceBundle;

import static com.protobuf.Message.MyMessage.ActionType.CreateGroupAction;

public class CreatGroupController implements Initializable {
    @FXML
    TextField groupName;
    @FXML
    ListView friendListView;
    @FXML
    Button submitButton;

    private ObservableList <CreatGroupOfItem> friendList = FXCollections.observableArrayList();

    private List<String> UserNameList = new LinkedList<String>();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
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
        CreatGroupOfItem creatGroupOfItem = null;
        for(int i = 0 ; i < SingletonMessage.getSingleton().getMyFriendsList().size();i++){
            creatGroupOfItem = new CreatGroupOfItem(SingletonMessage.getSingleton().getMyFriendsList().get(i).getUserName(),SingletonMessage.getSingleton().getMyFriendsList().get(i).getNickName());
            System.out.println();
            if(SingletonMessage.getSingleton().getMyFriendsList().get(i).getUserName().equals("100"))
                continue;
            friendList.add(creatGroupOfItem);
            creatGroupOfItem = null;
        }
    }

    public List<String> getUserNameList() {
        return UserNameList;
    }

    public void createGroupAction(ActionEvent actionEvent) {
        if(groupName.getText().trim().equals("") || UserNameList.size() == 0){
            //不能为空。
            return;
        }

        Message.Create_Group_Request.Builder group_user = Message.Create_Group_Request.newBuilder();
        group_user.setGroupName(groupName.getText().trim());
        group_user.setGroupCreator(SingletonMessage.getSingleton().getCurrentUser().getUsername());
        for(int i = 0 ; i < UserNameList.size();i++){
            group_user.addUserName(UserNameList.get(i).toString().trim());
        }
        group_user.addUserName(SingletonMessage.getSingleton().getCurrentUser().getUsername());
        Message.MyMessage myMessage = Message.MyMessage.newBuilder().setActionType(CreateGroupAction)
                .setRequest(Message.Request.newBuilder().setCreateGroupRequest(group_user.build())).build();
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                MyClient.getInstance().getChannel().writeAndFlush(myMessage);
            }
        });
        ((Stage)friendListView.getScene().getWindow()).close();
        CreatGroupFrame.getInstance().destoryInstance();
    }
}
