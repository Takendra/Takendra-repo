package com.viewlift.views.adapters

import android.annotation.SuppressLint
import android.view.View
import android.view.ViewGroup
import com.viewlift.R
import com.viewlift.extensions.inflate
import com.viewlift.extensions.setTypeFace
import com.viewlift.presenters.AppCMSPresenter
import kotlinx.android.synthetic.main.element_country_code.view.countryCode

class CountrySpinnerAdapter(private val appCMSPresenter: AppCMSPresenter) : BaseCountryAdapter(appCMSPresenter) {

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup) = getCommonView(position, convertView, parent)

    @SuppressLint("SetTextI18n")
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view: View
        if (convertView == null) {
            view = parent.inflate(R.layout.element_country_code)
            parent.setBackgroundColor(appCMSPresenter.generalBackgroundColor)
            view.countryCode.setTextColor(appCMSPresenter.generalTextColor)
            view.countryCode.setTypeFace(appCMSPresenter)
        } else {
            view = convertView
        }
        view.countryCode.text = "+${countryList[position].countryCode}"
        return view
    }
}