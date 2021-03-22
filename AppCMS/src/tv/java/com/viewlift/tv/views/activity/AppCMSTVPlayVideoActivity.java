package com.viewlift.tv.views.activity;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.amazon.device.iap.PurchasingService;
import com.viewlift.AppCMSApplication;
import com.viewlift.R;
import com.viewlift.analytics.AppsFlyerUtils;
import com.viewlift.models.data.appcms.api.AppCMSSignedURLResult;
import com.viewlift.models.data.appcms.api.ClosedCaptions;
import com.viewlift.models.data.appcms.api.ContentDatum;
import com.viewlift.models.data.appcms.api.Gist;
import com.viewlift.models.data.appcms.api.Module;
import com.viewlift.models.data.appcms.api.Mpeg;
import com.viewlift.models.data.appcms.api.VideoAssets;
import com.viewlift.models.data.appcms.ui.android.MetaPage;
import com.viewlift.models.data.appcms.ui.android.NavigationUser;
import com.viewlift.presenters.AppCMSPresenter;
import com.viewlift.tv.utility.Utils;
import com.viewlift.tv.views.dialog.AppCMSTVVerifyVideoPinDialog;
import com.viewlift.tv.views.fragment.AppCMSPlayVideoFragment;
import com.viewlift.tv.views.fragment.AppCMSTVAutoplayFragment;
import com.viewlift.tv.views.fragment.AppCmsGenericDialogFragment;
import com.viewlift.tv.views.fragment.AppCmsLinkYourAccountFragment;
import com.viewlift.tv.views.fragment.AppCmsLoginDialogFragment;
import com.viewlift.tv.views.fragment.AppCmsResetPasswordFragment;
import com.viewlift.tv.views.fragment.AppCmsSignUpDialogFragment;
import com.viewlift.tv.views.fragment.AppCmsTvErrorFragment;
import com.viewlift.tv.views.fragment.ClearDialogFragment;
import com.viewlift.tv.views.fragment.ParentalGateViewFragment;
import com.viewlift.tv.views.fragment.PreviewDialogFragment;
import com.viewlift.utils.CommonUtils;
import com.viewlift.utils.ContentTypeChecker;
import com.viewlift.utils.LanguageHelper;
import com.viewlift.views.adapters.PlayerEpisodeAdapter;
import com.viewlift.views.adapters.PlayerSeasonAdapter;
import com.viewlift.views.binders.AppCMSBinder;
import com.viewlift.views.binders.AppCMSVideoPageBinder;
import com.viewlift.views.customviews.VideoPlayerView;
import com.viewlift.views.listener.VideoSelected;
import com.viewlift.views.rxbus.SeasonTabSelectorBus;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TreeMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.functions.Action0;
import rx.functions.Action1;

import static com.viewlift.presenters.AppCMSPresenter.DIALOG_FRAGMENT_TAG;
import static com.viewlift.presenters.AppCMSPresenter.LOGIN_SUCCESSFUL_ON_AUTOPLAY;
import static com.viewlift.presenters.AppCMSPresenter.PRESENTER_OPEN_AUTOPLAY_SCREEN;

