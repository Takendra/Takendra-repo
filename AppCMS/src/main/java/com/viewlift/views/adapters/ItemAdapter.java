package com.viewlift.views.adapters;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.viewlift.R;
import com.viewlift.models.data.appcms.api.ContentDatum;
import com.viewlift.models.data.appcms.api.Season_;
import com.viewlift.presenters.AppCMSPresenter;
import com.viewlift.views.rxbus.SeasonTabSelectorBus;

import java.util.List;


public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ViewHolder> {

    List<Season_> seasonData;
    AppCMSPresenter appCMSPresenter;
    Context context;
    BottomSheetDialog mBottomSheetDialog;
    ConstraintLayout constraintLayout;

    public ItemAdapter(List<Season_> items, Context context, AppCMSPresenter appCMSPresenter, BottomSheetDialog mBottomSheetDialog, ConstraintLayout constraintLayout) {
        seasonData = items;
        this.appCMSPresenter = appCMSPresenter;
        this.context = context;
        this.mBottomSheetDialog = mBottomSheetDialog;
        this.constraintLayout = constraintLayout;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem = layoutInflater.inflate(R.layout.bottom_sheet_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(listItem);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.textView.setText(seasonData.get(position).getTitle());
        if (appCMSPresenter.getSelectedSeason() != position)
            holder.textView.setTextColor(Color.parseColor("#9b9b9b"));
        else
            holder.textView.setTextColor(Color.parseColor(appCMSPresenter.getAppCtaBackgroundColor()));
    }

    @Override
    public int getItemCount() {
        if (seasonData == null)
            return 0;
        else
            return seasonData.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView textView;
        String item;

        public ViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            textView = itemView.findViewById(R.id.textView);
        }

        @Override
        public void onClick(View v) {
            TextView episodes = constraintLayout.findViewById(R.id.episodes);
            episodes.setTextColor(Color.parseColor(appCMSPresenter.getAppCtaBackgroundColor()));
            TextView season = constraintLayout.findViewById(R.id.season);
            season.setTextColor(Color.parseColor(appCMSPresenter.getAppTextColor()));
            setTextViewDrawableColor(season, Color.parseColor(appCMSPresenter.getAppTextColor()));
            int position = getAdapterPosition();
            appCMSPresenter.setSelectedSeason(position);
            List<ContentDatum> adapterData = seasonData.get(position).getEpisodes();
            appCMSPresenter.setSeasonEpisodeAdapterData(adapterData);
            if (adapterData != null && adapterData.size() > 0)
                appCMSPresenter.setRelatedVideos(adapterData.get(0).getRelatedVideos());
            else
                appCMSPresenter.setRelatedVideos(null);
            SeasonTabSelectorBus.instanceOf().setTab(adapterData);
            mBottomSheetDialog.dismiss();
        }
    }

    public void setmBottomSheetDialog(BottomSheetDialog mBottomSheetDialog) {
        this.mBottomSheetDialog = mBottomSheetDialog;
    }

    private void setTextViewDrawableColor(TextView textView, int color) {
        for (Drawable drawable : textView.getCompoundDrawables()) {
            if (drawable != null) {
                drawable.setColorFilter(new PorterDuffColorFilter(color, PorterDuff.Mode.SRC_IN));
            }
        }
    }
}


