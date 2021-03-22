package com.viewlift.views.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.viewlift.R;
import com.viewlift.models.data.appcms.api.ContentDatum;
import com.viewlift.models.data.appcms.api.Module;
import com.viewlift.models.data.appcms.ui.AppCMSUIKeyType;
import com.viewlift.models.data.appcms.ui.android.AppCMSAndroidModules;
import com.viewlift.models.data.appcms.ui.page.Component;
import com.viewlift.models.data.appcms.ui.page.Layout;
import com.viewlift.models.data.appcms.ui.page.Settings;
import com.viewlift.presenters.AppCMSPresenter;
import com.viewlift.views.customviews.BaseView;
import com.viewlift.views.customviews.CollectionGridItemView;
import com.viewlift.views.customviews.PageView;
import com.viewlift.views.customviews.ViewCreator;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class AppCMSSeeAllAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>
        implements AppCMSBaseAdapter {
    private final RecyclerView recyclerView;
    protected Context mContext;
    protected Layout parentLayout;
    protected Component component;
    protected AppCMSPresenter appCMSPresenter;
    protected Settings settings;
    protected ViewCreator viewCreator;
    protected Map<String, AppCMSUIKeyType> jsonValueKeyMap;
    Module moduleAPI;
    public List<ContentDatum> adapterData;
    CollectionGridItemView.OnClickHandler onClickHandler;
    int defaultWidth;
    int defaultHeight;
    boolean useMarginsAsPercentages;
    String componentViewType;
    AppCMSAndroidModules appCMSAndroidModules;
    private boolean useParentSize;
    private AppCMSUIKeyType viewTypeKey;
    private AppCMSUIKeyType uiBlockName;
    int adapterSize = 0;
    String blockName;
    boolean constrainteView;
    private PageView pageView;
    private boolean isLoadingAdded = false;
    public static final int ITEM = 0;
    public static final int LOADING = 1;
    private boolean isClickable;
    private boolean retryPageLoad = false;
    private String errorMsg;


    public AppCMSSeeAllAdapter(Context context,
                               ViewCreator viewCreator,
                               AppCMSPresenter appCMSPresenter,
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
                               PageView pageView,
                               RecyclerView recyclerView) {
        this.mContext = context;
        this.viewCreator = viewCreator;
        this.appCMSPresenter = appCMSPresenter;
        this.parentLayout = parentLayout;
        this.useParentSize = useParentSize;
        this.component = component;
        this.jsonValueKeyMap = jsonValueKeyMap;
        this.moduleAPI = moduleAPI;
        this.componentViewType = viewType;
        this.constrainteView = constrainteView;

        this.pageView = pageView;
        this.blockName = blockName;
        this.uiBlockName = jsonValueKeyMap.get(blockName);
        this.isClickable = false;
        if (blockName == null) {
            this.uiBlockName = AppCMSUIKeyType.PAGE_EMPTY_KEY;
        }
        this.viewTypeKey = jsonValueKeyMap.get(componentViewType);
        if (this.viewTypeKey == null) {
            this.viewTypeKey = AppCMSUIKeyType.PAGE_EMPTY_KEY;
        }
        if (moduleAPI != null && moduleAPI.getContentData() != null) {
            this.adapterData = moduleAPI.getContentData();
        } else {
            this.adapterData = new ArrayList<>();
        }
        adapterSize = adapterData.size();
        this.defaultWidth = defaultWidth;
        this.defaultHeight = defaultHeight;
        this.useMarginsAsPercentages = true;
        this.appCMSAndroidModules = appCMSAndroidModules;
        this.recyclerView = recyclerView;
        int spanCount = ((GridLayoutManager)recyclerView.getLayoutManager()).getSpanCount();
        System.out.println(" spanCount "+spanCount);
        if ((spanCount==1 || spanCount == 2) && !BaseView.isTablet(context)){
            component.getLayout().getMobile().setGridHeight(spanCount==2?155:250);
            component.getLayout().getMobile().setGridWidth(spanCount==2?170:355);
            for (Component childComponent : component.getComponents()){
                if (childComponent.getType().toLowerCase().equalsIgnoreCase("image")){
                    childComponent.getLayout().getMobile().setWidth(spanCount==2?170:355);
                    childComponent.getLayout().getMobile().setHeight(spanCount==2?128:200);
                }else if (childComponent.getType().toLowerCase().equalsIgnoreCase("label")){
                    childComponent.getLayout().getMobile().setThumbnailWidth(spanCount==2?170:355);
                    childComponent.getLayout().getMobile().setThumbnailHeight(spanCount==2?120:220);
                }
            }
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder = null;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        if (viewType == ITEM) {
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
            viewHolder = new ViewHolder(view);
        } else {
            View viewLoading = inflater.inflate(R.layout.item_progress, parent, false);
            viewHolder = new LoadingVH(viewLoading);
        }
        return viewHolder;
    }


    @Override
    public int getItemCount() {
        return adapterData != null && adapterData.size() > 0 ? adapterData.size() : 1;
    }

    @Override
    public int getItemViewType(int position) {
        return (position == adapterData.size() - 1 && isLoadingAdded) ? LOADING : ITEM;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        LinearLayoutManager layoutManager = ((LinearLayoutManager) recyclerView.getLayoutManager());
        int firstVisiblePosition = layoutManager.findFirstVisibleItemPosition();
        switch (getItemViewType(position)) {
            case ITEM:
                final ViewHolder viewHolder = (ViewHolder) holder;
                if (0 <= position && position < adapterData.size() && viewHolder.componentView != null) {
                    bindView(viewHolder.componentView, adapterData.get(position), position);
                }

                for (int i = 0; i < viewHolder.componentView.getChildItems().size(); i++) {
                    CollectionGridItemView.ItemContainer itemContainer = viewHolder.componentView.getChildItems().get(i);
                    if (itemContainer.getComponent().getKey() != null) {

                    }
                }
                break;

            case LOADING:
                LoadingVH loadingVH = (LoadingVH) holder;

                if (retryPageLoad) {
                    loadingVH.mErrorLayout.setVisibility(View.VISIBLE);
                    loadingVH.mProgressBar.setVisibility(View.GONE);

                    loadingVH.mErrorTxt.setText(
                            errorMsg != null ?
                                    errorMsg :
                                    mContext.getString(R.string.error));

                } else {
                    loadingVH.mErrorLayout.setVisibility(View.GONE);
                    loadingVH.mProgressBar.setVisibility(View.VISIBLE);
                }
                break;
        }

    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        //recyclerView.scrollToPosition(row_index);

    }

    @Override
    public void resetData(RecyclerView listView) {
        // notifyDataSetChanged();
    }

    @Override
    public void updateData(RecyclerView listView, List<ContentDatum> contentData) {

    }

    @SuppressLint("ClickableViewAccessibility")
    void bindView(View itemView,
                  final ContentDatum data, int position) throws IllegalArgumentException {
        if (onClickHandler == null) {
            onClickHandler = new CollectionGridItemView.OnClickHandler() {
                @Override
                public void click(CollectionGridItemView collectionGridItemView,
                                  Component childComponent, ContentDatum data, int clickPosition) {
                    if (isClickable) {
                        String action;
                        if (data != null && data.getGist() != null
                                && data.getGist().getPermalink() != null
                                && data.getGist().getContentType() != null) {
                            if (appCMSPresenter.getAppCMSMain().getFeatures().isTrickPlay()
                                    && data.getGist() != null && data.getGist().getContentType() != null
                                    && data.getGist().getContentType().toLowerCase().contains(mContext.getString(R.string.content_type_video).toLowerCase())) {
                                action = mContext.getString(R.string.app_cms_action_watchvideo_key);
                                appCMSPresenter.launchVideoPlayer(data,
                                        data.getGist().getId(),
                                        0,
                                        null,
                                        -1,
                                        action);
                                return;
                            }else if (data.getGist().getContentType().toLowerCase().contains(mContext.getString(R.string.app_cms_full_length_feature_key_type).toLowerCase()))
                            {
                                action = mContext.getString(R.string.app_cms_action_detailvideopage_key);
                            }  else if (data.getGist().getContentType().toLowerCase().contains(mContext.getString(R.string.app_cms_bundle_key_type).toLowerCase())) {
                                action = mContext.getString(R.string.app_cms_action_detailbundlepage_key);
                            } else if (data.getGist().getContentType().toLowerCase().contains(mContext.getString(R.string.app_cms_photo_gallery_key_type).toLowerCase())
                                    && data.getGist().getContentType().toLowerCase().contains(mContext.getString(R.string.app_cms_photo_image_key_type).toLowerCase())) {
                                action = mContext.getString(R.string.app_cms_action_photo_gallerypage_key);
                            } else if(data.getGist().getContentType().toLowerCase().contains(itemView.getContext().getString(R.string.app_cms_article_key_type).toLowerCase())) {
                                appCMSPresenter.setCurrentArticleIndex(-1);
                                appCMSPresenter.navigateToArticlePage(data.getGist().getId(), data.getGist().getTitle(), false, null, false);
                                return;
                            } else  {
                                action = mContext.getString(R.string.app_cms_action_showvideopage_key);
                            }
                            appCMSPresenter.launchButtonSelectedAction(data.getGist().getPermalink(),
                                    action,
                                    data.getGist().getTitle(),
                                    null,
                                    null,
                                    false,
                                    0,
                                    null);
                            ((GridLayoutManager) recyclerView.getLayoutManager()).scrollToPositionWithOffset(clickPosition, 0);
                        }
                    }
                }

                @Override
                public void play(Component childComponent, ContentDatum data) {

                }
            };
        }
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
                    null,
                    blockName, moduleAPI.getMetadataMap());
        }
    }

    public boolean isClickable() {
        return isClickable;
    }

    @Override
    public void setClickable(boolean clickable) {
        isClickable = clickable;
    }

    public void addAll(List objectList) {
        adapterData.addAll(objectList);
        notifyItemInserted(adapterData.size() - 1);
        setClickable(true);
    }

    public void add(Object object) {
        ContentDatum contentDatum = new ContentDatum();
        adapterData.add(contentDatum);
        notifyItemInserted(adapterData.size() - 1);
    }

    public void remove(Object object) {
        int position = adapterData.indexOf(object);
        if (position > -1) {
            adapterData.remove(position);
            notifyItemRemoved(position);
        }
    }

    public void clear() {
        isLoadingAdded = false;
        while (getItemCount() > 0) {
            remove(adapterData.get(0));
        }
    }

    public void addLoadingFooter() {
        isLoadingAdded = true;
        ContentDatum contentDatum = new ContentDatum();
        add(contentDatum);
    }

    public void removeLoadingFooter() {
        isLoadingAdded = false;

        int position = adapterData.size() - 1;
        {
            adapterData.remove(position);
            notifyItemRemoved(position);
        }
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        CollectionGridItemView componentView;

        public ViewHolder(View itemView) {
            super(itemView);
            this.componentView = (CollectionGridItemView) itemView;
        }
    }

    protected class LoadingVH extends RecyclerView.ViewHolder implements View.OnClickListener {
        private ProgressBar mProgressBar;
        private ImageButton mRetryBtn;
        private TextView mErrorTxt;
        private LinearLayout mErrorLayout;

        public LoadingVH(View itemView) {
            super(itemView);

            mProgressBar = itemView.findViewById(R.id.loadmore_progress);
            mRetryBtn = itemView.findViewById(R.id.loadmore_retry);
            mErrorTxt = itemView.findViewById(R.id.loadmore_errortxt);
            mErrorLayout = itemView.findViewById(R.id.loadmore_errorlayout);

            mRetryBtn.setOnClickListener(this);
            mErrorLayout.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.loadmore_retry:
                case R.id.loadmore_errorlayout:

                    showRetry(false, null);
                    //mCallback.retryPageLoad();


                    break;
            }
        }
    }

    public void showRetry(boolean show, @Nullable String errorMsg) {
        retryPageLoad = show;
        notifyItemChanged(adapterData.size() - 1);

        if (errorMsg != null) this.errorMsg = errorMsg;
    }
}