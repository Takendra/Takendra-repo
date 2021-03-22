package com.viewlift.models.data.appcms.subscriptions

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.viewlift.models.data.appcms.api.PaymentHandlerResponse
import java.io.Serializable

data class AppCMSUserSubscriptionPlanInfoResult(@SerializedName("id") @Expose
                                                val id: String,

                                                @SerializedName("planId")
                                                @Expose
                                                val planId: String,

                                                @SerializedName("subscriptionStatus")
                                                @Expose
                                                val subscriptionStatus: String? = null,

                                                @SerializedName("subscriptionStartDate")
                                                @Expose
                                                val subscriptionStartDate: String,

                                                @SerializedName("subscriptionEndDate")
                                                @Expose
                                                val subscriptionEndDate: String,

                                                @SerializedName("paymentHandler")
                                                @Expose
                                                val paymentHandler: String,

                                                @SerializedName("platform")
                                                @Expose
                                                val platform: String,

                                                @SerializedName("vlTransactionId")
                                                @Expose
                                                val vlTransactionId: String? = null,

                                                @SerializedName("receipt")
                                                @Expose
                                                val receipt: String,

                                                @SerializedName("totalAmount")
                                                @Expose
                                                val totalAmount: Double,

                                                @SerializedName("gatewayChargeId")
                                                @Expose
                                                val gatewayChargeId: String,

                                                @SerializedName("identifier")
                                                @Expose
                                                val identifier: String,

                                                @SerializedName("countryCode")
                                                @Expose
                                                val countryCode: String,

                                                @SerializedName("currencyCode")
                                                @Expose
                                                val currencyCode: String,

                                                @SerializedName("freeTrial")
                                                @Expose
                                                val isFreeTrial: Boolean,

                                                @SerializedName("brand")
                                                @Expose
                                                val brand: String,

                                                @SerializedName("paymentHandlerDisplayName")
                                                @Expose
                                                val paymentHandlerDisplayName: String? = null,
                                                @SerializedName("paymentHandlerResponse")
                                                @Expose
                                                val paymentHandlerResponse: PaymentHandlerResponse? = null,

                                                @SerializedName("paymentOperator")
                                                @Expose
                                                val paymentOperator: String? = null) : Serializable