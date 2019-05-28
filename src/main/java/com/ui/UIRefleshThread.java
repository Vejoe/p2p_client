package com.ui;

import com.domain.SingletonMessage;
import com.ui.chatMain.ChatMain;
import com.util.Base64Utils;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import static java.lang.Thread.sleep;

public class UIRefleshThread implements Runnable{
    boolean over = false;

    public void run() {
        while(true){

            if(over){
                break;
            }
            System.out.println("更新UIRefleshThread！");
            if(ChatMain.getInstance()!= null && ChatMain.getInstance().getChatController()!=null && ChatMain.getInstance().getChatController().getFriendUserName()!=null&&!ChatMain.getInstance().getChatController().getFriendUserName().equals("")){
                System.out.println("进来更新UIRefleshThread！");
                ChatMain.getInstance().getChatController().getAcceptListView().setItems(null);
                ChatMain.getInstance().getChatController().getAcceptListView().setItems(SingletonMessage.getSingleton().getChatMessageMap().get(ChatMain.getInstance().getChatController().getFriendUserName()));
            }

            try {
                sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public boolean isOver() {
        return over;
    }

    public void setOver(boolean over) {
        this.over = over;
    }
}
