package com.viewlift.models.data.appcms.ui.authentication

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.viewlift.models.data.appcms.subscriptions.PlanDetail

data class MonetizationPlan(@SerializedName("featureSetting") @Expose
                            val featureSetting: FeatureSetting? = null,
                            @SerializedName("planDetails")
                            @Expose
                            val planDetails: List<PlanDetail>? = null,

                            @SerializedName("name")
                            @Expose
                            val name: String,

                            @SerializedName("description")
                            @Expose
                            val description: String?=null,

                            @SerializedName("renewalCycleType")
                            @Expose
                            val renewalCycleType: String,

                            @SerializedName("renewalCyclePeriodMultiplier")
                            @Expose
                            val renewalCyclePeriodMultiplier: Int)