package com.viewlift.db

import android.content.Context
import android.content.SharedPreferences
import android.net.ConnectivityManager
import android.text.TextUtils
import com.google.gson.Gson
import com.viewlift.Utils
import com.viewlift.models.data.appcms.user.UserPurchases
import org.json.JSONObject
import java.util.*

class AppPreference(context: Context) {
    private var context: Context
    private var sharedPrefs: SharedPreferences

    init {
        this.context = context
        sharedPrefs = context.getSharedPreferences(Utils.getProperty("SiteId", context), Context.MODE_PRIVATE)
    }

    private val OFFLINE_KEY_LICENSE_PREF_NAME = "offlineKeyMap"
    private val GOOGLE_PLAY_APP_STORE_VERSION_PREF_NAME = "google_play_app_store_version_pref_name"
    private val INSTANCE_ID_PREF_NAME = "instance_id_pref_name"
    private val USER_ID_SHARED_PREF_NAME = "user_id_pref"
    private val USER_LOGGED_IN_TIME_PREF_NAME = "user_loggedin_time_pref"
    private val DOWNLOAD_UI_ID = "download_page_id_pref"
    private val SUBSCRIPTION_STATUS = "subscription_status_pref_name"
    private val DOWNLOAD_OVER_CELLULAR_ENABLED_PREF_NAME = "download_over_cellular_enabled_pref_name"
    private val ACTIVE_NETWORK_TYPE_PREF_NAME = "active_network_type_pref_name"
    private val PLAYING_VIDEO_PREF_NAME = "playing_offline_video_pref_name"
    private val PREVIEW_LIVE_STATUS = "live_preview_status_pref_name"
    private val PREVIEW_LIVE_TIMER_VALUE = "live_preview_timer_pref_name"
    private val FACEBOOK_ACCESS_TOKEN_SHARED_PREF_NAME = "facebook_access_token_shared_pref_name"
    private val GOOGLE_ACCESS_TOKEN_SHARED_PREF_NAME = "google_access_token_shared_pref_name"
    private val CAST_SHARED_PREF_NAME = "cast_shown"
    private val ISAPP_PREINSTALL_PREF_NAME = "is_app_preinstall"
    private val ANONYMOUS_AUTH_TOKEN_PREF_NAME = "anonymous_auth_token_pref_key"
    private val LOGIN_WITH_LINK_ACCOUNT_PREF_NAME = "login_with_link_account"
    private val USER_DOWNLOAD_SDCARD_SHARED_PREF_NAME = "user_download_sd_card_pref"
    private val USER_DOWNLOAD_QUALITY_SCREEN_SHARED_PREF_NAME = "user_download_quality_screen_pref"
    private val USER_DOWNLOAD_QUALITY_SHARED_PREF_NAME = "user_download_quality_pref"
    private val USER_DOWNLOAD_QUALITY_POSITION_SHARED_PREF_NAME = "user_download_quality_pref_position"
    private val USER_CLOSED_CAPTION_PREF_KEY = "user_closed_caption_pref_key"
    private val USER_LIVE_PLAYER_PREF_KEY = "user_live_player_pref_key"
    private val AUDIO_SHUFFLED_SHARED_PREF_NAME = "audio_shuffled_sd_card_pref"
    private val USER_NAME_SHARED_PREF_NAME = "user_name_pref"
    private val USER_AUTH_PROVIDER_SHARED_PREF_NAME = "user_auth_provider_shared_pref_name"
    private val USER_EMAIL_SHARED_PREF_NAME = "user_email_pref"
    private val FRESHCHAT_EMAIL_SHARED_PREF_NAME = "freshchat_email_pref"
    private val USER_PHONE_SHARED_PREF_NAME = "user_phone_pref"
    private val USER_CHECKOUT_PHONE_SHARED_PREF_NAME = "user_checkout_phone_pref"
    private val FRESHCHAT_PHONE_SHARED_PREF_NAME = "freshchat_phone_pref"
    private val FRESHCHAT_PHONE_COUNTRY_CODE_SHARED_PREF_NAME = "freshchat_phone_country_phone_pref"
    private val USER_JUSPAY_PHONE_SHARED_PREF_NAME = "user_juspay_phone_pref"
    private val USER_PHONE_COUNTRY_CODE_SHARED_PREF_NAME = "user_phone_country_code_pref"
    private val USER_PASSWORD_SHARED_PREF_NAME = "user_password_pref"
    private val REFRESH_TOKEN_SHARED_PREF_NAME = "refresh_token_pref"
    private val PHONE_VALUE = "phone_value"
    private val AUTH_TOKEN_SHARED_PREF_NAME = "auth_token_pref"
    private val MINI_PLAYER_VIEW_STATUS = "mini_player_view_status"
    private val APPS_FLYER_KEY_PREF_NAME = "apps_flyer_pref_name_key"
    private val NETWORK_CONNECTED_SHARED_PREF_NAME = "network_connected_share_pref_name"
    private val WIFI_CONNECTED_SHARED_PREF_NAME = "wifi_connected_shared_pref_name"
    private val AUTO_PLAY_ENABLED_PREF_NAME = "autoplay_enabled_pref_key"
    private val AUTO_PLAY_DEFAULT_CHANGED_PREF_NAME = "autoplay_default_changed_pref_key"
    private val IS_USER_SUBSCRIBED = "is_user_subscribed_pref_key"
    private val FLOODLIGHT_STATUS_PREF_NAME = "floodlight_status_pref_key"
    private val EXISTING_GOOGLE_PLAY_SUBSCRIPTION_PRICE = "existing_google_play_subscription_price_pref_key"
    private val EXISTING_GOOGLE_PLAY_SUBSCRIPTION_ID = "existing_google_play_subscription_id_key_pref_key"
    private val EXISTING_GOOGLE_PLAY_SUBSCRIPTION_PURCHASE_TOKEN = "existing_google_play_subscription_purchase_token_key_pref_key"
    private val EXISTING_GOOGLE_PLAY_SUBSCRIPTION_SUSPENDED = "existing_google_play_subscription_suspended_pref_key"
    private val ACTIVE_SUBSCRIPTION_SKU = "active_subscription_sku_pref_key"
    private val ACTIVE_SUBSCRIPTION_COUNTRY_CODE = "active_subscription_country_code_key"
    private val ACTIVE_SUBSCRIPTION_ID = "active_subscription_id_pref_key"
    private val ACTIVE_SUBSCRIPTION_CURRENCY = "active_subscription_currency_pref_key"
    private val USER_FREE_PLAY_TIME_SHARED_PREF_NAME = "user_free_play_time_pref_name"
    private val EXISTING_GOOGLE_PLAY_SUBSCRIPTION_DESCRIPTION = "existing_google_play_subscription_title_pref_key"
    private val EXISTING_SUBSCRIPTION_PLAN_DESCRIPTION = "existing_subscription_plan_description"
    private val EXISTING_SUBSCRIPTION_PLAN_NAME = "existing_subscription_plan_name"
    private val ACTIVE_SUBSCRIPTION_PLAN_NAME = "active_subscription_plan_name_pref_key"
    private val ACTIVE_SUBSCRIPTION_PLAN_TITLE = "active_subscription_plan_title_pref_key"
    private val ACTIVE_SUBSCRIPTION_END_DATE = "active_subscription_end_date_pref_key"
    private val ACTIVE_SUBSCRIPTION_START_DATE = "active_subscription_start_date_pref_key"
    private val ACTIVE_SUBSCRIPTION_STATUS = "active_subscription_status_pref_key"
    private val IS_SUBSCRIBED_PLAN_RENEWABLE = "is_subscribed_plan_renewable"
    private val ACTIVE_SUBSCRIPTION_PLATFORM = "active_subscription_platform_pref_key"
    private val ACTIVE_SUBSCRIPTION_PRICE_NAME = "active_subscription_plan_price_pref_key"
    private val ACTIVE_SUBSCRIPTION_PLAN_CYCLE_TYPE = "active_subscription_plan_cycle_type_pref_key"
    private val ACTIVE_SUBSCRIPTION_PLAN_CYCLE_PERIODMULTIPLIER = "active_subscription_plan_cycle_period_multiplier_pref_key"
    private val ACTIVE_SUBSCRIPTION_PLAN_CURRENCY_CODE = "active_subscription_plan_currency_code_pref_key"
    private val ACTIVE_SUBSCRIPTION_PROCESSOR_NAME = "active_subscription_payment_processor_key"
    private val RESTORE_SUBSCRIPTION_RECEIPT = "restore_subscription_payment_process_key"
    private val ACTIVE_SUBSCRIPTION_RECEIPT = "active_subscription_token_pref_key"
    private val APP_LAUNCHED_FROM_DEEPLINK_PREFS = "app_launched_from_deeplink_prefs_key"
    private val DEEPLINK_CONTENT_ID_PREFS = "deeplink_content_id_prefs_key"
    private val SUBTITLE_LANGUAGE_PREFS = "subtitle_language_prefs_key"
    private val SUBTITLE_SIZE_PREFS = "subtitle_size_prefs_key"
    private val AUDIO_LANGUAGE_PREFS = "audio_language_prefs_key"
    private val IS_HOME_STARTED = "is_home_started"
    private val IS_AUDIO_RELOAD = "is_audio_reload"
    private val LAST_PLAY_SONG_DETAILS = "last_play_song_details"
    private val APP_LAST_LAUNCH_TIME = "APP_LAST_LAUNCH_TIME".toLowerCase(Locale.ROOT)
    private val APP_LAUNCH_TIME = "APP_LAUNCH_TIME".toLowerCase(Locale.ROOT)
    private val LOGIN_SHARED_PREF_NAME = "login_pref"
    private val PURCHASE_KEY = "purchase_key"
    private val LEFT_NAV_KEY = "left_nav_key"
    private val KEY_MSG_WHATSAPP = "MSG-whatsapp";
    private val EMAIL_CONSENT_CHECKED_PREFS = "MSG-EMAIL_CONSENT_CHECKED_PREFS";
    private val APPSFLYER_EVENT_FIRST_APP_OPEN_SENT = "APPSFLYER_EVENT_FIRST_APP_OPEN_SENT".toLowerCase(Locale.ROOT)
    private val APPSFLYER_EVENT_ENGAGED_USER_SENT = "APPSFLYER_EVENT_ENGAGED_USER_SENT".toLowerCase(Locale.ROOT)
    private val APP_OPEN_COUNT = "APP_OPEN_COUNT".toLowerCase(Locale.ROOT)
    private val VIDEO_WATCH_COUNT = "VIDEO_WATCH_COUNT".toLowerCase(Locale.ROOT)
    private val RATING_DIALOG_SHOWN_TIME = "RATING_DIALOG_SHOWN_TIME".toLowerCase(Locale.ROOT)
    private val RATING_DIALOG_SHOWN_FOR_VIDEO_COUNT = "RATING_DIALOG_SHOWN_FOR_VIDEO_COUNT".toLowerCase(Locale.ROOT)
    private val RATING_DIALOG_SHOWN_FOR_APP_OPEN = "RATING_DIALOG_SHOWN_FOR_APP_OPEN".toLowerCase(Locale.ROOT)
    private val PREF_NAME_CALENDAR_EVENT_ID = "PREF_NAME_CALENDAR_EVENT_ID".toLowerCase(Locale.ROOT)
    private val USER_SUBSCRIPTION_PLAN_TILTE = "USER_SUBSCRIPTION_PLAN_TILTE".toLowerCase(Locale.ROOT)
    private val VIDEO_STREAMING_QUALITY = "VIDEO_STREAMING_QUALITY".toLowerCase(Locale.ROOT)
    private val TV_PROVIDER = "TV_PROVIDER".toLowerCase(Locale.ROOT)
    private val PREF_REGISTRATION_TYPE = "PREF_REGISTRATION_TYPE".toLowerCase(Locale.ROOT)
    private val PREF_COUPAN_CODE = "PREF_SOURCE".toLowerCase(Locale.ROOT)
    private val BROWSER_STORAGE = "browser_storage"
    private val BROWSER_OPEN = "browser_open"
    private val IS_SHOW_PIP_MODE = "is_show_pip"
    private val TVE_USER_ID = "TVE_USER_ID".toLowerCase(Locale.ROOT)
    private val TV_PROVIDER_LOGO = "TV_PROVIDER_LOGO".toLowerCase(Locale.ROOT)
    private val TV_PROVIDER_NAME = "TV_PROVIDER_NAME".toLowerCase(Locale.ROOT)
    private val TVE_USER_LOGGED_IN_TIME = "TVE_USER_LOGGED_IN_TIME".toLowerCase(Locale.ROOT)

