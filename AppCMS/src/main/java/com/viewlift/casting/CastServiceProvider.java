package com.viewlift.casting;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.os.Handler;

import androidx.fragment.app.FragmentActivity;
import androidx.mediarouter.media.MediaRouter;

import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.github.amlcurran.showcaseview.ShowcaseView;
import com.github.amlcurran.showcaseview.targets.Target;
import com.github.amlcurran.showcaseview.targets.ViewTarget;
import com.google.android.gms.cast.CastDevice;
import com.google.android.gms.cast.MediaInfo;
import com.google.android.gms.cast.framework.CastContext;
import com.google.android.gms.cast.framework.CastSession;
import com.google.android.gms.cast.framework.media.RemoteMediaClient;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.viewlift.AppCMSApplication;
import com.viewlift.BuildConfig;
import com.viewlift.audio.AudioServiceHelper;
import com.viewlift.R;
import com.viewlift.casting.roku.RokuCastingOverlay;
import com.viewlift.casting.roku.RokuDevice;
import com.viewlift.casting.roku.RokuLaunchThreadParams;
import com.viewlift.casting.roku.RokuWrapper;
import com.viewlift.casting.roku.dialog.CastChooserDialog;
import com.viewlift.casting.roku.dialog.CastDisconnectDialog;
import com.viewlift.db.AppPreference;
import com.viewlift.models.data.appcms.ui.main.LocalisedStrings;
import com.viewlift.presenters.AppCMSPresenter;
import com.viewlift.views.activity.AppCMSPageActivity;
import com.viewlift.views.activity.AppCMSPlayVideoActivity;
import com.viewlift.views.customviews.ViewCreator;

import org.json.JSONObject;

import java.util.List;

import javax.inject.Inject;

import uk.co.deanwild.materialshowcaseview.MaterialShowcaseView;

import static com.viewlift.presenters.AppCMSPresenter.DialogType.VIDEO_NOT_AVAILABLE_ALERT;

/**
 * A singleton to manage the different casting options such as chromecast and roku , on different activities.
 * google cast and roku instances creates single time here and on activity change instance of activity and cast icon view pass here.
 * and update the cast icon view on basis of casting status from remote devices
 */

public class CastServiceProvider {
    @SuppressLint("StaticFieldLeak")
    private static CastServiceProvider objMain;

    private String TAG = "CastServiceProvider";
    public String currentCastingUrl = "";
    private FragmentActivity mActivity;
    private ImageButton mMediaRouteButton, mPlayerMediaRouteButton;
    private CastHelper mCastHelper;
    private RokuWrapper rokuWrapper;
    private boolean isHomeScreen = false;
    private CastChooserDialog castChooserDialog;
    private CastSession mCastSession;
    private AnimationDrawable castAnimDrawable;
    boolean isTrailer = false, isFreeContent = false;

    public static final String CAST_STATUS = "com.viewlift.casting.CASTING_STATUS";

    private String pageName;

    private boolean isPPVContent = false;

    /**
     * callBackRokuMediaSelection gets the calls related to selected roku devices
     */
    private CastChooserDialog.CastChooserDialogEventListener callBackRokuMediaSelection =
            new CastChooserDialog.CastChooserDialogEventListener() {
                @Override
                public void onRokuDeviceSelected(RokuDevice selectedRokuDevice) {
                    mMediaRouteButton.setOnClickListener(null);
                    castAnimDrawable.start();
                    rokuWrapper.setSelectedRokuDevice(selectedRokuDevice);
                    if (mActivity != null)
                        launchRokuPlaybackLocation();
                }

                @Override
                public void onChromeCastDeviceSelect() {
                    mMediaRouteButton.setOnClickListener(null);
                    castAnimDrawable.start();
                }
            };
    private CastDisconnectDialog castDisconnectDialog;
    private ILaunchRemoteMedia callRemoteMediaPlayback;
    private Context mContext;
    @Inject
    AppPreference appPreference;
    @Inject
    AppCMSPresenter appCMSPresenter;
    @Inject
    LocalisedStrings localisedStrings;
    private ShowcaseView mShowCaseView;
    private boolean allowFreePlay;
    private CastCallBackListener castCallBackListener;
    private boolean tvodCastAllowed;
    private boolean isTvodContent, isSvodContent;

    /**
     * callBackCastHelper gets the calls related to chromecast devices selections
     */

