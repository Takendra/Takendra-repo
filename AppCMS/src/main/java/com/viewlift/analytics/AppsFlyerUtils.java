package com.viewlift.analytics;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;
import android.text.TextUtils;

import com.appsflyer.AppsFlyerLib;
import com.viewlift.R;
import com.viewlift.Utils;
import com.viewlift.models.data.appcms.api.ContentDatum;
import com.viewlift.models.data.appcms.api.CreditBlock;
import com.viewlift.models.data.appcms.ui.AppCMSUIKeyType;
import com.viewlift.models.data.appcms.ui.android.MetaPage;
import com.viewlift.presenters.AppCMSPresenter;
import com.viewlift.views.activity.AppCMSPageActivity;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import static com.viewlift.Utils.isFireTVDevice;

/**
 * Created by amit on 27/07/17.
 */

public class AppsFlyerUtils {

    private static final String REGISTRATION_APP_EVENT_NAME = "Registration";
    private static final String APP_OPEN_EVENT_NAME = "App Open";
    private static final String LOGIN_EVENT_NAME = "Login";
    private static final String LOGOUT_EVENT_NAME = "Logout";
    private static final String SUBSCRIPTION_EVENT_NAME = "Subscription";
    private static final String TVOD_PURCHASE = "Tvod_purchase";
    private static final String TVOD_PURCHASE_COMPLETE = "tvod_purchase_completion";
    //    public static final String CANCEL_SUBSCRIPTION_EVENT_NAME = "Cancel Subscription";
    private static final String FILM_VIEWING_EVENT_NAME = "Film Viewing";

    private static final String USER_ID_EVENT_VALUE = "UUID";
    private static final String DEVICE_ID_EVENT_VALUE = "Device ID";
    private static final String USER_ENTITLEMENT_STATE_EVENT_VALUE = "Entitled";
    private static final String USER_REGISTER_STATE_EVENT_VALUE = "Registered";
    private static final String PRODUCT_NAME_EVENT_VALUE = "Product Name";
    private static final String PRICE_EVENT_VALUE = "Price";
    private static final String CURRENCY_EVENT_VALUE = "Currency";

    private static final String EVENT_PARAM_USER_NAME = "name";
    private static final String EVENT_PARAM_USER_EMAIL = "email";
    private static final String EVENT_PARAM_USER_PHONE = "phone";
    private static final String EVENT_PARAM_USER_ID = "userId";

    private static final String EVENT_PARAM_REGISTRATION_TYPE = "registrationType";
    private static final String EVENT_PARAM_APP_VERSION = "appVersion";
    private static final String EVENT_PARAM_PLATFORM = "platform";
    private static final String EVENT_PARAM_SOURCE = "Source";
    private static final String EVENT_PARAM_AMOUNT = "amount";
    private static final String EVENT_PARAM_PAYMENT_PLAN = "paymentPlan";
    private static final String EVENT_PARAM_COUNTRY = "country";
    private static final String EVENT_PARAM_DISCOUNT_AMOUNT = "discountAmount";
    private static final String EVENT_PARAM_PAYMENT_HANDLER = "paymentHandler";
    private static final String EVENT_PARAM_PAYMENT_MODE = "Payment Mode";
    private static final String EVENT_PARAM_CURRENCY = "currency";
    private static final String EVENT_PARAM_DISCOUNTED_AMOUNT = "discountedAmount";
    private static final String EVENT_PARAM_TRANSACTION_AMOUNT = "transactionAmount";
    private static final String EVENT_PARAM_OTP_VERIFIED = "otpVerified";
    private static final String EVENT_PARAM_PHONE_NO = "phoneNo";
    private static final String EVENT_PARAM_SUBSCRIPTION_TYPE = "Subscription Type"; // (new/Auto-renewal/manual-renew)";
    private static final String EVENT_PARAM_COUPON_CODE = "Coupon Code";
    private static final String EVENT_PARAM_COUPON_VALUE = "Coupon Value";
    private static final String EVENT_PARAM_SUBSCRIPTION_START_DATE = "planStartDate";
    private static final String EVENT_PARAM_SUBSCRIPTION_END_DATE = "planEndDate";
    private static final String PURCHASE_TYPE = "PURCHASE_TYPE".toLowerCase();

    private static final String EVENT_PARAM_CONTENT_ID = "contentId";
    private static final String EVENT_PARAM_CONTENT_NAME = "contentName";
    private static final String EVENT_PARAM_CONTENT_DURATION = "contentDuration";
    private static final String EVENT_PARAM_SHOW_NAME = "showName";
    private static final String EVENT_PARAM_CONTENT_TYPE = "contentType";
    private static final String EVENT_PARAM_BIT_RATE = "bitRate";
    private static final String EVENT_PARAM_CONTENT_TITLE = "contentTitle";
    private static final String EVENT_PARAM_ACTOR_NAME = "actorName";
    private static final String EVENT_PARAM_NETWORK_TYPE = "networkType";
    private static final String EVENT_PARAM_DIRECTOR_NAME = "directorName";
    private static final String EVENT_PARAM_CONTENT_GENRE = "contentGenre";
    private static final String EVENT_PARAM_MUSIC_DIRECTOR_NAME = "musicDirectorName";
    private static final String EVENT_PARAM_SINGER_NAME = "singerName";
    private static final String EVENT_PARAM_PLAYBACK_TYPE = "playbackType";
    private static final String EVENT_PARAM_EPISODE_NUMBER = "Episode Number";
    private static final String EVENT_PARAM_SEASON_NUMBER = "Season Number";
    private static final String EVENT_PARAM_LOCATION = "Location";
    private static final String EVENT_PARAM_DEVICE = "device";
    private static final String EVENT_PARAM_OS_BROWSER = "OS/Browser";
    private static final String EVENT_PARAM_LAST_PAGE_NAME = "lastPageName";
    private static final String EVENT_PARAM_LAST_ACTIVITY_NAME = "Last Activity Name";
    private static final String EVENT_PARAM_PAGE_URL = "pageURL";
    private static final String EVENT_PARAM_LAST_ACTIVITY = "lastActivity";
    private static final String EVENT_PARAM_KEYWORD = "keyword";


    private static final String FILM_CATEGORY_EVENT_VALUE = "Category";
    private static final String FILM_ID_EVENT_VALUE = "Film ID";
    private static final String EVENT_FIRST_APP_OPEN = "First App Open";
    private static final String EVENT_ENGAGED_INSTALLER = "Engaged Installer";

    private static final String EVENT_REGISTRATION_COMPLETE = "registration complete";
    private static final String EVENT_SUBSCRIPTION_INITIATED = "Subscription Initiated";
    private static final String EVENT_SUBSCRIPTION_COMPLETED = "Subscription Completed";
    private static final String EVENT_SUBSCRIPTION_FAILED = "Subscription Failed";
    private static final String EVENT_PLAY_STARTED = "Play Started";
    private static final String EVENT_WATCHED = "Watched";
    private static final String EVENT_APP_DOWNLOAD = "App Download";
    private static final String EVENT_APP_UNINSTALLS = "App Uninstalls";
    private static final String EVENT_OFFLINE_DOWNLOAD = "Offline Download";
    private static final String EVENT_ADDED_TO_WATCHLIST = "Added To Watchlist";
    private static final String EVENT_REMOVED_FROM_WATCHLIST = "Removed From Watchlist";
    private static final String EVENT_PAGE_VIEWED = "Page Viewed";
    private static final String EVENT_VIEW_PLANS = "View Plans";
    private static final String EVENT_LOGOUT = "Logout";
    private static final String EVENT_SEARCHED = "Searched";
    private static final String EVENT_CAST = "Cast";
    private static final String EVENT_ADD_TO_CART = "Add to cart";
    private static final String TAG = AppsFlyerUtils.class.getSimpleName();