    private val UA_DEEP_LINK = "UA_DEEP_LINK".toLowerCase(Locale.ROOT)
    private val PARENTAL_PIN = "PARENTAL_PIN".toLowerCase(Locale.ROOT)
    private val PARENTAL_RATING = "parentalRating".toLowerCase(Locale.ROOT)
    private val PARENTAL_CONTROLS = "PARENTAL_CONTROLS".toLowerCase(Locale.ROOT)
    private val BIO_METRIC_PIN = "BIO_METRIC_PIN".toLowerCase(Locale.ROOT)
    private val LOGIN_TYPE = "LOGIN_TYPE".toLowerCase(Locale.ROOT)
    private val GOOGLE_USER_ID = "GOOGLE_USER_ID".toLowerCase(Locale.ROOT)
    private val FACEBOOK_USER_ID = "FACEBOOK_USER_ID".toLowerCase(Locale.ROOT)
    private val USER_PERSONALIZED_GENRES = "user_personalized_genres"
    private val USER_ALLOWED_DOWNLOAD = "USER_ALLOWED_DOWNLOAD".toLowerCase(Locale.ROOT)
    private val USER_ALLOWED_CASTING = "USER_ALLOWED_CASTING".toLowerCase(Locale.ROOT)
    private val USER_ALLOWED_HD_STREAMING = "USER_ALLOWED_HD_STREAMING".toLowerCase(Locale.ROOT)
    private val FIRETV_PLAY_ALLOWED = "FIRETV_PLAY_ALLOWED".toLowerCase(Locale.ROOT)
    private val ANDROID_PLAY_ALLOWED = "ANDROID_PLAY_ALLOWED".toLowerCase(Locale.ROOT)
    private val SET_USER_PASSWORD = "SET_USER_PASSWORD".toLowerCase(Locale.ROOT)
    private val CHURNED_USER = "CHURNED_USER".toLowerCase(Locale.ROOT)
    private val Play_Store_Country_Code = "Play_Store_Country_Code".toLowerCase(Locale.ROOT)
    private val TV_DEVICE_ACTIVATED = "tv_device_activated"
    private val USER_PURCHASES = "USER_PURCHASES".toLowerCase(Locale.ROOT)
    private val ACTIVE_SUBSCRIPTION_PAYMENT_OPERATOR = "active_subscription_payment_operator"
    private val PERSONALIZATION_SCREEN_SHOWN = "personalization_screen_shown"


    /** Sets boolean to allow video play on FTV on basis of subscribed plan feature
     * @param allowed - true/false
     **/
    fun setFiretvPlayAllowed(allowed: Boolean) {
        sharedPrefs.edit().putBoolean(FIRETV_PLAY_ALLOWED, allowed).apply()
    }

    /** Check if video play is allowed on FTV
     * @return boolean to allow video play on FTV
     **/
    fun isFiretvPlayAllowed(): Boolean {
        return sharedPrefs.getBoolean(FIRETV_PLAY_ALLOWED, true)
    }

    /** Sets boolean for mixed user signup if user has set the password or not
     * @param set - true/false
     **/
    fun setUserPassword(set: Boolean) {
        sharedPrefs.edit().putBoolean(SET_USER_PASSWORD, set).apply()
    }

    /** Check if user has set the password or not
     * @return boolean for password ser
     **/
    fun isSetUserPassword(): Boolean {
        return sharedPrefs.getBoolean(SET_USER_PASSWORD, true)
    }

    /** Set this when user's subscription status is CANCELLED / SUSPENDED
     * @param isChurned - true/false
     **/
    fun setChurnedUser(isChurned: Boolean) {
        sharedPrefs.edit().putBoolean(CHURNED_USER, isChurned).apply()
    }

    /** Check if the user is churned or not as to show ads
     * @return boolean for churned user
     **/
    fun isChurnedUser(): Boolean {
        return sharedPrefs.getBoolean(CHURNED_USER, false)
    }

    /** Sets boolean to allow video play on mobile on basis of subscribed plan feature
     * @param allowed - true/false
     **/
    fun setAndroidPlayAllowed(allowed: Boolean) {
        sharedPrefs.edit().putBoolean(ANDROID_PLAY_ALLOWED, allowed).apply()
    }

    /** Check if video play is allowed on mobile
     * @return boolean to allow video play on mobile
     **/
    fun isAndroidPlayAllowed(): Boolean {
        return sharedPrefs.getBoolean(ANDROID_PLAY_ALLOWED, true)
    }

    fun setGooglePlayAppStoreVersion(googlePlayAppStoreVersion: String?): Boolean {
        return sharedPrefs.edit().putString(GOOGLE_PLAY_APP_STORE_VERSION_PREF_NAME, googlePlayAppStoreVersion).commit()
    }

    fun getGooglePlayAppStoreVersion(): String? {
        return sharedPrefs.getString(GOOGLE_PLAY_APP_STORE_VERSION_PREF_NAME, null)
    }

    fun setOfflineLicenseMap(offlineJsonValue: String?) {
        sharedPrefs.edit().remove(OFFLINE_KEY_LICENSE_PREF_NAME).apply()
        sharedPrefs.edit().putString(OFFLINE_KEY_LICENSE_PREF_NAME, offlineJsonValue).apply()
    }

    fun getOfflineKeyPreference(): String? {
        return sharedPrefs.getString(OFFLINE_KEY_LICENSE_PREF_NAME, JSONObject().toString())
    }

    fun getInstanceId(): String? {
        return sharedPrefs.getString(INSTANCE_ID_PREF_NAME, null)
    }

    fun setInstanceId(instanceId: String?) {
        sharedPrefs.edit().putString(INSTANCE_ID_PREF_NAME, instanceId).apply()
    }

    /** Sets logged in user's id and loggedin time
     * @param userId - logged in user id
     **/
    fun setLoggedInUser(userId: String?) {
        sharedPrefs.edit().putString(USER_ID_SHARED_PREF_NAME, userId).apply()
        setLoggedInTime()
    }

