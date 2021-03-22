package com.viewlift.views.customviews

import android.R
import android.annotation.SuppressLint
import android.content.Context
import android.text.TextUtils
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton
import com.viewlift.models.data.appcms.api.ContentDatum
import com.viewlift.models.data.appcms.api.MetadataMap
import com.viewlift.models.data.appcms.api.Module
import com.viewlift.models.data.appcms.ui.AppCMSUIKeyType
import com.viewlift.models.data.appcms.ui.android.AppCMSAndroidModules
import com.viewlift.models.data.appcms.ui.page.Component
import com.viewlift.models.data.appcms.ui.page.ModuleWithComponents
import com.viewlift.presenters.AppCMSPresenter
import com.viewlift.views.customviews.constraintviews.ConstraintViewCreator

@SuppressLint("ViewConstructor")
class TrayModule(context: Context,
                 private val moduleInfo: ModuleWithComponents?,
                  moduleAPI: Module?,
                 private val jsonValueKeyMap: Map<String, AppCMSUIKeyType>?,
                 appCMSPresenter: AppCMSPresenter,
                 pageView: PageView,
                 viewCreator: ViewCreator?,
                 constraintViewCreator: ConstraintViewCreator,
                 appCMSAndroidModules: AppCMSAndroidModules?) : ModuleView<ModuleWithComponents?>(context, moduleInfo, false) {
    private val appCMSPresenter: AppCMSPresenter
    private val viewCreator: ViewCreator?
    var moduleAPI: Module?
    var mContext: Context
    private val appCMSAndroidModules: AppCMSAndroidModules?
    var pageView: PageView
    var linearLayout: LinearLayout? = null
    var textView: TextView? = null
    var mLinearLayout: LinearLayout
    lateinit var blockName: String
    var constraintViewCreator: ConstraintViewCreator
    var mConstraintLayout: ConstraintLayout

    init {
        this.appCMSPresenter = appCMSPresenter
        this.viewCreator = viewCreator
        this.mContext = context
        this.appCMSAndroidModules = appCMSAndroidModules
        this.pageView = pageView
        this.moduleAPI = moduleAPI

        if (moduleInfo != null) {
            blockName = moduleInfo.blockName
        }

        mLinearLayout = LinearLayout(mContext)
        mLinearLayout.layoutParams = LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        mLinearLayout.orientation = LinearLayout.VERTICAL
        mLinearLayout.setBackgroundColor(appCMSPresenter.generalBackgroundColor)

        this.constraintViewCreator = constraintViewCreator
        mConstraintLayout = ConstraintLayout(mContext)
        mConstraintLayout.id = com.viewlift.R.id.mConstraintLayout
        layoutParams = LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        mConstraintLayout.layoutParams = LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        mConstraintLayout.setBackgroundColor(appCMSPresenter.generalBackgroundColor)

        //initializeView()
        initViewConstraint()
    }


    fun initViewConstraint() {
        val childContainer = createTrayChildrenContainer()
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
    }

    fun initializeView() {
        if (moduleInfo != null && moduleAPI != null && jsonValueKeyMap != null && appCMSPresenter != null && viewCreator != null) {
            val childContainer = getTrayChildrenContainer()

            childContainer.setBackgroundColor(ContextCompat.getColor(mContext, R.color.transparent))
            var module: ModuleWithComponents? = moduleInfo
            val moduleView: ModuleView<*> = ModuleView(mContext, module, true)
            if (module != null && module.components != null) {
                for (i in module.components.indices) {
                    val component = module.components[i]
                    addChildComponents(moduleView, component, appCMSAndroidModules, i)
                }
            }
            childContainer.addView(mLinearLayout)
        }
    }

    private fun addChildComponents(moduleView: ModuleView<*>,
                                   subComponent: Component,
                                   appCMSAndroidModules: AppCMSAndroidModules?, i: Int) {
        val componentViewResult = viewCreator!!.componentViewResult
        if (componentViewResult!!.onInternalEvent != null) {
            appCMSPresenter!!.addInternalEvent(componentViewResult.onInternalEvent)
        }
        val subComponentChildContainer = moduleView.getChildrenContainer()
        var componentType = jsonValueKeyMap!![subComponent.type]
        if (componentType == null) {
            componentType = AppCMSUIKeyType.PAGE_EMPTY_KEY
        }
        var componentKey = jsonValueKeyMap[subComponent.key]
        if (componentKey == null) {
            componentKey = AppCMSUIKeyType.PAGE_EMPTY_KEY
        }
        if (componentViewResult != null && subComponentChildContainer != null) {
            viewCreator.createComponentView(getContext(),
                    subComponent,
                    subComponent.layout,
                    moduleAPI,
                    appCMSAndroidModules,
                    pageView,
                    moduleInfo!!.settings,
                    jsonValueKeyMap,
                    appCMSPresenter,
                    false,
                    moduleInfo.view,
                    moduleInfo.id, moduleInfo.blockName, moduleInfo.isConstrainteView)
            val componentView = componentViewResult.componentView
            if (componentType == AppCMSUIKeyType.PAGE_SEPARATOR_VIEW_KEY) {
                val layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 5)
                layoutParams.setMargins(10, 5, 10, 15)
                componentView!!.layoutParams = layoutParams
            }
            if (componentView != null) {
                mLinearLayout.addView(componentView)
            }
        } else {
            moduleView.setComponentHasView(i, false)
        }
    }

    companion object {
        private val TAG = TrayModule::class.java.simpleName
    }


}