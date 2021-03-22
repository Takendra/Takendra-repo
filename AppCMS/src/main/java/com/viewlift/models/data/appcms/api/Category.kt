package com.viewlift.models.data.appcms.api

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

class Category(@SerializedName("id") @Expose
               val id: String,
               @SerializedName("title")
               @Expose
               val title: String?=null,
               @SerializedName("gist")
               @Expose
               val gist: Gist?=null,
               var isSelected: Boolean = false) : Serializable