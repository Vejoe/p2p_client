package com.Service.ServiceImpl;

import com.Service.UserService;
import com.domain.GroupsVO;
import com.domain.MyFriendsVO;
import com.domain.SingletonMessage;
import com.domain.UserVO;
import com.netty.MyClient;
import com.netty.MyClientUdp;
import com.protobuf.Message;
import com.sun.security.ntlm.Client;
import com.ui.chatMain.ChatMain;
import com.ui.javacv.CameraFrameMain;
import com.ui.javacv.MyCameraUtil;
import com.ui.javacv.RecordOrPlayVoice;
import com.ui.listItem.AcceptMessageOfItem;
import com.ui.listItem.NewFriendOfRightItem;
import com.ui.listItem.OneOfListItems;
import com.util.Base64Utils;
import com.util.CameraUtils;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static com.protobuf.Message.CameraOrVoiceRequest.CameraOrVoiceType.Camera;
import static com.protobuf.Message.CameraOrVoiceRequest.CameraOrVoiceType.Voice;
import static com.protobuf.Message.MyMessage.ActionType.CameraOrVoiceResponseAction;
import static com.protobuf.Message.MyMessage.ActionType.CloseCameraOrVoiceAction;

public class UserServiceImpl implements UserService {

    /**
     * Author:weizhihao
     * 处理刚上线时候发来的未处理新好友的信息
     * @param myMessage
     */
    public void handleMakeFriendsAction(Message.MyMessage myMessage) {
        final Message.MyMessage _myMessage = myMessage;
        Platform.runLater(
                new Runnable() {
                    public void run() {
                        SingletonMessage.getSingleton().getNewFriendList().clear();
                        System.out.println("list个数:"+_myMessage.getResponse().getMakeFriendResponse().getSendFriendRequestUserMessageCount());
                        for (int i = 0; i < _myMessage.getResponse().getMakeFriendResponse().getSendFriendRequestUserMessageCount(); i++) {
                            Base64Utils.Base64ToImage(_myMessage.getResponse().getMakeFriendResponse().getSendFriendRequestUserMessage(i).getFaceImage()
                                    , this.getClass().getResource("/").getPath() + "Image/" + _myMessage.getResponse().getMakeFriendResponse().getSendFriendRequestUserMessage(i).getSendUserName() + ".jpg");
                            System.out.println("时间：" + _myMessage.getResponse().getMakeFriendResponse().getSendFriendRequestUserMessage(i).getRequestDateTime());
                            SingletonMessage.getSingleton().getNewFriendList().add(new NewFriendOfRightItem(
                                    _myMessage.getResponse().getMakeFriendResponse().getSendFriendRequestUserMessage(i).getSendUserName(),
                                    _myMessage.getResponse().getMakeFriendResponse().getSendFriendRequestUserMessage(i).getNickname(),
                                    _myMessage.getResponse().getMakeFriendResponse().getSendFriendRequestUserMessage(i).getRemarks(),
                                    _myMessage.getResponse().getMakeFriendResponse().getSendFriendRequestUserMessage(i).getRequestDateTime()
                            ));
                        }
                    }
                });
    }



    /**
     * 更新发来的好友列表信息
     * @param myMessage
     */
    public void handleGetMyFriendsListAction(Message.MyMessage myMessage){
        final Message.MyMessage _myMessage = myMessage;
        System.out.println("handleGetMyFriendsListAction:好友数量："+_myMessage.getResponse().getGetMyFriendsMessageResponse().getMyFriendsMessageCount());
        Platform.runLater(
                new Runnable() {
                    public void run() {
                        SingletonMessage.getSingleton().getMyFriendsList().clear();
                        SingletonMessage.getSingleton().getMyFriendsList().add(new OneOfListItems("100", "新的朋友",2));

                        System.out.println("list个数:"+_myMessage.getResponse().getGetMyFriendsMessageResponse().getMyFriendsMessageCount());

                        for (int i = 0; i < _myMessage.getResponse().getGetMyFriendsMessageResponse().getMyFriendsMessageCount(); i++) {
                            SingletonMessage.getSingleton().getMyFriendVOMap().put(_myMessage.getResponse().getGetMyFriendsMessageResponse().getMyFriendsMessage(i).getUsername(),new MyFriendsVO(_myMessage.getResponse().getGetMyFriendsMessageResponse().getMyFriendsMessage(i).getUsername(),_myMessage.getResponse().getGetMyFriendsMessageResponse().getMyFriendsMessage(i).getNickname()));

                            Base64Utils.Base64ToImage(_myMessage.getResponse().getGetMyFriendsMessageResponse().getMyFriendsMessage(i).getFaceImage(),
                                    this.getClass().getResource("/").getPath() + "Image/" + _myMessage.getResponse().getGetMyFriendsMessageResponse().getMyFriendsMessage(i).getUsername() + ".jpg");


                            SingletonMessage.getSingleton().getMyFriendsList().add(
                                    new OneOfListItems(_myMessage.getResponse().getGetMyFriendsMessageResponse().getMyFriendsMessage(i).getUsername(),
                                            _myMessage.getResponse().getGetMyFriendsMessageResponse().getMyFriendsMessage(i).getNickname(), 1));
                        }

                    }
                }
        );
    }

