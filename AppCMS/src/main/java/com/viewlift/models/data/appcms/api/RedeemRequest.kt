package com.viewlift.models.data.appcms.api

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * Model class for redemption API request
 *
 * @author Vinay
 * @since 2019-01-21
 * @param url - API URL
 * @param couponCode - coupon Code
 * @param offerCode - offer Code
 * @param site - site name
 * @param platform - would be android/firetv
 * @param siteId - siteId
 * @param transaction - transaction
 * @param userId - userId
 * @param userId - userId
 * @param purchaseType - purchase Type
 * @param currencyCode - currency Code
 */
data class RedeemRequest(@SerializedName("url") @Expose
                         var url: String? = null,

                         @SerializedName("couponCode")
                         @Expose
                         var couponCode: String? = null,

                         @SerializedName("offerCode")
                         @Expose
                         var offerCode: String? = null,

                         @SerializedName("site")
                         @Expose
                         var site: String? = null,

                         @SerializedName("platform")
                         @Expose
                         var platform: String? = null,

                         @SerializedName("siteId")
                         @Expose
                         var siteId: String? = null,

                         @SerializedName("transaction")
                         @Expose
                         var transaction: String? = null,

                         @SerializedName("userId")
                         @Expose
                         var userId: String? = null,

                         @SerializedName("purchaseType")
                         @Expose
                         var purchaseType: String? = null,

                         @SerializedName("currencyCode")
                         @Expose
                         var currencyCode: String? = null)