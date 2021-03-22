package com.viewlift.tv.views.fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.annotation.Nullable;
import androidx.leanback.widget.ListRow;
import androidx.leanback.widget.ObjectAdapter;
import androidx.leanback.widget.OnItemViewClickedListener;
import androidx.leanback.widget.OnItemViewSelectedListener;
import androidx.leanback.widget.Presenter;
import androidx.leanback.widget.Row;
import androidx.leanback.widget.RowPresenter;

import com.viewlift.AppCMSApplication;
import com.viewlift.R;
import com.viewlift.models.data.appcms.api.AppCMSTransactionDataValue;
import com.viewlift.models.data.appcms.api.ClosedCaptions;
import com.viewlift.models.data.appcms.api.ContentDatum;
import com.viewlift.models.data.appcms.api.Module;
import com.viewlift.models.data.appcms.ui.android.MetaPage;
import com.viewlift.models.data.appcms.ui.android.NavigationUser;
import com.viewlift.presenters.AppCMSPresenter;
import com.viewlift.tv.model.BrowseFragmentRowData;
import com.viewlift.tv.model.SeeAllCard;
import com.viewlift.tv.utility.Utils;
import com.viewlift.tv.views.activity.AppCmsHomeActivity;
import com.viewlift.tv.views.customviews.AppCMSArrayObjectAdaptor;
import com.viewlift.tv.views.customviews.CustomTVVideoPlayerView;
import com.viewlift.tv.views.customviews.TVExpandedViewModule;
import com.viewlift.tv.views.customviews.TVPageView;

import rx.functions.Action1;

/**
 * Created by nitin.tyagi on 6/29/2017.
 */

public class AppCmsBrowseFragment extends BaseBrowseFragment {
    private AppCMSArrayObjectAdaptor mRowsAdapter;
    private final String TAG = AppCmsBrowseFragment.class.getName();
    private View view;
    private TVPageView pageView;
    private String screenName;
    private boolean typeData = false;
    int adapterSize = 0;
    private boolean isEnableMiniPlayer;

    public static AppCmsBrowseFragment newInstance(Context context){
        AppCmsBrowseFragment appCmsBrowseFragment = new AppCmsBrowseFragment();
        return appCmsBrowseFragment;
    }

    public void setPageView(TVPageView tvPageView) {
        pageView = tvPageView;
    }

    public void setmRowsAdapter(AppCMSArrayObjectAdaptor rowsAdapter) {
        this.mRowsAdapter = rowsAdapter;
        setAdapter(mRowsAdapter);
        // todo check anas azeem
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = super.onCreateView(inflater, container, savedInstanceState);

        if (appCMSPresenter.isNewsTemplate()) {
            Utils.setBrowseFragmentViewParameters(view,
                    (int) getResources().getDimension(R.dimen.browse_fragment_news_template_margin_left),
                    (int) getResources().getDimension(R.dimen.browse_fragment_margin_top_for_tray));
        } else {
            Utils.setBrowseFragmentViewParameters(view,
                    (int) getResources().getDimension(R.dimen.browse_fragment_margin_left),
                    (int) getResources().getDimension(R.dimen.browse_fragment_margin_top));
        }

        if(appCMSPresenter.getAppCMSMain().getFeatures() != null){
            isEnableMiniPlayer = appCMSPresenter.getAppCMSMain().getFeatures().isEnableMiniPlayer();
        }
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        AppCmsHomeActivity activity = (AppCmsHomeActivity) getActivity();
        new Handler().postDelayed(() -> {
            if(null != customVideoVideoPlayerView){
                if (activity.isNavigationVisible() /*|| activity.isSubNavigationVisible()*/) {
                } else {
                    if(activity.isActive()) {
                        customVideoVideoPlayerView.resumePlayer();
                    }
                }
            }
        },50);

        /* if the current screen in a video page, then the layout containing the BrowseFragment
         *is given a height based on the row height we get from Page UI json, instead of match parent*/
        if (screenName != null && (screenName.toLowerCase().contains("video page") || screenName.toLowerCase().contains("show page") || screenName.toLowerCase().contains("bundle page"))) {

            FrameLayout browseFrame = getActivity().findViewById(R.id.appcms_browsefragment);
            ViewGroup.LayoutParams layoutParams = browseFrame.getLayoutParams();

            int rowHeight = 380;
            try {
                if(mRowsAdapter != null && mRowsAdapter.size() > 0){
                    for(int i=0;i<mRowsAdapter.size();i++){
                        ObjectAdapter adapter = ((ListRow) mRowsAdapter.get(i)).getAdapter();
                        if(adapter != null && adapter.size() > 0){
                            BrowseFragmentRowData browseFragmentRowData = (BrowseFragmentRowData) adapter.get(0);
                            int tempHeight = Integer.parseInt(browseFragmentRowData.uiComponentList.get(0).getLayout().getTv().getHeight());
                            if (tempHeight > rowHeight) {
                                rowHeight = tempHeight;
                            }
                        }
                    }
                }
            } catch (Exception e) {
                rowHeight = 380;
            }

            /*ObjectAdapter adapter = ((ListRow) mRowsAdapter.get(0)).getAdapter();
            int rowHeight = 0;
            try {
                for (int i = 0; i < adapter.size(); i++) {
                    BrowseFragmentRowData browseFragmentRowData = (BrowseFragmentRowData) adapter.get(i);
                    int tempHeight = Integer.parseInt(browseFragmentRowData.uiComponentList.get(0).getLayout().getTv().getHeight());
                    if (tempHeight > rowHeight) {
                        rowHeight = tempHeight;
                    }
                }
            } catch (Exception e) {
                rowHeight = 380;
            }*/


//            int rowHeight = Integer.parseInt(((BrowseFragmentRowData) ((ListRow) mRowsAdapter.get(0)).getAdapter().get(0)).uiComponentList.get(0).getLayout().getTv().getHeight());
            // this 160 value includes header and the empty space other than the actual row item
            layoutParams.height = 160 + rowHeight;
            browseFrame.setLayoutParams(layoutParams);

        }
    }

