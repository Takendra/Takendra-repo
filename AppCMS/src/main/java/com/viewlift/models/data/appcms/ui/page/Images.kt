package com.viewlift.models.data.appcms.ui.page

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class Images(@SerializedName("desktop")
                  @Expose
                  val desktop: String? = null,

                  @SerializedName("mobile")
                  @Expose
                  val mobile: String? = null,

                  @SerializedName("tabletLandscape")
                  @Expose
                  val tabletLandscape: String? = null,

                  @SerializedName("tabletPortrait")
                  @Expose
                  val tabletPortrait: String? = null,

                  @SerializedName("ott")
                  @Expose
                  val isOtt: Boolean = false,

                  @SerializedName("crossImage")
                  @Expose
                  val crossImage: String? = null,

                  @SerializedName("planBackgroundImage")
                  @Expose
                  val planBackgroundImage: String? = null) : Serializable
