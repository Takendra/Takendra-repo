package com.viewlift.models.data.appcms.ui.main

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class Recommendation(@SerializedName("isRecommendation") @Expose
                          val isRecommendation: Boolean = false,

                          @SerializedName("isRecommendationAutoPlay")
                          @Expose
                          val isRecommendationAutoPlay: Boolean = false,

                          @SerializedName("isPersonalization")
                          @Expose
                          val isPersonalization: Boolean = false,

                          @SerializedName("isSubscribed")
                          @Expose
                          val isSubscribed: Boolean = false,

                          @SerializedName("recommendCategories")
                          @Expose
                          val recommendCategories: MutableList<RecommendationCategories>? = null) : Serializable

data class RecommendationCategories(@SerializedName("title") @Expose
                                    val title: String? = null,

                                    @SerializedName("imgUrl")
                                    @Expose
                                    val imgUrl: String? = null) : Serializable