    public interface CastCallBackListener {
        void onCastStatusUpdate();
    }

    public void setCastCallBackListener(CastCallBackListener castCallBackListener) {
        this.castCallBackListener = castCallBackListener;
    }

    private CastHelper.Callback callBackCastHelper = new CastHelper.Callback() {
        @Override
        public void onApplicationConnected() {
            if (mActivity != null && (mActivity instanceof AppCMSPlayVideoActivity ||
                    (appCMSPresenter.isPageAVideoPage(pageName)))) {
                launchChromecastRemotePlayback(CastingUtils.CASTING_MODE_CHROMECAST);
            } else {

                if (castCallBackListener != null) {
                    castCallBackListener.onCastStatusUpdate();
                }
            }

            stopRokuDiscovery();
        }

        @Override
        public void onApplicationDisconnected() {

            currentCastingUrl = "";
            if (castCallBackListener != null) {
                castCallBackListener.onCastStatusUpdate();
            }
        }

        @Override
        public void onRouterAdded(MediaRouter mMediaRouter, MediaRouter.RouteInfo route) {
            List<MediaRouter.RouteInfo> c_routes = mMediaRouter.getRoutes();
            mCastHelper.routes.clear();
            mCastHelper.routes.addAll(c_routes);
            mCastHelper.routes.addAll(rokuWrapper.getRokuDevices());
            mCastHelper.onFilterRoutes(mCastHelper.routes);
            mCastHelper.isCastDeviceAvailable = mCastHelper.routes.size() > 0;
            refreshCastMediaIcon();
            if (castChooserDialog != null && mCastHelper != null && mCastHelper.routes != null) {
                castChooserDialog.setRoutes(mCastHelper.routes);
            }
        }

        @Override
        public void onRouterRemoved(MediaRouter mMediaRouter, MediaRouter.RouteInfo info) {
            for (int i = 0; i < mCastHelper.routes.size(); i++) {
                if (mCastHelper.routes.get(i) instanceof MediaRouter.RouteInfo) {
                    MediaRouter.RouteInfo routeInfo = (MediaRouter.RouteInfo) mCastHelper.routes.get(i);
                    if (routeInfo.equals(info)) {
                        mCastHelper.routes.remove(i);
                        refreshCastMediaIcon();
                        break;
                    }
                }
            }
            if (castChooserDialog != null) {
                castChooserDialog.setRoutes(mCastHelper.routes);
            }
            mCastHelper.isCastDeviceAvailable = mCastHelper.routes.size() > 0;
            refreshCastMediaIcon();
        }

        @Override
        public void onRouterSelected(MediaRouter mMediaRouter, MediaRouter.RouteInfo info) {
            mCastHelper.chromeCastConnecting = true;
            mCastHelper.mSelectedDevice = CastDevice.getFromBundle(info.getExtras());
            mCastHelper.isCastDeviceConnected = true;
            refreshCastMediaIcon();
            if (rokuWrapper.isRokuDiscoveryTimerRunning()) {
                rokuWrapper.stopDiscoveryTimer();
            }
        }

        @Override
        public void onRouterUnselected(MediaRouter mMediaRouter, MediaRouter.RouteInfo info) {
            mCastHelper.mSelectedDevice = null;
            refreshCastMediaIcon();
            if (!rokuWrapper.isRokuDiscoveryTimerRunning()) {
                //
            }

            appCMSPresenter.sendChromecastDisconnectedAction();
        }
    };
    /**
     * callBackRokuDiscoveredDevices gets the calls related to roku devices discovery
     */
    private RokuWrapper.RokuWrapperEventListener callBackRokuDiscoveredDevices =
            new RokuWrapper.RokuWrapperEventListener() {
                @Override
                public void onRokuDiscovered(List<RokuDevice> rokuDeviceList) {

                    mCastHelper.routes.clear();
                    if (mCastHelper.mMediaRouter != null)
                        mCastHelper.routes.addAll(mCastHelper.mMediaRouter.getRoutes());
                    mCastHelper.routes.addAll(rokuWrapper.getRokuDevices());
                    mCastHelper.onFilterRoutes(mCastHelper.routes);
                    castChooserDialog.setRoutes(mCastHelper.routes);
                    mCastHelper.isCastDeviceAvailable = mCastHelper.routes.size() > 0;
                    refreshCastMediaIcon();
                }


                @Override
                public void onRokuConnected(RokuDevice selectedRokuDevice) {
                    rokuWrapper.setRokuConnected(true);

                    refreshCastMediaIcon();
                    if (rokuWrapper.isRokuDiscoveryTimerRunning()) {
                        rokuWrapper.stopDiscoveryTimer();
                    }
                    setRokuPlayScreen();
                }

                @Override
                public void onRokuStopped() {
                    rokuWrapper.setRokuConnected(false);
                    refreshCastMediaIcon();

                    if (!rokuWrapper.isRokuDiscoveryTimerRunning()) {
                        //
                    }
                }

                @Override
                public void onRokuConnectedFailed(String obj) {
                }
            };

