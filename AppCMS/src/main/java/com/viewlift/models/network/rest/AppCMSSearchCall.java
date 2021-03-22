package com.viewlift.models.network.rest;

import android.content.Context;

import androidx.annotation.WorkerThread;

import android.text.TextUtils;
import android.util.Log;

import com.viewlift.R;
import com.viewlift.models.data.appcms.api.AppCMSPageAPI;
import com.viewlift.models.data.appcms.api.ContentDatum;
import com.viewlift.models.data.appcms.api.FilterGroupsModel;
import com.viewlift.models.data.appcms.api.Module;
import com.viewlift.models.data.appcms.api.Person;
import com.viewlift.models.data.appcms.api.Tag;
import com.viewlift.models.data.appcms.search.AppCMSSearchRelatedEpisode;
import com.viewlift.models.data.appcms.search.CategorySearchFilter;
import com.viewlift.models.data.appcms.search.AppCMSSearchResult;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import rx.Observable;
import rx.functions.Action1;

/**
 * Created by viewlift on 6/12/17.
 */

public class AppCMSSearchCall {
    private static final String TAG = "AppCMSSearchCall";

    private final AppCMSSearchRest appCMSSearchRest;

    private Map<String, String> authHeaders;

    @Inject
    public AppCMSSearchCall(AppCMSSearchRest appCMSSearchRest) {
        this.appCMSSearchRest = appCMSSearchRest;
        this.authHeaders = new HashMap<>();
    }

    @WorkerThread
    public List<AppCMSSearchResult> call(String url, String apiKey, String authToken) throws IOException {
        try {
            authHeaders.clear();
            if (!TextUtils.isEmpty(authToken)) {
                authHeaders.put("Authorization", authToken);
            }
            authHeaders.put("x-api-key", apiKey);
            Log.i(TAG, url);
            return appCMSSearchRest.get(authHeaders, url).execute().body();
        } catch (Exception e) {
            Log.e(TAG, "Failed to execute search query " + url + ": " + e.toString());
        }
        return null;
    }

    @WorkerThread
    public void callFilterForPerson(String url, String apiKey, String authToken,
                                    final Action1<List<Person>> personAction) {


        try {
            authHeaders.clear();
            authHeaders.put("Authorization", authToken);
            authHeaders.put("x-api-key", apiKey);
            appCMSSearchRest.getPersons(authHeaders, url).enqueue(new Callback<List<Person>>() {
                @Override
                public void onResponse(Call<List<Person>> call, Response<List<Person>> response) {
                    Observable.just(response.body()).subscribe(personAction);
                }

                @Override
                public void onFailure(Call<List<Person>> call, Throwable t) {
                    personAction.call(null);
                }
            });
        } catch (Exception e) {
            //Log.e(TAG, "Failed to execute watchlist " + url + ": " + e.toString());
        }
    }

    @WorkerThread
    public void callFilterForEpisodeClass(String url, String apiKey, String authToken,
                                          final Action1<AppCMSSearchRelatedEpisode> personAction) {


        try {
            authHeaders.clear();
            authHeaders.put("Authorization", authToken);
            authHeaders.put("x-api-key", apiKey);
            appCMSSearchRest.getEpisodeClass(authHeaders, url).enqueue(new Callback<AppCMSSearchRelatedEpisode>() {
                @Override
                public void onResponse(Call<AppCMSSearchRelatedEpisode> call, Response<AppCMSSearchRelatedEpisode> response) {
                    Observable.just(response.body()).subscribe(personAction);
                }

                @Override
                public void onFailure(Call<AppCMSSearchRelatedEpisode> call, Throwable t) {
                    personAction.call(null);
                }
            });


        } catch (Exception e) {
            //Log.e(TAG, "Failed to execute watchlist " + url + ": " + e.toString());
        }
    }

    @WorkerThread
    public void callFilterForCategory(String url, String apiKey, String authToken,
                                      final Action1<CategorySearchFilter> categoryAction) {


        try {
            authHeaders.clear();
            authHeaders.put("Authorization", authToken);
            authHeaders.put("x-api-key", apiKey);
            appCMSSearchRest.getCategory(authHeaders, url).enqueue(new Callback<CategorySearchFilter>() {
                @Override
                public void onResponse(Call<CategorySearchFilter> call, Response<CategorySearchFilter> response) {
                    Observable.just(response.body()).subscribe(categoryAction);
                }

                @Override
                public void onFailure(Call<CategorySearchFilter> call, Throwable t) {
                    categoryAction.call(null);
                }
            });

        } catch (Exception e) {
            //Log.e(TAG, "Failed to execute watchlist " + url + ": " + e.toString());
        }
    }

    @WorkerThread
    public void callFilterForEquipment(String url, String apiKey, String authToken,
                                       final Action1<List<Tag>> equipmentAction) {
        try {
            authHeaders.clear();
            authHeaders.put("Authorization", authToken);
            authHeaders.put("x-api-key", apiKey);
            appCMSSearchRest.getEquipment(authHeaders, url).enqueue(new Callback<List<Tag>>() {
                @Override
                public void onResponse(Call<List<Tag>> call, Response<List<Tag>> response) {
                    Observable.just(response.body()).subscribe(equipmentAction);
                }

                @Override
                public void onFailure(Call<List<Tag>> call, Throwable t) {
                    equipmentAction.call(null);
                }
            });

        } catch (Exception e) {
            //Log.e(TAG, "Failed to execute watchlist " + url + ": " + e.toString());
        }
    }

