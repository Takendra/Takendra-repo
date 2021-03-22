package com.viewlift.views.dialog

import android.graphics.Color
import android.text.style.ClickableSpan
import android.view.View
import android.widget.Toast
import androidx.appcompat.widget.*
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.constraintlayout.widget.Guideline
import androidx.core.content.ContextCompat
import butterknife.BindView
import butterknife.ButterKnife
import butterknife.OnClick
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.viewlift.CMSColorUtils
import com.viewlift.R
import com.viewlift.extensions.gone
import com.viewlift.extensions.visible
import com.viewlift.models.billing.appcms.purchase.TvodPurchaseData
import com.viewlift.models.data.appcms.api.ContentDatum
import com.viewlift.models.data.appcms.api.MonetizationModels
import com.viewlift.models.data.appcms.ui.page.Component
import com.viewlift.presenters.AppCMSPresenter
import com.viewlift.presenters.AppCMSPresenter.EntitlementPendingVideoData
import com.viewlift.utils.ContentTypeChecker
import com.viewlift.views.activity.AppCMSPageActivity
import com.viewlift.views.customviews.ViewCreator
import com.viewlift.views.rxbus.SeasonTabSelectorBus
import io.reactivex.rxjava3.core.Observer
import io.reactivex.rxjava3.disposables.Disposable
import java.util.*

/**
 * This class is responsible to show the bottom overlay for price options attached to content - video, series, bundle
 * @author  Wishy
 * @since   2020-07-10
 */

class WaysToWatchBottom(val appCMSPresenter: AppCMSPresenter, val videoContentDatum: ContentDatum) {
    private lateinit var contentTypeChecker: ContentTypeChecker
    private var seriesPurchase = false;
    private var bundlePurchase = false;
    private var rent = false;
    private var seasonCounter = 0;
    private var episodeCounter = 0;
    private var videoCounter = 0;
    private var sheetBehavior: BottomSheetBehavior<*>? = null

    @BindView(R.id.parentLayout)
    lateinit var parentLayout: ConstraintLayout

    @BindView(R.id.title)
    lateinit var title: AppCompatTextView

    @BindView(R.id.description)
    lateinit var description: AppCompatTextView

    @BindView(R.id.alreadyLogin)
    lateinit var alreadyLogin: AppCompatTextView

    @BindView(R.id.termsView)
    lateinit var termsView: AppCompatTextView

    @BindView(R.id.backButton)
    lateinit var backButton: AppCompatTextView

    @BindView(R.id.backButton2)
    lateinit var backButton2: AppCompatTextView

    @BindView(R.id.episodeNum)
    lateinit var episodeNum: AppCompatTextView

    @BindView(R.id.episodeName)
    lateinit var episodeName: AppCompatTextView

    @BindView(R.id.episodePrice)
    lateinit var episodePrice: AppCompatTextView

    @BindView(R.id.seasonNum)
    lateinit var seasonNum: AppCompatTextView

    @BindView(R.id.seasonName)
    lateinit var seasonName: AppCompatTextView

    @BindView(R.id.seasonPrice)
    lateinit var seasonPrice: AppCompatTextView

    @BindView(R.id.seriesName)
    lateinit var seriesName: AppCompatTextView

    @BindView(R.id.seriesTitle)
    lateinit var seriesTitle: AppCompatTextView

    @BindView(R.id.seriesPrice)
    lateinit var seriesPrice: AppCompatTextView

    @BindView(R.id.entitlementButton1)
    lateinit var entitlementButton1: AppCompatButton

    @BindView(R.id.entitlementButton2)
    lateinit var entitlementButton2: AppCompatButton

    @BindView(R.id.entitlementButton3)
    lateinit var entitlementButton3: AppCompatButton

    @BindView(R.id.entitlementButton4)
    lateinit var entitlementButton4: AppCompatButton

    @BindView(R.id.verticalGuideline)
    lateinit var verticalGuideline: Guideline

    @BindView(R.id.verticalGuidelineTvod)
    lateinit var verticalGuidelineTvod: Guideline

    @BindView(R.id.episodeInfo)
    lateinit var episodeInfo: AppCompatImageView

    @BindView(R.id.seasonInfo)
    lateinit var seasonInfo: AppCompatImageView

    @BindView(R.id.seriesInfo)
    lateinit var seriesInfo: AppCompatImageView

    @BindView(R.id.episodeUp)
    lateinit var episodeUp: AppCompatImageButton

    @BindView(R.id.episodeDown)
    lateinit var episodeDown: AppCompatImageButton

    @BindView(R.id.seasonUp)
    lateinit var seasonUp: AppCompatImageButton

    @BindView(R.id.seasonDown)
    lateinit var seasonDown: AppCompatImageButton

    @BindView(R.id.tveSvodButtons)
    lateinit var tveSvodButtons: ConstraintLayout

    @BindView(R.id.tvodButtons)
    lateinit var tvodButtons: ConstraintLayout

    @BindView(R.id.tvodEpisodePurchase)
    lateinit var tvodEpisodePurchase: ConstraintLayout

    @BindView(R.id.episodeContainer)
    lateinit var episodeContainer: ConstraintLayout

    @BindView(R.id.episodeChooser)
    lateinit var episodeChooser: ConstraintLayout

    @BindView(R.id.videoChooser)
    lateinit var videoChooser: ConstraintLayout

    @BindView(R.id.tvodSeasonPurchase)
    lateinit var tvodSeasonPurchase: ConstraintLayout

    @BindView(R.id.seriesContainer)
    lateinit var seriesContainer: ConstraintLayout

    @BindView(R.id.seasonContainer)
    lateinit var seasonContainer: ConstraintLayout

    @BindView(R.id.seasonChooser)
    lateinit var seasonChooser: ConstraintLayout

    @BindView(R.id.tvodSeriesPurchase)
    lateinit var tvodSeriesPurchase: ConstraintLayout

    @BindView(R.id.tvodPurchaseOptions)
    lateinit var tvodPurchaseOptions: ConstraintLayout

    @BindView(R.id.waysToWatchOptions)
    lateinit var waysToWatchOptions: ConstraintLayout

    @BindView(R.id.bundlePurchaseOptions)
    lateinit var bundlePurchaseOptions: ConstraintLayout

    @BindView(R.id.videoPurchase)
    lateinit var videoPurchase: ConstraintLayout

    @BindView(R.id.tvodBundlePurchase)
    lateinit var tvodBundlePurchase: ConstraintLayout

    @BindView(R.id.videoNum)
    lateinit var videoNum: AppCompatTextView

    @BindView(R.id.videoName)
    lateinit var videoName: AppCompatTextView

    @BindView(R.id.bundleTitle)
    lateinit var bundleTitle: AppCompatTextView

    @BindView(R.id.bundleName)
    lateinit var bundleName: AppCompatTextView

    @BindView(R.id.bundlePrice)
    lateinit var bundlePrice: AppCompatTextView

    @BindView(R.id.videoUp)
    lateinit var videoUp: AppCompatImageButton

    @BindView(R.id.videoDown)
    lateinit var videoDown: AppCompatImageButton

    @BindView(R.id.seriesInformation)
    lateinit var seriesInformation: ConstraintLayout

    @BindView(R.id.seriesInformationBackButton)
    lateinit var seriesInformationBackButton: AppCompatTextView

    @BindView(R.id.seriesPriceWithType)
    lateinit var seriesPriceWithType: AppCompatTextView

    @BindView(R.id.seriesPriceWithTypeUnderline)
    lateinit var seriesPriceWithTypeUnderline: View

    @BindView(R.id.seriesIncludeTitle)
    lateinit var seriesIncludeTitle: AppCompatTextView

    @BindView(R.id.seasonTitleWithEpisodesLayout)
    lateinit var seasonTitleWithEpisodesLayout: LinearLayoutCompat


    init {
        if (appCMSPresenter.currentActivity is AppCMSPageActivity) {
            val sheetLayout: ConstraintLayout = appCMSPresenter.currentActivity.findViewById(R.id.parentLayoutSheet)
            ButterKnife.bind(this, sheetLayout)
            sheetBehavior = BottomSheetBehavior.from(sheetLayout)
            contentTypeChecker = ContentTypeChecker(appCMSPresenter.currentContext)
            seriesPurchase = false;
            bundlePurchase = false;
            rent = false;
            resetViews();
            setViewStyles()
            setData()
            if (videoContentDatum.moduleApi != null) {
                if (videoContentDatum.moduleApi.moduleType != null && videoContentDatum.moduleApi.moduleType.equals("BundleDetailModule", true)) {
                    if (isVisible())
                        removeSheet()
                    bundlePurchase = true
                    bundlePurchaseOptions.gone()
                    tvodPurchaseOptions.gone()
                    waysToWatchOptions.visible()
                    videoCounter = 0
                    videoDown.setColorFilter(ContextCompat.getColor(appCMSPresenter.currentContext, android.R.color.darker_gray))
                    videoUp.setColorFilter(Color.parseColor(appCMSPresenter.appTextColor))
                    if (videoContentDatum.moduleApi.contentData[0].monetizationModels != null)
                        handleButtonVisibilityForBundle(videoContentDatum.moduleApi.contentData[0].monetizationModels)

                } else if ((videoContentDatum.moduleApi.moduleType != null && videoContentDatum.moduleApi.moduleType.equals("ShowDetailModule", true))
                        || contentTypeChecker.isPlansAvailableForSeriesOrSeasonOrEpisode(videoContentDatum.moduleApi.contentData[0])) {
                    bundlePurchaseOptions.gone()
                    tvodPurchaseOptions.gone()
                    waysToWatchOptions.visible()
                    seriesPurchase = true
                    resetSeasonEpisodeCounter()
                    if (videoContentDatum.isFromEntitlement || appCMSPresenter.isWaysToWatchFromEntitlement)
                        handleButtonVisibilityForSeriesEntitlement(videoContentDatum.moduleApi.contentData[0].monetizationModels)
                    else
                        handleButtonVisibilityForSeries(videoContentDatum.moduleApi.contentData[0].monetizationModels)

                    SeasonTabSelectorBus.instanceOf().selectedTab.subscribe(object : Observer<List<ContentDatum?>> {
                        override fun onSubscribe(d: Disposable) {
                        }

                        override fun onNext(episodes: List<ContentDatum?>) {
                            findSeasonNum(episodes)
                        }

                        override fun onError(e: Throwable) {}
                        override fun onComplete() {}
                    })


                } else if (videoContentDatum.isFromEntitlement || appCMSPresenter.isWaysToWatchFromEntitlement) {
                    handleButtonVisibilityEntitlement()
                } else
                    handleButtonVisibility()
                if (!videoContentDatum.isFromEntitlement) {
                    videoContentDatum.isFromStandalone = false
                    val entitlementPendingVideoData = EntitlementPendingVideoData.Builder()
                            .contentDatum(videoContentDatum).isFromContent(true)
                            .build()
                    appCMSPresenter.setEntitlementPendingVideoData(entitlementPendingVideoData)
                }
            } else if (videoContentDatum.isFromEntitlement) {
                handleButtonVisibilityEntitlement()
            }
        }
    }


    private fun findSeasonNum(episodes: List<ContentDatum?>) {
        for (i in videoContentDatum.moduleApi.contentData[0].season.indices) {
            val season = videoContentDatum.moduleApi.contentData[0].season[i]
            if (season.episodes[0].gist.id.equals(episodes[0]?.gist?.id)) {
                seasonCounter = i
                episodeCounter = 0
                setPriceForSeries(videoContentDatum.moduleApi.contentData[0])
                if (seasonCounter == 0) {
                    seasonDown.setColorFilter(CMSColorUtils.lightenColor(Color.parseColor(appCMSPresenter.appCtaTextColor)))
                    seasonUp.setColorFilter(Color.parseColor(appCMSPresenter.appCtaTextColor))
                } else if (seasonCounter == videoContentDatum.moduleApi.contentData[0].season.size - 1) {
                    seasonUp.setColorFilter(CMSColorUtils.lightenColor(Color.parseColor(appCMSPresenter.appCtaTextColor)))
                    seasonDown.setColorFilter(Color.parseColor(appCMSPresenter.appCtaTextColor))
                } else {
                    seasonUp.setColorFilter(Color.parseColor(appCMSPresenter.appCtaTextColor))
                    seasonDown.setColorFilter(Color.parseColor(appCMSPresenter.appCtaTextColor))
                }
                episodeDown.setColorFilter(CMSColorUtils.lightenColor(Color.parseColor(appCMSPresenter.appCtaTextColor)))
                episodeUp.setColorFilter(Color.parseColor(appCMSPresenter.appCtaTextColor))
                break
            }

        }
    }

