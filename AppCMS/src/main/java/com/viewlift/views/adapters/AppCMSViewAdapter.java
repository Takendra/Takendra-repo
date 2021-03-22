package com.viewlift.views.adapters;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.viewlift.AppCMSApplication;
import com.viewlift.audio.playback.AudioPlaylistHelper;
import com.viewlift.R;
import com.viewlift.models.data.appcms.api.AppCMSTransactionDataValue;
import com.viewlift.models.data.appcms.api.ContentDatum;
import com.viewlift.models.data.appcms.api.Gist;
import com.viewlift.models.data.appcms.api.MetadataMap;
import com.viewlift.models.data.appcms.api.Module;
import com.viewlift.models.data.appcms.api.RelatedVideo;
import com.viewlift.models.data.appcms.photogallery.IPhotoGallerySelectListener;
import com.viewlift.models.data.appcms.ui.AppCMSUIKeyType;
import com.viewlift.models.data.appcms.ui.android.AppCMSAndroidModules;
import com.viewlift.models.data.appcms.ui.android.LocalizationResult;
import com.viewlift.models.data.appcms.ui.main.GenericMessages;
import com.viewlift.models.data.appcms.ui.main.LocalisedStrings;
import com.viewlift.models.data.appcms.ui.page.Component;
import com.viewlift.models.data.appcms.ui.page.Layout;
import com.viewlift.models.data.appcms.ui.page.Settings;
import com.viewlift.presenters.AppCMSPresenter;
import com.viewlift.utils.CommonUtils;
import com.viewlift.utils.ContentTypeChecker;
import com.viewlift.views.customviews.BaseView;
import com.viewlift.views.customviews.CollectionGridItemView;
import com.viewlift.views.customviews.PhotoGalleryNextPreviousListener;
import com.viewlift.views.customviews.ViewCreator;
import com.viewlift.views.customviews.constraintviews.ConstraintCollectionGridItemView;
import com.viewlift.views.customviews.constraintviews.ConstraintViewCreator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

/*
 * Created by viewlift on 5/5/17.
 */

