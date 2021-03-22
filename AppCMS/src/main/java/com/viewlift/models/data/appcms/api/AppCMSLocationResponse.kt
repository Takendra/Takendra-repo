package com.viewlift.models.data.appcms.api

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class AppCMSLocationResponse(@SerializedName("longitude") @Expose
                                  val longitude: Double,
                                  @SerializedName("latitude")
                                  @Expose
                                  val latitude: Double,

                                  @SerializedName("cityname")
                                  @Expose
                                  val cityname: String,

                                  @SerializedName("postalcode")
                                  @Expose
                                  val postalcode: String,

                                  @SerializedName("metrocode")
                                  @Expose
                                  val metrocode: Any,

                                  @SerializedName("timezone")
                                  @Expose
                                  val timezone: String,

                                  @SerializedName("subdivision_iso_code")
                                  @Expose
                                  val subdivisionIsoCode: String,

                                  @SerializedName("subdivision_en_name")
                                  @Expose
                                  val subdivisionEnName: String,

                                  @SerializedName("countryname")
                                  @Expose
                                  val countryname: String,

                                  @SerializedName("countryisocode")
                                  @Expose
                                  val countryisocode: String,

                                  @SerializedName("continent")
                                  @Expose
                                  val continent: String,

                                  @SerializedName("ispname")
                                  @Expose
                                  val ispname: String,

                                  @SerializedName("isporganization")
                                  @Expose
                                  val isporganization: String,

                                  @SerializedName("origip")
                                  @Expose
                                  val origip: String)