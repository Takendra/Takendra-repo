package com.viewlift.tv.views.fragment;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import androidx.annotation.Nullable;
import androidx.leanback.widget.ArrayObjectAdapter;
import androidx.leanback.widget.ListRow;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.viewlift.AppCMSApplication;
import com.viewlift.R;
import com.viewlift.models.data.appcms.api.ContentDatum;
import com.viewlift.models.data.appcms.api.Module;
import com.viewlift.models.data.appcms.ui.page.Component;
import com.viewlift.presenters.AppCMSPresenter;
import com.viewlift.tv.model.BrowseFragmentRowData;
import com.viewlift.tv.utility.Utils;
import com.viewlift.tv.views.activity.AppCmsHomeActivity;
import com.viewlift.tv.views.component.AppCMSTVViewComponent;
import com.viewlift.tv.views.component.DaggerAppCMSTVViewComponent;
import com.viewlift.tv.views.customviews.AppCMSTVTrayAdapter;
import com.viewlift.tv.views.customviews.CustomHeaderItem;
import com.viewlift.tv.views.customviews.TVModuleView;
import com.viewlift.tv.views.customviews.TVPageView;
import com.viewlift.tv.views.module.AppCMSTVPageViewModule;
import com.viewlift.tv.views.presenter.CardPresenter;
import com.viewlift.views.binders.AppCMSBinder;
import com.viewlift.views.binders.AppCMSSwitchSeasonBinder;

import java.util.Arrays;
import java.util.List;

/**
 * Created by nitin.tyagi on 6/28/2017.
 */

public class AppCmsTVPageFragment extends BaseFragment {

    private FrameLayout pageContainer;
    private AppCMSBinder mAppCMSBinder;
    private AppCMSPresenter appCMSPresenter;
    private AppCMSTVViewComponent appCmsViewComponent;
    private TVPageView tvPageView;
    public String mPageId;
    private View focusableView;
    private AppCmsNavigationFragment.OnNavigationVisibilityListener onNavigationVisibilityListener;
    private List<String> modulesToIgnoreList;

    public static AppCmsTVPageFragment newInstance(Context context, AppCMSBinder appCMSBinder) {
        AppCmsTVPageFragment appCmsTVPageFragment = new AppCmsTVPageFragment();
        Bundle bundle = new Bundle();
        bundle.putBinder("app_cms_binder", appCMSBinder);
        appCmsTVPageFragment.mPageId = appCMSBinder.getScreenName();
        appCmsTVPageFragment.setArguments(bundle);
        return appCmsTVPageFragment;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        Bundle bundle = getArguments();
        mAppCMSBinder = (AppCMSBinder) bundle.getBinder("app_cms_binder");

        appCMSPresenter = ((AppCMSApplication) getActivity().getApplication())
                .getAppCMSPresenterComponent()
                .appCMSPresenter();

        //clear the Adapter.
        if (null != appCmsViewComponent && null != appCmsViewComponent.tvviewCreator()
                && null != appCmsViewComponent.tvviewCreator().mRowsAdapter) {
            appCmsViewComponent.tvviewCreator().mRowsAdapter.clear();
        }

        if (appCmsViewComponent == null && mAppCMSBinder != null) {
            appCmsViewComponent = buildAppCMSViewComponent();
        }


        if (appCmsViewComponent != null) {
            tvPageView = appCmsViewComponent.appCMSTVPageView();
        } else {
            tvPageView = null;
        }

        if (tvPageView != null) {
            if (tvPageView.getParent() != null) {
                ((ViewGroup) tvPageView.getParent()).removeAllViews();
            }
        }
        if (container != null) {
            container.removeAllViews();
        }

        if (null != tvPageView && (tvPageView.getChildrenContainer()).findViewById(R.id.appcms_browsefragment) != null) {
            if (getChildFragmentManager().findFragmentByTag(mAppCMSBinder.getScreenName()) == null) {
                AppCmsBrowseFragment browseFragment = AppCmsBrowseFragment. newInstance(getActivity());
                browseFragment.setPageView(tvPageView);
                browseFragment.setmRowsAdapter(appCmsViewComponent.tvviewCreator().mRowsAdapter);
                browseFragment.setScreenName(mAppCMSBinder.getScreenName());
                getChildFragmentManager().beginTransaction().replace(R.id.appcms_browsefragment, browseFragment, mAppCMSBinder.getScreenName()).commitAllowingStateLoss();
                Log.d("TAG","TESTS .. ScreenName = "+mAppCMSBinder.getScreenName());
            } else {
                refreshBrowseFragment();
            }
        }
        return tvPageView;
    }


