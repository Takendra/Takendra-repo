package com.viewlift.models.data.appcms.api

import com.google.gson.annotations.SerializedName

data class OfferDiscount (
		@SerializedName("amount")                val amount                : Double?,
		@SerializedName("nextBillingDate")       val nextBillingDate       : String?,
		@SerializedName("offerDescription")      val offerDescription      : String?,
		@SerializedName("reduceChargeOfferName") val reduceChargeOfferName : String?,
		@SerializedName("appliedOfferCode")      val appliedOfferCode      : String?,
		@SerializedName("reduceChargeOfferId")   val reduceChargeOfferId   : String?,
		@SerializedName("offerTitle")            val offerTitle            : String?
)