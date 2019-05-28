package com.ui.chatMain;

import com.domain.SingletonMessage;
import com.netty.MyClient;
import com.protobuf.Message;
import com.ui.MyMessage.MyMessage;
import com.ui.addFriends.AddFriendMain;
import com.ui.creatGroup.CreatGroupFrame;
import com.ui.creatGroup.GroupDetailMessage;
import com.ui.javacv.CameraFrameMain;
import com.ui.listItem.*;
import com.util.Base64Utils;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.io.*;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ResourceBundle;

import static com.protobuf.Message.MyMessage.ActionType.*;

public class ChatController implements Initializable {

    private double xOffset = 0;
    private double yOffset = 0;
    @FXML
    private Label tempView;
    private String friendNickName = "";
    private String friendUserName ="";
    @FXML
    ImageView chatMessageIcon,linkManIcon,minimumIcon,closeIcon,MyImage,detailMessage_Image;
    @FXML
    Pane messageListWindow,friendsListWindow;
    @FXML
    Pane acceptMessageWindow,newFriendsListWindow,friendMessageShow,sendMessageWindow;
    @FXML
    ListView messageList;
    @FXML
    ListView friendsList;
    @FXML
    ListView acceptListView;
    @FXML
    Label right_top , detailMessage_nickName ,detailMessage_userName;
    @FXML
    TextArea send_messageText;
    @FXML
    VBox friendMessageVbox;
    @FXML
    ImageView fileIcon,voiceIcon,captureIcon;

    public ListView getNewFriendsList() {
        return newFriendsList;
    }

    @FXML
    ListView newFriendsList;
    private AddFriendMain addFriendMain = null;
    final SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");


    public void initialize(URL location, ResourceBundle resources) {
        tempView.setText("消息列表");
        chatMessageIcon.setImage(new Image("/icon/chatMessage_OnPress.png"));
        friendMessageVbox.setVisible(false);
        friendsListWindow.setVisible(false);
        newFriendsListWindow.setVisible(false);
        friendMessageShow.setVisible(true);
        acceptMessageWindow.setVisible(false);
        sendMessageWindow.setVisible(false);

        friendsList.setCellFactory(new Callback<ListView<OneOfListItems>, javafx.scene.control.ListCell<OneOfListItems>>()
        {
            public ListCell<OneOfListItems> call(ListView<OneOfListItems> listView)
            {
                return new OneOfListViewCell();
            }
        });

        messageList.setCellFactory(new Callback<ListView<OneOfListItems>, javafx.scene.control.ListCell<OneOfListItems>>()
        {
            public ListCell<OneOfListItems> call(ListView<OneOfListItems> listView)
            {
                return new OneOfListViewCell();
            }
        });

        newFriendsList.setCellFactory(new Callback<ListView<NewFriendOfRightItem>, javafx.scene.control.ListCell<NewFriendOfRightItem>>()
        {
            public ListCell<NewFriendOfRightItem> call(ListView<NewFriendOfRightItem> listView)
            {
                return new NewFriendsListViewCell();
            }
        });

        acceptListView.setCellFactory(new Callback<ListView<AcceptMessageOfItem>, javafx.scene.control.ListCell<AcceptMessageOfItem>>()
        {
            public ListCell<AcceptMessageOfItem> call(ListView<AcceptMessageOfItem> listView)
            {
                return new AcceptMessageOfItemCell();
            }
        });

//        //接受聊天框的listView设置
//        acceptListView.setItems(SingletonMessage.getSingleton().getCurrentChattingList());

        //右侧新朋友框的listView设置
        newFriendsList.setItems(SingletonMessage.getSingleton().getNewFriendList());
        //左侧朋友框的listView设置
        friendsList.setItems(SingletonMessage.getSingleton().getMyFriendsList());
        SingletonMessage.getSingleton().getMyFriendsList().add(new OneOfListItems("100", "新的朋友",2));
        //左侧聊天的listView设置
        messageList.setItems(SingletonMessage.getSingleton().getNowChattingList());

        SingletonMessage.getSingleton().getChatMessageMap().put("weizhihao1",null);

        if(Base64Utils.photoValidation(this.getClass().getResource("/").getPath()+"/Image/"+ SingletonMessage.getSingleton().getCurrentUser().getUsername()+".jpg")){
            MyImage.setImage(new Image("/Image/"+ SingletonMessage.getSingleton().getCurrentUser().getUsername()+".jpg"));
        }else{
            MyImage.setImage(new Image("/Image/jaychou.jpg"));
        }

 }


