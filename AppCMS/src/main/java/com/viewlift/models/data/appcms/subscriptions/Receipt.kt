package com.viewlift.models.data.appcms.subscriptions

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Receipt(@SerializedName("orderId") @Expose
                   val orderId: String,

                   @SerializedName("packageName")
                   @Expose
                   val packageName: String,

                   @SerializedName("productId")
                   @Expose
                   val productId: String,

                   @SerializedName("purchaseTime")
                   @Expose
                   val purchaseTime: Long,

                   @SerializedName("purchaseState")
                   @Expose
                   val purchaseState: Int,

                   @SerializedName("purchaseToken")
                   @Expose
                   val purchaseToken: String,

                   @SerializedName("autoRenewing")
                   @Expose
                   val isAutoRenewing: Boolean)