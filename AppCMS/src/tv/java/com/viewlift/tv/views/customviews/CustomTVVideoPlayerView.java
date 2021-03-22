package com.viewlift.tv.views.customviews;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.google.ads.interactivemedia.v3.api.AdDisplayContainer;
import com.google.ads.interactivemedia.v3.api.AdErrorEvent;
import com.google.ads.interactivemedia.v3.api.AdEvent;
import com.google.ads.interactivemedia.v3.api.AdsLoader;
import com.google.ads.interactivemedia.v3.api.AdsManager;
import com.google.ads.interactivemedia.v3.api.AdsRequest;
import com.google.ads.interactivemedia.v3.api.ImaSdkFactory;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.viewlift.R;
import com.viewlift.models.data.appcms.api.AppCMSSignedURLResult;
import com.viewlift.models.data.appcms.api.AppCMSTransactionDataValue;
import com.viewlift.models.data.appcms.api.ClosedCaptions;
import com.viewlift.models.data.appcms.api.ContentDatum;
import com.viewlift.models.data.appcms.api.VideoAssets;
import com.viewlift.models.data.appcms.beacon.BeaconBuffer;
import com.viewlift.models.data.appcms.beacon.BeaconHandler;
import com.viewlift.models.data.appcms.beacon.BeaconPing;
import com.viewlift.models.data.appcms.beacon.BeaconRunnable;
import com.viewlift.models.data.appcms.ui.android.NavigationUser;
import com.viewlift.models.data.appcms.ui.main.AppCMSMain;
import com.viewlift.models.data.appcms.ui.page.Component;
import com.viewlift.presenters.AppCMSPresenter;
import com.viewlift.tv.AppCmsTVSplashActivity;
import com.viewlift.tv.utility.Utils;
import com.viewlift.tv.views.activity.AppCmsHomeActivity;
import com.viewlift.tv.views.dialog.AppCMSTVVerifyVideoPinDialog;
import com.viewlift.tv.views.fragment.ClearDialogFragment;
import com.viewlift.utils.CommonUtils;
import com.viewlift.views.customviews.TVVideoPlayerView;
import com.viewlift.views.customviews.VideoPlayerView;
import com.viewlift.views.listener.TrailerCompletedCallback;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import rx.functions.Action1;

import static com.google.android.exoplayer2.Player.STATE_BUFFERING;
import static com.google.android.exoplayer2.Player.STATE_ENDED;
import static com.google.android.exoplayer2.Player.STATE_READY;

/**
 * Created by nitin.tyagi on 1/8/2018.
 */

