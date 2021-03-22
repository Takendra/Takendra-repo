package com.viewlift.models.network.background.tasks;

import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.viewlift.R;
import com.viewlift.models.data.appcms.api.AppCMSPageAPI;
import com.viewlift.models.data.appcms.api.ContentDatum;
import com.viewlift.models.data.appcms.api.Module;
import com.viewlift.models.data.appcms.search.AppCMSSearchResult;
import com.viewlift.models.data.appcms.ui.android.MetaPage;
import com.viewlift.models.network.rest.AppCMSSearchCall;
import com.viewlift.presenters.AppCMSPresenter;
import com.viewlift.utils.CommonUtils;
import com.viewlift.views.activity.AppCMSPageActivity;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.functions.Action1;

public class SearchQuery {
    AppCMSPresenter appCMSPresenter;

    public void searchInstance(AppCMSPresenter appcmsPresenter) {
        this.appCMSPresenter = appcmsPresenter;
    }

    private class SearchAsyncTask extends AsyncTask<String, Void, List<AppCMSSearchResult>> {
        final Action1<List<AppCMSSearchResult>> dataReadySubscriber;
        final AppCMSSearchCall appCMSSearchCall;
        final String apiKey;

        SearchAsyncTask(Action1<List<AppCMSSearchResult>> dataReadySubscriber,
                        AppCMSSearchCall appCMSSearchCall,
                        String apiKey) {
            this.dataReadySubscriber = dataReadySubscriber;
            this.appCMSSearchCall = appCMSSearchCall;
            this.apiKey = apiKey;
            appCMSPresenter.showLoader();
        }

        @Override
        protected List<AppCMSSearchResult> doInBackground(String... params) {
            if (params.length > 1) {
                try {
                    return appCMSSearchCall.call(params[0], params[1], params[2]);
                } catch (IOException e) {
                    //Log.e(TAG, "I/O DialogType retrieving search data from URL: " + params[0]);
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(List<AppCMSSearchResult> result) {
            Observable.just(result).subscribe(dataReadySubscriber);
        }
    }

    String searchTerm;
    String queryTerm;

    public void searchQuery(String query) {
        queryTerm = query;
        searchTerm = queryTerm;

        if (!TextUtils.isEmpty(searchTerm)) {
            //Send Search Term in Firebase Analytics Logs
            appCMSPresenter.getFirebaseAnalytics().searchQueryEvent(queryTerm);

            String countryCode = appCMSPresenter.isUserLoggedIn() ? CommonUtils.getCountryCodeFromAuthToken(appCMSPresenter.getAuthToken()) : CommonUtils.getCountryCodeFromAuthToken(appCMSPresenter.getAppPreference().getAnonymousUserToken());
            final String url = appCMSPresenter.getCurrentActivity().getString(R.string.app_cms_search_api_url_with_types,
                    appCMSPresenter.getApiBaseUrl(),
                    appCMSPresenter.getAppCMSMain().getInternalName(),
                    searchTerm,
                    appCMSPresenter.getLanguageParamForAPICall(),
                    appCMSPresenter.getCurrentActivity().getString(R.string.search_types_for_app),
                    countryCode);

            if (appCMSPresenter.getCurrentActivity() instanceof AppCMSPageActivity) {
                new SearchAsyncTask(data -> {
                    if (data != null) {
                        AppCMSPageAPI pageApi = convertToAppCMSPageAPI(data);
                        System.out.println("page api data-" + pageApi);
                        appCMSPresenter.getPageAPILruCache().put(appCMSPresenter.getSearchPage().getPageId(), pageApi);
                        appCMSPresenter.getFirebaseAnalytics().screenViewEvent(appCMSPresenter.getCurrentContext().getString(R.string.value_search_result_screen));
                        if (pageApi.getModules() != null && pageApi.getModules().size() > 0 && pageApi.getModules().get(0).getContentData() != null
                                && pageApi.getModules().get(0).getContentData().size() > 0) {
                            new SearchQuery().updateMessage(appCMSPresenter, false, appCMSPresenter.getLocalisedStrings().getNoSearchResultText());
                        } else {
                            new SearchQuery().updateMessage(appCMSPresenter, true, appCMSPresenter.getLocalisedStrings().getNoSearchResultText());
                        }
                        appCMSPresenter.navigateToSearchPage(appCMSPresenter.isNavbarPresent(), appCMSPresenter.isAppbarPresent());
                    }
                }, (((AppCMSPageActivity) appCMSPresenter.getCurrentActivity()).appCMSSearchCall), appCMSPresenter.getApiKey()).execute(url,
                        appCMSPresenter.getApiKey(), appCMSPresenter.getAuthToken());
            }
        }
    }

    public void searchEmptyQuery(String query, boolean navbarPresent, boolean appbarPresent) {
        queryTerm = query;
        searchTerm = queryTerm;
        AppCMSPageAPI pageApi = new AppCMSPageAPI();
        MetaPage searchMetaPage = appCMSPresenter.getSearchPage();
        if (searchMetaPage != null) {
            appCMSPresenter.getPageAPILruCache().put(searchMetaPage.getPageId(), pageApi);
            appCMSPresenter.navigateToSearchPage(navbarPresent, appbarPresent);
            new SearchQuery().updateMessage(appCMSPresenter, false, appCMSPresenter.getLocalisedStrings().getNoSearchResultText());
        }
    }

    public void libraryEmptyQuery(String query) {
        queryTerm = query;
        searchTerm = queryTerm;
        AppCMSPageAPI pageApi = new AppCMSPageAPI();
        appCMSPresenter.getPageAPILruCache().put(appCMSPresenter.getSearchPage().getPageId(), pageApi);
        new SearchQuery().updateMessage(appCMSPresenter, false, appCMSPresenter.getLocalisedStrings().getNotPurchasedText());
    }

    public void updateMessage(AppCMSPresenter appCMSPresenter, boolean isShow, String text) {
        if(appCMSPresenter.getCurrentActivity() == null)
            return;
        View searchView = appCMSPresenter.getCurrentActivity().findViewById(R.id.search_layout);
        if (searchView != null) {
            if (isShow)
                searchView.setVisibility(View.VISIBLE);
            else
                searchView.setVisibility(View.GONE);
        }
        TextView tvNoSearch = appCMSPresenter.getCurrentActivity().findViewById(R.id.no_search);
        if (tvNoSearch != null) {
            tvNoSearch.setTextColor(appCMSPresenter.getGeneralTextColor());
            tvNoSearch.setText(text);
        }
    }

    public AppCMSPageAPI convertToAppCMSPageAPI(List<AppCMSSearchResult> searchData) {
        AppCMSPageAPI appCMSPageAPI = new AppCMSPageAPI();
        Module module = new Module();
        List<ContentDatum> data = new ArrayList<>();
        for (AppCMSSearchResult searchResult : searchData) {
            data.add(searchResult.getContent(appCMSPresenter.getCurrentContext()));
        }
        module.setContentData(data);
        appCMSPageAPI.setId("");
        List<Module> moduleList = new ArrayList<>();
        moduleList.add(module);
        appCMSPageAPI.setModules(moduleList);
        return appCMSPageAPI;
    }
}
