package com.viewlift.analytics;

import android.content.Context;
import android.content.Intent;

import androidx.legacy.content.WakefulBroadcastReceiver;

import com.clevertap.android.sdk.CleverTapAPI;

public class CleverTapGcmBroadcastReceiver extends WakefulBroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        CleverTapAPI.createNotification(context, intent.getExtras());
    }
}
