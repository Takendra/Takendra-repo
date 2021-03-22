package com.viewlift.views.customviews;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.media.AudioManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.media.MediaMetadataCompat;
import android.support.v4.media.session.MediaSessionCompat;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.AppCompatToggleButton;
import androidx.appcompat.widget.LinearLayoutCompat;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.constraintlayout.widget.Guideline;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.audio.AudioAttributes;
import com.google.android.exoplayer2.ext.mediasession.MediaSessionConnector;
import com.google.android.exoplayer2.mediacodec.MediaCodecRenderer;
import com.google.android.exoplayer2.mediacodec.MediaCodecUtil;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.FixedTrackSelection;
import com.google.android.exoplayer2.trackselection.MappingTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.ui.AspectRatioFrameLayout;
import com.google.android.exoplayer2.ui.PlayerNotificationManager;
import com.viewlift.AppCMSApplication;
import com.viewlift.R;
import com.viewlift.Utils;
import com.viewlift.casting.CastHelper;
import com.viewlift.casting.CastServiceProvider;
import com.viewlift.casting.CastingUtils;
import com.viewlift.db.AppPreference;
import com.viewlift.models.billing.appcms.purchase.TvodPurchaseData;
import com.viewlift.models.data.appcms.api.AppCMSTransactionDataValue;
import com.viewlift.models.data.appcms.api.ClosedCaptions;
import com.viewlift.models.data.appcms.api.ContentDatum;
import com.viewlift.models.data.appcms.api.Gist;
import com.viewlift.models.data.appcms.api.Module;
import com.viewlift.models.data.appcms.api.Mpeg;
import com.viewlift.models.data.appcms.api.VideoAssets;
import com.viewlift.models.data.appcms.beacon.BeaconBuffer;
import com.viewlift.models.data.appcms.beacon.BeaconHandler;
import com.viewlift.models.data.appcms.beacon.BeaconPing;
import com.viewlift.models.data.appcms.beacon.BeaconRunnable;
import com.viewlift.models.data.appcms.downloads.DownloadClosedCaptionRealm;
import com.viewlift.models.data.appcms.ui.main.AppCMSMain;
import com.viewlift.models.data.appcms.ui.main.LocalisedStrings;
import com.viewlift.models.data.appcms.ui.page.Settings;
import com.viewlift.models.data.appcms.ui.page.Component;
import com.viewlift.presenters.AppCMSPresenter;
import com.viewlift.utils.CommonUtils;
import com.viewlift.utils.ShowDetailsPromoHandler;
import com.viewlift.utils.TrailerPlayBack;
import com.viewlift.utils.ContentTypeChecker;
import com.viewlift.views.activity.AppCMSPageActivity;
import com.viewlift.views.adapters.ClosedCaptionSelectorAdapter;
import com.viewlift.views.adapters.HLSStreamingQualitySelectorAdapter;
import com.viewlift.views.adapters.LanguageSelectorAdapter;
import com.viewlift.views.adapters.LiveModuleTabAdapter;
import com.viewlift.views.adapters.PlayerEpisodeAdapter;
import com.viewlift.views.adapters.PlayerNotificationAdapter;
import com.viewlift.views.adapters.PlayerSeasonAdapter;
import com.viewlift.views.adapters.StreamingQualitySelectorAdapter;
import com.viewlift.views.binders.AppCMSBinder;
import com.viewlift.views.binders.AppCMSVideoPageBinder;
import com.viewlift.views.customviews.constraintviews.TimerViewFutureContent;
import com.viewlift.views.dialog.AppCMSVerifyVideoPinDialog;
import com.viewlift.views.dialog.CustomShape;
import com.viewlift.views.listener.PlayerPlayPauseState;
import com.viewlift.views.listener.TrailerCompletedCallback;
import com.viewlift.views.listener.VideoSelected;
import com.viewlift.views.rxbus.SeasonTabSelectorBus;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;
import java.util.TreeMap;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Optional;
import rx.functions.Action1;

import static androidx.core.app.NotificationCompat.PRIORITY_LOW;
import static com.google.android.exoplayer2.Player.STATE_BUFFERING;
import static com.google.android.exoplayer2.Player.STATE_ENDED;
import static com.google.android.exoplayer2.Player.STATE_IDLE;
import static com.google.android.exoplayer2.Player.STATE_READY;