    public static void trackInstallationEvent(Application application) {
        AppsFlyerLib.getInstance().setAndroidIdData(getAndroidId(application));
    }

    @SuppressLint("HardwareIds")
    private static String getAndroidId(Context context) {
        String androidId;
        androidId = "" + android.provider.Settings.Secure.getString(context.getContentResolver(),
                android.provider.Settings.Secure.ANDROID_ID);
        return androidId;
    }

    public static void registrationEvent(AppCMSPresenter appCMSPresenter,
                                         Context context,
                                         String userID,
                                         String key) {
        Map<String, Object> eventValue = new HashMap<>();
        eventValue.put(USER_ENTITLEMENT_STATE_EVENT_VALUE, appCMSPresenter.isUserSubscribed());
        eventValue.put(USER_REGISTER_STATE_EVENT_VALUE, true);

        addUserDetails(eventValue, appCMSPresenter);

        if (appCMSPresenter.getAppPreference() != null)
            eventValue.put(EVENT_PARAM_REGISTRATION_TYPE, appCMSPresenter.getAppPreference().getRegistrationType());
        if(appCMSPresenter.getCurrentActivity() != null)
            eventValue.put(EVENT_PARAM_APP_VERSION, appCMSPresenter.getCurrentActivity().getString(R.string.app_cms_app_version));
        eventValue.put(EVENT_PARAM_PLATFORM, getPlatform(appCMSPresenter));
        eventValue.put(DEVICE_ID_EVENT_VALUE, getAndroidId(appCMSPresenter.getCurrentContext()));

        AppsFlyerLib.getInstance().setCustomerUserId(userID);
        AppsFlyerLib.getInstance().trackEvent(context, REGISTRATION_APP_EVENT_NAME, eventValue);
    }

    public static void appOpenEvent(Context context, AppCMSPresenter appCMSPresenter) {
        Map<String, Object> eventValue = new HashMap<>();
        if (appCMSPresenter.isUserLoggedIn()) {
            AppsFlyerLib.getInstance().setCustomerUserId(appCMSPresenter.getLoggedInUser());
            addUserDetails(eventValue, appCMSPresenter);
            eventValue.put(USER_ENTITLEMENT_STATE_EVENT_VALUE, appCMSPresenter.isUserSubscribed());
            eventValue.put(USER_REGISTER_STATE_EVENT_VALUE, appCMSPresenter.isUserLoggedIn());
            if(appCMSPresenter.getCurrentActivity() != null)
                eventValue.put(EVENT_PARAM_APP_VERSION, appCMSPresenter.getCurrentActivity().getString(R.string.app_cms_app_version));
            eventValue.put(EVENT_PARAM_PLATFORM, getPlatform(appCMSPresenter));
            eventValue.put(DEVICE_ID_EVENT_VALUE, getAndroidId(context));
        }

        AppsFlyerLib.getInstance().trackEvent(context, APP_OPEN_EVENT_NAME, eventValue);
    }

    public static void loginEvent(AppCMSPresenter appCMSPresenter) {
        Map<String, Object> eventValue = new HashMap<>();
        eventValue.put(USER_ENTITLEMENT_STATE_EVENT_VALUE, appCMSPresenter.isUserSubscribed());
        eventValue.put(USER_REGISTER_STATE_EVENT_VALUE, true);

        addUserDetails(eventValue, appCMSPresenter);

        if (appCMSPresenter.getAppPreference() != null)
            eventValue.put(EVENT_PARAM_REGISTRATION_TYPE, appCMSPresenter.getAppPreference().getRegistrationType());
        if(appCMSPresenter.getCurrentActivity() != null)
            eventValue.put(EVENT_PARAM_APP_VERSION, appCMSPresenter.getCurrentActivity().getString(R.string.app_cms_app_version));
        eventValue.put(EVENT_PARAM_PLATFORM, getPlatform(appCMSPresenter));
        eventValue.put(DEVICE_ID_EVENT_VALUE, getAndroidId(appCMSPresenter.getCurrentContext()));

        AppsFlyerLib.getInstance().setCustomerUserId(appCMSPresenter.getLoggedInUser());
        AppsFlyerLib.getInstance().trackEvent(appCMSPresenter.getCurrentContext(), LOGIN_EVENT_NAME, eventValue);
    }

    public static void logoutEvent(Context context, String userID) {
        Map<String, Object> eventValue = new HashMap<>();
        eventValue.put(USER_ENTITLEMENT_STATE_EVENT_VALUE, true);
        eventValue.put(USER_ID_EVENT_VALUE, userID);
        eventValue.put(USER_REGISTER_STATE_EVENT_VALUE, true);

        AppsFlyerLib.getInstance().setCustomerUserId(userID);
        AppsFlyerLib.getInstance().trackEvent(context, LOGOUT_EVENT_NAME, eventValue);
    }

    public static void subscriptionEvent(Context context,
                                         boolean isSubscribing,
                                         String deviceID,
                                         String price,
                                         String plan,
                                         String currency,
                                         String userID) {
        Map<String, Object> eventValue = new HashMap<>();

        eventValue.put(PRODUCT_NAME_EVENT_VALUE, plan);
        eventValue.put(PRICE_EVENT_VALUE, price);
        eventValue.put(USER_ENTITLEMENT_STATE_EVENT_VALUE, true);
        eventValue.put(DEVICE_ID_EVENT_VALUE, deviceID);
        eventValue.put(CURRENCY_EVENT_VALUE, currency);

        if (isSubscribing) {
            AppsFlyerLib.getInstance().setCustomerUserId(userID);
            AppsFlyerLib.getInstance().trackEvent(context, SUBSCRIPTION_EVENT_NAME, eventValue);

            /*
             * As per QA's request - Cancel Subscription isn't needed.
             * For now, It will be done on the Server side.
             */

//        } else {
//            AppsFlyerLib.getInstance().trackEvent(context, CANCEL_SUBSCRIPTION_EVENT_NAME, eventValue);
//            //Log.d("AppsFlyer__", "Cancel Sub Event");
        }
    }

    public static void purchaseEvent(Context context,
                                     String deviceID,
                                     String userID,
                                     String plan,
                                     String price,
                                     String currency,
                                     String contentId,
                                     String contentName,
                                     String type) {
        Map<String, Object> eventValue = new HashMap<>();
        eventValue.put(PRODUCT_NAME_EVENT_VALUE, plan);
        eventValue.put(PRICE_EVENT_VALUE, price);
        eventValue.put(DEVICE_ID_EVENT_VALUE, deviceID);
        eventValue.put(CURRENCY_EVENT_VALUE, currency);
        eventValue.put(EVENT_PARAM_CONTENT_ID, contentId);
        eventValue.put(EVENT_PARAM_CONTENT_NAME, contentName);
        eventValue.put(PURCHASE_TYPE, type);

        AppsFlyerLib.getInstance().setCustomerUserId(userID);
        AppsFlyerLib.getInstance().trackEvent(context, TVOD_PURCHASE, eventValue);
    }

