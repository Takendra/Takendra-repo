package com.viewlift.views.fragments;


import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.core.content.ContextCompat;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.viewlift.AppCMSApplication;
import com.viewlift.R;
import com.viewlift.models.data.appcms.api.MetadataMap;
import com.viewlift.models.data.appcms.api.Module;
import com.viewlift.models.data.appcms.ui.page.Component;
import com.viewlift.models.data.appcms.ui.page.ModuleList;
import com.viewlift.presenters.AppCMSPresenter;
import com.viewlift.views.binders.AppCMSBinder;
import com.viewlift.views.customviews.ViewCreator;
import com.viewlift.views.rxbus.AppBus;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * A simple {@link Fragment} subclass.
 */
public class GenericAuthenticationFragment extends Fragment {

    private AppCMSBinder appCMSBinder;
    @BindView(R.id.header)
    LinearLayoutCompat header;
    @BindView(R.id.pageTitle)
    AppCompatTextView pageTitle;
    @BindView(R.id.account)
    AppCompatTextView account;
    @BindView(R.id.billing)
    AppCompatTextView billing;
    @BindView(R.id.confirmation)
    AppCompatTextView confirmation;
    @BindView(R.id.parentLayout)
    NestedScrollView parentLayout;
    private int selectedColor, unselectedColor;
    private FragmentManager fragmentManager;
    @Inject
    AppCMSPresenter appCMSPresenter;

    public GenericAuthenticationFragment() {
        // Required empty public constructor
    }

    public static GenericAuthenticationFragment newInstance(Context context, AppCMSBinder appCMSBinder) {
        GenericAuthenticationFragment fragment = new GenericAuthenticationFragment();
        Bundle args = new Bundle();
        args.putBinder(context.getString(R.string.fragment_page_bundle_key), appCMSBinder);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_generic_authentication, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        ((AppCMSApplication) getActivity().getApplicationContext()).getAppCMSPresenterComponent().inject(this);
        setViewStyles();
        appCMSBinder = ((AppCMSBinder) getArguments().getBinder(getContext().getString(R.string.fragment_page_bundle_key)));
        setTextToViews();
        selectedColor = Color.parseColor(appCMSPresenter.getAppTextColor());
        unselectedColor = appCMSPresenter.getBrandPrimaryCtaColor() + 0x92000000;
        fragmentManager = getChildFragmentManager();
        if (appCMSPresenter.isUserLoggedIn())
            showFragment(true);
        else
            showFragment(false);
        parentLayout.setBackgroundColor(Color.parseColor(appCMSPresenter.getAppBackgroundColor()));
        if (appCMSPresenter.getLaunchType() == AppCMSPresenter.LaunchType.SUBSCRIBE || appCMSPresenter.getLaunchType() == AppCMSPresenter.LaunchType.V2_SUBSCRIPTION_FLOW) {
            header.setVisibility(View.VISIBLE);
            pageTitle.setVisibility(View.VISIBLE);
        } else {
            header.setVisibility(View.GONE);
            pageTitle.setVisibility(View.GONE);
        }
        handleFragmentSelection();
    }

    private void setTextToViews() {
        MetadataMap metadataMap = null;
        for (int i = 0; i < appCMSBinder.getAppCMSPageUI().getModuleList().size(); i++) {
            ModuleList moduleList = appCMSBinder.getAppCMSPageUI().getModuleList().get(i);
            if (moduleList.getBlockName().equalsIgnoreCase(getString(R.string.ui_block_authentication_17)) && moduleList.getSettings() != null && moduleList.getSettings().getOptions() != null) {
                if (appCMSBinder.getAppCMSPageAPI() != null) {
                    Module moduleApi = appCMSPresenter.matchModuleAPIToModuleUI(moduleList, appCMSBinder.getAppCMSPageAPI());
                    if (moduleApi != null) {
                        metadataMap = moduleApi.getMetadataMap();
                        break;
                    }
                }
            }
        }
        if (metadataMap != null) {
            if (!TextUtils.isEmpty(metadataMap.getAccountLabel()))
                account.setText(metadataMap.getAccountLabel());
            if (!TextUtils.isEmpty(metadataMap.getBillingLabel()))
                billing.setText(metadataMap.getBillingLabel());
            if (!TextUtils.isEmpty(metadataMap.getConfirmationLabel()))
                confirmation.setText(metadataMap.getConfirmationLabel());
            if (!TextUtils.isEmpty(metadataMap.getSubscribeLabel()))
                pageTitle.setText(metadataMap.getSubscribeLabel());
        }
    }

    private void setViewStyles() {
        parentLayout.setBackgroundColor(Color.parseColor(appCMSPresenter.getAppBackgroundColor()));
        pageTitle.setTextColor(Color.parseColor(appCMSPresenter.getAppTextColor()));
        Component component = new Component();
        ViewCreator.setTypeFace(getActivity(), appCMSPresenter, appCMSPresenter.getJsonValueKeyMap(), component, account);
        ViewCreator.setTypeFace(getActivity(), appCMSPresenter, appCMSPresenter.getJsonValueKeyMap(), component, billing);
        ViewCreator.setTypeFace(getActivity(), appCMSPresenter, appCMSPresenter.getJsonValueKeyMap(), component, confirmation);
        component.setFontWeight(getString(R.string.app_cms_page_font_bold_key));
        ViewCreator.setTypeFace(getActivity(), appCMSPresenter, appCMSPresenter.getJsonValueKeyMap(), component, pageTitle);
    }

    private void setBillingSelected() {
        account.setTextColor(unselectedColor);
        billing.setTextColor(selectedColor);
        confirmation.setTextColor(unselectedColor);
    }

    private void setAccountSelected() {
        account.setTextColor(selectedColor);
        billing.setTextColor(unselectedColor);
        confirmation.setTextColor(unselectedColor);
    }

    private void handleFragmentSelection() {
        AppBus.instanceOf().getShowPaymentOption().subscribe(new io.reactivex.rxjava3.core.Observer<Boolean>() {
            @Override
            public void onSubscribe(io.reactivex.rxjava3.disposables.Disposable d) {

            }

            @Override
            public void onNext(Boolean showPlayer) {
                showFragment(true);
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        });
    }


    private void showFragment(Boolean billing) {
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        Fragment fragment = null;
        if (billing) {
            setBillingSelected();
            fragment = BillingFragment.newInstance(appCMSPresenter.getCurrentContext(), appCMSBinder);
        } else {
            setAccountSelected();
            fragment = AccountLoginFragment.newInstance(appCMSPresenter.getCurrentContext(), appCMSBinder);
        }
        fragmentTransaction.replace(R.id.authenticationContainer, fragment,
                "authentication");
        fragmentTransaction.commitAllowingStateLoss();
            /*fragmentTransaction.commitAllowingStateLoss();
            getChildFragmentManager().executePendingTransactions();*/
    }


}
