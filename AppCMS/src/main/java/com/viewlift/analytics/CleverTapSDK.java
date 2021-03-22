package com.viewlift.analytics;

import android.app.NotificationManager;
import android.content.Context;
import android.location.Location;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.text.TextUtils;

import com.clevertap.android.sdk.CTInboxListener;
import com.clevertap.android.sdk.CTInboxStyleConfig;
import com.clevertap.android.sdk.CleverTapAPI;
import com.google.android.gms.common.util.Strings;
import com.google.firebase.iid.FirebaseInstanceId;
import com.viewlift.AppCMSApplication;
import com.viewlift.BuildConfig;
import com.viewlift.R;
import com.viewlift.Utils;
import com.viewlift.db.AppPreference;
import com.viewlift.models.data.appcms.api.ClosedCaptions;
import com.viewlift.models.data.appcms.api.ContentDatum;
import com.viewlift.models.data.appcms.downloads.DownloadStatus;
import com.viewlift.presenters.AppCMSPresenter;
import com.xiaomi.mipush.sdk.MiPushClient;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import static com.viewlift.Utils.isFireTVDevice;
import static com.viewlift.utils.CommonUtils.isOnePlusTV;

/**
 * This class is used for sending clevertap analytics events
 *
 * @author Wishy
 * @since 2019-07-18
 */
public class CleverTapSDK implements CTInboxListener {


    Context context;
    CleverTapAPI cleverTapAPI;
    AppPreference appPreference;
    AppCMSPresenter appCMSPresenter;
    final String EVENT_PLAY_STARTED = "Play Started";
    final String EVENT_WATCHED = "Watched";
    final String EVENT_DOWNLOAD_INITIATED = "Download initiated";
    final String EVENT_DOWNLOAD_COMPLETED = "Download completed";
    final String EVENT_SHARE = "Shared";
    final String EVENT_SEARCH = "Searched";
    final String EVENT_VIEW_PLANS = "View Plans";
    final String EVENT_SIGNED_UP = "Signed Up";
    final String EVENT_LOGIN = "Login";
    final String EVENT_PLAYER_BITRATE_CHANGE = "Player BitRate changed";
    final String EVENT_DOWNLOAD_BITRATE_CHANGE = "Download BitRate changed";
    final String EVENT_CAST = "Cast";
    final String EVENT_PAGE_VIEWED = "Page Viewed";
    final String EVENT_PAGE_VIEW = "Page View";
    final String EVENT_LOGOUT = "Logout";
    final String EVENT_ADD_TO_WATCHLIST = "Added to Watchlist";
    final String EVENT_REMOVE_FROM_WATCHLIST = "Removed From Watchlist";
    final String EVENT_SUBSCRIPTION_INITIATED = "Subscription Initiated";
    final String EVENT_MEDIA_ERROR = "Media Error";
    final String EVENT_UTM_VISITED = "UTM Visited";
    final String EVENT_NOTIFICATION_VIEWED = "Notification Viewed";
    final String EVENT_SUBSCRIPATION_PURCHASED_NEW = "Subscription Purchased NEW";
    final String EVENT_SUBSCRIPATION_RENEWED_AUTOMATICALLY_NEW = "Subscription Renewed Automatically NEW";
    final String EVENT_SUBSCRIPATION_SUSPENDED_NEW = "Subscription Suspended NEW";
    final String EVENT_SUBSCRIPATION_CANCELLED_NEW = "Subscription Cancelled NEW";
    final String EVENT_SUBSCRIPATION_RENEWED_MANUALY_NEW = "Subscription Renewed Manualy NEW";
    final String EVENT_SUBSCRIPATION_SUBSCRIPTION_COMPLETED = "Subscription Completed";
    final String EVENT_SUBSCRIPATION_PLAN_STATUS_SUCCESS = "Subscription Plan Status Success";
    final String EVENT_SUBSCRIPATION_PLAN_STATUS_FAILURE = "Subscription Plan Status Failure";
    final String EVENT_INVITE_EVENT = "Invite Event";


    final String KEY_SUBSCRIPTION_START_DATE = "subscriptionStartDate";
    final String KEY_SUBSCRIPTION_END_DATE = "subscriptionEndDate";

    final String KEY_TRANSCATION = "transactionId";
    final String KEY_EDITORS_NAME = "editorsName";
    final String KEY_MUSIC_BY_NAME = "music by Name";
    final String KEY_ISTENED_TIME = "Listened Time";
    final String KEY_CAST_NAME = "castName";
    final String KEY_MUSIC_NAME = "musicName";
    final String KEY_PRODUCTION_COMPANY_NAME = "production company Name";
    final String KEY_PRODUCED_BY_NAME = "produced by Name";
    final String KEY_PRODUCERS_NAME = "producersName";
    final String KEY_STARRING_NAME = "starringName";
    final String KEY_CINEMA_TO_GRAPHY_NAME = "cinematographyName";
    final String KEY_WRITERS_NAME = "writersName";
    final String KEY_SCREEN_PLAY_BY_NAME = "screenplay by Name";
    final String KEY_LYRICS_NAME = "lyricsName";
    final String KEY_EDITED_BY_NAME = "edited byName";
    final String KEY_PLAN_TYPE = "plan Type";
    final String KEY_AUTO_DEBIT_ENABLED = "autoDebitEnabled";

    final String KEY_PAGE_URL = "pageURL";
    final String KEY_TRANSCATION_AMOUNT = "transactionAmount";
    final String KEY_SUBSCRIPTION_PLAN = "subscriptionPlan";
    final String KEY_SUBSCRIPTION_VIA_PLATFORM = "subscribed_via_platform";
    final String KEY_PAYMENT_HANDLER = "paymentHandler";
    final String KEY_SUBSCRIPTION_FROM_COUNTRY = "subscribed_from_country";
    final String KEY_CURRENCY = "currency";
    final String KEY_USER_ID = "userId";
    final String KEY_FREE_TRIAL_ = "freeTrial";
    final String KEY_REDEMPTION_CODE = "redemptionCode";
    final String KEY_DISCOUNT_AMOUNT = "discountAmount";
    final String KEY_PAYMENT_MODE = "paymentMode";
    final String KEY_PLAN_ID = "planId";


    final String KEY_PAGE_NAME = "Page Name";
    final String KEY_LAST_ACTIVITY_NAME = "Last Activity Name";
    final String KEY_LAST_PAGE_NAME = "last Page Name";
    final String KEY_PLATFORM = "Platform";
    final String KEY_APP_VERSION = "App Version";
    final String KEY_REG_TYPE = "Registration Type";
    final String KEY_CONTENT_ID = "Content ID";
    final String KEY_CONTENT_TITLE = "Content Title";
    final String KEY_CONTENT_TYPE = "Content Type";
    final String KEY_CONTENT_TYPE_DETAIL = "Content Type Detail";
    final String KEY_CONTENT_IS_FREE = "freeOrPaid";
    final String KEY_CONTENT_PARENTAL_RATING = "rating";
    final String KEY_PLAY_SOURCE = "Play Source";
    final String KEY_SOURCE = "Source";
    final String KEY_CONTENT_GENRE = "Content Genre";
    final String KEY_ERROR = "Error Message";
    final String KEY_CONTENT_DURATION = "Content Duration";
    final String KEY_EPISODE_NUMBER = "Episode Number";
    final String KEY_PLAYBACK_TYPE = "Playback Type";
    final String KEY_SEASON_NUMBER = "Season Number";
    final String KEY_NETWORK_TYPE = "Network Type";
    final String KEY_SHOW_NAME = "Show name";
    final String KEY_DIRECTOR_NAME = "Director Name";
    final String KEY_MUSIC_DIRECTOR_NAME = "Music Director Name";
    final String KEY_ACTOR_NAME = "Actor Name";
    final String KEY_SINGER_NAME = "Singer Name";
    final String KEY_WATCH_TIME = "Watched time";
    final String KEY_LISTENING_TIME = "Listening time";
    final String KEY_CHANNEL = "Channel";
    final String KEY_STREAM = "Stream";
    final String KEY_BUFFER_TIME = "buffer_time";
    final String KEY_BUFFER_COUNT = "buffer_count";
    final String KEY_SUBTITLES = "subtitles";
    final String KEY_BITRATE = "Bitrate";
    final String KEY_KEYWORD = "Keyword";
    final String KEY_MEDIUM = "Medium";
    final String KEY_QUALITY = "Quality";
    final String KEY_CAST_TYPE = "Cast Type";
    final String KEY_WZRK_ID = "wzrk_id";
    final String KEY_CAMPAIGN_ID = "Campaign id";
    final String KEY_INSTALL = "Install";
    final String KEY_WZRK_PIVOT = "wzrk_pivot";
    final String KEY_WZRK_CTS = "wzrk_cts";
    final String KEY_WZRK_ACCT_ID = "wzrk_acct_id";
    final String KEY_WZRK_PN = "wzrk_pn";
    final String KEY_WZRK_DL = "wzrk_dl";
    final String KEY_WZRK_PID = "wzrk_pid";
    final String KEY_WZRK_CID = "wzrk_cid";
    final String KEY_WZRK_CAMPAIGN_TYPE = "Campaign type";
    final String KEY_WZRK_BC = "wzrk_bc";
    final String KEY_WZRK_BI = "wzrk_bi";
    final String KEY_WZRK_BP = "wzrk_bp";
    final String KEY_WZRK_ANIMATED = "wzrk_animated";
    final String KEY_WZRK_C2A = "wzrk_c2a";
    final String KEY_WZRK_RNV = "wzrk_rnv";
    final String EVENT_SUBSCRIPATION_PLAN_ENDED = "Subscription End";
    final String KEY_PLAN_ENDED = "end Date";
    final String KEY_TYPE_OF_SHARE_KEY = "Type of share";
    final String KEY_COUNT_OF_REFERRALS_KEY = "Count of referrals";
    final String KEY_SUCCESSFUL_INSTALLS_KEY = "Successful installs";
    final String KEY_SUCCESSFUL__SUBSCRIPTIONS = "Successful_subscriptions";
    final String KEY_PAGE_TYPE = "Successful_subscriptions";

