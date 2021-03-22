package com.viewlift.tv.views.customviews;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import com.viewlift.R;
import com.viewlift.models.data.appcms.api.ContentDatum;
import com.viewlift.models.data.appcms.api.FeatureDetail;
import com.viewlift.models.data.appcms.api.Module;
import com.viewlift.models.data.appcms.api.PlanDetail;
import com.viewlift.models.data.appcms.ui.page.Component;
import com.viewlift.presenters.AppCMSPresenter;
import com.viewlift.tv.utility.Utils;
import com.viewlift.tv.views.activity.AppCmsHomeActivity;
import com.viewlift.utils.CommonUtils;

import java.util.ArrayList;
import java.util.Currency;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import androidx.core.content.ContextCompat;

import static com.viewlift.utils.CommonUtils.getColor;

public class TVSubscriptionPlanFeaturesLayout extends TableLayout {

    private Context mContext;
    private AppCMSPresenter appCMSPresenter;
    private Module module;
    private int currentSelection = 0;
    int plansToShow = 3;
    int totalPlans = 0;
    int currentPlanCount = 0;
    //TableLayout tableLayout;
    private ScrollView scrollView;
    private TextView[] planTitle;
    private String freeTrailLabel;

    public TVSubscriptionPlanFeaturesLayout(Context context,
                                            Module module,
                                            AppCMSPresenter appCMSPresenter) {
        super(context);
        this.mContext = context;
        this.module = module;
        this.appCMSPresenter = appCMSPresenter;
        initializeView(module,appCMSPresenter);
    }


    public void initializeView(Module module,AppCMSPresenter appCMSPresenter) {
        this.module = module;
        this.appCMSPresenter = appCMSPresenter;

        //setStretchAllColumns(false);
        if (module.getContentData() != null && module.getContentData().size() != 0) {
            if (module.getContentData().size() > 3)
                plansToShow = 3;
            else
                plansToShow = module.getContentData().size();
            currentPlanCount = plansToShow;
            totalPlans = module.getContentData().size();

            createPlanTitleView(module.getContentData());
            //tableLayout = new TableLayout(getContext());

            //TableLayout.LayoutParams params = new TableLayout.LayoutParams(1100, 600);
            //params.setMargins(0,20,0,0);
            //tableLayout.setLayoutParams(params);
            createPlanPriceView(module.getContentData());
            createFeaturesView(module.getContentData());
            //addView(tableLayout,1);
        }
    }

    private void changeSelectedPlan(Module module) {
        removeAllViews();
        createPlanTitleView(module.getContentData());
        //tableLayout = new TableLayout(getContext());
        //TableLayout.LayoutParams params = new TableLayout.LayoutParams(1100, 600);
        //params.setMargins(0,20,0,0);
        //tableLayout.setLayoutParams(params);
        createPlanPriceView(module.getContentData());
        createFeaturesView(module.getContentData());
        //addView(tableLayout,1);
        if(planTitle[currentSelection] != null) planTitle[currentSelection].requestFocus();
    }

