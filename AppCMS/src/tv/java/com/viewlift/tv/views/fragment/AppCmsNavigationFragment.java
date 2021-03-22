package com.viewlift.tv.views.fragment;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.Fragment;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.viewlift.AppCMSApplication;
import com.viewlift.R;
import com.viewlift.models.data.appcms.ui.android.MetaPage;
import com.viewlift.models.data.appcms.ui.android.Navigation;
import com.viewlift.models.data.appcms.ui.android.NavigationFooter;
import com.viewlift.models.data.appcms.ui.android.NavigationPrimary;
import com.viewlift.models.data.appcms.ui.android.NavigationUser;
import com.viewlift.presenters.AppCMSPresenter;
import com.viewlift.tv.AppCmsTVSplashActivity;
import com.viewlift.tv.utility.Utils;
import com.viewlift.tv.views.activity.AppCmsBaseActivity;
import com.viewlift.tv.views.activity.AppCmsHomeActivity;
import com.viewlift.tv.views.customviews.TVCollectionGridItemView;
import com.viewlift.utils.CommonUtils;
import com.viewlift.views.binders.AppCMSBinder;

import java.util.ArrayList;
import java.util.List;

import static com.viewlift.models.data.appcms.ui.AppCMSUIKeyType.ANDROID_AUTH_SCREEN_KEY;
import static com.viewlift.models.data.appcms.ui.AppCMSUIKeyType.ANDROID_HISTORY_NAV_KEY;
import static com.viewlift.models.data.appcms.ui.AppCMSUIKeyType.ANDROID_HISTORY_SCREEN_KEY;
import static com.viewlift.models.data.appcms.ui.AppCMSUIKeyType.ANDROID_LIBRARY_NAV_KEY;
import static com.viewlift.models.data.appcms.ui.AppCMSUIKeyType.ANDROID_LIBRARY_SCREEN_KEY;
import static com.viewlift.models.data.appcms.ui.AppCMSUIKeyType.ANDROID_SUBSCRIPTION_SCREEN_KEY;
import static com.viewlift.models.data.appcms.ui.AppCMSUIKeyType.ANDROID_WATCHLIST_NAV_KEY;
import static com.viewlift.models.data.appcms.ui.AppCMSUIKeyType.ANDROID_WATCHLIST_SCREEN_KEY;

public class AppCmsNavigationFragment extends Fragment {

    private static final String TAG = AppCmsNavigationFragment.class.getCanonicalName();
    private static OnNavigationVisibilityListener navigationVisibilityListener;
    private RecyclerView mRecyclerView;
    private int textColor = -1;
    private int bgColor = -1;
    private AppCMSBinder appCmsBinder;
    private TextView navMenuSubscriptionModule;
    private AppCMSPresenter appCMSPresenter;
    private String mSelectedPageId = null;
    private int selectedPosition = -1;
    private ArrayList<NavigationPrimary> navigationSubItemList;
    private FitnessNavigationAdapter fitnessNavigationAdapter;
    private boolean menuExpanded;
    private boolean isEnabled;
    private Switch aSwitch;
    private View layoutSwitch,liveSeparatorView;


    public static AppCmsNavigationFragment newInstance(Context context,
                                                       OnNavigationVisibilityListener listener,
                                                       AppCMSBinder appCMSBinder,
                                                       int textColor,
                                                       int bgColor) {

        AppCmsNavigationFragment fragment = new AppCmsNavigationFragment();
        Bundle args = new Bundle();
        args.putBinder(context.getString(R.string.fragment_page_bundle_key), appCMSBinder);
        args.putInt(context.getString(R.string.app_cms_text_color_key), textColor);
        args.putInt(context.getString(R.string.app_cms_bg_color_key), bgColor);
        fragment.setArguments(args);
        navigationVisibilityListener = listener;
        return fragment;
    }

    public TextView getNavMenuSubscriptionModule() {
        return navMenuSubscriptionModule;
    }


    private void startSplashActivity() {
        Intent intent = new Intent(this.getActivity(), AppCmsTVSplashActivity.class);
        startActivity(intent);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_navigation, null);

        Bundle args = getArguments();
        textColor = args.getInt(getResources().getString(R.string.app_cms_text_color_key));
        bgColor = args.getInt(getResources().getString(R.string.app_cms_bg_color_key));

        IBinder binder = args.getBinder(getResources().getString(R.string.fragment_page_bundle_key));
        if (!(binder instanceof AppCMSBinder)) {
            Log.e(TAG, "onCreateView: Binder not instance of AppCMSBinder. Restarting Application.");
            restartApplication();
            return view;
        }
        AppCMSBinder appCMSBinder = ((AppCMSBinder) binder);
        this.appCmsBinder = appCMSBinder;
        appCMSPresenter = ((AppCMSApplication) getActivity().getApplication())
                .getAppCMSPresenterComponent()
                .appCMSPresenter();
        if(!appCMSPresenter.isLeftNavigationEnabled())
            view.setBackgroundColor(bgColor);
        TextView navMenuTile = (TextView) view.findViewById(R.id.nav_menu_title);
        View navTopLine = view.findViewById(R.id.nav_top_line);
        navMenuSubscriptionModule = (TextView) view.findViewById(R.id.nav_menu_subscription_module);

        mRecyclerView = (RecyclerView) view.findViewById(R.id.navRecylerView);
        mRecyclerView.setLayoutDirection(View.LAYOUT_DIRECTION_LTR);

