package com.viewlift.utils

import android.content.Context
import com.google.gson.GsonBuilder
import com.viewlift.R
import com.viewlift.models.data.appcms.api.*
import com.viewlift.models.data.appcms.ui.authentication.FeatureSetting
import com.viewlift.models.data.appcms.user.UserPurchases
import java.util.*

/**
 * This class is responsible to check monetization model and plan attached to content
 * Also checks whether the content is purchased/rented by user
 * @author  Wishy
 * @since   2020-07-20
 */
class ContentTypeChecker(val context: Context) {

    /** Check if content is FREE
     * @param subscriptionPlans - list of plans
     * @return true if found
     * */
    fun isContentFree(subscriptionPlans: List<ContentDatum>): Boolean {
        return subscriptionPlans.any { it.planMonetizationModel.equals(context.getString(R.string.pricing_model_FREE), ignoreCase = true) }
//        if (subscriptionPlans.size != 0)
//            return checkAllFree(subscriptionPlans)
//        return false
    }

    /** Check if content is AVOD
     * @param subscriptionPlans - list of plans
     * @return true if found
     * */
    fun isContentAVOD(subscriptionPlans: List<ContentDatum>): Boolean {
        if (subscriptionPlans.size != 0)
            return checkAllAvod(subscriptionPlans)
        return false
    }

    /** Check if content is TVOD
     * @param subscriptionPlans - list of plans
     * @return true if found
     * */
    fun isContentTVOD(subscriptionPlans: List<ContentDatum>): Boolean {
        if (subscriptionPlans.size != 0)
            return checkAllTvod(subscriptionPlans)
        return false
    }

    /** Check if SVOD plan exists
     * @param subscriptionPlans - list of plans
     * @return true if found
     * */
    fun isSvodContentExist(subscriptionPlans: List<ContentDatum>): Boolean {
        return subscriptionPlans.any { it.planMonetizationModel.equals(context.getString(R.string.pricing_model_SVOD), ignoreCase = true) }
    }

    /** Check if content is SVOD
     * @param subscriptionPlans - list of plans
     * @return true if found
     * */
    fun isContentSVOD(subscriptionPlans: List<ContentDatum>): Boolean {
        if (subscriptionPlans.size != 0)
            return checkAllSvod(subscriptionPlans)
        return false
    }

    /** Check if content is TVE
     * @param subscriptionPlans - list of plans
     * @return true if found
     * */
    fun isContentTVE(subscriptionPlans: List<ContentDatum>): Boolean {
        if (subscriptionPlans.size != 0)
            return checkAllTve(subscriptionPlans)
        return false
    }

    /** Check if content is TVE and SVOD both
     * @param subscriptionPlans - list of plans
     * @param monetizationModels - list of monetizationModels
     * @return true if found
     * */
    fun isContentSVOD_TVE(subscriptionPlans: List<ContentDatum>, monetizationModels: List<MonetizationModels>?): Boolean {
        var svod = subscriptionPlans.any { it.planMonetizationModel.equals(context.getString(R.string.pricing_model_SVOD), ignoreCase = true) }
        val tve = subscriptionPlans.any { it.planMonetizationModel.equals(context.getString(R.string.pricing_model_TVE), ignoreCase = true) }
        if (monetizationModels != null && isContentSvodPlanSeries(monetizationModels)) svod = true
        return svod && tve
    }

    /** Check if content is TVOD and SVOD both
     * @param subscriptionPlans - list of plans
     * @param monetizationModels - list of monetizationModels
     * @return true if found
     * */
    fun isContentSVOD_TVOD(subscriptionPlans: List<ContentDatum>, monetizationModels: List<MonetizationModels>?): Boolean {
        val tvod = subscriptionPlans.any { it.planMonetizationModel.equals(context.getString(R.string.pricing_model_TVOD), ignoreCase = true) }
        var svod = subscriptionPlans.any { it.planMonetizationModel.equals(context.getString(R.string.pricing_model_SVOD), ignoreCase = true) }
        if (monetizationModels != null && isContentSvodPlanSeries(monetizationModels)) svod = true
        return svod && tvod
    }

    /** Check if content is TVE and TVOD both
     * @param subscriptionPlans - list of plans
     * @param monetizationModels - list of monetizationModels
     * @return true if found
     * */
    fun isContentTVOD_TVE(subscriptionPlans: List<ContentDatum>): Boolean {
        val tvod = subscriptionPlans.any { it.planMonetizationModel.equals(context.getString(R.string.pricing_model_TVOD), ignoreCase = true) }
        val tve = subscriptionPlans.any { it.planMonetizationModel.equals(context.getString(R.string.pricing_model_TVE), ignoreCase = true) }
        return tve && tvod
    }

