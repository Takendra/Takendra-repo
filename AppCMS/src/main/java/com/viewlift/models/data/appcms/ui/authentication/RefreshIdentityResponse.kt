package com.viewlift.models.data.appcms.ui.authentication

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class RefreshIdentityResponse(
        @SerializedName("authorization_token")
        @Expose
        val authorizationToken: String? = null,

        @SerializedName("refresh_token")
        @Expose
        val refreshToken: String? = null,

        @SerializedName("id")
        @Expose
        val id: String? = null,

        var errorCode: Int = 0,
        var isSuccessful: Boolean = false)