    public static void purchaseCompleteEvent(Context context,
                                     String deviceID,
                                     String userID,
                                     String plan,
                                     String price,
                                     String currency,
                                     String contentId,
                                     String contentName,
                                     String type) {
        Map<String, Object> eventValue = new HashMap<>();
        eventValue.put(PRODUCT_NAME_EVENT_VALUE, plan);
        eventValue.put(PRICE_EVENT_VALUE, price);
        eventValue.put(DEVICE_ID_EVENT_VALUE, deviceID);
        eventValue.put(CURRENCY_EVENT_VALUE, currency);
        eventValue.put(EVENT_PARAM_CONTENT_ID, contentId);
        eventValue.put(EVENT_PARAM_CONTENT_NAME, contentName);
        eventValue.put(PURCHASE_TYPE, type);

        AppsFlyerLib.getInstance().setCustomerUserId(userID);
        AppsFlyerLib.getInstance().trackEvent(context, TVOD_PURCHASE_COMPLETE, eventValue);
    }

    public static void filmViewingEvent(Context context,
                                        String category,
                                        String filmId,
                                        AppCMSPresenter appCMSPresenter) {
        Map<String, Object> eventValue = new HashMap<>();

        if (!TextUtils.isEmpty(category)) {
            eventValue.put(FILM_CATEGORY_EVENT_VALUE, category);
        }
        eventValue.put(USER_ID_EVENT_VALUE, appCMSPresenter.getLoggedInUser());
        eventValue.put(FILM_ID_EVENT_VALUE, filmId);
        eventValue.put("true", true);
        eventValue.put(USER_ENTITLEMENT_STATE_EVENT_VALUE,
                !TextUtils.isEmpty(appCMSPresenter.getActiveSubscriptionId()));

        AppsFlyerLib.getInstance().setCustomerUserId(appCMSPresenter.getLoggedInUser());
        AppsFlyerLib.getInstance().trackEvent(context, FILM_VIEWING_EVENT_NAME, eventValue);
    }

    public static void firstAppOpen(Context context, String user) {
        Map<String, Object> eventValue = new HashMap<>();
        eventValue.put(USER_ID_EVENT_VALUE, user);
        eventValue.put(DEVICE_ID_EVENT_VALUE, getAndroidId(context));
        AppsFlyerLib.getInstance().setCustomerUserId(user);
        AppsFlyerLib.getInstance().trackEvent(context, EVENT_FIRST_APP_OPEN, eventValue);
    }

    public static void engageInstaller(Context context, String user) {
        Map<String, Object> eventValue = new HashMap<>();
        eventValue.put(USER_ID_EVENT_VALUE, user);
        eventValue.put(DEVICE_ID_EVENT_VALUE, getAndroidId(context));
        AppsFlyerLib.getInstance().setCustomerUserId(user);
        AppsFlyerLib.getInstance().trackEvent(context, EVENT_ENGAGED_INSTALLER, eventValue);
    }

    public static void setEventRegistrationComplete(AppCMSPresenter appCMSPresenter) {
        Map<String, Object> eventValue = new HashMap<>();
        eventValue.put(USER_ID_EVENT_VALUE, appCMSPresenter.getLoggedInUser());
        addUserDetails(eventValue, appCMSPresenter);
        if (appCMSPresenter.getAppPreference() != null)
            eventValue.put(EVENT_PARAM_REGISTRATION_TYPE, appCMSPresenter.getAppPreference().getRegistrationType());
        if(appCMSPresenter.getCurrentActivity() != null)
            eventValue.put(EVENT_PARAM_APP_VERSION, appCMSPresenter.getCurrentActivity().getString(R.string.app_cms_app_version));
        eventValue.put(EVENT_PARAM_PLATFORM, getPlatform(appCMSPresenter));

        AppsFlyerLib.getInstance().setCustomerUserId(appCMSPresenter.getLoggedInUser());
        AppsFlyerLib.getInstance().trackEvent(appCMSPresenter.getCurrentContext(), EVENT_REGISTRATION_COMPLETE, eventValue);
    }

    public static void setEventSubscriptionInitiated(AppCMSPresenter appCMSPresenter,
                                                     String paymentHandler,
                                                     double transactionAmount,
                                                     String country,
                                                     double discountPrice,
                                                     double planPrice,
                                                     String currency,
                                                     String planName,
                                                     double discountAmount,
                                                     String subscriptionEndDate) {
        Map<String, Object> eventValue = new HashMap<>();
        addUserDetails(eventValue, appCMSPresenter);
        eventValue.put(USER_ENTITLEMENT_STATE_EVENT_VALUE, appCMSPresenter.isUserSubscribed());
        eventValue.put(USER_REGISTER_STATE_EVENT_VALUE, true);
        if(appCMSPresenter.getCurrentActivity() != null)
            eventValue.put(EVENT_PARAM_APP_VERSION, appCMSPresenter.getCurrentActivity().getString(R.string.app_cms_app_version));
        eventValue.put(DEVICE_ID_EVENT_VALUE, getAndroidId(appCMSPresenter.getCurrentContext()));

        if (appCMSPresenter.getAppsFlyerConversionData() != null)
            eventValue.put(EVENT_PARAM_SOURCE, getCampaignSource(appCMSPresenter.getAppsFlyerConversionData()));

        eventValue.put(EVENT_PARAM_AMOUNT, planPrice);
        if (appCMSPresenter.getAppPreference() != null)
            eventValue.put(EVENT_PARAM_PAYMENT_PLAN, !TextUtils.isEmpty(appCMSPresenter.getAppPreference().getActiveSubscriptionId()) ? appCMSPresenter.getAppPreference().getActiveSubscriptionId() : planName);
        else {
            eventValue.put(EVENT_PARAM_PAYMENT_PLAN, planName);
        }
        eventValue.put(EVENT_PARAM_COUNTRY, country);

        eventValue.put(EVENT_PARAM_DISCOUNT_AMOUNT, discountAmount);
        eventValue.put(EVENT_PARAM_PLATFORM, getPlatform(appCMSPresenter));
        eventValue.put(EVENT_PARAM_PAYMENT_HANDLER, paymentHandler);
        if (appCMSPresenter.getUserSubscriptionInfo() != null && !TextUtils.isEmpty(appCMSPresenter.getUserSubscriptionInfo().getBrand()))
            eventValue.put(EVENT_PARAM_PAYMENT_MODE, appCMSPresenter.getUserSubscriptionInfo().getBrand());

        eventValue.put(EVENT_PARAM_CURRENCY, currency);

        eventValue.put(EVENT_PARAM_DISCOUNTED_AMOUNT, discountPrice);
        eventValue.put(EVENT_PARAM_TRANSACTION_AMOUNT, transactionAmount);

        // eventValue.put(EVENT_PARAM_OTP_VERIFIED, appCMSPresenter.getAppPreference().getLoginType().equalsIgnoreCase(currentActivity.getString(R.string.login_type_otp)));
        if (appCMSPresenter.getAppPreference() != null) {
            eventValue.put(EVENT_PARAM_PHONE_NO, appCMSPresenter.getAppPreference().getLoggedInUserPhone());
            eventValue.put(EVENT_PARAM_COUPON_CODE, appCMSPresenter.getAppPreference().getCoupanCode());
        }

        eventValue.put(EVENT_PARAM_SUBSCRIPTION_TYPE, planName);
        eventValue.put(EVENT_PARAM_COUPON_VALUE, "");

        eventValue.put(EVENT_PARAM_SUBSCRIPTION_START_DATE, new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(Calendar.getInstance().getTime()));
        eventValue.put(EVENT_PARAM_SUBSCRIPTION_END_DATE, subscriptionEndDate);

        AppsFlyerLib.getInstance().setCustomerUserId(appCMSPresenter.getLoggedInUser());
        AppsFlyerLib.getInstance().trackEvent(appCMSPresenter.getCurrentContext(), EVENT_SUBSCRIPTION_INITIATED, eventValue);
    }