    /** Gets user's logged in time for app
     * @return user's logged in time
     **/
    fun getLoggedInTime(): Long {
        return sharedPrefs.getLong(USER_LOGGED_IN_TIME_PREF_NAME, -1L)
    }

    /** Sets loggedin time
     **/
    private fun setLoggedInTime() {
        val now = Date()
        sharedPrefs.edit().putLong(USER_LOGGED_IN_TIME_PREF_NAME, now.time).apply()
    }

    /** Gets old sharedPreference download page ID
     * @return old sharedPreference download page ID
     **/
    @Deprecated("Not used , delete the function in future")
    private fun getOldDownloadPageId(): String? {
        val downloadPref = context.getSharedPreferences(DOWNLOAD_UI_ID, Context.MODE_PRIVATE)
        return downloadPref.getString(DOWNLOAD_UI_ID, null)
    }

    /** Gets download page ID
     * @return download page ID
     **/
    fun getDownloadPageId(): String? {
        return sharedPrefs.getString(DOWNLOAD_UI_ID, null)
    }

    /** Sets download page ID
     * @param pageId - download page ID
     **/
    fun setDownloadPageId(pageId: String?) {
        sharedPrefs.edit().putString(DOWNLOAD_UI_ID, pageId).apply()
    }

    /** Sets user's subscription status
     * @param subscriptionStatus - can be COMPLETED / SUSPENDED / CANCELLED
     **/
    fun setSubscriptionStatus(subscriptionStatus: String?) {
        sharedPrefs.edit().putString(SUBSCRIPTION_STATUS, subscriptionStatus).apply()
    }

    /** Gets user's subscription status
     * @return subscription status - can be COMPLETED / SUSPENDED / CANCELLED
     **/
    fun getSubscriptionStatus(): String? {
        return sharedPrefs.getString(SUBSCRIPTION_STATUS, null)
    }

    /** Sets user's preference to use mobile data for download
     * @param downloadOverCellularEnabled - user's preference to use mobile data for download
     **/
    fun setDownloadOverCellularEnabled(downloadOverCellularEnabled: Boolean) {
        sharedPrefs.edit().putBoolean(DOWNLOAD_OVER_CELLULAR_ENABLED_PREF_NAME, downloadOverCellularEnabled).apply()
    }

    /** gets user's preference to use mobile data for download
     * @return user's preference to use mobile data for download
     **/
    fun getDownloadOverCellularEnabled(): Boolean {
        return sharedPrefs.getBoolean(DOWNLOAD_OVER_CELLULAR_ENABLED_PREF_NAME, false)
    }

    fun setWhatsappChecked(whatsAppConsentEnabled: Boolean): Boolean {
        return sharedPrefs.edit().putBoolean(KEY_MSG_WHATSAPP, whatsAppConsentEnabled).commit()
    }

    fun getWhatsappChecked(): Boolean {
        return sharedPrefs.getBoolean(KEY_MSG_WHATSAPP, false)
    }

    fun setEmailConsentChecked(emailConsentEnabled: Boolean): Boolean {
        return sharedPrefs.edit().putBoolean(EMAIL_CONSENT_CHECKED_PREFS, emailConsentEnabled).commit()
    }

    fun getEmailConsentChecked(): Boolean {
        return sharedPrefs.getBoolean(EMAIL_CONSENT_CHECKED_PREFS, false)
    }

    /** Sets internet usage type
     * @param activeNetworkType - internet usage type
     * 0 - Mobile
     * 1 - Wifie
     **/
    fun setActiveNetworkType(activeNetworkType: Int): Boolean {
        return sharedPrefs.edit().putInt(ACTIVE_NETWORK_TYPE_PREF_NAME, activeNetworkType).commit()
    }

    /** Gets internet usage type
     * @return internet usage type
     * 0 - Mobile
     * 1 - Wifie
     **/
    fun getActiveNetworkType(): Int {
        return sharedPrefs.getInt(ACTIVE_NETWORK_TYPE_PREF_NAME, ConnectivityManager.TYPE_MOBILE)
    }

    fun setPlayingVideo(playingOfflineVideo: Boolean): Boolean {
        return sharedPrefs.edit().putBoolean(PLAYING_VIDEO_PREF_NAME, playingOfflineVideo).commit()
    }

    fun getPlayingVideo(): Boolean {
        return sharedPrefs.getBoolean(PLAYING_VIDEO_PREF_NAME, false)
    }

    fun setPreviewStatus(previewStatus: Boolean) {
        sharedPrefs.edit().putBoolean(PREVIEW_LIVE_STATUS, previewStatus).apply()
    }

    fun getPreviewStatus(): Boolean {
        return sharedPrefs.getBoolean(PREVIEW_LIVE_STATUS, false)
    }

    fun setPreviewTimerValue(previewTimer: Int) {
        sharedPrefs.edit().putInt(PREVIEW_LIVE_TIMER_VALUE, previewTimer).apply()
    }

    fun getPreviewTimerValue(): Int {
        return sharedPrefs.getInt(PREVIEW_LIVE_TIMER_VALUE, 0)
    }

    /** Gets facebook access token
     * @return facebook token
     **/
    fun getFacebookAccessToken(): String? {
        return sharedPrefs.getString(FACEBOOK_ACCESS_TOKEN_SHARED_PREF_NAME, null)
    }

    /** Sets facebook access token
     * @param token - facebook token
     **/
    fun setFacebookAccessToken(token: String?) {
        sharedPrefs.edit().putString(FACEBOOK_ACCESS_TOKEN_SHARED_PREF_NAME, token).apply()
    }

    /** Gets google access token
     * @return google token
     **/
    fun getGoogleAccessToken(): String? {
        return sharedPrefs.getString(GOOGLE_ACCESS_TOKEN_SHARED_PREF_NAME, null)
    }

    /** Sets google access token
     * @param token - google token
     **/
    fun setGoogleAccessToken(token: String?) {
        sharedPrefs.edit().putString(GOOGLE_ACCESS_TOKEN_SHARED_PREF_NAME, token).apply()
    }

    fun setCastOverLay(): Boolean {
        return sharedPrefs.edit().putBoolean(CAST_SHARED_PREF_NAME, true).commit()
    }

    fun isCastOverLayShown(): Boolean {
        return sharedPrefs.getBoolean(CAST_SHARED_PREF_NAME, false)
    }

    fun setAppPreinstalled(isPreinstall: Boolean): Boolean {
        return sharedPrefs.edit().putBoolean(ISAPP_PREINSTALL_PREF_NAME, isPreinstall).commit()
    }

    fun isAppPreinstalled(): Boolean {
        return sharedPrefs.getBoolean(ISAPP_PREINSTALL_PREF_NAME, false)
    }

    /** Gets auth token for guest user
     * @return guest user auth token
     **/
    fun getAnonymousUserToken(): String? {
        return sharedPrefs.getString(ANONYMOUS_AUTH_TOKEN_PREF_NAME, null)
    }

    /** sets auth token for guest user
     * @param anonymousAuthToken - guest user auth token
     **/
    fun setAnonymousUserToken(anonymousAuthToken: String?) {
        sharedPrefs.edit().putString(ANONYMOUS_AUTH_TOKEN_PREF_NAME, anonymousAuthToken).apply()
    }

    fun isLoginWithLinkYourAccount(): Boolean {
        return sharedPrefs.getBoolean(LOGIN_WITH_LINK_ACCOUNT_PREF_NAME, false)
    }

    fun setLoginWithLinkYourAccount(login: Boolean) {
        sharedPrefs.edit().putBoolean(LOGIN_WITH_LINK_ACCOUNT_PREF_NAME, login).apply()
    }

    fun isPreferredStorageLocationSDCard(): Boolean {
        return sharedPrefs.getBoolean(USER_DOWNLOAD_SDCARD_SHARED_PREF_NAME, false)
    }

    fun setPreferredStorageLocationSDCard(downloadPref: Boolean): Boolean {
        return sharedPrefs.edit().putBoolean(USER_DOWNLOAD_SDCARD_SHARED_PREF_NAME,
                downloadPref).commit()
    }

    fun getUserDownloadLocationPref(): Boolean {
        return sharedPrefs.getBoolean(USER_DOWNLOAD_SDCARD_SHARED_PREF_NAME, false)
    }

    fun setUserDownloadLocationPref(downloadPref: Boolean): Boolean {
        return sharedPrefs.edit().putBoolean(USER_DOWNLOAD_SDCARD_SHARED_PREF_NAME,
                downloadPref).commit()
    }

    fun isDownloadQualityScreenShowBefore(): Boolean {
        return sharedPrefs.getBoolean(USER_DOWNLOAD_QUALITY_SCREEN_SHARED_PREF_NAME, false)
    }

    fun setDownloadQualityScreenShowBefore(show: Boolean) {
        sharedPrefs.edit().putBoolean(USER_DOWNLOAD_QUALITY_SCREEN_SHARED_PREF_NAME, show).apply()
    }

    fun getUserDownloadQualityPref(): String? {
        return sharedPrefs.getString(USER_DOWNLOAD_QUALITY_SHARED_PREF_NAME, null);
    }

