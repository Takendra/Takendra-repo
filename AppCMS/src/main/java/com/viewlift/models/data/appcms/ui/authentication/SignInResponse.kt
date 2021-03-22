package com.viewlift.models.data.appcms.ui.authentication

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class SignInResponse(@SerializedName("authorizationToken") @Expose
                          val authorizationToken: String? = null,

                          @SerializedName("refreshToken")
                          @Expose
                          val refreshToken: String? = null,

                          @SerializedName("email")
                          @Expose
                          val email: String? = null,

                          @SerializedName("name")
                          @Expose
                          val name: String? = null,

                          @SerializedName("userId")
                          @Expose
                          val userId: String? = null,

                          @SerializedName("picture")
                          @Expose
                          val picture: String? = null,

                          @SerializedName("isSubscribed")
                          @Expose
                          val isSubscribed: Boolean = false,

                          @SerializedName("message")
                          @Expose
                          val message: String? = null,

                          @SerializedName("provider")
                          @Expose
                          val provider: String? = null,

                          @SerializedName("providers")
                          @Expose
                          val providers: List<String> = emptyList(),

                          @SerializedName("phoneNumber")
                          @Expose
                          val phoneNumber: String? = null,

                          @SerializedName("sent")
                          @Expose
                          val isSent: Boolean = false,
                          var errorResponse: ErrorResponse? = null,
                          var isErrorResponseSet: Boolean = false): Serializable