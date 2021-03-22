package com.viewlift.tv.views.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.PersistableBundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.amazon.device.iap.PurchasingService;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.crashlytics.FirebaseCrashlytics;
import com.viewlift.AppCMSApplication;
import com.viewlift.R;
import com.viewlift.models.data.appcms.api.AppCMSPageAPI;
import com.viewlift.models.data.appcms.api.ContentDatum;
import com.viewlift.models.data.appcms.api.Gist;
import com.viewlift.models.data.appcms.api.Module;
import com.viewlift.models.data.appcms.ui.AppCMSUIKeyType;
import com.viewlift.models.data.appcms.ui.android.Headers;
import com.viewlift.models.data.appcms.ui.android.MetaPage;
import com.viewlift.models.data.appcms.ui.android.NavigationPrimary;
import com.viewlift.models.data.appcms.ui.android.NavigationUser;
import com.viewlift.models.data.appcms.ui.main.AppCMSMain;
import com.viewlift.models.data.appcms.ui.page.ModuleList;
import com.viewlift.models.network.modules.AppCMSSearchUrlModule;
import com.viewlift.presenters.AppCMSPresenter;
import com.viewlift.tv.AppCmsTVSplashActivity;
import com.viewlift.tv.iap.IapCallBacks;
import com.viewlift.tv.utility.Utils;
import com.viewlift.tv.views.component.AppCmsTvSearchComponent;
import com.viewlift.tv.views.component.DaggerAppCmsTvSearchComponent;
import com.viewlift.tv.views.customviews.CustomTVVideoPlayerView;
import com.viewlift.tv.views.customviews.NavigationPlaceholderFramelayout;
import com.viewlift.tv.views.dialog.AppCMSTVVerifyVideoPinDialog;
import com.viewlift.tv.views.fragment.AccountDetailsEditDialogFragment;
import com.viewlift.tv.views.fragment.AppCMSVerticalGridFragment;
import com.viewlift.tv.views.fragment.AppCmsBrowseFragment;
import com.viewlift.tv.views.fragment.AppCmsChangelanguageFragment;
import com.viewlift.tv.views.fragment.AppCmsGenericDialogFragment;
import com.viewlift.tv.views.fragment.AppCmsLibraryFragment;
import com.viewlift.tv.views.fragment.AppCmsLinkYourAccountFragment;
import com.viewlift.tv.views.fragment.AppCmsLoginDialogFragment;
import com.viewlift.tv.views.fragment.AppCmsMyProfileFragment;
import com.viewlift.tv.views.fragment.AppCmsNavigationFragment;
import com.viewlift.tv.views.fragment.AppCmsResetPasswordFragment;
import com.viewlift.tv.views.fragment.AppCmsSearchFragment;
import com.viewlift.tv.views.fragment.AppCmsSignUpDialogFragment;
import com.viewlift.tv.views.fragment.AppCmsSubNavigationFragment;
import com.viewlift.tv.views.fragment.AppCmsTVPageFragment;
import com.viewlift.tv.views.fragment.AppCmsTvErrorFragment;
import com.viewlift.tv.views.fragment.BaseFragment;
import com.viewlift.tv.views.fragment.ClearDialogFragment;
import com.viewlift.tv.views.fragment.ParentalGateViewFragment;
import com.viewlift.tv.views.fragment.PreviewDialogFragment;
import com.viewlift.tv.views.fragment.TVODPurchaseDialogFragment;
import com.viewlift.tv.views.fragment.TextOverlayDialogFragment;
import com.viewlift.views.binders.AppCMSBinder;
import com.viewlift.views.binders.AppCMSSwitchSeasonBinder;
import com.viewlift.views.binders.RetryCallBinder;
import com.viewlift.views.listener.TrailerCompletedCallback;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Stack;

import static com.viewlift.presenters.AppCMSPresenter.CLOSE_DIALOG_ACTION;
import static com.viewlift.presenters.AppCMSPresenter.DIALOG_FRAGMENT_TAG;
import static com.viewlift.presenters.AppCMSPresenter.STAND_ALONE_PLAYER_REFRESH;
import static com.viewlift.tv.utility.Utils.getCustomTVVideoPlayerView;
import static com.viewlift.tv.utility.Utils.setCustomTVVideoPlayerView;
import static com.viewlift.tv.views.customviews.NavigationPlaceholderFramelayout.NavigationState.NAVIGATION_STATE_CLOSED;
import static com.viewlift.tv.views.customviews.NavigationPlaceholderFramelayout.NavigationState.NAVIGATION_STATE_EXPANDED;

