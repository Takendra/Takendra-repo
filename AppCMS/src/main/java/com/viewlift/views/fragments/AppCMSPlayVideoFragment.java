package com.viewlift.views.fragments;

import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.GradientDrawable;
import android.media.AudioManager;
import android.media.MediaDrm;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.UiThread;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.percentlayout.widget.PercentLayoutHelper;
import androidx.percentlayout.widget.PercentRelativeLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.drm.DrmSession;
import com.google.android.exoplayer2.drm.KeysExpiredException;
import com.google.android.exoplayer2.offline.Download;
import com.google.android.gms.cast.framework.CastSession;
import com.viewlift.AppCMSApplication;
import com.viewlift.BuildConfig;
import com.viewlift.R;
import com.viewlift.analytics.AppsFlyerUtils;
import com.viewlift.casting.CastHelper;
import com.viewlift.casting.CastServiceProvider;
import com.viewlift.db.AppPreference;
import com.viewlift.models.data.appcms.api.AppCMSSignedURLResult;
import com.viewlift.models.data.appcms.api.AppCMSTransactionDataValue;
import com.viewlift.models.data.appcms.api.ContentDatum;
import com.viewlift.models.data.appcms.api.Module;
import com.viewlift.models.data.appcms.beacon.BeaconBuffer;
import com.viewlift.models.data.appcms.beacon.BeaconHandler;
import com.viewlift.models.data.appcms.beacon.BeaconPing;
import com.viewlift.models.data.appcms.beacon.BeaconRunnable;
import com.viewlift.models.data.appcms.downloads.DownloadClosedCaptionRealm;
import com.viewlift.models.data.appcms.history.UpdateHistoryResponse;
import com.viewlift.models.data.appcms.ui.main.AppCMSMain;
import com.viewlift.models.data.appcms.ui.main.LocalisedStrings;
import com.viewlift.offlinedrm.OfflineVideoData;
import com.viewlift.models.data.appcms.ui.page.Component;
import com.viewlift.presenters.AppCMSPresenter;
import com.viewlift.utils.CommonUtils;
import com.viewlift.utils.ContentTypeChecker;
import com.viewlift.views.activity.AppCMSPlayVideoActivity;
import com.viewlift.views.adapters.ClosedCaptionSelectorAdapter;
import com.viewlift.views.adapters.HLSStreamingQualitySelectorAdapter;
import com.viewlift.views.adapters.LanguageSelectorAdapter;
import com.viewlift.views.adapters.StreamingQualitySelectorAdapter;
import com.viewlift.views.customviews.PlayerSettingsView;
import com.viewlift.views.customviews.VideoPlayerView;
import com.viewlift.views.customviews.ViewCreator;
import com.viewlift.views.customviews.constraintviews.TimerViewFutureContent;
import com.viewlift.views.dialog.RestWorkoutDialog;
import com.viewlift.views.listener.PlayerPlayPauseState;
import com.viewlift.views.listener.VideoSelected;

import java.net.CookieHandler;
import java.net.CookieManager;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Optional;
import butterknife.Unbinder;
import rx.functions.Action0;
import rx.functions.Action1;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

