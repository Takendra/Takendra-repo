package com.viewlift.models.data.appcms.history

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class AppCMSDeleteHistoryResult(@SerializedName("videoId") @Expose
                                     val videoId: String)