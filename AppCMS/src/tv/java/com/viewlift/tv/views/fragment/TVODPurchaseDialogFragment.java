package com.viewlift.tv.views.fragment;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.style.ClickableSpan;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.viewlift.AppCMSApplication;
import com.viewlift.R;
import com.viewlift.models.billing.appcms.purchase.TvodPurchaseData;
import com.viewlift.models.data.appcms.api.ContentDatum;
import com.viewlift.models.data.appcms.api.Season_;
import com.viewlift.models.data.appcms.ui.android.MetaPage;
import com.viewlift.models.data.appcms.ui.android.NavigationUser;
import com.viewlift.models.data.appcms.ui.page.Component;
import com.viewlift.presenters.AppCMSPresenter;
import com.viewlift.tv.utility.Utils;
import com.viewlift.tv.views.customviews.RecyclerViewItemDecoration;
import com.viewlift.utils.CommonUtils;
import com.viewlift.utils.ContentTypeChecker;

import java.util.ArrayList;
import java.util.List;

import rx.functions.Action1;

public class TVODPurchaseDialogFragment extends AbsDialogFragment implements View.OnClickListener, View.OnFocusChangeListener {

    public static final String DIALOG_HEIGHT_KEY = "dialog_height_key";
    public static final String DIALOG_WIDTH_KEY = "dialog_width_key";
    public static final String DIALOG_TITLE_KEY = "dialog_title_key";
    public static final String DIALOG_MESSAGE_KEY = "dialog_message_key";
    public static final String DIALOG_MESSAGE__SIZE_KEY = "dialog_message_size_key";
    public static final String DIALOG_MESSAGE_TEXT_COLOR_KEY = "dialog_message_text_color_key";
    public static final String DIALOG_BACKGROUND_COLOR = "dialog_background_color";
    public static final String DIALOG_TYPE_TEXT_KEY = "dialog_type_text_key";
    private Context mContext;
    private String dialogType;
    private Action1<String> onBackKeyListener;
    private LinearLayout parentLayout;
    private RelativeLayout seriesView, seasonView, seasonRootView, episodeView, episodeRootView;
    private TextView tvSeriesTitle, tvSeasonTitle, tvEpisodeTitle;
    private TextView tvSeriesPlanPrice, tvSeasonPlanPrice, tvEpisodePlanPrice;
    private TextView tvSeasonEdit, tvEpisodeEdit;
    private TextView tvTitle, tvDescription;
    private AppCMSPresenter appCMSPresenter;
    private RecyclerView recyclerView;
    private Season_ season;
    private ContentDatum episode, series;
    private String dialogTitle, planDescription;
    private ContentTypeChecker contentTypeChecker;
    private boolean isSeasonViewAdapter;

    public TVODPurchaseDialogFragment() {
        setStyle(DialogFragment.STYLE_NO_TITLE, R.style.blurDialog);
    }

    public static TVODPurchaseDialogFragment newInstance(Bundle bundle) {
        TVODPurchaseDialogFragment fragment = new TVODPurchaseDialogFragment();
        fragment.setArguments(bundle);

        return fragment;
    }

    public void setOnBackKeyListener(Action1<String> onBackKeyListener) {
        this.onBackKeyListener = onBackKeyListener;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View mView = inflater.inflate(R.layout.tvod_purchase_dialog, container, false);

        appCMSPresenter = ((AppCMSApplication) getActivity().getApplication())
                .getAppCMSPresenterComponent()
                .appCMSPresenter();

        Bundle arguments = getArguments();
        dialogType = arguments.getString(DIALOG_TYPE_TEXT_KEY, null);

        contentTypeChecker = new ContentTypeChecker(getContext());
        String backGroundColor = appCMSPresenter.getAppBackgroundColor();
        if (arguments.getString(DIALOG_BACKGROUND_COLOR, null) != null)
            backGroundColor = arguments.getString(DIALOG_BACKGROUND_COLOR, null);
        mView.findViewById(R.id.fragment_clear_overlay).setBackgroundColor(Color.parseColor(backGroundColor));

        findViewById(mView);
        setTextViewStyleColor();

        dialogTitle = arguments.getString(DIALOG_TITLE_KEY, null);
        if (!TextUtils.isEmpty(dialogTitle)) {
            tvTitle.setVisibility(View.VISIBLE);
            tvTitle.setText(dialogTitle);
        }

        setDescriptionText();

        recyclerView.setVisibility(View.GONE);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(),
                LinearLayoutManager.VERTICAL,
                false);
        recyclerView
                .setLayoutManager(linearLayoutManager);
        recyclerView.addItemDecoration(new RecyclerViewItemDecoration(40, RecyclerViewItemDecoration.VERTICAL));

