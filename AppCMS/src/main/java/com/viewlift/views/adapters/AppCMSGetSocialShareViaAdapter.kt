package com.viewlift.views.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.viewlift.databinding.GetSocialShareviaAdapterBinding
import com.viewlift.presenters.AppCMSPresenter
import com.viewlift.utils.GetSocialHelper
import im.getsocial.sdk.invites.InviteChannel


class AppCMSGetSocialShareViaAdapter(var context: Context?, private val inviteChannels: List<InviteChannel>, var appCMSPresenter: AppCMSPresenter) : RecyclerView.Adapter<AppCMSGetSocialShareViaAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

       // view.setBackgroundColor(appCMSPresenter.generalBackgroundColor)
        val inflater = LayoutInflater.from(parent.context)
        val binding = GetSocialShareviaAdapterBinding.inflate(inflater)
        return ViewHolder(binding)

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.appCmsGetsocialShareviaText.text = inviteChannels[position].channelName
        if (inviteChannels[position].iconImageUrl != null) {
            Glide.with(holder.binding.appCmsGetsocialShareviaImage.context).load(inviteChannels[position].iconImageUrl).into(holder.binding.appCmsGetsocialShareviaImage)
            holder.binding.appCmsGetsocialShareviaImage.visibility = View.VISIBLE
        } else {
            holder.binding.appCmsGetsocialShareviaImage.visibility = View.INVISIBLE
        }
        holder.itemView.setOnClickListener {
            GetSocialHelper.openChannel(inviteChannels[position].channelId,appCMSPresenter)


        }
    }

    override fun getItemCount(): Int {
        return inviteChannels.size
    }

    class ViewHolder(val binding: GetSocialShareviaAdapterBinding) : RecyclerView.ViewHolder(binding.root)

}


