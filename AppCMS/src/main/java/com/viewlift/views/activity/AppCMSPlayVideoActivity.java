package com.viewlift.views.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.PopupWindow;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.exoplayer2.offline.Download;
import com.google.android.exoplayer2.offline.DownloadRequest;
import com.google.android.exoplayer2.upstream.FileDataSource;
import com.viewlift.AppCMSApplication;
import com.viewlift.BuildConfig;
import com.viewlift.R;
import com.viewlift.Utils;
import com.viewlift.analytics.AppsFlyerUtils;
import com.viewlift.casting.CastHelper;
import com.viewlift.casting.CastServiceProvider;
import com.viewlift.casting.CastingUtils;
import com.viewlift.db.AppPreference;
import com.viewlift.models.data.appcms.api.AppCMSSignedURLResult;
import com.viewlift.models.data.appcms.api.ClosedCaptions;
import com.viewlift.models.data.appcms.api.ContentDatum;
import com.viewlift.models.data.appcms.api.Gist;
import com.viewlift.models.data.appcms.api.Module;
import com.viewlift.models.data.appcms.api.Mpeg;
import com.viewlift.models.data.appcms.api.VideoAssets;
import com.viewlift.models.data.appcms.downloads.DownloadClosedCaptionRealm;
import com.viewlift.models.data.appcms.downloads.DownloadStatus;
import com.viewlift.models.data.appcms.ui.main.LocalisedStrings;
import com.viewlift.offlinedrm.OfflineVideoData;
import com.viewlift.presenters.AppCMSPresenter;
import com.viewlift.utils.CommonUtils;
import com.viewlift.utils.ContentTypeChecker;
import com.viewlift.views.adapters.PlayerEpisodeAdapter;
import com.viewlift.views.adapters.PlayerSeasonAdapter;
import com.viewlift.views.binders.AppCMSVideoPageBinder;
import com.viewlift.views.customviews.BaseView;
import com.viewlift.views.customviews.VideoPlayerView;
import com.viewlift.views.dialog.AppCMSVerifyVideoPinDialog;
import com.viewlift.views.fragments.AppCMSPlayVideoFragment;
import com.viewlift.views.fragments.OnResumeVideo;
import com.viewlift.views.listener.VideoSelected;
import com.viewlift.views.rxbus.SeasonTabSelectorBus;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.functions.Action0;
import rx.functions.Action1;

import static com.viewlift.presenters.AppCMSPresenter.DialogType.VIDEO_NOT_AVAILABLE_ALERT;

