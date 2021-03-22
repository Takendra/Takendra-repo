package com.viewlift.models.data.appcms.subscriptions

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class AppCMSSubscriptionPlanResult(@SerializedName("id") @Expose
                                        val id: String?,

                                        @SerializedName("name")
                                        @Expose
                                        val name: String?,

                                        @SerializedName("identifier")
                                        @Expose
                                        val identifier: String?,

                                        @SerializedName("renewable")
                                        @Expose
                                        val isRenewable: Boolean?,

                                        @SerializedName("renewalCyclePeriodMultiplier")
                                        @Expose
                                        val renewalCyclePeriodMultiplier: Int?,

                                        @SerializedName("renewalCycleType")
                                        @Expose
                                        val renewalCycleType: String?,

                                        @SerializedName("planDetails")
                                        @Expose
                                        val planDetails: List<PlanDetail>?=null,

                                        @SerializedName("si_status")
                                        @Expose
                                        val siStatus: String?,

                                        @SerializedName("error")
                                        @Expose
                                        val error: String?,

                                        @SerializedName("message")
                                        @Expose
                                        val message: String?,

                                        @SerializedName("subscriptionStatus")
                                        @Expose
                                        val subscriptionStatus: String?,

                                        @SerializedName("unsubscribed")
                                        @Expose
                                        val isUnsubscribed: Boolean?)