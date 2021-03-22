package com.viewlift.tv.views.fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.viewlift.AppCMSApplication;
import com.viewlift.R;
import com.viewlift.models.data.appcms.ui.main.RecommendationGenre;
import com.viewlift.models.data.appcms.ui.page.Component;
import com.viewlift.presenters.AppCMSPresenter;
import com.viewlift.tv.utility.Utils;
import com.viewlift.tv.views.activity.AppCmsHomeActivity;
import com.viewlift.utils.CommonUtils;
import com.viewlift.views.adapters.RecommendationDataAdapter;

import java.util.ArrayList;

import rx.functions.Action1;

import static com.viewlift.presenters.AppCMSPresenter.SHOW_SUBSCRIPTION_MESSAGE_ON_VIDEO_PLAYER_ACTION;

/**
 * Created by anas.azeem on 9/13/2017.
 * Owned by ViewLift, NYC
 */

public class RecommendationFragment extends DialogFragment {

    public static final String DIALOG_HEIGHT_KEY = "dialog_height_key";
    public static final String DIALOG_WIDTH_KEY = "dialog_width_key";
    public static final String DIALOG_TITLE_KEY = "dialog_title_key";
    public static final String DIALOG_MESSAGE_KEY = "dialog_message_key";
    public static final String DIALOG_TITLE_SIZE_KEY = "dialog_title_size_key";
    public static final String DIALOG_MESSAGE__SIZE_KEY = "dialog_message_size_key";
    public static final String DIALOG_TITLE_TEXT_COLOR_KEY = "dialog_title_text_color_key";
    public static final String DIALOG_MESSAGE_TEXT_COLOR_KEY = "dialog_message_text_color_key";
    public static final String DIALOG_POSITIVE_BUTTON_TEXT_KEY = "dialog_positive_button_text_key";
    public static final String DIALOG_NEGATIVE_BUTTON_TEXT_KEY = "dialog_negative_button_text_key";
    public static final String DIALOG_TITLE_VISIBILITY_KEY = "dialog_title_visibility_key";
    public static final String DIALOG_MESSAGE_VISIBILITY_KEY = "dialog_message_visibility_key";
    public static final String DIALOG_POSITIVE_BUTTON_VISIBILITY_KEY
            = "dialog_positive_button_visibility_key";
    public static final String DIALOG_NEGATIVE_BUTTON_VISIBILITY_KEY
            = "dialog_negative_button_visibility_key";
    public static final String OPEN_DIALOG_FROM_SETTING_PAGE = "open_dialog_from_setting_page";
    private Action1<String> onPositiveButtonClicked;
    private Action1<String> onNegativeButtonClicked;
    private Action1<String> onBackKeyListener;

    private boolean isFocusOnPositiveButton = true;
    private Button positiveButton;
    private Button negativeButton;
    private RelativeLayout rl_button,rl_header;
    private String genreString="";
    private boolean isUserSettingPage;
    private AppCMSPresenter appCMSPresenter;

    public RecommendationFragment() {
        setStyle(DialogFragment.STYLE_NORMAL, android.R.style.Theme_Translucent_NoTitleBar);
    }

    public static RecommendationFragment newInstance(Bundle bundle) {
        RecommendationFragment fragment = new RecommendationFragment();
        fragment.setArguments(bundle);
        return fragment;
    }


    public void setOnPositiveButtonClicked(Action1<String> onPositiveButtonClicked) {
        this.onPositiveButtonClicked = onPositiveButtonClicked;
    }

    public void setOnNegativeButtonClicked(Action1<String> onNegativeButtonClicked) {
        this.onNegativeButtonClicked = onNegativeButtonClicked;
    }

    public void setOnBackKeyListener(Action1<String> onBackKeyListener) {
        this.onBackKeyListener = onBackKeyListener;
    }

    ArrayList<RecommendationGenre> recommendationGenreArrayList;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View mView = inflater.inflate(R.layout.recommendation_tv_layout, container, false);

        appCMSPresenter = ((AppCMSApplication) getActivity().getApplication())
                .getAppCMSPresenterComponent()
                .appCMSPresenter();

        appCMSPresenter.sendAppsFlyerPageViewEvent("Recommendation Screen", null);
        String backGroundColor = appCMSPresenter.getAppBackgroundColor();

//        mView.findViewById(R.id.fragment_clear_overlay).setBackgroundColor(Color.parseColor(backGroundColor));

