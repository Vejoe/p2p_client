package com.ui.creatGroup;

import javafx.scene.control.ListCell;

public class CreatGroupOfItemCell  extends ListCell<CreatGroupOfItem> {
    @Override
    public void updateItem(CreatGroupOfItem creatGroupOfItem, boolean empty) {
        super.updateItem(creatGroupOfItem, empty);
        if (creatGroupOfItem != null) {
            setGraphic(creatGroupOfItem.gethBoxItem());
        }else{
            setGraphic(null);
        }
    }
}
