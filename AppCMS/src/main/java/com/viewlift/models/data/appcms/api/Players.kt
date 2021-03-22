package com.viewlift.models.data.appcms.api

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class Players(@SerializedName("firstName") @Expose
                   val firstName: String,
                   @SerializedName("lastName")
                   @Expose
                   val lastName: String,
                   @SerializedName("weight")
                   @Expose
                   val weight: String,
                   @SerializedName("height")
                   @Expose
                   val height: String,
                   @SerializedName("data")
                   @Expose
                   val data: Data): Serializable