package com.viewlift.mobile.pushnotif;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.urbanairship.UAirship;
import com.urbanairship.actions.ActionValue;
import com.urbanairship.actions.OpenExternalUrlAction;
import com.urbanairship.channel.AirshipChannelListener;
import com.urbanairship.push.NotificationActionButtonInfo;
import com.urbanairship.push.NotificationInfo;
import com.urbanairship.push.NotificationListener;
import com.urbanairship.push.PushListener;
import com.urbanairship.push.PushMessage;
import com.urbanairship.push.PushTokenListener;

import java.util.Locale;
import java.util.Map;

import com.viewlift.mobile.AppCMSLaunchActivity;
import com.viewlift.utils.CommonUtils;
import com.zendesk.util.StringUtils;
import zendesk.core.Zendesk;
import zendesk.support.Support;
import zendesk.support.request.RequestActivity;

public class AirshipListener implements PushListener, NotificationListener, PushTokenListener, AirshipChannelListener {

    private static final String TAG = "AirshipListener";

    @Override
    public void onNotificationPosted(@NonNull NotificationInfo notificationInfo) {
        Log.i(TAG, "Notification posted: " + notificationInfo);
    }

    @Override
    public boolean onNotificationOpened(@NonNull NotificationInfo notificationInfo) {
        Log.i(TAG, "Notification opened: " + notificationInfo);


        Map<String, ActionValue> actions = notificationInfo.getMessage().getActions();

        if (actions.containsKey(OpenExternalUrlAction.DEFAULT_REGISTRY_NAME) || actions.containsKey(OpenExternalUrlAction.DEFAULT_REGISTRY_SHORT_NAME)) {

           /* Intent intent = new Intent(Intent.ACTION_VIEW, uri)
                    .setPackage(UAirship.getPackageName())
                    .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            UAirship.getApplicationContext().startActivity(intent);*/

            // Just return true so SDK does not auto launch the main activity
            return true;


        }

        // Extract ticket id
        final String tid = notificationInfo.getMessage().getPushBundle().getString("tid");
        if (tid != null) {
            // Check if ticket id is valid
            if (!StringUtils.hasLength(tid)) {
                Log.e(TAG, String.format(Locale.US, "No valid ticket id found: '%s'", tid));
                return false;
            }

            Log.d(TAG, String.format(Locale.US, "Ticket Id found: %s", tid));

            // Create an Intent for showing RequestActivity
            final Intent zendeskDeepLinkIntent = getZendeskDeepLinkIntent(UAirship.getApplicationContext(), tid);

            // Check if Intent is valid
            if (zendeskDeepLinkIntent == null) {
                Log.e(TAG, "Error zendeskDeepLinkIntent is 'null'");
                return false;
            }
            //UAirship.getApplicationContext().startActivity(zendeskDeepLinkIntent);
            // Show RequestActivity
            UAirship.getApplicationContext().sendBroadcast(zendeskDeepLinkIntent);
            return true;

        }
        // Return false here to allow Airship to auto launch the launcher
        // activity for foreground notification action buttons
        return false;
    }

    @Override
    public boolean onNotificationForegroundAction(@NonNull NotificationInfo notificationInfo, @NonNull NotificationActionButtonInfo actionButtonInfo) {
        Log.i(TAG, "Notification action: " + notificationInfo + " " + actionButtonInfo);

        // Return false here to allow Airship to auto launch the launcher
        // activity for foreground notification action buttons
        return false;
    }

    @Override
    public void onNotificationBackgroundAction(@NonNull NotificationInfo notificationInfo, @NonNull NotificationActionButtonInfo actionButtonInfo) {
        Log.i(TAG, "Notification action: " + notificationInfo + " " + actionButtonInfo);
    }

    @Override
    public void onNotificationDismissed(@NonNull NotificationInfo notificationInfo) {
        Log.i(TAG, "Notification dismissed. Alert: " + notificationInfo.getMessage().getAlert() + ". Notification ID: " + notificationInfo.getNotificationId());
    }

    @Override
    public void onPushReceived(@NonNull PushMessage message, boolean notificationPosted) {
        Log.i(TAG, "Received push message. Alert: " + message.getAlert() + ". Posted notification: " + notificationPosted);
    }

    @Override
    public void onChannelCreated(@NonNull String s) {
        Log.i(TAG, "onChannelCreated: " + s);
    }

    @Override
    public void onChannelUpdated(@NonNull String s) {
        Log.i(TAG, "onChannelUpdated: " + s);
    }

    @Override
    public void onPushTokenUpdated(@NonNull String s) {
        Log.i(TAG, "onPushTokenUpdated: " + s);
    }



    @Nullable
    private Intent getZendeskDeepLinkIntent(Context context, String ticketId) {

        // Make sure that Zendesk is initialized
        if (!Zendesk.INSTANCE.isInitialized()) {
            Zendesk.INSTANCE.init(context, CommonUtils.getZendeskSubdomainUrl(),
                    CommonUtils.getZendeskApplicationId(),
                    CommonUtils.getZendeskOauthClientId());
            Support.INSTANCE.init(Zendesk.INSTANCE);
        }

        // Initialize a back stack
        final Intent appCMSLaunchActivity = new Intent(context, AppCMSLaunchActivity.class);

        return RequestActivity.builder().withRequestId(ticketId).deepLinkIntent(context, appCMSLaunchActivity);
    }

}
