package com.viewlift.models.data.appcms.ui.main

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class CustomerService(@SerializedName("email") @Expose
                           val email: String? = null,
                           @SerializedName("phoneNumber")
                           @Expose
                           val phoneNumber: String? = null,
                           @SerializedName("phone")
                           @Expose
                           val phone: String? = null,
                           @SerializedName("freshChat")
                           @Expose
                           val freshChat: FreshChat? = null,
                           @SerializedName("zendesk")
                           @Expose
                           val zendesk: Zendesk? = null) : Serializable

data class FreshChat(@SerializedName("enableFreshChat") @Expose
                     val isEnableFreshChat: Boolean,
                     @SerializedName("appID")
                     @Expose
                     val appID: String? = null,
                     @SerializedName("key")
                     @Expose
                     val key: String? = null) : Serializable

data class Zendesk(@SerializedName("clientID") @Expose
                   val clientID: String,
                   @SerializedName("appID")
                   @Expose
                   val appID: String,
                   @SerializedName("url")
                   @Expose
                   val url: String? = null) : Serializable