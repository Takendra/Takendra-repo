package com.viewlift.mobile.notification;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

import com.urbanairship.push.PushMessage;
import com.urbanairship.push.notifications.ActionsNotificationExtender;
import com.urbanairship.push.notifications.NotificationArguments;
import com.urbanairship.push.notifications.NotificationProvider;
import com.urbanairship.push.notifications.NotificationResult;
import com.urbanairship.util.NotificationIdGenerator;
import com.urbanairship.util.UAStringUtil;
import com.viewlift.R;
import com.viewlift.utils.CommonUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Iterator;

public class CustomNotificationFactory implements NotificationProvider  {

    Context context;
    private static final String CHANNEL_ID = "com.viewlift.Airship.Notification.CHANNEL_ID";
    private static final String UAMP_CHANNEL_ID = "UAMP_Channel_ID";
    private static final String UAMP_CHANNEL_DESC = "Channel ID for UAMP";
   /* public CustomNotificationFactory(@NonNull Context context) {
        super(context);
        this.context = context;
    }

    @Nullable
    @Override
    public Notification createNotification(@NonNull PushMessage message, int notificationId) {
        // do not display a notification if there is not an alert
        if (UAStringUtil.isEmpty(message.getAlert())) {
            return null;
        }
        System.out.println("onPushReceived from createNotification  ");
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        /**
         * Urban Airship deeplink get handles by bellow code through the SDK
         * Only restriction is deeplink must create at urbanAirship as follows
         *    1. app://<deeplink URL>
         *    2. in mainfest.xml must add metadata to with android:scheme="app"
         *
         */
    /**
     * Creates Notification Channel. This is required in Android O+ to display notifications.
     */
    @RequiresApi(Build.VERSION_CODES.O)
    private void createNotificationChannel(NotificationManager mNotificationManager) {
        if (mNotificationManager.getNotificationChannel(CHANNEL_ID) == null) {
            try {
                NotificationChannel notificationChannel =
                        new NotificationChannel(CHANNEL_ID,
                                UAMP_CHANNEL_ID,
                                NotificationManager.IMPORTANCE_LOW);

                notificationChannel.setDescription(UAMP_CHANNEL_DESC);

                mNotificationManager.createNotificationChannel(notificationChannel);
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    @NonNull
    @Override
    public NotificationArguments onCreateNotificationArguments(@NonNull Context context, @NonNull PushMessage pushMessage) {
        String channel = pushMessage.getNotificationChannel("defaultChannel");
        return NotificationArguments.newBuilder(pushMessage)
                .setNotificationChannelId(channel)
                .setNotificationId(pushMessage.getNotificationTag(), NotificationIdGenerator.nextID())
                .build();
    }

    @NonNull
    @Override
    public NotificationResult onCreateNotification(@NonNull Context context, @NonNull NotificationArguments notificationArguments) {
        this.context=context;
        PushMessage message = notificationArguments.getMessage();
        // do not display a notification if there is not an alert
        if (UAStringUtil.isEmpty(message.getAlert())) {
            return NotificationResult.cancel();
        }


        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        int color = Build.VERSION.SDK_INT >= 23 ? context.getResources().getColor(R.color.colorAccent, null) : context.getResources().getColor(R.color.colorAccent);
        // Notification channels are only supported on Android O+.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createNotificationChannel(notificationManager);
        }
       String payload= message.getStylePayload();
        //Bitmap bitmap = getBitmapFromURL("https://homepages.cae.wisc.edu/~ece533/images/airplane.png");
        Bitmap bitmap = null;
        if(payload!=null&&!payload.isEmpty()) {
            try {
                JSONObject jsonObject = new JSONObject(payload);
                for (Iterator<String> iter = jsonObject.keys(); iter.hasNext(); ) {
                    String key = iter.next();
                    if (key.equalsIgnoreCase("big_picture")) {
                        String value= String.valueOf(jsonObject.getString(key));
                        bitmap= CommonUtils.getBitmapFromURL(value);
                    }
                }
            } catch (JSONException err) {
                Log.d("Error", err.toString());
            }
            System.out.println(payload);
        }
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.BigPictureStyle bigPictureStyle= new NotificationCompat.BigPictureStyle();
        bigPictureStyle.bigLargeIcon(bitmap).bigPicture(bitmap);
        if(bitmap==null){
            bigPictureStyle=null;
        }
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context,CHANNEL_ID)
                .setSmallIcon(R.mipmap.app_logo)
                .setColor(color)
                .setContentTitle(message.getTitle())
                .setContentText(message.getAlert())
                .setStyle(bigPictureStyle)
                .setAutoCancel(true).setLargeIcon(bitmap).setSubText(message.getSummary())
                .setSound(defaultSoundUri);

        // Notification action buttons
        notificationBuilder.extend(new ActionsNotificationExtender(context, notificationArguments));

        return NotificationResult.notification(notificationBuilder.build());

    }

    @Override
    public void onNotificationCreated(@NonNull Context context, @NonNull Notification notification, @NonNull NotificationArguments notificationArguments) {

    }
//    public Bitmap getBitmapFromURL(String strURL) {
//        try {
//            URL url = new URL(strURL);
//            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
//            connection.setDoInput(true);
//            connection.connect();
//            InputStream input = connection.getInputStream();
//            Bitmap myBitmap = BitmapFactory.decodeStream(input);
//            return myBitmap;
//        } catch (IOException e) {
//            e.printStackTrace();
//            return null;
//        }
//    }
}
