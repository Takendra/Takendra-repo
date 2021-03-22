package com.viewlift.tv;

import android.animation.ObjectAnimator;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Resources;
import android.graphics.PorterDuff;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentTransaction;

import com.amazon.device.messaging.ADM;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.LoopingMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.ui.PlayerControlView;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DataSpec;
import com.google.android.exoplayer2.upstream.RawResourceDataSource;
import com.viewlift.AppCMSApplication;
import com.viewlift.R;
import com.viewlift.Utils;
import com.viewlift.models.data.appcms.ui.android.NavigationUser;
import com.viewlift.presenters.AppCMSPresenter;
import com.viewlift.tv.adm.AppCMSADMServerMsgHandler;
import com.viewlift.tv.utility.CustomProgressBar;
import com.viewlift.tv.views.fragment.AppCmsTvErrorFragment;
import com.viewlift.tv.views.fragment.ClearDialogFragment;
import com.viewlift.utils.CommonUtils;
import com.viewlift.views.components.AppCMSPresenterComponent;

import java.util.Arrays;
import java.util.List;

import static android.view.View.VISIBLE;
import static com.google.android.exoplayer2.Player.STATE_ENDED;

/**
 * Created by viewlift on 6/22/17.
 */

public class AppCmsTVSplashActivity extends AppCompatActivity implements AppCmsTvErrorFragment.ErrorFragmentListener {

    private CountDownTimer countDownTimer;
    private boolean needSplashProgress;