    fun setUserDownloadQualityPref(downloadQuality: String?) {
        sharedPrefs.edit().putString(USER_DOWNLOAD_QUALITY_SHARED_PREF_NAME, downloadQuality).apply()
    }

    fun setUserDownloadQualityPositionref(downloadQualityPosition: Int) {
        sharedPrefs.edit().putInt(USER_DOWNLOAD_QUALITY_POSITION_SHARED_PREF_NAME, downloadQualityPosition).apply()
    }

    fun getUserDownloadPositionQualityPref(): Int {
        return sharedPrefs.getInt(USER_DOWNLOAD_QUALITY_POSITION_SHARED_PREF_NAME, 0)
    }

    fun getClosedCaptionPreference(): Boolean {
        return sharedPrefs.getBoolean(USER_CLOSED_CAPTION_PREF_KEY, true)
    }

    fun setClosedCaptionPreference(isClosedCaptionOn: Boolean) {
        sharedPrefs.edit().putBoolean(USER_CLOSED_CAPTION_PREF_KEY, isClosedCaptionOn).apply()
    }

    fun getLivePlayerPreference(): Boolean {
        return sharedPrefs.getBoolean(USER_LIVE_PLAYER_PREF_KEY, false)
    }

    fun setLivePlayerPreference(isLive: Boolean) {
        sharedPrefs.edit().putBoolean(USER_LIVE_PLAYER_PREF_KEY, isLive).apply()
    }

    fun getAudioShuffledPreference(): Boolean {
        return sharedPrefs.getBoolean(AUDIO_SHUFFLED_SHARED_PREF_NAME, false)
    }

    fun setAudioShuffledPreference(isAudioShuffledOn: Boolean) {
        sharedPrefs.edit().putBoolean(AUDIO_SHUFFLED_SHARED_PREF_NAME, isAudioShuffledOn).apply()
    }

    fun setLoggedInUserName(userName: String?) {
        sharedPrefs.edit().putString(USER_NAME_SHARED_PREF_NAME, userName).apply()
    }

    fun getUserAuthProviderName(): String? {
        return sharedPrefs.getString(USER_AUTH_PROVIDER_SHARED_PREF_NAME, null)
    }

    fun setUserAuthProviderName(userAuthProviderName: String?) {
        sharedPrefs.edit().putString(USER_AUTH_PROVIDER_SHARED_PREF_NAME, userAuthProviderName).apply()
    }

    fun setLoggedInUserEmail(userEmail: String?) {
        sharedPrefs.edit().putString(USER_EMAIL_SHARED_PREF_NAME, userEmail).apply()
    }

    fun setLoggedInUserPhone(userPhone: String?) {
        sharedPrefs.edit().putString(USER_PHONE_SHARED_PREF_NAME, userPhone).apply()
    }

    fun setCheckOutPhoneNumber(checkoutPhone: String?) {
        sharedPrefs.edit().putString(USER_CHECKOUT_PHONE_SHARED_PREF_NAME, checkoutPhone).apply();

    }

    fun setFreshchatPhone(phone: String?) {
        sharedPrefs.edit().putString(FRESHCHAT_PHONE_SHARED_PREF_NAME, phone).apply()
    }

    fun getFreshchatPhone(): String? {
        return sharedPrefs.getString(FRESHCHAT_PHONE_SHARED_PREF_NAME, null)
    }

    fun setFreshchatPhoneCountryCode(phone: String?) {
        sharedPrefs.edit().putString(FRESHCHAT_PHONE_COUNTRY_CODE_SHARED_PREF_NAME, phone).apply()
    }

    fun getFreshchatPhoneCountryCode(): String? {
        return sharedPrefs.getString(FRESHCHAT_PHONE_COUNTRY_CODE_SHARED_PREF_NAME, null)
    }

    fun setFreshchatEmail(phone: String?) {
        sharedPrefs.edit().putString(FRESHCHAT_EMAIL_SHARED_PREF_NAME, phone).apply()
    }

    fun getFreshchatEmail(): String? {
        return sharedPrefs.getString(FRESHCHAT_EMAIL_SHARED_PREF_NAME, null)
    }

    fun getLoggedInUserPhone(): String? {
        return sharedPrefs.getString(USER_PHONE_SHARED_PREF_NAME, null)
    }

    fun getCheckOutPhoneNumber(): String? {
        return sharedPrefs.getString(USER_CHECKOUT_PHONE_SHARED_PREF_NAME, null)
    }

    fun setLoggedInJuspayUserPhone(userPhone: String?) {
        sharedPrefs.edit().putString(USER_JUSPAY_PHONE_SHARED_PREF_NAME, userPhone).apply()
    }

    fun getLoggedInJusPayUserPhone(): String? {
        return sharedPrefs.getString(USER_JUSPAY_PHONE_SHARED_PREF_NAME, null)
    }

    fun setLoggedInUserPhoneCountryCode(userEmail: String?) {
        sharedPrefs.edit().putString(USER_PHONE_COUNTRY_CODE_SHARED_PREF_NAME, userEmail).apply()
    }

    fun getLoggedInUserPassword(): String? {
        return sharedPrefs.getString(USER_PASSWORD_SHARED_PREF_NAME, null)
    }

    fun setLoggedInUserPassword(password: String?) {
        sharedPrefs.edit().putString(USER_PASSWORD_SHARED_PREF_NAME, password).apply()
    }

    fun setRefreshToken(refreshToken: String?) {
        sharedPrefs.edit().putString(REFRESH_TOKEN_SHARED_PREF_NAME, refreshToken).apply()
    }


    fun setAuthToken(authToken: String?) {
        sharedPrefs.edit().putString(AUTH_TOKEN_SHARED_PREF_NAME, authToken).apply()
    }


    fun setMiniPLayerVisibility(previewStatus: Boolean) {
        sharedPrefs.edit().putBoolean(MINI_PLAYER_VIEW_STATUS, previewStatus).apply()
    }

    fun getMiniPLayerVisibility(): Boolean {
        return sharedPrefs.getBoolean(MINI_PLAYER_VIEW_STATUS, true)
    }

    fun getAppsFlyerKey(): String? {
        return sharedPrefs.getString(APPS_FLYER_KEY_PREF_NAME, null)
    }

    fun setAppsFlyerKey(appsFlyerKey: String?) {
        sharedPrefs.edit().putString(APPS_FLYER_KEY_PREF_NAME, appsFlyerKey).apply()
    }

    fun getNetworkConnectedState(): Boolean {
        return sharedPrefs.getBoolean(NETWORK_CONNECTED_SHARED_PREF_NAME, true)
    }

    fun setNetworkConnected(networkConnected: Boolean): Boolean {
        return sharedPrefs.edit().putBoolean(NETWORK_CONNECTED_SHARED_PREF_NAME, networkConnected).commit()
    }

    fun setWifiConnected(wifiConnected: Boolean): Boolean {
        return sharedPrefs.edit().putBoolean(WIFI_CONNECTED_SHARED_PREF_NAME, wifiConnected).commit()
    }

    fun isWifiConnected(): Boolean {
        return sharedPrefs.getBoolean(WIFI_CONNECTED_SHARED_PREF_NAME, false)
    }

    fun getAutoplayEnabledUserPref(): Boolean {
        return sharedPrefs.getBoolean(AUTO_PLAY_ENABLED_PREF_NAME, true)
    }

    fun setAutoplayEnabledUserPref(isAutoplayEnabled: Boolean) {
        sharedPrefs.edit().putBoolean(AUTO_PLAY_ENABLED_PREF_NAME, isAutoplayEnabled).apply()
    }

    fun getAutoplayDefaultValueChangedPref(): Boolean {
        return sharedPrefs.getBoolean(AUTO_PLAY_DEFAULT_CHANGED_PREF_NAME, true)
    }

    fun setAutoplayDefaultValueChangedPref(isDefaultValueChanged: Boolean) {
        sharedPrefs.edit().putBoolean(AUTO_PLAY_DEFAULT_CHANGED_PREF_NAME, isDefaultValueChanged).apply()
    }

    fun getIsUserSubscribed(): Boolean {
        return sharedPrefs.getBoolean(IS_USER_SUBSCRIBED, false)
    }

    fun setIsUserSubscribed(userSubscribed: Boolean) {
        sharedPrefs.edit().putBoolean(IS_USER_SUBSCRIBED, userSubscribed).apply()
    }

    fun getFloodLightStatus(): Boolean {
        return sharedPrefs.getBoolean(FLOODLIGHT_STATUS_PREF_NAME, false)
    }

    fun saveFloodLightStatus(floodlightStatus: Boolean) {
        sharedPrefs.edit().putBoolean(FLOODLIGHT_STATUS_PREF_NAME, floodlightStatus).apply()
    }

    fun getExistingGooglePlaySubscriptionPrice(): String? {
        return sharedPrefs.getString(EXISTING_GOOGLE_PLAY_SUBSCRIPTION_PRICE, null)
    }

    fun setExistingGooglePlaySubscriptionPrice(existingGooglePlaySubscriptionPrice: String?) {
        sharedPrefs.edit().putString(EXISTING_GOOGLE_PLAY_SUBSCRIPTION_PRICE,
                existingGooglePlaySubscriptionPrice).apply()
    }

