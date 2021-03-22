package com.viewlift.models.data.appcms.api

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class AppCMSRentalResponse(@SerializedName("transactionEndDate") @Expose
                            val transactionEndDate: Long)