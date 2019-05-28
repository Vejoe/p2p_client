package com.util;

import com.domain.SingletonMessage;
import com.domain.UserVO;
import com.protobuf.Message;
import javafx.scene.control.Alert;
import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.InputStreamEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.ConnectException;
import java.util.Properties;

public class HttpClientUtils {
    public static String getUrl(){
        //获取url地址
        Properties pro = new Properties();
        InputStream stream = ClassLoader.getSystemResourceAsStream("Message.properties");
        String url = "";
        try{
            pro.load(stream);
            url =pro.getProperty("url");
            System.out.println(url);
            stream.close();
        }catch (Exception e){
            e.printStackTrace();
        }
        return url;
    }


    //  把信息传输去服务端并返回成功或者连接失败
    public static int postMessage(byte[] register_message2Byte,String rightUrl){
        int flag=0;
        // 创建默认的httpClient实例.
        CloseableHttpClient httpClient = HttpClients.createDefault();
        // 做post动作
        HttpPost httpPost = new HttpPost(HttpClientUtils.getUrl()+rightUrl);
        System.out.println("访问："+HttpClientUtils.getUrl()+rightUrl);
        ByteArrayInputStream inputStream = new ByteArrayInputStream(register_message2Byte);
        InputStreamEntity inputStreamEntity = new InputStreamEntity(inputStream);
        httpPost.addHeader("Content-Type","application/x-protobuf");
        httpPost.addHeader("Accept", "application/json");
        httpPost.setEntity(inputStreamEntity);
        CloseableHttpResponse response=null;
        try{

            response = httpClient.execute(httpPost);
            HttpEntity httpEntity = response.getEntity();
            ByteArrayOutputStream byteArrayInputStream = new ByteArrayOutputStream();
            httpEntity.writeTo(byteArrayInputStream);
            System.out.println(new String(byteArrayInputStream.toByteArray()));
            flag=Integer.parseInt(new String(byteArrayInputStream.toByteArray()));

        }catch (ConnectException connect){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error Connect");
            alert.setHeaderText(null);
            alert.setContentText("连接服务器失败！");
            alert.showAndWait();
        }catch (ClientProtocolException clientProtocolException){
            clientProtocolException.printStackTrace();
        }catch (IOException io){
            io.printStackTrace();
        }
        return flag;
    }


    //  把登录信息传输去服务端并返回成功或者连接失败
    public static int postLogin(byte[] register_message2Byte,String rightUrl)throws Exception{
        int flag=0;
        // 创建默认的httpClient实例.
        CloseableHttpClient httpClient = HttpClients.createDefault();
        // 做post动作
        HttpPost httpPost = new HttpPost(HttpClientUtils.getUrl()+rightUrl);
        System.out.println("访问："+HttpClientUtils.getUrl()+rightUrl);
        ByteArrayInputStream inputStream = new ByteArrayInputStream(register_message2Byte);
        InputStreamEntity inputStreamEntity = new InputStreamEntity(inputStream);
        httpPost.addHeader("Content-Type","application/x-protobuf");
        httpPost.addHeader("Accept", "application/x-protobuf");
        httpPost.setEntity(inputStreamEntity);
        CloseableHttpResponse response=null;
        try{

            response = httpClient.execute(httpPost);
            HttpEntity entity = response.getEntity();
            ByteArrayOutputStream buf = new ByteArrayOutputStream();
            entity.writeTo(buf);
            System.out.println(new String(buf.toByteArray())+"#################");
            Message.Login_User_Message login_user_message = Message.Login_User_Message.parseFrom(buf.toByteArray());
            if(login_user_message.getUserName() != null && !login_user_message.getUserName().equals("")){
                flag = 1;
                SingletonMessage.getSingleton().setCurrentUser(new UserVO(login_user_message.getUserName(),login_user_message.getNickname()));
                if(login_user_message.getFaceImage()!=null  &&  !login_user_message.getFaceImage().equals("")){
                    Base64Utils.Base64ToImage(login_user_message.getFaceImage(),HttpClientUtils.class.getResource("/").getPath()+"Image/"+login_user_message.getUserName()+".jpg");
                }
            }
        }catch (ConnectException connect){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error Connect");
            alert.setHeaderText(null);
            alert.setContentText("连接服务器失败！");
            alert.showAndWait();
        }
        return flag;
    }

    public static Message.MyMessage postMyMessage(byte[] register_message2Byte, String rightUrl){
        // 创建默认的httpClient实例.
        CloseableHttpClient httpClient = HttpClients.createDefault();
        // 做post动作
        HttpPost httpPost = new HttpPost(HttpClientUtils.getUrl()+rightUrl);
        System.out.println("访问："+HttpClientUtils.getUrl()+rightUrl);
        ByteArrayInputStream inputStream = new ByteArrayInputStream(register_message2Byte);
        InputStreamEntity inputStreamEntity = new InputStreamEntity(inputStream);
        httpPost.addHeader("Content-Type","application/x-protobuf");
        httpPost.addHeader("Accept", "application/x-protobuf");
        httpPost.setEntity(inputStreamEntity);
        CloseableHttpResponse response=null;
        try{

            response = httpClient.execute(httpPost);
            HttpEntity entity = response.getEntity();
            ByteArrayOutputStream buf = new ByteArrayOutputStream();
            entity.writeTo(buf);
            System.out.println(new String(buf.toByteArray())+"#################");
            Message.MyMessage myMessage = Message.MyMessage.parseFrom(buf.toByteArray());
            return myMessage;
        }catch (ConnectException connect){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error Connect");
            alert.setHeaderText(null);
            alert.setContentText("连接服务器失败！");
            alert.showAndWait();
        }catch (ClientProtocolException clientProtocolException){
            clientProtocolException.printStackTrace();
        }catch (IOException io){
            io.printStackTrace();
        }
        return null;
    }

}
