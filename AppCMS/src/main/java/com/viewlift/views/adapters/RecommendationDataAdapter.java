package com.viewlift.views.adapters;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.PorterDuff;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.viewlift.R;
import com.viewlift.models.data.appcms.ui.main.RecommendationGenre;
import com.viewlift.presenters.AppCMSPresenter;
import com.viewlift.models.data.appcms.ui.page.Component;
import com.viewlift.presenters.AppCMSPresenter;
import com.viewlift.utils.CommonUtils;


import java.util.ArrayList;

public class RecommendationDataAdapter extends RecyclerView.Adapter<RecommendationDataAdapter.ViewHolder> {

    private ArrayList<RecommendationGenre> android;
    private Context context;
    AppCMSPresenter appCMSPresenter;

    public RecommendationDataAdapter(Context context, ArrayList<RecommendationGenre> android,AppCMSPresenter appCMSPresenter) {
        this.android = android;
        this.context = context;
        this.appCMSPresenter = appCMSPresenter;
    }

    @Override
    public RecommendationDataAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.recommend_row_layout, viewGroup, false);

        if (appCMSPresenter.isNewsTemplate()) {
            view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.recommend_row_news_layout, viewGroup, false);
        }
         return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final RecommendationDataAdapter.ViewHolder viewHolder, final int i) {

        viewHolder.tv_android.setText(android.get(i).getAndroid_version_name());
        Glide.with(context).load(android.get(i).getAndroid_image_url()).into(viewHolder.img_android);
        if (appCMSPresenter.isNewsTemplate() && appCMSPresenter.getPlatformType() == AppCMSPresenter.PlatformType.TV) {
            viewHolder.itemView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View view, boolean hasFocus) {
                    if (hasFocus || android.get(i).isItemSelected()) {
                        viewHolder.shadowLayout.setBackgroundResource(R.drawable.border_news_rectangular);
                    } else {
                        viewHolder.shadowLayout.setBackgroundResource(R.drawable.genre_custom_border);
                    }
                }
            });
        }
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (android.get(i).isItemSelected()) {
                    unhighlightView(viewHolder);
                    android.get(i).setItemSelected(false);
                } else {
                    highlightView(viewHolder);
                    android.get(i).setItemSelected(true);
                }
            }
        });

        if (this.android.get(i).isItemSelected()) {
            highlightView(viewHolder);
        } else {
            unhighlightView(viewHolder);
        }
    }

    @Override
    public int getItemCount() {
        return android.size();
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    private void highlightView(ViewHolder holder) {
        holder.shadowLayout.setBackgroundResource(R.drawable.genre_background_selected);
        if (appCMSPresenter.isNewsTemplate()) {
            if (appCMSPresenter.getPlatformType() == AppCMSPresenter.PlatformType.TV) {
                holder.shadowLayout.setBackgroundResource(R.drawable.border_news_rectangular);
                holder.tv_android.setTextColor(appCMSPresenter.getBrandPrimaryCtaTextColor());
            } else {
                holder.shadowLayout.setBackgroundResource(R.drawable.round_btn);
                holder.shadowLayout.getBackground().setColorFilter(appCMSPresenter.getBrandPrimaryCtaColor(), PorterDuff.Mode.SRC_ATOP);
                holder.tv_android.setTextColor(appCMSPresenter.getGeneralBackgroundColor());
            }
        }
    }

    private void unhighlightView(ViewHolder holder) {
        holder.shadowLayout.setBackgroundResource(R.drawable.genre_custom_border);
        if (appCMSPresenter.isNewsTemplate()) {
            holder.tv_android.setTextColor(appCMSPresenter.getGeneralTextColor());
            if (appCMSPresenter.getPlatformType() == AppCMSPresenter.PlatformType.TV) {
                holder.shadowLayout.setBackgroundResource(R.drawable.genre_custom_border);
            } else {
                holder.shadowLayout.setBackgroundResource(R.drawable.round_btn);
                holder.shadowLayout.getBackground().setColorFilter(ContextCompat.getColor(context, R.color.recommendedbackgroundColor), PorterDuff.Mode.SRC_ATOP);
            }
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tv_android;
        private ImageView img_android;
        private RelativeLayout shadowLayout;
        LinearLayout ll_mainLayout;

        public ViewHolder(View view) {
            super(view);
            shadowLayout = view.findViewById(R.id.img_shadow);
            tv_android = view.findViewById(R.id.tv_android);
            img_android = view.findViewById(R.id.img_android);
            ll_mainLayout = view.findViewById(R.id.ll_mainLayout);
            if (appCMSPresenter.isNewsTemplate() && appCMSPresenter.getPlatformType() != AppCMSPresenter.PlatformType.TV) {
                RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) shadowLayout.getLayoutParams();
                lp.width = 800;
                shadowLayout.setLayoutParams(lp);
            }
        }
    }

}
