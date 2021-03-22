package com.viewlift.models.data.appcms.ui.authentication

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class RestoreAmazonPurchase(@SerializedName("email") @Expose
                                 val email: String?=null,


                                 @SerializedName("userId")
                                 @Expose
                                 val userId: String?=null,

                                 @SerializedName("status")
                                 @Expose
                                 val status: String?=null,


                                 @SerializedName("subscriptionStatus")
                                 @Expose
                                 val subscriptionStatus: String?=null)