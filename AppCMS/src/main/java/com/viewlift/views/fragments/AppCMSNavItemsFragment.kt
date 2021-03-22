package com.viewlift.views.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.viewlift.AppCMSApplication
import com.viewlift.R
import com.viewlift.databinding.FragmentMenuNavBinding
import com.viewlift.extensions.gone
import com.viewlift.models.data.appcms.ui.main.LocalisedStrings
import com.viewlift.models.data.appcms.ui.page.Component
import com.viewlift.presenters.AppCMSPresenter
import com.viewlift.views.adapters.AppCMSNavItemsAdapter
import com.viewlift.views.binders.AppCMSBinder
import com.viewlift.views.customviews.BaseView
import com.viewlift.views.customviews.ViewCreator
import com.viewlift.views.dialog.CustomShape

/**
 * A simple [Fragment] subclass.
 */
class AppCMSNavItemsFragment : DialogFragment() {
    private var appCMSBinder: AppCMSBinder? = null
    private var appCMSNavItemsAdapter: AppCMSNavItemsAdapter? = null
    var freeTrialButtonText: String = ""
    var loginButtonText: String = ""
    var tveButtonText: String = ""
    var freeTrialButtonTextColor: Int = 0
    var loginButtonTextColor: Int = 0
    var freeTrialButtonBackgroundColor: Int = 0
    var pageBackgroundColor: Int = 0

    private lateinit var appCMSPresenter: AppCMSPresenter
    private lateinit var localisedStrings: LocalisedStrings
    private lateinit var binding: FragmentMenuNavBinding


