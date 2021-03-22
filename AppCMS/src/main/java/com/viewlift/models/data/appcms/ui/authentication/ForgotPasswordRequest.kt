package com.viewlift.models.data.appcms.ui.authentication

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class ForgotPasswordRequest(@SerializedName("email") @Expose
                            val email: String)