    private static final String TAG = "ADMMessenger";
    private FrameLayout videoPlayerLayout;
    private SimpleExoPlayer player;
    private AppCMSPresenter appCMSPresenter;
    /**
     * Catches intents sent from the onMessage() callback to update the UI.
     */
    private BroadcastReceiver msgReceiver;
    private Uri deepLinkQuery;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "Amazon Device Details: "+getDeviceDetail());
        String packageName = getApplicationContext().getPackageName();
        List<String> strings = Arrays.asList(getResources().getStringArray(R.array.app_cms_splash_progress_needed));

        needSplashProgress = strings.contains(packageName);

        appCMSPresenter =
                ((AppCMSApplication) getApplication()).getAppCMSPresenterComponent().appCMSPresenter();
        appCMSPresenter.setPinVerified(false);
        if (getIntent() != null && getIntent().getAction() != null && getIntent().getData() != null) {
            if (getIntent().getAction().equalsIgnoreCase(getString(R.string.LAUNCHER_DEEPLINK_ACTION))) {
                appCMSPresenter.setIsTVAppLaunchTypeDeepLink(true);
                appCMSPresenter.setDeepLinkContentID(getIntent().getData().toString());

                /*In newer Fire TVs, with version 7.1.2, the AppCMSTVVideoPlayActivity doesn't close
                * when Alexa is requested to play a new movie, when one is already playing, to make
                * sure the Activity gets closed and a Broadcast is sent from here and received only
                * on the Video Activity.*/
                Intent intent = new Intent();
                intent.setAction(getString(R.string.deeplink_close_player_activity_action));
                sendBroadcast(intent);
            }

            if (CommonUtils.isOnePlusTV() && CommonUtils.isValidDeepLink(this, getIntent().getData())) {
                deepLinkQuery = getIntent().getData();
            }
        }
        if ((getIntent().getFlags() & Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT) != 0) {
            // Activity was brought to front and not created,
            // Thus finishing this will get us to the last viewed activity
            finish();
            return;
        }
        setContentView(R.layout.activity_launch_tv);
        ImageView imageView = (ImageView) findViewById(R.id.splash_logo);

        imageView.setBackgroundResource(R.drawable.tv_logo);
        getAppCmsMain();


        if (needSplashProgress) {
            ProgressBar progressBar = (ProgressBar) findViewById(R.id.loading_progress_bar);
            progressBar.setVisibility(View.VISIBLE);
            progressBar.getIndeterminateDrawable().setColorFilter(
                    getResources().getColor(android.R.color.white), PorterDuff.Mode.MULTIPLY);
            countDownTimer = new CountDownTimer(11000 ,50) {
                @Override
                public void onTick(long l) {
                    progress = progress+1;
                    progressBar.setProgress(progress);
                }

                @Override
                public void onFinish() {

                }
            }.start();
        }

        if (Utils.isFireTVDevice(getApplicationContext())) {
            register();
            com.viewlift.tv.utility.Utils.broadcastCapabilities(this);
        }

    }

    int progress = 0;
    private void getAppCmsMain(){
        AppCMSPresenterComponent appCMSPresenterComponent =
                ((AppCMSApplication) getApplication()).getAppCMSPresenterComponent();

        if(appCMSPresenterComponent.appCMSPresenter().isNetworkConnected()){
        appCMSPresenterComponent.appCMSPresenter().getAppCMSMain(this,
                Utils.getProperty("SiteId", getApplicationContext()),
                deepLinkQuery != null ? deepLinkQuery : Uri.parse(""),
                AppCMSPresenter.PlatformType.TV,
                true, null);
        }else{
            showErrorFragment(true,null,true, getString(R.string.blank_string));
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(broadcastReceiver,new IntentFilter(AppCMSPresenter.ERROR_DIALOG_ACTION));
        registerReceiver(broadcastReceiver,new IntentFilter(AppCMSPresenter.ACTION_LOGO_ANIMATION));
        registerReceiver(broadcastReceiver, new IntentFilter(AppCMSPresenter.LETS_GO_ACTION));
        registerReceiver(broadcastReceiver, new IntentFilter(AppCMSPresenter.SUBSCRIPTION_DIALOG));
        if (Utils.isFireTVDevice(getApplicationContext())) {
            initADMReceiver();
        }
    }

    @Override
    protected void onPause() {
        unregisterReceiver(broadcastReceiver);
        if(null != countDownTimer)
        countDownTimer.cancel();
        if (Utils.isFireTVDevice(getApplicationContext()))
          unregisterReceiver(msgReceiver);
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(AppCMSPresenter.ERROR_DIALOG_ACTION)){
                Bundle bundle = intent.getBundleExtra(getString(R.string.retryCallBundleKey));
                boolean shouldRetry = bundle.getBoolean(getString(R.string.retry_key));
                String message = bundle.getString("error_message");
                boolean show_header = bundle.getBoolean(getString(R.string.show_header));
                showErrorFragment(shouldRetry,message,show_header,getString(R.string.app_cms_dismiss_alert_dialog_button_text));
            }else if(intent.getAction().equals(AppCMSPresenter.ACTION_LOGO_ANIMATION)){

                if (!needSplashProgress) {
                    startLogoAnimation();
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            CustomProgressBar.getInstance(AppCmsTVSplashActivity.this).showProgressDialog(AppCmsTVSplashActivity.this,"");
                        }
                    },550);
                }

            }else if (intent.getAction().equals(AppCMSPresenter.LETS_GO_ACTION)) {
                createPlayer();
            } else if (intent.getAction().equals(AppCMSPresenter.SUBSCRIPTION_DIALOG)) {
                openEntitlementDialog();
            }
        }
    };


    private void startLogoAnimation() {
        final ImageView logo = (ImageView) findViewById(R.id.splash_logo);
        int logoWidth = logo.getWidth();
        int logoHeight = logo.getHeight();

        BitmapDrawable bd = (BitmapDrawable) this.getResources().getDrawable(R.drawable.app_logo);
        int smallWidth = bd.getBitmap().getWidth();
        int smallHeight = bd.getBitmap().getHeight();

        float xScale = 0;//(float) (((smallWidth * 100) / logoWidth)) / 100;
        float yScale = 0;//(float) (((smallHeight * 100) / logoHeight)) / 100;


        new Handler().post(new Runnable() {
            @Override
            public void run() {
                ObjectAnimator translateX = ObjectAnimator.ofFloat(logo, "translationX",
                        ((Resources.getSystem().getDisplayMetrics().widthPixels / 2) - smallWidth / 2 - getResources().getDimension(R.dimen.logo_margin)));
                translateX.setDuration(500);
                translateX.start();

                ObjectAnimator translateY = ObjectAnimator.ofFloat(logo, "translationY",
                        ((Resources.getSystem().getDisplayMetrics().heightPixels / 2) - smallHeight / 2 - getResources().getDimension(R.dimen.logo_margin)));
                translateY.setDuration(500);
                translateY.start();

                ObjectAnimator anim = ObjectAnimator.ofFloat(logo, "scaleX", xScale);
                anim.setDuration(500); // duration 3 seconds
                anim.start();

                ObjectAnimator anim2 = ObjectAnimator.ofFloat(logo, "scaleY", yScale);
                anim2.setDuration(500); // duration 3 seconds
                anim2.start();
            }
        });
    }


    private String getDeviceDetail(){
        StringBuffer stringBuffer = new StringBuffer();
        try {
            String AMAZON_MODEL = Build.MODEL;
            if (Utils.isFireTVDevice(getApplicationContext())) {
                stringBuffer.append("FireTV :: ");
            } else {
                stringBuffer.append("NOT A FireTV :: ");
            }
            if (AMAZON_MODEL.matches("AFTN")) {
                stringBuffer.append("Firetv Gen = 3rd");
            } else if (AMAZON_MODEL.matches("AFTS")) {
                stringBuffer.append("Firetv  Gen = 2nd");
            } else if (AMAZON_MODEL.matches("AFTB")) {
                stringBuffer.append("Firetv  Gen = 1st");
            } else if (AMAZON_MODEL.matches("AFTT")) {
                stringBuffer.append("FireStick  Gen = 2nd");
            } else if (AMAZON_MODEL.matches("AFTM")) {
                stringBuffer.append("FireStick  Gen = 1st");
            } else if (AMAZON_MODEL.matches("AFTRS")) {
                stringBuffer.append("FireTV Edition ");
            } else {
                stringBuffer.append(AMAZON_MODEL);
            }
            stringBuffer.append(" SDK_INT = " + Build.VERSION.SDK_INT);
        }catch (Exception e){

        }
        return stringBuffer.toString();
    }

    public void showErrorFragment(boolean shouldRegisterInternetReciever, String message, boolean showHeader, String negBtnTxt){
        CustomProgressBar.getInstance(this).dismissProgressDialog();
        Bundle bundle = new Bundle();
        bundle.putBoolean(getString(R.string.retry_key) , shouldRegisterInternetReciever);
        bundle.putBoolean(getString(R.string.register_internet_receiver_key) , shouldRegisterInternetReciever);
        bundle.putString(getString(R.string.tv_dialog_msg_key),message);
        bundle.putBoolean(getString(R.string.show_header),showHeader);
        bundle.putString(getString(R.string.close_btn_txt),negBtnTxt);
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        AppCmsTvErrorFragment errorActivityFragment = AppCmsTvErrorFragment.newInstance(
                bundle);
        errorActivityFragment.setErrorListener(this);
        errorActivityFragment.show(ft, getString(R.string.error_dialog_fragment_tag));
    }


    @Override
    public void onErrorScreenClose(boolean closeActivity) {
        finish();
    }

    @Override
    public void onRetry(Bundle bundle) {
        getAppCmsMain();
    }


    @Override
    protected void onStop() {
        CustomProgressBar.getInstance(this).dismissProgressDialog();
        super.onStop();
        finish();
    }
    /**
     * Register the app with ADM and send the registration ID to your server
     */
    private void register() {
        final ADM adm = new ADM(this);
        if (adm.isSupported()) {
            if (adm.getRegistrationId() == null) {
                adm.startRegister();
            } else {
                /* Send the registration ID for this app instance to your server.
                 This is a redundancy since this should already have been performed at registration time from the onRegister() callback
                 but we do it because our python server doesn't save registration IDs.*/


                final String admRegistrationId = adm.getRegistrationId();
                Log.i(TAG, "ADM registration Id:" + admRegistrationId);

                final AppCMSADMServerMsgHandler srv = new AppCMSADMServerMsgHandler();
                srv.registerAppInstance(getApplicationContext(), adm.getRegistrationId());
            }
        }
    }

    /**
     * Unregister the app with ADM.
     * Your server will get notified from the SampleADMMessageHandler:onUnregistered() callback
     */
    private void unregister() {
        final ADM adm = new ADM(this);
        if (adm.isSupported()) {
            if (adm.getRegistrationId() != null) {
                adm.startUnregister();
            }
        }
    }


    /**
     * Create a {@link BroadcastReceiver} for listening to messages from ADM.
     *
     * @param msgKey  String to access message field from data JSON.
     * @param timeKey String to access timeStamp field from data JSON.
     * @return {@link BroadcastReceiver} for listening to messages from ADM.
     */
    private BroadcastReceiver createBroadcastReceiver(final String msgKey,
                                                      final String timeKey) {
        BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {

            /** {@inheritDoc} */
            @Override
            public void onReceive(final Context context, final Intent broadcastIntent) {
                if (broadcastIntent != null) {

                    /* Extract message from the extras in the intent. */
                    final String msg = broadcastIntent.getStringExtra(msgKey);
                    final String srvTimeStamp = broadcastIntent.getStringExtra(timeKey);

                    if (msg != null && srvTimeStamp != null) {
                        Log.i(TAG, msg);

                        /* Display the message in the UI. */
//                        final TextView tView = (TextView)findViewById(R.id.textMsgServer);
//                        tView.append("Server Time Stamp: " + srvTimeStamp + "\nMessage from server: " + msg + "\n\n");
                    }

                    /* Clear notifications if any. */
                    final NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                    mNotificationManager.cancel(12345678);
                }
            }
        };
        return broadcastReceiver;
    }

    private void initADMReceiver() {

        /* String to access message field from data JSON. */
        final String msgKey = getString(R.string.json_data_msg_key);

        /* String to access timeStamp field from data JSON. */
        final String timeKey = getString(R.string.json_data_time_key);

        /* Intent action that will be triggered in onMessage() callback. */
        final String intentAction = getString(R.string.intent_msg_action);

        /* Intent category that will be triggered in onMessage() callback. */
        final String msgCategory = getString(R.string.intent_msg_category);

        final Intent nIntent = getIntent();
        if (nIntent != null) {
            /* Extract message from the extras in the intent. */
            final String msg = nIntent.getStringExtra(msgKey);
            final String srvTimeStamp = nIntent.getStringExtra(timeKey);

            /* If msgKey and timeKey extras exist then we're coming from clicking a notification intent. */
            if (msg != null && srvTimeStamp != null) {
                Log.i(TAG, msg);
                /* Display the message in the UI. */
//                final TextView tView = (TextView)findViewById(R.id.textMsgServer);
                Log.d(TAG, "Server Time Stamp: " + srvTimeStamp + "\nMessage from server: " + msg + "\n\n");

                /* Clear notifications if any. */
                final NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                mNotificationManager.cancel(12345678);
            }
        }

        /* Listen for messages coming from SampleADMMessageHandler onMessage() callback. */
        msgReceiver = createBroadcastReceiver(msgKey, timeKey);
        final IntentFilter messageIntentFilter = new IntentFilter(intentAction);
        messageIntentFilter.addCategory(msgCategory);
        this.registerReceiver(msgReceiver, messageIntentFilter);
    }

    private void createPlayer() {
        videoPlayerLayout = findViewById(R.id.videoPlayer);
        videoPlayerLayout.setVisibility(View.VISIBLE);
        PlayerView playerView = new PlayerView(this);
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT);
        playerView.setLayoutParams(layoutParams);
        videoPlayerLayout.addView(playerView);
        player = ExoPlayerFactory.newSimpleInstance(this, new DefaultTrackSelector());
        playerView.setPlayer(player);

        final RawResourceDataSource rawResourceDataSource = new RawResourceDataSource(this);
