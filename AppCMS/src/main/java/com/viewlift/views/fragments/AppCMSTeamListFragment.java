package com.viewlift.views.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import androidx.annotation.Nullable;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.viewlift.AppCMSApplication;
import com.viewlift.R;
import com.viewlift.presenters.AppCMSPresenter;
import com.viewlift.views.adapters.AppCMSTeamItemAdapter;
import com.viewlift.views.binders.AppCMSBinder;
import com.viewlift.views.customviews.BaseView;

/**
 * Created by sandeep.singh on 11/7/2017.
 */

public class AppCMSTeamListFragment extends Fragment {
    private static final String TAG = "AppCMSTeamListFragment";
    private final String FIREBASE_SCREEN_VIEW_EVENT = "screen_view";
    private final String FIREBASE_LOGIN_SCREEN_VALUE = "Login Screen";
    private final String LOGIN_STATUS_KEY = "logged_in_status";
    private final String LOGIN_STATUS_LOGGED_IN = "logged_in";
    private final String LOGIN_STATUS_LOGGED_OUT = "not_logged_in";
    private AppCMSPresenter appCMSPresenter;
    private AppCMSBinder appCMSBinder;
    private AppCMSTeamItemAdapter appCMSTeamItemAdapter;

    public static AppCMSTeamListFragment newInstance(Context context,
                                                     AppCMSBinder appCMSBinder,
                                                     int textColor,
                                                     int bgColor,
                                                     int borderColor,
                                                     int buttonColor) {
        AppCMSTeamListFragment fragment = new AppCMSTeamListFragment();
        Bundle args = new Bundle();
        args.putBinder(context.getString(R.string.fragment_page_bundle_key), appCMSBinder);
        args.putInt(context.getString(R.string.app_cms_text_color_key), textColor);
        args.putInt(context.getString(R.string.app_cms_bg_color_key), bgColor);
        args.putInt(context.getString(R.string.app_cms_border_color_key), borderColor);
        args.putInt(context.getString(R.string.app_cms_button_color_key), buttonColor);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        Bundle args = getArguments();

        int textColor = args.getInt(getContext().getString(R.string.app_cms_text_color_key));
        int bgColor = args.getInt(getContext().getString(R.string.app_cms_bg_color_key));
        int borderColor = args.getInt(getContext().getString(R.string.app_cms_border_color_key));
        int buttonColor = args.getInt(getContext().getString(R.string.app_cms_button_color_key));

        try {
            appCMSBinder =
                    ((AppCMSBinder) args.getBinder(getContext().getString(R.string.fragment_page_bundle_key)));
        } catch (Exception e) {
            //Log.e(TAG, "Failed to extract appCMSBinder from args");
        }
        View view = inflater.inflate(R.layout.fragment_menu_nav, container, false);
        RecyclerView navItemsList = view.findViewById(R.id.nav_items_list);
        appCMSPresenter = ((AppCMSApplication) getActivity().getApplication())
                .getAppCMSPresenterComponent()
                .appCMSPresenter();

        if (appCMSBinder != null && appCMSBinder.getNavigation() != null) {
            appCMSTeamItemAdapter = new AppCMSTeamItemAdapter(appCMSBinder.getNavigation().getTabBar(),
                    appCMSPresenter,
                    appCMSBinder.getJsonValueKeyMap(),
                    appCMSBinder.isUserLoggedIn(),
                    appCMSBinder.isUserSubscribed(),
                    textColor);

            navItemsList.setAdapter(appCMSTeamItemAdapter);
            appCMSPresenter.setAppOrientation();

//            NestedScrollView nestedScrollView = view.findViewById(R.id.app_cms_nav_items_main_view);

            LinearLayout appCMSNavLoginContainer = view.findViewById(R.id.app_cms_nav_login_container);

//            if (!BaseView.isTablet(getContext())) {
//                ((RelativeLayout.LayoutParams) nestedScrollView.getLayoutParams()).addRule(RelativeLayout.ALIGN_PARENT_TOP | RelativeLayout.CENTER_HORIZONTAL);
//                ((RelativeLayout.LayoutParams) nestedScrollView.getLayoutParams()).setMargins(70, 70, 0, 0);
//            } else {
//                ((RelativeLayout.LayoutParams) nestedScrollView.getLayoutParams()).addRule(RelativeLayout.CENTER_IN_PARENT);
//            }
            appCMSNavLoginContainer.setVisibility(View.GONE);

        }

        setBgColor(bgColor, view);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        appCMSTeamItemAdapter.setUserLoggedIn(appCMSPresenter.isUserLoggedIn());
        appCMSTeamItemAdapter.setUserSubscribed(appCMSPresenter.isUserSubscribed());
        appCMSTeamItemAdapter.notifyDataSetChanged();
        appCMSPresenter.setIsTeamPageVisible(true);
        appCMSPresenter.dismissPopupWindowPlayer(true);
    }

    @Override
    public void onPause() {
        super.onPause();
        appCMSPresenter.setIsTeamPageVisible(false);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        appCMSPresenter.setAppOrientation();
        appCMSPresenter.setIsTeamPageVisible(false);
    }

    private void setBgColor(int bgColor, View view) {
//        RelativeLayout appCMSNavigationMenuMainLayout =
//                view.findViewById(R.id.app_cms_navigation_menu_main_layout);
//        appCMSNavigationMenuMainLayout.setBackgroundColor(bgColor);
    }
}
