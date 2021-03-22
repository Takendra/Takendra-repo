package com.viewlift.views.adapters

import android.content.Context
import android.graphics.Color
import android.os.Handler
import android.text.TextUtils
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.viewlift.AppCMSApplication
import com.viewlift.R
import com.viewlift.Utils
import com.viewlift.models.data.appcms.api.ContentDatum
import com.viewlift.models.data.appcms.api.MetadataMap
import com.viewlift.models.data.appcms.api.Module
import com.viewlift.models.data.appcms.ui.AppCMSUIKeyType
import com.viewlift.models.data.appcms.ui.android.AppCMSAndroidModules
import com.viewlift.models.data.appcms.ui.page.Component
import com.viewlift.models.data.appcms.ui.page.Items
import com.viewlift.models.data.appcms.ui.page.Layout
import com.viewlift.models.data.appcms.ui.page.Settings
import com.viewlift.presenters.AppCMSPresenter
import com.viewlift.utils.CommonUtils
import com.viewlift.views.customviews.CollectionGridItemView
import com.viewlift.views.customviews.InternalEvent
import com.viewlift.views.customviews.OnInternalEvent
import com.viewlift.views.customviews.ViewCreator
import com.viewlift.views.customviews.constraintviews.ConstraintCollectionGridItemView
import com.viewlift.views.customviews.constraintviews.ConstraintViewCreator
import com.viewlift.views.utilities.ImageLoader
import com.viewlift.views.utilities.ImageUtils
import java.util.*

