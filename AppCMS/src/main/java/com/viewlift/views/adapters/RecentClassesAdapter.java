package com.viewlift.views.adapters;

import android.graphics.drawable.PaintDrawable;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.viewlift.R;
import com.viewlift.models.data.appcms.api.ContentDatum;
import com.viewlift.models.data.appcms.api.RelatedVideo;
import com.viewlift.models.data.appcms.ui.page.Component;
import com.viewlift.presenters.AppCMSPresenter;

import java.util.List;

public class RecentClassesAdapter extends RecyclerView.Adapter<RecentClassesAdapter.ListItemViewHolder> {

    private List<RelatedVideo> list;
    private AppCMSPresenter appCMSPresenter;
    private Component mComponent;

    public RecentClassesAdapter(Component component, AppCMSPresenter appCMSPresenter, ContentDatum data) {
        this.appCMSPresenter = appCMSPresenter;
        this.mComponent = component;
        this.list = data.getGist().getRelatedVideos();
        for (int i = 0; i < this.list.size(); i++) {
            if (this.list.get(i) != null
                    && this.list.get(i).getGist() != null
                    && (this.list.get(i).getGist().isLiveStream()
                    || this.list.get(i).getGist().getScheduleEndDate() > 0
                    || this.list.get(i).getGist().getScheduleStartDate() > 0)) {
                this.list.remove(i);
            }
        }
    }