@SuppressWarnings("deprecation")
public class AppCMSPlayVideoFragment extends Fragment
        implements
        VideoPlayerView.ErrorEventListener,
        VideoPlayerView.OnBeaconAdsEvent,
        VideoPlayerView.VideoPlayerSettingsEvent,
        Animation.AnimationListener,
        OnResumeVideo,
        PlayerPlayPauseState {
    private static final String TAG = "PlayVideoFragment";

    public static final long SECS_TO_MSECS = 1000L;
    private static final String PLAYER_SCREEN_NAME = "Player Screen";
    private static double ttfirstframe = 0d;
    private static int apod = 0;
    private static boolean isVideoDownloaded;
    private static boolean isOfflineVideoDowloaded;
    private static boolean isLiveStreaming;
    private final String FIREBASE_STREAM_START = "stream_start";
    private final String FIREBASE_STREAM_25 = "stream_25_pct";
    private final String FIREBASE_STREAM_50 = "stream_50_pct";
    private final String FIREBASE_STREAM_75 = "stream_75_pct";
    private final String FIREBASE_STREAM_95 = "stream_95_pct";
    private final String FIREBASE_STREAM_100 = "stream_100_pct";
    private final String FIREBASE_CLIP_START = "clip_start";
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
    private final int totalCountdownInMillis = 2000;
    private final int countDownIntervalInMillis = 20;
    Handler mProgressHandler;
    Runnable mProgressRunnable;
    long mTotalVideoDuration;
    Animation animSequential, animFadeIn, animFadeOut, animTranslate;
    boolean isStreamStart, isStream25, isStream50, isStream75, isStream95, isStream100;
    int maxPreviewSecs = 0;
    int playedVideoSecs = 0;

    @Inject
    AppPreference appPreference;
    @Inject
    AppCMSPresenter appCMSPresenter;
    @Inject
    LocalisedStrings localisedStrings;

    private String fontColor = "#ffffffff";
    private String title;
    private String hlsUrl;
    private String permaLink;
    private boolean isTrailer;
    private String filmId;
    private String segmentId = null;
    private String seriesId;
    private String primaryCategory;
    private String imageUrl;
    private String parentScreenName;
    private String adsUrl;
    private String parentalRating;
    private boolean freeContent;

    private class DRMObj {
        boolean isDrmVideoEnabled;
        String licenseUrl;
        String licenseToken;
        boolean isOffline;
    }

    DRMObj drmObj;

    @BindView(R.id.app_cms_video_player_info_container)
    LinearLayoutCompat videoPlayerInfoContainer;
    @BindView(R.id.app_cms_video_player_main_container)
    RelativeLayout videoPlayerMainContainer;
    @BindView(R.id.app_cms_content_rating_main_container)
    PercentRelativeLayout contentRatingMainContainer;
    @BindView(R.id.app_cms_content_rating_animation_container)
    PercentRelativeLayout contentRatingAnimationContainer;
    @BindView(R.id.app_cms_content_rating_info_container)
    LinearLayoutCompat contentRatingInfoContainer;
    @BindView(R.id.app_cms_video_player_done_button)
    AppCompatImageButton videoPlayerViewDoneButton;
    @BindView(R.id.app_cms_video_player_title_view)
    AppCompatTextView videoPlayerTitleView;
    @BindView(R.id.app_cms_content_rating_header_view)
    AppCompatTextView contentRatingHeaderView;
    @BindView(R.id.app_cms_content_rating_viewer_discretion)
    AppCompatTextView contentRatingDiscretionView;
    @BindView(R.id.app_cms_content_rating_title_header)
    AppCompatTextView contentRatingTitleHeader;
    @BindView(R.id.app_cms_content_rating_title)
    AppCompatTextView contentRatingTitleView;
    @BindView(R.id.app_cms_content_rating_back)
    AppCompatTextView contentRatingBack;
    @BindView(R.id.app_cms_content_rating_back_underline)
    View contentRatingBackUnderline;
    @BindView(R.id.app_cms_video_player_container)
    VideoPlayerView videoPlayerView;
    @BindView(R.id.app_cms_video_loading)
    LinearLayoutCompat videoLoadingProgress;
    private OnClosePlayerEvent onClosePlayerEvent;
    private OnUpdateContentDatumEvent onUpdateContentDatumEvent;
    private BeaconPing beaconPing;
    private BeaconHandler mBeaconHandler;
    private BeaconRunnable mBeaconRunnable;
    private long beaconMsgTimeoutMsec;
    private String policyCookie;
    private String signatureCookie;
    private String keyPairIdCookie;
    private boolean isVideoLoaded = false;
    private BeaconBuffer beaconBuffer;
    private long beaconBufferingTimeoutMsec;
    private boolean sentBeaconPlay;
    private boolean sentBeaconFirstFrame;

    private VideoPlayerView.StreamingQualitySelector streamingQualitySelector;
    private VideoPlayerView.SeasonEpisodeSelctionEvent seasonEpisodeSelctionEvent;
    private VideoPlayerView.ClosedCaptionSelector closedCaptionSelector;
    private VideoPlayerView.VideoPlayerSettingsEvent videoPlayerSettingsEvent;
    @BindView(R.id.playerSettingView)
    PlayerSettingsView playerSettingsView;
    @Nullable
    @BindView(R.id.settingsButton)
    AppCompatImageButton settingsButton;
    @Nullable
    @BindView(R.id.relatedVideoSection)
    ConstraintLayout relatedVideoSection;
    @Nullable
    @BindView(R.id.nextEpisodeContainer)
    ConstraintLayout nextEpisodeContainer;
    @Nullable
    @BindView(R.id.previousEpisodeContainer)
    ConstraintLayout previousEpisodeContainer;
    @Nullable
    @BindView(R.id.nextEpisode)
    AppCompatImageView nextEpisodeImg;
    @Nullable
    @BindView(R.id.previousEpisode)
    AppCompatImageView previousEpisodeImg;
    @Nullable
    @BindView(R.id.previous)
    AppCompatTextView previous;
    @Nullable
    @BindView(R.id.next)
    AppCompatTextView next;
    @Nullable
    @BindView(R.id.timerView)
    TimerViewFutureContent timerView;
    private boolean showEntitlementDialog = false;
    private String mStreamId;
    private long mStartBufferMilliSec = 0l;
    private long mStopBufferMilliSec;
    @BindView(R.id.app_cms_content_rating_progress_bar)
    ProgressBar progressBar;
    private Runnable seekListener;
    private int progressCount = 0;
    private Handler seekBarHandler;
    private boolean showCRWWarningMessage;


    private boolean isAdDisplayed, isADPlay;
    private int playIndex;
    private long watchedTime;
    private long runTime;
    private long videoPlayTime = 0;
    @BindView(R.id.media_route_button)
    AppCompatImageButton mMediaRouteButton;
    private CastServiceProvider castProvider;
    private CastSession mCastSession;
    private CastHelper mCastHelper;
    private String closedCaptionUrl;
    private boolean isCastConnected;
    int bufferCount;
    int bufferTime;
    private String lastPlayType = "";
    ContentTypeChecker contentTypeChecker;
    Unbinder unbinder;
    AppCompatTextView textViewVideoLoading;
    CastServiceProvider.ILaunchRemoteMedia callBackRemotePlayback = castingModeChromecast -> {
        if (onClosePlayerEvent != null) {
            pauseVideo();
            long castPlayPosition = watchedTime * SECS_TO_MSECS;
            if (!isCastConnected) {
                castPlayPosition = videoPlayerView.getCurrentPosition();
            }

            onClosePlayerEvent.onRemotePlayback(castPlayPosition,
                    castingModeChromecast,
                    sentBeaconPlay,
                    onApplicationEnded -> {
                        if (getActivity() != null) {
                            getActivity().finish();
                        }
                    });
        }
    };
    private boolean crwCreated;
    private boolean refreshToken;
    private Timer refreshTokenTimer;
    private TimerTask refreshTokenTimerTask;
    private Timer entitlementCheckTimer;
    private TimerTask entitlementCheckTimerTask;
    private boolean entitlementCheckCancelled = false;
    Module moduleApi;
    List<ContentDatum> allEpisodes = null;
    ContentDatum videoContentData;

    AudioManager audioManager;
    boolean tvodCastAllowed;
    boolean isTvodContent, isSvodContent;

    public static AppCMSPlayVideoFragment newInstance(Context context,
                                                      String primaryCategory,
                                                      String title,
                                                      String permaLink,
                                                      boolean isTrailer,
                                                      String hlsUrl,
                                                      String filmId,
                                                      String adsUrl,
                                                      boolean requestAds,
                                                      int playIndex,
                                                      long watchedTime,
                                                      String imageUrl,
                                                      String closedCaptionUrl,
                                                      String parentalRating,
                                                      long videoRunTime,
                                                      boolean freeContent,
                                                      AppCMSSignedURLResult appCMSSignedURLResult,
                                                      boolean isDrmVideoEnabled,
                                                      String licenseUrl,
                                                      String licenseToken,
                                                      ContentDatum videoContentData,
                                                      Boolean isOffline, boolean tvodCastAllowed, boolean isTvodContent, boolean isSvodContent) {
        AppCMSPlayVideoFragment appCMSPlayVideoFragment = new AppCMSPlayVideoFragment();
        Bundle args = new Bundle();
        args.putString(context.getString(R.string.video_primary_category_key), primaryCategory);
        args.putString(context.getString(R.string.video_player_title_key), title);
        args.putString(context.getString(R.string.video_player_permalink_key), permaLink);
        args.putString(context.getString(R.string.video_player_hls_url_key), hlsUrl);
        args.putString(context.getString(R.string.video_layer_film_id_key), filmId);
        args.putString(context.getString(R.string.video_player_ads_url_key), adsUrl);
        args.putBoolean(context.getString(R.string.video_player_request_ads_key), requestAds);
        args.putInt(context.getString(R.string.play_index_key), playIndex);
        args.putLong(context.getString(R.string.watched_time_key), watchedTime);
        args.putLong(context.getString(R.string.run_time_key), videoRunTime);
        args.putBoolean(context.getString(R.string.free_content_key), freeContent);

        args.putString(context.getString(R.string.played_movie_image_url), imageUrl);
        args.putString(context.getString(R.string.video_player_closed_caption_key), closedCaptionUrl);
        args.putBoolean(context.getString(R.string.video_player_is_trailer_key), isTrailer);
        args.putString(context.getString(R.string.video_player_content_rating_key), parentalRating);

        if (isDrmVideoEnabled) {
            args.putBoolean(context.getString(R.string.drm_enabled), isDrmVideoEnabled);
            args.putString(context.getString(R.string.license_url), licenseUrl);
            args.putString(context.getString(R.string.license_token), licenseToken);
        }

        if (appCMSSignedURLResult != null) {
            appCMSSignedURLResult.parseKeyValuePairs();
            args.putString(context.getString(R.string.signed_policy_key), appCMSSignedURLResult.getPolicy());
            args.putString(context.getString(R.string.signed_signature_key), appCMSSignedURLResult.getSignature());
            args.putString(context.getString(R.string.signed_keypairid_key), appCMSSignedURLResult.getKeyPairId());
        } else {
            args.putString(context.getString(R.string.signed_policy_key), "");
            args.putString(context.getString(R.string.signed_signature_key), "");
            args.putString(context.getString(R.string.signed_keypairid_key), "");
        }
        args.putSerializable(context.getString(R.string.video_content_data), videoContentData);
        args.putBoolean(context.getString(R.string.tvod_cast_allowed), tvodCastAllowed);
        args.putBoolean(context.getString(R.string.tvod_content), isTvodContent);
        args.putBoolean(context.getString(R.string.svod_content), isSvodContent);

        appCMSPlayVideoFragment.setArguments(args);

        return appCMSPlayVideoFragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnClosePlayerEvent) {
            onClosePlayerEvent = (OnClosePlayerEvent) context;
        }
        if (context instanceof OnUpdateContentDatumEvent) {
            onUpdateContentDatumEvent = (OnUpdateContentDatumEvent) context;
        }

        if (context instanceof VideoPlayerView.StreamingQualitySelector) {
            streamingQualitySelector = (VideoPlayerView.StreamingQualitySelector) context;
        }
        if (context instanceof VideoPlayerView.SeasonEpisodeSelctionEvent) {
            seasonEpisodeSelctionEvent = (VideoPlayerView.SeasonEpisodeSelctionEvent) context;
        }
        if (context instanceof VideoPlayerView.ClosedCaptionSelector) {
            closedCaptionSelector = (VideoPlayerView.ClosedCaptionSelector) context;
        }
        if (context instanceof VideoPlayerView.VideoPlayerSettingsEvent) {
            videoPlayerSettingsEvent = (VideoPlayerView.VideoPlayerSettingsEvent) context;
        }
        if (context instanceof RegisterOnResumeVideo) {
            ((RegisterOnResumeVideo) context).registerOnResumeVideo(this);
        }
        ((AppCMSApplication) context.getApplicationContext()).getAppCMSPresenterComponent().inject(this);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        if (args != null) {
            title = args.getString(getString(R.string.video_player_title_key));
            permaLink = args.getString(getString(R.string.video_player_permalink_key));
            isTrailer = args.getBoolean(getString(R.string.video_player_is_trailer_key));
            hlsUrl = args.getString(getContext().getString(R.string.video_player_hls_url_key));
            filmId = args.getString(getContext().getString(R.string.video_layer_film_id_key));
            adsUrl = args.getString(getContext().getString(R.string.video_player_ads_url_key));

            playIndex = args.getInt(getString(R.string.play_index_key));
            watchedTime = args.getLong(getContext().getString(R.string.watched_time_key));
            runTime = args.getLong(getContext().getString(R.string.run_time_key));

            imageUrl = args.getString(getContext().getString(R.string.played_movie_image_url));
            closedCaptionUrl = args.getString(getContext().getString(R.string.video_player_closed_caption_key));
            primaryCategory = args.getString(getString(R.string.video_primary_category_key));
            parentalRating = args.getString(getString(R.string.video_player_content_rating_key));

            freeContent = args.getBoolean(getString(R.string.free_content_key));
            tvodCastAllowed = args.getBoolean(getString(R.string.tvod_cast_allowed));
            isTvodContent = args.getBoolean(getString(R.string.tvod_content));
            isSvodContent = args.getBoolean(getString(R.string.svod_content));

            policyCookie = args.getString(getString(R.string.signed_policy_key));
            signatureCookie = args.getString(getString(R.string.signed_signature_key));
            keyPairIdCookie = args.getString(getString(R.string.signed_keypairid_key));
            videoContentData = (ContentDatum) args.getSerializable(getString(R.string.video_content_data));
            refreshToken = !(TextUtils.isEmpty(policyCookie) ||
                    TextUtils.isEmpty(signatureCookie) ||
                    TextUtils.isEmpty(keyPairIdCookie));

            drmObj = new DRMObj();
            drmObj.isDrmVideoEnabled = args.getBoolean(getContext().getString(R.string.drm_enabled));
            if (drmObj.isDrmVideoEnabled) {
                drmObj.licenseToken = args.getString(getContext().getString(R.string.license_token));
                drmObj.licenseUrl = args.getString(getContext().getString(R.string.license_url));
            }
        }

        hlsUrl = hlsUrl.replaceAll(" ", "+");

        sentBeaconPlay = (0 < playIndex && watchedTime != 0);

        beaconMsgTimeoutMsec = getActivity().getResources().getInteger(R.integer.app_cms_beacon_timeout_msec);
        beaconBufferingTimeoutMsec = getActivity().getResources().getInteger(R.integer.app_cms_beacon_buffering_timeout_msec);

        // It Handles the player stream Firebase events.
        setFirebaseProgressHandling();

        parentScreenName = getContext().getString(R.string.app_cms_beacon_video_player_parent_screen_name);
        setRetainInstance(true);
        //VideoPlayerView.isBitRateUpdatedCT=false;

        audioManager = (AudioManager) getActivity().getSystemService(Context.AUDIO_SERVICE);

        if (contentTypeChecker == null)
            contentTypeChecker = new ContentTypeChecker(getActivity());
        AppsFlyerUtils.filmViewingEvent(getContext(), primaryCategory, filmId, appCMSPresenter);
    }

    private void createAllEpisodeList() {
        boolean segment = false;
        int currentSeason = 0;
        int currentEpisode = 0;
        if (moduleApi != null && moduleApi.getContentData() != null
                && moduleApi.getContentData().size() >= 1
                && moduleApi.getContentData().get(0).getSeason() != null) {
            allEpisodes = new ArrayList<>();
            for (int i = 0; i < moduleApi.getContentData().get(0).getSeason().size(); i++) {
                if (moduleApi.getContentData().get(0).getSeason().get(i).getEpisodes() != null) {
                    for (int j = 0; j < moduleApi.getContentData().get(0).getSeason().get(i).getEpisodes().size(); j++) {
                        ContentDatum episode = moduleApi.getContentData().get(0).getSeason().get(i).getEpisodes().get(j);
                        if (episode.getRelatedVideos() != null && episode.getRelatedVideos().size() != 0) {
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
                segmentId = moduleApi.getContentData().get(0).getSeason().get(currentSeason).getEpisodes().get(currentEpisode).getGist().getId();
            }

            for (int i = 0; i < moduleApi.getContentData().get(0).getSeason().size(); i++) {
                if (moduleApi.getContentData().get(0).getSeason().get(i).getEpisodes() != null) {
                    allEpisodes.addAll(moduleApi.getContentData().get(0).getSeason().get(i).getEpisodes());
                }
            }
        }

    }

    private int findCurrentPlayingPositionOfEpisode() {
        if (segmentId == null)
            segmentId = filmId;
        if (allEpisodes != null) {
            for (int i = 0; i < allEpisodes.size(); i++) {
                if (allEpisodes.get(i).getGist() != null && segmentId.equalsIgnoreCase(allEpisodes.get(i).getGist().getId()))
                    return i;
            }
        }
        return -1;
    }

    private boolean isFromError = false;

    @Override
    public void playerError(ExoPlaybackException ex) {
        appCMSPresenter.sendEventMediaError(onUpdateContentDatumEvent.getCurrentContentDatum(), ex.getMessage(), videoPlayerView.getCurrentPosition() / 1000);
        final Handler playerHandler = new Handler();
        playerHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (drmObj != null && drmObj.isDrmVideoEnabled && ex instanceof ExoPlaybackException && ex.getCause() != null && ((ex.getCause() instanceof DrmSession.DrmSessionException))) {
                    if (ex.getCause().getCause() != null && ex.getCause().getCause() instanceof KeysExpiredException) {
                        if (filmId != null) {
                            if (appCMSPresenter.isUserLoggedIn()) {
                                appCMSPresenter.refreshIdentity(appPreference.getRefreshToken(), new Action0() {
                                    @Override
                                    public void call() {
                                        appCMSPresenter.refreshVideoData(filmId,
                                                updatedContentDatum -> {
                                                    drmObj.licenseToken = updatedContentDatum.getStreamingInfo().getVideoAssets().getWideVine().getLicenseToken();
                                                    drmObj.licenseUrl = updatedContentDatum.getStreamingInfo().getVideoAssets().getWideVine().getLicenseUrl();
                                                    if (updatedContentDatum.getStreamingInfo().getVideoAssets().getWideVine() != null && updatedContentDatum.getStreamingInfo().getVideoAssets().getWideVine().getUrl() != null) {
                                                        streamingQualitySelector.setVideoUrl(updatedContentDatum.getStreamingInfo().getVideoAssets().getWideVine().getUrl());
                                                    }

                                                    pauseVideo();
                                                    videoPlayTime = videoPlayerView.getCurrentPosition() / SECS_TO_MSECS;
                                                    videoPlayerView.releasePlayer();
                                                    final Handler handler = new Handler();
                                                    handler.postDelayed(new Runnable() {
                                                        @Override
                                                        public void run() {
                                                            //Do something after 100ms
                                                            isFromError = true;
                                                            onResume();
                                                        }
                                                    }, 1000);
                                                }, null, false, false, videoContentData);
                                    }
                                });
                            } else {
                                appCMSPresenter.refreshVideoData(filmId,
                                        updatedContentDatum -> {
                                            onUpdateContentDatumEvent.updateContentDatum(updatedContentDatum);
                                            drmObj.licenseToken = updatedContentDatum.getStreamingInfo().getVideoAssets().getWideVine().getLicenseToken();
                                            drmObj.licenseUrl = updatedContentDatum.getStreamingInfo().getVideoAssets().getWideVine().getLicenseUrl();
                                            if (updatedContentDatum.getStreamingInfo().getVideoAssets().getWideVine() != null && updatedContentDatum.getStreamingInfo().getVideoAssets().getWideVine().getUrl() != null) {
                                                streamingQualitySelector.setVideoUrl(updatedContentDatum.getStreamingInfo().getVideoAssets().getWideVine().getUrl());
                                            }

                                            final Handler handler = new Handler();
                                            handler.postDelayed(new Runnable() {
                                                @Override
                                                public void run() {
                                                    //Do something after 100ms
                                                    isFromError = true;
                                                    onResume();
                                                }
                                            }, 1000);
                                        }, null, false, false, videoContentData);
                            }
                        }
                    } else if (ex.getCause().getCause() != null && ((ex.getCause().getCause()) instanceof MediaDrm.MediaDrmStateException)) {
                        appCMSPresenter.showDialog(AppCMSPresenter.DialogType.DOWNLOAD_NOT_AVAILABLE,
                                localisedStrings.getUserOnlineTimeAlertText(),
                                false,
                                () -> onClosePlayerEvent.closePlayer(),
                                null, localisedStrings.getPremiumContentText());
                    } else {
                        appCMSPresenter.showDialog(AppCMSPresenter.DialogType.DRM_PLAY_ERROR,
                                localisedStrings.getPlaybackErrorText(),
                                false,
                                () -> onClosePlayerEvent.closePlayer(),
                                null, null);
                    }
                }
            }
        }, 1000);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        int layout = R.layout.fragment_video_player;
        if (appCMSPresenter.getTemplateType() == AppCMSPresenter.TemplateType.NEWS)
            layout = R.layout.fragment_video_player_news;
        View rootView = inflater.inflate(layout, container, false);
        unbinder = ButterKnife.bind(this, rootView);
        moduleApi = videoContentData.getModuleApi();
        castProvider = CastServiceProvider.getInstance(getActivity());
        createAllEpisodeList();
        int currentPlayIndex = findCurrentPlayingPositionOfEpisode();
        if (currentPlayIndex != -1)
            videoContentData = allEpisodes.get(currentPlayIndex);
        startEntitlementCheckTimer();
        videoPlayerView.setmSettingButton(settingsButton);

        videoPlayerView.getPlayerView().getController().setPlayerPlayPauseState(this);
        if (!TextUtils.isEmpty(title)) {
            videoPlayerTitleView.setText(title);
        }
        videoPlayerTitleView.setTextColor(Color.parseColor(ViewCreator.getColorWithOpacity(getContext(),
                fontColor,
                0xff)));
        if (previous != null)
            previous.setTextColor(appCMSPresenter.getBlockTitleColor());
        if (next != null)
            next.setTextColor(appCMSPresenter.getBlockTitleColor());

        sendFirebaseAnalyticsEvents(title);

        videoPlayerViewDoneButton.setOnClickListener(v -> {
            if (onClosePlayerEvent != null) {
                updateWatchedHistory();
                onClosePlayerEvent.closePlayer();
            }
        });

        videoPlayerViewDoneButton.setColorFilter(Color.parseColor(fontColor));
        videoPlayerInfoContainer.bringToFront();

        videoPlayerView.applyTimeBarColor(Color.parseColor(CommonUtils.getColor(getContext(),
                appCMSPresenter.getAppCtaBackgroundColor())));
        videoPlayerView.setOnBeaconAdsEvent(this);

        videoPlayerView.setVideoPlayerSettingsEvent(this);

        if (streamingQualitySelector != null) {
            videoPlayerView.setStreamingQualitySelector(streamingQualitySelector);
        }
        videoPlayerView.setSeasonEpisodeSelctionEvent(seasonEpisodeSelctionEvent);

        videoPlayerView.setFilmId(filmId);
        if (closedCaptionSelector != null) {
            videoPlayerView.setClosedCaptionsSelector(closedCaptionSelector);
        }

        CookieManager cookieManager = new CookieManager();
        CookieHandler.setDefault(cookieManager);
        if (!TextUtils.isEmpty(policyCookie) &&
                !TextUtils.isEmpty(signatureCookie) &&
                !TextUtils.isEmpty(keyPairIdCookie)) {
            videoPlayerView.setPolicyCookie(policyCookie);
            videoPlayerView.setSignatureCookie(signatureCookie);
            videoPlayerView.setKeyPairIdCookie(keyPairIdCookie);
        }

        videoPlayerView.setListener(this);
        videoPlayerView.setVideoContentData(videoContentData);
        showTimer(videoContentData);

        ProgressBar videoLoadingProgressBar = rootView.findViewById(R.id.video_loading_progress_indicator);

        textViewVideoLoading = rootView.findViewById(R.id.video_loading_text);

        textViewVideoLoading.setText(localisedStrings.getLoadingVideoText());
        try {
            videoLoadingProgressBar.getIndeterminateDrawable().setTint(appCMSPresenter.getBrandPrimaryCtaColor());
        } catch (Exception e) {
            e.printStackTrace();
//                //Log.w(TAG, "Failed to set color for loader: " + e.getMessage());
            videoLoadingProgressBar.getIndeterminateDrawable().setTint(ContextCompat.getColor(getContext(), R.color.colorAccent));
        }

        boolean allowFreePlay = !appCMSPresenter.isAppSVOD() || isTrailer || freeContent;
        setCasting(allowFreePlay);

        try {
            mStreamId = appCMSPresenter.getStreamingId(title);
        } catch (Exception e) {
            //Log.e(TAG, e.getMessage());
            mStreamId = filmId + appCMSPresenter.getCurrentTimeStamp();
        }

        isVideoDownloaded = appCMSPresenter.isVideoDownloaded(filmId);
        isOfflineVideoDowloaded = appCMSPresenter.isOfflineVideoDownloaded(filmId);

        setCurrentWatchProgress(runTime, watchedTime);

        videoPlayerView.setOnPlayerStateChanged(new Action1<VideoPlayerView.PlayerState>() {
            @Override
            public void call(VideoPlayerView.PlayerState playerState) {

                if (beaconPing != null) {
                    beaconPing.playbackState = playerState.getPlaybackState();
                }

                if (playerState.getPlaybackState() == ExoPlayer.STATE_READY
                        && !isCastConnected) {

                    long updatedRunTime = 0;
                    try {
                        updatedRunTime = videoPlayerView.getDuration() / 1000;

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    setCurrentWatchProgress(updatedRunTime, watchedTime);


                    if (!isVideoLoaded) {
                        if (!isTrailer && !isLiveStreaming) {
                            videoPlayerView.setCurrentPosition(videoPlayTime * SECS_TO_MSECS);
                            boolean isWatchHistoryUpdateEnabled = appCMSPresenter.getAppCMSMain() != null
                                    && appCMSPresenter.getAppCMSMain().getFeatures() != null
                                    && appCMSPresenter.getAppCMSMain().getFeatures().getWatchedHistory() != null
                                    && appCMSPresenter.getAppCMSMain().getFeatures().getWatchedHistory().isEnabled();
                            if (isWatchHistoryUpdateEnabled) {
                                appCMSPresenter.updateWatchedTime(
                                        filmId,
                                        seriesId,
                                        videoPlayerView.getCurrentPosition() / 1000, updateHistoryResponse -> {
                                            if (updateHistoryResponse.getResponseCode() == 401) {
                                                pauseVideo();
                                                appCMSPresenter.showDialog(AppCMSPresenter.DialogType.MAX_STREAMS_ERROR, updateHistoryResponse.getErrorMessage(), true, null, null, null);
                                            }
                                        });
                            }
                        }
                        isVideoLoaded = true;
                    }

                    if (beaconBuffer != null) {
                        beaconBuffer.sendBeaconBuffering = false;
                    }

                    if (beaconPing != null) {
                        beaconPing.sendBeaconPing = true;

                        if (!beaconPing.isAlive()) {
                            try {
                                if (lastPlayType.contains("BUFFERING")) {
                                    bufferCount++;
                                    lastPlayType = "PLAYING";
                                }
                                beaconPing.setBufferTime(bufferTime);
                                beaconPing.setBufferCount(bufferCount);
                                beaconPing.start();
                                mTotalVideoDuration = videoPlayerView.getDuration() / 1000;
                                mTotalVideoDuration -= mTotalVideoDuration % 4;
                                mProgressHandler.post(mProgressRunnable);
                            } catch (Exception e) {

                            }
                        }
                    }

                    if (!sentBeaconFirstFrame && appCMSPresenter.getAppCMSMain() != null && appCMSPresenter.getAppCMSMain().getFeatures() != null
                            && appCMSPresenter.getAppCMSMain().getFeatures().isEnableQOS()) {
                        mStopBufferMilliSec = new Date().getTime();
                        ttfirstframe = mStartBufferMilliSec == 0l ? 0d : ((mStopBufferMilliSec - mStartBufferMilliSec) / 1000d);
                        appCMSPresenter.sendBeaconMessage(filmId,
                                permaLink,
                                parentScreenName,
                                videoPlayerView.getCurrentPosition(),
                                false,
                                AppCMSPresenter.BeaconEvent.FIRST_FRAME,
                                "Video",
                                videoPlayerView.getBitrate() != 0 ? String.valueOf(videoPlayerView.getBitrate()) : null,
                                String.valueOf(videoPlayerView.getVideoHeight()),
                                String.valueOf(videoPlayerView.getVideoWidth()),
                                mStreamId,
                                ttfirstframe,
                                0,
                                isVideoDownloaded);
                        sentBeaconFirstFrame = true;
                        appCMSPresenter.sendGaEvent(getContext().getResources().getString(R.string.play_video_action),
                                getContext().getResources().getString(R.string.play_video_category),
                                (title != null && !TextUtils.isEmpty(title)) ? title : filmId);
                    }


                    videoLoadingProgress.setVisibility(GONE);
                } else if (playerState.getPlaybackState() == ExoPlayer.STATE_ENDED) {
                    //Log.d(TAG, "Video ended");

                    // close the player if current video is a trailer. We don't want to auto-play it
                    if (onClosePlayerEvent != null &&
                            permaLink.contains(
                                    getString(R.string.app_cms_action_qualifier_watchvideo_key))) {
                        onClosePlayerEvent.closePlayer();
                        return;
                    }

                    //if user is not subscribe or ot login than on seek to end dont open autoplay screen# fix for SVFA-2403
                    if (appCMSPresenter.isAppSVOD() &&
                            !isTrailer &&
                            !freeContent &&
                            !appCMSPresenter.isUserSubscribed() && !entitlementCheckCancelled) {
                        showEntitlementDialog = true;
                    }

                    if (onClosePlayerEvent != null && playerState.isPlayWhenReady() && appCMSPresenter.getRestWorkoutDialog()) {
                        try {
                            RestWorkoutDialog restWorkoutDialog = new RestWorkoutDialog(getActivity(), appCMSPresenter, moduleApi, videoPlayerView, onClosePlayerEvent);
                            restWorkoutDialog.open();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    } else if (onClosePlayerEvent != null && playerState.isPlayWhenReady() && !showEntitlementDialog) {
                        //entitlementCheckTimerTask.cancel();
                        // tell the activity that the movie is finished
                        onClosePlayerEvent.onMovieFinished();
                    } else if (appCMSPresenter.isAppSVOD() &&
                            !isTrailer &&
                            !freeContent &&
                            !appCMSPresenter.isUserSubscribed() && !entitlementCheckCancelled) {
                        //entitlementCheckTimerTask.cancel();
                        // tell the activity that the movie is finished
                        onClosePlayerEvent.onMovieFinished();
                    }
                    boolean isWatchHistoryUpdateEnabled = appCMSPresenter.getAppCMSMain() != null
                            && appCMSPresenter.getAppCMSMain().getFeatures() != null
                            && appCMSPresenter.getAppCMSMain().getFeatures().getWatchedHistory() != null
                            && appCMSPresenter.getAppCMSMain().getFeatures().getWatchedHistory().isEnabled();
                    if (isWatchHistoryUpdateEnabled) {
                        // int interval = appCMSPresenter.getAppCMSMain().getFeatures().getWatchedHistory().getIntervalInt();
                        if (!isTrailer /*&& interval <= (videoPlayerView.getCurrentPosition() / 1000)*/) {
                            appCMSPresenter.updateWatchedTime(filmId, seriesId,
                                    videoPlayerView.getCurrentPosition() / 1000, s -> {
                                        UpdateHistoryResponse response = (UpdateHistoryResponse) s;
                                        if (((UpdateHistoryResponse) s).getResponseCode() == 401) {
                                            pauseVideo();
                                            appCMSPresenter.showDialog(AppCMSPresenter.DialogType.MAX_STREAMS_ERROR, response.getErrorMessage(), true, null, null, null);
                                        }
                                    });
                        }
                    }
                } else if (playerState.getPlaybackState() == ExoPlayer.STATE_BUFFERING ||
                        playerState.getPlaybackState() == ExoPlayer.STATE_IDLE) {
                    videoLoadingProgress.setVisibility(VISIBLE);
                    lastPlayType = "BUFFERING";
                    bufferTime++;
                    if ((int) (videoPlayerView.getCurrentPosition() / 1000) == (int) ((videoPlayerView.getDuration() / 1000) * 0.25) ||
                            (int) (videoPlayerView.getCurrentPosition() / 1000) == (int) ((videoPlayerView.getDuration() / 1000) * 0.5) ||
                            (int) (videoPlayerView.getCurrentPosition() / 1000) == (int) ((videoPlayerView.getDuration() / 1000) * 0.75)) {
                        bufferCount = 0;
                        bufferTime = 0;
                    }
                    if (beaconPing != null) {
                        beaconPing.sendBeaconPing = false;
                    }

                    if (beaconBuffer != null && appCMSPresenter.getAppCMSMain() != null && appCMSPresenter.getAppCMSMain().getFeatures() != null && appCMSPresenter.getAppCMSMain().getFeatures().isEnableQOS()) {
                        beaconBuffer.sendBeaconBuffering = true;
                        if (!beaconBuffer.isAlive()) {
                            beaconBuffer.start();
                        }
                    }

                }

                if (!sentBeaconPlay) {
                    sentBeaconPlay = true;
                    appCMSPresenter.sendBeaconMessage(filmId,
                            permaLink,
                            parentScreenName,
                            videoPlayerView.getCurrentPosition(),
                            false,
                            AppCMSPresenter.BeaconEvent.PLAY,
                            "Video",
                            videoPlayerView.getBitrate() != 0 ? String.valueOf(videoPlayerView.getBitrate()) : null,
                            String.valueOf(videoPlayerView.getVideoHeight()),
                            String.valueOf(videoPlayerView.getVideoWidth()),
                            mStreamId,
                            0d,
                            0,
                            isVideoDownloaded);

                    mStartBufferMilliSec = new Date().getTime();

                    appCMSPresenter.sendGaEvent(getContext().getResources().getString(R.string.play_video_action),
                            getContext().getResources().getString(R.string.play_video_category),
                            (title != null && !TextUtils.isEmpty(title)) ? title : filmId);
                    appCMSPresenter.sendPlayStartedEvent(onUpdateContentDatumEvent.getCurrentContentDatum());

                }

            }
        });

        videoPlayerView.setOnPlayerControlsStateChanged(visibility -> {
            if (visibility == GONE) {
                if (videoPlayerInfoContainer != null)
                    videoPlayerInfoContainer.setVisibility(GONE);
                if (appCMSPresenter.getPlatformType() == AppCMSPresenter.PlatformType.TV) {
                    if (((AppCMSPlayVideoActivity) (getActivity())).getSeasonEpisodePopUpWindow() != null && ((AppCMSPlayVideoActivity) (getActivity())).getSeasonEpisodePopUpWindow().isShowing())
                        ((AppCMSPlayVideoActivity) (getActivity())).getSeasonEpisodePopUpWindow().dismiss();
                } else {
                    if (relatedVideoSection != null && videoPlayerView.getRelatedVideoButton() != null && videoPlayerView.getRelatedVideoButton().isSelected()) {
                        videoPlayerView.getRelatedVideoButton().setColorFilter(appCMSPresenter.getBrandSecondaryCtaTextColor(), android.graphics.PorterDuff.Mode.SRC_IN);
                        relatedVideoSection.setVisibility(View.GONE);
                        videoPlayerView.getRelatedVideoButton().setSelected(false);
                    }
                }
                /*if (((AppCMSPlayVideoActivity) (getActivity())).getSeasonEpisodePopUpWindow() != null &&
                        ((AppCMSPlayVideoActivity) (getActivity())).getSeasonEpisodePopUpWindow().isShowing()) {
                    videoPlayerView.getPlayerView().maybeShowController(true);
//                    ((AppCMSPlayVideoActivity) (getActivity())).getSeasonEpisodePopUpWindow().dismiss();
                }*/

            } else if (visibility == VISIBLE) {
//                videoPlayerView.getPlayerView().maybeShowController(false);
                if (videoPlayerInfoContainer != null)
                    videoPlayerInfoContainer.setVisibility(VISIBLE);
                if (appCMSPresenter.getPlatformType() != AppCMSPresenter.PlatformType.TV && videoPaused) {
                    if (relatedVideoSection != null && relatedVideoSection.getVisibility() == GONE) {
                        loadPrevNextImage();
                    }
                }
            }
        });

        videoPlayerView.setOnClosedCaptionButtonClicked(isChecked -> {
            if (videoPlayerView.getPlayerView() != null && videoPlayerView.getPlayerView().getSubtitleView() != null)
                videoPlayerView.getPlayerView().getSubtitleView().setVisibility(isChecked ? VISIBLE : GONE);
            appPreference.setClosedCaptionPreference(isChecked);
        });
        if (onUpdateContentDatumEvent != null && onUpdateContentDatumEvent.getCurrentContentDatum() != null
                && onUpdateContentDatumEvent.getCurrentContentDatum().getStreamingInfo() != null) {
            isLiveStreaming = onUpdateContentDatumEvent.getCurrentContentDatum().getStreamingInfo().isLiveStream();
        } else {
            isLiveStreaming = false;
        }
        if (onUpdateContentDatumEvent != null && onUpdateContentDatumEvent.getCurrentContentDatum() != null
                && onUpdateContentDatumEvent.getCurrentContentDatum().getGist() != null
                && onUpdateContentDatumEvent.getCurrentContentDatum().getGist().getSeriesId() != null
                && !TextUtils.isEmpty(onUpdateContentDatumEvent.getCurrentContentDatum().getGist().getSeriesId())) {
            seriesId = onUpdateContentDatumEvent.getCurrentContentDatum().getGist().getSeriesId();
        }
        if (videoPlayerView.getPlayerView() != null && videoPlayerView.getPlayerView().getController() != null) {
            videoPlayerView.getPlayerView().getController().setPlayingLive(isLiveStreaming);
        }

        playerSettingsView.initializeView();


        initViewForCRW();

        try {
            createContentRatingView();
        } catch (Exception e) {
            //Log.e(TAG, "Error ContentRatingView: " + e.getMessage());
        }


        beaconPing = new BeaconPing(beaconMsgTimeoutMsec,
                appCMSPresenter,
                filmId,
                permaLink,
                isTrailer,
                parentScreenName,
                videoPlayerView,
                mStreamId, onUpdateContentDatumEvent.getCurrentContentDatum());

        beaconBuffer = new BeaconBuffer(beaconBufferingTimeoutMsec,
                appCMSPresenter,
                filmId,
                permaLink,
                parentScreenName,
                videoPlayerView,
                mStreamId, onUpdateContentDatumEvent.getCurrentContentDatum());


        mBeaconHandler = new BeaconHandler(videoPlayerView.getPlayer().getApplicationLooper());
        mBeaconRunnable = new BeaconRunnable(beaconPing,
                beaconBuffer,
                mBeaconHandler,
                videoPlayerView);
        mBeaconHandler.handle(mBeaconRunnable);


        videoLoadingProgress.bringToFront();
        videoLoadingProgress.setVisibility(VISIBLE);

        showCRWWarningMessage = true;

        /*if (isVideoDownloaded) {
            videoPlayerView.startPlayer();
        }*/
        setFont();
        return rootView;

    }

    @Optional
    @OnClick(R.id.previousEpisodeContainer)
    void previousEpisodeClick() {
        if (allEpisodes != null && allEpisodes.size() > 0)
            videoSelected.selectedVideoListener(allEpisodes.get(findCurrentPlayingPositionOfEpisode() - 1), findCurrentPlayingPositionOfEpisode());
    }

    @Optional
    @OnClick(R.id.nextEpisodeContainer)
    void nextEpisodeClick() {
        if (allEpisodes != null && allEpisodes.size() > 0)
            videoSelected.selectedVideoListener(allEpisodes.get(findCurrentPlayingPositionOfEpisode() + 1), findCurrentPlayingPositionOfEpisode());
    }

    private void startEntitlementCheckTimer() {
        if (appCMSPresenter.isAppSVOD() &&
                !isTrailer &&
                !freeContent &&
                !appCMSPresenter.isUserSubscribed()) {
            startPreviewTimer();
        } else if (onUpdateContentDatumEvent == null || onUpdateContentDatumEvent.getCurrentContentDatum() == null
                || onUpdateContentDatumEvent.getCurrentContentDatum().getPricing() == null
                || onUpdateContentDatumEvent.getCurrentContentDatum().getPricing().getType() == null) {
            return;
        } else if (appCMSPresenter.getAppCMSMain() != null /*&& appCMSPresenter.getAppCMSMain().isForceLogin()*/ && !appCMSPresenter.isUserLoggedIn()
                && appCMSPresenter.getAppCMSMain().getFeatures() != null && appCMSPresenter.getAppCMSMain().getFeatures().getFreePreview() != null
                && appCMSPresenter.getAppCMSMain().getFeatures().getFreePreview().isFreePreview()
                && CommonUtils.isFreeContent(appCMSPresenter.getCurrentContext(), onUpdateContentDatumEvent.getCurrentContentDatum())) {
            startPreviewTimer();
        } else if ((onUpdateContentDatumEvent.getCurrentContentDatum().getPricing().getType().equalsIgnoreCase(getContext().getString(R.string.PURCHASE_TYPE_TVOD))
                || onUpdateContentDatumEvent.getCurrentContentDatum().getPricing().getType().equalsIgnoreCase(getContext().getString(R.string.PURCHASE_TYPE_PPV))) &&
                !appCMSPresenter.isUserSubscribed()) {

            appCMSPresenter.getTransactionData(onUpdateContentDatumEvent.getCurrentContentDatum().getGist().getId(), updatedContentDatum -> {
                appCMSPresenter.stopLoader();
                boolean isPlayable = true;
                boolean isPurchased = false;
                AppCMSTransactionDataValue objTransactionData = null;

                if (updatedContentDatum != null &&
                        updatedContentDatum.size() > 0) {
                    if (updatedContentDatum.get(0).size() == 0) {
                        isPlayable = false;
                    } else {
                        isPurchased = true;

                    }
                } else {
                    isPlayable = false;
                }

                if (!isPurchased) {
                    startPreviewTimer();
                }
            }, "Video");
        }
    }

    private void startPreviewTimer() {

        double entitlementCheckMultiplier = 1;
        entitlementCheckCancelled = false;

        AppCMSMain appCMSMain = appCMSPresenter.getAppCMSMain();
        if (appCMSMain != null &&
                appCMSMain.getFeatures() != null &&
                appCMSMain.getFeatures().getFreePreview() != null &&
                appCMSMain.getFeatures().getFreePreview().isFreePreview() &&
                appCMSMain.getFeatures().getFreePreview().getLength() != null &&
                appCMSMain.getFeatures().getFreePreview().getLength().getUnit().equalsIgnoreCase("Minutes")) {
            try {
                entitlementCheckMultiplier = Double.parseDouble(appCMSMain.getFeatures().getFreePreview().getLength().getMultiplier());
            } catch (Exception e) {
                //Log.e(TAG, "Error parsing free preview multiplier value: " + e.getMessage());
            }
        }
        maxPreviewSecs = (int) (entitlementCheckMultiplier * 60);


        boolean isPricingInfo = false;
        if ((onUpdateContentDatumEvent.getCurrentContentDatum().getPricing() != null &&
                onUpdateContentDatumEvent.getCurrentContentDatum().getPricing().getType() != null &&
                (onUpdateContentDatumEvent.getCurrentContentDatum().getPricing().getType().equalsIgnoreCase(getContext().getString(R.string.PURCHASE_TYPE_TVOD)) ||
//                        onUpdateContentDatumEvent.getCurrentContentDatum().getPricing().getType().equalsIgnoreCase(getContext().getString(R.string.PURCHASE_TYPE_SVOD_TVOD)) ||
//                        onUpdateContentDatumEvent.getCurrentContentDatum().getPricing().getType().equalsIgnoreCase(getContext().getString(R.string.PURCHASE_TYPE_SVOD_PPV)) ||
                        onUpdateContentDatumEvent.getCurrentContentDatum().getPricing().getType().equalsIgnoreCase(getContext().getString(R.string.PURCHASE_TYPE_PPV)))) ||
                contentTypeChecker.isContentPurchased(appPreference.getUserPurchases(), onUpdateContentDatumEvent.getCurrentContentDatum().getGist().getId())) {
            isPricingInfo = true;
        }

        if (videoContentData.getSubscriptionPlans() != null && (contentTypeChecker.isContentSVOD_TVE(videoContentData.getSubscriptionPlans(), null)
                || contentTypeChecker.isContentSVOD(videoContentData.getSubscriptionPlans()) || (appPreference.getTVEUserId() != null && contentTypeChecker.isContentTVE(videoContentData.getSubscriptionPlans()))
                || (contentTypeChecker.isContentTVOD(videoContentData.getSubscriptionPlans()) &&
                !(contentTypeChecker.isContentPurchased(appPreference.getUserPurchases(), onUpdateContentDatumEvent.getCurrentContentDatum().getGist().getId())
                        || (onUpdateContentDatumEvent.getCurrentContentDatum().getSeasonId() != null && contentTypeChecker.isContentPurchased(appPreference.getUserPurchases(), onUpdateContentDatumEvent.getCurrentContentDatum().getSeasonId()))
                        || (onUpdateContentDatumEvent.getCurrentContentDatum().getSeriesId() != null && contentTypeChecker.isContentPurchased(appPreference.getUserPurchases(), onUpdateContentDatumEvent.getCurrentContentDatum().getSeriesId())))
        )))
            isPricingInfo = false;
/*
        else if (appPreference.getTVEUserId() != null && checkContentType.isContentTVE(videoContentData))
            isPricingInfo = true;
*/

        if (!isPricingInfo) {
            entitlementCheckTimerTask = new TimerTask() {
                @Override
                public void run() {
                    if (isAdded() && !appPreference.getIsUserSubscribed()) {
                        checkEntitlement(this, appCMSMain);
                    }
                }
            };

            entitlementCheckTimer = new Timer();
            entitlementCheckTimer.schedule(entitlementCheckTimerTask, 1000, 1000);
        } else {

            /**
             * THis is need to handle the case of continue watching flow for TVOD
             */
            String contentType = onUpdateContentDatumEvent.getCurrentContentDatum().getGist().getContentType();
            if (!CommonUtils.getUserPurchasedBundle().contains(onUpdateContentDatumEvent.getCurrentContentDatum().getGist().getId())) {
                if (contentType.equalsIgnoreCase("BUNDLE")) {
                    appCMSPresenter.getTransactionDataResponse(onUpdateContentDatumEvent.getCurrentContentDatum().getGist().getId(), appCMSTransactionDataResponse -> {
                        if (appCMSTransactionDataResponse != null
                                && appCMSTransactionDataResponse.getRecords() != null
                                && appCMSTransactionDataResponse.getRecords().size() > 0) {

                            Map<String, AppCMSTransactionDataValue> map = new HashMap<String, AppCMSTransactionDataValue>();
                            for (AppCMSTransactionDataValue item : appCMSTransactionDataResponse.getRecords()) {
                                map.put(item.getGist().getId(), item);
                                CommonUtils.setUserPurchasedBundle(item.getGist().getId());
                            }
                            if (!CommonUtils.getUserPurchasedBundle().contains(onUpdateContentDatumEvent.getCurrentContentDatum().getGist().getId())) {
                                showTvodDialogforClosePlayer();
                            }
                        } else {
                            showTvodDialogforClosePlayer();
                        }
                    }, onUpdateContentDatumEvent.getCurrentContentDatum().getPricing().getType());
                } else if (contentType.equalsIgnoreCase("VIDEO") && !appCMSPresenter.getAppCMSMain().isMonetizationModelEnabled()) {
                    appCMSPresenter.getTransactionData(onUpdateContentDatumEvent.getCurrentContentDatum().getGist().getId(), updatedContentDatum -> {

                        if (updatedContentDatum != null
                                && updatedContentDatum.size() > 0) {
                            for (Map<String, AppCMSTransactionDataValue> map : updatedContentDatum) {
                                for (Map.Entry<String, AppCMSTransactionDataValue> entry : map.entrySet()) {
                                    CommonUtils.setUserPurchasedBundle(entry.getKey());
                                }
                            }
                            if (!CommonUtils.getUserPurchasedBundle().contains(onUpdateContentDatumEvent.getCurrentContentDatum().getGist().getId())) {
                                showTvodDialogforClosePlayer();
                            }
                        } else {
                            showTvodDialogforClosePlayer();
                        }
                    }, contentType);
                }
            }

        }


    }

    private void showTvodDialogforClosePlayer() {
        appCMSPresenter.showDialog(AppCMSPresenter.DialogType.TVOD_CONTENT_ERROR,
                localisedStrings.getTVODContentError(appCMSPresenter.getAppCMSMain().getDomainName()),
                false,
                () -> onClosePlayerEvent.closePlayer(),
                null,
                localisedStrings.getPremiumContentText());
    }

    @UiThread
    private void checkEntitlement(TimerTask timerTask, AppCMSMain appCMSMain) {
        if (!entitlementCheckCancelled) {
            int secsViewed = 0;
            if (appCMSMain != null && appCMSMain.getFeatures() != null && appCMSMain.getFeatures().getFreePreview() != null
                    && appCMSMain.getFeatures().getFreePreview().isPerVideo()) {
                try {
                    //if(videoPlayerView.)
                    secsViewed = (int) videoPlayerView.getCurrentPosition() / 1000;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                playedVideoSecs = appPreference.getPreviewTimerValue();
            }
            boolean isTvePlay = videoContentData.getSubscriptionPlans() != null && contentTypeChecker.isContentTVE(videoContentData.getSubscriptionPlans()) && appPreference.getTVEUserId() != null;
            boolean isAVOD_FreePlay = (videoContentData.getSubscriptionPlans() != null && (contentTypeChecker.isContentAVOD(videoContentData.getSubscriptionPlans()) || contentTypeChecker.isContentFree(videoContentData.getSubscriptionPlans())));
            boolean isSvodPlay = videoContentData.getSubscriptionPlans() != null && contentTypeChecker.isContentSVOD(videoContentData.getSubscriptionPlans()) && appCMSPresenter.isUserSubscribed();

            if (((maxPreviewSecs < playedVideoSecs) || (maxPreviewSecs < secsViewed)) && ((!appCMSMain.isMonetizationModelEnabled() && !appCMSPresenter.isUserSubscribed())
                    || (!isTvePlay && !isAVOD_FreePlay && !isSvodPlay))) {

                if (onUpdateContentDatumEvent != null) {
                    AppCMSPresenter.EntitlementPendingVideoData entitlementPendingVideoData
                            = new AppCMSPresenter.EntitlementPendingVideoData.Builder()
                            .action(appCMSPresenter.getStringDataById(getActivity(), R.string.app_cms_page_play_key))
                            .closerLauncher(false)
                            .contentDatum(onUpdateContentDatumEvent.getCurrentContentDatum())
                            .currentlyPlayingIndex(playIndex)
                            .pagePath(permaLink)
                            .filmTitle(title)
                            .extraData(null)
                            .relatedVideoIds(onUpdateContentDatumEvent.getCurrentRelatedVideoIds())
                            .currentWatchedTime(videoPlayerView.getCurrentPosition() / 1000)
                            .build();
                    appCMSPresenter.setEntitlementPendingVideoData(entitlementPendingVideoData);
                }


                appCMSPresenter.dismissPopupWindowPlayer(true);

                showEntitlementDialog = true;
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        //Log.d(TAG, "User is not subscribed - pausing video and showing Subscribe dialog");
                        pauseVideo();

                        if (videoPlayerView != null) {
                            videoPlayerView.disableController();
                        }

                        videoPlayerInfoContainer.setVisibility(VISIBLE);
                        //System.out.println(appPreference.getActiveSubscriptionPlanName()+" isUserSubscribed "+ appCMSPresenter.isUserSubscribed()+ " userpref "+appPreference.getIsUserSubscribed());
                        if (appCMSMain.isForceLogin() && !appCMSPresenter.isUserLoggedIn()
                                && CommonUtils.isFreeContent(appCMSPresenter.getCurrentContext(), onUpdateContentDatumEvent.getCurrentContentDatum())) {
                            appCMSPresenter.showEntitlementDialog(AppCMSPresenter.DialogType.ENTITLEMENT_LOGIN_REQUIRED,
                                    () -> {
                                        if (onClosePlayerEvent != null) {
                                            onClosePlayerEvent.closePlayer();
                                            showEntitlementDialog = false;
                                        }
                                    }, null);
                        } else if (appCMSPresenter.getAppCMSMain().isMonetizationModelEnabled()) {
                            appCMSPresenter.openEntitlementScreen(videoContentData, false);
                        } else if (appCMSPresenter.isUserLoggedIn()) {
                            //System.out.println("isUserSubscribed SUBSCRIPTION_REQUIRED_PLAYER "+ appCMSPresenter.isUserSubscribed());
                            appCMSPresenter.showEntitlementDialog(AppCMSPresenter.DialogType.SUBSCRIPTION_REQUIRED_PLAYER_PREVIEW,
                                    () -> {
                                        if (onClosePlayerEvent != null) {
                                            onClosePlayerEvent.closePlayer();
                                            showEntitlementDialog = false;
                                        }
                                    }, null);
                        } else {
                            appCMSPresenter.showEntitlementDialog(AppCMSPresenter.DialogType.LOGIN_AND_SUBSCRIPTION_REQUIRED_PLAYER_PREVIEW,
                                    () -> {
                                        if (onClosePlayerEvent != null) {
                                            onClosePlayerEvent.closePlayer();
                                            showEntitlementDialog = false;
                                        }
                                    }, null);
                        }
                    }
                });

                timerTask.cancel();
                entitlementCheckCancelled = true;
            } else {
                Log.d(TAG, "User is subscribed - resuming video");
            }
            playedVideoSecs++;
            appPreference.setPreviewTimerValue(playedVideoSecs);
        }
    }

    private void setCurrentWatchProgress(long runTime, long watchedTime) {
        System.out.println("watched time--" + watchedTime);

        if (runTime > 0 && watchedTime > 0 && runTime > watchedTime) {
            long playDifference = runTime - watchedTime;
            long playTimePercentage = ((watchedTime * 100) / runTime);

            if (playTimePercentage >= 98 && playDifference <= 30) {
                videoPlayTime = 0;
            } else {
                videoPlayTime = watchedTime;
            }
        } else {
            videoPlayTime = 0;
        }
    }

    private void sendFirebaseAnalyticsEvents(String screenVideoName) {
        if (screenVideoName == null || getActivity() == null) {
            return;
        }
        appCMSPresenter.getFirebaseAnalytics().screenViewEvent(getString(R.string.value_player_screen) + "-" + screenVideoName);
    }

    private void setCasting(boolean allowFreePlay) {
        try {
            if (BuildConfig.FLAVOR_app.equalsIgnoreCase(AppCMSPresenter.KINDLE_BUILD_VARIENT)) {
                mMediaRouteButton.setVisibility(GONE);
                return;
            }

            castProvider.setAllowFreePlay(allowFreePlay);
            castProvider.setRemotePlaybackCallback(callBackRemotePlayback);
            isCastConnected = castProvider.isCastingConnected();
            castProvider.playChromeCastPlaybackIfCastConnected();
            castProvider.setTvodCastAllowed(tvodCastAllowed, isTvodContent);
            castProvider.setSvodContent(isSvodContent);
            castProvider.setTrailer(isTrailer);
            castProvider.setTrailer(freeContent);
            if (isCastConnected) {
                getActivity().finish();
            } else {
                castProvider.setActivityInstance(getActivity(), mMediaRouteButton);
            }
        } catch (Exception e) {
            //Log.e(TAG, "Error initializing cast provider: " + e.getMessage());
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        final View decorView = getActivity().getWindow().getDecorView();
        decorView.setOnSystemUiVisibilityChangeListener(
                new View.OnSystemUiVisibilityChangeListener() {
                    @Override
                    public void onSystemUiVisibilityChange(int i) {
                        int height = decorView.getHeight();
                        if (getActivity().getWindow() != null) {
                            getActivity().getWindow().getDecorView()
                                    .setSystemUiVisibility(
                                            View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                                                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                                                    | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                                                    | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                                                    | View.SYSTEM_UI_FLAG_FULLSCREEN // hide status bar
                                                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
                        }
                        //Log.i(TAG, "Current height: " + height);
                    }
                });
    }

    @Override
    public void onResume() {
        if (null != drmObj && drmObj.isDrmVideoEnabled) {
            videoPlayerView.setDRMEnabled(drmObj.isDrmVideoEnabled);
            videoPlayerView.setLicenseUrl(drmObj.licenseUrl);
            videoPlayerView.setLicenseTokenDRM(drmObj.licenseToken);
            videoPlayerView.setAppOffline(drmObj.isOffline);
        }
        videoPlayerMainContainer.requestLayout();
        videoPlayerView.setAppCMSPresenter(appCMSPresenter);

        videoPlayerView.init(getContext());
        videoPlayerView.enableController();

        videoPlayerView.setClosedCaptionEnabled(appPreference.getClosedCaptionPreference());
        videoPlayerView.getPlayerView().getSubtitleView()
                .setVisibility(appPreference.getClosedCaptionPreference()
                        ? VISIBLE
                        : GONE);
        videoPlayerView.setAdsUrl(adsUrl);

        if (isOfflineVideoDowloaded) {
            try {
                Download offlineVideo = appCMSPresenter.getAppCMSApplication().getOfflineDRMManager().getDownloadTracker().getDowloadedVideoObject(appCMSPresenter.getCurrentPlayingVideo());
                OfflineVideoData offlineVideoData = appCMSPresenter.deserialize(offlineVideo.request.data);
                closedCaptionUrl = appCMSPresenter.downloadedMediaLocalURI(offlineVideoData.getCcFileEnqueueId());
                videoPlayerView.setOfflineSubtitleUri(Uri.parse(closedCaptionUrl));
            } catch (Exception e) {
            }
        }

        if (isVideoDownloaded) {
            List<DownloadClosedCaptionRealm> cListData = appCMSPresenter.getRealmController().getAllDownloadedCCFiles(filmId);
            if (cListData != null && cListData.size() > 0)
                closedCaptionUrl = appCMSPresenter.downloadedMediaLocalURI(cListData.get(0).getCcFileEnqueueId());
            if (closedCaptionUrl != null)
                videoPlayerView.setOfflineUri(Uri.parse(hlsUrl), Uri.parse(closedCaptionUrl));
            else
                videoPlayerView.setOfflineUri(Uri.parse(hlsUrl), null);
        }
        videoPlayerView.preparePlayer();

        if (!isLiveStreaming) {
            videoPlayerView.setCurrentPosition(videoPlayTime * SECS_TO_MSECS);
        }
        appCMSPresenter.setShowNetworkConnectivity(false);


        resumeVideo();
        //update rental start time
        updateVideoStartTime();

        if (isFromError) {
            isFromError = false;
            videoPlayerView.resumeStartPlayer();
        }

        super.onResume();
    }

    private void updateVideoStartTime() {
        if (onUpdateContentDatumEvent != null && onUpdateContentDatumEvent.getCurrentContentDatum() != null && onUpdateContentDatumEvent.getCurrentContentDatum().getGist() != null
                && onUpdateContentDatumEvent.getCurrentContentDatum().getGist().getRentStartTime() == 0 &&
                onUpdateContentDatumEvent.getCurrentContentDatum().getGist().getTransactionEndDate() == 0) {
            appCMSPresenter.updateVideoStartTime(onUpdateContentDatumEvent.getCurrentContentDatum().getGist().getId());
        }
    }

    @Override
    public void onPause() {
        Log.d(TAG, "onPause");
        pauseVideo();
        videoPlayTime = videoPlayerView.getCurrentPosition() / SECS_TO_MSECS;
        videoPlayerView.releasePlayer();
        super.onPause();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        getPercentageFromResource();
        if (videoPlayerView != null) {
            videoPlayerView.setFillBasedOnOrientation();
        }
    }

    private void pauseVideo() {
        if (videoPlayerView != null) {
            videoPlayerView.pausePlayer();
        }
        if (beaconPing != null) {
            beaconPing.sendBeaconPing = false;
        }
        if (beaconBuffer != null) {
            beaconBuffer.sendBeaconBuffering = false;
        }
    }

    private void resumeVideo() {
        if (null != videoPlayerView)
            videoPlayerView.resumePlayer();
        if (beaconPing != null) {
            beaconPing.sendBeaconPing = true;
        }
        if (beaconBuffer != null && appCMSPresenter.getAppCMSMain() != null && appCMSPresenter.getAppCMSMain().getFeatures() != null && appCMSPresenter.getAppCMSMain().getFeatures().isEnableQOS()) {
            beaconBuffer.sendBeaconBuffering = true;
        }
        //Log.d(TAG, "Resuming playback");

        if (castProvider != null) {
            castProvider.onActivityResume();
        }
    }

    private void sendAdImpression() {
        if (beaconPing != null) {
            beaconPing.sendBeaconPing = false;
            if (mProgressHandler != null)
                mProgressHandler.removeCallbacks(mProgressRunnable);
        }
        if (appCMSPresenter != null && appCMSPresenter.getAppCMSMain() != null && appCMSPresenter.getAppCMSMain().getFeatures() != null
                && appCMSPresenter.getAppCMSMain().getFeatures().getAnalyticsBeacon() != null
                && appCMSPresenter.getAppCMSMain().getFeatures().getAnalyticsBeacon().isEnabled()) {

            appCMSPresenter.sendBeaconMessage(filmId,
                    permaLink,
                    parentScreenName,
                    videoPlayerView.getCurrentPosition(),
                    false,
                    AppCMSPresenter.BeaconEvent.AD_IMPRESSION,
                    "Video",
                    videoPlayerView.getBitrate() != 0 ? String.valueOf(videoPlayerView.getBitrate()) : null,
                    String.valueOf(videoPlayerView.getVideoHeight()),
                    String.valueOf(videoPlayerView.getVideoWidth()),
                    mStreamId,
                    0d,
                    apod,
                    isVideoDownloaded);
        }
    }

    private void sendAdRequest() {
        if (!TextUtils.isEmpty(mStreamId) && appCMSPresenter != null && appCMSPresenter.getAppCMSMain() != null
                && appCMSPresenter.getAppCMSMain().getFeatures() != null
                && appCMSPresenter.getAppCMSMain().getFeatures().getAnalyticsBeacon() != null
                && appCMSPresenter.getAppCMSMain().getFeatures().getAnalyticsBeacon().isEnabled()) {
            appCMSPresenter.sendBeaconMessage(filmId,
                    permaLink,
                    parentScreenName,
                    videoPlayerView.getCurrentPosition(),
                    false,
                    AppCMSPresenter.BeaconEvent.AD_REQUEST,
                    "Video",
                    videoPlayerView.getBitrate() != 0 ? String.valueOf(videoPlayerView.getBitrate()) : null,
                    String.valueOf(videoPlayerView.getVideoHeight()),
                    String.valueOf(videoPlayerView.getVideoWidth()),
                    mStreamId,
                    0d,
                    apod,
                    isVideoDownloaded);
        }
    }

    private void resumeContent() {
        isAdDisplayed = false;
        // videoPlayerView.startPlayer();
        if (beaconPing != null) {
            beaconPing.sendBeaconPing = true;
        }

        if (beaconPing != null && !beaconPing.isAlive()) {
            beaconPing.start();

            if (mProgressHandler != null)
                mProgressHandler.post(mProgressRunnable);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        if (appCMSPresenter.isAppSVOD() && !freeContent) {
            if (entitlementCheckTimerTask != null) {
                entitlementCheckTimerTask.cancel();
            }

            if (entitlementCheckTimer != null) {
                entitlementCheckTimer.cancel();
            }
        }

        if (refreshToken) {
            if (refreshTokenTimerTask != null) {
                refreshTokenTimerTask.cancel();
            }

            if (refreshTokenTimer != null) {
                refreshTokenTimer.cancel();
            }
        }
        segmentId = null;
        setVideoSelected(null);
        streamingQualitySelector = null;
        seasonEpisodeSelctionEvent = null;
        onUpdateContentDatumEvent = null;
        closedCaptionSelector = null;
    }

    @Override
    public void onDestroyView() {

        if (appCMSPresenter.isAppSVOD() && !freeContent) {
            if (entitlementCheckTimerTask != null) {
                entitlementCheckTimerTask.cancel();
            }

            if (entitlementCheckTimer != null) {
                entitlementCheckTimer.cancel();
            }
        }

        videoPlayerView.setOnPlayerStateChanged(null);
        beaconPing.sendBeaconPing = false;
        beaconPing.runBeaconPing = false;
        beaconPing.videoPlayerView = null;
        beaconPing = null;

        if (mBeaconHandler != null) {
            mBeaconHandler.removeCallbacks(mBeaconRunnable);
            mBeaconHandler = null;
        }

        if (mBeaconRunnable != null) {
            mBeaconRunnable.onDestroyView();
            mBeaconRunnable = null;
        }

        if (mProgressHandler != null) {
            mProgressHandler.removeCallbacks(mProgressRunnable);
            mProgressHandler = null;
        }

        beaconBuffer.sendBeaconBuffering = false;
        beaconBuffer.runBeaconBuffering = false;
        beaconBuffer.videoPlayerView = null;
        beaconBuffer = null;

        onClosePlayerEvent = null;
        unbinder.unbind();
        super.onDestroyView();
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
                            (((float) (videoPlayerView.getCurrentPosition() / 1000) / mTotalVideoDuration) * 100);
                    if (appCMSPresenter.getmFireBaseAnalytics() != null) {
                        sendProgressAnalyticEvents(mPercentage);
                    }
                }
                mProgressHandler.postDelayed(this, 1000);
            }
        };
    }

    public void sendClipStartAnalyticEvent() {
        Bundle bundle = new Bundle();
        if (appCMSPresenter.getmFireBaseAnalytics() != null) {
            if (videoContentData != null && videoContentData.getCategories() != null &&
                    videoContentData.getCategories().size() > 0) {
                for (int position = 0; position < videoContentData.getCategories().size(); position++) {
                    if (videoContentData.getCategories().get(position).getTitle() != null &&
                            videoContentData.getCategories().get(position).getTitle().equalsIgnoreCase("_Clips")) {
                        bundle.putBoolean("isclip", true);
                        break;
                    } else
                        bundle.putBoolean("isclip", false);
                }
            } else
                bundle.putBoolean("isclip", false);
            appCMSPresenter.getmFireBaseAnalytics().logEvent(FIREBASE_CLIP_START, bundle);
        }
    }

    public void sendProgressAnalyticEvents(long progressPercent) {
        Bundle bundle = new Bundle();
        bundle.putString(FIREBASE_VIDEO_ID_KEY, filmId);
        bundle.putString(FIREBASE_VIDEO_NAME_KEY, title);
        bundle.putString(FIREBASE_PLAYER_NAME_KEY, FIREBASE_PLAYER_NATIVE);
        bundle.putString(FIREBASE_MEDIA_TYPE_KEY, FIREBASE_MEDIA_TYPE_VIDEO);
        bundle.putString(FIREBASE_SERIES_ID_KEY, seriesId);
        if (onUpdateContentDatumEvent.getCurrentContentDatum() != null && onUpdateContentDatumEvent.getCurrentContentDatum().getSeriesName() != null)
            bundle.putString(FIREBASE_SERIES_NAME_KEY, onUpdateContentDatumEvent.getCurrentContentDatum().getSeriesName());
        bundle.putLong("content_seconds_watched", videoPlayerView.getDuration());
        bundle.putString("platform_name", "Android");
        if (videoContentData != null && videoContentData.getSeriesData() != null && videoContentData.getSeriesData().size() > 0 &&
                videoContentData.getSeriesData().get(0).getGist() != null &&
                videoContentData.getSeriesData().get(0).getGist().getTitle() != null)
            bundle.putString("showname", videoContentData.getSeriesData().get(0).getGist().getTitle());

        if (videoContentData != null && videoContentData.getSeriesData() != null && videoContentData.getSeriesData().size() > 0) {
            bundle.putInt("episode_number", videoContentData.getSeriesData().get(0).getEpisodeNumber());
            bundle.putInt("season_number", videoContentData.getSeriesData().get(0).getSeasonNumber());
        }
        bundle.putString("network_name", getActivity().getApplicationInfo().loadLabel(getActivity().getPackageManager()).toString());
        String videoType = "free";
        if (onUpdateContentDatumEvent.getCurrentContentDatum() != null && !onUpdateContentDatumEvent.getCurrentContentDatum().getGist().isFree())
            videoType = "restricted";
        bundle.putString("video_asset_status", videoType);
        if (appCMSPresenter.getAppPreference().getTvProvider() != null)
            bundle.putString("tvprovider_name", appCMSPresenter.getAppPreference().getTvProvider());

        //Logs an app event.
        if (progressPercent == 0 && !isStreamStart) {
            isStreamStart = true;
            appCMSPresenter.getmFireBaseAnalytics().logEvent(FIREBASE_STREAM_START, bundle);
            appCMSPresenter.getAppPreference().setVideoWatchCount();
        }

        if (!isStreamStart) {
            isStreamStart = true;
            appCMSPresenter.getmFireBaseAnalytics().logEvent(FIREBASE_STREAM_START, bundle);
            appCMSPresenter.getAppPreference().setVideoWatchCount();
        }

        if (progressPercent >= 25 && progressPercent < 50 && !isStream25) {
            if (!isStreamStart) {
                isStreamStart = true;
                appCMSPresenter.getmFireBaseAnalytics().logEvent(FIREBASE_STREAM_START, bundle);
                appCMSPresenter.getAppPreference().setVideoWatchCount();
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

        if (progressPercent >= 75 && progressPercent < 95 && !isStream75) {
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
        if (progressPercent >= 95 && progressPercent <= 98 && !isStream95) {
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
            isStream95 = true;
            appCMSPresenter.getmFireBaseAnalytics().logEvent(FIREBASE_STREAM_95, bundle);

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
            if (!isStream95) {
                isStream95 = true;
                appCMSPresenter.getmFireBaseAnalytics().logEvent(FIREBASE_STREAM_95, bundle);
            }

            isStream100 = true;
            appCMSPresenter.getmFireBaseAnalytics().logEvent(FIREBASE_STREAM_100, bundle);

        }
    }

    @Override
    public void onRefreshTokenCallback() {
        if (onUpdateContentDatumEvent != null &&
                onUpdateContentDatumEvent.getCurrentContentDatum() != null &&
                onUpdateContentDatumEvent.getCurrentContentDatum().getGist() != null) {

            appCMSPresenter.refreshVideoData(getOriginalID(),
                    updatedContentDatum -> {
                        if (updatedContentDatum == null) {
                            getActivity().finish();
                            return;
                        }
                        onUpdateContentDatumEvent.updateContentDatum(updatedContentDatum);
                        if (onUpdateContentDatumEvent != null && onUpdateContentDatumEvent.getCurrentContentDatum() != null
                                && onUpdateContentDatumEvent.getCurrentContentDatum().getGist() != null
                                && onUpdateContentDatumEvent.getCurrentContentDatum().getGist().getSeriesId() != null
                                && !TextUtils.isEmpty(onUpdateContentDatumEvent.getCurrentContentDatum().getGist().getSeriesId())) {
                            seriesId = onUpdateContentDatumEvent.getCurrentContentDatum().getGist().getSeriesId();
                        }
//                        appCMSPresenter.isFromEntitlementAPI=false;
                        appCMSPresenter.getAppCMSSignedURL(filmId, appCMSSignedURLResult -> {
                            if (videoPlayerView != null && appCMSSignedURLResult != null) {
                                boolean foundMatchingMpeg = false;
                                if (!TextUtils.isEmpty(hlsUrl) && hlsUrl.contains("mp4")) {
                                    if (updatedContentDatum != null &&
                                            updatedContentDatum.getStreamingInfo() != null &&
                                            updatedContentDatum.getStreamingInfo().getVideoAssets() != null &&
                                            updatedContentDatum.getStreamingInfo()
                                                    .getVideoAssets()
                                                    .getMpeg() != null &&
                                            !updatedContentDatum.getStreamingInfo()
                                                    .getVideoAssets()
                                                    .getMpeg()
                                                    .isEmpty()) {
                                        updatedContentDatum.getGist()
                                                .setWatchedTime(videoPlayerView.getCurrentPosition() / 1000L);
                                        for (int i = 0;
                                             i < updatedContentDatum.getStreamingInfo()
                                                     .getVideoAssets()
                                                     .getMpeg()
                                                     .size() &&
                                                     !foundMatchingMpeg;
                                             i++) {
                                            int queryIndex = hlsUrl.indexOf("?");
                                            if (0 <= queryIndex) {
                                                if (updatedContentDatum.getStreamingInfo()
                                                        .getVideoAssets()
                                                        .getMpeg()
                                                        .get(0)
                                                        .getUrl()
                                                        .contains(hlsUrl.substring(0, queryIndex))) {
                                                    foundMatchingMpeg = true;
                                                    hlsUrl = updatedContentDatum.getStreamingInfo()
                                                            .getVideoAssets()
                                                            .getMpeg()
                                                            .get(0)
                                                            .getUrl();
                                                }
                                            }
                                        }
                                    }
                                }

                                videoPlayerView.updateSignatureCookies(appCMSSignedURLResult.getPolicy(),
                                        appCMSSignedURLResult.getSignature(),
                                        appCMSSignedURLResult.getKeyPairId());

                                if (foundMatchingMpeg && updatedContentDatum.getGist() != null) {
                                    videoPlayerView.preparePlayer();
                                    videoPlayerView.setCurrentPosition(updatedContentDatum.getGist()
                                            .getWatchedTime() * 1000L);
                                }

                            }
                        });


                    }, null, false, false, videoContentData);
        }
    }

    @Override
    public void onFinishCallback(String message) {

        AppCMSPresenter.BeaconEvent event;
        if (message.contains("Unable")) {
            event = AppCMSPresenter.BeaconEvent.DROPPED_STREAM;
        } else if (message.contains("Response")) {
            event = AppCMSPresenter.BeaconEvent.FAILED_TO_START;
        } else {
            event = AppCMSPresenter.BeaconEvent.FAILED_TO_START;
        }
        if (appCMSPresenter.getAppCMSMain() != null && appCMSPresenter.getAppCMSMain().getFeatures().isEnableQOS()) {
            appCMSPresenter.sendBeaconMessage(filmId,
                    permaLink,
                    parentScreenName,
                    videoPlayerView.getCurrentPosition(),
                    false,
                    event,
                    "Video",
                    videoPlayerView.getBitrate() != 0 ? String.valueOf(videoPlayerView.getBitrate()) : null,
                    String.valueOf(videoPlayerView.getVideoHeight()),
                    String.valueOf(videoPlayerView.getVideoWidth()),
                    mStreamId,
                    0d,
                    0,
                    isVideoDownloaded);
        }
        if (onClosePlayerEvent != null) {
            onClosePlayerEvent.closePlayer();
        }

        if (!TextUtils.isEmpty(message)) {
            Toast.makeText(getContext(), message, Toast.LENGTH_LONG).show();
        }
    }

    private void initViewForCRW() {
        contentRatingTitleHeader.setTextColor(Color.parseColor(fontColor));
        contentRatingTitleHeader.setText(localisedStrings.getContentRatingDescText());
        contentRatingTitleView.setTextColor(Color.parseColor(fontColor));
        contentRatingDiscretionView.setTextColor(Color.parseColor(fontColor));
        contentRatingDiscretionView.setText(localisedStrings.getContentRatingDiscretion());
        contentRatingBack.setTextColor(Color.parseColor(fontColor));
        contentRatingBack.setText(localisedStrings.getBackCta());
        int highlightColor = appCMSPresenter.getBlockTitleColor();
        contentRatingBackUnderline.setBackgroundColor(highlightColor);
        contentRatingHeaderView.setTextColor(highlightColor);
        contentRatingHeaderView.setText(localisedStrings.getContentRatingWarning());
        applyBorderToComponent(contentRatingInfoContainer, 1, highlightColor);
        progressBar.getProgressDrawable()
                .setColorFilter(highlightColor, PorterDuff.Mode.SRC_IN);
        progressBar.setMax(100);

        contentRatingBack.setOnClickListener(v -> getActivity().finish());
    }

    private void createContentRatingView() throws Exception {
        crwCreated = true;
        if (appCMSPresenter.shouldDisplayCRW() &&
                !isTrailer &&
                !getParentalRating().equalsIgnoreCase(getString(R.string.age_rating_converted_g)) && appCMSPresenter.getAppCMSMain() != null &&
                appCMSPresenter.getAppCMSMain().getFeatures() != null &&
                appCMSPresenter.getAppCMSMain().getFeatures().isUserContentRating() &&
                !getParentalRating().equalsIgnoreCase(getString(R.string.age_rating_converted_default)) &&
                watchedTime == 0) {
            videoPlayerMainContainer.setVisibility(GONE);
            contentRatingMainContainer.setVisibility(VISIBLE);
            //animateView();
            if (videoPlayerView != null) {
                videoPlayerView.pausePlayer();
            }
            startCountdown();
        } else {
            contentRatingMainContainer.setVisibility(GONE);
            videoPlayerMainContainer.setVisibility(VISIBLE);
            if (videoPlayerView != null) {
                if (!showEntitlementDialog) {
                    videoPlayerView.startPlayer(true);
                } else {
                    videoPlayerView.pausePlayer();
                }
            }
        }
    }

    private String getParentalRating() {
        if (!isTrailer && !TextUtils.isEmpty(parentalRating) &&
                !parentalRating.equalsIgnoreCase(getString(R.string.age_rating_converted_g)) &&
                !parentalRating.equalsIgnoreCase(getString(R.string.age_rating_converted_default)) &&
                watchedTime == 0) {
            contentRatingTitleView.setText(parentalRating);
        }
        return parentalRating != null ? parentalRating : getString(R.string.age_rating_converted_default);
    }

    private void startCountdown() {
        new CountDownTimer(totalCountdownInMillis, countDownIntervalInMillis) {
            @Override
            public void onTick(long millisUntilFinished) {
                long progress = (long) (100.0 * (1.0 - (double) millisUntilFinished / (double) totalCountdownInMillis));
//                Log.d(TAG, "CRW Progress:" + progress);
                if (null != progressBar && AppCMSPlayVideoFragment.this.isAdded())
                    progressBar.setProgress((int) progress);
            }

            @Override
            public void onFinish() {
                if (null != contentRatingMainContainer)
                    contentRatingMainContainer.setVisibility(GONE);
                if (null != videoPlayerMainContainer)
                    videoPlayerMainContainer.setVisibility(VISIBLE);
                if (null != videoPlayerView && isAdded())
                    videoPlayerView.startPlayer(true);
            }
        }.start();
    }

    private void applyBorderToComponent(View view, int width, int Color) {
        GradientDrawable rectangleBorder = new GradientDrawable();
        rectangleBorder.setShape(GradientDrawable.RECTANGLE);
        rectangleBorder.setStroke(width, Color);
        view.setBackground(rectangleBorder);
    }

    private void getPercentageFromResource() {
        float heightPercent = getResources().getFraction(R.fraction.mainContainerHeightPercent, 1, 1);
        float widthPercent = getResources().getFraction(R.fraction.mainContainerWidthPercent, 1, 1);
        float bottomMarginPercent = getResources().getFraction(R.fraction.app_cms_content_rating_progress_bar_margin_bottom_percent, 1, 1);

        PercentRelativeLayout.LayoutParams params = (PercentRelativeLayout.LayoutParams) contentRatingAnimationContainer.getLayoutParams();
        PercentLayoutHelper.PercentLayoutInfo info = params.getPercentLayoutInfo();

        PercentRelativeLayout.LayoutParams paramsProgressBar = (PercentRelativeLayout.LayoutParams) progressBar.getLayoutParams();
        PercentLayoutHelper.PercentLayoutInfo infoProgress = paramsProgressBar.getPercentLayoutInfo();

        info.heightPercent = heightPercent;
        info.widthPercent = widthPercent;
        infoProgress.bottomMarginPercent = bottomMarginPercent;

        contentRatingAnimationContainer.requestLayout();
        progressBar.requestLayout();
    }

    private void animateView() {

        animSequential = AnimationUtils.loadAnimation(getContext(),
                R.anim.sequential);
        animFadeIn = AnimationUtils.loadAnimation(getContext(),
                R.anim.fade_in);
        animFadeOut = AnimationUtils.loadAnimation(getContext(),
                R.anim.fade_out);
        animTranslate = AnimationUtils.loadAnimation(getContext(),
                R.anim.translate);

        animFadeIn.setAnimationListener(this);
        animFadeOut.setAnimationListener(this);
        animSequential.setAnimationListener(this);
        animTranslate.setAnimationListener(this);

        contentRatingMainContainer.setVisibility(VISIBLE);

        if (getParentalRating().contains(getString(R.string.age_rating_pg)) ||
                !getParentalRating().contains(getString(R.string.age_rating_g))) {
            contentRatingHeaderView.startAnimation(animFadeIn);
            contentRatingInfoContainer.startAnimation(animFadeIn);
        } else {
            contentRatingHeaderView.setVisibility(GONE);
        }
        contentRatingInfoContainer.setVisibility(VISIBLE);

        contentRatingTitleView.startAnimation(animSequential);
        contentRatingTitleHeader.startAnimation(animSequential);

        contentRatingTitleView.setVisibility(VISIBLE);
        contentRatingTitleHeader.setVisibility(VISIBLE);
    }

    @Override
    public void onAnimationStart(Animation animation) {
        //
    }

    @Override
    public void onAnimationEnd(Animation animation) {
        if (animation == animFadeIn) {
            if (showCRWWarningMessage &&
                    getParentalRating().contains(getString(R.string.age_rating_pg)) ||
                    !getParentalRating().contains(getString(R.string.age_rating_g))) {
                contentRatingDiscretionView.startAnimation(animFadeOut);
                contentRatingDiscretionView.setVisibility(VISIBLE);
                showCRWWarningMessage = false;
            } else {
                contentRatingDiscretionView.setVisibility(GONE);
            }
        }
    }

    @Override
    public void onAnimationRepeat(Animation animation) {
        //
    }

    @Override
    public void onResumeVideo() {
        if (videoPlayerView != null && !showEntitlementDialog) {
            resumeVideo();
        }

    }

    public PlayerSettingsView getPlayerSettingsView() {
        return playerSettingsView;
    }

    @Override
    public void launchSetting(ClosedCaptionSelectorAdapter closedCaptionSelectorAdapter,
                              StreamingQualitySelectorAdapter streamingQualitySelectorAdapter) {
        setPreviousNextVisibility(false);
        if (videoPlayerView != null) {
            videoPlayerView.pausePlayer();
        }

        videoPlayerMainContainer.setVisibility(GONE);
        playerSettingsView.setClosedCaptionSelectorAdapter(closedCaptionSelectorAdapter);
        playerSettingsView.setStreamingQualitySelectorAdapter(streamingQualitySelectorAdapter);
        playerSettingsView.updateSettingItems();
        playerSettingsView.setPlayerSettingsEvent(this);
        playerSettingsView.setVisibility(VISIBLE);
        if (appCMSPresenter.isPortraitViewing()) {
            appCMSPresenter.restrictLandscapeOnly();
        }


    }

    @Override
    public void launchSetting(ClosedCaptionSelectorAdapter closedCaptionSelectorAdapter,
                              HLSStreamingQualitySelectorAdapter videoQualityAdapter,
                              LanguageSelectorAdapter languageSelectorAdapter) {
        setPreviousNextVisibility(false);
        if (videoPlayerView != null) {
            videoPlayerView.pausePlayer();
        }
        videoPlayerMainContainer.setVisibility(GONE);
        playerSettingsView.setClosedCaptionSelectorAdapter(closedCaptionSelectorAdapter);
        playerSettingsView.setHlsStreamingQualitySelectorAdapter(videoQualityAdapter);
        playerSettingsView.setLanguageSelectorAdapter(languageSelectorAdapter);
        playerSettingsView.updateSettingItems();
        playerSettingsView.setPlayerSettingsEvent(this);
        playerSettingsView.setVisibility(VISIBLE);
        if (appCMSPresenter.isPortraitViewing()) {
            appCMSPresenter.restrictLandscapeOnly();
        }


    }

    @Override
    public void launchSetting(ClosedCaptionSelectorAdapter closedCaptionSelectorAdapter,
                              HLSStreamingQualitySelectorAdapter videoQualityAdapter,
                              StreamingQualitySelectorAdapter streamingQualitySelectorAdapter,
                              LanguageSelectorAdapter languageSelectorAdapter) {
        setPreviousNextVisibility(false);
        if (videoPlayerView != null) {
            videoPlayerView.pausePlayer();
        }
        videoPlayerMainContainer.setVisibility(GONE);
        playerSettingsView.setClosedCaptionSelectorAdapter(closedCaptionSelectorAdapter);
        playerSettingsView.setHlsStreamingQualitySelectorAdapter(videoQualityAdapter);
        playerSettingsView.setStreamingQualitySelectorAdapter(streamingQualitySelectorAdapter);
        playerSettingsView.setLanguageSelectorAdapter(languageSelectorAdapter);
        playerSettingsView.updateSettingItems();
        playerSettingsView.setPlayerSettingsEvent(this);
        playerSettingsView.setVisibility(VISIBLE);
        if (appCMSPresenter.isPortraitViewing()) {
            appCMSPresenter.restrictLandscapeOnly();
        }


    }

    @Override
    public void finishPlayerSetting() {
        playerSettingsView.setVisibility(GONE);
        videoPlayerMainContainer.setVisibility(VISIBLE);
        videoPlayerView.setClosedCaption(playerSettingsView.getSelectedClosedCaptionIndex());
        videoPlayerView.setStreamingQuality(playerSettingsView.getSelectedStreamingQualityIndex(), videoPlayerView.availableStreamingQualitiesHLS != null ? videoPlayerView.availableStreamingQualitiesHLS.get(playerSettingsView.getSelectedStreamingQualityIndex()) : "");
        videoPlayerView.setAudioLanguage(playerSettingsView.getLanguageSelectorIndex());
        videoPlayerView.startPlayer(true);
        /**
         if landscape only true than show player only in landscape view*/

        if (appCMSPresenter.isPlayerLandscapeOnly()) {
            appCMSPresenter.restrictLandscapeOnly();

        } else {
            appCMSPresenter.unrestrictPortraitOnly();
        }
    }

    public void setSelectionIndex() {
        playerSettingsView.setCCSelectionIndex();
    }

    public interface OnClosePlayerEvent {
        void closePlayer();

        /**
         * Method is to be called by the fragment to tell the activity that a movie is finished
         * playing. Primarily in the {@link ExoPlayer#STATE_ENDED}
         */
        void onMovieFinished();

        void onRemotePlayback(long currentPosition,
                              int castingMode,
                              boolean sentBeaconPlay,
                              Action1<CastHelper.OnApplicationEnded> onApplicationEndedAction);
    }

    public interface OnUpdateContentDatumEvent {
        void updateContentDatum(ContentDatum contentDatum);

        ContentDatum getCurrentContentDatum();

        List<String> getCurrentRelatedVideoIds();
    }

    public interface RegisterOnResumeVideo {
        void registerOnResumeVideo(OnResumeVideo onResumeVideo);
    }

    private String getOriginalID() {
        String filmId = onUpdateContentDatumEvent.getCurrentContentDatum()
                .getGist()
                .getOriginalObjectId();
        if (filmId == null) {
            filmId = onUpdateContentDatumEvent.getCurrentContentDatum()
                    .getGist()
                    .getId();
        }
        return filmId;
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
        long updatedRunTime = 0;
        try {

            updatedRunTime = onUpdateContentDatumEvent.getCurrentContentDatum().getGist().getRuntime();

        } catch (Exception e) {
            e.printStackTrace();
        }
        setCurrentWatchProgress(updatedRunTime, watchedTime);

        if (isVideoLoaded) {
            if (!isTrailer && !isLiveStreaming) {
                videoPlayerView.setCurrentPosition(videoPlayTime * SECS_TO_MSECS);
                boolean isWatchHistoryUpdateEnabled = appCMSPresenter.getAppCMSMain() != null
                        && appCMSPresenter.getAppCMSMain().getFeatures() != null
                        && appCMSPresenter.getAppCMSMain().getFeatures().getWatchedHistory() != null
                        && appCMSPresenter.getAppCMSMain().getFeatures().getWatchedHistory().isEnabled();
                if (isWatchHistoryUpdateEnabled) {
                    appCMSPresenter.updateWatchedTime(filmId, seriesId,
                            videoPlayerView.getCurrentPosition() / 1000, s -> {
                                UpdateHistoryResponse response = (UpdateHistoryResponse) s;
                                if (((UpdateHistoryResponse) s).getResponseCode() == 401) {
                                    pauseVideo();
                                    appCMSPresenter.showDialog(AppCMSPresenter.DialogType.MAX_STREAMS_ERROR, response.getErrorMessage(), true, null, null, null);
                                }
                            });
                }
            }

        }

    }



 /*   int phoneVolume;

    void setPlayerVolumeImage(ImageButton volumeImage, boolean defaultVolume) {
        audioManager = (AudioManager) getActivity().getSystemService(Context.AUDIO_SERVICE);
        int currentVal = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
        if (currentVal != 0)
            phoneVolume = currentVal;
        int maxVol = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.P) {
            int minVol = audioManager.getStreamMinVolume(AudioManager.STREAM_MUSIC);
        }

        if (defaultVolume) {
            if (currentVal == 0) {
                videoPlayerView.getPlayerVolume().setSelected(true);
                videoPlayerView.getPlayerVolume().setImageResource(R.drawable.player_mute);
                audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, 0, 0);
            } else {
                videoPlayerView.getPlayerVolume().setSelected(false);
                videoPlayerView.getPlayerVolume().setImageResource(R.drawable.player_volume);
                audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, currentVal, 0);
            }
        } else {
            if (volumeImage.isSelected()) {
                volumeImage.setImageResource(R.drawable.player_mute);
                audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, 0, 0);
            } else {
                volumeImage.setImageResource(R.drawable.player_volume);
                audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, phoneVolume, 0);
            }
        }

    }*/

   /* @Override
    public void volumeClick(ImageButton volume) {
        if (volume.isSelected()) {
            volume.setSelected(false);
        } else {
            volume.setSelected(true);
        }
       // setPlayerVolumeImage(volume, false);


    }*/

    private void setPreviousNextEpisodeImage(String imageUrl, AppCompatImageView imageView) {
        RequestOptions requestOptions = new RequestOptions()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .fitCenter();
        Glide.with(imageView.getContext())
                .load(imageUrl)
                .apply(requestOptions)
                .into(imageView);
    }

    public void setVideoSelected(VideoSelected videoSelected) {
        this.videoSelected = videoSelected;
    }


    VideoSelected videoSelected;
    boolean videoPaused;

    @Override
    public void playerState(boolean isVideoPaused) {
        videoPaused = isVideoPaused;
        if (isVideoPaused) {
            loadPrevNextImage();
        } else {
            setPreviousNextVisibility(false);
        }
    }

    public void loadPrevNextImage() {
        int currentPlayingEpisodePos = findCurrentPlayingPositionOfEpisode();
        if (allEpisodes != null && nextEpisodeContainer != null && currentPlayingEpisodePos < allEpisodes.size() - 1) {
            setPreviousNextEpisodeImage(allEpisodes.get(currentPlayingEpisodePos + 1).getGist().getImageGist().get_16x9(), nextEpisodeImg);
        }
        if (allEpisodes != null && previousEpisodeContainer != null && currentPlayingEpisodePos > 0) {
            setPreviousNextEpisodeImage(allEpisodes.get(currentPlayingEpisodePos - 1).getGist().getImageGist().get_16x9(), previousEpisodeImg);
        }
        setPreviousNextVisibility(true);
    }

    public void setPreviousNextVisibility(boolean visibility) {
        int currentPlayingEpisodePos = findCurrentPlayingPositionOfEpisode();
        if (allEpisodes != null && previousEpisodeContainer != null && nextEpisodeContainer != null) {
            if (visibility) {
                if (currentPlayingEpisodePos < allEpisodes.size() - 1)
                    nextEpisodeContainer.setVisibility(VISIBLE);
                if (currentPlayingEpisodePos >= 1)
                    previousEpisodeContainer.setVisibility(VISIBLE);
            } else {
                nextEpisodeContainer.setVisibility(GONE);
                previousEpisodeContainer.setVisibility(GONE);
            }
        }
    }


    public int getPlayerInfoContainerHeight() {
        return videoPlayerInfoContainer.getHeight();
    }

    public ConstraintLayout getRelatedVideoSection() {
        return relatedVideoSection;
    }

    public void updateWatchedHistory() {
        boolean isWatchHistoryUpdateEnabled = appCMSPresenter.getAppCMSMain() != null
                && appCMSPresenter.getAppCMSMain().getFeatures() != null
                && appCMSPresenter.getAppCMSMain().getFeatures().getWatchedHistory() != null
                && appCMSPresenter.getAppCMSMain().getFeatures().getWatchedHistory().isEnabled();
        boolean isLiveStream = videoContentData != null && videoContentData.getGist() != null && videoContentData.getGist().isLiveStream();
        if (isWatchHistoryUpdateEnabled && !isLiveStream) {
            //int interval = appCMSPresenter.getAppCMSMain().getFeatures().getWatchedHistory().getIntervalInt();
            if (videoPlayerView != null && !isTrailer /*&& interval <= (videoPlayerView.getCurrentPosition() / 1000)*/) {
                appCMSPresenter.updateWatchedTime(filmId, seriesId,
                        videoPlayerView.getCurrentPosition() / 1000, null);
            }
        }
    }

    private void setFont() {
        Component component = new Component();
        component.setFontWeight(getString(R.string.app_cms_page_font_bold_key));
        ViewCreator.setTypeFace(getContext(), appCMSPresenter, appCMSPresenter.getJsonValueKeyMap(), component, contentRatingHeaderView);
        ViewCreator.setTypeFace(getContext(), appCMSPresenter, appCMSPresenter.getJsonValueKeyMap(), component, contentRatingDiscretionView);
        ViewCreator.setTypeFace(getContext(), appCMSPresenter, appCMSPresenter.getJsonValueKeyMap(), component, contentRatingTitleView);
        ViewCreator.setTypeFace(getContext(), appCMSPresenter, appCMSPresenter.getJsonValueKeyMap(), component, contentRatingBack);
        component.setFontWeight(getString(R.string.app_cms_page_font_italic_key));
        if (null != contentRatingTitleHeader && null != appCMSPresenter)
            ViewCreator.setTypeFace(getContext(), appCMSPresenter, appCMSPresenter.getJsonValueKeyMap(), component, contentRatingTitleHeader);
        component.setFontWeight(null);
        if (null != videoPlayerTitleView && null != appCMSPresenter)
            ViewCreator.setTypeFace(getContext(), appCMSPresenter, appCMSPresenter.getJsonValueKeyMap(), component, videoPlayerTitleView);
        if (null != textViewVideoLoading && null != appCMSPresenter)
            ViewCreator.setTypeFace(getContext(), appCMSPresenter, appCMSPresenter.getJsonValueKeyMap(), component, textViewVideoLoading);
    }

    private void showTimer(ContentDatum contentDatum) {
        long eventDate = (contentDatum.getGist().getScheduleStartDate());
        //calculate remaining time from event date and current date
        long remainingTime = CommonUtils.getTimeIntervalForEvent(eventDate, "yyyy MMM dd HH:mm:ss");
        boolean isEventSchedule = false;
        String coverImage = null;

        if (contentDatum.getGist() != null
                && contentDatum.getGist().getEventSchedule() != null
                && contentDatum.getGist().getEventSchedule().size() > 0
                && contentDatum.getGist().getEventSchedule().get(0) != null
                && contentDatum.getGist().getEventSchedule().get(0).getEventTime() != 0) {
            eventDate = contentDatum.getGist().getEventSchedule().get(0).getEventTime();

            //calculate remaining time from event date and current date
            remainingTime = CommonUtils.getTimeIntervalForEventSchedule(eventDate * 1000L, "EEE MMM dd HH:mm:ss");
            isEventSchedule = true;
        }

        if (remainingTime > 0) {
            if (contentDatum.getGist().getImageGist() != null
                    && contentDatum.getGist().getImageGist().get_16x9() != null
                    && !TextUtils.isEmpty(contentDatum.getGist().getImageGist().get_16x9())) {
                coverImage = contentDatum.getGist().getImageGist().get_16x9();
            } else if (contentDatum.getGist().getVideoImageUrl() != null
                    && !TextUtils.isEmpty(contentDatum.getGist().getVideoImageUrl())) {
                coverImage = contentDatum.getGist().getVideoImageUrl();
            } else if (contentDatum.getImageUrl() != null
                    && !TextUtils.isEmpty(contentDatum.getImageUrl())) {
                coverImage = contentDatum.getImageUrl();
            }

            initializeFutureContentTimerView(eventDate, remainingTime, isEventSchedule, coverImage);
        }
    }

    public void initializeFutureContentTimerView(long eventDate, long remainingTime, boolean isEventSchedule, String coverImage) {
        timerView.initializeView(new TimerViewFutureContent.FutureEventCountdownListener() {
            @Override
            public void countDownOver() {
                timerView.setVisibility(GONE);
                startEntitlementCheckTimer();
            }

            @Override
            public void closeClick() {
                onClosePlayerEvent.closePlayer();
            }
        }, coverImage);
        timerView.startTimer(getActivity(), eventDate, remainingTime, isEventSchedule);
        timerView.setVisibility(VISIBLE);
        timerView.showCloseIcon(true);
    }
}