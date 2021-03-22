package com.viewlift.views.customviews

import android.content.Context
import android.text.TextUtils
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton
import com.google.gson.GsonBuilder
import com.viewlift.R
import com.viewlift.Utils
import com.viewlift.models.data.appcms.api.ContentDatum
import com.viewlift.models.data.appcms.api.MetadataMap
import com.viewlift.models.data.appcms.api.Module
import com.viewlift.models.data.appcms.ui.AppCMSUIKeyType
import com.viewlift.models.data.appcms.ui.android.AppCMSAndroidModules
import com.viewlift.models.data.appcms.ui.page.AppCMSPageUI
import com.viewlift.models.data.appcms.ui.page.ModuleList
import com.viewlift.models.data.appcms.ui.page.ModuleWithComponents
import com.viewlift.presenters.AppCMSPresenter
import com.viewlift.utils.CommonUtils
import com.viewlift.utils.LocalModuleList.getModuleList
import com.viewlift.views.customviews.constraintviews.ConstraintViewCreator

internal class RecommendationModule(var mContext: Context,
                                    moduleInfo: ModuleWithComponents?,
                                    moduleAPI: Module?,
                                    jsonValueKeyMap: Map<String, AppCMSUIKeyType>,
                                    private val appCMSPresenter: AppCMSPresenter,
                                    pageView: PageView?,
                                    viewCreator: ViewCreator?,
                                    constraintViewCreator: ConstraintViewCreator,
                                    private var appCMSAndroidModules: AppCMSAndroidModules) : ModuleView<ModuleWithComponents?>(mContext, moduleInfo, false) {
    var pageView: PageView? = null
    var moduleAPI: Module?
    var mModuleinfo: ModuleWithComponents?
    var jsonValueKeyMap: Map<String, AppCMSUIKeyType>
    private val thumbnailType: String
    var module: ModuleList? = null
    var constraintViewCreator: ConstraintViewCreator
    var mConstraintLayout: ConstraintLayout
    var childContainer : ViewGroup? = null

    fun initViewConstraint() {
        if (childContainer == null) {
            childContainer = createTrayChildrenContainer()
        }
        if (module!!.components == null) return
        val size = module!!.components.size
        if (moduleAPI == null) {
            moduleAPI = Module()
            moduleAPI!!.id = module!!.id
        }
        var contentDatum: ContentDatum? = null
        contentDatum = if (moduleAPI != null && (moduleAPI!!.contentData == null || moduleAPI!!.contentData.size == 0)) ContentDatum() else moduleAPI!!.contentData[0]
        var metadataMap: MetadataMap? = null
        var headerView = false
        if (moduleAPI != null && moduleAPI!!.metadataMap != null) {
            metadataMap = moduleAPI!!.metadataMap
        }
        if( mConstraintLayout.childCount == 0) {
            for (i in 0 until size) {
                val childComponent = module!!.components[i]
                module!!.components[i].settings = module!!.settings
                val chieldView = constraintViewCreator.createComponentView(mContext,
                        childComponent,
                        moduleAPI,
                        jsonValueKeyMap,
                        childComponent.type,
                        moduleAPI!!.id,
                        module!!.blockName,
                        pageView,
                        module,
                        appCMSAndroidModules)
                if (chieldView != null) {
                    if (constraintViewCreator.componentViewResult.addToPageView) {
                        headerView = true
                        pageView!!.addToConstraintHeaderView(chieldView)
                        constraintViewCreator.setComponentViewRelativePosition(mContext,
                                chieldView,
                                contentDatum,
                                jsonValueKeyMap,
                                childComponent.type,
                                childComponent,
                                pageView!!.headerConstraintView,
                                module!!.blockName,
                                module,
                                metadataMap)
                    } else if (constraintViewCreator.componentViewResult.addBottomFloatingButton) {
                        pageView!!.addBottomFloatingButton(chieldView as ExtendedFloatingActionButton)
                        constraintViewCreator.componentViewResult.addBottomFloatingButton = false
                    } else {
                        mConstraintLayout.addView(chieldView)
                        constraintViewCreator.setComponentViewRelativePosition(mContext,
                                chieldView,
                                contentDatum,
                                jsonValueKeyMap,
                                childComponent.type,
                                childComponent,
                                mConstraintLayout,
                                module!!.blockName,
                                module,
                                metadataMap)
                    }
                }
                if (pageView != null) {
                    pageView!!.addViewWithComponentId(ViewWithComponentId.Builder()
                            .id(moduleAPI!!.id + childComponent.key)
                            .view(chieldView)
                            .build())
                }
            }
            if (headerView) pageView!!.reorderConstraintHeader()
            val presenterOnInternalEvents = appCMSPresenter.onInternalEvents
            if (presenterOnInternalEvents != null) {
                for (onInternalEvent in presenterOnInternalEvents) {
                    for (receiverInternalEvent in presenterOnInternalEvents) {
                        if (thumbnailType.equals("_32*9", ignoreCase = true)){
                            println(  " "+onInternalEvent.moduleId +" :- onInternalEvent.moduleId == receiverInternalEvent.moduleId -: " +receiverInternalEvent.moduleId)
                        }
                        if (receiverInternalEvent != onInternalEvent) {
                            if (thumbnailType.equals("_32*9", ignoreCase = true)){
                                println(  "receiverInternalEvent != onInternalEvent  "+onInternalEvent.moduleId +" :- onInternalEvent.moduleId == receiverInternalEvent.moduleId -: " +receiverInternalEvent.moduleId)
                            }
                            if (!TextUtils.isEmpty(onInternalEvent.moduleId) && onInternalEvent.moduleId == receiverInternalEvent.moduleId) {
                                onInternalEvent.addReceiver(receiverInternalEvent)
                            }
                        }
                    }
                }
            }
            childContainer!!.addView(mConstraintLayout)
        }
    }

    @JvmName("setModuleAPI1")
    fun setModuleAPI(moduleAPI: Module?) {
        this.moduleAPI = moduleAPI
    }

    init {
        appCMSAndroidModules = appCMSAndroidModules
        this.jsonValueKeyMap = jsonValueKeyMap
        this.moduleAPI = moduleAPI
        mModuleinfo = moduleInfo
        if (mModuleinfo!!.settings.contentType.equals("Audio", true)){
            thumbnailType = "_1*1"
        } else {
            thumbnailType = mModuleinfo!!.settings.thumbnailType
        }
        this.constraintViewCreator = constraintViewCreator
        mConstraintLayout = ConstraintLayout(mContext)
        mConstraintLayout.id = R.id.mConstraintLayout
        layoutParams = LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        mConstraintLayout.layoutParams = LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        mConstraintLayout.setBackgroundColor(appCMSPresenter.generalBackgroundColor)
        if (mModuleinfo != null) {
            val appCMSPageUILocal = getModuleList(mContext)
            val appCMSPageUI1 = GsonBuilder().create().fromJson(
                    Utils.loadJsonFromAssets(mContext, "trays.json"),
                    AppCMSPageUI::class.java)
            module = if (thumbnailType.equals("_32*9", ignoreCase = true)) appCMSPageUI1.moduleList[28] else appCMSPageUI1.moduleList[24]
            if (thumbnailType.equals("_1*1", ignoreCase = true)) {
                module = appCMSPageUI1.moduleList[10]
            }
            module!!.setId(mModuleinfo!!.id)
            module!!.setSettings(mModuleinfo!!.settings)
            module!!.setSvod(mModuleinfo!!.isSvod)
            module!!.setType(mModuleinfo!!.type)
            module!!.setView(mModuleinfo!!.view)
            module!!.setBlockName(mModuleinfo!!.blockName)
            if (!thumbnailType.equals("_32*9", ignoreCase = true)) {
                val ratio = CommonUtils.trayThumbnailRatio(thumbnailType)
                module!!.getComponents()[2].components[0].layout.mobile.ratio = ratio
                module!!.getComponents()[2].components[0].layout.tabletLandscape.ratio = ratio
                module!!.getComponents()[2].components[0].layout.tabletPortrait.ratio = ratio
                module!!.getComponents()[2].components[1].layout.mobile.ratio = ratio
                module!!.getComponents()[2].components[1].layout.tabletLandscape.ratio = ratio
                module!!.getComponents()[2].components[1].layout.tabletPortrait.ratio = ratio
            }else {
                module!!.settings.isLoop = true
                module!!.setBlockName("carousel04")
                module!!.setId(mModuleinfo!!.id+" "+thumbnailType)
            }
        }
    }
}