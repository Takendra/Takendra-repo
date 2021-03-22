package com.viewlift.views.customviews;

import android.content.Context;
import android.graphics.Color;

import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.core.content.ContextCompat;

import com.viewlift.R;
import com.viewlift.models.data.appcms.ui.android.NavigationPrimary;
import com.viewlift.presenters.AppCMSPresenter;

/**
 * Created by viewlift on 12/1/17.
 */

public class TabCreator {
    public interface OnClickHandler {
        NavBarItemView getSelectedNavItem();
        void setSelectedMenuTabIndex(int selectedMenuTabIndex);
        void setSelectedSearchTabIndex(int selectedSearchTabIndex);
        void selectNavItemAndLaunchPage(NavBarItemView navBarItemView,
                                        String pageId,
                                        String pageTitle);
        void closeMenuPageIfHighlighted(NavBarItemView menuNavBarItemView);
    }

    private LinearLayoutCompat appCMSTabNavContainer;
    private AppCMSPresenter appCMSPresenter;
    private OnClickHandler onClickHandler;

    public void create(Context context, int currentIndex, NavigationPrimary tabItem) {
        if (appCMSTabNavContainer.getChildAt(currentIndex) instanceof
                NavBarItemView) {
            final NavBarItemView navBarItemView =
                    (NavBarItemView) appCMSTabNavContainer.getChildAt(currentIndex);
            int highlightColor;
            try {
                highlightColor = Color.parseColor(appCMSPresenter.getAppCMSMain().getBrand()
                        .getGeneral().getBlockTitleColor());
            } catch (Exception e) {
                //Log.w(TAG, "Failed to set AppCMS branding color for navigation item: " +
//                            e.getMessage());
                highlightColor = ContextCompat.getColor(context, R.color.colorAccent);
            }

            String tabIcon = tabItem.getIcon();
            if (tabIcon != null) {
                tabIcon = tabIcon.replace("-", "_");
            }
            navBarItemView.setImage(tabIcon);
            navBarItemView.setHighlightColor(highlightColor);

            String navigationTitle = appCMSPresenter.getNavigationTitle(tabItem.getLocalizationMap());
            navBarItemView.setLabel(/*tabItem.getTitle()*/navigationTitle != null ? navigationTitle : tabItem.getTitle());

            if (appCMSPresenter.getPageFunctionValue(tabItem.getPageId(),tabItem.getTitle()).equalsIgnoreCase("Search")) {
                navBarItemView.setOnClickListener(v -> {
                    if (onClickHandler.getSelectedNavItem() == navBarItemView) {
                        return;
                    }

                    if (!appCMSPresenter.isNetworkConnected()) {
                        if (!appCMSPresenter.isUserLoggedIn()) {
                            appCMSPresenter.showDialog(AppCMSPresenter.DialogType.NETWORK, null, false,
                                    () -> appCMSPresenter.launchBlankPage(),
                                    null,null);
                            return;
                        }

                        appCMSPresenter.showDialog(AppCMSPresenter.DialogType.NETWORK,
                                appCMSPresenter.getLocalisedStrings().getInternetConnectionMsg(),
                                true,
                                () -> appCMSPresenter.navigateToDownloadPage(appCMSPresenter.getDownloadPageId()),
                                null,null);
                        return;
                    }

                    appCMSPresenter.launchSearchPage();
                });
                onClickHandler.setSelectedSearchTabIndex(currentIndex);
            } else if (appCMSPresenter.getPageFunctionValue(tabItem.getPageId(),tabItem.getTitle()).equalsIgnoreCase("Menu")) {
                navBarItemView.setOnClickListener(v -> {
                    if (!appCMSPresenter.launchNavigationPage()) {
                        //Log.e(TAG, "Could not launch navigation page!");
                    } else {
                        if (navBarItemView.getResources().getBoolean(R.bool.menu_icon_dismisses_menu_page)) {
                            onClickHandler.closeMenuPageIfHighlighted(navBarItemView);
                        }
                    }
                });
                onClickHandler.setSelectedMenuTabIndex(currentIndex);
            } else {
                navBarItemView.setOnClickListener(v -> {
                    if (onClickHandler.getSelectedNavItem() == navBarItemView) {
                        return;
                    }

                    appCMSPresenter.showMainFragmentView(true);
                    onClickHandler.selectNavItemAndLaunchPage(navBarItemView,
                            tabItem.getPageId(),
                            tabItem.getTitle());
                });

                navBarItemView.setTag(tabItem.getPageId());
                if (navBarItemView.getParent() == null) {
                    appCMSTabNavContainer.addView(navBarItemView);
                }
            }
        }
    }

    public LinearLayoutCompat getAppCMSTabNavContainer() {
        return appCMSTabNavContainer;
    }

    public void setAppCMSTabNavContainer(LinearLayoutCompat appCMSTabNavContainer) {
        this.appCMSTabNavContainer = appCMSTabNavContainer;
    }

    public AppCMSPresenter getAppCMSPresenter() {
        return appCMSPresenter;
    }

    public void setAppCMSPresenter(AppCMSPresenter appCMSPresenter) {
        this.appCMSPresenter = appCMSPresenter;
    }

    public OnClickHandler getOnClickHandler() {
        return onClickHandler;
    }

    public void setOnClickHandler(OnClickHandler onClickHandler) {
        this.onClickHandler = onClickHandler;
    }
}
