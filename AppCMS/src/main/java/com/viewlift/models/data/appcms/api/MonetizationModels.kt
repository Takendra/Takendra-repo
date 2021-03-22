package com.viewlift.models.data.appcms.api

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable


data class MonetizationModels(@SerializedName("id") @Expose
                              var id: String,

                              @SerializedName("type")
                              @Expose
                              var type: String?=null):Serializable
