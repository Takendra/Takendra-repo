package com.viewlift.models.data.appcms.ui.main

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class General(@SerializedName("backgroundColor") @Expose
                   val backgroundColor: String?=null,
                   @SerializedName("blockTitleColor")
                   @Expose
                   val blockTitleColor: String?=null,
                   @SerializedName("fontFamily")
                   @Expose
                   val fontFamily: String?=null,
                   @SerializedName("pageTitleColor")
                   @Expose
                   val pageTitleColor: String?=null,
                   @SerializedName("textColor")
                   @Expose
                   val textColor: String?=null) : Serializable