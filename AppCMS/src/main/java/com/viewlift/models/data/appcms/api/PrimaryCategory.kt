package com.viewlift.models.data.appcms.api

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class PrimaryCategory(@SerializedName("title") @Expose
                           val title: String?=null) : Serializable