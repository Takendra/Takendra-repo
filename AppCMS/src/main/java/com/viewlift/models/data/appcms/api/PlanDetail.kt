package com.viewlift.models.data.appcms.api

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

/**
 * Model class for plan details API response
 *
 * @author Jonathan
 * @since 2017-07-17
 * @param recurringPaymentAmount - recurring Payment Amount
 * @param recurringPaymentCurrencyCode - recurring Payment Currency Code
 * @param featureDetails - list features available on plan
 * @param countryCode - country Code
 * @param preRoll - contains ad url for videos to play
 * @param callToAction -
 * @param discountedPrice - discounted Price
 * @param isDefault - if true show this plan details otherwise pick the 0th element
 * @param supportedDevices - list of platforms supported for this plan
 * @param strikeThroughPrice - price to be shown with strike through
 * @param title - plan name
 * @param description - plan description
 * @param description - plan description
 * @param isPurchaseEnabled - for TVOD plan
 * @param isRentEnabled - for TVOD plan
 * @param purchaseAmount - TVOD plan purchase price
 * @param rentAmount - TVOD plan rent price
 */

data class PlanDetail(@SerializedName(value = "recurringPaymentAmount", alternate = ["amount"]) @Expose
                      val recurringPaymentAmount: Double,
                      @SerializedName(value = "recurringPaymentCurrencyCode", alternate = ["currencyCode"])
                      @Expose
                      val recurringPaymentCurrencyCode: String? = null,

                      @SerializedName(value = "featureDetails", alternate = ["features"])
                      @Expose
                      val featureDetails: List<FeatureDetail>? = null,

                      @SerializedName("countryCode")
                      @Expose
                      val countryCode: String,

                      @SerializedName("preRoll")
                      @Expose
                      val preRoll: PreRoll? = null,

                      @SerializedName("callToAction")
                      @Expose
                      val callToAction: String? = null,

                      @SerializedName("discountedPrice")
                      @Expose
                      val discountedPrice: Double,

                      @SerializedName("isDefault")
                      @Expose
                      val isDefault: Boolean,

                      @SerializedName("supportedDevices")
                      @Expose
                      val supportedDevices: List<String>,

                      @SerializedName("strikeThroughPrice")
                      @Expose
                      val strikeThroughPrice: Double,

                      @SerializedName("title")
                      @Expose
                      val title: String? = null,

                      @SerializedName("description")
                      @Expose
                      val description: String? = null,

                      @SerializedName("isPurchaseEnabled")
                      @Expose
                      val isPurchaseEnabled: Boolean,

                      @SerializedName("isRentEnabled")
                      @Expose
                      val isRentEnabled: Boolean,
                      @SerializedName("purchaseAmount")
                      @Expose
                      val purchaseAmount: Float = 0F,
                      @SerializedName("rentAmount")
                      @Expose
                      val rentAmount: Float = 0F,
                      @SerializedName("allowedPayMethods")
                      @Expose
                      var allowedPayMethods: List<String?>? = null,
                      @SerializedName("carrierBillingProviders")
                      @Expose
                      val carrierBillingProviders: List<String?>? = null,

                      @SerializedName("hidePlanPrice")
                      @Expose
                      val isHidePlanPrice: Boolean = false) : Serializable