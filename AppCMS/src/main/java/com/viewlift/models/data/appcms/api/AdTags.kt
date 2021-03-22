package com.viewlift.models.data.appcms.api

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class AdTags(@SerializedName("android") @Expose
                  val androidAdTag: AndroidAdTag? = null,

                  @SerializedName("amazon_fire")
                  @Expose
                  val amazonAdTag: AndroidAdTag? = null
) : Serializable

data class AndroidAdTag(@SerializedName("active") @Expose
                        val isActive: Boolean,

                        @SerializedName("url")
                        @Expose
                        val adUrl: String? = null
) : Serializable