package com.viewlift.models.billing.appcms.subscriptions

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class InAppPurchaseData(@SerializedName("autoRenewing") @Expose
                             val isAutoRenewing: Boolean,
                             @SerializedName("orderId")
                             @Expose
                             val orderId: String,

                             @SerializedName("packageName")
                             @Expose
                             val packageName: String,

                             @SerializedName("productId")
                             @Expose
                             val productId: String,

                             @SerializedName("purchaseTime")
                             @Expose
                             val purchaseTime: Long = 0,

                             @SerializedName("purchaseState")
                             @Expose
                             val purchaseState: Long = 0,

                             @SerializedName("developerPayload")
                             @Expose
                             val developerPayload: String,

                             @SerializedName("purchaseToken")
                             @Expose
                             val purchaseToken: String,
                             @SerializedName("acknowledged")
                             @Expose
                             val isAacknowledged: Boolean,
                             @SerializedName("obfuscatedAccountId")
                             @Expose
                             val obfuscatedAccountId: String)