package com.viewlift.tv.views.fragment;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.percentlayout.widget.PercentRelativeLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.google.ads.interactivemedia.v3.api.ImaSdkFactory;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.drm.DrmSession;
import com.viewlift.AppCMSApplication;
import com.viewlift.R;
import com.viewlift.analytics.AppsFlyerUtils;
import com.viewlift.models.data.appcms.api.AppCMSSignedURLResult;
import com.viewlift.models.data.appcms.api.ClosedCaptions;
import com.viewlift.models.data.appcms.api.ContentDatum;
import com.viewlift.models.data.appcms.api.Gist;
import com.viewlift.models.data.appcms.api.Module;
import com.viewlift.models.data.appcms.api.Season_;
import com.viewlift.models.data.appcms.api.VideoAssets;
import com.viewlift.models.data.appcms.beacon.BeaconBuffer;
import com.viewlift.models.data.appcms.beacon.BeaconHandler;
import com.viewlift.models.data.appcms.beacon.BeaconPing;
import com.viewlift.models.data.appcms.beacon.BeaconRunnable;
import com.viewlift.models.data.appcms.ui.android.MetaPage;
import com.viewlift.models.data.appcms.ui.android.NavigationUser;
import com.viewlift.models.data.appcms.ui.main.AppCMSMain;
import com.viewlift.models.data.appcms.ui.page.Component;
import com.viewlift.models.data.appcms.ui.page.Layout;
import com.viewlift.models.data.appcms.ui.tv.FireTV;
import com.viewlift.presenters.AppCMSPresenter;
import com.viewlift.tv.utility.HistorySyncUtils;
import com.viewlift.tv.utility.Utils;
import com.viewlift.utils.CommonUtils;
import com.viewlift.utils.ContentTypeChecker;
import com.viewlift.views.customviews.VideoPlayerView;
import com.viewlift.views.customviews.exoplayerview.AppCMSPlayerView;
import com.viewlift.views.listener.PlayerPlayPauseState;
import com.viewlift.views.listener.TrailerCompletedCallback;
import com.viewlift.views.listener.VideoSelected;

import java.net.CookieHandler;
import java.net.CookieManager;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Optional;
import rx.functions.Action0;
import rx.functions.Action1;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

/**
 * Created by viewlift on 6/14/17.
 */

