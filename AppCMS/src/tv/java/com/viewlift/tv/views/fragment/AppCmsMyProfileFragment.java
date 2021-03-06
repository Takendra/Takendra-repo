package com.viewlift.tv.views.fragment;


import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.viewlift.AppCMSApplication;
import com.viewlift.R;
import com.viewlift.models.data.appcms.api.ContentDatum;
import com.viewlift.models.data.appcms.api.Module;
import com.viewlift.presenters.AppCMSPresenter;
import com.viewlift.tv.views.activity.AppCmsBaseActivity;
import com.viewlift.tv.views.activity.AppCmsHomeActivity;
import com.viewlift.tv.views.component.AppCMSTVViewComponent;
import com.viewlift.tv.views.component.DaggerAppCMSTVViewComponent;
import com.viewlift.tv.views.customviews.AppCMSTVTrayAdapter;
import com.viewlift.tv.views.customviews.TVModuleView;
import com.viewlift.tv.views.customviews.TVPageView;
import com.viewlift.tv.views.module.AppCMSTVPageViewModule;
import com.viewlift.views.binders.AppCMSBinder;

import java.util.Arrays;
import java.util.List;



public class AppCmsMyProfileFragment extends BaseFragment implements AppCmsSubNavigationFragment.OnSubNavigationVisibilityListener {

    private static final String TAG = AppCmsMyProfileFragment.class.getSimpleName();
    private AppCmsSubNavigationFragment appCmsSubNavigationFragment;
    private AppCMSBinder mAppCMSBinder;
    private AppCMSPresenter appCMSPresenter;
    private AppCMSTVViewComponent appCmsViewComponent;
    private TVPageView tvPageView;
    private View subNavigationPlaceholder;
    private RelativeLayout subNavContaineer;
    private Context mContext;
    private List<String> modulesToIgnoreList;

    public static AppCmsMyProfileFragment newInstance(Context context, AppCMSBinder appCMSBinder) {
        AppCmsMyProfileFragment appCmsTVPageFragment = new AppCmsMyProfileFragment();
        Bundle bundle = new Bundle();
        bundle.putBinder("app_cms_binder", appCMSBinder);
        appCmsTVPageFragment.setArguments(bundle);
        return appCmsTVPageFragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        Bundle bundle = getArguments();
        mAppCMSBinder = (AppCMSBinder) bundle.getBinder("app_cms_binder");
        appCMSPresenter = ((AppCMSApplication) getActivity().getApplication())
                .getAppCMSPresenterComponent()
                .appCMSPresenter();

        if (appCmsViewComponent == null && mAppCMSBinder != null) {
            appCmsViewComponent = buildAppCMSViewComponent();
        }

        if (appCmsViewComponent != null) {
            tvPageView = appCmsViewComponent.appCMSTVPageView();
        } else {
            tvPageView = null;
        }

        if (tvPageView != null) {
            if (tvPageView.getParent() != null) {
                ((ViewGroup) tvPageView.getParent()).removeAllViews();
            }
        }
        if (container != null) {
            container.removeAllViews();
        }


        View view = inflater.inflate(R.layout.app_cms_my_profile_fragment, null);
        subNavigationPlaceholder = view.findViewById(R.id.sub_navigation_placholder);
        subNavContaineer = (RelativeLayout) view.findViewById(R.id.sub_navigation_containeer);

        FrameLayout pageHolder = (FrameLayout) view.findViewById(R.id.profile_placeholder);

        if (appCMSPresenter.getTemplateType().equals(AppCMSPresenter.TemplateType.ENTERTAINMENT) ||
                appCMSPresenter.isNewsLeftNavigationEnabled()) {
            setSubNavigationFragment();
            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) pageHolder.getLayoutParams();
            params.addRule(RelativeLayout.BELOW, R.id.sub_navigation_containeer);
            showSubNavigation(true);

        } else {
            if (subNavigationPlaceholder != null) {
                subNavigationPlaceholder.setVisibility(View.GONE);
                subNavContaineer.setVisibility(View.GONE);
            }
        }

        if (appCMSPresenter.isLeftNavigationEnabled() && !appCMSPresenter.isNewsLeftNavigationEnabled()) {
            RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) subNavigationPlaceholder.getLayoutParams();
            layoutParams.height = ViewGroup.LayoutParams.MATCH_PARENT;
            layoutParams.width = 600;