    public static void setEventSubscriptionCompleted(AppCMSPresenter appCMSPresenter,
                                                     String paymentHandler,
                                                     double transactionAmount,
                                                     String country,
                                                     double discountPrice,
                                                     double planPrice,
                                                     String currency,
                                                     String planName,
                                                     double discountAmount,
                                                     String subscriptionEndDate) {
        Map<String, Object> eventValue = new HashMap<>();
        addUserDetails(eventValue, appCMSPresenter);

        eventValue.put(USER_ENTITLEMENT_STATE_EVENT_VALUE, appCMSPresenter.isUserSubscribed());
        eventValue.put(USER_REGISTER_STATE_EVENT_VALUE, true);
        if(appCMSPresenter.getCurrentActivity() != null)
            eventValue.put(EVENT_PARAM_APP_VERSION, appCMSPresenter.getCurrentActivity().getString(R.string.app_cms_app_version));
        eventValue.put(DEVICE_ID_EVENT_VALUE, getAndroidId(appCMSPresenter.getCurrentContext()));

        if (appCMSPresenter.getAppsFlyerConversionData() != null)
            eventValue.put(EVENT_PARAM_SOURCE, getCampaignSource(appCMSPresenter.getAppsFlyerConversionData()));

        eventValue.put(EVENT_PARAM_AMOUNT, planPrice);
        if (appCMSPresenter.getAppPreference() != null) {
            eventValue.put(EVENT_PARAM_PAYMENT_PLAN, !TextUtils.isEmpty(appCMSPresenter.getAppPreference().getActiveSubscriptionId()) ? appCMSPresenter.getAppPreference().getActiveSubscriptionId() : planName);
            eventValue.put(EVENT_PARAM_PHONE_NO, appCMSPresenter.getAppPreference().getLoggedInUserPhone());
            eventValue.put(EVENT_PARAM_COUPON_CODE, appCMSPresenter.getAppPreference().getCoupanCode());
        } else {
            eventValue.put(EVENT_PARAM_PAYMENT_PLAN, planName);
        }
        eventValue.put(EVENT_PARAM_COUNTRY, country);

        eventValue.put(EVENT_PARAM_DISCOUNT_AMOUNT, discountAmount);
        eventValue.put(EVENT_PARAM_PLATFORM, getPlatform(appCMSPresenter));
        eventValue.put(EVENT_PARAM_PAYMENT_HANDLER, paymentHandler);
        if (appCMSPresenter.getUserSubscriptionInfo() != null && !TextUtils.isEmpty(appCMSPresenter.getUserSubscriptionInfo().getBrand()))
            eventValue.put(EVENT_PARAM_PAYMENT_MODE, appCMSPresenter.getUserSubscriptionInfo().getBrand());

        eventValue.put(EVENT_PARAM_CURRENCY, currency);

        eventValue.put(EVENT_PARAM_DISCOUNTED_AMOUNT, discountPrice);
        eventValue.put(EVENT_PARAM_TRANSACTION_AMOUNT, transactionAmount);

        // eventValue.put(EVENT_PARAM_OTP_VERIFIED, appCMSPresenter.getAppPreference().getLoginType().equalsIgnoreCase(currentActivity.getString(R.string.login_type_otp)));

        eventValue.put(EVENT_PARAM_SUBSCRIPTION_TYPE, planName);
        eventValue.put(EVENT_PARAM_COUPON_VALUE, "");

        eventValue.put(EVENT_PARAM_SUBSCRIPTION_START_DATE, new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(Calendar.getInstance().getTime()));
        eventValue.put(EVENT_PARAM_SUBSCRIPTION_END_DATE, subscriptionEndDate);

        AppsFlyerLib.getInstance().setCustomerUserId(appCMSPresenter.getLoggedInUser());
        AppsFlyerLib.getInstance().trackEvent(appCMSPresenter.getCurrentContext(), EVENT_SUBSCRIPTION_COMPLETED, eventValue);
    }

    public static void setEventSubscriptionFailed(AppCMSPresenter appCMSPresenter,
                                                  String paymentHandler,
                                                  double transactionAmount,
                                                  String country,
                                                  double discountPrice,
                                                  double planPrice,
                                                  String currency,
                                                  String planName,
                                                  double discountAmount,
                                                  String subscriptionEndDate) {
        Map<String, Object> eventValue = new HashMap<>();
        addUserDetails(eventValue, appCMSPresenter);

        eventValue.put(USER_ENTITLEMENT_STATE_EVENT_VALUE, appCMSPresenter.isUserSubscribed());
        eventValue.put(USER_REGISTER_STATE_EVENT_VALUE, true);
        if(appCMSPresenter.getCurrentActivity() != null)
            eventValue.put(EVENT_PARAM_APP_VERSION, appCMSPresenter.getCurrentActivity().getString(R.string.app_cms_app_version));
        eventValue.put(DEVICE_ID_EVENT_VALUE, getAndroidId(appCMSPresenter.getCurrentContext()));

        if (appCMSPresenter.getAppsFlyerConversionData() != null)
            eventValue.put(EVENT_PARAM_SOURCE, getCampaignSource(appCMSPresenter.getAppsFlyerConversionData()));

        eventValue.put(EVENT_PARAM_AMOUNT, planPrice);
        if (appCMSPresenter.getAppPreference() != null) {
            eventValue.put(EVENT_PARAM_PAYMENT_PLAN, !TextUtils.isEmpty(appCMSPresenter.getAppPreference().getActiveSubscriptionId()) ? appCMSPresenter.getAppPreference().getActiveSubscriptionId() : planName);
            eventValue.put(EVENT_PARAM_PHONE_NO, appCMSPresenter.getAppPreference().getLoggedInUserPhone());
            eventValue.put(EVENT_PARAM_COUPON_CODE, appCMSPresenter.getAppPreference().getCoupanCode());
        } else {
            eventValue.put(EVENT_PARAM_PAYMENT_PLAN, planName);
        }
        eventValue.put(EVENT_PARAM_COUNTRY, country);

        eventValue.put(EVENT_PARAM_DISCOUNT_AMOUNT, discountAmount);
        eventValue.put(EVENT_PARAM_PLATFORM, getPlatform(appCMSPresenter));
        eventValue.put(EVENT_PARAM_PAYMENT_HANDLER, paymentHandler);
        if (appCMSPresenter.getUserSubscriptionInfo() != null && !TextUtils.isEmpty(appCMSPresenter.getUserSubscriptionInfo().getBrand()))
            eventValue.put(EVENT_PARAM_PAYMENT_MODE, appCMSPresenter.getUserSubscriptionInfo().getBrand());

        eventValue.put(EVENT_PARAM_CURRENCY, currency);

        eventValue.put(EVENT_PARAM_DISCOUNTED_AMOUNT, discountPrice);
        eventValue.put(EVENT_PARAM_TRANSACTION_AMOUNT, transactionAmount);

        // eventValue.put(EVENT_PARAM_OTP_VERIFIED, appCMSPresenter.getAppPreference().getLoginType().equalsIgnoreCase(currentActivity.getString(R.string.login_type_otp)));

        eventValue.put(EVENT_PARAM_SUBSCRIPTION_TYPE, planName);
        eventValue.put(EVENT_PARAM_COUPON_VALUE, "");

        eventValue.put(EVENT_PARAM_SUBSCRIPTION_START_DATE, new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(Calendar.getInstance().getTime()));
        eventValue.put(EVENT_PARAM_SUBSCRIPTION_END_DATE, subscriptionEndDate);

        AppsFlyerLib.getInstance().setCustomerUserId(appCMSPresenter.getLoggedInUser());
        AppsFlyerLib.getInstance().trackEvent(appCMSPresenter.getCurrentContext(), EVENT_SUBSCRIPTION_FAILED, eventValue);
    }

