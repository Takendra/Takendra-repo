package com.viewlift.views.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.PorterDuff;

import androidx.recyclerview.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.viewlift.audio.playback.AudioPlaylistHelper;
import com.viewlift.R;
import com.viewlift.models.data.appcms.api.ContentDatum;
import com.viewlift.models.data.appcms.api.Gist;
import com.viewlift.models.data.appcms.search.AppCMSSearchResult;
import com.viewlift.presenters.AppCMSPresenter;
import com.viewlift.views.customviews.BaseView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import rx.Observable;
import rx.functions.Action1;

/*
 * Created by viewlift on 6/12/17.
 */

public class AppCMSSearchInnerItemAdapter extends RecyclerView.Adapter<AppCMSSearchInnerItemAdapter.ViewHolder> {
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
    private final AppCMSPresenter appCMSPresenter;
    private final Context context;
    private Action1 action;
    private int imageWidth = 0;
    private int imageHeight = 0;
    private int textSize = 0;
    private int textWidth = 0;
    private int textTopMargin = 0;
    private List<AppCMSSearchResult> appCMSSearchResults;
    int placeholder = R.drawable.vid_image_placeholder_port;

    public AppCMSSearchInnerItemAdapter(Context context, AppCMSPresenter appCMSPresenter,
                                        List<AppCMSSearchResult> appCMSSearchResults) {
        this.context = context;
        this.appCMSPresenter = appCMSPresenter;
        this.appCMSSearchResults = appCMSSearchResults;
        DEVICE_WIDTH = context.getResources().getDisplayMetrics().widthPixels;
        DEVICE_HEIGHT = context.getResources().getDisplayMetrics().heightPixels;
        this.imageWidth = (int) getImageWidth(context);
        this.imageHeight = (int) getImageHeight(context);
        this.textSize = (int) getTextSize(context);
        this.textWidth = (int) getTextWidth(context);
        this.textTopMargin = (int) getTextTopMargin(context);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.search_result_item,
                viewGroup,
                false);
        return new ViewHolder(view, imageWidth, imageHeight, textSize, textWidth, textTopMargin);
    }

    @SuppressWarnings("unchecked")
    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, int i) {
        final int adapterPosition = i;
        String contentType = "";

        viewHolder.parentLayout.setOnClickListener(v -> {
            if (action != null) {
                Observable.just("progress").subscribe(action);
            }
            context.sendBroadcast(new Intent(AppCMSPresenter.PRESENTER_PAGE_LOADING_ACTION));

            if (appCMSSearchResults.get(adapterPosition).getGist() != null && appCMSSearchResults.get(adapterPosition).getGist().getMediaType() != null
                    && appCMSSearchResults.get(adapterPosition).getGist().getMediaType().toLowerCase().contains(context.getString(R.string.app_cms_article_key_type).toLowerCase())) {
                appCMSPresenter.setCurrentArticleIndex(-1);
                appCMSPresenter.navigateToArticlePage(appCMSSearchResults.get(adapterPosition).getGist().getId(), appCMSSearchResults.get(adapterPosition).getGist().getTitle(), false, null, false);
                return;
            } else if (appCMSSearchResults.get(adapterPosition).getGist() != null && appCMSSearchResults.get(adapterPosition).getGist().getMediaType() != null
                    && appCMSSearchResults.get(adapterPosition).getGist().getMediaType().toLowerCase().contains(context.getString(R.string.app_cms_photo_gallery_key_type).toLowerCase())) {
                appCMSPresenter.navigateToPhotoGalleryPage(appCMSSearchResults.get(adapterPosition).getGist().getId(), appCMSSearchResults.get(adapterPosition).getGist().getTitle(), null, false);
                return;

            } else if (appCMSSearchResults.get(adapterPosition).getGist() != null && appCMSSearchResults.get(adapterPosition).getGist().getMediaType() != null
                    && appCMSSearchResults.get(adapterPosition).getGist().getMediaType().toLowerCase().contains(context.getString(R.string.content_type_event).toLowerCase())) {
                appCMSPresenter.getCurrentActivity().sendBroadcast(new Intent(AppCMSPresenter
                        .PRESENTER_PAGE_LOADING_ACTION));
                appCMSPresenter.navigateToEventDetailPage(appCMSSearchResults.get(adapterPosition).getGist().getPermalink());
                return;

            }
            /*get audio details on tray click item and play song*/
            if (appCMSSearchResults.get(adapterPosition).getGist() != null &&
                    appCMSSearchResults.get(adapterPosition).getGist().getMediaType() != null &&
                    appCMSSearchResults.get(adapterPosition).getGist().getMediaType().toLowerCase().contains(context.getString(R.string.media_type_audio).toLowerCase()) &&
                    appCMSSearchResults.get(adapterPosition).getGist().getContentType() != null &&
                    appCMSSearchResults.get(adapterPosition).getGist().getContentType().toLowerCase().contains(context.getString(R.string.content_type_audio).toLowerCase())) {
                List<String> audioPlaylistId = new ArrayList<String>();
                audioPlaylistId.add(appCMSSearchResults.get(adapterPosition).getGist().getId());
                AudioPlaylistHelper.getInstance().setCurrentPlaylistData(null);
                AudioPlaylistHelper.getInstance().setPlaylist(audioPlaylistId);
                appCMSPresenter.getCurrentActivity().sendBroadcast(new Intent(AppCMSPresenter
                        .PRESENTER_PAGE_LOADING_ACTION));
                AudioPlaylistHelper.getInstance().playAudioOnClickItem(appCMSSearchResults.get(adapterPosition).getGist().getId(), 0);
                return;
            }
            /*Get playlist data and open playlist page*/
            if (appCMSSearchResults.get(adapterPosition).getGist() != null && appCMSSearchResults.get(adapterPosition).getGist().getMediaType() != null
                    && appCMSSearchResults.get(adapterPosition).getGist().getMediaType().toLowerCase().contains(context.getString(R.string.media_type_playlist).toLowerCase())) {
                appCMSPresenter.navigateToPlaylistPage(appCMSSearchResults.get(adapterPosition).getGist().getId());
                return;
            }
            String permalink="";
            String action="";
            String title="";

            try {
                if(appCMSSearchResults.get(adapterPosition).getGist()!=null) {
                     permalink = appCMSSearchResults.get(adapterPosition).getGist().getPermalink();
                     action = viewHolder.view.getContext().getString(R.string.app_cms_action_detailvideopage_key);
                    if (permalink.contains(viewHolder.view.getContext().getString(R.string.app_cms_shows_deeplink_path_name)) ||
                            appCMSSearchResults.get(adapterPosition).getGist().getContentType().equals(context.getString(R.string.app_cms_episodic_key_type))) {
                        action = viewHolder.view.getContext().getString(R.string.app_cms_action_showvideopage_key);
                    }
                     title = appCMSSearchResults.get(adapterPosition).getGist().getTitle();
                }else if(appCMSSearchResults.get(adapterPosition).getPermalink()!=null &&
                        appCMSSearchResults.get(adapterPosition).getMediaType()!=null &&
                        appCMSSearchResults.get(adapterPosition).getTitle()!=null) {
                    permalink = appCMSSearchResults.get(adapterPosition).getPermalink();
                    action = viewHolder.view.getContext().getString(R.string.app_cms_action_detailbundlepage_key);
                    if (permalink.contains(viewHolder.view.getContext().getString(R.string.app_cms_page_path_bundle)) ||
                            appCMSSearchResults.get(adapterPosition).getGist().getMediaType().equals(context.getString(R.string.app_cms_bundle_key_type))) {
                        action = viewHolder.view.getContext().getString(R.string.app_cms_action_detailbundlepage_key);
                    }
                     title = appCMSSearchResults.get(adapterPosition).getTitle();
                }
                //Log.d(TAG, "Launching " + permalink + ":" + action);
                if (!appCMSPresenter.launchButtonSelectedAction(permalink,
                        action,
                        title,
                        null,
                        null,
                        true,
                        0,
                        null)) {
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

//        if (appCMSSearchResults.get(adapterPosition)!=null && appCMSSearchResults.get(adapterPosition).getGist()!=null &&
//                 appCMSSearchResults.get(adapterPosition).getGist().getContentType() != null) {
//            contentType = appCMSSearchResults.get(adapterPosition).getGist().getContentType();
//
//        }
        if (appCMSSearchResults.get(adapterPosition).getGist() != null &&
                !TextUtils.isEmpty(appCMSSearchResults.get(adapterPosition).getGist().getTitle())) {
            viewHolder.filmTitle.setText(appCMSSearchResults.get(adapterPosition).getGist().getTitle());
        }else  if (appCMSSearchResults.get(adapterPosition).getTitle() != null &&
                !TextUtils.isEmpty(appCMSSearchResults.get(adapterPosition).getTitle())) {
            viewHolder.filmTitle.setText(appCMSSearchResults.get(adapterPosition).getTitle());
        }

//        if (appCMSSearchResults.get(adapterPosition).getGist() != null &&
//                appCMSSearchResults.get(adapterPosition).getGist().getPosterImageUrl() != null &&
//                !TextUtils.isEmpty(appCMSSearchResults.get(adapterPosition).getGist().getPosterImageUrl())) {
//
//            final String imageUrl = viewHolder.view.getContext().getString(R.string.app_cms_image_with_resize_query,
//                    appCMSSearchResults.get(adapterPosition).getGist().getPosterImageUrl(),
//                    imageWidth,
//                    imageHeight);
//            RequestOptions requestOptions = new RequestOptions().placeholder(placeholder);
//
//            Glide.with(viewHolder.view.getContext())
//                    .load(imageUrl).apply(requestOptions)
//                    .into(viewHolder.filmThumbnail);
//        }

        if (appCMSSearchResults.get(adapterPosition).getGist() != null &&
                appCMSSearchResults.get(adapterPosition).getGist().getContentType() != null &&
                appCMSSearchResults.get(adapterPosition).getGist().getContentType().toLowerCase().contains(context.getString(R.string.content_type_audio).toLowerCase()) &&
                appCMSSearchResults.get(adapterPosition).getGist().getImageGist() != null) {


            String url = "";
            if (appCMSSearchResults.get(adapterPosition).getGist().getImageGist().get_1x1() != null) {
                url = appCMSSearchResults.get(adapterPosition).getGist().getImageGist().get_1x1();
            } else if (appCMSSearchResults.get(adapterPosition).getGist().getImageGist().get_16x9() != null) {
                url = appCMSSearchResults.get(adapterPosition).getGist().getImageGist().get_16x9();
            } else if (appCMSSearchResults.get(adapterPosition).getGist().getImageGist().get_3x4() != null) {
                url = appCMSSearchResults.get(adapterPosition).getGist().getImageGist().get_3x4();
            } else if (appCMSSearchResults.get(adapterPosition).getGist().getImageGist().get_4x3() != null) {
                url = appCMSSearchResults.get(adapterPosition).getGist().getImageGist().get_4x3();
            }

            //if (appCMSPresenter.getIsMoreOptionsAvailable()) {
                applySportsStyleDefault(viewHolder, createEmptySquareBitmap(), false);
            //}
            placeholder = R.drawable.vid_image_placeholder_square;

            if (!TextUtils.isEmpty(url)) {
                loadImageURL(viewHolder.filmThumbnail, viewHolder.view.getContext(), url);
            } else {
                viewHolder.filmThumbnail.setImageResource(placeholder);
            }

        } else if (appCMSSearchResults.get(adapterPosition).getGist() != null &&
                appCMSSearchResults.get(adapterPosition).getGist().getLandscapeImageUrl() != null &&

                !TextUtils.isEmpty(appCMSSearchResults.get(adapterPosition).getGist().getLandscapeImageUrl())) {
            final String imageUrl = appCMSSearchResults.get(adapterPosition).getGist().getLandscapeImageUrl();

//            RequestOptions requestOptions = new RequestOptions().placeholder(placeholder);
//
//            Glide.with(viewHolder.view.getContext())
//                    .load(imageUrl).apply(requestOptions)
//                    .into(viewHolder.filmThumbnail);

            //if (appCMSPresenter.getIsMoreOptionsAvailable()) {
                applySportsStyleDefault(viewHolder, createEmptyBitmap(), false);
            //}

            loadImageURL(viewHolder.filmThumbnail, viewHolder.view.getContext(), imageUrl);
        } /*else if (appCMSSearchResults.get(adapterPosition).getContentDetails() != null &&
                appCMSSearchResults.get(adapterPosition).getContentDetails().getPosterImage() != null &&
                !TextUtils.isEmpty(appCMSSearchResults.get(adapterPosition).getContentDetails().getPosterImage().getUrl())) {

            final String imageUrl = viewHolder.view.getContext().getString(R.string.app_cms_image_with_resize_query,
                    appCMSSearchResults.get(adapterPosition).getContentDetails().getPosterImage().getUrl(),
                    imageWidth,
                    imageHeight);
            RequestOptions requestOptions = new RequestOptions().placeholder(placeholder);

            Glide.with(viewHolder.view.getContext())
                    .load(imageUrl).apply(requestOptions)
                    .into(viewHolder.filmThumbnail);
        }*/ else if (appCMSSearchResults.get(adapterPosition).getContentDetails() != null &&
                appCMSSearchResults.get(adapterPosition).getContentDetails().getVideoImage() != null &&
                appCMSSearchResults.get(adapterPosition).getContentDetails().getVideoImage().getSecureUrl() != null) {
            //if (appCMSPresenter.getIsMoreOptionsAvailable()) {
                applySportsStyleDefault(viewHolder, createEmptyBitmap(), false);
            //}
            String url = appCMSSearchResults.get(adapterPosition).getContentDetails().getVideoImage().getSecureUrl();
            loadImageURL(viewHolder.filmThumbnail, viewHolder.view.getContext(), url);
            /*RequestOptions requestOptions = new RequestOptions().placeholder(placeholder).fitCenter();

            final String imageUrl = viewHolder.view.getContext().getString(R.string.app_cms_image_with_resize_query,
                    appCMSSearchResults.get(adapterPosition).getContentDetails().getVideoImage().getSecureUrl(),
                    imageWidth,
                    imageHeight);
            Glide.with(viewHolder.view.getContext())
                    .asBitmap().apply(requestOptions)
                    .load(imageUrl)
                    .listener(new RequestListener<Bitmap>() {
                        @Override
                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Bitmap> target, boolean isFirstResource) {
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(Bitmap resource, Object model, Target<Bitmap> target, DataSource dataSource, boolean isFirstResource) {
                            if (appCMSPresenter.getIsMoreOptionsAvailable()) {
                                Bitmap bitmap = resource;
                                viewHolder.filmThumbnail.setLayoutParams(new RelativeLayout.LayoutParams(bitmap.getWidth(), bitmap.getHeight()));
                                viewHolder.filmThumbnail.setImageBitmap(bitmap);

                                viewHolder.titleLayout.setLayoutParams(new FrameLayout.LayoutParams(bitmap.getWidth(), ViewGroup.LayoutParams.WRAP_CONTENT));

                                FrameLayout.LayoutParams titleLayoutParams = (FrameLayout.LayoutParams) viewHolder.titleLayout.getLayoutParams();
                                titleLayoutParams.setMargins(0, bitmap.getHeight(), 0, 0);
                                viewHolder.titleLayout.setLayoutParams(titleLayoutParams);

                                //viewHolder.gridOptions.setVisibility(View.VISIBLE);
                            }
                            return false;
                        }
                    })
                    .into(viewHolder.filmThumbnail);*/

        } else if (appCMSSearchResults.get(adapterPosition).getGist() != null &&
                appCMSSearchResults.get(adapterPosition).getGist().getImageGist() != null &&
                appCMSSearchResults.get(adapterPosition).getGist().getImageGist().get_16x9() != null) {

            String url = "";
            if (appCMSSearchResults.get(adapterPosition).getGist().getImageGist().get_16x9() != null) {
                url = appCMSSearchResults.get(adapterPosition).getGist().getImageGist().get_16x9();
            } else if (appCMSSearchResults.get(adapterPosition).getGist().getImageGist().get_3x4() != null) {
                url = appCMSSearchResults.get(adapterPosition).getGist().getImageGist().get_3x4();
            } else if (appCMSSearchResults.get(adapterPosition).getGist().getImageGist().get_1x1() != null) {
                url = appCMSSearchResults.get(adapterPosition).getGist().getImageGist().get_1x1();
            } else if (appCMSSearchResults.get(adapterPosition).getGist().getImageGist().get_4x3() != null) {
                url = appCMSSearchResults.get(adapterPosition).getGist().getImageGist().get_4x3();
            }


            //if (appCMSPresenter.getIsMoreOptionsAvailable()) {
                applySportsStyleDefault(viewHolder, createEmptyBitmap(), false);
            //}

            loadImageURL(viewHolder.filmThumbnail, viewHolder.view.getContext(), url);

        }else{
            String url = "";

            applySportsStyleDefault(viewHolder, createEmptyBitmap(), false);


            loadImageURL(viewHolder.filmThumbnail, viewHolder.view.getContext(), url);

        }


        if (appCMSSearchResults.get(adapterPosition).getGist() != null &&
                appCMSSearchResults.get(adapterPosition).getGist().getMediaType() != null
                && (appCMSSearchResults.get(adapterPosition).getGist().getMediaType().toLowerCase().contains(context.getString(R.string.app_cms_article_key_type).toLowerCase())
                || appCMSSearchResults.get(adapterPosition).getGist().getMediaType().toLowerCase().contains(context.getString(R.string.app_cms_photo_gallery_key_type).toLowerCase())
                || appCMSSearchResults.get(adapterPosition).getGist().getMediaType().toLowerCase().contains(context.getString(R.string.content_type_event).toLowerCase()))) {
            if (appCMSPresenter.getIsMoreOptionsAvailable()) {
                applySportsStyleDefault(viewHolder, createEmptyBitmap(), false);
            }
        }
        viewHolder.gridOptions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String permalink = appCMSSearchResults.get(adapterPosition).getGist().getPermalink();
                String action = viewHolder.view.getContext().getString(R.string.app_cms_action_open_option_dialog);
                String title = appCMSSearchResults.get(adapterPosition).getGist().getTitle();
                ContentDatum contentDatum = new ContentDatum();
                contentDatum.setGist(appCMSSearchResults.get(adapterPosition).getGist());
                appCMSPresenter.launchButtonSelectedAction(permalink,
                        action,
                        title,
                        null,
                        contentDatum,
                        true,
                        0,
                        null);
            }
        });
    }

    @Override
    public int getItemCount() {
        return appCMSSearchResults != null ? appCMSSearchResults.size() : 0;
    }

    public void loadImageURL(View view, Context context, String url) {
        imageWidth = createEmptyBitmap().getWidth();
        imageHeight = createEmptyBitmap().getHeight();
//        if (appCMSPresenter.getIsMoreOptionsAvailable()) {
//            imageWidth = createEmptyBitmap().getWidth();
//            imageHeight = createEmptyBitmap().getHeight();
//        } else {
//            imageWidth = (int) getImageWidth(context);
//            imageHeight = (int) getImageHeight(context);
//        }

        String imageUrl = context.getString(R.string.app_cms_image_with_resize_query,
                url,
                imageWidth,
                imageHeight);
        RequestOptions requestOptions = new RequestOptions().placeholder(placeholder).fitCenter();

        Glide.with(context)
                .load(imageUrl).apply(requestOptions)
                .into((ImageView) view);
    }


    public void setData(List<AppCMSSearchResult> results) {
        appCMSSearchResults = results;
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
        View view;
        FrameLayout parentLayout;
        ImageView filmThumbnail;
        ImageView gridOptions;
        TextView filmTitle, thumbnailInfo;
        RelativeLayout titleLayout;
        RelativeLayout filmThumbnailLayout;

        public ViewHolder(View view,
                          int imageWidth,
                          int imageHeight,
                          int textSize,
                          int textWidth,
                          int textTopMargin) {
            super(view);
            this.view = view;
            this.parentLayout = view.findViewById(R.id.search_result_item_view);
            this.filmThumbnailLayout = new RelativeLayout(view.getContext());
            this.filmThumbnailLayout.setLayoutParams(new FrameLayout.LayoutParams(imageWidth, imageHeight));

            this.filmThumbnail = new ImageView(view.getContext());
            RelativeLayout.LayoutParams filmImageThumbnailLayoutParams =
                    new RelativeLayout.LayoutParams(imageWidth, imageHeight);
            this.filmThumbnail.setLayoutParams(filmImageThumbnailLayoutParams);

            this.thumbnailInfo = new TextView(view.getContext());
            this.thumbnailInfo.setTextColor(appCMSPresenter.getBrandPrimaryCtaColor());
            this.thumbnailInfo.setBackgroundColor(appCMSPresenter.getBrandPrimaryCtaTextColor());
            this.thumbnailInfo.setTextSize(textSize);
            RelativeLayout.LayoutParams thumbnailInfoParams =
                    new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                            ViewGroup.LayoutParams.WRAP_CONTENT);
            thumbnailInfoParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
            this.thumbnailInfo.setLayoutParams(thumbnailInfoParams);

            this.filmThumbnailLayout.addView(this.filmThumbnail);
            this.filmThumbnailLayout.addView(this.thumbnailInfo);

            this.parentLayout.addView(this.filmThumbnailLayout);

            this.titleLayout = new RelativeLayout(view.getContext());
            FrameLayout.LayoutParams titleLayoutParams =
                    new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT,
                            FrameLayout.LayoutParams.WRAP_CONTENT);
            titleLayoutParams.setMargins(0, textTopMargin, 0, 0);
            this.titleLayout.setLayoutParams(titleLayoutParams);

            RelativeLayout.LayoutParams gridLayoutParams =
                    new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                            ViewGroup.LayoutParams.WRAP_CONTENT);
            gridLayoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
            this.gridOptions = new ImageView(view.getContext());
            this.gridOptions.setId(View.generateViewId());
            this.gridOptions.setLayoutParams(gridLayoutParams);
            this.gridOptions.setBackground(context.getDrawable(R.drawable.dots_more));
            this.gridOptions.getBackground().setTint(appCMSPresenter.getGeneralTextColor());
            this.gridOptions.getBackground().setTintMode(PorterDuff.Mode.MULTIPLY);
            this.gridOptions.setVisibility(View.GONE);
            this.titleLayout.addView(this.gridOptions);

            this.filmTitle = new TextView(view.getContext());
            RelativeLayout.LayoutParams filmTitleLayoutParams =
                    new RelativeLayout.LayoutParams(textWidth,
                            ViewGroup.LayoutParams.WRAP_CONTENT);
            filmTitleLayoutParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
            filmTitleLayoutParams.addRule(RelativeLayout.CENTER_VERTICAL);
            filmTitleLayoutParams.addRule(RelativeLayout.LEFT_OF, this.gridOptions.getId());
            this.filmTitle.setLayoutParams(filmTitleLayoutParams);
            this.filmTitle.setTextSize(textSize);
            this.filmTitle.setMaxLines(2);

            this.filmTitle.setTextColor(appCMSPresenter.getGeneralTextColor());
            this.filmTitle.setEllipsize(TextUtils.TruncateAt.END);
            this.titleLayout.addView(this.filmTitle);

            this.parentLayout.addView(this.titleLayout);
        }
    }

    Bitmap createEmptyBitmap() {
        Bitmap emptyBitmap;
        Bitmap.Config conf = Bitmap.Config.ARGB_8888;

        if(BaseView.isTablet(context)) {
            emptyBitmap = Bitmap.createBitmap(230, 130, conf);
        }else{
            emptyBitmap = Bitmap.createBitmap(426, 239, conf);

        }
        return emptyBitmap;
    }

    Bitmap createEmptySquareBitmap() {
        Bitmap emptyBitmap;
        Bitmap.Config conf = Bitmap.Config.ARGB_8888;

        if(BaseView.isTablet(context)) {
             emptyBitmap = Bitmap.createBitmap(170, 170, conf);
        }else{
            emptyBitmap = Bitmap.createBitmap(400, 400, conf);

        }
        return emptyBitmap;
    }

    void applySportsStyleDefault(ViewHolder viewHolder, Bitmap image, boolean isVisible) {
        viewHolder.filmThumbnailLayout.setLayoutParams(new FrameLayout.LayoutParams(image.getWidth(), image.getHeight()));
        viewHolder.filmThumbnail.setLayoutParams(new RelativeLayout.LayoutParams(image.getWidth(), image.getHeight()));
        viewHolder.titleLayout.setLayoutParams(new FrameLayout.LayoutParams(image.getWidth(), ViewGroup.LayoutParams.WRAP_CONTENT));

        FrameLayout.LayoutParams titleLayoutParams = (FrameLayout.LayoutParams) viewHolder.titleLayout.getLayoutParams();
        titleLayoutParams.setMargins(0, image.getHeight(), 0, 0);
        viewHolder.titleLayout.setLayoutParams(titleLayoutParams);

        if (isVisible) {
            viewHolder.gridOptions.setVisibility(View.VISIBLE);
        } else {
            viewHolder.gridOptions.setVisibility(View.GONE);
        }

        placeholder = R.drawable.vid_image_placeholder_land;
        viewHolder.filmThumbnail.setScaleType(ImageView.ScaleType.FIT_XY);
    }

    private void setThumbInfoText(View view, Gist data) {
        String thumbInfo = null;
        if (data.getPublishDate() != null) {
            thumbInfo = getDateFormat(Long.parseLong(data.getPublishDate()), "MMM dd");
        }
        if (data != null && data.getReadTime() != null) {
            StringBuilder readTimeText = new StringBuilder()
                    .append(data.getReadTime().trim())
                    .append("min")
                    .append(" read ");

            if (thumbInfo != null && thumbInfo.length() > 0) {
                readTimeText.append("|")
                        .append(" ")
                        .append(thumbInfo);
            }
            ((TextView) view).setText(readTimeText);
        } else {
            long runtime = data.getRuntime();
            if (thumbInfo != null) {
                ((TextView) view).setText(appCMSPresenter.convertSecondsToTime(runtime) + " | " + thumbInfo);
            } else {
                ((TextView) view).setText(appCMSPresenter.convertSecondsToTime(runtime));
            }

        }
    }

    private String getDateFormat(long timeMilliSeconds, String dateFormat) {
        SimpleDateFormat formatter = new SimpleDateFormat(dateFormat);

        // Create a calendar object that will convert the date and time value in milliseconds to date.
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(timeMilliSeconds);
        return formatter.format(calendar.getTime());
    }

}