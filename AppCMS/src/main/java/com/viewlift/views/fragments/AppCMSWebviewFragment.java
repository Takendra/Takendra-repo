package com.viewlift.views.fragments;


import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.fragment.app.Fragment;

import com.google.gson.Gson;
import com.viewlift.AppCMSApplication;
import com.viewlift.R;
import com.viewlift.Utils;
import com.viewlift.db.AppPreference;
import com.viewlift.models.data.appcms.ui.main.LocalisedStrings;
import com.viewlift.models.data.verimatrix.ResourceAccess;
import com.viewlift.models.data.verimatrix.TVProvider;
import com.viewlift.models.data.verimatrix.VerimatrixResponse;
import com.viewlift.presenters.AppCMSPresenter;
import com.viewlift.utils.CommonUtils;
import com.viewlift.views.binders.AppCMSBinder;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * A simple {@link Fragment} subclass.
 */
public class AppCMSWebviewFragment extends Fragment {
    @Inject
    AppCMSPresenter appCMSPresenter;
    @Inject
    AppPreference appPreference;
    @Inject
    LocalisedStrings localisedStrings;
    private AppCMSBinder appCMSBinder;
    @BindView(R.id.webview)
    WebView webview;
    boolean fullAppLogout = false;
    Unbinder unbinder;
    TVProvider slectedProvider = null;

    public AppCMSWebviewFragment() {
        // Required empty public constructor
    }

    public static AppCMSWebviewFragment newInstance(Context context, AppCMSBinder appCMSBinder) {
        AppCMSWebviewFragment fragment = new AppCMSWebviewFragment();
        Bundle args = new Bundle();
        args.putBinder(context.getString(R.string.fragment_page_bundle_key), appCMSBinder);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_webview, container, false);
        unbinder = ButterKnife.bind(this, view);
        appCMSBinder = ((AppCMSBinder) getArguments().getBinder(getContext().getString(R.string.fragment_page_bundle_key)));
        ((AppCMSApplication) getActivity().getApplicationContext()).getAppCMSPresenterComponent().inject(this);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (Utils.isColorDark(appCMSPresenter.getGeneralBackgroundColor())) {
                getActivity().getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            } else {
                getActivity().getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            }
        }
        webview.loadUrl(appCMSBinder.getLaunchData().getExtraData()[0]);
        webview.getSettings().setJavaScriptEnabled(true);
        webview.addJavascriptInterface(new MyJavaScriptInterface(), "HTMLOUT");
        webview.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                Log.e("ulr", url);
                view.loadUrl(url);
                return true;
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                view.loadUrl("javascript:window.HTMLOUT.processHTML(document.body.getElementsByTagName('pre')[0].innerHTML);");
            }
        });
        return view;
    }

    class MyJavaScriptInterface {

        MyJavaScriptInterface() {
        }

        @JavascriptInterface
        public void processHTML(String html) {
            Gson gson = new Gson();
            VerimatrixResponse verimatrixResponse = gson.fromJson(html, VerimatrixResponse.class);
            if (verimatrixResponse != null) {
                if (verimatrixResponse.getApiType().equalsIgnoreCase(getString(R.string.verimatrix_api_type_logout))) {
                    /*if (fullAppLogout)
                        appCMSPresenter.logout();
                    else {
                        appCMSPresenter.sendCloseOthersAction(null, true, false);
                        appPreference.setTVEUserId(null);
                    }*/
                    appCMSPresenter.logout();
                } else if (verimatrixResponse.getApiType().equalsIgnoreCase(getString(R.string.verimatrix_api_type_bounce))) {
//                    appCMSPresenter.sendCloseOthersAction(null, true, false);
                    appCMSPresenter.updateTVEAsyncTask(verimatrixResponse.isAuthenticated());
                    /*if (!verimatrixResponse.isAuthenticated()) {
                        if (appPreference.getLoggedInUserEmail() == null)
                            fullAppLogout = true;
                        String url = getContext().getString(R.string.verimatrix_logout, getContext().getString(R.string.verimatrix__base_url),
                                getContext().getString(R.string.verimatrix_partner_id));
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                webview.loadUrl(url);
                            }
                        });
                    } else*/


                } else if (verimatrixResponse.getApiType().equalsIgnoreCase(getString(R.string.verimatrix_api_type_resource_access))) {
                    appCMSPresenter.verimatrixContentAuthorization(verimatrixResponse.isAuthenticated());
                } else if (verimatrixResponse.getApiType().equalsIgnoreCase(getString(R.string.verimatrix_api_type_resource_access_list))) {
                    slectedProvider.setResourceIds(getResoureId(verimatrixResponse.getResourceAccess()));
                    appCMSPresenter.startLoginTVEAsyncTask(slectedProvider.getUserId(), slectedProvider);
                } else if (verimatrixResponse.getApiType().equalsIgnoreCase(getString(R.string.verimatrix_api_type_init))) {
                    if (verimatrixResponse.isAuthenticated()) {
                        for (String key : verimatrixResponse.getProviders().keySet()) {
                            slectedProvider = verimatrixResponse.getProviders().get(key);
                        }
                        slectedProvider.setUserId(verimatrixResponse.getUserId());
                        if (appCMSPresenter.getmFireBaseAnalytics() != null)
                            appCMSPresenter.getmFireBaseAnalytics().logEvent(getString(R.string.firebase_event_name_tve_login_succes), new Bundle());
                        appCMSPresenter.getAppPreference().setTvProviderLogo(slectedProvider.getImages().getImageUrl());
                        if (authzUrl() != null) {
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    webview.loadUrl(authzUrl());
                                }
                            });
                        } else
                            appCMSPresenter.startLoginTVEAsyncTask(slectedProvider.getUserId(), slectedProvider);
                    } else {
                        if (appCMSPresenter.getmFireBaseAnalytics() != null)
                            appCMSPresenter.getmFireBaseAnalytics().logEvent(getString(R.string.firebase_event_name_tve_login_fail), new Bundle());
                        appCMSPresenter.sendCloseOthersAction(null, true, false);
                    }

                }
            }
        }

    }

    private String authzUrl() {
        String url = null;
        String resource = CommonUtils.getVerimatrixResourceIds(appCMSPresenter.getAppCMSAndroid(), getString(R.string.tvprovider_verimatrix));
        if (resource != null) {
            String[] resourceIds = resource.split(",");
            if (resourceIds.length > 0) {
                StringBuilder resId = new StringBuilder();
                for (String resourceId : resourceIds) {
                    resId.append("resource_id=");
                    resId.append(resourceId);
                    resId.append("&");
                }
                url = getString(R.string.verimatrix_resource_access_list, getString(R.string.verimatrix__base_url),
                        CommonUtils.getVerimatrixPartnerId(appCMSPresenter.getAppCMSAndroid(), getString(R.string.tvprovider_verimatrix)), resId.toString());
            }

        }
        return url;
    }

    private Map<String, Boolean> getResoureId(List<ResourceAccess> resourceAccess) {
        Map<String, Boolean> resource = new HashMap<>();
        for (int i = 0; i < resourceAccess.size(); i++) {
            resource.put(resourceAccess.get(i).getResource(), resourceAccess.get(i).getAuthenticated());
        }
        return resource;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