    /** Check if content is TVE, SVOD, TVOD
     * @param subscriptionPlans - list of plans
     * @param monetizationModels - list of monetizationModels
     * @return true if found
     * */
    fun isContentSVOD_TVOD_TVE(subscriptionPlans: List<ContentDatum>, monetizationModels: List<MonetizationModels>?): Boolean {
        val tvod = subscriptionPlans.any { it.planMonetizationModel.equals(context.getString(R.string.pricing_model_TVOD), ignoreCase = true) }
        val tve = subscriptionPlans.any { it.planMonetizationModel.equals(context.getString(R.string.pricing_model_TVE), ignoreCase = true) }
        var svod = subscriptionPlans.any { it.planMonetizationModel.equals(context.getString(R.string.pricing_model_SVOD), ignoreCase = true) }

        if (monetizationModels != null && isContentSvodPlanSeries(monetizationModels)) svod = true
        return svod && tvod && tve
    }

    /** Check if content is AVOD and TVOD both
     * @param subscriptionPlans - list of plans
     * @param monetizationModels - list of monetizationModels
     * @return true if found
     * */
    fun isContentTVOD_AVOD(subscriptionPlans: List<ContentDatum>): Boolean {
        val tvod = subscriptionPlans.any { it.planMonetizationModel.equals(context.getString(R.string.pricing_model_TVOD), ignoreCase = true) }
        val avod = subscriptionPlans.any { it.planMonetizationModel.equals(context.getString(R.string.pricing_model_AVOD), ignoreCase = true) }
        return tvod && avod
    }

    /** Check if content is TVE and AVOD both
     * @param subscriptionPlans - list of plans
     * @param monetizationModels - list of monetizationModels
     * @return true if found
     * */
    fun isContentTVE_AVOD(subscriptionPlans: List<ContentDatum>): Boolean {
        val tve = subscriptionPlans.any { it.planMonetizationModel.equals(context.getString(R.string.pricing_model_TVE), ignoreCase = true) }
        val avod = subscriptionPlans.any { it.planMonetizationModel.equals(context.getString(R.string.pricing_model_AVOD), ignoreCase = true) }
        return tve && avod
    }

    /** Check if content is AVOD and SVOD both
     * @param subscriptionPlans - list of plans
     * @param monetizationModels - list of monetizationModels
     * @return true if found
     * */
    fun isContentSVOD_AVOD(subscriptionPlans: List<ContentDatum>): Boolean {
        val svod = subscriptionPlans.any { it.planMonetizationModel.equals(context.getString(R.string.pricing_model_SVOD), ignoreCase = true) }
        val avod = subscriptionPlans.any { it.planMonetizationModel.equals(context.getString(R.string.pricing_model_AVOD), ignoreCase = true) }
        return svod && avod
    }

    /** Check if ads are enabled
     * @param subscriptionPlans - list of plans
     * @param monetizationType - canbe TVOD / SVOD
     * @return true if enabled
     * */
    fun isAdsAvailable(subscriptionPlans: List<ContentDatum>, monetizationType: String): Boolean {
        return subscriptionPlans.filter { it.planMonetizationModel.equals(monetizationType, ignoreCase = true) }.first().featureSetting.isIncludingAds
    }

    /** Fetch android ad url for video player
     * @param subscriptionPlans - list of plans
     * @return associated ad url
     * */
    fun getAdTagFromAVOD(subscriptionPlans: List<ContentDatum>): String? {
        var adTag: String? = null
        if (subscriptionPlans.size > 0) {
            for (i in subscriptionPlans.indices) {
                if (subscriptionPlans[i].planMonetizationModel != null &&
                        subscriptionPlans[i].planMonetizationModel.equals(context.getString(R.string.pricing_model_AVOD), ignoreCase = true)) {
                    if (subscriptionPlans[i].planDetails != null && subscriptionPlans[i].planDetails.size > 0) {
                        var planIndex = subscriptionPlans[i].planDetails.indexOf(subscriptionPlans[i].planDetails.find {
                            it.isDefault
                        })
                        if (planIndex == -1)
                            planIndex = 0
                        if (subscriptionPlans[i].planDetails[planIndex] != null && subscriptionPlans[i].planDetails[planIndex].preRoll?.adTags?.androidAdTag?.adUrl != null)
                            adTag = subscriptionPlans[i].planDetails[planIndex].preRoll?.adTags?.androidAdTag?.adUrl
                        return adTag
                    }

                }
            }
        }
        return adTag
    }

