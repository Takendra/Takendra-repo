package com.viewlift.casting;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.mediarouter.app.MediaRouteDiscoveryFragment;
import androidx.mediarouter.media.MediaRouteSelector;
import androidx.mediarouter.media.MediaRouter;

import com.google.android.gms.cast.CastDevice;
import com.google.android.gms.cast.CastMediaControlIntent;
import com.google.android.gms.cast.MediaStatus;
import com.google.android.gms.cast.MediaTrack;
import com.google.android.gms.cast.framework.CastContext;
import com.google.android.gms.cast.framework.CastSession;
import com.google.android.gms.cast.framework.SessionManagerListener;
import com.google.android.gms.cast.framework.media.RemoteMediaClient;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.viewlift.R;
import com.viewlift.models.data.appcms.api.ClosedCaptions;
import com.viewlift.models.data.appcms.api.ContentDatum;
import com.viewlift.models.data.appcms.api.VideoAssets;
import com.viewlift.presenters.AppCMSPresenter;
import com.viewlift.utils.CommonUtils;
import com.viewlift.views.activity.AppCMSPageActivity;
import com.viewlift.views.activity.AppCMSPlayVideoActivity;
import com.viewlift.views.binders.AppCMSVideoPageBinder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import rx.Observable;
import rx.functions.Action1;

public class CastHelper {
    private String TAG = "CastHelper";
    private static CastHelper objMain;
    private String beaconScreenName = "";

    private CastContext mCastContext;
    private CastSession mCastSession;
    public MediaRouter mMediaRouter;
    private SessionManagerListener<CastSession> mSessionManagerListener;
    private Callback callBackRemoteListener;
    private FragmentActivity mActivity;
    private final Context mAppContext;
    private RemoteMediaClient.Callback remoteCallback;
   // private RemoteMediaClient.Listener remoteListener;
    private RemoteMediaClient.ProgressListener progressListener;
    private List<ContentDatum> listRelatedVideosDetails;
    private List<String> listRelatedVideosId;
    int mediaIndex = 0 ;
    private List<String> listCompareRelatedVideosId;

    private MediaRouteSelector mMediaRouteSelector;
    private MyMediaRouterCallback mMediaRouterCallback;
    private AppCMSPresenter appCMSPresenterComponenet;
    private String appName;
    public List<Object> routes = new ArrayList<>();
    public boolean isCastDeviceAvailable = false;
    public boolean isCastDeviceConnected = false;
    public boolean chromeCastConnecting = false;
    public CastDevice mSelectedDevice;
    private int currentPlayingIndex = 0;
    public int playIndexPosition = 0;
    private static long castCurrentDuration;
    private long castCurrentMediaPosition;
    private static final String DISCOVERY_FRAGMENT_TAG = "DiscoveryFragment";

    private AppCMSVideoPageBinder binderPlayScreen;
    private boolean isMainMediaId = false;
    private long currentMediaPosition = 0;
    private String startingFilmId = "";
    private boolean sentBeaconPlay;
    private boolean sentBeaconFirstFrame;
    private boolean sendBeaconPing;

    private boolean onAppDisConnectCalled = false;
    private Action1<OnApplicationEnded> onApplicationEndedAction;
    private String imageUrl = "";
    private String title = "";
    private String videoUrl = "";
    private String paramLink = "";

    private static String mStreamId;
    private long mStartBufferMilliSec = 0l;
    private long mStopBufferMilliSec;
    private static double ttfirstframe = 0d;
    private long beaconBufferingTime;


    private static boolean isVideoDownloaded;