    fun getExistingGooglePlaySubscriptionId(): String? {
        return sharedPrefs.getString(EXISTING_GOOGLE_PLAY_SUBSCRIPTION_ID, null)
    }

    fun setExistingGooglePlaySubscriptionId(existingGooglePlaySubscriptionId: String?) {
        sharedPrefs.edit().putString(EXISTING_GOOGLE_PLAY_SUBSCRIPTION_ID, existingGooglePlaySubscriptionId).apply()
    }

    fun getExistingGooglePlaySubscriptionPurchaseToken(): String? {
        return sharedPrefs.getString(EXISTING_GOOGLE_PLAY_SUBSCRIPTION_PURCHASE_TOKEN, null)
    }

    fun setExistingGooglePlaySubscriptionPurchaseToken(existingGooglePlaySubscriptionPurchaseToken: String?) {
        sharedPrefs.edit().putString(EXISTING_GOOGLE_PLAY_SUBSCRIPTION_PURCHASE_TOKEN, existingGooglePlaySubscriptionPurchaseToken).apply()
    }

    fun isExistingGooglePlaySubscriptionSuspended(): Boolean {
        return sharedPrefs.getBoolean(EXISTING_GOOGLE_PLAY_SUBSCRIPTION_SUSPENDED, false)
    }

    fun setExistingGooglePlaySubscriptionSuspended(existingSubscriptionSuspended: Boolean) {
        sharedPrefs.edit().putBoolean(EXISTING_GOOGLE_PLAY_SUBSCRIPTION_SUSPENDED, existingSubscriptionSuspended).apply()
    }

    fun getActiveSubscriptionSku(): String? {
        return sharedPrefs.getString(ACTIVE_SUBSCRIPTION_SKU, null)
    }

    fun setActiveSubscriptionSku(subscriptionSku: String?) {
        sharedPrefs.edit().putString(ACTIVE_SUBSCRIPTION_SKU, subscriptionSku).apply()
    }

    fun getActiveSubscriptionCountryCode(): String? {
        return sharedPrefs.getString(ACTIVE_SUBSCRIPTION_COUNTRY_CODE, null)
    }

    fun setActiveSubscriptionCountryCode(countryCode: String?) {
        sharedPrefs.edit().putString(ACTIVE_SUBSCRIPTION_COUNTRY_CODE, countryCode).apply()
    }

    fun getActiveSubscriptionId(): String? {
        return sharedPrefs.getString(ACTIVE_SUBSCRIPTION_ID, null)
    }

    fun setActiveSubscriptionId(subscriptionId: String?) {
        sharedPrefs.edit().putString(ACTIVE_SUBSCRIPTION_ID, subscriptionId).apply()
    }

    fun getActiveSubscriptionCurrency(): String? {
        return sharedPrefs.getString(ACTIVE_SUBSCRIPTION_CURRENCY, null)
    }

    fun setActiveSubscriptionCurrency(subscriptionCurrency: String?) {
        sharedPrefs.edit().putString(ACTIVE_SUBSCRIPTION_CURRENCY, subscriptionCurrency).apply()
    }

    fun getUserFreePlayTimePreference(): Long {
        return sharedPrefs.getLong(USER_FREE_PLAY_TIME_SHARED_PREF_NAME, 0)
    }

    fun setUserFreePlayTimePreference(userFreePlayTime: Long) {
        sharedPrefs.edit().putLong(USER_FREE_PLAY_TIME_SHARED_PREF_NAME, userFreePlayTime).apply()
    }

    fun getExistingGooglePlaySubscriptionDescription(): String? {
        return sharedPrefs.getString(EXISTING_GOOGLE_PLAY_SUBSCRIPTION_DESCRIPTION, null)
    }

    fun setExistingGooglePlaySubscriptionDescription(existingGooglePlaySubscriptionDescription: String?) {
        sharedPrefs.edit().putString(EXISTING_GOOGLE_PLAY_SUBSCRIPTION_DESCRIPTION,
                existingGooglePlaySubscriptionDescription).apply()
    }

    fun getExistingSubscriptionPlanDescription(): String? {
        return sharedPrefs.getString(EXISTING_SUBSCRIPTION_PLAN_DESCRIPTION, null)
    }

    fun setExistingSubscriptionPlanDescription(existingSubscriptionPlanDescription: String?) {
        sharedPrefs.edit().putString(EXISTING_SUBSCRIPTION_PLAN_DESCRIPTION,
                existingSubscriptionPlanDescription).apply()
    }

    fun getExistingSubscriptionPlanName(): String? {
        return sharedPrefs.getString(EXISTING_SUBSCRIPTION_PLAN_NAME, null)
    }

    fun setExistingSubscriptionPlanName(existingSubscriptionPlanName: String?) {
        sharedPrefs.edit().putString(EXISTING_SUBSCRIPTION_PLAN_NAME,
                existingSubscriptionPlanName).apply()
    }

    fun setActiveSubscriptionPlanName(subscriptionPlanName: String?) {
        sharedPrefs.edit().putString(ACTIVE_SUBSCRIPTION_PLAN_NAME, subscriptionPlanName).apply()
    }

    fun setActiveSubscriptionTitle(subscriptionPlanTitle: String?) {
        sharedPrefs.edit().putString(ACTIVE_SUBSCRIPTION_PLAN_TITLE, subscriptionPlanTitle).apply()
    }

    fun getActiveSubscriptionEndDate(): String? {
        return sharedPrefs.getString(ACTIVE_SUBSCRIPTION_END_DATE, null)
    }

    fun setActiveSubscriptionEndDate(subscriptionEndDate: String?) {
        sharedPrefs.edit().putString(ACTIVE_SUBSCRIPTION_END_DATE, subscriptionEndDate).apply()
    }

    fun getActiveSubscriptionStartDate(): String? {
        return sharedPrefs.getString(ACTIVE_SUBSCRIPTION_START_DATE, null)
    }

    fun setActiveSubscriptionStartDate(subscriptionEndDate: String?) {
        sharedPrefs.edit().putString(ACTIVE_SUBSCRIPTION_START_DATE, subscriptionEndDate).apply()
    }

    fun getActiveSubscriptionStatus(): String? {
        return sharedPrefs.getString(ACTIVE_SUBSCRIPTION_STATUS, null)
    }

    fun setActiveSubscriptionStatus(subscriptionStatus: String?) {
        sharedPrefs.edit().putString(ACTIVE_SUBSCRIPTION_STATUS, subscriptionStatus).apply()
    }

    fun setIsSubscribedPlanRenewable(isSubscribedPlanRenewable: Boolean) {
        sharedPrefs.edit().putBoolean(IS_SUBSCRIBED_PLAN_RENEWABLE, isSubscribedPlanRenewable).apply()
    }

    fun isSubscribedPlanRenewable(): Boolean {
        return sharedPrefs.getBoolean(IS_SUBSCRIBED_PLAN_RENEWABLE, false)
    }

    fun getActiveSubscriptionPlatform(): String? {
        return sharedPrefs.getString(ACTIVE_SUBSCRIPTION_PLATFORM, null)
    }

    fun setActiveSubscriptionPlatform(platform: String?): Boolean {
        return sharedPrefs.edit().putString(ACTIVE_SUBSCRIPTION_PLATFORM, platform).commit()
    }

    fun getUserSubscriptionPlanTitle(): String? {
        return sharedPrefs.getString(USER_SUBSCRIPTION_PLAN_TILTE, null)
    }

    fun setUserSubscriptionPlanTitle(planTitle: String?) {
        sharedPrefs.edit().putString(USER_SUBSCRIPTION_PLAN_TILTE, planTitle).apply()
    }

    fun getActiveSubscriptionPrice(): String? {
        return sharedPrefs.getString(ACTIVE_SUBSCRIPTION_PRICE_NAME, null)
    }

    fun setActiveSubscriptionPrice(subscriptionPrice: String?) {
        sharedPrefs.edit().putString(ACTIVE_SUBSCRIPTION_PRICE_NAME, subscriptionPrice).apply()
    }

    fun getActiveSubscriptionPlanCycle(): String? {
        return sharedPrefs.getString(ACTIVE_SUBSCRIPTION_PLAN_CYCLE_TYPE, null)
    }

    fun setActiveSubscriptionPlanCycle(subscriptionPlanCycle: String?) {
        sharedPrefs.edit().putString(ACTIVE_SUBSCRIPTION_PLAN_CYCLE_TYPE, subscriptionPlanCycle).apply()
    }

    fun getActiveSubscriptionPriceCurrencyCode(): String? {
        return sharedPrefs.getString(ACTIVE_SUBSCRIPTION_PLAN_CURRENCY_CODE, null)
    }

    fun setActiveSubscriptionPriceCurrencyCode(subscriptionPrice: String?) {
        sharedPrefs.edit().putString(ACTIVE_SUBSCRIPTION_PLAN_CURRENCY_CODE, subscriptionPrice).apply()
    }

    fun getActiveSubscriptionPlanCyclePeriodMultiplier(): Int {
        return sharedPrefs.getInt(ACTIVE_SUBSCRIPTION_PLAN_CYCLE_PERIODMULTIPLIER, 0)
    }

    fun setActiveSubscriptionPlanCyclePeriodMultiplier(subscriptionPrice: Int) {
        sharedPrefs.edit().putInt(ACTIVE_SUBSCRIPTION_PLAN_CYCLE_PERIODMULTIPLIER, subscriptionPrice).apply()
    }

