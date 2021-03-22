package com.viewlift.views.adapters

import android.annotation.SuppressLint
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import com.viewlift.R
import com.viewlift.extensions.inflate
import com.viewlift.extensions.setTypeFace
import com.viewlift.models.data.appcms.getCountries
import com.viewlift.presenters.AppCMSPresenter
import kotlinx.android.synthetic.main.item_country_adapter.view.*

abstract class BaseCountryAdapter(private val appCMSPresenter: AppCMSPresenter) : BaseAdapter()  {

    protected val countryList by lazy {
        getCountries().sortedWith(compareBy { it.countryName })
    }

    override fun getCount()               = countryList.size
    override fun getItem(position: Int)   = countryList[position]
    override fun getItemId(position: Int) = position.toLong()

    fun getCountryCodeIndex(countryCode: String) = countryList.indexOfFirst { it.countryCode == countryCode }
    fun getCountryCode(position: Int)            = countryList[position]

    @SuppressLint("SetTextI18n")
    fun getCommonView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view: View
        if (convertView == null) {
            view = parent.inflate(R.layout.item_country_adapter)
            parent.setBackgroundColor(appCMSPresenter.generalBackgroundColor)
            view.countryName.setTextColor(appCMSPresenter.generalTextColor)
            view.countryCode.setTextColor(appCMSPresenter.generalTextColor)
            view.countryName.setTypeFace(appCMSPresenter)
            view.countryCode.setTypeFace(appCMSPresenter)
        } else {
            view = convertView
        }

        val currentCountry    = countryList[position]
        view.countryName.text = currentCountry.countryName
        view.countryCode.text = "+${currentCountry.countryCode}"
        return view
    }
}