package com.viewlift.models.data.appcms.api

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class AppCMSEmailConsentValue(@SerializedName("copy") @Expose
                                   val copy: String,

                                   @SerializedName("checked")
                                   @Expose
                                   val isChecked: Boolean,

                                   @SerializedName("visible")
                                   @Expose
                                   val isVisible: Boolean)