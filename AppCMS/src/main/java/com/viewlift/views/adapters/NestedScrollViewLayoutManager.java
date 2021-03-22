package com.viewlift.views.adapters;

import android.content.Context;
import androidx.recyclerview.widget.LinearLayoutManager;

public class NestedScrollViewLayoutManager extends LinearLayoutManager {

    private boolean isScrollEnabled = true;

    public NestedScrollViewLayoutManager(Context context) {
        super(context);
    }

    public void setScrollEnabled(boolean flag) {
        this.isScrollEnabled = flag;
    }

    @Override
    public boolean canScrollVertically() {
        //Similarly you can customize "canScrollHorizontally()" for managing horizontal scroll
        return isScrollEnabled && super.canScrollVertically();
    }
}
