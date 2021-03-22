package com.viewlift.models.data.appcms.history

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class SeriesHistory(@SerializedName("videoId") @Expose
                         val videoId: String?=null,
                         @SerializedName("latestEpisode")
                         @Expose
                         val isLatestEpisode: Boolean)