public class CustomVideoPlayerView extends VideoPlayerView implements VideoPlayerView.OnBeaconAdsEvent,
        VideoPlayerView.VideoPlayerSettingsEvent,
        VideoPlayerView.ClosedCaptionSelector,
        VideoPlayerView.StreamingQualitySelector,
        VideoPlayerView.SeasonEpisodeSelctionEvent, VideoSelected, PlayerSeasonAdapter.SeasonClickListener,
        TimerViewFutureContent.FutureEventCountdownListener {

    private static final String TAG = CustomVideoPlayerView.class.getSimpleName();
    private Context mContext;
    @Inject
    AppPreference appPreference;
    @Inject
    AppCMSPresenter appCMSPresenter;
    @Inject
    LocalisedStrings localisedStrings;

    private FrameLayout.LayoutParams baseLayoutParms;
    private LinearLayoutCompat customLoaderContainer;
    private AppCompatTextView loaderMessageView;
    private LinearLayoutCompat customMessageContainer;
    private LinearLayoutCompat customPreviewContainer;
    private LinearLayoutCompat previewBtnsLayout;
    private RelativeLayout parentView;
    private LinearLayoutCompat llTopBar;
    private LinearLayout relatedVideoLayout;

    private AppCompatTextView app_cms_video_player_title_view;

    private ViewCreator.VideoPlayerContent videoPlayerContent;

    public List<String> videoPlayListIds = new ArrayList<>();
    public boolean isFromPlayListPage = false;
    public boolean isPlayerAlreadyLoaded = false;

    private AppCMSVideoPageBinder binder;
    private AppCompatTextView customMessageView;
    private AppCompatTextView previewCustomMessageView;
    private LinearLayoutCompat customPlayBack;
    private String videoDataId = null;
    private String seriesId = null;
    private String videoTitle = null;
    public int currentPlayingIndex = 0;
    List<String> relatedVideoId;
    private AppCompatImageButton mSettingButton;
    private AppCompatToggleButton mFullScreenButton;
    private RelativeLayout muteNotifyView;
    private boolean isMuteNotifyVisible;
    private boolean shouldRequestAds = false;
    private boolean isADPlay;
    /* private ImaSdkFactory sdkFactory;
     private AdsLoader adsLoader;
     private AdsManager adsManager;*/
    private String adsUrl;
    private boolean isAdDisplayed, isAdError;
    private boolean isAdsDisplaying;
    private AppCompatButton btnLogin;
    private AppCompatButton btnStartFreeTrial;
    private boolean isLiveStream;

    private static int apod = 0;


    private long watchedPercentage = 0;
    private long watchedPercentageVideoPage = 0;
    private final String FIREBASE_STREAM_START = "stream_start";
    private final String FIREBASE_STREAM_25 = "stream_25_pct";
    private final String FIREBASE_STREAM_50 = "stream_50_pct";
    private final String FIREBASE_STREAM_75 = "stream_75_pct";
    private final String FIREBASE_STREAM_100 = "stream_100_pct";

    private final String FIREBASE_VIDEO_ID_KEY = "video_id";
    private final String FIREBASE_VIDEO_NAME_KEY = "video_name";
    private final String FIREBASE_SERIES_ID_KEY = "series_id";
    private final String FIREBASE_SERIES_NAME_KEY = "series_name";
    private final String FIREBASE_PLAYER_NAME_KEY = "player_name";
    private final String FIREBASE_MEDIA_TYPE_KEY = "media_type";
    private final String FIREBASE_PLAYER_NATIVE = "Native";
    private final String FIREBASE_PLAYER_CHROMECAST = "Chromecast";
    private final String FIREBASE_MEDIA_TYPE_VIDEO = "Video";
    private final String FIREBASE_SCREEN_VIEW_EVENT = "screen_view";
    Handler mProgressHandler;
    Runnable mProgressRunnable;
    long mTotalVideoDuration;
    boolean isStreamStart, isStream25, isStream50, isStream75, isStream100;
    boolean lastPlayState = false;

    private BeaconHandler mBeaconHandler;
    private BeaconRunnable mBeaconRunnable;
    private BeaconBuffer beaconBufferingThread;
    private long beaconBufferingTimeoutMsec;
    private boolean sentBeaconPlay;
    private boolean sentBeaconFirstFrame;
    private BeaconPing beaconMessageThread;
    private long beaconMsgTimeoutMsec;
    private String mStreamId;
    private String permaLink;
    private String parentScreenName;
    boolean isVideoDownloaded;
    boolean isTrailer;
    private long mStartBufferMilliSec = 0l;
    private long mStopBufferMilliSec;
    private static double ttfirstframe = 0d;
    private long watchedTime = 0l;
    private static final long SECS_TO_MSECS = 1000L;
    private long videoPlayTime = 0l;
    private boolean isVideoLoaded = false;
    private CustomVideoPlayerView videoPlayerViewSingle;

    TimerViewFutureContent timerViewFutureContent;
    PlayerSettingsView playerSettingsView;
    private Map<String, String> availableStreamingQualityMap;
    private List<String> availableStreamingQualities;

    private boolean isVideoPlaying = true;
    private boolean isTimerRun = true;
    public String lastUrl = "", closedCaptionUri = "";
    ContentDatum onUpdatedContentDatum;
    public boolean isPreviewShown = false;

    protected int currentTrackIndex = 0;

    private AppCompatToggleButton mToggleButton;
    private AppCompatToggleButton mZoomButton;
    public boolean hideMiniPlayer = false;
    public boolean isPinDialogShown = false;
    private double entitlementCheckMultiplier = 0;
    private boolean isMaxStreamError = false;
    private boolean episodePlay = false;
    private boolean videoPaused = false;
    List<ContentDatum> allEpisodes = null;
    private Settings settings;

    public CustomVideoPlayerView(Context context, AppCMSPresenter appCMSPresenter, Settings settings, Module moduleApiData) {
        super(context, appCMSPresenter);

        Log.d("VIDEOPLAYLIST", "INITIAL");
        mContext = context;
        this.settings = settings;
        ((AppCMSApplication) mContext.getApplicationContext()).getAppCMSPresenterComponent().inject(this);
        //  createNotification();
        onDestroyNotification();
        createLoader();
        if (contentTypeChecker == null)
            contentTypeChecker = new ContentTypeChecker(context);
        mFullScreenButton = createFullScreenToggleButton();
        mZoomButton = createZoomToogleButton();



        if (!appCMSPresenter.getAppCMSMain().isMonetizationModelEnabled())
            createPreviewMessageView();
        else
            createPreviewMessageViewNew();
        touchToCastOverlay();
        createTopBarView();
        if (settings != null && moduleApiData != null && settings.isShowTabs())
            showLiveModuleTab(moduleApiData);
        initiateStreamingId();

        initializeSettingButton();

        //initializeFutureContentTimerView();

        videoPlayerContent = new ViewCreator.VideoPlayerContent();

        setFirebaseProgressHandling();
        hideRelatedVideoView(false);
        videoPlayerViewSingle = this;
        videoPlayerViewSingle.setSeasonEpisodeSelctionEvent(this);

        parentScreenName = getContext().getString(R.string.app_cms_beacon_video_player_parent_screen_name);

        beaconMsgTimeoutMsec = getResources().getInteger(R.integer.app_cms_beacon_timeout_msec);
        beaconBufferingTimeoutMsec = getResources().getInteger(R.integer.app_cms_beacon_buffering_timeout_msec);


        setOnBeaconAdsEvent(this);
        beaconMessageThread = new BeaconPing(beaconMsgTimeoutMsec,
                appCMSPresenter,
                videoDataId,
                permaLink,
                isTrailer,
                parentScreenName,
                this,
                mStreamId, onUpdatedContentDatum);

        beaconBufferingThread = new BeaconBuffer(beaconBufferingTimeoutMsec,
                appCMSPresenter,
                videoDataId,
                permaLink,
                parentScreenName,
                this,
                mStreamId, onUpdatedContentDatum);

        mBeaconHandler = new BeaconHandler(this.getPlayer().getApplicationLooper());
        mBeaconRunnable = new BeaconRunnable(beaconMessageThread,
                beaconBufferingThread,
                mBeaconHandler,
                this);
        mBeaconHandler.handle(mBeaconRunnable);

    }

    private AudioManager audioManager;

    public void setVideoId(String videoId) {
        this.videoDataId = videoId;
    }

    public String getVideoId() {
        return videoDataId;
    }

    public void setOnUpdatedContentDatum(ContentDatum contentDatum) {
        onUpdatedContentDatum = contentDatum;
    }

    public void setVideoUri(String videoId, String msg, boolean isTrailer, boolean isPageTrailer, ContentDatum videoContentData) {

        showOverlayWhenCastingConnected();
        hideRestrictedMessage();
        showProgressBar(msg);
        releasePlayer();
        videoDataId = videoId;
        this.isTrailer = isTrailer;
        beaconMessageThread.setTrailer(isTrailer);
        init(mContext);
        isVideoDownloaded = appCMSPresenter.isVideoDownloaded(videoDataId);
        appCMSPresenter.setCustomPlayerVideoView(true);
        videoContentData.setFromStandalone(true);
        createAllEpisodeList();
        if (mToggleButton.isChecked())
            hideRelatedVideoView(true);
        else
            hideRelatedVideoView(false);
        appCMSPresenter.refreshVideoData(videoId, contentDatum -> {
            {
                if (contentDatum != null) {
                    setVideoContentData(contentDatum);
                    try {
                        {
                            if (contentDatum.getAppCMSSignedURLResult() != null) {
                                updateSignatureCookies(contentDatum.getAppCMSSignedURLResult().getPolicy(),
                                        contentDatum.getAppCMSSignedURLResult().getSignature(),
                                        contentDatum.getAppCMSSignedURLResult().getKeyPairId());
                                setPolicyCookie(contentDatum.getAppCMSSignedURLResult().getPolicy());
                                setSignatureCookie(contentDatum.getAppCMSSignedURLResult().getSignature());
                                setKeyPairIdCookie(contentDatum.getAppCMSSignedURLResult().getKeyPairId());
                            }
                            onUpdatedContentDatum = contentDatum;
                            if (onUpdatedContentDatum != null
                                    && onUpdatedContentDatum.getGist() != null
                                    && onUpdatedContentDatum.getGist().getSeriesId() != null
                                    && !TextUtils.isEmpty(onUpdatedContentDatum.getGist().getSeriesId())) {
                                seriesId = onUpdatedContentDatum.getGist().getSeriesId();
                            }
                            if (beaconBufferingThread != null) {
                                beaconBufferingThread.setContentDatum(onUpdatedContentDatum);
                            }
                            if (beaconMessageThread != null) {
                                beaconMessageThread.setContentDatum(onUpdatedContentDatum);
                            }
                            initializeStreamingQualityValues(contentDatum.getStreamingInfo().getVideoAssets());

                            getPermalink(contentDatum);
                            if (appCMSPresenter.isUserLoggedIn()) {
                                setWatchedTime(appCMSPresenter.getUserHistoryContentDatum(contentDatum.getGist().getId()));
                            }
                            if (contentDatum != null &&
                                    contentDatum.isDRMEnabled()) {
                                setDRMEnabled(contentDatum.isDRMEnabled());
                                setLicenseUrl(contentDatum.getStreamingInfo().getVideoAssets().getWideVine().getLicenseUrl());
                                setLicenseTokenDRM(contentDatum.getStreamingInfo().getVideoAssets().getWideVine().getLicenseToken());
                                releasePlayer();
                                init(mContext);
                            }
                            boolean freePreview = appCMSPresenter.getAppCMSMain().getFeatures() != null &&
                                    appCMSPresenter.getAppCMSMain().getFeatures().getFreePreview() != null &&
                                    appCMSPresenter.getAppCMSMain().getFeatures().getFreePreview().isFreePreview();
                            if (appCMSPresenter.getAppCMSMain().isForceLogin() && !appCMSPresenter.isUserLoggedIn() && contentDatum.getGist().getFree() && !freePreview) {
                                showPreviewFrame(contentDatum);
                            } else if (!contentDatum.getGist().getFree()) {
                                boolean isTveContent = onUpdatedContentDatum.getSubscriptionPlans() != null && appPreference.getTVEUserId() != null
                                        && contentTypeChecker.isContentTVE(onUpdatedContentDatum.getSubscriptionPlans());
                                boolean isContentPurchased = appCMSPresenter.isUserLoggedIn() && (appCMSPresenter.getAppPreference().getUserPurchases() != null && !TextUtils.isEmpty(appCMSPresenter.getAppPreference().getUserPurchases())
                                        && (contentTypeChecker.isContentPurchased(appCMSPresenter.getAppPreference().getUserPurchases(), videoDataId)
                                        || (onUpdatedContentDatum.getSeasonId() != null && contentTypeChecker.isContentPurchased(appCMSPresenter.getAppPreference().getUserPurchases(), onUpdatedContentDatum.getSeasonId()))
                                        || (onUpdatedContentDatum.getSeriesId() != null && contentTypeChecker.isContentPurchased(appCMSPresenter.getAppPreference().getUserPurchases(), onUpdatedContentDatum.getSeriesId()))));
                                //check login and subscription first.
                                if (CommonUtils.isPPVContent(mContext, onUpdatedContentDatum)) {
                                    checkPPVPlayState();
                                } else if (!appCMSPresenter.isUserLoggedIn() && !appPreference.getPreviewStatus()) {
                                    getVideoPreview();

                                    if (entitlementCheckMultiplier > 0) {
                                        /*if (shouldRequestAds) {
//                                             requestAds(adsUrl);
                                        } else {*/
                                        playVideos(0, contentDatum);
//                                        }
                                    }
                                    appPreference.setPreviewStatus(false);
                                    if (!appCMSPresenter.isAppSVOD()) {
                                        if (isPageTrailer && !appCMSPresenter.getDefaultTrailerPlay())
                                            return;
                                        playVideos(0, contentDatum);
                                    }
                                } else if (appCMSPresenter.isUserLoggedIn()
                                        && CommonUtils.isPPVContent(mContext, onUpdatedContentDatum)) {
                                    checkPPVPlayState();
                                } else {
                                    if (appCMSPresenter.isUserSubscribed() || isTveContent || isContentPurchased) {
                                        playVideos(0, contentDatum);
                                        appPreference.setPreviewStatus(false);
                                    } else {
                                        getVideoPreview();
                                        if (!appPreference.getPreviewStatus()) {
                                            playVideos(0, contentDatum);
                                            // requestAds(adsUrl);
                                        } else {
                                            if (entitlementCheckMultiplier > 0) {
                                                playVideos(0, contentDatum);
                                            }
                                        }
                                    }
                                }
                            } else {
                                /*if (shouldRequestAds) {
                                   // requestAds(adsUrl);
                                } else*/
                                if (appCMSPresenter.getAppCMSMain().isForceLogin() && !appCMSPresenter.isUserLoggedIn() && freePreview) {
                                    getVideoPreview();
                                }

                                playVideos(0, contentDatum);
                            }
                            onDestroyNotification();
                            createNotification();
                            setNotification();
                            setTopBarStatus();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        //
//                        appCMSPresenter.showDialog(AppCMSPresenter.DialogType.VIDEO_NOT_AVAILABLE,
//                                getString(R.string.app_cms_video_not_available_error_message),
//                                false,
//                                this::finish,
//                                null);
                    }
                    videoTitle = contentDatum.getGist().getTitle();


                }
            }
        }, null, false, false, videoContentData);
        videoDataId = videoId;
        sentBeaconPlay = false;
        sentBeaconFirstFrame = false;
        streamingQualitySelectorCreated = false;
        audioSelectorCreated = false;
        audioSelectorShouldCreated = true;
        closedCaptionSelectorCreated = false;

        if (playerSettingsView != null && playerSettingsView.getSimpleItemRecyclerViewAdapter() != null) {
            playerSettingsView.getSimpleItemRecyclerViewAdapter().setSELECTED_ITEM_POSITION(0);
        }
        languageSelectorAdapter = null;
        closedCaptionSelectorAdapter = null;
        hlsListViewAdapter = null;
        listViewAdapter = null;


    }

    private void setTopBarStatus() {
        setOnPlayerControlsStateChanged(visibility -> {
            if (visibility == View.GONE) {
                if (tabSelectedScreen != null && settings != null && settings.isShowTabs()) {
                    tabSelectedScreen.setVisibility(GONE);
                }
                llTopBar.setVisibility(View.GONE);
            } else if (visibility == View.VISIBLE && mToggleButton != null &&
                    mToggleButton.isChecked() && (settings == null || (settings != null && !settings.isShowTabs()))) {
                llTopBar.setVisibility(View.VISIBLE);
            } else if (visibility == View.VISIBLE && tabSelectedScreen != null && settings != null && settings.isShowTabs()) {
                tabSelectedScreen.setVisibility(VISIBLE);
            }
        });
        if (onUpdatedContentDatum != null &&
                onUpdatedContentDatum.getGist() != null &&
                onUpdatedContentDatum.getGist().getTitle() != null &&
                app_cms_video_player_title_view != null) {
            app_cms_video_player_title_view.setText(onUpdatedContentDatum.getGist().getTitle());
        }

    }

    public void checkVideoStatus(String videoId, ContentDatum contentDatum) {
        if (videoContentDatum == null)
            videoContentDatum = contentDatum;
        setTopBarStatus();

        setVideoPlayerStatus();
        if (appCMSPresenter != null && appCMSPresenter.isCastEnable()) {
            mediaButton.setVisibility(VISIBLE);
            CastServiceProvider.getInstance(mContext).setVideoPlayerMediaButton(mediaButton);
            CastServiceProvider.getInstance(mContext).onActivityResume();
            if (CommonUtils.isPPVContent(mContext, onUpdatedContentDatum)) {
                CastServiceProvider.getInstance(mContext).setPPVContent(true);
            }

        }
        if (appCMSPresenter.getAppCMSMain().isMonetizationModelEnabled())
            setVideoUri(videoId, localisedStrings.getLoadingVideoText(), isTrailer, isTrailer, videoContentDatum);

        boolean isFreeContent = CommonUtils.isFreeContent(mContext, onUpdatedContentDatum);

        boolean isFreePreview = appCMSPresenter.getAppCMSMain().getFeatures() != null &&
                appCMSPresenter.getAppCMSMain().getFeatures().getFreePreview() != null &&
                appCMSPresenter.getAppCMSMain().getFeatures().getFreePreview().isFreePreview();
        boolean isTveContent = onUpdatedContentDatum != null && onUpdatedContentDatum.getSubscriptionPlans() != null && appPreference.getTVEUserId() != null
                && contentTypeChecker.isContentTVE(onUpdatedContentDatum.getSubscriptionPlans());
        boolean isContentPurchased = appCMSPresenter.isUserLoggedIn() && appPreference.getUserPurchases() != null && !TextUtils.isEmpty(appPreference.getUserPurchases()) && contentTypeChecker.isContentPurchased(appPreference.getUserPurchases(), videoId);
        if (CommonUtils.isPPVContent(mContext, onUpdatedContentDatum)) {
            checkPPVPlayState();
            return;
        } /*else if(appCMSPresenter.getAppCMSMain().isForceLogin() && !appCMSPresenter.isUserLoggedIn()){
            showPreviewFrame();
        }*/ else if (appCMSPresenter.getAppCMSMain().isForceLogin() && !appCMSPresenter.isUserLoggedIn()) {
            if (isFreePreview) {
                if (isPreviewShown) {
                    pausePlayer();
                    appCMSPresenter.dismissPopupWindowPlayer(true);
                }
                getVideoPreview();
            } else {
                showPreviewFrame(onUpdatedContentDatum);
            }
            return;
        } else if (isFreeContent || isTveContent || isContentPurchased || appCMSPresenter.isUserSubscribed()) {
            hidePreviewFrame();
            if (!isTrailer)
                setUseController(true);
            if (getPlayer().getPlaybackState() == STATE_IDLE) {
                setVideoUri(onUpdatedContentDatum.getGist().getId(), localisedStrings.getLoadingVideoText(), isTrailer, isTrailer, onUpdatedContentDatum);
            } else if (getPlayer().getPlaybackState() != STATE_BUFFERING && getPlayer().getPlaybackState() != STATE_READY) {
                resumePlayer();
            }
            if (beaconMessageThread != null) {
                beaconMessageThread.sendBeaconPing = true;
            }
            if (beaconBufferingThread != null && appCMSPresenter.getAppCMSMain().getFeatures() != null && appCMSPresenter.getAppCMSMain().getFeatures().isEnableQOS()) {
                beaconBufferingThread.sendBeaconBuffering = true;
            }
            return;
        }/*else if(!appCMSPresenter.getAppCMSMain().isForceLogin() && appCMSPresenter.isUserLoggedIn()
                && isFreeContent){
            hidePreviewFrame();
        }*/ else if (!appCMSPresenter.isUserSubscribed() && isPreviewShown) {
            pausePlayer();
            showPreviewFrame(onUpdatedContentDatum);
            return;
        } else if (onUpdatedContentDatum != null && !onUpdatedContentDatum.getGist().getFree()) {
            //check login and subscription first.
            if (!appCMSPresenter.isUserLoggedIn() && !appPreference.getPreviewStatus()) {
                getVideoPreview();
            } else {
                if ((appCMSPresenter.isAppSVOD() &&
                        !appCMSPresenter.isUserSubscribed()) && !isTveContent) {
                    getVideoPreview();
                } else {
                    hidePreviewFrame();
                    if (getPlayer().getPlaybackState() == STATE_IDLE) {
                        setVideoUri(onUpdatedContentDatum.getGist().getId(), localisedStrings.getLoadingVideoText(), false, false, onUpdatedContentDatum);
                    }
                }
            }
        } else {
            hidePreviewFrame();
        }
    }

    private int currentIndex(String videoId) {
        if (relatedVideoId != null && relatedVideoId.size() < currentPlayingIndex)
            for (int i = 0; i < relatedVideoId.size(); i++) {
                if (videoId.equalsIgnoreCase(relatedVideoId.get(i))) {
                    return i;
                }
            }
        return 0;
    }

    private void playVideos(int currentIndex, ContentDatum contentDatum) {
        customPreviewContainer.setVisibility(View.GONE);
        hideRestrictedMessage();

        if (null != customPlayBack)
            customPlayBack.setVisibility(View.VISIBLE);
        String url = null;
        String closedCaptionUrl = null;
        boolean useHls = Utils.isHLS();

        permaLink = contentDatum.getGist().getPermalink();
        if (null != contentDatum && null != contentDatum.getStreamingInfo() && null != contentDatum.getStreamingInfo().getVideoAssets()) {
            if (null != contentDatum.getStreamingInfo().getVideoAssets().getHls() && useHls) {
                url = contentDatum.getStreamingInfo().getVideoAssets().getHls();
            } else if (null != contentDatum.getStreamingInfo().getVideoAssets().getHls() && contentDatum.getGist() != null
                    && contentDatum.getGist().isLiveStream()) {
                url = contentDatum.getStreamingInfo().getVideoAssets().getHls();
            } else if (null != contentDatum.getStreamingInfo().getVideoAssets().getMpeg()
                    && contentDatum.getStreamingInfo().getVideoAssets().getMpeg().size() > 0) {
                String quality = appCMSPresenter.getAppPreference().getVideoStreamingQuality();
                if (quality != null && quality.length() > 0) {
                    for (Mpeg mpeg : contentDatum.getStreamingInfo().getVideoAssets().getMpeg()) {
                        if (mpeg.getRenditionValue().contains(quality)) {
                            url = mpeg.getUrl();
                        }
                    }
                } else {
                    url = contentDatum.getStreamingInfo().getVideoAssets().getMpeg().get(0).getUrl();
                }
            }
            if (contentDatum.getContentDetails() != null
                    && contentDatum.getContentDetails().getClosedCaptions() != null
                    && !contentDatum.getContentDetails().getClosedCaptions().isEmpty()) {
                for (ClosedCaptions cc : contentDatum.getContentDetails().getClosedCaptions()) {
                    if (cc.getUrl() != null &&
                            !cc.getUrl().equalsIgnoreCase(getContext().getString(R.string.download_file_prefix)) &&
                            cc.getFormat() != null &&
                            "SRT".equalsIgnoreCase(cc.getFormat())) {
                        closedCaptionUrl = cc.getUrl();
                    }
                }
            }
            if (playerView != null && playerView.getController() != null) {
                playerView.getController().setPlayingLive(isLiveStream);
            }
        }

        playerView.getController().setPlayerPlayPauseState(new PlayerPlayPauseState() {
            @Override
            public void playerState(boolean isVideoPaused) {
                videoPaused = isVideoPaused;
                if (appCMSPresenter.isFullScreenVisible && !isLiveStream) {
                    createPlayerLefoutScreen();
                    if (isVideoPaused) {
                        createNextVideoContainer();
                        loadPrevNextImage();
                    }
                }
                if (!isVideoPaused) {
                    setPreviousNextVisibility(false);
                    if (playerLeftOutScreen != null)
                        playerLeftOutScreen.removeView(nextPreviousVideoContainer);
                }
            }
        });

        playerView.getController().setPlayerEvents(isVideoPaused -> {
            /**
             * removed check of !getDefaultTrailerPlay()  commented bellow is because https://viewlift.freshdesk.com/a/tickets/30768https://viewlift.freshdesk.com/a/tickets/30768https://viewlift.freshdesk.com/a/tickets/30768
             *  // getDefaultTrailerPlay() must handled by some other way
             */
            if (isVideoPaused) {
                isVideoPlaying = false;
                //   if (isVideoPaused && appCMSPresenter.getAppPreference().getMiniPLayerVisibility()) {
                appCMSPresenter.setIsMiniPlayerPlaying(false);
            } else {
                isVideoPlaying = !isVideoPaused;
                appCMSPresenter.setIsMiniPlayerPlaying(true);
            }

        });
        if (null != url) {
            lastUrl = url;
            closedCaptionUri = closedCaptionUrl;
            setBeaconData();
            if (!useAdUrl) {
                adsUrl = null;
            }
            setAdsUrl(adsUrl);
            setUri(Uri.parse(url), closedCaptionUrl == null ? null : Uri.parse(closedCaptionUrl));
            setCurrentPosition(watchedTime * SECS_TO_MSECS);
            resumePlayer();

            if (currentIndex == 0) {
                relatedVideoId = contentDatum.getContentDetails().getRelatedVideoIds();
            }
            currentPlayingIndex = currentIndex(contentDatum.getGist().getId());

            hideProgressBar();
            if (!isMuteNotifyVisible
                    && appCMSPresenter != null
                    && appCMSPresenter.getAppCMSMain() != null
                    && appCMSPresenter.getAppCMSMain().getFeatures() != null
                    && appCMSPresenter.getAppCMSMain().getFeatures().isMuteSound()) {
                muteNotifyView = createMuteNotifyView();
                getPlayerView().addView(muteNotifyView);
                audioManager = (AudioManager) appCMSPresenter.getCurrentActivity().getSystemService(Context.AUDIO_SERVICE);
                setPlayerVolumeImage(playerVolumeButton, true);
            } else if (appCMSPresenter.getTemplateType() == AppCMSPresenter.TemplateType.NEWS) {
                createMuteView();
            }

           /* if (contentDatum != null &&
                    contentDatum.getGist() != null &&
                    contentDatum.getGist().getWatchedTime() != 0) {
                watchedTime = contentDatum.getGist().getWatchedTime();
            }*/

            long duration = contentDatum.getGist().getRuntime();
            if (duration <= watchedTime) {
                watchedTime = 0L;
            }
            setCurrentPosition(watchedTime * SECS_TO_MSECS);
            videoTitle = contentDatum.getGist().getTitle();

            videoPlayerContent.videoUrl = lastUrl;
            videoPlayerContent.ccUrl = closedCaptionUri;
            videoPlayerContent.videoPlayTime = getCurrentPosition() / SECS_TO_MSECS;
        }
        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            if (!BaseView.isTablet(appCMSPresenter.getCurrentContext())) {
                appCMSPresenter.getCurrentActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);
            }
        }, 2000);
    }

    public Timer entitlementCheckTimer;
    public TimerTask entitlementCheckTimerTask;
    private boolean entitlementCheckCancelled = false;
    int maxPreviewSecs = 0;
    int playedVideoSecs = 0;
    int secsViewed = 0;

    public void getVideoPreview() {

        if (entitlementCheckTimer != null) {
            entitlementCheckTimer.cancel();
            entitlementCheckTimer = null;
        }
        if (appCMSPresenter.isAppSVOD() &&
                !appCMSPresenter.isUserSubscribed()) {
            entitlementCheckCancelled = false;

            AppCMSMain appCMSMain = appCMSPresenter.getAppCMSMain();
            if (appCMSMain != null &&
                    appCMSMain.getFeatures() != null &&
                    appCMSMain.getFeatures().getFreePreview() != null &&
                    appCMSMain.getFeatures().getFreePreview().isFreePreview()) {
                if (appCMSMain.getFeatures().getFreePreview().getLength() != null &&
                        appCMSMain.getFeatures().getFreePreview().getLength().getUnit().equalsIgnoreCase("Minutes")) {
                    try {
                        entitlementCheckMultiplier = Double.parseDouble(appCMSMain.getFeatures().getFreePreview().getLength().getMultiplier());
                    } catch (Exception e) {
                        e.printStackTrace();
                        Log.e(TAG, "Error parsing free preview multiplier value: " + e.getMessage());
                    }
                }
            }

            maxPreviewSecs = (int) (entitlementCheckMultiplier * 60);

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (appCMSPresenter != null &&
                            appCMSPresenter.getCurrentActivity() != null) {
                        appCMSPresenter.getCurrentActivity().runOnUiThread(() -> appCMSPresenter.dismissPopupWindowPlayer(true));
                    }
                }
            }, maxPreviewSecs * 1000);

            entitlementCheckTimerTask = new TimerTask() {
                @Override
                public void run() {
                    checkEntitlement(this, appCMSMain);
                }
            };

            entitlementCheckTimer = new Timer();
            entitlementCheckTimer.schedule(entitlementCheckTimerTask, 1000, 1000);
        } else {
            appPreference.setPreviewStatus(false);
            hidePreviewFrame();
        }

    }

    private void checkEntitlement(TimerTask timertask, AppCMSMain appCMSMain) {
        if (!entitlementCheckCancelled && isTimerRun) {
            if (!isLiveStream && appCMSMain.getFeatures().getFreePreview().isPerVideo()) {
                secsViewed = (int) getPlayer().getCurrentPosition() / 1000;
            }
            if ((isLiveStream && appCMSMain.getFeatures().getFreePreview().isPerVideo()) || !appCMSMain.getFeatures().getFreePreview().isPerVideo()) {
                playedVideoSecs = appPreference.getPreviewTimerValue();
            }
            boolean isTVEContent = onUpdatedContentDatum.getSubscriptionPlans() != null && appPreference.getTVEUserId() != null && contentTypeChecker.isContentTVE(onUpdatedContentDatum.getSubscriptionPlans());
            if (((maxPreviewSecs < playedVideoSecs) || (maxPreviewSecs < secsViewed)) && !appCMSPresenter.isUserSubscribed() && isTVEContent) {
                //pausePlayer();
                //if mini player is showing than dismiss the mini player
                if (appCMSPresenter != null &&
                        appCMSPresenter.getCurrentActivity() != null) {
                    appCMSPresenter.getCurrentActivity().runOnUiThread(() -> appCMSPresenter.dismissPopupWindowPlayer(true));
                }

                if (onUpdatedContentDatum != null) {
                    List<String> relatedVideos = onUpdatedContentDatum.getContentDetails() != null ? onUpdatedContentDatum.getContentDetails().getRelatedVideoIds() : null;
                    onUpdatedContentDatum.setFromStandalone(true);
                    AppCMSPresenter.EntitlementPendingVideoData entitlementPendingVideoData
                            = new AppCMSPresenter.EntitlementPendingVideoData.Builder()
                            //.action(getContext().getString(R.string.app_cms_page_play_key))
                            .closerLauncher(false)
                            .contentDatum(onUpdatedContentDatum)
                            .currentlyPlayingIndex(0)
                            .pagePath(onUpdatedContentDatum.getGist().getPermalink())
                            .filmTitle(onUpdatedContentDatum.getGist().getTitle())
                            .extraData(null)
                            .relatedVideoIds(relatedVideos)
                            .currentWatchedTime(getPlayer().getCurrentPosition() / 1000)
                            .build();
                    appCMSPresenter.setEntitlementPendingVideoData(entitlementPendingVideoData);
                }
                appPreference.setPreviewStatus(true);

                hideMiniPlayer = true;
                ((Activity) mContext).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        showPreviewFrame(onUpdatedContentDatum);
                    }
                });

                timertask.cancel();
                entitlementCheckCancelled = true;
                return;

            } else {
                hideMiniPlayer = false;

            }
            playedVideoSecs++;
            appPreference.setPreviewTimerValue(playedVideoSecs);

        }
    }

    private void handleTvodBuyRent(ContentDatum videoContentDatum) {
        if (contentTypeChecker.purchaseEnabledTvod(videoContentDatum.getSubscriptionPlans()) && contentTypeChecker.rentEnabledTvod(videoContentDatum.getSubscriptionPlans())) {
            entitlementButton1.setVisibility(VISIBLE);
            entitlementButton2.setVisibility(VISIBLE);
            entitlementButton1.setText(localisedStrings.getRentLabel() + " " + contentTypeChecker.fetchTvodRentPrice(Objects.requireNonNull(contentTypeChecker.tvodPlan(videoContentDatum.getSubscriptionPlans()))));
            entitlementButton2.setText(localisedStrings.getBuyLabel() + " " + contentTypeChecker.fetchTvodBuyPrice(Objects.requireNonNull(contentTypeChecker.tvodPlan(videoContentDatum.getSubscriptionPlans()))));
            buttonPositions(tvodButtons, entitlementButton1, entitlementButton2, verticalGuidelineTvod);
        } else if (!contentTypeChecker.purchaseEnabledTvod(videoContentDatum.getSubscriptionPlans()) && contentTypeChecker.rentEnabledTvod(videoContentDatum.getSubscriptionPlans())) {
            entitlementButton2.setVisibility(GONE);
            entitlementButton1.setVisibility(VISIBLE);
            entitlementButton1.setText(localisedStrings.getRentLabel() + " " + contentTypeChecker.fetchTvodRentPrice(Objects.requireNonNull(contentTypeChecker.tvodPlan(videoContentDatum.getSubscriptionPlans()))));
            expandButton(entitlementButton1, tvodButtons);
        } else if (contentTypeChecker.purchaseEnabledTvod(videoContentDatum.getSubscriptionPlans()) && !contentTypeChecker.rentEnabledTvod(videoContentDatum.getSubscriptionPlans())) {
            entitlementButton1.setVisibility(GONE);
            entitlementButton2.setVisibility(VISIBLE);
            entitlementButton2.setText(localisedStrings.getBuyLabel() + " " + contentTypeChecker.fetchTvodBuyPrice(Objects.requireNonNull(contentTypeChecker.tvodPlan(videoContentDatum.getSubscriptionPlans()))));
            expandButton(entitlementButton2, tvodButtons);
        }
    }

    private void buttonPositions(ConstraintLayout layout, AppCompatButton buttonLeft, AppCompatButton buttonRight, Guideline guideline) {
        ConstraintSet constraintSet = new ConstraintSet();
        constraintSet.clone(layout);
        constraintSet.connect(buttonLeft.getId(), ConstraintSet.START, ConstraintSet.PARENT_ID, ConstraintSet.START, 0);
        constraintSet.connect(buttonLeft.getId(), ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.TOP, 0);
        constraintSet.connect(buttonLeft.getId(), ConstraintSet.END, guideline.getId(), ConstraintSet.END, 10);

        constraintSet.connect(buttonRight.getId(), ConstraintSet.END, ConstraintSet.PARENT_ID, ConstraintSet.END, 0);
        constraintSet.connect(buttonRight.getId(), ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.TOP, 0);
        constraintSet.connect(buttonRight.getId(), ConstraintSet.START, guideline.getId(), ConstraintSet.END, 10);

        constraintSet.applyTo(layout);
    }

    private void expandButton(AppCompatButton button, ConstraintLayout layout) {
        ConstraintSet constraintSet = new ConstraintSet();
        constraintSet.clone(layout);
        constraintSet.connect(button.getId(), ConstraintSet.START, ConstraintSet.PARENT_ID, ConstraintSet.START, 0);
        constraintSet.connect(button.getId(), ConstraintSet.END, ConstraintSet.PARENT_ID, ConstraintSet.END, 0);
        constraintSet.connect(button.getId(), ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.TOP, 0);
        constraintSet.applyTo(layout);
    }

    ContentTypeChecker contentTypeChecker;
    ContentDatum videoContentDatum;

    public void showPreviewFrame(ContentDatum videoContentDatum) {
        disableController();
        isPreviewShown = true;
        appPreference.setPreviewStatus(true);

        if (appCMSPresenter.getAppCMSMain().isMonetizationModelEnabled()) {
            if (onUpdatedContentDatum != null && onUpdatedContentDatum.getSubscriptionPlans() != null)
                this.videoContentDatum = onUpdatedContentDatum;
            else if (videoContentDatum != null && videoContentDatum.getSubscriptionPlans() != null)
                this.videoContentDatum = videoContentDatum;

            videoContentDatum.setFromStandalone(true);
            setDataWaysToWatch();
            AppCMSPresenter.EntitlementPendingVideoData entitlementPendingVideoData
                    = new AppCMSPresenter.EntitlementPendingVideoData.Builder()
                    .contentDatum(videoContentDatum)
                    .build();
            appCMSPresenter.setEntitlementPendingVideoData(entitlementPendingVideoData);
            if (videoContentDatum != null && videoContentDatum.getSubscriptionPlans() != null) {
                entitlementButton1.setVisibility(GONE);
                entitlementButton2.setVisibility(GONE);
                entitlementButton3.setVisibility(GONE);
                entitlementButton4.setVisibility(GONE);
                if (contentTypeChecker.isContentSVOD_TVOD_TVE(videoContentDatum.getSubscriptionPlans(), null)) {
                    handleTvodBuyRent(videoContentDatum);
                    entitlementButton3.setVisibility(VISIBLE);
                    entitlementButton4.setVisibility(VISIBLE);
                    buttonPositions(tveSvodButtons, entitlementButton3, entitlementButton4, verticalGuideline);
                    if (appPreference.getTVEUserId() != null) {
                        entitlementButton4.setVisibility(GONE);
                        expandButton(entitlementButton3, tveSvodButtons);
                    }
                } else if (contentTypeChecker.isContentSVOD_TVOD(videoContentDatum.getSubscriptionPlans(), null)) {
                    handleTvodBuyRent(videoContentDatum);
                    entitlementButton3.setVisibility(VISIBLE);
                    entitlementButton4.setVisibility(GONE);
                    expandButton(entitlementButton3, tveSvodButtons);
                } else if (contentTypeChecker.isContentSVOD_TVE(videoContentDatum.getSubscriptionPlans(), null)) {
                    entitlementButton1.setVisibility(GONE);
                    entitlementButton2.setVisibility(GONE);
                    entitlementButton3.setVisibility(VISIBLE);
                    entitlementButton4.setVisibility(VISIBLE);
                    buttonPositions(tveSvodButtons, entitlementButton3, entitlementButton4, verticalGuideline);
                    if (appPreference.getTVEUserId() != null) {
                        entitlementButton4.setVisibility(GONE);
                        expandButton(entitlementButton3, tveSvodButtons);
                    }
                } else if (contentTypeChecker.isContentTVOD_TVE(videoContentDatum.getSubscriptionPlans())) {
                    handleTvodBuyRent(videoContentDatum);
                    entitlementButton3.setVisibility(GONE);
                    entitlementButton4.setVisibility(VISIBLE);
                    expandButton(entitlementButton4, tveSvodButtons);
                    if (appPreference.getTVEUserId() != null)
                        entitlementButton4.setVisibility(GONE);
                } else if (contentTypeChecker.isContentTVOD(videoContentDatum.getSubscriptionPlans()) || contentTypeChecker.isContentTVOD_AVOD(videoContentDatum.getSubscriptionPlans())) {
                    handleTvodBuyRent(videoContentDatum);
                    entitlementButton3.setVisibility(GONE);
                    entitlementButton4.setVisibility(GONE);
                } else if (contentTypeChecker.isContentTVE(videoContentDatum.getSubscriptionPlans()) || contentTypeChecker.isContentTVE_AVOD(videoContentDatum.getSubscriptionPlans())) {
                    entitlementButton2.setVisibility(GONE);
                    entitlementButton1.setVisibility(GONE);
                    entitlementButton3.setVisibility(GONE);
                    entitlementButton4.setVisibility(VISIBLE);
                    expandButton(entitlementButton4, tveSvodButtons);
                } else if (contentTypeChecker.isContentSVOD(videoContentDatum.getSubscriptionPlans()) || contentTypeChecker.isContentSVOD_AVOD(videoContentDatum.getSubscriptionPlans())) {
                    entitlementButton1.setVisibility(GONE);
                    entitlementButton2.setVisibility(GONE);
                    entitlementButton4.setVisibility(GONE);
                    entitlementButton3.setVisibility(VISIBLE);
                    expandButton(entitlementButton3, tveSvodButtons);
                }
            } else {
                entitlementButton1.setVisibility(GONE);
                entitlementButton2.setVisibility(GONE);
                entitlementButton4.setVisibility(GONE);
                entitlementButton3.setVisibility(GONE);
            }

        } else {

            if (btnLogin != null) {
                if (appCMSPresenter.isUserLoggedIn()) {
                    btnLogin.setVisibility(View.GONE);
                } else {
                    btnLogin.setVisibility(View.VISIBLE);
                }
            }

            String message = null;
            if (isScheduledForFuture(onUpdatedContentDatum)) {
                showTimerAndMessage();
                return;
            }


            boolean isFreeContent = CommonUtils.isFreeContent(getContext(), onUpdatedContentDatum);

            if (appCMSPresenter.getAppCMSMain().isForceLogin() && !appCMSPresenter.isUserLoggedIn()
                    && isFreeContent && (btnStartFreeTrial != null && btnLogin != null)) {
                btnStartFreeTrial.setText(localisedStrings.getSignUpText());
                btnLogin.setText(localisedStrings.getSignInText());
                message = localisedStrings.getEntitlementLoginErrorMessageText();
            } else if (appCMSPresenter.getAppCMSMain().isForceLogin() && appCMSPresenter.isUserLoggedIn()
                    && isFreeContent) {
                return;
            } else if (CommonUtils.isPPVContent(getContext(), onUpdatedContentDatum)) {
                checkPPVPlayState();
                return;
            } else if (appCMSPresenter.isUserLoggedIn() && appCMSPresenter.isUserSubscribed()) {
                return;
            } else if (appCMSPresenter.isShowDialogForWebPurchase()) {
                // message = appCMSPresenter.getWebSubscriptionMessagePrefixText();
               /* makeLinkClickable(localisedStrings.getWebSubscriptionMessagePrefixText(), localisedStrings.getWebSubscriptionMessageSuffixText(), appCMSPresenter.getAppCMSMain().getDomainName());
                return;*/
                /**
                 * As per discussion with Kapil we are not showing website this will be added in the WebSubscriptionMessagePrefix message at tools.
                 */
                message = localisedStrings.getWebSubscriptionMessagePrefixText();
            /*if (appCMSPresenter.isUserLoggedIn()) {
                message = appCMSPresenter.getPremiumLoggedInUserMsg();
            } else {
                message = appCMSPresenter.getPremiumContentGuestUserDialogMessageText();
                */

            } /* else if (appCMSPresenter != null &&
                appCMSPresenter.getAppCMSAndroid().getSubscriptionFlowContent() != null &&
                appCMSPresenter.getAppCMSAndroid().getSubscriptionFlowContent().getOverlayMessage() != null) {
            message = appCMSPresenter.getAppCMSAndroid().getSubscriptionFlowContent().getOverlayMessage();
        } */ else if (isMaxStreamError) {
                message = localisedStrings.getMaxStreamErrorText();
            } else {
                message = localisedStrings.getEncourageUserLoginText();
            }
            if (previewCustomMessageView != null)
                previewCustomMessageView.setText(message);
            hideRestrictedMessage();
        }

        customPreviewContainer.post(new Runnable() {
            @Override
            public void run() {
                customPreviewContainer.setVisibility(View.VISIBLE);

            }
        });
    }


    /**
     * Description : Checks if media is scheduled for future
     *
     * @param onUpdatedContentDatum Content to be checked
     * @return
     */

    public boolean isScheduledForFuture(ContentDatum onUpdatedContentDatum) {

        long eventDate = (onUpdatedContentDatum.getGist().getScheduleStartDate());
        long remainingTime = CommonUtils.getTimeIntervalForEvent(eventDate, "yyyy MMM dd HH:mm:ss");

        if (onUpdatedContentDatum.getGist() != null
                && onUpdatedContentDatum.getGist().getEventSchedule() != null
                && onUpdatedContentDatum.getGist().getEventSchedule().size() > 0
                && onUpdatedContentDatum.getGist().getEventSchedule().get(0) != null
                && onUpdatedContentDatum.getGist().getEventSchedule().get(0).getEventTime() != 0) {
            eventDate = onUpdatedContentDatum.getGist().getEventSchedule().get(0).getEventTime();

            //calculate remaining time from event date and current date
            remainingTime = CommonUtils.getTimeIntervalForEventSchedule(eventDate * 1000L, "EEE MMM dd HH:mm:ss");
        }

        if (remainingTime > 0)
            return true;
        else
            return false;
    }

    void showTimerAndMessage() {
        appCMSPresenter.getTransactionData(onUpdatedContentDatum.getGist().getId(), updatedContentDatum -> {
            appCMSPresenter.stopLoader();
            boolean isPurchased = false;
            AppCMSTransactionDataValue objTransactionData = null;
            String msg = "";

            if (updatedContentDatum != null &&
                    updatedContentDatum.size() > 0 && updatedContentDatum.get(0).size() > 0) {
                isPurchased = true;
                objTransactionData = updatedContentDatum.get(0).get(onUpdatedContentDatum.getGist().getId());
            }

            long eventDate = (onUpdatedContentDatum.getGist().getScheduleStartDate());
            //calculate remaining time from event date and current date
            long remainingTime = CommonUtils.getTimeIntervalForEvent(eventDate, "yyyy MMM dd HH:mm:ss");
            boolean isEventSchedule = false;
            String coverImage = null;

            if (onUpdatedContentDatum.getGist() != null
                    && onUpdatedContentDatum.getGist().getEventSchedule() != null
                    && onUpdatedContentDatum.getGist().getEventSchedule().size() > 0
                    && onUpdatedContentDatum.getGist().getEventSchedule().get(0) != null
                    && onUpdatedContentDatum.getGist().getEventSchedule().get(0).getEventTime() != 0) {
                eventDate = onUpdatedContentDatum.getGist().getEventSchedule().get(0).getEventTime();

                //calculate remaining time from event date and current date
                remainingTime = CommonUtils.getTimeIntervalForEventSchedule(eventDate * 1000L, "EEE MMM dd HH:mm:ss");
                isEventSchedule = true;
            }

            if (remainingTime > 0) {
                if (onUpdatedContentDatum.getGist().getImageGist() != null
                        && onUpdatedContentDatum.getGist().getImageGist().get_16x9() != null
                        && !TextUtils.isEmpty(onUpdatedContentDatum.getGist().getImageGist().get_16x9())) {
                    coverImage = onUpdatedContentDatum.getGist().getImageGist().get_16x9();
                } else if (onUpdatedContentDatum.getGist().getVideoImageUrl() != null
                        && !TextUtils.isEmpty(onUpdatedContentDatum.getGist().getVideoImageUrl())) {
                    coverImage = onUpdatedContentDatum.getGist().getVideoImageUrl();
                } else if (onUpdatedContentDatum.getImageUrl() != null
                        && !TextUtils.isEmpty(onUpdatedContentDatum.getImageUrl())) {
                    coverImage = onUpdatedContentDatum.getImageUrl();
                }

                initializeFutureContentTimerView(eventDate, remainingTime, isEventSchedule, coverImage);
            }


            // now show uh-oh message
            if (onUpdatedContentDatum.getPricing().getType().equalsIgnoreCase("FREE") || appCMSPresenter.isUserSubscribed() || isPurchased) {
                return;
            } else if (!isTimerRefreshRequired && CommonUtils.isPPVContent(getContext(), onUpdatedContentDatum)) {
                appCMSPresenter.showDialog(AppCMSPresenter.DialogType.TVOD_CONTENT_ERROR,
                        localisedStrings.getTVODContentError(appCMSPresenter.getAppCMSMain().getDomainName()), false, null, null, localisedStrings.getPremiumContentText());
            }
        }, "Video");

    }

    public void makeLinkClickable(String mesgPrefix, String msgSuffix, String domain) {

        String webLinkPlan = mContext.getString(R.string.web_view_plans_path, domain);
        String message = mesgPrefix + "\n" + webLinkPlan + " \n" + msgSuffix;

        int spanStart = (mesgPrefix.length() + 1);
        int spanEnd = (mesgPrefix.length() + webLinkPlan.length() + 1);
        SpannableString spannableString = new SpannableString(message);
        if (TextUtils.isEmpty(msgSuffix) && TextUtils.isEmpty(domain)) {
            previewCustomMessageView.setText(mesgPrefix);
        } else {
            ClickableSpan clickableSpan = new ClickableSpan() {
                @Override
                public void onClick(View view) {
                    //what happens whe i click
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(webLinkPlan));
                    appCMSPresenter.getCurrentActivity().startActivity(browserIntent);
                }
            };
            spannableString.setSpan(clickableSpan,
                    spanStart,
                    spanEnd,
                    SpannableString.SPAN_EXCLUSIVE_INCLUSIVE);


            previewCustomMessageView.setText(spannableString, AppCompatTextView.BufferType.SPANNABLE);
            previewCustomMessageView.setClickable(true);
            previewCustomMessageView.setLinkTextColor(appCMSPresenter.getBrandPrimaryCtaColor());
            previewCustomMessageView.setMovementMethod(LinkMovementMethod.getInstance());
            previewCustomMessageView.setTextAlignment(TEXT_ALIGNMENT_CENTER);

        }

        hideRestrictedMessage();
        customPreviewContainer.post(new Runnable() {
            @Override
            public void run() {
                customPreviewContainer.setVisibility(View.VISIBLE);
            }
        });
    }

    public void setErrorMessage(String message) {
        previewCustomMessageView.setText(message);
        disableController();
        customPreviewContainer.setVisibility(View.VISIBLE);
    }

    private void hidePreviewFrame() {
        hideMiniPlayer = false;
        if (useController)
            enableController();
        isPreviewShown = false;
        customPreviewContainer.post(new Runnable() {
            @Override
            public void run() {
                customPreviewContainer.setVisibility(View.GONE);
                // if (appCMSPresenter.getAppPreference().getMiniPLayerVisibility() && (appCMSPresenter.getIsMiniPlayerPlaying()) ) {
                if (isVideoPlaying && (appCMSPresenter.getAppPreference().getMiniPLayerVisibility()
                        && (appCMSPresenter.getIsMiniPlayerPlaying())
                        || appCMSPresenter.getDefaultTrailerPlay()
                        || !appCMSPresenter.getAppPreference().getShowPIPVisibility())) {
                    resumePlayer();
                } else
                    pausePlayer();
            }
        });
    }

    public void setTrailerCompletedCallback(TrailerCompletedCallback trailerCompletedCallback) {
        this.trailerCompletedCallback = trailerCompletedCallback;
    }

    TrailerCompletedCallback trailerCompletedCallback;


    @Override
    public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {

        if (beaconMessageThread != null) {
            beaconMessageThread.playbackState = playbackState;
        }

        boolean isWatchHistoryUpdateEnabled = appCMSPresenter.getAppCMSMain().getFeatures() != null
                && appCMSPresenter.getAppCMSMain().getFeatures().getWatchedHistory() != null
                && appCMSPresenter.getAppCMSMain().getFeatures().getWatchedHistory().isEnabled();

        switch (playbackState) {
            case STATE_ENDED:
                if (trailerCompletedCallback != null && isTrailer)
                    trailerCompletedCallback.videoCompleted();
                pausePlayer();
                if (!isTrailer)
                    createCustomMessageView();

                        /*if (isFromPlayListPage) {

                            if (isPlayerAlreadyLoaded) {
                                return;
                            }

                            if (appCMSPresenter.getAutoplayEnabledUserPref(mContext)) {
                                AppCMSPresenter.currentVideoPlayListIndex = AppCMSPresenter.currentVideoPlayListIndex + 1;
                                if (AppCMSPresenter.currentVideoPlayListIndex <= videoPlayListIds.size() - 1) {
                                    SeasonTabSelectorBus.instanceOf().setTimeCarousel(AppCMSPresenter.currentVideoPlayListIndex);
                                    isPlayerAlreadyLoaded = true;
                                    setVideoUri(videoPlayListIds.get(AppCMSPresenter.currentVideoPlayListIndex), appCMSPresenter.getLoadingVideoText());
                                    final Handler handler = new Handler();
                                    handler.postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            // Do something after 5s = 5000ms
                                            isPlayerAlreadyLoaded = false;
                                        }
                                    }, 2000);
                                }
                            } else {
                                showRestrictMessage(appCMSPresenter.getAutoPlayoffMessageText());
                            }

                            return;
                        }*/

                if (null != relatedVideoId && currentPlayingIndex <= relatedVideoId.size() - 1 && !isTrailer) {
                    if (entitlementCheckTimer != null) {
                        entitlementCheckTimer.cancel();
                    }
                    if (appCMSPresenter.getAutoplayEnabledUserPref(mContext)) {
                        if (appCMSPresenter.getCurrentPageName() != null &&
                                !TextUtils.isEmpty(appCMSPresenter.getCurrentPageName()) &&
                                appCMSPresenter.getCurrentPageName().equalsIgnoreCase("Video Page") &&
                                !appCMSPresenter.isFullScreenVisible) {

                            Log.d("VIDEOPLAYLIST", "1 ----> " + Integer.toString(currentPlayingIndex));

                            appCMSPresenter.refreshVideoData(relatedVideoId.get(currentPlayingIndex), contentDatum -> {
                                getPermalink(contentDatum);
                                setAdsUrl(adsUrl);
                                appCMSPresenter.launchButtonSelectedAction(contentDatum.getGist().getPermalink(),
                                        mContext.getString(R.string.app_cms_action_detailvideopage_key),
                                        contentDatum.getGist().getTitle(),
                                        null,
                                        contentDatum,
                                        false,
                                        currentPlayingIndex,
                                        relatedVideoId
                                );

                            }, null, false, false, null);
                        } else {
                            Log.d("VIDEOPLAYLIST", "2 ----> " + Integer.toString(currentPlayingIndex));
                            setVideoUri(relatedVideoId.get(currentPlayingIndex), localisedStrings.getLoadingVideoText(), isTrailer, false, onUpdatedContentDatum);
                        }

                    } else {
                        //disableController();
                        if (appCMSPresenter.getAppCMSMain().getFeatures().isAutoPlay())
                            showRestrictMessage(localisedStrings.getAutoPlayoffMessageText());
                    }
                } else if (currentPLayingVideoContentData != null && currentPLayingVideoContentData.getModuleApi() != null && currentPLayingVideoContentData.getModuleApi().getContentData() != null && currentPLayingVideoContentData.getModuleApi().getContentData().get(0).getSeason() != null && !isTrailer) {
                    int currentPlayedSeason = currentPlayingSeasonPosition();
                    int currentPlayedEpisode = findCurrentPlayingPositionOfEpisode();
                    if (currentPlayedSeason + 1 == currentPLayingVideoContentData.getModuleApi().getContentData().get(0).getSeason().size() &&
                            currentPLayingVideoContentData.getModuleApi().getContentData().get(0).getSeason().get(currentPlayedSeason).getEpisodes().size() == currentPlayedEpisode + 1) {
                        showRestrictMessage(localisedStrings.getNoVideoInQueueText());
                    } else {
                        int nextSeason = currentPlayedSeason;
                        int nextEpisode = currentPlayedEpisode;
                        if (onUpdatedContentDatum.getGist().getId().equalsIgnoreCase(currentPLayingVideoContentData.getModuleApi().getContentData().get(0).getSeason().get(currentPlayedSeason).getEpisodes().get(currentPLayingVideoContentData.getModuleApi().getContentData().get(0).getSeason().get(currentPlayedSeason).getEpisodes().size() - 1).getGist().getId())) {
                            nextSeason++;
                        } else {
                            nextEpisode++;
                        }
                        CustomVideoPlayerView customVideoPlayerView = appCMSPresenter.getCurrentActivity().findViewById(R.id.videoTrailer);
                        if (customVideoPlayerView != null) {
                            ShowDetailsPromoHandler.getInstance().setTrailerPromoAutoPlay(appCMSPresenter, currentPLayingVideoContentData.getModuleApi().getContentData().get(0).getSeason().get(nextSeason).getEpisodes().get(nextEpisode), customVideoPlayerView);
                            SeasonTabSelectorBus.instanceOf().setCurrentPlayingVideoId(currentPLayingVideoContentData.getModuleApi().getContentData().get(0).getSeason().get(nextSeason).getEpisodes().get(nextEpisode).getId());
                        }


                        setDisplayMetadata(currentPLayingVideoContentData.getModuleApi().getContentData().get(0).getSeason().get(nextSeason).getEpisodes().get(nextEpisode));
                    }
                } else {
                    if (!isTrailer) {
                        showRestrictMessage(localisedStrings.getNoVideoInQueueText());
                    }
                }
                updateWatchedHistory();

                break;
            case STATE_BUFFERING:
            case STATE_IDLE:
                showProgressBar(localisedStrings.getLiveStreamingText());
                getPlayerView().hideController();
                if (beaconMessageThread != null) {
                    beaconMessageThread.sendBeaconPing = false;
                }
                if (beaconBufferingThread != null && appCMSPresenter.getAppCMSMain().getFeatures() != null && appCMSPresenter.getAppCMSMain().getFeatures().isEnableQOS()) {
                    beaconBufferingThread.sendBeaconBuffering = true;
                    if (!beaconBufferingThread.isAlive()) {
                        beaconBufferingThread.start();
                    }
                }
                ((Activity) mContext).getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
                if (appCMSPresenter.getAppCMSMain().getFeatures().isMuteSound() && muteNotifyView != null)
                    muteNotifyView.setVisibility(GONE);

                break;
            case STATE_READY:
                hideProgressBar();
                if (trailerCompletedCallback != null && isTrailer)
                    trailerCompletedCallback.videoStarted();

                if (!player.isPlayingAd()) {
                    if (!streamingQualitySelectorCreated) {
                        createStreamingQualitySelectorForHLS();
                        createStreamingQualitySelector();
                    }
                    if (!closedCaptionSelectorCreated)
                        initCCAdapter();
                    if (!audioSelectorCreated && audioSelectorShouldCreated)
                        if ((appCMSPresenter.getPlatformType() == AppCMSPresenter.PlatformType.TV)) {
                            createAudioSelector();
                        } else if ((appCMSPresenter.getPlatformType() == AppCMSPresenter.PlatformType.ANDROID)) {
                            createLanguageSelector();
                        }
                }

                if (streamingQualitySelectorCreated || closedCaptionSelectorCreated) {
                    if (!streamingQualitySelectorCreated && useHls) {
                        if (!appCMSPresenter.isVideoDownloaded(getFilmId())) {
                            //createStreamingQualitySelectorForHLS();
                            //   createLanguageSelector();
                        }
                    }
                    if (availableClosedCaptions != null) {
                        settingsButtonVisibility(true);
                    } else if (listViewAdapter != null) {
                        settingsButtonVisibility(true);
                    } else if (hlsListViewAdapter != null) {
                        settingsButtonVisibility(true);
                    }

                }


                if (getPlayerView().getPlayer().getPlayWhenReady()) {
                    ((Activity) mContext).getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

                } else {
                    ((Activity) mContext).getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

                }
                /**
                 * This will mute the Standalone video in time of ads or streaming refresh
                 *
                 */
                if (appCMSPresenter.getAppCMSMain().getFeatures().isMuteSound() && isMuteNotifyVisible && muteNotifyView != null && isLiveStream()) {
                    muteNotifyView.setVisibility(VISIBLE);
                    setPlayerVolumeImage(playerVolumeButton, true);
                }

                long updatedRunTime = 0;
                try {
                    updatedRunTime = getDuration() / 1000;
                } catch (Exception e) {
                    e.printStackTrace();
                }
                videoPlayTime = appCMSPresenter.setCurrentWatchProgress(updatedRunTime, watchedTime);

                if (!isVideoLoaded) {
                    setCurrentPosition(videoPlayTime * SECS_TO_MSECS);

                    if (isWatchHistoryUpdateEnabled && !isTrailer && !isLiveStream) {
                        appCMSPresenter.updateWatchedTime(videoDataId, seriesId, getCurrentPosition() / 1000, updateHistoryResponse -> {
                            if (updateHistoryResponse.getResponseCode() == 401) {
                                pausePlayer();
                                appCMSPresenter.showDialog(AppCMSPresenter.DialogType.MAX_STREAMS_ERROR, updateHistoryResponse.getErrorMessage(), true, null, null, updateHistoryResponse.getErrorCode());
                            }
                        });
                    }
                    isVideoLoaded = true;
                }
                if (beaconBufferingThread != null) {
                    beaconBufferingThread.sendBeaconBuffering = false;
                }
                if (beaconMessageThread != null) {
                    beaconMessageThread.sendBeaconPing = true;
                    if (!beaconMessageThread.isAlive()) {
                        try {
                            beaconMessageThread.start();
                            mTotalVideoDuration = getDuration() / 1000;
                            mTotalVideoDuration -= mTotalVideoDuration % 4;
                            mProgressHandler.post(mProgressRunnable);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    if (!sentBeaconFirstFrame && appCMSPresenter.getAppCMSMain().getFeatures().isEnableQOS()) {
                        mStopBufferMilliSec = new Date().getTime();
                        ttfirstframe = mStartBufferMilliSec == 0l ? 0d : ((mStopBufferMilliSec - mStartBufferMilliSec) / 1000d);
                        appCMSPresenter.sendBeaconMessage(videoDataId,
                                permaLink,
                                parentScreenName,
                                getCurrentPosition(),
                                false,
                                AppCMSPresenter.BeaconEvent.FIRST_FRAME,
                                "Video",
                                getBitrate() != 0 ? String.valueOf(getBitrate()) : null,
                                String.valueOf(getVideoHeight()),
                                String.valueOf(getVideoWidth()),
                                mStreamId,
                                ttfirstframe,
                                0,
                                isVideoDownloaded);
                        sentBeaconFirstFrame = true;
                        appCMSPresenter.sendGaEvent(mContext.getResources().getString(R.string.play_video_action),
                                mContext.getResources().getString(R.string.play_video_category),
                                (videoTitle != null && !TextUtils.isEmpty(videoTitle)) ? videoTitle : videoDataId);

                    }
                }

                if (CastServiceProvider.getInstance(mContext).isCastingConnected()) {
                    pausePlayer();
                }
                setNotification();
                break;

            default:
                hideProgressBar();
        }

        if (!sentBeaconPlay) {
            appCMSPresenter.sendBeaconMessage(videoDataId,
                    permaLink,
                    parentScreenName,
                    getCurrentPosition(),
                    false,
                    AppCMSPresenter.BeaconEvent.PLAY,
                    "Video",
                    getBitrate() != 0 ? String.valueOf(getBitrate()) : null,
                    String.valueOf(getVideoHeight()),
                    String.valueOf(getVideoWidth()),
                    mStreamId,
                    0d,
                    0,
                    isVideoDownloaded);
            sentBeaconPlay = true;
            mStartBufferMilliSec = new Date().getTime();

            appCMSPresenter.sendGaEvent(mContext.getResources().getString(R.string.play_video_action),
                    mContext.getResources().getString(R.string.play_video_category),
                    (videoTitle != null && !TextUtils.isEmpty(videoTitle)) ? videoTitle : videoDataId);
            if (onUpdatedContentDatum != null)
                appCMSPresenter.sendPlayStartedEvent(onUpdatedContentDatum);
        }
    }

    public void setVideoTitle(String title) {
        videoTitle = title;
    }

    public void releasePreviousAdsPlayer() {
        try {
            if (mImaAdsLoader != null) {
                if (mImaAdsLoader.getAdDisplayContainer().getAdContainer() != null) {
                    mImaAdsLoader.getAdDisplayContainer().getAdContainer().removeAllViews();
                }
                mImaAdsLoader.release();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void setVideoPlayerStatus() {
        showOverlayWhenCastingConnected();
        if (mToggleButton != null && mToggleButton.isChecked()) {
            llTopBar.setVisibility(View.VISIBLE);
        } else {
            llTopBar.setVisibility(View.GONE);
        }
        ((AppCMSPageActivity) mContext).setCastingVisibility(true);

    }

    public void pausePlayer() {
        if (null != getPlayer()) {
            //    videoPaused=true;
            getPlayer().setPlayWhenReady(false);
        }
        if (beaconMessageThread != null) {
            beaconMessageThread.sendBeaconPing = false;
        }
        if (beaconBufferingThread != null) {
            beaconBufferingThread.sendBeaconBuffering = false;
        }
    }

    public void resumePlayer() {
        if (onUpdatedContentDatum != null && !appCMSPresenter.isPinVerified() && CommonUtils.isUnderAgeRestrictions(appCMSPresenter, onUpdatedContentDatum.getParentalRating())) {
            if (isPinDialogShown)
                return;
            isPinDialogShown = true;
            AppCMSVerifyVideoPinDialog.newInstance(isVerified -> {
                appCMSPresenter.setPinVerified(isVerified);
                isPinDialogShown = false;
                if (isVerified) {
                    resumePlayer();
                    enableController();
                } else {
                    isVideoPlaying = false;
                    pausePlayer();
                    disableController();
                    appCMSPresenter.dismissPopupWindowPlayer(true);
                    appCMSPresenter.setMiniPLayerVisibility(false);
                }
            }, true).show(appCMSPresenter.getCurrentActivity().getSupportFragmentManager(), AppCMSVerifyVideoPinDialog.class.getSimpleName());
            return;
        }

        if (null != getPlayer() && !getPlayer().getPlayWhenReady()) {
            videoPaused = false;
            getPlayer().setPlayWhenReady(true);
        }
        if (beaconMessageThread != null) {
            beaconMessageThread.sendBeaconPing = true;
        }
        if (beaconBufferingThread != null && appCMSPresenter.getAppCMSMain().getFeatures() != null && appCMSPresenter.getAppCMSMain().getFeatures().isEnableQOS()) {
            beaconBufferingThread.sendBeaconBuffering = true;
        }
        if (appCMSPresenter.getIsMiniPlayerPlaying()) {
            onDestroyNotification();
            createNotification();
            setNotification();
        }
//        createNotification();
//        setNotification();
        /** Bellow code is for managing CC state between full player and standalone player for navigation */
        if (appPreference.getPreferredSubtitleLanguage() != null && appPreference.getPreferredSubtitleLanguage().equalsIgnoreCase("off")) {
            closeCaptionPreferenceCheck(appPreference.getPreferredSubtitleLanguage(), 0);
            setSubtitleViewVisibility(false);
        } else if (appPreference.getPreferredSubtitleLanguage() != null && availableClosedCaptions != null && availableClosedCaptions.size() > 0) {
            for (int i = 0; i < availableClosedCaptions.size(); i++) {
                ClosedCaptions closedCaptions = availableClosedCaptions.get(i);
                if (closedCaptions.getLanguage().equalsIgnoreCase("cc")) {
                    closeCaptionPreferenceCheck(closedCaptions.getLanguage(), i);
                    setClosedCaption(i);
                    setSubtitleViewVisibility(true);

                    break;
                } else if (closedCaptions.getLanguage().equalsIgnoreCase(appPreference.getPreferredSubtitleLanguage())) {
                    closeCaptionPreferenceCheck(closedCaptions.getLanguage(), i);
                    setClosedCaption(i);
                    setSubtitleViewVisibility(true);
                    break;
                }
            }
        }
    }

    public String getPageId() {
        return pageId;
    }

    public void setPageId(String pageId) {
        this.pageId = pageId;
    }

    String pageId;


    @Override
    public void onMediaPeriodCreated(int windowIndex, MediaSource.MediaPeriodId mediaPeriodId) {

    }

    @Override
    public void onMediaPeriodReleased(int windowIndex, MediaSource.MediaPeriodId mediaPeriodId) {

    }

    @Override
    public void onLoadStarted(int windowIndex, @Nullable MediaSource.MediaPeriodId mediaPeriodId, LoadEventInfo loadEventInfo, MediaLoadData mediaLoadData) {

    }

    @Override
    public void onLoadCompleted(int windowIndex, @Nullable MediaSource.MediaPeriodId mediaPeriodId, LoadEventInfo loadEventInfo, MediaLoadData mediaLoadData) {

    }

    @Override
    public void onLoadCanceled(int windowIndex, @Nullable MediaSource.MediaPeriodId mediaPeriodId, LoadEventInfo loadEventInfo, MediaLoadData mediaLoadData) {

    }

    @Override
    public void onLoadError(int windowIndex, @Nullable MediaSource.MediaPeriodId mediaPeriodId, LoadEventInfo loadEventInfo, MediaLoadData mediaLoadData, IOException error, boolean wasCanceled) {

    }

    @Override
    public void onReadingStarted(int windowIndex, MediaSource.MediaPeriodId mediaPeriodId) {

    }

    @Override
    public void onUpstreamDiscarded(int windowIndex, MediaSource.MediaPeriodId mediaPeriodId, MediaLoadData mediaLoadData) {

    }

    @Override
    public void onDownstreamFormatChanged(int windowIndex, @Nullable MediaSource.MediaPeriodId mediaPeriodId, MediaLoadData mediaLoadData) {

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
        return getUri().toString();
    }

    @Override
    public void setVideoUrl(String url) {

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
        }

        return availableStreamingQualities.size() - 1;
    }

    @Override
    public String getFilmId() {
        return videoDataId;
    }

    @Override
    public void launchSetting(ClosedCaptionSelectorAdapter closedCaptionSelectorAdapter, StreamingQualitySelectorAdapter videoQualityAdapter) {
        if (appCMSPresenter.videoPlayerView != null) {
            appCMSPresenter.videoPlayerView.pausePlayer();
        }
        removeRelatedVideoView();
        //videoPlayerMainContainer.setVisibility(View.GONE);
        playerSettingsView.setClosedCaptionSelectorAdapter(closedCaptionSelectorAdapter);
        playerSettingsView.setStreamingQualitySelectorAdapter(videoQualityAdapter);
        playerSettingsView.updateSettingItems();
        playerSettingsView.setPlayerSettingsEvent(this);
        playerSettingsView.setVisibility(View.VISIBLE);

    }

    @Override
    public void launchSetting(ClosedCaptionSelectorAdapter closedCaptionSelectorAdapter, HLSStreamingQualitySelectorAdapter hlsStreamingQualitySelectorAdapter, LanguageSelectorAdapter languageSelectorAdapter) {
        if (appCMSPresenter.videoPlayerView != null) {
            appCMSPresenter.videoPlayerView.pausePlayer();
        }

        //videoPlayerMainContainer.setVisibility(View.GONE);
        playerSettingsView.setClosedCaptionSelectorAdapter(closedCaptionSelectorAdapter);
        playerSettingsView.setHlsStreamingQualitySelectorAdapter(hlsStreamingQualitySelectorAdapter);
        //playerSettingsView.setStreamingQualitySelectorAdapter(null);
        playerSettingsView.setLanguageSelectorAdapter(languageSelectorAdapter);
        playerSettingsView.updateSettingItems();
        playerSettingsView.setPlayerSettingsEvent(this);
        playerSettingsView.setVisibility(View.VISIBLE);

    }

    @Override
    public void launchSetting(ClosedCaptionSelectorAdapter closedCaptionSelectorAdapter, HLSStreamingQualitySelectorAdapter hlsStreamingQualitySelectorAdapter, StreamingQualitySelectorAdapter streamingQualitySelectorAdapter, LanguageSelectorAdapter languageSelectorAdapter) {
        if (appCMSPresenter.videoPlayerView != null) {
            appCMSPresenter.videoPlayerView.pausePlayer();
        }
        removeRelatedVideoView();
        setPreviousNextVisibility(false);
        //videoPlayerMainContainer.setVisibility(View.GONE);
        playerSettingsView.setClosedCaptionSelectorAdapter(closedCaptionSelectorAdapter);
        playerSettingsView.setHlsStreamingQualitySelectorAdapter(hlsStreamingQualitySelectorAdapter);
        playerSettingsView.setStreamingQualitySelectorAdapter(streamingQualitySelectorAdapter);
        playerSettingsView.setLanguageSelectorAdapter(languageSelectorAdapter);
        playerSettingsView.updateSettingItems();
        playerSettingsView.setPlayerSettingsEvent(this);
        playerSettingsView.setVisibility(View.VISIBLE);

    }

    @Override
    public void finishPlayerSetting() {
        playerSettingsView.setVisibility(View.GONE);
        //videoPlayerMainContainer.setVisibility(View.VISIBLE);
       /* if (appCMSPresenter.videoPlayerView != null) {
            appCMSPresenter.videoPlayerView.setClosedCaption(playerSettingsView.getSelectedClosedCaptionIndex());
            appCMSPresenter.videoPlayerView.setStreamingQuality(playerSettingsView.getSelectedStreamingQualityIndex(),
                    (appCMSPresenter.videoPlayerView.hlsListViewAdapter != null &&
                            appCMSPresenter.videoPlayerView.availableStreamingQualitiesHLS != null &&
                            appCMSPresenter.videoPlayerView.availableStreamingQualitiesHLS.size() > playerSettingsView.getSelectedStreamingQualityIndex()) ?
                            appCMSPresenter.videoPlayerView.availableStreamingQualitiesHLS.get(playerSettingsView.getSelectedStreamingQualityIndex()) : "");
            appCMSPresenter.videoPlayerView.setAudioLanguage(playerSettingsView.getLanguageSelectorIndex());
            appCMSPresenter.videoPlayerView.startPlayer(true);
        }*/
        if (playerSettingsView.getSelectedClosedCaptionIndex() >= 0) {
            setClosedCaption(playerSettingsView.getSelectedClosedCaptionIndex());
            if(playerSettingsView.getSelectedStreamingQualityIndex()>=0 )
            setStreamingQuality(playerSettingsView.getSelectedStreamingQualityIndex(), availableStreamingQualitiesHLS != null ? availableStreamingQualitiesHLS.get(playerSettingsView.getSelectedStreamingQualityIndex()) : "");
            if(playerSettingsView.getLanguageSelectorIndex()>=0)
            setAudioLanguage(playerSettingsView.getLanguageSelectorIndex());
        }
        startPlayer(true);
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
    }

    @Override
    public List<ClosedCaptions> getAvailableClosedCaptions() {
        List<ClosedCaptions> closedCaptionsList = new ArrayList<>();

        if (appCMSPresenter != null && appCMSPresenter.getRealmController().isCCFileAvailableForOffLine(onUpdatedContentDatum.getGist().getId())) {

            for (DownloadClosedCaptionRealm dc : appCMSPresenter.getRealmController().getAllDownloadedCCFiles(onUpdatedContentDatum.getGist().getId())) {
                ClosedCaptions cc = Utils.convertDownloadClosedCaptionToClosedCaptions(dc);
                if ("SRT".equalsIgnoreCase(cc.getFormat())) {
                    cc.setUrl(appCMSPresenter.downloadedMediaLocalURI(dc.getCcFileEnqueueId()));
                    closedCaptionsList.add(cc);
                }
            }
        } else {
            if (onUpdatedContentDatum != null
                    && onUpdatedContentDatum.getContentDetails() != null
                    && onUpdatedContentDatum.getContentDetails().getClosedCaptions() != null) {
                ArrayList<ClosedCaptions> closedCaptions = onUpdatedContentDatum.getContentDetails().getClosedCaptions();
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

        if (appCMSPresenter != null && appCMSPresenter.getRealmController().isCCFileAvailableForOffLine(onUpdatedContentDatum.getGist().getId())) {

            for (DownloadClosedCaptionRealm dc : appCMSPresenter.getRealmController().getAllDownloadedCCFiles(onUpdatedContentDatum.getGist().getId())) {
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
            if (onUpdatedContentDatum != null
                    && onUpdatedContentDatum.getContentDetails() != null
                    && onUpdatedContentDatum.getContentDetails().getClosedCaptions() != null) {
                ArrayList<ClosedCaptions> closedCaptions = onUpdatedContentDatum.getContentDetails().getClosedCaptions();

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
    public void countDownOver() {

        if (timerViewFutureContent != null) {
            timerViewFutureContent.setVisibility(View.GONE);
        }
        playVideos(0, onUpdatedContentDatum);
    }

    class ForegroundObserver extends AsyncTask<Context, Void, Boolean> {

        @Override
        protected Boolean doInBackground(Context... params) {
            final Context context = params[0].getApplicationContext();
            return isAppOnForeground(context);
        }

        private boolean isAppOnForeground(Context context) {
            ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
            List<ActivityManager.RunningAppProcessInfo> appProcesses = activityManager.getRunningAppProcesses();
            if (appProcesses == null) {
                return false;
            }
            final String packageName = context.getPackageName();
            for (ActivityManager.RunningAppProcessInfo appProcess : appProcesses) {
                if (appProcess.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND && appProcess.processName.equals(packageName)) {
                    return true;
                }
            }
            return false;
        }
    }

    public void resumePlayerLastState() {
        if (null != getPlayer()) {
            if (onUpdatedContentDatum != null && !appCMSPresenter.isPinVerified() && CommonUtils.isUnderAgeRestrictions(appCMSPresenter, onUpdatedContentDatum.getParentalRating())) {
                if (isPinDialogShown)
                    return;
                isPinDialogShown = true;
                AppCMSVerifyVideoPinDialog.newInstance(isVerified -> {
                    appCMSPresenter.setPinVerified(isVerified);
                    isPinDialogShown = false;
                    if (isVerified) {
                        resumePlayerLastState();
                        enableController();
                    } else {
                        isVideoPlaying = false;
                        pausePlayer();
                        disableController();
                        appCMSPresenter.dismissPopupWindowPlayer(true);
                        appCMSPresenter.setMiniPLayerVisibility(false);
                    }
                }, true).show(appCMSPresenter.getCurrentActivity().getSupportFragmentManager(), AppCMSVerifyVideoPinDialog.class.getSimpleName());
                return;
            }
            if (isVideoPlaying) {
                try {
                    if (new ForegroundObserver().execute(mContext).get()) {
                        getPlayer().setPlayWhenReady(true);
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            } else {
                getPlayer().setPlayWhenReady(false);
            }
        }
    }

    public void releasePlayer() {
        if (getPlayer() != null) {
            getPlayer().release();
        }
    }

    public void setPauseState() {
        isVideoPlaying = false;
    }

    public void setPlayState() {
        isVideoPlaying = true;
    }

    private void createLoader() {

        customLoaderContainer = new LinearLayoutCompat(mContext);
        customLoaderContainer.setOrientation(LinearLayoutCompat.VERTICAL);
        customLoaderContainer.setGravity(Gravity.CENTER);
        ProgressBar progressBar = new ProgressBar(mContext);
        progressBar.setIndeterminate(true);
        progressBar.getIndeterminateDrawable().
                setColorFilter(ContextCompat.getColor(mContext, R.color.colorAccent),
                        PorterDuff.Mode.MULTIPLY
                );
        LinearLayoutCompat.LayoutParams progressbarParam = new LinearLayoutCompat.LayoutParams(50, 50);
        progressBar.setLayoutParams(progressbarParam);
        customLoaderContainer.addView(progressBar);
        loaderMessageView = new AppCompatTextView(mContext);
        LinearLayoutCompat.LayoutParams textViewParams = new LinearLayoutCompat.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        loaderMessageView.setLayoutParams(textViewParams);
        customLoaderContainer.addView(loaderMessageView);
        this.addView(customLoaderContainer);

    }

    private void createCustomMessageView() {
        if (customMessageContainer == null) {
            customMessageContainer = new LinearLayoutCompat(mContext);
        } else {
            if (customMessageContainer.getParent() != null) {
                ((ViewGroup) customMessageContainer.getParent()).removeView(customMessageContainer);
            }
        }
        if (settings != null && settings.isShowTabs() && tabSelectedScreen != null) {
            tabSelectedScreen.setVisibility(VISIBLE);
            LinearLayoutCompat.LayoutParams customMessageContainerParam = new LinearLayoutCompat.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            customMessageContainerParam.topMargin = 90;
            customMessageContainer.setLayoutParams(customMessageContainerParam);
        }
        customMessageContainer.setOrientation(LinearLayoutCompat.HORIZONTAL);
        customMessageContainer.setGravity(Gravity.CENTER);
        customMessageContainer.setBackgroundColor(Color.parseColor("#d4000000"));
        if (customMessageView == null) {
            customMessageView = new AppCompatTextView(mContext);
        } else {
            if (customMessageView.getParent() != null) {
                ((ViewGroup) customMessageView.getParent()).removeView(customMessageView);
            }
        }
        LinearLayoutCompat.LayoutParams textViewParams = new LinearLayoutCompat.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        textViewParams.gravity = Gravity.CENTER;
        customMessageView.setLayoutParams(textViewParams);
        customMessageView.setTextColor(Color.parseColor("#ffffff"));
        customMessageView.setTextSize(15);
        customMessageView.setPadding(20, 20, 20, 20);

        customMessageContainer.addView(customMessageView);
        customMessageContainer.setVisibility(View.INVISIBLE);
        this.addView(customMessageContainer);

    }

    public void showOverlayWhenCastingConnected() {
        if (CastServiceProvider.getInstance(mContext).isCastingConnected()) {
            if (parentView != null) {
                customMessageView.setText(localisedStrings.getTouchToCastMsgText());
                parentView.setVisibility(VISIBLE);
            }
            pausePlayer();

            if (CastingUtils.getRemoteMediaId(mContext) != null && onUpdatedContentDatum != null) {
                String filmId = CastingUtils.getRemoteMediaId(mContext);
                if (filmId.equalsIgnoreCase(""))
                    customMessageView.setText(CastingUtils.getCurrentPlayingVideoName(mContext));
                else
                    customMessageView.setText(localisedStrings.getCastMsgPrefixText() + CastingUtils.getCurrentPlayingVideoName(mContext) + localisedStrings.getCastMsgSuffixText() + CastServiceProvider.getInstance(mContext).getConnectedDeviceName());
            }
        } else {
            if (parentView != null) {
                parentView.setVisibility(GONE);
            }
        }
    }


    private void touchToCastOverlay() {

        parentView = new RelativeLayout(mContext);
        parentView.setClickable(true);
        parentView.setBackgroundColor(ContextCompat.getColor(mContext, R.color.backgroundColor));
        RelativeLayout.LayoutParams imageViewParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        parentView.setLayoutParams(imageViewParams);
        AppCompatImageView defaultIcon = new AppCompatImageView(mContext);
        defaultIcon.setPadding(30, 20, 30, 20);

        defaultIcon.setLayoutParams(imageViewParams);
        defaultIcon.setImageResource(R.drawable.logo);
        parentView.addView(defaultIcon);

        customMessageView = new AppCompatTextView(mContext);
        RelativeLayout.LayoutParams textViewParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        textViewParams.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE);// = Gravity.CENTER;
        customMessageView.setText(localisedStrings.getTouchToCastMsgText());
        customMessageView.setLayoutParams(textViewParams);
        customMessageView.setBackgroundColor(Color.parseColor("#CC000000"));
        customMessageView.setTextColor(Color.parseColor("#ffffff"));
        customMessageView.setTextSize(20);
//        customMessageView.setAlpha(0.7f);
        customMessageView.setTypeface(customMessageView.getTypeface(), Typeface.BOLD);
        customMessageView.setPadding(30, 20, 30, 20);
        parentView.addView(customMessageView);
        customMessageView.setOnClickListener((v) -> {

            if (CastingUtils.getRemoteMediaId(mContext) != null && onUpdatedContentDatum != null) {
                String filmId = CastingUtils.getRemoteMediaId(mContext);
                if (filmId.equalsIgnoreCase("") || (!filmId.equalsIgnoreCase(onUpdatedContentDatum.getGist().getId()))) {
                    if (onUpdatedContentDatum.getStreamingInfo() == null || onUpdatedContentDatum.getStreamingInfo().getVideoAssets() == null) {
                        onUpdatedContentDatum.setFromStandalone(true);
                        appCMSPresenter.refreshVideoData(onUpdatedContentDatum.getGist().getId(), new Action1<ContentDatum>() {
                            @Override
                            public void call(ContentDatum contentDatum) {
                                onUpdatedContentDatum = contentDatum;
                                sendMediaToRemote();
                            }
                        }, null, false, false, onUpdatedContentDatum);
                    } else {
                        sendMediaToRemote();
                    }
                }
            }
        });


        if (CastServiceProvider.getInstance(mContext).isCastingConnected()) {
            String filmId = CastingUtils.getRemoteMediaId(mContext);
            if (filmId.equalsIgnoreCase(""))
                customMessageView.setText(CastingUtils.getCurrentPlayingVideoName(mContext));
            else
                customMessageView.setText(localisedStrings.getCastMsgPrefixText() + CastingUtils.getCurrentPlayingVideoName(mContext) + localisedStrings.getCastMsgSuffixText() + CastServiceProvider.getInstance(mContext).getConnectedDeviceName());
        }

        parentView.setVisibility(View.GONE);
        this.addView(parentView);

    }

    private void sendMediaToRemote() {
        List<String> relateVideoIds = onUpdatedContentDatum.getContentDetails().getRelatedVideoIds();
        String currentVideotoCast = onUpdatedContentDatum.getGist().getId();
        if (this.binder == null) {
            this.binder = appCMSPresenter.getDefaultAppCMSVideoPageBinder(onUpdatedContentDatum,
                    -1,
                    onUpdatedContentDatum.getContentDetails().getRelatedVideoIds(),
                    false,
                    false,  /** TODO: Replace with a value that is true if the video is a trailer */
                    !appCMSPresenter.isAppSVOD(),
                    appCMSPresenter.getAppAdsURL(onUpdatedContentDatum),
                    appCMSPresenter.getAppBackgroundColor(), null);
        } else {
            binder.setContentData(onUpdatedContentDatum);
        }
        //In case we are casting from Playlist Page:
                    /*if (isFromPlayListPage) {
                        relateVideoIds = videoPlayListIds;
                        currentVideotoCast = videoPlayListIds.get(AppCMSPresenter.currentVideoPlayListIndex);
                    }*/

        CastHelper.getInstance(mContext).launchRemoteMedia(appCMSPresenter,
                relateVideoIds,
                currentVideotoCast,
                0,
                binder,
                true,
                onApplicationEnded -> {
                    System.out.println("videos finished");
                });
        //CastServiceProvider.getInstance(mContext).launchSingeRemoteMedia(onUpdatedContentDatum.getGist().getTitle(), permaLink, onUpdatedContentDatum.getGist().getVideoImageUrl(), lastUrl, onUpdatedContentDatum.getGist().getId(), 0, false);
    }


    public void setMute(boolean isMute) {
        if (isMute)
            player.setVolume(0f);
        else
            player.setVolume(1f);
    }


    public void initializeFutureContentTimerView(long eventDate, long remainingTime, boolean isEventSchedule, String coverImage) {
        ViewCreator.stopCountdownTimer();
        if (timerViewFutureContent == null) {
            timerViewFutureContent = new TimerViewFutureContent(mContext);
            timerViewFutureContent.initializeView(this, coverImage);

            //timerViewFutureContent.setVisibility(View.GONE);
            this.addView(timerViewFutureContent);
        }
        timerViewFutureContent.startTimer(mContext, eventDate, remainingTime, isEventSchedule);
    }

    public void initializeSettingButton() {

        setStreamingQualitySelector(this);
        setClosedCaptionsSelector(this);
        setVideoPlayerSettingsEvent(this);

        playerSettingsView = new PlayerSettingsView(mContext);
        playerSettingsView.initializeView();

        playerSettingsView.setVisibility(View.GONE);
        this.addView(playerSettingsView);

        mSettingButton = createSettingButton();
        //((RelativeLayout) getPlayerView().findViewById(R.id.exo_controller_container)).addView(mSettingButton);
        if (appCMSPresenter.getTemplateType() == AppCMSPresenter.TemplateType.NEWS)
            setmSettingButton(mSettingButton);
        if (mSettingButton != null) {
            mSettingButton.setOnClickListener(v -> {

/*
                if (availableClosedCaptions == null
                        && hlsListViewAdapter == null
                        && listViewAdapter == null) {
                    appCMSPresenter.showToast(localisedStrings.getPlayerSettingsUnavailbleText(), Toast.LENGTH_SHORT);
                } else if (videoPlayerSettingsEvent != null) {
                    if (streamingQualitySelector != null && hlsListViewAdapter != null*//*&& uri != null*//*) {
                 *//*int streamingQualityIndex = streamingQualitySelector.getMpegResolutionIndexFromUrl(uri.toString());

                        if (hlsListViewAdapter.getItemCount() > streamingQualityIndex)
                            hlsListViewAdapter.setSelectedIndex(streamingQualityIndex);
                        hlsListViewAdapter.notifyDataSetChanged();*//*
                        videoPlayerSettingsEvent.launchSetting(closedCaptionSelectorAdapter, hlsListViewAdapter, languageSelectorAdapter);
                    }
                    if (streamingQualitySelector != null && listViewAdapter != null && uri != null) {
                        int streamingQualityIndex = streamingQualitySelector.getMpegResolutionIndexFromUrl(uri.toString());

                        if (listViewAdapter.getItemCount() > streamingQualityIndex)
                            listViewAdapter.setSelectedIndex(streamingQualityIndex);
                        listViewAdapter.notifyDataSetChanged();
                        videoPlayerSettingsEvent.launchSetting(closedCaptionSelectorAdapter, listViewAdapter);
                    }

                    if (availableClosedCaptions != null) {
                        videoPlayerSettingsEvent.launchSetting(closedCaptionSelectorAdapter, listViewAdapter);
                    }

                    videoPlayerSettingsEvent.launchSetting(availableClosedCaptions, closedCaptionSelectorAdapter == null ? 0 : closedCaptionSelectorAdapter.getSelectedIndex(),
                            availableStreamingQualitiesHLS, hlsListViewAdapter == null ? 0 : hlsListViewAdapter.getSelectedIndex(),
                            availableStreamingQualities, listViewAdapter == null ? 0 : listViewAdapter.getSelectedIndex());

                } else {
                    appCMSPresenter.showToast(localisedStrings.getSomethingWentWrongText(), Toast.LENGTH_SHORT);
                }*/

            });
        }
    }

    View waysToWatchView;

    private void createPreviewMessageViewNew() {
        customPreviewContainer = null;
        customPreviewContainer = new LinearLayoutCompat(mContext);
        customPreviewContainer.setId(R.id.customPreviewContainer);
        customPreviewContainer.setOrientation(LinearLayoutCompat.VERTICAL);
        customPreviewContainer.setGravity(Gravity.CENTER);
        customPreviewContainer.setBackgroundColor(appCMSPresenter.getGeneralBackgroundColor());

        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (waysToWatchView == null)
            waysToWatchView = inflater.inflate(R.layout.ways_to_watch_standalone, null);
        ButterKnife.bind(this, waysToWatchView);
        setViewStyleWaysToWatch();
        customPreviewContainer.addView(waysToWatchView);
        this.addView(customPreviewContainer);
    }

    @Nullable
    @BindView(R.id.parentLayout)
    ConstraintLayout parentLayout;

    @Nullable
    @BindView(R.id.tveSvodButtons)
    ConstraintLayout tveSvodButtons;

    @Nullable
    @BindView(R.id.tvodButtons)
    ConstraintLayout tvodButtons;

    @Nullable
    @BindView(R.id.alreadyLogin)
    AppCompatTextView alreadyLogin;

    @Nullable
    @BindView(R.id.entitlementButton1)
    AppCompatButton entitlementButton1;

    @Nullable
    @BindView(R.id.entitlementButton2)
    AppCompatButton entitlementButton2;

    @Nullable
    @BindView(R.id.entitlementButton3)
    AppCompatButton entitlementButton3;

    @Nullable
    @BindView(R.id.entitlementButton4)
    AppCompatButton entitlementButton4;

    @Nullable
    @BindView(R.id.verticalGuidelineTvod)
    Guideline verticalGuidelineTvod;

    @Nullable
    @BindView(R.id.verticalGuideline)
    Guideline verticalGuideline;

    private void setViewStyleWaysToWatch() {
        parentLayout.setBackgroundColor(ViewCreator.getTransparentColor(appCMSPresenter.getGeneralBackgroundColor(), 0.8f));
        alreadyLogin.setTextColor(appCMSPresenter.getGeneralTextColor());
        alreadyLogin.setLinkTextColor(appCMSPresenter.getBrandPrimaryCtaColor());

        entitlementButton1.setBackground(CustomShape.createRoundedRectangleDrawable(appCMSPresenter.getBrandPrimaryCtaColor()));
        entitlementButton2.setBackground(CustomShape.createRoundedRectangleDrawable(appCMSPresenter.getBrandPrimaryCtaColor()));
        entitlementButton3.setBackground(CustomShape.createRoundedRectangleDrawable(appCMSPresenter.getBrandPrimaryCtaColor()));
        entitlementButton4.setBackground(CustomShape.createRoundedRectangleDrawable(appCMSPresenter.getBrandPrimaryCtaColor()));
        entitlementButton1.setTextColor(Color.parseColor(appCMSPresenter.getAppTextColor()));
        entitlementButton2.setTextColor(Color.parseColor(appCMSPresenter.getAppTextColor()));
        entitlementButton3.setTextColor(Color.parseColor(appCMSPresenter.getAppTextColor()));
        entitlementButton4.setTextColor(Color.parseColor(appCMSPresenter.getAppTextColor()));
        ViewCreator.setTypeFace(getContext(), appCMSPresenter, appCMSPresenter.getJsonValueKeyMap(), new Component(), alreadyLogin);
        ViewCreator.setTypeFace(getContext(), appCMSPresenter, appCMSPresenter.getJsonValueKeyMap(), new Component(), entitlementButton1);
        ViewCreator.setTypeFace(getContext(), appCMSPresenter, appCMSPresenter.getJsonValueKeyMap(), new Component(), entitlementButton2);
        ViewCreator.setTypeFace(getContext(), appCMSPresenter, appCMSPresenter.getJsonValueKeyMap(), new Component(), entitlementButton3);
        ViewCreator.setTypeFace(getContext(), appCMSPresenter, appCMSPresenter.getJsonValueKeyMap(), new Component(), entitlementButton4);
        setDataWaysToWatch();
    }

    private void setDataWaysToWatch() {
        alreadyLogin.setText(appCMSPresenter.getLocalisedStrings().getHaveAccountText() + " " + appCMSPresenter.getLocalisedStrings().getLoginText());
        entitlementButton1.setText(getContext().getString(R.string.rent));
        entitlementButton2.setText(getContext().getString(R.string.buy));
        if (appCMSPresenter.isUserSubscribed())
            entitlementButton3.setText(appCMSPresenter.getLocalisedStrings().getUpgradeSubscriptionText());
        else
            entitlementButton3.setText(appCMSPresenter.getLocalisedStrings().getBecomeMemberText());
        entitlementButton4.setText(appCMSPresenter.getLocalisedStrings().getChooseTVProviderText());
        ClickableSpan loginClick = new ClickableSpan() {
            @Override
            public void onClick(@NonNull View widget) {
                appCMSPresenter.navigateToLoginPage(false);
            }
        };

        appCMSPresenter.makeTextViewLinks(alreadyLogin, new String[]{appCMSPresenter.getLocalisedStrings().getLoginText()}, new ClickableSpan[]{loginClick}, true);
        if (appCMSPresenter.isUserLoggedIn())
            alreadyLogin.setVisibility(GONE);
        else
            alreadyLogin.setVisibility(VISIBLE);

    }

    @Optional
    @OnClick(R.id.entitlementButton1)
    void rentClick() {
        if (videoContentDatum.getSubscriptionPlans() != null) {
            ContentDatum selectedPlan = contentTypeChecker.tvodPlan(videoContentDatum.getSubscriptionPlans());
            appCMSPresenter.setContentToPurchase(new TvodPurchaseData(null, null, videoContentDatum.getGist().getId(), false, false, false, true, selectedPlan, videoContentDatum.getGist().getId(), videoContentDatum.getGist().getTitle()));
            appCMSPresenter.setLaunchType(AppCMSPresenter.LaunchType.TVOD_PURCHASE);
            appCMSPresenter.navigateToLoginPage(false);
        }

    }

    @Optional
    @OnClick(R.id.entitlementButton2)
    void buyClick() {

        if (videoContentDatum.getSubscriptionPlans() != null) {
            ContentDatum selectedPlan = contentTypeChecker.tvodPlan(videoContentDatum.getSubscriptionPlans());
            appCMSPresenter.setContentToPurchase(new TvodPurchaseData(null, null, videoContentDatum.getGist().getId(), false, false, false, false, selectedPlan, videoContentDatum.getGist().getId(), videoContentDatum.getGist().getTitle()));
            appCMSPresenter.setLaunchType(AppCMSPresenter.LaunchType.TVOD_PURCHASE);
            appCMSPresenter.navigateToLoginPage(false);
        }
    }

    @Optional
    @OnClick(R.id.entitlementButton3)
    void memberClick() {
        if (appPreference.getActiveSubscriptionProcessor() != null
                && !(appPreference.getActiveSubscriptionProcessor().toLowerCase().equalsIgnoreCase(mContext.getString(R.string.subscription_android_payment_processor).toLowerCase()) ||
                appPreference.getActiveSubscriptionProcessor().toLowerCase().equalsIgnoreCase(mContext.getString(R.string.subscription_android_payment_processor_friendly).toLowerCase()))) {
            appCMSPresenter.showEntitlementDialog(AppCMSPresenter.DialogType.CANNOT_UPGRADE_SUBSCRIPTION_CONTENT, null, null);
            return;
        }

        if (videoContentDatum.getSubscriptionPlans() != null) {
            appCMSPresenter.navigateToContentSubscription(videoContentDatum.getSubscriptionPlans());
            return;
        }
        appCMSPresenter.navigateToSubscriptionPlansPage(false);
    }

    @Optional
    @OnClick(R.id.entitlementButton4)
    void tveClick() {
        appCMSPresenter.setLoginFromNavPage(false);
        appCMSPresenter.openTvProviderScreen();
    }


    private void createPreviewMessageView() {
        int buttonColor, textColor;
        if (appCMSPresenter != null &&
                appCMSPresenter.getBrandPrimaryCtaTextColor() != 0 &&
                appCMSPresenter.getBrandPrimaryCtaColor() != 0) {
            buttonColor = appCMSPresenter.getBrandPrimaryCtaColor();
            textColor = appCMSPresenter.getBrandPrimaryCtaTextColor();
        } else {
            buttonColor = Color.parseColor(String.valueOf(R.color.colorAccent));
            textColor = Color.parseColor("#ffffff");
        }

        customPreviewContainer = null;
        previewCustomMessageView = null;
        btnStartFreeTrial = null;
        btnLogin = null;

        customPreviewContainer = new LinearLayoutCompat(mContext);
        customPreviewContainer.setId(R.id.customPreviewContainer);
        customPreviewContainer.setOrientation(LinearLayoutCompat.VERTICAL);
        customPreviewContainer.setGravity(Gravity.CENTER);
        customPreviewContainer.setBackgroundColor(appCMSPresenter.getGeneralBackgroundColor());
        previewCustomMessageView = new AppCompatTextView(mContext);
        previewCustomMessageView.setId(R.id.previewCustomMessageView);
        LinearLayoutCompat.LayoutParams textViewParams = new LinearLayoutCompat.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        textViewParams.gravity = Gravity.CENTER;
        String message = null;
        if (appCMSPresenter != null &&
                appCMSPresenter.getAppCMSAndroid().getSubscriptionFlowContent() != null &&
                appCMSPresenter.getAppCMSAndroid().getSubscriptionFlowContent().getOverlayMessage() != null) {
            message = appCMSPresenter.getAppCMSAndroid().getSubscriptionFlowContent().getOverlayMessage();
        } else {
            message = localisedStrings.getPremiumContentGuestUserDialogMessageText();
        }

        if (appCMSPresenter.isShowDialogForWebPurchase()) {
            if (appCMSPresenter.isUserLoggedIn()) {
                message = localisedStrings.getPremiumLoggedInUserMsg();
            } else {
                message = localisedStrings.getPremiumContentGuestUserDialogMessageText();
            }
        }
        previewCustomMessageView.setGravity(TEXT_ALIGNMENT_CENTER);
        previewCustomMessageView.setText(message);
        previewCustomMessageView.setLayoutParams(textViewParams);
        previewCustomMessageView.setTextColor(appCMSPresenter.getGeneralTextColor());
        previewCustomMessageView.setTextSize(15);
        previewCustomMessageView.setPadding(20, 20, 20, 20);
        LinearLayoutCompat.LayoutParams buttonParams = new LinearLayoutCompat.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        buttonParams.setMargins(5, 5, 5, 5);

        previewBtnsLayout = new LinearLayoutCompat(mContext);
        previewBtnsLayout.setOrientation(LinearLayoutCompat.HORIZONTAL);
        previewBtnsLayout.setGravity(Gravity.CENTER);

        btnStartFreeTrial = new AppCompatButton(mContext);
        btnStartFreeTrial.setBackgroundColor(buttonColor);
        if (appCMSPresenter.getAppCMSMain().isForceLogin() && !appCMSPresenter.isUserLoggedIn()
                && CommonUtils.isFreeContent(appCMSPresenter.getCurrentContext(), onUpdatedContentDatum)) {
            btnStartFreeTrial.setText(appCMSPresenter.getLocalisedStrings().getSignUpText());
        } else if (appCMSPresenter.getAppCMSAndroid() != null && appCMSPresenter.getAppCMSAndroid().getSubscriptionFlowContent() != null
                && appCMSPresenter.getAppCMSAndroid().getSubscriptionFlowContent().getSubscriptionButtonText() != null) {
            btnStartFreeTrial.setText(appCMSPresenter.getAppCMSAndroid().getSubscriptionFlowContent().getSubscriptionButtonText());
        } else {
            btnStartFreeTrial.setText(localisedStrings.getSubscribeNowText());
        }
        btnStartFreeTrial.setTextColor(textColor);
        btnStartFreeTrial.setPadding(10, 10, 10, 10);
        btnStartFreeTrial.setLayoutParams(buttonParams);

        btnStartFreeTrial.setGravity(Gravity.CENTER);

        btnStartFreeTrial.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                /**
                 * Commented due to KRON-407
                 */
                if (appCMSPresenter.getAppCMSMain().isForceLogin() && !appCMSPresenter.isUserLoggedIn()
                        && CommonUtils.isFreeContent(appCMSPresenter.getCurrentContext(), onUpdatedContentDatum)) {
                    appCMSPresenter.setLaunchType(AppCMSPresenter.LaunchType.SIGNUP);
                    appCMSPresenter.navigateToLoginPage(false);
                } else {
                    //Call exitFullScreenPlayer() to Resolved KRON-210
                    // appCMSPresenter.setEntitlementPendingVideoData(null);
                    appCMSPresenter.navigateToSubscriptionPlansPage(false);
                    appCMSPresenter.exitFullScreenPlayer();
                }

            }
        });
        btnLogin = new AppCompatButton(mContext);
        /*if (appCMSPresenter.getAppCMSAndroid() != null && appCMSPresenter.getAppCMSAndroid().getSubscriptionFlowContent() != null
                && appCMSPresenter.getAppCMSAndroid().getSubscriptionFlowContent().getLoginButtonText() != null) {
            btnLogin.setText(appCMSPresenter.getAppCMSAndroid().getSubscriptionFlowContent().getLoginButtonText());
        } else {
            btnLogin.setText(localisedStrings.getSignInText());
        }*/
        btnLogin.setText(localisedStrings.getSignInText());
        btnLogin.setBackgroundColor(buttonColor);
        btnLogin.setTextColor(textColor);
        btnLogin.setPadding(10, 10, 10, 10);
        btnLogin.setGravity(Gravity.CENTER);
        btnLogin.setLayoutParams(buttonParams);

        /**
         * if we need to show web flow message then hide subscribe btn and
         * show login if not login
         */
        if (appCMSPresenter.isShowDialogForWebPurchase()) {
            btnStartFreeTrial.setVisibility(View.GONE);

            if (!appCMSPresenter.isUserLoggedIn()) {
                btnLogin.setVisibility(View.VISIBLE);
            } else {
                btnLogin.setVisibility(View.GONE);
            }
        }

        btnLogin.setOnClickListener(view -> {
            //Call exitFullScreenPlayer() to Resolved KRON-210
            appCMSPresenter.setLaunchType(AppCMSPresenter.LaunchType.LOGIN_AND_SIGNUP);
            // appCMSPresenter.setEntitlementPendingVideoData(null);
            appCMSPresenter.navigateToLoginPage(false);
            appCMSPresenter.exitFullScreenPlayer();
        });


        previewBtnsLayout.addView(btnLogin);
        previewBtnsLayout.addView(btnStartFreeTrial);

        customPreviewContainer.addView(previewCustomMessageView);
        customPreviewContainer.addView(previewBtnsLayout);

        customPreviewContainer.setVisibility(View.INVISIBLE);
        this.addView(customPreviewContainer);
    }

    AppCompatImageButton mediaButton, app_cms_video_player_done_button;
    String fontColor = "#ffffffff";

    private void createTopBarView() {
        llTopBar = new LinearLayoutCompat(mContext);
        llTopBar.setGravity(Gravity.TOP);
        LinearLayoutCompat.LayoutParams llParams = new LinearLayoutCompat.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        LayoutInflater li = LayoutInflater.from(mContext);
        View layout = li.inflate(R.layout.custom_video_player_top_bar, null, false);
        mediaButton = layout.findViewById(R.id.media_route_button);
        ConstraintLayout parentLayout = layout.findViewById(R.id.parentTopLayout);
        if (appCMSPresenter.getTemplateType() == AppCMSPresenter.TemplateType.NEWS)
            parentLayout.setBackgroundColor(ContextCompat.getColor(mContext, android.R.color.transparent));
        app_cms_video_player_title_view = layout.findViewById(R.id.app_cms_mini_video_player_title_view);
        app_cms_video_player_done_button = layout.findViewById(R.id.app_cms_video_player_done_button);
        app_cms_video_player_done_button.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                // appCMSPresenter.setAppOrientation();
                if (episodePlay)
                    appCMSPresenter.exitFullScreenEpisodePlayer();
                else
                    appCMSPresenter.exitFullScreenPlayer();
                mToggleButton.setChecked(false);
            }
        });

        app_cms_video_player_done_button.setColorFilter(Color.parseColor(fontColor));
        mediaButton.setColorFilter(Color.parseColor(fontColor));
        setBackgroundColor(appCMSPresenter.getGeneralBackgroundColor());

        layout.setLayoutParams(llParams);

        llTopBar.setLayoutParams(llParams);
        llTopBar.addView(layout);
        llTopBar.setVisibility(View.VISIBLE);
        if (appCMSPresenter != null && appCMSPresenter.isCastEnable()) {
            CastServiceProvider.getInstance(mContext).setVideoPlayerMediaButton(mediaButton);

            mediaButton.setVisibility(VISIBLE);
        }

        llTopBar.setVisibility(View.INVISIBLE);
        this.addView(llTopBar);
    }

    public void disableTopBar() {
        llTopBar.setVisibility(GONE);
    }

    private void showProgressBar(String text) {
        if (null != customLoaderContainer && null != loaderMessageView) {
            loaderMessageView.setText(text);
            loaderMessageView.setTextColor(getResources().getColor(android.R.color.white));
            customLoaderContainer.setVisibility(View.VISIBLE);
        }
    }

    private void hideProgressBar() {
        if (null != customLoaderContainer) {
            customLoaderContainer.setVisibility(View.INVISIBLE);
        }
    }

    private void showRestrictMessage(String message) {
        if (null != customMessageContainer && null != customMessageView) {
            disableController();
            hideProgressBar();
            loaderMessageView.setTextColor(getResources().getColor(android.R.color.white));
            customMessageView.setText(appCMSPresenter.getLanguageResourcesFile().getUIresource(message));
            customMessageContainer.setVisibility(View.VISIBLE);
        }
    }

    private void hideRestrictedMessage() {
        if (null != customMessageContainer) {
            enableController();
            customMessageContainer.setVisibility(View.GONE);
        }
    }

    protected AppCompatToggleButton createFullScreenToggleButton() {
        /*mToggleButton = new ToggleButton(getContext());
        RelativeLayout.LayoutParams toggleLP = new RelativeLayout.LayoutParams(BaseView.dpToPx(R.dimen.app_cms_video_controller_cc_width, getContext()), BaseView.dpToPx(R.dimen.app_cms_video_controller_cc_width, getContext()));
        toggleLP.addRule(RelativeLayout.ABOVE, R.id.seek_bar_parent);
        toggleLP.addRule(RelativeLayout.CENTER_VERTICAL);
        toggleLP.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        toggleLP.setMarginStart(BaseView.dpToPx(R.dimen.app_cms_video_controller_cc_left_margin, getContext()));
        mToggleButton.setLayoutParams(toggleLP);*/
        mToggleButton = playerView.findViewById(R.id.playerFullScreenButton);
        mToggleButton.setVisibility(VISIBLE);
        mToggleButton.setChecked(false);
        mToggleButton.setTextOff("");
        mToggleButton.setTextOn("");
        mToggleButton.setText("");
        mToggleButton.setBackgroundDrawable(getResources().getDrawable(R.drawable.full_screen_toggle_selector, null));
        mToggleButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!appCMSPresenter.isFullScreenVisible) {
                    appCMSPresenter.isExitFullScreen = true;
                } else {
                    appCMSPresenter.isExitFullScreen = false;

                }
            }
        });
        mToggleButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                //todo work on maximizing the player on this event
                if (isChecked) {
                    if (BaseView.isTablet(appCMSPresenter.getCurrentContext())) {
                        mZoomButton.setVisibility(View.VISIBLE);
                        hideRelatedVideoView(true);
                        updateWatchedHistory();
                        if (seasonEpisodeView != null && seasonEpisodeView.getVisibility() == View.VISIBLE && playerLeftOutScreen != null) {
                            removeRelatedVideoView();
                        }

                        if (episodePlay)
                            appCMSPresenter.showFullScreenEpisodePlayer();
                        else
                            appCMSPresenter.showFullScreenPlayer();
                    } else {
                        appCMSPresenter.restrictLandscapeOnly();
                    }
                } else {
                    if (BaseView.isTablet(appCMSPresenter.getCurrentContext())) {
                        appCMSPresenter.isFullScreenVisible = false;
                        setPreviousNextVisibility(false);
                        if (seasonEpisodeView != null && seasonEpisodeView.getVisibility() == View.VISIBLE && playerLeftOutScreen != null) {
                            removeRelatedVideoView();
                        }
                        llTopBar.setVisibility(View.GONE);
                        // appCMSPresenter.setAppOrientation();
                        if (episodePlay)
                            appCMSPresenter.exitFullScreenEpisodePlayer();
                        else
                            appCMSPresenter.exitFullScreenPlayer();

                        mZoomButton.setVisibility(View.GONE);
                        hideRelatedVideoView(false);
                        updateWatchedHistory();
                    } else {
                        if (appCMSPresenter.isFullScreenVisible) {
                            appCMSPresenter.exitFullScreenPlayer();
                        }
                        appCMSPresenter.restrictPortraitOnly();
                    }
                }
            }
        });

        return mToggleButton;
    }

    protected AppCompatToggleButton createZoomToogleButton() {
        mZoomButton = playerView.findViewById(R.id.playerZoomButton);
        mZoomButton.setOnClickListener(v -> {
            if (player != null && (player.getPlaybackState() == Player.STATE_IDLE || player.getPlaybackState() == STATE_BUFFERING)) {
                mZoomButton.setChecked(!mZoomButton.isChecked());
                return;
            }
            int resizeModeToSet = playerView.getResizeMode() == AspectRatioFrameLayout.RESIZE_MODE_ZOOM ? AspectRatioFrameLayout.RESIZE_MODE_FIT : AspectRatioFrameLayout.RESIZE_MODE_ZOOM;
            playerView.setResizeMode(resizeModeToSet);

        });
        return mZoomButton;
    }

    @Override
    protected AppCompatImageButton createSettingButton() {
        /*mSettingButton = new ImageButton(getContext());
        RelativeLayout.LayoutParams toggleLP = new RelativeLayout.LayoutParams(BaseView.dpToPx(R.dimen.app_cms_video_controller_cc_width, getContext()), BaseView.dpToPx(R.dimen.app_cms_video_controller_cc_width, getContext()));
        toggleLP.addRule(RelativeLayout.ABOVE, R.id.seek_bar_parent);
        toggleLP.addRule(RelativeLayout.CENTER_VERTICAL);
        toggleLP.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
        toggleLP.setMarginStart(BaseView.dpToPx(R.dimen.app_cms_video_controller_cc_left_margin, getContext()));
        toggleLP.setMarginEnd(BaseView.dpToPx(R.dimen.app_cms_video_controller_cc_left_margin, getContext()));
        mSettingButton.setLayoutParams(toggleLP);
        mSettingButton.setId(R.id.videoPlayerSettingButton);
        mSettingButton.setBackground(getResources().getDrawable(R.drawable.ic_settings_24dp, null));*/
        mSettingButton = playerView.findViewById(R.id.playerSettingButton);
        //mSettingButton.setVisibility(GONE);
        return mSettingButton;
    }


    public void updateFullscreenButtonState(int screenMode) {
        switch (screenMode) {
            case Configuration.ORIENTATION_LANDSCAPE:
                mFullScreenButton.setChecked(true);
                break;
            case Configuration.ORIENTATION_PORTRAIT:
                mFullScreenButton.setChecked(false);
                break;
            default:
                mFullScreenButton.setChecked(true);
                break;
        }
    }

    private void getPermalink(ContentDatum contentDatum) {
        if (contentDatum != null) {
            if (contentDatum.getStreamingInfo() != null) {
                isLiveStream = contentDatum.getStreamingInfo().isLiveStream();
            }
            if (!isLiveStream && playerVolumeButton != null)
                playerVolumeButton.setVisibility(GONE);

            //if (!isLiveStream && !appCMSPresenter.isUserSubscribed()) {
            if (!appCMSPresenter.isUserSubscribed()) {
                //adsUrl = appCMSPresenter.getAdsUrl(appCMSPresenter.getPermalinkCompletePath(contentDatum.getGist().getPermalink()));
                adsUrl = appCMSPresenter.getAppAdsURL(contentDatum);
            }
        }

        shouldRequestAds = adsUrl != null && !TextUtils.isEmpty(adsUrl);
        isAdDisplayed = false;

    }

    public void setFirebaseProgressHandling() {
        mProgressHandler = new Handler();
        mProgressRunnable = new Runnable() {
            @Override
            public void run() {
                mProgressHandler.removeCallbacks(this);
                long totalVideoDurationMod4 = mTotalVideoDuration / 4;
                if (totalVideoDurationMod4 > 0) {
                    long mPercentage = (long)
                            (((float) (getCurrentPosition() / 1000) / mTotalVideoDuration) * 100);
                    if (appCMSPresenter.getmFireBaseAnalytics() != null) {
                        sendProgressAnalyticEvents(mPercentage);
                    }
                }
                mProgressHandler.postDelayed(this, 1000);
            }
        };
    }

    public void sendProgressAnalyticEvents(long progressPercent) {
        Bundle bundle = new Bundle();
        bundle.putString(FIREBASE_VIDEO_ID_KEY, videoDataId);
        bundle.putString(FIREBASE_VIDEO_NAME_KEY, videoTitle);
        bundle.putString(FIREBASE_PLAYER_NAME_KEY, FIREBASE_PLAYER_NATIVE);
        bundle.putString(FIREBASE_MEDIA_TYPE_KEY, FIREBASE_MEDIA_TYPE_VIDEO);
        //Logs an app event.
        if (progressPercent == 0 && !isStreamStart) {
            isStreamStart = true;
            appCMSPresenter.getmFireBaseAnalytics().logEvent(FIREBASE_STREAM_START, bundle);
            appCMSPresenter.getAppPreference().setVideoWatchCount();
        }

        if (!isStreamStart) {
            isStreamStart = true;
            appCMSPresenter.getmFireBaseAnalytics().logEvent(FIREBASE_STREAM_START, bundle);
        }

        if (progressPercent >= 25 && progressPercent < 50 && !isStream25) {
            if (!isStreamStart) {
                appCMSPresenter.getmFireBaseAnalytics().logEvent(FIREBASE_STREAM_START, bundle);
                appCMSPresenter.getAppPreference().setVideoWatchCount();
                isStreamStart = true;
            }

            isStream25 = true;
            appCMSPresenter.getmFireBaseAnalytics().logEvent(FIREBASE_STREAM_25, bundle);
        }

        if (progressPercent >= 50 && progressPercent < 75 && !isStream50) {
            if (!isStream25) {
                isStream25 = true;
                appCMSPresenter.getmFireBaseAnalytics().logEvent(FIREBASE_STREAM_25, bundle);
            }

            isStream50 = true;
            appCMSPresenter.getmFireBaseAnalytics().logEvent(FIREBASE_STREAM_50, bundle);
        }

        if (progressPercent >= 75 && progressPercent <= 100 && !isStream75) {
            if (!isStream25) {
                isStream25 = true;
                appCMSPresenter.getmFireBaseAnalytics().logEvent(FIREBASE_STREAM_25, bundle);
            }

            if (!isStream50) {
                isStream50 = true;
                appCMSPresenter.getmFireBaseAnalytics().logEvent(FIREBASE_STREAM_50, bundle);
            }

            isStream75 = true;
            appCMSPresenter.getmFireBaseAnalytics().logEvent(FIREBASE_STREAM_75, bundle);
        }

        if (progressPercent >= 98 && progressPercent <= 100 && !isStream100) {
            if (!isStream25) {
                isStream25 = true;
                appCMSPresenter.getmFireBaseAnalytics().logEvent(FIREBASE_STREAM_25, bundle);
            }

            if (!isStream50) {
                isStream50 = true;
                appCMSPresenter.getmFireBaseAnalytics().logEvent(FIREBASE_STREAM_50, bundle);
            }

            if (!isStream75) {
                isStream75 = true;
                appCMSPresenter.getmFireBaseAnalytics().logEvent(FIREBASE_STREAM_75, bundle);
            }

            isStream100 = true;
            appCMSPresenter.getmFireBaseAnalytics().logEvent(FIREBASE_STREAM_100, bundle);

        }
    }

    private void setWatchedTime(ContentDatum contentDatum) {
        if (contentDatum != null) {
            if (appCMSPresenter != null &&
                    appCMSPresenter.getCurrentPageName() != null &&
                    !TextUtils.isEmpty(appCMSPresenter.getCurrentPageName()) &&
                    appCMSPresenter.getCurrentPageName().equalsIgnoreCase("Video Page") &&
                    watchedPercentageVideoPage != 0) {
                watchedPercentage = watchedPercentageVideoPage;
                watchedPercentageVideoPage = 0l;
            } else if (contentDatum.getGist().getWatchedPercentage() > 0) {
                watchedPercentage = contentDatum.getGist().getWatchedPercentage();
                watchedTime = contentDatum.getGist().getWatchedTime();
            } else {
                long watchedTime = contentDatum.getGist().getWatchedTime();
                long runTime = contentDatum.getGist().getRuntime();
                if (watchedTime > 0 && runTime > 0) {
                    watchedPercentage = (long) (((double) watchedTime / (double) runTime) * 100.0);
                }
                this.watchedTime = contentDatum.getGist().getWatchedTime();
            }
        }
    }

    @Override
    public void onPlayerError(ExoPlaybackException e) {
        String errorString = null;
        e.printStackTrace();
        if (e instanceof ExoPlaybackException) {
            errorString = e.getCause().toString();
            if (lastUrl != null) {
                updateToken(Uri.parse(lastUrl), closedCaptionUri == null ? null : Uri.parse(String.valueOf(closedCaptionUri)));
            }
//            setUri(Uri.parse(lastUrl), closedCaptionUri == null ? null : Uri.parse(String.valueOf(closedCaptionUri)));
        }
        if (e.type == ExoPlaybackException.TYPE_RENDERER) {
            Exception cause = e.getRendererException();
            if (cause instanceof MediaCodecRenderer.DecoderInitializationException) {
                // Special case for decoder initialization failures.
                MediaCodecRenderer.DecoderInitializationException decoderInitializationException =
                        (MediaCodecRenderer.DecoderInitializationException) cause;
                if (decoderInitializationException.diagnosticInfo == null) {
                    if (decoderInitializationException.getCause() instanceof MediaCodecUtil.DecoderQueryException) {
                        errorString = mContext.getString(R.string.error_querying_decoders);
                    } else if (decoderInitializationException.secureDecoderRequired) {
                        errorString = mContext.getString(R.string.error_no_secure_decoder,
                                decoderInitializationException.mimeType);
                    } else {
                        errorString = mContext.getString(R.string.error_no_decoder,
                                decoderInitializationException.mimeType);
                    }
                } else {
                    errorString = mContext.getString(R.string.error_instantiating_decoder,
                            decoderInitializationException.diagnosticInfo);
                }
            }
        } else if (e.type == ExoPlaybackException.TYPE_SOURCE) {
            // MappingTrackSelector.SelectionOverride override = new MappingTrackSelector.SelectionOverride(FIXED_FACTORY, 0, currentTrackIndex++);
            DefaultTrackSelector.SelectionOverride override = new DefaultTrackSelector.SelectionOverride(0, currentTrackIndex++);
            MappingTrackSelector.MappedTrackInfo currentMappedTrackInfo = trackSelector.getCurrentMappedTrackInfo();
            if (currentMappedTrackInfo != null
                    && currentMappedTrackInfo.getTrackGroups(0) != null
                    && currentMappedTrackInfo.getTrackGroups(0).get(0) != null
                    && (currentTrackIndex <= currentMappedTrackInfo.getTrackGroups(0).get(0).length)) {
                if ((player.getCurrentPosition() + 5000) >= player.getDuration()) {
                    if (appCMSPresenter.isNetworkConnected()) {
                        currentPlayingIndex++;
                        Toast.makeText(mContext, "There is some video playback error", Toast.LENGTH_LONG).show();
                    }
                } else {
                    if (appCMSPresenter.isNetworkConnected()) {
                        if (isLiveStream && appCMSPresenter.getIsMiniPlayerPlaying())
                            resumePlayer();
                        else {
                            trackSelector.setSelectionOverride(0, currentMappedTrackInfo.getTrackGroups(0), override);
                            init(mContext);
                        }
                    } else {
                        if (isBehindLiveWindow(e)) {
                            init(mContext);
                            if (lastUrl != null)
                                updateToken(Uri.parse(lastUrl), Uri.parse(closedCaptionUri));
                        }
                    }
                }
            }
        } else {

            if (errorString != null) {
                Toast.makeText(mContext, errorString, Toast.LENGTH_LONG).show();
            }
            if (isBehindLiveWindow(e)) {
                init(mContext);
                updateToken(Uri.parse(lastUrl), Uri.parse(closedCaptionUri));
            }
        }
        Log.e("Playback exception", errorString);

    }


    private void setBeaconData() {
        try {
            mStreamId = appCMSPresenter.getStreamingId(videoDataId);
        } catch (Exception e) {
            e.printStackTrace();
            mStreamId = videoDataId + appCMSPresenter.getCurrentTimeStamp();
        }
        beaconBufferingThread.setBeaconData(videoDataId, permaLink, mStreamId);
        beaconMessageThread.setBeaconData(videoDataId, permaLink, mStreamId);
    }

    public boolean isLiveStream() {
        return isLiveStream;
    }

    public interface IgetPlayerEvent {

        void getIsVideoPaused(boolean isVideoPaused);
    }

    public ViewCreator.VideoPlayerContent getVideoPlayerContent() {
        return videoPlayerContent;
    }

    private void sendAdRequest() {
        if (!TextUtils.isEmpty(mStreamId) && appCMSPresenter != null && appCMSPresenter.getAppCMSMain().getFeatures().getAnalyticsBeacon().isEnabled()) {
            appCMSPresenter.sendBeaconMessage(videoDataId,
                    permaLink,
                    parentScreenName,
                    getCurrentPosition(),
                    false,
                    AppCMSPresenter.BeaconEvent.AD_REQUEST,
                    "Video",
                    getBitrate() != 0 ? String.valueOf(getBitrate()) : null,
                    String.valueOf(getVideoHeight()),
                    String.valueOf(getVideoWidth()),
                    mStreamId,
                    0d,
                    apod,
                    isVideoDownloaded);
        }
    }

    private void sendAdImpression() {
        if (beaconMessageThread != null) {
            beaconMessageThread.sendBeaconPing = false;
        }
        if (appCMSPresenter != null && appCMSPresenter.getAppCMSMain().getFeatures().getAnalyticsBeacon().isEnabled()) {
            appCMSPresenter.sendBeaconMessage(videoDataId,
                    permaLink,
                    parentScreenName,
                    getCurrentPosition(),
                    false,
                    AppCMSPresenter.BeaconEvent.AD_IMPRESSION,
                    "Video",
                    getBitrate() != 0 ? String.valueOf(getBitrate()) : null,
                    String.valueOf(getVideoHeight()),
                    String.valueOf(getVideoWidth()),
                    mStreamId,
                    0d,
                    apod,
                    isVideoDownloaded);
        }
    }

    private void updateToken(Uri videoUri, Uri closedCaptionUri) {

        appCMSPresenter.refreshVideoData(videoDataId, contentDatum -> {
            {
                updateSignatureCookies(contentDatum.getAppCMSSignedURLResult().getPolicy(),
                        contentDatum.getAppCMSSignedURLResult().getSignature(),
                        contentDatum.getAppCMSSignedURLResult().getKeyPairId());
                setPolicyCookie(contentDatum.getAppCMSSignedURLResult().getPolicy());
                setSignatureCookie(contentDatum.getAppCMSSignedURLResult().getSignature());
                setKeyPairIdCookie(contentDatum.getAppCMSSignedURLResult().getKeyPairId());

                setAdsUrl(adsUrl);
                setUri(Uri.parse(lastUrl), closedCaptionUri == null ? null : Uri.parse(String.valueOf(closedCaptionUri)));

            }
        }, null, false, false, null);
    }


    public void initiateStreamingId() {
        try {
            mStreamId = appCMSPresenter.getStreamingId(videoDataId);
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
            mStreamId = videoDataId + appCMSPresenter.getCurrentTimeStamp();
        }

    }

    @Override
    public void sendBeaconAdImpression() {
        sendAdImpression();
    }

    @Override
    public void sendBeaconAdRequest() {
        sendAdRequest();
    }

    @Override
    public void setPlayerCurrentPostionAfterAds() {
       /* long updatedRunTime = 0;
        try {
            updatedRunTime = getDuration() / 1000;
        } catch (Exception e) {
            e.printStackTrace();
        }
        videoPlayTime = appCMSPresenter.setCurrentWatchProgress(updatedRunTime, watchedTime);

        if (!isVideoLoaded) {
            setCurrentPosition(videoPlayTime * SECS_TO_MSECS);
            if (!isTrailer && !isLiveStream) {
                appCMSPresenter.updateWatchedTime(videoDataId, getCurrentPosition() / 1000);
            }
            isVideoLoaded = true;
        }*/
    }

    AppCompatImageButton playerVolumeButton;

    public void createMuteView() {
        audioManager = (AudioManager) appCMSPresenter.getCurrentActivity().getSystemService(Context.AUDIO_SERVICE);
        if (appCMSPresenter.getPlatformType() == AppCMSPresenter.PlatformType.ANDROID) {
            playerVolume = playerView.findViewById(R.id.playerVolume);

            if (mFullScreenButton != null) {
                /*RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams)playerVolume.getLayoutParams();
                lp.addRule(RelativeLayout.ALIGN_RIGHT,mFullScreenButton.getId());
                lp.setMargins(0,0,60,20);*/
            }
        }
        hidePlayerVolumeView(true);
        if (playerVolume != null) {
            setPlyerVoumeButtom(playerVolume);
            playerVolume.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (playerVolume.isSelected()) {
                        playerVolume.setSelected(false);
                    } else {
                        playerVolume.setSelected(true);
                    }
                    setPlayerVolumeImage(playerVolume, false);
                }
            });
            setPlayerVolumeImage(playerVolume, true);
        }

    }


    public RelativeLayout createMuteNotifyView() {
        setMute(true);
        int bgColor = 0;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            bgColor = mContext.getResources().getColor(R.color.semiTransparentColor, null);
        } else {
            bgColor = mContext.getResources().getColor(R.color.semiTransparentColor);
        }

        int bottomMargin = playerView.findViewById(R.id.exo_controller_container).getHeight();


        muteNotifyView = new RelativeLayout(mContext);
        muteNotifyView.setId(R.id.layoutMuteNotification);
        muteNotifyView.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        muteNotifyView.setBackgroundColor(ContextCompat.getColor(getContext(), android.R.color.transparent));

        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(BaseView.dpToPx(R.dimen.app_cms_video_controller_cc_width, getContext()), BaseView.dpToPx(R.dimen.app_cms_video_controller_cc_width, getContext()));


        playerVolumeButton = new AppCompatImageButton(mContext);
        playerVolumeButton.setId(R.id.imageButtonMute);
        playerVolumeButton.setLayoutParams(lp);
        playerVolumeButton.setImageResource(R.drawable.player_mute);
        playerVolumeButton.setSelected(true);
        playerVolumeButton.setColorFilter(Color.parseColor(appCMSPresenter.getAppCtaBackgroundColor()), android.graphics.PorterDuff.Mode.SRC_IN);
        playerVolumeButton.setBackgroundColor(ContextCompat.getColor(getContext(), android.R.color.transparent));
        playerVolumeButton.setOnClickListener(view -> {
            // TODO: 22/02/19 We may add it later

            hideMuteNotifyView();
            if (playerVolumeButton.isSelected()) {
                playerVolumeButton.setSelected(false);
            } else {
                playerVolumeButton.setSelected(true);
            }
            setPlayerVolumeImage(playerVolumeButton, false);

        });

        lp.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, playerVolumeButton.getId());
        lp.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, playerVolumeButton.getId());
        lp.bottomMargin = bottomMargin >= 0 ? bottomMargin : 100;
        // lp.addRule(RelativeLayout.CENTER_VERTICAL,imageButtonMute.getId());


        muteNotifyView.addView(playerVolumeButton);

        isMuteNotifyVisible = true;

        return muteNotifyView;
    }

    public void updateVolumeIcon(boolean mute) {
        if (playerVolumeButton != null) {
            if (!mute) {
                playerVolumeButton.setSelected(true);
                playerVolumeButton.setImageResource(R.drawable.player_volume);
            } else {
                playerVolumeButton.setSelected(false);
                playerVolumeButton.setImageResource(R.drawable.player_mute);
            }
        }
    }

    public void hideMuteNotifyView() {
        if (muteNotifyView != null) {
            muteNotifyView.setVisibility(GONE);
            isMuteNotifyVisible = false;
            setMute(false);
        }
    }

    public void setUseController(boolean useController) {
        this.useController = useController;
    }

    boolean useController;

    boolean useAdUrl = true;

    public void setUseAdUrl(boolean useAdUrl) {
        this.useAdUrl = useAdUrl;
    }

    public void checkPPVPlayState() {
        appCMSPresenter.getTransactionData(onUpdatedContentDatum.getGist().getId(), updatedContentDatum -> {
            appCMSPresenter.stopLoader();
            customPreviewContainer.setVisibility(View.GONE);
            boolean isPlayable = true;
            long expirationDate = 0;
            boolean isPurchased = false;
            AppCMSTransactionDataValue objTransactionData = null;
            String msg = "";

            if (updatedContentDatum != null &&
                    updatedContentDatum.size() > 0) {
                if (updatedContentDatum.get(0).size() == 0) {
                    isPlayable = false;
                } else {
                    isPurchased = true;
                    objTransactionData = updatedContentDatum.get(0).get(onUpdatedContentDatum.getGist().getId());

                    /**
                     * get the transaction end date and compare with current time if end date is less than current date
                     * playable will be false
                     */
                    expirationDate = objTransactionData.getTransactionEndDate();
                }
            } else {
                isPlayable = false;
            }

            long timeToExpire = CommonUtils.getTimeIntervalForEvent(expirationDate * 1000, "EEE MMM dd HH:mm:ss");
            long eventDate = (onUpdatedContentDatum.getGist().getScheduleStartDate());
            //calculate remaining time from event date and current date
            long remainingTime = CommonUtils.getTimeIntervalForEvent(eventDate, "yyyy MMM dd HH:mm:ss");
            boolean isEventSchedule = false;
            String coverImage = null;

            if (onUpdatedContentDatum.getGist() != null
                    && onUpdatedContentDatum.getGist().getEventSchedule() != null
                    && onUpdatedContentDatum.getGist().getEventSchedule().size() > 0
                    && onUpdatedContentDatum.getGist().getEventSchedule().get(0) != null
                    && onUpdatedContentDatum.getGist().getEventSchedule().get(0).getEventTime() != 0) {
                eventDate = onUpdatedContentDatum.getGist().getEventSchedule().get(0).getEventTime();

                //calculate remaining time from event date and current date
                remainingTime = CommonUtils.getTimeIntervalForEventSchedule(eventDate * 1000L, "EEE MMM dd HH:mm:ss");
                isEventSchedule = true;
            }


            /**
             * If pricing type is svod+tvod or svod+tvod then check if user is subscribed or
             * content is purchased by user ,if both conditions are false then show subscribe message.
             */
            if (onUpdatedContentDatum.getPricing().getType().equalsIgnoreCase(mContext.getString(R.string.PURCHASE_TYPE_SVOD_TVOD))
                    || onUpdatedContentDatum.getPricing().getType().equalsIgnoreCase(mContext.getString(R.string.PURCHASE_TYPE_SVOD_PPV))) {


                if ((appCMSPresenter.isUserSubscribed() || isPurchased)
                        && videoDataId.equalsIgnoreCase(onUpdatedContentDatum.getGist().getId())) {
                    isPlayable = true;
                } else {
                    isPlayable = false;
                    if (appCMSPresenter.isShowDialogForWebPurchase()) {
                        makeLinkClickable(localisedStrings.getTVODContentError(appCMSPresenter.getAppCMSMain().getDomainName()), "", "");
                        return;
                    } else if (!appCMSPresenter.getAppCMSMain().getFeatures().getFreePreview().isFreePreview()) {
                        hideProgressBar();
                        appCMSPresenter.showDialog(AppCMSPresenter.DialogType.TVOD_CONTENT_ERROR,
                                localisedStrings.getTVODContentError(appCMSPresenter.getAppCMSMain().getDomainName()), false, null, null, localisedStrings.getPremiumContentText());
                        return;
                    }

                }
                if (isPlayable && appCMSPresenter.isUserLoggedIn()) {
                    /*appCMSPresenter.launchVideoPlayer(data,
                            data.getGist().getId(),
                            finalCurrentPlayingIndex,
                            finalRelatedVideoIds,
                            -1,
                            finalAction);*/
                    //getPlayer().setPlayWhenReady(true);

                    //setVideoUri(onUpdatedContentDatum.getGist().getId(), appCMSPresenter.getLoadingVideoText());

                    if (onUpdatedContentDatum.getGist().getImageGist() != null
                            && onUpdatedContentDatum.getGist().getImageGist().get_16x9() != null
                            && !TextUtils.isEmpty(onUpdatedContentDatum.getGist().getImageGist().get_16x9())) {
                        coverImage = onUpdatedContentDatum.getGist().getImageGist().get_16x9();
                    } else if (onUpdatedContentDatum.getGist().getVideoImageUrl() != null
                            && !TextUtils.isEmpty(onUpdatedContentDatum.getGist().getVideoImageUrl())) {
                        coverImage = onUpdatedContentDatum.getGist().getVideoImageUrl();
                    } else if (onUpdatedContentDatum.getImageUrl() != null
                            && !TextUtils.isEmpty(onUpdatedContentDatum.getImageUrl())) {
                        coverImage = onUpdatedContentDatum.getImageUrl();
                    }

                    if (remainingTime > 0) {

                        initializeFutureContentTimerView(eventDate, remainingTime, isEventSchedule, coverImage);
                        return;
                    } else {
                        playVideos(0, onUpdatedContentDatum);
                    }
                    //resumePlayer();
                    return;
                } else {
                    msg = localisedStrings.getCannotPurchaseItemMsg(appCMSPresenter.getAppCMSMain().getDomainName());
                }
            }


            /**
             * if end time is expired then show no purchase dialog message
             */
            if (expirationDate > 0 && timeToExpire < 0) {
                isPlayable = false;
            }
//                                    if(updatedContentDatum==null){
//                                        isPlayable=true;
//                                    }
            if (!isPlayable) {
                if (appCMSPresenter.getAppCMSMain().isMonetizationModelEnabled()) {
                    showPreviewFrame(onUpdatedContentDatum);
                } else {
                    if (localisedStrings.getCannotPurchaseItemMsg(appCMSPresenter.getAppCMSMain().getDomainName()) == null) {
                        appCMSPresenter.showNoPurchaseDialog(appCMSPresenter.getLanguageResourcesFile().getUIresource(mContext.getString(R.string.rental_title)), appCMSPresenter.getLanguageResourcesFile().getStringValue(mContext.getString(R.string.cannot_purchase_item_msg), appCMSPresenter.getAppCMSMain().getDomainName()));
                    } else {
                        appCMSPresenter.showNoPurchaseDialog(appCMSPresenter.getLanguageResourcesFile().getUIresource(mContext.getString(R.string.rental_title)), localisedStrings.getCannotPurchaseItemMsg(appCMSPresenter.getAppCMSMain().getDomainName()));
                        msg = localisedStrings.getCannotPurchaseItemMsg(appCMSPresenter.getAppCMSMain().getDomainName());
                    }
                }
            } else {

                                        /*
                                        Check if schedule start date is greater then current date than show message and cannot play
                                         */
                if (onUpdatedContentDatum != null &&
                        onUpdatedContentDatum.getGist() != null && (onUpdatedContentDatum.getGist().getScheduleStartDate() > 0 || onUpdatedContentDatum.getGist().getScheduleEndDate() > 0)) {
                    if (appCMSPresenter.isScheduleVideoPlayable(onUpdatedContentDatum.getGist().getScheduleStartDate(), onUpdatedContentDatum.getGist().getScheduleEndDate(), null)) {
                        playVideos(0, onUpdatedContentDatum);
                        //launchScreeenPlayer(data, finalCurrentPlayingIndex, relatedVideoIds, finalAction, title, permalink);
                    }
                } else {
                    String rentalPeriod = "";
                    if (onUpdatedContentDatum.getPricing().getRent() != null &&
                            onUpdatedContentDatum.getPricing().getRent().getRentalPeriod() > 0) {
                        rentalPeriod = String.valueOf(onUpdatedContentDatum.getPricing().getRent().getRentalPeriod());
                    }
                    if (objTransactionData != null) {
                        rentalPeriod = String.valueOf(objTransactionData.getRentalPeriod());
                    }


                    boolean isShowRentalPeriodDialog = true;
                    /**
                     * if transaction getdata api containf transaction end date .It means Rent API called before
                     * and we have shown rent period dialog before so dont need to show rent dialog again. else sow rent period dilaog
                     */
                    isShowRentalPeriodDialog = (objTransactionData != null && objTransactionData.getPurchaseStatus() != null && objTransactionData.getPurchaseStatus().equalsIgnoreCase("RENTED"));

                    if (isShowRentalPeriodDialog) {

                        if (rentalPeriod == null || TextUtils.isEmpty(rentalPeriod)) {
                            rentalPeriod = "0";
                        }
                        msg = appCMSPresenter.getLanguageResourcesFile().getStringValue(mContext.getString(R.string.rent_time_dialog_mssg),
                                rentalPeriod);
                        if (localisedStrings.getRentTimeDialogMsg(rentalPeriod) != null)
                            msg = localisedStrings.getRentTimeDialogMsg(rentalPeriod);

                        appCMSPresenter.showRentTimeDialog(retry -> {
                            if (retry) {
                                appCMSPresenter.getRentalData(onUpdatedContentDatum.getGist().getId(), rentalResponse -> {
                                    resumePlayer();
                                    //launchScreeenPlayer(onUpdatedContentDatum, finalCurrentPlayingIndex, relatedVideoIds, finalAction, title, permalink);
                                }, false, 0);
                            } else {
//                                                appCMSPresenter.sendCloseOthersAction(null, true, false);
                            }
                        }, msg, "", "", "", true, true);
                    } else {
                        playVideos(0, onUpdatedContentDatum);
                        //launchScreeenPlayer(onUpdatedContentDatum, finalCurrentPlayingIndex, relatedVideoIds, finalAction, title, permalink);

                    }
                }
            }
            if (previewCustomMessageView != null)
                previewCustomMessageView.setText(msg);
            pausePlayer();
            // hideRestrictedMessage();
        }, "Video");
    }

    public static boolean isTimerRefreshRequired = false;


    /**
     * Overridden method : need this method to check if timer refresh is required or not
     *
     * @param changedView
     * @param visibility
     */
    @Override
    protected void onVisibilityChanged(View changedView, int visibility) {
        super.onVisibilityChanged(changedView, visibility);
        if (visibility == View.VISIBLE) {
            if (onUpdatedContentDatum != null && isTimerRefreshRequired && isScheduledForFuture(onUpdatedContentDatum) && !CommonUtils.isPPVContent(getContext(), onUpdatedContentDatum)) {
                showTimerAndMessage();
            }
            isTimerRefreshRequired = false;
        } else {
            isTimerRefreshRequired = true;
        }
    }


    /**
     * Method will be called when timer is finished.
     */
    public void refreshVideo() {
        if (onUpdatedContentDatum != null) {
            setVideoUri(onUpdatedContentDatum.getGist().getId(), localisedStrings.getLoadingVideoText(), isTrailer, false, onUpdatedContentDatum);
        }

    }

    /**
     * On some devices , visibility changed do not get call in case power button is pressed. this method will keep track in that case.
     */
    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        isTimerRefreshRequired = true;
    }

    public boolean isEpisodePlay() {
        return episodePlay;
    }

    public void setEpisodePlay(boolean episodePlay) {
        if (episodePlay && !BaseView.isTablet(appCMSPresenter.getCurrentContext())) {
            appCMSPresenter.getCurrentActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);
        }
        this.episodePlay = episodePlay;
    }

    public boolean isVideoPaused() {
        return videoPaused;
    }

    public void flipFullScreen(boolean isLandscape) {
        appCMSPresenter.isExitFullScreen = !isLandscape;
        if (isLandscape) {
//                if(!episodePlay)
//                     appCMSPresenter.restrictLandscapeOnly();
            appCMSPresenter.isFullScreenVisible = true;
            mZoomButton.setVisibility(View.VISIBLE);
            hideRelatedVideoView(true);
            updateWatchedHistory();
            if (seasonEpisodeView != null && seasonEpisodeView.getVisibility() == View.VISIBLE && playerLeftOutScreen != null) {
                removeRelatedVideoView();
            }

            if (episodePlay)
                appCMSPresenter.showFullScreenEpisodePlayer();
            else
                appCMSPresenter.showFullScreenPlayer();
            mToggleButton.setChecked(true);
        } else {
            appCMSPresenter.isFullScreenVisible = false;
            setPreviousNextVisibility(false);
            llTopBar.setVisibility(View.GONE);
//                if(!episodePlay)
//                     appCMSPresenter.setAppOrientation();
            if (episodePlay)
                appCMSPresenter.exitFullScreenEpisodePlayer();
            else
                appCMSPresenter.exitFullScreenPlayer();

            mZoomButton.setVisibility(View.GONE);
            hideRelatedVideoView(false);
            if (seasonEpisodeView != null && seasonEpisodeView.getVisibility() == View.VISIBLE && playerLeftOutScreen != null) {
                removeRelatedVideoView();
            }
            updateWatchedHistory();
            mToggleButton.setChecked(false);
        }

    }

    View realtedVideoView;

    @Override
    public void viewClick(View view, int height) {
        if (realtedVideoView == null)
            realtedVideoView = view;
        createPlayerLefoutScreen();
        if (view.isSelected()) {
            removeRelatedVideoView();
            createNextVideoContainer();
            loadPrevNextImage();
        } else {
            ((ImageButton) view).setColorFilter(Color.parseColor(appCMSPresenter.getAppCtaBackgroundColor()), android.graphics.PorterDuff.Mode.SRC_IN);
            if (currentPLayingVideoContentData != null && currentPLayingVideoContentData.getModuleApi() != null && currentPLayingVideoContentData.getModuleApi().getContentData() != null)
                showSeasonEpisodeViewOnPlayerMobile(playerLeftOutScreen, llTopBar.getWidth() / 2);
            view.setSelected(true);
            if (playerLeftOutScreen != null)
                playerLeftOutScreen.removeView(nextPreviousVideoContainer);
            setPreviousNextVisibility(false);

        }
    }


    ConstraintLayout playerLeftOutScreen;

    void createPlayerLefoutScreen() {
        if (playerLeftOutScreen == null) {
            playerLeftOutScreen = new ConstraintLayout(mContext);
            LinearLayout.LayoutParams llParams = new LinearLayout.LayoutParams(llTopBar.getWidth(), BaseView.getDeviceWidth() - (getPlayerSeekBarAndControllerHeight() + llTopBar.getHeight()));
            if (BaseView.isTablet(getContext()))
                llParams = new LinearLayout.LayoutParams(llTopBar.getWidth(), BaseView.getDeviceHeight() - (getPlayerSeekBarAndControllerHeight() + llTopBar.getHeight()));
            llParams.setMargins(0, llTopBar.getHeight(), 0, getPlayerSeekBarAndControllerHeight());
            playerLeftOutScreen.setLayoutParams(llParams);
            this.addView(playerLeftOutScreen);
        }
    }

    View nextPreviousVideoContainer;

    @Nullable
    @BindView(R.id.nextEpisodeContainer)
    ConstraintLayout nextEpisodeContainer;
    @Nullable
    @BindView(R.id.previousEpisodeContainer)
    ConstraintLayout previousEpisodeContainer;
    @Nullable
    @BindView(R.id.nextEpisode)
    ImageView nextEpisodeImg;
    @Nullable
    @BindView(R.id.previousEpisode)
    ImageView previousEpisodeImg;
    @Nullable
    @BindView(R.id.previous)
    TextView previous;
    @Nullable
    @BindView(R.id.next)
    TextView next;

    private void createNextVideoContainer() {
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (nextPreviousVideoContainer == null)
            nextPreviousVideoContainer = inflater.inflate(R.layout.next_previous_episode_player_view, null);
        ButterKnife.bind(this, nextPreviousVideoContainer);
        nextPreviousVideoContainer.setId(View.generateViewId());
//        nextPreviousVideoContainer.setLayoutParams(new ConstraintLayout.LayoutParams(playerLeftOutScreen.getWidth(), playerLeftOutScreen.getHeight()));
        nextPreviousVideoContainer.setLayoutParams(playerLeftOutScreen.getLayoutParams());
        if (nextPreviousVideoContainer != null && nextPreviousVideoContainer.getParent() != null) {
            try {
                ((ViewGroup) nextPreviousVideoContainer.getParent()).removeView(nextPreviousVideoContainer);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        playerLeftOutScreen.addView(nextPreviousVideoContainer);
        ConstraintSet constraintSet = new ConstraintSet();
        constraintSet.clone(playerLeftOutScreen);
        constraintSet.connect(playerLeftOutScreen.getId(), ConstraintSet.RIGHT, ConstraintSet.PARENT_ID, ConstraintSet.RIGHT, 0);
        constraintSet.connect(playerLeftOutScreen.getId(), ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.TOP, 0);
        constraintSet.connect(playerLeftOutScreen.getId(), ConstraintSet.LEFT, ConstraintSet.PARENT_ID, ConstraintSet.LEFT, 0);
        constraintSet.connect(playerLeftOutScreen.getId(), ConstraintSet.BOTTOM, ConstraintSet.PARENT_ID, ConstraintSet.BOTTOM, 0);
        constraintSet.applyTo(playerLeftOutScreen);

        if (previous != null)
            previous.setTextColor(Color.parseColor(appCMSPresenter.getAppCMSMain().getBrand().getGeneral().getBlockTitleColor()));
        if (next != null)
            next.setTextColor(Color.parseColor(appCMSPresenter.getAppCMSMain().getBrand().getGeneral().getBlockTitleColor()));

    }

    public void loadPrevNextImage() {
        int currentPlayingEpisodePos = findCurrentPlayingPositionForNextPrevious();
        if (allEpisodes != null && nextEpisodeContainer != null && currentPlayingEpisodePos < allEpisodes.size() - 1) {
            next.setVisibility(VISIBLE);
            setPreviousNextEpisodeImage(allEpisodes.get(currentPlayingEpisodePos + 1).getGist().getImageGist().get_16x9(), nextEpisodeImg);
            nextEpisodeImg.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    isVideoPlaying = true;
                    CustomVideoPlayerView customVideoPlayerView = appCMSPresenter.getCurrentActivity().findViewById(R.id.videoTrailer);
                    if (customVideoPlayerView != null) {
                        ShowDetailsPromoHandler.getInstance().setTrailerPromoAutoPlay(appCMSPresenter, allEpisodes.get(currentPlayingEpisodePos + 1), customVideoPlayerView);
                    }
//                    setVideoUri(allEpisodes.get(currentPlayingEpisodePos + 1).getGist().getId(),
//                            appCMSPresenter.getLocalisedStrings().getLoadingVideoText(), false, false);
                    setDisplayMetadata(allEpisodes.get(currentPlayingEpisodePos + 1));
                    setPreviousNextVisibility(false);
                    nextPreviousVideoContainer = null;
                }
            });
        } else {
            next.setVisibility(GONE);
        }
        if (allEpisodes != null && previousEpisodeContainer != null && currentPlayingEpisodePos > 0) {
            previous.setVisibility(VISIBLE);
            setPreviousNextEpisodeImage(allEpisodes.get(currentPlayingEpisodePos - 1).getGist().getImageGist().get_16x9(), previousEpisodeImg);
            previousEpisodeImg.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    isVideoPlaying = true;
                    CustomVideoPlayerView customVideoPlayerView = appCMSPresenter.getCurrentActivity().findViewById(R.id.videoTrailer);
                    if (customVideoPlayerView != null) {
                        ShowDetailsPromoHandler.getInstance().setTrailerPromoAutoPlay(appCMSPresenter, allEpisodes.get(currentPlayingEpisodePos - 1), customVideoPlayerView);
                    }
//                    setVideoUri(allEpisodes.get(currentPlayingEpisodePos - 1).getGist().getId(),
//                            appCMSPresenter.getLocalisedStrings().getLoadingVideoText(), false, false);
                    setDisplayMetadata(allEpisodes.get(currentPlayingEpisodePos - 1));
                    setPreviousNextVisibility(false);
                    nextPreviousVideoContainer = null;
                }
            });
        } else {
            previous.setVisibility(GONE);
        }
        setPreviousNextVisibility(true);
    }

    public void setPreviousNextVisibility(boolean visibility) {
        int currentPlayingEpisodePos = findCurrentPlayingPositionForNextPrevious();
        if (allEpisodes != null && previousEpisodeContainer != null && nextEpisodeContainer != null) {
            if (visibility) {
                if (currentPlayingEpisodePos < allEpisodes.size() - 1)
                    nextEpisodeContainer.setVisibility(VISIBLE);
                if (currentPlayingEpisodePos >= 1)
                    previousEpisodeContainer.setVisibility(VISIBLE);
                if (seasonEpisodeView != null && seasonEpisodeView.getVisibility() == View.VISIBLE && playerLeftOutScreen != null) {
                    removeRelatedVideoView();
                }
            } else {
                nextEpisodeContainer.setVisibility(GONE);
                previousEpisodeContainer.setVisibility(GONE);
            }
        }
    }

    private void setPreviousNextEpisodeImage(String imageUrl, ImageView imageView) {
        RequestOptions requestOptions = new RequestOptions()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .fitCenter();
        Glide.with(imageView.getContext())
                .load(imageUrl)
                .apply(requestOptions)
                .into(imageView);
    }


    private void showSeasonEpisodeViewOnPlayerMobile(ConstraintLayout layout, int width) {
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (seasonEpisodeView == null)
            seasonEpisodeView = inflater.inflate(R.layout.player_seasons_episode_list, null);
        ButterKnife.bind(this, seasonEpisodeView);
        seasonEpisodeView.setId(View.generateViewId());
        seasonEpisodeView.setLayoutParams(new ConstraintLayout.LayoutParams(width, ViewGroup.LayoutParams.MATCH_PARENT));
        if (seasonEpisodeView != null && seasonEpisodeView.getParent() != null) {
            ((ViewGroup) seasonEpisodeView.getParent()).removeView(seasonEpisodeView);
        }
        layout.addView(seasonEpisodeView);
        ConstraintSet constraintSet = new ConstraintSet();
        constraintSet.clone(layout);
        constraintSet.connect(seasonEpisodeView.getId(), ConstraintSet.RIGHT, ConstraintSet.PARENT_ID, ConstraintSet.RIGHT, 0);
        constraintSet.connect(seasonEpisodeView.getId(), ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.TOP, 0);
        constraintSet.applyTo(layout);
        episodeTitle.setText(localisedStrings.getEpisodesHeaderText());
        seasonTitle.setText(localisedStrings.getSeasonsLabelText());
        seasonRecylerView.setLayoutManager(new LinearLayoutManager(mContext,
                LinearLayoutManager.VERTICAL,
                false));
        episodeRecylerView.setLayoutManager(new LinearLayoutManager(mContext,
                LinearLayoutManager.VERTICAL,
                false));
        PlayerSeasonAdapter playerSeasonAdapter = new PlayerSeasonAdapter(currentPLayingVideoContentData.getModuleApi().getContentData().get(0).getSeason(), appCMSPresenter, currentPlayingSeasonPosition());
        playerSeasonAdapter.setSeasonClickListener(this);
        seasonRecylerView.setAdapter(playerSeasonAdapter);
        seasonRecylerView.smoothScrollToPosition(currentPlayingSeasonPosition());
        PlayerEpisodeAdapter playerEpisodeAdapter = new PlayerEpisodeAdapter(currentPLayingVideoContentData.getModuleApi().getContentData().get(0).getSeason().get(0).getEpisodes(), episodeRecylerView, appCMSPresenter);
        playerEpisodeAdapter.setVideoSelected(this);
        episodeRecylerView.setAdapter(playerEpisodeAdapter);
        SeasonTabSelectorBus.instanceOf().setTab(currentPLayingVideoContentData.getModuleApi().getContentData().get(0).getSeason().get(currentPlayingSeasonPosition()).getEpisodes());

    }

    private void showLiveModuleTab(Module moduleApiData) {
        tabSelectedScreen = new ConstraintLayout(mContext);
        LinearLayout.LayoutParams llParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        //if (BaseView.isTablet(getContext()))
        //     llParams = new LinearLayout.LayoutParams(llTopBar.getWidth(), BaseView.getDeviceHeight() - (getPlayerSeekBarAndControllerHeight() + llTopBar.getHeight()));
        //    llParams.setMargins(0, llTopBar.getHeight(), 0, getPlayerSeekBarAndControllerHeight());
        tabSelectedScreen.setLayoutParams(llParams);

        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (liveModuleTabView == null)
            liveModuleTabView = inflater.inflate(R.layout.live_module_list, null);
        ButterKnife.bind(this, liveModuleTabView);
        liveModuleTabView.setId(View.generateViewId());
        liveModuleTabView.setLayoutParams(new ConstraintLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
//        if (liveModuleTabView != null && liveModuleTabView.getParent() != null) {
//            ((ViewGroup) liveModuleTabView.getParent()).removeView(seasonEpisodeView);
//        }
        tabSelectedScreen.addView(liveModuleTabView);
        tabSelectedScreen.setVisibility(VISIBLE);

        ConstraintSet constraintSet = new ConstraintSet();
        constraintSet.clone(tabSelectedScreen);
        constraintSet.connect(liveModuleTabView.getId(), ConstraintSet.RIGHT, ConstraintSet.PARENT_ID, ConstraintSet.RIGHT, 0);
        constraintSet.connect(liveModuleTabView.getId(), ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.TOP, 0);
        constraintSet.applyTo(tabSelectedScreen);
        seasonRecylerView.setLayoutManager(new LinearLayoutManager(mContext,
                LinearLayoutManager.HORIZONTAL,
                false));
        LiveModuleTabAdapter liveModuleTabAdapter = new LiveModuleTabAdapter(settings.getTabs(), appCMSPresenter, 0, this, moduleApiData);
//        liveModuleTabAdapter.setSeasonClickListener(this);
        seasonRecylerView.setAdapter(liveModuleTabAdapter);
        // seasonRecylerView.smoothScrollToPosition(currentPlayingSeasonPosition());
        //SeasonTabSelectorBus.instanceOf().setTab(moduleApi.getContentData().get(0).getSeason().get(currentPlayingSeasonPosition()).getEpisodes());
        this.addView(tabSelectedScreen);
    }

    ConstraintLayout tabSelectedScreen;

    View seasonEpisodeView;
    View liveModuleTabView;
    @Nullable
    @BindView(R.id.seasonTitle)
    TextView seasonTitle;
    @Nullable
    @BindView(R.id.episodeTitle)
    TextView episodeTitle;
    @Nullable
    @BindView(R.id.seasonRecylerView)
    RecyclerView seasonRecylerView;
    @Nullable
    @BindView(R.id.episodeRecylerView)
    RecyclerView episodeRecylerView;

    @Override
    public void selectedVideoListener(ContentDatum contentDatum, int positionPlayed) {
        updateWatchedHistory();
        CustomVideoPlayerView customVideoPlayerView = appCMSPresenter.getCurrentActivity().findViewById(R.id.videoTrailer);
        setDisplayMetadata(contentDatum);
        if (customVideoPlayerView != null) {
            ShowDetailsPromoHandler.getInstance().setTrailerPromoAutoPlay(appCMSPresenter, contentDatum, customVideoPlayerView);
        }

//        setVideoUri(contentDatum.getGist().getId(),
//                appCMSPresenter.getLocalisedStrings().getLoadingVideoText(), false, false);
        removeRelatedVideoView();
        appCMSPresenter.setShowDetailsGist(contentDatum.getGist());
        appCMSPresenter.setSelectedSeason(selectedSeason);
        appCMSPresenter.setSeasonEpisodeAdapterData(currentPLayingVideoContentData.getModuleApi().getContentData().get(0).getSeason().get(selectedSeason).getEpisodes());

    }

    public void updateWatchedHistory() {
        if (!appCMSPresenter.isUserLoggedIn())
            return;
        boolean isWatchHistoryUpdateEnabled = appCMSPresenter.getAppCMSMain().getFeatures() != null
                && appCMSPresenter.getAppCMSMain().getFeatures().getWatchedHistory() != null
                && appCMSPresenter.getAppCMSMain().getFeatures().getWatchedHistory().isEnabled();
        boolean isLiveStream = onUpdatedContentDatum != null && onUpdatedContentDatum.getGist() != null && onUpdatedContentDatum.getGist().isLiveStream();
        if (!isLiveStream && !isTrailer && isWatchHistoryUpdateEnabled && onUpdatedContentDatum.getGist() != null) {
            appCMSPresenter.updateWatchedTime(onUpdatedContentDatum.getGist().getId(), seriesId,
                    getCurrentPosition() / 1000, updateHistoryResponse -> {
                        if (updateHistoryResponse.getResponseCode() == 401) {
                            if (updateHistoryResponse.getErrorCode().equalsIgnoreCase("MAX_STREAMS_ERROR")) {
                                pausePlayer();
                                isMaxStreamError = true;
                                showPreviewFrame(onUpdatedContentDatum);
                            }
                        } else {
                            isMaxStreamError = false;
                        }
                    });
        }
    }

    private void removeRelatedVideoView() {
        if (realtedVideoView != null) {
            ((ImageButton) realtedVideoView).setColorFilter(ContextCompat.getColor(getContext(), android.R.color.white), android.graphics.PorterDuff.Mode.SRC_IN);
            realtedVideoView.setSelected(false);
            if (seasonEpisodeView != null)
                playerLeftOutScreen.removeView(seasonEpisodeView);
        }
    }

    int currentPlayingSeasonPosition() {
        int position = 0;
        if (currentPLayingVideoContentData.getModuleApi() != null && currentPLayingVideoContentData.getModuleApi().getContentData() != null
                && currentPLayingVideoContentData.getModuleApi().getContentData().size() > 0
                && currentPLayingVideoContentData.getModuleApi().getContentData().get(0).getSeason() != null) {
            for (int i = 0; i < currentPLayingVideoContentData.getModuleApi().getContentData().get(0).getSeason().size(); i++) {
                if (currentPLayingVideoContentData.getModuleApi().getContentData().get(0).getSeason().get(i).getEpisodes() != null) {
                    for (int j = 0; j < currentPLayingVideoContentData.getModuleApi().getContentData().get(0).getSeason().get(i).getEpisodes().size(); j++) {
                        String comaperId = currentPLayingVideoContentData.getModuleApi().getContentData().get(0).getSeason().get(i).getEpisodes().get(j).getGist() == null ?
                                currentPLayingVideoContentData.getModuleApi().getContentData().get(0).getSeason().get(i).getEpisodes().get(j).getId() :
                                currentPLayingVideoContentData.getModuleApi().getContentData().get(0).getSeason().get(i).getEpisodes().get(j).getGist().getId();
                        if (onUpdatedContentDatum.getGist().getId().equalsIgnoreCase(comaperId))
                            return i;
                    }
                }
            }
        }
        return position;
    }


    int selectedSeason = 0;

    @Override
    public void selectedSeason(int position) {
        selectedSeason = position;
    }

    private void createAllEpisodeList() {
        boolean segment = false;
        int currentSeason = 0;
        int currentEpisode = 0;
        if (currentPLayingVideoContentData != null && currentPLayingVideoContentData.getModuleApi() != null && currentPLayingVideoContentData.getModuleApi().getContentData() != null
                && currentPLayingVideoContentData.getModuleApi().getContentData().size() >= 1
                && currentPLayingVideoContentData.getModuleApi().getContentData().get(0).getSeason() != null) {
            allEpisodes = new ArrayList<>();
            for (int i = 0; i < currentPLayingVideoContentData.getModuleApi().getContentData().get(0).getSeason().size(); i++) {
                if (currentPLayingVideoContentData.getModuleApi().getContentData().get(0).getSeason().get(i).getEpisodes() != null) {
                    allEpisodes.addAll(currentPLayingVideoContentData.getModuleApi().getContentData().get(0).getSeason().get(i).getEpisodes());
                }
            }
        }

    }

    private int findCurrentPlayingPositionOfEpisode() {
        int currentPlayingSeason = currentPlayingSeasonPosition();
        for (int episodeNum = 0; episodeNum < currentPLayingVideoContentData.getModuleApi().getContentData().get(0).getSeason().get(currentPlayingSeason).getEpisodes().size(); episodeNum++) {
            if (onUpdatedContentDatum.getGist().getId().equalsIgnoreCase(currentPLayingVideoContentData.getModuleApi().getContentData().get(0).getSeason().get(currentPlayingSeason).getEpisodes().get(episodeNum).getGist().getId()))
                return episodeNum;
        }
        return -1;
    }

    private int findCurrentPlayingPositionForNextPrevious() {
        if (allEpisodes != null && onUpdatedContentDatum!=null&&onUpdatedContentDatum.getGist()!=null) {
            for (int i = 0; i < allEpisodes.size(); i++) {
                if (allEpisodes.get(i).getGist() != null && onUpdatedContentDatum.getGist().getId().equalsIgnoreCase(allEpisodes.get(i).getGist().getId()))
                    return i;
            }
        }
        return -1;
    }

    private void setDisplayMetadata(ContentDatum data) {
        TextView title = appCMSPresenter.getCurrentActivity().findViewById(R.id.contentTitle);
        if (title != null)
            title.setText(data.getGist().getTitle());
        TextView contentDescription = appCMSPresenter.getCurrentActivity().findViewById(R.id.contentDescription);
        if (contentDescription != null && data.getGist().getDescription() != null && data.getGist().getTitle() != null) {
            int descLength = data.getGist().getDescription().length();
            int dis_max_length = mContext.getResources().getInteger(R.integer.app_cms_show_details_discription_length);
            if (descLength > dis_max_length) {
                contentDescription.setSingleLine(false);
                contentDescription.setEllipsize(TextUtils.TruncateAt.END);
                contentDescription.setMaxLines(2);
                ViewCreator.setMoreLinkInDescription(appCMSPresenter, contentDescription, data.getGist().getTitle(), data.getGist().getDescription(), dis_max_length, (Color.parseColor("#ffffff")));
            } else
                contentDescription.setText(data.getGist().getDescription());
        }
        TextView saveUpdate = appCMSPresenter.getCurrentActivity().findViewById(R.id.save);
        if (appCMSPresenter.isFilmAddedToWatchlist(data.getGist().getId()))
            saveUpdate.setText(localisedStrings.getRemoveFromWatchlistText());
        else
            saveUpdate.setText(localisedStrings.getAddToWatchlistText());
    }


    SimpleExoPlayer simpleExoPlayer = null;
    PlayerNotificationManager pnm;
    PlayerNotificationAdapter notificationAdapter;

    public void createNotification() {
        try {
            if (notificationAdapter == null)
                notificationAdapter = new PlayerNotificationAdapter();


            if (pnm == null)
                pnm = PlayerNotificationManager.createWithNotificationChannel(
                        mContext,
                        mContext.getString(R.string.player_notification_channel_id),
                        R.string.player_notification_channel_name,
                        R.string.player_notification_channel_desc,
                        239,
                        notificationAdapter);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void onDestroyNotification() {
        try {
            if (pnm != null)
                pnm.setPlayer(null);
//            if (simpleExoPlayer != null)
//                simpleExoPlayer.release();
            pnm = null;
            simpleExoPlayer = null;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setNotification() {
        new Handler().postDelayed(() -> {
            try {
                boolean showNotification = true;
                if (notificationAdapter != null && onUpdatedContentDatum != null) {
                    if (((AppCMSPageActivity) appCMSPresenter.getCurrentActivity()).getUpdatedAppCMSBinder() != null) {
                        AppCMSBinder appCMSBinder = ((AppCMSPageActivity) appCMSPresenter.getCurrentActivity()).getUpdatedAppCMSBinder();
                        if (appCMSPresenter != null && appCMSBinder != null && appCMSBinder.getAppCMSPageUI() != null &&
                                appCMSBinder.getAppCMSPageUI().getModuleList() != null && appCMSBinder.getAppCMSPageUI().getModuleList().size() > 0 &&
                                (appCMSPresenter.getModuleListByName(appCMSBinder.getAppCMSPageUI().getModuleList(), appCMSPresenter.getCurrentActivity().getString(R.string.app_cms_page_module_key_showdetail_06)) != null)) {
                            if (onUpdatedContentDatum != null &&
                                    onUpdatedContentDatum.getStreamingInfo() != null &&
                                    !onUpdatedContentDatum.getStreamingInfo().isLiveStream()) {
                                notificationAdapter.setData(onUpdatedContentDatum);
                                showNotification = true;
                            } else {
                                showNotification = false;
                                onDestroyNotification();
                            }
                        } else if (appCMSBinder == null || appCMSBinder.getAppCMSPageUI() == null ||
                                appCMSBinder.getAppCMSPageUI().getModuleList() == null) {
                            showNotification = false;
                            onDestroyNotification();
                        } else {
                            notificationAdapter.setData(onUpdatedContentDatum);
                            showNotification = true;
                        }
                    }


                }
                if (showNotification) {
                    boolean showDetailPage = false;
                    if (((AppCMSPageActivity) appCMSPresenter.getCurrentActivity()).getUpdatedAppCMSBinder() != null) {
                        AppCMSBinder appCMSBinder = ((AppCMSPageActivity) appCMSPresenter.getCurrentActivity()).getUpdatedAppCMSBinder();
                        if (appCMSPresenter != null && appCMSBinder != null && appCMSBinder.getAppCMSPageUI() != null &&
                                appCMSBinder.getAppCMSPageUI().getModuleList() != null && appCMSBinder.getAppCMSPageUI().getModuleList().size() > 0 &&
                                (appCMSPresenter.getModuleListByName(appCMSBinder.getAppCMSPageUI().getModuleList(), appCMSPresenter.getCurrentActivity().getString(R.string.app_cms_page_module_key_showdetail_06)) != null)) {
                            showDetailPage = true;
                        }
                    }
                    if (pnm != null) {
                        if (appCMSPresenter.getVideoPlayerViewCache(appCMSPresenter.getShowDeatil06TrailerPlayerKey()) != null && showDetailPage) {
                            simpleExoPlayer = appCMSPresenter.getVideoPlayerViewCache(appCMSPresenter.getShowDeatil06TrailerPlayerKey()).getPlayer();
                        } else {
                            simpleExoPlayer = getPlayer();
                        }
                        pnm.setUseNavigationActions(false);
                        MediaSessionCompat mediaSession = new MediaSessionCompat(mContext, "AppCMSPlayer");
                        mediaSession.setActive(true);
                        String title = "unknown";
                        String description = "unknown";
                        if (onUpdatedContentDatum != null && onUpdatedContentDatum.getGist() != null) {
                            title = onUpdatedContentDatum.getGist().getTitle();
                        }
                        if (onUpdatedContentDatum != null && onUpdatedContentDatum.getSeriesData() != null &&
                                onUpdatedContentDatum.getSeriesData().size() > 0 &&
                                onUpdatedContentDatum.getSeriesData().get(0).getTitle() != null) {
                            description = onUpdatedContentDatum.getGist().getDescription();
                        }
                        mediaSession.setMetadata(new MediaMetadataCompat.Builder()
                                .putString(MediaMetadataCompat.METADATA_KEY_TITLE, title)
                                .putString(MediaMetadataCompat.METADATA_KEY_DISPLAY_DESCRIPTION, description)
                                .build());
                        MediaSessionConnector mediaSessionConnector =
                                new MediaSessionConnector(mediaSession);
                        mediaSessionConnector.setPlayer(simpleExoPlayer);
                        pnm.setMediaSessionToken(mediaSession.getSessionToken());
                        pnm.setPlayer(simpleExoPlayer);
                        pnm.setPriority(PRIORITY_LOW);
//                        AudioAttributes audioAttributes = new AudioAttributes.Builder() .setUsage(C.USAGE_MEDIA) .setContentType(C.CONTENT_TYPE_MOVIE) .build();
//                        simpleExoPlayer.setAudioAttributes(audioAttributes, /* handleAudioFocus= */ true);
                    }
                } else {
                    onDestroyNotification();
                }
            } catch (Exception e) {
                System.out.println("crashingRTA" + e.toString());
                e.printStackTrace();
            }
        }, 1);
    }

    public void setstreamingQualitySelector(Boolean streamingQualitySelector) {
        streamingQualitySelectorCreated = streamingQualitySelector;
    }

    public PlayerSettingsView getPlayerSettingsView() {
        return playerSettingsView;
    }

    @Override
    public void closeClick() {

    }
}

