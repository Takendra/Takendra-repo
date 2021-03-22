package com.viewlift.models.data.appcms.ui.main

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class PlayerProgressColor(@SerializedName("progressBarColor") @Expose
                               val progressBarColor: String? = null,

                               @SerializedName("progressBarBackgroundColor")
                               @Expose
                               val progressBarBackgroundColor: String? = null) : Serializable