public class AppCMSPlayVideoActivity extends AppCompatActivity implements
        AppCMSPlayVideoFragment.OnClosePlayerEvent,
        AppCMSPlayVideoFragment.OnUpdateContentDatumEvent,
        VideoPlayerView.StreamingQualitySelector,
        VideoPlayerView.ClosedCaptionSelector,
        VideoPlayerView.SeasonEpisodeSelctionEvent,
        //  VideoPlayerSettingsEvent,
        AppCMSPlayVideoFragment.RegisterOnResumeVideo, VideoSelected {
    private static final String TAG = "VideoPlayerActivity";

    private BroadcastReceiver handoffReceiver;
    private ConnectivityManager connectivityManager;
    private BroadcastReceiver networkConnectedReceiver;
    @Inject
    AppPreference appPreference;
    @Inject
    AppCMSPresenter appCMSPresenter;
    @Inject
    LocalisedStrings localisedStrings;

    private int currentlyPlayingIndex = 0;
    private AppCMSVideoPageBinder binder;
    private List<String> relateVideoIds;
    private String title;
    private String hlsUrl;
    private String videoImageUrl;
    private String filmId;
    private boolean isLiveStream;
    private boolean useHls;
    private String primaryCategory;
    private String contentRating;
    private long videoRunTime;
    private FrameLayout appCMSPlayVideoPageContainer;
    private CastServiceProvider castProvider;

    private Map<String, String> availableStreamingQualityMap;
    private List<String> availableStreamingQualities;
    Module moduleApi;
    private OnResumeVideo onResumeVideo;
    AppCMSPlayVideoFragment appCMSPlayVideoFragment;
    ContentDatum videoContentData;
    ContentTypeChecker contentTypeChecker;
    /*@Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(LanguageHelper.onAttach(base));
    }*/

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        setFullScreenFocus();
        super.onCreate(savedInstanceState);
        if (!BuildConfig.DEBUG && getWindow() != null) {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE,
                    WindowManager.LayoutParams.FLAG_SECURE);
        }
        setContentView(R.layout.activity_video_player_page);
        ((AppCMSApplication) getApplicationContext()).getAppCMSPresenterComponent().inject(this);
        if (appCMSPresenter != null &&
                appCMSPresenter.getAppCMSMain() != null &&
                appCMSPresenter.getAppCMSMain().getFeatures() != null) {
            Utils.setHls(appCMSPresenter.getAppCMSMain().getFeatures().isHls());
        }
        contentTypeChecker = new ContentTypeChecker(this);
        //Check in File Data Source.
        FileDataSource.setIsVideoDownloadDRM(false);
        getBundleData(getIntent());
        if (null != appCMSPresenter) {
            appCMSPresenter.stopAudioServices();
            appCMSPresenter.setDefaultTrailerPlay(false);
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        try {
            Fragment fragmentPlayer = getSupportFragmentManager().findFragmentById(R.id.app_cms_play_video_page_container);
            if (fragmentPlayer != null) {
                getSupportFragmentManager()
                        .beginTransaction()
                        .remove(getSupportFragmentManager().findFragmentById(R.id.app_cms_play_video_page_container)).commitAllowingStateLoss();
            }
        } catch (Exception e) {

        }
        getBundleData(intent);
        super.onNewIntent(intent);
    }

    private void getBundleData(Intent intent) {
        appCMSPlayVideoPageContainer =
                findViewById(R.id.app_cms_play_video_page_container);

        Bundle bundleExtra = intent.getBundleExtra(getString(R.string.app_cms_video_player_bundle_binder_key));
        String[] extra = intent.getStringArrayExtra(getString(R.string.video_player_hls_url_key));
        useHls = Utils.isHLS();
        String defaultVideoResolution = getString(R.string.default_video_resolution);
        try {
            binder = (AppCMSVideoPageBinder)
                    bundleExtra.getBinder(getString(R.string.app_cms_video_player_binder_key));
            if (binder != null
                    && binder.getContentData() != null
                    && binder.getContentData().getGist() != null) {
                moduleApi = binder.getContentData().getModuleApi();
                Gist gist = binder.getContentData().getGist();
                title = binder.getContentData().getGist().getTitle();
                filmId = binder.getContentData().getGist().getOriginalObjectId();
                if (filmId == null) {
                    filmId = binder.getContentData().getGist().getId();
                }
                if (binder.isTrailer()) {
                    filmId = null;
                    if (binder.getContentData() != null &&
                            binder.getContentData().getContentDetails() != null &&
                            binder.getContentData().getContentDetails().getTrailers() != null &&
                            !binder.getContentData().getContentDetails().getTrailers().isEmpty() &&
                            binder.getContentData().getContentDetails().getTrailers().get(0) != null) {
                        filmId = binder.getContentData().getContentDetails().getTrailers().get(0).getId();
                        title = binder.getContentData().getContentDetails().getTrailers().get(0).getTitle();
                    } else if (binder.getContentData().getShowDetails() != null &&
                            binder.getContentData().getShowDetails().getTrailers() != null &&
                            !binder.getContentData().getShowDetails().getTrailers().isEmpty() &&
                            binder.getContentData().getShowDetails().getTrailers().get(0) != null &&
                            binder.getContentData().getShowDetails().getTrailers().get(0).getId() != null) {
                        filmId = binder.getContentData().getShowDetails().getTrailers().get(0).getId();
                        title = binder.getContentData().getShowDetails().getTrailers().get(0).getTitle();
                    }
                }
                videoContentData = binder.getContentData();

                int currentPlayIndex = findCurrentPlayingPositionOfEpisode();
                if (currentPlayIndex != -1 && videoContentData != null && allEpisodes != null)
                    videoContentData = allEpisodes.get(currentPlayIndex);
                if (binder.isOffline()) {
                    Handler handler = new Handler();
                    handler.postDelayed(() -> {
                        try {
                            appCMSPresenter.setCurrentPlayingVideo(filmId);
                            launchVideoPlayer(gist, extra, useHls, defaultVideoResolution, intent, null, true, true,false);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }, 500);
                } else {
                    boolean isNeedCallBack = false;
                    if (binder.getContentData() != null && binder.getContentData().getPricing() != null && binder.getContentData().getPricing().getType() != null && (binder.getContentData().getPricing().getType().equalsIgnoreCase(this.getString(R.string.PURCHASE_TYPE_SVOD_TVOD)) ||
                            binder.getContentData().getPricing().getType().equalsIgnoreCase(this.getString(R.string.PURCHASE_TYPE_SVOD_PPV)))) {
                        isNeedCallBack = true;
                    }
                    if (filmId != null) {
                        appCMSPresenter.showLoader();
                        appCMSPresenter.refreshVideoData(filmId,
                                updatedContentDatum -> {
                                    appCMSPresenter.stopLoader();
                                    if (updatedContentDatum != null) {
                                        try {
                                            updatedContentDatum.setModuleApi(binder.getContentData().getModuleApi());
                                            boolean isTvodCastAllowed = true;
                                            boolean isTvodContent = false;
                                            boolean isSvodContent = false;
                                            if (updatedContentDatum.getSubscriptionPlans() != null && contentTypeChecker.isContentTVOD(updatedContentDatum.getSubscriptionPlans())) {
                                                isTvodCastAllowed = updatedContentDatum.getSubscriptionPlans().get(0).getFeatureSetting().isBeamingAllowed();
                                                isTvodContent = true;
                                            }if (updatedContentDatum.getSubscriptionPlans() != null && contentTypeChecker.isContentSVOD(updatedContentDatum.getSubscriptionPlans())) {
                                                isSvodContent = true;
                                            }
                                            if (updatedContentDatum.getSubscriptionPlans() != null && contentTypeChecker.isContentTVOD(updatedContentDatum.getSubscriptionPlans())
                                                    && !contentTypeChecker.isContentConsumptionAndroid(updatedContentDatum.getSubscriptionPlans().get(0).getFeatureSetting().getAllowedDevices())) {
                                                appCMSPresenter.showDialog(VIDEO_NOT_AVAILABLE_ALERT, appCMSPresenter.getLocalisedStrings().getVideoUnavailablePlatformMsg(), false, () -> {
                                                    closePlayer();
                                                }, null, appCMSPresenter.getLocalisedStrings().getAlertTitle());
                                                return;
                                            } else if (!appPreference.isAndroidPlayAllowed()) {
                                                appCMSPresenter.showEntitlementDialog(AppCMSPresenter.DialogType.SUBSCRIPTION_PLAN_UPGRADE,
                                                        () -> {
                                                        }, null);
                                                return;
                                            } else if (updatedContentDatum.isLoginRequired()) {
                                                appCMSPresenter.showEntitlementDialog(AppCMSPresenter.DialogType.ENTITLEMENT_LOGIN_REQUIRED,
                                                        () -> {
                                                            closePlayer();
                                                        }, null);
                                                return;
                                            } else if (updatedContentDatum.isEmailRequired()) {
                                                appCMSPresenter.emailDialog(new Action0() {
                                                    @Override
                                                    public void call() {
                                                        if (appPreference.getLoggedInUserEmail() == null)
                                                            closePlayer();
                                                        else {
                                                            getBundleData(intent);
                                                        }
                                                    }
                                                });
                                                return;
                                            }
                                            if (updatedContentDatum.getAdUrl() != null)
                                                binder.setAdsUrl(updatedContentDatum.getAdUrl());
                                            else
                                                binder.setAdsUrl(null);

                                            updatedContentDatum.setModuleApi(binder.getContentData().getModuleApi());
                                            binder.setContentData(updatedContentDatum);
                                            appCMSPresenter.setCurrentContentDatum(updatedContentDatum);
                                            if (binder.getRelateVideoIds() != null)
                                                updatedContentDatum.getContentDetails().setRelatedVideoIds(appCMSPresenter.relatedVideosForShows(updatedContentDatum, binder.getRelateVideoIds()));
                                            launchVideoPlayer(updatedContentDatum.getGist(), extra, useHls,
                                                    defaultVideoResolution, intent,
                                                    binder.getContentData().getAppCMSSignedURLResult(), isTvodCastAllowed, isTvodContent,isSvodContent);
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                            appCMSPresenter.showDialog(AppCMSPresenter.DialogType.VIDEO_NOT_AVAILABLE,
                                                    localisedStrings.getVideoNotloadedText(),
                                                    false,
                                                    this::finish,
                                                    null, null);
                                        }
                                    } else {
                                        appCMSPresenter.showDialog(AppCMSPresenter.DialogType.VIDEO_NOT_AVAILABLE,
                                                localisedStrings.getVideoNotloadedText(),
                                                false,
                                                this::finish,
                                                null, null);
                                    }
                                }, null, false, isNeedCallBack, videoContentData);
                    } else {
                        appCMSPresenter.showDialog(AppCMSPresenter.DialogType.VIDEO_NOT_AVAILABLE,
                                localisedStrings.getVideoNotloadedText(),
                                false,
                                this::finish,
                                null, null);
                    }
                }
            } else {
                appCMSPresenter.showDialog(AppCMSPresenter.DialogType.VIDEO_NOT_AVAILABLE,
                        localisedStrings.getVideoNotloadedText(),
                        false,
                        this::finish,
                        null, null);
            }
        } catch (ClassCastException e) {
            //Log.e(TAG, e.getMessage());
        }

        handoffReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (intent != null &&
                        intent.getStringExtra(getString(R.string.app_cms_package_name_key)) != null &&
                        !intent.getStringExtra(getString(R.string.app_cms_package_name_key)).equals(getPackageName())) {
                    return;
                }
                String sendingPage = intent.getStringExtra(getString(R.string.app_cms_closing_page_name));
                if (intent.getBooleanExtra(getString(R.string.close_self_key), true) &&
                        (sendingPage == null || getString(R.string.app_cms_video_page_tag).equals(sendingPage))) {
                    //Log.d(TAG, "Closing activity");
//                    finish();
                }
            }
        };

        connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        networkConnectedReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (intent != null &&
                        intent.getStringExtra(getString(R.string.app_cms_package_name_key)) != null &&
                        !intent.getStringExtra(getString(R.string.app_cms_package_name_key)).equals(getPackageName())) {
                    return;
                }

                NetworkInfo activeNetwork = connectivityManager.getActiveNetworkInfo();
                if ((appCMSPresenter.getCurrentActivity() instanceof AppCMSPlayVideoActivity) && appCMSPresenter.getCurrentPlayingVideo() != null &&
                        appCMSPresenter.isVideoDownloaded(appCMSPresenter.getCurrentPlayingVideo())) {
                    return;
                }

                if ((((appCMSPresenter.getCurrentActivity() instanceof AppCMSPlayVideoActivity)) ||
                        ((appCMSPresenter.getCurrentActivity() instanceof AppCMSPlayAudioActivity))) &&
                        filmId != null) {
                    DownloadRequest download = appCMSPresenter.getAppCMSApplication().getOfflineDRMManager().getDownloadTracker().isVideoOfflineDownloaded(filmId);
                    if(download != null)
                        return;
                }

                try {
                    if (((binder != null &&
                            binder.getContentData() != null &&
                            binder.getContentData().getGist() != null &&
                            ((binder.getContentData().getGist().getDownloadStatus() != null &&
                                    binder.getContentData().getGist().getDownloadStatus() != DownloadStatus.STATUS_COMPLETED &&
                                    binder.getContentData().getGist().getDownloadStatus() != DownloadStatus.STATUS_SUCCESSFUL) ||
                                    binder.getContentData().getGist().getDownloadStatus() == null))) &&
                            (activeNetwork == null ||
                                    !activeNetwork.isConnectedOrConnecting())) {
                        appCMSPresenter.showDialog(AppCMSPresenter.DialogType.NETWORK,
                                localisedStrings.getInternetConnectionMsg(),
                                false, () -> closePlayer(),
                                null, null);
                    } else if (onResumeVideo != null) {
                        onResumeVideo.onResumeVideo();
                    }
                } catch (Exception e) {
                    if ((binder != null &&
                            binder.getContentData() != null &&
                            binder.getContentData().getGist() != null &&
                            ((binder.getContentData().getGist().getDownloadStatus() != null &&
                                    binder.getContentData().getGist().getDownloadStatus() != DownloadStatus.STATUS_COMPLETED &&
                                    binder.getContentData().getGist().getDownloadStatus() != DownloadStatus.STATUS_SUCCESSFUL) ||
                                    binder.getContentData().getGist().getDownloadStatus() == null))) {
                        appCMSPresenter.showDialog(AppCMSPresenter.DialogType.NETWORK,
                                localisedStrings.getInternetConnectionMsg(),
                                false, () -> closePlayer(),
                                null, null);
                    }
                }
            }
        };

        registerReceiver(handoffReceiver, new IntentFilter(AppCMSPresenter.PRESENTER_CLOSE_SCREEN_ACTION));
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }

    String licenseUrl;
    String licenseToken;

    public void launchVideoPlayer(Gist gist,
                                  String[] extra,
                                  boolean useHls,
                                  String defaultVideoResolution,
                                  Intent intent,
                                  AppCMSSignedURLResult appCMSSignedURLResult, boolean tvodCastAllowed, boolean isTvodContent, boolean isSvodContent) {

        if (binder.getContentData() != null && binder.getContentData().getParentalRating() != null) {
            final boolean hasHls = useHls;
            if (!appCMSPresenter.isPinVerified() && CommonUtils.isUnderAgeRestrictions(appCMSPresenter, binder.getContentData().getParentalRating())) {
                AppCMSVerifyVideoPinDialog.newInstance(isVerified -> {
                    appCMSPresenter.setPinVerified(isVerified);
                    if (isVerified) {
                        launchVideoPlayer(gist, extra, hasHls, defaultVideoResolution, intent,
                                appCMSSignedURLResult, tvodCastAllowed,isTvodContent, isSvodContent);
                    } else {
                        finish();
                    }
                }, true).show(getSupportFragmentManager(), AppCMSVerifyVideoPinDialog.class.getSimpleName());
                return;
            }
        }

        String videoUrl = "";
        String closedCaptionUrl = null;
        //title = gist.getTitle();
        appPreference.setPlayingVideo(true);

        if (gist != null && gist.getKisweEventId() != null &&
                gist.getKisweEventId().trim().length() > 0) {
            //appCMSPresenter.launchKiswePlayer(gist.getKisweEventId());
            finish();
        } else if (binder.isOffline()
                && extra != null
                && extra.length >= 2
                && extra[1] != null
                && gist.getDownloadStatus().equals(DownloadStatus.STATUS_SUCCESSFUL)) {
            videoUrl = !TextUtils.isEmpty(extra[1]) ? extra[1] : "";
        }

        /*If the video is already downloaded, play if from there, even if Internet is
         * available*/
        else if (gist.getId() != null
                && appCMSPresenter.getRealmController() != null
                && appCMSPresenter.getRealmController().getDownloadByIdBelongstoUser(gist.getId(), appPreference.getLoggedInUser()) != null
                && appCMSPresenter.getRealmController().getDownloadById(gist.getId()).getDownloadStatus() != null
                && appCMSPresenter.getRealmController().getDownloadById(gist.getId()).getDownloadStatus().equals(DownloadStatus.STATUS_SUCCESSFUL)) {
            videoUrl = appCMSPresenter.getRealmController().getDownloadById(gist.getId()).getLocalURI();
        }
        else if(appCMSPresenter.getAppCMSApplication().getOfflineDRMManager().getDownloadTracker().isVideoOfflineDownloaded(filmId)!=null){
            videoUrl = "";
        }
        else if (binder.getContentData() != null &&
                binder.getContentData().getStreamingInfo() != null &&
                binder.getContentData().getStreamingInfo().getVideoAssets() != null) {
            VideoAssets videoAssets = binder.getContentData().getStreamingInfo().getVideoAssets();

            if (useHls) {
                videoUrl = videoAssets.getHls();
                if (binder.getContentData().isDRMEnabled()) {
                    videoUrl = videoAssets.getWideVine().getUrl();
                    licenseUrl = videoAssets.getWideVine().getLicenseUrl();
                    licenseToken = videoAssets.getWideVine().getLicenseToken();
                }
                /*for hls streaming quality values are extracted in the VideoPlayerView class*/
            } /*else {
                initializeStreamingQualityValues(videoAssets);
            }*/
            if (TextUtils.isEmpty(videoUrl)) {
                useHls = false;
                Utils.setHls(false);
                initializeStreamingQualityValues(videoAssets);
                if (videoAssets.getMpeg() != null && !videoAssets.getMpeg().isEmpty()) {
                    if (videoAssets.getMpeg().get(0) != null) {
                        videoUrl = videoAssets.getMpeg().get(0).getUrl();
                    }
                    for (int i = 0; i < videoAssets.getMpeg().size() && TextUtils.isEmpty(videoUrl); i++) {
                        if (videoAssets.getMpeg().get(i) != null &&
                                videoAssets.getMpeg().get(i).getRenditionValue() != null &&
                                videoAssets.getMpeg().get(i).getRenditionValue().contains(defaultVideoResolution)) {
                            videoUrl = videoAssets.getMpeg().get(i).getUrl();
                        }
                    }
                }
            }/*else{
                // For handling the case when one video do not have HLS URL and other might have HLS URL
                Utils.setHls(appCMSPresenter.getAppCMSMain().getFeatures().isHls());
            }*/


        }
        if (binder.getContentData() != null &&
                binder.getContentData().getStreamingInfo() != null) {
            isLiveStream = binder.getContentData().getStreamingInfo().isLiveStream();
        }
        // TODO: 7/27/2017 Implement CC for multiple languages.
        if (binder.getContentData() != null
                && binder.getContentData().getContentDetails() != null
                && binder.getContentData().getContentDetails().getClosedCaptions() != null
                && !binder.getContentData().getContentDetails().getClosedCaptions().isEmpty()) {
            for (ClosedCaptions cc : binder.getContentData().getContentDetails().getClosedCaptions()) {
                if (cc.getUrl() != null) {
                    if ((cc.getFormat() != null &&
                            "srt".equalsIgnoreCase(cc.getFormat())) ||
                            cc.getUrl().toLowerCase().contains("srt")) {
                        closedCaptionUrl = cc.getUrl();
                    }
                }
            }
        }

        String permaLink = gist.getPermalink();
        hlsUrl = videoUrl;
        //hlsUrl = "https://tveuniversalkids-vh.akamaihd.net/i/prod/video/578/19/200131_4108714_Top_3_Shared_Room_Makeovers_anvver_4_,2,40,25,18,12,7,4,00.mp4.csmil/master.m3u8?__b__=1000&hdnea=st=1595529770~exp=1595530700~acl=/i/prod/video/578/19/200131_4108714_Top_3_Shared_Room_Makeovers_anvver_4_*~id=dc0a4c88-dfde-4960-8e97-f6f3ce4289ee~hmac=289208309d38bfd9b89948431b6ab8a637c5f622525e2b5e8eb63dbcbc60e31e";
        //Temp addition for Kron issue
        // hlsUrl = "https://tkx.apis.anvato.net/rest/v2/mcp/video/adst1kWZmkpydXKx?anvack=EZmrR4Ev5xYnMfdbY5TPc7naKmy32MKp&eud=gN70du6Yp5XrWjnoU65Bv3WZ7yP7GLCKRBi7%2FVkJ5HCc0BXP9UzS6ElZevVkLOM6tTSH%2F8iIWfnerHhk3PBSlA%3D%3D";
        // hlsUrl = "http://sample.vodobox.com/planete_interdite/planete_interdite_alternate.m3u8";
        // hlsUrl = "https://supercrosshls.akamaized.net/Renditions/20181211/1544557853691_1810879sx60_sec_videopass_ondemand/hls/1544557853691_1810879sx60_sec_videopass_ondemand.m3u8?hdnts=exp=1546014040~acl=/Renditions/20181211/1544557853691_1810879sx60_sec_videopass_ondemand/hls/*~hmac=989254390b632a398aefd8193b43b92b5c1f73bb5812f0e7b505a347abeea239";

        videoImageUrl = gist.getVideoImageUrl();

        if (binder.getContentData() != null && binder.getContentData().getGist() != null) {
            filmId = binder.getContentData().getGist().getOriginalObjectId();
            if (filmId == null) {
                filmId = binder.getContentData().getGist().getId();
            }
            videoRunTime = binder.getContentData().getGist().getRuntime();
        }

        appCMSPresenter.setCurrentPlayingVideo(filmId);
        String adsUrl = binder.getAdsUrl();
        String bgColor = binder.getBgColor();
        int playIndex = binder.getCurrentPlayingVideoIndex();
        long watchedTime = intent.getLongExtra(getString(R.string.watched_time_key), 0L);
        long duration = binder.getContentData().getGist().getRuntime();
        double durationPercentage = ((0.95) * duration);
        if (duration <= watchedTime ) {
            watchedTime = 0L;
        }
        if(watchedTime >= durationPercentage ) {
            watchedTime = 0L;
        }

        if (gist.getPrimaryCategory() != null && gist.getPrimaryCategory().getTitle() != null) {
            primaryCategory = gist.getPrimaryCategory().getTitle();
        }

        boolean playAds = binder.isPlayAds();


        if (appCMSPresenter.getAutoplayEnabledUserPref(this)) {
            if (appCMSPresenter.getAppCMSMain().getRecommendation() != null &&
                    (appCMSPresenter.getAppCMSMain().getRecommendation().isRecommendation()
                            || appCMSPresenter.getAppCMSMain().getRecommendation().isPersonalization())
                    && appCMSPresenter.getAppCMSMain().getRecommendation().isRecommendationAutoPlay()) {
                if (CommonUtils.recommendedIds != null
                        && CommonUtils.recommendedIds.size() > 0
                        && appCMSPresenter.isRecommendationTrayClicked) {
                    int currentVideoIndex = 0;
                    for (int i = 0; i < CommonUtils.recommendedIds.size(); i++) {
                        if (CommonUtils.recommendedIds.get(i) != null
                                && CommonUtils.recommendedIds.get(i).equalsIgnoreCase(filmId)) {
                            currentVideoIndex = i;
                            break;
                        }
                    }
                    binder.setCurrentPlayingVideoIndex(currentVideoIndex);
                    binder.setRelateVideoIds(CommonUtils.recommendedIds);
                }
            }
        }

        relateVideoIds = binder.getRelateVideoIds();
        if (moduleApi != null && moduleApi.getContentData() != null && moduleApi.getContentData().size() > 0 && moduleApi.getContentData().get(0).getSeason() != null) {
            relateVideoIds = new ArrayList<>();
            for (int i = 0; i < moduleApi.getContentData().get(0).getSeason().size(); i++) {
                if (moduleApi.getContentData().get(0).getSeason().get(i).getEpisodes() != null) {
                    for (int j = 0; j < moduleApi.getContentData().get(0).getSeason().get(i).getEpisodes().size(); j++) {
                        try {
                            relateVideoIds.add(moduleApi.getContentData().get(0).getSeason().get(i).getEpisodes().get(j).getGist().getId());
                        } catch (Exception e) {
                            if (moduleApi.getContentData().get(0).getSeason().get(i).getEpisodes().get(j).getGist() == null) {
                                relateVideoIds.add(moduleApi.getContentData().get(0).getSeason().get(i).getEpisodes().get(j).getId());
                            }
                        }
                    }
                }
            }
            binder.setRelateVideoIds(relateVideoIds);
        }
        currentlyPlayingIndex = binder.getCurrentPlayingVideoIndex();

        if (binder.getContentData() != null && binder.getContentData().getParentalRating() != null) {
            //contentRating = binder.getContentData().getParentalRating() == null ? getString(R.string.age_rating_converted_default) : binder.getContentData().getParentalRating();
            contentRating = binder.getContentData().getParentalRating();
        }

        boolean freeContent = false;
        if (binder.getContentData() != null && binder.getContentData().getGist() != null &&
                binder.getContentData().getGist().getFree()) {
            freeContent = binder.getContentData().getGist().getFree();
        }

        try {
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            appCMSPlayVideoFragment =
                    AppCMSPlayVideoFragment.newInstance(AppCMSPlayVideoActivity.this,
                            primaryCategory,
                            title,
                            permaLink,
                            binder.isTrailer(),
                            hlsUrl,
                            filmId,
                            adsUrl,
                            playAds,
                            playIndex,
                            watchedTime,
                            videoImageUrl,
                            closedCaptionUrl,
                            contentRating, videoRunTime,
                            freeContent,
                            appCMSSignedURLResult,
                            binder.getContentData().isDRMEnabled(),
                            licenseUrl,
                            licenseToken,
                            binder.getContentData(),
                            binder.isOffline(), tvodCastAllowed, isTvodContent,isSvodContent);
            fragmentTransaction.add(R.id.app_cms_play_video_page_container,
                    appCMSPlayVideoFragment,
                    getString(R.string.video_fragment_tag_key));
            fragmentTransaction.addToBackStack(getString(R.string.video_fragment_tag_key));
            fragmentTransaction.commit();
            appCMSPlayVideoFragment.setVideoSelected(this);
        } catch (Exception e) {
            //
            appCMSPresenter.showDialog(AppCMSPresenter.DialogType.VIDEO_NOT_AVAILABLE,
                    localisedStrings.getVideoNotloadedText(),
                    false,
                    this::finish,
                    null, null);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        // This is to enable offline video playback even when Internet is not available.
        if (binder != null && !binder.isOffline() && !appCMSPresenter.isNetworkConnected()) {
            appCMSPresenter.showDialog(AppCMSPresenter.DialogType.NETWORK,
                    localisedStrings.getInternetConnectionMsg(),
                    false,
                    null,
                    null, null);
            finish();
        }

        registerReceiver(networkConnectedReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));

/**
 * if landscape only true than show player only in landscape view
 */
        if (appCMSPresenter.isPlayerLandscapeOnly()) {
            appCMSPresenter.restrictLandscapeOnly();

        } else {
            appCMSPresenter.unrestrictPortraitOnly();
        }

        appCMSPresenter.setCancelAllLoads(false);
    }

    @Override
    public void onBackPressed() {
//        super.onBackPressed();
        appCMSPresenter.setDefaultTrailerPlay(false);
        Fragment attachedView = getSupportFragmentManager().findFragmentById(R.id.app_cms_play_video_page_container);

        if (attachedView instanceof AppCMSPlayVideoFragment) {
            ((AppCMSPlayVideoFragment) attachedView).updateWatchedHistory();
        }

        if (attachedView != null) {
            if (((AppCMSPlayVideoFragment) attachedView).getPlayerSettingsView() != null && ((AppCMSPlayVideoFragment) attachedView).getPlayerSettingsView().getVisibility() == View.VISIBLE) {
                ((AppCMSPlayVideoFragment) attachedView).getPlayerSettingsView().setCCSelectionIndex();
                ((AppCMSPlayVideoFragment) attachedView).finishPlayerSetting();
            } else {
                finish();
                appCMSPresenter.setEntitlementPendingVideoData(null);
            }
        } else {
            finish();
        }

    }

    @Override
    protected void onDestroy() {
        try {
            unregisterReceiver(handoffReceiver);
            unregisterReceiver(networkConnectedReceiver);
        } catch (Exception e) {
            //Log.e(TAG, "Failed to unregister Handoff Receiver: " + e.getMessage());
        }

        try {
            appPreference.setPlayingVideo(false);
        } catch (Exception e) {

        }
        appCMSPresenter.setCurrentPlayingVideo(null);
        FileDataSource.setIsVideoDownloadDRM(false);
        appCMSPresenter.setDefaultTrailerPlay(false);
        appCMSPresenter.sendRefreshPageAction();
        super.onDestroy();
    }


    @Override
    public void closePlayer() {
        finish();
    }

    @Override
    public void onMovieFinished() {
        if (!TextUtils.isEmpty(appPreference.getAppsFlyerKey())) {
            AppsFlyerUtils.setEventWatched(appCMSPresenter, binder.getContentData());
        }
        if (appCMSPresenter.getAutoplayEnabledUserPref(getApplication())) {
            int currentPlayIndex = findCurrentPlayingPositionOfEpisode();
            binder.setCurrentPlayingVideoIndex(currentPlayIndex);
            if (appCMSPresenter.getRestWorkoutDialog()) {
                closePlayer();
                appCMSPresenter.launchRestWorkoutPage(moduleApi, binder);
            } else if (!binder.isOffline()) {
                if (!binder.isTrailer()
                        && relateVideoIds != null
                        && currentPlayIndex + 1 < relateVideoIds.size()) {
                    appCMSPresenter.openAutoPlayScreen(binder, o -> {
                    });
                } else {
                    closePlayer();
                }
            } else {
                if (binder.getRelateVideoIds() != null
                        && currentPlayIndex + 1 < relateVideoIds.size()) {
                    appCMSPresenter.openAutoPlayScreen(binder, o -> {
                    });
                } else {
                    closePlayer();
                }
            }
        } else {
            closePlayer();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        try {
            unregisterReceiver(networkConnectedReceiver);
        } catch (Exception e) {

        }
    }

    @Override
    public void onRemotePlayback(long currentPosition,
                                 int castingModeChromecast,
                                 boolean sendBeaconPlay,
                                 Action1<CastHelper.OnApplicationEnded> onApplicationEndedAction) {
        if (binder != null) {
            appCMSPresenter.sendCastEvent(binder.getContentData());
            if (castingModeChromecast == CastingUtils.CASTING_MODE_CHROMECAST && !binder.isTrailer()) {
                if (binder.isOffline()) {
                    appCMSPresenter.refreshVideoData(binder.getContentData().getGist().getId(), contentDatum -> {
                        binder.setContentData(contentDatum);
                        CastHelper.getInstance(getApplicationContext()).launchRemoteMedia(appCMSPresenter,
                                relateVideoIds,
                                filmId,
                                currentPosition,
                                binder,
                                sendBeaconPlay,
                                onApplicationEndedAction);
                    }, null, false, false,binder.getContentData());
                } else {
                    CastHelper.getInstance(getApplicationContext()).launchRemoteMedia(appCMSPresenter,
                            relateVideoIds,
                            filmId,
                            currentPosition,
                            binder,
                            sendBeaconPlay,
                            onApplicationEndedAction);
                }
            } else if (castingModeChromecast == CastingUtils.CASTING_MODE_CHROMECAST && binder.isTrailer()) {
                CastHelper.getInstance(getApplicationContext()).launchTrailer(appCMSPresenter, filmId, binder, currentPosition);
            }
        }
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus){
            setFullScreenFocus();
        }
    }

    private void setFullScreenFocus() {
        if (getWindow() != null) {
            getWindow().getDecorView()
                    .setSystemUiVisibility(
                            View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                                    | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                                    | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                                    | View.SYSTEM_UI_FLAG_FULLSCREEN // hide status bar
                                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        }
    }

    @Override
    public void updateContentDatum(ContentDatum contentDatum) {
        if (binder != null) {
            binder.setContentData(contentDatum);
        }
    }

    @Override
    public ContentDatum getCurrentContentDatum() {
        if (binder != null && binder.getContentData() != null) {
            return binder.getContentData();
        }
        return null;
    }

    @Override
    public List<String> getCurrentRelatedVideoIds() {
        if (binder != null && binder.getRelateVideoIds() != null) {
            return binder.getRelateVideoIds();
        }
        return null;
    }

    @Override
    public List<String> getAvailableStreamingQualities() {
        if (availableStreamingQualities != null) {
            Set<String> set = new LinkedHashSet<>(availableStreamingQualities);
            availableStreamingQualities = new ArrayList<>(set);
            return availableStreamingQualities;
        }
        return new ArrayList<>();
    }

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
            int index = 0;
            for (Map.Entry<String, String> entry : availableStreamingQualityMap.entrySet()) {
                // System.out.println(entry.getKey() + "  / " + entry.getValue());
                if (entry.getValue().toLowerCase().equalsIgnoreCase(mpegUrl.toLowerCase())) {
                    //   System.out.println(entry.getKey() + " index =" + index + "/ " + entry.getValue());
                    return index;
                }
                index++;
            }

            /*String mpegUrlWithoutCdn = mpegUrl;
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
            }*/
        }

        return availableStreamingQualities.size() - 1;
    }

    @Override
    public String getFilmId() {
        return filmId;
    }

    @Override
    public boolean isLiveStream() {
        return isLiveStream;
    }

    private void initializeStreamingQualityValues(VideoAssets videoAssets) {
        if (availableStreamingQualityMap == null) {
            availableStreamingQualityMap = new TreeMap<>();
        } else {
            availableStreamingQualityMap.clear();
        }
        if (availableStreamingQualities == null) {
            availableStreamingQualities = new ArrayList<>();
        } else {
            availableStreamingQualities.clear();
        }
        if (videoAssets != null && videoAssets.getMpeg() != null) {
            List<Mpeg> availableMpegs = videoAssets.getMpeg();
            int numAvailableMpegs = availableMpegs.size();
            for (int i = 0; i < numAvailableMpegs; i++) {
                Mpeg availableMpeg = availableMpegs.get(i);
                String resolution = null;
                if (!TextUtils.isEmpty(availableMpeg.getRenditionValue())) {
                    resolution = availableMpeg.getRenditionValue().replace("_", "");
                } else {
                    String mpegUrl = availableMpeg.getUrl();
                    if (!TextUtils.isEmpty(mpegUrl)) {
                        resolution = getMpegResolutionFromUrl(mpegUrl);
                    }
                }
                if (!TextUtils.isEmpty(resolution)) {
                    availableStreamingQualities.add(resolution);
                    availableStreamingQualityMap.put(resolution, availableMpeg.getUrl());
                }
            }
        }

        Collections.sort(availableStreamingQualities, (q1, q2) -> {
            int i1 = Integer.valueOf(q1.replace("p", ""));
            int i2 = Integer.valueOf(q2.replace("p", ""));
            if (i2 > i1) {
                return -1;
            } else if (i1 == i2) {
                return 0;
            } else {
                return 1;
            }
        });


        int numStreamingQualities = availableStreamingQualities.size();
        for (int i = 0; i < numStreamingQualities; i++) {
            availableStreamingQualities.set(i, availableStreamingQualities.get(i));
        }
        //sortStreamQualityMapByRendition();

    }

    private void sortStreamQualityMapByRendition() {

        try {
            TreeMap<String, String> sortMap = new TreeMap<String, String>(new Comparator<String>() {
                @Override
                public int compare(String o1, String o2) {
                    return Integer.valueOf(o1.replace("p", "")).compareTo(Integer.valueOf(o2.replace("p", "")));
                    // return Integer.valueOf(o2.replace("p", "")).compareTo(Integer.valueOf(o1.replace("p", "")));
                }
            });

            sortMap.putAll(availableStreamingQualityMap);
            availableStreamingQualityMap.clear();
            availableStreamingQualityMap = sortMap;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void registerOnResumeVideo(OnResumeVideo onResumeVideo) {
        this.onResumeVideo = onResumeVideo;
    }

    @Override
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (BaseView.isTablet(this)) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);
        } else {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Making sure video is always played in Landscape
        Fragment attachedView = getSupportFragmentManager().findFragmentById(R.id.app_cms_play_video_page_container);
        if (attachedView != null) {
            //((AppCMSPlayVideoFragment)attachedView).finishPlayerSetting();

            /**
             * if landscape only true than show player only in landscape view and if setting screen is visile then show it in landscape
             */
            if (((AppCMSPlayVideoFragment) attachedView).getPlayerSettingsView() != null
                    && ((AppCMSPlayVideoFragment) attachedView).getPlayerSettingsView().getVisibility() == View.VISIBLE
                    && appCMSPresenter != null
                    && !appCMSPresenter.isPortraitViewing()) {
                appCMSPresenter.restrictLandscapeOnly();

            } else {
                if (appCMSPresenter.isPlayerLandscapeOnly()) {
                    appCMSPresenter.restrictLandscapeOnly();

                } else {
                    appCMSPresenter.unrestrictPortraitOnly();
                }
            }
        }

    }

    @Override
    public List<ClosedCaptions> getAvailableClosedCaptions() {
         List<ClosedCaptions> closedCaptionsList = new ArrayList<>();
        //This is check for offline Drm
        boolean isOfflineVideoDowloaded = appCMSPresenter.isOfflineVideoDownloaded(filmId);
        if(isOfflineVideoDowloaded && binder.isOffline()){
            try {
                Download offlineVideo = appCMSPresenter.getAppCMSApplication().getOfflineDRMManager().getDownloadTracker().getDowloadedVideoObject(appCMSPresenter.getCurrentPlayingVideo());
                OfflineVideoData offlineVideoData = appCMSPresenter.deserialize(offlineVideo.request.data);
                ClosedCaptions cc = new ClosedCaptions();
                if ("SRT".equalsIgnoreCase(offlineVideoData.getSubtitlesFileFormat()) && !offlineVideoData.getSubtitlesFileURL().equalsIgnoreCase("file:///")) {
                    cc.setUrl(appCMSPresenter.downloadedMediaLocalURI(offlineVideoData.getCcFileEnqueueId()));
                    cc.setLanguage(offlineVideoData.getSubtitlesFileLanguage());
                    cc.setFormat(offlineVideoData.getSubtitlesFileFormat());
                    closedCaptionsList.add(cc);
                }
            }catch (Exception e){}
        }

        if (appCMSPresenter != null && appCMSPresenter.getRealmController()!=null && binder!=null && binder.getContentData()!=null && appCMSPresenter.getRealmController().isCCFileAvailableForOffLine(binder.getContentData().getGist().getId())) {
            for (DownloadClosedCaptionRealm dc : appCMSPresenter.getRealmController().getAllDownloadedCCFiles(binder.getContentData().getGist().getId())) {
                ClosedCaptions cc = Utils.convertDownloadClosedCaptionToClosedCaptions(dc);
                if ("SRT".equalsIgnoreCase(cc.getFormat())) {
                    cc.setUrl(appCMSPresenter.downloadedMediaLocalURI(dc.getCcFileEnqueueId()));
                    closedCaptionsList.add(cc);
                }
            }
        } else {
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
        }
        return closedCaptionsList;
    }

    @Override
    public String getSubtitleLanguageFromIndex(int index) {
        String language = null;
        List<ClosedCaptions> closedCaptionsList = new ArrayList<>();

        if (appCMSPresenter != null && appCMSPresenter.getRealmController().isCCFileAvailableForOffLine(binder.getContentData().getGist().getId())) {

            for (DownloadClosedCaptionRealm dc : appCMSPresenter.getRealmController().getAllDownloadedCCFiles(binder.getContentData().getGist().getId())) {
                ClosedCaptions cc = Utils.convertDownloadClosedCaptionToClosedCaptions(dc);
                if ("SRT".equalsIgnoreCase(cc.getFormat())) {
                    cc.setUrl(appCMSPresenter.downloadedMediaLocalURI(dc.getCcFileEnqueueId()));
                    closedCaptionsList.add(cc);
                }
            }

            if (!closedCaptionsList.isEmpty()) {
                language = closedCaptionsList.get(index).getLanguage();
            }

        } else {
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

                if (!closedCaptionsList.isEmpty()) {
                    language = closedCaptionsList.get(index).getLanguage();
                }
            }
        }
        return language;
    }

    @Override
    public void viewClick(View view, int height) {
        if (appCMSPresenter.getPlatformType() == AppCMSPresenter.PlatformType.TV) {
            showSeasonEpisodeViewOnPlayer(view, height);
        } else {
            if (view.isSelected()) {
                ((AppCompatImageButton) view).setColorFilter(appCMSPresenter.getBrandSecondaryCtaTextColor(), android.graphics.PorterDuff.Mode.SRC_IN);
                appCMSPlayVideoFragment.getRelatedVideoSection().setVisibility(View.GONE);
                view.setSelected(false);
                appCMSPlayVideoFragment.loadPrevNextImage();
            } else {
                ((AppCompatImageButton) view).setColorFilter(Color.parseColor(appCMSPresenter.getAppCtaBackgroundColor()), android.graphics.PorterDuff.Mode.SRC_IN);
                showSeasonEpisodeViewOnPlayerMobile(view, height);
                appCMSPlayVideoFragment.setPreviousNextVisibility(false);

            }
        }
    }

    View seasonEpisodeView;
    @BindView(R.id.seasonTitle)
    AppCompatTextView seasonTitle;
    @BindView(R.id.episodeTitle)
    AppCompatTextView episodeTitle;
    @BindView(R.id.seasonRecylerView)
    RecyclerView seasonRecylerView;
    @BindView(R.id.episodeRecylerView)
    RecyclerView episodeRecylerView;

    public PopupWindow getSeasonEpisodePopUpWindow() {
        return seasonEpisodePopUpWindow;
    }

    PopupWindow seasonEpisodePopUpWindow;

    public void showSeasonEpisodeViewOnPlayerMobile(View anchorView, int height) {
        LayoutInflater inflater = (LayoutInflater) anchorView.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (seasonEpisodeView == null)
            seasonEpisodeView = inflater.inflate(R.layout.player_seasons_episode_list, null);
        ButterKnife.bind(this, seasonEpisodeView);
        if (BaseView.isTablet(this))
            seasonEpisodeView.setLayoutParams(new ConstraintLayout.LayoutParams(BaseView.getDeviceWidth() / 2, BaseView.getDeviceHeight() - (height)));
        else
            seasonEpisodeView.setLayoutParams(new ConstraintLayout.LayoutParams(BaseView.getDeviceHeight() / 2, BaseView.getDeviceWidth() - (height + appCMSPlayVideoFragment.getPlayerInfoContainerHeight())));
        if (seasonEpisodeView != null && seasonEpisodeView.getParent() != null) {
            ((ViewGroup) seasonEpisodeView.getParent()).removeView(seasonEpisodeView);
        }
        appCMSPlayVideoFragment.getRelatedVideoSection().addView(seasonEpisodeView);
        episodeTitle.setText(localisedStrings.getEpisodesHeaderText());
        seasonTitle.setText(localisedStrings.getSeasonsLabelText());
        seasonRecylerView.setLayoutManager(new LinearLayoutManager(this,
                LinearLayoutManager.VERTICAL,
                false));
        episodeRecylerView.setLayoutManager(new LinearLayoutManager(this,
                LinearLayoutManager.VERTICAL,
                false));

        seasonRecylerView.setAdapter(new PlayerSeasonAdapter(moduleApi.getContentData().get(0).getSeason(), appCMSPresenter, currentPlayingSeasonPosition()));
        seasonRecylerView.smoothScrollToPosition(currentPlayingSeasonPosition());
        PlayerEpisodeAdapter playerEpisodeAdapter = new PlayerEpisodeAdapter(moduleApi.getContentData().get(0).getSeason().get(0).getEpisodes(), episodeRecylerView, appCMSPresenter);
        playerEpisodeAdapter.setVideoSelected(this);
        episodeRecylerView.setAdapter(playerEpisodeAdapter);
        SeasonTabSelectorBus.instanceOf().setTab(moduleApi.getContentData().get(0).getSeason().get(currentPlayingSeasonPosition()).getEpisodes());
        appCMSPlayVideoFragment.getRelatedVideoSection().setVisibility(View.VISIBLE);
        anchorView.setSelected(true);

    }

    public void showSeasonEpisodeViewOnPlayer(View anchorView, int height) {
        LayoutInflater inflater = (LayoutInflater) anchorView.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (seasonEpisodeView == null)
            seasonEpisodeView = inflater.inflate(R.layout.player_seasons_episode_list_tv, null);
        ButterKnife.bind(this, seasonEpisodeView);
        episodeTitle.setText(localisedStrings.getEpisodesHeaderText());
        seasonTitle.setText(localisedStrings.getSeasonsLabelText());
        seasonRecylerView.setLayoutManager(new LinearLayoutManager(this,
                LinearLayoutManager.VERTICAL,
                false));
        episodeRecylerView.setLayoutManager(new LinearLayoutManager(this,
                LinearLayoutManager.VERTICAL,
                false));
        if (seasonEpisodePopUpWindow == null) {
            seasonEpisodePopUpWindow = new PopupWindow(this);
            seasonEpisodePopUpWindow.setContentView(seasonEpisodeView);
            seasonEpisodePopUpWindow.setHeight(WindowManager.LayoutParams.WRAP_CONTENT);
            seasonEpisodePopUpWindow.setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
            if (BaseView.isTablet(this))
                seasonEpisodePopUpWindow.setHeight(BaseView.getDeviceHeight() - (height + appCMSPlayVideoFragment.getPlayerInfoContainerHeight()));
            else
                seasonEpisodePopUpWindow.setHeight(BaseView.getDeviceWidth() - (height + appCMSPlayVideoFragment.getPlayerInfoContainerHeight()));
            seasonEpisodeView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
            if (BaseView.isTablet(this))
                seasonEpisodePopUpWindow.setWidth(BaseView.getDeviceWidth() / 2);
            else
                seasonEpisodePopUpWindow.setWidth(BaseView.getDeviceHeight() / 2);

        }
        if (Build.VERSION.SDK_INT >= 21) {
            seasonEpisodePopUpWindow.setElevation(5.0f);
        }
        if (moduleApi != null) {
            seasonRecylerView.setAdapter(new PlayerSeasonAdapter(moduleApi.getContentData().get(0).getSeason(), appCMSPresenter, currentPlayingSeasonPosition()));
            PlayerEpisodeAdapter playerEpisodeAdapter = new PlayerEpisodeAdapter(moduleApi.getContentData().get(0).getSeason().get(0).getEpisodes(), episodeRecylerView, appCMSPresenter);
            playerEpisodeAdapter.setVideoSelected(this);
            episodeRecylerView.setAdapter(playerEpisodeAdapter);
            SeasonTabSelectorBus.instanceOf().setTab(moduleApi.getContentData().get(0).getSeason().get(currentPlayingSeasonPosition()).getEpisodes());
            seasonEpisodePopUpWindow.setOutsideTouchable(true);
            seasonEpisodePopUpWindow.setFocusable(true);
            seasonEpisodePopUpWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
                @Override
                public void onDismiss() {
                    ((AppCompatImageButton) anchorView).setColorFilter(appCMSPresenter.getBrandSecondaryCtaTextColor(), android.graphics.PorterDuff.Mode.SRC_IN);
                    appCMSPlayVideoFragment.setPreviousNextVisibility(true);
                    appCMSPlayVideoFragment.loadPrevNextImage();
                }
            });
            int[] viewLocation = new int[2];
            anchorView.getLocationOnScreen(viewLocation);
            if (!seasonEpisodePopUpWindow.isShowing()) {
                seasonEpisodePopUpWindow.showAtLocation(anchorView, Gravity.BOTTOM, viewLocation[0], height);
                appCMSPlayVideoFragment.setPreviousNextVisibility(false);
            }
        }
    }

    int currentPlayingSeasonPosition() {
        int position = 0;
        if (moduleApi != null && moduleApi.getContentData() != null
                && moduleApi.getContentData().size() > 0
                && moduleApi.getContentData().get(0).getSeason() != null) {
            for (int i = 0; i < moduleApi.getContentData().get(0).getSeason().size(); i++) {
                if (moduleApi.getContentData().get(0).getSeason().get(i).getEpisodes() != null) {
                    for (int j = 0; j < moduleApi.getContentData().get(0).getSeason().get(i).getEpisodes().size(); j++) {
                        String comaperId = moduleApi.getContentData().get(0).getSeason().get(i).getEpisodes().get(j).getGist() == null ?
                                moduleApi.getContentData().get(0).getSeason().get(i).getEpisodes().get(j).getId() :
                                moduleApi.getContentData().get(0).getSeason().get(i).getEpisodes().get(j).getGist().getId();
                        if (filmId.equalsIgnoreCase(comaperId))
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
        appCMSPresenter.setEpisodeTrailerID(null);
        appCMSPresenter.playNextVideo(appCMSPresenter.getDefaultAppCMSVideoPageBinder(contentDatum, positionPlayed, null, binder.isOffline(), binder.isTrailer(), binder.isPlayAds(),
                binder.getAdsUrl(), binder.getBgColor(), null),
                positionPlayed,
                0);
    }

    List<ContentDatum> allEpisodes = null;

    private void createAllEpisodeList() {
        boolean segment = false;
        int currentSeason = 0;
        int currentEpisode = 0;
        if (moduleApi != null && moduleApi.getContentData() != null && !moduleApi.getContentData().isEmpty() && moduleApi.getContentData().get(0).getSeason() != null) {
            allEpisodes = new ArrayList<>();
            for (int i = 0; i < moduleApi.getContentData().get(0).getSeason().size(); i++) {
                if (moduleApi.getContentData().get(0).getSeason().get(i).getEpisodes() != null) {
                    for (int j = 0; j < moduleApi.getContentData().get(0).getSeason().get(i).getEpisodes().size(); j++) {
                        ContentDatum episode = moduleApi.getContentData().get(0).getSeason().get(i).getEpisodes().get(j);
                        if (episode.getRelatedVideos() != null) {
                            for (int k = 0; k < episode.getRelatedVideos().size(); k++) {
                                if (episode.getRelatedVideos().get(k).getGist() != null && episode.getRelatedVideos().get(k).getGist().getId() != null && episode.getRelatedVideos().get(k).getGist().getId().equalsIgnoreCase(filmId)) {
                                    segment = true;
                                    currentSeason = i;
                                    currentEpisode = j;
                                    break;
                                }
                            }
                        }
                    }
                }
            }
            if (segment) {
                for (int i = 0; i < moduleApi.getContentData().get(0).getSeason().get(currentSeason).getEpisodes().get(currentEpisode).getRelatedVideos().size(); i++) {
                    allEpisodes.add(moduleApi.getContentData().get(0).getSeason().get(currentSeason).getEpisodes().get(currentEpisode).getRelatedVideos().get(i));
                }
            } else {
                for (int i = 0; i < moduleApi.getContentData().get(0).getSeason().size(); i++) {
                    if (moduleApi.getContentData().get(0).getSeason().get(i).getEpisodes() != null) {
                        for (int j = 0; j < moduleApi.getContentData().get(0).getSeason().get(i).getEpisodes().size(); j++) {
                            allEpisodes.add(moduleApi.getContentData().get(0).getSeason().get(i).getEpisodes().get(j));
                        }
                    }
                }
            }
        }

    }

    private int findCurrentPlayingPositionOfEpisode() {
        createAllEpisodeList();
        if (allEpisodes != null) {
            for (int i = 0; i < allEpisodes.size(); i++) {
                if (filmId.equalsIgnoreCase(allEpisodes.get(i).getGist().getId()))
                    return i;
            }
        }
        return currentlyPlayingIndex;
    }

}