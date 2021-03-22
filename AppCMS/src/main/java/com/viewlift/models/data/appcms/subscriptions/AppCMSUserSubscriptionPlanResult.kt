package com.viewlift.models.data.appcms.subscriptions

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class AppCMSUserSubscriptionPlanResult(@SerializedName("subscriptionPlanInfo")
                                            @Expose
                                            var subscriptionPlanInfo: AppCMSSubscriptionPlanResult? = null,

                                            @SerializedName("subscriptionInfo")
                                            @Expose
                                            val subscriptionInfo: AppCMSUserSubscriptionPlanInfoResult? = null)