    private fun setViewStyles() {
        parentLayout.setBackgroundColor(ViewCreator.getTransparentColor(appCMSPresenter.generalBackgroundColor, 0.8f))
        title.setTextColor(appCMSPresenter.generalTextColor)
        description.setTextColor(appCMSPresenter.generalTextColor)
        alreadyLogin.setTextColor(appCMSPresenter.generalTextColor)
        alreadyLogin.setLinkTextColor(appCMSPresenter.brandPrimaryCtaColor)
        backButton.setTextColor(appCMSPresenter.generalTextColor)
        backButton2.setTextColor(appCMSPresenter.generalTextColor)
        seriesInformationBackButton.setTextColor(appCMSPresenter.generalTextColor)
        backButton.getCompoundDrawables()[0].setTint(appCMSPresenter.generalTextColor)
        backButton2.getCompoundDrawables()[0].setTint(appCMSPresenter.generalTextColor)
        seriesInformationBackButton.getCompoundDrawables()[0].setTint(appCMSPresenter.generalTextColor)
        episodeName.setTextColor(Color.parseColor(appCMSPresenter.appCtaTextColor))
        episodePrice.setTextColor(Color.parseColor(appCMSPresenter.appCtaTextColor))
        seasonName.setTextColor(Color.parseColor(appCMSPresenter.appCtaTextColor))
        seasonPrice.setTextColor(Color.parseColor(appCMSPresenter.appCtaTextColor))
        seriesName.setTextColor(Color.parseColor(appCMSPresenter.appCtaTextColor))
        seriesPrice.setTextColor(Color.parseColor(appCMSPresenter.appCtaTextColor))
        seriesTitle.setTextColor(Color.parseColor(appCMSPresenter.appCtaTextColor))
        seasonNum.setTextColor(Color.parseColor(appCMSPresenter.appCtaTextColor))
        episodeNum.setTextColor(Color.parseColor(appCMSPresenter.appCtaTextColor))
        videoNum.setTextColor(Color.parseColor(appCMSPresenter.appCtaTextColor))
        videoName.setTextColor(Color.parseColor(appCMSPresenter.appCtaTextColor))
        bundleTitle.setTextColor(Color.parseColor(appCMSPresenter.appCtaTextColor))
        bundleName.setTextColor(Color.parseColor(appCMSPresenter.appCtaTextColor))
        bundlePrice.setTextColor(Color.parseColor(appCMSPresenter.appCtaTextColor))
        seriesPriceWithType.setTextColor(appCMSPresenter.generalTextColor)
        seriesPriceWithTypeUnderline.setBackgroundColor(appCMSPresenter.generalTextColor)
        seriesIncludeTitle.setTextColor(appCMSPresenter.generalTextColor)

        backButton.text = appCMSPresenter.localisedStrings.backCta
        backButton2.text = appCMSPresenter.localisedStrings.backCta
        seriesInformationBackButton.text = appCMSPresenter.localisedStrings.backCta
        seriesIncludeTitle.text = appCMSPresenter.localisedStrings.seriesInclude

        entitlementButton1.setBackground(CustomShape.createRoundedRectangleDrawable(appCMSPresenter.brandPrimaryCtaColor))
        entitlementButton2.setBackground(CustomShape.createRoundedRectangleDrawable(appCMSPresenter.brandPrimaryCtaColor))
        entitlementButton3.setBackground(CustomShape.createRoundedRectangleDrawable(appCMSPresenter.brandPrimaryCtaColor))
        entitlementButton4.setBackground(CustomShape.createRoundedRectangleDrawable(appCMSPresenter.brandPrimaryCtaColor))
        tvodEpisodePurchase.setBackground(CustomShape.createRoundedRectangleDrawable(appCMSPresenter.brandPrimaryCtaColor))
        tvodSeasonPurchase.setBackground(CustomShape.createRoundedRectangleDrawable(appCMSPresenter.brandPrimaryCtaColor))
        tvodSeriesPurchase.setBackground(CustomShape.createRoundedRectangleDrawable(appCMSPresenter.brandPrimaryCtaColor))
        entitlementButton1.setTextColor(Color.parseColor(appCMSPresenter.appCtaTextColor))
        entitlementButton2.setTextColor(Color.parseColor(appCMSPresenter.appCtaTextColor))
        entitlementButton3.setTextColor(Color.parseColor(appCMSPresenter.appCtaTextColor))
        entitlementButton4.setTextColor(Color.parseColor(appCMSPresenter.appCtaTextColor))
        episodeUp.setColorFilter(Color.parseColor(appCMSPresenter.appCtaTextColor))
        episodeDown.setColorFilter(Color.parseColor(appCMSPresenter.appCtaTextColor))
        seasonUp.setColorFilter(Color.parseColor(appCMSPresenter.appCtaTextColor))
        seasonDown.setColorFilter(Color.parseColor(appCMSPresenter.appCtaTextColor))
        seriesInfo.setColorFilter(Color.parseColor(appCMSPresenter.appTextColor))
        seasonInfo.setColorFilter(Color.parseColor(appCMSPresenter.appTextColor))
        episodeInfo.setColorFilter(Color.parseColor(appCMSPresenter.appTextColor))
        seasonChooser.setBackground(CustomShape.createLeftSideRoundedRectangleDrawable(CMSColorUtils.darkenColor(appCMSPresenter.brandPrimaryCtaColor)))
        episodeChooser.setBackground(CustomShape.createLeftSideRoundedRectangleDrawable(CMSColorUtils.darkenColor(appCMSPresenter.brandPrimaryCtaColor)))
        videoChooser.setBackground(CustomShape.createLeftSideRoundedRectangleDrawable(CMSColorUtils.darkenColor(appCMSPresenter.brandPrimaryCtaColor)))
        videoPurchase.setBackground(CustomShape.createRoundedRectangleDrawable(appCMSPresenter.brandPrimaryCtaColor))
        tvodBundlePurchase.setBackground(CustomShape.createRoundedRectangleDrawable(appCMSPresenter.brandPrimaryCtaColor))
        videoUp.setColorFilter(Color.parseColor(appCMSPresenter.appTextColor))
        videoDown.setColorFilter(Color.parseColor(appCMSPresenter.appTextColor))
        termsView.setTextColor(Color.parseColor(appCMSPresenter.appTextColor))
        termsView.setLinkTextColor(Color.parseColor(appCMSPresenter.appTextColor))
        val component = Component()
        ViewCreator.setTypeFace(parentLayout.context, appCMSPresenter, appCMSPresenter.jsonValueKeyMap, component, title)
        ViewCreator.setTypeFace(parentLayout.context, appCMSPresenter, appCMSPresenter.jsonValueKeyMap, component, description)
        ViewCreator.setTypeFace(parentLayout.context, appCMSPresenter, appCMSPresenter.jsonValueKeyMap, component, alreadyLogin)
        ViewCreator.setTypeFace(parentLayout.context, appCMSPresenter, appCMSPresenter.jsonValueKeyMap, component, backButton)
        ViewCreator.setTypeFace(parentLayout.context, appCMSPresenter, appCMSPresenter.jsonValueKeyMap, component, backButton2)
        ViewCreator.setTypeFace(parentLayout.context, appCMSPresenter, appCMSPresenter.jsonValueKeyMap, component, seriesInformationBackButton)
        ViewCreator.setTypeFace(parentLayout.context, appCMSPresenter, appCMSPresenter.jsonValueKeyMap, component, episodeName)
        ViewCreator.setTypeFace(parentLayout.context, appCMSPresenter, appCMSPresenter.jsonValueKeyMap, component, episodePrice)
        ViewCreator.setTypeFace(parentLayout.context, appCMSPresenter, appCMSPresenter.jsonValueKeyMap, component, episodePrice)
        ViewCreator.setTypeFace(parentLayout.context, appCMSPresenter, appCMSPresenter.jsonValueKeyMap, component, seasonName)
        ViewCreator.setTypeFace(parentLayout.context, appCMSPresenter, appCMSPresenter.jsonValueKeyMap, component, seasonPrice)
        ViewCreator.setTypeFace(parentLayout.context, appCMSPresenter, appCMSPresenter.jsonValueKeyMap, component, seriesName)
        ViewCreator.setTypeFace(parentLayout.context, appCMSPresenter, appCMSPresenter.jsonValueKeyMap, component, seriesPrice)
        ViewCreator.setTypeFace(parentLayout.context, appCMSPresenter, appCMSPresenter.jsonValueKeyMap, component, entitlementButton1)
        ViewCreator.setTypeFace(parentLayout.context, appCMSPresenter, appCMSPresenter.jsonValueKeyMap, component, entitlementButton2)
        ViewCreator.setTypeFace(parentLayout.context, appCMSPresenter, appCMSPresenter.jsonValueKeyMap, component, entitlementButton3)
        ViewCreator.setTypeFace(parentLayout.context, appCMSPresenter, appCMSPresenter.jsonValueKeyMap, component, entitlementButton4)
        ViewCreator.setTypeFace(parentLayout.context, appCMSPresenter, appCMSPresenter.jsonValueKeyMap, component, termsView)
        component.fontWeight = parentLayout.context.getString(R.string.app_cms_page_font_bold_key)
        ViewCreator.setTypeFace(parentLayout.context, appCMSPresenter, appCMSPresenter.jsonValueKeyMap, component, seriesPriceWithType)
        ViewCreator.setTypeFace(parentLayout.context, appCMSPresenter, appCMSPresenter.jsonValueKeyMap, component, seriesIncludeTitle)
        ViewCreator.setTypeFace(parentLayout.context, appCMSPresenter, appCMSPresenter.jsonValueKeyMap, component, seriesTitle)
        ViewCreator.setTypeFace(parentLayout.context, appCMSPresenter, appCMSPresenter.jsonValueKeyMap, component, seasonNum)
        ViewCreator.setTypeFace(parentLayout.context, appCMSPresenter, appCMSPresenter.jsonValueKeyMap, component, episodeNum)
        ViewCreator.setTypeFace(parentLayout.context, appCMSPresenter, appCMSPresenter.jsonValueKeyMap, component, bundleTitle)
        ViewCreator.setTypeFace(parentLayout.context, appCMSPresenter, appCMSPresenter.jsonValueKeyMap, component, videoNum)
    }

    private fun setData() {
        title.text = appCMSPresenter.localisedStrings.waysToWatchText
        description.text = appCMSPresenter.localisedStrings.waysToWatchMessageText
        alreadyLogin.text = appCMSPresenter.localisedStrings.haveAccountText.plus(" ").plus(appCMSPresenter.localisedStrings.loginText)
        entitlementButton1.text = appCMSPresenter.localisedStrings.rentLabel
        entitlementButton2.text = appCMSPresenter.localisedStrings.buyLabel
        if (appCMSPresenter.isUserSubscribed)
            entitlementButton3.text = appCMSPresenter.localisedStrings.upgradeSubscriptionText
        else
            entitlementButton3.text = appCMSPresenter.localisedStrings.becomeMemberText
        entitlementButton4.text = appCMSPresenter.localisedStrings.chooseTVProviderText
        val loginClick: ClickableSpan = object : ClickableSpan() {
            override fun onClick(view: View) {
                removeSheet()
                appCMSPresenter.setSelectedPlan(null, null);
                appCMSPresenter.launchType = AppCMSPresenter.LaunchType.LOGIN_AND_SIGNUP
                appCMSPresenter.navigateToLoginPage(false)
            }
        }
        appCMSPresenter.makeTextViewLinks(alreadyLogin, arrayOf(appCMSPresenter.localisedStrings.loginText), arrayOf(loginClick), true)
        if (appCMSPresenter.isUserLoggedIn) {
            alreadyLogin.gone()
            description.text = appCMSPresenter.localisedStrings.waysToWatchMessageText
        } else {
            alreadyLogin.visible()
            description.text = appCMSPresenter.localisedStrings.waysToWatchMessageText
        }
        termsView.setText(appCMSPresenter.localisedStrings.tnCext)
        val tosClick: ClickableSpan = object : ClickableSpan() {
            override fun onClick(view: View) {
                removeSheet()
                appCMSPresenter.navigatToTOSPage(null, null)
            }
        }
        val privacyClick: ClickableSpan = object : ClickableSpan() {
            override fun onClick(view: View) {
                removeSheet()
                appCMSPresenter.navigateToPrivacyPolicy(null, null)
            }
        }
        appCMSPresenter.makeTextViewLinks(termsView, arrayOf(
                appCMSPresenter.localisedStrings.termsOfUsesText, appCMSPresenter.localisedStrings.privacyPolicyText), arrayOf(tosClick, privacyClick), true)


    }

