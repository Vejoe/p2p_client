package com.ui.addFriends;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class Testaddfriend extends Application {

        public void start(Stage primaryStage) throws Exception {
            Parent root = FXMLLoader.load(this.getClass().getClassLoader().getResource("fxml/cameraFrame1.fxml"));
            primaryStage.setTitle("cameraFrame Stage");
            primaryStage.setScene(new Scene(root, 400,500));
            primaryStage.setResizable(false);
            primaryStage.getIcons().add(new Image("/icon/chat.png"));
            primaryStage.show();
        }

        public static void main(String[] args) {
            launch(args);
        }
}
