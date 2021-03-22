package com.viewlift.models.data.appcms.ui.main

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class AppVersions(@SerializedName("fireTv") @Expose
                       val fireTVAppVersion: AppVersion? = null,

                       @SerializedName("android") @Expose
                       val androidAppVersion: AppVersion? = null,

                       @SerializedName("androidTv") @Expose
                       val androidTVAppVersion: AppVersion? = null) : Serializable

data class AppVersion(@SerializedName("minimum") @Expose
                      var minimum: String,

                      @SerializedName("latest")
                      @Expose
                      var latest: String,

                      @SerializedName("updateUrl")
                      @Expose
                      var updateUrl: String) : Serializable