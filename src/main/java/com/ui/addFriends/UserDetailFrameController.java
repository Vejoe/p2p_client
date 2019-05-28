package com.ui.addFriends;

import com.util.Base64Utils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class UserDetailFrameController implements Initializable {
    private double xOffset = 0;
    private double yOffset = 0;
    @FXML
    Label  nickName,userName;
    @FXML
    ImageView imageView;

    public void setNickName(String nickName){
        this.nickName.setText(nickName);
    }
    public void setUserName(String userName){
        this.userName.setText(userName);
        if(Base64Utils.photoValidation(this.getClass().getResource("/").getPath()+"/Image/"+this.userName.getText()+".jpg")){
            this.imageView.setImage(new Image("/Image/"+this.userName.getText()+".jpg"));
        }else {
            this.imageView.setImage(new Image("/Image/jaychou.jpg"));
        }
        System.out.println(Base64Utils.photoValidation("/Image/"+this.userName.getText()+".jpg"));
    }
    //拖拽设置。
    public void frameOnPress(MouseEvent mouseEvent) {
        xOffset = mouseEvent.getSceneX();
        yOffset = mouseEvent.getSceneY();
    }
    public void frameDragged(MouseEvent mouseEvent) {
        userName.getScene().getWindow().setX(mouseEvent.getScreenX() - xOffset);
        userName.getScene().getWindow().setY(mouseEvent.getScreenY() - yOffset);
    }

    public void cannelFrame(ActionEvent actionEvent) {
        ((Stage)userName.getScene().getWindow()).close();
        UserDetailFrame.getInstance().destoryInstance();
    }

    public void addFrame(ActionEvent actionEvent) throws Exception{
        new RemarksFrame(userName.getText());
    }

    public void initialize(URL location, ResourceBundle resources) {

    }
}
