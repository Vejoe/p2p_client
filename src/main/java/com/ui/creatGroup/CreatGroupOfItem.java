package com.ui.creatGroup;

import com.util.Base64Utils;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;

import java.io.IOException;

public class CreatGroupOfItem {
    @FXML
    HBox hBoxItem;
    @FXML
    ImageView friendImage;
    @FXML
    ImageView selectIcon;
    @FXML
    Label nickNameLabel;
    boolean select = false;
    String userName ;
    int flag = 0;//1代表修改

    public CreatGroupOfItem(String userName , String nickName){

        String photoUrl = "/Image/"+userName+".jpg";
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/creatGroup_Item.fxml"));
        loader.setController(this);
        try{
            loader.load();
        }catch(IOException e) {
            throw new RuntimeException(e);
        }
        this.nickNameLabel.setText(nickName);
        if(!Base64Utils.photoValidation(this.getClass().getResource("/").getPath()+photoUrl)){
            photoUrl = "/Image/jaychou.jpg";
        }
        this.friendImage.setImage(new Image(photoUrl));
        this.userName = userName;
    }

    public HBox gethBoxItem() {
        return hBoxItem;
    }
    public void hBoxOnclick(MouseEvent mouseEvent) throws Exception{
        if(flag == 0){
            if(select){
                select = false;
                selectIcon.setImage(new Image("/icon/select.png"));
                CreatGroupFrame.getInstance().getControl().getUserNameList().remove(this.userName);
                System.out.println(CreatGroupFrame.getInstance().getControl().getUserNameList().size());
            }else {
                select = true;
                selectIcon.setImage(new Image("/icon/select_onpress.png"));
                CreatGroupFrame.getInstance().getControl().getUserNameList().add(this.userName);
                System.out.println(CreatGroupFrame.getInstance().getControl().getUserNameList().size());
            }
        }
    }

    public ImageView getSelectIcon() {
        return selectIcon;
    }

    public void setFlag(int flag) {
        this.flag = flag;
    }
}
