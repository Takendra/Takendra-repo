package com.viewlift.freshchat;

import android.content.Context;
import android.text.TextUtils;

import com.freshchat.consumer.sdk.Freshchat;
import com.freshchat.consumer.sdk.FreshchatConfig;
import com.freshchat.consumer.sdk.FreshchatUser;
import com.freshchat.consumer.sdk.exception.MethodNotAllowedException;

public class FreshChatSDKUtil {

    public static String launchTime = "";

    public static void launchFreshChat(Context mContext, String userEmail, String userName, String userPhone, String countryCode) {
        //Update user information
        FreshchatUser user = Freshchat.getInstance(mContext).getUser();
        userName = (userName == null || TextUtils.isEmpty(userName)) ?userEmail:userName;
        user.setFirstName(userName).setEmail(userEmail).setPhone(countryCode, userPhone);
        try {
            Freshchat.getInstance(mContext).setUser(user);
            Freshchat.showConversations(mContext);
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
        Freshchat.setImageLoader(com.freshchat.consumer.sdk.j.af.aw(mContext));
        FreshchatConfig freshchatConfig = new FreshchatConfig(appId, appKey);
        Freshchat.getInstance(mContext).init(freshchatConfig);
    }

}
