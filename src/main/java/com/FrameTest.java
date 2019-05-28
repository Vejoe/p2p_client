package com;

import com.ui.creatGroup.CreatGroupFrame;
import javafx.application.Application;
import javafx.stage.Stage;

public class FrameTest extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        CreatGroupFrame.getInstance();
    }
}
