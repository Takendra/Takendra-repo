package com.viewlift.views.customviews;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.text.SpannableString;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.viewlift.AppCMSApplication;
import com.viewlift.R;
import com.viewlift.models.data.appcms.api.ContentDatum;
import com.viewlift.models.data.appcms.api.FeatureDetail;
import com.viewlift.models.data.appcms.api.MetadataMap;
import com.viewlift.models.data.appcms.api.Module;
import com.viewlift.models.data.appcms.api.PlanDetail;
import com.viewlift.presenters.AppCMSPresenter;
import com.viewlift.utils.BillingHelper;
import com.viewlift.utils.CommonUtils;
import com.viewlift.views.dialog.CustomShape;

import java.util.ArrayList;
import java.util.Currency;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import javax.inject.Inject;

import rx.functions.Action1;

public class PlanFeaturesLayout extends TableLayout {

    @Inject
    AppCMSPresenter appCMSPresenter;
    int currentSelection = 0;
    Module module;
    int plansToshow = 3;
    int totalPlans = 0;
    int currentPlanCount = 0;
    MetadataMap metadataMap;

    public PlanFeaturesLayout(Context context) {
        super(context);
        ((AppCMSApplication) getContext().getApplicationContext()).getAppCMSPresenterComponent().inject(this);
    }


    public void initializeView(Module module) {
        this.module = module;
        if (module.getMetadataMap() != null)
            metadataMap = module.getMetadataMap();
        removeAllViews();
        if (module.getContentData() != null && module.getContentData().size() != 0) {
            if (BaseView.isTablet(getContext())) {
                if (module.getContentData().size() > 6)
                    plansToshow = 6;
                else
                    plansToshow = module.getContentData().size();
            } else {
                if (module.getContentData().size() > 3)
                    plansToshow = 3;
                else
                    plansToshow = module.getContentData().size();
            }
            currentPlanCount = plansToshow;
            totalPlans = module.getContentData().size();
            createPlanTitleView(module.getContentData());
            createPlanPriceView(module.getContentData());
            createFeaturesView(module.getContentData());

        }
    }


    private void changeSelectedPlan(Module module) {
        removeAllViews();
        createPlanTitleView(module.getContentData());
        createPlanPriceView(module.getContentData());
        createFeaturesView(module.getContentData());

    }

