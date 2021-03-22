package com.viewlift.models.network.rest

import com.google.gson.JsonElement
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.HeaderMap
import retrofit2.http.POST
import retrofit2.http.Url

/**
 * This interface is for Redemption API calls
 *
 * @author Vinay
 * @since 2019-01-21
 */
interface AppCMSRedeemRest {

    /**
     * POST REST API for coupon redemption
     *
     * @param url  - API URL
     * @param body   - API Body
     * @param headers  - headers
     * @author  Vinay
     */
    @POST
    fun redeem(@Url url: String, @Body body: Any, @HeaderMap headers: Map<String, String>): Call<JsonElement>

}