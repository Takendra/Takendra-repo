package com.viewlift.models.data.appcms.api

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.viewlift.models.data.appcms.audio.AudioAssets
import java.io.Serializable

data class StreamingInfo(@SerializedName("isLiveStream") @Expose
                         val isLiveStream: Boolean = false,
                         @SerializedName("videoAssets")
                         @Expose
                         var videoAssets: VideoAssets? = null,
                         @SerializedName("audioAssets")
                         @Expose
                         var audioAssets: AudioAssets? = null,
                         @SerializedName("photogalleryAssets")
                         @Expose
                         val photogalleryAssets: List<PhotoGalleryData> ?=null) : Serializable