    /**
     * Handles buttons for bundle content on basis of monetization models
     * @param monetizationModels - models attached to content
     **/
    private fun handleButtonVisibilityForBundle(monetizationModels: List<MonetizationModels>) {
        val isContentPurchased = contentTypeChecker.isBundlePurchased(appCMSPresenter.appPreference.getUserPurchases(), videoContentDatum.moduleApi.contentData[0].gist.bundleList, videoContentDatum.moduleApi.contentData[0].gist.id);
        val isUserSubscribed = appCMSPresenter.isUserSubscribed
        val isTveUser = appCMSPresenter.appPreference.getTVEUserId() != null
        /* content is SVOD, TVE, TVOD */
        if (contentTypeChecker.isContentSVODMonetization(monetizationModels) && contentTypeChecker.isContentTVODMonetization(monetizationModels)
                && contentTypeChecker.isContentTVEMonetization(monetizationModels)) {
            if (!(isContentPurchased || isUserSubscribed || isTveUser)) {
                handleTvodBundleBuyRent(videoContentDatum.moduleApi.contentData[0].bundlePlans)
                expandSheet()
            }
            return
        }
        /* content is SVOD, TVE */
        if (contentTypeChecker.isContentSVODMonetization(monetizationModels)
                && contentTypeChecker.isContentTVEMonetization(monetizationModels)
                && !(appCMSPresenter.isUserSubscribed && appCMSPresenter.appPreference.getTVEUserId() != null)) {
            if (!(isUserSubscribed || isTveUser)) {
                entitlementButton1.gone()
                entitlementButton2.gone()
                expandSheet()
            }
            return
        }
        /* content is  TVE, TVOD */
        if (contentTypeChecker.isContentTVODMonetization(monetizationModels)
                && contentTypeChecker.isContentTVEMonetization(monetizationModels)) {
            if (!(isContentPurchased || isTveUser)) {
                handleTvodBundleBuyRent(videoContentDatum.moduleApi.contentData[0].bundlePlans)
                entitlementButton3.gone()
                expandButton(entitlementButton4, tveSvodButtons)
                expandSheet()
            }
            return
        }
        /* content is SVOD, TVOD */
        if (contentTypeChecker.isContentSVODMonetization(monetizationModels) && contentTypeChecker.isContentTVODMonetization(monetizationModels)) {
            if (!(isContentPurchased || isUserSubscribed)) {
                handleTvodBundleBuyRent(videoContentDatum.moduleApi.contentData[0].bundlePlans)
                entitlementButton4.gone()
                expandButton(entitlementButton3, tveSvodButtons)
                expandSheet()
            }
            return
        }
        /* content is SVOD */
        if (contentTypeChecker.isContentSVODMonetization(monetizationModels) && !appCMSPresenter.isUserSubscribed) {
            entitlementButton1.gone()
            entitlementButton2.gone()
            entitlementButton4.gone()
            expandButton(entitlementButton3, tveSvodButtons)
            expandSheet()
            return
        }
        /* content is TVOD */
        if (contentTypeChecker.isContentTVODMonetization(monetizationModels) && !isContentPurchased && videoContentDatum.moduleApi.contentData[0].bundlePlans != null) {
            entitlementButton4.gone()
            entitlementButton3.gone()
            handleTvodBundleBuyRent(videoContentDatum.moduleApi.contentData[0].bundlePlans)
            if (entitlementButton1.visibility == View.VISIBLE || entitlementButton2.visibility == View.VISIBLE) {
                expandSheet()
            }
            return
        }
        /* content is TVE */
        if (contentTypeChecker.isContentTVEMonetization(monetizationModels) && appCMSPresenter.appPreference.getTVEUserId() == null) {
            entitlementButton1.gone()
            entitlementButton2.gone()
            entitlementButton3.gone()
            expandButton(entitlementButton4, tveSvodButtons)
            expandSheet()
            return
        }
    }

    /**
     * Handles buttons for series content on basis of monetization models
     * @param monetizationModels - models attached to content
     **/
    private fun handleButtonVisibilityForSeriesEntitlement(monetizationModels: List<MonetizationModels>) {
        expandSheet()
        if (contentTypeChecker.isPurchaseAvailableForSeriesOrSeasonOrEpisode(videoContentDatum.moduleApi.contentData[0]) &&
                contentTypeChecker.isRentAvailableForSeriesOrSeasonOrEpisode(videoContentDatum.moduleApi.contentData[0])) {
            entitlementButton2.text = appCMSPresenter.localisedStrings.buyOptionsLabel
            entitlementButton1.text = appCMSPresenter.localisedStrings.rentOptionsLabel
        } else if (!contentTypeChecker.isPurchaseAvailableForSeriesOrSeasonOrEpisode(videoContentDatum.moduleApi.contentData[0]) &&
                contentTypeChecker.isRentAvailableForSeriesOrSeasonOrEpisode(videoContentDatum.moduleApi.contentData[0])) {
            entitlementButton2.gone()
            entitlementButton1.text = appCMSPresenter.localisedStrings.rentOptionsLabel
            expandButton(entitlementButton1, tvodButtons)
        } else if (contentTypeChecker.isPurchaseAvailableForSeriesOrSeasonOrEpisode(videoContentDatum.moduleApi.contentData[0]) &&
                !contentTypeChecker.isRentAvailableForSeriesOrSeasonOrEpisode(videoContentDatum.moduleApi.contentData[0])) {
            entitlementButton1.gone()
            entitlementButton2.text = appCMSPresenter.localisedStrings.buyOptionsLabel
            expandButton(entitlementButton2, tvodButtons)
        } else {
            entitlementButton1.gone()
            entitlementButton2.gone()
        }

        if (contentTypeChecker.isContentSVODMonetization(monetizationModels) && contentTypeChecker.isContentTVEMonetization(monetizationModels)) {
            if (appCMSPresenter.isUserSubscribed) {
                entitlementButton3.gone()
                expandButton(entitlementButton4, tveSvodButtons)
            }

            if (appCMSPresenter.appPreference.getTVEUserId() != null && entitlementButton3.visibility == View.VISIBLE) {
                entitlementButton4.gone()
                expandButton(entitlementButton3, tveSvodButtons)
            }
        } else if (!contentTypeChecker.isContentSVODMonetization(monetizationModels) && contentTypeChecker.isContentTVEMonetization(monetizationModels)) {
            entitlementButton3.gone()
            expandButton(entitlementButton4, tveSvodButtons)
        } else if (contentTypeChecker.isContentSVODMonetization(monetizationModels) && !contentTypeChecker.isContentTVEMonetization(monetizationModels)) {
            entitlementButton4.gone()
            expandButton(entitlementButton3, tveSvodButtons)
        } else {
            entitlementButton3.gone()
            entitlementButton4.gone()
        }
    }

    /**
     * Handles TVOD buttons for series
     **/
    private fun handleTVODButtonsForSeries() {
        if (contentTypeChecker.isPurchaseAvailableForSeriesOrSeasonOrEpisode(videoContentDatum.moduleApi.contentData[0]) &&
                contentTypeChecker.isRentAvailableForSeriesOrSeasonOrEpisode(videoContentDatum.moduleApi.contentData[0])) {
            entitlementButton2.text = appCMSPresenter.localisedStrings.buyOptionsLabel
            entitlementButton1.text = appCMSPresenter.localisedStrings.rentOptionsLabel
        } else if (!contentTypeChecker.isPurchaseAvailableForSeriesOrSeasonOrEpisode(videoContentDatum.moduleApi.contentData[0]) &&
                contentTypeChecker.isRentAvailableForSeriesOrSeasonOrEpisode(videoContentDatum.moduleApi.contentData[0])) {
            entitlementButton2.gone()
            entitlementButton1.text = appCMSPresenter.localisedStrings.rentOptionsLabel
            expandButton(entitlementButton1, tvodButtons)
        } else if (contentTypeChecker.isPurchaseAvailableForSeriesOrSeasonOrEpisode(videoContentDatum.moduleApi.contentData[0]) &&
                !contentTypeChecker.isRentAvailableForSeriesOrSeasonOrEpisode(videoContentDatum.moduleApi.contentData[0])) {
            entitlementButton1.gone()
            entitlementButton2.text = appCMSPresenter.localisedStrings.buyOptionsLabel
            expandButton(entitlementButton2, tvodButtons)
        } else {
            entitlementButton1.gone()
            entitlementButton2.gone()
        }
    }

    /**
     * Handles buttons for series content on basis of monetization models
     * @param monetizationModels - models attached to content
     **/
    private fun handleButtonVisibilityForSeries(monetizationModels: List<MonetizationModels>) {
        val isUserSubscribed = appCMSPresenter.isUserSubscribed
        val isTveUser = appCMSPresenter.appPreference.getTVEUserId() != null
        val isSeriesPurchased = contentTypeChecker.isContentPurchased(appCMSPresenter.appPreference.getUserPurchases(), videoContentDatum.moduleApi.contentData[0].gist.id);
        val isAllSeasonsPurchased = contentTypeChecker.isAllSeasonsPurchased(appCMSPresenter.appPreference.getUserPurchases(), videoContentDatum.moduleApi.contentData[0].season);
        val isAllEpisodesPurchased = contentTypeChecker.isAllEpisodesPurchased(appCMSPresenter.appPreference.getUserPurchases(), videoContentDatum.moduleApi.contentData[0].season);
        val isEveryContentPurchased = isSeriesPurchased || isAllSeasonsPurchased || isAllEpisodesPurchased
        if (isEveryContentPurchased)
            return

        /* content is TVE, SVOD, TVOD */
        if (contentTypeChecker.isContentTVEMonetization(monetizationModels) && contentTypeChecker.isContentSVODMonetization(monetizationModels)
                && contentTypeChecker.isContentTVODMonetization(monetizationModels)) {
            if (isUserSubscribed && isTveUser) {
                entitlementButton3.gone()
                entitlementButton4.gone()
            } else if (!isUserSubscribed && isTveUser) {
                expandButton(entitlementButton3, tveSvodButtons)
                entitlementButton4.gone()
            } else if (isUserSubscribed && !isTveUser) {
                expandButton(entitlementButton4, tveSvodButtons)
                entitlementButton3.gone()
            }
            if (!isEveryContentPurchased)
                expandSheet()
            return
        }

        /* content is TVE, SVOD */
        if (contentTypeChecker.isContentTVEMonetization(monetizationModels) && contentTypeChecker.isContentSVODMonetization(monetizationModels)) {
            if (!(isUserSubscribed || isTveUser)) {
                if (!isEveryContentPurchased)
                    handleTVODButtonsForSeries() else {
                    entitlementButton1.gone()
                    entitlementButton2.gone()
                }
                expandSheet()
            }
            return
        }

        /* content is TVE, TVOD */
        if (contentTypeChecker.isContentTVEMonetization(monetizationModels) && contentTypeChecker.isContentTVODMonetization(monetizationModels)) {
            entitlementButton3.gone()
            if (!isTveUser)
                expandButton(entitlementButton4, tveSvodButtons)
            else
                entitlementButton4.gone()
            if (!isEveryContentPurchased) {
                handleTVODButtonsForSeries()
                expandSheet()
            }
            return
        }

        /* content is SVOD, TVOD */
        if (contentTypeChecker.isContentSVODMonetization(monetizationModels) && contentTypeChecker.isContentTVODMonetization(monetizationModels)) {
            entitlementButton4.gone()
            if (!isUserSubscribed)
                expandButton(entitlementButton3, tveSvodButtons)
            else
                entitlementButton3.gone()
            if (!isEveryContentPurchased) {
                handleTVODButtonsForSeries()
                expandSheet()
            }
            return
        }
        /* content is TVOD */
        if (contentTypeChecker.isContentTVODMonetization(monetizationModels) && !isEveryContentPurchased) {
            entitlementButton4.gone()
            entitlementButton3.gone()
            handleTVODButtonsForSeries()
            if (entitlementButton1.visibility == View.VISIBLE || entitlementButton2.visibility == View.VISIBLE)
                expandSheet()
            return
        }

        /* content is SVOD */
        if (contentTypeChecker.isContentSVODMonetization(monetizationModels)) {
            if (!isUserSubscribed) {
                entitlementButton4.gone()
                expandButton(entitlementButton3, tveSvodButtons)
            }
            if (!isEveryContentPurchased)
                handleTVODButtonsForSeries()
            else {
                entitlementButton1.gone()
                entitlementButton2.gone()
            }
            if (!isUserSubscribed || entitlementButton1.visibility == View.VISIBLE || entitlementButton2.visibility == View.VISIBLE)
                expandSheet()
            return
        }
        /* content is TVE */
        if (contentTypeChecker.isContentTVEMonetization(monetizationModels)) {
            if (!isTveUser) {
                entitlementButton3.gone()
                expandButton(entitlementButton4, tveSvodButtons)
            }
            if (!isEveryContentPurchased)
                handleTVODButtonsForSeries() else {
                entitlementButton1.gone()
                entitlementButton2.gone()
            }
            expandSheet()
            return
        }
    }

