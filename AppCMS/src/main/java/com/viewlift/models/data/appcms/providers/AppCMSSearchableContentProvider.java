package com.viewlift.models.data.appcms.providers;

import android.app.SearchManager;
import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.net.Uri;
import android.provider.BaseColumns;
import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.gson.Gson;
import com.viewlift.AppCMSApplication;
import com.viewlift.BuildConfig;
import com.viewlift.R;
import com.viewlift.models.data.appcms.search.AppCMSSearchResult;
import com.viewlift.models.network.modules.AppCMSSearchUrlData;
import com.viewlift.models.network.rest.AppCMSSearchCall;
import com.viewlift.presenters.AppCMSPresenter;
import com.viewlift.utils.CommonUtils;

import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import okhttp3.OkHttpClient;

import static android.app.SearchManager.SUGGEST_URI_PATH_QUERY;

public class AppCMSSearchableContentProvider extends ContentProvider {
    public static final String URI_AUTHORITY = BuildConfig.AUTHORITY;
    private static final String TAG = "SearchableProvider";
    private static final UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
    private static final String[] SUGGESTION_COLUMN_NAMES = {BaseColumns._ID,
            SearchManager.SUGGEST_COLUMN_TEXT_1,
            SearchManager.SUGGEST_COLUMN_DURATION,
            SearchManager.SUGGEST_COLUMN_INTENT_DATA};
    private AppCMSPresenter appCMSPresenter;

    static {
        uriMatcher.addURI(URI_AUTHORITY, SUGGEST_URI_PATH_QUERY, 1);
        uriMatcher.addURI(URI_AUTHORITY, null, 2);
    }

    @Inject
    AppCMSSearchUrlData appCMSSearchUrlData;

    @Inject
    AppCMSSearchCall appCMSSearchCall;
    private Gson gson;
    private OkHttpClient client;

