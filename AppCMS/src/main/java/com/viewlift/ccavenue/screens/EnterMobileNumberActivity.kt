package com.viewlift.ccavenue.screens

import android.annotation.SuppressLint
import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.PorterDuff
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.InputType
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.viewlift.AppCMSApplication
import com.viewlift.R
import com.viewlift.Utils
import com.viewlift.ccavenue.utility.AvenuesParams
import com.viewlift.extensions.gone
import com.viewlift.extensions.setTypeFace
import com.viewlift.extensions.visible
import com.viewlift.models.data.appcms.AppCMSCarrierBillingRequest
import com.viewlift.models.data.appcms.api.AppCMSPageAPI
import com.viewlift.models.data.appcms.api.MetadataMap
import com.viewlift.models.data.appcms.api.Module
import com.viewlift.models.data.appcms.api.OrderStatus
import com.viewlift.models.data.appcms.ui.AppCMSUIKeyType
import com.viewlift.models.data.appcms.ui.page.Component
import com.viewlift.presenters.AppCMSPresenter
import com.viewlift.utils.CommonUtils
import com.viewlift.views.customviews.InputView
import com.viewlift.views.fragments.AppCMSThankYouFragment
import kotlinx.android.synthetic.main.activity_enter_mobile_number.*
import java.util.*

class EnterMobileNumberActivity : AppCompatActivity() {

    private val appCMSPresenter    by lazy { (application as AppCMSApplication).appCMSPresenterComponent.appCMSPresenter() }
    private val carrierBillingCall by lazy { (application as AppCMSApplication).appCMSPresenterComponent.appCMSCarrierBillingCall }
    private val selectedPlanName   by lazy { intent.getStringExtra("plan_to_purchase_name") }
    private val selectedPlanAmount by lazy { intent.getStringExtra(AvenuesParams.AMOUNT)   }
    private val selectedPlanId     by lazy { intent.getStringExtra(getString(R.string.app_cms_plan_id)) }

    private val selectedPlanCurrency by lazy {
        val currency = intent.getStringExtra(AvenuesParams.CURRENCY)
        if (currency == "INR") "₹" else currency
    }

    private val module: Module? by lazy {
        (intent.getSerializableExtra(getString(R.string.page_content_data)) as? AppCMSPageAPI)?.modules?.find {
            it.moduleType == "ViewPlanModule"
        }
    }

    private val metadataMap: MetadataMap? by lazy { module?.metadataMap }

    private val inputView by lazy {
        InputView(this, appCMSPresenter, AppCMSUIKeyType.PAGE_PHONETEXTFIELD_KEY, null).apply {
            layoutParams = LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
            editText.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_CLASS_PHONE
        }
    }

