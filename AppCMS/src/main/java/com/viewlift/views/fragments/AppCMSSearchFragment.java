package com.viewlift.views.fragments;

import android.app.SearchManager;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.DialogFragment;

import com.viewlift.AppCMSApplication;
import com.viewlift.R;
import com.viewlift.models.data.appcms.ui.main.LocalisedStrings;
import com.viewlift.presenters.AppCMSPresenter;
import com.viewlift.views.adapters.SearchSuggestionsAdapter;
import com.viewlift.views.customviews.BaseView;
import com.viewlift.views.customviews.ViewCreator;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

/*
 * Created by viewlift on 6/20/17.
 */

public class AppCMSSearchFragment extends DialogFragment {
//    private static final String TAG = "SearchFragment";

    @BindView(R.id.app_cms_search_fragment)
    RelativeLayout appCMSNavigationMenuMainLayout;

    @BindView(R.id.app_cms_search_fragment_view)
    SearchView appCMSSearchView;

    @BindView(R.id.app_cms_search_button)
    Button appCMSGoButton;

    @Inject
    AppCMSPresenter appCMSPresenter;
    @Inject
    LocalisedStrings localisedStrings;

    private String searchQuery;
    private OnSaveSearchQuery onSaveSearchQuery;

    public static AppCMSSearchFragment newInstance(Context context,
                                                   long bgColor,
                                                   long buttonColor,
                                                   long textColor) {
        Bundle args = new Bundle();
        args.putLong(context.getString(R.string.bg_color_key), bgColor);
        args.putLong(context.getString(R.string.button_color_key), buttonColor);
        args.putLong(context.getString(R.string.text_color_key), textColor);
        AppCMSSearchFragment appCMSSearchFragment = new AppCMSSearchFragment();
        appCMSSearchFragment.setArguments(args);
        return appCMSSearchFragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnSaveSearchQuery) {
            onSaveSearchQuery = (OnSaveSearchQuery) context;
        }
    }

    View view = null;

    @Nullable
    @Override
    @SuppressWarnings("ConstantConditions")
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search, container, false);

        ButterKnife.bind(this, view);
        ((AppCMSApplication) getActivity().getApplicationContext()).getAppCMSPresenterComponent().inject(this);
        Bundle args = getArguments();
        long bgColor = 0xff000000 + args.getLong(getContext().getString(R.string.bg_color_key));
        long buttonColor = args.getLong(getString(R.string.button_color_key));
        long textColor = args.getLong(getString(R.string.text_color_key));
        SearchManager searchManager = (SearchManager) getActivity().getSystemService(Context.SEARCH_SERVICE);
        SearchSuggestionsAdapter searchSuggestionsAdapter = new SearchSuggestionsAdapter(appCMSPresenter, getActivity(),
                null,
                searchManager.getSearchableInfo(getActivity().getComponentName()),
                true);

        appCMSSearchView.setQueryHint(localisedStrings.getSearchLabelText().toUpperCase());
        appCMSSearchView.setSuggestionsAdapter(searchSuggestionsAdapter);
        appCMSSearchView.setIconifiedByDefault(false);
        appCMSSearchView.requestFocus();
        TextView searchText = appCMSSearchView.findViewById(androidx.appcompat.R.id.search_src_text);
        appCMSPresenter.setCursorDrawableColor((EditText) searchText, 0);

        appCMSPresenter.showSoftKeyboard(appCMSSearchView);

        if (searchQuery != null) {
            appCMSSearchView.setQuery(searchQuery, false);
        } else if (onSaveSearchQuery != null) {
            appCMSSearchView.setQuery(onSaveSearchQuery.restoreQuery(), false);
        }

        appCMSSearchView.setOnSuggestionListener(new SearchView.OnSuggestionListener() {

            @Override
            public boolean onSuggestionSelect(int position) {
                return true;
            }

            @Override
            public boolean onSuggestionClick(int position) {
                Cursor cursor = (Cursor) appCMSSearchView.getSuggestionsAdapter().getItem(position);
                String[] searchHintResult = cursor.getString(cursor.getColumnIndex("suggest_intent_data")).split(",");
                appCMSPresenter.searchSuggestionClick(searchHintResult);
                return true;
            }
        });

        buttonColor = appCMSPresenter.getBrandPrimaryCtaColor();
        textColor = appCMSPresenter.getBrandPrimaryCtaTextColor();
        appCMSGoButton.setBackgroundColor(0xff000000 + (int) buttonColor);
        appCMSGoButton.setTextColor(0xff000000 + (int) ViewCreator.adjustColor1(textColor, buttonColor));
        appCMSGoButton.setText(localisedStrings.getGoText());

        appCMSGoButton.setOnClickListener(v -> {
            if (appCMSSearchView.getQuery().toString().trim().length() == 0) {
                appCMSPresenter.showEmptySearchToast();
                return;
            }
            appCMSPresenter.sendSearchEvent(appCMSSearchView.getQuery().toString());
            appCMSPresenter.launchSearchResultsPage(appCMSSearchView.getQuery().toString(),
                    null,
                    false);
        });

        setBgColor((int) bgColor);

        appCMSPresenter.setAppOrientation();

        return view;
    }

    @Override
    public void onPause() {
        super.onPause();
        searchQuery = appCMSSearchView.getQuery().toString();
    }

    @Override
    public void onResume() {
        super.onResume();

        if (searchQuery != null) {
            appCMSSearchView.setQuery(searchQuery, false);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        if (BaseView.isTablet(getContext())) {
            appCMSPresenter.unrestrictPortraitOnly();
        }

        appCMSPresenter.closeSoftKeyboard();
        appCMSSearchView.clearFocus();
        if (onSaveSearchQuery != null) {
            onSaveSearchQuery.saveQuery(searchQuery);
        }
    }

    @SuppressWarnings("ConstantConditions")
    private void setBgColor(int bgColor) {
        appCMSNavigationMenuMainLayout.setBackgroundColor(bgColor);
    }

    public interface OnSaveSearchQuery {
        void saveQuery(String searchQuery);

        String restoreQuery();
    }
}
