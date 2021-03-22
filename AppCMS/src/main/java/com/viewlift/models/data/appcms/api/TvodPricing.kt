package com.viewlift.models.data.appcms.api

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable


data class TvodPricing(@SerializedName("buyAmount")
                       @Expose
                       val buyAmount: Float = 0F,
                       @SerializedName("rentAmount")
                       @Expose
                       val rentAmount: Float = 0F,
                       @SerializedName("countryCode")
                       @Expose
                       val countryCode: String,
                       @SerializedName("currencyCode")
                       @Expose
                       val currencyCode: String):Serializable
