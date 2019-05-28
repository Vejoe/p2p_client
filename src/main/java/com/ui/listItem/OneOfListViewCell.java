package com.ui.listItem;

import javafx.scene.control.ListCell;

public class OneOfListViewCell extends ListCell<OneOfListItems> {
    @Override
    public void updateItem(OneOfListItems oneOfListItems, boolean empty)
    {
        super.updateItem(oneOfListItems,empty);
        if(oneOfListItems != null)
        {
            setGraphic(oneOfListItems.gethBoxItem());
        }else{
            setGraphic(null);
        }
    }
}