    public void chatMessagePress(MouseEvent mouseEvent) {
        tempView.setText("消息列表");
        chatMessageIcon.setImage(new Image("/icon/chatMessage_OnPress.png"));
        linkManIcon.setImage(new Image("/icon/linkMan.png"));
        messageListWindow.setVisible(true);
        friendsListWindow.setVisible(false);
        sendMessageWindow.setVisible(true);
        acceptMessageWindow.setVisible(true);
        newFriendsListWindow.setVisible(false);
        friendMessageShow.setVisible(false);
    }

    public void linkManPress(MouseEvent mouseEvent) {
        tempView.setText("好友列表");
        chatMessageIcon.setImage(new Image("/icon/chatMessage.png"));
        linkManIcon.setImage(new Image("/icon/linkMan_OnPress.png"));
        messageListWindow.setVisible(false);
        friendsListWindow.setVisible(true);
        sendMessageWindow.setVisible(false);
        acceptMessageWindow.setVisible(false);
        newFriendsListWindow.setVisible(false);
        friendMessageShow.setVisible(true);
    }


    public void SendMessageAction(ActionEvent actionEvent) {
         final String msg = send_messageText.getText();
         if(msg.equals(""))
             return;

         Message.MyMessage _myMessage = Message.MyMessage.newBuilder()
                .setActionType(ChatMessageSendOrAccept)
                .setRequest(Message.Request.newBuilder().setSendChatMessageRequest(
                        Message.Send_ChatMessage_Request.newBuilder()
                                .setSendUserId(SingletonMessage.getSingleton().getCurrentUser().getUsername())
                                .setAcceptUserId(friendUserName)
                                .setMsg(msg)
                                .setMsgType("1")
                                .setCreateTime(df.format(new Date()))
                                .build()).build()).build();

                 ChannelFuture channelFuture = MyClient.getInstance().getChannel().writeAndFlush(_myMessage);
                 channelFuture.addListener(new ChannelFutureListener() {
                     public void operationComplete(ChannelFuture channelFuture) throws Exception {
                         if(channelFuture.isSuccess()){
                             System.out.println("发送成功");
                             Platform.runLater(new Runnable() {
                                 public void run() {
                                     ChatMain.getInstance().getChatController().getAcceptListView().setItems(FXCollections.observableArrayList());
//                                     SingletonMessage.getSingleton().getCurrentChattingList().add(new AcceptMessageOfItem(SingletonMessage.getSingleton().getCurrentUser().getUsername(), SingletonMessage.getSingleton().getCurrentUser().getNickname(), df.format(new Date()), msg));
                                     SingletonMessage.getSingleton().getChatMessageMap().get(friendUserName).add(
                                             new AcceptMessageOfItem(SingletonMessage.getSingleton().getCurrentUser().getUsername(),SingletonMessage.getSingleton().getCurrentUser().getNickname(), df.format(new Date()), msg,1));
                                     ChatMain.getInstance().getChatController().getAcceptListView().setItems(SingletonMessage.getSingleton().getChatMessageMap().get(friendUserName));
                                     System.out.println(friendUserName+"信息个数："+SingletonMessage.getSingleton().getChatMessageMap().get(friendUserName).size());
                                 }
                             });
                         }else{
                             Platform.runLater(new Runnable() {
                                 public void run() {
                                     Alert alert = new Alert(Alert.AlertType.ERROR);
                                     alert.setTitle("Error Message");
                                     alert.setHeaderText(null);
                                     alert.setContentText("网络不通，发送失败！");
                                     alert.showAndWait();
                                 }
                             });

                         }
                     }});

        send_messageText.setText("");
    }
    public void minimumAction(MouseEvent mouseEvent) {
        ((Stage)chatMessageIcon.getScene().getWindow()).setIconified(true);
    }

    public void closeAction(MouseEvent mouseEvent) {
        Message.MyMessage myMessage = Message.MyMessage.newBuilder()
            .setActionType(CloseTCPConnection).setCloseTCPConnection(
                    Message.CloseTCPConnection.newBuilder().setCloseUserName(SingletonMessage.getSingleton().getCurrentUser().getUsername()).build()
                ).build();
        MyClient.getInstance().getChannel().writeAndFlush(myMessage);
        MyClient.getInstance().close();
        ((Stage)chatMessageIcon.getScene().getWindow()).close();
    }

    public void frameOnPress(MouseEvent mouseEvent) {
        xOffset = mouseEvent.getSceneX();
        yOffset = mouseEvent.getSceneY();
    }

    public void frameDragged(MouseEvent mouseEvent) {
        chatMessageIcon.getScene().getWindow().setX(mouseEvent.getScreenX() - xOffset);
        chatMessageIcon.getScene().getWindow().setY(mouseEvent.getScreenY() - yOffset);
    }


    public void addFriendClicked(MouseEvent mouseEvent) throws Exception{
        if(addFriendMain == null){
            addFriendMain = AddFriendMain.getInstance();
        }
        addFriendMain.addFriendshow();
    }

