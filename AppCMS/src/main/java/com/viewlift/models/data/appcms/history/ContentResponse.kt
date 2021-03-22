package com.viewlift.models.data.appcms.history

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.viewlift.models.data.appcms.api.*

data class ContentResponse(@SerializedName("gist") @Expose
                           val gist: Gist?=null,

                           @SerializedName("pricing")
                           @Expose
                           val pricing: Pricing,

                           @SerializedName("contentDetails")
                           @Expose
                           val contentDetails: ContentDetails?=null,

                           @SerializedName("series")
                           @Expose
                           val seriesData: List<ContentDatum>?=null,

                           @SerializedName("tags")
                           @Expose
                           val tags: MutableList<Tag?>?=null,

                           @SerializedName("grade")
                           @Expose
                           val grade: String)