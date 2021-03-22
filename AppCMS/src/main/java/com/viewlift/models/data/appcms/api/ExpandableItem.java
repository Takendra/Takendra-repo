package com.viewlift.models.data.appcms.api;



import com.viewlift.views.customviews.expendablerecyclerview.models.ExpandableGroup;

import java.io.Serializable;
import java.util.List;

public class ExpandableItem extends ExpandableGroup<ExpandableItem> implements Serializable {
    public ExpandableItem(String title, List<ExpandableItem> items) {
        super(title, items);
    }
}
