package com.viewlift.analytics;

import android.content.Context;
import android.net.ConnectivityManager;
import android.os.Bundle;

import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.viewlift.R;
import com.viewlift.Utils;
import com.viewlift.db.AppPreference;
import com.viewlift.models.data.appcms.api.ClosedCaptions;
import com.viewlift.models.data.appcms.api.ContentDatum;
import com.viewlift.models.data.appcms.downloads.DownloadStatus;
import com.viewlift.presenters.AppCMSPresenter;

import java.math.BigDecimal;
import java.util.Currency;
import java.util.HashMap;
import java.util.Map;

public class FacebookAnalytics {

    AppEventsLogger eventsLogger;
    AppPreference appPreference;
    public boolean isAppRunning = true;
    Context context;
    AppCMSPresenter appCMSPresenter;
    final String EVENT_PLAY_STARTED = "play started";
    final String EVENT_WATCHED = "watched";
    // final String EVENT_DOWNLOAD_INITIATED = "Download initiate";
    final String EVENT_DOWNLOAD_INITIATED = "download initiated";
    final String EVENT_DOWNLOAD_COMPLETED = "download completed";
    final String USER_SELECTED_PLAN = "User Selected Plan";
    final String EVENT_SHARE = "Shared";
    final String EVENT_SEARCH = "Searched";
    final String EVENT_VIEW_PLANS = "View Plans";
    final String EVENT_SIGNED_UP = "Signed Up";
    final String EVENT_LOGIN = "Login";
    final String EVENT_PLAYER_BITRATE_CHANGE = "Player BitRate changed";
    final String EVENT_DOWNLOAD_BITRATE_CHANGE = "Download BitRate changed";
    final String EVENT_CAST = "Cast";
    final String EVENT_PAGE_VIEWED = "Page Viewed";
    final String EVENT_TYPE = "Event Type";
    final String EVENT_LOGOUT = "Logout";
    final String EVENT_ADD_TO_WATCHLIST = "Added to Watchlist";
    final String EVENT_REMOVE_FROM_WATCHLIST = "Removed From Watchlist";
    final String EVENT_SUBSCRIPTION_INITIATED = "Subscription Initiated";
    final String EVENT_SUBSCRIPTION_PURCHASED = "Subscription Purchased";
    final String SUBSCRIBED_VIA_PLATFORM = "Subscribed via platform";
    final String TRANSACTION_AMOUNT = "Transaction Amount";
    final String EVENT_MEDIA_ERROR = "Media Error";

    final String KEY_PAGE_NAME = "Page Name";
    final String KEY_LAST_ACTIVITY_NAME = "Last Page Name";
    final String KEY_PLATFORM = "Platform";
    final String KEY_APP_VERSION = "App Version";
    final String KEY_REG_TYPE = "Registration Type";
    final String KEY_CONTENT_ID = "Content ID";
    final String KEY_CONTENT_TITLE = "Content Title";
    final String KEY_CONTENT_TYPE = "Content Type";
    final String KEY_PLAY_SOURCE = "Play Source";
    final String KEY_CONTENT_GENRE = "Content Genre";
    final String KEY_ERROR = "Error Message";
    final String KEY_CONTENT_DURATION = "Content Duration";
    final String KEY_EPISODE_NUMBER = "Episode Number";
    final String KEY_PLAYBACK_TYPE = "Playback Type";
    final String KEY_SEASON_NUMBER = "Season Number";
    final String KEY_NETWORK_TYPE = "Network Type";
    final String KEY_SHOW_NAME = "Show Name";
    final String KEY_DL_INITIATE_SHOW_NAME = "Show ";
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
    final String KEY_KEYWORD = "Search keyword";
    final String KEY_MEDIUM = "Medium";
    final String KEY_QUALITY = "Quality";
    final String KEY_CAST_TYPE = "Cast Type";
    final String PLATFORM_VALUE = "Android";
    final String KEY_PROFILE_NAME = "Name";
    final String KEY_PROFILE_EMAIL = "Email";
    final String KEY_PROFILE_IDENTITY = "User Id";
    final String KEY_PROFILE_PHONE = "Phone";
    final String KEY_PROFILE_USER_STATUS = "User Status";
    final String KEY_PROFILE_SUBSCRIPTION_PAYMENT_MODE = "Payment Mode";
    final String KEY_PROFILE_SUBSCRIPTION_START_DATE = "Subscription Start Date";
    final String KEY_PROFILE_SUBSCRIPTION_END_DATE = "Subscription End Date";
    final String KEY_PROFILE_SUBSCRIPTION_TRANSACTION_ID = "Transaction ID";
    final String KEY_PROFILE_SUBSCRIPTION_AMOUNT = "Amount";
    final String KEY_PROFILE_DISCOUNT_AMOUNT = "Discount Amount";
    final String KEY_PROFILE_PAYMENT_PLAN = "Payment Plan";
    final String KEY_PROFILE_SOURCE = "Source";
    final String KEY_PROFILE_PAYMENT_HANDLER = "Payment Handler";
    final String KEY_PROFILE_COUNTRY = "Country";
    final String KEY_PROFILE_CURRENCY = "Currency";
    final String KEY_PROFILE_REGISTRATION_METHOD = "Registration Method";
    final String KEY_FREE_TRIAL = "Free Trial";