public class AppCMSPlayVideoFragment
        extends Fragment
        implements AppCmsTvErrorFragment.ErrorFragmentListener,
        VideoPlayerView.ErrorEventListener,
        VideoPlayerView.OnBeaconAdsEvent, PlayerPlayPauseState,
        VideoPlayerView.OnPlayNextEvent{
    private static final String TAG = "PlayVideoFragment";

    private AppCMSPresenter appCMSPresenter;
    private static final long SECONDS_TO_MILLIS = 1000L;

    private String fontColor;
    private String title;
    private String hlsUrl;
    private String permaLink;
    private String filmId;
    private String seriesId;
    private String parentScreenName;
    private String adsUrl;
    private boolean shouldRequestAds;
    private RelativeLayout playBackStateLayout;
    private ProgressBar progressBar;
    private LinearLayout videoPlayerInfoContainer;
    private View backgroundView;
    private Button videoPlayerViewDoneButton;
    private TextView videoPlayerTitleView, playBackStateTextView;
    private VideoPlayerView videoPlayerView;
    private OnClosePlayerEvent onClosePlayerEvent;
    private BeaconPing beaconMessageThread;
    private long beaconMsgTimeoutMsec;

    private ImaSdkFactory sdkFactory;
    AppCmsTvErrorFragment errorActivityFragment;
    boolean networkConnect, networkDisconnect = true;
    private ArrayList<ClosedCaptions> closedCaptionUrl;
    private Context mContext;
    private boolean isTrailer;
    private boolean isPromo=false;
    private int playIndex;
    private long watchedTime;
    private String imageUrl;
    private String primaryCategory;
    private String parentalRating;
    private String mStreamId;
    private long runtime;
    private long videoPlayTime;
    private TimerTask entitlementCheckTimerTask;
    private Timer entitlementCheckTimer;
    private boolean entitlementCheckCancelled;
    private boolean freeContent;
    private static int apod = 0;
    private BeaconBuffer beaconBufferingThread;
    private BeaconHandler mBeaconHandler;
    private BeaconRunnable mBeaconRunnable;
    private long mStopBufferMilliSec;
    private long mStartBufferMilliSec = 0l;
    private double ttfirstframe;
    private long beaconBufferingTimeoutMsec;
    private boolean sentBeaconPlay;
    private boolean sentBeaconFirstFrame;
    private boolean sentHistoryAPICallAtVideoLaunch;
    private boolean sentHistoryAPICallAtVideoFinished;
    private long mTotalVideoDuration;
    int maxPreviewSecs = 0;
    private VideoPlayerView.StreamingQualitySelector streamingQualitySelector;
    private VideoPlayerView.ClosedCaptionSelector closedCaptionSelector;
    private PercentRelativeLayout contentRatingMainContainer;
    private RelativeLayout mathProblemViewContainer;
    private LinearLayout contentRatingInfoContainer;
    private TextView contentRatingHeaderView;
    private TextView contentRatingDiscretionView;
    private TextView contentRatingTitleHeader;
    private RelativeLayout videoPlayerMainContainer;

    private final int totalCountdownInMillis = 2000;
    private final int countDownIntervalInMillis = 20;
    private String signatureCookie;
    private String policyCookie;
    private String keyPairIdCookie;
    private ContentDatum contentDatum;
    private boolean playWhenReady = true;
    int playedVideoSecs = 0;
    private String segmentId = null;
    private Button mathProblemWatchNowButton;
    private TextView mathProblemWrongAnswerTextView;
    private TextView mathProblemTextView;
    private TextView mathProblemMessageView;
    private TextView mathProblemSubtextView;
    private int firstNumber, secondNumber;
    private EditText mathProblemAnswerEditTextView;


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
    ContentTypeChecker contentTypeChecker;
    Handler mProgressHandler;
    Runnable mProgressRunnable;
    boolean isStreamStart, isStream25, isStream50, isStream75, isStream95, isStream100;
    private String seriesParentalRating;

    private class DRMObj {
        boolean isDrmVideoEnabled;
        String licenseUrl;
        String licenseToken;
    }

    DRMObj drmObj;

    public VideoPlayerView getVideoPlayerView() {
        return videoPlayerView;
    }

    private VideoPlayerView.SeasonEpisodeSelctionEvent seasonEpisodeSelctionEvent;
    private Module moduleApi;
    private VideoSelected videoSelected;
    private List<ContentDatum> allEpisodes = null;
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

    @Nullable
    @BindView(R.id.playNextEpisodeContainer)
    ConstraintLayout playNextEpisodeContainer;
    @Nullable
    @BindView(R.id.playNextEpisodeImage)
    AppCompatImageView playNextEpisodeImage;
    @Nullable
    @BindView(R.id.playNextTitle)
    AppCompatTextView playNextTitle;
    @Nullable
    @BindView(R.id.playNextEpisodeTitle)
    AppCompatTextView playNextEpisodeTitle;
    @Nullable
    @BindView(R.id.playNextEpisodeDescription)
    AppCompatTextView playNextEpisodeDescription;


    public interface OnClosePlayerEvent {
        void closePlayer();

        /**
         * Method is to be called by the fragment to tell the activity that a movie is finished
         * playing. Primarily in the {@link ExoPlayer#STATE_ENDED}
         */
        void onMovieFinished();
    }

    public static AppCMSPlayVideoFragment newInstance(Context context,
                                                      String primaryCategory,
                                                      String fontColor,
                                                      String title,
                                                      String permaLink,
                                                      boolean isTrailer,
                                                      String hlsUrl,
                                                      String filmId,
                                                      String adsUrl,
                                                      boolean requestAds,
                                                      int playIndex,
                                                      long watchedTime,
                                                      long runtime,
                                                      String imageUrl,
                                                      ArrayList<ClosedCaptions> closedCaptionUrl,
                                                      String parentalRating,
                                                      boolean freeContent,
                                                      AppCMSSignedURLResult appCMSSignedURLResult,
                                                      ContentDatum contentDatum,
                                                      String seriesParentalRating,
                                                      boolean isDrmVideoEnabled,
                                                      String licenseUrl,
                                                      String licenseToken) {

        AppCMSPlayVideoFragment appCMSPlayVideoFragment = new AppCMSPlayVideoFragment();
        Bundle args = new Bundle();
        args.putString(context.getString(R.string.video_player_font_color_key), fontColor);
        args.putString(context.getString(R.string.video_primary_category_key), primaryCategory);
        args.putString(context.getString(R.string.video_player_title_key), title);
        args.putString(context.getString(R.string.video_player_permalink_key), permaLink);
        args.putString(context.getString(R.string.video_player_hls_url_key), hlsUrl);
        args.putString(context.getString(R.string.video_layer_film_id_key), filmId);
        args.putString(context.getString(R.string.video_player_ads_url_key), adsUrl);
        args.putBoolean(context.getString(R.string.video_player_request_ads_key), requestAds);
        args.putInt(context.getString(R.string.play_index_key), playIndex);
        args.putLong(context.getString(R.string.watched_time_key), watchedTime);
        args.putLong(context.getString(R.string.run_time_key), runtime);
        args.putString(context.getString(R.string.played_movie_image_url), imageUrl);
        args.putParcelableArrayList(context.getString(R.string.video_player_closed_caption_key), closedCaptionUrl);
        args.putBoolean(context.getString(R.string.video_player_is_trailer_key), isTrailer);
        args.putString(context.getString(R.string.video_player_content_rating_key), parentalRating);
        args.putBoolean(context.getString(R.string.free_content_key), freeContent);
        args.putSerializable("content_datum", contentDatum);
        args.putSerializable("series_parental_rating", seriesParentalRating);
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

        if (isDrmVideoEnabled) {
            args.putBoolean(context.getString(R.string.drm_enabled), isDrmVideoEnabled);
            args.putString(context.getString(R.string.license_url), licenseUrl);
            args.putString(context.getString(R.string.license_token), licenseToken);
        }

        appCMSPlayVideoFragment.setArguments(args);
        return appCMSPlayVideoFragment;
    }

    /*@Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mContext = activity;
        if (activity instanceof OnClosePlayerEvent) {
            onClosePlayerEvent = (OnClosePlayerEvent) activity;
        }
        if (activity instanceof VideoPlayerView.StreamingQualitySelector) {
            streamingQualitySelector = (VideoPlayerView.StreamingQualitySelector) activity;
        }
    }*/

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.mContext = context;
        if (context instanceof OnClosePlayerEvent) {
            onClosePlayerEvent = (OnClosePlayerEvent) context;
        }
        if (context instanceof VideoPlayerView.StreamingQualitySelector) {
            streamingQualitySelector = (VideoPlayerView.StreamingQualitySelector) context;
        }
        if (context instanceof VideoPlayerView.ClosedCaptionSelector) {
            closedCaptionSelector = (VideoPlayerView.ClosedCaptionSelector) context;
        }
        if (context instanceof VideoPlayerView.SeasonEpisodeSelctionEvent) {
            seasonEpisodeSelctionEvent = (VideoPlayerView.SeasonEpisodeSelctionEvent) context;
        }

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        if (args != null) {
            fontColor = args.getString(getString(R.string.video_player_font_color_key));
            title = args.getString(getString(R.string.video_player_title_key));
            permaLink = args.getString(getString(R.string.video_player_permalink_key));
            isTrailer = args.getBoolean(getString(R.string.video_player_is_trailer_key));
            hlsUrl = args.getString(getString(R.string.video_player_hls_url_key));
            filmId = args.getString(getString(R.string.video_layer_film_id_key));
            adsUrl = args.getString(getString(R.string.video_player_ads_url_key));
            shouldRequestAds = args.getBoolean(getString(R.string.video_player_request_ads_key));
            playIndex = args.getInt(getString(R.string.play_index_key));
            watchedTime = args.getLong(getString(R.string.watched_time_key));
            runtime = args.getLong(getString(R.string.run_time_key));
            imageUrl = args.getString(getString(R.string.played_movie_image_url));
            closedCaptionUrl = args.getParcelableArrayList(getString(R.string.video_player_closed_caption_key));
            primaryCategory = args.getString(getString(R.string.video_primary_category_key));
            parentalRating = args.getString(getString(R.string.video_player_content_rating_key));
            freeContent = args.getBoolean(getString(R.string.free_content_key));
            policyCookie = args.getString(getString(R.string.signed_policy_key));
            signatureCookie = args.getString(getString(R.string.signed_signature_key));
            keyPairIdCookie = args.getString(getString(R.string.signed_keypairid_key));
            contentDatum = (ContentDatum) args.getSerializable("content_datum");
            moduleApi = contentDatum.getModuleApi();
            seriesParentalRating = args.getString("series_parental_rating");
            seriesId = contentDatum.getGist().getSeriesId();
            createAllEpisodeList();
            Log.d(TAG, "ANAS: free " + freeContent);

            drmObj = new DRMObj();
            drmObj.isDrmVideoEnabled = args.getBoolean(getContext().getString(R.string.drm_enabled));
            if (drmObj.isDrmVideoEnabled) {
                drmObj.licenseToken = args.getString(getContext().getString(R.string.license_token));
                drmObj.licenseUrl = args.getString(getContext().getString(R.string.license_url));
            }
        }

        appCMSPresenter =
                ((AppCMSApplication) getActivity().getApplication())
                        .getAppCMSPresenterComponent()
                        .appCMSPresenter();
        if (contentTypeChecker == null)
            contentTypeChecker = new ContentTypeChecker(getActivity());

        sentBeaconPlay = (0 < playIndex && watchedTime != 0);
        beaconMsgTimeoutMsec = getActivity().getResources().getInteger(R.integer.app_cms_beacon_timeout_msec);
        beaconBufferingTimeoutMsec = getActivity().getResources().getInteger(R.integer.app_cms_beacon_buffering_timeout_msec);
        setFirebaseProgressHandling();
        parentScreenName = getActivity().getString(R.string.app_cms_beacon_video_player_parent_screen_name);
        try {
            mStreamId = appCMSPresenter.getStreamingId(title);
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
            mStreamId = filmId + appCMSPresenter.getCurrentTimeStamp();
        }
        setRetainInstance(true);
    }

    private void preparePlayer() {
        if (drmObj.isDrmVideoEnabled) {
            videoPlayerView.setDRMEnabled(drmObj.isDrmVideoEnabled);
            videoPlayerView.setLicenseUrl(drmObj.licenseUrl);
            videoPlayerView.setLicenseTokenDRM(drmObj.licenseToken);
        }
        videoPlayerView.setAppCMSPresenter(appCMSPresenter);
        videoPlayerView.setAdsUrl(adsUrl);
        videoPlayerView.init(getActivity());
        videoPlayerView.setSubtitleViewVisibility(false);
        videoPlayerView.getPlayer().setPlayWhenReady(true);
        videoPlayerView.setClosedCaptionEnabled(false);
        String promId=null;
        if(contentDatum!=null&&contentDatum.getContentDetails()!=null
                &&contentDatum.getContentDetails().getPromos()!=null&&
                contentDatum.getContentDetails().getPromos().size()>0&&
                contentDatum.getContentDetails().getPromos().get(0).getId() != null){
            promId=contentDatum.getContentDetails().getPromos().get(0).getId();
            videoPlayerView.setFilmId(promId);
            isPromo=true;
        }
        else
        videoPlayerView.setFilmId(filmId);
        videoPlayerView.getPlayerView().getSubtitleView()
                .setVisibility(appCMSPresenter.getClosedCaptionPreference()
                        ? VISIBLE
                        : GONE);
        videoPlayerView.preparePlayer();
        Log.i(TAG, "Playing video: " + hlsUrl);
            setTrailerCompletedCallback(new TrailerCompletedCallback() {
            @Override
            public void videoCompleted() {
                videoPlayerView.setAdsUrl(null);
                if (streamingQualitySelector.getVideoUrl() != null) {
                    streamingQualitySelector.setVideoUrl(hlsUrl);
                }
                videoPlayerView.enableController();
                Log.i(TAG, "Playing video: complete");
                videoPlayerView.setFilmId(filmId);
                videoPlayerView.preparePlayer();
                videoPlayerInfoContainer.setVisibility(VISIBLE);
            }

            @Override
            public void videoStarted() {
                Log.i(TAG, "Playing video: start");
            }
        });

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


        if (!contentDatum.getGist().isLiveStream()) {
            if (isFromError) {
                isFromError = false;
                videoPlayerView.setCurrentPosition(videoPlayerView.getPlayerCurrrentPosition());
            } else {
                videoPlayerView.setCurrentPosition(videoPlayTime * SECONDS_TO_MILLIS);
            }
        }

        if (CommonUtils.isOnePlusTV() && getActivity() != null && contentDatum != null) {
            HistorySyncUtils.sendBroadCastForPlayHistorySync(getActivity(), contentDatum);
        }


        videoPlayerView.setVideoContentData(contentDatum);
        videoPlayerView.createMessage(contentDatum);
        videoPlayerView.setOnPlayerStateChanged(new Action1<VideoPlayerView.PlayerState>() {
            @Override
            public void call(VideoPlayerView.PlayerState playerState) {
                String text = "";
                switch (playerState.getPlaybackState()) {
                    case ExoPlayer.STATE_BUFFERING:
                        Log.d(TAG, "Video STATE_BUFFERING");
                        String bufferingText = appCMSPresenter.getLocalisedStrings().getLiveStreamingText();
                        text += bufferingText;

                        playBackStateLayout.setVisibility(VISIBLE);

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
                    case ExoPlayer.STATE_READY:
                        Log.d(TAG, "Video STATE_READY");
                        text += "";


                        if (beaconBufferingThread != null) {
                            beaconBufferingThread.sendBeaconBuffering = false;
                        }
                        if (beaconMessageThread != null) {
                            beaconMessageThread.sendBeaconPing = true;
                            if (!beaconMessageThread.isAlive()) {
                                beaconMessageThread.start();
                                mProgressHandler.post(mProgressRunnable);

                                mTotalVideoDuration = videoPlayerView.getDuration() / 1000;
                                mTotalVideoDuration -= mTotalVideoDuration % 4;
                            }
                            if (!sentBeaconFirstFrame && appCMSPresenter.getAppCMSMain().getFeatures().isEnableQOS()) {
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
                                        false);
                                sentBeaconFirstFrame = true;
                            }
                            if (!sentHistoryAPICallAtVideoLaunch && !contentDatum.getGist().isLiveStream()) {
                                updateWatchedHistory();
                                sentHistoryAPICallAtVideoLaunch = true;
                            }
                        }
                        playBackStateLayout.setVisibility(GONE);

                        break;
                    case ExoPlayer.STATE_ENDED:
                        Log.d(TAG, "Video STATE_ENDED");
                        if (trailerCompletedCallback != null && isPromo) {
                            trailerCompletedCallback.videoCompleted();
                            isPromo = false;
                        } else {
                            playBackStateLayout.setVisibility(GONE);
                            if (onClosePlayerEvent != null && isTrailer) {
                                videoPlayerView.releasePlayer();
                                if (onClosePlayerEvent != null) {
                                    onClosePlayerEvent.closePlayer();
                                }
                                return;
                            }
                        }
                        if (onClosePlayerEvent != null && playerState.isPlayWhenReady()) {
                            // tell the activity that the movie is finished
                            //sedn the history when complete play.
                            if (!isTrailer
                                    && !contentDatum.getGist().isLiveStream()
                                    && videoPlayerView != null
                                    && !sentHistoryAPICallAtVideoFinished) {
                                updateWatchedHistory();
                                sentHistoryAPICallAtVideoFinished = true;
                            }

                            if (shouldAutoPlay()) {
                                onClosePlayerEvent.onMovieFinished();
                            } else {
                                if (onClosePlayerEvent != null) {
                                    onClosePlayerEvent.closePlayer();
                                }
                            }
                        }
                        break;
                    default:
                        text += "";
                        playBackStateLayout.setVisibility(GONE);
                        break;
                }
                playBackStateTextView.setText(text);
            }
        });
        videoPlayerView.setOnPlayerControlsStateChanged(new Action1<Integer>() {
            @Override
            public void call(Integer visiblity) {
                if (visiblity == GONE) {
                    videoPlayerInfoContainer.setVisibility(GONE);
                } else if (visiblity == VISIBLE) {
                    videoPlayerInfoContainer.setVisibility(VISIBLE);
                }
            }
        });
        videoPlayerView.setOnClosedCaptionButtonClicked(isChecked -> {
            videoPlayerView.getPlayerView().getSubtitleView()
                    .setVisibility(isChecked ? VISIBLE : GONE);
            appCMSPresenter.setClosedCaptionPreference(isChecked);
        });
    }

    private boolean shouldAutoPlay() {
        boolean isPerVideo = appCMSPresenter.getAppCMSMain().getFeatures().getFreePreview().isPerVideo();
        if (isPerVideo && !appCMSPresenter.isUserSubscribed() && !freeContent) {
            return false;
        }
        return true;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        appCMSPresenter.sendAppsFlyerPageViewEvent("Video Player", null);
        View rootView = inflater.inflate(R.layout.fragment_video_player_tv, container, false);
        ButterKnife.bind(this, rootView);
        videoPlayerMainContainer =
                (RelativeLayout) rootView.findViewById(R.id.app_cms_video_player_main_container);
        videoPlayerInfoContainer =
                (LinearLayout) rootView.findViewById(R.id.app_cms_video_player_info_container);

        backgroundView = rootView.findViewById(R.id.background_view);

        videoPlayerTitleView = (TextView) rootView.findViewById(R.id.app_cms_video_player_title_view);
        if (!TextUtils.isEmpty(title)) {
            videoPlayerTitleView.setText(title);
        }
        if (!TextUtils.isEmpty(fontColor)) {
            videoPlayerTitleView.setTextColor(getResources().getColor(android.R.color.white)/*Color.parseColor(fontColor)*/);
        }

        videoPlayerViewDoneButton = (Button) rootView.findViewById(R.id.app_cms_video_player_done_button);
        videoPlayerViewDoneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onClosePlayerEvent != null) {
                    videoPlayerView.releasePlayer();
                    if (onClosePlayerEvent != null) {
                        onClosePlayerEvent.closePlayer();
                    }
                }
            }
        });

        videoPlayerViewDoneButton.setTextColor(Color.parseColor(fontColor));

        videoPlayerInfoContainer.bringToFront();

        videoPlayerView = (VideoPlayerView) rootView.findViewById(R.id.app_cms_video_player_container);
        videoPlayerView.setAppCMSPresenter(appCMSPresenter);
        videoPlayerView.applyTimeBarColor(Color.parseColor(Utils.getFocusBorderColor(getContext(), appCMSPresenter)));
        videoPlayerView.setOnBeaconAdsEvent(this);
        if (streamingQualitySelector != null) {
            videoPlayerView.setStreamingQualitySelector(streamingQualitySelector);
        }
        if (closedCaptionSelector != null) {
            videoPlayerView.setClosedCaptionsSelector(closedCaptionSelector);
        }
        videoPlayerView.getPlayerView().hideController();
        videoPlayerInfoContainer.setVisibility(View.INVISIBLE);
        videoPlayerView.setSeasonEpisodeSelctionEvent(seasonEpisodeSelctionEvent);

        playBackStateLayout = (RelativeLayout) rootView.findViewById(R.id.playback_state_layout);
        playBackStateTextView = (TextView) rootView.findViewById(R.id.playback_state_text);
        playBackStateTextView.setTextColor(getResources().getColor(android.R.color.white)/*Color.parseColor(fontColor)*/);
        progressBar = (ProgressBar) rootView.findViewById(R.id.progress_bar);

        progressBar.getIndeterminateDrawable().
                setColorFilter(Color.parseColor(Utils.getFocusBorderColor(getActivity(), appCMSPresenter)),
                        PorterDuff.Mode.MULTIPLY
                );

        CookieManager cookieManager = new CookieManager();
        CookieHandler.setDefault(cookieManager);

        if (!TextUtils.isEmpty(policyCookie) &&
                !TextUtils.isEmpty(signatureCookie) &&
                !TextUtils.isEmpty(keyPairIdCookie)) {

            videoPlayerView.setPolicyCookie(policyCookie);
            videoPlayerView.setSignatureCookie(signatureCookie);
            videoPlayerView.setKeyPairIdCookie(keyPairIdCookie);
        }

        if (videoPlayerView.getPlayerView().getController() != null) {
            videoPlayerView.getPlayerView().getController().setPlayingLive(contentDatum.getGist().isLiveStream());
        }

        initViewForCRW(rootView);

        initMathProblemView(rootView);

        boolean mathProblemRequired = false;
        try {
            if (seriesParentalRating != null) {
                mathProblemRequired = (seriesParentalRating.matches(".*\\d.*") &&
                        Integer.parseInt(seriesParentalRating.split("\\+")[0]) >= 5 /*&&
                            Integer.parseInt(seriesParentalRating.split("\\+")[0]) <= 11*/);
            }
            if (!mathProblemRequired) {
                mathProblemRequired = contentDatum.getParentalRating().matches(".*\\d.*") &&
                        Integer.parseInt(contentDatum.getParentalRating().split("\\+")[0]) >= 5 /*&&
                    Integer.parseInt(contentDatum.getParentalRating().split("\\+")[0]) <= 11*/;
            }
        } catch (Exception e) {
            mathProblemRequired = false;
        }

        if (mathProblemRequired
                && appCMSPresenter.getAppCMSMain().getFeatures().isMathProblemEnabled()
                && appCMSPresenter.isUserLoggedIn()) {
            createAndShowMathProblemView();
        } else {
            try {
                createContentRatingView();
            } catch (Exception e) {
                preparePlayer();
                startEntitlementCheckTimer();
            }
        }

        if (!sentBeaconPlay) {
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
                    false);
            sentBeaconPlay = true;
            mStartBufferMilliSec = new Date().getTime();

            appCMSPresenter.sendGaEvent(getString(R.string.play_video_action),
                    getString(R.string.play_video_category),
                    (title != null && !TextUtils.isEmpty(title)) ? title : filmId);
            appCMSPresenter.sendPlayStartedEvent(contentDatum);
        }

        beaconMessageThread = new BeaconPing(beaconMsgTimeoutMsec,
                appCMSPresenter,
                filmId,
                permaLink,
                isTrailer,
                parentScreenName,
                videoPlayerView,
                mStreamId,
                contentDatum);

        beaconBufferingThread = new BeaconBuffer(beaconBufferingTimeoutMsec,
                appCMSPresenter,
                filmId,
                permaLink,
                parentScreenName,
                videoPlayerView,
                mStreamId,
                contentDatum);

        mBeaconHandler = new BeaconHandler(videoPlayerView.getPlayer().getApplicationLooper());
        mBeaconRunnable = new BeaconRunnable(beaconMessageThread,
                beaconBufferingThread,
                mBeaconHandler,
                videoPlayerView);
        mBeaconHandler.handle(mBeaconRunnable);

        hideControlsForLiveStream(contentDatum);
        setPrevNextBackground();
        AppsFlyerUtils.filmViewingEvent(getContext(), primaryCategory, filmId, appCMSPresenter);
        return rootView;
    }

    public boolean isSubscriptionDialogVisible = false;

    private void startEntitlementCheckTimer() {
        System.out.println("secsViewed startEntitlementCheckTimer +++++++++++++++++++++++");
        if (entitlementCheckTimer != null || entitlementCheckTimerTask != null) {
            /*That means timer is already running and no need to create a new timer*/
            return;
        }
        System.out.println("secsViewed startEntitlementCheckTimer __________________");

        if (contentDatum != null && contentDatum.getSubscriptionPlans() != null
                && (contentTypeChecker.isContentAVOD(contentDatum.getSubscriptionPlans())
                || contentTypeChecker.isContentFree(contentDatum.getSubscriptionPlans()))) return;

        double entitlementCheckMultiplier = 5;
        boolean isPerVideo = false, isFreePreview = false;
        entitlementCheckCancelled = false;

        AppCMSMain appCMSMain = appCMSPresenter.getAppCMSMain();
        if (appCMSMain != null &&
                appCMSMain.getFeatures() != null &&
                appCMSMain.getFeatures().getFreePreview() != null &&
                appCMSMain.getFeatures().getFreePreview().isFreePreview()) {
            if (appCMSMain.getFeatures().getFreePreview().getLength() != null &&
                    appCMSMain.getFeatures().getFreePreview().getLength().getUnit()
                            .equalsIgnoreCase("Minutes")) {
                try {
                    isFreePreview = appCMSMain.getFeatures().getFreePreview().isFreePreview();
                    entitlementCheckMultiplier = Double.parseDouble(appCMSMain.getFeatures()
                            .getFreePreview().getLength().getMultiplier());
                } catch (Exception e) {
                    //Log.e(TAG, "Error parsing free preview multiplier value: " + e.getMessage());
                }
            }
            isPerVideo = appCMSMain.getFeatures().getFreePreview().isPerVideo();
        } else {
            entitlementCheckMultiplier = 0; //isFreePreview is false that means we have to show preview dialog immediate.
        }

        final int maxPreviewSecs = (int) (entitlementCheckMultiplier * 60);
        boolean finalIsPerVideo = isPerVideo;

        if (isPerVideo || isFreePreview) {
            entitlementCheckTimerTask = new TimerTask() {
                @Override
                public void run() {
                    if (!entitlementCheckCancelled) {
                        int secsViewed;
                        if (finalIsPerVideo) {
                            secsViewed = (int) videoPlayerView.getPlayer().getCurrentPosition() / 1000;
                        } else {
                            secsViewed = (int) (appCMSPresenter.getUserFreePlayTimePreference() / 1000);
                        }

                        Log.d(TAG, "secsViewed  is = " + secsViewed + " totalPreviewTime = " + maxPreviewSecs);

                        if ((maxPreviewSecs < secsViewed || maxPreviewSecs < playedVideoSecs) && !appCMSPresenter.isUserSubscribed()) {

                            if (getActivity() != null) {
                                getActivity().runOnUiThread(() -> {
                                    pauseVideo();
                                    if (videoPlayerView != null) {
                                        videoPlayerView.disableController();
                                    }
                                    videoPlayerInfoContainer.setVisibility(View.INVISIBLE);
                                    if (contentDatum != null && contentDatum.getSubscriptionPlans() != null) {
                                        //showPreviewDialog();
                                        appCMSPresenter.openEntitlementScreen(contentDatum, false);
                                    } else {
                                        openSubscriptionDialog();
                                    }
                                    entitlementCheckCancelled = true;
                                });
                            }

                        }

                    }
                    if (videoPlayerView.getPlayer().getPlayWhenReady()
                            && videoPlayerView.getPlayer().getPlaybackState() == Player.STATE_READY) {
                        playedVideoSecs++;
                    }
                    if (!finalIsPerVideo && null != videoPlayerView && null != videoPlayerView.getPlayer() &&
                            videoPlayerView.getPlayer().getPlayWhenReady()) {
                        /*if perVideo is false and the player is not playing*/
                        appCMSPresenter.setUserFreePlayTimePreference(appCMSPresenter.getUserFreePlayTimePreference() + 1000);
                    }
                }
            };
            entitlementCheckTimer = new Timer();
            entitlementCheckTimer.schedule(entitlementCheckTimerTask, 1000, 1000);
        }
    }

    private void openSubscriptionDialog() {
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
                requireContext(),
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
            Utils.pageLoading(true, requireActivity());
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
            if (appCMSPresenter.getPlatformType().equals(AppCMSPresenter.PlatformType.TV)) {
                appCMSPresenter.openTVErrorDialog(appCMSPresenter.getLocalisedStrings().getGuestUserSubsctiptionMsgText(), "", false);
            } else {
                Utils.pageLoading(true, requireActivity());
                MetaPage subscriptionPage = appCMSPresenter.getSubscriptionPage();
                if (subscriptionPage != null) {
                    String title = getString(R.string.view_plans_label);
                    if (appCMSPresenter.getGenericMessages() != null
                            && appCMSPresenter.getGenericMessages().getViewPlansCta() != null) {
                        title = appCMSPresenter.getGenericMessages().getViewPlansCta();
                    }
                    appCMSPresenter.navigateToTVPage(
                            subscriptionPage.getPageId(),
                            title,
                            null,
                            false,
                            Uri.EMPTY,
                            false,
                            true, false, true, false, false);
                    appCMSPresenter.setViewPlanPageOpenFromADialog(true);
                }
            }
        });

        newFragment.setOnBackKeyListener(s -> {
            newFragment.dismiss();
            requireActivity().finish();
        });
    }

    public void cancelTimer() {
        if (null != entitlementCheckTimerTask) {
            entitlementCheckTimerTask.cancel();
            entitlementCheckTimerTask = null;
        }
        if (null != entitlementCheckTimer) {
            entitlementCheckTimer.cancel();
            entitlementCheckTimer = null;
        }
    }


    public void showPreviewDialog() {
        String dialogMessage = appCMSPresenter.getLocalisedStrings().getWaysToWatchMessageText();
        String dialogTitle = appCMSPresenter.getLocalisedStrings().getWaysToWatchText();
        ;
        String positiveButtonText = appCMSPresenter.getLocalisedStrings().getSignInText();
        String negativeButtonText = appCMSPresenter.getLocalisedStrings().getBecomeMemberText();
        String neutralButtonText = null;
        String extraButtonText = null;

        if (appCMSPresenter.isUserLoggedIn() && appCMSPresenter.getAppPreference().getLoggedInUserEmail() != null) {
            positiveButtonText = "";
        }

        if(contentDatum.getSubscriptionPlans()!=null
                && contentDatum.getModuleApi() != null && contentDatum.getModuleApi().getContentData() != null
                && contentDatum.getModuleApi().getContentData().size() > 0
                && contentTypeChecker.isContentSVOD_TVE(contentDatum.getSubscriptionPlans(), contentDatum.getModuleApi().getContentData().get(0).getMonetizationModels())
                && (!appCMSPresenter.isUserLoggedInByTVProvider() || !appCMSPresenter.isUserSubscribed())){
            positiveButtonText = "";
            negativeButtonText = "";
            neutralButtonText = appCMSPresenter.getLocalisedStrings().getChooseTVProviderText();
        }

        if(contentDatum.getSubscriptionPlans()!=null
                &&contentTypeChecker.isContentTVE(contentDatum.getSubscriptionPlans()) && !appCMSPresenter.isUserLoggedInByTVProvider()){
            neutralButtonText = appCMSPresenter.getLocalisedStrings().getChooseTVProviderText();
        }else if(contentDatum.getSubscriptionPlans()!=null
                && contentDatum.getModuleApi() != null && contentDatum.getModuleApi().getContentData() != null
                && contentDatum.getModuleApi().getContentData().size() > 0
                &&contentTypeChecker.isContentSVOD_TVOD(contentDatum.getSubscriptionPlans(),contentDatum.getModuleApi().getContentData().get(0).getMonetizationModels())){
            extraButtonText = appCMSPresenter.getLocalisedStrings().getOwnText();
        }

        PreviewDialogFragment newFragment = Utils.getPreviewDialogFragment(
                getActivity(),
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
        isSubscriptionDialogVisible = true;
        newFragment.setOnPositiveButtonClicked(new Action1<String>() {
            @Override
            public void call(String s) {
                boolean showParentalGateView = appCMSPresenter.getAppCMSMain().getCompliance() != null
                        && appCMSPresenter.getAppCMSMain().getCompliance().isCoppa();

                Utils.pageLoading(true, getActivity());
                isSubscriptionDialogVisible = false;
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
            isSubscriptionDialogVisible = false;
            if (contentDatum != null && contentDatum.getSubscriptionPlans() != null) {
                appCMSPresenter.navigateToContentSubscription(contentDatum.getSubscriptionPlans());
            } else {
                Utils.pageLoading(true, getActivity());
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

    public void pauseVideo() {
       /* if (shouldRequestAds && adsManager != null && isAdDisplayed) {
            adsManager.pause();
        } else {
            tvVideoPlayerView.pausePlayer();
        }*/

        videoPlayerView.pausePlayer();

        if (beaconMessageThread != null) {
            beaconMessageThread.sendBeaconPing = false;
        }
        if (beaconBufferingThread != null) {
            beaconBufferingThread.sendBeaconBuffering = false;
        }
    }

    public void resumeVideo() {
        if (videoPlayerView != null) {
            videoPlayerView.enableController();
        }
        videoPlayerInfoContainer.setVisibility(VISIBLE);
        showController();
        videoPlayerView.startPlayer(playWhenReady);
        //tvVideoPlayerView.resumePlayer();
        if (beaconMessageThread != null) {
            beaconMessageThread.sendBeaconPing = true;
        }
        if (beaconBufferingThread != null) {
            beaconBufferingThread.sendBeaconBuffering = true;
        }
        Log.d(TAG, "Resuming playback");
    }

    @Override
    public void onResume() {
        if (drmObj.isDrmVideoEnabled) {
            videoPlayerView.setDRMEnabled(drmObj.isDrmVideoEnabled);
            videoPlayerView.setLicenseUrl(drmObj.licenseUrl);
            videoPlayerView.setLicenseTokenDRM(drmObj.licenseToken);
        }
        videoPlayerView.setListener(this);
        videoPlayerView.setOnPlayNextListener(this);

        appCMSPresenter.setCancelAllLoads(false);
        if (!isSubscriptionDialogVisible) {
            resumeVideo();
            Log.d(TAG, "Resuming playback");
        }

        getActivity().registerReceiver(networkReciever, new IntentFilter("android.net.conn.CONNECTIVITY_CHANGE"));

        super.onResume();
    }

    @Override
    public void onPause() {
        playWhenReady = videoPlayerView.getPlayer().getPlayWhenReady();
        videoPlayerView.pausePlayer();
        getActivity().unregisterReceiver(networkReciever);
        videoPlayerView.setListener(null);
        super.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
        if (!isTrailer
                && !contentDatum.getGist().isLiveStream()
                && videoPlayerView != null
                && !sentHistoryAPICallAtVideoFinished) {
            updateWatchedHistory();
            sentHistoryAPICallAtVideoFinished = true;
        }
    }

    @Override
    public void onDestroyView() {
        videoPlayerView.releasePlayer();
        onClosePlayerEvent = null;

        segmentId = null;
        super.onDestroyView();

        videoPlayerView.setOnPlayerStateChanged(null);
        videoPlayerView.releasePlayer();

        if (null != beaconMessageThread) {
            beaconMessageThread.sendBeaconPing = false;
            beaconMessageThread.runBeaconPing = false;
            beaconMessageThread.videoPlayerView = null;
            beaconMessageThread = null;
        }

        if (null != beaconBufferingThread) {
            beaconBufferingThread.sendBeaconBuffering = false;
            beaconBufferingThread.runBeaconBuffering = false;
            beaconBufferingThread.videoPlayerView = null;
            beaconBufferingThread = null;
        }
        if (mProgressHandler != null) {
            mProgressHandler.removeCallbacks(mProgressRunnable);
            mProgressHandler = null;
        }
        onClosePlayerEvent = null;
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

    private void sendProgressAnalyticEvents(long progressPercent) {
        Bundle bundle = new Bundle();
        bundle.putString(FIREBASE_VIDEO_ID_KEY, filmId);
        bundle.putString(FIREBASE_VIDEO_NAME_KEY, title);
        bundle.putString(FIREBASE_PLAYER_NAME_KEY, FIREBASE_PLAYER_NATIVE);
        bundle.putString(FIREBASE_MEDIA_TYPE_KEY, FIREBASE_MEDIA_TYPE_VIDEO);
        bundle.putString(FIREBASE_SERIES_ID_KEY, seriesId);
        bundle.putString(FIREBASE_SERIES_NAME_KEY, contentDatum.getSeriesName());
        bundle.putLong("content_seconds_watched", videoPlayerView.getDuration());
        String platform;
        if (com.viewlift.Utils.isFireTVDevice(appCMSPresenter.getCurrentContext())) {
            platform = mContext.getString(R.string.app_cms_query_param_amazon_platform);
        } else if (appCMSPresenter.getPlatformType().equals(AppCMSPresenter.PlatformType.TV)) {
            platform = mContext.getString(R.string.app_cms_query_param_android_tv);
        } else {
            platform = mContext.getString(R.string.app_cms_query_param_android_platform);
        }
        bundle.putString("platform_name", platform);
        bundle.putString("network_name", getActivity().getApplicationInfo().loadLabel(getActivity().getPackageManager()).toString());
        String videoType = "free";
        if (!contentDatum.getGist().isFree())
            videoType = "restricted";
        bundle.putString("video_asset_status", videoType);
        if (appCMSPresenter.getAppPreference().getTvProvider() != null)
            bundle.putString("tvprovider_name", appCMSPresenter.getAppPreference().getTvProvider());

        //Logs an app event.
        if (progressPercent == 0 && !isStreamStart) {
            appCMSPresenter.getmFireBaseAnalytics().logEvent(FIREBASE_STREAM_START, bundle);
            isStreamStart = true;
        }

        if (!isStreamStart) {
            appCMSPresenter.getmFireBaseAnalytics().logEvent(FIREBASE_STREAM_START, bundle);
            isStreamStart = true;
        }

        if (progressPercent >= 25 && progressPercent < 50 && !isStream25) {
            if (!isStreamStart) {
                appCMSPresenter.getmFireBaseAnalytics().logEvent(FIREBASE_STREAM_START, bundle);
                isStreamStart = true;
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
    }

    public boolean showController() {
        if (null != videoPlayerView) {
            //AppCMSSimpleExoPlayerView playerView =  videoPlayerView.getPlayerView();
            AppCMSPlayerView playerView = videoPlayerView.getPlayerView();
            if (null != playerView) {
                if (null != playerView.getPlayer()) {
                    if (playerView.getPlayer().getPlayWhenReady() && !playerView.getPlayer().isPlayingAd()) {
                        playerView.showController();
                    }
                }
            }
        }
        return true;
    }

    BroadcastReceiver networkReciever = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();
            if (action != null && action.equalsIgnoreCase("android.net.conn.CONNECTIVITY_CHANGE")) {
                if (appCMSPresenter.isNetworkConnected()) {
                    if (networkConnect) {
                        networkDisconnect = true;
                        if (!TextUtils.isEmpty(hlsUrl)) {
                            videoPlayerView.sendPlayerPosition(videoPlayerView.getPlayer().getCurrentPosition());
                            videoPlayerView.setUriOnConnection();
                        }
                    }
                } else {
                    if (networkDisconnect) {
                        networkConnect = true;
                        networkDisconnect = false;
                        Bundle bundle = new Bundle();
                        bundle.putBoolean(getString(R.string.retry_key), false);
                        bundle.putBoolean(getString(R.string.register_internet_receiver_key), true);
                        FragmentTransaction ft = getFragmentManager().beginTransaction();
                        errorActivityFragment = AppCmsTvErrorFragment.newInstance(
                                bundle);
                        errorActivityFragment.setErrorListener(AppCMSPlayVideoFragment.this);
                        errorActivityFragment.show(ft, getString(R.string.error_dialog_fragment_tag));
                    }
                }
            }
        }
    };

    @Override
    public void onErrorScreenClose(boolean closeActivity) {
        errorActivityFragment.dismiss();
    }

    @Override
    public void onRetry(Bundle bundle) {

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

        appCMSPresenter.sendBeaconMessage(filmId,
                permaLink,
                parentScreenName,
                videoPlayerView.getCurrentPosition(),
                false,
                event,
                "Video",
                String.valueOf(videoPlayerView.getBitrate()),
                String.valueOf(videoPlayerView.getHeight()),
                String.valueOf(videoPlayerView.getWidth()),
                mStreamId,
                0d,
                0,
                false);
        videoPlayerView.releasePlayer();
        if (!TextUtils.isEmpty(message)) {
            Toast.makeText(getActivity(), message, Toast.LENGTH_LONG).show();
        }

        if (onClosePlayerEvent != null)
            onClosePlayerEvent.closePlayer();
    }

    private boolean isFromError = false;

    @Override
    public void playerError(ExoPlaybackException ex) {
        try {
            Log.d("Player Error", ex.getCause().getCause().toString());
            String dialogMessage = appCMSPresenter.getLanguageResourcesFile().getUIresource(getString(R.string.drm_header));
            String dialogTitle = appCMSPresenter.getLanguageResourcesFile().getUIresource(getString(R.string.drm_title));
            String backCTA = appCMSPresenter.getLanguageResourcesFile().getUIresource(getString(R.string.back));

            if (drmObj.isDrmVideoEnabled && ex instanceof ExoPlaybackException && ex.getCause() instanceof DrmSession.DrmSessionException) {
                if (filmId != null) {
                    playBackStateLayout.setVisibility(VISIBLE);
                    if (appCMSPresenter.isUserLoggedIn()) {
                        appCMSPresenter.refreshIdentity(appCMSPresenter.getRefreshToken(), new Action0() {
                            @Override
                            public void call() {
                                appCMSPresenter.refreshVideoData(filmId,
                                        updatedContentDatum -> {
                                            pauseVideo();
                                            videoPlayerView.releasePlayer();
                                            drmObj.licenseToken = updatedContentDatum.getStreamingInfo().getVideoAssets().getWideVine().getLicenseToken();
                                            drmObj.licenseUrl = updatedContentDatum.getStreamingInfo().getVideoAssets().getWideVine().getLicenseUrl();
                                            if (streamingQualitySelector.getVideoUrl() != null) {
                                                streamingQualitySelector.setVideoUrl(updatedContentDatum.getStreamingInfo().getVideoAssets().getWideVine().getUrl());
                                            }
                                            final Handler handler = new Handler();
                                            handler.postDelayed(new Runnable() {
                                                @Override
                                                public void run() {
                                                    //Do something after 100ms
                                                    playBackStateLayout.setVisibility(GONE);
                                                    isFromError = true;
                                                    preparePlayer();
                                                }
                                            }, 1000);
                                        }, null, false, false, contentDatum);
                            }
                        });
                    } else {
                        appCMSPresenter.refreshVideoData(filmId,
                                updatedContentDatum -> {
                                    pauseVideo();
                                    videoPlayerView.releasePlayer();
                                    drmObj.licenseToken = updatedContentDatum.getStreamingInfo().getVideoAssets().getWideVine().getLicenseToken();
                                    drmObj.licenseUrl = updatedContentDatum.getStreamingInfo().getVideoAssets().getWideVine().getLicenseUrl();
                                    final Handler handler = new Handler();
                                    handler.postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            //Do something after 100ms
                                            playBackStateLayout.setVisibility(GONE);
                                            isFromError = true;
                                            preparePlayer();
                                        }
                                    }, 1000);
                                }, null, false, false, contentDatum);
                    }
                }
            } else {
                ClearDialogFragment newFragment = Utils.getClearDialogFragment(
                        getActivity(),
                        appCMSPresenter,
                        getResources().getDimensionPixelSize(R.dimen.text_clear_dialog_width),
                        getResources().getDimensionPixelSize(R.dimen.text_add_to_watchlist_sign_in_dialog_height),
                        dialogTitle,
                        dialogMessage,
                        backCTA,
                        getString(R.string.blank_string),
                         14
                );
                newFragment.setOnPositiveButtonClicked(new Action1<String>() {
                    @Override
                    public void call(String s) {
                        if (onClosePlayerEvent != null)
                            onClosePlayerEvent.closePlayer();
                    }
                });
            }


        } catch (Exception e) {
            Log.d("Player Error", e.getMessage().toString());
        }


//        if (drmObj.isDrmVideoEnabled && ex instanceof ExoPlaybackException && ex.getCause() instanceof DrmSession.DrmSessionException && ex.getCause().getCause() instanceof KeysExpiredException) {
//            if (filmId != null) {
//                if (appCMSPresenter.isUserLoggedIn()) {
//                    appCMSPresenter.refreshIdentity(appPreference.getRefreshToken(), new Action0() {
//                        @Override
//                        public void call() {
//                            appCMSPresenter.refreshVideoData(filmId,
//                                    updatedContentDatum -> {
//                                        drmObj.licenseToken=updatedContentDatum.getStreamingInfo().getVideoAssets().getWideVine().getLicenseToken();
//                                        drmObj.licenseUrl=updatedContentDatum.getStreamingInfo().getVideoAssets().getWideVine().getLicenseUrl();
//                                        videoPlayerView.releasePlayer();
//                                        final Handler handler = new Handler();
//                                        handler.postDelayed(new Runnable() {
//                                            @Override
//                                            public void run() {
//                                                //Do something after 100ms
//                                                isFromError=true;
//                                                onResume();
//                                            }
//                                        }, 100);
//                                    }, null, false, false);
//                        }
//                    });
//                }else {
//                    appCMSPresenter.refreshVideoData(filmId,
//                            updatedContentDatum -> {
//                                drmObj.licenseToken=updatedContentDatum.getStreamingInfo().getVideoAssets().getWideVine().getLicenseToken();
//                                drmObj.licenseUrl=updatedContentDatum.getStreamingInfo().getVideoAssets().getWideVine().getLicenseUrl();
//                                final Handler handler = new Handler();
//                                handler.postDelayed(new Runnable() {
//                                    @Override
//                                    public void run() {
//                                        //Do something after 100ms
//                                        isFromError=true;
//                                        onResume();
//                                    }
//                                }, 100);
//                            }, null, false, false);
//                }
//            }
//        }


    }

    private void initMathProblemView(View rootView) {
        mathProblemViewContainer = rootView.findViewById(R.id.app_cms_math_problem_view_main_container);
        mathProblemViewContainer.setBackgroundColor(appCMSPresenter.getGeneralBackgroundColor());
        mathProblemWatchNowButton = rootView.findViewById(R.id.app_cms_math_submit_answer_button);
        mathProblemWrongAnswerTextView = rootView.findViewById(R.id.app_cms_math_wrong_answer_view);
        mathProblemTextView = rootView.findViewById(R.id.app_cms_math_problem_view);
        mathProblemMessageView = rootView.findViewById(R.id.app_cms_math_problem_message_view);
        mathProblemSubtextView = rootView.findViewById(R.id.app_cms_math_problem_subtext_view);
        mathProblemAnswerEditTextView = rootView.findViewById(R.id.app_cms_math_problem_answer_field_view);

        mathProblemMessageView.setText(appCMSPresenter.getLocalisedStrings().getShowForBigKidsMessage());
        mathProblemWatchNowButton.setText(appCMSPresenter.getLocalisedStrings().getWatchNowCtaNbc());
        mathProblemSubtextView.setText(appCMSPresenter.getLocalisedStrings().getAnswerMathProblemMessage());
        mathProblemWrongAnswerTextView.setText(appCMSPresenter.getLocalisedStrings().getFindAdultForHelpMessage());


        mathProblemMessageView.setTypeface(Utils.getSpecificTypeface(
                mContext,
                appCMSPresenter,
                mContext.getString(R.string.app_cms_page_font_regular_key)));

        mathProblemWatchNowButton.setTypeface(Utils.getSpecificTypeface(
                mContext,
                appCMSPresenter,
                mContext.getString(R.string.app_cms_page_font_regular_key)));

        mathProblemWrongAnswerTextView.setTypeface(Utils.getSpecificTypeface(
                mContext,
                appCMSPresenter,
                mContext.getString(R.string.app_cms_page_font_regular_key)));

        mathProblemTextView.setTypeface(Utils.getSpecificTypeface(
                mContext,
                appCMSPresenter,
                mContext.getString(R.string.app_cms_page_font_regular_key)));

        mathProblemSubtextView.setTypeface(Utils.getSpecificTypeface(
                mContext,
                appCMSPresenter,
                mContext.getString(R.string.app_cms_page_font_regular_key)));

        mathProblemAnswerEditTextView.setTypeface(Utils.getSpecificTypeface(
                mContext,
                appCMSPresenter,
                mContext.getString(R.string.app_cms_page_font_regular_key)));

        Component component = new Component();
        component.setFontFamily(getString(R.string.app_cms_page_font_family_key));
        component.setFontWeight(getString(R.string.app_cms_page_font_semibold_key));
        component.setBorderColor("#ffffff");
        component.setBorderWidth(4);
        Layout layout = new Layout();
        FireTV fireTVLayout = new FireTV();
        fireTVLayout.setHeight("50");
        fireTVLayout.setWidth("250");
        layout.setTv(fireTVLayout);
        component.setLayout(layout);
        mathProblemWatchNowButton.setTextColor(Utils.getButtonTextColorDrawable(
                CommonUtils.getColor(getActivity(), Integer.toHexString(ContextCompat.getColor(getActivity(),
                        R.color.btn_color_with_opacity))),
                CommonUtils.getColor(getActivity(), Integer.toHexString(ContextCompat.getColor(getActivity(),
                        android.R.color.white))), appCMSPresenter
        ));
        String focusColor = Utils.getFocusColor(getActivity(), appCMSPresenter);

        mathProblemWatchNowButton.setBackground(Utils.setButtonBackgroundSelector(getActivity(),
                Color.parseColor(focusColor != null ? focusColor : "#000000"),
                component,
                appCMSPresenter));

        mathProblemWatchNowButton.setTypeface(Utils.getTypeFace(getActivity(), appCMSPresenter, component));
    }

    private void createAndShowMathProblemView() {
        mathProblemTextView.setText(createProblem());
        mathProblemAnswerEditTextView.addTextChangedListener(answerViewTextWatcher);
        mathProblemWatchNowButton.setOnClickListener(mathProblemWatchNowClickListener);
        mathProblemViewContainer.setVisibility(VISIBLE);
        videoPlayerMainContainer.setVisibility(GONE);
    }

    private String createProblem() {
        Random r = new Random();
        int low = 1;
        int high = 20;
        firstNumber = r.nextInt(high - low) + low;
        secondNumber = r.nextInt(high - low) + low;

        return Integer.toString(firstNumber).concat(" + ").concat(Integer.toString(secondNumber));
    }

    private View.OnClickListener mathProblemWatchNowClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            if (mathProblemAnswerEditTextView.getText().length() < 1) {
                return;
            }

            if (checkMathProblemResult()) {
                mathProblemViewContainer.setVisibility(GONE);
                videoPlayerMainContainer.setVisibility(VISIBLE);
                preparePlayer();
                startEntitlementCheckTimer();
            } else {
                mathProblemWrongAnswerTextView.setVisibility(VISIBLE);
                /*new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mathProblemAnswerEditTextView.setText("");
                        mathProblemWrongAnswerTextView.setVisibility(View.INVISIBLE);
                    }
                }, 5000);*/
            }
        }
    };

    private TextWatcher answerViewTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            mathProblemWrongAnswerTextView.setVisibility(View.INVISIBLE);
        }
    };

    private boolean checkMathProblemResult() {
        return firstNumber + secondNumber == Integer.parseInt(mathProblemAnswerEditTextView.getText().toString());
    }

    private void initViewForCRW(View rootView) {

        contentRatingMainContainer =
                (PercentRelativeLayout) rootView.findViewById(R.id.app_cms_content_rating_main_container);
        contentRatingMainContainer.setBackgroundColor(appCMSPresenter.getGeneralBackgroundColor());
        contentRatingInfoContainer =
                (LinearLayout) rootView.findViewById(R.id.app_cms_content_rating_info_container);
        contentRatingHeaderView = (TextView) rootView.findViewById(R.id.app_cms_content_rating_header_view);
        contentRatingTitleHeader = (TextView) rootView.findViewById(R.id.app_cms_content_rating_title_header);
        contentRatingDiscretionView = (TextView) rootView.findViewById(R.id.app_cms_content_rating_viewer_discretion);

        contentRatingHeaderView.setText(appCMSPresenter.getLocalisedStrings().getContentRatingTextWarningLabel());
        contentRatingTitleHeader.setText(appCMSPresenter.getLocalisedStrings().getContentRatingDescText());
        contentRatingDiscretionView.setText(appCMSPresenter.getLocalisedStrings().getContentRatingViewerDiscretionLabel());

        progressBar = (ProgressBar) rootView.findViewById(R.id.app_cms_content_rating_progress_bar);

        if (!TextUtils.isEmpty(fontColor)) {
            contentRatingTitleHeader.setTextColor(Color.parseColor(fontColor));
        }

        if (appCMSPresenter.getAppCMSMain() != null &&
                !TextUtils.isEmpty(appCMSPresenter.getAppCMSMain().getBrand().getGeneral().getBlockTitleColor())) {
            int highlightColor =
                    Color.parseColor(appCMSPresenter.getAppCMSMain().getBrand().getGeneral().getBlockTitleColor());
            contentRatingHeaderView.setTextColor(highlightColor);
            applyBorderToComponent(contentRatingInfoContainer, 1, highlightColor);
            progressBar.getProgressDrawable()
                    .setColorFilter(highlightColor, PorterDuff.Mode.SRC_IN);
            progressBar.setMax(100);
        }
    }

    private void createContentRatingView() throws Exception {
        String promId=null;
        if(contentDatum!=null&&contentDatum.getContentDetails()!=null
                &&contentDatum.getContentDetails().getPromos()!=null&&
                contentDatum.getContentDetails().getPromos().size()>0&&
                contentDatum.getContentDetails().getPromos().get(0).getId() != null){
            promId=contentDatum.getContentDetails().getPromos().get(0).getId();
            videoPlayerView.setFilmId(promId);
            isPromo=true;
        }
        if (!isTrailer &&
                !parentalRating.equalsIgnoreCase(getString(R.string.age_rating_converted_g)) &&
                !parentalRating.equalsIgnoreCase(getString(R.string.age_rating_converted_default)) &&
                appCMSPresenter.getAppCMSMain().getFeatures().isUserContentRating() &&
                watchedTime == 0) {
            videoPlayerMainContainer.setVisibility(GONE);
            contentRatingMainContainer.setVisibility(VISIBLE);
            String contentRatingDescriptionText = String.format("%s %s", appCMSPresenter.getLocalisedStrings().getContentRatingDescText(), parentalRating);
            contentRatingTitleHeader.setText(contentRatingDescriptionText);
            new Handler().post(this::startCountdown);
        }else if(isPromo==true && promId!=null){
            appCMSPresenter.refreshVideoData(promId,
                    updatedContentDatum -> {
                        String videoUrl = "";
                        if (updatedContentDatum != null &&
                                updatedContentDatum.getStreamingInfo() != null &&
                                updatedContentDatum.getStreamingInfo().getVideoAssets() != null) {
                            videoUrl=getVideoUrl(updatedContentDatum.getStreamingInfo().getVideoAssets());
                            Log.d(TAG , "Url is = "+videoUrl);
                            if (streamingQualitySelector.getVideoUrl() != null) {
                                streamingQualitySelector.setVideoUrl(videoUrl);
                            }
//                            contentRatingMainContainer.setVisibility(GONE);
//                            videoPlayerMainContainer.setVisibility(VISIBLE);
//                            appCMSPresenter.tvVideoPlayerView.getPlayerView().hideController();
//                            appCMSPresenter.tvVideoPlayerView.getPlayerView().setUseController(false);
                            videoPlayerInfoContainer.setVisibility(GONE);
                            videoPlayerView.disableController();
                            isPromo=false;
                            preparePlayer();

                        }
                    }, null, false, false,contentDatum);
        }
        else {
            contentRatingMainContainer.setVisibility(GONE);
            videoPlayerMainContainer.setVisibility(VISIBLE);
            preparePlayer();
            startEntitlementCheckTimer();
        }
    }
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
    /*private String getParentalRating() {
        if (!isTrailer &&
                !parentalRating.equalsIgnoreCase(getString(R.string.age_rating_converted_g)) &&
                !parentalRating.equalsIgnoreCase(getString(R.string.age_rating_converted_default)) &&
                watchedTime == 0) {
            contentRatingTitleHeader.setText(getString(R.string.content_rating_description_placeholder, parentalRating));
        }
        return parentalRating != null ? parentalRating : getString(R.string.age_rating_converted_default);
    }*/

    private void startCountdown() {
        new CountDownTimer(totalCountdownInMillis, countDownIntervalInMillis) {
            @Override
            public void onTick(long millisUntilFinished) {
                long progress = (long) (100.0 * (1.0 - (double) millisUntilFinished / (double) totalCountdownInMillis));
//                Log.d(TAG, "CRW Progress:" + progress);
                progressBar.setProgress((int) progress);
            }

            @Override
            public void onFinish() {
                if (isVisible() && isAdded()) {
                    contentRatingMainContainer.setVisibility(GONE);
                    videoPlayerMainContainer.setVisibility(VISIBLE);
                    preparePlayer();
                    startEntitlementCheckTimer();
                }
            }
        }.start();
    }

    private void applyBorderToComponent(View view, int width, int Color) {
        GradientDrawable rectangleBorder = new GradientDrawable();
        rectangleBorder.setShape(GradientDrawable.RECTANGLE);
        rectangleBorder.setStroke(width, Color);
        view.setBackground(rectangleBorder);
    }

    public void hideControlsForLiveStream(ContentDatum contentDatum) {
        try {
            boolean isLiveStream = contentDatum.getGist().isLiveStream();
            videoPlayerView.getPlayerView().findViewById(R.id.exo_position).setVisibility(isLiveStream ? GONE : VISIBLE);
            videoPlayerView.getPlayerView().findViewById(R.id.exo_progress).setVisibility(isLiveStream ? GONE : VISIBLE);
            videoPlayerView.getPlayerView().findViewById(R.id.exo_duration).setVisibility(isLiveStream ? GONE : VISIBLE);

            if (isLiveStream) {
                View rewind = videoPlayerView.getPlayerView().findViewById(R.id.exo_rew);
                rewind.setTag(rewind.getVisibility());
                rewind.getViewTreeObserver().addOnGlobalLayoutListener(() -> {
                    if (rewind.getVisibility() == VISIBLE) {
                        rewind.setVisibility(GONE);
                    }
                });

                View forward = videoPlayerView.getPlayerView().findViewById(R.id.exo_ffwd);
                forward.setTag(rewind.getVisibility());
                forward.getViewTreeObserver().addOnGlobalLayoutListener(() -> {
                    if (forward.getVisibility() == VISIBLE) {
                        forward.setVisibility(GONE);
                    }
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
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

    }

    private void sendAdImpression() {
        if (beaconMessageThread != null) {
            beaconMessageThread.sendBeaconPing = false;
            if (mProgressHandler != null)
                mProgressHandler.removeCallbacks(mProgressRunnable);
        }
        if (appCMSPresenter != null && appCMSPresenter.getAppCMSMain().getFeatures().getAnalyticsBeacon().isEnabled()) {

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
                    false);
        }
    }

    private void sendAdRequest() {
        if (!TextUtils.isEmpty(mStreamId) && appCMSPresenter != null && appCMSPresenter.getAppCMSMain().getFeatures().getAnalyticsBeacon().isEnabled()) {
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
                    false);
        }
    }

    private void createAllEpisodeList() {
        boolean segment = false;
        int currentSeason = 0;
        int currentEpisode = 0;
        List<Season_> seasonList = null;
        if (moduleApi != null && moduleApi.getContentData() != null && moduleApi.getContentData().get(0).getSeason() != null) {
            seasonList = moduleApi.getContentData().get(0).getSeason();
        } else if (contentDatum != null && contentDatum.getSeason() != null ) {
            seasonList = contentDatum.getSeason();
        } else {
            return;
        }
        if (seasonList != null) {
            allEpisodes = new ArrayList<>();
            for (int i = 0; i < seasonList.size(); i++) {
                if (seasonList.get(i).getEpisodes() != null) {
                    for (int j = 0; j <seasonList.get(i).getEpisodes().size(); j++) {
                        ContentDatum episode = seasonList.get(i).getEpisodes().get(j);
                        if (episode.getRelatedVideos() != null && episode.getRelatedVideos().size() > 0) {
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
                for (int i = 0; i < seasonList.get(currentSeason).getEpisodes().get(currentEpisode).getRelatedVideos().size(); i++) {
                    // allEpisodes.add(moduleApi.getContentData().get(0).getSeason().get(currentSeason).getEpisodes().get(currentEpisode).getRelatedVideos().get(i));
                    if (seasonList.get(currentSeason).getEpisodes().get(currentEpisode).getRelatedVideos().get(i).getGist().getId().equalsIgnoreCase(filmId)) {
                        segmentId = seasonList.get(currentSeason).getEpisodes().get(currentEpisode).getGist().getId();
                        break;
                    }
                }
            }
            for (int i = 0; i < seasonList.size(); i++) {
                if (seasonList.get(i).getEpisodes() != null) {
                    for (int j = 0; j < seasonList.get(i).getEpisodes().size(); j++) {
                        allEpisodes.add(seasonList.get(i).getEpisodes().get(j));
                    }
                }
            }

        }

    }

    private int findCurrentPlayingPositionOfEpisode() {
        if (segmentId == null) {
            segmentId = filmId;
        }
        if (allEpisodes != null && allEpisodes.size() > 0) {
            for (int i = 0; i < allEpisodes.size(); i++) {
                if (segmentId.equalsIgnoreCase(allEpisodes.get(i).getGist().getId()))
                    return i;
            }
        }
        return -1;
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

    public void setVideoSelected(VideoSelected videoSelected) {
        this.videoSelected = videoSelected;
    }

    @Optional
    @OnClick(R.id.previousEpisode)
    void previousEpisodeClick() {
        videoSelected.selectedVideoListener(allEpisodes.get(findCurrentPlayingPositionOfEpisode() - 1), findCurrentPlayingPositionOfEpisode());
    }

    @Optional
    @OnClick(R.id.nextEpisode)
    void nextEpisodeClick() {
        videoSelected.selectedVideoListener(allEpisodes.get(findCurrentPlayingPositionOfEpisode() + 1), findCurrentPlayingPositionOfEpisode());
    }

    private void setPrevNextBackground() {
        if (appCMSPresenter.isNewsTemplate()) {
            if (previous != null) {
                previous.setTextColor(appCMSPresenter.getGeneralTextColor());
                previous.setTypeface(Utils.getSpecificTypeface(
                        mContext,
                        appCMSPresenter,
                        mContext.getString(R.string.app_cms_page_font_regular_key)));
            }
            if (next != null) {
                next.setTextColor(appCMSPresenter.getGeneralTextColor());
                next.setTypeface(Utils.getSpecificTypeface(
                        mContext,
                        appCMSPresenter,
                        mContext.getString(R.string.app_cms_page_font_regular_key)));
            }

            Component component = new Component();
            component.setBorderEnable(true);

            previousEpisodeImg.setBackground(Utils.getTrayBorder(getActivity(), Utils.getFocusBorderColor(getActivity(), appCMSPresenter), component));
            previousEpisodeImg.setPadding(1, 3, 1, 3);

            nextEpisodeImg.setBackground(Utils.getTrayBorder(getActivity(), Utils.getFocusBorderColor(getActivity(), appCMSPresenter), component));
            nextEpisodeImg.setPadding(1, 3, 1, 3);

            videoPlayerView.getPlayerView().getController().setPlayerPlayPauseState(this);
        }
    }

    public void loadPrevNextImage() {
        if (allEpisodes != null && allEpisodes.size() > 0) {
            int currentPlayingEpisodePos = findCurrentPlayingPositionOfEpisode();
            if (nextEpisodeContainer != null && currentPlayingEpisodePos < allEpisodes.size() - 1) {
                setPreviousNextEpisodeImage(allEpisodes.get(currentPlayingEpisodePos + 1).getGist().getImageGist().get_16x9(), nextEpisodeImg);
            }
            if (previousEpisodeContainer != null && currentPlayingEpisodePos > 0) {
                setPreviousNextEpisodeImage(allEpisodes.get(currentPlayingEpisodePos - 1).getGist().getImageGist().get_16x9(), previousEpisodeImg);
            }
            setPreviousNextVisibility(true);
        }
    }

    public void setPreviousNextVisibility(boolean visibility) {
        if (allEpisodes != null && allEpisodes.size() > 0) {
            int currentPlayingEpisodePos = findCurrentPlayingPositionOfEpisode();
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

    @Override
    public void playerState(boolean isVideoPaused) {
        if (appCMSPresenter.isNewsTemplate()) {
            if (isVideoPaused) {
                setBackgroundViewVisibility(true);
                loadPrevNextImage();
            } else {
                setBackgroundViewVisibility(false);
                setPreviousNextVisibility(false);
            }
        }
    }

    public void setBackgroundViewVisibility(boolean visibility) {
        if (visibility) {
            backgroundView.setVisibility(View.VISIBLE);
        } else {
            backgroundView.setVisibility(View.GONE);
        }

    }

    public void updateWatchedHistory() {
        boolean isWatchHistoryUpdateEnabled = appCMSPresenter.getAppCMSMain().getFeatures() != null
                && appCMSPresenter.getAppCMSMain().getFeatures().getWatchedHistory() != null
                && appCMSPresenter.getAppCMSMain().getFeatures().getWatchedHistory().isEnabled();
        if (isWatchHistoryUpdateEnabled) {
            int interval = appCMSPresenter.getAppCMSMain().getFeatures().getWatchedHistory().getIntervalInt();
            if (!isTrailer /*&& interval <= (videoPlayerView.getCurrentPosition() / 1000)*/) {
                appCMSPresenter.updateWatchedTime(filmId, seriesId,
                        videoPlayerView.getCurrentPosition() / 1000, null);
            }
        }
    }
    public void setTrailerCompletedCallback(TrailerCompletedCallback trailerCompletedCallback) {
        this.trailerCompletedCallback = trailerCompletedCallback;
    }
    private TrailerCompletedCallback trailerCompletedCallback;

    @Override
    public void performPlayNextVisibility(boolean visible) {

        if (allEpisodes != null && allEpisodes.size() > 0) {
            int currentPlayingEpisodePos = findCurrentPlayingPositionOfEpisode();
            if (nextEpisodeContainer != null && currentPlayingEpisodePos < allEpisodes.size() - 1) {
                Gist gist = allEpisodes.get(currentPlayingEpisodePos + 1).getGist();
                setPlayNextEpisodeImage(gist.getImageGist().get_16x9(), gist.getTitle(), gist.getDescription());
            }
            if (visible) {
                if (currentPlayingEpisodePos < allEpisodes.size() - 1)
                    playNextEpisodeContainer.setVisibility(VISIBLE);

                playNextEpisodeContainer.setFocusable(true);
            } else {
                playNextEpisodeContainer.setVisibility(GONE);
            }
        } else {
            playNextEpisodeContainer.setVisibility(GONE);
        }
    }
    @Override
    public void performPlayNextClick(){
        if (playNextEpisodeContainer != null && playNextEpisodeContainer.getVisibility() == VISIBLE){
            playNextEpisodeContainer.performClick();
        }
    }

    @Override
    public boolean getPlayNextFocus(){
        if (playNextEpisodeContainer != null && playNextEpisodeContainer.getVisibility() == VISIBLE){
           return playNextEpisodeContainer.isFocused();
        }
        return false;
    }

    private void setPlayNextEpisodeImage(String imageUrl, String  playNextTitleText, String playNextDescriptionText) {
        RequestOptions requestOptions = new RequestOptions()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .fitCenter();
        Glide.with(playNextEpisodeImage.getContext())
                .load(imageUrl)
                .apply(requestOptions)
                .into(playNextEpisodeImage);


        playNextEpisodeTitle.setText(playNextTitleText);
        playNextEpisodeDescription.setText(playNextDescriptionText);
        playNextTitle.setText(appCMSPresenter.getLocalisedStrings().getNextLabel());

    }

    @Optional
    @OnClick(R.id.playNextEpisodeContainer)
    void playNextEpisodeContainerClick() {
        videoSelected.selectedVideoListener(allEpisodes.get(findCurrentPlayingPositionOfEpisode() + 1), findCurrentPlayingPositionOfEpisode());
    }
}

