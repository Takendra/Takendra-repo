package com.viewlift.models.data.appcms.ui.authentication

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class SignUpEmailConsentRequest(@SerializedName("email") @Expose
                                     var email: String? = null,

                                     @SerializedName("password")
                                     @Expose
                                     var password: String? = null,

                                     @SerializedName("emailConsent")
                                     @Expose
                                     var isEmailConsent: Boolean = false,

                                     @SerializedName("genreValue")
                                     @Expose
                                     var genreValue: String? = null,

                                     @SerializedName("userIdValue")
                                     @Expose
                                     var userIdValue: String? = null,

                                     @SerializedName("firetvUserId")
                                     @Expose
                                     var firetvUserId: String? = null)