    /** Fetch firetv ad url for video player
     * @param subscriptionPlans - list of plans
     * @return associated ad url
     * */
    fun getAmazonAdTagFromAVOD(subscriptionPlans: List<ContentDatum>): String? {
        var adTag: String? = null
        if (subscriptionPlans.size > 0) {
            for (i in subscriptionPlans.indices) {
                if (subscriptionPlans[i].planMonetizationModel != null &&
                        subscriptionPlans[i].planMonetizationModel.equals(context.getString(R.string.pricing_model_AVOD), ignoreCase = true)) {
                    if (subscriptionPlans[i].planDetails != null && subscriptionPlans[i].planDetails.size > 0) {
                        var planIndex = subscriptionPlans[i].planDetails.indexOf(subscriptionPlans[i].planDetails.find {
                            it.isDefault
                        })
                        if (planIndex == -1)
                            planIndex = 0
                        if (subscriptionPlans[i].planDetails[planIndex] != null && subscriptionPlans[i].planDetails[planIndex].preRoll?.adTags?.amazonAdTag?.adUrl != null
                                && subscriptionPlans[i].planDetails[planIndex].preRoll?.adTags?.amazonAdTag?.isActive!!)
                            adTag = subscriptionPlans[i].planDetails[planIndex].preRoll?.adTags?.amazonAdTag?.adUrl
                        return adTag
                    }

                }
            }
        }
        return adTag
    }

    /** Fetch android ad url for video player
     * @param ads - Ads object
     * @return associated ad url
     * */
    fun getAdTagAndroid(ads: Ads): String? {
        var adTag: String? = null
        if (ads.preRoll != null && ads.preRoll.adTags != null && ads.preRoll.adTags.androidAdTag != null && ads.preRoll.adTags.androidAdTag.isActive)
            adTag = ads.preRoll.adTags.androidAdTag.adUrl
        return adTag
    }

    /** Fetch firetv ad url for video player
     * @param ads - Ads object
     * @return associated ad url
     * */
    fun getAdTagAmazon(ads: Ads): String? {
        var adTag: String? = null
        if (ads.preRoll != null && ads.preRoll.adTags != null && ads.preRoll.adTags.amazonAdTag != null && ads.preRoll.adTags.amazonAdTag.isActive)
            adTag = ads.preRoll.adTags.amazonAdTag.adUrl
        return adTag
    }

    /** Fetch features to play ads
     * @param subscriptionPlans - list of plans
     * @return featuresetting object for ads
     * */
    fun getAdFeatures(subscriptionPlans: List<ContentDatum>): FeatureSetting? {
        var featureSetting: FeatureSetting? = null
        if (subscriptionPlans.size > 0) {
            for (i in subscriptionPlans.indices) {
                if (subscriptionPlans[i].planMonetizationModel != null &&
                        subscriptionPlans[i].planMonetizationModel.equals(context.getString(R.string.pricing_model_AVOD), ignoreCase = true)
                        && subscriptionPlans[i].featureSetting != null) featureSetting = subscriptionPlans[i].featureSetting
                return featureSetting
            }
        }
        return featureSetting
    }

    /** Fetch features to FREE plan
     * @param subscriptionPlans - list of plans
     * @return featuresetting object for FREE plan
     * */
    fun getFreePlanFeatures(subscriptionPlans: List<ContentDatum>): FeatureSetting? {
        var featureSetting: FeatureSetting? = null
        if (subscriptionPlans.size > 0) {
            for (i in subscriptionPlans.indices) {
                if (subscriptionPlans[i].planMonetizationModel != null &&
                        subscriptionPlans[i].planMonetizationModel.equals(context.getString(R.string.pricing_model_FREE), ignoreCase = true)
                        && subscriptionPlans[i].featureSetting != null)
                    featureSetting = subscriptionPlans[i].featureSetting
                return featureSetting
            }
        }
        return featureSetting
    }


    /** Check if all plans are FREE
     * @param subscriptionPlans - list of plans
     * @return true if found
     * */
    private fun checkAllFree(subscriptionPlans: List<ContentDatum>): Boolean {
        var free = false
        for (i in subscriptionPlans.indices) {
            if (subscriptionPlans[i].planMonetizationModel != null &&
                    subscriptionPlans[i].planMonetizationModel.equals(context.getString(R.string.pricing_model_FREE), ignoreCase = true))
                free = true
            else
                return false
        }
        return free
    }

    /** Check if all plans are AVOD
     * @param subscriptionPlans - list of plans
     * @return true if found
     * */
    private fun checkAllAvod(subscriptionPlans: List<ContentDatum>): Boolean {
        var avod = false
        for (i in subscriptionPlans.indices) {
            if (subscriptionPlans[i].planMonetizationModel != null &&
                    subscriptionPlans[i].planMonetizationModel.equals(context.getString(R.string.pricing_model_AVOD), ignoreCase = true))
                avod = true
            else
                return false
        }
        return avod
    }

