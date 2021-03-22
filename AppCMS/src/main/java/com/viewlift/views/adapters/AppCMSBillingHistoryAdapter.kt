package com.viewlift.views.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import butterknife.BindView
import butterknife.ButterKnife
import com.viewlift.R
import com.viewlift.models.billing.appcms.BillingHistory
import com.viewlift.models.data.appcms.api.MetadataMap
import com.viewlift.presenters.AppCMSPresenter
import com.viewlift.utils.CommonUtils

class AppCMSBillingHistoryAdapter(private val appCMSPresenter: AppCMSPresenter, private val billingHistoryModel: BillingHistory, var headingColor: Int, var textColor: Int, private val metadataMap: MetadataMap?) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        if (viewType == HEADER_VIEW) {
            return HeaderViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.billing_history_adapter_header_file, parent, false))
        } else if (viewType == NORMAL_VIEW) {
            return CustomViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.billing_history_adapter_file, parent, false))
        }
        return CustomViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.billing_history_adapter_file, parent, false))
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holderx: RecyclerView.ViewHolder, position: Int) {
        try {
            if (holderx is HeaderViewHolder) {
                val vh = holderx
                vh.dateLblHeader!!.setTextColor(textColor)
                vh.appliedOffersHeader!!.setTextColor(textColor)
                vh.descriptionHeader!!.setTextColor(textColor)
                vh.servicePeriodHeader!!.setTextColor(textColor)
                vh.totalHeader!!.setTextColor(textColor)
                vh.titleLblHeader!!.setTextColor(textColor)
                if (metadataMap != null) {
                    vh.dateLblHeader!!.text = metadataMap.billingHistoryTableDate
                    vh.appliedOffersHeader!!.text = metadataMap.billingHistoryTableOffers
                    vh.descriptionHeader!!.text = metadataMap.billingHistoryTableDescription
                    vh.servicePeriodHeader!!.text = metadataMap.billingHistoryTableServicePeriod
                    vh.totalHeader!!.text = metadataMap.billingHistoryTableTotal
                    vh.titleLblHeader!!.text = metadataMap.billingHistoryTableTitle
                }
            } else if (holderx is CustomViewHolder) {
                val holder = holderx
                val itemsBean = billingHistoryModel.items!![billingHistoryModel.items.size - position]

                holder.dateLbl!!.setTextColor(textColor)
                holder.titleLbl!!.setTextColor(textColor)
                holder.servicePeriod!!.setTextColor(textColor)
                holder.description!!.setTextColor(textColor)
                holder.appliedOffers!!.setTextColor(textColor)
                holder.total!!.setTextColor(textColor)

                var description: String? = "N/A"
                description = itemsBean.transactiontype
                if (itemsBean.purchaseType != null) {
                    description = itemsBean.purchaseType
                }
                var servicePeriodVal = ""
                try {

                    if (itemsBean.subscriptionStartDate != null && itemsBean.subscriptionEndDate != null && itemsBean.purchaseType==null) {

                        servicePeriodVal = """
                    ${appCMSPresenter.getDateFormat(CommonUtils.getMillisecondFromDateString("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", itemsBean.subscriptionStartDate), "MMMM dd, yyyy")}
                    ${appCMSPresenter.getDateFormat(CommonUtils.getMillisecondFromDateString("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", itemsBean.subscriptionEndDate), "MMMM dd, yyyy")}
                    """.trimIndent()
                        holder.description!!.text = itemsBean.transactiontype
                        holder.appliedOffers!!.text = if (itemsBean.offerName != null) itemsBean.offerName else "NA"
                    } else {
                        servicePeriodVal = "N/A"
                    }
                } catch (e: Exception) {
                    servicePeriodVal = "N/A"
                }

                if (itemsBean.completedAt != null) {
                    holder.dateLbl!!.text = CommonUtils.getDatebyDefaultZone(itemsBean.completedAt, "MMMM dd, yyyy")
                } else {
                    holder.dateLbl!!.text = "N/A"
                }
                holder.titleLbl!!.text = if (itemsBean.title != null) itemsBean.title else "N/A"
                holder.servicePeriod!!.text = servicePeriodVal
                holder.description!!.text = description
                holder.appliedOffers!!.text = if (itemsBean.offerName != null) itemsBean.offerName else "N/A"
                holder.total!!.text = itemsBean.currencyCode + " " + itemsBean.totalAmount
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (position == 0) {
            HEADER_VIEW
        } else super.getItemViewType(position)
    }

    override fun getItemCount(): Int {
        return billingHistoryModel.items!!.size + 1
    }

    internal class CustomViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        @JvmField
        @BindView(R.id.date_lbl)
        var dateLbl: TextView? = null

        @JvmField
        @BindView(R.id.title_lbl)
        var titleLbl: TextView? = null

        @JvmField
        @BindView(R.id.service_period)
        var servicePeriod: TextView? = null

        @JvmField
        @BindView(R.id.description)
        var description: TextView? = null

        @JvmField
        @BindView(R.id.applied_offers)
        var appliedOffers: TextView? = null

        @JvmField
        @BindView(R.id.total)
        var total: TextView? = null

        init {
            ButterKnife.bind(this, itemView)
        }
    }

    internal class HeaderViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        @JvmField
        @BindView(R.id.date_lbl_header)
        var dateLblHeader: TextView? = null

        @JvmField
        @BindView(R.id.title_lbl_header)
        var titleLblHeader: TextView? = null

        @JvmField
        @BindView(R.id.service_period_header)
        var servicePeriodHeader: TextView? = null

        @JvmField
        @BindView(R.id.description_header)
        var descriptionHeader: TextView? = null

        @JvmField
        @BindView(R.id.applied_offers_header)
        var appliedOffersHeader: TextView? = null

        @JvmField
        @BindView(R.id.total_header)
        var totalHeader: TextView? = null

        init {
            ButterKnife.bind(this, itemView)
        }
    }


    companion object {
        private const val HEADER_VIEW = 1
        private const val NORMAL_VIEW = 0
    }

}