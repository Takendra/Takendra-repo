package com.viewlift.models.network.rest;

import com.google.gson.JsonElement;
import com.viewlift.models.data.urbanairship.UAAssociateNamedUserRequest;
import com.viewlift.models.data.urbanairship.UAAssociateNamedUserResponse;
import com.viewlift.models.data.urbanairship.UANamedUserRequest;
import com.viewlift.models.data.urbanairship.UANamedUserResponse;
import com.viewlift.models.data.verimatrix.VerimatrixResponse;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.HeaderMap;
import retrofit2.http.POST;
import retrofit2.http.Url;

/**
 * Created by viewlift on 12/21/17.
 */

public interface VerimatrixRest {
    @GET
    Call<VerimatrixResponse> chooser(@Url String url);

    @GET
    Call<VerimatrixResponse> polling(@Url String url);

    @POST
    Call<VerimatrixResponse> shortCode(@Url String url, @Body Object body);

}
