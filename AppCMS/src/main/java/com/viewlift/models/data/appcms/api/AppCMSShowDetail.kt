package com.viewlift.models.data.appcms.api

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class AppCMSShowDetail(@SerializedName("permalink")
                            @Expose
                            val permalink: String,
                            @SerializedName("gist")
                            @Expose
                            val gist: Gist,
                            @SerializedName("seasons")
                            @Expose
                            val seasons: List<Season_>,
                            @SerializedName("id")
                            @Expose
                            val id: String) {
    fun convertToContentDatum(): ContentDatum? {
        val contentDatum = ContentDatum()
        val contentDetails = ContentDetails()
        contentDatum.gist = gist
        contentDatum.contentDetails = contentDetails
        contentDatum.season = seasons
        contentDatum.permalink = permalink
        contentDatum.id = id
        return contentDatum
    }
}