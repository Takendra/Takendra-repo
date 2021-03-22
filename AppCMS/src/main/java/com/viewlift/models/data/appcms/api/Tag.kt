package com.viewlift.models.data.appcms.api

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class Tag(@SerializedName("title") @Expose
               var title: String? = null,

               @SerializedName("images")
               @Expose
               val images: Images? = null,

               @SerializedName("tagType")
               @Expose
               var tagType: String? = null,
               var isSelected: Boolean = false) : Serializable