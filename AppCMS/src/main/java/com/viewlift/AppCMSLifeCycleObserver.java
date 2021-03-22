package com.viewlift;

import android.util.Log;

import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.OnLifecycleEvent;

import com.viewlift.utils.CommonUtils;


public class AppCMSLifeCycleObserver implements LifecycleObserver {

    private static final String TAG = AppCMSLifeCycleObserver.class.getName();

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    public void onEnterForeground(){
        Log.d(TAG,"  App is in onEnterForeground ");
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    public void  onEnterBackground(){
        CommonUtils.isFromBackground = true;
        Log.d(TAG,"  App is in onEnterBackground ");
    }
}
