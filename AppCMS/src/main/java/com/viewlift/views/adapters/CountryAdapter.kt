package com.viewlift.views.adapters

import android.view.View
import android.view.ViewGroup
import com.viewlift.presenters.AppCMSPresenter

class CountryAdapter(appCMSPresenter: AppCMSPresenter) : BaseCountryAdapter(appCMSPresenter) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup)
            = getCommonView(position, convertView, parent)
}