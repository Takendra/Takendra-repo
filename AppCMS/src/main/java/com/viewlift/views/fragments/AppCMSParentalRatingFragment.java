package com.viewlift.views.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.viewlift.AppCMSApplication;
import com.viewlift.R;
import com.viewlift.db.AppPreference;
import com.viewlift.models.data.appcms.api.AppCMSParentalRating;
import com.viewlift.models.data.appcms.api.Module;
import com.viewlift.presenters.AppCMSPresenter;
import com.viewlift.views.adapters.AppCMSParentalRatingAdapter;
import com.viewlift.views.binders.AppCMSBinder;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class AppCMSParentalRatingFragment extends Fragment {

    @Inject
    AppCMSPresenter appCMSPresenter;
    @Inject
    AppPreference appPreference;

    private Module module;
    private AppCMSParentalRatingAdapter adapter;

    @BindView(R.id.containerView)
    ConstraintLayout containerView;
    @BindView(R.id.pageTitle)
    TextView pageTitle;
    @BindView(R.id.ratingLimitMsg)
    TextView ratingLimitMsg;
    @BindView(R.id.tapAgeMessage)
    TextView tapAgeMessage;
    @BindView(R.id.parentalRatingListView)
    RecyclerView parentalRatingListView;
    @BindView(R.id.saveBtn)
    Button saveBtn;

    public static AppCMSParentalRatingFragment newInstance(Context context, AppCMSBinder appCMSBinder) {
        AppCMSParentalRatingFragment fragment = new AppCMSParentalRatingFragment();
        Bundle args = new Bundle();
        args.putBinder(context.getString(R.string.fragment_page_bundle_key), appCMSBinder);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_app_parental_rating, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        ((AppCMSApplication) getActivity().getApplicationContext()).getAppCMSPresenterComponent().inject(this);
        if (appCMSPresenter != null)
            appCMSPresenter.showLoadingDialog(false);
        int bgColor         = appCMSPresenter.getGeneralBackgroundColor();
        int buttonColor     = appCMSPresenter.getBrandPrimaryCtaColor();
        int buttonTextColor = appCMSPresenter.getBrandPrimaryCtaTextColor();
        int textColor       = appCMSPresenter.getGeneralTextColor();

        containerView.setBackgroundColor(bgColor);
        saveBtn.setBackgroundColor(buttonColor);
        saveBtn.setTextColor(buttonTextColor);
        pageTitle.setTextColor(textColor);

        try {
            if (getArguments() != null) {
                AppCMSBinder appCMSBinder = ((AppCMSBinder) getArguments().getBinder(getString(R.string.fragment_page_bundle_key)));
                if (appCMSPresenter != null && appCMSBinder != null && appCMSBinder.getAppCMSPageUI() != null) {
                    module = appCMSPresenter.matchModuleAPIToModuleUI(appCMSPresenter.getRelatedModuleForBlock(appCMSBinder.getAppCMSPageUI().getModuleList(), getString(R.string.ui_block_user_management_01)), appCMSBinder.getAppCMSPageAPI());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        setLocalisedStrings();
        setRestrictionsListView();
    }

    private void setLocalisedStrings() {
        if (module != null && module.getMetadataMap() != null && module.getMetadataMap().getViewingRestrictionsCTA() != null)
            pageTitle.setText(module.getMetadataMap().getViewingRestrictionsCTA());
        else
            pageTitle.setText(getString(R.string.viewing_restrictions_cta));

        if (module != null && module.getMetadataMap() != null && module.getMetadataMap().getTapAgeMessage() != null)
            tapAgeMessage.setText(module.getMetadataMap().getTapAgeMessage());
        else
            tapAgeMessage.setText(getString(R.string.tap_age_message));

        if (module != null && module.getMetadataMap() != null && module.getMetadataMap().getSaveRatingCTA() != null)
            saveBtn.setText(module.getMetadataMap().getSaveRatingCTA());
        else
            saveBtn.setText(getString(R.string.save_rating_cta));
    }

    private void setRestrictionsListView() {
        adapter = new AppCMSParentalRatingAdapter(appCMSPresenter, getDataList(), ratingLimitMsg::setText);
        parentalRatingListView.setLayoutManager(new LinearLayoutManager(getContext()));
        parentalRatingListView.setAdapter(adapter);
    }

    @OnClick(R.id.saveBtn)
    void onClick() {
        if (appCMSPresenter != null && adapter != null && adapter.getSelectedItem() != null) {
            appCMSPresenter.launchButtonSelectedAction(null, getString(R.string.app_cms_action_setParentalRating), null,
                    new String[]{adapter.getSelectedItem().getName()}, null, false, -1, null);
        }
    }

    private List<AppCMSParentalRating> getDataList() {
        List<AppCMSParentalRating> dataList = new ArrayList<>();
        AppCMSParentalRating allRestrictions = new AppCMSParentalRating();
        allRestrictions.setName("All");
        if (module != null && module.getMetadataMap() != null && module.getMetadataMap().getAllRatingCategory() != null)
            allRestrictions.setCategory(module.getMetadataMap().getAllRatingCategory());
        else
            allRestrictions.setCategory(getString(R.string.all_rating_category));
        if (module != null && module.getMetadataMap() != null && module.getMetadataMap().getAllRatingMessage() != null)
            allRestrictions.setMessage(module.getMetadataMap().getAllRatingMessage());
        else
            allRestrictions.setMessage(getString(R.string.all_rating_message));
        dataList.add(allRestrictions);

        AppCMSParentalRating _7Restrictions = new AppCMSParentalRating();
        _7Restrictions.setName("7+");
        if (module != null && module.getMetadataMap() != null && module.getMetadataMap().getSevenRatingCategory() != null)
            _7Restrictions.setCategory(module.getMetadataMap().getSevenRatingCategory());
        else
            _7Restrictions.setCategory(getString(R.string.seven_rating_category));
        if (module != null && module.getMetadataMap() != null && module.getMetadataMap().getSevenRatingMessage() != null)
            _7Restrictions.setMessage(module.getMetadataMap().getSevenRatingMessage());
        else
            _7Restrictions.setMessage(getString(R.string.seven_rating_message));
        dataList.add(_7Restrictions);

        AppCMSParentalRating _13Restrictions = new AppCMSParentalRating();
        _13Restrictions.setName("13+");
        if (module != null && module.getMetadataMap() != null && module.getMetadataMap().getThirteenRatingCategory() != null)
            _13Restrictions.setCategory(module.getMetadataMap().getThirteenRatingCategory());
        else
            _13Restrictions.setCategory(getString(R.string.thirteen_rating_category));
        if (module != null && module.getMetadataMap() != null && module.getMetadataMap().getThirteenRatingMessage() != null)
            _13Restrictions.setMessage(module.getMetadataMap().getThirteenRatingMessage());
        else
            _13Restrictions.setMessage(getString(R.string.thirteen_rating_message));
        dataList.add(_13Restrictions);

        AppCMSParentalRating _16Restrictions = new AppCMSParentalRating();
        _16Restrictions.setName("16+");
        if (module != null && module.getMetadataMap() != null && module.getMetadataMap().getSixteenRatingCategory() != null)
            _16Restrictions.setCategory(module.getMetadataMap().getSixteenRatingCategory());
        else
            _16Restrictions.setCategory(getString(R.string.sixteen_rating_category));
        if (module != null && module.getMetadataMap() != null && module.getMetadataMap().getSixteenRatingMessage() != null)
            _16Restrictions.setMessage(module.getMetadataMap().getSixteenRatingMessage());
        else
            _16Restrictions.setMessage(getString(R.string.sixteen_rating_message));
        dataList.add(_16Restrictions);

        AppCMSParentalRating _18Restrictions = new AppCMSParentalRating();
        _18Restrictions.setName("18+");
        if (module != null && module.getMetadataMap() != null && module.getMetadataMap().getEighteenRatingCategory() != null)
            _18Restrictions.setCategory(module.getMetadataMap().getEighteenRatingCategory());
        else
            _18Restrictions.setCategory(getString(R.string.eighteen_rating_category));
        if (module != null && module.getMetadataMap() != null && module.getMetadataMap().getEighteenRatingMessage() != null)
            _18Restrictions.setMessage(module.getMetadataMap().getEighteenRatingMessage());
        else
            _18Restrictions.setMessage(getString(R.string.eighteen_rating_message));
        dataList.add(_18Restrictions);
        return dataList;
    }
}
