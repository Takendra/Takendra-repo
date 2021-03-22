package com.viewlift.models.data.appcms.api

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class AppCMSCardInfoResponse(@SerializedName("id") @Expose
                                  val id: String,
                                  @SerializedName("object")
                                  @Expose
                                  val `object`: String,
                                  @SerializedName("brand")
                                  @Expose
                                  val brand: String,
                                  @SerializedName("bank")
                                  @Expose
                                  val bank: String,
                                  @SerializedName("country")
                                  @Expose
                                  val country: String,
                                  @SerializedName("type")
                                  @Expose
                                  val type: String,
                                  @SerializedName("mandate_support")
                                  @Expose
                                  val isMandateSupport: Boolean)