    public void refreshPage(){
        if(null != appCmsViewComponent){
            if(appCMSPresenter.getAppCMSMain().isMonetizationModelEnabled()){
                modulesToIgnoreList = Arrays.asList(getResources().getStringArray(R.array.app_cms_deprecate_modules_ignore_tv));
            }else{
                modulesToIgnoreList = Arrays.asList(getResources().getStringArray(R.array.app_cms_modules_to_ignore_tv));
            }
            appCmsViewComponent.tvviewCreator().refreshPageView(tvPageView,
                    mAppCMSBinder.getAppCMSPageUI(),
                    mAppCMSBinder.getAppCMSPageAPI(),
                    mAppCMSBinder.getJsonValueKeyMap(),
                    appCMSPresenter,
                    modulesToIgnoreList,
                    mAppCMSBinder.getContentDatum());
        }
    }

    @Override
    public void onResume() {
        super.onResume();
//        requestFocus();
        if(null != mAppCMSBinder && !getString(R.string.language_key).equalsIgnoreCase(mAppCMSBinder.getPageName())){
        try {
            if (focusableView != null) {
                focusableView.requestFocus();
            } else {
                requestFocus();
            }
            AppCmsHomeActivity activity = (AppCmsHomeActivity) getActivity();
            if(Utils.isPlayerSelected){
                appCMSPresenter.sendBroadcastToHandleMiniPlayer(false);
            }else{
                appCMSPresenter.sendBroadcastToHandleMiniPlayer(activity.isMiniPlayerVisibleToPage());
            }

        } catch (Exception e) {
        }
    }

        if (null != appCMSPresenter) {
            appCMSPresenter.sendStopLoadingPageAction(false, null);
            if (appCMSPresenter.isUserLoggedIn() && mAppCMSBinder.getPageName() != null
                    && mAppCMSBinder.getPageName().equalsIgnoreCase(getString(R.string.app_cms_watchlist_navigation_title))) {
                updateAdapterData(mAppCMSBinder);
            }
        }

        if (onNavigationVisibilityListener != null) {
            onNavigationVisibilityListener.enableNavigation();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        Utils.isPlayerSelected = false;
        if (getActivity() != null
                && getActivity().getCurrentFocus() != null
                && getActivity().getCurrentFocus().getId() != R.id.nav_item_layout) {
            focusableView = getActivity().getCurrentFocus();
        }
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        //super.onSaveInstanceState(outState);
    }

    public void requestFocus() {
        new Handler().postDelayed(() -> {
            if (null != tvPageView) {
                ViewGroup ChildContaineer = tvPageView.getChildrenContainer();
                int childcount = 0;
                if (null != ChildContaineer) {
                    childcount = ChildContaineer.getChildCount();
                }
                for (int i = 0; i < childcount; i++) {
                    if (ChildContaineer.getChildAt(0) instanceof TVModuleView) {
                        TVModuleView tvModuleView = (TVModuleView) ChildContaineer.getChildAt(0);
                        ViewGroup moduleChildContaineer = tvModuleView.getChildrenContainer();
                        int moduleChild = moduleChildContaineer.getChildCount();
                        for (int j = 0; j < moduleChild; j++) {
                            View view = moduleChildContaineer.getChildAt(j);
                            if (null != view) {
                                System.out.println("View isFocusable == " + view.isFocusable() + "TAG =  = == " + (view.getTag() != null ? view.getTag().toString() : null));
                                if (null != view.getTag() &&
                                        view.getTag().toString().equalsIgnoreCase(appCMSPresenter.getCurrentContext().getString(R.string.video_image_key))) {
                                    ((FrameLayout) view).getChildAt(0).requestFocus();
                                    break;
                                } else if (view.isFocusable()) {
                                    view.requestFocus();
                                    /* in case of a video page we have included a scroll view to
                                    * enable scrolling when related video tray items were getting
                                    * cut-off from the bottom. BrowseFragment, by default, gets
                                    * the focus, to avoid page being scrolled to the bottom, this
                                    * following statement is added.*/
                                    if (tvPageView.getScrollView() != null){
                                        tvPageView.getScrollView().scrollTo(0,0);
                                    }
                                    break;
                                } else {
                                    view.clearFocus();
                                }
                            }
                        }
                    }
                }
            }
        }, 10);
    }



    public void refreshBrowseFragment(){
        try {
            if (null != appCmsViewComponent.tvviewCreator() && null != appCmsViewComponent.tvviewCreator().mRowsAdapter) {
                int totalNumber = 0;
                for (int i = 0; i < appCmsViewComponent.tvviewCreator().mRowsAdapter.size(); i++) {
                    ListRow listRow = (ListRow) appCmsViewComponent.tvviewCreator().mRowsAdapter.get(i);
                    CustomHeaderItem customHeaderItem = (CustomHeaderItem) listRow.getHeaderItem();
                    for (Module module : mAppCMSBinder.getAppCMSPageAPI().getModules()) {
                        if (module.getId().equalsIgnoreCase(customHeaderItem.getmModuleId())) {
                            List<ContentDatum> contentData = module.getContentData();
                            for (int i1 = 0; i1 < contentData.size(); i1++) {
                                ContentDatum contentDatum = contentData.get(i1);
                                for (int j = 0; j < listRow.getAdapter().size(); j++) {
                                    if (((BrowseFragmentRowData) listRow.getAdapter().get(j)).contentData.getGist().getId() != null
                                            && ((BrowseFragmentRowData) listRow.getAdapter().get(j)).contentData.getGist().getId().equalsIgnoreCase(contentDatum.getGist().getId())) {
                                        BrowseFragmentRowData rowData = (BrowseFragmentRowData) listRow.getAdapter().get(j);
                                        rowData.contentData = module.getContentData().get(i1);
                                        totalNumber++;
                                        break;
                                    }
                                }

                            }
                        }
                    }
                }
                appCmsViewComponent.tvviewCreator().mRowsAdapter.notifyMe();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Used to update the episodes' progress bar on ShowDetail page.
     */
    public void refreshShowDetailEpisodes() {
        try {
            if (null != appCmsViewComponent.tvviewCreator() && null != appCmsViewComponent.tvviewCreator().mRowsAdapter
                 && appCmsViewComponent.tvviewCreator().mRowsAdapter.size() > 0){
                appCmsViewComponent.tvviewCreator().mRowsAdapter.notifyMe();
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    @Override
    public void onDestroyView() {
        if (tvPageView != null)
            tvPageView.setBackground(null);

        if(getActivity() != null && ((AppCmsHomeActivity) getActivity()).getCustomVideoVideoPlayerView1() != null){
            ((AppCmsHomeActivity) getActivity()).removeHandlerCallBacks();
        }
        super.onDestroyView();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (getActivity() instanceof AppCmsNavigationFragment.OnNavigationVisibilityListener) {
            onNavigationVisibilityListener = (AppCmsNavigationFragment.OnNavigationVisibilityListener) getActivity();
        }
    }

    public AppCMSTVViewComponent buildAppCMSViewComponent() {
        return DaggerAppCMSTVViewComponent.builder()
                .appCMSTVPageViewModule(new AppCMSTVPageViewModule(getActivity(),
                        mAppCMSBinder.getAppCMSPageUI(),
                        mAppCMSBinder.getAppCMSPageAPI(),
                        mAppCMSBinder.getJsonValueKeyMap(),
                        appCMSPresenter,
                        mAppCMSBinder.getPageId()
                ))
                .build();
    }

    public void updateBinder(AppCMSBinder appCmsBinder) {
        mAppCMSBinder = appCmsBinder;
    }

    public void updateAdapterData(AppCMSBinder appCmsBinder) {
        try {
            TVModuleView tvModuleView = (TVModuleView) tvPageView.getChildrenContainer().getChildAt(0);
            int childCount = tvModuleView.getChildrenContainer().getChildCount();
            for (int i = 0; i < childCount; i++) {
                if (null != tvModuleView.getChildrenContainer().getChildAt(i)
                        && tvModuleView.getChildrenContainer().getChildAt(i) instanceof RecyclerView) {
                    RecyclerView recyclerView = (RecyclerView) tvModuleView.getChildrenContainer().getChildAt(i);
                    ((AppCMSTVTrayAdapter) recyclerView.getAdapter()).setContentData(appCmsBinder.getAppCMSPageAPI().getModules().get(0).getContentData());
                    if(appCmsBinder.getAppCMSPageAPI().getModules().get(0).getContentData().size() == 0){
                        View view = tvModuleView.findViewById(R.id.appcms_removeall);
                        if(null != view){
                            view.setVisibility(View.GONE);
                        }
                    }
                }
            }
        } catch (Exception e) {

        }
    }

    public void updateSeasonData(AppCMSSwitchSeasonBinder appCMSSwitchSeasonBinder) {

        try {
            if (null != appCmsViewComponent.tvviewCreator()
                    && null != appCmsViewComponent.tvviewCreator().mRowsAdapter
                    && appCMSSwitchSeasonBinder.getSelectedSeasonIndex() != SwitchSeasonsDialogFragment.getSelectedSeasonIndex()) {

                ListRow listRow = (ListRow) appCmsViewComponent.tvviewCreator().mRowsAdapter.get(0);
                ArrayObjectAdapter  arrayObjectAdapter = (ArrayObjectAdapter) listRow.getAdapter();
                CardPresenter trayCardPresenter = (CardPresenter) arrayObjectAdapter.getPresenter(0);
                List<Component> uiComponentList = ((BrowseFragmentRowData) arrayObjectAdapter.get(0)).uiComponentList;
                String blockName = ((BrowseFragmentRowData) arrayObjectAdapter.get(0)).blockName;
                String action = ((BrowseFragmentRowData) arrayObjectAdapter.get(0)).action;
                arrayObjectAdapter.clear();

                ArrayObjectAdapter traylistRowAdapter = new ArrayObjectAdapter(trayCardPresenter);
                List<ContentDatum> episodes = appCMSSwitchSeasonBinder.getSeasonList().get(appCMSSwitchSeasonBinder.getSelectedSeasonIndex()).getEpisodes();
                for (int i = 0; i < episodes.size(); i++) {
                    List<String> relatedVids = Utils.getRelatedVideosInShow(
                            appCMSSwitchSeasonBinder.getSeasonList(),
                            appCMSSwitchSeasonBinder.getSelectedSeasonIndex(),
                            i - 1);
                    ContentDatum contentDatum = episodes.get(i);
                    contentDatum.setSeason(appCMSSwitchSeasonBinder.getSeasonList());
                    BrowseFragmentRowData rowData = new BrowseFragmentRowData();
                    rowData.contentData = contentDatum;
                    rowData.relatedVideoIds = relatedVids;
                    rowData.uiComponentList = uiComponentList;
                    rowData.action = action;
                    rowData.blockName = blockName;
                    rowData.rowNumber = appCMSSwitchSeasonBinder.getTrayIndex();
                    traylistRowAdapter.add(rowData);
                }
                CustomHeaderItem customHeaderItem = (CustomHeaderItem) listRow.getHeaderItem();
                CustomHeaderItem header = new CustomHeaderItem(appCMSPresenter.getCurrentActivity(), 0,
                        appCMSPresenter.getLanguageResourcesFile().getUIresource(getString(R.string.season))
                                +" " + (appCMSSwitchSeasonBinder.getSelectedSeasonIndex() + 1));

                header.setFontFamily(customHeaderItem.getFontFamily());
                header.setFontSize(customHeaderItem.getFontSize());
                header.setFontWeight(customHeaderItem.getFontWeight());
                header.setmBackGroundColor(customHeaderItem.getmBackGroundColor());
                header.setmIsCarousal(customHeaderItem.ismIsCarousal());
                header.setmIsLivePlayer(customHeaderItem.ismIsLivePlayer());
                header.setmListRowHeight(customHeaderItem.getmListRowHeight());
                header.setmListRowLeftMargin(customHeaderItem.getmListRowLeftMargin());
                header.setmListRowRightMargin(customHeaderItem.getmListRowRightMargin());
                header.setmListRowWidth(customHeaderItem.getmListRowWidth());
                header.setmModuleId(customHeaderItem.getmModuleId());
                header.setContentDescription(customHeaderItem.getContentDescription());
                header.setDescription(customHeaderItem.getDescription());
                appCmsViewComponent.tvviewCreator().mRowsAdapter.remove(appCmsViewComponent.tvviewCreator().mRowsAdapter.get(0));
                appCmsViewComponent.tvviewCreator().mRowsAdapter.add(new ListRow(header, traylistRowAdapter));
                appCmsViewComponent.tvviewCreator().mRowsAdapter.notifyArrayItemRangeChanged(0, 1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean isSubNavigationVisible() {
        return false;
    }

    @Override
    public boolean isSubNavExist() {
        return false;
    }


    @Override
    public void showSubNavigation(boolean shouldShow) {

    }

    @Override
    public void setSubnavExistence(boolean isExist) {

    }
}
