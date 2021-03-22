package com.viewlift.models.data.appcms.subscriptions

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class AppCMSValidatePlaySubRequest(@SerializedName("receipt") @Expose
                                        val receipt: String,

                                        @SerializedName("planIdentifier")
                                        @Expose
                                        val planIdentifier: String)