    /** Check if all plans are TVOD
     * @param subscriptionPlans - list of plans
     * @return true if found
     * */
    private fun checkAllTvod(subscriptionPlans: List<ContentDatum>): Boolean {
        var tvod = false
        for (i in subscriptionPlans.indices) {
            if (subscriptionPlans[i].planMonetizationModel != null &&
                    subscriptionPlans[i].planMonetizationModel.equals(context.getString(R.string.pricing_model_TVOD), ignoreCase = true))
                tvod = true
            else
                return false
        }
        return tvod
    }

    /** Check if all plans are SVOD
     * @param subscriptionPlans - list of plans
     * @return true if found
     * */
    private fun checkAllSvod(subscriptionPlans: List<ContentDatum>): Boolean {
        var svod = false
        for (i in subscriptionPlans.indices) {
            if (subscriptionPlans[i].planMonetizationModel != null &&
                    subscriptionPlans[i].planMonetizationModel.equals(context.getString(R.string.pricing_model_SVOD), ignoreCase = true))
                svod = true
            else
                return false
        }
        return svod
    }

    /** Check if all plans are TVE
     * @param subscriptionPlans - list of plans
     * @return true if found
     * */
    private fun checkAllTve(subscriptionPlans: List<ContentDatum>): Boolean {
        var tve = false
        for (i in subscriptionPlans.indices) {
            if (subscriptionPlans[i].planMonetizationModel != null &&
                    subscriptionPlans[i].planMonetizationModel.equals(context.getString(R.string.pricing_model_TVE), ignoreCase = true))
                tve = true
            else
                return false
        }
        return tve
    }

    /** Finds position of TVOD plan in the list
     * @param subscriptionPlans - list of plans
     * @return position of TVOD plan
     * */
    private fun tvodPlanPos(subscriptionPlans: List<ContentDatum>): Int {
        return subscriptionPlans.indexOf(subscriptionPlans.find { it.planMonetizationModel.equals(context.getString(R.string.pricing_model_TVOD), ignoreCase = true) })
    }

    /** Checks if purchase is enabled on TVOD plan
     * @param subscriptionPlans - list of plans
     * @return true if condition matched
     * */
    fun purchaseEnabledTvod(subscriptionPlans: List<ContentDatum>): Boolean {
        val position = tvodPlanPos(subscriptionPlans)
        if (position == -1)
            return false
        if (subscriptionPlans[position].planDetails != null) {
            var planIndex = subscriptionPlans[position].planDetails.indexOf(subscriptionPlans[position].planDetails.find {
                it.isDefault
            })
            if (planIndex == -1)
                planIndex = 0
            return subscriptionPlans[position].planDetails[planIndex].isPurchaseEnabled
        }
        return false
    }

    /** Checks if rent is enabled on TVOD plan
     * @param subscriptionPlans - list of plans
     * @return true if condition matched
     * */
    fun rentEnabledTvod(subscriptionPlans: List<ContentDatum>): Boolean {
        val position = tvodPlanPos(subscriptionPlans)
        if (position == -1)
            return false
        if (subscriptionPlans[position].planDetails != null) {
            var planIndex = subscriptionPlans[position].planDetails.indexOf(subscriptionPlans[position].planDetails.find {
                it.isDefault
            })
            if (planIndex == -1)
                planIndex = 0
            return subscriptionPlans[position].planDetails[planIndex].isRentEnabled
        }
        return false
    }

    /** Get TVOD plan from list
     * @param subscriptionPlans - list of plans
     * @return TVOD plan object
     * */
    fun tvodPlan(subscriptionPlans: List<ContentDatum>): ContentDatum? {
        if (tvodPlanPos(subscriptionPlans) == -1)
            return null
        else
            return subscriptionPlans[tvodPlanPos(subscriptionPlans)]
    }

    /** Checks whether SVOD model exist
     * @param monetizationModels - list of monetizationModels
     * @return true if condition matches
     * */
    private fun isContentSvodPlanSeries(monetizationModels: List<MonetizationModels>): Boolean {
        return monetizationModels.any { it.type.equals(context.getString(R.string.pricing_model_SVOD), ignoreCase = true) }
    }

