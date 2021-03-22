package com.viewlift.views.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.style.ClickableSpan;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.viewlift.AppCMSApplication;
import com.viewlift.R;
import com.viewlift.models.data.appcms.api.ContentDatum;
import com.viewlift.models.data.appcms.api.Module;
import com.viewlift.models.data.appcms.ui.AppCMSUIKeyType;
import com.viewlift.models.data.appcms.ui.android.AppCMSAndroidModules;
import com.viewlift.models.data.appcms.ui.main.LocalisedStrings;
import com.viewlift.models.data.appcms.ui.page.Component;
import com.viewlift.models.data.appcms.ui.page.Layout;
import com.viewlift.models.data.appcms.ui.page.Settings;
import com.viewlift.presenters.AppCMSPresenter;
import com.viewlift.views.customviews.BaseView;
import com.viewlift.views.customviews.CollectionGridItemView;
import com.viewlift.views.customviews.ViewCreator;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

/*
 * Created by viewlift on 5/5/17.
 */

public class AppCMSBenefitPlanPageAdapter extends RecyclerView.Adapter<AppCMSBenefitPlanPageAdapter.ViewHolder>
        implements AppCMSBaseAdapter {
    private static final String TAG = "AppCMSViewAdapter";
    protected Context mContext;
    protected Layout parentLayout;
    protected Component component;
    protected Settings settings;
    protected ViewCreator viewCreator;
    protected Map<String, AppCMSUIKeyType> jsonValueKeyMap;
    Module moduleAPI;
    List<ContentDatum> adapterData;
    CollectionGridItemView.OnClickHandler onClickHandler;
    int defaultWidth;
    int defaultHeight;
    boolean useMarginsAsPercentages;
    String componentViewType;
    AppCMSAndroidModules appCMSAndroidModules;
    private boolean useParentSize;
    private AppCMSUIKeyType viewTypeKey;
    private boolean isClickable;
    public static final int TYPE_FOOTER = 2;
    public static final int TYPE_HEADER = 3;
    public static final int TYPE_ITEM = 1;
    int adapterSize = 0;
    String blockName;
    @Inject
    AppCMSPresenter appCMSPresenter;
    @Inject
    LocalisedStrings localisedStrings;
    public AppCMSBenefitPlanPageAdapter(Context context,
                                        ViewCreator viewCreator,
                                        Settings settings,
                                        Layout parentLayout,
                                        boolean useParentSize,
                                        Component component,
                                        Map<String, AppCMSUIKeyType> jsonValueKeyMap,
                                        Module moduleAPI,
                                        int defaultWidth,
                                        int defaultHeight,
                                        String viewType,
                                        AppCMSAndroidModules appCMSAndroidModules, String blockName) {
        this.mContext = context;
        this.viewCreator = viewCreator;
        this.settings = settings;
        this.parentLayout = parentLayout;
        this.useParentSize = useParentSize;
        this.component = component;
        this.jsonValueKeyMap = jsonValueKeyMap;
        this.moduleAPI = moduleAPI;
        this.blockName = blockName;
        ((AppCMSApplication) this.mContext.getApplicationContext()).getAppCMSPresenterComponent().inject(this);
        this.componentViewType = viewType;
        this.viewTypeKey = jsonValueKeyMap.get(componentViewType);
        if (this.viewTypeKey == null) {
            this.viewTypeKey = AppCMSUIKeyType.PAGE_EMPTY_KEY;
        }
        this.defaultWidth = defaultWidth;
        this.defaultHeight = defaultHeight;
        this.useMarginsAsPercentages = true;
        this.isClickable = true;
        this.setHasStableIds(false);
        this.appCMSAndroidModules = appCMSAndroidModules;
        appCMSPresenter.setSinglePlanFeatureAvailable(true);
        /*if (settings.getItems() == null)
            adapterSize = 0;
        else
            adapterSize = settings.getItems().size() + 1;*/
        if (moduleAPI == null || moduleAPI.getExtendedMap() == null)
            adapterSize = 0;
        else {
            adapterSize = moduleAPI.getExtendedMap().getItems().size() + 1;
            createAdapterData();
//            notifyDataSetChanged();
        }
    }

    private void createAdapterData() {
        adapterData = new ArrayList<>();
        for (int i = 0; i < adapterSize - 1; i++) {
            ContentDatum contentDatum = new ContentDatum();
            contentDatum.setPlanImgUrl(moduleAPI.getExtendedMap().getItems().get(i).getImageUrl());
            contentDatum.setTitle(moduleAPI.getExtendedMap().getItems().get(i).getTitle());
            adapterData.add(contentDatum);
        }

    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_ITEM) {
            CollectionGridItemView view = viewCreator.createCollectionGridItemView(parent.getContext(),
                    parentLayout,
                    useParentSize,
                    component,
                    appCMSPresenter,
                    moduleAPI,
                    appCMSAndroidModules,
                    settings,
                    jsonValueKeyMap,
                    defaultWidth,
                    defaultHeight,
                    useMarginsAsPercentages,
                    true,
                    this.componentViewType,
                    false,
                    false, this.viewTypeKey, blockName);
            /*tablet landscape*/
            if (BaseView.isLandscape(mContext)) {
                FrameLayout.LayoutParams layPar = (FrameLayout.LayoutParams) view.getLayoutParams();
                layPar.setMargins(0, 25, 0, 0);
                view.setLayoutParams(layPar);
            } else
                /*tablet portrait*/
                if (BaseView.isTablet(mContext) && !BaseView.isLandscape(mContext)) {
                    FrameLayout.LayoutParams layPar = (FrameLayout.LayoutParams) view.getLayoutParams();
                    layPar.setMargins(0, 20, 0, 0);
                    view.setLayoutParams(layPar);
                } else
                    /*mobile*/
                    if (!BaseView.isTablet(mContext) && !appCMSPresenter.isHoichoiApp()) {
                        FrameLayout.LayoutParams layPar = (FrameLayout.LayoutParams) view.getLayoutParams();
                        layPar.setMargins(0, 45, 0, 0);
                        view.setLayoutParams(layPar);
                    }
            return new ViewHolder(view);
        } else if (viewType == TYPE_HEADER) {
            TextView header = new TextView(mContext);
            header.setLayoutParams(new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.WRAP_CONTENT));
            header.setTextColor(appCMSPresenter.getGeneralTextColor());
            header.setTextSize(16f);
            header.setGravity(Gravity.CENTER);
            if (moduleAPI != null && moduleAPI.getExtendedMap() != null && moduleAPI.getExtendedMap().getFormData() != null && moduleAPI.getExtendedMap().getFormData().getTitleInput() != null)
                header.setText(moduleAPI.getExtendedMap().getFormData().getTitleInput());
            else
                header.setText(moduleAPI.getTitle());
            header.setTypeface(ResourcesCompat.getFont(mContext,R.font.font_bold));
            FrameLayout.LayoutParams layPar = (FrameLayout.LayoutParams) header.getLayoutParams();
            layPar.setMargins(0, 55, 0, 0);
            header.setLayoutParams(layPar);
            return new ViewHolder(header);
        } else {
            TextView termsView = new TextView(mContext);
            termsView.setLayoutParams(new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.WRAP_CONTENT));
            termsView.setTextColor(appCMSPresenter.getGeneralTextColor());
            termsView.setTextSize(14f);
            if (appCMSPresenter.isHoichoiApp()) {
                termsView.setPadding(5, 10, 5, 0);
            } else {
                termsView.setPadding(5, 35, 5, 15);
            }
            termsView.setGravity(Gravity.CENTER);
            termsView.setText(localisedStrings.getTnCext());
            termsView.setLinkTextColor(ContextCompat.getColor(mContext, android.R.color.white));
            ClickableSpan tosClick = new ClickableSpan() {
                @Override
                public void onClick(View view) {
                    appCMSPresenter.navigatToTOSPage(null, null);
                }
            };
            ClickableSpan privacyClick = new ClickableSpan() {
                @Override
                public void onClick(View view) {
                    appCMSPresenter.navigateToPrivacyPolicy(null, null);
                }
            };
            appCMSPresenter.makeTextViewLinks(termsView, new String[]{
                    localisedStrings.getTermsOfUsesText(), localisedStrings.getPrivacyPolicyText()}, new ClickableSpan[]{tosClick, privacyClick}, true);
            FrameLayout.LayoutParams layPar = (FrameLayout.LayoutParams) termsView.getLayoutParams();
            layPar.setMargins(0, 0, 0, 8);
            termsView.setLayoutParams(layPar);
            return new ViewHolder(termsView);
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (appCMSPresenter.isMOTVApp()) {
            if (position == 0 /*&& appCMSPresenter.isMOTVApp()*/)
                return TYPE_HEADER;
            else if (position > 0)
                return TYPE_ITEM;

        } else {
            if (position < settings.getItems().size())
                return TYPE_ITEM;
            else
                return TYPE_FOOTER;
        }
        return 0;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        if (holder.componentView != null) {
            int pos = position;
            if (appCMSPresenter.isMOTVApp()) {
                pos = position - 1;
            }
            for (int i = 0; i < holder.componentView.getNumberOfChildren(); i++) {
                if (holder.componentView.getChild(i) instanceof TextView) {
                    ((TextView) holder.componentView.getChild(i)).setText("");
                }
//                bindView(holder.componentView, null, pos);
                bindView(holder.componentView, adapterData.get(pos), pos);
            }
        }
    }


    @Override
    public int getItemCount() {
        /*if (settings.getItems() == null)
            return 0;
        else*/
        return adapterSize;
    }

    @Override
    public void resetData(RecyclerView listView) {
        notifyDataSetChanged();
    }

    @Override
    public void updateData(RecyclerView listView, List<ContentDatum> contentData) {
        listView.setAdapter(null);
        adapterData = null;
        notifyDataSetChanged();
        adapterData = contentData;
        notifyDataSetChanged();
        listView.setAdapter(this);
        listView.invalidate();
        notifyDataSetChanged();
    }

    @SuppressLint("ClickableViewAccessibility")
    void bindView(CollectionGridItemView itemView,
                  final ContentDatum data, int position) throws IllegalArgumentException {


        for (int i = 0; i < itemView.getNumberOfChildren(); i++) {
            itemView.bindChild(itemView.getContext(),
                    itemView.getChild(i),
                    data,
                    jsonValueKeyMap,
                    onClickHandler,
                    componentViewType,
                    appCMSPresenter.getBrandPrimaryCtaColor(),
                    appCMSPresenter,
                    position, settings, blockName,moduleAPI.getMetadataMap());
        }
    }


    public boolean isClickable() {
        return isClickable;
    }

    @Override
    public void setClickable(boolean clickable) {
        isClickable = clickable;
    }


    @Override
    public void onViewRecycled(ViewHolder holder) {
        super.onViewRecycled(holder);
        if (holder.componentView != null) {
            int childCount = holder.componentView.getChildCount();
            for (int i = 0; i < childCount; i++) {
                View child = holder.componentView.getChild(i);
                if (child instanceof ImageView) {
                    Glide.with(child.getContext()).clear(child);
                }
            }
        }
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        CollectionGridItemView componentView;
        TextView termsView;

        public ViewHolder(TextView itemView) {
            super(itemView);
            this.termsView = itemView;
        }

        public ViewHolder(View itemView) {
            super(itemView);
            this.componentView = (CollectionGridItemView) itemView;
        }
    }


}
