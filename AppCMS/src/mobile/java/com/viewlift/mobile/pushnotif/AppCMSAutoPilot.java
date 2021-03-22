package com.viewlift.mobile.pushnotif;

import android.content.Intent;
import android.net.Uri;

import androidx.annotation.NonNull;

import com.urbanairship.Autopilot;
import com.urbanairship.UAirship;
import com.urbanairship.actions.DeepLinkListener;
import com.viewlift.AppCMSApplication;
import com.viewlift.mobile.notification.CustomNotificationFactory;
import com.viewlift.presenters.AppCMSPresenter;

public class AppCMSAutoPilot extends Autopilot {
    private String prodAppKey;
    private String prodSecret;
    private String devAppKey;
    private String devSecret;
    private String senderId;

    @Override
    public void onAirshipReady(@NonNull UAirship airship) {
        airship.getPushManager().setUserNotificationsEnabled(true);
        airship.getPushManager()
                .setNotificationProvider(new CustomNotificationFactory());

        AirshipListener airshipListener = new AirshipListener();
        airship.getPushManager().addPushListener(airshipListener);
        airship.getPushManager().addPushTokenListener(airshipListener);
        airship.getPushManager().setNotificationListener(airshipListener);
        airship.getChannel().addChannelListener(airshipListener);

        airship.setDeepLinkListener(s ->
        {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(s))
                    .setPackage(UAirship.getPackageName())
                    .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            if(intent!=null && intent.getData()!=null&& UAirship.getApplicationContext() instanceof AppCMSApplication &&
                    ((AppCMSApplication)UAirship.getApplicationContext()).getAppCMSPresenterComponent()!=null&&
                    ((AppCMSApplication)UAirship.getApplicationContext()).getAppCMSPresenterComponent().appPreference()!=null)
           ((AppCMSApplication)UAirship.getApplicationContext()).getAppCMSPresenterComponent().appPreference().setUADeepLink(intent.getData().toString());
            UAirship.getApplicationContext().startActivity(intent);
            return true;
        });
        /*airship.getMessageCenter().setOnShowMessageCenterListener(messageId -> {
            // Use an implicit navigation deep link for now as explicit deep links are broken
            // with multi navigation host fragments
            Uri uri = Uri.parse("https://www.snagfilms.com/films/title/sink-or-swim");


            Intent intent = new Intent(Intent.ACTION_VIEW, uri)
                    .setPackage(UAirship.getPackageName())
                    .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            UAirship.getApplicationContext().startActivity(intent);
            return true;
        });
        CustomNotificationFactory notificationFactory;
        notificationFactory = new CustomNotificationFactory(UAirship.getApplicationContext());

        // Set the factory on the PushManager
        airship.getPushManager().setNotificationFactory(notificationFactory);*/


    }
}
