package com.viewlift.models.network.rest

import com.viewlift.models.billing.appcms.BillingHistory
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.HeaderMap
import retrofit2.http.Url

interface AppCMSBillingHistoryRest {
    @GET
    operator fun get(@Url url: String?, @HeaderMap headers: Map<String?, String?>?): Call<BillingHistory?>?
}