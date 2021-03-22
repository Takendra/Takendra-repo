package com.viewlift.views.fragments;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.viewlift.AppCMSApplication;
import com.viewlift.BuildConfig;
import com.viewlift.R;
import com.viewlift.casting.CastServiceProvider;
import com.viewlift.models.data.appcms.ui.page.ModuleList;
import com.viewlift.presenters.AppCMSPresenter;
import com.viewlift.utils.CommonUtils;
import com.viewlift.views.activity.AppCMSPlayVideoActivity;
import com.viewlift.views.binders.AppCMSBinder;
import com.viewlift.views.components.AppCMSViewComponent;
import com.viewlift.views.components.DaggerAppCMSViewComponent;
import com.viewlift.views.customviews.BaseView;
import com.viewlift.views.customviews.CustomVideoPlayerView;
import com.viewlift.views.customviews.FullPlayerView;
import com.viewlift.views.customviews.MiniPlayerView;
import com.viewlift.views.customviews.PageView;
import com.viewlift.views.customviews.VideoPlayerView;
import com.viewlift.views.customviews.ViewCreator;
import com.viewlift.views.customviews.constraintviews.ConstraintViewCreator;
import com.viewlift.views.customviews.moduleview.constraintview.StandAlonPlayerModule;
import com.viewlift.views.modules.AppCMSPageViewModule;

import java.lang.ref.SoftReference;
import java.lang.ref.WeakReference;
import java.util.List;

import static android.view.View.VISIBLE;

public class AppCMSPageFragment extends Fragment {
    private static final String TAG = "AppCMSPageFragment";
    private final String FIREBASE_SCREEN_VIEW_EVENT = "screen_view";
    private final String LOGIN_STATUS_KEY = "logged_in_status";
    private final String LOGIN_STATUS_LOGGED_IN = "logged_in";
    private final String LOGIN_STATUS_LOGGED_OUT = "not_logged_in";
    private AppCMSViewComponent appCMSViewComponent;
    private OnPageCreation onPageCreation;
    private AppCMSPresenter appCMSPresenter;
    private AppCMSBinder appCMSBinder;
    private PageView pageView;
    private String videoPageName = "Video Page";
    private String authentication_screen_name = "Authentication Screen";


    private boolean shouldSendFirebaseViewItemEvent;
    private ViewGroup pageViewGroup;

    private OnScrollGlobalLayoutListener onScrollGlobalLayoutListener;
    private Runnable runnableCallback;

