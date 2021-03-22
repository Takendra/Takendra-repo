package com.viewlift.models.data.appcms.api

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class AuthorData(@SerializedName("publishDate") @Expose
                      val publishDate: Long,
                      @SerializedName("name")
                      @Expose
                      val name: String? = null) : Serializable