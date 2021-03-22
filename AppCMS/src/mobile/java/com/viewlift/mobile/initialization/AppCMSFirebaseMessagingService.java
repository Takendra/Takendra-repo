package com.viewlift.mobile.initialization;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.webkit.URLUtil;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.appsflyer.AppsFlyerLib;
import com.clevertap.android.sdk.CleverTapAPI;
import com.clevertap.android.sdk.pushnotification.NotificationInfo;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.urbanairship.push.fcm.AirshipFirebaseIntegration;
import com.viewlift.AppCMSApplication;
import com.viewlift.EventReceiver;
import com.viewlift.R;
import com.viewlift.presenters.AppCMSPresenter;
import com.viewlift.utils.CommonUtils;
import com.viewlift.views.activity.AppCMSPageActivity;

import java.util.Iterator;
import java.util.Map;

public class AppCMSFirebaseMessagingService extends FirebaseMessagingService {
    private static int NOTIFICATION_ID = 413;
    EventReceiver eventReceiver = new EventReceiver();


    @Override
    public void onCreate() {
        super.onCreate();
        registerReceiver(eventReceiver, new IntentFilter("LAUNCH_EVENT"));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver(eventReceiver);
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        try {
            if (isFreshchatNotification(remoteMessage)) {
                Intent launchFreshChatBroadcast = new Intent("LAUNCH_EVENT");
                launchFreshChatBroadcast.putExtra("FRESHCHAT_NOTIIFCATION_KEY", "FRESHCHAT_NOTIIFCATION_KEY");
                launchFreshChatBroadcast.putExtra("FRESHCHAT_REMOTE_MESSAGE_KEY", remoteMessage);
                this.sendBroadcast(launchFreshChatBroadcast);
            } else if (remoteMessage.getData().size() > 0) {
                Bundle extras = new Bundle();
                for (Map.Entry<String, String> entry : remoteMessage.getData().entrySet()) {
                    extras.putString(entry.getKey(), entry.getValue());
                }
                NotificationInfo info = CleverTapAPI.getNotificationInfo(extras);
                if (info.fromCleverTap) {
                    AppCMSPresenter appCMSPresenter = ((AppCMSApplication)getApplication()).getAppCMSPresenterComponent().appCMSPresenter();
                    if (appCMSPresenter != null && appCMSPresenter.isCleverTapAvailable) {
                        //CleverTapAPI.createNotification(this.getApplicationContext(), extras);
                        CleverTapAPI.processPushNotification(getApplicationContext(), extras);
                    }
                } else if (isUrbanAirshipAlert(remoteMessage)) {
                    // not from CleverTap handle yourself or pass to another provider

                    AirshipFirebaseIntegration.processMessageSync(this.getApplicationContext(), remoteMessage);

                } else {
                    /**
                     * This will call if application is in focus
                     */
                    if (remoteMessage.getNotification() != null) {
                        sendNotification(remoteMessage);
                    }

                }
            }else if (remoteMessage.getNotification() != null) {
                sendNotification(remoteMessage);
            }
        } catch (Exception t) {
            Log.d("FirebaseMessagingServic", "Error parsing FCM message" + t.getMessage());
            t.printStackTrace();
        }
    }

    public boolean isUrbanAirshipAlert(RemoteMessage remoteMessage) {

        try {
            for (String key : remoteMessage.getData().keySet()) {
                if (key.equalsIgnoreCase("com.urbanairship.push.ALERT")) {
                    Log.d("isUrbanAirshipAlert:- ", key);
                    return true;

                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    @Override
    public void onNewToken(String newToken) {
        //super.onNewToken(newToken);
        // Notify Urban Airship that the token is refreshed.

        System.out.println(" onNewToken onNewToken " + newToken);
        Log.d("create new onNewToken", newToken);

        // Sending new token to AppsFlyer
        try {
            AirshipFirebaseIntegration.processNewToken(this.getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }
        try{
            AppsFlyerLib.getInstance().updateServerUninstallToken(getApplicationContext(), newToken);
        }catch (Exception exception){
            exception.printStackTrace();
        }


        /**
         * FreshChat token placement
         */
        Intent launchFreshChatBroadcast = new Intent("LAUNCH_EVENT");
        launchFreshChatBroadcast.putExtra("FRESHCHAT_FIREBASE_TOKEN_KEY", newToken);
        sendBroadcast(launchFreshChatBroadcast);
    }

    private void sendNotification(RemoteMessage remoteMessage) {
        Intent intent = new Intent(this, AppCMSPageActivity.class);
        String link = remoteMessage.getData().get("deeplink");
        String openBrowser = remoteMessage.getData().get("openBrowser");
        PendingIntent pendingIntent = null;
        if (openBrowser != null && !CommonUtils.isEmpty(openBrowser)
            && URLUtil.isValidUrl(openBrowser)) {
            final Intent intentOpenBrowser = new Intent(Intent.ACTION_VIEW, Uri.parse(openBrowser));
            //intent.setPackage(getAlternative(context));
            intentOpenBrowser.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intentOpenBrowser,
                    PendingIntent.FLAG_ONE_SHOT);
        }else {

            intent.putExtra("deeplink_uri", link);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
             pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                    PendingIntent.FLAG_ONE_SHOT);
        }

        String channelId = getString(R.string.default_notification_channel_id);
        String channelName = getString(R.string.default_notification_channel_name);
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(this, channelId)
                        .setSmallIcon(R.mipmap.app_logo)
                        .setContentTitle(remoteMessage.getNotification().getTitle())
                        .setContentText(remoteMessage.getNotification().getBody())
                        .setAutoCancel(true)
                        .setSound(defaultSoundUri)
                        .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        // Since android Oreo notification channel is needed.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            try {
                NotificationChannel channel = new NotificationChannel(channelId,
                        channelName,
                        NotificationManager.IMPORTANCE_DEFAULT);
                notificationManager.createNotificationChannel(channel);
            }catch (Exception e){
                e.printStackTrace();
            }
        }

        //NOTIFICATION_ID = (int) SystemClock.uptimeMillis();
        NOTIFICATION_ID = CommonUtils.getNotificationId();
        notificationManager.notify(NOTIFICATION_ID, notificationBuilder.build());
    }

    /**
     * Following three methods are for checking if Notification came from FreshChat
     */

    public boolean isFreshchatNotification(@NonNull Bundle bundle) {
        return bundle != null && bundle.containsKey("source") && "freshchat_user".equals(bundle.getString("source"));
    }

    public boolean isFreshchatNotification(@NonNull Object messageObject) {
        Bundle var1 = getFreshChatBundle(messageObject);


        return var1 != null && isFreshchatNotification(var1);
    }

    public Bundle getFreshChatBundle(@NonNull Object var0) {
        if (var0 instanceof RemoteMessage) {
            RemoteMessage var1 = (RemoteMessage) var0;
            if (var1.getData() != null && var1.getData().size() != 0) {
                Bundle var2 = new Bundle();
                Map var3 = var1.getData();
                Iterator var4 = var3.keySet().iterator();

                while (var4.hasNext()) {
                    String var5 = (String) var4.next();
                    var2.putString(var5, (String) var3.get(var5));
                }

                return var2;
            } else {
                return null;
            }
        }
        return null;
    }
}
