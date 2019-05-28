package com.domain;

import java.util.List;

public class GroupsVO {
    private String group_creator;
    private String groupName;
    List<UserVO> group_User;

    public String getGroup_creator() {
        return group_creator;
    }

    public void setGroup_creator(String group_creator) {
        this.group_creator = group_creator;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public List<UserVO> getGroup_User() {
        return group_User;
    }

    public void setGroup_User(List<UserVO> group_User) {
        this.group_User = group_User;
    }
}