    final String KEY_DEVICE_ACTIVATED = "Device Activated";


    private int ischeck;

    private boolean isInitialize;


    // final String KEY_SEASON_NUMBER = "seasonNumber";


    @Inject
    public CleverTapSDK(Context context, AppPreference appPreference) {
        this.context = context;
        this.appPreference = appPreference;
    }

    public void initializeSDK(AppCMSPresenter appCMSPresenter) {
        this.appCMSPresenter = appCMSPresenter;
        try {
            CleverTapAPI.changeCredentials(appCMSPresenter.getAppCMSMain().getCleverTapAnalyticsId(), appCMSPresenter.getAppCMSMain().getCleverTapToken());
        } catch (Exception e) {
            e.printStackTrace();
        }
        cleverTapAPI = CleverTapAPI.getDefaultInstance(context.getApplicationContext());
        if (BuildConfig.DEBUG) {
            CleverTapAPI.setDebugLevel(CleverTapAPI.LogLevel.DEBUG);
        }

        if (appCMSPresenter.getAppCMSMain().isAppInboxEnable()) {
            cleverTapAPI.initializeInbox();
            cleverTapAPI.setCTNotificationInboxListener(this);
        }

        cleverTapAPI.setCTPushAmpListener((AppCMSApplication) context.getApplicationContext());
        cleverTapAPI.enableDeviceNetworkInfoReporting(true);
        CleverTapAPI.createNotificationChannel(context.getApplicationContext(), Utils.getProperty("AppName", context),
                Utils.getProperty("AppName", context), Utils.getProperty("AppName", context), NotificationManager.IMPORTANCE_MAX, false);

        if (appCMSPresenter.getAppCMSMain().getAnalytics() != null
                && !TextUtils.isEmpty(appCMSPresenter.getAppCMSMain().getAnalytics().getCleverMIPushAppId())
                && !TextUtils.isEmpty(appCMSPresenter.getAppCMSMain().getAnalytics().getCleverTapMIPushAppKey())) {
            MiPushClient.registerPush(context, appCMSPresenter.getAppCMSMain().getAnalytics().getCleverMIPushAppId(), appCMSPresenter.getAppCMSMain().getAnalytics().getCleverTapMIPushAppKey());
            cleverTapAPI.pushXiaomiRegistrationId(MiPushClient.getRegId(context), true);
        }

        FirebaseInstanceId.getInstance().getInstanceId().addOnCompleteListener(task -> {
            if (task.isSuccessful() && task.getResult() != null && !TextUtils.isEmpty(task.getResult().getToken())) {
                cleverTapAPI.pushFcmRegistrationId(task.getResult().getToken(), true);
            }
        });

        isInitialize = true;
        setLocation();
    }

    public CleverTapAPI getCleverTapAPI() {
        return cleverTapAPI;
    }

    public boolean isInitialize() {
        return isInitialize;
    }

    final String KEY_PROFILE_NAME = "Name";
    final String KEY_PROFILE_EMAIL = "Email";
    final String KEY_PROFILE_IDENTITY = "Identity";
    final String KEY_PROFILE_PHONE = "Phone";
    final String KEY_PROFILE_PHONE_NUMBER = "phone_number";
    final String KEY_PROFILE_USER_STATUS = "User Status";
    final String KEY_PROFILE_SUBSCRIPTION_PAYMENT_MODE = "Payment Mode";
    final String KEY_PROFILE_SUBSCRIPTION_START_DATE = "Subscription Start Date";
    final String KEY_PROFILE_SUBSCRIPTION_END_DATE = "Subscription End Date";
    final String KEY_PROFILE_SUBSCRIPTION_TRANSACTION_ID = "Transaction ID";
    final String KEY_PROFILE_SUBSCRIPTION_AMOUNT = "Amount";
    final String KEY_PROFILE_DISCOUNT_AMOUNT = "Discount Amount";
    final String KEY_PROFILE_DISCOUNTED_AMOUNT = "Discounted Amount";
    final String KEY_PROFILE_PAYMENT_PLAN = "Payment Plan";
    final String KEY_PROFILE_SOURCE = "Source";
    final String KEY_PROFILE_PAYMENT_HANDLER = "Payment Handler";
    final String KEY_PROFILE_COUNTRY = "Country";
    final String KEY_PROFILE_CURRENCY = "Currency";
    final String KEY_PROFILE_REGISTRATION_METHOD = "Registration Method";
    final String KEY_FREE_TRIAL = "Free Trial";
    final String KEY_LOCATION = "location";
    final String KEY_LAT_AND_LONG = "Latitude and longitude";
    final String KEY_MSG_WHATSAPP = "MSG-whatsapp";

    public void sendUserProfile(String loggedInUser, String loggedInUserName, String loggedInUserEmail,
                                String userStatus, String subscriptionStartDate, String subscriptionEndDate,
                                String transId, String country, double discountPrice, double planPrice,
                                String currency, String planName, String paymentHandler, boolean freeTrial,
                                String mobile, String paymentMode, String provider) {
        HashMap<String, Object> userProfile = new HashMap<>();
        userProfile.put(KEY_PROFILE_NAME, loggedInUserName);
        userProfile.put(KEY_PROFILE_EMAIL, loggedInUserEmail);
        userProfile.put(KEY_PROFILE_IDENTITY, loggedInUser);
        userProfile.put(KEY_PROFILE_USER_STATUS, userStatus);
        userProfile.put(KEY_PROFILE_SUBSCRIPTION_PAYMENT_MODE, paymentMode);
        userProfile.put(KEY_PROFILE_SUBSCRIPTION_START_DATE, subscriptionStartDate);
        userProfile.put(KEY_PROFILE_SUBSCRIPTION_END_DATE, subscriptionEndDate);
        userProfile.put(KEY_PROFILE_SUBSCRIPTION_TRANSACTION_ID, transId);
        userProfile.put(KEY_PROFILE_SUBSCRIPTION_AMOUNT, planPrice);
        userProfile.put(KEY_PROFILE_DISCOUNT_AMOUNT, discountPrice);
        userProfile.put(KEY_PROFILE_PAYMENT_PLAN, planName);
        userProfile.put(KEY_PROFILE_SOURCE, getPlatform(appCMSPresenter));
        userProfile.put(KEY_PROFILE_PAYMENT_HANDLER, paymentHandler);
        userProfile.put(KEY_PROFILE_COUNTRY, country);
        userProfile.put(KEY_PROFILE_CURRENCY, currency);
        if (!Strings.isEmptyOrWhitespace(userPhoneNumber()))
            userProfile.put(KEY_PROFILE_PHONE, userPhoneNumber());
        if (!Strings.isEmptyOrWhitespace(userPhoneNumber()))
            userProfile.put(KEY_PROFILE_PHONE_NUMBER, userPhoneNumber());
        userProfile.put(KEY_PROFILE_REGISTRATION_METHOD, provider);
        userProfile.put(KEY_MSG_WHATSAPP, appPreference.getWhatsappChecked());
        String freetry;
        if (freeTrial)
            freetry = "Yes";
        else
            freetry = "No";
        userProfile.put(KEY_FREE_TRIAL, freetry);
        setLocation();
        cleverTapAPI.onUserLogin(userProfile);
    }

