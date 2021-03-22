package com.viewlift.views.adapters;

import android.graphics.Color;
import android.graphics.Typeface;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.viewlift.AppCMSApplication;
import com.viewlift.R;
import com.viewlift.models.data.appcms.api.ContentDatum;
import com.viewlift.models.data.appcms.ui.page.Component;
import com.viewlift.presenters.AppCMSPresenter;
import com.viewlift.views.customviews.ViewCreator;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.functions.Action1;

public class Plan04Adapter extends RecyclerView.Adapter<Plan04Adapter.MyViewHolder> {
    public Plan04Adapter(List<ContentDatum> adapterData, Action1<View> clickAction) {
        if (adapterData == null)
            adapterData = new ArrayList<>();
        this.adapterData.addAll(adapterData);
        this.clickAction=clickAction;
//        Collections.reverse(this.adapterData);

    }
    Action1<View> clickAction;
    List<ContentDatum> adapterData = new ArrayList<>();
    @Inject
    AppCMSPresenter appCMSPresenter;

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.plan_04_item, parent, false);
        ((AppCMSApplication) parent.getContext().getApplicationContext()).getAppCMSPresenterComponent().inject(this);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        if (adapterData.get(position).getName() != null)
            holder.planName.setText(adapterData.get(position).getName());
        if (adapterData.get(position).getDescription() != null &&
                ((!adapterData.get(position).getDescription().toLowerCase().equalsIgnoreCase("required")) &&
                        (!adapterData.get(position).getDescription().toLowerCase().contains("\n" + "required")))) {
            SpannableStringBuilder builder = new SpannableStringBuilder();
            SpannableString redSpannable = new SpannableString(adapterData.get(position).getDescription().toUpperCase());
            redSpannable.setSpan(new ForegroundColorSpan(Color.parseColor("#EA0000")), 0, adapterData.get(position).getDescription().length(), 0);
            builder.append(redSpannable);
            holder.planDescription.setText(builder, TextView.BufferType.SPANNABLE);
        } else {
            holder.planDescription.setVisibility(View.GONE);
            holder.planName.setPadding(0,0,0,8);
        }
        if (adapterData.get(position).getPlanDetails() != null) {
            String[] planAmt = Double.toString(adapterData.get(position).getPlanDetails().get(0).getRecurringPaymentAmount()).split("\\.");
            holder.planAmount.setText(planAmt[0], TextView.BufferType.SPANNABLE);
        }

        if (adapterData.get(position).getPlanDetails() != null) {
            String[] planAmt = Double.toString(adapterData.get(position).getPlanDetails().get(0).getRecurringPaymentAmount()).split("\\.");
            holder.planAmountDec.setText(planAmt[1], TextView.BufferType.SPANNABLE);
        }
        if (adapterData.get(position).getPlanDetails() != null && adapterData.get(position).getPlanDetails().get(0) != null && adapterData.get(position).getPlanDetails().get(0).getFeatureDetails() != null) {
            SpannableStringBuilder builder = new SpannableStringBuilder();
            for (int i = 0; i < adapterData.get(position).getPlanDetails().get(0).getFeatureDetails().size(); i++) {
                if (i == 0) {
                    SpannableString redSpannable = new SpannableString(adapterData.get(position).getPlanDetails().get(0).getFeatureDetails().get(0).getTextToDisplay());
                    redSpannable.setSpan(new ForegroundColorSpan(Color.parseColor("#EA0000")), 0, adapterData.get(position).getPlanDetails().get(0).getFeatureDetails().get(0).getTextToDisplay().length(), 0);
                    redSpannable.setSpan(new StyleSpan(Typeface.BOLD), 0, adapterData.get(position).getPlanDetails().get(0).getFeatureDetails().get(0).getTextToDisplay().length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    builder.append(redSpannable);
                    builder.append("\n");
                } else {
                    SpannableString redSpannable = new SpannableString(adapterData.get(position).getPlanDetails().get(0).getFeatureDetails().get(i).getTextToDisplay());
                    redSpannable.setSpan(new ForegroundColorSpan(Color.parseColor("#000000")), 0, adapterData.get(position).getPlanDetails().get(0).getFeatureDetails().get(i).getTextToDisplay().length(), 0);
                    builder.append(redSpannable);
                }
            }
            holder.planFeature.setText(builder, TextView.BufferType.SPANNABLE);
        }
        if (position == adapterData.size() - 1) {
            holder.leftSeparator.setVisibility(View.GONE);
        }
        if (adapterData.get(position).getPlanDetails().get(0).getCallToAction() != null)
            holder.planPurchaseButton.setText(adapterData.get(position).getPlanDetails().get(0).getCallToAction());
        holder.planPurchaseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickAction.call(v);
                double price = adapterData.get(position).getPlanDetails().get(0).getStrikeThroughPrice();
                if (price == 0) {
                    price = adapterData.get(position).getPlanDetails().get(0).getRecurringPaymentAmount();
                }

                double discountedPrice = adapterData.get(position).getPlanDetails().get(0).getRecurringPaymentAmount();
                double discountPrice=adapterData.get(position).getPlanDetails().get(0).getDiscountedPrice();

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

                appCMSPresenter.initiateSignUpAndSubscription(adapterData.get(position).getIdentifier(),
                        adapterData.get(position).getId(),
                        adapterData.get(position).getPlanDetails().get(0).getCountryCode(),
                        adapterData.get(position).getName(),
                        price,
                        discountedPrice,
                        adapterData.get(position).getPlanDetails().get(0).getRecurringPaymentCurrencyCode(),
                        adapterData.get(position).getPlanDetails().get(0).getCountryCode(),
                        adapterData.get(position).getRenewable(),
                        adapterData.get(position).getRenewalCycleType(),
                        upgradesAvailable,discountPrice,
                        adapterData.get(position).getPlanDetails().get(0).getAllowedPayMethods(),
                        adapterData.get(position).getPlanDetails().get(0).getCarrierBillingProviders());
            }
        });

    }

    @Override
    public int getItemCount() {
        return adapterData.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.planName)
        TextView planName;
        @BindView(R.id.planDescription)
        TextView planDescription;
        @BindView(R.id.planAmount)
        TextView planAmount;
        @BindView(R.id.planAmountDec)
        TextView planAmountDec;
        @BindView(R.id.planFeature)
        TextView planFeature;
        @BindView(R.id.leftSeparator)
        View leftSeparator;
        @BindView(R.id.planPurchaseButton)
        Button planPurchaseButton;

        public MyViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
            Component component = new Component();
            component.setFontWeight(view.getContext().getString(R.string.app_cms_page_font_bold_key));
            ViewCreator.setTypeFace(view.getContext(), appCMSPresenter, appCMSPresenter.getJsonValueKeyMap(), component, planName);
            ViewCreator.setTypeFace(view.getContext(), appCMSPresenter, appCMSPresenter.getJsonValueKeyMap(), component, planDescription);
            ViewCreator.setTypeFace(view.getContext(), appCMSPresenter, appCMSPresenter.getJsonValueKeyMap(), component, planAmount);
            ViewCreator.setTypeFace(view.getContext(), appCMSPresenter, appCMSPresenter.getJsonValueKeyMap(), component, planAmountDec);
            ViewCreator.setTypeFace(view.getContext(), appCMSPresenter, appCMSPresenter.getJsonValueKeyMap(), component, planPurchaseButton);
            component.setFontWeight(null);
            ViewCreator.setTypeFace(view.getContext(), appCMSPresenter, appCMSPresenter.getJsonValueKeyMap(), component, planFeature);
        }
    }
}
