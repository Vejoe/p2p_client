package com.netty;

import java.net.InetSocketAddress;
import java.util.HashMap;

public class UserUDPIPMap {
    private static HashMap<String, InetSocketAddress> manager = new HashMap<>();

    public static void put(String senderId, InetSocketAddress ip) {
        manager.put(senderId, ip);
    }

    public static InetSocketAddress get(String senderId) {
        return manager.get(senderId);
    }
    public static int getCount() {
        return manager.size();
    }

}
