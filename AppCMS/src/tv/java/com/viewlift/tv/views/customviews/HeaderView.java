package com.viewlift.tv.views.customviews;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.viewlift.R;
import com.viewlift.models.data.appcms.api.Module;
import com.viewlift.models.data.appcms.ui.AppCMSUIKeyType;
import com.viewlift.models.data.appcms.ui.page.Component;
import com.viewlift.models.data.appcms.ui.page.Layout;
import com.viewlift.presenters.AppCMSPresenter;
import com.viewlift.tv.utility.Utils;
import com.viewlift.utils.CommonUtils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Map;


/**
 * Created by nitin.tyagi on 7/12/2017.
 */

public class HeaderView extends TVBaseView {

    private final boolean publishLabel;
    private Context mContext;
    private Component mComponent;
    private int DEFAULT_HEIGHT = LayoutParams.WRAP_CONTENT;
    private int DEFAULT_WIDTH =  LayoutParams.MATCH_PARENT;
    private  Map<String, AppCMSUIKeyType> mJsonValueKeyMap;
    private Module mModuleData;
    private AppCMSPresenter mAppCMSPresenter;

    public HeaderView(Context context, Component component,
                      Map<String, AppCMSUIKeyType> jsonValueKeyMap,
                      Module moduleAPI,
                      AppCMSPresenter appCMSPresenter,
                      boolean publishLabel) {
        super(context);
        mContext = context;
        mComponent = component;
        mJsonValueKeyMap = jsonValueKeyMap;
        mModuleData = moduleAPI;
        mAppCMSPresenter = appCMSPresenter;
        this.publishLabel = publishLabel;
        init();
        initComponent();
    }


    @Override
    public void init() {
        float viewHeight = Utils.getViewHeight(mContext , mComponent.getLayout() ,DEFAULT_HEIGHT );
        float viewWidth = Utils.getViewHeight(mContext ,mComponent.getLayout() ,DEFAULT_WIDTH );
        LayoutParams layoutParams= new LayoutParams((int)viewWidth , (int)viewHeight);
        setLayoutParams(layoutParams);
        int color = Color.TRANSPARENT;
        if (mComponent.getBackgroundColor() != null) {
            color = Color.parseColor(mComponent.getBackgroundColor());
        }
        setBackgroundColor(color);
    }

    @Override
    protected Component getChildComponent(int index) {
        return null;
    }

    @Override
    protected Layout getLayout() {
        return null;
    }

    View componentView;
    private void initComponent(){
        if(null != mComponent && mComponent.getComponents() != null && mComponent.getComponents().size() > 0){
            for(Component component : mComponent.getComponents()){
                componentView = null;
                createComponentHeaderView(component);
                if(null != componentView){
                    setViewMarginsFromComponent(component,
                            componentView,
                            mComponent.getLayout(),
                            this,
                            mJsonValueKeyMap,
                            false,
                    false,
                            component.getView());
                    addView(componentView);
                }

            }

        }
    }


    private View createComponentHeaderView(Component component){

        AppCMSUIKeyType componentType = mJsonValueKeyMap.get(component.getType());
        if (mModuleData == null) {
            return null;
        }
        if (componentType == null) {
            componentType = AppCMSUIKeyType.PAGE_EMPTY_KEY;
        }
        AppCMSUIKeyType componentKey = mJsonValueKeyMap.get(component.getKey());
        if (componentKey == null) {
            componentKey = AppCMSUIKeyType.PAGE_EMPTY_KEY;
        }

        switch (componentType){
            case PAGE_LABEL_KEY:
                componentView = new TextView(mContext);
                int textColor = ContextCompat.getColor(mContext, R.color.colorAccent);
                if (!TextUtils.isEmpty(component.getTextColor())) {
                    textColor = Color.parseColor(CommonUtils.getColor(mContext, component.getTextColor()));
                } else if (component.getStyles() != null) {
                    if (!TextUtils.isEmpty(component.getStyles().getColor())) {
                        textColor = Color.parseColor(CommonUtils.getColor(mContext, component.getStyles().getColor()));
                    } else if (!TextUtils.isEmpty(component.getStyles().getTextColor())) {
                        textColor = Color.parseColor(CommonUtils.getColor(mContext, component.getStyles().getTextColor()));
                    }
                }
                if (null != mAppCMSPresenter.getAppTextColor()) {
                    textColor = Color.parseColor(mAppCMSPresenter.getAppTextColor());
                }
                ((TextView)componentView).setTextColor(textColor);
                Typeface typeface = Utils.getTypeFace(mContext , mAppCMSPresenter, component );
                if(null != typeface){
                    ((TextView) componentView).setTypeface(typeface);
                }
                switch (componentKey) {
                    case PAGE_VIDEO_TITLE_KEY:
                    case PAGE_API_TITLE:
                    case PAGE_SHOW_TITLE_KEY:
                        if(null != mModuleData && null != mModuleData.getContentData() && mModuleData.getContentData().size() > 0) {
                            if (mModuleData.getContentData().get(0).getGist() != null
                                    && !TextUtils.isEmpty(mModuleData.getContentData().get(0).getGist().getTitle())) {
                                ((TextView) componentView).setText(mModuleData.getContentData().get(0).getGist().getTitle());
                                if (component.getNumberOfLines() != 0) {
                                    ((TextView) componentView).setMaxLines(component.getNumberOfLines());
                                }
                                ((TextView) componentView).setEllipsize(TextUtils.TruncateAt.END);
                            }
                        }
                        break;

                    case PAGE_VIDEO_SUBTITLE_KEY:
                        if(null != mModuleData && null != mModuleData.getContentData() && mModuleData.getContentData().size() > 0)
                            setVideoViewWithSubtitle(mContext , mModuleData.getContentData().get(0) , componentView,mAppCMSPresenter);
                        break;

                    case PAGE_SHOW_SUBTITLE_KEY:
                        if(null != mModuleData && null != mModuleData.getContentData() && mModuleData.getContentData().size() > 0)
                            setShowViewWithSubtitle(mContext, mModuleData, mModuleData.getContentData().get(0) , componentView, mAppCMSPresenter);
                        break;

                    case PAGE_BUNDLE_SUBTITLE_KEY:
                        if(null != mModuleData && null != mModuleData.getContentData() && mModuleData.getContentData().size() > 0)
                            if(mModuleData.getContentData().get(0).getGist().getBundleList()!=null && mModuleData.getContentData().get(0).getGist().getBundleList().size()>0){
                                ((TextView) componentView).setText(mModuleData.getContentData().get(0).getGist().getBundleList().size() + " FILMS");
                            }
                        break;
                    case PAGE_VIDEO_PUBLISHDATE_KEY:
                        if (publishLabel
                                && !TextUtils.isEmpty(mModuleData.getContentData().get(0).getGist().getPublishDate())
                                && !mModuleData.getContentData().get(0).getGist().isLiveStream()) {
                            try {
                                String format = new SimpleDateFormat("MMMM dd, yyyy", Locale.getDefault()).format(new Date(Long.parseLong(mModuleData.getContentData().get(0).getGist().getPublishDate())));
                                ((TextView) componentView).setText(String.format("%s %s", mAppCMSPresenter.getLocalisedStrings().getPublishedText(), format));
                            } catch (Exception ignored) {
                                ((TextView) componentView).setText("");
                            }
                        } else {
                            ((TextView) componentView).setText("");
                        }
                        break;
                }
        }

        return componentView;
    }
}