    /** Checks whether content is purchased
     * @param userPurchases - user purchases to fetched from AppPrefrence
     * @param contentId - content to check seriesId/SeasonId/VideoId
     * @return true if condition matches
     * */
    fun isContentPurchased(userPurchases: String?, contentId: String): Boolean {
        if (userPurchases == null || userPurchases.isBlank()) return false
        val purchase: List<UserPurchases> = GsonBuilder().create().fromJson(userPurchases, Array<UserPurchases>::class.java).toList()
        val purchased = purchase.any { it.contentId.equals(contentId, ignoreCase = true) }
        return purchased
    }

    /** Checks whether all seasons from series are purchased
     * @param userPurchases - user purchases to fetched from AppPrefrence
     * @param seasons - list of season
     * @return true if condition matches
     * */
    fun isAllSeasonsPurchased(userPurchases: String?, seasons: List<Season_>): Boolean {
        if (userPurchases == null || userPurchases.isBlank()) return false
        val purchase: List<UserPurchases> = GsonBuilder().create().fromJson(userPurchases, Array<UserPurchases>::class.java).toList()
        var seasonPurchase = 0
        for (i in seasons.indices) {
            for (j in purchase.indices) {
                if (purchase[j].contentId.equals(seasons[i].id, ignoreCase = true)) {
                    seasonPurchase++
                }
            }
        }
        return seasonPurchase == seasons.size
    }

    /** Checks whether all episodes from a series is purchased
     * @param userPurchases - user purchases to fetched from AppPrefrence
     * @param seasons - list of seasons
     * @return true if condition matches
     * */
    fun isAllEpisodesPurchased(userPurchases: String?, seasons: List<Season_>): Boolean {
        if (userPurchases == null || userPurchases.isBlank()) return false
        val purchase: List<UserPurchases> = GsonBuilder().create().fromJson(userPurchases, Array<UserPurchases>::class.java).toList()
        var toalEpisodes = 0
        for (i in seasons.indices) {
            toalEpisodes = toalEpisodes + seasons[i].episodes.size
        }
        var episodePurchase = 0
        for (i in seasons.indices) {
            for (j in seasons[i].episodes.indices) {
                for (k in purchase.indices) {
                    if (purchase[k].contentId.equals(seasons[i].episodes[j].gist.id, ignoreCase = true) ||
                            isContentFREEMonetization(seasons[i].episodes[j].monetizationModels) ||
                            isContentAVODMonetization(seasons[i].episodes[j].monetizationModels)) {
                        episodePurchase++
                    }
                }
            }
        }
        return episodePurchase == toalEpisodes
    }

    /** Checks whether bundle is purchased
     * @param userPurchases - user purchases to fetched from AppPrefrence
     * @param bundleList - list of bundle videos
     * @param bundleId - id of bundle
     * @return true if condition matches
     * */
    fun isBundlePurchased(userPurchases: String?, bundleList: List<ContentDatum>, bundleId: String): Boolean {
        if (userPurchases == null || userPurchases.isBlank()) return false
        val purchase: List<UserPurchases> = GsonBuilder().create().fromJson(userPurchases, Array<UserPurchases>::class.java).toList()
        val purchased = purchase.any { it.contentId.equals(bundleId, ignoreCase = true) }
        if (purchased)
            return true
        var bundlePurchase = 0
        for (i in bundleList.indices) {
            for (j in purchase.indices) {
                if (purchase[j].contentId.equals(bundleList[i].gist.id, ignoreCase = true)) {
                    bundlePurchase++
                }
                if (bundlePurchase == bundleList.size)
                    break
            }
        }
        return bundlePurchase == bundleList.size
    }

    /** Checks whether content is rented by user
     * @param userPurchases - user purchases to fetched from AppPrefrence
     * @param contentId - content to check seriesId/SeasonId/VideoId
     * @return true if condition matches
     * */
    fun isContentRentedByUser(userPurchases: String?, contentId: String): Boolean {
        if (userPurchases == null || userPurchases.isBlank()) return false
        val purchase: List<UserPurchases> = GsonBuilder().create().fromJson(userPurchases, Array<UserPurchases>::class.java).toList()
        val rented = purchase.any { it.contentId.equals(contentId, ignoreCase = true) && it.purchaseType.equals(context.resources.getStringArray(R.array.purchase_type)[1]) }
        return rented
    }

    /** Checks whether content is purchased by user
     * @param userPurchases - user purchases to fetched from AppPrefrence
     * @param contentId - content to check seriesId/SeasonId/VideoId
     * @return true if condition matches
     * */
    fun isContentPurchasedByUser(userPurchases: String?, contentId: String): Boolean {
        if (userPurchases == null || userPurchases.isBlank()) return false
        val purchase: List<UserPurchases> = GsonBuilder().create().fromJson(userPurchases, Array<UserPurchases>::class.java).toList()
        val rented = purchase.any { it.contentId.equals(contentId, ignoreCase = true) && it.purchaseType.equals(context.resources.getStringArray(R.array.purchase_type)[0]) }
        return rented
    }

