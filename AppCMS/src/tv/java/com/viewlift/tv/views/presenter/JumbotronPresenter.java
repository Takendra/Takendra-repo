package com.viewlift.tv.views.presenter;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.viewlift.R;
import com.viewlift.models.data.appcms.api.ContentDatum;
import com.viewlift.models.data.appcms.api.Module;
import com.viewlift.models.data.appcms.ui.AppCMSUIKeyType;
import com.viewlift.models.data.appcms.ui.page.Component;
import com.viewlift.presenters.AppCMSPresenter;
import com.viewlift.tv.model.BrowseFragmentRowData;
import com.viewlift.tv.utility.Utils;
import com.viewlift.tv.views.customviews.HoverCard;

import java.util.List;
import java.util.Map;

public class JumbotronPresenter extends CardPresenter {

    private static final String TAG = JumbotronPresenter.class.getCanonicalName();
    private final Component parentComponent;
    private final boolean isNoInfo;
    private Context mContext;
    private AppCMSPresenter mAppCMSPresenter;
    private boolean infoHover;


    public JumbotronPresenter(Context context, AppCMSPresenter appCMSPresenter, Component component,
                              Map<String, AppCMSUIKeyType> appCMSUIKeyTypeMap, boolean infoHover, boolean isNoInfo,
                              Module moduleData){
        super(context , appCMSPresenter, appCMSUIKeyTypeMap, infoHover, isNoInfo);
        mContext = context;
        mAppCMSPresenter = appCMSPresenter;
        this.parentComponent = component;
        this.infoHover = infoHover;
        this.isNoInfo = isNoInfo;
        this.module = moduleData;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent) {
            //Log.d("Presenter" , " CardPresenter onCreateViewHolder******");
            final CustomFrameLayout frameLayout = new CustomFrameLayout(parent.getContext());
            FrameLayout.LayoutParams layoutParams;

        layoutParams = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);

        frameLayout.setLayoutParams(layoutParams);
        frameLayout.setFocusable(true);
        //  frameLayout.setBackgroundColor(ContextCompat.getColor(mContext , android.R.color.black));
        frameLayout.setBackgroundColor(Color.parseColor(mAppCMSPresenter.getAppBackgroundColor()));