    /**
     * Handles buttons for video content on basis of subscription plans from entitlement plans
     **/
    private fun handleButtonVisibilityEntitlement() {
        if (videoContentDatum.subscriptionPlans != null) {
            if (contentTypeChecker.isContentSVOD_TVOD_TVE(videoContentDatum.subscriptionPlans, null)) {
                handleTvodBuyRentEntitlement(videoContentDatum.subscriptionPlans)
                if (appCMSPresenter.appPreference.getTVEUserId() != null) {
                    entitlementButton4.gone()
                    expandButton(entitlementButton3, tveSvodButtons)
                }
                expandSheet()
            } else if (contentTypeChecker.isContentSVOD_TVOD(videoContentDatum.subscriptionPlans, null)) {
                handleTvodBuyRentEntitlement(videoContentDatum.subscriptionPlans)
                entitlementButton4.gone()
                expandButton(entitlementButton3, tveSvodButtons)
                expandSheet()
            } else if (contentTypeChecker.isContentSVOD_TVE(videoContentDatum.subscriptionPlans, null)) {
                entitlementButton1.gone()
                entitlementButton2.gone()
                if (appCMSPresenter.appPreference.getTVEUserId() != null) {
                    entitlementButton4.gone()
                    expandButton(entitlementButton3, tveSvodButtons)
                }
                expandSheet()
            } else if (contentTypeChecker.isContentTVOD_TVE(videoContentDatum.subscriptionPlans)) {
                handleTvodBuyRentEntitlement(videoContentDatum.subscriptionPlans)
                entitlementButton3.gone()
                expandButton(entitlementButton4, tveSvodButtons)
                if (appCMSPresenter.appPreference.getTVEUserId() != null)
                    entitlementButton4.gone()
                expandSheet()
            } else if (contentTypeChecker.isContentTVOD(videoContentDatum.subscriptionPlans) || contentTypeChecker.isContentTVOD_AVOD(videoContentDatum.subscriptionPlans)) {
                handleTvodBuyRentEntitlement(videoContentDatum.subscriptionPlans)
                entitlementButton3.gone()
                entitlementButton4.gone()
                expandSheet()
            } else if (contentTypeChecker.isContentTVE(videoContentDatum.subscriptionPlans) || contentTypeChecker.isContentTVE_AVOD(videoContentDatum.subscriptionPlans)) {
                entitlementButton1.gone()
                entitlementButton2.gone()
                entitlementButton3.gone()
                expandButton(entitlementButton4, tveSvodButtons)
                expandSheet()
            } else if (contentTypeChecker.isContentSVOD(videoContentDatum.subscriptionPlans) || contentTypeChecker.isContentSVOD_AVOD(videoContentDatum.subscriptionPlans)) {
                entitlementButton1.gone()
                entitlementButton2.gone()
                entitlementButton4.gone()
                expandButton(entitlementButton3, tveSvodButtons)
                expandSheet()
            }
        }
    }

    /**
     * Handles buttons for video content on basis of monetization models
     **/
    private fun handleButtonVisibility() {
        var isContentPurchased = contentTypeChecker.isContentPurchased(appCMSPresenter.appPreference.getUserPurchases(), videoContentDatum.moduleApi.contentData[0].gist.id);
        if (!isContentPurchased && videoContentDatum.moduleApi.contentData[0].seasonId != null)
            isContentPurchased = contentTypeChecker.isContentPurchased(appCMSPresenter.appPreference.getUserPurchases(), videoContentDatum.moduleApi.contentData[0].seasonId);
        if (!isContentPurchased && videoContentDatum.moduleApi.contentData[0].seriesId != null)
            isContentPurchased = contentTypeChecker.isContentPurchased(appCMSPresenter.appPreference.getUserPurchases(), videoContentDatum.moduleApi.contentData[0].seriesId);
        val isUserSubscribed = appCMSPresenter.isUserSubscribed
        val isTveUser = appCMSPresenter.appPreference.getTVEUserId() != null
        if (videoContentDatum.monetizationModels.size > 0) {
            /* content is SVOD, TVE, TVOD */
            if (contentTypeChecker.isContentSVODMonetization(videoContentDatum.monetizationModels) && contentTypeChecker.isContentTVODMonetization(videoContentDatum.monetizationModels)
                    && contentTypeChecker.isContentTVEMonetization(videoContentDatum.monetizationModels)) {
                if (!(isContentPurchased || isUserSubscribed || isTveUser)) {
                    handleTvodBuyRent()
                    expandSheet()
                }
                return
            }
            /* content is SVOD, TVE */
            if (contentTypeChecker.isContentSVODMonetization(videoContentDatum.monetizationModels)
                    && contentTypeChecker.isContentTVEMonetization(videoContentDatum.monetizationModels)
                    && !(appCMSPresenter.isUserSubscribed && appCMSPresenter.appPreference.getTVEUserId() != null)) {
                if (!(isUserSubscribed || isTveUser)) {
                    entitlementButton1.gone()
                    entitlementButton2.gone()
                    expandSheet()
                }
                return
            }
            /* content is  TVE, TVOD */
            if (contentTypeChecker.isContentTVODMonetization(videoContentDatum.monetizationModels)
                    && contentTypeChecker.isContentTVEMonetization(videoContentDatum.monetizationModels)) {
                if (!(isContentPurchased || isTveUser)) {
                    handleTvodBuyRent()
                    entitlementButton3.gone()
                    expandButton(entitlementButton4, tveSvodButtons)
                    expandSheet()
                }
                return
            }
            /* content is SVOD, TVOD */
            if (contentTypeChecker.isContentSVODMonetization(videoContentDatum.monetizationModels) && contentTypeChecker.isContentTVODMonetization(videoContentDatum.monetizationModels)) {
                if (!(isContentPurchased || isUserSubscribed)) {
                    handleTvodBuyRent()
                    entitlementButton4.gone()
                    expandButton(entitlementButton3, tveSvodButtons)
                    expandSheet()
                }
                return
            }
            /* content is SVOD */
            if (contentTypeChecker.isContentSVODMonetization(videoContentDatum.monetizationModels) && !appCMSPresenter.isUserSubscribed) {
                if (!contentTypeChecker.isContentFREEMonetization(videoContentDatum.monetizationModels)) {
                    entitlementButton1.gone()
                    entitlementButton2.gone()
                    entitlementButton4.gone()
                    expandButton(entitlementButton3, tveSvodButtons)
                    expandSheet()
                }
                return
            }
            /* content is TVOD */
            if (contentTypeChecker.isContentTVODMonetization(videoContentDatum.monetizationModels) && !isContentPurchased) {
                entitlementButton4.gone()
                entitlementButton3.gone()
                handleTvodBuyRent()
                if ((entitlementButton1.visibility == View.VISIBLE || entitlementButton2.visibility == View.VISIBLE)) {
                    expandSheet()
                }
                return
            }
            /* content is TVE */
            if (contentTypeChecker.isContentTVEMonetization(videoContentDatum.monetizationModels) && appCMSPresenter.appPreference.getTVEUserId() == null) {
                entitlementButton1.gone()
                entitlementButton2.gone()
                entitlementButton3.gone()
                expandButton(entitlementButton4, tveSvodButtons)
                expandSheet()
                return
            }
            /*if (contentTypeChecker.isContentAVODMonetization(videoContentDatum.monetizationModels) || contentTypeChecker.isContentAVODMonetization(videoContentDatum.monetizationModels)) {
                removeSheet()
                return
            }*/

        } else {
            if (!isUserSubscribed) {
                entitlementButton1.gone()
                entitlementButton2.gone()
                entitlementButton4.gone()
                expandButton(entitlementButton3, tveSvodButtons)
                expandSheet()
            }
        }


    }

    private fun handleTvodBuyRentEntitlement(subscriptionPlans: List<ContentDatum>) {
        if (contentTypeChecker.purchaseEnabledTvod(subscriptionPlans) && contentTypeChecker.rentEnabledTvod(subscriptionPlans)) {
            entitlementButton2.text = appCMSPresenter.localisedStrings.buyLabel.plus(" ").plus(contentTypeChecker.tvodPlan(subscriptionPlans)?.let { contentTypeChecker.fetchTvodBuyPrice(it) })
            entitlementButton1.text = appCMSPresenter.localisedStrings.rentLabel.plus(" ").plus(contentTypeChecker.tvodPlan(subscriptionPlans)?.let { contentTypeChecker.fetchTvodRentPrice(it) })
        } else if (!contentTypeChecker.purchaseEnabledTvod(subscriptionPlans) && contentTypeChecker.rentEnabledTvod(subscriptionPlans)) {
            entitlementButton2.gone()
            entitlementButton1.text = appCMSPresenter.localisedStrings.rentLabel.plus(" ").plus(contentTypeChecker.tvodPlan(subscriptionPlans)?.let { contentTypeChecker.fetchTvodRentPrice(it) })
            expandButton(entitlementButton1, tvodButtons)
        } else if (contentTypeChecker.purchaseEnabledTvod(subscriptionPlans) && !contentTypeChecker.rentEnabledTvod(subscriptionPlans)) {
            entitlementButton1.gone()
            entitlementButton2.text = appCMSPresenter.localisedStrings.buyLabel.plus(" ").plus(contentTypeChecker.tvodPlan(subscriptionPlans)?.let { contentTypeChecker.fetchTvodBuyPrice(it) })
            expandButton(entitlementButton2, tvodButtons)
        }
    }

    private fun handleTvodBuyRentBundle(subscriptionPlans: List<ContentDatum>) {
        if (contentTypeChecker.purchaseEnabledTvod(subscriptionPlans) && contentTypeChecker.rentEnabledTvod(subscriptionPlans)) {
            entitlementButton2.text = appCMSPresenter.localisedStrings.buyOptionsLabel
            entitlementButton1.text = appCMSPresenter.localisedStrings.rentOptionsLabel
        } else if (!contentTypeChecker.purchaseEnabledTvod(subscriptionPlans) && contentTypeChecker.rentEnabledTvod(subscriptionPlans)) {
            entitlementButton2.gone()
            entitlementButton1.text = appCMSPresenter.localisedStrings.rentOptionsLabel
            expandButton(entitlementButton1, tvodButtons)
        } else if (contentTypeChecker.purchaseEnabledTvod(subscriptionPlans) && !contentTypeChecker.rentEnabledTvod(subscriptionPlans)) {
            entitlementButton1.gone()
            entitlementButton2.text = appCMSPresenter.localisedStrings.buyOptionsLabel
            expandButton(entitlementButton2, tvodButtons)
        } else {
            entitlementButton1.gone()
            entitlementButton2.gone()
        }
    }