    public static void setEventPlayStarted(AppCMSPresenter appCMSPresenter,
                                           ContentDatum contentDatum) {
        if (appCMSPresenter != null && contentDatum != null && contentDatum.getGist() != null) {
            Map<String, Object> eventValue = new HashMap<>();
            addUserDetails(eventValue, appCMSPresenter);

            if (appCMSPresenter.getAppPreference() != null) {
                eventValue.put(EVENT_PARAM_BIT_RATE, appCMSPresenter.getAppPreference().getUserDownloadQualityPref());
            }

            eventValue.put(EVENT_PARAM_CONTENT_ID, contentDatum.getGist().getId());
            eventValue.put(EVENT_PARAM_CONTENT_DURATION, (contentDatum.getGist().getRuntime() / 60));
            eventValue.put(EVENT_PARAM_SHOW_NAME, contentDatum.getGist().getSeriesTitle());
            eventValue.put(EVENT_PARAM_PLATFORM, getPlatform(appCMSPresenter));
            eventValue.put(EVENT_PARAM_CONTENT_TYPE, contentDatum.getContentType());
            eventValue.put(EVENT_PARAM_CONTENT_TITLE, contentDatum.getGist().getSeriesTitle());
            eventValue.put(EVENT_PARAM_ACTOR_NAME, contentDatum.getGist().getArtistName());
            eventValue.put(EVENT_PARAM_NETWORK_TYPE, Utils.getNetworkType(appCMSPresenter.getCurrentContext()));
            eventValue.put(EVENT_PARAM_DIRECTOR_NAME, contentDatum.getGist().getDirectorName());
            eventValue.put(EVENT_PARAM_CONTENT_GENRE, contentDatum.getGist().getGenre());
            eventValue.put(EVENT_PARAM_MUSIC_DIRECTOR_NAME, getDirectorName(appCMSPresenter, contentDatum.getCreditBlocks()));
            eventValue.put(EVENT_PARAM_SINGER_NAME, appCMSPresenter.getArtistNameFromCreditBlocks(contentDatum.getCreditBlocks()));
            eventValue.put(EVENT_PARAM_PLAYBACK_TYPE, contentDatum.getGist().getMediaType());
            eventValue.put(EVENT_PARAM_EPISODE_NUMBER, contentDatum.getSeasonEpisodeNum());
            eventValue.put(EVENT_PARAM_SEASON_NUMBER, contentDatum.getSeasonId());

            AppsFlyerLib.getInstance().setCustomerUserId(appCMSPresenter.getLoggedInUser());
            AppsFlyerLib.getInstance().trackEvent(appCMSPresenter.getCurrentContext(), EVENT_PLAY_STARTED, eventValue);
        }
    }

    public static void setEventWatched(AppCMSPresenter appCMSPresenter,
                                       ContentDatum contentDatum) {
        if (appCMSPresenter != null && contentDatum != null && contentDatum.getGist() != null) {
            Map<String, Object> eventValue = new HashMap<>();
            addUserDetails(eventValue, appCMSPresenter);

            eventValue.put(EVENT_PARAM_CONTENT_ID, contentDatum.getGist().getId());
            eventValue.put(EVENT_PARAM_CONTENT_DURATION, (contentDatum.getGist().getRuntime() / 60));
            eventValue.put(EVENT_PARAM_SHOW_NAME, contentDatum.getGist().getTitle());
            eventValue.put(EVENT_PARAM_PLATFORM, getPlatform(appCMSPresenter));
            eventValue.put(EVENT_PARAM_CONTENT_TYPE, contentDatum.getContentType());
            if (appCMSPresenter.getAppPreference() != null)
                eventValue.put(EVENT_PARAM_BIT_RATE, appCMSPresenter.getAppPreference().getUserDownloadQualityPref());
            eventValue.put(EVENT_PARAM_CONTENT_TITLE, contentDatum.getGist().getTitle());
            eventValue.put(EVENT_PARAM_ACTOR_NAME, contentDatum.getGist().getArtistName());
            eventValue.put(EVENT_PARAM_NETWORK_TYPE, Utils.getNetworkType(appCMSPresenter.getCurrentContext()));
            eventValue.put(EVENT_PARAM_DIRECTOR_NAME, contentDatum.getGist().getDirectorName());
            eventValue.put(EVENT_PARAM_CONTENT_GENRE, contentDatum.getGist().getGenre());
            eventValue.put(EVENT_PARAM_MUSIC_DIRECTOR_NAME, getDirectorName(appCMSPresenter, contentDatum.getCreditBlocks()));
            eventValue.put(EVENT_PARAM_SINGER_NAME, appCMSPresenter.getArtistNameFromCreditBlocks(contentDatum.getCreditBlocks()));
            eventValue.put(EVENT_PARAM_PLAYBACK_TYPE, contentDatum.getGist().getMediaType());
            eventValue.put(EVENT_PARAM_EPISODE_NUMBER, contentDatum.getSeasonEpisodeNum());
            eventValue.put(EVENT_PARAM_SEASON_NUMBER, contentDatum.getSeasonId());

            AppsFlyerLib.getInstance().setCustomerUserId(appCMSPresenter.getLoggedInUser());
            AppsFlyerLib.getInstance().trackEvent(appCMSPresenter.getCurrentContext(), EVENT_WATCHED, eventValue);
        }
    }

    public static void setEventAppDownload(Context context, String user) {
        Map<String, Object> eventValue = new HashMap<>();
        eventValue.put(USER_ID_EVENT_VALUE, user);
        eventValue.put(DEVICE_ID_EVENT_VALUE, getAndroidId(context));
        AppsFlyerLib.getInstance().setCustomerUserId(user);
        AppsFlyerLib.getInstance().trackEvent(context, EVENT_APP_DOWNLOAD, eventValue);
    }

    public static void setEventAppUninstalls(Context context, String user) {
        Map<String, Object> eventValue = new HashMap<>();
        eventValue.put(USER_ID_EVENT_VALUE, user);
        eventValue.put(DEVICE_ID_EVENT_VALUE, getAndroidId(context));
        AppsFlyerLib.getInstance().setCustomerUserId(user);
        AppsFlyerLib.getInstance().trackEvent(context, EVENT_APP_UNINSTALLS, eventValue);
    }

