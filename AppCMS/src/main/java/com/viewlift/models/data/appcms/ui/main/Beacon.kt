package com.viewlift.models.data.appcms.ui.main

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class Beacon(@SerializedName("siteName") @Expose
                  val siteName: String,

                  @SerializedName("clientId")
                  @Expose
                  val clientId: String,

                  @SerializedName("apiBaseUrl")
                  @Expose
                  val apiBaseUrl: String?=null) : Serializable