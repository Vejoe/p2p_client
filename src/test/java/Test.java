
import com.domain.SingletonMessage;
import com.netty.MyClient;
import com.netty.MyClientUdp;
import com.protobuf.Message;
import com.util.HttpClientUtils;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.socket.DatagramPacket;
import org.bytedeco.javacv.*;

import javax.swing.*;
import java.io.InputStream;
import java.net.InetSocketAddress;
import java.util.*;


public class Test {
    @org.junit.Test
    public void test()throws Exception{
        Properties pro = new Properties();
        InputStream stream = ClassLoader.getSystemResourceAsStream("Message.properties");
        pro.load(stream);
        String url =pro.getProperty("url");
        System.out.println(url);
        stream.close();
    }
    @org.junit.Test
    public void tetet()throws Exception{
        Message.Login_Message login_message = Message.Login_Message.newBuilder()
        .setPassword("88816811")
        .setUsername("weizhihao1")
        .build();
        System.out.println(HttpClientUtils.postLogin(login_message.toByteArray() ,"/user/login"));
    }

    @org.junit.Test
    public  void sssss(){
        List<String> arrayString = new ArrayList<String>();
        arrayString.add("sssss");
        Map<String ,List<String>> mmm = new HashMap<String ,List<String>>();
        mmm.put("test",arrayString);
        arrayString.add("sssss1111");
        System.out.println(mmm.get("test").size());
        System.out.println(mmm.get("123123123"));
    }

    @org.junit.Test
    public void testCamera222222() throws FrameGrabber.Exception, InterruptedException {
        List<String> testString = new LinkedList<String>();
        String uu = new String("1111");
        testString.add(uu);
        uu = null;
        uu = new String("2222");
        testString.add(uu);
        System.out.println(testString);
    }
    @org.junit.Test
    public void ttt(){

    }

}
