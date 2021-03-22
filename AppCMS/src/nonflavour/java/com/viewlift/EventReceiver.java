package com.viewlift;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;


public class EventReceiver extends BroadcastReceiver {

    

    @Override
    public void onReceive(Context context, Intent intent) {
        System.out.println("EventReceiver  NNo Flavour  sdsdsd");
    }
}
