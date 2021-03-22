package com.viewlift.models.data.appcms.api

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class Team(@SerializedName("players") @Expose
                val players: List<Players>? = null,
                @SerializedName("name") @Expose
                val name: String? = null,
                var contentDataPlayers: List<ContentDatum>? = null):Serializable