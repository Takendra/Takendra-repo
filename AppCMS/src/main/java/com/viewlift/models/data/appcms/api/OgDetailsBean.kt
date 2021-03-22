package com.viewlift.models.data.appcms.api

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class OgDetailsBean(@SerializedName("title") @Expose
                         val title: String)