package com.viewlift.models.data.appcms.ui.authentication

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.viewlift.models.data.appcms.api.MetadataMap
import java.io.Serializable

data class PhoneObjectRequest(@SerializedName("email") @Expose
                              var email: String? = null,

                              @SerializedName("phone")
                              @Expose
                              var phone: String? = null,

                              @SerializedName("requestType")
                              @Expose
                              var requestType: String? = null,

                              @SerializedName("metadataMap")
                              @Expose
                              var metadataMap: MetadataMap? = null,

                              @Expose
                              @SerializedName("emailConsent")
                              var isEmailConsent: Boolean = false,

                              @SerializedName("otpValue")
                              @Expose
                              var otpValue: String? = null,

                              @SerializedName("name")
                              @Expose
                              var name: String? = null,

                              @SerializedName("fragmentName")
                              @Expose
                              var fragmentName: String? = null,

                              @Expose
                              @SerializedName("isFromVerify")
                              var isFromVerify: Boolean = false,

                              @SerializedName("url")
                              @Expose
                              var url: String? = null,

                              @Expose
                              @SerializedName("screenName")
                              var screenName: String? = null,

                              @Expose
                              @SerializedName("whatsappConsent")
                              var isWhatsAppConsent: Boolean = false,

                              @SerializedName("checkOutScreenPhoneNo")
                              @Expose
                              var checkOutScreenPhoneNo: String? = null) : Serializable