package com.viewlift.tv.views.customviews;

import android.content.Context;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.viewlift.R;
import com.viewlift.models.data.appcms.api.ContentDatum;
import com.viewlift.models.data.appcms.api.Module;
import com.viewlift.models.data.appcms.api.Season_;
import com.viewlift.models.data.appcms.ui.AppCMSUIKeyType;
import com.viewlift.models.data.appcms.ui.page.Component;
import com.viewlift.models.data.appcms.ui.page.Layout;
import com.viewlift.models.data.appcms.ui.tv.FireTV;
import com.viewlift.presenters.AppCMSPresenter;
import com.viewlift.tv.utility.Utils;

import java.util.Map;

import static com.viewlift.models.data.appcms.ui.AppCMSUIKeyType.VIEW_GRAVITY_CENTER_HORIZONTAL;
import static com.viewlift.models.data.appcms.ui.AppCMSUIKeyType.VIEW_GRAVITY_END;
import static com.viewlift.tv.utility.Utils.getViewHeight;
import static com.viewlift.tv.utility.Utils.getViewWidth;


/**
 * Created by nitin.tyagi on 7/12/2017.
 */

public abstract class TVBaseView extends FrameLayout {


    protected static int DEVICE_WIDTH;
    protected static int DEVICE_HEIGHT;
    public static final int STANDARD_MOBILE_WIDTH_PX = 1920;
    public static final int STANDARD_MOBILE_HEIGHT_PX = 1080;
    private static float LETTER_SPACING = 0.05f;
    private ViewGroup childrenContainer;
    protected boolean[] componentHasViewList;

    public TVBaseView(@NonNull Context context) {
        super(context);
        DEVICE_WIDTH = Utils.getDeviceWidth(getContext());
        DEVICE_HEIGHT = Utils.getDeviceHeight(getContext());
    }


    public abstract void init();

    protected abstract Component getChildComponent(int index);

    protected abstract Layout getLayout();


    public static void setShowViewWithSubtitle(Context context, Module moduleAPI, ContentDatum data, View view, AppCMSPresenter mAppCMSPresenter) {
        int number = 0;
        String seasonsText = mAppCMSPresenter.getLanguageResourcesFile().getUIresource(context.getResources().getString(R.string.seasons));
        String programText = mAppCMSPresenter.getLanguageResourcesFile().getUIresource(context.getResources().getString(R.string.program));
        String episodeText = mAppCMSPresenter.getLanguageResourcesFile().getUIresource(context.getResources().getString(R.string.episode));
        String programsText = mAppCMSPresenter.getLanguageResourcesFile().getUIresource(context.getResources().getString(R.string.programs));
        String episodesText = mAppCMSPresenter.getLanguageResourcesFile().getUIresource(context.getResources().getString(R.string.episodes));

        seasonsText = mAppCMSPresenter.getLocalisedStrings().getHoverSeasonsLabel();
        if (moduleAPI != null && moduleAPI.getMetadataMap() != null
                && moduleAPI.getMetadataMap().getProgramLabel() != null) {
            programText = moduleAPI.getMetadataMap().getProgramLabel();
        }
        episodeText = mAppCMSPresenter.getLocalisedStrings().getHoverEpisodeLabel();

        if (moduleAPI != null && moduleAPI.getMetadataMap() != null
                && moduleAPI.getMetadataMap().getProgramsLabel() != null) {
            programsText = moduleAPI.getMetadataMap().getProgramsLabel();
        }
        episodesText = mAppCMSPresenter.getLocalisedStrings().getHoverEpisodesLabel();

        StringBuilder stringBuilder = new StringBuilder();
        if (data.getSeason() != null && data.getSeason().size() > 1) {
            number = data.getSeason().size();
            stringBuilder.append(number).append(" ");
            stringBuilder.append(seasonsText);
        } else if (data.getSeason() != null) {
            for (Season_ season : data.getSeason()) {
                number += season.getEpisodes().size();
            }
            if (number > 0) {
                stringBuilder.append(number).append(" ");
                if (number == 1) {
                    stringBuilder.append(mAppCMSPresenter.isMovieSpreeApp() ? programText : episodeText);
                } else {
                    stringBuilder.append(mAppCMSPresenter.isMovieSpreeApp() ? programsText : episodesText);
                }
            }
        }
        //SVFA-3323
        if (data.getGist() != null && data.getGist().getPrimaryCategory() != null && data.getGist().getPrimaryCategory().getTitle() != null) {
            if (stringBuilder.length() > 0) {
                stringBuilder.append(" | ");
            }
            stringBuilder.append(data.getGist().getPrimaryCategory().getTitle() != null ? data.getGist().getPrimaryCategory().getTitle().toUpperCase() : "");
        }
        ((TextView) view).setText(stringBuilder);
        ((TextView) view).setLetterSpacing(LETTER_SPACING);
    }

