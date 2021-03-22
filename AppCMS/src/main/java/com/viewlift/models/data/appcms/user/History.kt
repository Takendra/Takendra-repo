package com.viewlift.models.data.appcms.user

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.viewlift.models.data.appcms.likes.Records

data class History (@SerializedName("records") @Expose
                     val records: List<Records>)