    public void updateUserProfile(String userStatus, String transId, String country, double discountPrice, double planPrice,
                                  String currency, String planName, String paymentHandler, String paymentMode) {
        HashMap<String, Object> userProfile = new HashMap<>();
        userProfile.put(KEY_PROFILE_NAME, appPreference.getLoggedInUserName());
        userProfile.put(KEY_PROFILE_EMAIL, appPreference.getLoggedInUserEmail());
        userProfile.put(KEY_PROFILE_IDENTITY, appPreference.getLoggedInUser());
        userProfile.put(KEY_PROFILE_USER_STATUS, userStatus);
        userProfile.put(KEY_PROFILE_SUBSCRIPTION_PAYMENT_MODE, paymentMode);
        userProfile.put(KEY_PROFILE_SUBSCRIPTION_AMOUNT, planPrice);
        userProfile.put(KEY_PROFILE_DISCOUNT_AMOUNT, discountPrice);
        userProfile.put(KEY_PROFILE_PAYMENT_PLAN, planName);
        userProfile.put(KEY_PROFILE_SOURCE, getPlatform(appCMSPresenter));
        userProfile.put(KEY_PROFILE_PAYMENT_HANDLER, paymentHandler);
        userProfile.put(KEY_PROFILE_COUNTRY, country);
        userProfile.put(KEY_PROFILE_CURRENCY, currency);
        if (!Strings.isEmptyOrWhitespace(userPhoneNumber()))
            userProfile.put(KEY_PROFILE_PHONE, userPhoneNumber());
        if (!Strings.isEmptyOrWhitespace(userPhoneNumber()))
            userProfile.put(KEY_PROFILE_PHONE_NUMBER, userPhoneNumber());

        userProfile.put(KEY_PROFILE_REGISTRATION_METHOD, appPreference.getUserAuthProviderName());
        userProfile.put(KEY_MSG_WHATSAPP, appPreference.getWhatsappChecked());

        if (!TextUtils.isEmpty(transId))
            userProfile.put(KEY_PROFILE_SUBSCRIPTION_TRANSACTION_ID, transId);

        cleverTapAPI.pushProfile(userProfile);
    }