public class AppCmsHomeActivity extends AppCmsBaseActivity implements
        AppCmsNavigationFragment.OnNavigationVisibilityListener,
        AppCmsTvErrorFragment.ErrorFragmentListener, IapCallBacks,
        ParentalGateViewFragment.ParentalGateViewInteractionListener {

    public static final int OPEN_SEARCH_RESULT_CODE = 9182389;
    private final String TAG = AppCmsHomeActivity.class.getName();
    private boolean isActive;
    RelativeLayout navParentContainer;
    AppCmsNavigationFragment navigationFragment;
    AppCMSBinder updatedAppCMSBinder;
    AppCMSPresenter appCMSPresenter;
    AppCmsLoginDialogFragment loginDialog;
    AppCmsSignUpDialogFragment signUpDialog;
    boolean shouldShowLeftNav = false;
    boolean shouldShowSubLeftNav = false;
    private FrameLayout navHolder;
    private NavigationPlaceholderFramelayout navigationPlaceholderFramelayout;
    private FrameLayout homeHolder;
    private FrameLayout shadowView, miniVideoPlayerView;
    private BroadcastReceiver presenterActionReceiver;
    private BroadcastReceiver updateHistoryDataReciever;
    private Stack<String> appCMSBinderStack;
    private Map<String, AppCMSBinder> appCMSBinderMap;
    private AppCmsTvSearchComponent appCMSSearchUrlComponent;
    private AppCmsResetPasswordFragment appCmsResetPasswordFragment;
    private AppCmsSubNavigationFragment appCmsSubNavigationFragment;
    private BroadcastReceiver updateWatchListDataReceiver;
    private AppCmsLinkYourAccountFragment appCmsLinkYourAccountFragment;
    public AppCmsLibraryFragment appCmsLibraryFragment;
    private RelativeLayout subscribeNowStripContaineer;
    private Animation animBottomSlide;
    private boolean isEnableMiniPlayer;
    private String trailerID;
    private String promoID;
    private AppCMSBinder appCMSBinder;
    private int textColor;
    private int bgColor;
    private AppCMSBinder myProfileBinder;
    private String pageName;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        appCMSPresenter = ((AppCMSApplication)getApplication())
                .getAppCMSPresenterComponent()
                .appCMSPresenter();
        if (getIntent() != null && getIntent().getAction() != null && getIntent().getData() != null) {
            if (getIntent().getAction().equalsIgnoreCase(getString(R.string.LAUNCHER_DEEPLINK_ACTION))) {
                ContentDatum contentDatum = new ContentDatum();
                Gist gist = new Gist();
                gist.setId(getIntent().getData().toString());
                contentDatum.setGist(gist);
                appCMSPresenter.launchTVVideoPlayer(contentDatum, 0, null, 0, null);
            }
        }
        isActive = true;
        Bundle args = getIntent().getBundleExtra(getString(R.string.app_cms_bundle_key));
        appCMSBinder = null;
        if (null != args && args.getBinder(getString(R.string.app_cms_binder_key)) instanceof AppCMSBinder) {
            appCMSBinder = (AppCMSBinder) args.getBinder(getString(R.string.app_cms_binder_key));
        } else {
            if (null != savedInstanceState) {
                startSplashActivity();
                finish();
                return;
            }
        }
        updatedAppCMSBinder = appCMSBinder;

        appCMSBinderStack = new Stack<>();
        appCMSBinderMap = new HashMap<>();

        appCMSPresenter = ((AppCMSApplication) getApplication())
                .getAppCMSPresenterComponent()
                .appCMSPresenter();

        new Thread(() -> {
            //Fabric.with(getApplication(), new Crashlytics())
            FirebaseCrashlytics.getInstance().setCrashlyticsCollectionEnabled(true);
            if (appCMSPresenter != null) {
                appCMSPresenter.initializeAppCMSAnalytics();
                checkCleverTapSDK();
            }
        }).run();
        //Settings The Firebase Analytics for TV
        FirebaseAnalytics mFireBaseAnalytics = FirebaseAnalytics.getInstance(this);
        if (appCMSPresenter != null) {
            appCMSPresenter.setmFireBaseAnalytics(mFireBaseAnalytics);
        }

        if (appCMSPresenter != null && null != appCMSPresenter.getAppCMSAndroid() && appCMSPresenter.getAppCMSAndroid().getAnalytics() != null) {
            appCMSPresenter.initializeGA(appCMSPresenter.getAppCMSAndroid().getAnalytics().getGoogleAnalyticsId());
        }


        String tag = getTag(updatedAppCMSBinder);
        appCMSBinderStack.push(tag);
        appCMSBinderMap.put(tag, updatedAppCMSBinder);

        if (appCMSBinder != null) {
            AppCMSMain appCMSMain = appCMSBinder.getAppCMSMain();

            textColor = Color.parseColor(appCMSMain.getBrand().getCta().getPrimary().getTextColor());
            bgColor = Color.parseColor(appCMSMain.getBrand().getGeneral().getBackgroundColor());

            String[] color = appCMSPresenter.getAppBackgroundColor().split("#");

            navigationFragment = AppCmsNavigationFragment.newInstance(
                    this,
                    this,
                    appCMSBinder,
                    textColor,
                    bgColor);

            //  appCmsSubNavigationFragment = AppCmsSubNavigationFragment.newInstance(this, this);
            // Locale locale = appCMSPresenter.getCurrentContext().getResources().getConfiguration().locale;
            Locale locale = getResources().getConfiguration().locale;
            System.out.println("Language Locale in HomeActivity is = " + locale.getLanguage());

            setContentView(R.layout.activity_app_cms_tv_home);

            subscribeNowStripContaineer = (RelativeLayout) findViewById(R.id.subscribe_now_strip_containeer);
            navParentContainer = (RelativeLayout) findViewById(R.id.navigation_layouts_container);
            navHolder = (FrameLayout) findViewById(R.id.navigation_placholder);
            navigationPlaceholderFramelayout = (NavigationPlaceholderFramelayout) findViewById(R.id.news_navigation_placholder);
            // subNavHolder = (FrameLayout) findViewById(R.id.sub_navigation_placeholder);
            homeHolder = (FrameLayout) findViewById(R.id.home_placeholder);
            homeHolder.setBackgroundColor(Color.parseColor(appCMSPresenter.getAppBackgroundColor()));
            shadowView = (FrameLayout) findViewById(R.id.shadow_view);
            miniVideoPlayerView = (FrameLayout) findViewById(R.id.mini_video_player_View);

            if (appCMSPresenter.isNewsLeftNavigationEnabled()) {
                ViewGroup.LayoutParams layoutParams = navigationPlaceholderFramelayout.getLayoutParams();
                layoutParams.height = ViewGroup.LayoutParams.MATCH_PARENT;

                RelativeLayout.LayoutParams parentLayoutparam = (RelativeLayout.LayoutParams) navParentContainer.getLayoutParams();
                parentLayoutparam.height = ViewGroup.LayoutParams.MATCH_PARENT;
                navParentContainer.bringToFront();
                navParentContainer.setVisibility(View.INVISIBLE);

                navigationPlaceholderFramelayout.setBackgroundColor(Color.parseColor(Utils.getNavigationBackgroundColor(this, appCMSPresenter)));
                navigationPlaceholderFramelayout.getBackground().setAlpha(200);
                parentLayoutparam.addRule(RelativeLayout.ALIGN_PARENT_LEFT, R.id.navRecylerView);
                layoutParams.width = 93;
                parentLayoutparam.width = 93;

                RelativeLayout.LayoutParams homeDataLayoutParams = (RelativeLayout.LayoutParams) subscribeNowStripContaineer.getLayoutParams();
                homeDataLayoutParams.leftMargin = 93;
                subscribeNowStripContaineer.setLayoutParams(homeDataLayoutParams);
            } else if (appCMSPresenter.isLeftNavigationEnabled()) {
//            navHolder.findViewById(R.id.left_menu_app_logo).setVisibility(View.VISIBLE);
                ViewGroup.LayoutParams layoutParams = navHolder.getLayoutParams();
                layoutParams.height = ViewGroup.LayoutParams.MATCH_PARENT;
                layoutParams.width = 700;

                RelativeLayout.LayoutParams parentLayoutparam = (RelativeLayout.LayoutParams) navParentContainer.getLayoutParams();
                parentLayoutparam.height = ViewGroup.LayoutParams.MATCH_PARENT;
                parentLayoutparam.addRule(RelativeLayout.CENTER_HORIZONTAL, R.id.navRecylerView);
                navParentContainer.bringToFront();
                navParentContainer.setBackground(getDrawable(R.drawable.left_nav_gradient));
                navParentContainer.getBackground().setTint(Color.parseColor(appCMSPresenter.getAppBackgroundColor()));
                navParentContainer.setVisibility(View.INVISIBLE);
            } else if (appCMSPresenter.getTemplateType().equals(AppCMSPresenter.TemplateType.SPORTS)) {
                ViewGroup.LayoutParams layoutParams = navHolder.getLayoutParams();
                layoutParams.height = ViewGroup.LayoutParams.MATCH_PARENT;
                RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) subscribeNowStripContaineer.getLayoutParams();
                params.addRule(RelativeLayout.BELOW, R.id.navigation_layouts_container);
            } else {
                RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) subscribeNowStripContaineer.getLayoutParams();
                params.addRule(RelativeLayout.BELOW, R.id.navigation_layouts_container);
            }

            setNavigationFragment(navigationFragment);
            if (appCMSPresenter.isLeftNavigationEnabled()) {
                if (appCMSPresenter.isNewsLeftNavigationEnabled()) {
                    showNavigation(false);
                } else {
                    showNavigation(true);
                }
            }
            setPageFragment(appCMSBinder);
            appCMSPresenter.sendGaScreen(appCMSBinder.getScreenName());
            showInfoIcon(appCMSBinder.getPageId());

            if (null == appCMSSearchUrlComponent) {
                appCMSSearchUrlComponent = DaggerAppCmsTvSearchComponent.builder()
                        .appCMSSearchUrlModule(new AppCMSSearchUrlModule(appCMSMain.getApiBaseUrlCached(),
                                appCMSMain.getInternalName(),
                                appCMSPresenter.getApiKey(),
                                /*appCMSPresenter.getAuthToken(),*/
                                appCMSBinder.getAppCMSSearchCall()))
                        .build();
            }

            updateHistoryDataReciever = new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {
                    if (intent.getAction().equals(AppCMSPresenter.PRESENTER_UPDATE_HISTORY_ACTION)) {
                        updateData();
                    }
                }
            };

            updateWatchListDataReceiver = new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {
                    if (intent.getAction().equals(AppCMSPresenter.PRESENTER_UPDATE_WATCHLIST_ACTION)) {
                        updateWatchListData();
                    }
                }
            };

            registerReceiver(updateWatchListDataReceiver, new IntentFilter(AppCMSPresenter.PRESENTER_UPDATE_WATCHLIST_ACTION));
            registerReceiver(updateHistoryDataReciever, new IntentFilter(AppCMSPresenter.PRESENTER_UPDATE_HISTORY_ACTION));

        presenterActionReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (intent.getAction().equals(AppCMSPresenter.PRESENTER_NAVIGATE_ACTION)) {
                    Bundle args = intent.getBundleExtra(getString(R.string.app_cms_bundle_key));
                    updatedAppCMSBinder = (AppCMSBinder) args.getBinder(getString(R.string.app_cms_binder_key));
                    setPageName(updatedAppCMSBinder.getPageName());
                    try {
                        if (isActive) {
                            if (/*!appCMSPresenter.isPagePrimary(((AppCMSBinder) args.getBinder(getString(R.string.app_cms_binder_key))).getPageId()) &&*/
                                    (appCMSPresenter.isPageUser(((AppCMSBinder) args.getBinder(getString(R.string.app_cms_binder_key))).getPageId())
                                            || appCMSPresenter.isPageFooter(((AppCMSBinder) args.getBinder(getString(R.string.app_cms_binder_key))).getPageId())
                                            || appCMSPresenter.isViewPlanPage(((AppCMSBinder) args.getBinder(getString(R.string.app_cms_binder_key))).getPageId())
                                            || (appCMSPresenter.getTosPage() != null && appCMSPresenter.getTosPage().getPageId().equalsIgnoreCase(((AppCMSBinder) args.getBinder(getString(R.string.app_cms_binder_key))).getPageId()))
                                            || (appCMSPresenter.getPrivacyPolicyPage() != null && appCMSPresenter.getPrivacyPolicyPage().getPageId().equalsIgnoreCase(((AppCMSBinder) args.getBinder(getString(R.string.app_cms_binder_key))).getPageId())))) {
                                //check first its a request for Terms of service or Privacy Policy dialog.
                                if ((((AppCMSBinder) args.getBinder(getString(R.string.app_cms_binder_key))).getExtraScreenType() ==
                                        AppCMSPresenter.ExtraScreenType.TERM_OF_SERVICE)) {
                                    openGenericDialog(intent, false);
                                } else if ((((AppCMSBinder) args.getBinder(getString(R.string.app_cms_binder_key))).getExtraScreenType() ==
                                        AppCMSPresenter.ExtraScreenType.EDIT_PROFILE)) {
                                    AppCMSBinder binder = (AppCMSBinder) args.getBinder(getString(R.string.app_cms_binder_key));
                                    if (binder.getPageName().equalsIgnoreCase(getString(R.string.app_cms_sign_up_pager_title))
                                            || binder.getScreenName().equalsIgnoreCase(getString(R.string.app_cms_pagename_create_login_key))
                                            || binder.getScreenName().equalsIgnoreCase(getString(R.string.view_plans_label))) {
                                        openSignUpDialog(intent, true);
                                    } else {
                                        openLoginDialog(intent, true);
                                    }
                                } else {
                                    myProfileBinder = (AppCMSBinder) args.getBinder(getString(R.string.app_cms_binder_key));
                                    if (myProfileBinder != null && myProfileBinder.isShowParentalGateView()) {
                                        openParentalGateViewFragment();
                                    } else {
                                        openMyProfile();
                                        handleProfileFragmentAction(myProfileBinder);
                                        showNavigation(false);
                                    }
                                }
                            } else {
                                updatedAppCMSBinder = (AppCMSBinder) args.getBinder(getString(R.string.app_cms_binder_key));
                                handleLaunchPageAction(updatedAppCMSBinder);
                                showNavigation(false); //close navigation if any.
                                enableNavigation();
                            }
                        }
                    } catch (ClassCastException e) {
                    }
                } else if (intent.getAction().equals(AppCMSPresenter.PRESENTER_PAGE_LOADING_ACTION)) {
                    Utils.pageLoading(true, AppCmsHomeActivity.this);
                } else if (intent.getAction().equals(AppCMSPresenter.PRESENTER_STOP_PAGE_LOADING_ACTION)) {
                    Utils.pageLoading(false, AppCmsHomeActivity.this);
                } else if (intent.getAction().equals(AppCMSPresenter.PRESENTER_RESET_NAVIGATION_ITEM_ACTION)) {
                    //Log.d(TAG, "Nav item - Received broadcast to select navigation item with page Id: " +
//                            intent.getStringExtra(getString(R.string.navigation_item_key)));
                    //  selectNavItem(intent.getStringExtra(getString(R.string.navigation_item_key)));
                } else if (intent.getAction().equals(AppCMSPresenter.PRESENTER_DEEPLINK_ACTION)) {
                    if (intent.getData() != null) {
                    }
                } else if (intent.getAction().equals(AppCMSPresenter.PRESENTER_DIALOG_ACTION)) {
                    Bundle bundle = intent.getBundleExtra(getString(R.string.dialog_item_key));
                    FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                    TextOverlayDialogFragment newFragment = TextOverlayDialogFragment.newInstance(
                            context,
                            bundle);
                    newFragment.show(ft, DIALOG_FRAGMENT_TAG);
                } else if (intent.getAction().equals(AppCMSPresenter.SEARCH_ACTION)) {
                    openSearchFragment(intent);
                    //  showSubNavigation(false, false); //close subnavigation if any.
                    showNavigation(false); //close navigation if any.
                } else if (intent.getAction().equals(AppCMSPresenter.LIBRARY_ACTION)) {
                    openLibraryFragment(intent);
                    //  showSubNavigation(false, false); //close subnavigation if any.
                    showNavigation(false); //close navigation if any.
                } else if (intent.getAction().equals(AppCMSPresenter.LIBRARY_DATA)) {
                    appCmsLibraryFragment.setAdapter(appCMSPresenter.libraryAllData);
                    //  showSubNavigation(false, false); //close subnavigation if any.
                    showNavigation(false); //close navigation if any.
                } else if (intent.getAction().equals(CLOSE_DIALOG_ACTION)) {
                    Utils.pageLoading(false, AppCmsHomeActivity.this);
                    closeSignInDialog();
                    closeSignUpDialog();
                } else if (intent.getAction().equals(STAND_ALONE_PLAYER_REFRESH)) {
                    Fragment parentFragment = getSupportFragmentManager().findFragmentById(R.id.home_placeholder);
                    if (parentFragment != null) {
                        Fragment fragment = parentFragment.getChildFragmentManager().
                                findFragmentById(R.id.appcms_browsefragment);
                        if (fragment instanceof AppCmsBrowseFragment) {
                            AppCmsBrowseFragment browseFragment = (AppCmsBrowseFragment) fragment;
                            CustomTVVideoPlayerView customVideoVideoPlayerView = browseFragment.getCustomVideoVideoPlayerView();
                            if (customVideoVideoPlayerView != null) {
                                customVideoVideoPlayerView.refreshStandAlonePlayer();
                            }
                        }
                    }
                } else if (intent.getAction().equals(AppCMSPresenter.ERROR_DIALOG_ACTION)) {
                    openErrorDialog(intent);
                } else if (intent.getAction().equals(AppCMSPresenter.ACTION_RESET_PASSWORD)) {
                    openResetPasswordScreen(intent);
                } else if (intent.getAction().equals(AppCMSPresenter.ACTION_LINK_YOUR_ACCOUNT)) {
                    openLinkYourAccountScreen(intent);
                } else if (intent.getAction().equals(AppCMSPresenter.ACTION_CHANGE_LANGUAGE)) {
                    openChangeLanguageScreen(intent);
                } else if (intent.getAction().equals(AppCMSPresenter.ENTITLEMENT_LOGIN_DIALOG)) {
                    openEntitlementDialog();
                } else if (intent.getAction().equals(AppCMSPresenter.RESTORE_PURCHASE_DIALOG)) {
                    String email = intent.getStringExtra(getString(R.string.app_cms_email_id_label));
                    openRestorePurchaseDialog(email);
                } else if (intent.getAction().equals(AppCMSPresenter.SUBSCRIPTION_DIALOG)) {
                    if (appCMSPresenter.isFireTVSubscriptionEnabled()) {
                        Bundle bundle = intent.getBundleExtra(getString(R.string.retryCallBundleKey));
                        ContentDatum contentDatum = (ContentDatum) intent.getSerializableExtra(getString(R.string.tv_dialog_content_datum));/*CommonUtils.contentDatum;*/
                        if (contentDatum != null && contentDatum.getSubscriptionPlans() != null) {
                            String contentType = intent.getStringExtra(getString(R.string.tv_dialog_content_type_key));
                            openSubscriptionDialog(contentType, contentDatum);
                        } else {
                            openSubscriptionDialog();
                        }
                    } else {
                        openEntitlementDialog();
                    }
                } else if (intent.getAction().equals(AppCMSPresenter.GENERIC_DIALOG)) {
                    ContentDatum contentDatum = (ContentDatum) intent.getSerializableExtra(getString(R.string.tv_dialog_content_datum));
                    openGenericDialog(intent,contentDatum);
                    }else if (intent.getAction().equals(AppCMSPresenter.ACCOUNT_DETAILS_EDIT_INFO_DIALOG)) {
                        openAccountDetailsEditInfoDialog(intent);
                    } else if (intent.getAction().equals(AppCMSPresenter.TVOD_PURCHASE_DIALOG)) {
                        openTVODPurchaseDialog(intent);
                    }else if (intent.getAction().equals(AppCMSPresenter.PRESENTER_UPDATE_HISTORY_ACTION)) {
                        updateData();
                    } else if (intent.getAction().equals(AppCMSPresenter.UPDATE_SUBSCRIPTION)) {
                    if (appCMSPresenter.isAmazonPurchaseInitiated()) {
                        if (!appCMSPresenter.isUserSubscribed()) {
                            PurchasingService.purchase(skuToPurchase);
                            appCMSPresenter.sendGaIAPEvent(com.viewlift.Utils.getIapEventBundle(appCMSPresenter.IAP_AMAZON_INITIATED, appCMSPresenter.AMAZON_IAP, appCMSPresenter.getLoggedInUser(), appCMSPresenter.getAmazonUserId(), skuToPurchase, "", ""));
                            if (!TextUtils.isEmpty(appCMSPresenter.getAppPreference().getAppsFlyerKey())) {
                                sendAppsFlyerSubscriptionInitiatedEvent();
                            }
                        } else {
                            // Toast.makeText(context,"User is Already Subscribed.", Toast.LENGTH_SHORT).show();
                            if (!appCMSPresenter.isSignupFlag())
                                appCMSPresenter.navigateToHomePage(true);
                        }
                    }
                        updateSubscriptionStrip();
                        if (appCMSPresenter.getTemplateType().equals(AppCMSPresenter.TemplateType.SPORTS)) {
                            notifyNavigationChanged();
                        } else {
                            navigationFragment.notifyDataSetInvalidate();
                        }
                        // Calling refreshPage to update the states of Start Watching & Add to watchlist button when user logs in.
                        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.home_placeholder);
                        if (fragment instanceof AppCmsTVPageFragment) {
                            ((AppCmsTVPageFragment) fragment).refreshPage();
                        }
                    } else if (intent.getAction().equals(AppCMSPresenter.INITIATE_AMAZON_PURCHASE)) {
                        if (appCMSPresenter.isAmazonPurchaseInitiated()) {
                            PurchasingService.purchase(skuToPurchase);
                        }
                    } else if (intent.getAction().equals(AppCMSPresenter.TVOD_PURCHASE)) {
                        String sku = intent.getStringExtra(getString(R.string.tvod_sku));
                        PurchasingService.purchase(sku);
                    }else if (intent.getAction().equals(AppCMSPresenter.CHECK_UPDATE_ACTION)) {
                        setUpUpdate();
                    }else if (intent.getAction().equals(AppCMSPresenter.RECOMMENDATION_DIALOG_FRAGMENT_TAG)) {
                        showRecommendationDialog(intent);
                    }else if (intent.getAction().equals(AppCMSPresenter.CHECK_UPDATE_ACTION)) {
                    setUpUpdate();
                } else if (intent.getAction().equals(AppCMSPresenter.SWITCH_SEASON_ACTION)) {
                        AppCMSSwitchSeasonBinder appCMSSwitchSeasonBinder = (AppCMSSwitchSeasonBinder) intent.getExtras().getBundle("app_cms_season_selector_key").getBinder("app_cms_episode_data");
                        AppCmsTVPageFragment parentFragment = (AppCmsTVPageFragment) getSupportFragmentManager().findFragmentById(R.id.home_placeholder);
                        parentFragment.updateSeasonData(appCMSSwitchSeasonBinder);
                    } else if (intent.getAction().equals(AppCMSPresenter.PRESENTER_TRAY_UPDATE_ON_ACTION)) {
                        isEnableMiniPlayer = intent.getBooleanExtra(getString(R.string.app_cms_bundle_key), true);
                        boolean svod = appCMSPresenter.isAppSVOD() && !appCMSPresenter.isUserLoggedIn();
                        if (isEnableMiniPlayer && !svod) {
                            if (getCustomTVVideoPlayerView() != null && getCustomTVVideoPlayerView().isVideoPlaying()) {
                                showMiniPlayer();
                            }
                        } else {
                            hideMiniPlayer();
                        }
                    } else if (intent.getAction().equals(AppCMSPresenter.PRESENTER_OPEN_PIN_DIALOG_ACTION)) {
                        AppCMSTVVerifyVideoPinDialog.newInstance(isVerified -> {
                            appCMSPresenter.setPinVerified(isVerified);
                            if (isVerified) {
                                sendBroadcast(new Intent(STAND_ALONE_PLAYER_REFRESH));
                            }
                        }).show(getSupportFragmentManager(), AppCMSTVVerifyVideoPinDialog.class.getSimpleName());

                } else if (intent.getAction().equals(getString(R.string.intent_msg_action))) {
//                    Toast.makeText(AppCmsHomeActivity.this, intent.getStringExtra(getString(R.string.json_data_msg_key)), Toast.LENGTH_SHORT).show();
                    if (intent.getStringExtra(getString(R.string.json_data_msg_key)).equalsIgnoreCase(getString(R.string.adm_directive_search_and_play))) {
                        String contentId = intent.getStringExtra(getString(R.string.json_content_id_key));
                        /*if (intent.getStringExtra(getString(R.string.json_data_type_key)).equalsIgnoreCase("SERIES")){
                            Toast.makeText(context, "SHOWS", Toast.LENGTH_SHORT).show();

                            appCMSPresenter.getShowDetails(contentId, new Action1<AppCMSShowDetail>() {
                                @Override
                                public void call(AppCMSShowDetail appCMSShowDetail) {
                                    System.out.println("SHOWDETAIL: " + appCMSShowDetail.getGist().getTitle());

                                    ContentDatum contentDatum = new ContentDatum();
                                    appCMSShowDetail.getGist().setContentType("series");
                                    contentDatum.setGist(appCMSShowDetail.getGist());
                                    contentDatum.setSeason(appCMSShowDetail.getSeasons());
                                    contentDatum.setPermalink(appCMSShowDetail.getPermalink());
                                    contentDatum.setId(appCMSShowDetail.getId());

                                    /*appCMSPresenter.launchTVButtonSelectedAction(
                                            appCMSShowDetail.getPermalink(),
                                            "showDetailPage",
                                            "",
                                            null,
                                            contentDatum,
                                            false,
                                            -1,
                                            null,
                                            null);*//*

                                    playEpisode(contentDatum);
                                }
                            });

                        } else*/
                        {
                            if (contentId != null) {
                                ContentDatum contentDatum = new ContentDatum();
                                Gist gist = new Gist();
                                gist.setId(contentId);
                                contentDatum.setGist(gist);
                                appCMSPresenter.launchTVVideoPlayer(
                                        contentDatum,
                                        0,
                                        null,
                                        0,
                                        null);
                            } else {
                                Toast.makeText(context, "Some error occurred", Toast.LENGTH_SHORT).show();
                            }
                        }
                    } else if (intent.getStringExtra(getString(R.string.json_data_msg_key)).equalsIgnoreCase(getString(R.string.adm_directive_search_and_display_results))) {
                        String searchString = intent.getStringExtra(getString(R.string.json_seek_search_string_key));
                        appCMSPresenter.openSearch("Search", "Search", searchString);
                    } else if (intent.getStringExtra(getString(R.string.json_data_msg_key)).equalsIgnoreCase(getString(R.string.adm_directive_play))) {
                    } else if (intent.getStringExtra(getString(R.string.json_data_msg_key)).equalsIgnoreCase(getString(R.string.adm_directive_pause))) {
                    } else if (intent.getStringExtra(getString(R.string.json_data_msg_key)).equalsIgnoreCase(getString(R.string.adm_directive_fast_forward))) {
                    } else if (intent.getStringExtra(getString(R.string.json_data_msg_key)).equalsIgnoreCase(getString(R.string.adm_directive_rewind))) {
                    }
                }
            }
            };


            updateSubscriptionStrip();
            //Show "Push menu button for menu" icon.
            if (appCMSPresenter.getTemplateType() == AppCMSPresenter.TemplateType.SPORTS) {
                findViewById(R.id.press_up_button).setVisibility(View.VISIBLE);
                findViewById(R.id.top_logo).setVisibility(View.VISIBLE);

                findViewById(R.id.footer_logo).setVisibility(View.INVISIBLE);
                showMenuIcon(View.INVISIBLE);
                findViewById(R.id.black_shadow).setVisibility(View.INVISIBLE);

            } else if (appCMSPresenter.isNewsTemplate()) {
                findViewById(R.id.press_up_button).setVisibility(View.INVISIBLE);
                findViewById(R.id.press_down_button).setVisibility(View.INVISIBLE);
                findViewById(R.id.top_logo).setVisibility(View.INVISIBLE);

                findViewById(R.id.footer_logo).setVisibility(View.INVISIBLE);
                showMenuIcon(View.INVISIBLE);
                findViewById(R.id.black_shadow).setVisibility(View.INVISIBLE);
            } else {
                findViewById(R.id.press_up_button).setVisibility(View.INVISIBLE);
                findViewById(R.id.press_down_button).setVisibility(View.INVISIBLE);
                findViewById(R.id.top_logo).setVisibility(View.INVISIBLE);

                findViewById(R.id.footer_logo).setVisibility(View.VISIBLE);
                showMenuIcon(View.VISIBLE);
                findViewById(R.id.black_shadow).setVisibility(View.VISIBLE);
            }
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
            if (appCMSPresenter != null && appCMSPresenter.getAppCMSMain().getFeatures() != null) {
                isEnableMiniPlayer = appCMSPresenter.getAppCMSMain().getFeatures().isEnableMiniPlayer();
                if (isEnableMiniPlayer) {
                    animBottomSlide = AnimationUtils.loadAnimation(this, R.anim.mini_player_slide_bottom);
                }
            }
        }
        setupIAPOnCreate();
        if (appCMSPresenter.isSignupFlag() && appCMSPresenter.isAmazonPurchaseInitiated()) {
            if (appCMSPresenter.isPersonalizationEnabled()) {
                if (!appCMSPresenter.isRecommendationOnlyForSubscribedEnabled()) {
                    appCMSPresenter.openRecommendationDialog(false);
                } else if (appCMSPresenter.isUserSubscribed()){
                    appCMSPresenter.openRecommendationDialog(false);
                }
            }
            appCMSPresenter.setAmazonPurchaseInitiated(false);
        }
    }

    private void checkCleverTapSDK() {
        if (appCMSPresenter.checkCleverTapAvailability() && appCMSPresenter.getCleverTapInstance() != null) {
            if (!appCMSPresenter.getCleverTapInstance().isInitialize()) {
                appCMSPresenter.initializeCleverTap();
                if (updatedAppCMSBinder != null && updatedAppCMSBinder.getScreenName() != null) {
                    appCMSPresenter.sendPageViewEvent("", updatedAppCMSBinder.getScreenName(), null);
                }
            }
            // checkUnreadInboxMessageCount(); // this method is sued only in Phone
        }
    }
    private void showMenuIcon(int visibility) {
        try {
            if (null != findViewById(R.id.info_icon)) {
                ImageView imageView = (ImageView) findViewById(R.id.info_icon);
                imageView.setVisibility(
                        appCMSPresenter.isLeftNavigationEnabled() ? View.INVISIBLE : visibility
                );
                imageView.getBackground().setTint(Utils.getComplimentColor(appCMSPresenter.getGeneralBackgroundColor()));
            }
        } catch (Exception e) {

        }
    }

    private void startSplashActivity() {
        Intent intent = new Intent(AppCmsHomeActivity.this, AppCmsTVSplashActivity.class);
        startActivity(intent);
    }

    private void updateSubscriptionStrip() {
        /*Check Subscription in case of SPORTS TEMPLATE*/
        if (appCMSPresenter.getTemplateType() == AppCMSPresenter.TemplateType.SPORTS && !appCMSPresenter.getAppCMSMain().getFeatures().isWebSubscriptionOnly()) {
            if (appCMSPresenter.isAppSVOD()) {
                if (!appCMSPresenter.isUserLoggedIn()) {
                    setSubscriptionText(false);
                } else {

                    if (appCMSPresenter.getCurrentActivity() != null) {
                        appCMSPresenter.getSubscriptionData(appCMSUserSubscriptionPlanResult -> {
                            try {
                                if (appCMSUserSubscriptionPlanResult != null) {
                                    String subscriptionStatus = appCMSUserSubscriptionPlanResult.getSubscriptionInfo().getSubscriptionStatus();
                                    if (subscriptionStatus.equalsIgnoreCase("COMPLETED") ||
                                            subscriptionStatus.equalsIgnoreCase("DEFERRED_CANCELLATION")) {
                                        setSubscriptionText(true);
                                    } else {
                                        setSubscriptionText(false);
                                    }
                                } else {
                                    setSubscriptionText(false);
                                }
                            } catch (Exception e) {
                                setSubscriptionText(false);
                            }
                        }, false);
                    }
                }
            } else {
                setSubscriptionText(true);
            }
        } else {
            findViewById(R.id.subscribe_now_strip).setVisibility(View.GONE);
        }
    }

    private void setSubscriptionText(boolean isSubscribe) {
        String message = getResources().getString(R.string.blank_string);
        if (!isSubscribe) {
            if (appCMSPresenter.getNavigation().getSettings().getPrimaryCta().getPlacement().equalsIgnoreCase("banner")) {
                if (null != appCMSPresenter.getNavigation()
                        && null != appCMSPresenter.getNavigation().getSettings()
                        && null != appCMSPresenter.getNavigation().getSettings().getLocalizationMap()
                        && null != appCMSPresenter.getNavigation().getSettings().getLocalizationMap().get(appCMSPresenter.getLanguage().getLanguageCode())
                        && null != appCMSPresenter.getNavigation().getSettings().getLocalizationMap().get(appCMSPresenter.getLanguage().getLanguageCode()).getPrimaryCta()
                        && null != appCMSPresenter.getNavigation().getSettings().getLocalizationMap().get(appCMSPresenter.getLanguage().getLanguageCode()).getPrimaryCta().getCtaText()) {
                    message = appCMSPresenter.getNavigation().getSettings().getLocalizationMap().get(appCMSPresenter.getLanguage().getLanguageCode()).getPrimaryCta().getCtaText();
                    if (message.length() == 0) {
                        message = appCMSPresenter.getLanguageResourcesFile().getUIresource(getResources().getString(R.string.watch_live_text));
                    }
                }
            }

            TextView textView = (TextView) findViewById(R.id.subscribe_now_strip);
            textView.setText(message);
            textView.setBackgroundColor(Color.parseColor(Utils.getFocusColor(this, appCMSPresenter)));
            textView.setTextColor(Color.parseColor(Utils.getTextColor(this, appCMSPresenter)));
            textView.setGravity(Gravity.CENTER);
            RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) textView.getLayoutParams();
            if (message.length() == 0) {
                layoutParams.height = 10;
            } else {
                layoutParams.height = 40;
            }
            textView.setLayoutParams(layoutParams);
        } else {
            TextView textView = (TextView) findViewById(R.id.subscribe_now_strip);
            textView.setBackgroundColor(Color.parseColor(Utils.getFocusColor(this, appCMSPresenter)));
            RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) textView.getLayoutParams();
            layoutParams.height = 10;
            textView.setLayoutParams(layoutParams);

        }
    }

    private void handleProfileFragmentAction(AppCMSBinder updatedAppCMSBinder) {
        String tag = getTag(updatedAppCMSBinder);
        appCMSBinderMap.put(tag, updatedAppCMSBinder);
        appCMSBinderStack.push(tag);
        Fragment previousFragment = getSupportFragmentManager().findFragmentById(R.id.home_placeholder);
        if (null != previousFragment && previousFragment instanceof AppCmsMyProfileFragment &&
                !Arrays.asList(getResources().getStringArray(R.array.app_cms_setting_page_content_key)).contains(updatedAppCMSBinder.getPageName())) {
            getSupportFragmentManager().popBackStack();
            appCMSBinderStack.pop();
        }
        appCMSPresenter.sendGaScreen(updatedAppCMSBinder.getScreenName());
        AppCmsMyProfileFragment appCmsMyProfileFragment = AppCmsMyProfileFragment.newInstance(this, updatedAppCMSBinder);
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.home_placeholder, appCmsMyProfileFragment, tag).addToBackStack(tag).commitAllowingStateLoss();
        appCMSPresenter.sendAppsFlyerPageViewEvent(updatedAppCMSBinder.getPageName(), updatedAppCMSBinder.getPageId());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == AppCMSPresenter.PLAYER_REQUEST_CODE) {
            updateSubscriptionStrip();
            Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.home_placeholder);
            if (null != fragment && fragment instanceof AppCmsTVPageFragment) {
                ((AppCmsTVPageFragment) fragment).refreshBrowseFragment();
                ((AppCmsTVPageFragment) fragment).refreshPage();
                ((AppCmsTVPageFragment) fragment).refreshShowDetailEpisodes();
                AppCmsTVPageFragment appCmsTVPageFragment = ((AppCmsTVPageFragment) fragment);
                AppCMSBinder appCmsBinder = appCMSBinderMap.get(appCmsTVPageFragment.getTag());
                appCMSPresenter.sendAppsFlyerPageViewEvent(appCmsBinder.getPageName(), appCmsBinder.getPageId());
                if (appCmsBinder.getPageName()
                        .equalsIgnoreCase(getString(R.string.app_cms_history_navigation_title))) {
                    ((AppCmsTVPageFragment) fragment).updateAdapterData(appCmsBinder);
                }
            } else if (null != fragment && fragment instanceof AppCmsMyProfileFragment) {
                AppCmsMyProfileFragment profileFragment = ((AppCmsMyProfileFragment) fragment);
                AppCMSBinder appCmsBinder = appCMSBinderMap.get(profileFragment.getTag());
                appCMSPresenter.sendAppsFlyerPageViewEvent(appCmsBinder.getPageName(), appCmsBinder.getPageId());
                ((AppCmsMyProfileFragment) fragment).updateAdapterData(appCmsBinder);
            }

            if (resultCode == OPEN_SEARCH_RESULT_CODE) {
                new Handler().postDelayed(() -> {
                    appCMSPresenter.openSearch("Search", "Search",
                            data.getStringExtra(getString(R.string.json_seek_search_string_key)));
                }, 0);
            }
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        registerReceivers();
        isActive = true;
        appCMSPresenter.tryLaunchingPlayerFromDeeplink(null);
        if (findViewById(R.id.trailer_view_id) != null && trailerID != null) {
            setCarouselPlayerTask(this, appCMSPresenter, findViewById(R.id.trailer_view_id), trailerID, "",null);
        }
        setUpIAPOnResume();

        if (appCMSPresenter.getShouldCheckTVUpgrade() &&
                (appCMSPresenter.isAppUpgradeAvailable() || appCMSPresenter.isAppBelowMinVersion())) {
            setUpUpdate();
            appCMSPresenter.setShouldCheckTVUpgrade(false);
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if (intent != null && intent.getAction() != null && intent.getData() != null) {
            if (intent.getAction().equalsIgnoreCase(getString(R.string.LAUNCHER_DEEPLINK_ACTION))) {
                appCMSPresenter.tryLaunchingPlayerFromDeeplink(null);
            }
        }
    }

    private void registerReceivers() {
        registerReceiver(presenterActionReceiver, new IntentFilter(AppCMSPresenter.PRESENTER_NAVIGATE_ACTION));
        registerReceiver(presenterActionReceiver, new IntentFilter(AppCMSPresenter.PRESENTER_PAGE_LOADING_ACTION));
        registerReceiver(presenterActionReceiver, new IntentFilter(AppCMSPresenter.PRESENTER_STOP_PAGE_LOADING_ACTION));
        registerReceiver(presenterActionReceiver, new IntentFilter(AppCMSPresenter.PRESENTER_RESET_NAVIGATION_ITEM_ACTION));
        registerReceiver(presenterActionReceiver, new IntentFilter(AppCMSPresenter.PRESENTER_DIALOG_ACTION));
        registerReceiver(presenterActionReceiver, new IntentFilter(AppCMSPresenter.PRESENTER_CLEAR_DIALOG_ACTION));
        registerReceiver(presenterActionReceiver, new IntentFilter(AppCMSPresenter.SEARCH_ACTION));
        registerReceiver(presenterActionReceiver, new IntentFilter(AppCMSPresenter.LIBRARY_ACTION));
        registerReceiver(presenterActionReceiver, new IntentFilter(AppCMSPresenter.LIBRARY_DATA));
        registerReceiver(presenterActionReceiver, new IntentFilter(CLOSE_DIALOG_ACTION));
        registerReceiver(presenterActionReceiver, new IntentFilter(AppCMSPresenter.ERROR_DIALOG_ACTION));
        registerReceiver(presenterActionReceiver, new IntentFilter(AppCMSPresenter.ACTION_RESET_PASSWORD));
        registerReceiver(presenterActionReceiver, new IntentFilter(AppCMSPresenter.UPDATE_SUBSCRIPTION));
        registerReceiver(presenterActionReceiver, new IntentFilter(AppCMSPresenter.SWITCH_SEASON_ACTION));
        registerReceiver(presenterActionReceiver, new IntentFilter(AppCMSPresenter.ACTION_LINK_YOUR_ACCOUNT));
        registerReceiver(presenterActionReceiver, new IntentFilter(AppCMSPresenter.ACTION_CHANGE_LANGUAGE));
        registerReceiver(presenterActionReceiver, new IntentFilter(AppCMSPresenter.ENTITLEMENT_LOGIN_DIALOG));
        registerReceiver(presenterActionReceiver, new IntentFilter(AppCMSPresenter.RESTORE_PURCHASE_DIALOG));
        registerReceiver(presenterActionReceiver, new IntentFilter(AppCMSPresenter.SUBSCRIPTION_DIALOG));
        registerReceiver(presenterActionReceiver, new IntentFilter(AppCMSPresenter.ACCOUNT_DETAILS_EDIT_INFO_DIALOG));
        registerReceiver(presenterActionReceiver, new IntentFilter(AppCMSPresenter.TVOD_PURCHASE_DIALOG));
        registerReceiver(presenterActionReceiver, new IntentFilter(getString(R.string.intent_msg_action)));
        registerReceiver(presenterActionReceiver, new IntentFilter(AppCMSPresenter.PRESENTER_TRAY_UPDATE_ON_ACTION));
        registerReceiver(presenterActionReceiver, new IntentFilter(STAND_ALONE_PLAYER_REFRESH));
        registerReceiver(presenterActionReceiver, new IntentFilter(AppCMSPresenter.INITIATE_AMAZON_PURCHASE));
        registerReceiver(presenterActionReceiver, new IntentFilter(AppCMSPresenter.RECOMMENDATION_DIALOG_FRAGMENT_TAG));
        registerReceiver(presenterActionReceiver, new IntentFilter(AppCMSPresenter.PRESENTER_OPEN_PIN_DIALOG_ACTION));
        registerReceiver(presenterActionReceiver, new IntentFilter(AppCMSPresenter.GENERIC_DIALOG));
        registerReceiver(presenterActionReceiver, new IntentFilter(AppCMSPresenter.TVOD_PURCHASE));
        registerReceiver(presenterActionReceiver, new IntentFilter(AppCMSPresenter.CHECK_UPDATE_ACTION));
    }

    @Override
    protected void onPause() {
        if (null != presenterActionReceiver) {
            try {
                unregisterReceiver(presenterActionReceiver);
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            }
        }

        //when activity pause then close the full screen player.
        if (appCMSPresenter.isFullScreenVisible) {
            appCMSPresenter.tvVideoPlayerView.getPlayerView().hideController();
            appCMSPresenter.tvVideoPlayerView.getPlayerView().setUseController(false);
            appCMSPresenter.exitFullScreenTVPlayer();
        }
        if (getCustomTVVideoPlayerView() != null) {
            getCustomTVVideoPlayerView().pausePlayer();
        }
        if (getCustomVideoVideoPlayerView1() != null) {
            removeHandlerCallBacks();
        }
        isActive = false;
        super.onPause();

    }

    @Override
    protected void onStop() {
        if (isNavigationVisible()) {
            handleNavigationVisibility();
        }
        super.onStop();
    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {

    }

    private void openErrorDialog(Intent intent) {
        Utils.pageLoading(false, this);
        Bundle bundle = intent.getBundleExtra(getString(R.string.retryCallBundleKey));
        bundle.putBoolean(getString(R.string.retry_key), bundle.getBoolean(getString(R.string.retry_key)));
        bundle.putBoolean(getString(R.string.register_internet_receiver_key), bundle.getBoolean(getString(R.string.register_internet_receiver_key)));
        bundle.putString(getString(R.string.tv_dialog_msg_key), bundle.getString(getString(R.string.tv_dialog_msg_key)));
        bundle.putString(getString(R.string.tv_dialog_header_key), bundle.getString(getString(R.string.tv_dialog_header_key)));

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        AppCmsTvErrorFragment newFragment = AppCmsTvErrorFragment.newInstance(
                bundle);
        newFragment.setErrorListener(this);
        newFragment.show(ft, DIALOG_FRAGMENT_TAG);
    }

    private void openResetPasswordScreen(Intent intent) {
        if (null != intent) {
            Bundle bundle = intent.getBundleExtra(getString(R.string.app_cms_bundle_key));
            if (null != bundle) {
                AppCMSBinder appCMSBinder = (AppCMSBinder) bundle.get(getString(R.string.app_cms_binder_key));
                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                appCmsResetPasswordFragment = AppCmsResetPasswordFragment.newInstance(
                        appCMSBinder);
                appCmsResetPasswordFragment.show(ft, DIALOG_FRAGMENT_TAG);
                Utils.pageLoading(false, this);
                appCMSPresenter.sendAppsFlyerPageViewEvent(appCMSBinder.getPageName(), appCMSBinder.getPageId());
            }
        }
    }

    private void openLoginDialog(Intent intent, boolean isLoginPage) {
        if (null != intent) {
            Bundle bundle = intent.getBundleExtra(getString(R.string.app_cms_bundle_key));
            if (null != bundle) {
                AppCMSBinder appCMSBinder = (AppCMSBinder) bundle.get(getString(R.string.app_cms_binder_key));
                bundle.putBoolean("isLoginPage", isLoginPage);
                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                loginDialog = AppCmsLoginDialogFragment.newInstance(
                        appCMSBinder);
                loginDialog.show(ft, DIALOG_FRAGMENT_TAG);
                Utils.pageLoading(false, this);
                if (appCMSBinder != null) {
                    appCMSPresenter.sendAppsFlyerPageViewEvent(appCMSBinder.getPageName(), appCMSBinder.getPageId());
                }
            }
        }
    }

    private void openSignUpDialog(Intent intent, boolean isLoginPage) {
        if (null != intent) {
            Bundle bundle = intent.getBundleExtra(getString(R.string.app_cms_bundle_key));
            if (null != bundle) {
                AppCMSBinder appCMSBinder = (AppCMSBinder) bundle.get(getString(R.string.app_cms_binder_key));
                bundle.putBoolean("isLoginPage", isLoginPage);
                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                signUpDialog = AppCmsSignUpDialogFragment.newInstance(
                        appCMSBinder);
                signUpDialog.show(ft, DIALOG_FRAGMENT_TAG);
                Utils.pageLoading(false, this);
                if (appCMSBinder != null) {
                    appCMSPresenter.sendAppsFlyerPageViewEvent(appCMSBinder.getPageName(), appCMSBinder.getPageId());
                }
            }
        }
    }

    private void openGenericDialog(Intent intent, boolean isLoginPage) {
        if (null != intent) {
            Bundle bundle = intent.getBundleExtra(getString(R.string.app_cms_bundle_key));
            if (null != bundle) {
                AppCMSBinder appCMSBinder = (AppCMSBinder) bundle.get(getString(R.string.app_cms_binder_key));
                bundle.putBoolean("isLoginPage", isLoginPage);
                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                AppCmsGenericDialogFragment newFragment = AppCmsGenericDialogFragment.newInstance(
                        appCMSBinder);
                newFragment.show(ft, DIALOG_FRAGMENT_TAG);
                Utils.pageLoading(false, this);
                if (appCMSBinder != null) {
                    appCMSPresenter.sendAppsFlyerPageViewEvent(appCMSBinder.getPageName(), appCMSBinder.getPageId());
                }
            }
        }
    }

    public void openGenericDialog(Intent intent , ContentDatum contentDatum){
        ClearDialogFragment newFragment = Utils.getClearDialogFragment(
                this,
                appCMSPresenter,
                getResources().getDimensionPixelSize(R.dimen.text_clear_dialog_width),
                getResources().getDimensionPixelSize(R.dimen.text_clear_dialog_height),
                intent.getStringExtra(getString(R.string.tv_dialog_header_key)),
                intent.getStringExtra(getString(R.string.tv_dialog_msg_key)),
                intent.getStringExtra(getString(R.string.tv_dialog_positive_button_key)),
                intent.getStringExtra(getString(R.string.tv_dialog_negative_button_key)),
                 14
        );

        newFragment.setOnPositiveButtonClicked(s -> {
            if(contentDatum != null && contentDatum.getSubscriptionPlans() != null){
                appCMSPresenter.navigateToContentSubscription(contentDatum.getSubscriptionPlans());
            }else{
                Utils.pageLoading(true,this);
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

        newFragment.setOnNegativeButtonClicked(s -> {
            appCMSPresenter.navigateToLibraryPage(appCMSPresenter.getLibraryPage().getPageId(), appCMSPresenter.getLibraryPage().getPageName(), false, false);
            appCMSPresenter.openLibrary(appCMSPresenter.getLibraryPage().getPageId(), appCMSPresenter.getLibraryPage().getPageName());
        });

    }

    private void openAccountDetailsEditInfoDialog(Intent intent) {
        if (null != intent) {
            AccountDetailsEditDialogFragment newFragment = Utils.getAccountEditDialogFragment(
                    this,
                    appCMSPresenter,
                    getResources().getDimensionPixelSize(R.dimen.preview_overlay_dialog_width),
                    getResources().getDimensionPixelSize(R.dimen.preview_overlay_dialog_height),
                    intent.getStringExtra(getString(R.string.tv_dialog_header_key)),
                    null,
                    intent.getStringExtra(getString(R.string.tv_dialog_msg_key)),
                    appCMSPresenter.getLocalisedStrings().getSaveText(),
                    appCMSPresenter.getLocalisedStrings().getCancelText(),
                    null,
                    12f,
                    "#1A1A1A",
                    intent.getStringExtra(getString(R.string.tv_dialog_type_key))
            );
        }
    }

    private void openTVODPurchaseDialog(Intent intent) {
        if (null != intent) {
            if(appCMSPresenter.getModuleApi() != null &&
                    appCMSPresenter.getModuleApi().getContentData() != null &&
                    appCMSPresenter.getModuleApi().getContentData().size() > 0 &&
                    appCMSPresenter.getModuleApi().getContentData().get(0).getSeason() != null &&
                    appCMSPresenter.getModuleApi().getContentData().get(0).getSeason().size() > 0) {
                TVODPurchaseDialogFragment newFragment = Utils.getTVODDialogFragment(
                        this,
                        appCMSPresenter,
                        getResources().getDimensionPixelSize(R.dimen.tvod_dialog_width),
                        getResources().getDimensionPixelSize(R.dimen.tvod_dialog_height),
                        intent.getStringExtra(getString(R.string.tv_dialog_header_key)),
                        intent.getStringExtra(getString(R.string.tv_dialog_msg_key)),
                        12f,
                        intent.getStringExtra(getString(R.string.tv_dialog_type_key)));
            }
        }
    }

    @Override
    public void onErrorScreenClose(boolean closeActivity) {
        /*if (appCmsResetPasswordFragment != null
                && appCmsResetPasswordFragment.isAdded()
                && appCmsResetPasswordFragment.isVisible()) {
            appCmsResetPasswordFragment.dismiss();
        }*/
    }

    @Override
    public void onRetry(Bundle bundle) {
        RetryCallBinder retryCallBinder = (RetryCallBinder) bundle.getBinder(getString(R.string.retryCallBinderKey));
        AppCMSPresenter.RETRY_TYPE retryType = retryCallBinder != null ? retryCallBinder.getRetry_type() : null;
        boolean isTosPage = bundle.getBoolean(getString(R.string.is_tos_dialog_page_key));
        boolean isLoginPage = bundle.getBoolean(getString(R.string.is_login_dialog_page_key));
        if (retryType != null) {
            switch (retryType) {
                case BUTTON_ACTION:
                    appCMSPresenter.launchTVButtonSelectedAction(
                            retryCallBinder.getPagePath(),
                            retryCallBinder.getAction(),
                            retryCallBinder.getFilmTitle(),
                            retryCallBinder.getExtraData(),
                            retryCallBinder.getContentDatum(),
                            retryCallBinder.isCloselauncher(),
                            -1,
                            null,
                            null);
                    break;
                case VIDEO_ACTION:
                    appCMSPresenter.launchTVVideoPlayer(
                            retryCallBinder.getContentDatum(),
                            0,
                            retryCallBinder.getContentDatum().getContentDetails() != null
                                    ? retryCallBinder.getContentDatum().getContentDetails().getRelatedVideoIds()
                                    : null,
                            retryCallBinder.getContentDatum().getGist().getWatchedTime(),
                            null);

                    break;
                case PAGE_ACTION:
                    appCMSPresenter.navigateToTVPage(
                            retryCallBinder.getFilmId(),
                            retryCallBinder.getFilmTitle(),
                            retryCallBinder.getPagePath(),
                            retryCallBinder.isCloselauncher(),
                            Uri.EMPTY,
                            false,
                            isTosPage,
                            isLoginPage, false, false, false);
                    break;

                case SEARCH_RETRY_ACTION:
                    String tag = getString(R.string.app_cms_search_label);
                    Fragment fragment = getSupportFragmentManager().findFragmentByTag(tag);
                    if (fragment instanceof AppCmsSearchFragment) {
                        ((AppCmsSearchFragment) fragment).searchResult(retryCallBinder.getFilmTitle());
                    }
                    break;

                case WATCHLIST_RETRY_ACTION:
                    appCMSPresenter.showLoadingDialog(true);
                    appCMSPresenter.navigateToWatchlistPage(
                            retryCallBinder.getPageId(),
                            retryCallBinder.getFilmTitle(),
                            retryCallBinder.getPagePath(),
                            false, false, false);
                    break;
                case SUB_NAV_RETRY_ACTION:
                    appCMSPresenter.showLoadingDialog(true);
                    appCMSPresenter.navigateToSubNavigationPage(
                            retryCallBinder.getPageId(),
                            retryCallBinder.getFilmTitle(),
                            retryCallBinder.getPagePath(),
                            retryCallBinder.getPrimary(),
                            retryCallBinder.getItems(),
                            null,
                            false);
                    break;
                case HISTORY_RETRY_ACTION:
                    appCMSPresenter.showLoadingDialog(true);
                    appCMSPresenter.navigateToHistoryPage(
                            retryCallBinder.getPageId(),
                            retryCallBinder.getFilmTitle(),
                            retryCallBinder.getPagePath(),
                            false, false);
                    break;
                case RESET_PASSWORD_RETRY:
                    appCMSPresenter.showLoadingDialog(true);
                    appCMSPresenter.resetPassword(retryCallBinder.getFilmTitle(), null); //filmtitle here means emailid.
                    break;

                case LOGOUT_ACTION:
                    appCMSPresenter.logoutTV();
                    break;

                case EDIT_WATCHLIST:
                    if (appCMSPresenter.isNetworkConnected()) {
                        appCMSPresenter.editWatchlist(retryCallBinder.getContentDatum(),
                                retryCallBinder.getCallback(),
                                !bundle.getBoolean("queued"), true, null);
                    } else {
                        appCMSPresenter.openErrorDialog(retryCallBinder.getContentDatum(),
                                bundle.getBoolean("queued"),
                                retryCallBinder.getCallback());
                    }
                    break;
            }
        }
    }

    private String getTag(AppCMSBinder appCMSBinder) {
        String key = null;
        if (appCMSBinder != null && !appCMSPresenter.isPagePrimary(appCMSBinder.getPageId())) {
            key = appCMSBinder.getPageId() + appCMSBinder.getScreenName();
        } else if (appCMSBinder != null) {
            key = appCMSBinder.getPageId();
        }
        return key;
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
    }

    private void selectNavItem(String pageId) {
        //Log.d(TAG , "Nav Pageid = "+pageId);
        navigationFragment.setSelectedPageId(pageId, navigationPlaceholderFramelayout != null ? navigationPlaceholderFramelayout.getNavigationState().equals(NAVIGATION_STATE_EXPANDED) : false);
    }

    @Override
    protected void onDestroy() {
        try {
            unregisterReceiver(updateHistoryDataReciever);
            unregisterReceiver(updateWatchListDataReceiver);
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            appCMSPresenter.getPlayerLruCache().evictAll();

            if (getCustomTVVideoPlayerView() != null) {
                getCustomTVVideoPlayerView().releasePlayer();
            }
            setCustomTVVideoPlayerView(null);
        } catch (Exception e) {
            e.printStackTrace();
        }
        super.onDestroy();
    }

    private void handleLaunchPageAction(AppCMSBinder appCMSBinder) {

        int distanceFromStackTop = -1;
        String tag = getTag(appCMSBinder);

        distanceFromStackTop = appCMSBinderStack.search(tag);
        appCMSBinderStack.push(tag);
        appCMSBinderMap.put(tag, appCMSBinder);

        showInfoIcon(appCMSBinder.getPageId());
        //Log.d(TAG, "Launching new page: " + appCMSBinder.getPageName());
        appCMSPresenter.sendGaScreen(appCMSBinder.getScreenName());
        boolean isPoped = getSupportFragmentManager().popBackStackImmediate(/*appCMSBinder.getPageId()*/tag, 1);

        if (isPoped) {
            if (appCMSBinderStack.contains(getTag(appCMSBinder)))
                appCMSBinderStack.remove(getTag(appCMSBinder));
        }
        //if(!isPoped)
        setPageFragment(updatedAppCMSBinder);
        //else
        //selectNavItem(updatedAppCMSBinder.getPageId());
    }

    private Fragment getTopFragment() {
        try {
            FragmentManager fragmentManager = getSupportFragmentManager();
            if (fragmentManager.getBackStackEntryCount() < 0) return null;
        return fragmentManager.findFragmentByTag(fragmentManager.getBackStackEntryAt(fragmentManager.getBackStackEntryCount() - 1).getName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void onBackPressed() {

        if (appCMSPresenter.isFullScreenVisible) {
            appCMSPresenter.tvVideoPlayerView.getPlayerView().hideController();
            appCMSPresenter.tvVideoPlayerView.getPlayerView().setUseController(false);
            appCMSPresenter.exitFullScreenTVPlayer();
            return;
        }

        //if navigation is visible then first hide the navigation.
        if (appCMSPresenter.isNewsLeftNavigationEnabled()) {
            if (isNavigationExpanded()) {
                handleNavigationVisibility();
                return;
            }
        } else {
            if (isNavigationVisible()) {
                handleNavigationVisibility();
                return;
            }
        }
        if (updatedAppCMSBinder != null)
            appCMSPresenter.removeFromPageMap(updatedAppCMSBinder.getScreenName());

       /* if (isSubNavigationVisible()) {
            if (appCmsSubNavigationFragment.isTeamsShowing()) {
                handleNavigationVisibility();
                showSubNavigation(false, false);
            } else {
                handleNavigationVisibility();
                showSubNavigation(false, true);
            }
            return;
        }*/

        /*if (appCMSPresenter.getTemplateType() == AppCMSPresenter.TemplateType.SPORTS) {
            if (null != getTopFragment() && getTopFragment() instanceof AppCmsMyProfileFragment) {
                showSubNavigation(true, false);
            }
        }*/



        if (getSupportFragmentManager().getBackStackEntryCount() <= 1) {

            ClearDialogFragment newFragment = Utils.getClearDialogFragment(this, appCMSPresenter,
                    getResources().getDimensionPixelSize(R.dimen.text_clear_dialog_width),
                    getResources().getDimensionPixelSize(R.dimen.text_clear_dialog_height),
                    "",
                    appCMSPresenter.getLocalisedStrings().getAppExitAlertMessage(),
                    appCMSPresenter.getLocalisedStrings().getNoText(),
                    appCMSPresenter.getLocalisedStrings().getYesText(), 14
            );

            newFragment.setOnPositiveButtonClicked(s -> newFragment.dismiss());

            newFragment.setOnNegativeButtonClicked(s -> {
                if (updatedAppCMSBinder != null)
                    appCMSPresenter.removeFromPageMap(updatedAppCMSBinder.getScreenName());

                if (appCMSBinderStack.size() > 0) {
                    appCMSBinderStack.pop();
                }
                finish();
            });
            newFragment.setOnBackKeyListener(s -> newFragment.dismiss());
        } else {

            if (appCMSBinderStack.size() > 0) {
                appCMSBinderStack.pop();
            }

            if (appCMSBinderStack.size() > 0) {
                AppCMSBinder appCMSBinder = appCMSBinderMap.get(appCMSBinderStack.peek());
                if (appCMSBinder != null) {
                    appCMSPresenter.sendAppsFlyerPageViewEvent(appCMSBinder.getPageName(), appCMSBinder.getPageId());
                }
                if (appCMSBinderStack.peek().equalsIgnoreCase(getString(R.string.search_pageid))
                        || appCMSBinderStack.peek().equalsIgnoreCase(getString(R.string.my_profile_pageid))) {
                    selectNavItem(appCMSBinderStack.peek());
                    showInfoIcon(appCMSBinderStack.peek());
                } else {
                    updatedAppCMSBinder = appCMSBinder;
                    String pageId = null;
                    if (null != updatedAppCMSBinder) {

                    }
                    if (null == pageId) {
                        pageId = appCMSBinderStack.peek();
                    }
                    selectNavItem(pageId);
                    showInfoIcon(pageId);
                }
            }
            setPageName(null);
            super.onBackPressed();

            if (getSupportFragmentManager().getBackStackEntryCount() == 0) {
                finish();
            }
        }
    }

   /* @Override
    public void showSubNavigation(boolean shouldShow, boolean showTeams) {
        new Handler().post(() -> {
            *//*subNavHolder.setVisibility(shouldShow ? View.VISIBLE : View.GONE);*//*
            shadowView.setVisibility(shouldShow ? View.VISIBLE : View.GONE);
            appCmsSubNavigationFragment.setFocusable(shouldShow);
            if (shouldShow) {
                // navigationFragment.setSelectorColor();
                appCmsSubNavigationFragment.notifyDataSetInvalidate(showTeams);
            }
        });
    }*/

    private void setPageFragment(AppCMSBinder appCMSBinder) {
        Fragment attached = getSupportFragmentManager().findFragmentById(R.id.home_placeholder);
        if (attached == null || (attached != null && !attached.getTag().equalsIgnoreCase(getTag(appCMSBinder)))) {
            AppCmsTVPageFragment appCMSPageFragment = AppCmsTVPageFragment.newInstance(this, appCMSBinder);
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            String tag = getTag(appCMSBinder);
            fragmentTransaction.replace(R.id.home_placeholder, appCMSPageFragment, tag).addToBackStack(tag).commitAllowingStateLoss();
            appCMSPresenter.sendAppsFlyerPageViewEvent(appCMSBinder.getPageName(), appCMSBinder.getPageId());
        } else {
            if (appCMSBinderStack.contains(getTag(appCMSBinder)))
                appCMSBinderStack.remove(getTag(appCMSBinder));
            if (null != appCMSPresenter)
                appCMSPresenter.sendStopLoadingPageAction(false, null);
        }
        selectNavItem(appCMSBinder.getPageId());
        if (!appCMSPresenter.getNavigationModulePref()) {
            toggleLeftNavigationModule(true);
            appCMSPresenter.setNavigationModuleFirstTime(true);
        } else {
            toggleLeftNavigationModule(false);
        }
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
//        Log.d(TAG, "dispatchKeyEvent() called with: event = [" + event + "]");
        Log.d(TAG, "() called with: Focus = [" + getCurrentFocus() + "]");
        int keyCode = event.getKeyCode();
        int action = event.getAction();
        if (appCMSPresenter.isFullScreenVisible) {
            appCMSPresenter.tvVideoPlayerView.getPlayerView().showController();
            switch (action) {
                case KeyEvent.ACTION_DOWN:
                    switch (keyCode) {
                        case KeyEvent.KEYCODE_MEDIA_PLAY_PAUSE:
                            appCMSPresenter.tvVideoPlayerView.findViewById(R.id.exo_pause).requestFocus();
                            appCMSPresenter.tvVideoPlayerView.findViewById(R.id.exo_play).requestFocus();
                            if (appCMSPresenter.tvVideoPlayerView.getPlayerView() != null) {
                                appCMSPresenter.tvVideoPlayerView.setHardPause(appCMSPresenter.tvVideoPlayerView.getPlayer().getPlayWhenReady());
                                if (appCMSPresenter.tvVideoPlayerView.getPlayer().getPlayWhenReady()) {
                                    appCMSPresenter.tvVideoPlayerView.getPlayerView().getPlayer().seekTo(appCMSPresenter.tvVideoPlayerView.getPlayer().getContentPosition() + 1000);
                                }
                                return super.dispatchKeyEvent(event)
                                        || appCMSPresenter.tvVideoPlayerView.getPlayerView()
                                        .dispatchKeyEvent(event);
                            }
                            break;
                        case KeyEvent.KEYCODE_DPAD_CENTER:
                            if (appCMSPresenter.tvVideoPlayerView.getPlayerView() != null
                                    && (findViewById(R.id.exo_play).hasFocus()
                                    || findViewById(R.id.exo_pause).hasFocus())) {
                                appCMSPresenter.tvVideoPlayerView.setHardPause(appCMSPresenter.tvVideoPlayerView.getPlayer().getPlayWhenReady());
                                if (appCMSPresenter.tvVideoPlayerView.getPlayer().getPlayWhenReady()) {
                                    appCMSPresenter.tvVideoPlayerView.getPlayerView().getPlayer().seekTo(appCMSPresenter.tvVideoPlayerView.getPlayer().getContentPosition() + 1000);
                                }
                            }
                            return super.dispatchKeyEvent(event);
                        case KeyEvent.KEYCODE_MEDIA_REWIND:
                            if (null != appCMSPresenter.tvVideoPlayerView) {
                                if (appCMSPresenter.tvVideoPlayerView.isLiveStream()) return true;
                                appCMSPresenter.tvVideoPlayerView.findViewById(R.id.exo_rew).requestFocus();
                                return super.dispatchKeyEvent(event);
                            }
                            break;
                        case KeyEvent.KEYCODE_MEDIA_FAST_FORWARD:
                            if (null != appCMSPresenter.tvVideoPlayerView) {
                                if (appCMSPresenter.tvVideoPlayerView.isLiveStream()) return true;
                                appCMSPresenter.tvVideoPlayerView.findViewById(R.id.exo_ffwd).requestFocus();
                                return super.dispatchKeyEvent(event);
                            }
                        case KeyEvent.KEYCODE_DPAD_UP:
                            if (findViewById(R.id.exo_pause).hasFocus() ||
                                    findViewById(R.id.exo_play).hasFocus() ||
                                    findViewById(R.id.exo_ffwd).hasFocus() ||
                                    findViewById(R.id.exo_rew).hasFocus()) {
                                return true;
                            } else {
                                return super.dispatchKeyEvent(event);
                            }
                        default:
                            return super.dispatchKeyEvent(event);
                    }
                default:
                    return super.dispatchKeyEvent(event);
            }
        }

        switch (action) {
            case KeyEvent.ACTION_DOWN:
                switch (keyCode) {
                    case KeyEvent.KEYCODE_MENU:
                    case KeyEvent.KEYCODE_0:
                        if (!appCMSPresenter.isLeftNavigationEnabled()) {
                            handleNavigationVisibility();
                            hideFooterControl();
                        }
                        toggleLeftNavigationModule(false);
                        return true;
                    case KeyEvent.KEYCODE_MEDIA_PLAY_PAUSE:
                    case KeyEvent.KEYCODE_MEDIA_PLAY:
                        handlePlayRemoteKey();
                        break;
                    case KeyEvent.KEYCODE_DPAD_DOWN:
                        //if navigation fragment is open then hold down key event otherwise pass it.
                        if (!appCMSPresenter.isLeftNavigationEnabled()
                                && isNavigationVisible()
                                && (navigationFragment.getNavMenuSubscriptionModule() != null
                                && !navigationFragment.getNavMenuSubscriptionModule().isFocused())) {
                            handleNavigationVisibility();
                            return true;
                        }
                        break;

                    case KeyEvent.KEYCODE_DPAD_LEFT:
                        if (appCMSPresenter.isLeftNavigationEnabled()) {
                            if (getTopFragment() instanceof BaseFragment) {
                                BaseFragment baseFragment = (BaseFragment) getTopFragment();
                                if (baseFragment.isSubNavExist()) {
                                    if (appCMSPresenter.isNewsLeftNavigationEnabled() && shouldShowLeftNav) {
                                        if (!isNavigationExpanded()) {
                                            handleNavigationVisibility();
                                            hideFooterControl();
                                        }
                                    } else if (baseFragment.isSubNavigationVisible()) {
                                        baseFragment.showSubNavigation(false);
                                    } else if (shouldShowSubLeftNav) {
                                        baseFragment.showSubNavigation(true);
                                    }
                                } else if (shouldShowLeftNav) {
                                    if (appCMSPresenter.isNewsLeftNavigationEnabled()) {
                                        if (!isNavigationExpanded()) {
                                            if (!Utils.isPlayerSelected && findViewById(R.id.video_player_id) != null) {
                                                findViewById(R.id.video_player_id).setFocusable(false);
                                            }
                                            handleNavigationVisibility();
                                            hideFooterControl();
                                        }
                                    } else {
                                        handleNavigationVisibility();
                                        hideFooterControl();
                                    }
                                }
                            }
                        }

                        if (!isNavigationVisible()) {
                            toggleLeftNavigationModule(shouldShowLeftNav);
                        }

                        break;
                    case KeyEvent.KEYCODE_DPAD_RIGHT:
                        if (appCMSPresenter.isLeftNavigationEnabled()) {
                            if (getTopFragment() instanceof BaseFragment) {
                                BaseFragment baseFragment = (BaseFragment) getTopFragment();
                                if (baseFragment.isSubNavExist()) {
                                    if (isNavigationExpanded()) {
                                        handleNavigationVisibility();
                                        hideFooterControl();
                                    } else if (baseFragment.isSubNavigationVisible()) {
                                        if (appCMSPresenter.isNewsTemplate()) {
                                            baseFragment.showSubNavigation(true);
                                        } else {
                                            baseFragment.showSubNavigation(false);
                                        }
                                    }
                                } else if (isNavigationExpanded() && appCMSPresenter.isNewsLeftNavigationEnabled()) {
                                    if (findViewById(R.id.video_player_id) != null) {
                                        findViewById(R.id.video_player_id).setFocusable(true);
                                    }
                                    handleNavigationVisibility();
                                    hideFooterControl();
                                    navigationFragment.setFocusable(false);
                                    return super.dispatchKeyEvent(event);
                                } else if (isNavigationVisible()) {
                                    handleNavigationVisibility();
                                    hideFooterControl();
                                }
                            }
                        }
                        if (isNavigationModuleVisible()) {
                            toggleLeftNavigationModule(false);

                        }
                        break;
                    default:
                        break;
                }
                break;
            default:
                break;
            case KeyEvent.ACTION_UP:
                switch (keyCode) {
                    case KeyEvent.KEYCODE_MENU:
                        return true;
                }
        }
        return super.dispatchKeyEvent(event);
    }

    private boolean isNavigationModuleVisible() {
        RelativeLayout leftNavModuleParentContainer = findViewById(R.id.left_navigation_parent_container);
        if (leftNavModuleParentContainer != null) {
            return leftNavModuleParentContainer.getVisibility() == View.VISIBLE;
        }
        return false;
    }

    private Headers pageHeader;

    public boolean isPageHeader(String pageId) {
        List<Headers> headerList = appCMSPresenter.getAppCMSAndroid().getHeaders();
        if (headerList != null) {
            for (Headers header : headerList) {
                List<NavigationPrimary> headerItemData = header.getData();
                for (NavigationPrimary headerItemData1 : headerItemData) {
                    if (headerItemData1.getPageId() != null && pageId.contains(headerItemData1.getPageId())) {
                        pageHeader = header;
                        return true;
                    }
                }
            }
        }
        return false;
    }


    public void toggleLeftNavigationModule(boolean b) {
        try {
            boolean isNavigationModule = false;
            String pageId = appCMSBinderStack.peek();
            String headerPageNavId = null;
            AppCMSBinder appCMSBinder = appCMSBinderMap.get(pageId);
            if (appCMSBinder != null) {
                List<ModuleList> moduleLists = appCMSBinder.getAppCMSPageUI().getModuleList();
                for (ModuleList module : moduleLists) {
                    if (module.getType() != null && module.getType().equalsIgnoreCase("AC CustomNavigation 01")) {
                        isNavigationModule = true;
                        headerPageNavId = module.getSettings() != null ? module.getSettings().getNavigationId() : null;
                        pageHeader = null;
                        break;
                    } else if (isPageHeader(pageId) && !appCMSPresenter.isLeftNavigationEnabled()) {
                        isNavigationModule = true;
                        headerPageNavId = null;
                        break;
                    }
                }
            }

            if (isNavigationModule) {
                if (headerPageNavId != null) {
                    pageHeader = appCMSPresenter.getHeaderObj(headerPageNavId);
                }
            }

            RelativeLayout leftNavParentContaineer = findViewById(R.id.left_navigation_parent_container);
            if (isNavigationModule && b) {
                RelativeLayout navRl = (RelativeLayout) leftNavParentContaineer.getChildAt(1);
                RecyclerView recyclerView = (RecyclerView) navRl.getChildAt(0);
                if (recyclerView != null) {
                    leftNavParentContaineer.setVisibility(View.VISIBLE);
                    RecyclerView.ViewHolder holder = recyclerView.findViewHolderForLayoutPosition(0);
                    LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                    new Handler().post(() -> {
                        if (linearLayoutManager.findViewByPosition(0) != null)
                            linearLayoutManager.findViewByPosition(0).requestFocus();
                    });
                } else {
                    leftNavParentContaineer.setVisibility(View.GONE);
                    leftNavParentContaineer.getChildAt(1).clearFocus();
                }
            } else {
                leftNavParentContaineer.setVisibility(View.GONE);
                leftNavParentContaineer.getChildAt(1).clearFocus();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void handleNavigationVisibility() {
       // fix for RTA no need to add condition for RTA , as left menu is also showing in History or otehr pages
        if ((!appCMSBinderStack.isEmpty() && (appCMSPresenter.isPagePrimary(appCMSBinderStack.peek()) || appCMSPresenter.isPageHeader(appCMSBinderStack.peek())))||appCMSPresenter.isNewsTemplate()) {
            Fragment parentFragment = getSupportFragmentManager().findFragmentById(R.id.home_placeholder);
            AppCmsBrowseFragment browseFragment = null;
            if (null != parentFragment) {
                if (parentFragment instanceof AppCmsTVPageFragment || parentFragment instanceof AppCmsSearchFragment || parentFragment instanceof AppCmsMyProfileFragment) {
                    Fragment fragment = parentFragment.getChildFragmentManager().
                            findFragmentById(R.id.appcms_browsefragment);
                    if (fragment instanceof AppCmsBrowseFragment) {
                        browseFragment = (AppCmsBrowseFragment) fragment;
                        if (appCMSPresenter.isNewsLeftNavigationEnabled()) {
                            if (isNavigationExpanded()) {
                                showNavigation(false);
                            } else {
                                showNavigation(true);
                            }
                        } else {
                            if (isNavigationVisible()) {
                                showNavigation(false);
                                if (null != browseFragment && null != browseFragment.getCustomVideoVideoPlayerView() /*&& !isSubNavigationVisible()*/) {
                                    browseFragment.getCustomVideoVideoPlayerView().resumePlayer();
                                }
                            } else {
                                if (null != browseFragment && null != browseFragment.getCustomVideoVideoPlayerView()) {
                                    browseFragment.getCustomVideoVideoPlayerView().pausePlayer();
                                }
                                showNavigation(true);
                            }
                        }
                    } else {
                        if (appCMSPresenter.isNewsLeftNavigationEnabled()) {
                            if (isNavigationExpanded()) {
                                showNavigation(false);
                            } else {
                                showNavigation(true);
                            }
                        } else {
                            if (isNavigationVisible()) {
                                showNavigation(false);
                            } else {
                                showNavigation(true);
                            }
                        }
                    }
                }
            }
        }
    }

    public void keyPressed(View v) {
        String tag = appCMSBinderStack.peek();//getString(R.string.app_cms_search_label);
        Fragment fragment = getSupportFragmentManager().findFragmentByTag(tag);
        if (fragment instanceof AppCmsSearchFragment) {
            ((AppCmsSearchFragment) fragment).keyPressed(v);
        }
    }

    private void handlePlayRemoteKey() {
        Fragment parentFragment = getSupportFragmentManager().findFragmentById(R.id.home_placeholder);
        if (null != parentFragment) {
            AppCmsBrowseFragment browseFragment = null;
            AppCMSVerticalGridFragment verticalGridFragment = null;
            if (parentFragment instanceof AppCmsTVPageFragment) {
                browseFragment = (AppCmsBrowseFragment) parentFragment.getChildFragmentManager().
                        findFragmentById(R.id.appcms_browsefragment);
            } else if (parentFragment instanceof AppCmsSearchFragment) {

                if (parentFragment.getChildFragmentManager().
                        findFragmentById(R.id.appcms_search_results_container) instanceof AppCmsBrowseFragment) {
                    browseFragment = (AppCmsBrowseFragment) parentFragment.getChildFragmentManager().
                            findFragmentById(R.id.appcms_search_results_container);
                } else if (parentFragment.getChildFragmentManager().
                        findFragmentById(R.id.appcms_search_results_container) instanceof AppCMSVerticalGridFragment) {
                    verticalGridFragment = (AppCMSVerticalGridFragment) parentFragment.getChildFragmentManager().
                            findFragmentById(R.id.appcms_search_results_container);
                }

            }
            if (null != browseFragment && browseFragment.hasFocus()) {
                browseFragment.pushedPlayKey();
            } else if (verticalGridFragment != null && verticalGridFragment.hasFocus()) {
                verticalGridFragment.pushedPlayKey();
            }
        }
    }

    @Override
    public void showNavigation(final boolean shouldShow) {
        new Handler().post(() -> {
            if (appCMSPresenter.isNewsLeftNavigationEnabled()) {
                navigationPlaceholderFramelayout.setNavigationState(
                        shouldShow
                                ? NAVIGATION_STATE_EXPANDED
                                : NAVIGATION_STATE_CLOSED);
                navigationFragment.setLeftMenuState(shouldShow);
//                shadowView.setVisibility(shouldShow ? View.GONE : View.GONE);
                if (updatedAppCMSBinder != null && (appCMSPresenter.isPagePrimary(updatedAppCMSBinder.getPageId())
                        || appCMSPresenter.isPageUser(updatedAppCMSBinder.getPageId())
                        || appCMSPresenter.isPageFooter(updatedAppCMSBinder.getPageId()))) {
                    navigationPlaceholderFramelayout.setVisibility(View.VISIBLE);
                    navParentContainer.setVisibility(View.VISIBLE);
                    navigationFragment.setFocusable(shouldShow);
                }
                if (shouldShow) {
                    navigationFragment.notifyDataSetInvalidate();
                }
            } else {
                navHolder.setVisibility(shouldShow ? View.VISIBLE : View.GONE);
                shadowView.setVisibility(shouldShow ? View.VISIBLE : View.GONE);
                if (appCMSPresenter.isLeftNavigationEnabled())
                    navParentContainer.setVisibility(shouldShow ? View.VISIBLE : View.INVISIBLE);
                navigationFragment.setFocusable(shouldShow);
                if (shouldShow) {
                    // navigationFragment.setSelectorColor();
                    navigationFragment.notifyDataSetInvalidate();
                }
            }
        });
    }

    @Override
    public void enableNavigation() {
        if (appCMSPresenter.isNewsLeftNavigationEnabled()) {
            RelativeLayout.LayoutParams homeDataLayoutParams = (RelativeLayout.LayoutParams) subscribeNowStripContaineer.getLayoutParams();
            if (updatedAppCMSBinder != null && (appCMSPresenter.isPagePrimary(updatedAppCMSBinder.getPageId())
                    || appCMSPresenter.isPageUser(updatedAppCMSBinder.getPageId())
                    || appCMSPresenter.isPageFooter(updatedAppCMSBinder.getPageId()))) {
                homeDataLayoutParams.leftMargin = 93;
                navigationPlaceholderFramelayout.setVisibility(View.VISIBLE);
                navParentContainer.setVisibility(View.VISIBLE);
            } else {
                homeDataLayoutParams.leftMargin = 0;
                navigationPlaceholderFramelayout.setVisibility(View.GONE);
                navParentContainer.setVisibility(View.GONE);
            }
            subscribeNowStripContaineer.setLayoutParams(homeDataLayoutParams);
        }
    }

    @Override
    public void showNavigation() {
        if (appCMSPresenter.isNewsLeftNavigationEnabled()) {
            RelativeLayout.LayoutParams homeDataLayoutParams = (RelativeLayout.LayoutParams) subscribeNowStripContaineer.getLayoutParams();
            homeDataLayoutParams.leftMargin = 93;
            navigationPlaceholderFramelayout.setVisibility(View.VISIBLE);
            navParentContainer.setVisibility(View.VISIBLE);
        }
    }

    public void disableNavigation() {
        if (appCMSPresenter.isNewsLeftNavigationEnabled()) {
            RelativeLayout.LayoutParams homeDataLayoutParams = (RelativeLayout.LayoutParams) subscribeNowStripContaineer.getLayoutParams();
            homeDataLayoutParams.leftMargin = 0;
            navigationPlaceholderFramelayout.setVisibility(View.GONE);
            navParentContainer.setVisibility(View.GONE);
            subscribeNowStripContaineer.setLayoutParams(homeDataLayoutParams);
        }
    }

    public void notifyNavigationChanged() {
        navigationFragment = AppCmsNavigationFragment.newInstance(
                this,
                this,
                appCMSBinder,
                textColor,
                bgColor);
        setNavigationFragment(navigationFragment);
    }

    public boolean isNavigationVisible() {
        return (navHolder != null ? navHolder.getVisibility() == View.VISIBLE : false);
    }

    public boolean isNavigationExpanded() {
        return navigationPlaceholderFramelayout.getNavigationState().equals(NavigationPlaceholderFramelayout.NavigationState.NAVIGATION_STATE_EXPANDED);
    }


    private void openParentalGateViewFragment() {
        ParentalGateViewFragment parentalGateViewFragment = ParentalGateViewFragment.getInstance();
        /*String tag = "PARENTAL_GATE_VIEW_PAGE";
        appCMSBinderStack.push(tag);
        getSupportFragmentManager().beginTransaction().replace(R.id.home_placeholder, parentalGateViewFragment,
                tag).addToBackStack(tag).commit();*/

        parentalGateViewFragment.show(getSupportFragmentManager().beginTransaction(), "DIALOG_FRAGMENT_TAG");
    }

    public void openSearchFragment(Intent intent) {
        Bundle args = intent.getBundleExtra(getString(R.string.app_cms_bundle_key));
        AppCMSBinder appCmsBinder = (AppCMSBinder) args.getBinder(getString(R.string.app_cms_binder_key));
        updatedAppCMSBinder = appCmsBinder;
        if (updatedAppCMSBinder != null) {
            appCMSPresenter.sendAppsFlyerPageViewEvent(updatedAppCMSBinder.getPageName(), updatedAppCMSBinder.getPageId());
        }
        int distanceFromStackTop = -1;
        String tag = getTag(appCmsBinder);/*getString(R.string.app_cms_search_label);*/

        distanceFromStackTop = appCMSBinderStack.search(tag);

        //Log.d(TAG, "Page distance from top: " + distanceFromStackTop);
        if (0 < distanceFromStackTop) {
            for (int i = 0; i < distanceFromStackTop; i++) {
                //Log.d(TAG, "Popping stack to get to page item");
                try {
                    appCMSBinderStack.pop();
                } catch (IllegalStateException e) {
                    //Log.e(TAG, "Error popping back stack: " + e.getMessage());
                }
            }
        }

        showInfoIcon(tag);
        appCMSBinderMap.put(tag, updatedAppCMSBinder);
        appCMSBinderStack.push(tag);
        appCMSPresenter.sendGaScreen(appCmsBinder.getScreenName());

        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.home_placeholder);
        if (null != fragment && fragment instanceof AppCmsSearchFragment) {
            getSupportFragmentManager().popBackStack();
        }
        AppCmsSearchFragment searchFragment = AppCmsSearchFragment.newInstance(appCmsBinder.getSearchQuery().toString(), appCmsBinder.getAppCMSPageUI());
        getSupportFragmentManager().beginTransaction().replace(R.id.home_placeholder, searchFragment,
                tag).addToBackStack(tag).commit();
        selectNavItem(tag);
    }

    public void openLibraryFragment(Intent intent) {

        Bundle args = intent.getBundleExtra(getString(R.string.app_cms_bundle_key));
        AppCMSBinder appCmsBinder = (AppCMSBinder) args.getBinder(getString(R.string.app_cms_binder_key));
        int distanceFromStackTop = -1;
        String tag = "library";
        if (0 < distanceFromStackTop) {
            for (int i = 0; i < distanceFromStackTop; i++) {
                //Log.d(TAG, "Popping stack to get to page item");
                try {
                    appCMSBinderStack.pop();
                } catch (IllegalStateException e) {
                    //Log.e(TAG, "Error popping back stack: " + e.getMessage());
                }
            }
        }

        showInfoIcon(tag);
        appCMSBinderStack.push(tag);
        appCMSPresenter.sendGaScreen(appCmsBinder.getScreenName());


        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.home_placeholder);

        if (null != fragment && fragment instanceof AppCmsLibraryFragment) {
            getSupportFragmentManager().popBackStack();
        }
        AppCmsLibraryFragment libraryFragment = AppCmsLibraryFragment.newInstance();
        getSupportFragmentManager().beginTransaction().replace(R.id.home_placeholder, libraryFragment,
                tag).addToBackStack(tag).commit();
        appCmsLibraryFragment = libraryFragment;
        selectNavItem(tag);
    }

    private void openMyProfile() {
        int distanceFromStackTop = -1;
        String tag = getString(R.string.my_profile_pageid);

        distanceFromStackTop = appCMSBinderStack.search(tag);
        showInfoIcon(tag);
//        appCMSBinderStack.push(tag);
        selectNavItem(tag);
    }

    private void showInfoIcon(String pageId) {
        if (appCMSPresenter.getTemplateType() == AppCMSPresenter.TemplateType.ENTERTAINMENT) {
            showMenuIcon(appCMSPresenter.isPagePrimary(pageId) ? View.VISIBLE : View.INVISIBLE);
        }
    }

   /* @Override
    public int getSubNavigationContainer() {
        return R.id.sub_navigation_placeholder;
    }*/

    public AppCmsTvSearchComponent getAppCMSSearchComponent() {
        return appCMSSearchUrlComponent;
    }

    @Override
    public int getNavigationContainer() {
        if (appCMSPresenter.isNewsLeftNavigationEnabled()) {
            return R.id.news_navigation_placholder;
        } else {
            return R.id.navigation_placholder;
        }

    }

    private void updateData() {
        if (appCMSPresenter != null) {
            final AppCMSMain appCMSMain = appCMSPresenter.getAppCMSMain();
            for (Map.Entry<String, AppCMSBinder> appCMSBinderEntry : appCMSBinderMap.entrySet()) {
                final AppCMSBinder appCMSBinder = appCMSBinderEntry.getValue();
                if (appCMSBinder != null) {
                    String endPoint = appCMSPresenter.getPageIdToPageAPIUrl(appCMSBinder.getPageId());
                    boolean usePageIdQueryParam = true;
                    if (appCMSPresenter.isPageAVideoPage(appCMSBinder.getScreenName())) {
                        endPoint = appCMSPresenter.getPageNameToPageAPIUrl(appCMSBinder.getScreenName());
                        usePageIdQueryParam = false;
                    }

                    if (!TextUtils.isEmpty(endPoint)
                            && !appCMSBinder.getPageName().equalsIgnoreCase
                            (getString(R.string.app_cms_watchlist_navigation_title))
                            && !appCMSBinder.getPageName().equalsIgnoreCase
                            (getString(R.string.app_cms_history_navigation_title))) {

                        String apiUrl = appCMSPresenter.getApiUrl(usePageIdQueryParam,
                                false,
                                false,
                                false,
                                null,
                                appCMSMain.getApiBaseUrl(),
                                endPoint,
                                appCMSMain.getInternalName(),
                                appCMSBinder.getPagePath(),
                                appCMSBinder.getAppCMSPageUI() != null
                                        && appCMSBinder.getAppCMSPageUI().getCaching() != null &&
                                        !appCMSBinder.getAppCMSPageUI().getCaching().shouldOverrideCaching() &&
                                        appCMSBinder.getAppCMSPageUI().getCaching().isEnabled());

                        //appCMSPresenter.getPageAPILruCache().remove(appCMSBinder.getPagePath());
                        appCMSPresenter.getPageIdContent(apiUrl,
                                appCMSBinder.getPagePath(),
                                null,
                                appCMSBinder.getAppCMSPageUI().getCaching() != null &&
                                        !appCMSBinder.getAppCMSPageUI().getCaching().shouldOverrideCaching() &&
                                        appCMSBinder.getAppCMSPageUI().getCaching().isEnabled(),
                                false,
                                appCMSPageAPI -> {
                                    if (appCMSPageAPI != null) {
                                        boolean updatedHistory = false;
                                        if (appCMSPresenter.isUserLoggedIn()) {
                                            if (appCMSPageAPI.getModules() != null) {
                                                List<Module> modules = appCMSPageAPI.getModules();
                                                for (int i = 0; i < modules.size(); i++) {
                                                    Module module = modules.get(i);
                                                    AppCMSUIKeyType moduleType = appCMSPresenter.getJsonValueKeyMap().get(module.getModuleType());
                                                    if (moduleType == AppCMSUIKeyType.PAGE_API_HISTORY_MODULE_KEY) {
                                                        if (module.getContentData() != null &&
                                                                !module.getContentData().isEmpty()) {
                                                            int finalI = i;
                                                            appCMSPresenter.getHistoryData(appCMSHistoryResult -> {
                                                                if (appCMSHistoryResult != null) {
                                                                    AppCMSPageAPI historyAPI = appCMSHistoryResult.convertToAppCMSPageAPI(appCMSPageAPI.getId(), false);
                                                                    historyAPI.getModules().get(0).setId(module.getId());
                                                                    historyAPI.getModules().get(0).setTitle(module.getTitle());
                                                                    modules.set(finalI, historyAPI.getModules().get(0));
                                                                    appCMSBinder.updateAppCMSPageAPI(appCMSPageAPI);
                                                                }
                                                            });
                                                            updatedHistory = true;
                                                        }
                                                    }
                                                }
                                            }
                                        }

                                        appCMSBinderMap.put(getTag(appCMSBinder), appCMSBinder);
                                        int totalNoOfFragment = getSupportFragmentManager().getBackStackEntryCount();
                                        for (int i = 0; i < totalNoOfFragment; i++) {
                                            FragmentManager.BackStackEntry backStackEntry = getSupportFragmentManager().getBackStackEntryAt(i);
                                            String tag = backStackEntry.getName();
                                            Fragment fragment = getSupportFragmentManager().findFragmentByTag(tag);
                                            AppCMSBinder appCmsBinder = appCMSBinderMap.get(tag);
                                            if (fragment instanceof AppCmsTVPageFragment) {
                                                ((AppCmsTVPageFragment) fragment).updateBinder(appCmsBinder);
                                            }
                                        }
                                    }
                                });
                    } else if (appCMSBinder.getPageName().equalsIgnoreCase
                            (getString(R.string.app_cms_history_navigation_title))) {
                        AppCMSPageAPI appCMSPageAPI = appCMSBinder.getAppCMSPageAPI();
                        appCMSPresenter.getHistoryData(appCMSHistoryResult -> {
                            if (appCMSHistoryResult != null) {
                                AppCMSPageAPI historyAPI =
                                        appCMSHistoryResult.convertToAppCMSPageAPI(appCMSPageAPI.getId(), false);
                                appCMSBinder.updateAppCMSPageAPI(historyAPI);
                            }
                        });
                    }
                }
            }
        }
    }

    private void updateWatchListData() {
        if (appCMSPresenter != null) {
            for (Map.Entry<String, AppCMSBinder> appCMSBinderEntry : appCMSBinderMap.entrySet()) {
                final AppCMSBinder appCMSBinder = appCMSBinderEntry.getValue();
                if (appCMSBinder != null) {
                    if (appCMSBinder.getPageName().equalsIgnoreCase(getString(R.string.app_cms_watchlist_navigation_title))) {
                        AppCMSPageAPI appCMSPageAPI = appCMSBinder.getAppCMSPageAPI();
                        appCMSPresenter.getWatchlistData(appCMSWatchlistResult -> {
                            if (null != appCMSWatchlistResult) {
                                AppCMSPageAPI pageAPI;
                                if (appCMSWatchlistResult != null) {
                                    pageAPI = appCMSWatchlistResult.convertToAppCMSPageAPI(appCMSPageAPI.getId(), false);
                                    appCMSBinder.updateAppCMSPageAPI(pageAPI);
                                }
                                int totalNoOfFragment = getSupportFragmentManager().getBackStackEntryCount();
                                for (int i = 0; i < totalNoOfFragment; i++) {
                                    FragmentManager.BackStackEntry backStackEntry = getSupportFragmentManager().getBackStackEntryAt(i);
                                    String tag = backStackEntry.getName();
                                    Fragment fragment = getSupportFragmentManager().findFragmentByTag(tag);
                                    AppCMSBinder appCmsBinder = appCMSBinderMap.get(tag);
                                    if (appCMSBinder.getPageName().equalsIgnoreCase(getString(R.string.app_cms_watchlist_navigation_title))) {
                                        if (fragment instanceof AppCmsTVPageFragment) {
                                            ((AppCmsTVPageFragment) fragment).updateBinder(appCmsBinder);
                                        } else if (fragment instanceof AppCmsMyProfileFragment) {
                                            ((AppCmsMyProfileFragment) fragment).updateBinder(appCmsBinder);
                                        }
                                    }


                                }
                            }
                        }, true);

                    }

                }
            }
        }
    }

    private void updateModuleData(ContentDatum contentDatum) {
        if (appCMSPresenter != null) {
            for (Map.Entry<String, AppCMSBinder> appCMSBinderEntry : appCMSBinderMap.entrySet()) {
                final AppCMSBinder appCMSBinder = appCMSBinderEntry.getValue();
                if (appCMSBinder != null) {
                    int totalNoOfFragment = getSupportFragmentManager().getBackStackEntryCount();
                    for (int i = 0; i < totalNoOfFragment; i++) {
                        FragmentManager.BackStackEntry backStackEntry = getSupportFragmentManager().getBackStackEntryAt(i);
                        String tag = backStackEntry.getName();
                        Fragment fragment = getSupportFragmentManager().findFragmentByTag(tag);
                        AppCMSBinder appCmsBinder = appCMSBinderMap.get(tag);
                        appCmsBinder.setContentDatum(contentDatum);
                        if (appCMSBinder.getPageName().contains(getString(R.string.app_cms_watchlist_navigation_title))) {
                            if (fragment instanceof AppCmsMyProfileFragment) {
                                ((AppCmsMyProfileFragment) fragment).updateBinder(appCmsBinder);
                                ((AppCmsMyProfileFragment) fragment).refreshPage();
                            }
                        }
                    }
                }
            }
        }
    }

    public void refreshPersonalizationSettingsModule() {
        int totalNoOfFragment = getSupportFragmentManager().getBackStackEntryCount();
        for (int i = 0; i < totalNoOfFragment; i++) {
            FragmentManager.BackStackEntry backStackEntry = getSupportFragmentManager().getBackStackEntryAt(i);
            String tag = backStackEntry.getName();
            AppCMSBinder appCmsBinder = appCMSBinderMap.get(tag);
            String pageName = appCmsBinder.getPageName();
            Fragment fragment = getSupportFragmentManager().findFragmentByTag(tag);
            if (fragment instanceof AppCmsMyProfileFragment && pageName.equals("personalizationView")) {
                ((AppCmsMyProfileFragment) fragment).refreshPage();
            }
        }
    }


    public void closeSignUpDialog() {
        new Handler().postDelayed(() -> {
            if (signUpDialog != null) {
                signUpDialog.dismiss();
                signUpDialog = null;
            }
        }, 100);

    }

    public void closeSignInDialog() {
        new Handler().postDelayed(() -> {
            if (loginDialog != null) {
                loginDialog.dismiss();
                loginDialog = null;
            }
        }, 100);

    }

    private void hideFooterControl() {
        if (appCMSPresenter.getTemplateType() == AppCMSPresenter.TemplateType.SPORTS) {
            findViewById(R.id.press_up_button).setVisibility(View.INVISIBLE);
            findViewById(R.id.press_down_button).setVisibility(View.INVISIBLE);
        }
    }

    private void openLinkYourAccountScreen(Intent intent) {
        if (null != intent) {
            Bundle bundle = intent.getBundleExtra(getString(R.string.app_cms_bundle_key));
            if (null != bundle) {
                AppCMSBinder appCMSBinder = (AppCMSBinder) bundle.get(getString(R.string.app_cms_binder_key));
                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                appCmsLinkYourAccountFragment = AppCmsLinkYourAccountFragment.newInstance(
                        appCMSBinder);
                appCmsLinkYourAccountFragment.show(ft, DIALOG_FRAGMENT_TAG);
                Utils.pageLoading(false, this);
                if (appCMSBinder != null) {
                    appCMSPresenter.sendAppsFlyerPageViewEvent(appCMSBinder.getPageName(), appCMSBinder.getPageId());
                }
            }
        }
    }

    private void openChangeLanguageScreen(Intent intent) {
        Bundle bundle = intent.getBundleExtra(getString(R.string.app_cms_bundle_key));
        AppCMSBinder appCMSBinder = (AppCMSBinder) bundle.get(getString(R.string.app_cms_binder_key));
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        AppCmsChangelanguageFragment appCmsChangelanguageFragment = AppCmsChangelanguageFragment.newInstance(appCMSBinder);
        appCmsChangelanguageFragment.show(ft, DIALOG_FRAGMENT_TAG);
        Utils.pageLoading(false, this);
        if (appCMSBinder != null) {
            appCMSPresenter.sendAppsFlyerPageViewEvent(appCMSBinder.getPageName(), appCMSBinder.getPageId());
        }
    }

    public void shouldShowLeftNavigation(boolean shouldShowLeftnav) {
        this.shouldShowLeftNav = shouldShowLeftnav;
    }

    public void shouldShowSubLeftNavigation(boolean shouldShowSubLeftnav) {
        this.shouldShowSubLeftNav = shouldShowSubLeftnav;
    }

    public void playEpisode(ContentDatum contentDatum) {

        appCMSPresenter.showLoadingDialog(true);
        if (contentDatum != null &&
                contentDatum.getSeason() != null &&
                contentDatum.getSeason().get(0) != null &&
                contentDatum.getSeason().get(0).getEpisodes() != null &&
                contentDatum.getSeason().get(0).getEpisodes().get(0) != null) {

            List<String> relatedVideosIds = com.viewlift.Utils.getRelatedVideosInShow2(
                    contentDatum.getSeason(),
                    0,
                    -1,
                    contentDatum.getSeason().get(0).getEpisodes().get(1).getId());

            ContentDatum updatedData = new ContentDatum();
            Gist gist = new Gist();
            updatedData.setGist(gist);
            gist.setId(contentDatum.getSeason().get(0).getEpisodes().get(1).getId());

            appCMSPresenter.launchTVVideoPlayer(
                    updatedData,
                    0,
                    relatedVideosIds,
                    0,
                    null);

        }
    }

    public void openRestorePurchaseDialog(String email) {
        String dialogMessage = getResources().getString(R.string.already_subscribe_msg, email);
        String positiveButtonText = appCMSPresenter.getLanguageResourcesFile().getUIresource(getString(R.string.signin));
        String cancelCTA = appCMSPresenter.getLanguageResourcesFile().getUIresource(getString(R.string.go_to_home));

        if (appCMSPresenter.isUserLoggedIn()) {
            positiveButtonText = appCMSPresenter.getLanguageResourcesFile().getUIresource(getString(R.string.cancel));
        }
        ClearDialogFragment newFragment = Utils.getClearDialogFragment(
                this,
                appCMSPresenter,
                getResources().getDimensionPixelSize(R.dimen.text_clear_dialog_width),
                getResources().getDimensionPixelSize(R.dimen.text_add_to_watchlist_sign_in_dialog_height),
                "",
                dialogMessage,
                positiveButtonText,
                cancelCTA,
                 14
        );

        newFragment.setOnPositiveButtonClicked(s -> {

            if (appCMSPresenter.isUserLoggedIn()) {
                newFragment.dismiss();
            } else {
                Utils.pageLoading(true, AppCmsHomeActivity.this);
                NavigationUser navigationUser = appCMSPresenter.getLoginNavigation();
                appCMSPresenter.navigateToTVPage(
                        navigationUser.getPageId(),
                        navigationUser.getTitle(),
                        navigationUser.getUrl(),
                        false,
                        Uri.EMPTY,
                        false,
                        false,
                        appCMSPresenter.isViewPlanPageOpenFromADialog(), false, false, false);
            }
        });

        newFragment.setOnNegativeButtonClicked(s -> {
            Utils.pageLoading(true, AppCmsHomeActivity.this);
            appCMSPresenter.navigateToHomePage(true);
        });

        newFragment.setOnBackKeyListener(s -> newFragment.dismiss());
    }

    public void openSubscriptionDialog(){
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
                this,
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
            Utils.pageLoading(true, AppCmsHomeActivity.this);
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
            Utils.pageLoading(true, AppCmsHomeActivity.this);
            MetaPage subscriptionPage = appCMSPresenter.getSubscriptionPage();
            if (subscriptionPage != null) {
                String title = getString(R.string.view_plans_label);
                if(appCMSPresenter.getGenericMessages() != null
                        && appCMSPresenter.getGenericMessages().getViewPlansCta() != null){
                    title = appCMSPresenter.getGenericMessages().getViewPlansCta();
                }
                appCMSPresenter.navigateToTVPage(subscriptionPage.getPageId(),
                        title,
                        null,
                        false,
                        Uri.EMPTY,
                        false,
                        true, false, true, false, false);
                appCMSPresenter.setViewPlanPageOpenFromADialog(true);
            }
        });

        newFragment.setOnBackKeyListener(s -> newFragment.dismiss());
    }

    public void openSubscriptionDialog(String contentType,ContentDatum contentDatum){
        String dialogMessage = appCMSPresenter.getLocalisedStrings().getWaysToWatchMessageText();
        String dialogTitle = appCMSPresenter.getLocalisedStrings().getWaysToWatchText();
        String positiveButtonText = appCMSPresenter.getLocalisedStrings().getSignInText();
        String negativeButtonText = appCMSPresenter.getLocalisedStrings().getBecomeMemberText();
        String neutralButtonText = null;
        String extraButtonText = null;

        if(appCMSPresenter.isUserLoggedIn() && appCMSPresenter.getAppPreference().getLoggedInUserEmail() != null) {
            positiveButtonText = "";
        }

        if(contentType != null && contentType.equals(getString(R.string.pricing_model_TVE))){
            positiveButtonText = "";
            negativeButtonText = "";
            neutralButtonText = appCMSPresenter.getLocalisedStrings().getChooseTVProviderText();
        }else if(contentType != null && getString(R.string.pricing_model_SVOD_TVE).equals(contentType)){
            neutralButtonText = appCMSPresenter.getLocalisedStrings().getChooseTVProviderText();
        }else if(contentType != null && getString(R.string.pricing_model_FREE).equals(contentType)){
            negativeButtonText = "";
            neutralButtonText = "";
        }else if(contentType != null && getString(R.string.pricing_model_SVOD_TVOD).equals(contentType)){
            extraButtonText = appCMSPresenter.getLocalisedStrings().getOwnText();
        }else if(contentType != null && getString(R.string.pricing_model_SVOD_TVOD_TVE).equals(contentType)){
            neutralButtonText = appCMSPresenter.getLocalisedStrings().getChooseTVProviderText();
            extraButtonText = appCMSPresenter.getLocalisedStrings().getOwnText();
        }

        PreviewDialogFragment newFragment = Utils.getPreviewDialogFragment(
                this,
                appCMSPresenter,
                getResources().getDimensionPixelSize(R.dimen.text_clear_dialog_width),
                getResources().getDimensionPixelSize(R.dimen.text_add_to_watchlist_sign_in_dialog_height),
                dialogTitle,
                dialogMessage,
                positiveButtonText,
                negativeButtonText,
                neutralButtonText,
                extraButtonText,14
        );

        newFragment.setOnPositiveButtonClicked(s -> {
            Utils.pageLoading(true, AppCmsHomeActivity.this);
            NavigationUser navigationUser = appCMSPresenter.getLoginNavigation();
            appCMSPresenter.navigateToTVPage(
                    navigationUser.getPageId(),
                    navigationUser.getTitle(),
                    navigationUser.getUrl(),
                    false,
                    Uri.EMPTY,
                    false,
                    false,
                    true,
                    false,
                    false,
                    false);
        });

        newFragment.setOnNegativeButtonClicked(s -> {
            if(contentDatum != null && contentDatum.getSubscriptionPlans() != null){
                appCMSPresenter.navigateToContentSubscription(contentDatum.getSubscriptionPlans());
            }else{
                Utils.pageLoading(true, AppCmsHomeActivity.this);
                String title = getResources().getString(R.string.view_plans_label);
                MetaPage subscriptionPage = appCMSPresenter.getSubscriptionPage();
                appCMSPresenter.navigateToTVPage(
                        subscriptionPage.getPageId(),
                        title,
                        null,
                        false,
                        Uri.EMPTY,
                        false,
                        true,
                        true,
                        true,
                        false,
                        false);
            }
            appCMSPresenter.setViewPlanPageOpenFromADialog(true);
        });

        newFragment.setOnNeutralButtonClicked(s -> {
            Utils.pageLoading(true, AppCmsHomeActivity.this);
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

        newFragment.setOnExtraButtonClicked(s ->{
            appCMSPresenter.openTVErrorDialog( appCMSPresenter.getLanguageResourcesFile().getUIresource(getString(R.string.cannot_purchase_msg)),
                    null,
                    false);
        });

        newFragment.setOnBackKeyListener(s -> newFragment.dismiss());
    }

    public void openEntitlementDialog(){
        String dialogMessage = appCMSPresenter.getLocalisedStrings().getUnsubscribedDownloadMsgText();
        String positiveButtonText = appCMSPresenter.getLocalisedStrings().getLoginText();
        String dialogTitle = appCMSPresenter.getLocalisedStrings().getLoginRequiredText();
        String cancelCTA = appCMSPresenter.getLocalisedStrings().getCancelText();

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
            if(appCMSPresenter.isAppSVOD()) {
                if(!appCMSPresenter.isUserLoggedIn()){
                    dialogMessage = appCMSPresenter.getLocalisedStrings().getEntitlementLoginErrorMessageText();
                }else{
                    dialogMessage = appCMSPresenter.getLocalisedStrings().getWebSubscriptionMessagePrefixText()
                            + " " + appCMSPresenter.getLocalisedStrings().getWebSubscriptionMessageSuffixText();
                }

            }else{
                dialogMessage = appCMSPresenter.getLocalisedStrings().getEntitlementLoginErrorMessageText();
            }
            dialogTitle = appCMSPresenter.getLocalisedStrings().getLoginRequiredText();
            positiveButtonText = appCMSPresenter.getLocalisedStrings().getSignInText();
        }


        ClearDialogFragment newFragment = Utils.getClearDialogFragment(
                this,
                appCMSPresenter,
                getResources().getDimensionPixelSize(R.dimen.text_clear_dialog_width),
                getResources().getDimensionPixelSize(R.dimen.text_add_to_watchlist_sign_in_dialog_height),
                dialogTitle,
                dialogMessage,
                positiveButtonText,
                cancelCTA,
                 14
        );

        newFragment.setOnPositiveButtonClicked(s -> {
            Utils.pageLoading(true, AppCmsHomeActivity.this);
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

        newFragment.setOnNegativeButtonClicked(s -> newFragment.dismiss());

        newFragment.setOnBackKeyListener(s -> newFragment.dismiss());
    }

    private void hideMiniPlayer() {
        try {
            if (null != getCustomTVVideoPlayerView()) {
                FrameLayout videoPlayerView = findViewById(R.id.video_player_id);
                if (miniVideoPlayerView.getChildAt(0) != null) {
                    miniVideoPlayerView.removeAllViews();
                }
                if (videoPlayerView == null) {
                    getCustomTVVideoPlayerView().pausePlayer();
                } else {

                    if (videoPlayerView.getChildAt(0) == null) {
                        videoPlayerView.addView(getCustomTVVideoPlayerView());
                    }
                    getCustomTVVideoPlayerView().resumePlayer();
                    getCustomTVVideoPlayerView().getPlayer().setVolume(1);
                    if (findViewById(R.id.live_player_component_id) != null) {
                        findViewById(R.id.live_player_component_id).setVisibility(View.VISIBLE);
                    }
                    if (findViewById(R.id.expanded_view_id) != null) {
                        findViewById(R.id.expanded_view_id).setVisibility(View.GONE);
                    }

                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void showMiniPlayer() {
        try {
            if (null != getCustomTVVideoPlayerView()) {
                miniVideoPlayerView.setVisibility(View.VISIBLE);
                if (miniVideoPlayerView.getChildAt(0) == null) {
                    if (getCustomTVVideoPlayerView().getParent() != null) {
                        ((FrameLayout) getCustomTVVideoPlayerView().getParent()).removeAllViews();
                    }
                    if (appCMSPresenter.isNewsTemplate()) {
                        if (appCMSPresenter.getLivePlayerPreference()) {
                            miniVideoPlayerView.addView(getCustomTVVideoPlayerView());
                            Utils.startAnimation(miniVideoPlayerView, this, Utils.AnimationType.BOTTOM);
                            miniVideoPlayerView.setVisibility(View.VISIBLE);
                        } else {
                            miniVideoPlayerView.setVisibility(View.GONE);
                        }
                    } else {
                        miniVideoPlayerView.addView(getCustomTVVideoPlayerView());
                        Utils.startAnimation(miniVideoPlayerView, this, Utils.AnimationType.BOTTOM);
                        miniVideoPlayerView.setVisibility(View.VISIBLE);
                    }
                }
                if (findViewById(R.id.live_player_component_id) != null) {
                    findViewById(R.id.live_player_component_id).setVisibility(View.GONE);
                }
                if (findViewById(R.id.expanded_view_id) != null) {
                    findViewById(R.id.expanded_view_id).setVisibility(View.VISIBLE);
                }
                getCustomTVVideoPlayerView().resumePlayer();
                getCustomTVVideoPlayerView().getPlayer().setVolume(0);

            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    public boolean isMiniPlayerVisibleToPage() {
        if (updatedAppCMSBinder != null && !TextUtils.isEmpty(updatedAppCMSBinder.getPageName())) {
            String pageName = updatedAppCMSBinder.getPageName();
            if (pageName.equalsIgnoreCase("Shows")/*||
                    pageName.equalsIgnoreCase("Home Page") ||
                    pageName.equalsIgnoreCase("Home")*/) {
                if (getCustomTVVideoPlayerView() != null)
                    getCustomTVVideoPlayerView().pausePlayer();
                return false;
            }else if(!com.viewlift.tv.utility.Utils.isMiniPlayer && appCMSPresenter != null && appCMSPresenter.isNewsTemplate()){
                return false;
            }
            return true;
        }
        return true;
    }

    CustomTVVideoPlayerView customVideoVideoPlayerView1;
    private Runnable carouselPlayerTask = null;
    private Handler handler = new Handler();
    public void setCarouselPlayerTask(Context context,AppCMSPresenter appCMSPresenter,FrameLayout frameLayout, String id, String title,String promoID) {
         this.promoID=promoID;
        if (carouselPlayerTask != null) handler.removeCallbacks(carouselPlayerTask);
        if (customVideoVideoPlayerView1 != null) customVideoVideoPlayerView1.stopPlayer();

        frameLayout.setBackgroundColor(Color.TRANSPARENT);
        frameLayout.removeAllViews();

        if (id != null) {
            trailerID = id;
            if (customVideoVideoPlayerView1 == null) {
                customVideoVideoPlayerView1 = new CustomTVVideoPlayerView(context, appCMSPresenter);
            }
            if (promoID != null) {
                customVideoVideoPlayerView1.setPromo(true);
            } else {
                customVideoVideoPlayerView1.setPromo(false);
            }
            customVideoVideoPlayerView1.setHardPause(true);
            customVideoVideoPlayerView1.setVideoUri(id, title, null, true,false);
            carouselPlayerTask = () -> {
                customVideoVideoPlayerView1.setHardPause(false);
                if (customVideoVideoPlayerView1.getParent() != null)
                    ((FrameLayout) customVideoVideoPlayerView1.getParent()).removeAllViews();
                frameLayout.addView(customVideoVideoPlayerView1);
                Utils.startAnimation(frameLayout, this, Utils.AnimationType.FADE_IN);
                customVideoVideoPlayerView1.resumePlayer();

            };
            handler.postDelayed(carouselPlayerTask, 2500);

            customVideoVideoPlayerView1.setTrailerCompletedCallback(new TrailerCompletedCallback() {
                @Override
                public void videoCompleted() {
                    customVideoVideoPlayerView1.stopPlayer();
                    frameLayout.removeAllViews();
                    frameLayout.setBackgroundColor(Color.TRANSPARENT);
                }

                @Override
                public void videoStarted() {}
            });
        }
    }

    public void removeHandlerCallBacks() {
        if (handler != null) {
            handler.removeCallbacksAndMessages(null);
            if (customVideoVideoPlayerView1 != null) {
                customVideoVideoPlayerView1.releasePlayer();
                customVideoVideoPlayerView1 = null;
            }
            if ((findViewById(R.id.trailer_view_id)) != null) {
                ((FrameLayout) findViewById(R.id.trailer_view_id)).removeAllViews();
                findViewById(R.id.trailer_view_id).setBackgroundColor(Color.TRANSPARENT);

            }
        }

    }

    public CustomTVVideoPlayerView getCustomVideoVideoPlayerView1() {
        return customVideoVideoPlayerView1;
    }


    @Override
    public void onCorrectImageSelected() {
        if (myProfileBinder != null) {
            openMyProfile();
            handleProfileFragmentAction(myProfileBinder);
            showNavigation(false);
        }
        new Handler().postDelayed(() -> {
            if (ParentalGateViewFragment.getInstance() != null
                    && ParentalGateViewFragment.getInstance().isVisible()
                    && ParentalGateViewFragment.getInstance().isAdded()) {
                ParentalGateViewFragment.getInstance().dismiss();
            }
        }, 1000);
    }

    public void setPageName(String pageName){
        this.pageName = pageName;
    }

    public String getPagName(){
        return pageName;
    }
    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public String getCurrentPage(){
        return updatedAppCMSBinder != null ? updatedAppCMSBinder.getPageName() : null;
    }
}