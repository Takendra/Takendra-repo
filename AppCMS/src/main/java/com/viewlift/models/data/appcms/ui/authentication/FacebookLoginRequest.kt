package com.viewlift.models.data.appcms.ui.authentication

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class FacebookLoginRequest(@SerializedName("facebookToken") @Expose
                                val accessToken: String,

                                @SerializedName("userId")
                                @Expose
                                val userId: String)