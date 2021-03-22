package com.viewlift.tv.views.presenter;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.FocusFinder;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import androidx.core.graphics.drawable.RoundedBitmapDrawable;
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory;
import androidx.leanback.widget.Presenter;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.viewlift.R;
import com.viewlift.models.data.appcms.api.AppCMSTransactionDataValue;
import com.viewlift.models.data.appcms.api.ContentDatum;
import com.viewlift.models.data.appcms.api.Gist;
import com.viewlift.models.data.appcms.api.Module;
import com.viewlift.models.data.appcms.api.Season_;
import com.viewlift.models.data.appcms.ui.AppCMSUIKeyType;
import com.viewlift.models.data.appcms.ui.page.Component;
import com.viewlift.models.data.appcms.weather.Day;
import com.viewlift.models.data.appcms.weather.Hour;
import com.viewlift.presenters.AppCMSPresenter;
import com.viewlift.tv.model.BrowseFragmentRowData;
import com.viewlift.tv.utility.Utils;
import com.viewlift.tv.views.activity.AppCmsHomeActivity;
import com.viewlift.tv.views.customviews.HoverCard;
import com.viewlift.tv.views.customviews.TVBaseView;
import com.viewlift.tv.views.fragment.ClearDialogFragment;
import com.viewlift.utils.CommonUtils;
import com.viewlift.views.customviews.CustomTypefaceSpan;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import static android.view.View.FOCUS_UP;
import static com.viewlift.models.data.appcms.ui.AppCMSUIKeyType.PAGE_THUMBNAIL_DESCRIPTION_KEY;
import static com.viewlift.models.data.appcms.ui.AppCMSUIKeyType.WEATHER_THUMBNAIL_TITLE_LABEL;
import static com.viewlift.tv.utility.Utils.getViewHeight;
import static com.viewlift.tv.utility.Utils.getViewWidth;

/**
 * Created by nitin.tyagi on 6/29/2017.
 */

public class CardPresenter extends Presenter {
    private static final String TAG = CardPresenter.class.getCanonicalName();
    private final boolean infoHover;
    private final boolean isNoInfo;
    protected Module module;
    int i = 0;
    int mHeight = -1;
    int mWidth = -1;
    String borderColor = null;
    private String trayBackground;
    private Component mComponent;
    private AppCMSPresenter mAppCmsPresenter = null;
    private Context mContext;
    private Map<String, AppCMSUIKeyType> mJsonKeyValuemap;
    private boolean consumeUpKeyEvent = false;
    private boolean isExpandedViewSupported = false;

    public CardPresenter(Context context,
                         AppCMSPresenter appCMSPresenter,
                         int height,
                         int width,
                         boolean infoHover,
                         boolean isNoInfo,
                         Component component,
                         Map<String, AppCMSUIKeyType> jsonKeyValuemap,
                         Module module,
                         boolean isExpandedViewSupported) {
        mContext = context;
        mAppCmsPresenter = appCMSPresenter;
        mHeight = height;
        mWidth = width;
        mComponent = component;
        this.trayBackground = mComponent.getTrayBackground();
        mJsonKeyValuemap = jsonKeyValuemap;
        borderColor = Utils.getPrimaryHoverBorderColor(mContext,appCMSPresenter);
        this.infoHover = infoHover;
        this.isNoInfo = isNoInfo;
        this.module = module;
        this.isExpandedViewSupported = isExpandedViewSupported;
    }


    public CardPresenter(Context context,
                         AppCMSPresenter appCMSPresenter,
                         Map<String, AppCMSUIKeyType> jsonKeyValuemap,
                         boolean infoHover,
                         boolean isNoInfo) {
        mContext = context;
        mAppCmsPresenter = appCMSPresenter;
        borderColor = Utils.getPrimaryHoverBorderColor(mContext, appCMSPresenter);
        mJsonKeyValuemap = jsonKeyValuemap;
        this.infoHover = infoHover;
        this.isNoInfo = isNoInfo;
    }

