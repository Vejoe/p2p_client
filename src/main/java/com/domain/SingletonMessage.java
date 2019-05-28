package com.domain;

import com.ui.listItem.AcceptMessageOfItem;
import com.ui.listItem.NewFriendOfRightItem;
import com.ui.listItem.OneOfListItems;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SingletonMessage {
    static SingletonMessage singletonMessage = new SingletonMessage();
    //查找朋友时候的列表
    private ObservableList<OneOfListItems> findFriendList = FXCollections.observableArrayList();
    //左侧自己好友的列表
    private ObservableList<OneOfListItems> myFriendsList = FXCollections.observableArrayList();
    //左侧当前聊天好友的列表
    private ObservableList<OneOfListItems> nowChattingList = FXCollections.observableArrayList();
    //右侧新好友请求的列表
    private ObservableList<NewFriendOfRightItem> newFriendList = FXCollections.observableArrayList();
    //右侧聊天窗体信息集合
    private Map<String ,ObservableList<AcceptMessageOfItem>> chatMessageMap = new HashMap<String, ObservableList<AcceptMessageOfItem>>();
    //通过userName寻找到该好友信息
    private Map<String ,MyFriendsVO> myFriendVOMap = new HashMap<String ,MyFriendsVO>();

    private Map<String ,GroupsVO> myGroupMap = new HashMap<String ,GroupsVO>();

    private UserVO currentUser = new UserVO();
    private NewFriendsVO newFriendsVO = new NewFriendsVO();

    private SingletonMessage(){}

    public static SingletonMessage getSingleton(){
        return singletonMessage;
    }


    public ObservableList<OneOfListItems> getFindFriendList() {
        return findFriendList;
    }


    public UserVO getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(UserVO currentUser) {
        this.currentUser = currentUser;
    }

    public NewFriendsVO getNewFriendsVO() {
        return newFriendsVO;
    }

    public void setNewFriendsVO(NewFriendsVO newFriendsVO) {
        this.newFriendsVO = newFriendsVO;
    }

    public ObservableList<NewFriendOfRightItem> getNewFriendList() {
        return newFriendList;
    }

    public void setNewFriendList(ObservableList<NewFriendOfRightItem> newFriendList) {
        this.newFriendList = newFriendList;
    }

    public ObservableList<OneOfListItems> getMyFriendsList() {
        return myFriendsList;
    }

    public Map<String, ObservableList<AcceptMessageOfItem>> getChatMessageMap() {
        return chatMessageMap;
    }


    public Map<String, MyFriendsVO> getMyFriendVOMap() {
        return myFriendVOMap;
    }

    public ObservableList<OneOfListItems> getNowChattingList() {
        return nowChattingList;
    }

    public Map<String, GroupsVO> getMyGroupMap() {
        return myGroupMap;
    }
}