public class AppCMSViewAdapter extends RecyclerView.Adapter<AppCMSViewAdapter.ViewHolder>
        implements AppCMSBaseAdapter {
    private static final String TAG = "AppCMSViewAdapter";
    static int selectedPosition = -1;
    private final String person;
    private final String episodicContentType;
    private final String seasonContentType;

    private final String fullLengthFeatureType;
    protected Context mContext;
    protected Layout parentLayout;
    protected Component component;
    @Inject
    AppCMSPresenter appCMSPresenter;
    @Inject
    LocalisedStrings localisedStrings;
    protected Settings settings;
    protected ViewCreator viewCreator;
    protected Map<String, AppCMSUIKeyType> jsonValueKeyMap;
    Module moduleAPI;
    List<ContentDatum> adapterData;
    CollectionGridItemView.OnClickHandler onClickHandler;
    ConstraintCollectionGridItemView.OnClickHandler onConstraintClickHandler;
    int defaultWidth;
    int defaultHeight;
    boolean useMarginsAsPercentages;
    String componentViewType;
    AppCMSAndroidModules appCMSAndroidModules;
    CollectionGridItemView[] planItemView;
    private boolean useParentSize;
    private AppCMSUIKeyType viewTypeKey;
    private AppCMSUIKeyType uiBlockName;
    private int unselectedColor;
    private int selectedColor;
    private boolean isClickable;
    private String videoAction;
    private String openOptionsAction;
    private String purchasePlanAction;
    private String showAction;
    private String personAction;
    private String bundleDetailAction;
    private String watchTrailerAction;
    private MotionEvent lastTouchDownEvent;
    private Gist preGist;
    boolean emptyList = false;
    private final int TYPE_SEE_ALL = 1;
    private final int TYPE_DEFAULT = 2;
    int seeAllWidth = 0;
    int seeAllHeight = 0;
    float seeAllTextSize = 0;
    private int adapterSize = 0;
    String blockName;
    boolean constrainteView;
    LocalizationResult localizationResult = null;
    GenericMessages genericMessages = null;
    private ConstraintViewCreator constraintViewCreator;

    public AppCMSViewAdapter(Context context,
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
                             AppCMSAndroidModules appCMSAndroidModules,
                             String blockName,
                             boolean constrainteView,
                             ConstraintViewCreator constraintViewCreator) {
        this.mContext = context;
        this.viewCreator = viewCreator;
        this.constraintViewCreator = constraintViewCreator;
        ((AppCMSApplication) this.mContext.getApplicationContext()).getAppCMSPresenterComponent().inject(this);
        this.parentLayout = parentLayout;
        this.useParentSize = useParentSize;
        this.component = component;
        this.jsonValueKeyMap = jsonValueKeyMap;
        this.moduleAPI = moduleAPI;
        this.componentViewType = viewType;
        this.constrainteView = constrainteView;
        this.settings = settings;
        this.blockName = blockName;
        this.uiBlockName = jsonValueKeyMap.get(blockName);

        if (blockName == null) {
            this.uiBlockName = AppCMSUIKeyType.PAGE_EMPTY_KEY;
        }
        this.viewTypeKey = jsonValueKeyMap.get(componentViewType);
        if (this.viewTypeKey == null) {
            this.viewTypeKey = AppCMSUIKeyType.PAGE_EMPTY_KEY;
        }


        if (moduleAPI != null && moduleAPI.getContentData() != null && moduleAPI.getModuleType() != null
                && moduleAPI.getModuleType().equalsIgnoreCase("BundleDetailModule")
                && moduleAPI.getContentData().get(0).getGist() != null && moduleAPI.getContentData().get(0).getGist().getBundleList() != null) {
            this.adapterData = moduleAPI.getContentData().get(0).getGist().getBundleList();
        } else if (moduleAPI != null && moduleAPI.getContentData() != null) {
            this.adapterData = moduleAPI.getContentData();
            /*if (this.adapterData.size() >= adapterSize && this.viewTypeKey == AppCMSUIKeyType.PAGE_SEE_ALL_CATEGORY_KEY && appCMSPresenter.getSeeAllPage() == null && isSeeAllTray())
                adapterSize = adapterData.size();*/
        } else {
            this.adapterData = new ArrayList<>();
        }
        if (this.adapterData.size() == 0) {
            emptyList = true;
        }

        /*if (moduleAPI != null && moduleAPI.getFilters() != null &&
                moduleAPI.getFilters().getLimit() >= adapterData.size()) {
            this.adapterSize = moduleAPI.getFilters().getLimit();
        } else {
            adapterSize = adapterData.size();
        }*/

        adapterSize = adapterData.size();

        if (this.viewTypeKey == AppCMSUIKeyType.PAGE_PHOTO_TRAY_MODULE_KEY) {
            /*remove data from 1st position since it contains photogallery details*/
            if (adapterData.get(0).getStreamingInfo() != null) {
                List<ContentDatum> data = new ArrayList<>();
                data.addAll(moduleAPI.getContentData());
                data.remove(0);
                adapterData = data;
            }
            selectedPosition = 0;
            if (adapterData.size() > 0) {
                preGist = adapterData.get(0).getGist();
            }
        }
        this.defaultWidth = defaultWidth;
        this.defaultHeight = defaultHeight;
        this.useMarginsAsPercentages = true;
        this.videoAction = getVideoAction(context);
        this.showAction = getShowAction(context);
        this.personAction = getPersonAction(context);
        this.bundleDetailAction = getBundleDetailAction(context);
        this.watchTrailerAction = getWatchTrailerAction(context);
        this.openOptionsAction = getOpenOptionsAction(context);
        this.purchasePlanAction = getPurchasePlanAction(context);

        this.unselectedColor = ContextCompat.getColor(context, android.R.color.white);
        this.selectedColor = appCMSPresenter.getBrandPrimaryCtaColor();
        this.isClickable = true;

        this.setHasStableIds(false);

        this.appCMSAndroidModules = appCMSAndroidModules;

        this.episodicContentType = context.getString(R.string.app_cms_episodic_key_type);
        this.seasonContentType = context.getString(R.string.app_cms_episodic_season_prefix);
        this.person = context.getString(R.string.app_cms_person_key_type);
        this.fullLengthFeatureType = context.getString(R.string.app_cms_full_length_feature_key_type);
        planItemView = new CollectionGridItemView[adapterSize];

        if (appCMSPresenter.getAppCMSMain() != null
                && appCMSPresenter.getAppCMSMain().getGenericMessages() != null
                && appCMSPresenter.getAppCMSMain().getGenericMessages().getLocalizationMap() != null
                && appCMSPresenter.getAppCMSMain().getGenericMessages().getLocalizationMap().size() > 0
                && appCMSPresenter.getAppCMSMain().getGenericMessages().getLocalizationMap().get(appCMSPresenter.getLanguage().getLanguageCode()) != null) {
            localizationResult = appCMSPresenter.getAppCMSMain().getGenericMessages().getLocalizationMap().get(appCMSPresenter.getLanguage().getLanguageCode());

        }
        if (appCMSPresenter.getAppCMSMain().getGenericMessages() != null) {
            genericMessages = appCMSPresenter.getAppCMSMain().getGenericMessages();
        }
        //sortPlan(); as per MSEAN-1434
    }

    private List<ContentDatum> getRelatedVideos(Module moduleAPI) {
        List<ContentDatum> adapterData = moduleAPI.getContentData();
        List<ContentDatum> tempData = new ArrayList<>();

        if (adapterData.size() > 0 &&
                adapterData.get(0) != null &&
                adapterData.get(0).getGist() != null &&
                adapterData.get(0).getGist().getRelatedVideos() != null &&
                adapterData.get(0).getGist().getRelatedVideos().size() > 0) {
            ArrayList<RelatedVideo> relatedVideos = new ArrayList<>();
            for (int i = 0; i < adapterData.get(0).getGist().getRelatedVideos().size(); i++) {
                if (!adapterData.get(0).getGist().getRelatedVideos().get(i).getGist().isLiveStream()) {
                    relatedVideos.add(adapterData.get(0).getGist().getRelatedVideos().get(i));
                }
            }
            int relatedVideosSize = relatedVideos.size();
            if (relatedVideosSize > 4) {
                relatedVideosSize = 4;
            }

            for (int i = 0; i < relatedVideosSize; i++) {
                ContentDatum newData = new ContentDatum();
                newData = adapterData.get(0);
                newData.getGist().getRelatedVideos().set(i, relatedVideos.get(i));
                tempData.add(newData);


            }
        }

        return tempData;
    }

    private boolean isSeeAllTray() {
        return moduleAPI != null && moduleAPI.getSettings() != null &&
                (moduleAPI.getSettings().isSeeAll() ||
                        moduleAPI.getSettings().isSeeAllCard());
    }

    private IPhotoGallerySelectListener iPhotoGallerySelectListener;

    public void setPhotoGalleryImageSelectionListener(IPhotoGallerySelectListener iPhotoGallerySelectListener) {
        this.iPhotoGallerySelectListener = iPhotoGallerySelectListener;
    }

    public PhotoGalleryNextPreviousListener setPhotoGalleryImageSelectionListener(PhotoGalleryNextPreviousListener listener) {
        listener = new PhotoGalleryNextPreviousListener() {
            @Override
            public void previousPhoto(Button previousButton) {

                if (getSelectedPosition() == 0) {
                    return;
                } else if (getSelectedPosition() == 1) {
                    previousButton.setBackgroundColor(Color.parseColor("#c8c8c8"));
                    previousButton.setEnabled(false);
                }
                selectedPosition--;
                iPhotoGallerySelectListener.selectedImageData(adapterData.get(selectedPosition).getGist().getVideoImageUrl(), selectedPosition);
                if (preGist != null)
                    preGist.setSelectedPosition(false);
                preGist = adapterData.get(getSelectedPosition()).getGist();
                adapterData.get(getSelectedPosition()).getGist().setSelectedPosition(true);
                notifyDataSetChanged();
            }

            @Override
            public void nextPhoto(Button nextButton) {
                if (getSelectedPosition() == adapterData.size() - 1) {
                    return;
                } else if (getSelectedPosition() == adapterData.size() - 2 || getSelectedPosition() == 1) {
                    nextButton.setBackgroundColor(Color.parseColor("#c8c8c8"));
                    nextButton.setEnabled(false);
                }
                if (adapterData.size() == 0) {
                    nextButton.setBackgroundColor(Color.parseColor("#c8c8c8"));
                    nextButton.setEnabled(false);
                    return;
                }
                selectedPosition++;
                iPhotoGallerySelectListener.selectedImageData(adapterData.get(selectedPosition).getGist().getVideoImageUrl(), selectedPosition);
                if (preGist != null)
                    preGist.setSelectedPosition(false);
                preGist = adapterData.get(getSelectedPosition()).getGist();
                adapterData.get(getSelectedPosition()).getGist().setSelectedPosition(true);
                notifyDataSetChanged();
            }
        };


        return listener;
    }

    public static int getSelectedPosition() {
        return selectedPosition;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_DEFAULT) {

//            if(blockName!=null && blockName.equalsIgnoreCase("Search01"))
            if (!constrainteView) {

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
                        useRoundedCorners(), this.viewTypeKey, blockName);
                if (view.getChildItems() != null) {
                    for (int i = 0; i < view.getChildItems().size(); i++) {
                        CollectionGridItemView.ItemContainer itemContainer = view.getChildItems().get(i);
                        if (itemContainer != null && itemContainer.getComponent().getKey() != null) {
                            View childView = itemContainer.getChildView();
                            if (itemContainer.getComponent().getKey().equalsIgnoreCase(mContext.getString(R.string.app_cms_page_thumbnail_image_key))) {
                                FrameLayout.LayoutParams layPar = (FrameLayout.LayoutParams) childView.getLayoutParams();
                                seeAllWidth = layPar.width;
                                seeAllHeight = layPar.height;
                            }
                            if (itemContainer.getComponent().getKey().equalsIgnoreCase(mContext.getString(R.string.app_cms_page_thumbnail_title_key))) {
                                seeAllTextSize = ((TextView) childView).getTextSize();
                                break;
                            }
                        }
                    }
                }

                if (viewTypeKey == AppCMSUIKeyType.PAGE_SUBSCRIPTION_SELECTPLAN_03_KEY) {
                    applyBgColorToChildren(view, selectedColor);
                }

                if (viewTypeKey == AppCMSUIKeyType.PAGE_SUBSCRIPTION_SELECTPLAN_01_KEY ||
                        uiBlockName == AppCMSUIKeyType.UI_BLOCK_SELECTPLAN_04) {
                    setBorder(view, unselectedColor);
                }
                if (viewTypeKey == AppCMSUIKeyType.PAGE_PHOTO_TRAY_MODULE_KEY) {
                    view.setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                }


                if (this.viewTypeKey == AppCMSUIKeyType.PAGE_AC_SEARCH_MODULE_KEY && moduleAPI != null && moduleAPI.getTitle() != null && moduleAPI.getTitle().equalsIgnoreCase("ARTICLE")) {
                    view.setBackgroundColor(Color.parseColor(component.getBackgroundColor()));
                }

                return new ViewHolder(view);
            } else {
                MetadataMap metadataMap = null;
                if (moduleAPI != null && moduleAPI.getMetadataMap() != null)
                    metadataMap = moduleAPI.getMetadataMap();
                ConstraintCollectionGridItemView view = constraintViewCreator.createConstraintCollectionGridItemView(parent.getContext(),
                        parentLayout,
                        true,
                        component,
                        moduleAPI,
                        appCMSAndroidModules,
                        settings,
                        jsonValueKeyMap,
                        defaultWidth,
                        defaultHeight,
                        useMarginsAsPercentages,
                        false,
                        this.componentViewType != null ? this.componentViewType : component.getView(),
                        true,
                        false,
                        viewTypeKey, blockName, metadataMap);

                if (view.getChildItems() != null) {
                    for (int i = 0; i < view.getChildItems().size(); i++) {
                        ConstraintCollectionGridItemView.ItemContainer itemContainer = view.getChildItems().get(i);
                        if (itemContainer != null && itemContainer.getComponent().getKey() != null) {
                            View childView = itemContainer.getChildView();
                            if (itemContainer.getComponent().getKey().equalsIgnoreCase(mContext.getString(R.string.app_cms_page_thumbnail_image_key))) {
                                ConstraintLayout.LayoutParams layPar = (ConstraintLayout.LayoutParams) childView.getLayoutParams();
                                seeAllWidth = layPar.width;
                                seeAllHeight = layPar.height;
                            }
                            if (itemContainer.getComponent().getKey().equalsIgnoreCase(mContext.getString(R.string.app_cms_page_thumbnail_title_key))) {
                                seeAllTextSize = ((TextView) childView).getTextSize();
                                break;
                            }
                        }
                    }
                }


                if (viewTypeKey == AppCMSUIKeyType.PAGE_SUBSCRIPTION_SELECTPLAN_03_KEY) {
                    applyBgColorToChildren(view, selectedColor);
                }

                if (viewTypeKey == AppCMSUIKeyType.PAGE_SUBSCRIPTION_SELECTPLAN_01_KEY ||
                        uiBlockName == AppCMSUIKeyType.UI_BLOCK_SELECTPLAN_04) {
                    setBorder(view, unselectedColor);
                }
                if (viewTypeKey == AppCMSUIKeyType.PAGE_PHOTO_TRAY_MODULE_KEY) {
                    view.setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                }


                if (this.viewTypeKey == AppCMSUIKeyType.PAGE_AC_SEARCH_MODULE_KEY && moduleAPI.getTitle().equalsIgnoreCase("ARTICLE")) {
                    view.setBackgroundColor(Color.parseColor(component.getBackgroundColor()));
                }
                return new ViewHolder(view);
            }
        } else {
            TextView seeAllView = new TextView(mContext);
            FrameLayout.LayoutParams layPar = new FrameLayout.LayoutParams(seeAllWidth, seeAllHeight);
            seeAllView.setLayoutParams(layPar);
            seeAllView.setTextColor(appCMSPresenter.getGeneralTextColor());
            seeAllView.setTextSize(seeAllTextSize / mContext.getResources().getDisplayMetrics().scaledDensity);
            seeAllView.setBackgroundColor(appCMSPresenter.getGeneralBackgroundColor());
            seeAllView.setGravity(Gravity.CENTER);

            if (moduleAPI.getTitle() != null) {
                String seeAllTrayTitle = appCMSPresenter.getLanguageResourcesFile().getStringValue(mContext.getString(R.string.app_cms_see_all_tray_title), moduleAPI.getTitle());
                if (localizationResult != null && localizationResult.getSeeAllTray() != null)
                    seeAllTrayTitle = localizationResult.getSeeAllTray() + "\n" + moduleAPI.getTitle();
                else if (genericMessages != null && genericMessages.getSeeAllTray() != null)
                    seeAllTrayTitle = genericMessages.getSeeAllTray() + "\n" + moduleAPI.getTitle();

                Spannable text = new SpannableString(seeAllTrayTitle);
                text.setSpan(new RelativeSizeSpan(1.5f), 8, seeAllTrayTitle.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                text.setSpan(new StyleSpan(Typeface.BOLD), 0, 8, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                seeAllView.setText(text);
            }

            return new ViewHolder(seeAllView);
        }


    }

    public static void increaseFontSizeForPath(Spannable spannable, String path, float increaseTime) {
        int startIndexOfPath = spannable.toString().indexOf(path);
        spannable.setSpan(new RelativeSizeSpan(increaseTime), startIndexOfPath,
                startIndexOfPath + path.length(), 0);
    }

    @Override
    public int getItemViewType(int position) {
//        if (position < adapterData.size()) {

        if (position < adapterSize) {
            return TYPE_DEFAULT;
        } else {
            if (this.adapterData.size() < adapterSize || this.viewTypeKey == AppCMSUIKeyType.PAGE_SEE_ALL_CATEGORY_01_KEY || this.viewTypeKey == AppCMSUIKeyType.PAGE_SEE_ALL_CATEGORY_02_KEY || appCMSPresenter.getSeeAllPage() == null || !isSeeAllTray())
                return TYPE_DEFAULT;
            else
                return TYPE_SEE_ALL;
        }

//        if (emptyList && (viewTypeKey == AppCMSUIKeyType.PAGE_AC_SEARCH_MODULE_KEY || viewTypeKey == AppCMSUIKeyType.PAGE_AC_SEARCH02_MODULE_KEY)) {
//            TextView emptyView = new TextView(mContext);
//            emptyView.setTextColor(appCMSPresenter.getGeneralTextColor());
//            emptyView.setTextSize(24f);
//            emptyView.setText(appCMSPresenter.getLanguageResourcesFile().getUIresource(mContext.getString(R.string.no_results)));
//            }
//        return new ViewHolder(view);
    }

    @Override
    public int getItemCount() {
//        return (adapterData != null ? adapterData.size() : 0);
        if (this.adapterData.size() < adapterSize || this.viewTypeKey == AppCMSUIKeyType.PAGE_SEE_ALL_CATEGORY_02_KEY || this.viewTypeKey == AppCMSUIKeyType.PAGE_SEE_ALL_CATEGORY_01_KEY || appCMSPresenter.getSeeAllPage() == null || !isSeeAllTray())
            return adapterData.size();
        else
            return adapterSize + 1;
    }

    private boolean useRoundedCorners() {
        return mContext.getString(R.string.app_cms_page_subscription_selectionplan_03_key).equals(componentViewType);
    }

    private void applyBgColorToChildren(ViewGroup viewGroup, int bgColor) {
        for (int i = 0; i < viewGroup.getChildCount(); i++) {
            View child = viewGroup.getChildAt(i);
            if (child instanceof ViewGroup) {
                if (child instanceof CardView) {
                    ((CardView) child).setUseCompatPadding(true);
                    ((CardView) child).setPreventCornerOverlap(false);
                    ((CardView) child).setCardBackgroundColor(bgColor);
                } else {
                    child.setBackgroundColor(bgColor);
                }
                applyBgColorToChildren((ViewGroup) child, bgColor);
            }
        }
    }

    private void selectViewPlan(CollectionGridItemView collectionGridItemView, String selectedText) {
        collectionGridItemView.setSelectable(true);
        for (View collectionGridChild : collectionGridItemView.getViewsToUpdateOnClickEvent()) {
            if (collectionGridChild instanceof Button) {
                Component childComponent = collectionGridItemView.matchComponentToView(collectionGridChild);
                if (selectedText == null) {
                    selectedText = childComponent.getSelectedText();
                }
                ((TextView) collectionGridChild).setText(selectedText);
                ((TextView) collectionGridChild).setTextColor(Color.parseColor(CommonUtils.getColor(mContext,
                        childComponent.getTextColor())));
                collectionGridChild.setBackgroundColor(selectedColor);
            }
        }
    }

    private void deselectViewPlan(CollectionGridItemView collectionGridItemView) {
        collectionGridItemView.setSelectable(false);
        for (View collectionGridChild : collectionGridItemView
                .getViewsToUpdateOnClickEvent()) {
            if (collectionGridChild instanceof Button) {
                Component childComponent = collectionGridItemView.matchComponentToView(collectionGridChild);
                ((TextView) collectionGridChild).setText(childComponent.getText());
                collectionGridChild.setBackgroundColor(ContextCompat.getColor(collectionGridItemView.getContext(),
                        R.color.disabledButtonColor));
            }
        }
    }

    private void deselectViewPlan01(CollectionGridItemView collectionGridItemView, String selectedText) {
        collectionGridItemView.setSelectable(false);
        for (View collectionGridChild : collectionGridItemView
                .getViewsToUpdateOnClickEvent()) {
            if (collectionGridChild instanceof Button) {
                Component childComponent = collectionGridItemView.matchComponentToView(collectionGridChild);
                if (selectedText != null && !TextUtils.isEmpty(selectedText)) {
                    ((TextView) collectionGridChild).setText(selectedText);
                } else {
                    ((TextView) collectionGridChild).setText(appCMSPresenter.getLanguageResourcesFile().getUIresource(childComponent.getText()));
                }
                setBorder(collectionGridChild, ContextCompat.getColor(mContext, R.color.disabledButtonColor));
                ((TextView) collectionGridChild).setTextColor(ContextCompat.getColor(mContext, R.color.disabledButtonColor));
            }
        }
    }


    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
//        if (0 <= position && position < adapterData.size()) {
        if (0 <= position && position < adapterSize && holder.constraintComponentView != null) {
            for (int i = 0; i < holder.constraintComponentView.getNumberOfChildren(); i++) {
                if (holder.constraintComponentView.getChild(i) instanceof TextView) {
                    ((TextView) holder.constraintComponentView.getChild(i)).setText("");
                }
            }
            if (viewTypeKey == AppCMSUIKeyType.PAGE_SUBSCRIPTION_SELECTPLAN_01_KEY ||
                    uiBlockName == AppCMSUIKeyType.UI_BLOCK_SELECTPLAN_04 ||
                    viewTypeKey == AppCMSUIKeyType.PAGE_PHOTO_TRAY_MODULE_KEY) {
                planItemView[position] = holder.componentView;
            }
            bindView(holder.constraintComponentView, adapterData.get(position), position);
        } else if (0 <= position && position < adapterSize && holder.componentView != null) {
            for (int i = 0; i < holder.componentView.getNumberOfChildren(); i++) {
                if (holder.componentView.getChild(i) instanceof TextView) {
                    ((TextView) holder.componentView.getChild(i)).setText("");
                }
            }
            if (viewTypeKey == AppCMSUIKeyType.PAGE_SUBSCRIPTION_SELECTPLAN_01_KEY ||
                    uiBlockName == AppCMSUIKeyType.UI_BLOCK_SELECTPLAN_04 ||
                    viewTypeKey == AppCMSUIKeyType.PAGE_PHOTO_TRAY_MODULE_KEY) {
                planItemView[position] = holder.componentView;
            }
            bindView(holder.componentView, adapterData.get(position), position);
        }

        if (viewTypeKey == AppCMSUIKeyType.PAGE_SUBSCRIPTION_SELECTPLAN_01_KEY ||
                uiBlockName == AppCMSUIKeyType.UI_BLOCK_SELECTPLAN_04) {
            for (int i = 0; i < planItemView.length; i++) {
                if (planItemView[i] != null) {
                    String selectedText = null;
                    if (adapterData.get(i) != null &&
                            adapterData.get(i).getPlanDetails() != null &&
                            adapterData.get(i).getPlanDetails().get(0) != null &&
                            adapterData.get(i).getPlanDetails().get(0).getCallToAction() != null) {
                        selectedText = adapterData.get(i).getPlanDetails().get(0).getCallToAction();
                    }
                    if (selectedPosition == i) {
                        setBorder(planItemView[i], selectedColor);
                        selectViewPlan(planItemView[i], selectedText);
                    } else {
                        setBorder(planItemView[i], ContextCompat.getColor(mContext, android.R.color.white));
                        deselectViewPlan01(planItemView[i], selectedText);
                    }
                }
            }
        }
        if (viewTypeKey == AppCMSUIKeyType.PAGE_SUBSCRIPTION_SELECTPLAN_03_KEY ||
                viewTypeKey == AppCMSUIKeyType.PAGE_SUBSCRIPTION_SELECTPLAN_01_KEY ||
                uiBlockName == AppCMSUIKeyType.UI_BLOCK_SELECTPLAN_04 ||
                viewTypeKey == AppCMSUIKeyType.PAGE_PHOTO_TRAY_MODULE_KEY) {
            int selectableIndex = -1;
            for (int i = 0; i < adapterSize; i++) {
                if (holder.componentView.isSelectable()) {
                    selectableIndex = i;
                }
            }

            if (selectableIndex == -1) {
                selectableIndex = 0;
            }

            if (selectableIndex == position) {
                if (viewTypeKey != AppCMSUIKeyType.PAGE_SUBSCRIPTION_SELECTPLAN_03_KEY &&
                        viewTypeKey != AppCMSUIKeyType.PAGE_SUBSCRIPTION_SELECTPLAN_01_KEY &&
                        uiBlockName != AppCMSUIKeyType.UI_BLOCK_SELECTPLAN_04 &&
                        viewTypeKey != AppCMSUIKeyType.PAGE_PHOTO_TRAY_MODULE_KEY) {
                    holder.componentView.setSelectable(true);
                    holder.componentView.performClick();
                }
            } else {
                //
            }

            if (viewTypeKey == AppCMSUIKeyType.PAGE_SUBSCRIPTION_SELECTPLAN_03_KEY ||
                    viewTypeKey == AppCMSUIKeyType.PAGE_SUBSCRIPTION_SELECTPLAN_01_KEY ||
                    uiBlockName == AppCMSUIKeyType.UI_BLOCK_SELECTPLAN_04 ||
                    viewTypeKey == AppCMSUIKeyType.PAGE_PHOTO_TRAY_MODULE_KEY) {
                holder.componentView.setSelectable(true);
            }

            if (viewTypeKey == AppCMSUIKeyType.PAGE_PHOTO_TRAY_MODULE_KEY) {
                setBorder(planItemView[position], adapterData.get(position).getGist().isSelectedPosition() ? selectedColor : ContextCompat.getColor(mContext, android.R.color.white));
            }
        }
        if (holder.seeAllView != null) {
            holder.seeAllView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String action = mContext.getString(R.string.app_cms_see_all_category_action);
                    if (moduleAPI != null && moduleAPI.getSettings() != null
                            && moduleAPI.getSettings().getSeeAllPermalink() != null
                            && moduleAPI.getId() != null) {
                        appCMSPresenter.setSeeAllModule(moduleAPI);
                        appCMSPresenter.setLastPage(false);
                        appCMSPresenter.setOffset(0);
                        appCMSPresenter.launchButtonSelectedAction(moduleAPI.getSettings().getSeeAllPermalink(),
                                action,
                                moduleAPI.getTitle(),
                                null,
                                null,
                                false,
                                0,
                                null);
                    }

                }
            });
        }
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

        //sortPlan(); as per MSEAN-1434

        notifyDataSetChanged();
        listView.setAdapter(this);
        listView.invalidate();
        notifyDataSetChanged();
    }

    @SuppressLint("ClickableViewAccessibility")
    void bindView(View itemView,
                  final ContentDatum data, int position) throws IllegalArgumentException {
        if (onClickHandler == null) {
            if (viewTypeKey == AppCMSUIKeyType.PAGE_SUBSCRIPTION_SELECTPLAN_01_KEY
                    || uiBlockName == AppCMSUIKeyType.UI_BLOCK_SELECTPLAN_04) {
                onClickHandler = new CollectionGridItemView.OnClickHandler() {
                    @Override
                    public void click(CollectionGridItemView collectionGridItemView, Component childComponent, ContentDatum data,
                                      int clickPosition) {
                        if (appCMSPresenter.isSelectedSubscriptionPlan()) {
                            selectedPosition = clickPosition;
                        } else {
                            appCMSPresenter.setSelectedSubscriptionPlan(true);
                        }
                        for (int i = 0; i < planItemView.length; i++) {
                            if (planItemView[i] != null) {
                                String selectedText = null;
                                if (adapterData.get(i) != null &&
                                        adapterData.get(i).getPlanDetails() != null &&
                                        adapterData.get(i).getPlanDetails().get(0) != null &&
                                        adapterData.get(i).getPlanDetails().get(0).getCallToAction() != null) {
                                    selectedText = adapterData.get(i).getPlanDetails().get(0).getCallToAction();
                                }
                                if (selectedPosition == i) {
                                    setBorder(planItemView[i], selectedColor);
                                    selectViewPlan(planItemView[i], selectedText);
                                } else {
                                    setBorder(planItemView[i], ContextCompat.getColor(mContext, android.R.color.white));
                                    deselectViewPlan01(planItemView[i], selectedText);
                                }
                            }
                        }
                        if (childComponent != null &&
                                childComponent.getAction() != null &&
                                purchasePlanAction != null) {
                            if (childComponent.getAction().contains(purchasePlanAction)) {
                                subcriptionPlanClick(collectionGridItemView, data);
                            }
                        }
                    }

                    @Override
                    public void play(Component childComponent, ContentDatum data) {
                        // NO-OP - Play is not implemented here
                    }
                };
            } else if (viewTypeKey == AppCMSUIKeyType.PAGE_SUBSCRIPTION_SELECTPLAN_03_KEY) {
                onClickHandler = new CollectionGridItemView.OnClickHandler() {
                    @Override
                    public void click(CollectionGridItemView collectionGridItemView,
                                      Component childComponent,
                                      ContentDatum data, int clickPosition) {
                        if (isClickable) {
                            subcriptionPlanClick(collectionGridItemView, data);

                        }
                    }

                    @Override
                    public void play(Component childComponent, ContentDatum data) {
                        // NO-OP - Play is not implemented here
                    }
                };
            } else if (viewTypeKey == AppCMSUIKeyType.PAGE_ARTICLE_FEED_MODULE_KEY) {
                onClickHandler = new CollectionGridItemView.OnClickHandler() {
                    @Override
                    public void click(CollectionGridItemView collectionGridItemView,
                                      Component childComponent, ContentDatum data, int clickPosition) {
                        if (childComponent != null && childComponent.getKey() != null) {
                            if (jsonValueKeyMap.get(childComponent.getKey()) == AppCMSUIKeyType.PAGE_THUMBNAIL_READ_MORE_KEY) {
                                if (data.getGist() != null && data.getGist().getMediaType() != null
                                        && data.getGist().getMediaType().toLowerCase().contains(itemView.getContext().getString(R.string.app_cms_article_key_type).toLowerCase())) {
                                    appCMSPresenter.setCurrentArticleIndex(-1);
                                    appCMSPresenter.navigateToArticlePage(data.getGist().getId(), data.getGist().getTitle(), false, null, false);
                                    return;
                                }
                            }
                        }
                    }

                    @Override
                    public void play(Component childComponent, ContentDatum data) {

                    }
                };

            } else if (viewTypeKey == AppCMSUIKeyType.PAGE_BRAND_TRAY_MODULE_KEY ||
                    viewTypeKey == AppCMSUIKeyType.PAGE_BRAND_CAROUSEL_MODULE_TYPE) {
                onClickHandler = new CollectionGridItemView.OnClickHandler() {
                    @Override
                    public void click(CollectionGridItemView collectionGridItemView,
                                      Component childComponent, ContentDatum data, int clickPosition) {
                        if (childComponent != null && childComponent.getKey() != null) {
                            String action = mContext.getString(R.string.app_cms_instructor_details_action);
                            if (data.getGist().getContentType() != null &&
                                    data.getGist().getContentType().equals(episodicContentType)) {
                                action = showAction;
                            }
                            String permalink = data.getGist().getPermalink();
                            String title = data.getGist().getTitle();
                            appCMSPresenter.launchButtonSelectedAction(permalink,
                                    action,
                                    title,
                                    null,
                                    data,
                                    false,
                                    0,
                                    null);
                        }
                    }

                    @Override
                    public void play(Component childComponent, ContentDatum data) {

                    }
                };

            } else if (viewTypeKey == AppCMSUIKeyType.PAGE_SUBSCRIPTION_SELECTPLAN_03_KEY) {
                onClickHandler = new CollectionGridItemView.OnClickHandler() {
                    @Override
                    public void click(CollectionGridItemView collectionGridItemView,
                                      Component childComponent,
                                      ContentDatum data, int clickPosition) {
                        if (isClickable) {
                            subcriptionPlanClick(collectionGridItemView, data);

                        }
                    }

                    @Override
                    public void play(Component childComponent, ContentDatum data) {
                        // NO-OP - Play is not implemented here
                    }
                };
            } else if (viewTypeKey == AppCMSUIKeyType.PAGE_PHOTO_TRAY_MODULE_KEY) {
                onClickHandler = new CollectionGridItemView.OnClickHandler() {
                    @Override
                    public void click(CollectionGridItemView collectionGridItemView, Component childComponent,
                                      ContentDatum data, int clickPosition) {
                        selectedPosition = clickPosition;
                        iPhotoGallerySelectListener.selectedImageData(data.getGist().getVideoImageUrl(), selectedPosition);
                        //selectViewPlan(planItemView[clickPosition], null);
                        for (int i = 0; i < planItemView.length; i++) {
                            if (planItemView[i] != null) {
                                if (clickPosition == i) {
                                    setBorder(planItemView[i], selectedColor);
                                } else {
                                    setBorder(planItemView[i], ContextCompat.getColor(mContext, android.R.color.white));
                                }
                            }
                        }
                    }

                    @Override
                    public void play(Component childComponent, ContentDatum data) {
                    }
                };

            } else {
                if (itemView instanceof ConstraintCollectionGridItemView) {
                    onConstraintClickHandler = new ConstraintCollectionGridItemView.OnClickHandler() {
                        @Override
                        public void click(ConstraintCollectionGridItemView collectionGridItemView, Component childComponent, ContentDatum data, int clickPosition) {
                            onClick(itemView, childComponent, data, clickPosition);
                        }

                        @Override
                        public void play(Component childComponent, ContentDatum data) {
                            onPlay(childComponent, data);
                        }
                    };
                } else {
                    onClickHandler = new CollectionGridItemView.OnClickHandler() {
                        @Override
                        public void click(CollectionGridItemView collectionGridItemView,
                                          Component childComponent,
                                          ContentDatum data, int clickPosition) {
                            onClick(itemView, childComponent, data, clickPosition);
                        }


                        @Override
                        public void play(Component childComponent, ContentDatum data) {
                            onPlay(childComponent, data);
                        }
                    };

                }
            }
        }

        if (viewTypeKey == AppCMSUIKeyType.PAGE_SUBSCRIPTION_SELECTPLAN_03_KEY ||
                uiBlockName == AppCMSUIKeyType.UI_BLOCK_SELECTPLAN_04 ||
                viewTypeKey == AppCMSUIKeyType.PAGE_SUBSCRIPTION_SELECTPLAN_01_KEY) {
            /*itemView.setOnClickListener(v -> onClickHandler.click(itemView,
                    component, data, position));*/
        }
        if (viewTypeKey == AppCMSUIKeyType.PAGE_SUBSCRIPTION_SELECTPLAN_03_KEY ||
                viewTypeKey == AppCMSUIKeyType.PAGE_SUBSCRIPTION_SELECTPLAN_01_KEY
                || uiBlockName == AppCMSUIKeyType.UI_BLOCK_SELECTPLAN_04 ||
                viewTypeKey == AppCMSUIKeyType.PAGE_PHOTO_TRAY_MODULE_KEY) {
            //
        } else {
            itemView.setOnTouchListener((View v, MotionEvent event) -> {
                if (event.getActionMasked() == MotionEvent.ACTION_DOWN) {
                    lastTouchDownEvent = event;
                }

                return false;
            });
            itemView.setOnClickListener(v -> {
                if (isClickable && data != null && data.getGist() != null) {
                    if (v instanceof CollectionGridItemView) {
                        try {
                            int eventX = (int) lastTouchDownEvent.getX();
                            int eventY = (int) lastTouchDownEvent.getY();
                            ViewGroup childContainer = ((CollectionGridItemView) v).getChildrenContainer();
                            int childrenCount = childContainer.getChildCount();
                            for (int i = 0; i < childrenCount; i++) {
                                View childView = childContainer.getChildAt(i);
                                if (childView instanceof Button) {
                                    int[] childLocation = new int[2];
                                    childView.getLocationOnScreen(childLocation);
                                    int childX = childLocation[0] - 8;
                                    int childY = childLocation[1] - 8;
                                    int childWidth = childView.getWidth() + 8;
                                    int childHeight = childView.getHeight() + 8;
                                    if (childX <= eventX && eventX <= childX + childWidth) {
                                        if (childY <= eventY && eventY <= childY + childHeight) {
                                            childView.performClick();
                                            return;
                                        }
                                    }
                                }
                            }
                        } catch (Exception e) {
                            //
                        }
                    }

                    String permalink = data.getGist().getPermalink();
                    String title = data.getGist().getTitle();
                    String action = videoAction;

                    String contentType = "";

                    if (data.getGist() != null && data.getGist().getContentType() != null) {
                        contentType = data.getGist().getContentType();
                    }

                    if (contentType.equals(episodicContentType)) {
                        action = showAction;
                    } else if (contentType.equalsIgnoreCase(person)) {
                        action = personAction;
                    } else if (contentType.equals(fullLengthFeatureType)) {
                        action = videoAction;
                    }
                    /*get audio details on tray click item and play song*/
                    if (data.getGist() != null &&
                            data.getGist().getMediaType() != null &&
                            data.getGist().getMediaType().toLowerCase().contains(itemView.getContext().getString(R.string.media_type_audio).toLowerCase()) &&
                            data.getGist().getContentType() != null &&
                            data.getGist().getContentType().toLowerCase().contains(itemView.getContext().getString(R.string.content_type_audio).toLowerCase())) {
                        List<String> audioPlaylistId = new ArrayList<String>();
                        audioPlaylistId.add(data.getGist().getId());
                        AudioPlaylistHelper.getInstance().setCurrentPlaylistId(data.getGist().getId());
                        AudioPlaylistHelper.getInstance().setCurrentPlaylistData(null);
                        AudioPlaylistHelper.getInstance().setPlaylist(audioPlaylistId);
                        appCMSPresenter.getCurrentActivity().sendBroadcast(new Intent(AppCMSPresenter
                                .PRESENTER_PAGE_LOADING_ACTION));
                        AudioPlaylistHelper.getInstance().playAudioOnClickItem(data.getGist().getId(), 0);
                        return;
                    }

                    /*Get playlist data and open playlist page*/
                    if (data.getGist() != null && data.getGist().getMediaType() != null
                            && data.getGist().getMediaType().toLowerCase().contains(itemView.getContext().getString(R.string.media_type_playlist).toLowerCase())) {
                        appCMSPresenter.navigateToPlaylistPage(data.getGist().getId());
                        return;
                    }

                    //Log.d(TAG, "Launching " + permalink + ":" + action);
                    List<String> relatedVideoIds = null;
                    if (data.getContentDetails() != null &&
                            data.getContentDetails().getRelatedVideoIds() != null) {
                        relatedVideoIds = data.getContentDetails().getRelatedVideoIds();
                    }
                    int currentPlayingIndex = -1;
                    if (relatedVideoIds == null) {
                        currentPlayingIndex = 0;
                    }

                    if (data.getGist() == null ||
                            data.getGist().getContentType() == null) {
                        if (!appCMSPresenter.launchVideoPlayer(data,
                                data.getGist().getId(),
                                currentPlayingIndex,
                                relatedVideoIds,
                                -1,
                                action)) {
                            //Log.e(TAG, "Could not launch action: " +
//                                    " permalink: " +
//                                    permalink +
//                                    " action: " +
//                                    action);
                        }
                    } else if (viewTypeKey == AppCMSUIKeyType.PAGE_LIVE_SCHEDULE_NO_DATA_TYPE) {
                    } else if (viewTypeKey == AppCMSUIKeyType.PAGE_CONTINUE_WATCHING_MODULE_KEY) {
                        action = mContext.getString(R.string.app_cms_action_watchvideo_key);
                        if (!appCMSPresenter.launchVideoPlayer(data,
                                data.getGist().getId(),
                                currentPlayingIndex,
                                relatedVideoIds,
                                -1,
                                action)) {
                            //Log.e(TAG, "Could not launch action: " +
//                                                " permalink: " +
//                                                permalink +
//                                                " action: " +
//                                                action);
                        }
                    } else {

                        if (!appCMSPresenter.launchButtonSelectedAction(permalink,
                                action,
                                title,
                                null,
                                null,
                                false,
                                currentPlayingIndex,
                                relatedVideoIds)) {
                            //Log.e(TAG, "Could not launch action: " +
//                                    " permalink: " +
//                                    permalink +
//                                    " action: " +
//                                    action);
                        }

                    }
                }

            });

        }

        if (itemView instanceof ConstraintCollectionGridItemView) {
            for (int i = 0; i < ((ConstraintCollectionGridItemView) itemView).getNumberOfChildren(); i++) {
                View childView = ((ConstraintCollectionGridItemView) itemView).getChild(i);
                if (component.getSettings() != null && component.getSettings().isNoInfo()
                        && componentViewType.contains("carousel") && !(childView instanceof ImageView)) {
                    continue;
                }
                ((ConstraintCollectionGridItemView) itemView).bindChild(itemView.getContext(),
                        ((ConstraintCollectionGridItemView) itemView).getChild(i),
                        data,
                        jsonValueKeyMap,
                        onConstraintClickHandler,
                        componentViewType,
                        appCMSPresenter.getBrandPrimaryCtaColor(),
                        appCMSPresenter,
                        position,
                        null,
                        blockName, moduleAPI.getMetadataMap());
            }
        } else {
            for (int i = 0; i < ((CollectionGridItemView) itemView).getNumberOfChildren(); i++) {
                ((CollectionGridItemView) itemView).bindChild(itemView.getContext(),
                        ((CollectionGridItemView) itemView).getChild(i),
                        data,
                        jsonValueKeyMap,
                        onClickHandler,
                        componentViewType,
                        appCMSPresenter.getBrandPrimaryCtaColor(),
                        appCMSPresenter,
                        position,
                        settings,
                        blockName, moduleAPI.getMetadataMap());
            }
        }
    }

    void subcriptionPlanClick(CollectionGridItemView collectionGridItemView, ContentDatum data) {
        if (collectionGridItemView.isSelectable()) {
            //Log.d(TAG, "Initiating signup and subscription: " +
//                                        data.getIdentifier());

            double price = data.getPlanDetails().get(0).getStrikeThroughPrice();
            if (price == 0) {
                price = data.getPlanDetails().get(0).getRecurringPaymentAmount();
            }

            double discountedPrice = data.getPlanDetails().get(0).getRecurringPaymentAmount();
            double discountedAmount = data.getPlanDetails().get(0).getDiscountedPrice();
            boolean upgradesAvailable = false;
            for (ContentDatum plan : adapterData) {
                if (plan != null &&
                        plan.getPlanDetails() != null &&
                        !plan.getPlanDetails().isEmpty() &&
                        ((plan.getPlanDetails().get(0).getStrikeThroughPrice() != 0 &&
                                price < plan.getPlanDetails().get(0).getStrikeThroughPrice()) ||
                                (plan.getPlanDetails().get(0).getRecurringPaymentAmount() != 0 &&
                                        price < plan.getPlanDetails().get(0).getRecurringPaymentAmount()))) {
                    upgradesAvailable = true;
                }
            }
            appCMSPresenter.setSelectedPlan(data.getId(), adapterData);
            appCMSPresenter.initiateSignUpAndSubscription(data.getIdentifier(),
                    data.getId(),
                    data.getPlanDetails().get(0).getCountryCode(),
                    data.getName(),
                    price,
                    discountedPrice,
                    data.getPlanDetails().get(0).getRecurringPaymentCurrencyCode(),
                    data.getPlanDetails().get(0).getCountryCode(),
                    data.getRenewable(),
                    data.getRenewalCycleType(),
                    upgradesAvailable, discountedAmount,
                    data.getPlanDetails().get(0).getAllowedPayMethods(),
                    data.getPlanDetails().get(0).getCarrierBillingProviders());
        } else {
            collectionGridItemView.performClick();
        }
    }

    public boolean isClickable() {
        return isClickable;
    }

    @Override
    public void setClickable(boolean clickable) {
        isClickable = clickable;
    }

    private String getDefaultAction(Context context) {
        return context.getString(R.string.app_cms_action_videopage_key);
    }

    private String getShowAction(Context context) {
        return context.getString(R.string.app_cms_action_showvideopage_key);
    }

    private String getPersonAction(Context context) {
        return context.getString(R.string.app_cms_instructor_details_action);
    }

    private String getBundleDetailAction(Context context) {
        return context.getString(R.string.app_cms_action_detailbundlepage_key);
    }

    private String getOpenOptionsAction(Context context) {
        return context.getString(R.string.app_cms_action_open_option_dialog);
    }

    private String getPurchasePlanAction(Context context) {
        return context.getString(R.string.app_cms_action_purchase_plan);
    }

    private String getVideoAction(Context context) {
        return context.getString(R.string.app_cms_action_detailvideopage_key);
    }

    private String getWatchTrailerAction(Context context) {
        return context.getString(R.string.app_cms_action_watchtrailervideo_key);
    }

    private String getHlsUrl(ContentDatum data) {
        if (data.getStreamingInfo() != null &&
                data.getStreamingInfo().getVideoAssets() != null &&
                data.getStreamingInfo().getVideoAssets().getHls() != null) {
            return data.getStreamingInfo().getVideoAssets().getHls();
        }
        return null;
    }

    private void setBorder(View itemView,
                           int color) {
        GradientDrawable planBorder = new GradientDrawable();
        planBorder.setShape(GradientDrawable.RECTANGLE);
        if (BaseView.isTablet(mContext)) {
            planBorder.setStroke(5, color);
            itemView.setPadding(3, 3, 3, 3);
        } else {
            planBorder.setStroke(7, color);
            itemView.setPadding(7, 7, 7, 7);
        }
        planBorder.setColor(ContextCompat.getColor(itemView.getContext(), android.R.color.transparent));

        itemView.setBackground(planBorder);
    }

    public void sortPlan() {
        if (mContext.getResources().getBoolean(R.bool.sort_plans_in_ascending_order)) {
            sortPlansByPriceInAscendingOrder();
        } else {
            sortPlansByPriceInDescendingOrder();
        }
    }

    private void sortPlansByPriceInDescendingOrder() {
        if ((viewTypeKey == AppCMSUIKeyType.PAGE_SUBSCRIPTION_SELECTPLAN_03_KEY) && adapterData != null) {

            Collections.sort(adapterData,
                    (datum1, datum2) -> {
                        if (datum1.getPlanDetails().get(0).getStrikeThroughPrice() == 0 &&
                                datum2.getPlanDetails().get(0).getStrikeThroughPrice() == 0) {
                            return Double.compare(datum2.getPlanDetails().get(0)
                                    .getRecurringPaymentAmount(), datum1.getPlanDetails().get(0)
                                    .getRecurringPaymentAmount());
                        }
                        return Double.compare(datum2.getPlanDetails().get(0)
                                .getStrikeThroughPrice(), datum1.getPlanDetails().get(0)
                                .getStrikeThroughPrice());
                    });
        }
    }

    private void sortPlansByPriceInAscendingOrder() {
        sortPlansByPriceInDescendingOrder();
        Collections.reverse(adapterData);
    }

    @Override
    public void onViewRecycled(ViewHolder holder) {
        super.onViewRecycled(holder);
        if (holder.componentView != null && holder.componentView.getChildItems() != null) {
            int childCount = holder.componentView.getChildCount();
            for (int i = 0; i < childCount; i++) {
                View child = holder.componentView.getChild(i);
                Activity currentActivity = null;
                if (child.getContext() instanceof Activity)
                    currentActivity = (Activity) child.getContext();
                if ((child instanceof ImageView) && (currentActivity != null) && !currentActivity.isDestroyed()) {
                    try {
                        Glide.with(child.getContext()).clear(child);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        CollectionGridItemView componentView;
        ConstraintCollectionGridItemView constraintComponentView;
        TextView seeAllView;

        public ViewHolder(View itemView) {
            super(itemView);
            if (itemView instanceof FrameLayout)
                this.componentView = (CollectionGridItemView) itemView;
            else if (itemView instanceof ConstraintLayout) {
                this.constraintComponentView = (ConstraintCollectionGridItemView) itemView;
            }
        }

        public ViewHolder(TextView itemView) {
            super(itemView);
            this.seeAllView = itemView;
        }
    }

    public void onClick(View itemView, Component childComponent, ContentDatum data, int clickPosition) {
        if (isClickable) {
            if (data.getGist() != null) {
                appCMSPresenter.setPlaySource("");
                if (moduleAPI.getContentData().get(0).getGist() != null &&
                        moduleAPI.getContentData().get(0).getGist().getContentType() != null &&
                        !TextUtils.isEmpty(moduleAPI.getContentData().get(0).getGist().getContentType()) && moduleAPI.getContentData().get(0).getGist().getContentType().contains("SERIES"))
                    appCMSPresenter.setPlaySource(moduleAPI.getContentData().get(0).getGist().getTitle());
                else
                    appCMSPresenter.setPlaySource(moduleAPI.getTitle());

                appCMSPresenter.isRecommendationTrayClicked = false;
                if (moduleAPI.getTitle() != null
                        && moduleAPI.getModuleType() != null
                        && moduleAPI.getModuleType().equalsIgnoreCase(mContext.getString(R.string.app_cms_api_history_module_key))
                        && moduleAPI.getTitle().toLowerCase().indexOf("recommendation") != -1) {
                    appCMSPresenter.isRecommendationTrayClicked = true;
                    //Rendering
                    appCMSPresenter.sendGaEvent(mContext.getResources().getString(R.string.play_video_action),
                            mContext.getResources().getString(R.string.recommend_tray_clicked), "true");
                }

                //Log.d(TAG, "Clicked on item: " + data.getGist().getTitle());
                String permalink = data.getGist().getPermalink();

                if (data.getGist() != null &&
                        data.getGist().getRelatedVideos() != null &&
                        data.getGist().getRelatedVideos().size() > clickPosition &&
                        data.getGist().getRelatedVideos().get(clickPosition) != null &&
                        data.getGist().getRelatedVideos().get(clickPosition).getGist() != null &&
                        data.getGist().getRelatedVideos().get(clickPosition).getGist().getPermalink() != null) {
                    permalink = data.getGist().getRelatedVideos().get(clickPosition).getGist().getPermalink();
                }
                String action = videoAction;
                if (childComponent != null && !TextUtils.isEmpty(childComponent.getAction())) {
                    action = childComponent.getAction();
                }
                String title = data.getGist().getTitle();
                String hlsUrl = getHlsUrl(data);

                @SuppressWarnings("MismatchedReadAndWriteOfArray")
                String[] extraData = new String[3];
                extraData[0] = permalink;
                extraData[1] = hlsUrl;
                extraData[2] = data.getGist().getId();
                //Log.d(TAG, "Launching " + permalink + ": " + action);
                List<String> relatedVideoIds = null;
                if (data.getContentDetails() != null &&
                        data.getContentDetails().getRelatedVideoIds() != null) {
                    relatedVideoIds = data.getContentDetails().getRelatedVideoIds();
                }
                int currentPlayingIndex = -1;
                if (relatedVideoIds == null) {
                    currentPlayingIndex = 0;
                }

                String contentType = "";

                if (data.getGist() != null && data.getGist().getContentType() != null) {
                    contentType = data.getGist().getContentType();
                }


                if (action.contains(videoAction) && data.getGist() != null && data.getGist().getMediaType() != null &&
                        data.getGist().getMediaType().toLowerCase().contains(itemView.getContext().getString(R.string.media_type_video).toLowerCase()))
                    appCMSPresenter.setPlaySource(appCMSPresenter.getPlaySource() + "_Video Detail");
                if (action.contains(videoAction) && data.getGist() != null && data.getGist().getContentType() != null &&
                        data.getGist().getContentType().toLowerCase().contains(itemView.getContext().getString(R.string.media_type_video).toLowerCase()))
                    appCMSPresenter.setPlaySource(appCMSPresenter.getPlaySource() + "_Video Detail");

                if (action.contains(videoAction) && data.getGist() != null &&
                        data.getGist().getMediaType() != null &&
                        data.getGist().getMediaType().toLowerCase().contains(itemView.getContext().getString(R.string.content_type_series).toLowerCase()) &&
                        data.getGist().getContentType() != null &&
                        data.getGist().getContentType().toLowerCase().contains(itemView.getContext().getString(R.string.content_type_series).toLowerCase())) {
                    appCMSPresenter.setPlaySource("");
                    appCMSPresenter.setPlaySource(childComponent.getType());
                    appCMSPresenter.setPlaySource(appCMSPresenter.getPlaySource() + "_Show Detail");
                } else if (action.contains(videoAction) && data.getGist() != null &&
                        data.getGist().getContentType() != null &&
                        data.getGist().getContentType().toLowerCase().contains(itemView.getContext().getString(R.string.content_type_season).toLowerCase())) {
                    appCMSPresenter.setPlaySource("");
                    appCMSPresenter.setPlaySource(childComponent.getType());
                    appCMSPresenter.setPlaySource(appCMSPresenter.getPlaySource() + "_Show Detail");
                } else if (action.contains(videoAction) && data.getGist() != null &&
                        data.getGist().getMediaType() != null &&
                        !data.getGist().getMediaType().toLowerCase().contains(itemView.getContext().getString(R.string.media_type_audio).toLowerCase()) &&
                        data.getGist().getContentType() != null &&
                        !data.getGist().getContentType().toLowerCase().contains(itemView.getContext().getString(R.string.content_type_audio).toLowerCase()) &&
                        !data.getGist().getMediaType().toLowerCase().contains(itemView.getContext().getString(R.string.media_type_playlist).toLowerCase())) {
                    appCMSPresenter.setPlaySource(appCMSPresenter.getPlaySource() + "_Video Detail");
                } else if (data.getGist() != null &&
                        data.getGist().getMediaType() != null &&
                        data.getGist().getMediaType().toLowerCase().contains(itemView.getContext().getString(R.string.media_type_audio).toLowerCase()) &&
                        data.getGist().getContentType() != null &&
                        data.getGist().getContentType().toLowerCase().contains(itemView.getContext().getString(R.string.content_type_audio).toLowerCase())) {
                    appCMSPresenter.setPlaySource(appCMSPresenter.getPlaySource() + "_Music");
                } else if (data.getGist() != null && data.getGist().getMediaType() != null
                        && data.getGist().getMediaType().toLowerCase().contains(itemView.getContext().getString(R.string.media_type_playlist).toLowerCase())) {
                    appCMSPresenter.setPlaySource(appCMSPresenter.getPlaySource() + "_Playlist Screen - " + data.getGist().getTitle());
                }

                if (action.contains("watchVideo") && moduleAPI.getTitle().contains("Continue Watching")) {
                    appCMSPresenter.setPlaySource(appCMSPresenter.getPlaySource() + "_Home Page");
                }
                /*get audio details on tray click item and play song*/
                if (data.getGist() != null &&
                        data.getGist().getMediaType() != null &&
                        data.getGist().getMediaType().toLowerCase().contains(itemView.getContext().getString(R.string.media_type_audio).toLowerCase()) &&
                        data.getGist().getContentType() != null &&
                        data.getGist().getContentType().toLowerCase().contains(itemView.getContext().getString(R.string.content_type_audio).toLowerCase())) {
                    List<String> audioPlaylistId = new ArrayList<String>();
                    audioPlaylistId.add(data.getGist().getId());
                    AudioPlaylistHelper.getInstance().setCurrentPlaylistId(data.getGist().getId());
                    AudioPlaylistHelper.getInstance().setCurrentPlaylistData(null);
                    AudioPlaylistHelper.getInstance().setPlaylist(audioPlaylistId);
                    appCMSPresenter.getCurrentActivity().sendBroadcast(new Intent(AppCMSPresenter
                            .PRESENTER_PAGE_LOADING_ACTION));
                    AudioPlaylistHelper.getInstance().playAudioOnClickItem(data.getGist().getId(), 0);
                    return;
                }

                /*Get playlist data and open playlist page*/
                if (data.getGist() != null && data.getGist().getContentType() != null &&
                        data.getGist().getContentType().toLowerCase().contains(itemView.getContext().getString(R.string.content_type_audio).toLowerCase()) &&
                        data.getGist().getMediaType() != null
                        && data.getGist().getMediaType().toLowerCase().contains(itemView.getContext().getString(R.string.media_type_playlist).toLowerCase())) {
                    appCMSPresenter.navigateToPlaylistPage(data.getGist().getId());
                    return;
                }


                if ((viewTypeKey == AppCMSUIKeyType.PAGE_AC_SEARCH_MODULE_KEY || viewTypeKey == AppCMSUIKeyType.PAGE_AC_SEARCH02_MODULE_KEY || viewTypeKey == AppCMSUIKeyType.PAGE_LIBRARY_01_MODULE_KEY || viewTypeKey == AppCMSUIKeyType.PAGE_LIBRARY_02_MODULE_KEY)
                        && (((data.getGist().getMediaType() == null || data.getGist().getMediaType().toLowerCase().contains(itemView.getContext().getString(R.string.app_cms_bundle_key_type).toLowerCase()))
                        && data.getGist() != null && data.getGist().getContentType() != null && data.getGist().getContentType().toLowerCase().contains(itemView.getContext().getString(R.string.app_cms_bundle_key_type).toLowerCase())))) {
                    action = itemView.getContext().getString(R.string.app_cms_action_detailbundlepage_key);
                    if (!appCMSPresenter.launchButtonSelectedAction(permalink,
                            action,
                            title,
                            null,
                            null,
                            false,
                            0,
                            null)) {
                    }
                    return;
                }

                if ((viewTypeKey == AppCMSUIKeyType.PAGE_AC_SEARCH_MODULE_KEY || viewTypeKey == AppCMSUIKeyType.PAGE_AC_SEARCH02_MODULE_KEY || viewTypeKey == AppCMSUIKeyType.PAGE_LIBRARY_01_MODULE_KEY || viewTypeKey == AppCMSUIKeyType.PAGE_LIBRARY_02_MODULE_KEY)
                        && ((data.getGist() != null && data.getGist().getMediaType() != null
                        && data.getGist().getMediaType().toLowerCase().contains(itemView.getContext().getString(R.string.media_type_episode).toLowerCase())
                        && data.getGist() != null && data.getGist().getContentType() != null && data.getGist().getContentType().toLowerCase().contains(itemView.getContext().getString(R.string.app_cms_bundle_key_type).toLowerCase())))) {
                    action = "lectureDetailPage";
                }

                if (data.getGist() != null && data.getGist().getContentType() != null
                        && data.getGist().getContentType().toLowerCase().contains(itemView.getContext().getString(R.string.app_cms_team_label).toLowerCase())) {
                    appCMSPresenter.navigateToTeamDetailPage("acd7eac5-8bba-46bb-b337-b475df5ef680", data.getGist().getTitle(), false);
                    return;
                }
                if (data.getGist() != null && data.getGist().getContentType() != null &&
                        data.getGist().getContentType().toLowerCase().contains(itemView.getContext().getString(R.string.content_type_audio).toLowerCase()) &&
                        data.getGist().getMediaType() != null
                        && data.getGist().getMediaType().toLowerCase().contains(itemView.getContext().getString(R.string.media_type_playlist).toLowerCase())) {
                    appCMSPresenter.navigateToPlaylistPage(data.getGist().getId());
                    return;
                }
                if (action.contains(openOptionsAction)) {
                    appCMSPresenter.launchButtonSelectedAction(permalink,
                            openOptionsAction,
                            title,
                            null,
                            data,
                            false,
                            currentPlayingIndex,
                            relatedVideoIds);
                    return;
                }
                if (contentType.equals(episodicContentType) || contentType.equalsIgnoreCase(seasonContentType)) {
                    action = showAction;
                } else if (contentType.equals(fullLengthFeatureType)) {
                    action = action != null && action.equalsIgnoreCase("openOptionDialog") ? action : videoAction;
                }
//                                if(data.getGist().getTitle().equalsIgnoreCase("Test Encoding ")){
//                                    action=bundleDetailAction;
//                                }
                if (data.getGist() != null && data.getGist().getMediaType() != null
                        && data.getGist().getMediaType().toLowerCase().contains(itemView.getContext().getString(R.string.media_type_bundle).toLowerCase())) {
                    action = bundleDetailAction;
                }
//                                if (data.getGist() != null && data.getGist().getMediaType() != null
//                                        && data.getGist().getMediaType().toLowerCase().contains(itemView.getContext().getString(R.string.media_type_episode).toLowerCase())) {
//                                    action = showAction;
//
//                                }
                if (data.getGist() != null && data.getGist().getMediaType() != null
                        && data.getGist().getMediaType().toLowerCase().contains(itemView.getContext().getString(R.string.app_cms_article_key_type).toLowerCase())) {
                    appCMSPresenter.setCurrentArticleIndex(-1);
                    appCMSPresenter.navigateToArticlePage(data.getGist().getId(), data.getGist().getTitle(), false, null, false);
                    return;
                }
                if (data.getGist() != null && data.getGist().getContentType() != null
                        && data.getGist().getContentType().toLowerCase().contains(itemView.getContext().getString(R.string.app_cms_event_key_type).toLowerCase())) {
                    appCMSPresenter.navigateToEventDetailPage(data.getGist().getPermalink());
                    return;
                }
                //PHOTOGALLERY
                if (data.getGist() != null && data.getGist().getMediaType() != null
                        && data.getGist().getMediaType().contains("PHOTOGALLERY")) {
                    appCMSPresenter.setCurrentPhotoGalleryIndex(clickPosition);
                    appCMSPresenter.navigateToPhotoGalleryPage(data.getGist().getId(), data.getGist().getTitle(), adapterData, false);
                    return;
                }

                if (viewTypeKey == AppCMSUIKeyType.PAGE_AC_BUNDLEDETAIL_TRAY_MODULE_KEY && appCMSPresenter.getAppCMSMain().isMonetizationModelEnabled()) {
                    ContentTypeChecker contentTypeChecker = new ContentTypeChecker(mContext);
                    boolean isTveContent = appCMSPresenter.getAppPreference().getTVEUserId() != null
                            && contentTypeChecker.isContentTVEMonetization(data.getMonetizationModels());
                    boolean isContentPurchased = appCMSPresenter.isUserLoggedIn() && appCMSPresenter.getAppPreference().getUserPurchases() != null
                            && !TextUtils.isEmpty(appCMSPresenter.getAppPreference().getUserPurchases())
                            && contentTypeChecker.isContentPurchased(appCMSPresenter.getAppPreference().getUserPurchases(), moduleAPI.getContentData().get(0).getGist().getId());
                    boolean isContentFree = contentTypeChecker.isContentFREEMonetization(data.getMonetizationModels()) || contentTypeChecker.isContentAVODMonetization(data.getMonetizationModels());
                    boolean isContentSvod = appCMSPresenter.isUserSubscribed() && contentTypeChecker.isContentSVODMonetization(data.getMonetizationModels());
                    /*if (isTveContent || isContentPurchased || isContentFree || isContentSvod) {
                        action = mContext.getString(R.string.app_cms_action_watchvideo_key);
                        appCMSPresenter.launchButtonSelectedAction(permalink,
                                action,
                                title,
                                null,
                                data,
                                false,
                                currentPlayingIndex,
                                relatedVideoIds);
                    } else {
                        data.setModuleApi(moduleAPI);
                        appCMSPresenter.openEntitlementScreen(data);
                    }*/
                    if (isTveContent || isContentPurchased || isContentFree || isContentSvod) {
                        action = mContext.getString(R.string.app_cms_action_watchvideo_key);
                        appCMSPresenter.launchButtonSelectedAction(permalink,
                                action,
                                title,
                                null,
                                data,
                                false,
                                currentPlayingIndex,
                                relatedVideoIds);
                    } else {
                        action = mContext.getString(R.string.app_cms_action_detailvideopage_key);
                        appCMSPresenter.launchButtonSelectedAction(permalink,
                                action,
                                title,
                                null,
                                data,
                                false,
                                currentPlayingIndex,
                                relatedVideoIds);
                    }

                } else if (data.getGist() == null ||
                        data.getGist().getContentType() == null) {

                    if ((data.getPricing() != null && data.getPricing().getType() != null && ((data.getPricing().getType().equalsIgnoreCase(mContext.getString(R.string.PURCHASE_TYPE_TVOD))
                            || data.getPricing().getType().equalsIgnoreCase(mContext.getString(R.string.PURCHASE_TYPE_PPV))) ||
                            (data.getPricing().getType().equalsIgnoreCase(mContext.getString(R.string.PURCHASE_TYPE_SVOD_TVOD))
                                    || data.getPricing().getType().equalsIgnoreCase(mContext.getString(R.string.PURCHASE_TYPE_SVOD_PPV)))))) {

                        int finalCurrentPlayingIndex = currentPlayingIndex;
                        List<String> finalRelatedVideoIds = relatedVideoIds;
                        String finalAction = action;


                        appCMSPresenter.getTransactionData(data.getGist().getId(), updatedContentDatum -> {

                            boolean isPlayable = true;
                            AppCMSTransactionDataValue objTransactionData = null;

                            boolean isPurchased = false;

                            if (updatedContentDatum != null &&
                                    updatedContentDatum.size() > 0) {
                                if (updatedContentDatum.get(0).size() == 0) {
                                    isPlayable = false;
                                } else {
                                    objTransactionData = updatedContentDatum.get(0).get(data.getGist().getId());

                                }
                            } else {
                                isPlayable = false;

                            }

                            /**
                             * If pricing type is svod+tvod or svod+tvod then check if user is subscribed or
                             * content is purchased by user ,if both conditions are false then show subscribe message.
                             */
                            if (data.getPricing().getType().equalsIgnoreCase(mContext.getString(R.string.PURCHASE_TYPE_SVOD_TVOD))
                                    || data.getPricing().getType().equalsIgnoreCase(mContext.getString(R.string.PURCHASE_TYPE_SVOD_PPV))) {

//                                                if (!appCMSPresenter.isUserLoggedIn()) {
//                                                    appCMSPresenter.showSubscribeMessage();
//                                                    return;
//                                                }else{
//                                                    appCMSPresenter.launchVideoPlayer(data,
//                                                            data.getGist().getId(),
//                                                            finalCurrentPlayingIndex,
//                                                            finalRelatedVideoIds,
//                                                            -1,
//                                                            finalAction);
//                                                    return;
//                                                }
                                if (appCMSPresenter.isUserSubscribed() || isPurchased) {
                                    isPlayable = true;
                                } else {

                                    if (!appCMSPresenter.getAppCMSMain().getFeatures().getFreePreview().isFreePreview()) {
                                        appCMSPresenter.showSubscribeMessage();
                                        isPlayable = false;
                                        return;
                                    }

                                }
                                if (isPlayable) {
                                    appCMSPresenter.launchVideoPlayer(data,
                                            data.getGist().getId(),
                                            finalCurrentPlayingIndex,
                                            finalRelatedVideoIds,
                                            -1,
                                            finalAction);
                                    return;
                                }
                            }
                            if (!isPlayable) {
                                if (localisedStrings.getCannotPurchaseItemMsg(appCMSPresenter.getAppCMSMain().getDomainName()) == null)
                                    appCMSPresenter.showNoPurchaseDialog(appCMSPresenter.getLanguageResourcesFile().getUIresource(mContext.getString(R.string.rental_title)), appCMSPresenter.getLanguageResourcesFile().getStringValue(mContext.getString(R.string.cannot_purchase_item_msg), appCMSPresenter.getAppCMSMain().getDomainName()));
                                else
                                    appCMSPresenter.showNoPurchaseDialog(appCMSPresenter.getLanguageResourcesFile().getUIresource(mContext.getString(R.string.rental_title)), localisedStrings.getCannotPurchaseItemMsg(appCMSPresenter.getAppCMSMain().getDomainName()));
                            } else {

                                String rentalPeriod = "";
                                if (data.getPricing() != null && data.getPricing().getRent() != null &&
                                        data.getPricing().getRent().getRentalPeriod() > 0) {
                                    rentalPeriod = String.valueOf(data.getPricing().getRent().getRentalPeriod());
                                }
                                if (objTransactionData != null) {
                                    rentalPeriod = String.valueOf(objTransactionData.getRentalPeriod());
                                }

                                boolean isShowRentalPeriodDialog = true;
                                /**
                                 * if transaction getdata api containf transaction end date .It means Rent API called before
                                 * and we have shown rent period dialog before so dont need to show rent dialog again. else sow rent period dilaog
                                 */
                                isShowRentalPeriodDialog = (objTransactionData != null && objTransactionData.getPurchaseStatus() != null && objTransactionData.getPurchaseStatus().equalsIgnoreCase("RENTED"));
                                if (isShowRentalPeriodDialog) {

                                    if (rentalPeriod == null || TextUtils.isEmpty(rentalPeriod)) {
                                        rentalPeriod = "0";
                                    }
                                    String msg = appCMSPresenter.getLanguageResourcesFile().getStringValue(mContext.getString(R.string.rent_time_dialog_mssg),
                                            rentalPeriod);
                                    if (localisedStrings.getRentTimeDialogMsg(rentalPeriod) != null)
                                        msg = localisedStrings.getRentTimeDialogMsg(rentalPeriod);
                                    appCMSPresenter.showRentTimeDialog(retry -> {
                                        if (retry) {
                                            appCMSPresenter.getRentalData(moduleAPI.getContentData().get(0).getGist().getId(), getRentalData -> {
                                                if (!appCMSPresenter.launchVideoPlayer(data,
                                                        data.getGist().getId(),
                                                        finalCurrentPlayingIndex,
                                                        finalRelatedVideoIds,
                                                        -1,
                                                        finalAction)) {

                                                }
                                            }, false, 0);
                                        } else {
//                                                appCMSPresenter.sendCloseOthersAction(null, true, false);
                                        }
                                    }, msg, "", "", "", true, true);
                                } else {
                                    if (!appCMSPresenter.launchVideoPlayer(data,
                                            data.getGist().getId(),
                                            finalCurrentPlayingIndex,
                                            finalRelatedVideoIds,
                                            -1,
                                            finalAction)) {
                                        //Log.e(TAG, "Could not launch action: " +
//                                                " permalink: " +
//                                                permalink +
//                                                " action: " +
//                                                action);
                                    }
                                }

                            }

                        }, "Video");
                    } else {
                        if (!appCMSPresenter.isNetworkConnected() && !appCMSPresenter.isVideoDownloaded(data.getGist().getId())) { //checking isVideoOffline here to fix SVFA-1431 in offline mode
                            appCMSPresenter.launchButtonSelectedAction(permalink,
                                    action,
                                    title,
                                    null,
                                    action.equalsIgnoreCase("openOptionDialog") ? data : null,
                                    false,
                                    currentPlayingIndex,
                                    relatedVideoIds);


                        } else if (!appCMSPresenter.launchVideoPlayer(data,
                                data.getGist().getId(),
                                currentPlayingIndex,
                                relatedVideoIds,
                                -1,
                                action)) {
                            //Log.e(TAG, "Could not launch action: " +
//                                                " permalink: " +
//                                                permalink +
//                                                " action: " +
//                                                action);
                        }
                    }
                } else if (viewTypeKey == AppCMSUIKeyType.PAGE_LIVE_SCHEDULE_NO_DATA_TYPE) {
                } else if (viewTypeKey == AppCMSUIKeyType.PAGE_CONTINUE_WATCHING_MODULE_KEY) {
                    action = mContext.getString(R.string.app_cms_action_watchvideo_key);
                    if (!appCMSPresenter.launchVideoPlayer(data,
                            data.getGist().getId(),
                            currentPlayingIndex,
                            relatedVideoIds,
                            -1,
                            action)) {
                        //Log.e(TAG, "Could not launch action: " +
//                                                " permalink: " +
//                                                permalink +
//                                                " action: " +
//                                                action);
                    }
                } else {
                    if (data.getGist() != null && data.getGist().getContentType() != null &&
                            data.getGist().getContentType().toLowerCase().contains(itemView.getContext().getString(R.string.content_type_video).toLowerCase()) &&
                            data.getGist().getMediaType() != null
                            && data.getGist().getMediaType().toLowerCase().contains(itemView.getContext().getString(R.string.media_type_playlist).toLowerCase())) {
                        action = mContext.getString(R.string.app_cms_video_playlist_page_action);
                    }
                    if (appCMSPresenter.getAppCMSMain() != null &&
                            appCMSPresenter.getAppCMSMain().getFeatures() != null &&
                            appCMSPresenter.getAppCMSMain().getFeatures().isOpenShowDetail() &&
                            action != null &&
                            action.equalsIgnoreCase(mContext.getString(R.string.app_cms_action_detailvideopage_key)) &&
                            data.getSeriesData() != null &&
                            data.getSeriesData().size() > 0 && data.getSeriesData().get(0).getGist() != null &&
                            data.getSeriesData().get(0).getGist().getPermalink() != null &&
                            data.getGist() != null) {
                        action = mContext.getString(R.string.app_cms_action_showvideopage_key);
                        if (data.getSeriesData().get(0).getGist().getPermalink() != null) {
                            permalink = data.getSeriesData().get(0).getGist().getPermalink();
                        }
                    }
                    if (appCMSPresenter.getAppCMSMain().getFeatures().isTrickPlay() &&
                            appCMSPresenter.getAppCMSMain() != null &&
                            appCMSPresenter.getAppCMSMain().getFeatures() != null &&
                            !appCMSPresenter.getAppCMSMain().getFeatures().isOpenShowDetail() &&
                            data.getGist() != null && data.getGist().getContentType() != null
                            && data.getGist().getContentType().toLowerCase().contains(itemView.getContext().getString(R.string.content_type_video).toLowerCase())) {
                        data.setModuleApi(moduleAPI);
                        appCMSPresenter.launchButtonSelectedAction(permalink,
                                mContext.getString(R.string.app_cms_action_watchvideo_key),
                                title,
                                null,
                                /*action.equalsIgnoreCase("openOptionDialog") ? data : null*/data,
                                false,
                                currentPlayingIndex,
                                relatedVideoIds);
                    } else {
                        if (data.getGist().getContentType() != null && data.getGist().getContentType().toLowerCase().contains(itemView.getContext().getString(R.string.content_type_person).toLowerCase()))
                            appCMSPresenter.navigateToPersonDetailPage(permalink);
                        else
                            appCMSPresenter.setEpisodeId(null);

                        if (CommonUtils.isRecommendationTrayModule(blockName)){
                            CommonUtils.recommendationPlay(appCMSPresenter, moduleAPI);
                        }else {
                            CommonUtils.recommendedIds = null;
                        }
                        data.setModuleApi(moduleAPI);
                            appCMSPresenter.launchButtonSelectedAction(permalink,
                                    action,
                                    title,
                                    null,
                                    /*action.equalsIgnoreCase("openOptionDialog") ? data : null*/data,
                                    false,
                                    currentPlayingIndex,
                                    relatedVideoIds);
                    }
                }
            }
        }
    }

    public void onPlay(Component childComponent, ContentDatum data) {
        if (isClickable) {
            if (data.getGist() != null) {
                //Log.d(TAG, "Playing item: " + data.getGist().getTitle());
                String filmId = null;
                List<String> relatedVideoIds = null;
                if (childComponent.getAction() != null && childComponent.getAction().contains(watchTrailerAction)) {
                    if (moduleAPI.getContentData().get(0) != null &&
                            data.getShowDetails() != null &&
                            data.getShowDetails().getTrailers() != null &&
                            data.getShowDetails().getTrailers().get(0) != null &&
                            data.getShowDetails().getTrailers().get(0).getId() != null) {
                        filmId = moduleAPI.getContentData().get(0).getShowDetails().getTrailers().get(0).getId();
                    }
                } else {
                    filmId = data.getGist().getId();
                    if (data.getContentDetails() != null &&
                            data.getContentDetails().getRelatedVideoIds() != null) {
                        relatedVideoIds = data.getContentDetails().getRelatedVideoIds();
                    }
                }
                String permaLink = data.getGist().getPermalink();
                String title = data.getGist().getTitle();

                int currentPlayingIndex = -1;
                if (relatedVideoIds == null) {
                    currentPlayingIndex = 0;
                }
                if (!appCMSPresenter.launchVideoPlayer(data,
                        filmId,
                        currentPlayingIndex,
                        relatedVideoIds,
                        -1,
                        null)) {
                    //Log.e(TAG, "Could not launch play action: " +
//                                            " filmId: " +
//                                            filmId +
//                                            " permaLink: " +
//                                            permaLink +
//                                            " title: " +
//                                            title);
                }
            }
        }
    }
}