    private CastServiceProvider(Context activity) {
        this.mContext = activity.getApplicationContext();
        ((AppCMSApplication) this.mContext).getAppCMSPresenterComponent().inject(this);
        setCasting();
        allowFreePlay = appCMSPresenter.isAppAVOD();
    }

    public static synchronized CastServiceProvider getInstance(Context activity) {
        if (objMain == null) {
            objMain = new CastServiceProvider(activity);
        }
        return objMain;
    }

    private void setCasting() {
        initChromecast();
        initRoku();
    }

    @SuppressWarnings("unused")
    public boolean isAllowFreePlay() {
        return allowFreePlay;
    }

    public void setAllowFreePlay(boolean allowFreePlay) {
        this.allowFreePlay = allowFreePlay;
    }

    private void initChromecast() {
        try {
            mCastHelper = CastHelper.getInstance(mContext);
            if (mCastSession == null) {
                mCastSession = CastContext.getSharedInstance(mContext).getSessionManager()
                        .getCurrentCastSession();
            }

            if (mCastHelper != null) {
                mCastHelper.initCastingObj();
                mCastHelper.setCallBackListener(callBackCastHelper);
                mCastHelper.setCastSessionManager();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initRoku() {
        rokuWrapper = RokuWrapper.getInstance();
        rokuWrapper.setListener(callBackRokuDiscoveredDevices);
    }

    /*
    set the image button for chromecastmedia view and current activity instance
    onLaunchRemotePlayback get from player screen as media need to play on player screen only
    */
    public void setActivityInstance(FragmentActivity mActivity, ImageButton mediaRouterView) {
        this.mActivity = mActivity;
        this.mMediaRouteButton = mediaRouterView;
        mCastHelper.setInstance(mActivity);
        mMediaRouteButton.setImageDrawable(mActivity.getResources().getDrawable(R.drawable.anim_cast, null));
        castAnimDrawable = (AnimationDrawable) mMediaRouteButton.getDrawable();
    }

    public void setVideoPlayerMediaButton(ImageButton mediaRouterView) {
        this.mPlayerMediaRouteButton = mediaRouterView;
        mPlayerMediaRouteButton.setImageDrawable(mActivity.getResources().getDrawable(R.drawable.anim_cast, null));
        castAnimDrawable = (AnimationDrawable) mPlayerMediaRouteButton.getDrawable();
        refreshCastMediaIcon();

    }

    public void onActivityResume() {
        try {
            GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
            int resultCode = apiAvailability.isGooglePlayServicesAvailable(mActivity);
            if (resultCode == ConnectionResult.SUCCESS) {

                refreshCastMediaIcon();
                if (mCastSession == null) {
                    mCastSession = CastContext.getSharedInstance(mActivity).getSessionManager()
                            .getCurrentCastSession();
                }
                if (mCastHelper != null) {
                    mCastHelper.setCastSessionManager();
                    if (shouldCastMiniControllerVisible()) {
                        AudioServiceHelper.getAudioInstance().changeMiniControllerVisiblity(true);
                    } else {
                        AudioServiceHelper.getAudioInstance().changeMiniControllerVisiblity(false);
                    }

                    createMediaChooserDialog();
                    try {
                        mCastHelper.setCastDiscovery();
                    } catch (IllegalArgumentException e) {
                        e.printStackTrace();
                    }

                    if (mCastHelper.mMediaRouter != null && mCastHelper.mMediaRouter.getSelectedRoute().isDefault()) {
                        mCastHelper.mSelectedDevice = null;
                    } else if (mCastHelper.mMediaRouter != null && mCastHelper.mMediaRouter.getSelectedRoute().getConnectionState()
                            == MediaRouter.RouteInfo.CONNECTION_STATE_CONNECTED) {
                        mCastHelper.isCastDeviceAvailable = true;
                        mCastHelper.mSelectedDevice = CastDevice.getFromBundle(mCastHelper.mMediaRouter.getSelectedRoute().getExtras());
                    }
                }
            } else {
                int PLAY_SERVICES_RESOLUTION_REQUEST = 1001;
                if (apiAvailability.isUserResolvableError(resultCode)) {
                    if (BuildConfig.FLAVOR.equalsIgnoreCase(AppCMSPresenter.MOBILE_BUILD_VARIENT)) {
                        apiAvailability.getErrorDialog(appCMSPresenter.getCurrentActivity(), resultCode, PLAY_SERVICES_RESOLUTION_REQUEST)
                                .show();
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean shouldCastMiniControllerVisible() {
        boolean shouldControllerVisible = true;
        RemoteMediaClient mRemoteMediaClient = null;

        try {

            mCastSession = CastContext.getSharedInstance(mActivity).getSessionManager()
                    .getCurrentCastSession();
            if (mCastSession != null && mCastSession.isConnected()) {
                mRemoteMediaClient = mCastSession.getRemoteMediaClient();
            }

            if (mRemoteMediaClient != null) {
                MediaInfo mediaInfo = mRemoteMediaClient.getMediaInfo();
                if (mediaInfo == null) {
                    return false;
                }
                String appPackageName = mContext.getPackageName();

                JSONObject customData = mediaInfo.getCustomData();
                if (customData != null && customData.has(CastingUtils.ITEM_TYPE)) {
                    String remoteItemType = customData.getString(CastingUtils.ITEM_TYPE);
                    shouldControllerVisible = !remoteItemType.equalsIgnoreCase(appPackageName + "" + CastingUtils.ITEM_TYPE_AUDIO);
                } else
                    shouldControllerVisible = customData != null && customData.has("video_title");
            } else {
                shouldControllerVisible = false;
            }
        } catch (Exception e) {
        }
        return shouldControllerVisible;
    }

    //if user comes from player screen and Remote devices already connected launch remote playback
    @SuppressWarnings("UnusedReturnValue")
    public boolean playChromeCastPlaybackIfCastConnected() {
        boolean isConnected = false;
        if (mCastHelper.isRemoteDeviceConnected()) {
            launchChromecastRemotePlayback(CastingUtils.CASTING_MODE_CHROMECAST);
            isConnected = true;
            stopRokuDiscovery();
        }
        return isConnected;
    }

    @SuppressWarnings("unused")
    public boolean playRokuCastPlaybackIfCastConnected() {
        boolean isConnected = false;
        if (rokuWrapper.isRokuConnected()) {
            launchChromecastRemotePlayback(CastingUtils.CASTING_MODE_ROKU);
            isConnected = true;
        }
        return isConnected;
    }

    @SuppressWarnings("unused")
    public void launchRokuCasting(String filmId, String videoImageUrl, String title) {
        launchRokuPlaybackLocation();
    }

    public boolean isCastingConnected() {
        boolean isConnected = false;
        if (mCastHelper != null && mCastHelper.isCastDeviceAvailable && (mCastHelper.isRemoteDeviceConnected() || rokuWrapper.isRokuConnected())) {
            isConnected = true;
        }
        return isConnected;
    }

    private void createMediaChooserDialog() {
        castChooserDialog = new CastChooserDialog(mActivity, callBackRokuMediaSelection);
        mCastHelper.routes.clear();
        if (mCastHelper.mMediaRouter != null && mCastHelper.mMediaRouter.getRoutes() != null) {
            mCastHelper.routes.addAll(mCastHelper.mMediaRouter.getRoutes());
        }

        mCastHelper.routes.addAll(rokuWrapper.getRokuDevices());
        mCastHelper.onFilterRoutes(mCastHelper.routes);
        castChooserDialog.setRoutes(mCastHelper.routes);
    }

    @SuppressWarnings("unused")
    private void startRokuDiscovery() {
        // TODO: Replace appId with value retrieved from AppCMS
        String appId = "";
        if (!TextUtils.isEmpty(appId))
            rokuWrapper.startDiscoveryTimer();
    }

    private void stopRokuDiscovery() {
        if (rokuWrapper.isRokuDiscoveryTimerRunning()) {
            rokuWrapper.stopDiscoveryTimer();
        }
    }

    @SuppressWarnings("unused")
    public void onAppDestroy() {
        mCastHelper.removeCastSessionManager();
        mCastHelper.removeCallBackListener(null);
        mCastHelper.removeInstance();
        rokuWrapper.removeListener();
        stopRokuDiscovery();
    }

    public void launchChromecastRemotePlayback(int castingModeChromecast) {
        if (callRemoteMediaPlayback != null) {
            callRemoteMediaPlayback.setRemotePlayBack(castingModeChromecast);
        }
    }

    /**
     * launchRokuPlaybackLocation launch the media files on selected Roku device
     */
    private void launchRokuPlaybackLocation() {
        String userId = null;
        String contentId = "0000015c-a2b4-d7a8-a3dc-b6f6f6ad0000";

        if (contentId != null && mActivity instanceof AppCMSPlayVideoActivity) {
            try {
                rokuWrapper.sendFilmLaunchRequest(
                        contentId,
                        RokuLaunchThreadParams.CONTENT_TYPE_FILM,
                        userId);
            } catch (Exception e) {
                rokuWrapper.sendAppLaunchRequest();
                e.printStackTrace();
            }
        } else {
            rokuWrapper.sendAppLaunchRequest();
        }
    }

    private void setRokuPlayScreen() {
        if (mActivity instanceof AppCMSPlayVideoActivity)
            mActivity.startActivity(new Intent(mActivity, RokuCastingOverlay.class));
    }

    public void isHomeScreen(boolean fromHomePage) {
        isHomeScreen = fromHomePage;
    }


    public void showIntroOverLay() {


        if (mMediaRouteButton != null && mActivity != null && isHomeScreen && appCMSPresenter.isCastEnable() && !appCMSPresenter.isHoichoiApp()) {
            appPreference.setCastOverLay();

            new Handler().postDelayed(() -> {
                MaterialShowcaseView showcaseView = new MaterialShowcaseView.Builder(mActivity)
                        .setTarget(mMediaRouteButton)
                        .setDismissText(localisedStrings.getOkText())
                        .setMaskColour(Color.parseColor(ViewCreator.getColorWithOpacity(mContext, "000000", 80)))
                        .setDismissTextColor(appCMSPresenter.getBrandPrimaryCtaColor())
                        .setContentText(localisedStrings.getTouchToCastMsgText())
                        .setContentTextColor(Color.parseColor(appCMSPresenter.getAppTextColor()))
                        .setDismissOnTouch(true)
//                        .setDelay(withDelay) // optional but starting animations immediately in onCreate can make them choppy
//                        .singleUse("2_3_4") // provide a unique ID used to ensure it is only shown once
                        .show();

                /*int textSize = 18;
                float scaledSizeInPixels = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP,
                        textSize, mContext.getResources().getDisplayMetrics());

                Target target = new ViewTarget(mMediaRouteButton.getId(), mActivity);
                TextPaint textPaint = new TextPaint();
                textPaint.setColor(Color.parseColor(appCMSPresenter.getAppTextColor()));
                textPaint.setTextSize(scaledSizeInPixels);
                mShowCaseView = new ShowcaseView.Builder(mActivity)
                        .setTarget(target) //Here is where you supply the id of the action bar item you want to display
//                        .setContentText(appCMSPresenter.getLanguageResourcesFile().getUIresource(mContext.getString(R.string.app_cast_overlay_text)))
                        .setContentText(localisedStrings.getTouchToCastMsgText())
                        .setContentTextPaint(textPaint)
                        .build();

                mShowCaseView.forceTextPosition(ShowcaseView.ABOVE_SHOWCASE);
                // mShowCaseView.setShowcaseColor(Color.parseColor(ViewCreator.getColorWithOpacity(mContext, "000000", 75)));
                mShowCaseView.setShowcaseColor(Color.parseColor(appCMSPresenter.getAppCtaBackgroundColor()));
//                mShowCaseView.setBackgroundColourShowCase(Color.parseColor(ViewCreator.getColorWithOpacity(mContext, "000000", 80)));
                mShowCaseView.setEndButtonBackgroundColor(Color.parseColor(appCMSPresenter.getAppCtaBackgroundColor()));
                mShowCaseView.setEndButtonTextColor(Color.parseColor(appCMSPresenter.getAppCtaTextColor()));

                mShowCaseView.show();
                mShowCaseView.invalidate();
                mShowCaseView.bringToFront();
                mActivity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);*/


            }, 500);

        }
    }


    public boolean isOverlayVisible() {
        boolean isVisible = false;
        if (mShowCaseView != null && mShowCaseView.isShowing()) {
            isVisible = true;
            ((ViewGroup) mActivity.getWindow().getDecorView()).removeView(mShowCaseView);
            mShowCaseView.hide();
            mShowCaseView = null;
        }
        return isVisible;
    }

    public void showCastoverlay() {
        if (mCastHelper.isCastDeviceAvailable)
            if (!appPreference.isCastOverLayShown()) {
//                appCMSPresenter.setCastOverLay();
                showIntroOverLay();
            }
    }

    /**
     * refreshCastMediaIcon invalidate the media icon view on the basis of casting status i.e disconnected/Connected
     */
    private void refreshCastMediaIcon() {
        refreshLivePlayerCastMediaIcon();
        if (mMediaRouteButton == null)
            return;

        mMediaRouteButton.setVisibility(mCastHelper != null
                && mCastHelper.isCastDeviceAvailable
                && (appCMSPresenter != null && appCMSPresenter.isCastEnable()) ? View.VISIBLE
                : mActivity != null && mActivity instanceof AppCMSPageActivity ? View.GONE : View.GONE);

        if (mCastHelper == null)
            return;

        //Setting the Casting Overlay for Casting
        if (mCastHelper.isCastDeviceAvailable && appCMSPresenter.isCastEnable())
            if (!appPreference.isCastOverLayShown()
                    && mContext.getResources().getBoolean(R.bool.display_chromecast_overlay)) {
//                appCMSPresenter.setCastOverLay();
                showIntroOverLay();
            }

        if (!mCastHelper.isCastDeviceAvailable && castChooserDialog != null && castChooserDialog.isShowing()) {
            castChooserDialog.dismiss();
        }

        if (mCastHelper.isCastDeviceAvailable && appCMSPresenter.getAppCMSMain() != null &&
                appCMSPresenter.getAppCMSMain().getBrand() != null && appCMSPresenter.getAppCMSMain().getBrand()
                .getGeneral() != null && appCMSPresenter.getAppCMSMain().getBrand()
                .getGeneral().getBlockTitleColor() != null) {
            if (rokuWrapper.isRokuConnected() || mCastHelper.isRemoteDeviceConnected()) {
                castAnimDrawable.stop();
                Drawable selectedImageDrawable = mActivity.getResources()
                        .getDrawable(R.drawable.toolbar_cast_connected, null);
                int fillColor = Color.parseColor(appCMSPresenter.getAppCMSMain().getBrand()
                        .getGeneral().getBlockTitleColor());
                selectedImageDrawable.setColorFilter(new PorterDuffColorFilter(fillColor,
                        PorterDuff.Mode.MULTIPLY));
                mMediaRouteButton.setImageDrawable(selectedImageDrawable);
            } else {
                castAnimDrawable.stop();
                mMediaRouteButton.setImageDrawable(mActivity.getResources()
                        .getDrawable(R.drawable.toolbar_cast_disconnected, null));
            }
        }

        mMediaRouteButton.setOnClickListener(v -> {
            if (appCMSPresenter.getAppCMSMain().isMonetizationModelEnabled()) {
                allowFreePlay = isTrailer || isFreeContent || appCMSPresenter.isAppAVOD();
                if (isTvodContent) {
                    if (!appCMSPresenter.isUserLoggedIn()) {
                        appCMSPresenter.showEntitlementDialog(AppCMSPresenter.DialogType.LOGIN_REQUIRED,
                                () -> {
                                    if (mActivity instanceof AppCMSPlayVideoActivity) {
                                        mActivity.finish();
                                    }
                                }, null);
                    } else if (isTvodContent) {
                        if (!tvodCastAllowed) {
                            appCMSPresenter.showDialog(VIDEO_NOT_AVAILABLE_ALERT, appCMSPresenter.getLocalisedStrings().getCastUnavailableMsg(), false, null, null, appCMSPresenter.getLocalisedStrings().getAlertTitle());
                        } else
                            connectCast();
                    } else {
                        connectCast();
                    }
                } else if (isSvodContent) {
                    if (!appCMSPresenter.isUserSubscribed()) {
                        appCMSPresenter.showEntitlementDialog(AppCMSPresenter.DialogType.SUBSCRIPTION_PREMIUM_CASTING,
                                () -> {
                                    if (mActivity instanceof AppCMSPlayVideoActivity) {
                                        mActivity.finish();
                                    }
                                }, null);
                    } else if (!appCMSPresenter.getAppPreference().isUserAllowedCasting()) {
                        appCMSPresenter.showEntitlementDialog(AppCMSPresenter.DialogType.SUBSCRIPTION_PLAN_UPGRADE,
                                () -> {
                                }, null);
                    } else
                        connectCast();
                } else {
                    connectCast();
                }
            } else {
                // Allow free play is not being set while app is launched
                // TODO : done as viewcretor also updating the same way. will update the code as concluded.
                allowFreePlay = (!appCMSPresenter.isAppSVOD() && appCMSPresenter.isAppAVOD()) || isTrailer || isFreeContent;
                if (appCMSPresenter.isAppTVOD() && !appCMSPresenter.isUserLoggedIn()) {
                    appCMSPresenter.showEntitlementDialog(AppCMSPresenter.DialogType.LOGIN_REQUIRED,
                            () -> {
                                if (mActivity instanceof AppCMSPlayVideoActivity) {
                                    mActivity.finish();
                                }
                            }, null);
                } else if (!allowFreePlay && !isPPVContent() &&
                        ((!appCMSPresenter.isAppSVOD() && appCMSPresenter.isUserLoggedIn()) ||
                                (appCMSPresenter.isAppSVOD() && !appCMSPresenter.isUserSubscribed()))) {
                    CastContext.getSharedInstance(appCMSPresenter.getCurrentActivity())
                            .getSessionManager().endCurrentSession(true);
                    if (appCMSPresenter.isAppSVOD() && appCMSPresenter.isUserLoggedIn()) {
                        appCMSPresenter.showEntitlementDialog(AppCMSPresenter.DialogType.SUBSCRIPTION_PREMIUM_CASTING,
                                null, null);
                    } else if (appCMSPresenter.isAppSVOD()) {
                        appCMSPresenter.showEntitlementDialog(AppCMSPresenter.DialogType.SUBSCRIPTION_PREMIUM_CASTING,
                                () -> {
                                    if (mActivity instanceof AppCMSPlayVideoActivity) {
                                        mActivity.finish();
                                    }
                                }, null);
                    } else if (!appCMSPresenter.isAppSVOD() && !appCMSPresenter.isUserLoggedIn()) {
                        appCMSPresenter.showEntitlementDialog(AppCMSPresenter.DialogType.LOGIN_REQUIRED,
                                () -> {
                                    if (mActivity instanceof AppCMSPlayVideoActivity) {
                                        mActivity.finish();
                                    }
                                }, null);
                    }
                } else {
                    connectCast();
                }
            }
        });
    }

    private void connectCast() {
        try {
            castDisconnectDialog = new CastDisconnectDialog(mActivity);

            if (mCastHelper.mSelectedDevice == null && mActivity != null) {
                castChooserDialog.setRoutes(mCastHelper.routes);
                castChooserDialog.show();
            } else if (mCastHelper.mSelectedDevice != null && mCastHelper.mMediaRouter != null
                    && mActivity != null) {
                castDisconnectDialog.setToBeDisconnectDevice(mCastHelper.mMediaRouter);
                castDisconnectDialog.show();
            }
        } catch (Exception e) {
            //
        }
    }

    private void refreshLivePlayerCastMediaIcon() {
        if (mPlayerMediaRouteButton == null)
            return;

        mPlayerMediaRouteButton.setVisibility(mCastHelper.isCastDeviceAvailable ? View.VISIBLE : View.GONE);

        //Setting the Casting Overlay for Casting
        if (mCastHelper.isCastDeviceAvailable)
            if (!appPreference.isCastOverLayShown()
                    && mContext.getResources().getBoolean(R.bool.display_chromecast_overlay)) {
                appPreference.setCastOverLay();
                showIntroOverLay();
            }

        if (!mCastHelper.isCastDeviceAvailable && castChooserDialog != null && castChooserDialog.isShowing()) {
            castChooserDialog.dismiss();
        }

        if (mCastHelper.isCastDeviceAvailable) {
            if (rokuWrapper.isRokuConnected() || mCastHelper.isRemoteDeviceConnected()) {
                castAnimDrawable.stop();
                Drawable selectedImageDrawable = mActivity.getResources()
                        .getDrawable(R.drawable.toolbar_cast_connected, null);
                int fillColor = Color.parseColor(appCMSPresenter.getAppCMSMain().getBrand()
                        .getGeneral().getBlockTitleColor());
                selectedImageDrawable.setColorFilter(new PorterDuffColorFilter(fillColor,
                        PorterDuff.Mode.MULTIPLY));
                mPlayerMediaRouteButton.setImageDrawable(selectedImageDrawable);
            } else {
                castAnimDrawable.stop();
                mPlayerMediaRouteButton.setImageDrawable(mActivity.getResources()
                        .getDrawable(R.drawable.toolbar_cast_disconnected, null));
            }
        }

        mPlayerMediaRouteButton.setOnClickListener(v -> {
            if (!allowFreePlay && !appCMSPresenter.isUserSubscribed() && !isPPVContent()) {
                CastContext.getSharedInstance(appCMSPresenter.getCurrentActivity())
                        .getSessionManager().endCurrentSession(true);
                if (appCMSPresenter.isAppSVOD() && appCMSPresenter.isUserLoggedIn()) {
                    appCMSPresenter.showEntitlementDialog(AppCMSPresenter.DialogType.SUBSCRIPTION_PREMIUM_CONTENT_REQUIRED,
                            null, null);
                } else if (appCMSPresenter.isAppSVOD()) {
                    appCMSPresenter.showEntitlementDialog(AppCMSPresenter.DialogType.LOGIN_AND_SUBSCRIPTION_PREMIUM_CONTENT_REQUIRED,
                            () -> {
                                if (mActivity instanceof AppCMSPlayVideoActivity) {
                                    mActivity.finish();
                                }
                            }, null);
                }
            } else {
                try {
                    castDisconnectDialog = new CastDisconnectDialog(mActivity);

                    if (mCastHelper.mSelectedDevice == null && mActivity != null) {
                        castChooserDialog.setRoutes(mCastHelper.routes);
                        castChooserDialog.show();
                    } else if (mCastHelper.mSelectedDevice != null && mCastHelper.mMediaRouter != null
                            && mActivity != null) {
                        castDisconnectDialog.setToBeDisconnectDevice(mCastHelper.mMediaRouter);
                        castDisconnectDialog.show();
                    }
                } catch (Exception e) {
                }
            }
        });
    }

    public void setRemotePlaybackCallback(ILaunchRemoteMedia onLaunchRemotePLayback) {
        this.callRemoteMediaPlayback = onLaunchRemotePLayback;
    }

    public interface ILaunchRemoteMedia {

        void setRemotePlayBack(int castingModeChromecast);

    }

    public void launchSingeRemoteMedia(String title, String paramLink, String imageUrl, String videoPlayUrl, String filmId, long currentPosition, boolean isTrailer) {
        if (mCastHelper != null)
            mCastHelper.launchSingeRemoteMedia(title, paramLink, imageUrl, videoPlayUrl, filmId, currentPosition, false);
    }

    public void setPageName(String pageName) {
        this.pageName = pageName;
    }

    public boolean castDeviceConnected() {
        return mCastHelper.isCastDeviceConnected;
    }

    public String getConnectedDeviceName() {
        try {
            if (mCastSession == null)
                return "";
            return mCastSession.getCastDevice().getFriendlyName();
        } catch (Exception ex) {
            ex.printStackTrace();
            return "";
        }
    }

    public boolean isPPVContent() {
        return isPPVContent;
    }

    public void setPPVContent(boolean PPVContent) {
        isPPVContent = PPVContent;
    }

    public void setTvodCastAllowed(boolean tvodCastAllowed, boolean isTvodContent) {
        this.tvodCastAllowed = tvodCastAllowed;
        this.isTvodContent = isTvodContent;
    }

    public void setSvodContent(boolean svodContent) {
        isSvodContent = svodContent;
    }

    public void setTrailer(boolean trailer) {
        isTrailer = trailer;
    }

    public void setFreeContent(boolean freeContent) {
        isFreeContent = freeContent;
    }

    public static void releaseInstance() {
        objMain = null;
    }
}