    public void requestFocus(boolean requestFocus) {
        if (null != view) {
            if (requestFocus)
                view.requestFocus();
            else
                view.clearFocus();
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //Log.d(TAG , "appcmsBrowseFragment onActivityCreated");
        if (null != mRowsAdapter) {
            setAdapter(mRowsAdapter);
        }

        setOnItemViewClickedListener(new ItemViewClickedListener());
        setOnItemViewSelectedListener(new ItemViewSelectedListener());

    }


    AppCMSPresenter appCMSPresenter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        appCMSPresenter = ((AppCMSApplication) getActivity().getApplication())
                .getAppCMSPresenterComponent()
                .appCMSPresenter();
    }

    ContentDatum data = null;
    BrowseFragmentRowData rowData = null;
    long clickedTime;

    public void pushedPlayKey() {
        if (null != rowData
                && typeData
                && null != rowData.contentData
                && null != rowData.contentData.getGist()
                && !rowData.isPlayerComponent
                && !rowData.blockName.equalsIgnoreCase("weatherTray01")) {
            Utils.pageLoading(true, getActivity());
            ContentDatum contentDatum = rowData.contentData;
            String filmId = contentDatum.getGist().getId();
            String permaLink = contentDatum.getGist().getPermalink();
            String title = contentDatum.getGist().getTitle();

            long diff = System.currentTimeMillis() - clickedTime;
            if (diff > 2000) {
                clickedTime = System.currentTimeMillis();
                if (null != contentDatum.getGist().getContentType() &&
                        contentDatum.getGist().getContentType().equalsIgnoreCase("SERIES")) {
                    appCMSPresenter.launchTVButtonSelectedAction(
                            contentDatum.getGist().getPermalink(),
                            "showDetailPage",
                            contentDatum.getGist().getTitle(),
                            null,
                            contentDatum,
                            false,
                            -1,
                            null,
                            null);
                }else if (rowData.contentData.getGist().getContentType() != null
                        && rowData.contentData.getGist().getContentType().equalsIgnoreCase("BUNDLE")) {
                    appCMSPresenter.launchTVButtonSelectedAction(
                            contentDatum.getGist().getPermalink(),
                            "bundleDetailPage",
                            contentDatum.getGist().getTitle(),
                            null,
                            contentDatum,
                            false,
                            -1,
                            null,
                            null);
                } else {
                    /*if (contentDatum.getPricing() != null) {
                        if ("TVOD".equalsIgnoreCase(contentDatum.getPricing().getType())) {
                            appCMSPresenter.getTransactionData(contentDatum.getGist().getId(), maps -> {
                                if (maps != null
                                        && maps.get(0) != null
                                        && maps.get(0).get(contentDatum.getGist().getId()) != null) {
                                    if(appCMSPresenter.isScheduleVideoPlayable(contentDatum.getGist().getScheduleStartDate(),
                                            contentDatum.getGist().getScheduleEndDate(), null)) {
                                        AppCMSTransactionDataValue appCMSTransactionDataValue = maps.get(0).get(contentDatum.getGist().getId());
                                        if (appCMSTransactionDataValue.getTransactionEndDate() != 0) {
                                            ClearDialogFragment clearDialogFragment = Utils.getClearDialogFragment(
                                                    getContext(),
                                                    appCMSPresenter,
                                                    getContext().getResources().getDimensionPixelSize(R.dimen.text_clear_dialog_width),
                                                    getContext().getResources().getDimensionPixelSize(R.dimen.text_clear_dialog_height),
                                                    null,
                                                    appCMSPresenter.getLocalisedStrings().getRentTimeDialogMsg(String.valueOf(appCMSTransactionDataValue.getRentalPeriod())),
                                                    appCMSPresenter.getLocalisedStrings().getPlayText(),
                                                    appCMSPresenter.getLocalisedStrings().getCancelCta(),
                                                   null, 13f
                                            );

                                            clearDialogFragment.setOnPositiveButtonClicked(s -> {
                                                appCMSPresenter.launchTVVideoPlayer(contentDatum,
                                                        0,
                                                        rowData.relatedVideoIds,
                                                        contentDatum.getGist().getWatchedTime(),
                                                        null);
                                            });
                                            appCMSPresenter.showLoadingDialog(false);
                                        } else {
                                            appCMSPresenter.launchTVVideoPlayer(contentDatum,
                                                    0,
                                                    rowData.relatedVideoIds,
                                                    contentDatum.getGist().getWatchedTime(),
                                                    null);
                                        }
                                    }
                                } else {
                                    Utils.getClearDialogFragment(
                                            getContext(),
                                            appCMSPresenter,
                                            getResources().getDimensionPixelSize(R.dimen.text_clear_dialog_width),
                                            getResources().getDimensionPixelSize(R.dimen.text_clear_dialog_height),
                                            null,
                                            appCMSPresenter.getLocalisedStrings().getCannotPurchaseItemMsg(appCMSPresenter.getAppCMSMain().getDomainName()),
                                            appCMSPresenter.getLocalisedStrings().getBackCta(),
                                            null,
                                            null,  10f
                                    );
                                    appCMSPresenter.showLoadingDialog(false);
                                }
                            }, contentDatum.getGist().getContentType());
                        } else if ("PPV".equalsIgnoreCase(contentDatum.getPricing().getType())) {
                            appCMSPresenter.getTransactionData(contentDatum.getGist().getId(), maps -> {
                                if (maps != null
                                        && maps.get(0) != null
                                        && maps.get(0).get(contentDatum.getGist().getId()) != null) {
                                    AppCMSTransactionDataValue appCMSTransactionDataValue = maps.get(0).get(contentDatum.getGist().getId());

                                    if(appCMSPresenter.isScheduleVideoPlayable(contentDatum.getGist().getScheduleStartDate(),
                                            contentDatum.getGist().getScheduleEndDate(), null)) {
                                        appCMSPresenter.launchTVVideoPlayer(contentDatum,
                                                0,
                                                rowData.relatedVideoIds,
                                                contentDatum.getGist().getWatchedTime(),
                                                null);
                                    }
                                } else {
                                    Utils.getClearDialogFragment(
                                            getContext(),
                                            appCMSPresenter,
                                            getResources().getDimensionPixelSize(R.dimen.text_clear_dialog_width),
                                            getResources().getDimensionPixelSize(R.dimen.text_clear_dialog_height),
                                            null,
                                            appCMSPresenter.getLocalisedStrings().getCannotPurchaseItemMsg(appCMSPresenter.getAppCMSMain().getDomainName()),
                                            appCMSPresenter.getLocalisedStrings().getBackCta(),
                                            null,
                                            null, 10f
                                    );
                                    appCMSPresenter.showLoadingDialog(false);
                                }
                            }, contentDatum.getGist().getContentType());
                        } else if ("SVOD+PPV".equalsIgnoreCase(contentDatum.getPricing().getType())
                                || "SVOD+TVOD".equalsIgnoreCase(contentDatum.getPricing().getType())) {
                            {
                                appCMSPresenter.showLoadingDialog(true);

                                if(appCMSPresenter.isUserLoggedIn()
                                        && appCMSPresenter.isUserSubscribed()){
                                    if(appCMSPresenter.isScheduleVideoPlayable(contentDatum.getGist().getScheduleStartDate(),
                                            contentDatum.getGist().getScheduleEndDate(), null)){
                                        {
                                           *//* if (contentDatum != null &&
                                                    contentDatum.getStreamingInfo() != null &&
                                                    contentDatum.getStreamingInfo().getVideoAssets() != null)*//* {
                                            appCMSPresenter.launchTVVideoPlayer(contentDatum,
                                                    0,
                                                    rowData.relatedVideoIds,
                                                    contentDatum.getGist().getWatchedTime(),
                                                    null);
                                        } *//*else {
                                                appCMSPresenter.openTVErrorDialog(
                                                        appCMSPresenter.getLanguageResourcesFile().getStringValue(getString(R.string.api_error_message,getString(R.string.app_name))),
                                                        appCMSPresenter.getLanguageResourcesFile().getUIresource(getString(R.string.app_connectivity_dialog_title)), false);
                                            }*//*
                                            // appCMSPresenter.showLoadingDialog(false);
                                        }
                                    }
                                }else{
                                    if (!appCMSPresenter.isUserLoggedIn()) {
                                        String cancelCTA = appCMSPresenter.getLocalisedStrings().getCancelText();
                                        String dialogMessage = appCMSPresenter.getLocalisedStrings().getPremiumContentGuestUserDialogMessageText();
                                        String dialogTitle = appCMSPresenter.getLocalisedStrings().getLoginRequiredText();
                                        String positiveButtonText = appCMSPresenter.getLocalisedStrings().getSignInText();

                                        ClearDialogFragment newFragment = Utils.getClearDialogFragment(
                                                getContext(),
                                                appCMSPresenter,
                                                getResources().getDimensionPixelSize(R.dimen.text_clear_dialog_width),
                                                getResources().getDimensionPixelSize(R.dimen.text_add_to_watchlist_sign_in_dialog_height),
                                                dialogTitle,
                                                dialogMessage,
                                                positiveButtonText,
                                                cancelCTA,
                                                null, 14
                                        );

                                        newFragment.setOnPositiveButtonClicked(new Action1<String>() {
                                            @Override
                                            public void call(String s) {
                                                appCMSPresenter.showLoadingDialog(true);
                                                NavigationUser navigationUser = appCMSPresenter.getLoginNavigation();
                                                appCMSPresenter.navigateToTVPage(
                                                        navigationUser.getPageId(),
                                                        navigationUser.getTitle(),
                                                        navigationUser.getUrl(),
                                                        false,
                                                        Uri.EMPTY,
                                                        false,
                                                        false,
                                                        true, false, false, false);
                                            }
                                        });

                                        newFragment.setOnNegativeButtonClicked(new Action1<String>() {
                                            @Override
                                            public void call(String s) {
                                                newFragment.dismiss();
                                            }
                                        });

                                        newFragment.setOnBackKeyListener(new Action1<String>() {
                                            @Override
                                            public void call(String s) {
                                                newFragment.dismiss();
                                            }
                                        });
                                        appCMSPresenter.showLoadingDialog(false);

                                    } else if (!appCMSPresenter.isUserSubscribed()) {
                                        appCMSPresenter.getTransactionData(contentDatum.getGist().getId(), maps -> {
                                            appCMSPresenter.showLoadingDialog(false);
                                            if (maps != null
                                                    && maps.get(0) != null
                                                    && maps.get(0).get(contentDatum.getGist().getId()) != null) {

                                                if(appCMSPresenter.isScheduleVideoPlayable(contentDatum.getGist().getScheduleStartDate(),
                                                        contentDatum.getGist().getScheduleEndDate(), null)){
                                                    {
                                                        new Handler().postDelayed(new Runnable() {
                                                            @Override
                                                            public void run() {
                                                                *//*if (contentDatum != null &&
                                                                        contentDatum.getStreamingInfo() != null &&
                                                                        contentDatum.getStreamingInfo().getVideoAssets() != null)*//* {

                                                                    appCMSPresenter.launchTVVideoPlayer(contentDatum,
                                                                            0,
                                                                            rowData.relatedVideoIds,
                                                                            contentDatum.getGist().getWatchedTime(),
                                                                            null);

                                                                } *//*else {
                                                                    appCMSPresenter.openTVErrorDialog(
                                                                            appCMSPresenter.getLanguageResourcesFile().getStringValue(getString(R.string.api_error_message,getString(R.string.app_name))),
                                                                            appCMSPresenter.getLanguageResourcesFile().getUIresource(getString(R.string.app_connectivity_dialog_title)), false);
                                                                }*//*
                                                            }

                                                        }, 300);

                                                    }
                                                }
                                                appCMSPresenter.showLoadingDialog(false);
                                            } else {
                                                //TODO: show Subscription Dialog.
                                                String dialogMessage = appCMSPresenter.getLanguageResourcesFile().getUIresource(getResources().getString(R.string.unsubscribe_text));
                                                if (appCMSPresenter.getAppCMSAndroid() != null
                                                        && appCMSPresenter.getAppCMSAndroid().getSubscriptionFlowContent() != null
                                                        && appCMSPresenter.getAppCMSAndroid().getSubscriptionFlowContent().getOverlayMessage() != null) {
                                                    dialogMessage = appCMSPresenter.getAppCMSAndroid().getSubscriptionFlowContent().getOverlayMessage();
                                                }
                                                ClearDialogFragment newFragment = Utils.getClearDialogFragment(
                                                        getContext(),
                                                        appCMSPresenter,
                                                        getResources().getDimensionPixelSize(R.dimen.text_clear_dialog_width),
                                                        getResources().getDimensionPixelSize(R.dimen.text_add_to_watchlist_sign_in_dialog_height),
                                                        appCMSPresenter.getLanguageResourcesFile().getUIresource(getResources().getString(R.string.subscription_required)),
                                                        dialogMessage,
                                                        appCMSPresenter.getLocalisedStrings().getCancelCta(),
                                                        getResources().getString(R.string.blank_string),
                                                        null, 14
                                                );

                                                newFragment.setOnPositiveButtonClicked(new Action1<String>() {
                                                    @Override
                                                    public void call(String s) {
                                                        newFragment.dismiss();
                                                    }
                                                });

                                                newFragment.setOnBackKeyListener(new Action1<String>() {
                                                    @Override
                                                    public void call(String s) {
                                                        newFragment.dismiss();
                                                    }
                                                });
                                                appCMSPresenter.showLoadingDialog(false);
                                            }

                                        }, contentDatum.getGist().getContentType());

                                    }
                                }
                            }

                        }else {

                            if(!appCMSPresenter.isScheduleVideoPlayable(contentDatum.getGist().getScheduleStartDate(),
                                    contentDatum.getGist().getScheduleEndDate(), null)){
                                return;
                            }

                            appCMSPresenter.launchTVVideoPlayer(contentDatum,
                                    0,
                                    rowData.relatedVideoIds,
                                    contentDatum.getGist().getWatchedTime(),
                                    null);
                        }
                    }*//* else {

                        if(!appCMSPresenter.isScheduleVideoPlayable(contentDatum.getGist().getScheduleStartDate(),
                                contentDatum.getGist().getScheduleEndDate(), null)){
                            return;
                        }*/

                        appCMSPresenter.launchTVVideoPlayer(contentDatum,
                                0,
                                rowData.relatedVideoIds,
                                contentDatum.getGist().getWatchedTime(),
                                null);
                    //}
                }
            } else {
                appCMSPresenter.showLoadingDialog(false);
            }
        }
    }