    /**
     * 处理接受到的新聊天信息处理
     * @param myMessage
     */
    public void handleChatMessageSendOrAcceptAction(Message.MyMessage myMessage) {
        final Message.MyMessage _myMessage = myMessage;
        System.out.println("处理接受到的新聊天信息处理:"+myMessage);
        Platform.runLater(new Runnable() {
            public void run() {
                for(int i=0;i<_myMessage.getResponse().getAcceptChatMessageResponse().getAcceptChatMessageBodyCount();i++){
                    String friendUserName = "";
                    String friendNickName = "";
                    friendUserName = _myMessage.getResponse().getAcceptChatMessageResponse().getAcceptChatMessageBody(i).getSendUserId();
                    friendNickName = _myMessage.getResponse().getAcceptChatMessageResponse().getAcceptChatMessageBody(i).getSendUserNickname();

                    if(_myMessage.getResponse().getAcceptChatMessageResponse().getAcceptChatMessageBody(i).getSendUserImage()!=null&&
                            !_myMessage.getResponse().getAcceptChatMessageResponse().getAcceptChatMessageBody(i).getSendUserImage().equals("")){
                        Base64Utils.Base64ToImage(_myMessage.getResponse().getAcceptChatMessageResponse().getAcceptChatMessageBody(i).getSendUserImage(),
                                this.getClass().getResource("/").getPath() + "Image/" + _myMessage.getResponse().getAcceptChatMessageResponse().getAcceptChatMessageBody(i).getSendUserId() + ".jpg");
                    }

                    String tempFlag = _myMessage.getResponse().getAcceptChatMessageResponse().getAcceptChatMessageBody(i).getMsgType();
                    String path = "C:/ChatTemp/";
                    String messageTemp=_myMessage.getResponse().getAcceptChatMessageResponse().getAcceptChatMessageBody(i).getMsg();
                    if(Integer.parseInt(tempFlag) == 2 ||
                            Integer.parseInt(tempFlag) == 3){
                        File uploadFilePath = new File(path);
                        // 如果该目录不存在,则创建之
                        if(uploadFilePath.exists() == false) {
                            uploadFilePath.mkdirs();
                            System.out.println("路径不存在,但是已经成功创建了" + path);
                        }
                        String[] tempmsg = messageTemp.split(",");
                        System.out.println("来到此处："+"c:/ChatTemp/"+tempmsg[0]);
                        Base64Utils.Base64ToImage(tempmsg[1],"C:\\ChatTemp\\"+tempmsg[0]);
                        messageTemp = "C:\\ChatTemp\\"+tempmsg[0];
                    }

                    //转发时候没有带上friendNickName，所以从getMyFriendVOMap（）进行添加。
                    if(SingletonMessage.getSingleton().getMyFriendVOMap().get(friendUserName)!=null&&
                            !SingletonMessage.getSingleton().getMyFriendVOMap().get(friendUserName).getNickname().equals("")){
                        friendNickName = SingletonMessage.getSingleton().getMyFriendVOMap().get(friendUserName).getNickname();
                    }
                    //群组的处理
                    if(_myMessage.getResponse().getAcceptChatMessageResponse().getAcceptChatMessageBody(i).getAcceptUserId().length()>20){
                        //如果Map存储的有该用户的聊天记录就不用
                        if(SingletonMessage.getSingleton().getChatMessageMap().get(_myMessage.getResponse().getAcceptChatMessageResponse().getAcceptChatMessageBody(i).getAcceptUserId())==null){
                            ObservableList<AcceptMessageOfItem> currentChatting = FXCollections.observableArrayList(new AcceptMessageOfItem(
                                    friendUserName,
                                    friendNickName,
                                    _myMessage.getResponse().getAcceptChatMessageResponse().getAcceptChatMessageBody(i).getCreateTime(),
                                    messageTemp,
                                    Integer.parseInt(tempFlag))
                            );
                            SingletonMessage.getSingleton().getChatMessageMap().put(_myMessage.getResponse().getAcceptChatMessageResponse().getAcceptChatMessageBody(i).getAcceptUserId(),currentChatting);
                        }else{
                            SingletonMessage.getSingleton().getChatMessageMap().get(_myMessage.getResponse().getAcceptChatMessageResponse().getAcceptChatMessageBody(i).getAcceptUserId()).add(new AcceptMessageOfItem(
                                    friendUserName,
                                    friendNickName,
                                    _myMessage.getResponse().getAcceptChatMessageResponse().getAcceptChatMessageBody(i).getCreateTime(),
                                    messageTemp,
                                    Integer.parseInt(tempFlag))
                            );
                        }

                    //个人信息的处理
                    }else{
                        //如果Map存储的有该用户的聊天记录就不用
                        if(SingletonMessage.getSingleton().getChatMessageMap().get(_myMessage.getResponse().getAcceptChatMessageResponse().getAcceptChatMessageBody(i).getSendUserId())==null){
                            //第一次添加在左侧聊天窗体
                            System.out.println("111111:"+_myMessage.getResponse().getAcceptChatMessageResponse().getAcceptChatMessageBody(i).getSendUserNickname());
                            SingletonMessage.getSingleton().getNowChattingList().add(i,new OneOfListItems(friendUserName ,
                                    friendNickName ,0));
                            ObservableList<AcceptMessageOfItem> currentChatting = FXCollections.observableArrayList(new AcceptMessageOfItem(
                                    friendUserName,
                                    friendNickName,
                                    _myMessage.getResponse().getAcceptChatMessageResponse().getAcceptChatMessageBody(i).getCreateTime(),
                                    messageTemp,
                                    Integer.parseInt(tempFlag))
                            );

                            SingletonMessage.getSingleton().getChatMessageMap().put(_myMessage.getResponse().getAcceptChatMessageResponse().getAcceptChatMessageBody(i).getSendUserId(),currentChatting);
                        }else{
                            System.out.println("22222:"+_myMessage.getResponse().getAcceptChatMessageResponse().getAcceptChatMessageBody(i).getSendUserNickname());
                            SingletonMessage.getSingleton().getChatMessageMap().get(_myMessage.getResponse().getAcceptChatMessageResponse().getAcceptChatMessageBody(i).getSendUserId()).add(new AcceptMessageOfItem(
                                    friendUserName,
                                    friendNickName,
                                    _myMessage.getResponse().getAcceptChatMessageResponse().getAcceptChatMessageBody(i).getCreateTime(),
                                    messageTemp,
                                    Integer.parseInt(tempFlag))
                            );
                        }
                    }


                }
            }
        });
    }

