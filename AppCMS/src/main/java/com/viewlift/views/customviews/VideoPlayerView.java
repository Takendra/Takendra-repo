package com.viewlift.views.customviews;

import android.animation.LayoutTransition;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.database.ContentObserver;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.DrawableContainer;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.StateListDrawable;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Surface;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.MainThread;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.UiThread;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.AppCompatToggleButton;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.ads.interactivemedia.v3.api.AdEvent;
import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.DefaultRenderersFactory;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.Format;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.PlaybackPreparer;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.RenderersFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.audio.AudioAttributes;
import com.google.android.exoplayer2.decoder.DecoderCounters;
import com.google.android.exoplayer2.drm.DefaultDrmSessionEventListener;
import com.google.android.exoplayer2.drm.DefaultDrmSessionManager;
import com.google.android.exoplayer2.drm.DrmSessionManager;
import com.google.android.exoplayer2.drm.ExoMediaCrypto;
import com.google.android.exoplayer2.drm.ExoMediaDrm;
import com.google.android.exoplayer2.drm.FrameworkMediaCrypto;
import com.google.android.exoplayer2.drm.FrameworkMediaDrm;
import com.google.android.exoplayer2.drm.HttpMediaDrmCallback;
import com.google.android.exoplayer2.drm.MediaDrmCallback;
import com.google.android.exoplayer2.drm.UnsupportedDrmException;
import com.google.android.exoplayer2.ext.ima.ImaAdsLoader;
import com.google.android.exoplayer2.offline.Download;
import com.google.android.exoplayer2.offline.DownloadHelper;
import com.google.android.exoplayer2.source.BehindLiveWindowException;
import com.google.android.exoplayer2.source.ConcatenatingMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.MediaSourceEventListener;
import com.google.android.exoplayer2.source.MediaSourceFactory;
import com.google.android.exoplayer2.source.MergingMediaSource;
import com.google.android.exoplayer2.source.ProgressiveMediaSource;
import com.google.android.exoplayer2.source.SingleSampleMediaSource;
import com.google.android.exoplayer2.source.TrackGroup;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.source.ads.AdPlaybackState;
import com.google.android.exoplayer2.source.ads.AdsLoader;
import com.google.android.exoplayer2.source.ads.AdsMediaSource;
import com.google.android.exoplayer2.source.dash.DashMediaSource;
import com.google.android.exoplayer2.source.hls.HlsMediaSource;
import com.google.android.exoplayer2.source.smoothstreaming.SsMediaSource;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.MappingTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.ui.AspectRatioFrameLayout;
import com.google.android.exoplayer2.ui.DefaultTimeBar;
import com.google.android.exoplayer2.ui.DefaultTrackNameProvider;
import com.google.android.exoplayer2.ui.TrackNameProvider;
import com.google.android.exoplayer2.upstream.AssetDataSource;
import com.google.android.exoplayer2.upstream.ContentDataSource;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DataSource.Factory;
import com.google.android.exoplayer2.upstream.DataSpec;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSource;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSource;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;
import com.google.android.exoplayer2.upstream.FileDataSource;
import com.google.android.exoplayer2.upstream.HttpDataSource;
import com.google.android.exoplayer2.upstream.TransferListener;
import com.google.android.exoplayer2.util.Assertions;
import com.google.android.exoplayer2.util.MimeTypes;
import com.google.android.exoplayer2.util.Util;
import com.google.android.exoplayer2.video.VideoListener;
import com.google.android.exoplayer2.video.VideoRendererEventListener;
import com.viewlift.AppCMSApplication;
import com.viewlift.BuildConfig;
import com.viewlift.R;
import com.viewlift.Utils;
import com.viewlift.db.AppPreference;
import com.viewlift.models.data.appcms.api.ClosedCaptions;
import com.viewlift.models.data.appcms.api.ContentDatum;
import com.viewlift.models.data.appcms.api.Gist;
import com.viewlift.models.data.appcms.ui.CCFontSize;
import com.viewlift.models.data.appcms.ui.main.LocalisedStrings;
import com.viewlift.models.data.playersettings.HLSStreamingQuality;
import com.viewlift.models.data.playersettings.PlayerEventPayLoad;
import com.viewlift.offlinedrm.OfflineVideoData;
import com.viewlift.presenters.AppCMSPresenter;
import com.viewlift.utils.CommonUtils;
import com.viewlift.utils.ContentTypeChecker;
import com.viewlift.views.activity.AppCMSPlayVideoActivity;
import com.viewlift.views.adapters.AudioSelectorAdapter;
import com.viewlift.views.adapters.ClosedCaptionSelectorAdapter;
import com.viewlift.views.adapters.ClosedCaptionSizeSelectorAdapter;
import com.viewlift.views.adapters.HLSStreamingQualitySelectorAdapter;
import com.viewlift.views.adapters.LanguageSelectorAdapter;
import com.viewlift.views.adapters.StreamingQualitySelectorAdapter;
import com.viewlift.views.customviews.exoplayerview.AppCMSPlayerView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.UUID;

import javax.inject.Inject;

import rx.Observable;
import rx.functions.Action1;

import static com.google.android.exoplayer2.Player.STATE_BUFFERING;

