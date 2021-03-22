package com.viewlift.views.fragments

import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.os.Bundle
import android.text.Html
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.widget.AppCompatImageView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import butterknife.BindView
import butterknife.ButterKnife
import com.viewlift.AppCMSApplication
import com.viewlift.R
import com.viewlift.extensions.gone
import com.viewlift.models.billing.appcms.BillingHistory
import com.viewlift.models.billing.appcms.ItemsBean
import com.viewlift.models.data.appcms.api.MetadataMap
import com.viewlift.utils.CommonUtils
import com.viewlift.views.adapters.AppCMSBillingHistoryAdapter
import java.io.IOException
import java.text.DateFormat
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

class AppCMSBillingHistoryFragment : DialogFragment() {

    private val appCMSPresenter by lazy {
        (requireActivity().applicationContext as AppCMSApplication).appCMSPresenterComponent.appCMSPresenter()
    }


    private var appTextColor = 0
    private var appHeadingColor = 0

    @BindView(R.id.closeDialogButtton)
    lateinit var dialogCloseButton: AppCompatImageView

    @BindView(R.id.progressVerify)
    lateinit var progressVerify: ProgressBar

    @BindView(R.id.recyclerView2)
    lateinit var allBillingHistory: RecyclerView

    @BindView(R.id.title_lb)
    lateinit var title_lb: TextView

    @BindView(R.id.parentLayout)
    lateinit var parentLayout: ConstraintLayout

    @BindView(R.id.detailsDescription)
    lateinit var detailsDescription: TextView

    @BindView(R.id.joinDate)
    lateinit var joinDate: TextView

    @BindView(R.id.lastCharge)
    lateinit var lastCharge: TextView

    @BindView(R.id.nextCharge)
    lateinit var nextCharge: TextView

    @BindView(R.id.plan_name)
    lateinit var planName: TextView

    @BindView(R.id.plan_price)
    lateinit var planPrice: TextView

