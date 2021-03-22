package com.viewlift.models.data.appcms.ui.main

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class AppCMSXAPIKey (@SerializedName("apiKey") @Expose
                          val x_ApiKey: String?=null):Serializable