package com.viewlift.views.adapters.plans;

import android.content.Context;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import androidx.appcompat.widget.AppCompatTextView;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.viewlift.AppCMSApplication;
import com.viewlift.R;
import com.viewlift.models.data.appcms.api.ContentDatum;
import com.viewlift.models.data.appcms.ui.page.Component;
import com.viewlift.presenters.AppCMSPresenter;
import com.viewlift.utils.CommonUtils;
import com.viewlift.views.customviews.ViewCreator;

import java.util.ArrayList;
import java.util.Currency;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PlansSpinnerAdapter extends BaseAdapter {
    private Context context;
    private List<ContentDatum> plans;
    @Inject
    AppCMSPresenter appCMSPresenter;

    public PlansSpinnerAdapter(Context context, List<ContentDatum> plans) {
        this.context = context;
        this.plans = plans;
        if (this.plans == null)
            this.plans = new ArrayList<>();
        ((AppCMSApplication) context.getApplicationContext()).getAppCMSPresenterComponent().inject(this);
    }

    @Override
    public int getCount() {
        return plans.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int id) {
        return id;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = (LayoutInflater.from(context)).inflate(R.layout.plan_spinner_view, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        ViewCreator.setTypeFace(holder.planTitle.getContext(), appCMSPresenter, appCMSPresenter.getJsonValueKeyMap(), new Component(), holder.planTitle);
        ViewCreator.setTypeFace(holder.planPrice.getContext(), appCMSPresenter, appCMSPresenter.getJsonValueKeyMap(), new Component(), holder.planPrice);
        ViewCreator.setTypeFace(holder.planFrequency.getContext(), appCMSPresenter, appCMSPresenter.getJsonValueKeyMap(), new Component(), holder.planFrequency);

        holder.planTitle.setText(fetchPlanTitle(plans.get(position)));
        holder.planPrice.setText(fetchPlanPrice(plans.get(position)));
        holder.planFrequency.setText(fetchPlanFrequency(plans.get(position), convertView.getContext()));
        holder.parentLayout.setBackgroundColor(Color.parseColor(appCMSPresenter.getAppBackgroundColor()));
        holder.planTitle.setTextColor(Color.parseColor(appCMSPresenter.getAppTextColor()));
        holder.planPrice.setTextColor(Color.parseColor(appCMSPresenter.getAppTextColor()));
        holder.planFrequency.setTextColor(Color.parseColor(appCMSPresenter.getAppTextColor()));
        return convertView;
    }

    static class ViewHolder {
        @BindView(R.id.parentLayout)
        ConstraintLayout parentLayout;
        @BindView(R.id.planTitle)
        AppCompatTextView planTitle;
        @BindView(R.id.planPrice)
        AppCompatTextView planPrice;
        @BindView(R.id.planFrequency)
        AppCompatTextView planFrequency;

        private ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }

    private String fetchPlanFrequency(ContentDatum data, Context context) {
        StringBuilder planDuration = new StringBuilder();
        if (data.getRenewalCycleType() != null && data.getRenewalCycleType().contains(context.getString(R.string.app_cms_plan_renewal_cycle_type_monthly))) {
            planDuration.append(" ");
            planDuration.append(context.getString(R.string.forward_slash));
            planDuration.append(" ");
            if (data.getRenewalCyclePeriodMultiplier() == 1) {
                planDuration.append(context.getString(R.string.plan_type_month));
            } else {
                planDuration.append(data.getRenewalCyclePeriodMultiplier());
                planDuration.append(" ");
                if (data.getRenewalCyclePeriodMultiplier() > 1)
                    planDuration.append(context.getString(R.string.plan_type_months));
                else
                    planDuration.append(context.getString(R.string.plan_type_month));
            }
        }
        if (data.getRenewalCycleType() != null && data.getRenewalCycleType().contains(context.getString(R.string.app_cms_plan_renewal_cycle_type_yearly))) {
            planDuration.append(" ");
            planDuration.append(context.getString(R.string.forward_slash));
            planDuration.append(" ");
            if (data.getRenewalCyclePeriodMultiplier() == 1) {
                planDuration.append(context.getString(R.string.plan_type_year));
            } else {
                planDuration.append(data.getRenewalCyclePeriodMultiplier());
                planDuration.append(" ");
                if (data.getRenewalCyclePeriodMultiplier() > 1)
                    planDuration.append(context.getString(R.string.plan_type_years));
                else
                    planDuration.append(context.getString(R.string.plan_type_year));
            }
        }
        if (data.getRenewalCycleType() != null && data.getRenewalCycleType().contains(context.getString(R.string.app_cms_plan_renewal_cycle_type_daily))) {
            planDuration.append(" ");
            planDuration.append(context.getString(R.string.forward_slash));
            planDuration.append(" ");
            if (data.getRenewalCyclePeriodMultiplier() == 1) {
                planDuration.append(context.getString(R.string.plan_type_day));
            } else {
                planDuration.append(data.getRenewalCyclePeriodMultiplier());
                planDuration.append(" ");
                if (data.getRenewalCyclePeriodMultiplier() > 1)
                    planDuration.append(context.getString(R.string.plan_type_days));
                else
                    planDuration.append(context.getString(R.string.plan_type_day));
            }
        }
        return planDuration.toString();
    }

    private String fetchPlanTitle(ContentDatum data) {
        int planIndex = 0;
        String title = "";
        if (data.getPlanDetails() != null) {
            for (int i = 0; i < data.getPlanDetails().size(); i++) {
                if (data.getPlanDetails().get(i) != null &&
                        data.getPlanDetails().get(i).isDefault()) {
                    planIndex = i;
                }
            }
            title = data.getPlanDetails().get(planIndex).getTitle();
        }
        if (TextUtils.isEmpty(title))
            title = data.getName();
        return title;
    }

    private String fetchPlanPrice(ContentDatum data) {
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
        String formattedRecurringPaymentAmount = context.getString(R.string.cost_with_fraction,
                recurringPaymentAmount);
        if (recurringPaymentAmount - (int) recurringPaymentAmount == 0) {
            formattedRecurringPaymentAmount = context.getString(R.string.cost_without_fraction,
                    recurringPaymentAmount);
        }

        StringBuilder planAmt = new StringBuilder();
        if (currency != null) {
            String currencySymbol = currency.getSymbol();
            planAmt.append(currencySymbol);
        }
        planAmt.append(formattedRecurringPaymentAmount);
        /*if (CommonUtils.planViewPriceList != null
                && data.getPlanDetails() != null
                && CommonUtils.planViewPriceList.containsKey(data.getIdentifier())) {
            return CommonUtils.planViewPriceList.get(data.getIdentifier()).toString();
        }*/
        return planAmt.toString();
    }

}