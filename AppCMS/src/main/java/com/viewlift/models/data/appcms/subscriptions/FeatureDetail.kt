package com.viewlift.models.data.appcms.subscriptions

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class FeatureDetail(@SerializedName("textToDisplay") @Expose
                         val textToDisplay: String,

                         @SerializedName("value")
                         @Expose
                         val value: String,

                         @SerializedName("valueType")
                         @Expose
                         val valueType: String)