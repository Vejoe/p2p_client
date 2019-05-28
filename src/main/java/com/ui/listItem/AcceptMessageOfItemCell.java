package com.ui.listItem;

import javafx.scene.control.ListCell;

public class AcceptMessageOfItemCell  extends ListCell<AcceptMessageOfItem> {
    @Override
    public void updateItem(AcceptMessageOfItem acceptMessageOfItem, boolean empty) {
        super.updateItem(acceptMessageOfItem, empty);
        if (acceptMessageOfItem != null) {
            setGraphic(acceptMessageOfItem.getHBoxItem());
        }else{
            setGraphic(null);
        }
    }
}
