package com.viewlift.models.data.appcms.history

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class UpdateHistoryResponse(
        @SerializedName("errorCode") @Expose
        val errorCode: String?=null,
        @SerializedName("errorMessage")
        @Expose
        val errorMessage: String?=null,
        @SerializedName("responseCode")
        @Expose
        var responseCode: Int=0)