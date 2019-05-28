package com.util;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.*;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import org.opencv.core.Mat;

import javafx.application.Platform;
import javafx.beans.property.ObjectProperty;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;

import javax.imageio.ImageIO;

public class CameraUtils {
        /**
         * Convert a Mat object (OpenCV) in the corresponding Image for JavaFX
         *
         * @param frame
         *            the {@link Mat} representing the current frame
         * @return the {@link Image} to show
         */
        public static Image mat2Image (Mat frame){
            int width = frame.width(), height = frame.height(), channels = frame.channels();
            try {
                return SwingFXUtils.toFXImage(matToBufferedImage(frame), null);
            } catch (Exception e) {
                System.err.println("Cannot convert the Mat object: " + e);
                return null;
            }
         }

        /**
         * Generic method for putting element running on a non-JavaFX thread on the
         * JavaFX thread, to properly update the UI
         *
         * @param property
         *            a {@link ObjectProperty}
         * @param value
         *            the value to set for the given {@link ObjectProperty}
         */
        public static <T > void onFXThread ( final ObjectProperty<T> property, final T value)
        {
            Platform.runLater(() -> {
                property.set(value);
            });
        }

        /**
         *
         * @param original
         *            the {@link Mat} object in BGR or grayscale
         * @return the corresponding {@link BufferedImage}
         */
        private static BufferedImage matToBufferedImage (Mat original)
        {
            // init
            BufferedImage image = null;
            int width = original.width(), height = original.height(), channels = original.channels();
            byte[] sourcePixels = new byte[width * height * channels];
            original.get(0, 0, sourcePixels);

            if (original.channels() > 1) {
                image = new BufferedImage(width, height, BufferedImage.TYPE_3BYTE_BGR);
            } else {
                image = new BufferedImage(width, height, BufferedImage.TYPE_BYTE_GRAY);
            }
            final byte[] targetPixels = ((DataBufferByte) image.getRaster().getDataBuffer()).getData();
            System.arraycopy(sourcePixels, 0, targetPixels, 0, sourcePixels.length);
            return image;
        }

    /**
     * 将成像后的Image图片对象转成Byte[]，方便转成base64字符串。
     * @param image
     * @return byte[]
     */
    public static byte[] imageToByte(Image image){
        ByteArrayOutputStream byteOutput = new ByteArrayOutputStream();
        try{
            ImageIO.write(SwingFXUtils.fromFXImage(image, null), "jpeg", byteOutput);
        }catch (IOException io){
            io.printStackTrace();
        }
        byte[] b = byteOutput.toByteArray();

        try{
            byteOutput.close();
        }catch (IOException IO ){
            IO.printStackTrace();
        }

        return b;
    }

    /**
     * 将接受到Base64字符串转回来的byte[]字节返回一个图像。
     * @param bytes
     * @return Image
     */
    public static Image byteToImage(byte[] bytes){
        ByteArrayInputStream in = new ByteArrayInputStream(bytes); //将b作为输入流；
        Image img = new Image(in);
        Canvas canvas = new Canvas(700, 514);
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.drawImage(img,0,0);
        return img;
    }


}