    public static void setEventOfflineDownload(AppCMSPresenter appCMSPresenter,
                                               ContentDatum contentDatum) {
        if (appCMSPresenter != null && contentDatum != null && contentDatum.getGist() != null) {
            Map<String, Object> eventValue = new HashMap<>();
            addUserDetails(eventValue, appCMSPresenter);

            eventValue.put(EVENT_PARAM_CONTENT_ID, contentDatum.getGist().getId());
            eventValue.put(EVENT_PARAM_CONTENT_DURATION, (contentDatum.getGist().getRuntime() / 60));
            eventValue.put(EVENT_PARAM_SHOW_NAME, contentDatum.getGist().getTitle());
            eventValue.put(EVENT_PARAM_PLATFORM, getPlatform(appCMSPresenter));
            eventValue.put(EVENT_PARAM_CONTENT_TYPE, contentDatum.getContentType());
            if (appCMSPresenter.getAppPreference() != null)
                eventValue.put(EVENT_PARAM_BIT_RATE, appCMSPresenter.getAppPreference().getUserDownloadQualityPref());
            eventValue.put(EVENT_PARAM_CONTENT_TITLE, contentDatum.getGist().getTitle());
            eventValue.put(EVENT_PARAM_ACTOR_NAME, contentDatum.getGist().getArtistName());
            eventValue.put(EVENT_PARAM_NETWORK_TYPE, Utils.getNetworkType(appCMSPresenter.getCurrentContext()));
            eventValue.put(EVENT_PARAM_DIRECTOR_NAME, contentDatum.getGist().getDirectorName());
            eventValue.put(EVENT_PARAM_CONTENT_GENRE, contentDatum.getGist().getGenre());
            eventValue.put(EVENT_PARAM_MUSIC_DIRECTOR_NAME, getDirectorName(appCMSPresenter, contentDatum.getCreditBlocks()));
            eventValue.put(EVENT_PARAM_SINGER_NAME, appCMSPresenter.getArtistNameFromCreditBlocks(contentDatum.getCreditBlocks()));
            eventValue.put(EVENT_PARAM_PLAYBACK_TYPE, contentDatum.getGist().getMediaType());
            eventValue.put(EVENT_PARAM_EPISODE_NUMBER, contentDatum.getSeasonEpisodeNum());
            eventValue.put(EVENT_PARAM_SEASON_NUMBER, contentDatum.getSeasonId());


            AppsFlyerLib.getInstance().setCustomerUserId(appCMSPresenter.getLoggedInUser());
            AppsFlyerLib.getInstance().trackEvent(appCMSPresenter.getCurrentContext(), EVENT_OFFLINE_DOWNLOAD, eventValue);
        }
    }

    public static void setEventAddToWatchList(AppCMSPresenter appCMSPresenter,
                                              ContentDatum contentDatum) {
        if (appCMSPresenter != null && contentDatum != null && contentDatum.getGist() != null) {
            Map<String, Object> eventValue = new HashMap<>();
            addUserDetails(eventValue, appCMSPresenter);
            if(appCMSPresenter.getCurrentActivity() != null)
                eventValue.put(EVENT_PARAM_APP_VERSION, appCMSPresenter.getCurrentActivity().getString(R.string.app_cms_app_version));
            eventValue.put(DEVICE_ID_EVENT_VALUE, getAndroidId(appCMSPresenter.getCurrentContext()));

            eventValue.put(EVENT_PARAM_CONTENT_ID, contentDatum.getGist().getId());
            eventValue.put(EVENT_PARAM_CONTENT_DURATION, (contentDatum.getGist().getRuntime() / 60));
            eventValue.put(EVENT_PARAM_SHOW_NAME, contentDatum.getGist().getTitle());
            eventValue.put(EVENT_PARAM_PLATFORM, getPlatform(appCMSPresenter));
            eventValue.put(EVENT_PARAM_CONTENT_TYPE, contentDatum.getContentType());
            if (appCMSPresenter.getAppPreference() != null)
                eventValue.put(EVENT_PARAM_BIT_RATE, appCMSPresenter.getAppPreference().getUserDownloadQualityPref());
            eventValue.put(EVENT_PARAM_CONTENT_TITLE, contentDatum.getGist().getTitle());
            eventValue.put(EVENT_PARAM_ACTOR_NAME, contentDatum.getGist().getArtistName());
            eventValue.put(EVENT_PARAM_NETWORK_TYPE, Utils.getNetworkType(appCMSPresenter.getCurrentContext()));
            eventValue.put(EVENT_PARAM_DIRECTOR_NAME, contentDatum.getGist().getDirectorName());
            eventValue.put(EVENT_PARAM_CONTENT_GENRE, contentDatum.getGist().getGenre());
            eventValue.put(EVENT_PARAM_MUSIC_DIRECTOR_NAME, getDirectorName(appCMSPresenter, contentDatum.getCreditBlocks()));
            eventValue.put(EVENT_PARAM_SINGER_NAME, appCMSPresenter.getArtistNameFromCreditBlocks(contentDatum.getCreditBlocks()));
            eventValue.put(EVENT_PARAM_PLAYBACK_TYPE, contentDatum.getGist().getMediaType());
            eventValue.put(EVENT_PARAM_EPISODE_NUMBER, contentDatum.getSeasonEpisodeNum());
            eventValue.put(EVENT_PARAM_SEASON_NUMBER, contentDatum.getSeasonId());

            AppsFlyerLib.getInstance().setCustomerUserId(appCMSPresenter.getLoggedInUser());
            AppsFlyerLib.getInstance().trackEvent(appCMSPresenter.getCurrentContext(), EVENT_ADDED_TO_WATCHLIST, eventValue);
        }
    }

    public static void setEventRemoveFromWatchList(AppCMSPresenter appCMSPresenter,
                                                   ContentDatum contentDatum) {
        if (appCMSPresenter != null && contentDatum != null && contentDatum.getGist() != null) {
            Map<String, Object> eventValue = new HashMap<>();
            addUserDetails(eventValue, appCMSPresenter);
            if(appCMSPresenter.getCurrentActivity() != null)
                eventValue.put(EVENT_PARAM_APP_VERSION, appCMSPresenter.getCurrentActivity().getString(R.string.app_cms_app_version));
            eventValue.put(DEVICE_ID_EVENT_VALUE, getAndroidId(appCMSPresenter.getCurrentContext()));

            eventValue.put(EVENT_PARAM_CONTENT_ID, contentDatum.getGist().getId());
            eventValue.put(EVENT_PARAM_CONTENT_DURATION, (contentDatum.getGist().getRuntime() / 60));
            eventValue.put(EVENT_PARAM_SHOW_NAME, contentDatum.getGist().getTitle());
            eventValue.put(EVENT_PARAM_PLATFORM, getPlatform(appCMSPresenter));
            eventValue.put(EVENT_PARAM_CONTENT_TYPE, contentDatum.getContentType());
            if (appCMSPresenter.getAppPreference() != null)
                eventValue.put(EVENT_PARAM_BIT_RATE, appCMSPresenter.getAppPreference().getUserDownloadQualityPref());
            eventValue.put(EVENT_PARAM_CONTENT_TITLE, contentDatum.getGist().getTitle());
            eventValue.put(EVENT_PARAM_ACTOR_NAME, contentDatum.getGist().getArtistName());
            eventValue.put(EVENT_PARAM_NETWORK_TYPE, Utils.getNetworkType(appCMSPresenter.getCurrentContext()));
            eventValue.put(EVENT_PARAM_DIRECTOR_NAME, contentDatum.getGist().getDirectorName());
            eventValue.put(EVENT_PARAM_CONTENT_GENRE, contentDatum.getGist().getGenre());
            eventValue.put(EVENT_PARAM_MUSIC_DIRECTOR_NAME, getDirectorName(appCMSPresenter, contentDatum.getCreditBlocks()));
            eventValue.put(EVENT_PARAM_SINGER_NAME, appCMSPresenter.getArtistNameFromCreditBlocks(contentDatum.getCreditBlocks()));
            eventValue.put(EVENT_PARAM_PLAYBACK_TYPE, contentDatum.getGist().getMediaType());
            eventValue.put(EVENT_PARAM_EPISODE_NUMBER, contentDatum.getSeasonEpisodeNum());
            eventValue.put(EVENT_PARAM_SEASON_NUMBER, contentDatum.getSeasonId());


            AppsFlyerLib.getInstance().setCustomerUserId(appCMSPresenter.getLoggedInUser());
            AppsFlyerLib.getInstance().trackEvent(appCMSPresenter.getCurrentContext(), EVENT_REMOVED_FROM_WATCHLIST, eventValue);
        }
    }

