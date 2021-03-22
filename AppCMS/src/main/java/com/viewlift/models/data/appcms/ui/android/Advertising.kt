package com.viewlift.models.data.appcms.ui.android

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class Advertising(@SerializedName("videoTag") @Expose
                       var videoTag: String? = null,

                       @SerializedName("adProvider")
                       @Expose
                       val adProvider: String? = null): Serializable