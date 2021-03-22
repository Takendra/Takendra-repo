package com.viewlift.models.data.appcms.api

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class Columns(@SerializedName("tablet") @Expose
                   var tablet: Int ,
                   @SerializedName("desktop")
                   @Expose
                   val desktop: Int ,
                   @SerializedName("mobile")
                   @Expose
                   var mobile: Int ,
                   @SerializedName("ott")
                   @Expose
                   val ott: Int ) : Serializable