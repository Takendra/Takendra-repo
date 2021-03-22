package com.viewlift.models.data.appcms.ui.authentication

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Phone(@SerializedName("number") @Expose
                 val number: String,

                 @SerializedName("country")
                 @Expose
                 val country: String)