package com.viewlift.models.data.appcms.ui.authentication

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class OTPRequest(@SerializedName("phoneNumber") @Expose
                      var phoneNumber: String? = null,

                      @SerializedName("requestType")
                      @Expose
                      var requestType: String? = null,

                      @SerializedName("otpValue")
                      @Expose
                      var otpValue: String? = null,


                      @SerializedName("emailConsent")
                      @Expose
                      var isEmailConsent: Boolean = false,


                      @SerializedName("email")
                      @Expose
                      var email: String? = null,


                      @SerializedName("name")
                      @Expose
                      var name: String? = null,

                      @SerializedName("whatsappConsent") @Expose
                      var isWhatsAppConsent: Boolean = false)