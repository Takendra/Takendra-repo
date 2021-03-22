package com.viewlift.views.fragments;

import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.viewlift.AppCMSApplication;
import com.viewlift.R;
import com.viewlift.db.AppPreference;
import com.viewlift.models.data.appcms.api.BaseInterface;
import com.viewlift.models.data.appcms.api.Language;
import com.viewlift.models.data.appcms.api.Mpeg;
import com.viewlift.presenters.AppCMSPresenter;
import com.viewlift.utils.LocaleUtils;
import com.viewlift.views.adapters.AppCMSDownloadQualityAdapter;
import com.viewlift.views.adapters.AppCMSDownloadRadioAdapter;
import com.viewlift.views.binders.AppCMSDownloadQualityBinder;
import com.viewlift.views.components.AppCMSViewComponent;
import com.viewlift.views.components.DaggerAppCMSViewComponent;
import com.viewlift.views.customviews.PageView;
import com.viewlift.views.modules.AppCMSPageViewModule;

import java.util.HashMap;
import java.util.List;

import javax.inject.Inject;

/**
 * Created by sandeep.singh on 7/28/2017.
 */

public class AppCMSDownloadQualityFragment extends Fragment
        implements AppCMSDownloadRadioAdapter.ItemClickListener<BaseInterface> {

    private static final String TAG = AppCMSDownloadQualityFragment.class.getSimpleName();
    private AppCMSDownloadQualityBinder binder;
    @Inject
    AppPreference appPreference;
    @Inject
    AppCMSPresenter appCMSPresenter;
    private AppCMSViewComponent appCMSViewComponent;
    private PageView pageView;
    private String downloadQuality;
    private int downloadQualityPosition;
    private boolean downloadQualityChanged;
    private Language selectedLanguage;
    HashMap<String, Integer> downloadMapping = new HashMap<>();
    RecyclerView listDownloadQuality;
    public AppCMSDownloadQualityFragment() {
        // Required empty public constructor
    }

    public static AppCMSDownloadQualityFragment newInstance(Context context, AppCMSDownloadQualityBinder binder) {
        AppCMSDownloadQualityFragment fragment = new AppCMSDownloadQualityFragment();
        Bundle args = new Bundle();
        args.putBinder(context.getString(R.string.app_cms_download_setting_binder_key), binder);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        try {
            super.onAttach(context);

            binder = (AppCMSDownloadQualityBinder)
                    getArguments().getBinder(context.getString(R.string.app_cms_download_setting_binder_key));

            ((AppCMSApplication) context.getApplicationContext()).getAppCMSPresenterComponent().inject(this);
            appCMSViewComponent = buildAppCMSViewComponent();
        } catch (ClassCastException e) {
            //Log.e(TAG, "Could not attach fragment: " + e.toString());
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (appCMSViewComponent == null && binder != null) {
            appCMSViewComponent = buildAppCMSViewComponent();
        }

        if (appCMSViewComponent != null) {
            pageView = appCMSViewComponent.appCMSPageView();
        } else {
            pageView = null;
        }


        if (pageView != null) {
            RecyclerView nestedScrollView = pageView.findViewById(R.id.home_nested_scroll_view);

            nestedScrollView.addOnItemTouchListener(new RecyclerView.SimpleOnItemTouchListener() {
                @Override
                public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
                    // Stop only scrolling.
                    return rv.getScrollState() == RecyclerView.SCROLL_STATE_DRAGGING;
                }
            });
            if (pageView.getParent() != null) {
                ((ViewGroup) pageView.getParent()).removeAllViews();
            }

            appCMSPresenter.setAppOrientation();
        }

        if (container != null) {
            container.removeAllViews();
        }

        if (pageView != null) {

            listDownloadQuality = (RecyclerView) (pageView.findChildViewById(R.id.tableViewSettingsList) != null ?
                    pageView.findChildViewById(R.id.tableViewSettingsList) : pageView.findChildViewById(R.id.download_quality_selection_list));
            Button continueButton = (Button) (pageView.findChildViewById(R.id.continueButton) != null ?
                    pageView.findChildViewById(R.id.continueButton) :
                    pageView.findChildViewById(R.id.download_quality_continue_button));
            Button cancelButton = (Button) (pageView.findChildViewById(R.id.cancelButton) != null ?
                    pageView.findChildViewById(R.id.cancelButton) :
                    pageView.findChildViewById(R.id.download_quality_cancel_button));
            TextView title = (TextView) pageView.findChildViewById(R.id.DownloadQualityTitle);

            if (title != null)
            title.setTextColor(appCMSPresenter.getGeneralTextColor());

            if (listDownloadQuality != null) {
                ((AppCMSDownloadQualityAdapter) listDownloadQuality.getAdapter()).setItemClickListener(this);
                listDownloadQuality.setBackgroundColor(Color.parseColor("#00000000"));
            }
            if (continueButton != null) {
                continueButton.setBackgroundColor(appCMSPresenter.getBrandPrimaryCtaColor());
                continueButton.setTextColor(Color.parseColor(appCMSPresenter.getAppCtaTextColor()));
                continueButton.setOnClickListener(v -> {
                    if (downloadQualityChanged) {
                        appPreference.setUserDownloadQualityPref(downloadQuality);
                        appCMSPresenter.sendDownloadBitrateEvent(downloadQuality);
                        appPreference.setUserDownloadQualityPositionref(downloadQualityPosition);
                    }
                    if (binder != null &&
                            binder.getEntitlementContentDatum() != null &&
                            (binder.getResultAction1() != null || appCMSPresenter.isNewsTemplate())) {
                        appPreference.setUserDownloadQualityPref(downloadQuality);
                        appCMSPresenter.sendDownloadBitrateEvent(downloadQuality);
                        appCMSPresenter.setEpisodeId(null);
                        appCMSPresenter.setPlayshareControl(true);
                        appCMSPresenter.editDownload(binder.getEntitlementContentDatum(),binder.getEntitlementContentDatum(), binder.getResultAction1(), null,null);
                    } else {
                        if (appCMSPresenter.isNetworkConnected()) {
                            if (selectedLanguage != null && selectedLanguage.getLanguageCode() != null) {
                                LocaleUtils.setLocale(appCMSPresenter.getCurrentContext(), selectedLanguage.getLanguageCode());
                                //LanguageHelper.setLocale(appCMSPresenter.getCurrentContext(), selectedLanguage.getLanguageCode());
                                AppCMSPresenter.PRE_LANGUAGE = appCMSPresenter.getLanguage().getLanguageCode();
                                appCMSPresenter.setLanguage(selectedLanguage);
                                if (!appCMSPresenter.isUserLoggedIn()) {
                                    appPreference.setAnonymousUserToken(null);
                                } else {
                                    appPreference.setAuthToken(null);
                                }
                                /**
                                 * Activity is taking too much time because of acicution of
                                 */
                                new Handler().post(new Runnable() {
                                    @Override
                                    public void run() {
                                        appCMSPresenter.clearPageApiData();
                                       // appCMSPresenter.navigateToHomePage(true);
                                    }
                                });



                            }
                        } else {
                            appCMSPresenter.showNoNetworkConnectivityToast();
                        }
                    }

                    getActivity().finish();
                });
            }
            if (cancelButton != null) {
                cancelButton.setOnClickListener(v -> getActivity().finish());
            }
            appPreference.setDownloadQualityScreenShowBefore(true);
            pageView.setBackgroundColor(appCMSPresenter.getGeneralBackgroundColor());
            try {
                setBgColor(Color.parseColor(appCMSPresenter.getAppBackgroundColor()));

            } catch (Exception e) {
                setBgColor(ContextCompat.getColor(getContext(), android.R.color.black));
            }

        }
        return pageView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (savedInstanceState != null) {
            try {
                binder = (AppCMSDownloadQualityBinder) savedInstanceState.getBinder(getString(R.string.app_cms_download_setting_binder_key));
            } catch (ClassCastException e) {
                //Log.e(TAG, "Could not attach fragment: " + e.toString());
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (PageView.isTablet(getContext()) || (binder != null && binder.isFullScreenEnabled())) {
            handleOrientation(getActivity().getResources().getConfiguration().orientation);
        }

        if (pageView == null) {
            //Log.e(TAG, "AppCMS page creation error");
        } else {

            //getActivity().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            /*getActivity().getWindow().setBackgroundDrawable(appCMSPresenter != null ?
                    new ColorDrawable(appCMSPresenter.getGeneralBackgroundColor()) :
                    new ColorDrawable(Color.TRANSPARENT));*/
            pageView.notifyAdaptersOfUpdate();
        }

        if (appCMSPresenter != null) {
            appCMSPresenter.dismissOpenDialogs(null);
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

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (binder != null && appCMSViewComponent.viewCreator() != null) {
            appCMSPresenter.removeLruCacheItem(getContext(), binder.getPageId());
        }

        binder = null;
        pageView = null;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBinder(getString(R.string.app_cms_binder_key), binder);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        handleOrientation(newConfig.orientation);
    }

    public AppCMSViewComponent buildAppCMSViewComponent() {
        try {
            return DaggerAppCMSViewComponent.builder()
                    .appCMSPageViewModule(new AppCMSPageViewModule(getContext(),
                            binder.getPageId(),
                            binder.getAppCMSPageUI(),
                            binder.getAppCMSPageAPI(),
                            appCMSPresenter.getAppCMSAndroidModules(),
                            binder.getScreenName(),
                            binder.getJsonValueKeyMap(),
                            appCMSPresenter))
                    .build();
        } catch (Exception e) {
            //Log.e(TAG, e.getMessage());
        }
        return null;
    }

    @Override
    public void onItemClick(BaseInterface item) {
        if (item instanceof Mpeg) {
            this.downloadQuality = ((Mpeg) item).getRenditionValue();
            List<BaseInterface> itemsReturned = ((AppCMSDownloadQualityAdapter) listDownloadQuality.getAdapter()).returnItems();
            for (int i = 0; i < itemsReturned.size(); i++) {
                if (itemsReturned.get(i) instanceof Mpeg) {
                    Mpeg mpeg = (Mpeg) itemsReturned.get(i);
                    if (mpeg.getRenditionValue().equals(this.downloadQuality)) {
                        downloadQualityPosition = i;
                    }
                }
            }
            downloadQualityChanged = true;
        } else if (item instanceof Language) {
            selectedLanguage = ((Language) item);
            downloadQualityChanged = false;
        }
    }

    private void setBgColor(int bgColor) {
        Window window = getActivity().getWindow();
        if (window != null) {
            window.setBackgroundDrawable(new ColorDrawable(bgColor));
        }
    }
}