    private fun handleTvodBuyRent() {
        if (videoContentDatum.tvodPricing != null && videoContentDatum.tvodPricing.rentAmount != 0F && videoContentDatum.tvodPricing.buyAmount != 0F) {
            entitlementButton2.text = appCMSPresenter.localisedStrings.buyLabel.plus(" ").plus(Currency.getInstance(videoContentDatum.tvodPricing.currencyCode).symbol).plus(" ").plus(videoContentDatum.tvodPricing.buyAmount)
            entitlementButton1.text = appCMSPresenter.localisedStrings.rentLabel.plus(" ").plus(Currency.getInstance(videoContentDatum.tvodPricing.currencyCode).symbol).plus(" ").plus(videoContentDatum.tvodPricing.rentAmount)
        } else if (videoContentDatum.tvodPricing != null && videoContentDatum.tvodPricing.rentAmount != 0F && videoContentDatum.tvodPricing.buyAmount == 0F) {
            entitlementButton2.gone()
            entitlementButton1.text = appCMSPresenter.localisedStrings.rentLabel.plus(" ").plus(Currency.getInstance(videoContentDatum.tvodPricing.currencyCode).symbol).plus(" ").plus(videoContentDatum.tvodPricing.rentAmount)
            expandButton(entitlementButton1, tvodButtons)
        } else if (videoContentDatum.tvodPricing != null && videoContentDatum.tvodPricing.rentAmount == 0F && videoContentDatum.tvodPricing.buyAmount != 0F) {
            entitlementButton1.gone()
            entitlementButton2.text = appCMSPresenter.localisedStrings.buyLabel.plus(" ").plus(Currency.getInstance(videoContentDatum.tvodPricing.currencyCode).symbol).plus(" ").plus(videoContentDatum.tvodPricing.buyAmount)
            expandButton(entitlementButton2, tvodButtons)
        } else {
            entitlementButton1.gone()
            entitlementButton2.gone()
        }
    }

    private fun handleTvodBundleBuyRent(subscriptionPlans: List<ContentDatum>) {

        if (contentTypeChecker.purchaseEnabledTvod(subscriptionPlans) && contentTypeChecker.rentEnabledTvod(subscriptionPlans)) {
            contentTypeChecker.tvodPlan(subscriptionPlans)?.let {
                entitlementButton1.visible()
                entitlementButton2.visible()
                entitlementButton2.text = appCMSPresenter.localisedStrings.buyLabel.plus(" ").plus(contentTypeChecker.fetchTvodBuyPrice(it))
                entitlementButton1.text = appCMSPresenter.localisedStrings.rentLabel.plus(" ").plus(contentTypeChecker.fetchTvodRentPrice(it))
            }

        } else if (!contentTypeChecker.purchaseEnabledTvod(subscriptionPlans) && contentTypeChecker.rentEnabledTvod(subscriptionPlans)) {
            entitlementButton2.gone()
            expandButton(entitlementButton1, tvodButtons)
            contentTypeChecker.tvodPlan(subscriptionPlans)?.let {
                entitlementButton1.text = appCMSPresenter.localisedStrings.rentLabel.plus(" ").plus(contentTypeChecker.fetchTvodRentPrice(it))
            }
        } else if (contentTypeChecker.purchaseEnabledTvod(subscriptionPlans) && !contentTypeChecker.rentEnabledTvod(subscriptionPlans)) {
            entitlementButton1.gone()
            expandButton(entitlementButton2, tvodButtons)
            contentTypeChecker.tvodPlan(subscriptionPlans)?.let {
                entitlementButton2.text = appCMSPresenter.localisedStrings.buyLabel.plus(" ").plus(contentTypeChecker.fetchTvodBuyPrice(it))
            }
        } else {
            entitlementButton1.gone()
            entitlementButton2.gone()
        }
    }


    @OnClick(R.id.entitlementButton1)
    fun rentClick() {
        rent = true
        if (seriesPurchase) {
            tvodPurchaseOptions.visible()
            waysToWatchOptions.gone()
            bundlePurchaseOptions.gone()
            setPriceForSeries(videoContentDatum.moduleApi.contentData[0])
        } else if (bundlePurchase) {
            removeSheet()
            val selectedPlan = contentTypeChecker.tvodPlan(videoContentDatum.moduleApi.contentData[0].bundlePlans)
            appCMSPresenter.contentToPurchase = selectedPlan?.let { TvodPurchaseData(null, null, videoContentDatum.moduleApi.contentData[0].gist.id, false, false, true, true, it, videoContentDatum.moduleApi.contentData[0].gist.id, videoContentDatum.moduleApi.contentData[0].gist.title) }
            appCMSPresenter.launchType = AppCMSPresenter.LaunchType.TVOD_PURCHASE
            appCMSPresenter.navigateToLoginPage(false)
            /* bundlePurchaseOptions.visible()
             tvodPurchaseOptions.gone()
             waysToWatchOptions.gone()
             setPriceForBundle()
             setVideoTitles()*/
        } else {
            removeSheet()
            if (!videoContentDatum.isFromEntitlement) {
                appCMSPresenter.fetchSubscriptionPlansById(contentTypeChecker.contentPlansId(videoContentDatum.moduleApi.contentData[0].contentPlans, appCMSPresenter.currentContext.getString(R.string.pricing_model_TVOD))[0], {
                    val selectedPlan = it[0]
                    appCMSPresenter.contentToPurchase = TvodPurchaseData(null, null, videoContentDatum.gist.id, false, false, false, true, selectedPlan, videoContentDatum.gist.id, videoContentDatum.gist.title)
                    appCMSPresenter.launchType = AppCMSPresenter.LaunchType.TVOD_PURCHASE
                    appCMSPresenter.navigateToLoginPage(true)

                }, true)
            } else
                if (videoContentDatum.subscriptionPlans != null) {
                    val selectedPlan = contentTypeChecker.tvodPlan(videoContentDatum.subscriptionPlans)
                    appCMSPresenter.contentToPurchase = selectedPlan?.let { TvodPurchaseData(null, null, videoContentDatum.gist.id, false, false, false, true, it, videoContentDatum.gist.id, videoContentDatum.gist.title) }
                    appCMSPresenter.launchType = AppCMSPresenter.LaunchType.TVOD_PURCHASE
                    appCMSPresenter.navigateToLoginPage(false)
                }

        }
    }

    @OnClick(R.id.entitlementButton2)
    fun buyClick() {
        rent = false
        if (seriesPurchase) {
            tvodPurchaseOptions.visible()
            waysToWatchOptions.gone()
            setPriceForSeries(videoContentDatum.moduleApi.contentData[0])
        } else if (bundlePurchase) {
            removeSheet()
            val selectedPlan = contentTypeChecker.tvodPlan(videoContentDatum.moduleApi.contentData[0].bundlePlans)
            appCMSPresenter.contentToPurchase = selectedPlan?.let { TvodPurchaseData(null, null, videoContentDatum.moduleApi.contentData[0].gist.id, false, false, true, false, it, videoContentDatum.moduleApi.contentData[0].gist.id, videoContentDatum.moduleApi.contentData[0].gist.title) }
            appCMSPresenter.launchType = AppCMSPresenter.LaunchType.TVOD_PURCHASE
            appCMSPresenter.navigateToLoginPage(false)
            /* bundlePurchaseOptions.visible()
             tvodPurchaseOptions.gone()
             waysToWatchOptions.gone()
             setPriceForBundle()
             setVideoTitles()*/
        } else {
            removeSheet()
            if (!videoContentDatum.isFromEntitlement) {
                appCMSPresenter.fetchSubscriptionPlansById(contentTypeChecker.contentPlansId(videoContentDatum.moduleApi.contentData[0].contentPlans, appCMSPresenter.currentContext.getString(R.string.pricing_model_TVOD))[0], {
                    val selectedPlan = it[0]
                    appCMSPresenter.contentToPurchase = TvodPurchaseData(null, null, videoContentDatum.gist.id, false, false, false, false, selectedPlan, videoContentDatum.gist.id, videoContentDatum.gist.title)
                    appCMSPresenter.launchType = AppCMSPresenter.LaunchType.TVOD_PURCHASE
                    appCMSPresenter.navigateToLoginPage(true)

                }, true)
            } else if (videoContentDatum.subscriptionPlans != null) {
                val selectedPlan = contentTypeChecker.tvodPlan(videoContentDatum.subscriptionPlans)
                appCMSPresenter.contentToPurchase = selectedPlan?.let { TvodPurchaseData(null, null, videoContentDatum.gist.id, false, false, false, false, it, videoContentDatum.gist.id, videoContentDatum.gist.title) }
                appCMSPresenter.launchType = AppCMSPresenter.LaunchType.TVOD_PURCHASE
                appCMSPresenter.navigateToLoginPage(false)
            }
        }
    }


    @OnClick(R.id.entitlementButton3)
    fun memberClick() {
        removeSheet()
        if (appCMSPresenter.appPreference.getActiveSubscriptionProcessor() != null
                && !(appCMSPresenter.appPreference.getActiveSubscriptionProcessor()!!.toLowerCase().equals(appCMSPresenter.currentContext.getString(R.string.subscription_android_payment_processor).toLowerCase(), ignoreCase = true) ||
                        appCMSPresenter.appPreference.getActiveSubscriptionProcessor()!!.toLowerCase().equals(appCMSPresenter.currentContext.getString(R.string.subscription_android_payment_processor_friendly).toLowerCase(), ignoreCase = true))) {
            appCMSPresenter.showEntitlementDialog(AppCMSPresenter.DialogType.CANNOT_UPGRADE_SUBSCRIPTION_CONTENT, null, null)
            return
        }
        appCMSPresenter.loginFromNavPage = false
        if (seriesPurchase) {
            if (videoContentDatum.moduleApi.contentData[0].contentPlans != null) {
                if (videoContentDatum.moduleApi.contentData[0].contentPlans.size == 0) {
                    appCMSPresenter.navigateToSubscriptionPlansPage(false);
                    return
                }
                contentTypeChecker.contentPlansId(videoContentDatum.moduleApi.contentData[0].contentPlans, appCMSPresenter.currentContext.getString(R.string.pricing_model_SVOD)).apply {
                    var planId = this.get(0);
                    if (this.size > 1)
                        planId = this.joinToString(separator = ",")
                    appCMSPresenter.fetchSubscriptionPlansById(planId, {
                        appCMSPresenter.navigateToContentSubscription(it)

                    }, true)
                }
            } else {
                appCMSPresenter.navigateToSubscriptionPlansPage(false);
            }

        } else {
            if (videoContentDatum.subscriptionPlans != null && videoContentDatum.subscriptionPlans.size > 0) {
                appCMSPresenter.navigateToContentSubscription(videoContentDatum.subscriptionPlans)
                return
            }
            if (videoContentDatum.moduleApi.contentData[0].contentPlans != null) {
                if (videoContentDatum.moduleApi.contentData[0].contentPlans.size == 0) {
                    appCMSPresenter.navigateToSubscriptionPlansPage(false);
                    return
                }
                contentTypeChecker.contentPlansId(videoContentDatum.moduleApi.contentData[0].contentPlans, appCMSPresenter.currentContext.getString(R.string.pricing_model_SVOD)).apply {
                    var planId = this.get(0);
                    if (this.size > 1)
                        planId = this.joinToString(separator = ",")
                    appCMSPresenter.fetchSubscriptionPlansById(planId, {
                        if (it.size == 0) {
                            appCMSPresenter.navigateToSubscriptionPlansPage(false);
                        } else
                            appCMSPresenter.navigateToContentSubscription(it)

                    }, true)
                }
            } else {
                appCMSPresenter.navigateToSubscriptionPlansPage(false);
            }
        }
    }

    @OnClick(R.id.entitlementButton4)
    fun tveClick() {
        removeSheet()
        appCMSPresenter.loginFromNavPage = false
        appCMSPresenter.openTvProviderScreen()
    }

    @OnClick(R.id.backButton)
    fun backClick() {
        tvodPurchaseOptions.gone()
        bundlePurchaseOptions.gone()
        waysToWatchOptions.visible()
    }

    @OnClick(R.id.seriesInformationBackButton)
    fun seriesInformationBackClick() {
        parentLayout.setBackgroundColor(ViewCreator.getTransparentColor(appCMSPresenter.generalBackgroundColor, 0.8f))
        tvodPurchaseOptions.visible()
        bundlePurchaseOptions.gone()
        waysToWatchOptions.gone()
        seriesInformation.gone()
    }