    public void addGroupClicked(MouseEvent mouseEvent) {
        CreatGroupFrame.getInstance().showPrimaryStage();

    }


    public Pane getAcceptMessageWindow() {
        return acceptMessageWindow;
    }

    public Pane getNewFriendsListWindow() {
        return newFriendsListWindow;
    }

    public Pane getFriendMessageShow() {
        return friendMessageShow;
    }

    public Pane getSendMessageWindow() {
        return sendMessageWindow;
    }

    public String getFriendUserName() {return friendUserName; }

    public void setFriendUserName(String firendUserName) { this.friendUserName = firendUserName;  }

    public String getFriendNickName() {
        return friendNickName;
    }

    public void setFriendNickName(String friendNickName) {
        this.friendNickName = friendNickName;
    }

    public Label getRight_top() {
        return right_top;
    }

    public ListView getAcceptListView() {
        return acceptListView;
    }


    public void sendToSelectFriend(ActionEvent actionEvent) {
        int count = SingletonMessage.getSingleton().getNowChattingList().size();
        if(count == 0){
            OneOfListItems oneOfListItems = new OneOfListItems(detailMessage_userName.getText(),detailMessage_nickName.getText(),0);
            SingletonMessage.getSingleton().getNowChattingList().add(0,oneOfListItems);
            System.out.println("getNowChattingList():"+SingletonMessage.getSingleton().getNowChattingList().size());
        }
        for(int i = 0;i<count;i++){
            //已存在则不添加
            if(SingletonMessage.getSingleton().getNowChattingList().get(i).getUserName().equals(detailMessage_userName.getText())){
                break;
            }
            if(i == count-1){
                OneOfListItems oneOfListItems = new OneOfListItems(detailMessage_userName.getText(),detailMessage_nickName.getText(),0);
                SingletonMessage.getSingleton().getNowChattingList().add(0,oneOfListItems);
                System.out.println("getNowChattingList():"+SingletonMessage.getSingleton().getNowChattingList().size());
            }
        }
    }

