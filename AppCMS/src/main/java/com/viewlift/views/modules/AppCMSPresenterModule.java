package com.viewlift.views.modules;

import com.google.gson.Gson;
import com.viewlift.analytics.CleverTapSDK;
import com.viewlift.analytics.FacebookAnalytics;
import com.viewlift.db.AppPreference;
import com.viewlift.models.data.appcms.api.AppCMSPageAPI;
import com.viewlift.models.data.appcms.ui.AppCMSUIKeyType;
import com.viewlift.models.data.appcms.ui.main.LocalisedStrings;
import com.viewlift.models.data.appcms.ui.page.AppCMSPageUI;
import com.viewlift.models.network.modules.AppCMSSearchModule;
import com.viewlift.models.network.rest.AppCMSAddToWatchlistCall;
import com.viewlift.models.network.rest.AppCMSAndroidModuleCall;
import com.viewlift.models.network.rest.AppCMSAndroidUICall;
import com.viewlift.models.network.rest.AppCMSAnonymousAuthTokenCall;
import com.viewlift.models.network.rest.AppCMSArticleCall;
import com.viewlift.models.network.rest.AppCMSAudioDetailCall;
import com.viewlift.models.network.rest.AppCMSBeaconCall;
import com.viewlift.models.network.rest.AppCMSBeaconRest;
import com.viewlift.models.network.rest.AppCMSBillingHistoryCall;
import com.viewlift.models.network.rest.AppCMSCCAvenueCall;
import com.viewlift.models.network.rest.AppCMSCCAvenueRSAKeyCall;
import com.viewlift.models.network.rest.AppCMSDeleteHistoryCall;
import com.viewlift.models.network.rest.AppCMSEmailConsentCall;
import com.viewlift.models.network.rest.AppCMSEventArchieveCall;
import com.viewlift.models.network.rest.AppCMSFacebookLoginCall;
import com.viewlift.models.network.rest.AppCMSGoogleLoginCall;
import com.viewlift.models.network.rest.AppCMSHistoryCall;
import com.viewlift.models.network.rest.AppCMSIPGeoLocatorCall;
import com.viewlift.models.network.rest.AppCMSJuspayCall;
import com.viewlift.models.network.rest.AppCMSLibraryCall;
import com.viewlift.models.network.rest.AppCMSLocationCall;
import com.viewlift.models.network.rest.AppCMSMainUICall;
import com.viewlift.models.network.rest.AppCMSPageUICall;
import com.viewlift.models.network.rest.AppCMSParentalRatingMapCall;
import com.viewlift.models.network.rest.AppCMSPhotoGalleryCall;
import com.viewlift.models.network.rest.AppCMSPlaylistCall;
import com.viewlift.models.network.rest.AppCMSRedeemCall;
import com.viewlift.models.network.rest.AppCMSRefreshIdentityCall;
import com.viewlift.models.network.rest.AppCMSResetPasswordCall;
import com.viewlift.models.network.rest.AppCMSResourceCall;
import com.viewlift.models.network.rest.AppCMSRestorePurchaseCall;
import com.viewlift.models.network.rest.AppCMSRosterCall;
import com.viewlift.models.network.rest.AppCMSSSLCommerzInitiateCall;
import com.viewlift.models.network.rest.AppCMSScheduleCall;
import com.viewlift.models.network.rest.AppCMSSearchCall;
import com.viewlift.models.network.rest.AppCMSSignInCall;
import com.viewlift.models.network.rest.AppCMSSignedURLCall;
import com.viewlift.models.network.rest.AppCMSSiteCall;
import com.viewlift.models.network.rest.AppCMSSubscribeForLatestNewsCall;
import com.viewlift.models.network.rest.AppCMSSubscriptionCall;
import com.viewlift.models.network.rest.AppCMSSubscriptionPlanCall;
import com.viewlift.models.network.rest.AppCMSTeamRoasterCall;
import com.viewlift.models.network.rest.AppCMSTeamStandingCall;
import com.viewlift.models.network.rest.AppCMSUpdateWatchHistoryCall;
import com.viewlift.models.network.rest.AppCMSUserDownloadVideoStatusCall;
import com.viewlift.models.network.rest.AppCMSUserIdentityCall;
import com.viewlift.models.network.rest.AppCMSUserVideoStatusCall;
import com.viewlift.models.network.rest.AppCMSWatchlistCall;
import com.viewlift.models.network.rest.AppCMSWeatherFeedCall;
import com.viewlift.models.network.rest.GetUserRecommendGenreCall;
import com.viewlift.models.network.rest.GoogleCancelSubscriptionCall;
import com.viewlift.models.network.rest.GoogleRefreshTokenCall;
import com.viewlift.calendar.AppCalendarEvent;
import com.viewlift.offlinedrm.OfflineVideoStatusCall;
import com.viewlift.models.network.rest.VerimatrixCall;
import com.viewlift.presenters.AppCMSActionType;
import com.viewlift.presenters.AppCMSPresenter;

