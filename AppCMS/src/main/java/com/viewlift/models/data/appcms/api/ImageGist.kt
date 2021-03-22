package com.viewlift.models.data.appcms.api

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class ImageGist(@SerializedName("_32x9") @Expose
                     val _32x9: String?=null,

                     @SerializedName("_16x9")
                     @Expose
                     var _16x9: String?=null,

                     @SerializedName("_4x3")
                     @Expose
                     val _4x3: String?=null,

                     @SerializedName("_3x4")
                     @Expose
                     val _3x4: String?=null,

                     @SerializedName("_1x1")
                     @Expose
                     val _1x1: String?=null,

                     @SerializedName("_9x16")
                     @Expose
                     val _9x16: String?=null) : Serializable