package com.viewlift.models.data.appcms.audio

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class AudioAssets(@SerializedName("mp3") @Expose
                       var mp3: Mp3?=null) : Serializable

data class Mp3(@SerializedName("url") @Expose
               var url: String?=null) :Serializable