    public CardPresenter(Context context,
                         AppCMSPresenter appCMSPresenter,
                         int height,
                         int width,
                         boolean infoHover,
                         boolean isNoInfo,
                         Component component,
                         Map<String, AppCMSUIKeyType> jsonKeyValuemap) {
        mContext = context;
        mAppCmsPresenter = appCMSPresenter;
        mHeight = height;
        mWidth = width;
        mComponent = component;
        this.trayBackground = mComponent.getTrayBackground();
        mJsonKeyValuemap = jsonKeyValuemap;
        borderColor = Utils.getPrimaryHoverBorderColor(mContext, appCMSPresenter);
        this.infoHover = infoHover;
        this.isNoInfo = isNoInfo;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent) {
        //Log.d("Presenter" , " CardPresenter onCreateViewHolder******");
        final CustomFrameLayout frameLayout = new CustomFrameLayout(parent.getContext());
        FrameLayout.LayoutParams layoutParams;

        if (mHeight != -1 && mWidth != -1) {
            layoutParams = new FrameLayout.LayoutParams(
                    Utils.getViewXAxisAsPerScreen(mContext, mWidth),
                    Utils.getViewXAxisAsPerScreen(mContext, mHeight));
        } else {
            layoutParams = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT,
                    FrameLayout.LayoutParams.WRAP_CONTENT);
        }
        frameLayout.setLayoutParams(layoutParams);
        frameLayout.setFocusable(true);
        createComponentView(mComponent ,frameLayout);
        if(infoHover) ((HoverCard)frameLayout.hoverLayout).initViews();
        return new ViewHolder(frameLayout);
}

    protected void createComponentView(Component parentComponent, CustomFrameLayout parentLayout) {

        List<Component> componentList = parentComponent.getComponents();
        if(null != componentList && componentList.size() > 0) {
            for (Component component : componentList) {
                AppCMSUIKeyType componentType = mAppCmsPresenter.getJsonValueKeyMap().get(component.getType());
                if (componentType == null) {
                    componentType = AppCMSUIKeyType.PAGE_EMPTY_KEY;
                }

                AppCMSUIKeyType componentKey = mAppCmsPresenter.getJsonValueKeyMap().get(component.getKey());
                if (componentKey == null) {
                    componentKey = AppCMSUIKeyType.PAGE_EMPTY_KEY;
                }

                switch (componentType) {
                    case VIEW:
                        View view = new View(parentLayout.getContext());
                        switch (componentKey){
                            case WEATHER_WIDGET_VIEW:
                                Integer itemWidth = Integer.valueOf(component.getLayout().getTv().getWidth());
                                Integer itemHeight = Integer.valueOf(component.getLayout().getTv().getHeight());
                                FrameLayout.LayoutParams parms = new FrameLayout.LayoutParams(
                                        Utils.getViewXAxisAsPerScreen(mContext, itemWidth),
                                        Utils.getViewYAxisAsPerScreen(mContext, itemHeight));
                                int leftMargin = 0;
                                int topMargin = 0;
                                if (component.getLayout() != null
                                        && component.getLayout().getTv() != null) {
                                    if (component.getLayout().getTv().getLeftMargin() != null) {
                                        leftMargin = Integer.valueOf(component.getLayout().getTv().getLeftMargin());
                                    }
                                    if (component.getLayout().getTv().getTopMargin() != null) {
                                        topMargin = Integer.valueOf(component.getLayout().getTv().getTopMargin());
                                    }
                                }
                                parms.setMargins(leftMargin, topMargin, 0, 0);
                                view.setLayoutParams(parms);
                                parentLayout.addView(view);
                                parentLayout.addChildComponentAndView(view, component);
                                break;
                        }
                        break;
                    case PAGE_BUTTON_KEY:{

                        switch (componentKey){
                            case PAGE_WATCH_NOW_CAROUSEL_KEY: {

                                if (!isNoInfo) {
//                                    Button button = new Button(new ContextThemeWrapper(parentLayout.getContext(), android.R.style.Widget_DeviceDefault_Button_Borderless), null, android.R.style.Widget_DeviceDefault_Button_Borderless);
                                    Button button = new Button(parentLayout.getContext());
                                    button.setStateListAnimator(null);
                                    int viewWidth = (int) getViewWidth(mContext, component.getLayout(), FrameLayout.LayoutParams.WRAP_CONTENT);
                                    int viewHeight = (int) getViewHeight(mContext, component.getLayout(), FrameLayout.LayoutParams.WRAP_CONTENT);
                                    FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(viewWidth, viewHeight);
                                    if (component.getLayout().getTv().getTopMargin() != null)
                                        layoutParams.topMargin = Utils.getViewYAxisAsPerScreen(mContext, Integer.valueOf(component.getLayout().getTv().getTopMargin()));
                                    else
                                        layoutParams.topMargin = Utils.getViewYAxisAsPerScreen(mContext, 0);

                                    if (component.getLayout().getTv().getLeftMargin() != null)
                                        layoutParams.leftMargin = Utils.getViewYAxisAsPerScreen(mContext, Integer.valueOf(component.getLayout().getTv().getLeftMargin()));
                                    else
                                        layoutParams.leftMargin = Utils.getViewYAxisAsPerScreen(mContext, 0);

                                    if (!TextUtils.isEmpty(component.getTextColor())) {
                                        ((TextView) button).setTextColor(Color.parseColor(CommonUtils.getColor(mContext, component.getTextColor())));
                                        if (!TextUtils.isEmpty(component.getBorderColor())) {
                                            ((TextView) button).setTextColor(Utils.getButtonTextColorDrawable(
                                                    CommonUtils.getColor(mContext, component.getBorderColor()),
                                                    CommonUtils.getColor(mContext, component.getTextColor()), mAppCmsPresenter));
                                        }
                                    }
                                    if (!TextUtils.isEmpty(component.getBackgroundColor())) {
                                        button.setBackgroundColor(Color.parseColor(CommonUtils.getColor(mContext, component.getBackgroundColor())));
                                    } else {
                                        button.setBackground(
                                                Utils.setButtonBackgroundSelector(mContext,
                                                        Color.parseColor(Utils.getFocusColor(mContext, mAppCmsPresenter)),
                                                        component,
                                                        mAppCmsPresenter));
                                    }

                                    if (component.getLetetrSpacing() != 0) {
                                        ((TextView) button).
                                                setLetterSpacing(component.getLetetrSpacing());
                                    }

                                    int tintColor = Color.parseColor(CommonUtils.getColor(mContext,
                                            Utils.getFocusColor(mContext, mAppCmsPresenter)));

                                    button.setLayoutParams(layoutParams);
                                    button.setTypeface(Utils.getTypeFace(mContext,
                                            mAppCmsPresenter,
                                            component));
                                    if (component.getText() != null)
                                        button.setText(component.getText());
                                    if (component.getFontSize() != 0) {
                                        button.setTextSize(component.getFontSize());
                                    }
                                    button.setEnabled(false);
                                    button.setFocusable(false);
                                    button.setPadding(0, 0, 0, 0);
                                    parentLayout.addView(button);
                                    parentLayout.addChildComponentAndView(button, component);
                                }
                            }
                            break;
                        }
                    }
                    break;
                    case PAGE_IMAGE_KEY:
                        ImageView imageView = new ImageView(parentLayout.getContext());
                        switch (componentKey) {
                            case PAGE_CIRCULAR_THUMNAIL_IMAGE_KEY:
                            case PAGE_THUMBNAIL_IMAGE_KEY:
                            case PAGE_WIDE_CAROUSEL_IMAGE_KEY:
                            case PAGE_TALL_THUMBNAIL_IMAGE_KEY:{
                                Integer itemWidth = Integer.valueOf(component.getLayout().getTv().getWidth());
                                Integer itemHeight = Integer.valueOf(component.getLayout().getTv().getHeight());
                                FrameLayout.LayoutParams parms = new FrameLayout.LayoutParams(
                                        Utils.getViewXAxisAsPerScreen(mContext, itemWidth),
                                        Utils.getViewYAxisAsPerScreen(mContext, itemHeight));
                                int leftMargin = 0;
                                int topMargin = 0;
                                if (component.getLayout() != null
                                        && component.getLayout().getTv() != null) {
                                    if (component.getLayout().getTv().getLeftMargin() != null) {
                                        leftMargin = Integer.valueOf(component.getLayout().getTv().getLeftMargin());
                                    }
                                    if (component.getLayout().getTv().getTopMargin() != null) {
                                        topMargin = Integer.valueOf(component.getLayout().getTv().getTopMargin());
                                    }
                                }
                                parms.setMargins(leftMargin, topMargin, 0, 0);
                                int gridImagePadding = Integer.valueOf(component.getLayout().getTv().getPadding());
                                imageView.setLayoutParams(parms);
                                if(null != parentLayout.hoverLayout) {
                                    FrameLayout.LayoutParams hoverParams = new FrameLayout.LayoutParams(
                                            Utils.getViewXAxisAsPerScreen(mContext, itemWidth - gridImagePadding*2),
                                            Utils.getViewYAxisAsPerScreen(mContext, itemHeight - gridImagePadding*2));

                                    hoverParams.setMargins(leftMargin + gridImagePadding, topMargin + gridImagePadding, 0, 0);
                                    parentLayout.hoverLayout.setLayoutParams(hoverParams);
                                    ((HoverCard)parentLayout.hoverLayout).setCardHeight(itemHeight);
                                    ((HoverCard)parentLayout.hoverLayout).setCardWidth(itemWidth);
                                }

                                imageView.setPadding(gridImagePadding, gridImagePadding, gridImagePadding, gridImagePadding);
                                imageView.setScaleType(ImageView.ScaleType.FIT_XY);
                                parentLayout.addView(imageView);
                                parentLayout.addChildComponentAndView(imageView, component);
                                break;
                            }

                            case NEWS_THUMBNAIL_IMAGE:
                                setLayouPram(component, imageView);
                                parentLayout.addView(imageView);
                                parentLayout.addChildComponentAndView(imageView, component);
                                imageView.setFocusable(false);
                                imageView.setClickable(false);
                                parentLayout.setFocusable(false);
                                parentLayout.setClickable(false);

                                break;

                            case PAGE_BEDGE_IMAGE_KEY:
                            case PAGE_BADGE_IMAGE_KEY:
                            case WEATHER_CLOUD_IMAGE:
                            case WEATHER_DROP_IMAGE:
                            case PAGE_WIDE_BADGE_IMAGE_KEY:
                                {
                                Integer badgeItemWidth = Integer.valueOf(component.getLayout().getTv().getWidth());
                                Integer badgeItemHeight = Integer.valueOf(component.getLayout().getTv().getHeight());
                                    int gridImagePadding = 0;
                                if(component.getLayout().getTv().getPadding() != null){
                                    gridImagePadding = Integer.valueOf(component.getLayout().getTv().getPadding());
                                }

                                FrameLayout.LayoutParams badgeParams = new FrameLayout.LayoutParams(
                                        Utils.getViewXAxisAsPerScreen(mContext, badgeItemWidth),
                                        Utils.getViewYAxisAsPerScreen(mContext, badgeItemHeight));
                                String leftMargin = component.getLayout().getTv().getLeftMargin() != null ? component.getLayout().getTv().getLeftMargin(): "0";
                                String topMargin = component.getLayout().getTv().getTopMargin() != null ? component.getLayout().getTv().getTopMargin(): "0";

                                badgeParams.setMargins(
                                        Integer.valueOf(leftMargin),
                                        Integer.valueOf(topMargin),
                                        0,
                                        0);
                                imageView.setPadding(gridImagePadding, gridImagePadding, gridImagePadding, gridImagePadding);
                                imageView.setScaleType(ImageView.ScaleType.FIT_XY);
                                imageView.setLayoutParams(badgeParams);
                                parentLayout.addView(imageView);
                                parentLayout.addChildComponentAndView(imageView, component);
                                break;
                            }

                            /*case PAGE_VIDEO_HOVER_BACKGROUND_KEY: {
                                if (infoHover) {
                                    Integer itemWidth = Integer.valueOf(component.getLayout().getTv().getWidth());
                                    Integer itemHeight = Integer.valueOf(component.getLayout().getTv().getHeight());
                                    FrameLayout.LayoutParams parms = new FrameLayout.LayoutParams(
                                            Utils.getViewXAxisAsPerScreen(mContext, itemWidth),
                                            Utils.getViewYAxisAsPerScreen(mContext, itemHeight));
                                    int leftMargin = 0;
                                    int topMargin = 0;
                                    if (component.getLayout() != null
                                            && component.getLayout().getTv() != null) {
                                        if (component.getLayout().getTv().getLeftMargin() != null) {
                                            leftMargin = Integer.valueOf(component.getLayout().getTv().getLeftMargin());
                                        }
                                        if (component.getLayout().getTv().getTopMargin() != null) {
                                            topMargin = Integer.valueOf(component.getLayout().getTv().getTopMargin());
                                        }
                                    }
                                    parms.setMargins(leftMargin, topMargin, 0, 0);

                                    imageView.setLayoutParams(parms);
                                    int gridImagePadding = Integer.valueOf(component.getLayout().getTv().getPadding());
                                    imageView.setPadding(gridImagePadding, gridImagePadding, gridImagePadding, gridImagePadding);
                                    imageView.setScaleType(ImageView.ScaleType.FIT_XY);
                                    parentLayout.hoverLayout.addView(imageView);
                                    parentLayout.addChildComponentAndView(imageView, component);
                                    parentLayout.setHoverBackground(imageView);

                                    imageView.setId(R.id.videoBackgroundOnHover);
                                    imageView.setBackgroundColor(Color.parseColor("#CC" + mAppCmsPresenter.getAppBackgroundColor().replace("#","")));
                                    imageView.setAlpha(0f);
                                }
                                break;
                            }*/

                        }
                        break;

                    case PAGE_LABEL_KEY:{
                        TextView tvTitle = new TextView(parentLayout.getContext());
                        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(
                                ViewGroup.LayoutParams.WRAP_CONTENT,
                                ViewGroup.LayoutParams.WRAP_CONTENT);
                        if (componentKey.equals(AppCMSUIKeyType.PAGE_THUMBNAIL_TIME_AND_DATE_KEY)) {
                            layoutParams = new FrameLayout.LayoutParams(
                                    ViewGroup.LayoutParams.WRAP_CONTENT,
                                    ViewGroup.LayoutParams.WRAP_CONTENT);
                            StringBuilder stringBuilder = new StringBuilder();
                            if (mAppCmsPresenter.getAppCMSMain().getBrand().getMetadata() != null) {
                                tvTitle.setBackgroundColor(Color.parseColor(mAppCmsPresenter.getAppBackgroundColor()));
                                tvTitle.getBackground().setAlpha(128);
                                tvTitle.setGravity(Gravity.CENTER);
                                Integer padding = Integer.valueOf(component.getLayout().getTv().getPadding());
                                tvTitle.setPadding(6, padding, 10, 4);
                                tvTitle.setVisibility(View.VISIBLE);
                                tvTitle.setTextColor(Color.parseColor(mAppCmsPresenter.getAppTextColor()));
                            } else /*Don't show time and date as metadata is null*/ {
                                tvTitle.setVisibility(View.INVISIBLE);
                            }
                            parentLayout.addView(tvTitle);
                            parentLayout.addChildComponentAndView(tvTitle, component);
                        } else if (componentKey.equals(AppCMSUIKeyType.PAGE_NEWS_THUMBNAIL_TIME_AND_DATE_KEY)) {
                            layoutParams = new FrameLayout.LayoutParams(
                                    ViewGroup.LayoutParams.WRAP_CONTENT,
                                    ViewGroup.LayoutParams.WRAP_CONTENT);
                            layoutParams.gravity = Gravity.END;
                            Integer padding = Integer.valueOf(component.getLayout().getTv().getPadding());
                            tvTitle.setPadding(padding, padding, padding, padding);
                            tvTitle.setVisibility(View.GONE);
                            tvTitle.setTextColor(Color.parseColor(mAppCmsPresenter.getAppTextColor()));
                            parentLayout.addView(tvTitle);
                            parentLayout.addChildComponentAndView(tvTitle, component);
                        }else if (componentKey.equals(AppCMSUIKeyType.PAGE_EPISODE_THUMBNAIL_TITLE_KEY)) {
                            Integer height = component.getLayout().getTv().getHeight() != null
                                    ? Integer.valueOf(component.getLayout().getTv().getHeight())
                                    : 0;
                            layoutParams = new FrameLayout.LayoutParams(
                                    FrameLayout.LayoutParams.MATCH_PARENT,
                                    Utils.getViewYAxisAsPerScreen(mContext, height));
                            tvTitle.setSelected(true);
                            tvTitle.setEllipsize(TextUtils.TruncateAt.MARQUEE);
                            parentLayout.addView(tvTitle);
                            parentLayout.addChildComponentAndView(tvTitle, component);
                            tvTitle.setMaxLines(1);
                            tvTitle.setTextColor(Color.parseColor(mAppCmsPresenter.getAppTextColor()));
                            parentLayout.setThumbnailTitle(tvTitle);
                        }  else if (componentKey.equals(AppCMSUIKeyType.PAGE_THUMBNAIL_TITLE_KEY)
                                || componentKey.equals(WEATHER_THUMBNAIL_TITLE_LABEL)
                            || componentKey.equals(AppCMSUIKeyType.WEATHER_TEMP_HIGH_LABEL)
                            ||    componentKey.equals(AppCMSUIKeyType.WEATHER_TEMP_LOW_LABEL)
                                ||  componentKey.equals(AppCMSUIKeyType.WEATHER_SHORT_PHRASE_LABEL)
                                || componentKey.equals(AppCMSUIKeyType.WEATHER_DROP_PERCENTAGE_LABEL)
                                || componentKey.equals(PAGE_THUMBNAIL_DESCRIPTION_KEY)
                                ){
                            Integer height = component.getLayout().getTv().getHeight() != null
                                    ? Integer.valueOf(component.getLayout().getTv().getHeight())
                                    : 0;

                            Integer width = component.getLayout().getTv().getWidth() != null
                                    ? Integer.valueOf(component.getLayout().getTv().getWidth())
                                    : 0;

                            layoutParams = new FrameLayout.LayoutParams(
                                    FrameLayout.LayoutParams.MATCH_PARENT,
                                    Utils.getViewYAxisAsPerScreen(mContext, height));

                            if(componentKey.equals(AppCMSUIKeyType.WEATHER_SHORT_PHRASE_LABEL)){
                                layoutParams = new FrameLayout.LayoutParams(
                                        width,
                                        Utils.getViewYAxisAsPerScreen(mContext, height));
                            }


                            if (component.getTextAlignment() != null
                                    && componentKey.equals(AppCMSUIKeyType.WEATHER_TEMP_LOW_LABEL)
                                    && mJsonKeyValuemap.get(component.getTextAlignment()) ==
                                    AppCMSUIKeyType.PAGE_TEXTALIGNMENT_CENTER_HORIZONTAL_KEY) {
                                tvTitle.setGravity(Gravity.CENTER_HORIZONTAL);
                            }
                            tvTitle.setSingleLine(false);
                            tvTitle.setEllipsize(TextUtils.TruncateAt.END);
                            parentLayout.addView(tvTitle);
                            parentLayout.addChildComponentAndView(tvTitle, component);
                            parentLayout.setThumbnailTitle(tvTitle);
                            tvTitle.setMaxLines(2);
                            tvTitle.setTextColor(Color.parseColor(mAppCmsPresenter.getAppTextColor()));
                        } else if (componentKey.equals(AppCMSUIKeyType.PAGE_THUMBNAIL_TITLE_WITH_BG_KEY)) {
                            if (!isNoInfo) {
                                Integer height = component.getLayout().getTv().getHeight() != null
                                        ? Integer.valueOf(component.getLayout().getTv().getHeight())
                                        : 0;

                                Integer width = component.getLayout().getTv().getWidth() != null
                                        ? Integer.valueOf(component.getLayout().getTv().getWidth())
                                        : 0;

                                layoutParams = new FrameLayout.LayoutParams(
                                        FrameLayout.LayoutParams.WRAP_CONTENT,
                                        Utils.getViewYAxisAsPerScreen(mContext, height));

                                tvTitle.setEllipsize(TextUtils.TruncateAt.END);
                                parentLayout.addView(tvTitle);
                                parentLayout.addChildComponentAndView(tvTitle, component);
                                parentLayout.setThumbnailTitle(tvTitle);
                                tvTitle.setMaxLines(2);
                                tvTitle.setBackground(mContext.getDrawable(R.drawable.text_bg));
                                tvTitle.setTextColor(Color.parseColor(mAppCmsPresenter.getAppTextColor()));
                                tvTitle.setPadding(10, 0, 10,0);
                            }

                        }
                            if(mAppCmsPresenter.getTemplateType() == AppCMSPresenter.TemplateType.ENTERTAINMENT)
                                tvTitle.setSingleLine(true);
                            tvTitle.setEllipsize(TextUtils.TruncateAt.END);
                            tvTitle.setSelected(true);

                        if (component.getLayout().getTv().getTopMargin() != null)
                            layoutParams.topMargin = Utils.getViewYAxisAsPerScreen(mContext, Integer.valueOf(component.getLayout().getTv().getTopMargin()));
                        else
                            layoutParams.topMargin = Utils.getViewYAxisAsPerScreen(mContext, 0);

                        if (component.getLayout().getTv().getLeftMargin() != null)
                            layoutParams.leftMargin = Utils.getViewYAxisAsPerScreen(mContext, Integer.valueOf(component.getLayout().getTv().getLeftMargin()));
                        else
                            layoutParams.leftMargin = Utils.getViewYAxisAsPerScreen(mContext, 0);

                        if (component.getLayout().getTv().getRightMargin() != null)
                            layoutParams.rightMargin = Utils.getViewYAxisAsPerScreen(mContext, Integer.valueOf(component.getLayout().getTv().getRightMargin()));
                        else
                            layoutParams.rightMargin = Utils.getViewYAxisAsPerScreen(mContext, 0);


                        tvTitle.setLayoutParams(layoutParams);
                        tvTitle.setTypeface(Utils.getTypeFace(mContext,
                                mAppCmsPresenter,
                                component));
                        if (component.getText() != null)
                            tvTitle.setText(component.getText());
                        if (component.getFontSize() != 0) {
                            tvTitle.setTextSize(component.getFontSize());
                        }
                        break;
                    }
                    case PAGE_PROGRESS_VIEW_KEY: {
                        FrameLayout.LayoutParams progressBarParams = new FrameLayout.LayoutParams(
                                FrameLayout.LayoutParams.MATCH_PARENT,
                                Utils.getViewYAxisAsPerScreen(mContext, Integer.valueOf(component.getLayout().getTv().getHeight())));
                        progressBarParams.topMargin = Utils.getViewYAxisAsPerScreen(mContext, Integer.valueOf(component.getLayout().getTv().getYAxis()));

                        ProgressBar progressBar = new ProgressBar(mContext,
                                null,
                                R.style.Widget_AppCompat_ProgressBar_Horizontal);
                        progressBar.setLayoutParams(progressBarParams);

                        int gridImagePadding = Integer.valueOf(component.getLayout().getTv().getPadding());
                        progressBar.setPadding(gridImagePadding, 0, gridImagePadding, 0);
                        progressBar.setProgressDrawable(Utils.getProgressDrawable(mContext, component.getUnprogressColor(), mAppCmsPresenter));
                        progressBar.setFocusable(false);
                        parentLayout.addView(progressBar);
                        parentLayout.addChildComponentAndView(progressBar, component);
                        break;
                    }
                }
            }
        }
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, Object item) {
        BrowseFragmentRowData rowData = (BrowseFragmentRowData)item;
         ContentDatum contentData = rowData.contentData;
        List<Component> componentList = rowData.uiComponentList;
        String blockName = rowData.blockName;
        CustomFrameLayout cardView = (CustomFrameLayout) viewHolder.view;
        if(null != blockName && ( blockName.equalsIgnoreCase("tray03")
                || blockName.equalsIgnoreCase("search02"))
                && !rowData.infoHover){
            boolean isSameTextandPrimaryHoverColor = false;
            try {
                isSameTextandPrimaryHoverColor = mAppCmsPresenter.getAppTextColor().equals(Utils.getPrimaryHoverColor(mContext, mAppCmsPresenter));
            }catch (Exception e){

            }
            cardView.setBackground(Utils.getTrayBorder(mContext, Utils.getPrimaryHoverColor(mContext, mAppCmsPresenter),
                    mAppCmsPresenter.getAppBackgroundColor() ,isSameTextandPrimaryHoverColor ));
        }
        bindComponent(cardView, contentData, blockName, rowData.infoHover, rowData.weatherHour, rowData.weatherInterval,rowData);
        cardView.postInvalidate();
        //createComponent(componentList, cardView, contentData,blockName);
        boolean isShowDetailPage = false;
        if(mAppCmsPresenter.getCurrentActivity() instanceof AppCmsHomeActivity
                && !TextUtils.isEmpty(((AppCmsHomeActivity) mAppCmsPresenter.getCurrentActivity()).getCurrentPage())
                && ((AppCmsHomeActivity) mAppCmsPresenter.getCurrentActivity()).getCurrentPage().toLowerCase().contains("show")) {
            isShowDetailPage = true;
        }
        boolean finalIsShowDetailPage = isShowDetailPage;
        cardView.setOnKeyListener((v, keyCode, event) -> {

            if (rowData.blockName != null && finalIsShowDetailPage) {
                toggleButtonsFocusability(rowData, cardView, keyCode, event);
            }
            if(keyCode == KeyEvent.KEYCODE_DPAD_UP) {
                if(event.getAction() == KeyEvent.ACTION_DOWN){
                     if (rowData.rowNumber == 0) {
                        consumeUpKeyEvent = true;
                    } else {
                        consumeUpKeyEvent = false;
                    }
                } else if (event.getAction() == KeyEvent.ACTION_UP) {
                     if (rowData.rowNumber == 0) {
                        if (consumeUpKeyEvent) {
                            @SuppressLint("WrongConstant")
                            View view = cardView.focusSearch(View.FOCUS_BACKWARD);
                            if (view != null && view instanceof Button) {
                                view.requestFocus();
                            } else {
                                cardView.clearFocus();
                            }
                            consumeUpKeyEvent = false;
                        }
                    }
                }
            } else if (keyCode == KeyEvent.KEYCODE_DPAD_DOWN
                    && event.getAction() == KeyEvent.ACTION_UP) {
                consumeUpKeyEvent = false;
            }
            return false;
        });

        if(rowData.itemPosition == 0 && rowData.rowNumber == 0){
            if(rowData.itemPosition == 0){
                cardView.setId(R.id.browse_first_row);
            }
        }
    }

    public void setLayouPram(Component component, View view) {
        Integer itemWidth = Integer.valueOf(component.getLayout().getTv().getWidth());
        Integer itemHeight = Integer.valueOf(component.getLayout().getTv().getHeight());
        FrameLayout.LayoutParams parms = new FrameLayout.LayoutParams(
                Utils.getViewXAxisAsPerScreen(mContext, itemWidth),
                Utils.getViewYAxisAsPerScreen(mContext, itemHeight));
        int leftMargin = 0;
        int topMargin = 0;
        if (component.getLayout() != null
                && component.getLayout().getTv() != null) {
            if (component.getLayout().getTv().getLeftMargin() != null) {
                leftMargin = Integer.valueOf(component.getLayout().getTv().getLeftMargin());
            }
            if (component.getLayout().getTv().getTopMargin() != null) {
                topMargin = Integer.valueOf(component.getLayout().getTv().getTopMargin());
            }
        }
        parms.setMargins(leftMargin, topMargin, 0, 0);
        int gridImagePadding = Integer.valueOf(component.getLayout().getTv().getPadding() != null ? component.getLayout().getTv().getPadding() : "0");
        if (view instanceof TextView) {
            if (!TextUtils.isEmpty(component.getTextColor())) {
                ((TextView) view).setTextColor(Color.parseColor(component.getTextColor()));
                ((TextView) view).setTextSize(component.getLayout().getTv().getFontSize());
            }
        }
        view.setLayoutParams(parms);
    }

    private void toggleButtonsFocusability(BrowseFragmentRowData rowData, CustomFrameLayout cardView, int keyCode, KeyEvent event) {
        System.out.println("toggleButtonsFocusability. KeyCode = "+keyCode + " keyevent = "+event.getAction() + ", rowData.rowNumber: " + rowData.rowNumber + ", rowData.blockName: " + rowData.blockName);
        View nextFocus = FocusFinder.getInstance().findNextFocus(
                (((Activity) cardView.getContext()).findViewById(R.id.subscribe_now_strip_containeer)),
                ((Activity) cardView.getContext()).getCurrentFocus(),
                FOCUS_UP);
        if (nextFocus instanceof TextView) {
            System.out.println("anas nextFocus: " + ((TextView) nextFocus).getText());
        } else {
            System.out.println("anas nextFocus: " + nextFocus);
        }
        try {
            if (rowData.rowNumber != 0) {
                if (nextFocus instanceof Button) {
                    nextFocus.setFocusable(true);
                }
            } else {
                if (nextFocus != null) nextFocus.setFocusable(true);
                View btnAddToWatchlist = mAppCmsPresenter.getCurrentActivity().findViewById(R.id.btn_add_to_watchlist);
                View btnWatchTrailer = mAppCmsPresenter.getCurrentActivity().findViewById(R.id.btn_watch_trailer);
                View btnStartWatching = mAppCmsPresenter.getCurrentActivity().findViewById(R.id.btn_start_watching);
                View btnRentOption = mAppCmsPresenter.getCurrentActivity().findViewById(R.id.btn_rent_option);
                View btnBuyOption = mAppCmsPresenter.getCurrentActivity().findViewById(R.id.btn_buy_option);
                if (btnAddToWatchlist != null) btnAddToWatchlist.setFocusable(true);
                if (btnWatchTrailer != null) btnWatchTrailer.setFocusable(true);
                if (btnStartWatching != null) btnStartWatching.setFocusable(true);
                if (btnRentOption != null) btnRentOption.setFocusable(true);
                if (btnBuyOption != null) btnBuyOption.setFocusable(true);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void bindHoverInfo() {
    }


    @Override
    public void onUnbindViewHolder(ViewHolder viewHolder) {
    }

    protected void bindComponent(CustomFrameLayout cardView,
                                 ContentDatum contentData,
                                 String blockName,
                                 boolean infoHover,
                                 Object weatherHour ,
                                 String weatherInterval,
                                 BrowseFragmentRowData rowData) {
        List<CustomFrameLayout.ChildComponentAndView> childComponentAndViewsList = cardView.getChildViewList();
        if(null != childComponentAndViewsList && childComponentAndViewsList.size() > 0){
            for(CustomFrameLayout.ChildComponentAndView childComponentAndView : childComponentAndViewsList){
                AppCMSUIKeyType componentType = mAppCmsPresenter.getJsonValueKeyMap().get(childComponentAndView.component.getType());
                if (componentType == null) {
                    componentType = AppCMSUIKeyType.PAGE_EMPTY_KEY;
                }

                AppCMSUIKeyType componentKey = mAppCmsPresenter.getJsonValueKeyMap().get(childComponentAndView.component.getKey());
                if (componentKey == null) {
                    componentKey = AppCMSUIKeyType.PAGE_EMPTY_KEY;
                }

                switch (componentType) {
                    case PAGE_BUTTON_KEY: {
                        Button button = (Button) childComponentAndView.childView;

                        switch (componentKey) {
                            case PAGE_WATCH_NOW_CAROUSEL_KEY:
                                button.setText(mAppCmsPresenter.getLocalisedStrings().getWatchNowCtaNbc());
                                break;
                        }
                    }
                    break;
                    case VIEW:
                        View view = childComponentAndView.childView;
                        switch (componentKey) {
                            case WEATHER_WIDGET_VIEW:
                                 view.setBackground(Utils.getTrayBorder(mContext, borderColor, childComponentAndView.component));
                                break;
                        }
                        break;
                    case PAGE_IMAGE_KEY:
                        ImageView imageView = (ImageView) childComponentAndView.childView;
                        switch(componentKey) {
                            case WEATHER_IMAGE:
                                Integer imageWidth = Integer.valueOf(childComponentAndView.component.getLayout().getTv().getWidth());
                                Integer imageHeight = Integer.valueOf(childComponentAndView.component.getLayout().getTv().getHeight());
                                break;
                            case PAGE_CIRCULAR_THUMNAIL_IMAGE_KEY: {
                                Integer itemWidth = Integer.valueOf(childComponentAndView.component.getLayout().getTv().getWidth());
                                Integer itemHeight = Integer.valueOf(childComponentAndView.component.getLayout().getTv().getHeight());
                                    String imageUrl = null;
                                    if (contentData != null && contentData.getGist() != null) {
                                        if(contentData.getGist().getImageGist() != null && contentData.getGist().getImageGist().get_1x1() != null
                                                && contentData.getGist().getImageGist().get_1x1().length()> 0){
                                            imageUrl = contentData.getGist().getImageGist().get_1x1() + "?impolicy=resize&w=" + itemWidth + "&h=" + itemHeight;
                                        }
                                    }
                                Bitmap placeholder = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.video_image_placeholder);
                                RoundedBitmapDrawable circularBitmapDrawable = RoundedBitmapDrawableFactory.create(mContext.getResources(), placeholder);
                                circularBitmapDrawable.setCircular(true);
                                    if(!this.infoHover) {
                                        Glide.with(mContext)
                                                .load(imageUrl)
                                                .apply(new RequestOptions().circleCropTransform().diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                                                        .placeholder(circularBitmapDrawable)//video_image_round_placeholder appcms_circular_default_thumbnail
                                                        .error(circularBitmapDrawable))//video_image_placeholder  /*ContextCompat.getDrawable(mContext, R.drawable.video_image_round_placeholder))*/
                                                .into(imageView);
                                    }else{
                                           Glide.with(mContext)
                                            .load(imageUrl)
                                            .apply( new RequestOptions().diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                                                    .placeholder(circularBitmapDrawable)
                                                    .error(circularBitmapDrawable))
                                            .into(imageView);
                                    }

                                if (null != blockName) {
                                    if (blockName.equalsIgnoreCase("newsTray02")) {
                                       if(!this.infoHover && blockName.equalsIgnoreCase("newsTray02"))
                                          imageView.setBackground(Utils.getTrayRoundBorder(mContext, borderColor, childComponentAndView.component));
                                       else
                                           imageView.setBackground(Utils.getTrayBorder(mContext, borderColor, childComponentAndView.component));

                                        if (blockName.equalsIgnoreCase("tray11"))
                                            imageView.setPadding(4, 4, 4, 4);
                                    } else if ((blockName.equalsIgnoreCase("tray03")
                                            || blockName.equalsIgnoreCase("search02")) && infoHover) {
                                        imageView.setBackground(Utils.getTrayBorder(mContext, borderColor, childComponentAndView.component));
                                        imageView.setPadding(5, 5, 5, 5);
                                    }
                                }
                            }
                                break;
                            case PAGE_THUMBNAIL_IMAGE_KEY: {
                                Integer itemWidth = Integer.valueOf(childComponentAndView.component.getLayout().getTv().getWidth());
                                Integer itemHeight = Integer.valueOf(childComponentAndView.component.getLayout().getTv().getHeight());
                                // imageView.setBackground(null);
                                if (itemWidth > itemHeight) {
                                    String imageUrl = null;
                                    if (contentData != null
                                            && contentData.getGist() != null) {
                                        if (contentData.getGist().getVideoImageUrl() != null) {
                                            imageUrl = contentData.getGist().getVideoImageUrl() + "?impolicy=resize&w=" + itemWidth + "&h=" + itemHeight;
                                        } else if (contentData.getImages() != null && contentData.getImages().get_16x9Image() != null
                                                && contentData.getImages().get_16x9Image().getUrl() != null) {
                                            imageUrl = contentData.getImages().get_16x9Image().getUrl() + "?impolicy=resize&w=" + itemWidth + "&h=" + itemHeight;
                                        } else if (contentData.getGist().getPosterImageUrl() != null) {
                                            imageUrl = contentData.getGist().getPosterImageUrl() + "?impolicy=resize&w=" + itemWidth + "&h=" + itemHeight;
                                        }
                                    }
                                    if (TextUtils.isEmpty(imageUrl)
                                            && contentData.getImages() != null) {
                                        if (contentData.getImages().get_16x9() != null
                                                && contentData.getImages().get_16x9().getUrl() != null) {
                                            imageUrl = contentData.getImages().get_16x9().getUrl() + "?impolicy=resize&w=" + itemWidth + "&h=" + itemHeight;
                                        }
                                    }

                                    Glide.with(mContext)
                                            .load(imageUrl)
                                            .apply(new RequestOptions().diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                                                    .placeholder(R.drawable.video_image_placeholder)
                                                    .error(ContextCompat.getDrawable(mContext, R.drawable.video_image_placeholder)))
                                            .into(imageView);
                                } else {
                                    if (contentData != null
                                            && contentData.getGist() != null) {
                                        String imageUrl = null;
                                        if (contentData.getGist().getPosterImageUrl() != null) {
                                            imageUrl = contentData.getGist().getPosterImageUrl() + "?impolicy=resize&w=" + itemWidth + "&h=" + itemHeight;
                                        } else if (contentData.getImages() != null && contentData.getImages().get_3x4() != null
                                                && contentData.getImages().get_3x4().getUrl() != null) {
                                            imageUrl = contentData.getImages().get_3x4().getUrl() + "?impolicy=resize&w=" + itemWidth + "&h=" + itemHeight;
                                        } else if (contentData.getImages() != null && contentData.getImages().get_3x4Image() != null
                                                && contentData.getImages().get_3x4Image().getUrl() != null) {
                                            imageUrl = contentData.getImages().get_3x4Image().getUrl() + "?impolicy=resize&w=" + itemWidth + "&h=" + itemHeight;
                                        } else if (contentData.getGist().getVideoImageUrl() != null) {
                                            imageUrl = contentData.getGist().getVideoImageUrl();
                                            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                                        }
                                        Glide.with(mContext)
                                                .load(imageUrl)
                                                .apply(new RequestOptions().diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                                                        .placeholder(R.drawable.poster_image_placeholder)
                                                        .error(ContextCompat.getDrawable(mContext, R.drawable.poster_image_placeholder)))
                                                .into(imageView);
                                    }
                                }

                                if (null != blockName) {
                                    if (blockName.equalsIgnoreCase("tray01")
                                            || blockName.equalsIgnoreCase("tray02")
                                            || blockName.equalsIgnoreCase("grid01")
                                            || blockName.equalsIgnoreCase("showDetail01")
                                            || blockName.equalsIgnoreCase("showDetail08")
                                            || blockName.equalsIgnoreCase("bundleDetail01")
                                            || blockName.equalsIgnoreCase("tray04")
                                            || blockName.equalsIgnoreCase("continuewatching01")
                                            || blockName.equalsIgnoreCase("continuewatching02")
                                            || blockName.equalsIgnoreCase("categoryDetail01")
                                            | blockName.equalsIgnoreCase("recommendation01")
                                            || blockName.equalsIgnoreCase("newsRecommendation01")
                                            || blockName.equalsIgnoreCase("search01")
                                            || blockName.equalsIgnoreCase("trayFeaturedVideo01")
                                            || blockName.equalsIgnoreCase("tray11")
                                            || blockName.equalsIgnoreCase("userPersonalizatio01")
                                            || blockName.equalsIgnoreCase("userPersonalization01")) {
                                        imageView.setBackground(Utils.getTrayBorder(mContext, borderColor, childComponentAndView.component));
                                        if (blockName.equalsIgnoreCase("tray11"))
                                            imageView.setPadding(4, 4, 4, 4);
                                    } else if ((blockName.equalsIgnoreCase("tray03")
                                            || blockName.equalsIgnoreCase("search02")) && infoHover) {
                                        imageView.setBackground(Utils.getTrayBorder(mContext, borderColor, childComponentAndView.component));
                                        imageView.setPadding(5, 5, 5, 5);
                                    }
                                }
                                break;
                            }
                            case PAGE_TALL_THUMBNAIL_IMAGE_KEY: {
                                Integer itemWidth = Integer.valueOf(childComponentAndView.component.getLayout().getTv().getWidth());
                                Integer itemHeight = Integer.valueOf(childComponentAndView.component.getLayout().getTv().getHeight());
                                // imageView.setBackground(null);

                                if (contentData != null
                                        && contentData.getGist() != null) {
                                    String imageUrl = null;
                                    if (contentData.getGist().getImageGist().get_9x16() != null) {
                                        imageUrl = contentData.getGist().getImageGist().get_9x16() + "?impolicy=resize&w=" + itemWidth + "&h=" + itemHeight;
                                    } else if (contentData.getGist().getPosterImageUrl() != null) {
                                        imageUrl = contentData.getGist().getPosterImageUrl() + "?impolicy=resize&w=" + itemWidth + "&h=" + itemHeight;
                                    } else if (contentData.getImages() != null && contentData.getImages().get_3x4() != null
                                            && contentData.getImages().get_3x4().getUrl() != null) {
                                        imageUrl = contentData.getImages().get_3x4().getUrl() + "?impolicy=resize&w=" + itemWidth + "&h=" + itemHeight;
                                    } else if (contentData.getImages() != null && contentData.getImages().get_3x4Image() != null
                                            && contentData.getImages().get_3x4Image().getUrl() != null) {
                                        imageUrl = contentData.getImages().get_3x4Image().getUrl() + "?impolicy=resize&w=" + itemWidth + "&h=" + itemHeight;
                                    } else if (contentData.getGist().getVideoImageUrl() != null) {
                                        imageUrl = contentData.getGist().getVideoImageUrl();
                                        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                                    }
                                    Glide.with(mContext)
                                            .load(imageUrl)
                                            .apply(new RequestOptions().diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                                                    .placeholder(R.drawable.poster_image_placeholder)
                                                    .error(ContextCompat.getDrawable(mContext, R.drawable.poster_image_placeholder)))
                                            .into(imageView);
                                }


                                if (null != blockName) {
                                    if (blockName.equalsIgnoreCase("grid02")
                                            || blockName.equalsIgnoreCase("tray12")
                                            || blockName.equalsIgnoreCase("recommendation01")
                                            || blockName.equalsIgnoreCase("userPersonalizatio01")
                                            || blockName.equalsIgnoreCase("userPersonalization01")) {
                                        imageView.setBackground(Utils.getTrayBorder(mContext, borderColor, childComponentAndView.component));
                                    }
                                }

                            }
                            break;
                            case PAGE_WIDE_CAROUSEL_IMAGE_KEY: {
                                Integer itemWidth = Integer.valueOf(childComponentAndView.component.getLayout().getTv().getWidth());
                                Integer itemHeight = Integer.valueOf(childComponentAndView.component.getLayout().getTv().getHeight());
                                // imageView.setBackground(null);

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
                                                    .placeholder(R.drawable.video_image_placeholder_wide)
                                                    .error(ContextCompat.getDrawable(mContext, R.drawable.video_image_placeholder_wide)))
                                            .into(imageView);
                                    imageView.setBackground(Utils.getTrayBorder(mContext, borderColor, childComponentAndView.component));

                                }
                            }
                            break;
                            case PAGE_BEDGE_IMAGE_KEY:
                            case PAGE_BADGE_IMAGE_KEY: {
                                if (contentData != null
                                        && null != contentData.getGist()
                                        && null != contentData.getGist().getBadgeImages()) {
                                    Integer badgeItemWidth = Integer.valueOf(childComponentAndView.component.getLayout().getTv().getWidth());
                                    Integer badgeItemHeight = Integer.valueOf(childComponentAndView.component.getLayout().getTv().getHeight());
                                    try {
                                        String imageUrl;
                                        if (badgeItemWidth > badgeItemHeight) {
                                            imageUrl = contentData.getGist().getBadgeImages()
                                                    .get_16x9() + "?impolicy=resize" +
                                                    "&w=" + badgeItemWidth +
                                                    "&h=" + badgeItemHeight;
                                        } else {
                                            imageUrl = contentData.getGist().getBadgeImages()
                                                    .get_3x4() + "?impolicy=resize" +
                                                    "&w=" + badgeItemWidth +
                                                    "&h=" + badgeItemHeight;
                                        }
                                        //      imageView.setBackground(null);
                                        Glide.with(mContext)
                                                .load(imageUrl)
                                                .apply(new RequestOptions().diskCacheStrategy(DiskCacheStrategy.RESOURCE))
                                                .into(imageView);

                                    } catch (Exception e) {

                                    }
                                } else {
                                    imageView.setImageDrawable(null);
                                }
                            }
                            break;

                            case PAGE_WIDE_BADGE_IMAGE_KEY:{
                                if (contentData != null
                                        && null != contentData.getGist()
                                        && null != contentData.getGist().getBadgeImages()) {
                                    Integer badgeItemWidth = Integer.valueOf(childComponentAndView.component.getLayout().getTv().getWidth());
                                    Integer badgeItemHeight = Integer.valueOf(childComponentAndView.component.getLayout().getTv().getHeight());
                                    try {
                                        String imageUrl;
                                        if (contentData.getGist().getBadgeImages().get_32x9() != null) {
                                            imageUrl = contentData.getGist().getBadgeImages()
                                                    .get_32x9() + "?impolicy=resize" +
                                                    "&w=" + badgeItemWidth +
                                                    "&h=" + badgeItemHeight;
                                        } else if (badgeItemWidth > badgeItemHeight) {
                                            imageUrl = contentData.getGist().getBadgeImages()
                                                    .get_16x9() + "?impolicy=resize" +
                                                    "&w=" + badgeItemWidth +
                                                    "&h=" + badgeItemHeight;
                                        } else {
                                            imageUrl = contentData.getGist().getBadgeImages()
                                                    .get_3x4() + "?impolicy=resize" +
                                                    "&w=" + badgeItemWidth +
                                                    "&h=" + badgeItemHeight;
                                        }
                                        //      imageView.setBackground(null);
                                        Glide.with(mContext)
                                                .load(imageUrl)
                                                .apply(new RequestOptions().diskCacheStrategy(DiskCacheStrategy.RESOURCE))
                                                .into(imageView);

                                    } catch (Exception e) {

                                    }
                                } else {
                                    imageView.setImageDrawable(null);
                                }
                            }
                                break;

                            case WEATHER_CLOUD_IMAGE:
                                int drawableId = -1;
                                if(weatherInterval.equalsIgnoreCase("hourly")){
                                    Hour hour  = (Hour)weatherHour;
                                    String iconCode = hour.getSkyCode();
                                    if(iconCode != null)
                                    drawableId = getWeatherIconCode(iconCode);
                                }else{
                                    Day day = (Day)weatherHour;
                                    String iconCode = day.getSkyCode();
                                    if(iconCode != null)
                                        drawableId = getWeatherIconCode(iconCode);
                                }
                                imageView.setBackground(drawableId !=-1 ? mContext.getDrawable(drawableId) : mContext.getDrawable(R.drawable.ic_weather_na));
                                break;

                            case WEATHER_DROP_IMAGE:
                                imageView.setBackground(mContext.getDrawable(R.drawable.raindrop_close_up));
                                break;

                            case NEWS_THUMBNAIL_IMAGE:
                            {
                                int childViewWidth = (int) getViewWidth(mContext,
                                        childComponentAndView.component.getLayout(),
                                        ViewGroup.LayoutParams.MATCH_PARENT);
                                int childViewHeight = (int) getViewHeight(mContext,
                                        childComponentAndView.component.getLayout(),
                                        getViewHeight(mContext,
                                                childComponentAndView.component.getLayout(),
                                                ViewGroup.LayoutParams.WRAP_CONTENT));

                                String imageUrl =
                                        mContext.getString(R.string.app_cms_image_with_resize_query,
                                                contentData.getGist().getImageGist().get_16x9(),
                                                childViewWidth,
                                                childViewHeight);
                                //Log.d(TAG, "Loading image: " + imageUrl);
                                Glide.with(mContext)
                                        .load(imageUrl)
                                        .apply(new RequestOptions().diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                                                .centerCrop()
                                                .placeholder(R.drawable.video_image_placeholder)
                                                .error(ContextCompat.getDrawable(mContext, R.drawable.video_image_placeholder)))
                                        .into((ImageView) imageView);
                                imageView.setFocusable(true);

                                imageView.setBackground(Utils.getTrayBorder(mContext, borderColor, childComponentAndView.component));
                                imageView.setPadding(3, 3, 3, 3);

                                imageView.setOnKeyListener(new View.OnKeyListener() {
                                    @Override
                                    public boolean onKey(View view, int keyCode, KeyEvent keyEvent) {
                                        if (keyCode == KeyEvent.KEYCODE_DPAD_UP) {
                                            if (keyEvent.getAction() == KeyEvent.ACTION_DOWN) {
                                                if(rowData.itemPosition == 0){
                                                    imageView.clearFocus();
                                                }
                                            }
                                        }
                                        return false;
                                    }
                                });

                                imageView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                                    @Override
                                    public void onFocusChange(View view, boolean b) {
                                        if (mContext instanceof AppCmsHomeActivity) {
                                            ((AppCmsHomeActivity) mContext).shouldShowLeftNavigation(b);
                                        }
                                    }
                                });

                                imageView.setOnClickListener(v -> {
                                    try {
                                        ContentDatum item = rowData.contentDatumList.get(rowData.itemPosition);
                                        Gist gist;
                                        if(null == (gist = item.getGist())){
                                            mAppCmsPresenter.navigateToExpandedDetailPage(null,rowData.contentDatumList,rowData.itemPosition);
                                            return;
                                        }
                                        String contentType = gist.getContentType();
                                        String permalink = gist.getPermalink();
                                        String title = gist.getTitle();
                                        String hlsUrl = getHlsUrl(item);
                                        String[] extraData = new String[4];
                                        extraData[0] = permalink;
                                        extraData[1] = hlsUrl;
                                        extraData[2] = gist.getId();
                                        if (null != mAppCmsPresenter && !contentType.equalsIgnoreCase("") && (contentType.equalsIgnoreCase("SERIES")
                                                || contentType.equalsIgnoreCase("SEASON")
                                                || contentType.equalsIgnoreCase("SEASONS")
                                                || contentType.equalsIgnoreCase("shows"))) {
                                            if (!mAppCmsPresenter.launchTVButtonSelectedAction(permalink,
                                                    "showDetailPage",
                                                    title,
                                                    extraData,
                                                    item,
                                                    false,
                                                    -1,
                                                    rowData.relatedVideoIds,
                                                    null)) {
                                            }
                                        } else {
                                            mAppCmsPresenter.navigateToExpandedDetailPage(null, rowData.contentDatumList, rowData.itemPosition);
                                        }
                                    }catch (Exception e){
                                        mAppCmsPresenter.navigateToExpandedDetailPage(null,rowData.contentDatumList,rowData.itemPosition);
                                    }
                                });

                            }
                            break;
                        }
                        break;

                    case PAGE_LABEL_KEY:
                        TextView tvTitle = (TextView) childComponentAndView.childView;
                        //tvTitle.setText(mContext.getResources().getString(R.string.blank_string));
                        if (componentKey.equals(AppCMSUIKeyType.PAGE_THUMBNAIL_TIME_AND_DATE_KEY)) {
                            StringBuilder stringBuilder = new StringBuilder();
                            if (mAppCmsPresenter.getAppCMSMain().getBrand().getMetadata() != null) {
                                String time = Utils.convertSecondsToTime(contentData.getGist().getRuntime());
                                String date = null;
                                if(null != contentData
                                        && null != contentData.getGist()
                                        && null != contentData.getGist().getPublishDate()) {
                                    try {
                                        date = mAppCmsPresenter.getDateFormat(
                                                Long.parseLong(contentData.getGist().getPublishDate()),
                                                "MMMM dd, yyyy");
                                    } catch (Exception e) {
                                    }
                                }
                                if (mAppCmsPresenter.getAppCMSMain().getBrand().getMetadata().isDisplayDuration()) {
                                    stringBuilder.append(time);
                                }
                                if (mAppCmsPresenter.getAppCMSMain().getBrand().getMetadata().isDisplayPublishedDate() && null != date) {
                                    if (stringBuilder.length() > 0) stringBuilder.append(" | ");
                                    stringBuilder.append(date);
                                }
                                tvTitle.setVisibility(View.VISIBLE);
                            } else /*Don't show time and date as metadata is null*/ {
                                tvTitle.setVisibility(View.INVISIBLE);
                            }
                            tvTitle.setText(stringBuilder);
                            tvTitle.setTextSize(childComponentAndView.component.getFontSize());
                        } else if (componentKey.equals(AppCMSUIKeyType.PAGE_NEWS_THUMBNAIL_TIME_AND_DATE_KEY)){
                            if(contentData.getGist() != null && contentData.getGist().getContentType() != null &&
                                    !(contentData.getGist().getContentType().equalsIgnoreCase("series")
                                            || contentData.getGist().getContentType().equalsIgnoreCase("show"))) {
                                final int SECONDS_PER_MINS = 60;
                                int duration = 0;
                                String suffixText = mAppCmsPresenter.getLocalisedStrings().getMinText();
                                duration = (int) (contentData.getGist().getRuntime() / SECONDS_PER_MINS);
                                if (duration == 0) {
                                    suffixText = mAppCmsPresenter.getLocalisedStrings().getSecText();
                                    duration = (int) ((contentData.getGist().getRuntime()) % SECONDS_PER_MINS);
                                }

                                StringBuilder runtimeText = new StringBuilder()
                                        .append(duration)
                                        .append(" ")
                                        .append(suffixText);
                                tvTitle.setText(runtimeText);
                                tvTitle.setVisibility(View.VISIBLE);
                                tvTitle.setBackgroundColor(Color.parseColor(mAppCmsPresenter.getAppBackgroundColor()));
                            }else{
                                tvTitle.setVisibility(View.GONE);
                            }
                        }else if (componentKey.equals(AppCMSUIKeyType.PAGE_EPISODE_THUMBNAIL_TITLE_KEY)) {
                            try {
                                tvTitle.setEllipsize(TextUtils.TruncateAt.END);
                                int episodeNumber = getEpisodeNumber(contentData,
                                        contentData.getGist().getId());
                                String text = contentData.getGist().getTitle();
                                SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder(Integer.toString(episodeNumber));
                                spannableStringBuilder.append(" ").append(text);
                                tvTitle.setText(spannableStringBuilder);
                                Typeface font = Utils.getSpecificTypeface(mContext, mAppCmsPresenter, mContext.getString(R.string.opensans_extrabold_ttf));
                                spannableStringBuilder.setSpan(new ForegroundColorSpan(Color.parseColor("#7b7b7b")), 0, 2, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                                spannableStringBuilder.setSpan(new CustomTypefaceSpan("", font), 0, 2, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
                                tvTitle.setText(spannableStringBuilder);
                            }catch (Exception e){

                            }
                        } else if (componentKey.equals(AppCMSUIKeyType.PAGE_THUMBNAIL_TITLE_KEY)){
                            if (contentData != null && contentData.getGist() != null
                                    && contentData.getGist().getTitle() != null) {
                                tvTitle.setEllipsize(TextUtils.TruncateAt.END);
                                tvTitle.setText(contentData.getGist().getTitle());
                                tvTitle.setMaxLines(2);
                            } else if (contentData != null && contentData.getTitle() != null) {
                                tvTitle.setEllipsize(TextUtils.TruncateAt.END);
                                tvTitle.setText(contentData.getTitle());
                                tvTitle.setMaxLines(2);
                            }
                        } else if (componentKey.equals(AppCMSUIKeyType.PAGE_THUMBNAIL_TITLE_WITH_BG_KEY)){
                            if (!isNoInfo) {
                                StringBuilder stringBuilder = new StringBuilder();
                                if (contentData != null && contentData.getGist() != null
                                        && contentData.getGist().getTitle() != null) {
                                    stringBuilder.append(contentData.getGist().getTitle());
                                }
                                if (contentData != null && contentData.getGist() != null
                                        && contentData.getGist().getRuntime() > 0) {
                                    if (stringBuilder.length() > 0) stringBuilder.append(" | ");

                                    final int SECONDS_PER_MINS = 60;
                                    int duration = 0;
                                    String suffixText = mAppCmsPresenter.getLocalisedStrings().getMinText();
                                    duration = (int) (contentData.getGist().getRuntime() / SECONDS_PER_MINS);
                                    if (duration == 0) {
                                        suffixText = mAppCmsPresenter.getLocalisedStrings().getSecText();
                                        duration = (int) ((contentData.getGist().getRuntime()) % SECONDS_PER_MINS);
                                    }

                                    StringBuilder runtimeText = new StringBuilder()
                                            .append(duration)
                                            .append(" ")
                                            .append(suffixText);
                                    stringBuilder.append(runtimeText.toString().toLowerCase());
                                }
                                if (contentData != null && contentData.getGist() != null
                                        && contentData.getGist().getPrimaryCategory() != null
                                        && contentData.getGist().getPrimaryCategory().getTitle() != null) {
                                    if (stringBuilder.length() > 0) stringBuilder.append(" | ");
                                    stringBuilder.append(contentData.getGist().getPrimaryCategory().getTitle().toUpperCase());
                                }

                                if (stringBuilder.length() > 0) {
                                    tvTitle.setEllipsize(TextUtils.TruncateAt.END);
                                    tvTitle.setText(stringBuilder.toString());
                                    tvTitle.setMaxLines(2);
                                } else if (contentData != null && contentData.getTitle() != null) {
                                    tvTitle.setEllipsize(TextUtils.TruncateAt.END);
                                    tvTitle.setText(contentData.getTitle());
                                    tvTitle.setMaxLines(2);
                                }
                            }
                        } else if (componentKey.equals(AppCMSUIKeyType.PAGE_VIDEO_TITLE_ON_HOVER_KEY)){
                            tvTitle.setMaxLines(childComponentAndView.component.getNumberOfLines());
                            tvTitle.setText(contentData.getGist().getTitle());
                            //tvTitle.setEllipsize(TextUtils.TruncateAt.MARQUEE);
                            tvTitle.setSelected(true);
                        }else if (componentKey.equals(AppCMSUIKeyType.PAGE_VIDEO_SUB_TITLE_ON_HOVER_KEY)){
                            tvTitle.setMaxLines(childComponentAndView.component.getNumberOfLines());
                            try {
                                if (contentData.getGist().getContentType() != null
                                        && contentData.getGist().getContentType().equalsIgnoreCase("SERIES")) {
                                    TVBaseView.setShowViewWithSubtitle(mContext,
                                            module, contentData,
                                            tvTitle, mAppCmsPresenter);
                                } else {
                                    TVBaseView.setVideoViewWithSubtitle(mContext,
                                            contentData,
                                            tvTitle,
                                            mAppCmsPresenter);
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        } else if (componentKey.equals(AppCMSUIKeyType.PAGE_VIDEO_DESCRIPTION_ON_HOVER_KEY)) {
                            tvTitle.setMaxLines(childComponentAndView.component.getNumberOfLines());
                            tvTitle.setText(contentData.getGist().getDescription());
                        }else if(componentKey.equals(AppCMSUIKeyType.WEATHER_TEMP_HIGH_LABEL)){
                            tvTitle.setTextColor(Color.parseColor(childComponentAndView.component.getTextColor()));
                        }else if(componentKey.equals(AppCMSUIKeyType.WEATHER_TEMP_LOW_LABEL)){
                            if(weatherInterval.equalsIgnoreCase("hourly")){
                                Hour hour  = (Hour)weatherHour;
                                tvTitle.setText(hour.getTempF() + Utils.DEGREE /*+ " / " + hour.getTempC() + Utils.DEGREE*/);
                            }else{
                                Day day = (Day)weatherHour;
                                tvTitle.setText(day.getHiTempF() + Utils.DEGREE + " / " + day.getLoTempF() + Utils.DEGREE);
                            }
                            tvTitle.setTextColor(Color.parseColor(childComponentAndView.component.getTextColor()));
                        }else if(componentKey.equals(AppCMSUIKeyType.WEATHER_SHORT_PHRASE_LABEL)){
                            if(weatherInterval.equalsIgnoreCase("hourly")){
                                Hour hour  = (Hour)weatherHour;
                                tvTitle.setText(hour.getSkyLong());
                            }else{
                                Day day = (Day)weatherHour;
                                tvTitle.setText(day.getShortPhrase());
                            }
                            tvTitle.setEllipsize(TextUtils.TruncateAt.MARQUEE);
                            tvTitle.setSelected(false);
                            tvTitle.setTextColor(Color.parseColor(childComponentAndView.component.getTextColor()));
                        }else if(componentKey.equals(AppCMSUIKeyType.WEATHER_THUMBNAIL_TITLE_LABEL)){
                            if(weatherInterval.equalsIgnoreCase("hourly")){
                                Hour hour  = (Hour)weatherHour;
                                if(hour.getValidDateUtc() != null ){
                                    try {
                                        Date date1=new SimpleDateFormat("MM/dd/yyyy hh:mm:ss aa").parse(hour.getValidDateLocal());
                                        System.out.println("date1 = "+"\t"+date1);

                                        String[] dateAndTime = hour.getValidDateUtc().split(" ");
                                        tvTitle.setText(android.text.format.DateFormat.format("h aa",date1)/*dateAndTime[1] +" "+ dateAndTime[2]*/);

                                    }catch (Exception e){
                                        tvTitle.setText(hour.getValidDateUtc());
                                    }
                                }
                            }else{
                                Day day = (Day)weatherHour;
                                try {
                                    Date date1=new SimpleDateFormat("MM/dd/yyyy hh:mm:ss aa").parse(day.getValidDateLocal());
                                    System.out.println("date1 = "+"\t"+date1);
                                    tvTitle.setText(android.text.format.DateFormat.format("EEEE MM/dd",date1));
                                   // tvTitle.setText(day.getDayOfWk()  +" "+ dateAndTime[0]);
                                   /* String[] dateAndTime = day.getValidDateUtc().split(" ");
                                    tvTitle.setText(day.getDayOfWk()  +" "+ dateAndTime[0]);*/
                                }catch (Exception e){
                                    tvTitle.setText(day.getDayOfWk() + " " + day.getValidDateLocal());
                                }

                            }

                        }else if(componentKey.equals(AppCMSUIKeyType.WEATHER_DROP_PERCENTAGE_LABEL)){

                            if(weatherInterval.equalsIgnoreCase("hourly")){
                                Hour hour  = (Hour)weatherHour;
                                tvTitle.setText(hour.getPrecipChance() + "%");
                            }else{
                                Day day = (Day)weatherHour;
                                tvTitle.setText(day.getPrecipChance() + "%");
                            }
                             tvTitle.setTextColor(Color.parseColor(childComponentAndView.component.getTextColor()));

                        } else if (componentKey == PAGE_THUMBNAIL_DESCRIPTION_KEY) {
                            if(contentData.getGist() != null && contentData.getGist().getDescription() != null){
                                tvTitle.setText(contentData.getGist().getDescription());
                            }
                        }
                        //tvTitle.setSingleLine(true);
//                        tvTitle.setEllipsize(TextUtils.TruncateAt.END);
//                        tvTitle.setSelected(true);

                        break;

                    case PAGE_PROGRESS_VIEW_KEY:
                        ProgressBar progressBar = (ProgressBar) childComponentAndView.childView;
                        progressBar.setProgress(0);
                        int progress = 0;
                        ContentDatum userHistoryContentDatum = null;
                        if (contentData != null && contentData.getGist() != null
                                && contentData.getGist().getId() != null) {
                            userHistoryContentDatum = mAppCmsPresenter.getUserHistoryContentDatum(contentData.getGist().getId());
                        }
                        if (userHistoryContentDatum != null && userHistoryContentDatum.getGist() != null) {
                            progress = (int) Math.ceil(Utils.getPercentage(userHistoryContentDatum.getGist().getRuntime(), userHistoryContentDatum.getGist().getWatchedTime()));
                        }
                        if(progress > 0) {
                            progressBar.setProgress(progress);
                            progressBar.setVisibility(View.VISIBLE);
                        }else{
                            progressBar.setVisibility(View.INVISIBLE);
                        }
                        break;
                }
            }
        }
    }
    private int getEpisodeNumber(ContentDatum mainContentData, String id) {
        int returnVal = 0;
        if (mainContentData.getSeason() != null) {
            for (int seasonNumber = 0; seasonNumber < mainContentData.getSeason().size(); seasonNumber++) {
                Season_ season = mainContentData.getSeason().get(seasonNumber);
                for (int episodeNumber = 0; episodeNumber < season.getEpisodes().size(); episodeNumber++) {
                    ContentDatum contentDatum = season.getEpisodes().get(episodeNumber);
                    if (contentDatum.getGist().getId().equalsIgnoreCase(id)) {
                        returnVal = episodeNumber + 1;
                        break;
                    }
                }
                if (returnVal > 0) break;
            }
        }

        return returnVal;
    }


    public class CustomFrameLayout extends FrameLayout{
        List<CustomFrameLayout.ChildComponentAndView> childView = null;
        TextView hoverTitle;
        TextView thumbnailTitle;
        TextView hoverSubTitle;
        TextView hoverDescription;
        View hoverBackground;
        LinearLayout hoverLayout;

        public CustomFrameLayout(@NonNull Context context) {
            super(context);
            childView = new ArrayList<>();
            hoverLayout = new HoverCard(context,mAppCmsPresenter,this);
            this.addView(hoverLayout);
        }

        public void setThumbnailTitle(TextView thumbnailTitle) {
            this.thumbnailTitle = thumbnailTitle;
        }

        public TextView getHoverTitle() {
            return hoverTitle;
        }

        public void setHoverTitle(TextView hoverTitle) {
            this.hoverTitle = hoverTitle;
        }

        public TextView getHoverSubTitle() {
            return hoverSubTitle;
        }

        public void setHoverSubTitle(TextView hoverSubTitle) {
            this.hoverSubTitle = hoverSubTitle;
        }

        public TextView getHoverDescription() {
            return hoverDescription;
        }

        public void setHoverDescription(TextView hoverDescription) {
            this.hoverDescription = hoverDescription;
        }

        public View getHoverBackground() {
            return hoverBackground;
        }

        public void setHoverBackground(View hoverBackground) {
            this.hoverBackground = hoverBackground;
        }

        public List<CustomFrameLayout.ChildComponentAndView> getChildViewList() {
            return childView;
        }

        public void addChildComponentAndView(View componentView, Component component) {
            CustomFrameLayout.ChildComponentAndView childComponentAndView = new CustomFrameLayout.ChildComponentAndView();
            childComponentAndView.childView = componentView;
            childComponentAndView.component = component;
            childView.add(childComponentAndView);
        }

        @Override
        public void setSelected(boolean selected) {
            super.setSelected(selected);
            if (selected) {
                if (hoverTitle != null
                        && hoverSubTitle != null
                        && hoverDescription != null
                        && hoverLayout != null) {
                    hoverLayout.bringToFront();
                    hoverLayout.setVisibility(VISIBLE);
                    startHoverAnimation(thumbnailTitle, hoverTitle, hoverSubTitle, hoverDescription, hoverBackground, false);
                }
            } else {
                if (hoverTitle != null
                        && hoverSubTitle != null
                        && hoverDescription != null
                        && hoverLayout != null) {
                    startHoverAnimation(thumbnailTitle, hoverTitle, hoverSubTitle, hoverDescription, hoverBackground, true);
                }
            }
        }

        public void startHoverAnimation(TextView thumbnailTitle,
                                        TextView hoverTitle,
                                        TextView hoverSubTitle,
                                        TextView hoverDescription,
                                        View hoverBackground,
                                        boolean reverse) {

            int translateStartPosition = 20;
            int translateEndPosition = 0;
            int duration = 500;

            float alphaStartVal = 0f;
            float alphaEndVal = 1f;
            AccelerateDecelerateInterpolator interpolator = new AccelerateDecelerateInterpolator();

            if (thumbnailTitle != null) {
                ObjectAnimator alphaThumbnail = ObjectAnimator.ofFloat(thumbnailTitle, "alpha", alphaEndVal, alphaStartVal);
                alphaThumbnail.setDuration(duration / 2);
                alphaThumbnail.setInterpolator(interpolator);

                if (reverse) {
                    alphaThumbnail.reverse();
                } else {
                    alphaThumbnail.start();
                }
            }

            ObjectAnimator translationY = ObjectAnimator.ofFloat(hoverTitle, "translationY", translateStartPosition, translateEndPosition);
            translationY.setDuration(duration);
            translationY.setInterpolator(interpolator);

            if (reverse) {
                translationY.reverse();
            } else {
                translationY.start();
            }

            ObjectAnimator translationY1 = ObjectAnimator.ofFloat(hoverSubTitle, "translationY", translateStartPosition, translateEndPosition);
            translationY1.setDuration(duration);
            translationY1.setInterpolator(interpolator);

            if (reverse) {
                translationY1.reverse();
            } else {
                translationY1.setStartDelay(duration / 4);
                translationY1.start();
            }

            ObjectAnimator translationY2 = ObjectAnimator.ofFloat(hoverDescription, "translationY", translateStartPosition, translateEndPosition);
            translationY2.setDuration(duration);
            translationY2.setInterpolator(interpolator);
            translationY2.addListener(getAnimatorListener());

            if (reverse) {
                translationY2.reverse();
            } else {
                translationY2.setStartDelay(duration/2);
                translationY2.start();
            }

            ObjectAnimator alpha = ObjectAnimator.ofFloat(hoverTitle, "alpha", alphaStartVal, alphaEndVal);
            alpha.setDuration(duration);
            alpha.setInterpolator(interpolator);

            if (reverse) alpha.reverse();
            else alpha.start();

            ObjectAnimator alpha1 = ObjectAnimator.ofFloat(hoverSubTitle, "alpha", alphaStartVal, alphaEndVal);
            alpha1.setDuration(duration);
            alpha1.setInterpolator(interpolator);

            if (reverse) {
                alpha1.reverse();
            } else {
                alpha1.setStartDelay(duration / 4);
                alpha1.start();
            }

            ObjectAnimator alpha2 = ObjectAnimator.ofFloat(hoverDescription, "alpha", alphaStartVal, alphaEndVal);
            alpha2.setDuration(duration);
            alpha2.setInterpolator(interpolator);

            if (reverse) {
                alpha2.reverse();
            } else {
                alpha2.setStartDelay(duration/2);
                alpha2.start();
            }

            ObjectAnimator alpha3 = ObjectAnimator.ofFloat(hoverBackground, "alpha", alphaStartVal, alphaEndVal);
            alpha3.setDuration(duration);
            alpha3.setInterpolator(interpolator);

            if (reverse) alpha3.reverse();
            else alpha3.start();

            hoverTitle.setEllipsize(TextUtils.TruncateAt.MARQUEE);
        }

        @NonNull
        private Animator.AnimatorListener getAnimatorListener() {
            return new Animator.AnimatorListener() {
                        @Override
                        public void onAnimationStart(Animator animator) {
                            CustomFrameLayout.this.hoverLayout.setVisibility(VISIBLE);
                        }

                        @Override
                        public void onAnimationEnd(Animator animator) {
                            if (!CustomFrameLayout.this.isSelected()) {
                                CustomFrameLayout.this.hoverLayout.setVisibility(INVISIBLE);
                            }

                        }

                        @Override
                        public void onAnimationCancel(Animator animator) {

                        }

                        @Override
                        public void onAnimationRepeat(Animator animator) {

                        }
                    };
        }

        public class ChildComponentAndView {
            Component component;
            View childView;
        }

    }



    public int getWeatherIconCode(String iconCode){
        switch(iconCode){
            case "00":
                return R.drawable.ic_weather_00;
            case "01":
                return R.drawable.ic_weather_01;
            case "02":
                return R.drawable.ic_weather_02;
            case "03":
                return R.drawable.ic_weather_03;
            case "04":
                return R.drawable.ic_weather_04;
            case "05":
                return R.drawable.ic_weather_05;
            case "06":
                return R.drawable.ic_weather_06;

            case "07":
                return R.drawable.ic_weather_07;
            case "08":
                return R.drawable.ic_weather_08;
            case "09":
                return R.drawable.ic_weather_09;

            case "10":
                return R.drawable.ic_weather_10;

            case "11":
                return R.drawable.ic_weather_11;

            case "12":
                return R.drawable.ic_weather_12;

            case "13":
                return R.drawable.ic_weather_13;

            case "14":
                return R.drawable.ic_weather_14;

            case "15":
                return R.drawable.ic_weather_15;

            case "16":
                return R.drawable.ic_weather_16;

            case "17":
                return R.drawable.ic_weather_17;

            case "18":
                return R.drawable.ic_weather_18;

            case "19":
                return R.drawable.ic_weather_19;

            case "20":
                return R.drawable.ic_weather_20;

            case "21":
                return R.drawable.ic_weather_21;

            case "22":
                return R.drawable.ic_weather_22;

            case "23":
                return R.drawable.ic_weather_23;

            case "24":
                return R.drawable.ic_weather_24;

            case "25":
                return R.drawable.ic_weather_25;

            case "26":
                return R.drawable.ic_weather_26;

            case "27":
                return R.drawable.ic_weather_27;

            case "28":
                return R.drawable.ic_weather_28;

            case "29":
                return R.drawable.ic_weather_29;

            case "30":
                return R.drawable.ic_weather_30;

            case "31":
                return R.drawable.ic_weather_31;

            case "32":
                return R.drawable.ic_weather_32;

            case "33":
                return R.drawable.ic_weather_33;

            case "34":
                return R.drawable.ic_weather_34;

            case "35":
                return R.drawable.ic_weather_35;

            case "36":
                return R.drawable.ic_weather_36;

            case "37":
                return R.drawable.ic_weather_37;

            case "38":
                return R.drawable.ic_weather_38;

            case "39":
                return R.drawable.ic_weather_39;

            case "40":
                return R.drawable.ic_weather_40;

            default:
                return R.drawable.ic_weather_na;


        }

    }

    private String getDefaultAction(Context context, Component component) {
        if (null != component.getItemClickAction()) {
            return component.getItemClickAction();
        }
        return context.getString(R.string.app_cms_action_videopage_key);
    }

    private String getHlsUrl(ContentDatum data) {
        if (data.getStreamingInfo() != null &&
                data.getStreamingInfo().getVideoAssets() != null &&
                data.getStreamingInfo().getVideoAssets().getHls() != null) {
            return data.getStreamingInfo().getVideoAssets().getHls();
        }
        return null;
    }


    public void play(Component childComponent, ContentDatum contentDatum) {
        //Log.d(TAG, "Clicked on item: " + contentDatum.getGist().getTitle());
        String contentType = contentDatum.getGist().getContentType();
        boolean typeSeries = "SERIES".equalsIgnoreCase(contentType)
                || "SEASON".equalsIgnoreCase(contentType);
        long scheduleStartDate = contentDatum.getGist().getScheduleStartDate();
        long scheduleEndDate = contentDatum.getGist().getScheduleEndDate();
        long currentTimeMillis = System.currentTimeMillis();

        if ((contentDatum.getPricing() != null && contentDatum.getPricing().getType() != null && (contentDatum.getPricing().getType().equalsIgnoreCase("TVOD") ||
                contentDatum.getPricing().getType().equalsIgnoreCase("PPV"))) ||
                (contentDatum.getGist().getPurchaseType() != null && (contentDatum.getGist().getPurchaseType().equalsIgnoreCase("RENT")))) {
            mAppCmsPresenter.getTransactionData(contentDatum.getGist().getId(), maps -> {
                if (maps != null
                        && maps.get(0) != null
                        && maps.get(0).get(contentDatum.getGist().getId()) != null) {
                    AppCMSTransactionDataValue appCMSTransactionDataValue = maps.get(0).get(contentDatum.getGist().getId());

                    if (appCMSTransactionDataValue.getTransactionEndDate() != 0) {
                        ClearDialogFragment clearDialogFragment = Utils.getClearDialogFragment(
                                mContext,
                                mAppCmsPresenter,
                                mContext.getResources().getDimensionPixelSize(R.dimen.text_clear_dialog_width),
                                mContext.getResources().getDimensionPixelSize(R.dimen.text_clear_dialog_height),
                                null,
                                mAppCmsPresenter.getLanguageResourcesFile().getStringValue(mContext.getString(R.string.rent_start_msg), String.valueOf(appCMSTransactionDataValue.getRentalPeriod())),
                                mAppCmsPresenter.getLanguageResourcesFile().getUIresource(mContext.getString(R.string.label_play)),
                                mAppCmsPresenter.getLanguageResourcesFile().getUIresource(mContext.getString(R.string.cancel)),
                                13f
                        );

                        clearDialogFragment.setOnPositiveButtonClicked(s -> {
                            mAppCmsPresenter.launchTVVideoPlayer(
                                    contentDatum,
                                    0,
                                    null,
                                    contentDatum.getGist().getWatchedTime(),
                                    null);
                        });
                    } else {
                        mAppCmsPresenter.launchTVVideoPlayer(
                                contentDatum,
                                0,
                                null,
                                contentDatum.getGist().getWatchedTime(),
                                null);
                    }


                } else {
                    Utils.getClearDialogFragment(
                            mContext,
                            mAppCmsPresenter,
                            mContext.getResources().getDimensionPixelSize(R.dimen.text_clear_dialog_width),
                            mContext.getResources().getDimensionPixelSize(R.dimen.text_clear_dialog_height),
                            null,
                            mAppCmsPresenter.getLanguageResourcesFile().getUIresource(mContext.getString(R.string.cannot_purchase_msg)),
                            "BACK",
                            null,
                            10f
                    );
                }
            },contentDatum.getContentType());
        } else if ((contentDatum.getPricing() != null && contentDatum.getPricing().getType() != null && (contentDatum.getPricing().getType().equalsIgnoreCase("TVOD") ||
                contentDatum.getPricing().getType().equalsIgnoreCase("PPV"))) ||
                (contentDatum.getGist().getPurchaseType() != null && (contentDatum.getGist().getPurchaseType().equalsIgnoreCase("PURCHASE")))) {
            mAppCmsPresenter.getTransactionData(contentDatum.getGist().getId(), maps -> {
                if (maps != null
                        && maps.get(0) != null
                        && maps.get(0).get(contentDatum.getGist().getId()) != null) {
                    AppCMSTransactionDataValue appCMSTransactionDataValue = maps.get(0).get(contentDatum.getGist().getId());
                    if (contentDatum.getGist().getScheduleStartDate() > System.currentTimeMillis()) {
                        //click(itemView, childComponent, contentDatum);
                    } else {
                        mAppCmsPresenter.launchTVVideoPlayer(
                                contentDatum,
                                0,
                                null,
                                contentDatum.getGist().getWatchedTime(),
                                null);
                    }
                } else {
                    Utils.getClearDialogFragment(
                            mContext,
                            mAppCmsPresenter,
                            mContext.getResources().getDimensionPixelSize(R.dimen.text_clear_dialog_width),
                            mContext.getResources().getDimensionPixelSize(R.dimen.text_clear_dialog_height),
                            null,
                            mAppCmsPresenter.getLanguageResourcesFile().getUIresource(mContext.getString(R.string.cannot_purchase_msg)),
                            mAppCmsPresenter.getLanguageResourcesFile().getUIresource(mContext.getString(R.string.back)),
                            null,
                            10f
                    );
                }
            }, contentDatum.getContentType());
        } else {
            if (!mAppCmsPresenter.isScheduleVideoPlayable(contentDatum.getGist().getScheduleStartDate(), contentDatum.getGist().getScheduleEndDate(),null)) {
                return;
            }
            mAppCmsPresenter.launchTVVideoPlayer(
                    contentDatum,
                    0,
                    null,
                    contentDatum.getGist().getWatchedTime(),
                    null);
        }
    }

}