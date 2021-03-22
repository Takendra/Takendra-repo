package com.viewlift.views.activity;

import android.animation.AnimatorSet;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DownloadManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.ActivityNotFoundException;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.IntentSender;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.Typeface;
import android.media.AudioManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.StrictMode;
import android.support.v4.media.MediaMetadataCompat;
import android.telephony.PhoneNumberUtils;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.OrientationEventListener;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.inputmethod.InputMethodManager;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.appcompat.widget.SwitchCompat;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.billingclient.api.BillingClient;
import com.android.billingclient.api.BillingResult;
import com.android.billingclient.api.Purchase;
import com.android.billingclient.api.PurchasesUpdatedListener;
import com.bumptech.glide.Glide;
import com.clevertap.android.sdk.CleverTapAPI;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.offline.DownloadRequest;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.credentials.Credential;
import com.google.android.gms.auth.api.credentials.HintRequest;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.util.Strings;
import com.google.android.gms.tasks.Task;
import com.google.android.material.appbar.AppBarLayout;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.crashlytics.FirebaseCrashlytics;
import com.jakewharton.threetenabp.AndroidThreeTen;
import com.urbanairship.UAirship;
import com.urbanairship.actions.DeepLinkListener;
import com.viewlift.AppCMSApplication;
import com.viewlift.BuildConfig;
import com.viewlift.EventReceiver;
import com.viewlift.R;
import com.viewlift.Utils;
import com.viewlift.audio.AudioServiceHelper;
import com.viewlift.audio.ui.PlaybackControlsFragment;
import com.viewlift.audio.utils.TaskRemoveService;
import com.viewlift.casting.CastHelper;
import com.viewlift.casting.CastServiceProvider;
import com.viewlift.ccavenue.screens.EnterMobileNumberActivity;
import com.viewlift.db.AppPreference;
import com.viewlift.models.data.appcms.api.AppCMSLibraryResult;
import com.viewlift.models.data.appcms.api.AppCMSPageAPI;
import com.viewlift.models.data.appcms.api.ContentDatum;
import com.viewlift.models.data.appcms.api.Module;
import com.viewlift.models.data.appcms.ui.AppCMSUIKeyType;
import com.viewlift.models.data.appcms.ui.android.Navigation;
import com.viewlift.models.data.appcms.ui.android.NavigationFooter;
import com.viewlift.models.data.appcms.ui.android.NavigationPrimary;
import com.viewlift.models.data.appcms.ui.android.NavigationUser;
import com.viewlift.models.data.appcms.ui.main.AppCMSMain;
import com.viewlift.models.data.appcms.ui.main.LocalisedStrings;
import com.viewlift.models.data.appcms.ui.main.Ratings;
import com.viewlift.models.data.appcms.ui.page.AppCMSPageUI;
import com.viewlift.models.data.appcms.ui.page.Component;
import com.viewlift.models.data.appcms.ui.page.ModuleList;
import com.viewlift.models.network.background.tasks.SearchQuery;
import com.viewlift.models.network.rest.AppCMSSearchCall;
import com.viewlift.presenters.AppCMSPresenter;
import com.viewlift.presenters.BitmapCachePresenter;
import com.viewlift.utils.AppUpdateHelper;
import com.viewlift.utils.BillingHelper;
import com.viewlift.utils.CommonUtils;
import com.viewlift.utils.GetSocialHelper;
import com.viewlift.utils.RecyclerItemClickListner;
import com.viewlift.views.adapters.AppCMSLeftNavigationMenuAdapter;
import com.viewlift.views.binders.AppCMSBinder;
import com.viewlift.views.customviews.BaseView;
import com.viewlift.views.customviews.CustomWebView;
import com.viewlift.views.customviews.MiniPlayerView;
import com.viewlift.views.customviews.NavBarItemView;
import com.viewlift.views.customviews.TabCreator;
import com.viewlift.views.customviews.ViewCreator;
import com.viewlift.views.customviews.constraintviews.ConstraintViewCreator;
import com.viewlift.views.dialog.CustomShape;
import com.viewlift.views.fragments.AppCMSChangePasswordFragment;
import com.viewlift.views.fragments.AppCMSEditProfileFragment;
import com.viewlift.views.fragments.AppCMSGetSocialFragment;
import com.viewlift.views.fragments.AppCMSMoreFragment;
import com.viewlift.views.fragments.AppCMSNavItemsFragment;
import com.viewlift.views.fragments.AppCMSNoPurchaseFragment;
import com.viewlift.views.fragments.AppCMSPageFragment;
import com.viewlift.views.fragments.AppCMSParentalControlsFragment;
import com.viewlift.views.fragments.AppCMSParentalPINFragment;
import com.viewlift.views.fragments.AppCMSParentalRatingFragment;
import com.viewlift.views.fragments.AppCMSReauthoriseUserFragment;
import com.viewlift.views.fragments.AppCMSRedemptionSuccessDialog;
import com.viewlift.views.fragments.AppCMSResetPasswordFragment;
import com.viewlift.views.fragments.AppCMSRestDeatilsFragment;
import com.viewlift.views.fragments.AppCMSSearchFragment;
import com.viewlift.views.fragments.AppCMSTeamListFragment;
import com.viewlift.views.fragments.AppCMSWebviewFragment;
import com.viewlift.views.fragments.GenericAuthenticationFragment;
import com.viewlift.views.fragments.MathProblemFragment;
import com.viewlift.views.fragments.TVProviderFragment;
import com.viewlift.views.fragments.UserProfileSettingsFragment;
import com.viewlift.views.rxbus.AppBus;

import org.json.JSONException;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.EmptyStackException;
import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Locale;
import java.util.Map;
import java.util.Stack;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Completable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.rxjava3.functions.Consumer;
import rx.functions.Action0;
import rx.functions.Action1;

import static android.view.View.VISIBLE;
import static com.viewlift.presenters.AppCMSPresenter.JUSPAY_REQUEST_CODE;