        setSeries(appCMSPresenter.getModuleApi().getContentData().get(0));
        setSeason(getSeries().getSeason().get(0));
        setEpisode(getSeries().getSeason().get(0).getEpisodes().get(0));

        setSeriesData();
        setSeasonData();
        setEpisodeData();

        getDialog().setOnKeyListener((dialog, keyCode, event) -> {
            if (keyCode == KeyEvent.KEYCODE_BACK
                    && event.getAction() == KeyEvent.ACTION_DOWN) {
                if (recyclerView.getVisibility() == View.VISIBLE) {
                    recyclerView.setVisibility(View.GONE);
                    parentLayout.setVisibility(View.VISIBLE);
                    tvTitle.setText(dialogTitle);
                    setDescriptionText();
                } else {
                    dismiss();
                }
                return true;
            }
            return false;
        });

        return mView;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }

    private void findViewById(View mView) {
        tvTitle = mView.findViewById(R.id.text_overlay_title);
        tvDescription = mView.findViewById(R.id.text_description);
        parentLayout = mView.findViewById(R.id.parentLayout);

        seriesView = mView.findViewById(R.id.seriesView);
        seasonView = mView.findViewById(R.id.seasonView);
        seasonRootView = mView.findViewById(R.id.seasonRootView);
        episodeView = mView.findViewById(R.id.episodeView);
        episodeRootView = mView.findViewById(R.id.episodeRootView);

        tvSeriesTitle = mView.findViewById(R.id.seriesTitle);
        tvSeasonTitle = mView.findViewById(R.id.seasonTitle);
        tvEpisodeTitle = mView.findViewById(R.id.episodeTitle);

        tvSeriesPlanPrice = mView.findViewById(R.id.seriesPlanPrice);
        tvSeasonPlanPrice = mView.findViewById(R.id.seasonPlanPrice);
        tvEpisodePlanPrice = mView.findViewById(R.id.episodePlanPrice);

        tvSeasonEdit = mView.findViewById(R.id.seasonEdit);
        tvEpisodeEdit = mView.findViewById(R.id.episodeEdit);

        recyclerView = mView.findViewById(R.id.recyclerView);
    }

    private void setTextViewStyleColor() {
        tvTitle.setTextColor(appCMSPresenter.getBrandSecondaryCtaTextColor());
        tvTitle.setTypeface(Utils.getSpecificTypeface(getActivity(), appCMSPresenter, getString(R.string.app_cms_page_font_bold_key)));

        tvSeriesTitle.setTextColor(appCMSPresenter.getBrandSecondaryCtaTextColor());
        tvSeriesTitle.setTypeface(Utils.getSpecificTypeface(getActivity(), appCMSPresenter, getString(R.string.app_cms_page_font_bold_key)));

        tvSeasonTitle.setTextColor(appCMSPresenter.getBrandSecondaryCtaTextColor());
        tvSeasonTitle.setTypeface(Utils.getSpecificTypeface(getActivity(), appCMSPresenter, getString(R.string.app_cms_page_font_bold_key)));

        tvEpisodeTitle.setTextColor(appCMSPresenter.getBrandSecondaryCtaTextColor());
        tvEpisodeTitle.setTypeface(Utils.getSpecificTypeface(getActivity(), appCMSPresenter, getString(R.string.app_cms_page_font_bold_key)));

        tvSeriesPlanPrice.setTextColor(appCMSPresenter.getBrandSecondaryCtaTextColor());
        tvSeriesPlanPrice.setTypeface(Utils.getSpecificTypeface(getActivity(), appCMSPresenter, getString(R.string.app_cms_page_font_bold_key)));

        tvSeasonPlanPrice.setTextColor(appCMSPresenter.getBrandSecondaryCtaTextColor());
        tvSeasonPlanPrice.setTypeface(Utils.getSpecificTypeface(getActivity(), appCMSPresenter, getString(R.string.app_cms_page_font_bold_key)));

        tvEpisodePlanPrice.setTextColor(appCMSPresenter.getBrandSecondaryCtaTextColor());
        tvEpisodePlanPrice.setTypeface(Utils.getSpecificTypeface(getActivity(), appCMSPresenter, getString(R.string.app_cms_page_font_bold_key)));

        tvSeasonEdit.setTextColor(appCMSPresenter.getBrandSecondaryCtaTextColor());
        tvSeasonEdit.setTypeface(Utils.getSpecificTypeface(getActivity(), appCMSPresenter, getString(R.string.app_cms_page_font_bold_key)));
        tvSeasonEdit.setOnClickListener(this);

        tvEpisodeEdit.setTextColor(appCMSPresenter.getBrandSecondaryCtaTextColor());
        tvEpisodeEdit.setTypeface(Utils.getSpecificTypeface(getActivity(), appCMSPresenter, getString(R.string.app_cms_page_font_bold_key)));
        tvEpisodeEdit.setOnClickListener(this);

        seriesView.setBackground(Utils.setButtonBackgroundSelector(getActivity(),
                Color.parseColor(Utils.getFocusColor(getActivity(), appCMSPresenter)),
                getComponent(),
                appCMSPresenter));
        seasonView.setBackground(Utils.setButtonBackgroundSelector(getActivity(),
                Color.parseColor(Utils.getFocusColor(getActivity(), appCMSPresenter)),
                getComponent(),
                appCMSPresenter));
        episodeView.setBackground(Utils.setButtonBackgroundSelector(getActivity(),
                Color.parseColor(Utils.getFocusColor(getActivity(), appCMSPresenter)),
                getComponent(),
                appCMSPresenter));

        tvSeasonEdit.setBackground(Utils.setButtonBackgroundSelector(getActivity(),
                Color.parseColor(Utils.getFocusColor(getActivity(), appCMSPresenter)),
                getComponent(),
                appCMSPresenter));
        tvEpisodeEdit.setBackground(Utils.setButtonBackgroundSelector(getActivity(),
                Color.parseColor(Utils.getFocusColor(getActivity(), appCMSPresenter)),
                getComponent(),
                appCMSPresenter));

        seriesView.setOnClickListener(this);
        seasonView.setOnClickListener(this);
        episodeView.setOnClickListener(this);

        seriesView.setOnFocusChangeListener(this);
        seasonView.setOnFocusChangeListener(this);
        episodeView.setOnFocusChangeListener(this);
        tvSeasonEdit.setOnFocusChangeListener(this);
        tvEpisodeEdit.setOnFocusChangeListener(this);

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
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.episodeEdit:
            case R.id.seasonEdit:
                if (v.getId() == R.id.seasonEdit) {
                    isSeasonViewAdapter = true;
                    tvTitle.setText(appCMSPresenter.getLocalisedStrings().getSeasonLabelText());
                } else {
                    isSeasonViewAdapter = false;
                    tvTitle.setText(appCMSPresenter.getLocalisedStrings().getEpisodeText());
                }
                parentLayout.setVisibility(View.GONE);
                tvDescription.setVisibility(View.GONE);
                recyclerView.setVisibility(View.VISIBLE);
                recyclerView.setAdapter(new SeasonViewAdapter(getActivity(), appCMSPresenter));
                break;

            case R.id.seriesView:
                if (!appCMSPresenter.isUserLoggedIn()) {
                    openLoginDialog(appCMSPresenter);
                }else if(!appCMSPresenter.isFireTVSubscriptionEnabled()){
                    appCMSPresenter.getCurrentActivity().sendBroadcast(new Intent(AppCMSPresenter.ENTITLEMENT_LOGIN_DIALOG));
                }else {
                    if (getSeries().getSeriesPlans() != null
                            && !contentTypeChecker.isContentPurchased(appCMSPresenter.getAppPreference().getUserPurchases(), getSeries().getGist().getId())) {
                        ContentDatum selectedSeriesPlan = contentTypeChecker.tvodPlan(getSeries().getSeriesPlans());
                        if (selectedSeriesPlan != null) {
                            TvodPurchaseData tvodPurchaseData = new TvodPurchaseData(getSeries().getId(),
                                    null,
                                    null, true, false, false, dialogType.equalsIgnoreCase(mContext.getString(R.string.rent_options)),
                                    selectedSeriesPlan, getSeries().getGist().getId() , getSeries().getGist().getTitle() != null ? getSeries().getGist().getTitle() : "");
                            initiateTVODPurchase(tvodPurchaseData);
                        }
                    }
                }
                break;
            case R.id.seasonView:
                if (!appCMSPresenter.isUserLoggedIn()) {
                    openLoginDialog(appCMSPresenter);
                }else if(!appCMSPresenter.isFireTVSubscriptionEnabled()){
                    appCMSPresenter.getCurrentActivity().sendBroadcast(new Intent(AppCMSPresenter.ENTITLEMENT_LOGIN_DIALOG));
                } else {
                    if (getSeason().getSeasonPlans() != null && !isSeasonPurchased()) {
                        ContentDatum selectedSeasonPlan = contentTypeChecker.tvodPlan(getSeason().getSeasonPlans());
                        if (selectedSeasonPlan != null) {
                            TvodPurchaseData seasonTVODPurchaseData = new TvodPurchaseData(null,
                                    getSeason().getId(),
                                    null, false, true, false, dialogType.equalsIgnoreCase(mContext.getString(R.string.rent_options)),
                                    selectedSeasonPlan, selectedSeasonPlan.getId() ,selectedSeasonPlan.getTitle());
                            initiateTVODPurchase(seasonTVODPurchaseData);
                        }
                    }
                }
                break;
            case R.id.episodeView:
                if (!appCMSPresenter.isUserLoggedIn()) {
                    openLoginDialog(appCMSPresenter);
                }else if(!appCMSPresenter.isFireTVSubscriptionEnabled()){
                    appCMSPresenter.getCurrentActivity().sendBroadcast(new Intent(AppCMSPresenter.ENTITLEMENT_LOGIN_DIALOG));
                } else {
                    if (getEpisode().getEpisodePlans() != null && !isEpisodePurchased(getEpisode())) {
                        ContentDatum selectedEpisodePlan = contentTypeChecker.tvodPlan(getEpisode().getEpisodePlans());
                        if (selectedEpisodePlan != null &&  getEpisode().getGist() != null) {
                            TvodPurchaseData episodeTVODPurchaseData = new TvodPurchaseData(null,
                                    null,
                                    getEpisode().getGist().getId(), false, false, false, dialogType.equalsIgnoreCase(mContext.getString(R.string.rent_options)),
                                    selectedEpisodePlan , getEpisode().getGist().getId(), getEpisode().getGist().getTitle());
                            initiateTVODPurchase(episodeTVODPurchaseData);
                        }
                    }
                }
                break;
        }
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        switch (v.getId()) {
            case R.id.seasonEdit:
                setEditButtonVisibility(hasFocus, tvEpisodeEdit);
                setTextViewColor(hasFocus, tvSeasonEdit);
                break;
            case R.id.seasonView:
                setEditButtonVisibility(hasFocus, tvEpisodeEdit);
                setTextViewColor(hasFocus, tvSeasonTitle);
                setTextViewColor(hasFocus, tvSeasonPlanPrice);
                break;

            case R.id.episodeView:
                setEditButtonVisibility(hasFocus, tvSeasonEdit);
                setTextViewColor(hasFocus, tvEpisodeTitle);
                setTextViewColor(hasFocus, tvEpisodePlanPrice);
                break;
            case R.id.episodeEdit:
                setEditButtonVisibility(hasFocus, tvSeasonEdit);
                setTextViewColor(hasFocus, tvEpisodeEdit);
                break;

            case R.id.seriesView:
                setEditButtonVisibility(hasFocus, tvEpisodeEdit);
                setEditButtonVisibility(hasFocus, tvSeasonEdit);
                setTextViewColor(hasFocus, tvSeriesTitle);
                setTextViewColor(hasFocus, tvSeriesPlanPrice);
                break;
            case R.id.text_description:
                setEditButtonVisibility(hasFocus, tvEpisodeEdit);
                setEditButtonVisibility(hasFocus, tvSeasonEdit);
                break;
        }
    }

    private void setEditButtonVisibility(boolean isVisible, View view) {
        view.setVisibility(isVisible ? View.GONE : View.VISIBLE);
    }

    private void setTextViewColor(boolean isVisible, TextView view) {
        view.setTextColor(isVisible ? appCMSPresenter.getBrandPrimaryCtaTextColor() : appCMSPresenter.getBrandSecondaryCtaTextColor());
    }


    public class SeasonViewAdapter extends RecyclerView.Adapter<SeasonViewAdapter.ViewHolder> {
        private List<Season_> mSeasonList;
        private List<ContentDatum> mEpisodeList;

        public class ViewHolder extends RecyclerView.ViewHolder {
            public RelativeLayout rootView;
            public TextView textTitle, textPlanPrice;

            public ViewHolder(View itemView) {
                super(itemView);
                rootView = itemView.findViewById(R.id.rootView);
                textTitle = itemView.findViewById(R.id.title);
                textPlanPrice = itemView.findViewById(R.id.planPrice);
            }
        }

        public SeasonViewAdapter(Context activity,
                                 AppCMSPresenter appCMSPresenter) {
            setHasStableIds(true);
            if (isSeasonViewAdapter) {
                mSeasonList = getSeasonList();
            } else {
                mEpisodeList = getEpisodeList();
            }
        }

        @Override
        public SeasonViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                               int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.tvod_purchase_season_item, parent, false);
            ViewHolder vh = new ViewHolder(view);

            vh.rootView.setBackground(Utils.setButtonBackgroundSelector(getActivity(),
                    Color.parseColor(Utils.getFocusColor(getActivity(), appCMSPresenter)),
                    getComponent(),
                    appCMSPresenter));

            setTextViewColor(false, vh.textTitle);
            setTextViewColor(false, vh.textPlanPrice);

            vh.rootView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    setTextViewColor(hasFocus, vh.textTitle);
                    setTextViewColor(hasFocus, vh.textPlanPrice);
                }
            });
            return vh;
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            String price;
            holder.rootView.setVisibility(View.VISIBLE);
            if (isSeasonViewAdapter) {
                holder.textTitle.setText(mSeasonList.get(position).getTitle());
                if (contentTypeChecker.isContentRentedByUser(appCMSPresenter.getAppPreference().getUserPurchases(), getSeason().getId())
                        || (getSeason().getEpisodes() != null && getSeason().getEpisodes().size() > 0
                        && contentTypeChecker.isContentRentedByUser(appCMSPresenter.getAppPreference().getUserPurchases(), getSeason().getEpisodes().get(0).getGist().getId()))) {
                    holder.textPlanPrice.setText(appCMSPresenter.getLocalisedStrings().getRentedLabel());
                } else if (contentTypeChecker.isContentPurchasedByUser(appCMSPresenter.getAppPreference().getUserPurchases(), getSeason().getId())
                        || (getSeason().getEpisodes() != null && getSeason().getEpisodes().size() > 0
                        && contentTypeChecker.isContentPurchasedByUser(appCMSPresenter.getAppPreference().getUserPurchases(), getSeason().getEpisodes().get(0).getGist().getId()))) {
                    holder.textPlanPrice.setText(appCMSPresenter.getLocalisedStrings().getPurchasedLabel());
                } else {
                    price = contentTypeChecker.fetchPlanPrice(mSeasonList.get(position).getSeasonPlans(), dialogType);
                     if (price.equalsIgnoreCase(mContext.getString(R.string.not_applicable))) {
                        holder.textPlanPrice.setVisibility(View.GONE);
                        holder.rootView.setVisibility(View.GONE);
                    } else {
                        holder.textPlanPrice.setText(price);
                    }
                }
            } else {
                holder.textTitle.setText(mEpisodeList.get(position).getGist().getTitle());
                if (contentTypeChecker.isContentRentedByUser(appCMSPresenter.getAppPreference().getUserPurchases(), mEpisodeList.get(position).getGist().getId())
                        || contentTypeChecker.isContentRentedByUser(appCMSPresenter.getAppPreference().getUserPurchases(), getSeason().getId())) {
                    holder.textPlanPrice.setText(appCMSPresenter.getLocalisedStrings().getRentedLabel());
                } else if (contentTypeChecker.isContentPurchasedByUser(appCMSPresenter.getAppPreference().getUserPurchases(), mEpisodeList.get(position).getGist().getId())
                        || contentTypeChecker.isContentPurchasedByUser(appCMSPresenter.getAppPreference().getUserPurchases(), getSeason().getId())) {
                    holder.textPlanPrice.setText(appCMSPresenter.getLocalisedStrings().getPurchasedLabel());
                } else {
                    price = contentTypeChecker.fetchPlanPrice(mEpisodeList.get(position).getEpisodePlans(), dialogType);
                     if (price.equalsIgnoreCase(mContext.getString(R.string.not_applicable))) {
                        holder.textPlanPrice.setVisibility(View.GONE);
                        holder.rootView.setVisibility(View.GONE);
                    } else {
                        holder.textPlanPrice.setText(contentTypeChecker.fetchPlanPrice(mEpisodeList.get(position).getEpisodePlans(), dialogType));
                    }
                }
            }

            holder.rootView.setOnClickListener(view -> {
                recyclerView.setVisibility(View.GONE);
                parentLayout.setVisibility(View.VISIBLE);
                setDescriptionText();
                tvTitle.setText(dialogTitle);
                if (isSeasonViewAdapter) {
                    setSeason(mSeasonList.get(position));
                    setSeasonData();

                    setEpisode(getSeason().getEpisodes().get(0));
                    setEpisodeData();
                } else {
                    setEpisode(mEpisodeList.get(position));
                    setEpisodeData();
                }
            });
        }

        @Override
        public int getItemCount() {
            if (isSeasonViewAdapter) {
                return mSeasonList.size();
            } else {
                return mEpisodeList.size();
            }
        }

        @Override
        public long getItemId(int position) {
            return position;
        }
    }

    private void setDescriptionText() {
        if (dialogType.equalsIgnoreCase(mContext.getString(R.string.rent_options))){
            tvDescription.setVisibility(View.VISIBLE);
        } else {
            tvDescription.setVisibility(View.GONE);
            return;
        }
        tvDescription.setFocusable(true);
        tvDescription.setText(appCMSPresenter.getLocalisedStrings().getTimePeriodVerbiage());
        tvDescription.setLinkTextColor(appCMSPresenter.getBrandPrimaryCtaBorderColor());
        ClickableSpan tosClick = new ClickableSpan() {
            @Override
            public void onClick(View view) {
                MetaPage tosPage = appCMSPresenter.getTosPage();
                if (null != tosPage) {
                    appCMSPresenter.navigateToTVPage(
                            tosPage.getPageId(),
                            tosPage.getPageName(),
                            tosPage.getPageUI(),
                            false,
                            Uri.EMPTY,
                            false,
                            true,
                            false, false, false, false);
                }
            }
        };
        ClickableSpan privacyClick = new ClickableSpan() {
            @Override
            public void onClick(View view) {
                MetaPage privacyPolicyPage = appCMSPresenter.getPrivacyPolicyPage();
                if (null != privacyPolicyPage) {
                    appCMSPresenter.navigateToTVPage(
                            privacyPolicyPage.getPageId(),
                            privacyPolicyPage.getPageName(),
                            privacyPolicyPage.getPageUI(),
                            false,
                            Uri.EMPTY,
                            false,
                            true,
                            false, false, false, false);
                }
            }
        };
        appCMSPresenter.makeTextViewLinks(tvDescription, new String[]{
                mContext.getString(R.string.terms_of_service),
                appCMSPresenter.getLocalisedStrings().getPrivacyPolicyText()}, new ClickableSpan[]{tosClick, privacyClick}, false);

        tvDescription.setOnFocusChangeListener(this);
    }

    private void setSeasonData() {
        if (getSeason() != null) {
            seasonRootView.setVisibility(View.VISIBLE);
            tvSeasonTitle.setText(getSeason().getTitle());
            if (contentTypeChecker.isContentRentedByUser(appCMSPresenter.getAppPreference().getUserPurchases(), getSeason().getId())
                    || (dialogType.equalsIgnoreCase(mContext.getString(R.string.rent_options))
                    && isSeasonPurchased())) {
                tvSeasonPlanPrice.setText(appCMSPresenter.getLocalisedStrings().getRentedLabel());
            } else if (contentTypeChecker.isContentPurchasedByUser(appCMSPresenter.getAppPreference().getUserPurchases(), getSeason().getId())
                    || isSeasonPurchased()) {
                tvSeasonPlanPrice.setText(appCMSPresenter.getLocalisedStrings().getPurchasedLabel());
            } else {
                String price = contentTypeChecker.fetchPlanPrice(getSeason().getSeasonPlans(), dialogType);
                if (price.equalsIgnoreCase(mContext.getString(R.string.not_applicable))) {
                    seasonRootView.setVisibility(View.GONE);
                } else {
                    tvSeasonPlanPrice.setText(price);
                }
            }
        }
    }

    private void setEpisodeData() {
        if (getEpisode() != null) {
            episodeRootView.setVisibility(View.VISIBLE);
            tvEpisodeTitle.setText(getEpisode().getGist().getTitle());
            if (contentTypeChecker.isContentRentedByUser(appCMSPresenter.getAppPreference().getUserPurchases(), getEpisode().getGist().getId())
                    || contentTypeChecker.isContentRentedByUser(appCMSPresenter.getAppPreference().getUserPurchases(), getSeason().getId())) {
                tvEpisodePlanPrice.setText(appCMSPresenter.getLocalisedStrings().getRentedLabel());
            } else if (contentTypeChecker.isContentPurchasedByUser(appCMSPresenter.getAppPreference().getUserPurchases(), getEpisode().getGist().getId())
                    || contentTypeChecker.isContentPurchasedByUser(appCMSPresenter.getAppPreference().getUserPurchases(), getSeason().getId())) {
                tvEpisodePlanPrice.setText(appCMSPresenter.getLocalisedStrings().getPurchasedLabel());
            } else {
                String price = contentTypeChecker.fetchPlanPrice(getEpisode().getEpisodePlans(), dialogType);
                if (price.equalsIgnoreCase(mContext.getString(R.string.not_applicable))) {
                    episodeRootView.setVisibility(View.GONE);
                } else {
                    tvEpisodePlanPrice.setText(price);
                }
            }
        }
    }

    private void setSeriesData() {
        seriesView.setVisibility(View.VISIBLE);
        tvSeriesTitle.setText(getSeries().getGist().getTitle());
        if (contentTypeChecker.isContentPurchasedByUser(appCMSPresenter.getAppPreference().getUserPurchases(), getSeries().getGist().getId())) {
            tvSeriesPlanPrice.setText(appCMSPresenter.getLocalisedStrings().getPurchasedLabel());
        } else if (contentTypeChecker.isContentRentedByUser(appCMSPresenter.getAppPreference().getUserPurchases(), getSeries().getGist().getId())) {
            tvSeriesPlanPrice.setText(appCMSPresenter.getLocalisedStrings().getRentedLabel());
        } else {
            String price = contentTypeChecker.fetchPlanPrice(getSeries().getSeriesPlans(), dialogType);
            if (price.equalsIgnoreCase(mContext.getString(R.string.not_applicable))) {
                seriesView.setVisibility(View.GONE);
            } else {
                tvSeriesPlanPrice.setText(price);
            }
        }
    }

    public void setSeries(ContentDatum series) {
        this.series = series;
    }

    public ContentDatum getSeries() {
        return series;
    }

    public void setSeason(Season_ season) {
        this.season = season;
    }

    public Season_ getSeason() {
        return season;
    }

    public void setEpisode(ContentDatum episode) {
        this.episode = episode;
    }

    public ContentDatum getEpisode() {
        return episode;
    }

    public List<Season_> getSeasonList() {
        List<Season_> seasonContentList = new ArrayList<>();
        try {
            if (getSeries().getSeason() != null &&
                    getSeries().getSeason().size() > 0) {
                seasonContentList = getSeries().getSeason();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return seasonContentList;
    }

    public List<ContentDatum> getEpisodeList() {
        List<ContentDatum> episodeList = new ArrayList<>();
        try {
            if (getSeason() != null
                    && getSeason().getEpisodes() != null
                    && getSeason().getEpisodes().size() > 0) {
                episodeList = getSeason().getEpisodes();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return episodeList;
    }

    private void initiateTVODPurchase(TvodPurchaseData tvodPurchaseData) {
        dismiss();
        appCMSPresenter.setContentToPurchase(tvodPurchaseData);
        appCMSPresenter.initiateTvodPurchase();
    }

    private Component getComponent() {
        Component component = new Component();
        component.setFontFamily(getString(R.string.app_cms_page_font_family_key));
        component.setFontWeight(getString(R.string.app_cms_page_font_bold_key));
        component.setCornerRadius(4);
        component.setBorderColor(CommonUtils.getColor(getActivity(), Integer.toHexString(ContextCompat.getColor(getActivity(),
                R.color.btn_color_with_opacity))));
        component.setBorderWidth(4);
        return component;
    }

    private void openLoginDialog(AppCMSPresenter appCMSPresenter) {
        appCMSPresenter.setLaunchType(AppCMSPresenter.LaunchType.LOGIN_AND_SIGNUP);
        NavigationUser navigationUser = appCMSPresenter.getLoginNavigation();
        appCMSPresenter.navigateToTVPage(
                navigationUser.getPageId(),
                navigationUser.getTitle(),
                navigationUser.getUrl(),
                false,
                Uri.EMPTY,
                false,
                false,
                true,
                false, false, false);
    }

    private boolean isSeasonPurchased() {
        boolean isEveryContentPurchased;
        boolean isSeasonsPurchased;
        boolean isAllEpisodesPurchased;
        isSeasonsPurchased = contentTypeChecker.isContentPurchased(appCMSPresenter.getAppPreference().getUserPurchases(), getSeason().getId());
        isAllEpisodesPurchased = contentTypeChecker.isAllEpisodesPurchased(appCMSPresenter.getAppPreference().getUserPurchases(), getSeasonList());
        isEveryContentPurchased = isSeasonsPurchased || isAllEpisodesPurchased;
        return isEveryContentPurchased;
    }

    private boolean isEpisodePurchased(ContentDatum episode) {
        boolean isContentPurchased;
        boolean isSeasonsPurchased;
        boolean isEpisodesPurchased;
        boolean isEpisodesFree;
        isSeasonsPurchased = contentTypeChecker.isContentPurchased(appCMSPresenter.getAppPreference().getUserPurchases(), getSeason().getId());
        isEpisodesPurchased = contentTypeChecker.isContentPurchased(appCMSPresenter.getAppPreference().getUserPurchases(), episode.getGist().getId());
        isEpisodesFree = contentTypeChecker.isContentFREEMonetization(episode.getMonetizationModels()) || contentTypeChecker.isContentAVODMonetization(episode.getMonetizationModels());
        isContentPurchased = isSeasonsPurchased || isEpisodesPurchased || isEpisodesFree;
        return isContentPurchased;
    }

}
