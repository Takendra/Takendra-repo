package com.viewlift.models.data.appcms.ui.main

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class Brand(@SerializedName("navigation")
                 @Expose
                 val navigation: Navigation? = null,
                 @SerializedName("cta")
                 @Expose
                 val cta: Cta? = null,
                 @SerializedName("general")
                 @Expose
                 val general: General? = null,
                 @SerializedName("footer") @Expose
                 val footer: Footer? = null,
                 @SerializedName("metadata")
                 @Expose
                 val metadata: Metadata? = null,
                 @SerializedName("player")
                 @Expose
                 val player: PlayerProgressColor? = null,
                 @SerializedName("useDefault")
                 @Expose
                 val isUseDefault: Boolean = false) : Serializable

data class Metadata(@SerializedName("displayDuration") @Expose
                    val isDisplayDuration: Boolean,

                    @SerializedName("displayPublishDate")
                    @Expose
                    val isDisplayPublishedDate: Boolean) : Serializable