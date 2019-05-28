package com.ui.javacv;

import com.domain.SingletonMessage;
import com.netty.MyClientUdp;
import com.protobuf.Message;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.io.*;

import static com.protobuf.Message.MyMessage.ActionType.CloseCameraOrVoiceAction;

public class CameraFrameMain {
    private CameraFrameController cameraFrameController;
    private static CameraFrameMain cameraFrameMain;
    private Stage primaryStage = new  Stage();

    private boolean hasCapture = true;
    private boolean opener = true;
    private String userName = "";


    public static CameraFrameMain getInstance(String userName,boolean opener ,boolean hasCapture){
        if(cameraFrameMain == null){
            cameraFrameMain = new CameraFrameMain(userName,opener ,hasCapture);
        }
        return cameraFrameMain;
    }

    public static CameraFrameMain getInstance(){
        return cameraFrameMain;
    }

    private CameraFrameMain(String userName, boolean opener ,boolean hasCapture){
        this.userName = userName;
        try{
            loadLib("opencv_java341");
        }catch (IOException io){
            io.printStackTrace();
        }

        FXMLLoader fxmlLoader = new FXMLLoader(this.getClass().getResource("/fxml/cameraFrame1.fxml"));
        cameraFrameController = new CameraFrameController(userName ,opener,hasCapture);
        fxmlLoader.setController(cameraFrameController);
        try{
            Parent root = fxmlLoader.load();
            primaryStage.setTitle("Camera Frame");
            primaryStage.setScene(new Scene(root, 400, 500));
        }catch (IOException io){
            io.printStackTrace();
        }
        primaryStage.setResizable(false);
        primaryStage.getIcons().add(new Image("/icon/chat.png"));

//        primaryStage.setOnCloseRequest((new EventHandler<WindowEvent>() {
//            public void handle(WindowEvent we)
//            {
//                MyCameraUtil.getSingleton().setClosed();
//            }
//        }));
        primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            public void handle(WindowEvent event) {
                Message.MyMessage myMessage = Message.MyMessage.newBuilder()
                        .setActionType(CloseCameraOrVoiceAction)
                        .setCloseCameraOrVoice(
                                Message.CloseCameraOrVoice.newBuilder().setCloseUserName(SingletonMessage.getSingleton().getCurrentUser().getUsername())
                                        .setAcceptUserName(userName)
                                        .setCloseRemark("对方关闭了音视频窗体！")
                                        .build()
                        ).build();
                MyClientUdp.getInstance().close();
                MyClientUdp.getInstance().setInstance(null);
                RecordOrPlayVoice.getSingleton().close();
                MyCameraUtil.getSingleton().setClosed();
                destoryInstance();
            }
        });
    }



    public void destoryInstance(){
        cameraFrameMain = null;
    }

    public void startPrimaryStage(){
        primaryStage.show();
    }

    public CameraFrameController getCameraFrameController() {
        return cameraFrameController;
    }




    //BIN_LIB为JAR包中存放DLL的路径
    //getResourceAsStream以JAR中根路径为开始点
    private synchronized static void loadLib(String libName) throws IOException {
        String systemType = System.getProperty("os.name");
        String libExtension = (systemType.toLowerCase().indexOf("win")!=-1) ? ".dll" : ".so";

        String libFullName = libName + libExtension;

        String nativeTempDir = System.getProperty("java.io.tmpdir");


        InputStream in = null;
        BufferedInputStream reader = null;
        FileOutputStream writer = null;

        File extractedLibFile = new File(nativeTempDir+File.separator+libFullName);
        if(!extractedLibFile.exists()){
            try {
                System.out.println("/lib/"+libFullName);
                in = CameraFrameMain.class.getResourceAsStream("/lib/"+libFullName);

                if(in==null)
                    in =  CameraFrameMain.class.getResourceAsStream(libFullName);
                CameraFrameMain.class.getResource(libFullName);
                reader = new BufferedInputStream(in);
                writer = new FileOutputStream(extractedLibFile);

                byte[] buffer = new byte[1024];

                while (reader.read(buffer) > 0){
                    writer.write(buffer);
                    buffer = new byte[1024];
                }
            } catch (IOException e){
                e.printStackTrace();
            } finally {
                if(in!=null)
                    in.close();
                if(writer!=null)
                    writer.close();
            }
        }
        System.out.println(extractedLibFile.toString());
        System.load(extractedLibFile.toString());

    }

    public Stage getPrimaryStage() {
        return primaryStage;
    }

    public boolean isHasCapture() {
        return hasCapture;
    }

    public void setHasCapture(boolean hasCapture) {
        this.hasCapture = hasCapture;
    }

    public boolean isOpener() {
        return opener;
    }

    public void setOpener(boolean opener) {
        this.opener = opener;
    }
}
