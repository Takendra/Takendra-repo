package com.viewlift.models.data.appcms.ui.authentication

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class SignUpRequest(@SerializedName("email") @Expose
                         var email: String? = null,

                         @SerializedName("password")
                         @Expose
                         var password: String? = null,

                         @SerializedName("genreValue")
                         @Expose
                         var genreValue: String? = null,

                         @SerializedName("userIdValue")
                         @Expose
                         var userIdValue: String? = null,

                         @SerializedName("tveUserId")
                         @Expose
                         var tveUserId: String? = null,

                         @SerializedName("firetvUserId")
                         @Expose
                         var firetvUserId: String? = null)