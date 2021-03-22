package com.viewlift.models.network.rest

import android.util.Log
import com.google.gson.Gson
import com.google.gson.JsonElement
import com.viewlift.models.data.appcms.AppCMSCarrierBillingRequest
import com.viewlift.models.data.appcms.AppCMSCarrierBillingResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import rx.Observable
import rx.functions.Action1
import java.util.*
import javax.inject.Singleton

@Singleton
class AppCMSCarrierBillingCall(val retrofit: Retrofit, val gson: Gson) {
    private var authHeaders: HashMap<String, String> = HashMap()
    private val appCMSCarrierBillingRest: AppCMSCarrierBillingRest = retrofit.create(AppCMSCarrierBillingRest::class.java)


    fun call(url: String, apiKey: String, authToken: String, request: AppCMSCarrierBillingRequest, action1: Action1<AppCMSCarrierBillingResponse?>) {
        try {
            authHeaders.clear()
            authHeaders["Authorization"] = authToken
            authHeaders["x-api-key"]     = apiKey

            appCMSCarrierBillingRest.getRobiAndGPDataBundleSubscription(url, authHeaders, request).enqueue(object : Callback<JsonElement?> {
                override fun onResponse(call: Call<JsonElement?>, response: Response<JsonElement?>) {
                    var appCMSCarrierBillingResponse: AppCMSCarrierBillingResponse? = null
                    try {
                        if (response.body() != null) {
                            appCMSCarrierBillingResponse = gson.fromJson(response.body(), AppCMSCarrierBillingResponse::class.java)
                        } else if (response.errorBody() != null) {
                            appCMSCarrierBillingResponse = gson.fromJson(response.errorBody()!!.charStream(), AppCMSCarrierBillingResponse::class.java)
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                    Observable.just(appCMSCarrierBillingResponse).subscribe(action1)
                }

                override fun onFailure(call: Call<JsonElement?>, t: Throwable) {
                    Log.e(TAG, "" + t)
                    Observable.just(null as AppCMSCarrierBillingResponse?).subscribe(action1)
                }
            })
        } catch (e: Exception) {
            Log.e(TAG, "" + e)
            Observable.just(null as AppCMSCarrierBillingResponse?).subscribe(action1)
        }
    }

    companion object {
        private const val TAG = "CarrierBillingCall"
    }
}