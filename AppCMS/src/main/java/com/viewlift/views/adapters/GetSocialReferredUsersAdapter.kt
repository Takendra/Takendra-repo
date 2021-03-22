package com.viewlift.views.adapters

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.viewlift.R
import com.viewlift.extensions.gone
import com.viewlift.extensions.inflate
import com.viewlift.extensions.setTypeFace
import com.viewlift.extensions.visible
import com.viewlift.presenters.AppCMSPresenter
import com.viewlift.utils.CommonUtils
import com.viewlift.utils.GetSocialHelper.KEY_APP_PLATFORM
import com.viewlift.utils.GetSocialHelper.KEY_EMAIL_ID
import com.viewlift.utils.GetSocialHelper.KEY_PHONE
import im.getsocial.sdk.invites.ReferralUser
import kotlinx.android.synthetic.main.item_for_referred_user.view.*

class GetSocialReferredUsersAdapter(private val appCMSPresenter: AppCMSPresenter,
                                    private val users: MutableList<ReferralUser>) : RecyclerView.Adapter<GetSocialReferredUsersAdapter.ViewHolder>() {

    override fun getItemCount() = users.size
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewHolder(parent.inflate(R.layout.item_for_referred_user))
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindView(position)
    }

    fun addAll(newUsers: List<ReferralUser>) {
        users.clear()
        users.addAll(newUsers.filter { it.eventData.containsKey(KEY_EMAIL_ID) && it.eventData.containsKey(KEY_PHONE) })
        notifyDataSetChanged()
    }

    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        init {
            itemView.snTv                 .setTypeFace(appCMSPresenter)
            itemView.referredFriendsTV    .setTypeFace(appCMSPresenter)
            itemView.subscriptionStatusTV .setTypeFace(appCMSPresenter)
            itemView.installChannelTV     .setTypeFace(appCMSPresenter)
            itemView.subscriptionDateTV   .setTypeFace(appCMSPresenter)

            appCMSPresenter.generalTextColor.let {
                itemView.snTv                 .setTextColor(it)
                itemView.referredFriendsTV    .setTextColor(it)
                itemView.mobileTV             .setTextColor(it)
                itemView.subscriptionStatusTV .setTextColor(it)
                itemView.installChannelTV     .setTextColor(it)
                itemView.subscriptionDateTV   .setTextColor(it)
                itemView.separatorView        .setBackgroundColor(it)
            }
        }

        fun bindView(position: Int) {
            itemView.snTv.text = (position + 1).toString()
            users[position].let { user ->
                user.eventData?.let {
                    if (it[KEY_EMAIL_ID]?.isNotBlank() == true) {
                        itemView.referredFriendsTV.text = it[KEY_EMAIL_ID]
                        itemView.referredFriendsTV.visible()
                    } else {
                        itemView.referredFriendsTV.gone()
                    }

                    if (it[KEY_PHONE]?.isNotBlank() == true) {
                        itemView.mobileTV.text = it[KEY_PHONE]
                        itemView.mobileTV.visible()
                    } else {
                        itemView.mobileTV.gone()
                    }

                    itemView.installChannelTV.text     = it[KEY_APP_PLATFORM] ?: ""
                    itemView.subscriptionStatusTV.text = appCMSPresenter.localisedStrings?.getSocialSubscribedText ?: ""
                }

                itemView.subscriptionDateTV.text = CommonUtils.getDateFormatByTimeZone(user.eventDate * 1000, "MMM dd yyyy")
            }
        }
    }
}