    /**
     * Fix for JM-26
     */
    public static void setVideoViewWithSubtitle(Context context, ContentDatum data, View view , AppCMSPresenter appCMSPresenter) {

        long durationInSeconds = data.getGist().getRuntime();

        long minutes = durationInSeconds / 60;
        long seconds = durationInSeconds % 60;

        String year = data.getGist().getYear();
        String primaryCategory =
                data.getGist().getPrimaryCategory() != null ?
                        data.getGist().getPrimaryCategory().getTitle() :
                        null;
//        boolean appendFirstSep = minutes > 0
//                && (!TextUtils.isEmpty(year) || !TextUtils.isEmpty(primaryCategory));
//        boolean appendSecondSep = (minutes > 0 || !TextUtils.isEmpty(year))
//                && !TextUtils.isEmpty(primaryCategory);

        StringBuilder infoText = new StringBuilder();

        if (minutes == 1) {
            infoText/*.append("0")*/.append(minutes).append(" ").append(appCMSPresenter.getLocalisedStrings().getMinText());
        } else {
            String minsText = appCMSPresenter.getLocalisedStrings().getMinsText();
            if (minutes > 1 && minutes < 10) {
                infoText/*.append("0")*/.append(minutes).append(" ").append(minsText);
            } else if (minutes >= 10) {
                infoText.append(minutes).append(" ").append(minsText);
            }
        }

        if (minutes < 1) {
            if (seconds == 1) {
                infoText.append(" ")/*.append("0")*/.append(seconds).append(" ").append(appCMSPresenter.getLocalisedStrings().getSecText());
            } else {
                String secsText = appCMSPresenter.getLocalisedStrings().getSecsText();
                if (seconds > 1 && seconds < 10) {
                    infoText.append(" ")/*.append("0")*/.append(seconds).append(" ").append(secsText);
                } else if (seconds >= 10) {
                    infoText.append(" ").append(seconds).append(" ").append(secsText);
                }
            }
        }

        if (!TextUtils.isEmpty(year)) {
            infoText.append(context.getString(R.string.text_separator));
            infoText.append(year);
        }

        if (!TextUtils.isEmpty(primaryCategory)) {
            if (!TextUtils.isEmpty(infoText))
                infoText.append(context.getString(R.string.text_separator));
            infoText.append(primaryCategory.toUpperCase());
        }

        ((TextView) view).setText(infoText.toString());
        ((TextView) view).setLetterSpacing(LETTER_SPACING);

    }

    public ViewGroup getChildrenContainer() {
        if (childrenContainer == null) {
            return createChildrenContainer();
        }
        return childrenContainer;
    }

    public void setComponentHasView(int index, boolean hasView) {
        if (componentHasViewList != null) {
            componentHasViewList[index] = hasView;
        }
    }

    protected ViewGroup createChildrenContainer() {
        childrenContainer = new FrameLayout(getContext());
        int viewWidth = (int) getViewWidth(getContext(), getLayout(), (float) LayoutParams.MATCH_PARENT);
        int viewHeight = (int) getViewHeight(getContext(), getLayout(), (float) LayoutParams.MATCH_PARENT);
        FrameLayout.LayoutParams childContainerLayoutParams =
                new FrameLayout.LayoutParams(viewWidth, viewHeight);
        childrenContainer.setLayoutParams(childContainerLayoutParams);
        addView(childrenContainer);
        return childrenContainer;
    }

