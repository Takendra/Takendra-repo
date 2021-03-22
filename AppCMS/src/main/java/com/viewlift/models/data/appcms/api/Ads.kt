package com.viewlift.models.data.appcms.api

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.viewlift.models.data.appcms.ui.authentication.FeatureSetting
import java.io.Serializable

data class Ads(@SerializedName("features") @Expose
               val featureSetting: FeatureSetting,

               @SerializedName("preRoll")
               @Expose
               val preRoll: PreRoll?=null
)

data class PreRoll(@SerializedName("adTags") @Expose
                   val adTags: AdTags?=null): Serializable