package com.viewlift.models.data.appcms.sslcommerz

import com.google.gson.annotations.SerializedName
import com.viewlift.models.billing.appcms.purchase.InAppPurchase

data class SSLInitiateBody(@SerializedName("planId")
                           var planId: String? = null,
                           @SerializedName("tran_id")
                           var tran_id: String? = null,
                           @SerializedName("phone")
                           var phone: String? = null,
                           @SerializedName("transactionRequest")
                           var transactionRequest: InAppPurchase? = null)


