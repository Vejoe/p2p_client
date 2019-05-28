package com.ui.addFriends;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Control;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

public class RemarksFrame {

    public RemarksFrame(String userName) throws Exception{
        FXMLLoader fxmlLoader = new FXMLLoader(this.getClass().getResource("/fxml/RemarksFrame.fxml"));
        Parent root = fxmlLoader.load();
        RemarkController remarkController = fxmlLoader.getController();
        remarkController.setUserName(userName);
        Stage stage = new Stage();
        stage.setScene(new Scene(root,400,100));
        stage.setResizable(false);
        stage.getIcons().add(new Image("/icon/chat.png"));
        stage.setTitle("User DetailFrame");
        stage.show();
    }

}