    /**
     * 接收查询新的朋友的信息
     * @param myMessage
     */
    public void handleQueryFriendsAction(Message.MyMessage myMessage) {
        final Message.MyMessage _myMessage = myMessage;
        System.out.println(this.getClass().getResource("/").getPath()+"Image/");
        if(myMessage.getResponse().getQueryFriendsResponse().getFaceImage()!=null && !myMessage.getResponse().getQueryFriendsResponse().getFaceImage().equals("")){
            Base64Utils.Base64ToImage(myMessage.getResponse().getQueryFriendsResponse().getFaceImage(),this.getClass().getResource("/").getPath()+"Image/"+_myMessage.getResponse().getQueryFriendsResponse().getUserName()+".jpg");
        }
        Platform.runLater(new Runnable() {
            public void run() {
                SingletonMessage.getSingleton().getFindFriendList().clear();
                SingletonMessage.getSingleton().getFindFriendList().add(new OneOfListItems(_myMessage.getResponse().getQueryFriendsResponse().getUserName(),_myMessage.getResponse().getQueryFriendsResponse().getNickname(),3));
            }
        });
    }

    /**
     * 处理转发过来的群组信息。
     * @param myMessage
     */
    public void handleGroupMessageAction(Message.MyMessage myMessage) {
        System.out.println("!!!");
        for(int i =0;i<myMessage.getResponse().getCreateGroupResponse().getAllGroupCount();i++){
            List<UserVO> userVOList = new ArrayList<UserVO>();
            for(int j= 0;j<myMessage.getResponse().getCreateGroupResponse().getAllGroup(i).getGroupUserCount();j++){

                UserVO userVO = new UserVO();
                userVO.setUsername(myMessage.getResponse().getCreateGroupResponse().getAllGroup(i).getGroupUser(j).getUsername());
                userVO.setNickname(myMessage.getResponse().getCreateGroupResponse().getAllGroup(i).getGroupUser(j).getNickname());
                if(myMessage.getResponse().getCreateGroupResponse().getAllGroup(i).getGroupUser(j).getFaceImage()!=null && !myMessage.getResponse().getCreateGroupResponse().getAllGroup(i).getGroupUser(j).getFaceImage().equals("")){
                    Base64Utils.Base64ToImage(myMessage.getResponse().getCreateGroupResponse().getAllGroup(i).getGroupUser(j).getFaceImage(),this.getClass().getResource("/").getPath()+"Image/"+myMessage.getResponse().getCreateGroupResponse().getAllGroup(i).getGroupUser(j).getUsername()+".jpg");
                }
                userVOList.add(userVO);

            }
            GroupsVO groupsVO = new GroupsVO();
            groupsVO.setGroup_User(userVOList);
            groupsVO.setGroupName(myMessage.getResponse().getCreateGroupResponse().getAllGroup(i).getGroupName());
            groupsVO.setGroup_creator(myMessage.getResponse().getCreateGroupResponse().getAllGroup(i).getGroupCreator());
            System.out.println("SingletonMessage.getSingleton().getMyGroupMap():"+SingletonMessage.getSingleton().getMyGroupMap().size());
            SingletonMessage.getSingleton().getMyGroupMap().put(myMessage.getResponse().getCreateGroupResponse().getAllGroup(i).getGroupid(),groupsVO);
            System.out.println("SingletonMessage.getSingleton().getMyGroupMap():"+SingletonMessage.getSingleton().getMyGroupMap().size());
        }
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                System.out.println("!!!!");
                if(myMessage.getResponse().getCreateGroupResponse().getAllGroupCount()>1){
                    System.out.println("!!!!!");
                    for(String groupid : SingletonMessage.getSingleton().getMyGroupMap().keySet()){
                        OneOfListItems oneOfListItems = new OneOfListItems(groupid,SingletonMessage.getSingleton().getMyGroupMap().get(groupid).getGroupName(),0);
                        SingletonMessage.getSingleton().getNowChattingList().add(oneOfListItems);
                    }
                }else{
                    for(String groupid : SingletonMessage.getSingleton().getMyGroupMap().keySet()){
                        int count = SingletonMessage.getSingleton().getNowChattingList().size();
                        for(int i = 0;i<count;i++){
                            if(SingletonMessage.getSingleton().getNowChattingList().get(i).getUserName().equals(groupid)){
                                System.out.println("SingletonMessage.getSingleton().getNowChattingList().remove(i);");
                                SingletonMessage.getSingleton().getNowChattingList().remove(i);
                                SingletonMessage.getSingleton().getNowChattingList().add(i,new OneOfListItems(groupid,SingletonMessage.getSingleton().getMyGroupMap().get(groupid).getGroupName(),0));
                                break;
                            }
                            if(i == count-1){
                                OneOfListItems oneOfListItems = new OneOfListItems(groupid,SingletonMessage.getSingleton().getMyGroupMap().get(groupid).getGroupName(),0);
                                SingletonMessage.getSingleton().getNowChattingList().add(0,oneOfListItems);
                            }
                        }
                    }
                }

            }
        });
    }

    /**
     * 处理对方音视频聊天请求的信息
     * @param myMessage
     */
    public void handleCameraOrVoiceRequestAction(Message.MyMessage myMessage) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                //判断窗体是否打开，打开则返回用户正在音视频聊天，否则自动打开让用户选择是否接听
                if (CameraFrameMain.getInstance() == null) {
                    System.out.println("CameraFrameMain.getInstance() == null:"+CameraFrameMain.getInstance() == null);
                    if(myMessage.getRequest().getCameraOrVoiceRequest().getCameraOrVoiceType() == Camera){
                        System.out.println("该东西是视频模式！");
                        CameraFrameMain.getInstance(myMessage.getRequest().getCameraOrVoiceRequest().getSender(),false,true);
                        CameraFrameMain.getInstance().startPrimaryStage();
                    }else{
                        System.out.println("myMessage.getRequest().getCameraOrVoiceRequest().getCameraOrVoiceType() == Voice:"+(myMessage.getRequest().getCameraOrVoiceRequest().getCameraOrVoiceType() == Voice));
                        System.out.println("该东西是语音模式！");
                        CameraFrameMain.getInstance(myMessage.getRequest().getCameraOrVoiceRequest().getSender(),false,false);
                        CameraFrameMain.getInstance().startPrimaryStage();
                    }
                }else{
                    System.out.println("CameraFrameMain.getInstance() == null????????:"+CameraFrameMain.getInstance() == null);
                    Message.CameraOrVoiceResponse.Builder cameraOrVoiceResponse = Message.CameraOrVoiceResponse.newBuilder();
                    cameraOrVoiceResponse.setAccepter(myMessage.getRequest().getCameraOrVoiceRequest().getAccepter());
                    cameraOrVoiceResponse.setSender(myMessage.getRequest().getCameraOrVoiceRequest().getSender());
                    cameraOrVoiceResponse.setRefuseRemark("用户正在通话中！");
                    cameraOrVoiceResponse.setAcceptOrNo(Message.CameraOrVoiceResponse.AcceptOrNo.Refuse);
                    cameraOrVoiceResponse.setCameraOrVoiceType(Message.CameraOrVoiceResponse.CameraOrVoiceType.Camera);
                    if(myMessage.getRequest().getCameraOrVoiceRequest().getCameraOrVoiceType()==Voice){
                        cameraOrVoiceResponse.setCameraOrVoiceType(Message.CameraOrVoiceResponse.CameraOrVoiceType.Voice);
                    }
                    Message.MyMessage myMessage1 =  Message.MyMessage.newBuilder().setActionType(CameraOrVoiceResponseAction)
                            .setResponse(Message.Response.newBuilder().setCameraOrVoiceResponse(
                                    cameraOrVoiceResponse.build()
                            ).build()).build();
                    MyClient.getInstance().getChannel().writeAndFlush(myMessage1);
                }
            }
        });

    }

    /**
     * 处理对方接受或者拒绝音视频聊天后返回的信息
     * @param myMessage
     */
    public void handleCameraOrVoiceResponseAction(Message.MyMessage myMessage) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                //拒绝直接关闭窗体
                if (myMessage.getResponse().getCameraOrVoiceResponse().getAcceptOrNo() == Message.CameraOrVoiceResponse.AcceptOrNo.Refuse
                        && CameraFrameMain.getInstance() != null
                        && CameraFrameMain.getInstance().getCameraFrameController().getFrienduserName().equals(myMessage.getResponse().getCameraOrVoiceResponse().getAccepter())) {
                    System.out.println("handleCameraOrVoiceResponseAction进来了");
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error Message");
                    alert.setHeaderText(null);
                    alert.setContentText(myMessage.getResponse().getCameraOrVoiceResponse().getRefuseRemark());
                    alert.showAndWait();
                    CameraFrameMain.getInstance().getPrimaryStage().close();
                    CameraFrameMain.getInstance().destoryInstance();
                    MyClientUdp.getInstance().setInstance(null);
                    RecordOrPlayVoice.getSingleton().close();
                    MyCameraUtil.getSingleton().setClosed();
                } else if (myMessage.getResponse().getCameraOrVoiceResponse().getAcceptOrNo() == Message.CameraOrVoiceResponse.AcceptOrNo.Accept) {
                    //成功后开始连接
                    if (CameraFrameMain.getInstance() != null && CameraFrameMain.getInstance().getCameraFrameController().getFrienduserName().equals(myMessage.getResponse().getCameraOrVoiceResponse().getSender())) {
                        CameraFrameMain.getInstance().getCameraFrameController().startUDP();
                        System.out.println("成功后开始连接1111!");
                        if(CameraFrameMain.getInstance().getCameraFrameController().isHasCapture()){
                            CameraFrameMain.getInstance().getCameraFrameController().setCameraPane();
                            CameraFrameMain.getInstance().getCameraFrameController().startCamera();
                            CameraFrameMain.getInstance().getCameraFrameController().startVoice();
                        }else {
                            CameraFrameMain.getInstance().getCameraFrameController().startVoice();
                        }
                    } else {//用户把窗体关闭了或者已打开的窗体已不是原来那一个,发送关闭信息给对方
                        Message.MyMessage newMessage = Message.MyMessage.newBuilder().setActionType(CloseCameraOrVoiceAction).setCloseCameraOrVoice(
                                Message.CloseCameraOrVoice.newBuilder().setAcceptUserName(myMessage.getResponse().getCameraOrVoiceResponse().getSender())
                                        .setCloseRemark("对方已关闭！").setCloseUserName(SingletonMessage.getSingleton().getCurrentUser().getUsername()).build()
                        ).build();
                        MyClient.getInstance().getChannel().writeAndFlush(newMessage);
                    }

                }
            }
        });
    }

    @Override
    public void handleCloseCameraOrVoiceAction(Message.MyMessage myMessage) {
        System.out.println(" handleCloseCameraOrVoiceAction(Message.MyMessage myMessage) !!!!");
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                if(CameraFrameMain.getInstance() != null
                        && CameraFrameMain.getInstance().getCameraFrameController().getFrienduserName().equals(myMessage.getCloseCameraOrVoice().getCloseUserName())) {
                    MyClientUdp.getInstance().close();
                    MyClientUdp.getInstance().setInstance(null);
                    RecordOrPlayVoice.getSingleton().close();
                    MyCameraUtil.getSingleton().setClosed();
                    ((Stage)CameraFrameMain.getInstance().getPrimaryStage().getScene().getWindow()).close();
                    CameraFrameMain.getInstance().destoryInstance();

                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error Message");
                    alert.setHeaderText(null);
                    alert.setContentText(myMessage.getCloseCameraOrVoice().getCloseRemark());
                    alert.showAndWait();

                }
            }
        });
    }

    @Override
    public void handleDeleteFriendAction(Message.MyMessage myMessage) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {

                if(SingletonMessage.getSingleton().getMyFriendVOMap().get(myMessage.getRequest().getDeleteFriendRequest().getSenderUserName())!= null){
                    System.out.println("SingletonMessage.getSingleton().getMyFriendVOMap():"+SingletonMessage.getSingleton().getMyFriendVOMap().size());
                    SingletonMessage.getSingleton().getMyFriendVOMap().remove(myMessage.getRequest().getDeleteFriendRequest().getSenderUserName());
                    System.out.println("SingletonMessage.getSingleton().getMyFriendVOMap():"+SingletonMessage.getSingleton().getMyFriendVOMap().size());
                }
                if(SingletonMessage.getSingleton().getChatMessageMap().get(myMessage.getRequest().getDeleteFriendRequest().getSenderUserName())!=null){
                    System.out.println("SingletonMessage.getSingleton().getChatMessageMap():"+SingletonMessage.getSingleton().getChatMessageMap().size());
                    SingletonMessage.getSingleton().getChatMessageMap().remove(myMessage.getRequest().getDeleteFriendRequest().getSenderUserName());
                    System.out.println("SingletonMessage.getSingleton().getChatMessageMap():"+SingletonMessage.getSingleton().getChatMessageMap().size());
                }
                for(int i = 0 ;i < SingletonMessage.getSingleton().getMyFriendsList().size() ; i++){

                    if(SingletonMessage.getSingleton().getMyFriendsList().get(i).getUserName().equals(myMessage.getRequest().getDeleteFriendRequest().getSenderUserName())){
                        System.out.println("SingletonMessage.getSingleton().getMyFriendsList().size():"+SingletonMessage.getSingleton().getMyFriendsList().size());
                        SingletonMessage.getSingleton().getMyFriendsList().remove(i);
                        ChatMain.getInstance().getChatController().getFriendsList().setItems(null);
                        ChatMain.getInstance().getChatController().getFriendsList().setItems(SingletonMessage.getSingleton().getMyFriendsList());
                        System.out.println("SingletonMessage.getSingleton().getMyFriendsList().size():"+SingletonMessage.getSingleton().getMyFriendsList().size());
                    }
                }
                for(int i = 0 ;i < SingletonMessage.getSingleton().getNowChattingList().size() ; i++){
                    if(SingletonMessage.getSingleton().getNowChattingList().get(i).getUserName().equals(myMessage.getRequest().getDeleteFriendRequest().getSenderUserName())){
                        SingletonMessage.getSingleton().getNowChattingList().remove(i);
                    }
                }
            }
        });

    }
}
