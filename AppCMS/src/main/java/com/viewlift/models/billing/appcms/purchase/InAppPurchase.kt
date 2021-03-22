package com.viewlift.models.billing.appcms.purchase

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class InAppPurchase(@SerializedName("transaction")
                         @Expose
                         var transaction: String? = null,
                         @SerializedName("offerCode")
                         @Expose
                         var offerCode: String? = null,
                         @SerializedName("receipt")
                         @Expose
                         var receipt: String? = null,
                         @SerializedName("purchaseType")
                         @Expose
                         var purchaseType: String? = null,
                         @SerializedName("platform")
                         @Expose
                         var platform: String? = null,
                         @SerializedName("siteId")
                         @Expose
                         var siteId: String? = null,
                         @SerializedName("userId")
                         @Expose
                         var userId: String? = null,
                         @SerializedName("site")
                         @Expose
                         var site: String? = null,
                         @SerializedName("device")
                         @Expose
                         var device: String? = null,
                         @SerializedName("contentRequest")
                         @Expose
                         var contentRequest: ContentRequest? = null,
                         @SerializedName("paymentUniqueId")
                         @Expose
                         var paymentUniqueId: String? = null,
                         @SerializedName("redirectUrl")
                         @Expose
                         var redirectUrl: String? = null,
                         @SerializedName("juspayData")
                         @Expose
                         var juspayData: JuspayData? = null)

data class ContentRequest(@SerializedName("contentType")
                          @Expose
                          val contentType: String,
                          @SerializedName("seriesId")
                          @Expose
                          val seriesId: String?,
                          @SerializedName("seasonId")
                          @Expose
                          val seasonId: String?,
                          @SerializedName("bundleId")
                          @Expose
                          val bundleId: String?,
                          @SerializedName("contentId")
                          @Expose
                          val contentId: String?,
                          @SerializedName("videoQuality")
                          @Expose
                          val videoQuality: String)

data class JuspayData(@SerializedName("options.create_mandate")
                      @Expose
                      val createMandate: String = "OPTIONAL",
                      @SerializedName("options.get_client_auth_token")
                      @Expose
                      val getClientAuthToken: Boolean = true,
                      @SerializedName("mobile_number")
                      @Expose
                      var mobileNumber: String? = null)


