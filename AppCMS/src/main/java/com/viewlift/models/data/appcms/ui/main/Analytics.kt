package com.viewlift.models.data.appcms.ui.main

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class Analytics(@SerializedName("cleverMIPushAppId") @Expose
                     val cleverMIPushAppId: String,

                     @SerializedName("cleverTapMIPushAppKey")
                     @Expose
                     val cleverTapMIPushAppKey: String):Serializable