    @Override
    public boolean onCreate() {
        gson = new Gson();
        client = new OkHttpClient.Builder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(10, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .build();
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection,
                        @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        MatrixCursor cursor = null;

        if (getContext() instanceof AppCMSApplication && needInjection()) {
            appCMSPresenter =
                    ((AppCMSApplication) getContext()).getAppCMSPresenterComponent().appCMSPresenter();
            appCMSPresenter.getAppCMSSearchUrlComponent().inject(this);
            if (needInjection()) {
                return null;
            }
        }

        switch (uriMatcher.match(uri)) {
            case 1:
            case 2:
                //Log.d(TAG, "Performing a search of Viewlift films");
                String countryCode = appCMSPresenter.isUserLoggedIn() ? CommonUtils.getCountryCodeFromAuthToken(appCMSPresenter.getAuthToken()) : CommonUtils.getCountryCodeFromAuthToken(appCMSPresenter.getAppPreference().getAnonymousUserToken());
                if (selectionArgs != null &&
                        selectionArgs.length > 0 &&
                        !TextUtils.isEmpty(selectionArgs[0].trim())) {
                    String url = getContext().getString(R.string.app_cms_search_api_url_with_types,
                            appCMSPresenter.getAppCMSMain().getApiBaseUrlCached(),
                            appCMSPresenter.getAppCMSMain().getInternalName(),
                            selectionArgs[0],
                            appCMSPresenter.getLanguageParamForAPICall(), appCMSPresenter.getCurrentActivity().getString(R.string.search_types_for_app), countryCode);
                    //Log.d(TAG, "Search URL: " + url);
                    try {
                        List<AppCMSSearchResult> searchResultList = appCMSSearchCall.call(url, appCMSPresenter.getApiKey(), appCMSPresenter.getAuthToken());
                        if (searchResultList != null) {
                            //Log.d(TAG, "Search results received (" + searchResultList.size() + "): ");
                            cursor = new MatrixCursor(SUGGESTION_COLUMN_NAMES, searchResultList.size());

                            for (int i = 0; i < searchResultList.size(); i++) {
                                Uri permalinkUri = Uri.parse(searchResultList.get(i).getGist().getPermalink());
                                String filmUri = permalinkUri.getLastPathSegment();
                                String title = searchResultList.get(i).getGist().getTitle();
                                String runtime = String.valueOf(searchResultList.get(i).getGist().getRuntime());
                                String mediaType = searchResultList.get(i).getGist().getMediaType();
                                String contentType = searchResultList.get(i).getGist().getContentType();
                                String gistId = searchResultList.get(i).getGist().getId();
                                String tags = "other";
                                Boolean seriesData = false;
                                if ((appCMSPresenter.getAppCMSMain().getFeatures().isOpenShowDetail()
                                        || appCMSPresenter.getTemplateType() == AppCMSPresenter.TemplateType.NEWS)) {
                                    if (/*appCMSPresenter.getAppCMSMain().getFeatures().isShowDetailToggle() && */
                                            searchResultList.get(i).getSeriesData() != null && searchResultList.get(i).getSeriesData().size() > 0 &&
                                                    searchResultList.get(i).getSeriesData().get(0).getGist() != null &&
                                                    searchResultList.get(i).getSeriesData().get(0).getGist().getPermalink() != null &&
                                                    !mediaType.isEmpty()
                                                    && !mediaType.toLowerCase().contains(getContext().getString(R.string.media_type_video).toLowerCase())) {
                                        seriesData = true;
                                        permalinkUri = Uri.parse(searchResultList.get(i).getSeriesData().get(0).getGist().getPermalink());
                                    } else {
                                        permalinkUri = Uri.parse(searchResultList.get(i).getGist().getPermalink());
                                        seriesData = false;
                                    }
                                } else {
                                    if (/*appCMSPresenter.getAppCMSMain().getFeatures().isShowDetailToggle() && */
                                            searchResultList.get(i).getSeriesData() != null && searchResultList.get(i).getSeriesData().size() > 0 &&
                                                    searchResultList.get(i).getSeriesData().get(0).getGist() != null &&
                                                    searchResultList.get(i).getSeriesData().get(0).getGist().getPermalink() != null &&
                                                    !mediaType.isEmpty()
                                                    && !mediaType.toLowerCase().contains(getContext().getString(R.string.media_type_video).toLowerCase())
                                                    && !mediaType.toLowerCase().contains(getContext().getString(R.string.media_type_episode).toLowerCase())) {
                                        seriesData = true;
                                        permalinkUri = Uri.parse(searchResultList.get(i).getSeriesData().get(0).getGist().getPermalink());
                                    } else {
                                        permalinkUri = Uri.parse(searchResultList.get(i).getGist().getPermalink());
                                        seriesData = false;
                                    }
                                }
                                if (searchResultList.get(i).getTags() != null && searchResultList.get(i).getTags().size() > 0) {
                                    for (int countTag = 0; countTag < searchResultList.get(i).getTags().size(); countTag++) {
                                        if (searchResultList.get(i).getTags().get(countTag).getTitle() != null && searchResultList.get(i).getTags().get(countTag).getTitle().equalsIgnoreCase(getContext().getString(R.string.tag_promo)))
                                            tags = searchResultList.get(i).getTags().get(countTag).getTitle();
                                    }
                                }

                                String audioCount = "0";
                                if (searchResultList.get(i).getAudioList() != null && searchResultList.get(i).getAudioList().size() > 0) {
                                    audioCount = searchResultList.get(i).getAudioList().size() + "";
                                }

                                int searchEpisodeCount = 0;
                                if (searchResultList.get(i).getSeasons() != null) {
                                    for (int j = 0; j < searchResultList.get(i).getSeasons().size(); j++) {
                                        searchEpisodeCount = searchEpisodeCount + searchResultList.get(i).getSeasons().get(j).getEpisodes().size();
                                    }
                                }
                                if (searchResultList.get(i).getAudioList() != null && searchResultList.get(i).getAudioList().size() > 0) {
                                    audioCount = searchResultList.get(i).getAudioList().size() + "";
                                }
                                String yearSong = "";
                                if (searchResultList.get(i).getGist() != null && searchResultList.get(i).getGist().getYear() != null) {
                                    yearSong = searchResultList.get(i).getGist().getYear();
                                }
                                if (mediaType != null && mediaType.equalsIgnoreCase(getContext().getString(R.string.media_type_player)) && title == null) {
                                    title = searchResultList.get(i).getTitle();
                                }

                                String searchHintResult = title.replaceAll(",", " ") +
                                        "," +
                                        runtime +
                                        "," +
                                        filmUri +
                                        "," +
                                        permalinkUri +
                                        "," +
                                        mediaType +
                                        "," +
                                        contentType +
                                        "," +
                                        gistId + "," + audioCount + "," + yearSong + "," + searchEpisodeCount +
                                        "," + tags +
                                        "," + seriesData + "";

                                Object[] rowResult = {i,
                                        title,
                                        runtime,
                                        searchHintResult};

                                cursor.addRow(rowResult);
                                //Log.d(TAG, searchResultList.get(i).getGist().getTitle());
                                //Log.d(TAG, String.valueOf(searchResultList.get(i).getGist().getRuntime())
//                                        + " seconds");
                            }
                        } else {
                            //Log.d(TAG, "No search results found");
                        }
                    } catch (Exception e) {
                        Log.e(TAG, "Received exception: " + e.getMessage());
                    }
                } else {
                    //Log.d(TAG, "Could not retrieved results - search content provider has not been injected");
                }
                break;

            default:
        }
        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        return null;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection,
                      @Nullable String[] selectionArgs) {
        return 0;
    }

    private boolean needInjection() {
        return appCMSSearchCall == null || appCMSSearchUrlData == null;
    }
}
