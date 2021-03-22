package com.viewlift.models.billing.appcms.purchase

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class SocketData(@SerializedName("action")
                      @Expose
                      val action: String = "subscribe",
                      @SerializedName("eventType")
                      @Expose
                      val eventType: String = "PURCHASE_SUCCESS",
                      @SerializedName("token")
                      @Expose
                      val token: String)

data class SocketResponse(@SerializedName("event")
                          @Expose
                          val event: String,
                          @SerializedName("status")
                          @Expose
                          val status: String)