//        String uri = "android.resource://" + getPackageName() + "/" + R.raw.welcome_video;
        DataSpec dataSpec = new DataSpec(RawResourceDataSource.buildRawResourceUri(R.raw.welcome_video));
        try {
            rawResourceDataSource.open(dataSpec);

            DataSource.Factory factory = new DataSource.Factory() {
                @Override
                public DataSource createDataSource() {
                    return rawResourceDataSource;
                }
            };
            MediaSource videoSource = new ExtractorMediaSource.Factory(factory).createMediaSource(rawResourceDataSource.getUri());

            LoopingMediaSource loopingMediaSource = new LoopingMediaSource(videoSource);
            player.prepare(videoSource);

        } catch (RawResourceDataSource.RawResourceDataSourceException e) {
            e.printStackTrace();
        }
        playerView.setBackgroundColor(ContextCompat.getColor(this, android.R.color.black));
        playerView.hideController();
        playerView.setControllerVisibilityListener(new PlayerControlView.VisibilityListener() {
            @Override
            public void onVisibilityChange(int visibility) {
                if (visibility == VISIBLE) {
                    playerView.hideController();
                }
            }
        });
        player.setPlayWhenReady(true);
        player.addListener(new Player.EventListener() {
            @Override
            public void onTimelineChanged(Timeline timeline, Object manifest, int reason) {
                View currentFocus = AppCmsTVSplashActivity.this.getCurrentFocus();
            }

            @Override
            public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {

                View currentFocus = AppCmsTVSplashActivity.this.getCurrentFocus();

            }

            @Override
            public void onLoadingChanged(boolean isLoading) {
                View currentFocus = AppCmsTVSplashActivity.this.getCurrentFocus();
            }

            @Override
            public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
                switch (playbackState) {
                    case STATE_ENDED:
                        if (player != null) {
                            player.stop();
                        }
                        appCMSPresenter.navigateToHomePage(false);
                        break;
                }
                View currentFocus = AppCmsTVSplashActivity.this.getCurrentFocus();
            }

            @Override
            public void onRepeatModeChanged(int repeatMode) {
                View currentFocus = AppCmsTVSplashActivity.this.getCurrentFocus();
            }

            @Override
            public void onShuffleModeEnabledChanged(boolean shuffleModeEnabled) {
                View currentFocus = AppCmsTVSplashActivity.this.getCurrentFocus();
            }

            @Override
            public void onPlayerError(ExoPlaybackException error) {
                View currentFocus = AppCmsTVSplashActivity.this.getCurrentFocus();
            }

            @Override
            public void onPositionDiscontinuity(int reason) {
                View currentFocus = AppCmsTVSplashActivity.this.getCurrentFocus();
            }

            @Override
            public void onPlaybackParametersChanged(PlaybackParameters playbackParameters) {
                View currentFocus = AppCmsTVSplashActivity.this.getCurrentFocus();
            }

            @Override
            public void onSeekProcessed() {
                View currentFocus = AppCmsTVSplashActivity.this.getCurrentFocus();
            }
        });
    }

    public void openEntitlementDialog() {
        String dialogMessage;
        String positiveButtonText;
        String dialogTitle;

        if (appCMSPresenter.isUserLoggedIn() && appCMSPresenter.getAppPreference().getLoggedInUserEmail() != null) {
            dialogMessage = appCMSPresenter.getLocalisedStrings().getPremiumLoggedInUserMsg();
            if (appCMSPresenter.getGenericMessagesLocalizationMap() != null
                    && appCMSPresenter.getGenericMessagesLocalizationMap().getWebSubscriptionMessagePrefix() != null
                    && appCMSPresenter.getGenericMessagesLocalizationMap().getWebSubscriptionMessageSuffix() != null) {
                dialogMessage = appCMSPresenter.getGenericMessagesLocalizationMap().getWebSubscriptionMessagePrefix()
                        + " " + appCMSPresenter.getGenericMessagesLocalizationMap().getWebSubscriptionMessageSuffix();
            }
            dialogTitle = appCMSPresenter.getLocalisedStrings().getPremiumContentText();
            positiveButtonText = "";
        } else {
            dialogMessage = appCMSPresenter.getLocalisedStrings().getWebSubscriptionMessagePrefixText()
                    + " " + appCMSPresenter.getLocalisedStrings().getWebSubscriptionMessageSuffixText();
            dialogTitle = appCMSPresenter.getLocalisedStrings().getLoginRequiredText();
            positiveButtonText = appCMSPresenter.getLocalisedStrings().getSignInText();
        }


        ClearDialogFragment newFragment = com.viewlift.tv.utility.Utils.getClearDialogFragment(
                this,
                appCMSPresenter,
                getResources().getDimensionPixelSize(R.dimen.text_clear_dialog_width),
                getResources().getDimensionPixelSize(R.dimen.text_add_to_watchlist_sign_in_dialog_height),
                dialogTitle,
                dialogMessage,
                positiveButtonText,
                appCMSPresenter.getLocalisedStrings().getCancelText(),
                14);

        newFragment.setOnPositiveButtonClicked(s -> navigateToLoginFragment());
        newFragment.setOnNegativeButtonClicked(s -> finish());
        newFragment.setOnBackKeyListener(s -> finish());
    }

    public void openSubscriptionDialog(Intent intent) {
        if (null != intent) {
            Bundle bundle = intent.getBundleExtra(getString(R.string.app_cms_bundle_key));
            if (null != bundle) {
                if (bundle.getBoolean(getString(R.string.shouldNavigateToLogin), false)) {
                    navigateToLoginFragment();
                    return;
                }
                String dialogTitle   = bundle.getString(getString(R.string.tv_dialog_header_key));
                String dialogMessage = bundle.getString(getString(R.string.tv_dialog_msg_key));
                String positiveButtonText = appCMSPresenter.getLanguageResourcesFile().getUIresource(getString(R.string.app_cms_subscribe_now));
                String cancelCTA = appCMSPresenter.getLanguageResourcesFile().getUIresource(getString(R.string.cancel));

                if (appCMSPresenter.getGenericMessagesLocalizationMap() != null
                        && appCMSPresenter.getGenericMessagesLocalizationMap().getSubscribeNowDialogButton() != null) {
                    positiveButtonText = appCMSPresenter.getGenericMessagesLocalizationMap().getSubscribeNowDialogButton();
                }
                if (appCMSPresenter.getGenericMessagesLocalizationMap() != null
                        && appCMSPresenter.getGenericMessagesLocalizationMap().getErrorDialogCancelCta() != null) {
                    cancelCTA = appCMSPresenter.getGenericMessagesLocalizationMap().getErrorDialogCancelCta();
                }

                ClearDialogFragment newFragment = com.viewlift.tv.utility.Utils.getClearDialogFragment(
                        this,
                        appCMSPresenter,
                        getResources().getDimensionPixelSize(R.dimen.text_clear_dialog_width),
                        getResources().getDimensionPixelSize(R.dimen.text_add_to_watchlist_sign_in_dialog_height),
                        dialogTitle,
                        dialogMessage,
                        positiveButtonText,
                        cancelCTA,
                        14);

                newFragment.setOnPositiveButtonClicked(s -> navigateToLoginFragment());
                newFragment.setOnNegativeButtonClicked(s -> finish());
                newFragment.setOnBackKeyListener(s -> finish());

            }
        }

    }

    private void navigateToLoginFragment() {
        appCMSPresenter.setLaunchType(AppCMSPresenter.LaunchType.DEEPLINK);
        com.viewlift.tv.utility.Utils.pageLoading(true, this);
        NavigationUser navigationUser = appCMSPresenter.getLoginNavigation();
        appCMSPresenter.navigateToTVPage(navigationUser.getPageId(), navigationUser.getTitle(), navigationUser.getUrl(),
                false, Uri.EMPTY, false, false,
                true, false, false, false);
    }
}

