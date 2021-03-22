package com.viewlift.models.data.appcms.ui.authentication

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class GoogleLoginRequest(@SerializedName("googleToken") @Expose
                               val accessToken: String)