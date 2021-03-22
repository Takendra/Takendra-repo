package com.viewlift.views.adapters;

import android.content.Context;
import android.graphics.Color;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.viewlift.AppCMSApplication;
import com.viewlift.R;
import com.viewlift.models.data.appcms.search.AppCMSSearchResult;
import com.viewlift.models.data.appcms.ui.main.LocalisedStrings;
import com.viewlift.presenters.AppCMSPresenter;
import com.viewlift.utils.CommonUtils;
import com.viewlift.views.customviews.BaseView;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import rx.functions.Action1;


/*
 * Created by viewlift on 6/12/17.
 */

public class AppCMSSearchItemAdapter extends RecyclerView.Adapter<AppCMSSearchItemAdapter.ViewHolder> {
    private static final String TAG = "AppCMSSearchAdapter";

    private static final float STANDARD_MOBILE_WIDTH_PX = 375f;
    private static final float STANDARD_MOBILE_HEIGHT_PX = 667f;

    private static final float STANDARD_TABLET_WIDTH_PX = 768f;
    private static final float STANDARD_TABLET_HEIGHT_PX = 1024f;
    private static final float IMAGE_WIDTH_MOBILE = 111f;
    private static final float IMAGE_HEIGHT_MOBILE = 164f;
    private static final float IMAGE_WIDTH_TABLET_LANDSCAPE = 154f;
    private static final float IMAGE_HEIGHT_TABLET_LANDSCAPE = 240f;
    private static final float IMAGE_WIDTH_TABLET_PORTRAIT = 154f;
    private static final float IMAGE_HEIGHT_TABLET_PORTRAIT = 240f;
    private static final float TEXTSIZE_MOBILE = 11f;
    private static final float TEXTSIZE_TABLET_LANDSCAPE = 14f;
    private static final float TEXTSIZE_TABLET_PORTRAIT = 14f;
    private static final float TEXT_WIDTH_MOBILE = IMAGE_WIDTH_MOBILE;
    private static final float TEXT_WIDTH_TABLET_LANDSCAPE = 154f;
    private static final float TEXT_WIDTH_TABLET_PORTRAIT = 154f;
    private static final float TEXT_TOPMARGIN_MOBILE = 170f;
    private static final float TEXT_TOPMARGIN_TABLET_LANDSCAPE = 242f;
    private static final float TEXT_TOPMARGIN_TABLET_PORTRAIT = 242f;
    private static float DEVICE_WIDTH;
    private static int DEVICE_HEIGHT;
    @Inject
    AppCMSPresenter appCMSPresenter;
    @Inject
    LocalisedStrings localisedStrings;
    private final Context context;
    private Action1 action;
    private int imageWidth = 0;
    private int imageHeight = 0;
    private int textSize = 0;
    private int textWidth = 0;
    private int textTopMargin = 0;
    private List<AppCMSSearchResult> appCMSSearchResults;
    int placeholder = R.drawable.vid_image_placeholder_port;
    private List<AppCMSSearchResult> seriesList = new ArrayList<>();
    private List<AppCMSSearchResult> audioList = new ArrayList<>();
    private List<AppCMSSearchResult> articleList = new ArrayList<>();
    private List<AppCMSSearchResult> photosList = new ArrayList<>();

    private List<AppCMSSearchResult> videoList = new ArrayList<>();
    private List<List<AppCMSSearchResult>> listData = new ArrayList<>();
    private AppCMSSearchInnerItemAdapter appCMSSearchInnerItemAdapter;

