package com.viewlift.models.billing.appcms.authentication

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class GoogleRefreshTokenResponse(
        @SerializedName("access_token") @Expose
        var accessToken: String,
        @SerializedName("expires_in")
        @Expose
        val expiresIn: Int,
        @SerializedName("token_type")
        @Expose
        val tokenType: String
)