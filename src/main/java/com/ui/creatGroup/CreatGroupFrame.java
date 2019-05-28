package com.ui.creatGroup;

import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Control;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.io.IOException;

public class CreatGroupFrame {
    private static CreatGroupFrame creatGroupFrame;
    private CreatGroupController control;
    Stage primaryStage = new Stage();
    public synchronized static CreatGroupFrame getInstance(){
        if(creatGroupFrame == null){
            creatGroupFrame = new CreatGroupFrame();
        }
        System.out.println("public synchronized static CreatGroupFrame getInstance()!!");
        return creatGroupFrame;
    }

    private CreatGroupFrame(){
        Parent root =null;
        try{
            FXMLLoader fxmlLoader = new FXMLLoader(this.getClass().getClassLoader().getResource("fxml/creatGroup.fxml"));
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

    public CreatGroupController getControl() {
        return control;
    }
    public void showPrimaryStage(){
        primaryStage.show();
    }
    public void destoryInstance(){
        creatGroupFrame = null;
    }
}
