package com.viewlift.models.data.appcms.api

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

@Deprecated("Not used if monetization is enabled in main json")
data class Pricing(@SerializedName("rent") @Expose
                   val rent: Rent,
                   @SerializedName("type")
                   @Expose
                   @Deprecated("Not used if monetization is enabled in main json")
                   val type: String? = null) : Serializable

@Deprecated("Not used if monetization is enabled in main json")
data class Rent(@SerializedName("rentalPeriod") @Expose
                @Deprecated("Not used if monetization is enabled in main json")
                val rentalPeriod: Int) : Serializable