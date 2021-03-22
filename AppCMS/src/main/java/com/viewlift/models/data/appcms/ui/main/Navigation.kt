package com.viewlift.models.data.appcms.ui.main

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class Navigation(@SerializedName("backgroundColor") @Expose
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

data class Link(@SerializedName("textColor") @Expose
                val textColor: String? = null,
                @SerializedName("style")
                @Expose
                val style: List<String>? = null,
                @SerializedName("backgroundColor")
                @Expose
                val backgroundColor: String? = null) : Serializable

data class LinkActive(@SerializedName("style") @Expose
                      val style: List<String?>? = null,

                      @SerializedName("backgroundColor")
                      @Expose
                      val backgroundColor: String? = null) : Serializable

data class LinkHover(@SerializedName("textColor") @Expose
                     val textColor: String? = null,

                     @SerializedName("style")
                     @Expose
                     val style: List<String>? = null,

                     @SerializedName("backgroundColor")
                     @Expose
                     val backgroundColor: String? = null) : Serializable