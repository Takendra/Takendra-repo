package com.viewlift.views.customviews.moduleview.constraintview

import android.content.Context
import android.text.TextUtils
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton
import com.viewlift.R
import com.viewlift.models.data.appcms.api.ContentDatum
import com.viewlift.models.data.appcms.api.MetadataMap
import com.viewlift.models.data.appcms.api.Module
import com.viewlift.models.data.appcms.ui.AppCMSUIKeyType
import com.viewlift.models.data.appcms.ui.android.AppCMSAndroidModules
import com.viewlift.models.data.appcms.ui.page.ModuleWithComponents
import com.viewlift.presenters.AppCMSPresenter
import com.viewlift.views.customviews.ModuleView
import com.viewlift.views.customviews.PageView
import com.viewlift.views.customviews.ViewWithComponentId
import com.viewlift.views.customviews.constraintviews.ConstraintViewCreator

class StandAlonPlayerModule(context: Context,
                            private val moduleInfo: ModuleWithComponents,
                            moduleAPI: Module?,
                            jsonValueKeyMap: Map<String, AppCMSUIKeyType>,
                            appCMSPresenter: AppCMSPresenter,
                            pageView: PageView?,
                            constraintViewCreator: ConstraintViewCreator,
                            appCMSAndroidModules: AppCMSAndroidModules) : ModuleView<ModuleWithComponents?>(context, moduleInfo, false) {
    private var moduleAPI: Module?
    private val jsonValueKeyMap: Map<String, AppCMSUIKeyType>
    private val appCMSPresenter: AppCMSPresenter
    lateinit var mContext: Context
    private val appCMSAndroidModules: AppCMSAndroidModules
    var pageView: PageView?
    var mConstraintLayout: ConstraintLayout
    var constraintViewCreator: ConstraintViewCreator
    fun initViewConstraint() {
        val childContainer = getChildrenContainer()
        if (moduleInfo.components == null) return
        val size = moduleInfo.components.size
        if (moduleAPI == null) {
            moduleAPI = Module()
            moduleAPI!!.id = moduleInfo.id
        }
        var contentDatum: ContentDatum? = null
        contentDatum = if (moduleAPI != null && (moduleAPI!!.contentData == null || moduleAPI!!.contentData.size == 0)) ContentDatum() else moduleAPI!!.contentData[0]
        var metadataMap: MetadataMap? = null
        var headerView = false
        if (moduleAPI != null && moduleAPI!!.metadataMap != null) metadataMap = moduleAPI!!.metadataMap
        for (i in 0 until size) {
            val childComponent = moduleInfo.components[i]
            moduleInfo.components[i].settings = moduleInfo.settings
            val chieldView = constraintViewCreator.createComponentView(mContext,
                    childComponent,
                    moduleAPI,
                    jsonValueKeyMap,
                    childComponent.type,
                    moduleAPI!!.id,
                    moduleInfo.blockName,
                    pageView,
                    moduleInfo,
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
                            pageView!!.headerConstraintView, moduleInfo.blockName, moduleInfo,
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
                            mConstraintLayout, moduleInfo.blockName, moduleInfo,
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
                    if (receiverInternalEvent !== onInternalEvent) {
                        if (!TextUtils.isEmpty(onInternalEvent.moduleId) && onInternalEvent.moduleId == receiverInternalEvent.moduleId) {
                            onInternalEvent.addReceiver(receiverInternalEvent)
                        }
                    }
                }
            }
        }
        childContainer.addView(mConstraintLayout)
    }

    companion object {
        private val TAG = StandAlonPlayerModule::class.java.simpleName
    }

    init {
        this.moduleAPI = moduleAPI ?: Module()
        this.constraintViewCreator = constraintViewCreator
        this.appCMSAndroidModules = appCMSAndroidModules
        this.jsonValueKeyMap = jsonValueKeyMap
        this.appCMSPresenter = appCMSPresenter
        this.mContext = context
        this.pageView = pageView
        mConstraintLayout = ConstraintLayout(this.mContext)
        mConstraintLayout.id = R.id.stand_alone_video_player_container_id
        layoutParams = LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        mConstraintLayout.setBackgroundColor(appCMSPresenter.generalBackgroundColor)
        try {
            initViewConstraint()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}