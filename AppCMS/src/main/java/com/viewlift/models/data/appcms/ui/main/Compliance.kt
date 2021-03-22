package com.viewlift.models.data.appcms.ui.main

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class Compliance(@SerializedName("coppa") @Expose
                      var isCoppa: Boolean,
                      @SerializedName("euPortability")
                      @Expose
                      var isEuPortability: Boolean,

                      @SerializedName("gdpr")
                      @Expose
                      var isGdpr: Boolean,

                      @SerializedName("dcCompliance")
                      @Expose
                      var isDcCompliance: Boolean) : Serializable