package com.viewlift.views.fragments

import android.content.DialogInterface
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.*
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.Nullable
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import com.viewlift.AppCMSApplication
import com.viewlift.R
import com.viewlift.models.data.appcms.ui.main.LocalisedStrings
import com.viewlift.models.data.appcms.ui.page.Component
import com.viewlift.presenters.AppCMSPresenter
import com.viewlift.views.binders.AppCMSBinder
import com.viewlift.views.customviews.ViewCreator
import com.viewlift.views.dialog.CustomShape


class AppCMSRetryPromptFragment : DialogFragment() {
    private var btnRetry: Button? = null
    private var btnBackToHome: Button? = null
    private var transactionFailedTxt: TextView? = null
    private var transactionFailedDescTxt: TextView? = null
    private var annualPlanTxt: TextView? = null
    private var childConstraint: ConstraintLayout? = null
    private var retry_IMG: ImageView? = null
    private val appCMSBinder: AppCMSBinder? = null
    private lateinit var appCMSPresenter: AppCMSPresenter
    private lateinit var localisedStrings: LocalisedStrings

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NO_TITLE, 0)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        this.dialog?.setCanceledOnTouchOutside(false)
        appCMSPresenter = (requireActivity().application as AppCMSApplication)
                .appCMSPresenterComponent
                .appCMSPresenter()
        appCMSPresenter.setAppOrientation()
        localisedStrings = (requireActivity().application as AppCMSApplication)
                .appCMSPresenterComponent
                .localisedStrings()
        return inflater.inflate(R.layout.retry_promt_layout, container)

    }

    override fun onViewCreated(view: View, @Nullable savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dialog?.window?.setBackgroundDrawableResource(R.drawable.rounded_layout);
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT));
        btnRetry = view.findViewById<View>(R.id.btnRetry) as Button
        childConstraint = view.findViewById<View>(R.id.childConstraint) as ConstraintLayout
        btnBackToHome = view.findViewById<View>(R.id.btnBackToHome) as Button
        childConstraint?.setBackground(CustomShape.createRoundedRectangleDrawable(Color.parseColor(appCMSPresenter.appBackgroundColor)))
        btnRetry?.setBackground(CustomShape.createRoundedRectangleDrawable(appCMSPresenter.brandPrimaryCtaColor))
        CustomShape.makeRoundCorner(ContextCompat.getColor(requireContext(), android.R.color.transparent), 7, btnBackToHome, 2, appCMSPresenter.brandPrimaryCtaColor)
        ViewCreator.setTypeFace(context, appCMSPresenter, appCMSPresenter.jsonValueKeyMap, Component(), btnBackToHome)
        transactionFailedTxt = view.findViewById<View>(R.id.transactionFailedTxt) as TextView
        transactionFailedDescTxt = view.findViewById<View>(R.id.transactionFailedDescTxt) as TextView
        annualPlanTxt = view.findViewById<View>(R.id.annualPlanTxt) as TextView
        val title = arguments?.getString("title", "Enter Name")
        this.activity?.overridePendingTransition(R.anim.slide_in_up, R.anim.slide_out_up)
        dialog!!.setTitle(title)
        dialog!!.setCancelable(false)
        dialog!!.window!!.setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE)
        //retry_IMG?.setImageResource(R.drawable.payerror)
        transactionFailedTxt?.setText(localisedStrings.failMessageTitleText)
        transactionFailedTxt?.setTextColor(appCMSPresenter.brandPrimaryCtaColor)
        transactionFailedDescTxt?.setText(localisedStrings.failText)
        annualPlanTxt?.setText(localisedStrings.toPayText + " " + appCMSPresenter.getSelectedPlanPrice() + " (" + appCMSPresenter.getSelectedPlanValue() + ")")
        btnRetry?.setText(localisedStrings.retryButtonText)
        btnBackToHome?.setText(localisedStrings.backToHomeButtonText)
        btnRetry?.setTextColor(appCMSPresenter.brandPrimaryCtaTextColor)
        btnBackToHome?.setTextColor(appCMSPresenter.brandPrimaryCtaTextColor)
        btnRetry?.setOnClickListener {
            //appCMSPresenter?.finalizeSignupAfterCCAvenueSubscription(null)
            this.dismiss();
        }

        btnBackToHome?.setOnClickListener {
            this.dismiss()
            appCMSPresenter?.navigateToHomePage(true)
            if (appCMSPresenter.launchType == AppCMSPresenter.LaunchType.SUBSCRIBE) {
                Handler(Looper.getMainLooper()).postDelayed({ appCMSPresenter.showPersonalizationscreenIfEnabled(false , true) }, 1000L)
            }
        }


        // handleDialog()
    }


    fun handleDialog() {
        Handler(Looper.getMainLooper()).postDelayed({
            /* Create an Intent that will start the Menu-Activity. */
            dialog!!.dismiss()
            appCMSPresenter.navigateToHomePage(true)
        }, 20000)
    }

    override fun onResume() {
        super.onResume()
        dialog?.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
        dialog?.window?.setBackgroundDrawable(GradientDrawable().apply {
            setColor((Color.TRANSPARENT))
        })

        dialog!!.setOnKeyListener(object : DialogInterface.OnKeyListener {
            override fun onKey(dialog: DialogInterface?, keyCode: Int, event: KeyEvent?): Boolean {
                return if (keyCode == KeyEvent.KEYCODE_BACK) {
                    return true
                } else false // pass on to be processed as normal
            }
        })
    }

    companion object {
        @JvmStatic
        fun newInstance(title: String?): AppCMSRetryPromptFragment {
            val frag = AppCMSRetryPromptFragment()
            val args = Bundle()
            args.putString("title", title)
            frag.arguments = args
            return frag
        }
    }
}