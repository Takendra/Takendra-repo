package com.viewlift.models.data.appcms.ui.android

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class Blocks(@SerializedName("name") @Expose
                  val name: String? = null,

                  @SerializedName("version")
                  @Expose
                  val version: String? = null) : Serializable