public class AppCMSPageActivity extends AppCompatActivity implements
        AppCMSPageFragment.OnPageCreation,
        FragmentManager.OnBackStackChangedListener,
        GoogleApiClient.OnConnectionFailedListener,
        GoogleApiClient.ConnectionCallbacks,
        AppCMSSearchFragment.OnSaveSearchQuery,
        TabCreator.OnClickHandler, PurchasesUpdatedListener {
    private static final String TAG = "AppCMSPageActivity";
    private final static float CLICK_DRAG_TOLERANCE = 10; // Often, there will be a slight, unintentional, drag when the user taps the view, so we need to account for this.
    public static boolean isDailyView = false;
    private AudioManager audio;
    public boolean isCleverTapAvailable = false;
    private boolean isGoogleApiConnected = false;
    private boolean isRatingDialogShown = false;

    @BindView(R.id.app_cms_parent_layout)
    RelativeLayout appCMSParentLayout;

    @BindView(R.id.app_cms_page_loading_progressbar)
    ProgressBar loadingProgressBar;

    @BindView(R.id.app_cms_parent_view)
    RelativeLayout appCMSParentView;

    @BindView(R.id.app_cms_fragment)
    FrameLayout appCMSFragment;

    @BindView(R.id.no_search)
    AppCompatTextView no_search;

    @BindView(R.id.app_cms_fixed_banner_view)
    LinearLayoutCompat fixedBannerView;

    @BindView(R.id.bannerId)
    AppCompatTextView bannerId;

    @BindView(R.id.search_layout)
    RelativeLayout search_layout;

    @BindView(R.id.app_cms_appbarlayout)
    AppBarLayout appBarLayout;

    @BindView(R.id.app_cms_tab_nav_container)
    LinearLayoutCompat appCMSTabNavContainer;

    @BindView(R.id.ll_media_route_button)
    LinearLayoutCompat ll_media_route_button;

    @BindView(R.id.media_route_button)
    AppCompatImageButton mMediaRouteButton;

    @BindView(R.id.app_cms_close_button)
    AppCompatImageButton closeButton;

    @BindView(R.id.app_cms_cast_conroller)
    FrameLayout appCMSCastController;

    @BindView(R.id.readMessageIdFl)
    FrameLayout readMessageIdFl;

    @BindView(R.id.new_version_available_parent)
    FrameLayout newVersionUpgradeAvailable;

    @BindView(R.id.new_version_available_textview)
    AppCompatTextView newVersionAvailableTextView;

    @BindView(R.id.cart_badge)
    AppCompatTextView cart_badge_count;

    @BindView(R.id.new_version_available_close_button)
    AppCompatImageButton newVersionAvailableCloseButton;

    @BindView(R.id.app_cms_search_button)
    AppCompatImageButton mSearchTopButton;

    @BindView(R.id.app_cms_share_button)
    AppCompatImageButton mShareTopButton;

    @BindView(R.id.app_cms_profile_btn)
    AppCompatImageButton mProfileTopButton;

    @BindView(R.id.app_cms_left_nav_btn)
    AppCompatImageButton mLefTNavDrawerButton;

    @BindView(R.id.bellIcon)
    AppCompatImageButton bellIcon;

    @BindView(R.id.app_cms_toolbar)
    Toolbar toolbar;

    @BindView(R.id.app_cms_start_free_trial_tool)
    AppCompatTextView appCMSNavFreeTrialTool;

    @BindView(R.id.app_cms_toolbar_logo)
    AppCompatImageView appCMSHeaderImage;

    @BindView(R.id.app_cms_mini_player_available)
    SwitchCompat showMiniPlayer;

    @BindView(R.id.app_cms_new_template_header)
    LinearLayoutCompat appCMSNewsTemplateHeader;

    @BindView(R.id.app_cms_left_Navigation_Drawer_list)
    RecyclerView appCMSLeftDrawerList;

    @BindView(R.id.app_cms_left_drawer_layout)
    DrawerLayout appCMSLeftDrawerLayout;

    @BindView(R.id.btnHeaderSubscribe)
    AppCompatTextView appCMSbtnHeaderSubscribe;


    @Inject
    public AppCMSSearchCall appCMSSearchCall;

    @Inject
    AppPreference appPreference;
    @Inject
    AppCMSPresenter appCMSPresenter;
    @Inject
    LocalisedStrings localisedStrings;
    private Stack<String> appCMSBinderStack;
    private Map<String, AppCMSBinder> appCMSBinderMap;

    private EventReceiver eventReceiver = new EventReceiver();
    private BroadcastReceiver presenterActionReceiver;
    private BroadcastReceiver presenterCloseActionReceiver;
    private BroadcastReceiver networkConnectedReceiver;
    private BroadcastReceiver wifiConnectedReceiver;
    private BroadcastReceiver downloadReceiver;
    private BroadcastReceiver notifyUpdateListsReceiver;
    private BroadcastReceiver refreshPageDataReceiver;
    private BroadcastReceiver phoneHintReceiver;
    private BroadcastReceiver clearBackStackReceiver;
    private BroadcastReceiver processDeeplinkReceiver;
    private BroadcastReceiver processOpenBrowserReceiver;
    private BroadcastReceiver enterFullScreenReceiver;
    private BroadcastReceiver exitFullScreenReceiver;
    private BroadcastReceiver keepScreenOnReceiver;
    private BroadcastReceiver clearKeepScreenOnReceiver;
    private BroadcastReceiver chromecastDisconnectedReceiver;
    private BroadcastReceiver uaReceiveChannelIdReceiver;
    private BroadcastReceiver uaReceiveAppKeyReceiver;
    private BroadcastReceiver gmsReceiveInstanceIdReceiver;
    private BroadcastReceiver progressDialogReceiver;
    ProgressDialog progressDownloadDialog = null;
    String package_name;
    private GoogleApiClient mGoogleApiClient;

    private boolean resumeInternalEvents;
    public boolean isActive;
    private boolean shouldSendCloseOthersAction;
    private AppCMSBinder updatedAppCMSBinder;
    private CallbackManager callbackManager;
    private AccessToken accessToken;
    private boolean handlingClose;
    private boolean castDisabled;
    private ConnectivityManager connectivityManager;
    private WifiManager wifiManager;
    private String searchQuery;
    private boolean isDownloadPageOpen = false;
    private boolean loaderWaitingFor3rdPartyLogin = false;
    private LinearLayoutCompat appCMSTabNavContainerItems;
    private Uri pendingDeeplinkUri;
    private TabCreator tabCreator;

    private boolean libsThreadExecuted;
    private float downRawX, downRawY;
    private float dX, dY;
    private final String mobileLaunchActivity = "com.viewlift.mobile.AppCMSLaunchActivity";
    private int PLAY_SERVICES_RESOLUTION_REQUEST = 1001;
    private boolean isTabCreated = false;
    public boolean isFromLogin = false;
    private Typeface face;
    public String leftNavId, bottomTabHeader;
    ArrayList<NavigationPrimary> leftNavList;
    ArrayList<String> logoURL = new ArrayList<>();
    int posOfLogo = 0;
    NavBarItemView navBarItemView;

    private boolean isFixedBannerHidden;

    private AppUpdateHelper mAppUpdateHelper;

    private Disposable disposable;


    AppCMSLeftNavigationMenuAdapter appCMSLeftNavigationMenuAdapter;

    private boolean needToCheckReferralData;

    private boolean checkPlayServices() {
        try {
            if (BuildConfig.FLAVOR.equalsIgnoreCase(AppCMSPresenter.MOBILE_BUILD_VARIENT)) {
                GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
                int resultCode = apiAvailability.isGooglePlayServicesAvailable(this);
                if (resultCode != ConnectionResult.SUCCESS) {

                    if (apiAvailability.isUserResolvableError(resultCode)) {
                        if (BuildConfig.FLAVOR.equalsIgnoreCase(AppCMSPresenter.MOBILE_BUILD_VARIENT)) {
                            apiAvailability.getErrorDialog(this, resultCode, PLAY_SERVICES_RESOLUTION_REQUEST)
                                    .show();
                        }
                    } /*else {
                Log.i(TAG, "This device is not supported.");
                Toast.makeText(this, "This device is not supported.", Toast.LENGTH_SHORT).show();
                finish();
            }*/
                    return false;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (!BaseView.isTablet(this)) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();

        StrictMode.setThreadPolicy(policy);
        ((AppCMSApplication) getApplicationContext()).getAppCMSPresenterComponent().inject(this);
        int layout = R.layout.activity_appcms_page;
        if (appCMSPresenter.isNewsTemplate()) {
            layout = R.layout.activity_appcms_news_page;
        }
        setContentView(layout);
        try {
            checkPlayServices();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        ButterKnife.bind(this);
        if (CommonUtils.isServiceInStartedState)
            AudioServiceHelper.getAudioInstance().createMediaBrowserService(this);
        else
            AudioServiceHelper.getAudioInstance().setActivity(this);
        AudioServiceHelper.getAudioInstance().setCallBack(callbackAudioService);
        if (appCMSPresenter.getTemplateType() == AppCMSPresenter.TemplateType.NEWS)
            startService(new Intent(getBaseContext(), TaskRemoveService.class));
        if (appCMSPresenter.isNetworkConnected())
            appPreference.setAppLaunchTime();
        if (appCMSPresenter.getPlatformType() != AppCMSPresenter.PlatformType.TV) {
            clearAPICache();
        }
        if (appCMSPresenter.getAppPreference() != null && appCMSPresenter.getAppPreference().getShowPIPVisibility()) {
            appCMSPresenter.setMiniPLayerVisibility(true);
        }
        appCMSBinderStack = new Stack<>();
        appCMSBinderMap = new HashMap<>();
        initPageActivity();
        checkCleverTapSDK();
        if (getIntent() != null && getIntent().getBooleanExtra(AppCMSPresenter.EXTRA_OPEN_AUDIO_PLAYER, false)) {
            if (appCMSPresenter != null && !appPreference.getAppHomeActivityCreated() && !appCMSPresenter.isAudioActvityVisible()) {
                try {
                    Class launchActivity = Class.forName(mobileLaunchActivity);
                    startActivity(new Intent(this, launchActivity));
                } catch (Exception ignored) {
                }
                finish();
            } else {
                if (checkPlayServices()) {
                    Intent fullScreenIntent = new Intent(this, AppCMSPlayAudioActivity.class)
                            .setFlags(
                                    Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    if (fullScreenIntent.getParcelableExtra(
                            PlaybackControlsFragment.EXTRA_CURRENT_MEDIA_DESCRIPTION) != null) {
                        MediaMetadataCompat description = fullScreenIntent.getParcelableExtra(
                                PlaybackControlsFragment.EXTRA_CURRENT_MEDIA_DESCRIPTION);
                        fullScreenIntent.putExtra(AppCMSPresenter.EXTRA_CURRENT_MEDIA_DESCRIPTION, description);
                    }
                    startActivity(fullScreenIntent);
                }
            }
            appPreference.setAppHomeActivityCreated(true);

        }
        Bundle args = getIntent().getBundleExtra(getString(R.string.app_cms_bundle_key));
        if (args != null) {
            try {
                updatedAppCMSBinder =
                        (AppCMSBinder) args.getBinder(getString(R.string.app_cms_binder_key));
                if (updatedAppCMSBinder != null) {
                    shouldSendCloseOthersAction = updatedAppCMSBinder.shouldSendCloseAction();
                }
            } catch (ClassCastException e) {
                Log.e(TAG, "Could not read AppCMSBinder: " + e.toString());
            }
        }

        clearBackStackReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (intent.getAction().equals(AppCMSPresenter.PRESENTER_CLEAR_BACKSTACK_ACTION)) {
                    handleBack(true, true, true, true);
                }
            }
        };
        presenterActionReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (intent != null &&
                        intent.getStringExtra(getString(R.string.app_cms_package_name_key)) != null &&
                        !intent.getStringExtra(getString(R.string.app_cms_package_name_key)).equals(getPackageName())) {
                    return;
                }

                if (intent == null ||
                        intent.getStringExtra(getString(R.string.app_cms_package_name_key)) == null) {
                    return;
                }

                if (intent.getAction() != null
                        && intent.getAction().equals(AppCMSPresenter.PRESENTER_NAVIGATE_ACTION)) {

                    Bundle args = intent.getBundleExtra(getString(R.string.app_cms_bundle_key));
                    try {
                        String previousPage = "";
                        if (updatedAppCMSBinder != null && updatedAppCMSBinder.getScreenName() != null)
                            previousPage = updatedAppCMSBinder.getScreenName();
                        updatedAppCMSBinder =
                                (AppCMSBinder) args.getBinder(getString(R.string.app_cms_binder_key));
                        if (updatedAppCMSBinder != null) {
                            mergeInputData(updatedAppCMSBinder, updatedAppCMSBinder.getPageId());
                        }
                        appCMSPresenter.sendPageViewEvent(previousPage, updatedAppCMSBinder.getScreenName(), null);
                        checkedFixedBanner();
                        if (isActive) {
                            try {
                                handleLaunchPageAction(updatedAppCMSBinder,
                                        false,
                                        false,
                                        false);

                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        } else if (updatedAppCMSBinder != null) {
                            if (appCMSPresenter.getCurrentActivity() instanceof EnterMobileNumberActivity && appCMSPresenter.isHomePage(updatedAppCMSBinder.getPageId())) {
                                appCMSBinderStack.clear();
                                appCMSBinderMap.clear();
                            }
                            Intent appCMSIntent = new Intent(AppCMSPageActivity.this,
                                    AppCMSPageActivity.class);
                            appCMSIntent.putExtra(AppCMSPageActivity.this.getString(R.string.app_cms_bundle_key), args);
                            appCMSIntent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                            AppCMSPageActivity.this.startActivity(appCMSIntent);
                            if (updatedAppCMSBinder.shouldSendCloseAction()) {
                                shouldSendCloseOthersAction = true;
                            }
                        }
                    } catch (ClassCastException e) {
                        //Log.e(TAG, "Could not read AppCMSBinder: " + e.toString());
                    }
                } else if (intent.getAction().equals(AppCMSPresenter.PRESENTER_PAGE_LOADING_ACTION)) {
                    loaderWaitingFor3rdPartyLogin = intent.getBooleanExtra(getString(R.string.thrid_party_login_intent_extra_key), false);
                    pageLoading(true);
                } else if (intent.getAction().equals(AppCMSPresenter.PRESENTER_STOP_PAGE_LOADING_ACTION)) {
                    loaderWaitingFor3rdPartyLogin = false;
                    pageLoading(false);
                } else if (intent.getAction().equals(AppCMSPresenter.PRESENTER_RESET_NAVIGATION_ITEM_ACTION)) {
//                    Log.d(TAG, "Nav item - Received broadcast to select navigation item with page Id: " +
//                            intent.getStringExtra(getString(R.string.navigation_item_key)));
                    selectNavItem(intent.getStringExtra(getString(R.string.navigation_item_key)));
                } else if (intent.getAction().equals(AppCMSPresenter.PRESENTER_UPDATE_HISTORY_ACTION)) {
                    updateData();
                } else if (intent.getAction().equals(AppCMSPresenter.PRESENTER_REFRESH_PAGE_ACTION)) {
                    if (!appCMSBinderStack.isEmpty()) {
                        AppCMSBinder appCMSBinder = appCMSBinderMap.get(appCMSBinderStack.peek());

                        handleLaunchPageAction(appCMSBinder,
                                false,
                                false,
                                false);

                        if (GetSocialHelper.needToProcessReferralData) {
                            navigateToReferralPage();
                            GetSocialHelper.needToProcessReferralData = false;
                        }
                    }
                }
            }
        };

        processDeeplinkReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (intent != null &&
                        intent.getStringExtra(getPackageName()) != null &&
                        !intent.getStringExtra(getString(R.string.app_cms_package_name_key)).equals(getPackageName())) {
                    return;
                }
                String deeplinkUrl = intent.getStringExtra(getString(R.string.deeplink_uri_extra_key));
                if (intent.getStringExtra(getString(R.string.app_cms_package_name_key)) == null) {
                    return;
                }


                if (!TextUtils.isEmpty(deeplinkUrl)) {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    if (!isActive) {
                        if (appCMSPresenter.getCurrentContext() != null) {
                            try {
                                Intent appCMSIntent = new Intent(appCMSPresenter.getCurrentContext(),
                                        AppCMSPageActivity.class);
                                appCMSIntent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                                appCMSIntent.putExtra(getString(R.string.deeplink_uri_extra_key), deeplinkUrl);
                                appCMSPresenter.getCurrentContext().startActivity(appCMSIntent);
                            } catch (Exception e) {

                            }
                        }
                    } else {
                        processDeepLink(Uri.parse(deeplinkUrl));

                    }
                }
            }
        };
        processOpenBrowserReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (intent != null &&
                        intent.getStringExtra(getPackageName()) != null &&
                        !intent.getStringExtra(getString(R.string.app_cms_package_name_key)).equals(getPackageName())) {
                    return;
                }
                String openBrowserUrl = intent.getStringExtra(getString(R.string.openBrowser_uri_extra_key));
                if (intent.getStringExtra(getString(R.string.app_cms_package_name_key)) == null) {
                    return;
                }
                if (appCMSPresenter.getCurrentContext() != null) {
                    try {
                        Intent openBrowserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(openBrowserUrl));
                        openBrowserIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        appCMSPresenter.getCurrentContext().startActivity(openBrowserIntent);
                    } catch (Exception e) {

                    }
                }
            }
        };


        enterFullScreenReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (intent != null &&
                        intent.getStringExtra(getString(R.string.app_cms_package_name_key)) != null &&
                        !intent.getStringExtra(getString(R.string.app_cms_package_name_key)).equals(getPackageName())) {
                    return;
                }
                if (intent == null ||
                        intent.getStringExtra(getString(R.string.app_cms_package_name_key)) == null) {
                    return;
                }
                //enterFullScreenVideoPlayer();
            }
        };

        exitFullScreenReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (intent != null &&
                        intent.getStringExtra(getString(R.string.app_cms_package_name_key)) != null &&
                        !intent.getStringExtra(getString(R.string.app_cms_package_name_key)).equals(getPackageName())) {
                    return;
                }
                if (intent == null ||
                        intent.getStringExtra(getString(R.string.app_cms_package_name_key)) == null) {
                    return;
                }
                boolean relaunchPage = intent.getBooleanExtra(getString(R.string.exit_fullscreen_relaunch_page_extra_key), true);
                //exitFullScreenVideoPlayer(relaunchPage);
            }
        };

        presenterCloseActionReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (intent != null &&
                        intent.getStringExtra(getString(R.string.app_cms_package_name_key)) != null &&
                        !intent.getStringExtra(getString(R.string.app_cms_package_name_key)).equals(getPackageName())) {
                    return;
                }
                if (intent == null ||
                        intent.getStringExtra(getString(R.string.app_cms_package_name_key)) == null) {
                    return;
                }
                if (intent.getAction() != null && intent.getAction().equals(AppCMSPresenter.PRESENTER_CLOSE_SCREEN_ACTION)) {
                    boolean closeSelf = intent.getBooleanExtra(getString(R.string.close_self_key),
                            false);
                    boolean closeOnePage = intent.getBooleanExtra(getString(R.string.close_one_page_key), false);
                    if (closeSelf && !handlingClose && appCMSBinderStack.size() >= 1) {
                        handlingClose = true;
                        handleCloseAction(closeOnePage);
                        for (String appCMSBinderKey : appCMSBinderStack) {
                            AppCMSBinder appCMSBinder = appCMSBinderMap.get(appCMSBinderKey);
                            if (appCMSBinder != null) {
                                RefreshAppCMSBinderAction appCMSBinderAction =
                                        new RefreshAppCMSBinderAction(appCMSPresenter,
                                                appCMSBinder,
                                                appCMSPresenter.isUserLoggedIn());
                                try {
                                    appCMSPresenter.refreshPageAPIData(appCMSBinder.getAppCMSPageUI(),
                                            appCMSBinder.getPageId(),
                                            null,
                                            appCMSBinderAction);
                                } catch (Exception ex) {
                                }
                            }
                        }
                        handlingClose = false;
                    }

                    appCMSPresenter.initiateAfterLoginAction();
                }
            }
        };

        connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = null;

        if (connectivityManager != null) {
            activeNetwork = connectivityManager.getActiveNetworkInfo();
        }

        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();
        appCMSPresenter.setNetworkConnected(isConnected, null);
        if (activeNetwork != null) {
            appPreference.setActiveNetworkType(activeNetwork.getType());
        }
        networkConnectedReceiver = new BroadcastReceiver() {
            @SuppressLint("LongLogTag")
            @Override
            public void onReceive(Context context, Intent intent) {
                NetworkInfo activeNetwork = connectivityManager.getActiveNetworkInfo();
                boolean isConnected = activeNetwork != null &&
                        activeNetwork.isConnectedOrConnecting();
                String pageId = "";
                if (!appCMSBinderStack.isEmpty()) {
                    pageId = appCMSBinderStack.peek();

                    // if user is on video or audio player and content is already downloaded then dont move to download page so return fromm here
                    if ((((appCMSPresenter.getCurrentActivity() instanceof AppCMSPlayVideoActivity)) ||
                            ((appCMSPresenter.getCurrentActivity() instanceof AppCMSPlayAudioActivity))) &&
                            appCMSPresenter.getCurrentPlayingVideo() != null &&
                            appCMSPresenter.isVideoDownloaded(appCMSPresenter.getCurrentPlayingVideo())) {
                        return;
                    }

                    if ((((appCMSPresenter.getCurrentActivity() instanceof AppCMSPlayVideoActivity)) ||
                            ((appCMSPresenter.getCurrentActivity() instanceof AppCMSPlayAudioActivity))) &&
                            appCMSPresenter.getCurrentPlayingVideo() != null) {
                        DownloadRequest download = appCMSPresenter.getAppCMSApplication().getOfflineDRMManager().getDownloadTracker().isVideoOfflineDownloaded(appCMSPresenter.getCurrentPlayingVideo());
                        if (download != null)
                            return;
                    }

                    if (appPreference.getNetworkConnectedState() && !isConnected) {
                        appCMSPresenter.setShowNetworkConnectivity(true);
                        appCMSPresenter.showNoNetworkConnectivityToast();
                    } else {
                        appCMSPresenter.setShowNetworkConnectivity(false);
                        appCMSPresenter.cancelAlertDialog();
                    }
                    try {
                        if (isConnected) {
                            setCastingInstance();
                            castDisabled = false;
                        } else {
                            CastHelper.getInstance(AppCMSPageActivity.this).disconnectChromecastOnLogout();
                            castDisabled = true;
                        }
                    } catch (Exception ex) {
                    }
                }
                if (activeNetwork != null) {
                    appPreference.setActiveNetworkType(activeNetwork.getType());
                }
                /**
                 * Bellow code is to removing all the pages  if there are any, before opening download page
                 */
                if (!appPreference.getNetworkConnectedState() &&
                        isConnected &&
                        !appPreference.getPlayingVideo()) {
                    handleBack(true,
                            true,
                            (appCMSBinderStack != null && appCMSBinderStack.size() > 1) ? true : false,
                            true);
                }
                appCMSPresenter.setNetworkConnected(isConnected, pageId);
            }
        };

        if (getApplicationContext() != null) {
            wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
            wifiConnectedReceiver = new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {
                    if (intent != null &&
                            intent.getStringExtra(getString(R.string.app_cms_package_name_key)) != null &&
                            !intent.getStringExtra(getString(R.string.app_cms_package_name_key)).equals(getPackageName())) {
                        return;
                    }
                    if (intent == null ||
                            intent.getStringExtra(getString(R.string.app_cms_package_name_key)) == null) {
                        return;
                    }

                    appPreference.setWifiConnected(wifiManager.isWifiEnabled());
                }
            };
        }

        downloadReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (intent != null &&
                        intent.getStringExtra(getString(R.string.app_cms_package_name_key)) != null &&
                        !intent.getStringExtra(getString(R.string.app_cms_package_name_key)).equals(getPackageName())) {
                    return;
                }
                if (intent == null ||
                        intent.getStringExtra(getString(R.string.app_cms_package_name_key)) == null) {
                    return;
                }

                String action = intent.getAction();
                if (DownloadManager.ACTION_DOWNLOAD_COMPLETE.equals(action)) {
                    DownloadManager downloadManager = (DownloadManager) context.getSystemService(DOWNLOAD_SERVICE);
                    long referenceId = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);
                    DownloadManager.Query downloadQuery = new DownloadManager.Query();
                    downloadQuery.setFilterById(referenceId);

                    @SuppressWarnings("ConstantConditions")
                    Cursor cursor = downloadManager.query(downloadQuery);
                    if (cursor.moveToFirst()) {
                        try {
                            String mimeType = cursor.getString(cursor.getColumnIndexOrThrow(DownloadManager.COLUMN_MEDIA_TYPE));
                            int status = cursor.getInt(cursor.getColumnIndexOrThrow(DownloadManager.COLUMN_STATUS));
                            if (mimeType.contains("mp4") &&
                                    (status == DownloadManager.STATUS_SUCCESSFUL ||
                                            status == DownloadManager.STATUS_FAILED)) {
                                appCMSPresenter.startNextDownload();
                            }
                        } catch (Exception e) {
                        }
                    }
                }
            }
        };

        notifyUpdateListsReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (intent != null &&
                        intent.getStringExtra(getString(R.string.app_cms_package_name_key)) != null &&
                        !intent.getStringExtra(getString(R.string.app_cms_package_name_key)).equals(getPackageName())) {
                    return;
                }
                if (intent == null ||
                        intent.getStringExtra(getString(R.string.app_cms_package_name_key)) == null) {
                    return;
                }
                List<Fragment> fragmentList = getSupportFragmentManager().getFragments();
                for (Fragment fragment : fragmentList) {
                    if (fragment instanceof AppCMSPageFragment) {
                        ((AppCMSPageFragment) fragment).updateDataLists();
                    }
                }
            }
        };


        phoneHintReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (intent != null &&
                        intent.getStringExtra(getString(R.string.app_cms_package_name_key)) != null &&
                        !intent.getStringExtra(getString(R.string.app_cms_package_name_key)).equals(getPackageName())) {
                    return;
                }
                if (intent == null || intent.getStringExtra(getString(R.string.app_cms_package_name_key)) == null) {
                    return;
                }
                requestHint();
            }
        };

        refreshPageDataReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (intent != null &&
                        intent.getStringExtra(getString(R.string.app_cms_package_name_key)) != null &&
                        !intent.getStringExtra(getString(R.string.app_cms_package_name_key)).equals(getPackageName())) {
                    return;
                }
                if (intent == null ||
                        intent.getStringExtra(getString(R.string.app_cms_package_name_key)) == null) {
                    return;
                }
                refreshPageData();
            }
        };

        keepScreenOnReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (intent != null &&
                        intent.getStringExtra(getString(R.string.app_cms_package_name_key)) != null &&
                        !intent.getStringExtra(getString(R.string.app_cms_package_name_key)).equals(getPackageName())) {
                    return;
                }
                if (intent == null ||
                        intent.getStringExtra(getString(R.string.app_cms_package_name_key)) == null) {
                    return;
                }
                keepScreenOn();
            }
        };

        clearKeepScreenOnReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (intent != null &&
                        intent.getStringExtra(getString(R.string.app_cms_package_name_key)) != null &&
                        !intent.getStringExtra(getString(R.string.app_cms_package_name_key)).equals(getPackageName())) {
                    return;
                }
                if (intent == null ||
                        intent.getStringExtra(getString(R.string.app_cms_package_name_key)) == null) {
                    return;
                }

                clearKeepScreenOn();
            }
        };

        chromecastDisconnectedReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (intent != null &&
                        intent.getStringExtra(getString(R.string.app_cms_package_name_key)) != null &&
                        !intent.getStringExtra(getString(R.string.app_cms_package_name_key)).equals(getPackageName())) {
                    return;
                }
                if (intent == null ||
                        intent.getStringExtra(getString(R.string.app_cms_package_name_key)) == null) {
                    return;
                }
                handleLaunchPageAction(updatedAppCMSBinder,
                        false,
                        false,
                        false);
            }
        };

        uaReceiveChannelIdReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (intent != null) {
                    String channelId = intent.getStringExtra("channel_id");
                    appCMSPresenter.setUaChannelId(channelId);
                    System.out.println("Channel ID - >  " + channelId);
                }
            }
        };

        uaReceiveAppKeyReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (intent != null) {
                    String appKey = intent.getStringExtra("app_key");
                    appCMSPresenter.setUaAccessKey(appKey);
                }
            }
        };

        progressDialogReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (intent != null && intent.getBooleanExtra(getString(R.string.app_cms_bundle_key), false)) {
                    if (progressDownloadDialog == null) {
                        progressDownloadDialog = new ProgressDialog(AppCMSPageActivity.this);
                        progressDownloadDialog.setCancelable(false);
                    }
                    if (appCMSPresenter.isFetchingVideoQualities()) {
                        progressDownloadDialog.setMessage(localisedStrings.getVideoQualitiesText());
                    } else {
                        progressDownloadDialog.setMessage(localisedStrings.getAddToDownloadQueueText());
                    }
                    appCMSPresenter.setFetchingVideoQualities(false);
                    progressDownloadDialog.show();
                } else {
                    if (progressDownloadDialog != null) {
                        if (progressDownloadDialog.isShowing()) {
                            progressDownloadDialog.dismiss();
                        }
                    }
                }
            }
        };

        gmsReceiveInstanceIdReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (intent != null) {
                    String instanceId = intent.getStringExtra("gms_instance_id");
                    appPreference.setInstanceId(instanceId);
                }
            }
        };
        try {
            registerReceiver(presenterActionReceiver,
                    new IntentFilter(AppCMSPresenter.PRESENTER_NAVIGATE_ACTION));
            registerReceiver(presenterActionReceiver,
                    new IntentFilter(AppCMSPresenter.PRESENTER_PAGE_LOADING_ACTION));
            registerReceiver(presenterActionReceiver,
                    new IntentFilter(AppCMSPresenter.PRESENTER_STOP_PAGE_LOADING_ACTION));
            registerReceiver(presenterActionReceiver,
                    new IntentFilter(AppCMSPresenter.PRESENTER_RESET_NAVIGATION_ITEM_ACTION));
            registerReceiver(presenterActionReceiver,
                    new IntentFilter(AppCMSPresenter.PRESENTER_UPDATE_HISTORY_ACTION));
            registerReceiver(presenterActionReceiver,
                    new IntentFilter(AppCMSPresenter.PRESENTER_REFRESH_PAGE_ACTION));
            registerReceiver(refreshPageDataReceiver,
                    new IntentFilter(AppCMSPresenter.PRESENTER_REFRESH_PAGE_DATA_ACTION));
            registerReceiver(clearBackStackReceiver,
                    new IntentFilter(AppCMSPresenter.PRESENTER_CLEAR_BACKSTACK_ACTION));
            registerReceiver(wifiConnectedReceiver,
                    new IntentFilter(WifiManager.WIFI_STATE_CHANGED_ACTION));
            registerReceiver(downloadReceiver,
                    new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));
            registerReceiver(notifyUpdateListsReceiver,
                    new IntentFilter(AppCMSPresenter.PRESENTER_UPDATE_LISTS_ACTION));
            registerReceiver(processDeeplinkReceiver,
                    new IntentFilter(AppCMSPresenter.PRESENTER_DEEPLINK_ACTION));
            registerReceiver(processOpenBrowserReceiver,
                    new IntentFilter(AppCMSPresenter.PRESENTER_OPEN_BROWSER_ACTION));
            registerReceiver(progressDialogReceiver,
                    new IntentFilter(AppCMSPresenter.PRESENTER_PROGRESS_ACTION));
            registerReceiver(uaReceiveChannelIdReceiver,
                    new IntentFilter("receive_ua_channel_id"));
            registerReceiver(uaReceiveAppKeyReceiver,
                    new IntentFilter("receive_ua_app_key"));
            registerReceiver(gmsReceiveInstanceIdReceiver,
                    new IntentFilter("receive_gms_instance_id"));
        } catch (IllegalArgumentException ex) {
            ex.printStackTrace();
        }
        Intent registerInitReceivers = new Intent("INITIALIZATION");
        registerInitReceivers.putExtra("init_action", "register_receiver");
        sendBroadcast(registerInitReceivers);

        resumeInternalEvents = false;
        shouldSendCloseOthersAction = false;

        try {
            package_name = getApplicationContext().getPackageName();
        } catch (Exception e) {
            e.printStackTrace();
            package_name = Utils.getProperty("AppPackageName", this);

        }
        appCMSPresenter.checkPreinstallApp(package_name);

        audio = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        setMenuIcon();
        appCMSPresenter.initializeGetSocial();
        //setLeftNavIcon();

    }

    void handleOrientationForEpisodePlayer(boolean isLandscape) {
        System.out.println("Player Oriantation  handleOrientationForEpisodePlayer ");
        // if (appCMSPresenter.isBlockShowDetail06(updatedAppCMSBinder.getAppCMSPageUI())) {
        if (/*!appCMSPresenter.isFullScreenVisible && */ appCMSPresenter.getTrailerPlayerView() != null && appCMSPresenter.getTrailerPlayerView().isEpisodePlay()
                && appCMSPresenter.getTrailerPlayerView().getPlayer() != null
                && (appCMSPresenter.getTrailerPlayerView().getPlayer().getPlaybackState() == Player.STATE_READY
                || appCMSPresenter.getTrailerPlayerView().getPlayer().getPlaybackState() == Player.STATE_BUFFERING)) {
            appCMSPresenter.getTrailerPlayerView().flipFullScreen(isLandscape);
        } else if (appCMSPresenter.videoPlayerView != null
                && appCMSPresenter.videoPlayerView.getPlayer() != null
//                && (appCMSPresenter.videoPlayerView.getPlayer().isPlaying() || (appCMSPresenter.videoPlayerView.getPlayerSettingsView() != null && appCMSPresenter.videoPlayerView.getPlayerSettingsView().getVisibility() == View.VISIBLE))
                && (appCMSPresenter.videoPlayerView.getPlayer().getPlaybackState() == Player.STATE_READY
                || appCMSPresenter.videoPlayerView.getPlayer().getPlaybackState() == Player.STATE_BUFFERING)) {
            appCMSPresenter.videoPlayerView.flipFullScreen(isLandscape);
        } else if (appCMSPresenter.videoPlayerView != null
                && appCMSPresenter.videoPlayerView.getPlayer() != null
                && appCMSPresenter.isFullScreenVisible) {
            appCMSPresenter.videoPlayerView.flipFullScreen(isLandscape);
        } else {
            appCMSPresenter.restrictPortraitOnly();
        }
        // }
    }

    private Handler handlerClearCache = new Handler();
    int cacheTimeDelay = 180; // 180 second
    public Runnable runnableClearCache = new Runnable() {
        @Override
        public void run() {
            Date current = new Date();
            if ((current.getTime() / 1000 - appPreference.getAppLastLaunchTime() / 1000) >= appCMSPresenter.getAPICacheTime()) {
                if (!appCMSPresenter.isViewPlanPage(updatedAppCMSBinder.getPageId())) {
                    appPreference.setAppLastLaunchTime();
                    appCMSPresenter.clearPageAPIData(() -> {
                                appCMSPresenter.setPageLoading(false);
                            },
                            true);
                }
            }
            handlerClearCache.postDelayed(this, 1000 * cacheTimeDelay);
        }
    };

    private void clearAPICache() {
        if (appPreference.getAppLastLaunchTime() <= 0) {
            appPreference.setAppLastLaunchTime();
        }
        if (appCMSPresenter.getAPICacheTime() < cacheTimeDelay) {
            cacheTimeDelay = (int) appCMSPresenter.getAPICacheTime();
        }
        if (!appCMSPresenter.isNewsTemplate()) {
            handlerClearCache.post(runnableClearCache);
        }
    }

    private void keepScreenOn() {
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }

    private void clearKeepScreenOn() {
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }

    private void initPageActivity() {

        appCMSPresenter.checkForExistingSubscription(false);

        if (updatedAppCMSBinder != null) {
            try {
                appCMSParentView.setBackgroundColor(Color.parseColor(appCMSPresenter.getAppBackgroundColor()));
            } catch (Exception e) {
//                //Log.w(TAG, "Could not set background color of app based upon AppCMS branding - defaulting to primaryDark: " +
//                        e.getMessage());
                appCMSParentView.setBackgroundColor(ContextCompat.getColor(this, R.color.colorPrimaryDark));
            }
        }


        manageTopBar();
        //createTabBar();
        startFreeTrialTool();
        setToolItemsUIColor();

        //Settings The Firebase Analytics for Android
        FirebaseAnalytics mFireBaseAnalytics = FirebaseAnalytics.getInstance(this);
        if (mFireBaseAnalytics != null && appCMSPresenter != null) {
            appCMSPresenter.setmFireBaseAnalytics(mFireBaseAnalytics);
        }


        closeButton.setOnClickListener(v -> {
                    if (appCMSPresenter.getTrailerPlayerView() != null)
                        appCMSPresenter.getTrailerPlayerView().stopPlayer();
                    View view = this.getCurrentFocus();
                    if (view != null) {
                        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        //noinspection ConstantConditions
                        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                    }
                    appCMSPresenter.sendCloseOthersAction(null, true, false);
                }
        );


        //ToDo:  dynamically visible/hide search /profile btn as per API response, currently showing for MSE
        mSearchTopButton.setOnClickListener(v -> {
                    openSearchPage(false, true);
                }
        );


        bellIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cart_badge_count.setVisibility(View.GONE);
                if (appCMSPresenter.getCleverTapInstance() != null)
                    appCMSPresenter.getCleverTapInstance().loadAppInboxView();

            }
        });


        mShareTopButton.setOnClickListener(v -> {
                    openShareLink();
                }
        );

        //ToDo:  dynamically visible/hide search /profile btn as per API response, currently showing for MSE
        mProfileTopButton.setOnClickListener(v -> {
                    if (appCMSLeftDrawerLayout.isDrawerOpen(GravityCompat.START)) {
                        appCMSLeftDrawerLayout.closeDrawers();
                    }
                    if (appCMSPresenter.isUserLoggedIn()) {
                        appCMSPresenter.launchNavigationPage();
                    } else {
                        if (appCMSPresenter != null) {
                            appCMSPresenter.setLaunchType(AppCMSPresenter.LaunchType.LOGIN_AND_SIGNUP);
                            appCMSPresenter.navigateToLoginPage(false);
                            if (appCMSPresenter.isUserLoggedIn())
                                appCMSPresenter.getFirebaseAnalytics().userPropertyLoginStatus(getString(R.string.status_logged_in));
                            else
                                appCMSPresenter.getFirebaseAnalytics().userPropertyLoginStatus(getString(R.string.status_logged_out));
                            appCMSPresenter.getFirebaseAnalytics().screenViewEvent(getString(R.string.value_login_screen));
                        }
                    }
                }
        );

        mLefTNavDrawerButton.setOnClickListener(v -> {
                    if (appCMSPresenter.isHeaderNavExist()) {
                        if (appCMSLeftDrawerLayout.isDrawerOpen(GravityCompat.START)) {
                            appCMSLeftDrawerLayout.closeDrawers();
                        } else {
                            openLeftDrawer();
                        }
                    }

                }
        );
        inflateCastMiniController();
        appCMSPresenter.setMiniPlayerToggleView(showMiniPlayer);
        showMiniPlayer.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                switchColor(isChecked);
                if (isChecked) {
                    refreshPageData();
                    appPreference.setShowPIPVisibility(true);
                    appCMSPresenter.setMiniPLayerVisibility(true);
                } else {
                    appPreference.setShowPIPVisibility(false);
                    appCMSPresenter.setMiniPLayerVisibility(false);
                    if (appCMSPresenter.relativeLayoutPIP != null && appCMSPresenter.pipPlayerVisible) {
                        appCMSPresenter.relativeLayoutPIP.removeminiplayer();
                    }
                }
            }
        });
        if (loadingProgressBar != null) {
            try {
                loadingProgressBar.getIndeterminateDrawable().setTint(Color.parseColor(appCMSPresenter.getAppCMSMain()
                        .getBrand().getCta().getPrimary().getBackgroundColor()));
            } catch (Exception e) {
//                //Log.w(TAG, "Failed to set color for loader: " + e.getMessage());
                loadingProgressBar.getIndeterminateDrawable().setTint(ContextCompat.getColor(this, R.color.colorAccent));
            }
        }

        if (appCMSPresenter != null) {
            try {
                newVersionUpgradeAvailable.setBackgroundColor(Color.parseColor(
                        appCMSPresenter.getAppCtaBackgroundColor()));
                newVersionAvailableTextView.setTextColor(Color.parseColor(
                        appCMSPresenter.getAppCtaTextColor()));
            } catch (Exception e) {
//                //Log.w(TAG, "Failed to set AppCMS branding colors for soft upgrade messages: " +
//                        e.getMessage());
            }
        }

        newVersionAvailableTextView.setOnClickListener((v) -> {
            Intent googlePlayStoreUpgradeAppIntent = new Intent(Intent.ACTION_VIEW,
                    Uri.parse(getString(R.string.google_play_store_upgrade_app_url,
                            getString(R.string.package_name))));
            startActivity(googlePlayStoreUpgradeAppIntent);
        });

        newVersionAvailableCloseButton.setOnClickListener((v) -> {
            ValueAnimator heightAnimator = ValueAnimator.ofInt(newVersionUpgradeAvailable.getHeight(),
                    0);
            heightAnimator.addUpdateListener((animation) -> {
                Integer value = (Integer) animation.getAnimatedValue();
                newVersionUpgradeAvailable.getLayoutParams().height = value;
                if (value == 0) {
                    newVersionUpgradeAvailable.setVisibility(View.GONE);
                }
                newVersionUpgradeAvailable.requestLayout();
            });

            AnimatorSet set = new AnimatorSet();
            set.play(heightAnimator);
            set.setInterpolator(new AccelerateDecelerateInterpolator());
            set.start();
        });
        appCMSPresenter.initializeFacebookSdk();
    }


    private void openSearchPage(boolean navbarPresent, boolean appbarPresent) {
        appCMSPresenter.setNavbarPresent(navbarPresent);
        appCMSPresenter.setAppbarPresent(appbarPresent);
        SearchQuery objSearchQuery = new SearchQuery();
        objSearchQuery.searchInstance(appCMSPresenter);
        ViewCreator.setSearchText("");
        ConstraintViewCreator.setSearchText("");
        objSearchQuery.searchEmptyQuery("", navbarPresent, appbarPresent);
    }

    private boolean shouldReadNavItemsFromAppCMS() {
        return appCMSPresenter.getNavigation() != null &&
                appCMSPresenter.getNavigation().getTabBar() != null &&
                !appCMSPresenter.getNavigation().getTabBar().isEmpty();
    }

    private void openShareLink() {
        AppCMSMain appCMSMain = appCMSPresenter.getAppCMSMain();
        if (appCMSMain != null &&
                updatedAppCMSBinder != null &&
                updatedAppCMSBinder.getAppCMSPageAPI() != null &&
                updatedAppCMSBinder.getAppCMSPageAPI().getModules() != null) {
            int moduleSize = updatedAppCMSBinder.getAppCMSPageAPI().getModules().size();
            for (int i = 0; i < moduleSize; i++) {
                if (updatedAppCMSBinder.getAppCMSPageAPI().getModules().get(i) != null &&
                        updatedAppCMSBinder.getAppCMSPageAPI().getModules().get(i).getContentData() != null &&
                        !updatedAppCMSBinder.getAppCMSPageAPI().getModules().get(i).getContentData().isEmpty() &&
                        updatedAppCMSBinder.getAppCMSPageAPI().getModules().get(i).getContentData().get(0) != null &&
                        updatedAppCMSBinder.getAppCMSPageAPI().getModules().get(i).getContentData().get(0).getGist() != null) {
                    if (appCMSPresenter.isPageAtPersonDetailPage(updatedAppCMSBinder.getPageName()) || (updatedAppCMSBinder.getAppCMSPageAPI().getModules().get(i).getContentData().get(0).getGist().getMediaType() != null && (updatedAppCMSBinder.getAppCMSPageAPI().getModules().get(i).getContentData().get(0).getGist().getMediaType().toLowerCase().contains(getString(R.string.app_cms_article_key_type).toLowerCase()) ||
                            updatedAppCMSBinder.getAppCMSPageAPI().getModules().get(i).getContentData().get(0).getGist().getMediaType().toLowerCase().contains(getString(R.string.app_cms_photo_gallery_key_type).toLowerCase()))) ||
                            updatedAppCMSBinder.getAppCMSPageAPI().getModules().get(i).getModuleType() != null && updatedAppCMSBinder.getAppCMSPageAPI().getModules().get(i).getModuleType().toLowerCase().contains("VideoDetailModule".toLowerCase())) {
                        getShareLink(i);
                    }
                }
            }
        }

    }

    private void getShareLink(int position) {
        if (updatedAppCMSBinder.getAppCMSPageAPI().getModules().get(position).getContentData().get(0).getGist().getPermalink() != null &&
                updatedAppCMSBinder.getAppCMSPageAPI().getModules().get(position).getContentData().get(0).getGist().getTitle() != null) {
            StringBuilder filmUrl = new StringBuilder();
            filmUrl.append(appCMSPresenter.getAppCMSMain().getDomainName());
            filmUrl.append(updatedAppCMSBinder.getAppCMSPageAPI().getModules().get(position).getContentData().get(0).getGist().getPermalink());
            String[] extraData = new String[1];
            extraData[0] = filmUrl.toString();
            appCMSPresenter.launchButtonSelectedAction(updatedAppCMSBinder.getAppCMSPageAPI().getModules().get(position).getContentData().get(0).getGist().getPermalink(),
                    getString(R.string.app_cms_action_share_key),
                    updatedAppCMSBinder.getAppCMSPageAPI().getModules().get(position).getContentData().get(0).getGist().getTitle(),
                    extraData,
                    updatedAppCMSBinder.getAppCMSPageAPI().getModules().get(position).getContentData().get(0),
                    false,
                    0,
                    null);
        } else if (updatedAppCMSBinder.getAppCMSPageAPI().getModules().get(position).getContentData().get(0).getGist().getPermalink() != null && appCMSPresenter.isPageAtPersonDetailPage(updatedAppCMSBinder.getPageName())) {
            StringBuilder filmUrl = new StringBuilder();
            filmUrl.append(appCMSPresenter.getAppCMSMain().getDomainName());
            filmUrl.append(updatedAppCMSBinder.getAppCMSPageAPI().getModules().get(position).getContentData().get(0).getGist().getPermalink());
            String[] extraData = new String[1];
            extraData[0] = filmUrl.toString();
            appCMSPresenter.launchButtonSelectedAction(updatedAppCMSBinder.getAppCMSPageAPI().getModules().get(position).getContentData().get(0).getGist().getPermalink(),
                    getString(R.string.app_cms_action_share_key),
                    "Player detail",
                    extraData,
                    updatedAppCMSBinder.getAppCMSPageAPI().getModules().get(position).getContentData().get(0),
                    false,
                    0,
                    null);
        }
    }

    private boolean shouldShowSearchInToolbar() {
        if (appCMSPresenter.getNavigation() != null &&
                appCMSPresenter.getNavigation().getRight() != null &&
                !appCMSPresenter.getNavigation().getRight().isEmpty()) {
            List<NavigationPrimary> rightNav = appCMSPresenter.getNavigation().getRight();
            int numNavItems = rightNav.size();
            for (int i = 0; i < numNavItems; i++) {
                NavigationPrimary navItem = rightNav.get(i);
                if (navItem != null &&
                        navItem.getTitle() != null &&
                        appCMSPresenter.getPageFunctionValue(navItem.getPageId(), navItem.getTitle()).equals(getString(R.string.app_cms_search_label))) {
                    return true;
                }
            }
        }
        return false;
    }

    // Construct a request for phone numbers and show the picker
    private void requestHint() {
        HintRequest hintRequest = new HintRequest.Builder()
                .setPhoneNumberIdentifierSupported(true)
                .build();


        GoogleSignInOptions googleSignInOptions = new GoogleSignInOptions
                .Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .requestIdToken(getString(R.string.server_client_id))
                .build();
        appCMSPresenter.getGoogleApiClient(googleSignInOptions);

        PendingIntent intent = Auth.CredentialsApi.getHintPickerIntent(
                appCMSPresenter.googleApiClient, hintRequest);
        try {

            startIntentSenderForResult(intent.getIntentSender(),
                    2, null, 0, 0, 0);
        } catch (ActivityNotFoundException | IntentSender.SendIntentException e) {
            e.printStackTrace();
        }
    }

    public void setToolItemsUIColor() {
        int fillColor = appCMSPresenter.getGeneralTextColor();

        if (appCMSPresenter.isNewsTemplate())
            mMediaRouteButton.setColorFilter(ContextCompat.getColor(this, android.R.color.white), PorterDuff.Mode.SRC_IN);
        else
            mMediaRouteButton.setColorFilter(fillColor, PorterDuff.Mode.SRC_IN);

        mProfileTopButton.getDrawable().setColorFilter(new PorterDuffColorFilter(fillColor, PorterDuff.Mode.MULTIPLY));
        mProfileTopButton.setBackgroundColor(ContextCompat.getColor(this, android.R.color.transparent));

        mLefTNavDrawerButton.getDrawable().setColorFilter(new PorterDuffColorFilter(fillColor, PorterDuff.Mode.MULTIPLY));
        mLefTNavDrawerButton.setBackgroundColor(ContextCompat.getColor(this, android.R.color.transparent));

        mSearchTopButton.getDrawable().setColorFilter(new PorterDuffColorFilter(fillColor, PorterDuff.Mode.MULTIPLY));
        mSearchTopButton.setBackgroundColor(ContextCompat.getColor(this, android.R.color.transparent));

        mShareTopButton.getDrawable().setColorFilter(new PorterDuffColorFilter(fillColor, PorterDuff.Mode.SRC_IN));
        mShareTopButton.setBackgroundColor(ContextCompat.getColor(this, android.R.color.transparent));

        closeButton.getDrawable().setColorFilter(new PorterDuffColorFilter(fillColor, PorterDuff.Mode.MULTIPLY));
        closeButton.setBackgroundColor(ContextCompat.getColor(this, android.R.color.transparent));

    }

    private void setMenuIcon() {
        mProfileTopButton.setImageResource(R.drawable.profile);
    }

    private void setLeftNavIcon() {
        if (leftNavId != null) {
            if (appCMSPresenter.isHeaderNavExist()) {
                mLefTNavDrawerButton.setVisibility(View.VISIBLE);
                mLefTNavDrawerButton.setImageResource(R.drawable.menu_);
                bellIcon.setVisibility(View.GONE);
            }
        } else {
            mLefTNavDrawerButton.setVisibility(View.GONE);
            bellIcon.setVisibility(View.VISIBLE);
        }

    }

    private void inflateCastMiniController() {
        if (GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(this) ==
                ConnectionResult.SUCCESS && appCMSPresenter.isNetworkConnected()) {
            try {
                if (appCMSCastController.getChildCount() <= 0)
                    LayoutInflater.from(this).inflate(R.layout.fragment_castminicontroller, appCMSCastController);
                if (appCMSPresenter.isNetworkConnected()) {
                    castDisabled = false;
                }
            } catch (Exception e) {
                castDisabled = true;
            }
        } else {
            castDisabled = true;
        }
    }

    @Override
    public void onBackPressed() {
        try {
            if (appCMSPresenter.getWaysToWatchBottom() != null && appCMSPresenter.getWaysToWatchBottom().isVisible()) {
                appCMSPresenter.getWaysToWatchBottom().removeSheet();
                return;
            }

            if (appCMSPresenter != null && appCMSPresenter.popupWindow != null
                    && appCMSPresenter.popupWindow.isShowing()) {
                appCMSPresenter.popupWindow.dismiss();
                appCMSPresenter.popupWindow = null;
                return;
            }
            if (CustomWebView.mWebFbPlayerView != null && CustomWebView.mWebFbPlayerView.getVisibility() == View.VISIBLE && CustomWebView.mWebChromeClient != null && CustomWebView.isWebVideoFullView) {
                CustomWebView.mWebChromeClient.onHideCustomView();
                return;
            }
            if (appCMSPresenter.isFullScreenVisible) {
                if (appCMSPresenter.getCurrentActivity().findViewById(R.id.video_player_id) != null)
                    appCMSPresenter.exitFullScreenPlayer();
                else
                    appCMSPresenter.exitFullScreenEpisodePlayer();
                return;
            }
            if (!appCMSBinderStack.isEmpty() &&
                    appCMSBinderMap.get(appCMSBinderStack.peek()) != null &&
                    appCMSBinderMap.get(appCMSBinderStack.peek()).getPageId() != null) {
                if (appCMSPresenter.isShowPage(appCMSBinderMap.get(appCMSBinderStack.peek()).getPageId()) && appCMSPresenter.getVideoPlayerViewCache(appCMSPresenter.getShowDeatil06TrailerPlayerKey()) != null) {
                    appCMSPresenter.getVideoPlayerViewCache(appCMSPresenter.getShowDeatil06TrailerPlayerKey()).stopPlayer();
                    appCMSPresenter.setDefaultTrailerPlay(false);
                    appCMSPresenter.setShowDeatil06TrailerPlayerKey(null);
                } else if (appCMSPresenter.isPageLoginPage(appCMSBinderMap.get(appCMSBinderStack.peek()).getPageId()) || appCMSPresenter.isViewPlanPage(appCMSBinderMap.get(appCMSBinderStack.peek()).getPageId())) {
                    appCMSPresenter.setSelectedPlan(null, null);
                }else if(appCMSPresenter.isJusPayPaymentPage(appCMSBinderMap.get(appCMSBinderStack.peek()).getPageId())){
                    if(appCMSPresenter.getLaunchType() == AppCMSPresenter.LaunchType.SUBSCRIBE){
                        new Handler(Looper.getMainLooper()).postDelayed(() -> { appCMSPresenter.showPersonalizationscreenIfEnabled(false, true); },1000L);
                    }
                }
            }
            if (updatedAppCMSBinder.getAppCMSPageUI() != null && updatedAppCMSBinder.getAppCMSPageUI().getModuleList() != null && appCMSPresenter.getRelatedModuleForBlock(updatedAppCMSBinder.getAppCMSPageUI().getModuleList(), this.getString(R.string.app_cms_navigation_page_name)) == null) {
                appCMSPresenter.getCurrentActivity().findViewById(R.id.search_layout).setVisibility(View.GONE);
            }
//            appCMSPresenter.setEntitlementPendingVideoData(null);
            appCMSPresenter.setAudioPlayerOpen(false);
            if (!handlingClose && !isPageLoading()) {
                if (appCMSPresenter.isAddOnFragmentVisible()) {
                    for (Fragment fragment : getSupportFragmentManager().getFragments()) {
                        if (fragment instanceof AppCMSMoreFragment) {
                            ((AppCMSMoreFragment) fragment).sendDismissAction();
                        }
                        if (fragment instanceof AppCMSNoPurchaseFragment) {
                            ((AppCMSNoPurchaseFragment) fragment).sendDismissAction();
                        }
                        if (fragment instanceof AppCMSRedemptionSuccessDialog) {
                            ((AppCMSRedemptionSuccessDialog) fragment).sendDismissAction();
                        }
                    }
                    return;
                }

                handlingClose = true;
                handleCloseAction(false);
                handlingClose = false;
            } else if (isPageLoading()) {
                pageLoading(false);
                appCMSPresenter.setIsLoading(false);
                appCMSPresenter.setNavItemToCurrentAction(this);
            }

            appCMSPresenter.cancelCustomToast();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (resumeInternalEvents) {
            appCMSPresenter.restartInternalEvents();
        }
        if (appCMSPresenter.getTemplateType() == AppCMSPresenter.TemplateType.NEWS) {
            try {
                if (appCMSPresenter.getTrailerPlayerView() != null) {
                    appCMSPresenter.getTrailerPlayerView().pausePlayer();
                    appCMSPresenter.getTrailerPlayerView().onDestroyNotification();

                    ImageView thumbnailImage = findViewById(R.id.trailerthumbnailImage);
                    new Handler(getMainLooper()).postDelayed(() -> {
                        if (thumbnailImage != null && appCMSPresenter.getTrailerPlayerView() != null) {
                            appCMSPresenter.getTrailerPlayerView().setVisibility(VISIBLE);
                            thumbnailImage.setVisibility(View.GONE);
                            appCMSPresenter.getTrailerPlayerView().resumePlayer();
                        }
                    }, 100);


                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        appCMSPresenter.setCancelAllLoads(false);
        if (AudioServiceHelper.getAudioInstance() != null) {
            AudioServiceHelper.getAudioInstance().onStart();
            AudioServiceHelper.getAudioInstance().createAudioPlaylistInstance(appCMSPresenter, this);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (appCMSPresenter.getPlatformType() != AppCMSPresenter.PlatformType.TV &&
                updatedAppCMSBinder != null &&
                updatedAppCMSBinder.getScreenName() != null &&
                updatedAppCMSBinder.getPageId() != null &&
                updatedAppCMSBinder.getScreenName().equalsIgnoreCase(getString(R.string.app_cms_pagename_homescreen_key))) {
            if (CommonUtils.isFromBackground) {
                appCMSPresenter.clearSinglePageAPIData(() -> {
                            appCMSPresenter.setPageLoading(false);
                        },
                        true, updatedAppCMSBinder.getPageId());
            }
        }
        if (!AppCMSPresenter.PRE_LANGUAGE.equalsIgnoreCase(appCMSPresenter.getLanguage().getLanguageCode())) {
            isTabCreated = false;
            createTabBar();
        }
        registerReceiver(networkConnectedReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));

        NetworkInfo activeNetwork = null;
        if (connectivityManager != null) {
            activeNetwork = connectivityManager.getActiveNetworkInfo();
        }
        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();
        appCMSPresenter.setNetworkConnected(isConnected, null);


        if (appCMSPresenter.getAppCMSMain() != null && CommonUtils.isEmpty(CommonUtils.Play_Store_Country_Code)) {
            appCMSPresenter.getPlanData();
        }

        if (appPreference.getNetworkConnectedState() && !isConnected) {
            appCMSPresenter.setShowNetworkConnectivity(true);
            appCMSPresenter.showNoNetworkConnectivityToast();
        } else {
            appCMSPresenter.setShowNetworkConnectivity(false);
            appCMSPresenter.cancelAlertDialog();
        }
        if (activeNetwork != null) {
            appPreference.setActiveNetworkType(activeNetwork.getType());
        }
        if (!libsThreadExecuted) {
            new Thread(() -> {
                Intent initReceivers = new Intent("INITIALIZATION");
                initReceivers.putExtra("init_action", "init");
                sendBroadcast(initReceivers);
                //Fabric.with(getApplication(), new Crashlytics());
                FirebaseCrashlytics.getInstance().setCrashlyticsCollectionEnabled(true);
                // Obtain the FirebaseAnalytics instance.
                FirebaseAnalytics.getInstance(this);
                AndroidThreeTen.init(this);
                //AppsFlyerLib.getInstance().startTracking(getApplication());
                if (!FacebookSdk.isInitialized()) {
                    FacebookSdk.setApplicationId(Utils.getProperty("FacebookAppId", AppCMSPageActivity.this));
                    FacebookSdk.sdkInitialize(getApplicationContext());
                }
                AppEventsLogger.activateApp(getApplication());
                callbackManager = CallbackManager.Factory.create();
                LoginManager.getInstance().registerCallback(callbackManager,
                        new FacebookCallback<LoginResult>() {
                            @Override
                            public void onSuccess(LoginResult loginResult) {
                                AppCMSPageActivity.this.accessToken = loginResult.getAccessToken();
                                if (appCMSPresenter != null && AppCMSPageActivity.this.accessToken != null) {
                                    GraphRequest request = GraphRequest.newMeRequest(
                                            AppCMSPageActivity.this.accessToken,
                                            (user, response) -> {
                                                String username = null;
                                                String email = null;
                                                try {
                                                    username = user.getString("name");
                                                    email = user.getString("email");
                                                } catch (JSONException | NullPointerException e) {
                                                    //Log.e(TAG, "Error parsing Facebook Graph JSON: " + e.getMessage());
                                                }

                                                if (appCMSPresenter.getLaunchType() == AppCMSPresenter.LaunchType.SUBSCRIBE) {
                                                    handleCloseAction(false);
                                                }
                                                if (appCMSPresenter.getLaunchType() == AppCMSPresenter.LaunchType.VIEWING_RESTRICTIONS_VIEW || appCMSPresenter.getLaunchType() == AppCMSPresenter.LaunchType.VIDEO_PIN_VIEW) {
                                                    if (appPreference != null && appPreference.getFacebookUserId() != null
                                                            && appPreference.getFacebookUserId().equalsIgnoreCase(AppCMSPageActivity.this.accessToken.getUserId())) {
                                                        handleCloseAction(false);
                                                        if (appCMSPresenter.getLaunchType() == AppCMSPresenter.LaunchType.VIEWING_RESTRICTIONS_VIEW) {
                                                            if (appPreference != null && !TextUtils.isEmpty(appPreference.getParentalPin())) {
                                                                appCMSPresenter.navigateToViewingRestrictionsPage();
                                                            } else {
                                                                appCMSPresenter.setLaunchType(AppCMSPresenter.LaunchType.VIEWING_RESTRICTIONS_FROM_VIDEO_PIN_VIEW);
                                                                appCMSPresenter.navigateToChangeVideoPinPage();
                                                            }
                                                        } else {
                                                            appCMSPresenter.navigateToChangeVideoPinPage();
                                                        }
                                                    } else {
                                                        loaderWaitingFor3rdPartyLogin = false;
                                                        pageLoading(false);
                                                        new Handler().postDelayed(() -> {
                                                            String errorTxt = getString(R.string.facebook_error);
                                                            if (appCMSPresenter.getModuleApi() != null && appCMSPresenter.getModuleApi().getMetadataMap() != null && appCMSPresenter.getModuleApi().getMetadataMap().getFacebookError() != null) {
                                                                errorTxt = appCMSPresenter.getModuleApi().getMetadataMap().getFacebookError();
                                                            }
                                                            appCMSPresenter.showDialog(AppCMSPresenter.DialogType.SIGNIN, errorTxt, false, null, null, localisedStrings.getSignInText());
                                                        }, 50);
                                                    }
                                                    return;
                                                }
                                                appCMSPresenter.setFacebookAccessToken(
                                                        AppCMSPageActivity.this.accessToken.getToken(),
                                                        AppCMSPageActivity.this.accessToken.getUserId(),
                                                        username,
                                                        email,
                                                        false,
                                                        true);
                                            });
                                    Bundle parameters = new Bundle();
                                    parameters.putString("fields", "id,name,email");
                                    request.setParameters(parameters);
                                    request.executeAsync();
                                }
                            }

                            @Override
                            public void onCancel() {
                                // App code
//                        Log.e(TAG, "Facebook login was cancelled");
                                loaderWaitingFor3rdPartyLogin = false;
                                pageLoading(false);
                            }

                            @Override
                            public void onError(FacebookException exception) {
                                // App code
//                        Log.e(TAG, "Facebook login exception: " + exception.getMessage());
                                loaderWaitingFor3rdPartyLogin = false;
                                pageLoading(false);
                            }
                        });

                accessToken = AccessToken.getCurrentAccessToken();


                if (appCMSPresenter != null) {
                    appCMSPresenter.initializeAppCMSAnalytics();
                    FirebaseAnalytics mFireBaseAnalytics = FirebaseAnalytics.getInstance(this);
                    if (mFireBaseAnalytics != null && appCMSPresenter != null) {
                        appCMSPresenter.setmFireBaseAnalytics(mFireBaseAnalytics);
                    }
                }

                inflateCastMiniController();

                appCMSPresenter.setBitmapCachePresenter(
                        new BitmapCachePresenter(this, getSupportFragmentManager()));

                Intent sendChannelIdIntent = new Intent("INITIALIZATION");
                sendChannelIdIntent.putExtra("init_action", "send_channel_id");
                sendBroadcast(sendChannelIdIntent);

                Intent sendAppKey = new Intent("INITIALIZATION");
                sendAppKey.putExtra("init_action", "send_app_key");
                sendBroadcast(sendAppKey);
            }).run();
            libsThreadExecuted = true;
        }

        appCMSPresenter.setAppOrientation();

        if (this.findViewById(R.id.video_player_id) != null &&
                appCMSPresenter.isAutoRotate()) {
            appCMSPresenter.unrestrictPortraitOnly();
        } else
            appCMSPresenter.setAppOrientation();
        resume();
        try {

            registerReceiver(phoneHintReceiver,
                    new IntentFilter(AppCMSPresenter.PRESENTER_PHONE_HINT));

            registerReceiver(eventReceiver, new IntentFilter("LAUNCH_EVENT"));
            registerReceiver(eventReceiver, new IntentFilter(AppCMSPresenter.ACTION_LAUNCH_JUSPAY));
            registerReceiver(eventReceiver, new IntentFilter(AppCMSPresenter.ACTION_PRE_FATCH_JUSPAY_ASSETS));
            registerReceiver(presenterCloseActionReceiver,
                    new IntentFilter(AppCMSPresenter.PRESENTER_CLOSE_SCREEN_ACTION));
            registerReceiver(enterFullScreenReceiver,
                    new IntentFilter(AppCMSPresenter.PRESENTER_ENTER_FULLSCREEN_ACTION));
            registerReceiver(exitFullScreenReceiver,
                    new IntentFilter(AppCMSPresenter.PRESENTER_EXIT_FULLSCREEN_ACTION));
            registerReceiver(keepScreenOnReceiver,
                    new IntentFilter(AppCMSPresenter.PRESENTER_KEEP_SCREEN_ON_ACTION));
            registerReceiver(clearKeepScreenOnReceiver,
                    new IntentFilter(AppCMSPresenter.PRESENTER_CLEAR_KEEP_SCREEN_ON_ACTION));
            registerReceiver(chromecastDisconnectedReceiver,
                    new IntentFilter(AppCMSPresenter.PRESENTER_CHROMECAST_DISCONNECTED_ACTION));
        } catch (Exception e) {
        }

        appCMSPresenter.setCancelAllLoads(false);

        appCMSPresenter.setCurrentActivity(this);
        appPreference.setAppHomeActivityCreated(true);

//        Log.d(TAG, "onResume()");
        //Log.d(TAG, "checkForExistingSubscription()");

        if (BuildConfig.FLAVOR.contains(AppCMSPresenter.MOBILE_BUILD_VARIENT) && mAppUpdateHelper == null) {
            mAppUpdateHelper = new AppUpdateHelper(appCMSPresenter);
        }

        if (updatedAppCMSBinder != null && updatedAppCMSBinder.getExtraScreenType() != null &&
                updatedAppCMSBinder.getExtraScreenType() != AppCMSPresenter.ExtraScreenType.BLANK) {
            appCMSPresenter.refreshPages(shouldRefresh -> {
                if (mAppUpdateHelper != null && (appCMSPresenter.isAppBelowMinVersion() || appCMSPresenter.isAppUpgradeAvailable())) {
                    mAppUpdateHelper.checkAppUpgradeAvailable();
                }

            }, true, 0, 0);
        }

        try {
            if (appCMSBinderMap != null && !appCMSBinderMap.isEmpty() && appCMSBinderStack != null && !appCMSBinderStack.isEmpty()) {
                AppCMSBinder appCMSBinder = appCMSBinderMap.get(appCMSBinderStack.peek());
                isDownloadPageOpen = appCMSBinder != null && appCMSBinder.getPageId().equalsIgnoreCase(appPreference.getDownloadPageId());
            }

            if (appCMSPresenter.isDownloadPage(updatedAppCMSBinder.getPageId()) &&
                    !appCMSPresenter.isNetworkConnected() &&
                    appCMSPresenter.shouldShowNetworkContectivity()) {
                appCMSPresenter.showNoNetworkConnectivityToast();
                appCMSPresenter.setShowNetworkConnectivity(false);
            }
        } catch (Exception e) {
            //
        }

        if (pendingDeeplinkUri != null) {
            processDeepLink(pendingDeeplinkUri);
            pendingDeeplinkUri = null;
        }

        if (isConnected) {
            appCMSPresenter.syncSubscription();
        }

        Ratings ratings = null;
        // For testing purpose
        //long minutesSinceLastRating = (new Date().getTime() - appPreference.getRatingDialogShownTime()) / (1000L * 60L);
        if (!isRatingDialogShown && (!appPreference.getRatingDialogShownForVideoCount() || !appPreference.getRatingDialogShownForAppOpen()) && (appPreference.getRatingDialogShownTime() == 0 || (appCMSPresenter.getDifferenceDays(new Date(appPreference.getRatingDialogShownTime()), new Date()) >= 7))) {
            if (appCMSPresenter.getAppCMSMain() != null && appCMSPresenter.getAppCMSMain().getFeatures() != null && appCMSPresenter.getAppCMSMain().getFeatures().getRatings() != null)
                ratings = appCMSPresenter.getAppCMSMain().getFeatures().getRatings();
            int videoWatchCount = ratings == null ? Ratings.DEFAULT_VIDEO_WATCH_COUNT : ratings.getVideoCountInt();
            int appOpenCount = ratings == null ? Ratings.DEFAULT_APP_VISIT_COUNT : ratings.getAppVisitInt();
            if (ratings != null && ratings.isRatingEnabled()
                    && ((appPreference.getVideoWatchCount() >= videoWatchCount && !appPreference.getRatingDialogShownForVideoCount())
                    || (appPreference.getAppOpenCount() >= appOpenCount && !appPreference.getRatingDialogShownForAppOpen()))) {
                boolean isForAppOpen = appPreference.getAppOpenCount() >= appOpenCount && !appPreference.getRatingDialogShownForAppOpen();
                isRatingDialogShown = true;
                appPreference.setRatingDialogShownTime(System.currentTimeMillis());
                new Handler().postDelayed(() -> {
                    askUserForRating(isForAppOpen);
                }, 2000);
            }
        }

        if (needToCheckReferralData && GetSocialHelper.needToProcessReferralData) {
            navigateToReferralPage();
            GetSocialHelper.needToProcessReferralData = false;
            needToCheckReferralData = false;
        }

    }

    void askUserForRating(boolean isForAppOpen) {
        if (!BuildConfig.DEBUG) {
            appCMSPresenter.showDialog(AppCMSPresenter.DialogType.RATING_PROMPT, "", true, () -> {
                if (isForAppOpen) {
                    appPreference.setRatingDialogShownForAppOpen();
                } else {
                    appPreference.setRatingDialogShownForVideoCount();
                }
                isRatingDialogShown = false;
                Intent googlePlayRatingIntent = new Intent(Intent.ACTION_VIEW,
                        Uri.parse(getString(R.string.google_play_store_upgrade_app_url,
                                getString(R.string.package_name))));
                startActivity(googlePlayRatingIntent);
            }, () -> {
                isRatingDialogShown = false;
                appCMSPresenter.showDialog(AppCMSPresenter.DialogType.CONTACT_US, "", true, () -> {
                    if (appCMSPresenter.isFreshChatEnable()) {
                        appCMSPresenter.launchFreshChat(appCMSPresenter.getAppCMSMain().getCustomerService().getFreshChat().getAppID(),
                                appCMSPresenter.getAppCMSMain().getCustomerService().getFreshChat().getKey(), false);
                    } else {
                        appCMSPresenter.navigateToContactUsPage();
                    }
                }, () -> {
                }, "");
            }, "");
        }

    }

    private void refreshPageData() {
        boolean cancelLoadingOnFinish = false;
        if (!appCMSPresenter.isPageLoading()) {
            pageLoading(true);
            cancelLoadingOnFinish = true;
        }
        if (appCMSBinderMap != null &&
                appCMSBinderStack != null &&
                !appCMSBinderStack.isEmpty()) {
            AppCMSBinder appCMSBinder = appCMSBinderMap.get(appCMSBinderStack.peek());
            if (appCMSBinder != null) {
                AppCMSPageUI appCMSPageUI = appCMSPresenter.getAppCMSPageUI(appCMSBinder.getScreenName());
                if (appCMSPageUI != null) {
                    appCMSBinder.setAppCMSPageUI(appCMSPageUI);
                } else if (cancelLoadingOnFinish) {
                    pageLoading(false);
                } else if (appCMSPageUI == null && appCMSPresenter.isPageLoading())
                    pageLoading(false);
                if (!appCMSPresenter.isCategoryPage(appCMSBinder.getPageId()) &&
                        !appCMSPresenter.isFullScreenVisible &&
                        appCMSPresenter.getCurrentActivity() instanceof AppCMSPageActivity &&
                        (updatedAppCMSBinder != null && updatedAppCMSBinder.getAppCMSPageUI() != null &&
                                updatedAppCMSBinder.getAppCMSPageUI().getModuleList() != null &&
                                updatedAppCMSBinder.getAppCMSPageUI().getModuleList().size() > 0 &&
                                appCMSPresenter.getRelatedModuleForBlock(updatedAppCMSBinder.getAppCMSPageUI().getModuleList(), this.getString(R.string.ui_block_showDetail_06)) == null) &&
                        !appCMSPresenter.isPageSearch(updatedAppCMSBinder.getScreenName())) {
                    updateData(appCMSBinder, () -> {
                        appCMSPresenter.sendRefreshPageAction();
                        showEntitlementOptions(appCMSBinder);
                    });
                } else if (appCMSPresenter.isPageLoading())
                    pageLoading(false);
            } else if (cancelLoadingOnFinish) {
                pageLoading(false);
            }
        } else if (cancelLoadingOnFinish) {
            pageLoading(false);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        pageLoading(false);

        appCMSPresenter.cancelInternalEvents();

        isActive = false;
        //appCMSPresenter.setEpisodeId(null);
        appCMSPresenter.closeSoftKeyboard();
        appCMSPresenter.cancelCustomToast();

        if (appCMSPresenter.isFullScreenVisible) {
            appCMSPresenter.exitFullScreenPlayer();
        }
        if (appCMSPresenter.videoPlayerView != null && updatedAppCMSBinder.isShowDetailsPage()&&appCMSPresenter.isNewsTemplate()) {

        }
        else {
        if (appCMSPresenter.videoPlayerView != null) {
            appCMSPresenter.videoPlayerView.pausePlayer();
        }
        if (appCMSPresenter.getTrailerPlayerView() != null) {
            appCMSPresenter.getTrailerPlayerView().pausePlayer();
        }
        }
        if (appCMSPresenter.setLaunchingVideoPlayerView() != null) {
            appCMSPresenter.setLaunchingVideoPlayerView().release();
//            appCMSPresenter.getPlayerView().stop();
//            appCMSPresenter.getPlayerView().setPlayWhenReady(false);
        }
        try {
            unregisterReceiver(presenterCloseActionReceiver);
            unregisterReceiver(enterFullScreenReceiver);
            unregisterReceiver(exitFullScreenReceiver);
            unregisterReceiver(keepScreenOnReceiver);
            unregisterReceiver(clearKeepScreenOnReceiver);
            unregisterReceiver(chromecastDisconnectedReceiver);
            unregisterReceiver(networkConnectedReceiver);
            unregisterReceiver(eventReceiver);
            unregisterReceiver(phoneHintReceiver);
        } catch (Exception e) {
            e.printStackTrace();
        }
        ViewCreator.stopCountdownTimer();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if (intent.getExtras() != null) {
            Bundle fcmDeepLinkBundle = intent.getExtras();
            for (String key : fcmDeepLinkBundle.keySet()) {
                if (key.equalsIgnoreCase("deeplink")) {
                    Object value = getIntent().getExtras().get(key);
                    Log.d(TAG, "Key: onNewIntent " + key + " Value: " + value);

                }
            }
        }
        try {
            if (appCMSPresenter.videoPlayerView != null) {
                appCMSPresenter.videoPlayerView.setPauseState();
            }
            if (Intent.ACTION_VIEW.equals(intent.getAction()) ||
                    Intent.ACTION_SEARCH.equals(intent.getAction())) {

                String queryTerm = intent.getStringExtra(SearchManager.QUERY);
                if (queryTerm.trim().length() == 0) {
                    appCMSPresenter.showEmptySearchToast();
                    return;
                }
                SearchQuery objSearchQuery = new SearchQuery();
                objSearchQuery.searchInstance(appCMSPresenter);
                objSearchQuery.searchQuery(queryTerm);
                appCMSPresenter.sendSearchEvent(queryTerm);

                return;
            }
            Bundle args = intent.getBundleExtra(getString(R.string.app_cms_bundle_key));
            if (args != null) {
                updatedAppCMSBinder =
                        (AppCMSBinder) args.getBinder(getString(R.string.app_cms_binder_key));
                if (updatedAppCMSBinder != null) {
                    mergeInputData(updatedAppCMSBinder, updatedAppCMSBinder.getPageId());
                }
                if (isActive) {
                    handleLaunchPageAction(updatedAppCMSBinder,
                            false,
                            false,
                            false);
                }
            }

            String deeplinkUri = intent.getStringExtra(getString(R.string.deeplink_uri_extra_key));
            if (!TextUtils.isEmpty(deeplinkUri)) {
                pendingDeeplinkUri = Uri.parse(deeplinkUri);

            }
            if (intent.getBooleanExtra(AppCMSPresenter.EXTRA_OPEN_AUDIO_PLAYER, false)) {

                if (appCMSPresenter != null && !appPreference.getAppHomeActivityCreated() && !appCMSPresenter.isAudioActvityVisible()) {
                    Class launchActivity = Class.forName(mobileLaunchActivity);
                    startActivity(new Intent(this, launchActivity));
                    finish();
                } else {
                    if (checkPlayServices() && Utils.isNetworkAvailable(this)) {
                        Intent fullScreenIntent = new Intent(this, AppCMSPlayAudioActivity.class)
                                .setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP |
                                        Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        if (intent.getParcelableExtra(
                                PlaybackControlsFragment.EXTRA_CURRENT_MEDIA_DESCRIPTION) != null) {
                            MediaMetadataCompat description = intent.getParcelableExtra(
                                    PlaybackControlsFragment.EXTRA_CURRENT_MEDIA_DESCRIPTION);
                            fullScreenIntent.putExtra(AppCMSPresenter.EXTRA_CURRENT_MEDIA_DESCRIPTION, description);

                        }
                        startActivity(fullScreenIntent);
                    }
                }
                appPreference.setAppHomeActivityCreated(true);

            }

        } catch (Exception e) {
            e.printStackTrace();
            //
        }


        try {
            if (appCMSPresenter != null && appCMSPresenter.isCleverTapAvailable && appCMSPresenter.getCleverTapInstance() != null) {
                appCMSPresenter.getCleverTapInstance().pushNotificationClickedEvent(intent.getExtras());
            }
        } catch (Throwable t) {
            t.printStackTrace();
        }

        needToCheckReferralData = true;
    }

    @Override
    protected void onStop() {
        super.onStop();
        appCMSPresenter.dismissPopupWindowPlayer(true);
        appCMSPresenter.setDefaultTrailerPlay(false);
        appCMSPresenter.setEpisodeTrailerID(null);
        appCMSPresenter.setEpisodePromoID(null);
//        if (appCMSPresenter.getTrailerPlayerView() != null &&appCMSPresenter.getTemplateType() != AppCMSPresenter.TemplateType.NEWS)
//            appCMSPresenter.getTrailerPlayerView().stopPlayer();
        appCMSPresenter.cancelInternalEvents();

        appCMSPresenter.setShowNetworkConnectivity(true);

        if (!appCMSBinderStack.isEmpty() &&
                isPageLoading() &&
                appCMSPresenter.isPageLoginPage(appCMSBinderStack.peek()) &&
                appCMSPresenter.isUserLoggedIn()) {
            handleCloseAction(true);
        }
        if (AudioServiceHelper.getAudioInstance() != null) {
            AudioServiceHelper.getAudioInstance().onStop();
        }
        if (appCMSPresenter != null && updatedAppCMSBinder != null && updatedAppCMSBinder.getAppCMSPageUI() != null && appCMSPresenter.getCurrentActivity() != null &&
                updatedAppCMSBinder.getAppCMSPageUI().getModuleList() != null && updatedAppCMSBinder.getAppCMSPageUI().getModuleList().size() > 0 &&
                (appCMSPresenter.getModuleListByName(updatedAppCMSBinder.getAppCMSPageUI().getModuleList(), appCMSPresenter.getCurrentActivity().getString(R.string.app_cms_page_module_key_showdetail_06)) != null)) {
            if (appCMSPresenter.getTrailerPlayerView() != null && appCMSPresenter.getTemplateType() == AppCMSPresenter.TemplateType.NEWS && !appCMSPresenter.getTrailerPlayerView().isVideoPaused()) {
                appCMSPresenter.getTrailerPlayerView().resumePlayer();
            }
        } else if (appCMSPresenter.videoPlayerView != null && appCMSPresenter.getTemplateType() == AppCMSPresenter.TemplateType.NEWS) {
            appCMSPresenter.videoPlayerView.createNotification();
            if (appCMSPresenter.getIsMiniPlayerPlaying() && !appCMSPresenter.videoPlayerView.isVideoPaused())
                appCMSPresenter.videoPlayerView.resumePlayer();
            else {
                appCMSPresenter.videoPlayerView.pausePlayer();
            }
        }
    }

    @Override
    protected void onDestroy() {
        try {
            if (appCMSPresenter.videoPlayerView != null && appCMSPresenter.getTemplateType() == AppCMSPresenter.TemplateType.NEWS) {
                appCMSPresenter.videoPlayerView.onDestroyNotification();
            }
            if (appCMSPresenter.getTrailerPlayerView() != null) {
                appCMSPresenter.getTrailerPlayerView().onDestroyNotification();
            }
        } catch (Exception e) {

        }
        if (handlerClearCache != null) {
            handlerClearCache.removeCallbacks(runnableClearCache);
        }
        super.onDestroy();
        ViewCreator viewCreator = null;
        for (int i = 0; i < getSupportFragmentManager().getBackStackEntryCount(); i++) {
            Fragment fragment =
                    getSupportFragmentManager().findFragmentById(getSupportFragmentManager()
                            .getBackStackEntryAt(i).getId());
            if (fragment instanceof AppCMSPageFragment) {
                viewCreator = ((AppCMSPageFragment) fragment).getViewCreator();
            }
        }

        if (updatedAppCMSBinder != null && viewCreator != null) {
            appCMSPresenter.removeLruCacheItem(this, updatedAppCMSBinder.getPageId());
        }

        try {
            unregisterReceiver(presenterActionReceiver);
            unregisterReceiver(wifiConnectedReceiver);
            //unregisterReceiver(phoneHintReceiver);
            unregisterReceiver(downloadReceiver);
            unregisterReceiver(notifyUpdateListsReceiver);
            unregisterReceiver(processDeeplinkReceiver);
            unregisterReceiver(processOpenBrowserReceiver);
            unregisterReceiver(refreshPageDataReceiver);
            unregisterReceiver(uaReceiveChannelIdReceiver);
            unregisterReceiver(uaReceiveAppKeyReceiver);
            unregisterReceiver(gmsReceiveInstanceIdReceiver);
            unregisterReceiver(progressDialogReceiver);
            unregisterReceiver(clearBackStackReceiver);
        } catch (IllegalArgumentException e) {
//            Log.e(TAG, "receiver not regiestered " + e.getMessage());
            e.printStackTrace();
        }

        Intent unregisterInitReceivers = new Intent("INITIALIZATION");
        unregisterInitReceivers.putExtra("init_action", "unregister_receiver");
        sendBroadcast(unregisterInitReceivers);

        InputMethodManager imm =
                (InputMethodManager) this.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null && appCMSParentView != null) {
            imm.hideSoftInputFromWindow(appCMSParentView.getWindowToken(), 0);
        }

        appCMSPresenter.setCancelAllLoads(true);

        appCMSPresenter.dismissPopupWindowPlayer(true);

        appCMSPresenter.resetLaunched();
        appCMSPresenter.clearVideoPlayerViewCache();
        appCMSPresenter.clearWebViewCache();
        appPreference.setMiniPLayerVisibility(false);
        appPreference.setAppHomeActivityCreated(false);
        appCMSPresenter.setIsMiniPlayerPlaying(true);

        if (mAppUpdateHelper != null) {
            mAppUpdateHelper.onDestroy();
        }

        CastServiceProvider.releaseInstance();

        if (disposable != null)
            disposable.dispose();

    }

    @Override
    public void onSuccess(AppCMSBinder appCMSBinder) {
        appCMSPresenter.restartInternalEvents();
        resumeInternalEvents = true;

        if (appCMSBinder != null && appCMSBinder.getSearchQuery() != null) {
            processDeepLink(appCMSBinder.getSearchQuery());
            appCMSBinder.clearSearchQuery();
        }
    }

    private void showEntitlementOptions(AppCMSBinder appCMSBinder) {
        if (appCMSPresenter.getAppCMSMain().isMonetizationModelEnabled() && appCMSBinder != null) {
            appCMSPresenter.removeWaysToWatch();
            if (appCMSPresenter.isPageAVideoPage(appCMSBinder.getPageName())) {
                if (appCMSBinder.getAppCMSPageAPI() != null) {
                    ModuleList moduleListApi = appCMSPresenter.getRelatedModuleForBlock(appCMSBinder.getAppCMSPageUI().getModuleList(), getString(R.string.ui_block_video_player_info_02));
                    if (moduleListApi == null) {
                        moduleListApi = appCMSPresenter.getRelatedModuleForBlock(appCMSBinder.getAppCMSPageUI().getModuleList(), getString(R.string.ui_block_video_player_info_03));
                        if (moduleListApi == null) {
                            moduleListApi = appCMSPresenter.getRelatedModuleForBlock(appCMSBinder.getAppCMSPageUI().getModuleList(), getString(R.string.ui_block_video_player_info_04));
                            if (moduleListApi == null) {
                                moduleListApi = appCMSPresenter.getRelatedModuleForBlock(appCMSBinder.getAppCMSPageUI().getModuleList(), getString(R.string.ui_block_video_player_info_05));
                                if (moduleListApi == null) {
                                    moduleListApi = appCMSPresenter.getRelatedModuleForBlock(appCMSBinder.getAppCMSPageUI().getModuleList(), getString(R.string.ui_block_video_player_info_06));
                                    if (moduleListApi == null) {
                                        moduleListApi = appCMSPresenter.getRelatedModuleForBlock(appCMSBinder.getAppCMSPageUI().getModuleList(), getString(R.string.ui_block_video_player_info_07));
                                    }
                                }
                            }
                        }
                    }


                    if (moduleListApi != null) {
                        Module module = appCMSPresenter.matchModuleAPIToModuleUI(moduleListApi, appCMSBinder.getAppCMSPageAPI());
                        module.getContentData().get(0).setFromEntitlement(false);
                        module.getContentData().get(0).setModuleApi(module);
                        appCMSPresenter.openEntitlementScreen(module.getContentData().get(0), false);
                    }
                }

            }

            if (appCMSPresenter.isPageABundlePage(appCMSBinder.getPageId(), appCMSBinder.getPageName())) {
                if (appCMSBinder.getAppCMSPageAPI() != null) {
                    ModuleList moduleListApi = appCMSPresenter.getRelatedModuleForBlock(appCMSBinder.getAppCMSPageUI().getModuleList(), getString(R.string.ui_block_bundleDetail_01));
                    if (moduleListApi == null) {
                        moduleListApi = appCMSPresenter.getRelatedModuleForBlock(appCMSBinder.getAppCMSPageUI().getModuleList(), getString(R.string.ui_block_bundleDetail_02));
                    }
                    if (moduleListApi != null) {
                        Module module = appCMSPresenter.matchModuleAPIToModuleUI(moduleListApi, appCMSBinder.getAppCMSPageAPI());
                        module.getContentData().get(0).getGist().getBundleList().get(0).setModuleApi(module);
                        module.getContentData().get(0).getGist().getBundleList().get(0).setFromEntitlement(false);
                        appCMSPresenter.openEntitlementScreen(module.getContentData().get(0).getGist().getBundleList().get(0), false);
                    }
                }
            }
            if (appCMSPresenter.isShowPage(appCMSBinder.getPageId())) {
                if (appCMSBinder.getAppCMSPageAPI() != null) {
                    ModuleList moduleListApi = appCMSPresenter.getRelatedModuleForBlock(appCMSBinder.getAppCMSPageUI().getModuleList(), getString(R.string.ui_block_showDetail_02));
                    if (moduleListApi == null) {
                        moduleListApi = appCMSPresenter.getRelatedModuleForBlock(appCMSBinder.getAppCMSPageUI().getModuleList(), getString(R.string.ui_block_showDetail_03));
                        if (moduleListApi == null) {
                            moduleListApi = appCMSPresenter.getRelatedModuleForBlock(appCMSBinder.getAppCMSPageUI().getModuleList(), getString(R.string.ui_block_showDetail_04));
                            if (moduleListApi == null) {
                                moduleListApi = appCMSPresenter.getRelatedModuleForBlock(appCMSBinder.getAppCMSPageUI().getModuleList(), getString(R.string.ui_block_showDetail_05));
                                if (moduleListApi == null) {
                                    moduleListApi = appCMSPresenter.getRelatedModuleForBlock(appCMSBinder.getAppCMSPageUI().getModuleList(), getString(R.string.ui_block_showDetail_06));
                                    if (moduleListApi == null) {
                                        moduleListApi = appCMSPresenter.getRelatedModuleForBlock(appCMSBinder.getAppCMSPageUI().getModuleList(), getString(R.string.ui_block_showDetail_07));
                                        if (moduleListApi == null) {
                                            moduleListApi = appCMSPresenter.getRelatedModuleForBlock(appCMSBinder.getAppCMSPageUI().getModuleList(), getString(R.string.ui_block_showDetail_08));
                                        }
                                    }
                                }
                            }
                        }
                    }
                    if (moduleListApi != null) {
                        Module module = appCMSPresenter.matchModuleAPIToModuleUI(moduleListApi, appCMSBinder.getAppCMSPageAPI());
                        if (module.getContentData().get(0).getSeason().size() > 0) {
                            module.getContentData().get(0).getSeason().get(0).getEpisodes().get(0).setModuleApi(module);
                            module.getContentData().get(0).getSeason().get(0).getEpisodes().get(0).setFromEntitlement(false);
                            appCMSPresenter.openEntitlementScreen(module.getContentData().get(0).getSeason().get(0).getEpisodes().get(0), false);
                        }
                    }
                }
            }
        }
    }

    private void checkCleverTapSDK() {
        if (appCMSPresenter.checkCleverTapAvailability() && appCMSPresenter.getCleverTapInstance() != null) {
            appCMSPresenter.initializeCleverTap();
            if (updatedAppCMSBinder != null && updatedAppCMSBinder.getScreenName() != null) {
                appCMSPresenter.sendPageViewEvent("", updatedAppCMSBinder.getScreenName(), null);
            }
            checkUnreadInboxMessageCount();
        }
    }

    public AppCMSBinder getUpdatedAppCMSBinder() {
        return updatedAppCMSBinder;
    }

    @SuppressWarnings("ConstantConditions")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        appCMSPresenter.setCurrentActivity(this);

        if (requestCode == AppCMSPresenter.RC_GOOGLE_SIGN_IN) {
            handleSignInResult(GoogleSignIn.getSignedInAccountFromIntent(data));
            return;
        }

        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == AppCMSPresenter.RC_PHONE_SIGN_IN) {
                /*You will receive user selected phone number here if selected and send it to the server for request the otp*/
                Credential credential = data.getParcelableExtra(Credential.EXTRA_KEY);
                if (credential != null) {
                    String phoneNumberWithCountryCode = PhoneNumberUtils.formatNumber(credential.getId(), Locale.getDefault().getCountry());
                    if (phoneNumberWithCountryCode != null) {
                        try {
                            String[] splitCountryWithNumber = phoneNumberWithCountryCode.split(" ", 2);
                            if (splitCountryWithNumber.length > 0) {
                                appCMSPresenter.setUnformattedPhoneCountryCode(splitCountryWithNumber[0]);
                                appCMSPresenter.setUnformattedPhone(splitCountryWithNumber[1].replaceAll(" ", ""));
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }

            } else if (requestCode == AppCMSPresenter.ADD_GOOGLE_ACCOUNT_TO_DEVICE_REQUEST_CODE) {
                appCMSPresenter.initiateItemPurchase(false);
            } else if (requestCode == AppCMSPresenter.CC_AVENUE_REQUEST_CODE) {
                boolean subscriptionSuccess = data.getBooleanExtra(getString(R.string.app_cms_ccavenue_payment_success),
                        false);
                if (subscriptionSuccess) {
                    appCMSPresenter.finalizeSignupAfterCCAvenueSubscription(false);
                }
            } else if (requestCode == JUSPAY_REQUEST_CODE) {
                if (appCMSPresenter.getJusPayUtils() != null)
                    appCMSPresenter.getJusPayUtils().endSubscriptionCall(data);
            } else {
                if (FacebookSdk.isFacebookRequestCode(requestCode)) {
                    pageLoading(true);
                    if (callbackManager != null)
                        callbackManager.onActivityResult(requestCode, resultCode, data);
                } else if (requestCode == AppCMSPresenter.RC_PURCHASE_PLAY_STORE_ITEM) {
                    appCMSPresenter.finalizeSignupAfterSubscription(data.getStringExtra("INAPP_PURCHASE_DATA"));
                }
            }

        } else if (resultCode == RESULT_CANCELED) {
            loaderWaitingFor3rdPartyLogin = false;
            pageLoading(false);
            /*if (requestCode == AppCMSPresenter.RC_PURCHASE_PLAY_STORE_ITEM) {
                if (!TextUtils.isEmpty(appPreference.getActiveSubscriptionSku())) {
                    appCMSPresenter.showConfirmCancelSubscriptionDialog(retry -> {
                        if (retry) {
                            appCMSPresenter.initiateItemPurchase(false);
                        } else {
                            appCMSPresenter.sendCloseOthersAction(null, true, false);
                        }
                    });
                }
            } else*/
            if (requestCode == JUSPAY_REQUEST_CODE) {
                appCMSPresenter.showJusPayInitilizationErrorDialog();
            }
        }
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            if (account != null) {
                Log.d(TAG, "Google Signin Status Message: " + account.toString());
                if (appCMSPresenter.getLaunchType() == AppCMSPresenter.LaunchType.SUBSCRIBE) {
                    handleCloseAction(false);
                }

                if (appCMSPresenter.getLaunchType() == AppCMSPresenter.LaunchType.VIEWING_RESTRICTIONS_VIEW || appCMSPresenter.getLaunchType() == AppCMSPresenter.LaunchType.VIDEO_PIN_VIEW) {
                    if (appPreference != null && appPreference.getGoogleUserId() != null
                            && appPreference.getGoogleUserId().equalsIgnoreCase(account.getId())) {
                        handleCloseAction(true);
                        if (appCMSPresenter.getLaunchType() == AppCMSPresenter.LaunchType.VIEWING_RESTRICTIONS_VIEW) {
                            if (appPreference != null && !TextUtils.isEmpty(appPreference.getParentalPin())) {
                                appCMSPresenter.navigateToViewingRestrictionsPage();
                            } else {
                                appCMSPresenter.setLaunchType(AppCMSPresenter.LaunchType.VIEWING_RESTRICTIONS_FROM_VIDEO_PIN_VIEW);
                                appCMSPresenter.navigateToChangeVideoPinPage();
                            }
                        } else {
                            appCMSPresenter.navigateToChangeVideoPinPage();
                        }
                    } else {
                        loaderWaitingFor3rdPartyLogin = false;
                        pageLoading(false);
                        new Handler().postDelayed(() -> {
                            String errorTxt = getString(R.string.google_error);
                            if (appCMSPresenter.getModuleApi() != null && appCMSPresenter.getModuleApi().getMetadataMap() != null && appCMSPresenter.getModuleApi().getMetadataMap().getGoogleError() != null) {
                                errorTxt = appCMSPresenter.getModuleApi().getMetadataMap().getGoogleError();
                            }
                            appCMSPresenter.showDialog(AppCMSPresenter.DialogType.SIGNIN, errorTxt, false, null, null, localisedStrings.getSignInText());
                        }, 50);
                    }
                    return;
                }
                appCMSPresenter.setGoogleAccessToken(account.getIdToken(),
                        account.getId(),
                        account.getDisplayName(),
                        account.getEmail(),
                        false,
                        true);
            }
        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.e(TAG, "signInResult:failed code=" + e.getStatusCode());
            String message = null;
            switch (e.getStatusCode()) {
                case CommonStatusCodes.API_NOT_CONNECTED:
                    message = "The API is not connected.";
                    break;

                case CommonStatusCodes.CANCELED:
                    break;

                case CommonStatusCodes.DEVELOPER_ERROR:
                    message = "The app is configured incorrectly.";
                    break;

                case CommonStatusCodes.ERROR:
                    message = "An error has occurred.";
                    break;

                case CommonStatusCodes.INTERNAL_ERROR:
                    message = "An internal server error has occurred.";
                    break;

                case CommonStatusCodes.INTERRUPTED:
                    message = "The login attempt was interrupted.";
                    break;

                case CommonStatusCodes.INVALID_ACCOUNT:
                    message = "An invalid account is being used.";
                    break;

                case CommonStatusCodes.NETWORK_ERROR:
                    message = "A network error has occurred.";
                    break;

                case CommonStatusCodes.RESOLUTION_REQUIRED:
                    message = "Additional resolution is required.";
                    break;

                case CommonStatusCodes.SIGN_IN_REQUIRED:
                    message = "Sign In is required.";
                    break;

                case CommonStatusCodes.TIMEOUT:
                    message = "A timeout has occurred.";
                    break;

                default:
                    break;
            }
            if (!TextUtils.isEmpty(message)) {
                appCMSPresenter.showDialog(AppCMSPresenter.DialogType.SIGNIN,
                        message,
                        false,
                        null,
                        null,
                        null);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        switch (requestCode) {
            case AppCMSPresenter.REQUEST_WRITE_EXTERNAL_STORAGE_FOR_DOWNLOADS:
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    appCMSPresenter.resumeDownloadAfterPermissionGranted();
                }
                break;
            default:
                break;
        }
    }

    @SuppressWarnings("ConstantConditions")
    @Override
    public void onError(AppCMSBinder appCMSBinder) {
        if (appCMSBinder != null && appCMSBinder.getPageId() != null) {
            //Log.e(TAG, "Nav item - DialogType attempting to launch page: "
//                    + appCMSBinder.getPageName() + " - " + appCMSBinder.getPageId());
            if (!appCMSBinderStack.isEmpty() &&
                    !TextUtils.isEmpty(appCMSBinderStack.peek()) &&
                    appCMSBinderStack.peek().equals(appCMSBinder.getPageId())) {
                try {
                    getSupportFragmentManager().popBackStackImmediate();
                } catch (NullPointerException | IllegalStateException e) {
                    //Log.e(TAG, "DialogType popping back stack: " + e.getMessage());
                }
                handleBack(true, false, false, true);
            }
        }
        if (!appCMSBinderStack.isEmpty()) {
            handleLaunchPageAction(appCMSBinderMap.get(appCMSBinderStack.peek()),
                    false,
                    false,
                    false);
        } else {
            if (appCMSPresenter.isNetworkConnected()) {
                finish();
            }
        }
    }


    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);


        if (updatedAppCMSBinder != null && appCMSPresenter.isBlockShowDetail06(updatedAppCMSBinder.getAppCMSPageUI()) || (appCMSPresenter.videoPlayerView != null /*&& appCMSPresenter.videoPlayerView.getPlayer().isPlaying()*/)) {
            handleOrientationForEpisodePlayer(newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE);
            if (updatedAppCMSBinder.isShowDetailsPage()) {
                handleToolbar(false,
                        updatedAppCMSBinder.getAppCMSMain(),
                        updatedAppCMSBinder.getPageId());
            }
            return;
        }
        appCMSPresenter.setDownlistScreenCache(null);
        if (updatedAppCMSBinder != null && updatedAppCMSBinder.getScreenName() != null && updatedAppCMSBinder.getScreenName().equalsIgnoreCase(getResources().getString(R.string.rest_workout_screen))) {
            appCMSPresenter.restrictLandscapeOnly();
            return;
        }
        if (appCMSPresenter.isFullScreenVisible && appCMSPresenter.videoPlayerView != null) {
            appCMSPresenter.restrictLandscapeOnly();
            if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
                appCMSPresenter.exitFullScreenPlayer();
                appCMSPresenter.sendRefreshPageAction();
            }
            return;
        }

        AppCMSBinder appCMSBinder = !appCMSBinderStack.isEmpty() ?
                appCMSBinderMap.get(appCMSBinderStack.peek()) :
                null;
        if (appCMSPresenter != null) {
            if (appCMSBinder != null &&
                    appCMSBinder.isShowDetailsPage()) {
                handleToolbar(false,
                        appCMSBinder.getAppCMSMain(),
                        appCMSBinder.getPageId());
            } else {
                appCMSPresenter.cancelInternalEvents();
                appCMSPresenter.onConfigurationChange(true);
                if (appCMSPresenter.isMainFragmentViewVisible()) {
                    if (!appCMSPresenter.isMainFragmentTransparent()) {
                        appCMSPresenter.showMainFragmentView(true);
                    }
                    if (appCMSBinder != null) {
                        appCMSPresenter.pushActionInternalEvents(appCMSBinder.getPageId()
                                + BaseView.isLandscape(this));
                        handleLaunchPageAction(appCMSBinder,
                                true,
                                false,
                                false);
                    }
                }
            }
        }
    }

    public void pageLoading(boolean pageLoading) {

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (pageLoading) {
                    appCMSPresenter.setMainFragmentTransparency(0.5f);
                    appCMSFragment.setEnabled(false);
                    appCMSTabNavContainer.setEnabled(false);
                    loadingProgressBar.setVisibility(View.VISIBLE);
                    loadingProgressBar.bringToFront();
                    //while progress bar loading disable user interaction
                    getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                            WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                    for (int i = 0; i < appCMSTabNavContainer.getChildCount(); i++) {
                        appCMSTabNavContainerItems.getChildAt(i).setEnabled(false);
                    }
                    appCMSPresenter.setPageLoading(true);
                } else if (!loaderWaitingFor3rdPartyLogin) {
                    appCMSPresenter.setMainFragmentTransparency(1.0f);
                    if (appCMSPresenter.isAddOnFragmentVisible()) {
                        appCMSPresenter.showAddOnFragment(true, 0.09f);
                    }
                    appCMSFragment.setEnabled(true);
                    appCMSTabNavContainer.setEnabled(true);
                    loadingProgressBar.setVisibility(View.GONE);
                    //clear user interaction blocker flag
                    getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                    for (int i = 0; i < appCMSTabNavContainer.getChildCount(); i++) {
                        appCMSTabNavContainerItems.getChildAt(i).setEnabled(true);
                    }
                    appCMSPresenter.setPageLoading(false);
                }
            }
        });

    }

    private boolean isPageLoading() {
        return (loadingProgressBar.getVisibility() == View.VISIBLE);
    }

    private void handleBack(boolean popBinderStack,
                            boolean closeActionPage,
                            boolean recurse,
                            boolean popActionStack) {
        if (popBinderStack && !appCMSBinderStack.isEmpty()) {
            appCMSBinderMap.remove(appCMSBinderStack.peek());
            appCMSBinderStack.pop();
            if (popActionStack) {
                appCMSPresenter.popActionInternalEvents();
            }
        }

        if (!appCMSBinderStack.isEmpty()) {
            updatedAppCMSBinder = appCMSBinderMap.get(appCMSBinderStack.peek());
            Log.d(TAG, "Back pressed - handling nav bar");
            handleNavbar(appCMSBinderMap.get(appCMSBinderStack.peek()));
            if (appCMSBinderMap.get(appCMSBinderStack.peek()) != null &&
                    appCMSBinderMap.get(appCMSBinderStack.peek()).getPageName() != null) {
                Log.d(TAG, "Resetting previous AppCMS data: "
                        + appCMSBinderMap.get(appCMSBinderStack.peek()).getPageName());
            } else if (appCMSBinderMap.get(appCMSBinderStack.peek()) == null) {
                appCMSBinderStack.pop();
            }
        }

        if (shouldPopStack(null) || closeActionPage) {
            try {
                getSupportFragmentManager().popBackStackImmediate();
            } catch (NullPointerException | IllegalStateException e) {
                Log.e(TAG, "Failed to pop Fragment from back stack");
            }
            if (recurse) {
                Log.d(TAG, "Handling back - recursive op");
                handleBack(popBinderStack,
                        closeActionPage && !appCMSBinderStack.isEmpty(),
                        recurse,
                        popActionStack);
            }
        }

        if (updatedAppCMSBinder != null) {
            /*
             * casting button will show only on home page, movie page and player page so check which
             * page will be open
             */
            if (!castDisabled) {
                setMediaRouterButtonVisibility(updatedAppCMSBinder.getPageId());
            }
            handleToolbar(updatedAppCMSBinder.isAppbarPresent(),
                    updatedAppCMSBinder.getAppCMSMain(),
                    updatedAppCMSBinder.getPageId());
            handleNavbar(updatedAppCMSBinder);
            handleMiniPlayerVisibility(updatedAppCMSBinder);
        }
    }

    private void resume() {
        appCMSPresenter.restartInternalEvents();

        if (appCMSBinderStack != null && !appCMSBinderStack.isEmpty()) {
            //Log.d(TAG, "Activity resumed - resetting nav item");
            selectNavItem(appCMSBinderStack.peek());
        }

        if (!isActive) {
            if (updatedAppCMSBinder != null) {
                if (updatedAppCMSBinder.getExtraScreenType() != AppCMSPresenter.ExtraScreenType.BLANK) {

                    handleLaunchPageAction(updatedAppCMSBinder,
                            appCMSPresenter.getConfigurationChanged(),
                            false,
                            false);
                }
            }
        }

        appCMSPresenter.setVideoPlayerHasStarted();

        isActive = true;

        if (shouldSendCloseOthersAction && appCMSPresenter != null) {
            appCMSPresenter.sendCloseOthersAction(null, false, false);
            shouldSendCloseOthersAction = false;
        }

        setCastingInstance();

        if (updatedAppCMSBinder != null &&
                updatedAppCMSBinder.getExtraScreenType() == AppCMSPresenter.ExtraScreenType.BLANK) {
            pageLoading(true);
        }

        if (updatedAppCMSBinder != null &&
                updatedAppCMSBinder.getExtraScreenType() == AppCMSPresenter.ExtraScreenType.SEARCH) {
            mSearchTopButton.setVisibility(View.GONE);
        } else if (shouldShowSearchInToolbar()) {
            mSearchTopButton.setVisibility(View.VISIBLE);
        }
    }

    private boolean shouldPopStack(String newPageId) {
        return !isBinderStackEmpty()
                && !isBinderStackTopNull()
                && ((!TextUtils.isEmpty(newPageId) && appCMSPresenter.isPagePrimary(newPageId))
                && !appCMSPresenter.isPagePrimary(appCMSBinderStack.peek())
                && appCMSBinderMap.get(appCMSBinderStack.peek()).getExtraScreenType() != AppCMSPresenter.ExtraScreenType.SEARCH)
                && !waitingForSubscriptionFinalization()
                && !atMostOneUserPageOnTopStack(newPageId);
    }

    public boolean isBinderStackEmpty() {
        return appCMSBinderStack.isEmpty();
    }

    private boolean isBinderStackTopNull() {
        return appCMSBinderMap.get(appCMSBinderStack.peek()) == null;
    }

    private boolean waitingForSubscriptionFinalization() {
        return (appCMSPresenter.isViewPlanPage(appCMSBinderStack.peek()) &&
                !appCMSPresenter.isUserSubscribed());
    }


    public void openLeftDrawer() {


        //appCMSParentLayout.setVisibility(View.INVISIBLE);
        appCMSLeftDrawerLayout.openDrawer(GravityCompat.START);
        appCMSLeftDrawerLayout.setVisibility(View.VISIBLE);
        leftNavList = new ArrayList<NavigationPrimary>();
        leftNavList.clear();

        for (int i = 0; i < appCMSPresenter.getAppCMSAndroid().getHeaders().size(); i++) {

            if (appCMSPresenter.getAppCMSAndroid().getHeaders().get(i).getId().equalsIgnoreCase(leftNavId)) {
                for (int j = 0; j < appCMSPresenter.getAppCMSAndroid().getHeaders().get(i).getData().size(); j++) {
                    leftNavList.add(appCMSPresenter.getAppCMSAndroid().getHeaders().get(i).getData().get(j));
                }
            }

            appCMSLeftDrawerList.addOnItemTouchListener(new RecyclerItemClickListner(this, new RecyclerItemClickListner.OnItemClickListener() {
                @Override
                public void onItemClick(View vw, int position) {
                    if (appCMSLeftDrawerLayout.isDrawerOpen(GravityCompat.START)) {
                        appCMSLeftDrawerLayout.closeDrawer(Gravity.LEFT, false);
                        appCMSLeftDrawerLayout.closeDrawers();
                    }

                    try {
                        if (leftNavList.get(position).getDisplayedPath().equalsIgnoreCase("Sub Navigation Screen")) {
                            appCMSPresenter.navigateToDisplayPage(leftNavList.get(position).getPageId(),
                                    leftNavList.get(position).getTitle(),
                                    leftNavList.get(position).getUrl(),
                                    false,
                                    true,
                                    false,
                                    true,
                                    false,
                                    null, leftNavList.get(position).getItems());
                        } else {
//                            try {
//                                Glide.with(getApplicationContext()).load(leftNavList.get(position).getIcon())// resizes the image to these dimensions (in pixel)
//                                        .fitCenter().
//                                        into(appCMSHeaderImage);
//                            } catch (Exception ex) {
//                                ex.printStackTrace();
//                            }

                            if (leftNavList.get(position).getItems() != null
                                    && leftNavList.get(position).getItems().size() > 0) {
                                appCMSPresenter.navigateToDisplayPage(leftNavList.get(position).getPageId(),
                                        leftNavList.get(position).getTitle(),
                                        leftNavList.get(position).getUrl(),
                                        false,
                                        true,
                                        false,
                                        true,
                                        false,
                                        null, leftNavList.get(position).getItems());
                            } else {
                                appCMSPresenter.navigateToPage(leftNavList.get(position).getPageId(),
                                        leftNavList.get(position).getDisplayedPath(),
                                        leftNavList.get(position).getUrl(),
                                        false,
                                        true,
                                        false,
                                        true,
                                        false,
                                        null);
                            }


                        }


                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }


                }
            }
            ));

        }

        appCMSLeftDrawerList.setBackgroundColor(appCMSPresenter.getGeneralBackgroundColor());
        if (leftNavList != null && leftNavList.size() > 0) {
            appCMSLeftNavigationMenuAdapter = new AppCMSLeftNavigationMenuAdapter(getApplicationContext(), leftNavList, this, appCMSPresenter);
            appCMSLeftDrawerList.setAdapter(appCMSLeftNavigationMenuAdapter);
            appCMSLeftDrawerList.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL,
                    false));

        }


    }

    private boolean atMostOneUserPageOnTopStack(String newPageId) {
        return (newPageId == null ||
                !appCMSBinderStack.isEmpty() &&
                        ((appCMSPresenter.isPageUser(appCMSBinderStack.peek()) &&
                                !appCMSPresenter.isPageUser(newPageId)) ||
                                (!appCMSPresenter.isPageUser(appCMSBinderStack.peek())) &&
                                        appCMSPresenter.isPageUser(newPageId)));
    }

    private void createScreenFromAppCMSBinder(final AppCMSBinder appCMSBinder) {
        //Log.d(TAG, "Handling new AppCMSBinder: " + appCMSBinder.getPageName());

        pageLoading(false);

        handleOrientation(getResources().getConfiguration().orientation, appCMSBinder);
        /*
         * casting button will show only on home page, movie page and player page so check which
         * page will be open
         */
        if (!castDisabled) {
            setMediaRouterButtonVisibility(appCMSBinder.getPageId());
        }
        checkHomePage(appCMSBinder.getPageId());
        appCMSPresenter.setCurrentPageName(appCMSBinder.getPageId());
        appCMSPresenter.setCurrentAppCMSBinder(appCMSBinder);
        createFragment(appCMSBinder);
    }

    private void checkHomePage(String pageId) {
        if ((appCMSPresenter.findHomePageNavItem() != null &&
                !TextUtils.isEmpty(appCMSPresenter.findHomePageNavItem().getPageId()) &&
                appCMSPresenter.findHomePageNavItem().getPageId().equalsIgnoreCase(pageId)) ||

                (appCMSPresenter.findMoviesPageNavItem() != null &&
                        !TextUtils.isEmpty(appCMSPresenter.findMoviesPageNavItem().getPageId()) &&
                        appCMSPresenter.findMoviesPageNavItem().getPageId().equalsIgnoreCase(pageId)) &&
                        !appCMSPresenter.isWatchlistPage(pageId)) {
            setCastingVisibility(true);
            CastServiceProvider.getInstance(this).isHomeScreen(true);
        } else {
            setCastingVisibility(false);
            CastServiceProvider.getInstance(this).isHomeScreen(false);
        }

        if (!CastServiceProvider.getInstance(this).isOverlayVisible()) {
            CastServiceProvider.getInstance(this).showCastoverlay();
        }
    }

    private void createFragment(AppCMSBinder appCMSBinder) {
        try {
            getSupportFragmentManager().addOnBackStackChangedListener(this);
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            Fragment appCMSPageFragment = null;
            if (appCMSPresenter != null || (appCMSBinder.getAppCMSPageUI() == null ||
                    appCMSBinder.getAppCMSPageUI().getModuleList() == null) || (appCMSBinder != null && appCMSBinder.getAppCMSPageUI() != null &&
                    appCMSBinder.getAppCMSPageUI().getModuleList() != null && appCMSBinder.getAppCMSPageUI().getModuleList().size() > 0 &&
                    !(appCMSPresenter.getModuleListByName(updatedAppCMSBinder.getAppCMSPageUI().getModuleList(), getString(R.string.app_cms_page_module_key_showdetail_06)) != null))) {
                if (appCMSPresenter.getTrailerPlayerView() != null) {
                    appCMSPresenter.getTrailerPlayerView().stopPlayer();
                }
            }
            switch (appCMSBinder.getExtraScreenType()) {
                case NAVIGATION:
                    try {
                        appCMSPresenter.setEntitlementPendingVideoData(null);
                        appCMSPageFragment = AppCMSNavItemsFragment.newInstance(this, appCMSBinder);
                        //send menu screen event for firebase
                        appCMSPresenter.getFirebaseAnalytics().screenViewEvent(getString(R.string.value_menu_screen));
                    } catch (IllegalArgumentException e) {
                        e.printStackTrace();
                    }
                    break;
                case TEAM:
                    try {
                        if (appCMSBinder.getNavigation().getTabBar() != null && appCMSPresenter.isPageTeamNavigationPage(appCMSBinder.getNavigation().getTabBar())) {
                            appCMSPageFragment =
                                    AppCMSTeamListFragment.newInstance(this,
                                            appCMSBinder,
                                            Color.parseColor(appCMSBinder.getAppCMSMain().getBrand().getGeneral().getTextColor()),
                                            Color.parseColor(appCMSBinder.getAppCMSMain().getBrand().getGeneral().getBackgroundColor()),
                                            Color.parseColor(appCMSBinder.getAppCMSMain().getBrand().getGeneral().getPageTitleColor()),
                                            Color.parseColor(appCMSBinder.getAppCMSMain().getBrand().getCta().getPrimary().getBackgroundColor()));
                            //send menu screen event for firebase
                            appCMSPresenter.getFirebaseAnalytics().screenViewEvent(getString(R.string.value_team_navigation_screen));
                        }
                    } catch (IllegalArgumentException ignored) {
                    }
                    break;
                case SEARCH:
                    try {
                        appCMSPageFragment = AppCMSSearchFragment.newInstance(this,
                                Long.parseLong(appCMSPresenter.getAppBackgroundColor()
                                        .replace("#", ""), 16),
                                Long.parseLong(appCMSBinder.getAppCMSMain().getBrand().getCta().getPrimary().getBackgroundColor()
                                        .replace("#", ""), 16),
                                Long.parseLong(appCMSBinder.getAppCMSMain().getBrand().getCta().getPrimary().getTextColor()
                                        .replace("#", ""), 16));
                        appCMSPresenter.getFirebaseAnalytics().screenViewEvent(getString(R.string.value_search_screen));
                    } catch (NumberFormatException ignored) {
                    }
                    break;

                case RESET_PASSWORD:
                    appCMSPageFragment = AppCMSResetPasswordFragment.newInstance(this,
                            appCMSBinder,
                            appCMSBinder.getPagePath());
                    appCMSPresenter.onOrientationChange(true);
                    break;

                case EDIT_PROFILE:
                    appCMSPageFragment = AppCMSEditProfileFragment.newInstance(this,
                            appPreference.getLoggedInUserName(),
                            appPreference.getLoggedInUserEmail(),
                            appCMSBinder);
                    break;
                case REST_SCREEN:
                    appCMSPageFragment =
                            AppCMSRestDeatilsFragment.newInstance(this,
                                    appPreference.getLoggedInUserName(),
                                    appPreference.getLoggedInUserEmail(),
                                    appCMSBinder);
                    break;
                case CHANGE_PASSWORD:
                    appCMSPageFragment = AppCMSChangePasswordFragment.newInstance(this,
                            appCMSBinder);
                    break;

                case NONE:
                    appCMSPageFragment = AppCMSPageFragment.newInstance(this, appCMSBinder);
                    showEntitlementOptions(appCMSBinder);
                    break;

                case MATH_PROBLEM_SCREEN:
                    appCMSPageFragment = MathProblemFragment.newInstance(this, appCMSBinder);
                    break;
                case PARENTAL_CONTROLS:
                    appCMSPageFragment = AppCMSParentalControlsFragment.newInstance(this, appCMSBinder);
                    break;
                case VIDEO_PIN:
                    appCMSPageFragment = AppCMSParentalPINFragment.newInstance(this, appCMSBinder);
                    break;
                case VIEWING_RESTRICTIONS:
                    appCMSPageFragment = AppCMSParentalRatingFragment.newInstance(this, appCMSBinder);
                    break;
                case RE_AUTHORISE_USER:
                    appCMSPageFragment = AppCMSReauthoriseUserFragment.newInstance(this, appCMSBinder);
                    break;
                case GET_SOCIAL:
                    appCMSPageFragment = AppCMSGetSocialFragment.newInstance(this, appCMSBinder);
                    break;
                case TV_PROVIDER_SCREEN:
                    appCMSPageFragment = TVProviderFragment.newInstance(this, appCMSBinder);
                    break;
                case WEB_VIEW_SCREEN:
                    appCMSPageFragment = AppCMSWebviewFragment.newInstance(this, appCMSBinder);
                    break;
                case GENERIC_LOGIN_SIGNUP:
                    appCMSPageFragment = GenericAuthenticationFragment.newInstance(this, appCMSBinder);
                    break;
                case USER_PROFILE_SETTINGS:
                    appCMSPresenter.getUserData(null);
                    appCMSPageFragment = UserProfileSettingsFragment.newInstance(this, appCMSBinder);
                    break;
                default:
                    break;
            }
            handleMiniPlayerVisibility(updatedAppCMSBinder);
            /*if (!(appCMSPageFragment instanceof AppCMSPageFragment) && appCMSPresenter.videoPlayerView != null) {
                appCMSPresenter.videoPlayerView.pausePlayer();
            }*/
            if (appCMSPageFragment != null) {
                if (appCMSBinder.isShowDetailsPage()) {
                    Fragment fragment = getSupportFragmentManager().findFragmentByTag(appCMSBinder.getPageId() + BaseView.isLandscape(this));
                    if (fragment != null)
                        fragmentTransaction.remove(fragment);
                    fragmentTransaction.add(R.id.app_cms_fragment, appCMSPageFragment, appCMSBinder.getPageId() + BaseView.isLandscape(this));
                } else {
                    fragmentTransaction.replace(R.id.app_cms_fragment, appCMSPageFragment, appCMSBinder.getPageId() + BaseView.isLandscape(this));
                }
                fragmentTransaction.addToBackStack(appCMSBinder.getPageId() + BaseView.isLandscape(this));
                fragmentTransaction.commit();
                getSupportFragmentManager().executePendingTransactions();
            }
        } catch (NullPointerException | IllegalStateException | IllegalArgumentException e) {
            e.printStackTrace();
            Log.e(TAG, "Failed to add Fragment to back stack");
        }
    }

    private void handleMiniPlayerVisibility(AppCMSBinder appCMSBinder) {
        if (appCMSBinder != null && appCMSBinder.getAppCMSPageUI() != null &&
                appCMSBinder.getAppCMSPageUI().getModuleList() != null && appCMSBinder.getAppCMSPageUI().getModuleList().size() > 0 &&
                (appCMSPresenter.getModuleListByName(this.updatedAppCMSBinder.getAppCMSPageUI().getModuleList(), getString(R.string.standaloneplayer02)) != null)) {
            //showMiniPlayer.setVisibility(View.VISIBLE);
            appCMSNewsTemplateHeader.setVisibility(View.VISIBLE);
            appCMSHeaderImage.setVisibility(View.GONE);
        } else {
            // showMiniPlayer.setVisibility(View.GONE);
            appCMSNewsTemplateHeader.setVisibility(View.GONE);
            appCMSHeaderImage.setVisibility(View.VISIBLE);
        }
        if (appPreference.getShowPIPVisibility()) {
            showMiniPlayer.setChecked(true);
            switchColor(true);
        }
    }

    public void selectNavItemAndLaunchPage(NavBarItemView v, String pageId, String pageTitle) {
        boolean appbarPresent = true;
        if (appCMSPresenter.navigateToPage(pageId,
                pageTitle,
                null,
                false,
                appbarPresent,
                false,
                true,
                false,
                null)) {
        } else {
            selectNavItem(v);
        }
    }

    @Override
    public void closeMenuPageIfHighlighted(NavBarItemView menuNavBarItemView) {
        if (!menuNavBarItemView.isItemSelected()) {
            resumeInternalEvents = true;
            selectNavItem(menuNavBarItemView);
        } else {
            unselectNavItem(menuNavBarItemView);
            appCMSPresenter.sendCloseOthersAction(null, true, false);
        }
    }

    private void selectNavItem(NavBarItemView v) {
        if (v != null && v.getTag() != null) {
            unselectAllNavItems();
            NavTabTag navigationTabTag = (NavTabTag) v.getTag();
            v.select(true, navigationTabTag);
        }
    }

    private void unselectAllNavItems() {
        for (int i = 0; i < appCMSTabNavContainerItems.getChildCount(); i++) {
            if (appCMSTabNavContainerItems.getChildAt(i) instanceof NavBarItemView) {
                unselectNavItem((NavBarItemView) appCMSTabNavContainerItems.getChildAt(i));
            }
        }
    }

    private void unselectNavItem(NavBarItemView v) {
        NavTabTag navigationTabTag = (NavTabTag) v.getTag();

        v.select(false, navigationTabTag);
    }

    public NavBarItemView getSelectedNavItem() {
        for (int i = 0; i < appCMSTabNavContainerItems.getChildCount(); i++) {
            if (((NavBarItemView) appCMSTabNavContainerItems.getChildAt(i)).isItemSelected()) {
                return (NavBarItemView) appCMSTabNavContainerItems.getChildAt(i);
            }
        }
        return null;
    }

    @Override
    public void setSelectedMenuTabIndex(int selectedMenuTabIndex) {
        // navMenuPageIndex = selectedMenuTabIndex;
    }

    @Override
    public void setSelectedSearchTabIndex(int selectedSearchTabIndex) {
        //navSearchPageIndex = selectedSearchTabIndex;
    }

    private void handleNavbar(AppCMSBinder appCMSBinder) {
        if (appCMSBinder != null && !isDeepLink) {
            final Navigation navigation = appCMSBinder.getNavigation();
            //final ModuleList moduleFooter = appCMSBinder.getAppCMSPageUI() != null ? appCMSBinder.getAppCMSPageUI().getModuleList().get(appCMSBinder.getAppCMSPageUI().getModuleList().size() - 1) : null;
            final ModuleList moduleFooter = appCMSPresenter.getTabBarUIFooterModule();
            if (navigation != null && navigation.getNavigationPrimary() != null &&
                    navigation.getNavigationPrimary().isEmpty() || !appCMSBinder.isNavbarPresent()) {  // for the pages like Hoichoi it is Used here where we dont getting value in settings
                appCMSTabNavContainer.setVisibility(View.GONE);
            } else {
                appCMSTabNavContainer.setVisibility(View.VISIBLE);
                selectNavItem(appCMSBinder.getPageId());
            }
        }
    }

    private void handleOrientation(int orientation, AppCMSBinder appCMSBinder) {
        if (appCMSBinder != null) {
            if (appCMSBinder.isFullScreenEnabled() &&
                    (orientation == Configuration.ORIENTATION_LANDSCAPE)) {
                handleToolbar(false,
                        appCMSBinder.getAppCMSMain(),
                        appCMSBinder.getPageId());
                hideSystemUI(getWindow().getDecorView());
            } else {
                handleToolbar(appCMSBinder.isAppbarPresent(),
                        appCMSBinder.getAppCMSMain(),
                        appCMSBinder.getPageId());
                showSystemUI(getWindow().getDecorView());
            }
            handleNavbar(appCMSBinder);
            if (appCMSBinder != null && appCMSBinder.getScreenName() != null && appCMSBinder.getScreenName().equalsIgnoreCase(getResources().getString(R.string.rest_workout_screen))) {
//                appCMSPresenter.onConfigurationChange(true);
//                appCMSPresenter.onOrientationChange(true);
//                appCMSPresenter.rotateToLandscape();
                handleToolbar(false,
                        appCMSBinder.getAppCMSMain(),
                        appCMSBinder.getPageId());
                hideSystemUI(getWindow().getDecorView());
            }
        }
    }

    private void handleToolbar(boolean appbarPresent, AppCMSMain appCMSMain, String pageId) {
        if (!appbarPresent) {
            appBarLayout.setVisibility(View.GONE);
        } else {
            try {
                toolbar.setTitleTextColor(Color.parseColor(appCMSMain
                        .getBrand()
                        .getGeneral()
                        .getTextColor()));
            } catch (IllegalArgumentException e) {
                //Log.e(TAG, "Error in parsing color. " + e.getLocalizedMessage());
            } catch (Exception e) {
                toolbar.setTitleTextColor(ContextCompat.getColor(this, R.color.colorAccent));
            }
            setSupportActionBar(toolbar);
            ActionBar supportActionBar = getSupportActionBar();
            if (supportActionBar != null) {
                supportActionBar.setDisplayHomeAsUpEnabled(false);
                supportActionBar.setDisplayShowHomeEnabled(false);
                supportActionBar.setHomeButtonEnabled(false);
                supportActionBar.setTitle("");
            }
            appBarLayout.setVisibility(View.VISIBLE);

            if (appCMSPresenter.isPagePrimary(pageId) &&
                    !appCMSPresenter.isViewPlanPage(pageId)) {
                closeButton.setVisibility(View.GONE);
            } else if (appCMSPresenter.isViewPlanPage(pageId)) {
                closeButton.setVisibility(View.VISIBLE);
                setCastingVisibility(false);
            } else {
                closeButton.setVisibility(View.VISIBLE);
            }


            if (appCMSPresenter.isArticlePage(updatedAppCMSBinder.getPageId()) ||
                    appCMSPresenter.isPhotoGalleryPage(updatedAppCMSBinder.getPageId()) ||
                    appCMSPresenter.isPageAVideoPage(updatedAppCMSBinder.getPageName()) ||
                    appCMSPresenter.isPageAtPersonDetailPage(updatedAppCMSBinder.getPageName())) {
                mShareTopButton.setVisibility(View.VISIBLE);
                mSearchTopButton.setVisibility(View.VISIBLE);
                setCastingVisibility(false);
            } else {
                mShareTopButton.setVisibility(View.GONE);
                mSearchTopButton.setVisibility(View.GONE);
                if (appCMSPresenter.isHomePage(updatedAppCMSBinder.getPageId()))
                    setCastingVisibility(true);
                else
                    setCastingVisibility(false);
            }
            handleSearchButtonVisiblity();
//            setMediaRouterButtonVisibility(pageId);
        }
    }

    public void handleSearchButtonVisiblity() {
        if (updatedAppCMSBinder != null && updatedAppCMSBinder.getNavigation() != null && updatedAppCMSBinder.getNavigation().getRight() != null) {
            if (appCMSPresenter.isPageSearch(updatedAppCMSBinder.getScreenName())) {
                mSearchTopButton.setVisibility(View.GONE);
            } else {
                mSearchTopButton.setVisibility(View.VISIBLE);
            }
        } else {
            mSearchTopButton.setVisibility(View.GONE);
        }
        if (!appCMSPresenter.isPageSearch(updatedAppCMSBinder.getScreenName())) {
            search_layout.setVisibility(View.GONE);
        }
        if (mSearchTopButton.getVisibility() == View.VISIBLE &&
                (appCMSPresenter.isPageLoginPage(updatedAppCMSBinder.getPageId()))
                || updatedAppCMSBinder.getExtraScreenType() == AppCMSPresenter.ExtraScreenType.RESET_PASSWORD
                || updatedAppCMSBinder.getExtraScreenType() == AppCMSPresenter.ExtraScreenType.NAVIGATION
                || updatedAppCMSBinder.getExtraScreenType() == AppCMSPresenter.ExtraScreenType.TV_PROVIDER_SCREEN
                || updatedAppCMSBinder.getExtraScreenType() == AppCMSPresenter.ExtraScreenType.WEB_VIEW_SCREEN) {
            mSearchTopButton.setVisibility(View.GONE);
        }
    }

    @SuppressWarnings("ConstantConditions")
    private void handleLaunchPageAction(final AppCMSBinder appCMSBinder,
                                        boolean configurationChanged,
                                        boolean leavingExtraPage,
                                        boolean keepPage) {
        if (!appCMSPresenter.isFullScreenVisible)
            appCMSPresenter.setAppOrientation();
        if (appCMSBinder.getScreenName() != null) {
            appCMSPresenter.sendGaScreen(appCMSBinder.getScreenName());
        }
        setVisibilityForStartFreeTrial(appCMSBinder.getPageId());
        int lastBackStackEntry = getSupportFragmentManager().getBackStackEntryCount();
        boolean poppedStack = false;
        if (!appCMSBinder.shouldSendCloseAction() &&
                lastBackStackEntry > 0 &&
                (appCMSBinder.getPageId() + BaseView.isLandscape(this))
                        .equals(getSupportFragmentManager()
                                .getBackStackEntryAt(lastBackStackEntry - 1)
                                .getName()) &&
                getSupportFragmentManager().findFragmentByTag(appCMSBinder.getPageId()
                        + BaseView.isLandscape(this)) instanceof AppCMSPageFragment) {
            ((AppCMSPageFragment) getSupportFragmentManager().findFragmentByTag(appCMSBinder.getPageId()
                    + BaseView.isLandscape(this))).refreshView(appCMSBinder);
            if ((appCMSBinder.getAppCMSPageAPI() != null || appCMSBinder.getExtraScreenType()
                    != AppCMSPresenter.ExtraScreenType.NONE)) {
                pageLoading(false);
            } else {
                pageLoading(true);
            }
            isFromLogin = false;
            appCMSBinderMap.put(appCMSBinder.getPageId(), appCMSBinder);
            if (appCMSBinder.getAppCMSPageAPI() == null)
                refreshPageData();
            try {
                handleToolbar(appCMSBinder.isAppbarPresent(),
                        appCMSBinder.getAppCMSMain(),
                        appCMSBinder.getPageId());
                handleNavbar(appCMSBinder);
                updatedAppCMSBinder = appCMSBinderMap.get(appCMSBinderStack.peek());
                appCMSPresenter.showMainFragmentView(true);
                appCMSPresenter.restartInternalEvents();
                appCMSPresenter.dismissOpenDialogs(null);
                showEntitlementOptions(updatedAppCMSBinder);
            } catch (EmptyStackException e) {
                Log.e(TAG, "Error attempting to restart screen: " + appCMSBinder.getScreenName());
            }
        } else {
            boolean createFragment = true;

            int distanceFromStackTop = appCMSBinderStack.search(appCMSBinder.getPageId());
            //Log.d(TAG, "Page distance from top: " + distanceFromStackTop);
            int i = 1;
            while (((i < distanceFromStackTop && !configurationChanged) ||
                    ((i < distanceFromStackTop &&
                            (!isBinderStackEmpty() &&
                                    !isBinderStackTopNull() &&
                                    !atMostOneUserPageOnTopStack(appCMSBinder.getPageId()) &&
                                    !leavingExtraPage)) &&
                            ((!leavingExtraPage && shouldPopStack(appCMSBinder.getPageId())) || configurationChanged)) ||
                    (appCMSBinder.shouldSendCloseAction() &&
                            appCMSBinderStack.size() > 1 &&
                            i < appCMSBinderStack.size()))) {
                //Log.d(TAG, "Popping stack to getList to page item");
                try {
                    getSupportFragmentManager().popBackStackImmediate();
                    createFragment = false;
                } catch (NullPointerException | IllegalStateException e) {
                    Log.e(TAG, "DialogType popping back stack: " + e.getMessage());
                }
                if ((i < distanceFromStackTop - 1) ||
                        (!configurationChanged && !atMostOneUserPageOnTopStack(appCMSBinder.getPageId()))) {
                    handleBack(true,
                            false,
                            false,
                            !appCMSBinder.shouldSendCloseAction());
                    poppedStack = true;
                }
                i++;
            }
            if (!appCMSBinderStack.isEmpty()) {
                AppCMSBinder currentAppCMSBinder = appCMSBinderMap.get(appCMSBinderStack.peek());
                try {
                    createFragment = currentAppCMSBinder.getExtraScreenType() != AppCMSPresenter.ExtraScreenType.SEARCH;
                } catch (Exception e) {
                    //
                    Log.e(TAG, "" + e.toString());

                }
            }

            if (!appCMSBinderStack.isEmpty() && appCMSBinderMap.get(appCMSBinderStack.peek()) != null) {
                try {
                    createFragment = !((appCMSBinderMap.get(appCMSBinderStack.peek())
                            .getExtraScreenType() == AppCMSPresenter.ExtraScreenType.SEARCH
                            && updatedAppCMSBinder.getExtraScreenType() == AppCMSPresenter.ExtraScreenType.SEARCH)
                            || (appCMSBinderMap.get(appCMSBinderStack.peek())
                            .getExtraScreenType() == AppCMSPresenter.ExtraScreenType.PARENTAL_CONTROLS
                            && updatedAppCMSBinder.getExtraScreenType() == AppCMSPresenter.ExtraScreenType.PARENTAL_CONTROLS));
                } catch (Exception e) {
                    Log.e(TAG, "" + e.toString());
                }
            }

            if (distanceFromStackTop < 0 ||
                    appCMSBinder.shouldSendCloseAction() ||
                    (!configurationChanged && appCMSBinder.getExtraScreenType() !=
                            AppCMSPresenter.ExtraScreenType.NONE)) {
                if (!isBinderStackEmpty() &&
                        !isBinderStackTopNull() &&
                        (appCMSPresenter.isPageNavigationPage(appCMSBinderStack.peek()) ||
                                isDownloadPageOpen) &&
                        appCMSPresenter.isPagePrimary(appCMSBinder.getPageId())) {
                    try {
                        getSupportFragmentManager().popBackStackImmediate();
                    } catch (NullPointerException | IllegalStateException e) {
                        //Log.e(TAG, "DialogType popping back stack: " + e.getMessage());
                    }
                    appCMSBinderMap.remove(appCMSBinderStack.peek());
                    appCMSBinderStack.pop();
                    isDownloadPageOpen = false;
                }

                if (appCMSBinderStack.search(appCMSBinder.getPageId()) < 0) {
                    appCMSBinderStack.push(appCMSBinder.getPageId());
                }
                appCMSBinderMap.put(appCMSBinder.getPageId(), appCMSBinder);
            }

            if (distanceFromStackTop >= 0) {
                try {
                    switch (appCMSBinder.getExtraScreenType()) {
                        case NAVIGATION:
                            createFragment = true;
                            break;
                        case TEAM:
                        case SEARCH:
                            //Log.d(TAG, "Popping stack to getList to page item");
                            try {
                                createFragment = false;
                                if (!isBinderStackEmpty() &&
                                        !isBinderStackTopNull() &&
                                        appCMSBinderStack.peek().equals(appCMSBinder.getPageId()) &&
                                        !keepPage) {
                                    try {
                                        getSupportFragmentManager().popBackStackImmediate();
                                    } catch (NullPointerException | IllegalStateException e) {
                                        //Log.e(TAG, "DialogType popping back stack: " + e.getMessage());
                                    }
                                    createFragment = true;
                                }

                                if (poppedStack) {
                                    appCMSBinderStack.push(appCMSBinder.getPageId());
                                    appCMSBinderMap.put(appCMSBinder.getPageId(), appCMSBinder);
                                }

                                if (!createFragment) {
                                    handleToolbar(appCMSBinder.isAppbarPresent(),
                                            appCMSBinder.getAppCMSMain(),
                                            appCMSBinder.getPageId());
                                    handleNavbar(appCMSBinder);
                                }
                            } catch (IllegalStateException e) {
                                Log.e(TAG, "DialogType popping back stack: " + e.getMessage());
                            }
                            break;
                        case NONE:
                            if (poppedStack) {
                                if (appCMSBinderStack.search(appCMSBinder.getPageId()) < 0) {
                                    appCMSBinderStack.push(appCMSBinder.getPageId());
                                }
                                appCMSBinderMap.put(appCMSBinder.getPageId(), appCMSBinder);
                            }
                            break;

                        default:
                            break;
                    }
                } catch (Exception e) {
                    Log.e(TAG, "" + e.toString());

                }
            }

            appCMSBinder.unsetSendCloseAction();

            updatedAppCMSBinder = appCMSBinderMap.get(appCMSBinderStack.peek());

            if (mAppUpdateHelper != null && (appCMSPresenter.isAppBelowMinVersion() || appCMSPresenter.isAppUpgradeAvailable())) {
                mAppUpdateHelper.checkAppUpgradeAvailable();
            }

            if (appCMSPresenter.isAppBelowMinVersion() && BuildConfig.FLAVOR.equalsIgnoreCase(AppCMSPresenter.MOBILE_BUILD_VARIENT)) {
                appCMSPresenter.launchUpgradeAppActivity();
            } else if (appCMSPresenter.isAppUpgradeAvailable() && BuildConfig.FLAVOR.equalsIgnoreCase(AppCMSPresenter.MOBILE_BUILD_VARIENT)) {
                newVersionUpgradeAvailable.getLayoutParams().height = ViewGroup.LayoutParams.WRAP_CONTENT;
                newVersionAvailableTextView.setText("");
                newVersionAvailableTextView.setText(appCMSPresenter.getLanguageResourcesFile().getStringValue(getString(R.string.a_new_version_of_the_app_is_available_text),
                        getString(R.string.app_cms_app_version),
                        Utils.getProperty("AppName", this)));
                newVersionUpgradeAvailable.setVisibility(View.VISIBLE);
                newVersionUpgradeAvailable.requestLayout();
            }

            if (createFragment) {
                createScreenFromAppCMSBinder(appCMSBinder);
            } else {
                int lastFragment = getSupportFragmentManager().getFragments().size();
                Fragment fragment = getSupportFragmentManager().getFragments().get(lastFragment - 1);
                if (fragment instanceof AppCMSPageFragment) {
                    ((AppCMSPageFragment) fragment).refreshView(appCMSBinder);
                }
                if (appCMSBinder.getAppCMSPageAPI() != null || appCMSBinder.getExtraScreenType()
                        != AppCMSPresenter.ExtraScreenType.NONE) {
                    pageLoading(false);
                } else {
                    pageLoading(true);
                }
                handleToolbar(appCMSBinder.isAppbarPresent(),
                        appCMSBinder.getAppCMSMain(),
                        appCMSBinder.getPageId());
            }
        }

        if (appCMSBinder.getExtraScreenType() == AppCMSPresenter.ExtraScreenType.SEARCH) {
            mSearchTopButton.setVisibility(View.GONE);
        } else if (shouldShowSearchInToolbar()) {
            mSearchTopButton.setVisibility(View.VISIBLE);
        }
        if (appCMSBinder != null && appCMSBinder.getAppCMSPageUI() != null && appCMSBinder.getAppCMSPageUI().getModuleList() != null) {
            getLeftNavItem(appCMSBinder.getAppCMSPageUI().getModuleList());
        }

    }

    private void hideSystemUI(View decorView) {
        decorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION |
                        View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN |
                        View.SYSTEM_UI_FLAG_HIDE_NAVIGATION |
                        View.SYSTEM_UI_FLAG_FULLSCREEN |
                        View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY |
                        View.SYSTEM_UI_FLAG_LOW_PROFILE);
    }

    private void showSystemUI(View decorView) {
        decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (Utils.isColorDark(appCMSPresenter.getGeneralBackgroundColor())) {
                decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            } else {
                decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            }
        }
    }

    private void manageTopBar() {
        if (appCMSPresenter.getNavigation() != null && appCMSPresenter.getNavigation().getLeft() != null && appCMSPresenter.getNavigation().getLeft().size() > 0) {
            for (int i = 0; i < appCMSPresenter.getNavigation().getLeft().size(); i++) {
                if (appCMSPresenter.getNavigation().getLeft().get(i).getDisplayedPath().equalsIgnoreCase("Authentication Screen")) {
                    mProfileTopButton.setVisibility(View.VISIBLE);
                }
            }
        }
        if (appCMSPresenter.getTemplateType() == AppCMSPresenter.TemplateType.SPORTS)
            mProfileTopButton.setVisibility(View.VISIBLE);
        else
            mProfileTopButton.setVisibility(View.GONE);
        if (appCMSPresenter.getNavigation() != null
                && appCMSPresenter.getNavigation().getRight() != null &&
                appCMSPresenter.getNavigation().getRight().size() > 0) {
            for (int i = 0; i < appCMSPresenter.getNavigation().getRight().size(); i++) {
                if (appCMSPresenter.getNavigation().getRight().get(i).getDisplayedPath() != null && appCMSPresenter.getNavigation().getRight().get(i).getDisplayedPath().equalsIgnoreCase("Search Screen")) {
                    mSearchTopButton.setVisibility(View.VISIBLE);
                }
            }
        }
    }

    private void createTabBar() {
        ModuleList tabBarModule = appCMSPresenter.getTabBarUIFooterModule();
        if (appCMSPresenter.getNavigation() != null &&
                appCMSPresenter.getNavigation().getTabBar() != null &&
                !isTabCreated && tabBarModule != null) {
            isTabCreated = true;
            int WEIGHT_SUM = getResources().getInteger(R.integer.nav_bar_items_weightsum);
            int weight = WEIGHT_SUM / appCMSPresenter.getNavigation().getTabBar().size();

            appCMSTabNavContainer.removeAllViews();

            //add separator view
            if (tabBarModule.isTabSeparator()) {
                View sepratorView = new View(this);
                sepratorView.setLayoutParams(new LinearLayoutCompat.LayoutParams(LinearLayoutCompat.LayoutParams.MATCH_PARENT, (int) BaseView.convertDpToPixel(getResources().getDimension(R.dimen.nav_item_separator_height), this)));
                sepratorView.setBackgroundColor(Color.parseColor(tabBarModule.getTabSeparator_color()));
                appCMSTabNavContainer.addView(sepratorView);
            }

            //add navigation item parent view
            appCMSTabNavContainerItems = new LinearLayoutCompat(this);
            LinearLayoutCompat.LayoutParams appCMSTabNavContainerItemsParam = new LinearLayoutCompat.LayoutParams(LinearLayoutCompat.LayoutParams.WRAP_CONTENT, LinearLayoutCompat.LayoutParams.MATCH_PARENT);
           /* if (BaseView.isTablet(this)){

                weight=(weight/2);
            }else{
                appCMSTabNavContainerItems.setWeightSum(WEIGHT_SUM);
            }*/

            appCMSTabNavContainerItemsParam.gravity = Gravity.CENTER;

            appCMSTabNavContainerItems.setLayoutParams(appCMSTabNavContainerItemsParam);
            appCMSTabNavContainerItems.setOrientation(LinearLayoutCompat.HORIZONTAL);
            appCMSTabNavContainer.addView(appCMSTabNavContainerItems);

            if (!appCMSPresenter.isNetworkConnected()) {
                // leftNavId = "a6c5a098-3267-4fe1-a70d-db304514123d";
                if (appPreference.getleftnavigationKey() != null)
                    leftNavId = appPreference.getleftnavigationKey();
            }


            if (bottomTabHeader != null && !bottomTabHeader.equals("")) {
                if (appCMSPresenter.isHeaderNavExist()) {
                    for (int i = 0; i < appCMSPresenter.getAppCMSAndroid().getHeaders().size(); i++) {
                        if (appCMSPresenter.getAppCMSAndroid().getHeaders().get(i).isType()) {
                            int headerBottomBarItemsCount = Math.min(appCMSPresenter.getAppCMSAndroid().getHeaders().get(i).getData().size(), 5);
                            for (int j = 0; j < headerBottomBarItemsCount; j++) {
                                NavigationPrimary navigationItem = appCMSPresenter.getAppCMSAndroid().getHeaders().get(i).getData().get(j);
                                String navigationTitle = appCMSPresenter.getNavigationTitle(navigationItem.getLocalizationMap());
                                navBarItemView = new NavBarItemView(this, tabBarModule, appCMSPresenter, weight);
                                int highlightColor = ContextCompat.getColor(this, R.color.colorNavBarText);
                                if (appCMSPresenter.getAppCMSMain() != null && appCMSPresenter.getAppCMSMain().getBrand() != null && appCMSPresenter.getAppCtaBackgroundColor() != null) {
                                    highlightColor = Color.parseColor(appCMSPresenter.getAppCtaBackgroundColor());
                                }
                                String tabLabel = navigationTitle != null ? navigationTitle : navigationItem.getTitle();
                                navBarItemView.setTabImage(navigationItem.getIcon());
                                navBarItemView.setLabel(tabLabel);
                                navBarItemView.setHighlightColor(highlightColor);
                                navBarItemView.setId(i);


                                String tagId = null;
                                if (navigationItem.getPageId() != null && !TextUtils.isEmpty(navigationItem.getPageId())) {
                                    tagId = navigationItem.getPageId();
                                } else if (navigationItem.getDisplayedPath() != null) {
                                    tagId = navigationItem.getDisplayedPath();
                                } else if (navigationItem.getTitle() != null) {
                                    tagId = navigationItem.getTitle();
                                }

                                NavTabTag navigationTag = new NavTabTag();
                                navigationTag.setPageId(tagId);
                                navigationTag.setNavigationTabBar(navigationItem);
                                navigationTag.setNavigationModuleItem(tabBarModule);
                                navBarItemView.setTag(navigationTag);

                                navBarItemView.setOnClickListener(v -> {

                                    if (v.getTag() != null) {
                                        NavTabTag navigationTabTag = (NavTabTag) v.getTag();
                                        if (navigationTabTag.isTabSelected()) {
                                            return;
                                        }
                                        selectNavItem(navigationTabTag.getPageId());
                                        selectNavItem((NavBarItemView) v);
                                        appCMSPresenter.showMainFragmentView(true);
                                        if (navigationTabTag.getPageId().equals("Menu Screen") || navigationTabTag.getPageId().contains("Menu") ||
                                                navigationTabTag.getPageId().contains("Account Settings")) {
                                            if (navigationTabTag.getPageId().contains("Menu Screen") || navigationTabTag.getPageId().contains("Menu")) {
                                                if (navigationTabTag.getPageId().contains("Menu Screen") || navigationTabTag.getPageId().contains("Menu")) {
                                                    //appCMSHeaderImage.setImageResource(R.drawable.menu_);

//                                                    if (navigationTabTag.getTabBar().getIcon() != null) {
//                                                        Glide.with(getApplicationContext()).load(navigationTabTag.getTabBar().getIcon())// resizes the image to these dimensions (in pixel)
//                                                                .fitCenter().
//                                                                into(appCMSHeaderImage);
//                                                    }


                                                }
                                            }
                                            appCMSPresenter.launchNavigationPage();
                                        } else if (navigationTabTag.getTabBar().getDisplayedPath().contains("Ftf") ||
                                                navigationTabTag.getTabBar().getDisplayedPath().contains("LSN") ||
                                                navigationTabTag.getTabBar().getDisplayedPath().contains("ECAC") ||
                                                navigationTabTag.getTabBar().getDisplayedPath().contains("Home")) {
                                            /**
                                             * Need to set logic for open
                                             */

                                            if (appCMSLeftDrawerLayout.isDrawerOpen(GravityCompat.START)) {
                                                appCMSLeftDrawerLayout.closeDrawer(Gravity.LEFT, false);
                                                appCMSLeftDrawerLayout.closeDrawers();
                                                //appCMSParentLayout.setVisibility(View.VISIBLE);

                                            }
                                            appCMSPresenter.navigateToPage(navigationTabTag.getTabBar().getPageId(),
                                                    navigationTabTag.getTabBar().getDisplayedPath(),
                                                    navigationTabTag.getTabBar().getUrl(),
                                                    false,
                                                    true,
                                                    false,
                                                    true,
                                                    false,
                                                    null);


                                            try {
                                                if (navigationTabTag.getTabBar().getIcon() != null) {
                                                    Glide.with(getApplicationContext()).load(navigationTabTag.getTabBar().getIcon())// resizes the image to these dimensions (in pixel)
                                                            .fitCenter().
                                                            into(appCMSHeaderImage);
                                                } else {
                                                    appCMSHeaderImage.setImageResource(R.drawable.logo_icon);
                                                }
//
                                            } catch (Exception ex) {
                                                ex.printStackTrace();
                                            }


                                        } else if (navigationTabTag.getPageId().equalsIgnoreCase("TEAMS") ||
                                                appCMSPresenter.getPageFunctionValue(navigationTabTag.getTabBar().getPageId(), navigationTabTag.getTabBar().getTitle()).equalsIgnoreCase("TEAMS") || appCMSPresenter.getPageFunctionValue(navigationTabTag.getTabBar().getPageId(), navigationTabTag.getTabBar().getTitle()).equalsIgnoreCase("TEAM")) {
                                            appCMSPresenter.launchTeamNavPage();
                                        } else if (navigationTabTag.getPageId().equals("Search Screen") || (
                                                navigationTabTag.getTabBar() != null && navigationTabTag.getTabBar().getDisplayedPath() != null && navigationTabTag.getTabBar().getDisplayedPath().equals("Search Screen"))) {
                                            appCMSPresenter.launchSearchPage();
                                        } else if (navigationTabTag.getTabBar().getDisplayedPath() != null && navigationTabTag.getTabBar().getItems() != null && navigationTabTag.getTabBar().getItems().size() > 0) {
                                            if (navigationTabTag.getTabBar().getDisplayedPath().equals("Sub Navigation Screen")) {
                                                appCMSPresenter.navigateToDisplayPage(navigationTabTag.getTabBar().getPageId(),
                                                        navigationTabTag.getTabBar().getTitle(),
                                                        navigationTabTag.getTabBar().getUrl(),
                                                        false,
                                                        true,
                                                        false,
                                                        true,
                                                        false,
                                                        null, navigationTabTag.getTabBar().getItems());
                                            } else if (navigationTabTag.getTabBar().isOpenInChromeCustomTab() && !TextUtils.isEmpty(navigationTabTag.getTabBar().getUrl())) {
                                                String url = navigationTabTag.getTabBar().getUrl();
                                                if (!url.startsWith(getString(R.string.https_scheme))) {
                                                    url = appCMSPresenter.getAppCMSMain().getDomainName() + navigationTabTag.getTabBar().getUrl() + "?app=true";
                                                }
                                                appCMSPresenter.openChromeTab(url);
                                            } else if (!TextUtils.isEmpty(navigationTabTag.getPageId()))
                                                selectNavItemAndLaunchPage(navBarItemView,
                                                        appCMSPresenter.getNavigation().getTabBar().get(v.getId()).getPageId(),
                                                        appCMSPresenter.getNavigation().getTabBar().get(v.getId()).getTitle());
                                        } else if (!TextUtils.isEmpty(navigationTabTag.getPageId())) {
                                            selectNavItemAndLaunchPage(navBarItemView,
                                                    appCMSPresenter.getNavigation().getTabBar().get(v.getId()).getPageId(),
                                                    appCMSPresenter.getNavigation().getTabBar().get(v.getId()).getTitle());
                                        }
                                    }
                                });

                                appCMSTabNavContainerItems.addView(navBarItemView);


                            }
                            break;
                        }
                    }
                }
            } else {
                appCMSHeaderImage.setImageResource(R.drawable.logo_icon);
                int bottomBarItemsCount = Math.min(appCMSPresenter.getNavigation().getTabBar().size(), 5);
                for (int i = 0; i < bottomBarItemsCount; i++) {
                    NavigationPrimary navigationItem = appCMSPresenter.getNavigation().getTabBar().get(i);
                    String navigationTitle = appCMSPresenter.getNavigationTitle(navigationItem.getLocalizationMap());

                    navBarItemView = new NavBarItemView(this, tabBarModule, appCMSPresenter, weight);
                    String tabLabel = /*navigationFooter.getTitle().toUpperCase()*/navigationTitle != null ? navigationTitle : navigationItem.getTitle();
                    //navBarItemView.setTabImage("https://snagfilms-a.akamaihd.net/17/e2/878c6abc4eeb8bb81507b58299f9/1523631777051_byomkesh_s03_ep_02_1.0000000.jpg");
                    navBarItemView.setTabImage(navigationItem.getIcon());
                    navBarItemView.setLabel(tabLabel);
                    navBarItemView.setHighlightColor(appCMSPresenter.getBrandPrimaryCtaTextColor());
                    navBarItemView.setId(i);

                    String tagId = null;
                    if (navigationItem.getPageId() != null && !TextUtils.isEmpty(navigationItem.getPageId())) {
                        tagId = navigationItem.getPageId();
                    } else if (navigationItem.getDisplayedPath() != null) {
                        tagId = navigationItem.getDisplayedPath();
                    } else if (navigationItem.getTitle() != null) {
                        tagId = navigationItem.getTitle();
                    }

                    NavTabTag navigationTag = new NavTabTag();
                    navigationTag.setPageId(tagId);
                    navigationTag.setNavigationTabBar(navigationItem);
                    navigationTag.setNavigationModuleItem(tabBarModule);
                    navBarItemView.setTag(navigationTag);

                    navBarItemView.setOnClickListener(v -> {
                        if (v.getTag() != null) {
                            NavTabTag navigationTabTag = (NavTabTag) v.getTag();
                            if (navigationTabTag.isTabSelected()) {
                                return;
                            }
                            if(!(!appCMSPresenter.isUserLoggedIn()&&navigationTabTag!=null &&navigationTabTag.navigationTabBar!=null&&
                            navigationTabTag.navigationTabBar.getTitle()!=null&&navigationTabTag.navigationTabBar.getTitle().equalsIgnoreCase("Saved"))) {
                                selectNavItem(navigationTabTag.getPageId());
                                selectNavItem((NavBarItemView) v);
                            }
                            appCMSPresenter.showMainFragmentView(true);
                            if (navigationTabTag.getPageId().equals("Menu Screen") || navigationTabTag.getPageId().contains("Menu")) {
                                appCMSPresenter.launchNavigationPage();
                            } else if (navigationTabTag.getPageId().equalsIgnoreCase("TEAMS") ||
                                    appCMSPresenter.getPageFunctionValue(navigationTabTag.getTabBar().getPageId(), navigationTabTag.getTabBar().getTitle()).equalsIgnoreCase("TEAMS") || appCMSPresenter.getPageFunctionValue(navigationTabTag.getTabBar().getPageId(), navigationTabTag.getTabBar().getTitle()).equalsIgnoreCase("TEAM")) {
                                appCMSPresenter.launchTeamNavPage();
                            } else if (navigationTabTag.getPageId().equals("Search Screen") || (
                                    navigationTabTag.getTabBar() != null && navigationTabTag.getTabBar().getDisplayedPath() != null && navigationTabTag.getTabBar().getDisplayedPath().equals("Search Screen"))) {
//                            appCMSPresenter.launchSearchPage();
                                openSearchPage(true, true);
                            } else if (navigationTabTag.getTabBar() != null && navigationTabTag.getTabBar().getDisplayedPath() != null && appCMSPresenter.isWatchlistPage(navigationTabTag.getTabBar().getPageId())) {
                                if (!appCMSPresenter.isUserLoggedIn()) {
                                    appCMSPresenter.setAppbarPresent(false);
                                    appCMSPresenter.showEntitlementDialog(AppCMSPresenter.DialogType.LOGIN_REQUIRED,
                                            null, null);
                                    return;
                                }
                                pageLoading(true);
                                appCMSPresenter.navigateToWatchlistPage(navigationTabTag.getTabBar().getPageId(),
                                        navigationTabTag.getTabBar().getTitle(), navigationTabTag.getTabBar().getUrl(), true, true, false);
                            } else if (navigationTabTag.getTabBar().getDisplayedPath() != null && navigationTabTag.getTabBar().getItems() != null && navigationTabTag.getTabBar().getItems().size() > 0) {
                                if (navigationTabTag.getTabBar().getDisplayedPath().equals("Sub Navigation Screen")) {
                                    appCMSPresenter.navigateToDisplayPage(navigationTabTag.getTabBar().getPageId(),
                                            navigationTabTag.getTabBar().getTitle(),
                                            navigationTabTag.getTabBar().getUrl(),
                                            false,
                                            true,
                                            false,
                                            true,
                                            false,
                                            null, navigationTabTag.getTabBar().getItems());
                                } else if (navigationTabTag.getTabBar().isOpenInChromeCustomTab() && !TextUtils.isEmpty(navigationTabTag.getTabBar().getUrl())) {
                                    String url = navigationTabTag.getTabBar().getUrl();
                                    if (!url.startsWith(getString(R.string.https_scheme))) {
                                        url = appCMSPresenter.getAppCMSMain().getDomainName() + navigationTabTag.getTabBar().getUrl() + "?app=true";
                                    }
                                    appCMSPresenter.openChromeTab(url);
                                } else if (!TextUtils.isEmpty(navigationTabTag.getPageId()))
                                    selectNavItemAndLaunchPage(navBarItemView,
                                            appCMSPresenter.getNavigation().getTabBar().get(v.getId()).getPageId(),
                                            appCMSPresenter.getNavigation().getTabBar().get(v.getId()).getTitle());
                            } else if (!TextUtils.isEmpty(navigationTabTag.getPageId())) {
                                selectNavItemAndLaunchPage(navBarItemView,
                                        appCMSPresenter.getNavigation().getTabBar().get(v.getId()).getPageId(),
                                        appCMSPresenter.getNavigation().getTabBar().get(v.getId()).getTitle());
                            }
                        }
                    });
                    appCMSTabNavContainerItems.addView(navBarItemView);
                }
            }
        }
        setSubscribeBtnOnHeader();
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        this.isGoogleApiConnected = true;
        Log.d(TAG, "onConnected() called with: bundle = [" + bundle + "]");
    }

    @Override
    public void onConnectionSuspended(int i) {
        this.isGoogleApiConnected = false;
        Log.d(TAG, "onConnectionSuspended() called with: i = [" + i + "]");
    }

    @Override
    public void onPurchasesUpdated(BillingResult billingResult, @Nullable List<Purchase> list) {
        // System.out.println("onPurchasesUpdated   in page activity ");
        if (appCMSPresenter.getLaunchType() != AppCMSPresenter.LaunchType.TVOD_PURCHASE) {
            if (list != null && !list.isEmpty() && list.get(0) != null) {
                //   System.out.println("onPurchasesUpdated   in page activity "+list.get(0).getOriginalJson());
                appCMSPresenter.finalizeSignupAfterSubscription(list.get(0).getOriginalJson());
            } else if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.USER_CANCELED) {
                //if (!TextUtils.isEmpty(appPreference.getActiveSubscriptionSku())) {
                appCMSPresenter.showConfirmCancelSubscriptionDialog(retry -> {
                    if (retry) {
                        appCMSPresenter.initiateItemPurchase(false);
                    } else {
                        appCMSPresenter.sendCloseOthersAction(null, true, false);
                        appCMSPresenter.showPersonalizationscreenIfEnabled(false, true);
                        /*if (appCMSPresenter.isPersonalizationEnabled()) {
                            appCMSPresenter.showLoader();
                            AppCMSPresenter.isFromSettings = false;
                            appCMSPresenter.getUserRecommendedGenres(appCMSPresenter.getLoggedInUser(), s -> {
                                appCMSPresenter.setSelectedGenreString(s);
                                //if (isRecommendationEnabled()) {
                                Handler handler = new Handler();
                                handler.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        try {
                                            appCMSPresenter.stopLoader();
                                            appCMSPresenter.showRecommendationGenreDialog(null);
                                        } catch (Exception e) {
                                        }
                                    }
                                }, 1000);
                                //}
                            }, false);
                        }*/
                    }
                });
                //}
            } else {
                BillingHelper.getInstance(appCMSPresenter).showErrorDialog(billingResult.getResponseCode(), false);
            }
        }
    }

    public class NavTabTag {
        public String getPageId() {
            return pageId;
        }

        public void setPageId(String pageId) {
            this.pageId = pageId;
        }

        public NavigationPrimary getTabBar() {
            return navigationTabBar;
        }

        public void setNavigationTabBar(NavigationPrimary navigationTabBar) {
            this.navigationTabBar = navigationTabBar;
        }

        private String pageId;
        private NavigationPrimary navigationTabBar;

        public boolean isTabSelected() {
            return isTabSelected;
        }

        public void setTabSelected(boolean tabSelected) {
            isTabSelected = tabSelected;
        }

        private boolean isTabSelected;

        public ModuleList getNavigationModuleItem() {
            return navigationModuleItem;
        }

        public void setNavigationModuleItem(ModuleList navigationModuleItem) {
            this.navigationModuleItem = navigationModuleItem;
        }

        private ModuleList navigationModuleItem;

    }

    public void selectNavItem(String pageId) {
        boolean foundPage = false;
        if (!TextUtils.isEmpty(pageId) && appCMSTabNavContainerItems != null) {
            for (int i = 0; i < appCMSTabNavContainerItems.getChildCount(); i++) {
                NavTabTag navigationTabTag = null;
                if (appCMSTabNavContainerItems.getChildAt(i).getTag() != null) {
                    navigationTabTag = (NavTabTag) appCMSTabNavContainerItems.getChildAt(i).getTag();
                }

                if (navigationTabTag != null && !TextUtils.isEmpty(navigationTabTag.getPageId()) && (pageId.contains(navigationTabTag.getPageId()) || pageId.equalsIgnoreCase(navigationTabTag.getPageId()) || pageId.equalsIgnoreCase(getString(R.string.app_cms_menu_screen_tag)) || navigationTabTag.getPageId() != null && pageId.equalsIgnoreCase("navigation") && navigationTabTag.getPageId().equals(getString(R.string.app_cms_menu_screen_tag)) || pageId.equalsIgnoreCase(getString(R.string.app_cms_team_page_tag)) && appCMSPresenter.getPageFunctionValue(navigationTabTag.getTabBar().getTitle(), navigationTabTag.getTabBar().getTitle()).equalsIgnoreCase(getString(R.string.app_cms_team_page_tag)))) {
                    /**
                     * Handling header logo change for FTF type of case
                     */
                    if (appCMSPresenter.isHeaderNavExist() &&
                            navigationTabTag.getTabBar().getIcon() != null &&
                            CommonUtils.checkURL(navigationTabTag.getTabBar().getIcon())) {
                        Glide.with(getApplicationContext()).load(navigationTabTag.getTabBar().getIcon())// resizes the image to these dimensions (in pixel)
                                .fitCenter().
                                into(appCMSHeaderImage);
                    } else {
                        appCMSHeaderImage.setImageResource(R.drawable.logo_icon);
                    }

                    selectNavItem(((NavBarItemView) appCMSTabNavContainerItems.getChildAt(i)));
                    navigationTabTag.setTabSelected(true);
                    foundPage = true;
                } else {
                    navigationTabTag.setTabSelected(false);
                }
            }
        }
    }

    boolean isDeepLink = false;

    public void processDeepLink(Uri deeplinkUri) {
        isDeepLink = true;
        new Handler(getMainLooper()).postDelayed(() -> {
            isDeepLink = false;
            if (updatedAppCMSBinder != null) {
                handleNavbar(updatedAppCMSBinder);
            }
        }, 1000);
        String title = deeplinkUri.getLastPathSegment();
        String action = getString(R.string.app_cms_action_detailvideopage_key);
        StringBuilder pagePath = new StringBuilder();
        appCMSPresenter.checkQuery = true;
        if (appCMSPresenter.dialog != null && appCMSPresenter.dialog.isShowing()) {
            if (!appCMSPresenter.isUserLoggedIn() &&
                    (deeplinkUri.toString().contains(getString(R.string.watchlist_key_for_deeplink)) ||
                            deeplinkUri.toString().contains(getString(R.string.download_key_for_deeplink)) ||
                            deeplinkUri.toString().contains(getString(R.string.history_key_for_deeplink)) ||
                            deeplinkUri.toString().contains(getString(R.string.settings_key_for_deeplink)) ||
                            deeplinkUri.toString().contains(getString(R.string.account_key_for_deeplink)))) {
            } else
                appCMSPresenter.dialog.dismiss();
        }
        if (Strings.isEmptyOrWhitespace(title) || title.equalsIgnoreCase(getString(R.string.home_url))) {
            action = getString(R.string.app_cms_action_homepage_key);
        } else if (!Strings.isEmptyOrWhitespace(title) && !title.equalsIgnoreCase(getString(R.string.home_url))) {
//            if (!Strings.isEmptyOrWhitespace(title) && title.equalsIgnoreCase(getString(R.string.myaha_url)))
//                title = getString(R.string.my_aha_key_for_deeplink);
            int bottomTabCount = Math.min(appCMSPresenter.getNavigation().getTabBar().size(), 5);
            for (int i = 0; i < bottomTabCount; i++) {
                NavigationPrimary navigationItem = appCMSPresenter.getNavigation().getTabBar().get(i);
                if (navigationItem.getTitle().equalsIgnoreCase(title)) {
                    String pageId = navigationItem.pageId;
                    new Handler(getMainLooper()).postDelayed(() -> {
                        selectNavItemAndLaunchPage(navBarItemView, pageId, navigationItem.getTitle());
                    }, 1000);
                    return;
                }
            }
        }

        if (deeplinkUri.toString().contains(getString(R.string.view_plans).toLowerCase())) {
            if (appCMSPresenter.isUserSubscribed()) {
                runOnUiThread(() -> appCMSPresenter.showToast(localisedStrings.getAlreadySubscribed(), Toast.LENGTH_LONG));
                appCMSPresenter.resetDeeplinkQuery();
                return;
            } else {
                action = getString(R.string.app_cms_action_startfreetrial_key);
            }
        }
/*
        if (deeplinkUri.toString().contains(getString(R.string.refferal_plan))) {
            if (appCMSPresenter.isUserSubscribed()) {
                runOnUiThread(() -> appCMSPresenter.showToast(localisedStrings.getAlreadySubscribed(), Toast.LENGTH_LONG));
                appCMSPresenter.resetDeeplinkQuery();
                return;
            } else {
                action = getString(R.string.app_cms_action_referralPlans_key);
            }
        }

        if (GetSocialHelper.isInitialized() && GetSocialHelper.isGetSocialDeepLink(deeplinkUri)) {
            if (appCMSPresenter.isUserSubscribed()) {
                runOnUiThread(() -> appCMSPresenter.showToast(localisedStrings.getAlreadySubscribed(), Toast.LENGTH_LONG));
                appCMSPresenter.resetDeeplinkQuery();
                return;
            } else {
                action = getString(R.string.app_cms_action_referralPlans_key);
            }
        }
*/
        if (deeplinkUri.toString().contains(getString(R.string.signin_key_for_deeplink))) {
            if (!appCMSPresenter.isUserLoggedIn())
                action = getString(R.string.app_cms_action_signin_key);
            else {
                action = getString(R.string.app_cms_action_homepage_key);
                runOnUiThread(() -> appCMSPresenter.showToast(localisedStrings.getAlreadyLoggedInText(), Toast.LENGTH_LONG));

            }
        }
        if (deeplinkUri.toString().contains(getString(R.string.signup_key_for_deeplink))) {
            if (!appCMSPresenter.isUserLoggedIn()) {
                appCMSPresenter.setLaunchType(AppCMSPresenter.LaunchType.SIGNUP);
                appCMSPresenter.navigateToLoginPage(true);
            } else {
                action = getString(R.string.app_cms_action_homepage_key);
                runOnUiThread(() -> appCMSPresenter.showToast(localisedStrings.getAlreadyLoggedInText(), Toast.LENGTH_LONG));
            }
        }
        if (deeplinkUri.toString().contains(getString(R.string.download_key_for_deeplink))) {
            if (appCMSPresenter.isUserLoggedIn()) {
                appCMSPresenter.navigateToDownloadPage(appPreference.getDownloadPageId());
                return;
            } else {
                appCMSPresenter.showEntitlementDialog(AppCMSPresenter.DialogType.LOGIN_REQUIRED,
                        () -> {
                        }, null);
                return;
            }
        }
        if (deeplinkUri.toString().contains(getString(R.string.history_key_for_deeplink))) {
            if (appCMSPresenter.isUserLoggedIn()) {
                int size = appCMSPresenter.getNavigation().getNavigationUser().size();
                NavigationUser user;
                for (int i = 0; i < size; i++) {
                    user = appCMSPresenter.getNavigation().getNavigationUser().get(i);
                    if (deeplinkUri.toString().contains(user.getTitle().toLowerCase())) {
                        appCMSPresenter.navigateToHistoryPage(user.pageId, user.getTitle(), user.getUrl(), false, false);
                        return;
                    }
                }
            } else {
                appCMSPresenter.showEntitlementDialog(AppCMSPresenter.DialogType.LOGIN_REQUIRED,
                        () -> {
                        }, null);
                return;
            }
        }
        if (deeplinkUri.toString().contains(getString(R.string.watchlist_key_for_deeplink))) {
            if (appCMSPresenter.isUserLoggedIn()) {
                int size = appCMSPresenter.getNavigation().getNavigationUser().size();
                NavigationUser user;
                for (int i = 0; i < size; i++) {
                    user = appCMSPresenter.getNavigation().getNavigationUser().get(i);
                    if (deeplinkUri.toString().contains(user.getTitle().toLowerCase())) {
                        appCMSPresenter.navigateToWatchlistPage(user.pageId, user.getTitle(), user.getUrl(), false, false, false);
                        return;
                    }
                }
            } else {
                appCMSPresenter.showEntitlementDialog(AppCMSPresenter.DialogType.LOGIN_REQUIRED,
                        () -> {
                        }, null);
                return;

            }
        }
        if (deeplinkUri.toString().contains(getString(R.string.settings_key_for_deeplink)) ||
                deeplinkUri.toString().contains(getString(R.string.account_key_for_deeplink))) {
            if (appCMSPresenter.isUserLoggedIn()) {
                int size = appCMSPresenter.getNavigation().getNavigationUser().size();
                NavigationUser user;
                for (int i = 0; i < size; i++) {
                    user = appCMSPresenter.getNavigation().getNavigationUser().get(i);
                    if (deeplinkUri.toString().contains(user.getTitle().toLowerCase()) ||
                            user.getTitle().toLowerCase().contains(getString(R.string.account_key_for_deeplink))) {
                        refreshPageData();
                        appCMSPresenter.navigateToPage(user.pageId,
                                user.getTitle(),
                                user.getUrl(),
                                false,
                                false,
                                false,
                                false,
                                false,
                                null);
                        return;
                    }
                }
            } else {
                appCMSPresenter.showEntitlementDialog(AppCMSPresenter.DialogType.LOGIN_REQUIRED,
                        () -> {
                        }, null);
                return;

            }
        }
        if (deeplinkUri.toString().contains(getString(R.string.referral_key_for_deeplink))) {
            for (int i = 0; i < appCMSPresenter.getAppCMSAndroid().getMetaPages().size(); i++) {
                if (appCMSPresenter.getAppCMSAndroid().getMetaPages().get(i).getPageName().equalsIgnoreCase(getString(R.string.refer_earn_page_name_key_for_deeplink))) {
                    appCMSPresenter.navigateToPage(appCMSPresenter.getAppCMSAndroid().getMetaPages().get(i).getPageId(),
                            appCMSPresenter.getAppCMSAndroid().getMetaPages().get(i).getPageName(),
                            "",
                            false, true,
                            false,
                            true,
                            false,
                            null);
                    selectNavItem("Menu Screen");
                    appCMSPresenter.resetDeeplinkQuery();
                    updatedAppCMSBinder.clearSearchQuery();
                    return;
                }
            }
        }

        if (deeplinkUri.toString().contains(getString(R.string.genres_key_for_deeplink)) || deeplinkUri.toString().contains(getString(R.string.refer_earn_key_for_deeplink))) {
//            appCMSPresenter.getAppCMSAndroid().getMetaPages();
            NavigationPrimary navigationPrimary1;
            int bottomTabCount = Math.min(appCMSPresenter.getNavigation().getTabBar().size(), 5);
            if (appCMSPresenter.getAppCMSAndroid() != null && appCMSPresenter.getAppCMSAndroid().getHeaders() != null) {
                List<NavigationPrimary> data = appCMSPresenter.getAppCMSAndroid().getHeaders().get(0).getData();
                for (int i = 0; i < data.size(); i++) {
                    NavigationPrimary navigationPrimary = data.get(i);
                    if (navigationPrimary.getTitle().equalsIgnoreCase(getString(R.string.genres_key_for_deeplink))) {
                        if (navigationPrimary.getItems() != null && navigationPrimary.getItems().size() != 0) {
                            for (int j = 0; j < navigationPrimary.getItems().size(); j++) {
                                if (navigationPrimary.getItems().get(j).getTitle().toLowerCase().equalsIgnoreCase(title)) {
                                    navigationPrimary1 = navigationPrimary.getItems().get(j);
                                    appCMSPresenter.navigateToPage(navigationPrimary1.pageId,
                                            navigationPrimary1.getTitle(),
                                            navigationPrimary1.getUrl(),
                                            false, true,
                                            false,
                                            true,
                                            false,
                                            null);
                                    boolean flag = false;
                                    for (int k = 0; k < bottomTabCount; k++) {
                                        if (appCMSPresenter.getNavigation().getTabBar().get(k).getTitle().contains(getString(R.string.genres_key_for_deeplink))) {
                                            flag = true;
                                            break;
                                        }
                                    }
                                    if (!flag) {
                                        selectNavItem("Menu Screen");
                                        appCMSPresenter.showMainFragmentView(true);
                                    }
                                    return;
                                }
                            }
                        } else {
                            appCMSPresenter.navigateToPage(navigationPrimary.pageId,
                                    navigationPrimary.getTitle(),
                                    navigationPrimary.getUrl(),
                                    false, true,
                                    false,
                                    true,
                                    false,
                                    null);
                        }

                    }
                    if (navigationPrimary.getTitle().equalsIgnoreCase(getString(R.string.refer_earn_title_key_for_deeplink))) {
                        appCMSPresenter.navigateToPage(navigationPrimary.pageId,
                                navigationPrimary.getTitle(),
                                navigationPrimary.getUrl(),
                                false, true,
                                false,
                                true,
                                false,
                                null);
                    }
                }
            }
        }

        for (String pathSegment : deeplinkUri.getPathSegments()) {
            pagePath.append(File.separatorChar);
            pagePath.append(pathSegment);
        }
        if (pagePath.toString().startsWith(getString(R.string.series_key_for_deeplink)) || pagePath.toString().startsWith(getString(R.string.show)) || pagePath.toString().startsWith(getString(R.string.originals)))
            action = getString(R.string.app_cms_action_showvideopage_key);
//        if (pagePath.toString().contains(getString(R.string.referrals_key_for_deeplink))) {
//            action = getString(R.string.app_cms_action_referralPlans_key);
//        }

        if (pagePath.toString().contains(getString(R.string.app_cms_page_path_article))) {
            appCMSPresenter.setCurrentArticleIndex(-1);
            action = getString(R.string.app_cms_action_articlepage_key);
        }
        if (!Strings.isEmptyOrWhitespace(title) && appCMSPresenter.getNavigation().getNavigationFooter() != null) {
            for (int i = 0; i < appCMSPresenter.getNavigation().getNavigationFooter().size(); i++) {
                NavigationFooter navigationFooter = appCMSPresenter.getNavigation().getNavigationFooter().get(i);
                if (navigationFooter.getUrl().equalsIgnoreCase(title) || navigationFooter.getUrl().equalsIgnoreCase("/" + title)) {
                    appCMSPresenter.openFooterPage(navigationFooter, navigationFooter.getTitle());
                    return;
                }
            }
            for (int i = 0; i < appCMSPresenter.getNavigation().getNavigationPrimary().size(); i++) {
                NavigationPrimary navigationPrimary = appCMSPresenter.getNavigation().getNavigationPrimary().get(i);
                if (navigationPrimary.getUrl().equalsIgnoreCase(title) || navigationPrimary.getUrl().equalsIgnoreCase("/" + title)) {
                    appCMSPresenter.navigateToPage(navigationPrimary.getPageId(),
                            navigationPrimary.getTitle(),
                            navigationPrimary.getUrl(),
                            false,
                            true,
                            false,
                            true,
                            false,
                            null);
                    new Handler(getMainLooper()).postDelayed(() -> {
                        selectNavItem("Menu Screen");
                    }, 1000);

                    return;
                }
            }
        }
        if (pagePath.toString().contains(getString(R.string.app_cms_page_path_bundle))) {
            action = getString(R.string.app_cms_action_detailbundlepage_key);
        } else if (pagePath.toString().contains(getString(R.string.app_cms_page_path_photo_gallery)) ||
                pagePath.toString().contains(getString(R.string.app_cms_deep_link_path_photos))) {
            action = getString(R.string.app_cms_action_photo_gallerypage_key);
        } else if (pagePath.toString().contains(getString(R.string.app_cms_page_path_fighter)) ||
                pagePath.toString().contains(getString(R.string.app_cms_page_path_roster))) {
            appCMSPresenter.forceLoad();
            appCMSPresenter.navigateToPersonDetailsPage(pagePath.toString());
            appCMSPresenter.resetDeeplinkQuery();
            return;
        }
        appCMSPresenter.forceLoad();

        if (appCMSPresenter.getAppCMSMain().getFeatures() != null && appCMSPresenter.getAppCMSMain().getFeatures().isShowDetailOnlyOnApps()
                && (action.equalsIgnoreCase(getString(R.string.app_cms_action_detailvideopage_key)))) {
            String finalTitle = title;
            appCMSPresenter.deeplinkCallForShowDetailEpisode(action, pagePath.toString(), contentDatum -> {
                String permalink = pagePath.toString();
                String pageAction = getString(R.string.app_cms_action_detailvideopage_key);
                appCMSPresenter.setshowdetailsClickPostionDate(contentDatum);
                appCMSPresenter.setDefaultTrailerPlay(true);
                if (contentDatum.getSeriesData() != null) {
                    permalink = contentDatum.getSeriesData().get(0).getGist().getPermalink();
                    appCMSPresenter.setEpisodeId(contentDatum.getGist().getId());
                    if (contentDatum.getContentDetails() != null && contentDatum.getContentDetails().getTrailers() != null &&
                            contentDatum.getContentDetails().getTrailers().size() > 0 &&
                            contentDatum.getContentDetails().getTrailers().get(0).getId() != null) {
                        appCMSPresenter.setEpisodeTrailerID(contentDatum.getContentDetails().getTrailers().get(0).getId());
                    }
                    if (contentDatum.getContentDetails() != null && contentDatum.getContentDetails().getPromos() != null &&
                            contentDatum.getContentDetails().getPromos().size() > 0 &&
                            contentDatum.getContentDetails().getPromos().get(0).getId() != null) {
                        appCMSPresenter.setEpisodePromoID(contentDatum.getContentDetails().getPromos().get(0).getId());
                    }
                    pageAction = getString(R.string.app_cms_action_showvideopage_key);
                }
                appCMSPresenter.launchButtonSelectedAction(permalink,
                        pageAction,
                        finalTitle,
                        null,
                        contentDatum,
                        false,
                        0,
                        null);
                appCMSPresenter.resetDeeplinkQuery();
                updatedAppCMSBinder.clearSearchQuery();
            });
        } else if (deeplinkUri.toString().contains(getString(R.string.open_video_key_for_deeplink))) {
            getContentData(pagePath.toString());
        } else if (!deeplinkUri.toString().contains(getString(R.string.settings_key_for_deeplink)) &&
                !deeplinkUri.toString().contains(getString(R.string.account_key_for_deeplink)) &&
                !deeplinkUri.toString().contains(getString(R.string.watchlist_key_for_deeplink)) &&
                !deeplinkUri.toString().contains(getString(R.string.history_key_for_deeplink)) &&
                !deeplinkUri.toString().contains(getString(R.string.download_key_for_deeplink))) {
            appCMSPresenter.launchButtonSelectedAction(pagePath.toString(),
                    action,
                    title,
                    null,
                    null,
                    false,
                    0,
                    null);
            appCMSPresenter.resetDeeplinkQuery();
            updatedAppCMSBinder.clearSearchQuery();
        }
    }

    public void navigateToReferralPage() {
        if (appCMSPresenter.isUserSubscribed()) {
            runOnUiThread(() -> appCMSPresenter.showToast(localisedStrings.getAlreadySubscribed(), Toast.LENGTH_LONG));
        } else {
            try {
                disposable = Completable.timer(3000, TimeUnit.MILLISECONDS).observeOn(AndroidSchedulers.mainThread()).subscribe(() ->
                        appCMSPresenter.navigateToReferralPage(), e -> Log.e(TAG, ":" + e));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void updateData(AppCMSBinder appCMSBinder, Action0 readyAction) {
        final AppCMSMain appCMSMain = appCMSPresenter.getAppCMSMain();

        if (appCMSBinder.getScreenName() != null && appCMSBinder.getScreenName().contains("Sub Nav") ||
                (appCMSBinder != null
                        && appCMSBinder.getAppCMSPageUI() != null
                        && isScreenSubNavigation(appCMSBinder.getAppCMSPageUI().getModuleList()))) {
            if (readyAction != null) {
                readyAction.call();
            }
        } else if (appCMSPresenter.isPageSearch(appCMSBinder.getScreenName()) && !TextUtils.isEmpty(ViewCreator.searchQueryText)) {
            if (readyAction != null) {
                readyAction.call();
            }
            if (!TextUtils.isEmpty(ViewCreator.searchQueryText)) {
                SearchQuery objSearchQuery = new SearchQuery();
                objSearchQuery.searchInstance(appCMSPresenter);
                objSearchQuery.searchQuery(ViewCreator.searchQueryText);
            }
        } else if (appCMSPresenter.isHistoryPage(appCMSBinder.getPageId())) {
            appCMSPresenter.getHistoryData(appCMSHistoryResult -> {
                if (appCMSHistoryResult != null) {
                    AppCMSPageAPI historyAPI =
                            appCMSHistoryResult.convertToAppCMSPageAPI(appCMSBinder.getPageId(), false);
                    historyAPI.getModules().get(0).setId(appCMSBinder.getPageId());
                    appCMSBinder.updateAppCMSPageAPI(historyAPI);
                    if (readyAction != null) {
                        readyAction.call();
                    }
                } else if (readyAction != null) {
                    readyAction.call();
                }
            });
        } else if (appCMSPresenter.isWatchlistPage(appCMSBinder.getPageId())) {
            appCMSPresenter.getWatchlistData(appCMSWatchlistResult -> {
                if (appCMSWatchlistResult != null) {
                    AppCMSPageAPI watchlistAPI =
                            appCMSWatchlistResult.convertToAppCMSPageAPI(appCMSBinder.getPageId(), false);
                    watchlistAPI.getModules().get(0).setId(appCMSBinder.getPageId());
                    appCMSBinder.updateAppCMSPageAPI(watchlistAPI);
                    if (readyAction != null) {
                        readyAction.call();
                    }
                } else if (readyAction != null) {
                    readyAction.call();
                }
            }, appCMSPresenter.isWatchlistPage(appCMSBinder.getPageId()));
        } else if (appCMSPresenter.isFollowPage(appCMSBinder.getPageId())) {
            appCMSPresenter.getFollowingData(appCMSWatchlistResult -> {
                if (appCMSWatchlistResult != null) {
                    AppCMSPageAPI followAPI =
                            appCMSWatchlistResult.convertToAppCMSPageAPI(appCMSBinder.getPageId(), false
                            );
                    followAPI.getModules().get(0).setId(appCMSBinder.getPageId());
                    appCMSBinder.updateAppCMSPageAPI(followAPI);
                    if (readyAction != null) {
                        readyAction.call();
                    }
                } else if (readyAction != null) {
                    readyAction.call();
                }
            });
        } else if (appCMSPresenter.isPlaylistPage(appCMSBinder.getPageId()) && appCMSPresenter.isHoichoiApp()) {
            appCMSPresenter.getPlaylistRefreshData(appCMSPlaylistResult -> {
                if (appCMSPlaylistResult != null) {
                    AppCMSPageAPI playlistApi =
                            appCMSPlaylistResult.convertToAppCMSPageAPI(appCMSBinder.getPageId());
                    playlistApi.getModules().get(0).setId(appCMSBinder.getPageId());
                    appCMSBinder.updateAppCMSPageAPI(playlistApi);
                    if (readyAction != null) {
                        readyAction.call();
                    }
                } else if (readyAction != null) {
                    if (appCMSPresenter.getPageAPILruCache().get(appCMSBinder.getPageId()) != null) {
                        appCMSBinder.updateAppCMSPageAPI(appCMSPresenter.getPageAPILruCache().get(appCMSBinder.getPageId()));
                    }
                    readyAction.call();
                }
            }, appCMSBinder.getPagePath());
        } else if (appCMSPresenter.isDownloadPage(appCMSBinder.getPageId())) {
            appCMSPresenter.navigateToDownloadPage(appPreference.getDownloadPageId());
        } else if (appCMSPresenter.isEventPage(appCMSBinder.getPageId())) {
            appCMSPresenter.getEventsPageRefreshData(appCmsPageApiAction -> {
                if (appCmsPageApiAction != null) {
                    appCMSBinder.updateAppCMSPageAPI(appCmsPageApiAction);
                    if (readyAction != null) {
                        readyAction.call();
                    }
                } else if (readyAction != null) {
                    readyAction.call();
                }
            }, appCMSBinder.getPagePath());
        } else if (appCMSPresenter.isSchedulePage(appCMSBinder.getPageId())) {
            appCMSPresenter.getScheduleRefreshData(appCMSPlaylistResultAction -> {
                if (appCMSPlaylistResultAction != null) {
                    AppCMSPageAPI pageAPI =
                            appCMSPresenter.convertToMonthlyData(appCMSPlaylistResultAction);
                    appCMSBinder.updateAppCMSPageAPI(pageAPI);
                    if (readyAction != null) {
                        readyAction.call();
                    }
                } else if (readyAction != null) {
                    readyAction.call();
                }
            });
        } else if (appCMSPresenter.isRosterPage(appCMSBinder.getPageId())) {
            appCMSPresenter.getRosterRefreshData(appCMSPlaylistResultAction -> {
                if (appCMSPlaylistResultAction != null) {
                    AppCMSPageAPI pageAPI = appCMSPresenter.convertRosterDataToAppCMSPageAPI(appCMSBinder.getPageId(), appCMSPlaylistResultAction);

                    appCMSBinder.updateAppCMSPageAPI(pageAPI);

                    if (readyAction != null) {
                        readyAction.call();
                    }
                } else if (readyAction != null) {
                    readyAction.call();
                }
            });
        } else if (appCMSPresenter.isLibraryPage(appCMSBinder.getPageId())) {
            appCMSPresenter.getLibraryRefreshData((AppCMSLibraryResult appCMSPlaylistResultAction) -> {
                if (appCMSPlaylistResultAction != null) {
                    AppCMSPageAPI pageAPI = appCMSPlaylistResultAction.convertToAppCMSPageAPI(appCMSBinder.getPageId(), appCMSPresenter.getAppCMSMain().isMonetizationModelEnabled());

                    appCMSBinder.updateAppCMSPageAPI(pageAPI);

                    if (readyAction != null) {
                        readyAction.call();
                    }
                } else if (readyAction != null) {
                    readyAction.call();
                }
            });
        } else {
            String endPoint = appCMSPresenter.getPageIdToPageAPIUrl(appCMSBinder.getPageId());
            boolean usePageIdQueryParam = true;
            if (appCMSPresenter.isPageAVideoPage(appCMSBinder.getScreenName()) ||
                    appCMSPresenter.isPageAShowPage(appCMSBinder.getPageId(), appCMSBinder.getScreenName()) ||
                    appCMSPresenter.isPageABundlePage(appCMSBinder.getPageId(), appCMSBinder.getScreenName()) ||
                    appCMSPresenter.isCategoryPage(appCMSBinder.getPageId())) {
                endPoint = appCMSPresenter.getPageNameToPageAPIUrl(appCMSBinder.getPageName());
                usePageIdQueryParam = false;
            }

            if (!TextUtils.isEmpty(endPoint)) {
                String baseUrl = appCMSMain.getApiBaseUrl();
                String siteId = appCMSMain.getInternalName();
                boolean viewPlans = appCMSPresenter.isViewPlanPage(appCMSBinder.getPageId());
                boolean showPage = appCMSPresenter.isShowPage(appCMSBinder.getPageId());
                boolean categoryPage = appCMSPresenter.isCategoryPage(appCMSBinder.getPageId());
                String apiUrl = appCMSPresenter.getApiUrl(usePageIdQueryParam,
                        viewPlans,
                        showPage,
                        categoryPage,
                        null,
                        baseUrl,
                        endPoint,
                        siteId,
                        appCMSBinder.getPagePath(),
                        appCMSBinder.getAppCMSPageUI().getCaching() != null &&
                                !appCMSBinder.getAppCMSPageUI().getCaching().shouldOverrideCaching() &&
                                appCMSBinder.getAppCMSPageUI().getCaching().isEnabled());
                appCMSPresenter.getPageIdContent(apiUrl,
                        appCMSBinder.getPagePath(),
                        null,
                        appCMSBinder.getAppCMSPageUI().getCaching() != null &&
                                appCMSBinder.getAppCMSPageUI().getCaching().isEnabled(),
                        false,
                        appCMSPageAPI -> {

                            if (appCMSPageAPI == null) {
                                return;
                            }
                            boolean isSelectedModuleFound = false;

                            if (appCMSPresenter.isViewReferralPage(appCMSPageAPI.getId())) {
                                for (int i = 0; i < appCMSPageAPI.getModules().size(); i++) {
                                    Module module = appCMSPageAPI.getModules().get(i);
                                    if (module.getModuleType().equalsIgnoreCase("ViewPlanModule")) {
                                        if (appCMSPageAPI.getModules().get(i).getMetadataMap() != null) {
                                            int finalI = i;
                                            appCMSPresenter.fetchSubscriptionPlansById(appCMSPageAPI.getModules().get(i).getMetadataMap().getPlans(), contentData -> {
                                                appCMSPresenter.stopLoader();
                                                if (contentData != null) {
                                                    if (appCMSPageAPI != null) {
                                                        appCMSPageAPI.getModules().get(finalI).setContentData(contentData);
                                                        appCMSBinder.updateAppCMSPageAPI(appCMSPageAPI);
                                                    }
                                                    if (readyAction != null) {
                                                        readyAction.call();
                                                    }
                                                }
                                            }, false);
                                        }
                                    }
                                }

                            } else {

                                for (int i = 0; i < appCMSPageAPI.getModules().size(); i++) {
                                    if (appCMSPageAPI.getModules().get(i) != null &&
                                            appCMSPageAPI.getModules().get(i).getModuleType() != null) {

                                        if (appCMSPageAPI.getModules().get(i).getModuleType().equalsIgnoreCase("VideoDetailModule")) {
                                            int position = i;

                                            if ((appCMSPageAPI.getModules().get(i) != null && appCMSPageAPI.getModules().get(i).getContentData() != null &&
                                                    CommonUtils.isPPVContent(this, appCMSPageAPI.getModules().get(i).getContentData().get(0)))) {
                                                isSelectedModuleFound = true;

                                                String contentType = appCMSPageAPI.getModules().get(i).getContentData().get(0).getGist().getContentType();
                                                appCMSPresenter.getTransactionData(appCMSPageAPI.getModules().get(i).getContentData().get(0).getGist().getId(), updatedContentDatum -> {

                                                    appCMSPageAPI.getModules().get(position).getContentData().get(0).getGist().setObjTransactionDataValue(updatedContentDatum);
                                                    appCMSPageAPI.getModules().get(position).getContentData().get(0).getGist().setRentedDialogShow(false);
                                                    if (appCMSPageAPI != null) {
                                                        appCMSBinder.updateAppCMSPageAPI(appCMSPageAPI);
                                                    }
                                                    if (readyAction != null) {
                                                        readyAction.call();
                                                    }
                                                }, contentType);
                                            }


                                        }
                                    }
                                }
                                Log.w(TAG, "Retrieved page content");
                                if (appCMSPageAPI != null && !isSelectedModuleFound) {
                                    appCMSBinder.updateAppCMSPageAPI(appCMSPageAPI);
                                }
                                if (readyAction != null) {
                                    readyAction.call();
                                }
                            }
                        });
            } else if (readyAction != null) {
                readyAction.call();
            }
        }

        checkUnreadInboxMessageCount();
    }

    private void updateData() {
        final AppCMSMain appCMSMain = appCMSPresenter.getAppCMSMain();
        if (appCMSPresenter != null) {
            for (Map.Entry<String, AppCMSBinder> appCMSBinderEntry : appCMSBinderMap.entrySet()) {
                final AppCMSBinder appCMSBinder = appCMSBinderEntry.getValue();
                if (appCMSBinder != null) {
                    if (appCMSPresenter.isHistoryPage(appCMSBinder.getPageId())) {
                        appCMSPresenter.getHistoryData(appCMSHistoryResult -> {
                            if (appCMSHistoryResult != null) {
                                AppCMSPageAPI historyAPI =
                                        appCMSHistoryResult.convertToAppCMSPageAPI(appCMSBinder.getPageId(), false);
                                historyAPI.getModules().get(0).setId(appCMSBinder.getPageId());
                                appCMSPresenter.mergeData(historyAPI, appCMSBinder.getAppCMSPageAPI());
                                appCMSBinder.updateAppCMSPageAPI(appCMSBinder.getAppCMSPageAPI());

                                //Log.d(TAG, "Updated watched history for loaded displays");
                            }
                        });
                    } else if (appCMSPresenter.isWatchlistPage(appCMSBinder.getPageId())) {
                        appCMSPresenter.getWatchlistData(appCMSWatchlistResult -> {
                            if (appCMSWatchlistResult != null) {
                                AppCMSPageAPI watchlistAPI =
                                        appCMSWatchlistResult.convertToAppCMSPageAPI(appCMSBinder.getPageId(), false);
                                watchlistAPI.getModules().get(0).setId(appCMSBinder.getPageId());
                                appCMSPresenter.mergeData(watchlistAPI, appCMSBinder.getAppCMSPageAPI());
                                appCMSBinder.updateAppCMSPageAPI(appCMSBinder.getAppCMSPageAPI());
                                //Log.d(TAG, "notd watched history for loaded displays");
                            }
                        }, appCMSPresenter.isWatchlistPage(appCMSBinder.getPageId()));
                    } else {
                        String endPoint = appCMSPresenter.getPageIdToPageAPIUrl(appCMSBinder.getPageId());
                        boolean usePageIdQueryParam = true;
                        if (appCMSPresenter.isPageAVideoPage(appCMSBinder.getScreenName())) {
                            endPoint = appCMSPresenter.getPageNameToPageAPIUrl(appCMSBinder.getScreenName());
                            usePageIdQueryParam = false;
                        }

                        if (!TextUtils.isEmpty(endPoint)) {
                            String baseUrl = appCMSMain.getApiBaseUrl();
                            String siteId = appCMSMain.getInternalName();
                            boolean viewPlans = appCMSPresenter.isViewPlanPage(appCMSBinder.getPageId());
                            boolean showPage = appCMSPresenter.isShowPage(appCMSBinder.getPageId());
                            boolean categoryPage = appCMSPresenter.isCategoryPage(appCMSBinder.getPageId());
                            String apiUrl = appCMSPresenter.getApiUrl(usePageIdQueryParam,
                                    viewPlans,
                                    showPage,
                                    categoryPage,
                                    null,
                                    baseUrl,
                                    endPoint,
                                    siteId,
                                    appCMSBinder.getPagePath(),
                                    appCMSBinder.getAppCMSPageUI().getCaching() != null &&
                                            appCMSBinder.getAppCMSPageUI().getCaching().isEnabled());

                            appCMSPresenter.getPageIdContent(apiUrl,
                                    appCMSBinder.getPagePath(),
                                    null,
                                    appCMSBinder.getAppCMSPageUI().getCaching() != null && appCMSBinder.getAppCMSPageUI().getCaching().isEnabled(),
                                    false,
                                    appCMSPageAPI -> {
                                        if (appCMSPageAPI != null) {
                                            if (appCMSPresenter.isUserLoggedIn()) {
                                                if (appCMSPageAPI.getModules() != null) {
                                                    for (Module module : appCMSPageAPI.getModules()) {
                                                        AppCMSUIKeyType moduleType = appCMSPresenter.getJsonValueKeyMap().get(module.getModuleType());
                                                        if (moduleType == AppCMSUIKeyType.PAGE_API_HISTORY_MODULE_KEY ||
                                                                moduleType == AppCMSUIKeyType.PAGE_VIDEO_DETAILS_KEY) {
                                                            appCMSPresenter.getHistoryData(appCMSHistoryResult -> {
                                                                if (appCMSHistoryResult != null) {
                                                                    AppCMSPageAPI historyAPI =
                                                                            appCMSHistoryResult.convertToAppCMSPageAPI(appCMSPageAPI.getId(), false);
                                                                    historyAPI.getModules().get(0).setId(module.getId());
                                                                    appCMSPresenter.mergeData(historyAPI, appCMSPageAPI);
                                                                    appCMSBinder.updateAppCMSPageAPI(appCMSPageAPI);

                                                                    //Log.d(TAG, "Updated watched history for loaded displays");
                                                                }
                                                            });
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    });
                        }
                    }
                }
            }
        }
    }

    private void mergeInputData(AppCMSBinder updatedAppCMSBinder, String pageId) {
        if (appCMSBinderMap.containsKey(pageId) && appCMSPresenter != null &&
                appCMSPresenter.isPageAVideoPage(updatedAppCMSBinder.getScreenName())) {
            AppCMSBinder appCMSBinder = appCMSBinderMap.get(pageId);
            if (updatedAppCMSBinder.getPagePath().equals(appCMSBinder.getPagePath())) {
                AppCMSPageAPI updatedAppCMSPageAPI = updatedAppCMSBinder.getAppCMSPageAPI();
                AppCMSPageAPI appCMSPageAPI = appCMSBinder.getAppCMSPageAPI();
                appCMSPresenter.mergeData(updatedAppCMSPageAPI, appCMSPageAPI);
            }
        }
    }

    @Override
    public void onBackStackChanged() {
        appCMSPresenter.dismissOpenDialogs(null);
        if (!appCMSPresenter.getConfigurationChanged() &&
                !appCMSPresenter.isMainFragmentTransparent()) {
            appCMSPresenter.showMainFragmentView(true);
        }

        appCMSPresenter.onConfigurationChange(false);
        appCMSPresenter.cancelInternalEvents();
        appCMSPresenter.restartInternalEvents();
        if (appCMSPresenter.isViewPlanPage(updatedAppCMSBinder.getPageId())) {
            //Log.d(TAG, "checkForExistingSubscription() - 1532");
            appCMSPresenter.checkForExistingSubscription(appCMSPresenter.getLaunchType() == AppCMSPresenter.LaunchType.SUBSCRIBE && !appCMSPresenter.isUserSubscribed());
            appCMSPresenter.refreshSubscriptionData(null, true);
        }

        getSupportFragmentManager().removeOnBackStackChangedListener(this);

        if (updatedAppCMSBinder != null && updatedAppCMSBinder.getSearchQuery() != null) {
            //Log.d(TAG, "Successfully loaded page " + appCMSBinder.getPageName());
            //Log.d(TAG, "Processing search query for deeplink " +
//                    appCMSBinder.getSearchQuery().toString());
            appCMSPresenter.sendDeepLinkAction(updatedAppCMSBinder.getSearchQuery());
            updatedAppCMSBinder.clearSearchQuery();
        }

        try {
            reportFullyDrawn();
        } catch (Exception e) {
            Log.e(TAG, "" + e.toString());

        }
    }

    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        //Log.e(TAG, "Failed to connect for Google SignIn: " + connectionResult.getErrorMessage());
    }

    private void setMediaRouterButtonVisibility(String pageId) {
        if (!castDisabled) {
            try {
                if ((appCMSPresenter.findHomePageNavItem() != null &&
                        !TextUtils.isEmpty(appCMSPresenter.findHomePageNavItem().getPageId()) &&
                        appCMSPresenter.findHomePageNavItem().getPageId().equalsIgnoreCase(pageId)) ||
                        (appCMSPresenter.findMoviesPageNavItem() != null &&
                                !TextUtils.isEmpty(appCMSPresenter.findMoviesPageNavItem().getPageId()) &&
                                appCMSPresenter.findMoviesPageNavItem().getPageId().equalsIgnoreCase(pageId)) && !appCMSPresenter.isWatchlistPage(pageId)) {
                    setCastingVisibility(true);
                    CastServiceProvider.getInstance(this).isHomeScreen(true);
                } else {
                    setCastingVisibility(false);
                    CastServiceProvider.getInstance(this).isHomeScreen(false);
                }

                if (appCMSPresenter.isCastEnable() &&
                        CastServiceProvider.getInstance(this).isOverlayVisible()) {
                    CastServiceProvider.getInstance(this).showIntroOverLay();
                }
            } catch (Exception e) {
                Log.e(TAG, "" + e.toString());

            }
        }
        if (CastServiceProvider.getInstance(this).shouldCastMiniControllerVisible()) {
            appCMSCastController.setVisibility(View.VISIBLE);
        } else {
            appCMSCastController.setVisibility(View.GONE);

        }
    }

    public void setCastingVisibility(boolean isVisible) {
        if (appCMSPresenter != null && appCMSPresenter.getAppCMSMain() != null && appCMSPresenter.getAppCMSMain().getFeatures() != null
                && appCMSPresenter.getAppCMSMain().getFeatures().isCasting() && isVisible)
            ll_media_route_button.setVisibility(View.VISIBLE);
        else
            ll_media_route_button.setVisibility(View.GONE);
    }

    private void setCastingInstance() {
        try {
            CastServiceProvider.getInstance(this).setActivityInstance(AppCMSPageActivity.this, mMediaRouteButton);
            CastServiceProvider.getInstance(this).onActivityResume();
            appCMSPresenter.setCurrentMediaRouteButton(mMediaRouteButton);
            if (mMediaRouteButton.getParent() != null && mMediaRouteButton.getParent() instanceof ViewGroup) {
                appCMSPresenter.setCurrentMediaRouteButtonParent((ViewGroup) mMediaRouteButton.getParent());
            }
        } catch (Exception e) {
            Log.e(TAG, "Failed to initialize cast provider: " + e.getMessage());
        }
    }

    private void handleCloseAction(boolean closeOnePage) {
        if (!appCMSBinderStack.isEmpty()) {
            try {
                int lastBackStackCount = getSupportFragmentManager().getBackStackEntryCount() - 1;
                if (0 < lastBackStackCount) {
                    String lastBackStackEntryName = getSupportFragmentManager().getBackStackEntryAt(lastBackStackCount)
                            .getName();
                    String lastBackStackEntryWithoutOrientationName = lastBackStackEntryName.substring(0,
                            lastBackStackEntryName.indexOf("true") > 0 ? lastBackStackEntryName.indexOf("true") :
                                    lastBackStackEntryName.indexOf("false") > 0 ? lastBackStackEntryName.indexOf("false") :
                                            lastBackStackEntryName.length());
                    while (lastBackStackCount > 0 &&
                            getSupportFragmentManager().getBackStackEntryAt(lastBackStackCount).getName().contains(lastBackStackEntryWithoutOrientationName)) {
                        getSupportFragmentManager().popBackStackImmediate();
                        lastBackStackCount = getSupportFragmentManager().getBackStackEntryCount() - 1;
                    }
                }
            } catch (Exception e) {
                Log.e(TAG, "DialogType popping back stack: " + e.getMessage());
            }

            try {
                /*
                if (appCMSPresenter.isViewPlanPage(appCMSBinderStack.peek())) {
                    if (appCMSPresenter.getLaunchType() == AppCMSPresenter.LaunchType.SUBSCRIBE) {
                        appCMSPresenter.setLaunchType(AppCMSPresenter.LaunchType.LOGIN_AND_SIGNUP);
                    }
                }
                */

                //On Video Playlist, We make this as True so making it again as false.
                if (appCMSPresenter.videoPlayerView != null) {
                    appCMSPresenter.videoPlayerView.isFromPlayListPage = false;
                    appCMSPresenter.currentVideoPlayListIndex = 0;
                }

                AppCMSBinder appCMSBinder = appCMSBinderMap.get(appCMSBinderStack.peek());
                if (appCMSPresenter.isViewPlanPage(appCMSBinder.getPageId())) {
                    appCMSPresenter.setSelectedPlan(null, null);
                }
                boolean leavingExtraPage = false;
                if (appCMSBinder != null) {
                    leavingExtraPage = appCMSBinder.getExtraScreenType() !=
                            AppCMSPresenter.ExtraScreenType.NONE;

                    boolean recurse = !closeOnePage &&
                            appCMSPresenter.isPageAVideoPage(appCMSBinder.getScreenName());

                    handleBack(true,
                            false,
                            recurse,
                            true);
                }

                if (appCMSBinderStack.isEmpty()) {
                    finishAffinity();
                    return;
                }

                appCMSBinder = appCMSBinderMap.get(appCMSBinderStack.peek());
                appCMSPresenter.setCurrentAppCMSBinder(appCMSBinder);
                if (appCMSPresenter != null && appCMSBinder != null) {
                    if (appCMSPresenter.getAppCMSMain().getFeatures().isSplashModule() &&
                            appCMSPresenter.isPageSplashPage(appCMSBinder.getPageId())) {
                        finishAffinity();
                        return;
                    }
                    leavingExtraPage = appCMSBinder.getExtraScreenType() !=
                            AppCMSPresenter.ExtraScreenType.NONE;

                    appCMSPresenter.pushActionInternalEvents(appCMSBinder.getPageId()
                            + BaseView.isLandscape(this));


                    if (appCMSPresenter.isLibraryPage(appCMSBinder.getPageId())) {
                        updateData();
                    }
                    if (appCMSPresenter.isPageAVideoPage(appCMSBinder.getPageName())) {
                        pageLoading(true);

                        updateData(appCMSBinder, () -> {
                            appCMSPresenter.sendRefreshPageAction();
//                            pageLoading(false);

                        });
                    }
                    handleLaunchPageAction(appCMSBinder,
                            false,
                            leavingExtraPage,
                            appCMSBinder.getExtraScreenType()
                                    == AppCMSPresenter.ExtraScreenType.SEARCH);
                }
            } catch (Exception e) {
                //
            }
            isActive = true;
        } else {
            isActive = false;
            finishAffinity();
        }

        if (appCMSPresenter != null) {
            appCMSPresenter.restartInternalEvents();
        }

        if (updatedAppCMSBinder != null &&
                updatedAppCMSBinder.getPageName() != null &&
                appCMSPresenter.isPageAVideoPage(updatedAppCMSBinder.getPageName())) {

        } else {
            /*if (appCMSPresenter.videoPlayerView != null) {
                appCMSPresenter.videoPlayerView.pausePlayer();
            }*/
        }/*else {
            ViewCreator.pausePlayer();
            ViewCreator.clearPlayerView();
        }*/

        // ViewCreator.resetFullPlayerMode(this, appCMSPresenter);
    }

    @Override
    public void saveQuery(String searchQuery) {
        this.searchQuery = searchQuery;
    }

    @Override
    public String restoreQuery() {
        return searchQuery;
    }

    private int getAppCMSBinderStackSize() {
        if (appCMSBinderStack != null && !appCMSBinderStack.isEmpty()) {
            try {
                return appCMSBinderStack.size();
            } catch (Exception e) {

            }
        }
        return 0;
    }

    private String getAppCMSBinderStackEntry(int index) {
        String result = null;
        if (appCMSBinderStack != null && !appCMSBinderStack.isEmpty()) {
            try {
                ListIterator<String> listIterator = appCMSBinderStack.listIterator();
                int currentIndex = 0;
                while (listIterator.hasNext() && currentIndex < index) {
                    currentIndex++;
                }
                result = listIterator.next();
            } catch (Exception e) {

            }
        }

        return result;
    }

    private static class RefreshAppCMSBinderAction implements Action1<AppCMSPageAPI> {
        private AppCMSPresenter appCMSPresenter;
        private AppCMSBinder appCMSBinder;
        private boolean userLoggedIn;

        RefreshAppCMSBinderAction(AppCMSPresenter appCMSPresenter,
                                  AppCMSBinder appCMSBinder,
                                  boolean userLoggedIn) {
            this.appCMSPresenter = appCMSPresenter;
            this.appCMSBinder = appCMSBinder;
            this.userLoggedIn = userLoggedIn;
        }

        @Override
        public void call(AppCMSPageAPI appCMSPageAPI) {
            userLoggedIn = appCMSPresenter.isUserLoggedIn();
            if (userLoggedIn && appCMSPageAPI != null && appCMSPageAPI.getModules() != null) {
                for (Module module : appCMSPageAPI.getModules()) {
                    AppCMSUIKeyType moduleType = appCMSPresenter.getJsonValueKeyMap().get(module.getModuleType());
                    if (moduleType == AppCMSUIKeyType.PAGE_API_HISTORY_MODULE_KEY ||
                            moduleType == AppCMSUIKeyType.PAGE_VIDEO_DETAILS_KEY) {
                        if (module.getContentData() != null &&
                                !module.getContentData().isEmpty()) {
                            appCMSPresenter.getHistoryData(appCMSHistoryResult -> {
                                if (appCMSHistoryResult != null) {
                                    AppCMSPageAPI historyAPI =
                                            appCMSHistoryResult.convertToAppCMSPageAPI(appCMSPageAPI.getId(), false);
                                    historyAPI.getModules().get(0).setId(module.getId());
                                    appCMSPresenter.mergeData(historyAPI, appCMSPageAPI);
                                    appCMSBinder.updateAppCMSPageAPI(appCMSPageAPI);
                                }
                            });
                        }
                    }
                }
            }
        }
    }

    public void startFreeTrialTool() {
        if (appCMSPresenter != null &&
                appCMSPresenter.isAppSVOD()) {
            int buttonColor, textColor;

            buttonColor = appCMSPresenter.getBrandPrimaryCtaColor();
            textColor = appCMSPresenter.getGeneralTextColor();

            appCMSNavFreeTrialTool.setTextColor(textColor);
            appCMSNavFreeTrialTool.setBackgroundColor(buttonColor);
            if (appCMSPresenter.getNavigation() != null &&
                    appCMSPresenter.getNavigation().getSettings() != null &&
                    appCMSPresenter.getNavigation().getSettings().getPrimaryCta() != null &&
                    appCMSPresenter.getNavigation().getSettings().getPrimaryCta().getPlacement() != null &&
                    (appCMSPresenter.getNavigation().getSettings().getPrimaryCta().getPlacement().contains(getString(R.string.navigation_settings_primaryCta_placement)) ||
                            (appCMSPresenter.getNavigation().getSettings().getPrimaryCta().isDisplayBannerOnMobile()) &&
                                    appCMSPresenter.getNavigation().getSettings().getPrimaryCta().getPlacement().equalsIgnoreCase(getString(R.string.navigation_settings_primaryCta_placement_masthead)))) {
                if (appCMSPresenter.getNavigation().getSettings().getPrimaryCta().getBannerText() != null ||
                        appCMSPresenter.getNavigation().getSettings().getPrimaryCta().getCtaText() != null) {

                    String source = appCMSPresenter.getLocalizedCtaText(appCMSPresenter.getNavigation().getSettings().getLocalizationMap(), appCMSPresenter.getNavigation().getSettings().getPrimaryCta(), true);
                    if (source != null) {
                        SpannableString content = new SpannableString(source);
                        String bannerText = appCMSPresenter.getLocalizedBannerText(appCMSPresenter.getNavigation().getSettings().getLocalizationMap(), appCMSPresenter.getNavigation().getSettings().getPrimaryCta());
                        if (bannerText != null)
                            content.setSpan(new UnderlineSpan(), bannerText.length(),
                                    content.length(), 0);
                        else
                            content.setSpan(new UnderlineSpan(), 0,
                                    content.length(), 0);
                        appCMSNavFreeTrialTool.setText(content);
                    }
                }
            }
            appCMSNavFreeTrialTool.setOnClickListener(v -> {
                if (appCMSPresenter != null) {
                    appCMSPresenter.setLaunchType(AppCMSPresenter.LaunchType.SUBSCRIBE);
                    appCMSPresenter.navigateToSubscriptionPlansPage(true);
                }
            });

        }

    }

    void setVisibilityForStartFreeTrial(String pageId) {
        if (appCMSPresenter.getNavigation() != null &&
                appCMSPresenter.isAppSVOD() &&
                appCMSPresenter.getNavigation().getSettings() != null &&
                appCMSPresenter.getNavigation().getSettings().getPrimaryCta() != null &&
                appCMSPresenter.getNavigation().getSettings().getPrimaryCta().getPlacement() != null &&
                (appCMSPresenter.getNavigation().getSettings().getPrimaryCta().getPlacement().contains(getString(R.string.navigation_settings_primaryCta_placement)) ||
                        (appCMSPresenter.getNavigation().getSettings().getPrimaryCta().isDisplayBannerOnMobile() &&
                                appCMSPresenter.getNavigation().getSettings().getPrimaryCta().getPlacement().equalsIgnoreCase(getString(R.string.navigation_settings_primaryCta_placement_masthead))))) {

            if (appCMSPresenter.isViewPlanPage(pageId) || appCMSPresenter.isPageLoginPage(pageId) || appCMSPresenter.isPageNavigationPage(pageId) || appCMSPresenter.isShowDialogForWebPurchase()) {
                appCMSNavFreeTrialTool.setVisibility(View.GONE);
                return;
            }
            if (!appCMSPresenter.isUserSubscribed()) {
                startFreeTrialTool();
                if ((appCMSPresenter.getNavigation().getSettings().getPrimaryCta().getPlacement().contains(getString(R.string.navigation_settings_primaryCta_placement))))
                    appCMSNavFreeTrialTool.setVisibility(View.VISIBLE);
                else if (appCMSPresenter.isHomePage(updatedAppCMSBinder.getPageId()) &&
                        appCMSPresenter.getNavigation().getSettings().getPrimaryCta().isDisplayBannerOnMobile() &&
                        appCMSPresenter.getNavigation().getSettings().getPrimaryCta().getPlacement().equalsIgnoreCase(getString(R.string.navigation_settings_primaryCta_placement_masthead))) {
                    appCMSNavFreeTrialTool.setVisibility(View.VISIBLE);
                } else
                    appCMSNavFreeTrialTool.setVisibility(View.GONE);
            } else {
                appCMSNavFreeTrialTool.setVisibility(View.GONE);
            }
        }
    }


    public void setFullScreenFocus() {
        synchronized (this) {
            getWindow().getDecorView()
                    .setSystemUiVisibility(
                            View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                                    | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                                    | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                                    | View.SYSTEM_UI_FLAG_FULLSCREEN // hide status bar
                                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);

            appCMSTabNavContainer.setVisibility(View.GONE);
            appBarLayout.setVisibility(View.GONE);
        }
    }

    public void exitFullScreenFocus() {
        synchronized (this) {
            getWindow().getDecorView()
                    .setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);
            appCMSTabNavContainer.setVisibility(View.VISIBLE);
            AppCMSBinder appCMSBinder = !appCMSBinderStack.isEmpty() ?
                    appCMSBinderMap.get(appCMSBinderStack.peek()) :
                    null;
            if (BaseView.isTablet(this) && appCMSPresenter != null && appCMSBinder != null &&
                    appCMSBinder.isShowDetailsPage()) {
                handleToolbar(false,
                        appCMSBinder.getAppCMSMain(),
                        appCMSBinder.getPageId());
                appBarLayout.setVisibility(View.GONE);
            } else
                appBarLayout.setVisibility(View.VISIBLE);
        }
    }

    AudioServiceHelper.IaudioServiceCallBack callbackAudioService = new AudioServiceHelper.IaudioServiceCallBack() {
        @Override
        public void getAudioPlaybackControlVisibility(boolean shouldCastControllerShow) {

            if (shouldCastControllerShow) {
                appCMSCastController.setVisibility(View.VISIBLE);
            } else {
                appCMSCastController.setVisibility(View.GONE);
            }
        }

        @Override
        public void onConnect() {

        }
    };


    @SuppressLint("ClickableViewAccessibility")
    public void dragMiniPlayer(final MiniPlayerView relativeLayoutPIP) {
        relativeLayoutPIP.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent event) {
                int action = event.getAction();
                System.out.println("Touched  ...");
                if (action == MotionEvent.ACTION_DOWN) {
                    downRawX = event.getRawX();
                    downRawY = event.getRawY();
                    dX = view.getX() - downRawX;
                    dY = view.getY() - downRawY;
                    return true;
                } else if (action == MotionEvent.ACTION_MOVE) {
                    int viewWidth = view.getWidth();
                    int viewHeight = view.getHeight();

                    View viewParent = (View) view.getParent();
                    int parentWidth = viewParent.getWidth();
                    int parentHeight = viewParent.getHeight();

                    float newX = event.getRawX() + dX;
                    newX = Math.max(0, newX); // Don't allow the view past the left hand side of screen
                    newX = Math.min(parentWidth - viewWidth, newX); // Don't allow the view past the right hand side of screen

                    float newY = event.getRawY() + dY;
                    newY = Math.max(toolbar.getHeight(), newY); // Don't allow the view past the top of screen including toolbar
                    int bottomHeight = viewHeight + appCMSTabNavContainer.getHeight();
                    newY = Math.min(parentHeight - bottomHeight, newY); // Don't allow the view past the bottom of screen including bottombar

                    view.animate()
                            .x(newX)
                            .y(newY)
                            .setDuration(0)
                            .start();

                    return true;

                } else if (action == MotionEvent.ACTION_UP) {

                    float upRawX = event.getRawX();
                    float upRawY = event.getRawY();

                    float upDX = upRawX - downRawX;
                    float upDY = upRawY - downRawY;

                    if (Math.abs(upDX) < CLICK_DRAG_TOLERANCE && Math.abs(upDY) < CLICK_DRAG_TOLERANCE) { // A click
                        if (updatedAppCMSBinder != null && updatedAppCMSBinder.getAppCMSPageUI() != null &&
                                updatedAppCMSBinder.getAppCMSPageUI().getModuleList() != null &&
                                updatedAppCMSBinder.getAppCMSPageUI().getModuleList().size() > 0 &&
                                (appCMSPresenter.getModuleListByName(updatedAppCMSBinder.getAppCMSPageUI().getModuleList(), getString(R.string.app_cms_page_video_player_module_key)) != null ||
                                        appCMSPresenter.getModuleListByName(updatedAppCMSBinder.getAppCMSPageUI().getModuleList(), getString(R.string.app_cms_page_video_player_02_module_key)) != null))
                            relativeLayoutPIP.pipClick();
                        return true;
                    } else { // A drag
                        return true;
                    }

                }
                return true;
            }
        });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_VOLUME_UP:
                //Do something
                if (appCMSPresenter != null
                        && appCMSPresenter.videoPlayerView != null
                        && appCMSPresenter.videoPlayerView.getPlayer() != null) {
                    appCMSPresenter.videoPlayerView.getPlayer().setVolume(1f);
                    appCMSPresenter.videoPlayerView.updateVolumeIcon(false);
//                    appCMSPresenter.videoPlayerView.hideMuteNotifyView();

                }
                if (appCMSPresenter != null && appCMSPresenter.videoPlayerView != null) {
                    audio.adjustStreamVolume(AudioManager.STREAM_MUSIC,
                            AudioManager.ADJUST_RAISE, AudioManager.FLAG_SHOW_UI);
                }
                return true;
            case KeyEvent.KEYCODE_VOLUME_DOWN:
                if (appCMSPresenter != null && appCMSPresenter.videoPlayerView != null) {
                    audio.adjustStreamVolume(AudioManager.STREAM_MUSIC,
                            AudioManager.ADJUST_LOWER, AudioManager.FLAG_SHOW_UI);
                    if (audio.getStreamVolume(AudioManager.STREAM_MUSIC) == 0)
                        appCMSPresenter.videoPlayerView.updateVolumeIcon(true);
                }
                return true;
            case KeyEvent.KEYCODE_BACK:
                onBackPressed();
        }
        return true;
    }

    public boolean isScreenSubNavigation(List<ModuleList> modules) {

        if (modules != null && modules.size() <= 3) {
            for (ModuleList module : modules) {
                if (module.getBlockName() != null && module.getBlockName().contains("subNav")) {
                    return true;
                }
            }
        }
        return false;
    }

    public void getLeftNavItem(List<ModuleList> modules) {
        for (ModuleList module : modules) {


            if (module != null && module.getBlockName() != null && module.getBlockName().contains("customNavigation")) {
                leftNavId = module.getSettings().getNavigationId();
                appPreference.setleftnavigationKey(leftNavId);
                for (int i = 0; i < appCMSPresenter.getAppCMSAndroid().getHeaders().size(); i++) {
                    if (appCMSPresenter.getAppCMSAndroid().getHeaders().get(i).isType()) {
                        bottomTabHeader = module.getSettings().getNavigationId();
                    }
                }
            }
        }
        createTabBar();
        setLeftNavIcon();
    }

    public void getLeftNavItemISNetworkNotConnected(List<ModuleList> modules) {
        for (ModuleList module : modules) {
            if (module.getBlockName().contains("customNavigation")) {
                leftNavId = module.getSettings().getNavigationId();
            }
        }
    }


    public int getModuleHeight() {
        if (appCMSTabNavContainer.getVisibility() == View.VISIBLE && appBarLayout.getVisibility() == View.VISIBLE)
            return appCMSParentView.getHeight() - appCMSTabNavContainer.getHeight() - appBarLayout.getHeight();
        return appCMSParentView.getHeight();
    }

    public void checkedFixedBanner() {
        if (!isFixedBannerHidden && appCMSPresenter != null && appCMSPresenter.getAppCMSMain() != null && appCMSPresenter.getAppCMSMain().getFeatures() != null
                && appCMSPresenter.getAppCMSMain().getFeatures().isExtendSubscriptionBanner() && appCMSPresenter.isUserLoggedIn() && !appCMSPresenter.isUserSubscribed()
                && appCMSPresenter.getAppPreference() != null && !TextUtils.isEmpty(appCMSPresenter.getAppPreference().getSubscriptionStatus())
                && appCMSPresenter.getAppPreference().getSubscriptionStatus().equalsIgnoreCase(getString(R.string.subscription_status_suspended))
                && appCMSPresenter.getLaunchType() != AppCMSPresenter.LaunchType.VIEWING_RESTRICTIONS_VIEW
                && appCMSPresenter.getLaunchType() != AppCMSPresenter.LaunchType.VIDEO_PIN_VIEW) {
            ViewCreator.setTypeFace(this, appCMSPresenter, appCMSPresenter.getJsonValueKeyMap(), new Component(), bannerId);
            bannerId.setText(localisedStrings.getSubscriptionExpiredText());
            fixedBannerView.setVisibility(View.VISIBLE);
            findViewById(R.id.app_cms_close_banner_button).setOnClickListener(v -> {
                fixedBannerView.setVisibility(View.GONE);
                isFixedBannerHidden = true;
            });

            fixedBannerView.setOnClickListener(view -> appCMSPresenter.navigateToSubscriptionPlansPage(false));
        } else {
            fixedBannerView.setVisibility(View.GONE);
        }
    }

    public int getTabbarHeight() {
        return appCMSTabNavContainer.getHeight();
    }

    private void checkUnreadInboxMessageCount() {
        if (appCMSPresenter != null && appCMSPresenter.getAppCMSMain() != null && appCMSPresenter.getAppCMSMain().isAppInboxEnable()
                && appCMSPresenter.isCleverTapAvailable && appCMSPresenter.getCleverTapInstance() != null
                && appCMSPresenter.getCleverTapInstance().isInitialize()) {
            CleverTapAPI cleverTapAPI = appCMSPresenter.getCleverTapInstance().getCleverTapAPI();
            if (cleverTapAPI != null) {
                readMessageIdFl.setVisibility(View.VISIBLE);
                int unReadCount = cleverTapAPI.getInboxMessageCount();
                ViewCreator.setTypeFace(this, appCMSPresenter, appCMSPresenter.getJsonValueKeyMap(), new Component(), cart_badge_count);
                if (unReadCount == 0) {
                    cart_badge_count.setVisibility(View.GONE);
                } else {
                    cart_badge_count.setVisibility(View.VISIBLE);
                }
            }
        } else {
            readMessageIdFl.setVisibility(View.GONE);
        }
    }


    public void setSubscribeBtnOnHeader() {
        //appCMSbtnHeaderSubscribe.setBackground(CustomShape.createRoundedRectangleDrawable(appCMSPresenter.getBrandPrimaryCtaColor()));
        //appCMSbtnHeaderSubscribe.setText(appCMSPresenter.getLocalisedStrings().getSubscribeNowText());
        if (appCMSPresenter.getAppCMSMain() != null && appCMSPresenter.getAppCMSMain().getFeatures() != null && appCMSPresenter.getAppCMSMain().getFeatures().isEnableSubscribeNow() && !appCMSPresenter.isUserSubscribed()) {
            appCMSbtnHeaderSubscribe.setBackground(CustomShape.createRoundedRectangleDrawable(appCMSPresenter.getBrandPrimaryCtaColor()));
            appCMSbtnHeaderSubscribe.setTextColor(appCMSPresenter.getBrandPrimaryCtaTextColor());
            appCMSbtnHeaderSubscribe.setVisibility(View.VISIBLE);
            appCMSbtnHeaderSubscribe.setText(R.string.app_cms_subscribe_now);
            if (appCMSPresenter.getNavigation() != null && appCMSPresenter.getNavigation().getSettings() != null
                    && appCMSPresenter.getNavigation().getSettings().getPrimaryCta() != null
                    && appCMSPresenter.getNavigation().getSettings().getPrimaryCta().getCtaText() != null) {
                String localizedCtaText = appCMSPresenter.getLocalizedCtaText(appCMSPresenter.getNavigation().getSettings().getLocalizationMap(), appCMSPresenter.getNavigation().getSettings().getPrimaryCta(), false);
                if (!TextUtils.isEmpty(localizedCtaText)) {
                    appCMSbtnHeaderSubscribe.setText(localizedCtaText);
                } else {
                    appCMSbtnHeaderSubscribe.setText(appCMSPresenter.getNavigation().getSettings().getPrimaryCta().getCtaText());
                }
            }

            appCMSbtnHeaderSubscribe.setOnClickListener(view -> appCMSPresenter.navigateToSubscriptionPlansPage(false));
        } else {
            appCMSbtnHeaderSubscribe.setVisibility(View.GONE);
        }

    }

    public String getCurrentPageId() {
        return updatedAppCMSBinder != null ? updatedAppCMSBinder.getPageId() : null;
    }

    public Map<String, AppCMSBinder> getAppCMSBinderMap() {
        return appCMSBinderMap;
    }

    private void switchColor(boolean checked) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            showMiniPlayer.getThumbDrawable().setColorFilter(checked ? appCMSPresenter.getBrandPrimaryCtaColor() : appCMSPresenter.getGeneralTextColor(), PorterDuff.Mode.MULTIPLY);
            showMiniPlayer.getTrackDrawable().setColorFilter(!checked ? appCMSPresenter.getBrandSecondaryCtaTextColor() : appCMSPresenter.getGeneralTextColor(), PorterDuff.Mode.MULTIPLY);
        }
    }

    public void getContentData(String pagePath) {
        String apiUrl = appCMSPresenter.getApiUrl(false,
                false,
                false,
                false,
                null,
                appCMSPresenter.getAppCMSMain().getApiBaseUrl(),
                "/content/pages",
                appCMSPresenter.getAppCMSMain().getInternalName(),
                pagePath,
                false);
        Log.i("ApiUrl : ", "" + apiUrl);
        appCMSPresenter.getPageIdContent(apiUrl,
                pagePath,
                null,
                false,
                false,
                new AppCMSPageAPIAction(false,
                        false,
                        false,
                        null,
                        null,
                        null,
                        "",
                        pagePath,
                        false,
                        false,
                        null) {
                    @Override
                    public void call(AppCMSPageAPI appCMSPageAPI) {
                        if (appCMSPageAPI != null) {
                            if (true) {
                                try {
                                    boolean flag = false;
                                    String action = getString(R.string.app_cms_action_watchvideo_key);
                                    for (int i = 0; i < appCMSPageAPI.getModules().size(); i++) {
                                        if (appCMSPageAPI.getModules().get(i).getContentData() != null) {
                                            for (int j = 0; j < appCMSPageAPI.getModules().get(i).getContentData().size(); j++) {
                                                if (appCMSPageAPI.getModules().get(i).getContentData().get(j).getGist().getPermalink().equalsIgnoreCase(pagePath)) {
                                                    if (!appCMSPageAPI.getModules().get(i).getContentData().get(j).getGist().getContentType().equalsIgnoreCase(getString(R.string.app_cms_series_title))) {
                                                        //appCMSPresenter.launchVideoPlayer(appCMSPageAPI.getModules().get(i).getContentData().get(j), appCMSPageAPI.getModules().get(i).getContentData().get(j).getGist().getId(), 0, null, 0, action);
                                                        appCMSPresenter.launchButtonSelectedAction(appCMSPageAPI.getModules().get(i).getContentData().get(j).getGist().getPermalink(),
                                                                action,
                                                                appCMSPageAPI.getModules().get(i).getContentData().get(j).getGist().getTitle(),
                                                                null,
                                                                appCMSPageAPI.getModules().get(i).getContentData().get(j),
                                                                false,
                                                                0,
                                                                null);
                                                    }
                                                    flag = true;
                                                    break;
                                                }
                                            }
                                            if (flag)
                                                break;
                                        }
                                    }
                                } catch (Exception e1) {
                                    Log.d(TAG, e1.toString());
                                }
                            } else {
                                appCMSPresenter.resetDeeplinkQuery();
                            }
                        }
                    }
                });
    }

    private abstract static class AppCMSPageAPIAction implements Action1<AppCMSPageAPI> {
        final boolean appbarPresent;
        final boolean fullscreenEnabled;
        final boolean navbarPresent;
        final AppCMSPageUI appCMSPageUI;
        final String action;
        final String pageId;
        final String pageTitle;
        final String pagePath;
        final boolean launchActivity;
        final boolean sendCloseAction;
        final Uri searchQuery;

        AppCMSPageAPIAction(boolean appbarPresent,
                            boolean fullscreenEnabled,
                            boolean navbarPresent,
                            AppCMSPageUI appCMSPageUI,
                            String action,
                            String pageId,
                            String pageTitle,
                            String pagePath,
                            boolean launchActivity,
                            boolean sendCloseAction,
                            Uri searchQuery) {
            this.appbarPresent = appbarPresent;
            this.fullscreenEnabled = fullscreenEnabled;
            this.navbarPresent = navbarPresent;
            this.appCMSPageUI = appCMSPageUI;
            this.action = action;
            this.pageId = pageId;
            this.pageTitle = pageTitle;
            this.pagePath = pagePath;
            this.launchActivity = launchActivity;
            this.sendCloseAction = sendCloseAction;
            this.searchQuery = searchQuery;
        }
    }

}
