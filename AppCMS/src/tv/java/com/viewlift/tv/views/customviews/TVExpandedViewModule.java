package com.viewlift.tv.views.customviews;

import android.annotation.SuppressLint;
import android.content.Context;

import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.viewlift.R;
import com.viewlift.models.data.appcms.api.ContentDatum;
import com.viewlift.models.data.appcms.api.MetadataMap;
import com.viewlift.models.data.appcms.api.Module;
import com.viewlift.models.data.appcms.api.Trailer;
import com.viewlift.models.data.appcms.ui.AppCMSUIKeyType;
import com.viewlift.models.data.appcms.ui.page.Component;
import com.viewlift.models.data.appcms.ui.page.Layout;
import com.viewlift.presenters.AppCMSPresenter;
import com.viewlift.tv.utility.Utils;
import com.viewlift.tv.views.activity.AppCmsHomeActivity;
import com.viewlift.views.customviews.OnInternalEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


@SuppressLint("ViewConstructor")
public class TVExpandedViewModule extends TVBaseView {

    private final TVPageView pageView;
    private TVModuleView moduleList;
    private Context mContext;
    private Component mComponent;
    private Map<String, AppCMSUIKeyType> mJsonValueKeyMap;
    private final TVViewCreator tvViewCreator;
    private final Module moduleAPI;
    AppCMSPresenter appCMSPresenter;
    private ContentDatum contentDatum;


    public TVExpandedViewModule(TVModuleView moduleList,
                                Context context,
                                Component component,
                                Module moduleAPI,
                                TVViewCreator tvViewCreator,
                                Map<String, AppCMSUIKeyType> jsonValueKeyMap,
                                AppCMSPresenter appCMSPresenter,
                                MetadataMap metadataMap,
                                TVPageView pageView) {
        super(context);
        mContext = context;
        mComponent = component;
        mJsonValueKeyMap = jsonValueKeyMap;
        this.appCMSPresenter = appCMSPresenter;
        this.tvViewCreator = tvViewCreator;
        this.moduleAPI = moduleAPI;
        this.moduleList = moduleList;
        this.pageView = pageView;
        init();
        setFocusable(false);
    }


    @Override
    public void init() {
        TVViewCreator.ComponentViewResult componentViewResult =
                tvViewCreator.getComponentViewResult();
        List<OnInternalEvent> onInternalEvents = new ArrayList<>();

        for (Component component : mComponent.getComponents()) {
            tvViewCreator.createComponentView(mContext,
                    component,
                    component.getLayout(),
                    moduleAPI,
                    pageView,
                    component.getSettings(),
                    mJsonValueKeyMap,
                    appCMSPresenter,
                    false,
                    null,
                    false);

            if (componentViewResult.onInternalEvent != null) {
                onInternalEvents.add(componentViewResult.onInternalEvent);
            }

            View componentView = componentViewResult.componentView;

            if (componentView != null) {
                setViewMarginsFromComponent(component,
                        componentView,
                        component.getLayout(),
                        this,
                        mJsonValueKeyMap,
                        false,
                        false,
                        component.getView());

            }
            addView(componentView);
        }
    }


    @Override
    protected Component getChildComponent(int index) {
        if (mComponent.getComponents() != null &&
                0 <= index &&
                index < mComponent.getComponents().size()) {
            return mComponent.getComponents().get(index);
        }
        return null;
    }

    @Override
    protected Layout getLayout() {
        return mComponent.getLayout();
    }