public class AppCMSTVPlayVideoActivity extends AppCmsBaseActivity implements
        AppCMSPlayVideoFragment.OnClosePlayerEvent, AppCmsTvErrorFragment.ErrorFragmentListener,
        VideoPlayerView.StreamingQualitySelector, VideoPlayerView.ClosedCaptionSelector,
        VideoPlayerView.SeasonEpisodeSelctionEvent, VideoSelected,
        ParentalGateViewFragment.ParentalGateViewInteractionListener,
        AppCMSTVAutoplayFragment.OnPageCreation,
        AppCMSTVAutoplayFragment.FragmentInteractionListener {
    private static final String TAG = "TVPlayVideoActivity";
    int ffAndRewindDelta = 10000;

    private BroadcastReceiver handoffReceiver;
    private BroadcastReceiver closeActivityOnDeepLinkActionReceiver;
    private AppCMSPresenter appCMSPresenter;
    FrameLayout appCMSPlayVideoPageContainer;

    private AppCMSVideoPageBinder binder;

    private AppCMSPlayVideoFragment appCMSPlayVideoFragment;
    private String title;
    private String hlsUrl;
    private String videoImageUrl;
    private String filmId;
    private String primaryCategory;
    private List<String> relateVideoIds;
    private int currentlyPlayingIndex = 0;
    private AppCmsResetPasswordFragment appCmsResetPasswordFragment;
    private Map<String, String> availableStreamingFormats;
    private Map<String, String> availableStreamingQualityMap;
    private boolean isLiveStream;
    private AppCmsLinkYourAccountFragment appCmsLinkYourAccountFragment;
    String licenseUrl, licenseToken;
    private Module moduleApi;
    PopupWindow seasonEpisodePopUpWindow;
    View seasonEpisodeView;
    @BindView(R.id.seasonTitle)
    TextView seasonTitle;
    @BindView(R.id.episodeTitle)
    TextView episodeTitle;
    @BindView(R.id.seasonRecylerView)
    RecyclerView seasonRecylerView;
    @BindView(R.id.episodeRecylerView)
    RecyclerView episodeRecylerView;
    private Intent mIntent;
    ContentTypeChecker contentTypeChecker;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tv_video_player_page);
        appCMSPresenter =
                ((AppCMSApplication) getApplication()).getAppCMSPresenterComponent().appCMSPresenter();
        if (contentTypeChecker == null)
            contentTypeChecker = new ContentTypeChecker(this);
        if (appCMSPresenter != null && appCMSPresenter.getAppCMSMain() != null
                && appCMSPresenter.getAppCMSMain().getFeatures() != null) {
            com.viewlift.Utils.setHls(appCMSPresenter.getAppCMSMain().getFeatures().isHls());
        }

        appCMSPlayVideoPageContainer =
                (FrameLayout) findViewById(R.id.app_cms_play_video_page_container);
        Intent intent = getIntent();
        Bundle bundleExtra = intent.getBundleExtra(getString(R.string.app_cms_video_player_bundle_binder_key));

        try {
            binder = (AppCMSVideoPageBinder)
                    bundleExtra.getBinder(getString(R.string.app_cms_video_player_binder_key));
            if (binder != null
                    && binder.getContentData() != null
                    && binder.getContentData().getGist() != null) {
                appCMSPresenter.sendPageViewEvent(binder.getPageName(), binder.getScreenName(), null);
                moduleApi = binder.getContentData().getModuleApi();
                if (binder.isTrailer()) {
                    String id = null;
                    if (binder.getContentData() != null &&
                            binder.getContentData().getContentDetails() != null &&
                            binder.getContentData().getContentDetails().getTrailers() != null &&
                            !binder.getContentData().getContentDetails().getTrailers().isEmpty() &&
                            binder.getContentData().getContentDetails().getTrailers().get(0) != null) {
                        id = binder.getContentData().getContentDetails().getTrailers().get(0).getId();
                    } else if (binder.getContentData() != null &&
                            binder.getContentData().getShowDetails() != null &&
                            binder.getContentData().getShowDetails().getTrailers() != null &&
                            !binder.getContentData().getShowDetails().getTrailers().isEmpty() &&
                            binder.getContentData().getShowDetails().getTrailers().get(0) != null &&
                            binder.getContentData().getShowDetails().getTrailers().get(0).getId() != null) {
                        id = binder.getContentData().getShowDetails().getTrailers().get(0).getId();
                    }
                    if (id != null) {
                        binder.getContentData().setTrailer(true);
                        appCMSPresenter.refreshVideoData(id,
                                updatedContentDatum -> {
                                    if (!AppCMSTVPlayVideoActivity.this.isFinishing() && updatedContentDatum != null) {
                                        try {
                                            updatedContentDatum.setModuleApi(moduleApi);
                                            binder.setContentData(updatedContentDatum);
                                        } catch (Exception e) {
                                            //
                                        }
                                        launchVideoPlayer(updatedContentDatum.getGist(), updatedContentDatum.getAppCMSSignedURLResult());
                                    }else{
                                        finish();
                                    }
                                }, null, false, true ,binder.getContentData());
                    }
                }
                /*Check if streaming info is null, if so call entitlement API as a fallback*/
                else if (binder.getContentData().getStreamingInfo() == null) {
                    appCMSPresenter.showLoader();
                    appCMSPresenter.refreshVideoData(binder.getContentData().getGist().getId(),
                            updatedContentDatum -> {
                                appCMSPresenter.stopLoader();
                                if (!AppCMSTVPlayVideoActivity.this.isFinishing()) {
                                    try {
                                        binder.setContentData(updatedContentDatum);
                                    } catch (Exception e) {
                                        //
                                    }
                                    launchVideoPlayer(updatedContentDatum.getGist(), updatedContentDatum.getAppCMSSignedURLResult());
                                }
                            }, null, false, false,binder.getContentData());
                } else {
                    launchVideoPlayer(binder.getContentData().getGist(), binder.getContentData().getAppCMSSignedURLResult());
                }
            }
        } catch (ClassCastException e) {
            e.printStackTrace();
        }

        closeActivityOnDeepLinkActionReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (getString(R.string.deeplink_close_player_activity_action).equalsIgnoreCase(intent.getAction())) {
                    finish();
                } else if (intent.getAction().equals(AppCMSPresenter.ACTION_LINK_YOUR_ACCOUNT)) {
                    openLinkYourAccountScreen(intent);
                }
            }
        };

        handoffReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String sendingPage = intent.getStringExtra(getString(R.string.app_cms_closing_page_name));

                if (intent.getAction().equals(AppCMSPresenter.PRESENTER_NAVIGATE_ACTION)) {
                    Bundle args = intent.getBundleExtra(getString(R.string.app_cms_bundle_key));
                    AppCMSBinder binder = (AppCMSBinder) args.getBinder(getString(R.string.app_cms_binder_key));
                    if ((binder.getExtraScreenType() ==
                            AppCMSPresenter.ExtraScreenType.EDIT_PROFILE)) {
                        if (binder.isShowParentalGateView()) {
                            mIntent = intent;
                            openParentalGateViewFragment();
                        } else {
                            openLoginOrSignUpDialog(intent, args, binder.getExtraScreenType());
                        }
                    } else if ((binder.getExtraScreenType() ==
                            AppCMSPresenter.ExtraScreenType.TERM_OF_SERVICE)) {
                        openGenericDialog(intent, false);
                    } else {
                        if (binder.isShowParentalGateView()) {
                            openParentalGateViewFragment();
                            mIntent = intent;
                        } else {
                            openLoginOrSignUpDialog(intent, args, binder.getExtraScreenType());
                        }
                    }

                } else if (intent.getAction().equals(AppCMSPresenter.CLOSE_DIALOG_ACTION)) {
                    closeSignInDialog();
                    closeSignUpDialog();
                    Utils.pageLoading(false, AppCMSTVPlayVideoActivity.this);
                } else if (intent.getAction().equals(AppCMSPresenter.UPDATE_SUBSCRIPTION)) {
                    if (appCMSPresenter.isAmazonPurchaseInitiated()) {
                        if (!appCMSPresenter.isUserSubscribed()) {
                            PurchasingService.purchase(skuToPurchase);
                            appCMSPresenter.sendGaIAPEvent(com.viewlift.Utils.getIapEventBundle(appCMSPresenter.IAP_AMAZON_INITIATED, appCMSPresenter.AMAZON_IAP, appCMSPresenter.getLoggedInUser(), appCMSPresenter.getAmazonUserId(), skuToPurchase, "", ""));
                            if (!TextUtils.isEmpty(appCMSPresenter.getAppPreference().getAppsFlyerKey())) {
                                sendAppsFlyerSubscriptionInitiatedEvent();
                            }
                        } else {
                            // Toast.makeText(context,"User is Already Subscribed.", Toast.LENGTH_SHORT).show();
                            if (!appCMSPresenter.isSignupFlag())
                                appCMSPresenter.navigateToHomePage(true);
                        }
                    }
                } else if (intent.getAction().equals(AppCMSPresenter.RECOMMENDATION_DIALOG_FRAGMENT_TAG)) {
                    showRecommendationDialog(intent);
                    appCMSPresenter.setIsFromSetting(true);
                } else if (intent.getAction().equals(AppCMSPresenter.ACTION_RESET_PASSWORD)) {
                    openResetPasswordScreen(intent);
                } else if (intent.getAction().equals(AppCMSPresenter.CLOSE_VIEW_PLANS_PAGE_ON_VIDEO_PLAYER_ACTION)) {
                    if (getSupportFragmentManager().findFragmentByTag("VIEW PLANS") instanceof AppCmsGenericDialogFragment) {
                        ((AppCmsGenericDialogFragment) getSupportFragmentManager().findFragmentByTag("VIEW PLANS")).dismiss();
                    }
                } else if (intent.getAction().equals(AppCMSPresenter.SHOW_SUBSCRIPTION_MESSAGE_ON_VIDEO_PLAYER_ACTION)) {
                    appCMSPresenter.getSubscriptionData(appCMSUserSubscriptionPlanResult -> {
                        if (appCMSUserSubscriptionPlanResult != null) {
                            String subscriptionStatus = appCMSUserSubscriptionPlanResult.getSubscriptionInfo().getSubscriptionStatus();
                            if (subscriptionStatus.equalsIgnoreCase("COMPLETED")
                                    || subscriptionStatus.equalsIgnoreCase("DEFERRED_CANCELLATION")) {
                                if (getSupportFragmentManager().findFragmentByTag("VIEW PLANS") instanceof AppCmsGenericDialogFragment) {
                                    ((AppCmsGenericDialogFragment) getSupportFragmentManager().findFragmentByTag("VIEW PLANS")).dismiss();
                                }
                                appCMSPlayVideoFragment.resumeVideo();
                            } else {
                                if (contentDatum != null && contentDatum.getSubscriptionPlans() != null) {
                                    //appCMSPlayVideoFragment.showPreviewDialog();
                                    appCMSPresenter.openEntitlementScreen(contentDatum,false);
                                } else {
                                    if (appCMSPresenter.isFireTVSubscriptionEnabled()) {
                                        openSubscriptionDialog();
                                    } else {
                                        openEntitlementDialog();
                                    }                                }
                            }
                        } else {
                            if (contentDatum != null && contentDatum.getSubscriptionPlans() != null) {
                                //appCMSPlayVideoFragment.showPreviewDialog();
                                appCMSPresenter.openEntitlementScreen(contentDatum,false);
                            } else {
                                if (getSupportFragmentManager().findFragmentByTag("VIEW PLANS") instanceof AppCmsGenericDialogFragment) {
                                    boolean viewPlansPageVisible = getSupportFragmentManager().findFragmentByTag("VIEW PLANS").isVisible();
                                    if (!viewPlansPageVisible) openSubscriptionDialog();
                                } else {
                                    if (appCMSPresenter.isFireTVSubscriptionEnabled()) {
                                        openSubscriptionDialog();
                                    } else {
                                        openEntitlementDialog();
                                    }
                                }
                            }
                        }
                    }, false);
                } else if (intent.getAction().equals(AppCMSPresenter.ERROR_DIALOG_ACTION)) {
                    if (appCMSPlayVideoFragment != null && appCMSPlayVideoFragment.getVideoPlayerView() != null) {
                        appCMSPlayVideoFragment.getVideoPlayerView().pausePlayer();
                    }
                    openErrorDialog(intent);
                }else if (intent.getAction().equals(AppCMSPresenter.GENERIC_DIALOG)) {
                    openGenericDialog(intent);
                }else if (intent.getAction().equals(AppCMSPresenter.MAX_SIMULTANEOUS_STREAM_DIALOG)) {
                    openMaxSimultaneousStreamErrorDialog(intent);
                } else if (intent.getAction().equals(AppCMSPresenter.PRESENTER_PAGE_LOADING_ACTION)) {
                    Utils.pageLoading(true, AppCMSTVPlayVideoActivity.this);
                } else if (intent.getAction().equals(AppCMSPresenter.PRESENTER_STOP_PAGE_LOADING_ACTION)) {
                    Utils.pageLoading(false, AppCMSTVPlayVideoActivity.this);
                } else if (intent.getAction().equals(AppCMSPresenter.RESTORE_PURCHASE_DIALOG)) {
                    String email = intent.getStringExtra(getString(R.string.app_cms_email_id_label));
                    openRestorePurchaseDialog(email);
                } else if (intent.getAction().equals(AppCMSPresenter.ENTITLEMENT_LOGIN_DIALOG)) {
                    openEntitlementDialog();
                }/* else if (intent.getBooleanExtra(getString(R.string.close_self_key), true) &&
                        (sendingPage == null || getString(R.string.app_cms_video_page_tag).equals(sendingPage))) {

                }*/ else if (intent.getAction().equals(getString(R.string.intent_msg_action))) {
//                    Toast.makeText(AppCMSTVPlayVideoActivity.this, intent.getStringExtra(getString(R.string.json_data_msg_key)), Toast.LENGTH_SHORT).show();
                    if (intent.getStringExtra(getString(R.string.json_data_msg_key)).equalsIgnoreCase(getString(R.string.adm_directive_search_and_play))) {
                        ContentDatum contentDatum = new ContentDatum();
                        Gist gist = new Gist();
                        gist.setId(intent.getStringExtra(getString(R.string.json_content_id_key)));
                        contentDatum.setGist(gist);
                        appCMSPresenter.launchTVVideoPlayer(
                                contentDatum,
                                0,
                                null,
                                0,
                                () -> closePlayer()
                        );
                    } else if (intent.getStringExtra(getString(R.string.json_data_msg_key)).equalsIgnoreCase(getString(R.string.adm_directive_search_and_display_results))) {
                        AppCMSTVPlayVideoActivity.this.setResult(AppCmsHomeActivity.OPEN_SEARCH_RESULT_CODE, intent);
                        closePlayer();
                    } else if (intent.getStringExtra(getString(R.string.json_data_msg_key)).equalsIgnoreCase(getString(R.string.adm_directive_play))) {
                        if (appCMSPlayVideoFragment != null) {
                            appCMSPlayVideoFragment.resumeVideo();
                        }
                    } else if (intent.getStringExtra(getString(R.string.json_data_msg_key)).equalsIgnoreCase(getString(R.string.adm_directive_pause))) {
                        if (appCMSPlayVideoFragment != null) {
                            appCMSPlayVideoFragment.pauseVideo();
                        }
                    } else if (intent.getStringExtra(getString(R.string.json_data_msg_key)).equalsIgnoreCase(getString(R.string.adm_directive_fast_forward))) {
                        if (appCMSPlayVideoFragment != null) {
                            long currentPosition = appCMSPlayVideoFragment.getVideoPlayerView().getCurrentPosition();
                            Log.d(TAG, "ADM Message. Current Time: " + currentPosition + ", seekTo: " + (currentPosition + ffAndRewindDelta));
                            appCMSPlayVideoFragment.getVideoPlayerView().getPlayerView().getController().show();
                            appCMSPlayVideoFragment.getVideoPlayerView().getPlayer().seekTo(currentPosition + ffAndRewindDelta);
                        }
                    } else if (intent.getStringExtra(getString(R.string.json_data_msg_key)).equalsIgnoreCase(getString(R.string.adm_directive_rewind))) {
                        if (appCMSPlayVideoFragment != null) {
                            long currentPosition = appCMSPlayVideoFragment.getVideoPlayerView().getCurrentPosition();
                            Log.d(TAG, "ADM Message. Current Time: " + currentPosition + ", seekTo: " + (currentPosition - ffAndRewindDelta));
                            appCMSPlayVideoFragment.getVideoPlayerView().getPlayerView().getController().show();
                            appCMSPlayVideoFragment.getVideoPlayerView().getPlayer().seekTo(appCMSPlayVideoFragment.getVideoPlayerView().getCurrentPosition() - ffAndRewindDelta);
                        }
                    } else if (intent.getStringExtra(getString(R.string.json_data_msg_key)).equalsIgnoreCase(getString(R.string.adm_directive_start_over))) {
                        if (appCMSPlayVideoFragment != null) {
                            appCMSPlayVideoFragment.getVideoPlayerView().getPlayerView().getController().show();
                            appCMSPlayVideoFragment.getVideoPlayerView().getPlayer().seekTo(0);
                        }
                    } else if (intent.getStringExtra(getString(R.string.json_data_msg_key)).equalsIgnoreCase(getString(R.string.adm_directive_next))) {
                        if (appCMSPlayVideoFragment != null) {
                            appCMSPlayVideoFragment.getVideoPlayerView().getPlayerView().getController().show();
                        }
                        onMovieFinished();
                    } else if (intent.getStringExtra(getString(R.string.json_data_msg_key)).equalsIgnoreCase(getString(R.string.adm_directive_previous))) {
                        if (currentlyPlayingIndex > 0) {
                            playPreviousMovie();
                        } else {
                            if (appCMSPlayVideoFragment != null) {
                                appCMSPlayVideoFragment.getVideoPlayerView().getPlayerView().getController().show();
                                appCMSPlayVideoFragment.getVideoPlayerView().getPlayer().seekTo(0);
                            }
                        }
                    } else if (intent.getStringExtra(getString(R.string.json_data_msg_key)).equalsIgnoreCase(getString(R.string.adm_directive_adjust_seek_position))) {
                        if (appCMSPlayVideoFragment != null) {
                            long currentPosition = appCMSPlayVideoFragment.getVideoPlayerView().getCurrentPosition();
                            long seekDelta = intent.getLongExtra(getString(R.string.json_seek_delta_key), 0);
                            long seekToPosition = currentPosition + seekDelta;
                            if (seekToPosition < 0) seekToPosition = 0;
                            appCMSPlayVideoFragment.getVideoPlayerView().getPlayerView().getController().show();
                            appCMSPlayVideoFragment.getVideoPlayerView().getPlayer().seekTo(seekToPosition);
                        }
                    }
                } else if (intent.getAction().equalsIgnoreCase(PRESENTER_OPEN_AUTOPLAY_SCREEN)){
                    Bundle args = intent.getBundleExtra(getString(R.string.app_cms_video_player_bundle_binder_key));
                    AppCMSVideoPageBinder binder = (AppCMSVideoPageBinder) args.getBinder(getString(R.string.app_cms_video_player_binder_key));
                    openAutoplayDialog(binder);
                } else if (intent.getAction().equalsIgnoreCase(LOGIN_SUCCESSFUL_ON_AUTOPLAY)){
                    binder.setCurrentPlayingVideoIndex(binder.getCurrentPlayingVideoIndex() - 1);
                    onCountdownFinished();
                }else if (intent.getAction().equals(AppCMSPresenter.TVOD_PURCHASE)) {
                    String sku = intent.getStringExtra(getString(R.string.tvod_sku));
                    PurchasingService.purchase(sku);
                }else if(intent.getAction().equals(AppCMSPresenter.UPDATE_SUBSCRIPTION)){
                    String movieId = binder.getContentData().getGist().getId();
                     if(binder != null && binder.getRelateVideoIds() != null){
                        movieId = binder.getRelateVideoIds().get(binder.getCurrentPlayingVideoIndex());
                     }
                    appCMSPresenter.refreshVideoData(movieId, contentDatum -> {
                         appCMSPresenter.stopLoader();
                        if(contentDatum != null) {
                            binder.setContentData(contentDatum);
                            appCMSPresenter.playNextVideo(binder,
                                    binder.getCurrentPlayingVideoIndex() + 1,
                                    0);
                            binder.setCurrentPlayingVideoIndex(binder.getCurrentPlayingVideoIndex() + 1);
                        }
                    }, null, false, true,binder.getContentData());
                }
            }
        };
        registerReceiver(closeActivityOnDeepLinkActionReceiver, new IntentFilter(getString(R.string.deeplink_close_player_activity_action)));
        registerReceiver(closeActivityOnDeepLinkActionReceiver, new IntentFilter(AppCMSPresenter.ACTION_LINK_YOUR_ACCOUNT));
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }


    @Override
    public int getNavigationContainer() {
        return 0;
    }

    private void openLoginOrSignUpDialog(Intent intent, Bundle args, AppCMSPresenter.ExtraScreenType extraScreenType) {
        AppCMSBinder binder = (AppCMSBinder) args.getBinder(getString(R.string.app_cms_binder_key));
        boolean isAutoplay = extraScreenType.equals(AppCMSPresenter.ExtraScreenType.AUTOPLAY);
        if (binder.getPageName().equalsIgnoreCase(getString(R.string.app_cms_sign_up_pager_title)) ||
                binder.getPageName().equalsIgnoreCase(getString(R.string.app_cms_pagename_create_login_key))) {
            openSignUpDialog(intent, true, isAutoplay);
        }else if(binder.getPageName().equalsIgnoreCase("enntitlement_screen")){
            openGenericDialog(intent,false);
        }else{
            openLoginDialog(intent, true, isAutoplay);
        }
    }

    private void openLinkYourAccountScreen(Intent intent) {
        if (null != intent) {
            Bundle bundle = intent.getBundleExtra(getString(R.string.app_cms_bundle_key));
            if (null != bundle) {
                AppCMSBinder appCMSBinder = (AppCMSBinder) bundle.get(getString(R.string.app_cms_binder_key));
                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                appCmsLinkYourAccountFragment = AppCmsLinkYourAccountFragment.newInstance(
                        appCMSBinder);
                appCmsLinkYourAccountFragment.show(ft, DIALOG_FRAGMENT_TAG);
                Utils.pageLoading(false, this);
            }
        }
    }

    private void sortStreamQualityMapByRendition() {

        try {
            TreeMap<String, String> sortMap = new TreeMap<String, String>(new Comparator<String>() {
                @Override
                public int compare(String o1, String o2) {
                    return Integer.valueOf(o2.replace("p", "")).compareTo(Integer.valueOf(o1.replace("p", "")));
                }
            });

            sortMap.putAll(availableStreamingFormats);
            availableStreamingFormats.clear();
            availableStreamingQualityMap.clear();
            availableStreamingFormats = sortMap;
            availableStreamingQualityMap = sortMap;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void initializeStreamingQualityValues(VideoAssets videoAssets) {
        if (availableStreamingQualityMap == null) {
            availableStreamingQualityMap = new HashMap<>();
        } else {
            availableStreamingQualityMap.clear();
        }
        if (availableStreamingFormats == null) {
            availableStreamingFormats = new HashMap<>();
        }
        if (videoAssets != null && videoAssets.getMpeg() != null) {
            List<Mpeg> availableMpegs = videoAssets.getMpeg();
            int numAvailableMpegs = availableMpegs.size();
            for (int i = 0; i < numAvailableMpegs; i++) {
                Mpeg availableMpeg = availableMpegs.get(i);
                String mpegUrl = availableMpeg.getUrl();
                if (availableMpeg.getRenditionValue() != null) {
                    String renVal = availableMpeg.getRenditionValue().replace("_", "");
                    if (!TextUtils.isEmpty(renVal)) {
                        availableStreamingFormats.put(renVal, availableMpeg.getUrl());
                        availableStreamingQualityMap.put(renVal, availableMpeg.getUrl());
                    }
                } else if (availableMpeg.getBitrate() > 0) {
                    String bitrateVal = buildBitrateString(availableMpeg.getBitrate());
                    if (!TextUtils.isEmpty(bitrateVal)) {
                        availableStreamingFormats.put(bitrateVal, availableMpeg.getUrl());
                        availableStreamingQualityMap.put(bitrateVal, availableMpeg.getUrl());
                    }
                } else if (!TextUtils.isEmpty(mpegUrl)) {
                    String resolution = getMpegResolutionFromUrl(mpegUrl);
                    if (!TextUtils.isEmpty(resolution)) {
                        availableStreamingFormats.put(resolution, availableMpeg.getUrl());
                        availableStreamingQualityMap.put(resolution, availableMpeg.getUrl());
                    }
                }
            }
            sortStreamQualityMapByRendition();
        }
    }

    private String buildBitrateString(int bitrate) {
        return String.format(Locale.US, "%.2fMbit", bitrate / 1000f);
    }

    private void launchVideoPlayer(Gist gist , AppCMSSignedURLResult appCMSSignedURLResult) {
        if (binder.getContentData() != null && binder.getContentData().getParentalRating() != null) {
            if (!appCMSPresenter.isPinVerified() && CommonUtils.isUnderAgeRestrictions(appCMSPresenter, binder.getContentData().getParentalRating())) {
                AppCMSTVVerifyVideoPinDialog.newInstance(isVerified -> {
                    appCMSPresenter.setPinVerified(isVerified);
                    if (isVerified) {
                        launchVideoPlayer(gist, appCMSSignedURLResult);
                    } else {
                        finish();
                    }
                }).show(getSupportFragmentManager(), AppCMSTVVerifyVideoPinDialog.class.getSimpleName());
                return;
            }
        }
        String videoUrl = "";
        String closedCaptionUrl = null;
        title = gist.getTitle();
        String fontColor = binder.getFontColor();
        boolean useHls = com.viewlift.Utils.isHLS();
        String defaultVideoResolution = getString(R.string.default_video_resolution);
        if (binder.getContentData() != null &&
                binder.getContentData().getStreamingInfo() != null &&
                binder.getContentData().getStreamingInfo().getVideoAssets() != null) {
            VideoAssets videoAssets = binder.getContentData().getStreamingInfo().getVideoAssets();

            initializeStreamingQualityValues(videoAssets);

            if (useHls) {
                if (binder.getContentData().isDRMEnabled()) {
                    videoUrl = videoAssets.getWideVine().getUrl();
                    licenseUrl = videoAssets.getWideVine().getLicenseUrl();
                    licenseToken = videoAssets.getWideVine().getLicenseToken();
                } else {
                    videoUrl = videoAssets.getHls();
                }
            }
            if (TextUtils.isEmpty(videoUrl)) {
                com.viewlift.Utils.setHls(false);
                if (videoAssets.getMpeg() != null && !videoAssets.getMpeg().isEmpty()) {
                    if (videoAssets.getMpeg().get(0) != null) {
                        videoUrl = videoAssets.getMpeg().get(0).getUrl();
                    }
                    for (int i = 0; i < videoAssets.getMpeg().size() /*&& TextUtils.isEmpty(videoUrl)*/; i++) {
                        if (videoAssets.getMpeg().get(i) != null &&
                                videoAssets.getMpeg().get(i).getRenditionValue() != null &&
                                videoAssets.getMpeg().get(i).getRenditionValue().contains(defaultVideoResolution)) {
                            videoUrl = videoAssets.getMpeg().get(i).getUrl();
                        }
                    }
                }
            }

            /*if (useHls && videoAssets.getMpeg() != null && videoAssets.getMpeg().size() > 0) {
                if (videoAssets.getMpeg().get(0).getUrl() != null &&
                        videoAssets.getMpeg().get(0).getUrl().indexOf("?") > 0) {
                    videoUrl = videoUrl + videoAssets.getMpeg().get(0).getUrl().substring(videoAssets.getMpeg().get(0).getUrl().indexOf("?"));
                }
            }*/
        }

        if (binder.getContentData() != null &&
                binder.getContentData().getGist() != null) {
            isLiveStream = binder.getContentData().getGist().isLiveStream();
        }
//        ArrayList<String> ccUrls = new ArrayList<>();
        // TODO: 7/27/2017 Implement CC for multiple languages.
        if (binder.getContentData() != null
                && binder.getContentData().getContentDetails() != null
                && binder.getContentData().getContentDetails().getClosedCaptions() != null
                && !binder.getContentData().getContentDetails().getClosedCaptions().isEmpty()) {
            for (ClosedCaptions cc : binder.getContentData().getContentDetails().getClosedCaptions()) {
                /*if (cc.getUrl() != null &&
                        !cc.getUrl().equalsIgnoreCase(getString(R.string.download_file_prefix)) &&
                        cc.getFormat() != null &&
                        cc.getFormat().equalsIgnoreCase("SRT")) {
                    closedCaptionUrl = cc.getUrl();
                }*/
//                ccUrls.add(cc.getUrl());
            }
        }
        String permaLink = gist.getPermalink();
        hlsUrl = videoUrl;
        videoImageUrl = gist.getVideoImageUrl();
        filmId = binder.getContentData().getGist().getOriginalObjectId();
        if (filmId == null) {
            filmId = binder.getContentData().getGist().getId();
        }
        String adsUrl = binder.getAdsUrl();
        String bgColor = binder.getBgColor();
        int playIndex = binder.getCurrentPlayingVideoIndex();
        long watchedTime = binder.getContentData().getGist().getWatchedTime();
        if (gist.getPrimaryCategory() != null && gist.getPrimaryCategory().getTitle() != null)
            primaryCategory = gist.getPrimaryCategory().getTitle();
        boolean playAds = binder.isPlayAds();


        if (appCMSPresenter.getAutoplayEnabledUserPref(this)) {
            if(appCMSPresenter.getAppCMSMain().getRecommendation()!=null &&
                    appCMSPresenter.getAppCMSMain().getRecommendation().isRecommendation() &&
                    appCMSPresenter.getAppCMSMain().getRecommendation().isRecommendationAutoPlay()){
                if(AppCMSPresenter.recommendedIds !=null
                        && AppCMSPresenter.recommendedIds.size() > 0
                        && appCMSPresenter.isRecommendationTray){
                    int currentVideoIndex = 0;
                    for(int i = 0; i< AppCMSPresenter.recommendedIds.size(); i++){
                        if(AppCMSPresenter.recommendedIds.get(i) != null
                                && AppCMSPresenter.recommendedIds.get(i).equalsIgnoreCase(filmId)){
                            currentVideoIndex=i;
                            break;
                        }
                    }
                    binder.setCurrentPlayingVideoIndex(currentVideoIndex);
                    binder.setRelateVideoIds(AppCMSPresenter.recommendedIds);
                }
            }
        }


        relateVideoIds = binder.getRelateVideoIds();
        currentlyPlayingIndex = binder.getCurrentPlayingVideoIndex();
        /*if (!TextUtils.isEmpty(bgColor)) {
            appCMSPlayVideoPageContainer.setBackgroundColor(Color.parseColor(bgColor));
        }*/

        boolean freeContent = false;
        if (binder.getContentData() != null && binder.getContentData().getGist() != null &&
                binder.getContentData().getGist().getFree()) {
            freeContent = binder.getContentData().getGist().getFree();
        }

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        appCMSPlayVideoFragment =
                AppCMSPlayVideoFragment.newInstance(this,
                        null,
                        fontColor,
                        title,
                        permaLink,
                        binder.isTrailer(),
                        hlsUrl,
                        filmId,
                        adsUrl,
                        playAds,
                        playIndex,
                        watchedTime,
                        binder.getContentData().getGist().getRuntime(),
                        null,
                        binder.getContentData().getContentDetails().getClosedCaptions(),
                        binder.getContentData().getParentalRating(),
                        freeContent,
                        appCMSSignedURLResult,
                        binder.getContentData(),
                        binder.getSeriesParentalRating(),
                        binder.getContentData().isDRMEnabled(),
                        licenseUrl, licenseToken);
        fragmentTransaction.add(R.id.app_cms_play_video_page_container,
                appCMSPlayVideoFragment,
                getString(R.string.video_fragment_tag_key));
        fragmentTransaction.addToBackStack(getString(R.string.video_fragment_tag_key));
        fragmentTransaction.commit();
        appCMSPlayVideoFragment.setVideoSelected(this);
    }


    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(LanguageHelper.onAttach(newBase));
    }

    /**
     * Method is used to find and return to-be-played url. Based on the selection and availability,
     * the method computes the HLS or MP4 url of a particular movie.
     *
     * @param videoAssets assets in which all the url data is present
     * @return to-be-played url
     */
    @Nullable
    private String getVideoUrl(VideoAssets videoAssets) {
        boolean useHls = com.viewlift.Utils.isHLS();
        String defaultVideoResolution = getString(R.string.default_video_resolution);
        String videoUrl = null;
        if (useHls) {
            videoUrl = videoAssets.getHls();
        }
        if (TextUtils.isEmpty(videoUrl)) {
            if (videoAssets.getMpeg() != null && !videoAssets.getMpeg().isEmpty()) {

                for (int i = 0; i < videoAssets.getMpeg().size() && TextUtils.isEmpty(videoUrl); i++) {
                    if (videoAssets.getMpeg().get(i) != null &&
                            videoAssets.getMpeg().get(i).getRenditionValue() != null &&
                            videoAssets.getMpeg().get(i).getRenditionValue().contains(defaultVideoResolution)) {
                        videoUrl = videoAssets.getMpeg().get(i).getUrl();
                    }
                }
                if (videoAssets.getMpeg().get(0) != null && TextUtils.isEmpty(videoUrl)) {
                    videoUrl = videoAssets.getMpeg().get(0).getUrl();
                }
            }
        }
        return videoUrl;
    }


    AppCmsLoginDialogFragment loginDialog;
    AppCmsSignUpDialogFragment signUpDialog;
    AppCMSTVAutoplayFragment autoplayDialogFragment;

    private void openLoginDialog(Intent intent, boolean isLoginPage, boolean isAutoplay) {
        if (null != intent) {
            Bundle bundle = intent.getBundleExtra(getString(R.string.app_cms_bundle_key));
            if (null != bundle) {
                if (isAutoplay)
                    appCMSPresenter.setLaunchType(AppCMSPresenter.LaunchType.AUTOPLAY_SCREEN);
                AppCMSBinder appCMSBinder = (AppCMSBinder) bundle.get(getString(R.string.app_cms_binder_key));
                bundle.putBoolean("isLoginPage", isLoginPage);
                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                loginDialog = AppCmsLoginDialogFragment.newInstance(
                        appCMSBinder);
                loginDialog.show(ft, "DIALOG_FRAGMENT_TAG");

                loginDialog.setBackKeyListener(new Action1<String>() {
                    @Override
                    public void call(String s) {
                        appCMSPlayVideoFragment.cancelTimer();
                        finish();
                    }
                });
            }
        }
    }

    private void openSignUpDialog(Intent intent, boolean isLoginPage, boolean isAutoplay) {
        if (null != intent) {
            Bundle bundle = intent.getBundleExtra(getString(R.string.app_cms_bundle_key));
            if (null != bundle) {
                AppCMSBinder appCMSBinder = (AppCMSBinder) bundle.get(getString(R.string.app_cms_binder_key));
                if (isAutoplay)
                    appCMSPresenter.setLaunchType(AppCMSPresenter.LaunchType.AUTOPLAY_SCREEN);
                bundle.putBoolean("isLoginPage", isLoginPage);
                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                signUpDialog = AppCmsSignUpDialogFragment.newInstance(
                        appCMSBinder);
                signUpDialog.show(ft, "DIALOG_FRAGMENT_TAG");

                signUpDialog.setBackKeyListener(new Action1<String>() {
                    @Override
                    public void call(String s) {
                        appCMSPlayVideoFragment.cancelTimer();
                        finish();
                    }
                });

            }
        }
    }

    private void openAutoplayDialog(AppCMSVideoPageBinder binder) {
        FragmentManager supportFragmentManager = getSupportFragmentManager();
        supportFragmentManager.popBackStack();
        FragmentTransaction fragmentTransaction = supportFragmentManager.beginTransaction();
        autoplayDialogFragment = AppCMSTVAutoplayFragment.newInstance(this, binder);
        fragmentTransaction.replace(R.id.app_cms_play_video_page_container, autoplayDialogFragment, binder.getContentData().getGist().getId());
        fragmentTransaction.addToBackStack(binder.getContentData().getGist().getId());
        fragmentTransaction.commit();

    }

    public void closeSignUpDialog() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (signUpDialog != null) {
                    signUpDialog.dismiss();
                    signUpDialog = null;
                }
            }
        }, 50);

    }

    public void closeSignInDialog() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (loginDialog != null) {
                    loginDialog.dismiss();
                    loginDialog = null;
                }
            }
        }, 50);

    }


    private void openResetPasswordScreen(Intent intent) {
        if (null != intent) {

            Bundle bundle = intent.getBundleExtra(getString(R.string.app_cms_bundle_key));
            if (null != bundle) {
                AppCMSBinder appCMSBinder = (AppCMSBinder) bundle.get(getString(R.string.app_cms_binder_key));
                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                appCmsResetPasswordFragment = AppCmsResetPasswordFragment.newInstance(
                        appCMSBinder);
                appCmsResetPasswordFragment.show(ft, "DIALOG_FRAGMENT_TAG");
                Utils.pageLoading(false, AppCMSTVPlayVideoActivity.this);
            }
        }
    }


    private void openErrorDialog(Intent intent) {
        Bundle bundle = intent.getBundleExtra(getString(R.string.retryCallBundleKey));
        bundle.putBoolean(getString(R.string.retry_key), bundle.getBoolean(getString(R.string.retry_key)));
        bundle.putBoolean(getString(R.string.register_internet_receiver_key), bundle.getBoolean(getString(R.string.register_internet_receiver_key)));
        bundle.putString(getString(R.string.tv_dialog_msg_key), bundle.getString(getString(R.string.tv_dialog_msg_key)));
        bundle.putString(getString(R.string.tv_dialog_header_key), bundle.getString(getString(R.string.tv_dialog_header_key)));

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        AppCmsTvErrorFragment newFragment = AppCmsTvErrorFragment.newInstance(
                bundle);
        newFragment.setErrorListener(this);
        newFragment.show(ft, "DIALOG_FRAGMENT_TAG");
        Utils.pageLoading(false, AppCMSTVPlayVideoActivity.this);
    }


    private void openGenericDialog(Intent intent, boolean isLoginPage) {
        if (null != intent) {
            Bundle bundle = intent.getBundleExtra(getString(R.string.app_cms_bundle_key));
            if (null != bundle) {
                AppCMSBinder appCMSBinder = (AppCMSBinder) bundle.get(getString(R.string.app_cms_binder_key));
                bundle.putBoolean("isLoginPage", isLoginPage);
                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                AppCmsGenericDialogFragment newFragment = AppCmsGenericDialogFragment.newInstance(
                        appCMSBinder);
                String fragmentTag = "DIALOG_FRAGMENT_TAG";
                if (appCMSBinder != null && appCMSBinder.getPageName().equals("VIEW PLANS")) {
                    fragmentTag = "VIEW PLANS";
                }
                    newFragment.show(ft, fragmentTag);
                Utils.pageLoading(false, this);
            }
        }
    }

    public void openGenericDialog(Intent intent){
        ClearDialogFragment newFragment = Utils.getClearDialogFragment(
                this,
                appCMSPresenter,
                getResources().getDimensionPixelSize(R.dimen.text_clear_dialog_width),
                getResources().getDimensionPixelSize(R.dimen.text_clear_dialog_height),
                intent.getStringExtra(getString(R.string.tv_dialog_header_key)),
                intent.getStringExtra(getString(R.string.tv_dialog_msg_key)),
                intent.getStringExtra(getString(R.string.tv_dialog_positive_button_key)),
                intent.getStringExtra(getString(R.string.tv_dialog_negative_button_key)),
                 14
        );

        newFragment.setOnPositiveButtonClicked(s -> {
            if(binder.getContentData() != null && binder.getContentData().getSubscriptionPlans() != null){
                appCMSPresenter.navigateToContentSubscription(binder.getContentData().getSubscriptionPlans());
            }else{
                Utils.pageLoading(true,this);
                /*PrimaryCta primaryCta =  appCMSPresenter.getNavigation().getSettings().getPrimaryCta();*/
                MetaPage viewPlanPage = appCMSPresenter.getSubscriptionPage();
                appCMSPresenter.navigateToTVPage(viewPlanPage.getPageId(),
                        viewPlanPage.getPageName(),
                        null,
                        false,
                        Uri.EMPTY,
                        false,
                        true, true, true, false, false);
            }
            appCMSPresenter.setViewPlanPageOpenFromADialog(true);
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        registerRecievers();
        appCMSPresenter.setCancelAllLoads(false);
        if (!appCMSPresenter.isNetworkConnected()) {
            // appCMSPresenter.showErrorDialog(AppCMSPresenter.Error.NETWORK, null); //TODO : need to show error dialog.
            finish();
        }

        appCMSPresenter.tryLaunchingPlayerFromDeeplink(new Action0() {
            @Override
            public void call() {
                AppCMSTVPlayVideoActivity.this.closePlayer();
            }
        });

    }

    private void registerRecievers() {
        registerReceiver(handoffReceiver, new IntentFilter(AppCMSPresenter.PRESENTER_CLOSE_SCREEN_ACTION));
        registerReceiver(handoffReceiver, new IntentFilter(AppCMSPresenter.PRESENTER_NAVIGATE_ACTION));
        registerReceiver(handoffReceiver, new IntentFilter(AppCMSPresenter.PRESENTER_OPEN_AUTOPLAY_SCREEN));
        registerReceiver(handoffReceiver, new IntentFilter(AppCMSPresenter.CLOSE_DIALOG_ACTION));
        registerReceiver(handoffReceiver, new IntentFilter(AppCMSPresenter.RECOMMENDATION_DIALOG_FRAGMENT_TAG));
        registerReceiver(handoffReceiver, new IntentFilter(AppCMSPresenter.UPDATE_SUBSCRIPTION));
        registerReceiver(handoffReceiver, new IntentFilter(AppCMSPresenter.ACTION_RESET_PASSWORD));
        registerReceiver(handoffReceiver, new IntentFilter(AppCMSPresenter.SHOW_SUBSCRIPTION_MESSAGE_ON_VIDEO_PLAYER_ACTION));
        registerReceiver(handoffReceiver, new IntentFilter(AppCMSPresenter.CLOSE_VIEW_PLANS_PAGE_ON_VIDEO_PLAYER_ACTION));
        registerReceiver(handoffReceiver, new IntentFilter(AppCMSPresenter.ERROR_DIALOG_ACTION));
        registerReceiver(handoffReceiver, new IntentFilter(AppCMSPresenter.PRESENTER_PAGE_LOADING_ACTION));
        registerReceiver(handoffReceiver, new IntentFilter(AppCMSPresenter.PRESENTER_STOP_PAGE_LOADING_ACTION));
        registerReceiver(handoffReceiver, new IntentFilter(LOGIN_SUCCESSFUL_ON_AUTOPLAY));
        registerReceiver(handoffReceiver, new IntentFilter(getString(R.string.intent_msg_action)));
        registerReceiver(handoffReceiver, new IntentFilter(AppCMSPresenter.GENERIC_DIALOG));
        registerReceiver(handoffReceiver, new IntentFilter(AppCMSPresenter.MAX_SIMULTANEOUS_STREAM_DIALOG));
        registerReceiver(handoffReceiver, new IntentFilter(AppCMSPresenter.RESTORE_PURCHASE_DIALOG));
        registerReceiver(handoffReceiver, new IntentFilter(AppCMSPresenter.TVOD_PURCHASE));
        registerReceiver(handoffReceiver, new IntentFilter(AppCMSPresenter.UPDATE_SUBSCRIPTION));
        registerReceiver(handoffReceiver, new IntentFilter(AppCMSPresenter.ENTITLEMENT_LOGIN_DIALOG));

    }

    @Override
    protected void onPause() {
        unregisterReceiver(handoffReceiver);
        super.onPause();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent returnIntent = new Intent();
        setResult(AppCompatActivity.RESULT_OK, returnIntent);
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(closeActivityOnDeepLinkActionReceiver);
    }

    @Override
    public void closePlayer() {
        finish();
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {

        boolean result = false;
        if (event.getAction() == KeyEvent.ACTION_DOWN) {
            if (null != appCMSPlayVideoFragment) {
                appCMSPlayVideoFragment.showController();
            }

            switch (event.getKeyCode()) {
                case KeyEvent.KEYCODE_MEDIA_NEXT:
                    if (appCMSPlayVideoFragment != null
                            && appCMSPlayVideoFragment.getVideoPlayerView() != null
                            && appCMSPlayVideoFragment.getVideoPlayerView().getPlayerView() != null
                            && appCMSPlayVideoFragment.getVideoPlayerView().getPlayer() != null) {
                        long currentPosition = appCMSPlayVideoFragment.getVideoPlayerView().getCurrentPosition();
                        appCMSPlayVideoFragment.getVideoPlayerView().getPlayerView().getController().show();
                        appCMSPlayVideoFragment.getVideoPlayerView().getPlayer().seekTo(currentPosition + ffAndRewindDelta);
                        appCMSPlayVideoFragment.getVideoPlayerView().getPlayer().setPlayWhenReady(appCMSPlayVideoFragment.getVideoPlayerView().getPlayer().getPlayWhenReady());
                    }
                    return true;
                case KeyEvent.KEYCODE_MEDIA_PREVIOUS:
                    if (appCMSPlayVideoFragment != null
                            && appCMSPlayVideoFragment.getVideoPlayerView() != null
                            && appCMSPlayVideoFragment.getVideoPlayerView().getPlayerView() != null
                            && appCMSPlayVideoFragment.getVideoPlayerView().getPlayer() != null) {
                        appCMSPlayVideoFragment.getVideoPlayerView().getPlayerView().getController().show();
                        appCMSPlayVideoFragment.getVideoPlayerView().getPlayer().seekTo(appCMSPlayVideoFragment.getVideoPlayerView().getCurrentPosition() - ffAndRewindDelta);
                        appCMSPlayVideoFragment.getVideoPlayerView().getPlayer().setPlayWhenReady(appCMSPlayVideoFragment.getVideoPlayerView().getPlayer().getPlayWhenReady());
                    }
                    return true;
                case KeyEvent.KEYCODE_MEDIA_PLAY_PAUSE:
                    if (null != appCMSPlayVideoPageContainer) {
                        if (appCMSPlayVideoPageContainer.findViewById(R.id.exo_pause) != null)
                            appCMSPlayVideoPageContainer.findViewById(R.id.exo_pause).requestFocus();

                        if (appCMSPlayVideoPageContainer.findViewById(R.id.exo_play) != null)
                            appCMSPlayVideoPageContainer.findViewById(R.id.exo_play).requestFocus();

                        if (appCMSPlayVideoFragment != null
                                && appCMSPlayVideoFragment.getVideoPlayerView() != null
                                && appCMSPlayVideoFragment.getVideoPlayerView().getPlayerView() != null) {
                            return super.dispatchKeyEvent(event)
                                    || appCMSPlayVideoFragment.getVideoPlayerView().getPlayerView()
                                    .dispatchKeyEvent(event);
                        }
                    }
                    break;
                case KeyEvent.KEYCODE_MEDIA_REWIND:
                    if (null != appCMSPlayVideoPageContainer&&null!=appCMSPlayVideoPageContainer.findViewById(R.id.exo_rew)) {
                        appCMSPlayVideoPageContainer.findViewById(R.id.exo_rew).requestFocus();
                        return super.dispatchKeyEvent(event);
                    }
                    break;
                case KeyEvent.KEYCODE_MEDIA_FAST_FORWARD:
                    if (null != appCMSPlayVideoPageContainer&&null!=appCMSPlayVideoPageContainer.findViewById(R.id.exo_ffwd)) {
                        appCMSPlayVideoPageContainer.findViewById(R.id.exo_ffwd).requestFocus();
                        return super.dispatchKeyEvent(event);
                    }
                    break;
            }
        }
        return super.dispatchKeyEvent(event);
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

    @Override
    public void onMovieFinished() {
        if (!TextUtils.isEmpty(appCMSPresenter.getAppPreference().getAppsFlyerKey())) {
            AppsFlyerUtils.setEventWatched( appCMSPresenter, binder.getContentData());
        }
        playNextMovie();
    }

    private void playNextMovie() {
        try {
            if (appCMSPresenter.getAutoplayEnabledUserPref(getApplication())) {
                if (!binder.isTrailer()
                        && relateVideoIds != null
                        && currentlyPlayingIndex + 1 < relateVideoIds.size()) {
                    binder.setCurrentPlayingVideoIndex(currentlyPlayingIndex);

                    appCMSPresenter.openAutoPlayScreen(binder, new Action1<Object>() {
                        @Override
                        public void call(Object o) {
                            if (o != null && o instanceof String && o.equals("server_error")) {
                                Toast.makeText(AppCMSTVPlayVideoActivity.this,
                                        appCMSPresenter.getLanguageResourcesFile().getUIresource(getString(R.string.next_video_loading_error_msg)),
                                        Toast.LENGTH_SHORT).show();
                            }
                            closePlayer();
                        }
                    });

                } else {
                    closePlayer();
                }
            } else {
                closePlayer();
            }
        } catch (IndexOutOfBoundsException e) {
            Toast.makeText(AppCMSTVPlayVideoActivity.this,
                    appCMSPresenter.getLanguageResourcesFile().getUIresource(getString(R.string.action_not_supported)),
                    Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
        }
    }

    private void playPreviousMovie() {
        try {
            if (appCMSPresenter.getAutoplayEnabledUserPref(getApplication())) {
                if (!binder.isTrailer()
                        && relateVideoIds != null
                        && currentlyPlayingIndex + 1 < relateVideoIds.size()) {
                    binder.setCurrentPlayingVideoIndex(currentlyPlayingIndex - 2);
                    appCMSPresenter.openAutoPlayScreen(binder, o -> closePlayer());
                } else {
                    closePlayer();
                }
            } else {
                closePlayer();
            }
        } catch (IndexOutOfBoundsException e) {
            Toast.makeText(AppCMSTVPlayVideoActivity.this,
                    appCMSPresenter.getLanguageResourcesFile().getUIresource(getString(R.string.action_not_supported)),
                    Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
        }
    }

    @Override
    public void onErrorScreenClose(boolean closeActivity) {
        if (closeActivity) {
            finish();
        }
    }

    @Override
    public void onRetry(Bundle bundle) {

    }

    @Override
    protected void onStop() {
        super.onStop();
        if (appCMSPlayVideoFragment != null) {
            appCMSPlayVideoFragment.cancelTimer();
        }
        finish();
    }

    @Override
    public List<String> getAvailableStreamingQualities() {
        if (availableStreamingFormats != null) {
            return new ArrayList<>(availableStreamingFormats.keySet());
        } else {
            return new ArrayList<>();
        }
    }

    /**
     * Retuns the hlsUrl variable be it hls or mp4
     *
     * @return
     */
    @Override
    public String getVideoUrl() {
        return hlsUrl;
    }

    @Override
    public void setVideoUrl(String url) {
        hlsUrl = url;
    }

    @Override
    public String getStreamingQualityUrl(String streamingQuality) {
        if (availableStreamingQualityMap != null && availableStreamingQualityMap.containsKey(streamingQuality)) {
            return availableStreamingQualityMap.get(streamingQuality);
        }
        return null;
    }

    @Override
    public String getMpegResolutionFromUrl(String mpegUrl) {
        if (mpegUrl != null) {
            int mpegIndex = mpegUrl.indexOf(".mp4");
            if (0 < mpegIndex) {
                int startIndex = mpegUrl.substring(0, mpegIndex).lastIndexOf("/");
                if (0 <= startIndex && startIndex < mpegIndex) {
                    return mpegUrl.substring(startIndex + 1, mpegIndex);
                }
            }
        }
        return null;
    }

    @Override
    public int getMpegResolutionIndexFromUrl(String mpegUrl) {
        if (!TextUtils.isEmpty(mpegUrl)) {
            String mpegUrlWithoutCdn = mpegUrl;
            int mp4Index = mpegUrl.indexOf(".mp4");
            if (0 <= mp4Index) {
                mpegUrlWithoutCdn = mpegUrl.substring(0, mp4Index);
            }
            List<String> availableStreamingQualities = getAvailableStreamingQualities();
            if (availableStreamingQualities != null) {
                for (int i = 0; i < availableStreamingQualities.size(); i++) {
                    String availableStreamingQuality = availableStreamingQualities.get(i);
                    if (!TextUtils.isEmpty(availableStreamingQuality)) {

                        if (availableStreamingQualityMap.get(availableStreamingQuality) != null &&
                                availableStreamingQualityMap.get(availableStreamingQuality).contains(mpegUrlWithoutCdn)) {
                            return i;
                        }
                    }
                }
            }
        }

        return availableStreamingFormats.size() - 1;
    }

    @Override
    public String getFilmId() {
        return filmId;
    }

    @Override
    public boolean isLiveStream() {
        return isLiveStream;
    }

    @Override
    public List<ClosedCaptions> getAvailableClosedCaptions() {
        List<ClosedCaptions> closedCaptionsList = new ArrayList<>();

        if (binder != null
                && binder.getContentData() != null
                && binder.getContentData().getContentDetails() != null
                && binder.getContentData().getContentDetails().getClosedCaptions() != null) {
            ArrayList<ClosedCaptions> closedCaptions = binder.getContentData().getContentDetails().getClosedCaptions();
            if (closedCaptions != null) {
                for (ClosedCaptions captions : closedCaptions) {
                    if ("SRT".equalsIgnoreCase(captions.getFormat())) {
                        closedCaptionsList.add(captions);
                    }
                }
            }
        }

        return closedCaptionsList;
    }

    @Override
    public String getSubtitleLanguageFromIndex(int index) {
        String language = null;

        if (binder != null
                && binder.getContentData() != null
                && binder.getContentData().getContentDetails() != null
                && binder.getContentData().getContentDetails().getClosedCaptions() != null) {
            ArrayList<ClosedCaptions> closedCaptions = binder.getContentData().getContentDetails().getClosedCaptions();
            List<ClosedCaptions> closedCaptionsList = new ArrayList<>();

            if (closedCaptions != null) {
                for (ClosedCaptions captions : closedCaptions) {
                    if ("SRT".equalsIgnoreCase(captions.getFormat())) {
                        closedCaptionsList.add(captions);
                    }
                }
            }

            if (!closedCaptionsList.isEmpty()) {
                language = closedCaptionsList.get(index).getLanguage();
            }
        }
        return language;
    }

    @Override
    public void viewClick(View view, int height) {
        appCMSPlayVideoFragment.setBackgroundViewVisibility(true);
        showSeasonEpisodeViewOnPlayer(view, height);
    }


    public void showSeasonEpisodeViewOnPlayer(View anchorView, int height) {
        LayoutInflater inflater = (LayoutInflater) anchorView.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (seasonEpisodeView == null)
            seasonEpisodeView = inflater.inflate(R.layout.player_seasons_episode_list, null);
        ButterKnife.bind(this, seasonEpisodeView);

        episodeTitle.setText(appCMSPresenter.getLocalisedStrings().getEpisodesHeaderText());
        seasonTitle.setText(appCMSPresenter.getLocalisedStrings().getSeasonsLabelText());

        seasonRecylerView.setLayoutManager(new LinearLayoutManager(this,
                LinearLayoutManager.VERTICAL,
                false));
        episodeRecylerView.setLayoutManager(new LinearLayoutManager(this,
                LinearLayoutManager.VERTICAL,
                false));

        if (seasonEpisodePopUpWindow == null) {
            seasonEpisodePopUpWindow = new PopupWindow(this);
            seasonEpisodePopUpWindow.setContentView(seasonEpisodeView);
            seasonEpisodePopUpWindow.setHeight(980 - height);
            seasonEpisodeView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
            seasonEpisodePopUpWindow.setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
            seasonEpisodePopUpWindow.setWidth(Utils.getDeviceWidth(this) / 2);
        }

        if (Build.VERSION.SDK_INT >= 21) {
            seasonEpisodePopUpWindow.setElevation(5.0f);
        }

        if (moduleApi != null) {
            seasonRecylerView.setAdapter(new PlayerSeasonAdapter(moduleApi.getContentData().get(0).getSeason(), appCMSPresenter, currentPlayingSeasonPosition()));

            PlayerEpisodeAdapter playerEpisodeAdapter = new PlayerEpisodeAdapter(moduleApi.getContentData().get(0).getSeason().get(0).getEpisodes(), episodeRecylerView, appCMSPresenter);
            playerEpisodeAdapter.setVideoSelected(this);
            SeasonTabSelectorBus.instanceOf().setTab(moduleApi.getContentData().get(0).getSeason().get(currentPlayingSeasonPosition()).getEpisodes());

            episodeRecylerView.setAdapter(playerEpisodeAdapter);
            seasonEpisodePopUpWindow.setFocusable(true);
            seasonEpisodePopUpWindow.update();
            seasonEpisodePopUpWindow.setOnDismissListener(() -> {
                appCMSPlayVideoFragment.setPreviousNextVisibility(true);
                appCMSPlayVideoFragment.loadPrevNextImage();
            });
            int[] viewLocation = new int[2];
            anchorView.getLocationOnScreen(viewLocation);
            if (!seasonEpisodePopUpWindow.isShowing()) {
                seasonEpisodePopUpWindow.showAtLocation(anchorView, Gravity.BOTTOM, viewLocation[0], height);
            }
            appCMSPlayVideoFragment.setPreviousNextVisibility(false);
        }
    }

    private int currentPlayingSeasonPosition() {
        int position = 0;
        if (moduleApi.getContentData().get(0).getSeason() != null) {
            for (int i = 0; i < moduleApi.getContentData().get(0).getSeason().size(); i++) {
                if (moduleApi.getContentData().get(0).getSeason().get(i).getEpisodes() != null) {
                    for (int j = 0; j < moduleApi.getContentData().get(0).getSeason().get(i).getEpisodes().size(); j++) {
                        if (filmId.equalsIgnoreCase(moduleApi.getContentData().get(0).getSeason().get(i).getEpisodes().get(j).getGist().getId()))
                            return i;
                    }
                }
            }
        }
        return position;
    }

    @Override
    public void selectedVideoListener(ContentDatum contentDatum, int positionPlayed) {
        if (seasonEpisodePopUpWindow != null && seasonEpisodePopUpWindow.isShowing())
            seasonEpisodePopUpWindow.dismiss();
        contentDatum.setModuleApi(moduleApi);
        finish();
        appCMSPresenter.playNextVideo(appCMSPresenter.getDefaultAppCMSVideoPageBinder(contentDatum, positionPlayed,
                null, binder.isOffline(), binder.isTrailer(), binder.isPlayAds(),
                binder.getAdsUrl(), binder.getBgColor(), null),
                positionPlayed,
                0);
    }

    private void openParentalGateViewFragment() {
        ParentalGateViewFragment parentalGateViewFragment = ParentalGateViewFragment.getInstance();
        parentalGateViewFragment.show(getSupportFragmentManager().beginTransaction(), "DIALOG_FRAGMENT_TAG");
    }

    @Override
    public void onCorrectImageSelected() {
        Bundle args = mIntent.getBundleExtra(getString(R.string.app_cms_bundle_key));
        AppCMSBinder binder = (AppCMSBinder) args.getBinder(getString(R.string.app_cms_binder_key));
        openLoginOrSignUpDialog(mIntent, args, binder.getExtraScreenType());
        new Handler().postDelayed(() -> {
            if (ParentalGateViewFragment.getInstance() != null
                    && ParentalGateViewFragment.getInstance().isVisible()
                    && ParentalGateViewFragment.getInstance().isAdded()) {
                ParentalGateViewFragment.getInstance().dismiss();
            }
        }, 1000);
    }

    @Override
    public void onSuccess(AppCMSVideoPageBinder binder) {

    }

    @Override
    public void onError(AppCMSVideoPageBinder binder) {

    }

    @Override
    public void onCountdownFinished() {
        try {
            appCMSPlayVideoFragment.getVideoPlayerView().releasePlayer();
            binder.setCurrentPlayingVideoIndex(binder.getCurrentPlayingVideoIndex() + 1);
            String movieId = binder.getRelateVideoIds().get(binder.getCurrentPlayingVideoIndex());
            appCMSPresenter.refreshVideoData(movieId, contentDatum -> {
                ContentDatum contentData = binder.getContentData();
                contentDatum.setModuleApi(binder.getContentData().getModuleApi());
            binder.setContentData(contentDatum);
            if (contentDatum != null) {
                if (contentDatum.getVideoPlayError() == null) {
                        getSupportFragmentManager().popBackStack();
                    launchVideoPlayer(contentDatum.getGist(), contentDatum.getAppCMSSignedURLResult());
                } else {
                    binder.setCurrentPlayingVideoIndex(binder.getCurrentPlayingVideoIndex() - 1);
                    appCMSPresenter.openTVErrorDialog(
                            contentDatum.getVideoPlayError(),
                            "",
                            false
                    );
                }
            } /*else {
                binder.setCurrentPlayingVideoIndex(binder.getCurrentPlayingVideoIndex() - 1);
                    if (appCMSPresenter.isFireTVSubscriptionEnabled()) {
                        if (contentData != null && contentData.getSubscriptionPlans() != null) {
                            openNewSubscriptionDialog();
                        } else {
                            openSubscriptionDialog();
                        }
                    } else {
                        openEntitlementDialog();
                    }
                }*/
            }, null, false, true,binder.getContentData());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void openNewSubscriptionDialog() {
        String dialogMessage = appCMSPresenter.getLocalisedStrings().getWaysToWatchMessageText();
        String dialogTitle = appCMSPresenter.getLocalisedStrings().getWaysToWatchText();;
        String positiveButtonText = appCMSPresenter.getLocalisedStrings().getSignInText();
        String negativeButtonText = appCMSPresenter.getLocalisedStrings().getBecomeMemberText();
        String neutralButtonText = null;
        String extraButtonText = null;

        if(appCMSPresenter.isUserLoggedIn() && appCMSPresenter.getAppPreference().getLoggedInUserEmail() != null) {
            positiveButtonText = "";
        }

        if((contentDatum.getSubscriptionPlans()!=null&&contentTypeChecker.isContentSVOD_TVE(contentDatum.getSubscriptionPlans(),contentDatum.getModuleApi().getContentData().get(0).getMonetizationModels())) &&
                (!appCMSPresenter.isUserLoggedInByTVProvider() || !appCMSPresenter.isUserSubscribed())){
            positiveButtonText = "";
            negativeButtonText = "";
            neutralButtonText = appCMSPresenter.getLocalisedStrings().getChooseTVProviderText();
        }

        if(contentDatum.getSubscriptionPlans()!=null&&contentTypeChecker.isContentTVE(contentDatum.getSubscriptionPlans()) && !appCMSPresenter.isUserLoggedInByTVProvider()){
            neutralButtonText = appCMSPresenter.getLocalisedStrings().getChooseTVProviderText();
        }else if(contentDatum.getSubscriptionPlans()!=null&&contentTypeChecker.isContentSVOD_TVOD(contentDatum.getSubscriptionPlans(),contentDatum.getModuleApi().getContentData().get(0).getMonetizationModels())){
            extraButtonText = appCMSPresenter.getLocalisedStrings().getOwnText();
        }

        PreviewDialogFragment newFragment = Utils.getPreviewDialogFragment(
                this,
                appCMSPresenter,
                getResources().getDimensionPixelSize(R.dimen.preview_overlay_dialog_width),
                getResources().getDimensionPixelSize(R.dimen.preview_overlay_dialog_height),
                dialogTitle,
                dialogMessage,
                positiveButtonText,
                negativeButtonText,
                neutralButtonText,
                extraButtonText,
                14
        );
        appCMSPlayVideoFragment.isSubscriptionDialogVisible = true;
        newFragment.setOnPositiveButtonClicked(new Action1<String>() {
            @Override
            public void call(String s) {
                boolean showParentalGateView = appCMSPresenter.getAppCMSMain().getCompliance() != null
                        && appCMSPresenter.getAppCMSMain().getCompliance().isCoppa();

                appCMSPlayVideoFragment.isSubscriptionDialogVisible = false;
                NavigationUser navigationUser = appCMSPresenter.getLoginNavigation();
                appCMSPresenter.navigateToTVPage(
                        navigationUser.getPageId(),
                        navigationUser.getTitle(),
                        navigationUser.getUrl(),
                        false,
                        Uri.EMPTY,
                        false,
                        false,
                        true, false,
                        showParentalGateView,
                        false);
            }
        });

        newFragment.setOnNegativeButtonClicked(s -> {
            appCMSPlayVideoFragment.isSubscriptionDialogVisible = false;
            if(contentDatum != null && contentDatum.getSubscriptionPlans() != null){
                appCMSPresenter.navigateToContentSubscription(contentDatum.getSubscriptionPlans());
            }else{
                Utils.pageLoading(true, this);
                MetaPage viewPlanPage = appCMSPresenter.getSubscriptionPage();
                appCMSPresenter.navigateToTVPage(viewPlanPage.getPageId(),
                        viewPlanPage.getPageName(),
                        null,
                        false,
                        Uri.EMPTY,
                        false,
                        true, true, true, false, false);
            }
            appCMSPresenter.setViewPlanPageOpenFromADialog(true);
        });

        newFragment.setOnNeutralButtonClicked(s -> {
            if ((appCMSPresenter.getLaunchType() != (AppCMSPresenter.LaunchType.NAVIGATE_TO_HOME_FROM_LOGIN_DIALOG))) {
                appCMSPresenter.setLaunchType(AppCMSPresenter.LaunchType.HOME);
            }
            String[] extraData = new String[1];
            extraData[0] = getResources().getString(R.string.app_cms_page_link_your_account_with_tv_provider_btn_key);
            appCMSPresenter.launchTVButtonSelectedAction(
                    null,
                    "linkAccount",
                    null,
                    extraData,
                    null,
                    false,
                    0,
                    null,
                    null);
        });
    }

    public void openSubscriptionDialog(){
        String dialogMessage;
        String positiveButtonText;
        if (!appCMSPresenter.isUserLoggedIn()) {
            dialogMessage = appCMSPresenter.getLocalisedStrings().getEncourageUserLoginText();
            positiveButtonText = appCMSPresenter.getLocalisedStrings().getSignInText();
        } else {
            dialogMessage = appCMSPresenter.getLocalisedStrings().getEncourageUserLoginText();
            positiveButtonText = "";
        }

        String negativeButtonText = appCMSPresenter.getLocalisedStrings().getSubscribeNowText();
        String dialogTitle = appCMSPresenter.getLocalisedStrings().getPremiumContentText();
        ClearDialogFragment newFragment = Utils.getClearDialogFragment(
                this,
                appCMSPresenter,
                getResources().getDimensionPixelSize(R.dimen.text_clear_dialog_width),
                getResources().getDimensionPixelSize(R.dimen.text_add_to_watchlist_sign_in_dialog_height),
                dialogTitle,
                dialogMessage,
                positiveButtonText,
                negativeButtonText,
                14
        );

        newFragment.setOnPositiveButtonClicked(s -> {
            Utils.pageLoading(true, AppCMSTVPlayVideoActivity.this);
            NavigationUser navigationUser = appCMSPresenter.getLoginNavigation();
            appCMSPresenter.navigateToTVPage(
                    navigationUser.getPageId(),
                    navigationUser.getTitle(),
                    navigationUser.getUrl(),
                    false,
                    Uri.EMPTY,
                    false,
                    false,
                    true, false, false, false);
        });

        newFragment.setOnNegativeButtonClicked(s -> {
            Utils.pageLoading(true, AppCMSTVPlayVideoActivity.this);
            MetaPage subscriptionPage = appCMSPresenter.getSubscriptionPage();
            if (subscriptionPage != null) {
                String title = getString(R.string.view_plans_label);
                if(appCMSPresenter.getGenericMessages() != null
                        && appCMSPresenter.getGenericMessages().getViewPlansCta() != null){
                    title = appCMSPresenter.getGenericMessages().getViewPlansCta();
                }
                appCMSPresenter.navigateToTVPage(subscriptionPage.getPageId(),
                        title,
                        null,
                        false,
                        Uri.EMPTY,
                        false,
                        true, false, true, false, false);
                appCMSPresenter.setViewPlanPageOpenFromADialog(true);
            }
        });

        newFragment.setOnBackKeyListener(s -> {
            newFragment.dismiss();
            finish();
        });
    }

    public void openEntitlementDialog() {
        String dialogMessage;
        String dialogTitle;
        String positiveButtonText;
        if (appCMSPresenter.isUserLoggedIn()) {
            dialogMessage = appCMSPresenter.getLocalisedStrings().getWebSubscriptionMessagePrefixText()
                    + " " + appCMSPresenter.getLocalisedStrings().getWebSubscriptionMessageSuffixText();
            dialogTitle = appCMSPresenter.getLocalisedStrings().getPremiumContentText();
            positiveButtonText = "";
        } else {
            dialogMessage = appCMSPresenter.getLocalisedStrings().getWebSubscriptionMessagePrefixText()
                    + " " + appCMSPresenter.getLocalisedStrings().getWebSubscriptionMessageSuffixText();
            dialogTitle = appCMSPresenter.getLocalisedStrings().getLoginRequiredText();
            positiveButtonText = appCMSPresenter.getLocalisedStrings().getSignInText();
        }

        String cancelCTA = appCMSPresenter.getLocalisedStrings().getCancelText();
        ClearDialogFragment newFragment = Utils.getClearDialogFragment(
                this,
                appCMSPresenter,
                getResources().getDimensionPixelSize(R.dimen.text_clear_dialog_width),
                getResources().getDimensionPixelSize(R.dimen.text_add_to_watchlist_sign_in_dialog_height),
                dialogTitle,
                dialogMessage,
                positiveButtonText,
                cancelCTA,
                14
        );

        newFragment.setOnPositiveButtonClicked(s -> {
            Utils.pageLoading(true, AppCMSTVPlayVideoActivity.this);
            NavigationUser navigationUser = appCMSPresenter.getLoginNavigation();
            appCMSPresenter.navigateToTVPage(
                    navigationUser.getPageId(),
                    navigationUser.getTitle(),
                    navigationUser.getUrl(),
                    false,
                    Uri.EMPTY,
                    false,
                    false,
                    true, false, false, false);
        });

        newFragment.setOnNegativeButtonClicked(s -> newFragment.dismiss());

        newFragment.setOnBackKeyListener(s -> newFragment.dismiss());
    }


    public void openRestorePurchaseDialog(String email) {
        String dialogMessage = getResources().getString(R.string.already_subscribe_msg, email);
        String positiveButtonText = appCMSPresenter.getLanguageResourcesFile().getUIresource(getString(R.string.signin));
        String cancelCTA = appCMSPresenter.getLanguageResourcesFile().getUIresource(getString(R.string.go_to_home));

        if (appCMSPresenter.isUserLoggedIn()) {
            positiveButtonText = appCMSPresenter.getLanguageResourcesFile().getUIresource(getString(R.string.cancel));
        }
        ClearDialogFragment newFragment = Utils.getClearDialogFragment(
                this,
                appCMSPresenter,
                getResources().getDimensionPixelSize(R.dimen.text_clear_dialog_width),
                getResources().getDimensionPixelSize(R.dimen.text_add_to_watchlist_sign_in_dialog_height),
                "",
                dialogMessage,
                positiveButtonText,
                cancelCTA,
                 14
        );

        newFragment.setOnPositiveButtonClicked(s -> {

            if (appCMSPresenter.isUserLoggedIn()) {
                newFragment.dismiss();
            } else {
                Utils.pageLoading(true, AppCMSTVPlayVideoActivity.this);
                NavigationUser navigationUser = appCMSPresenter.getLoginNavigation();
                appCMSPresenter.navigateToTVPage(
                        navigationUser.getPageId(),
                        navigationUser.getTitle(),
                        navigationUser.getUrl(),
                        false,
                        Uri.EMPTY,
                        false,
                        false,
                        appCMSPresenter.isViewPlanPageOpenFromADialog(), false, false, false);
            }
        });

        newFragment.setOnNegativeButtonClicked(s -> {
            Utils.pageLoading(true, AppCMSTVPlayVideoActivity.this);
            appCMSPresenter.navigateToHomePage(true);
        });

        newFragment.setOnBackKeyListener(s -> newFragment.dismiss());
    }
    public void openMaxSimultaneousStreamErrorDialog (Intent intent) {
        Bundle bundle = intent.getBundleExtra(getString(R.string.retryCallBundleKey));
        String maxStreamErrorText = bundle.getString(getString(R.string.tv_dialog_msg_key));
        if (TextUtils.isEmpty(maxStreamErrorText)) {
            maxStreamErrorText = appCMSPresenter.getLocalisedStrings().getMaxStreamErrorText();
        }
        ClearDialogFragment newFragment = Utils.getClearDialogFragment(
                appCMSPresenter.getCurrentContext(),
                appCMSPresenter,
                appCMSPresenter.getCurrentContext().getResources().getDimensionPixelSize(R.dimen.text_clear_dialog_width),
                appCMSPresenter.getCurrentContext().getResources().getDimensionPixelSize(R.dimen.text_add_to_watchlist_sign_in_dialog_height),
                null,
                maxStreamErrorText,
                null,
                appCMSPresenter.getLanguageResourcesFile().getUIresource(appCMSPresenter.getCurrentContext().getString(R.string.close)),
                14
        );

        newFragment.setOnPositiveButtonClicked(s -> {
            newFragment.dismiss();
            appCMSPresenter.getCurrentActivity().finish();

        });

        newFragment.setOnNegativeButtonClicked(s -> {
            newFragment.dismiss();
            appCMSPresenter.getCurrentActivity().finish();
        });
    }

    @Override
    public void closeActivity() {
        appCMSPresenter.sendCloseOthersAction(null, true, false);
        finish();
    }
}