    /** Get rent expiry date for a rented content
     * @param userPurchases - user purchases to fetched from AppPrefrence
     * @param contentId - content to check seriesId/SeasonId/VideoId
     * @return rent end date
     * */
    fun getVideoRentEndDate(userPurchases: String?, contentId: String): Long {
        if (userPurchases == null || userPurchases.isBlank()) return 0
        val purchase: List<UserPurchases> = GsonBuilder().create().fromJson(userPurchases, Array<UserPurchases>::class.java).toList()
        val puchasedItem = purchase.find { it.contentId.equals(contentId, ignoreCase = true) }
        if (puchasedItem != null) {
            return puchasedItem.rentEndDate
        }
        return 0
    }

    /** Get content plans id's
     * @param contentPlans - list of plans associated with content
     * @param type - can be SVOD/TVOD
     * @return  plans id's
     * */
    fun contentPlansId(contentPlans: List<ContentPlans>, type: String): List<String> {
        for (i in contentPlans.indices) {
            if (contentPlans[i].type.equals(type, ignoreCase = true)) return contentPlans[i].id
        }
        return contentPlans[0].id
    }

    /** Checks if plan exist
     * @param monetizationModels - list of monetizationModels
     * @param planType - can be SVOD/TVOD
     * @return true if condition matches
     * */
    fun checkPlanExist(monetizationModels: List<MonetizationModels>?, planType: String?): Boolean {
        if (monetizationModels != null) {
            return monetizationModels.any { it.type.equals(planType, ignoreCase = true) }
        }
        return false
    }

    /** Check if plans are available at series/season/episode level
     * @param series - series data
     * @return true if condition matches
     * */
    fun isPlansAvailableForSeriesOrSeasonOrEpisode(series: ContentDatum): Boolean {
        if (series.seriesPlans != null) return true else if (series.season != null) {
            return isPlansAvailableForSeasonOrEpisode(series.season)
        }
        return false
    }

    /** Check if plans are available at season/episode level
     * @param season - list of season
     * @return true if condition matches
     * */
    private fun isPlansAvailableForSeasonOrEpisode(season: List<Season_>): Boolean {
        if (season.size > 0) {
            for (i in season.indices) {
                if (season[i].seasonPlans != null)
                    return true
                else {
                    if (isPlansAvialableForEpisode(season[i].episodes))
                        return true
                }
            }
        }
        return false
    }

    /** Check if plans are available at episode level
     * @param episodes - list of episodes
     * @return true if condition matches
     * */
    private fun isPlansAvialableForEpisode(episodes: List<ContentDatum>): Boolean {
        if (episodes.size > 0) {
            for (i in episodes.indices) {
                if (episodes[i].episodePlans != null) return true
            }
        }
        return false
    }

    /** Check if monetizationModel is TVOD
     * @param monetizationModels - list of monetizationModels
     * @return true if condition matches
     * */
    fun isContentTVODMonetization(monetizationModels: List<MonetizationModels>): Boolean {
        return monetizationModels.any { it.type.equals(context.getString(R.string.pricing_model_TVOD), ignoreCase = true) }
    }

    /** Check if monetizationModel is SVOD
     * @param monetizationModels - list of monetizationModels
     * @return true if condition matches
     * */
    fun isContentSVODMonetization(monetizationModels: List<MonetizationModels>): Boolean {
        return monetizationModels.any { it.type.equals(context.getString(R.string.pricing_model_SVOD), ignoreCase = true) }
    }

    /** Check if monetizationModel is TVE
     * @param monetizationModels - list of monetizationModels
     * @return true if condition matches
     * */
    fun isContentTVEMonetization(monetizationModels: List<MonetizationModels>): Boolean {
        return monetizationModels.any { it.type.equals(context.getString(R.string.pricing_model_TVE), ignoreCase = true) }
    }

    /** Check if monetizationModel is FREE
     * @param monetizationModels - list of monetizationModels
     * @return true if condition matches
     * */
    fun isContentFREEMonetization(monetizationModels: List<MonetizationModels>): Boolean {
        return monetizationModels.any { it.type.equals(context.getString(R.string.pricing_model_FREE), ignoreCase = true) }
    }

    /** Check if monetizationModel is AVOD
     * @param monetizationModels - list of monetizationModels
     * @return true if condition matches
     * */
    fun isContentAVODMonetization(monetizationModels: List<MonetizationModels>): Boolean {
        return monetizationModels.any { it.type.equals(context.getString(R.string.pricing_model_AVOD), ignoreCase = true) }
    }

