package com.viewlift.models.data.appcms.api

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class AppCMSSavedCardsResponse(@SerializedName("statusCode") @Expose
                                    val statusCode: Int,

                                    @SerializedName("Data")
                                    @Expose
                                    val cardDataResult: CardDataResult? = null)

data class CardDataResult(@SerializedName("cards") @Expose
                          val cardsList: List<CardData>? = null)

data class CardData(
        @SerializedName("card_token") @Expose
        val cardToken: String,

        @SerializedName("card_reference")
        @Expose
        val cardReference: String,

        @SerializedName("card_number")
        @Expose
        val cardNumber: String,

        @SerializedName("card_isin")
        @Expose
        val cardIsIn: String,

        @SerializedName("card_exp_year")
        @Expose
        val cardExpYear: String,

        @SerializedName("card_exp_month")
        @Expose
        val cardExpMonth: String,

        @SerializedName("card_type")
        @Expose
        val cardType: String,

        @SerializedName("card_issuer")
        @Expose
        val cardIssuer: String,

        @SerializedName("card_brand")
        @Expose
        val cardBrand: String,

        @SerializedName("name_on_card")
        @Expose
        val nameOnCard: String,

        @SerializedName("expired")
        @Expose
        val expired: String,

        @SerializedName("card_fingerprint")
        @Expose
        val cardFingerprint: String
)