    @OnClick(R.id.seriesInfo)
    fun seriesInfoClick() {
        seriesInformation.visible()
        createSeasonEpisodeView(videoContentDatum.moduleApi.contentData[0])
        tvodPurchaseOptions.gone()
    }

    @OnClick(R.id.backButton2)
    fun back2Click() {
        tvodPurchaseOptions.gone()
        bundlePurchaseOptions.gone()
        waysToWatchOptions.visible()
    }

    @OnClick(R.id.tvodEpisodePurchase)
    fun tvodEpisodePurchaseClick() {
        if (episodePrice.text.equals(appCMSPresenter.currentContext.getString(R.string.not_applicable)) || episodePrice.text.equals(appCMSPresenter.localisedStrings.rentedLabel)
                || episodePrice.text.equals(appCMSPresenter.localisedStrings.purchasedLabel) || episodePrice.text.equals(appCMSPresenter.localisedStrings.freeLabel))
            return

        removeSheet()
        val selectedPlan = contentTypeChecker.tvodPlan(videoContentDatum.moduleApi.contentData[0].season[seasonCounter].episodes[episodeCounter].episodePlans)
        appCMSPresenter.contentToPurchase = selectedPlan?.let {
            TvodPurchaseData(videoContentDatum.moduleApi.contentData[0].gist.id, videoContentDatum.moduleApi.contentData[0].season[seasonCounter].id, videoContentDatum.moduleApi.contentData[0].season[seasonCounter].episodes[episodeCounter].gist.id, false, false, false, rent, it,
                    videoContentDatum.moduleApi.contentData[0].season[seasonCounter].episodes[episodeCounter].gist.id, videoContentDatum.moduleApi.contentData[0].season[seasonCounter].episodes[episodeCounter].gist.title)
        }
        appCMSPresenter.launchType = AppCMSPresenter.LaunchType.TVOD_PURCHASE
        appCMSPresenter.navigateToLoginPage(false)
    }

    @OnClick(R.id.videoUp)
    fun videoUpClick() {
        if (videoContentDatum.moduleApi.contentData[0].gist.bundleList.size == videoCounter + 1) {
            videoUp.setColorFilter(ContextCompat.getColor(appCMSPresenter.currentContext, android.R.color.darker_gray))
            Toast.makeText(appCMSPresenter.currentContext, appCMSPresenter.localisedStrings.noVideoMsg, Toast.LENGTH_SHORT).show()
        } else {
            videoCounter++
            if (videoContentDatum.moduleApi.contentData[0].gist.bundleList.size == videoCounter + 1)
                videoUp.setColorFilter(ContextCompat.getColor(appCMSPresenter.currentContext, android.R.color.darker_gray))
            videoDown.setColorFilter(Color.parseColor(appCMSPresenter.appTextColor))
            setVideoTitles()
        }
    }

    @OnClick(R.id.videoDown)
    fun videoDownClick() {
        if (videoCounter == 0) {
            videoDown.setColorFilter(ContextCompat.getColor(appCMSPresenter.currentContext, android.R.color.darker_gray))
            Toast.makeText(appCMSPresenter.currentContext, appCMSPresenter.localisedStrings.noVideoMsg, Toast.LENGTH_SHORT).show()
        } else {
            videoUp.setColorFilter(Color.parseColor(appCMSPresenter.appTextColor))
            videoCounter--
            if (videoCounter == 0)
                videoDown.setColorFilter(ContextCompat.getColor(appCMSPresenter.currentContext, android.R.color.darker_gray))
            setVideoTitles()
        }
    }

    @OnClick(R.id.episodeUp)
    fun episodeUpClick() {
        if (videoContentDatum.moduleApi.contentData[0].season[seasonCounter].episodes.size == episodeCounter + 1) {
            episodeUp.setColorFilter(CMSColorUtils.lightenColor(Color.parseColor(appCMSPresenter.appCtaTextColor)))
            Toast.makeText(appCMSPresenter.currentContext, appCMSPresenter.localisedStrings.noEpisodeMsg, Toast.LENGTH_SHORT).show()
        } else {
            episodeCounter++
            if (videoContentDatum.moduleApi.contentData[0].season[seasonCounter].episodes.size == episodeCounter + 1)
                episodeUp.setColorFilter(CMSColorUtils.lightenColor(Color.parseColor(appCMSPresenter.appCtaTextColor)))
            episodeDown.setColorFilter(Color.parseColor(appCMSPresenter.appCtaTextColor))
            setPriceForSeries(videoContentDatum.moduleApi.contentData[0])
        }
    }

    @OnClick(R.id.episodeDown)
    fun episodeDownClick() {
        if (episodeCounter == 0) {
            episodeDown.setColorFilter(CMSColorUtils.lightenColor(Color.parseColor(appCMSPresenter.appCtaTextColor)))
            Toast.makeText(appCMSPresenter.currentContext, appCMSPresenter.localisedStrings.noEpisodeMsg, Toast.LENGTH_SHORT).show()
        } else {
            episodeUp.setColorFilter(Color.parseColor(appCMSPresenter.appCtaTextColor))
            episodeCounter--
            if (episodeCounter == 0)
                episodeDown.setColorFilter(CMSColorUtils.lightenColor(Color.parseColor(appCMSPresenter.appCtaTextColor)))
            setPriceForSeries(videoContentDatum.moduleApi.contentData[0])
        }
    }

    @OnClick(R.id.tvodSeasonPurchase)
    fun tvodSeasonPurchaseClick() {
        if (seasonPrice.text.equals(appCMSPresenter.currentContext.getString(R.string.not_applicable)) || seasonPrice.text.equals(appCMSPresenter.localisedStrings.rentedLabel) || seasonPrice.text.equals(appCMSPresenter.localisedStrings.purchasedLabel))
            return

        removeSheet()
        val selectedPlan = contentTypeChecker.tvodPlan(videoContentDatum.moduleApi.contentData[0].season[seasonCounter].seasonPlans)
        appCMSPresenter.contentToPurchase = selectedPlan?.let {
            TvodPurchaseData(videoContentDatum.moduleApi.contentData[0].id, videoContentDatum.moduleApi.contentData[0].season[seasonCounter].id, null, false, true, false, rent, it,
                    videoContentDatum.moduleApi.contentData[0].season[seasonCounter].id, videoContentDatum.moduleApi.contentData[0].season[seasonCounter].title)
        }
        appCMSPresenter.launchType = AppCMSPresenter.LaunchType.TVOD_PURCHASE
        appCMSPresenter.navigateToLoginPage(false)
    }

    @OnClick(R.id.seasonUp)
    fun seasonUpClick() {
        if (videoContentDatum.moduleApi.contentData[0].season.size == seasonCounter + 1) {
            seasonUp.setColorFilter(CMSColorUtils.lightenColor(Color.parseColor(appCMSPresenter.appCtaTextColor)))
            Toast.makeText(appCMSPresenter.currentContext, appCMSPresenter.localisedStrings.noSeasonMsg, Toast.LENGTH_SHORT).show()
        } else {
            seasonCounter++
            episodeCounter = 0
            if (videoContentDatum.moduleApi.contentData[0].season.size == seasonCounter + 1)
                seasonUp.setColorFilter(CMSColorUtils.lightenColor(Color.parseColor(appCMSPresenter.appCtaTextColor)))
            seasonDown.setColorFilter(Color.parseColor(appCMSPresenter.appCtaTextColor))
            episodeUp.setColorFilter(Color.parseColor(appCMSPresenter.appCtaTextColor))
            episodeDown.setColorFilter(CMSColorUtils.lightenColor(Color.parseColor(appCMSPresenter.appCtaTextColor)))
            setPriceForSeries(videoContentDatum.moduleApi.contentData[0])
        }
    }

    @OnClick(R.id.seasonDown)
    fun seasonDownClick() {
        if (seasonCounter == 0) {
            seasonDown.setColorFilter(CMSColorUtils.lightenColor(Color.parseColor(appCMSPresenter.appCtaTextColor)))
            Toast.makeText(appCMSPresenter.currentContext, appCMSPresenter.localisedStrings.noSeasonMsg, Toast.LENGTH_SHORT).show()
        } else {
            seasonCounter--
            episodeCounter = 0
            if (seasonCounter == 0)
                seasonDown.setColorFilter(CMSColorUtils.lightenColor(Color.parseColor(appCMSPresenter.appCtaTextColor)))
            seasonUp.setColorFilter(Color.parseColor(appCMSPresenter.appCtaTextColor))
            episodeUp.setColorFilter(Color.parseColor(appCMSPresenter.appCtaTextColor))
            episodeDown.setColorFilter(CMSColorUtils.lightenColor(Color.parseColor(appCMSPresenter.appCtaTextColor)))
            setPriceForSeries(videoContentDatum.moduleApi.contentData[0])

        }
    }

    @OnClick(R.id.tvodSeriesPurchase)
    fun tvodSeriesPurchaseClick() {
        if (seriesPrice.text.equals(appCMSPresenter.currentContext.getString(R.string.not_applicable)) || seriesPrice.text.equals(appCMSPresenter.localisedStrings.rentedLabel)
                || seriesPrice.text.equals(appCMSPresenter.localisedStrings.purchasedLabel) || seriesPrice.text.isEmpty())
            return
        removeSheet()
        val selectedPlan = contentTypeChecker.tvodPlan(videoContentDatum.moduleApi.contentData[0].seriesPlans)
        appCMSPresenter.contentToPurchase = selectedPlan?.let {
            TvodPurchaseData(videoContentDatum.moduleApi.contentData[0].gist.id, null, null, true, false, false, rent, it,
                    videoContentDatum.moduleApi.contentData[0].gist.id, videoContentDatum.moduleApi.contentData[0].gist.title)
        }
        appCMSPresenter.launchType = AppCMSPresenter.LaunchType.TVOD_PURCHASE
        appCMSPresenter.navigateToLoginPage(false)
    }

    @OnClick(R.id.tvodBundlePurchase)
    fun tvodBundlePurchaseClick() {
        removeSheet()
        val selectedPlan = contentTypeChecker.tvodPlan(videoContentDatum.moduleApi.contentData[0].bundlePlans)
        appCMSPresenter.contentToPurchase = selectedPlan?.let {
            TvodPurchaseData(null, null, videoContentDatum.moduleApi.contentData[0].gist.id, false, false, true, rent, it,
                    videoContentDatum.moduleApi.contentData[0].gist.id, videoContentDatum.moduleApi.contentData[0].gist.title)
        }
        appCMSPresenter.launchType = AppCMSPresenter.LaunchType.TVOD_PURCHASE
        appCMSPresenter.navigateToLoginPage(false)
    }

    private fun setPriceForBundle() {
        bundleName.text = videoContentDatum.moduleApi.contentData[0].gist.title
        bundleTitle.text = appCMSPresenter.localisedStrings.bundleHeaderText.plus(" :")
        if (rent) {
            contentTypeChecker.tvodPlan(videoContentDatum.moduleApi.contentData[0].bundlePlans)?.let {
                bundlePrice.text = contentTypeChecker.fetchTvodRentPrice(it)
                bundleTitle.text = appCMSPresenter.localisedStrings.bundleHeaderText.plus(":")
            }
        } else {
            contentTypeChecker.tvodPlan(videoContentDatum.moduleApi.contentData[0].bundlePlans)?.let {
                bundlePrice.text = contentTypeChecker.fetchTvodBuyPrice(it)
                bundleTitle.text = appCMSPresenter.localisedStrings.bundleHeaderText.plus(":")
            }
        }
        if (videoContentDatum.moduleApi.contentData[0].gist.bundleList.size == 1) {
            videoUp.setColorFilter(ContextCompat.getColor(appCMSPresenter.currentContext, android.R.color.darker_gray))
            videoDown.setColorFilter(ContextCompat.getColor(appCMSPresenter.currentContext, android.R.color.darker_gray))
        }
    }

    private fun setVideoTitles() {
        val videoCount = videoCounter + 1
        videoNum.text = videoCount.toString().plus(":")
        videoName.text = videoContentDatum.moduleApi.contentData[0].gist.bundleList[videoCounter].gist.title
    }

