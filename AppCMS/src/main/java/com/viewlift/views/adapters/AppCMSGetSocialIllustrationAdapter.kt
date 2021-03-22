package com.viewlift.views.adapters

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.viewlift.R
import com.viewlift.extensions.inflate
import com.viewlift.extensions.loadUrl
import com.viewlift.models.data.appcms.api.MetadataMap
import kotlinx.android.synthetic.main.getsocial_illustration_adapter.view.*

class AppCMSGetSocialIllustrationAdapter(private val items: List<MetadataMap.Item>) : RecyclerView.Adapter<AppCMSGetSocialIllustrationAdapter.ViewHolder>() {

    override fun getItemCount() = items.size
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewHolder(parent.inflate(R.layout.getsocial_illustration_adapter))

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.itemView.getSocialOfferImg.loadUrl(items[position].imageUrl)
        holder.itemView.getSocialOfferTV.text = items[position].description
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
}