import java.lang.ref.ReferenceQueue;
import java.util.Map;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;

/**
 * Created by viewlift on 5/22/17.
 */

@Module(includes = {AppCMSSearchModule.class})
public class AppCMSPresenterModule {
    @Provides
    @Singleton
    public ReferenceQueue<Object> providesReferenceQueue() {
        return new ReferenceQueue<>();
    }


    @Provides
    @Singleton
    public AppCMSPresenter providesAppCMSPresenter(Gson gson, Retrofit retrofit,
                                                   AppCMSArticleCall appCMSArticleCall,
                                                   AppCMSPhotoGalleryCall appCMSPhotoGalleryCall,
                                                   AppCMSScheduleCall appCMSScheduleCall,
                                                   AppCMSRosterCall appCMSRosterCall,
                                                   AppCMSLibraryCall appCMSLibraryCall,

                                                   AppCMSPlaylistCall appCMSPlaylistCall,
                                                   AppCMSTeamStandingCall appCMSTeamStandingCall,
                                                   AppCMSTeamRoasterCall appCMSTeamRoasterCall,
                                                   AppCMSEventArchieveCall appCMSEventArchieveCall,

                                                   AppCMSSSLCommerzInitiateCall appCMSSSLCommerzInitiateCall,
                                                   AppCMSCCAvenueRSAKeyCall appCMSCCAvenueRSAKeyCall,
                                                   AppCMSAudioDetailCall appCMSAudioDetailCall,
                                                   AppCMSMainUICall appCMSMainUICall,
                                                   AppCMSAndroidUICall appCMSAndroidUICall,
                                                   AppCMSPageUICall appCMSPageUICall,
                                                   AppCMSResourceCall appCMSResourceCall,

                                                   AppCMSSiteCall appCMSSiteCall,
                                                   AppCMSIPGeoLocatorCall appCMSIPGeoLocatorCall,
                                                   AppCMSWeatherFeedCall appCMSWeatherFeedCall,
                                                   AppCMSSearchCall appCMSSearchCall,

                                                   AppCMSWatchlistCall appCMSWatchlistCall,
                                                   AppCMSBillingHistoryCall appCMSBillingHistoryCall,
                                                   AppCMSHistoryCall appCMSHistoryCall,

                                                   AppCMSDeleteHistoryCall appCMSDeleteHistoryCall,

                                                   AppCMSSubscriptionCall appCMSSubscriptionCall,
                                                   AppCMSSubscriptionPlanCall appCMSSubscriptionPlanCall,
                                                   AppCMSAnonymousAuthTokenCall appCMSAnonymousAuthTokenCall,

                                                   AppCMSBeaconRest appCMSBeaconRest,
                                                   AppCMSSignInCall appCMSSignInCall,
                                                   AppCMSRedeemCall appCMSRedeemCall,
                                                   AppCMSRefreshIdentityCall appCMSRefreshIdentityCall,
                                                   AppCMSResetPasswordCall appCMSResetPasswordCall,

                                                   AppCMSFacebookLoginCall appCMSFacebookLoginCall,
                                                   AppCMSGoogleLoginCall appCMSGoogleLoginCall,

                                                   AppCMSUserIdentityCall appCMSUserIdentityCall,
                                                   GoogleRefreshTokenCall googleRefreshTokenCall,
                                                   GoogleCancelSubscriptionCall googleCancelSubscriptionCall,
                                                   AppCMSAddToWatchlistCall appCMSAddToWatchlistCall,

                                                   AppCMSCCAvenueCall appCMSCCAvenueCall,

                                                   AppCMSUpdateWatchHistoryCall appCMSUpdateWatchHistoryCall,
                                                   AppCMSUserVideoStatusCall appCMSUserVideoStatusCall,
                                                   AppCMSUserDownloadVideoStatusCall appCMSUserDownloadVideoStatusCall,
                                                   AppCMSBeaconCall appCMSBeaconCall,

                                                   AppCMSRestorePurchaseCall appCMSRestorePurchaseCall,

                                                   AppCMSAndroidModuleCall appCMSAndroidModuleCall,

                                                   AppCMSSignedURLCall appCMSSignedURLCall,

                                                   Map<String, AppCMSUIKeyType> jsonValueKeyMap,
                                                   Map<String, String> pageNameToActionMap,
                                                   Map<String, AppCMSPageUI> actionToPageMap,
                                                   Map<String, AppCMSPageAPI> actionToPageAPIMap,
                                                   Map<String, AppCMSActionType> actionToActionTypeMap,

                                                   ReferenceQueue<Object> referenceQueue,
                                                   AppCMSSubscribeForLatestNewsCall appCMSSubscribeForLatestNewsCall,
                                                   CleverTapSDK cleverTapSDK,
                                                   AppCMSEmailConsentCall appCMSEmailConsentCall,
                                                   AppPreference appPreference,
                                                   FacebookAnalytics facebookAnalytics,
                                                   AppCMSJuspayCall appCMSJuspayCall,
                                                   GetUserRecommendGenreCall getUserRecommendGenreCall,
                                                   LocalisedStrings localisedStrings, AppCalendarEvent appCalendarEvent, AppCMSLocationCall appCMSLocationCall,
                                                   AppCMSParentalRatingMapCall ratingMapCall,
                                                   VerimatrixCall verimatrixCall,  OfflineVideoStatusCall offlineVideoStatusCall) {
        return new AppCMSPresenter(gson,retrofit,
                appCMSArticleCall,
                appCMSPhotoGalleryCall,
                appCMSScheduleCall,appCMSRosterCall,appCMSLibraryCall,
                appCMSPlaylistCall,appCMSTeamStandingCall,appCMSTeamRoasterCall,appCMSEventArchieveCall,
                appCMSSSLCommerzInitiateCall,
                appCMSCCAvenueRSAKeyCall,
                appCMSAudioDetailCall,
                appCMSMainUICall,
                appCMSAndroidUICall,
                appCMSPageUICall,
                appCMSResourceCall,
                appCMSSiteCall,
                appCMSIPGeoLocatorCall,
                appCMSWeatherFeedCall,
                appCMSSearchCall,

                appCMSWatchlistCall,
                appCMSBillingHistoryCall,
                appCMSHistoryCall,

                appCMSDeleteHistoryCall,

                appCMSSubscriptionCall,
                appCMSSubscriptionPlanCall,
                appCMSAnonymousAuthTokenCall,

                appCMSBeaconRest,
                appCMSSignInCall,
                appCMSRedeemCall,
                appCMSRefreshIdentityCall,
                appCMSResetPasswordCall,

                appCMSFacebookLoginCall,
                appCMSGoogleLoginCall,

                appCMSUserIdentityCall,
                googleRefreshTokenCall,
                googleCancelSubscriptionCall,

                appCMSUpdateWatchHistoryCall,
                appCMSUserVideoStatusCall,
                appCMSUserDownloadVideoStatusCall,
                appCMSBeaconCall,
                appCMSRestorePurchaseCall,

                appCMSAndroidModuleCall,

                appCMSSignedURLCall,

                appCMSAddToWatchlistCall,

                appCMSCCAvenueCall,

                jsonValueKeyMap,
                pageNameToActionMap,
                actionToPageMap,
                actionToPageAPIMap,
                actionToActionTypeMap,

                referenceQueue,
                appCMSSubscribeForLatestNewsCall,
                cleverTapSDK,
                appCMSEmailConsentCall,
                appPreference,
                facebookAnalytics,
                appCMSJuspayCall,
                getUserRecommendGenreCall,
                localisedStrings, appCalendarEvent, appCMSLocationCall, ratingMapCall,verimatrixCall, offlineVideoStatusCall);
    }


}
