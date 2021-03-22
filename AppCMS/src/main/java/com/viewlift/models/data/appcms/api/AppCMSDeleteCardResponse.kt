package com.viewlift.models.data.appcms.api

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class AppCMSDeleteCardResponse(
        @SerializedName("statusCode") @Expose
        val statusCode: Int,
        @SerializedName("Data")
        @Expose
        val deleteCardData: DeleteCardData
)

data class DeleteCardData(
        @SerializedName("deleted") @Expose
        val isDeleted: Boolean
)