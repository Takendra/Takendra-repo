package com.viewlift.views.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.bumptech.glide.request.transition.Transition;
import com.viewlift.R;
import com.viewlift.models.data.appcms.api.ContentDatum;
import com.viewlift.models.data.appcms.api.Module;
import com.viewlift.models.data.appcms.ui.page.Component;
import com.viewlift.models.data.appcms.ui.page.Tabs;
import com.viewlift.presenters.AppCMSPresenter;
import com.viewlift.utils.CommonUtils;
import com.viewlift.utils.TrailerPlayBack;
import com.viewlift.views.customviews.CustomVideoPlayerView;
import com.viewlift.views.customviews.ViewCreator;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class LiveModuleTabAdapter extends RecyclerView.Adapter<LiveModuleTabAdapter.MyViewHolder> {
    List<Tabs> livePlayerTabList = new ArrayList<>();
    AppCMSPresenter appCMSPresenter;
    int currentPlayingSeason;
    SeasonClickListener seasonClickListener;
    CustomVideoPlayerView customVideoPlayerView;
    String videoId = "";
    Module moduleApi;
    String title = null;

    public LiveModuleTabAdapter(List<Tabs> tabsList, AppCMSPresenter appCMSPresenter, int currentPlayingSeason, CustomVideoPlayerView customVideoPlayerView, Module moduleApi) {
        if (tabsList == null)
            tabsList = new ArrayList<>();
        this.customVideoPlayerView = customVideoPlayerView;
        this.moduleApi = moduleApi;
        this.livePlayerTabList.addAll(tabsList);
        this.appCMSPresenter = appCMSPresenter;
        this.currentPlayingSeason = currentPlayingSeason;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.element_live_player, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        holder.title.setText(livePlayerTabList.get(position).getTabTitle());
        if (currentPlayingSeason == position) {
            holder.title.requestFocus();
            if (appCMSPresenter.getPlatformType() == AppCMSPresenter.PlatformType.TV)
                holder.title.setTextColor(appCMSPresenter.getBrandPrimaryCtaTextColor());
            else {
                holder.title.setTextColor(Color.parseColor(appCMSPresenter.getAppCtaBackgroundColor()));
                holder.selected_background.setBackgroundColor(Color.parseColor(appCMSPresenter.getAppCtaBackgroundColor()));
                holder.livPlayerTabImage.setColorFilter(Color.parseColor(appCMSPresenter.getAppCtaBackgroundColor()), android.graphics.PorterDuff.Mode.SRC_IN);
            }
        } else {
            if (appCMSPresenter.getPlatformType() == AppCMSPresenter.PlatformType.TV)
                holder.title.setTextColor(appCMSPresenter.getBrandSecondaryCtaTextColor());
            else {
                holder.title.setTextColor(ContextCompat.getColor(holder.itemView.getContext(), android.R.color.white));
                holder.selected_background.setBackground(null);
                holder.livPlayerTabImage.setColorFilter(appCMSPresenter.getCurrentActivity().getResources().getColor(R.color.color_white), android.graphics.PorterDuff.Mode.SRC_IN);
            }
        }
        RequestOptions requestOptions = new RequestOptions()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .fitCenter()
                .placeholder(R.drawable.logo_icon);
        Glide.with(holder.itemView.getContext())
                .asBitmap()
                .load(livePlayerTabList.get(position).getTabIcon())
                .apply(requestOptions)
                .into(new BitmapImageViewTarget(holder.livPlayerTabImage) {
                    @Override
                    public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                        super.onResourceReady(resource, transition);

                    }
                });
        ViewCreator.setTypeFace(holder.itemView.getContext(), appCMSPresenter, appCMSPresenter.getJsonValueKeyMap(), new Component(), holder.title);
        holder.title.setClickable(true);
        holder.itemView.setClickable(true);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //SeasonTabSelectorBus.instanceOf().setTab(seasons.get(position).getTabTitle());
                currentPlayingSeason = position;
                setLiveplayerData(position);
                notifyDataSetChanged();
                if (seasonClickListener != null)
                    seasonClickListener.selectedSeason(position);

            }
        });
        holder.title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //SeasonTabSelectorBus.instanceOf().setTab(seasons.get(position).getEpisodes());
                currentPlayingSeason = position;
                setLiveplayerData(position);
                notifyDataSetChanged();
                if (videoId != null) {
//                    customVideoPlayerView.setVideoUri(videoId, appCMSPresenter.getLocalisedStrings().getLoadingVideoText(), false, false);
//                    customVideoPlayerView.setVideoTitle(title);
//                    customVideoPlayerView.setstreamingQualitySelector(false);
                    // videoPlayerContent = customVideoPlayerView.getVideoPlayerContent();
                }

            }
        });
    }

    private void setLiveplayerData(int position) {
        if (moduleApi != null && moduleApi.getContentData() != null
                && moduleApi.getContentData().size() != 0
                && moduleApi.getContentData().get(position) != null
                && moduleApi.getContentData().get(position).getGist() != null
                && moduleApi.getContentData().get(position).getGist().getTitle() != null
                && !TextUtils.isEmpty(moduleApi.getContentData().get(position).getGist().getTitle())) {
            title = moduleApi.getContentData().get(position).getGist().getTitle();
        }
        if (moduleApi != null &&
                moduleApi.getContentData() != null &&
                !moduleApi.getContentData().isEmpty() &&
                moduleApi.getContentData().size() != 0 &&
                moduleApi.getContentData().get(position) != null &&
                moduleApi.getContentData().get(position).getGist() != null &&
                moduleApi.getContentData().get(position).getGist().getId() != null) {
            videoId = moduleApi.getContentData().get(position).getGist().getOriginalObjectId();
            if (videoId == null) {
                videoId = moduleApi.getContentData().get(position).getGist().getId();
            }
        }
        ContentDatum contentDatum = null;
        if (moduleApi != null && moduleApi.getContentData() != null
                && moduleApi.getContentData().size() > 0
                && moduleApi.getContentData().get(position) != null) {
            contentDatum = moduleApi.getContentData().get(position);
        }
        if (videoId != null && (!videoId.isEmpty())) {
//            String trailerId=null;
//            String promoId=null;
            customVideoPlayerView.setVideoTitle(title);
            CommonUtils.setTabPostion(position);
            customVideoPlayerView.setstreamingQualitySelector(false);
            if (contentDatum != null &&
                    contentDatum.getGist() != null && contentDatum.getGist().isLiveStream()) {
                TrailerPlayBack.trailerPlayBack().openTrailer(customVideoPlayerView, appCMSPresenter, contentDatum, videoId);
            } else {
                customVideoPlayerView.setUseAdUrl(true);
                customVideoPlayerView.setVideoUri(videoId, appCMSPresenter.getLocalisedStrings().getLoadingVideoText(), false, false, contentDatum);
            }

            // videoPlayerContent = customVideoPlayerView.getVideoPlayerContent();
        }
    }

    @Override
    public int getItemCount() {
        return livePlayerTabList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.title)
        AppCompatTextView title;
        @BindView(R.id.selected_background)
        View selected_background;
        @BindView(R.id.livPlayerTabImage)
        ImageView livPlayerTabImage;

        public MyViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);

            title.setOnFocusChangeListener((v, hasFocus) -> {
                if (hasFocus) {
                    title.setTextColor(appCMSPresenter.getBrandPrimaryCtaTextColor());
                } else {
                    title.setTextColor(appCMSPresenter.getBrandSecondaryCtaTextColor());
                }
            });
        }
    }

    public void setSeasonClickListener(CustomVideoPlayerView customVideoPlayerView) {
    }

    public void setSeasonClickListener(SeasonClickListener seasonClickListener) {
        this.seasonClickListener = seasonClickListener;
    }

    public interface SeasonClickListener {
        void selectedSeason(int position);
    }
}