    public AppCMSSearchItemAdapter(Context context, List<AppCMSSearchResult> appCMSSearchResults) {
        this.context = context;
        ((AppCMSApplication) this.context.getApplicationContext()).getAppCMSPresenterComponent().inject(this);
        this.appCMSSearchResults = appCMSSearchResults;
        DEVICE_WIDTH = context.getResources().getDisplayMetrics().widthPixels;
        DEVICE_HEIGHT = context.getResources().getDisplayMetrics().heightPixels;
        this.imageWidth = (int) getImageWidth(context);
        this.imageHeight = (int) getImageHeight(context);
        this.textSize = (int) getTextSize(context);
        this.textWidth = (int) getTextWidth(context);
        this.textTopMargin = (int) getTextTopMargin(context);

        filterData(appCMSSearchResults);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.search_result_item_parent,
                viewGroup,
                false);
        return new ViewHolder(view);
    }

    @SuppressWarnings("unchecked")
    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, int i) {

        appCMSSearchInnerItemAdapter = new AppCMSSearchInnerItemAdapter(context, appCMSPresenter, listData.get(i));
        LinearLayoutManager layoutManager
                = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);
        viewHolder.recyclerView.setLayoutManager(layoutManager);
        viewHolder.recyclerView.setAdapter(appCMSSearchInnerItemAdapter);
        appCMSSearchInnerItemAdapter.setData(listData.get(i));
        setTrayTitle(viewHolder.parentTitle, i);
    }

    private void setTrayTitle(TextView view, int pos) {
        if (listData.get(pos).get(0).getGist() != null && listData.get(pos).get(0).getGist().getContentType() != null) {
            String contentType = listData.get(pos).get(0).getGist().getContentType();
            if (contentType.equalsIgnoreCase(context.getString(R.string.app_cms_series_content_type)) ||
                    contentType.equalsIgnoreCase(context.getString(R.string.content_type_show))) {
                appCMSPresenter.setMoreIconAvailable();
                view.setText(localisedStrings.getSeriesHeaderText());
            } else if (contentType.equalsIgnoreCase(context.getString(R.string.app_cms_video_content_type))) {
                appCMSPresenter.setMoreIconAvailable();
                view.setText(localisedStrings.getVideosHeaderText());
            } else if (contentType.equalsIgnoreCase(context.getString(R.string.content_type_audio))) {
                appCMSPresenter.setMoreIconAvailable();
                view.setText(localisedStrings.getAudioHeaderText());
            } else if (contentType.equalsIgnoreCase(context.getString(R.string.app_cms_article_key_type))) {
                appCMSPresenter.setMoreIconAvailable();
                view.setText(localisedStrings.getArticleHeaderText());
            } else if ((contentType.equalsIgnoreCase(context.getString(R.string.app_cms_photo_gallery_key_type)) ||
                    contentType.equalsIgnoreCase(context.getString(R.string.app_cms_photo_image_key_type)))) {
                appCMSPresenter.setMoreIconAvailable();
                view.setText(localisedStrings.getGalleryHeaderText());
            }
        }
        view.setTextColor(Color.parseColor(CommonUtils.getColor(context,
                appCMSPresenter.getAppCMSMain().getBrand().getGeneral().getBlockTitleColor())));
    }

    @Override
    public int getItemCount() {
        return listData != null ? listData.size() : 0;
    }

    private void filterData(List<AppCMSSearchResult> appCMSSearchResults) {
        if (appCMSSearchResults != null) {
            for (int i = 0; i < appCMSSearchResults.size(); i++) {

                if (appCMSSearchResults.get(i).getGist() != null
                        && appCMSSearchResults.get(i).getGist().getContentType() != null
                        && (appCMSSearchResults.get(i).getGist().getContentType().toLowerCase().equalsIgnoreCase(context.getString(R.string.app_cms_article_key_type).toLowerCase())
                )) {
                    articleList.add(appCMSSearchResults.get(i));
                } else if (appCMSSearchResults.get(i).getGist() != null
                        && appCMSSearchResults.get(i).getGist().getContentType() != null
                        && (appCMSSearchResults.get(i).getGist().getContentType().equalsIgnoreCase(context.getString(R.string.content_type_series))
                        || appCMSSearchResults.get(i).getGist().getContentType().equalsIgnoreCase(context.getString(R.string.content_type_show)))) {
                    seriesList.add(appCMSSearchResults.get(i));
                } else if (appCMSSearchResults.get(i).getGist() != null
                        && appCMSSearchResults.get(i).getGist().getContentType() != null
                        && appCMSSearchResults.get(i).getGist().getContentType().equalsIgnoreCase(context.getString(R.string.content_type_audio))) {
                    audioList.add(appCMSSearchResults.get(i));
                } else if (appCMSSearchResults.get(i).getGist() != null
                        && appCMSSearchResults.get(i).getGist().getContentType() != null
                        && (appCMSSearchResults.get(i).getGist().getContentType().toLowerCase().equalsIgnoreCase(context.getString(R.string.app_cms_photo_gallery_key_type).toLowerCase())
                        || (appCMSSearchResults.get(i).getGist().getContentType().toLowerCase().equalsIgnoreCase(context.getString(R.string.app_cms_photo_image_key_type).toLowerCase())
                )
                )) {
                    photosList.add(appCMSSearchResults.get(i));
                } else {
                    videoList.add(appCMSSearchResults.get(i));
                }

            }
        }

        if (articleList.size() > 0)
            listData.add(articleList);
        if (audioList.size() > 0)
            listData.add(audioList);
        if (photosList.size() > 0)
            listData.add(photosList);
        if (seriesList.size() > 0)
            listData.add(seriesList);
        if (videoList.size() > 0)
            listData.add(videoList);

    }

    public void setData(List<AppCMSSearchResult> results) {
        appCMSSearchResults = results;
        listData.clear();
        seriesList.clear();
        videoList.clear();
        audioList.clear();
        photosList.clear();
        articleList.clear();

        filterData(results);
        notifyDataSetChanged();
    }

    private float getImageWidth(Context context) {
        if (BaseView.isTablet(context)) {
            if (BaseView.isLandscape(context)) {
                return DEVICE_WIDTH * (IMAGE_WIDTH_TABLET_LANDSCAPE / STANDARD_TABLET_HEIGHT_PX);
            } else {
                return DEVICE_WIDTH * (IMAGE_WIDTH_TABLET_PORTRAIT / STANDARD_TABLET_WIDTH_PX);
            }
        }
        return DEVICE_WIDTH * (IMAGE_WIDTH_MOBILE / STANDARD_MOBILE_WIDTH_PX);
    }

    private float getTextWidth(Context context) {
        if (BaseView.isTablet(context)) {
            if (BaseView.isLandscape(context)) {
                return DEVICE_WIDTH * (TEXT_WIDTH_TABLET_LANDSCAPE / STANDARD_TABLET_HEIGHT_PX);
            } else {
                return DEVICE_WIDTH * (TEXT_WIDTH_TABLET_PORTRAIT / STANDARD_TABLET_WIDTH_PX);
            }
        }
        return DEVICE_WIDTH * (TEXT_WIDTH_MOBILE / STANDARD_MOBILE_WIDTH_PX);
    }

    private float getImageHeight(Context context) {
        if (BaseView.isTablet(context)) {
            if (BaseView.isLandscape(context)) {
                return DEVICE_HEIGHT * (IMAGE_HEIGHT_TABLET_LANDSCAPE / STANDARD_TABLET_WIDTH_PX);
            } else {
                return DEVICE_HEIGHT * (IMAGE_HEIGHT_TABLET_PORTRAIT / STANDARD_TABLET_HEIGHT_PX);
            }
        }
        return DEVICE_HEIGHT * (IMAGE_HEIGHT_MOBILE / STANDARD_MOBILE_HEIGHT_PX);
    }

    private float getTextTopMargin(Context context) {
        if (BaseView.isTablet(context)) {
            if (BaseView.isLandscape(context)) {
                return DEVICE_HEIGHT * (TEXT_TOPMARGIN_TABLET_LANDSCAPE / STANDARD_TABLET_WIDTH_PX);
            } else {
                return DEVICE_HEIGHT * (TEXT_TOPMARGIN_TABLET_PORTRAIT / STANDARD_TABLET_HEIGHT_PX);
            }
        }
        return DEVICE_HEIGHT * (TEXT_TOPMARGIN_MOBILE / STANDARD_MOBILE_HEIGHT_PX);
    }

    private float getTextSize(Context context) {
        if (BaseView.isTablet(context)) {
            if (BaseView.isLandscape(context)) {
                return TEXTSIZE_TABLET_LANDSCAPE;
            } else {
                return TEXTSIZE_TABLET_PORTRAIT;
            }
        }
        return TEXTSIZE_MOBILE;
    }

    public void handleProgress(Action1 action) {
        this.action = action;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        RecyclerView recyclerView;
        View view;
        TextView parentTitle;

        public ViewHolder(View view) {
            super(view);
            this.view = view;
            this.parentTitle = view.findViewById(R.id.parentTitle);
            this.recyclerView = view.findViewById(R.id.parentRecyclerView);
            this.parentTitle.setTextColor(appCMSPresenter.getGeneralTextColor());
        }
    }
}