        /*Bind Views*/
        negativeButton = (Button) mView.findViewById(R.id.btn_skip);
        positiveButton = (Button) mView.findViewById(R.id.btn_shows);
        rl_button=(RelativeLayout) mView.findViewById(R.id.rl_button);
        rl_header=(RelativeLayout) mView.findViewById(R.id.rl_header);
        RecyclerView recyclerView = (RecyclerView) mView.findViewById(R.id.card_recycler_view);
        recyclerView.setHasFixedSize(true);
        int spanCount=4;
        if(appCMSPresenter.isNewsTemplate()){
            spanCount=3;
        }
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getContext(), spanCount);
        recyclerView.setLayoutManager(layoutManager);
        ArrayList<RecommendationGenre> recommendationGenreArrayList;

        if (appCMSPresenter.isFromSetting())
            recommendationGenreArrayList = prepareSelectedData();
        else
            recommendationGenreArrayList = prepareData();

        RecommendationDataAdapter adapter = new RecommendationDataAdapter(getContext(), recommendationGenreArrayList,appCMSPresenter);
        recyclerView.setAdapter(adapter);
        recyclerView.setFocusable(true);
        recyclerView.setNextFocusLeftId(R.id.btn_shows);

        TextView tvHeaderTitle = (TextView) mView.findViewById(R.id.text_overlay_title);
        tvHeaderTitle.setFocusable(false);
        tvHeaderTitle.setEnabled(false);
        tvHeaderTitle.setClickable(false);
        tvHeaderTitle.setTextColor(Color.parseColor(appCMSPresenter.getAppTextColor()));

        TextView tvSubHeaderTitle = (TextView) mView.findViewById(R.id.text_overlay_sub_title);
        tvSubHeaderTitle.setFocusable(false);
        tvSubHeaderTitle.setEnabled(false);
        tvSubHeaderTitle.setClickable(false);
        tvSubHeaderTitle.setTextColor(Color.parseColor(appCMSPresenter.getAppTextColor()));
        if (appCMSPresenter.getTemplateType() == AppCMSPresenter.TemplateType.NEWS) {
            rl_button.setHorizontalGravity(Gravity.LEFT|Gravity.CENTER_VERTICAL);
            rl_header.setHorizontalGravity(Gravity.LEFT|Gravity.CENTER_VERTICAL);
        }else {
            rl_button.setHorizontalGravity(Gravity.CENTER_HORIZONTAL);
            rl_header.setHorizontalGravity(Gravity.CENTER_HORIZONTAL);
            tvSubHeaderTitle.setGravity(Gravity.CENTER_HORIZONTAL);
            tvHeaderTitle.setGravity(Gravity.CENTER_HORIZONTAL);
        }
        tvSubHeaderTitle.setText(appCMSPresenter.getLocalisedStrings().getRecommendationSubTitle());

        /*Request focus on the description */
        //tvDescription.requestFocus();
        Bundle arguments = getArguments();
        String title = arguments.getString(DIALOG_TITLE_KEY, null);
        String description = arguments.getString(DIALOG_MESSAGE_KEY, null);
        String textColor = arguments.getString(DIALOG_MESSAGE_TEXT_COLOR_KEY, null);
        String positiveBtnText = arguments.getString(DIALOG_POSITIVE_BUTTON_TEXT_KEY, null);
        String negativeBtnText = arguments.getString(DIALOG_NEGATIVE_BUTTON_TEXT_KEY, null);
        float messageSize = arguments.getFloat(DIALOG_MESSAGE__SIZE_KEY);
        isUserSettingPage = arguments.getBoolean(OPEN_DIALOG_FROM_SETTING_PAGE, false);

        if (appCMSPresenter.getLocalisedStrings() != null) {

            if (appCMSPresenter.getLocalisedStrings().getRecommendationTitle() != null) {
                tvHeaderTitle.setText(appCMSPresenter.getLocalisedStrings().getRecommendationTitle());
            }
            if (!appCMSPresenter.isFromSetting()) {
                positiveButton.setText(appCMSPresenter.getLocalisedStrings().getStartBrowsingText());
            } else {
                positiveButton.setText(appCMSPresenter.getLocalisedStrings().getSaveText());
            }
            negativeButton.setText(appCMSPresenter.getLocalisedStrings().getSkipText());
        }


