package com.viewlift.views.activity;

import android.app.SearchManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.RemoteException;
import android.support.v4.media.MediaBrowserCompat;
import android.support.v4.media.session.MediaControllerCompat;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.viewlift.AppCMSApplication;
import com.viewlift.R;
import com.viewlift.audio.MusicService;
import com.viewlift.models.data.appcms.api.AppCMSPageAPI;
import com.viewlift.models.data.appcms.api.ContentDatum;
import com.viewlift.models.data.appcms.api.Module;
import com.viewlift.models.data.appcms.search.AppCMSSearchResult;
import com.viewlift.models.data.appcms.ui.main.AppCMSMain;
import com.viewlift.models.data.appcms.ui.page.Component;
import com.viewlift.models.network.modules.AppCMSSearchUrlData;
import com.viewlift.models.network.rest.AppCMSSearchCall;
import com.viewlift.presenters.AppCMSPresenter;
import com.viewlift.views.adapters.AppCMSSearchInnerItemAdapter;
import com.viewlift.views.adapters.AppCMSSearchItemAdapter;
import com.viewlift.views.adapters.SearchSuggestionsAdapter;
import com.viewlift.views.customviews.ViewCreator;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.Observable;
import rx.functions.Action1;

/*
 * Created by viewlift on 6/12/17.
 */

public class AppCMSSearchActivity extends AppCompatActivity {

    @BindView(R.id.app_cms_search_results)
    RecyclerView appCMSSearchResultsView;

    @BindView(R.id.search_page_loading_progressbar)
    ProgressBar progressBar;

    @BindView(R.id.app_cms_searchbar)
    SearchView appCMSSearchView;

    @BindView(R.id.app_cms_search_results_container)
    LinearLayout appCMSSearchResultsContainer;

    @BindView(R.id.no_results_textview)
    TextView noResultsTextview;

    @BindView(R.id.app_cms_close_button)
    ImageButton appCMSCloseButton;

    @Inject
    AppCMSSearchUrlData appCMSSearchUrlData;

    @Inject
    AppCMSSearchCall appCMSSearchCall;
    private MediaBrowserCompat mMediaBrowser;

    private String searchQuery;
    private AppCMSSearchItemAdapter appCMSSearchItemAdapter;
    private AppCMSSearchInnerItemAdapter appCMSSearchInnerItemAdapter;

    private BroadcastReceiver handoffReceiver;
    private AppCMSPresenter appCMSPresenter;
    private BroadcastReceiver presenterActionReceiver;
    int col = 2;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        ButterKnife.bind(this);

        appCMSSearchItemAdapter = new AppCMSSearchItemAdapter(this, null);

        appCMSSearchInnerItemAdapter = new AppCMSSearchInnerItemAdapter(this, ((AppCMSApplication) getApplication()).getAppCMSPresenterComponent()
                .appCMSPresenter(), null);


        LinearLayoutManager layoutManager
                = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);

