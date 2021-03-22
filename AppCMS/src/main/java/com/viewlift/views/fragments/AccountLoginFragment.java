package com.viewlift.views.fragments;


import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatSpinner;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
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
import com.viewlift.views.adapters.plans.PlansSpinnerAdapter;
import com.viewlift.views.binders.AppCMSBinder;
import com.viewlift.views.customviews.ViewCreator;
import com.viewlift.views.dialog.CustomShape;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnItemSelected;
import butterknife.Unbinder;

import static com.viewlift.presenters.AppCMSPresenter.LaunchType.TVOD_PURCHASE;


/**
 * A simple {@link Fragment} subclass.
 */
public class AccountLoginFragment extends Fragment {

    @BindView(R.id.signupHeader)
    AppCompatTextView signupHeader;
    @BindView(R.id.loginHeader)
    AppCompatTextView loginHeader;
    @BindView(R.id.plan)
    AppCompatSpinner plan;
    @BindView(R.id.planContainer)
    ConstraintLayout planContainer;
    private AppCMSBinder appCMSBinder;
    @Inject
    AppCMSPresenter appCMSPresenter;
    Unbinder unbinder;
    @BindView(R.id.parentLayout)
    LinearLayoutCompat parentLayout;

    public AccountLoginFragment() {
        // Required empty public constructor
    }

    public static AccountLoginFragment newInstance(Context context, AppCMSBinder appCMSBinder) {
        AccountLoginFragment fragment = new AccountLoginFragment();
        Bundle args = new Bundle();
        args.putBinder(context.getString(R.string.fragment_page_bundle_key), appCMSBinder);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_account_login, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        unbinder = ButterKnife.bind(this, view);
        ((AppCMSApplication) getActivity().getApplicationContext()).getAppCMSPresenterComponent().inject(this);
        setViewStyles();
        appCMSBinder = ((AppCMSBinder) getArguments().getBinder(getContext().getString(R.string.fragment_page_bundle_key)));
        setPlanDetails();
        handleModuleSettings();
        showFragment(false);
        if (appCMSPresenter.getLaunchType() == AppCMSPresenter.LaunchType.SUBSCRIBE || appCMSPresenter.getLaunchType() == AppCMSPresenter.LaunchType.SIGNUP) {
            setSelection(false);
            showFragment(false);
        } else if (appCMSPresenter.getLaunchType() == AppCMSPresenter.LaunchType.LOGIN_AND_SIGNUP) {
            setSelection(true);
            showFragment(true);
        }
    }


    private void setViewStyles() {
        int appTextColor = Color.parseColor(appCMSPresenter.getAppTextColor());
        int appBackgroundColor = Color.parseColor(appCMSPresenter.getAppBackgroundColor());
        int transparentColor = ContextCompat.getColor(getContext(), android.R.color.transparent);
        int cornerRadius = 10;
        int rectangleStroke = 2;
        parentLayout.setBackgroundColor(appBackgroundColor);
        signupHeader.setTextColor(appTextColor);
        CustomShape.makeRoundCorner(transparentColor, cornerRadius, planContainer, rectangleStroke, appTextColor);
    }

    private void setSelection(boolean login) {
        Component component = new Component();
        signupHeader.setTextColor(Color.parseColor(appCMSPresenter.getAppTextColor()));
        loginHeader.setTextColor(Color.parseColor(appCMSPresenter.getAppTextColor()));
        if (login) {
            ViewCreator.setTypeFace(getActivity(), appCMSPresenter, appCMSPresenter.getJsonValueKeyMap(), component, signupHeader);
            component.setFontWeight(getString(R.string.app_cms_page_font_bold_key));
            ViewCreator.setTypeFace(getActivity(), appCMSPresenter, appCMSPresenter.getJsonValueKeyMap(), component, loginHeader);
        } else {
            ViewCreator.setTypeFace(getActivity(), appCMSPresenter, appCMSPresenter.getJsonValueKeyMap(), component, loginHeader);
            component.setFontWeight(getString(R.string.app_cms_page_font_bold_key));
            ViewCreator.setTypeFace(getActivity(), appCMSPresenter, appCMSPresenter.getJsonValueKeyMap(), component, signupHeader);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    private void setPlanDetails() {
        if (appCMSPresenter.getSelectedPlanId() != null && appCMSPresenter.getLaunchType() != TVOD_PURCHASE) {
            PlansSpinnerAdapter adapter = new PlansSpinnerAdapter(getContext(), appCMSPresenter.getPlans());
            plan.setAdapter(adapter);
            for (int i = 0; i < appCMSPresenter.getPlans().size(); i++) {
                if (appCMSPresenter.getPlans().get(i).getId().equalsIgnoreCase(appCMSPresenter.getSelectedPlanId())) {
                    plan.setSelection(i);
                    break;
                }
            }
            planContainer.setVisibility(View.VISIBLE);
        } else {
            planContainer.setVisibility(View.GONE);
        }
    }


    @OnClick(R.id.signupHeader)
    void signupHeaderClick() {
        setPlanDetails();
        setSelection(false);
        showFragment(false);
    }

    @OnItemSelected(R.id.plan)
    void planSelected(Spinner spinner, int position) {
        appCMSPresenter.setSelectedPlan(appCMSPresenter.getPlans().get(position).getId(), appCMSPresenter.getPlans());
    }

    @OnClick(R.id.loginHeader)
    void loginHeaderClick() {
        planContainer.setVisibility(View.GONE);
        setSelection(true);
        showFragment(true);
    }

    private void handleModuleSettings() {
        for (int i = 0; i < appCMSBinder.getAppCMSPageUI().getModuleList().size(); i++) {
            ModuleList moduleList = appCMSBinder.getAppCMSPageUI().getModuleList().get(i);
            if (moduleList.getBlockName().equalsIgnoreCase(getString(R.string.ui_block_authentication_17))) {
                if (appCMSBinder.getAppCMSPageAPI() != null) {
                    Module moduleApi = appCMSPresenter.matchModuleAPIToModuleUI(moduleList, appCMSBinder.getAppCMSPageAPI());
                    if (moduleApi != null) {
                        MetadataMap metadataMap = moduleApi.getMetadataMap();
                        if (metadataMap.getSignUpTab() != null)
                            signupHeader.setText(metadataMap.getSignUpTab());
                        if (metadataMap.getLoginTab() != null)
                            loginHeader.setText(metadataMap.getLoginTab());
                    }
                }
                break;
            }
        }
    }

    private void showFragment(boolean login) {
        FragmentManager fragmentManager = getChildFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        Fragment fragment = null;
        if (login)
            fragment = LoginFragment.newInstance(getActivity(), appCMSBinder);
        else
            fragment = SignUpFragment.newInstance(getActivity(), appCMSBinder);
        if (fragment != null) {
            fragmentTransaction.replace(R.id.authenticationOptionsContainer, fragment,
                    "loginsignup");
            fragmentTransaction.commit();
            getChildFragmentManager().executePendingTransactions();
        }
    }
}
