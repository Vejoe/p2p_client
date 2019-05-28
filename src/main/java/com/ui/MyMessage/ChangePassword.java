package com.ui.MyMessage;

import com.ui.creatGroup.CreatGroupController;
import com.ui.creatGroup.CreatGroupFrame;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.io.IOException;

public class ChangePassword {
    private static ChangePassword changePassword;
    private ChangePasswordController control;
    Stage primaryStage = new Stage();
    public synchronized static ChangePassword getInstance(){
        if(changePassword == null){
            changePassword = new ChangePassword();
        }
        System.out.println("public synchronized static CreatGroupFrame getInstance()!!");
        return changePassword;
    }

    private ChangePassword(){
        Parent root =null;
        try{
            FXMLLoader fxmlLoader = new FXMLLoader(this.getClass().getClassLoader().getResource("fxml/changePassword.fxml"));
            root = fxmlLoader.load();
            control =  fxmlLoader.getController();
        }catch(IOException ioException){
            ioException.printStackTrace();
        }
        primaryStage.setTitle("Change Password");
        primaryStage.setScene(new Scene(root, 300,200));
        primaryStage.setResizable(false);
        primaryStage.getIcons().add(new Image("/icon/chat.png"));

        primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            public void handle(WindowEvent event) {
                destoryInstance();
            }
        });
    }

    public void showPrimaryStage(){
        primaryStage.show();
    }
    public void destoryInstance(){
        changePassword = null;
    }
}
