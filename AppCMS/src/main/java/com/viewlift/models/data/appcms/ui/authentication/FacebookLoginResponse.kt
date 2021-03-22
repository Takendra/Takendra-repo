package com.viewlift.models.data.appcms.ui.authentication

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class FacebookLoginResponse(
        @SerializedName("authorizationToken") @Expose
        val authorizationToken: String,

        @SerializedName("refreshToken")
        @Expose
        val refreshToken: String,

        @SerializedName("userId")
        @Expose
        val userId: String,

        @SerializedName("email")
        @Expose
        val email: String?=null,

        @SerializedName("isSubscribed")
        @Expose
        val isSubscribed: Boolean,

        @SerializedName("code")
        @Expose
        val errorCode: String,

        @SerializedName("error")
        @Expose
        val error: String,

        @SerializedName("existingUser")
        @Expose
        val isExistingUser: Boolean,

        @SerializedName("phoneNumber")
        @Expose
        val phoneNumber: String
)