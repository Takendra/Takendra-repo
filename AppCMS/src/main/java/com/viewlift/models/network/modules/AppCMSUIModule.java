package com.viewlift.models.network.modules;

/**
 * Created by viewlift on 5/4/17.
 */

import android.content.Context;
import android.content.res.AssetManager;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.viewlift.R;
import com.viewlift.Utils;
import com.viewlift.analytics.CleverTapSDK;
import com.viewlift.analytics.FacebookAnalytics;
import com.viewlift.calendar.AppCalendarEvent;
import com.viewlift.db.AppPreference;
import com.viewlift.models.data.appcms.api.AppCMSPageAPI;
import com.viewlift.models.data.appcms.ui.AppCMSUIKeyType;
import com.viewlift.models.data.appcms.ui.main.LocalisedStrings;
import com.viewlift.models.data.appcms.ui.page.AppCMSPageUI;
import com.viewlift.models.network.rest.AppCMSAddToWatchlistRest;
import com.viewlift.models.network.rest.AppCMSAndroidModuleCall;
import com.viewlift.models.network.rest.AppCMSAndroidModuleRest;
import com.viewlift.models.network.rest.AppCMSAndroidUICall;
import com.viewlift.models.network.rest.AppCMSAndroidUIRest;
import com.viewlift.models.network.rest.AppCMSAnonymousAuthTokenCall;
import com.viewlift.models.network.rest.AppCMSAnonymousAuthTokenRest;
import com.viewlift.models.network.rest.AppCMSArticleCall;
import com.viewlift.models.network.rest.AppCMSArticleRest;
import com.viewlift.models.network.rest.AppCMSAudioDetailCall;
import com.viewlift.models.network.rest.AppCMSAudioDetailRest;
import com.viewlift.models.network.rest.AppCMSBeaconRest;
import com.viewlift.models.network.rest.AppCMSBillingHistoryCall;
import com.viewlift.models.network.rest.AppCMSBillingHistoryRest;
import com.viewlift.models.network.rest.AppCMSCCAvenueCall;
import com.viewlift.models.network.rest.AppCMSCCAvenueRSAKeyCall;
import com.viewlift.models.network.rest.AppCMSCCAvenueRSAKeyRest;
import com.viewlift.models.network.rest.AppCMSCCAvenueRest;
import com.viewlift.models.network.rest.AppCMSCarrierBillingCall;
import com.viewlift.models.network.rest.AppCMSDeleteHistoryRest;
import com.viewlift.models.network.rest.AppCMSEmailConsentCall;
import com.viewlift.models.network.rest.AppCMSEmailConsentRest;
import com.viewlift.models.network.rest.AppCMSEventArchieveCall;
import com.viewlift.models.network.rest.AppCMSEventArchieveRest;
import com.viewlift.models.network.rest.AppCMSFacebookLoginCall;
import com.viewlift.models.network.rest.AppCMSFacebookLoginRest;
import com.viewlift.models.network.rest.AppCMSGoogleLoginCall;
import com.viewlift.models.network.rest.AppCMSGoogleLoginRest;
import com.viewlift.models.network.rest.AppCMSHistoryCall;
import com.viewlift.models.network.rest.AppCMSHistoryRest;
import com.viewlift.models.network.rest.AppCMSIPGeoLocatorCall;
import com.viewlift.models.network.rest.AppCMSIPGeoLocatorRest;
import com.viewlift.models.network.rest.AppCMSJuspayRest;
import com.viewlift.models.network.rest.AppCMSLibraryRest;
import com.viewlift.models.network.rest.AppCMSLocationRest;
import com.viewlift.models.network.rest.AppCMSMainUICall;
import com.viewlift.models.network.rest.AppCMSMainUIRest;
import com.viewlift.models.network.rest.AppCMSPageUICall;
import com.viewlift.models.network.rest.AppCMSPageUIRest;
import com.viewlift.models.network.rest.AppCMSParentalRatingMapRest;
import com.viewlift.models.network.rest.AppCMSPhotoGalleryCall;
import com.viewlift.models.network.rest.AppCMSPhotoGalleryRest;
import com.viewlift.models.network.rest.AppCMSPlaylistCall;
import com.viewlift.models.network.rest.AppCMSPlaylistRest;
import com.viewlift.models.network.rest.AppCMSRedeemCall;
import com.viewlift.models.network.rest.AppCMSRedeemRest;
import com.viewlift.models.network.rest.AppCMSRefreshIdentityCall;
import com.viewlift.models.network.rest.AppCMSRefreshIdentityRest;
import com.viewlift.models.network.rest.AppCMSResetPasswordCall;
import com.viewlift.models.network.rest.AppCMSResetPasswordRest;
import com.viewlift.models.network.rest.AppCMSResourceCall;
import com.viewlift.models.network.rest.AppCMSRestorePurchaseCall;
import com.viewlift.models.network.rest.AppCMSRestorePurchaseRest;
import com.viewlift.models.network.rest.AppCMSRosterRest;
import com.viewlift.models.network.rest.AppCMSSSLCommerzInitiateCall;
import com.viewlift.models.network.rest.AppCMSSSLCommerzInitiateRest;
import com.viewlift.models.network.rest.AppCMSScheduleRest;
import com.viewlift.models.network.rest.AppCMSSignInCall;
import com.viewlift.models.network.rest.AppCMSSignInRest;
import com.viewlift.models.network.rest.AppCMSSignedURLCall;
import com.viewlift.models.network.rest.AppCMSSignedURLRest;
import com.viewlift.models.network.rest.AppCMSSubscribeForLatestNewsCall;
import com.viewlift.models.network.rest.AppCMSSubscribeForLatestNewsRest;
import com.viewlift.models.network.rest.AppCMSSubscriptionPlanCall;
import com.viewlift.models.network.rest.AppCMSSubscriptionPlanRest;
import com.viewlift.models.network.rest.AppCMSSubscriptionRest;
import com.viewlift.models.network.rest.AppCMSTeamRoasterCall;
import com.viewlift.models.network.rest.AppCMSTeamRoasterRest;
import com.viewlift.models.network.rest.AppCMSTeamStandingCall;
import com.viewlift.models.network.rest.AppCMSTeamStandingRest;
import com.viewlift.models.network.rest.AppCMSUpdateWatchHistoryCall;
import com.viewlift.models.network.rest.AppCMSUpdateWatchHistoryRest;
import com.viewlift.models.network.rest.AppCMSUserDownloadVideoStatusCall;
import com.viewlift.models.network.rest.AppCMSUserIdentityCall;
import com.viewlift.models.network.rest.AppCMSUserIdentityRest;
import com.viewlift.models.network.rest.AppCMSUserVideoStatusCall;
import com.viewlift.models.network.rest.AppCMSUserVideoStatusRest;
import com.viewlift.models.network.rest.AppCMSWatchlistCall;
import com.viewlift.models.network.rest.AppCMSWatchlistRest;
import com.viewlift.models.network.rest.AppCMSWeatherFeedCall;
import com.viewlift.models.network.rest.AppCMSWeatherFeedRest;
import com.viewlift.models.network.rest.GetUserRecommendGenreCall;
import com.viewlift.models.network.rest.GetUserRecommendGenreRest;
import com.viewlift.models.network.rest.GoogleCancelSubscriptionCall;
import com.viewlift.models.network.rest.GoogleCancelSubscriptionRest;
import com.viewlift.models.network.rest.GoogleRefreshTokenCall;
import com.viewlift.models.network.rest.GoogleRefreshTokenRest;
import com.viewlift.models.network.rest.VerimatrixCall;
import com.viewlift.models.network.rest.VerimatrixRest;
import com.viewlift.offlinedrm.OfflineVideoStatusCall;
import com.viewlift.presenters.AppCMSActionType;
import com.viewlift.stag.generated.Stag;

import org.simpleframework.xml.convert.AnnotationStrategy;
import org.simpleframework.xml.core.Persister;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.Cache;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.simplexml.SimpleXmlConverterFactory;

@Module
public class AppCMSUIModule {
    private final String baseUrl;
    private final File storageDirectory;
    private final Map<String, AppCMSUIKeyType> jsonValueKeyMap;
    private final Map<String, String> pageNameToActionMap;
    private final Map<String, AppCMSPageUI> actionToPageMap;
    private final Map<String, AppCMSPageAPI> actionToPageAPIMap;
    private final Map<String, AppCMSActionType> actionToActionTypeMap;
    private final long defaultConnectionTimeout;
    private final long defaultWriteConnectionTimeout;
    private final long defaultReadConnectionTimeout;
    private final long unknownHostExceptionTimeout;
    private final Cache cache;
    private final AssetManager assetManager;
    private final Context context;

    public AppCMSUIModule(Context context) {
        this.context = context;
        this.baseUrl = Utils.getProperty("BaseUrl", context);

        // NOTE: Replaced with Utils.getProperty()
        //this.baseUrl = context.getString(R.string.app_cms_baseurl);

        this.storageDirectory = context.getFilesDir();

        this.jsonValueKeyMap = new HashMap<>();
        createJsonValueKeyMap(context);

        this.pageNameToActionMap = new HashMap<>();
        createPageNameToActionMap(context);

        this.actionToPageMap = new HashMap<>();
        createActionToPageMap(context);

        this.actionToActionTypeMap = new HashMap<>();
        createActionToActionTypeMap(context);

        this.actionToPageAPIMap = new HashMap<>();
        createActionToPageAPIMap(context);

        this.defaultConnectionTimeout =
                context.getResources().getInteger(R.integer.app_cms_default_connection_timeout_msec);

        this.defaultWriteConnectionTimeout =
                context.getResources().getInteger(R.integer.app_cms_default_write_timeout_msec);

        this.defaultReadConnectionTimeout =
                context.getResources().getInteger(R.integer.app_cms_default_read_timeout_msec);

        this.unknownHostExceptionTimeout =
                context.getResources().getInteger(R.integer.app_cms_unknown_host_exception_connection_timeout_msec);
        int cacheSize = 10 * 1024 * 1024; // 10 MB
        cache = new Cache(context.getCacheDir(), cacheSize);

        this.assetManager = context.getAssets();
    }

    @Provides
    @Singleton
    public CleverTapSDK providesCleverTapSDK(AppPreference appPreference) {
        return new CleverTapSDK(context, appPreference);
    }

    @Provides
    @Singleton
    public LocalisedStrings providesLocalisedStrings() {
        return new LocalisedStrings(context);
    }


    @Provides
    public AppCalendarEvent providesAppPermissions() {
        return new AppCalendarEvent(context);
    }

    @Provides
    @Singleton
    public FacebookAnalytics providesFaceookSDK(AppPreference appPreference) {
        return new FacebookAnalytics(context, appPreference);
    }

    @Provides
    @Singleton
    public AppPreference providesAppPreference() {
        return new AppPreference(context);
    }


