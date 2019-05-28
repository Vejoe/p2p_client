package com.ui.addFriends;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * 添加朋友界面
 */
public class AddFriendMain {
    private static AddFriendMain addFriendMain;
    Stage primaryStage = new Stage();

    public static synchronized AddFriendMain getInstance() {
        if(addFriendMain == null) {
            addFriendMain = new AddFriendMain();
        }
        return addFriendMain;
    }


    private AddFriendMain(){
        Parent root =null;
        try{
            FXMLLoader fxmlLoader = new FXMLLoader(this.getClass().getClassLoader().getResource("fxml/addFriend.fxml"));
            root = fxmlLoader.load();
        }catch(IOException ioException){
            ioException.printStackTrace();
        }
        primaryStage.setTitle("AddFriends Stage");
        primaryStage.setScene(new Scene(root, 300,400));
        primaryStage.setResizable(false);
        primaryStage.getIcons().add(new Image("/icon/chat.png"));
    }

    public void addFriendshow(){
        primaryStage.show();
    }
}