    public boolean hasFocus() {
        return (null != view && view.hasFocus());
    }

    public void setScreenName(String screenName) {
        this.screenName = screenName;
    }

    private class ItemViewClickedListener implements OnItemViewClickedListener {
        @Override
        public void onItemClicked(Presenter.ViewHolder itemViewHolder, Object item,
                                  RowPresenter.ViewHolder rowViewHolder, Row row) {

            if(appCMSPresenter.isFullScreenVisible){
                return;
            }


            appCMSPresenter.isRecommendationTray=false;
            if("recommendation01".equalsIgnoreCase(rowData.blockName) ||
                    "newsRecommendation01".equalsIgnoreCase(rowData.blockName)){
                appCMSPresenter.isRecommendationTray=true;
            }

            if (null != item && item instanceof BrowseFragmentRowData) {
                BrowseFragmentRowData rowData = (BrowseFragmentRowData) item;
                if(customVideoVideoPlayerView != null && rowData.isPlayerComponent){
                    if(customVideoVideoPlayerView.isLoginButtonVisible()){
                        if(!appCMSPresenter.isUserLoggedIn()) {
                            customVideoVideoPlayerView.performLoginButtonClick();
                        }
                        else{
                            customVideoVideoPlayerView.showRestrictMessage(getString(R.string.reload_page_from_menu));
                            customVideoVideoPlayerView.toggleLoginButtonVisibility(false);
                        }
                        return;
                    }else if(customVideoVideoPlayerView.isTvodSubscribeButtonVisible()){
                        customVideoVideoPlayerView.performTvodPurchaseButtonClick();
                        return;
                    }
                    appCMSPresenter.setTVVideoPlayerView(null);
                    appCMSPresenter.setTVVideoPlayerView(customVideoVideoPlayerView);
                    appCMSPresenter.tvVideoPlayerView.getPlayerView().setUseController(true);
                    customVideoVideoPlayerView.hideControlsForLiveStream();
                    appCMSPresenter.showFullScreenTVPlayer();
                    appCMSPresenter.tvVideoPlayerView.getPlayerView().getPlayer().seekTo(appCMSPresenter.tvVideoPlayerView.getPlayer().getContentPosition() + 1000);
                    return;
                }else if("tray11".equalsIgnoreCase(rowData.blockName) ||
                        "newsRecommendation01".equalsIgnoreCase(rowData.blockName)){
                    if(pageView.findViewById(R.id.video_player_id) != null) {
                        pageView.findViewById(R.id.video_player_id).setFocusable(false);
                    }
                    appCMSPresenter.navigateToExpandedDetailPage(rowData.moduleTitle,rowData.contentDatumList,rowData.itemPosition);
                    return ;
                }else if(rowData.blockName.equalsIgnoreCase("weatherTray01")){
                    MetaPage weatherPage = appCMSPresenter.getWeatherPage();
                    if(weatherPage != null){

                        appCMSPresenter.navigateToTVPage(
                                weatherPage.getPageId(),
                                weatherPage.getPageName(),
                                weatherPage.getPageAPI(),
                                false,
                                Uri.EMPTY,
                                false,
                                false,
                                false,
                                false, false, false);
                    }

                    return;
                }

                Module moduleApi = rowData.moduleApi;
                if(moduleApi != null) moduleApi.setItemPosition(rowData.itemPosition);
                appCMSPresenter.setModuleApi(moduleApi);

                ContentDatum data = rowData.contentData;
                String action = rowData.action;

                if(appCMSPresenter.getAppCMSMain().getFeatures().isTrickPlay()){
                    action = getString(R.string.app_cms_action_watchvideo_key);
                }

                if (rowData.contentData.getGist() == null)
                    return;

                if (action.equalsIgnoreCase(getString(R.string.app_cms_action_watchvideo_key))) {
                    pushedPlayKey();
                } else {
                    if ((null != rowData.contentData.getGist().getContentType() &&
                            rowData.contentData.getGist().getContentType().equalsIgnoreCase("SERIES"))) {
                        action = "showDetailPage";
                    }
                    else if (rowData.contentData.getGist().getContentType() != null
                            && rowData.contentData.getGist().getContentType().equalsIgnoreCase("SEASON")) {
                        action = "showDetailPage";
                    }
                    else if (rowData.contentData.getGist().getContentType() != null
                            && rowData.contentData.getGist().getContentType().equalsIgnoreCase("SEASONS")) {
                        action = "showDetailPage";
                    }
                    else if (rowData.contentData.getGist().getContentType() != null
                            && rowData.contentData.getGist().getContentType().equalsIgnoreCase("BUNDLE")
                            && rowData.contentData.getGist().getMediaType()!=null
                            && rowData.contentData.getGist().getMediaType().equalsIgnoreCase("EPISODIC")) {
                        action = "lectureDetailPage";
                    }

                    else if (rowData.contentData.getGist().getContentType() != null
                            && rowData.contentData.getGist().getContentType().equalsIgnoreCase("BUNDLE")) {
                        action = "bundleDetailPage";
                    }

                    String permalink = data.getGist().getPermalink();
                    String title = data.getGist().getTitle();
                    String hlsUrl = getHlsUrl(data);
                    String[] extraData = new String[4];
                    extraData[0] = permalink;
                    extraData[1] = hlsUrl;
                    extraData[2] = data.getGist().getId();
                    if (data.getContentDetails() != null &&
                            data.getContentDetails().getClosedCaptions() != null) {
                        for (ClosedCaptions closedCaption :
                                data.getContentDetails().getClosedCaptions()) {
                            if ("SRT".equalsIgnoreCase(closedCaption.getFormat())) {
                                extraData[3] = closedCaption.getUrl();
                                break;
                            }
                        }
                    }
                    if (!appCMSPresenter.launchTVButtonSelectedAction(permalink,
                            action,
                            title,
                            extraData,
                            data,
                            false,
                            -1,
                            rowData.relatedVideoIds,
                            null)) {

                    }
                }
                itemViewHolder.view.setClickable(false);
                new Handler().postDelayed(() -> itemViewHolder.view.setClickable(true), 2000);
            } else if (item instanceof SeeAllCard) {
                String action = getContext().getString(R.string.app_cms_see_all_category_action);
//                appCMSPresenter.navigateToSeeAllCategoryPage(((SeeAllCard) item).getModuleAPI(), action);
                 String[] authData = new String[2];
                 authData[0] = ((SeeAllCard) item).getModuleAPI().getId();
                 authData[1] = String.valueOf(adapterSize);
                 appCMSPresenter.setSeeAllModule(((SeeAllCard) item).getModuleAPI());
                 appCMSPresenter.setLastPage(false);
                 appCMSPresenter.setOffset(0);
                 appCMSPresenter.launchTVButtonSelectedAction(((SeeAllCard) item).getModuleAPI().getSettings().getSeeAllPermalink(),
                         action,
                         ((SeeAllCard) item).getModuleAPI().getTitle(),
                         authData,
                         null,
                         false,
                         0,
                         null,
                         null);
             }
        }
    }

