package com.viewlift.models.network.rest

import com.viewlift.models.billing.appcms.purchase.InAppPurchase
import com.viewlift.models.billing.appcms.purchase.TvodPurchaseData
import com.viewlift.models.billing.appcms.purchase.TvodPurchaseResponse
import retrofit2.Call
import retrofit2.http.*

interface PurchaseProductRest {
    @POST
    fun postPurchase(@Url url: String, @HeaderMap authHeaders: Map<String, String>, @Body request: InAppPurchase): Call<TvodPurchaseResponse>

}