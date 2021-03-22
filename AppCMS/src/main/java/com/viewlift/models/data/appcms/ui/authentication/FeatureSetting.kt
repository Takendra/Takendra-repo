package com.viewlift.models.data.appcms.ui.authentication

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class FeatureSetting(@SerializedName("isEmailRequired") @Expose
                          val isEmailRequired: Boolean,
                          @SerializedName("isLoginRequired")
                          @Expose
                          val isLoginRequired: Boolean,

                          @SerializedName("subscribed")
                          @Expose
                          val isSubscribed: Boolean,

                          @SerializedName("loggedIn")
                          @Expose
                          val isLoggedIn: Boolean,

                          @SerializedName("nonLoggedIn")
                          @Expose
                          val isGuestUser: Boolean,

                          @SerializedName("transactionPurchased")
                          @Expose
                          val isTransactionPurchased: Boolean,

                          @SerializedName("churned")
                          @Expose
                          val isChurned: Boolean,

                          @SerializedName("isHdStreaming")
                          @Expose
                          val isHdStreaming: Boolean,

                          @SerializedName("isBeamingAllowed")
                          @Expose
                          val isBeamingAllowed: Boolean,

                          @SerializedName("includingAds")
                          @Expose val isIncludingAds: Boolean,

                          @SerializedName("isDownloadAllowed")
                          @Expose
                          val isDownloadAllowed: Boolean,

                          @SerializedName("contentConsumption")
                          @Expose
                          val allowedDevices: MutableList<String>? = null) : Serializable