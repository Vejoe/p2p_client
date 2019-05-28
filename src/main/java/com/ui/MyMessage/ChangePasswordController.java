package com.ui.MyMessage;

import com.domain.SingletonMessage;
import com.protobuf.Message;
import com.util.HttpClientUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import static com.protobuf.Message.MyMessage.ActionType.ChangeMyMessageAction;


public class ChangePasswordController {
    @FXML
    PasswordField OldPassord;
    @FXML
    PasswordField NewPassword;
    @FXML
    PasswordField Confirm_Psw;
    public void comfirmedPassword(ActionEvent actionEvent) {
        if(!NewPassword.getText().equals(Confirm_Psw.getText())||!NewPassword.getText().matches(("[a-zA-Z0-9]+")) || (NewPassword.getText().length()<6) ||  NewPassword.getText().length()>16){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error Message");
            alert.setHeaderText(null);
            alert.setContentText("两次密码不一样或者密码有非字母数字或者密码不在6到16个字符内！");
            alert.showAndWait();
            return;
        }
        Message.MyMessage msg = Message.MyMessage.newBuilder().setActionType(ChangeMyMessageAction).setRequest(Message.Request.newBuilder().setChangeMyPassword(
                Message.ChangeMyPassword.newBuilder()
                .setUserName(SingletonMessage.getSingleton().getCurrentUser().getUsername())
                .setOldPassword(OldPassord.getText().trim())
                .setNewpassword(NewPassword.getText().trim())
                .build()
                ).build()
        ).build();
        int flag = HttpClientUtils.postMessage(msg.toByteArray(),"/user/updatePassword");
        if(flag!=0){
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Success Dialog");
            alert.setHeaderText(null);
            alert.setContentText("修改密码成功!");
            alert.showAndWait();
            Stage stage = (Stage)OldPassord.getScene().getWindow();
            stage.close();
        }else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error Message");
            alert.setHeaderText(null);
            alert.setContentText("旧密码错误！");
            alert.showAndWait();
        }
        ChangePassword.getInstance().destoryInstance();
    }
}
