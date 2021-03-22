package com.viewlift.models.data.appcms.api

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class AppCMSStreamingInfo(@SerializedName("streamingInfo") @Expose
                               val streamingInfo: StreamingInfo)