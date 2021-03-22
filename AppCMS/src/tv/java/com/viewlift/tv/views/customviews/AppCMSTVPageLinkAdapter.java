package com.viewlift.tv.views.customviews;

import android.content.Context;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.viewlift.R;
import com.viewlift.models.data.appcms.api.CategoryPages;
import com.viewlift.models.data.appcms.api.ContentDatum;
import com.viewlift.models.data.appcms.api.Gist;
import com.viewlift.models.data.appcms.api.Module;
import com.viewlift.models.data.appcms.ui.AppCMSUIKeyType;
import com.viewlift.models.data.appcms.ui.page.Component;
import com.viewlift.models.data.appcms.ui.page.Layout;
import com.viewlift.models.data.appcms.ui.page.Settings;
import com.viewlift.presenters.AppCMSPresenter;
import com.viewlift.tv.utility.Utils;
import com.viewlift.views.customviews.InternalEvent;
import com.viewlift.views.customviews.OnInternalEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.viewlift.models.data.appcms.ui.AppCMSUIKeyType.PAGE_LINK_HEADER_TITLE;

/**
 * Created by anas.azeem on 9/7/2017.
 * Owned by ViewLift, NYC
 */

public class AppCMSTVPageLinkAdapter
        extends RecyclerView.Adapter<RecyclerView.ViewHolder>
        implements OnInternalEvent {

    private final Settings settings;
    private List<ContentDatum> adapterData;
    private final AppCMSPresenter appCMSPresenter;
    private final Map<String, AppCMSUIKeyType> jsonValueKeyMap;
    private final String viewType;
    private final TVViewCreator tvViewCreator;
    private final Context context;
    private final Module module;
    private final Layout parentLayout;
    private final Component component;
    private AppCMSUIKeyType viewTypeKey;
    protected String defaultAction;
    private TVCollectionGridItemView.OnClickHandler onClickHandler;
    private boolean isClickable;
    private List<OnInternalEvent> receivers;
    int currentSelectedLanguageIndex = 0;
    private boolean isLoadingAdded = false;
    public static final int ITEM = 0;
    public static final int LOADING = 1;
    private boolean retryPageLoad = false;

    public AppCMSTVPageLinkAdapter(Context context,
                                   List adapterData,
                                   Component component,
                                   Layout parentLayout,
                                   AppCMSPresenter appCMSPresenter,
                                   Map<String, AppCMSUIKeyType> jsonValueKeyMap,
                                   String viewType,
                                   TVViewCreator tvViewCreator,
                                   Module moduleAPI,
                                   Settings settings) {
        this.context = context;
        this.adapterData = adapterData;
        this.component = component;
        this.parentLayout = parentLayout;
        this.appCMSPresenter = appCMSPresenter;
        this.jsonValueKeyMap = jsonValueKeyMap;
        this.defaultAction = getDefaultAction(context, component);
        this.viewType = viewType;
        this.tvViewCreator = tvViewCreator;
        this.module = moduleAPI;
        this.viewTypeKey = jsonValueKeyMap.get(viewType);
        if (this.viewTypeKey == null) {
            this.viewTypeKey = AppCMSUIKeyType.PAGE_EMPTY_KEY;
        }
        this.isClickable = true;
        this.receivers = new ArrayList<>();

        if (this.adapterData == null) {
            this.adapterData = new ArrayList<>();
        }
        this.settings = settings;
    }

    private String getDefaultAction(Context context, Component component) {
        if (null != component.getItemClickAction()) {
            return component.getItemClickAction();
        }
        return context.getString(R.string.app_cms_action_videopage_key);
    }


    public Component getComponentByKey(String key,List<Component> componentList){
        Component retComp = null;
        for(Component component: componentList) {
            if (key.equalsIgnoreCase(component.getKey())) {
                retComp= component;
            }
        }
        return retComp;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder = null;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        Component childComp;
        if (viewType == ITEM) {
            childComp = getComponentByKey(context.getString(R.string.pageLinkTableGridView),component.getComponents());
        }else{
            childComp = getComponentByKey(context.getString(R.string.pageLinkHeaderTitle),component.getComponents());
        }
        AppCMSUIKeyType componentKey = jsonValueKeyMap.get(component.getKey());
        component.getKey().equalsIgnoreCase(PAGE_LINK_HEADER_TITLE.toString());
            TVCollectionGridItemView collectionGridItemView;
            FrameLayout parentLayout = new FrameLayout(context);
            FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            parentLayout.setLayoutParams(params);

            TVViewCreator.ComponentViewResult componentViewResult =
                    tvViewCreator.getComponentViewResult();
            collectionGridItemView = new TVCollectionGridItemView(
                    context,
                    this.parentLayout,
                    false,
                    childComp,
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    Utils.getFocusColor(context, appCMSPresenter));

            List<OnInternalEvent> onInternalEvents = new ArrayList<>();
            if(childComp != null) {
                    if (null != childComp) {
                        tvViewCreator.createComponentView(context,
                                childComp,
                                this.parentLayout,
                                module,
                                null,
                                childComp.getSettings(),
                                jsonValueKeyMap,
                                appCMSPresenter,
                                false,
                                this.viewType,
                                false);

                        if (componentViewResult.onInternalEvent != null) {
                            onInternalEvents.add(componentViewResult.onInternalEvent);
                        }

                        View componentView = componentViewResult.componentView;
                        if (componentView != null) {
                            TVCollectionGridItemView.ItemContainer itemContainer =
                                    new TVCollectionGridItemView.ItemContainer.Builder()
                                            .childView(componentView)
                                            .component(childComp)
                                            .build();
                            collectionGridItemView.addChild(itemContainer);
                            //collectionGridItemView.setComponentHasView(i, true);
                            collectionGridItemView.setViewMarginsFromComponent(childComp,
                                    componentView,
                                    collectionGridItemView.getLayout(),
                                    collectionGridItemView.getChildrenContainer(),
                                    jsonValueKeyMap,
                                    false,
                                    false,
                                    this.viewType);
                        } else {
                           // collectionGridItemView.setComponentHasView(i, false);
                        }
                    }
              //  }
            }
            viewHolder = new ViewHolder(collectionGridItemView);

        return viewHolder;
    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final ViewHolder viewHolder = (ViewHolder) holder;

        switch (getItemViewType(position)) {
            case ITEM:
                TVCollectionGridItemView tvCollectionGridItemView = viewHolder.componentView;
                RecyclerView recyclerView = (RecyclerView) tvCollectionGridItemView.getChild(0);

                List<ContentDatum> contentDatumList = new ArrayList();
                for(CategoryPages categoryPages : adapterData.get(position).getPages()){
                    ContentDatum contentDatum1 = new ContentDatum();
                    Gist gist1 = new Gist();
                    gist1.setId(categoryPages.getId());
                    gist1.setTitle(categoryPages.getTitle());
                    gist1.setPermalink(categoryPages.getPath().get(0));
                    contentDatum1.setGist(gist1);
                    contentDatumList.add(contentDatum1);
                }

                AppCMSTVTrayAdapter pageLinkGridAdapter = new AppCMSTVTrayAdapter(context,
                        contentDatumList,
                        component.getComponents().get(1),
                        component.getComponents().get(1).getLayout(),
                        appCMSPresenter,
                        jsonValueKeyMap,
                        this.viewType,
                        tvViewCreator,
                        module,
                        settings);
                recyclerView.setAdapter(pageLinkGridAdapter);
                break;
            case LOADING:
                if (0 <= position && adapterData != null && position < adapterData.size()) {
                    bindView(viewHolder.componentView, adapterData.get(position), position, currentSelectedLanguageIndex);
                }
                break;

        }
    }

    public boolean isClickable() {
        return isClickable;
    }

    public void setClickable(boolean clickable) {
        isClickable = clickable;
    }

    private String getHlsUrl(ContentDatum data) {
        if (data.getStreamingInfo() != null &&
                data.getStreamingInfo().getVideoAssets() != null &&
                data.getStreamingInfo().getVideoAssets().getHls() != null) {
            return data.getStreamingInfo().getVideoAssets().getHls();
        }
        return null;
    }

    void bindView(TVCollectionGridItemView itemView,
                  final ContentDatum data, int position,int currentSelectedLanguageIndex) throws IllegalArgumentException {

        for (int i = 0; i < itemView.getNumberOfChildren(); i++) {
            itemView.bindChild(itemView.getContext(),
                    itemView.getChild(i),
                    data,
                    jsonValueKeyMap,
                    onClickHandler,
                    viewTypeKey,
                    position,
                    module,
                    currentSelectedLanguageIndex,
                    settings);
        }

    }


    @Override
    public int getItemCount() {
        return adapterData != null && adapterData.size() > 0 ? adapterData.size() : 1;
    }


    @Override
    public int getItemViewType(int position) {
         if(((ContentDatum)adapterData.get(position)).getPages() != null && ((ContentDatum)adapterData.get(position)).getPages().size() > 0){
             return ITEM;
         }else{
             return LOADING;
         }
    }

    @Override
    public void addReceiver(OnInternalEvent e) {
        receivers.add(e);
    }

    @Override
    public void sendEvent(InternalEvent<?> event) {
        for (OnInternalEvent internalEvent : receivers) {
            internalEvent.receiveEvent(event);
        }
    }

    @Override
    public void receiveEvent(InternalEvent<?> event) {
        adapterData.clear();
        notifyDataSetChanged();
    }

    @Override
    public void cancel(boolean cancel) {

    }

    @Override
    public void setModuleId(String moduleId) {

    }

    @Override
    public String getModuleId() {
        return null;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TVCollectionGridItemView componentView;

        public ViewHolder(View itemView) {
            super(itemView);
            if (itemView instanceof TVCollectionGridItemView) {
                this.componentView = (TVCollectionGridItemView) itemView;
            }
        }
    }

    protected class LoadingVH extends RecyclerView.ViewHolder implements View.OnClickListener{
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

                    //showRetry(false, null);
                    //mCallback.retryPageLoad();


                    break;
            }
        }
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
}