    companion object {
        @JvmStatic
        fun newInstance(context: Context, appCMSBinder: AppCMSBinder?): AppCMSNavItemsFragment {
            return AppCMSNavItemsFragment().apply {
                arguments = Bundle().apply {
                    putBinder(context.getString(R.string.fragment_page_bundle_key), appCMSBinder)
                }
            }
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding = FragmentMenuNavBinding.inflate(inflater, container, false)
        return binding.getRoot()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.fragmentVar = this
        appCMSPresenter = (requireActivity().application as AppCMSApplication)
                .appCMSPresenterComponent
                .appCMSPresenter()
        appCMSPresenter.setAppOrientation()
        localisedStrings = (requireActivity().application as AppCMSApplication)
                .appCMSPresenterComponent
                .localisedStrings()
        val args = arguments
        loginButtonTextColor = appCMSPresenter.generalTextColor //args?.getInt(resources.getString(R.string.app_cms_text_color_key))!!
        pageBackgroundColor = appCMSPresenter.generalBackgroundColor //args.getInt(resources.getString(R.string.app_cms_bg_color_key))
        val borderColor = appCMSPresenter.brandPrimaryCtaBorderColor //args.getInt(resources.getString(R.string.app_cms_border_color_key))
        try {
            appCMSBinder = args?.getBinder(resources.getString(R.string.fragment_page_bundle_key)) as? AppCMSBinder?
        } catch (e: Exception) {
            e.printStackTrace()
        }
        if (!appCMSPresenter.appCMSMain.features.isLoginModuleEnable)
            binding.appCmsNavLoginButton.gone()
        if (!appCMSPresenter.appCMSMain.features.isSignupModuleEnable)
            binding.appCmsNavSubscribeButton.gone()
        if (appCMSPresenter.navigation != null) {
            setMenuAdapter(loginButtonTextColor)
            freeTrialButtonTextAndClick()
            loginButtonTextAndClick(borderColor)
//            tveLoginTextAndClick(borderColor)
            tvProviderImage()
            handleAuthenticateVisibility()
        }
    }

    fun tvProviderImage() {
        if (appCMSPresenter.isUserLoggedIn) {
            if (appCMSPresenter.appPreference.getTvProviderLogo() != null) {
                Glide.with(this)
                        .load(appCMSPresenter.appPreference.getTvProviderLogo())
                        .into(binding.tvProviderImage)
                binding.tvProviderImage.setVisibility(View.VISIBLE)
            }
        }
    }

    fun handleAuthenticateVisibility() {
        if (appCMSPresenter.isUserLoggedIn) {
            binding.appCmsNavLoginContainer.setVisibility(View.GONE)
            val constraintSet = ConstraintSet()
            constraintSet.clone(binding.parentLayout)
            constraintSet.connect(binding.navItemsList.id, ConstraintSet.END, ConstraintSet.PARENT_ID, ConstraintSet.END, 0)
            constraintSet.applyTo(binding.parentLayout)
//            (binding.appCmsNavItemsMainView.getLayoutParams() as RelativeLayout.LayoutParams).addRule(RelativeLayout.CENTER_IN_PARENT)

        } else {
//            if (!BaseView.isTablet(context)) {
//                (binding.appCmsNavItemsMainView.getLayoutParams() as RelativeLayout.LayoutParams).addRule(RelativeLayout.ALIGN_PARENT_BOTTOM)
//            } else {
//                (binding.appCmsNavItemsMainView.getLayoutParams() as RelativeLayout.LayoutParams).addRule(RelativeLayout.CENTER_IN_PARENT)
//            }
            binding.appCmsNavLoginContainer.setVisibility(View.VISIBLE)
        }
    }

    fun setMenuAdapter(textColor: Int) {
        appCMSNavItemsAdapter = context?.let {
            AppCMSNavItemsAdapter(it, appCMSPresenter.navigation,
                    appCMSPresenter.jsonValueKeyMap,
                    appCMSPresenter.isUserLoggedIn,
                    appCMSPresenter.isUserSubscribed,
                    textColor)
        }
        binding.navItemsList.adapter = appCMSNavItemsAdapter

    }

    fun tveLoginTextAndClick(borderColor: Int) {
        if (appCMSPresenter.appCMSAndroid.tveSettings != null)
            binding.appCmsNavTveLoginButton.visibility = View.VISIBLE
        tveButtonText = localisedStrings.getTveLoginText()
        CustomShape.makeRoundCorner(ContextCompat.getColor(requireContext(), android.R.color.transparent), 7, binding.appCmsNavTveLoginButton, 2, borderColor)
        ViewCreator.setTypeFace(context, appCMSPresenter, appCMSPresenter.jsonValueKeyMap, Component(), binding.appCmsNavTveLoginButton)
        binding.appCmsNavTveLoginButton.setOnClickListener {
            appCMSPresenter.loginFromNavPage = true
            appCMSPresenter.openTvProviderScreen()
        }
    }

    fun loginButtonTextAndClick(borderColor: Int) {
        loginButtonText = resources.getString(R.string.app_cms_log_in_pager_title)
        if (appCMSPresenter.navigation != null && appCMSPresenter.navigation.settings != null &&
                appCMSPresenter.navigation.settings.primaryCta != null && appCMSPresenter.navigation.settings.primaryCta.loginCtaText != null) {
            loginButtonText = appCMSPresenter.localizedLoginText
        }
        CustomShape.makeRoundCorner(ContextCompat.getColor(requireContext(), android.R.color.transparent), 7, binding.appCmsNavLoginButton, 2, borderColor)
        ViewCreator.setTypeFace(context, appCMSPresenter, appCMSPresenter.jsonValueKeyMap, Component(), binding.appCmsNavLoginButton)
        binding.appCmsNavLoginButton.setOnClickListener {
            //   appCMSPresenter.navigateToReferralPage();
            appCMSPresenter.launchType = AppCMSPresenter.LaunchType.LOGIN_AND_SIGNUP
            appCMSPresenter.navigateToLoginPage(true)
            appCMSPresenter.firebaseAnalytics.screenViewEvent(getString(R.string.value_login_screen))
            if (appCMSPresenter.isUserLoggedIn) {
                appCMSPresenter.firebaseAnalytics.userPropertyLoginStatus(getString(R.string.status_logged_in))
            } else {
                appCMSPresenter.firebaseAnalytics.userPropertyLoginStatus(getString(R.string.status_logged_out))
            }
        }

    }

    fun freeTrialButtonTextAndClick() {
        freeTrialButtonTextColor = appCMSPresenter.getBrandPrimaryCtaTextColor()
        freeTrialButtonBackgroundColor = appCMSPresenter.getBrandPrimaryCtaColor()
        freeTrialButtonText = resources.getString(R.string.app_cms_subscribe_now)
        if (appCMSPresenter.appCMSMain.serviceType.equals(resources.getString(R.string.app_cms_main_svod_service_type_key))) {
            if (appCMSPresenter.navigation != null && appCMSPresenter.navigation.settings != null
                    && appCMSPresenter.navigation.settings.primaryCta != null && appCMSPresenter.navigation.settings.primaryCta.ctaText != null) {
                if (appCMSPresenter.isShowDialogForWebPurchase)
                    freeTrialButtonText = localisedStrings.getSignUpText()
                else {
                    val localizedCtaText = appCMSPresenter.getLocalizedCtaText(appCMSPresenter.navigation.settings.getLocalizationMap(), appCMSPresenter.navigation.settings.primaryCta, false)
                    freeTrialButtonText = localizedCtaText
                            ?: appCMSPresenter.navigation.settings.primaryCta.ctaText
                }
            } else
                freeTrialButtonText = localisedStrings.getSignUpText()
        } else
            freeTrialButtonText = localisedStrings.getSignUpText()

        ViewCreator.setTypeFace(context, appCMSPresenter, appCMSPresenter.jsonValueKeyMap, Component(), binding.appCmsNavSubscribeButton)
        binding.appCmsNavSubscribeButton.setOnClickListener {
            if (appCMSPresenter.isAppSVOD) {
                if (appCMSPresenter.isShowDialogForWebPurchase) {
                    appCMSPresenter.launchType = AppCMSPresenter.LaunchType.SIGNUP
                    appCMSPresenter.navigateToLoginPage(false)
                } else {
                    appCMSPresenter.launchType = AppCMSPresenter.LaunchType.SUBSCRIBE
                    appCMSPresenter.navigateToSubscriptionPlansPage(true)
                }
            } else {
                appCMSPresenter.launchType = AppCMSPresenter.LaunchType.SIGNUP
                appCMSPresenter.navigateToLoginPage(false)
            }
        }
    }

    override fun onResume() {
        super.onResume()
        try {
            appCMSNavItemsAdapter?.userLoggedIn = appCMSPresenter.isUserLoggedIn
            appCMSNavItemsAdapter?.userSubscribed = appCMSPresenter.isUserSubscribed
            appCMSNavItemsAdapter?.resetMenu(appCMSPresenter.navigation)
            if (appCMSPresenter.relativeLayoutPIP != null) {
                if (appCMSPresenter.isMiniPlayerPlaying) {
                    appCMSPresenter.showPopupWindowPlayer(true)
                } else appCMSPresenter.showPopupWindowPlayer(false)
            }
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        appCMSPresenter.setAppOrientation()
    }

}