    public void refreshData(ContentDatum contentDatum){
        setContentData(contentDatum);

        for (Component component : mComponent.getComponents()) {
            AppCMSUIKeyType componentKey = mJsonValueKeyMap.get(component.getKey());
            if (componentKey == null) {
                componentKey = AppCMSUIKeyType.PAGE_EMPTY_KEY;
            }

            switch (componentKey) {
                case PAGE_EXPANDED_VIEW_TITLE_KEY:
                    if (contentDatum!= null &&
                            contentDatum.getGist() != null &&
                            !TextUtils.isEmpty(contentDatum.getGist().getTitle())) {
                        if(pageView.findViewById(R.id.expanded_view_title) != null) {
                            ((TextView) pageView.findViewById(R.id.expanded_view_title)).setText(contentDatum.getGist().getTitle().trim());
                        }
                    }
                    break;

                case PAGE_EXPANDED_VIEW_DESCRIPTION_KEY:
                    if (contentDatum!= null &&
                            contentDatum.getGist() != null &&
                            !TextUtils.isEmpty(contentDatum.getGist().getDescription())) {
                        if(pageView.findViewById(R.id.expanded_view_description) != null) {
                            ((TextView) pageView.findViewById(R.id.expanded_view_description)).setText(contentDatum.getGist().getDescription());
                        }
                    }
                    break;
                case PAGE_VIDEO_IMAGE_KEY:
                case PAGE_SHOW_IMAGE_KEY:
                    String imageUrl = "";
                    View view = pageView.findViewById(R.id.expanded_view_show_image);
                    if (contentDatum != null && contentDatum.getSeriesData() != null &&
                            contentDatum.getSeriesData().size() > 0 &&
                            contentDatum.getSeriesData().get(0).getGist() != null &&
                            contentDatum.getSeriesData().get(0).getGist().getImageGist() != null)  {
                        int viewWidth,viewHeight = 0;
                        if(view != null) {
                            viewHeight = view.getLayoutParams().height;
                            viewWidth = view.getLayoutParams().width;
                            if (contentDatum.getSeriesData().get(0).getGist().getImageGist().get_32x9() != null) {
                                viewWidth = (int) Utils.getViewWidthByRatio("32:9", viewHeight);
                                imageUrl = mContext.getString(R.string.app_cms_image_with_resize_query,
                                        contentDatum.getSeriesData().get(0).getGist().getImageGist().get_32x9(),
                                        viewWidth,
                                        viewHeight);
                            } else if (contentDatum.getSeriesData().get(0).getGist().getImageGist().get_16x9() != null) {
                                viewWidth = (int) Utils.getViewWidthByRatio("16:9", viewHeight);
                                imageUrl = mContext.getString(R.string.app_cms_image_with_resize_query,
                                        contentDatum.getSeriesData().get(0).getGist().getImageGist().get_16x9(),
                                        viewWidth,
                                        viewHeight);
                            }

                            Glide.with(mContext)
                                    .load(imageUrl)
                                    .apply(new RequestOptions().diskCacheStrategy(DiskCacheStrategy.RESOURCE))
                                    .into((ImageView) view);
                        }
                    }else{
                        if(view != null) {
                            Glide.with(mContext)
                                    .load(imageUrl)
                                    .apply(new RequestOptions().diskCacheStrategy(DiskCacheStrategy.RESOURCE))
                                    .into((ImageView) view);
                        }
                    }


                    break;
                case PAGE_THUMBNAIL_VIDEO_IMAGE_KEY:
                    String videoImageUrl = "";
                    if (contentDatum != null
                            && contentDatum.getGist() != null
                            && contentDatum.getGist().getVideoImageUrl() != null) {
                        videoImageUrl = contentDatum.getGist().getVideoImageUrl();
                    }
                    if(pageView.findViewById(R.id.expanded_view_video_image) != null) {
                        Glide.with(mContext)
                                .load(videoImageUrl)
                                .apply(new RequestOptions().diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                                        .error(ContextCompat.getDrawable(mContext, R.drawable.video_image_placeholder))
                                        .placeholder(ContextCompat.getDrawable(mContext, R.drawable.video_image_placeholder)))
                                .into((ImageView) pageView.findViewById(R.id.expanded_view_video_image));
                    }
                    break;

                case PAGE_TRAILER_VIDEO_PLAYER_VIEW_KEY_VALUE:
                    String contentType = contentDatum.getGist().getContentType();
                    System.out.println("contentType: "+contentType);
                    String trailerID = null,trailerTitle = null;
                    List<Trailer> trailers = null;
                    if (contentType.equalsIgnoreCase(mContext.getString(R.string.content_type_series))){
                        if (contentDatum.getShowDetails() != null) {
                            trailers = contentDatum.getShowDetails().getTrailers();
                        }
                    } else/* if (contentType.equalsIgnoreCase(mContext.getString(R.string.content_type_video)))*/{
                        if (contentDatum.getContentDetails() != null) {
                            trailers = contentDatum.getContentDetails().getTrailers();
                        }
                    }
                    if(trailers != null
                            && trailers.size() > 0
                            && trailers.get(0).getId() != null){
                        trailerID = trailers.get(0).getId();
                        trailerTitle = trailers.get(0).getTitle();
                    }
                    if(pageView.findViewById(R.id.trailer_view_id) != null) {
                        ((AppCmsHomeActivity) appCMSPresenter.getCurrentActivity()).setCarouselPlayerTask(mContext, appCMSPresenter, (pageView.findViewById(R.id.trailer_view_id)), trailerID, trailerTitle,null);
                    }
                    break;
            }
        }
    }

    public void updateTrailerID() {
        refreshData(contentDatum);
    }

    private void setContentData(ContentDatum contentDatum) {
        this.contentDatum = contentDatum;
    }



}
