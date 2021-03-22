package com.viewlift.models.data.appcms.ui.android

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class Analytics(@SerializedName("googleTagManagerId") @Expose
                     val googleTagManagerId: String? = null,

                     @SerializedName("googleAnalyticsId")
                     @Expose
                     val googleAnalyticsId: String? = null,

                     @SerializedName("kochavaAppId")
                     @Expose
                     val kochavaAppId: String? = null,

                     @SerializedName("appflyerDevKey")
                     @Expose
                     val appflyerDevKey: String? = null,

                     @SerializedName("omnitureAppSDKConfigFile")
                     @Expose
                     val omnitureAppSDKConfigFile: String? = null): Serializable