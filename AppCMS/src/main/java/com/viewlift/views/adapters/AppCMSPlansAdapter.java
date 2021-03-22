package com.viewlift.views.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.viewlift.AppCMSApplication;
import com.viewlift.R;
import com.viewlift.models.data.appcms.api.ContentDatum;
import com.viewlift.models.data.appcms.api.MetadataMap;
import com.viewlift.models.data.appcms.api.Module;
import com.viewlift.models.data.appcms.ui.AppCMSUIKeyType;
import com.viewlift.models.data.appcms.ui.android.AppCMSAndroidModules;
import com.viewlift.models.data.appcms.ui.main.LocalisedStrings;
import com.viewlift.models.data.appcms.ui.page.Component;
import com.viewlift.models.data.appcms.ui.page.Layout;
import com.viewlift.models.data.appcms.ui.page.Settings;
import com.viewlift.presenters.AppCMSPresenter;
import com.viewlift.views.customviews.BaseView;
import com.viewlift.views.customviews.CollectionGridItemView;
import com.viewlift.views.customviews.ViewCreator;
import com.viewlift.views.customviews.constraintviews.ConstraintCollectionGridItemView;
import com.viewlift.views.customviews.constraintviews.ConstraintViewCreator;
import com.viewlift.views.dialog.CustomShape;

import java.util.ArrayList;
import java.util.Currency;
import java.util.List;
import java.util.Map;

import javax.annotation.Nonnull;
import javax.inject.Inject;

import static com.viewlift.models.data.appcms.ui.AppCMSUIKeyType.UI_BLOCK_SELECTPLAN_03;

/*
 * Created by viewlift on 5/5/17.
 */

