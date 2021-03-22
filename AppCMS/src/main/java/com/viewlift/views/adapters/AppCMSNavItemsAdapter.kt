package com.viewlift.views.adapters

import android.content.Context
import android.content.Intent
import android.content.res.Resources
import android.net.Uri
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.viewlift.AppCMSApplication
import com.viewlift.R
import com.viewlift.Utils
import com.viewlift.databinding.NavItemBinding
import com.viewlift.db.AppPreference
import com.viewlift.extensions.gone
import com.viewlift.extensions.invisible
import com.viewlift.extensions.visible
import com.viewlift.models.data.appcms.ui.AppCMSUIKeyType
import com.viewlift.models.data.appcms.ui.android.Navigation
import com.viewlift.models.data.appcms.ui.android.NavigationFooter
import com.viewlift.models.data.appcms.ui.android.NavigationPrimary
import com.viewlift.models.data.appcms.ui.android.NavigationUser
import com.viewlift.models.data.appcms.ui.main.LocalisedStrings
import com.viewlift.models.data.appcms.ui.page.Component
import com.viewlift.presenters.AppCMSPresenter
import com.viewlift.views.activity.WebViewActivity
import com.viewlift.views.customviews.BaseView
import com.viewlift.views.customviews.ViewCreator
import rx.functions.Action0
import java.util.*

