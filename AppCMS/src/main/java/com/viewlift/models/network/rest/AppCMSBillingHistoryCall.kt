package com.viewlift.models.network.rest

import androidx.annotation.WorkerThread
import com.viewlift.models.billing.appcms.BillingHistory
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import rx.Observable
import rx.functions.Action1
import java.io.IOException
import java.util.*
import kotlin.jvm.Throws

class AppCMSBillingHistoryCall  constructor(private val appCMSBillingHistoryRest: AppCMSBillingHistoryRest) {
    @WorkerThread
    @Throws(IOException::class)
    fun call(url: String?, token: String?, xApiKey: String?,
             billingHistoryModelAction1: Action1<BillingHistory?>) {
        try {
            val authHeaders: MutableMap<String?, String?> = HashMap()
            authHeaders["Authorization"] = token
            authHeaders["x-api-key"] = xApiKey
            appCMSBillingHistoryRest[url, authHeaders]!!.enqueue(object : Callback<BillingHistory?> {
                override fun onResponse(call: Call<BillingHistory?>,
                                        response: Response<BillingHistory?>) {
                    if (response.body() != null) Observable.just(response.body()).subscribe(billingHistoryModelAction1)
                }

                override fun onFailure(call: Call<BillingHistory?>,
                                       t: Throwable) {
                    //Log.e(TAG, "onFailure: " + t.getMessage());
                    billingHistoryModelAction1.call(null)
                }
            })
        } catch (e: Exception) {
            //Log.e(TAG, "Failed to execute watchlist " + url + ": " + e.toString());
        }
    }

    companion object {
        private val TAG = AppCMSEventArchieveCall::class.java.simpleName + "TAG"
    }

}