    private void createJsonValueKeyMap(Context context) {
        jsonValueKeyMap.put(context.getString(R.string.ui_block_tray_01), AppCMSUIKeyType.UI_BLOCK_TRAY_01);
        jsonValueKeyMap.put(context.getString(R.string.ui_block_tray_02), AppCMSUIKeyType.UI_BLOCK_TRAY_02);
        jsonValueKeyMap.put(context.getString(R.string.ui_block_tray_11), AppCMSUIKeyType.UI_BLOCK_TRAY_11);
        jsonValueKeyMap.put(context.getString(R.string.ui_block_news_tray_02), AppCMSUIKeyType.UI_BLOCK_NEWS_TRAY_02);
        jsonValueKeyMap.put(context.getString(R.string.ui_block_bundleDetail_01), AppCMSUIKeyType.UI_BLOCK_BUNDLE_DETAIL_01);
        jsonValueKeyMap.put(context.getString(R.string.ui_block_bundleDetail_02), AppCMSUIKeyType.UI_BLOCK_BUNDLE_DETAIL_02);

        jsonValueKeyMap.put(context.getString(R.string.ui_block_search_01), AppCMSUIKeyType.UI_BLOCK_SEARCH_01);
        jsonValueKeyMap.put(context.getString(R.string.ui_block_search_04), AppCMSUIKeyType.UI_BLOCK_SEARCH_04);

        jsonValueKeyMap.put(context.getString(R.string.ui_block_selectplan_01), AppCMSUIKeyType.UI_BLOCK_SELECTPLAN_01);
        jsonValueKeyMap.put(context.getString(R.string.ui_block_selectplan_02), AppCMSUIKeyType.UI_BLOCK_SELECTPLAN_02);
        jsonValueKeyMap.put(context.getString(R.string.ui_block_selectplan_03), AppCMSUIKeyType.UI_BLOCK_SELECTPLAN_03);
        jsonValueKeyMap.put(context.getString(R.string.ui_block_selectplan_04), AppCMSUIKeyType.UI_BLOCK_SELECTPLAN_04);
        jsonValueKeyMap.put(context.getString(R.string.ui_block_selectplan_06), AppCMSUIKeyType.UI_BLOCK_SELECTPLAN_06);
        jsonValueKeyMap.put(context.getString(R.string.ui_block_contentBlock_01), AppCMSUIKeyType.UI_BLOCK_CONTENT_BLOCK_01);
        jsonValueKeyMap.put(context.getString(R.string.ui_block_authentication_01), AppCMSUIKeyType.UI_BLOCK_AUTHENTICATION_01);
        jsonValueKeyMap.put(context.getString(R.string.ui_block_authentication_02), AppCMSUIKeyType.UI_BLOCK_AUTHENTICATION_02);
        jsonValueKeyMap.put(context.getString(R.string.ui_block_authentication_05), AppCMSUIKeyType.UI_BLOCK_AUTHENTICATION_05);
        jsonValueKeyMap.put(context.getString(R.string.ui_block_authentication_14), AppCMSUIKeyType.UI_BLOCK_AUTHENTICATION_14);
        jsonValueKeyMap.put(context.getString(R.string.ui_block_reset_password_01), AppCMSUIKeyType.UI_BLOCK_RESET_PASSWORD_01);
        jsonValueKeyMap.put(context.getString(R.string.ui_block_resetPassword_03), AppCMSUIKeyType.UI_BLOCK_RESET_PASSWORD_03);

        jsonValueKeyMap.put(context.getString(R.string.ui_block_profile_02), AppCMSUIKeyType.UI_BLOCK_PROFILE_02);
        jsonValueKeyMap.put(context.getString(R.string.ui_block_profile_03), AppCMSUIKeyType.UI_BLOCK_PROFILE_03);

        jsonValueKeyMap.put(context.getString(R.string.ui_block_user_management_01), AppCMSUIKeyType.UI_BLOCK_USER_MANAGEMENT_01);
        jsonValueKeyMap.put(context.getString(R.string.ui_block_user_management_02), AppCMSUIKeyType.UI_BLOCK_USER_MANAGEMENT_02);
        jsonValueKeyMap.put(context.getString(R.string.ui_block_user_management_03), AppCMSUIKeyType.UI_BLOCK_USER_MANAGEMENT_03);
        jsonValueKeyMap.put(context.getString(R.string.ui_block_user_management_04), AppCMSUIKeyType.UI_BLOCK_USER_MANAGEMENT_04);
        jsonValueKeyMap.put(context.getString(R.string.ui_block_user_management_05), AppCMSUIKeyType.UI_BLOCK_USER_MANAGEMENT_05);

        jsonValueKeyMap.put(context.getString(R.string.ui_block_history_01), AppCMSUIKeyType.UI_BLOCK_HISTORY_01);
        jsonValueKeyMap.put(context.getString(R.string.ui_block_history_02), AppCMSUIKeyType.UI_BLOCK_HISTORY_02);
        jsonValueKeyMap.put(context.getString(R.string.ui_block_history_04), AppCMSUIKeyType.UI_BLOCK_HISTORY_04);

        jsonValueKeyMap.put(context.getString(R.string.ui_block_watchlist_01), AppCMSUIKeyType.UI_BLOCK_WATCHLIST_01);
        jsonValueKeyMap.put(context.getString(R.string.ui_block_watchlist_02), AppCMSUIKeyType.UI_BLOCK_WATCHLIST_02);
        jsonValueKeyMap.put(context.getString(R.string.ui_block_watchlist_03), AppCMSUIKeyType.UI_BLOCK_WATCHLIST_03);
        jsonValueKeyMap.put(context.getString(R.string.ui_block_watchlist_04), AppCMSUIKeyType.UI_BLOCK_WATCHLIST_04);

        jsonValueKeyMap.put(context.getString(R.string.ui_block_downloads_01), AppCMSUIKeyType.UI_BLOCK_DOWNLOADS_01);
        jsonValueKeyMap.put(context.getString(R.string.ui_block_downloads_03), AppCMSUIKeyType.UI_BLOCK_DOWNLOADS_03);
        jsonValueKeyMap.put(context.getString(R.string.ui_block_downloads_02), AppCMSUIKeyType.UI_BLOCK_DOWNLOADS_02);

        jsonValueKeyMap.put(context.getString(R.string.ui_block_showDetail_01), AppCMSUIKeyType.UI_BLOCK_SHOW_DETAIL_01);
        jsonValueKeyMap.put(context.getString(R.string.ui_block_showDetail_06), AppCMSUIKeyType.UI_BLOCK_SHOW_DETAIL_06);
        jsonValueKeyMap.put(context.getString(R.string.ui_block_showDetail_07), AppCMSUIKeyType.UI_BLOCK_SHOW_DETAIL_07);

        jsonValueKeyMap.put(context.getString(R.string.ui_block_referral_plans_01),AppCMSUIKeyType.UI_BLOCK_REFERRAL_PLANS_01);

        jsonValueKeyMap.put(context.getString(R.string.ui_block_grid_01), AppCMSUIKeyType.UI_BLOCK_GRID_01);

        jsonValueKeyMap.put(context.getString(R.string.ui_block_standalone_player_02), AppCMSUIKeyType.UI_BLOCK_STANDALONE_PLAYER_02);

        jsonValueKeyMap.put(context.getString(R.string.ui_block_carousel_07), AppCMSUIKeyType.UI_BLOCK_CAROUSEL_07);
        jsonValueKeyMap.put(context.getString(R.string.ui_block_event_carousel_02), AppCMSUIKeyType.UI_BLOCK_EVENT_CAROUSEL_02);

        jsonValueKeyMap.put(context.getString(R.string.ui_block_continueWatching_01), AppCMSUIKeyType.UI_BLOCK_CONTINUEWATCHING_01);
        jsonValueKeyMap.put(context.getString(R.string.ui_block_continueWatching_02), AppCMSUIKeyType.UI_BLOCK_CONTINUEWATCHING_02);

        jsonValueKeyMap.put(context.getString(R.string.ui_block_recommendation_01), AppCMSUIKeyType.UI_BLOCK_RECOMENDATION_01);
        jsonValueKeyMap.put(context.getString(R.string.ui_block_news_recommendation_01), AppCMSUIKeyType.UI_BLOCK_NEWS_RECOMENDATION_01);
        jsonValueKeyMap.put(context.getString(R.string.ui_block_userPersonalization_01), AppCMSUIKeyType.UI_BLOCK_USER_PERSONALIZATION_01);
        jsonValueKeyMap.put(context.getString(R.string.ui_block_recommendPopular_01), AppCMSUIKeyType.UI_BLOCK_RECOMENDPOPULAR_01);

        jsonValueKeyMap.put(context.getString(R.string.ui_block_payment_01), AppCMSUIKeyType.UI_BLOCK_PAYMENT_01);
        jsonValueKeyMap.put(context.getString(R.string.ui_block_bankList_key), AppCMSUIKeyType.UI_BLOCK_BANK_LIST);
        jsonValueKeyMap.put(context.getString(R.string.ui_block_cardPayment_key), AppCMSUIKeyType.UI_BLOCK_CARD_PAYMENT_01);

        jsonValueKeyMap.put(context.getString(R.string.ui_block_autoplay_01), AppCMSUIKeyType.UI_BLOCK_AUTO_PLAY_01);
        jsonValueKeyMap.put(context.getString(R.string.ui_block_autoplay_02), AppCMSUIKeyType.UI_BLOCK_AUTO_PLAY_02);
        jsonValueKeyMap.put(context.getString(R.string.ui_block_autoplay_03), AppCMSUIKeyType.UI_BLOCK_AUTO_PLAY_03);
        jsonValueKeyMap.put(context.getString(R.string.ui_block_autoplay_04), AppCMSUIKeyType.UI_BLOCK_AUTO_PLAY_04);

        jsonValueKeyMap.put(context.getString(R.string.ui_block_filter_02), AppCMSUIKeyType.UI_BLOCK_FILTER_02);

        jsonValueKeyMap.put(context.getString(R.string.ui_block_playlist_01), AppCMSUIKeyType.UI_BLOCK_PLAYLIST);

        jsonValueKeyMap.put(context.getString(R.string.ui_block_pagelink_01), AppCMSUIKeyType.UI_BLOCK_PAGE_LINK_MODULE);
        jsonValueKeyMap.put(context.getString(R.string.ui_block_video_player_info_01), AppCMSUIKeyType.UI_BLOCK_VIDEO_PLAYER_INFO_01);


        jsonValueKeyMap.put(context.getString(R.string.categoryLayoutTray07), AppCMSUIKeyType.CATEGORY_TRAY_LAYOUT_07);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_more_feedback_title_key), AppCMSUIKeyType.PAGE_MORE_FEEDBACK_TITLE_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_more_privacy_policy_title_key), AppCMSUIKeyType.PAGE_MORE_PRIVACY_POLICY_TITLE_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_more_terms_of_service_title_key), AppCMSUIKeyType.PAGE_MORE_TERMS_OF_SERVICE_TITLE_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_more_about_neou_title_key), AppCMSUIKeyType.PAGE_MORE_ABOUT_NEOU_TITLE_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_more_browse_concepts_title_key), AppCMSUIKeyType.PAGE_MORE_BROWSE_CONCEPTS_TITLE_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_more_learn_title_key), AppCMSUIKeyType.PAGE_MORE_LEARN_TITLE_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_more_signOut_title_key), AppCMSUIKeyType.PAGE_MORE_SIGN_OUT_TITLE_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_more_subscriptions_title_key), AppCMSUIKeyType.PAGE_MORE_SUBSCRIPTIONS_TITLE_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_more_connected_devices_title_key), AppCMSUIKeyType.PAGE_MORE_CONNECTED_TITLE_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_more_settings_title_key), AppCMSUIKeyType.PAGE_MORE_ACCOUNT_SETTINGS_TITLE_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_more_account_title_key), AppCMSUIKeyType.PAGE_MORE_ACCOUNT_TITLE_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_searchBackButton_key), AppCMSUIKeyType.PAGE_SEARCH_BACK_BUTTON_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_searchResultLabel_key), AppCMSUIKeyType.PAGE_SEARCH_RESULT_LABEL_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_follow_empty_label_key), AppCMSUIKeyType.PAGE_FOLLOW_EMPTY_LABEL_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_thumbnailImageSearch_key), AppCMSUIKeyType.PAGE_SEARCH_THUMBNAIL_IMAGE_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_bookmarkSearch_key), AppCMSUIKeyType.PAGE_SEARCH_BOOKMARK_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_titleSearch_key), AppCMSUIKeyType.PAGE_SEARCH_TITLE_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_thumbnailBadgeImageSearch_key), AppCMSUIKeyType.PAGE_SEARCH_THUMBNAIL_BADGE_IMAGE_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_bookmarkFlagSearch_key), AppCMSUIKeyType.PAGE_SEARCH_BOOKMARK_FLAG_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_downloadButtonSearch_key), AppCMSUIKeyType.PAGE_SEARCH_DOWNLOAD_BUTTON_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_playImageSearch_key), AppCMSUIKeyType.PAGE_SEARCH_PLAY_IMAGE_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_descriptionSearch_key), AppCMSUIKeyType.PAGE_SEARCH_DESCRIPTION_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_gridSearchResult_key), AppCMSUIKeyType.PAGE_SEARCH_GRID_RESULT_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_profile_edit_image_key), AppCMSUIKeyType.PAGE_PROFILE_EDIT_IMAGE_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_profile_nameValue_key), AppCMSUIKeyType.PAGE_PROFILE_NAME_VALUE_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_profile_locationValue_key), AppCMSUIKeyType.PAGE_PROFILE_LOCATION_VALUE_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_profile_location_key), AppCMSUIKeyType.PAGE_PROFILE_LOCATION_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_right_arrow), AppCMSUIKeyType.PAGE_PROFILE_RIGHT_ARROW_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_profile_concepts), AppCMSUIKeyType.PAGE_PROFILE_CONCEPTS_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_profile_bookmarked), AppCMSUIKeyType.PAGE_PROFILE_BOOKMARKED_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_profile_history), AppCMSUIKeyType.PAGE_PROFILE_HISTORY_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_profile_title), AppCMSUIKeyType.PAGE_PROFILE_TITLE_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_edit_profile_exerciseValue), AppCMSUIKeyType.PAGE_EDIT_PROFILE_EXERCISE_VALUE_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_edit_profile_exerciseLabel), AppCMSUIKeyType.PAGE_EDIT_PROFILE_EXERCISE_LABEL_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_edit_profile_bodyTypeValue), AppCMSUIKeyType.PAGE_EDIT_PROFILE_BODY_TYPE_VALUE_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_edit_profile_bodyTypeLabel), AppCMSUIKeyType.PAGE_EDIT_PROFILE_BODY_TYPE_LABEL_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_edit_profile_fitnessGoalValue), AppCMSUIKeyType.PAGE_EDIT_PROFILE_FITNESS_GOAL_VALUE_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_edit_profile_fitnessGoalLabel), AppCMSUIKeyType.PAGE_EDIT_PROFILE_FITNESS_GOAL_LABEL_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_edit_profile_zipCodeValue), AppCMSUIKeyType.PAGE_EDIT_PROFILE_ZIPCODE_VALUE_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_edit_profile_zipCodeLabel), AppCMSUIKeyType.PAGE_EDIT_PROFILE_ZIPCODE_LABEL_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_edit_profile_birthdayValue), AppCMSUIKeyType.PAGE_EDIT_PROFILE_BIRTHDAY_VALUE_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_edit_profile_birthdayLabel), AppCMSUIKeyType.PAGE_EDIT_PROFILE_BIRTHDAY_LABEL_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_edit_profile_genderValue), AppCMSUIKeyType.PAGE_EDIT_PROFILE_GENDER_VALUE_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_edit_profile_genderLabel), AppCMSUIKeyType.PAGE_EDIT_PROFILE_GENDER_LABEL_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_edit_profile_firstName), AppCMSUIKeyType.PAGE_EDIT_PROFILE_FIRST_NAME_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_edit_profile_firstNameValue), AppCMSUIKeyType.PAGE_EDIT_PROFILE_FIRST_NAME_VALUE_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_edit_profile_lastNameProfile), AppCMSUIKeyType.PAGE_EDIT_PROFILE_LAST_NAME_PROFILE_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_edit_profile_lastNameValue), AppCMSUIKeyType.PAGE_EDIT_PROFILE_LAST_NAME_VALUE_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_edit_profile_chainBackground), AppCMSUIKeyType.PAGE_EDIT_PROFILE_CHAIN_BACKGROUND_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_edit_profile_locationValue), AppCMSUIKeyType.PAGE_EDIT_PROFILE_LOCATION_VALUE_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_edit_profile_location), AppCMSUIKeyType.PAGE_EDIT_PROFILE_LOCATION_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_edit_profile_chainPublicProfile), AppCMSUIKeyType.PAGE_EDIT_PROFILE_CHAIN_PUBLIC_PROFILE_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_edit_profile_save_button), AppCMSUIKeyType.PAGE_EDIT_PROFILE_SAVE_BUTTON_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_edit_profile_title), AppCMSUIKeyType.PAGE_EDIT_PROFILE_TITLE_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_edit_profile_camera_button), AppCMSUIKeyType.PAGE_EDIT_PROFILE_CAMERA_BUTTON_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_edit_profile_image), AppCMSUIKeyType.PAGE_EDIT_PROFILE_IMAGE_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_private_profile), AppCMSUIKeyType.PAGE_EDIT_PROFILE_PRIVATE_PROFILE_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_public_profile), AppCMSUIKeyType.PAGE_EDIT_PROFILE_PUBLIC_PROFILE_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_editProfileGrid), AppCMSUIKeyType.PAGE_EDIT_PROFILE_GRID_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_edit_profile_usernameValue), AppCMSUIKeyType.PAGE_EDIT_PROFILE_USERNAME_VALUE_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_edit_profile_username), AppCMSUIKeyType.PAGE_EDIT_PROFILE_USERNAME_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_brand_detail_page_expandableGrid_key), AppCMSUIKeyType.PAGE_BRAND_DETAIL_EXPANDABLE_GRID_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_browse_page_durationCarouselBrowse_key), AppCMSUIKeyType.PAGE_BROWSE_DURATION_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_browse_page_tagBrandTitleBrowse_key), AppCMSUIKeyType.PAGE_BROWSE_TAG_BRAND_TITLE_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_browse_page_tagClassTitleBrowse_key), AppCMSUIKeyType.PAGE_BROWSE_TAG_CLASS_TITLE_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_live_schedule_item_title_key), AppCMSUIKeyType.PAGE_LIVE_SCHEDULE_ITEM_TITLE_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_live_schedule_live_play_button_key), AppCMSUIKeyType.PAGE_LIVE_SCHEDULE_LIVE_PLAY_BUTTON_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_live_schedule_tag_brand_title_key), AppCMSUIKeyType.PAGE_LIVE_SCHEDULE_TAG_BRAND_TITLE_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_live_schedule_tag_class_title_key), AppCMSUIKeyType.PAGE_LIVE_SCHEDULE_TAG_CLASS_TITLE_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_browse_page_classesTitle_key), AppCMSUIKeyType.PAGE_BROWSE_CLASSES_TITLE_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_browse_page_conceptsTitle_key), AppCMSUIKeyType.PAGE_BROWSE_CONCEPTS_TITLE_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_browse_page_arrows_button_key), AppCMSUIKeyType.PAGE_BROWSE_ARROWS_BUTTON_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_browse_page_add_to_favorites_key), AppCMSUIKeyType.PAGE_BROWSE_ADD_TO_FAVORITES_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_browse_page_vod_play_button_key), AppCMSUIKeyType.PAGE_BROWSE_VOD_PLAY_BUTTON_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_browse_page_concepts_grid_key), AppCMSUIKeyType.PAGE_BROWSE_CONCEPTS_GRID_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_browse_page_browse_top_grid_key), AppCMSUIKeyType.PAGE_BROWSE_TOP_GRID_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_browse_page_class_image_key), AppCMSUIKeyType.PAGE_BROWSE_CLASS_IMAGE_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_browse_page_class_title_key), AppCMSUIKeyType.PAGE_BROWSE_CLASS_TITLE_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_browse_head_browse_all_grid_key), AppCMSUIKeyType.PAGE_BROWSE_ALL_GRID_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_browse_page_total_num_text_key), AppCMSUIKeyType.PAGE_BROWSE_TOTAL_NUM_TEXT_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_browse_page_all_text_key), AppCMSUIKeyType.PAGE_BROWSE_ALL_TEXT_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_browse_page_view_text_key), AppCMSUIKeyType.PAGE_BROWSE_VIEW_TEXT_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_splash_lets_go_button_key), AppCMSUIKeyType.PAGE_SPLASH_LETS_GO_BUTTON_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_schedule_month_key), AppCMSUIKeyType.PAGE_SCHEDULE_MONTH_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_home_live_and_upcoming_key), AppCMSUIKeyType.PAGE_HOME_LIVE_AND_UPCOMING);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_home_duration_carousel_key), AppCMSUIKeyType.PAGE_HOME_DURATION_CAROUSEL);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_home_title_thumbnail_key), AppCMSUIKeyType.PAGE_HOME_TITLE_THUMBNAIL);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_home_title_description_key), AppCMSUIKeyType.PAGE_HOME_TITLE_DESCRIPTION);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_tray_thumbnail_play_key), AppCMSUIKeyType.PAGE_HOME_TRAY_THUMBNAIL_PLAY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_vod_thumbnail_play_key), AppCMSUIKeyType.PAGE_HOME_VOD_THUMBNAIL_PLAY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_play_first_episode_first_season_button_key), AppCMSUIKeyType.PAGE_FIRST_SEASON_FIRST_EPISODE_PLAY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_live_schedule_item_vertical_height_key), AppCMSUIKeyType.PAGE_LIVE_SCHEDULE_ITEM_VERTICAL_HEIGHT_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_live_schedule_no_data_type), AppCMSUIKeyType.PAGE_LIVE_SCHEDULE_NO_DATA_TYPE);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_brand_carousel_key), AppCMSUIKeyType.PAGE_BRANDS_CAROUSEL_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_brand_carousel_description_key), AppCMSUIKeyType.PAGE_BRANDS_CAROUSEL_DESCRIPTION_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_brand_carousel_name_key), AppCMSUIKeyType.PAGE_BRANDS_CAROUSEL_NAME_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_brand_tray_title_brand_key), AppCMSUIKeyType.PAGE_BRANDS_TRAY_TITLE_BRAND_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_brand_collection_brand_key), AppCMSUIKeyType.PAGE_BRANDS_COLLECTIONGRID_BRAND_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_home_carousel_event_timer_key), AppCMSUIKeyType.PAGE_HOME_CAROUSEL_EVENT_TIMER_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_live_schedule_grid_no_data_key), AppCMSUIKeyType.PAGE_LIVE_SCHEDULE_GRID_NO_DATA_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_live_schedule_no_data_key), AppCMSUIKeyType.PAGE_LIVE_SCHEDULE_NO_DATA_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_live_schedule_background_key), AppCMSUIKeyType.PAGE_LIVE_SCHEDULE_BACKGROUND_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_live_schedule_view_image_key), AppCMSUIKeyType.PAGE_LIVE_SCHEDULE_VIEW_IMAGE_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_live_schedule_view_button_key), AppCMSUIKeyType.PAGE_LIVE_SCHEDULE_VIEW_BUTTON_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_week_schedule_6day_grid_key), AppCMSUIKeyType.PAGE_SCHEDULE_6DAY_GRID_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_week_schedule_5day_grid_key), AppCMSUIKeyType.PAGE_SCHEDULE_5DAY_GRID_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_week_schedule_4day_grid_key), AppCMSUIKeyType.PAGE_SCHEDULE_4DAY_GRID_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_week_schedule_3day_grid_key), AppCMSUIKeyType.PAGE_SCHEDULE_3DAY_GRID_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_week_schedule_2day_grid_key), AppCMSUIKeyType.PAGE_SCHEDULE_2DAY_GRID_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_week_schedule_1day_grid_key), AppCMSUIKeyType.PAGE_SCHEDULE_1DAY_GRID_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_week_schedule_0day_grid_key), AppCMSUIKeyType.PAGE_SCHEDULE_0DAY_GRID_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_schedule_no_data_key), AppCMSUIKeyType.PAGE_SCHEDULE_NO_DATA_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_week_schedule_grid_no_data_key), AppCMSUIKeyType.PAGE_SCHEDULE_GRID_NO_DATA_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_schedule_day_key), AppCMSUIKeyType.PAGE_SCHEDULE_DAY_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_schedule_date_key), AppCMSUIKeyType.PAGE_SCHEDULE_DATE_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_schedule_inactive_key), AppCMSUIKeyType.PAGE_SCHEDULE_INACTIVE_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_schedule_active_key), AppCMSUIKeyType.PAGE_SCHEDULE_ACTIVE_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_badge_image_week_LV_key), AppCMSUIKeyType.PAGE_WEEK_SCHEDULE_BADGE_IMAGE_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_thumbnail_image_week_LV_key), AppCMSUIKeyType.PAGE_WEEK_SCHEDULE_THUMBNAIL_IMAGE_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_link_schedule_view_grid_key), AppCMSUIKeyType.PAGE_WEEK_SCHEDULE_VIEW_GRID_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_week_schedule_date_grid_key), AppCMSUIKeyType.PAGE_WEEK_SCHEDULE_DATE_GRID_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_link_horizontal_view_grid_key), AppCMSUIKeyType.PAGE_LINK_MODULE_HORIZONTAL_GRID_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_link_schedule_grid_key), AppCMSUIKeyType.PAGE_WEEK_SCHEDULE_GRID_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_class_grid_key), AppCMSUIKeyType.PAGE_CLASS_GRID_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_live_schedule_grid_key), AppCMSUIKeyType.PAGE_LIVE_SCHEDULE_GRID_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_instructor_recent_grid_key), AppCMSUIKeyType.PAGE_INSTRUCTOR_RECENT_GRID_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_instructor_category_recent_key), AppCMSUIKeyType.PAGE_INSTRUCTOR_CATEGORY_RECENT_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_instructor_trainer_name_recent_key), AppCMSUIKeyType.PAGE_INSTRUCTOR_TRAINER_NAME_RECENT_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_instructor_page_badge_image_recent_key), AppCMSUIKeyType.PAGE_INSTRUCTOR_BADGE_IMAGE_RECENT_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_instructor_page_thumbnail_image_recent_key), AppCMSUIKeyType.PAGE_INSTRUCTOR_THUMBNAIL_IMAGE_RECENT_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_instructor_page_class_duration_recent_key), AppCMSUIKeyType.PAGE_INSTRUCTOR_CLASS_DURATION_RECENT_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_instructor_page_duration_image_recent_key), AppCMSUIKeyType.PAGE_INSTRUCTOR_DURATION_IMAGE_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_live_schedule_badge_image_key), AppCMSUIKeyType.PAGE_LIVE_SCHEDULE_BADGE_IMAGE_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_live_schedule_thumbnail_title_key), AppCMSUIKeyType.PAGE_LIVE_SCHEDULE_THUMBNAIL_TITLE_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_live_schedule_thumbnail_image_key), AppCMSUIKeyType.PAGE_LIVE_SCHEDULE_THUMBNAIL_IMAGE_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_live_schedule_remaining_lv_key), AppCMSUIKeyType.PAGE_LIVE_SCHEDULE_REMAINING_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_live_schedule_duration_image_key), AppCMSUIKeyType.PAGE_LIVE_SCHEDULE_DURATION_IMAGE_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_live_schedule_class_duration_live_schedule_key), AppCMSUIKeyType.PAGE_LIVE_SCHEDULE_CLASS_DURATION_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_home_class_duration_netflix_key), AppCMSUIKeyType.PAGE_HOME_CLASS_DURATION_NETFLIX_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_brands_thumbnail_image_round_key), AppCMSUIKeyType.PAGE_BRANDS_THUMBNAIL_IMAGE_ROUND_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_badge_image_detail_key), AppCMSUIKeyType.PAGE_VIDEO_DETAIL_BADGE_IMAGE_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_instruction_instructor_text_key), AppCMSUIKeyType.PAGE_INSTRUCTOR_INSTRACTOR_TEXT_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_instruction_background_color_key), AppCMSUIKeyType.PAGE_INSTRUCTOR_BACKGROUND_COLOR_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_instruction_background_separator_color_key), AppCMSUIKeyType.PAGE_INSTRUCTOR_BACKGROUND_SEPARATOR_COLOR_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_instruction_description_key), AppCMSUIKeyType.PAGE_INSTRUCTOR_DESCRIPTION_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_instructor_separator_key), AppCMSUIKeyType.PAGE_INSTRUCTOR_SEPARATOR_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_instructor_twitter_icon_key), AppCMSUIKeyType.PAGE_INSTRUCTOR_TWITTER_ICON_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_instructor_facebook_icon_key), AppCMSUIKeyType.PAGE_INSTRUCTOR_FACEBOOK_ICON_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_instructor_instagram_icon_key), AppCMSUIKeyType.PAGE_INSTRUCTOR_INSTAGRAM_ICON_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_Instructor_key), AppCMSUIKeyType.ANDROID_INSTRUCTOR_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_video_detail_equipment_needed_list_key), AppCMSUIKeyType.PAGE_VIDEO_DETAIL_EQUIPMENT_NEEDED_LIST_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_video_detail_playlist_label_value_key), AppCMSUIKeyType.PAGE_VIDEO_DETAIL_PLAYLIST_LABEL_VALUE_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_video_detail_playlist_label_key), AppCMSUIKeyType.PAGE_VIDEO_DETAIL_PLAYLIST_LABEL_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_video_detail_intensity_label_key), AppCMSUIKeyType.PAGE_VIDEO_DETAIL_INTENSITY_LABEL_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_video_detail_intensity_value_key), AppCMSUIKeyType.PAGE_VIDEO_DETAIL_INTENSITY_VALUE_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_video_detail_language_label_value_key), AppCMSUIKeyType.PAGE_VIDEO_DETAIL_LANGUAGE_LABEL_VALUE_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_video_detail_language_label_key), AppCMSUIKeyType.PAGE_VIDEO_DETAIL_LANGUAGE_LABEL_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_video_detail_language_image_key), AppCMSUIKeyType.PAGE_VIDEO_DETAIL_LANGUAGE_IMAGE_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_video_detail_rating_label_value_key), AppCMSUIKeyType.PAGE_VIDEO_DETAIL_RATING_LABEL_VALUE_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_video_detail_rating_label_key), AppCMSUIKeyType.PAGE_VIDEO_DETAIL_RATING_LABEL_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_video_detail_difficulty_label_value_key), AppCMSUIKeyType.PAGE_VIDEO_DETAIL_DIFFICULTY_LABEL_VALUE_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_video_detail_difficulty_label_key), AppCMSUIKeyType.PAGE_VIDEO_DETAIL_DIFFICULTY_LABEL_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_video_detail_favourite_button_key), AppCMSUIKeyType.PAGE_VIDEO_DETAIL_FAVOURITE_BUTTON_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_video_detail_air_date_time_key), AppCMSUIKeyType.PAGE_VIDEO_DETAIL_AIR_DATE_TIME_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_video_detail_air_date_time_label_key), AppCMSUIKeyType.PAGE_VIDEO_DETAIL_AIR_DATE_TIME_LABEL_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_video_detail_class_play_button_key), AppCMSUIKeyType.PAGE_VIDEO_DETAIL_CLASS_PLAY_BUTTON_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_video_detail_category_title_key), AppCMSUIKeyType.PAGE_VIDEO_DETAIL_CATEGORY_TITLE_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_video_detail_timer_fitness_key), AppCMSUIKeyType.PAGE_VIDEO_DETAIL_TIMER_FITNESS_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_video_detail_instructor_title_key), AppCMSUIKeyType.PAGE_INSTRUCTOR_TITLE_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_seasons_count_key), AppCMSUIKeyType.PAGE_SEASONS_COUNT_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_instructor_equipment_title_key), AppCMSUIKeyType.PAGE_INSTRUCTOR_EQUIPMENT_TITLE_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_instructor_equipment_title_key), AppCMSUIKeyType.PAGE_INSTRUCTOR_EQUIPMENT_TITLE_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_instructor_equipment_description_key), AppCMSUIKeyType.PAGE_INSTRUCTOR_EQUIPMENT_DESCRIPTION_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_video_detail_class_duration_key), AppCMSUIKeyType.PAGE_VIDEO_DETAIL_CLASS_DURATION_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_video_detail_equipment_need_lable_key), AppCMSUIKeyType.PAGE_VIDEO_DETAIL_EQUIPMENT_NEED_LABLE_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_video_detail_class_duration_separator_key), AppCMSUIKeyType.PAGE_VIDEO_DETAIL_CLASS_DURATION_SEPARATOR_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_carousel_time_key), AppCMSUIKeyType.PAGE_CAROUSEL_TIME_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_carousel_arrow_image_key), AppCMSUIKeyType.PAGE_CAROUSEL_ARROW_IMAGE_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_tray_netflix_module_key), AppCMSUIKeyType.PAGE_TRAY_NETFLIX_MODULE_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_video_detail_instructor_key), AppCMSUIKeyType.PAGE_VIDEO_DETAIL_INFO_03_MODULE_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_video_detail_with_info_04_key), AppCMSUIKeyType.PAGE_VIDEO_DETAIL_INFO_04_MODULE_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_ac_profile_02_key), AppCMSUIKeyType.PAGE_AC_PROFILE_02_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_event_carousal_04_key), AppCMSUIKeyType.PAGE_EVENT_CAROUSAL_04_MODULE_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_page_control_02_key), AppCMSUIKeyType.PAGE_PAGE_CONTROL_VIEW_02_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_page_control_03_key), AppCMSUIKeyType.PAGE_PAGE_CONTROL_VIEW_03_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_equipment_next_button_equipment_key), AppCMSUIKeyType.PAGE_EQUIPMENT_NEXT_BUTTON_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_equipment_skip_button_key), AppCMSUIKeyType.PAGE_EQUIPMENT_SKIP_BUTTON_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_equipment_Title_key), AppCMSUIKeyType.PAGE_EQUIPMENT_TITLE_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_equipment_page_grid_key), AppCMSUIKeyType.PAGE_EQUIPMENT_GRID_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_user_equipment_title_key), AppCMSUIKeyType.PAGE_EQUIPMENT_USER_TITLE_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_video_detail_equipment_separator_view_key), AppCMSUIKeyType.PAGE_VIDEO_DETAILE_SEPARATOR_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_video_layer), AppCMSUIKeyType.PAGE_VIDEO_LAYER);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_splash_background_video_key), AppCMSUIKeyType.PAGE_SPLASH_BACKGROUND_VIDEO_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_equipmentpage_key), AppCMSUIKeyType.ANDROID_EQUIPMENT_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_equipment_selection_key), AppCMSUIKeyType.PAGE_EQUPMENT_SELECTION_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_Splash_Logo_key), AppCMSUIKeyType.PAGE_SPLASH_LOGO_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_carousel_video_duration_separation_key), AppCMSUIKeyType.PAGE_CAROUSEL_VIDEO_DURATION_SEPARATION_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_brand_carousel_module_key), AppCMSUIKeyType.PAGE_BRAND_CAROUSEL_MODULE_TYPE);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_brand_carousel_03_module_key), AppCMSUIKeyType.PAGE_BRAND_CAROUSEL_03_MODULE_TYPE);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_schedule_carousel_module_key), AppCMSUIKeyType.PAGE_SCHEDULE_CAROUSEL_MODULE_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_schedule_carousel_date_key), AppCMSUIKeyType.PAGE_SCHEDULE_CAROUSEL_DATE_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_trainer_name_key), AppCMSUIKeyType.PAGE_TRAINER_NAME_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_schedule_carousel_live_button_key), AppCMSUIKeyType.PAGE_SCHEDULE_CAROUSEL_LIVE_BUTTON_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_schedule_carousel_add_to_calendar_button_key), AppCMSUIKeyType.PAGE_SCHEDULE_CAROUSEL_ADD_TO_CALENDAR_BUTTON_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_schedulecarousel_next_class_key), AppCMSUIKeyType.PAGE_SCHEDULE_CAROUSEL_NEXT_CLASS_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_schedule_next_class_time_key), AppCMSUIKeyType.PAGE_LIVE_SCHEDULE_NEXT_CLASS_TIME_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_schedule_next_class_key), AppCMSUIKeyType.PAGE_LIVE_SCHEDULE_NEXT_CLASS_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_home_tray_title_key), AppCMSUIKeyType.PAGE_HOME_TRAY_TITLE_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_subscribe_email_go_button_key), AppCMSUIKeyType.PAGE_SUBSCRIBE_EMAIL_GO_BUTTON_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_subscribeEditText_key), AppCMSUIKeyType.PAGE_SUBSCRIBE_EMAIL_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_ratingbar_key), AppCMSUIKeyType.PAGE_RATINGBAR);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_video_type_key), AppCMSUIKeyType.PAGE_VIDEO_TYPE_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_main_svod_service_type_key), AppCMSUIKeyType.MAIN_SVOD_SERVICE_TYPE);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_main_avod_service_type_key), AppCMSUIKeyType.MAIN_AVOD_SERVICE_TYPE);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_pagename_authscreen_key), AppCMSUIKeyType.ANDROID_AUTH_SCREEN_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_pagename_searchscreen_key), AppCMSUIKeyType.ANDROID_SEARCH_SCREEN_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_pagename_splashscreen_key), AppCMSUIKeyType.ANDROID_SPLASH_SCREEN_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_pagename_downloadsettingscreen_key), AppCMSUIKeyType.ANDROID_DOWNLOAD_SETTINGS_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_search_module_key), AppCMSUIKeyType.PAGE_AC_SEARCH_MODULE_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_search_02_module_key), AppCMSUIKeyType.PAGE_AC_SEARCH02_MODULE_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_search_03_module_key), AppCMSUIKeyType.PAGE_AC_SEARCH03_MODULE_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_download_title), AppCMSUIKeyType.ANDROID_DOWNLOAD_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_action_playlistpage_key), AppCMSUIKeyType.ANDROID_PLAYLIST_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_action_teamdetail_key), AppCMSUIKeyType.ANDROID_TEAM_DETAIL_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_pagename_homescreen_key), AppCMSUIKeyType.ANDROID_HOME_SCREEN_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_pagename_ftf_homescreen_key), AppCMSUIKeyType.ANDROID_FTF_HOME_SCREEN_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_pagename_sign_up_key), AppCMSUIKeyType.ANDROID_SIGN_UP_SCREEN_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_pagename_create_login_key), AppCMSUIKeyType.ANDROID_SIGN_UP_SCREEN_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_pagename_showsscreen_key), AppCMSUIKeyType.ANDROID_SHOWS_SCREEN_KEY);
        jsonValueKeyMap.put(context.getString(R.string.contact_us_roku), AppCMSUIKeyType.ANDROID_CONTACT_US_ROKU_SCREEN_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_pagename_browse_by_genre_key), AppCMSUIKeyType.ANDROID_BROWSE_BY_GENRE_SCREEN_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_pagename_browse_key), AppCMSUIKeyType.ANDROID_BROWSE_SCREEN_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_pagename_privacy_policy_key), AppCMSUIKeyType.PRIVACY_POLICY_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_pagename_link_your_account_key), AppCMSUIKeyType.LINK_ACCOUNT_PAGE_KEY);
        jsonValueKeyMap.put(context.getString(R.string.sub_nav_page_key), AppCMSUIKeyType.SUB_NAV_PAGE_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_ui_block_customNavigation), AppCMSUIKeyType.ANDROID_CUSTOM_NAVIGATION_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_detail_app_logo_key), AppCMSUIKeyType.PAGE_VIDEO_DETAIL_APP_LOGO_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_pagename_terms_of_services_key), AppCMSUIKeyType.TERMS_OF_SERVICE_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_pagename_search_screen_key), AppCMSUIKeyType.PAGE_SEARCH_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_pagename_moviesscreen_key), AppCMSUIKeyType.ANDROID_MOVIES_SCREEN_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_pagename_contact_key), AppCMSUIKeyType.ANDROID_CONTACT_SCREEN_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_pagename_showsscreen_key), AppCMSUIKeyType.ANDROID_SHOWS_SCREEN_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_pagename_subscriptionscreen_key), AppCMSUIKeyType.ANDROID_SUBSCRIPTION_SCREEN_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_pagename_historyscreen_key), AppCMSUIKeyType.ANDROID_HISTORY_SCREEN_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_pagename_historyscreen_key), AppCMSUIKeyType.ANDROID_HISTORY_SCREEN_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_pagename_bookmarks_screen_key), AppCMSUIKeyType.ANDROID_BOOKMARKS_SCREEN_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_watchlist_navigation_title), AppCMSUIKeyType.ANDROID_WATCHLIST_NAV_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_favorites_navigation_title), AppCMSUIKeyType.ANDROID_FAVORITES_NAV_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_pagename_my_watchlistscreen_key), AppCMSUIKeyType.ANDROID_WATCHLIST_SCREEN_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_follow_navigation_title), AppCMSUIKeyType.ANDROID_FOLLOW_SCREEN_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_pagename_my_library_screen_key), AppCMSUIKeyType.ANDROID_LIBRARY_SCREEN_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_library_navigation_title), AppCMSUIKeyType.ANDROID_LIBRARY_NAV_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_pagename_watchlistscreen_key), AppCMSUIKeyType.ANDROID_WATCHLIST_SCREEN_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_pagename_articlescreen_key), AppCMSUIKeyType.ANDROID_ARTICLE_SCREEN_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_pagename_photogalleryscreen_key), AppCMSUIKeyType.ANDROID_PHOTOGALLERY_SCREEN_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_pagename_schedule_screen_key), AppCMSUIKeyType.ANDROID_SCHEDULE_SCREEN_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_pagename_person_screen_key), AppCMSUIKeyType.ANDROID_PERSON_SCREEN_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_pagename_eventscreen_key), AppCMSUIKeyType.ANDROID_EVENT_SCREEN_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_pagename_rosterscreen_key), AppCMSUIKeyType.ANDROID_FIGHTER_ROSTER_SCREEN_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_download_page_title), AppCMSUIKeyType.ANDROID_DOWNLOAD_NAV_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_more_screen_tag), AppCMSUIKeyType.ANDROID_MORE_NAV_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_history_navigation_title), AppCMSUIKeyType.ANDROID_HISTORY_NAV_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_settings_page_title_text), AppCMSUIKeyType.ANDROID_SETTINGS_NAV_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_button_switch_key), AppCMSUIKeyType.PAGE_BUTTON_SWITCH_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_checkbox), AppCMSUIKeyType.PAGE_CHECKBOX_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_email_consent_checkbox_key), AppCMSUIKeyType.PAGE_EMAIL_CONSENT_CHECKBOX_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_whatsapp_consent_checkbox_key), AppCMSUIKeyType.PAGE_WHATSAPP_CONSENT_CHECKBOX_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_age_consent_checkbox_key), AppCMSUIKeyType.PAGE_AGE_CONSENT_CHECKBOX_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_button), AppCMSUIKeyType.PAGE_BUTTON_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_edit_text), AppCMSUIKeyType.PAGE_EDIT_TEXT_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_edit_account_from_website), AppCMSUIKeyType.PAGE_SETTING_ACCOUNT_EDIT_TEXT_FROM_WEBSITE_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_edit_phone_from_website), AppCMSUIKeyType.PAGE_SETTING_ACCOUNT_EDIT_PHONE_FROM_WEBSITE_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_video_detail_like_component_key), AppCMSUIKeyType.PAGE_LIKE_COMPONENT_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_text_view), AppCMSUIKeyType.PAGE_LABEL_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_constraint_guide_line_key), AppCMSUIKeyType.PAGE_CONSTRAINT_GUIDE_LINE_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_constraint_guide_line_top), AppCMSUIKeyType.PAGE_CONSTRAINT_GUIDE_LINE_TOP);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_constraint_guide_line_center), AppCMSUIKeyType.PAGE_CONSTRAINT_GUIDE_LINE_CENTER);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_chain_key), AppCMSUIKeyType.PAGE_CHAIN_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_vod_more_related_key), AppCMSUIKeyType.PAGE_VOD_MORE_RELATED_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_vod_you_may_like_key), AppCMSUIKeyType.PAGE_VOD_YOU_MAY_LIKE_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_chain_vertical_key), AppCMSUIKeyType.PAGE_VERTICAL_CHAIN_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_chain_horizontal_key), AppCMSUIKeyType.PAGE_HORIZONTAL_CHAIN_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_venue_label_key), AppCMSUIKeyType.PAGE_VENUE_LABEL_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_timer_title_label_key), AppCMSUIKeyType.PAGE_TIMER_TITLE_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_redemption_code_label_key), AppCMSUIKeyType.PAGE_REDEMPTION_CODE_LABEL_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_redemption_error_label_key), AppCMSUIKeyType.PAGE_REDEMPTION_ERROR_LABEL_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_age_error_label_key), AppCMSUIKeyType.PAGE_AGE_ERROR_LABEL_KEY);
        jsonValueKeyMap.put(context.getString(R.string.ui_block_article_tray_01), AppCMSUIKeyType.UI_BLOCK_ARTICLE_01);
        jsonValueKeyMap.put(context.getString(R.string.ui_block_tray_04), AppCMSUIKeyType.UI_BLOCK_TRAY_04);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_upcoming_timer), AppCMSUIKeyType.PAGE_UPCOMING_TIMER_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_rent_active_component_key), AppCMSUIKeyType.PAGE_RENT_ACTIVE_COMPONENT_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_ads_key), AppCMSUIKeyType.PAGE_ADS_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_article_title_key), AppCMSUIKeyType.PAGE_ARTICLE_TITLE_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_article_feed_bottom_text_key), AppCMSUIKeyType.PAGE_ARTICLE_FEED_BOTTOM_TEXT_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_article_description_key), AppCMSUIKeyType.PAGE_ARTICLE_DESCRIPTION_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_vod_more_collection_grid_key), AppCMSUIKeyType.PAGE_VOD_MORE_COLLECTIONGRID_KEY);
        jsonValueKeyMap.put(context.getString(R.string.grid_collection_grid_key), AppCMSUIKeyType.GRID_COLLECTIONGRID_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_vod_concept_collection_grid_key), AppCMSUIKeyType.PAGE_VOD_CONCEPT_COLLECTIONGRID_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_recycler_view), AppCMSUIKeyType.PAGE_COLLECTIONGRID_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_collapsible_key), AppCMSUIKeyType.PAGE_COLLAPSIBLE_VIEW);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_collection_grid_season_key), AppCMSUIKeyType.PAGE_COLLECTIONGRID_SEASON_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_see_all_collection_grid_key), AppCMSUIKeyType.PAGE_SEE_ALL_COLLECTIONGRID_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_grid_view_collection_grid_key), AppCMSUIKeyType.PAGE_GRID_VIEW_COLLECTIONGRID_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_table_view_key), AppCMSUIKeyType.PAGE_TABLE_VIEW_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_live_player_component_table_view_key), AppCMSUIKeyType.PAGE_LIVE_PLAYER_COMPONENT_TABLE_VIEW_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_live_player_component_title_label_key), AppCMSUIKeyType.PAGE_LIVE_PLAYER_COMPONENT_TITLE_LABEL_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_vertical_grid_view_key), AppCMSUIKeyType.PAGE_VERTICAL_GRID_VIEW_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_season_table_view_key), AppCMSUIKeyType.PAGE_SEASON_TABLE_VIEW_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_episode_table_view_key), AppCMSUIKeyType.PAGE_EPISODE_TABLE_VIEW_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_segment_table_view_key), AppCMSUIKeyType.PAGE_SEGMENT_TABLE_VIEW_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_table_view_setting_language_download_key), AppCMSUIKeyType.PAGE_TABLE_VIEW_SETTING_LANGUAGE_DOWNLOAD_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_multiple_table_view_key), AppCMSUIKeyType.PAGE_MULTI_TABLE_VIEW_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_progress_bar), AppCMSUIKeyType.PAGE_PROGRESS_VIEW_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_carousel), AppCMSUIKeyType.PAGE_CAROUSEL_VIEW_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_content_carousel), AppCMSUIKeyType.PAGE_CONTENT_CAROUSEL_VIEW_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_list_key), AppCMSUIKeyType.PAGE_LIST_VIEW_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_list_module_key), AppCMSUIKeyType.PAGE_LIST_MODULE_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_watch_live_button_key), AppCMSUIKeyType.PAGE_WATCH_LIVE_BUTTON_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_redeem_btn_key), AppCMSUIKeyType.PAGE_REDEEM_BUTTON_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_video_player_key), AppCMSUIKeyType.PAGE_VIDEO_PLAYER_VIEW_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_video_player_key_value), AppCMSUIKeyType.PAGE_VIDEO_PLAYER_VIEW_KEY_VALUE);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_trailer_video_player_key_value), AppCMSUIKeyType.PAGE_TRAILER_VIDEO_PLAYER_VIEW_KEY_VALUE);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_schedule_key_value), AppCMSUIKeyType.PAGE_SCHEDULE_PAGE_KEY_VALUE);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_show_player_key), AppCMSUIKeyType.PAGE_SHOW_PLAYER_VIEW_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_web_view_key), AppCMSUIKeyType.PAGE_WEB_VIEW_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_article_web_view_key), AppCMSUIKeyType.PAGE_ARTICLE_WEB_VIEW_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_article_previous_button_key), AppCMSUIKeyType.PAGE_ARTICLE_PREVIOUS_BUTTON_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_article_next_button_key), AppCMSUIKeyType.PAGE_ARTICLE_NEXT_BUTTON_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_full_screen_key), AppCMSUIKeyType.PAGE_FULL_SCREEN_IMAGE_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_carousel_image_key), AppCMSUIKeyType.PAGE_CAROUSEL_IMAGE_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_concept_image_key), AppCMSUIKeyType.PAGE_CAROUSEL_IMAGE_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_carousel_badge_image_key), AppCMSUIKeyType.PAGE_CAROUSEL_BADGE_IMAGE_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_wide_carousel_image_key), AppCMSUIKeyType.PAGE_WIDE_CAROUSEL_IMAGE_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_wide_badge_image_key), AppCMSUIKeyType.PAGE_WIDE_BADGE_IMAGE_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_video_detail_player_key), AppCMSUIKeyType.PAGE_VIDEO_DETAIL_PLAYER_VIEW_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_carousel_add_to_watchlist_key), AppCMSUIKeyType.PAGE_CAROUSEL_ADD_TO_WATCHLIST_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_add_to_watchlist_key), AppCMSUIKeyType.PAGE_ADD_TO_WATCHLIST_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_add_to_watchlist_with_text_key), AppCMSUIKeyType.PAGE_ADD_TO_WATCHLIST_WITH_TEXT_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_add_to_watchlist_with_icon_key), AppCMSUIKeyType.PAGE_ADD_TO_WATCHLIST_WITH_ICON_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_video_download_button_key), AppCMSUIKeyType.PAGE_VIDEO_DOWNLOAD_BUTTON_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_download_component_key), AppCMSUIKeyType.PAGE_VIDEO_DOWNLOAD_COMPONENT_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_page_control_key), AppCMSUIKeyType.PAGE_PAGE_CONTROL_VIEW_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_separator_view), AppCMSUIKeyType.PAGE_SEPARATOR_VIEW_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_selection_view), AppCMSUIKeyType.VIEW_SELECTION);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_content_separator_view), AppCMSUIKeyType.PAGE_CONTENT_SEPARATOR_VIEW_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_tray_article_separator_key), AppCMSUIKeyType.TRAY_ARTICLE_SEPARATOR_VIEW_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_fill_background), AppCMSUIKeyType.PAGE_FILL_BACKGROUND);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_download_separator_key), AppCMSUIKeyType.DOWNLOAD_SEPARATE_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_view_details_background), AppCMSUIKeyType.PAGE_VIEW_DETAILS_BACKGROUND);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_view_timer_background), AppCMSUIKeyType.PAGE_VIEW_TIMER_BACKGROUND);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_rest_time_key), AppCMSUIKeyType.PAGE_REST_TIME_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_cardView_key), AppCMSUIKeyType.PAGE_CARD_VIEW);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_search_separator_key), AppCMSUIKeyType.PAGE_SEARCH_SEPARATOR_VIEW_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_link_separator_key), AppCMSUIKeyType.PAGE_LINK_SEPARATOR_VIEW_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_concept_separator_key), AppCMSUIKeyType.PAGE_CONCEPT_SEPARATOR_VIEW_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_result_separator_key), AppCMSUIKeyType.PAGE_RESULT_SEPARATOR_VIEW_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_multicolumn_table_key), AppCMSUIKeyType.PAGE_MULTICOLUMN_TABLE_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_article_bottom_button_background_key), AppCMSUIKeyType.PAGE_BOTTOM_BACKGROUND_ARTICLE_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_segmented_view), AppCMSUIKeyType.PAGE_SEGMENTED_VIEW_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_castview_key), AppCMSUIKeyType.PAGE_CASTVIEW_VIEW_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_image_view), AppCMSUIKeyType.PAGE_IMAGE_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_circular_thumbnail_image_view), AppCMSUIKeyType.PAGE_CIRCULAR_THUMNAIL_IMAGE_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_countdown_timer_view), AppCMSUIKeyType.PAGE_COUNTDOWN_TIMER_VIEW);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_carousel_gradient_layout_key), AppCMSUIKeyType.PAGE_CAROUSEL_GRADIENT_LAYOUT_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_bg_key), AppCMSUIKeyType.PAGE_BG_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_logo_key), AppCMSUIKeyType.PAGE_LOGO_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_info_key), AppCMSUIKeyType.PAGE_INFO_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_play_key), AppCMSUIKeyType.PAGE_PLAY_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_action_detailvideopage_key), AppCMSUIKeyType.PAGE_PLAY_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_action_showvideopage_key), AppCMSUIKeyType.PAGE_SHOW_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_action_referralPlans_key), AppCMSUIKeyType.PAGE_REFERRAL_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_concept_landing_page_key), AppCMSUIKeyType.PAGE_CONCEPT_LANDING_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_action_browse_shows_key), AppCMSUIKeyType.PAGE_BROWSE_SHOWS_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_action_detailbundlepage_key), AppCMSUIKeyType.PAGE_BUNDLE_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_action_articlepage_key), AppCMSUIKeyType.PAGE_ARTICLE_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_action_photo_gallerypage_key), AppCMSUIKeyType.PAGE_PHOTO_GALLERY_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_team_page_tag), AppCMSUIKeyType.PAGE_TEAMS_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_video_watchnow_key), AppCMSUIKeyType.PAGE_WATCH_VIDEO_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_play_image_key), AppCMSUIKeyType.PAGE_PLAY_IMAGE_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_play_live_image_key), AppCMSUIKeyType.PAGE_PLAY_LIVE_IMAGE_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_logo_image_key), AppCMSUIKeyType.PAGE_LOGO_IMAGE_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_tray_title_key), AppCMSUIKeyType.PAGE_TRAY_TITLE_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_vod_concept_tray_title_key), AppCMSUIKeyType.PAGE_VOD_CONCEPT_TRAY_TITLE_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_vod_more_tray_title_key), AppCMSUIKeyType.PAGE_VOD_MORE_TRAY_TITLE_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_expanded_view_title_key), AppCMSUIKeyType.PAGE_EXPANDED_VIEW_TITLE_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_live_player_title_key), AppCMSUIKeyType.PAGE_LIVE_PLAYER_TITLE_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_live_player_icon_key), AppCMSUIKeyType.PAGE_LIVE_PLAYER_ICON_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_expanded_view_description_key), AppCMSUIKeyType.PAGE_EXPANDED_VIEW_DESCRIPTION_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_live_player_description_key), AppCMSUIKeyType.PAGE_LIVE_PLAYER_DESCRIPTION_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_search_block_key), AppCMSUIKeyType.PAGE_SEARCH_BLOCK_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_search_block_back_button_theme_key), AppCMSUIKeyType.PAGE_SEARCH_BLOCK_BACK_BUTTON_THEME_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_no_of_category_key), AppCMSUIKeyType.PAGE_NO_OF_CATEGORY_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_schedule_title_key), AppCMSUIKeyType.PAGE_TRAY_SCHEDULE_TITLE_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_season_tray_title_key), AppCMSUIKeyType.PAGE_TRAY_SEASON_TITLE_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_tray_title_underline_key), AppCMSUIKeyType.PAGE_TRAY_TITLE_UNDERLINE_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_schedule_page_title_key), AppCMSUIKeyType.PAGE_SCHEDULE_TRAY_TITLE_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_category_page_title_key), AppCMSUIKeyType.PAGE_CATEGORY_TRAY_TITLE_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_schedule_key), AppCMSUIKeyType.PAGE_SCHEDULE_SCREEN_TITLE_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_language_key), AppCMSUIKeyType.PAGE_LANGUAGE_SCREEN_TITLE_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_roster_key), AppCMSUIKeyType.PAGE_ROSTER_SCREEN_TITLE_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_fighter_key), AppCMSUIKeyType.PAGE_FIGHTER_SCREEN_TITLE_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_home_team_title_key), AppCMSUIKeyType.PAGE_HOME_TEAM_TITLE_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_away_team_title_key), AppCMSUIKeyType.PAGE_AWAY_TEAM_TITLE_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_thumbnail_image_key), AppCMSUIKeyType.PAGE_THUMBNAIL_IMAGE_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_segment_thumbnail_key), AppCMSUIKeyType.PAGE_SEGMENT_THUMBNAIL_IMAGE_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_episode_thumbnail_key), AppCMSUIKeyType.PAGE_EPISODE_THUMBNAIL_IMAGE_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_thumbnail_image_expanded_key), AppCMSUIKeyType.PAGE_EXPANDED_THUMBNAIL_IMAGE_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_thumbnail_image_watchlist_key), AppCMSUIKeyType.PAGE_WATCHLIST_THUMBNAIL_IMAGE_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_tall_thumbnail_image_key), AppCMSUIKeyType.PAGE_TALL_THUMBNAIL_IMAGE_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_link_thumbnail_image_key), AppCMSUIKeyType.PAGE_LINK_THUMBNAIL_IMAGE_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_thumbnail_image_sub_menu_key), AppCMSUIKeyType.PAGE_THUMBNAIL_IMAGE_SUBMENU_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_home_team_image_key), AppCMSUIKeyType.PAGE_HOME_TEAM_IMAGE_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_away_team_image_key), AppCMSUIKeyType.PAGE_AWAY_TEAM_IMAGE_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_plan_feature_image_key), AppCMSUIKeyType.PAGE_PLAN_FEATURE_IMAGE_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_plan_feature_title_key), AppCMSUIKeyType.PAGE_PLAN_FEATURE_TITLE_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_thumbnail_date_and_image_key), AppCMSUIKeyType.PAGE_THUMBNAIL_TIME_AND_DATE_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_news_thumbnail_date_and_image_key), AppCMSUIKeyType.PAGE_NEWS_THUMBNAIL_TIME_AND_DATE_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_badge_image_key), AppCMSUIKeyType.PAGE_BADGE_IMAGE_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_thumbnail_title_key), AppCMSUIKeyType.PAGE_THUMBNAIL_TITLE_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_video_title_on_hover_key), AppCMSUIKeyType.PAGE_VIDEO_TITLE_ON_HOVER_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_video_sub_title_on_hover_key), AppCMSUIKeyType.PAGE_VIDEO_SUB_TITLE_ON_HOVER_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_video_description_on_hover_key), AppCMSUIKeyType.PAGE_VIDEO_DESCRIPTION_ON_HOVER_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_video_hover_background_key), AppCMSUIKeyType.PAGE_VIDEO_HOVER_BACKGROUND_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_thumbnail_description_key), AppCMSUIKeyType.PAGE_THUMBNAIL_DESCRIPTION_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_thumbnail_read_more_text_key), AppCMSUIKeyType.PAGE_THUMBNAIL_READ_MORE_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_episode_thumbnail_title_key), AppCMSUIKeyType.PAGE_EPISODE_THUMBNAIL_TITLE_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_text_alignment_center_key), AppCMSUIKeyType.PAGE_TEXTALIGNMENT_CENTER_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_text_alignment_center_horizontal_key), AppCMSUIKeyType.PAGE_TEXTALIGNMENT_CENTER_HORIZONTAL_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_text_alignment_center_vertical_key), AppCMSUIKeyType.PAGE_TEXTALIGNMENT_CENTER_VERTICAL_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_text_alignment_key), AppCMSUIKeyType.PAGE_TEXTALIGNMENT_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_text_alignment_left_key), AppCMSUIKeyType.PAGE_TEXTALIGNMENT_LEFT_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_text_alignment_right_key), AppCMSUIKeyType.PAGE_TEXTALIGNMENT_RIGHT_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_carousel_title_key), AppCMSUIKeyType.PAGE_CAROUSEL_TITLE_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_carousel_info_key), AppCMSUIKeyType.PAGE_CAROUSEL_INFO_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_font_bold_key), AppCMSUIKeyType.PAGE_TEXT_BOLD_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_font_semibold_key), AppCMSUIKeyType.PAGE_TEXT_SEMIBOLD_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_font_extrabold_key), AppCMSUIKeyType.PAGE_TEXT_EXTRABOLD_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_font_black_key), AppCMSUIKeyType.PAGE_TEXT_BLACK_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_font_black_italic_key), AppCMSUIKeyType.PAGE_TEXT_BLACK_ITALIC_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_font_hairline_key), AppCMSUIKeyType.PAGE_TEXT_HAIRLINE_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_font_hairline_italic_key), AppCMSUIKeyType.PAGE_TEXT_HAIRLINE_ITALIC_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_font_heavy_key), AppCMSUIKeyType.PAGE_TEXT_HEAVY_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_font_heavy_italic_key), AppCMSUIKeyType.PAGE_TEXT_HEAVY_ITALIC_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_font_light_key), AppCMSUIKeyType.PAGE_TEXT_LIGHT_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_font_light_italic_key), AppCMSUIKeyType.PAGE_TEXT_LIGHT_ITALIC_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_font_bold_italic_key), AppCMSUIKeyType.PAGE_TEXT_BLACK_ITALIC_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_font_italic_key), AppCMSUIKeyType.PAGE_TEXT_ITALIC_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_font_thin_key), AppCMSUIKeyType.PAGE_TEXT_THIN_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_font_thin_italic_key), AppCMSUIKeyType.PAGE_TEXT_THIN_ITALIC_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_font_family_key), AppCMSUIKeyType.PAGE_TEXT_OPENSANS_FONTFAMILY_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_font_family_key), AppCMSUIKeyType.PAGE_TEXT_OPENSANS_FONTFAMILY_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_font_lato_family_key), AppCMSUIKeyType.PAGE_TEXT_LATO_FONTFAMILY_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_font_montserrat_family_key), AppCMSUIKeyType.PAGE_TEXT_MONTSERRAT_FONT_FAMILY_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_font_titillium_web_family_key), AppCMSUIKeyType.PAGE_TEXT_TITILLIUM_WEB_FONT_FAMILY_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_font_din_pro_family_key), AppCMSUIKeyType.PAGE_TEXT_DIN_PRO_FONT_FAMILY_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_font_proxima_nova_family_key), AppCMSUIKeyType.PAGE_TEXT_PROXIMA_NOVA_FONT_FAMILY_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_textview_key), AppCMSUIKeyType.PAGE_TEXTVIEW_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_textfield_key), AppCMSUIKeyType.PAGE_TEXTFIELD_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_emailtextfield_key), AppCMSUIKeyType.PAGE_EMAILTEXTFIELD_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_emailtextfield2_key), AppCMSUIKeyType.PAGE_EMAILTEXTFIELD2_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_passwordtextfield_key), AppCMSUIKeyType.PAGE_PASSWORDTEXTFIELD_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_passwordtextfield2_key), AppCMSUIKeyType.PAGE_PASSWORDTEXTFIELD2_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_redeemptiontextfield_key), AppCMSUIKeyType.PAGE_REDEMPTIONTEXTFIELD_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_email_consent_checkbox_key), AppCMSUIKeyType.PAGE_EMAIL_CONSENT_CHECKBOX_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_forgotpassword_key), AppCMSUIKeyType.PAGE_FORGOTPASSWORD_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_mobileinput_key), AppCMSUIKeyType.PAGE_MOBILETEXTFIELD_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_authentication_module), AppCMSUIKeyType.PAGE_AUTHENTICATION_MODULE_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_api_description_key), AppCMSUIKeyType.PAGE_API_DESCRIPTION);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_api_episode_description_key), AppCMSUIKeyType.PAGE_API_EPISODE_DESCRIPTION);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_api_fitness_bpm_key), AppCMSUIKeyType.PAGE_API_FITNESS_BPM_VALUE);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_reset_password_module), AppCMSUIKeyType.PAGE_RESET_PASSWORD_MODULE_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_reset_password3_module), AppCMSUIKeyType.PAGE_RESET_PASSWORD3_MODULE_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_contact_us_module), AppCMSUIKeyType.PAGE_CONTACT_US_MODULE_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_reset_password_cancel_button_key), AppCMSUIKeyType.RESET_PASSWORD_CANCEL_BUTTON_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_reset_password_continue_button_key), AppCMSUIKeyType.RESET_PASSWORD_CONTINUE_BUTTON_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_forgotPasswordTitle_key), AppCMSUIKeyType.RESET_PASSWORD_TITLE_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_login_key), AppCMSUIKeyType.PAGE_LOGIN_BUTTON_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_launch_login_key), AppCMSUIKeyType.PAGE_LAUNCH_LOGIN_BUTTON_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_signup_key), AppCMSUIKeyType.PAGE_SIGNUP_BUTTON_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_forgot_password_detail_key), AppCMSUIKeyType.PAGE_FORGOT_PASSWORD_DETAIL_LABEL_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_forgot_password_label_key), AppCMSUIKeyType.PAGE_FORGOT_PASSWORD_LABEL_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_event_details_key), AppCMSUIKeyType.PAGE_EVENT_DETAILS_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_authentication_module), AppCMSUIKeyType.PAGE_AUTHENTICATION_MODULE_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_api_summary_text_key), AppCMSUIKeyType.PAGE_API_SUMMARY_TEXT_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_planmetadatatitle_key), AppCMSUIKeyType.PAGE_PLANMETA_DATA_TITLE_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_planmetadataimage_key), AppCMSUIKeyType.PAGE_PLANMETADDATAIMAGE_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_planmetadatadevicecount_key), AppCMSUIKeyType.PAGE_PLANMETADATADEVICECOUNT_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_settings_title_key), AppCMSUIKeyType.PAGE_SETTINGS_TITLE_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_personalization_key), AppCMSUIKeyType.EDIT_PERSONALIZATION);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_personalization_key_header), AppCMSUIKeyType.EDIT_PERSONALIZATION_HEADER);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_settings_name_value_key), AppCMSUIKeyType.PAGE_SETTINGS_NAME_VALUE_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_settings_phone_label_key), AppCMSUIKeyType.PAGE_SETTINGS_PHONE_LABEL_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_settings_phone_value_key), AppCMSUIKeyType.PAGE_SETTINGS_PHONE_VALUE_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_settings_email_value_key), AppCMSUIKeyType.PAGE_SETTINGS_EMAIL_VALUE_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_settings_plan_value_key), AppCMSUIKeyType.PAGE_SETTINGS_PLAN_VALUE_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_settings_next_billing_due_date_value_key), AppCMSUIKeyType.PAGE_SETTINGS_NEXT_BILLING_DUE_DATE_VALUE_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_settings_next_billing_due_date_key), AppCMSUIKeyType.PAGE_SETTINGS_NEXT_BILLING_DUE_DATE_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_settings_plan_price_value_key), AppCMSUIKeyType.PAGE_SETTINGS_PLAN_PRICE_VALUE_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_settings_plan_processor_title_key), AppCMSUIKeyType.PAGE_SETTINGS_PLAN_PROCESSOR_TITLE_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_settings_plan_processor_value_key), AppCMSUIKeyType.PAGE_SETTINGS_PLAN_PROCESSOR_VALUE_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_settings_download_quality_value_key), AppCMSUIKeyType.PAGE_SETTINGS_DOWNLOAD_QUALITY_PROFILE_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_edit_profile_key), AppCMSUIKeyType.PAGE_SETTINGS_EDIT_PROFILE_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_change_password_key), AppCMSUIKeyType.PAGE_SETTINGS_CHANGE_PASSWORD_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_contact_number_label), AppCMSUIKeyType.CONTACT_US_PHONE_LABEL);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_email_id_label), AppCMSUIKeyType.CONTACT_US_EMAIL_LABEL);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_contact_us_call_icon_key), AppCMSUIKeyType.CONTACT_US_PHONE_IMAGE);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_contact_us_email_icon_key), AppCMSUIKeyType.CONTACT_US_EMAIL_IMAGE);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_cancel_subscription_key), AppCMSUIKeyType.PAGE_SETTINGS_CANCEL_PLAN_PROFILE_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_upgrade_subscription_key), AppCMSUIKeyType.PAGE_SETTINGS_UPGRADE_PLAN_PROFILE_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_background_image_view_key), AppCMSUIKeyType.PAGE_BACKGROUND_IMAGE_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_season_background_image_view_key), AppCMSUIKeyType.PAGE_SEASON_BACKGROUND_IMAGE_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_episode_background_image_view_key), AppCMSUIKeyType.PAGE_EPISODE_BACKGROUND_IMAGE_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_segment_background_image_view_key), AppCMSUIKeyType.PAGE_SEGMENT_BACKGROUND_IMAGE_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_background_image_type_type), AppCMSUIKeyType.PAGE_BACKGROUND_IMAGE_TYPE_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_settings_app_version_value_key), AppCMSUIKeyType.PAGE_SETTINGS_APP_VERSION_VALUE_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_togglebutton_key), AppCMSUIKeyType.PAGE_TOGGLE_BUTTON_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_autoplay_toggle_button_key), AppCMSUIKeyType.PAGE_AUTOPLAY_TOGGLE_BUTTON_KEY);


        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_re_subscription_key),
                AppCMSUIKeyType.PAGE_SETTINGS_RE_SUBSCRIBE_PLAN_PROFILE_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_settings_plan_cancel_schedule_label),
                AppCMSUIKeyType.PAGE_SETTINGS_PLAN_CANCEL_SCHEDULE_LABEL_KEY);



        jsonValueKeyMap.put(context.getString(R.string
                .app_cms_page_use_sd_card_for_downloads_toggle_button_key), AppCMSUIKeyType.PAGE_SD_CARD_FOR_DOWNLOADS_TOGGLE_BUTTON_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_user_management_use_sd_card_for_downloads_text_key), AppCMSUIKeyType.PAGE_SD_CARD_FOR_DOWNLOADS_TEXT_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_photogallery_title), AppCMSUIKeyType.PAGE_PHOTO_GALLERY_TITLE_TXT_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_record_type_key), AppCMSUIKeyType.PAGE_RECORD_TYPE_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_photogallery_authName), AppCMSUIKeyType.PAGE_PHOTO_GALLERY_AUTH_TXT_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_photogallery_subTitle), AppCMSUIKeyType.PAGE_PHOTO_GALLERY_SUBTITLE_TXT_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_photogallery_imgCount), AppCMSUIKeyType.PAGE_PHOTO_GALLERY_IMAGE_COUNT_TXT_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_photogallery_selectedImg), AppCMSUIKeyType.PAGE_PHOTO_GALLERY_SELECTED_IMAGE);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_player_Img), AppCMSUIKeyType.PAGE_PHOTO_PLAYER_IMAGE);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_player_team_Img), AppCMSUIKeyType.PAGE_PHOTO_TEAM_IMAGE);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_player_record_label_key), AppCMSUIKeyType.PAGE_PLAYER_RECORD_LABEL_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_player_team_title_label_key), AppCMSUIKeyType.PAGE_PLAYER_TEAM_TITLE_TXT_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_player_weightdivision_value_key), AppCMSUIKeyType.PAGE_WEIGHT_DIVISION_VALUE_TXT_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_tableHeaderLabel_label_key), AppCMSUIKeyType.PAGE_TABEL_LABEL_HEADER_TXT_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_statsViewHeader_label_key), AppCMSUIKeyType.PAGE_STATE_LABEL_TXT_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_roster_title_key), AppCMSUIKeyType.PAGE_ROSTER_TITLE_TXT_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_fight_selection_label_key), AppCMSUIKeyType.PAGE_FIGHT_SELECTION_TXT_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_player_score_label_key), AppCMSUIKeyType.PAGE_PLAYER_SCORE_TEXT);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_player_weight_value_label_key), AppCMSUIKeyType.PAGE_WEIGHT_VALUE_TEXT);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_player_height_value_key), AppCMSUIKeyType.PAGE_HEIGHT_VALUE_TEXT);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_player_birthdate_value_label_key), AppCMSUIKeyType.PAGE_BIRTHDATE_VALUE_TEXT);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_player_hometown_value_label_key), AppCMSUIKeyType.PAGE_HOMETOWN_VALUE_TEXT);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_player_name_key), AppCMSUIKeyType.PAGE_PLAYER_NAME_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_photogallery_preButton), AppCMSUIKeyType.PAGE_PHOTOGALLERY_PRE_BUTTON_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_photogallery_nextButton), AppCMSUIKeyType.PAGE_PHOTOGALLERY_NEXT_BUTTON_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_photogallery_next_gallery), AppCMSUIKeyType.PAGE_PHOTOGALLERY_NEXT_GALLERY_LABEL_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_photogallery_prev_gallery), AppCMSUIKeyType.PAGE_PHOTOGALLERY_PREV_GALLERY_LABEL_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_photo_gallery_grid_key), AppCMSUIKeyType.PAGE_PHOTOGALLERY_GRID_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_photogallery_image_key), AppCMSUIKeyType.PAGE_PHOTO_GALLERY_IMAGE_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_closed_captions_toggle_button_key), AppCMSUIKeyType.PAGE_CLOSED_CAPTIONS_TOGGLE_BUTTON_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_user_management_autoplay_text_key), AppCMSUIKeyType.PAGE_USER_MANAGEMENT_AUTOPLAY_TEXT_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_user_management_caption_title_text_key), AppCMSUIKeyType.PAGE_USER_MANAGEMENT_CAPTION_TEXT_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_user_management_dowload_video_qality_title_text_key), AppCMSUIKeyType.PAGE_USER_MANAGEMENT_DOWNLOAD_VIDEO_QUALITY_TEXT_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_user_management_cell_data_title_text_key), AppCMSUIKeyType.PAGE_USER_MANAGEMENT_CELL_DATA_TEXT_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_player_height_label_key), AppCMSUIKeyType.PAGE_HEIGHT_LABEL_TEXT);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_player_weight_label_key), AppCMSUIKeyType.PAGE_WEIGHT_LABEL_TEXT);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_plan_single_plan_subscribe_text_key), AppCMSUIKeyType.PAGE_SINGLE_PLAN_SUBSCRIBE_TEXT_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_plan_feature_text_key), AppCMSUIKeyType.PAGE_PLAN_FEATURE_TEXT_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_plan_title_key), AppCMSUIKeyType.PAGE_PLAN_TITLE_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_plan_priceinfo_key), AppCMSUIKeyType.PAGE_PLAN_PRICEINFO_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_plan_image_key), AppCMSUIKeyType.PAGE_PLAN_IMAGE_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_plan_strike_out_priceinfo_key), AppCMSUIKeyType.PAGE_PLAN_STRIKE_OUT_PRICEINFO_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_plan_strike_out_line_key),AppCMSUIKeyType.PAGE_PLAN_STRIKE_OUT_LINNE);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_plan_description_key), AppCMSUIKeyType.PAGE_PLAN_DESCRIPTION_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_plan_bestvalue_key), AppCMSUIKeyType.PAGE_PLAN_BESTVALUE_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_plan_purchase_button_key), AppCMSUIKeyType.PAGE_PLAN_PURCHASE_BUTTON_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_plan_meta_dataview_key), AppCMSUIKeyType.PAGE_PLAN_META_DATA_VIEW_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_article_module_key), AppCMSUIKeyType.PAGE_ARTICLE_MODULE_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_history_module_key), AppCMSUIKeyType.PAGE_HISTORY_01_MODULE_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_watchlist_module_key), AppCMSUIKeyType.PAGE_WATCHLIST_01_MODULE_KEY);
        jsonValueKeyMap.put(context.getString(R.string.appcms_follow_module), AppCMSUIKeyType.PAGE_FOLLOW_01_MODULE_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_mylibrary_module_key), AppCMSUIKeyType.PAGE_LIBRARY_01_MODULE_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_mylibrary_module_key2), AppCMSUIKeyType.PAGE_LIBRARY_02_MODULE_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_bundle_detail_01_module_key), AppCMSUIKeyType.PAGE_BUNDLEDETAIL_01_MODULE_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_history_module_key2), AppCMSUIKeyType.PAGE_HISTORY_02_MODULE_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_history_module_key4), AppCMSUIKeyType.PAGE_HISTORY_04_MODULE_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_watchlist_module_key2), AppCMSUIKeyType.PAGE_WATCHLIST_02_MODULE_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_watchlist_module_key3), AppCMSUIKeyType.PAGE_WATCHLIST_03_MODULE_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_download_module_key), AppCMSUIKeyType.PAGE_DOWNLOAD_01_MODULE_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_playlist_module_key), AppCMSUIKeyType.PAGE_PLAYLIST_MODULE_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_fight_list_module_key), AppCMSUIKeyType.PAGE_FIGHT_LIST_PARENT_MODULE_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_sub_nav_module_key), AppCMSUIKeyType.PAGE_SUB_NAV_MODULE_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_fight_list_module_key), AppCMSUIKeyType.PAGE_SUB_NAV_MODULE_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_download_module_key2), AppCMSUIKeyType.PAGE_DOWNLOAD_02_MODULE_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_continue_watching_module_key), AppCMSUIKeyType.PAGE_CONTINUE_WATCHING_MODULE_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_continue_watching_02_module_key), AppCMSUIKeyType.PAGE_CONTINUE_WATCHING_02_MODULE_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_ac_web_frame_01), AppCMSUIKeyType.PAGE_AC_WEB_FRAME_03_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_settings_component_key), AppCMSUIKeyType.PAGE_SETTINGS_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_user_management_download_settings_key), AppCMSUIKeyType.PAGE_USER_MANAGEMENT_DOWNLOADS_MODULE_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_watchlist_duration_key), AppCMSUIKeyType.PAGE_WATCHLIST_DURATION_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_history_duration_key), AppCMSUIKeyType.PAGE_WATCHLIST_DURATION_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_watchlist_duration_unit_key), AppCMSUIKeyType.PAGE_WATCHLIST_DURATION_UNIT_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_watchlist_description_key), AppCMSUIKeyType.PAGE_WATCHLIST_DESCRIPTION_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_api_episode_title_key), AppCMSUIKeyType.PAGE_EPISODE_TITLE_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_contact_number_label), AppCMSUIKeyType.CONTACT_US_PHONE_LABEL);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_email_id_label), AppCMSUIKeyType.CONTACT_US_EMAIL_LABEL);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_history_last_added_label), AppCMSUIKeyType.PAGE_HISTORY_LAST_ADDED_LABEL_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_history_last_added_date_label), AppCMSUIKeyType.PAGE_HISTORY_LAST_ADDED_DATE_LABEL_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_fighter_name_label_key), AppCMSUIKeyType.PAGE_FIGHTER_LABEL_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_fighter_selector_view_key), AppCMSUIKeyType.PAGE_FIGHTER_SELECTOR_VIEW_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_fighter_selector_arrow_view_key), AppCMSUIKeyType.PAGE_FIGHTER_SELECTOR_ARROW_VIEW_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_signup_footer_label_key), AppCMSUIKeyType.PAGE_SIGNUP_FOOTER_LABEL_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_api_history_module_key), AppCMSUIKeyType.PAGE_API_HISTORY_MODULE_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_module_showdetail_key), AppCMSUIKeyType.PAGE_API_SHOWDETAIL_MODULE_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_show_detail_module_key), AppCMSUIKeyType.PAGE_API_SHOWDETAIL_MODULE_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_expanded_module), AppCMSUIKeyType.PAGE_EXPANDED_VIEW_MODULE_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_team_detail_module_key), AppCMSUIKeyType.PAGE_API_TEAMDETAIL_MODULE_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_multitabletray_key), AppCMSUIKeyType.PAGE_API_MULTITABLE_TEAM_MODULE_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_subscription_page_key), AppCMSUIKeyType.PAGE_SUBSCRIPTION_PAGE_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_subscription_selectionplan_02_key), AppCMSUIKeyType.PAGE_SUBSCRIPTION_SELECTPLAN_02_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_subscription_selectionplan_03_key), AppCMSUIKeyType.PAGE_SUBSCRIPTION_SELECTPLAN_03_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_photo_gallery_02_key), AppCMSUIKeyType.PAGE_PHOTO_GALLERY_TRAY_02_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_photo_gallery_grid_01_key), AppCMSUIKeyType.PAGE_PHOTO_GALLERY_GRID_01_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_article_tray_key), AppCMSUIKeyType.PAGE_ARTICLE_TRAY_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_subscription_selectionplan_01_key), AppCMSUIKeyType.PAGE_SUBSCRIPTION_SELECTPLAN_01_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_subscription_imagetextrow_key), AppCMSUIKeyType.PAGE_SUBSCRIPTION_IMAGEROW_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_subscription_imagetextrow_02_key), AppCMSUIKeyType.PAGE_SUBSCRIPTION_IMAGEROW_02_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_season_tray_module_key), AppCMSUIKeyType.PAGE_SEASON_TRAY_MODULE_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_season_tab_module_key), AppCMSUIKeyType.PAGE_SEASON_TAB_MODULE_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_episode_tray_module_key), AppCMSUIKeyType.PAGE_EPISODE_TRAY_MODULE_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_segment_tray_module_key), AppCMSUIKeyType.PAGE_SEGMENT_TRAY_MODULE_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_search_tray_module_key), AppCMSUIKeyType.PAGE_SEARCH_TRAY_MODULE_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_search_tray_02_module_key), AppCMSUIKeyType.PAGE_SEARCH_TRAY_02_MODULE_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_carousel_module_key), AppCMSUIKeyType.PAGE_CAROUSEL_MODULE_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_carousel_landscape_module_key), AppCMSUIKeyType.PAGE_CAROUSEL_LANDSACPE_MODULE_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_event_carousel_module_key), AppCMSUIKeyType.PAGE_EVENT_CAROUSEL_MODULE_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_event_carousel_03_module_key), AppCMSUIKeyType.PAGE_EVENT_CAROUSEL_03_MODULE_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_video_player_module_key), AppCMSUIKeyType.PAGE_VIDEO_PLAYER_MODULE_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_video_player_02_module_key), AppCMSUIKeyType.PAGE_VIDEO_PLAYER_02_MODULE_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_setting_module), AppCMSUIKeyType.PAGE_SETTINGS_MODULE_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_user_management_module_key), AppCMSUIKeyType.PAGE_USER_MANAGEMENT_MODULE_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_user_management_module_key4), AppCMSUIKeyType.PAGE_USER_MANAGEMENT_MODULE_KEY4);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_user_management_module_key3), AppCMSUIKeyType.PAGE_USER_MANAGEMENT_MODULE_KEY3);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_authentication_module), AppCMSUIKeyType.PAGE_AUTHENTICATION_MODULE_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_redeem_offer_module), AppCMSUIKeyType.PAGE_REDEEM_OFFER_MODULE_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_game_detail_module), AppCMSUIKeyType.PAGE_GAME_DETAIL_MODULE_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_fightsummary_key_type), AppCMSUIKeyType.PAGE_FIGHT_SUMMARY_MODULE_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_player_detail_module), AppCMSUIKeyType.PAGE_PLAYER_DETAIL_MODULE_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_event_detail_module), AppCMSUIKeyType.PAGE_EVENT_DETAIL_MODULE_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_tray_module_key), AppCMSUIKeyType.PAGE_TRAY_MODULE_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_tray_02_module_key), AppCMSUIKeyType.PAGE_TRAY_02_MODULE_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_tray_05_module_key), AppCMSUIKeyType.PAGE_TRAY_05_MODULE_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_fight_table_key), AppCMSUIKeyType.PAGE_FIGHT_TABLE_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_tray_05_module_key), AppCMSUIKeyType.PAGE_EVENT_DETAIL_MODULE_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_schedule_module_key), AppCMSUIKeyType.PAGE_AC_TEAM_SCHEDULE_MODULE_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_ancillary_pages_module), AppCMSUIKeyType.PAGE_AC_RICH_TEXT_MODULE_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_tray_bundle_module_key), AppCMSUIKeyType.PAGE_AC_BUNDLEDETAIL_TRAY_MODULE_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_parent_bg_key), AppCMSUIKeyType.PAGE_AC_TEAM_PARENT_BG_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_roster_page_module_key), AppCMSUIKeyType.PAGE_AC_ROSTER_MODULE_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_search_module_key), AppCMSUIKeyType.PAGE_AC_SEARCH_MODULE_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_tray_06_module_key), AppCMSUIKeyType.PAGE_TRAY_06_MODULE_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_tray_03_module_key), AppCMSUIKeyType.PAGE_TRAY_03_MODULE_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_bannerAd_module_key), AppCMSUIKeyType.PAGE_BANNER_AD_MODULE_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_mediumRectangleAd_module_key), AppCMSUIKeyType.PAGE_MEDIAM_RECTANGLE_AD_MODULE_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_photo_tray_module_key), AppCMSUIKeyType.PAGE_PHOTO_TRAY_MODULE_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_brand_tray_module_key), AppCMSUIKeyType.PAGE_BRAND_TRAY_MODULE_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_tray_08_module_key), AppCMSUIKeyType.PAGE_TRAY_08_MODULE_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_article_feed_module_key), AppCMSUIKeyType.PAGE_ARTICLE_FEED_MODULE_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_person_details_01_module_key), AppCMSUIKeyType.PAGE_PERSON_DETAIL_01_MODULE_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_person_details_02_module_key), AppCMSUIKeyType.PAGE_PERSON_DETAIL_02_MODULE_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_person_details_03_module_key), AppCMSUIKeyType.PAGE_PERSON_DETAIL_03_MODULE_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_show_details_04_module_key), AppCMSUIKeyType.PAGE_SHOW_DETAIL_04_MODULE_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_grid_module_key), AppCMSUIKeyType.PAGE_GRID_MODULE_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_video_player_with_info_key), AppCMSUIKeyType.PAGE_VIDEO_DETAILS_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_api_video_detail_module_key), AppCMSUIKeyType.PAGE_VIDEO_DETAILS_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_login_component_key), AppCMSUIKeyType.PAGE_LOGIN_COMPONENT_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_create_login_component_key), AppCMSUIKeyType.PAGE_CREATE_LOGIN_COMPONENT_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_download_video_tab_component_key), AppCMSUIKeyType.PAGE_DOWNLOAD_VIDEO_TAB_COMPONENT_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_download_audio_tab_component_key), AppCMSUIKeyType.PAGE_DOWNLOAD_AUDIO_TAB_COMPONENT_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_signup_component_key), AppCMSUIKeyType.PAGE_SIGNUP_COMPONENT_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_removeall_key), AppCMSUIKeyType.PAGE_REMOVEALL_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_game_tickets_key), AppCMSUIKeyType.PAGE_GAME_TICKETS_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_video_image_key), AppCMSUIKeyType.PAGE_VIDEO_IMAGE_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_countDown_timer_key), AppCMSUIKeyType.PAGE_COUNTDOWN_TIMER_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_show_image_video_key), AppCMSUIKeyType.PAGE_VIDEO_IMAGE_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_show_image_thumbnail_video_key), AppCMSUIKeyType.PAGE_SHOW_IMAGE_THUMBNAIL_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_show_image_video_key), AppCMSUIKeyType.PAGE_SHOW_IMAGE_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_show_gradient_image_video_key), AppCMSUIKeyType.PAGE_SHOW_GRADIENT_IMAGE_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_episode_image_video_key), AppCMSUIKeyType.PAGE_SHOW_EPISODE_IMAGE_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_video_trailer_key), AppCMSUIKeyType.DETAIL_PAGE_TRAILER);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_bottom_Sheet_key), AppCMSUIKeyType.BOTTOM_SHEET_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_bedge_image_key), AppCMSUIKeyType.PAGE_BEDGE_IMAGE_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_thumbnail_image_video_key), AppCMSUIKeyType.PAGE_THUMBNAIL_VIDEO_IMAGE_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_thumbnail_image_bundle_key), AppCMSUIKeyType.PAGE_THUMBNAIL_BUNDLE_IMAGE_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_start_watching_button_key), AppCMSUIKeyType.PAGE_START_WATCHING_BUTTON_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_start_watching_from_beginning_button_key), AppCMSUIKeyType.PAGE_START_WATCHING_FROM_BEGINNING_BUTTON_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_show_start_watching_button_key), AppCMSUIKeyType.PAGE_SHOW_START_WATCHING_BUTTON_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_bundle_start_watching_button_key), AppCMSUIKeyType.PAGE_BUNDLE_START_WATCHING_BUTTON_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_video_play_button_key), AppCMSUIKeyType.PAGE_VIDEO_PLAY_BUTTON_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_show_detail_button_key), AppCMSUIKeyType.PAGE_SHOW_DETAIL_BUTTON_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_play_episode_button_key), AppCMSUIKeyType.PAGE_PLAY_EPISODE_BUTTON_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_video_description_key), AppCMSUIKeyType.PAGE_VIDEO_DESCRIPTION_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_instructor_video_description_key), AppCMSUIKeyType.PAGE_INSTRUCTOR_VIDEO_DESCRIPTION_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_instructor_detail_badge_image_key), AppCMSUIKeyType.PAGE_INSTRUCTOR_DETAIL_BADGE_IMAGE_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_show_video_description_key), AppCMSUIKeyType.PAGE_VIDEO_DESCRIPTION_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_video_title_key), AppCMSUIKeyType.PAGE_VIDEO_TITLE_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_show_title_key), AppCMSUIKeyType.PAGE_SHOW_TITLE_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_link_title_key), AppCMSUIKeyType.PAGE_LINK_TITLE_GRID_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_link_item_title_key), AppCMSUIKeyType.PAGE_LINK_ITEM_TITLE_GRID_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_download_setting_title_key), AppCMSUIKeyType.PAGE_DOWNLOAD_SETTING_TITLE_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_video_subtitle_key), AppCMSUIKeyType.PAGE_VIDEO_SUBTITLE_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_show_video_subtitle_key), AppCMSUIKeyType.PAGE_SHOW_SUBTITLE_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_recent_class_title_key), AppCMSUIKeyType.PAGE_RECENT_CLASS_TITLE_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_bundle_video_subtitle_key), AppCMSUIKeyType.PAGE_BUNDLE_SUBTITLE_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_video_share_key), AppCMSUIKeyType.PAGE_VIDEO_SHARE_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_video_cast_key), AppCMSUIKeyType.PAGE_VIDEO_CAST_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_video_close_key), AppCMSUIKeyType.PAGE_VIDEO_CLOSE_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_browse_concept_key), AppCMSUIKeyType.PAGE_BROWSE_CONCEPT_BUTTON_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_back_arrow_key), AppCMSUIKeyType.PAGE_BACK_ARROW_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_start_workout_button_key), AppCMSUIKeyType.PAGE_START_WORKOUT);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_video_starrating_key), AppCMSUIKeyType.PAGE_VIDEO_STARRATING_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_video_ageLabel_key), AppCMSUIKeyType.PAGE_VIDEO_AGE_LABEL_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_video_credits_director_key), AppCMSUIKeyType.PAGE_VIDEO_CREDITS_DIRECTOR_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_video_credits_directedby_key), AppCMSUIKeyType.PAGE_VIDEO_CREDITS_DIRECTEDBY_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_video_credits_directors), AppCMSUIKeyType.PAGE_VIDEO_CREDITS_DIRECTORS_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_video_credits_starring_key), AppCMSUIKeyType.PAGE_VIDEO_CREDITS_STARRING_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_watchTrailer_key), AppCMSUIKeyType.PAGE_VIDEO_WATCH_TRAILER_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_barrier_key), AppCMSUIKeyType.PAGE_BARRIER_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_show_watchTrailer_key), AppCMSUIKeyType.PAGE_SHOW_WATCH_TRAILER_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_bundle_watchTrailer_key), AppCMSUIKeyType.PAGE_BUNDLE_WATCH_TRAILER_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_api_title_key), AppCMSUIKeyType.PAGE_API_TITLE);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_plan_title_key), AppCMSUIKeyType.PAGE_PLAN_TITLE);

        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_expire_time_key), AppCMSUIKeyType.PAGE_EXPIRE_TIME_TITLE);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_title_key), AppCMSUIKeyType.PAGE_API_TITLE);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_watchlist_page_title_key), AppCMSUIKeyType.WATCHLIST_PAGE_API_TITLE);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_history_page_title_key), AppCMSUIKeyType.HISTORY_PAGE_API_TITLE);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_contact_us_page_title_key), AppCMSUIKeyType.CONTACT_US_PAGE_API_TITLE);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_language_settings_page_title_key), AppCMSUIKeyType.LANGUAGE_SETTINGS_PAGE_API_TITLE);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_account_page_title_key), AppCMSUIKeyType.ACCOUNT_PAGE_API_TITLE);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_you_are_signed_in_as_label_key), AppCMSUIKeyType.YOU_ARE_SIGNED_IN_AS_LABEL_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_api_show_title_key), AppCMSUIKeyType.PAGE_SHOW_TITLE_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_api_bundle_title_key), AppCMSUIKeyType.PAGE_API_TITLE);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_bundle_tray_title_key), AppCMSUIKeyType.PAGE_TRAY_BUNDLE_TITLE_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_switch_seasons_key), AppCMSUIKeyType.PAGE_SHOW_SWITCH_SEASONS_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_api_thumbnail_url_key), AppCMSUIKeyType.PAGE_API_THUMBNAIL_URL);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_header_view), AppCMSUIKeyType.PAGE_HEADER_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_videodetail_header_view), AppCMSUIKeyType.PAGE_VIDEO_DETAIL_HEADER_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_autoplay_module_key_01), AppCMSUIKeyType.PAGE_AUTOPLAY_MODULE_KEY_01);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_autoplay_module_key_02), AppCMSUIKeyType.PAGE_AUTOPLAY_MODULE_KEY_02);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_autoplay_module_key_03), AppCMSUIKeyType.PAGE_AUTOPLAY_MODULE_KEY_03);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_autoplay_module_key_04), AppCMSUIKeyType.PAGE_AUTOPLAY_MODULE_KEY_04);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_autoplay_landscape_module_key_01), AppCMSUIKeyType.PAGE_AUTOPLAY_LANDSCAPE_MODULE_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_autoplay_portrait_module_key_01), AppCMSUIKeyType.PAGE_AUTOPLAY_PORTRAIT_MODULE_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_download_setting_module_key), AppCMSUIKeyType.PAGE_DOWNLOAD_SETTING_MODULE_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_language_setting_module_key), AppCMSUIKeyType.PAGE_LANGUAGE_SETTING_MODULE_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_autoplay_back_key), AppCMSUIKeyType.PAGE_AUTOPLAY_BACK_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_autoplay_finished_up_title_key), AppCMSUIKeyType.PAGE_AUTOPLAY_FINISHED_UP_TITLE_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_autoplay_movie_title_key), AppCMSUIKeyType.PAGE_AUTOPLAY_MOVIE_TITLE_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_autoplay_finished_movie_title_key), AppCMSUIKeyType.PAGE_AUTOPLAY_FINISHED_MOVIE_TITLE_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_autoplay_finished_movie_image_key), AppCMSUIKeyType.PAGE_AUTOPLAY_FINISHED_MOVIE_IMAGE_KEY);
        jsonValueKeyMap.put(context.getString(R.string.icon_image_key), AppCMSUIKeyType.PAGE_ICON_IMAGE_KEY);
        jsonValueKeyMap.put(context.getString(R.string.icon_label_key), AppCMSUIKeyType.PAGE_ICON_LABEL_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_series_image_key),
                AppCMSUIKeyType.PAGE_SERIES_IMAGE_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_autoplay_movie_subheading_key),
                AppCMSUIKeyType.PAGE_AUTOPLAY_MOVIE_SUBHEADING_KEY);

        jsonValueKeyMap.put(context.getString(R.string.app_cms_autoplay_movie_description_key),
                AppCMSUIKeyType.PAGE_AUTOPLAY_MOVIE_DESCRIPTION_KEY);

