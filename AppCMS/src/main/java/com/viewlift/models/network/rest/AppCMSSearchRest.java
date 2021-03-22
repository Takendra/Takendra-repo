package com.viewlift.models.network.rest;

import com.viewlift.models.data.appcms.api.Tag;
import com.viewlift.models.data.appcms.search.AppCMSSearchRelatedEpisode;
import com.viewlift.models.data.appcms.search.CategorySearchFilter;
import com.viewlift.models.data.appcms.api.Person;
import com.viewlift.models.data.appcms.search.AppCMSSearchResult;

import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.HeaderMap;
import retrofit2.http.Url;

/**
 * Created by viewlift on 6/12/17.
 */

public interface AppCMSSearchRest {
    @GET
    Call<List<AppCMSSearchResult>> get(@HeaderMap Map<String, String> authHeaders, @Url String url);

    @GET
    Call<AppCMSSearchRelatedEpisode> getEpisodeClass(@HeaderMap Map<String, String> authHeaders, @Url String url);

    @GET
    Call<List<Person>> getPersons(@HeaderMap Map<String, String> authHeaders, @Url String url);

    @GET
    Call<CategorySearchFilter> getCategory(@HeaderMap Map<String, String> authHeaders, @Url String url);

    @GET
    Call<List<Tag>> getEquipment(@HeaderMap Map<String, String> authHeaders, @Url String url);
}
