package com.viewlift.models.data.appcms.ui.main

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class GetRecommendationGenres (@SerializedName("genreValue") @Expose
                                    val genreValue: String)