package com.viewlift.models.data.verimatrix

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class TVProvider(@SerializedName("display_name") @Expose
                      val displayName: String,
                      @SerializedName("name")
                      @Expose
                      val name: String,
                      @SerializedName("logos")
                      @Expose
                      val images: Logo? = null,
                      var tvProviderIdp: String,
                      var userId: String,
                      var resourceIds: MutableMap<String?, Boolean?>)

data class Logo(@SerializedName("full") @Expose
                val imageUrl: String? = null)