    @WorkerThread
    public List<Person> callPersonFilter(String apiKey, String url) throws IOException {
        try {
            authHeaders.clear();
            authHeaders.put("x-api-key", apiKey);
            Log.e(TAG, "search PersonFilter url -" + url);
            //Log.e(TAG, "search PersonFilter url api key -" + apiKey  );

            return appCMSSearchRest.getPersons(authHeaders, url).execute().body();
        } catch (Exception e) {
            Log.e(TAG, "Failed to execute search query " + url + ": " + e.toString());
        }
        return null;
    }

    @WorkerThread
    public CategorySearchFilter callCategoryFilter(String authToken, String apiKey, String url) throws IOException {
        try {
            authHeaders.clear();
            authHeaders.put("Authorization", authToken);
            authHeaders.put("x-api-key", apiKey);
            Log.e(TAG, "search PersonFilter url -" + url);
            //Log.e(TAG, "search PersonFilter url api key -" + apiKey  );

            return appCMSSearchRest.getCategory(authHeaders, url).execute().body();
        } catch (Exception e) {
            Log.e(TAG, "Failed to execute search query " + url + ": " + e.toString());
        }
        return null;
    }

    @WorkerThread
    public List<Tag> callEquipmentFilter(String authToken, String apiKey, String url) throws IOException {
        try {
            authHeaders.clear();
            authHeaders.put("Authorization", authToken);
            authHeaders.put("x-api-key", apiKey);
            Log.e(TAG, "search EquipmentFilter url -" + url);
            //Log.e(TAG, "search EquipmentFilter url api key -" + apiKey  );

            return appCMSSearchRest.getEquipment(authHeaders, url).execute().body();
        } catch (Exception e) {
            Log.e(TAG, "Failed to execute search query " + url + ": " + e.toString());
        }
        return null;
    }

    public AppCMSPageAPI convertToPageAPI(String pageId, List<Person> personsList,
                                          CategorySearchFilter categorySearchFilter, List<Tag> tags, Context context) {
        List<FilterGroupsModel> filterGroupList = new ArrayList<>();
        filterGroupList.add(new FilterGroupsModel(context.getString(R.string.search_filter_instructor).toUpperCase(), personsList));
        filterGroupList.add(new FilterGroupsModel(context.getString(R.string.search_filter_class_type).toUpperCase(), categorySearchFilter.getRecords()));
        filterGroupList = manageTags(tags, context, filterGroupList);
        AppCMSPageAPI appCMSPageAPI = new AppCMSPageAPI();
        appCMSPageAPI.setId(pageId);
        Module module = new Module();
        List<ContentDatum> data = new ArrayList<>();
        ContentDatum contentDatum = new ContentDatum();
        contentDatum.setFilterGroupList(filterGroupList);
        contentDatum.setId(pageId);
        data.add(contentDatum);
        module.setContentData(data);
        List<Module> moduleList = new ArrayList<>();
        moduleList.add(module);
        appCMSPageAPI.setModules(moduleList);
        return appCMSPageAPI;
    }


    private List<FilterGroupsModel> manageTags(List<Tag> tagList, Context context, List<FilterGroupsModel> filterGroupList) {
        List<Tag> timeList = new ArrayList<>();
        List<Tag> musicList = new ArrayList<>();
        List<Tag> dificultyList = new ArrayList<>();
        List<Tag> explicitOptionList = new ArrayList<>();
        List<Tag> equipmentList = new ArrayList<>();
        List<Tag> brandList = new ArrayList<>();
        try {
            for (Tag item :
                    tagList) {
                if (item.getTagType() != null
                        && !TextUtils.isEmpty(item.getTagType())) {

                    switch (item.getTagType()) {
                        case "brand":
                            brandList.add(item);
                            break;
                        case "difficulty":
                            dificultyList.add(item);
                            break;
                        case "equipment":
                            equipmentList.add(item);
                            break;
                        case "language":
                            if (explicitOptionList.size() < 1) {
                                Tag tagExplicit = new Tag();
                                tagExplicit.setTagType("language");
                                tagExplicit.setTitle("Filter Out");
                                explicitOptionList.add(tagExplicit);
                            }
                            break;
                        case "musicType":
                            musicList.add(item);
                            break;
                        case "classDuration":
                            timeList.add(item);
                            break;
                    }
                }
            }
            filterGroupList.add(new FilterGroupsModel(context.getString(R.string.search_filter_brand).toUpperCase(), brandList));
            filterGroupList.add(new FilterGroupsModel(context.getString(R.string.search_filter_difficulty).toUpperCase(), dificultyList));
            filterGroupList.add(new FilterGroupsModel(context.getString(R.string.search_filter_equipment).toUpperCase(), equipmentList));
            filterGroupList.add(new FilterGroupsModel(context.getString(R.string.search_filter_explicit_content).toUpperCase(), explicitOptionList));
            filterGroupList.add(new FilterGroupsModel(context.getString(R.string.search_filter_music).toUpperCase(), musicList));
            filterGroupList.add(new FilterGroupsModel(context.getString(R.string.search_filter_time).toUpperCase(), timeList));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return filterGroupList;
    }
}
