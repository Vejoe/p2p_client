package com.ui.listItem;

import com.domain.SingletonMessage;
import com.util.Base64Utils;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;

import java.io.File;
import java.io.IOException;

public class AcceptMessageOfItem {
    @FXML
    HBox hBoxItem;
    @FXML
    Text msgText;
    @FXML
    ImageView imageView;
    @FXML
    Label friendNikeName,messageTime;
    @FXML
    ImageView tempImage;
    String msg = "";
    //普通消息1，图片2，文件3
    int messageFlag = 1;


    public AcceptMessageOfItem(String userName ,String nickName,String dateTime ,String msg ,int messageFlag){
        this.messageFlag = messageFlag;
        this.msg = msg;
        String photoUrl = "/Image/"+userName+".jpg";
        String fxmlurl = "/fxml/leftOfChatFrame_Item2.fxml";

        if(userName.equals(SingletonMessage.getSingleton().getCurrentUser().getUsername())){
            fxmlurl = "/fxml/rightOfChatFrameItem2.fxml";
        }
        FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlurl));
        loader.setController(this);
        try{
            loader.load();
        }catch(IOException e) {
            throw new RuntimeException(e);
        }
        if(!Base64Utils.photoValidation(this.getClass().getResource("/").getPath()+photoUrl)){
            System.out.println(" 图片错误："+photoUrl);
            photoUrl = "/Image/jaychou.jpg";
        }
        this.friendNikeName.setText(nickName);
        this.imageView.setImage(new Image(photoUrl));
        this.messageTime.setText(dateTime);
        if(messageFlag == 1){
            this.msgText.setText(msg);
            this.tempImage.setVisible(false);
            this.tempImage.setManaged(false);
        }else if(messageFlag == 2){
            msg  = "file:"+msg;
            System.out.println(msg);
            this.msgText.setVisible(false);
            this.msgText.setManaged(false);
            this.tempImage.setFitWidth(150);
            this.tempImage.setFitHeight(150);
            this.tempImage.setImage(new Image(msg));
        }else {
            this.msgText.setVisible(false);
            this.msgText.setManaged(false);
        }

    }

    public HBox getHBoxItem(){
        return hBoxItem;
    }

    public static void open_file(String filePath) {
        File file = new File(filePath);
        if (!file.exists()) {
            return;
        }
        Runtime runtime = null;
        try {
            runtime = Runtime.getRuntime();
                // System.out.println("is linux");
            runtime.exec("nautilus " + filePath);

        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            if (null != runtime) {
                runtime.runFinalization();
            }
        }
    }
    public void textFlowdoubleClick(MouseEvent mouseEvent) throws Exception{
        if(mouseEvent.getClickCount()==2){
            if(this.messageFlag == 3){
                Runtime.getRuntime().exec("explorer /select, "+this.msg);
                System.out.println(this.msg);
            }else if(this.messageFlag == 2){
                Runtime.getRuntime().exec(new String[]{"cmd.exe", "/c", this.msg});
            }
        }
    }

}