    private CastHelper(Context mContext) {
        mAppContext = mContext.getApplicationContext();
        try {
            GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
            int resultCode = apiAvailability.isGooglePlayServicesAvailable(mContext);


            if (resultCode == ConnectionResult.SUCCESS) {

                mCastContext = CastContext.getSharedInstance(mAppContext);
                mMediaRouteSelector = new MediaRouteSelector.Builder()
                        .addControlCategory("com.google.android.gms.cast.CATEGORY_CAST")
                        .build();
                appName = mAppContext.getResources().getString(R.string.app_name);
                beaconScreenName = mAppContext.getResources().getString(R.string.app_cms_beacon_casting_screen_name);
                mMediaRouterCallback = new MyMediaRouterCallback();
                castCurrentMediaPosition = 0L;
                setCastDiscovery();
            }/*else{

            Log.i(TAG, "This device is not supported.");
            Toast.makeText(mContext, "This device is not supported. Please upgrade your play-service", Toast.LENGTH_SHORT).show();
        }*/
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static class OnApplicationEnded {
        private int recommendedVideoIndex;
        private long currentWatchedTime;

        public int getRecommendedVideoIndex() {
            return recommendedVideoIndex;
        }

        public void setRecommendedVideoIndex(int recommendedVideoIndex) {
            this.recommendedVideoIndex = recommendedVideoIndex;
        }

        public long getCurrentWatchedTime() {
            return currentWatchedTime;
        }

        public void setCurrentWatchedTime(long currentWatchedTime) {
            this.currentWatchedTime = currentWatchedTime;
        }
    }


    public void setCastDiscovery() {
        if (CastingUtils.IS_CHROMECAST_ENABLE) {
            mMediaRouter = MediaRouter.getInstance(mAppContext);
            mMediaRouter.addCallback(mMediaRouteSelector, mMediaRouterCallback,
                    MediaRouter.CALLBACK_FLAG_PERFORM_ACTIVE_SCAN);
            if (mActivity instanceof AppCMSPageActivity)
                try {
                    addMediaRouterDiscoveryFragment();
                } catch (IllegalStateException e) {
                    e.printStackTrace();
                }
        }

    }

    public static synchronized CastHelper getInstance(Context context) {
        if (objMain == null) {
            try {
                objMain = new CastHelper(context);
            } catch (Exception e) {

            }
        }
        return objMain;
    }

    public void initCastingObj() {
        if (mCastSession == null) {
            mCastSession = CastContext.getSharedInstance(mAppContext).getSessionManager()
                    .getCurrentCastSession();
        }
        mCastSession = mCastContext.getSessionManager().getCurrentCastSession();
        setupCastListener();
        initRemoteClientListeners();
        initProgressListeners();

    }

    public String getDeviceName() {
        String deviceName = "";
        try {
            mCastSession = CastContext.getSharedInstance(mAppContext).getSessionManager()
                    .getCurrentCastSession();
            if (mCastSession != null && mCastSession.isConnected()) {
                deviceName = mCastSession.getCastDevice().getFriendlyName();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return deviceName;
    }


    public void setInstance(FragmentActivity mActivity) {
        this.mActivity = mActivity;
    }

    public void removeInstance() {
        this.mActivity = null;
    }

    public void setCallBackListener(Callback remoteMediaCallback) {
        callBackRemoteListener = remoteMediaCallback;
    }

    public void removeCallBackListener(Callback remoteMediaCallback) {
        callBackRemoteListener = remoteMediaCallback;
    }

    public void setCastSessionManager() {
        try {
            mCastContext.getSessionManager().addSessionManagerListener(mSessionManagerListener, CastSession.class);
        } catch (Exception npe) {
        }
    }

    public void removeCastSessionManager() {

        mCastContext.getSessionManager().removeSessionManagerListener(mSessionManagerListener, CastSession.class);
    }

    public void removeMediaRouterRemoveCallback() {
        if (mMediaRouter != null)
            mMediaRouter.removeCallback(mMediaRouterCallback);
    }

    private void addMediaRouterDiscoveryFragment() {
        FragmentManager fm = mActivity.getSupportFragmentManager();
        DiscoveryFragment fragment =
                (DiscoveryFragment) fm.findFragmentByTag(DISCOVERY_FRAGMENT_TAG);
        if (fragment == null) {
            fragment = new DiscoveryFragment();
            fragment.setCallback(mMediaRouterCallback);
            fragment.setRouteSelector(mMediaRouteSelector);
            fm.beginTransaction().add(fragment, DISCOVERY_FRAGMENT_TAG).commit();
        } else {
            fragment.setCallback(mMediaRouterCallback);
            fragment.setRouteSelector(mMediaRouteSelector);
        }
    }

    public static final class DiscoveryFragment extends MediaRouteDiscoveryFragment {
        private static final String TAG = "DiscoveryFragment";
        private MediaRouter.Callback mCallback;

        public DiscoveryFragment() {
            mCallback = null;
        }

        public void setCallback(MediaRouter.Callback cb) {
            mCallback = cb;
        }

        @Override
        public MediaRouter.Callback onCreateCallback() {
            return mCallback;
        }

        @Override
        public int onPrepareCallbackFlags() {
            // Add the CALLBACK_FLAG_UNFILTERED_EVENTS flag to ensure that we will
            // observe and log all route events including those that are for routes
            // that do not match our selector.  This is only for demonstration purposes
            // and should not be needed by most applications.
            return super.onPrepareCallbackFlags() | MediaRouter.CALLBACK_FLAG_UNFILTERED_EVENTS;
        }
    }

    public interface Callback {
        void onApplicationConnected();

        void onApplicationDisconnected();

        void onRouterAdded(MediaRouter mMediaRouter, MediaRouter.RouteInfo route);

        void onRouterRemoved(MediaRouter mMediaRouter, MediaRouter.RouteInfo route);

        void onRouterSelected(MediaRouter mMediaRouter, MediaRouter.RouteInfo info);

        void onRouterUnselected(MediaRouter mMediaRouter, MediaRouter.RouteInfo info);
    }

    public void finishPlayerScreenOnCastConnect() {
        if (callBackRemoteListener != null && mActivity != null & mActivity instanceof AppCMSPlayVideoActivity) {
            mActivity.finish();
        }
    }

    public boolean isRemoteDeviceConnected() {
        boolean isCastDeviceConnected = false;
        if (mMediaRouter == null)
            return false;

        if (mMediaRouter.getSelectedRoute().isDefault()) {
            isCastDeviceConnected = false;

        } else if (mMediaRouter.getSelectedRoute().getConnectionState()
                == MediaRouter.RouteInfo.CONNECTION_STATE_CONNECTED) {
            isCastDeviceConnected = true;

        } else if (mSelectedDevice != null) {
            isCastDeviceConnected = true;

        } else if (mMediaRouter.getSelectedRoute().getConnectionState()
                == MediaRouter.RouteInfo.CONNECTION_STATE_CONNECTING) {
            isCastDeviceConnected = true;
        }
        return isCastDeviceConnected;
    }


    public void launchRemoteMedia(AppCMSPresenter appCMSPresenter,
                                  List<String> relateVideoId,
                                  String filmId,
                                  long currentPosition,
                                  AppCMSVideoPageBinder binder,
                                  boolean sentBeaconPlay,
                                  Action1<OnApplicationEnded> onApplicationEndedAction) {
        this.sentBeaconPlay = sentBeaconPlay;
        this.onApplicationEndedAction = onApplicationEndedAction;
        if (mActivity != null && CastingUtils.isMediaQueueLoaded) {

            CastingUtils.isRemoteMediaControllerOpen = false;
            currentMediaPosition = currentPosition;
            binderPlayScreen = binder;
            startingFilmId = filmId;
            if (getRemoteMediaClient() == null) {
                return;
            }

            CastingUtils.isMediaQueueLoaded = false;
            //getRemoteMediaClient().removeListener(remoteListener);
            getRemoteMediaClient().unregisterCallback(remoteCallback);
            getRemoteMediaClient().removeProgressListener(progressListener);
            this.appCMSPresenterComponenet = appCMSPresenter;
            listRelatedVideosDetails = new ArrayList<ContentDatum>();
            listRelatedVideosId = new ArrayList<String>();
            listCompareRelatedVideosId = new ArrayList<String>();

            if (filmId == null && relateVideoId == null) {
                return;
            }

            /*if (relateVideoId != null) {
                if (!relateVideoId.contains(filmId)) {
                    isMainMediaId = true;
                    listRelatedVideosId.add(filmId);
                    currentPlayingIndex = 0;
                } else {
                    currentPlayingIndex = relateVideoId.indexOf(filmId);
                }

                listRelatedVideosId.addAll(relateVideoId.subList(currentPlayingIndex,relateVideoId.size()));
                listCompareRelatedVideosId.addAll(listRelatedVideosId);
                currentPlayingIndex=0;
            } else if (filmId != null) {
                currentPlayingIndex = 0;
                listRelatedVideosId.add(filmId);
                listCompareRelatedVideosId.add(filmId);
            }*/
            String customReceiverId = appCMSPresenter.getAppCMSMain().getFeatures().getCustomReceiverId();
            CommonUtils.setCustomReceiverId(customReceiverId);
            String playingUrl = CastingUtils.getPlayingUrl(binderPlayScreen.getContentData());
            if (binderPlayScreen == null || playingUrl == null || TextUtils.isEmpty(playingUrl)) {
                return;
            }

            getRemoteMediaClient().addProgressListener(progressListener, 1000);
            launchSingeRemoteMedia(binderPlayScreen, playingUrl, filmId, currentPosition, false);

            // now add related videos in queue

            boolean shouldAddRelatedVideoInQueue = (customReceiverId == null || customReceiverId.equalsIgnoreCase(CastMediaControlIntent.DEFAULT_MEDIA_RECEIVER_APPLICATION_ID));
            if (shouldAddRelatedVideoInQueue && relateVideoId != null && relateVideoId.size() > 0) {
                listRelatedVideosId = relateVideoId;
                mediaIndex = 0;
                addMediaListToRemote();
                CastingUtils.isMediaQueueLoaded = true;
            }


            //relateVideoId = null;
           /* if (relateVideoId == null && binderPlayScreen != null && playingUrl != null && !TextUtils.isEmpty(playingUrl)) {
            } else {

            }*/
            Toast.makeText(mAppContext, appCMSPresenter.getLocalisedStrings().getLoadingOnCastingText(), Toast.LENGTH_SHORT).show();
            try {
                mStreamId = appCMSPresenterComponenet.getStreamingId(binder.getContentData().getGist().getTitle());
            } catch (Exception e) {
                mStreamId = filmId + appCMSPresenterComponenet.getCurrentTimeStamp();
            }


        }
    }

    //launchTrailer use to launch media when auto play off
    public void launchTrailer(AppCMSPresenter appCMSPresenter, String filmId, AppCMSVideoPageBinder binder, long currentPosition) {

        Toast.makeText(mAppContext, appCMSPresenter.getLocalisedStrings().getLoadingOnCastingText(), Toast.LENGTH_SHORT).show();
        if(binder == null || binder.getContentData() == null){
            Toast.makeText(mAppContext, appCMSPresenter.getLocalisedStrings().getStreamingInfoErrorText(), Toast.LENGTH_SHORT).show();
            return;
        }
        this.appCMSPresenterComponenet = appCMSPresenter;
        if (binder.getContentData().getContentDetails() != null
                && binder.getContentData().getContentDetails().getTrailers() != null
                && binder.getContentData().getContentDetails().getTrailers().size() > 0
                && binder.getContentData().getContentDetails().getTrailers().get(0) != null
                && binder.getContentData().getContentDetails().getTrailers().get(0).getVideoAssets() != null) {
            title = binder.getContentData().getContentDetails().getTrailers().get(0).getTitle();
            VideoAssets videoAssets = binder.getContentData().getContentDetails().getTrailers().get(0).getVideoAssets();
            if (videoAssets != null && videoAssets.getMpeg() != null && videoAssets.getMpeg().size() > 0) {
                videoUrl = videoAssets.getMpeg().get(videoAssets.getMpeg().size() - 1).getUrl();
            }
        } else {
            if (binder.getContentData().getGist() != null && binder.getContentData().getGist().getTitle() != null) {
                title = binder.getContentData().getGist().getTitle();
            }
            videoUrl = CastingUtils.getPlayingUrl(binder.getContentData());
        }

        if (videoUrl != null && !TextUtils.isEmpty(videoUrl)) {
            launchSingeRemoteMedia(binder, videoUrl, filmId, currentPosition, true);
        }
        try {
            mStreamId = appCMSPresenterComponenet.getStreamingId(binder.getContentData().getGist().getTitle());
        } catch (Exception e) {
            mStreamId = filmId + appCMSPresenterComponenet.getCurrentTimeStamp();
        }
    }


    public void launchSingeRemoteMedia(AppCMSVideoPageBinder binder, String videoPlayUrl, String filmId, long currentPosition, boolean isTrailer) {

        boolean isFreeVideo = false;
        boolean isDrmEnabled = false;
        String externalContentId = "";
        String seriesName = "";
        String seriesId = "";
        String contentCategory = "";
        String mediaType = "";
        String subTitles = "";
        int totalDuration = 0;
        String contentId = "";
        String mediaTitle = "";
        String mediaImage = "";
        String mediaYear = "";
        String licenseUrl ="";
        String licenseToken = "";

        String isCloudFrontTokenEnabled = "false";
        String keyPairId = "";
        String policy = "";
        String signature = "";

        List<MediaTrack> tracks = new ArrayList<>();

        if (binder != null && binder.getContentData() != null && binder.getContentData().getGist() != null) {
            if (binder.getContentData().getGist().getPermalink() != null) {
                paramLink = binder.getContentData().getGist().getPermalink();
            }
            title = CastingUtils.getTitle(binder.getContentData(), isTrailer);
            if (binder.getContentData().getGist().getVideoImageUrl() != null) {
                imageUrl = binder.getContentData().getGist().getVideoImageUrl();
            }
            ContentDatum contentDatum = binder.getContentData();
            isFreeVideo = contentDatum.getGist().isFree();
            isDrmEnabled = contentDatum.isDRMEnabled();
            externalContentId ="";
            contentCategory = contentDatum.getCategoryTitle();
            mediaType = contentDatum.getMediaType();
            JSONArray subTitlesObj = new JSONArray();//.add("subtitleUrl", binder.getContentData().get);
            if (contentDatum.getContentDetails() != null
                    && contentDatum.getContentDetails().getClosedCaptions() != null
                    && !contentDatum.getContentDetails().getClosedCaptions().isEmpty()) {
                for (ClosedCaptions cc : contentDatum.getContentDetails().getClosedCaptions()) {
                    if (cc.getUrl() != null &&
                            !cc.getUrl().equalsIgnoreCase(appCMSPresenterComponenet.getCurrentContext().getString(R.string.download_file_prefix)) &&
                            cc.getFormat() != null &&
                            "SRT".equalsIgnoreCase(cc.getFormat())) {
                        try {
                            JSONObject tempObj = new JSONObject();
                            tempObj.putOpt("subtitleUrl", cc.getUrl());
                            tempObj.putOpt("language", cc.getLanguage());
                            subTitlesObj.put(tempObj);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
            subTitles = subTitlesObj.toString();
            totalDuration = (int) (contentDatum.getGist().getRuntime() / 60);
            contentId = filmId;
            mediaTitle = contentDatum.getGist().getTitle();
            mediaImage = contentDatum.getGist().getVideoImageUrl();
            if(contentDatum.getStreamingInfo()!=null && contentDatum.getStreamingInfo().getVideoAssets()!=null && contentDatum.getStreamingInfo().getVideoAssets().getWideVine()!=null
                    && contentDatum.getStreamingInfo().getVideoAssets().getWideVine().getLicenseUrl()!=null) {
                licenseUrl = contentDatum.getStreamingInfo().getVideoAssets().getWideVine().getLicenseUrl();
                licenseToken = contentDatum.getStreamingInfo().getVideoAssets().getWideVine().getLicenseToken();
            }
           // mediaYear = contentDatum.getContentDetails().\
            if(contentDatum.getStreamingInfo()!=null
                    && contentDatum.getStreamingInfo().getVideoAssets()!=null
                    && contentDatum.getStreamingInfo().getVideoAssets().getHlsDetail() !=null
                    && contentDatum.getStreamingInfo().getVideoAssets().getHlsDetail().getSignature() !=null
                    && contentDatum.getStreamingInfo().getVideoAssets().getHlsDetail().getPolicy() !=null
                    && contentDatum.getStreamingInfo().getVideoAssets().getHlsDetail().getKeypairId() !=null) {
                keyPairId = contentDatum.getStreamingInfo().getVideoAssets().getHlsDetail().getKeypairId();
                signature = contentDatum.getStreamingInfo().getVideoAssets().getHlsDetail().getSignature();
                policy = contentDatum.getStreamingInfo().getVideoAssets().getHlsDetail().getPolicy();
                isCloudFrontTokenEnabled = "true";
            }
            CastingUtils.isLiveStream =  contentDatum.getGist().isLiveStream();
            tracks = CastingUtils.getMediaTracks(contentDatum);
        }

        JSONObject customData = new JSONObject();
        try {
            customData.put("isFreeVideo",isFreeVideo);
            customData.put("isDrmEnabled",isDrmEnabled);
            customData.put("externalContentId",externalContentId);
            customData.put("seriesName",seriesName);
            customData.put("seriesId",seriesId);
            customData.put("contentCategory",contentCategory);
            customData.put("mediaType",mediaType);
//            customData.put("subTitles",subTitles);
            customData.put("totalDuration",totalDuration);
            customData.put("contentId",contentId);
            customData.put("mediaTitle",mediaTitle);
            customData.put("mediaImage",mediaImage);
            customData.put("mediaYear",mediaYear);
            customData.put("licenseUrl", licenseUrl);
            customData.put("licenseToken", licenseToken);


            customData.put("isCloudFrontTokenEnabled", isCloudFrontTokenEnabled);
            customData.put("Key-Pair-Id", keyPairId);
            customData.put("Policy", policy);
            customData.put("Signature", signature);


        }catch (Exception e){
            e.printStackTrace();
        }


        CastingUtils.isRemoteMediaControllerOpen = false;
        //JSONObject customData = new JSONObject();
        try {
            customData.put(CastingUtils.MEDIA_KEY, filmId);
        } catch (JSONException e) {
        }
        String appPackageName = mAppContext.getPackageName();

        try {
            customData.put(CastingUtils.PARAM_KEY, paramLink);
            customData.put(CastingUtils.VIDEO_TITLE, title);
            customData.put(CastingUtils.ITEM_TYPE, appPackageName + "" + CastingUtils.ITEM_TYPE_VIDEO);

        } catch (JSONException e) {
        }
        if (getRemoteMediaClient() != null) {
            getRemoteMediaClient().load(CastingUtils.buildMediaInfo(title,
                    appName,
                    imageUrl,
                    videoPlayUrl,
                    customData,
                    mAppContext, tracks), true, currentPosition);
            //getRemoteMediaClient().addListener(remoteListener);
            getRemoteMediaClient().registerCallback(remoteCallback);
            onAppDisConnectCalled = false;
            CastingUtils.isMediaQueueLoaded = true;

          //  getRemoteMediaClient().registerCallback(remoteCallback);
            getRemoteMediaClient().addProgressListener(progressListener, 1000);
            onAppDisConnectCalled = false;

        }

    }

    public void launchSingeRemoteMedia(String title, String paramLink, String imageUrl, String videoPlayUrl, String filmId, long currentPosition, boolean isTrailer) {

        this.paramLink = paramLink != null ? paramLink : "";
        this.imageUrl = imageUrl != null ? imageUrl : "";
        this.title = title != null ? title : "";
        CastingUtils.isRemoteMediaControllerOpen = false;
        JSONObject customData = new JSONObject();
        try {
            customData.put(CastingUtils.MEDIA_KEY, filmId);
        } catch (JSONException e) {
        }
        String appPackageName = mAppContext.getPackageName();

        try {
            customData.put(CastingUtils.PARAM_KEY, paramLink);
            customData.put(CastingUtils.VIDEO_TITLE, title);
            customData.put(CastingUtils.ITEM_TYPE, appPackageName + "" + CastingUtils.ITEM_TYPE_VIDEO);
        } catch (JSONException e) {
        }
        if (getRemoteMediaClient() != null) {
            getRemoteMediaClient().load(CastingUtils.buildMediaInfo(title,
                    appName,
                    imageUrl,
                    videoPlayUrl,
                    customData,
                    mAppContext, null), true, currentPosition);
            //getRemoteMediaClient().addListener(remoteListener);
            getRemoteMediaClient().registerCallback(remoteCallback);
            onAppDisConnectCalled = false;
        }
    }


    public void openRemoteController() {

        if (mActivity != null) {
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
            }
            if (!CastingUtils.isRemoteMediaControllerOpen) {
                Intent intent = new Intent(mActivity, ExpandedControlsActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                mActivity.startActivity(intent);
                CastingUtils.isRemoteMediaControllerOpen = true;

            }
        }
    }


    private void initRemoteClientListeners() {

        remoteCallback = new RemoteMediaClient.Callback() {
            @Override
            public void onStatusUpdated() {
                openRemoteController();
                if (getRemoteMediaClient() != null && getRemoteMediaClient().getMediaStatus() != null &&
                        getRemoteMediaClient().getMediaStatus().getCurrentItemId() <
                                getRemoteMediaClient().getMediaStatus().getLoadingItemId()) {
                    sentBeaconPlay = false;
                    sentBeaconFirstFrame = false;
                    try {
                        mStreamId = appCMSPresenterComponenet.getStreamingId(title);
                    } catch (Exception e) {
                        mStreamId = CastingUtils.getRemoteMediaId(mAppContext) + appCMSPresenterComponenet.getCurrentTimeStamp();
                    }
                }
                updatePlaybackState();
            }

            @Override
            public void onMetadataUpdated() {
                try {
                    JSONObject getRemoteObject = CastContext.getSharedInstance(mAppContext)
                            .getSessionManager()
                            .getCurrentCastSession()
                            .getRemoteMediaClient()
                            .getCurrentItem()
                            .getCustomData();
                    CastingUtils.castingMediaId = getRemoteObject.getString(CastingUtils.MEDIA_KEY);
                } catch (Exception e) {
                }
                if (listCompareRelatedVideosId != null) {
                    playIndexPosition = listCompareRelatedVideosId
                            .indexOf(CastingUtils.castingMediaId);
                }

            }

            @Override
            public void onQueueStatusUpdated() {
                super.onQueueStatusUpdated();
            }

            @Override
            public void onPreloadStatusUpdated() {
                super.onPreloadStatusUpdated();
            }

            @Override
            public void onSendingRemoteMediaRequest() {
                super.onSendingRemoteMediaRequest();
            }

            @Override
            public void onAdBreakStatusUpdated() {
                super.onAdBreakStatusUpdated();
            }
        };
        /*remoteListener = new RemoteMediaClient.Listener() {
            @Override
            public void onStatusUpdated() {
                openRemoteController();
                if (getRemoteMediaClient() != null && getRemoteMediaClient().getMediaStatus() != null &&
                        getRemoteMediaClient().getMediaStatus().getCurrentItemId() <
                                getRemoteMediaClient().getMediaStatus().getLoadingItemId()) {
                    sentBeaconPlay = false;
                    sentBeaconFirstFrame = false;
                    try {
                        mStreamId = appCMSPresenterComponenet.getStreamingId(title);
                    } catch (Exception e) {
                        //Log.e(TAG, e.getMessage());
                        mStreamId = CastingUtils.getRemoteMediaId(mAppContext) + appCMSPresenterComponenet.getCurrentTimeStamp();
                    }
                }
                updatePlaybackState();
            }

            @Override
            public void onMetadataUpdated() {
                try {
                    JSONObject getRemoteObject = CastContext.getSharedInstance(mAppContext)
                            .getSessionManager()
                            .getCurrentCastSession()
                            .getRemoteMediaClient()
                            .getCurrentItem()
                            .getCustomData();
                    CastingUtils.castingMediaId = getRemoteObject.getString(CastingUtils.MEDIA_KEY);
                } catch (Exception e) {
                    //Log.e(TAG, e.getLocalizedMessage());
                }
                if (listCompareRelatedVideosId != null) {
                    playIndexPosition = listCompareRelatedVideosId
                            .indexOf(CastingUtils.castingMediaId);
                }

                //Log.d(TAG, "Remote Media listener-" + "onMetadataUpdated");
            }

            @Override
            public void onQueueStatusUpdated() {
                //Log.d(TAG, "Remote Media listener-" + "onQueueStatusUpdated");
            }

            @Override
            public void onPreloadStatusUpdated() {
                //Log.d(TAG, "Remote Media listener-" + "onPreloadStatusUpdated");
            }

            @Override
            public void onSendingRemoteMediaRequest() {
                //Log.d(TAG, "Remote Media listener-" + "onSendingRemoteMediaRequest");

            }

            @Override
            public void onAdBreakStatusUpdated() {
                //Log.d(TAG, "Remote Media listener-" + "onAdBreakStatusUpdated");

            }
        };*/

    }


    private void setupCastListener() {
        mSessionManagerListener = new SessionManagerListener<CastSession>() {

            @Override
            public void onSessionEnded(CastSession session, int error) {

                onApplicationDisconnected();
            }

            @Override
            public void onSessionResumed(CastSession session, boolean wasSuspended) {

                onApplicationConnected(session);
            }

            @Override
            public void onSessionResumeFailed(CastSession session, int error) {

                onApplicationDisconnected();
            }

            @Override
            public void onSessionStarted(CastSession session, String sessionId) {

                onApplicationConnected(session);
            }

            @Override
            public void onSessionStartFailed(CastSession session, int error) {
                onApplicationDisconnected();
            }

            @Override
            public void onSessionStarting(CastSession session) {

            }

            @Override
            public void onSessionEnding(CastSession session) {

            }

            @Override
            public void onSessionResuming(CastSession session, String sessionId) {
            }

            @Override
            public void onSessionSuspended(CastSession session, int reason) {

            }

            private void onApplicationConnected(CastSession castSession) {
                mCastSession = castSession;
                if (callBackRemoteListener != null)
                    callBackRemoteListener.onApplicationConnected();

            }

            private void onApplicationDisconnected() {
                CastingUtils.isMediaQueueLoaded = true;

                OnApplicationEnded onApplicationEnded = new OnApplicationEnded();
                onApplicationEnded.setCurrentWatchedTime(castCurrentMediaPosition);
                onApplicationEnded.setRecommendedVideoIndex(playIndexPosition);

                if (getRemoteMediaClient() != null) {
                    getRemoteMediaClient().stop();
                   // getRemoteMediaClient().removeListener(remoteListener);
                    getRemoteMediaClient().unregisterCallback(remoteCallback);
                    getRemoteMediaClient().removeProgressListener(progressListener);
                }

                onAppDisConnectCalled = false;
                if (callBackRemoteListener != null && mActivity != null && mActivity instanceof AppCMSPlayVideoActivity && binderPlayScreen != null && !onAppDisConnectCalled) {
                    onAppDisConnectCalled = true;
                    //if player activity already opened than finish it
                    if (onApplicationEndedAction != null) {
                        Observable.just(onApplicationEnded).subscribe(onApplicationEndedAction);
                    }
                    //if casted from local play screen from first video than this video will not in related video list  so set -1 index position to play on local player

                    if (CastingUtils.castingMediaId == null || TextUtils.isEmpty(CastingUtils.castingMediaId)) {
                        CastingUtils.castingMediaId = startingFilmId;
                    }
                    if (isMainMediaId) {
                        playIndexPosition--;
                    } else if (listCompareRelatedVideosId != null) {
                        playIndexPosition = listCompareRelatedVideosId.indexOf(CastingUtils.castingMediaId);
                    }

                    //Log.d(TAG, "Cast Index " + playIndexPosition);
                    if (listRelatedVideosDetails != null && listRelatedVideosDetails.size() > 0) {
                        int currentVideoDetailIndex = getCurrentIndex(listRelatedVideosDetails, CastingUtils.castingMediaId);
                        if (currentVideoDetailIndex < listRelatedVideosDetails.size())
                            binderPlayScreen.setContentData(listRelatedVideosDetails.get(currentVideoDetailIndex));
                    }

                    binderPlayScreen.setCurrentPlayingVideoIndex(playIndexPosition);
                    if (listCompareRelatedVideosId != null && playIndexPosition < listCompareRelatedVideosId.size()) {
                        appCMSPresenterComponenet.playNextVideo(binderPlayScreen,
                                binderPlayScreen.getCurrentPlayingVideoIndex(),
                                castCurrentMediaPosition);
                    }

                    CastingUtils.castingMediaId = "";
                }
                if (callBackRemoteListener != null)
                    callBackRemoteListener.onApplicationDisconnected();
            }
        };
    }


    private void initProgressListeners() {

        progressListener = (remoteCastProgress, totalCastDuration) -> {
            boolean isWatchHistoryUpdateEnabled = appCMSPresenterComponenet.getAppCMSMain().getFeatures() != null
                    && appCMSPresenterComponenet.getAppCMSMain().getFeatures().getWatchedHistory() != null
                    && appCMSPresenterComponenet.getAppCMSMain().getFeatures().getWatchedHistory().isEnabled();
            if (isWatchHistoryUpdateEnabled && !CastingUtils.isLiveStream) {
                int interval = appCMSPresenterComponenet.getAppCMSMain().getFeatures().getWatchedHistory().getIntervalInt();
                castCurrentMediaPosition = remoteCastProgress;
                castCurrentDuration = remoteCastProgress / 1000;
                try {
                    if (castCurrentDuration % interval == 0 ) {
                        String currentRemoteMediaId = CastingUtils.getRemoteMediaId(mAppContext);
                        String currentMediaParamKey = CastingUtils.getRemoteParamKey(mAppContext);


                        if (!TextUtils.isEmpty(currentRemoteMediaId)) {
                            appCMSPresenterComponenet.updateWatchedTime(currentRemoteMediaId, null,
                                    castCurrentDuration, updateHistoryResponse -> {
                                        if (updateHistoryResponse.getResponseCode() == 401 && updateHistoryResponse.getErrorCode() != null) {
                                            if (updateHistoryResponse.getErrorCode().equalsIgnoreCase("MAX_STREAMS_ERROR")) {
                                                appCMSPresenterComponenet.showDialog(AppCMSPresenter.DialogType.MAX_STREAMS_ERROR, updateHistoryResponse.getErrorMessage(), true, () -> {
                                                    getRemoteMediaClient().stop();
                                                }, null, null);
                                            }
                                        } else {

                                        }
                                    });
                           if(appCMSPresenterComponenet.getAppCMSMain().getFeatures().getAnalyticsBeacon().isEnabled()) {
                               appCMSPresenterComponenet.sendBeaconMessage(currentRemoteMediaId,
                                       currentMediaParamKey,
                                       beaconScreenName,
                                       castCurrentDuration,
                                       true,
                                       AppCMSPresenter.BeaconEvent.PING,
                                       "Video",
                                       null,
                                       null,
                                       null,
                                       mStreamId,
                                       0d,
                                       0,
                                       isVideoDownloaded);
                           }
                            appCMSPresenterComponenet.sendGaEvent(mAppContext.getResources().getString(R.string.play_video_action),
                                    mAppContext.getResources().getString(R.string.play_video_category),
                                    (title != null && !TextUtils.isEmpty(title)) ? title : currentRemoteMediaId);

                        /*appCMSPresenterComponenet.updateWatchedTime(currentRemoteMediaId, null,
                                castCurrentDuration, null);*/
                        }

                    }
                } catch (Exception e) {
                }
            }
        };
    }


    private void removeNonFreeVideos() {
        List<Integer> freeMovieIndices = new ArrayList<>();
        List<ContentDatum> freeMovies = new ArrayList<>();
        List<String> freeMovieIds = new ArrayList<>();
        for (int i = 0; i < listRelatedVideosDetails.size(); i++) {
            ContentDatum contentDatum = listRelatedVideosDetails.get(i);
            if (contentDatum != null &&
                    contentDatum.getGist() != null &&
                    contentDatum.getGist().getFree()) {
                freeMovieIndices.add(i);
            }
        }

        for (int i = 0; i < freeMovieIndices.size(); i++) {
            freeMovies.add(listRelatedVideosDetails.get(freeMovieIndices.get(i)));
            freeMovieIds.add(listRelatedVideosDetails.get(freeMovieIndices.get(i)).getGist().getId());
        }

        listRelatedVideosDetails = freeMovies;
        listCompareRelatedVideosId = freeMovieIds;
    }

    /*private void castMediaListToRemoteLocation() {
        CastingUtils.isMediaQueueLoaded = true;
        if (getRemoteMediaClient() != null && listRelatedVideosDetails != null && listRelatedVideosDetails.size() > 0) {
            MediaQueueItem[] queueItemsArray = CastingUtils.BuildCastingQueueItems(listRelatedVideosDetails,
                    appName,
                    listCompareRelatedVideosId,
                    mAppContext);
            getRemoteMediaClient().queueLoad(queueItemsArray, currentPlayingIndex,
                    MediaStatus.REPEAT_MODE_REPEAT_OFF, currentMediaPosition, null);
           //getRemoteMediaClient().addListener(remoteListener);
            getRemoteMediaClient().registerCallback(remoteCallback);
            getRemoteMediaClient().addProgressListener(progressListener, 1000);
            onAppDisConnectCalled = false;
        } else if (binderPlayScreen != null && binderPlayScreen.getContentData() != null) {

            videoUrl = CastingUtils.getPlayingUrl(binderPlayScreen.getContentData());
            if (videoUrl != null && !TextUtils.isEmpty(videoUrl)) {
                launchSingeRemoteMedia(binderPlayScreen, videoUrl, startingFilmId, currentMediaPosition, false);
            }
        }
    }*/


    private void addMediaListToRemote() {
        if (mediaIndex >= listRelatedVideosId.size() - 1) {
            return; //loop is finished;
        }

        mediaIndex++;
        String id = listRelatedVideosId.get(mediaIndex);
        appCMSPresenterComponenet.refreshVideoData(id, new Action1<ContentDatum>() {
            @Override
            public void call(ContentDatum contentDatum) {
                if (contentDatum != null &&
                        contentDatum.getGist() != null &&
                        (contentDatum.getGist().getFree() || appCMSPresenterComponenet.isUserSubscribed())) {
                    getRemoteMediaClient().queueAppendItem(CastingUtils.buildSingleCastingQueueItem(contentDatum, appName,
                            listCompareRelatedVideosId,
                            mAppContext), null);
                }
                addMediaListToRemote();
            }
        }, null, false, false,null);

    }


    private class MyMediaRouterCallback extends MediaRouter.Callback {
        @Override
        public void onRouteAdded(MediaRouter router, MediaRouter.RouteInfo route) {
            //Log.w(TAG, "MyMediaRouterCallback-onRouteAdded ");
            List<MediaRouter.RouteInfo> c_routes = mMediaRouter.getRoutes();
            routes.clear();
            routes.addAll(c_routes);
            onFilterRoutes(routes);
            isCastDeviceAvailable = routes.size() > 0;
            if (callBackRemoteListener != null)
                callBackRemoteListener.onRouterAdded(mMediaRouter, route);
        }

        @Override
        public void onRouteRemoved(MediaRouter router, MediaRouter.RouteInfo route) {
            //Log.w(TAG, "MyMediaRouterCallback-onRouteRemoved ");
            for (int i = 0; i < routes.size(); i++) {
                if (routes.get(i) instanceof MediaRouter.RouteInfo) {
                    MediaRouter.RouteInfo routeInfo = (MediaRouter.RouteInfo) routes.get(i);
                    if (routeInfo.equals(route)) {
                        routes.remove(i);
                        break;
                    }
                }
            }
            isCastDeviceAvailable = routes.size() > 0;
            if (callBackRemoteListener != null)
                callBackRemoteListener.onRouterRemoved(mMediaRouter, route);
        }

        @Override
        public void onRouteSelected(MediaRouter router, MediaRouter.RouteInfo info) {

            chromeCastConnecting = true;
            mSelectedDevice = CastDevice.getFromBundle(info.getExtras());
            isCastDeviceConnected = true;
            if (callBackRemoteListener != null)
                callBackRemoteListener.onRouterSelected(mMediaRouter, info);
        }

        @Override
        public void onRouteUnselected(MediaRouter router, MediaRouter.RouteInfo info) {
            mSelectedDevice = null;
            isCastDeviceConnected = false;
            if (callBackRemoteListener != null)
                callBackRemoteListener.onRouterUnselected(mMediaRouter, info);
            CastingUtils.isMediaQueueLoaded = true;
        }
    }

    public RemoteMediaClient getRemoteMediaClient() {
        CastSession castSession = CastContext.getSharedInstance(mAppContext).getSessionManager()
                .getCurrentCastSession();
        if (castSession == null || !castSession.isConnected()) {
            //Log.w(TAG, "Trying to get a RemoteMediaClient when no CastSession is started.");
            return null;
        }
        return castSession.getRemoteMediaClient();
    }


    private void updatePlaybackState() {
        boolean isFinish = false;
        mCastSession = CastContext.getSharedInstance(mAppContext).getSessionManager()
                .getCurrentCastSession();

        if (listRelatedVideosDetails != null && listRelatedVideosDetails.size() > 0) {
            int currentVideoDetailIndex = getCurrentIndex(listRelatedVideosDetails, CastingUtils.castingMediaId);
            if (currentVideoDetailIndex >= listRelatedVideosDetails.size()) {
                isFinish = true;
            }
        } else {
            isFinish = true;
        }

        if (getRemoteMediaClient() == null) {
            return;
        }
        int status = getRemoteMediaClient().getPlayerState();
        int idleReason = getRemoteMediaClient().getIdleReason();
        String currentRemoteMediaId = CastingUtils.getRemoteMediaId(mAppContext);
        String currentMediaParamKey = CastingUtils.getRemoteParamKey(mAppContext);

        if (!sentBeaconPlay) {

            if (appCMSPresenterComponenet == null)
                return;
            isVideoDownloaded = appCMSPresenterComponenet.isVideoDownloaded(currentRemoteMediaId);
            mStartBufferMilliSec = new Date().getTime();
            if (!TextUtils.isEmpty(currentRemoteMediaId)) {
                mStopBufferMilliSec = new Date().getTime();
                appCMSPresenterComponenet.sendBeaconMessage(currentRemoteMediaId,
                        currentMediaParamKey,
                        beaconScreenName,
                        castCurrentDuration,
                        true,
                        AppCMSPresenter.BeaconEvent.PLAY,
                        "Video",
                        null,
                        null,
                        null,
                        mStreamId,
                        0d,
                        0,
                        isVideoDownloaded);
                sentBeaconPlay = true;

                appCMSPresenterComponenet.sendGaEvent(mAppContext.getResources().getString(R.string.play_video_action),
                        mAppContext.getResources().getString(R.string.play_video_category),
                        (title != null && !TextUtils.isEmpty(title)) ? title : currentRemoteMediaId);
            }
        }
        switch (status) {
            case MediaStatus.PLAYER_STATE_PLAYING:
                sendBeaconPing = true;
                if (!sentBeaconFirstFrame) {

                    if (!TextUtils.isEmpty(currentRemoteMediaId)) {
                        if (appCMSPresenterComponenet.getAppCMSMain().getFeatures().isEnableQOS()) {
                            mStopBufferMilliSec = new Date().getTime();
                            ttfirstframe = mStartBufferMilliSec == 0l ? 0d : ((mStopBufferMilliSec - mStartBufferMilliSec) / 1000d);
                            appCMSPresenterComponenet.sendBeaconMessage(currentRemoteMediaId,
                                    currentMediaParamKey,
                                    beaconScreenName,
                                    castCurrentDuration,
                                    true,
                                    AppCMSPresenter.BeaconEvent.FIRST_FRAME,
                                    "Video",
                                    null,
                                    null,
                                    null,
                                    mStreamId,
                                    ttfirstframe,
                                    0,
                                    isVideoDownloaded);
                            sentBeaconFirstFrame = true;
                        }
                    }
                }
                break;

            case MediaStatus.PLAYER_STATE_PAUSED:
                sendBeaconPing = false;
                break;

            case MediaStatus.PLAYER_STATE_UNKNOWN:
                sendBeaconPing = false;
                break;

            case MediaStatus.PLAYER_STATE_BUFFERING:
                sendBeaconPing = false;
                if (((System.currentTimeMillis() - beaconBufferingTime) / 1000) >= 5) {
                    beaconBufferingTime = System.currentTimeMillis();
                    if (appCMSPresenterComponenet != null && appCMSPresenterComponenet.getAppCMSMain().getFeatures().isEnableQOS()) {

                        appCMSPresenterComponenet.sendBeaconMessage(currentRemoteMediaId,
                                currentMediaParamKey,
                                beaconScreenName,
                                castCurrentDuration,
                                true,
                                AppCMSPresenter.BeaconEvent.BUFFERING,
                                "Video",
                                null,
                                null,
                                null,
                                mStreamId,
                                0d,
                                0,
                                isVideoDownloaded);
                    }
                }


                break;

            case MediaStatus.PLAYER_STATE_IDLE:
                sendBeaconPing = false;

                if (idleReason == MediaStatus.IDLE_REASON_FINISHED) {
                    //If all movies in auto play queue have been finished then finish the player activity if opened
                    if (isFinish && mActivity instanceof AppCMSPlayVideoActivity) {
                        mActivity.finish();
                    }

                }
                break;

            default: // case unknown
                sendBeaconPing = false;
                break;
        }
    }


    /**
     * Called to filter the set of routes that should be included in the list.
     * <p>
     * The default implementation iterates over all routes in the provided list and
     * removes those for which {@link #onFilterRoute} returns false.
     * </p>
     *
     * @param route The list of routes to filter in-place, never null.
     */
    public void onFilterRoutes(@NonNull List<Object> route) {
        for (int i = routes.size(); i-- > 0; ) {
            if (routes.get(i) instanceof MediaRouter.RouteInfo)
                if (!onFilterRoute((MediaRouter.RouteInfo) routes.get(i))) {
                    routes.remove(i);
                }

        }
    }

    /**
     * Returns true if the route should be included in the list.
     * <p>
     * The default implementation returns true for enabled non-default routes that
     * match the selector.  Subclasses can override this method to filter routes
     * differently.
     * </p>
     *
     * @param route The route to consider, never null.
     * @return True if the route should be included in the chooser dialog.
     */
    @SuppressLint("RestrictedApi")
    public boolean onFilterRoute(@NonNull MediaRouter.RouteInfo route) {
        return !route.isDefaultOrBluetooth() && route.isEnabled()
                && route.matchesSelector(mMediaRouteSelector);
    }

    public int getCurrentIndex(List<ContentDatum> list, String videoid) {
        int i = 0;
        for (i = 0; i < list.size(); i++) {
            if (videoid.equalsIgnoreCase(list.get(i).getGist().getId())) {
                return i;
            }
        }
        return i;
    }

    public void disconnectChromecastOnLogout() {
        if (CastContext.getSharedInstance(mAppContext).getSessionManager() != null) {

            try {
                if (CastContext.getSharedInstance(mAppContext).getSessionManager() != null) {
                    CastContext.getSharedInstance(mAppContext).getSessionManager().removeSessionManagerListener(mSessionManagerListener, CastSession.class);
                }

                //CastContext.getSharedInstance(mAppContext).getSessionManager().getCurrentCastSession().getRemoteMediaClient().removeListener(remoteListener);
                CastContext.getSharedInstance(mAppContext).getSessionManager().getCurrentCastSession().getRemoteMediaClient().unregisterCallback(remoteCallback);

                mSessionManagerListener = null;
                CastContext.getSharedInstance(mAppContext).getSessionManager().endCurrentSession(true);
            } catch (Exception e) {

            }
        }
    }

    public String getStartingFilmId() {
        return startingFilmId;
    }
}