    @Deprecated("Use isContentAVODMonetization")
    fun isAllContentAVODMonetization(monetizationModels: List<MonetizationModels>): Boolean {
        var avod = false
        for (i in monetizationModels.indices) {
            if (monetizationModels[i].type != null &&
                    monetizationModels[i].type.equals(context.getString(R.string.pricing_model_AVOD), ignoreCase = true))
                avod = true
            else
                return false
        }
        return avod
    }

    /** Check if purchase is enabled at series/season/episode level
     * @param series - series data
     * @return true if condition matches
     * */
    fun isPurchaseAvailableForSeriesOrSeasonOrEpisode(series: ContentDatum): Boolean {
        var purchaseEnabled = false;
        if (series.monetizationModels != null) {
            if (isContentTVODMonetization(series.monetizationModels) && series.seriesPlans != null) {
                purchaseEnabled = purchaseEnabledTvod(series.seriesPlans)
            }
        }

        if (!purchaseEnabled && series.season != null && series.season.size != 0) {
            return isPurchaseAvailableForSeasonOrEpisode(series.season)
        }

        return purchaseEnabled
    }

    /** Check if purchase is enabled at season/episode level
     * @param season - list of seasons
     * @return true if condition matches
     * */
    private fun isPurchaseAvailableForSeasonOrEpisode(season: List<Season_>): Boolean {
        for (i in season.indices) {
            if (season[i].monetizationModels != null && isContentTVODMonetization(season[i].monetizationModels)) {
                return purchaseEnabledTvod(season[i].seasonPlans)
            } else {
                if (isPurchaseAvailableForEpisode(season[i].episodes))
                    return true
            }
        }
        return false
    }

    /** Check if purchase is enabled at episode level
     * @param episode - list of episodes
     * @return true if condition matches
     * */
    private fun isPurchaseAvailableForEpisode(episode: List<ContentDatum>): Boolean {
        for (i in episode.indices) {
            if (episode[i].monetizationModels != null && isContentTVODMonetization(episode[i].monetizationModels)) {
                if (purchaseEnabledTvod(episode[i].episodePlans))
                    return true
            }
        }
        return false
    }

    /** Check if rent is enabled at series/season/episode level
     * @param series - series data
     * @return true if condition matches
     * */
    fun isRentAvailableForSeriesOrSeasonOrEpisode(series: ContentDatum): Boolean {
        var rentEnabled = false;
        if (series.monetizationModels != null) {
            if (isContentTVODMonetization(series.monetizationModels) && series.seriesPlans != null && !series.seriesPlans.isEmpty()) {
                rentEnabled = rentEnabledTvod(series.seriesPlans)
            }
        }
        if (!rentEnabled && series.season != null && series.season.size != 0) {
            return isRentAvailableForSeasonOrEpisode(series.season)
        }
        return rentEnabled
    }

    /** Check if rent is enabled at season/episode level
     * @param season - list of seasons
     * @return true if condition matches
     * */
    private fun isRentAvailableForSeasonOrEpisode(season: List<Season_>): Boolean {
        for (i in season.indices) {
            if (season[i].monetizationModels != null && isContentTVODMonetization(season[i].monetizationModels)) {
                return rentEnabledTvod(season[i].seasonPlans)
            } else {
                if (isRentAvailableForEpisode(season[i].episodes))
                    return true
            }
        }
        return false
    }

    /** Check if rent is enabled at episode level
     * @param episode - list of episodes
     * @return true if condition matches
     * */
    private fun isRentAvailableForEpisode(episode: List<ContentDatum>): Boolean {
        for (i in episode.indices) {
            if (episode[i].monetizationModels != null && isContentTVODMonetization(episode[i].monetizationModels)) {
                if (episode[i].episodePlans != null && rentEnabledTvod(episode[i].episodePlans))
                    return true
            }
        }
        return false
    }

