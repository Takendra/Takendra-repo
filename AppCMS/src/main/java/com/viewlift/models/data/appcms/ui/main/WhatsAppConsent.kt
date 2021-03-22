package com.viewlift.models.data.appcms.ui.main

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class WhatsAppConsent(@SerializedName("enableWhatsappConsent")
                           @Expose
                           var isEnableWhatsappConsent: Boolean = false,

                           @SerializedName("whatsappConsentMessage")
                           @Expose
                           var whatsappConsentMessage: String? = null,

                           @SerializedName("isWhatsappChecked")
                           @Expose
                           var isWhatsappChecked: Boolean = false) : Serializable {


}