package com.viewlift.models.data.appcms.user

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class ParentalControlResponse(@SerializedName("parentalControl") @Expose
                                   var isParentalControl: Boolean = false,
                                   @SerializedName("parentalPin")
                                   @Expose
                                   var parentalPin: String? = null,
                                   @SerializedName("parentalRating")
                                   @Expose
                                   var parentalRating: String? = null,
                                   @SerializedName("saved")
                                   @Expose
                                   var isSaved: Boolean = false)