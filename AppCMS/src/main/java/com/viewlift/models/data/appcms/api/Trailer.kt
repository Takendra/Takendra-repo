package com.viewlift.models.data.appcms.api

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class Trailer(@SerializedName("id") @Expose
                   val id: String? = null,

                   @SerializedName("permalink")
                   @Expose
                   val permalink: String?=null,

                   @SerializedName("videoAssets")
                   @Expose
                   val videoAssets: VideoAssets? = null,

                   @SerializedName("title")
                   @Expose
                   val title: String?=null) : Serializable