package com.ui.register;

import com.protobuf.Message;
import com.util.Base64Utils;
import com.util.HttpClientUtils;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.InputStreamEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import javax.imageio.ImageIO;
import javax.rmi.CORBA.Util;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.ConnectException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class RegisterController   implements Initializable{
    @FXML
    TextField PhotoPath;
    @FXML
    TextField NickName;
    @FXML
    TextField UserName;
    @FXML
    PasswordField PassWord;
    @FXML
    PasswordField ComfirmPass;
    @FXML
    Button RegisterButton;
    @FXML
    ImageView FaceImage;

    public void initialize(URL location, ResourceBundle resources) {
        PhotoPath.textProperty().addListener(new ChangeListener<String>() {
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if(!oldValue.equals(newValue)){
                    String url = "file:" + newValue;
                    if(photoValidation(newValue)){
                        System.out.println(url);
                        FaceImage.setImage(new javafx.scene.image.Image(url,200,200,false,false));
                    }
                }
            }
        });
        UserName.textProperty().addListener(new ChangeListener<String>() {
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if(isContainChinese(newValue)){
                    UserName.setText(oldValue);
                }
            }
        });
        ComfirmPass.textProperty().addListener(new ChangeListener<String>() {
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if(isContainChinese(newValue)){
                    ComfirmPass.setText(oldValue);
                }
            }
        });
        PassWord.textProperty().addListener(new ChangeListener<String>() {
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if(isContainChinese(newValue)){
                    PassWord.setText(oldValue);
                }
            }
        });
    }

    //
    @FXML
    public void registerAction(ActionEvent actionEvent) throws Exception{
        if(!valication()){
            return ;
        }
        String imageBase64 = null;
        if(photoValidation(PhotoPath.getText().trim())){
            imageBase64 = Base64Utils.ImageToBase64ByLocal(PhotoPath.getText().trim());
            System.out.println("imageBase64:"+imageBase64);
        }
        Message.Register_Message register_message = Message.Register_Message.newBuilder()
                .setUsername(UserName.getText().trim())
                .setPassword(PassWord.getText().trim())
                .setFaceImageBig(imageBase64)
                .setNickname(NickName.getText().trim())
                .build();
        System.out.println(PhotoPath.getText());
        byte[] register_message2Byte = register_message.toByteArray();
        if(HttpClientUtils.postMessage(register_message2Byte,"/user/register")==1){
            System.out.println("插入成功！");
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Success Dialog");
            alert.setHeaderText(null);
            alert.setContentText("注册成功!");
            alert.showAndWait();
            Stage stage = (Stage)RegisterButton.getScene().getWindow();
            stage.close();
        }else{
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error Message");
            alert.setHeaderText(null);
            alert.setContentText("账号已存在，请重新输入！");
            alert.showAndWait();
        }
    }

    //对每个数据进行校验。
    public boolean valication(){
        if(!(NickName.getText().trim()!=null && !NickName.getText().trim().equals(""))){
            errorAlert("NickName 不能为空，请重新输入！");
            return false;
        }else if(!(UserName.getText().trim()!=null
                && !UserName.getText().trim().equals("")
                && UserName.getText().trim().length()>=6
                && UserName.getText().trim().length()<= 16)){
            errorAlert("UserName 不能为空且大于6位小于16位，请重新输入！");
            return false;
        }else if(!(PassWord.getText().trim()!=null
                && !PassWord.getText().trim().equals("")
                && PassWord.getText().trim().length()>=6
                && PassWord.getText().trim().length()<= 16
                && PassWord.getText().trim().equals(ComfirmPass.getText().trim()))){
            errorAlert("PassWord 不能为空且大于6位小于16位，请重新输入！");
            return false;
        }
        return true;
    }

    /**
     * 判断字符串中是否包含中文
     * @param str
     * 待校验字符串
     * @return 是否为中文
     * @warn 不能校验是否为中文标点符号
     */
    public static boolean isContainChinese(String str) {
        Pattern p = Pattern.compile("[\u4e00-\u9fa5]");
        Matcher m = p.matcher(str);
        if (m.find()) {
            return true;
        }
        return false;
    }

    public void errorAlert(String message){
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error Message");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    //选择图片
    public void selectPhoto(ActionEvent actionEvent) {
        FileChooser fileChooser = new FileChooser();
        Stage stage = (Stage)RegisterButton.getScene().getWindow();
        File file = fileChooser.showOpenDialog(stage);
        if(file!=null){
            String path = file.getPath();
            PhotoPath.setText(path);
        }
    }

    public boolean photoValidation(String url){
        File  imageFile = new File(url);
        try{
            java.awt.Image image = ImageIO.read(imageFile);
            return image != null;
        }catch(IOException ioException){
            return false;
        }
    }

}
