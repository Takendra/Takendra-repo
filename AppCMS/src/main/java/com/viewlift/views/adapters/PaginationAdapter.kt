package com.viewlift.views.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.viewlift.R
import com.viewlift.models.data.appcms.api.ContentDatum
import com.viewlift.models.data.appcms.api.Module
import com.viewlift.models.data.appcms.ui.AppCMSUIKeyType
import com.viewlift.models.data.appcms.ui.android.AppCMSAndroidModules
import com.viewlift.models.data.appcms.ui.page.Component
import com.viewlift.models.data.appcms.ui.page.Layout
import com.viewlift.presenters.AppCMSPresenter
import com.viewlift.views.customviews.constraintviews.ConstraintCollectionGridItemView
import com.viewlift.views.customviews.constraintviews.ConstraintViewCreator

class PaginationAdapter(val mContext: Context,
                        val viewCreator: ConstraintViewCreator,
                        val appCMSPresenter: AppCMSPresenter,
                        val parentLayout: Layout,
                        val component: Component,
                        val jsonValueKeyMap: Map<String, AppCMSUIKeyType>,
                        val moduleAPI: Module?,
                        val defaultWidth: Int,
                        val defaultHeight: Int,
                        val componentViewType: String,
                        val appCMSAndroidModules: AppCMSAndroidModules,
                        val blockName: String,
                        val recyclerView: RecyclerView) : RecyclerView.Adapter<RecyclerView.ViewHolder>(), AppCMSBaseAdapter {
    private var adapterData: MutableList<ContentDatum>
    private val ITEM = 0
    private val LOADING = 1
    private var errorMsg: String? = null
    private var retryPageLoad = false
    private var isLoadingAdded = false
    var onClickHandler: ConstraintCollectionGridItemView.OnClickHandler? = null

    init {
        if (moduleAPI != null && moduleAPI.contentData != null) {
            this.adapterData = moduleAPI.contentData
        } else {
            this.adapterData = mutableListOf()
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        var viewHolder: RecyclerView.ViewHolder?
        if (viewType == ITEM) {
            val view: ConstraintCollectionGridItemView = viewCreator.createConstraintCollectionGridItemView(parent.context,
                    parentLayout,
                    true,
                    component,
                    moduleAPI,
                    appCMSAndroidModules,
                    null,
                    jsonValueKeyMap,
                    defaultWidth,
                    defaultHeight,
                    false,
                    false,
                    this.componentViewType ?: component.getView(),
                    true,
                    false,
                    jsonValueKeyMap.get(componentViewType), blockName, moduleAPI?.getMetadataMap())
            viewHolder = ViewHolder(view)
        } else {
            val viewLoading: View = LayoutInflater.from(parent.context).inflate(R.layout.item_progress, parent, false)
            viewHolder = LoadingVH(viewLoading)
        }
        return viewHolder
    }


    override fun getItemCount(): Int {
        return adapterData.size
    }

    override fun getItemViewType(position: Int): Int {
        return if (position == adapterData.size - 1 && isLoadingAdded) LOADING else ITEM
    }


    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (getItemViewType(position)) {
            ITEM -> {
                val viewHolder = holder as ViewHolder
                if (0 <= position && position < adapterData.size) {
                    bindView(viewHolder.componentView, adapterData[position], position)
                }
                var i = 0
                while (i < viewHolder.componentView.childItems.size) {
                    val itemContainer = viewHolder.componentView.childItems[i]
                    if (itemContainer.component.key != null) {
                    }
                    i++
                }
            }
            LOADING -> {
                val loadingVH = holder as LoadingVH
                if (retryPageLoad) {
                    loadingVH.mErrorLayout.visibility = View.VISIBLE
                    loadingVH.mProgressBar.visibility = View.GONE
                    loadingVH.mErrorTxt.text = if (errorMsg != null) errorMsg else holder.itemView.context.getString(R.string.error)
                } else {
                    loadingVH.mErrorLayout.visibility = View.GONE
                    loadingVH.mProgressBar.visibility = View.VISIBLE
                }
            }
        }
    }

    fun bindView(itemView: View,
                 data: ContentDatum?, position: Int) {
        if (onClickHandler == null) {

            onClickHandler = object : ConstraintCollectionGridItemView.OnClickHandler {
                override fun click(collectionGridItemView: ConstraintCollectionGridItemView, childComponent: Component, data: ContentDatum, clickPosition: Int) {

                    val action: String
                    if (data != null && data.gist != null && data.gist.permalink != null && data.gist.contentType != null) {
                        if (appCMSPresenter.getAppCMSMain().getFeatures().isTrickPlay
                                && data.gist != null && data.gist.contentType != null && data.gist.contentType.toLowerCase().contains(mContext.getString(R.string.content_type_video).toLowerCase())) {
                            action = mContext.getString(R.string.app_cms_action_watchvideo_key)
                            appCMSPresenter.launchVideoPlayer(data,
                                    data.gist.id,
                                    0,
                                    null,
                                    -1,
                                    action)
                            return
                        } else if (data.gist.contentType.toLowerCase().contains(mContext.getString(R.string.app_cms_full_length_feature_key_type).toLowerCase())) {
                            action = mContext.getString(R.string.app_cms_action_detailvideopage_key)
                        } else if (data.gist.contentType.toLowerCase().contains(mContext.getString(R.string.app_cms_bundle_key_type).toLowerCase())) {
                            action = mContext.getString(R.string.app_cms_action_detailbundlepage_key)
                        } else if (data.gist.contentType.toLowerCase().contains(mContext.getString(R.string.app_cms_photo_gallery_key_type).toLowerCase())
                                && data.gist.contentType.toLowerCase().contains(mContext.getString(R.string.app_cms_photo_image_key_type).toLowerCase())) {
                            action = mContext.getString(R.string.app_cms_action_photo_gallerypage_key)
                        } else if (data.gist.contentType.toLowerCase().contains(itemView.context.getString(R.string.app_cms_article_key_type).toLowerCase())) {
                            appCMSPresenter.setCurrentArticleIndex(-1)
                            appCMSPresenter.navigateToArticlePage(data.gist.id, data.gist.title, false, null, false)
                            return
                        } else {
                            action = mContext.getString(R.string.app_cms_action_showvideopage_key)
                        }
                        appCMSPresenter.launchButtonSelectedAction(data.gist.permalink,
                                action,
                                data.gist.title,
                                null,
                                null,
                                false,
                                0,
                                null)
                        (recyclerView.layoutManager as GridLayoutManager).scrollToPositionWithOffset(clickPosition, 0)
                    }
                }

                override fun play(childComponent: Component, data: ContentDatum) {}
            }
        }
        for (i in 0 until (itemView as ConstraintCollectionGridItemView).numberOfChildren) {
            itemView.bindChild(itemView.getContext(),
                    itemView.getChild(i),
                    data,
                    jsonValueKeyMap,
                    onClickHandler,
                    componentViewType,
                    appCMSPresenter.brandPrimaryCtaColor,
                    appCMSPresenter,
                    position,
                    null,
                    blockName, moduleAPI!!.metadataMap)
        }
    }

    fun addAll(objectList: MutableList<ContentDatum>) {
        adapterData.addAll(objectList)
        notifyItemInserted(adapterData.size - 1)
        setClickable(true)
    }

    fun add(`object`: Any?) {
        val contentDatum = ContentDatum()
        adapterData.add(contentDatum)
        notifyItemInserted(adapterData.size - 1)
    }

    fun remove(`object`: Any?) {
        val position = adapterData.indexOf(`object`)
        if (position > -1) {
            adapterData.removeAt(position)
            notifyItemRemoved(position)
        }
    }

    fun clear() {
        isLoadingAdded = false
        while (itemCount > 0) {
            remove(adapterData[0])
        }
    }

    fun addLoadingFooter() {
        isLoadingAdded = true
        val contentDatum = ContentDatum()
        add(contentDatum)
    }

    fun removeLoadingFooter() {
        isLoadingAdded = false
        val position = adapterData.size - 1
        run {
            adapterData.removeAt(position)
            notifyItemRemoved(position)
        }
    }

    override fun resetData(listView: RecyclerView?) {
    }

    override fun updateData(listView: RecyclerView?, contentData: MutableList<ContentDatum>?) {
    }

    override fun setClickable(clickable: Boolean) {
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var componentView: ConstraintCollectionGridItemView

        init {
            componentView = itemView as ConstraintCollectionGridItemView
        }
    }

    protected inner class LoadingVH(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
        val mProgressBar: ProgressBar
        val mRetryBtn: ImageButton
        val mErrorTxt: TextView
        val mErrorLayout: LinearLayout
        override fun onClick(view: View) {
            when (view.id) {
                R.id.loadmore_retry, R.id.loadmore_errorlayout -> {
                    showRetry(false, null)
                }
            }
        }

        init {

            mProgressBar = itemView.findViewById(R.id.loadmore_progress)
            mRetryBtn = itemView.findViewById(R.id.loadmore_retry)
            mErrorTxt = itemView.findViewById(R.id.loadmore_errortxt)
            mErrorLayout = itemView.findViewById(R.id.loadmore_errorlayout)
            mRetryBtn.setOnClickListener(this)
            mErrorLayout.setOnClickListener(this)
        }
    }

    private fun showRetry(show: Boolean, errorMsg: String?) {
        retryPageLoad = show
        notifyItemChanged(adapterData.size - 1)
        if (errorMsg != null) this.errorMsg = errorMsg
    }
}