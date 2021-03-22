package com.viewlift.models.data.appcms.ui.authentication

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class AnonymousAuthTokenResponse (@SerializedName("authorizationToken") @Expose
                                  val authorizationToken: String)