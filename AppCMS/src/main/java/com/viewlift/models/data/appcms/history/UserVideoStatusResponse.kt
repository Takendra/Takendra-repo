package com.viewlift.models.data.appcms.history

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class UserVideoStatusResponse(@SerializedName("isQueued") @Expose
                                   val isQueued: Boolean ,
                                   @SerializedName("watchedPercentage")
                                   @Expose
                                   val watchedPercentage: Long ,
                                   @SerializedName("watchedTime")
                                   @Expose
                                   val watchedTime: Long )