//        appCMSPresenter.setIsFromSetting(false);

        if (description == null) {
            throw new RuntimeException("Description is null");
        }


        textColor = appCMSPresenter.getAppTextColor();
        String desc_text = getString(R.string.text_with_color,
                Integer.toHexString(Color.parseColor(textColor)).substring(2),
                description);

        /*if (!TextUtils.isEmpty(title)) {
            tvTitle.setText(title);
        }*/

        Component component = new Component();
        component.setFontFamily(getString(R.string.app_cms_page_font_family_key));

        //tvDescription.setTextSize(R.dimen.text_ovelay_dialog_desc_font_size);

        Component btnComponent1 = new Component();
        btnComponent1.setFontFamily(getString(R.string.app_cms_page_font_family_key));
        btnComponent1.setFontWeight(getString(R.string.app_cms_page_font_semibold_key));
        btnComponent1.setBorderColor(CommonUtils.getColor(getActivity(), Integer.toHexString(ContextCompat.getColor(getActivity(),
                R.color.btn_color_with_opacity))));
        btnComponent1.setBorderWidth(4);

        negativeButton.setBackground(Utils.setButtonBackgroundSelector(getActivity(),
                Color.parseColor(Utils.getFocusColor(getActivity(), appCMSPresenter)),
                btnComponent1,
                appCMSPresenter));

        negativeButton.setTextColor(Utils.getButtonTextColorDrawable(
                CommonUtils.getColor(getActivity(), Integer.toHexString(ContextCompat.getColor(getActivity(),
                        R.color.btn_color_with_opacity))),
                CommonUtils.getColor(getActivity(), Integer.toHexString(ContextCompat.getColor(getActivity(),
                        android.R.color.white))), appCMSPresenter
        ));


//        negativeButton.setTypeface(Utils.getTypeFace(getActivity(), appCMSPresenter.getJsonValueKeyMap(), btnComponent1));

        positiveButton.setBackground(Utils.setButtonBackgroundSelector(getActivity(),
                Color.parseColor(Utils.getFocusColor(getActivity(), appCMSPresenter)),
                btnComponent1,
                appCMSPresenter));

        positiveButton.setTextColor(Utils.getButtonTextColorDrawable(
                CommonUtils.getColor(getActivity(), Integer.toHexString(ContextCompat.getColor(getActivity(),
                        R.color.btn_color_with_opacity))),
                CommonUtils.getColor(getActivity(), Integer.toHexString(ContextCompat.getColor(getActivity(),
                        android.R.color.white))), appCMSPresenter
        ));


