package com.ui.listItem;

import javafx.scene.control.ListCell;

public class NewFriendsListViewCell extends ListCell<NewFriendOfRightItem> {
    @Override
    public void updateItem(NewFriendOfRightItem newFriendOfRightItem, boolean empty) {
        super.updateItem(newFriendOfRightItem, empty);
        if (newFriendOfRightItem != null) {
            setGraphic(newFriendOfRightItem.gethBoxItem());
        }else{
            setGraphic(null);
        }
    }
}
