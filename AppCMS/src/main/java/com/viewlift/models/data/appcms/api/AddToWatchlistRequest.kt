package com.viewlift.models.data.appcms.api

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class AddToWatchlistRequest(@SerializedName("userId") @Expose
                                 var userId: String? = null,

                                 @SerializedName("contentId")
                                 @Expose
                                 var contentId: String? = null,

                                 @SerializedName("contentType")
                                 @Expose
                                 var contentType: String? = null,

                                 @SerializedName("position")
                                 @Expose
                                 var position: Long = 0,

                                 @SerializedName("contentIds")
                                 @Expose
                                 var contentIds: String? = null)