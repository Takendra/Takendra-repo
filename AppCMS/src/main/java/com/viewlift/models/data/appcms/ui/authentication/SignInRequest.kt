package com.viewlift.models.data.appcms.ui.authentication

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class SignInRequest(@SerializedName("email") @Expose
                         var email: String? = null,

                         @SerializedName("requestType")
                         @Expose
                         var requestType: String? = null,

                         @SerializedName("name")
                         @Expose
                         var name: String? = null,

                         @SerializedName("password")
                         @Expose
                         var password: String? = null,

                         @SerializedName("emailConsent")
                         @Expose
                         var isEmailConsent: Boolean = false,

                         @SerializedName("userId")
                         @Expose
                         var userId: String? = null,

                         @SerializedName("provider")
                         @Expose
                         var provider: String? = null,

                         @SerializedName("mvpdProvider")
                         @Expose
                         var mvpdProvider: String? = null,

                         @SerializedName("tveUserId")
                         @Expose
                         var tveUserId: String? = null,

                         @SerializedName("idpLogo")
                         @Expose
                         var idpLogo: String? = null,

                         @SerializedName("idpName")
                         @Expose
                         var idpName: String? = null,

                         @SerializedName("resourceIdAccess")
                         @Expose
                         var resourceIds: MutableMap<String, Boolean>? = null,
                         var isIgnorePassword: Boolean = false)