    public static void setEventPageViewed(AppCMSPresenter appCMSPresenter,
                                          String lastPage,
                                          String pageName,
                                          String appVersion,
                                          String pageURL) {
        if (appCMSPresenter.getCurrentActivity() != null) {
            Map<String, Object> eventValue = new HashMap<>();
            addUserDetails(eventValue, appCMSPresenter);
            eventValue.put(EVENT_PARAM_LAST_PAGE_NAME, lastPage);
            eventValue.put(EVENT_PARAM_PLATFORM, getPlatform(appCMSPresenter));
            eventValue.put(EVENT_PARAM_APP_VERSION, appCMSPresenter.getCurrentActivity().getString(R.string.app_cms_app_version));
            eventValue.put(EVENT_PARAM_LAST_ACTIVITY_NAME, lastPage);
            eventValue.put(DEVICE_ID_EVENT_VALUE, getAndroidId(appCMSPresenter.getCurrentContext()));

            if (appCMSPresenter.getCurrentActivity() instanceof AppCMSPageActivity) {
                MetaPage metaPage = appCMSPresenter.getMetaPage(((AppCMSPageActivity) appCMSPresenter.getCurrentActivity()).getCurrentPageId());
                if (metaPage != null) {
                    eventValue.put(EVENT_PARAM_PAGE_URL, metaPage.getPageAPI());
                }
            } else {
                eventValue.put(EVENT_PARAM_PAGE_URL, pageURL);
            }
            eventValue.put(EVENT_PARAM_LAST_ACTIVITY, lastPage);
            if (appCMSPresenter.getAppsFlyerConversionData() != null)
                eventValue.put(EVENT_PARAM_SOURCE, getCampaignSource(appCMSPresenter.getAppsFlyerConversionData()));


            AppsFlyerLib.getInstance().trackEvent(appCMSPresenter.getCurrentContext(), EVENT_PAGE_VIEWED, eventValue);
        }
    }

    public static void setEventViewPlans(AppCMSPresenter appCMSPresenter) {
        Map<String, Object> eventValue = new HashMap<>();
        addUserDetails(eventValue, appCMSPresenter);
        if(appCMSPresenter.getCurrentActivity() != null)
            eventValue.put(EVENT_PARAM_APP_VERSION, appCMSPresenter.getCurrentActivity().getString(R.string.app_cms_app_version));
        eventValue.put(EVENT_PARAM_PLATFORM, getPlatform(appCMSPresenter));
        eventValue.put(DEVICE_ID_EVENT_VALUE, getAndroidId(appCMSPresenter.getCurrentContext()));
        if (appCMSPresenter.getAppsFlyerConversionData() != null)
            eventValue.put(EVENT_PARAM_SOURCE, getCampaignSource(appCMSPresenter.getAppsFlyerConversionData()));
        AppsFlyerLib.getInstance().trackEvent(appCMSPresenter.getCurrentContext(), EVENT_VIEW_PLANS, eventValue);
    }

    public static void setEventLogout(AppCMSPresenter appCMSPresenter) {
        Map<String, Object> eventValue = new HashMap<>();
        addUserDetails(eventValue, appCMSPresenter);
        eventValue.put(USER_ENTITLEMENT_STATE_EVENT_VALUE, appCMSPresenter.isUserSubscribed());
        eventValue.put(USER_REGISTER_STATE_EVENT_VALUE, appCMSPresenter.isUserLoggedIn());
        if(appCMSPresenter.getCurrentActivity() != null)
            eventValue.put(EVENT_PARAM_APP_VERSION, appCMSPresenter.getCurrentActivity().getString(R.string.app_cms_app_version));
        eventValue.put(EVENT_PARAM_PLATFORM, getPlatform(appCMSPresenter));
        eventValue.put(DEVICE_ID_EVENT_VALUE, getAndroidId(appCMSPresenter.getCurrentContext()));

        AppsFlyerLib.getInstance().setCustomerUserId(appCMSPresenter.getLoggedInUser());
        AppsFlyerLib.getInstance().trackEvent(appCMSPresenter.getCurrentContext(), EVENT_LOGOUT, eventValue);
    }

    public static void setEventSearched(AppCMSPresenter appCMSPresenter, String keyword) {
        Map<String, Object> eventValue = new HashMap<>();
        addUserDetails(eventValue, appCMSPresenter);
        if(appCMSPresenter.getCurrentActivity() != null)
            eventValue.put(EVENT_PARAM_APP_VERSION, appCMSPresenter.getCurrentActivity().getString(R.string.app_cms_app_version));
        eventValue.put(EVENT_PARAM_PLATFORM, getPlatform(appCMSPresenter));
        eventValue.put(DEVICE_ID_EVENT_VALUE, getAndroidId(appCMSPresenter.getCurrentContext()));
        eventValue.put(EVENT_PARAM_KEYWORD, keyword);

        AppsFlyerLib.getInstance().setCustomerUserId(appCMSPresenter.getLoggedInUser());
        AppsFlyerLib.getInstance().trackEvent(appCMSPresenter.getCurrentContext(), EVENT_SEARCHED, eventValue);
    }

    public static void setEventCast(AppCMSPresenter appCMSPresenter,
                                    ContentDatum contentDatum) {
        if (appCMSPresenter != null && contentDatum != null && contentDatum.getGist() != null) {
            Map<String, Object> eventValue = new HashMap<>();
            addUserDetails(eventValue, appCMSPresenter);

            eventValue.put(EVENT_PARAM_CONTENT_ID, contentDatum.getGist().getId());
            eventValue.put(EVENT_PARAM_CONTENT_DURATION, (contentDatum.getGist().getRuntime() / 60));
            eventValue.put(EVENT_PARAM_SHOW_NAME, contentDatum.getGist().getTitle());
            eventValue.put(EVENT_PARAM_PLATFORM, getPlatform(appCMSPresenter));
            eventValue.put(EVENT_PARAM_CONTENT_TYPE, contentDatum.getContentType());
            if (appCMSPresenter.getAppPreference() != null)
                eventValue.put(EVENT_PARAM_BIT_RATE, appCMSPresenter.getAppPreference().getUserDownloadQualityPref());
            eventValue.put(EVENT_PARAM_CONTENT_TITLE, contentDatum.getGist().getTitle());
            eventValue.put(EVENT_PARAM_ACTOR_NAME, contentDatum.getGist().getArtistName());
            eventValue.put(EVENT_PARAM_NETWORK_TYPE, Utils.getNetworkType(appCMSPresenter.getCurrentContext()));
            eventValue.put(EVENT_PARAM_DIRECTOR_NAME, getDirectorName(appCMSPresenter, contentDatum.getCreditBlocks()));
            eventValue.put(EVENT_PARAM_CONTENT_GENRE, contentDatum.getGist().getGenre());
            eventValue.put(EVENT_PARAM_MUSIC_DIRECTOR_NAME, getDirectorName(appCMSPresenter, contentDatum.getCreditBlocks()));
            eventValue.put(EVENT_PARAM_SINGER_NAME, appCMSPresenter.getArtistNameFromCreditBlocks(contentDatum.getCreditBlocks()));
            eventValue.put(EVENT_PARAM_PLAYBACK_TYPE, contentDatum.getGist().getMediaType());
            eventValue.put(EVENT_PARAM_EPISODE_NUMBER, contentDatum.getSeasonEpisodeNum());
            eventValue.put(EVENT_PARAM_SEASON_NUMBER, contentDatum.getSeasonId());

            AppsFlyerLib.getInstance().setCustomerUserId(appCMSPresenter.getLoggedInUser());
            AppsFlyerLib.getInstance().trackEvent(appCMSPresenter.getCurrentContext(), EVENT_CAST, eventValue);
        }
    }

