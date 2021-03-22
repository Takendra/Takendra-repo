package com.viewlift.models.data.appcms.subscriptions

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class AppCMSRestorePurchaseRequest(@SerializedName("paymentUniqueId") @Expose
                                        var paymentUniqueId: String? = null,

                                        @SerializedName("site")
                                        @Expose
                                        var site: String? = null,

                                        @SerializedName("platform")
                                        @Expose
                                        var platform: String? = null)