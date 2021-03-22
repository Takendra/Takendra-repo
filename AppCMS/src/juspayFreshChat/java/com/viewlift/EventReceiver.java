package com.viewlift;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.text.TextUtils;

import com.google.firebase.iid.FirebaseInstanceId;
import com.viewlift.db.AppPreference;
import com.viewlift.freshchat.FreshChatInfoActivity;
import com.viewlift.freshchat.FreshChatSDKUtil;
import com.viewlift.juspay.JuspayPaymentActivity;
import com.viewlift.presenters.AppCMSPresenter;

import in.juspay.godel.PaymentActivity;

import static com.viewlift.presenters.AppCMSPresenter.JUSPAY_REQUEST_CODE;

public class EventReceiver extends BroadcastReceiver {

    AppCMSPresenter appCMSPresenter;
    Context mContext;

    @Override
    public void onReceive(Context context, Intent intent) {
        appCMSPresenter = ((AppCMSApplication) context.getApplicationContext()).getAppCMSPresenterComponent().appCMSPresenter();
        mContext = context;

        if (intent != null && !TextUtils.isEmpty(intent.getAction())) {
            if (intent.getAction().equalsIgnoreCase(AppCMSPresenter.ACTION_LAUNCH_JUSPAY))
                launchJusPay(context, intent);
            else if (intent.getAction().equalsIgnoreCase(AppCMSPresenter.ACTION_PRE_FATCH_JUSPAY_ASSETS))
                PaymentActivity.preFetch(appCMSPresenter.getCurrentActivity(), appCMSPresenter.getMerchantId() + "_android");
            else
                launchFreshChat(context, intent);
        }
    }

    private void launchFreshChat(Context context, Intent intent) {
        AppPreference appPreference = appCMSPresenter.getAppPreference();
        if (context != null && intent != null) {
            String appId = intent.getStringExtra("FRESHCHAT_APP_ID");
            String appKey = intent.getStringExtra("FRESHCHAT_APP_KEY");

            String firebaseToken = intent.getStringExtra("FRESHCHAT_FIREBASE_TOKEN_KEY");
            String notification = intent.getStringExtra("FRESHCHAT_NOTIIFCATION_KEY");
            Object remoteMessage = intent.getExtras().getParcelable("FRESHCHAT_REMOTE_MESSAGE_KEY");
            if (!TextUtils.isEmpty(appId) &&
                    !TextUtils.isEmpty(appKey) &&
                    TextUtils.isEmpty(FreshChatSDKUtil.launchTime)) {

                FreshChatSDKUtil.launchTime = intent.getStringExtra("FRESHCHAT_APP_LAUNCH_TIME");
                FreshChatSDKUtil.isFAQPage  = intent.getBooleanExtra("IS_FAQ_PAGE", false);
                FirebaseInstanceId.getInstance().getInstanceId().addOnCompleteListener(task -> {
                    if (!task.isSuccessful()) {
                        // Log.w(TAG, "getInstanceId failed", task.getException());
                        FreshChatSDKUtil.launchTime = "";
                        return;
                    }

                    // Get new Instance ID token
                    String token = task.getResult().getToken();
                    System.out.println("Frash Chat Token :-  " + token);
                    FreshChatSDKUtil.setFirebaseToken(context, token, appId, appKey);
                    if (appCMSPresenter.videoPlayerView != null&&appCMSPresenter.videoPlayerView.getPlayer()!=null){
                        Handler handler = new Handler();
                        handler.postDelayed(() -> appCMSPresenter.videoPlayerView.pausePlayer(), 2000);
                    }
                    if (appCMSPresenter.getUserPhoneNumber() != null
                            && appPreference.getLoggedInUserPhoneCounntryCode() != null
                            && intent.getStringExtra("FRESHCHAT_APP_LAUNCH_TIME").equalsIgnoreCase(FreshChatSDKUtil.launchTime)) {

                        FreshChatSDKUtil.launchFreshChat(mContext,
                                appPreference.getLoggedInUserEmail(),
                                appPreference.getLoggedInUserName(),
                                appCMSPresenter.getUserPhoneNumber(),
                                appPreference.getLoggedInUserPhoneCounntryCode());

                    }else if (appPreference.getFreshchatPhone() != null
                            && appPreference.getLoggedInUserPhoneCounntryCode() != null
                            && intent.getStringExtra("FRESHCHAT_APP_LAUNCH_TIME").equalsIgnoreCase(FreshChatSDKUtil.launchTime)) {

                        FreshChatSDKUtil.launchFreshChat(mContext,
                                appPreference.getLoggedInUserEmail(),
                                appPreference.getLoggedInUserName(),
                                appPreference.getFreshchatPhone(),
                                appPreference.getLoggedInUserPhoneCounntryCode());

                    } else {
                        if (appCMSPresenter.isHoichoiApp()) {
                            /*
                               According to Vishnu from Hoichoi
                               Do not show FrashChatUserInfoActivity to Hoichoi user.
                             */
                            FreshChatSDKUtil.launchFreshChat(mContext,
                                    appPreference.getLoggedInUserEmail(),
                                    appPreference.getLoggedInUserName(),
                                    appCMSPresenter.getUserPhoneNumber(),
                                    appPreference.getLoggedInUserPhoneCounntryCode());
                        } else {
                            Intent userInfoIntent = new Intent(appCMSPresenter.getCurrentActivity(), FreshChatInfoActivity.class);
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

    private void launchJusPay(Context context, Intent intent) {
        if (intent != null && appCMSPresenter.getCurrentActivity() != null) {
            String planId  = intent.getStringExtra(context.getString(R.string.app_cms_plan_id));
            String payLoad = intent.getStringExtra(context.getString(R.string.juspay_payload));
            AppPreference appPreference = appCMSPresenter.getAppPreference();
            if (!TextUtils.isEmpty(planId) && !TextUtils.isEmpty(payLoad) && appPreference != null && !TextUtils.isEmpty(appPreference.getLoggedInUser())) {
                Intent juspayIntent = new Intent(context, JuspayPaymentActivity.class);
                juspayIntent.putExtra(context.getString(R.string.app_cms_plan_id), planId);
                juspayIntent.putExtra(context.getString(R.string.juspay_payload), payLoad);
                appCMSPresenter.getCurrentActivity().startActivityForResult(juspayIntent, JUSPAY_REQUEST_CODE);
            }
        }
    }
}