    fun setActiveSubscriptionProcessor(paymentProcessor: String?) {
        sharedPrefs.edit().putString(ACTIVE_SUBSCRIPTION_PROCESSOR_NAME, paymentProcessor).apply()
    }

    fun getRestoreSubscriptionReceipt(): String? {
        return sharedPrefs.getString(RESTORE_SUBSCRIPTION_RECEIPT, null)
    }

    fun setRestoreSubscriptionReceipt(subscriptionToken: String?) {
        sharedPrefs.edit().putString(RESTORE_SUBSCRIPTION_RECEIPT, subscriptionToken).apply()
    }

    fun getActiveSubscriptionReceipt(): String? {
        return sharedPrefs.getString(ACTIVE_SUBSCRIPTION_RECEIPT, null)
    }

    fun setActiveSubscriptionReceipt(subscriptionToken: String?) {
        sharedPrefs.edit().putString(ACTIVE_SUBSCRIPTION_RECEIPT, subscriptionToken).apply()
    }

    fun setIsTVAppLaunchTypeDeepLink(isTVAppLaunchTypeDeepLink: Boolean) {
        sharedPrefs.edit().putBoolean(APP_LAUNCHED_FROM_DEEPLINK_PREFS, isTVAppLaunchTypeDeepLink).apply()
    }

    fun getIsTVAppLaunchTypeDeepLink(): Boolean {
        return sharedPrefs.getBoolean(APP_LAUNCHED_FROM_DEEPLINK_PREFS, false)
    }

    fun setDeepLinkContentID(contentID: String?) {
        sharedPrefs.edit().putString(DEEPLINK_CONTENT_ID_PREFS, contentID).apply()
    }

    fun getDeepLinkContentID(): String? {
        return sharedPrefs.getString(DEEPLINK_CONTENT_ID_PREFS, null)
    }

    fun setPreferredSubtitleLanguage(language: String?) {
        sharedPrefs.edit().putString(SUBTITLE_LANGUAGE_PREFS, language).apply()
    }

    fun getPreferredSubtitleLanguage(): String? {
        return sharedPrefs.getString(SUBTITLE_LANGUAGE_PREFS, null)
    }

    fun setPreferredSubtitleTextSize(size: Float) {
        sharedPrefs.edit().putFloat(SUBTITLE_SIZE_PREFS, size).apply()
    }

    fun getPreferredSubtitleTextSize(defaultValue: Float): Float {
        return sharedPrefs.getFloat(SUBTITLE_SIZE_PREFS, defaultValue);
    }

    fun setPreferredAudioLanguage(language: String?) {
        sharedPrefs.edit().putString(AUDIO_LANGUAGE_PREFS, language).apply()
    }

    fun getPreferredAudioLanguage(): String? {
        return sharedPrefs.getString(AUDIO_LANGUAGE_PREFS, null)
    }

    fun getAppHomeActivityCreated(): Boolean {
        return sharedPrefs.getBoolean(IS_HOME_STARTED, false)
    }

    fun setAppHomeActivityCreated(isHomeCreated: Boolean) {
        sharedPrefs.edit().putBoolean(IS_HOME_STARTED, isHomeCreated).apply()
    }

    fun getAudioReload(): Boolean {
        return sharedPrefs.getBoolean(IS_AUDIO_RELOAD, false)
    }

    fun setAudioReload(isReload: Boolean) {
        sharedPrefs.edit().putBoolean(IS_AUDIO_RELOAD, isReload).apply()
    }

    fun saveLastPlaySongPosition(song: String?) {
        sharedPrefs.edit().putString(LAST_PLAY_SONG_DETAILS, song).apply()
    }

    fun getLastPlaySongPosition(): String? {
        return sharedPrefs.getString(LAST_PLAY_SONG_DETAILS, "")
    }

    fun getAppLastLaunchTime(): Long {
        return sharedPrefs.getLong(APP_LAST_LAUNCH_TIME, -1L)
    }

    fun setAppLastLaunchTime() {
        //val now = Date()
        // sharedPrefs.edit().putLong(APP_LAST_LAUNCH_TIME, now.time).apply()
        sharedPrefs.edit().putLong(APP_LAST_LAUNCH_TIME, getAppLaunchTime()).apply()
    }

    fun getAppLaunchTime(): Long {
        return sharedPrefs.getLong(APP_LAUNCH_TIME, -1L)
    }

    fun setAppLaunchTime() {
        val now = Date()
        sharedPrefs.edit().putLong(APP_LAUNCH_TIME, now.time).apply()
    }

    fun getLoggedInUser(): String? {
        return sharedPrefs.getString(USER_ID_SHARED_PREF_NAME, null)
    }

    fun getAuthToken(): String? {
        return sharedPrefs.getString(AUTH_TOKEN_SHARED_PREF_NAME, null)
    }

    @Deprecated("Not used , delete the function in future")
    fun getOldCalendarEventId(): String? {
        val sharedPrefs = context.getSharedPreferences(PREF_NAME_CALENDAR_EVENT_ID, 0)
        return sharedPrefs.getString(PREF_NAME_CALENDAR_EVENT_ID, null)
    }

    fun getCalendarEventId(): String? {
        return sharedPrefs.getString(PREF_NAME_CALENDAR_EVENT_ID, null)
    }

    fun setCalendarEventId(calendarId: Long) {
        var previousid: String? = ""
        if (getCalendarEventId() != null) {
            previousid = getCalendarEventId()
        }
        sharedPrefs.edit().putString(PREF_NAME_CALENDAR_EVENT_ID, "$previousid$calendarId;").apply()
    }

    @Deprecated("Not used , delete the function in future")
    private fun getLoggedInUserOldPreference(): String? {
        val sharedPrefs = context.getSharedPreferences(LOGIN_SHARED_PREF_NAME, 0)
        return sharedPrefs.getString(USER_ID_SHARED_PREF_NAME, null)
    }

    @Deprecated("Not used , delete the function in future")
    private fun getAuthTokenOldPreference(): String? {
        val sharedPrefs = context.getSharedPreferences(AUTH_TOKEN_SHARED_PREF_NAME, 0)
        return sharedPrefs.getString(AUTH_TOKEN_SHARED_PREF_NAME, null)
    }

    fun getLoggedInUserEmail(): String? {
        return sharedPrefs.getString(USER_EMAIL_SHARED_PREF_NAME, null)
    }

    fun getLoggedInUserPhoneCounntryCode(): String? {
        return sharedPrefs.getString(USER_PHONE_COUNTRY_CODE_SHARED_PREF_NAME, null)
    }

    @Deprecated("Not used , delete the function in future")
    private fun getLoggedInUserEmailOldPreference(): String? {
        val sharedPrefs = context.getSharedPreferences(USER_EMAIL_SHARED_PREF_NAME, 0)
        return sharedPrefs.getString(USER_EMAIL_SHARED_PREF_NAME, null)
    }

    fun getRefreshToken(): String? {
        return sharedPrefs.getString(REFRESH_TOKEN_SHARED_PREF_NAME, null)
    }

    @Deprecated("Not used , delete the function in future")
    fun getRefreshTokenOldPreference(): String? {
        val sharedPrefs = context.getSharedPreferences(REFRESH_TOKEN_SHARED_PREF_NAME, 0)
        return sharedPrefs.getString(REFRESH_TOKEN_SHARED_PREF_NAME, null)
    }

    @Deprecated("Not used , delete the function in future")
    private fun getLoggedInUserNameOldPreference(): String? {
        val sharedPrefs = context.getSharedPreferences(USER_NAME_SHARED_PREF_NAME, 0)
        return sharedPrefs.getString(USER_NAME_SHARED_PREF_NAME, null)
    }

    fun getLoggedInUserName(): String? {
        return sharedPrefs.getString(USER_NAME_SHARED_PREF_NAME, null)
    }

    @Deprecated("Not used , delete the function in future")
    private fun getActiveSubscriptionPlanNameOldPreference(): String? {
        val sharedPrefs = context.getSharedPreferences(ACTIVE_SUBSCRIPTION_PLAN_NAME, 0)
        return sharedPrefs.getString(ACTIVE_SUBSCRIPTION_PLAN_NAME, null)
    }

    fun getActiveSubscriptionPlanTitle(): String? {
        val sharedPrefs = context.getSharedPreferences(ACTIVE_SUBSCRIPTION_PLAN_TITLE, 0)
        return sharedPrefs.getString(ACTIVE_SUBSCRIPTION_PLAN_TITLE, null)
    }

    fun getActiveSubscriptionPlanName(): String? {
        return sharedPrefs.getString(ACTIVE_SUBSCRIPTION_PLAN_NAME, null)
    }

    @Deprecated("Not used , delete the function in future")
    private fun getActiveSubscriptionProcessorOldPreference(): String? {
        val sharedPrefs = context.getSharedPreferences(ACTIVE_SUBSCRIPTION_PROCESSOR_NAME, 0)
        return sharedPrefs.getString(ACTIVE_SUBSCRIPTION_PROCESSOR_NAME, null)
    }

    fun getActiveSubscriptionProcessor(): String? {
        return sharedPrefs.getString(ACTIVE_SUBSCRIPTION_PROCESSOR_NAME, null)
    }

