package com.viewlift.models.data.appcms.api

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.viewlift.models.data.appcms.weather.Cities
import java.io.Serializable

data class AppCMSPageAPI(@SerializedName("id") @Expose
                         var id: String? = null,

                         @SerializedName("title")
                         @Expose
                         var title: String? = null,

                         @SerializedName("metadataMap")
                         @Expose
                         val metadataMap: MetadataMap? = null,

                         @SerializedName("modules")
                         @Expose
                         var modules: List<Module>? = null,

                         @SerializedName("moduleIds")
                         @Expose
                         var moduleIds: List<String>?= emptyList(),
                         var weatherWidgetData: Cities? = null) : Serializable