            layoutParams.setMargins(0, 0, 0, 0);
            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) pageHolder.getLayoutParams();
            params.removeRule(RelativeLayout.BELOW);
            subNavContaineer.bringToFront();
            subNavContaineer.setBackground(getActivity().getDrawable(R.drawable.left_nav_gradient));
            subNavContaineer.getBackground().setTint(Color.parseColor(appCMSPresenter.getAppBackgroundColor()));

            if (null != getActivity() && getActivity() instanceof AppCmsBaseActivity) {
                if (!((AppCmsBaseActivity) getActivity()).isProfileFirstTime()) {
                    showSubNavigation(false);
                }
            }
        }

        pageHolder.addView(tvPageView);
        tvPageView.setBackgroundColor(Color.parseColor(appCMSPresenter.getAppCMSMain().getBrand().getGeneral().getBackgroundColor()));
        return view;
    }


    @Override
    public boolean isSubNavigationVisible() {
        if (null != subNavigationPlaceholder) {
            return subNavigationPlaceholder.getVisibility() == View.VISIBLE;
        }
        return false;
    }

    boolean isSubNavExist = false;

    @Override
    public boolean isSubNavExist() {
        return isSubNavExist;
    }

    @Override
    public void setSubnavExistence(boolean isExist) {
        isSubNavExist = isExist;
    }

    @Override
    public void showSubNavigation(boolean shouldShow) {
        if (!appCMSPresenter.isNewsLeftNavigationEnabled()) {
            try {
                RecyclerView recyclerView = ((AppCmsHomeActivity) mContext).findViewById(R.id.tv_recycler_view);
                if (recyclerView != null) {
                    recyclerView.setDescendantFocusability(shouldShow
                                    ? RecyclerView.FOCUS_BLOCK_DESCENDANTS
                                    : RecyclerView.FOCUS_AFTER_DESCENDANTS);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (null != subNavigationPlaceholder) {
                subNavigationPlaceholder.setVisibility(shouldShow ? View.VISIBLE : View.INVISIBLE);
                subNavContaineer.setVisibility(shouldShow ? View.VISIBLE : View.INVISIBLE);
                if (null != appCmsSubNavigationFragment && null != appCmsSubNavigationFragment.getCustomAdapter()) {
                    appCmsSubNavigationFragment.setFocusonSelectedItem();
                }
                new Handler().post(() -> {
                    if (/*shouldShow && */null != appCmsSubNavigationFragment) {
                        appCmsSubNavigationFragment.setFocusable(shouldShow);
                    }
                });
            }
        }
    }

    public void updateAdapterData(AppCMSBinder appCmsBinder) {
        try {
            TVModuleView tvModuleView = (TVModuleView) tvPageView.getChildrenContainer().getChildAt(0);
            int childCount = tvModuleView.getChildrenContainer().getChildCount();
            for (int i = 0; i < childCount; i++) {
                if (null != tvModuleView.getChildrenContainer().getChildAt(i)
                        && tvModuleView.getChildrenContainer().getChildAt(i) instanceof RecyclerView) {
                    RecyclerView recyclerView = (RecyclerView) tvModuleView.getChildrenContainer().getChildAt(i);
                    //HistoryModule
                    List<ContentDatum> contentDatumList = null;
                    for (Module module : appCmsBinder.getAppCMSPageAPI().getModules()) {
                        if (module.getModuleType().equalsIgnoreCase("HistoryModule")) {
                            contentDatumList = module.getContentData();
                            break;
                        }
                    }
                    if (contentDatumList != null && contentDatumList.size() > 0) {
                        ((AppCMSTVTrayAdapter) recyclerView.getAdapter()).setContentData(contentDatumList);
                    }
                    if (contentDatumList != null && contentDatumList.size() == 0) {
                        View view = tvModuleView.findViewById(R.id.appcms_removeall);
                        if (null != view) {
                            view.setVisibility(View.GONE);
                        }
                    }
                }
            }
        } catch (Exception e) {

        }
    }


    @Override
    public void onResume() {
        super.onResume();
        if (null != appCMSPresenter) {
            appCMSPresenter.sendStopLoadingPageAction(false, null);
            if(!appCMSPresenter.isNewsTemplate() || (getActivity() instanceof  AppCmsHomeActivity && ((AppCmsHomeActivity)getActivity()).isMiniPlayerVisibleToPage())) {
                appCMSPresenter.sendBroadcastToHandleMiniPlayer(true);
           }
            if (appCMSPresenter.isUserLoggedIn()
                    && mAppCMSBinder.getPageName().equalsIgnoreCase(getString(R.string.app_cms_watchlist_navigation_title))) {
                updateAdapterData(mAppCMSBinder);
            }
        }

        if (isSubNavExist && isSubNavigationVisible()) {

        }
    }

    private void setSubNavigationFragment() {
        appCmsSubNavigationFragment = AppCmsSubNavigationFragment.newInstance(getActivity(), this);
        Bundle bundle = new Bundle();
        bundle.putBinder("app_cms_binder", mAppCMSBinder);
        appCmsSubNavigationFragment.setArguments(bundle);
        appCmsSubNavigationFragment.setSelectedPageId(mAppCMSBinder.getPageId());
        getChildFragmentManager().beginTransaction().replace(R.id.sub_navigation_placholder, appCmsSubNavigationFragment).commitAllowingStateLoss();
    }


    public AppCMSTVViewComponent buildAppCMSViewComponent() {
        return DaggerAppCMSTVViewComponent.builder()
                .appCMSTVPageViewModule(new AppCMSTVPageViewModule(getActivity(),
                        mAppCMSBinder.getAppCMSPageUI(),
                        mAppCMSBinder.getAppCMSPageAPI(),
                        mAppCMSBinder.getJsonValueKeyMap(),
                        appCMSPresenter,
                        mAppCMSBinder.getPageId()
                ))
                .build();
    }

    public void updateBinder(AppCMSBinder appCmsBinder) {
        mAppCMSBinder = appCmsBinder;
    }


    public void refreshPage() {
        if (null != appCmsViewComponent) {
            if(appCMSPresenter.getAppCMSMain().isMonetizationModelEnabled()){
                modulesToIgnoreList = Arrays.asList(getResources().getStringArray(R.array.app_cms_deprecate_modules_ignore));
            }else{
                modulesToIgnoreList = Arrays.asList(getResources().getStringArray(R.array.app_cms_modules_to_ignore_tv));
            }
            appCmsViewComponent.tvviewCreator().refreshPageView(tvPageView,
                    mAppCMSBinder.getAppCMSPageUI(),
                    mAppCMSBinder.getAppCMSPageAPI(),
                    mAppCMSBinder.getJsonValueKeyMap(),
                    appCMSPresenter,
                    Arrays.asList(getResources().getStringArray(R.array.app_cms_modules_to_ignore_tv)
                    ), mAppCMSBinder.getContentDatum());
        }
    }
}
