package com.viewlift.views.adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.appcompat.widget.AppCompatTextView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.viewlift.AppCMSApplication;
import com.viewlift.R;
import com.viewlift.models.billing.utils.Payment;
import com.viewlift.models.data.appcms.ui.page.Component;
import com.viewlift.presenters.AppCMSPresenter;
import com.viewlift.views.customviews.ViewCreator;
import com.viewlift.views.dialog.CustomShape;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PaymentOptionsAdapter extends RecyclerView.Adapter<PaymentOptionsAdapter.MyViewHolder> {
    @Inject
    AppCMSPresenter appCMSPresenter;
    PaymentOptionSelected paymentOptionSelected;
    List<Payment> paymentProviders;
    Context context;

    public PaymentOptionsAdapter(Context context, List<Payment> paymentProviders) {
        this.context = context;
        this.paymentProviders = paymentProviders;
        ((AppCMSApplication) context.getApplicationContext()).getAppCMSPresenterComponent().inject(this);
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.element_payment, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        CustomShape.makeRoundCorner(ContextCompat.getColor(holder.parentLayout.getContext(), android.R.color.transparent), 10, holder.parentLayout, 2, Color.parseColor(appCMSPresenter.getAppTextColor()));
        ViewCreator.setTypeFace(holder.itemView.getContext(), appCMSPresenter, appCMSPresenter.getJsonValueKeyMap(), new Component(), holder.paymentName);
        ViewCreator.setTypeFace(holder.itemView.getContext(), appCMSPresenter, appCMSPresenter.getJsonValueKeyMap(), new Component(), holder.paymentInf);
        holder.paymentName.setTextColor(Color.parseColor(appCMSPresenter.getAppTextColor()));
        holder.paymentName.setText(paymentProviders.get(position).getName().toString());
        holder.paymentName.setCompoundDrawablesWithIntrinsicBounds(paymentProviders.get(position).getImage(), 0, 0, 0);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                paymentOptionSelected.billingSelected(paymentProviders.get(position));
            }
        });
    }

    public void setPaymentOptionSelected(PaymentOptionSelected paymentOptionSelected) {
        this.paymentOptionSelected = paymentOptionSelected;
    }

    @Override
    public int getItemCount() {
        return paymentProviders.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.parentLayout)
        ConstraintLayout parentLayout;
        @BindView(R.id.paymentName)
        AppCompatTextView paymentName;
        @BindView(R.id.paymentInf)
        AppCompatTextView paymentInf;


        public MyViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    public interface PaymentOptionSelected {
        void billingSelected(Payment payment);
    }
}
