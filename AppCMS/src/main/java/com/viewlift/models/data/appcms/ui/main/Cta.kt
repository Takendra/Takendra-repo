package com.viewlift.models.data.appcms.ui.main

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class Cta(@SerializedName("secondary--hover") @Expose
               val secondaryHover: SecondaryHover? = null,

               @SerializedName("primary")
               @Expose
               val primary: Primary? = null,

               @SerializedName("secondary")
               @Expose
               val secondary: Secondary? = null,

               @SerializedName("primary--hover")
               @Expose
               val primaryHover: PrimaryHover? = null) : Serializable

data class Primary(@SerializedName("textColor") @Expose
                   val textColor: String? = null,

                   @SerializedName("backgroundColor")
                   @Expose
                   val backgroundColor: String? = null,

                   @SerializedName("border")
                   @Expose
                   val border: Border? = null) : Serializable

data class PrimaryHover(@SerializedName("textColor") @Expose
                        val textColor: String? = null,

                        @SerializedName("backgroundColor")
                        @Expose
                        val backgroundColor: String? = null,

                        @SerializedName("border")
                        @Expose
                        val border: Border? = null) : Serializable

data class Secondary(@SerializedName("textColor") @Expose
                     val textColor: String? = null,

                     @SerializedName("backgroundColor")
                     @Expose
                     val backgroundColor: String? = null,

                     @SerializedName("border")
                     @Expose
                     val border: Border? = null) : Serializable

data class SecondaryHover(@SerializedName("textColor") @Expose
                          val textColor: String? = null,

                          @SerializedName("backgroundColor")
                          @Expose
                          val backgroundColor: String? = null,

                          @SerializedName("border")
                          @Expose
                          val border: Border? = null) : Serializable

data class Border(@SerializedName("color") @Expose
                  val color: String? = null,

                  @SerializedName("width")
                  @Expose
                  val width: String? = null) : Serializable