        if(appCMSPresenter.isNewsLeftNavigationEnabled()){
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(),
                    LinearLayoutManager.VERTICAL,
                    false);
            mRecyclerView
                    .setLayoutManager(linearLayoutManager);
            mRecyclerView.setPadding(0, 0, 0, 0);
            ((RelativeLayout.LayoutParams) mRecyclerView.getLayoutParams()).addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE);
        }else if(appCMSPresenter.isLeftNavigationEnabled()){
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(),
                    LinearLayoutManager.VERTICAL,
                    false);
            mRecyclerView
                    .setLayoutManager(linearLayoutManager);
            mRecyclerView.setPadding(0, 0, 0, 0);
            RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) mRecyclerView.getLayoutParams();
            layoutParams.addRule(RelativeLayout.BELOW, R.id.left_menu_app_logo);
            layoutParams.setMargins(40, 0, 0, 0);
        } else {
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(),
                    LinearLayoutManager.HORIZONTAL,
                    false);
            mRecyclerView
                    .setLayoutManager(linearLayoutManager);
            ((RelativeLayout.LayoutParams) mRecyclerView.getLayoutParams()).addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE);
        }
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());


        if(null!=appCmsBinder&&appCMSPresenter.isNewsLeftNavigationEnabled()){
            view.findViewById(R.id.left_news_menu_app_logo).setVisibility(View.VISIBLE);
            view.setFocusable(false);

            if(appCMSPresenter.getAppCMSMain().getFeatures() != null && appCMSPresenter.getAppCMSMain().getFeatures().isEnableMiniPlayer()){
                createLiveToggle(view);
            }
            fitnessNavigationAdapter = new FitnessNavigationAdapter(getActivity(), textColor, bgColor,
                    appCmsBinder.getNavigation(),
                    appCmsBinder.isUserLoggedIn(),
                    appCMSPresenter);

            mRecyclerView.setAdapter(fitnessNavigationAdapter);
            navMenuTile.setVisibility(View.GONE);
            navTopLine.setVisibility(View.GONE);
            view.findViewById(R.id.left_menu_app_logo).setVisibility(View.INVISIBLE);
        }else if (null!=appCmsBinder&&appCMSPresenter.isLeftNavigationEnabled()) {
            LeftNavigationAdapter navigationAdapter = new LeftNavigationAdapter(getActivity(), textColor, bgColor,
                    appCmsBinder.getNavigation(),
                    appCmsBinder.isUserLoggedIn(),
                        appCMSPresenter);
            mRecyclerView.setAdapter(navigationAdapter);
            navMenuTile.setVisibility(View.GONE);
            navTopLine.setVisibility(View.GONE);
            navMenuSubscriptionModule.setVisibility(View.GONE);
        } else if (null!=appCmsBinder&&appCMSPresenter.getTemplateType().equals(AppCMSPresenter.TemplateType.ENTERTAINMENT)) {
            NavigationAdapter navigationAdapter = new NavigationAdapter(getActivity(), textColor, bgColor,
                    appCmsBinder.getNavigation(),
                    appCmsBinder.isUserLoggedIn(),
                    appCMSPresenter);

            mRecyclerView.setAdapter(navigationAdapter);
            navMenuTile.setVisibility(View.GONE);
            navTopLine.setVisibility(View.GONE);
            navTopLine.setVisibility(View.GONE);
            navMenuSubscriptionModule.setVisibility(View.GONE);
            view.findViewById(R.id.left_menu_app_logo).setVisibility(View.INVISIBLE);
        } else {
            STNavigationAdapter navigationAdapter = new STNavigationAdapter(
                    getActivity(),
                    textColor,
                    bgColor,
                    appCmsBinder.getNavigation(),
                    appCmsBinder.isUserLoggedIn(),
                    appCMSPresenter);

            mRecyclerView.setAdapter(navigationAdapter);
            String menuText = appCMSPresenter.getLocalisedStrings().getMenuTitle();
            navMenuTile.setText(menuText);
            navMenuTile.setTextColor(Color.parseColor(appCMSPresenter.getAppTextColor()));
            navMenuTile.setTypeface(Utils.getSpecificTypeface(getActivity(),
                    appCMSPresenter,
                    getString(R.string.app_cms_page_font_regular_key)));
            navMenuTile.setVisibility(View.VISIBLE);
            navTopLine.setVisibility(View.VISIBLE);
            navTopLine.setBackgroundColor(Color.parseColor(Utils.getFocusColor(getActivity(),appCMSPresenter)));
            view.findViewById(R.id.left_menu_app_logo).setVisibility(View.INVISIBLE);

            if (appCMSPresenter.isAppSVOD() && !appCMSPresenter.getAppCMSMain().getFeatures().isWebSubscriptionOnly()) {
                String message = getResources().getString(R.string.blank_string);
                     if (null != appCMSPresenter) {
                        message = appCMSPresenter.getTopBannerText();
                    }
                    if(message.length() == 0) {
                        message = appCMSPresenter.getLanguageResourcesFile().getUIresource(getResources().getString(R.string.watch_live_text));
                    }

                navMenuSubscriptionModule.setText(message);
                navMenuSubscriptionModule.setBackground(Utils.setButtonBackgroundSelector(getActivity(),
                        Color.parseColor(Utils.getFocusColor(getActivity(),appCMSPresenter))
                        ,null,appCMSPresenter ));
                navMenuSubscriptionModule.setTextColor(Color.parseColor(Utils.getTextColor(getActivity(),appCMSPresenter)));

                toggleVisibilityOfSubscriptionModule();

                navMenuSubscriptionModule.setOnClickListener(v -> {
                    MetaPage viewPlanPage = appCMSPresenter.getSubscriptionPage();
                    appCMSPresenter.navigateToTVPage(viewPlanPage.getPageId(),
                            viewPlanPage.getPageName(),
                            null,
                            false,
                            Uri.EMPTY,
                            false,
                            true, true, true, false, false);
                });
            }
        }
        return view;
    }

    /**
     * In case of older Fire TVs where the system memory is comparatively low, the app when resuming
     * from background doesn't always carry the intended payload (AppCMSBinder). In those, sporadic
     * cases, we restart the application.
     */
    private void restartApplication() {
        Intent mStartActivity = new Intent(getActivity(), AppCmsTVSplashActivity.class);
        int mPendingIntentId = 123456;
        PendingIntent mPendingIntent = PendingIntent.getActivity(getActivity(), mPendingIntentId, mStartActivity, PendingIntent.FLAG_CANCEL_CURRENT);
        AlarmManager mgr = (AlarmManager) getActivity().getSystemService(Context.ALARM_SERVICE);
        mgr.set(AlarmManager.RTC, System.currentTimeMillis() + 100, mPendingIntent);
        System.exit(0);
    }

    private void toggleVisibilityOfSubscriptionModule() {
        if(appCMSPresenter.getTemplateType() == AppCMSPresenter.TemplateType.SPORTS
                && !appCMSPresenter.isLeftNavigationEnabled()
                && !appCMSPresenter.getAppCMSMain().getFeatures().isWebSubscriptionOnly()) {
            appCMSPresenter.getSubscriptionData(appCMSUserSubscriptionPlanResult -> {
                try {
                    if (appCMSUserSubscriptionPlanResult != null) {
                        String subscriptionStatus = appCMSUserSubscriptionPlanResult.getSubscriptionInfo().getSubscriptionStatus();
                        if (subscriptionStatus.equalsIgnoreCase("COMPLETED") ||
                                subscriptionStatus.equalsIgnoreCase("DEFERRED_CANCELLATION")) {
                            navMenuSubscriptionModule.setVisibility(View.GONE);
                        } else {
                            navMenuSubscriptionModule.setVisibility(View.VISIBLE);
                        }
                    } else {
                        navMenuSubscriptionModule.setVisibility(View.VISIBLE);
                    }
                } catch (Exception e) {
                    navMenuSubscriptionModule.setVisibility(View.VISIBLE);
                }
            }, false);
        }
    }

    public void setFocusable(boolean hasFocus) {
        if (null != mRecyclerView) {
            if (hasFocus)
                mRecyclerView.requestFocus();
            else
                mRecyclerView.clearFocus();
        }
    }

    public void setSelectedPageId(String selectedPageId, boolean menuExpanded) {
        this.mSelectedPageId = selectedPageId;
        if (mRecyclerView != null
                && mRecyclerView.getAdapter() != null
                && mRecyclerView.getAdapter() instanceof FitnessNavigationAdapter
                && menuExpanded) {
            mRecyclerView.getAdapter().notifyDataSetChanged();
        }
    }

    public void setLeftMenuState(boolean menuExpanded) {
        this.menuExpanded = menuExpanded;
        if (mRecyclerView != null
                && mRecyclerView.getAdapter() != null
                && mRecyclerView.getAdapter() instanceof FitnessNavigationAdapter) {
            mRecyclerView.getAdapter().notifyDataSetChanged();
        }
    }


    public void notifyDataSetInvalidate() {
        if (null != mRecyclerView && null != mRecyclerView.getAdapter()) {
            if (mRecyclerView.getAdapter() instanceof LeftNavigationAdapter) {
                ((LeftNavigationAdapter) mRecyclerView.getAdapter()).invalidateDataSet();
            }else if (mRecyclerView.getAdapter() instanceof NavigationAdapter) {
                ((NavigationAdapter) mRecyclerView.getAdapter()).invalidateDataSet();
            }
            mRecyclerView.getAdapter().notifyDataSetChanged();
        }
        if (appCMSPresenter.isAppSVOD()) {
            toggleVisibilityOfSubscriptionModule();
        }
    }

    private boolean isEndPosition() {
        return selectedPosition == appCmsBinder.getNavigation().getNavigationPrimary().size() - 1;
    }

    private boolean isStartPosition() {
        return (selectedPosition == 0);
    }

    public interface OnNavigationVisibilityListener {
        void showNavigation(boolean shouldShow);
        void enableNavigation();
        void showNavigation();
    }

    public boolean isPageToBeSelect(NavigationPrimary navigationPrimary , String pageId){
        if(navigationPrimary.getPageId() != null && navigationPrimary.getPageId().equalsIgnoreCase(pageId)){
            return true;
        }
        return false;
    }

    class NavigationAdapter extends RecyclerView.Adapter<NavigationAdapter.NavItemHolder> {
        private Context mContext;
        private LayoutInflater inflater;
        private int textColor;
        private int bgColor;
        private Navigation navigation;
        private boolean isuserLoggedIn;
        private AppCMSPresenter appCmsPresenter;
        private int currentNavPos;
        private List<NavigationPrimary> navigationPrimaryList = new ArrayList<>();

        public NavigationAdapter(Context activity,
                                 int textColor,
                                 int bgColor,
                                 Navigation navigation,
                                 boolean userLoggedIn,
                                 AppCMSPresenter appCMSPresenter) {
            mContext = activity;
            this.textColor = textColor;
            this.bgColor = bgColor;
            this.navigation = appCMSPresenter.getNavigation();
            this.isuserLoggedIn = userLoggedIn;
            this.appCmsPresenter = appCMSPresenter;
            for (NavigationPrimary navigationPrimary: navigation.getNavigationPrimary()) {
                if ((appCMSPresenter.isUserLoggedIn() && navigationPrimary.getAccessLevels().isLoggedIn())
                        || (!appCMSPresenter.isUserLoggedIn() && navigationPrimary.getAccessLevels().isLoggedOut())) {
                    navigationPrimaryList.add((navigationPrimary));
                }
            }
        }



        public void invalidateDataSet() {
            navigationPrimaryList.clear();
            for (NavigationPrimary navigationPrimary: navigation.getNavigationPrimary()) {
                if ((appCMSPresenter.isUserLoggedIn() && navigationPrimary.getAccessLevels().isLoggedIn())
                        || (!appCMSPresenter.isUserLoggedIn() && navigationPrimary.getAccessLevels().isLoggedOut())) {
                    navigationPrimaryList.add((navigationPrimary));
                }
            }
        }
        public Object getItem(int i) {
            return navigationPrimaryList.get(i);
        }

        @Override
        public NavItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {

            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.navigation_item, parent, false);
            NavItemHolder navItemHolder = new NavItemHolder(view);
            return navItemHolder;
        }

        @Override
        public void onBindViewHolder(NavItemHolder holder, final int position) {
            final NavigationPrimary primary = (NavigationPrimary) getItem(position);

            //Set the localise title for Hardcoded navigation item like My Profile or Search
            if (primary.getPageId() != null && primary.getPageId().equalsIgnoreCase(getString(R.string.my_profile_pageid))){

                if(primary.getTitle() != null && ( primary.getPagePath() != null && !primary.getPagePath().equalsIgnoreCase("More"))) {
                    String myProfileText = String.format("%s %s", appCMSPresenter.getLocalisedStrings().getMyNavItemPrefix(),
                            appCmsPresenter.getAppCMSAndroid().getShortAppName() != null ?
                                    appCmsPresenter.getAppCMSAndroid().getShortAppName() :
                                    getString(R.string.profile_label));
                    primary.setTitle(myProfileText);
                }
                /*primary.setTitle(getString(R.string.app_cms_my_profile_label,
                        appCmsPresenter.getAppCMSAndroid().getShortAppName() != null ?
                                appCmsPresenter.getAppCMSAndroid().getShortAppName() :
                                getString(R.string.profile_label)));*/
            }else if(primary.getPageId() != null && primary.getPageId().equalsIgnoreCase(getString(R.string.search_pageid))){
                primary.setTitle(appCMSPresenter.getLanguageResourcesFile().getUIresource(getString(R.string.app_cms_search_label)));
            }

            String title = appCMSPresenter.getNavigationTitle(primary.getLocalizationMap());
            holder.navItemView.setText(title!= null ? title.toUpperCase():primary.getTitle().toUpperCase() );
            holder.navItemView.setTag(R.string.item_position, position);

            if (null != mSelectedPageId) {
                System.out.println("Selected PageId = "+mSelectedPageId + " \t Primary PageId = "+primary.getPageId() + " &&&& Position = "+position);
                if (isPageToBeSelect(primary,mSelectedPageId)) {
                    holder.navItemlayout.requestFocus();
                    mRecyclerView.scrollToPosition(position);
                 } else {
                    holder.navItemlayout.clearFocus();
                 }
            }

            holder.navItemlayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    navigationVisibilityListener.showNavigation(false);
                    //getActivity().sendBroadcast(new Intent(AppCMSPresenter.PRESENTER_PAGE_LOADING_ACTION));
                    Utils.pageLoading(true, getActivity());

                    new Handler().postDelayed(new Runnable() {
                        @SuppressLint("NewApi")
                        @Override
                        public void run() {
                            if (primary.getPageId() != null) {
                                String pageFunctionValue = appCMSPresenter.getPageFunctionValue(primary.getPageId(), primary.getTitle());
                                boolean showParentalGateView = appCMSPresenter.getAppCMSMain().getCompliance() != null
                                        && appCMSPresenter.getAppCMSMain().getCompliance().isCoppa();
                                if (primary.getPageId().equalsIgnoreCase(getString(R.string.search_pageid))
                                        || pageFunctionValue.contains(getString(R.string.search_pageid))
                                        || pageFunctionValue.contains("Search")) {
                                    appCmsPresenter.openSearch(primary.getPageId(), primary.getTitle());
                                    Utils.pageLoading(false, getActivity());
                                } else if (primary.getPageId().equalsIgnoreCase(getString(R.string.my_profile_pageid))) {
                                    NavigationUser navigationUser = getNavigationUser(navigation, appCMSPresenter);
                                    if (navigationUser != null) {

                                        String pageFunction = appCMSPresenter.getPageFunctionValue(navigationUser.getPageId());
                                        if (null != pageFunction
                                                && ANDROID_WATCHLIST_NAV_KEY.equals(appCmsBinder.getJsonValueKeyMap().get(pageFunction))
                                                || ANDROID_WATCHLIST_SCREEN_KEY.equals(appCmsBinder.getJsonValueKeyMap().get(pageFunction))) {
                                            appCmsPresenter.navigateToWatchlistPage(
                                                    navigationUser.getPageId(),
                                                    navigationUser.getTitle(),
                                                    navigationUser.getUrl(),
                                                    false,
                                                    false,
                                                    showParentalGateView);
                                        } else if (null != pageFunction
                                                && ANDROID_HISTORY_NAV_KEY.equals(appCmsBinder.getJsonValueKeyMap().get(pageFunction))
                                                && ANDROID_HISTORY_SCREEN_KEY.equals(appCmsBinder.getJsonValueKeyMap().get(pageFunction))) {
                                            appCmsPresenter.navigateToHistoryPage(
                                                    navigationUser.getPageId(),
                                                    navigationUser.getTitle(),
                                                    navigationUser.getUrl(),
                                                    false,
                                                    showParentalGateView);
                                        } else if (ANDROID_LIBRARY_NAV_KEY.equals(appCmsBinder.getJsonValueKeyMap()
                                                .get(pageFunction))
                                                || ANDROID_LIBRARY_SCREEN_KEY.equals(appCmsBinder.getJsonValueKeyMap()
                                                .get(pageFunction))) {

                                            appCmsPresenter.showLoadingDialog(true);
                                            appCmsPresenter.navigateToLibraryPage(
                                                    navigationUser.getPageId(),
                                                    navigationUser.getTitle(),
                                                    false,
                                                    showParentalGateView);

                                            appCmsPresenter.openLibrary(navigationUser.getPageId(), navigationUser.getTitle());


                                        } else if (ANDROID_AUTH_SCREEN_KEY.equals(appCmsBinder.getJsonValueKeyMap()
                                                .get(pageFunction))) {
                                            navigationUser = appCMSPresenter.getLoginNavigation();
                                            appCmsPresenter.navigateToTVPage(
                                                    navigationUser.getPageId(),
                                                    navigationUser.getTitle(),
                                                    navigationUser.getUrl(),
                                                    false,
                                                    Uri.EMPTY,
                                                    false,
                                                    false,
                                                    false,
                                                    false,
                                                    showParentalGateView, false);
                                        } else {
                                            appCmsPresenter.navigateToTVPage(
                                                    navigationUser.getPageId(),
                                                    navigationUser.getTitle(),
                                                    navigationUser.getUrl(),
                                                    false,
                                                    Uri.EMPTY,
                                                    false,
                                                    false,
                                                    false,
                                                    false,
                                                    showParentalGateView, false);
                                        }
                                    } else {
                                        Toast.makeText(mContext, mContext.getString(R.string.something_wrong), Toast.LENGTH_LONG).show();
                                        Utils.pageLoading(false, getActivity());
                                    }

                                } else if (ANDROID_WATCHLIST_NAV_KEY.equals(appCmsBinder
                                        .getJsonValueKeyMap().get(pageFunctionValue))
                                        || ANDROID_WATCHLIST_SCREEN_KEY.equals(appCmsBinder
                                        .getJsonValueKeyMap().get(pageFunctionValue))) {
                                    appCmsPresenter.navigateToWatchlistPage(
                                            primary.getPageId(),
                                            primary.getTitle(),
                                            primary.getUrl(),
                                            false, false, false);
                                } else if (ANDROID_SUBSCRIPTION_SCREEN_KEY.equals(appCmsBinder
                                        .getJsonValueKeyMap().get(pageFunctionValue))) {

                                    appCmsPresenter.navigateToTVPage(primary.getPageId(),
                                            primary.getTitle(),
                                            primary.getUrl(),
                                            false,
                                            null,
                                            true,
                                            true,
                                            true, true,
                                            false, false);
                                }/*  else if (ANDROID_HISTORY_NAV_KEY.equals(appCmsBinder.getJsonValueKeyMap().get(pageFunctionValue))
                                    || ANDROID_HISTORY_SCREEN_KEY.equals(appCmsBinder.getJsonValueKeyMap().get(pageFunctionValue))
                                    || ANDROID_HISTORY_NAV_KEY.equals(appCmsBinder.getJsonValueKeyMap().get(pageFunctionValue))) {
                                appCmsPresenter.navigateToHistoryPage(
                                        primary.getPageId(),
                                        primary.getTitle(),
                                        primary.getUrl(),
                                        false,
                                        Uri.EMPTY,
                                        false,
                                        false,
                                        false,
                                        false,
                                        showParentalGateView);
                            }*/ else if (!TextUtils.isEmpty(primary.getPageId())
                                        && (appCmsPresenter.getPageType(primary.getPageId()).contains("Sub Navigation")
                                        || primary.getDisplayedPath().equalsIgnoreCase("Sub Navigation Screen")
                                        || primary.getDisplayedPath().equalsIgnoreCase("Sub Nav"))
                                        && primary.getItems() != null && primary.getItems().size() > 0) {
                                    // navigationVisibilityListener.showNavigation(false);
//                        subNavigationVisibilityListener.showSubNavigation(true, true);
                                    appCmsPresenter.sendGaScreen(primary.getTitle() + " Navigation Page");
//                        Utils.pageLoading(false, getActivity());
                                    if (primary.getPageId() == null) {
                                        primary.setPageId(primary.getItems().get(0).getPageId());
                                    }
                                    appCmsPresenter.navigateToSubNavigationPage(
                                            primary.getPageId(),
                                            primary.getTitle(),
                                            primary.getUrl(),
                                            primary,
                                            primary.getItems(),
                                            null,
                                            false
                                    );

                                } else if (!appCmsPresenter.navigateToTVPage(primary.getPageId(),
                                        primary.getTitle(),
                                        primary.getUrl(),
                                        false,
                                        null,
                                        true,
                                        false,
                                        false, false, false, false)) {

                                }
                            }
                        }
                    }, 500);
                }
            });
        }

        /*private NavigationUser getNavigationUser() {
            List<NavigationUser> navigationUserList = navigation.getNavigationUser();
            for (NavigationUser navigationUser : navigationUserList) {
                if (appCmsPresenter.isUserLoggedIn() && navigationUser.getAccessLevels().isLoggedIn()) {
                    return navigationUser;
                } else if (!appCmsPresenter.isUserLoggedIn() && navigationUser.getAccessLevels().isLoggedOut()) {
                    return navigationUser;
                }
            }
            return null;
        }*/

        @Override
        public int getItemCount() {
            int totalCount = 0;
            if (null != this.navigationPrimaryList)
                totalCount = this.navigationPrimaryList.size();
            return totalCount;
        }


        class NavItemHolder extends RecyclerView.ViewHolder {
            TextView navItemView;
            RelativeLayout navItemlayout;

            public NavItemHolder(View itemView) {
                super(itemView);
                navItemView = (TextView) itemView.findViewById(R.id.nav_item_label);
                navItemlayout = (RelativeLayout) itemView.findViewById(R.id.nav_item_layout);
                navItemlayout.setBackground(Utils.getNavigationSelector(mContext, appCmsPresenter, false, bgColor,false));
                navItemView.setTextColor(Color.parseColor(Utils.getTextColor(mContext, appCmsPresenter)));
                navItemView.setTypeface(Utils.getSpecificTypeface(
                        mContext,
                        appCMSPresenter,
                        mContext.getString(R.string.app_cms_page_font_semibold_key)));
                navItemlayout.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                    @Override
                    public void onFocusChange(View view, boolean hasFocus) {

                        String text = navItemView.getText().toString();
                        int position = (int) navItemView.getTag(R.string.item_position);
                        selectedPosition = position;

                        //Log.d("TAG","Nav position = "+position);
                        if (hasFocus) {
                            navItemView.setText(text.toUpperCase());
                            navItemView.setTypeface(Utils.getSpecificTypeface(
                                    mContext,
                                    appCMSPresenter,
                                    mContext.getString(R.string.app_cms_page_font_extrabold_key)));
                        } else {
                            navItemView.setText(text.toUpperCase());
                            navItemView.setTypeface(Utils.getSpecificTypeface(
                                    mContext,
                                    appCMSPresenter,
                                    mContext.getString(R.string.app_cms_page_font_semibold_key)));
                        }
                    }
                });

                navItemlayout.setOnKeyListener(new View.OnKeyListener() {
                    @Override
                    public boolean onKey(View view, int i, KeyEvent keyEvent) {
                        int keyCode = keyEvent.getKeyCode();
                        int action = keyEvent.getAction();
                        if (action == KeyEvent.ACTION_DOWN) {
                            int position = (int) navItemView.getTag(R.string.item_position);
                            selectedPosition = position;

                            switch (keyCode) {
                                case KeyEvent.KEYCODE_DPAD_LEFT:
                                    if (isStartPosition()) {
                                        return true;
                                    }
                                    break;
                                case KeyEvent.KEYCODE_DPAD_RIGHT:
                                    if (isEndPosition()) {
                                        return true;
                                    }
                                    break;
                            }
                        }
                        return false;
                    }
                });
            }
        }
    }

    class STNavigationAdapter extends RecyclerView.Adapter<STNavigationAdapter.STNavItemHolder> {
        private Context mContext;
        private int textColor;
        private int bgColor;
        private Navigation navigation;
        private boolean isuserLoggedIn;
        private AppCMSPresenter appCmsPresenter;
        private List<NavigationPrimary> navigationPrimaries = new ArrayList<>();

        public STNavigationAdapter(Context activity,
                                   int textColor,
                                   int bgColor,
                                   Navigation navigation,
                                   boolean userLoggedIn,
                                   AppCMSPresenter appCMSPresenter) {
            mContext = activity;
            this.textColor = textColor;
            this.bgColor = bgColor;
            this.navigation = navigation;
            this.isuserLoggedIn = userLoggedIn;
            this.appCmsPresenter = appCMSPresenter;

            if (navigation.getNavigationPrimary() != null) {
                for (int i = 0; i < navigation.getNavigationPrimary().size(); i++) {
                    NavigationPrimary navigationPrimary = navigation.getNavigationPrimary().get(i);
                    if (!((appCMSPresenter.isUserLoggedIn() && !navigationPrimary.getAccessLevels().isLoggedIn()) ||
                            (!appCMSPresenter.isUserLoggedIn() && !navigationPrimary.getAccessLevels().isLoggedOut()))) {
                        this.navigationPrimaries.add(navigationPrimary);
                    }
                }
            }
        }


        public Object getItem(int i) {
            return navigationPrimaries.get(i);
        }

        @Override
        public STNavItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.st_navigation_item, parent, false);
            return new STNavItemHolder(view);
        }

        @Override
        public void onBindViewHolder(STNavItemHolder holder, final int position) {
            final NavigationPrimary primary = (NavigationPrimary) getItem(position);

            //Set the localise title for Hardcoded navigation item like My Profile or Search
            if (getString(R.string.my_profile_pageid).equalsIgnoreCase(primary.getPageId())){
               /* primary.setTitle(getString(R.string.app_cms_my_profile_label,
                        appCmsPresenter.getAppCMSAndroid().getShortAppName() != null ?
                                appCmsPresenter.getAppCMSAndroid().getShortAppName() :
                                getString(R.string.profile_label)));*/
            }else if(getString(R.string.search_pageid).equalsIgnoreCase(primary.getPageId())){
                primary.setTitle(appCMSPresenter.getLanguageResourcesFile().getUIresource(getString(R.string.app_cms_search_label)));
            }

            String title = appCMSPresenter.getNavigationTitle(primary.getLocalizationMap());
            holder.navItemView.setText(title != null ? title.toUpperCase() : primary.getTitle().toUpperCase());
            holder.navItemView.setTag(R.string.item_position, position);
            holder.navItemView.setTypeface(Utils.getSpecificTypeface(
                    mContext,
                    appCMSPresenter,
                    mContext.getString(R.string.app_cms_page_font_regular_key)));
            if (primary.getIcon() != null) {
                holder.navImageView.setImageResource(Utils.getIcon(primary.getIcon(),mContext,appCMSPresenter));
                if(null != holder.navImageView.getDrawable()) {
                    holder.navImageView.getDrawable().setTint(Utils.getComplimentColor(appCmsPresenter.getGeneralBackgroundColor()));
                    holder.navImageView.getDrawable().setTintMode(PorterDuff.Mode.MULTIPLY);
                }
            }else{
                holder.navImageView.setImageResource(android.R.color.transparent);
            }
            if (null != mSelectedPageId) {
                if (null != primary.getPageId() && isPageToBeSelect(primary,mSelectedPageId)) {
                    holder.navItemLayout.requestFocus();
                } else {
                    holder.navItemLayout.clearFocus();
                }
            }


            holder.navItemLayout.setOnClickListener(view -> {
                final boolean[] showNavigation = {false};
                Utils.pageLoading(true, getActivity());
                new Handler().postDelayed(() -> {
                    if(null == primary.getPageId()){
                        Toast.makeText(mContext, mContext.getString(R.string.something_wrong), Toast.LENGTH_LONG).show();
                        Utils.pageLoading(false, getActivity());
                        return;
                    }
                    String pageFunctionValue = appCMSPresenter.getPageFunctionValue(primary.getPageId(),primary.getTitle());

                    if (primary.getPageId().equalsIgnoreCase(getString(R.string.search_pageid))
                            || pageFunctionValue.contains(getString(R.string.search_pageid))
                            || pageFunctionValue.contains("Search")) {
                        appCmsPresenter.openSearch(primary.getPageId(), primary.getTitle());
                        Utils.pageLoading(false, getActivity());
                    }

                    /*Settings*/
                    else if (primary.getTitle().equalsIgnoreCase(getString(R.string.app_cms_settings_page_tag))
                            || primary.getDisplayedPath().contains(getString(R.string.app_cms_pagename_my_account_screen_key))) {
                        createSubNavigationListForST();
                        String navigationTitle = appCmsPresenter.getNavigationTitle(primary.localizationMap);
                        appCmsPresenter.navigateToSubNavigationPage(
                                primary.getPageId(),
                                !TextUtils.isEmpty(navigationTitle) ? navigationTitle : primary.getTitle(),
                                primary.getUrl(),
                                primary,
                                navigationSubItemList,
                                null,
                                false
                        );

                    }

                    /*Profile/ My snag, my hoichoi etc*/
                    else if (getString(R.string.my_profile_pageid).equalsIgnoreCase(primary.getPageId())) {

                        NavigationUser navigationUser = getNavigationUser();
                        String pageFunction = appCMSPresenter.getPageFunctionValue(navigationUser.getPageId());
                        if (null != pageFunction
                                && ANDROID_WATCHLIST_NAV_KEY.equals(appCmsBinder
                                .getJsonValueKeyMap().get(pageFunction))
                        || ANDROID_WATCHLIST_SCREEN_KEY.equals(appCmsBinder
                                .getJsonValueKeyMap().get(pageFunction))) {
                            appCmsPresenter.navigateToWatchlistPage(
                                    navigationUser.getPageId(),
                                    navigationUser.getTitle(),
                                    navigationUser.getUrl(),
                                    false,false, false);
                        } else {
                            appCmsPresenter.navigateToTVPage(
                                    navigationUser.getPageId(),
                                    navigationUser.getTitle(),
                                    navigationUser.getUrl(),
                                    false,
                                    Uri.EMPTY,
                                    false,
                                    false,
                                    false, false, false, false);
                        }
                    }
                    //This code is for SubNavigation items like Teams in MSE. So we are treating here that if primary.getItems() is not null then its a subnavigation.
                    else if (!TextUtils.isEmpty(primary.getPageId())
                            && (appCmsPresenter.getPageType(primary.getPageId()).contains("Sub Navigation")
                            || primary.getDisplayedPath().toLowerCase().contains("Sub Navigation Screen".toLowerCase()))
                            && primary.getItems() != null && primary.getItems().size() > 0) {
                       // navigationVisibilityListener.showNavigation(false);
//                        subNavigationVisibilityListener.showSubNavigation(true, true);
                        appCmsPresenter.sendGaScreen(primary.getTitle() + " Navigation Page");
//                        Utils.pageLoading(false, getActivity());
                        if(primary.getPageId() == null){
                            primary.setPageId(primary.getItems().get(0).getPageId());
                        }
                        appCmsPresenter.navigateToSubNavigationPage(
                                primary.getPageId(),
                                primary.getTitle(),
                                primary.getUrl(),
                                primary,
                                primary.getItems(),
                                null,
                                false
                        );

                    }

                    /*Watchlist*/
                    else if (null != pageFunctionValue
                            && (pageFunctionValue.equalsIgnoreCase(getString(R.string.app_cms_page_watchlist_title))
                            || pageFunctionValue.contains("Watchlist"))) {
                        if (appCmsPresenter.isUserLoggedIn()) {
                            //navigationVisibilityListener.showNavigation(false);
                            TVCollectionGridItemView.thumbNailClickPosition = 0;
                            Utils.pageLoading(true, getActivity());
                            appCmsPresenter.navigateToWatchlistPage(
                                    primary.getPageId(),
                                    primary.getTitle(),
                                    primary.getUrl(),
                                    false,false, false);
                        } else /*user not logged in*/ {
                            Utils.pageLoading(false, getActivity());

                            String positiveButtonText = appCMSPresenter.getLanguageResourcesFile().getUIresource(getString(R.string.sign_in_text));
                            String negativeButtonText = appCMSPresenter.getLanguageResourcesFile().getUIresource(getString(R.string.cancel));
                            String dialogMessage = appCMSPresenter.getLanguageResourcesFile().getUIresource(mContext.getString(R.string.open_watchlist_dialog_text));

                            if (appCMSPresenter.getLocalisedStrings() != null
                                    && appCMSPresenter.getLocalisedStrings().getSignInText() != null) {
                                positiveButtonText = appCMSPresenter.getLocalisedStrings().getSignInText();
                            }

                            if (appCMSPresenter.getLocalisedStrings() != null
                                    && appCMSPresenter.getLocalisedStrings().getCancelCta() != null) {
                                negativeButtonText = appCMSPresenter.getLocalisedStrings().getCancelCta();
                            }

                            dialogMessage = appCMSPresenter.getLocalisedStrings().getLoginToSeeWatchlistLabel();

                            ClearDialogFragment newFragment = Utils.getClearDialogFragment(
                                    mContext,
                                    appCMSPresenter,
                                    mContext.getResources().getDimensionPixelSize(R.dimen.text_clear_dialog_width),
                                    mContext.getResources().getDimensionPixelSize(R.dimen.text_add_to_watchlist_sign_in_dialog_height),
                                    positiveButtonText,
                                    dialogMessage,
                                    positiveButtonText,
                                    negativeButtonText,
                                     14);
                            newFragment.setOnPositiveButtonClicked(s -> {

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
                        }
                    }

                    /*History*/
                    else if (null != pageFunctionValue
                            && ( pageFunctionValue.equalsIgnoreCase(getString(R.string.app_cms_page_history_title))
                            || pageFunctionValue.contains("History") )) {
                        if (appCmsPresenter.isUserLoggedIn()) {
                            //navigationVisibilityListener.showNavigation(false);
                            TVCollectionGridItemView.thumbNailClickPosition = 0;
                            Utils.pageLoading(true, getActivity());
                            appCmsPresenter.navigateToHistoryPage(
                                    primary.getPageId(),
                                    primary.getTitle(),
                                    primary.getUrl(),
                                    false, false);
                        } else /*user not logged in*/ {
                            Utils.pageLoading(false, getActivity());
                            String positiveButtonText = appCMSPresenter.getLanguageResourcesFile().getUIresource(getString(R.string.sign_in_text));
                            String negativeButtonText = appCMSPresenter.getLanguageResourcesFile().getUIresource(getString(R.string.cancel));
                            String dialogMessage = appCMSPresenter.getLanguageResourcesFile().getUIresource(mContext.getString(R.string.open_history_dialog_text));

                            if (appCMSPresenter.getLocalisedStrings() != null
                                    && appCMSPresenter.getLocalisedStrings().getSignInText() != null) {
                                positiveButtonText = appCMSPresenter.getLocalisedStrings().getSignInText();
                            }

                            if (appCMSPresenter.getLocalisedStrings() != null
                                    && appCMSPresenter.getLocalisedStrings().getCancelCta() != null) {
                                negativeButtonText = appCMSPresenter.getLocalisedStrings().getCancelCta();
                            }

                            dialogMessage = appCMSPresenter.getLocalisedStrings().getLoginToSeeHistoryLabel();

                            ClearDialogFragment newFragment = Utils.getClearDialogFragment(
                                    mContext,
                                    appCMSPresenter,
                                    mContext.getResources().getDimensionPixelSize(R.dimen.text_clear_dialog_width),
                                    mContext.getResources().getDimensionPixelSize(R.dimen.text_add_to_watchlist_sign_in_dialog_height),
                                    positiveButtonText,
                                    dialogMessage,
                                    positiveButtonText,
                                    negativeButtonText,
                                    14);
                            newFragment.setOnPositiveButtonClicked(s -> {

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
                        }
                    } else if (ANDROID_LIBRARY_NAV_KEY.equals(appCmsBinder.getJsonValueKeyMap()
                            .get(pageFunctionValue))
                            || ANDROID_LIBRARY_SCREEN_KEY.equals(appCmsBinder.getJsonValueKeyMap()
                            .get(pageFunctionValue))) {
                        appCmsPresenter.showLoadingDialog(true);
                        appCmsPresenter.navigateToLibraryPage(
                                primary.getPageId(),
                                primary.getTitle(),
                                false, false);
                    }else {
                        appCmsPresenter.navigateToTVPage(primary.getPageId(),
                                primary.getTitle(),
                                primary.getUrl(),
                                false,
                                null,
                                true,
                                false,
                                false, false, false, false);
                       // navigationVisibilityListener.showNavigation(false);
                    }

                }, 500);
            });
        }

        private NavigationUser getNavigationUser() {
            List<NavigationUser> navigationUserList = navigation.getNavigationUser();
            for (NavigationUser navigationUser : navigationUserList) {
                if (appCmsPresenter.isUserLoggedIn() && navigationUser.getAccessLevels().isLoggedIn()) {
                    return navigationUser;
                } else if (!appCmsPresenter.isUserLoggedIn() && navigationUser.getAccessLevels().isLoggedOut()) {
                    return navigationUser;
                }
            }
            return null;
        }

        @Override
        public int getItemCount() {
            /*int totalCount = 0;
            if (null != this.navigation && null != navigation.getNavigationPrimary())
                totalCount = this.navigation.getNavigationPrimary().size();*/
            return navigationPrimaries.size();
        }


        class STNavItemHolder extends RecyclerView.ViewHolder {
            TextView navItemView;
            ImageView navImageView;
            RelativeLayout navItemLayout;

            public STNavItemHolder(View itemView) {
                super(itemView);
                navItemView = (TextView) itemView.findViewById(R.id.nav_item_label);
                navImageView = (ImageView) itemView.findViewById(R.id.nav_item_image);
                navItemLayout = (RelativeLayout) itemView.findViewById(R.id.nav_item_layout);
                navItemLayout.setBackground(Utils.getMenuSelector(mContext, appCmsPresenter.getAppCtaBackgroundColor(),
                        /*Utils.getComplimentColor(Color.parseColor(appCmsPresenter.getAppBackgroundColor()))*/
                        appCMSPresenter.getAppCMSMain().getBrand().getCta().getSecondary().getBorder().getColor()));
                navItemView.setTextColor(Color.parseColor(Utils.getTextColor(mContext, appCmsPresenter)));
                navItemView.setTypeface(Utils.getSpecificTypeface(
                        mContext,
                        appCMSPresenter,
                        mContext.getString(R.string.app_cms_page_font_semibold_key)));

                navItemLayout.setOnFocusChangeListener((view, hasFocus) -> {

                    selectedPosition = (int) navItemView.getTag(R.string.item_position);

                    if (navImageView != null && navImageView.getDrawable() != null) {
                        if (hasFocus) {
                            navImageView.getDrawable().setTint(Utils.getComplimentColor(Color.parseColor(appCmsPresenter.getAppCtaBackgroundColor())));
                         } else {
                            navImageView.getDrawable().setTint(Utils.getComplimentColor(appCmsPresenter.getGeneralBackgroundColor()));
                         }
                    }
                });

                navItemLayout.setOnKeyListener((view, i, keyEvent) -> {
                    int keyCode = keyEvent.getKeyCode();
                    int action = keyEvent.getAction();
                    if (action == KeyEvent.ACTION_DOWN) {

                        int position = (int) navItemView.getTag(R.string.item_position);
                        selectedPosition = position;

                        switch (keyCode) {
                            case KeyEvent.KEYCODE_DPAD_LEFT:
                                if (isStartPosition()) {
                                    return true;
                                }
                                break;
                            case KeyEvent.KEYCODE_DPAD_RIGHT:
                                if (isEndPosition()) {
                                    return true;
                                }
                                break;
                        }
                    }
                    return false;
                });
            }
        }
    }

    class LeftNavigationAdapter extends RecyclerView.Adapter<LeftNavigationAdapter.NavItemHolder> {
        private Context mContext;
        private LayoutInflater inflater;
        private int textColor;
        private int bgColor;
        private Navigation navigation;
        private boolean isuserLoggedIn;
        private AppCMSPresenter appCmsPresenter;
        private int currentNavPos;
        private List<NavigationPrimary> navigationPrimaryList = new ArrayList<>();

        public LeftNavigationAdapter(Context activity,
                                 int textColor,
                                 int bgColor,
                                 Navigation navigation,
                                 boolean userLoggedIn,
                                 AppCMSPresenter appCMSPresenter) {
            mContext = activity;
            this.textColor = textColor;
            this.bgColor = bgColor;
            this.navigation = navigation;
            this.isuserLoggedIn = userLoggedIn;
            this.appCmsPresenter = appCMSPresenter;

            if (navigation.getNavigationPrimary() != null) {
                for (int i = 0; i < navigation.getNavigationPrimary().size(); i++) {
                    NavigationPrimary navigationPrimary = navigation.getNavigationPrimary().get(i);
                    if (shouldAddToNavigation(navigationPrimary)) {
                        if (!(navigationPrimary.getTitle().equalsIgnoreCase("view plans")
                                && !appCMSPresenter.isFireTVSubscriptionEnabled())) {
                            this.navigationPrimaryList.add(navigationPrimary);
                        }
                    }
                }
            }
        }


        public Object getItem(int i) {
            return navigationPrimaryList.get(i);
        }

        @Override
        public NavItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {

            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.left_navigation_item, parent, false);
            NavItemHolder navItemHolder = new NavItemHolder(view);
            return navItemHolder;
        }

        @Override
        public void onBindViewHolder(NavItemHolder holder, final int position) {
            final NavigationPrimary primary = (NavigationPrimary) getItem(position);

            if (primary != null && !CommonUtils.isEmpty(primary.getPageId())) {
                // null check for support ticket : 28118
                if (primary.getPageId().equalsIgnoreCase(getString(R.string.my_profile_pageid))) {
                    if(primary.getTitle() != null && ( primary.getPagePath() != null && !primary.getPagePath().equalsIgnoreCase("More"))) {
                        primary.setTitle(appCMSPresenter.getLanguageResourcesFile().getStringValue(getString(R.string.app_cms_my_profile_label),
                                appCmsPresenter.getAppCMSAndroid().getShortAppName() != null ?
                                        appCmsPresenter.getAppCMSAndroid().getShortAppName() :
                                        getString(R.string.profile_label)));
                    }
                /*primary.setTitle(getString(R.string.app_cms_my_profile_label,
                        appCmsPresenter.getAppCMSAndroid().getShortAppName() != null ?
                                appCmsPresenter.getAppCMSAndroid().getShortAppName() :
                                getString(R.string.profile_label)));*/
                } else if (primary.getPageId().equalsIgnoreCase(getString(R.string.search_pageid))) {
                    primary.setTitle(appCMSPresenter.getLanguageResourcesFile().getUIresource(getString(R.string.app_cms_search_label)));
                }
            }

            String title = appCMSPresenter.getNavigationTitle(primary.getLocalizationMap());
            holder.navItemView.setText(title!= null ? title.toUpperCase():primary.getTitle().toUpperCase());
            holder.navItemView.setTypeface(Utils.getSpecificTypeface(mContext,
                    appCMSPresenter,
                    getString(R.string.app_cms_page_font_regular_key)));
            holder.navItemView.setTag(R.string.item_position, position);
            holder.navItemView.setTextSize(getResources().getDimension(R.dimen.appcms_tv_leftnavigation_textSize));

            holder.navIconView.setImageResource(Utils.getIcon(primary.getIcon(),mContext,appCMSPresenter));
            if (primary.getIcon() != null) {
                holder.navIconView.setImageResource(Utils.getIcon(primary.getIcon(),mContext,appCMSPresenter));
                if(null != holder.navIconView.getDrawable()) {
                    holder.navIconView.getDrawable().setTint(Utils.getComplimentColor(appCmsPresenter.getGeneralBackgroundColor()));
                    holder.navIconView.getDrawable().setTintMode(PorterDuff.Mode.MULTIPLY);
                }
            }else{
                holder.navIconView.setImageResource(android.R.color.transparent);
            }

            if (null != mSelectedPageId) {
                if (isPageToBeSelect(primary, mSelectedPageId)) {
                    holder.navItemlayout.requestFocus();
                    mRecyclerView.scrollToPosition(position);
                } else {
                    holder.navItemlayout.clearFocus();
                }
            }


            holder.navItemlayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //navigationVisibilityListener.showNavigation(false);
                    Utils.pageLoading(true, getActivity());

                    new Handler().postDelayed(new Runnable() {
                        @SuppressLint("NewApi")
                        @Override
                        public void run() {
                            if (primary.getPageId() != null){
                                String pageFn = appCMSPresenter.getPageFunctionValue(primary.getPageId(), primary.getTitle());
                                if (primary.getPageId().equalsIgnoreCase(getString(R.string.search_pageid))
                                        || pageFn.contains(getString(R.string.search_pageid))
                                        || pageFn.contains("Search")) {
                                    appCmsPresenter.openSearch(primary.getPageId(), primary.getTitle());
                                    Utils.pageLoading(false, getActivity());
                                } else if (primary.getPageId().equalsIgnoreCase(getString(R.string.my_profile_pageid))) {
                                    NavigationUser navigationUser = getNavigationUser(navigation, appCMSPresenter);
                                    if (null != getActivity() && getActivity() instanceof AppCmsBaseActivity) {
                                        ((AppCmsBaseActivity) getActivity()).setProfileFirstTime(true);
                                    }
                                    if (navigationUser != null) {
                                        String pageFunction = appCMSPresenter.getPageFunctionValue(navigationUser.getPageId());
                                        if (pageFunction != null
                                                && (ANDROID_WATCHLIST_NAV_KEY.equals(appCmsBinder
                                                .getJsonValueKeyMap().get(pageFunction))
                                                || ANDROID_WATCHLIST_SCREEN_KEY.equals(appCmsBinder
                                                .getJsonValueKeyMap().get(pageFunction)))) {
                                            appCmsPresenter.navigateToWatchlistPage(
                                                    navigationUser.getPageId(),
                                                    navigationUser.getTitle(),
                                                    navigationUser.getUrl(),
                                                    false, false, false);
                                        } else if (ANDROID_HISTORY_NAV_KEY.equals(appCmsBinder.getJsonValueKeyMap().get(pageFunction))
                                                && ANDROID_HISTORY_SCREEN_KEY.equals(appCmsBinder.getJsonValueKeyMap().get(pageFunction))) {
                                            // appCmsPresenter.showLoadingDialog(true);
                                            appCmsPresenter.navigateToHistoryPage(
                                                    navigationUser.getPageId(),
                                                    navigationUser.getTitle(),
                                                    navigationUser.getUrl(),
                                                    false, false);
                                        } else if (ANDROID_LIBRARY_NAV_KEY.equals(appCmsBinder.getJsonValueKeyMap().get(pageFunction))
                                                || ANDROID_LIBRARY_SCREEN_KEY.equals(appCmsBinder.getJsonValueKeyMap().get(pageFunction))) {
                                            appCmsPresenter.showLoadingDialog(true);
                                            appCmsPresenter.navigateToLibraryPage(
                                                    navigationUser.getPageId(),
                                                    navigationUser.getTitle(),
                                                    false, false);

                                            appCmsPresenter.openLibrary(navigationUser.getPageId(), navigationUser.getTitle());
                                        } else {
                                            appCmsPresenter.navigateToTVPage(
                                                    navigationUser.getPageId(),
                                                    navigationUser.getTitle(),
                                                    navigationUser.getUrl(),
                                                    false,
                                                    Uri.EMPTY,
                                                    false,
                                                    false,
                                                    false, false, false, false);
                                        }
                                    } else {
                                        Toast.makeText(mContext, mContext.getString(R.string.something_wrong), Toast.LENGTH_LONG).show();
                                        Utils.pageLoading(false, getActivity());
                                    }

                                } //This code is for SubNavigation items like Teams in MSE. So we are treating here that if primary.getItems() is not null then its a subnavigation.
                                else if (null != appCmsPresenter.getPageType(primary.getPageId())
                                        && (appCmsPresenter.getPageType(primary.getPageId()).contains("Sub Navigation")
                                        || primary.getDisplayedPath().equalsIgnoreCase("Sub Navigation Screen"))
                                        && primary.getItems() != null && primary.getItems().size() > 0) {
                                    appCmsPresenter.sendGaScreen(primary.getTitle() + " Navigation Page");
                                    if (primary.getPageId() == null) {
                                        primary.setPageId(primary.getItems().get(0).getPageId());
                                    }
                                    appCmsPresenter.navigateToSubNavigationPage(
                                            primary.getPageId(),
                                            primary.getTitle(),
                                            primary.getUrl(),
                                            primary,
                                            primary.getItems(),
                                            null,
                                            false
                                    );
                                }
                                /*Settings*/
                                else if (primary.getTitle().equalsIgnoreCase(getString(R.string.app_cms_settings_page_tag))) {
                                    createSubNavigationListForST();
                                    appCmsPresenter.navigateToSubNavigationPage(
                                            primary.getPageId(),
                                            primary.getTitle(),
                                            primary.getUrl(),
                                            primary,
                                            navigationSubItemList,
                                            null,
                                            false
                                    );
                                } else if (ANDROID_LIBRARY_NAV_KEY.equals(appCmsBinder.getJsonValueKeyMap()
                                        .get(pageFn))
                                        || ANDROID_LIBRARY_SCREEN_KEY.equals(appCmsBinder.getJsonValueKeyMap()
                                        .get(pageFn))) {
                                    appCmsPresenter.showLoadingDialog(true);
                                    appCmsPresenter.navigateToLibraryPage(
                                            primary.getPageId(),
                                            primary.getTitle(),
                                            false, false);
                                } else if (!appCmsPresenter.navigateToTVPage(
                                        primary.getPageId(),
                                        primary.getTitle(),
                                        primary.getUrl(),
                                        false,
                                        null,
                                        true,
                                        false,
                                        false, false, false, false)) {

                                }
                            }
                        }
                    }, 500);
                }
            });
        }

        @Override
        public int getItemCount() {
           /* int totalCount = 0;
            if (null != this.navigation && null != navigation.getNavigationPrimary())
                totalCount = this.navigation.getNavigationPrimary().size();
            return totalCount;*/
           return navigationPrimaryList.size();
        }


        class NavItemHolder extends RecyclerView.ViewHolder {
            TextView navItemView;
            LinearLayout navItemlayout;
            ImageView navIconView;

            public NavItemHolder(View itemView) {
                super(itemView);
                navItemView = (TextView) itemView.findViewById(R.id.nav_item_label);
                navItemlayout = (LinearLayout) itemView.findViewById(R.id.nav_item_layout);
                navIconView = (ImageView) itemView.findViewById(R.id.nav_item_image);
                navItemlayout.setBackground(Utils.getNavigationSelector(mContext, appCmsPresenter, false, bgColor, false));

                navItemView.setTextColor(Color.parseColor(Utils.getTextColor(mContext, appCmsPresenter)));
                navItemlayout.setAlpha(0.4F);
                navItemlayout.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                    @Override
                    public void onFocusChange(View view, boolean hasFocus) {

                        String text = navItemView.getText().toString();
                        int position = (int) navItemView.getTag(R.string.item_position);
                        selectedPosition = position;

                        //Log.d("TAG","Nav position = "+position);
                        if (hasFocus) {
                            navItemlayout.setAlpha(1.0F);
                        } else {
                            navItemlayout.setAlpha(0.4F);
                        }
                    }
                });

                navItemlayout.setOnKeyListener(new View.OnKeyListener() {
                    @Override
                    public boolean onKey(View view, int i, KeyEvent keyEvent) {
                        int keyCode = keyEvent.getKeyCode();
                        int action = keyEvent.getAction();
                        if (action == KeyEvent.ACTION_DOWN) {
                            int position = (int) navItemView.getTag(R.string.item_position);
                            selectedPosition = position;
                            switch (keyCode) {
                                case KeyEvent.KEYCODE_DPAD_UP:
                                    if (isStartPosition()) {
                                        return true;
                                    }
                                    break;
                                case KeyEvent.KEYCODE_DPAD_DOWN:
                                    if (isEndPosition()) {
                                        return true;
                                    }
                                    break;
                            }
                        }
                        return false;
                    }
                });
            }
        }

        void invalidateDataSet() {
            navigationPrimaryList.clear();
            for (NavigationPrimary navigationPrimary: navigation.getNavigationPrimary()) {
                if (shouldAddToNavigation(navigationPrimary)) {
                    if (!(navigationPrimary.getTitle().equalsIgnoreCase("view plans")
                            && !appCmsPresenter.isFireTVSubscriptionEnabled())) {
                        navigationPrimaryList.add((navigationPrimary));
                    }
                }
            }
        }

        private boolean shouldAddToNavigation(NavigationPrimary navigationPrimary) {
            if (appCMSPresenter.isUserLoggedIn() && appCMSPresenter.isUserSubscribed()) {
                return (!((appCMSPresenter.isUserLoggedIn() && !navigationPrimary.getAccessLevels().isLoggedIn()) ||
                        (!appCMSPresenter.isUserLoggedIn() && !navigationPrimary.getAccessLevels().isLoggedOut()))) &&
                        (appCMSPresenter.isUserSubscribed() && navigationPrimary.getAccessLevels().isSubscribed());
            } else {
                return !((appCMSPresenter.isUserLoggedIn() && !navigationPrimary.getAccessLevels().isLoggedIn()) ||
                        (!appCMSPresenter.isUserLoggedIn() && !navigationPrimary.getAccessLevels().isLoggedOut()));
            }
        }
    }


    private void createSubNavigationListForST() {
        if (null == navigationSubItemList) {
            navigationSubItemList = new ArrayList<>();
        }

        navigationSubItemList.clear();
        String autoplayOff = appCMSPresenter.getLocalisedStrings().getAutoplayOffMenu();
        String autoplayOn = appCMSPresenter.getLocalisedStrings().getAutoplayOnMenu();
        String closedCaptionOff = appCMSPresenter.getLocalisedStrings().getClosedCaptionOffMenu();
        String closedCaptionOn = appCMSPresenter.getLocalisedStrings().getClosedCaptionOnMenu();
        NavigationPrimary navigationSubItem1 = new NavigationPrimary();
        navigationSubItem1.setIcon(getString(R.string.st_autoplay_icon_key));

        String autoplayText;
        if (!appCMSPresenter.getAutoplayDefaultValueChangedPref(getActivity())) {
            if (appCMSPresenter.isAutoPlayEnable()) {
                autoplayText = autoplayOn;
            } else {
                autoplayText = autoplayOff;
            }
        } else {
            if (appCMSPresenter.getAutoplayEnabledUserPref(getActivity())){
                autoplayText = autoplayOn;
            } else {
                autoplayText = autoplayOff;
            }
        }
        navigationSubItem1.setTitle(autoplayText);

        navigationSubItem1.setDisplayedPath("autoPlay");
        navigationSubItemList.add(navigationSubItem1);

        navigationSubItem1 = new NavigationPrimary();
        navigationSubItem1.setIcon(getString(R.string.st_closed_caption_icon_key));
        if (appCMSPresenter.getClosedCaptionPreference()) {
            navigationSubItem1.setTitle(closedCaptionOn);
        } else {
            navigationSubItem1.setTitle(closedCaptionOff);
        }
        navigationSubItem1.setDisplayedPath("closedCaptions");
        navigationSubItemList.add(navigationSubItem1);

        if (appCMSPresenter.isAppSVOD()) {
            if (appCMSPresenter.isUserLoggedIn()) {
                navigationSubItem1 = new NavigationPrimary();
                String manageSubscriptionText = "MANAGE SUBSCRIPTION";
                navigationSubItem1.setDisplayedPath("manageSubscription");
                manageSubscriptionText = appCMSPresenter.getLocalisedStrings().getManageSubscriptionText();
                navigationSubItem1.setTitle(manageSubscriptionText);
                navigationSubItem1.setIcon(getString(R.string.st_manage_subscription_icon_key));
                navigationSubItemList.add(navigationSubItem1);
            } else /*Guest User*/{
                navigationSubItem1 = new NavigationPrimary();
                String subscribeNowText = "SUBSCRIBE NOW";
                navigationSubItem1.setDisplayedPath("manageSubscription");
                subscribeNowText = appCMSPresenter.getLocalisedStrings().getSubscribeNowText();
                navigationSubItem1.setTitle(subscribeNowText);
                navigationSubItem1.setIcon(getString(R.string.st_manage_subscription_icon_key));
                navigationSubItemList.add(navigationSubItem1);
            }
        }

        Navigation navigation = appCMSPresenter.getNavigation();
        for (int i = 0; i < navigation.getNavigationUser().size(); i++) {
            NavigationUser navigationUser = navigation.getNavigationUser().get(i);

            if ((!((appCMSPresenter.isUserLoggedIn() && !navigationUser.getAccessLevels().isLoggedIn()) ||
                    (!appCMSPresenter.isUserLoggedIn() && !navigationUser.getAccessLevels().isLoggedOut()))) &&
                    !navigationUser.getAccessLevels().isLoggedOut()
                    && appCMSPresenter.isUserLoggedIn()) {
                NavigationPrimary navigationSubItem = new NavigationPrimary();
                String navigationTitle = appCMSPresenter.getNavigationTitle(navigationUser.getLocalizationMap());

                navigationSubItem.setPageId(navigationUser.getPageId());
                navigationSubItem.setTitle(!TextUtils.isEmpty(navigationTitle) ? navigationTitle : navigationUser.getTitle());
                navigationSubItem.setUrl(navigationUser.getUrl());
                navigationSubItem.setIcon(navigationUser.getIcon());
                navigationSubItem.setAccessLevels(navigationUser.getAccessLevels());
                navigationSubItemList.add(navigationSubItem);
            }
        }
            /*if (!isUserLogin) {
                return;
            }*/

        if(null != navigation.getNavigationFooter()) {
            for (int i = 0; i < navigation.getNavigationFooter().size(); i++) {
                if (!((appCMSPresenter.isUserLoggedIn() && !navigation.getNavigationFooter().get(i).getAccessLevels().isLoggedIn()) ||
                        (!appCMSPresenter.isUserLoggedIn() && !navigation.getNavigationFooter().get(i).getAccessLevels().isLoggedOut()))) {
                    NavigationFooter navigationFooter = navigation.getNavigationFooter().get(i);
                    NavigationPrimary navigationSubItem = new NavigationPrimary();
                    navigationSubItem.setPageId(navigationFooter.getPageId());
                    String navigationTitle = appCMSPresenter.getNavigationTitle(navigationFooter.getLocalizationMap());
                    navigationSubItem.setTitle(!TextUtils.isEmpty(navigationTitle) ? navigationTitle : navigationFooter.getTitle());
                    navigationSubItem.setUrl(navigationFooter.getUrl());
                    navigationSubItem.setIcon(navigationFooter.getIcon());
                    navigationSubItem.setAccessLevels(navigationFooter.getAccessLevels());
                    if (null == navigationSubItemList) {
                        navigationSubItemList = new ArrayList<>();
                    }
                    navigationSubItemList.add(navigationSubItem);
                }
            }
        }
        navigationSubItem1 = new NavigationPrimary();
        if (appCMSPresenter.isUserLoggedIn()) {
            String logoutText = appCMSPresenter.getLanguageResourcesFile().getUIresource(getString(R.string.app_cms_sign_out_label));
            if (appCMSPresenter.getNavigation().getSettings().getLocalizationMap() != null
                    && appCMSPresenter.getNavigation().getSettings().getLocalizationMap().get(appCMSPresenter.getLanguage().getLanguageCode()) != null
                    && appCMSPresenter.getNavigation().getSettings().getLocalizationMap().get(appCMSPresenter.getLanguage().getLanguageCode()).getPrimaryCta() != null
                    && appCMSPresenter.getNavigation().getSettings().getLocalizationMap().get(appCMSPresenter.getLanguage().getLanguageCode()).getPrimaryCta().getLogoutCtaText() != null) {
                logoutText = appCMSPresenter.getNavigation().getSettings().getLocalizationMap().get(appCMSPresenter.getLanguage().getLanguageCode()).getPrimaryCta().getLogoutCtaText();
            }
            navigationSubItem1.setTitle(logoutText);
            navigationSubItem1.setIcon(getString(R.string.st_signout_icon_key));
        } else {
            String loginText = appCMSPresenter.getLanguageResourcesFile().getUIresource(getString(R.string.sign_in_text));
            if (appCMSPresenter.getNavigation().getSettings().getLocalizationMap() != null
                    && appCMSPresenter.getNavigation().getSettings().getLocalizationMap().get(appCMSPresenter.getLanguage().getLanguageCode()) != null
                    && appCMSPresenter.getNavigation().getSettings().getLocalizationMap().get(appCMSPresenter.getLanguage().getLanguageCode()).getPrimaryCta() != null
                    && appCMSPresenter.getNavigation().getSettings().getLocalizationMap().get(appCMSPresenter.getLanguage().getLanguageCode()).getPrimaryCta().getLoginCtaText() != null) {
                loginText = appCMSPresenter.getNavigation().getSettings().getLocalizationMap().get(appCMSPresenter.getLanguage().getLanguageCode()).getPrimaryCta().getLoginCtaText();
            }
            navigationSubItem1.setTitle(loginText);
            navigationSubItem1.setIcon(getString(R.string.st_signin_icon_key));
        }
        navigationSubItemList.add(navigationSubItem1);
    }

    class FitnessNavigationAdapter extends RecyclerView.Adapter<FitnessNavigationAdapter.NavItemHolder> {
        private Context mContext;
        private LayoutInflater inflater;
        private int textColor;
        private int bgColor;
        private Navigation navigation;
        private boolean isUserLoggedIn;
        private AppCMSPresenter appCmsPresenter;
        private int currentNavPos;

        public FitnessNavigationAdapter(Context activity,
                                        int textColor,
                                        int bgColor,
                                        Navigation navigation,
                                        boolean userLoggedIn,
                                        AppCMSPresenter appCMSPresenter) {
            mContext = activity;
            this.textColor = textColor;
            this.bgColor = bgColor;
            this.navigation = navigation;
            this.isUserLoggedIn = userLoggedIn;
            this.appCmsPresenter = appCMSPresenter;
        }


        public Object getItem(int i) {
            return navigation.getNavigationPrimary().get(i);
        }

        @Override
        public NavItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {

            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.fitness_left_navigation_item, parent, false);
            NavItemHolder navItemHolder = new NavItemHolder(view);
            return navItemHolder;
        }

        @Override
        public void onBindViewHolder(NavItemHolder holder, final int position) {
            final NavigationPrimary primary = (NavigationPrimary) getItem(position);
            holder.fitnessParentLayout.setFocusable(false);

            if (primary.getPageId().equalsIgnoreCase(getString(R.string.my_profile_pageid))) {
                String myProfileText;
                if (appCMSPresenter.isNewsTemplate()) {
                    if (!appCMSPresenter.isUserLoggedIn()) {
                        myProfileText = appCMSPresenter.getLocalizedLoginText();
                        primary.setIcon(mContext.getResources().getString(R.string.news_user_login_icon_key));
                    } else {
                        primary.setIcon(mContext.getResources().getString(R.string.st_user_icon_key));
                        myProfileText = "SETTINGS";
                    }

                } else {
                    myProfileText = String.format("%s %s", appCMSPresenter.getLocalisedStrings().getMyNavItemPrefix(),
                            appCmsPresenter.getAppCMSAndroid().getShortAppName() != null ?
                                    appCmsPresenter.getAppCMSAndroid().getShortAppName() :
                                    getString(R.string.profile_label));
                }

                primary.setTitle(myProfileText);

            } else if (primary.getPageId().equalsIgnoreCase(getString(R.string.search_pageid))) {
                primary.setTitle(appCMSPresenter.getLanguageResourcesFile().getUIresource(getString(R.string.app_cms_search_label)));
            }

            String title = appCMSPresenter.getNavigationTitle(primary.getLocalizationMap());
            holder.navItemView.setText(title != null ? title.toUpperCase() : primary.getTitle().toUpperCase());
            holder.navItemView.setTag(R.string.item_position, position);
            holder.navItemView.setTextSize(getResources().getDimension(R.dimen.appcms_tv_left_news_navigation_textSize));

            holder.navIconView.setImageResource(Utils.getIcon(primary.getIcon(), mContext,appCMSPresenter));
            if (primary.getIcon() != null) {
                holder.navIconView.setImageResource(Utils.getIcon(primary.getIcon(), mContext,appCMSPresenter));
                /*if (null != holder.navIconView.getDrawable()) {
                    holder.navIconView.getDrawable().setTint(Utils.getComplimentColor(appCmsPresenter.getGeneralBackgroundColor()));
                    holder.navIconView.getDrawable().setTintMode(PorterDuff.Mode.MULTIPLY);
                }*/
            } else {
                holder.navIconView.setImageResource(android.R.color.transparent);
            }

            if(menuExpanded) {
                holder.separatorViewFirst.setVisibility(View.INVISIBLE);
                aSwitch.setText("LIVE");
                aSwitch.setSwitchPadding(20);
            }
            else {
                holder.separatorViewFirst.setVisibility(View.VISIBLE);
                aSwitch.setText("");
                aSwitch.setSwitchPadding(0);
            }

            if (null != mSelectedPageId) {
                if (isPageToBeSelect(primary,mSelectedPageId)) {
                    holder.navItemlayout.requestFocus();
                    int selectedColor = ContextCompat.getColor(mContext, R.color.colorAccent);
                    if(menuExpanded)
                        selectedColor = ContextCompat.getColor(mContext, R.color.left_navigation_focused_color);
                    else
                        selectedColor = ContextCompat.getColor(mContext, R.color.left_navigation_unFocused_color);
                    holder.navItemlayout.setBackgroundColor(selectedColor);
                    holder.navItemView.setTextColor(appCMSPresenter.getBrandPrimaryCtaTextColor());
                    holder.navIconView.setColorFilter(appCMSPresenter.getBrandPrimaryCtaTextColor(), android.graphics.PorterDuff.Mode.MULTIPLY);
                    holder.separatorView.setVisibility(View.VISIBLE);
                } else {
                    holder.navItemlayout.clearFocus();
                    holder.navItemView.setTextColor(appCMSPresenter.getBrandSecondaryCtaTextColor());
                    holder.navIconView.setColorFilter(appCMSPresenter.getBrandSecondaryCtaTextColor(), android.graphics.PorterDuff.Mode.MULTIPLY);
                    holder.separatorView.setVisibility(View.INVISIBLE);
                    holder.separatorViewFirst.setVisibility(View.INVISIBLE);
                    holder.navItemlayout.setBackgroundColor(ContextCompat.getColor(mContext, R.color.transparentColor));

                }
            }

            holder.navItemlayout.setOnClickListener(view -> {
                //navigationVisibilityListener.showNavigation(false);
                Utils.pageLoading(true, getActivity());

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        String pageFn = appCMSPresenter.getPageFunctionValue(primary.getPageId(), primary.getTitle());

                        if (primary.getPageId().equalsIgnoreCase(getString(R.string.search_pageid))
                                || pageFn.contains(getString(R.string.search_pageid))
                                || pageFn.contains("Search")) {
                            appCmsPresenter.openSearch(primary.getPageId(), primary.getTitle());
                            Utils.pageLoading(false, getActivity());
                        } else if (primary.getPageId().equalsIgnoreCase(getString(R.string.my_profile_pageid))) {
                            NavigationUser navigationUser = getNavigationUser();
                            if (null != getActivity() && getActivity() instanceof AppCmsBaseActivity) {
                                ((AppCmsBaseActivity) getActivity()).setProfileFirstTime(true);
                            }
                            if (navigationUser != null) {
                                String pageFunction = appCMSPresenter.getPageFunctionValue(navigationUser.getPageId());
                                if (pageFunction != null
                                        && (ANDROID_WATCHLIST_NAV_KEY.equals(appCmsBinder
                                        .getJsonValueKeyMap().get(pageFunction))
                                        || ANDROID_WATCHLIST_SCREEN_KEY.equals(appCmsBinder
                                        .getJsonValueKeyMap().get(pageFunction)))
                                ) {
                                    appCmsPresenter.navigateToWatchlistPage(
                                            navigationUser.getPageId(),
                                            navigationUser.getTitle(),
                                            navigationUser.getUrl(),
                                            false,false, false);
                                } else if (ANDROID_HISTORY_NAV_KEY.equals(appCmsBinder.getJsonValueKeyMap().get(pageFunction))
                                        || ANDROID_HISTORY_SCREEN_KEY.equals(appCmsBinder.getJsonValueKeyMap().get(pageFunction))) {
                                    // appCmsPresenter.showLoadingDialog(true);
                                    appCmsPresenter.navigateToHistoryPage(
                                            navigationUser.getPageId(),
                                            navigationUser.getTitle(),
                                            navigationUser.getUrl(),
                                            false, false);
                                } else {
                                    appCmsPresenter.navigateToTVPage(
                                            navigationUser.getPageId(),
                                            navigationUser.getTitle(),
                                            navigationUser.getUrl(),
                                            false,
                                            Uri.EMPTY,
                                            false,
                                            false,
                                            false,
                                            false, false, false);
                                }
                            } else {
                                Toast.makeText(mContext, mContext.getString(R.string.something_wrong), Toast.LENGTH_LONG).show();
                                Utils.pageLoading(false, getActivity());
                            }

                        } //This code is for SubNavigation items like Teams in MSE. So we are treating here that if primary.getItems() is not null then its a subnavigation.
                        else if (null != appCmsPresenter.getPageType(primary.getPageId())
                                && (appCmsPresenter.getPageType(primary.getPageId()).contains("Sub Navigation")
                                || primary.getDisplayedPath().equalsIgnoreCase("Sub Navigation Screen"))
                                && primary.getItems() != null && primary.getItems().size() > 0) {
                            appCmsPresenter.sendGaScreen(primary.getTitle() + " Navigation Page");
                            if (primary.getPageId() == null) {
                                primary.setPageId(primary.getItems().get(0).getPageId());
                            }
                            appCmsPresenter.navigateToSubNavigationPage(
                                    primary.getPageId(),
                                    primary.getTitle(),
                                    primary.getUrl(),
                                    primary,
                                    primary.getItems(),
                                    null,
                                    false
                            );
                        }
                        /*Settings*/
                        else if (primary.getTitle().equalsIgnoreCase(getString(R.string.app_cms_settings_page_tag))) {
                            createSubNavigationListForST();
                            appCmsPresenter.navigateToSubNavigationPage(
                                    primary.getPageId(),
                                    primary.getTitle(),
                                    primary.getUrl(),
                                    primary,
                                    navigationSubItemList,
                                    null,
                                    false
                            );
                        } else if (ANDROID_LIBRARY_NAV_KEY.equals(appCmsBinder.getJsonValueKeyMap()
                                .get(pageFn))
                                || ANDROID_LIBRARY_SCREEN_KEY.equals(appCmsBinder.getJsonValueKeyMap()
                                .get(pageFn))) {
                            appCmsPresenter.showLoadingDialog(true);
                            appCmsPresenter.navigateToLibraryPage(
                                    primary.getPageId(),
                                    primary.getTitle(),
                                    false, false);
                        }else if (pageFn != null
                                && (ANDROID_WATCHLIST_NAV_KEY.equals(appCmsBinder
                                .getJsonValueKeyMap().get(pageFn))
                                || ANDROID_WATCHLIST_SCREEN_KEY.equals(appCmsBinder
                                .getJsonValueKeyMap().get(pageFn)))) {
                            appCmsPresenter.navigateToWatchlistPage(
                                    primary.getPageId(),
                                    primary.getTitle(),
                                    primary.getUrl(),
                                    false,false, false);
                        } else {
                            appCmsPresenter.navigateToTVPage(
                                    primary.getPageId(),
                                    primary.getTitle(),
                                    primary.getUrl(),
                                    false,
                                    null,
                                    true,
                                    false,
                                    false,false, false, false);
                        }
                    }
                }, 500);
            });
        }

        private NavigationUser getNavigationUser() {
            List<NavigationUser> navigationUserList = navigation.getNavigationUser();
            for (NavigationUser navigationUser : navigationUserList) {
                if (appCmsPresenter.isUserLoggedIn() && navigationUser.getAccessLevels().isLoggedIn()) {
                    return navigationUser;
                } else if (!appCmsPresenter.isUserLoggedIn() && navigationUser.getAccessLevels().isLoggedOut()) {
                    return navigationUser;
                }
            }
            return null;
        }

        @Override
        public int getItemCount() {
            int totalCount = 0;
            if (null != this.navigation && null != navigation.getNavigationPrimary())
                totalCount = this.navigation.getNavigationPrimary().size();
            return totalCount;
        }


        class NavItemHolder extends RecyclerView.ViewHolder {
            TextView navItemView;
            LinearLayout fitnessParentLayout;
            RelativeLayout navItemlayout;
            ImageView navIconView;
            View separatorView,separatorViewFirst;

            public NavItemHolder(View itemView) {
                super(itemView);
                fitnessParentLayout = (LinearLayout) itemView.findViewById(R.id.fitness_parent_layout);
                navItemView = (TextView) itemView.findViewById(R.id.nav_item_label);
                navItemlayout = itemView.findViewById(R.id.nav_item_layout);
                navIconView = (ImageView) itemView.findViewById(R.id.nav_item_image);
                separatorView = itemView.findViewById(R.id.nav_item_separator);
                separatorViewFirst = itemView.findViewById(R.id.nav_item_separator_first);

                separatorViewFirst.setBackgroundColor(Color.parseColor(Utils.getFocusBorderColor(mContext,appCMSPresenter)));
                separatorView.setBackgroundColor(Color.parseColor(Utils.getFocusBorderColor(mContext,appCMSPresenter)));
                //navItemlayout.setBackground(Utils.getNavigationSelector(mContext, appCmsPresenter, false, ContextCompat.getColor(mContext, R.color.left_navigation_unFocused_color)));

                navItemView.setTextColor(Color.parseColor(Utils.getTextColor(mContext, appCmsPresenter)));
                navItemView.setTypeface(Utils.getSpecificTypeface(
                        mContext,
                        appCMSPresenter,
                        mContext.getString(R.string.app_cms_page_font_regular_key)));

                navItemlayout.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                    @Override
                    public void onFocusChange(View view, boolean hasFocus) {
                        int position = (int) navItemView.getTag(R.string.item_position);
                        selectedPosition = position;
                        final NavigationPrimary primary = (NavigationPrimary) getItem(position);
                        if (hasFocus) {
                            if(isPageToBeSelect(primary,mSelectedPageId))
                                navItemlayout.setBackgroundColor(ContextCompat.getColor(mContext, R.color.left_navigation_focused_color));
                            else
                                navItemlayout.setBackgroundColor(ContextCompat.getColor(mContext, R.color.left_navigation_unFocused_color));
                            separatorView.setVisibility(View.VISIBLE);
                        } else {
                            if(isPageToBeSelect(primary,mSelectedPageId))
                                navItemlayout.setBackgroundColor(0x15000000 + ContextCompat.getColor(mContext, R.color.left_navigation_focused_color));
                            else
                            navItemlayout.setBackgroundColor(ContextCompat.getColor(mContext, R.color.transparentColor));
                            separatorView.setVisibility(View.INVISIBLE);

                        }
                    }
                });

                navItemlayout.setOnKeyListener(new View.OnKeyListener() {
                    @Override
                    public boolean onKey(View view, int i, KeyEvent keyEvent) {
                        int keyCode = keyEvent.getKeyCode();
                        int action = keyEvent.getAction();
                        if (action == KeyEvent.ACTION_DOWN) {
                            int position = (int) navItemView.getTag(R.string.item_position);
                            selectedPosition = position;
                            switch (keyCode) {
                                case KeyEvent.KEYCODE_DPAD_UP:
                                    if (isStartPosition()) {
                                        return false;
                                    }
                                    break;
                                case KeyEvent.KEYCODE_DPAD_DOWN:
                                    if (isEndPosition()) {
                                        return true;
                                    }
                                    break;
                            }
                        }
                        return false;
                    }

                });
            }
        }
    }

    private void createLiveToggle(View view) {

        aSwitch = view.findViewById(R.id.setting_switch);
        aSwitch.setVisibility(View.VISIBLE);

        layoutSwitch = view.findViewById(R.id.layout_switch);
        layoutSwitch.setFocusable(false);

        liveSeparatorView = view.findViewById(R.id.live_nav_item_separator);
        liveSeparatorView.setFocusable(false);

        Utils.setToggleViewStyle(layoutSwitch,liveSeparatorView,getActivity(),appCMSPresenter,aSwitch);

        isEnabled = appCMSPresenter
                .getLivePlayerPreference();
        aSwitch.setChecked(isEnabled);
        aSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            isEnabled = isChecked;
            appCMSPresenter.setLivePlayerPreference(isEnabled);
            if(isEnabled){
                com.viewlift.tv.utility.Utils.isMiniPlayer = true;
                aSwitch.setTextColor((appCMSPresenter.getBrandPrimaryCtaTextColor()));
            }else{
                com.viewlift.tv.utility.Utils.isMiniPlayer = false;
                aSwitch.setTextColor((appCMSPresenter.getBrandSecondaryCtaTextColor()));
            }
            AppCmsHomeActivity activity = (AppCmsHomeActivity) getActivity();
            if(!activity.isMiniPlayerVisibleToPage() && isEnabled){
                isEnabled = false;
            }
            if(!Utils.isPlayerSelected){
                if(isEnabled) {
                    appCMSPresenter.sendBroadcastToHandleMiniPlayer(isEnabled);
                }else{
                    if(appCMSPresenter.getCurrentActivity().findViewById(R.id.mini_video_player_View) != null){
                        appCMSPresenter.getCurrentActivity().findViewById(R.id.mini_video_player_View).setVisibility(View.GONE);
                    }
                }
            }

        });

        aSwitch.setOnKeyListener((view1, i, keyEvent) -> {
            int keyCode = keyEvent.getKeyCode();
            int action = keyEvent.getAction();
            if (action == KeyEvent.ACTION_DOWN) {
                switch (keyCode) {
                    case KeyEvent.KEYCODE_DPAD_UP:
                        return true;

                    case KeyEvent.KEYCODE_DPAD_RIGHT:
                        aSwitch.setFocusable(false);
                        return true;
                }
            }
            return false;
        });

        if(isEnabled){
            aSwitch.setTextColor((appCMSPresenter.getBrandPrimaryCtaTextColor()));
        }else{
            aSwitch.setTextColor((appCMSPresenter.getBrandSecondaryCtaTextColor()));
        }
    }

    private boolean isMoreOptionExist(List<NavigationPrimary> navigationPrimaryList){
        if(navigationPrimaryList != null && navigationPrimaryList.size() > 0) {
            for (int i = 0; i < navigationPrimaryList.size(); i++) {
                if (navigationPrimaryList.get(i).getPagePath() != null && navigationPrimaryList.get(i).getPagePath().equalsIgnoreCase("More"))
                    return true;
            }
        }
        return false;
    }


    private NavigationUser getNavigationUser(Navigation navigation, AppCMSPresenter appCMSPresenter) {
        List<NavigationUser> navigationUserList = navigation.getNavigationUser();
        if(!appCMSPresenter.isUserLoggedIn() && isMoreOptionExist(navigation.getNavigationPrimary())) {
            NavigationFooter navigationFooter = navigation.getNavigationFooter().get(0);
            NavigationUser navigationUser = new NavigationUser();
            navigationUser.setPageId(navigationFooter.getPageId());
            navigationUser.setAccessLevels(navigationFooter.getAccessLevels());
            navigationUser.setAnchor(navigationFooter.getAnchor());
            navigationUser.setDisplayedName(navigationFooter.getDisplayedName());
            navigationUser.setDisplayedPath(navigationFooter.getDisplayedPath());
            navigationUser.setTitle(navigationFooter.getTitle());
            navigationUser.setUrl(navigationFooter.getUrl());
            navigationUser.setIcon(navigationFooter.getIcon());
            return navigationUser;
        }else{
            for (NavigationUser navigationUser : navigationUserList) {
                if (appCMSPresenter.isUserLoggedIn() && navigationUser.getAccessLevels().isLoggedIn()) {
                    return navigationUser;
                } else if (!appCMSPresenter.isUserLoggedIn() && navigationUser.getAccessLevels().isLoggedOut()) {
                    return navigationUser;
                }
            }
        }
        return null;
    }

}




