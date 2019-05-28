package com.ui.listItem;

import com.domain.SingletonMessage;
import com.domain.UserVO;
import com.ui.addFriends.AddFriendMain;
import com.ui.addFriends.UserDetailFrame;
import com.ui.chatMain.ChatController;
import com.ui.chatMain.ChatMain;
import com.util.Base64Utils;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;

import java.io.IOException;

public class OneOfListItems {
    @FXML
    ImageView imageItem;
    @FXML
    Label labelItem;
    @FXML
    HBox hBoxItem;
    //0代表在聊天页面，1代表在好友信息,2代表新好友添加信息页面，3查新好友。
    int WindowsFlag;
   private String  userName;
   private  String nickName;


    public OneOfListItems(String userName,String nickName,int WindowsFlag){
        this.WindowsFlag = WindowsFlag;
        String photoUrl = "/Image/"+userName+".jpg";
        this.userName = userName;
        this.nickName = nickName;
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/oneOfListItems.fxml"));
        loader.setController(this);
        try{
            loader.load();
        }catch(IOException e) {
            throw new RuntimeException(e);
        }
        System.out.println("check:"+Base64Utils.photoValidation(this.getClass().getResource("/").getPath()+photoUrl));
        if(!Base64Utils.photoValidation(this.getClass().getResource("/").getPath()+photoUrl)){
            photoUrl = "/Image/jaychou.jpg";
        }
        if(WindowsFlag == 2){
            photoUrl = "/icon/newFriend.png";
        }
        //群组的时候改图片
        if(userName.length()>20){
            this.nickName = nickName+"("+SingletonMessage.getSingleton().getMyGroupMap().get(userName).getGroup_User().size()+")";
            photoUrl = "/icon/group.png";
        }
        System.out.println("checkAFfter:"+this.getClass().getResource("/").getPath()+photoUrl);
        this.imageItem.setImage(new Image(photoUrl));
        this.labelItem.setText(this.nickName);
    }



    public HBox gethBoxItem(){
        return hBoxItem;
    }

    public void itemOnClick(MouseEvent mouseEvent) throws Exception{
        if(WindowsFlag == 2){
                ChatMain.getInstance().getChatController().getNewFriendsListWindow().setVisible(true);
                ChatMain.getInstance().getChatController().getSendMessageWindow().setVisible(false);
                ChatMain.getInstance().getChatController().getFriendMessageShow().setVisible(false);
                ChatMain.getInstance().getChatController().getAcceptMessageWindow().setVisible(false);
                ChatMain.getInstance().getChatController().getRight_top().setText(nickName);
        }else if(WindowsFlag == 1){
                ChatMain.getInstance().getChatController().getNewFriendsListWindow().setVisible(false);
                ChatMain.getInstance().getChatController().getSendMessageWindow().setVisible(false);
                ChatMain.getInstance().getChatController().getFriendMessageShow().setVisible(true);
                 ChatMain.getInstance().getChatController().getFriendMessageVbox().setVisible(true);
                ChatMain.getInstance().getChatController().getAcceptMessageWindow().setVisible(false);
                ChatMain.getInstance().getChatController().getRight_top().setText(nickName);
                ChatMain.getInstance().getChatController().refleshDetailMessage(userName,nickName);

        }else if(WindowsFlag == 0){
                ChatMain.getInstance().getChatController().getAcceptListView().setItems(FXCollections.observableArrayList());
                ChatMain.getInstance().getChatController().getNewFriendsListWindow().setVisible(false);
                ChatMain.getInstance().getChatController().getSendMessageWindow().setVisible(true);
                ChatMain.getInstance().getChatController().getFriendMessageShow().setVisible(false);
                ChatMain.getInstance().getChatController().getAcceptMessageWindow().setVisible(true);
                //群组关闭视频和语音入口
                if(userName.length()>20){
                    ChatMain.getInstance().getChatController().getCaptureIcon().setVisible(false);
                    ChatMain.getInstance().getChatController().getVoiceIcon().setVisible(false);
                }else {
                    ChatMain.getInstance().getChatController().getCaptureIcon().setVisible(true);
                    ChatMain.getInstance().getChatController().getVoiceIcon().setVisible(true);
                }
                ChatMain.getInstance().getChatController().setFriendUserName(userName);
                ChatMain.getInstance().getChatController().getRight_top().setText(nickName);

                if(SingletonMessage.getSingleton().getChatMessageMap().get(userName)==null){
                    ObservableList<AcceptMessageOfItem> currentChatting = FXCollections.observableArrayList();
                    SingletonMessage.getSingleton().getChatMessageMap().put(userName, currentChatting );
                }
                ChatMain.getInstance().getChatController().getAcceptListView().setItems(SingletonMessage.getSingleton().getChatMessageMap().get(userName));
                System.out.println(userName+"信息个数："+SingletonMessage.getSingleton().getChatMessageMap().get(userName).size());
                System.out.println("userName:"+userName);
        }else if(WindowsFlag == 3){
            if(mouseEvent.getClickCount() == 2){
                System.out.println("用户名;"+labelItem.getText());
                UserDetailFrame.getInstance(userName,labelItem.getText());
                }
        }
    }

    public String getUserName() {
        return userName;
    }

    public String getNickName() {
        return nickName;
    }
}