    private fun setPriceForSeries(contentDatum: ContentDatum) {
        seriesName.text = contentDatum.gist.title
        seriesTitle.text = appCMSPresenter.localisedStrings.seriesHeaderText.plus(":")

        val seasonNumber = seasonCounter + 1
        val episodeNumber = episodeCounter + 1
        seasonNum.text = "S".plus(seasonNumber).plus(":")
        episodeNum.text = "EP".plus(episodeNumber).plus(":")
        if (contentDatum.seriesPlans != null) {
            seriesContainer.visible()
            if (rent) {
                if (contentTypeChecker.isContentRentedByUser(appCMSPresenter.appPreference.getUserPurchases(), contentDatum.gist.id)) {
                    setTvodPrice(appCMSPresenter.localisedStrings.rentedLabel, seriesPrice)
                    seriesContainer.gone()
                } else if (contentTypeChecker.isContentPurchasedByUser(appCMSPresenter.appPreference.getUserPurchases(), contentDatum.gist.id)) {
                    setTvodPrice(appCMSPresenter.localisedStrings.purchasedLabel, seriesPrice)
                    seriesContainer.gone()
                } else
                    contentTypeChecker.tvodPlan(contentDatum.seriesPlans)?.let { contentTypeChecker.fetchTvodRentPrice(it) }.let {
                        if (it != null) {
                            setTvodPrice(it, seriesPrice)
                            seriesTitle.text = appCMSPresenter.localisedStrings.seriesHeaderText.plus(":")
                        } else {
                            setTvodPrice(appCMSPresenter.currentContext.getString(R.string.not_applicable), seriesPrice)
                            seriesContainer.gone()
                        }
                    }
            } else {
                if (contentTypeChecker.isContentRentedByUser(appCMSPresenter.appPreference.getUserPurchases(), contentDatum.gist.id)) {
                    setTvodPrice(appCMSPresenter.localisedStrings.rentedLabel, seriesPrice)
                    seriesContainer.gone()
                } else if (contentTypeChecker.isContentPurchasedByUser(appCMSPresenter.appPreference.getUserPurchases(), contentDatum.gist.id)) {
                    seriesContainer.gone()
                    setTvodPrice(appCMSPresenter.localisedStrings.purchasedLabel, seriesPrice)
                } else
                    contentTypeChecker.tvodPlan(contentDatum.seriesPlans)?.let { contentTypeChecker.fetchTvodBuyPrice(it) }.let {
                        if (it != null) {
                            setTvodPrice(it, seriesPrice)
                            seriesTitle.text = appCMSPresenter.localisedStrings.seriesHeaderText.plus(":")
                        } else {
                            setTvodPrice(appCMSPresenter.currentContext.getString(R.string.not_applicable), seriesPrice)
                            seriesContainer.gone()
                        }
                    }
            }
        } else {
            setTvodPrice(appCMSPresenter.currentContext.getString(R.string.not_applicable), seriesPrice)
            seriesContainer.gone()
        }


        seasonName.text = contentDatum.season[seasonCounter].title
        setTvodPrice(appCMSPresenter.currentContext.getString(R.string.not_applicable), seasonPrice)
        if (contentDatum.season[seasonCounter].seasonPlans != null) {
            seasonContainer.visible()
            if (rent) {
                if (contentTypeChecker.isContentRentedByUser(appCMSPresenter.appPreference.getUserPurchases(), contentDatum.season[seasonCounter].id)
                        || contentTypeChecker.isContentRentedByUser(appCMSPresenter.appPreference.getUserPurchases(), contentDatum.gist.id)) {
                    setTvodPrice(appCMSPresenter.localisedStrings.rentedLabel, seasonPrice)
                    seasonContainer.gone()
                } else if (contentTypeChecker.isContentPurchasedByUser(appCMSPresenter.appPreference.getUserPurchases(), contentDatum.season[seasonCounter].id)
                        || contentTypeChecker.isContentPurchasedByUser(appCMSPresenter.appPreference.getUserPurchases(), contentDatum.gist.id)) {
                    setTvodPrice(appCMSPresenter.localisedStrings.purchasedLabel, seasonPrice)
                    seasonContainer.gone()
                } else
                    contentTypeChecker.tvodPlan(contentDatum.season[seasonCounter].seasonPlans)?.let { contentTypeChecker.fetchTvodRentPrice(it) }.let {
                        if (it != null) {
                            setTvodPrice(it, seasonPrice)
                            seasonNum.text = "S".plus(seasonNumber).plus(":")
                        } else {
                            setTvodPrice(appCMSPresenter.currentContext.getString(R.string.not_applicable), seasonPrice)
                            seasonContainer.gone()
                        }
                    }
            } else {
                if (contentTypeChecker.isContentPurchasedByUser(appCMSPresenter.appPreference.getUserPurchases(), contentDatum.season[seasonCounter].id)
                        || contentTypeChecker.isContentPurchasedByUser(appCMSPresenter.appPreference.getUserPurchases(), contentDatum.gist.id))
                    setTvodPrice(appCMSPresenter.localisedStrings.purchasedLabel, seasonPrice)
                else if (contentTypeChecker.isContentRentedByUser(appCMSPresenter.appPreference.getUserPurchases(), contentDatum.season[seasonCounter].id)
                        || contentTypeChecker.isContentRentedByUser(appCMSPresenter.appPreference.getUserPurchases(), contentDatum.gist.id))
                    setTvodPrice(appCMSPresenter.localisedStrings.rentedLabel, seasonPrice)
                else
                    contentTypeChecker.tvodPlan(contentDatum.season[seasonCounter].seasonPlans)?.let { contentTypeChecker.fetchTvodBuyPrice(it) }.let {
                        if (it != null) {
                            setTvodPrice(it, seasonPrice)
                            seasonNum.text = "S".plus(seasonNumber).plus(":")
                        } else {
                            setTvodPrice(appCMSPresenter.currentContext.getString(R.string.not_applicable), seasonPrice)
                            seasonContainer.gone()
                        }
                    }
            }
        } else {
            seasonContainer.gone()
            if (rent) {
                if (contentTypeChecker.isContentRentedByUser(appCMSPresenter.appPreference.getUserPurchases(), contentDatum.season[seasonCounter].id)
                        || contentTypeChecker.isContentRentedByUser(appCMSPresenter.appPreference.getUserPurchases(), contentDatum.gist.id))
                    setTvodPrice(appCMSPresenter.localisedStrings.rentedLabel, seasonPrice)
                else if (contentTypeChecker.isContentPurchasedByUser(appCMSPresenter.appPreference.getUserPurchases(), contentDatum.season[seasonCounter].id)
                        || contentTypeChecker.isContentPurchasedByUser(appCMSPresenter.appPreference.getUserPurchases(), contentDatum.gist.id))
                    setTvodPrice(appCMSPresenter.localisedStrings.purchasedLabel, seasonPrice)

            } else {
                if (contentTypeChecker.isContentPurchasedByUser(appCMSPresenter.appPreference.getUserPurchases(), contentDatum.season[seasonCounter].id)
                        || contentTypeChecker.isContentPurchasedByUser(appCMSPresenter.appPreference.getUserPurchases(), contentDatum.gist.id))
                    setTvodPrice(appCMSPresenter.localisedStrings.purchasedLabel, seasonPrice)
                else if (contentTypeChecker.isContentRentedByUser(appCMSPresenter.appPreference.getUserPurchases(), contentDatum.season[seasonCounter].id)
                        || contentTypeChecker.isContentRentedByUser(appCMSPresenter.appPreference.getUserPurchases(), contentDatum.gist.id))
                    setTvodPrice(appCMSPresenter.localisedStrings.rentedLabel, seasonPrice)
            }
        }


        episodeName.text = contentDatum.season[seasonCounter].episodes[episodeCounter].gist.title
        setTvodPrice(appCMSPresenter.currentContext.getString(R.string.not_applicable), episodePrice)
        if (contentDatum.season[seasonCounter].episodes[episodeCounter].episodePlans != null) {
            if (rent) {
                if (contentTypeChecker.isContentFree(contentDatum.season[seasonCounter].episodes[episodeCounter].episodePlans) ||
                        contentTypeChecker.isContentAVOD(contentDatum.season[seasonCounter].episodes[episodeCounter].episodePlans))
                    setTvodPrice(appCMSPresenter.localisedStrings.freeLabel, episodePrice)
                else if (contentTypeChecker.isContentRentedByUser(appCMSPresenter.appPreference.getUserPurchases(), contentDatum.season[seasonCounter].episodes[episodeCounter].gist.id)
                        || contentTypeChecker.isContentRentedByUser(appCMSPresenter.appPreference.getUserPurchases(), contentDatum.season[seasonCounter].id)
                        || contentTypeChecker.isContentRentedByUser(appCMSPresenter.appPreference.getUserPurchases(), contentDatum.gist.id))
                    setTvodPrice(appCMSPresenter.localisedStrings.rentedLabel, episodePrice)
                else if (contentTypeChecker.isContentPurchasedByUser(appCMSPresenter.appPreference.getUserPurchases(), contentDatum.season[seasonCounter].episodes[episodeCounter].gist.id)
                        || contentTypeChecker.isContentPurchasedByUser(appCMSPresenter.appPreference.getUserPurchases(), contentDatum.season[seasonCounter].id)
                        || contentTypeChecker.isContentPurchasedByUser(appCMSPresenter.appPreference.getUserPurchases(), contentDatum.gist.id))
                    setTvodPrice(appCMSPresenter.localisedStrings.purchasedLabel, episodePrice)
                else if (contentDatum.season[seasonCounter].episodes[episodeCounter].episodePlans.size > 0)
                    contentTypeChecker.tvodPlan(contentDatum.season[seasonCounter].episodes[episodeCounter].episodePlans)?.let { contentTypeChecker.fetchTvodRentPrice(it) }?.let {
                        setTvodPrice(it, episodePrice)
                        episodeNum.text = "EP".plus(episodeNumber).plus(":")
                    }
            } else {
                if (contentTypeChecker.isContentFree(contentDatum.season[seasonCounter].episodes[episodeCounter].episodePlans) ||
                        contentTypeChecker.isContentAVOD(contentDatum.season[seasonCounter].episodes[episodeCounter].episodePlans))
                    setTvodPrice(appCMSPresenter.localisedStrings.freeLabel, episodePrice)
                else if (contentTypeChecker.isContentPurchasedByUser(appCMSPresenter.appPreference.getUserPurchases(), contentDatum.season[seasonCounter].episodes[episodeCounter].gist.id)
                        || contentTypeChecker.isContentPurchasedByUser(appCMSPresenter.appPreference.getUserPurchases(), contentDatum.season[seasonCounter].id)
                        || contentTypeChecker.isContentPurchasedByUser(appCMSPresenter.appPreference.getUserPurchases(), contentDatum.gist.id))
                    setTvodPrice(appCMSPresenter.localisedStrings.purchasedLabel, episodePrice)
                else if (contentTypeChecker.isContentRentedByUser(appCMSPresenter.appPreference.getUserPurchases(), contentDatum.season[seasonCounter].episodes[episodeCounter].gist.id)
                        || contentTypeChecker.isContentRentedByUser(appCMSPresenter.appPreference.getUserPurchases(), contentDatum.season[seasonCounter].id)
                        || contentTypeChecker.isContentRentedByUser(appCMSPresenter.appPreference.getUserPurchases(), contentDatum.gist.id))
                    setTvodPrice(appCMSPresenter.localisedStrings.rentedLabel, episodePrice)
                else if (contentDatum.season[seasonCounter].episodes[episodeCounter].episodePlans.size > 0)
                    contentTypeChecker.tvodPlan(contentDatum.season[seasonCounter].episodes[episodeCounter].episodePlans)?.let { contentTypeChecker.fetchTvodBuyPrice(it) }.let {
                        if (it != null) {
                            setTvodPrice(it, episodePrice)
                            episodeNum.text = "EP".plus(episodeNumber).plus(":")
                        } else
                            setTvodPrice(appCMSPresenter.currentContext.getString(R.string.not_applicable), episodePrice)
                    }
            }
        } else {
            if (rent) {
                if (contentTypeChecker.isContentRentedByUser(appCMSPresenter.appPreference.getUserPurchases(), contentDatum.season[seasonCounter].episodes[episodeCounter].gist.id)
                        || contentTypeChecker.isContentRentedByUser(appCMSPresenter.appPreference.getUserPurchases(), contentDatum.season[seasonCounter].id)
                        || contentTypeChecker.isContentRentedByUser(appCMSPresenter.appPreference.getUserPurchases(), contentDatum.gist.id))
                    setTvodPrice(appCMSPresenter.localisedStrings.rentedLabel, episodePrice)
                else if (contentTypeChecker.isContentPurchasedByUser(appCMSPresenter.appPreference.getUserPurchases(), contentDatum.season[seasonCounter].episodes[episodeCounter].gist.id)
                        || contentTypeChecker.isContentPurchasedByUser(appCMSPresenter.appPreference.getUserPurchases(), contentDatum.season[seasonCounter].id)
                        || contentTypeChecker.isContentPurchasedByUser(appCMSPresenter.appPreference.getUserPurchases(), contentDatum.gist.id))
                    setTvodPrice(appCMSPresenter.localisedStrings.purchasedLabel, episodePrice)
            } else {
                if (contentTypeChecker.isContentPurchasedByUser(appCMSPresenter.appPreference.getUserPurchases(), contentDatum.season[seasonCounter].episodes[episodeCounter].gist.id)
                        || contentTypeChecker.isContentPurchasedByUser(appCMSPresenter.appPreference.getUserPurchases(), contentDatum.season[seasonCounter].id)
                        || contentTypeChecker.isContentPurchasedByUser(appCMSPresenter.appPreference.getUserPurchases(), contentDatum.gist.id))
                    setTvodPrice(appCMSPresenter.localisedStrings.purchasedLabel, episodePrice)
                else if (contentTypeChecker.isContentRentedByUser(appCMSPresenter.appPreference.getUserPurchases(), contentDatum.season[seasonCounter].episodes[episodeCounter].gist.id)
                        || contentTypeChecker.isContentRentedByUser(appCMSPresenter.appPreference.getUserPurchases(), contentDatum.season[seasonCounter].id)
                        || contentTypeChecker.isContentRentedByUser(appCMSPresenter.appPreference.getUserPurchases(), contentDatum.gist.id))
                    setTvodPrice(appCMSPresenter.localisedStrings.rentedLabel, episodePrice)
            }
        }

        if (contentDatum.season.size == 1) {
            seasonUp.setColorFilter(CMSColorUtils.lightenColor(Color.parseColor(appCMSPresenter.appCtaTextColor)))
            seasonDown.setColorFilter(CMSColorUtils.lightenColor(Color.parseColor(appCMSPresenter.appCtaTextColor)))
        }

        if (contentDatum.season[seasonCounter].episodes.size == 1) {
            episodeUp.setColorFilter(CMSColorUtils.lightenColor(Color.parseColor(appCMSPresenter.appCtaTextColor)))
            episodeDown.setColorFilter(CMSColorUtils.lightenColor(Color.parseColor(appCMSPresenter.appCtaTextColor)))
        }
        /*if (contentDatum.season.size == 1)
            seasonChooser.gone()
        else
            seasonChooser.visible()
        if (contentDatum.season[seasonCounter].episodes.size == 1)
            episodeChooser.gone()
        else
            episodeChooser.visible()*/
    }

