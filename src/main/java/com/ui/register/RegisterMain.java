package com.ui.register;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class RegisterMain {

    public RegisterMain() throws Exception{
        System.out.println(this.getClass().getClassLoader().getResource("/fxml/register.fxml"));
        System.out.println(this.getClass().getResource("/fxml/register.fxml"));
        Parent root = FXMLLoader.load(this.getClass().getClassLoader().getResource("fxml/register.fxml"));
        Stage primaryStage = new Stage();
        primaryStage.setResizable(false);
        primaryStage.setTitle("Register Stage");
        primaryStage.setScene(new Scene(root,320,460));
        primaryStage.getIcons().add(new Image("/icon/chat.png"));
        primaryStage.show();
    }
}
