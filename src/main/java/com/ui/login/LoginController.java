package com.ui.login;

import com.netty.MyClient;
import com.protobuf.Message;
import com.ui.chatMain.ChatMain;
import com.ui.register.RegisterMain;
import com.util.Base64Utils;
import com.util.HttpClientUtils;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class LoginController implements Initializable {
    @FXML
    TextField userName;
    @FXML
    ImageView user_img;
    @FXML
    PasswordField passWord;

    public LoginController(){
        System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!");
    }

    @FXML
    public void loginNow(ActionEvent actionEvent) throws Exception{
        Message.Login_Message login_message = Message.Login_Message.newBuilder()
                .setUsername(userName.getText().trim())
                .setPassword(passWord.getText().trim())
                .build();
        if(HttpClientUtils.postLogin(login_message.toByteArray(),"/user/login")!=0){
            MyClient.getInstance().start();
            ChatMain.getInstance(userName.getText(),passWord.getText());
            ((Stage)(userName.getScene().getWindow())).close();
        }else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error Message");
            alert.setHeaderText(null);
            alert.setContentText("密码错误！");
            alert.showAndWait();
        }
    }
    public void registerNow(ActionEvent actionEvent) throws Exception{
        new RegisterMain();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        userName.textProperty().addListener(new ChangeListener<String>() {
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if(Base64Utils.photoValidation(this.getClass().getResource("/").getPath()+"/Image/"+newValue+".jpg")){
                    user_img.setImage(new Image("/Image/"+newValue+".jpg"));
                }else {
                    user_img.setImage(new Image("/Image/jaychou.jpg"));
                }
            }
        });
    }
}