class AppCMSNavItemsAdapter(context: Context, navigation: Navigation, jsonValueKeyMap: Map<String, AppCMSUIKeyType>,
                            userLoggedIn: Boolean, userSubscribed: Boolean, textColor: Int) :
        RecyclerView.Adapter<AppCMSNavItemsAdapter.ViewHolder>() {

    private var appCMSPresenter: AppCMSPresenter
    private var localisedStrings: LocalisedStrings
    private var appPreference: AppPreference
    private lateinit var pageFunction: String
    private var mContext: Context
    var userLoggedIn: Boolean
    var userSubscribed: Boolean
    private var jsonValueKeyMap: Map<String, AppCMSUIKeyType>
    private var numPrimaryItems = 0
    private var numUserItems = 0
    private var numFooterItems = 0
    private var numItemClickedPosition = -1
    private var menuNavigation: Navigation = Navigation()
    private var textColor = 0
    private var itemSelected = false
    private var singleMenuIconAvailable = true
    private var singleSeparatorAvailable = true

    init {
        appCMSPresenter = (context.applicationContext as AppCMSApplication)
                .appCMSPresenterComponent
                .appCMSPresenter()

        localisedStrings = (context.applicationContext as AppCMSApplication)
                .appCMSPresenterComponent
                .localisedStrings()

        appPreference = (context.applicationContext as AppCMSApplication)
                .appCMSPresenterComponent
                .appPreference()
        this.jsonValueKeyMap = jsonValueKeyMap
        this.mContext = context
        this.userLoggedIn = userLoggedIn
        this.userSubscribed = userSubscribed
        this.textColor = textColor
        removePlans(navigation)
    }

    private fun removePlans(nav: Navigation) {
        for (i in nav.getNavigationUser().indices) {
            val nu: NavigationUser = nav.getNavigationUser().get(i)
            pageFunction = appCMSPresenter.getPageFunctionValue(nu.getPageId(), nu.title)
            val titleKey: AppCMSUIKeyType? = jsonValueKeyMap.get(pageFunction)
            if (!appCMSPresenter.isDownloadable && (titleKey == AppCMSUIKeyType.ANDROID_DOWNLOAD_NAV_KEY || titleKey == AppCMSUIKeyType.ANDROID_DOWNLOAD_KEY)) {
                nav.getNavigationUser().removeAt(i)
                break
            }
        }
        if (nav.navigationPrimary != null)
            for (k in nav.navigationPrimary.indices) {
                if ((nav.navigationPrimary[k].title.toLowerCase(Locale.ROOT).equals(mContext.getString(R.string.app_cms_more_screen_tag).toLowerCase(Locale.ROOT)) ||
                                nav.navigationPrimary[k].title.toLowerCase(Locale.ROOT).equals(mContext.getString(R.string.app_cms_menu_tag).toLowerCase(Locale.ROOT))) && nav.navigationPrimary[k].items == null) {
                    nav.navigationPrimary.removeAt(k)
                    break
                }
            }
        if (!userLoggedIn) {
            menuNavigation.navigationFooter = nav.navigationFooter?.filter { it.accessLevels != null && it.accessLevels.isLoggedOut }
            menuNavigation.settings = nav.settings
            val npList: MutableList<NavigationPrimary> = ArrayList()
            if (nav.navigationPrimary != null) {
                for (i in nav.navigationPrimary.indices) {
                    val np = nav.navigationPrimary[i]
                    if (np.accessLevels != null && np.accessLevels.isLoggedOut) {
                        npList.add(np)
                    }
                }
            }
            menuNavigation.setNavigationPrimary(npList)
        } else if (userSubscribed) {
            menuNavigation.navigationFooter = nav.navigationFooter?.filter { it.accessLevels != null && (it.accessLevels.isLoggedIn || it.accessLevels.isSubscribed) }
            menuNavigation.navigationFooter?.forEach { it.accessLevels.isLoggedIn = true }
            menuNavigation.settings = nav.settings
            val npList: MutableList<NavigationPrimary> = ArrayList()
            if (nav.navigationPrimary != null) {
                for (i in nav.navigationPrimary.indices) {
                    val np = nav.navigationPrimary[i]
                    if (np.accessLevels != null
                            && np.accessLevels.isSubscribed) {
                        npList.add(np)
                    }
                }
            }
            menuNavigation.setNavigationPrimary(npList)
            val nuList: MutableList<NavigationUser> = ArrayList()
            for (i in nav.navigationUser.indices) {
                val nu = nav.navigationUser[i]
                if (!appCMSPresenter.isViewPlanPage(nu.getPageId())) {
                    nuList.add(nu)
                }
            }
            menuNavigation.setNavigationUser(nuList)
        } else if (!userSubscribed) {
            menuNavigation.navigationFooter = nav.navigationFooter
            menuNavigation.settings = nav.settings
            val npList: MutableList<NavigationPrimary> = ArrayList()
            if (nav.navigationPrimary != null) {
                for (i in nav.navigationPrimary.indices) {
                    val np = nav.navigationPrimary[i]
                    if (np.accessLevels != null
                            && np.accessLevels.isLoggedIn) {
                        npList.add(np)
                    }
                }
            }
            menuNavigation.setNavigationPrimary(npList)
            menuNavigation.setNavigationUser(nav.navigationUser)
        } else {
            menuNavigation = nav
        }
        singleMenuIconAvailable = menuNavigation.navigationPrimary.any { it.icon != null }
                || (menuNavigation.navigationUser != null && menuNavigation.navigationUser.any { it.icon != null })
                || menuNavigation.navigationFooter.any { it.icon != null }
        singleSeparatorAvailable = menuNavigation.navigationPrimary.any { it.isAddSeparator == true }
                || (menuNavigation.navigationUser != null && menuNavigation.navigationUser.any { it.isAddSeparator == true })
                || menuNavigation.navigationFooter.any { it.isAddSeparator == true }
    }

    fun resetMenu(navigation: Navigation) {
        removePlans(navigation)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AppCMSNavItemsAdapter.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = NavItemBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }


    override fun onBindViewHolder(holder: AppCMSNavItemsAdapter.ViewHolder, position: Int) {
        var indexOffset = 0
        holder.binding.navItemLabel.text = ""
        val component = Component();
        ViewCreator.setTypeFace(holder.itemView.context, appCMSPresenter, appCMSPresenter.jsonValueKeyMap, component, holder.binding.navItemLabel)
        if (position >= numPrimaryItems) {
            indexOffset += numPrimaryItems
        } else {
            var foundViewableItem = false
            var j: Int = position
            while (j < menuNavigation.getNavigationPrimary().size && !foundViewableItem) {
                if (menuNavigation.getNavigationPrimary().get(j).accessLevels != null) {
                    if (userLoggedIn && !menuNavigation.getNavigationPrimary().get(j).accessLevels.isLoggedIn ||
                            !userLoggedIn && !menuNavigation.getNavigationPrimary().get(j).accessLevels.isLoggedOut ||
                            userSubscribed && !menuNavigation.getNavigationPrimary().get(j).accessLevels.isSubscribed) {
                        indexOffset++
                    } else {
                        foundViewableItem = true
                    }
                }
                j++
            }
        }
        if (!singleMenuIconAvailable)
            holder.binding.navItemLogo.gone()
        if (numItemClickedPosition == position) {
            holder.binding.navItemSelector.visible()
            holder.binding.navItemSelector.setBackgroundColor(appCMSPresenter.brandPrimaryCtaColor)
            holder.binding.navItemLogo.setColorFilter(appCMSPresenter.brandPrimaryCtaColor, android.graphics.PorterDuff.Mode.SRC_IN);
            holder.binding.navItemLabel.setTextColor(appCMSPresenter.brandPrimaryCtaColor)
            component.fontWeight = mContext.getString(R.string.app_cms_page_font_bold_key)
            ViewCreator.setTypeFace(holder.itemView.context, appCMSPresenter, appCMSPresenter.jsonValueKeyMap, component, holder.binding.navItemLabel)
        } else {
            holder.binding.navItemSelector.invisible()
            holder.binding.navItemLogo.setColorFilter(textColor, android.graphics.PorterDuff.Mode.SRC_IN);
        }
        if (menuNavigation.getNavigationPrimary() != null &&
                (position + indexOffset) < menuNavigation.getNavigationPrimary().size
                && position < numPrimaryItems) {
            val navigationPrimary: NavigationPrimary = menuNavigation.getNavigationPrimary().get(position + indexOffset)
            if (navigationPrimary.getAccessLevels() != null) {
                if (userLoggedIn && navigationPrimary.accessLevels.isLoggedIn ||
                        !userLoggedIn && navigationPrimary.accessLevels.isLoggedOut ||
                        !userSubscribed && !navigationPrimary.accessLevels.isSubscribed) {
                    var localisedTitle: String? = null
                    if (navigationPrimary.getLocalizationMap() !== null)
                        localisedTitle = appCMSPresenter.getNavigationTitle(navigationPrimary.getLocalizationMap())
                    val navigationTitleEN = appCMSPresenter.getNavigationTitleForEN(navigationPrimary.getLocalizationMap())
                    holder.binding.navItemLabel.text = localisedTitle?.toUpperCase(Locale.ROOT)
                            ?: navigationPrimary.getTitle().toUpperCase(Locale.ROOT)
                    holder.binding.navItemLabel.setTextColor(textColor)
                    setMenuImage(navigationPrimary.icon, holder.binding.navItemLogo, holder.binding.navItemLabel)
                    if (navigationPrimary.isAddSeparator)
                        holder.binding.navItemSeperator.visible()
                    else
                        decreaseSeparatorMargin(holder.binding.navItemSeperator)
                    setMenuSpace(navigationPrimary.icon, navigationPrimary.isAddSeparator, holder.binding.navItemLabel)
                    if (numItemClickedPosition == position) {
                        holder.binding.navItemLogo.setColorFilter(appCMSPresenter.brandPrimaryCtaColor, android.graphics.PorterDuff.Mode.SRC_IN);
                        holder.binding.navItemLabel.setTextColor(appCMSPresenter.brandPrimaryCtaColor)
                    } else {
                        holder.binding.navItemLogo.setColorFilter(textColor, android.graphics.PorterDuff.Mode.SRC_IN);
                    }
                    holder.itemView.setOnClickListener {
                        numItemClickedPosition = position
                        notifyDataSetChanged()
                        pageFunction = appCMSPresenter.getPageFunctionValue(navigationPrimary.getPageId(), navigationPrimary.title)
                        var titleKey = jsonValueKeyMap[pageFunction]
                        if (titleKey == null) {
                            titleKey = AppCMSUIKeyType.PAGE_EMPTY_KEY
                        }
                        if (navigationPrimary.displayedPath != null && navigationPrimary.items != null && navigationPrimary.items.size > 0) {
                            if (navigationPrimary.displayedPath.toLowerCase(Locale.ROOT).equals("Sub Navigation Screen".toLowerCase(Locale.ROOT), ignoreCase = true) ||
                                    navigationPrimary.displayedPath.toLowerCase(Locale.ROOT).equals("Subnavigation Screen".toLowerCase(Locale.ROOT), ignoreCase = true) ||
                                    navigationPrimary.displayedPath.toLowerCase(Locale.ROOT).equals("More".toLowerCase(Locale.ROOT), ignoreCase = true)) {
                                appCMSPresenter.navigateToDisplayPage(navigationPrimary.getPageId(),
                                        navigationPrimary.title,
                                        navigationPrimary.url,
                                        false,
                                        true,
                                        false,
                                        true,
                                        false,
                                        null, navigationPrimary.items)
                            } else if (navigationPrimary.isOpenInChromeCustomTab && !navigationPrimary.url.isEmpty()) {
                                var url = navigationPrimary.url
                                if (!url.startsWith(mContext.getString(R.string.https_scheme))) {
                                    url = appCMSPresenter.appCMSMain.domainName + navigationPrimary.url + "?app=true"
                                }
                                appCMSPresenter.openChromeTab(url)
                            } else
                                appCMSPresenter.navigateToPage(navigationPrimary.getPageId(),
                                        navigationPrimary.title,
                                        navigationPrimary.url,
                                        false,
                                        true,
                                        false,
                                        true,
                                        false,
                                        null)
                        } else if (titleKey == AppCMSUIKeyType.ANDROID_SUBSCRIPTION_SCREEN_KEY || appCMSPresenter.isViewPlanPage(navigationPrimary.getPageId())) {
                            appCMSPresenter.navigateToSubscriptionPlansPage(true)
                        } else if (titleKey == AppCMSUIKeyType.PAGE_TEAMS_KEY) {
                            appCMSPresenter.launchTeamNavPage()
                        } else if (titleKey == AppCMSUIKeyType.PAGE_SCHEDULE_SCREEN_TITLE_KEY ||
                                titleKey == AppCMSUIKeyType.ANDROID_SCHEDULE_SCREEN_KEY) {
                            appCMSPresenter.navigateToSchedulePage(navigationPrimary.getPageId(),
                                    navigationPrimary.title, false)
                        } else if (titleKey == AppCMSUIKeyType.LANGUAGE_SCREEN_KEY ||
                                navigationPrimary.displayedPath != null && navigationPrimary.displayedPath.contains("Language Settings") || navigationTitleEN != null && navigationTitleEN.contains("LANGUAGE")) {
                            appCMSPresenter.showLanguageScreen(navigationPrimary.getPageId(), navigationPrimary.title)
                        } else if (titleKey == AppCMSUIKeyType.PAGE_ROSTER_SCREEN_TITLE_KEY || titleKey == AppCMSUIKeyType.PAGE_FIGHTER_SCREEN_TITLE_KEY) {
                            appCMSPresenter.navigateToRosterPage(navigationPrimary.getPageId(),
                                    navigationPrimary.title, false)
                        } else if (titleKey == AppCMSUIKeyType.ANDROID_WATCHLIST_SCREEN_KEY) {
                            if (!appCMSPresenter.isUserLoggedIn) {
                                appCMSPresenter.isAppbarPresent = false
                                appCMSPresenter.showEntitlementDialog(AppCMSPresenter.DialogType.LOGIN_REQUIRED,
                                        null, null)
                                return@setOnClickListener
                            }
                            appCMSPresenter.showLoadingDialog(true)
                            appCMSPresenter.navigateToWatchlistPage(navigationPrimary.getPageId(),
                                    navigationPrimary.title, navigationPrimary.url, true, true, false)
                        } else if (pageFunction.equals(mContext.getString(R.string.contact_us), ignoreCase = true) ||
                                pageFunction.contains(mContext.getString(R.string.contact_us)) ||
                                pageFunction.contains(mContext.getString(R.string.contact_us_roku)) ||
                                pageFunction.contains(mContext.getString(R.string.contact_us)) ||
                                pageFunction.equals(mContext.getString(R.string.support), ignoreCase = true)) {
                            if (appCMSPresenter.isFreshChatEnable) {
                                appCMSPresenter.launchFreshChat(appCMSPresenter.appCMSMain.customerService.freshChat?.appID,
                                        appCMSPresenter.appCMSMain.customerService.freshChat?.key, false)
                            }
                        } else if (!appCMSPresenter.navigateToPage(navigationPrimary.getPageId(),
                                        navigationPrimary.title,
                                        navigationPrimary.url,
                                        false,
                                        true,
                                        false,
                                        true,
                                        false,
                                        null)) {
                            //Log.e(TAG, "Could not navigate to page with Title: " +
//                                    navigationPrimary.getTitle() +
//                                    " Id: " +
//                                    navigationPrimary.getPageId());
                        } else {
                            itemSelected = true
                        }
                    }
                }
            }
        } else {
            if (userLoggedIn && menuNavigation.getNavigationUser() != null) {
                var j = 0
                while (j <= (position - indexOffset) && j < menuNavigation.getNavigationUser().size) {
                    if (menuNavigation.getNavigationUser().get(j).getAccessLevels() != null) {
                        if (userLoggedIn && !menuNavigation.getNavigationUser().get(j).accessLevels.isLoggedIn ||
                                !userLoggedIn && !menuNavigation.getNavigationUser().get(j).accessLevels.isLoggedOut) {
                            indexOffset--
                        }
                    }
                    j++
                }
            }
            //user nav
            if (userLoggedIn && menuNavigation.getNavigationUser() != null &&
                    0 <= (position - indexOffset) && (position - indexOffset) < menuNavigation.getNavigationUser().size) {
                val navigationUser: NavigationUser = menuNavigation.getNavigationUser().get(position - indexOffset)
                var localisedTitle: String? = null
                if (navigationUser.getLocalizationMap() !== null)
                    localisedTitle = appCMSPresenter.getNavigationTitle(navigationUser.getLocalizationMap())
                if (navigationUser != null && navigationUser.accessLevels != null && navigationUser.title != null) {
                    if (userLoggedIn && navigationUser.accessLevels.isLoggedIn ||
                            !userLoggedIn && navigationUser.accessLevels.isLoggedOut) {
                        holder.binding.navItemLabel.text = localisedTitle?.toUpperCase(Locale.ROOT)
                                ?: navigationUser.getTitle().toUpperCase(Locale.ROOT)
                        holder.binding.navItemLabel.setTextColor(textColor)
                        setMenuImage(navigationUser.icon, holder.binding.navItemLogo, holder.binding.navItemLabel)
                        if (navigationUser.isAddSeparator)
                            holder.binding.navItemSeperator.visible()
                        else
                            decreaseSeparatorMargin(holder.binding.navItemSeperator)
                        setMenuSpace(navigationUser.icon, navigationUser.isAddSeparator, holder.binding.navItemLabel)
                        if (numItemClickedPosition == position) {
                            holder.binding.navItemLogo.setColorFilter(appCMSPresenter.brandPrimaryCtaColor, android.graphics.PorterDuff.Mode.SRC_IN);
                            holder.binding.navItemLabel.setTextColor(appCMSPresenter.brandPrimaryCtaColor)
                        } else {
                            holder.binding.navItemLogo.setColorFilter(textColor, android.graphics.PorterDuff.Mode.SRC_IN);
                        }
                        holder.itemView.setOnClickListener {
                            numItemClickedPosition = position
                            notifyDataSetChanged()
                            appCMSPresenter.cancelInternalEvents()
                            pageFunction = appCMSPresenter.getPageFunctionValue(navigationUser.getPageId(), navigationUser.title)
                            var titleKey = jsonValueKeyMap[pageFunction]
                            if (titleKey == null) {
                                titleKey = AppCMSUIKeyType.PAGE_EMPTY_KEY
                            }
                            itemSelected = true
                            when (titleKey) {
                                AppCMSUIKeyType.ANDROID_DOWNLOAD_KEY, AppCMSUIKeyType.ANDROID_DOWNLOAD_NAV_KEY -> {
                                    appCMSPresenter.showLoadingDialog(true)
                                    appCMSPresenter.navigateToDownloadPage(navigationUser.getPageId())
                                }
                                AppCMSUIKeyType.ANDROID_SCHEDULE_SCREEN_KEY, AppCMSUIKeyType.PAGE_SCHEDULE_SCREEN_TITLE_KEY -> {
                                    appCMSPresenter.navigateToSchedulePage(navigationUser.getPageId(),
                                            navigationUser.title, false)
                                }
                                AppCMSUIKeyType.PAGE_ROSTER_SCREEN_TITLE_KEY, AppCMSUIKeyType.PAGE_FIGHTER_SCREEN_TITLE_KEY -> {
                                    appCMSPresenter.navigateToRosterPage(navigationUser.getPageId(),
                                            navigationUser.title, false)
                                }
                                AppCMSUIKeyType.ANDROID_WATCHLIST_NAV_KEY, AppCMSUIKeyType.ANDROID_WATCHLIST_SCREEN_KEY -> {
                                    if (!appCMSPresenter.isNetworkConnected) {
                                        if (!appCMSPresenter.isUserLoggedIn) {
                                            appCMSPresenter.showDialog(AppCMSPresenter.DialogType.NETWORK, null, false, Action0 { appCMSPresenter.launchBlankPage() },
                                                    null, null)
                                            return@setOnClickListener
                                        }
                                        appCMSPresenter.showDialog(AppCMSPresenter.DialogType.NETWORK,
                                                localisedStrings.internetConnectionMsg,
                                                true,
                                                Action0 { appCMSPresenter.navigateToDownloadPage(appPreference.getDownloadPageId()) },
                                                null, null)
                                        return@setOnClickListener
                                    }
                                    appCMSPresenter.showLoadingDialog(true)
                                    appCMSPresenter.navigateToWatchlistPage(navigationUser.getPageId(),
                                            navigationUser.title, navigationUser.url, false, false, false)
                                }
                                AppCMSUIKeyType.ANDROID_LIBRARY_NAV_KEY, AppCMSUIKeyType.ANDROID_LIBRARY_SCREEN_KEY -> {

                                    if (!appCMSPresenter.isNetworkConnected) {
                                        if (!appCMSPresenter.isUserLoggedIn) {
                                            appCMSPresenter.showDialog(AppCMSPresenter.DialogType.NETWORK, null, false, Action0 { appCMSPresenter.launchBlankPage() },
                                                    null, null)
                                            return@setOnClickListener
                                        }
                                        appCMSPresenter.showDialog(AppCMSPresenter.DialogType.NETWORK,
                                                localisedStrings.internetConnectionMsg,
                                                true,
                                                Action0 { appCMSPresenter.navigateToDownloadPage(appPreference.getDownloadPageId()) },
                                                null, null)
                                        return@setOnClickListener
                                    }
                                    appCMSPresenter.showLoadingDialog(true)
                                    appCMSPresenter.navigateToLibraryPage(navigationUser.getPageId(),
                                            navigationUser.title, false, false);
                                }
                                AppCMSUIKeyType.ANDROID_HISTORY_NAV_KEY, AppCMSUIKeyType.ANDROID_HISTORY_SCREEN_KEY -> {

                                    if (!appCMSPresenter.isNetworkConnected) {
                                        if (!appCMSPresenter.isUserLoggedIn) {
                                            appCMSPresenter.showDialog(AppCMSPresenter.DialogType.NETWORK, null, false, null, null, null)
                                            return@setOnClickListener
                                        }
                                        appCMSPresenter.showDialog(AppCMSPresenter.DialogType.NETWORK,
                                                localisedStrings.internetConnectionMsg,
                                                true,
                                                Action0 { appCMSPresenter.navigateToDownloadPage(appPreference.getDownloadPageId()) },
                                                null, null)
                                        return@setOnClickListener
                                    }
                                    appCMSPresenter.showLoadingDialog(true)
                                    appCMSPresenter.navigateToHistoryPage(navigationUser.getPageId(),
                                            navigationUser.title, navigationUser.url, false, false)
                                }
                                else -> {
                                    if (!appCMSPresenter.isNetworkConnected && titleKey != AppCMSUIKeyType.ANDROID_DOWNLOAD_NAV_KEY) {
                                        if (!appCMSPresenter.isUserLoggedIn) {
                                            appCMSPresenter.showDialog(AppCMSPresenter.DialogType.NETWORK,
                                                    null,
                                                    false,
                                                    null,
                                                    null, null)
                                            return@setOnClickListener
                                        }
                                        appCMSPresenter.showDialog(AppCMSPresenter.DialogType.NETWORK,
                                                localisedStrings.internetConnectionMsg,
                                                true,
                                                Action0 { appCMSPresenter.navigateToDownloadPage(appPreference.getDownloadPageId()) },
                                                null, null)
                                        return@setOnClickListener
                                    }
                                    var appBarPresent = false
                                    var navBarPresent = false
                                    if (appCMSPresenter.isViewPlanPage(navigationUser.getPageId())) {
                                        appBarPresent = true
                                    }
                                    if (navigationUser.url.equals(mContext.getString(R.string.referral_key_for_deeplink), ignoreCase = true)) {
                                        appBarPresent = true
                                        navBarPresent = true
                                    }
                                    if (!appCMSPresenter.navigateToPage(navigationUser.getPageId(),
                                                    navigationUser.title,
                                                    navigationUser.url,
                                                    false,
                                                    appBarPresent,
                                                    false,
                                                    navBarPresent,
                                                    false,
                                                    null)) {
                                        //Log.e(TAG, "Could not navigate to page with Title: "
//                                                + navigationUser.getTitle() + " Id: " + navigationUser.getPageId());
                                    }
                                }
                            }
                        }
                    }
                }
            }

            indexOffset = numPrimaryItems + numUserItems
            if (userLoggedIn && menuNavigation.getNavigationFooter() != null) {
                var j = 0
                while (j <= (position - indexOffset) && position < menuNavigation.getNavigationFooter().size) {
                    if (menuNavigation.getNavigationFooter().get(j).getAccessLevels() != null) {
                        if (userLoggedIn && !menuNavigation.getNavigationFooter().get(j).accessLevels.isLoggedIn ||
                                !userLoggedIn && !menuNavigation.getNavigationFooter().get(j).accessLevels.isLoggedOut) {
                            indexOffset--
                        }
                    }
                    j++
                }
            }
            //footer
            if (listFooterFinal != null && 0 <= (position - indexOffset) &&
                    (position - indexOffset) < listFooterFinal.size) {
                //val navigationFooter: NavigationFooter = menuNavigation.getNavigationFooter().get(position - indexOffset)
                val navigationFooter: NavigationFooter = listFooterFinal.elementAt(position - indexOffset)
                var localisedTitle: String? = null
                if (navigationFooter.getLocalizationMap() !== null)
                    localisedTitle = appCMSPresenter.getNavigationTitle(navigationFooter.getLocalizationMap())
                if (navigationFooter.getAccessLevels() != null) {
                    if (userLoggedIn && navigationFooter.accessLevels.isLoggedIn ||
                            !userLoggedIn && navigationFooter.accessLevels.isLoggedOut) {
                        holder.binding.navItemLabel.text = localisedTitle?.toUpperCase(Locale.ROOT)
                                ?: navigationFooter.getTitle().toUpperCase(Locale.ROOT)
                        holder.binding.navItemLabel.setTextColor(textColor)
                        setMenuImage(navigationFooter.icon, holder.binding.navItemLogo, holder.binding.navItemLabel)
                        if (navigationFooter.isAddSeparator)
                            holder.binding.navItemSeperator.visible()
                        else
                            decreaseSeparatorMargin(holder.binding.navItemSeperator)
                        setMenuSpace(navigationFooter.icon, navigationFooter.isAddSeparator, holder.binding.navItemLabel)
                        if (numItemClickedPosition == position) {
                            holder.binding.navItemLogo.setColorFilter(appCMSPresenter.brandPrimaryCtaColor, android.graphics.PorterDuff.Mode.SRC_IN);
                            holder.binding.navItemLabel.setTextColor(appCMSPresenter.brandPrimaryCtaColor)
                        } else {
                            holder.binding.navItemLogo.setColorFilter(textColor, android.graphics.PorterDuff.Mode.SRC_IN);
                        }
                        holder.itemView.setOnClickListener {
                            numItemClickedPosition = position
                            notifyDataSetChanged()
                            pageFunction = appCMSPresenter.getPageFunctionValue(navigationFooter.getPageId(), navigationFooter.title)
                            //Log.d(TAG, "Navigating to page with Title position: " + i);
                            //Log.d(TAG, "Navigating to page with Title position: " + i);
                            if (!appCMSPresenter.isNetworkConnected) {
                                if (!appCMSPresenter.isUserLoggedIn) {
                                    appCMSPresenter.showDialog(AppCMSPresenter.DialogType.NETWORK,
                                            null,
                                            false,
                                            null,
                                            null, null)
                                    return@setOnClickListener
                                }
                                appCMSPresenter.showDialog(AppCMSPresenter.DialogType.NETWORK,
                                        localisedStrings.internetConnectionMsg,
                                        true,
                                        Action0 { appCMSPresenter.navigateToDownloadPage(appPreference.getDownloadPageId()) },
                                        null, null)
                                return@setOnClickListener
                            }
                            appCMSPresenter.cancelInternalEvents()
                            itemSelected = true
                            if (TextUtils.isEmpty(navigationFooter.getPageId()) && Utils.isValid(navigationFooter.displayedPath)) {
                                if (navigationFooter.isOpenInChromeCustomTab) {
                                    appCMSPresenter.openChromeTab(navigationFooter.displayedPath + "?app=true")
                                } else {
                                    val intent = Intent(holder.itemView.context, WebViewActivity::class.java)
                                    intent.putExtra("url", navigationFooter.displayedPath + "?app=true")
                                    holder.itemView.context.startActivity(intent)
                                }
                            } else if (appCMSPresenter.isAncillaryPage(navigationFooter.getPageId())) {
                                openAncillaryPage(holder.itemView.context, navigationFooter)
                            } else if (navigationFooter.isOpenInChromeCustomTab && pageFunction.equals(mContext.getString(R.string.app_cms_page_shop_title), ignoreCase = true) && !TextUtils.isEmpty(pageFunction)) {
                                appCMSPresenter.openChromeTab(navigationFooter.url)
                            } /*else if (pageFunction.equals(mContext.getString(R.string.app_cms_pagename_refer_earn_key), ignoreCase = true) ||
                                    pageFunction.equals(mContext.getString(R.string.app_cms_pagename_refer_and_earn_key), ignoreCase = true)) {
                                appCMSPresenter.launchAppFragment(mContext.getString(R.string.app_cms_get_social_page_tag),
                                        LaunchData(null, false, false,
                                                null, null, null, null, false,
                                                -1, null, null, null), ExtraScreenType.GET_SOCIAL, true, null, null)
                            }*/ else if (navigationFooter.isOpenInChromeCustomTab && appCMSPresenter.appCMSMain != null && !TextUtils.isEmpty(appCMSPresenter.appCMSMain.domainName)
                                    && !TextUtils.isEmpty(navigationFooter.url)
                                    && (pageFunction.equals(mContext.getString(R.string.app_cms_pagename_privacy_policy_key), ignoreCase = true)
                                            || pageFunction.equals(mContext.getString(R.string.app_cms_pagename_terms_of_services_key), ignoreCase = true)
                                            || pageFunction.equals(mContext.getString(R.string.app_cms_pagename_about_us_key), ignoreCase = true))) {
                                val url = appCMSPresenter.appCMSMain.domainName + navigationFooter.url + "?app=true"
                                appCMSPresenter.openChromeTab(url)
                            } else if (pageFunction.equals(mContext.getString(R.string.frequently_asked_questions), ignoreCase = true)
                                    || pageFunction.toLowerCase(Locale.ROOT).contains(mContext.getString(R.string.faq).toLowerCase(Locale.ROOT))) {
                                if (appCMSPresenter.isFreshChatEnable) {
                                    appCMSPresenter.launchFreshChat(appCMSPresenter.appCMSMain.customerService.freshChat?.appID,
                                            appCMSPresenter.appCMSMain.customerService.freshChat?.key, true)
                                } else if (navigationFooter.isOpenInChromeCustomTab && appCMSPresenter.appCMSMain != null && !TextUtils.isEmpty(appCMSPresenter.appCMSMain.domainName)
                                        && !TextUtils.isEmpty(navigationFooter.url)) {
                                    val url = appCMSPresenter.appCMSMain.domainName + navigationFooter.url + "?app=true"
                                    appCMSPresenter.openChromeTab(url)
                                } else {
                                    appCMSPresenter.navigateToPage(navigationFooter.getPageId(),
                                            navigationFooter.title,
                                            navigationFooter.url,
                                            false,
                                            false,
                                            false,
                                            false,
                                            false,
                                            null)
                                }
                            } else if (pageFunction.equals(mContext.getString(R.string.contact_us), ignoreCase = true) ||
                                    pageFunction.contains(mContext.getString(R.string.contact_us)) ||
                                    pageFunction.contains(mContext.getString(R.string.contact_us_roku)) ||
                                    pageFunction.contains(mContext.getString(R.string.contact_us)) ||
                                    pageFunction.equals(mContext.getString(R.string.support), ignoreCase = true)) {
                                if (appCMSPresenter.isFreshChatEnable) {
                                    /*appCMSPresenter.launchFreshChat("1c6d375c-3b3c-4248-bee4-689c7e3a2fe2",
                                            "5f30b939-605d-4734-8e45-171657cdb57c", false);*/
                                    appCMSPresenter.launchFreshChat(appCMSPresenter.appCMSMain.customerService.freshChat?.appID,
                                            appCMSPresenter.appCMSMain.customerService.freshChat?.key, false)
                                } else  //For contactUs link openig mail intent
                                    if (navigationFooter != null) {
                                        var contactUsEmail: String? = if (navigationFooter.displayedPath.contains("mailto:")) navigationFooter.displayedPath.substring(7) else ""
                                        if ((contactUsEmail == null || TextUtils.isEmpty(contactUsEmail)) && appCMSPresenter.appCMSMain.customerService != null && appCMSPresenter.appCMSMain.customerService.email != null) contactUsEmail = appCMSPresenter.appCMSMain.customerService.email
                                        val intent = Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                                                "mailto", contactUsEmail, null))
                                        appCMSPresenter.currentActivity.startActivity(Intent.createChooser(intent, navigationFooter.title))
                                    } else {
                                        appCMSPresenter.navigateToPage(navigationFooter.getPageId(),
                                                navigationFooter.title,
                                                navigationFooter.url,
                                                false,
                                                false,
                                                false,
                                                false,
                                                false,
                                                null)
                                    }
                            } else if (!appCMSPresenter.navigateToPage(navigationFooter.getPageId(),
                                            navigationFooter.title,
                                            navigationFooter.url,
                                            false,
                                            false,
                                            false,
                                            false,
                                            false,
                                            null)) {
                                //Log.e(TAG, "Could not navigate to page with Title: " +
//                                        navigationFooter.getTitle() +
//                                        " Id: " +
//                                        navigationFooter.getPageId());
                            }
                        }
                    }
                }

            }
            indexOffset = numPrimaryItems + numUserItems + numFooterItems
            if (0 <= (position - indexOffset) && userLoggedIn) {
                val logoutText = appCMSPresenter.localizedLogoutText.toUpperCase(Locale.ROOT)
                holder.binding.navItemLabel.text = logoutText.toUpperCase(Locale.ROOT)
                holder.binding.navItemLabel.setTextColor(textColor)
                setMenuImage(menuNavigation.settings.primaryCta.logoutIcon, holder.binding.navItemLogo, holder.binding.navItemLabel)
                if (numItemClickedPosition == position) {
                    holder.binding.navItemLogo.setColorFilter(appCMSPresenter.brandPrimaryCtaColor, android.graphics.PorterDuff.Mode.SRC_IN);
                    holder.binding.navItemLabel.setTextColor(appCMSPresenter.brandPrimaryCtaColor)
                } else {
                    holder.binding.navItemLogo.setColorFilter(textColor, android.graphics.PorterDuff.Mode.SRC_IN);
                }
                holder.itemView.setOnClickListener {
                    if (!appCMSPresenter.isNetworkConnected) {
                        appCMSPresenter.showDialog(AppCMSPresenter.DialogType.NETWORK,
                                localisedStrings.internetConnectionMsg,
                                true,
                                Action0 { appCMSPresenter.navigateToDownloadPage(appPreference.getDownloadPageId()) },
                                null, null)
                    } else {
                        if (appCMSPresenter.isDownloadUnfinished() || appCMSPresenter.isDownloadOfflineInProgress()) {
                            appCMSPresenter.showEntitlementDialog(AppCMSPresenter.DialogType.LOGOUT_WITH_RUNNING_DOWNLOAD, null, null)
                        } else {
                            appCMSPresenter.showDialog(AppCMSPresenter.DialogType.SIGN_OUT, localisedStrings.logoutText,
                                    true,
                                    Action0 {
                                        appCMSPresenter.cancelInternalEvents()
                                        appCMSPresenter.logout()
                                    },
                                    null, appCMSPresenter.localizedLogoutText.toUpperCase(Locale.ROOT))
                        }
                    }
                }
            }

        }
    }

    override fun getItemCount(): Int {
        var totalItemCount = 0
        numPrimaryItems = 0
        numUserItems = 0
        numFooterItems = 0
        if (menuNavigation.getNavigationPrimary() != null) {
            for (i in menuNavigation.getNavigationPrimary().indices) {
                val navigationPrimary: NavigationPrimary = menuNavigation.getNavigationPrimary().get(i)
                if (navigationPrimary.accessLevels != null) {
                    if (!userSubscribed && !navigationPrimary.accessLevels.isSubscribed &&
                            (!userLoggedIn && navigationPrimary.accessLevels.isLoggedOut ||
                                    userLoggedIn && navigationPrimary.accessLevels.isLoggedIn)) {
                        totalItemCount++
                        numPrimaryItems++
                    } else if ((!userLoggedIn && navigationPrimary.accessLevels.isLoggedOut ||
                                    userLoggedIn && navigationPrimary.accessLevels.isLoggedIn) &&
                            navigationPrimary.accessLevels.isSubscribed) {
                        totalItemCount++
                        numPrimaryItems++
                    }
                }
            }
        }
        if (userLoggedIn && menuNavigation.getNavigationUser() != null) {
            for (i in menuNavigation.getNavigationUser().indices) {
                val navigationUser: NavigationUser = menuNavigation.getNavigationUser().get(i)
                if (navigationUser.accessLevels != null) {
                    if (!userLoggedIn && menuNavigation.getNavigationUser().get(i).accessLevels.isLoggedOut ||
                            userLoggedIn && menuNavigation.getNavigationUser().get(i).accessLevels.isLoggedIn) {
                        totalItemCount++
                        numUserItems++
                    }
                }
            }
        }
        if (menuNavigation.getNavigationFooter() != null) {
            for (i in menuNavigation.getNavigationFooter().indices) {
                val navigationFooter: NavigationFooter = menuNavigation.getNavigationFooter().get(i)
                if (navigationFooter.accessLevels != null) {
                    if (!userLoggedIn && menuNavigation.getNavigationFooter().get(i).accessLevels.isLoggedOut ||
                            userLoggedIn && menuNavigation.getNavigationFooter().get(i).accessLevels.isLoggedIn) {
                        totalItemCount++
                        numFooterItems++
                        listFooterFinal.add(navigationFooter)
                    }
                }
            }
        }
        if (userLoggedIn) {
            totalItemCount++
        }
        return totalItemCount
    }

    fun getItem(position: Int): Any? {
        return position
    }

    override fun getItemId(id: Int): Long {
        return id.toLong()
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    var listFooterFinal = mutableSetOf<NavigationFooter>()

    inner class ViewHolder(val binding: NavItemBinding) : RecyclerView.ViewHolder(binding.root)

    private fun openAncillaryPage(context: Context, navigationFooter: NavigationFooter) {
        if (appCMSPresenter.isFreshChatEnable && !TextUtils.isEmpty(appCMSPresenter.appCMSMain.customerService.freshChat?.appID)
                && !TextUtils.isEmpty(appCMSPresenter.appCMSMain.customerService.freshChat?.key)
                && (pageFunction.equals(context.getString(R.string.frequently_asked_questions), ignoreCase = true)
                        || pageFunction.toLowerCase(Locale.ROOT).contains(context.getString(R.string.faq).toLowerCase(Locale.ROOT))
                        || pageFunction.equals(context.getString(R.string.contact_us), ignoreCase = true)
                        || pageFunction.contains(context.getString(R.string.contact_us))
                        || pageFunction.contains(context.getString(R.string.contact_us_roku))
                        || pageFunction.contains(context.getString(R.string.contact_us))
                        || pageFunction.equals(context.getString(R.string.support), ignoreCase = true))) {
            val isFAQPage = pageFunction.equals(context.getString(R.string.frequently_asked_questions), ignoreCase = true) || pageFunction.toLowerCase(Locale.ROOT).contains(context.getString(R.string.faq).toLowerCase(Locale.ROOT))
            appCMSPresenter.launchFreshChat(appCMSPresenter.appCMSMain.customerService.freshChat?.appID,
                    appCMSPresenter.appCMSMain.customerService.freshChat?.key, isFAQPage)
        } else if (pageFunction.equals(context.getString(R.string.contact_us), ignoreCase = true)
                || pageFunction.contains(context.getString(R.string.contact_us))
                || pageFunction.contains(context.getString(R.string.contact_us_roku))
                || pageFunction.contains(context.getString(R.string.contact_us))
                || pageFunction.equals(context.getString(R.string.support), ignoreCase = true)) {
            if (navigationFooter.displayedPath != null) {
                var contactUsEmail: String? = if (navigationFooter.displayedPath.contains("mailto:")) navigationFooter.displayedPath.substring(7) else ""
                if ((contactUsEmail == null || TextUtils.isEmpty(contactUsEmail)) && appCMSPresenter.appCMSMain.customerService != null && appCMSPresenter.appCMSMain.customerService.email != null) contactUsEmail = appCMSPresenter.appCMSMain.customerService.email
                val intent = Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                        "mailto", contactUsEmail, null))
                appCMSPresenter.currentActivity.startActivity(Intent.createChooser(intent, navigationFooter.title))
            } else {
                appCMSPresenter.navigateToPage(navigationFooter.getPageId(),
                        navigationFooter.title,
                        navigationFooter.url,
                        false,
                        false,
                        false,
                        false,
                        false,
                        null)
            }
        } else if (navigationFooter.isOpenInChromeCustomTab && appCMSPresenter.appCMSMain != null && !TextUtils.isEmpty(appCMSPresenter.appCMSMain.domainName) && !TextUtils.isEmpty(navigationFooter.url)) {
            var url = navigationFooter.url
            if (!url.startsWith(context.getString(R.string.https_scheme))) {
                url = appCMSPresenter.appCMSMain.domainName + navigationFooter.url + "?app=true"
            }
            appCMSPresenter.openChromeTab(url)
        } else {
            appCMSPresenter.navigateToPage(navigationFooter.getPageId(),
                    navigationFooter.title,
                    navigationFooter.url,
                    false,
                    false,
                    false,
                    false,
                    false,
                    null)
        }
    }

    private fun decreaseSeparatorMargin(separatorView: View) {
        val params = separatorView.layoutParams as ConstraintLayout.LayoutParams
        val margin = params.topMargin
        if (margin > 35) {
            params.topMargin = margin - 20
            separatorView.layoutParams = params
        }
    }

    private fun setMenuImage(iconName: String?, imageView: AppCompatImageView, title: AppCompatTextView) {
        if (singleSeparatorAvailable) {
            title.setPadding(0, 25, 0, 0)
        }
        if (!singleMenuIconAvailable)
            return
        if (iconName != null && iconName.contains("https://")) {
            val imageUrl: String = mContext.getString(R.string.app_cms_image_with_resize_query,
                    iconName,
                    BaseView.dpToPx(R.dimen.app_cms_audio_item_padding, mContext),
                    BaseView.dpToPx(R.dimen.app_cms_audio_item_padding, mContext))
            try {
                Glide.with(mContext).load(imageUrl)
                        .into(imageView)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        } else {
            val resources: Resources = mContext.getResources()
            val drawableId: Int
            if (iconName != null) {
                drawableId = resources.getIdentifier(iconName.replace("-", "_"), "drawable", mContext.packageName)
                if (drawableId != 0) {
                    imageView.setImageDrawable(ContextCompat.getDrawable(mContext, drawableId))
                    imageView.setColorFilter(textColor, android.graphics.PorterDuff.Mode.SRC_IN);
                }
            }
        }
    }

    private fun setMenuSpace(iconName: String?, isSeperator: Boolean, tv: TextView) {
        /*if (iconName == null && !isSeperator) {
            tv.setPadding(0, 25, 0, 0)
        }*/
    }

}