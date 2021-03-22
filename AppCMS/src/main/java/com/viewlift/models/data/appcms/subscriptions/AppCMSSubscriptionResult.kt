package com.viewlift.models.data.appcms.subscriptions

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class AppCMSSubscriptionResult(@SerializedName("subscriptionPlanInfo") @Expose
                                    val subscriptionPlanInfo: Any,

                                    @SerializedName("subscriptionInfo")
                                    @Expose
                                    val subscriptionInfo: Any,

                                    @SerializedName("recurringPaymentAgreementReferenceId")
                                    @Expose
                                    val recurringPaymentAgreementReferenceId: Long,

                                    @SerializedName("recurringPaymentAgreementId")
                                    @Expose
                                    val recurringPaymentAgreementId: Long,

                                    @SerializedName("subscriptionOfferUsage")
                                    @Expose
                                    val subscriptionOfferUsage: Any)