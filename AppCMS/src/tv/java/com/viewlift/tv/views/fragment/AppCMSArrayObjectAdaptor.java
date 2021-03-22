package com.viewlift.tv.views.fragment;


import androidx.leanback.widget.ArrayObjectAdapter;
import androidx.leanback.widget.Presenter;

public class AppCMSArrayObjectAdaptor extends ArrayObjectAdapter {
    public AppCMSArrayObjectAdaptor(Presenter presenter) {
        super(presenter);
    }

    public void notifyMe(){
        notifyChanged();
    }
}