class ContentCarouselAdapter(val context: Context,
                             val viewCreator: ViewCreator?,
                             val settings: Settings?,
                             val parentLayout: Layout,
                             val component: Component,
                             val jsonValueKeyMap: Map<String, AppCMSUIKeyType>,
                             val moduleAPI: Module?,
                             val listView: RecyclerView,
                             val loop: Boolean,
                             val appCMSAndroidModules: AppCMSAndroidModules?,
                             val viewType: String, val blockName: String, val constrainteView: Boolean,
                             val constraintViewCreator: ConstraintViewCreator) : RecyclerView.Adapter<ContentCarouselAdapter.ViewHolder>(), AppCMSBaseAdapter, OnInternalEvent {

    var adapterData: ArrayList<Items>

    @JvmField
    var appCMSPresenter: AppCMSPresenter? = null

    private val cancelled = false

    private val started = false
    private var carouselHandler: Handler? = null
    private var carouselUpdater: Runnable? = null

    @Volatile
    private var updatedIndex: Int
    private val UPDATE_CAROUSEL_TO = 5000L
    var pagerSnapHelper: PagerSnapHelper? = null
    private val internalEventReceivers: MutableList<OnInternalEvent>
    private var moduleId: String? = null
    private fun initCarouselHandler() {
        carouselHandler = Handler()
        carouselUpdater = Runnable {
            if (adapterData.size > 1 && !cancelled && (loop || !loop && updatedIndex < adapterData.size)) {
                val firstVisibleIndex = (listView.layoutManager as LinearLayoutManager?)!!.findFirstVisibleItemPosition()
                val lastVisibleIndex = (listView.layoutManager as LinearLayoutManager?)!!.findLastVisibleItemPosition()
                if (updatedIndex < firstVisibleIndex) {
                    updatedIndex = firstVisibleIndex
                }
                if (lastVisibleIndex < updatedIndex) {
                    updatedIndex = lastVisibleIndex
                }
                if (0 <= updatedIndex) {
                    updateCarousel(updatedIndex + 1, false)
                }
                postUpdateCarousel()
            } else if (cancelled) {
                updatedIndex = defaultIndex
            }
        }
        postUpdateCarousel()
        pagerSnapHelper = object : PagerSnapHelper() {
            override fun onFling(velocityX: Int, velocityY: Int): Boolean {
                synchronized(listView) {
                    val firstVisibleIndex = (listView.layoutManager as LinearLayoutManager?)!!.findFirstVisibleItemPosition()
                    val lastVisibleIndex = (listView.layoutManager as LinearLayoutManager?)!!.findLastVisibleItemPosition()
                    if (firstVisibleIndex != lastVisibleIndex) {
                        var nextVisibleViewIndex = lastVisibleIndex
                        if (updatedIndex != firstVisibleIndex) {
                            nextVisibleViewIndex = firstVisibleIndex
                        }
                        try {
                            listView.smoothScrollToPosition(nextVisibleViewIndex)
                        } catch (e: Exception) {
                            //Log.e(TAG, "Error scrolling to position: " + nextVisibleViewIndex);
                        }
                        sendEvent(InternalEvent<Any>(nextVisibleViewIndex))
                        setUpdatedIndex(nextVisibleViewIndex)
                    }
                    return super.onFling(velocityX, velocityY)
                }
            }
        }
        (pagerSnapHelper as PagerSnapHelper).attachToRecyclerView(listView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var view: View?
        if (constrainteView) {
            var metadataMap: MetadataMap? = null
            if (moduleAPI != null && moduleAPI.metadataMap != null) metadataMap = moduleAPI.metadataMap
            view = constraintViewCreator.createConstraintCollectionGridItemView(parent.context,
                    parentLayout,
                    true,
                    component,
                    moduleAPI,
                    appCMSAndroidModules,
                    settings,
                    jsonValueKeyMap,
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    true,
                    false,
                    if (this.viewType.trim { it <= ' ' }.length > 2) this.viewType else component.view,
                    true,
                    false,
                    jsonValueKeyMap.get(this.viewType), blockName, metadataMap)
        } else {
            view = viewCreator?.createCollectionGridItemView(parent.context,
                    parentLayout,
                    true,
                    component,
                    appCMSPresenter,
                    moduleAPI,
                    appCMSAndroidModules,
                    settings,
                    jsonValueKeyMap,
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    true,
                    false,
                    if (this.viewType.trim { it <= ' ' }.length > 2) this.viewType else component.view,
                    true,
                    false, jsonValueKeyMap.get(this.viewType), blockName)
        }
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if (holder.constraintComponentView != null) {
            val childIndex: Int
            childIndex = if (loop) position % adapterData.size else position
            for (i in 0 until holder.constraintComponentView!!.numberOfChildren) {
                val childComponent = holder.constraintComponentView!!.matchComponentToView(holder.constraintComponentView!!.getChild(i))
                if (childComponent != null) {
                    var componentType = jsonValueKeyMap[childComponent.type]
                    if (componentType == null) {
                        componentType = AppCMSUIKeyType.PAGE_EMPTY_KEY
                    }
                    if (componentType == AppCMSUIKeyType.PAGE_IMAGE_KEY) {
                        val imageUrl = adapterData[childIndex].imageUrl2
                        (holder.constraintComponentView!!.getChild(i) as ImageView).scaleType = ImageView.ScaleType.FIT_CENTER
                        val placeholder = R.drawable.vid_image_placeholder_land
                        val requestOptions = RequestOptions()
                                .override(childComponent.layout.mobile.width.toInt(), childComponent.layout.mobile.height.toInt())
                                .placeholder(placeholder)
                                .fitCenter()
                        if (!ImageUtils.loadImage(holder.constraintComponentView!!.getChild(i) as ImageView, imageUrl, ImageLoader.ScaleType.START) && appCMSPresenter != null && appCMSPresenter!!.currentActivity != null && !appCMSPresenter!!.currentActivity.isFinishing) {
                            Glide.with(context.applicationContext)
                                    .load(imageUrl).apply(requestOptions) //                                        .override(size,size)
                                    .into((holder.constraintComponentView!!.getChild(i) as ImageView))
                        }
                    } else if (componentType == AppCMSUIKeyType.PAGE_LABEL_KEY) {
                        if (childComponent.key == "trayTitle") {
                            /*InputFilter[] maxFilters = new InputFilter[1];
                            maxFilters[0] = new InputFilter.LengthFilter(maxLength);
                            ((TextView) holder.constraintComponentView.getChild(i)).setFilters(maxFilters);*/(holder.constraintComponentView!!.getChild(i) as TextView).width = Utils.getDeviceWidth(context) / 2
                            (holder.constraintComponentView!!.getChild(i) as TextView).maxLines = 1
                            (holder.constraintComponentView!!.getChild(i) as TextView).ellipsize = TextUtils.TruncateAt.END
                            val titleText = if (CommonUtils.isEmpty(adapterData[childIndex].title)) "" else adapterData[childIndex].title.trim { it <= ' ' }
                            (holder.constraintComponentView!!.getChild(i) as TextView).text = titleText
                        } else if (childComponent.key == "description") {
                            // ((TextView) holder.constraintComponentView.getChild(i)).setMaxLines(5);
                            (holder.constraintComponentView!!.getChild(i) as TextView).text = adapterData[childIndex].shortParagraph
                        }
                        if (!CommonUtils.isEmpty(adapterData[childIndex].textColor)) {
                            try {
                                (holder.constraintComponentView!!.getChild(i) as TextView).setTextColor(Color.parseColor(adapterData[childIndex].textColor))
                            } catch (e: Exception) {
                                e.printStackTrace()
                            }
                        } else {
                            if (childComponent.key == "trayTitle") {
                                (holder.constraintComponentView!!.getChild(i) as TextView).setTextColor(appCMSPresenter!!.brandPrimaryCtaColor)
                            } else if (childComponent.key == "description") {
                                (holder.constraintComponentView!!.getChild(i) as TextView).setTextColor(appCMSPresenter!!.brandPrimaryCtaTextColor)
                            }
                        }
                    } else if (componentType == AppCMSUIKeyType.PAGE_SEPARATOR_VIEW_KEY) {
                        if (!CommonUtils.isEmpty(adapterData[childIndex].backgroundColor)) {
                            try {
                                holder.constraintComponentView!!.getChild(i).setBackgroundColor(Color.parseColor(adapterData[childIndex].backgroundColor))
                            } catch (e: Exception) {
                                e.printStackTrace()
                            }
                        } else {
                            holder.constraintComponentView!!.getChild(i).setBackgroundColor(appCMSPresenter!!.generalBackgroundColor)
                        }
                    }
                    /*else if(componentType == AppCMSUIKeyType.PAGE_BUTTON_KEY){
                        ((Button) holder.constraintComponentView.getChild(i)).setText(adapterData.get(childIndex).getButtonText());
                        ((Button) holder.constraintComponentView.getChild(i)).setOnClickListener((v)->{
                            appCMSPresenter.openChromeTab(adapterData.get(childIndex).getButtonLink());
                        });
                    }*/
                }
            }


            /*if (adapterData.size() != 0) {
                if (loop) {
                    bindView(holder.constraintComponentView,
                            adapterData.get(position % adapterData.size()), position);
                } else {
                    bindView(holder.constraintComponentView, adapterData.get(position), position);
                }
            }*/holder.constraintComponentView!!.setOnClickListener { _: View? -> appCMSPresenter!!.openChromeTab(adapterData[childIndex].buttonLink) }
        } else {
            for (i in 0 until holder.componentView!!.numberOfChildren) {
                val childComponent = holder.componentView!!.matchComponentToView(holder.componentView!!.getChild(i))
                if (childComponent != null) {
                    var componentType = jsonValueKeyMap[childComponent.type]
                    if (componentType == null) {
                        componentType = AppCMSUIKeyType.PAGE_EMPTY_KEY
                    }
                    if (componentType == AppCMSUIKeyType.PAGE_LABEL_KEY) {
                        (holder.componentView!!.getChild(i) as TextView).text = ""
                    } else if (componentType == AppCMSUIKeyType.PAGE_IMAGE_KEY) {
                        (holder.componentView!!.getChild(i) as ImageView).setImageResource(android.R.color.transparent)
                    }
                }
            }


            /*if (adapterData.size() != 0) {
                if (loop) {
                    bindView(holder.componentView,
                            adapterData.get(position % adapterData.size()), position);
                } else {
                    bindView(holder.componentView, adapterData.get(position), position);
                }
            }*/
        }
    }

    override fun getItemCount(): Int {
        return if (loop) {
            Int.MAX_VALUE
        } else adapterData.size
    }

    override fun resetData(listView: RecyclerView) {
        updatedIndex = defaultIndex
        sendCancelEventToReceivers(cancelled)
        sendEvent(InternalEvent<Any>(updatedIndex))
        listView.scrollToPosition(updatedIndex)
        cancel(false)
    }

    override fun updateData(listView: RecyclerView, contentData: List<ContentDatum>) {}
    override fun setClickable(clickable: Boolean) {}
    override fun addReceiver(e: OnInternalEvent) {
        internalEventReceivers.add(e)
    }

    override fun sendEvent(event: InternalEvent<*>?) {
        for (receiver in internalEventReceivers) {
            receiver.receiveEvent(event)
        }
    }

    override fun receiveEvent(event: InternalEvent<*>) {
        if (!cancelled) {
            if (event.eventData is Int) {
                updateCarousel(calculateUpdateIndex(event.eventData as Int), true)
            }
        }
    }

    override fun cancel(cancel: Boolean) {}
    override fun getModuleId(): String {
        return moduleId!!
    }

    override fun setModuleId(moduleId: String) {
        this.moduleId = moduleId
    }

    class ViewHolder : RecyclerView.ViewHolder {
        var componentView: CollectionGridItemView? = null
        var constraintComponentView: ConstraintCollectionGridItemView? = null
        var seeAllView: TextView? = null

        constructor(itemView: View?) : super(itemView!!) {
            if (itemView is FrameLayout) componentView = itemView as CollectionGridItemView? else if (itemView is ConstraintLayout) {
                constraintComponentView = itemView as ConstraintCollectionGridItemView?
            }
        }

        constructor(itemView: TextView?) : super(itemView!!) {
            seeAllView = itemView
        }
    }

    private val defaultIndex: Int
        private get() = if (adapterData.size != 0 && loop) {
            Int.MAX_VALUE / 2 - Int.MAX_VALUE / 2 % adapterData.size
        } else 0

    fun updateCarousel(index: Int, fromEvent: Boolean) {
        synchronized(listView) {
            setUpdatedIndex(index)
            try {
                listView.smoothScrollToPosition(updatedIndex)
            } catch (e: Exception) {
                //Log.e(TAG, "Error scrolling to position: " + updatedIndex);
            }
            if (!fromEvent) {
                sendEvent(InternalEvent<Any>(updatedIndex))
            }
        }
    }

    fun postUpdateCarousel() {
        carouselHandler!!.postDelayed(carouselUpdater, UPDATE_CAROUSEL_TO)
    }

    private fun setUpdatedIndex(index: Int) {
        updatedIndex = index
    }

    private fun calculateUpdateIndex(index: Int): Int {
        var index = index
        val firstVisibleIndex = (listView.layoutManager as LinearLayoutManager?)!!.findFirstVisibleItemPosition()
        val lastVisibleIndex = (listView.layoutManager as LinearLayoutManager?)!!.findLastVisibleItemPosition()
        if (index < firstVisibleIndex && adapterData.size < firstVisibleIndex - index) {
            index = if (firstVisibleIndex % adapterData.size < index % adapterData.size) {
                firstVisibleIndex + index % adapterData.size
            } else {
                firstVisibleIndex - index % adapterData.size
            }
        } else if (lastVisibleIndex < index && adapterData.size < index - lastVisibleIndex) {
            index = if (lastVisibleIndex % adapterData.size < index % adapterData.size) {
                index % adapterData.size + lastVisibleIndex
            } else {
                index % adapterData.size - lastVisibleIndex
            }
        }
        if (adapterData.size < Math.abs(index - firstVisibleIndex) ||
                adapterData.size < Math.abs(index - lastVisibleIndex)) {
            index = firstVisibleIndex
        }
        return index
    }

    private fun sendCancelEventToReceivers(cancel: Boolean) {
        for (receiver in internalEventReceivers) {
            receiver.cancel(cancel)
        }
    }

    init {
        adapterData = settings?.items ?: arrayListOf()
        internalEventReceivers = ArrayList()
        updatedIndex = defaultIndex
        appCMSPresenter = (context.applicationContext as AppCMSApplication)
                .appCMSPresenterComponent
                .appCMSPresenter()
        // (context.applicationContext as AppCMSApplication).appCMSPresenterComponent.inject(this)
        initCarouselHandler()
    }
}