public class AppCMSPlansAdapter extends RecyclerView.Adapter<AppCMSPlansAdapter.ViewHolder>
        implements AppCMSBaseAdapter {
    private static final String TAG = "AppCMSViewAdapter";
    protected Context mContext;
    protected Layout parentLayout;
    protected Component component;
    @Inject
    AppCMSPresenter appCMSPresenter;
    @Inject
    LocalisedStrings localisedStrings;
    protected Settings settings;
    protected ViewCreator viewCreator;
    protected Map<String, AppCMSUIKeyType> jsonValueKeyMap;
    Module moduleAPI;
    List<ContentDatum> adapterData;
    CollectionGridItemView.OnClickHandler onClickHandler;
    ConstraintCollectionGridItemView.OnClickHandler onConstraintClickHandler;
    int defaultWidth;
    int defaultHeight;
    boolean useMarginsAsPercentages;
    String componentViewType;
    AppCMSAndroidModules appCMSAndroidModules;
    private boolean useParentSize;
    private AppCMSUIKeyType viewTypeKey;
    private int selectedColor;
    private boolean isClickable;
    private boolean singlePlanViewShown = false;
    private boolean subscribeViewShown = false;
    public static final int TYPE_FOOTER = 2;
    public static final int TYPE_ITEM = 1;
    String blockName;
    AppCMSUIKeyType uiBlockName;
    private MetadataMap metadataMap;
    ConstraintViewCreator constraintViewCreator;


    public AppCMSPlansAdapter(Context context,
                              ViewCreator viewCreator,
                              ConstraintViewCreator constraintViewCreator,
                              Settings settings,
                              Layout parentLayout,
                              boolean useParentSize,
                              Component component,
                              Map<String, AppCMSUIKeyType> jsonValueKeyMap,
                              Module moduleAPI,
                              int defaultWidth,
                              int defaultHeight,
                              String viewType,
                              AppCMSAndroidModules appCMSAndroidModules,
                              String blockName,
                              MetadataMap metadataMap) {
        this.mContext = context;
        this.viewCreator = viewCreator;
        this.constraintViewCreator = constraintViewCreator;
        this.settings = settings;

        ((AppCMSApplication) this.mContext.getApplicationContext()).getAppCMSPresenterComponent().inject(this);
        this.parentLayout = parentLayout;
        this.useParentSize = useParentSize;
        this.blockName = blockName;
        this.component = component;
        this.jsonValueKeyMap = jsonValueKeyMap;
        this.moduleAPI = moduleAPI;
        this.metadataMap = metadataMap;
        if (moduleAPI != null && moduleAPI.getContentData() != null) {
            this.adapterData = moduleAPI.getContentData();
        } else {
            this.adapterData = new ArrayList<>();
        }
        uiBlockName = jsonValueKeyMap.get(blockName);

        if (blockName == null) {
            uiBlockName = AppCMSUIKeyType.PAGE_EMPTY_KEY;
        }
        this.componentViewType = viewType;
        this.viewTypeKey = jsonValueKeyMap.get(componentViewType);
        if (this.viewTypeKey == null) {
            this.viewTypeKey = AppCMSUIKeyType.PAGE_EMPTY_KEY;
        }
        this.defaultWidth = defaultWidth;
        this.defaultHeight = defaultHeight;
        this.useMarginsAsPercentages = true;
        this.selectedColor = appCMSPresenter.getBrandPrimaryCtaColor();
        this.isClickable = true;
        this.setHasStableIds(false);
        this.appCMSAndroidModules = appCMSAndroidModules;
    }


    @Nonnull
    @Override
    public ViewHolder onCreateViewHolder(@Nonnull ViewGroup parent, int viewType) {
        if (viewType == TYPE_ITEM) {

            if (constraintViewCreator != null && uiBlockName != UI_BLOCK_SELECTPLAN_03) {
                ConstraintCollectionGridItemView view = constraintViewCreator.createConstraintCollectionGridItemView(parent.getContext(),
                        parentLayout,
                        true,
                        component,
                        moduleAPI,
                        appCMSAndroidModules,
                        settings,
                        jsonValueKeyMap,
                        defaultWidth,
                        defaultHeight,
                        useMarginsAsPercentages,
                        false,
                        this.componentViewType != null ? this.componentViewType : component.getView(),
                        true,
                        useRoundedCorners(),
                        viewTypeKey,
                        blockName,
                        metadataMap);

                if (useRoundedCorners()) {
                    GradientDrawable shape = new GradientDrawable();
                    int cornerRadius = 34;
                    if (BaseView.isTablet(mContext))
                        cornerRadius = 24;
                    shape.setCornerRadius(cornerRadius);
                    shape.setColor(selectedColor);
                    view.setBackgroundDrawable(shape);
                }

                if (appCMSPresenter.isMOTVApp()) {
                    ConstraintLayout.LayoutParams layPar = new ConstraintLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                            ViewGroup.LayoutParams.WRAP_CONTENT);//(FrameLayout.LayoutParams) view.getLayoutParams();

                    if (BaseView.isLandscape(mContext)) {
                        layPar.setMargins(300, 25, 300, 0);
                    } else {
                        /*tablet portrait*/
                        if (BaseView.isTablet(mContext) && !BaseView.isLandscape(mContext)) {
                            layPar.setMargins(190, 20, 190, 0);
                        } else
                            /*mobile*/
                            if (!BaseView.isTablet(mContext)) {
                                layPar.setMargins(70, 45, 70, 0);
                            }
                    }
                    view.setLayoutParams(layPar);
                } else {
                    ConstraintLayout.LayoutParams layParValue = new ConstraintLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, view.getLayoutParams().height);
                    layParValue.setMargins(20, 25, 20, 25);

                    view.setLayoutParams(layParValue);
                }
                return new ViewHolder(view);
            } else {
                CollectionGridItemView view = viewCreator.createCollectionGridItemView(parent.getContext(),
                        parentLayout,
                        useParentSize,
                        component,
                        appCMSPresenter,
                        moduleAPI,
                        appCMSAndroidModules,
                        settings,
                        jsonValueKeyMap,
                        defaultWidth,
                        defaultHeight,
                        useMarginsAsPercentages,
                        true,
                        this.componentViewType,
                        false,
                        useRoundedCorners(),
                        this.viewTypeKey,
                        blockName);

                applyBgColorToChildren(view, selectedColor);
                //view.setBackgroundColor(selectedColor);
                if (appCMSPresenter.isMOTVApp()) {
                    FrameLayout.LayoutParams layPar = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                            ViewGroup.LayoutParams.WRAP_CONTENT);//(FrameLayout.LayoutParams) view.getLayoutParams();

                    if (BaseView.isLandscape(mContext)) {
                        layPar.setMargins(300, 25, 300, 0);
                    } else {
                        /*tablet portrait*/
                        if (BaseView.isTablet(mContext) && !BaseView.isLandscape(mContext)) {
                            layPar.setMargins(190, 20, 190, 0);
                        } else
                            /*mobile*/
                            if (!BaseView.isTablet(mContext)) {
                                layPar.setMargins(70, 45, 70, 0);
                            }
                    }

//            layPar.setMargins(0, 20, 0, 0);
                    layPar.gravity = Gravity.CENTER_HORIZONTAL;
                    view.setLayoutParams(layPar);
                } else {
                    FrameLayout.LayoutParams layParValue = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, view.getLayoutParams().height);
                    layParValue.setMargins(100, 30, 100, 0);
                    layParValue.gravity = Gravity.CENTER_HORIZONTAL;
                    //CustomShape.makeRoundCorner(ContextCompat.getColor(mContext, android.R.color.transparent), 7, view, 2, appCMSPresenter.getButtonBorderColor());
                    view.setLayoutParams(layParValue);
                }
                return new ViewHolder(view);
            }

        } else {
            TextView termsView = new TextView(mContext);
            termsView.setLayoutParams(new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.WRAP_CONTENT));
            termsView.setTextColor(appCMSPresenter.getGeneralTextColor());
            termsView.setTextSize(14f);
            termsView.setPadding(5, 35, 5, 15);
            termsView.setGravity(Gravity.CENTER);
            termsView.setText(localisedStrings.getTnCext());
            termsView.setLinkTextColor(ContextCompat.getColor(mContext, android.R.color.white));
            ClickableSpan tosClick = new ClickableSpan() {
                @Override
                public void onClick(@Nonnull View view) {
                    appCMSPresenter.navigatToTOSPage(null, null);
                }
            };
            ClickableSpan privacyClick = new ClickableSpan() {
                @Override
                public void onClick(@Nonnull View view) {
                    appCMSPresenter.navigateToPrivacyPolicy(null, null);
                }
            };

            appCMSPresenter.makeTextViewLinks(termsView, new String[]{
                    localisedStrings.getTermsOfUsesText(), localisedStrings.getPrivacyPolicyText()}, new ClickableSpan[]{tosClick, privacyClick}, true);
            FrameLayout.LayoutParams layPar = (FrameLayout.LayoutParams) termsView.getLayoutParams();
            layPar.setMargins(0, 0, 0, 8);
            termsView.setLayoutParams(layPar);
            return new ViewHolder(termsView);
        }
    }


    private TextView setSinglePlan(TextView textView) {
        double recurringPaymentAmount = adapterData.get(0).getPlanDetails()
                .get(0).getRecurringPaymentAmount();
        String formattedRecurringPaymentAmount = mContext.getString(R.string.cost_with_fraction,
                recurringPaymentAmount);
        if (recurringPaymentAmount - (int) recurringPaymentAmount == 0) {
            formattedRecurringPaymentAmount = mContext.getString(R.string.cost_without_fraction,
                    recurringPaymentAmount);
        }
        Currency currency = null;
        if (adapterData.get(0).getPlanDetails() != null &&
                !adapterData.get(0).getPlanDetails().isEmpty() &&
                adapterData.get(0).getPlanDetails().get(0) != null &&
                adapterData.get(0).getPlanDetails().get(0).getRecurringPaymentCurrencyCode() != null) {
            try {
                currency = Currency.getInstance(adapterData.get(0).getPlanDetails().get(0).getRecurringPaymentCurrencyCode());
            } catch (Exception e) {
                //Log.e(TAG, "Could not parse locale");
            }
        }
        StringBuilder planAmt = new StringBuilder();
        if (currency != null) {
            if (adapterData.get(0).getPlanDetails().get(0).getRecurringPaymentCurrencyCode().contains("INR"))
                planAmt.append(mContext.getResources().getString(R.string.rupee_symbol));
            else
                planAmt.append(currency.getSymbol());
        }
        planAmt.append(formattedRecurringPaymentAmount);
        StringBuilder planDuration = new StringBuilder();

        if (adapterData.get(0).getRenewalCycleType().contains(mContext.getString(R.string.app_cms_plan_renewal_cycle_type_monthly))) {
            planDuration.append(mContext.getString(R.string.forward_slash));
            if (adapterData.get(0).getRenewalCyclePeriodMultiplier() == 1) {
                planDuration.append(metadataMap.getMonth());
            } else if (adapterData.get(0).getRenewalCyclePeriodMultiplier() > 1) {
                planDuration.append(adapterData.get(0).getRenewalCyclePeriodMultiplier());
                planDuration.append(" ");
                if (adapterData.get(0).getRenewalCyclePeriodMultiplier() > 1) {
                    planDuration.append(metadataMap.getMonths());
                } else {
                    planDuration.append(metadataMap.getMonth());
                }
            }
        }
        if (adapterData.get(0).getRenewalCycleType().contains(mContext.getString(R.string.app_cms_plan_renewal_cycle_type_yearly))) {
            planDuration.append(mContext.getString(R.string.forward_slash));
            if (adapterData.get(0).getRenewalCyclePeriodMultiplier() > 1) {
                planDuration.append(adapterData.get(0).getRenewalCyclePeriodMultiplier());
                planDuration.append(" ");
                planDuration.append(metadataMap.getYears());
            } else {
                planDuration.append(metadataMap.getYear());
            }
        }
        if (adapterData.get(0).getRenewalCycleType().contains(mContext.getString(R.string.app_cms_plan_renewal_cycle_type_daily))) {
            planDuration.append(mContext.getString(R.string.forward_slash));
            if (adapterData.get(0).getRenewalCyclePeriodMultiplier() == 1) {
                planDuration.append(metadataMap.getDay());
            } else {
                planDuration.append(adapterData.get(0).getRenewalCyclePeriodMultiplier());
                planDuration.append(" ");
                if (adapterData.get(0).getRenewalCyclePeriodMultiplier() > 1) {
                    planDuration.append(metadataMap.getDays());
                } else {
                    planDuration.append(metadataMap.getDay());
                }
            }
        }
        planDuration.append("*");
        StringBuilder plan = new StringBuilder();
        plan.append(planAmt.toString());
        plan.append(planDuration.toString());
        Spannable text = new SpannableString(plan.toString());
        float durationFont = 2.1f;
        float priceFont = 3.0f;
        if (BaseView.isTablet(mContext)) {
            durationFont = 3.0f;
            priceFont = 4.0f;
        }
        text.setSpan(new RelativeSizeSpan(priceFont), 0, planAmt.toString().length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        text.setSpan(new ForegroundColorSpan(ContextCompat.getColor(mContext, R.color.splashbackgroundColor)), 0, planAmt.toString().length()/* + 1*/,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        text.setSpan(new RelativeSizeSpan(durationFont), planAmt.toString().length()/* + 1*/, plan.toString().length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        textView.setText(text, TextView.BufferType.SPANNABLE);
        return textView;
    }


    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        if (0 <= position && position < adapterData.size()) {
            createView(holder, position);
        }
    }

    private void createView(ViewHolder holder, final int position) {
        if (0 <= position && holder.constraintComponentView != null) {
            for (int i = 0; i < holder.constraintComponentView.getNumberOfChildren(); i++) {
                if (holder.constraintComponentView.getChild(i) instanceof TextView) {
                    ((TextView) holder.constraintComponentView.getChild(i)).setText("");
                }
            }

            bindView(holder.constraintComponentView, adapterData.get(position), position);
            holder.constraintComponentView.setSelectable(true);
        } else if (0 <= position && holder.componentView != null) {
            for (int i = 0; i < holder.componentView.getNumberOfChildren(); i++) {
                if (holder.componentView.getChild(i) instanceof TextView) {
                    ((TextView) holder.componentView.getChild(i)).setText("");
                }
            }

            bindView(holder.componentView, adapterData.get(position), position);
            holder.componentView.setSelectable(true);
        }
    }

    private boolean useRoundedCorners() {
        return mContext.getString(R.string.app_cms_page_subscription_selectionplan_02_key).equals(componentViewType);
    }

    private void applyBgColorToChildren(ViewGroup viewGroup, int bgColor) {

        for (int i = 0; i < viewGroup.getChildCount(); i++) {
            View child = viewGroup.getChildAt(i);
            if (child instanceof ViewGroup) {
                if (child instanceof CardView) {
                    ((CardView) child).setUseCompatPadding(true);
                    ((CardView) child).setPreventCornerOverlap(false);
                    ((CardView) child).setCardBackgroundColor(bgColor);
                } else {
                    child.setBackgroundColor(bgColor);
                }
                applyBgColorToChildren((ViewGroup) child, bgColor);
            }
        }
    }


    @Override
    public int getItemCount() {

        if (appCMSPresenter.isMOTVApp()) {
            if (adapterData != null && adapterData.size() != 0) {
                return adapterData.size() + 1;
            }

        } else if (adapterData != null && adapterData.size() != 0) {
            return adapterData.size();
        }
        return 0;
    }

    @Override
    public int getItemViewType(int position) {
        if (/*position == 1 && adapterData.size() == 1 &&*/ uiBlockName == AppCMSUIKeyType.UI_BLOCK_SELECTPLAN_03) {
            return TYPE_ITEM;
        }
        if (position < adapterData.size()) {
            return TYPE_ITEM;
        } else {
            return TYPE_FOOTER;
        }

    }

    @Override
    public void resetData(RecyclerView listView) {
        notifyDataSetChanged();
    }

    @Override
    public void updateData(RecyclerView listView, List<ContentDatum> contentData) {
        listView.setAdapter(null);
        adapterData = null;
        notifyDataSetChanged();
        adapterData = contentData;
        notifyDataSetChanged();
        listView.setAdapter(this);
        listView.invalidate();
        notifyDataSetChanged();
    }

    @SuppressLint("ClickableViewAccessibility")
    void bindView(View itemView,
                  final ContentDatum data, int position) throws IllegalArgumentException {

       /* if (onClickHandler == null) {
            if (viewTypeKey == AppCMSUIKeyType.PAGE_SUBSCRIPTION_SELECTPLAN_02_KEY) {
                onClickHandler = new CollectionGridItemView.OnClickHandler() {
                    @Override
                    public void click(CollectionGridItemView collectionGridItemView,
                                      Component childComponent,
                                      ContentDatum data, int clickPosition) {
                        if (isClickable) {
                            subcriptionPlanClick(collectionGridItemView, data);
                        }
                    }

                    @Override
                    public void play(Component childComponent, ContentDatum data) {
                        // NO-OP - Play is not implemented here
                    }
                };
            }
        }*/


        if (itemView instanceof ConstraintCollectionGridItemView) {

            if (onConstraintClickHandler == null) {
                if (viewTypeKey == AppCMSUIKeyType.PAGE_SUBSCRIPTION_SELECTPLAN_02_KEY) {
                    onConstraintClickHandler = new ConstraintCollectionGridItemView.OnClickHandler() {
                        @Override
                        public void click(ConstraintCollectionGridItemView collectionGridItemView,
                                          Component childComponent,
                                          ContentDatum data, int clickPosition) {
                            if (isClickable) {
                                subcriptionPlanClick(collectionGridItemView, data);
                            }
                        }

                        @Override
                        public void play(Component childComponent, ContentDatum data) {
                            // NO-OP - Play is not implemented here
                        }
                    };
                }
            }
            for (int i = 0; i < ((ConstraintCollectionGridItemView) itemView).getNumberOfChildren(); i++) {
                ((ConstraintCollectionGridItemView) itemView).bindChild(itemView.getContext(),
                        ((ConstraintCollectionGridItemView) itemView).getChild(i),
                        data,
                        jsonValueKeyMap,
                        onConstraintClickHandler,
                        componentViewType,
                        appCMSPresenter.getBrandPrimaryCtaColor(),
                        appCMSPresenter,
                        position, null, blockName, moduleAPI.getMetadataMap());
            }
        } else {

            if (onClickHandler == null) {
                if (viewTypeKey == AppCMSUIKeyType.PAGE_SUBSCRIPTION_SELECTPLAN_02_KEY) {
                    onClickHandler = new CollectionGridItemView.OnClickHandler() {
                        @Override
                        public void click(CollectionGridItemView collectionGridItemView,
                                          Component childComponent,
                                          ContentDatum data, int clickPosition) {
                            if (isClickable) {
                                subcriptionPlanClick(collectionGridItemView, data);
                            }
                        }

                        @Override
                        public void play(Component childComponent, ContentDatum data) {
                            // NO-OP - Play is not implemented here
                        }
                    };
                }
            }
            for (int i = 0; i < ((CollectionGridItemView) itemView).getNumberOfChildren(); i++) {
                ((CollectionGridItemView) itemView).bindChild(itemView.getContext(),
                        ((CollectionGridItemView) itemView).getChild(i),
                        data,
                        jsonValueKeyMap,
                        onClickHandler,
                        componentViewType,
                        appCMSPresenter.getBrandPrimaryCtaColor(),
                        appCMSPresenter,
                        position, null, blockName, moduleAPI.getMetadataMap());
            }
        }
    }

    private void subcriptionPlanClick(View itemView, ContentDatum data) {

        if (itemView instanceof CollectionGridItemView) {
            if (((CollectionGridItemView) itemView).isSelectable()) {
                double price = data.getPlanDetails().get(0).getStrikeThroughPrice();
                if (price == 0) {
                    price = data.getPlanDetails().get(0).getRecurringPaymentAmount();
                }

                double discountedPrice = data.getPlanDetails().get(0).getRecurringPaymentAmount();
                double discountedAmount = data.getPlanDetails().get(0).getDiscountedPrice();

                boolean upgradesAvailable = false;
                for (ContentDatum plan : adapterData) {
                    if (plan != null &&
                            plan.getPlanDetails() != null &&
                            !plan.getPlanDetails().isEmpty() &&
                            ((plan.getPlanDetails().get(0).getStrikeThroughPrice() != 0 &&
                                    price < plan.getPlanDetails().get(0).getStrikeThroughPrice()) ||
                                    (plan.getPlanDetails().get(0).getRecurringPaymentAmount() != 0 &&
                                            price < plan.getPlanDetails().get(0).getRecurringPaymentAmount()))) {
                        upgradesAvailable = true;
                    }
                }
                appCMSPresenter.setSelectedPlan(data.getId(), adapterData);
                appCMSPresenter.initiateSignUpAndSubscription(data.getIdentifier(),
                        data.getId(),
                        data.getPlanDetails().get(0).getCountryCode(),
                        data.getName(),
                        price,
                        discountedPrice,
                        data.getPlanDetails().get(0).getRecurringPaymentCurrencyCode(),
                        data.getPlanDetails().get(0).getCountryCode(),
                        data.getRenewable(),
                        data.getRenewalCycleType(),
                        upgradesAvailable, discountedAmount,
                        data.getPlanDetails().get(0).getAllowedPayMethods(),
                        data.getPlanDetails().get(0).getCarrierBillingProviders());
            } else {
                ((CollectionGridItemView) itemView).performClick();
            }
        } else if (itemView instanceof ConstraintCollectionGridItemView) {
            if (((ConstraintCollectionGridItemView) itemView).isSelectable()) {
                double price = data.getPlanDetails().get(0).getStrikeThroughPrice();
                if (price == 0) {
                    price = data.getPlanDetails().get(0).getRecurringPaymentAmount();
                }

                double discountedPrice = data.getPlanDetails().get(0).getRecurringPaymentAmount();
                double discountedAmount = data.getPlanDetails().get(0).getDiscountedPrice();

                boolean upgradesAvailable = false;
                for (ContentDatum plan : adapterData) {
                    if (plan != null &&
                            plan.getPlanDetails() != null &&
                            !plan.getPlanDetails().isEmpty() &&
                            ((plan.getPlanDetails().get(0).getStrikeThroughPrice() != 0 &&
                                    price < plan.getPlanDetails().get(0).getStrikeThroughPrice()) ||
                                    (plan.getPlanDetails().get(0).getRecurringPaymentAmount() != 0 &&
                                            price < plan.getPlanDetails().get(0).getRecurringPaymentAmount()))) {
                        upgradesAvailable = true;
                    }
                }
                appCMSPresenter.setSelectedPlan(data.getId(), adapterData);
                appCMSPresenter.initiateSignUpAndSubscription(data.getIdentifier(),
                        data.getId(),
                        data.getPlanDetails().get(0).getCountryCode(),
                        data.getName(),
                        price,
                        discountedPrice,
                        data.getPlanDetails().get(0).getRecurringPaymentCurrencyCode(),
                        data.getPlanDetails().get(0).getCountryCode(),
                        data.getRenewable(),
                        data.getRenewalCycleType(),
                        upgradesAvailable, discountedAmount,
                        data.getPlanDetails().get(0).getAllowedPayMethods(),
                        data.getPlanDetails().get(0).getCarrierBillingProviders());
            } else {
                ((ConstraintCollectionGridItemView) itemView).performClick();
            }
        }
    }

    public boolean isClickable() {
        return isClickable;
    }

    @Override
    public void setClickable(boolean clickable) {
        isClickable = clickable;
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {
        CollectionGridItemView componentView;
        ConstraintCollectionGridItemView constraintComponentView;
        TextView singlePlanView;

        public ViewHolder(TextView itemView) {
            super(itemView);
            this.singlePlanView = itemView;
        }

        public ViewHolder(View itemView) {
            super(itemView);
            if (itemView instanceof FrameLayout)
                this.componentView = (CollectionGridItemView) itemView;
            else if (itemView instanceof ConstraintLayout) {
                this.constraintComponentView = (ConstraintCollectionGridItemView) itemView;
            }
        }
    }


}