    private String getHlsUrl(ContentDatum data) {
        if (data.getStreamingInfo() != null &&
                data.getStreamingInfo().getVideoAssets() != null &&
                data.getStreamingInfo().getVideoAssets().getHls() != null) {
            return data.getStreamingInfo().getVideoAssets().getHls();
        }
        return null;
    }

    boolean isPlayerComponentSelected = false;
    CustomTVVideoPlayerView customVideoVideoPlayerView;
    private class ItemViewSelectedListener implements OnItemViewSelectedListener {
        @Override
        public void onItemSelected(Presenter.ViewHolder itemViewHolder, Object item,
                                   RowPresenter.ViewHolder rowViewHolder, Row row) {

            new Thread() {
                @Override
                public void run() {
                    if (item instanceof BrowseFragmentRowData) {
                        BrowseFragmentRowData item1 = (BrowseFragmentRowData) item;
                        if (item1.rowNumber == mRowsAdapter.size() - 1) {
                            System.out.println("Reached the last row");

                        }
                    }
                }
            }.start();
            if(appCMSPresenter.isFullScreenVisible){
                return;
            }

            if (item instanceof BrowseFragmentRowData) {
                typeData = true;
                isPlayerComponentSelected = false;
                rowData = (BrowseFragmentRowData) item;
                if (rowData != null) {
                    if(getActivity() instanceof AppCmsHomeActivity){
                        ((AppCmsHomeActivity) getActivity()).shouldShowLeftNavigation(rowData.itemPosition == 0);
                    }
                    data = rowData.contentData;
                    if(rowData.isPlayerComponent){
                        if( null != itemViewHolder && null != itemViewHolder.view
                                && ((FrameLayout)((FrameLayout) itemViewHolder.view).getChildAt(0)).getChildAt(0) instanceof CustomTVVideoPlayerView){
                            customVideoVideoPlayerView  =  (CustomTVVideoPlayerView)((FrameLayout)((FrameLayout) itemViewHolder.view).getChildAt(0)).getChildAt(0);
                            View parentLayout = appCMSPresenter.getCurrentActivity().findViewById(R.id.parent_layout);
                            if (parentLayout != null) {
                                parentLayout.requestFocus();
                            }
                            if(isEnableMiniPlayer) {
                                Utils.setCustomTVVideoPlayerView(null);
                                Utils.setCustomTVVideoPlayerView(customVideoVideoPlayerView);
                            }
                            if(customVideoVideoPlayerView.isLoginButtonVisible() && appCMSPresenter.isUserLoggedIn()){
                                customVideoVideoPlayerView.showRestrictMessage(getString(R.string.reload_page_from_menu));
                                customVideoVideoPlayerView.toggleLoginButtonVisibility(false);

                            }
                        }
                        isPlayerComponentSelected = true;
                        Utils.isPlayerSelected = true;
                        if(isPlayerComponentSelected){
                            new Handler().postDelayed(() -> customVideoVideoPlayerView.isVideoScheduled(),1000 );
                            appCMSPresenter.sendBroadcastToHandleMiniPlayer(false);
                        }
                        showMoreContentIcon();
                    } else if(rowData.isSearchPage){
                        if (AppCmsBrowseFragment.this.isAdded())
                        new Handler().postDelayed(() -> Utils.setBrowseFragmentViewParameters(view,
                                (int) getResources().getDimension(R.dimen.grid_browse_fragment_margin_left),
                                (int) getResources().getDimension(R.dimen.browse_fragment_margin_top)), 0);
                    } else if (null != rowData.blockName && (rowData.blockName.equalsIgnoreCase("showDetail01") ||
                            rowData.blockName.equalsIgnoreCase("showDetail08"))){
                        if (AppCmsBrowseFragment.this.isAdded())
                            new Handler().postDelayed(() -> Utils.setBrowseFragmentViewParameters(view,
                                (int) getResources().getDimension(R.dimen.browse_fragment_show_season_margin_left),
                                (int) getResources().getDimension(R.dimen.browse_fragment_margin_top)), 0);
                    }else {
                        hideController();
                        Utils.isPlayerSelected = false;
                        if(pageView != null){
                            if ( pageView.findViewById(R.id.expanded_view_id) != null)
                                ((TVExpandedViewModule) pageView.findViewById(R.id.expanded_view_id)).refreshData(rowData.contentData);
                            if(pageView.findViewById(R.id.video_player_id) != null
                                    && appCMSPresenter.getTemplateType().equals(AppCMSPresenter.TemplateType.NEWS))
                                pageView.findViewById(R.id.video_player_id).setFocusable(true);
                        }
                        if (rowData.rowNumber == 1) {
                            appCMSPresenter.sendBroadcastToHandleMiniPlayer(true);
                            if (customVideoVideoPlayerView != null) {
                                new Handler().postDelayed(() -> customVideoVideoPlayerView.isVideoScheduled(), 1000);
                            }
                        }
                    }
                }
            } else {
                typeData = false;
            }

        }

    }