    /** Get plan price for TVOD
     * @param subscriptionPlans - list of plans
     * @param planType - can be buy/rent
     * @return plan rent/buy price
     * */
    fun fetchPlanPrice(subscriptionPlans: List<ContentDatum>?, planType: String): String? {
        var planPrice = 0F
        var planPriceWithCurrencyCode: String? = null
        if (subscriptionPlans != null && subscriptionPlans.size > 0) {
            if (isContentFree(subscriptionPlans) || isContentAVOD(subscriptionPlans)) {
                planPriceWithCurrencyCode = "Free";
            } else {
                val contentDatum = tvodPlan(subscriptionPlans)
                if (contentDatum != null && contentDatum.planDetails != null && contentDatum.planDetails.size > 0) {
                    var planIndex = contentDatum.planDetails.indexOf(contentDatum.planDetails.find {
                        it.isDefault
                    })
                    if (planIndex == -1) planIndex = 0;
                    if (planType.trim { it <= ' ' }.contains(context.getString(R.string.buy), ignoreCase = true)) {
                        planPrice = contentDatum.planDetails[planIndex].purchaseAmount
                    } else if (planType.trim { it <= ' ' }.contains(context.getString(R.string.rent), ignoreCase = true)) {
                        planPrice = contentDatum.planDetails[planIndex].rentAmount
                    }
                    if (planPrice != 0F) {
                        planPriceWithCurrencyCode = Currency.getInstance(contentDatum.planDetails[planIndex].recurringPaymentCurrencyCode).symbol.plus(" ").plus(planPrice)
                    }
                }
            }

        }
        return planPriceWithCurrencyCode ?: "N/A"
    }

    /** Get plan description
     * @param subscriptionPlans - list of plans
     * @return description
     * */
    fun fetchPlanDescription(subscriptionPlans: List<ContentDatum>?): String? {
        var description: String? = null
        if (subscriptionPlans != null && subscriptionPlans.size > 0) {
            val contentDatum = tvodPlan(subscriptionPlans)
            if (contentDatum != null && contentDatum.planDetails != null && contentDatum.planDetails.size > 0) {
                var planIndex = contentDatum.planDetails.indexOf(contentDatum.planDetails.find {
                    it.isDefault
                })
                if (planIndex == -1)
                    planIndex = 0
                description = contentDatum.planDetails[planIndex].description
            }
        }
        return description
    }

    /** Get plan purchase price for TVOD
     * @param data - plan object
     * @return plan purchase price
     * */
    fun fetchTvodBuyPrice(data: ContentDatum): String? {
        var planIndex = data.planDetails.indexOf(data.planDetails.find {
            it.isDefault
        })
        if (planIndex == -1)
            planIndex = 0
        val currency = Currency.getInstance(data.planDetails[planIndex].recurringPaymentCurrencyCode)
        if (data.planDetails[planIndex].purchaseAmount == 0F)
            return null
        return currency.symbol.plus(" ").plus("%.2f".format(data.planDetails[planIndex].purchaseAmount))
    }

    /** Get plan purchase numeric price for TVOD
     * @param data - plan object
     * @return plan purchase price
     * */
    fun fetchTvodBuyNumericPrice(data: ContentDatum): Float {
        var planIndex = data.planDetails.indexOf(data.planDetails.find {
            it.isDefault
        })
        if (planIndex == -1)
            planIndex = 0
        return data.planDetails[planIndex].purchaseAmount
    }

    /** Get plan rent price for TVOD
     * @param data - plan object
     * @return plan rent price
     * */
    fun fetchTvodRentPrice(data: ContentDatum): String? {
        var planIndex = data.planDetails.indexOf(data.planDetails.find {
            it.isDefault
        })
        if (planIndex == -1)
            planIndex = 0
        val currency = Currency.getInstance(data.planDetails[planIndex].recurringPaymentCurrencyCode)
        if (data.planDetails[planIndex].rentAmount == 0F)
            return null
        return currency.symbol.plus(" ").plus("%.2f".format(data.planDetails[planIndex].rentAmount))
    }

    /** Get plan rent numeric price for TVOD
     * @param data - plan object
     * @return plan rent price
     * */
    fun fetchTvodRentNumericPrice(data: ContentDatum): Float {
        var planIndex = data.planDetails.indexOf(data.planDetails.find {
            it.isDefault
        })
        if (planIndex == -1)
            planIndex = 0

        return data.planDetails[planIndex].rentAmount
    }

    fun fetchCurrency(data: ContentDatum): String? {
        var planIndex = data.planDetails.indexOf(data.planDetails.find {
            it.isDefault
        })
        if (planIndex == -1)
            planIndex = 0
        return data.planDetails[planIndex].recurringPaymentCurrencyCode
    }

    /** Check if video is to be allowed to play on device
     * @param allowedDevices - list of allowedDevices
     * @return true if condition matches
     * */
    fun isContentConsumptionAndroid(allowedDevices: List<String>): Boolean {
        return allowedDevices.any { it.equals("android") }
    }

    /** Check if video is to be allowed to play on device
     * @param allowedDevices - list of allowedDevices
     * @return true if condition matches
     * */
    fun isContentConsumptionFireTV(allowedDevices: List<String>): Boolean {
        return allowedDevices.any { it.equals("fireTv") }
    }

}