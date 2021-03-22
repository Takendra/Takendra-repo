package com.viewlift.models.data.appcms.api

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class Images(@SerializedName("_1x1") @Expose
                  val _1x1: Image_1x1? = null,

                  @SerializedName("_3x4")
                  @Expose
                  val _3x4: Image_3x4? = null,

                  @SerializedName("_16x9")
                  @Expose
                  var _16x9: Image_16x9? = null,

                  @SerializedName("_16x9Image")
                  @Expose
                  var _16x9Image: Image_16x9? = null,

                  @SerializedName("_3x4Image")
                  @Expose
                  val _3x4Image: Image_3x4? = null,

                  @SerializedName("_32x9Image")
                  @Expose
                  val _32x9Image: Image_32x9? = null) : Serializable

data class Image_1x1(@SerializedName("url") @Expose
                     val url: String? = null) : Serializable

data class Image_3x4(@SerializedName("url") @Expose
                     val url: String? = null) : Serializable

data class Image_32x9(@SerializedName("url") @Expose
                      val url: String? = null) : Serializable

data class Image_16x9(@SerializedName("url") @Expose
                      var url: String? = null,

                      @SerializedName("secureUrl")
                      @Expose
                      var secureUrl: String? = null) : Serializable