        return new ViewHolder(frameLayout);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, Object item) {
        //Log.d("Presenter" , " CardPresenter onBindViewHolder******");
        BrowseFragmentRowData rowData = (BrowseFragmentRowData) item;
        ContentDatum contentData = rowData.contentData;
        List<Component> componentList = rowData.uiComponentList;
        String blockName = rowData.blockName;
        CustomFrameLayout cardView = (CustomFrameLayout) viewHolder.view;
        createComponent(componentList, cardView, contentData);
        if (infoHover) {
            ((HoverCard) cardView.hoverLayout).removeViews();
            ((HoverCard) cardView.hoverLayout).initViews();
            if (cardView.hoverLayout.getParent() == null) {
                cardView.addView(cardView.hoverLayout);
            }
            bindComponent(cardView, contentData, blockName, rowData.infoHover, rowData.weatherHour, rowData.weatherInterval, rowData);
        }
    }

    @Override
    public void onUnbindViewHolder(ViewHolder viewHolder) {
        try {
            ((ViewGroup) viewHolder.view).removeAllViews();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void createComponent(List<Component> componentList, CustomFrameLayout parentLayout, ContentDatum contentData) {
        if (null != componentList && componentList.size() > 0) {
            for (Component component : componentList) {
                AppCMSUIKeyType componentType = mAppCMSPresenter.getJsonValueKeyMap().get(component.getType());
                if (componentType == null) {
                    componentType = AppCMSUIKeyType.PAGE_EMPTY_KEY;
                }

                AppCMSUIKeyType componentKey = mAppCMSPresenter.getJsonValueKeyMap().get(component.getKey());
                if (componentKey == null) {
                    componentKey = AppCMSUIKeyType.PAGE_EMPTY_KEY;
                }

                Log.d(TAG, "createComponent() called with: componentType = [" + componentType + "], componentKey = [" + componentKey + "], contentData = [" + contentData + "]");

                switch (componentType) {
                    case PAGE_IMAGE_KEY:
                        ImageView imageView = new ImageView(parentLayout.getContext());
                        switch (componentKey) {
                            case PAGE_CAROUSEL_IMAGE_KEY: {
                                Integer itemWidth = Integer.valueOf(component.getLayout().getTv().getWidth());
                                Integer itemHeight = Integer.valueOf(component.getLayout().getTv().getHeight());

                                FrameLayout.LayoutParams parms = new FrameLayout.LayoutParams(
                                        Utils.getViewXAxisAsPerScreen(mContext, itemWidth),
                                        Utils.getViewYAxisAsPerScreen(mContext, itemHeight));

                                imageView.setLayoutParams(parms);
                                imageView.setBackground(Utils.getTrayBorder(mContext, borderColor, component));
                                int gridImagePadding = Integer.valueOf(component.getLayout().getTv().getPadding());
                                imageView.setPadding(gridImagePadding, gridImagePadding, gridImagePadding, gridImagePadding);
//                                imageView.setScaleType(ImageView.ScaleType.FIT_XY);

                                if (contentData != null
                                        && contentData.getGist() != null) {
                                    String imageUrl = null;
                                    if (contentData.getGist().getVideoImageUrl() != null) {
                                        imageUrl = contentData.getGist().getVideoImageUrl() + "?impolicy=resize&w=" + itemWidth + "&h=" + itemHeight;
                                    } else if (contentData.getImages() != null && contentData.getImages().get_16x9Image() != null
                                            && contentData.getImages().get_16x9Image().getUrl() != null) {
                                        imageUrl = contentData.getImages().get_16x9Image().getUrl() + "?impolicy=resize&w=" + itemWidth + "&h=" + itemHeight;
                                    } else if (contentData.getGist().getPosterImageUrl() != null) {
                                        imageUrl = contentData.getGist().getPosterImageUrl() + "?impolicy=resize&w=" + itemWidth + "&h=" + itemHeight;
                                    }
                                    Glide.with(mContext)
                                            .load(imageUrl)
                                            .apply(new RequestOptions().diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                                                    .placeholder(R.drawable.video_image_placeholder)
                                                    .error(ContextCompat.getDrawable(mContext, R.drawable.video_image_placeholder)))
                                            .into(imageView);
                                }
                                parentLayout.addView(imageView);

                                if (null != parentLayout.hoverLayout) {
                                    FrameLayout.LayoutParams hoverParams = new FrameLayout.LayoutParams(
                                            Utils.getViewXAxisAsPerScreen(mContext, itemWidth - gridImagePadding * 2),
                                            Utils.getViewYAxisAsPerScreen(mContext, itemHeight - gridImagePadding * 2));

                                    hoverParams.setMargins(gridImagePadding, gridImagePadding, 0, 0);
                                    parentLayout.hoverLayout.setLayoutParams(hoverParams);
                                    ((HoverCard) parentLayout.hoverLayout).setCardHeight(itemHeight);
                                    ((HoverCard) parentLayout.hoverLayout).setCardWidth(itemWidth);
                                }

                                break;
                            }
                            case PAGE_WIDE_CAROUSEL_IMAGE_KEY: {
                                Integer itemWidth = Integer.valueOf(component.getLayout().getTv().getWidth());
                                Integer itemHeight = Integer.valueOf(component.getLayout().getTv().getHeight());

                                FrameLayout.LayoutParams parms = new FrameLayout.LayoutParams(
                                        Utils.getViewXAxisAsPerScreen(mContext, itemWidth),
                                        Utils.getViewYAxisAsPerScreen(mContext, itemHeight));

                                imageView.setLayoutParams(parms);
                                imageView.setBackground(Utils.getTrayBorder(mContext, borderColor, component));
                                int gridImagePadding = Integer.valueOf(component.getLayout().getTv().getPadding());
                                imageView.setPadding(gridImagePadding, gridImagePadding, gridImagePadding, gridImagePadding);
                                imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);

                                if (contentData != null
                                        && contentData.getGist() != null) {
                                    String imageUrl = null;
                                    if (contentData.getGist().getImageGist().get_32x9() != null) {
                                        imageUrl = contentData.getGist().getImageGist().get_32x9() + "?impolicy=resize&w=" + itemWidth + "&h=" + itemHeight;
                                        imageView.setScaleType(ImageView.ScaleType.FIT_XY);
                                    } else if (contentData.getGist().getVideoImageUrl() != null) {
                                        imageUrl = contentData.getGist().getVideoImageUrl() + "?impolicy=resize&w=" + itemWidth + "&h=" + itemHeight;
                                    } else if (contentData.getImages() != null && contentData.getImages().get_16x9Image() != null
                                            && contentData.getImages().get_16x9Image().getUrl() != null) {
                                        imageUrl = contentData.getImages().get_16x9Image().getUrl() + "?impolicy=resize&w=" + itemWidth + "&h=" + itemHeight;
                                    } else if (contentData.getGist().getPosterImageUrl() != null) {
                                        imageUrl = contentData.getGist().getPosterImageUrl() + "?impolicy=resize&w=" + itemWidth + "&h=" + itemHeight;
                                    }
                                    Glide.with(mContext)
                                            .load(imageUrl)
                                            .apply(new RequestOptions().diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                                                    .placeholder(R.drawable.video_image_placeholder)
                                                    .error(ContextCompat.getDrawable(mContext, R.drawable.video_image_placeholder_wide)))
                                            .into(imageView);
                                }
                                parentLayout.addView(imageView);

                                if (null != parentLayout.hoverLayout) {
                                    FrameLayout.LayoutParams hoverParams = new FrameLayout.LayoutParams(
                                            Utils.getViewXAxisAsPerScreen(mContext, itemWidth - gridImagePadding * 2),
                                            Utils.getViewYAxisAsPerScreen(mContext, itemHeight - gridImagePadding * 2));

                                    hoverParams.setMargins(gridImagePadding, gridImagePadding, 0, 0);
                                    parentLayout.hoverLayout.setLayoutParams(hoverParams);
                                    ((HoverCard) parentLayout.hoverLayout).setCardHeight(itemHeight);
                                    ((HoverCard) parentLayout.hoverLayout).setCardWidth(itemWidth);
                                }

                                break;

                            }

                            case PAGE_BADGE_IMAGE_KEY: {
                                createComponentView(parentComponent, parentLayout);
                                bindComponent(parentLayout, contentData, parentComponent.getBlockName(),
                                        parentComponent.getSettings() != null && parentComponent.getSettings().isInfoHover(),
                                        null,
                                        null, null);
                            }
                            /*case PAGE_VIDEO_HOVER_BACKGROUND_KEY: {
                                createComponentView(parentComponent, parentLayout);
                                break;
                            }*/
                        }
                        break;
                    case PAGE_LABEL_KEY:
                    case PAGE_BUTTON_KEY:{
                        if (!isNoInfo) {
                            createComponentView(parentComponent, parentLayout);
                            bindComponent(parentLayout, contentData, parentComponent.getBlockName(),
                                    parentComponent.getSettings() != null && parentComponent.getSettings().isInfoHover(),
                                    null,
                                    null, null);
                        }
                        break;
                    }
                }
            }
        }
    }
}
