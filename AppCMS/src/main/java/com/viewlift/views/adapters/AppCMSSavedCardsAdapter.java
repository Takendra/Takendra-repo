package com.viewlift.views.adapters;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.viewlift.AppCMSApplication;
import com.viewlift.R;
import com.viewlift.models.data.appcms.api.CardData;
import com.viewlift.models.data.appcms.api.MetadataMap;
import com.viewlift.models.data.appcms.ui.main.LocalisedStrings;
import com.viewlift.presenters.AppCMSPresenter;

import java.util.List;

import javax.inject.Inject;

public class AppCMSSavedCardsAdapter extends RecyclerView.Adapter<AppCMSSavedCardsAdapter.CustomViewHolder> {

    private List<CardData> savedCardsList;
    private List<MetadataMap.Card> cardIcons;
    @Inject
    AppCMSPresenter appCMSPresenter;
    Context context;
    @Inject
    LocalisedStrings localisedStrings;

    public AppCMSSavedCardsAdapter(Context context, List<CardData> savedCardsList, List<MetadataMap.Card> cardIcons) {
        this.context = context;
        ((AppCMSApplication) this.context.getApplicationContext()).getAppCMSPresenterComponent().inject(this);
        this.savedCardsList = savedCardsList;
        this.cardIcons = cardIcons;
    }

    public void addAll(List<CardData> newCardsList) {
        savedCardsList.clear();
        savedCardsList.addAll(newCardsList);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.saved_card_list_adapter, parent, false);
        return new CustomViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CustomViewHolder holder, int position) {
        holder.bindData(savedCardsList.get(position));
    }

    @Override
    public int getItemCount() {
        return savedCardsList == null ? 0 : savedCardsList.size();
    }

    class CustomViewHolder extends RecyclerView.ViewHolder {

        private ImageView brandIcon;
        private TextView cardNo, tvPay, tvRemove;
        private EditText edtCvv;

        private CustomViewHolder(View itemView) {
            super(itemView);
            brandIcon = itemView.findViewById(R.id.brandIcon);
            cardNo = itemView.findViewById(R.id.cardNo);
            edtCvv = itemView.findViewById(R.id.edtCvv);
            tvPay = itemView.findViewById(R.id.tvPay);
            tvRemove = itemView.findViewById(R.id.tvRemove);
            tvRemove.setTextColor(Color.parseColor("#F81004"));
            cardNo.setTextColor(Color.BLACK);
            tvRemove.setPaintFlags(tvRemove.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        }

        private void bindData(CardData data) {
            Glide.with(brandIcon).load(getCardIconUrl(data.getCardBrand())).placeholder(R.drawable.card_placeholder).into(brandIcon);
            String strLast9Di = data.getCardNumber().length() >= 9 ? data.getCardNumber().substring(data.getCardNumber().length() - 9) : "";
            cardNo.setText(strLast9Di);
            tvPay.setOnClickListener(v -> {
                String cvvNo = edtCvv.getText().toString();
                if (TextUtils.isEmpty(cvvNo)) {
                    appCMSPresenter.showToast(appCMSPresenter.getCurrentActivity().getString(R.string.please_enter_cvv_number), Toast.LENGTH_SHORT);
                    return;
                }

                if (appCMSPresenter.getJusPayUtils() != null)
                    appCMSPresenter.getJusPayUtils().onPayCard(data, cvvNo);
            });

            tvRemove.setOnClickListener(v -> {
                if (appCMSPresenter.getJusPayUtils() != null) {
                    appCMSPresenter.getJusPayUtils().deleteSavedCard(data.getCardToken(), appCMSDeleteCardResponse -> {
                        appCMSPresenter.showLoadingDialog(false);
                        if (appCMSDeleteCardResponse != null && appCMSDeleteCardResponse.getDeleteCardData() != null && appCMSDeleteCardResponse.getDeleteCardData().isDeleted()) {
                            savedCardsList.remove(getAdapterPosition());
                            notifyItemRemoved(getAdapterPosition());
                        } else {
                            appCMSPresenter.showToast(localisedStrings.getFailText(), Toast.LENGTH_SHORT);
                        }
                    });
                }
            });
        }

        private String getCardIconUrl(String name) {
            if (cardIcons != null) {
                for (MetadataMap.Card card : cardIcons) {
                    if (name.equalsIgnoreCase(card.getTitle().replace(" ", "")))
                        return card.getImageUrl();
                }
            }
            return "";
        }
    }
}