    private fun setTvodPrice(price: String, priceView: AppCompatTextView) {
        priceView.text = price
    }

    /** Expand the button if only 1 is visible
     * @param button - button to expand
     * @param layout - ConstraintLayout to which the button is added
     **/
    private fun expandButton(button: AppCompatButton, layout: ConstraintLayout) {
        /*val params = LinearLayoutCompat.LayoutParams(LinearLayoutCompat.LayoutParams.MATCH_PARENT, LinearLayoutCompat.LayoutParams.WRAP_CONTENT)
        button.layoutParams=params*/
        val constraintSet = ConstraintSet()
        constraintSet.clone(layout)
        constraintSet.connect(button.id, ConstraintSet.START, ConstraintSet.PARENT_ID, ConstraintSet.START, 0)
        constraintSet.connect(button.id, ConstraintSet.END, ConstraintSet.PARENT_ID, ConstraintSet.END, 0)
        constraintSet.connect(button.id, ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.TOP, 0)
        constraintSet.applyTo(layout)
    }

    private fun resetViews() {
        if (tvodPurchaseOptions.visibility == View.VISIBLE || bundlePurchaseOptions.visibility == View.VISIBLE || seriesInformation.visibility == View.VISIBLE) {
            tvodPurchaseOptions.gone()
            bundlePurchaseOptions.gone()
            seriesInformation.gone()
            waysToWatchOptions.visible()
        }
        entitlementButton1.visible()
        entitlementButton2.visible()
        entitlementButton3.visible()
        entitlementButton4.visible()

        val constraintSet = ConstraintSet()
        constraintSet.clone(tveSvodButtons)
        constraintSet.connect(entitlementButton3.id, ConstraintSet.START, ConstraintSet.PARENT_ID, ConstraintSet.START, 0)
        constraintSet.connect(entitlementButton3.id, ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.TOP, 0)
        constraintSet.connect(entitlementButton3.id, ConstraintSet.END, verticalGuideline.id, ConstraintSet.END, 10)

        constraintSet.connect(entitlementButton4.id, ConstraintSet.END, ConstraintSet.PARENT_ID, ConstraintSet.END, 0)
        constraintSet.connect(entitlementButton4.id, ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.TOP, 0)
        constraintSet.connect(entitlementButton4.id, ConstraintSet.START, verticalGuideline.id, ConstraintSet.END, 10)

        constraintSet.applyTo(tveSvodButtons)

        val constraintSetTvod = ConstraintSet()
        constraintSetTvod.clone(tvodButtons)
        constraintSetTvod.connect(entitlementButton1.id, ConstraintSet.START, ConstraintSet.PARENT_ID, ConstraintSet.START, 0)
        constraintSetTvod.connect(entitlementButton1.id, ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.TOP, 0)
        constraintSetTvod.connect(entitlementButton1.id, ConstraintSet.END, verticalGuidelineTvod.id, ConstraintSet.END, 10)

        constraintSetTvod.connect(entitlementButton2.id, ConstraintSet.END, ConstraintSet.PARENT_ID, ConstraintSet.END, 0)
        constraintSetTvod.connect(entitlementButton2.id, ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.TOP, 0)
        constraintSetTvod.connect(entitlementButton2.id, ConstraintSet.START, verticalGuidelineTvod.id, ConstraintSet.END, 10)

        constraintSetTvod.applyTo(tvodButtons)

    }

    private fun resetSeasonEpisodeCounter() {
        if (videoContentDatum.moduleApi.contentData[0].season != null) {
            for (i in videoContentDatum.moduleApi.contentData[0].season.indices) {
                for (j in videoContentDatum.moduleApi.contentData[0].season[i].episodes.indices) {
                    if (videoContentDatum.gist.id.equals(videoContentDatum.moduleApi.contentData[0].season[i].episodes[j].gist.id)) {
                        episodeCounter = j
                        seasonCounter = i
                    }
                }
            }
            if (videoContentDatum.moduleApi.contentData[0].season.size == seasonCounter + 1) {
                seasonUp.setColorFilter(CMSColorUtils.lightenColor(Color.parseColor(appCMSPresenter.appCtaTextColor)))
                seasonDown.setColorFilter(Color.parseColor(appCMSPresenter.appCtaTextColor))
            }
            if (seasonCounter == 0) {
                seasonDown.setColorFilter(CMSColorUtils.lightenColor(Color.parseColor(appCMSPresenter.appCtaTextColor)))
                seasonUp.setColorFilter(Color.parseColor(appCMSPresenter.appCtaTextColor))
            }
            if (episodeCounter == 0) {
                episodeDown.setColorFilter(CMSColorUtils.lightenColor(Color.parseColor(appCMSPresenter.appCtaTextColor)))
                episodeUp.setColorFilter(Color.parseColor(appCMSPresenter.appCtaTextColor))
            }
            if (videoContentDatum.moduleApi.contentData[0].season[seasonCounter].episodes.size == episodeCounter + 1) {
                episodeUp.setColorFilter(CMSColorUtils.lightenColor(Color.parseColor(appCMSPresenter.appCtaTextColor)))
                episodeDown.setColorFilter(Color.parseColor(appCMSPresenter.appCtaTextColor))
            }

        }
    }


    /** Checks if the sheet is expanded
     * @return true if visible
     **/
    fun isVisible(): Boolean {
        val waysToWatch = sheetBehavior
        if (waysToWatch != null && waysToWatch.state == BottomSheetBehavior.STATE_EXPANDED)
            return true
        return false
    }

    fun removeSheet() {
        val waysToWatch = sheetBehavior
        if (waysToWatch != null)
            waysToWatch.setState(BottomSheetBehavior.STATE_COLLAPSED)
    }


    private fun expandSheet() {
        val waysToWatch = sheetBehavior
        if (waysToWatch != null && waysToWatch.state != BottomSheetBehavior.STATE_EXPANDED && !appCMSPresenter.isPageLoginPage(appCMSPresenter.currentAppCMSBinder.pageId)) {
            waysToWatch.setState(BottomSheetBehavior.STATE_EXPANDED)
        }
    }

    /** Called on click info icon for series to show series price along season name and number of episodes
     * @param contentDatum - series data object
     * */
    private fun createSeasonEpisodeView(contentDatum: ContentDatum) {
        parentLayout.setBackgroundColor(ViewCreator.getTransparentColor(appCMSPresenter.generalBackgroundColor, 0.93f))
        seriesInformationBackButton.getCompoundDrawables()[0].setTint(appCMSPresenter.generalTextColor)
        if (rent) {
            contentTypeChecker.tvodPlan(contentDatum.seriesPlans)?.let { contentTypeChecker.fetchTvodRentPrice(it) }?.let {
                seriesPriceWithType.text = appCMSPresenter.localisedStrings.rentLabel.plus(" ").plus(it)
            }
        } else {
            contentTypeChecker.tvodPlan(contentDatum.seriesPlans)?.let { contentTypeChecker.fetchTvodBuyPrice(it) }?.let {
                seriesPriceWithType.text = appCMSPresenter.localisedStrings.buyLabel.plus(" ").plus(it)
            }
        }
        seasonTitleWithEpisodesLayout.removeAllViews()
        for (i in contentDatum.season.indices) {
            val seasonTitle = AppCompatTextView(appCMSPresenter.currentContext)
            val seasonParams = LinearLayoutCompat.LayoutParams(LinearLayoutCompat.LayoutParams.WRAP_CONTENT, LinearLayoutCompat.LayoutParams.WRAP_CONTENT)
            seasonParams.setMargins(0, 12, 0, 0)
            seasonTitle.layoutParams = seasonParams
            seasonTitle.text = contentDatum.season[i].title
            seasonTitle.setTextColor(appCMSPresenter.generalTextColor)
            seasonTitle.textSize = 16F
            ViewCreator.setTypeFace(parentLayout.context, appCMSPresenter, appCMSPresenter.jsonValueKeyMap, Component(), seasonTitle)
            seasonTitleWithEpisodesLayout.addView(seasonTitle)

            val epiosedCount = AppCompatTextView(appCMSPresenter.currentContext)
            val episodeParams = LinearLayoutCompat.LayoutParams(LinearLayoutCompat.LayoutParams.WRAP_CONTENT, LinearLayoutCompat.LayoutParams.WRAP_CONTENT)
            episodeParams.setMargins(0, 4, 0, 0)
            epiosedCount.layoutParams = episodeParams
            val numEpisodes = contentDatum.season[i].episodes.size
            epiosedCount.text = "$numEpisodes".plus(" ").plus(appCMSPresenter.localisedStrings.episodesHeaderText)
            epiosedCount.setTextColor(ContextCompat.getColor(appCMSPresenter.currentContext, android.R.color.darker_gray))
            epiosedCount.textSize = 14F
            ViewCreator.setTypeFace(parentLayout.context, appCMSPresenter, appCMSPresenter.jsonValueKeyMap, Component(), epiosedCount)
            seasonTitleWithEpisodesLayout.addView(epiosedCount)

        }
    }


}