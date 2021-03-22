package com.viewlift.models.data.appcms.ui.main

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class Footer(@SerializedName("textColor") @Expose
                  val textColor: String? = null,

                  @SerializedName("backgroundColor")
                  @Expose
                  val backgroundColor: String? = null,

                  @SerializedName("link")
                  @Expose
                  val link: Link? = null,

                  @SerializedName("link--active")
                  @Expose
                  val linkActive: LinkActive? = null,

                  @SerializedName("link--hover")
                  @Expose
                  val linkHover: LinkHover? = null) : Serializable