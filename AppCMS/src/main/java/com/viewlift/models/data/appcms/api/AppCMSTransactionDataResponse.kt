package com.viewlift.models.data.appcms.api

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

@Deprecated("Not used if monetization is enabled in main json")
data class AppCMSTransactionDataResponse(@SerializedName(value = "records", alternate = ["videos"]) @Expose
                                         val records: List<AppCMSTransactionDataValue>? = null) : Serializable

@Deprecated("Not used if monetization is enabled in main json")
data class AppCMSTransactionDataValue(@SerializedName("purchaseStatus") @Expose
                                      val purchaseStatus: String,

                                      @SerializedName("rentalPeriod")
                                      @Expose
                                      val rentalPeriod: Float,

                                      @SerializedName("transactionEndDate")
                                      @Expose
                                      val transactionEndDate: Long,

                                      @SerializedName("videoId")
                                      @Expose
                                      val videoId: String,

                                      @SerializedName("id")
                                      @Expose
                                      val id: String,

                                      @SerializedName("gist")
                                      @Expose
                                      val gist: Gist) : Serializable