//        jsonValueKeyMap.put(context.getString(R.string.app_cms_autoplay_plan_description_key),
//                AppCMSUIKeyType.PAGE_PLAN_DESCRIPTION_KEY);

        jsonValueKeyMap.put(context.getString(R.string.app_cms_autoplay_movie_star_rating_key),
                AppCMSUIKeyType.PAGE_AUTOPLAY_MOVIE_STAR_RATING_KEY);

        jsonValueKeyMap.put(context.getString(R.string.app_cms_autoplay_movie_director_label_key),
                AppCMSUIKeyType.PAGE_AUTOPLAY_MOVIE_DIRECTOR_LABEL_KEY);

        jsonValueKeyMap.put(context.getString(R.string.app_cms_autoplay_movie_sub_director_label_key),
                AppCMSUIKeyType.PAGE_AUTOPLAY_MOVIE_SUB_DIRECTOR_LABEL_KEY);

        jsonValueKeyMap.put(context.getString(R.string.app_cms_autoplay_movie_image_key),
                AppCMSUIKeyType.PAGE_AUTOPLAY_MOVIE_IMAGE_KEY);

        jsonValueKeyMap.put(context.getString(R.string.app_cms_autoplay_play_button_key),
                AppCMSUIKeyType.PAGE_AUTOPLAY_MOVIE_PLAY_BUTTON_KEY);

        jsonValueKeyMap.put(context.getString(R.string.app_cms_autoplay_cancel_button_key),
                AppCMSUIKeyType.PAGE_AUTOPLAY_MOVIE_CANCEL_BUTTON_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_autoplay_cancel_button),
                AppCMSUIKeyType.PAGE_AUTOPLAY_CANCEL_BUTTON_KEY);

        jsonValueKeyMap.put(context.getString(R.string.app_cms_autoplay_playing_in_label_key),
                AppCMSUIKeyType.PAGE_AUTOPLAY_MOVIE_PLAYING_IN_LABEL_KEY);

        jsonValueKeyMap.put(context.getString(R.string.app_cms_autoplay_countdown_cancelled_label_key),
                AppCMSUIKeyType.PAGE_AUTOPLAY_MOVIE_COUNTDOWN_CANCELLED_LABEL_KEY);

        jsonValueKeyMap.put(context.getString(R.string.app_cms_autoplay_timer_label_key),
                AppCMSUIKeyType.PAGE_AUTOPLAY_MOVIE_TIMER_LABEL_KEY);

        jsonValueKeyMap.put(context.getString(R.string.app_cms_download_quality_continue_button_key),
                AppCMSUIKeyType.PAGE_DOWNLOAD_QUALITY_CONTINUE_BUTTON_KEY);

        jsonValueKeyMap.put(context.getString(R.string.app_cms_download_quality_cancel_button_key),
                AppCMSUIKeyType.PAGE_DOWNLOAD_QUALITY_CANCEL_BUTTON_KEY);

        jsonValueKeyMap.put(context.getString(R.string.app_cms_toggle_switch),
                AppCMSUIKeyType.PAGE_SETTING_TOGGLE_SWITCH_TYPE);

        jsonValueKeyMap.put(context.getString(R.string.app_cms_autoplay_toggle_switch_key),
                AppCMSUIKeyType.PAGE_SETTING_AUTOPLAY_TOGGLE_SWITCH_KEY);

        jsonValueKeyMap.put(context.getString(R.string.app_cms_closed_caption_toggle_switch_key),
                AppCMSUIKeyType.PAGE_SETTING_CLOSED_CAPTION_TOGGLE_SWITCH_KEY);

        jsonValueKeyMap.put(context.getString(R.string.app_cms_live_toggle_switch_key),
                AppCMSUIKeyType.PAGE_LIVE_TOGGLE_SWITCH_KEY);

        jsonValueKeyMap.put(context.getString(R.string.app_cms_cellular_data_toggle_switch_key),
                AppCMSUIKeyType.PAGE_DOWNLOAD_VIA_CELLULAR_NETWORK_KEY);

        jsonValueKeyMap.put(context.getString(R.string.app_cms_logout_button_key),
                AppCMSUIKeyType.PAGE_SETTING_LOGOUT_BUTTON_KEY);

        jsonValueKeyMap.put(context.getString(R.string.app_cms_title_label),
                AppCMSUIKeyType.PAGE_WATCHLIST_TITLE_LABEL);

        jsonValueKeyMap.put(context.getString(R.string.app_cms_library_item_title_label),
                AppCMSUIKeyType.PAGE_LIBRARY_ITEM_TITLE_LABEL);

        jsonValueKeyMap.put(context.getString(R.string.app_cms_description_label),
                AppCMSUIKeyType.PAGE_WATCHLIST_DESCRIPTION_LABEL);

        jsonValueKeyMap.put(context.getString(R.string.app_cms_instructor_title_label),
                AppCMSUIKeyType.PAGE_INSTRUCTOR_TITLE_LABEL);

        jsonValueKeyMap.put(context.getString(R.string.app_cms_segment_title_label),
                AppCMSUIKeyType.PAGE_SEGMENT_TITLE_KEY);

        jsonValueKeyMap.put(context.getString(R.string.app_cms_subtitle_label),
                AppCMSUIKeyType.PAGE_WATCHLIST_SUBTITLE_LABEL);

        jsonValueKeyMap.put(context.getString(R.string.app_cms_delete_item_button),
                AppCMSUIKeyType.PAGE_WATCHLIST_DELETE_ITEM_BUTTON);

        jsonValueKeyMap.put(context.getString(R.string.app_cms_signup_footer_label_key),
                AppCMSUIKeyType.PAGE_SIGNUP_FOOTER_LABEL_KEY);

        jsonValueKeyMap.put(context.getString(R.string.app_cms_autoplay_up_next_loader_key),
                AppCMSUIKeyType.PAGE_AUTOPLAY_UP_NEXT_LOADER_KEY);

        jsonValueKeyMap.put(context.getString(R.string.app_cms_autoplay_rotating_loader_view),
                AppCMSUIKeyType.PAGE_AUTOPLAY_ROTATING_LOADER_VIEW_KEY);

        jsonValueKeyMap.put(context.getString(R.string.app_cms_settings_subscription_duration_label),
                AppCMSUIKeyType.PAGE_SETTINGS_SUBSCRIPTION_DURATION_LABEL_KEY);

        jsonValueKeyMap.put(context.getString(R.string.app_cms_settings_subscription_end_date_label),
                AppCMSUIKeyType.PAGE_SETTINGS_SUBSCRIPTION_END_DATE_LABEL_KEY);

        jsonValueKeyMap.put(context.getString(R.string.app_cms_settings_manage_subscription_button_key),
                AppCMSUIKeyType.PAGE_SETTINGS_MANAGE_SUBSCRIPTION_BUTTON_KEY);

        jsonValueKeyMap.put(context.getString(R.string.app_cms_settings_manage_recommend_button_key),
                AppCMSUIKeyType.PAGE_SETTINGS_MANAGE_RECOMMEND_BUTTON_KEY);

        jsonValueKeyMap.put(context.getString(R.string.app_cms_plan_page_subscribe_now_button_key),
                AppCMSUIKeyType.PAGE_PLAN_SUBSCRIBE_NOW_BUTTON_KEY);

        jsonValueKeyMap.put(context.getString(R.string.app_cms_settings_subscription_label_key),
                AppCMSUIKeyType.PAGE_SETTINGS_SUBSCRIPTION_LABEL_KEY);

        jsonValueKeyMap.put(context.getString(R.string.app_cms_settings_user_email_label_key),
                AppCMSUIKeyType.PAGE_SETTINGS_USER_EMAIL_LABEL_KEY);

        jsonValueKeyMap.put("", AppCMSUIKeyType.PAGE_EMPTY_KEY);
        jsonValueKeyMap.put(null, AppCMSUIKeyType.PAGE_NULL_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_grid_option_key), AppCMSUIKeyType.PAGE_GRID_OPTION_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_thumbnail_option_key), AppCMSUIKeyType.PAGE_GRID_THUMBNAIL_INFO);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_photo_gallery_thumbnail_option_key), AppCMSUIKeyType.PAGE_GRID_PHOTO_GALLERY_THUMBNAIL_INFO);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_watchlist_duration_label), AppCMSUIKeyType.PAGE_WATCHLIST_DURATION_KEY_BG);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_fitness_watchlist_duration_label), AppCMSUIKeyType.PAGE_FITNESS_WATCHLIST_DURATION_KEY_BG);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_bookmark_dration_cat_label), AppCMSUIKeyType.PAGE_BOOKMARK_DURATION_CAT_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_search_result_count_label), AppCMSUIKeyType.PAGE_SEARCH_RESULT_COUNT_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_fitness_category_label), AppCMSUIKeyType.PAGE_FITNESS_CATEGORY_KEY_BG);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_thumbnail_badge_image), AppCMSUIKeyType.PAGE_THUMBNAIL_BADGE_IMAGE);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_banner_image_key), AppCMSUIKeyType.PAGE_BANNER_IMAGE);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_banner_detail_key), AppCMSUIKeyType.PAGE_BANNER_DETAIL_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_banner_detail_icon_key), AppCMSUIKeyType.PAGE_BANNER_DETAIL_ICON);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_banner_detail_background_key), AppCMSUIKeyType.PAGE_BANNER_DETAIL_BACKGROUND);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_banner_detail_button_key), AppCMSUIKeyType.PAGE_BANNER_DETAIL_BUTTON);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_banner_detail_title_key), AppCMSUIKeyType.PAGE_BANNER_DETAIL_TITLE);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_delete_watchlist_key), AppCMSUIKeyType.PAGE_DELETE_WATCHLIST_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_delete_history_key), AppCMSUIKeyType.PAGE_DELETE_HISTORY_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_bpm_icon_key), AppCMSUIKeyType.PAGE_BPM_ICON_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_bookmark_flag_key), AppCMSUIKeyType.PAGE_BOOKMARK_FLAG_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_bookmark_flag_delete_key), AppCMSUIKeyType.PAGE_BOOKMARK_FLAG_DELETE_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_fitness_time_key), AppCMSUIKeyType.PAGE_FITNESS_TIME_ICON_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_table_background_view), AppCMSUIKeyType.PAGE_GRID_BACKGROUND);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_download_duration_key), AppCMSUIKeyType.PAGE_DOWNLOAD_DURATION_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_download_duration_key), AppCMSUIKeyType.PAGE_EPISODE_DURATION_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_download_description_key), AppCMSUIKeyType.PAGE_DOWNLOAD_DESCRIPTION_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_history_description_key), AppCMSUIKeyType.PAGE_HISTORY_DESCRIPTION_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_history_duration_key), AppCMSUIKeyType.PAGE_HISTORY_DURATION_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_playlist_audio_duration_key), AppCMSUIKeyType.PAGE_AUDIO_DURATION_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_history_watched_time_key), AppCMSUIKeyType.PAGE_HISTORY_WATCHED_TIME_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_game_time_key), AppCMSUIKeyType.PAGE_GAME_TIME_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_game_date_key), AppCMSUIKeyType.PAGE_GAME_DATE_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_delete_download_key), AppCMSUIKeyType.PAGE_DELETE_DOWNLOAD_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_download_size_key), AppCMSUIKeyType.PAGE_DELETE_DOWNLOAD_VIDEO_SIZE_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_episode_duration_key), AppCMSUIKeyType.PAGE_EPISODE_DURATION_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_rawhtml_title_key), AppCMSUIKeyType.RAW_HTML_TITLE_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_rawhtml_image_key), AppCMSUIKeyType.RAW_HTML_IMAGE_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_playlist_title_key), AppCMSUIKeyType.PAGE_PLAYLIST_TITLE);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_playlist_sub_title_key), AppCMSUIKeyType.PAGE_PLAYLIST_SUB_TITLE);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_playlist_audio_artist_key), AppCMSUIKeyType.PAGE_PLAYLIST_AUDIO_ARTIST_TITLE);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_audio_download_button_key), AppCMSUIKeyType.PAGE_AUDIO_DOWNLOAD_BUTTON_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_playlist_download_button_key), AppCMSUIKeyType.PAGE_PLAYLIST_DOWNLOAD_BUTTON_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_audio_tray_module_key), AppCMSUIKeyType.PAGE_AUDIO_TRAY_MODULE_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_link_your_account_btn_key), AppCMSUIKeyType.PAGE_LINK_YOUR_ACOOUNT_BTN_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_link_your_account_with_tv_provider_btn_key), AppCMSUIKeyType.PAGE_LINK_YOUR_ACOOUNT_WITH_TV_PROVIDER_BTN_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_link_your_account_text_key), AppCMSUIKeyType.PAGE_LINK_YOUR_ACCOUNT_TEXT_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_select_your_plan_text_key), AppCMSUIKeyType.PAGE_SELECT_YOUR_PLAN_TEXT_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_subscription_metadata_text_key), AppCMSUIKeyType.PAGE_SUBSCRIPTION_METADATA_TEXT_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_dont_have_an_account_text_key), AppCMSUIKeyType.PAGE_DONT_HAVE_AN_ACCOUNT_TEXT_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_or_separator_text_key), AppCMSUIKeyType.PAGE_OR_SEPARATOR_LABEL_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_open_sign_up_button_key), AppCMSUIKeyType.OPEN_SIGN_UP_PAGE_BUTTON_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_link_your_account_module), AppCMSUIKeyType.PAGE_LINK_YOUR_ACCOUNT_MODULE_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_action_cancel_key), AppCMSUIKeyType.CANCEL_BUTTON_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_request_new_code_btn_key), AppCMSUIKeyType.REQUEST_NEW_CODE);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_code_sync_text_line_1), AppCMSUIKeyType.CODE_SYNC_TEXT_LINE_1);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_code_sync_text_line_2), AppCMSUIKeyType.CODE_SYNC_TEXT_LINE_2);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_code_sync_text_line_3), AppCMSUIKeyType.CODE_SYNC_TEXT_LINE_3);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_code_sync_text_line_header), AppCMSUIKeyType.CODE_SYNC_TEXT_LINE_HEADER);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_code_link_account_activate_device_label), AppCMSUIKeyType.CODE_LINK_ACCOUNT_ACTIVATE_DEVICE_LABEL);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_tab_layout), AppCMSUIKeyType.PAGE_TABLAYOUT_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_linear_layout_key), AppCMSUIKeyType.PAGE_LINEAR_LAYOUT_KEY);
        jsonValueKeyMap.put(context.getString(R.string.manageLanguageBtnKey), AppCMSUIKeyType.MANAGE_LANGUAGE_KEY);
        jsonValueKeyMap.put(context.getString(R.string.changeLanguageLabel), AppCMSUIKeyType.LANGUAGE_LABEL_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_pagename_change_language_key), AppCMSUIKeyType.LANGUAGE_SCREEN_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_link_change_language_action), AppCMSUIKeyType.CHANGE_LANGUAGE_KEY);
        jsonValueKeyMap.put(context.getString(R.string.weatherImage), AppCMSUIKeyType.WEATHER_IMAGE);
        jsonValueKeyMap.put(context.getString(R.string.weatherTrayTitle), AppCMSUIKeyType.WEATHER_TRAY_TITLE);
        jsonValueKeyMap.put(context.getString(R.string.cloudImage), AppCMSUIKeyType.WEATHER_CLOUD_IMAGE);
        jsonValueKeyMap.put(context.getString(R.string.dropImage), AppCMSUIKeyType.WEATHER_DROP_IMAGE);
        jsonValueKeyMap.put(context.getString(R.string.weatherWidgetView), AppCMSUIKeyType.WEATHER_WIDGET_VIEW);
        jsonValueKeyMap.put(context.getString(R.string.tempLowLabel), AppCMSUIKeyType.WEATHER_TEMP_LOW_LABEL);
        jsonValueKeyMap.put(context.getString(R.string.tempHighLabel), AppCMSUIKeyType.WEATHER_TEMP_HIGH_LABEL);
        jsonValueKeyMap.put(context.getString(R.string.view), AppCMSUIKeyType.VIEW);
        jsonValueKeyMap.put(context.getString(R.string.shortPhraseLabel), AppCMSUIKeyType.WEATHER_SHORT_PHRASE_LABEL);
        jsonValueKeyMap.put(context.getString(R.string.weatherThumbnailTitle), AppCMSUIKeyType.WEATHER_THUMBNAIL_TITLE_LABEL);
        jsonValueKeyMap.put(context.getString(R.string.dropPercentagelabel), AppCMSUIKeyType.WEATHER_DROP_PERCENTAGE_LABEL);
        jsonValueKeyMap.put(context.getString(R.string.cDataLabel), AppCMSUIKeyType.WEATHER_CDATA);
        jsonValueKeyMap.put(context.getString(R.string.dateLabel), AppCMSUIKeyType.WEATHER_DATE_LABEL);
        jsonValueKeyMap.put(context.getString(R.string.timeLabel), AppCMSUIKeyType.WEATHER_TIME_LABEL);
        jsonValueKeyMap.put(context.getString(R.string.locationImage), AppCMSUIKeyType.WEATHER_LOCATION_IMAGE);
        jsonValueKeyMap.put(context.getString(R.string.cityLabel), AppCMSUIKeyType.WEATHER_CITY_LABEL);
        jsonValueKeyMap.put(context.getString(R.string.tempratureLabel), AppCMSUIKeyType.WEATHER_TEMP_TICKER_LABEL);
        jsonValueKeyMap.put(context.getString(R.string.weather_page), AppCMSUIKeyType.WEATHER_PAGE);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_see_all_category_module_key), AppCMSUIKeyType.PAGE_SEE_ALL_CATEGORY_02_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_see_all_category01_module_key), AppCMSUIKeyType.PAGE_SEE_ALL_CATEGORY_01_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_see_all_category_name_key), AppCMSUIKeyType.PAGE_SEE_ALL_CATEGORY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_textbox_type), AppCMSUIKeyType.PAGE_TEXTBOX);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_last_name_key), AppCMSUIKeyType.PAGE_LAST_NAME);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_last_name_input_key), AppCMSUIKeyType.PAGE_LAST_NAME_INPUT);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_first_name_input_key), AppCMSUIKeyType.PAGE_FIRST_NAME_INPUT);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_terms_key), AppCMSUIKeyType.PAGE_TERMS);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_email_input_key), AppCMSUIKeyType.PAGE_EMAIL_INPUT);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_password_input_key), AppCMSUIKeyType.PAGE_PASSWORD_INPUT);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_sign_up_button_key), AppCMSUIKeyType.PAGE_SIGNUP_BUTTON);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_image_facebook_key), AppCMSUIKeyType.PAGE_IMAGE_FACEBOOK);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_login_button_key), AppCMSUIKeyType.PAGE_LOGIN_BUTTON_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_right_arrow_key), AppCMSUIKeyType.PAGE_RIGHT_ARROW_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_left_arrow_key), AppCMSUIKeyType.PAGE_LEFT_ARROW_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_thumbnail_image_tray07_key), AppCMSUIKeyType.PAGE_THUMBNAIL_IMAGE_TRAY07_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_tray_bottom_text1_key), AppCMSUIKeyType.PAGE_TRAY_BOTTOM_TEXT1_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_tray_bottom_text2_key), AppCMSUIKeyType.PAGE_TRAY_BOTTOM_TEXT2_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_duration_category_key), AppCMSUIKeyType.PAGE_TRAY_DURATION_CATEGORY_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_play_image1_key), AppCMSUIKeyType.PAGE_PLAY_IMAGE1_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_signin_button_key), AppCMSUIKeyType.PAGE_SIGNIN_BUTTON_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_facebook_signup_key), AppCMSUIKeyType.PAGE_FACEBOOK_SIGNUP_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_horizontal_line), AppCMSUIKeyType.PAGE_HORIZONTAL_LINE);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_date_separator), AppCMSUIKeyType.PAGE_DATE_SEPARATOR);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_item_background), AppCMSUIKeyType.PAGE_ITEM_BACKGROUND);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_item_line), AppCMSUIKeyType.PAGE_ITEM_LINE);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_constraint_tab_guide_line_center), AppCMSUIKeyType.PAGE_CONSTRAINT_TAB_GUIDE_LINE_CENTER);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_body_focus_label), AppCMSUIKeyType.PAGE_BODY_FOCUS_LABLE);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_body_focus_label_value), AppCMSUIKeyType.PAGE_BODY_FOCUS_LABLE_VALUE);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_set_up_label), AppCMSUIKeyType.PAGE_SET_UP_LABLE);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_set_up_required_label), AppCMSUIKeyType.PAGE_SET_UP_REQUIRED_LABLE);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_set_up_optional_label), AppCMSUIKeyType.PAGE_SET_UP_OPTIONAL_LABLE);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_filter_page_name), AppCMSUIKeyType.ANDROID_FILTER_PAGE);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_search_filter_key), AppCMSUIKeyType.PAGE_SEARCH_FILTER);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_close_page_key), AppCMSUIKeyType.PAGE_CLOSE);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_name_key), AppCMSUIKeyType.LABEL_PAGE_NAME);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_clear_filter_key), AppCMSUIKeyType.LABEL_CLEAR_FILTER);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_show_classes_key), AppCMSUIKeyType.LABEL_SHOW_CLASSES);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_view_gravity_bottom_center_key), AppCMSUIKeyType.VIEW_GRAVITY_BOTTOM_CENTER);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_view_gravity_center_vertical_end_key), AppCMSUIKeyType.VIEW_GRAVITY_VERTICAL_CENTER_END);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_view_gravity_left_key), AppCMSUIKeyType.VIEW_GRAVITY_START);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_view_gravity_start_key), AppCMSUIKeyType.VIEW_GRAVITY_START);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_view_gravity_right_key), AppCMSUIKeyType.VIEW_GRAVITY_END);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_view_gravity_end_key), AppCMSUIKeyType.VIEW_GRAVITY_END);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_view_gravity_center_horizontal_key), AppCMSUIKeyType.VIEW_GRAVITY_CENTER_HORIZONTAL);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_view_gravity_center_vertical_key), AppCMSUIKeyType.VIEW_GRAVITY_CENTER_VERTICAL);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_video_playlist_module_key), AppCMSUIKeyType.PAGE_VIDEO_PLAYLIST_MODULE_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_video_playlist_page_key), AppCMSUIKeyType.VIDEO_PLAYLIST_PAGE);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_class_format_label), AppCMSUIKeyType.PAGE_CLASS_FORMAT_LABLE);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_class_format_value_key), AppCMSUIKeyType.PAGE_CLASS_FORMAT_VALUE);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_view_all_detail), AppCMSUIKeyType.PAGE_VIEW_ALL_DETAIL);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_thumbnail_category_title), AppCMSUIKeyType.PAGE_THUMBNAIL_CATEGORY_TITLE);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_expendable_list), AppCMSUIKeyType.PAGE_EXPENDABLE_LIST);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_thumbnail_brand_and_title), AppCMSUIKeyType.PAGE_THUMBNAIL_BRAND_AND_TITLE);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_thumbnail_time_and_category), AppCMSUIKeyType.PAGE_THUMBNAIL_TIME_AND_CATEGORY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_guide_line_for_image), AppCMSUIKeyType.PAGE_GUIDE_LINE_FOR_IMAGE);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_badge_detail_page_image), AppCMSUIKeyType.PAGE_BADGE_DETAIL_PAGE_IMAGE);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_no_classes_scheduled), AppCMSUIKeyType.PAGE_NO_CLASSES_SCHEDULED);
        jsonValueKeyMap.put(context.getString(R.string.view_filter_button_key), AppCMSUIKeyType.VIEW_FILTER_BUTTON);
        jsonValueKeyMap.put(context.getString(R.string.page_filter), AppCMSUIKeyType.FILTER_PAGE);
        jsonValueKeyMap.put(context.getString(R.string.view_image_concept), AppCMSUIKeyType.PAGE_CONCEPT_IMAGE_TRAY_KEY);
        jsonValueKeyMap.put(context.getString(R.string.follow_thumbnail_image), AppCMSUIKeyType.PAGE_FOLLOW_THUMBNAIL_IMAGE_KEY);
        jsonValueKeyMap.put(context.getString(R.string.view_browse_sort), AppCMSUIKeyType.VIEW_BROWSE_SORT);
        jsonValueKeyMap.put(context.getString(R.string.label_profile_downloads), AppCMSUIKeyType.LABEL_PROFILE_DOWNLOAD);
        jsonValueKeyMap.put(context.getString(R.string.page_profile), AppCMSUIKeyType.PROFLE_PAGE);
        jsonValueKeyMap.put(context.getString(R.string.page_edit_profile), AppCMSUIKeyType.EDIT_PROFLE_PAGE);
        jsonValueKeyMap.put(context.getString(R.string.image_take_photo), AppCMSUIKeyType.IMAGE_TAKE_PHOTO);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_resets_password_page_key), AppCMSUIKeyType.RESET_PASSWORD_PAGE);
        jsonValueKeyMap.put(context.getString(R.string.or_label_key), AppCMSUIKeyType.TEXT_VIEW_OR);
        jsonValueKeyMap.put(context.getString(R.string.subscriptionItemBackground), AppCMSUIKeyType.SUBSCRIPTION_ITEM_BACKGROUND);
        jsonValueKeyMap.put(context.getString(R.string.skip_plan), AppCMSUIKeyType.BTN_SKIP_PLAN);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_settings_name_label_key), AppCMSUIKeyType.TEXT_VIEW_NAME);
        jsonValueKeyMap.put(context.getString(R.string.text_view_email_label), AppCMSUIKeyType.TEXT_VIEW_EMAIL);
        jsonValueKeyMap.put(context.getString(R.string.button_edit_profile), AppCMSUIKeyType.BUTTON_EDIT_PROFILE);
        jsonValueKeyMap.put(context.getString(R.string.button_change_password), AppCMSUIKeyType.BUTTON_CHANGE_PASSWORD);
        jsonValueKeyMap.put(context.getString(R.string.text_view_subscription_and_billing_label), AppCMSUIKeyType.TEXT_VIEW_SUBSCRIPTION_AND_BILLING);
        jsonValueKeyMap.put(context.getString(R.string.text_view_plan_label), AppCMSUIKeyType.TEXT_VIEW_PLAN);
        jsonValueKeyMap.put(context.getString(R.string.text_view_app_settings_label), AppCMSUIKeyType.TEXT_VIEW_APP_SETTINGS);
        jsonValueKeyMap.put(context.getString(R.string.text_view_app_version_label), AppCMSUIKeyType.TEXT_VIEW_APP_VERSION);
        jsonValueKeyMap.put(context.getString(R.string.text_view_download_settings_label), AppCMSUIKeyType.TEXT_VIEW_DOWNLOAD_SETTINGS);
        jsonValueKeyMap.put(context.getString(R.string.text_view_download_quality_label), AppCMSUIKeyType.TEXT_VIEW_DOWNLOAD_QUALITY);
        jsonValueKeyMap.put(context.getString(R.string.text_view_cellular_data_label), AppCMSUIKeyType.TEXT_VIEW_CELLULAR_DATA);
        jsonValueKeyMap.put(context.getString(R.string.text_view_language_label), AppCMSUIKeyType.TEXT_VIEW_LANGUAGE);
        jsonValueKeyMap.put(context.getString(R.string.button_manage_download), AppCMSUIKeyType.BUTTON_MANAGE_DOWNLOAD);
        jsonValueKeyMap.put(context.getString(R.string.page_account_settings), AppCMSUIKeyType.PAGE_ACCOUNT_SETTINGS);
        jsonValueKeyMap.put(context.getString(R.string.page_my_account), AppCMSUIKeyType.PAGE_MY_ACCOUNT);

        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_player_detail), AppCMSUIKeyType.PAGE_PLAYER_DETAIL);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_entitlement_screen), AppCMSUIKeyType.PAGE_ENTITLEMENT_SCREEN);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_entitlement_module_key), AppCMSUIKeyType.PAGE_ENTITLEMENT_MODULE_KEY);
        jsonValueKeyMap.put(context.getString(R.string.view_type_text_input_layout), AppCMSUIKeyType.VIEW_TYPE_TEXT_INPUT_LAYOUT);
        jsonValueKeyMap.put(context.getString(R.string.collection_grid_watchlist), AppCMSUIKeyType.COLLECTIONGRID_WATCHLIST);
        jsonValueKeyMap.put(context.getString(R.string.text_view_play), AppCMSUIKeyType.TEXT_VIEW_PLAY);
        jsonValueKeyMap.put(context.getString(R.string.text_view_share), AppCMSUIKeyType.TEXT_VIEW_SHARE);
        jsonValueKeyMap.put(context.getString(R.string.text_view_download), AppCMSUIKeyType.TEXT_VIEW_DOWNLOAD);
        jsonValueKeyMap.put(context.getString(R.string.text_view_save), AppCMSUIKeyType.TEXT_VIEW_SAVE);
        jsonValueKeyMap.put(context.getString(R.string.text_view_content_title), AppCMSUIKeyType.TEXT_VIEW_CONTENT_TITLE);
        jsonValueKeyMap.put(context.getString(R.string.text_view_content_description), AppCMSUIKeyType.TEXT_VIEW_CONTENT_DESCRIPTION);
        jsonValueKeyMap.put(context.getString(R.string.text_view_video_duration), AppCMSUIKeyType.TEXT_VIEW_VIDEO_DURATION);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_numbertextfield_key), AppCMSUIKeyType.PAGE_NUMBERTEXTFIELD_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_register_button_key), AppCMSUIKeyType.PAGE_REGISTER_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_rest_button_key), AppCMSUIKeyType.PAGE_REST_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_page_login_key), AppCMSUIKeyType.PAGE_LOGIN_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_app_logo_key), AppCMSUIKeyType.APP_LOGO_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_app_invalid_details_key), AppCMSUIKeyType.INVALID_DETAILS_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_action_loginWithFacebook_key), AppCMSUIKeyType.LOGIN_WITH_FACEBOOK);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_action_loginWithGoogle_key), AppCMSUIKeyType.LOGIN_WITH_GOOGLE);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_notification_key), AppCMSUIKeyType.ANDROID_NOTIFICATION_SCREEN_KEY);
        //JusPay Payment gateway
        jsonValueKeyMap.put(context.getString(R.string.app_cms_pagename_juspayPayment_screen_key), AppCMSUIKeyType.JUSPAY_PAYMENT_SCREEN_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_walletListView_key), AppCMSUIKeyType.WALLET_LIST_VIEW_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_savedCardList_key), AppCMSUIKeyType.SAVED_CARD_LIST_VIEW_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_NetBankingList_key), AppCMSUIKeyType.NETBANKING_LIST_VIEW_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_addNewCard_title_key), AppCMSUIKeyType.PAGE_ADD_NEW_CARD_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_moreBanks_title_key), AppCMSUIKeyType.PAGE_MORE_BANKS_TITLE_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_walletImage_key), AppCMSUIKeyType.WALLET_IMAGE_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_walletTitle_key), AppCMSUIKeyType.WALLET_TITLE_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_upiTitleError_key), AppCMSUIKeyType.UPI_TITLE_ERROR_KEY);

        jsonValueKeyMap.put(context.getString(R.string.app_cms_upiEditText_key), AppCMSUIKeyType.UPI_EDIT_TEXT_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_upiPayButton_key), AppCMSUIKeyType.PAGE_UPI_PAY_BUTTON_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_ui_page_bankListView_key), AppCMSUIKeyType.BANK_LIST_VIEW_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_ui_page_backButton_key), AppCMSUIKeyType.PAGE_BACK_BUTTON);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_blockView_key), AppCMSUIKeyType.PAGE_BLOCK_VIEW_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_cardEditText_key), AppCMSUIKeyType.CARD_EDIT_TEXT_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_cardPayButton_key), AppCMSUIKeyType.PAGE_CARD_PAY_BUTTON_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_cardIconsList_key), AppCMSUIKeyType.CARD_ICONS_LIST_VIEW_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_upiIconsList_key), AppCMSUIKeyType.UPI_ICONS_LIST_VIEW_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_cardCheckBox_key), AppCMSUIKeyType.CARD_CHECKBOX_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_walletOfferText_key), AppCMSUIKeyType.WALLET_OFFER_TEXT);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_alreadyHaveAnAccountText_key), AppCMSUIKeyType.ALREADY_HAVE_AN_ACCOUNT_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_news_thumbnail_image_key), AppCMSUIKeyType.NEWS_THUMBNAIL_IMAGE);
        jsonValueKeyMap.put(context.getString(R.string.standaloneplayer02), AppCMSUIKeyType.PAGE_STAND_ALONE_VIDEO_PLAYER02);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_parent_linear_view_key), AppCMSUIKeyType.PAGE_PARENT_LINEAR_VIEW);
        jsonValueKeyMap.put(context.getString(R.string.carousel_nodule_seven), AppCMSUIKeyType.PAGE_CAROUSEL_07_MODULE_KEY);
        jsonValueKeyMap.put(context.getString(R.string.text_view_season), AppCMSUIKeyType.SEASON_KEY);
        jsonValueKeyMap.put(context.getString(R.string.text_view_episodes), AppCMSUIKeyType.EPISODES_KEY);
        jsonValueKeyMap.put(context.getString(R.string.text_view_segments), AppCMSUIKeyType.SEGMENTS_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_date_separatorView_key), AppCMSUIKeyType.DATE_SEPARATOR_VIEW_KEY);
        jsonValueKeyMap.put(context.getString(R.string.collection_grid_vertical), AppCMSUIKeyType.COLLECTIONGRID_VERTICAL);
        jsonValueKeyMap.put(context.getString(R.string.vertical_collection_grid_viewPlan),AppCMSUIKeyType.VERTICAL_COLLECTION_GRID_VIEWPLAN);
        jsonValueKeyMap.put(context.getString(R.string.collection_grid_horizontal), AppCMSUIKeyType.COLLECTIONGRID_HORIZONTAL);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_trailer_thumbnail_image_key), AppCMSUIKeyType.TRAILER_THUMBNAIL_IMAGE_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_module_key_showdetail_06), AppCMSUIKeyType.PAGE_MODULE_KEY_SHOWDETAILS_06);
        jsonValueKeyMap.put(context.getString(R.string.navigationtableView), AppCMSUIKeyType.NAVIGATION_TABLE_VIEW);
        jsonValueKeyMap.put(context.getString(R.string.navigationtitleview), AppCMSUIKeyType.NAVIGATION_TITLE_LABEL);
        jsonValueKeyMap.put(context.getString(R.string.pageLinkTitle), AppCMSUIKeyType.PAGE_LINK_TITLE);
        jsonValueKeyMap.put(context.getString(R.string.pageLinkTableView), AppCMSUIKeyType.PAGE_LINK_TABLE_VIEW);
        jsonValueKeyMap.put(context.getString(R.string.pageLinkTableGridView), AppCMSUIKeyType.PAGE_LINK_TABLE_GRID_VIEW);
        jsonValueKeyMap.put(context.getString(R.string.pageLinkHeaderTitle), AppCMSUIKeyType.PAGE_LINK_HEADER_TITLE);
        jsonValueKeyMap.put(context.getString(R.string.pageLinkGridImage), AppCMSUIKeyType.PAGE_LINK_GRID_IMAGE);
        jsonValueKeyMap.put(context.getString(R.string.view_table_layout), AppCMSUIKeyType.VIEW_TABLE_LAYOUT);
        jsonValueKeyMap.put(context.getString(R.string.plan_features_table), AppCMSUIKeyType.TABLE_PLAN_FEATURES);
        jsonValueKeyMap.put(context.getString(R.string.button_subscribe), AppCMSUIKeyType.BUTTON_SUBSCRIBE);
        jsonValueKeyMap.put(context.getString(R.string.button_browse), AppCMSUIKeyType.BUTTON_BROWSE);
        jsonValueKeyMap.put(context.getString(R.string.floating_button), AppCMSUIKeyType.BUTTON_FLOATING);
        jsonValueKeyMap.put(context.getString(R.string.floating_button_subscribe), AppCMSUIKeyType.BUTTON__FLOATING_SUBSCRIBE);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_account_header_view_key),
                AppCMSUIKeyType.PAGE_SETTING_ACCOUNT_HEADER_VIEW_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_account_section_view_key),
                AppCMSUIKeyType.PAGE_SETTING_MODULE_VIEW);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_account_section_component_view_key),
                AppCMSUIKeyType.PAGE_SETTING_MODULE_COMPONENT_VIEW);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_setting_email_label_key),
                AppCMSUIKeyType.PAGE_SETTING_EMAIL_LABEL_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_setting_password_label_key),
                AppCMSUIKeyType.PAGE_SETTING_PASSWORD_LABEL_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_setting_password_value_key),
                AppCMSUIKeyType.PAGE_SETTING_PASSWORD_VALUE_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_setting_connected_account_label_key),
                AppCMSUIKeyType.PAGE_SETTING_CONNECTED_ACCOUNT_LABEL_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_setting_your_interest_value_key),
                AppCMSUIKeyType.PAGE_SETTING_YOUR_INTEREST_VALUE_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_sign_up_module_view_key),
                AppCMSUIKeyType.PAGE_SIGN_UP_MODULE_VIEW);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_login_module_view_key),
                AppCMSUIKeyType.PAGE_LOGIN_MODULE_VIEW);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_login_module_component_view_key),
                AppCMSUIKeyType.PAGE_LOGIN_MODULE_COMPONENT_VIEW);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_link_your_account_view_key),
                AppCMSUIKeyType.PAGE_LINK_YOUR_ACCOUNT_VIEW_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_login_view_key),
                AppCMSUIKeyType.PAGE_LOGIN_VIEW_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_login_module_component_view_with_short_code_key),
                AppCMSUIKeyType.PAGE_LINK_YOUR_ACCOUNT_WITH_SHORT_CODE_VIEW_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_login_module_component_or_separator_view_key),
                AppCMSUIKeyType.PAGE_LOGIN_MODULE_COMPONENT_OR_SEPARATOR_VIEW_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_link_your_account_with_tv_provider_view_key),
                AppCMSUIKeyType.PAGE_LINK_YOUR_ACCOUNT_WITH_TV_PROVIDER_VIEW_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_setting_account_name_container_view),
                AppCMSUIKeyType.PAGE_SETTING_ACCOUNT_DETAIL_NAME_CONTAINER_VIEW);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_setting_account_email_container_view),
                AppCMSUIKeyType.PAGE_SETTING_ACCOUNT_DETAIL_EMAIL_CONTAINER_VIEW);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_setting_account_password_container_view),
                AppCMSUIKeyType.PAGE_SETTING_ACCOUNT_DETAIL_PASSWORD_CONTAINER_VIEW);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_setting_account_details_component_view),
                AppCMSUIKeyType.PAGE_SETTING_ACCOUNT_DETAIL_COMPONENT_VIEW);
        jsonValueKeyMap.put(context.getString(R.string.text_view_plan_terms),
                AppCMSUIKeyType.TEXT_VIEW_PLAN_TERMS);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_login_with_emailpassword_key), AppCMSUIKeyType.LOGIN_WITH_EMAIL_PASWWORD);
        jsonValueKeyMap.put(context.getString(R.string.ui_block_recommend_01),
                AppCMSUIKeyType.RECOMMENDATION_TRAY_01);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_video_whatsapp_key), AppCMSUIKeyType.PAGE_VIDEO_WHATSAPP_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_setting_subscriptionGroup_key), AppCMSUIKeyType.PAGE_SETTING_SUBSCRIPTION_GROUP_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_setting_downloadGroup_key), AppCMSUIKeyType.PAGE_SETTING_DOWNLOAD_GROUP_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_setting_personalizationGroup_key), AppCMSUIKeyType.PAGE_SETTING_PERSONALIZATION_GROUP_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_settings_page_personalization_key), AppCMSUIKeyType.PAGE_SETTING_PERSONALIZATION_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_setting_parentalControlsHeader_key), AppCMSUIKeyType.PAGE_SETTINGS_PARENTAL_CONTROLS_HEADER_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_settings_page_parentalControls_key), AppCMSUIKeyType.PAGE_SETTINGS_PARENTAL_CONTROLS_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_group_key), AppCMSUIKeyType.PAGE_GROUP_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_setting_autoPlayGroup_key), AppCMSUIKeyType.PAGE_SETTING_AUTO_PLAY_GROUP_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_setting_mobileInfoGroup_key), AppCMSUIKeyType.PAGE_SETTING_MOBILE_INFO_GROUP_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_setting_parentalControlsGroup_key), AppCMSUIKeyType.PAGE_SETTING_PARENTAL_CONTROL_GROUP_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_setting_parentalControlToggle_key), AppCMSUIKeyType.PAGE_SETTING_PARENTAL_CONTROL_SWITCH);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_login_passwordHintView), AppCMSUIKeyType.PAGE_LOGIN_PASSWORD_HINT_VIEW);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_juspay_walletGroup_key), AppCMSUIKeyType.PAGE_JUSPAY_WALLET_GROUP_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_juspay_netBankingGroup_key), AppCMSUIKeyType.PAGE_JUSPAY_NETBANKING_GROUP_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_juspay_cardGroup_key), AppCMSUIKeyType.PAGE_JUSPAY_CARD_GROUP_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_juspay_upiGroup_key), AppCMSUIKeyType.PAGE_JUSPAY_UPI_GROUP_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_expand_details_key), AppCMSUIKeyType.PAGE_EXPAND_DETAILS_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_expand_shows_description_key), AppCMSUIKeyType.PAGE_EXPAND_SHOWS_DESCRIPTION);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_phonetextfield_key), AppCMSUIKeyType.PAGE_PHONETEXTFIELD_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_phonecountrycode_key), AppCMSUIKeyType.PAGE_PHONE_COUNTRY_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_live_player_component), AppCMSUIKeyType.LIVE_PLAYER_COMPONENT);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_offer_plan_name_key), AppCMSUIKeyType.PAGE_OFFER_PLAN_NAME_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_offer_referredBy_key),AppCMSUIKeyType.PAGE_OFFER_REFERRED_BY_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_offer_price_key),AppCMSUIKeyType.PAGE_OFFER_PRICE_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_strike_through_key),AppCMSUIKeyType.STRIKE_THROUGH_PRICE);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_cross_image_key),AppCMSUIKeyType.OFFER_CROSS_IMAGE);
        jsonValueKeyMap.put(context.getString(R.string.rentOptionButton), AppCMSUIKeyType.RENT_OPTION_BTN_KEY);
        jsonValueKeyMap.put(context.getString(R.string.image_series_indicator), AppCMSUIKeyType.IMAGE_SERIES_INDICATOR);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_show_detail_module_key8), AppCMSUIKeyType.PAGE_API_SHOWDETAIL_08_MODULE_KEY);
        jsonValueKeyMap.put(context.getString(R.string.appcms_detail_module_six), AppCMSUIKeyType.PAGE_API_VIDEO_PLAYER_INFO_06_MODULE_KEY);
        jsonValueKeyMap.put(context.getString(R.string.appcms_detail_module_six), AppCMSUIKeyType.PAGE_API_VIDEO_PLAYER_INFO_07_MODULE_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_main_tvod_service_type_key), AppCMSUIKeyType.MAIN_TVOD_SERVICE_TYPE);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_bundle_detail_02_module_key), AppCMSUIKeyType.PAGE_BUNDLEDETAIL_02_MODULE_KEY);
        jsonValueKeyMap.put(context.getString(R.string.ui_block_bundleDetail_01), AppCMSUIKeyType.UI_BLOCK_BUNDLE_DETAIL_01);
        jsonValueKeyMap.put(context.getString(R.string.ui_block_bundleDetail_02), AppCMSUIKeyType.UI_BLOCK_BUNDLE_DETAIL_02);
        jsonValueKeyMap.put(context.getString(R.string.trailer_watch_button), AppCMSUIKeyType.PAGE_TRAILER_PLAY_BUTTON_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_login_text_key), AppCMSUIKeyType.PAGE_LOGIN_TEXT_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_plan_page_become_member_button_key),
                AppCMSUIKeyType.PAGE_PLAN_BECOME_MEMBER_BUTTON_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_plan_page_buy_button_key),
                AppCMSUIKeyType.PAGE_PLAN_BUY_BUTTON_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_tv_provider_logo), AppCMSUIKeyType.PAGE_TV_PROVIDER_LOGO);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_parent_linear_activate_device_view_key), AppCMSUIKeyType.PAGE_PARENT_LINEAR_ACTIVATE_DEVICE_VIEW);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_pagename_subscription_flow_page_key),AppCMSUIKeyType.SUBSCRIPTION_FLOW_PAGE_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_cardTitle_Error_key), AppCMSUIKeyType.CARD_TITLE_ERROR_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_netBankingTitle_Error_key), AppCMSUIKeyType.NETBANKING_TITLE_ERROR_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_upiTitle_Error_key), AppCMSUIKeyType.UPI_TITLE_ERROR_KEY);
    }

    private void createPageNameToActionMap(Context context) {
        this.pageNameToActionMap.put(context.getString(R.string.app_cms_pagename_splashscreen_key),
                context.getString(R.string.app_cms_action_authpage_key));
        this.pageNameToActionMap.put(context.getString(R.string.app_cms_pagename_homescreen_key),
                context.getString(R.string.app_cms_action_homepage_key));
        /*this.pageNameToActionMap.put(context.getString(R.string.app_cms_pagename_musicHub_screen_key),
                context.getString(R.string.app_cms_action_musicHub_page_key));*/
        this.pageNameToActionMap.put(context.getString(R.string.app_cms_pagename_historyscreen_key),
                context.getString(R.string.app_cms_action_historypage_key));
        this.pageNameToActionMap.put(context.getString(R.string.app_cms_pagename_history_screen_key),
                context.getString(R.string.app_cms_action_historypage_key));
        this.pageNameToActionMap.put(context.getString(R.string.app_cms_page_schedule_key),
                context.getString(R.string.app_cms_pagename_schedule_screen_key));
        this.pageNameToActionMap.put(context.getString(R.string.app_cms_pagename_watchlistscreen_key),
                context.getString(R.string.app_cms_action_watchlistpage_key));
        this.pageNameToActionMap.put(context.getString(R.string.app_cms_pagename_videoscreen_key),
                context.getString(R.string.app_cms_action_detailvideopage_key));

        this.pageNameToActionMap.put(context.getString(R.string.app_cms_pagename_bundlescreen_key),
                context.getString(R.string.app_cms_action_detailbundlepage_key));
        this.pageNameToActionMap.put(context.getString(R.string.app_cms_pagename_bundle_template_key),
                context.getString(R.string.app_cms_action_detailbundlepage_key));
        this.pageNameToActionMap.put(context.getString(R.string.app_cms_pagename_showscreen_key),
                context.getString(R.string.app_cms_action_showvideopage_key));
        this.pageNameToActionMap.put(context.getString(R.string.app_cms_pagename_articlescreen_key),
                context.getString(R.string.app_cms_action_articlepage_key));
        this.pageNameToActionMap.put(context.getString(R.string.app_cms_pagename_photogalleryscreen_key),
                context.getString(R.string.app_cms_action_photo_gallerypage_key));
        this.pageNameToActionMap.put(context.getString(R.string.app_cms_pagename_moviesscreen_key),
                context.getString(R.string.app_cms_action_moviespage_key));
        this.pageNameToActionMap.put(context.getString(R.string.app_cms_page_name_forgotpassword),
                context.getString(R.string.app_cms_action_change_password_key));
        this.pageNameToActionMap.put(context.getString(R.string.app_cms_pagename_link_your_account_key),
                context.getString(R.string.app_cms_link_your_account_action));
        this.pageNameToActionMap.put(context.getString(R.string.app_cms_pagename_change_language_key),
                context.getString(R.string.app_cms_link_change_language_action));
        this.pageNameToActionMap.put(context.getString(R.string.app_cms_see_all_category_name_key).trim(),
                context.getString(R.string.app_cms_see_all_category_action));
        this.pageNameToActionMap.put(context.getString(R.string.app_cms_page_instructor_page_name_key),
                context.getString(R.string.app_cms_instructor_details_action));
        this.pageNameToActionMap.put(context.getString(R.string.app_cms_pagename_create_login_key),
                context.getString(R.string.app_cms_launch_login_page_action));
        this.pageNameToActionMap.put(context.getString(R.string.app_cms_page_video_playlist_page_key).trim(),
                context.getString(R.string.app_cms_video_playlist_page_action));
    }

    private void createActionToPageMap(Context context) {
        this.actionToPageMap.put(context.getString(R.string.app_cms_action_authpage_key), null);
        this.actionToPageMap.put(context.getString(R.string.app_cms_action_homepage_key), null);
        this.actionToPageMap.put(context.getString(R.string.app_cms_action_historypage_key), null);
        this.actionToPageMap.put(context.getString(R.string.app_cms_action_videopage_key), null);
        this.actionToPageMap.put(context.getString(R.string.app_cms_action_detailvideopage_key), null);
        this.actionToPageMap.put(context.getString(R.string.app_cms_action_articlepage_key), null);
        this.actionToPageMap.put(context.getString(R.string.app_cms_action_photo_gallerypage_key), null);
        this.actionToPageMap.put(context.getString(R.string.app_cms_action_watchvideo_key), null);
        this.actionToPageMap.put(context.getString(R.string.app_cms_action_watchlistpage_key), null);
        this.actionToPageMap.put(context.getString(R.string.app_cms_action_showvideopage_key), null);
        this.actionToPageMap.put(context.getString(R.string.app_cms_pagename_moviesscreen_key), null);
        //this.actionToPageMap.put(context.getString(R.string.app_cms_action_musicHub_page_key), null);
        this.actionToPageMap.put(context.getString(R.string.app_cms_action_forgotpassword_key), null);
        this.actionToPageMap.put(context.getString(R.string.app_cms_action_change_password_key), null);
        this.actionToPageMap.put(context.getString(R.string.app_cms_link_your_account_action), null);
        this.actionToPageMap.put(context.getString(R.string.app_cms_link_change_language_action), null);
        this.actionToPageMap.put(context.getString(R.string.app_cms_instructor_details_action), null);
        this.actionToPageMap.put(context.getString(R.string.app_cms_action_detailbundlepage_key), null);
        this.actionToPageMap.put(context.getString(R.string.app_cms_see_all_category_action), null);
        this.actionToPageMap.put(context.getString(R.string.app_cms_video_playlist_page_action), null);
        this.actionToPageMap.put(context.getString(R.string.app_cms_launch_login_page_action), null);

    }

    private void createActionToPageAPIMap(Context context) {
        this.actionToPageAPIMap.put(context.getString(R.string.app_cms_action_authpage_key), null);
        this.actionToPageAPIMap.put(context.getString(R.string.app_cms_action_homepage_key), null);
        this.actionToPageAPIMap.put(context.getString(R.string.app_cms_action_videopage_key), null);
        this.actionToPageAPIMap.put(context.getString(R.string.app_cms_action_watchvideo_key), null);
        this.actionToPageAPIMap.put(context.getString(R.string.app_cms_action_showvideopage_key), null);
        this.actionToPageAPIMap.put(context.getString(R.string.app_cms_action_articlepage_key), null);
        this.actionToPageAPIMap.put(context.getString(R.string.app_cms_action_photo_gallerypage_key), null);
        this.actionToPageAPIMap.put(context.getString(R.string.app_cms_page_name_forgotpassword), null);
        this.actionToPageAPIMap.put(context.getString(R.string.app_cms_pagename_moviesscreen_key), null);
        //this.actionToPageAPIMap.put(context.getString(R.string.app_cms_action_musicHub_page_key), null);
        this.actionToPageAPIMap.put(context.getString(R.string.app_cms_action_forgotpassword_key), null);
        this.actionToPageAPIMap.put(context.getString(R.string.app_cms_instructor_details_action), null);
        this.actionToPageAPIMap.put(context.getString(R.string.app_cms_see_all_category_action), null);
        this.actionToPageAPIMap.put(context.getString(R.string.app_cms_video_playlist_page_action), null);
        this.actionToPageAPIMap.put(context.getString(R.string.app_cms_launch_login_page_action), null);
    }

    private void createActionToActionTypeMap(Context context) {
        actionToActionTypeMap.put(context.getString(R.string.app_cms_action_openFeedback),
                AppCMSActionType.OPEN_FEEDBACK_PAGE);
        actionToActionTypeMap.put(context.getString(R.string.app_cms_action_open_download_page),
                AppCMSActionType.OPEN_DOWNLOAD_PAGE);
        actionToActionTypeMap.put(context.getString(R.string.app_cms_action_openPolicy),
                AppCMSActionType.OPEN_POLICY_PAGE);
        actionToActionTypeMap.put(context.getString(R.string.app_cms_action_openTerms),
                AppCMSActionType.OPEN_TERMS_PAGE);
        actionToActionTypeMap.put(context.getString(R.string.app_cms_action_openAbout),
                AppCMSActionType.OPEN_ABOUT_PAGE);
        actionToActionTypeMap.put(context.getString(R.string.app_cms_action_openConcepts),
                AppCMSActionType.OPEN_CONCEPTS_PAGE);
        actionToActionTypeMap.put(context.getString(R.string.app_cms_action_openSignOut),
                AppCMSActionType.OPEN_SIGN_OUT_PAGE);
        actionToActionTypeMap.put(context.getString(R.string.app_cms_action_openSubscriptions),
                AppCMSActionType.OPEN_SUBSCRIPTIONS_PAGE);
        actionToActionTypeMap.put(context.getString(R.string.app_cms_action_openDevices),
                AppCMSActionType.OPEN_DEVICES_PAGE);
        actionToActionTypeMap.put(context.getString(R.string.app_cms_action_openSettings),
                AppCMSActionType.OPEN_SETTINGS_PAGE);
        actionToActionTypeMap.put(context.getString(R.string.app_cms_action_openHistory),
                AppCMSActionType.OPEN_HISTORY_PAGE);
        actionToActionTypeMap.put(context.getString(R.string.app_cms_action_openBookmarkedClasses),
                AppCMSActionType.OPEN_BOOKMARKED_CLASSES_PAGE);
        actionToActionTypeMap.put(context.getString(R.string.app_cms_action_openConceptsYouFollow),
                AppCMSActionType.OPEN_CONCEPTS_YOU_FOLLOW_PAGE);
        actionToActionTypeMap.put(context.getString(R.string.app_cms_action_openEditProfilePage),
                AppCMSActionType.OPEN_EDIT_PROFILE_PAGE);
        actionToActionTypeMap.put(context.getString(R.string.app_cms_action_open_instagram_key),
                AppCMSActionType.OPEN_INSTAGRAM);
        actionToActionTypeMap.put(context.getString(R.string.app_cms_action_open_twitter_key),
                AppCMSActionType.OPEN_TWITTER);
        actionToActionTypeMap.put(context.getString(R.string.app_cms_action_open_facebook_key),
                AppCMSActionType.OPEN_FACEBOOK);
        actionToActionTypeMap.put(context.getString(R.string.app_cms_action_navigation_to_home),
                AppCMSActionType.NAVIGATE_TO_HOME);
        actionToActionTypeMap.put(context.getString(R.string.app_cms_action_launch_Plan_Page),
                AppCMSActionType.LAUNCH_PLAN_PAGE);
        actionToActionTypeMap.put(context.getString(R.string.app_cms_action_launch_login),
                AppCMSActionType.LAUNCH_LOGIN);
        actionToActionTypeMap.put(context.getString(R.string.app_cms_action_subscribe_go),
                AppCMSActionType.SUBSCRIBEGO);

        actionToActionTypeMap.put(context.getString(R.string.app_cms_action_authpage_key),
                AppCMSActionType.SPLASH_PAGE);

        actionToActionTypeMap.put(context.getString(R.string.app_cms_action_authpage_key),
                AppCMSActionType.AUTH_PAGE);

        actionToActionTypeMap.put(context.getString(R.string.app_cms_pagename_homescreen_key),
                AppCMSActionType.HOME_PAGE);

        actionToActionTypeMap.put(context.getString(R.string.app_cms_action_browse_key),
                AppCMSActionType.HOME_PAGE);

        actionToActionTypeMap.put(context.getString(R.string.app_cms_action_historypage_key),
                AppCMSActionType.HISTORY_PAGE);


        actionToActionTypeMap.put(context.getString(R.string.app_cms_instructor_details_action),
                AppCMSActionType.INSTRUCTOR_PAGE);

        actionToActionTypeMap.put(context.getString(R.string.app_cms_action_watchlistpage_key),
                AppCMSActionType.WATCHLIST_PAGE);
        actionToActionTypeMap.put(context.getString(R.string.app_cms_action_musicHub_page_key),
                AppCMSActionType.MUSIC_PAGE);
        actionToActionTypeMap.put(context.getString(R.string.app_cms_action_playlistpage_key), AppCMSActionType.PLAYLIST_PAGE);

        actionToActionTypeMap.put(context.getString(R.string.app_cms_action_videopage_key), AppCMSActionType.PLAY_VIDEO_PAGE);

        actionToActionTypeMap.put(context.getString(R.string.app_cms_action_detailvideopage_key), AppCMSActionType.VIDEO_PAGE);

        actionToActionTypeMap.put(context.getString(R.string.app_cms_action_showvideopage_key), AppCMSActionType.SHOW_PAGE);

        actionToActionTypeMap.put(context.getString(R.string.app_cms_action_referralPlans_key), AppCMSActionType.REFERRAL_PAGE);

        actionToActionTypeMap.put(context.getString(R.string.app_cms_action_articlepage_key), AppCMSActionType.ARTICLE_PAGE);

        actionToActionTypeMap.put(context.getString(R.string.app_cms_action_photo_gallerypage_key), AppCMSActionType.PHOTO_GALLERY_PAGE);

        actionToActionTypeMap.put(context.getString(R.string.app_cms_action_moviespage_key), AppCMSActionType.MOVIES_PAGE);

        actionToActionTypeMap.put(context.getString(R.string.app_cms_action_watchvideo_key), AppCMSActionType.PLAY_VIDEO_PAGE);

        actionToActionTypeMap.put(context.getString(R.string.app_cms_action_watchtrailervideo_key), AppCMSActionType.WATCH_TRAILER);

        actionToActionTypeMap.put(context.getString(R.string.app_cms_action_share_key), AppCMSActionType.SHARE);

        actionToActionTypeMap.put(context.getString(R.string.app_cms_action_cast_key), AppCMSActionType.CAST_VIDEO);

        actionToActionTypeMap.put(context.getString(R.string.app_cms_action_close_key), AppCMSActionType.CLOSE);

        actionToActionTypeMap.put(context.getString(R.string.app_cms_action_cancel_key), AppCMSActionType.CLOSE);

        actionToActionTypeMap.put(context.getString(R.string.app_cms_action_login_key), AppCMSActionType.LOGIN);

        actionToActionTypeMap.put(context.getString(R.string.app_cms_action_signin_key), AppCMSActionType.SIGNIN);

        actionToActionTypeMap.put(context.getString(R.string.app_cms_action_forgotpassword_key), AppCMSActionType.FORGOT_PASSWORD);
        actionToActionTypeMap.put(context.getString(R.string.app_cms_action_resetPassword_key), AppCMSActionType.RESET_PASSWORD);
        actionToActionTypeMap.put(context.getString(R.string.app_cms_action_loginfacebook_key), AppCMSActionType.LOGIN_FACEBOOK);
        actionToActionTypeMap.put(context.getString(R.string.app_cms_action_redeem_code_key), AppCMSActionType.REDEEM_CODE);
        actionToActionTypeMap.put(context.getString(R.string.app_cms_action_signupfacebook_key), AppCMSActionType.SIGNUP_FACEBOOK);
        actionToActionTypeMap.put(context.getString(R.string.app_cms_action_logingoogle_key), AppCMSActionType.LOGIN_GOOGLE);
        actionToActionTypeMap.put(context.getString(R.string.app_cms_action_signupgoogle_key), AppCMSActionType.SIGNUP_GOOGLE);
        actionToActionTypeMap.put(context.getString(R.string.app_cms_action_signup_key), AppCMSActionType.SIGNUP);
        actionToActionTypeMap.put(context.getString(R.string.app_cms_action_logout_key), AppCMSActionType.LOGOUT);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_title_label), AppCMSUIKeyType.PAGE_WATCHLIST_TITLE_LABEL);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_description_label), AppCMSUIKeyType.PAGE_WATCHLIST_DESCRIPTION_LABEL);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_delete_history_item_button), AppCMSUIKeyType.PAGE_WATCHLIST_DELETE_ITEM_BUTTON);

        jsonValueKeyMap.put(context.getString(R.string.app_cms_description_label), AppCMSUIKeyType.PAGE_WATCHLIST_DESCRIPTION_LABEL);

        jsonValueKeyMap.put(context.getString(R.string.app_cms_delete_watchlist_item_button), AppCMSUIKeyType.PAGE_WATCHLIST_DELETE_ITEM_BUTTON);

        actionToActionTypeMap.put(context.getString(R.string.app_cms_action_startfreetrial_key), AppCMSActionType.START_TRIAL);

        actionToActionTypeMap.put(context.getString(R.string.app_cms_action_editprofile_key), AppCMSActionType.EDIT_PROFILE);

        actionToActionTypeMap.put(context.getString(R.string.app_cms_action_change_password_key), AppCMSActionType.CHANGE_PASSWORD);

        actionToActionTypeMap.put(context.getString(R.string.app_cms_action_managesubscription_key), AppCMSActionType.MANAGE_SUBSCRIPTION);

        actionToActionTypeMap.put(context.getString(R.string.app_cms_action_change_download_quality_key), AppCMSActionType.CHANGE_DOWNLOAD_QUALITY);

        actionToActionTypeMap.put(context.getString(R.string.app_cms_action_open_option_dialog), AppCMSActionType.OPEN_OPTION_DIALOG);

        actionToActionTypeMap.put(context.getString(R.string.app_cms_action_detailbundlepage_key), AppCMSActionType.PAGE_BUNDLE_KEY);

        actionToActionTypeMap.put(context.getString(R.string.app_cms_banner_detail_button_action_key), AppCMSActionType.BANNER_DETAIL_CLICK);

        actionToActionTypeMap.put(context.getString(R.string.app_cms_delete_single_watchlist_action), AppCMSActionType.DELETE_SINGLE_WATCHLIST_ITEM);
        actionToActionTypeMap.put(context.getString(R.string.app_cms_delete_single_history_action), AppCMSActionType.DELETE_SINGLE_HISTORY_ITEM);

        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_video_publishdate_key), AppCMSUIKeyType.PAGE_VIDEO_PUBLISHDATE_KEY);

        actionToActionTypeMap.put(context.getString(R.string.app_cms_delete_single_download_action), AppCMSActionType.DELETE_SINGLE_DOWNLOAD_ITEM);

        actionToActionTypeMap.put(context.getString(R.string.app_cms_link_your_account_action), AppCMSActionType.LINK_YOUR_ACCOUNT);

        actionToActionTypeMap.put(context.getString(R.string.app_cms_delete_single_download_action), AppCMSActionType.DELETE_SINGLE_DOWNLOAD_ITEM);

        actionToActionTypeMap.put(context.getString(R.string.changeLanguage), AppCMSActionType.CHANGE_LANGUAGE);
        actionToActionTypeMap.put(context.getString(R.string.app_cms_see_all_category_action), AppCMSActionType.SEE_ALL_CATEGORY);
        actionToActionTypeMap.put(context.getString(R.string.app_cms_launch_login_page_action), AppCMSActionType.LAUNCH_LOGIN_PAGE);
        actionToActionTypeMap.put(context.getString(R.string.app_cms_launch_login_page_action), AppCMSActionType.OPEN_SETUP_PROFILE_FOR_NEW_USER);
        actionToActionTypeMap.put(context.getString(R.string.app_cms_video_playlist_page_action), AppCMSActionType.LAUNCH_VIDEO_PLAYLIST_PAGE);
        actionToActionTypeMap.put(context.getString(R.string.app_cms_wallet_payment_page_action), AppCMSActionType.LAUNCH_WALLET_PAYMENT_PAGE);
        actionToActionTypeMap.put(context.getString(R.string.app_cms_net_banking_payment_page_action), AppCMSActionType.LAUNCH_NET_BANKING_PAYMENT_PAGE);
        actionToActionTypeMap.put(context.getString(R.string.app_cms_page_launch_whatsapp_action), AppCMSActionType.LAUNCH_WHATSAPP);
        actionToActionTypeMap.put(context.getString(R.string.app_cms_action_launchParentalControls), AppCMSActionType.LAUNCH_PARENTAL_CONTROLS_PAGE);
        actionToActionTypeMap.put(context.getString(R.string.app_cms_action_manageParentalControls), AppCMSActionType.MANAGE_PARENTAL_CONTROLS);
        actionToActionTypeMap.put(context.getString(R.string.app_cms_action_launchViewingRestrictionsPage), AppCMSActionType.LAUNCH_VIEWING_RESTRICTIONS_PAGE);
        actionToActionTypeMap.put(context.getString(R.string.app_cms_action_launchChangeVideoPinPage), AppCMSActionType.LAUNCH_CHANGE_VIDEO_PIN_PAGE);
        actionToActionTypeMap.put(context.getString(R.string.app_cms_action_saveVideoPin), AppCMSActionType.SAVE_VIDEO_PIN);
        actionToActionTypeMap.put(context.getString(R.string.app_cms_action_setParentalRating), AppCMSActionType.SET_PARENTAL_RATING);
        actionToActionTypeMap.put(context.getString(R.string.app_cms_hide_show_details_action),
                AppCMSActionType.HIDE_SHOW_DETAILS);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_settings_edit_phone_value_key), AppCMSUIKeyType.PAGE_SETTINGS_EDIT_PHONE_VALUE_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_settings_billing_history_key), AppCMSUIKeyType.PAGE_SETTINGS_BILLING_HISTORY_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_proceed_to_payment_key), AppCMSUIKeyType.PAGE_SETTINGS_REFERRAL_PROCEED_TO_PAYMENT_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_subscribe_button_for_view_plan), AppCMSUIKeyType.PAGE_SUBSCRIBE_BTN_FOR_PLAN_KEY);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_offer_image_key), AppCMSUIKeyType.PAGE_SETTINGS_OFFER_IMAGE_KEY);
        actionToActionTypeMap.put(context.getString(R.string.app_cms_page_settings_edit_phone_value_key), AppCMSActionType.OPEN_EDIT_PHONE_PAGE);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_term_and_condition_plan_page_key), AppCMSUIKeyType.PAGE_TERM_AND_CONDITION_PLAN);
        jsonValueKeyMap.put(context.getString(R.string.app_cms_page_plan_page_plan_benefit1), AppCMSUIKeyType.PAGE_PLAN_BENEFIT1);
    }


    @Provides
    @Singleton
    public AssetManager providesAssetManager() {
        return assetManager;
    }

    @Provides
    @Singleton
    public Gson providesGson() {
        return new GsonBuilder().registerTypeAdapterFactory(new Stag.Factory())
                .create();
        // return new Gson();
    }

    @Provides
    @Singleton
    public File providesStorageDirectory() {
        return storageDirectory;
    }

    @Provides
    @Singleton
    public OkHttpClient providesOkHttpClient() {
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.level(HttpLoggingInterceptor.Level.BODY);
        return new OkHttpClient.Builder()
                .connectTimeout(defaultConnectionTimeout, TimeUnit.MILLISECONDS)
                .writeTimeout(defaultWriteConnectionTimeout, TimeUnit.MILLISECONDS)
                .readTimeout(defaultReadConnectionTimeout, TimeUnit.MILLISECONDS)
//                .addInterceptor(logging)
                .cache(cache)
                .build();
    }

    @Provides
    @Singleton
    public Retrofit providesRetrofit(OkHttpClient client, Gson gson) {
        return new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create(gson))
                .baseUrl(baseUrl)
                .client(client)
                .build();
    }

    @Provides
    @Singleton
    public AppCMSMainUIRest providesAppCMSMainUIRest(Retrofit retrofit) {
        return retrofit.create(AppCMSMainUIRest.class);
    }

    @Provides
    @Singleton
    public AppCMSAndroidUIRest providesAppCMSAndroidUIRest(Retrofit retrofit) {
        return retrofit.create(AppCMSAndroidUIRest.class);
    }

    @Provides
    @Singleton
    public AppCMSPageUIRest providesAppCMSPageUIRest(Retrofit retrofit) {
        return retrofit.create(AppCMSPageUIRest.class);
    }

    @Provides
    @Singleton
    public AppCMSWatchlistRest providesAppCMSWatchlistRest(Retrofit retrofit) {
        return retrofit.create(AppCMSWatchlistRest.class);
    }

    @Provides
    @Singleton
    public AppCMSPlaylistRest providesAppCMSPlaylistRest(Retrofit retrofit) {
        return retrofit.create(AppCMSPlaylistRest.class);
    }

    @Provides
    @Singleton
    public AppCMSTeamStandingRest providesAppCMSStandingRest(Retrofit retrofit) {
        return retrofit.create(AppCMSTeamStandingRest.class);
    }

    @Provides
    @Singleton
    public AppCMSTeamRoasterRest providesAppCMSRoasterRest(Retrofit retrofit) {
        return retrofit.create(AppCMSTeamRoasterRest.class);
    }

    @Provides
    @Singleton
    public AppCMSEventArchieveRest providesAppCMSEventArchieveRestt(Retrofit retrofit) {
        return retrofit.create(AppCMSEventArchieveRest.class);
    }

    @Provides
    @Singleton
    public AppCMSSSLCommerzInitiateRest providesAppCMSSSLCommerzConfigRest(Retrofit retrofit) {
        return retrofit.create(AppCMSSSLCommerzInitiateRest.class);
    }

    @Provides
    @Singleton
    public AppCMSSSLCommerzInitiateCall providesAppCMSSSLCommerzConfigCall(AppCMSSSLCommerzInitiateRest initiateRest, Gson gson) {
        return new AppCMSSSLCommerzInitiateCall(initiateRest, gson);
    }

    @Provides
    @Singleton
    public AppCMSCCAvenueRSAKeyRest providesAppCMSCCAvenueRSAKeyRest(Retrofit retrofit) {
        return retrofit.create(AppCMSCCAvenueRSAKeyRest.class);
    }

    @Provides
    @Singleton
    public AppCMSCCAvenueRSAKeyCall providesAppCMSCCAvenueRSAKeyCall(AppCMSCCAvenueRSAKeyRest initiateRest, Gson gson) {
        return new AppCMSCCAvenueRSAKeyCall(initiateRest, gson);
    }


    @Provides
    @Singleton
    public AppCMSAudioDetailRest providesAppCMSAudioDetailRest(Retrofit retrofit) {
        return retrofit.create(AppCMSAudioDetailRest.class);
    }

    @Provides
    @Singleton
    public AppCMSBillingHistoryRest providesAppCMSBillingHistoryRest(Retrofit retrofit) {
        return retrofit.create(AppCMSBillingHistoryRest.class);
    }

    @Provides
    @Singleton
    public AppCMSHistoryRest providesAppCMSHistoryRest(Retrofit retrofit) {
        return retrofit.create(AppCMSHistoryRest.class);
    }

    @Provides
    @Singleton
    public AppCMSArticleRest providesAppCMSArticleRest(Retrofit retrofit) {
        return retrofit.create(AppCMSArticleRest.class);
    }

    @Provides
    @Singleton
    public AppCMSScheduleRest providesAppCMSScheduleRest(Retrofit retrofit) {
        return retrofit.create(AppCMSScheduleRest.class);
    }

    @Provides
    @Singleton
    public AppCMSRosterRest providesAppCMSRostRest(Retrofit retrofit) {
        return retrofit.create(AppCMSRosterRest.class);
    }

    @Provides
    @Singleton
    public AppCMSLibraryRest providesAppCMSLibraryRest(Retrofit retrofit) {
        return retrofit.create(AppCMSLibraryRest.class);
    }

    @Provides
    @Singleton
    public AppCMSEmailConsentRest providesAppCMSEmailConsentRest(Retrofit retrofit) {
        return retrofit.create(AppCMSEmailConsentRest.class);
    }

    @Provides
    @Singleton
    public AppCMSPhotoGalleryRest providesAppCMSPhotoGalleryRest(Retrofit retrofit) {

        return retrofit.create(AppCMSPhotoGalleryRest.class);
    }

    @Provides
    @Singleton
    public AppCMSBeaconRest providesAppCMSBeaconMessage(Retrofit retrofit) {
        return retrofit.create(AppCMSBeaconRest.class);
    }

    @Provides
    @Singleton
    public AppCMSSignInRest providesAppCMSSignInRest(Retrofit retrofit) {
        return retrofit.create(AppCMSSignInRest.class);
    }

    @Provides
    @Singleton
    public AppCMSRedeemRest providesAppCMSRedeemRest(Retrofit retrofit) {
        return retrofit.create(AppCMSRedeemRest.class);
    }

    @Provides
    @Singleton
    public AppCMSRefreshIdentityRest providesAppCMSRefreshIdentityRest(Retrofit retrofit) {
        return retrofit.create(AppCMSRefreshIdentityRest.class);
    }

    @Provides
    @Singleton
    public AppCMSResetPasswordRest providesAppCMSResetPasswordRest(Retrofit retrofit) {
        return retrofit.create(AppCMSResetPasswordRest.class);
    }

    @Provides
    @Singleton
    public AppCMSFacebookLoginRest providesAppCMSFacebookLoginRest(Retrofit retrofit) {
        return retrofit.create(AppCMSFacebookLoginRest.class);
    }

    @Provides
    @Singleton
    public AppCMSGoogleLoginRest providesAppCMSGoogleLoginRest(Retrofit retrofit) {
        return retrofit.create(AppCMSGoogleLoginRest.class);
    }

    @Provides
    @Singleton
    public AppCMSUserIdentityRest providesAppCMSUserIdentityRest(Retrofit retrofit) {
        return retrofit.create(AppCMSUserIdentityRest.class);
    }

    @Provides
    @Singleton
    public AppCMSUpdateWatchHistoryRest providesAppCMSUpdateWatchHistoryRest(Retrofit retrofit) {
        return retrofit.create(AppCMSUpdateWatchHistoryRest.class);
    }

    @Provides
    @Singleton
    public AppCMSUserVideoStatusRest providesAppCMSUserVideoStatusRest(Retrofit retrofit) {
        return retrofit.create(AppCMSUserVideoStatusRest.class);
    }

    @Provides
    @Singleton
    public AppCMSAddToWatchlistRest providesAppCMSAddToWatchlistRest(Retrofit retrofit) {
        return retrofit.create(AppCMSAddToWatchlistRest.class);
    }

    @Provides
    @Singleton
    public AppCMSDeleteHistoryRest providesAppCMSDeleteHistoryRest(Retrofit retrofit) {
        return retrofit.create(AppCMSDeleteHistoryRest.class);
    }

    @Provides
    @Singleton
    public AppCMSSubscriptionRest providesAppCMSSubscriptionRest(Retrofit retrofit) {
        return retrofit.create(AppCMSSubscriptionRest.class);
    }

    @Provides
    @Singleton
    public AppCMSSubscriptionPlanRest providesAppCMSSubscriptionPlanRest(Retrofit retrofit) {
        return retrofit.create(AppCMSSubscriptionPlanRest.class);
    }

    @Provides
    @Singleton
    public AppCMSAnonymousAuthTokenRest providesAppCMSAnonymousAuthTokenRest(Retrofit retrofit) {
        return retrofit.create(AppCMSAnonymousAuthTokenRest.class);
    }

    @Provides
    @Singleton
    public GoogleRefreshTokenRest providesGoogleRefreshTokenRest(Retrofit retrofit) {
        return retrofit.create(GoogleRefreshTokenRest.class);
    }

    @Provides
    @Singleton
    public GoogleCancelSubscriptionRest providesGoogleCancelSubscriptionRest(Retrofit retrofit) {
        return retrofit.create(GoogleCancelSubscriptionRest.class);
    }

    @Provides
    @Singleton
    public AppCMSIPGeoLocatorRest providesAppCMSIPGeoLocatorRest(Retrofit retrofit) {
        return retrofit.create(AppCMSIPGeoLocatorRest.class);
    }


    @Provides
    @Singleton
    public AppCMSCCAvenueRest providesAppCMSAvenueRest(Retrofit retrofit) {
        return retrofit.create(AppCMSCCAvenueRest.class);
    }

    @Provides
    @Singleton
    public AppCMSRestorePurchaseRest providesAppCMSRestorePurchaseRest(Retrofit retrofit) {
        return retrofit.create(AppCMSRestorePurchaseRest.class);
    }

    @Provides
    @Singleton
    public AppCMSAndroidModuleRest providesAppCMSAndroidModuleRest(Retrofit retrofit) {
        return retrofit.create(AppCMSAndroidModuleRest.class);
    }

    @Provides
    @Singleton
    public AppCMSResourceCall providesAppCMSResourceCall(AppCMSPageUIRest appCMSPageUIRest, Gson gson) {
        return new AppCMSResourceCall(appCMSPageUIRest, gson, storageDirectory);
    }

    @Provides
    @Singleton
    public AppCMSSignedURLRest providesAppCMSSignedURLRest(Retrofit retrofit) {
        return retrofit.create(AppCMSSignedURLRest.class);
    }

    @Provides
    @Singleton
    public AppCMSMainUICall providesAppCMSMainUICall(OkHttpClient client,
                                                     AppCMSMainUIRest appCMSMainUIRest,
                                                     Gson gson) {
        return new AppCMSMainUICall(unknownHostExceptionTimeout,
                client,
                appCMSMainUIRest,
                gson,
                storageDirectory);
    }

    @Provides
    @Singleton
    public AppCMSAndroidUICall providesAppCMSAndroidUICall(AppCMSAndroidUIRest appCMSAndroidUIRest,
                                                           Gson gson) {
        return new AppCMSAndroidUICall(appCMSAndroidUIRest, gson, storageDirectory);
    }

    @Provides
    @Singleton
    public AppCMSPageUICall providesAppCMSPageUICall(AppCMSPageUIRest appCMSPageUIRest, Gson gson) {
        return new AppCMSPageUICall(appCMSPageUIRest, gson, storageDirectory);
    }

    @Provides
    @Singleton
    public AppCMSSignInCall providesAppCMSSignInCall(AppCMSSignInRest appCMSSignInRest, Gson gson) {
        return new AppCMSSignInCall(appCMSSignInRest, gson);
    }

    @Provides
    @Singleton
    public AppCMSRedeemCall providesAppCMSRedeemCall(AppCMSRedeemRest appCMSRedeemRest, Gson gson) {
        return new AppCMSRedeemCall(appCMSRedeemRest, gson);
    }

    @Provides
    @Singleton
    public AppCMSRefreshIdentityCall providesAppCMSRefreshIdentityCall(AppCMSRefreshIdentityRest appCMSRefreshIdentityRest) {
        return new AppCMSRefreshIdentityCall(appCMSRefreshIdentityRest);
    }

    @Provides
    @Singleton
    public AppCMSWatchlistCall providesAppCMSWatchlistCall(AppCMSWatchlistRest appCMSWatchlistRest, Gson gson) {
        return new AppCMSWatchlistCall(appCMSWatchlistRest, gson);
    }

    @Provides
    @Singleton
    public AppCMSPlaylistCall providesAppCMSPlaylistCall(AppCMSPlaylistRest appCMSPlaylistRest, Gson gson) {
        return new AppCMSPlaylistCall(appCMSPlaylistRest, gson);
    }

    @Provides
    @Singleton
    public AppCMSTeamStandingCall providesAppCMSStandingCall(AppCMSTeamStandingRest appCMSStandingRest, Gson gson) {
        return new AppCMSTeamStandingCall(appCMSStandingRest, gson);
    }

    @Provides
    @Singleton
    public AppCMSTeamRoasterCall providesAppCMSTeamRoasterCall(AppCMSTeamRoasterRest appCMSStandingRest, Gson gson) {
        return new AppCMSTeamRoasterCall(appCMSStandingRest, gson);
    }

    @Provides
    @Singleton
    public AppCMSEventArchieveCall providesAppCMSEventArchieveCall(AppCMSEventArchieveRest appCMSEventArchievRest, Gson gson) {
        return new AppCMSEventArchieveCall(appCMSEventArchievRest, gson);
    }

    @Provides
    @Singleton
    public AppCMSAudioDetailCall providesAppCMSAudioDetailCall(AppCMSAudioDetailRest appCMSAudioDetailRest, Gson gson) {
        return new AppCMSAudioDetailCall(appCMSAudioDetailRest, gson);
    }

    @Provides
    @Singleton
    public AppCMSHistoryCall providesAppCMSHistoryCall(AppCMSHistoryRest appCMSHistoryRest, Gson gson) {
        return new AppCMSHistoryCall(appCMSHistoryRest, gson);
    }

    @Provides
    @Singleton
    public AppCMSBillingHistoryCall providesAppCMSBillingHistoryCall(AppCMSBillingHistoryRest appCMSBillingHistoryRest) {
        return new AppCMSBillingHistoryCall(appCMSBillingHistoryRest);
    }

    @Provides
    @Singleton
    public AppCMSArticleCall providesAppCMSArticleCall(AppCMSArticleRest appCMSArticleRest, Gson gson) {
        return new AppCMSArticleCall(appCMSArticleRest, gson);
    }

    @Provides
    @Singleton
    public AppCMSPhotoGalleryCall providesAppCMSPhotoGalleryCall(AppCMSPhotoGalleryRest appCMSPhotoGalleryRest, Gson gson) {
        return new AppCMSPhotoGalleryCall(appCMSPhotoGalleryRest, gson);
    }

    @Provides
    @Singleton
    public AppCMSResetPasswordCall providesAppCMSPasswordCall(AppCMSResetPasswordRest appCMSResetPasswordRest,
                                                              Gson gson) {
        return new AppCMSResetPasswordCall(appCMSResetPasswordRest, gson);
    }

    @Provides
    @Singleton
    public AppCMSFacebookLoginCall providesAppCMSFacebookLoginCall(AppCMSFacebookLoginRest appCMSFacebookLoginRest,
                                                                   Gson gson) {
        return new AppCMSFacebookLoginCall(appCMSFacebookLoginRest, gson);
    }

    @Provides
    @Singleton
    public AppCMSGoogleLoginCall providesAppCMSGoogleLoginCall(AppCMSGoogleLoginRest appCMSGoogleLoginRest,
                                                               Gson gson) {
        return new AppCMSGoogleLoginCall(appCMSGoogleLoginRest, gson);
    }

    @Provides
    @Singleton
    public AppCMSUserIdentityCall providesAppCMSUserIdentityCall(AppCMSUserIdentityRest appCMSUserIdentityRest) {
        return new AppCMSUserIdentityCall(appCMSUserIdentityRest);
    }

    @Provides
    @Singleton
    public AppCMSUpdateWatchHistoryCall providesAppCMSUpdateWatchHistoryCall(AppCMSUpdateWatchHistoryRest appCMSUpdateWatchHistoryRest) {
        return new AppCMSUpdateWatchHistoryCall(appCMSUpdateWatchHistoryRest);
    }

    @Provides
    @Singleton
    public AppCMSUserVideoStatusCall providesAppCMSUserVideoStatusCall(AppCMSUserVideoStatusRest appCMSUserVideoStatusRest) {
        return new AppCMSUserVideoStatusCall(appCMSUserVideoStatusRest);
    }

    @Provides
    @Singleton
    public AppCMSUserDownloadVideoStatusCall providesAppCMSUserDownloadVideoStatusCall() {
        return new AppCMSUserDownloadVideoStatusCall();
    }


    @Provides
    @Singleton
    public AppCMSSubscriptionPlanCall appCMSSubscriptionPlanCall(AppCMSSubscriptionPlanRest appCMSSubscriptionPlanRest,
                                                                 Gson gson) {
        return new AppCMSSubscriptionPlanCall(appCMSSubscriptionPlanRest, gson);
    }

    @Provides
    @Singleton
    public AppCMSAnonymousAuthTokenCall appCMSAnonymousAuthTokenCall(AppCMSAnonymousAuthTokenRest appCMSAnonymousAuthTokenRest,
                                                                     Gson gson) {
        return new AppCMSAnonymousAuthTokenCall(appCMSAnonymousAuthTokenRest, gson);
    }

    @Provides
    @Singleton
    public GoogleRefreshTokenCall providesGoogleRefreshTokenCall(GoogleRefreshTokenRest googleRefreshTokenRest) {
        return new GoogleRefreshTokenCall(googleRefreshTokenRest);
    }

    @Provides
    @Singleton
    public GoogleCancelSubscriptionCall providesGoogleCancelSubscriptionCall(GoogleCancelSubscriptionRest googleCancelSubscriptionRest) {
        return new GoogleCancelSubscriptionCall(googleCancelSubscriptionRest);
    }

    @Provides
    @Singleton
    public AppCMSIPGeoLocatorCall providesAppCMSIPGeoLocatorCall(AppCMSIPGeoLocatorRest appCMSIPGeoLocatorRest) {
        return new AppCMSIPGeoLocatorCall(appCMSIPGeoLocatorRest);
    }


    @Provides
    @Singleton
    public AppCMSWeatherFeedCall providesAppCMSWeatherFeedCall(AppCMSWeatherFeedRest appCMSWeatherFeedRest) {
        return new AppCMSWeatherFeedCall(appCMSWeatherFeedRest);
    }

    @Provides
    @Singleton
    public GetUserRecommendGenreCall providesGetUserRecommendGenreCall(GetUserRecommendGenreRest getUserRecommendGenreRest, Gson gson) {
        return new GetUserRecommendGenreCall(getUserRecommendGenreRest, gson);
    }

    @Provides
    @Singleton
    public OfflineVideoStatusCall providesAppCMSOfflineVideoStatusCall() {
        return new OfflineVideoStatusCall();
    }

    @Provides
    @Singleton
    public GetUserRecommendGenreRest providesGetUserRecommendGenreRest(Retrofit retrofit) {
        return retrofit.create(GetUserRecommendGenreRest.class);
    }



    @Provides
    @Singleton
    public AppCMSCCAvenueCall providesAppCMSCCAvenueCall(AppCMSCCAvenueRest appCMSCCAvenueRest) {
        return new AppCMSCCAvenueCall(appCMSCCAvenueRest);
    }

    @Provides
    @Singleton
    public AppCMSRestorePurchaseCall providesAppCMSRestorePurchaseCall(Gson gson,
                                                                       AppCMSRestorePurchaseRest appCMSRestorePurchaseRest) {
        return new AppCMSRestorePurchaseCall(gson, appCMSRestorePurchaseRest);
    }

    @Provides
    @Singleton
    public AppCMSAndroidModuleCall providesAppCMSAndroidModuleCall(AssetManager assetManager,
                                                                   Gson gson,
                                                                   AppCMSAndroidModuleRest appCMSAndroidModuleRest) {
        return new AppCMSAndroidModuleCall(assetManager,
                gson,
                appCMSAndroidModuleRest,
                storageDirectory);
    }

    @Provides
    @Singleton
    public AppCMSSignedURLCall providesAppCMSSignedURLCall(AppCMSSignedURLRest appCMSSignedURLRest) {
        return new AppCMSSignedURLCall(appCMSSignedURLRest);
    }

    @Provides
    @Singleton
    public AppCMSEmailConsentCall providesAppCMSEmailConsentCall(AppCMSEmailConsentRest appCMSEmailConsentRest) {
        return new AppCMSEmailConsentCall(appCMSEmailConsentRest);
    }

    @Provides
    @Singleton
    public Map<String, AppCMSUIKeyType> providesJsonValueKeyMap() {
        return jsonValueKeyMap;
    }

    @Provides
    @Singleton
    public Map<String, String> providesPageNameToActionMap() {
        return pageNameToActionMap;
    }

    @Provides
    @Singleton
    public Map<String, AppCMSPageUI> providesActionToPageMap() {
        return actionToPageMap;
    }

    @Provides
    @Singleton
    public Map<String, AppCMSPageAPI> providesActionToToPageAPIMap() {
        return actionToPageAPIMap;
    }

    @Provides
    @Singleton
    public Map<String, AppCMSActionType> providesActionToActionTypeMap() {
        return actionToActionTypeMap;
    }

    @Provides
    @Singleton
    public AppCMSSubscribeForLatestNewsRest appCMSSubscribeForLatestNewsRest(Retrofit retrofit) {
        return retrofit.create(AppCMSSubscribeForLatestNewsRest.class);
    }

    @Provides
    @Singleton
    public AppCMSSubscribeForLatestNewsCall appCMSSubscribeForLatestNewsCall(AppCMSSubscribeForLatestNewsRest appCMSSubscribeForLatestNewsRest, Gson gson) {
        return new AppCMSSubscribeForLatestNewsCall(appCMSSubscribeForLatestNewsRest, gson);
    }

    @Provides
    @Singleton
    public AppCMSJuspayRest providesAppCMSJuspayRest(Retrofit retrofit) {
        return retrofit.create(AppCMSJuspayRest.class);
    }

    @Provides
    @Singleton
    public VerimatrixCall providesVerimatrixCall(VerimatrixRest verimatrixRest) {
        return new VerimatrixCall(verimatrixRest);
    }

    @Provides
    @Singleton
    public VerimatrixRest providesVerimatrixRest(Retrofit retrofit) {
        return retrofit.create(VerimatrixRest.class);
    }

    @Provides
    @Singleton
    public AppCMSLocationRest providesAppCMSLocationRest(Retrofit retrofit) {
        return retrofit.create(AppCMSLocationRest.class);
    }

    @Provides
    @Singleton
    public AppCMSParentalRatingMapRest providesAppCMSParentalRatingMapRest(Retrofit retrofit) {
        return retrofit.create(AppCMSParentalRatingMapRest.class);
    }


    @Provides
    @Singleton
    public AppCMSWeatherFeedRest providesAppCMSWeatherFeedRest(OkHttpClient client) {

        Retrofit retrofit1 = new Retrofit.Builder()
                .addConverterFactory(
                        SimpleXmlConverterFactory.createNonStrict(new Persister(new AnnotationStrategy())))
                .baseUrl(baseUrl)
                .client(client)
                .build();

        return retrofit1.create(AppCMSWeatherFeedRest.class);
    }

    @Provides
    public AppCMSCarrierBillingCall providesAppCMSCarrierBillingCall(Retrofit retrofit, Gson gson) {
        return new AppCMSCarrierBillingCall(retrofit, gson);
    }
}