    private var isOfferCodeApplied = false
    private var offerCode = ""
    private var offerDiscountedPrice = 0.0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_enter_mobile_number)
        containerView.setBackgroundColor(appCMSPresenter.generalBackgroundColor)
        appCMSPresenter.setAppOrientation()
        offerDiscountedPrice=selectedPlanAmount.toDouble()
        if (appCMSPresenter.useJusPay() && !appCMSPresenter.useCarrierBilling()) {
            appCmsAppbarLayout.gone()
            topView.visible()
            planInfoView.visible()
        }
        try {
            planName.text = selectedPlanName
            steps.text = getString(R.string.steps, "1")
            metadataMap?.let { setLocalizedText(it) }
            setViewFont()
            setViewColors()
            setListener()
            inputViewContainer.addView(inputView)
            inputView.hideTitleView()
            if (appCMSPresenter.appPreference?.getLoggedInUserPhone()?.isNotBlank() == true) {
                if (appCMSPresenter.appPreference?.getLoggedInUserPhone()?.equals("null")!!) {
                    inputView.editText.setText("")
                    inputView.editText.isEnabled = true
                    inputView.text = ""
                } else {
                    inputView.text = appCMSPresenter.appPreference.getLoggedInUserPhone()
                    inputView.hideCountryCodeLayout()
                    inputView.editText.isEnabled = false
                }
            }
            id_tv_biling_information!!.text = appCMSPresenter.languageResourcesFile.getUIresource(resources.getString(R.string.please_enter_your_phone_number_key))
            inputView.setHint(appCMSPresenter.languageResourcesFile.getUIresource(resources.getString(R.string.phone_number_key)))
            inputView.setTitle("Phone")
            if (appCMSPresenter.appPreference != null && appCMSPresenter.appPreference.getLoggedInUserPhone() != null) {
                inputView.text = appCMSPresenter.appPreference.getLoggedInUserPhone()
            }
            id_biling_information.text = appCMSPresenter.languageResourcesFile.getUIresource(resources.getString(R.string.mobile_mobile_will_only_be_used))
            when {
                appCMSPresenter.isHoichoiApp -> {
                    id_biling_information.visibility = View.GONE
                    checkoutBtn.text = appCMSPresenter.languageResourcesFile.getUIresource(resources.getString(R.string.continue_button))
                }
                appCMSPresenter.isAhaApp -> {
                    id_biling_information.visibility = View.GONE
                    checkoutBtn.text = appCMSPresenter.languageResourcesFile.getUIresource(resources.getString(R.string.payment_checkout))
                }
                else -> {
                    checkoutBtn.text = appCMSPresenter.languageResourcesFile.getUIresource(resources.getString(R.string.payment_checkout))
                }
            }

        } catch (ex: Exception) {
            ex.printStackTrace()
        }
    }

    @SuppressLint("SetTextI18n")
    private fun setLocalizedText(metadataMap: MetadataMap) {
        addBilling.text = (metadataMap.addBilling ?: getString(R.string.add_billing))
        planAmount.text = (metadataMap.verifyFirstPay
                ?: getString(R.string.verify_first_pay)) + " " + selectedPlanCurrency + selectedPlanAmount + "\n" + (metadataMap.onSeparator
                ?: getString(R.string.on_separator)) + " " + CommonUtils.getDateFormatByTimeZone(System.currentTimeMillis(), "dd MMM YYYY")
        havePromoCode.text = metadataMap.promoHeading ?: getString(R.string.promo_heading)
        addPromo.text = metadataMap.addPromo ?: getString(R.string.add_promo)
        applyPromoBtn.text = metadataMap.applyPromo ?: getString(R.string.apply_promo)
        planSubscriptionTime.text = metadataMap.planSubscription
                ?: getString(R.string.plan_subscription)
        totalAmount.text = metadataMap.total ?: getString(R.string.total)
        discountAmount.text = metadataMap.discountApplied ?: getString(R.string.discount_applied)
        totalBilling.text = metadataMap.totalBilling ?: getString(R.string.total_billing)
        promoCodeEdt.hint = metadataMap.enterPromoCode ?: getString(R.string.enter_promo_code)
    }

    private fun setViewFont() {
        val component = Component().apply { fontWeight = getString(R.string.app_cms_page_font_bold_key) }

        id_tv_biling_information.setTypeFace(appCMSPresenter)
        checkoutBtn.setTypeFace(appCMSPresenter, component)
        applyPromoBtn.setTypeFace(appCMSPresenter, component)
        planName.setTypeFace(appCMSPresenter, component)
        addPromo.setTypeFace(appCMSPresenter)
        id_biling_information.setTypeFace(appCMSPresenter)
        steps.setTypeFace(appCMSPresenter)
        addBilling.setTypeFace(appCMSPresenter)
        planAmount.setTypeFace(appCMSPresenter)
        havePromoCode.setTypeFace(appCMSPresenter)
        discountApplied.setTypeFace(appCMSPresenter)
        planSubscriptionTime.setTypeFace(appCMSPresenter)
        planTimeValue.setTypeFace(appCMSPresenter)
        totalAmount.setTypeFace(appCMSPresenter)
        amount.setTypeFace(appCMSPresenter)
        discountAmount.setTypeFace(appCMSPresenter)
        discount.setTypeFace(appCMSPresenter)
        totalBilling.setTypeFace(appCMSPresenter)
        billing.setTypeFace(appCMSPresenter)
        promoCodeEdt.setTypeFace(appCMSPresenter)
    }

    private fun setViewColors() {
        val colorCode = intent.getIntExtra("color_theme", R.color.colorNavBarText)
        elevated_button_card.setBackgroundColor(colorCode)
        applyPromoBtn.setBackgroundColor(colorCode)
        addPromo.strokeColor = ColorStateList.valueOf(colorCode)

        checkoutBtn.setTextColor(appCMSPresenter.brandPrimaryCtaTextColor)
        applyPromoBtn.setTextColor(appCMSPresenter.brandPrimaryCtaTextColor)
        addPromo.setTextColor(appCMSPresenter.brandPrimaryCtaTextColor)

        promoCodeView.setBackgroundColor(appCMSPresenter.generalTextColor)
        promoCodeEdt.setTextColor(appCMSPresenter.generalBackgroundColor)

        inputView.setBackgroundColor(appCMSPresenter.generalTextColor)
        inputView.editText.setTextColor(appCMSPresenter.generalBackgroundColor)
        inputView.editText.setHintTextColor(appCMSPresenter.generalBackgroundColor)
        inputView.countryCodeEditText?.setTextColor(appCMSPresenter.generalBackgroundColor)
        inputView.countryCodeEditText?.setHintTextColor(appCMSPresenter.generalBackgroundColor)
        inputView.separatorViewCountry?.setBackgroundColor(appCMSPresenter.generalBackgroundColor)
        inputView.separatorViewCountry?.alpha = 0.2f

        checkIcon.setColorFilter(colorCode, PorterDuff.Mode.SRC_IN)

        appCMSPresenter.generalTextColor.let {
            steps.setTextColor(it)
            addBilling.setTextColor(it)
            planName.setTextColor(it)
            planAmount.setTextColor(it)
            havePromoCode.setTextColor(it)
            discountApplied.setTextColor(it)
            planSubscriptionTime.setTextColor(it)
            planTimeValue.setTextColor(it)
            totalAmount.setTextColor(it)
            amount.setTextColor(it)
            discountAmount.setTextColor(it)
            discount.setTextColor(it)
            totalBilling.setTextColor(it)
            billing.setTextColor(it)
        }
    }

    private fun setListener() {
        checkoutBtn.setOnClickListener(::openPaymentPage)
        closeButton.setOnClickListener { finish()
            showPersonalizationOnCancellation() }
        closeIcon.setOnClickListener { finish()
            showPersonalizationOnCancellation()}

        addPromo.setOnClickListener {
            it.gone()
            applyPromoBtn.visible()
            promoCodeView.visible()
            steps.text = getString(R.string.steps, "2")
        }

        applyPromoBtn.setOnClickListener {
            appCMSPresenter.closeSoftKeyboard()
            if (isOfferCodeApplied) removeOfferCode() else validateOfferCode()
        }
    }

    private fun openPaymentPage(button: View) {
        val mobileNumber = inputView.text.trim()
        if (mobileNumber.isNotEmpty()) {
            appCMSPresenter.closeSoftKeyboard()
            if (appCMSPresenter.useCarrierBilling() && mobileNumber.length >= 10) {
                callRobiAndGPDataBundleApi(mobileNumber)
            } else if (appCMSPresenter.useJusPay() && mobileNumber.length >= 10) {
                showLoader()
                if (appCMSPresenter!!.appPreference.getLoggedInUserPhone() != null) {
                    if (appCMSPresenter!!.appPreference.getLoggedInUserPhone().equals("NA", ignoreCase = true) || appCMSPresenter!!.appPreference.getLoggedInUserPhone().equals("", ignoreCase = true) ||
                            appCMSPresenter!!.appPreference.getLoggedInUserPhone().equals(" ", ignoreCase = true)) {
                        appCMSPresenter!!.appPreference.setCheckOutPhoneNumber(mobileNumber)
                    }
                }
                if (appCMSPresenter!!.appPreference.getLoggedInUserPhone() == null) {
                    appCMSPresenter!!.appPreference.setCheckOutPhoneNumber(mobileNumber)
                }

                appCMSPresenter.jusPayUtils.setOfferCode(if (isOfferCodeApplied) offerCode else null)
                appCMSPresenter.setSelectedPlanPrice(offerDiscountedPrice, selectedPlanName)
                appCMSPresenter!!.navigateToJuspayPaymentPage { finish() }
            } else if (appCMSPresenter!!.useCCAvenue() && mobileNumber.length == 10) {
                button.isEnabled = false
                val intent = Intent(this@EnterMobileNumberActivity, WebViewActivity::class.java)
                intent.putExtras(getIntent())
                intent.putExtra("payment_option", "")
                intent.putExtra("orderId", "")
                intent.putExtra("accessCode", "")
                intent.putExtra("merchantID", "")
                intent.putExtra("cancelRedirectURL", "")
                intent.putExtra("rsa_key", "")
                intent.putExtra("billing_tel", mobileNumber)
                startActivity(intent)
                finish()
            } else if (appCMSPresenter!!.useSSLCommerz() && mobileNumber.isNotEmpty()) {
                button.isEnabled = false
                showLoader()
                appCMSPresenter!!.initiateSSLCommerzPurchase(mobileNumber,
                        intent.getStringExtra(getString(R.string.app_cms_plan_id)),
                        intent.getStringExtra("plan_to_purchase_name"), selectedPlanAmount.toDouble(), intent.getStringExtra(AvenuesParams.CURRENCY)) { isSuccess: Boolean ->
                    hideLoader()
                    if (isSuccess) finish()
                }
            } else {
                if (appCMSPresenter!!.useJusPay() || appCMSPresenter!!.useCCAvenue()) Toast.makeText(applicationContext, " Please enter a 10 digit Phone number.", Toast.LENGTH_SHORT).show() else Toast.makeText(applicationContext, " Please enter a 10 or 11 digit Phone number.", Toast.LENGTH_SHORT).show()
            }
        } else {
            if (appCMSPresenter!!.useJusPay() || appCMSPresenter!!.useCCAvenue()) Toast.makeText(applicationContext, " Please enter a 10 digit Phone number.", Toast.LENGTH_SHORT).show() else Toast.makeText(applicationContext, " Please enter a 10 or 11 digit Phone number.", Toast.LENGTH_SHORT).show()
        }
    }

    @SuppressLint("SetTextI18n")
    private fun validateOfferCode() {
        offerCode = promoCodeEdt.text.toString()
        val planId = intent.getStringExtra(getString(R.string.app_cms_plan_id))
        if (offerCode.isNotBlank()) {
            showLoader()
            appCMSPresenter.jusPayUtils.validateOfferCode(offerCode, planId) { pair ->
                val response = pair.first
                val error = pair.second
                if (response != null && response.offerDetails != null && response.offerDetails.reduceCharge != null) {
                    appCMSPresenter.jusPayUtils.calculateDiscount(offerCode, planId) {  pair ->
                        val discountResult = pair.first
                        val discountError = pair.second
                        if (discountResult != null) {
                            planDescriptionGroup.visible()
                            checkIcon.visible()
                            havePromoCode.gone()
                            isOfferCodeApplied = true
                            promoCodeEdt.isEnabled = false
                            offerDiscountedPrice = discountResult.amount ?: 0.0
                            val appliedDiscount = "$selectedPlanCurrency${selectedPlanAmount.toDouble().minus(offerDiscountedPrice)}"

                            if (response.renewalCycleType != null) {
                                planTimeValue.text = "$selectedPlanName (${response.renewalCyclePeriodMultiplier ?: ""} ${response.renewalCycleType ?: ""})"
                            } else {
                                planTimeValue.text = "$selectedPlanName ${response.renewalCyclePeriodMultiplier ?: ""}"
                            }

                            amount.text = "$selectedPlanCurrency$selectedPlanAmount"
                            discount.text = "- $appliedDiscount"
                            billing.text = "$selectedPlanCurrency${discountResult.amount}"
                            applyPromoBtn.text = metadataMap?.removePromo
                                    ?: getString(R.string.remove_promo)
                            total_billingseperator.visible()
                            if (metadataMap?.discountOfApplied?.isNotBlank() == true) {
                                discountApplied.text = String.format(metadataMap!!.discountOfApplied, appliedDiscount)
                            } else {
                                discountApplied.text = getString(R.string.discount_of_applied, appliedDiscount)
                            }

                            steps.text = getString(R.string.steps, "3")

                        }else{
                            if (metadataMap != null) {
                                appCMSPresenter.convertClassToMap(metadataMap)?.get(discountError.code)?.let { value -> showErrorToast(value as String) }
                                        ?: showErrorToast(discountError.message)
                            } else {
                                showErrorToast(discountError.message)
                            }
                        }
                        hideLoader()
                    }

                } else {
                    if (error?.code != null) {
                        if (metadataMap != null) {
                            appCMSPresenter.convertClassToMap(metadataMap)?.get(error.code)?.let { value -> showErrorToast(value as String) }
                                    ?: showErrorToast(error.message)
                        } else {
                            showErrorToast(error.message)
                        }
                    }
                    hideLoader()
                }
            }
        }
    }

    private fun removeOfferCode() {
        planDescriptionGroup.gone()
        checkIcon.gone()
        havePromoCode.visible()
        promoCodeEdt.setText("")
        isOfferCodeApplied = false
        promoCodeEdt.isEnabled = true
        discountApplied.text = ""
        planTimeValue.text = ""
        amount.text = ""
        discount.text = ""
        billing.text = ""
        applyPromoBtn.text = metadataMap?.applyPromo ?: getString(R.string.apply_promo)
        steps.text = getString(R.string.steps, "2")
        total_billingseperator.gone()
    }

    private fun callRobiAndGPDataBundleApi(mobileNumber: String) {
        if (appCMSPresenter != null && appCMSPresenter.appCMSMain != null) {
            if (!appCMSPresenter.isNetworkConnected) {
                appCMSPresenter.showDialog(AppCMSPresenter.DialogType.NETWORK, null, false, null, null, null)
                return
            }
            showLoader()
            val request = AppCMSCarrierBillingRequest(mobileNumber = mobileNumber.replace("+", ""),
                    planId = selectedPlanId, platform = getString(R.string.app_cms_subscription_platform_key))

            carrierBillingCall.call(getString(R.string.app_cms_data_bundle_api_url, appCMSPresenter.appCMSMain.apiBaseUrl,
                    appCMSPresenter.appCMSMain.internalName, CommonUtils.getPlay_Store_Country_Code(appCMSPresenter, Utils.getCountryCode())),
                    appCMSPresenter.apiKey, appCMSPresenter.authToken, request) { response ->
                if (response?.subscribed == true) {
                    onSubscriptionSuccess()
                } else {
                    hideLoader()
                    if (response?.message?.isNotBlank() == true) {
                        appCMSPresenter.showToast(response.message, Toast.LENGTH_LONG)
                    } else {
                        appCMSPresenter.sendEventSubscriptionFailure(null)
                        moveToThankYouPage(OrderStatus())
                    }
                }
            }
        }
    }

    private fun onSubscriptionSuccess() {
        showLoader()
        appCMSPresenter.getUserData {
            hideLoader()
            appCMSPresenter.sendEventSubscriptionSuccess()
            moveToThankYouPage(OrderStatus().apply {
                status = getString(R.string.charged)
            })
        }
    }

    private fun moveToThankYouPage(status: OrderStatus) {
        if (isFinishing || isDestroyed) return
        hideLoader()
        AppCMSThankYouFragment.newInstance(status).show(supportFragmentManager, "AppCMSThankYouFragment")
    }

    private fun showLoader() {
        applyPromoBtn.isEnabled = false
        checkoutBtn.isEnabled = false
        checkoutBtn.alpha = 0.5f
        applyPromoBtn.alpha = 0.5f
        progressBar.visible()
        window.setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
    }

    private fun hideLoader() {
        applyPromoBtn.isEnabled = true
        checkoutBtn.isEnabled = true
        checkoutBtn.alpha = 1f
        applyPromoBtn.alpha = 1f
        progressBar.gone()
        window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
    }

    private fun showErrorToast(msg: String?) {
        if (msg?.isNotBlank() == true)
            appCMSPresenter.showToast(msg, Toast.LENGTH_LONG)
    }

    override fun onDestroy() {
        super.onDestroy()
        appCMSPresenter.closeSoftKeyboard()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        showPersonalizationOnCancellation();
    }

    private fun showPersonalizationOnCancellation() {
        if(appCMSPresenter.launchType == AppCMSPresenter.LaunchType.SUBSCRIBE){
            Handler(Looper.getMainLooper()).postDelayed(Runnable { appCMSPresenter.showPersonalizationscreenIfEnabled(false, true); },1000L);
        }
    }
}