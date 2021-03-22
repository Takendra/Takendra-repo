package com.viewlift.models.billing.appcms

import com.google.gson.annotations.SerializedName

data class BillingHistory(@SerializedName("Count")
                          val count: String? = null,

                          @SerializedName("ScannedCount")
                          val scannedCount: String? = null,

                          @SerializedName("Items")
                          val items: List<ItemsBean>? = null)

data class ItemsBean(
        @SerializedName("planId")
        val planId: String? = null,

        @SerializedName("identifier")
        val identifier: String? = null,

        @SerializedName("site")
        val site: String? = null,

        @SerializedName("contentType")
        val contentType: String? = null,

        @SerializedName("purchaseType")
        val purchaseType: String? = null,

        @SerializedName("title")
        val title: String? = null,

        @SerializedName("offerName")
        val offerName: String? = null,

        @SerializedName("paymentUniqueId")
        val paymentUniqueId: String? = null,

        @SerializedName("completedAt")
        val completedAt: String? = null,

        @SerializedName("userId")
        val userId: String? = null,

        @SerializedName("id")
        val id: String? = null,

        @SerializedName("transactiontype")
        val transactiontype: String? = null,

        @SerializedName("preTaxAmount")
        val preTaxAmount: String? = null,

        @SerializedName("updateDate")
        val updateDate: String? = null,

        @SerializedName("offerCode")
        val offerCode: String? = null,

        @SerializedName("totalAmount")
        val totalAmount: String? = null,

        @SerializedName("countryCode")
        val countryCode: String? = null,

        @SerializedName("offerId")
        val offerId: String? = null,

        @SerializedName("subscriptionStartDate")
        val subscriptionStartDate: String? = null,

        @SerializedName("subscriptionEndDate")
        val subscriptionEndDate: String? = null,

        @SerializedName("paymentHandler")
        val paymentHandler: String? = null,

        @SerializedName("taxAmount")
        val taxAmount: String? = null,

        @SerializedName("siteOwner")
        val siteOwner: String? = null,

        @SerializedName("initiatedAt")
        val initiatedAt: String? = null,

        @SerializedName("receipt")
        val receipt: Any? = null,

        @SerializedName("platform")
        val platform: String? = null,

        @SerializedName("gatewayChargeId")
        val gatewayChargeId: String? = null,

        @SerializedName("currencyCode")
        val currencyCode: String? = null,

        @SerializedName("addedDate")
        val addedDate: String? = null,

        @SerializedName("vlTransactionId")
        val vlTransactionId: String? = null,

        @SerializedName("subscriptionStatus")
        val subscriptionStatus: String? = null,

        @SerializedName("freeTrial")
        val isFreeTrial: Boolean,

        @SerializedName("gatewayFee")
        val gatewayFee: String? = null
)