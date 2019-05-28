package com.ui.chatMain;

import com.netty.MyClient;
import com.protobuf.Message;
import com.sun.glass.ui.Application;
import com.ui.UIRefleshThread;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.WindowEvent;

import java.io.IOException;

import static com.protobuf.Message.MyMessage.ActionType.LoginAction;

public class ChatMain{
    UIRefleshThread uiRefleshThread;
    ChatController chatController = null;
    private static ChatMain chatMain;
    Stage primaryStage = new Stage();
    public static ChatMain getInstance(String userName, String passWord) {
        chatMain = new ChatMain(userName ,passWord);
        return chatMain;
    }
    public static ChatMain getInstance() {
        return chatMain;
    }

    private ChatMain(String userName, String passWord) {
        Parent root = null;
        FXMLLoader fxmlLoader = new FXMLLoader(this.getClass().getResource("/fxml/ChatMain1.fxml"));
        try {
            root = fxmlLoader.load();
        } catch (IOException io) {
            io.printStackTrace();
        }
        chatController = fxmlLoader.getController();
        primaryStage = new Stage();
        primaryStage.setTitle("ChatMessage");
        primaryStage.setScene(new Scene(root, 850, 600));
        primaryStage.setResizable(false);
        primaryStage.getIcons().add(new Image("/icon/chat.png"));
        primaryStage.initStyle(StageStyle.TRANSPARENT);
        primaryStage.show();

        Message.MyMessage myMessage = Message.MyMessage.newBuilder()
                .setActionType(LoginAction)
                .setLoginMessage(
                        Message.Login_Message.newBuilder().setUsername(userName).setPassword(passWord).build()
                ).build();
        MyClient.getInstance().getChannel().writeAndFlush(myMessage);

        primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            public void handle(WindowEvent event) {
                MyClient.getInstance().close();
//                uiRefleshThread.setOver(true);
            }
        });
//        uiRefleshThread = new UIRefleshThread();
//        Thread thread = new Thread(uiRefleshThread);
//        thread.start();
    }

    public ChatController getChatController() {
        return chatController;
    }
}
