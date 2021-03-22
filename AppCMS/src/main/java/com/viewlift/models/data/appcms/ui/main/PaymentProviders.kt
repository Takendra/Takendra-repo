package com.viewlift.models.data.appcms.ui.main

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class PaymentProviders(@SerializedName("stripe") @Expose
                            val stripe: Stripe? = null,

                            @SerializedName("ccavenue")
                            @Expose
                            val ccav: CCAv? = null,

                            @SerializedName("juspay")
                            @Expose
                            val jusPay: JusPay? = null,


                            @SerializedName("sslcommerz")
                            @Expose
                            val sslCommerz: SSLCommerz? = null) : Serializable

data class Stripe(@SerializedName("apiKey") @Expose
                  val apiKey: String? = null,

                  @SerializedName("country")
                  @Expose
                  val country: String? = null) : Serializable

data class CCAv(@SerializedName("apiKey") @Expose
                val apiKey: String,

                @SerializedName("country")
                @Expose
                val country: String) : Serializable

data class JusPay(@SerializedName("merchantId") @Expose
                  val merchantId: String? = null,
                  @SerializedName("country")
                  @Expose
                  val country: String? = null,
                  @SerializedName("return_url")
                  @Expose
                  val returnUrl: String? = null,
                  @SerializedName("bypassCheckoutScreen") @Expose
                  val isByPassCheckoutScreen: Boolean = false) : Serializable

data class SSLCommerz(
        @SerializedName("apiKey") @Expose
        val apiKey: String? = null,
        @SerializedName("country")
        @Expose
        val country: String? = null,
        @SerializedName("environment")
        @Expose
        val environment: String? = null,
        @SerializedName("sandboxStoreId")
        @Expose
        val sandboxStoreId: String? = null,
        @SerializedName("storeId")
        @Expose
        val storeId: String? = null,
        @SerializedName("isActive")
        @Expose
        val isActive: Boolean = false,
        @SerializedName("bypassCheckoutScreen") @Expose
        val isByPassCheckoutScreen: Boolean = false) : Serializable