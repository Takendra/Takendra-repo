package com.viewlift.freshchat;

import android.content.Context;
import android.text.TextUtils;

import com.freshchat.consumer.sdk.Freshchat;
import com.freshchat.consumer.sdk.FreshchatConfig;
import com.freshchat.consumer.sdk.FreshchatUser;
import com.freshchat.consumer.sdk.exception.MethodNotAllowedException;
import com.viewlift.AppCMSApplication;
import com.viewlift.db.AppPreference;
import com.viewlift.presenters.AppCMSPresenter;

import java.util.HashMap;
import java.util.Map;

public class FreshChatSDKUtil {

    public static String launchTime = "";
    public static boolean isFAQPage;

    public static void launchFreshChat(Context mContext, String userEmail, String userName, String userPhone, String countryCode) {
        //Update user information
        FreshchatUser user = Freshchat.getInstance(mContext).getUser();
        userName = (userName == null || TextUtils.isEmpty(userName)) ?userEmail:userName;
        user.setFirstName(userName).setEmail(userEmail).setPhone(countryCode, userPhone);
        try {
            Freshchat.getInstance(mContext).setUser(user);
            AppCMSPresenter appCMSPresenter = ((AppCMSApplication) mContext.getApplicationContext()).getAppCMSPresenterComponent().appCMSPresenter();
            AppPreference appPreference = appCMSPresenter.getAppPreference();
            Map<String, String> userMeta = new HashMap<String, String>();
            if(appPreference.getRegistrationType()!=null)
                userMeta.put("registrationType", appPreference.getRegistrationType());
            if(appPreference.getSubscriptionStatus()!=null)
                userMeta.put("subscriptionStatus", appPreference.getSubscriptionStatus());
            userMeta.put("isChurned", Boolean.toString(appPreference.isChurnedUser()));
            if(appPreference.getActiveSubscriptionPlatform()!=null)
                userMeta.put("subscriptionPlatform", appPreference.getActiveSubscriptionPlatform());
            if(appPreference.getActiveSubscriptionSku()!=null)
                userMeta.put("subscriptionSKU", appPreference.getActiveSubscriptionSku());

            if (isFAQPage) {
                Freshchat.showFAQs(mContext);
                isFAQPage = false;
            } else {
                Freshchat.showConversations(mContext);
            }
            launchTime = "";
        } catch (MethodNotAllowedException e) {
            e.printStackTrace();
        }
    }

    public static void setFirebaseToken(Context context, String fcmToken, String appId, String appKey) {
        init(context, appId, appKey);
        Freshchat.getInstance(context).setPushRegistrationToken(fcmToken);

    }

    public static void setFirebaseToken(Context context, String fcmToken) {
        Freshchat.getInstance(context).setPushRegistrationToken(fcmToken);
    }

    public static void handleFcmMessage(Context context, Object messageObject) {
        Freshchat.getInstance(context).handleFcmMessage(context, messageObject);
    }

    public static void init(Context mContext, String appId, String appKey) {
        //init
        Freshchat.setImageLoader(new CustomImageLoader());
        FreshchatConfig freshchatConfig = new FreshchatConfig(appId, appKey);
        Freshchat.getInstance(mContext).init(freshchatConfig);
    }

}