public class CustomTVVideoPlayerView
        extends TVVideoPlayerView
        implements AdErrorEvent.AdErrorListener,
    AdEvent.AdEventListener,
    VideoPlayerView.ErrorEventListener,VideoPlayerView.ClosedCaptionSelector {
        protected static final String TAG = CustomTVVideoPlayerView.class.getSimpleName();
    private final AppCMSPresenter appCMSPresenter;
    private boolean isTrailer;
    private Context mContext;
    private LinearLayout custonLoaderContaineer;
    private TextView loaderMessageView;
    private LinearLayout customMessageContaineer;
    private TextView customMessageView;
    protected boolean shouldRequestAds = false;
    private boolean isADPlay;
    private ImaSdkFactory sdkFactory;
    private AdsLoader adsLoader;
    private AdsManager adsManager;
    private String adsUrl;
    private boolean isAdsDisplaying;
    private boolean isAdDisplayed;
    private View imageViewContainer;
    private ImageView imageView;
    private long beaconMsgTimeoutMsec;
    private long beaconBufferingTimeoutMsec;
    private long mTotalVideoDuration;
    private boolean sentBeaconFirstFrame;
    private long mStopBufferMilliSec;
    private long mStartBufferMilliSec;
    private double ttfirstframe;
    private int currentPlayingIndex = -1;
    protected List<String> relatedVideoId;
    private String parentScreenName;
    protected String mStreamId;
    private int apod;
    protected ContentDatum videoData = null;
    private BeaconBuffer beaconBufferingThread;
    private BeaconPing beaconMessageThread;
    private boolean sentBeaconPlay;
    private Timer timer;
    private TimerTask timerTask;
    private ContentDatum contentDatum;
    private boolean isHardPause;
    private String videoDataId;
    private String videoTitle;
    private String permaLink;
    private boolean isErrorOccurred = false;
    private TrailerCompletedCallback trailerCompletedCallback;
    private static final long SECONDS_TO_MILLIS = 1000L;
   // private VisualTimer visualTimer;
    private FrameLayout visualTimerframeLayout;
    private BeaconHandler mBeaconHandler;
    private BeaconRunnable mBeaconRunnable;

    private final String FIREBASE_STREAM_START = "stream_start";
    private final String FIREBASE_STREAM_25 = "stream_25_pct";
    private final String FIREBASE_STREAM_50 = "stream_50_pct";
    private final String FIREBASE_STREAM_75 = "stream_75_pct";
    private final String FIREBASE_STREAM_95 = "stream_95_pct";
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
    private boolean defaultdAdsPlay=false;
    Handler mProgressHandler;
    Runnable mProgressRunnable;
    boolean isStreamStart, isStream25, isStream50, isStream75,isStream95, isStream100;

    public CustomTVVideoPlayerView(Context context,
                                   AppCMSPresenter appCMSPresenter) {
        super(context, appCMSPresenter);
        this.appCMSPresenter = appCMSPresenter;
        getPlayerView().setUseController(false);
        mContext = context;
        createLoader();
        createCustomMessageView();
        createTitleView();
        imageViewContainer = findViewById(R.id.videoPlayerThumbnailImageContainer);
        imageView = (ImageView) findViewById(R.id.videoPlayerThumbnailImage);
        setListener(this);
        if(!isTrailer) setClosedCaptionsSelector(this);
        parentScreenName = mContext.getString(R.string.app_cms_beacon_video_player_parent_screen_name);
        setupAds();
        getPlayerView().hideController();
        setOnPlayerControlsStateChanged(new Action1<Integer>() {
            @Override
            public void call(Integer integer) {
                headerTitleContaineer.setVisibility(integer);
            }
        });

        try {
            mStreamId = appCMSPresenter.getStreamingId(videoDataId);
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
            mStreamId = videoDataId + appCMSPresenter.getCurrentTimeStamp();
        }

    }


    private String getAdsUrl(ContentDatum contentDatum){
       // shouldRequestAds = true;
        shouldRequestAds = false;
        String adsUrl = appCMSPresenter.getAdsUrl(appCMSPresenter.getPermalinkCompletePath(contentDatum.getGist().getPermalink()));
        if(adsUrl != null &&!isTrailer /* && !contentDatum.getGist().getIsLiveStream()*/
                && (!appCMSPresenter.isUserSubscribed() )) {
            shouldRequestAds = true;
        }else if(defaultdAdsPlay){
            shouldRequestAds = true;
        }
        Log.e("shouldRequestAds",String.valueOf(shouldRequestAds));
        return adsUrl;
    }

    public void requestFocusOnLogin(){
        if(customMessageContaineer.getVisibility() == View.VISIBLE){
            //loginButton.requestFocus();
        }
    }

    public void setupAds() {
        sdkFactory = ImaSdkFactory.getInstance();
        adsLoader = sdkFactory.createAdsLoader(getContext());
        adsLoader.addAdErrorListener(this);
        adsLoader.addAdsLoadedListener(adsManagerLoadedEvent -> {
            adsManager = adsManagerLoadedEvent.getAdsManager();
            adsManager.addAdErrorListener(CustomTVVideoPlayerView.this);
            adsManager.addAdEventListener(CustomTVVideoPlayerView.this);
            adsManager.init();
        });
    }

    //Solve - https://snagfilms.atlassian.net/browse/VIEWLIFT-1135
    public boolean isVideoScheduled(){
        System.out.println("TestVinit isVideoScheduled called....");
        if (contentDatum != null &&
                contentDatum.getPricing() != null &&
                contentDatum.getGist() != null &&
                (contentDatum.getGist().getScheduleStartDate() > 0)) {
            long startTime = contentDatum.getGist().getScheduleStartDate();
            long endTime = contentDatum.getGist().getScheduleEndDate();

            long timeToStart = startTime - System.currentTimeMillis();

            if (startTime > 0 && timeToStart > 0) {
                if(visualTimerframeLayout != null){
                    visualTimerframeLayout.removeAllViews();
                    visualTimerframeLayout = null;
                }
                visualTimerframeLayout = new FrameLayout(mContext);
                LayoutParams layoutParams = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                layoutParams.gravity = Gravity.CENTER;
                visualTimerframeLayout.setLayoutParams(layoutParams);

                Component component = new Component();
                component.setText(getResources().getString(R.string.timer_until_pay_per_view_event));

                VisualTimer visualTimer = new VisualTimer(mContext,null , component, appCMSPresenter);
                visualTimer.setId(R.id.visualTimer);
                visualTimer.startTimer(timeToStart);
                visualTimer.setVisibility(View.VISIBLE);


                visualTimer.setCallBack(() -> {
                    removeView(visualTimerframeLayout);
                    imageViewContainer.setVisibility(View.GONE);
                    imageView.setVisibility(View.GONE);
                    String videoId = contentDatum.getGist().getOriginalObjectId();
                    String title = contentDatum.getGist().getTitle();
                    if (videoId == null)
                        videoId = contentDatum.getGist().getId();
                    setVideoUri(videoId, title,contentDatum,false,false);
                });
                visualTimerframeLayout.addView(visualTimer);
                addView(visualTimerframeLayout);
                setBackgroundImage();
                showRestrictMessage("");
                toggleLoginButtonVisibility(false);
                System.out.println("TestVinit isVideoScheduled timer has started....");
                return true;
            }
            return false;
        }
        return false;
    }

    public void refreshStandAlonePlayer(){
        hideBackgroundImage();
        String videoId = contentDatum.getGist().getOriginalObjectId();
        String title = contentDatum.getGist().getTitle();
        if (videoId == null)
            videoId = contentDatum.getGist().getId();
        setVideoUri(videoId, title,contentDatum,false,false);
    }

    private void hideBackgroundImage() {
        removeView(visualTimerframeLayout);
        imageViewContainer.setVisibility(View.GONE);
        imageView.setVisibility(View.GONE);
    }

    public void playVideos(ContentDatum contentDatum) {
        resetStreamFlags();
        setContentDatum(contentDatum);
        if (!appCMSPresenter.isPinVerified() && CommonUtils.isUnderAgeRestrictions(appCMSPresenter, contentDatum.getParentalRating())) {
            AppCMSTVVerifyVideoPinDialog.newInstance(isVerified -> {
                appCMSPresenter.setPinVerified(isVerified);
                if (isVerified) {
                    hideBackgroundImage();
                    playVideos(contentDatum);
                }
            }).show(appCMSPresenter.getCurrentActivity().getSupportFragmentManager(), AppCMSTVVerifyVideoPinDialog.class.getSimpleName());
            setBackgroundImage();
            pausePlayer();
            exitFullScreenPlayer();
            return;
        }

        try {
            mStreamId = appCMSPresenter.getStreamingId(videoData.getGist().getTitle());
        } catch (Exception e) {
            e.printStackTrace();
        }
        permaLink = contentDatum.getGist().getPermalink();
        setBeaconData();
        hideRestrictedMessage();
        String url = null;
        String closedCaptionUrl = null;
        if (null != contentDatum && null != contentDatum.getStreamingInfo()) {
//            shouldRequestAds = !contentDatum.getGist().getIsLiveStream();
            if (null != contentDatum.getStreamingInfo().getVideoAssets()){
                url = getVideoUrl(contentDatum.getStreamingInfo().getVideoAssets());
            }
        }
        if (!isTrailer && null != contentDatum && contentDatum.getContentDetails() != null
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

        Log.d(TAG , "Url is = "+url);
        if (null != url) {
            lastUrl = url;
            closedCaptionUri = closedCaptionUrl;
            setAppCMSPresenter(appCMSPresenter);
            AppCMSSignedURLResult appCMSSignedURLResult = contentDatum.getAppCMSSignedURLResult();
            if (appCMSSignedURLResult != null) {
                setPolicyCookie(appCMSSignedURLResult.getPolicy());
                setSignatureCookie(appCMSSignedURLResult.getSignature());
                setKeyPairIdCookie(appCMSSignedURLResult.getKeyPairId());
            }

            long runtime = contentDatum.getGist().getRuntime();
            long watchedTime = contentDatum.getGist().getWatchedTime();
            long videoPlayTime = 0;

            long playDifference = runtime - watchedTime;//((watchedTime*100)/runTime);
            long playTimePercentage = 0;
            if (runtime != 0) {
                playTimePercentage = ((watchedTime * 100) / runtime);
            }
            // if video watchtime is greater or equal to 98% of total run time and interval is less than 30 then play from start
            if (isTrailer || (playTimePercentage >= 98 && playDifference <= 30)) {
                videoPlayTime = 0;
            } else {
                videoPlayTime = watchedTime;
            }
            if(appCMSPresenter.isNewsTemplate()) {
                if (isTabIsClicked && defaultdAdsPlay) {
                    setTabIsClicked(false);
                    setAdsUrl(getAdsUrl(contentDatum));
                } else {
                    if (shouldRequestAds) setAdsUrl(getAdsUrl(contentDatum));
                }
            }
            Log.e("defaultdAdsPlay",String.valueOf(defaultdAdsPlay));

            setUri(Uri.parse(url), closedCaptionUrl == null ? null : Uri.parse(closedCaptionUrl));

            if (!contentDatum.getGist().isLiveStream()) {
                setCurrentPosition(videoPlayTime * SECONDS_TO_MILLIS);
            }



            isVideoPlaying = true;
            if (null != appCMSPresenter.getCurrentActivity() &&
                    appCMSPresenter.getCurrentActivity() instanceof AppCmsHomeActivity) {
                if (((AppCmsHomeActivity) appCMSPresenter.getCurrentActivity()).isActive()
                        && !isHardPause()) {
                    player.setPlayWhenReady(true);
                } else {
                    player.setPlayWhenReady(false);
                }
            }

            if (currentPlayingIndex == -1) {
                relatedVideoId = contentDatum.getContentDetails().getRelatedVideoIds();
            }
            if (playerView != null && playerView.getController() != null) {
                playerView.getController().setPlayingLive(isLiveStream);
            }

//            if (isTrailer&&playerView != null && playerView.getController() != null) {
//                disableController();
//            }else {
//                enableController();
//            }
//            if(isTrailer){
//                appCMSPresenter.tvVideoPlayerView.getPlayerView().setUseController(false);
//                appCMSPresenter.tvVideoPlayerView.getPlayerView().hideController();
//            }else {
//                appCMSPresenter.tvVideoPlayerView.getPlayerView().setUseController(true);
//                appCMSPresenter.tvVideoPlayerView.getPlayerView().showController();
//            }
           // hideProgressBar();
        }

    }

    private void initlizeBeaconsThread(){
        beaconMsgTimeoutMsec = getResources().getInteger(R.integer.app_cms_beacon_timeout_msec);
        beaconBufferingTimeoutMsec = getResources().getInteger(R.integer.app_cms_beacon_buffering_timeout_msec);
        beaconMessageThread = null;
        beaconBufferingThread = null;

        beaconMessageThread = new BeaconPing(beaconMsgTimeoutMsec,
                appCMSPresenter,
                videoDataId,
                permaLink,
                isTrailer,
                parentScreenName,
                this,
                mStreamId,
                contentDatum );

        beaconBufferingThread = new BeaconBuffer(beaconBufferingTimeoutMsec,
                appCMSPresenter,
                videoDataId,
                permaLink,
                parentScreenName,
                this,
                mStreamId,
                contentDatum);

        mBeaconHandler = new BeaconHandler(this.getPlayer().getApplicationLooper());
        mBeaconRunnable = new BeaconRunnable(beaconMessageThread,
                beaconBufferingThread,
                mBeaconHandler,
                this);
        mBeaconHandler.handle(mBeaconRunnable);
    }

    public void disableLiveRight(){
        disableRightFocusLive();
    }

    public void setVideoUri(String videoId, String title, ContentDatum _contentDatum,boolean isTrailer,boolean defaultdAdsPlay) {

        hideRestrictedMessage();
        showProgressBar(appCMSPresenter.getLocalisedStrings().getLoadingMessage());
        videoDataId = videoId;
        videoTitle = title;
        sentBeaconPlay = false;
        sentBeaconFirstFrame = false;
        this.isTrailer = isTrailer;
        if(_contentDatum != null) _contentDatum.setFromStandalone(true);
        this.defaultdAdsPlay=defaultdAdsPlay;
        appCMSPresenter.refreshVideoData(videoId, (ContentDatum contentDatum) -> {

            if (contentDatum != null) {
                if (!TextUtils.isEmpty(contentDatum.getVideoPlayError())) {
                    setBackgroundImage();
                    showRestrictMessage(contentDatum.getVideoPlayError());
                    toggleLoginButtonVisibility(false);
                    exitFullScreenPlayer();
                    pausePlayer();
                    return;
                }
                if (contentDatum.getAppCMSSignedURLResult() != null) {
                    updateSignatureCookies(contentDatum.getAppCMSSignedURLResult().getPolicy(),
                            contentDatum.getAppCMSSignedURLResult().getSignature(),
                            contentDatum.getAppCMSSignedURLResult().getKeyPairId());
                    setPolicyCookie(contentDatum.getAppCMSSignedURLResult().getPolicy());
                    setSignatureCookie(contentDatum.getAppCMSSignedURLResult().getSignature());
                    setKeyPairIdCookie(contentDatum.getAppCMSSignedURLResult().getKeyPairId());
                }

                this.contentDatum = contentDatum;
                setFirebaseProgressHandling();
                initlizeBeaconsThread();
                if (contentDatum.getGist() != null) {
                    isLiveStream = contentDatum.getGist().isLiveStream();
                }
                setTitle();
                adsUrl = getAdsUrl(contentDatum);
                Log.d(TAG, "CVP Free1 : " + contentDatum.getGist().getFree() + " isLiveStream = " + isLiveStream);

                if (isVideoScheduled()) {
                    return;
                }


                if (!contentDatum.getGist().getFree() && appCMSPresenter.isAppSVOD()) {
                    //check login and subscription first.
                    if (!appCMSPresenter.isUserLoggedIn()) {
                        if (userFreePlayTimeExceeded()) {
                            setBackgroundImage();
                            showRestrictMessage(getUnsubscribeOverlayText());
                            toggleLoginButtonVisibility(true);
                            exitFullScreenPlayer();
                        } else {
                            videoData = contentDatum;
                            /*if (shouldRequestAds) {
                                requestAds(adsUrl);
                            } else */{
                                playVideos(contentDatum);
                                startFreePlayTimer();
                            }

                        }
                    } else {
                        //check subscription data
                        appCMSPresenter.getSubscriptionData(appCMSUserSubscriptionPlanResult -> {
                            try {
                                if (appCMSUserSubscriptionPlanResult != null) {
                                    String subscriptionStatus = appCMSUserSubscriptionPlanResult.getSubscriptionInfo().getSubscriptionStatus();
                                    if ((subscriptionStatus.equalsIgnoreCase("COMPLETED") ||
                                            subscriptionStatus.equalsIgnoreCase("DEFERRED_CANCELLATION"))) {
                                        videoData = contentDatum;
                                        //  if (shouldRequestAds) requestAds(adsUrl);
                                        playVideos(contentDatum);
                                        // start free play time timer
                                    } else if (!userFreePlayTimeExceeded()) {
                                        //user is unsubscribe. So check if video is SVOD+PPV and user has purchased this video then play it.
                                        isVideoPlayble(playble -> {
                                            videoData = contentDatum;
                                            if (playble) {
                                                /*if (shouldRequestAds) {
                                                    requestAds(adsUrl);
                                                } else */{
                                                    playVideos(contentDatum);
                                                }
                                            } else {
                                                /*if (shouldRequestAds) {
                                                    requestAds(adsUrl);
                                                } else */{
                                                    playVideos(contentDatum);
                                                    startFreePlayTimer();
                                                }
                                            }
                                        });


                                    } else {
                                        //USER IS UNSUBSCRIBE . So check if video is SVOD+PPV and user purchased this video. so play it.
                                        isVideoPlayble(playble -> {
                                            if (playble) {
                                                playVideos(contentDatum);
                                            } else {
                                                setBackgroundImage();
                                                showRestrictMessage(getUnsubscribeOverlayText());
                                                toggleLoginButtonVisibility(false);
                                                exitFullScreenPlayer();
                                            }
                                        });

                                    }
                                } else {
                                    isVideoPlayble(playble -> {
                                        if (playble) {
                                            playVideos(contentDatum);
                                        } else {
                                            setBackgroundImage();
                                            showRestrictMessage(getUnsubscribeOverlayText());
                                            toggleLoginButtonVisibility(false);
                                            exitFullScreenPlayer();
                                        }
                                    });

                                }
                            } catch (Exception e) {
                                isVideoPlayble(playble -> {
                                    if (playble) {
                                        playVideos(contentDatum);
                                    } else {
                                        setBackgroundImage();
                                        showRestrictMessage(getUnsubscribeOverlayText());
                                        toggleLoginButtonVisibility(false);
                                        exitFullScreenPlayer();
                                    }
                                });
                            }
                        }, false);
                    }
                } else {
                    videoData = contentDatum;

                    /*if (shouldRequestAds&&!appCMSPresenter.isNewsTemplate()) {
                        requestAds(adsUrl);
                    } else
                        */{
                        playVideos(contentDatum);
                    }
                }

                videoTitle = contentDatum.getGist().getTitle();
            } else {
                this.contentDatum = _contentDatum;
                if (isVideoScheduled()) {
                    return;
                }

                if (appCMSPresenter.getAppCMSMain().isMonetizationModelEnabled()
                        && _contentDatum != null && _contentDatum.getSubscriptionPlans() != null){
                    toggleLoginButtonVisibility(true);
                }else if (appCMSPresenter.isAppAVOD()) {
                    hideProgressBar();
                    showRestrictMessage(appCMSPresenter.getLocalisedStrings().getContentNotAvailable());
                    toggleLoginButtonVisibility(false);
                } else if (appCMSPresenter.isUserLoggedIn()) {
                    if (!appCMSPresenter.isUserSubscribed()) {
                        setBackgroundImage();
                        showRestrictMessage(getUnsubscribeOverlayText());
                        toggleLoginButtonVisibility(false);
                        exitFullScreenPlayer();
                    }
                } else {
                    setBackgroundImage();
                    showRestrictMessage(getUnsubscribeOverlayText());
                    toggleLoginButtonVisibility(true);
                    exitFullScreenPlayer();
                }
                if (trailerCompletedCallback != null) {
                    trailerCompletedCallback.videoCompleted();
                }
            }
        },null,false,true,_contentDatum);
    }


    private void stopTimer(){
        if (timer != null) {
            timer.cancel();
            timer = null;
            Log.d(TAG, "CVP timer cancelled");
        }
        if (timerTask != null) {
            timerTask.cancel();
            timerTask = null;
            Log.d(TAG, "CVP timerTask cancelled");
        }
    }
    private void startFreePlayTimer() {
        if (timer != null || timerTask != null) {
            /*Means timer is already running*/
            return;
        }
        if(contentDatum == null){
            return;
        }
        if (!appCMSPresenter.isAppSVOD()) {
            return;
        }
        if(contentDatum.getGist() != null && contentDatum.getGist().getFree()){
            /*The video is free*/
            Log.d(TAG, "CVP Free : " + contentDatum.getGist().getFree());
            return;
        }
        Log.d(TAG, "CVP starting timer");
        final int totalFreePreviewTimeInMillis = getTotalFreePreviewTime();
        timer = new Timer();
        timerTask = new TimerTask() {
            @Override
            public void run() {
                if (appCMSPresenter.getUserFreePlayTimePreference() >= totalFreePreviewTimeInMillis) {
                    stopTimer();
                    appCMSPresenter.getCurrentActivity().runOnUiThread(() -> {
                        String message = getUnsubscribeOverlayText();
                        if (appCMSPresenter.isUserLoggedIn()) {
                            String finalMessage = message;

                                appCMSPresenter.getSubscriptionData(appCMSUserSubscriptionPlanResult -> {
                                    try {
                                        if (appCMSUserSubscriptionPlanResult != null) {
                                            String subscriptionStatus = appCMSUserSubscriptionPlanResult.getSubscriptionInfo().getSubscriptionStatus();
                                            if (!(subscriptionStatus.equalsIgnoreCase("COMPLETED") ||
                                                    subscriptionStatus.equalsIgnoreCase("DEFERRED_CANCELLATION"))) {
                                                //USER IS UBSUBSCRIBE
                                                isVideoPlayble(playble -> {
                                                    if(!playble){
                                                        pausePlayer();
                                                        setBackgroundImage();
                                                        showRestrictMessage(finalMessage);
                                                        toggleLoginButtonVisibility(false);
                                                        exitFullScreenPlayer();
                                                    }
                                                });

                                            }
                                        } else /*Unsubscribed*/ {
                                            //USER IS UBSUBSCRIBE
                                            isVideoPlayble(playble -> {
                                                if(!playble){
                                                    pausePlayer();
                                                    setBackgroundImage();
                                                    showRestrictMessage(finalMessage);
                                                    toggleLoginButtonVisibility(false);
                                                    exitFullScreenPlayer();
                                                }
                                            });
                                        }
                                    } catch (Exception e) {
                                        //USER IS UBSUBSCRIBE
                                        isVideoPlayble(playble -> {
                                            if(!playble){
                                                pausePlayer();
                                                setBackgroundImage();
                                                showRestrictMessage(finalMessage);
                                                toggleLoginButtonVisibility(false);
                                                exitFullScreenPlayer();
                                            }
                                        });
                                    }
                                }, false);

                        } else {
                            pausePlayer();
                            showRestrictMessage(message);
                            setBackgroundImage();
                            toggleLoginButtonVisibility(true);
                            exitFullScreenPlayer();
                        }
                    });

                    return;
                }
                if (null != getPlayer() &&
                        getPlayer().getPlayWhenReady()) {
                    Log.d(TAG, "CVP Timer called off. " + (appCMSPresenter.getUserFreePlayTimePreference() + 1000));
                    appCMSPresenter.setUserFreePlayTimePreference(appCMSPresenter.getUserFreePlayTimePreference() + 1000);
                }
            }
        };
        timer.scheduleAtFixedRate(timerTask, 0, 1000);
    }

    private void isVideoPlayble(Action1<Boolean> playbleAction){
        if(contentDatum != null && contentDatum.getPricing() != null
                && ( "SVOD+PPV".equalsIgnoreCase(contentDatum.getPricing().getType())))
        {
            appCMSPresenter.getTransactionData(contentDatum.getGist().getId(), maps -> {
                if (maps != null
                        && maps.get(0) != null
                        && maps.get(0).get(contentDatum.getGist().getId()) != null) {
                    AppCMSTransactionDataValue appCMSTransactionDataValue = maps.get(0).get(contentDatum.getGist().getId());
                    //video is playble.
                    playbleAction.call(true);
                }else {
                    //video is not playble.
                    playbleAction.call(false);
                }
            }, contentDatum.getGist().getContentType());
        }else{
            playbleAction.call(false);
        }

    }


    @NonNull
    private String getUnsubscribeOverlayText() {
        String message;
        if (!appCMSPresenter.isUserLoggedIn()) {
            message = appCMSPresenter.getLocalisedStrings().getEncourageUserLoginText();

            if (contentDatum != null && contentDatum.getPricing() != null
                    && ("SVOD+PPV".equalsIgnoreCase(contentDatum.getPricing().getType()))) {
                message = appCMSPresenter.getLocalisedStrings().getCannotPurchaseItemMsg(appCMSPresenter.getAppCMSMain().getDomainName());
            }
        } else {
            message = appCMSPresenter.getLocalisedStrings().getEncourageUserLoginText();
            if (contentDatum != null && contentDatum.getPricing() != null
                    && ("SVOD+PPV".equalsIgnoreCase(contentDatum.getPricing().getType()))) {
                message = appCMSPresenter.getLocalisedStrings().getCannotPurchaseItemMsg(appCMSPresenter.getAppCMSMain().getDomainName());
            }
        }
        return message;
    }

    private boolean userFreePlayTimeExceeded() {
        final long userFreePlayTime = appCMSPresenter.getUserFreePlayTimePreference();
        final int maxPreviewSecs = getTotalFreePreviewTime();
        return userFreePlayTime >= maxPreviewSecs;
    }


    /**
     * Checks the value of the AppCMSMain > Features > Free Preview > Length > Unit > Multiplier and
     * return the value in milliseconds
     *
     * @return returns the value of free preview in milliseconds
     */
    private int getTotalFreePreviewTime() {
        AppCMSMain appCMSMain = appCMSPresenter.getAppCMSMain();
        int entitlementCheckMultiplier = 0;
        if (appCMSMain != null &&
                appCMSMain.getFeatures() != null &&
                appCMSMain.getFeatures().getFreePreview() != null &&
                appCMSMain.getFeatures().getFreePreview().isFreePreview()) {
            if (appCMSMain.getFeatures().getFreePreview().getLength() != null &&
                    appCMSMain.getFeatures().getFreePreview().getLength().getUnit().equalsIgnoreCase("Minutes")) {
                try {
                    entitlementCheckMultiplier = Integer.parseInt(appCMSMain.getFeatures().getFreePreview().getLength().getMultiplier());
                } catch (Exception e) {
                    Log.e(TAG, "Error parsing free preview multiplier value: " + e.getMessage());
                }
            }
        }else{
            entitlementCheckMultiplier = 0; //isFreePreview is false that means we have to show preview dialog immediate.
        }
        return entitlementCheckMultiplier * 60 * 1000;
    }

    String lastUrl = null,closedCaptionUri = "";

    protected String getVideoUrl(VideoAssets videoAssets) {
        String defaultVideoResolution = mContext.getResources().getString(R.string.default_video_resolution);
        String videoUrl = videoAssets.getHls();

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

    @Override
    public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {

        if (beaconMessageThread != null) {
            beaconMessageThread.playbackState = playbackState;
        }

        switch (playbackState) {
            case STATE_ENDED:
//                if(appCMSPresenter.isFullScreenVisible) {
//                    getPlayerView().showController();
//                }
                if (trailerCompletedCallback != null) {
                    trailerCompletedCallback.videoCompleted();
                }else {
                    currentPlayingIndex++;
                    Log.d(TAG, "appCMSPresenter.getAutoplayEnabledUserPref(mContext): " +
                            appCMSPresenter.getAutoplayEnabledUserPref(mContext));
                    if (null != relatedVideoId
                            && currentPlayingIndex <= relatedVideoId.size() - 1) {
                        if (appCMSPresenter.getAutoplayEnabledUserPref(mContext)) {
                            imageViewContainer.setVisibility(View.GONE);
                            imageView.setVisibility(View.GONE);
                            showProgressBar("Loading Next Video...");
                            setVideoUri(relatedVideoId.get(currentPlayingIndex), videoTitle,contentDatum,false,false);
                        } else /*Autoplay is turned-off*/ {
                            setBackgroundImage();
                            showRestrictMessage(appCMSPresenter.getLanguageResourcesFile().getUIresource(getResources().getString(R.string.autoplay_off_msg)));
                            toggleLoginButtonVisibility(false);
                            exitFullScreenPlayer();
                        }
                    } else {
                        setBackgroundImage();
                        showRestrictMessage(appCMSPresenter.getLanguageResourcesFile().getUIresource(getResources().getString(R.string.no_more_videos_in_queue)));
                        toggleLoginButtonVisibility(false);
                        exitFullScreenPlayer();
                        currentPlayingIndex = -1;
                    }
                }
                break;
            case STATE_BUFFERING:
                showProgressBar(appCMSPresenter.getLocalisedStrings().getLiveStreamingText());

                if (beaconMessageThread != null) {
                    beaconMessageThread.sendBeaconPing = false;
                }
                if (beaconBufferingThread != null) {
                    beaconBufferingThread.sendBeaconBuffering = true;
                    if (!beaconBufferingThread.isAlive()) {
                        beaconBufferingThread.start();
                    }
                }

                break;
            case STATE_READY:
//                if(isTrailer) {
//                    getPlayerView().hideController();
//                }
                hideProgressBar();
                if (!isTrailer && closedCaptionSelector != null) {
                    if (!closedCaptionSelectorCreated) {
                        // create the dialog which contains the CC switcher list
                        initCCAdapter();
                    }
                }

                if (beaconBufferingThread != null) {
                    beaconBufferingThread.sendBeaconBuffering = false;
                }
                if (beaconMessageThread != null) {
                    //if(!isLiveStream)
                    beaconMessageThread.sendBeaconPing = true;
                    if (!beaconMessageThread.isAlive()) {
                        try {
                            beaconMessageThread.start();
                            mTotalVideoDuration = getDuration() / 1000;
                            mTotalVideoDuration -= mTotalVideoDuration % 4;
                            mProgressHandler.post(mProgressRunnable);
                        } catch (Exception e) {

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
                                false);
                        sentBeaconFirstFrame = true;
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
                                false);
                        sentBeaconPlay = true;
                        mStartBufferMilliSec = new Date().getTime();

                        appCMSPresenter.sendGaEvent(getResources().getString(R.string.play_video_action),
                                getResources().getString(R.string.play_video_category),
                                (videoTitle != null && !TextUtils.isEmpty(videoTitle)) ? videoTitle : videoDataId);

                    }
                }
                if (isManualPause() && playWhenReady) {
                    appCMSPresenter.refreshVideoData(contentDatum.getGist().getId(), new Action1<ContentDatum>() {
                                @Override
                                public void call(ContentDatum contentDatum) {
                                    System.out.println(TAG + " refreshVideoData:  " + contentDatum);
                                    if (!TextUtils.isEmpty(contentDatum.getVideoPlayError())) {
                                        setBackgroundImage();
                                        showRestrictMessage(contentDatum.getVideoPlayError());
                                        toggleLoginButtonVisibility(false);
                                        exitFullScreenPlayer();
                                        pausePlayer();
                                        return;
                                    }
                                }
                            },
                            null,
                            false,
                            true,
                            null);
                }
                break;
            default:
                hideProgressBar();
        }
    }

    private void setBackgroundImage() {
        if (mContext instanceof AppCmsHomeActivity) {
            if (((AppCmsHomeActivity) mContext).isFinishing()) {
                return;
            }
        }
        String videoImageUrl = null;
        if (contentDatum != null
                && contentDatum.getGist() != null
                && contentDatum.getGist().getVideoImageUrl() != null) {
            videoImageUrl = contentDatum.getGist().getVideoImageUrl();
        }
        if (!TextUtils.isEmpty(videoImageUrl)) {
            imageViewContainer.setVisibility(VISIBLE);
            imageView.setVisibility(View.VISIBLE);
            Glide.with(mContext)
                    .load(videoImageUrl)
                    .apply(new RequestOptions().diskCacheStrategy(DiskCacheStrategy.RESOURCE))
                    .into(imageView);
        }
    }

    private boolean isPlayerActive = true;
    public void pausePlayer() {
        Log.d(TAG , "pausePlayer()");
        super.pausePlayer();
        stopTimer();
        isPlayerActive = false;
        if (beaconMessageThread != null) {
            beaconMessageThread.sendBeaconPing = false;
        }
        if (beaconBufferingThread != null) {
            beaconBufferingThread.sendBeaconBuffering = false;
        }

    }

    public void resumePlayer() {
        System.out.println("CVP resumePlayer " + player);
        if (null != player && !player.getPlayWhenReady() && !isVideoScheduled()) {
            if (appCMSPresenter.getCurrentActivity() != null && appCMSPresenter.getCurrentActivity() instanceof AppCmsHomeActivity) {
                if (((AppCmsHomeActivity) appCMSPresenter.getCurrentActivity()).isActive()) {
                    startFreePlayTimer();
                    if(!isHardPause() && !isLoginButtonVisible()) {
                        Log.d(TAG , "resumePlayer()");
                        playerView.getPlayer().setPlayWhenReady(true);
                        if (beaconMessageThread != null) {
                          //  if(!isLiveStream)
                            beaconMessageThread.sendBeaconPing = true;
                        }
                        if (beaconBufferingThread != null) {
                            beaconBufferingThread.sendBeaconBuffering = true;
                        }
                        isPlayerActive = true;
                    }
                }
            }
        }
    }

    private void createLoader() {
        custonLoaderContaineer = new LinearLayout(mContext);
        custonLoaderContaineer.setOrientation(LinearLayout.VERTICAL);
        custonLoaderContaineer.setGravity(Gravity.CENTER);
        ProgressBar progressBar = new ProgressBar(mContext);
        progressBar.setIndeterminate(true);
        progressBar.getIndeterminateDrawable().
                setColorFilter(Color.parseColor(Utils.getProgressBarColor(mContext, appCMSPresenter)),
                        PorterDuff.Mode.MULTIPLY
                );
        LinearLayout.LayoutParams progressbarParam = new LinearLayout.LayoutParams(50, 50);
        progressBar.setLayoutParams(progressbarParam);
        custonLoaderContaineer.addView(progressBar);
        loaderMessageView = new TextView(mContext);
        LinearLayout.LayoutParams textViewParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        loaderMessageView.setLayoutParams(textViewParams);
        custonLoaderContaineer.addView(loaderMessageView);
        this.addView(custonLoaderContaineer);
    }

    private Button loginButton ,  cancelButton;
    private void createCustomMessageView() {
        customMessageContaineer = new LinearLayout(mContext);
        customMessageContaineer.setOrientation(LinearLayout.VERTICAL);
        customMessageContaineer.setGravity(Gravity.CENTER);
        customMessageView = new TextView(mContext);
        customMessageView.setGravity(Gravity.CENTER);
        customMessageView.setTextSize(20);
        customMessageView.setTypeface(Utils.getSpecificTypeface(mContext,
                appCMSPresenter,
                mContext.getString(R.string.app_cms_page_font_bold_key)));
        LinearLayout.LayoutParams textViewParams = new LinearLayout.LayoutParams(Utils.getViewXAxisAsPerScreen(mContext , 1400), ViewGroup.LayoutParams.WRAP_CONTENT);
        textViewParams.setMargins(0, 0,0, 50);
        customMessageView.setLayoutParams(textViewParams);
        customMessageView.setPadding(20, 20, 20, 20);
        if (customMessageView.getParent() != null) {
            ((ViewGroup) customMessageView.getParent()).removeView(customMessageView);
        }
        customMessageContaineer.addView(customMessageView);
        customMessageContaineer.setVisibility(View.INVISIBLE);


        loginButton = new Button(mContext);
        loginButton.setText(appCMSPresenter.getLocalisedStrings().getSignInText());
        LinearLayout.LayoutParams buttonParams = new LinearLayout.LayoutParams(350, 75);
        loginButton.setLayoutParams(buttonParams);
        loginButton.setPadding(50,0,50,0);
        loginButton.setBackgroundColor(Color.parseColor(appCMSPresenter.getAppCtaBackgroundColor()));
        loginButton.setTextColor(Color.parseColor(appCMSPresenter.getAppCtaTextColor()));
        customMessageContaineer.addView(loginButton);
        loginButton.setVisibility(View.VISIBLE);

        loginButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if(appCMSPresenter.isNetworkConnected()) {
                    NavigationUser navigationUser = appCMSPresenter.getLoginNavigation();
                    if (null != navigationUser) {
                        appCMSPresenter.setLaunchType(AppCMSPresenter.LaunchType.LOGIN_FROM_MINI_PLAYER);
                        appCMSPresenter.setCurrentContentDatum(contentDatum);
                        appCMSPresenter.navigateToTVPage(
                                 navigationUser.getPageId(),
                                navigationUser.getTitle(),
                                navigationUser.getUrl(),
                                false,
                                Uri.EMPTY,
                                false,
                                false,
                                true, false, false, false);
                        //appCMSPresenter.openEntitlementScreen(contentDatum);
                    }
                }
            }
        });
        createTvodPurchaseButton();
        this.addView(customMessageContaineer);
    }

    private Button tvodPurchaseutton;
    private void createTvodPurchaseButton(){
        tvodPurchaseutton = new Button(mContext);
        tvodPurchaseutton.setText(appCMSPresenter.getLocalisedStrings().getWaysToWatchText());
        LinearLayout.LayoutParams buttonParams = new LinearLayout.LayoutParams(350, 75);
        tvodPurchaseutton.setLayoutParams(buttonParams);
        tvodPurchaseutton.setPadding(50,0,50,0);
        tvodPurchaseutton.setBackgroundColor(Color.parseColor(appCMSPresenter.getAppCtaBackgroundColor()));
        tvodPurchaseutton.setTextColor(Color.parseColor(appCMSPresenter.getAppCtaTextColor()));
        customMessageContaineer.addView(tvodPurchaseutton);
        tvodPurchaseutton.setVisibility(View.INVISIBLE);
        tvodPurchaseutton.setOnClickListener(v -> {
            appCMSPresenter.openEntitlementScreen(contentDatum,true);
        });
    }

    private void showProgressBar(String text) {
        if (null != custonLoaderContaineer && null != loaderMessageView) {
            loaderMessageView.setText(text);
            custonLoaderContaineer.setVisibility(View.VISIBLE);
        }
    }

    protected void hideProgressBar() {
        if (null != custonLoaderContaineer) {
            custonLoaderContaineer.setVisibility(View.INVISIBLE);
        }
    }

    public void toggleLoginButtonVisibility (boolean show) {
        if (appCMSPresenter.getAppCMSMain().isMonetizationModelEnabled()
            && contentDatum != null && contentDatum.getSubscriptionPlans() != null) {
            if(show) showRestrictMessage(appCMSPresenter.getLocalisedStrings().getWaysToWatchMessageText());
            tvodPurchaseutton.setVisibility(show ? VISIBLE : GONE);
            loginButton.setVisibility(View.GONE);
        }else if (loginButton != null) {
            loginButton.setVisibility(show ? VISIBLE : GONE);
        }
    }

    public boolean isLoginButtonVisible(){
        return ( (loginButton.getVisibility() == View.VISIBLE)
                && (customMessageContaineer.getVisibility() == View.VISIBLE));
    }

    public boolean isTvodSubscribeButtonVisible(){
        return ( (tvodPurchaseutton.getVisibility() == View.VISIBLE)
                && (customMessageContaineer.getVisibility() == View.VISIBLE));
    }


    public void performLoginButtonClick(){
        if(null != loginButton){
            loginButton.performClick();
        }
    }
    public void showRestrictMessage(String message) {
        if (null != customMessageContaineer && null != customMessageView) {
            hideProgressBar();
            customMessageView.setText(message);
            customMessageContaineer.setVisibility(View.VISIBLE);
            isVideoPlaying = false;
            // loginButton.requestFocus();
        }
    }

    protected void hideRestrictedMessage() {
        if (null != customMessageContaineer) {
            customMessageContaineer.setVisibility(View.INVISIBLE);
            hideBackgroundImage();
        }
    }

    private void requestAds(String adTagUrl) {
        if (!TextUtils.isEmpty(adTagUrl) && adsLoader != null) {
            Log.d(TAG, "Requesting ads: " + adTagUrl);
            AdDisplayContainer adDisplayContainer = sdkFactory.createAdDisplayContainer();
            adDisplayContainer.setAdContainer(this);

            AdsRequest request = sdkFactory.createAdsRequest();
            request.setAdTagUrl(adTagUrl);
            request.setAdDisplayContainer(adDisplayContainer);

            adsLoader.requestAds(request);
            isAdsDisplaying = true;

            apod += 1;
            if (appCMSPresenter != null && appCMSPresenter.getAppCMSMain().getFeatures().getAnalyticsBeacon().isEnabled()) {
                appCMSPresenter.sendBeaconMessage(contentDatum.getGist().getId(),
                        contentDatum.getGist().getPermalink(),
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
                        false);
            }
        }
    }

    @Override
    public void onAdError(AdErrorEvent adErrorEvent) {
        Log.e(TAG, "OnAdError: " + adErrorEvent.getError().getMessage());
        if(isPlayerActive) {
            playVideos(contentDatum);
            startFreePlayTimer();
        }
        // startPlayer();
    }

    @Override
    public void onAdEvent(AdEvent adEvent) {
        Log.d(TAG, "onAdEvent: " + adEvent.getType());

        switch (adEvent.getType()) {
            case LOADED:
                if(null != adsManager) {
                    adsManager.start();
                    isAdsDisplaying = true;
                }
                break;
            case CONTENT_PAUSE_REQUESTED:
                isAdDisplayed = true;
                if (beaconMessageThread != null) {
                    beaconMessageThread.sendBeaconPing = false;
                }

                if (appCMSPresenter != null && appCMSPresenter.getAppCMSMain().getFeatures().getAnalyticsBeacon().isEnabled()) {
                    appCMSPresenter.sendBeaconMessage(contentDatum.getGist().getId(),
                            contentDatum.getGist().getPermalink(),
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
                            false);
                }
                getPlayer().setPlayWhenReady(false);
                break;
            case CONTENT_RESUME_REQUESTED:
                isAdDisplayed = false;
                break;
            case ALL_ADS_COMPLETED:
            case COMPLETED:
                if (adsManager != null) {
                    adsManager.destroy();
                    adsManager = null;
                }
                isAdsDisplaying = false;
                if(isPlayerActive) {
                    playVideos(contentDatum);
                    startFreePlayTimer();
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void onRefreshTokenCallback() {

    }

    @Override
    public void onFinishCallback(String message) {

        AppCMSPresenter.BeaconEvent event;
        if (message.contains("Unable")) {// If video position is something else then 0 It is dropped in between playing
            event = AppCMSPresenter.BeaconEvent.DROPPED_STREAM;
        } else if (message.contains("Response")) {
            event = AppCMSPresenter.BeaconEvent.FAILED_TO_START;
        } else {
            event = AppCMSPresenter.BeaconEvent.FAILED_TO_START;
        }

        appCMSPresenter.sendBeaconMessage(videoData.getGist().getId(),
                videoData.getGist().getPermalink(),
                parentScreenName,
                getCurrentPosition(),
                false,
                event,
                "Video",
                String.valueOf(getBitrate()),
                String.valueOf(getHeight()),
                String.valueOf(getWidth()),
                mStreamId,
                0d,
                0,
                false);
        if (!TextUtils.isEmpty(message)) {
            Toast.makeText(mContext, message, Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void playerError(ExoPlaybackException ex) {

    }

    /**
     * Method is used to hide the progress bar, timer, rewind and forward button when a live stream
     * playing
     */
    public void hideControlsForLiveStream() {
        try {
            getPlayerView().findViewById(R.id.exo_position).setVisibility(isLiveStream ? GONE : VISIBLE);
            getPlayerView().findViewById(R.id.exo_progress).setVisibility(isLiveStream ? GONE : VISIBLE);
            getPlayerView().findViewById(R.id.exo_duration).setVisibility(isLiveStream ? GONE : VISIBLE);

            if (isLiveStream) {
                View rewind = getPlayerView().findViewById(R.id.exo_rew);
                rewind.setTag(rewind.getVisibility());
                    rewind.getViewTreeObserver().addOnGlobalLayoutListener(() -> {
                    if (rewind.getVisibility() == VISIBLE) {
                        rewind.setVisibility(GONE);
                    }
                });

                View forward = getPlayerView().findViewById(R.id.exo_ffwd);
                forward.setTag(rewind.getVisibility());
                forward.getViewTreeObserver().addOnGlobalLayoutListener(() -> {
                    if (forward.getVisibility() == VISIBLE) {
                        forward.setVisibility(GONE);
                    }
                });
            }
        } catch (Exception e) {
        }
    }


    LinearLayout headerTitleContaineer;
    TextView titleView;
    private void createTitleView(){
        headerTitleContaineer = new LinearLayout(getContext());
        LayoutParams containeerParam = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT , Utils.getViewYAxisAsPerScreen(mContext , 100));
        containeerParam.gravity = Gravity.TOP;

        headerTitleContaineer.setLayoutParams(containeerParam);
        headerTitleContaineer.setGravity(Gravity.CENTER_VERTICAL);
        headerTitleContaineer.setBackgroundColor(getResources().getColor(R.color.appcms_shadow_color));

        titleView = new TextView(getContext());
        LinearLayout.LayoutParams textViewParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT ,LinearLayout.LayoutParams.WRAP_CONTENT);
        textViewParams.leftMargin = 45;
        titleView.setLayoutParams(textViewParams);
        titleView.setSingleLine(true);
        titleView.setEllipsize(TextUtils.TruncateAt.MARQUEE);
        titleView.setBackgroundColor(getResources().getColor(android.R.color.transparent));
        titleView.setTextColor(getResources().getColor(android.R.color.white)/*Color.parseColor(Utils.getTextColor(getContext() , appCMSPresenter))*/);
        titleView.setTextSize(24);
        headerTitleContaineer.addView(titleView);
        addView(headerTitleContaineer);
        headerTitleContaineer.setVisibility(View.INVISIBLE);
    }

    private void setTitle(){
        if(null != titleView){
            titleView.setText(contentDatum.getGist().getTitle());
        }

    }

    private void exitFullScreenPlayer(){
        getPlayerView().hideController();
        getPlayerView().setUseController(false);
        headerTitleContaineer.setVisibility(INVISIBLE);
        new Handler().postDelayed(() -> {
            appCMSPresenter.exitFullScreenTVPlayer();
        },100);
    }


    public void onPlayerError(ExoPlaybackException e) {
        if(!isErrorOccurred) {
            String errorString = null;
            e.printStackTrace();
            if (e instanceof ExoPlaybackException
                    && (e.type == ExoPlaybackException.TYPE_RENDERER)) {
                isErrorOccurred = true;


                ClearDialogFragment newFragment = Utils.getExitDialogFragment(
                        mContext,
                        appCMSPresenter,
                        getResources().getDimensionPixelSize(R.dimen.text_clear_dialog_width),
                        getResources().getDimensionPixelSize(R.dimen.text_add_to_watchlist_sign_in_dialog_height),
                        mContext.getString(R.string.error),
                        mContext.getString(R.string.media_codec_error),
                        mContext.getString(R.string.ok),
                        14
                );

                newFragment.setOnPositiveButtonClicked(s -> {

                    Intent mStartActivity = new Intent(mContext, AppCmsTVSplashActivity.class);
                    int mPendingIntentId = 123456;
                    PendingIntent mPendingIntent = PendingIntent.getActivity(mContext, mPendingIntentId, mStartActivity,
                            PendingIntent.FLAG_CANCEL_CURRENT);
                    AlarmManager mgr = (AlarmManager) mContext.getApplicationContext().getSystemService(Context.ALARM_SERVICE);
                    mgr.set(AlarmManager.RTC, System.currentTimeMillis() + 100, mPendingIntent);

                    System.exit(0);
                });

                newFragment.setOnBackKeyListener(s -> {

                    Intent mStartActivity = new Intent(mContext, AppCmsTVSplashActivity.class);
                    int mPendingIntentId = 123456;
                    PendingIntent mPendingIntent = PendingIntent.getActivity(mContext, mPendingIntentId, mStartActivity,
                            PendingIntent.FLAG_CANCEL_CURRENT);
                    AlarmManager mgr = (AlarmManager) mContext.getApplicationContext().getSystemService(Context.ALARM_SERVICE);
                    mgr.set(AlarmManager.RTC, System.currentTimeMillis() + 100, mPendingIntent);

                    System.exit(0);
                });

            }else if (isBehindLiveWindow(e)) {
                init(mContext);
                updateToken(Uri.parse(lastUrl), closedCaptionUri == null ? null : Uri.parse(String.valueOf(closedCaptionUri)));
            }else if(e instanceof ExoPlaybackException
                    && (e.type == ExoPlaybackException.TYPE_SOURCE)){
                showRestrictMessage(mContext.getString(R.string.media_source_error));
                toggleLoginButtonVisibility(false);
            }
        }
    }

    private void setBeaconData() {
        try {
            mStreamId = appCMSPresenter.getStreamingId(videoDataId);
        } catch (Exception e) {
            mStreamId = videoDataId + appCMSPresenter.getCurrentTimeStamp();
        }
        beaconBufferingThread.setBeaconData(videoDataId, permaLink, mStreamId);
        beaconMessageThread.setBeaconData(videoDataId, permaLink, mStreamId);
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

                setUri(Uri.parse(lastUrl), closedCaptionUri == null ? null : Uri.parse(String.valueOf(closedCaptionUri)));
                if (null != appCMSPresenter.getCurrentActivity() &&
                        appCMSPresenter.getCurrentActivity() instanceof AppCmsHomeActivity) {
                    if (((AppCmsHomeActivity) appCMSPresenter.getCurrentActivity()).isActive()
                            && !isHardPause()) {
                        player.setPlayWhenReady(true);
                    } else {
                        player.setPlayWhenReady(false);
                    }
                    if(Utils.getCustomTVVideoPlayerView() != null) {
                        if (Utils.isPlayerSelected) {
                            Utils.getCustomTVVideoPlayerView().getPlayer().setVolume(1);
                        } else {
                            Utils.getCustomTVVideoPlayerView().getPlayer().setVolume(0);
                        }
                    }
                }

            }},null,false, false,null);
    }

    public void setTrailerCompletedCallback(TrailerCompletedCallback trailerCompletedCallback) {
        this.trailerCompletedCallback = trailerCompletedCallback;
    }

    public void removeTimerCallBack() {
        try {
            if (visualTimerframeLayout != null) {
                VisualTimer visualTimer  = visualTimerframeLayout.findViewById(R.id.visualTimer);
                visualTimer.setCallBack(null);
                removeView(visualTimerframeLayout);
                visualTimerframeLayout = null;
            }
        }catch (Exception e){

        }
    }

    @Override
    public List<ClosedCaptions> getAvailableClosedCaptions() {
        List<ClosedCaptions> closedCaptionsList = new ArrayList<>();

        if (!isTrailer && contentDatum != null
                && contentDatum.getContentDetails() != null
                && contentDatum.getContentDetails().getClosedCaptions() != null) {
            ArrayList<ClosedCaptions> closedCaptions = contentDatum.getContentDetails().getClosedCaptions();
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

        if (contentDatum != null
                && contentDatum.getContentDetails() != null
                && contentDatum.getContentDetails().getClosedCaptions() != null) {
            ArrayList<ClosedCaptions> closedCaptions = contentDatum.getContentDetails().getClosedCaptions();
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

    private void sendProgressAnalyticEvents(long progressPercent) {
//        Log.d(TAG, "sendBeaconMessage() sendProgressAnalyticEvents; " + progressPercent);
        try {
        Bundle bundle = new Bundle();
        if(null != contentDatum){
            if(null != contentDatum.getGist()){
                bundle.putString(FIREBASE_VIDEO_ID_KEY, contentDatum.getGist().getId());
                bundle.putString(FIREBASE_VIDEO_NAME_KEY, contentDatum.getGist().getTitle());
                bundle.putString(FIREBASE_SERIES_ID_KEY, contentDatum.getGist().getSeriesId());
            }
            bundle.putString(FIREBASE_SERIES_NAME_KEY, contentDatum.getSeriesName());
        }


        bundle.putString(FIREBASE_PLAYER_NAME_KEY, FIREBASE_PLAYER_NATIVE);
        bundle.putString(FIREBASE_MEDIA_TYPE_KEY, FIREBASE_MEDIA_TYPE_VIDEO);


        bundle.putLong("content_seconds_watched", getDuration());
        String platform;
        if (com.viewlift.Utils.isFireTVDevice(appCMSPresenter.getCurrentContext())) {
            platform = mContext.getString(R.string.app_cms_query_param_amazon_platform);
        } else if (appCMSPresenter.getPlatformType().equals(AppCMSPresenter.PlatformType.TV)) {
            platform = mContext.getString(R.string.app_cms_query_param_android_tv);
        } else {
            platform = mContext.getString(R.string.app_cms_query_param_android_platform);
        }
        bundle.putString("platform_name", platform);
        bundle.putString("network_name", mContext.getApplicationInfo().loadLabel(mContext.getPackageManager()).toString());
        String videoType = "free";
        if (null != contentDatum && null != contentDatum.getGist() && !contentDatum.getGist().isFree())
            videoType = "restricted";
        bundle.putString("video_asset_status", videoType);
        if (appCMSPresenter.getAppPreference().getTvProvider() != null)
            bundle.putString("tvprovider_name", appCMSPresenter.getAppPreference().getTvProvider());

        //Logs an app event.
        if (progressPercent == 0 && !isStreamStart) {
            appCMSPresenter.getmFireBaseAnalytics().logEvent(FIREBASE_STREAM_START, bundle);
            isStreamStart = true;
            //Log.d(TAG, "sendBeaconMessage() called with: FIREBASE_STREAM_START; " + contentDatum.getGist().getTitle());
        }

        if (!isStreamStart) {
            appCMSPresenter.getmFireBaseAnalytics().logEvent(FIREBASE_STREAM_START, bundle);
            isStreamStart = true;
          //  Log.d(TAG, "sendBeaconMessage() called with: FIREBASE_STREAM_START; " + contentDatum.getGist().getTitle());
        }

        if (progressPercent >= 25 && progressPercent < 50 && !isStream25) {
            if (!isStreamStart) {
                appCMSPresenter.getmFireBaseAnalytics().logEvent(FIREBASE_STREAM_START, bundle);
                isStreamStart = true;
               // Log.d(TAG, "sendBeaconMessage() called with: FIREBASE_STREAM_START; " + contentDatum.getGist().getTitle());
            }

            appCMSPresenter.getmFireBaseAnalytics().logEvent(FIREBASE_STREAM_25, bundle);
            isStream25 = true;
        }

        if (progressPercent >= 50 && progressPercent < 75 && !isStream50) {
            if (!isStream25) {
                appCMSPresenter.getmFireBaseAnalytics().logEvent(FIREBASE_STREAM_25, bundle);
                isStream25 = true;
            }

            appCMSPresenter.getmFireBaseAnalytics().logEvent(FIREBASE_STREAM_50, bundle);
            isStream50 = true;
        }

        if (progressPercent >= 75 && progressPercent < 95 && !isStream75) {
            if (!isStream25) {
                appCMSPresenter.getmFireBaseAnalytics().logEvent(FIREBASE_STREAM_25, bundle);
                isStream25 = true;
            }

            if (!isStream50) {
                appCMSPresenter.getmFireBaseAnalytics().logEvent(FIREBASE_STREAM_50, bundle);
                isStream50 = true;
            }

            appCMSPresenter.getmFireBaseAnalytics().logEvent(FIREBASE_STREAM_75, bundle);
            isStream75 = true;
        }
        if (progressPercent >= 95 && progressPercent <= 98 && !isStream95) {
            if (!isStream25) {
                appCMSPresenter.getmFireBaseAnalytics().logEvent(FIREBASE_STREAM_25, bundle);
                isStream25 = true;
            }

            if (!isStream50) {
                appCMSPresenter.getmFireBaseAnalytics().logEvent(FIREBASE_STREAM_50, bundle);
                isStream50 = true;
            }
            if (!isStream75) {
                appCMSPresenter.getmFireBaseAnalytics().logEvent(FIREBASE_STREAM_75, bundle);
                isStream75 = true;
            }
            appCMSPresenter.getmFireBaseAnalytics().logEvent(FIREBASE_STREAM_95, bundle);
            isStream95 = true;
        }

        if (progressPercent >= 98 && progressPercent <= 100 && !isStream100) {
            if (!isStream25) {
                appCMSPresenter.getmFireBaseAnalytics().logEvent(FIREBASE_STREAM_25, bundle);
                isStream25 = true;
            }

            if (!isStream50) {
                appCMSPresenter.getmFireBaseAnalytics().logEvent(FIREBASE_STREAM_50, bundle);
                isStream50 = true;
            }

            if (!isStream75) {
                appCMSPresenter.getmFireBaseAnalytics().logEvent(FIREBASE_STREAM_75, bundle);
                isStream75 = true;
            }
            if (!isStream95) {
                appCMSPresenter.getmFireBaseAnalytics().logEvent(FIREBASE_STREAM_95, bundle);
                isStream95 = true;
            }

            appCMSPresenter.getmFireBaseAnalytics().logEvent(FIREBASE_STREAM_100, bundle);
            isStream100 = true;
        }
       }catch(Exception e){
            e.printStackTrace(); }
    }
    Boolean isPromo=false;
    public Boolean getPromo() {
        return isPromo;
    }

    public void setPromo(Boolean promo) {
        this.isPromo = promo;
    }

    Boolean isTabIsClicked=false;
    public void setTabIsClicked(Boolean isTabIsClicked) {
        this.isTabIsClicked = isTabIsClicked;
    }

    private void resetStreamFlags(){
        isStreamStart = false;
        isStream25 = false;
        isStream50 = false;
        isStream75 = false;
        isStream95 = false;
        isStream100 = false;
    }
    boolean useAdUrl=true;
    public void setUseAdUrl(boolean useAdUrl) {
        this.useAdUrl = useAdUrl;
    }

    public void performTvodPurchaseButtonClick() {
        if(tvodPurchaseutton != null){
            tvodPurchaseutton.performClick();
        }
    }
}
