package com.viewlift.models.billing.appcms.purchase

import com.viewlift.models.data.appcms.api.ContentDatum

data class TvodPurchaseData(
        val seriesId: String?,
        val seasonId: String?,
        val videoId: String?,
        val isSeriesPurchase: Boolean,
        val isSeasonPurchase: Boolean,
        val isBundlePurchase: Boolean,
        val isRent: Boolean = false,
        val planToPurchase: ContentDatum,
        val contentId: String,
        val contentName: String

)

