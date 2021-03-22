package com.viewlift.models.data.appcms.ui.main

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable
import java.util.*

data class EmailConsent(@SerializedName("enableEmailConsent") @Expose
                        val isEnableEmailConsent: Boolean,

                        @SerializedName("defaultMessage")
                        @Expose
                        val defaultMessage: String? = null,

                        @SerializedName("defaultChecked")
                        @Expose
                        val isDefaultChecked: Boolean,

                        @SerializedName("messages")
                        @Expose
                        val localizationMap: HashMap<String, EmailConsentMessage>) : Serializable

data class EmailConsentMessage(@SerializedName("message") @Expose
                               val message: String? = null,
                               @SerializedName("isChecked")
                               @Expose
                               val isChecked: Boolean) : Serializable