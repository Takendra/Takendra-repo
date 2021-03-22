package com.viewlift.models.data.appcms.api

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class VerimatrixShortCodeApiRequest(@SerializedName("_type") @Expose
                                         val type: String,
                                         @SerializedName("fed_data")
                                         @Expose
                                         val fedData: String)