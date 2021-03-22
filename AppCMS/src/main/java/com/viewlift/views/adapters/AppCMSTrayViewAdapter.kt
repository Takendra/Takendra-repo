package com.viewlift.views.adapters

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
import android.graphics.drawable.GradientDrawable
import android.text.Spannable
import android.text.SpannableString
import android.text.TextUtils
import android.text.style.RelativeSizeSpan
import android.text.style.StyleSpan
import android.view.Gravity
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.viewlift.AppCMSApplication
import com.viewlift.R
import com.viewlift.audio.playback.AudioPlaylistHelper
import com.viewlift.models.data.appcms.api.*
import com.viewlift.models.data.appcms.photogallery.IPhotoGallerySelectListener
import com.viewlift.models.data.appcms.ui.AppCMSUIKeyType
import com.viewlift.models.data.appcms.ui.android.AppCMSAndroidModules
import com.viewlift.models.data.appcms.ui.android.LocalizationResult
import com.viewlift.models.data.appcms.ui.main.GenericMessages
import com.viewlift.models.data.appcms.ui.main.LocalisedStrings
import com.viewlift.models.data.appcms.ui.page.Component
import com.viewlift.models.data.appcms.ui.page.Layout
import com.viewlift.models.data.appcms.ui.page.Settings
import com.viewlift.presenters.AppCMSPresenter
import com.viewlift.utils.CommonUtils
import com.viewlift.views.customviews.BaseView
import com.viewlift.views.customviews.PhotoGalleryNextPreviousListener
import com.viewlift.views.customviews.TrayCollectionGridItemView
import com.viewlift.views.customviews.ViewCreator
import com.viewlift.views.customviews.constraintviews.ConstraintCollectionGridItemView
import com.viewlift.views.customviews.constraintviews.ConstraintViewCreator
import java.util.*

