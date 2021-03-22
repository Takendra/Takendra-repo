package com.viewlift.models.data.appcms.user

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class UserPurchases(
        @SerializedName("contentId") @Expose
        val contentId: String,
        @SerializedName("purchaseType") @Expose
        val purchaseType: String,
        @SerializedName("rentEndDate") @Expose
        val rentEndDate: Long

)