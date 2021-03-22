package com.viewlift.models.data.appcms.subscriptions

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class PlanDetail(@SerializedName(value = "recurringPaymentAmount", alternate = ["amount"]) @Expose
                      val recurringPaymentAmount: Double,
                      @SerializedName(value = "recurringPaymentCurrencyCode", alternate = ["currencyCode"])
                      @Expose
                      val recurringPaymentCurrencyCode: String,

                      @SerializedName("countryCode")
                      @Expose
                      val countryCode: String,

                      @SerializedName("strikeThroughPrice")
                      @Expose
                      val strikeThroughPrice: Double,

                      @SerializedName("title")
                      @Expose
                      val title: String,

                      @SerializedName("description")
                      @Expose
                      val description: String) : Serializable