//        if(BaseView.isTablet(this)) {
//            if(BaseView.isLandscape(this)){
//                col=4;
//            }else{
//                col=3;
//            }
//        }else{
//            col=2;
//        }
//
//        GridLayoutManager gridLayoutManager=new GridLayoutManager(this, col,
//                GridLayoutManager.VERTICAL, false);

        appCMSSearchResultsView.setLayoutManager(layoutManager);
        appCMSSearchResultsView.setAdapter(appCMSSearchItemAdapter);
        appCMSPresenter = ((AppCMSApplication) getApplication())
                .getAppCMSPresenterComponent().appCMSPresenter();
        appCMSPresenter.getFirebaseAnalytics().screenViewEvent(appCMSPresenter.getCurrentContext().getString(R.string.value_search_result_screen));
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        @SuppressWarnings("ConstantConditions")
        SearchSuggestionsAdapter searchSuggestionsAdapter = new SearchSuggestionsAdapter(appCMSPresenter, this,
                null,
                searchManager.getSearchableInfo(getComponentName()),
                true);

        appCMSSearchResultsView.requestFocus();

        AppCMSMain appCMSMain =
                ((AppCMSApplication) getApplication()).getAppCMSPresenterComponent()
                        .appCMSPresenter()
                        .getAppCMSMain();

        appCMSPresenter.setAppOrientation();

        handoffReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (intent != null &&
                        intent.getStringExtra(getString(R.string.app_cms_package_name_key)) != null &&
                        !intent.getStringExtra(getString(R.string.app_cms_package_name_key)).equals(getPackageName())) {
                    return;
                }
                String sendingPage = intent.getStringExtra(getString(R.string.app_cms_closing_page_name));
                if (intent.getBooleanExtra(getString(R.string.close_self_key), true) ||
                        sendingPage == null ||
                        !getString(R.string.app_cms_navigation_page_tag).equals(sendingPage)) {
                    //Log.d(TAG, "Closing activity");
                    finish();
                }
            }
        };

        presenterActionReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                if (intent.getAction().equals(AppCMSPresenter.PRESENTER_PAGE_LOADING_ACTION)) {
                    progressBar.setVisibility(View.VISIBLE);

                } else if (intent.getAction().equals(AppCMSPresenter.PRESENTER_STOP_PAGE_LOADING_ACTION)) {
                    progressBar.setVisibility(View.INVISIBLE);

                }
            }
        };
        this.registerReceiver(presenterActionReceiver,
                new IntentFilter(AppCMSPresenter.PRESENTER_PAGE_LOADING_ACTION));

        this.registerReceiver(presenterActionReceiver,
                new IntentFilter(AppCMSPresenter.PRESENTER_STOP_PAGE_LOADING_ACTION));

        registerReceiver(handoffReceiver,
                new IntentFilter(AppCMSPresenter.PRESENTER_CLOSE_SCREEN_ACTION));
        noResultsTextview.setText(appCMSPresenter.getLocalisedStrings().getNoSearchResultText());
        ViewCreator.setTypeFace(this, appCMSPresenter, appCMSPresenter.getJsonValueKeyMap(), new Component(), noResultsTextview);
        appCMSSearchView.setQueryHint(appCMSPresenter.getLocalisedStrings().getSearchLabelText());
        //noinspection ConstantConditions
        appCMSSearchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        appCMSSearchView.setSuggestionsAdapter(searchSuggestionsAdapter);
        appCMSSearchView.setIconifiedByDefault(false);
        TextView searchText = appCMSSearchView.findViewById(androidx.appcompat.R.id.search_src_text);
        appCMSPresenter.setCursorDrawableColor((EditText) searchText, 0);

        appCMSSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                appCMSSearchView.clearFocus();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (newText.trim().isEmpty()) {
                    progressBar.setVisibility(View.INVISIBLE);
                    appCMSSearchItemAdapter.setData(null);
                    updateNoResultsDisplay(appCMSPresenter, null);
                }
                return false;
            }
        });

        appCMSSearchView.setOnSuggestionListener(new SearchView.OnSuggestionListener() {
            @Override
            public boolean onSuggestionSelect(int position) {
                return true;
            }

            @Override
            public boolean onSuggestionClick(int position) {
                Cursor cursor = (Cursor) appCMSSearchView.getSuggestionsAdapter().getItem(position);
                String[] searchHintResult = cursor.getString(cursor.getColumnIndex("suggest_intent_data")).split(",");
                appCMSPresenter.searchSuggestionClick(searchHintResult);
                finish();
                return true;
            }
        });
        if (appCMSMain != null &&
                appCMSMain.getBrand() != null &&
                appCMSMain.getBrand().getGeneral() != null &&
                !TextUtils.isEmpty(appCMSPresenter.getAppBackgroundColor())) {
            appCMSSearchResultsContainer.setBackgroundColor(Color.parseColor(appCMSPresenter.getAppBackgroundColor()));
        }

        appCMSCloseButton.setOnClickListener(v -> finish());

        handleIntent(getIntent());
        appCMSSearchItemAdapter.handleProgress((object) -> progressBar.setVisibility(View.VISIBLE));

        mMediaBrowser = new MediaBrowserCompat(this,
                new ComponentName(this, MusicService.class), mConnectionCallback, null);

    }

    private final MediaBrowserCompat.ConnectionCallback mConnectionCallback =
            new MediaBrowserCompat.ConnectionCallback() {
                @Override
                public void onConnected() {
                    try {
                        MediaControllerCompat mediaController = new MediaControllerCompat(
                                AppCMSSearchActivity.this, mMediaBrowser.getSessionToken());
                        MediaControllerCompat.setMediaController(AppCMSSearchActivity.this, mediaController);
                    } catch (RemoteException e) {
                    }
                }
            };

    @Override
    public void onStart() {
        super.onStart();
        if (mMediaBrowser != null) {
            mMediaBrowser.connect();
        }

    }

    @Override
    public void onStop() {
        super.onStop();
        if (mMediaBrowser != null) {
            mMediaBrowser.disconnect();
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            unregisterReceiver(handoffReceiver);
            unregisterReceiver(presenterActionReceiver);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    protected void onNewIntent(Intent intent) {
        setIntent(intent);
        handleIntent(intent);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        appCMSPresenter = ((AppCMSApplication) getApplication())
                .getAppCMSPresenterComponent().appCMSPresenter();
        appCMSPresenter.setNavItemToCurrentAction(this);
        finish();
    }

    @SuppressWarnings("ConstantConditions")
    private void handleIntent(Intent intent) {
        final AppCMSPresenter appCMSPresenter =
                ((AppCMSApplication) getApplication()).getAppCMSPresenterComponent().appCMSPresenter();
        if ((appCMSSearchUrlData == null || appCMSSearchCall == null) &&
                appCMSPresenter.getAppCMSSearchUrlComponent() != null) {
            appCMSPresenter.getAppCMSSearchUrlComponent().inject(this);
            if (appCMSSearchUrlData == null || appCMSSearchCall == null) {
                return;
            }
        }

        appCMSPresenter.cancelInternalEvents();
        appCMSPresenter.pushActionInternalEvents(getString(R.string.app_cms_action_search_key));

        if (Intent.ACTION_VIEW.equals(intent.getAction()) ||
                Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String searchTerm;
            String queryTerm;
            String filterTerm;
            int searchURLRes;
            String searchType;

            if (Intent.ACTION_VIEW.equals(intent.getAction())) {
                String[] searchHintResult = intent.getDataString().split(",");
                appCMSPresenter.searchSuggestionClick(searchHintResult);

            } else {
                queryTerm = intent.getStringExtra(SearchManager.QUERY);
                filterTerm = intent.getStringExtra("FILTER_TAG");
                searchTerm = queryTerm;
                if (filterTerm != null && !TextUtils.isEmpty(filterTerm)) {
                    if (queryTerm != null && !TextUtils.isEmpty(queryTerm)) {
                        searchTerm = queryTerm + filterTerm;
                    } else {
                        searchTerm = filterTerm;
                    }
                    searchURLRes = R.string.app_cms_search_filter_api_url;
                    searchType = getString(R.string.type_video_only);
                } else {
                    searchURLRes = R.string.app_cms_search_api_url_with_types;
                    searchType = getString(R.string.search_types_for_app);
                }
                if (!TextUtils.isEmpty(searchTerm) && appCMSSearchUrlData != null) {
                    appCMSSearchView.setQuery(queryTerm, false);
                    //Send Search Term in Firebase Analytics Logs
                    appCMSPresenter.getFirebaseAnalytics().searchQueryEvent(queryTerm);
                    final String url = getString(searchURLRes,
                            appCMSSearchUrlData.getBaseUrl(),
                            appCMSSearchUrlData.getSiteName(),
                            searchTerm,
                            appCMSPresenter.getLanguageParamForAPICall(),
                            searchType);
                    //Log.d(TAG, "Search URL: " + url);
                    new SearchAsyncTask(data -> {
                        if (data != null) {
                            progressBar.setVisibility(View.INVISIBLE);
                            AppCMSPageAPI pageApi = convertToAppCMSPageAPI(data);
                            System.out.println("page api data-" + pageApi);
                            appCMSPresenter.getPageAPILruCache().put("e426505d-5b78-4634-b7ab-5857b394ef1b", pageApi);
                            appCMSPresenter.navigateToSearchPage(appCMSPresenter.isNavbarPresent(), appCMSPresenter.isAppbarPresent());
//                            appCMSSearchItemAdapter.setData(data);
//                            updateNoResultsDisplay(appCMSPresenter, data);
                        }
                    }, appCMSSearchCall, appCMSPresenter.getApiKey(), appCMSPresenter.getAuthToken()).execute(url,
                            appCMSPresenter.getApiKey(), appCMSPresenter.getAuthToken());
                }
            }
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
        module.setTitle("Title");
        appCMSPageAPI.setId("");
        List<Module> moduleList = new ArrayList<>();
        moduleList.add(module);
        appCMSPageAPI.setModules(moduleList);

        return appCMSPageAPI;
    }

    private void updateNoResultsDisplay(AppCMSPresenter appCMSPresenter,
                                        List<AppCMSSearchResult> data) {
        if (data == null || data.isEmpty()) {
            try {
                if (appCMSPresenter.getAppCMSMain().getBrand() != null) {
                    noResultsTextview.setTextColor(Color.parseColor(appCMSPresenter.getAppCMSMain()
                            .getBrand()
                            .getGeneral()
                            .getTextColor()));
                    noResultsTextview.setVisibility(View.VISIBLE);
                }
            } catch (Exception e) {
                noResultsTextview.setTextColor(ContextCompat.getColor(this, android.R.color.white));
                noResultsTextview.setVisibility(View.VISIBLE);
            }
        } else {
            noResultsTextview.setVisibility(View.GONE);
        }
    }

    private class SearchAsyncTask extends AsyncTask<String, Void, List<AppCMSSearchResult>> {
        final Action1<List<AppCMSSearchResult>> dataReadySubscriber;
        final AppCMSSearchCall appCMSSearchCall;
        final String apiKey;

        SearchAsyncTask(Action1<List<AppCMSSearchResult>> dataReadySubscriber,
                        AppCMSSearchCall appCMSSearchCall,
                        String apiKey, String authToken) {
            this.dataReadySubscriber = dataReadySubscriber;
            this.appCMSSearchCall = appCMSSearchCall;
            this.apiKey = apiKey;
            progressBar.setVisibility(View.VISIBLE);
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
            progressBar.setVisibility(View.INVISIBLE);
            Observable.just(result).subscribe(dataReadySubscriber);
        }
    }
}
