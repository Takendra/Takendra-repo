package com.viewlift.models.data.appcms

import com.google.gson.annotations.SerializedName

data class AppCMSCarrierBillingRequest(
        @SerializedName("subscription") val subscription : String = "carrier-billing",
        @SerializedName("mobileNumber") val mobileNumber : String,
        @SerializedName("planId")       val planId       : String,
        @SerializedName("platform")     val platform     : String
)

data class AppCMSCarrierBillingResponse(val subscribed: Boolean?, val message: String?)