    public static void setEventAddToCart(AppCMSPresenter appCMSPresenter,
                                         String paymentHandler,
                                         double transactionAmount,
                                         String country,
                                         double discountPrice,
                                         double planPrice,
                                         String currency,
                                         String planName,
                                         double discountAmount,
                                         String subscriptionEndDate) {
        Map<String, Object> eventValue = new HashMap<>();
        addUserDetails(eventValue, appCMSPresenter);
        eventValue.put(USER_ENTITLEMENT_STATE_EVENT_VALUE, appCMSPresenter.isUserSubscribed());
        eventValue.put(USER_REGISTER_STATE_EVENT_VALUE, true);
        if(appCMSPresenter.getCurrentActivity() != null)
            eventValue.put(EVENT_PARAM_APP_VERSION, appCMSPresenter.getCurrentActivity().getString(R.string.app_cms_app_version));
        eventValue.put(DEVICE_ID_EVENT_VALUE, getAndroidId(appCMSPresenter.getCurrentContext()));

        if (appCMSPresenter.getAppsFlyerConversionData() != null)
            eventValue.put(EVENT_PARAM_SOURCE, getCampaignSource(appCMSPresenter.getAppsFlyerConversionData()));

        eventValue.put(EVENT_PARAM_AMOUNT, planPrice);
        if (appCMSPresenter.getAppPreference() != null)
            eventValue.put(EVENT_PARAM_PAYMENT_PLAN, !TextUtils.isEmpty(appCMSPresenter.getAppPreference().getActiveSubscriptionId()) ? appCMSPresenter.getAppPreference().getActiveSubscriptionId() : planName);
        else {
            eventValue.put(EVENT_PARAM_PAYMENT_PLAN, planName);
        }
        eventValue.put(EVENT_PARAM_COUNTRY, country);

        eventValue.put(EVENT_PARAM_DISCOUNT_AMOUNT, discountAmount);
        eventValue.put(EVENT_PARAM_PLATFORM, getPlatform(appCMSPresenter));
        eventValue.put(EVENT_PARAM_PAYMENT_HANDLER, paymentHandler);
        if (appCMSPresenter.getUserSubscriptionInfo() != null && !TextUtils.isEmpty(appCMSPresenter.getUserSubscriptionInfo().getBrand()))
            eventValue.put(EVENT_PARAM_PAYMENT_MODE, appCMSPresenter.getUserSubscriptionInfo().getBrand());

        eventValue.put(EVENT_PARAM_CURRENCY, currency);

        eventValue.put(EVENT_PARAM_DISCOUNTED_AMOUNT, discountPrice);
        eventValue.put(EVENT_PARAM_TRANSACTION_AMOUNT, transactionAmount);

        // eventValue.put(EVENT_PARAM_OTP_VERIFIED, appCMSPresenter.getAppPreference().getLoginType().equalsIgnoreCase(currentActivity.getString(R.string.login_type_otp)));
        if (appCMSPresenter.getAppPreference() != null) {
            eventValue.put(EVENT_PARAM_PHONE_NO, appCMSPresenter.getAppPreference().getLoggedInUserPhone());
            eventValue.put(EVENT_PARAM_COUPON_CODE, appCMSPresenter.getAppPreference().getCoupanCode());
        }

        eventValue.put(EVENT_PARAM_SUBSCRIPTION_TYPE, planName);
        eventValue.put(EVENT_PARAM_COUPON_VALUE, "");

        eventValue.put(EVENT_PARAM_SUBSCRIPTION_START_DATE, new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(Calendar.getInstance().getTime()));
        eventValue.put(EVENT_PARAM_SUBSCRIPTION_END_DATE, subscriptionEndDate);

        AppsFlyerLib.getInstance().setCustomerUserId(appCMSPresenter.getLoggedInUser());
        AppsFlyerLib.getInstance().trackEvent(appCMSPresenter.getCurrentContext(), EVENT_ADD_TO_CART, eventValue);
    }

    private static void addUserDetails(Map<String, Object> eventValue,
                                       AppCMSPresenter appCMSPresenter) {
        AppsFlyerLib.getInstance().setCustomerUserId(appCMSPresenter.getLoggedInUser());
        eventValue.put(USER_ID_EVENT_VALUE, appCMSPresenter.getLoggedInUser());
        eventValue.put(EVENT_PARAM_USER_EMAIL, appCMSPresenter.getLoggedInUserEmail());
        eventValue.put(EVENT_PARAM_USER_NAME, appCMSPresenter.getLoggedInUserName());
        eventValue.put(EVENT_PARAM_USER_ID, appCMSPresenter.getLoggedInUser());
        String phone = "";
        if (!TextUtils.isEmpty(appCMSPresenter.getAppPreference().getLoggedInUserPhone())) {
            phone = appCMSPresenter.getAppPreference().getLoggedInUserPhone();
        } else if (TextUtils.isEmpty(appCMSPresenter.getAppPreference().getLoggedInUserPhone())) {
            phone = appCMSPresenter.getAppPreference().getLoggedInUserPhone();
        }
        eventValue.put(EVENT_PARAM_USER_PHONE, phone);
    }

    private static String getCampaignSource(Map<String, Object> conversionData) {
        String campaignName = "";
        for (String attrName : conversionData.keySet()) {
            if (attrName.contains("campaign") && conversionData.get(attrName) != null) {
                campaignName = conversionData.get(attrName).toString();
            }
        }
        return campaignName;
    }

    private static String getDirectorName(AppCMSPresenter appCMSPresenter,
                                          List<CreditBlock> creditBlocks) {
        String directorName = "";
        if (creditBlocks == null || creditBlocks.size() == 0)
            return directorName;

        Map<String, AppCMSUIKeyType> jsonValueKeyMap = appCMSPresenter.getJsonValueKeyMap();
        for (CreditBlock creditBlock : creditBlocks) {
            if (creditBlock != null) {
                AppCMSUIKeyType creditBlockType = jsonValueKeyMap.get(creditBlock.getTitle());
                if (creditBlockType == AppCMSUIKeyType.PAGE_VIDEO_CREDITS_DIRECTEDBY_KEY ||
                        creditBlockType == AppCMSUIKeyType.PAGE_VIDEO_CREDITS_DIRECTOR_KEY ||
                        creditBlockType == AppCMSUIKeyType.PAGE_VIDEO_CREDITS_DIRECTORS_KEY) {
                    directorName = creditBlock.getTitle().toUpperCase();
                    break;
                }
            }

        }
        return directorName;
    }

    private static String getPlatform(AppCMSPresenter appCMSPresenter) {
        String platform;
        if (null != appCMSPresenter && null != appCMSPresenter.getPlatformType() && appCMSPresenter.getPlatformType().equals(AppCMSPresenter.PlatformType.ANDROID)) {
            platform = appCMSPresenter.getCurrentContext().getString(R.string.app_cms_query_param_android_platform);
        } else {
            if (isFireTVDevice(appCMSPresenter.getCurrentContext())) {
                platform = appCMSPresenter.getCurrentContext().getString(R.string.app_cms_query_param_fire_tv);
            } else {
                platform = appCMSPresenter.getCurrentContext().getString(R.string.app_cms_query_param_android_tv);
            }
        }
        return platform;
    }
}