    private void setLocation() {
        appCMSPresenter.getCurrentLocation(response -> {
            if (response != null) {
                try {
                    Location location = new Location("");
                    location.setLatitude(response.getLatitude());
                    location.setLongitude(response.getLongitude());

                    if (cleverTapAPI != null) {
                        cleverTapAPI.setLocation(location);
                        Map<String, Object> profile = new HashMap<>();
                        profile.put(KEY_LOCATION, response.getCityname() + ", " + response.getSubdivisionEnName() + ", " + response.getCountryname());
                        profile.put(KEY_LAT_AND_LONG, response.getLatitude() + ", " + response.getLongitude());
                        cleverTapAPI.pushProfile(profile);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void pushNotificationClickedEvent(Bundle bundle) {
        if (cleverTapAPI != null)
            cleverTapAPI.pushNotificationClickedEvent(bundle);
    }

    private HashMap<String, Object> playKeys(ContentDatum contentDatum) {
        HashMap<String, Object> playEvent = commonKeys(contentDatum);
        if (appCMSPresenter.isVideoDownloaded(contentDatum.getGist().getId())) {
            playEvent.put(KEY_PLAYBACK_TYPE, "Downloaded");
        } else {
            playEvent.put(KEY_PLAYBACK_TYPE, "Streamed");
        }
        playEvent.put(KEY_PLAY_SOURCE, appCMSPresenter.getPlaySource());
        playEvent.put(KEY_CONTENT_GENRE, getGenre(contentDatum));
        playEvent.put(KEY_CONTENT_DURATION, contentDatum.getGist().getRuntime());

        if (contentDatum.getGist().getMediaType() != null && contentDatum.getGist().getMediaType().contains(context.getResources().getString(R.string.media_type_episode))) {
            playEvent.put(KEY_EPISODE_NUMBER, contentDatum.getGist().getEpisodeNum());
            playEvent.put(KEY_SEASON_NUMBER, contentDatum.getGist().getSeasonNum());
            playEvent.put(KEY_SHOW_NAME, contentDatum.getGist().getShowName());
        }
        if (ischeck == 0) {
            playEvent.put(KEY_SINGER_NAME, appCMSPresenter.getArtistNameFromCreditBlocks(contentDatum.getCreditBlocks()));
        } else {
            playEvent.put(KEY_SINGER_NAME, contentDatum.getGist().getArtistName());
        }
        //playEvent.put(KEY_SINGER_NAME, appCMSPresenter.getArtistNameFromCreditBlocks(contentDatum.getCreditBlocks()));
        playEvent.put(KEY_BITRATE, appPreference.getUserDownloadQualityPref());

        playEvent.put(KEY_ACTOR_NAME, appCMSPresenter.getArtistNameFromCreditBlocks(contentDatum.getCreditBlocks()));
        playEvent.put(KEY_DIRECTOR_NAME, appCMSPresenter.getDirectorNameFromCreditBlocks(contentDatum.getCreditBlocks()));
        playEvent.put(KEY_MUSIC_DIRECTOR_NAME, appCMSPresenter.getDirectorNameFromCreditBlocks(contentDatum.getCreditBlocks()));
        //  playEvent.put(KEY_SINGER_NAME, appCMSPresenter.getArtistNameFromCreditBlocks(contentDatum.getCreditBlocks()));
        return playEvent;
    }

    private String getGenre(ContentDatum contentDatum) {
        String genre = "";
        if (contentDatum.getGist().getPrimaryCategory() != null && contentDatum.getGist().getPrimaryCategory().getTitle() != null)
            genre = contentDatum.getGist().getPrimaryCategory().getTitle();
        if (appCMSPresenter.isVideoDownloaded(contentDatum.getGist().getId()) || (contentDatum.getGist().getLocalFileUrl() != null
                && contentDatum.getGist().getLocalFileUrl().contains("file:")))
            genre = contentDatum.getGist().getGenre();
        if (contentDatum.getGist().getGenre() != null)
            genre = contentDatum.getGist().getGenre();
        return genre;
    }

    public void sendEventPlayStarted(ContentDatum contentDatum) {
        HashMap<String, Object> playEvent = playKeys(contentDatum);
        if (contentDatum.getGist().getDownloadStatus() == DownloadStatus.STATUS_COMPLETED)
            playEvent.put(KEY_BITRATE, appPreference.getUserDownloadQualityPref());
        if (contentDatum.getGist() != null && contentDatum.getGist().getContentType() != null &&
                contentDatum.getGist().getContentType().toLowerCase().contains(context.getString(R.string.content_type_audio).toLowerCase()))
            playEvent.remove(KEY_BITRATE);
        if (contentDatum.getGist() != null) {
            String str;
            if (contentDatum.getGist().getPermalink().contains((appCMSPresenter.getCurrentContext().getString(R.string.app_cms_kids_content_type)).toLowerCase())) {
                str = appCMSPresenter.getCurrentContext().getString(R.string.app_cms_kids_content_type).toLowerCase();
            } else if (contentDatum.getGist().getPermalink().contains((appCMSPresenter.getCurrentContext().getString(R.string.app_cms_originals_content_type)).toLowerCase()) || contentDatum.getGist().getPermalink().contains((appCMSPresenter.getCurrentContext().getString(R.string.app_cms_series_content_type)).toLowerCase())) {
                str = (appCMSPresenter.getCurrentContext().getString(R.string.app_cms_originals_content_type)).toLowerCase();
            } else {
                str = appCMSPresenter.getCurrentContext().getString(R.string.app_cms_movies_label).toLowerCase();
            }
            playEvent.put(KEY_CONTENT_TYPE_DETAIL, str);

            if (!TextUtils.isEmpty(contentDatum.getGist().getParentalRating())) {
                playEvent.put(KEY_CONTENT_PARENTAL_RATING, "" + contentDatum.getGist().getParentalRating());
            } else if (!TextUtils.isEmpty(contentDatum.getParentalRating())) {
                playEvent.put(KEY_CONTENT_PARENTAL_RATING, "" + contentDatum.getParentalRating());
            } else {
                playEvent.put(KEY_CONTENT_PARENTAL_RATING, "NR");
            }
            if (contentDatum.getGist().isFree()) {
                playEvent.put(KEY_CONTENT_IS_FREE, appCMSPresenter.getCurrentContext().getString(R.string.app_cms_content_type_paid));
            } else {
                playEvent.put(KEY_CONTENT_IS_FREE, appCMSPresenter.getCurrentContext().getString(R.string.app_cms_content_type_free));
            }
        }
        cleverTapAPI.pushEvent(EVENT_PLAY_STARTED, playEvent);
    }

    public void sendEventCast(ContentDatum contentDatum) {
        HashMap<String, Object> castEvent = playKeys(contentDatum);
        castEvent.put(KEY_CAST_TYPE, "chrome cast");
        cleverTapAPI.pushEvent(EVENT_CAST, castEvent);
    }

    public void sendEventWatched(ContentDatum contentDatum, long watchTime, String stream, int bufferCount, int bufferTime) {
        HashMap<String, Object> watchEvent = commonKeys(contentDatum);
        watchEvent.put(KEY_PLAY_SOURCE, appCMSPresenter.getPlaySource());

        String playbackType = "streamed";
        watchEvent.put(KEY_BITRATE, appCMSPresenter.getCurrentVideoStreamingQuality());
        if (contentDatum.getGist().getDownloadStatus() == DownloadStatus.STATUS_COMPLETED) {
            playbackType = "downloaded";
            watchEvent.put(KEY_BITRATE, appPreference.getUserDownloadQualityPref());
        }
        if (contentDatum.getGist() != null && contentDatum.getGist().getContentType() != null &&
                contentDatum.getGist().getContentType().toLowerCase().contains(context.getString(R.string.content_type_audio).toLowerCase()))
            watchEvent.remove(KEY_BITRATE);

        watchEvent.put(KEY_PLAYBACK_TYPE, playbackType);
        if (contentDatum.getGist() != null && contentDatum.getGist().getContentType() != null &&
                !contentDatum.getGist().getContentType().toLowerCase().contains(context.getString(R.string.content_type_audio).toLowerCase()))
            watchEvent.put(KEY_SUBTITLES, isSubtitlesAvailable(contentDatum));
        if (contentDatum.getGist() != null && contentDatum.getGist().getContentType() != null &&
                contentDatum.getGist().getContentType().toLowerCase().contains(context.getString(R.string.content_type_audio).toLowerCase()))
            watchEvent.put(KEY_LISTENING_TIME, watchTime);
        else
            watchEvent.put(KEY_WATCH_TIME, watchTime);
        //   watchEvent.put(KEY_WATCH_TIME, contentDatum.getGist().getWatchedTime());
        watchEvent.put(KEY_CONTENT_GENRE, getGenre(contentDatum));
        if (!appCMSPresenter.isVideoDownloaded(contentDatum.getGist().getId())) {
            watchEvent.put(KEY_BUFFER_TIME, bufferTime);
            watchEvent.put(KEY_BUFFER_COUNT, bufferCount);
        }
        watchEvent.put(KEY_STREAM, stream);
        if (contentDatum.getGist() != null && contentDatum.getGist().getContentType() != null) {
            watchEvent.put(KEY_CONTENT_TYPE, contentDatum.getGist().getContentType());
        }
        watchEvent.put(KEY_ISTENED_TIME, contentDatum.getGist().getWatchedTime());
        if (contentDatum.getGist() != null) {
            String str;
            if (contentDatum.getGist().getPermalink().contains((appCMSPresenter.getCurrentContext().getString(R.string.app_cms_kids_content_type)).toLowerCase())) {
                str = appCMSPresenter.getCurrentContext().getString(R.string.app_cms_kids_content_type).toLowerCase();
            } else if (contentDatum.getGist().getPermalink().contains((appCMSPresenter.getCurrentContext().getString(R.string.app_cms_originals_content_type)).toLowerCase()) || contentDatum.getGist().getPermalink().contains((appCMSPresenter.getCurrentContext().getString(R.string.app_cms_series_content_type)).toLowerCase())) {
                str = (appCMSPresenter.getCurrentContext().getString(R.string.app_cms_originals_content_type)).toLowerCase();
            } else {
                str = appCMSPresenter.getCurrentContext().getString(R.string.app_cms_movies_label).toLowerCase();
            }
            watchEvent.put(KEY_CONTENT_TYPE_DETAIL, str);
            if (!TextUtils.isEmpty(contentDatum.getGist().getParentalRating())) {
                watchEvent.put(KEY_CONTENT_PARENTAL_RATING, contentDatum.getGist().getParentalRating());
            } else if (!TextUtils.isEmpty(contentDatum.getParentalRating())) {
                watchEvent.put(KEY_CONTENT_PARENTAL_RATING, contentDatum.getParentalRating());
            } else {
                watchEvent.put(KEY_CONTENT_PARENTAL_RATING, "NR");
            }
            if (contentDatum.getGist().isFree()) {
                watchEvent.put(KEY_CONTENT_IS_FREE, appCMSPresenter.getCurrentContext().getString(R.string.app_cms_content_type_paid));
            } else {
                watchEvent.put(KEY_CONTENT_IS_FREE, appCMSPresenter.getCurrentContext().getString(R.string.app_cms_content_type_free));
            }
        }
        cleverTapAPI.pushEvent(EVENT_WATCHED, watchEvent);
    }


    private HashMap<String, Object> commonKeys(ContentDatum contentDatum) {
        HashMap<String, Object> commonEvent = new HashMap<>();
        commonEvent.put(KEY_PLATFORM, getPlatform(appCMSPresenter));
        commonEvent.put(KEY_CONTENT_ID, contentDatum.getGist().getId());
        commonEvent.put(KEY_CONTENT_TITLE, contentDatum.getGist().getTitle());
        String contentType;
        if (contentDatum.getGist().getMediaType() != null)
            if (contentDatum.getGist().getMediaType().contains(context.getResources().getString(R.string.media_type_episode))) {
                contentType = "Episode";
                if (appCMSPresenter.getShowDatum() != null && appCMSPresenter.getShowDatum().getGist() != null) {
                    if (!TextUtils.isEmpty(appCMSPresenter.getShowDatum().getGist().getEpisodeNum())) {
                        commonEvent.put(KEY_EPISODE_NUMBER, appCMSPresenter.getShowDatum().getGist().getEpisodeNum());
                    }

                    if (!TextUtils.isEmpty(appCMSPresenter.getShowDatum().getGist().getSeasonNum())) {
                        commonEvent.put(KEY_SEASON_NUMBER, appCMSPresenter.getShowDatum().getGist().getSeasonNum());
                    }

                    if (!TextUtils.isEmpty(appCMSPresenter.getShowDatum().getGist().getShowName())) {
                        commonEvent.put(KEY_SHOW_NAME, appCMSPresenter.getShowDatum().getGist().getShowName());
                    }

                }
            } else if (contentDatum.getGist().getMediaType().contains(context.getResources().getString(R.string.app_cms_series_content_type)))
                contentType = "Show";
            else
                contentType = contentDatum.getGist().getMediaType();
        else
            contentType = contentDatum.getGist().getContentType();
        commonEvent.put(KEY_CONTENT_TYPE, contentType);
        if (!contentDatum.getGist().getContentType().contains(context.getResources().getString(R.string.app_cms_series_content_type)))
            commonEvent.put(KEY_CONTENT_DURATION, contentDatum.getGist().getRuntime());
        String networkType = "Wifi";
        if (appPreference.getActiveNetworkType() == ConnectivityManager.TYPE_MOBILE)
            networkType = "Cellular";
        commonEvent.put(KEY_NETWORK_TYPE, networkType);
        if (contentDatum.getGist() != null && contentDatum.getGist().getContentType() != null &&
                contentDatum.getGist().getContentType().toLowerCase().contains(context.getString(R.string.content_type_audio).toLowerCase())) {
            commonEvent.put(KEY_MUSIC_DIRECTOR_NAME, appCMSPresenter.getDirectorNameFromCreditBlocks(contentDatum.getCreditBlocks()));
            commonEvent.put(KEY_SINGER_NAME, appCMSPresenter.getArtistNameFromCreditBlocks(contentDatum.getCreditBlocks()));
            if (appCMSPresenter.isVideoDownloaded(contentDatum.getGist().getId()) || (contentDatum.getGist().getLocalFileUrl() != null && contentDatum.getGist().getLocalFileUrl().contains("file:"))) {
                commonEvent.put(KEY_MUSIC_DIRECTOR_NAME, contentDatum.getGist().getDirectorName());
                commonEvent.put(KEY_SINGER_NAME, contentDatum.getGist().getArtistName());
            }
            if (contentDatum.getGist().getDirectorName() != null)
                commonEvent.put(KEY_MUSIC_DIRECTOR_NAME, contentDatum.getGist().getDirectorName());
            if (contentDatum.getGist().getDirectorName() != null)
                commonEvent.put(KEY_SINGER_NAME, contentDatum.getGist().getArtistName());

        } else {
            commonEvent.put(KEY_DIRECTOR_NAME, appCMSPresenter.getDirectorNameFromCreditBlocks(contentDatum.getCreditBlocks()));
            commonEvent.put(KEY_ACTOR_NAME, appCMSPresenter.getArtistNameFromCreditBlocks(contentDatum.getCreditBlocks()));
            if (appCMSPresenter.isVideoDownloaded(contentDatum.getGist().getId()) || (contentDatum.getGist().getLocalFileUrl() != null
                    && contentDatum.getGist().getLocalFileUrl().contains("file:"))) {
                commonEvent.put(KEY_DIRECTOR_NAME, contentDatum.getGist().getDirectorName());
                commonEvent.put(KEY_ACTOR_NAME, contentDatum.getGist().getArtistName());
            }
            if (contentDatum.getGist().getDirectorName() != null)
                commonEvent.put(KEY_DIRECTOR_NAME, contentDatum.getGist().getDirectorName());
            if (contentDatum.getGist().getDirectorName() != null)
                commonEvent.put(KEY_ACTOR_NAME, contentDatum.getGist().getArtistName());
        }
        return commonEvent;
    }

    public void sendEventDownloadStarted(ContentDatum contentDatum) {
        if (contentDatum != null) {
            HashMap<String, Object> startDownloadEvent = playKeys(contentDatum);
            ischeck = 0;
            startDownloadEvent.remove(KEY_PLAY_SOURCE);
            if (contentDatum.getGist() != null && contentDatum.getGist().getContentType() != null &&
                    !contentDatum.getGist().getContentType().toLowerCase().contains(context.getString(R.string.content_type_audio).toLowerCase()))
                startDownloadEvent.put(KEY_BITRATE, appPreference.getUserDownloadQualityPref());
            cleverTapAPI.pushEvent(EVENT_DOWNLOAD_INITIATED, startDownloadEvent);
        }
    }

    public void sendEventDownloadComplete(ContentDatum contentDatum) {
        if (contentDatum != null) {
            HashMap<String, Object> downloadEvent = playKeys(contentDatum);
            ischeck = 1;
            if (contentDatum.getGist() != null && contentDatum.getGist().getContentType() != null && !contentDatum.getGist().getContentType().toLowerCase().contains(context.getString(R.string.content_type_audio).toLowerCase()))
                downloadEvent.put(KEY_BITRATE, appPreference.getUserDownloadQualityPref());
//        if (contentDatum.getGist().getMediaType() != null && contentDatum.getGist().getMediaType().contains(context.getResources().getString(R.string.media_type_episode))) {
//            downloadEvent.put(KEY_EPISODE_NUMBER, contentDatum.getGist().getEpisodeNum());
//            downloadEvent.put(KEY_SEASON_NUMBER, contentDatum.getGist().getSeasonNum());
//            downloadEvent.put(KEY_SHOW_NAME, contentDatum.getGist().getShowName());
//
//        }
            // downloadEvent.remove(KEY_PLAY_SOURCE);
            //  downloadEvent.remove(KEY_PLAYBACK_TYPE);

            //   downloadEvent.put(KEY_SINGER_NAME, appCMSPresenter.getArtistNameFromCreditBlocks(contentDatum.getCreditBlocks()));
            cleverTapAPI.pushEvent(EVENT_DOWNLOAD_COMPLETED, downloadEvent);
        }
    }

    public void sendEventShare(ContentDatum contentDatum) {
        HashMap<String, Object> shareEvent = playKeys(contentDatum);
        shareEvent.remove(KEY_PLAY_SOURCE);
        shareEvent.put(KEY_MEDIUM, "Android Native");
        cleverTapAPI.pushEvent(EVENT_SHARE, shareEvent);
    }


    public void sendEventUTMVISITED(String wzrk_id, String Campaign_id, String Install, String wzrk_pivot, String wzrk_cts, String wzrk_acct_id, String wzrk_pn, String wzrk_dl, String wzrk_pid, String wzrk_cid, String Campaign_type, String wzrk_bc, String wzrk_bi, String wzrk_bp, String wzrk_animated, String wzrk_c2a, String wzrk_rnv) {
        HashMap<String, Object> UTMvisitedEvent = new HashMap<>();
        UTMvisitedEvent.put(KEY_WZRK_ID, wzrk_id);
        UTMvisitedEvent.put(KEY_CAMPAIGN_ID, Campaign_id);
        UTMvisitedEvent.put(KEY_INSTALL, Install);
        UTMvisitedEvent.put(KEY_WZRK_PIVOT, wzrk_pivot);
        UTMvisitedEvent.put(KEY_WZRK_CTS, wzrk_cts);
        UTMvisitedEvent.put(KEY_WZRK_ACCT_ID, wzrk_acct_id);
        UTMvisitedEvent.put(KEY_WZRK_PN, wzrk_pn);
        UTMvisitedEvent.put(KEY_WZRK_DL, wzrk_dl);
        UTMvisitedEvent.put(KEY_WZRK_PID, wzrk_pid);
        UTMvisitedEvent.put(KEY_WZRK_CID, wzrk_cid);
        UTMvisitedEvent.put(KEY_WZRK_CAMPAIGN_TYPE, Campaign_type);
        UTMvisitedEvent.put(KEY_WZRK_BC, wzrk_bc);
        UTMvisitedEvent.put(KEY_WZRK_BI, wzrk_bi);
        UTMvisitedEvent.put(KEY_WZRK_BP, wzrk_bp);
        UTMvisitedEvent.put(KEY_WZRK_ANIMATED, wzrk_animated);
        UTMvisitedEvent.put(KEY_WZRK_C2A, wzrk_c2a);
        UTMvisitedEvent.put(KEY_WZRK_RNV, wzrk_rnv);
        cleverTapAPI.pushEvent(EVENT_UTM_VISITED, UTMvisitedEvent);
    }


    public void sendEventNotificationView(String wzrk_id, String Campaign_id, String wzrk_pivot, String Campaign_type, String wzrk_animated) {
        HashMap<String, Object> notificationEvent = new HashMap<>();
        notificationEvent.put(KEY_WZRK_ID, wzrk_id);
        notificationEvent.put(KEY_CAMPAIGN_ID, Campaign_id);
        notificationEvent.put(KEY_WZRK_PIVOT, wzrk_pivot);
        notificationEvent.put(KEY_WZRK_CAMPAIGN_TYPE, Campaign_type);
        notificationEvent.put(KEY_WZRK_ANIMATED, wzrk_animated);

        cleverTapAPI.pushEvent(EVENT_NOTIFICATION_VIEWED, notificationEvent);
    }

    // Subscription Purchased NEW
    public void sendEventSubscripationPurchasedNew(String subscriptionStartDate, String subscriptionEndDate, String transactionId, double transactionAmount, String subscriptionPlan, String subscribed_via_platform, String paymentHandler, String subscribed_from_country, String currency, String userId, boolean freeTrial, String discountAmount, String paymentMode) {
        HashMap<String, Object> subscriptionPurchasedNewEvent = new HashMap<>();
        subscriptionPurchasedNewEvent.put(KEY_SUBSCRIPTION_START_DATE, subscriptionStartDate);
        subscriptionPurchasedNewEvent.put(KEY_SUBSCRIPTION_END_DATE, subscriptionEndDate);
        subscriptionPurchasedNewEvent.put(KEY_TRANSCATION, transactionId);
        subscriptionPurchasedNewEvent.put(KEY_TRANSCATION_AMOUNT, transactionAmount);
        subscriptionPurchasedNewEvent.put(KEY_SUBSCRIPTION_PLAN, subscriptionPlan);
        subscriptionPurchasedNewEvent.put(KEY_SUBSCRIPTION_VIA_PLATFORM, subscribed_via_platform);
        subscriptionPurchasedNewEvent.put(KEY_PAYMENT_HANDLER, paymentHandler);
        subscriptionPurchasedNewEvent.put(KEY_SUBSCRIPTION_FROM_COUNTRY, subscribed_from_country);
        subscriptionPurchasedNewEvent.put(KEY_CURRENCY, currency);
        subscriptionPurchasedNewEvent.put(KEY_USER_ID, userId);
        subscriptionPurchasedNewEvent.put(KEY_FREE_TRIAL_, freeTrial);
        subscriptionPurchasedNewEvent.put(KEY_DISCOUNT_AMOUNT, discountAmount);
        subscriptionPurchasedNewEvent.put(KEY_PAYMENT_MODE, paymentMode);
        if (!Strings.isEmptyOrWhitespace(userPhoneNumber()))
            subscriptionPurchasedNewEvent.put(KEY_PROFILE_PHONE, userPhoneNumber());

        cleverTapAPI.pushEvent(EVENT_SUBSCRIPATION_PURCHASED_NEW, subscriptionPurchasedNewEvent);
    }


    public void sendEventSubscriptionSuccess(String userId, String planId, String paymentHandler, double transactionAmount,
                                             String planName, String currency, String country, String subscriptionStartDate, String subscriptionEndDate) {
        HashMap<String, Object> subscriptionPurchasedNewEventSuccess = new HashMap<>();
        subscriptionPurchasedNewEventSuccess.put(KEY_USER_ID, userId);
        subscriptionPurchasedNewEventSuccess.put(KEY_PLAN_ID, planId);
        subscriptionPurchasedNewEventSuccess.put(KEY_PAYMENT_HANDLER, paymentHandler);
        subscriptionPurchasedNewEventSuccess.put(KEY_PLATFORM, getPlatform(appCMSPresenter));
        subscriptionPurchasedNewEventSuccess.put(KEY_TRANSCATION_AMOUNT, transactionAmount);
        subscriptionPurchasedNewEventSuccess.put(KEY_PROFILE_PAYMENT_PLAN, planName);
        subscriptionPurchasedNewEventSuccess.put(KEY_PROFILE_CURRENCY, currency);
        subscriptionPurchasedNewEventSuccess.put(KEY_PROFILE_COUNTRY, country);
        if (appCMSPresenter.getUserSubscriptionPlanResult() != null)
            subscriptionPurchasedNewEventSuccess.put(KEY_AUTO_DEBIT_ENABLED, appCMSPresenter.getUserSubscriptionPlanResult().isRenewable());
        else
            subscriptionPurchasedNewEventSuccess.put(KEY_AUTO_DEBIT_ENABLED, false);

        subscriptionPurchasedNewEventSuccess.put(KEY_PROFILE_SUBSCRIPTION_START_DATE, subscriptionStartDate);
        subscriptionPurchasedNewEventSuccess.put(KEY_PROFILE_SUBSCRIPTION_END_DATE, subscriptionEndDate);

        if (!Strings.isEmptyOrWhitespace(userPhoneNumber()))
            subscriptionPurchasedNewEventSuccess.put(KEY_PROFILE_PHONE, userPhoneNumber());

        cleverTapAPI.pushEvent(EVENT_SUBSCRIPATION_PLAN_STATUS_SUCCESS, subscriptionPurchasedNewEventSuccess);
    }

    public void sendEventSubscriptionEnd(String userId, String paymentPlan, String currency, String country, String planEndDate) {
        HashMap<String, Object> subscriptionEndEvent = new HashMap<>();
        subscriptionEndEvent.put(KEY_USER_ID, userId);
        subscriptionEndEvent.put(KEY_PLATFORM, getPlatform(appCMSPresenter));
        subscriptionEndEvent.put(KEY_PROFILE_PAYMENT_PLAN, paymentPlan);
        subscriptionEndEvent.put(KEY_PROFILE_CURRENCY, currency);
        subscriptionEndEvent.put(KEY_PROFILE_COUNTRY, country);
        subscriptionEndEvent.put(KEY_PLAN_ENDED, planEndDate);
        if (appCMSPresenter.getUserSubscriptionPlanResult() != null)
            subscriptionEndEvent.put(KEY_AUTO_DEBIT_ENABLED, appCMSPresenter.getUserSubscriptionPlanResult().isRenewable());
        else
            subscriptionEndEvent.put(KEY_AUTO_DEBIT_ENABLED, false);
        cleverTapAPI.pushEvent(EVENT_SUBSCRIPATION_PLAN_ENDED, subscriptionEndEvent);
    }

    public void sendInviteEvent(String referralCounts, String source, String successfulInstalls, String successfulSubscription) {
        HashMap<String, Object> subscriptionEndEvent = new HashMap<>();
        subscriptionEndEvent.put(KEY_TYPE_OF_SHARE_KEY, appCMSPresenter.getCurrentActivity().getString(R.string.app_cms_action_referralPlans1_key));
        subscriptionEndEvent.put(KEY_SOURCE, source);
        subscriptionEndEvent.put(KEY_PLATFORM, getPlatform(appCMSPresenter));
        if (appCMSPresenter.getUserSubscriptionInfo() != null)
            subscriptionEndEvent.put(KEY_PROFILE_PAYMENT_PLAN, appCMSPresenter.getUserSubscriptionInfo().getIdentifier());
        subscriptionEndEvent.put(KEY_COUNT_OF_REFERRALS_KEY,referralCounts);
        subscriptionEndEvent.put(KEY_SUCCESSFUL_INSTALLS_KEY, successfulInstalls);
        subscriptionEndEvent.put(KEY_SUCCESSFUL__SUBSCRIPTIONS, successfulSubscription);
        cleverTapAPI.pushEvent(EVENT_INVITE_EVENT, subscriptionEndEvent);
    }

    public void sendEventSubscripationFailure(String planId, String paymentHandler, double transactionAmount, String planName, String currency, String country) {
        HashMap<String, Object> subscriptionPurchasedNewEventFailure = new HashMap<>();
        subscriptionPurchasedNewEventFailure.put(KEY_USER_ID, appPreference.getLoggedInUser());
        subscriptionPurchasedNewEventFailure.put(KEY_PLAN_ID, planId);
        subscriptionPurchasedNewEventFailure.put(KEY_PAYMENT_HANDLER, paymentHandler);
        subscriptionPurchasedNewEventFailure.put(KEY_PLATFORM, getPlatform(appCMSPresenter));
        subscriptionPurchasedNewEventFailure.put(KEY_TRANSCATION_AMOUNT, transactionAmount);
        subscriptionPurchasedNewEventFailure.put(KEY_PROFILE_PAYMENT_PLAN, planName);
        subscriptionPurchasedNewEventFailure.put(KEY_PROFILE_CURRENCY, currency);
        subscriptionPurchasedNewEventFailure.put(KEY_PROFILE_COUNTRY, country);
        if (appCMSPresenter.getUserSubscriptionPlanResult() != null)
            subscriptionPurchasedNewEventFailure.put(KEY_AUTO_DEBIT_ENABLED, appCMSPresenter.getUserSubscriptionPlanResult().isRenewable());
        else
            subscriptionPurchasedNewEventFailure.put(KEY_AUTO_DEBIT_ENABLED, false);

        if (!Strings.isEmptyOrWhitespace(userPhoneNumber()))
            subscriptionPurchasedNewEventFailure.put(KEY_PROFILE_PHONE, userPhoneNumber());


        cleverTapAPI.pushEvent(EVENT_SUBSCRIPATION_PLAN_STATUS_FAILURE, subscriptionPurchasedNewEventFailure);
    }


    // SubscriptionRenewedAutomaticallyNEW
    public void sendEventSubscriptionRenewedAutomaticallyNEW(String subscriptionStartDate, String subscriptionEndDate, String transactionId, double transactionAmount, String subscriptionPlan, String subscribed_via_platform, String paymentHandler, String subscribed_from_country, String currency, String userId, boolean freeTrial, String discountAmount, String paymentMode) {
        HashMap<String, Object> subscriptionRenewedAutomaticallyNEW = new HashMap<>();
        subscriptionRenewedAutomaticallyNEW.put(KEY_SUBSCRIPTION_START_DATE, subscriptionStartDate);
        subscriptionRenewedAutomaticallyNEW.put(KEY_SUBSCRIPTION_END_DATE, subscriptionEndDate);
        subscriptionRenewedAutomaticallyNEW.put(KEY_TRANSCATION, transactionId);
        subscriptionRenewedAutomaticallyNEW.put(KEY_TRANSCATION_AMOUNT, transactionAmount);
        subscriptionRenewedAutomaticallyNEW.put(KEY_SUBSCRIPTION_PLAN, subscriptionPlan);
        subscriptionRenewedAutomaticallyNEW.put(KEY_SUBSCRIPTION_VIA_PLATFORM, subscribed_via_platform);
        subscriptionRenewedAutomaticallyNEW.put(KEY_PAYMENT_HANDLER, paymentHandler);
        subscriptionRenewedAutomaticallyNEW.put(KEY_SUBSCRIPTION_FROM_COUNTRY, subscribed_from_country);
        subscriptionRenewedAutomaticallyNEW.put(KEY_CURRENCY, currency);
        subscriptionRenewedAutomaticallyNEW.put(KEY_USER_ID, userId);
        subscriptionRenewedAutomaticallyNEW.put(KEY_FREE_TRIAL_, freeTrial);
        subscriptionRenewedAutomaticallyNEW.put(KEY_DISCOUNT_AMOUNT, discountAmount);
        subscriptionRenewedAutomaticallyNEW.put(KEY_PAYMENT_MODE, paymentMode);
        if (!Strings.isEmptyOrWhitespace(userPhoneNumber()))
            subscriptionRenewedAutomaticallyNEW.put(KEY_PROFILE_PHONE, userPhoneNumber());
        cleverTapAPI.pushEvent(EVENT_SUBSCRIPATION_RENEWED_AUTOMATICALLY_NEW, subscriptionRenewedAutomaticallyNEW);
    }

    // SubscriptionSuspendedNEW
    public void sendEventSubscriptionSuspendedNEW(String subscriptionStartDate, String subscriptionEndDate, String transactionId, double transactionAmount, String subscriptionPlan, String subscribed_via_platform, String paymentHandler, String subscribed_from_country, String currency, String userId, boolean freeTrial, String discountAmount, String paymentMode) {
        HashMap<String, Object> subscriptionSuspendedNewEvent = new HashMap<>();
        subscriptionSuspendedNewEvent.put(KEY_SUBSCRIPTION_START_DATE, subscriptionStartDate);
        subscriptionSuspendedNewEvent.put(KEY_SUBSCRIPTION_END_DATE, subscriptionEndDate);
        subscriptionSuspendedNewEvent.put(KEY_TRANSCATION, transactionId);
        subscriptionSuspendedNewEvent.put(KEY_TRANSCATION_AMOUNT, transactionAmount);
        subscriptionSuspendedNewEvent.put(KEY_SUBSCRIPTION_PLAN, subscriptionPlan);
        subscriptionSuspendedNewEvent.put(KEY_SUBSCRIPTION_VIA_PLATFORM, subscribed_via_platform);
        subscriptionSuspendedNewEvent.put(KEY_PAYMENT_HANDLER, paymentHandler);
        subscriptionSuspendedNewEvent.put(KEY_SUBSCRIPTION_FROM_COUNTRY, subscribed_from_country);
        subscriptionSuspendedNewEvent.put(KEY_CURRENCY, currency);
        subscriptionSuspendedNewEvent.put(KEY_USER_ID, userId);
        subscriptionSuspendedNewEvent.put(KEY_FREE_TRIAL_, freeTrial);
        subscriptionSuspendedNewEvent.put(KEY_DISCOUNT_AMOUNT, discountAmount);
        subscriptionSuspendedNewEvent.put(KEY_PAYMENT_MODE, paymentMode);
        if (!Strings.isEmptyOrWhitespace(userPhoneNumber()))
            subscriptionSuspendedNewEvent.put(KEY_PROFILE_PHONE, userPhoneNumber());
        cleverTapAPI.pushEvent(EVENT_SUBSCRIPATION_SUSPENDED_NEW, subscriptionSuspendedNewEvent);
    }


    // SubscriptionCancelledNEW
    public void sendEventSubscriptionCancelledNEW(String subscriptionStartDate, String subscriptionEndDate, String transactionId, double transactionAmount, String subscriptionPlan, String subscribed_via_platform, String paymentHandler, String subscribed_from_country, String currency, String userId, boolean freeTrial, String paymentMode) {
        HashMap<String, Object> subscriptionCancelledNewEvent = new HashMap<>();

        subscriptionCancelledNewEvent.put(KEY_SUBSCRIPTION_START_DATE, subscriptionStartDate);
        subscriptionCancelledNewEvent.put(KEY_SUBSCRIPTION_END_DATE, subscriptionEndDate);
        subscriptionCancelledNewEvent.put(KEY_TRANSCATION, transactionId);
        subscriptionCancelledNewEvent.put(KEY_TRANSCATION_AMOUNT, transactionAmount);
        subscriptionCancelledNewEvent.put(KEY_SUBSCRIPTION_PLAN, subscriptionPlan);
        subscriptionCancelledNewEvent.put(KEY_SUBSCRIPTION_VIA_PLATFORM, subscribed_via_platform);
        subscriptionCancelledNewEvent.put(KEY_PAYMENT_HANDLER, paymentHandler);
        subscriptionCancelledNewEvent.put(KEY_SUBSCRIPTION_FROM_COUNTRY, subscribed_from_country);
        subscriptionCancelledNewEvent.put(KEY_CURRENCY, currency);
        subscriptionCancelledNewEvent.put(KEY_USER_ID, userId);
        subscriptionCancelledNewEvent.put(KEY_FREE_TRIAL_, freeTrial);
        subscriptionCancelledNewEvent.put(KEY_PAYMENT_MODE, paymentMode);
        if (!Strings.isEmptyOrWhitespace(userPhoneNumber()))
            subscriptionCancelledNewEvent.put(KEY_PROFILE_PHONE, userPhoneNumber());
        cleverTapAPI.pushEvent(EVENT_SUBSCRIPATION_CANCELLED_NEW, subscriptionCancelledNewEvent);
    }

    // SubscriptionRenewedManualyNEW
    public void sendEventSubscriptionRenewedManualyNEW(String subscriptionStartDate, String subscriptionEndDate, String transactionId, double transactionAmount, String subscriptionPlan, String subscribed_via_platform, String paymentHandler, String subscribed_from_country, String currency, String userId, boolean freeTrial, String discountAmount, String paymentMode) {
        HashMap<String, Object> subscriptionRenewedManualyNewEvent = new HashMap<>();

        subscriptionRenewedManualyNewEvent.put(KEY_SUBSCRIPTION_START_DATE, subscriptionStartDate);
        subscriptionRenewedManualyNewEvent.put(KEY_SUBSCRIPTION_END_DATE, subscriptionEndDate);
        subscriptionRenewedManualyNewEvent.put(KEY_TRANSCATION, transactionId);
        subscriptionRenewedManualyNewEvent.put(KEY_TRANSCATION_AMOUNT, transactionAmount);
        subscriptionRenewedManualyNewEvent.put(KEY_SUBSCRIPTION_PLAN, subscriptionPlan);
        subscriptionRenewedManualyNewEvent.put(KEY_SUBSCRIPTION_VIA_PLATFORM, subscribed_via_platform);
        subscriptionRenewedManualyNewEvent.put(KEY_PAYMENT_HANDLER, paymentHandler);
        subscriptionRenewedManualyNewEvent.put(KEY_SUBSCRIPTION_FROM_COUNTRY, subscribed_from_country);
        subscriptionRenewedManualyNewEvent.put(KEY_CURRENCY, currency);
        subscriptionRenewedManualyNewEvent.put(KEY_USER_ID, userId);
        subscriptionRenewedManualyNewEvent.put(KEY_FREE_TRIAL_, freeTrial);
        subscriptionRenewedManualyNewEvent.put(KEY_DISCOUNT_AMOUNT, discountAmount);
        subscriptionRenewedManualyNewEvent.put(KEY_PAYMENT_MODE, paymentMode);

        cleverTapAPI.pushEvent(EVENT_SUBSCRIPATION_RENEWED_MANUALY_NEW, subscriptionRenewedManualyNewEvent);
    }


    // SubscriptionRenewedManualyNEW
    public void sendEventSubscriptionCompleted(String subscriptionPlan, String transactionId, double transactionAmount, String subscribed_via_platform, String paymentHandler, String subscribed_from_country, String currency, String userId, double discountAmount, double discountPrice, String planType) {
        HashMap<String, Object> subscriptionRenewedManualyNewEvent = new HashMap<>();
        subscriptionRenewedManualyNewEvent.put(KEY_TRANSCATION, transactionId);
        subscriptionRenewedManualyNewEvent.put(KEY_TRANSCATION_AMOUNT, transactionAmount);
        subscriptionRenewedManualyNewEvent.put(KEY_PROFILE_PAYMENT_PLAN, subscriptionPlan);
        subscriptionRenewedManualyNewEvent.put(KEY_SUBSCRIPTION_VIA_PLATFORM, subscribed_via_platform);
        subscriptionRenewedManualyNewEvent.put(KEY_PAYMENT_HANDLER, paymentHandler);
        subscriptionRenewedManualyNewEvent.put(KEY_SUBSCRIPTION_FROM_COUNTRY, subscribed_from_country);
        subscriptionRenewedManualyNewEvent.put(KEY_CURRENCY, currency);
        subscriptionRenewedManualyNewEvent.put(KEY_PROFILE_DISCOUNT_AMOUNT, discountPrice);
        subscriptionRenewedManualyNewEvent.put(KEY_USER_ID, userId);
        subscriptionRenewedManualyNewEvent.put(KEY_DISCOUNT_AMOUNT, discountAmount);
        subscriptionRenewedManualyNewEvent.put(KEY_PLAN_TYPE, planType);

        cleverTapAPI.pushEvent(EVENT_SUBSCRIPATION_SUBSCRIPTION_COMPLETED, subscriptionRenewedManualyNewEvent);
    }


    public void sendEventSearch(String keyword) {
        HashMap<String, Object> searchEvent = new HashMap<>();
        searchEvent.put(KEY_KEYWORD, keyword);
        searchEvent.put(KEY_PLATFORM, getPlatform(appCMSPresenter));
        cleverTapAPI.pushEvent(EVENT_SEARCH, searchEvent);
    }

    public void sendEventSignUp(String regType, String appVersion) {
        HashMap<String, Object> signUpEvent = new HashMap<>();
        signUpEvent.put(KEY_REG_TYPE, regType);
        signUpEvent.put(KEY_PLATFORM, getPlatform(appCMSPresenter));
        signUpEvent.put(KEY_APP_VERSION, appVersion);
        cleverTapAPI.pushEvent(EVENT_SIGNED_UP, signUpEvent);

    }

    public void sendEventLogin(String regType, String appVersion) {
        HashMap<String, Object> loginEvent = new HashMap<>();
        loginEvent.put(KEY_REG_TYPE, regType);
        loginEvent.put(KEY_PLATFORM, getPlatform(appCMSPresenter));
        loginEvent.put(KEY_APP_VERSION, appVersion);
        cleverTapAPI.pushEvent(EVENT_LOGIN, loginEvent);
    }

    public void sendEventPlayerBitrateChange(String quality) {
        HashMap<String, Object> bitrateEvent = new HashMap<>();
        bitrateEvent.put(KEY_PLATFORM, getPlatform(appCMSPresenter));
        bitrateEvent.put(KEY_QUALITY, quality);
        cleverTapAPI.pushEvent(EVENT_PLAYER_BITRATE_CHANGE, bitrateEvent);
    }

    public void sendEventDownloadBitrateChange(String quality) {
        HashMap<String, Object> bitrateEvent = new HashMap<>();
        bitrateEvent.put(KEY_PLATFORM, getPlatform(appCMSPresenter));
        bitrateEvent.put(KEY_QUALITY, quality);
        cleverTapAPI.pushEvent(EVENT_DOWNLOAD_BITRATE_CHANGE, bitrateEvent);
    }

    public void sendEventPageViewed(String lastPage, String pageName, String appVersion) {
        HashMap<String, Object> pageEvent = new HashMap<>();
        pageEvent.put(KEY_PLATFORM, getPlatform(appCMSPresenter));
        pageEvent.put(KEY_LAST_PAGE_NAME, pageName);
        pageEvent.put(KEY_LAST_ACTIVITY_NAME, lastPage);
        pageEvent.put(KEY_APP_VERSION, appVersion);
        if (appCMSPresenter.getUserSubscriptionInfo() != null)
            pageEvent.put(KEY_PROFILE_PAYMENT_PLAN, appCMSPresenter.getUserSubscriptionInfo().getIdentifier());
        pageEvent.put(KEY_SOURCE, appCMSPresenter.getPlaySource());
        cleverTapAPI.pushEvent(EVENT_PAGE_VIEWED, pageEvent);
    }

    public void sendEventPageView(String lastPage, String pageName) {
        HashMap<String, Object> pageEvent = new HashMap<>();
        if (appPreference.getLoggedInUser() != null) {
            pageEvent.put(KEY_USER_ID, appPreference.getLoggedInUser());
        } else {
            pageEvent.put(KEY_USER_ID, appCMSPresenter.getAppCMSAndroid().getAppName().toUpperCase());
        }
        pageEvent.put(KEY_PLATFORM, getPlatform(appCMSPresenter));
        pageEvent.put(KEY_LAST_PAGE_NAME, lastPage);
        pageEvent.put(KEY_LAST_ACTIVITY_NAME, pageName);
        cleverTapAPI.pushEvent(EVENT_PAGE_VIEW, pageEvent);
    }

    public void sendEventLogout() {
        HashMap<String, Object> logoutEvent = new HashMap<>();
        logoutEvent.put(KEY_PLATFORM, getPlatform(appCMSPresenter));
        cleverTapAPI.pushEvent(EVENT_LOGOUT, logoutEvent);
    }

    public void sendEventViewPlans(String pageType) {
        HashMap<String, Object> viewPlanEvent = new HashMap<>();
        viewPlanEvent.put(KEY_PLATFORM, getPlatform(appCMSPresenter));
        if (appCMSPresenter != null && appCMSPresenter.getPlaySource() != null)
            viewPlanEvent.put(KEY_SOURCE, appCMSPresenter.getPlaySource());
        viewPlanEvent.put(KEY_PAGE_TYPE, pageType);
        cleverTapAPI.pushEvent(EVENT_VIEW_PLANS, viewPlanEvent);
    }

    public void sendEventAddWatchlist(ContentDatum contentDatum) {
        HashMap<String, Object> watchlistEvent = playKeys(contentDatum);
        cleverTapAPI.pushEvent(EVENT_ADD_TO_WATCHLIST, watchlistEvent);

    }

    public void sendEventRemoveWatchlist(ContentDatum contentDatum) {
        HashMap<String, Object> watchlistEvent = playKeys(contentDatum);
        cleverTapAPI.pushEvent(EVENT_REMOVE_FROM_WATCHLIST, watchlistEvent);
    }

    public void sendEventMediaError(ContentDatum contentDatum, String error, long watchTime) {
        HashMap<String, Object> errorEvent = commonKeys(contentDatum);

        String playbackType = "streamed";
        if (contentDatum.getGist().getDownloadStatus() == DownloadStatus.STATUS_COMPLETED)
            playbackType = "downloaded";
        errorEvent.put(KEY_PLAYBACK_TYPE, playbackType);
        errorEvent.put(KEY_SUBTITLES, isSubtitlesAvailable(contentDatum));
        if (contentDatum.getGist() != null && contentDatum.getGist().getContentType() != null &&
                contentDatum.getGist().getContentType().toLowerCase().contains(context.getString(R.string.content_type_audio).toLowerCase()))
            errorEvent.put(KEY_LISTENING_TIME, watchTime);
        else
            errorEvent.put(KEY_WATCH_TIME, watchTime);
        errorEvent.put(KEY_CONTENT_GENRE, getGenre(contentDatum));
        errorEvent.put(KEY_ERROR, error);
        cleverTapAPI.pushEvent(EVENT_MEDIA_ERROR, errorEvent);
    }

    public void sendEventSubscriptionInitiated(String paymentHandler, double transactionAmount, String country, double discountPrice, double planPrice, String currency, String planName, double discountAmount) {
        HashMap<String, Object> subscriptionEvent = new HashMap<>();
        subscriptionEvent.put(KEY_PROFILE_PAYMENT_HANDLER, paymentHandler);
        subscriptionEvent.put(KEY_PROFILE_COUNTRY, country);
        subscriptionEvent.put(KEY_PLATFORM, getPlatform(appCMSPresenter));
        subscriptionEvent.put(KEY_PROFILE_DISCOUNT_AMOUNT, discountPrice);
        subscriptionEvent.put(KEY_PROFILE_SUBSCRIPTION_AMOUNT, planPrice);
        subscriptionEvent.put(KEY_PROFILE_CURRENCY, currency);
        subscriptionEvent.put(KEY_PROFILE_PAYMENT_PLAN, planName);
        subscriptionEvent.put(KEY_PROFILE_DISCOUNTED_AMOUNT, discountAmount);
        subscriptionEvent.put(KEY_TRANSCATION_AMOUNT, transactionAmount);
        cleverTapAPI.pushEvent(EVENT_SUBSCRIPTION_INITIATED, subscriptionEvent);

    }

    private String isSubtitlesAvailable(ContentDatum contentDatum) {
        boolean subtitleAvailable = false;
        if (contentDatum != null
                && contentDatum.getContentDetails() != null
                && contentDatum.getContentDetails().getClosedCaptions() != null
                && !contentDatum.getContentDetails().getClosedCaptions().isEmpty()) {
            for (ClosedCaptions cc : contentDatum.getContentDetails().getClosedCaptions()) {
                if (cc.getUrl() != null) {
                    if ((cc.getFormat() != null &&
                            cc.getFormat().equalsIgnoreCase("srt")) ||
                            cc.getUrl().toLowerCase().contains("srt")) {

                        subtitleAvailable = true;
                    }
                }
            }
        }
        if (subtitleAvailable)
            return "Y";
        else
            return "N";
    }

    @Override
    public void inboxDidInitialize() {

    }

    @Override
    public void inboxMessagesDidUpdate() {

    }


    public void loadAppInboxView() {
        ArrayList<String> tabs = new ArrayList<>();
        tabs.add(context.getString(R.string.coming_soon));
        tabs.add(context.getString(R.string.updates));
        //tabs.add("Others");//We support upto 2 tabs only. Additional tabs will be ignored
        CTInboxStyleConfig styleConfig = new CTInboxStyleConfig();
        styleConfig.setTabs(tabs);//Do not use this if you don't want to use tabs
        styleConfig.setTabBackgroundColor("#FFFFFF");//provide Hex code in string ONLY
        styleConfig.setSelectedTabIndicatorColor("#0000FF");
        styleConfig.setSelectedTabColor("#0000FF");
        styleConfig.setUnselectedTabColor("#000000");
        styleConfig.setBackButtonColor("#FFFFFF");
        styleConfig.setNavBarTitleColor("#FFFFFF");
        styleConfig.setNavBarTitle(context.getString(R.string.notifications));
        styleConfig.setNavBarColor("#000000");
        styleConfig.setInboxBackgroundColor("#FFFFFF");
        cleverTapAPI.showAppInbox(styleConfig); //Opens activity tith Tabs
    }


    public void sendDeviceActivatedEvent() {
        HashMap<String, Object> deviceActivationEvent = new HashMap<>();
        deviceActivationEvent.put(KEY_PLATFORM, getPlatform(appCMSPresenter));
        deviceActivationEvent.put(KEY_PROFILE_NAME, appPreference.getLoggedInUserName());
        deviceActivationEvent.put(KEY_PROFILE_EMAIL, appPreference.getLoggedInUserEmail());
        deviceActivationEvent.put(KEY_PROFILE_PHONE_NUMBER, appPreference.getLoggedInUserPhone());
        cleverTapAPI.pushEvent(KEY_DEVICE_ACTIVATED, deviceActivationEvent);
    }

    public String userPhoneNumber() {
        if (appPreference.getLoggedInUserPhone() != null) {
            return appPreference.getLoggedInUserPhone();
        } else if (appPreference.getCheckOutPhoneNumber() != null) {
            return appPreference.getCheckOutPhoneNumber();
        } else {
            return "";
        }
    }

    public String getPlatform(AppCMSPresenter appCMSPresenter) {
        String platform = "";
        if (appCMSPresenter != null && appCMSPresenter.getCurrentContext() != null) {
            if (null != appCMSPresenter.getPlatformType() && appCMSPresenter.getPlatformType().equals(AppCMSPresenter.PlatformType.ANDROID)) {
                platform = appCMSPresenter.getCurrentContext().getString(R.string.app_cms_query_param_android_platform);
            } else {
                if (isOnePlusTV()) {
                    platform = appCMSPresenter.getCurrentContext().getString(R.string.app_cms_query_param_one_plus_tv);
                } else if (isFireTVDevice(appCMSPresenter.getCurrentContext())) {
                    platform = appCMSPresenter.getCurrentContext().getString(R.string.app_cms_query_param_fire_tv);
                } else {
                    platform = appCMSPresenter.getCurrentContext().getString(R.string.app_cms_query_param_android_tv);
                }
            }
        }

        return platform;
    }
}