package com.viewlift.models.data.appcms.ui.authentication

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class ForgotPasswordResponse(@SerializedName("emailSent") @Expose
                                  val isEmailSent: Boolean,

                                  @SerializedName("error")
                                  @Expose
                                  val error: String,

                                  @SerializedName("code")
                                  @Expose
                                  val code: String?=null)