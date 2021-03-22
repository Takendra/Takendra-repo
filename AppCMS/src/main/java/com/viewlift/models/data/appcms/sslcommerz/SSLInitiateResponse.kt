package com.viewlift.models.data.appcms.sslcommerz

import com.google.gson.annotations.SerializedName

data class SSLInitiateResponse(@SerializedName("success")
                               val isSuccess: Boolean = false,
                               @SerializedName("error")
                               val error: String? = null,
                               @SerializedName("status")
                               val status: String? = null)


