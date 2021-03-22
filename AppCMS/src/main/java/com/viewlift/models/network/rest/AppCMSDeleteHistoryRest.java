package com.viewlift.models.network.rest;

import com.viewlift.models.data.appcms.api.DeleteHistoryRequest;
import com.viewlift.models.data.appcms.history.AppCMSDeleteHistoryResult;

import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.HTTP;
import retrofit2.http.HeaderMap;
import retrofit2.http.POST;
import retrofit2.http.Url;

public interface AppCMSDeleteHistoryRest {
    @POST
    Call<List<AppCMSDeleteHistoryResult>> post(@Url String url, @HeaderMap Map<String, String> headers,
                                               @Body DeleteHistoryRequest request);

    @DELETE
    Call<AppCMSDeleteHistoryResult> delete(@Url String url, @HeaderMap Map<String, String> headers);

    @HTTP(method = "DELETE", hasBody = true)
    Call<List<AppCMSDeleteHistoryResult>> removeSingle(@Url String url, @HeaderMap Map<String,
            String> headers, @Body DeleteHistoryRequest request);
}
