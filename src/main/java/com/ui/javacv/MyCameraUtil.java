package com.ui.javacv;

import com.domain.SingletonMessage;
import com.netty.MyClientUdp;
import com.netty.UserUDPIPMap;
import com.util.CameraUtils;
import io.netty.buffer.Unpooled;
import io.netty.channel.socket.DatagramPacket;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;
import org.opencv.videoio.VideoCapture;
import sun.misc.BASE64Encoder;

import java.net.InetSocketAddress;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * 供视频聊天摄像头使用
 */
public class MyCameraUtil {

    // a timer for acquiring the video stream
    private ScheduledExecutorService timer;
    // the OpenCV object that realizes the video capture
    private VideoCapture capture = new VideoCapture();
    // a flag to change the button behavior
    private boolean cameraActive = false;
    // the id of the camera to be used
    private static int cameraId = 0;

    private MyCameraUtil(){}
    private static MyCameraUtil myCameraUtil;
    public static synchronized  MyCameraUtil getSingleton(){
        if(myCameraUtil == null){
            myCameraUtil = new MyCameraUtil();
        }
        return myCameraUtil;
    }

    public void startCamera() {
        if (!this.cameraActive){
            // start the video capture
            this.capture.open(cameraId);

            // is the video stream available?
            if (this.capture.isOpened()){
                this.cameraActive = true;
                BASE64Encoder base64Encoder = new BASE64Encoder();
                // grab a frame every 33 ms (30 frames/sec)
                Runnable frameGrabber = new Runnable() {
                    @Override
                    public void run(){
                        // effectively grab and process a single frame
                        Mat frame = grabFrame();
                        // convert and show the frame
                        Image imageToShow = CameraUtils.mat2Image(frame);
                        updateImageView(CameraFrameMain.getInstance().getCameraFrameController().getMyImageView(), imageToShow);

                        if(UserUDPIPMap.get(CameraFrameMain.getInstance().getCameraFrameController().getFrienduserName())!= null){
                            MyClientUdp.getInstance().getChannel().writeAndFlush(new DatagramPacket(
                                    Unpooled.copiedBuffer(("Camera_Message "+ SingletonMessage.getSingleton().getCurrentUser().getUsername()+" "+CameraFrameMain.getInstance().getCameraFrameController().frienduserName+" " +base64Encoder.encode(CameraUtils.imageToByte(imageToShow))).getBytes()),
                                    UserUDPIPMap.get(CameraFrameMain.getInstance().getCameraFrameController().getFrienduserName())));
                        }

                        if(CameraFrameMain.getInstance().getCameraFrameController().getFriendByte() != null){
                            System.out.println("更新好友屏幕视频");
                            CameraFrameMain.getInstance().getCameraFrameController().getFriendImageView().setImage(CameraUtils.byteToImage(CameraFrameMain.getInstance().getCameraFrameController().getFriendByte()));
                        }
                    }
                };

                this.timer = Executors.newSingleThreadScheduledExecutor();
                this.timer.scheduleAtFixedRate(frameGrabber, 0, 33, TimeUnit.MILLISECONDS);


            }else
            {
                // log the error
                System.err.println("Impossible to open the camera connection...");
            }
        }else{
            // the camera is not active at this point
            this.cameraActive = false;
            // stop the timer
            this.stopAcquisition();
        }
    }


    private Mat grabFrame()
    {
        // init everything
        Mat frame = new Mat();

        // check if the capture is open
        if (this.capture.isOpened())
        {
            try
            {
                // read the current frame
                this.capture.read(frame);

//                // if the frame is not empty, process it
//                if (!frame.empty())
//                {
//                    Imgproc.cvtColor(frame, frame, Imgproc.COLOR_BGR2GRAY);
//                }

            }
            catch (Exception e)
            {
                // log the error
                System.err.println("Exception during the image elaboration: " + e);
            }
        }

        return frame;
    }

    /**
     * Stop the acquisition from the camera and release all the resources
     */
    private void stopAcquisition()
    {
        if (this.timer!=null && !this.timer.isShutdown())
        {
            try
            {
                // stop the timer
                this.timer.shutdown();
                this.timer.awaitTermination(33, TimeUnit.MILLISECONDS);
                cameraActive = false;
            }
            catch (InterruptedException e)
            {
                // log any exception
                System.err.println("Exception in stopping the frame capture, trying to release the camera now... " + e);
            }
        }

        if (this.capture.isOpened())
        {
            // release the camera
            this.capture.release();
        }
        myCameraUtil = null;
    }

    /**
     * Update the {@link ImageView} in the JavaFX main thread
     *
     * @param view
     *            the {@link ImageView} to update
     * @param image
     *            the {@link Image} to show
     */
    private void updateImageView(ImageView view, Image image){
        CameraUtils.onFXThread(view.imageProperty(), image);
    }

    /**
     * On application close, stop the acquisition from the camera
     */
    public void setClosed() {
        this.stopAcquisition();
    }
}