    private void addPreviousPlansView(TableRow row) {
        ImageView previousPlans;
        if (totalPlans > 3) {
            previousPlans = new ImageView(getContext());
            previousPlans.setFocusable(true);
            TableRow.LayoutParams params = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT);
            params.gravity = Gravity.CENTER_VERTICAL;
            previousPlans.setLayoutParams(params);
            row.addView(previousPlans);
            Drawable leftArrow = ContextCompat.getDrawable(mContext, R.drawable.left_arrow);
            previousPlans.setImageDrawable(leftArrow);
            Utils.setImageColorFilter(previousPlans
                    ,leftArrow,appCMSPresenter);
            previousPlans.setOnClickListener(new PreviousPlanClickListener());
            if (currentPlanCount <= 3)
                previousPlans.setVisibility(INVISIBLE);
        }
    }

    private void addPlanHeader(TableRow row,String text,String colorCode,int textSize) {
        TextView textView = new TextView(getContext());
        TableRow.LayoutParams params = new TableRow.LayoutParams(0, 55,3);
        params.gravity = Gravity.CENTER_VERTICAL;
        textView.setLayoutParams(params);
        textView.setGravity(Gravity.CENTER_VERTICAL);
        row.addView(textView);
        textView.setText(text);
        textView.setTextSize(textSize);
        textView.setMaxLines(1);
        textView.setEllipsize(TextUtils.TruncateAt.END);
        textView.setTextColor(Color.parseColor(colorCode));
    }

    private void addHeader(TableRow row,String text,String colorCode,int textSize) {
        TextView textView = new TextView(getContext());
        TableRow.LayoutParams params = new TableRow.LayoutParams(0, 55,3);
        params.gravity = Gravity.CENTER_VERTICAL;
        textView.setLayoutParams(params);
        textView.setGravity(Gravity.CENTER_VERTICAL);
        row.addView(textView);
        textView.setText(text);
        textView.setTextSize(textSize);
        textView.setTypeface(null, Typeface.BOLD);
        textView.setMaxLines(1);
        textView.setEllipsize(TextUtils.TruncateAt.END);
        textView.setTextColor(Color.parseColor(colorCode));
    }

    private void addNextPlansView(TableRow row) {
        ImageView nextPlans;
        if (totalPlans > 3) {
            nextPlans = new ImageView(getContext());
            nextPlans.setFocusable(true);
            TableRow.LayoutParams params = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT);
            params.gravity = Gravity.CENTER_VERTICAL;
            nextPlans.setLayoutParams(params);
            row.addView(nextPlans);
            nextPlans.setImageResource(R.drawable.right_arrow);
            Drawable rightArrow = ContextCompat.getDrawable(mContext, R.drawable.right_arrow);
            Utils.setImageColorFilter(nextPlans
                    ,rightArrow,appCMSPresenter);
            nextPlans.setOnClickListener(new NextPlanClickListener());
            if (currentPlanCount == totalPlans)
                nextPlans.setVisibility(INVISIBLE);
        }
    }

    private void createPlanTitleView(List<ContentDatum> contentData) {
        TableRow tableRow = new TableRow(getContext());
        String choosePlanLabel =mContext.getResources().getString(R.string.choose_a_plan);
        if (module != null && module.getTitle() != null){
            choosePlanLabel = module.getTitle();
        }
        addHeader(tableRow,choosePlanLabel,appCMSPresenter.getAppTextColor(),20);
        addPreviousPlansView(tableRow);
        planTitle = new TextView[plansToShow];
        int planPos = 0;
        for (int i = currentPlanCount - plansToShow; i < currentPlanCount; i++) {
            planTitle[planPos] = new TextView(getContext());
            planTitle[planPos].setFocusable(true);
            TableRow.LayoutParams params = new TableRow.LayoutParams(0, 60,1);
            if (planPos < plansToShow - 1)
                params.setMargins(0, 0, 40, 0);
            else
                params.setMargins(0, 0, 0, 0);
            planTitle[planPos].setPadding(5,5,5,5);
            planTitle[planPos].setLayoutParams(params);
            planTitle[planPos].setMaxLines(1);
            planTitle[planPos].setEllipsize(TextUtils.TruncateAt.END);
            planTitle[planPos].setTextSize(10);
            planTitle[planPos].setGravity(Gravity.CENTER);
            //planTitle[planPos].setWidth(58);
            planTitle[planPos].setText(fetchPlanTitle(contentData.get(i)));
            planTitle[planPos].setOnFocusChangeListener(new TVSubscriptionPlanFeaturesLayout.PlanCFocusChangeListener(planPos));
            planTitle[planPos].setOnClickListener(new TVSubscriptionPlanFeaturesLayout.PlanClickListener(planPos,contentData.get(i)));
            Component component = new Component();
            component.setCornerRadius(4);
            component.setBorderColor(CommonUtils.getColor(mContext, Integer.toHexString(ContextCompat.getColor(mContext,
                    R.color.btn_color_with_opacity))));
            component.setBorderWidth(4);
            planTitle[planPos].setBackground(Utils.setButtonBackgroundSelector(mContext,
                    Color.parseColor(Utils.getFocusColor(mContext, appCMSPresenter)),
                    component,
                    appCMSPresenter));
            planTitle[planPos].setTextColor(Utils.getButtonTextColorDrawable(
                    getColor(mContext, component.getBorderColor()),
                    getColor(mContext, component.getTextColor()), appCMSPresenter));
            tableRow.addView(planTitle[planPos]);
            planPos++;
        }
        //addEmptyTextView(tableRow);
        addNextPlansView(tableRow);
        addView(tableRow);
    }


    private void createPlanPriceView(List<ContentDatum> contentData) {
        TableRow tableRow2 = new TableRow(getContext());
        TableLayout.LayoutParams params = new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.WRAP_CONTENT);
        params.setMargins(0, 20, 0, 0);
        tableRow2.setLayoutParams(params);
        if (module.getMetadataMap() != null && module.getMetadataMap().getPriceTrialEnd() != null)
            freeTrailLabel = module.getMetadataMap().getPriceTrialEnd();
        else freeTrailLabel = mContext.getResources().getString(R.string.price_after_free_trail_ends);

        addPlanHeader(tableRow2,freeTrailLabel,appCMSPresenter.getAppTextColor(),10);
        addEmptyImageView(tableRow2);
        TextView[] planPriceView = new TextView[plansToShow];
        int planPos = 0;
        for (int i = currentPlanCount - plansToShow; i < currentPlanCount; i++) {
            planPriceView[planPos] = new TextView(getContext());
            TableRow.LayoutParams params2 = new TableRow.LayoutParams(0, 55,1);
            if (planPos < plansToShow - 1)
                params2.setMargins(0, 0, 40, 0);
            planPriceView[planPos].setLayoutParams(params2);
            planPriceView[planPos].setGravity(Gravity.CENTER);
            planPriceView[planPos].setTypeface(null, Typeface.BOLD);
            planPriceView[planPos].setMaxLines(1);
            planPriceView[planPos].setTextSize(9);
            planPriceView[planPos].setEllipsize(TextUtils.TruncateAt.END);
            planPriceView[planPos].setText(fetchPlanPrice(contentData.get(i)));
            if (currentSelection == planPos)
                planPriceView[planPos].setTextColor(Color.parseColor(appCMSPresenter.getAppTextColor()));
            else
                planPriceView[planPos].setTextColor(ContextCompat.getColor(getContext(), android.R.color.darker_gray));
            tableRow2.addView(planPriceView[planPos]);
            planPos++;
        }

        //addEmptyView(tableRow2);
        addEmptyImageView(tableRow2);
        addView(tableRow2);
        addRowSeparator();
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

    private String fetchPlanPrice(ContentDatum data) {
        StringBuilder plan = new StringBuilder();
        if (data != null) {
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
                } catch (Exception e) {
                    //Log.e(TAG, "Could not parse locale");
                }
            }

            double recurringPaymentAmount = data.getPlanDetails()
                    .get(planIndex).getRecurringPaymentAmount();
            String formattedRecurringPaymentAmount = getContext().getString(R.string.cost_with_fraction,
                    recurringPaymentAmount);
            if (recurringPaymentAmount - (int) recurringPaymentAmount == 0) {
                formattedRecurringPaymentAmount = getContext().getString(R.string.cost_without_fraction,
                        recurringPaymentAmount);
            }

            StringBuilder planAmt = new StringBuilder();
            if (currency != null) {
                String currencySymbol = currency.getSymbol();
                planAmt.append(currencySymbol);
            }
            planAmt.append(formattedRecurringPaymentAmount);
            StringBuilder planDuration = new StringBuilder();
            if(data.getRenewalCycleType() != null) {
                if (data.getRenewalCycleType().contains(getContext().getString(R.string.app_cms_plan_renewal_cycle_type_monthly))) {
                    planDuration.append(" ");
                    planDuration.append(getContext().getString(R.string.forward_slash));
                    planDuration.append(" ");
                    if (data.getRenewalCyclePeriodMultiplier() == 1) {
                        planDuration.append(appCMSPresenter.getLanguageResourcesFile().getUIresource(getContext().getString(R.string.plan_type_month)));
                    } else {
                        planDuration.append(data.getRenewalCyclePeriodMultiplier());
                        planDuration.append(" ");
                        if (data.getRenewalCyclePeriodMultiplier() > 1)
                            planDuration.append(appCMSPresenter.getLanguageResourcesFile().getUIresource(getContext().getString(R.string.plan_type_months)));
                        else
                            planDuration.append(appCMSPresenter.getLanguageResourcesFile().getUIresource(getContext().getString(R.string.plan_type_month)));
                    }
                }
                if (data.getRenewalCycleType().contains(getContext().getString(R.string.app_cms_plan_renewal_cycle_type_yearly))) {
                    planDuration.append(" ");
                    planDuration.append(getContext().getString(R.string.forward_slash));
                    planDuration.append(" ");
                    if (data.getRenewalCyclePeriodMultiplier() == 1) {
                        planDuration.append(appCMSPresenter.getLanguageResourcesFile().getUIresource(getContext().getString(R.string.plan_type_year)));
                    } else {
                        planDuration.append(data.getRenewalCyclePeriodMultiplier());
                        planDuration.append(" ");
                        if (data.getRenewalCyclePeriodMultiplier() > 1)
                            planDuration.append(appCMSPresenter.getLanguageResourcesFile().getUIresource(getContext().getString(R.string.plan_type_years)));
                        else
                            planDuration.append(appCMSPresenter.getLanguageResourcesFile().getUIresource(getContext().getString(R.string.plan_type_year)));
                    }
                }
                if (data.getRenewalCycleType().contains(getContext().getString(R.string.app_cms_plan_renewal_cycle_type_daily))) {
                    planDuration.append(" ");
                    planDuration.append(getContext().getString(R.string.forward_slash));
                    planDuration.append(" ");
                    if (data.getRenewalCyclePeriodMultiplier() == 1) {
                        planDuration.append(getContext().getString(R.string.plan_type_day));
                    } else {
                        planDuration.append(data.getRenewalCyclePeriodMultiplier());
                        planDuration.append(" ");
                        if (data.getRenewalCyclePeriodMultiplier() > 1)
                            planDuration.append(appCMSPresenter.getLanguageResourcesFile().getUIresource(getContext().getString(R.string.plan_type_days)));
                        else
                            planDuration.append(appCMSPresenter.getLanguageResourcesFile().getUIresource(getContext().getString(R.string.plan_type_day)));
                    }
                }
            }

            plan.append(planAmt.toString());
            plan.append(planDuration.toString());
        }
        return plan.toString();
    }

    private void createFeaturesView(List<ContentDatum> contentData) {
        List<String> features = getAllFeatures(contentData);
        if (features.size() != 0) {
            for (int i = 0; i < features.size(); i++) {
                TableRow tableRow2 = new TableRow(getContext());
                addPlanHeader(tableRow2,features.get(i),appCMSPresenter.getAppTextColor(),10);
                addEmptyImageView(tableRow2);
                LayoutParams rowParams = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
                rowParams.setMargins(0, 0, 0, 0);
                tableRow2.setLayoutParams(rowParams);
                ImageView[] featureImage = new ImageView[plansToShow];
                int img = 0;
                for (int k = currentPlanCount - plansToShow; k < currentPlanCount; k++) {
                    featureImage[img] = new ImageView(getContext());
                    TableRow.LayoutParams params2 = new TableRow.LayoutParams(0,  LayoutParams.WRAP_CONTENT,1);
                    if (img < plansToShow - 1)
                        params2.setMargins(0, 0, 50, 0);
                    params2.gravity = Gravity.CENTER_VERTICAL;
                    featureImage[img].setLayoutParams(params2);
                    if (checkFeatureExist(features.get(i), contentData.get(k)))
                        featureImage[img].setImageResource(R.drawable.ic_check);
                    else
                        featureImage[img].setImageResource(R.drawable.ic_clear);
                    if (currentSelection == img)
                        featureImage[img].setColorFilter(appCMSPresenter.getGeneralTextColor(), android.graphics.PorterDuff.Mode.SRC_IN);
                    else
                        featureImage[img].setColorFilter(ContextCompat.getColor(getContext(), android.R.color.darker_gray), android.graphics.PorterDuff.Mode.SRC_IN);
                    tableRow2.addView(featureImage[img]);
                    img++;
                }

                addEmptyImageView(tableRow2);
                //addEmptyView(tableRow2);
                //addEmptyTextView(tableRow2);
                addView(tableRow2);
                addRowSeparator();
            }
        }
    }

    private void addEmptyView(TableRow row) {
        if (totalPlans > 3) {
            ImageView imageView = new ImageView(getContext());
            imageView.setImageResource(R.drawable.left_arrow);
            imageView.setVisibility(INVISIBLE);
            row.addView(imageView);
        }
    }

    private void addEmptyImageView(TableRow row) {
        ImageView imageView = new ImageView(getContext());
        imageView.setImageResource(R.drawable.left_arrow);
        imageView.setVisibility(INVISIBLE);
        row.addView(imageView);
    }

    private void addEmptyTextView(TableRow row) {
        if (plansToShow < 3) {
            for (int i = 0; i < 3 - plansToShow; i++) {
                TextView textView = new TextView(getContext());
                TableRow.LayoutParams params2 = new TableRow.LayoutParams(0, 55, 1);
                textView.setLayoutParams(params2);
                textView.setText("Monthly");
                textView.setVisibility(INVISIBLE);
                row.addView(textView);
            }
        }
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

    private void addFeatureText(String feature) {
        TextView textView = new TextView(getContext());
        TableLayout.LayoutParams params = new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.WRAP_CONTENT);
        params.setMargins(0, 40, 0, 0);
        textView.setLayoutParams(params);
        textView.setGravity(Gravity.CENTER);
        textView.setText(feature);
        textView.setTextColor(Color.parseColor(appCMSPresenter.getAppCtaTextColor()));
        addView(textView);
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

    private void addRowSeparator() {
        View view = new View(getContext());
        LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, 3);
        params.setMargins(0, 0, 0, 0);
        view.setLayoutParams(params);
        view.setBackgroundColor(Color.parseColor("#323232"));
        addView(view);
    }

    class PlanCFocusChangeListener implements OnFocusChangeListener {
        int buttonId;
        PlanCFocusChangeListener(int id) {
            buttonId = id;
        }

        @Override
        public void onFocusChange(View view, boolean b) {
            currentSelection = buttonId;
            if(mContext instanceof AppCmsHomeActivity){
                ((AppCmsHomeActivity)(Activity) mContext).shouldShowSubLeftNavigation(currentSelection == 0);
            }
        }
    }

    class PlanClickListener implements OnClickListener {
        int buttonId;
        ContentDatum contentDatum;
        PlanClickListener(int id,ContentDatum contentDatum) {
            buttonId = id;
            this.contentDatum = contentDatum;
        }
        @Override
        public void onClick(View v) {
            currentSelection = buttonId;
            changeSelectedPlan(module);
            module.setItemPosition(getSelectedPlanPositionFromList());
        }
    }
    private int selectedPlanPositionFromList;
    public int getSelectedPlanPositionFromList() {
        if(currentPlanCount > 3){
            selectedPlanPositionFromList = currentPlanCount + currentSelection - plansToShow;
        }else{
            selectedPlanPositionFromList = currentSelection;
        }
        return selectedPlanPositionFromList;
    }

    public void setSelectedPlanPositionFromList(int selectedPlanPositionFromList) {
        this.selectedPlanPositionFromList = selectedPlanPositionFromList;
    }

    class PreviousPlanClickListener implements OnClickListener {

        PreviousPlanClickListener() { }
        @Override
        public void onClick(View v) {
            currentSelection = 0;
            plansToShow = 3;
            if (currentPlanCount % 3 == 0) {
                currentPlanCount = currentPlanCount - 3;
            } else if (currentPlanCount % 3 == 1) {
                currentPlanCount = currentPlanCount - 1;
            } else if (currentPlanCount % 3 == 2) {
                currentPlanCount = currentPlanCount - 2;
            }
            changeSelectedPlan(module);
            module.setItemPosition(getSelectedPlanPositionFromList());
        }
    }

    class NextPlanClickListener implements OnClickListener {

        NextPlanClickListener() { }
        @Override
        public void onClick(View v) {
            currentSelection = 0;
            int plansLeft=totalPlans-currentPlanCount;
            if(plansLeft >= 3){
                plansToShow = 3;
                currentPlanCount = currentPlanCount + plansToShow;
            }else if(plansLeft==2){
                plansToShow = 2;
                currentPlanCount = currentPlanCount + plansToShow;
            }else if(plansLeft==1){
                plansToShow = 1;
                currentPlanCount = currentPlanCount + plansToShow;
            }
            changeSelectedPlan(module);
            module.setItemPosition(getSelectedPlanPositionFromList());
        }
    }
}
