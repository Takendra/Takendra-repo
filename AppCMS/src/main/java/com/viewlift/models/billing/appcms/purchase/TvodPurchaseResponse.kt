package com.viewlift.models.billing.appcms.purchase

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class TvodPurchaseResponse(@SerializedName("status")
                                @Expose
                                val status: String? = null,
                                @SerializedName("error")
                                @Expose
                                val error: String? = null,
                                @SerializedName("details")
                                @Expose
                                val details: Details? = null)

data class Details(@SerializedName("order_id")
                   @Expose
                   val orderId: String? = null,
                   @SerializedName("juspay")
                   @Expose
                   val juspay: Juspay? = null)

data class Juspay(@SerializedName("client_auth_token")
                  @Expose
                  val clientToken: String? = null)

