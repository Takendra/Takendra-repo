package com.viewlift.views.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.bumptech.glide.request.transition.Transition;
import com.viewlift.R;
import com.viewlift.models.data.appcms.api.ContentDatum;
import com.viewlift.models.data.appcms.ui.page.Component;
import com.viewlift.presenters.AppCMSPresenter;
import com.viewlift.utils.CommonUtils;
import com.viewlift.views.customviews.ViewCreator;
import com.viewlift.views.listener.VideoSelected;
import com.viewlift.views.rxbus.SeasonTabSelectorBus;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;

public class PlayerEpisodeAdapter extends RecyclerView.Adapter<PlayerEpisodeAdapter.MyViewHolder> {
    List<ContentDatum> episodes = new ArrayList<>();
    AppCMSPresenter appCMSPresenter;

    public void setVideoSelected(VideoSelected videoSelected) {
        this.videoSelected = videoSelected;
    }

    VideoSelected videoSelected;

    public PlayerEpisodeAdapter(List<ContentDatum> episode, RecyclerView mRecyclerView, AppCMSPresenter appCMSPresenter) {
        if (episode == null)
            episode = new ArrayList<>();
        this.appCMSPresenter = appCMSPresenter;
        SeasonTabSelectorBus.instanceOf().getSelectedTab().subscribe(new Observer<List<ContentDatum>>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(List<ContentDatum> adapterDataSeason) {
                updateData(mRecyclerView, adapterDataSeason);
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        });
    }

    public void updateData(RecyclerView listView, List<ContentDatum> contentData) {
        listView.setAdapter(null);
        episodes = null;
        episodes = contentData;
        listView.setAdapter(this);
        listView.invalidate();
        notifyDataSetChanged();

    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        int layout = R.layout.element_player_episode;
        if (appCMSPresenter.getPlatformType() == AppCMSPresenter.PlatformType.TV)
            layout = R.layout.element_player_episode_tv;
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(layout, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        holder.thumbnailTitle.setText(episodes.get(position).getTitle());
        ViewCreator.setTypeFace(holder.itemView.getContext(), appCMSPresenter, appCMSPresenter.getJsonValueKeyMap(), new Component(), holder.thumbnailTitle);
//        String imageUrl = holder.itemView.getContext().getString(R.string.app_cms_image_with_resize_query,
//                seasons.get(0).getEpisodes().get(position).getGist().getImageGist().get_16x9(),
//                holder.thumbnailImage.getMeasuredWidth(),
//                holder.thumbnailImage.getMeasuredHeight());
//            Drawable placeholderDrawable = resizePlaceholder(holder.itemView.getContext().getResources().getDrawable(R.drawable.vid_image_placeholder_16x9), holder.thumbnailImage.getMeasuredWidth(),
//                holder.thumbnailImage.getMeasuredHeight(), holder.itemView.getContext());
        RequestOptions requestOptions = new RequestOptions()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
//                .override(holder.thumbnailImage.getMeasuredWidth(),
//                        holder.thumbnailImage.getMeasuredHeight())
                .fitCenter()
                /*.placeholder(placeholderDrawable)*/;
        Glide.with(holder.itemView.getContext())
                .asBitmap()
                .load(episodes.get(position).getGist().getImageGist().get_16x9())
                .apply(requestOptions)
                .into(new BitmapImageViewTarget(holder.thumbnailImage) {
                    @Override
                    public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                        super.onResourceReady(resource, transition);

                    }
                });
        holder.thumbnailImage.setBackground(CommonUtils.getTrayBorder(holder.thumbnailImage.getContext(), CommonUtils.getFocusBorderColor(holder.thumbnailImage.getContext(), appCMSPresenter), null));

        holder.itemView.setClickable(true);
        holder.thumbnailImage.setClickable(true);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                videoSelected.selectedVideoListener(episodes.get(position), position);
            }
        });
        holder.thumbnailImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                videoSelected.selectedVideoListener(episodes.get(position), position);
            }
        });
        holder.thumbnailImage.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                holder.thumbnailImage.setPadding(3, 3, 3, 3);
            } else {
                holder.thumbnailImage.setPadding(0, 0, 0, 0);
            }
        });
    }

    @Override
    public int getItemCount() {
        return episodes.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.thumbnailTitle)
        AppCompatTextView thumbnailTitle;
        @BindView(R.id.thumbnailImage)
        AppCompatImageView thumbnailImage;

        public MyViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    private Drawable resizePlaceholder(Drawable image, int width, int height, Context context) {
        Bitmap b = ((BitmapDrawable) image).getBitmap();
        Bitmap bitmapResized = Bitmap.createScaledBitmap(b, width, height, false);
        return new BitmapDrawable(context.getResources(), bitmapResized);
    }

}
