package com.viewlift.models.data.appcms.api

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class CategoryPages(@SerializedName("id") @Expose
                         val id: String,
                         @SerializedName("title")
                         @Expose
                         val title: String?=null,
                         @SerializedName("ogDetails")
                         @Expose
                         val ogDetails: OgDetailsBean?=null,
                         @SerializedName("path")
                         @Expose
                         val path: MutableList<String?>) : Serializable