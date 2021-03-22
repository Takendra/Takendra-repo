package com.viewlift.extensions

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.annotation.StringRes
import com.viewlift.R
import com.viewlift.models.data.appcms.ui.page.Component
import com.viewlift.presenters.AppCMSPresenter
import com.viewlift.views.customviews.ViewCreator

fun View?.visible() {
    this?.visibility = View.VISIBLE
}

fun View?.invisible() {
    this?.visibility = View.INVISIBLE
}

fun View?.gone() {
    this?.visibility = View.GONE
}

fun ViewGroup.inflate(@LayoutRes resource: Int, attachToRoot: Boolean = false) : View {
    return LayoutInflater.from(context).inflate(resource, this, attachToRoot)
}

fun View.setTypeFace(appCMSPresenter: AppCMSPresenter, component: Component? = null)  {
    ViewCreator.setTypeFace(context, appCMSPresenter, appCMSPresenter.jsonValueKeyMap, component, this)
}