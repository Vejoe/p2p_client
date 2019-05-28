package com.ui.addFriends;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Control;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.StageStyle;


public class UserDetailFrame {
    private static UserDetailFrame userDetailFrame;
    Stage primaryStage = new Stage();

    public static synchronized UserDetailFrame getInstance(){
        return userDetailFrame;
    }

    public static synchronized UserDetailFrame getInstance(String userName,String nickName) throws Exception {
        userDetailFrame = new UserDetailFrame(userName,nickName);
        return userDetailFrame;
    }




    private UserDetailFrame(String userName,String nickName) throws Exception{
        FXMLLoader fxmlLoader = new FXMLLoader(this.getClass().getResource("/fxml/userDetailFrame.fxml"));
        Parent root = fxmlLoader.load();
        UserDetailFrameController control = fxmlLoader.getController();
        control.setUserName(userName);
        control.setNickName(nickName);
        Stage stage = new Stage();
        stage.setScene(new Scene(root,300,400));
        stage.setResizable(false);
        stage.getIcons().add(new Image("/icon/chat.png"));
        stage.initStyle(StageStyle.TRANSPARENT);
        stage.setTitle("User DetailFrame");
        stage.show();
    }

    public void destoryInstance(){
        userDetailFrame = null;
    }
}
