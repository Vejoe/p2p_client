package com.ui.login;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class LoginMain1 extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        System.out.println(this.getClass().getResource("/fxml/login.fxml"));
        Parent root = FXMLLoader.load(this.getClass().getResource("/fxml/login.fxml"));
        primaryStage.setTitle("Hello Chat");
        primaryStage.setScene(new Scene(root, 430, 330));
        primaryStage.setResizable(false);
        primaryStage.getIcons().add(new Image("/icon/chat.png"));
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
