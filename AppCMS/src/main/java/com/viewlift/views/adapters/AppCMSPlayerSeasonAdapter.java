package com.viewlift.views.adapters;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.viewlift.R;
import com.viewlift.models.data.appcms.api.ContentDatum;
import com.viewlift.presenters.AppCMSPresenter;

import java.util.List;

/*
 * Created by viewlift on 5/5/17.
 */

public class AppCMSPlayerSeasonAdapter extends RecyclerView.Adapter<AppCMSPlayerSeasonAdapter.MyViewHolder> {


    protected Context mContext;
    protected AppCMSPresenter appCMSPresenter;

    private List<ContentDatum> adapterData;


    public AppCMSPlayerSeasonAdapter(Context context,
                                     AppCMSPresenter appCMSPresenter,
                                     List<ContentDatum> adapterData) {
        this.mContext = context;
        this.appCMSPresenter = appCMSPresenter;
        this.adapterData = adapterData;

    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent,
                                           int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.video_player_episodes_item, parent, false);

        MyViewHolder myViewHolder = new MyViewHolder(view);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int listPosition) {

        holder.episodeTitle.setText(adapterData.get(listPosition).getGist().getTitle());
        loadImage(mContext, adapterData.get(listPosition).getGist().getImageGist().get_16x9(), holder.thumbnailImage);
    }

    @Override
    public int getItemCount() {
        return adapterData.size();
    }

    private void loadImage(Context context, String url, ImageView imageView) {
        Glide.with(context)
                .load(Uri.decode(url))
                .apply(new RequestOptions().override(imageView.getWidth(), imageView.getHeight()).placeholder(R.drawable.vid_image_placeholder_land));
    }


    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView episodeTitle;
        ImageView thumbnailImage;

        public MyViewHolder(View itemView) {
            super(itemView);
            this.episodeTitle = itemView.findViewById(R.id.app_cms_episode_title);
            this.thumbnailImage = itemView.findViewById(R.id.thumbnail_image);
        }
    }

}