class AppCMSTrayViewAdapter(protected var mContext: Context,
                            viewCreator: ViewCreator?,
                            settings: Settings?,
                            parentLayout: Layout,
                            useParentSize: Boolean,
                            component: Component,
                            jsonValueKeyMap: Map<String?, AppCMSUIKeyType>,
                            moduleAPI: Module?,
                            defaultWidth: Int,
                            defaultHeight: Int,
                            viewType: String?,
                            appCMSAndroidModules: AppCMSAndroidModules?,
                            blockName: String?,
                            isTrayModule: Boolean,
                            private val constraintViewCreator: ConstraintViewCreator) : RecyclerView.Adapter<AppCMSTrayViewAdapter.ViewHolder>(), AppCMSBaseAdapter {
    private val person: String
    private val episodicContentType: String
    private val seasonContentType: String
    private val fullLengthFeatureType: String
    protected var parentLayout: Layout
    protected var component: Component


    var appCMSPresenter: AppCMSPresenter? = null
    var localisedStrings: LocalisedStrings? = null

    protected var settings: Settings ?
    protected var jsonValueKeyMap: Map<String?, AppCMSUIKeyType>
    var moduleAPI: Module?
    var adapterData: List<ContentDatum>? = null
    var onClickHandler: TrayCollectionGridItemView.OnClickHandler? = null
    var onConstraintClickHandler: ConstraintCollectionGridItemView.OnClickHandler? = null
    var defaultWidth: Int
    var defaultHeight: Int
    var useMarginsAsPercentages: Boolean
    var componentViewType: String?
    var appCMSAndroidModules: AppCMSAndroidModules?
    var planItemView: Array<TrayCollectionGridItemView?>
    private val useParentSize: Boolean
    private var viewTypeKey: AppCMSUIKeyType?
    private var uiBlockName: AppCMSUIKeyType?
    private val unselectedColor: Int
    private val selectedColor: Int
    private var isClickable: Boolean
    private val videoAction: String
    private val openOptionsAction: String
    private val purchasePlanAction: String?
    private val showAction: String
    private val personAction: String
    private val bundleDetailAction: String
    private val watchTrailerAction: String
    private var lastTouchDownEvent: MotionEvent? = null
    private var preGist: Gist? = null
    var emptyList = false
    private val TYPE_SEE_ALL = 1
    private val TYPE_DEFAULT = 2
    var seeAllWidth = 0
    var seeAllHeight = 0
    var seeAllTextSize = 0f
    private var adapterSize = 0
    var blockName: String?
    var isTrayModule: Boolean
    var localizationResult: LocalizationResult? = null
    var genericMessages: GenericMessages? = null
    private fun getRelatedVideos(moduleAPI: Module): List<ContentDatum?> {
        val adapterData = moduleAPI.contentData
        val tempData: MutableList<ContentDatum?> = ArrayList()
        if (adapterData.size > 0 && adapterData[0] != null && adapterData[0]!!.gist != null && adapterData[0]!!.gist.relatedVideos != null && adapterData[0]!!.gist.relatedVideos.size > 0) {
            val relatedVideos = ArrayList<RelatedVideo>()
            for (i in adapterData[0]!!.gist.relatedVideos.indices) {
                if (!adapterData[0]!!.gist.relatedVideos[i].gist.isLiveStream) {
                    relatedVideos.add(adapterData[0]!!.gist.relatedVideos[i])
                }
            }
            var relatedVideosSize = relatedVideos.size
            if (relatedVideosSize > 4) {
                relatedVideosSize = 4
            }
            for (i in 0 until relatedVideosSize) {
                var newData: ContentDatum? = ContentDatum()
                newData = adapterData[0]
                newData!!.gist.relatedVideos[i] = relatedVideos[i]
                tempData.add(newData)
            }
        }
        return tempData
    }

    private val isSeeAllTray: Boolean
        get() = moduleAPI != null && moduleAPI!!.settings != null &&
                (moduleAPI!!.settings.isSeeAll ||
                        moduleAPI!!.settings.isSeeAllCard)

    private var iPhotoGallerySelectListener: IPhotoGallerySelectListener? = null
    fun setPhotoGalleryImageSelectionListener(iPhotoGallerySelectListener: IPhotoGallerySelectListener?) {
        this.iPhotoGallerySelectListener = iPhotoGallerySelectListener
    }

    fun setPhotoGalleryImageSelectionListener(listener: PhotoGalleryNextPreviousListener?): PhotoGalleryNextPreviousListener {
        var listener = listener
        listener = object : PhotoGalleryNextPreviousListener {
            override fun previousPhoto(previousButton: Button) {
                if (selectedPosition == 0) {
                    return
                } else if (selectedPosition == 1) {
                    previousButton.setBackgroundColor(Color.parseColor("#c8c8c8"))
                    previousButton.isEnabled = false
                }
                selectedPosition--
                iPhotoGallerySelectListener!!.selectedImageData(adapterData!![selectedPosition].gist.videoImageUrl, selectedPosition)
                if (preGist != null) preGist!!.isSelectedPosition = false
                preGist = adapterData!![selectedPosition].gist
                adapterData!![selectedPosition].gist.isSelectedPosition = true
                notifyDataSetChanged()
            }

            override fun nextPhoto(nextButton: Button) {
                if (selectedPosition == adapterData!!.size - 1) {
                    return
                } else if (selectedPosition == adapterData!!.size - 2 || selectedPosition == 1) {
                    nextButton.setBackgroundColor(Color.parseColor("#c8c8c8"))
                    nextButton.isEnabled = false
                }
                if (adapterData!!.size == 0) {
                    nextButton.setBackgroundColor(Color.parseColor("#c8c8c8"))
                    nextButton.isEnabled = false
                    return
                }
                selectedPosition++
                iPhotoGallerySelectListener!!.selectedImageData(adapterData!![selectedPosition].gist.videoImageUrl, selectedPosition)
                if (preGist != null) preGist!!.isSelectedPosition = false
                preGist = adapterData!![selectedPosition].gist
                adapterData!![selectedPosition].gist.isSelectedPosition = true
                notifyDataSetChanged()
            }
        }
        return listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return if (viewType == TYPE_DEFAULT) {

//            if(blockName!=null && blockName.equalsIgnoreCase("Search01"))
            /*if (isTrayModule) {
                val view = viewCreator.createTrayCollectionGridItemView(parent.context,
                        parentLayout,
                        useParentSize,
                        component,
                        appCMSPresenter,
                        moduleAPI,
                        appCMSAndroidModules,
                        settings,
                        jsonValueKeyMap,
                        defaultWidth,
                        defaultHeight,
                        useMarginsAsPercentages,
                        true,
                        componentViewType,
                        false,
                        useRoundedCorners(),
                        viewTypeKey,
                        blockName)
                if (view.childItems != null) {
                    for (i in view.childItems.indices) {
                        val itemContainer = view.childItems[i]
                        if (itemContainer != null && itemContainer.component.key != null) {
                            val childView = itemContainer.childView
                            if (itemContainer.component.key.equals(mContext.getString(R.string.app_cms_page_thumbnail_image_key), ignoreCase = true)) {
                                //val layPar = childView.layoutParams as LinearLayout.LayoutParams

                                //FrameLayout.LayoutParams layPar = (FrameLayout.LayoutParams) childView.getLayoutParams();
                                val layPar: ViewGroup.LayoutParams = if (childView.layoutParams is FrameLayout.LayoutParams) childView.layoutParams as FrameLayout.LayoutParams else childView.layoutParams as LinearLayout.LayoutParams
                                seeAllWidth = layPar.width
                                seeAllHeight = layPar.height
                            }
                            if (itemContainer.component.key.equals(mContext.getString(R.string.app_cms_page_thumbnail_title_key), ignoreCase = true)) {
                                seeAllTextSize = (childView as TextView).textSize
                                break
                            }
                        }
                    }
                }
                if (viewTypeKey == AppCMSUIKeyType.PAGE_SUBSCRIPTION_SELECTPLAN_03_KEY) {
                    applyBgColorToChildren(view, selectedColor)
                }
                if (viewTypeKey == AppCMSUIKeyType.PAGE_SUBSCRIPTION_SELECTPLAN_01_KEY ||
                        uiBlockName == AppCMSUIKeyType.UI_BLOCK_SELECTPLAN_04) {
                    setBorder(view, unselectedColor)
                }
                if (viewTypeKey == AppCMSUIKeyType.PAGE_PHOTO_TRAY_MODULE_KEY) {
                    view.layoutParams = FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
                }
                if (viewTypeKey == AppCMSUIKeyType.PAGE_AC_SEARCH_MODULE_KEY && moduleAPI != null && moduleAPI!!.title != null && moduleAPI!!.title.equals("ARTICLE", ignoreCase = true)) {
                    view.setBackgroundColor(Color.parseColor(component.backgroundColor))
                }
                ViewHolder(view)
            } else {*/
                var metadataMap: MetadataMap? = null
                if (moduleAPI != null && moduleAPI!!.metadataMap != null) metadataMap = moduleAPI!!.metadataMap
                val view = constraintViewCreator.createConstraintCollectionGridItemView(parent.context,
                        parentLayout,
                        true,
                        component,
                        moduleAPI,
                        appCMSAndroidModules,
                        settings,
                        jsonValueKeyMap,
                        defaultWidth,
                        defaultHeight,
                        useMarginsAsPercentages,
                        false,
                        if (componentViewType != null) componentViewType else component.view,
                        true,
                        false,
                        viewTypeKey, blockName, metadataMap)
                if (view.childItems != null) {
                    for (i in view.childItems.indices) {
                        val itemContainer = view.childItems[i]
                        if (itemContainer != null && itemContainer.component.key != null) {
                            val childView = itemContainer.getChildView()
                            if (itemContainer.component.key.equals(mContext.getString(R.string.app_cms_page_thumbnail_image_key), ignoreCase = true)) {
                                val layPar = childView.layoutParams as ConstraintLayout.LayoutParams
                                seeAllWidth = layPar.width
                                seeAllHeight = layPar.height
                            }
                            if (itemContainer.component.key.equals(mContext.getString(R.string.app_cms_page_thumbnail_title_key), ignoreCase = true)) {
                                seeAllTextSize = (childView as TextView).textSize
                                break
                            }
                        }
                    }
                }
                if (viewTypeKey == AppCMSUIKeyType.PAGE_SUBSCRIPTION_SELECTPLAN_03_KEY) {
                    applyBgColorToChildren(view, selectedColor)
                }
                if (viewTypeKey == AppCMSUIKeyType.PAGE_SUBSCRIPTION_SELECTPLAN_01_KEY ||
                        uiBlockName == AppCMSUIKeyType.UI_BLOCK_SELECTPLAN_04) {
                    setBorder(view, unselectedColor)
                }
                if (viewTypeKey == AppCMSUIKeyType.PAGE_PHOTO_TRAY_MODULE_KEY) {
                    view.layoutParams = FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
                }
                if (viewTypeKey == AppCMSUIKeyType.PAGE_AC_SEARCH_MODULE_KEY && moduleAPI!!.title.equals("ARTICLE", ignoreCase = true)) {
                    view.setBackgroundColor(Color.parseColor(component.backgroundColor))
                }
                ViewHolder(view)
           // }
        } else {
            val seeAllView = TextView(mContext)
            val layPar = FrameLayout.LayoutParams(seeAllWidth, seeAllHeight)
            seeAllView.layoutParams = layPar
            seeAllView.setTextColor(appCMSPresenter!!.generalTextColor)
            seeAllView.textSize = seeAllTextSize / mContext.resources.displayMetrics.scaledDensity
            seeAllView.setBackgroundColor(appCMSPresenter!!.generalBackgroundColor)
            seeAllView.gravity = Gravity.CENTER
            if (moduleAPI!!.title != null) {
                var seeAllTrayTitle = appCMSPresenter!!.languageResourcesFile.getStringValue(mContext.getString(R.string.app_cms_see_all_tray_title), moduleAPI!!.title)
                if (localizationResult != null && localizationResult!!.seeAllTray != null) seeAllTrayTitle = """
     ${localizationResult!!.seeAllTray}
     ${moduleAPI!!.title}
     """.trimIndent() else if (genericMessages != null && genericMessages!!.seeAllTray != null) seeAllTrayTitle = """
     ${genericMessages!!.seeAllTray}
     ${moduleAPI!!.title}
     """.trimIndent()
                val text: Spannable = SpannableString(seeAllTrayTitle)
                text.setSpan(RelativeSizeSpan(1.5f), 8, seeAllTrayTitle.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
                text.setSpan(StyleSpan(Typeface.BOLD), 0, 8, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
                seeAllView.text = text
            }
            ViewHolder(seeAllView)
        }
    }

    override fun getItemViewType(position: Int): Int {
//        if (position < adapterData.size()) {
        return if (position < adapterSize) {
            TYPE_DEFAULT
        } else {
            if (adapterData!!.size < adapterSize || viewTypeKey == AppCMSUIKeyType.PAGE_SEE_ALL_CATEGORY_01_KEY || viewTypeKey == AppCMSUIKeyType.PAGE_SEE_ALL_CATEGORY_02_KEY || appCMSPresenter!!.seeAllPage == null || !isSeeAllTray) TYPE_DEFAULT else TYPE_SEE_ALL
        }

//        if (emptyList && (viewTypeKey == AppCMSUIKeyType.PAGE_AC_SEARCH_MODULE_KEY || viewTypeKey == AppCMSUIKeyType.PAGE_AC_SEARCH02_MODULE_KEY)) {
//            TextView emptyView = new TextView(mContext);
//            emptyView.setTextColor(appCMSPresenter.getGeneralTextColor());
//            emptyView.setTextSize(24f);
//            emptyView.setText(appCMSPresenter.getLanguageResourcesFile().getUIresource(mContext.getString(R.string.no_results)));
//            }
//        return new ViewHolder(view);
    }

    override fun getItemCount(): Int {
//        return (adapterData != null ? adapterData.size() : 0);
        return if (adapterData!!.size < adapterSize || viewTypeKey == AppCMSUIKeyType.PAGE_SEE_ALL_CATEGORY_02_KEY || viewTypeKey == AppCMSUIKeyType.PAGE_SEE_ALL_CATEGORY_01_KEY || appCMSPresenter!!.seeAllPage == null || !isSeeAllTray) adapterData!!.size else adapterSize + 1
    }

    private fun useRoundedCorners(): Boolean {
        return mContext.getString(R.string.app_cms_page_subscription_selectionplan_03_key) == componentViewType
    }

    private fun applyBgColorToChildren(viewGroup: ViewGroup, bgColor: Int) {
        for (i in 0 until viewGroup.childCount) {
            val child = viewGroup.getChildAt(i)
            if (child is ViewGroup) {
                if (child is CardView) {
                    child.useCompatPadding = true
                    child.preventCornerOverlap = false
                    child.setCardBackgroundColor(bgColor)
                } else {
                    child.setBackgroundColor(bgColor)
                }
                applyBgColorToChildren(child, bgColor)
            }
        }
    }

    private fun selectViewPlan(collectionGridItemView: TrayCollectionGridItemView?, selectedText: String?) {
        var selectedText = selectedText
        collectionGridItemView!!.isSelectable = true
        for (collectionGridChild in collectionGridItemView.viewsToUpdateOnClickEvent) {
            if (collectionGridChild is Button) {
                val childComponent = collectionGridItemView.matchComponentToView(collectionGridChild)
                if (selectedText == null) {
                    selectedText = childComponent.selectedText
                }
                (collectionGridChild as TextView).text = selectedText
                (collectionGridChild as TextView).setTextColor(Color.parseColor(CommonUtils.getColor(mContext,
                        childComponent.textColor)))
                collectionGridChild.setBackgroundColor(selectedColor)
            }
        }
    }



    private fun deselectViewPlan01(collectionGridItemView: TrayCollectionGridItemView?, selectedText: String?) {
        collectionGridItemView!!.isSelectable = false
        for (collectionGridChild in collectionGridItemView
                .viewsToUpdateOnClickEvent) {
            if (collectionGridChild is Button) {
                val childComponent = collectionGridItemView.matchComponentToView(collectionGridChild)
                if (selectedText != null && !TextUtils.isEmpty(selectedText)) {
                    (collectionGridChild as TextView).text = selectedText
                } else {
                    (collectionGridChild as TextView).text = appCMSPresenter!!.languageResourcesFile.getUIresource(childComponent.text)
                }
                setBorder(collectionGridChild, ContextCompat.getColor(mContext, R.color.disabledButtonColor))
                (collectionGridChild as TextView).setTextColor(ContextCompat.getColor(mContext, R.color.disabledButtonColor))
            }
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
//        if (0 <= position && position < adapterData.size()) {
        if (0 <= position && position < adapterSize && holder.constraintComponentView != null) {
            for (i in 0 until holder.constraintComponentView!!.numberOfChildren) {
                if (holder.constraintComponentView!!.getChild(i) is TextView) {
                    (holder.constraintComponentView!!.getChild(i) as TextView).text = ""
                }
            }
            if (viewTypeKey == AppCMSUIKeyType.PAGE_SUBSCRIPTION_SELECTPLAN_01_KEY || uiBlockName == AppCMSUIKeyType.UI_BLOCK_SELECTPLAN_04 || viewTypeKey == AppCMSUIKeyType.PAGE_PHOTO_TRAY_MODULE_KEY) {
                planItemView[position] = holder.componentView
            }
            bindView(holder.constraintComponentView, adapterData!![position], position)
        } else if (0 <= position && position < adapterSize && holder.componentView != null) {
            for (i in 0 until holder.componentView!!.numberOfChildren) {
                if (holder.componentView!!.getChild(i) is TextView) {
                    (holder.componentView!!.getChild(i) as TextView).text = ""
                }
            }
            if (viewTypeKey == AppCMSUIKeyType.PAGE_SUBSCRIPTION_SELECTPLAN_01_KEY || uiBlockName == AppCMSUIKeyType.UI_BLOCK_SELECTPLAN_04 || viewTypeKey == AppCMSUIKeyType.PAGE_PHOTO_TRAY_MODULE_KEY) {
                planItemView[position] = holder.componentView
            }
            bindView(holder.componentView, adapterData!![position], position)
        }
        if (viewTypeKey == AppCMSUIKeyType.PAGE_SUBSCRIPTION_SELECTPLAN_01_KEY ||
                uiBlockName == AppCMSUIKeyType.UI_BLOCK_SELECTPLAN_04) {
            for (i in planItemView.indices) {
                if (planItemView[i] != null) {
                    var selectedText: String? = null
                    if (adapterData!![i] != null && adapterData!![i].planDetails != null && adapterData!![i].planDetails[0] != null && adapterData!![i].planDetails[0].callToAction != null) {
                        selectedText = adapterData!![i].planDetails[0].callToAction
                    }
                    if (selectedPosition == i) {
                        setBorder(planItemView[i], selectedColor)
                        selectViewPlan(planItemView[i], selectedText)
                    } else {
                        setBorder(planItemView[i], ContextCompat.getColor(mContext, android.R.color.white))
                        deselectViewPlan01(planItemView[i], selectedText)
                    }
                }
            }
        }
        if (viewTypeKey == AppCMSUIKeyType.PAGE_SUBSCRIPTION_SELECTPLAN_03_KEY || viewTypeKey == AppCMSUIKeyType.PAGE_SUBSCRIPTION_SELECTPLAN_01_KEY || uiBlockName == AppCMSUIKeyType.UI_BLOCK_SELECTPLAN_04 || viewTypeKey == AppCMSUIKeyType.PAGE_PHOTO_TRAY_MODULE_KEY) {
            var selectableIndex = -1
            for (i in 0 until adapterSize) {
                if (holder.componentView!!.isSelectable) {
                    selectableIndex = i
                }
            }
            if (selectableIndex == -1) {
                selectableIndex = 0
            }
            if (selectableIndex == position) {
                if (viewTypeKey != AppCMSUIKeyType.PAGE_SUBSCRIPTION_SELECTPLAN_03_KEY && viewTypeKey != AppCMSUIKeyType.PAGE_SUBSCRIPTION_SELECTPLAN_01_KEY && uiBlockName != AppCMSUIKeyType.UI_BLOCK_SELECTPLAN_04 && viewTypeKey != AppCMSUIKeyType.PAGE_PHOTO_TRAY_MODULE_KEY) {
                    holder.componentView!!.isSelectable = true
                    holder.componentView!!.performClick()
                }
            } else {
                //
            }
            if (viewTypeKey == AppCMSUIKeyType.PAGE_SUBSCRIPTION_SELECTPLAN_03_KEY || viewTypeKey == AppCMSUIKeyType.PAGE_SUBSCRIPTION_SELECTPLAN_01_KEY || uiBlockName == AppCMSUIKeyType.UI_BLOCK_SELECTPLAN_04 || viewTypeKey == AppCMSUIKeyType.PAGE_PHOTO_TRAY_MODULE_KEY) {
                holder.componentView!!.isSelectable = true
            }
            if (viewTypeKey == AppCMSUIKeyType.PAGE_PHOTO_TRAY_MODULE_KEY) {
                setBorder(planItemView[position], if (adapterData!![position].gist.isSelectedPosition) selectedColor else ContextCompat.getColor(mContext, android.R.color.white))
            }
        }
        if (holder.seeAllView != null) {
            holder.seeAllView!!.setOnClickListener {
                val action = mContext.getString(R.string.app_cms_see_all_category_action)
                if (moduleAPI != null && moduleAPI!!.settings != null && moduleAPI!!.settings!!.seeAllPermalink != null && moduleAPI!!.id != null) {
                    appCMSPresenter!!.seeAllModule = moduleAPI
                    appCMSPresenter!!.isLastPage = false
                    appCMSPresenter!!.offset = 0
                    appCMSPresenter!!.launchButtonSelectedAction(moduleAPI!!.settings!!.seeAllPermalink,
                            action,
                            moduleAPI!!.title,
                            null,
                            null,
                            false,
                            0,
                            null)
                }
            }
        }
    }

    override fun resetData(listView: RecyclerView) {
        notifyDataSetChanged()
    }

    override fun updateData(listView: RecyclerView, contentData: List<ContentDatum>) {
        listView.adapter = null
        adapterData = null
        notifyDataSetChanged()
        adapterData = contentData

        //sortPlan(); as per MSEAN-1434
        notifyDataSetChanged()
        listView.adapter = this
        listView.invalidate()
        notifyDataSetChanged()
    }

    @SuppressLint("ClickableViewAccessibility")
    @Throws(IllegalArgumentException::class)
    fun bindView(itemView: View?,
                 data: ContentDatum?, position: Int) {
        if (onClickHandler == null) {
            if (viewTypeKey == AppCMSUIKeyType.PAGE_SUBSCRIPTION_SELECTPLAN_01_KEY
                    || uiBlockName == AppCMSUIKeyType.UI_BLOCK_SELECTPLAN_04) {
                onClickHandler = object : TrayCollectionGridItemView.OnClickHandler {
                    override fun click(collectionGridItemView: TrayCollectionGridItemView, childComponent: Component, data: ContentDatum,
                                       clickPosition: Int) {
                        if (appCMSPresenter!!.isSelectedSubscriptionPlan) {
                            selectedPosition = clickPosition
                        } else {
                            appCMSPresenter!!.isSelectedSubscriptionPlan = true
                        }
                        for (i in planItemView.indices) {
                            if (planItemView[i] != null) {
                                var selectedText: String? = null
                                if (adapterData!![i] != null && adapterData!![i].planDetails != null && adapterData!![i].planDetails[0] != null && adapterData!![i].planDetails[0].callToAction != null) {
                                    selectedText = adapterData!![i].planDetails[0].callToAction
                                }
                                if (selectedPosition == i) {
                                    setBorder(planItemView[i], selectedColor)
                                    selectViewPlan(planItemView[i], selectedText)
                                } else {
                                    setBorder(planItemView[i], ContextCompat.getColor(mContext, android.R.color.white))
                                    deselectViewPlan01(planItemView[i], selectedText)
                                }
                            }
                        }
                        if (childComponent != null && childComponent.action != null && purchasePlanAction != null) {
                            if (childComponent.action.contains(purchasePlanAction)) {
                                subcriptionPlanClick(collectionGridItemView, data)
                            }
                        }
                    }

                    override fun play(childComponent: Component, data: ContentDatum) {
                        // NO-OP - Play is not implemented here
                    }
                }
            } else if (viewTypeKey == AppCMSUIKeyType.PAGE_SUBSCRIPTION_SELECTPLAN_03_KEY) {
                onClickHandler = object : TrayCollectionGridItemView.OnClickHandler {
                    override fun click(collectionGridItemView: TrayCollectionGridItemView,
                                       childComponent: Component,
                                       data: ContentDatum, clickPosition: Int) {
                        if (isClickable) {
                            subcriptionPlanClick(collectionGridItemView, data)
                        }
                    }

                    override fun play(childComponent: Component, data: ContentDatum) {
                        // NO-OP - Play is not implemented here
                    }
                }
            } else if (viewTypeKey == AppCMSUIKeyType.PAGE_ARTICLE_FEED_MODULE_KEY) {
                onClickHandler = object : TrayCollectionGridItemView.OnClickHandler {
                    override fun click(collectionGridItemView: TrayCollectionGridItemView,
                                       childComponent: Component, data: ContentDatum, clickPosition: Int) {
                        if (childComponent != null && childComponent.key != null) {
                            if (jsonValueKeyMap[childComponent.key] == AppCMSUIKeyType.PAGE_THUMBNAIL_READ_MORE_KEY) {
                                if (data.gist != null && data.gist.mediaType != null && data.gist.mediaType.toLowerCase().contains(itemView!!.context.getString(R.string.app_cms_article_key_type).toLowerCase())) {
                                    appCMSPresenter!!.currentArticleIndex = -1
                                    appCMSPresenter!!.navigateToArticlePage(data.gist.id, data.gist.title, false, null, false)
                                    return
                                }
                            }
                        }
                    }

                    override fun play(childComponent: Component, data: ContentDatum) {}
                }
            } else if (viewTypeKey == AppCMSUIKeyType.PAGE_BRAND_TRAY_MODULE_KEY ||
                    viewTypeKey == AppCMSUIKeyType.PAGE_BRAND_CAROUSEL_MODULE_TYPE) {
                onClickHandler = object : TrayCollectionGridItemView.OnClickHandler {
                    override fun click(collectionGridItemView: TrayCollectionGridItemView,
                                       childComponent: Component, data: ContentDatum, clickPosition: Int) {
                        if (childComponent != null && childComponent.key != null) {
                            var action = mContext.getString(R.string.app_cms_instructor_details_action)
                            if (data.gist.contentType != null && data.gist.contentType == episodicContentType) {
                                action = showAction
                            }
                            val permalink = data.gist.permalink
                            val title = data.gist.title
                            appCMSPresenter!!.launchButtonSelectedAction(permalink,
                                    action,
                                    title,
                                    null,
                                    data,
                                    false,
                                    0,
                                    null)
                        }
                    }

                    override fun play(childComponent: Component, data: ContentDatum) {}
                }
            } else if (viewTypeKey == AppCMSUIKeyType.PAGE_SUBSCRIPTION_SELECTPLAN_03_KEY) {
                onClickHandler = object : TrayCollectionGridItemView.OnClickHandler {
                    override fun click(collectionGridItemView: TrayCollectionGridItemView,
                                       childComponent: Component,
                                       data: ContentDatum, clickPosition: Int) {
                        if (isClickable) {
                            subcriptionPlanClick(collectionGridItemView, data)
                        }
                    }

                    override fun play(childComponent: Component, data: ContentDatum) {
                        // NO-OP - Play is not implemented here
                    }
                }
            } else if (viewTypeKey == AppCMSUIKeyType.PAGE_PHOTO_TRAY_MODULE_KEY) {
                onClickHandler = object : TrayCollectionGridItemView.OnClickHandler {
                    override fun click(collectionGridItemView: TrayCollectionGridItemView, childComponent: Component,
                                       data: ContentDatum, clickPosition: Int) {
                        selectedPosition = clickPosition
                        iPhotoGallerySelectListener!!.selectedImageData(data.gist.videoImageUrl, selectedPosition)
                        //selectViewPlan(planItemView[clickPosition], null);
                        for (i in planItemView.indices) {
                            if (planItemView[i] != null) {
                                if (clickPosition == i) {
                                    setBorder(planItemView[i], selectedColor)
                                } else {
                                    setBorder(planItemView[i], ContextCompat.getColor(mContext, android.R.color.white))
                                }
                            }
                        }
                    }

                    override fun play(childComponent: Component, data: ContentDatum) {}
                }
            } else {
                if (itemView is ConstraintCollectionGridItemView) {
                    onConstraintClickHandler = object : ConstraintCollectionGridItemView.OnClickHandler {
                        override fun click(collectionGridItemView: ConstraintCollectionGridItemView, childComponent: Component, data: ContentDatum, clickPosition: Int) {
                            onClick(itemView, childComponent, data, clickPosition)
                        }

                        override fun play(childComponent: Component, data: ContentDatum) {
                            onPlay(childComponent, data)
                        }
                    }
                } else {
                    onClickHandler = object : TrayCollectionGridItemView.OnClickHandler {
                        override fun click(collectionGridItemView: TrayCollectionGridItemView,
                                           childComponent: Component,
                                           data: ContentDatum, clickPosition: Int) {
                            onClick(itemView, childComponent, data, clickPosition)
                        }

                        override fun play(childComponent: Component, data: ContentDatum) {
                            onPlay(childComponent, data)
                        }
                    }
                }
            }
        }
        if (viewTypeKey == AppCMSUIKeyType.PAGE_SUBSCRIPTION_SELECTPLAN_03_KEY || uiBlockName == AppCMSUIKeyType.UI_BLOCK_SELECTPLAN_04 || viewTypeKey == AppCMSUIKeyType.PAGE_SUBSCRIPTION_SELECTPLAN_01_KEY) {
            /*itemView.setOnClickListener(v -> onClickHandler.click(itemView,
                    component, data, position));*/
        }
        if (viewTypeKey == AppCMSUIKeyType.PAGE_SUBSCRIPTION_SELECTPLAN_03_KEY || viewTypeKey == AppCMSUIKeyType.PAGE_SUBSCRIPTION_SELECTPLAN_01_KEY || uiBlockName == AppCMSUIKeyType.UI_BLOCK_SELECTPLAN_04 || viewTypeKey == AppCMSUIKeyType.PAGE_PHOTO_TRAY_MODULE_KEY) {
            //
        } else {
            itemView!!.setOnTouchListener { v: View?, event: MotionEvent ->
                if (event.actionMasked == MotionEvent.ACTION_DOWN) {
                    lastTouchDownEvent = event
                }
                false
            }
            itemView.setOnClickListener { v: View? ->
                if (isClickable && data != null && data.gist != null) {
                    if (v is TrayCollectionGridItemView) {
                       /* try {
                            val eventX = lastTouchDownEvent!!.x.toInt()
                            val eventY = lastTouchDownEvent!!.y.toInt()
                            val childContainer = v.childrenContainer
                            val childrenCount = childContainer.childCount
                            for (i in 0 until childrenCount) {
                                val childView = childContainer.getChildAt(i)
                                if (childView is Button) {
                                    val childLocation = IntArray(2)
                                    childView.getLocationOnScreen(childLocation)
                                    val childX = childLocation[0] - 8
                                    val childY = childLocation[1] - 8
                                    val childWidth = childView.getWidth() + 8
                                    val childHeight = childView.getHeight() + 8
                                    if (childX <= eventX && eventX <= childX + childWidth) {
                                        if (childY <= eventY && eventY <= childY + childHeight) {
                                            childView.performClick()
                                            return@setOnClickListener
                                        }
                                    }
                                }
                            }
                        } catch (e: Exception) {
                            //
                        }*/
                    }
                    val permalink = data.gist.permalink
                    val title = data.gist.title
                    var action = videoAction
                    var contentType = ""
                    if (data.gist != null && data.gist.contentType != null) {
                        contentType = data.gist.contentType
                    }
                    if (contentType == episodicContentType) {
                        action = showAction
                    } else if (contentType.equals(person, ignoreCase = true)) {
                        action = personAction
                    } else if (contentType == fullLengthFeatureType) {
                        action = videoAction
                    }
                    /*get audio details on tray click item and play song*/if (data.gist != null && data.gist.mediaType != null &&
                            data.gist.mediaType.toLowerCase().contains(itemView.context.getString(R.string.media_type_audio).toLowerCase()) && data.gist.contentType != null &&
                            data.gist.contentType.toLowerCase().contains(itemView.context.getString(R.string.content_type_audio).toLowerCase())) {
                        val audioPlaylistId: MutableList<String> = ArrayList()
                        audioPlaylistId.add(data.gist.id)
                        AudioPlaylistHelper.getInstance().currentPlaylistId = data.gist.id
                        AudioPlaylistHelper.getInstance().currentPlaylistData = null
                        AudioPlaylistHelper.getInstance().setPlaylist(audioPlaylistId)
                        appCMSPresenter!!.currentActivity.sendBroadcast(Intent(AppCMSPresenter.PRESENTER_PAGE_LOADING_ACTION))
                        AudioPlaylistHelper.getInstance().playAudioOnClickItem(data.gist.id, 0)
                        return@setOnClickListener
                    }

                    /*Get playlist data and open playlist page*/if (data.gist != null && data.gist.mediaType != null && data.gist.mediaType.toLowerCase().contains(itemView.context.getString(R.string.media_type_playlist).toLowerCase())) {
                        appCMSPresenter!!.navigateToPlaylistPage(data.gist.id)
                        return@setOnClickListener
                    }

                    //Log.d(TAG, "Launching " + permalink + ":" + action);
                    var relatedVideoIds: List<String?>? = null
                    if (data.contentDetails != null &&
                            data.contentDetails.relatedVideoIds != null) {
                        relatedVideoIds = data.contentDetails.relatedVideoIds
                    }
                    var currentPlayingIndex = -1
                    if (relatedVideoIds == null) {
                        currentPlayingIndex = 0
                    }
                    if (data.gist == null ||
                            data.gist.contentType == null) {
                        if (!appCMSPresenter!!.launchVideoPlayer(data,
                                        data.gist.id,
                                        currentPlayingIndex,
                                        relatedVideoIds,
                                        -1,
                                        action)) {
                            //Log.e(TAG, "Could not launch action: " +
//                                    " permalink: " +
//                                    permalink +
//                                    " action: " +
//                                    action);
                        }
                    } else if (viewTypeKey == AppCMSUIKeyType.PAGE_LIVE_SCHEDULE_NO_DATA_TYPE) {
                    } else if (viewTypeKey == AppCMSUIKeyType.PAGE_CONTINUE_WATCHING_MODULE_KEY) {
                        action = mContext.getString(R.string.app_cms_action_watchvideo_key)
                        if (!appCMSPresenter!!.launchVideoPlayer(data,
                                        data.gist.id,
                                        currentPlayingIndex,
                                        relatedVideoIds,
                                        -1,
                                        action)) {
                            //Log.e(TAG, "Could not launch action: " +
//                                                " permalink: " +
//                                                permalink +
//                                                " action: " +
//                                                action);
                        }
                    } else {
                        if (!appCMSPresenter!!.launchButtonSelectedAction(permalink,
                                        action,
                                        title,
                                        null,
                                        null,
                                        false,
                                        currentPlayingIndex,
                                        relatedVideoIds)) {
                            //Log.e(TAG, "Could not launch action: " +
//                                    " permalink: " +
//                                    permalink +
//                                    " action: " +
//                                    action);
                        }
                    }
                }
            }
        }
        if (itemView is ConstraintCollectionGridItemView) {
            for (i in 0 until itemView.numberOfChildren) {
                val childView = itemView.getChild(i)
                if (component.settings != null && component.settings!!.isNoInfo
                        && componentViewType!!.contains("carousel") && childView !is ImageView) {
                    continue
                }
                itemView.bindChild(itemView.getContext(),
                        itemView.getChild(i),
                        data,
                        jsonValueKeyMap,
                        onConstraintClickHandler,
                        componentViewType,
                        appCMSPresenter!!.brandPrimaryCtaColor,
                        appCMSPresenter,
                        position,
                        null,
                        blockName, moduleAPI!!.metadataMap)
            }
        } else {
            for (i in 0 until (itemView as TrayCollectionGridItemView?)!!.numberOfChildren) {
                itemView?.bindChild(itemView.context,
                        (itemView as TrayCollectionGridItemView?)!!.getChild(i),
                        data,
                        jsonValueKeyMap,
                        onClickHandler,
                        componentViewType,
                        appCMSPresenter!!.brandPrimaryCtaColor,
                        appCMSPresenter,
                        position,
                        settings!!,
                        blockName,
                        moduleAPI!!.metadataMap)
            }
        }
    }

    fun subcriptionPlanClick(collectionGridItemView: TrayCollectionGridItemView, data: ContentDatum) {
        if (collectionGridItemView.isSelectable) {
            //Log.d(TAG, "Initiating signup and subscription: " +
//                                        data.getIdentifier());
            var price = data.planDetails[0].strikeThroughPrice
            if (price == 0.0) {
                price = data.planDetails[0].recurringPaymentAmount
            }
            val discountedPrice = data.planDetails[0].recurringPaymentAmount
            val discountedAmount = data.planDetails[0].discountedPrice
            var upgradesAvailable = false
            for (plan in adapterData!!) {
                if (plan != null && plan.planDetails != null &&
                        !plan.planDetails.isEmpty() &&
                        (plan.planDetails[0].strikeThroughPrice != 0.0 &&
                                price < plan.planDetails[0].strikeThroughPrice ||
                                plan.planDetails[0].recurringPaymentAmount != 0.0 &&
                                price < plan.planDetails[0].recurringPaymentAmount)) {
                    upgradesAvailable = true
                }
            }
            appCMSPresenter!!.initiateSignUpAndSubscription(data.identifier,
                    data.id,
                    data.planDetails[0].countryCode,
                    data.name,
                    price,
                    discountedPrice,
                    data.planDetails[0].recurringPaymentCurrencyCode,
                    data.planDetails[0].countryCode,
                    data.renewable,
                    data.renewalCycleType,
                    upgradesAvailable, discountedAmount,
                    data.planDetails[0].allowedPayMethods, data.planDetails[0].carrierBillingProviders)
        } else {
            collectionGridItemView.performClick()
        }
    }

    fun isClickable(): Boolean {
        return isClickable
    }

    override fun setClickable(clickable: Boolean) {
        isClickable = clickable
    }

    private fun getDefaultAction(context: Context): String {
        return context.getString(R.string.app_cms_action_videopage_key)
    }

    private fun getShowAction(context: Context): String {
        return context.getString(R.string.app_cms_action_showvideopage_key)
    }

    private fun getPersonAction(context: Context): String {
        return context.getString(R.string.app_cms_instructor_details_action)
    }

    private fun getBundleDetailAction(context: Context): String {
        return context.getString(R.string.app_cms_action_detailbundlepage_key)
    }

    private fun getOpenOptionsAction(context: Context): String {
        return context.getString(R.string.app_cms_action_open_option_dialog)
    }

    private fun getPurchasePlanAction(context: Context): String {
        return context.getString(R.string.app_cms_action_purchase_plan)
    }

    private fun getVideoAction(context: Context): String {
        return context.getString(R.string.app_cms_action_detailvideopage_key)
    }

    private fun getWatchTrailerAction(context: Context): String {
        return context.getString(R.string.app_cms_action_watchtrailervideo_key)
    }

    private fun getHlsUrl(data: ContentDatum): String? {
        if (data.streamingInfo?.videoAssets?.hls != null)
            return data.streamingInfo.videoAssets?.hls
        return null
    }

    private fun setBorder(itemView: View?,
                          color: Int) {
        val planBorder = GradientDrawable()
        planBorder.shape = GradientDrawable.RECTANGLE
        if (BaseView.isTablet(mContext)) {
            planBorder.setStroke(5, color)
            itemView!!.setPadding(3, 3, 3, 3)
        } else {
            planBorder.setStroke(7, color)
            itemView!!.setPadding(7, 7, 7, 7)
        }
        planBorder.setColor(ContextCompat.getColor(itemView.context, android.R.color.transparent))
        itemView.background = planBorder
    }

    fun sortPlan() {
        if (mContext.resources.getBoolean(R.bool.sort_plans_in_ascending_order)) {
            sortPlansByPriceInAscendingOrder()
        } else {
            sortPlansByPriceInDescendingOrder()
        }
    }

    private fun sortPlansByPriceInDescendingOrder() {
        if (viewTypeKey == AppCMSUIKeyType.PAGE_SUBSCRIPTION_SELECTPLAN_03_KEY && adapterData != null) {
            Collections.sort(adapterData
            ) { datum1: ContentDatum, datum2: ContentDatum ->
                if (datum1.planDetails[0].strikeThroughPrice == 0.0 && datum2.planDetails[0].strikeThroughPrice == 0.0) {
                    return@sort java.lang.Double.compare(datum2.planDetails[0]
                            .recurringPaymentAmount, datum1.planDetails[0]
                            .recurringPaymentAmount)
                }
                java.lang.Double.compare(datum2.planDetails[0]
                        .strikeThroughPrice, datum1.planDetails[0]
                        .strikeThroughPrice)
            }
        }
    }

    private fun sortPlansByPriceInAscendingOrder() {
        sortPlansByPriceInDescendingOrder()
        Collections.reverse(adapterData)
    }

    override fun onViewRecycled(holder: ViewHolder) {
        super.onViewRecycled(holder)
        if (holder.componentView != null && holder.componentView!!.childItems != null) {
            val childCount = holder.componentView!!.childCount
            for (i in 0 until childCount) {
                val child = holder.componentView!!.getChild(i)
                var currentActivity: Activity? = null
                if (child.context is Activity) currentActivity = child.context as Activity
                if (child is ImageView && currentActivity != null && !currentActivity.isDestroyed) {
                    try {
                        Glide.with(child.getContext()).clear(child)
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
            }
        }
    }

    class ViewHolder : RecyclerView.ViewHolder {
        var componentView: TrayCollectionGridItemView? = null
        var constraintComponentView: ConstraintCollectionGridItemView? = null
        var seeAllView: TextView? = null

        constructor(itemView: View?) : super(itemView!!) {
            if (itemView is LinearLayout) componentView = itemView as TrayCollectionGridItemView? else if (itemView is ConstraintLayout) {
                constraintComponentView = itemView as ConstraintCollectionGridItemView?
            }
        }

        constructor(itemView: TextView?) : super(itemView!!) {
            seeAllView = itemView
        }
    }

    fun onClick(itemView: View?, childComponent: Component?, data: ContentDatum, clickPosition: Int) {
        if (isClickable) {
            if (data.gist != null) {
                appCMSPresenter!!.playSource = ""
                if (moduleAPI!!.contentData[0].gist != null && moduleAPI!!.contentData[0].gist.contentType != null &&
                        !TextUtils.isEmpty(moduleAPI!!.contentData[0].gist.contentType) && moduleAPI!!.contentData[0].gist.contentType.contains("SERIES")) appCMSPresenter!!.playSource = moduleAPI!!.contentData[0].gist.title else appCMSPresenter!!.playSource = moduleAPI!!.title
                appCMSPresenter!!.isRecommendationTrayClicked = false
                if (moduleAPI!!.title != null && moduleAPI!!.moduleType != null && moduleAPI!!.moduleType.equals(mContext.getString(R.string.app_cms_api_history_module_key), ignoreCase = true)
                        && moduleAPI!!.title.toLowerCase().indexOf("recommendation") != -1) {
                    appCMSPresenter!!.isRecommendationTrayClicked = true
                    //Rendering
                    appCMSPresenter!!.sendGaEvent(mContext.resources.getString(R.string.play_video_action),
                            mContext.resources.getString(R.string.recommend_tray_clicked), "true")
                }

                //Log.d(TAG, "Clicked on item: " + data.getGist().getTitle());
                var permalink = data.gist.permalink
                if (data.gist != null && data.gist.relatedVideos != null && data.gist.relatedVideos.size > clickPosition && data.gist.relatedVideos[clickPosition] != null && data.gist.relatedVideos[clickPosition].gist != null && data.gist.relatedVideos[clickPosition].gist.permalink != null) {
                    permalink = data.gist.relatedVideos[clickPosition].gist.permalink
                }
                var action: String? = videoAction
                if (childComponent != null && !TextUtils.isEmpty(childComponent.action)) {
                    action = childComponent.action
                }
                val title = data.gist.title
                val hlsUrl = getHlsUrl(data)
                val extraData = arrayOfNulls<String>(3)
                extraData[0] = permalink
                extraData[1] = hlsUrl
                extraData[2] = data.gist.id
                //Log.d(TAG, "Launching " + permalink + ": " + action);
                var relatedVideoIds: List<String>? = null
                if (data.contentDetails != null &&
                        data.contentDetails.relatedVideoIds != null) {
                    relatedVideoIds = data.contentDetails.relatedVideoIds
                }
                var currentPlayingIndex = -1
                if (relatedVideoIds == null) {
                    currentPlayingIndex = 0
                }
                var contentType = ""
                if (data.gist != null && data.gist.contentType != null) {
                    contentType = data.gist.contentType
                }
                if (action!!.contains(videoAction) && data.gist != null && data.gist.mediaType != null &&
                        data.gist.mediaType.toLowerCase().contains(itemView!!.context.getString(R.string.media_type_video).toLowerCase())) appCMSPresenter!!.playSource = appCMSPresenter!!.playSource + "_Video Detail"
                if (action.contains(videoAction) && data.gist != null && data.gist.contentType != null &&
                        data.gist.contentType.toLowerCase().contains(itemView!!.context.getString(R.string.media_type_video).toLowerCase())) appCMSPresenter!!.playSource = appCMSPresenter!!.playSource + "_Video Detail"
                if (action.contains(videoAction) && data.gist != null && data.gist.mediaType != null &&
                        data.gist.mediaType.toLowerCase().contains(itemView!!.context.getString(R.string.content_type_series).toLowerCase()) && data.gist.contentType != null &&
                        data.gist.contentType.toLowerCase().contains(itemView.context.getString(R.string.content_type_series).toLowerCase())) {
                    appCMSPresenter!!.playSource = ""
                    appCMSPresenter!!.playSource = childComponent!!.type
                    appCMSPresenter!!.playSource = appCMSPresenter!!.playSource + "_Show Detail"
                } else if (action.contains(videoAction) && data.gist != null && data.gist.contentType != null &&
                        data.gist.contentType.toLowerCase().contains(itemView!!.context.getString(R.string.content_type_season).toLowerCase())) {
                    appCMSPresenter!!.playSource = ""
                    appCMSPresenter!!.playSource = childComponent!!.type
                    appCMSPresenter!!.playSource = appCMSPresenter!!.playSource + "_Show Detail"
                } else if (action.contains(videoAction) && data.gist != null && data.gist.mediaType != null &&
                        !data.gist.mediaType.toLowerCase().contains(itemView!!.context.getString(R.string.media_type_audio).toLowerCase()) && data.gist.contentType != null &&
                        !data.gist.contentType.toLowerCase().contains(itemView.context.getString(R.string.content_type_audio).toLowerCase()) &&
                        !data.gist.mediaType.toLowerCase().contains(itemView.context.getString(R.string.media_type_playlist).toLowerCase())) {
                    appCMSPresenter!!.playSource = appCMSPresenter!!.playSource + "_Video Detail"
                } else if (data.gist != null && data.gist.mediaType != null &&
                        data.gist.mediaType.toLowerCase().contains(itemView!!.context.getString(R.string.media_type_audio).toLowerCase()) && data.gist.contentType != null &&
                        data.gist.contentType.toLowerCase().contains(itemView.context.getString(R.string.content_type_audio).toLowerCase())) {
                    appCMSPresenter!!.playSource = appCMSPresenter!!.playSource + "_Music"
                } else if (data.gist != null && data.gist.mediaType != null && data.gist.mediaType.toLowerCase().contains(itemView!!.context.getString(R.string.media_type_playlist).toLowerCase())) {
                    appCMSPresenter!!.playSource = appCMSPresenter!!.playSource + "_Playlist Screen - " + data.gist.title
                }
                if (action.contains("watchVideo") && moduleAPI!!.title.contains("Continue Watching")) {
                    appCMSPresenter!!.playSource = appCMSPresenter!!.playSource + "_Home Page"
                }
                /*get audio details on tray click item and play song*/if (data.gist != null && data.gist.mediaType != null &&
                        data.gist.mediaType.toLowerCase().contains(itemView!!.context.getString(R.string.media_type_audio).toLowerCase()) && data.gist.contentType != null &&
                        data.gist.contentType.toLowerCase().contains(itemView.context.getString(R.string.content_type_audio).toLowerCase())) {
                    val audioPlaylistId: MutableList<String> = ArrayList()
                    audioPlaylistId.add(data.gist.id)
                    AudioPlaylistHelper.getInstance().currentPlaylistId = data.gist.id
                    AudioPlaylistHelper.getInstance().currentPlaylistData = null
                    AudioPlaylistHelper.getInstance().setPlaylist(audioPlaylistId)
                    appCMSPresenter!!.currentActivity.sendBroadcast(Intent(AppCMSPresenter.PRESENTER_PAGE_LOADING_ACTION))
                    AudioPlaylistHelper.getInstance().playAudioOnClickItem(data.gist.id, 0)
                    return
                }

                /*Get playlist data and open playlist page*/if (data.gist != null && data.gist.contentType != null &&
                        data.gist.contentType.toLowerCase().contains(itemView!!.context.getString(R.string.content_type_audio).toLowerCase()) && data.gist.mediaType != null && data.gist.mediaType.toLowerCase().contains(itemView.context.getString(R.string.media_type_playlist).toLowerCase())) {
                    appCMSPresenter!!.navigateToPlaylistPage(data.gist.id)
                    return
                }
                if ((viewTypeKey == AppCMSUIKeyType.PAGE_AC_SEARCH_MODULE_KEY || viewTypeKey == AppCMSUIKeyType.PAGE_AC_SEARCH02_MODULE_KEY || viewTypeKey == AppCMSUIKeyType.PAGE_LIBRARY_01_MODULE_KEY || viewTypeKey == AppCMSUIKeyType.PAGE_LIBRARY_02_MODULE_KEY)
                        && ((data.gist.mediaType == null || data.gist.mediaType.toLowerCase().contains(itemView!!.context.getString(R.string.app_cms_bundle_key_type).toLowerCase()))
                                && data.gist != null && data.gist.contentType != null && data.gist.contentType.toLowerCase().contains(itemView!!.context.getString(R.string.app_cms_bundle_key_type).toLowerCase()))) {
                    action = itemView.context.getString(R.string.app_cms_action_detailbundlepage_key)
                    if (!appCMSPresenter!!.launchButtonSelectedAction(permalink,
                                    action,
                                    title,
                                    null,
                                    null,
                                    false,
                                    0,
                                    null)) {
                    }
                    return
                }
                if ((viewTypeKey == AppCMSUIKeyType.PAGE_AC_SEARCH_MODULE_KEY || viewTypeKey == AppCMSUIKeyType.PAGE_AC_SEARCH02_MODULE_KEY || viewTypeKey == AppCMSUIKeyType.PAGE_LIBRARY_01_MODULE_KEY || viewTypeKey == AppCMSUIKeyType.PAGE_LIBRARY_02_MODULE_KEY)
                        && (data.gist != null && data.gist.mediaType != null && data.gist.mediaType.toLowerCase().contains(itemView!!.context.getString(R.string.media_type_episode).toLowerCase())
                                && data.gist != null && data.gist.contentType != null && data.gist.contentType.toLowerCase().contains(itemView.context.getString(R.string.app_cms_bundle_key_type).toLowerCase()))) {
                    action = "lectureDetailPage"
                }
                if (data.gist != null && data.gist.contentType != null && data.gist.contentType.toLowerCase().contains(itemView!!.context.getString(R.string.app_cms_team_label).toLowerCase())) {
                    appCMSPresenter!!.navigateToTeamDetailPage("acd7eac5-8bba-46bb-b337-b475df5ef680", data.gist.title, false)
                    return
                }
                if (data.gist != null && data.gist.contentType != null &&
                        data.gist.contentType.toLowerCase().contains(itemView!!.context.getString(R.string.content_type_audio).toLowerCase()) && data.gist.mediaType != null && data.gist.mediaType.toLowerCase().contains(itemView.context.getString(R.string.media_type_playlist).toLowerCase())) {
                    appCMSPresenter!!.navigateToPlaylistPage(data.gist.id)
                    return
                }
                if (action.contains(openOptionsAction)) {
                    appCMSPresenter!!.launchButtonSelectedAction(permalink,
                            openOptionsAction,
                            title,
                            null,
                            data,
                            false,
                            currentPlayingIndex,
                            relatedVideoIds)
                    return
                }
                if (contentType == episodicContentType || contentType.equals(seasonContentType, ignoreCase = true)) {
                    action = showAction
                } else if (contentType == fullLengthFeatureType) {
                    action = if (action != null && action.equals("openOptionDialog", ignoreCase = true)) action else videoAction
                }
                //                                if(data.getGist().getTitle().equalsIgnoreCase("Test Encoding ")){
//                                    action=bundleDetailAction;
//                                }
                if (data.gist != null && data.gist.mediaType != null && data.gist.mediaType.toLowerCase().contains(itemView!!.context.getString(R.string.media_type_bundle).toLowerCase())) {
                    action = bundleDetailAction
                }
                //                                if (data.getGist() != null && data.getGist().getMediaType() != null
//                                        && data.getGist().getMediaType().toLowerCase().contains(itemView.getContext().getString(R.string.media_type_episode).toLowerCase())) {
//                                    action = showAction;
//
//                                }
                if (data.gist != null && data.gist.mediaType != null && data.gist.mediaType.toLowerCase().contains(itemView!!.context.getString(R.string.app_cms_article_key_type).toLowerCase())) {
                    appCMSPresenter!!.currentArticleIndex = -1
                    appCMSPresenter!!.navigateToArticlePage(data.gist.id, data.gist.title, false, null, false)
                    return
                }
                if (data.gist != null && data.gist.contentType != null && data.gist.contentType.toLowerCase().contains(itemView!!.context.getString(R.string.app_cms_event_key_type).toLowerCase())) {
                    appCMSPresenter!!.navigateToEventDetailPage(data.gist.permalink)
                    return
                }
                //PHOTOGALLERY
                if (data.gist != null && data.gist.mediaType != null && data.gist.mediaType.contains("PHOTOGALLERY")) {
                    appCMSPresenter!!.currentPhotoGalleryIndex = clickPosition
                    appCMSPresenter!!.navigateToPhotoGalleryPage(data.gist.id, data.gist.title, adapterData, false)
                    return
                }
                if (data.gist == null ||
                        data.gist.contentType == null) {
                    if (data.pricing != null && data.pricing.type != null && ((data.pricing.type.equals(mContext.getString(R.string.PURCHASE_TYPE_TVOD), ignoreCase = true)
                                    || data.pricing.type.equals(mContext.getString(R.string.PURCHASE_TYPE_PPV), ignoreCase = true)) ||
                                    (data.pricing.type.equals(mContext.getString(R.string.PURCHASE_TYPE_SVOD_TVOD), ignoreCase = true)
                                            || data.pricing.type.equals(mContext.getString(R.string.PURCHASE_TYPE_SVOD_PPV), ignoreCase = true)))) {
                        val finalCurrentPlayingIndex = currentPlayingIndex
                        val finalRelatedVideoIds = relatedVideoIds
                        val finalAction = action
                        appCMSPresenter!!.getTransactionData(data.gist.id, { updatedContentDatum: List<Map<String?, AppCMSTransactionDataValue?>>? ->
                            var isPlayable = true
                            var objTransactionData: AppCMSTransactionDataValue? = null
                            val isPurchased = false
                            if (updatedContentDatum != null &&
                                    updatedContentDatum.size > 0) {
                                if (updatedContentDatum[0].size == 0) {
                                    isPlayable = false
                                } else {
                                    objTransactionData = updatedContentDatum[0][data.gist.id]
                                }
                            } else {
                                isPlayable = false
                            }
                            /**
                             * If pricing type is svod+tvod or svod+tvod then check if user is subscribed or
                             * content is purchased by user ,if both conditions are false then show subscribe message.
                             */
                            if (data.pricing.type.equals(mContext.getString(R.string.PURCHASE_TYPE_SVOD_TVOD), ignoreCase = true)
                                    || data.pricing.type.equals(mContext.getString(R.string.PURCHASE_TYPE_SVOD_PPV), ignoreCase = true)) {

//                                                if (!appCMSPresenter.isUserLoggedIn()) {
//                                                    appCMSPresenter.showSubscribeMessage();
//                                                    return;
//                                                }else{
//                                                    appCMSPresenter.launchVideoPlayer(data,
//                                                            data.getGist().getId(),
//                                                            finalCurrentPlayingIndex,
//                                                            finalRelatedVideoIds,
//                                                            -1,
//                                                            finalAction);
//                                                    return;
//                                                }
                                if (appCMSPresenter!!.isUserSubscribed || isPurchased) {
                                    isPlayable = true
                                } else {
                                    if (!appCMSPresenter!!.appCMSMain.features.freePreview?.isFreePreview!!) {
                                        appCMSPresenter!!.showSubscribeMessage()
                                        isPlayable = false
                                        return@getTransactionData
                                    }
                                }
                                if (isPlayable) {
                                    appCMSPresenter!!.launchVideoPlayer(data,
                                            data.gist.id,
                                            finalCurrentPlayingIndex,
                                            finalRelatedVideoIds,
                                            -1,
                                            finalAction)
                                    return@getTransactionData
                                }
                            }
                            if (!isPlayable) {
                                if (localisedStrings!!.getCannotPurchaseItemMsg(appCMSPresenter!!.appCMSMain.domainName) == null) appCMSPresenter!!.showNoPurchaseDialog(appCMSPresenter!!.languageResourcesFile.getUIresource(mContext.getString(R.string.rental_title)), appCMSPresenter!!.languageResourcesFile.getStringValue(mContext.getString(R.string.cannot_purchase_item_msg), appCMSPresenter!!.appCMSMain.domainName)) else appCMSPresenter!!.showNoPurchaseDialog(appCMSPresenter!!.languageResourcesFile.getUIresource(mContext.getString(R.string.rental_title)), localisedStrings!!.getCannotPurchaseItemMsg(appCMSPresenter!!.appCMSMain.domainName))
                            } else {
                                var rentalPeriod = ""
                                if (data.pricing != null && data.pricing.rent != null && data.pricing.rent.rentalPeriod > 0) {
                                    rentalPeriod = data.pricing.rent.rentalPeriod.toString()
                                }
                                if (objTransactionData != null) {
                                    rentalPeriod = objTransactionData.rentalPeriod.toString()
                                }
                                var isShowRentalPeriodDialog = true
                                /**
                                 * if transaction getdata api containf transaction end date .It means Rent API called before
                                 * and we have shown rent period dialog before so dont need to show rent dialog again. else sow rent period dilaog
                                 */
                                isShowRentalPeriodDialog = objTransactionData != null && objTransactionData.purchaseStatus != null && objTransactionData.purchaseStatus.equals("RENTED", ignoreCase = true)
                                if (isShowRentalPeriodDialog) {
                                    if (rentalPeriod == null || TextUtils.isEmpty(rentalPeriod)) {
                                        rentalPeriod = "0"
                                    }
                                    var msg = appCMSPresenter!!.languageResourcesFile.getStringValue(mContext.getString(R.string.rent_time_dialog_mssg),
                                            rentalPeriod)
                                    if (localisedStrings!!.getRentTimeDialogMsg(rentalPeriod) != null) msg = localisedStrings!!.getRentTimeDialogMsg(rentalPeriod)
                                    appCMSPresenter!!.showRentTimeDialog({ retry: Boolean ->
                                        if (retry) {
                                            appCMSPresenter!!.getRentalData(moduleAPI!!.contentData[0].gist.id, { getRentalData: AppCMSRentalResponse? ->
                                                if (!appCMSPresenter!!.launchVideoPlayer(data,
                                                                data.gist.id,
                                                                finalCurrentPlayingIndex,
                                                                finalRelatedVideoIds,
                                                                -1,
                                                                finalAction)) {
                                                }
                                            }, false, 0)
                                        } else {
//                                                appCMSPresenter.sendCloseOthersAction(null, true, false);
                                        }
                                    }, msg, "", "", "", true, true)
                                } else {
                                    if (!appCMSPresenter!!.launchVideoPlayer(data,
                                                    data.gist.id,
                                                    finalCurrentPlayingIndex,
                                                    finalRelatedVideoIds,
                                                    -1,
                                                    finalAction)) {
                                        //Log.e(TAG, "Could not launch action: " +
//                                                " permalink: " +
//                                                permalink +
//                                                " action: " +
//                                                action);
                                    }
                                }
                            }
                        }, "Video")
                    } else {
                        if (!appCMSPresenter!!.isNetworkConnected && !appCMSPresenter!!.isVideoDownloaded(data.gist.id)) { //checking isVideoOffline here to fix SVFA-1431 in offline mode
                            appCMSPresenter!!.launchButtonSelectedAction(permalink,
                                    action,
                                    title,
                                    null,
                                    if (action.equals("openOptionDialog", ignoreCase = true)) data else null,
                                    false,
                                    currentPlayingIndex,
                                    relatedVideoIds)
                        } else if (!appCMSPresenter!!.launchVideoPlayer(data,
                                        data.gist.id,
                                        currentPlayingIndex,
                                        relatedVideoIds,
                                        -1,
                                        action)) {
                            //Log.e(TAG, "Could not launch action: " +
//                                                " permalink: " +
//                                                permalink +
//                                                " action: " +
//                                                action);
                        }
                    }
                } else if (viewTypeKey == AppCMSUIKeyType.PAGE_LIVE_SCHEDULE_NO_DATA_TYPE) {
                } else if (viewTypeKey == AppCMSUIKeyType.PAGE_CONTINUE_WATCHING_MODULE_KEY) {
                    action = mContext.getString(R.string.app_cms_action_watchvideo_key)
                    if (!appCMSPresenter!!.launchVideoPlayer(data,
                                    data.gist.id,
                                    currentPlayingIndex,
                                    relatedVideoIds,
                                    -1,
                                    action)) {
                        //Log.e(TAG, "Could not launch action: " +
//                                                " permalink: " +
//                                                permalink +
//                                                " action: " +
//                                                action);
                    }
                } else {
                    data.setModuleApi(moduleAPI);
                    if (data.gist != null && data.gist.contentType != null &&
                            data.gist.contentType.toLowerCase().contains(itemView!!.context.getString(R.string.content_type_video).toLowerCase()) && data.gist.mediaType != null && data.gist.mediaType.toLowerCase().contains(itemView.context.getString(R.string.media_type_playlist).toLowerCase())) {
                        action = mContext.getString(R.string.app_cms_video_playlist_page_action)
                    }
                    if (appCMSPresenter!!.appCMSMain != null && appCMSPresenter!!.appCMSMain.features != null &&
                            appCMSPresenter!!.appCMSMain.features.isOpenShowDetail && action != null &&
                            action.equals(mContext.getString(R.string.app_cms_action_detailvideopage_key), ignoreCase = true) && data.seriesData != null &&
                            data.seriesData.size > 0 && data.seriesData[0].gist != null &&
                            data.seriesData[0].gist.permalink != null
                            && data.gist != null) {
                        action = mContext.getString(R.string.app_cms_action_showvideopage_key)
                        if (data.seriesData[0].gist.permalink != null) {
                            permalink = data.seriesData[0].gist.permalink
                        }
                    }
                    if (appCMSPresenter!!.appCMSMain.features.isTrickPlay && appCMSPresenter!!.appCMSMain != null
                            && appCMSPresenter!!.appCMSMain.features != null
                            && !appCMSPresenter!!.appCMSMain.features.isOpenShowDetail
                            && data.gist != null && data.gist.contentType != null
                            && data.gist.contentType.toLowerCase().contains(itemView!!.context.getString(R.string.content_type_video).toLowerCase())) {
                        appCMSPresenter!!.launchButtonSelectedAction(permalink,
                                mContext.getString(R.string.app_cms_action_watchvideo_key),
                                title,
                                null,  /*action.equalsIgnoreCase("openOptionDialog") ? data : null*/
                                data,
                                false,
                                currentPlayingIndex,
                                relatedVideoIds)
                    } else {
                        if (data.gist.contentType != null && data.gist.contentType.toLowerCase().contains(itemView!!.context.getString(R.string.content_type_person).toLowerCase())) appCMSPresenter!!.navigateToPersonDetailPage(permalink) else appCMSPresenter!!.setEpisodeId(null)
                        if(CommonUtils.isRecommendationTrayModule(blockName)){
                            CommonUtils.recommendationPlay(appCMSPresenter, moduleAPI)
                            appCMSPresenter!!.isRecommendationTrayClicked = true
                        }else {
                            CommonUtils.recommendedIds = null;
                            appCMSPresenter!!.isRecommendationTrayClicked = false
                        }
                        appCMSPresenter!!.launchButtonSelectedAction(permalink,
                                action,
                                title,
                                null,  /*action.equalsIgnoreCase("openOptionDialog") ? data : null*/
                                data,
                                false,
                                currentPlayingIndex,
                                relatedVideoIds)
                    }
                }
            }
        }
    }

    fun onPlay(childComponent: Component, data: ContentDatum) {
        if (isClickable) {
            if (data.gist != null) {
                //Log.d(TAG, "Playing item: " + data.getGist().getTitle());
                var filmId: String? = null
                var relatedVideoIds: List<String?>? = null
                if (childComponent.action != null && childComponent.action.contains(watchTrailerAction)) {
                    if (moduleAPI!!.contentData[0] != null && data.showDetails != null && data.showDetails.trailers != null && data.showDetails.trailers[0] != null && data.showDetails.trailers[0].id != null) {
                        filmId = moduleAPI!!.contentData[0].showDetails.trailers[0].id
                    }
                } else {
                    filmId = data.gist.id
                    if (data.contentDetails != null &&
                            data.contentDetails.relatedVideoIds != null) {
                        relatedVideoIds = data.contentDetails.relatedVideoIds
                    }
                }
                val permaLink = data.gist.permalink
                val title = data.gist.title
                var currentPlayingIndex = -1
                if (relatedVideoIds == null) {
                    currentPlayingIndex = 0
                }
                if (!appCMSPresenter!!.launchVideoPlayer(data,
                                filmId,
                                currentPlayingIndex,
                                relatedVideoIds,
                                -1,
                                null)) {
                    //Log.e(TAG, "Could not launch play action: " +
//                                            " filmId: " +
//                                            filmId +
//                                            " permaLink: " +
//                                            permaLink +
//                                            " title: " +
//                                            title);
                }
            }
        }
    }

    companion object {
        private const val TAG = "AppCMSViewAdapter"
        var selectedPosition = -1

        fun increaseFontSizeForPath(spannable: Spannable, path: String, increaseTime: Float) {
            val startIndexOfPath = spannable.toString().indexOf(path)
            spannable.setSpan(RelativeSizeSpan(increaseTime), startIndexOfPath,
                    startIndexOfPath + path.length, 0)
        }
    }

    init {
        appCMSPresenter = (mContext.applicationContext as AppCMSApplication)
                .appCMSPresenterComponent
                .appCMSPresenter()
        localisedStrings = (mContext.applicationContext as AppCMSApplication)
                .appCMSPresenterComponent
                .localisedStrings()

        this.parentLayout = parentLayout
        this.useParentSize = useParentSize
        this.component = component
        this.jsonValueKeyMap = jsonValueKeyMap
        this.moduleAPI = moduleAPI
        componentViewType = viewType
        this.isTrayModule = isTrayModule
        this.settings = settings
        this.blockName = blockName
        uiBlockName = jsonValueKeyMap[blockName]
        if (blockName == null) {
            uiBlockName = AppCMSUIKeyType.PAGE_EMPTY_KEY
        }
        viewTypeKey = jsonValueKeyMap[componentViewType]
        if (viewTypeKey == null) {
            viewTypeKey = AppCMSUIKeyType.PAGE_EMPTY_KEY
        }
        if (moduleAPI != null && moduleAPI.contentData != null) {
            adapterData = moduleAPI.contentData
            /*if (this.adapterData.size() >= adapterSize && this.viewTypeKey == AppCMSUIKeyType.PAGE_SEE_ALL_CATEGORY_KEY && appCMSPresenter.getSeeAllPage() == null && isSeeAllTray())
                adapterSize = adapterData.size();*/
        } else {
            adapterData = ArrayList()
        }
        if (adapterData!!.size == 0) {
            emptyList = true
        }

        /*if (moduleAPI != null && moduleAPI.getFilters() != null &&
                moduleAPI.getFilters().getLimit() >= adapterData.size()) {
            this.adapterSize = moduleAPI.getFilters().getLimit();
        } else {
            adapterSize = adapterData.size();
        }*/adapterSize = adapterData!!.size
        if (viewTypeKey == AppCMSUIKeyType.PAGE_PHOTO_TRAY_MODULE_KEY) {
            /*remove data from 1st position since it contains photogallery details*/
            if (adapterData!![0].streamingInfo != null) {
                val data: MutableList<ContentDatum> = ArrayList()
                data.addAll(moduleAPI!!.contentData)
                data.removeAt(0)
                adapterData = data
            }
            selectedPosition = 0
            if (adapterData!!.size > 0) {
                preGist = adapterData!![0].gist
            }
        }
        this.defaultWidth = defaultWidth
        this.defaultHeight = defaultHeight
        useMarginsAsPercentages = true
        videoAction = getVideoAction(mContext)
        showAction = getShowAction(mContext)
        personAction = getPersonAction(mContext)
        bundleDetailAction = getBundleDetailAction(mContext)
        watchTrailerAction = getWatchTrailerAction(mContext)
        openOptionsAction = getOpenOptionsAction(mContext)
        purchasePlanAction = getPurchasePlanAction(mContext)
        unselectedColor = ContextCompat.getColor(mContext, android.R.color.white)
        selectedColor = appCMSPresenter!!.brandPrimaryCtaColor
        isClickable = true
        setHasStableIds(false)
        this.appCMSAndroidModules = appCMSAndroidModules
        episodicContentType = mContext.getString(R.string.app_cms_episodic_key_type)
        seasonContentType = mContext.getString(R.string.app_cms_episodic_season_prefix)
        person = mContext.getString(R.string.app_cms_person_key_type)
        fullLengthFeatureType = mContext.getString(R.string.app_cms_full_length_feature_key_type)
        planItemView = arrayOfNulls(adapterSize)
        if (appCMSPresenter!!.appCMSMain != null && appCMSPresenter!!.appCMSMain.genericMessages != null && appCMSPresenter!!.appCMSMain.genericMessages.getLocalizationMap() != null && appCMSPresenter!!.appCMSMain.genericMessages.getLocalizationMap().size > 0 && appCMSPresenter!!.appCMSMain.genericMessages.getLocalizationMap()[appCMSPresenter!!.language.languageCode] != null) {
            localizationResult = appCMSPresenter!!.appCMSMain.genericMessages.getLocalizationMap()[appCMSPresenter!!.language.languageCode]
        }
        if (appCMSPresenter!!.appCMSMain.genericMessages != null) {
            genericMessages = appCMSPresenter!!.appCMSMain.genericMessages
        }
        //sortPlan(); as per MSEAN-1434
    }
}