    public FacebookAnalytics(Context context, AppPreference appPreference) {
        try {
            this.context = context;
            this.appPreference = appPreference;
            if (!FacebookSdk.isInitialized()) {
                FacebookSdk.setApplicationId(Utils.getProperty("FacebookAppId",context));
                FacebookSdk.sdkInitialize(context.getApplicationContext());
            }
            eventsLogger = AppEventsLogger.newLogger(context);
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    public void initializeSDK(AppCMSPresenter appCMSPresenter) {
        this.appCMSPresenter = appCMSPresenter;
        if (eventsLogger == null) {
            eventsLogger = AppEventsLogger.newLogger(context);
        }
    }


    public void sendSubscriptionPurchased(String loggedInUser, String loggedInUserName, String loggedInUserEmail,
                                          String userStatus, String subscriptionStartDate, String subscriptionEndDate,
                                          String userId, String country, double discountPrice, double planPrice,
                                          String currency, String planName, String paymentHandler, boolean freeTrial,
                                          String mobile, String paymentMode, String provider, String gatewayTransid) {

        if (currency == null) {
            currency = "";
        }
        if (gatewayTransid == null) {
            gatewayTransid = "";
        }
        Bundle userProfile = new Bundle();
        if (loggedInUserName != null)
            userProfile.putString(KEY_PROFILE_NAME, loggedInUserName);
        userProfile.putString(KEY_PROFILE_EMAIL, loggedInUserEmail);
        userProfile.putString(KEY_PROFILE_IDENTITY, loggedInUser);
        userProfile.putString(KEY_PROFILE_USER_STATUS, userStatus);
        userProfile.putString(KEY_PROFILE_SUBSCRIPTION_PAYMENT_MODE, paymentMode);
        userProfile.putString(KEY_PROFILE_SUBSCRIPTION_START_DATE, subscriptionStartDate);
        userProfile.putString(KEY_PROFILE_SUBSCRIPTION_END_DATE, subscriptionEndDate);
        userProfile.putString(KEY_PROFILE_SUBSCRIPTION_TRANSACTION_ID, gatewayTransid);
        userProfile.putDouble(TRANSACTION_AMOUNT, discountPrice);
        if (planPrice > discountPrice) {
            userProfile.putDouble(KEY_PROFILE_DISCOUNT_AMOUNT, planPrice - discountPrice);
        } else {
            userProfile.putDouble(KEY_PROFILE_DISCOUNT_AMOUNT, 0.0);
        }
        userProfile.putString(KEY_PROFILE_PAYMENT_PLAN, planName);
        userProfile.putString(SUBSCRIBED_VIA_PLATFORM, PLATFORM_VALUE);
        userProfile.putString(KEY_PROFILE_PAYMENT_HANDLER, paymentHandler);
        userProfile.putString(KEY_PROFILE_COUNTRY, country);
        //  userProfile.putString(KEY_PROFILE_CURRENCY, currency);
        if ((mobile != null) && (!mobile.isEmpty()))
            userProfile.putString(KEY_PROFILE_PHONE, mobile);
        userProfile.putString(KEY_PROFILE_REGISTRATION_METHOD, provider);

        String freetry;
        if (freeTrial)
            freetry = "Yes";
        else
            freetry = "No";
        userProfile.putString(KEY_FREE_TRIAL, freetry);

        //  eventsLogger.logEvent(EVENT_SUBSCRIPTION_PURCHASED,userProfile);
        eventsLogger.logPurchase(BigDecimal.valueOf(discountPrice), Currency.getInstance(currency), userProfile);

    }

    private HashMap<String, Object> playKeys(ContentDatum contentDatum) {
        HashMap<String, Object> playEvent = commonKeys(contentDatum);
        if (appCMSPresenter.isVideoDownloaded(contentDatum.getGist().getId()))
            playEvent.put(KEY_PLAYBACK_TYPE, "Downloaded");
        else
            playEvent.put(KEY_PLAYBACK_TYPE, "Streamed");

        playEvent.put(KEY_PLAY_SOURCE, appCMSPresenter.getPlaySource());
        playEvent.put(KEY_CONTENT_GENRE, getGenre(contentDatum));
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

        Bundle bundleplayEvent = new Bundle();
        try {
            if (playEvent != null) {
                while (playEvent.values().remove(null)) ;
                bundleplayEvent = mapToBundle(playEvent);

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        eventsLogger.logEvent(EVENT_PLAY_STARTED, bundleplayEvent);
    }

    public void sendEventCast(ContentDatum contentDatum) {
        HashMap<String, Object> castEvent = playKeys(contentDatum);
        Bundle bundlecastEvent = new Bundle();
        try {
            while (castEvent.values().remove(null)) ;
            bundlecastEvent = mapToBundle(castEvent);
        } catch (Exception e) {
            e.printStackTrace();
        }
        eventsLogger.logEvent(KEY_CAST_TYPE, bundlecastEvent);

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
        watchEvent.put(KEY_CONTENT_GENRE, getGenre(contentDatum));
        if (!appCMSPresenter.isVideoDownloaded(contentDatum.getGist().getId())) {
            watchEvent.put(KEY_BUFFER_TIME, bufferTime);
            watchEvent.put(KEY_BUFFER_COUNT, bufferCount);
        }
        watchEvent.put(KEY_STREAM, stream);
//        watchEvent.put(KEY_CHANNEL, );

        try {
            while (watchEvent.values().remove(null)) ;
            Bundle bundleWatchedEvent = new Bundle();
            bundleWatchedEvent = mapToBundle(watchEvent);
            eventsLogger.logEvent(EVENT_WATCHED, bundleWatchedEvent);
        } catch (Exception e) {

        }
    }


    public void sendEventDownloadStarted(ContentDatum contentDatum) {
        HashMap<String, Object> downloadEvent = playKeys(contentDatum);
        downloadEvent.remove(KEY_PLAY_SOURCE);
        if (contentDatum.getGist() != null && contentDatum.getGist().getContentType() != null &&
                !contentDatum.getGist().getContentType().toLowerCase().contains(context.getString(R.string.content_type_audio).toLowerCase()))
            downloadEvent.put(KEY_BITRATE, appPreference.getUserDownloadQualityPref());
        Bundle bundledownloadEvent = new Bundle();
        try {
            while (downloadEvent.values().remove(null)) ;
            bundledownloadEvent = mapToBundle(downloadEvent);
        } catch (Exception e) {
            e.printStackTrace();
        }
        eventsLogger.logEvent(EVENT_DOWNLOAD_INITIATED, bundledownloadEvent);
    }

    public void sendEventDownloadComplete(ContentDatum contentDatum) {
        HashMap<String, Object> downloadEvent = playKeys(contentDatum);
        if (contentDatum != null && contentDatum.getGist() != null && contentDatum.getGist().getContentType() != null &&
                !contentDatum.getGist().getContentType().toLowerCase().contains(context.getString(R.string.content_type_audio).toLowerCase()))
            downloadEvent.put(KEY_BITRATE, appPreference.getUserDownloadQualityPref());
        if (contentDatum.getGist().getMediaType() != null && contentDatum.getGist().getMediaType().contains(context.getResources().getString(R.string.media_type_episode))) {
            downloadEvent.put(KEY_EPISODE_NUMBER, contentDatum.getGist().getEpisodeNum());
            downloadEvent.put(KEY_SEASON_NUMBER, contentDatum.getGist().getSeasonNum());
            downloadEvent.put(KEY_SHOW_NAME, contentDatum.getGist().getShowName());
        }
        downloadEvent.remove(KEY_PLAY_SOURCE);
        downloadEvent.remove(KEY_PLAYBACK_TYPE);
        Bundle bundleDownloadEvent = new Bundle();
        try {
            while (downloadEvent.values().remove(null)) ;
            bundleDownloadEvent = mapToBundle(downloadEvent);
        } catch (Exception e) {
            e.printStackTrace();
        }
        eventsLogger.logEvent(EVENT_DOWNLOAD_COMPLETED, bundleDownloadEvent);
    }

    public void sendEventShare(ContentDatum contentDatum) {
        HashMap<String, Object> shareEvent = playKeys(contentDatum);
        shareEvent.remove(KEY_PLAY_SOURCE);
        shareEvent.put(KEY_MEDIUM, "Android Native");
        Bundle bundleshareEvent = new Bundle();
        try {
            while (shareEvent.values().remove(null)) ;
            bundleshareEvent = mapToBundle(shareEvent);
        } catch (Exception e) {
            e.printStackTrace();
        }

        eventsLogger.logEvent(EVENT_SHARE, bundleshareEvent);
    }

    public void sendEventSearch(String keyword) {
        Bundle searchEvent = new Bundle();
        searchEvent.putString(KEY_KEYWORD, keyword);
        searchEvent.putString(KEY_PLATFORM, PLATFORM_VALUE);
        eventsLogger.logEvent(EVENT_SEARCH, searchEvent);
    }

    public void sendEventSignUp(String regType) {
        Bundle signUpEvent = new Bundle();
        signUpEvent.putString(KEY_REG_TYPE, regType);
        signUpEvent.putString(KEY_PLATFORM, PLATFORM_VALUE);
        signUpEvent.putString(EVENT_TYPE, EVENT_SIGNED_UP);
        eventsLogger.logEvent(EVENT_SIGNED_UP, signUpEvent);
    }

    public void sendEventLogin(String regType, String appVersion) {
        Bundle loginEvent = new Bundle();
        loginEvent.putString(KEY_REG_TYPE, regType);
        loginEvent.putString(KEY_PLATFORM, PLATFORM_VALUE);
        loginEvent.putString(KEY_APP_VERSION, appVersion);
        eventsLogger.logEvent(EVENT_LOGIN, loginEvent);
    }

    public void sendEventPlayerBitrateChange(String quality) {
        Bundle bitrateEvent = new Bundle();
        bitrateEvent.putString(KEY_PLATFORM, PLATFORM_VALUE);
        bitrateEvent.putString(KEY_QUALITY, quality);
        eventsLogger.logEvent(EVENT_PLAYER_BITRATE_CHANGE, bitrateEvent);
    }

    public void sendEventDownloadBitrateChange(String quality) {
        Bundle bitrateEvent = new Bundle();
        bitrateEvent.putString(KEY_PLATFORM, PLATFORM_VALUE);
        bitrateEvent.putString(KEY_QUALITY, quality);
        eventsLogger.logEvent(EVENT_DOWNLOAD_BITRATE_CHANGE, bitrateEvent);
    }

    public void sendEventPageViewed(String lastPage, String pageName, String appVersion) {
        Bundle pageEvent = new Bundle();
        pageEvent.putString(KEY_PLATFORM, PLATFORM_VALUE);
        pageEvent.putString(KEY_PAGE_NAME, pageName);
        pageEvent.putString(KEY_LAST_ACTIVITY_NAME, lastPage);
        pageEvent.putString(KEY_APP_VERSION, appVersion);
        pageEvent.putString(EVENT_TYPE, EVENT_PAGE_VIEWED);
        //  eventsLogger.logEvent("Testing", pageEvent);
        eventsLogger.logEvent(EVENT_PAGE_VIEWED, pageEvent);

    }

    public void sendEventLogout() {
        Bundle logoutEvent = new Bundle();
        logoutEvent.putString(KEY_PLATFORM, PLATFORM_VALUE);
        eventsLogger.logEvent(EVENT_LOGOUT, logoutEvent);
    }

    public void sendEventViewPlans() {
        Bundle viewPlanEvent = new Bundle();
        viewPlanEvent.putString(KEY_PLATFORM, PLATFORM_VALUE);
        eventsLogger.logEvent(EVENT_VIEW_PLANS, viewPlanEvent);
    }

    public void sendEventAddWatchlist(ContentDatum contentDatum) {
        HashMap<String, Object> watchlistEvent = playKeys(contentDatum);

        Bundle bundlewatchlistEvent = new Bundle();
        try {
            if (watchlistEvent != null) {
                while (watchlistEvent.values().remove(null)) ;
                bundlewatchlistEvent = mapToBundle(watchlistEvent);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        eventsLogger.logEvent(EVENT_ADD_TO_WATCHLIST, bundlewatchlistEvent);
    }

    public void sendEventRemoveWatchlist(ContentDatum contentDatum) {
        HashMap<String, Object> watchlistEvent = playKeys(contentDatum);
        Bundle bundlewatchlistEvent = new Bundle();
        try {
            if (watchlistEvent != null) {
                while (watchlistEvent.values().remove(null)) ;
                bundlewatchlistEvent = mapToBundle(watchlistEvent);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        eventsLogger.logEvent(EVENT_REMOVE_FROM_WATCHLIST, bundlewatchlistEvent);
    }


    public void sendEventSubscriptionInitiated(String paymentHandler, String country, double discountPrice, double planPrice,
                                               String currency, String planName) {
        try {
            Bundle subscriptionEvent = new Bundle();
            subscriptionEvent.putString(KEY_PROFILE_PAYMENT_HANDLER, paymentHandler);
            subscriptionEvent.putString(KEY_PROFILE_COUNTRY, country);
            subscriptionEvent.putString(KEY_PLATFORM, PLATFORM_VALUE);
            subscriptionEvent.putDouble(KEY_PROFILE_DISCOUNT_AMOUNT, planPrice - discountPrice);
            subscriptionEvent.putDouble(KEY_PROFILE_SUBSCRIPTION_AMOUNT, discountPrice);
            subscriptionEvent.putString(KEY_PROFILE_CURRENCY, currency);
            subscriptionEvent.putString(KEY_PROFILE_PAYMENT_PLAN, planName);
            eventsLogger.logEvent(EVENT_SUBSCRIPTION_INITIATED, subscriptionEvent);
            sendEventUserSelectsaPlan(paymentHandler, country, discountPrice, planPrice, currency, planName);
        } catch (Exception e) {

        }
    }

    public void sendEventUserSelectsaPlan(String paymentHandler, String country, double discountPrice, double planPrice,
                                          String currency, String planName) {
        try {
            Bundle subscriptionEvent = new Bundle();
            subscriptionEvent.putString(KEY_PROFILE_PAYMENT_HANDLER, paymentHandler);
            subscriptionEvent.putString(KEY_PROFILE_COUNTRY, country);
            subscriptionEvent.putString(KEY_PLATFORM, PLATFORM_VALUE);
            subscriptionEvent.putDouble(KEY_PROFILE_DISCOUNT_AMOUNT, planPrice - discountPrice);
            subscriptionEvent.putDouble(KEY_PROFILE_SUBSCRIPTION_AMOUNT, discountPrice);
            subscriptionEvent.putString(KEY_PROFILE_CURRENCY, currency);
            subscriptionEvent.putString(KEY_PROFILE_PAYMENT_PLAN, planName);
            eventsLogger.logEvent(USER_SELECTED_PLAN, subscriptionEvent);

        } catch (Exception e) {

        }
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

    public void sendContactUsEvent(String pageName, String appVersion) {
        Bundle pageEvent = new Bundle();
        pageEvent.putString(KEY_PLATFORM, PLATFORM_VALUE);
        pageEvent.putString(KEY_PAGE_NAME, pageName);
        pageEvent.putString(KEY_LAST_ACTIVITY_NAME, "navigation");
        pageEvent.putString(KEY_APP_VERSION, appVersion);
        pageEvent.putString(EVENT_TYPE, EVENT_PAGE_VIEWED);
        eventsLogger.logEvent(EVENT_PAGE_VIEWED, pageEvent);
    }

    public static String toCamelCase(final String init) {
        if (init == null)
            return null;

        final StringBuilder ret = new StringBuilder(init.length());

        boolean firstChar = true;
        for (final String word : init.split(" ")) {
            if (!word.isEmpty()) {
                if (firstChar) {
                    ret.append(word.substring(0, 1).toLowerCase());
                    firstChar = false;
                } else {
                    ret.append(word.substring(0, 1).toUpperCase());
                }
                ret.append(word.substring(1).toLowerCase());
            }
            if (!(ret.length() == init.length()))
                ret.append(" ");
        }

        return ret.toString();
    }


    public static String formattedKey(String key) {

        String newKey = toCamelCase(key);
        newKey = newKey.replace(" ", "");
        return newKey;
    }

    public static Bundle mapToBundle(Map<String, Object> data) throws Exception {
        Bundle bundle = new Bundle();
        for (Map.Entry<String, Object> entry : data.entrySet()) {

            String formatted = formattedKey(entry.getKey());

            if (entry.getValue() instanceof String) {
                if (entry.getValue() != null && !(((String) entry.getValue()).isEmpty()))
                    bundle.putString(formatted, (String) entry.getValue());
            } else if (entry.getValue() instanceof Double) {

                bundle.putString(formatted, String.valueOf(entry.getValue()));
            } else if (entry.getValue() instanceof Integer) {
                bundle.putString(formatted, String.valueOf(entry.getValue()));
            } else if (entry.getValue() instanceof Long) {

                bundle.putString(formatted, String.valueOf(entry.getValue()));
            } else if (entry.getValue() instanceof Float) {
                bundle.putString(formatted, String.valueOf(entry.getValue()));

            }

        }
        return bundle;
    }

    private HashMap<String, Object> commonKeys(ContentDatum contentDatum) {
        HashMap<String, Object> commonEvent = new HashMap<>();
        if (contentDatum.getGist() != null) {
            commonEvent.put(KEY_PLATFORM, PLATFORM_VALUE);
            commonEvent.put(KEY_CONTENT_ID, contentDatum.getGist().getId());
            commonEvent.put(KEY_CONTENT_TITLE, contentDatum.getGist().getTitle());
            String contentType = "";
            if (contentDatum.getGist().getMediaType() != null)
                if (contentDatum.getGist().getMediaType().contains(context.getResources().getString(R.string.media_type_episode))) {
                    contentType = "Episode";
                    if (appCMSPresenter.getShowDatum() != null) {
                        commonEvent.put(KEY_EPISODE_NUMBER, appCMSPresenter.getShowDatum().getGist().getEpisodeNum());
                        commonEvent.put(KEY_SEASON_NUMBER, appCMSPresenter.getShowDatum().getGist().getSeasonNum());
                        commonEvent.put(KEY_SHOW_NAME, appCMSPresenter.getShowDatum().getGist().getShowName());
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
                if (appCMSPresenter.isVideoDownloaded(contentDatum.getGist().getId()) || (contentDatum.getGist().getLocalFileUrl() != null
                        && contentDatum.getGist().getLocalFileUrl().contains("file:"))) {
                    if (contentDatum.getGist().getDirectorName() != null)
                        commonEvent.put(KEY_MUSIC_DIRECTOR_NAME, contentDatum.getGist().getDirectorName());
                    if (contentDatum.getGist().getArtistName() != null)
                        commonEvent.put(KEY_SINGER_NAME, contentDatum.getGist().getArtistName());
                }
                if (contentDatum.getGist().getDirectorName() != null)
                    commonEvent.put(KEY_MUSIC_DIRECTOR_NAME, contentDatum.getGist().getDirectorName());
                if (contentDatum.getGist().getArtistName() != null)
                    commonEvent.put(KEY_SINGER_NAME, contentDatum.getGist().getArtistName());

            } else {
                commonEvent.put(KEY_DIRECTOR_NAME, appCMSPresenter.getDirectorNameFromCreditBlocks(contentDatum.getCreditBlocks()));
                commonEvent.put(KEY_ACTOR_NAME, appCMSPresenter.getArtistNameFromCreditBlocks(contentDatum.getCreditBlocks()));
                if (appCMSPresenter.isVideoDownloaded(contentDatum.getGist().getId()) || (contentDatum.getGist().getLocalFileUrl() != null
                        && contentDatum.getGist().getLocalFileUrl().contains("file:"))) {
                    if (contentDatum.getGist().getDirectorName() != null)
                        commonEvent.put(KEY_DIRECTOR_NAME, contentDatum.getGist().getDirectorName());
                    commonEvent.put(KEY_ACTOR_NAME, contentDatum.getGist().getArtistName());
                }
                if (contentDatum.getGist().getDirectorName() != null)
                    commonEvent.put(KEY_DIRECTOR_NAME, contentDatum.getGist().getDirectorName());
                if (contentDatum.getGist().getDirectorName() != null)
                    commonEvent.put(KEY_ACTOR_NAME, contentDatum.getGist().getArtistName());
            }
        }
        return commonEvent;
    }

}