    public static AppCMSPageFragment newInstance(Context context, AppCMSBinder appCMSBinder) {
        AppCMSPageFragment fragment = new AppCMSPageFragment();
        fragment.shouldSendFirebaseViewItemEvent = false;
        Bundle args = new Bundle();
        args.putBinder(context.getString(R.string.fragment_page_bundle_key), appCMSBinder);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        if (context instanceof OnPageCreation) {
            try {
                onPageCreation = (OnPageCreation) context;

                appCMSBinder =
                        ((AppCMSBinder) getArguments().getBinder(context.getString(R.string.fragment_page_bundle_key)));

                appCMSPresenter = ((AppCMSApplication) getActivity().getApplication())
                        .getAppCMSPresenterComponent()
                        .appCMSPresenter();
                new SoftReference<Object>(appCMSBinder, appCMSPresenter.getSoftReferenceQueue());
                appCMSViewComponent = buildAppCMSViewComponent();

                shouldSendFirebaseViewItemEvent = true;
            } catch (ClassCastException e) {
                //Log.e(TAG, "Could not attach fragment: " + e.toString());
            } catch (NullPointerException e) {
                //Log.e(TAG, "Could not attach fragment: " + e.toString());
            }
        } else {
            throw new RuntimeException("Attached context must implement " +
                    OnPageCreation.class.getCanonicalName());
        }
        super.onAttach(context);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        if (appCMSBinder != null && appCMSBinder.isShowDetailsPage() && getFragmentManager().getFragments() != null && getFragmentManager().getFragments().size() > 0) {
            for (int i = getFragmentManager().getFragments().size() - 1; i >= 0; i--) {
                Fragment fragment = getFragmentManager().getFragments().get(i);
                if (fragment instanceof AppCMSPageFragment) {
                    CommonUtils.setShowDetailsPage(true);
                    ((AppCMSPageFragment) fragment).onPause();
                    ((AppCMSPageFragment) fragment).onDestroyView();
                }
            }
        } else {
            CommonUtils.setShowDetailsPage(false);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        if (appCMSViewComponent == null && appCMSBinder != null) {
            try {
                appCMSViewComponent = buildAppCMSViewComponent();
            } catch (NullPointerException e) {
                e.printStackTrace();
            }
        }

        if (appCMSViewComponent != null) {
            pageView = appCMSViewComponent.appCMSPageView();
        } else {
            pageView = null;
            //Log.e(TAG, "AppCMS page creation error");
            onPageCreation.onError(appCMSBinder);
        }

        if (pageView != null) {
            if (pageView.getParent() != null) {
                ((ViewGroup) pageView.getParent()).removeAllViews();
            }
            onPageCreation.onSuccess(appCMSBinder);

        } else {
            onPageCreation.onError(appCMSBinder);
        }

        if (container != null) {
            if (appCMSBinder != null && !appCMSBinder.isShowDetailsPage()) {
                container.removeAllViews();
            }
            pageViewGroup = container;
        }

        /*
         * Here we are sending analytics for the screen views. Here we will log the events for
         * the Screen which will come on AppCMSPageActivity.
         */
        if (shouldSendFirebaseViewItemEvent) {
            sendFirebaseAnalyticsEvents(appCMSBinder);
            shouldSendFirebaseViewItemEvent = false;
        }
        if (pageView != null) {
            pageView.setOnScrollChangeListener(new PageView.OnScrollChangeListener() {
                @Override
                public void onScroll(int dx, int dy) {
                    if (appCMSBinder != null) {
                        appCMSBinder.setxScroll(appCMSBinder.getxScroll() + dx);
                        appCMSBinder.setyScroll(appCMSBinder.getyScroll() + dy);
                    }
                }

                @Override
                public void setCurrentPosition(int position) {
                    if (appCMSBinder != null) {
                        appCMSBinder.setCurrentScrollPosition(position);
                    }
                }
            });

            if (onScrollGlobalLayoutListener != null) {
                pageView.getViewTreeObserver().removeOnGlobalLayoutListener(onScrollGlobalLayoutListener);
                onScrollGlobalLayoutListener.setAppCMSBinder(appCMSBinder);
                onScrollGlobalLayoutListener.setPageView(pageView);
            } else {
                onScrollGlobalLayoutListener = new OnScrollGlobalLayoutListener(appCMSBinder, pageView);
            }

            pageView.getViewTreeObserver().addOnGlobalLayoutListener(onScrollGlobalLayoutListener);
            if (appCMSBinder != null && appCMSBinder.isShowDetailsPage() && pageView.findViewById(R.id.page_view_linear_layout) != null) {
                pageView.setBackgroundColor(Color.TRANSPARENT);
                pageView.findViewById(R.id.page_view_linear_layout).setBackgroundColor(appCMSPresenter.getGeneralBackgroundColor());
                pageView.setAnimateAlpha(true);
                pageView.findViewById(R.id.page_view_linear_layout).setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View view, MotionEvent event) {
                        Boolean isTouchOnHeaderView = false;
                        switch (event.getActionMasked()) {
                            case MotionEvent.ACTION_DOWN: {
                                View childview = pageView.findViewById(R.id.appLogoImage);
                                isTouchOnHeaderView = childview != null && childview.getTop() <= event.getY() && childview.getBottom() >= event.getY();
                                Log.e("okk", "" + isTouchOnHeaderView);
                                return !isTouchOnHeaderView;
                            }

                        }
                        return false;
                    }
                });
            }
            pageView.setListener(new PageView.Listener() {

                @Override
                public void onDismissed() {
                    getActivity().onBackPressed();
                }

                @Override
                public boolean onShouldInterceptTouchEvent() {
                    return appCMSBinder != null && appCMSBinder.isShowDetailsPage();
                }
            });
        }

        Log.d(TAG, "PageView created");

        return pageView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (savedInstanceState != null) {
            try {
                appCMSBinder = (AppCMSBinder)
                        savedInstanceState.getBinder(getString(R.string.app_cms_binder_key));
            } catch (ClassCastException e) {
                //Log.e(TAG, "Could not attach fragment: " + e.toString());
            }
        }
    }

    private void sendFirebaseAnalyticsEvents(AppCMSBinder appCMSVideoPageBinder) {
        if (appCMSPresenter == null || appCMSVideoPageBinder == null)
            return;
        if (appCMSVideoPageBinder.getScreenName() == null ||
                appCMSVideoPageBinder.getScreenName().equalsIgnoreCase(authentication_screen_name))
            return;

        String screenName = "";
        if (!appCMSVideoPageBinder.isUserLoggedIn()) {
            appCMSPresenter.getmFireBaseAnalytics().setUserProperty(LOGIN_STATUS_KEY, LOGIN_STATUS_LOGGED_OUT);
            screenName = appCMSVideoPageBinder.getScreenName();
        } else {
            appCMSPresenter.getmFireBaseAnalytics().setUserProperty(LOGIN_STATUS_KEY, LOGIN_STATUS_LOGGED_IN);
            if (!TextUtils.isEmpty(appCMSVideoPageBinder.getScreenName()) && appCMSVideoPageBinder.getScreenName().matches(videoPageName)) {
                screenName = appCMSVideoPageBinder.getScreenName() + "-" + appCMSVideoPageBinder.getPageName();
            } else {
                screenName = appCMSVideoPageBinder.getScreenName();
            }
        }
        //Logs an app event.
        appCMSPresenter.getFirebaseAnalytics().screenViewEvent(screenName);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (PageView.isTablet(getContext()) || (appCMSBinder != null && appCMSBinder.isFullScreenEnabled())) {
            handleOrientation(getResources().getConfiguration().orientation);
        }
        updateDataLists();

        if (pageView != null && pageView.findChildViewById(R.id.video_player_id) != null) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    setPageOriantationForVideoPage();
                }
            }, 3000);
            View nextChild = (pageView.findChildViewById(R.id.video_player_id));
            ViewGroup group = (ViewGroup) nextChild;
            if ((group.getChildAt(0)) != null) {
                // ((CustomVideoPlayerView) group.getChildAt(0)).requestAudioFocus();
                appCMSPresenter.videoPlayerView = ((CustomVideoPlayerView) group.getChildAt(0));
            }
        } else if (!BaseView.isTablet(getContext()) && appCMSPresenter != null) {
            appCMSPresenter.restrictPortraitOnly();
        }
        setMiniPlayer();
        try {
            CastServiceProvider.getInstance(getActivity()).setCastCallBackListener(castCallBackListener);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private CastServiceProvider.CastCallBackListener castCallBackListener = new CastServiceProvider.CastCallBackListener() {
        @Override
        public void onCastStatusUpdate() {
            if (pageView != null && pageView.findChildViewById(R.id.video_player_id) != null) {
                if (pageView.findChildViewById(R.id.video_player_id) instanceof FrameLayout) {

                    FrameLayout rootView = (FrameLayout) pageView.findChildViewById(R.id.video_player_id);
                    if (rootView != null && rootView.getChildCount() > 0 && rootView.getChildAt(0) instanceof CustomVideoPlayerView)
                        ((CustomVideoPlayerView) rootView.getChildAt(0)).showOverlayWhenCastingConnected();
                }
            }
            if (appCMSPresenter.videoPlayerView != null) {
                appCMSPresenter.videoPlayerView.showOverlayWhenCastingConnected();
            }
        }
    };

    @Override
    public void onPause() {
        super.onPause();
        updateDataLists();

        if (pageView != null && pageView.findChildViewById(R.id.video_player_id) != null) {
            View nextChild = (pageView.findChildViewById(R.id.video_player_id));
            ViewGroup group = (ViewGroup) nextChild;
            if (group.getChildAt(0) != null) {
                ((VideoPlayerView) group.getChildAt(0)).pausePlayer();
            }
        }

        /*if (appCMSPresenter != null) {
            if (appCMSPresenter.videoPlayerView != null && appCMSPresenter.videoPlayerView.getPlayer() != null) {
                appCMSPresenter.videoPlayerView.pausePlayer();
            }
            appCMSPresenter.dismissPopupWindowPlayer(false);
        }*/
    }

    public void updateDataLists() {
        if (pageView != null) {
            pageView.notifyAdaptersOfUpdate();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (appCMSPresenter != null) {
            appCMSPresenter.closeSoftKeyboard();
        }
        appCMSBinder = null;
        pageView = null;
        runnableCallback = null;
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (pageViewGroup != null && !CommonUtils.isShowDetailsPage()) {
            pageViewGroup.removeAllViews();
        }

        if (pageView != null && pageView.findChildViewById(R.id.video_player_id) != null) {
            RecyclerView nestedScrollView = pageView.findViewById(R.id.home_nested_scroll_view);
            /*Remove Runnable Callback*/
            if (null != nestedScrollView && null != runnableCallback) {
                nestedScrollView.removeCallbacks(runnableCallback);
            }
            View playerParent = (pageView.findChildViewById(R.id.video_player_id));
            ViewGroup group = (ViewGroup) playerParent;
            if (group.getChildAt(0) != null)
                ((VideoPlayerView) group.getChildAt(0)).pausePlayer();

            if (group.getChildAt(0) != null && ((CustomVideoPlayerView) group.getChildAt(0)).entitlementCheckTimer != null) {
                ((CustomVideoPlayerView) group.getChildAt(0)).entitlementCheckTimer.cancel();
                ((CustomVideoPlayerView) group.getChildAt(0)).entitlementCheckTimer = null;
            }

            if (group.getParent() != null) {
                ((ViewGroup) group.getParent()).removeAllViews();
            }
        }
        try {
            if (!BuildConfig.FLAVOR_app.equalsIgnoreCase(AppCMSPresenter.KINDLE_BUILD_VARIENT))
                CastServiceProvider.getInstance(getActivity()).setCastCallBackListener(null);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBinder(getString(R.string.app_cms_binder_key), appCMSBinder);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {

        super.onConfigurationChanged(newConfig);
        if (appCMSPresenter != null) {
            appCMSPresenter.isconfig = true;

            if (appCMSPresenter.isAutoRotate()) {
                if (pageView != null && pageView.findChildViewById(R.id.video_player_id) != null &&
                        !BaseView.isTablet(getActivity())) {

                    View nextChild = (pageView.findChildViewById(R.id.video_player_id));
                    ViewGroup group = (ViewGroup) nextChild;
                    if ((group.getChildAt(0)) == null &&
                            newConfig.orientation == Configuration.ORIENTATION_PORTRAIT &&
                            appCMSPresenter.isFullScreenVisible) {
                        appCMSPresenter.videoPlayerView.updateFullscreenButtonState(Configuration.ORIENTATION_PORTRAIT);

                    } else if ((group.getChildAt(0)) != null &&
                            !(group instanceof FullPlayerView) &&
                            newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {

                        appCMSPresenter.videoPlayerView = ((CustomVideoPlayerView) group.getChildAt(0));
                        appCMSPresenter.videoPlayerView.updateFullscreenButtonState(Configuration.ORIENTATION_LANDSCAPE);
                    }
                }
            }
            // if (!appCMSPresenter.isFullScreenVisible) {
            handleOrientation(newConfig.orientation);
        }
    }

    private void handleOrientation(int orientation) {
        if (appCMSPresenter != null) {
            if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
                appCMSPresenter.onOrientationChange(true);
            } else {
                appCMSPresenter.onOrientationChange(false);
            }
        }
    }

    public AppCMSViewComponent buildAppCMSViewComponent() throws NullPointerException {
        String screenName = appCMSBinder.getScreenName();
        /* if (!appCMSPresenter.isPageAVideoPage(screenName)) {
            screenName = appCMSBinder.getPageId();
        }*/

        return DaggerAppCMSViewComponent.builder()
                .appCMSPageViewModule(new AppCMSPageViewModule(getContext(),
                        appCMSBinder.getPageId(),
                        appCMSBinder.getAppCMSPageUI(),
                        appCMSBinder.getAppCMSPageAPI(),
                        appCMSPresenter.getAppCMSAndroidModules(),
                        screenName,
                        appCMSBinder.getJsonValueKeyMap(),
                        appCMSPresenter))
                .build();
    }

    public ViewCreator getViewCreator() {
        if (appCMSViewComponent != null) {
            return appCMSViewComponent.viewCreator();
        }
        return null;
    }

    public ConstraintViewCreator getConstraintViewCreator() {
        if (appCMSViewComponent != null) {
            return appCMSViewComponent.constraintViewCreator();
        }
        return null;
    }

    public List<String> getModulesToIgnore() {
        if (appCMSViewComponent != null) {
            return appCMSViewComponent.modulesToIgnore();
        }
        return null;
    }

    public void refreshView(AppCMSBinder appCMSBinder) {
        sendFirebaseAnalyticsEvents(appCMSBinder);
        this.appCMSBinder = appCMSBinder;
        ViewCreator viewCreator = getViewCreator();
        ConstraintViewCreator constraintViewCreator = getConstraintViewCreator();
        if (viewCreator != null)
            viewCreator.setIgnoreBinderUpdate(true);
        List<String> modulesToIgnore = getModulesToIgnore();

        if (viewCreator != null && modulesToIgnore != null) {
            boolean updatePage = false;
            if (pageView != null) {
                updatePage = pageView.getParent() != null;
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        setPageOriantationForVideoPage();

                    }
                }, 1000);
            }

            try {
                String screenName = appCMSBinder.getScreenName();
                if (!appCMSPresenter.isPageAVideoPage(screenName)) {
                    screenName = appCMSBinder.getPageId();
                }

                pageView = viewCreator.generatePage(getContext(),
                        appCMSBinder.getPageId(),
                        appCMSBinder.getAppCMSPageUI(),
                        appCMSBinder.getAppCMSPageAPI(),
                        appCMSPresenter.getAppCMSAndroidModules(),
                        screenName,
                        appCMSBinder.getJsonValueKeyMap(),
                        appCMSPresenter,
                        modulesToIgnore, constraintViewCreator);

                if (pageViewGroup != null &&
                        pageView != null &&
                        pageView.getParent() == null) {
                    if (pageViewGroup.getChildCount() > 0) {
                        pageViewGroup.removeAllViews();
                    }
                    pageViewGroup.addView(pageView);
                    if (updatePage) {
                        updateAllViews(pageViewGroup);
                    }
                }
                if (updatePage) {
                    updateAllViews(pageViewGroup);
                    pageView.notifyAdaptersOfUpdate();
                }

                if (pageView != null) {
                    pageView.setOnScrollChangeListener(new PageView.OnScrollChangeListener() {
                        @Override
                        public void onScroll(int dx, int dy) {
                            appCMSBinder.setxScroll(appCMSBinder.getxScroll() + dx);
                            appCMSBinder.setyScroll(appCMSBinder.getyScroll() + dy);
                        }

                        @Override
                        public void setCurrentPosition(int position) {
                            appCMSBinder.setCurrentScrollPosition(position);
                        }
                    });

                    if (onScrollGlobalLayoutListener != null) {
                        pageView.getViewTreeObserver().removeOnGlobalLayoutListener(onScrollGlobalLayoutListener);
                        onScrollGlobalLayoutListener.setAppCMSBinder(appCMSBinder);
                        onScrollGlobalLayoutListener.setPageView(pageView);
                    } else {
                        onScrollGlobalLayoutListener = new OnScrollGlobalLayoutListener(appCMSBinder, pageView);
                    }

                    pageView.getViewTreeObserver().addOnGlobalLayoutListener(onScrollGlobalLayoutListener);
                }

                if (pageViewGroup != null) {
                    pageViewGroup.requestLayout();
                }
            } catch (Exception e) {
                //
                e.printStackTrace();
            }
        }
        setMiniPlayer();

    }

    private void updateAllViews(ViewGroup pageViewGroup) {
        if (pageViewGroup.getVisibility() == VISIBLE) {
            pageViewGroup.setVisibility(View.GONE);
            pageViewGroup.setVisibility(VISIBLE);
        }
        pageViewGroup.requestLayout();
        for (int i = 0; i < pageViewGroup.getChildCount(); i++) {
            View child = pageViewGroup.getChildAt(i);
            if (child instanceof ViewGroup) {
                updateAllViews((ViewGroup) child);
            } else {
                if (child.getVisibility() == VISIBLE) {
                    child.setVisibility(View.GONE);
                    child.setVisibility(VISIBLE);
                }
                child.requestLayout();
            }
        }
    }

    public synchronized void setPageOriantationForVideoPage() {
        /**
         * if current activity is video player then restrict to landscape only
         */

        if (appCMSPresenter.getCurrentActivity() instanceof AppCMSPlayVideoActivity) {
            appCMSPresenter.restrictLandscapeOnly();
            return;
        }
        if (pageView != null && pageView.findChildViewById(R.id.video_player_id) != null && !BaseView.isTablet(appCMSPresenter.getCurrentContext()) /*&&
                appCMSPresenter.isAutoRotate()*/) {
            appCMSPresenter.unrestrictPortraitOnly();
        } else if (appCMSBinder != null && appCMSBinder.isShowDetailsPage()) {
            appCMSPresenter.unrestrictPortraitOnly();
        } else
            appCMSPresenter.setAppOrientation();
    }

    RecyclerView.OnScrollListener scrollListenerForMiniPlayer = new RecyclerView.OnScrollListener() {
        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
        }

        @Override
        public void onScrollStateChanged(RecyclerView v, int newState) {
            super.onScrollStateChanged(v, newState);
            switch (newState) {
                case RecyclerView.SCROLL_STATE_IDLE: {
                    synchronized (v) {
                        int videoPlayerModulePostition = 0;
                        if (v.getLayoutManager() != null &&
                                (v.getLayoutManager()) instanceof LinearLayoutManager) {
                            int firstVisibleIndex = ((LinearLayoutManager) v.getLayoutManager()).findFirstVisibleItemPosition();
                            ModuleList singleVideoUI = null;
                            if (pageView != null && pageView.getAppCMSPageUI() != null && pageView.getAppCMSPageUI().getModuleList() != null) {
                                singleVideoUI = appCMSPresenter.getModuleListByName(pageView.getAppCMSPageUI().getModuleList(), getString(R.string.app_cms_page_video_player_module_key));
                                if (singleVideoUI == null)
                                    singleVideoUI = appCMSPresenter.getModuleListByName(pageView.getAppCMSPageUI().getModuleList(), getString(R.string.app_cms_page_video_player_02_module_key));
                                if (singleVideoUI == null)
                                    singleVideoUI = appCMSPresenter.getModuleListByName(pageView.getAppCMSPageUI().getModuleList(), getString(R.string.app_cms_page_video_player_04_module_key));
                                if (singleVideoUI != null) {
                                    videoPlayerModulePostition = singleVideoUI.getModulePosition();
                                }
                                View nextChild = (pageView.findChildViewById(R.id.video_player_id));
                                ViewGroup group = (ViewGroup) nextChild;

                                if (group == null) {
                                    View standAloneCotainer = (pageView.findChildViewById(R.id.stand_alone_video_player_container_id));
                                    //nextChild = (standAloneCotainer.findViewById(R.id.video_player_id));
                                    if (standAloneCotainer != null && nextChild == null) {
                                        nextChild = standAloneCotainer.findViewById(R.id.videoPlayer);
                                    }

                                    group = (ViewGroup) nextChild;
                                }
                                if (firstVisibleIndex > videoPlayerModulePostition && singleVideoUI != null
                                        && singleVideoUI.getSettings().isShowPIP()) {
                                    if (appCMSPresenter.relativeLayoutPIP == null) {
                                        appCMSPresenter.relativeLayoutPIP = new MiniPlayerView(getActivity(), appCMSPresenter, v, appCMSBinder.getPageId());
                                    }
                                    if (group != null && appCMSPresenter.videoPlayerView != null && !appCMSPresenter.pipPlayerVisible) {
                                        if (appCMSPresenter.getIsMiniPlayerPlaying()) {
                                            appCMSPresenter.showPopupWindowPlayer(true);
                                            appCMSPresenter.setAppOrientation();
                                        } else {
                                            if (!BaseView.isTablet(appCMSPresenter.getCurrentContext())) {
                                                appCMSPresenter.getCurrentActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);
                                            }
                                            appCMSPresenter.showPopupWindowPlayer(false);
                                        }
                                    }

                                } else {

                                    if (group != null && appCMSPresenter.videoPlayerView != null) {
                                        // appCMSPresenter.setAppOrientation();
                                        if (!BaseView.isTablet(appCMSPresenter.getCurrentContext())) {
                                            appCMSPresenter.getCurrentActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);
                                        } else {
                                            appCMSPresenter.setAppOrientation();
                                        }
                                        appCMSPresenter.dismissPopupWindowPlayer(true);
                                        if (appCMSPresenter.videoPlayerView != null && !appCMSPresenter.videoPlayerView.hideMiniPlayer) {
                                            if (appCMSPresenter.getIsMiniPlayerPlaying()) {
                                                appCMSPresenter.videoPlayerView.setPlayState();
                                                appCMSPresenter.videoPlayerView.resumePlayerLastState();
                                            }
                                            appCMSPresenter.videoPlayerView.setUseController(true);
                                            appCMSPresenter.videoPlayerView.enableController();
                                        }
                                    }

                                }
                            }
                        }
                    }
                }
                break;
                case RecyclerView.SCROLL_STATE_DRAGGING: {
                }
                break;
                default:
                    break;
            }
        }
    };
    int firstVisibleIndex = -1;
    int videoPlayerModulePostition = 0;

    public void setMiniPlayer() {
        if ((pageView != null && pageView.getAppCMSPageUI() != null && pageView.findViewById(R.id.home_nested_scroll_view) != null) && pageView.findViewById(R.id.home_nested_scroll_view) instanceof RecyclerView) {
            RecyclerView nestedScrollView = pageView.findViewById(R.id.home_nested_scroll_view);
            if (appCMSPresenter.isHomePage(appCMSBinder.getPageId()) && !appCMSPresenter.isFullScreenVisible) {
                /*Create Runnable callback explicitly and pass this reference to view's postDelayed() method
                and also need to remove this callback when  fragment is destroying because its added in
                queue and deliver later if fragment not exist then throws Illegal state exception*/
                runnableCallback = () -> {
                    nestedScrollView.addOnScrollListener(scrollListenerForMiniPlayer);
                    nestedScrollView.refreshDrawableState();
                    appCMSPresenter.setLastPage(false);
                    nestedScrollView.getAdapter().notifyDataSetChanged();
                    firstVisibleIndex = ((LinearLayoutManager) nestedScrollView.getLayoutManager()).findFirstVisibleItemPosition();

                    if (AppCMSPageFragment.this.isAdded() && pageView != null) {
                        ModuleList singleVideoUI = appCMSPresenter.getModuleListByName(pageView.getAppCMSPageUI().getModuleList(), getString(R.string.app_cms_page_video_player_module_key));
                        if (singleVideoUI == null)
                            singleVideoUI = appCMSPresenter.getModuleListByName(pageView.getAppCMSPageUI().getModuleList(), getString(R.string.app_cms_page_video_player_02_module_key));
                        if (singleVideoUI == null)
                            singleVideoUI = appCMSPresenter.getModuleListByName(pageView.getAppCMSPageUI().getModuleList(), getString(R.string.app_cms_page_video_player_04_module_key));
                        if (singleVideoUI != null) {
                            videoPlayerModulePostition = singleVideoUI.getModulePosition();
                        }
                        if (firstVisibleIndex >= 0) {
                            if (firstVisibleIndex >videoPlayerModulePostition && singleVideoUI != null &&
                                    singleVideoUI.getSettings().isShowPIP()) {
                                if (appCMSPresenter.relativeLayoutPIP == null) {
                                    appCMSPresenter.relativeLayoutPIP = new MiniPlayerView(getActivity(), appCMSPresenter, nestedScrollView, appCMSBinder.getPageId());
                                }
                                View nextChild = (pageView.findChildViewById(R.id.video_player_id));
                                ViewGroup group = (ViewGroup) nextChild;
                                if (nextChild != null && appCMSPresenter.videoPlayerView != null) {
                                    appCMSPresenter.showPopupWindowPlayer(true);
                                }
                            } else {
                                View nextChild = (pageView.findChildViewById(R.id.video_player_id));
                                ViewGroup group = (ViewGroup) nextChild;
                                if (group != null && appCMSPresenter.videoPlayerView != null) {
                                    appCMSPresenter.setAppOrientation();
                                    appCMSPresenter.dismissPopupWindowPlayer(true);
                                    if (appCMSPresenter.videoPlayerView != null && !appCMSPresenter.videoPlayerView.hideMiniPlayer) {
                                        if (appCMSPresenter.getIsMiniPlayerPlaying() || appCMSPresenter.getDefaultTrailerPlay())
                                            appCMSPresenter.videoPlayerView.resumePlayerLastState();
                                        appCMSPresenter.videoPlayerView.setUseController(true);
                                        appCMSPresenter.videoPlayerView.enableController();
                                    }
                                }

                            }
                        } else {
                            appCMSPresenter.dismissPopupWindowPlayer(true);
                        }
                    }
                };
                nestedScrollView.postDelayed(runnableCallback, 10);
            }
        }
    }

    private void removeAllViews(ViewGroup viewGroup) {
        for (int i = 0; i < viewGroup.getChildCount(); i++) {
            if (viewGroup.getChildAt(i) instanceof ViewGroup) {
                removeAllViews(((ViewGroup) viewGroup.getChildAt(i)));
            }
        }
        viewGroup.removeAllViews();
    }

    public interface OnPageCreation {
        void onSuccess(AppCMSBinder appCMSBinder);

        void onError(AppCMSBinder appCMSBinder);

        // void enterFullScreenVideoPlayer();

        // void exitFullScreenVideoPlayer(boolean exitFullScreenVideoPlayer);
    }

    private class OnScrollGlobalLayoutListener implements ViewTreeObserver.OnGlobalLayoutListener {

        private WeakReference<AppCMSBinder> appCMSBinderWeakReference;
        private WeakReference<PageView> pageViewWeakReference;

        public OnScrollGlobalLayoutListener(AppCMSBinder appCMSBinder, PageView pageView) {
            appCMSBinderWeakReference = new WeakReference<>(appCMSBinder);
            pageViewWeakReference = new WeakReference<>(pageView);
        }

        @Override
        public void onGlobalLayout() {
            if (pageViewWeakReference.get() != null) {
                PageView pageView = pageViewWeakReference.get();
                AppCMSBinder appCMSBinder = appCMSBinderWeakReference.get();

                if (appCMSBinder != null && appCMSBinder.getPageId() != null) {

                    if (appCMSBinder.isScrollOnLandscape() != BaseView.isLandscape(pageView.getContext())) {
                        if (!appCMSPresenter.isViewPlanPage(appCMSBinder.getPageId())) {
                            appCMSBinder.setxScroll(0);
                            appCMSBinder.setyScroll(0);
                            pageView.scrollToPosition(appCMSBinder.getCurrentScrollPosition());
                        }
                    } else {
                        if (!appCMSPresenter.isViewPlanPage(appCMSBinder.getPageId())) {
                            int x = appCMSBinder.getxScroll();
                            int y = appCMSBinder.getyScroll();
                            pageView.scrollToPosition(-x, -y);
                            pageView.scrollToPosition(x, y);
                        }
                    }
                    appCMSBinder.setScrollOnLandscape(BaseView.isLandscape(pageView.getContext()));
                }
                pageView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
            }
        }

        public AppCMSBinder getAppCMSBinder() {
            return appCMSBinderWeakReference.get();
        }

        public void setAppCMSBinder(AppCMSBinder appCMSBinder) {
            appCMSBinderWeakReference = new WeakReference<>(appCMSBinder);
        }

        public PageView getPageView() {
            return pageViewWeakReference.get();
        }

        public void setPageView(PageView pageView) {
            pageViewWeakReference = new WeakReference<>(pageView);
        }
    }

}

