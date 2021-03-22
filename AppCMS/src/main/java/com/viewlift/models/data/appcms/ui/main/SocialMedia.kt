package com.viewlift.models.data.appcms.ui.main

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class SocialMedia(@SerializedName("facebook") @Expose
                       val facebook: Facebook? = null,

                       @SerializedName("twitter")
                       @Expose
                       val twitter: Twitter? = null,

                       @SerializedName("googlePlus")
                       @Expose
                       val googlePlus: GooglePlus? = null) : Serializable

data class Facebook(@SerializedName("url") @Expose
                    val url: String,

                    @SerializedName("appId")
                    @Expose
                    val appId: String?=null) : Serializable

class GooglePlus(@SerializedName("signin") @Expose
                 val isSignin: Boolean,
                 @SerializedName("googlePlusUrl")
                 @Expose
                 val googlePlusUrl: String,
                 @SerializedName("apiKey")
                 @Expose
                 val apiKey: String,
                 @SerializedName("authenticate")
                 @Expose
                 val isAuthenticate: Boolean) : Serializable

data class Twitter(@SerializedName("url") @Expose
                   val url: String? = null) : Serializable