    private void addPreviousPlansView(TableRow row) {
        ImageView previousPlans;
        previousPlans = new ImageView(getContext());
        TableRow.LayoutParams params = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT);
        params.gravity = Gravity.CENTER_VERTICAL;
        previousPlans.setLayoutParams(params);
        row.addView(previousPlans);
        previousPlans.setImageResource(R.drawable.left_arrow);
        previousPlans.setColorFilter(Color.parseColor(appCMSPresenter.getAppTextColor()), android.graphics.PorterDuff.Mode.SRC_IN);
        previousPlans.setOnClickListener(new PreviousPlanClickListener());
        if (currentPlanCount <= 3)
            previousPlans.setVisibility(INVISIBLE);
        if (BaseView.isTablet(getContext()) && currentPlanCount <= 6)
            previousPlans.setVisibility(INVISIBLE);
    }

    private void addNextPlansView(TableRow row) {
        ImageView nextPlans;
        nextPlans = new ImageView(getContext());
        TableRow.LayoutParams params = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT);
        params.gravity = Gravity.CENTER_VERTICAL;
        nextPlans.setLayoutParams(params);
        row.addView(nextPlans);
        nextPlans.setImageResource(R.drawable.right_arrow);
        nextPlans.setColorFilter(Color.parseColor(appCMSPresenter.getAppTextColor()), android.graphics.PorterDuff.Mode.SRC_IN);
        nextPlans.setOnClickListener(new NextPlanClickListener());
        if (currentPlanCount == totalPlans)
            nextPlans.setVisibility(INVISIBLE);
    }

    private void createPlanTitleView(List<ContentDatum> contentData) {
        TableRow tableRow = new TableRow(getContext());
        TableLayout.LayoutParams rowParams = new TableLayout.LayoutParams(TableLayout.LayoutParams.WRAP_CONTENT, TableLayout.LayoutParams.WRAP_CONTENT);
        tableRow.setLayoutParams(rowParams);
        addPreviousPlansView(tableRow);
        TextView[] planTitle = new TextView[plansToshow];
        int planPos = 0;
        for (int i = currentPlanCount - plansToshow; i < currentPlanCount; i++) {
            planTitle[planPos] = new TextView(getContext());
            TableRow.LayoutParams params = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, 120);
            if (planPos < plansToshow - 1)
                params.setMargins(0, 0, 40, 0);
            else
                params.setMargins(0, 0, 0, 0);
            params.weight = 1;
            planTitle[planPos].setLayoutParams(params);
//            planTitle[planPos].setSingleLine(true);
            planTitle[planPos].setMaxLines(1);
            planTitle[planPos].setPadding(5, 0, 5, 0);
            planTitle[planPos].setGravity(Gravity.CENTER);
            String title = fetchPlanTitle(contentData.get(i));
            planTitle[planPos].setText(title);
            if (title.length() > 20 && plansToshow > 1)
                planTitle[planPos].setText(title.substring(0, 20));
            planTitle[planPos].setOnClickListener(new PlanClickListener(planPos, contentData.get(i).getId()));
            if (currentSelection == planPos) {
                planTitle[planPos].setBackground(CustomShape.createRoundedRectangleDrawable(appCMSPresenter.getBrandPrimaryCtaColor()));
                planTitle[planPos].setTextColor(Color.parseColor(appCMSPresenter.getAppCtaTextColor()));
                currentSelectedPlan(contentData.get(i).getId());
            } else {
                planTitle[planPos].setBackground(CustomShape.createRoundedRectangleDrawable(appCMSPresenter.getBrandPrimaryCtaColor() + 0x92000000));
                planTitle[planPos].setTextColor(appCMSPresenter.getBrandPrimaryCtaColor() + 0x92000000);
            }
            tableRow.addView(planTitle[planPos]);
            planPos++;
        }
        addNextPlansView(tableRow);
        addView(tableRow);
    }


    private void createPlanPriceView(List<ContentDatum> contentData) {
        TextView textView = new TextView(getContext());
        TableLayout.LayoutParams params = new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.WRAP_CONTENT);
        params.setMargins(0, 70, 0, 0);
        textView.setLayoutParams(params);
        textView.setGravity(Gravity.CENTER);
        if (module.getMetadataMap() != null && module.getMetadataMap().getPriceTrialEnd() != null)
            textView.setText(module.getMetadataMap().getPriceTrialEnd());
        else textView.setText("Price after free trial ends");
        textView.setTextColor(Color.parseColor(appCMSPresenter.getAppTextColor()));
        addView(textView);

        TableRow tableRow2 = new TableRow(getContext());
        addEmptyViewLeft(tableRow2);
        TableLayout.LayoutParams rowParams = new TableLayout.LayoutParams(TableLayout.LayoutParams.WRAP_CONTENT, TableLayout.LayoutParams.WRAP_CONTENT);
        rowParams.setMargins(0, 20, 0, 0);
        tableRow2.setLayoutParams(rowParams);
        TextView[] planPriceView = new TextView[plansToshow];
        int planPos = 0;
        for (int i = currentPlanCount - plansToshow; i < currentPlanCount; i++) {
            planPriceView[planPos] = new TextView(getContext());
            TableRow.LayoutParams params2 = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT);
            params2.setMargins(0, 0, 40, 0);
            params2.weight = 1;
            planPriceView[planPos].setLayoutParams(params2);
            planPriceView[planPos].setGravity(Gravity.CENTER);
            planPriceView[planPos].setTypeface(null, Typeface.BOLD);
            int finalPlanPos = planPos;
            fetchPlanPrice(contentData.get(i), new Action1<String>() {
                @Override
                public void call(String price) {
                    planPriceView[finalPlanPos].setText(price);
                }
            });
            if (currentSelection == planPos)
                planPriceView[planPos].setTextColor(Color.parseColor(appCMSPresenter.getAppTextColor()));
            else
                planPriceView[planPos].setTextColor(appCMSPresenter.getBrandPrimaryCtaColor() + 0x92000000);
            tableRow2.addView(planPriceView[planPos]);
            planPos++;
        }
        addEmptyViewRight(tableRow2);
        addView(tableRow2);
        addRowSepartor();
    }

    private String fetchPlanTitle(ContentDatum data) {
        String title = null;
        if (data != null) {
            int planIndex = 0;
            for (int i = 0; i < data.getPlanDetails().size(); i++) {
                if (data.getPlanDetails().get(i) != null &&
                        data.getPlanDetails().get(i).isDefault()) {
                    planIndex = i;
                }
            }
            title = data.getPlanDetails()
                    .get(planIndex).getTitle();
        }
        if (title == null)
            title = data.getName();
        return title;
    }

    private void fetchPlanPrice(ContentDatum data, Action1<String> planPrice) {
        BillingHelper.getInstance(appCMSPresenter).getSubsSKUDetail(data.getIdentifier(), skuDetails -> {
            int planIndex = 0;
            for (int i = 0; i < data.getPlanDetails().size(); i++) {
                if (data.getPlanDetails().get(i) != null &&
                        data.getPlanDetails().get(i).isDefault()) {
                    planIndex = i;
                }
            }
            Currency currency = null;
            if (data.getPlanDetails() != null &&
                    !data.getPlanDetails().isEmpty() &&
                    data.getPlanDetails().get(planIndex) != null &&
                    data.getPlanDetails().get(planIndex).getRecurringPaymentCurrencyCode() != null) {
                try {
                    currency = Currency.getInstance(data.getPlanDetails().get(planIndex).getRecurringPaymentCurrencyCode());
                } catch (Exception ignored) {
                }
            }
            double recurringPaymentAmount = data.getPlanDetails()
                    .get(planIndex).getRecurringPaymentAmount();
            StringBuilder planAmt = new StringBuilder();


//            if (skuDetails != null) {
//                planAmt.append(skuDetails.getPrice());
//                CommonUtils.planViewPriceList.put(data.getIdentifier(), new SpannableString(planAmt.toString()));
//                CommonUtils.getAvailableCurrencies(skuDetails.getPriceCurrencyCode());
//            } else {
                if (currency != null) {
                    planAmt.append(currency.getSymbol());
                }
                planAmt.append(recurringPaymentAmount);
//            }
            StringBuilder planDuration = new StringBuilder();
            if (data.getRenewalCycleType() != null) {
                if (data.getRenewalCycleType().contains(getContext().getString(R.string.app_cms_plan_renewal_cycle_type_monthly))) {
                    planDuration.append(" ");
                    planDuration.append(getContext().getString(R.string.forward_slash));
                    planDuration.append(" ");
                    if (data.getRenewalCyclePeriodMultiplier() == 1) {
                        if (metadataMap.getMonth() != null)
                            planDuration.append(metadataMap.getMonth());
                        else
                            planDuration.append(getContext().getString(R.string.plan_type_month));
                    } else {
                        planDuration.append(data.getRenewalCyclePeriodMultiplier());
                        planDuration.append(" ");
                        if (data.getRenewalCyclePeriodMultiplier() > 1) {
                            if (metadataMap.getMonth() != null)
                                planDuration.append(metadataMap.getMonths());
                            else
                                planDuration.append(getContext().getString(R.string.plan_type_months));
                        } else {
                            if (metadataMap.getMonth() != null)
                                planDuration.append(metadataMap.getMonth());
                            else
                                planDuration.append(getContext().getString(R.string.plan_type_month));
                        }
                    }
                }
                if (data.getRenewalCycleType().contains(getContext().getString(R.string.app_cms_plan_renewal_cycle_type_yearly))) {
                    planDuration.append(" ");
                    planDuration.append(getContext().getString(R.string.forward_slash));
                    planDuration.append(" ");
                    if (data.getRenewalCyclePeriodMultiplier() == 1) {
                        if (metadataMap != null && metadataMap.getYear() != null)
                            planDuration.append(metadataMap.getYear());
                        else
                            planDuration.append(getContext().getString(R.string.plan_type_year));
                    } else {
                        planDuration.append(data.getRenewalCyclePeriodMultiplier());
                        planDuration.append(" ");
                        if (data.getRenewalCyclePeriodMultiplier() > 1) {
                            if (metadataMap != null && metadataMap.getYear() != null)
                                planDuration.append(metadataMap.getYear());
                            else
                                planDuration.append(getContext().getString(R.string.plan_type_years));
                        } else {
                            if (metadataMap != null && metadataMap.getYear() != null)
                                planDuration.append(metadataMap.getYear());
                            else
                                planDuration.append(getContext().getString(R.string.plan_type_year));
                        }
                    }
                }
                if (data.getRenewalCycleType().contains(getContext().getString(R.string.app_cms_plan_renewal_cycle_type_daily))) {
                    planDuration.append(" ");
                    planDuration.append(getContext().getString(R.string.forward_slash));
                    planDuration.append(" ");
                    if (data.getRenewalCyclePeriodMultiplier() == 1) {
                        if (metadataMap != null && metadataMap.getDays() != null)
                            planDuration.append(metadataMap.getDay());
                        else
                            planDuration.append(getContext().getString(R.string.plan_type_day));
                    } else {
                        planDuration.append(data.getRenewalCyclePeriodMultiplier());
                        planDuration.append(" ");
                        if (data.getRenewalCyclePeriodMultiplier() > 1) {
                            if (metadataMap != null && metadataMap.getDays() != null)
                                planDuration.append(metadataMap.getDays());
                            else
                                planDuration.append(getContext().getString(R.string.plan_type_days));
                        } else {
                            if (metadataMap != null && metadataMap.getDays() != null)
                                planDuration.append(metadataMap.getDay());
                            else
                                planDuration.append(getContext().getString(R.string.plan_type_day));
                        }
                    }
                }
            }
            StringBuilder plan = new StringBuilder();
            plan.append(planAmt.toString());
            plan.append(planDuration.toString());
            planPrice.call(plan.toString());

        });
    }

    private void createFeaturesView(List<ContentDatum> contentData) {
        List<String> features = getAllFeatures(contentData);
        if (features.size() != 0) {
            for (int i = 0; i < features.size(); i++) {
                addFeatureText(features.get(i));
                TableRow tableRow2 = new TableRow(getContext());
                TableLayout.LayoutParams rowParams = new TableLayout.LayoutParams(TableLayout.LayoutParams.WRAP_CONTENT, TableLayout.LayoutParams.WRAP_CONTENT);
                rowParams.setMargins(0, 20, 0, 0);
                tableRow2.setLayoutParams(rowParams);
                addEmptyViewLeft(tableRow2);
                ImageView[] featureImage = new ImageView[plansToshow];
                int img = 0;
                for (int k = currentPlanCount - plansToshow; k < currentPlanCount; k++) {
                    featureImage[img] = new ImageView(getContext());
                    TableRow.LayoutParams params2 = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT);
                    params2.setMargins(0, 0, 40, 0);
                    params2.weight = 1;
                    featureImage[img].setLayoutParams(params2);

                    if (checkFeatureExist(features.get(i), contentData.get(k)))
                        featureImage[img].setImageResource(R.drawable.ic_check);
                    else
                        featureImage[img].setImageResource(R.drawable.ic_clear);
                    if (currentSelection == img)
                        featureImage[img].setColorFilter(Color.parseColor(appCMSPresenter.getAppTextColor()), android.graphics.PorterDuff.Mode.SRC_IN);
                    else
                        featureImage[img].setColorFilter(ContextCompat.getColor(getContext(), android.R.color.darker_gray), android.graphics.PorterDuff.Mode.SRC_IN);
                    tableRow2.addView(featureImage[img]);
                    img++;
                }
                addEmptyViewRight(tableRow2);
                addView(tableRow2);
                addRowSepartor();
            }
        }
    }

    private void addEmptyViewLeft(TableRow row) {
        ImageView imageView = new ImageView(getContext());
        imageView.setImageResource(R.drawable.left_arrow);
        imageView.setVisibility(INVISIBLE);
        row.addView(imageView);
    }

    private void addEmptyViewRight(TableRow row) {
        ImageView imageView = new ImageView(getContext());
        imageView.setImageResource(R.drawable.right_arrow);
        imageView.setVisibility(INVISIBLE);
        row.addView(imageView);
    }

    private boolean checkFeatureExist(String feature, ContentDatum contentDatum) {
        if (contentDatum.getPlanDetails() != null &&
                !contentDatum.getPlanDetails().isEmpty()) {
            List<PlanDetail> planDetail = contentDatum.getPlanDetails();
            for (int j = 0; j < planDetail.size(); j++) {
                if (planDetail.get(j).getFeatureDetails() != null &&
                        !planDetail.get(j).getFeatureDetails().isEmpty()) {
                    List<FeatureDetail> details = planDetail.get(j).getFeatureDetails();
                    for (int k = 0; k < details.size(); k++) {
                        if (details.get(k).getTextToDisplay().toLowerCase().equalsIgnoreCase(feature.toLowerCase()))
                            return true;
                    }
                }
            }
        }
        return false;
    }

    private List<String> getAllFeatures(List<ContentDatum> contentData) {
        List<String> features = new ArrayList<>();
        for (int i = 0; i < contentData.size(); i++) {
            if (contentData.get(i).getPlanDetails() != null &&
                    !contentData.get(i).getPlanDetails().isEmpty()) {
                List<PlanDetail> planDetail = contentData.get(i).getPlanDetails();
                for (int j = 0; j < planDetail.size(); j++) {
                    if (planDetail.get(j).getFeatureDetails() != null &&
                            !planDetail.get(j).getFeatureDetails().isEmpty()) {
                        List<FeatureDetail> details = planDetail.get(j).getFeatureDetails();
                        for (int k = 0; k < details.size(); k++) {
                            features.add(details.get(k).getTextToDisplay());
                        }
                    }
                }
            }
        }
        Set<String> removeDuplicate = new LinkedHashSet<String>(features);
        features.clear();
        features.addAll(removeDuplicate);
        return features;
    }

    private void addFeatureText(String feature) {
        TextView textView = new TextView(getContext());
        TableLayout.LayoutParams params = new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.WRAP_CONTENT);
        params.setMargins(0, 40, 0, 0);
        textView.setLayoutParams(params);
        textView.setGravity(Gravity.CENTER);
        textView.setText(feature);
        textView.setTextColor(Color.parseColor(appCMSPresenter.getAppTextColor()));
        addView(textView);
    }


    private void addRowSepartor() {
        View view = new View(getContext());
        TableLayout.LayoutParams params = new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, 2);
        params.setMargins(0, 40, 0, 0);
        view.setLayoutParams(params);
        view.setBackgroundColor(ContextCompat.getColor(getContext(), android.R.color.darker_gray));
        addView(view);
    }

    class PlanClickListener implements OnClickListener {
        int buttonId;
        String planId;

        PlanClickListener(int id, String planId) {
            buttonId = id;
            this.planId = planId;
        }


        @Override
        public void onClick(View v) {
            currentSelection = buttonId;
            changeSelectedPlan(module);
            currentSelectedPlan(planId);
        }
    }


    class PreviousPlanClickListener implements OnClickListener {

        PreviousPlanClickListener() {
        }


        @Override
        public void onClick(View v) {
            currentSelection = 0;
            if (BaseView.isTablet(getContext())) {
                if (currentPlanCount % 6 == 0) {
                    currentPlanCount = currentPlanCount - 6;
                } else if (currentPlanCount % 6 == 5) {
                    currentPlanCount = currentPlanCount - 5;
                } else if (currentPlanCount % 6 == 4) {
                    currentPlanCount = currentPlanCount - 4;
                } else if (currentPlanCount % 6 == 3) {
                    currentPlanCount = currentPlanCount - 3;
                } else if (currentPlanCount % 6 == 2) {
                    currentPlanCount = currentPlanCount - 2;
                } else if (currentPlanCount % 6 == 1) {
                    currentPlanCount = currentPlanCount - 1;
                }
            } else {
                plansToshow = 3;
                if (currentPlanCount % 3 == 0) {
                    currentPlanCount = currentPlanCount - 3;
                } else if (currentPlanCount % 3 == 1) {
                    currentPlanCount = currentPlanCount - 1;
                } else if (currentPlanCount % 3 == 2) {
                    currentPlanCount = currentPlanCount - 2;
                }
            }
            changeSelectedPlan(module);
        }

    }

    class NextPlanClickListener implements OnClickListener {

        NextPlanClickListener() {
        }


        @Override
        public void onClick(View v) {
            currentSelection = 0;
            if (BaseView.isTablet(getContext())) {
                int plansLeft = totalPlans - currentPlanCount;
                if (plansLeft >= 6) {
                    plansToshow = 6;
                    currentPlanCount = currentPlanCount + plansToshow;
                } else if (plansLeft == 5) {
                    plansToshow = 5;
                    currentPlanCount = currentPlanCount + plansToshow;
                } else if (plansLeft == 4) {
                    plansToshow = 4;
                    currentPlanCount = currentPlanCount + plansToshow;
                } else if (plansLeft == 3) {
                    plansToshow = 3;
                    currentPlanCount = currentPlanCount + plansToshow;
                } else if (plansLeft == 2) {
                    plansToshow = 2;
                    currentPlanCount = currentPlanCount + plansToshow;
                } else if (plansLeft == 1) {
                    plansToshow = 1;
                    currentPlanCount = currentPlanCount + plansToshow;
                }
            } else {
                int plansLeft = totalPlans - currentPlanCount;
                if (plansLeft >= 3) {
                    plansToshow = 3;
                    currentPlanCount = currentPlanCount + plansToshow;
                } else if (plansLeft == 2) {
                    plansToshow = 2;
                    currentPlanCount = currentPlanCount + plansToshow;
                } else if (plansLeft == 1) {
                    plansToshow = 1;
                    currentPlanCount = currentPlanCount + plansToshow;
                }
            }
            changeSelectedPlan(module);
        }
    }

    void currentSelectedPlan(String planId) {
        appCMSPresenter.setSelectedPlan(planId, module.getContentData());
    }
}