public class VideoPlayerView extends FrameLayout implements Player.EventListener,
        MediaSourceEventListener, VideoListener,
        VideoRendererEventListener,
        PlaybackPreparer,
        DefaultDrmSessionEventListener {
    // DefaultDrmSessionManager.EventListener {
    private static final String TAG = "VideoPlayerView";
    private final String FIREBASE_Ad_START = "ad_start";
    private final String FIREBASE_Ad_END = "ad_end";
    private final String FIREBASE_Ad_SECONDS_WATCHED = "ad_seconds_watch";
    private long playerCurrentPosition = 0L;
    private static DefaultBandwidthMeter BANDWIDTH_METER = null;
    protected DataSource.Factory mediaDataSourceFactory;
    protected String userAgent;
    protected PlayerState playerState;
    protected SimpleExoPlayer player;
    DrmSessionManager<ExoMediaCrypto> drmSessionManager = null;
    //protected AppCMSSimpleExoPlayerView playerView;
    protected AppCMSPlayerView playerView;
    boolean isLoadedNext;
    OnBeaconAdsEvent onBeaconAdsEvent;
    DefaultTrackSelector trackSelector;
    DefaultTrackSelector.Parameters trackSelectorParameters;
    TrackGroupArray trackGroups;
    @Inject
    AppPreference appPreference;
    @Inject
    AppCMSPresenter appCMSPresenter;
    @Inject
    LocalisedStrings localisedStrings;

    View playerActionOverlay;
    AppCompatButton buttonSeekAction;

    public static final int PLAYER_EVENT_PAYLOAD_SKIP_INTRO = 90001;
    public static final int PLAYER_EVENT_PAYLOAD_SKIP_RECAP = 90002;
    public static final int PLAYER_EVENT_PAYLOAD_PLAY_NEXT = 90003;

    private AppCompatImageButton ccToggleButton;
    private AppCompatImageButton mSettingButton;
    protected AppCompatToggleButton mZoomButton;
    private AppCompatTextView playerLiveText;
    public AppCompatImageButton playerVolume;
    private LinearLayoutCompat chromecastLivePlayerParent;
    private ViewGroup chromecastButtonPreviousParent;
    private FrameLayout chromecastButtonPlaceholder;
    private AppCompatImageButton enterFullscreenButton;
    private AppCompatImageButton exitFullscreenButton;
    private AppCompatImageButton relatedVideoButton;
    private ConstraintLayout parentLayout;
    protected AppCompatTextView currentStreamingQualitySelector;
    protected AppCompatTextView playFromBeginning;
    private AppCompatTextView currentAudioSelector;
    private AlwaysSelectedTextView videoPlayerTitle;
    private DefaultTimeBar timeBar;
    private boolean isClosedCaptionEnabled = false;
    protected Uri uri;
    private Action1<PlayerState> onPlayerStateChanged;
    private Action1<Integer> onPlayerControlsStateChanged;
    private Action1<Boolean> onClosedCaptionButtonClicked;
    protected int resumeWindow;
    protected long resumePosition;
    private int timeBarColor;
    private long bitrate = 0L;
    private int videoHeight = 0;
    private int videoWidth = 0;
    private long mCurrentPlayerPosition;
    private ErrorEventListener mErrorEventListener;
    protected StreamingQualitySelector streamingQualitySelector;
    protected ClosedCaptionSelector closedCaptionSelector;
    protected VideoPlayerSettingsEvent videoPlayerSettingsEvent;
    private Map<String, Integer> failedMediaSourceLoads;
    private int fullscreenResizeMode;
    private Uri closedCaptionUri;
    private ClosedCaptions closedCaptions;
    private String policyCookie;
    private String signatureCookie;
    private String keyPairIdCookie;
    private boolean playerJustInitialized;
    private boolean playOnReattach;

    private boolean isDRMEnabled;
    private String licenseUrlDRM;
    private String licenseTokenDRM;
    private FrameworkMediaDrm mediaDrm;

    public boolean isAppOffline() {
        return isAppOffline;
    }

    public void setAppOffline(boolean appOffline) {
        isAppOffline = appOffline;
    }

    private boolean isAppOffline;
    private String filmId;


    private RecyclerView qualitySelectorRecyclerView;
    private RecyclerView closedCaptionSelectorRecyclerView;
    private RecyclerView closedCaptionSizeSelectorRecyclerView;
    protected ClosedCaptionSelectorAdapter closedCaptionSelectorAdapter;
    protected ClosedCaptionSizeSelectorAdapter closedCaptionSizeSelectorAdapter;
    private RecyclerView lanaguageSelectorRecyclerView;

    protected StreamingQualitySelectorAdapter listViewAdapter;
    protected HLSStreamingQualitySelectorAdapter hlsListViewAdapter;
    protected LanguageSelectorAdapter languageSelectorAdapter;
    protected AudioSelectorAdapter audioSelectorAdapter;
    private boolean fullScreenMode;
    private AdaptiveTrackSelection.Factory videoTrackSelectionFactory;
    private int mVideoRendererIndex = -1;
    private int mAudioRendererIndex = -1;
    private int mTextRendererIndex = -1;
    protected boolean streamingQualitySelectorCreated;
    protected boolean useHls;
    protected boolean closedCaptionSelectorCreated;
    protected boolean audioSelectorCreated;
    protected boolean audioSelectorShouldCreated = true;
    protected boolean closedCaptionHlsEmbeded;
    private int selectedSubtitleIndex;
    private boolean shouldShowSubtitle;
    private boolean selectedSubtitleLanguageAvailable;
    private boolean isBitRateUpdatedCT = false;
    AudioManager audioManager;

    public List<HLSStreamingQuality> availableStreamingQualitiesHLS;
    List<String> availableStreamingQualities;
    List<Format> availableAudioTracks;
    List<ClosedCaptions> availableClosedCaptions;
    public List<String> languageList;
    boolean isDefaultAudioAvailable = false;
    private boolean manualPause = false;
    ContentTypeChecker contentTypeChecker;

    public VideoPlayerView(Context context) {
        super(context);
        initializeView(context);
    }

    public VideoPlayerView(Context context, AppCMSPresenter appCMSPresenter) {
        super(context);
        initializeView(context);
    }

    public VideoPlayerView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initializeView(context);
    }

    public VideoPlayerView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initializeView(context);
    }

    public SimpleExoPlayer getPlayer() {
        return player;
    }

    public void setOnPlayerStateChanged(Action1<PlayerState> onPlayerStateChanged) {
        this.onPlayerStateChanged = onPlayerStateChanged;
    }

    public void setOnPlayerControlsStateChanged(Action1<Integer> onPlayerControlsStateChanged) {
        this.onPlayerControlsStateChanged = onPlayerControlsStateChanged;
    }

    public void setOnClosedCaptionButtonClicked(Action1<Boolean> onClosedCaptionButtonClicked) {
        this.onClosedCaptionButtonClicked = onClosedCaptionButtonClicked;
    }

    public boolean isManualPause() {
        return manualPause;
    }

    public void setManualPause(boolean manualPause) {
        System.out.println("playWhenReady  : setManualPause  "+manualPause);
        this.manualPause = manualPause;
    }

    public void setUriOnConnection() {
        this.uri = uri;
        try {
            player.prepare(buildMediaSource());
            player.seekTo(mCurrentPlayerPosition);
        } catch (IllegalStateException e) {
            //Log.e(TAG, "Unsupported video format for URI: " + uri.toString());
        }
    }

    public long getPlayerCurrrentPosition() {
        return mCurrentPlayerPosition;
    }

    String adsUrl;
    AdsMediaSource adsMediaSource;

    public void setAdsUrl(String adsUrl) {
        if (appCMSPresenter.getPlatformType() == AppCMSPresenter.PlatformType.ANDROID) {
            if (mImaAdsLoader == null && adsUrl != null && !TextUtils.isEmpty(adsUrl)) {
                mImaAdsLoader = createAdsLoader(getAdTagUri(adsUrl));
            }
        }
        this.adsUrl = adsUrl;

    }

    public void setUri(Uri videoUri, Uri closedCaptionUri) {
        this.uri = videoUri;
        String strUri = videoUri.toString().split("\\?")[0];
        this.uri = Uri.parse(strUri);
        this.closedCaptionUri = closedCaptionUri;
        try {
            MediaSource mediaSource = buildMediaSource(videoUri, closedCaptionUri);
            if (adsUrl != null && !TextUtils.isEmpty(adsUrl)) {
                mediaSource = createAdsMediaSource(mediaSource);
            }
            player.prepare(mediaSource);
        } catch (IllegalStateException e) {
            //Log.e(TAG, "Unsupported video format for URI: " + videoUri.toString());
        }
        if (appCMSPresenter != null/* && appCMSPresenter.getPlatformType() == AppCMSPresenter.PlatformType.ANDROID*/) {
            if (closedCaptionUri == null) {
                toggleCCSelectorVisibility(false);
                settingsButtonVisibility(false);
            } else if (closedCaptionUri != null) {
                closedCaptionSelectorCreated = false;
            } else {
                if (ccToggleButton != null) {
//                    ccToggleButton.setChecked(isClosedCaptionEnabled);
//                    ccToggleButton.setVisibility(VISIBLE);
                }
            }

        } else {
            toggleCCSelectorVisibility(false);
            settingsButtonVisibility(false);
        }

        try {
            if (getContext().getResources().getBoolean(R.bool.enable_stream_quality_selection) &&
                    currentStreamingQualitySelector != null &&
                    streamingQualitySelector != null) {
                List<String> availableStreamingQualities = streamingQualitySelector.getAvailableStreamingQualities();
                if (0 < availableStreamingQualities.size()) {
                    int streamingQualityIndex = streamingQualitySelector.getMpegResolutionIndexFromUrl(videoUri.toString());
                    if (0 <= streamingQualityIndex) {
                        currentStreamingQualitySelector.setText(availableStreamingQualities.get(streamingQualityIndex));
                        setSelectedStreamingQualityIndex();
                    }
                }
                if (availableStreamingQualities.size() == 0) {
                    currentStreamingQualitySelector.setVisibility(GONE);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * This method doesn't require Video Urls and Subtitle Urls in arguments because both of those
     * are queried, in {@link #buildMediaSource()} from the hosting activity using interfaces
     * which have methods implemented
     * eg. {@link AppCMSPlayVideoActivity#getAvailableClosedCaptions()}
     */

    Uri offlineVideoUri, offlineClosedCaptionUri;

    public void setOfflineUri(Uri videoUri, Uri closedCaptionUri) {
        this.offlineVideoUri = videoUri;
        this.offlineClosedCaptionUri = closedCaptionUri;
    }

    public void setOfflineSubtitleUri(Uri closedCaptionUri) {
        this.offlineClosedCaptionUri = closedCaptionUri;
    }

    OfflineVideoData offlineVideoData;

    @UiThread
    public void preparePlayer() {
        try {
            MediaSource mediaSource = null;
            if (isOfflineVideoDownloaded) {
                DefaultDrmSessionManager<ExoMediaCrypto> drmSessionManager;
                drmSessionManager = buildDrmSessionManager(getContext(), getLicenseUrl(), getLicenseTokenDRM());
                offlineVideoData = appCMSPresenter.deserialize(offlineVideo.request.data);
                if (offlineVideoData != null) {
                    if (appCMSPresenter.loadOfflineLicenseKeys() != null) {
                        appCMSPresenter.checkLicenseDuration(appCMSPresenter.loadOfflineLicenseKeys().get(getFilmId()), getFilmId(), drmSessionManager, this, isAppOffline);
                    }
                }
            } else {
                mediaSource = buildMediaSource();
                if (null != player && null != mediaSource) {
                    player.prepare(mediaSource);
                }
            }
        } catch (Exception e) {
            Log.e(TAG, "Unsupported video format for URI: " + e.getMessage());
        }
    }

    public void prepareDRMPlayer(DefaultDrmSessionManager<ExoMediaCrypto> drmSessionManager) {
        MediaSource mediaSource = null;

        if (appCMSPresenter.loadOfflineLicenseKeys() != null) {
            drmSessionManager.setMode(DefaultDrmSessionManager.MODE_PLAYBACK, appCMSPresenter.loadOfflineLicenseKeys().get(getFilmId()));
        }

        List<MediaSource> mediaSourceList = new ArrayList<>();
        mediaSource = DownloadHelper.createMediaSource(offlineVideo.request, mediaDataSourceFactory, drmSessionManager);
        mediaSourceList.add(mediaSource);
        //offlineClosedCaptionUri=Uri.parse("file:///storage/emulated/0/Android/data/air.com.snagfilms/files/closedCaptions/1590722903849_runfinalv211052020.srt");

        if (offlineClosedCaptionUri != null && !offlineClosedCaptionUri.toString().equalsIgnoreCase("file:///")) {
            Format textFormat = Format.createTextSampleFormat(null,
                    MimeTypes.APPLICATION_SUBRIP,
                    C.SELECTION_FLAG_DEFAULT,
                    "en");

            SingleSampleMediaSource singleSampleMediaSource = new SingleSampleMediaSource.Factory(mediaDataSourceFactory)
                    .createMediaSource(offlineClosedCaptionUri,
                            textFormat,
                            C.TIME_UNSET);
            mediaSourceList.add(singleSampleMediaSource);
            settingsButtonVisibility(true);
            setCCToggleButtonSelection(true);
            if (ccToggleButton.isSelected() && appCMSPresenter.getClosedCaptionPreference())
                setSubtitleViewVisibility(true);
            closeCaptionPreferenceCheck("english", 1);
            // Convert list into array and pass onto the MergingMediaSource constructor
            MediaSource[] mediaSources = new MediaSource[mediaSourceList.size()];
            mediaSourceList.toArray(mediaSources);
            mediaSource = new MergingMediaSource(mediaSources);
        } else {
            /*Disable CC if the user has turned CC off from settings*/
            settingsButtonVisibility(false);
            toggleCCSelectorVisibility(false);
            setCCToggleButtonSelection(false);
            setSubtitleViewVisibility(false);
            // Convert list into array and pass onto the MergingMediaSource constructor
            MediaSource[] mediaSources = new MediaSource[mediaSourceList.size()];
            mediaSourceList.toArray(mediaSources);
            mediaSource = new ConcatenatingMediaSource(mediaSources);
        }
        player.prepare(mediaSource);
        setCurrentPosition(getPlayerCurrentPosition());
    }

    private ImaAdsLoader createAdsLoader(Uri adTagUri) {

        try {
            mImaAdsLoader = new ImaAdsLoader.Builder(getContext())
                    .setAdEventListener(new PlayerAdEvent())
                    .buildForAdTag(adTagUri);
            mImaAdsLoader.setPlayer(player);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return mImaAdsLoader;
    }

    public Uri getUri() {
        return uri;
    }

    public boolean shouldPlayWhenReady() {
        return player != null && player.getPlayWhenReady();
    }

    public void startPlayer(boolean playWhenReady) {
        if (player != null) {
            player.setPlayWhenReady(playWhenReady);
            if (appCMSPresenter != null) {
                appCMSPresenter.sendKeepScreenOnAction();
            }
        }
    }

    public void resumePlayer() {
        if (player != null) {
            if (playerJustInitialized) {
                player.setPlayWhenReady(true);
                playerJustInitialized = false;
            } else {
                player.setPlayWhenReady(player.getPlayWhenReady());
            }

            if (appCMSPresenter != null) {
                if (player.getPlayWhenReady()) {
                    appCMSPresenter.sendKeepScreenOnAction();
                } else {
                    appCMSPresenter.sendClearKeepScreenOnAction();
                }
            }
            appCMSPresenter.cancelInternalEvents();
        }
    }

    public void resumeStartPlayer() {
        if (player != null) {
            player.setPlayWhenReady(true);
        }
    }

    public void pausePlayer() {
        if (player != null) {
            player.setPlayWhenReady(false);
            if (appCMSPresenter != null) {
                appCMSPresenter.sendClearKeepScreenOnAction();
            }
        }

    }

    public void stopPlayer() {
        if (player != null) {
            player.stop();
            if (appCMSPresenter != null) {
                appCMSPresenter.sendClearKeepScreenOnAction();
                appCMSPresenter.restartInternalEvents();
            }
        }
        if (appCMSPresenter.getTemplateType() == AppCMSPresenter.TemplateType.NEWS
                && volumeObserver != null) {
            getContext().getContentResolver().unregisterContentObserver(volumeObserver);
        }
    }

    public void releasePlayer() {
        if (player != null) {
            player.release();
            if (appCMSPresenter != null) {
                appCMSPresenter.sendClearKeepScreenOnAction();
            }
        }
        if (mImaAdsLoader != null) {
            mImaAdsLoader.setPlayer(null);
        }
        if (playNextHandler != null) {
            playNextHandler.removeCallbacksAndMessages(null);
        }
        if (skipIntroHandler != null) {
            skipIntroHandler.removeCallbacksAndMessages(null);
        }
        if (skipRecapHandler != null) {
            skipRecapHandler.removeCallbacksAndMessages(null);
        }
    }

    public long getDuration() {
        if (player != null) {
            return player.getDuration();
        }

        return -1L;
    }

    public long getCurrentPosition() {
        if (player != null) {
            // return player.getContentPosition();
            return player.getCurrentPosition();
        }

        return -1L;
    }

    public long getPlayerCurrentPosition() {
        return playerCurrentPosition;
    }

    public void setPlayerCurrentPosition(long playerCurrentPosition) {
        this.playerCurrentPosition = playerCurrentPosition;
    }

    public void setCurrentPosition(long currentPosition) {
        if (player != null) {
            if (CommonUtils.isPlayFromBeginning) {
                currentPosition = 0;
            }
            setPlayerCurrentPosition(currentPosition);
            player.seekTo(currentPosition);
        }
    }

    public long getBitrate() {
        return bitrate;
    }

    public void setBitrate(long bitrate) {
        this.bitrate = bitrate;
    }

    public int getVideoHeight() {
        return videoHeight;
    }

    public void setVideoHeight(int videoHeight) {
        this.videoHeight = videoHeight;
    }

    public int getVideoWidth() {
        return videoWidth;
    }

    public void setVideoWidth(int videoWidth) {
        this.videoWidth = videoWidth;
    }

    public void setClosedCaptionEnabled(boolean closedCaptionEnabled) {
        isClosedCaptionEnabled = closedCaptionEnabled;
    }

    public AppCMSPlayerView getPlayerView() {
        return playerView;
    }

    public void setFillBasedOnOrientation() {
        if (BaseView.isLandscape(getContext())) {
            if (mZoomButton != null) {
                mZoomButton.setVisibility(View.VISIBLE);
            }
            playerView.setResizeMode(fullscreenResizeMode);
        } else {
            if (mZoomButton != null) {
                mZoomButton.setVisibility(View.GONE);
            }
            playerView.setResizeMode(AspectRatioFrameLayout.RESIZE_MODE_FIT);
        }
    }

    public void enableController() {
        playerView.setUseController(true);
    }

    public void disableController() {
        if (playerView != null)
            playerView.setUseController(false);
    }

    public void updateSignatureCookies(String policyCookie,
                                       String signatureCookie,
                                       String keyPairIdCookie) {
        if (mediaDataSourceFactory != null &&
                mediaDataSourceFactory instanceof UpdatedUriDataSourceFactory) {
            ((UpdatedUriDataSourceFactory) mediaDataSourceFactory).updateSignatureCookies(policyCookie,
                    signatureCookie,
                    keyPairIdCookie);
        }
    }

    private void initializeView(Context context) {
        ((AppCMSApplication) context.getApplicationContext()).getAppCMSPresenterComponent().inject(this);
        BANDWIDTH_METER = new DefaultBandwidthMeter.Builder(getContext()).build();
        int layout = R.layout.video_player_view;
        if (appCMSPresenter.isNewsTemplate()) {
            //layout = R.layout.video_player_view_news;
        }
        LayoutInflater.from(context).inflate(layout, this);
        playerView = findViewById(R.id.videoPlayerView);
        playerJustInitialized = true;
        fullScreenMode = false;
        init(context);
    }

    public void init(Context context) {
        initializePlayer(context);
        playerState = new PlayerState();
        failedMediaSourceLoads = new HashMap<>();
        if (appCMSPresenter.getTemplateType() == AppCMSPresenter.TemplateType.NEWS) {
            volumeObserver = new VolumeObserver(context, new Handler());
            if (getPlayerVolume() != null) {
                setPlayerVolumeImage(getPlayerVolume(), true);
            }
            getContext().getContentResolver().registerContentObserver(android.provider.Settings.System.CONTENT_URI, true, volumeObserver);
        }
        if (contentTypeChecker == null)
            contentTypeChecker = new ContentTypeChecker(context);
    }

    public StreamingQualitySelector getStreamingQualitySelector() {
        return streamingQualitySelector;
    }

    public void setStreamingQualitySelector(StreamingQualitySelector streamingQualitySelector) {
        this.streamingQualitySelector = streamingQualitySelector;
    }


    public ClosedCaptionSelector getClosedCaptionSelector() {
        return closedCaptionSelector;
    }

    public void setClosedCaptionsSelector(ClosedCaptionSelector closedCaptionSelector) {
        this.closedCaptionSelector = closedCaptionSelector;
    }

    public void setVideoPlayerSettingsEvent(VideoPlayerSettingsEvent videoPlayerSettingsEvent) {
        this.videoPlayerSettingsEvent = videoPlayerSettingsEvent;
    }

    public void setSeasonEpisodeSelctionEvent(SeasonEpisodeSelctionEvent seasonEpisodeSelctionEvent) {
        this.seasonEpisodeSelctionEvent = seasonEpisodeSelctionEvent;
    }

    public boolean shouldPlayOnReattach() {
        return playOnReattach;
    }

    Download offlineVideo;
    boolean isOfflineVideoDownloaded;

    @MainThread
    private void initializePlayer(Context context) {
        resumeWindow = C.INDEX_UNSET;
        resumePosition = C.TIME_UNSET;
//        userAgent = Util.getUserAgent(getContext(),
//                getContext().getString(R.string.app_cms_user_agent));
        userAgent = CommonUtils.getUserAgent(appCMSPresenter);
        useHls = Utils.isHLS();

        ccToggleButton = createCC_ToggleButton();
        if (appCMSPresenter.getPlatformType() == AppCMSPresenter.PlatformType.TV) {
            if (appCMSPresenter.isNewsTemplate()) {
                ((RelativeLayout) playerView.findViewById(R.id.playerRightControls)).addView(ccToggleButton);
            } else {
                ((RelativeLayout) playerView.findViewById(R.id.exo_controller_container)).addView(ccToggleButton);
            }
        }
        currentStreamingQualitySelector = playerView.findViewById(R.id.streamingQualitySelector);
        currentAudioSelector = playerView.findViewById(R.id.audioSelector);

        if (appCMSPresenter.getPlatformType() == AppCMSPresenter.PlatformType.ANDROID) {
            if (appCMSPresenter.getTemplateType() == AppCMSPresenter.TemplateType.NEWS) {
                relatedVideoButton = findViewById(R.id.playerRealtedVideo);
                parentLayout=playerView.findViewById(R.id.parentLayout);
                if (currentPLayingVideoContentData != null && currentPLayingVideoContentData.getModuleApi() != null && currentPLayingVideoContentData.getModuleApi().getContentData() != null
                        && currentPLayingVideoContentData.getModuleApi().getContentData().size() >= 1
                        && currentPLayingVideoContentData.getModuleApi().getContentData().get(0).getSeason() != null)
                    hideRelatedVideoView(true);
                else
                    hideRelatedVideoView(false);
                if (relatedVideoButton != null) {
                    relatedVideoButton.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (seasonEpisodeSelctionEvent != null) {
                                pausePlayer();
                                relatedVideoButton.setColorFilter(Color.parseColor(appCMSPresenter.getAppCtaBackgroundColor()), android.graphics.PorterDuff.Mode.SRC_IN);
                                seasonEpisodeSelctionEvent.viewClick(v, playerView.findViewById(R.id.exo_controller_container).getHeight() +
                                        playerView.findViewById(R.id.seek_bar_parent).getHeight());
                            }
                        }
                    });
                }
                if (appCMSPresenter.getPlatformType() == AppCMSPresenter.PlatformType.ANDROID) {
                    playerVolume = findViewById(R.id.playerVolume);
                }
                hidePlayerVolumeView(true);
                if (playerVolume != null) {
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
                }
            }
//            if (appCMSPresenter.getTemplateType() != AppCMSPresenter.TemplateType.NEWS) {
//                mSettingButton = createSettingButton();
//            }
            mSettingButton = createSettingButton();
            mZoomButton = playerView.findViewById(R.id.playerZoomButton);

          //  playerLiveText = findViewById(R.id.playerLiveText);
            playerLiveText.setVisibility(GONE);

            if (playerLiveText != null && appCMSPresenter.getPlatformType() != AppCMSPresenter.PlatformType.TV) {
                playerLiveText.setText(context.getString(R.string.player_text_live));
                playerLiveText.setTextColor(appCMSPresenter.getBrandPrimaryCtaColor());
            }


            if (mZoomButton != null) {
                mZoomButton.setOnClickListener(v -> {
                    if (player != null && (player.getPlaybackState() == Player.STATE_IDLE || player.getPlaybackState() == STATE_BUFFERING)) {
                        mZoomButton.setChecked(!mZoomButton.isChecked());
                        return;
                    }
                    int resizeModeToSet = playerView.getResizeMode() == AspectRatioFrameLayout.RESIZE_MODE_ZOOM ? fullscreenResizeMode : AspectRatioFrameLayout.RESIZE_MODE_ZOOM;
                    playerView.setResizeMode(resizeModeToSet);
                });
            }

            if (mSettingButton != null) {
                mSettingButton.setOnClickListener(v -> {

                    if (closedCaptionSelectorAdapter == null
                            && hlsListViewAdapter == null
                            && listViewAdapter == null) {
                        appCMSPresenter.showToast(localisedStrings.getPlayerSettingsUnavailbleText(), Toast.LENGTH_SHORT);
                    } else if (videoPlayerSettingsEvent != null) {
                        if (streamingQualitySelector != null && hlsListViewAdapter != null
                            /*&& uri != null*/
                        ) {

                        /*int streamingQualityIndex = streamingQualitySelector.getMpegResolutionIndexFromUrl(uri.toString());

                        if (hlsListViewAdapter.getItemCount() > streamingQualityIndex)
                            hlsListViewAdapter.setSelectedIndex(streamingQualityIndex);
                        hlsListViewAdapter.notifyDataSetChanged();*/

                            videoPlayerSettingsEvent.launchSetting(closedCaptionSelectorAdapter, hlsListViewAdapter, languageSelectorAdapter);
                        }
                        if (streamingQualitySelector != null && listViewAdapter != null && uri != null) {
                            int streamingQualityIndex = streamingQualitySelector.getMpegResolutionIndexFromUrl(uri.toString());

                            if (listViewAdapter.getItemCount() > streamingQualityIndex)
                                listViewAdapter.setSelectedIndex(streamingQualityIndex);
                            listViewAdapter.notifyDataSetChanged();
                            //videoPlayerSettingsEvent.launchSetting(closedCaptionSelectorAdapter, listViewAdapter);
                            videoPlayerSettingsEvent.launchSetting(closedCaptionSelectorAdapter, hlsListViewAdapter, listViewAdapter, languageSelectorAdapter);
                        }

                        if (availableClosedCaptions != null) {
                            //videoPlayerSettingsEvent.launchSetting(closedCaptionSelectorAdapter, listViewAdapter);
                            videoPlayerSettingsEvent.launchSetting(closedCaptionSelectorAdapter, hlsListViewAdapter, listViewAdapter, languageSelectorAdapter);
                        }

                       /* videoPlayerSettingsEvent.launchSetting(availableClosedCaptions,
                                (closedCaptionSelectorAdapter == null ? 0 : closedCaptionSelectorAdapter.getSelectedIndex()),
                                availableStreamingQualitiesHLS, hlsListViewAdapter == null ? 0 : hlsListViewAdapter.getSelectedIndex(),
                                availableStreamingQualities, (listViewAdapter == null ? 0 : listViewAdapter.getSelectedIndex()));*/


                    } else {
                        appCMSPresenter.showToast(localisedStrings.getSomethingWentWrongText(), Toast.LENGTH_SHORT);
                    }

                });
            }
        } else {
            if (appCMSPresenter.isNewsTemplate()) {
                relatedVideoButton = findViewById(R.id.playerRealtedVideo);
                if (appCMSPresenter.getPlatformType() == AppCMSPresenter.PlatformType.TV) {
                    AppCompatImageButton exo_play = findViewById(R.id.exo_play);
                    AppCompatImageButton exo_pause = findViewById(R.id.exo_pause);
                    AppCompatImageButton exo_rew = findViewById(R.id.exo_rew);
                    AppCompatImageButton exo_ffwd = findViewById(R.id.exo_ffwd);
                    CommonUtils.setImageColorFilter(exo_play, null, appCMSPresenter);
                    CommonUtils.setImageColorFilter(exo_pause, null, appCMSPresenter);
                    CommonUtils.setImageColorFilter(exo_rew, null, appCMSPresenter);
                    CommonUtils.setImageColorFilter(exo_ffwd, null, appCMSPresenter);
                    currentStreamingQualitySelector.setTextColor(CommonUtils.getTextColorDrawable(context, appCMSPresenter));
                }

                hideRelatedVideoView(false);
                if (relatedVideoButton != null) {
                    CommonUtils.setImageColorFilter(relatedVideoButton, null, appCMSPresenter);
                    relatedVideoButton.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (seasonEpisodeSelctionEvent != null) {
                                pausePlayer();
                                seasonEpisodeSelctionEvent.viewClick(v, playerView.findViewById(R.id.exo_controller_container).getHeight() +
                                        playerView.findViewById(R.id.seek_bar_parent).getHeight());
                            }
                        }
                    });
                }
            }
            playFromBeginning = playerView.findViewById(R.id.startPlayFromBeginning);
            if (playFromBeginning != null) {
                playFromBeginning.setVisibility(View.GONE);
                playFromBeginning.setText(appCMSPresenter.getLocalisedStrings().getStartFromBeginningText());
                playFromBeginning.setOnClickListener(v -> {
                    if (player != null) {
                        player.seekTo(0);
                    }
                });
            }

            playerActionOverlay = findViewById(R.id.player_action_overlay);
            if (playerActionOverlay != null) {
                buttonSeekAction = playerActionOverlay.findViewById(R.id.button_seekAction);
                actionOverlayVisibility(false);
            }
        }

        //*/
        try {
            if (currentStreamingQualitySelector != null) {
                StateListDrawable drawable = (StateListDrawable) currentStreamingQualitySelector.getBackground();
                DrawableContainer.DrawableContainerState dcs = (DrawableContainer.DrawableContainerState) drawable.getConstantState();
                Drawable[] drawableItems = dcs.getChildren();
                GradientDrawable gradientDrawableChecked = (GradientDrawable) drawableItems[1]; // item 1
                gradientDrawableChecked.setStroke(2, Color.parseColor(CommonUtils.getFocusBorderColor(context, appCMSPresenter)));

                if (appCMSPresenter.getAppCMSMain().getBrand().getCta().getPrimary().getBackgroundColor().equalsIgnoreCase("#ffffff")) {
                    gradientDrawableChecked.setStroke(2, Color.parseColor("#E80B0F"));
                }
            }
        } catch (Exception e) {
        }

        /*
        currentStreamingQualitySelecto`r = playerView.findViewById(R.id.streamingQualitySelector);
        if (getContext().getResources().getBoolean(R.bool.enable_stream_quality_selection)
                *//*&& (null != appCMSPresenter && appCMSPresenter.getPlatformType() == AppCMSPresenter.PlatformType.ANDROID)*//*) {
            createStreamingQualitySelector();
        } else {
            currentStreamingQualitySelector.setVisibility(View.GONE);
        }
        */

       /* videoPlayerTitle = playerView.findViewById(R.id.app_cms_video_player_title_view);
        videoPlayerTitle.setText("");
        */

        //mediaDataSourceFactory = buildDataSourceFactory(true);
        //mediaDataSourceFactory = DrmUtils.buildDataSourceFactory(appCMSPresenter);
        mediaDataSourceFactory = buildDataSourceFactory(true);

        if (appCMSPresenter.getCurrentPlayingVideo() != null) {
            offlineVideo = appCMSPresenter.getAppCMSApplication().getOfflineDRMManager().getDownloadTracker().getDowloadedVideoObject(appCMSPresenter.getCurrentPlayingVideo());
            Log.d("offline-video-check", appCMSPresenter.getCurrentPlayingVideo());
            if (offlineVideo != null && offlineVideo.state == Download.STATE_COMPLETED) {
                FileDataSource.setIsVideoDownloadDRM(true);
                isDRMEnabled = true;
                isOfflineVideoDownloaded = true;
                mediaDataSourceFactory = appCMSPresenter.getAppCMSApplication().getOfflineDRMManager().buildDataSourceFactory();
            }
        }

        timeBar = playerView.findViewById(R.id.exo_progress);
        timeBar.setAdMarkerColor(Color.YELLOW);
        videoTrackSelectionFactory =
                new AdaptiveTrackSelection.Factory();

        DefaultTrackSelector.ParametersBuilder builder = new DefaultTrackSelector.ParametersBuilder(getContext());
        builder.setTunnelingAudioSessionId(C.generateAudioSessionIdV21(getContext()));

        trackSelectorParameters = builder.build();
        trackSelector =
                new DefaultTrackSelector(getContext(), videoTrackSelectionFactory);
        trackSelector.setParameters(trackSelectorParameters);


        if (!TextUtils.isEmpty(appPreference.getVideoStreamingQuality()) && appPreference.getVideoStreamingQuality().endsWith("p")) {
            int res = Integer.parseInt(appPreference.getVideoStreamingQuality().replace("p", ""));
            //          trackSelector.setParameters(trackSelector.getParameters().buildUpon().setMaxVideoSize(Integer.MAX_VALUE, res).build());
        }

        if (player != null) {
            player.release();
        }

        drmSessionManager = DrmSessionManager.getDummyDrmSessionManager();

        if (isDRMEnabled) {
            try {
                drmSessionManager = buildDrmSessionManager(getContext(), getLicenseUrl(), getLicenseTokenDRM());
            } catch (Exception e) {
            }
        }


        DefaultRenderersFactory defaultRenderersFactory = new DefaultRenderersFactory(getContext())
                .setExtensionRendererMode(DefaultRenderersFactory.EXTENSION_RENDERER_MODE_ON);

        RenderersFactory renderersFactory = buildRenderersFactory(false);
        TrackSelection.Factory trackSelectionFactory = new AdaptiveTrackSelection.Factory();
//        String abrAlgorithm = intent.getStringExtra(ABR_ALGORITHM_EXTRA);
//        if (abrAlgorithm == null || ABR_ALGORITHM_DEFAULT.equals(abrAlgorithm)) {
//            trackSelectionFactory = ;
//        } else if (ABR_ALGORITHM_RANDOM.equals(abrAlgorithm)) {
//            trackSelectionFactory = new RandomTrackSelection.Factory();
//        } else {
//
//        }

        trackSelector = new DefaultTrackSelector(/* context= */ getContext(), trackSelectionFactory);
        trackSelector.setParameters(
                trackSelector.buildUponParameters());
        player = new SimpleExoPlayer.Builder(getContext(), defaultRenderersFactory)
                .setTrackSelector(trackSelector)
                .setBandwidthMeter(BANDWIDTH_METER)
                .build();

        player.addListener(this);
        AudioAttributes audioAttributes = new AudioAttributes.Builder()
                .setUsage(C.USAGE_MEDIA)
                .setContentType(C.CONTENT_TYPE_MOVIE)
                .build();
        player.setAudioAttributes(audioAttributes, /* handleAudioFocus= */ true);
        // player.setVideoDebugListener(this);
        playerView.setPlayer(player);

        playerView.setPlaybackPreparer(this);
        playerView.setDoubleTouchListner(event -> {
            if (playerView != null &&
                    playerView.getController() != null &&
                    playerView.getPlayer() != null && !isLiveStreaming()) {
                int playerWidth = playerView.getWidth();
                if (playerWidth > 0) {
                    playerWidth = playerWidth / 2;
                }
                int touchX = (int) event.getX();
                if (findViewById(R.id.exo_pause).getVisibility() == VISIBLE) {
                    if (touchX > playerWidth) {
                        playerView.getController().fastForward(playerView.getPlayer());
                    } else {
                        playerView.getController().rewind(playerView.getPlayer());
                    }
                }
            }
        });
        playerView.setControllerVisibilityListener(visibility -> {
            if (onPlayerControlsStateChanged != null) {
                onPlayerControlsStateChanged.call(visibility);
            }
            if (appCMSPresenter.isNewsTemplate()) {
                if (visibility == View.VISIBLE)
                    parentLayout.setBackgroundColor(Color.parseColor("#73000000"));
                else
                    parentLayout.setBackgroundColor(ContextCompat.getColor(context, android.R.color.transparent));
            }
            if (!isLiveStreaming()) {
                if (visibility == View.VISIBLE) {
                    offsetSubtitleView();
                } else {
                    resetSubtitleView();
                }
            }
        });
        player.addVideoListener(this);

        if (context != null) {
            audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        }

        setFillBasedOnOrientation();

        fullscreenResizeMode = BaseView.isTablet(context)
                ? AspectRatioFrameLayout.RESIZE_MODE_FIT
                : AspectRatioFrameLayout.RESIZE_MODE_FIXED_HEIGHT;

        AppCompatImageButton exo_pause = findViewById(R.id.exo_pause);
        if (exo_pause != null) {
            exo_pause.setOnKeyListener((v, keyCode, event) -> {
                if (event.getAction() == KeyEvent.ACTION_DOWN) {
                    switch (event.getKeyCode()) {
                        case KeyEvent.KEYCODE_DPAD_CENTER:
                            System.out.println("playWhenReady: " + shouldPlayWhenReady());
                            setManualPause(true);
                            break;
                    }
                }
                return false;
            });
        }
    }

    public RenderersFactory buildRenderersFactory(boolean preferExtensionRenderer) {
        @DefaultRenderersFactory.ExtensionRendererMode
        int extensionRendererMode = useExtensionRenderers()
                ? (preferExtensionRenderer
                ? DefaultRenderersFactory.EXTENSION_RENDERER_MODE_PREFER
                : DefaultRenderersFactory.EXTENSION_RENDERER_MODE_ON)
                : DefaultRenderersFactory.EXTENSION_RENDERER_MODE_OFF;
        return new DefaultRenderersFactory(/* context= */ getContext()).setExtensionRendererMode(extensionRendererMode);
    }

    /**
     * Returns whether extension renderers should be used.
     */
    public boolean useExtensionRenderers() {
        return "withExtensions".equals(BuildConfig.FLAVOR);
    }

    private void offsetSubtitleView() {
        if (playerView.getSubtitleView() != null) {
            if (appCMSPresenter.getPlatformType() == AppCMSPresenter.PlatformType.TV) {
                playerView.getSubtitleView().animate().translationY(-100).setDuration(100);
            }
        }
    }

    public void disableRightFocus() {
        if (ccToggleButton.getVisibility() == VISIBLE) {
            ccToggleButton.setNextFocusRightId(ccToggleButton.getId());
            ccToggleButton.setNextFocusUpId(ccToggleButton.getId());
            findViewById(R.id.exo_play).setNextFocusRightId(ccToggleButton.getId());
            findViewById(R.id.exo_pause).setNextFocusRightId(ccToggleButton.getId());
        } else {
            findViewById(R.id.exo_play).setNextFocusRightId(findViewById(R.id.exo_play).getId());
            findViewById(R.id.exo_pause).setNextFocusRightId(findViewById(R.id.exo_pause).getId());
            toggleCCSelectorVisibility(false);
        }
    }

    private void resetSubtitleView() {
        if (playerView.getSubtitleView() != null) {
            playerView.getSubtitleView().animate().translationY(0).setDuration(100);
        }
    }

    private void setSubtitleTextSize(float size) {
        if (playerView.getSubtitleView() != null) {
            playerView.getSubtitleView().setFixedTextSize(TypedValue.COMPLEX_UNIT_SP, size);
        }
    }

    public void applyTimeBarColor(int timeBarColor) {
        if (null != timeBar) {
            timeBar.applyScrubberColor(timeBarColor);
            timeBar.applyUnplayedColor(timeBarColor);
            timeBar.applyBufferedColor(timeBarColor);
            timeBar.applyAdMarkerColor(timeBarColor);
            timeBar.applyPlayedAdMarkerColor(timeBarColor);
        }
    }

    public void setVideoTitle(String title, int textColor) {
        if (videoPlayerTitle != null) {
            videoPlayerTitle.setText(title);
            videoPlayerTitle.setTextColor(textColor);

        }
    }


    private void setSelectedStreamingQualityIndex() {
        if (streamingQualitySelector != null && listViewAdapter != null) {
            int currentIndex = -1;
            int updatedIndex = -1;
            try {
                currentIndex = listViewAdapter.getSelectedIndex();
                updatedIndex = streamingQualitySelector.getMpegResolutionIndexFromUrl(uri.toString());
                if (updatedIndex != -1) {
                    listViewAdapter.setSelectedIndex(updatedIndex);
                }
            } catch (Exception e) {
                //listViewAdapter.setSelectedIndex(0);
                listViewAdapter.setSelectedIndex(streamingQualitySelector.getAvailableStreamingQualities().size() - 1);
            }
            if (updatedIndex != -1 && currentIndex != -1 && updatedIndex != currentIndex) {
                listViewAdapter.notifyDataSetChanged();
            }
        }
    }

    /**
     * This method gets the available CCs, parse them and put them in list and show it to user when
     * CC button on the player is tapped.
     */
    private void createClosedCaptioningSelector() {

        /*Simply return if there are no tracks to be selected from*/
        MappingTrackSelector.MappedTrackInfo mappedTrackInfo = trackSelector.getCurrentMappedTrackInfo();
        setTracks(mappedTrackInfo);
        if (mappedTrackInfo == null) {
            return;
        }

        /*get the text (subtitle) renderer index*/


        /**a mock entry for "Off" option*/
        ClosedCaptions captions = new ClosedCaptions();
        captions.setLanguage(localisedStrings.getClosedCaptionOffText());
        /**fetch all the available SRTs*/
        availableClosedCaptions = closedCaptionSelector.getAvailableClosedCaptions();

        if (availableClosedCaptions.size() <= 0 && mTextRendererIndex > 0) {
            trackGroups = trackSelector.getCurrentMappedTrackInfo().getTrackGroups(mTextRendererIndex);
            for (int groupIndex = 0; groupIndex < trackGroups.length; groupIndex++) {
                TrackGroup group = trackGroups.get(groupIndex);
                for (int trackIndex = 0; trackIndex < group.length; trackIndex++) {
                    Format format = group.getFormat(trackIndex);
                    if (format.metadata != null) {
                        if (format.language != null && !TextUtils.isEmpty(format.language)) {
                            ClosedCaptions captionsHls = new ClosedCaptions();
                            captionsHls.setLanguage(format.language);
                            availableClosedCaptions.add(captionsHls);
                            closedCaptionHlsEmbeded = true;
                            closeCaptionPreferenceCheck(format.language, groupIndex);
                        } else if (format.language == null && trackGroups.length == 1) {
                            ClosedCaptions captionsHls = new ClosedCaptions();
                            captionsHls.setLanguage("CC");
                            availableClosedCaptions.add(captionsHls);
                            closedCaptionHlsEmbeded = true;
                            closeCaptionPreferenceCheck("CC", groupIndex);
                            break;
                        }
                    }
                }
            }
        }

        //Collections.sort(availableClosedCaptions);
        /**add the mock entry at the 0th index*/
        availableClosedCaptions.add(0, captions);

        //int selectedTrack = getSelectedCCTrack();
        for (int i = 0; i < availableClosedCaptions.size(); i++) {

            if (availableClosedCaptions.get(i).getLanguage().equalsIgnoreCase(appPreference.getPreferredSubtitleLanguage())
                    && !appPreference.getPreferredSubtitleLanguage().equalsIgnoreCase("off")) {
                closeCaptionPreferenceCheck(availableClosedCaptions.get(i).getLanguage(), i);
                selectedSubtitleIndex = i;
                break;
            }
        }
        // availableClosedCaptions.get(selectedTrack).setIsSelected(true);

        /**create adapter*/
        closedCaptionSelectorAdapter = new ClosedCaptionSelectorAdapter(getContext(),
                appCMSPresenter,
                availableClosedCaptions);
        closedCaptionSelectorAdapter.setSelectedIndex(selectedSubtitleIndex);
        closedCaptionSelectorRecyclerView = new RecyclerView(getContext());
        closedCaptionSelectorRecyclerView.setAdapter(closedCaptionSelectorAdapter);
        closedCaptionSelectorRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(),
                LinearLayoutManager.VERTICAL,
                false));

        closedCaptionSizeSelectorAdapter = new ClosedCaptionSizeSelectorAdapter(
                getContext(),
                appCMSPresenter,
                createCCTextSizeList());
        closedCaptionSizeSelectorRecyclerView = new RecyclerView(getContext());
        closedCaptionSizeSelectorRecyclerView.setAdapter(closedCaptionSizeSelectorAdapter);
        closedCaptionSizeSelectorRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(),
                LinearLayoutManager.VERTICAL,
                false));

        LinearLayout linearLayout = new LinearLayout(getContext());
        linearLayout.setLayoutTransition(new LayoutTransition());
        linearLayout.setOrientation(LinearLayout.HORIZONTAL);
        linearLayout.addView(closedCaptionSelectorRecyclerView);
        linearLayout.addView(closedCaptionSizeSelectorRecyclerView);
        linearLayout.setGravity(Gravity.CENTER);

        if (appCMSPresenter.getPlatformType() == AppCMSPresenter.PlatformType.TV) {
            AlertDialog.Builder builder;
            if ((appCMSPresenter.getPlatformType() == AppCMSPresenter.PlatformType.TV) && Utils.isFireTVDevice(getContext())) {
                builder = new AlertDialog.Builder(getContext(), android.R.style.Theme_Light_NoTitleBar_Fullscreen);
            } else {
                builder = new AlertDialog.Builder(getContext());
            }

            builder.setView(linearLayout);
            final Dialog closedCaptionSelectorDialog = builder.create();

            if (closedCaptionSelectorDialog.getWindow() != null) {
                if ((appCMSPresenter.getPlatformType() == AppCMSPresenter.PlatformType.TV) && Utils.isFireTVDevice(getContext())) {
                    closedCaptionSelectorDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#03000000")));
                    closedCaptionSelectorDialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
                } else {
                    closedCaptionSelectorDialog.getWindow().setBackgroundDrawable(new ColorDrawable(appCMSPresenter.getGeneralBackgroundColor()));
                }
            }

            /**Click handler of the dialog list items*/
            closedCaptionSelectorAdapter.setItemClickListener(item -> {
                int position = closedCaptionSelectorAdapter.getDownloadQualityPosition();
                setClosedCaption(position);
                toggleClosedCaptionSizeSelectorVisibility(position != 0);
//                closedCaptionSelectorDialog.dismiss();
            });
            /** Click handler of the CC button on the player, which just opens the dialog*/
            ccToggleButton.setOnClickListener(v -> {

                //if (appCMSPresenter.getPlatformType() == AppCMSPresenter.PlatformType.TV) {
                closedCaptionSelectorDialog.show();
                closedCaptionSelectorAdapter.notifyDataSetChanged();
                closedCaptionSizeSelectorAdapter.setPreSelectedFontPosition();
                closedCaptionSizeSelectorAdapter.notifyDataSetChanged();
                closedCaptionSelectorRecyclerView.scrollToPosition(0);
                toggleClosedCaptionSizeSelectorVisibility(ccToggleButton.isSelected());
                // }
            });
            closedCaptionSizeSelectorAdapter.setItemClickListener(item -> {
                setSubtitleTextSize(((CCFontSize) item).getSize());
                appPreference.setPreferredSubtitleTextSize(((CCFontSize) item).getSize());
                closedCaptionSizeSelectorAdapter.setPreSelectedFontPosition();
//                closedCaptionSelectorDialog.dismiss();
            });

            toggleCCSelectorVisibility(availableClosedCaptions.size() > 1);

        } else {
            boolean visibility = ((availableClosedCaptions != null && availableClosedCaptions.size() > 1)
                    || (availableStreamingQualitiesHLS != null && availableStreamingQualitiesHLS.size() > 1)
                    || (availableStreamingQualities != null && availableStreamingQualities.size() > 1));
            settingsButtonVisibility(visibility);
        }
        closedCaptionSelectorCreated = true;
    }

    private void toggleClosedCaptionSizeSelectorVisibility(boolean visibility) {
        if (closedCaptionSizeSelectorRecyclerView != null) {
            closedCaptionSizeSelectorRecyclerView.setVisibility(visibility ? VISIBLE : GONE);
        }
    }

    public void initCCAdapter() {
        createClosedCaptioningSelector();
        if (shouldShowSubtitle) {
            setClosedCaption(selectedSubtitleIndex);
            /* +1 to offset the "off" selection added to the dialog list*/
            closedCaptionSelectorAdapter.setSelectedIndex(selectedSubtitleIndex);
            if (appPreference.getPreferredSubtitleLanguage() != null
                    && !TextUtils.isEmpty(appPreference.getPreferredSubtitleLanguage())
                    && !appPreference.getPreferredSubtitleLanguage().equalsIgnoreCase("off")) {
                setSubtitleViewVisibility(true);
            }
        }
    }

    public void closeCaptionPreferenceCheck(String language, int i) {
        /**check if user has a preferred subtitle language, which he/she might have chosen in the
         past, method returns null if there is no preference*/
        String preferredSubtitleLanguage = appPreference.getPreferredSubtitleLanguage();
        if (preferredSubtitleLanguage != null) {
            if (preferredSubtitleLanguage.equalsIgnoreCase(language) && !preferredSubtitleLanguage.equalsIgnoreCase("off")) {
                selectedSubtitleIndex = i;
                selectedSubtitleLanguageAvailable = true;

                /*this is used in the onPlayerStateChanged*/
                shouldShowSubtitle = true;
                appPreference.setPreferredSubtitleLanguage(language);
            } else if (preferredSubtitleLanguage.equalsIgnoreCase("cc") && (language.equalsIgnoreCase("english") ||
                    language.equalsIgnoreCase("en"))) {
                selectedSubtitleIndex = i;
                selectedSubtitleLanguageAvailable = true;

                /*this is used in the onPlayerStateChanged*/
                shouldShowSubtitle = true;
                appPreference.setPreferredSubtitleLanguage(language);
            } else if (preferredSubtitleLanguage.equalsIgnoreCase("off")) {
                shouldShowSubtitle = false;
                setSubtitleViewVisibility(false);
            }
        } else {
            selectedSubtitleLanguageAvailable = false;
            setCCToggleButtonSelection(false);
            setSubtitleViewVisibility(false);
        }
    }

    /**
     * Returns the selected CC group index
     *
     * @return selected Closed Caption track
     */
    private int getSelectedCCTrack() {
//        getSelectedVideoTrack();
        int selectedTrack = 0;
        if (mTextRendererIndex > -1) {
            TrackGroupArray trackGroups = trackSelector.getCurrentMappedTrackInfo().getTrackGroups(mTextRendererIndex);
            for (int groupIndex = 0; groupIndex < trackGroups.length; groupIndex++) {
                TrackGroup trackGroup = trackGroups.get(groupIndex);
                for (int trackIndex = 0; trackIndex < trackGroup.length; trackIndex++) {
                    DefaultTrackSelector.SelectionOverride selectionOverride = trackSelector.getSelectionOverride(mTextRendererIndex, trackSelector.getCurrentMappedTrackInfo().getTrackGroups(mTextRendererIndex));
                    if (selectionOverride != null && selectionOverride.groupIndex == groupIndex && selectionOverride.containsTrack(trackIndex)) {
//                    Toast.makeText(getContext(), "Group Index: " +groupIndex +", Track Index: " + trackIndex, Toast.LENGTH_SHORT).show();

                        /* +1 to offset the mock "off" entry into the list*/
                        selectedTrack = groupIndex + 1;
                        break;
                    }
                }
            }
        }
        return selectedTrack;
    }

    /**
     * overrides the CC track selection with the group id passed as a paramater
     *
     * @param groupIndex index of the group you wanna select
     */
    private void setSelectedCCTrack(int groupIndex) {
        TrackGroupArray trackGroups1 = trackSelector.getCurrentMappedTrackInfo().getTrackGroups(mTextRendererIndex);
        DefaultTrackSelector.SelectionOverride override = new DefaultTrackSelector.SelectionOverride(
                groupIndex, 0);
        trackSelector.setSelectionOverride(mTextRendererIndex, trackGroups1, override);
    }

    private int getURLContetType(Uri uri, String extetion) {
        if (uri.getPath().endsWith("/vod"))
            extetion = "m3u8";
        if (uri.toString().contains("anvato.net")) {
            extetion = ".m3u8";
        }
        return Util.inferContentType(uri, extetion);
    }

    protected void createStreamingQualitySelector() {

        if (!appCMSPresenter.isVideoDownloaded(streamingQualitySelector.getFilmId())) {
            if (streamingQualitySelector != null && appCMSPresenter != null && !TextUtils.isEmpty(streamingQualitySelector.getVideoUrl())
                    && Uri.parse(streamingQualitySelector.getVideoUrl()) != null
                    && getURLContetType(Uri.parse(streamingQualitySelector.getVideoUrl()), "") == C.TYPE_OTHER) {
                availableStreamingQualities = streamingQualitySelector.getAvailableStreamingQualities();
                if (availableStreamingQualities != null && 1 < availableStreamingQualities.size()) {

                    /**
                     * the following is done to only have distinct values in the HLS track list. We are getting
                     multiple tracks for same resolution with different bitrate.*/
//                    Set<String> set = new TreeSet<>((o1, o2) -> {
//                        try {
//                            int i1 = Integer.valueOf(o1.replace("p", ""));
//                            int i2 = Integer.valueOf(o2.replace("p", ""));
//                            if (i1 < i2) {
//                                return -1;
//                            } else if (i1 == i2) {
//                                return 0;
//                            } else {
//                                return 1;
//                            }
//                        }catch (NumberFormatException ex){
//                            return 1;
//                        }catch (Exception ex){
//                            return 1;
//                        }
//
//                    });
//                    set.addAll(availableStreamingQualities);
//                    availableStreamingQualities.clear();
//                    availableStreamingQualities.addAll(set);

                    qualitySelectorRecyclerView = new RecyclerView(getContext());
                    listViewAdapter = new StreamingQualitySelectorAdapter(getContext(),
                            appCMSPresenter,
                            availableStreamingQualities);

                    qualitySelectorRecyclerView.setAdapter(listViewAdapter);
                    qualitySelectorRecyclerView.setBackgroundColor(Color.TRANSPARENT/*appCMSPresenter.getGeneralBackgroundColor()*/);
                    qualitySelectorRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(),
                            LinearLayoutManager.VERTICAL,
                            false));

                    setSelectedStreamingQualityIndex();
                    listViewAdapter.setSelectedIndex(0);
                    listViewAdapter.setPreSelectedQualityPosition();
                    if (appPreference.getVideoStreamingQuality() == null) {
                        String defaultVideoResolution = getContext().getString(R.string.default_video_resolution);
                        int res = Integer.parseInt(defaultVideoResolution.replace("p", ""));

                        /*For MP4s, by default, the highest resolution is rendered, to honor the setting we
                         * are telling the player that the max height can only be "res"*/
                        trackSelector.setParameters(trackSelector.getParameters().buildUpon().setMaxVideoSize(Integer.MAX_VALUE, res).build());
                    } else {
                        setStreamingQuality(listViewAdapter.getSelectedIndex(), "");
                    }

                    if (appCMSPresenter.getPlatformType() == AppCMSPresenter.PlatformType.TV) {
                        //trackSelector.setParameters(trackSelector.getParameters().buildUpon().setMaxVideoSize(Integer.MAX_VALUE, 720).build());

                        AlertDialog.Builder builder;
                        if ((appCMSPresenter.getPlatformType() == AppCMSPresenter.PlatformType.TV) && Utils.isFireTVDevice(getContext())) {
                            builder = new AlertDialog.Builder(getContext(), R.style.customDialog);
                        } else {
                            builder = new AlertDialog.Builder(getContext());
                        }

                        if (qualitySelectorRecyclerView.getParent() != null && qualitySelectorRecyclerView.getParent() instanceof ViewGroup) {
                            ((ViewGroup) qualitySelectorRecyclerView.getParent()).removeView(qualitySelectorRecyclerView);
                        }
                        builder.setView(qualitySelectorRecyclerView);
                        final Dialog streamingQualitySelectorDialog = builder.create();
                        if (streamingQualitySelectorDialog.getWindow() != null) {
                            if ((appCMSPresenter.getPlatformType() == AppCMSPresenter.PlatformType.TV) && Utils.isFireTVDevice(getContext())) {
                                streamingQualitySelectorDialog.getWindow().setBackgroundDrawable(new ColorDrawable(appCMSPresenter.getGeneralBackgroundColor()));
                                streamingQualitySelectorDialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
                            } else {
                                streamingQualitySelectorDialog.getWindow().setBackgroundDrawable(new ColorDrawable(appCMSPresenter.getGeneralBackgroundColor()));
                            }
                        }
                        if (appCMSPresenter.getPlatformType() != AppCMSPresenter.PlatformType.ANDROID) {
                            currentStreamingQualitySelector.setOnClickListener(v -> {
                                streamingQualitySelectorDialog.show();
                                listViewAdapter.notifyDataSetChanged();
                            });
                        }

                        listViewAdapter.setItemClickListener(v -> {
                            try {
                                VideoPlayerView.this.setStreamingQuality(listViewAdapter.getDownloadQualityPosition(), v);
                                currentStreamingQualitySelector.setText(availableStreamingQualities.get(listViewAdapter.getDownloadQualityPosition()));
                                listViewAdapter.setSelectedIndex(listViewAdapter.getDownloadQualityPosition());
                                streamingQualitySelectorDialog.dismiss();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        });
                        currentStreamingQualitySelector.setText(availableStreamingQualities.get(listViewAdapter.getSelectedIndex()));
                        currentStreamingQualitySelector.setVisibility(VISIBLE);
                    }// end of  if (appCMSPresenter.getPlatformType() == AppCMSPresenter.PlatformType.TV) {

                    settingsButtonVisibility(true);
                } else {
                    currentStreamingQualitySelector.setVisibility(GONE);
                }
            }
        } else {
            //video coming from downloaded
            if (appCMSPresenter.isUserLoggedIn() && currentStreamingQualitySelector != null) {
                currentStreamingQualitySelector.setVisibility(GONE);
            }
        }
        streamingQualitySelectorCreated = true;
    }

    protected void createLanguageSelector() {

        MappingTrackSelector.MappedTrackInfo mappedTrackInfo = trackSelector.getCurrentMappedTrackInfo();
        if (mappedTrackInfo == null) {
            return;
        }
        setTracks(mappedTrackInfo);
        if (getAudioTracks() != null && languageList.size() > 1) {
            lanaguageSelectorRecyclerView = new RecyclerView(getContext());
            languageSelectorAdapter = new LanguageSelectorAdapter(getContext(),
                    appCMSPresenter,
                    languageList);

            lanaguageSelectorRecyclerView.setAdapter(languageSelectorAdapter);
            lanaguageSelectorRecyclerView.setBackgroundColor(Color.TRANSPARENT/*appCMSPresenter.getGeneralBackgroundColor()*/);
            lanaguageSelectorRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(),
                    LinearLayoutManager.VERTICAL,
                    false));

            String prefLanguage = appPreference.getPreferredAudioLanguage();
            int preferedIndex = getPreferAudioIndex(prefLanguage);


            setAudioLanguage(preferedIndex);
            languageSelectorAdapter.setSelectedIndex(preferedIndex);

            audioSelectorCreated = true;
            //setSelectedStreamingQualityIndex();
            //languageSelectorAdapter.setSelectedIndex(0);
        } else {
            audioSelectorShouldCreated = false;
        }
    }

    public int getPreferAudioIndex(String prefLanguage) {
        int prefIndex = 0;
        if (prefLanguage == null || TextUtils.isEmpty(prefLanguage)) {
            return prefIndex;
        } else if (languageList.contains(prefLanguage)) {
            for (int index = 0; index < languageList.size(); index++) {
                if (prefLanguage.equalsIgnoreCase(languageList.get(index))) {
                    prefIndex = index;
                    break;
                }
            }
        } else if (!isDefaultAudioAvailable) {
            return prefIndex;
        }
        return prefIndex;
    }

    /**
     * Used to extract the different tracks available in an HLS stream.
     * <p>
     * {@link DefaultTrackSelector#getCurrentMappedTrackInfo} returns {@link MappingTrackSelector.MappedTrackInfo} object
     * </br>
     * <p>
     * {@link MappingTrackSelector.MappedTrackInfo#getTrackGroups(int)} is called with 0 as argument for video tracks, which returns {@link TrackGroupArray}.
     * </br></br></p><p>
     * {@link TrackGroupArray} is then iterated on index which return {@link TrackGroup} by calling the {@link TrackGroupArray#get(int)}
     * </br></br></p>
     * {@link TrackGroup#getFormat(int)} is called and {@link Format} is used to get the track index and the {@link Format#height} is used to calculate the resolution of the track.
     */
    protected void createStreamingQualitySelectorForHLS() {
        try {
            if (player == null) {
                return;
            }

            MappingTrackSelector.MappedTrackInfo mappedTrackInfo = trackSelector.getCurrentMappedTrackInfo();
            if (mappedTrackInfo == null) {
                return;
            }

            if (TextUtils.isEmpty(streamingQualitySelector.getVideoUrl()) || Uri.parse(streamingQualitySelector.getVideoUrl()) == null) {
                return;
            }

            if (streamingQualitySelector != null && streamingQualitySelector.getVideoUrl() != null
                    && getURLContetType(Uri.parse(streamingQualitySelector.getVideoUrl()), "") == C.TYPE_OTHER) {
                return;
            }
            setTracks(mappedTrackInfo);

            if (streamingQualitySelector != null && appCMSPresenter != null) {
                showStreamingQualitySelector();
                trackGroups = trackSelector.getCurrentMappedTrackInfo().getTrackGroups(mVideoRendererIndex);
                availableStreamingQualitiesHLS = new ArrayList<>();
                for (int groupIndex = 0; groupIndex < trackGroups.length; groupIndex++) {
                    TrackGroup group = trackGroups.get(groupIndex);
                    for (int trackIndex = 0; trackIndex < group.length; trackIndex++) {
                        Format format = group.getFormat(trackIndex);
                        if (format.height != Format.NO_VALUE) {
                            availableStreamingQualitiesHLS.add(new HLSStreamingQuality(trackIndex,
                                    format.height == Format.NO_VALUE ? "" : format.height + "p"));
                        } else {
                            availableStreamingQualitiesHLS.add(new HLSStreamingQuality(trackIndex,
                                    buildBitrateString(format)));
                        }
                    }
                }

                /** the following is done to only have distinct values in the HLS track list. We are getting
                 multiple tracks for same resolution with different bitrate.*/
                Set<HLSStreamingQuality> set = new TreeSet<>((o1, o2) -> {
                    try {
                        int i1 = Integer.valueOf(o1.getValue().replace("p", ""));
                        int i2 = Integer.valueOf(o2.getValue().replace("p", ""));
                        if (i1 < i2) {
                            return -1;
                        } else if (i1 == i2) {
                            return 0;
                        } else {
                            return 1;
                        }
                    } catch (NumberFormatException ex) {
                        return 1;
                    } catch (Exception ex) {
                        return 1;
                    }

                });
                set.addAll(availableStreamingQualitiesHLS);
                availableStreamingQualitiesHLS.clear();
                String autoText = localisedStrings.getAutoHlsText();
                if ((currentPLayingVideoContentData != null && currentPLayingVideoContentData.getSubscriptionPlans() != null && contentTypeChecker.isContentTVOD(currentPLayingVideoContentData.getSubscriptionPlans()))) {
                    if (currentPLayingVideoContentData.getSubscriptionPlans().get(0).getFeatureSetting().isHdStreaming())
                        availableStreamingQualitiesHLS.add(0, new HLSStreamingQuality(0, autoText));
                } else if (appPreference.isUserAllowedHDStreaming())
                    availableStreamingQualitiesHLS.add(0, new HLSStreamingQuality(0, autoText));
                availableStreamingQualitiesHLS.addAll(set);

                if (availableStreamingQualitiesHLS.size() > 1) {
                    qualitySelectorRecyclerView = new RecyclerView(getContext());
                    boolean isContentTvod = false;
                    boolean isTvodHdStreaming = false;
                    if (currentPLayingVideoContentData != null && currentPLayingVideoContentData.getSubscriptionPlans() != null && contentTypeChecker.isContentTVOD(currentPLayingVideoContentData.getSubscriptionPlans())) {
                        isContentTvod = true;
                        isTvodHdStreaming = currentPLayingVideoContentData.getSubscriptionPlans().get(0).getFeatureSetting().isHdStreaming();
                    }
                    hlsListViewAdapter = new HLSStreamingQualitySelectorAdapter(getContext(),
                            appCMSPresenter,
                            availableStreamingQualitiesHLS, isContentTvod, isTvodHdStreaming);

                    qualitySelectorRecyclerView.setAdapter(hlsListViewAdapter);
                    if (appCMSPresenter.getPlatformType() == AppCMSPresenter.PlatformType.TV) {
                        qualitySelectorRecyclerView.setBackgroundColor(Color.TRANSPARENT);
                        AlertDialog.Builder builder;

                        if ((appCMSPresenter.getPlatformType() == AppCMSPresenter.PlatformType.TV) && Utils.isFireTVDevice(getContext())) {
                            builder = new AlertDialog.Builder(getContext(), R.style.customDialog);
                        } else {
                            builder = new AlertDialog.Builder(getContext());
                        }

                        if (qualitySelectorRecyclerView.getParent() != null && qualitySelectorRecyclerView.getParent() instanceof ViewGroup) {
                            ((ViewGroup) qualitySelectorRecyclerView.getParent()).removeView(qualitySelectorRecyclerView);
                        }
                        builder.setView(qualitySelectorRecyclerView);
                        final Dialog dialog = builder.create();
                        if (dialog.getWindow() != null) {
                            if ((appCMSPresenter.getPlatformType() == AppCMSPresenter.PlatformType.TV) && Utils.isFireTVDevice(getContext())) {
                                //dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#03000000")));
                                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(appCMSPresenter.getGeneralBackgroundColor()));
                                dialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
                            } else {
                                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(appCMSPresenter.getGeneralBackgroundColor()));
                            }
                        }
                        if (appCMSPresenter.getPlatformType() != AppCMSPresenter.PlatformType.ANDROID) {
                            currentStreamingQualitySelector.setOnClickListener(v -> {
                                /** Click Handler*/
                                dialog.show();
                                hlsListViewAdapter.notifyDataSetChanged();
                            });
                        }
                        hlsListViewAdapter.setItemClickListener(v -> {

                            try {
                                if (v instanceof HLSStreamingQuality) {
                                    int selectedIndex = hlsListViewAdapter.getDownloadQualityPosition();
                                    if (selectedIndex == 0) {
                                        trackSelector.clearSelectionOverrides(mVideoRendererIndex);
                                    } else {
                                        int[] tracks = new int[1];
                                        tracks[0] = ((HLSStreamingQuality) v).getIndex();
                                        DefaultTrackSelector.SelectionOverride override = new DefaultTrackSelector.SelectionOverride(
                                                0, tracks);

                                        DefaultTrackSelector.ParametersBuilder builderParams =
                                                trackSelector.getParameters()
                                                        .buildUpon()
                                                        .clearSelectionOverrides(mVideoRendererIndex)
                                                        .setRendererDisabled(mVideoRendererIndex, false);
                                        builderParams.setSelectionOverride(mVideoRendererIndex, trackGroups, override);

                                        trackSelector.setParameters(builderParams.build());

                                        // trackSelector.setSelectionOverride(mVideoRendererIndex, trackGroups, override);
                                    }
                                    setStreamingQuality(selectedIndex, v);
                                    currentStreamingQualitySelector.setText(availableStreamingQualitiesHLS.get(selectedIndex).getValue());
                                    appCMSPresenter.sendPlayerBitrateEvent(availableStreamingQualitiesHLS.get(selectedIndex).getValue());
                                    hlsListViewAdapter.setSelectedIndex(selectedIndex);
                                }
                                dialog.hide();
                            } catch (Exception e) {

                            }
                        });
                        if (currentStreamingQualitySelector != null) {
                            if (currentPLayingVideoContentData != null && currentPLayingVideoContentData.getSubscriptionPlans() != null && contentTypeChecker.isContentTVOD(currentPLayingVideoContentData.getSubscriptionPlans())) {
                                if (currentPLayingVideoContentData.getSubscriptionPlans().get(0).getFeatureSetting().isHdStreaming())
                                    currentStreamingQualitySelector.setText(localisedStrings.getAutoHlsText());
                                else
                                    currentStreamingQualitySelector.setText(availableStreamingQualitiesHLS.get(hlsListViewAdapter.getSelectedIndex()).getValue());
                            } else if (appPreference.isUserAllowedHDStreaming()) {
                                currentStreamingQualitySelector.setText(localisedStrings.getAutoHlsText());
                            } else {
                                currentStreamingQualitySelector.setText(availableStreamingQualitiesHLS.get(hlsListViewAdapter.getSelectedIndex()).getValue());
                            }
                        }
                    } else {
                        qualitySelectorRecyclerView.setBackgroundColor(appCMSPresenter.getGeneralBackgroundColor());
                        settingsButtonVisibility(true);
                    }
                    qualitySelectorRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(),
                            LinearLayoutManager.VERTICAL,
                            false));

                    setSelectedStreamingQualityIndex();
                    if (appCMSPresenter.getPlatformType() == AppCMSPresenter.PlatformType.ANDROID) {
                        hlsListViewAdapter.setPreSelectedQualityPosition();
                        int selectedIndex = hlsListViewAdapter.getDownloadQualityPosition();
                        setStreamingQuality(selectedIndex, availableStreamingQualitiesHLS.get(selectedIndex));

                    }


                } else {
                    currentStreamingQualitySelector.setVisibility(GONE);
                }
            } else {
                currentStreamingQualitySelector.setVisibility(GONE);
            }
            streamingQualitySelectorCreated = true;

        } catch (Exception e) {
            Log.e(TAG, ":" + e);
        }
    }

    protected void createAudioSelector() {
        if (player == null) {
            return;
        }
        MappingTrackSelector.MappedTrackInfo mappedTrackInfo = trackSelector.getCurrentMappedTrackInfo();
        if (mappedTrackInfo == null) {
            return;
        }
        if (streamingQualitySelector.getVideoUrl() != null && (getURLContetType(Uri.parse(streamingQualitySelector.getVideoUrl()), "") == C.TYPE_OTHER)) {
            return;
        }
        for (int i = 0; i < mappedTrackInfo.getRendererCount(); i++) {
            TrackGroupArray trackGroups = mappedTrackInfo.getTrackGroups(i);
            if (trackGroups.length != 0) {
                if (player.getRendererType(i) == C.TRACK_TYPE_AUDIO) {
                    mAudioRendererIndex = i;
                    break;
                }
            }
        }
        trackGroups = mappedTrackInfo.getTrackGroups(mAudioRendererIndex);
        availableAudioTracks = new ArrayList<>();
        for (int groupIndex = 0; groupIndex < trackGroups.length; groupIndex++) {
            TrackGroup group = trackGroups.get(groupIndex);
            for (int trackIndex = 0; trackIndex < group.length; trackIndex++) {
                Format format = group.getFormat(trackIndex);
                availableAudioTracks.add(format);
            }
        }
        if (availableAudioTracks.size() > 1) {
            currentAudioSelector.setVisibility(VISIBLE);
            qualitySelectorRecyclerView = new RecyclerView(getContext());
            audioSelectorAdapter = new AudioSelectorAdapter(getContext(),
                    appCMSPresenter,
                    availableAudioTracks);

            qualitySelectorRecyclerView.setAdapter(audioSelectorAdapter);
            if (appCMSPresenter.getPlatformType() == AppCMSPresenter.PlatformType.TV) {
                qualitySelectorRecyclerView.setBackgroundColor(Color.TRANSPARENT);
            } else {
                qualitySelectorRecyclerView.setBackgroundColor(appCMSPresenter.getGeneralBackgroundColor());
            }
            qualitySelectorRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(),
                    LinearLayoutManager.VERTICAL,
                    false));

            AlertDialog.Builder builder = null;

            if ((appCMSPresenter.getPlatformType() == AppCMSPresenter.PlatformType.TV) && Utils.isFireTVDevice(getContext())) {
                builder = new AlertDialog.Builder(getContext(), R.style.customDialog);
            } else {
                builder = new AlertDialog.Builder(getContext());
            }

            if (qualitySelectorRecyclerView.getParent() != null && qualitySelectorRecyclerView.getParent() instanceof ViewGroup) {
                ((ViewGroup) qualitySelectorRecyclerView.getParent()).removeView(qualitySelectorRecyclerView);
            }
            builder.setView(qualitySelectorRecyclerView);
            final Dialog dialog = builder.create();
            if (dialog.getWindow() != null) {
                if ((appCMSPresenter.getPlatformType() == AppCMSPresenter.PlatformType.TV) && Utils.isFireTVDevice(getContext())) {
                    //dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#03000000")));
                    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(appCMSPresenter.getGeneralBackgroundColor()));
                    dialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
                } else {
                    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(appCMSPresenter.getGeneralBackgroundColor()));
                }
            }
            if (appCMSPresenter.getPlatformType() != AppCMSPresenter.PlatformType.ANDROID) {
                currentAudioSelector.setOnClickListener(v -> {
                    /**Click Handler*/
                    dialog.show();
                    audioSelectorAdapter.notifyDataSetChanged();
                });
            }

            audioSelectorAdapter.setItemClickListener(v -> {
                try {
                    if (v instanceof Format) {
                        DefaultTrackSelector.ParametersBuilder parametersBuilder = trackSelector.buildUponParameters();
                        int selectedIndex = audioSelectorAdapter.getDownloadQualityPosition();
                        DefaultTrackSelector.SelectionOverride override = new DefaultTrackSelector.SelectionOverride(
                                selectedIndex, 0);
                        parametersBuilder.setSelectionOverride(mAudioRendererIndex, trackGroups, override);

                        trackSelector.setParameters(parametersBuilder);
                        setStreamingQuality(selectedIndex, v);
                        Format format = ((Format) v);
                        String audioLable = (format.label != null && !TextUtils.isEmpty(format.label)) ?
                                format.label :
                                format.language;
                        currentAudioSelector.setText(audioLable);
                        audioSelectorAdapter.setSelectedIndex(selectedIndex);
                    }
                    dialog.hide();
                } catch (Exception e) {
                }
            });
        } else {
            if (currentAudioSelector != null)
                currentAudioSelector.setVisibility(GONE);
        }
        audioSelectorCreated = true;
    }

    boolean isRenderTracksAssign = false;

    public void setTracks(MappingTrackSelector.MappedTrackInfo mappedTrackInfo) {

        if ((mVideoRendererIndex == -1
                || mTextRendererIndex == -1
                || mAudioRendererIndex == -1) && !isRenderTracksAssign) {
            for (int i = 0; i < mappedTrackInfo.getRendererCount(); i++) {
                TrackGroupArray trackGroups = mappedTrackInfo.getTrackGroups(i);
                if (trackGroups.length != 0) {
                    switch (player.getRendererType(i)) {
                        case C.TRACK_TYPE_VIDEO:
                            mVideoRendererIndex = i;
                            break;
                        case C.TRACK_TYPE_TEXT:
                            mTextRendererIndex = i;
                            break;
                        case C.TRACK_TYPE_AUDIO:
                            mAudioRendererIndex = i;
                            break;
                    }
                }
            }
            isRenderTracksAssign = true;
        }
    }

    private String buildBitrateString(Format format) {
        return format.bitrate == Format.NO_VALUE ? ""
                : String.format(Locale.US, "%.2fMbit", format.bitrate / 1000000f);
    }


    private DrmSessionManager<ExoMediaCrypto> buildDrmSessionManager_ExoMediaCrypto(Context context,
                                                                                    String licenseUrl, String drmToken) {

        MediaDrmCallback mediaDrmCallback =
                createMediaDrmCallback(licenseUrl, new String[]{"X-AxDRM-Message", drmToken});

        return new DefaultDrmSessionManager.Builder()
                .setUuidAndExoMediaDrmProvider(C.WIDEVINE_UUID, FrameworkMediaDrm.DEFAULT_PROVIDER)
                .build(mediaDrmCallback);
    }

    private DefaultDrmSessionManager<ExoMediaCrypto> buildDrmSessionManager(Context context,
                                                                            String licenseUrl, String drmToken)
            throws UnsupportedDrmException {
        HttpMediaDrmCallback drmCallback = new HttpMediaDrmCallback(licenseUrl, buildHttpDataSourceFactory(BANDWIDTH_METER));
        if (drmToken != null) {
            drmCallback.setKeyRequestProperty("X-AxDRM-Message", drmToken);
        }
        DefaultDrmSessionManager.Builder drmSesseioMannagerBuilder = new DefaultDrmSessionManager.Builder();
        drmSesseioMannagerBuilder.setUuidAndExoMediaDrmProvider(C.WIDEVINE_UUID, (ExoMediaDrm.Provider) FrameworkMediaDrm.DEFAULT_PROVIDER);
        return drmSesseioMannagerBuilder.build(drmCallback);


    }

    private HttpMediaDrmCallback createMediaDrmCallback(
            String licenseUrl, String[] keyRequestPropertiesArray) {
        HttpDataSource.Factory licenseDataSourceFactory = buildHttpDataSourceFactory(BANDWIDTH_METER);
        HttpMediaDrmCallback drmCallback =
                new HttpMediaDrmCallback(licenseUrl, licenseDataSourceFactory);
        if (keyRequestPropertiesArray != null) {
            for (int i = 0; i < keyRequestPropertiesArray.length - 1; i += 2) {
                drmCallback.setKeyRequestProperty(keyRequestPropertiesArray[i],
                        keyRequestPropertiesArray[i + 1]);
            }
        }
        return drmCallback;
    }

    public Uri getAdTagUri(final String adURL) {
        return Uri.parse(adURL);
    }


    ImaAdsLoader mImaAdsLoader;


    /**
     * Returns an ads media source, reusing the ads loader if one exists.
     */
    @Nullable
    private MediaSource createAdsMediaSource(MediaSource mediaSource) {
        try {

            MediaSourceFactory adMediaSourceFactory = new MediaSourceFactory() {
                @Override
                public MediaSourceFactory setDrmSessionManager(DrmSessionManager<?> drmSessionManager) {
                    return null;
                }

                @Override
                public MediaSource createMediaSource(Uri uri) {
                    return VideoPlayerView.this.createLeafMediaSource(
                            uri, /* extension=*/ null, drmSessionManager);
                }

                @Override
                public int[] getSupportedTypes() {
                    return new int[0];
                }
            };

//            if (mImaAdsLoader == null) {
            mImaAdsLoader = createAdsLoader(getAdTagUri(adsUrl));
//            }
            return new AdsMediaSource(mediaSource, adMediaSourceFactory, mImaAdsLoader, playerView);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }

    }


    private MediaSource createLeafMediaSource(
            Uri uri, String overrideExtension, DrmSessionManager<?> drmSessionManager) {
        System.out.println(uri.toString());
        @C.ContentType int type = getURLContetType(uri, overrideExtension);
        switch (type) {
            case C.TYPE_DASH:
                return new DashMediaSource.Factory(new DefaultDataSourceFactory(getContext(), userAgent))  // Setting DefaultFataSourceFactory for DashMediaSource for working the AdaptivePlayer Rendition.
                        //return new DashMediaSource.Factory(mediaDataSourceFactory)
                        .setDrmSessionManager(drmSessionManager)
                        .createMediaSource(uri);
            case C.TYPE_SS:
                return new SsMediaSource.Factory(mediaDataSourceFactory)
                        .setDrmSessionManager(drmSessionManager)
                        .createMediaSource(uri);
            case C.TYPE_HLS:
                return new HlsMediaSource.Factory(mediaDataSourceFactory)
                        .setDrmSessionManager(drmSessionManager)
                        .createMediaSource(uri);
            case C.TYPE_OTHER:
                /*return new ExtractorMediaSource.Factory(mediaDataSourceFactory)
                        .createMediaSource(uri);*/
                return new ProgressiveMediaSource.Factory(mediaDataSourceFactory)
                        .setDrmSessionManager(drmSessionManager)
                        .createMediaSource(uri);
            default:
                throw new IllegalStateException("Unsupported type: " + type);
        }
    }

    private MediaSource buildMediaSource(Uri uri, Uri ccFileUrl) {
        if (mediaDataSourceFactory instanceof UpdatedUriDataSourceFactory) {
            if (null != policyCookie && null != signatureCookie && null != keyPairIdCookie) {
                ((UpdatedUriDataSourceFactory) mediaDataSourceFactory).signatureCookies.policyCookie = policyCookie;
                ((UpdatedUriDataSourceFactory) mediaDataSourceFactory).signatureCookies.signatureCookie = signatureCookie;
                ((UpdatedUriDataSourceFactory) mediaDataSourceFactory).signatureCookies.keyPairIdCookie = keyPairIdCookie;
            }
        }

        Format textFormat = Format.createTextSampleFormat(null,
                MimeTypes.APPLICATION_SUBRIP,
                C.SELECTION_FLAG_DEFAULT,
                "en");
        MediaSource videoSource = buildMediaSource(uri, "", drmSessionManager);

        if (ccFileUrl == null) {
            return videoSource;
        }

        SingleSampleMediaSource subtitleSource = new SingleSampleMediaSource.Factory(mediaDataSourceFactory).createMediaSource(
                ccFileUrl,
                textFormat,
                C.TIME_UNSET);

        // Plays the video with the side-loaded subtitle.
        return new MergingMediaSource(videoSource, subtitleSource);
    }


    /**
     * Queries video urls and subtitle urls from the hosting Activities which have implemented the
     * {@link VideoPlayerView.StreamingQualitySelector} or {@link VideoPlayerView.ClosedCaptionSelector}
     * <p>
     * This method iterated over multiple Mp4 & srt urls to create a {@link MergingMediaSource}
     * which have every possible rendition of Mp4 and every available SRT merged. In case of HLS, a
     * single .m3u8 file is merged with all available SRTs
     *
     * @return the merged media source object
     */
    private MediaSource buildMediaSource() {

        if (mediaDataSourceFactory instanceof UpdatedUriDataSourceFactory) {
            if (null != policyCookie && null != signatureCookie && null != keyPairIdCookie) {
                ((UpdatedUriDataSourceFactory) mediaDataSourceFactory).signatureCookies.policyCookie = policyCookie;
                ((UpdatedUriDataSourceFactory) mediaDataSourceFactory).signatureCookies.signatureCookie = signatureCookie;
                ((UpdatedUriDataSourceFactory) mediaDataSourceFactory).signatureCookies.keyPairIdCookie = keyPairIdCookie;
            }
        }

        // List of MediaSource which is later callowCrossProtocolRedirectsonverted to an array and used to create MergingMediaSource
        List<MediaSource> mediaSourceList = new ArrayList<>();

        /*Iterated over the available Mp4s, create an ExtractorMediaSource by calling the overloaded
         * buildMediaSource method */
        if (!useHls) {
            List<String> availableStreamingQualities = streamingQualitySelector.getAvailableStreamingQualities();
            for (int i = 0; i < availableStreamingQualities.size(); i++) {

                /*this returns an item something in the format of 360p or 0.25Mbit*/
                String streamingQuality = availableStreamingQualities.get(i);
                /*use this method to get the Mp4 url from the streaming quslitu*/
                String streamingQualityUrl = streamingQualitySelector.getStreamingQualityUrl(streamingQuality);

                if (streamingQualityUrl != null && !TextUtils.isEmpty(streamingQualityUrl)) {
                    // add the media source to the list
                    mediaSourceList.add(buildMediaSource(Uri.parse(streamingQualityUrl), "", drmSessionManager));
                }
            }

            if (offlineVideoUri != null)
                mediaSourceList.add(buildMediaSource(offlineVideoUri, "", drmSessionManager));

        } else { /* this is for HLS, getVideoUrl() returns the HLS url from the hosting activity*/

            if (streamingQualitySelector != null && streamingQualitySelector.getVideoUrl() != null && !TextUtils.isEmpty(streamingQualitySelector.getVideoUrl()))
                mediaSourceList.add(buildMediaSource(Uri.parse((streamingQualitySelector.getVideoUrl())), "", drmSessionManager));
            // mediaSourceList.add(createLeafMediaSource(Uri.parse((streamingQualitySelector.getVideoUrl())), "", drmSessionManager));
        }

        /*set subtitle text size, if the user haven't put in her preference, we default to 30sp*/
        setSubtitleTextSize(appPreference.getPreferredSubtitleTextSize(CommonUtils.getPlayerDefaultFontSize(appCMSPresenter)));

        /*Check if user has enabled CC in the app setting, it is off by default*/
        if (appPreference.getClosedCaptionPreference() && closedCaptionSelector != null) {
            /*getAvailableClosedCaptions() returns all the SRTs which we got in the ContentDatum*/
            List<ClosedCaptions> closedCaptionsList = closedCaptionSelector.getAvailableClosedCaptions();

            /* Iterate over the CC list and create a SingleSampleMediaSource for each Subtitles and add
             * each one of them to the list*/
            if (closedCaptionsList != null && closedCaptionsList.size() > 0) {
                for (int i = 0; i < closedCaptionsList.size(); i++) {
                    ClosedCaptions closedCaptions = closedCaptionsList.get(i);
                    if ("SRT".equalsIgnoreCase(closedCaptions.getFormat())) {
                        Format textFormat = Format.createTextSampleFormat(null,
                                MimeTypes.APPLICATION_SUBRIP,
                                C.SELECTION_FLAG_DEFAULT,
                                closedCaptions.getLanguage());

                        String ccFileUrl = closedCaptions.getUrl();

                        /*if (i == 0) {
                            ccFileUrl = "http://www.storiesinflight.com/js_videosub/jellies.srt";
                        }*/
                        if (ccFileUrl != null && !TextUtils.isEmpty(ccFileUrl) && (ccFileUrl.startsWith("file") || ccFileUrl.startsWith("http"))) {
                            System.out.println("CCURL IS == " + ccFileUrl + " LanguageCode is = " + closedCaptions.getLanguage());
                            SingleSampleMediaSource subtitleSource = new SingleSampleMediaSource.Factory(mediaDataSourceFactory).createMediaSource(
                                    Uri.parse(ccFileUrl),
                                    textFormat,
                                    C.TIME_UNSET);
                            mediaSourceList.add(subtitleSource);

                            /* CC button visibility & state is manipulated here*/
                            closeCaptionPreferenceCheck(closedCaptions.getLanguage(), i);
                        }
                    }
                }

                if (selectedSubtitleLanguageAvailable) {
                    setCCToggleButtonSelection(true);
                    setSubtitleViewVisibility(true);
                } else {
                    setCCToggleButtonSelection(false);
                    setSubtitleViewVisibility(false);
                }
            } else {
                /*Disable CC if the list is empty meaning no cc available for the particular movie*/

                if (streamingQualitySelector.getAvailableStreamingQualities().size() == 0) {
                    settingsButtonVisibility(false);
                }
                toggleCCSelectorVisibility(false);
                setCCToggleButtonSelection(false);
                setSubtitleViewVisibility(false);
            }
        } else {
            if (offlineClosedCaptionUri != null) {
                Format textFormat = Format.createTextSampleFormat(null,
                        MimeTypes.APPLICATION_SUBRIP,
                        C.SELECTION_FLAG_DEFAULT,
                        "en");

                SingleSampleMediaSource singleSampleMediaSource = new SingleSampleMediaSource.Factory(mediaDataSourceFactory)
                        .createMediaSource(offlineClosedCaptionUri,
                                textFormat,
                                C.TIME_UNSET);
                mediaSourceList.add(singleSampleMediaSource);
                setCCToggleButtonSelection(true);
                if (ccToggleButton.isSelected() && appCMSPresenter.getClosedCaptionPreference())
                    setSubtitleViewVisibility(true);
            } else {

                /*Disable CC if the user has turned CC off from settings*/
                settingsButtonVisibility(false);
                toggleCCSelectorVisibility(false);
                setCCToggleButtonSelection(false);
                setSubtitleViewVisibility(false);
            }
        }

        // Convert list into array and pass onto the MergingMediaSource constructor
        MediaSource[] mediaSources = new MediaSource[mediaSourceList.size()];
        mediaSourceList.toArray(mediaSources);

        //adsUrl = "https://pubads.g.doubleclick.net/gampad/ads?sz=640x480&iu=/124319096/external/ad_rule_samples&ciu_szs=300x250&impl=s&gdfp_req=1&env=vp&output=vmap&unviewed_position_start=1&vid=short_tencue&ad_rule=1&correlator=1586778403123&cust_params=deployment%3Ddevsite%26sample_ar%3Dpremidpostlongpod&cmsid=496";

        boolean isSubscribed = appCMSPresenter.getPlatformType().equals(AppCMSPresenter.PlatformType.ANDROID)
                ? appCMSPresenter.isUserSubscribed()
                : appCMSPresenter.getAppPreference().getIsUserSubscribed();
        if (adsUrl != null && !TextUtils.isEmpty(adsUrl)/* && !isSubscribed*/) {
            ConcatenatingMediaSource concatenatingMediaSource = null;
            try {
                concatenatingMediaSource = new ConcatenatingMediaSource(mediaSources);

            } catch (Exception e) {
                e.printStackTrace();
            }
            //return createAdsMediaSource(concatenatingMediaSource);
            return createAdsMediaSource(new MergingMediaSource(mediaSources));
            //return buildAdsMediaSource(new MergingMediaSource(mediaSources), adsUrl);
        } else {
            // Plays the video with the side-loaded subtitle.
            return new MergingMediaSource(mediaSources);
            //return new ConcatenatingMediaSource(mediaSources);
        }
    }


    private MediaSource buildMediaSource(
            Uri uri,
            String overrideExtension,
            DrmSessionManager<?> drmSessionManager) {
        @C.ContentType int type = getURLContetType(uri, overrideExtension);
        switch (type) {
            case C.TYPE_DASH:
                return new DashMediaSource.Factory(new DefaultDataSourceFactory(getContext(), userAgent))  // Setting DefaultFataSourceFactory for DashMediaSource  for working the AdaptivePlayer Rendition.
                        //return new DashMediaSource.Factory(mediaDataSourceFactory)
                        .setDrmSessionManager(drmSessionManager)
                        .createMediaSource(uri);
            case C.TYPE_SS:
                return new SsMediaSource.Factory(mediaDataSourceFactory)
                        .setDrmSessionManager(drmSessionManager)
                        .createMediaSource(uri);
            case C.TYPE_HLS:
                return new HlsMediaSource.Factory(mediaDataSourceFactory)
                        .setDrmSessionManager(drmSessionManager)
                        .createMediaSource(uri);
            case C.TYPE_OTHER:
                /*return new ExtractorMediaSource.Factory(mediaDataSourceFactory)
                        .createMediaSource(uri);*/

                return new ProgressiveMediaSource.Factory(mediaDataSourceFactory)
                        .setDrmSessionManager(drmSessionManager)
                        .createMediaSource(uri);
            default:
                throw new IllegalStateException("Unsupported type: " + type);
        }
    }

    private DefaultDrmSessionManager<FrameworkMediaCrypto> buildDrmSessionManagerV18(
            UUID uuid, String licenseUrl, String[] keyRequestPropertiesArray, boolean multiSession)
            throws UnsupportedDrmException {
        HttpDataSource.Factory licenseDataSourceFactory = buildHttpDataSourceFactory();
        HttpMediaDrmCallback drmCallback =
                new HttpMediaDrmCallback(licenseUrl, licenseDataSourceFactory);
        if (keyRequestPropertiesArray != null) {
            for (int i = 0; i < keyRequestPropertiesArray.length - 1; i += 2) {
                drmCallback.setKeyRequestProperty(keyRequestPropertiesArray[i],
                        keyRequestPropertiesArray[i + 1]);
            }
        }
        releaseMediaDrm();
        mediaDrm = FrameworkMediaDrm.newInstance(uuid);
        return new DefaultDrmSessionManager<>(uuid, mediaDrm, drmCallback, null, multiSession);
    }

    /**
     * Returns a {@link DataSource.Factory}.
     */
    public DataSource.Factory buildDataSourceFactory() {
        DefaultDataSourceFactory upstreamFactory =
                new DefaultDataSourceFactory(this.getContext(), buildHttpDataSourceFactory());
        return upstreamFactory;
    }


    private void releaseMediaDrm() {
        if (mediaDrm != null) {
            mediaDrm.release();
            mediaDrm = null;
        }
    }

    private DataSource.Factory buildDataSourceFactory(boolean useBandwidthMeter) {
        // return new DefaultDataSourceFactory(getContext(), buildHttpDataSourceFactory());
        return new DefaultDataSourceFactory(getContext(), buildDataSourceFactory(useBandwidthMeter ? BANDWIDTH_METER : null));
    }

    /**
     * Returns a {@link HttpDataSource.Factory}.
     */
    public HttpDataSource.Factory buildHttpDataSourceFactory() {
        return new DefaultHttpDataSourceFactory(userAgent);
    }

    private DataSource.Factory buildDataSourceFactory(DefaultBandwidthMeter bandwidthMeter) {
        return new UpdatedUriDataSourceFactory(getContext(),
                bandwidthMeter,
                //new DefaultDataSourceFactory(getContext(), buildHttpDataSourceFactory()),
                buildHttpDataSourceFactory(bandwidthMeter),
                policyCookie,
                signatureCookie,
                keyPairIdCookie);
    }

    private HttpDataSource.Factory buildHttpDataSourceFactory(DefaultBandwidthMeter bandwidthMeter) {
        // return new DefaultHttpDataSourceFactory(userAgent, bandwidthMeter);
        return new DefaultHttpDataSourceFactory(userAgent, bandwidthMeter, DefaultHttpDataSource.DEFAULT_CONNECT_TIMEOUT_MILLIS,
                DefaultHttpDataSource.DEFAULT_READ_TIMEOUT_MILLIS, true);
    }


    @Override
    public void onTracksChanged(TrackGroupArray trackGroupArray, TrackSelectionArray trackSelectionArray) {
        if ((appCMSPresenter.getPlatformType() == AppCMSPresenter.PlatformType.TV)) {
            if (trackSelectionArray != null
                    && trackSelectionArray.get(1) != null
                    && trackSelectionArray.get(1).getSelectedFormat() != null) {
                String id = (trackSelectionArray.get(1).getSelectedFormat().label != null &&
                        !TextUtils.isEmpty(trackSelectionArray.get(1).getSelectedFormat().label)) ?
                        trackSelectionArray.get(1).getSelectedFormat().label :
                        trackSelectionArray.get(1).getSelectedFormat().language;
                if (currentAudioSelector != null)
                    currentAudioSelector.setText(id);
            }

        }
    }

    @Override
    public void onLoadingChanged(boolean b) {

    }

    private List<String> getAudioTracks() {
        try {
            languageList = new ArrayList<>();
            languageList.clear();
            TrackNameProvider trackNameProvider = new DefaultTrackNameProvider(getResources());
            MappingTrackSelector.MappedTrackInfo trackInfo = trackSelector.getCurrentMappedTrackInfo();
            setTracks(trackInfo);
            if (trackInfo == null && mAudioRendererIndex == -1) {
                return null;
            }
            if (getURLContetType(uri, "") == C.TYPE_OTHER) {
                return null;
            }

            TrackGroupArray trackGroups = trackInfo.getTrackGroups(mAudioRendererIndex);
            String audioLable = null;
            if (trackGroups != null) {
                for (int groupIndex = 0; groupIndex < trackGroups.length; groupIndex++) {
                    TrackGroup group = trackGroups.get(groupIndex);
                    audioLable = (group.getFormat(0).label != null && !TextUtils.isEmpty(group.getFormat(0).label)) ?
                            group.getFormat(0).label :
                            group.getFormat(0).language;
                    languageList.add(audioLable);
                    isDefaultAudioAvailable = group.getFormat(0).id.toLowerCase().contains("default");
                }
            }
        } catch (Exception ex) {

        }
        return languageList;
    }

    private void checkAppResumeFromBackground() {
        if (CommonUtils.isFromBackground) {
            streamingQualitySelectorCreated = false;
            closedCaptionSelectorCreated = false;
            audioSelectorCreated = false;
            audioSelectorShouldCreated = true;
            CommonUtils.isFromBackground = false;
        }
    }

    @Override
    public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
        Log.d(TAG, "onPlayerStateChanged() called with: " +
                "playWhenReady = [" + playWhenReady + "], " +
                "playbackState = [" + playbackState + "], " +
                "manualPause = [" + isManualPause() + "]");
        if (playerState != null) {
            playerState.playWhenReady = playWhenReady;
            playerState.playbackState = playbackState;

            switch (playbackState) {
                case Player.STATE_READY: {
                    checkAppResumeFromBackground();
                    if (null != player && !player.isPlayingAd()) {
                        checkAndCreateSelectors();
                    }
                    if (currentPLayingVideoContentData != null && currentPLayingVideoContentData.getModuleApi() != null && currentPLayingVideoContentData.getModuleApi().getContentData() != null && currentPLayingVideoContentData.getModuleApi().getContentData().size() > 0) {
                        if (currentPLayingVideoContentData.getModuleApi().getContentData().get(0).getSeason() == null) {
                            hideRelatedVideoView(false);
                        } else if (currentPLayingVideoContentData.getModuleApi().getContentData().get(0).getSeason() != null
                                && currentPLayingVideoContentData.getModuleApi().getContentData().get(0).getSeason().size() == 0) {
                            hideRelatedVideoView(false);
                        } else {
                            hideRelatedVideoView(true);
                        }

                    }
                    if (!isLiveStreaming()) {
                        hidePlayerVolumeView(true);
                    }

                    if (isManualPause() && playWhenReady) {
                        appCMSPresenter.refreshVideoData(getFilmId(), new Action1<ContentDatum>() {
                                    @Override
                                    public void call(ContentDatum contentDatum) {
                                        System.out.println(TAG + " refreshVideoData:  " + contentDatum);
                                    }
                                },
                                null,
                                false,
                                false,
                                null);
                    }
                    break;
                }
                case Player.STATE_BUFFERING:
                    if (appCMSPresenter.getPlatformType() == AppCMSPresenter.PlatformType.ANDROID) {
                        getPlayerView().hideController();
                    }
                    break;
            }

            if (onPlayerStateChanged != null) {
                try {
                    Observable.just(playerState).subscribe(onPlayerStateChanged);
                } catch (Exception e) {
                    //Log.e(TAG, "Failed to update player state change status: " + e.getMessage());
                }
            }
        }
    }

    private void checkAndCreateSelectors() {
        if (!streamingQualitySelectorCreated) {
            if (!isOfflineVideoDownloaded) {
                createStreamingQualitySelectorForHLS();
                createStreamingQualitySelector();
            }
        }
        if (!closedCaptionSelectorCreated) {
            initCCAdapter();
        }
        if (!audioSelectorCreated && audioSelectorShouldCreated)
            if ((appCMSPresenter.getPlatformType() == AppCMSPresenter.PlatformType.TV)) {
                createAudioSelector();
            } else if ((appCMSPresenter.getPlatformType() == AppCMSPresenter.PlatformType.ANDROID)) {
                createLanguageSelector();
            }
    }


    protected void showStreamingQualitySelector() {
        if (null != currentStreamingQualitySelector
                && null != appCMSPresenter && uri != null && !appCMSPresenter.isVideoDownloaded(streamingQualitySelector.getFilmId())
                && appCMSPresenter.getPlatformType() == AppCMSPresenter.PlatformType.TV) {
            currentStreamingQualitySelector.setVisibility(View.VISIBLE);
        } else {
            if (appCMSPresenter != null && appCMSPresenter.isVideoDownloaded(streamingQualitySelector.getFilmId()) && appCMSPresenter.isUserLoggedIn()) {
                currentStreamingQualitySelector.setVisibility(View.GONE);
            } else if (appCMSPresenter != null && appCMSPresenter.getPlatformType() == AppCMSPresenter.PlatformType.TV) {
                if (currentStreamingQualitySelector != null)
                    currentStreamingQualitySelector.setVisibility(View.VISIBLE);
            }
        }
    }

    protected void toggleCCSelectorVisibility(boolean show) {
        if (null != ccToggleButton
                && appCMSPresenter != null
                && appCMSPresenter.getPlatformType() == AppCMSPresenter.PlatformType.TV) {
            ccToggleButton.setVisibility(show ? View.VISIBLE : View.GONE);
        }
    }

    public void setSubtitleViewVisibility(boolean show) {
        if (appCMSPresenter != null
                && appCMSPresenter.getPlatformType() == AppCMSPresenter.PlatformType.TV) {
            if (appPreference.getClosedCaptionPreference()) {
                VideoPlayerView.this.getPlayerView().getSubtitleView().setVisibility(show ? View.VISIBLE : View.GONE);
            } else {
                VideoPlayerView.this.getPlayerView().getSubtitleView().setVisibility(INVISIBLE);
            }
        } else {
            VideoPlayerView.this.getPlayerView().getSubtitleView().setVisibility(show ? View.VISIBLE : View.GONE);
        }
    }

    public void settingsButtonVisibility(boolean show) {

        if (null != mSettingButton
                && appCMSPresenter != null
                && appCMSPresenter.getPlatformType() == AppCMSPresenter.PlatformType.ANDROID) {
            mSettingButton.setVisibility(show ? View.VISIBLE : View.GONE);
            if (appCMSPresenter.isNewsTemplate() && show && !(getContext() instanceof AppCMSPlayVideoActivity)) {
               /* RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams)playerVolume.getLayoutParams();
                lp.addRule(RelativeLayout.ALIGN_RIGHT,mSettingButton.getId());
                lp.setMargins(0,0,60,20);*/
            }
        }
    }

    @Override
    public void onRepeatModeChanged(int repeatMode) {

    }

    @Override
    public void onShuffleModeEnabledChanged(boolean shuffleModeEnabled) {

    }


    @Override
    public void onPlayerError(ExoPlaybackException e) {
        mCurrentPlayerPosition = player.getCurrentPosition();
        if (isBehindLiveWindow(e)) {
            resumeWindow = C.INDEX_UNSET;
            resumePosition = C.TIME_UNSET;
            preparePlayer();
        } else if (mErrorEventListener != null) {
            mErrorEventListener.onRefreshTokenCallback();
            mErrorEventListener.playerError(e);
        }
    }

    protected static boolean isBehindLiveWindow(ExoPlaybackException e) {
        if (e.type != ExoPlaybackException.TYPE_SOURCE) {
            return false;
        }
        Throwable cause = e.getSourceException();
        while (cause != null) {
            if (cause instanceof BehindLiveWindowException) {
                return true;
            }
            cause = cause.getCause();
        }
        return false;
    }


    @Override
    public void onPlaybackSuppressionReasonChanged(int playbackSuppressionReason) {
        Log.d(TAG, "onPlaybackSuppressionReasonChanged() called with: playbackSuppressionReason = [" + playbackSuppressionReason + "]");
    }

    @Override
    public void onIsPlayingChanged(boolean isPlaying) {
        Log.d(TAG, "onIsPlayingChanged() called with: isPlaying = [" + isPlaying + "]");
    }

    @Override
    public void onPositionDiscontinuity(int reason) {
        Log.d(TAG, "onPositionDiscontinuity() called with: reason = [" + reason + "]");
        if (reason == 0) {
            if (isOfflineVideoDownloaded) {
                playerState.playWhenReady = true;
                playerState.playbackState = 4;
                if (onPlayerStateChanged != null) {
                    try {
                        Observable.just(playerState).subscribe(onPlayerStateChanged);
                    } catch (Exception e) {
                        //Log.e(TAG, "Failed to update player state change status: " + e.getMessage());
                    }
                }
            }
        }
    }

    @Override
    public void onPlaybackParametersChanged(PlaybackParameters playbackParameters) {
        Log.d(TAG, "onPlaybackParametersChanged() called with: playbackParameters = [" + playbackParameters + "]");
    }

    @Override
    public void onSeekProcessed() {
        Log.d(TAG, "onSeekProcessed() called");
    }


    public void sendPlayerPosition(long position) {
        mCurrentPlayerPosition = position;
    }

    public void setListener(ErrorEventListener errorEventListener) {
        mErrorEventListener = errorEventListener;
    }

    @Override
    public void onVideoEnabled(DecoderCounters counters) {

    }

    @Override
    public void onVideoDecoderInitialized(String decoderName, long initializedTimestampMs,
                                          long initializationDurationMs) {
        //
    }

    @Override
    public void onVideoInputFormatChanged(Format format) {
        setBitrate(format.bitrate / 1000);
        setVideoHeight(format.height);
        setVideoWidth(format.width);

        Log.d(TAG, "resolution: " + format.width + "x" + format.height);

        /*Only after the successful video track change, this method is called, setting the
         * currentStreamingQualitySelector warrants that it is the actual value which is now playing*/
        if (listViewAdapter != null) {
            /*String text = availableStreamingQualities.get(listViewAdapter.getSelectedIndex());
            appCMSPresenter.setCurrentVideoStreamingQuality(text);
            currentStreamingQualitySelector.setText(text);
            Log.d(TAG, "resolution: text " + text);
            if (isBitRateUpdatedCT) {
                appCMSPresenter.sendPlayerBitrateEvent(text);
                isBitRateUpdatedCT = false;
            }

            try {
                this.uri = Uri.parse(streamingQualitySelector.getStreamingQualityUrl(text));
                setSelectedStreamingQualityIndex();
            } catch (Exception e) {
            }

             */
        }
        if (hlsListViewAdapter != null && currentStreamingQualitySelector != null) {
           /* String textHls = availableStreamingQualitiesHLS.get(hlsListViewAdapter.getSelectedIndex()).getValue();
            currentStreamingQualitySelector.setText(textHls);
            appCMSPresenter.setCurrentVideoStreamingQuality(textHls);
            Log.d(TAG, "resolution: text" + textHls);
            if (isBitRateUpdatedCT) {
                appCMSPresenter.sendPlayerBitrateEvent(textHls);
                isBitRateUpdatedCT = false;
            }*/


        }
    }

    @Override
    public void onDroppedFrames(int count, long elapsedMs) {

    }

    @Override
    public void onVideoSizeChanged(int width, int height, int unappliedRotationDegrees, float pixelWidthHeightRatio) {
        //Log.i(TAG, "Video size changed: width = " +
//                width +
//                " height = " +
//                height +
//                " rotation degrees = " +
//                unappliedRotationDegrees +
//                " width/height ratio = " +
//                pixelWidthHeightRatio);
        if (width > height) {
            if (BaseView.isTablet(getContext())) {
                fullscreenResizeMode = AspectRatioFrameLayout.RESIZE_MODE_FIXED_WIDTH;
            } else {
                fullscreenResizeMode = AspectRatioFrameLayout.RESIZE_MODE_FIXED_HEIGHT;
            }
        } else {
            fullscreenResizeMode = AspectRatioFrameLayout.RESIZE_MODE_FIXED_HEIGHT;
        }

        if (BaseView.isLandscape(getContext())) {
            playerView.setResizeMode(fullscreenResizeMode);
        } else {
            playerView.setResizeMode(AspectRatioFrameLayout.RESIZE_MODE_FIT);
        }

        videoWidth = width;
        videoHeight = height;
    }

    @Override
    public void onRenderedFirstFrame(Surface surface) {

    }

    @Override
    public void onVideoDisabled(DecoderCounters counters) {

    }

    @Override
    public void onRenderedFirstFrame() {
        //Log.d(TAG, "Rendered first frame");
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        playOnReattach = player.getPlayWhenReady();
//        pausePlayer();

//        appCMSPresenter.updateWatchedTime(getFilmId(), player.getCurrentPosition());
    }

    public String getFilmId() {
        return filmId;
    }

    public void setFilmId(String filmId) {
        this.filmId = filmId;
    }

    public String getPolicyCookie() {
        return policyCookie;
    }

    public void setPolicyCookie(String policyCookie) {
        this.policyCookie = policyCookie;
    }

    public String getSignatureCookie() {
        return signatureCookie;
    }

    public void setSignatureCookie(String signatureCookie) {
        this.signatureCookie = signatureCookie;
    }

    public String getKeyPairIdCookie() {
        return keyPairIdCookie;
    }

    public void setKeyPairIdCookie(String keyPairIdCookie) {
        this.keyPairIdCookie = keyPairIdCookie;
    }

    public AppCMSPresenter getAppCMSPresenter() {
        return appCMSPresenter;
    }

    public void setAppCMSPresenter(AppCMSPresenter appCMSPresenter) {
        this.appCMSPresenter = appCMSPresenter;
    }

    protected AppCompatImageButton createCC_ToggleButton() {
        AppCompatImageButton mToggleButton = new AppCompatImageButton(getContext());
        RelativeLayout.LayoutParams toggleLP = new RelativeLayout.LayoutParams(BaseView.dpToPx(R.dimen.app_cms_video_controller_cc_width, getContext()), BaseView.dpToPx(R.dimen.app_cms_video_controller_cc_width, getContext()));
        toggleLP.addRule(RelativeLayout.CENTER_VERTICAL);
        if (appCMSPresenter.getTemplateType() == AppCMSPresenter.TemplateType.NEWS)
            toggleLP.addRule(RelativeLayout.START_OF, R.id.streamingQualitySelector);
        else
            toggleLP.addRule(RelativeLayout.RIGHT_OF, R.id.exo_media_controller);
        toggleLP.setMarginStart(BaseView.dpToPx(R.dimen.app_cms_video_controller_cc_left_margin, getContext()));
        toggleLP.setMarginEnd(BaseView.dpToPx(R.dimen.app_cms_video_controller_cc_left_margin, getContext()));
        mToggleButton.setLayoutParams(toggleLP);
        if (appCMSPresenter.isNewsTemplate()) {
            mToggleButton.setBackground(getResources().getDrawable(R.drawable.cc_button_selector_news, null));
        } else {
            mToggleButton.setBackground(getResources().getDrawable(R.drawable.cc_button_selector, null));
        }

//        mToggleButton.setId(R.id.ccToggleId);

        mToggleButton.setVisibility(GONE);
        return mToggleButton;
    }

    protected AppCompatImageButton createSettingButton() {
        /*mSettingButton = new ImageButton(getContext());
        RelativeLayout.LayoutParams toggleLP = new RelativeLayout.LayoutParams(BaseView.dpToPx(R.dimen.app_cms_video_controller_cc_width, getContext()), BaseView.dpToPx(R.dimen.app_cms_video_controller_cc_width, getContext()));
        toggleLP.addRule(RelativeLayout.CENTER_VERTICAL);
        toggleLP.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        toggleLP.addRule(RelativeLayout.RIGHT_OF,R.id.seek_bar_parent);
        toggleLP.setMarginStart(BaseView.dpToPx(R.dimen.app_cms_video_controller_cc_left_margin, getContext()));
        toggleLP.setMarginEnd(BaseView.dpToPx(R.dimen.app_cms_video_controller_cc_left_margin, getContext()));
        mSettingButton.setLayoutParams(toggleLP);
        mSettingButton.setId(View.generateViewId());
        mSettingButton.setBackground(getResources().getDrawable(R.drawable.ic_settings_24dp, null));*/

        mSettingButton = playerView.findViewById(R.id.playerSettingButton);
        mSettingButton.setVisibility(GONE);
        return mSettingButton;
    }

    public void showChromecastLiveVideoPlayer(boolean show) {
        if (show) {
            chromecastLivePlayerParent.setVisibility(VISIBLE);
            if (appCMSPresenter != null && appCMSPresenter.getCurrentMediaRouteButton() != null) {
                chromecastButtonPlaceholder.setVisibility(VISIBLE);
            } else {
                chromecastButtonPlaceholder.setVisibility(INVISIBLE);
            }
        } else {
            chromecastLivePlayerParent.setVisibility(INVISIBLE);
        }
    }

    public void disableFullScreenMode() {
        if (enterFullscreenButton != null &&
                exitFullscreenButton != null &&
                BaseView.isTablet(getContext())) {
            enterFullscreenButton.setVisibility(GONE);
            exitFullscreenButton.setVisibility(VISIBLE);
        }
    }

    public void exitFullscreenMode(boolean relaunchPage) {
        enableFullScreenMode();
        fullScreenMode = false;
        if (appCMSPresenter != null) {
            // appCMSPresenter.sendExitFullScreenAction(true);
        }
    }

    public void enableFullScreenMode() {
        if (enterFullscreenButton != null &&
                exitFullscreenButton != null &&
                BaseView.isTablet(getContext())) {
            exitFullscreenButton.setVisibility(INVISIBLE);
            enterFullscreenButton.setVisibility(VISIBLE);
        }
    }

    public void setChromecastButton(AppCompatImageButton chromecastButton) {
        if (chromecastButton.getParent() != null && chromecastButton.getParent() instanceof ViewGroup) {
            chromecastButtonPreviousParent = (ViewGroup) chromecastButton.getParent();
            chromecastButtonPreviousParent.removeView(chromecastButton);
        }
        chromecastButtonPlaceholder.addView(chromecastButton);
    }

    public void resetChromecastButton(AppCompatImageButton chromecastButton) {
        if (chromecastButton != null &&
                chromecastButton.getParent() != null &&
                chromecastButton.getParent() instanceof ViewGroup) {
            ((ViewGroup) chromecastButton.getParent()).removeView(chromecastButton);
        }
        if (chromecastButtonPreviousParent != null) {
            chromecastButtonPreviousParent.addView(chromecastButton);
        }
    }


    @Override
    public void onMediaPeriodCreated(int windowIndex, MediaSource.MediaPeriodId mediaPeriodId) {

    }

    @Override
    public void onMediaPeriodReleased(int windowIndex, MediaSource.MediaPeriodId mediaPeriodId) {

    }

    @Override
    public void onLoadStarted(int windowIndex, @Nullable MediaSource.MediaPeriodId mediaPeriodId, LoadEventInfo loadEventInfo, MediaLoadData mediaLoadData) {
        bitrate = (mediaLoadData.trackFormat.bitrate / 1000);
    }

    @Override
    public void onLoadCompleted(int windowIndex, @Nullable MediaSource.MediaPeriodId mediaPeriodId, LoadEventInfo loadEventInfo, MediaLoadData mediaLoadData) {

    }

    @Override
    public void onLoadCanceled(int windowIndex, @Nullable MediaSource.MediaPeriodId mediaPeriodId, LoadEventInfo loadEventInfo, MediaLoadData mediaLoadData) {

    }

    @Override
    public void onLoadError(int windowIndex, @Nullable MediaSource.MediaPeriodId mediaPeriodId, LoadEventInfo loadEventInfo, MediaLoadData mediaLoadData, IOException error, boolean wasCanceled) {

        /**
         * We can enhance logic here depending on the error code list that we will use for closing the video page.
         */
        if ((error.getMessage().contains("404") ||
                error.getMessage().contains("400"))
                && !isLoadedNext) {
            String failedMediaSourceLoadKey = loadEventInfo.dataSpec.uri.toString();
            if (failedMediaSourceLoads.containsKey(failedMediaSourceLoadKey)) {
                int tryCount = failedMediaSourceLoads.get(failedMediaSourceLoadKey);
                if (tryCount == 3) {
                    isLoadedNext = true;
                    mErrorEventListener.onFinishCallback(error.getMessage());
                } else {
                    failedMediaSourceLoads.put(failedMediaSourceLoadKey, tryCount + 1);
                }
            } else {
                failedMediaSourceLoads.put(failedMediaSourceLoadKey, 1);
            }
        } else if (mErrorEventListener != null) {
            mErrorEventListener.onRefreshTokenCallback();
        }
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
    public void onDrmSessionAcquired() {

    }

    @Override
    public void onDrmKeysLoaded() {

    }

    @Override
    public void onDrmSessionManagerError(Exception error) {

    }

    @Override
    public void onDrmKeysRestored() {

    }

    @Override
    public void onDrmKeysRemoved() {

    }

    @Override
    public void onDrmSessionReleased() {

    }


    public interface ErrorEventListener {
        void onRefreshTokenCallback();

        void onFinishCallback(String message);

        void playerError(ExoPlaybackException ex);
    }

    public interface StreamingQualitySelector {
        List<String> getAvailableStreamingQualities();


        /**
         * Returns the HLS url which will be used for playback
         */
        String getVideoUrl();

        void setVideoUrl(String url);

        String getStreamingQualityUrl(String streamingQuality);

        String getMpegResolutionFromUrl(String mpegUrl);

        int getMpegResolutionIndexFromUrl(String mpegUrl);

        String getFilmId();

        boolean isLiveStream();
    }

    /**
     * Contain methods used to fetch the closed captions' list and the language from the selected
     * index
     */
    public interface ClosedCaptionSelector {
        List<ClosedCaptions> getAvailableClosedCaptions();

        String getSubtitleLanguageFromIndex(int index);
    }

    public static class PlayerState {
        boolean playWhenReady;
        int playbackState;

        public boolean isPlayWhenReady() {
            return playWhenReady;
        }

        public int getPlaybackState() {
            return playbackState;
        }
    }

    public static class SignatureCookies {
        String policyCookie;
        String signatureCookie;
        String keyPairIdCookie;
    }

    private static class UpdatedUriDataSourceFactory implements Factory {
        private final Context context;
        private final TransferListener listener;
        private final DataSource.Factory baseDataSourceFactory;
        private SignatureCookies signatureCookies;

        /**
         * @param context   A context.
         * @param userAgent The User-Agent string that should be used.
         */
        public UpdatedUriDataSourceFactory(Context context, String userAgent, String policyCookie,
                                           String signatureCookie, String keyPairIdCookie) {
            this(context, userAgent, null, policyCookie, signatureCookie, keyPairIdCookie);
        }

        /**
         * @param context   A context.
         * @param userAgent The User-Agent string that should be used.
         * @param listener  An optional listener.
         */
        public UpdatedUriDataSourceFactory(Context context, String userAgent,
                                           TransferListener listener,
                                           String policyCookie, String signatureCookie, String keyPairIdCookie) {
            this(context, listener, new DefaultHttpDataSourceFactory(userAgent, listener), policyCookie,
                    signatureCookie, keyPairIdCookie);
        }

        /**
         * @param context               A context.
         * @param listener              An optional listener.
         * @param baseDataSourceFactory A {@link DataSource.Factory} to be used to create a base {@link DataSource}
         *                              for {@link DefaultDataSource}.
         * @param policyCookie          The cookie used for accessing CDN protected data.
         */
        public UpdatedUriDataSourceFactory(Context context, TransferListener listener,
                                           DataSource.Factory baseDataSourceFactory, String policyCookie,
                                           String signatureCookie, String keyPairIdCookie) {
            this.context = context.getApplicationContext();
            this.listener = listener;
            this.baseDataSourceFactory = baseDataSourceFactory;

            signatureCookies = new SignatureCookies();

            signatureCookies.policyCookie = policyCookie;
            signatureCookies.signatureCookie = signatureCookie;
            signatureCookies.keyPairIdCookie = keyPairIdCookie;
        }

        @Override
        public UpdatedUriDataSource createDataSource() {
            return new UpdatedUriDataSource(context, listener, baseDataSourceFactory.createDataSource(),
                    signatureCookies);
        }

        public void updateSignatureCookies(String policyCookie,
                                           String signatureCookie,
                                           String keyPairIdCookie) {
            signatureCookies.policyCookie = policyCookie;
            signatureCookies.signatureCookie = signatureCookie;
            signatureCookies.keyPairIdCookie = keyPairIdCookie;
        }
    }

    private static class UpdatedUriDataSource implements DataSource {
        private static final String SCHEME_ASSET = "asset";
        private static final String SCHEME_CONTENT = "content";

        private final DataSource baseDataSource;
        private final DataSource fileDataSource;
        private final DataSource assetDataSource;
        private final DataSource contentDataSource;
        private final SignatureCookies signatureCookies;

        private DataSource dataSource;

        /**
         * Constructs a new instance, optionally configured to follow cross-protocol redirects.
         *
         * @param context                     A context.
         * @param listener                    An optional listener.
         * @param userAgent                   The User-Agent string that should be used when requesting remote data.
         * @param allowCrossProtocolRedirects Whether cross-protocol redirects (i.e. redirects from HTTP
         *                                    to HTTPS and vice versa) are enabled when fetching remote data.
         */
        public UpdatedUriDataSource(Context context, TransferListener listener,
                                    String userAgent, boolean allowCrossProtocolRedirects,
                                    SignatureCookies signatureCookies) {
            this(context, listener, userAgent, DefaultHttpDataSource.DEFAULT_CONNECT_TIMEOUT_MILLIS,
                    DefaultHttpDataSource.DEFAULT_READ_TIMEOUT_MILLIS, allowCrossProtocolRedirects,
                    signatureCookies);
        }

        /**
         * Constructs a new instance, optionally configured to follow cross-protocol redirects.
         *
         * @param context                     A context.
         * @param listener                    An optional listener.
         * @param userAgent                   The User-Agent string that should be used when requesting remote data.
         * @param connectTimeoutMillis        The connection timeout that should be used when requesting remote
         *                                    data, in milliseconds. A timeout of zero is interpreted as an infinite timeout.
         * @param readTimeoutMillis           The read timeout that should be used when requesting remote data,
         *                                    in milliseconds. A timeout of zero is interpreted as an infinite timeout.
         * @param allowCrossProtocolRedirects Whether cross-protocol redirects (i.e. redirects from HTTP
         *                                    to HTTPS and vice versa) are enabled when fetching remote data.
         */
        public UpdatedUriDataSource(Context context, TransferListener listener,
                                    String userAgent, int connectTimeoutMillis, int readTimeoutMillis,
                                    boolean allowCrossProtocolRedirects, SignatureCookies signatureCookies) {
            this(context, listener,
                    new DefaultHttpDataSource(userAgent,
                            connectTimeoutMillis,
                            readTimeoutMillis,
                            allowCrossProtocolRedirects,
                            null),
                   /* new DefaultHttpDataSource(userAgent,
                            null,
                            listener,
                            connectTimeoutMillis,
                            readTimeoutMillis,
                            allowCrossProtocolRedirects,
                            null),*/
                    signatureCookies);
        }

        /**
         * Constructs a new instance that delegates to a provided {@link DataSource} for URI schemes other
         * than file, asset and content.
         *
         * @param context        A context.
         * @param listener       An optional listener.
         * @param baseDataSource A {@link DataSource} to use for URI schemes other than file, asset and
         *                       content. This {@link DataSource} should normally support at least http(s).
         */
        public UpdatedUriDataSource(Context context, TransferListener listener,
                                    DataSource baseDataSource,
                                    SignatureCookies signatureCookies) {
            this.baseDataSource = Assertions.checkNotNull(baseDataSource);

            this.fileDataSource = new FileDataSource();
            this.assetDataSource = new AssetDataSource(context);
            this.contentDataSource = new ContentDataSource(context);
            this.signatureCookies = signatureCookies;

            fileDataSource.addTransferListener(listener);
            assetDataSource.addTransferListener(listener);
            contentDataSource.addTransferListener(listener);

        }


        @Override
        public void addTransferListener(TransferListener transferListener) {

        }

        @Override
        public long open(DataSpec dataSpec) throws IOException {
            Assertions.checkState(dataSource == null);
            // Choose the correct source for the scheme.
            String scheme = dataSpec.uri.getScheme();
            if (Util.isLocalFileUri(dataSpec.uri)) {
                if (dataSpec.uri.getPath().startsWith("/android_asset/")) {
                    dataSource = assetDataSource;
                } else {
                    dataSource = fileDataSource;
                }
            } else if (SCHEME_ASSET.equals(scheme)) {
                dataSource = assetDataSource;
            } else if (SCHEME_CONTENT.equals(scheme)) {
                dataSource = contentDataSource;
            } else {
                dataSource = baseDataSource;
            }

            Uri updatedUri = Uri.parse(dataSpec.uri.toString().replaceAll(" ", "%20"));

            boolean useHls = dataSpec.uri.toString().contains(".m3u8") ||
                    dataSpec.uri.toString().contains(".ts") ||
                    dataSpec.uri.toString().contains("hls");
            if (useHls
                    && updatedUri.toString().contains("Policy=")
                    && updatedUri.toString().contains("Key-Pair-Id=")
                    && updatedUri.toString().contains("Signature=")
                    && updatedUri.toString().contains("?")) {
                updatedUri = Uri.parse(updatedUri.toString().substring(0, dataSpec.uri.toString().indexOf("?")));
            }

            if (useHls && dataSource instanceof DefaultHttpDataSource) {
                if (!TextUtils.isEmpty(signatureCookies.policyCookie) &&
                        !TextUtils.isEmpty(signatureCookies.signatureCookie) &&
                        !TextUtils.isEmpty(signatureCookies.keyPairIdCookie)) {
                    StringBuilder cookies = new StringBuilder();
                    cookies.append("CloudFront-Policy=");
                    cookies.append(signatureCookies.policyCookie);
                    cookies.append("; ");
                    cookies.append("CloudFront-Signature=");
                    cookies.append(signatureCookies.signatureCookie);
                    cookies.append("; ");
                    cookies.append("CloudFront-Key-Pair-Id=");
                    cookies.append(signatureCookies.keyPairIdCookie);
                    ((DefaultHttpDataSource) dataSource).setRequestProperty("Cookie", cookies.toString());
                }
            }

            final DataSpec updatedDataSpec = new DataSpec(updatedUri,
                    dataSpec.absoluteStreamPosition,
                    dataSpec.length,
                    dataSpec.key);

            // Open the source and return.
            try {
                return dataSource.open(updatedDataSpec);
            } catch (Exception e) {
                //Log.e(TAG, "Failed to load video: " + e.getMessage());
            }
            return 0L;
        }

        @Override
        public int read(byte[] buffer, int offset, int readLength) throws IOException {
            int result = 0;
            if (dataSource == null) {
                return 0;
            }
            if (dataSource instanceof FileDataSource &&
                    !dataSource.getUri().toString().toLowerCase().contains("srt")) {
                try {
                    long bytesRead = ((FileDataSource) dataSource).getBytesRead();
                    result = dataSource.read(buffer, offset, readLength);
                    for (int i = 0; i < 10 - bytesRead && i < readLength; i++) {
                        if (~buffer[i] >= -128 &&
                                ~buffer[i] <= 127 &&
                                buffer[i + offset] < 0) {
                            buffer[i + offset] = (byte) ~buffer[i + offset];
                        }
                    }
                    return result;
                } catch (Exception e) {
                    //Log.w(TAG, "Failed to retrieve number of bytes read from file input stream: " +
//                        e.getMessage());
                    result = dataSource.read(buffer, offset, readLength);
                }
            } else {
                try {
                    result = dataSource.read(buffer, offset, readLength);
                } catch (NullPointerException exception) {
                    exception.printStackTrace();
                }
            }
            return result;
        }

        @Override
        public Uri getUri() {
            return dataSource == null ? null : dataSource.getUri();
        }

        @Override
        public Map<String, List<String>> getResponseHeaders() {
            return null;
        }

        @Override
        public void close() throws IOException {
            if (dataSource != null) {
                try {
                    dataSource.close();
                } finally {
                    dataSource = null;
                }
            }
        }
    }


    public void setDRMEnabled(boolean DRMEnabled) {
        isDRMEnabled = DRMEnabled;
    }

    public boolean isDRMEnabled() {
        return isDRMEnabled;
    }

    public void setLicenseUrl(String licenseUrl) {
        this.licenseUrlDRM = licenseUrl;
    }

    public String getLicenseTokenDRM() {
        return licenseTokenDRM;
    }

    public void setLicenseTokenDRM(String licenseTokenDRM) {
        this.licenseTokenDRM = licenseTokenDRM;
    }

    public String getLicenseUrl() {
        return licenseUrlDRM;
    }

    public class PlayerAdEvent implements AdEvent.AdEventListener, AdsLoader.EventListener {

        @Override
        public void onAdEvent(AdEvent adEvent) {
            switch (adEvent.getType()) {
                case LOADED:
                    if (onBeaconAdsEvent != null) {
                        onBeaconAdsEvent.sendBeaconAdRequest();
                    }
                    System.out.println("Ads:-   LOADED called sendBeaconAdRequest ");
                    break;
                case CONTENT_RESUME_REQUESTED:
                    if (currentStreamingQualitySelector != null && appCMSPresenter.getPlatformType() == AppCMSPresenter.PlatformType.TV) {
                        currentStreamingQualitySelector.setVisibility(VISIBLE);
                    }
                    if (ccToggleButton != null
                            && appCMSPresenter.getPlatformType() == AppCMSPresenter.PlatformType.TV
                            && availableClosedCaptions.size() > 1) {
                        toggleCCSelectorVisibility(true);
                    }
                    System.out.println("Ads:-   CONTENT_RESUME_REQUESTED  ");
                    break;
                case ALL_ADS_COMPLETED:
                    System.out.println("Ads:-   ALL_ADS_COMPLETED  ");
                    if (onBeaconAdsEvent != null)
                        onBeaconAdsEvent.setPlayerCurrentPostionAfterAds();
                    releaseAdsLoader();
                    break;
                case CONTENT_PAUSE_REQUESTED:
                    if (currentStreamingQualitySelector != null) {
                        currentStreamingQualitySelector.setVisibility(INVISIBLE);
                    }
                    if (ccToggleButton != null) {
                        toggleCCSelectorVisibility(false);
                    }
                    if (onBeaconAdsEvent != null) {
                        onBeaconAdsEvent.sendBeaconAdImpression();
                    }
                    System.out.println("Ads:-   CONTENT_PAUSE_REQUESTED  sendBeaconAdImpression ");
                    break;
                case STARTED:
                    sendAdStartedFirebaseEvent();
                    break;
                case SKIPPED:
                    if (player != null && player.getCurrentPosition() > 0)
                        sendAdWatchedFirebaseEvent(+player.getCurrentPosition() / 1000);
                    //  sendAdWatchedFirebaseEvent(appCMSPresenter.getCurrentVideoWatchTime());
                    break;
                case PAUSED:
                    // sendAdWatchedFirebaseEvent(+player.getCurrentPosition()/1000);
                    //System.out.println("Ads:-   ADS_STARTED "+player.getCurrentPosition()/1000);
                    break;
                case COMPLETED:
                    checkAndCreateSelectors();
                    playerView.showController();
                    sendAdEndFirebaseEvent();
                    break;
                default:
                    System.out.println("Ads:-   default  ");

            }

        }


        @Override
        public void onAdPlaybackState(AdPlaybackState adPlaybackState) {
            System.out.println("Ads:-   onAdPlaybackState  ");
        }

        @Override
        public void onAdLoadError(AdsMediaSource.AdLoadException error, DataSpec dataSpec) {
            System.out.println("Ads:-   onAdLoadError  ");
        }


        @Override
        public void onAdClicked() {
            System.out.println("Ads:-   onAdClicked  ");
        }

        @Override
        public void onAdTapped() {
            System.out.println("Ads:-   onAdTapped  ");
        }


    }

    public void setOnBeaconAdsEvent(OnBeaconAdsEvent onBeaconAdsEvent) {
        this.onBeaconAdsEvent = onBeaconAdsEvent;
    }

    public interface OnBeaconAdsEvent {
        void sendBeaconAdImpression();

        void sendBeaconAdRequest();

        void setPlayerCurrentPostionAfterAds();
    }

    SeasonEpisodeSelctionEvent seasonEpisodeSelctionEvent;

    public interface SeasonEpisodeSelctionEvent {
        void viewClick(View view, int height);
    }

    public interface VideoPlayerSettingsEvent {
        /**
         * @param closedCaptionSelectorAdapter
         * @param videoQualityAdapter
         */
        void launchSetting(ClosedCaptionSelectorAdapter closedCaptionSelectorAdapter, StreamingQualitySelectorAdapter videoQualityAdapter);

        void launchSetting(ClosedCaptionSelectorAdapter closedCaptionSelectorAdapter, HLSStreamingQualitySelectorAdapter videoQualityAdapter, LanguageSelectorAdapter languageSelectorAdapter);

        void launchSetting(ClosedCaptionSelectorAdapter closedCaptionSelectorAdapter, HLSStreamingQualitySelectorAdapter hlsStreamingQualitySelectorAdapter, StreamingQualitySelectorAdapter streamingQualitySelectorAdapter, LanguageSelectorAdapter languageSelectorAdapter);

        void finishPlayerSetting();

    }


    public void setClosedCaption(int position) {

        if (position == -1) {
            return;
        }

        /** if position is anything else other than the mock "off" entry*/


        if (position != 0 && trackSelector.getCurrentMappedTrackInfo() != null) {
            setCCToggleButtonSelection(true);
            MappingTrackSelector.MappedTrackInfo trackInfo = trackSelector.getCurrentMappedTrackInfo();
            int textRenderIndex = mTextRendererIndex == -1 ? 0 : mTextRendererIndex;
            DefaultTrackSelector.SelectionOverride override;

            TrackGroupArray trackGroups1 = trackInfo.getTrackGroups(textRenderIndex);

            /**Fixed issue for CC default value added in list so check size of both list
             if track list length greater or equal remove from list*/

            int diff = -1;
            if (availableClosedCaptions != null && availableClosedCaptions.size() < trackGroups1.length && closedCaptionHlsEmbeded) {
                diff = trackGroups1.length - availableClosedCaptions.size();
            }
            int ccPos = position - 1;

            if (closedCaptionHlsEmbeded) {

                int indexDiff;
                if (position > diff && diff > 0) {
                    indexDiff = position - diff;
                } else {
                    indexDiff = position - 1;
                }
                ccPos = indexDiff;

                override = null;//new DefaultTrackSelector.SelectionOverride(ccPos, 0);
                DefaultTrackSelector.ParametersBuilder parametersBuilder = trackSelector.buildUponParameters();
                parametersBuilder.setRendererDisabled(textRenderIndex, false);
                if (override != null) {
                    parametersBuilder.setSelectionOverride(textRenderIndex, trackGroups1, override);
                } else {
                    parametersBuilder.clearSelectionOverrides(textRenderIndex);
                }
                trackSelector.setParameters(parametersBuilder);

            } else {
                if (closedCaptionSelectorAdapter != null && closedCaptionSelectorAdapter.closedCaptionsList != null && closedCaptionSelectorAdapter.closedCaptionsList.size() > 0 && closedCaptionSelectorAdapter.closedCaptionsList.size()>position) {
                    String language = closedCaptionSelectorAdapter.closedCaptionsList.get(position).getLanguage();
                    for (int groupIndex = 0; groupIndex < trackGroups1.length; groupIndex++) {
                        TrackGroup group = trackGroups1.get(groupIndex);
                        if (group != null && group.length > 0) {
                            for (int trackIndex = 0; trackIndex < group.length; trackIndex++) {
                                Format format = group.getFormat(trackIndex);
                                if (format != null && !TextUtils.isEmpty(format.language)
                                        && (format.language.equalsIgnoreCase(language) || CommonUtils.getLanguageNamefromCode(format.language).equalsIgnoreCase(language))) {
                                    ccPos = groupIndex;
                                    break;
                                }
                            }
                        }
                    }
                    override = new DefaultTrackSelector.SelectionOverride(ccPos, 0);

                    DefaultTrackSelector.ParametersBuilder builder =
                            trackSelector.getParameters()
                                    .buildUpon()
                                    .clearSelectionOverrides(textRenderIndex)
                                    .setRendererDisabled(textRenderIndex, false);
                    builder.setSelectionOverride(textRenderIndex, trackGroups1, override);

                    trackSelector.setParameters(builder.build());
                }
            }

            try {
                appPreference.setPreferredSubtitleLanguage(closedCaptionSelectorAdapter.closedCaptionsList.get(position).getLanguage());
            } catch (Exception e) {
                e.printStackTrace();
            }

            setSubtitleViewVisibility(true);

        } else { /**if position is the mock entry, just hide the subtitle view and do other stuff*/

            setCCToggleButtonSelection(false);
            setSubtitleViewVisibility(false);
            if (appCMSPresenter.getPlatformType() == AppCMSPresenter.PlatformType.TV) {
                appPreference.setPreferredSubtitleLanguage(null);
            }
            trackSelector.buildUponParameters().clearSelectionOverrides(mTextRendererIndex);
        }
        if (closedCaptionSelectorAdapter != null && closedCaptionSelectorAdapter.getItemCount() > position) {
            closedCaptionSelectorAdapter.setSelectedIndex(position);
            //availableClosedCaptions.get(position).setIsSelected(true);
        }

    }

    public void setStreamingQuality(int position, Object obj) {
        try {
            DefaultTrackSelector.Parameters parameters = trackSelector.getParameters();
            DefaultTrackSelector.ParametersBuilder parametersBuilder = parameters.buildUpon();
            if (obj instanceof HLSStreamingQuality && position != -1) {
                isBitRateUpdatedCT = true;
                int selectedIndex = /*hlsListViewAdapter.getDownloadQualityPosition()*/position;
                if (selectedIndex == 0) {

                    parametersBuilder.clearSelectionOverrides(mVideoRendererIndex);
                    trackSelector.setParameters(parametersBuilder.build());
                } else {
                    int[] tracks = new int[1];
                    tracks[0] = ((HLSStreamingQuality) obj).getIndex();
                    DefaultTrackSelector.SelectionOverride override = new DefaultTrackSelector.SelectionOverride(
                            0, tracks);
                    parametersBuilder.setSelectionOverride(mVideoRendererIndex, trackSelector.getCurrentMappedTrackInfo().getTrackGroups(mVideoRendererIndex), override);
                    trackSelector.setParameters(parametersBuilder.build());
                }
                if (currentStreamingQualitySelector != null)
                    // currentStreamingQualitySelector.setText(availableStreamingQualitiesHLS.get(selectedIndex).getValue());
                    hlsListViewAdapter.setSelectedIndex(selectedIndex);
                appPreference.setVideoStreamingQuality(availableStreamingQualitiesHLS.get(selectedIndex).getValue());
            } else {

                long playerCurrentVlue = player.getCurrentPosition();
                if (availableStreamingQualities != null && position != -1) {
                    isBitRateUpdatedCT = true;
                    currentStreamingQualitySelector.setText(availableStreamingQualities.get(position));
                    listViewAdapter.setSelectedIndex(position);
                    appPreference.setVideoStreamingQuality(availableStreamingQualities.get(position));
                    Uri uriQuality = Uri.parse(streamingQualitySelector.getStreamingQualityUrl(availableStreamingQualities.get(position)));
                    if (!uriQuality.toString().equalsIgnoreCase(uri.toString())) {
                        uri = uriQuality;
                        MediaSource mediaSource = buildMediaSource(uri, closedCaptionUri);
                        player.prepare(mediaSource);
                        player.setPlayWhenReady(true);
                        player.seekTo(playerCurrentVlue);
                    }
                }
            }


        } catch (Exception e) {

            e.printStackTrace();
        }
    }

    public void setAudioLanguage(int position) {
        if (position >= 0 && mAudioRendererIndex != -1) {
            try {

                TrackGroupArray audioTrackGroups = trackSelector.getCurrentMappedTrackInfo().getTrackGroups(mAudioRendererIndex);
                int[] tracks = new int[1];
                tracks[0] = position;
                DefaultTrackSelector.SelectionOverride override = new DefaultTrackSelector.SelectionOverride(
                        position, 0);

                DefaultTrackSelector.ParametersBuilder builder =
                        trackSelector.getParameters()
                                .buildUpon()
                                .clearSelectionOverrides(mAudioRendererIndex)
                                .setRendererDisabled(mAudioRendererIndex, false);
                builder.setSelectionOverride(mAudioRendererIndex, audioTrackGroups, override);

                trackSelector.setParameters(builder.build());

                try {
                    appPreference.setPreferredAudioLanguage(languageSelectorAdapter.getAvailableLanguages().get(position));
                } catch (Exception e) {
                    e.printStackTrace();
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    public void setCCToggleButtonSelection(boolean isSelected) {
        if (ccToggleButton != null) {
            ccToggleButton.setSelected(isSelected);
        }
    }

    public boolean isLiveStreaming() {
        if (getPlayerView() != null  /* if video is not Live */
                && getPlayerView().getController() != null
                && getPlayerView().getController().isPlayingLive() /* if video is not Live */) {
            if (appCMSPresenter.getTemplateType() == AppCMSPresenter.TemplateType.NEWS) {
                hideRelatedVideoView(false);
                //hidePlayerVolumeView(false);
                playerView.findViewById(R.id.seek_bar_parent).setVisibility(GONE);
            }
            if (playerLiveText != null && appCMSPresenter.getPlatformType() != AppCMSPresenter.PlatformType.TV) {
                playerLiveText.setVisibility(appCMSPresenter.isNewsTemplate() ? GONE : VISIBLE);
            }
            return true;
        } else {
            if (playerLiveText != null) {
                playerLiveText.setVisibility(GONE);
            }
            if (playerView != null && appCMSPresenter.getTemplateType() == AppCMSPresenter.TemplateType.NEWS) {
                playerView.findViewById(R.id.seek_bar_parent).setVisibility(VISIBLE);
            }
            return false;
        }
    }

    ContentDatum currentPLayingVideoContentData;

    public void setVideoContentData(ContentDatum videoContentData) {
        this.currentPLayingVideoContentData = videoContentData;
    }

    public void setmSettingButton(AppCompatImageButton mSettingButton) {
        this.mSettingButton = mSettingButton;
    }

    public void setPlyerVoumeButtom(AppCompatImageButton playerVolume) {
        this.playerVolume = playerVolume;
    }

    public AppCompatImageButton getPlayerVolume() {
        return playerVolume;
    }

    public AppCompatImageButton getRelatedVideoButton() {
        return relatedVideoButton;
    }

    public void hideRelatedVideoView(boolean visibility) {
        if (relatedVideoButton != null) {
            if (visibility)
                relatedVideoButton.setVisibility(VISIBLE);
            else
                relatedVideoButton.setVisibility(GONE);
        }
    }

    public void hidePlayerVolumeView(boolean visibility) {
        if (playerVolume != null) {
            if (visibility)
                playerVolume.setVisibility(VISIBLE);
            else
                playerVolume.setVisibility(GONE);
        }
    }

    private void releaseAdsLoader() {
        if (mImaAdsLoader != null) {
            mImaAdsLoader.release();
            mImaAdsLoader = null;
            playerView.getOverlayFrameLayout().removeAllViews();
        }
    }

    public void setClosedCaptionSelectorCreated(boolean closedCaptionSelectorCreated) {
        this.closedCaptionSelectorCreated = closedCaptionSelectorCreated;
    }

    @Override
    public void preparePlayback() {
        player.retry();
    }

    private void sendAdStartedFirebaseEvent() {
        if (appCMSPresenter.getmFireBaseAnalytics() != null) {
            Bundle bundle = new Bundle();
            bundle.putString("screen_view", "Ad View");
            appCMSPresenter.getmFireBaseAnalytics().logEvent(FIREBASE_Ad_START, bundle);
        }
    }

    private void sendAdEndFirebaseEvent() {
        if (appCMSPresenter.getmFireBaseAnalytics() != null) {
            Bundle bundle = new Bundle();
            bundle.putString("screen_view", "Ad End");
            appCMSPresenter.getmFireBaseAnalytics().logEvent(FIREBASE_Ad_END, null);
        }
    }

    private void sendAdWatchedFirebaseEvent(long watchTime) {
        if (appCMSPresenter.getmFireBaseAnalytics() != null) {
            Bundle bundle = new Bundle();
            bundle.putLong("content_seconds_watched", watchTime);
            appCMSPresenter.getmFireBaseAnalytics().logEvent(FIREBASE_Ad_SECONDS_WATCHED, bundle);
        }
    }


    VolumeObserver volumeObserver;

    public class VolumeObserver extends ContentObserver {

        Context mContext;

        public VolumeObserver(Context context, Handler handler) {
            super(handler);
            mContext = context;
            // audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        }

        @Override
        public boolean deliverSelfNotifications() {
            return false;
        }

        @Override
        public void onChange(boolean selfChange) {
            int currentVal = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
            if (getPlayerVolume() != null) {
                if (currentVal == 0) {
                    if (getPlayerVolume() != null) {
                        getPlayerVolume().setSelected(true);
                        getPlayerVolume().setImageResource(R.drawable.player_mute);
                    }
                    audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, 0, 0);
                } else {
                    if (getPlayerVolume() != null) {
                        getPlayerVolume().setSelected(false);
                        getPlayerVolume().setImageResource(R.drawable.player_volume);
                    }
                    audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, currentVal, 0);
                }
            }
        }

    }

    int phoneVolume;

    void setPlayerVolumeImage(AppCompatImageButton volumeImage, boolean defaultVolume) {
        int currentVal = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
        if (currentVal != 0)
            phoneVolume = currentVal;
        int maxVol = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.P) {
            int minVol = audioManager.getStreamMinVolume(AudioManager.STREAM_MUSIC);
        }
        if (defaultVolume) {
            if (currentVal == 0) {
                if (getPlayerVolume() != null) {
                    getPlayerVolume().setSelected(true);
                    getPlayerVolume().setImageResource(R.drawable.player_mute);
                }
                audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, 0, 0);
            } else {
                if (getPlayerVolume() != null) {
                    getPlayerVolume().setSelected(false);
                    getPlayerVolume().setImageResource(R.drawable.player_volume);
                }
                audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, currentVal, 0);
            }
        } else {
            if (volumeImage.isSelected()) {
                volumeImage.setImageResource(R.drawable.player_mute);
                audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, 0, 0);
                CommonUtils.DEFAULT_VOLUME_FROM_MINI_PLAYER = 0;

            } else {
                volumeImage.setImageResource(R.drawable.player_volume);
                audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, phoneVolume, 0);
                if (getPlayer() != null)
                    getPlayer().setVolume(phoneVolume);
            }
        }

    }


    int getPlayerSeekBarAndControllerHeight() {
        return playerView.findViewById(R.id.exo_controller_container).getHeight() +
                playerView.findViewById(R.id.seek_bar_parent).getHeight();
    }


    private List<CCFontSize> createCCTextSizeList() {
        List<CCFontSize> ccSizeList = new ArrayList<>();

        ccSizeList.add(new CCFontSize(0, localisedStrings.getSmallFontText(), 20));
        ccSizeList.add(new CCFontSize(1, localisedStrings.getRegularFontText(), 30));
        ccSizeList.add(new CCFontSize(2, localisedStrings.getLargeFontText(), 40));

        return ccSizeList;
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {

        if (event.getKeyCode() == KeyEvent.KEYCODE_DPAD_CENTER
                && playerActionOverlay != null && playerActionOverlay.getVisibility() == VISIBLE
                && buttonSeekAction != null
                && buttonSeekAction.getVisibility() == VISIBLE) {
            buttonSeekAction.performClick();
            return true;
        } else if (event.getKeyCode() == KeyEvent.KEYCODE_DPAD_CENTER && isPlayNextVisible) {
            isPlayNextVisible = false;
            onPlayNextEvent.performPlayNextClick();
            return true;
        } else if (event.getKeyCode() != KeyEvent.KEYCODE_DPAD_CENTER && isPlayNextVisible) {
            isPlayNextVisible = false;
        }
        if (event.getAction() == KeyEvent.ACTION_DOWN) {
            if (event.getKeyCode() == KeyEvent.KEYCODE_MEDIA_PLAY_PAUSE && !player.isPlayingAd()) {
                System.out.println("playWhenReady : " + shouldPlayWhenReady());
                setManualPause(!shouldPlayWhenReady());
            }
        }
        if (player.isPlayingAd() && (event.getKeyCode() == KeyEvent.KEYCODE_MEDIA_PLAY_PAUSE
                || event.getKeyCode() == KeyEvent.KEYCODE_MEDIA_NEXT
                || event.getKeyCode() == KeyEvent.KEYCODE_MEDIA_PREVIOUS
                || event.getKeyCode() == KeyEvent.KEYCODE_MEDIA_FAST_FORWARD
                || event.getKeyCode() == KeyEvent.KEYCODE_MEDIA_REWIND)) {
            return true;
        }
        return super.dispatchKeyEvent(event);
    }

    public void createMessage(ContentDatum data) {

        if (data != null && data.getGist() != null) {
            Gist gistData = data.getGist();
            PlayerEventPayLoad payLoad;
            if (gistData.getSkipIntroStartTime() != 0 && gistData.getSkipIntroEndTime() != 0) {
                payLoad = new PlayerEventPayLoad(PLAYER_EVENT_PAYLOAD_SKIP_INTRO,
                        gistData.getSkipIntroStartTime(),
                        gistData.getSkipIntroEndTime());

                createMessage(payLoad);
            }
            if (gistData.getSkipRecapStartTime() != 0 && gistData.getSkipRecapEndTime() != 0) {
                payLoad = new PlayerEventPayLoad(PLAYER_EVENT_PAYLOAD_SKIP_RECAP,
                        gistData.getSkipRecapStartTime(),
                        gistData.getSkipRecapEndTime());
                createMessage(payLoad);
            }
            if (gistData.getPlayNextTime() != 0) {
                payLoad = new PlayerEventPayLoad(PLAYER_EVENT_PAYLOAD_PLAY_NEXT,
                        gistData.getPlayNextTime(),
                        0);
                createMessage(payLoad);
            }
        }
    }

    Handler skipRecapHandler;
    Handler skipIntroHandler;
    Handler playNextHandler;
    final Runnable[] skipActionRnnable = new Runnable[3];

    public void createMessage(PlayerEventPayLoad payLoad) {
        player.createMessage((messageType, payload) -> {
            PlayerEventPayLoad payLoadVal = (PlayerEventPayLoad) payload;
            long finalSeekToPositionMs = (payLoadVal.getSeekTime() * 1000);
            if (playerActionOverlay != null) {
                AppCompatButton buttonSeekAction = playerActionOverlay.findViewById(R.id.button_seekAction);
                buttonSeekAction.setFocusable(true);
                buttonSeekAction.requestFocus();
                switch (payLoadVal.getPlayerEventType()) {
                    case PLAYER_EVENT_PAYLOAD_SKIP_INTRO:
                        buttonSeekAction.setText(appCMSPresenter.getLocalisedStrings().getSkipIntroButtonText());
                        skipIntroHandler = new Handler(player.getApplicationLooper());
                        skipActionRnnable[0] = new Runnable() {
                            @Override
                            public void run() {
                                if (playerActionOverlay != null) {
                                    System.out.println("player.getCurrentPosition() > finalSeekToPositionMs " + player.getCurrentPosition() + " - " + finalSeekToPositionMs);
                                    if (player.getCurrentPosition() >= finalSeekToPositionMs) {
                                        actionOverlayVisibility(false);
                                        skipIntroHandler.removeCallbacks(skipActionRnnable[0]);
                                    } else {
                                        actionOverlayVisibility(true);
                                        skipIntroHandler.postDelayed(skipActionRnnable[0], 1000);
                                    }
                                }
                            }
                        };
                        skipIntroHandler.postDelayed(skipActionRnnable[0], 1000);
                        break;
                    case PLAYER_EVENT_PAYLOAD_SKIP_RECAP:
                        buttonSeekAction.setText(appCMSPresenter.getLocalisedStrings().getSkipRecapButtonText());
                        skipRecapHandler = new Handler(player.getApplicationLooper());
                        skipActionRnnable[1] = new Runnable() {
                            @Override
                            public void run() {
                                if (playerActionOverlay != null) {
                                    System.out.println("player.getCurrentPosition() > finalSeekToPositionMs " + player.getCurrentPosition() + " - " + finalSeekToPositionMs);
                                    if (player.getCurrentPosition() >= finalSeekToPositionMs) {
                                        actionOverlayVisibility(false);
                                        skipRecapHandler.removeCallbacks(skipActionRnnable[1]);
                                    } else {
                                        actionOverlayVisibility(true);
                                        skipRecapHandler.postDelayed(skipActionRnnable[1], 1000);
                                    }
                                }
                            }
                        };
                        skipRecapHandler.postDelayed(skipActionRnnable[1], 1000);
                        break;
                    case PLAYER_EVENT_PAYLOAD_PLAY_NEXT:
                        long playNextTime = (payLoad.getPlayerEventTime() * 1000);
                        playNextHandler = new Handler(player.getApplicationLooper());
                        skipActionRnnable[2] = new Runnable() {
                            @Override
                            public void run() {
                                if (playerActionOverlay != null) {
                                    System.out.println("player.getCurrentPosition() > finalSeekToPositionMs   playNextTime " + player.getCurrentPosition() + " - " + playNextTime);
                                    if (player.getCurrentPosition() < playNextTime) {
                                        isPlayNextVisible = false;
                                        onPlayNextEvent.performPlayNextVisibility(isPlayNextVisible);
                                        playNextHandler.removeCallbacks(skipActionRnnable[2]);
                                    } else {
                                        playNextHandler.postDelayed(skipActionRnnable[2], 1000);
                                    }
                                }
                            }
                        };
                        playNextHandler.postDelayed(skipActionRnnable[2], 1000);

                        actionOverlayVisibility(false);
                        isPlayNextVisible = true;
                        onPlayNextEvent.performPlayNextVisibility(isPlayNextVisible);
                        break;
                }

                buttonSeekAction.setOnKeyListener((v, keyCode, event) -> {
                    System.out.println(" playWhenReady  buttonSeekAction.setOnKeyListener ");
                    System.out.println("KeyEvent. buttonSeekAction.setOnKeyListener((v, keyCode, event) VideoPlayerView  ");
                    if (event.getAction() == KeyEvent.ACTION_DOWN || event.getAction() == KeyEvent.ACTION_UP) {
                        switch (event.getKeyCode()) {
                            case KeyEvent.KEYCODE_DPAD_CENTER:
                                player.seekTo(finalSeekToPositionMs);
                                actionOverlayVisibility(false);
                                break;
                        }
                    }
                    return false;
                });
                buttonSeekAction.setOnClickListener(v -> {
                    //appCMSPresenter.showToast("ButtonClicked ", Toast.LENGTH_LONG);
                    actionOverlayVisibility(false);
                    player.seekTo(finalSeekToPositionMs);
                });

                playerActionOverlay.setVisibility(VISIBLE);


            }
        })
                .setHandler(new Handler(player.getApplicationLooper()))
                .setPosition(/* windowIndex= */  /* positionMs= */ (payLoad.getPlayerEventTime() * 1000))
                .setPayload(payLoad)
                .setDeleteAfterDelivery(false)
                .send();
    }

    public void actionOverlayVisibility(boolean visible) {
        if (visible) {
            playerActionOverlay.setVisibility(VISIBLE);
            buttonSeekAction.setVisibility(VISIBLE);
            buttonSeekAction.setFocusable(true);
        } else {
            playerActionOverlay.setVisibility(GONE);
            buttonSeekAction.setVisibility(GONE);
        }
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

    OnPlayNextEvent onPlayNextEvent;
    boolean isPlayNextVisible = false;

    public void setOnPlayNextListener(OnPlayNextEvent onPlayNextEvent) {
        this.onPlayNextEvent = onPlayNextEvent;
    }

    public interface OnPlayNextEvent {
        public void performPlayNextVisibility(boolean visible);

        public void performPlayNextClick();

        public boolean getPlayNextFocus();
    }
}
