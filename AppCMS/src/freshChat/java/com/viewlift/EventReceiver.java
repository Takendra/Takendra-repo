package com.viewlift;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import android.os.Handler;
import android.text.TextUtils;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.viewlift.db.AppPreference;
import com.viewlift.freshchat.FrashChatUserInfoActivity;
import com.viewlift.freshchat.FreshChatSDKUtil;
import com.viewlift.presenters.AppCMSPresenter;


public class EventReceiver extends BroadcastReceiver {
    AppCMSPresenter appCMSPresenter;
    AppPreference appPreference;

    @Override
    public void onReceive(Context context, Intent intent) {
        appCMSPresenter = ((AppCMSApplication) context.getApplicationContext()).getAppCMSPresenterComponent().appCMSPresenter();
        appPreference = appCMSPresenter.getAppPreference();
        if (intent != null) {
            String appId = intent.getStringExtra("FRESHCHAT_APP_ID");
            String appKey = intent.getStringExtra("FRESHCHAT_APP_KEY");

            String firebaseToken = intent.getStringExtra("FRESHCHAT_FIREBASE_TOKEN_KEY");
            String notification = intent.getStringExtra("FRESHCHAT_NOTIIFCATION_KEY");
            Object remoteMessage = intent.getExtras().getParcelable("FRESHCHAT_REMOTE_MESSAGE_KEY");
            if (!TextUtils.isEmpty(appId) &&
                    !TextUtils.isEmpty(appKey) &&
                    TextUtils.isEmpty(FreshChatSDKUtil.launchTime)) {

                FreshChatSDKUtil.launchTime = intent.getStringExtra("FRESHCHAT_APP_LAUNCH_TIME");
                FirebaseInstanceId.getInstance().getInstanceId().addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                    @Override
                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
                        if (!task.isSuccessful()) {
                            // Log.w(TAG, "getInstanceId failed", task.getException());

                            return;
                        }

                        // Get new Instance ID token
                        String token = task.getResult().getToken();
                        System.out.println("Frash Chat Token :-  " + token);
                        FreshChatSDKUtil.setFirebaseToken(context, token, appId, appKey);
                        if (appCMSPresenter.videoPlayerView != null && appCMSPresenter.videoPlayerView.getPlayer() != null) {
                            Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    appCMSPresenter.videoPlayerView.pausePlayer();
                                }
                            }, 2000);
                        }
                        if (((appPreference.getLoggedInUserPhone() != null
                                && appPreference.getLoggedInUserPhoneCounntryCode() != null) || (appPreference.getFreshchatPhone() != null
                                && appPreference.getFreshchatPhoneCountryCode() != null))
                                && intent.getStringExtra("FRESHCHAT_APP_LAUNCH_TIME").equalsIgnoreCase(FreshChatSDKUtil.launchTime)) {

                            String phone = appPreference.getFreshchatPhone();
                            String phoneCountryCode = appPreference.getFreshchatPhoneCountryCode();
                            if (appPreference.getLoggedInUserPhone() != null)
                                phone = appPreference.getLoggedInUserPhone();
                            if (appPreference.getLoggedInUserPhoneCounntryCode() != null)
                                phoneCountryCode = appPreference.getLoggedInUserPhoneCounntryCode();

                            FreshChatSDKUtil.launchFreshChat(context,
                                    appPreference.getLoggedInUserEmail(),
                                    appPreference.getLoggedInUserName(),
                                    phone,
                                    phoneCountryCode);

                        } else {
                            Intent userInfoIntent = new Intent(appCMSPresenter.getCurrentActivity(), FrashChatUserInfoActivity.class);
                            appCMSPresenter.getCurrentActivity().startActivity(userInfoIntent);
                        }
                    }
                });

            } else if (firebaseToken != null && !TextUtils.isEmpty(firebaseToken)) {
                FreshChatSDKUtil.setFirebaseToken(context, firebaseToken);
            } else if (notification != null && !TextUtils.isEmpty(notification)
                    && remoteMessage != null) {
                FreshChatSDKUtil.handleFcmMessage(context, remoteMessage);
            }

        }
    }

}
