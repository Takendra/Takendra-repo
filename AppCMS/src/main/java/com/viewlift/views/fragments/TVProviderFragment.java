package com.viewlift.views.fragments;


import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.viewlift.AppCMSApplication;
import com.viewlift.R;
import com.viewlift.models.data.appcms.ui.main.LocalisedStrings;
import com.viewlift.presenters.AppCMSPresenter;
import com.viewlift.views.adapters.TVProvidersAdapter;
import com.viewlift.views.binders.AppCMSBinder;
import com.viewlift.views.dialog.CustomShape;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnTextChanged;
import butterknife.Unbinder;

/**
 * A simple {@link Fragment} subclass.
 */
public class TVProviderFragment extends Fragment {
    @Inject
    AppCMSPresenter appCMSPresenter;
    @Inject
    LocalisedStrings localisedStrings;
    private AppCMSBinder appCMSBinder;
    @BindView(R.id.mainLayout)
    ConstraintLayout mainLayout;
    @BindView(R.id.chooseProvider)
    AppCompatTextView chooseProvider;
    @BindView(R.id.searchProvider)
    AppCompatEditText searchProvider;
    @BindView(R.id.providers)
    RecyclerView providers;
    Unbinder unbinder;
    TVProvidersAdapter tvProvidersAdapter;

    public TVProviderFragment() {
        // Required empty public constructor
    }

    public static TVProviderFragment newInstance(Context context, AppCMSBinder appCMSBinder) {
        TVProviderFragment fragment = new TVProviderFragment();
        Bundle args = new Bundle();
        args.putBinder(context.getString(R.string.fragment_page_bundle_key), appCMSBinder);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_tv_provider, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        unbinder = ButterKnife.bind(this, view);
        appCMSBinder = ((AppCMSBinder) getArguments().getBinder(getContext().getString(R.string.fragment_page_bundle_key)));
        ((AppCMSApplication) getActivity().getApplicationContext()).getAppCMSPresenterComponent().inject(this);
        if (appCMSPresenter.getmFireBaseAnalytics() != null)
            appCMSPresenter.getmFireBaseAnalytics().logEvent(getString(R.string.firebase_event_name_tve_provider_page), new Bundle());
        mainLayout.setBackgroundColor(appCMSPresenter.getGeneralBackgroundColor());
        chooseProvider.setTextColor(appCMSPresenter.getGeneralTextColor());
        providers.setLayoutManager(new GridLayoutManager(getActivity(), 2, GridLayoutManager.VERTICAL, false));
        tvProvidersAdapter = new TVProvidersAdapter(appCMSBinder.getLaunchData().getVerimatrixResponse().getTvProviders());
        providers.setAdapter(tvProvidersAdapter);
        searchProvider.setBackground(CustomShape.createRoundedRectangleDrawable(appCMSPresenter.getGeneralTextColor()));
    }

    @OnTextChanged(value = R.id.searchProvider,
            callback = OnTextChanged.Callback.TEXT_CHANGED)
    protected void afterEditTextChanged(Editable editable) {
        tvProvidersAdapter.getFilter().filter(editable.toString());
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

}