    @Deprecated("Not used , delete the function in future")
    private fun clearOldPreference() {
        val loginPreference = context.getSharedPreferences(LOGIN_SHARED_PREF_NAME, Context.MODE_PRIVATE)
        val loginEditor = loginPreference.edit()
        loginEditor.clear()
        loginEditor.apply()
        val tokenPreference = context.getSharedPreferences(AUTH_TOKEN_SHARED_PREF_NAME, Context.MODE_PRIVATE)
        val tokenEditor = tokenPreference.edit()
        tokenEditor.clear()
        tokenEditor.apply()
        val emailPreference = context.getSharedPreferences(USER_EMAIL_SHARED_PREF_NAME, Context.MODE_PRIVATE)
        val emailEditor = emailPreference.edit()
        emailEditor.clear()
        emailEditor.apply()
        val usernamePreference = context.getSharedPreferences(USER_NAME_SHARED_PREF_NAME, Context.MODE_PRIVATE)
        val usernameEditor = usernamePreference.edit()
        usernameEditor.clear()
        usernameEditor.apply()
        val planNamePreference = context.getSharedPreferences(ACTIVE_SUBSCRIPTION_PLAN_NAME, Context.MODE_PRIVATE)
        val planNameEditor = planNamePreference.edit()
        planNameEditor.clear()
        planNameEditor.apply()
        val planProcessorPreference = context.getSharedPreferences(ACTIVE_SUBSCRIPTION_PROCESSOR_NAME, Context.MODE_PRIVATE)
        val planProcessorEditor = planProcessorPreference.edit()
        planProcessorEditor.clear()
        planProcessorEditor.apply()
        clearOldAuthAndRefreshToken()
    }

    @Deprecated("Not used , delete the function in future")
    fun clearOldAuthAndRefreshToken() {
        val authTokenPreference = context.getSharedPreferences(AUTH_TOKEN_SHARED_PREF_NAME, Context.MODE_PRIVATE)
        val authTokenEditor = authTokenPreference.edit()
        authTokenEditor.clear()
        authTokenEditor.apply()
        val refreshTokenPreference = context.getSharedPreferences(REFRESH_TOKEN_SHARED_PREF_NAME, Context.MODE_PRIVATE)
        val refreshTokenEditor = refreshTokenPreference.edit()
        refreshTokenEditor.clear()
        refreshTokenEditor.apply()
    }


    fun setPurchaseKey(purchaseKey: Boolean): Boolean {
        return sharedPrefs.edit().putBoolean(PURCHASE_KEY, purchaseKey).commit()
    }

    fun getPurchaseKey(): Boolean {
        return sharedPrefs.getBoolean(PURCHASE_KEY, false)
    }


    fun getleftnavigationKey(): String? {
        return sharedPrefs.getString(LEFT_NAV_KEY, null)
    }

    fun setleftnavigationKey(navigationKey: String?) {
        sharedPrefs.edit().putString(LEFT_NAV_KEY, navigationKey).apply()
    }

    fun setAppsflyerEventFirstAppOpenSent(sent: Boolean): Boolean {
        return sharedPrefs.edit().putBoolean(APPSFLYER_EVENT_FIRST_APP_OPEN_SENT, sent).commit()
    }

    fun getAppsflyerEventFirstAppOpenSent(): Boolean {
        return sharedPrefs.getBoolean(APPSFLYER_EVENT_FIRST_APP_OPEN_SENT, false)
    }

    fun setAppsflyerEventEngagedUserSent(sent: Boolean): Boolean {
        return sharedPrefs.edit().putBoolean(APPSFLYER_EVENT_ENGAGED_USER_SENT, sent).commit()
    }

    fun getAppsflyerEventEngagedUserSent(): Boolean {
        return sharedPrefs.getBoolean(APPSFLYER_EVENT_ENGAGED_USER_SENT, false)
    }

    fun setAppOpenCount(): Boolean {
        return sharedPrefs.edit().putInt(APP_OPEN_COUNT, getAppOpenCount() + 1).commit()
    }

    fun getAppOpenCount(): Int {
        return sharedPrefs.getInt(APP_OPEN_COUNT, 0)
    }

    fun setVideoWatchCount(): Boolean {
        return sharedPrefs.edit().putInt(VIDEO_WATCH_COUNT, getVideoWatchCount() + 1).commit()
    }

    fun getRatingDialogShownForVideoCount(): Boolean {
        return sharedPrefs.getBoolean(RATING_DIALOG_SHOWN_FOR_VIDEO_COUNT, false)
    }

    fun setRatingDialogShownForVideoCount(): Boolean {
        return sharedPrefs.edit().putBoolean(RATING_DIALOG_SHOWN_FOR_VIDEO_COUNT, true).commit()
    }

    fun getRatingDialogShownForAppOpen(): Boolean {
        return sharedPrefs.getBoolean(RATING_DIALOG_SHOWN_FOR_APP_OPEN, false)
    }

    fun setRatingDialogShownForAppOpen(): Boolean {
        return sharedPrefs.edit().putBoolean(RATING_DIALOG_SHOWN_FOR_APP_OPEN, true).commit()
    }

    fun getVideoWatchCount(): Int {
        return sharedPrefs.getInt(VIDEO_WATCH_COUNT, 0)
    }

    fun setRatingDialogShownTime(time: Long): Boolean {
        return sharedPrefs.edit().putLong(RATING_DIALOG_SHOWN_TIME, time).commit()
    }

    fun getRatingDialogShownTime(): Long {
        return sharedPrefs.getLong(RATING_DIALOG_SHOWN_TIME, 0)
    }

    /** Gets logged in TVE provider name
     * @return logged in TV provider name
     **/
    fun getTvProvider(): String? {
        return sharedPrefs.getString(TV_PROVIDER, null)
    }

    /** Sets logged in TVE provider name
     * @param providerName - logged in TV provider name
     **/
    fun setTvProvider(providerName: String?) {
        sharedPrefs.edit().putString(TV_PROVIDER, providerName).apply()
    }

    fun setVideoStreamingQuality(quality: String?) {
        if (!TextUtils.isEmpty(quality) && quality!!.endsWith("p")) {
            sharedPrefs.edit().putString(VIDEO_STREAMING_QUALITY, quality).apply()
        } else {
            sharedPrefs.edit().remove(VIDEO_STREAMING_QUALITY).apply()
        }
    }

    fun getVideoStreamingQuality(): String? {
        return sharedPrefs.getString(VIDEO_STREAMING_QUALITY, "")
    }

    fun setParentalPin(parentalPin: String?) {
        sharedPrefs.edit().putString(PARENTAL_PIN, parentalPin).apply()
    }

    fun getParentalPin(): String? {
        return sharedPrefs.getString(PARENTAL_PIN, "")
    }

    fun setUADeepLink(deeplink: String?) {
        sharedPrefs.edit().putString(UA_DEEP_LINK, deeplink).apply()
    }

    fun getUADeepLink(): String? {
        return sharedPrefs.getString(UA_DEEP_LINK, "")
    }

    fun setParentalRating(parentalRating: String?) {
        sharedPrefs.edit().putString(PARENTAL_RATING, parentalRating).apply()
    }

    fun isParentalControlsEnable(): Boolean {
        return sharedPrefs.getBoolean(PARENTAL_CONTROLS, false)
    }

    fun setParentalControlsEnable(isEnable: Boolean) {
        sharedPrefs.edit().putBoolean(PARENTAL_CONTROLS, isEnable).apply()
    }

    fun getParentalRating(): String? {
        return sharedPrefs.getString(PARENTAL_RATING, "")
    }

    fun isBiometricPinEnable(): Boolean {
        return sharedPrefs.getBoolean(BIO_METRIC_PIN, false)
    }

    fun setBiometricPinEnable(isEnable: Boolean) {
        sharedPrefs.edit().putBoolean(BIO_METRIC_PIN, isEnable).apply()
    }

    fun setLoginType(loginType: String?) {
        sharedPrefs.edit().putString(LOGIN_TYPE, loginType).apply()
    }

    fun getLoginType(): String? {
        return sharedPrefs.getString(LOGIN_TYPE, "")
    }


    fun setRegistrationType(regType: String?) {
        sharedPrefs.edit().putString(PREF_REGISTRATION_TYPE, regType).apply()
    }

    fun getRegistrationType(): String? {
        return sharedPrefs.getString(PREF_REGISTRATION_TYPE, null)
    }

    fun setCoupanCode(source: String?) {
        sharedPrefs.edit().putString(PREF_COUPAN_CODE, source).apply()
    }

    fun getCoupanCode(): String? {
        return sharedPrefs.getString(PREF_COUPAN_CODE, null)
    }

    /** Sets TVE user ID
     * @param id - TVE user ID
     **/
    fun setTVEUserId(id: String?): Boolean {
        return sharedPrefs.edit().putString(TVE_USER_ID, id).commit()
    }

    /** gets TVE user ID
     * @return TVE user ID
     **/
    fun getTVEUserId(): String? {
        return sharedPrefs.getString(TVE_USER_ID, null)
    }

    /** Gets logged in TVE provider logo URL
     * @return Logo url for logged in TV provider
     **/
    fun getTvProviderLogo(): String? {
        return sharedPrefs.getString(TV_PROVIDER_LOGO, null)
    }