    public void setViewMarginsFromComponent(Component childComponent,
                                            View view,
                                            Layout parentLayout,
                                            View parentView,
                                            Map<String, AppCMSUIKeyType> jsonValueKeyMap,
                                            boolean useMarginsAsPercentages,
                                            boolean useWidthOfScreen,
                                            String viewType) {
        Layout layout = childComponent.getLayout();

        view.setPadding(0, 0, 0, 0);

        int lm = 0, tm = 0, rm = 0, bm = 0;
        int deviceHeight = getContext().getResources().getDisplayMetrics().heightPixels;
//        int viewWidth = (int) getViewWidth(getContext(), layout, FrameLayout.LayoutParams.WRAP_CONTENT);
        int viewWidth = (int) getViewWidth(getContext(), layout, FrameLayout.LayoutParams.MATCH_PARENT);
        int viewHeight = (int) getViewHeight(getContext(), layout, FrameLayout.LayoutParams.WRAP_CONTENT);

        int parentViewWidth = (int) getViewWidth(getContext(),
                parentLayout,
                parentView.getMeasuredWidth());
        int parentViewHeight = (int) getViewHeight(getContext(),
                parentLayout,
                parentView.getMeasuredHeight());
      //  int maxViewWidth = (int) getViewMaximumWidth(getContext(), layout, -1);
        int measuredHeight = parentViewHeight != 0 ? parentViewHeight : deviceHeight;
        int gravity = Gravity.NO_GRAVITY;

            FireTV mobile = layout.getTv();
            if (mobile != null) {
                if (getViewWidth(mobile) != -1) {
                    if (mobile.getXAxis() != null) {
                        float scaledX = DEVICE_WIDTH * (Float.valueOf(mobile.getXAxis()) / STANDARD_MOBILE_WIDTH_PX);
                        lm = Math.round(scaledX);
                    }
                }

                if (getViewHeight(mobile) != -1) {
                    if (mobile.getYAxis() != null) {
                        float scaledY = DEVICE_HEIGHT * ((Float.valueOf(mobile.getYAxis()) / STANDARD_MOBILE_HEIGHT_PX));
                        tm = Math.round(scaledY);
                    }
                }

                if (mobile.getLeftMargin() != null && (Float.valueOf(mobile.getLeftMargin()) != 0)) {
                    float scaledLm = DEVICE_WIDTH * ((Float.valueOf(mobile.getLeftMargin()) / STANDARD_MOBILE_WIDTH_PX));
                    lm = Math.round(scaledLm);
                }

                if (mobile.getTopMargin() != null && (Float.valueOf(mobile.getTopMargin())) != 0) {
                    float scaledLm = DEVICE_HEIGHT * ((Float.valueOf(mobile.getTopMargin()) / STANDARD_MOBILE_HEIGHT_PX));
                    tm = Math.round(scaledLm);
                }

                if(mobile.getRightMargin() != null && (Float.valueOf(mobile.getRightMargin())) != 0){
                    float scaledLm = DEVICE_WIDTH * ((Float.valueOf(mobile.getRightMargin()) / STANDARD_MOBILE_WIDTH_PX));
                    rm = Math.round(scaledLm);
                }

            }


        AppCMSUIKeyType componentType = jsonValueKeyMap.get(childComponent.getType());
        AppCMSUIKeyType componentKey = jsonValueKeyMap.get(childComponent.getKey());
        AppCMSUIKeyType viewGravity = jsonValueKeyMap.get(childComponent.getViewGravity());
        if (viewGravity == null) {
            viewGravity = AppCMSUIKeyType.PAGE_EMPTY_KEY;
        }
        if (componentType == AppCMSUIKeyType.PAGE_LABEL_KEY ||
                componentType == AppCMSUIKeyType.PAGE_BUTTON_KEY ||
                componentType == AppCMSUIKeyType.PAGE_CHECKBOX_KEY ||
                componentType == AppCMSUIKeyType.PAGE_IMAGE_KEY  ) {
            if (viewWidth < 0) {
                viewWidth = FrameLayout.LayoutParams.MATCH_PARENT;
            }else if(viewWidth == 0){
                viewWidth = LayoutParams.WRAP_CONTENT;
            }

            if(childComponent.getTextAlignment() != null){
                AppCMSUIKeyType textAlignment = jsonValueKeyMap.get(childComponent.getTextAlignment());
                switch(textAlignment){
                    case PAGE_TEXTALIGNMENT_LEFT_KEY:
                    case VIEW_GRAVITY_START:
                        gravity = Gravity.LEFT ;
                        if(componentKey == AppCMSUIKeyType.PAGE_VIDEO_TITLE_KEY){
                            gravity = Gravity.LEFT | Gravity.CENTER_VERTICAL;
                        }
                        break;
                    case PAGE_TEXTALIGNMENT_RIGHT_KEY:
                    case VIEW_GRAVITY_END:
                        gravity = Gravity.RIGHT ;
                        if(componentKey == AppCMSUIKeyType.PAGE_VIDEO_SUBTITLE_KEY ||
                                componentKey == AppCMSUIKeyType.PAGE_SHOW_SUBTITLE_KEY ||
                                        componentKey == AppCMSUIKeyType.PAGE_EDIT_TEXT_KEY ||
                                componentKey == AppCMSUIKeyType.PAGE_SETTING_ACCOUNT_EDIT_TEXT_FROM_WEBSITE_KEY||
                                componentKey == AppCMSUIKeyType.PAGE_SETTING_ACCOUNT_EDIT_PHONE_FROM_WEBSITE_KEY){
                            gravity = Gravity.RIGHT | Gravity.CENTER_VERTICAL;
                        }
                        break;
                    case PAGE_TEXTALIGNMENT_CENTER_KEY:
                        gravity = Gravity.CENTER;
                        if(componentKey == AppCMSUIKeyType.PAGE_SETTINGS_USER_EMAIL_LABEL_KEY){
                            gravity = Gravity.CENTER_HORIZONTAL;
                        }
                        break;
                    case PAGE_TEXTALIGNMENT_CENTER_HORIZONTAL_KEY:
                    case VIEW_GRAVITY_CENTER_HORIZONTAL:
                        gravity = Gravity.CENTER_HORIZONTAL;
                        break;
                    case PAGE_TEXTALIGNMENT_CENTER_VERTICAL_KEY:
                    case VIEW_GRAVITY_CENTER_VERTICAL:
                        gravity = Gravity.CENTER_VERTICAL;
                        break;
                }
                if(view instanceof TextView) {
                    ((TextView) view).setGravity(gravity);
                }
            }


            if (componentKey == null) {
                componentKey = AppCMSUIKeyType.PAGE_EMPTY_KEY;
            }
            switch (componentKey) {
                case PAGE_PLAY_EPISODE_BUTTON_KEY:
                case PAGE_CAROUSEL_ADD_TO_WATCHLIST_KEY:
                case PAGE_SHOW_DETAIL_BUTTON_KEY:
                    view.setPadding(childComponent.getLeftPadding(),0,childComponent.getRightPadding(),0);
                    break;

                case PAGE_LINK_YOUR_ACCOUNT_TEXT_KEY:
                case PAGE_DONT_HAVE_AN_ACCOUNT_TEXT_KEY:
                case PAGE_SELECT_YOUR_PLAN_TEXT_KEY:
                    ((TextView) view).setGravity(Gravity.CENTER_HORIZONTAL);
                    gravity = Gravity.NO_GRAVITY;
                    break;
                case PAGE_TRAY_TITLE_KEY:
                    break;
                case PAGE_PLAY_IMAGE_KEY:
                    if (AppCMSUIKeyType.PAGE_HISTORY_01_MODULE_KEY != jsonValueKeyMap.get(viewType)
                            && AppCMSUIKeyType.PAGE_HISTORY_02_MODULE_KEY != jsonValueKeyMap.get(viewType)
                            && AppCMSUIKeyType.PAGE_WATCHLIST_01_MODULE_KEY != jsonValueKeyMap.get(viewType)
                            && AppCMSUIKeyType.PAGE_LIBRARY_01_MODULE_KEY != jsonValueKeyMap.get(viewType)
                            && AppCMSUIKeyType.PAGE_WATCHLIST_02_MODULE_KEY != jsonValueKeyMap.get(viewType)) {
                        gravity = Gravity.CENTER;
                        tm = 0;
                        lm = 0;
                    }
                    break;
                case PAGE_THUMBNAIL_TITLE_KEY:
                 {
                        tm -= viewHeight / 2;
                        viewHeight *= 1.5;
                    }
                    break;
                case PAGE_WATCH_VIDEO_KEY:
                    gravity = Gravity.CENTER_HORIZONTAL;
                    break;
                case PAGE_VIDEO_SHARE_KEY:
                    break;
                case PAGE_API_TITLE:
                    viewHeight *= 1.5;
                    break;
                case PAGE_ADD_TO_WATCHLIST_KEY:
                case PAGE_VIDEO_WATCH_TRAILER_KEY:
                case PAGE_ICON_LABEL_KEY:
                    //viewWidth = FrameLayout.LayoutParams.WRAP_CONTENT;
                    int padding = Utils.getViewXAxisAsPerScreen(getContext(),childComponent.getPadding());
                    view.setPadding(padding,padding,padding,padding);
                    break;
                case PAGE_VIDEO_TITLE_KEY:
                   // if (appCMSPresenter.getTemplateType().equals(AppCMSPresenter.TemplateType.ENTERTAINMENT)) {
//                        viewWidth = DEVICE_WIDTH/2 - Utils.getViewXAxisAsPerScreen(getContext() , 150);
                   //   }
                    break;
                case PAGE_VIDEO_SUBTITLE_KEY:
                    viewWidth = DEVICE_WIDTH/2;
                    break;
                case PAGE_AUTOPLAY_FINISHED_UP_TITLE_KEY:
                case PAGE_AUTOPLAY_FINISHED_MOVIE_TITLE_KEY:
                    gravity = Gravity.NO_GRAVITY;
                    break;
                case PAGE_LEFT_ARROW_KEY:
                    if (AppCMSUIKeyType.PAGE_STAND_ALONE_VIDEO_PLAYER02 != jsonValueKeyMap.get(viewType))
                    gravity = Gravity.CENTER_VERTICAL;
                    break;
                case PAGE_RIGHT_ARROW_KEY:
                    if (AppCMSUIKeyType.PAGE_STAND_ALONE_VIDEO_PLAYER02 != jsonValueKeyMap.get(viewType))
                    gravity = Gravity.END | Gravity.CENTER_VERTICAL;

                break;
            }

            int fontsize = getFontsize(getContext(), childComponent);
            if (fontsize > 0) {
                if(view instanceof TextView) {
                    ((TextView) view).setTextSize((float) fontsize);
                }
            }
        } else if (componentType == AppCMSUIKeyType.PAGE_TEXTFIELD_KEY) {
            viewHeight *= 1.2;
        } else if (componentType == AppCMSUIKeyType.PAGE_IMAGE_KEY
            && componentKey == AppCMSUIKeyType.PAGE_AUTOPLAY_MOVIE_IMAGE_KEY) {
                int imagePadding = Integer.valueOf(
                        childComponent.getLayout().getTv().getPadding() != null
                                ? childComponent.getLayout().getTv().getPadding()
                                : "0");
                view.setPadding(imagePadding, imagePadding, imagePadding, imagePadding);
        }/*else if(componentType == AppCMSUIKeyType.PAGE_VIDEO_PLAYER_VIEW_KEY){
            viewHeight = DEVICE_HEIGHT;
            viewWidth = FrameLayout.LayoutParams.MATCH_PARENT;
        }*/ else if(componentKey == AppCMSUIKeyType.PAGE_ICON_IMAGE_KEY) {
            int padding = Utils.getViewXAxisAsPerScreen(getContext(), childComponent.getPadding());
            view.setPadding(padding, padding, padding, padding);
        } else if (componentType.equals(AppCMSUIKeyType.PAGE_TABLE_VIEW_KEY)) {
            Integer padding = Integer.valueOf(
                    childComponent.getLayout().getTv().getPadding() != null
                            ? childComponent.getLayout().getTv().getPadding()
                            : "0");
            int bottomPadding = 0;
            if(componentKey == AppCMSUIKeyType.PAGE_SEE_ALL_COLLECTIONGRID_KEY){
                 bottomPadding = 130;
            }
            view.setPadding(padding, padding, padding, bottomPadding);
            if (childComponent.getTextAlignment() != null) {
                AppCMSUIKeyType textAlignment = jsonValueKeyMap.get(childComponent.getTextAlignment());
                switch (textAlignment) {
                    case PAGE_TEXTALIGNMENT_CENTER_KEY:
                        gravity = Gravity.CENTER;
                        break;
                    default:
                        gravity = Gravity.NO_GRAVITY;
                }
            }
        } else if (componentType.equals(AppCMSUIKeyType.PAGE_IMAGE_KEY)) {
            if (AppCMSUIKeyType.PAGE_VIDEO_DETAIL_APP_LOGO_KEY.equals(componentKey)) {
                viewWidth = LayoutParams.WRAP_CONTENT;
                ((ImageView) view).setScaleType(ImageView.ScaleType.FIT_START);
            }
        }

        if (useWidthOfScreen) {
            viewWidth = DEVICE_WIDTH;
        }
        if (viewGravity == VIEW_GRAVITY_CENTER_HORIZONTAL) {
            gravity = Gravity.CENTER_HORIZONTAL;
        }
        else if(viewGravity == VIEW_GRAVITY_END){
            gravity = Gravity.END;
        }
        ViewGroup.MarginLayoutParams marginLayoutParams = new ViewGroup.MarginLayoutParams(viewWidth, viewHeight);
        marginLayoutParams.setMargins(lm, tm, rm, bm);
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(marginLayoutParams);
        layoutParams.width = viewWidth;
        layoutParams.height = viewHeight;
        if (componentType == AppCMSUIKeyType.PAGE_LABEL_KEY ||
                componentType == AppCMSUIKeyType.PAGE_BUTTON_KEY ||
                componentType == AppCMSUIKeyType.PAGE_IMAGE_KEY ||
                componentType == AppCMSUIKeyType.PAGE_TABLE_VIEW_KEY ||
                componentType == AppCMSUIKeyType.PAGE_VERTICAL_GRID_VIEW_KEY ||
                componentType == AppCMSUIKeyType.PAGE_CHECKBOX_KEY ||
                componentType == AppCMSUIKeyType.PAGE_SETTING_TOGGLE_SWITCH_TYPE ||
                componentType == AppCMSUIKeyType.PAGE_VIDEO_PLAYER_VIEW_KEY ||
                componentType == AppCMSUIKeyType.VIEW_TABLE_LAYOUT ||
                componentType == AppCMSUIKeyType.PAGE_PARENT_LINEAR_VIEW ||
                componentType == AppCMSUIKeyType.PAGE_SETTING_MODULE_COMPONENT_VIEW) {
            layoutParams.gravity = gravity;
        }
        view.setLayoutParams(layoutParams);
    }


    protected int getFontsize(Context context, Component component) {
        if (component.getFontSize() > 0) {
            return component.getFontSize();
        }
        if (component.getLayout().getTv().getFontSize() > 0) {
                return component.getLayout().getTv().getFontSize();
        }

        return 0;
    }

    protected void initializeComponentHasViewList(int size) {
        componentHasViewList = new boolean[size];
    }
}
