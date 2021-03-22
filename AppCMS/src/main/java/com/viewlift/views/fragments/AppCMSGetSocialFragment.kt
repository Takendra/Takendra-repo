package com.viewlift.views.fragments

import android.content.Context
import android.graphics.Paint
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.viewlift.AppCMSApplication
import com.viewlift.R
import com.viewlift.extensions.gone
import com.viewlift.extensions.setTypeFace
import com.viewlift.extensions.visible
import com.viewlift.models.data.appcms.api.MetadataMap
import com.viewlift.models.data.appcms.ui.page.Component
import com.viewlift.views.adapters.AppCMSGetSocialIllustrationAdapter
import com.viewlift.views.adapters.AppCMSGetSocialShareViaAdapter
import com.viewlift.views.binders.AppCMSBinder
import com.viewlift.views.customviews.BaseView
import com.viewlift.views.dialog.CustomShape
import com.viewlift.views.dialog.GetSocialReferredUsersDialog
import im.getsocial.sdk.Invites
import kotlinx.android.synthetic.main.fragment_getsocial.*


class AppCMSGetSocialFragment : Fragment(R.layout.fragment_getsocial) {

    private var metadataMap: MetadataMap? = null

    private val appCMSPresenter by lazy {
        (requireActivity().applicationContext as AppCMSApplication).appCMSPresenterComponent.appCMSPresenter()
    }

    private val localisedStrings by lazy {
        appCMSPresenter.localisedStrings
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val appCMSBinder = arguments?.getBinder(context?.getString(R.string.fragment_page_bundle_key)) as? AppCMSBinder?
        val module = appCMSPresenter.matchModuleAPIToModuleUI(appCMSPresenter.getRelatedModuleForBlock(appCMSBinder?.appCMSPageUI?.moduleList, getString(R.string.ui_block_refer_and_earn_01)), appCMSBinder!!.appCMSPageAPI)
        metadataMap = module?.metadataMap


        getSocialIllustrationList.adapter = AppCMSGetSocialIllustrationAdapter(metadataMap?.items
                ?: emptyList())
        getSocialIllustrationList.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)

        getSocialPageTitle.text = metadataMap?.getSocialFreeMessage
                ?: localisedStrings.socialFreeMessage
        getSocialReferralStatus.text = metadataMap?.getSocialReferredFriendsLink
                ?: getString(R.string.get_social_referred_friends_link)
        getSocialShareViaTV.text = metadataMap?.getSocialShareViaText
                ?: localisedStrings.socialShareviaText
        getSocialTermsAndConditionsTV.gone()
        getSocialShareViaTV.typeface = appCMSPresenter.boldTypeFace
        getSocialReferralStatus.setOnClickListener {
            GetSocialReferredUsersDialog.newInstance().show(childFragmentManager, "DIALOG")
        }
        getSocialTermsAndConditionsTV.setOnClickListener {
            if (metadataMap?.socialTermsAndConditions != null) {
                val newUrl = (metadataMap?.getSocialTermsAndConditionsPageURL.toString()).replace("\"", "")
                val finalURL = "$newUrl?app=true"
                appCMSPresenter.openChromeTab(finalURL)
            }
        }



        when {
            appCMSPresenter.isUserSubscribed -> {
                onSubscribed()
            }
            else -> {
                onUnsubscribed()
            }
        }
        getSocialSignInButtonText.setOnClickListener {
            if (appCMSPresenter.isUserLoggedIn) {
                appCMSPresenter.navigateToSubscriptionPlansPage(false)
            } else {
                appCMSPresenter.navigateToLoginPage(false)
            }
        }

        if (BaseView.isTablet(context)) {
            getSocialShareViaTV.textAlignment = View.TEXT_ALIGNMENT_CENTER
        } else {
            getSocialShareViaTV.textAlignment = View.TEXT_ALIGNMENT_TEXT_START
        }

        setViewStyles()

    }

    private fun setViewStyles() {
        val textColor = appCMSPresenter.generalTextColor
        getSocialPageTitle.setTextColor(textColor)
        getSocialShareViaTV.setTextColor(textColor)
        getSocialReferralStatus.setTextColor(appCMSPresenter.blockTitleTextColor)
        getSocialTermsAndConditionsTV.setTextColor(appCMSPresenter.blockTitleColor)
        val component = Component().apply { fontWeight = getString(R.string.app_cms_page_font_bold_key) }
        getSocialShareViaTV.setTypeFace(appCMSPresenter, component)
        getSocialReferralStatus.setTypeFace(appCMSPresenter, component)
        getSocialReferralStatus.paintFlags = getSocialReferralStatus.paintFlags or Paint.UNDERLINE_TEXT_FLAG
        getSocialTermsAndConditionsTV.setTypeFace(appCMSPresenter, component)
        getSocialTermsAndConditionsTV.paintFlags = getSocialReferralStatus.paintFlags or Paint.UNDERLINE_TEXT_FLAG
    }

    private fun onSubscribed() {
        getSocialPageTitle.visible()
        getSocialSignInButtonText.gone()
        getSocialShareViaTV.visible()
        getSocialShareViaList.visible()
        getSocialReferralStatus.visible()
        getSocialTermsAndConditionsTV.visible()
        getSocialTermsAndConditionsTV.text = metadataMap?.socialTermsAndConditions
                ?: localisedStrings.socialTermsAndConditions
        getSocialShareViaList.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        Invites.getAvailableChannels({ channels ->
            getSocialShareViaList.adapter = AppCMSGetSocialShareViaAdapter(context, channels, appCMSPresenter)
        }, {
            Log.d(TAG, "Failed to get channels : $it")
        })
    }

    private fun onUnsubscribed() {
        getSocialPageTitle.visible()
        getSocialSignInButtonText.visible()
        getSocialSignInButtonText.background = CustomShape.createRoundedRectangleDrawable(appCMSPresenter.brandPrimaryCtaColor)
        getSocialSignInButtonText.text = if (appCMSPresenter.isUserLoggedIn) localisedStrings.socialSubscribeButtonText else localisedStrings.socialSignInButtonText
        getSocialShareViaTV.gone()
        getSocialShareViaList.gone()
        getSocialReferralStatus.gone()
        getSocialTermsAndConditionsTV.visible()
        getSocialTermsAndConditionsTV.text = metadataMap?.socialTermsAndConditions
                ?: localisedStrings.socialTermsAndConditions
    }

    companion object {

        private const val TAG = "GetSocial"

        @JvmStatic
        fun newInstance(context: Context, appCMSBinder: AppCMSBinder?) = AppCMSGetSocialFragment().apply {
            arguments = Bundle().apply {
                putBinder(context.getString(R.string.fragment_page_bundle_key), appCMSBinder)
            }
        }
    }
}