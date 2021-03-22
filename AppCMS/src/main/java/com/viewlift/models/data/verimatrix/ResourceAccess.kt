package com.viewlift.models.data.verimatrix

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class ResourceAccess(
        @SerializedName("authorization") @Expose
        val authenticated: Boolean,
        @SerializedName("resource") @Expose
        val resource: String,
        @SerializedName("security_token") @Expose
        val security_token: String,
        @SerializedName("expires") @Expose
        val expires: Long
)