    public void deleteSelectFriend(ActionEvent actionEvent) {
        Message.MyMessage myMessage = Message.MyMessage.newBuilder()
                .setActionType(DeleteFriendAction)
                .setRequest(
                        Message.Request.newBuilder()
                                .setDeleteFriendRequest(
                                        Message.DeleteFriendRequest.newBuilder().setSenderUserName(SingletonMessage.getSingleton().getCurrentUser().getUsername())
                                        .setDeletedUserName(detailMessage_userName.getText()).build()
                                ).build()
                ).build();

        Platform.runLater(new Runnable() {
            @Override
            public void run() {

                if(SingletonMessage.getSingleton().getMyFriendVOMap().get(detailMessage_userName.getText())!= null){
                    System.out.println("SingletonMessage.getSingleton().getMyFriendVOMap():"+SingletonMessage.getSingleton().getMyFriendVOMap().size());
                    SingletonMessage.getSingleton().getMyFriendVOMap().remove(detailMessage_userName.getText());
                    System.out.println("SingletonMessage.getSingleton().getMyFriendVOMap():"+SingletonMessage.getSingleton().getMyFriendVOMap().size());
                }
                if(SingletonMessage.getSingleton().getChatMessageMap().get(detailMessage_userName.getText())!=null){
                    System.out.println("SingletonMessage.getSingleton().getChatMessageMap():"+SingletonMessage.getSingleton().getChatMessageMap().size());
                    SingletonMessage.getSingleton().getChatMessageMap().remove(detailMessage_userName.getText());
                    System.out.println("SingletonMessage.getSingleton().getChatMessageMap():"+SingletonMessage.getSingleton().getChatMessageMap().size());
                }
                for(int i = 0 ;i < SingletonMessage.getSingleton().getMyFriendsList().size() ; i++){

                    if(SingletonMessage.getSingleton().getMyFriendsList().get(i).getUserName().equals(detailMessage_userName.getText())){
                        System.out.println("SingletonMessage.getSingleton().getMyFriendsList().size():"+SingletonMessage.getSingleton().getMyFriendsList().size());
                        SingletonMessage.getSingleton().getMyFriendsList().remove(i);
                        friendsList.setItems(null);
                        friendsList.setItems(SingletonMessage.getSingleton().getMyFriendsList());
                        System.out.println("SingletonMessage.getSingleton().getMyFriendsList().size():"+SingletonMessage.getSingleton().getMyFriendsList().size());
                    }
                }
                for(int i = 0 ;i < SingletonMessage.getSingleton().getNowChattingList().size() ; i++){
                    if(SingletonMessage.getSingleton().getNowChattingList().get(i).getUserName().equals(detailMessage_userName.getText())){
                        SingletonMessage.getSingleton().getNowChattingList().remove(i);
                    }
                }
            }
        });


        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                ChannelFuture channelFuture = MyClient.getInstance().getChannel().writeAndFlush(myMessage);
            }
        });


    }

    public void refleshDetailMessage(String userName,String nickName){
        String photoUrl = "Image/"+userName+".jpg";
        if(!Base64Utils.photoValidation(this.getClass().getResource("/").getPath()+photoUrl)){
            photoUrl = "/Image/jaychou.jpg";
        }
        detailMessage_Image.setImage(new Image(photoUrl));
        detailMessage_nickName.setText(nickName);
        detailMessage_userName.setText(userName);
    }

    public VBox getFriendMessageVbox() {
        return friendMessageVbox;
    }

    public ImageView getVoiceIcon() {
        return voiceIcon;
    }

    public ImageView getCaptureIcon() {
        return captureIcon;
    }

    public void openMyMessage(MouseEvent mouseEvent) {
        MyMessage.getInstance().openPrimaryStage();
    }

    public ImageView getMyImage() {
        return MyImage;
    }

    public void rightTopClick(MouseEvent mouseEvent) {
        if(friendUserName.length()>20){
            GroupDetailMessage.getInstance().showPrimaryStage();
        }
    }

    public void voiceOnclick(MouseEvent mouseEvent) {
        System.out.println(" public void voiceOnclick(MouseEvent mouseEvent)");
        if(CameraFrameMain.getInstance()==null){
            CameraFrameMain.getInstance(friendUserName,true,false).startPrimaryStage();
        }else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error Message");
            alert.setHeaderText(null);
            alert.setContentText("正在通信，请结束后再进行语音通话");
            alert.showAndWait();
        }

    }

    public void captureOnclick(MouseEvent mouseEvent) {
        if(CameraFrameMain.getInstance()==null){
            CameraFrameMain.getInstance(friendUserName,true,true).startPrimaryStage();
        }else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error Message");
            alert.setHeaderText(null);
            alert.setContentText("正在通信，请结束后再进行视频通话");
            alert.showAndWait();
        }
    }

    public void fileOnclick(MouseEvent mouseEvent) {
        FileChooser fileChooser = new FileChooser();
        Stage stage = (Stage)fileIcon.getScene().getWindow();
        File file = fileChooser.showOpenDialog(stage);
        //普通消息1，图片2，文件3
        int messageFlag = 2;
        if(file!=null){
            String path = file.getPath();
            if(!Base64Utils.photoValidation(path)){
                messageFlag = 3;
            }
            final int flag = messageFlag;
            String fileName = file.getName();
            String message = Base64Utils.ImageToBase64ByLocal(path);
            Message.MyMessage _myMessage = Message.MyMessage.newBuilder()
                    .setActionType(ChatMessageSendOrAccept)
                    .setRequest(Message.Request.newBuilder().setSendChatMessageRequest(
                            Message.Send_ChatMessage_Request.newBuilder()
                                    .setSendUserId(SingletonMessage.getSingleton().getCurrentUser().getUsername())
                                    .setAcceptUserId(friendUserName)
                                    .setMsg(fileName+","+message)
                                    .setMsgType(messageFlag+"")
                                    .setCreateTime(df.format(new Date()))
                                    .build()).build()).build();

            ChannelFuture channelFuture = MyClient.getInstance().getChannel().writeAndFlush(_myMessage);

            channelFuture.addListener(new ChannelFutureListener() {
                public void operationComplete(ChannelFuture channelFuture) throws Exception {
                    if(channelFuture.isSuccess()){
                        System.out.println("发送成功");
                        Platform.runLater(new Runnable() {
                            public void run() {

                                ChatMain.getInstance().getChatController().getAcceptListView().setItems(FXCollections.observableArrayList());
                                SingletonMessage.getSingleton().getChatMessageMap().get(friendUserName).add(
                                        new AcceptMessageOfItem(SingletonMessage.getSingleton().getCurrentUser().getUsername(),SingletonMessage.getSingleton().getCurrentUser().getNickname(), df.format(new Date()), path,flag));
                                ChatMain.getInstance().getChatController().getAcceptListView().setItems(SingletonMessage.getSingleton().getChatMessageMap().get(friendUserName));
                                System.out.println(friendUserName+"信息个数："+SingletonMessage.getSingleton().getChatMessageMap().get(friendUserName).size());
                            }
                        });
                    }else{
                        Platform.runLater(new Runnable() {
                            public void run() {
                                Alert alert = new Alert(Alert.AlertType.ERROR);
                                alert.setTitle("Error Message");
                                alert.setHeaderText(null);
                                alert.setContentText("网络不通，发送失败！");
                                alert.showAndWait();
                            }
                        });

                    }
                }});

        }
    }

    public ListView getFriendsList() {
        return friendsList;
    }
}
