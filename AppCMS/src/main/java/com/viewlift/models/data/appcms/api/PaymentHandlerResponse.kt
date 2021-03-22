package com.viewlift.models.data.appcms.api

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class PaymentHandlerResponse(@SerializedName("order_id") @Expose
                                  var orderId: String? = null,
                                  @SerializedName("client_auth_token")
                                  @Expose
                                  val clientAuthToken: String? = null,

                                  @SerializedName("client_auth_token_expiry")
                                  @Expose
                                  val clientAuthTokenExpiry: String? = null,

                                  @SerializedName("cancelDate")
                                  @Expose
                                  val cancelDate: Any? = null,

                                  @SerializedName("purchaseDate")
                                  @Expose
                                  val purchaseDate: Long = 0,

                                  @SerializedName("parentProductId")
                                  @Expose
                                  val parentProductId: Any? = null,

                                  @SerializedName("quantity")
                                  @Expose
                                  val quantity: Any? = null,

                                  @SerializedName("isCanceled")
                                  @Expose
                                  val isCanceled: Boolean = false,

                                  @SerializedName("deferredDate")
                                  @Expose
                                  val deferredDate: Any? = null,

                                  @SerializedName("productId")
                                  @Expose
                                  val productId: String? = null,

                                  @SerializedName("freeTrialEndDate")
                                  @Expose
                                  val freeTrialEndDate: Long = 0,

                                  @SerializedName("sandbox")
                                  @Expose
                                  val sandbox: Boolean = false,

                                  @SerializedName("deferredSku")
                                  @Expose
                                  val deferredSku: Any? = null,

                                  @SerializedName("renewalDate")
                                  @Expose
                                  val renewalDate: Long = 0,

                                  @SerializedName("termSku")
                                  @Expose
                                  val termSku: String? = null,

                                  @SerializedName("service")
                                  @Expose
                                  val service: String? = null,

                                  @SerializedName("term")
                                  @Expose
                                  val term: String? = null,

                                  @SerializedName("receiptId")
                                  @Expose
                                  val receiptId: String? = null,

                                  @SerializedName("productType")
                                  @Expose
                                  val productType: String? = null,

                                  @SerializedName("testTransaction")
                                  @Expose
                                  val testTransaction: Boolean = false,

                                  @SerializedName("betaProduct")
                                  @Expose
                                  val betaProduct: Boolean = false,

                                  @SerializedName("status")
                                  @Expose
                                  val status: Int = 0, ) : Serializable