    @Override
    public ListItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ListItemViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.search_filter_result_item, parent, false));
    }

    @Override
    public void onBindViewHolder(ListItemViewHolder holder, int position) {
        if (list != null &&
                list.size() > 0 &&
                list.get(position) != null) {
            int imageHeight = holder.thumbnail.getHeight();
            int imageWidth = holder.thumbnail.getWidth();

            if (list.get(position).getGist() != null &&
                    list.get(position).getGist().getImageGist() != null &&
                    list.get(position).getGist().getImageGist().get_16x9() != null) {
                final String imageUrl = holder.itemView.getContext().getString(R.string.app_cms_image_with_resize_query,
                        list.get(position).getGist().getImageGist().get_16x9(),
                        imageWidth,
                        imageHeight);
                RequestOptions requestOptions = new RequestOptions()
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .placeholder(R.drawable.vid_image_placeholder_16x9)
                        .override(imageWidth, imageHeight);

                Glide.with(holder.itemView.getContext())
                        .asBitmap()
                        .load(imageUrl)
                        .apply(requestOptions)
                        .into(holder.thumbnail);
                holder.thumbnail.setScaleType(ImageView.ScaleType.FIT_XY);

            }

            if (list.get(position).getGist() != null &&
                    list.get(position).getGist().getBadgeImages() != null &&
                    list.get(position).getGist().getBadgeImages().get_16x9() != null) {
                Glide.with(holder.itemView.getContext())
                        .load(list.get(position).getGist().getBadgeImages().get_16x9())
                        .into(holder.badge);
            }

            String durationAndCategory = "";
            if (list.get(position).getTags() != null) {

                for (int i = 0; i < list.get(position).getTags().size(); i++) {
                    if (list.get(position).getTags().get(i) != null &&
                            list.get(position).getTags().get(i).getTagType() != null &&
                            list.get(position).getTags().get(i).getTitle() != null) {
                        if (list.get(position).getTags().get(i).getTagType().equals("classDuration")) {
                            durationAndCategory = list.get(position).getTags().get(i).getTitle();
                        }
                    }
                }
            }
            if (TextUtils.isEmpty(durationAndCategory)) {
                if (list.get(position).getGist() != null) {
                    durationAndCategory = list.get(position).getGist().getRuntime() + " MIN";
                }
            }

            if (list.get(position).getGist() != null &&
                    list.get(position).getGist().getPrimaryCategory() != null &&
                    list.get(position).getGist().getPrimaryCategory().getTitle() != null) {
                durationAndCategory = durationAndCategory + " • " + list.get(position).getGist().getPrimaryCategory().getTitle();
            }
            holder.duration.setText(durationAndCategory);

            holder.duration.setTextColor(appCMSPresenter.getGeneralTextColor());
            PaintDrawable gdDefault = new PaintDrawable(appCMSPresenter.getGeneralBackgroundColor());
            gdDefault.setCornerRadius(10f);
            gdDefault.setAlpha(85);

            holder.duration.setBackground(gdDefault);

            setAddToFavorites(holder);
            setVodPlayButton(holder);
            setBrandTitle(holder, position, gdDefault);
            setClassTitle(holder, position, gdDefault);
        }
    }

    private void setAddToFavorites(ListItemViewHolder holder) {
        holder.addToFavorites.setBackground(ContextCompat.getDrawable(holder.addToFavorites.getContext(), R.drawable.ic_bookmark_border_white_24dp));
    }

    private void setVodPlayButton(ListItemViewHolder holder) {
        holder.vodPlayButton.setBackground(ContextCompat.getDrawable(holder.vodPlayButton.getContext(), R.drawable.neo_video_detail_play));
    }

    private void setBrandTitle(ListItemViewHolder holder, int position, PaintDrawable gdDefault) {
        String tagBrandTitle = "";
        if (list != null &&
                list.size() > 0 &&
                list.get(position).getTags() != null) {
            for (int i = 0; i < list.get(position).getTags().size(); i++) {
                if (list.get(position).getTags().get(i) != null &&
                        list.get(position).getTags().get(i).getTagType() == null &&
                        list.get(position).getTags().get(i).getTitle() != null) {
                    tagBrandTitle = list.get(position).getTags().get(i).getTitle();
                    break;
                }
            }
        }
        holder.tagBrandTitleBrowse.setText(tagBrandTitle);
        holder.tagBrandTitleBrowse.setTextColor(appCMSPresenter.getGeneralTextColor());
        holder.tagBrandTitleBrowse.setBackground(gdDefault);
    }

    private void setClassTitle(ListItemViewHolder holder, int position, PaintDrawable gdDefault) {
        String tagClassTitle = "";
        if (list != null &&
                list.size() > 0 &&
                list.get(position).getTags() != null) {
            for (int i = 0; i < list.get(position).getTags().size(); i++) {
                if (list.get(position).getTags().get(i) != null &&
                        list.get(position).getTags().get(i).getTagType() != null &&
                        list.get(position).getTags().get(i).getTitle() != null) {
                    if (list.get(position).getTags().get(i).getTagType().equals("brand")) {
                        tagClassTitle = list.get(position).getTags().get(i).getTitle();
                        break;
                    }
                }
            }
        }
        holder.tagClassTitleBrowse.setText(tagClassTitle);
        holder.tagClassTitleBrowse.setTextColor(appCMSPresenter.getGeneralTextColor());
        holder.tagClassTitleBrowse.setBackground(gdDefault);
    }

    @Override
    public int getItemCount() {
        if (list.size() > 4) {
            return 4;
        }
        return list.size();
    }

    public class ListItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView duration, tagBrandTitleBrowse, tagClassTitleBrowse;
        ImageView badge, thumbnail, addToFavorites, vodPlayButton;

        ListItemViewHolder(View itemView) {
            super(itemView);
            duration = itemView.findViewById(R.id.textViewClassDuration);
            badge = itemView.findViewById(R.id.imageViewSearchFilterBadge);
            thumbnail = itemView.findViewById(R.id.imageViewSearchFilter);
            addToFavorites = itemView.findViewById(R.id.addToFavorites);
            vodPlayButton = itemView.findViewById(R.id.vodPlayButton);
            tagBrandTitleBrowse = itemView.findViewById(R.id.tagBrandTitleBrowse);
            tagClassTitleBrowse = itemView.findViewById(R.id.tagClassTitleBrowse);

//            if (mComponent != null) {
//                int width = (int) BaseView.getGridWidth(itemView.getContext(), mComponent.getLayout(), ViewGroup.LayoutParams.WRAP_CONTENT);
//                int height = (int) BaseView.getGridHeight(itemView.getContext(), mComponent.getLayout(), ViewGroup.LayoutParams.WRAP_CONTENT);
//                int padding = BaseView.dpToPx(R.dimen.search_item_padding_default, itemView.getContext());
//
//                ConstraintLayout.LayoutParams lp= new ConstraintLayout.LayoutParams(width,height);
//                lp.setMargins(0, padding, padding, padding);
//                itemView.setLayoutParams(lp);
//
//            }
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            String action = view.getContext().getString(R.string.app_cms_action_detailvideopage_key);
            ContentDatum contentDatum = new ContentDatum();
            contentDatum.setGist(list.get(getAdapterPosition()).getGist());
            appCMSPresenter.launchButtonSelectedAction(contentDatum.getGist().getPermalink(),
                    action,
                    contentDatum.getGist().getTitle(),
                    null,
                    contentDatum,
                    false,
                    0,
                    null);
        }
    }
}