package com.viewlift.models.billing.appcms.subscriptions

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class SkuDetails(
        @SerializedName("productId") @Expose
        val productId: String,

        @SerializedName("type")
        @Expose
        val type: String,

        @SerializedName("price")
        @Expose
        val price: String,

        @SerializedName("price_amount_micros")
        @Expose
        val priceAmountMicros: String,

        @SerializedName("price_currency_code")
        @Expose
        val priceCurrencyCode: String,

        @SerializedName("title")
        @Expose
        val title: String,

        @SerializedName("description")
        @Expose
        val description: String,

        @SerializedName("subscriptionPeriod")
        @Expose
        val subscriptionPeriod: String,

        @SerializedName("freeTrialPeriod")
        @Expose
        val freeTrialPeriod: String,

        @SerializedName("introductoryPrice")
        @Expose
        val introductoryPrice: String,

        @SerializedName("introductoryPriceAmountMicros")
        @Expose
        val introductoryPriceAmountMicros: String,

        @SerializedName("introductoryPricePeriod")
        @Expose
        val introductoryPricePeriod: String,

        @SerializedName("introductoryPriceCycles")
        @Expose
        val introductoryPriceCycles: String
)