    private lateinit var metadataMap: MetadataMap
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_billing_history, container, false)
        ButterKnife.bind(this, view)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        metadataMap = requireArguments().getSerializable("metadataMap") as MetadataMap
        appCMSPresenter.restrictPortraitOnly()
        progressVerify.gone()
        setViewStyle()
        try {
            appCMSPresenter.getBillingHistory { updateHistoryResponse: BillingHistory ->
                sortListByDate(updateHistoryResponse.items)
                progressVerify.gone()
                if (updateHistoryResponse.items != null && updateHistoryResponse.items.size > 0) {
                    lastCharge.text = metadataMap.billingHistoryLastChargeLabel.plus(" ").plus(CommonUtils.getDatebyDefaultZone(updateHistoryResponse.items[updateHistoryResponse.items.size - 1].completedAt, "MMMM dd, yyyy"))
                    joinDate.text = metadataMap.billingHistoryJoinDatelabel.plus(" ").plus(CommonUtils.getDatebyDefaultZone(updateHistoryResponse.items[0].subscriptionStartDate, "MMMM dd, yyyy"))
                }
                allBillingHistory.adapter = AppCMSBillingHistoryAdapter(appCMSPresenter, updateHistoryResponse, appHeadingColor, appTextColor, metadataMap)
                allBillingHistory.layoutManager = LinearLayoutManager(appCMSPresenter.currentContext, LinearLayoutManager.VERTICAL, false)
            }
        } catch (e: IOException) {
            e.printStackTrace()
            progressVerify.visibility = View.GONE
        }
        dialogCloseButton.setOnClickListener { dismiss() }
    }

    private fun setViewStyle() {
        appTextColor = appCMSPresenter.generalTextColor
        val appBackgroundColor = appCMSPresenter.generalBackgroundColor
        appHeadingColor = appCMSPresenter.buttonBorderColor
        progressVerify.visibility = View.VISIBLE
        parentLayout.setBackgroundColor(appBackgroundColor)
        planName.text = appCMSPresenter.activeSubscriptionPlanTitle
        if (!appCMSPresenter.appPreference.getUserSubscriptionPlanTitle().isNullOrEmpty())
            planName.text = appCMSPresenter.appPreference.getUserSubscriptionPlanTitle()
        else if (!appCMSPresenter.appPreference.getActiveSubscriptionPlanName().isNullOrEmpty())
            planName.text = appCMSPresenter.appPreference.getActiveSubscriptionPlanName()
        else planName.gone()
        title_lb.setTextColor(appCMSPresenter.getBrandPrimaryCtaColor())
        detailsDescription.setTextColor(appTextColor)
        if (!appCMSPresenter.appPreference.getExistingSubscriptionPlanDescription().isNullOrEmpty())
            detailsDescription.text = Html.fromHtml(appCMSPresenter.appPreference.getExistingSubscriptionPlanDescription())
        else
            detailsDescription.gone()

        joinDate.setTextColor(appTextColor)
        lastCharge.setTextColor(appTextColor)
        nextCharge.setTextColor(appTextColor)
        planName.setTextColor(appCMSPresenter.getBrandPrimaryCtaColor())
        planPrice.setTextColor(appTextColor)

        dialogCloseButton.getDrawable().setColorFilter(PorterDuffColorFilter(appCMSPresenter.generalTextColor, PorterDuff.Mode.SRC_IN))
        dialogCloseButton.setBackgroundColor(ContextCompat.getColor(appCMSPresenter.currentContext, android.R.color.transparent))

        nextCharge.text = metadataMap.billingHistoryNextChargeLabel.plus(" ")
        title_lb.text = metadataMap.billingHistoryButtonText
        if (!appCMSPresenter.appPreference.getActiveSubscriptionPrice().isNullOrEmpty()) {
            planPrice.text = appCMSPresenter.appPreference.getActiveSubscriptionPriceCurrencyCode().plus(" ").plus(appCMSPresenter.appPreference.getActiveSubscriptionPrice()?.toDouble()?.toInt().toString())
        } else planPrice.gone()

        if (appCMSPresenter.appPreference.getActiveSubscriptionEndDate() != null
                && appCMSPresenter.appPreference.getActiveSubscriptionStatus() != null
                && !appCMSPresenter.appPreference.getActiveSubscriptionStatus().equals(getString(R.string.subscription_status_deferred_cancellation), ignoreCase = true)) {
            if (appCMSPresenter.getAppPreference().getActiveSubscriptionEndDate()!!.contains("T")) {
                nextCharge.text = metadataMap.billingHistoryNextChargeLabel.plus(" ").plus(CommonUtils.getDatebyDefaultZone(appCMSPresenter.appPreference.getActiveSubscriptionEndDate(), "MMMM dd, yyyy"))
            } else {
                nextCharge.text = metadataMap.billingHistoryNextChargeLabel.plus(" ").plus(appCMSPresenter.getDateFormat(CommonUtils.getMillisecondFromDateString("MMMM dd, yyyy", appCMSPresenter.appPreference.getActiveSubscriptionEndDate()), "MMMM dd, yyyy"))
            }
        } else {
            nextCharge.visibility = View.GONE
        }
    }

    override fun onResume() {
        super.onResume()
        if (dialog != null && dialog!!.window != null && dialog?.window?.attributes != null) {
            val params = dialog!!.window!!.attributes
            params.width = ViewGroup.LayoutParams.MATCH_PARENT
            params.height = ViewGroup.LayoutParams.MATCH_PARENT
            dialog!!.window!!.attributes = params
        }
    }


    fun sortListByDate(billingHistoryModel: List<ItemsBean?>?) {
        val f: DateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
        Collections.sort(billingHistoryModel) { lhs: ItemsBean?, rhs: ItemsBean? ->
            try {
                if (lhs == null || rhs == null || TextUtils.isEmpty(lhs.completedAt) || TextUtils.isEmpty(rhs.completedAt))
                    return@sort 0
                return@sort f.parse(lhs.completedAt).compareTo(f.parse(rhs.completedAt))
            } catch (e: ParseException) {
                throw IllegalArgumentException(e)
            }
        }
    }

    companion object {
        @JvmStatic
        fun newInstance(metadataMap: MetadataMap?): AppCMSBillingHistoryFragment {
            val args = Bundle()
            args.putSerializable("metadataMap", metadataMap)
            val appCMSBillingHistoryFragment = AppCMSBillingHistoryFragment()
            appCMSBillingHistoryFragment.arguments = args
            return appCMSBillingHistoryFragment
        }
    }
}