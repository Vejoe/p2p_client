package com.ui.creatGroup;

import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.io.IOException;

public class GroupDetailMessage {

    private static GroupDetailMessage groupDetailMessage;
    private GroupDetailController control;
    Stage primaryStage = new Stage();


    public synchronized static GroupDetailMessage getInstance(){
        if(groupDetailMessage == null){
            groupDetailMessage = new GroupDetailMessage();
        }
        System.out.println("public synchronized static GroupDetailMessage getInstance()!!");
        return groupDetailMessage;
    }

    private GroupDetailMessage(){
        Parent root =null;
        try{
            FXMLLoader fxmlLoader = new FXMLLoader(this.getClass().getClassLoader().getResource("fxml/groupMessage.fxml"));
            root = fxmlLoader.load();
            control =  fxmlLoader.getController();
        }catch(IOException ioException){
            ioException.printStackTrace();
        }
        primaryStage.setTitle("Creat Group");
        primaryStage.setScene(new Scene(root, 300,450));
        primaryStage.setResizable(false);
        primaryStage.getIcons().add(new Image("/icon/chat.png"));
        primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            public void handle(WindowEvent event) {
                destoryInstance();
            }
        });

    }

    public GroupDetailController getControl() {
        return control;
    }
    public void showPrimaryStage(){
        primaryStage.show();
    }

    public void destoryInstance(){
        groupDetailMessage = null;
    }

}
