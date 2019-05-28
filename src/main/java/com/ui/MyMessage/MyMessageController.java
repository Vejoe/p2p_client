package com.ui.MyMessage;

import com.domain.SingletonMessage;
import com.domain.UserVO;
import com.netty.MyClient;
import com.protobuf.Message;
import com.ui.chatMain.ChatMain;
import com.util.Base64Utils;
import com.util.HttpClientUtils;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

import static com.protobuf.Message.MyMessage.ActionType.ChangeMyMessageAction;
import static com.util.Base64Utils.photoValidation;

public class MyMessageController implements Initializable {
    @FXML
    ImageView MyImage;
    @FXML
    Label UserName;
    @FXML
    TextField NickName;

    String ImageUrl="";

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        MyImage.setImage(new Image("Image/"+SingletonMessage.getSingleton().getCurrentUser().getUsername()+".jpg"));
        UserName.setText(SingletonMessage.getSingleton().getCurrentUser().getUsername());
        NickName.setText(SingletonMessage.getSingleton().getCurrentUser().getNickname());
    }

    public void changMyImage(ActionEvent actionEvent) {
        FileChooser fileChooser = new FileChooser();
        Stage stage = (Stage)MyImage.getScene().getWindow();
        File file = fileChooser.showOpenDialog(stage);
        if(file!=null){
            ImageUrl = file.getPath();
            System.out.println("ImageUrl:"+ImageUrl);
        }
        if(ImageUrl == null || ImageUrl.equals("")){
            return;
        }
        MyImage.setImage(new Image("file:" + ImageUrl));

    }

    public void changPassword(ActionEvent actionEvent) {
        ChangePassword.getInstance().showPrimaryStage();
    }

    public void comfirmedMyMessage(ActionEvent actionEvent) {
        if(!NickName.getText().matches(("[a-zA-Z0-9]+")) || (NickName.getText().length()<6) ||  NickName.getText().length()>16){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error Message");
            alert.setHeaderText(null);
            alert.setContentText("昵称有非字母数字或者昵称不在6到16个字符内！");
            alert.showAndWait();
            return;
        }

        String imageBase64 = "";
        if(photoValidation(ImageUrl.trim())){
            imageBase64 = Base64Utils.ImageToBase64ByLocal(ImageUrl);
        }

        Message.MyMessage myMessage = Message.MyMessage.newBuilder()
                .setActionType(ChangeMyMessageAction)
                .setRequest(Message.Request.newBuilder().setChangeMyMessageAction(Message.ChangeMyMessage_Request.newBuilder().setMyImage(imageBase64).setNickName(NickName.getText()).setUserName(UserName.getText()).build()).build()).build();
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                   Message.MyMessage myMessage1 = HttpClientUtils.postMyMessage(myMessage.toByteArray(),"/user/updateMyMessage");
                if(myMessage1.getRequest().getChangeMyMessageAction().getUserName() != null && !myMessage1.getRequest().getChangeMyMessageAction().getUserName().equals("")){
                    SingletonMessage.getSingleton().setCurrentUser(new UserVO(myMessage1.getRequest().getChangeMyMessageAction().getUserName(),myMessage1.getRequest().getChangeMyMessageAction().getNickName()));
                    if(myMessage1.getRequest().getChangeMyMessageAction().getMyImage()!=null  &&  !myMessage1.getRequest().getChangeMyMessageAction().getMyImage().equals("")){
                        Base64Utils.Base64ToImage(myMessage1.getRequest().getChangeMyMessageAction().getMyImage(),HttpClientUtils.class.getResource("/").getPath()+"Image/"+myMessage1.getRequest().getChangeMyMessageAction().getUserName()+".jpg");
                    }
                    if(Base64Utils.photoValidation(this.getClass().getResource("/").getPath()+"/Image/"+ SingletonMessage.getSingleton().getCurrentUser().getUsername()+".jpg")){
                        ChatMain.getInstance().getChatController().getMyImage().setImage(new Image("/Image/"+ SingletonMessage.getSingleton().getCurrentUser().getUsername()+".jpg"));
                    }else{
                        ChatMain.getInstance().getChatController().getMyImage().setImage(new Image("/Image/jaychou.jpg"));
                    }
                }
                   if(myMessage1!=null){
                       Alert alert = new Alert(Alert.AlertType.INFORMATION);
                       alert.setTitle("Success Dialog");
                       alert.setHeaderText(null);
                       alert.setContentText("修改资料!");
                       alert.showAndWait();
                       Stage stage = (Stage)NickName.getScene().getWindow();
                       stage.close();
                   }
            }
        });
    }


}
