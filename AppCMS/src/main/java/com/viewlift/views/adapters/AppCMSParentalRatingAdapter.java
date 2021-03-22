package com.viewlift.views.adapters;

import android.content.res.ColorStateList;
import android.graphics.PorterDuff;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatRadioButton;
import androidx.recyclerview.widget.RecyclerView;
import com.viewlift.R;
import com.viewlift.models.data.appcms.api.AppCMSParentalRating;
import com.viewlift.presenters.AppCMSPresenter;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AppCMSParentalRatingAdapter extends RecyclerView.Adapter<AppCMSParentalRatingAdapter.ViewHolder> {

    private AppCMSPresenter appCMSPresenter;
    private List<AppCMSParentalRating> dataList;
    private int selectedPosition = -1;
    private OnSelectRatingListener listener;

    public AppCMSParentalRatingAdapter(AppCMSPresenter appCMSPresenter, List<AppCMSParentalRating> dataList, OnSelectRatingListener listener) {
        this.appCMSPresenter = appCMSPresenter;
        this.dataList = dataList;
        this.listener = listener;

        if (appCMSPresenter.getAppPreference() != null) {
            String preSelectedRestrictions = appCMSPresenter.getAppPreference().getParentalRating();
            for (int i = 0; i < dataList.size(); i++) {
                if (dataList.get(i).getName().equalsIgnoreCase(preSelectedRestrictions)) {
                    setSelectedPosition(i);
                    break;
                }
            }
        }
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_parental_rating_adapter, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        AppCMSParentalRating parentalRating = dataList.get(position);
        holder.mName.setText(parentalRating.getName());
        holder.mCategory.setText(parentalRating.getCategory());

        if (selectedPosition == position)
            holder.mRadioButton.setChecked(true);
        else
            holder.mRadioButton.setChecked(false);

        if (position == dataList.size() - 1)
            holder.mSeparatorView.setVisibility(View.GONE);
        else
            holder.mSeparatorView.setVisibility(View.VISIBLE);

    }

    public AppCMSParentalRating getSelectedItem() {
        if (selectedPosition < 0 || selectedPosition >= dataList.size()) {
            return null;
        }
        return dataList.get(selectedPosition);
    }

    private void setSelectedPosition(int selectedPosition) {
        this.selectedPosition = selectedPosition;
        if (listener != null)
            listener.onSelectRating(dataList.get(selectedPosition).getMessage());
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.radioButton)
        AppCompatRadioButton mRadioButton;
        @BindView(R.id.name)
        TextView mName;
        @BindView(R.id.category)
        TextView mCategory;
        @BindView(R.id.separatorView)
        View mSeparatorView;

        private ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            int textColor = appCMSPresenter.getGeneralTextColor();
            int tintColor = appCMSPresenter.getBrandPrimaryCtaColor();
            mName.setTextColor(textColor);
            mCategory.setTextColor(textColor);
            itemView.setOnClickListener(v -> {
                setSelectedPosition(getAdapterPosition());
                notifyDataSetChanged();
            });

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (mRadioButton.getButtonDrawable() != null) {
                    mRadioButton.getButtonDrawable().setColorFilter(tintColor, PorterDuff.Mode.MULTIPLY);
                }
            } else {
                ColorStateList colorStateList = new ColorStateList(new int[][]{new int[]{android.R.attr.state_checked}, new int[]{}}, new int[]{
                        tintColor,
                        tintColor
                });

                mRadioButton.setButtonTintList(colorStateList);
            }
        }
    }

    public interface OnSelectRatingListener {
        void onSelectRating(String ratingMsg);
    }
}