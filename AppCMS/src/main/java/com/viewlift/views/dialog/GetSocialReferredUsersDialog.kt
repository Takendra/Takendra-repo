package com.viewlift.views.dialog

import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.viewlift.AppCMSApplication
import com.viewlift.R
import com.viewlift.extensions.gone
import com.viewlift.extensions.setTypeFace
import com.viewlift.extensions.visible
import com.viewlift.models.data.appcms.ui.page.Component
import com.viewlift.utils.GetSocialHelper.USER_SUBSCRIPTION_COMPLETED
import com.viewlift.views.adapters.GetSocialReferredUsersAdapter
import im.getsocial.sdk.Invites
import im.getsocial.sdk.common.PagingQuery
import im.getsocial.sdk.invites.ReferralUser
import im.getsocial.sdk.invites.ReferralUsersQuery
import kotlinx.android.synthetic.main.dialog_get_social_referred_users.*
import kotlinx.android.synthetic.main.item_for_referred_user.view.*
import java.text.DateFormat
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

class GetSocialReferredUsersDialog : DialogFragment() {

    private val appCMSPresenter by lazy {
        (requireActivity().applicationContext as AppCMSApplication).appCMSPresenterComponent.appCMSPresenter()
    }

    private val adapter by lazy { GetSocialReferredUsersAdapter(appCMSPresenter, arrayListOf())}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NO_TITLE, 0)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.dialog_get_social_referred_users, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setViewStyles()
        setUpRecyclerView()
        setLocalisedData()
        getReferredUsers()
        closeIcon.setOnClickListener { dismiss() }
    }

    override fun onResume() {
        super.onResume()
        dialog?.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
        dialog?.window?.setBackgroundDrawable(GradientDrawable().apply {
            setColor(appCMSPresenter.generalBackgroundColor)
        })
    }

    private fun setViewStyles() {
        view?.setBackgroundColor(appCMSPresenter.generalBackgroundColor)
        titleTV.setTextColor(appCMSPresenter.blockTitleTextColor)

        headerView.referredFriendsTV.maxLines = 2

        val component = Component().apply { fontWeight =  getString(R.string.app_cms_page_font_bold_key)}

        titleTV                         .setTypeFace(appCMSPresenter, component)
        noRecordFoundTV                 .setTypeFace(appCMSPresenter, component)
        headerView.snTv                 .setTypeFace(appCMSPresenter, component)
        headerView.referredFriendsTV    .setTypeFace(appCMSPresenter, component)
        headerView.subscriptionStatusTV .setTypeFace(appCMSPresenter, component)
        headerView.installChannelTV     .setTypeFace(appCMSPresenter, component)
        headerView.subscriptionDateTV   .setTypeFace(appCMSPresenter, component)

        appCMSPresenter.generalTextColor.let {
            noRecordFoundTV                 .setTextColor(it)
            headerView.snTv                 .setTextColor(it)
            headerView.referredFriendsTV    .setTextColor(it)
            headerView.mobileTV             .setTextColor(it)
            headerView.subscriptionStatusTV .setTextColor(it)
            headerView.installChannelTV     .setTextColor(it)
            headerView.subscriptionDateTV   .setTextColor(it)
            headerView.separatorView        .setBackgroundColor(it)
            separatorView                   .setBackgroundColor(it)
        }

        try {
            progressBar.indeterminateDrawable.setTint(appCMSPresenter.brandPrimaryCtaColor)
        } catch (e: Exception) {
            progressBar.indeterminateDrawable.setTint(ContextCompat.getColor(context ?: return, R.color.colorAccent))
        }
    }

    private fun setUpRecyclerView() {
        referredUserRV.layoutManager = LinearLayoutManager(context)
        referredUserRV.adapter       = adapter
    }

    private fun setLocalisedData() {
        appCMSPresenter.localisedStrings?.let {
            headerView.snTv                 .text = it.getSocialSerialNumberText
            headerView.referredFriendsTV    .text = it.getSocialReferredFriendsTitle
            headerView.installChannelTV     .text = it.getSocialInstallChannelTitle
            headerView.subscriptionStatusTV .text = it.getSocialSubscriptionStatusTitle
            headerView.subscriptionDateTV   .text = it.getSocialSubscriptionDateTitle
            noRecordFoundTV                 .text = it.getSocialNoRecordFoundText    ?: ""
            titleTV                         .text = it.getSocialReferredFriendsList  ?: ""
        }
    }

    private fun getReferredUsers() {
        progressBar.visible()
        val pagingQuery = PagingQuery(ReferralUsersQuery.usersForEvent(USER_SUBSCRIPTION_COMPLETED))
        Invites.getReferredUsers(pagingQuery, {
            progressBar?.gone()
            if (it.entries.size > 0) {
                referredUserRV?.visible()
                noRecordFoundTV?.gone()
                sortListByDate(it.entries)
                adapter.addAll(it.entries)
            } else {
                referredUserRV?.gone()
                noRecordFoundTV?.visible()
            }
        }, {
            progressBar?.gone()
            noRecordFoundTV?.visible()
        })
    }

    fun sortListByDate(newUsers: List<ReferralUser>?) {
        val f: DateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
        Collections.sort(newUsers) { lhs: ReferralUser?, rhs: ReferralUser? ->
            try {
                if (lhs == null || rhs == null || TextUtils.isEmpty(lhs.eventDate.toString()) || TextUtils.isEmpty(rhs.eventDate.toString()))
                    return@sort 0
                return@sort rhs.eventDate.compareTo(lhs.eventDate)
            } catch (e: ParseException) {
                throw IllegalArgumentException(e)
            }
        }
    }
    companion object {
        @JvmStatic
        fun newInstance() = GetSocialReferredUsersDialog()
    }
}