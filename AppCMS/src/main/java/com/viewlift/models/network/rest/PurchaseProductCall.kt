package com.viewlift.models.network.rest

import android.util.Log
import com.viewlift.models.billing.appcms.purchase.InAppPurchase
import com.viewlift.models.billing.appcms.purchase.TvodPurchaseResponse
import retrofit2.*
import rx.Observable
import rx.functions.Action1
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.collections.HashMap


@Singleton
class PurchaseProductCall(val retrofit: Retrofit) {
    private var authHeaders: HashMap<String, String> = HashMap()
    private val TAG = "PurchaseProductCall"
    private val purchaseProductRest: PurchaseProductRest = retrofit.create(PurchaseProductRest::class.java)


    fun call(url: String, apiKey: String, authToken: String, inAppPurchase: InAppPurchase,
             apiResponse: Action1<TvodPurchaseResponse>) {
        authHeaders.clear()
        authHeaders["Authorization"] = authToken
        authHeaders["x-api-key"] = apiKey
        Log.d(TAG, "URL: $url")

        purchaseProductRest.postPurchase(url, authHeaders, inAppPurchase).enqueue(object : Callback<TvodPurchaseResponse> {
            override fun onFailure(call: Call<TvodPurchaseResponse>, t: Throwable) {

                Observable.just(TvodPurchaseResponse())
                        .onErrorResumeNext { Observable.empty() }
                        .subscribe(apiResponse)
            }

            override fun onResponse(call: Call<TvodPurchaseResponse>, response: Response<TvodPurchaseResponse>) {
                Observable.just<TvodPurchaseResponse>(response.body())
                        .onErrorResumeNext { Observable.empty() }
                        .subscribe(apiResponse)

            }
        })
    }
}