package com.ui.MyMessage;

import com.netty.MyClient;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.io.IOException;

public class MyMessage {
    private static MyMessage myMessage;
    private MyMessageController control;
    Stage primaryStage = new Stage();
    public synchronized static MyMessage getInstance(){
        if(myMessage == null){
            myMessage = new MyMessage();
        }
        System.out.println(" public synchronized static MyMessage getInstance()!");
        return myMessage;
    }

    private MyMessage(){
        Parent root =null;
        try{
            FXMLLoader fxmlLoader = new FXMLLoader(this.getClass().getClassLoader().getResource("fxml/changeMyMessage.fxml"));
            root = fxmlLoader.load();
            control =  fxmlLoader.getController();
        }catch(IOException ioException){
            ioException.printStackTrace();
        }
        primaryStage.setTitle("Change MyMessage");
        primaryStage.setScene(new Scene(root, 300,400));
        primaryStage.setResizable(false);
        primaryStage.getIcons().add(new Image("/icon/chat.png"));
        primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            public void handle(WindowEvent event) {
                destoryInstance();
            }
        });
    }

    public void openPrimaryStage(){
        primaryStage.show();
    }

    public void destoryInstance(){
        myMessage = null;
    }
}