    boolean isFirstTime = true;
    private void showMoreContentIcon(){
        if(isPlayerComponentSelected && isFirstTime && mRowsAdapter != null && mRowsAdapter.size() > 1){
            isFirstTime = false;
            if(appCMSPresenter.getTemplateType() == AppCMSPresenter.TemplateType.SPORTS){
                getActivity().findViewById(R.id.press_down_button).setVisibility(View.VISIBLE);
            } else {
                getActivity().findViewById(R.id.press_down_button).setVisibility(View.INVISIBLE);
            }
        }
        hideFooterControls();
    }

    private void hideFooterControls(){
        new Handler().postDelayed(() -> hideController(),8000);
    }

    private void hideController() {
        if(appCMSPresenter.getTemplateType() == AppCMSPresenter.TemplateType.SPORTS){
            try {
                getActivity().findViewById(R.id.press_up_button).setVisibility(View.INVISIBLE);
                getActivity().findViewById(R.id.press_down_button).setVisibility(View.INVISIBLE);
                getActivity().findViewById(R.id.top_logo).setVisibility(View.INVISIBLE);
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onPause() {
        new Handler().postDelayed(() -> {
            if(null != customVideoVideoPlayerView && !isEnableMiniPlayer){
                customVideoVideoPlayerView.pausePlayer();
                customVideoVideoPlayerView.removeTimerCallBack();
            }

        }, 51);
        AppCmsHomeActivity activity = (AppCmsHomeActivity) getActivity();
        if (activity != null && activity.isActive()) {
            Utils.isPlayerSelected = false;
        }
        super.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(customVideoVideoPlayerView != null){
            customVideoVideoPlayerView.pausePlayer();
            customVideoVideoPlayerView.removeTimerCallBack();
        }
    }

    public CustomTVVideoPlayerView getCustomVideoVideoPlayerView(){
        return customVideoVideoPlayerView;
    }
}