//        positiveButton.setTypeface(Utils.getTypeFace(getActivity(), appCMSPresenter.getJsonValueKeyMap(), btnComponent1));

        positiveButton.requestFocus();

        if (TextUtils.isEmpty(positiveBtnText)) {
            positiveButton.setVisibility(View.GONE);
            negativeButton.requestFocus();
        }

        if (TextUtils.isEmpty(negativeBtnText)) {
            negativeButton.setVisibility(View.GONE);
            positiveButton.requestFocus();
        }

        /*Set click listener*/
        negativeButton.setOnClickListener(v -> {
//            if(null != onNegativeButtonClicked) {
//                onNegativeButtonClicked.call("");
//            }
            dismiss();
//            appCMSPresenter.sendUserRecommendationValues(appCMSPresenter.getLoggedInUser(),"NOGENRE");
        });


        positiveButton.setOnClickListener(v -> {
                try {
                    //final String genreString="";
                    for(int i=0;i<recommendationGenreArrayList.size();i++){
                        if(recommendationGenreArrayList.get(i).isItemSelected()){
                            genreString+=recommendationGenreArrayList.get(i).getAndroid_version_name()+"|";
                        }
                    }

                if (genreString.length() > 0) {
                    genreString = genreString.substring(0, genreString.length() - 1);
                } else {
                    genreString = null;
                }
                String finalGenreString = genreString;
                appCMSPresenter.showLoader();

                    appCMSPresenter.sendUserRecommendationValues(appCMSPresenter.getLoggedInUser(), genreString, signInResponse -> {
                        System.out.println(signInResponse);
                        System.out.println();
                        if (signInResponse != null
                                && signInResponse.getErrorResponse() != null
                                && signInResponse.getErrorResponse().getMessage() != null) {
                            appCMSPresenter.stopLoader();
                            Utils.getClearDialogFragment(
                                    getContext(),
                                    appCMSPresenter,
                                    getResources().getDimensionPixelSize(R.dimen.text_clear_dialog_width),
                                    getResources().getDimensionPixelSize(R.dimen.text_add_to_watchlist_sign_in_dialog_height),
                                    "ERROR",
                                    signInResponse.getErrorResponse().getMessage(),
                                    null,
                                    null,
                                    14f);
                        } else {
                            appCMSPresenter.stopLoader();
                            appCMSPresenter.setPersonalizedGenresPreference(finalGenreString,appCMSPresenter.isFromSetting(), true);
                            if (!appCMSPresenter.isFromSetting()) {
                                appCMSPresenter.navigateToHomePage(false);
                            } else {
                                if (requireActivity() instanceof AppCmsHomeActivity) {
                                    ((AppCmsHomeActivity) requireActivity()).refreshPersonalizationSettingsModule();
                                }
                            }
                            dismiss();
                        }
                    });
            } catch (Exception e) {
                appCMSPresenter.stopLoader();
                e.printStackTrace();
            }
        });


        getDialog().setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK
                        && event.getAction() == KeyEvent.ACTION_DOWN) {
                    if (null != onBackKeyListener)
                        onBackKeyListener.call("");
                }
                return false;
            }
        });
        appCMSPresenter.stopLoader();

        return mView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        Bundle bundle = new Bundle();
        int width = getArguments().getInt(DIALOG_WIDTH_KEY);
        int height = getArguments().getInt(DIALOG_HEIGHT_KEY);
        bundle.putInt(getString(R.string.tv_dialog_width_key), width);
        bundle.putInt(getString(R.string.tv_dialog_height_key), height);
        super.onActivityCreated(bundle);
    }


    @Override
    public void onPause() {
        super.onPause();
        if (negativeButton != null && negativeButton.hasFocus()) {
            isFocusOnPositiveButton = false;
        } else {
            isFocusOnPositiveButton = true;
        }
        Log.d("ANSA onPause", "isFocusOnPositiveButton = " + isFocusOnPositiveButton);
    }

    ArrayList<RecommendationGenre> recommendedversion;

    private ArrayList<RecommendationGenre> prepareData() {
        AppCMSPresenter appCMSPresenter = ((AppCMSApplication) getActivity().getApplication())
                .getAppCMSPresenterComponent()
                .appCMSPresenter();
        recommendedversion = new ArrayList<>();
        for (int i = 0; i < appCMSPresenter.getAppCMSMain().getRecommendation().getRecommendCategories().size(); i++) {
            RecommendationGenre androidVersion = new RecommendationGenre();
            androidVersion.setAndroid_version_name(appCMSPresenter.getAppCMSMain().getRecommendation().getRecommendCategories().get(i).getTitle());
            androidVersion.setAndroid_image_url(appCMSPresenter.getAppCMSMain().getRecommendation().getRecommendCategories().get(i).getImgUrl());
            recommendedversion.add(androidVersion);
        }
        return recommendedversion;
    }


    private ArrayList<RecommendationGenre> prepareSelectedData() {
        AppCMSPresenter appCMSPresenter = ((AppCMSApplication) getActivity().getApplication())
                .getAppCMSPresenterComponent()
                .appCMSPresenter();

        recommendedversion = new ArrayList<>();
        for (int i = 0; i < appCMSPresenter.getAppCMSMain().getRecommendation().getRecommendCategories().size(); i++) {
            RecommendationGenre recommendationGenre = new RecommendationGenre();
            recommendationGenre.setAndroid_version_name(appCMSPresenter.getAppCMSMain().getRecommendation().getRecommendCategories().get(i).getTitle());
            recommendationGenre.setAndroid_image_url(appCMSPresenter.getAppCMSMain().getRecommendation().getRecommendCategories().get(i).getImgUrl());


            boolean isFound;

            if (appCMSPresenter.getPersonalizedGenresPreference() != null) {
                isFound = (appCMSPresenter.getPersonalizedGenresPreference().toLowerCase()).contains(appCMSPresenter.getAppCMSMain().getRecommendation().getRecommendCategories().get(i).getTitle().toLowerCase()); //true
                if (isFound) {
                    recommendationGenre.setItemSelected(true);
                }
            }
//            if(selectedGenreItems.size()>0){
//                if((Arrays.asList(selectedGenreItems).contains(getAppCMSMain().getRecommendation().recommendCategories.get(i).getTitle()))){
//                    recommendationGenre.setItemSelected(true);
//                };
//            }
            recommendedversion.add(recommendationGenre);
        }
        return recommendedversion;
    }


    @Override
    public void onResume() {
        super.onResume();
        new Handler().postDelayed(() -> {
            if (isVisible() && isAdded()) {
                Log.d("ANSA onResume", "isFocusOnPositiveButton = " + isFocusOnPositiveButton);
                if (isFocusOnPositiveButton) {
                    positiveButton.requestFocus();
                } else {
                    negativeButton.requestFocus();
                }
            }
        }, 500);
    }


    @Override
    public void onDetach() {
        super.onDetach();
        Intent subscriptionMessageIntent = new Intent(SHOW_SUBSCRIPTION_MESSAGE_ON_VIDEO_PLAYER_ACTION);
        appCMSPresenter.getCurrentActivity().sendBroadcast(subscriptionMessageIntent);

        Intent closeViewPlanPageIntent = new Intent(AppCMSPresenter.CLOSE_VIEW_PLANS_PAGE_ON_VIDEO_PLAYER_ACTION);
        appCMSPresenter.getCurrentActivity().sendBroadcast(closeViewPlanPageIntent);
    }
}
