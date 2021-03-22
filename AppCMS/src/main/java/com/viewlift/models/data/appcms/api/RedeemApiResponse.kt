package com.viewlift.models.data.appcms.api

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.viewlift.models.data.appcms.ui.authentication.ErrorResponse

data class RedeemApiResponse(@SerializedName("id")
                             @Expose
                             val id: String? = null,
                             @SerializedName("contentType")
                             @Expose
                             val contentType: String? = null,
                             @SerializedName("permalink")
                             @Expose
                             val permalink: String? = null,
                             @SerializedName(value = "status", alternate = ["couponStatus"])
                             @Expose
                             val status: String? = null,
                             var errorResponse: ErrorResponse? = null,
                             var isErrorResponseSet: Boolean = false
)