    /** Sets logged in TVE provider logo URL
     * @param imgUrl - Logo url for logged in TV provider
     **/
    fun setTvProviderLogo(imgUrl: String?) {
        sharedPrefs.edit().putString(TV_PROVIDER_LOGO, imgUrl).apply()
    }

    /** Sets logged in TVE provider name
     * @param name - TVE provider name
     **/
    fun setTvProviderName(name: String?) {
        sharedPrefs.edit().putString(TV_PROVIDER_NAME, name).apply()
    }

    /** Gets logged in TVE provider name
     * @return TVE provider name
     **/
    fun getTvProviderName(): String? {
        return sharedPrefs.getString(TV_PROVIDER_NAME, null)
    }

    fun setTVEUserLoggedInTime() {
        val now = Date()
        sharedPrefs.edit().putLong(TVE_USER_LOGGED_IN_TIME, now.time).apply()
    }

    fun getTVEUserLoggedInTime(): Long {
        return sharedPrefs.getLong(TVE_USER_LOGGED_IN_TIME, -1L)
    }

    /** Sets download to be allowed for user on basis of subscribed plan
     * @param allowed - download allowed
     **/
    fun setUserAllowedDownload(allowed: Boolean) {
        sharedPrefs.edit().putBoolean(USER_ALLOWED_DOWNLOAD, allowed).apply()
    }

    /** Gets download to be allowed for user on basis of subscribed plan
     * @return download allowed
     **/
    fun isUserAllowedDownload(): Boolean {
        return sharedPrefs.getBoolean(USER_ALLOWED_DOWNLOAD, true)
    }

    /** Sets casting to be allowed for user on basis of subscribed plan
     * @param allowed - casting allowed
     **/
    fun setUserAllowedCasting(allowed: Boolean) {
        sharedPrefs.edit().putBoolean(USER_ALLOWED_CASTING, allowed).apply()
    }

    /** Gets casting to be allowed for user on basis of subscribed plan
     * @return casting allowed
     **/
    fun isUserAllowedCasting(): Boolean {
        return sharedPrefs.getBoolean(USER_ALLOWED_CASTING, true)
    }

    /**
     * this is only for send device Activated Event on CleverTap
     */
    fun isDeviceActivated(): Boolean {
        return sharedPrefs.getBoolean(TV_DEVICE_ACTIVATED, false)
    }

    /**
     * this is only for send device Activated Event on CleverTap
     */
    fun setDeviceActivated(isActivated: Boolean) {
        sharedPrefs.edit().putBoolean(TV_DEVICE_ACTIVATED, isActivated).apply()
    }

    /** Sets HD streaming to be allowed above and equal to 720p to user on basis of subscribed plan
     * @param allowed - HD streaming allowed
     **/
    fun setUserAllowedHDStreaming(allowed: Boolean) {
        sharedPrefs.edit().putBoolean(USER_ALLOWED_HD_STREAMING, allowed).apply()
    }

    /** Gets HD streaming to be allowed above and equal to 720p to user on basis of subscribed plan
     * @return HD streaming allowed
     **/
    fun isUserAllowedHDStreaming(): Boolean {
        return sharedPrefs.getBoolean(USER_ALLOWED_HD_STREAMING, true)
    }

    fun isbrowserLocalStorage(): Boolean {
        return sharedPrefs.getBoolean(BROWSER_STORAGE, false)
    }

    fun setbrowserLocalStorage(isEnable: Boolean) {
        sharedPrefs.edit().putBoolean(BROWSER_STORAGE, isEnable).apply()
    }

    fun isIsbrowserDataAlreadyOpen(): Boolean {
        return sharedPrefs.getBoolean(BROWSER_OPEN, false)
    }

    fun setIsbrowserDataAlreadyOpen(isEnable: Boolean) {
        sharedPrefs.edit().putBoolean(BROWSER_OPEN, isEnable).apply()
    }


    fun setGoogleUserId(googleUserId: String?) {
        sharedPrefs.edit().putString(GOOGLE_USER_ID, googleUserId).apply()
    }

    fun getGoogleUserId(): String? {
        return sharedPrefs.getString(GOOGLE_USER_ID, null)
    }

    fun setFacebookUserId(facebookUserId: String?) {
        sharedPrefs.edit().putString(FACEBOOK_USER_ID, facebookUserId).apply()
    }

    fun getFacebookUserId(): String? {
        return sharedPrefs.getString(FACEBOOK_USER_ID, null)
    }

    fun setShowPIPVisibility(previewStatus: Boolean) {
        sharedPrefs.edit().putBoolean(IS_SHOW_PIP_MODE, previewStatus).apply()
    }

    fun setUserPersonalizedGenres(userPersonalizedGenres: String?) {
        sharedPrefs.edit().putString(USER_PERSONALIZED_GENRES, userPersonalizedGenres).apply()
    }

    fun getUserPersonalizedGenres(): String? {
        return sharedPrefs.getString(USER_PERSONALIZED_GENRES, null)
    }

    fun getShowPIPVisibility(): Boolean {
        return sharedPrefs.getBoolean(IS_SHOW_PIP_MODE, true)
    }

    fun setPlay_Store_Country_Code(play_Store_Country_Code: String?) {
        sharedPrefs.edit().putString(Play_Store_Country_Code, play_Store_Country_Code).apply()
    }

    fun getPlay_Store_Country_Code(): String? {
        return sharedPrefs.getString(Play_Store_Country_Code, null)
    }

    /** Sets user purchases
     * @param purchase - list of user's TVOD purchases
     **/
    fun setUserPurchases(purchase: List<UserPurchases>) {
        var purchases = ""
        if (purchase.size != 0) {
            purchases = Gson().toJson(purchase)
        }
        sharedPrefs.edit().putString(USER_PURCHASES, purchases).apply()
    }

    /** Gets user purchases
     * @return user's TVOD purchases
     **/
    fun getUserPurchases(): String? {
        return sharedPrefs.getString(USER_PURCHASES, null)
    }

    fun setActiveSubscriptionPaymentOperator(paymentOperator: String?) {
        sharedPrefs.edit().putString(ACTIVE_SUBSCRIPTION_PAYMENT_OPERATOR, paymentOperator).apply()
    }

    fun getActiveSubscriptionPaymentOperator(): String {
        return sharedPrefs.getString(ACTIVE_SUBSCRIPTION_PAYMENT_OPERATOR, "") ?: ""
    }

    fun setPersonalizationScreenShown(shown: Boolean){
        sharedPrefs.edit().putBoolean(PERSONALIZATION_SCREEN_SHOWN, shown).apply()
    }

    fun isPersonalizationScreenShown(): Boolean {
        return sharedPrefs.getBoolean(PERSONALIZATION_SCREEN_SHOWN, false)
    }


    fun clear() {
        setDownloadPageId(null)
        setLoggedInUserName(null)
        setTVEUserId(null)
        setTvProviderLogo(null)
        setLoggedInUserEmail(null)
        setTVEUserId(null)
        setLoggedInUserPassword(null)
        setLoggedInUserPhone(null)
        setFreshchatPhone(null)
        setFreshchatPhoneCountryCode(null)
        setCheckOutPhoneNumber(null)
        setActiveSubscriptionReceipt(null)
        setRefreshToken(null)
        setAuthToken(null)
        setAnonymousUserToken(null)
        setIsUserSubscribed(false)
        setExistingGooglePlaySubscriptionId(null)
        setExistingGooglePlaySubscriptionPurchaseToken(null)
        setActiveSubscriptionProcessor(null)
        setActiveSubscriptionPaymentOperator(null)
        setRestoreSubscriptionReceipt(null)
        setFacebookAccessToken(null)
        setGoogleAccessToken(null)
        setUserAuthProviderName(null)
        setVideoStreamingQuality(null)
        setParentalPin(null)
        setParentalRating(null)
        setParentalControlsEnable(false)
        setBiometricPinEnable(false)
        setUserAllowedCasting(true)
        setUserAllowedDownload(true)
        setUserAllowedHDStreaming(true)
        setLoginType(null)
        setGoogleUserId(null)
        setFacebookUserId(null)
        setUserPurchases(listOf())
        clearOldPreference()
        setWhatsappChecked(false)
        setUserPersonalizedGenres(null)
        setPersonalizationScreenShown(false)
        clearSubscriptionDetails()
    }

    fun clearSubscriptionDetails() {
        setActiveSubscriptionPlatform(null)
        setActiveSubscriptionSku(null)
        setActiveSubscriptionCountryCode(null)
        setActiveSubscriptionId(null)
        setActiveSubscriptionProcessor(null)
        setActiveSubscriptionPaymentOperator(null)
        setActiveSubscriptionEndDate(null)
        setActiveSubscriptionStartDate(null)
        setActiveSubscriptionPlanName(null)
        setActiveSubscriptionPrice(null)
        setActiveSubscriptionPriceCurrencyCode(null)
        setActiveSubscriptionPlanCycle(null)
        setActiveSubscriptionPlanCyclePeriodMultiplier(0)
        setUserSubscriptionPlanTitle(null)
        setActiveSubscriptionStatus(null)
        setExistingSubscriptionPlanDescription(null)
        setExistingSubscriptionPlanName(null);
    }

}
