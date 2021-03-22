package com.viewlift.tv.views.fragment;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.leanback.widget.ArrayObjectAdapter;
import androidx.leanback.widget.ListRow;

import com.google.gson.GsonBuilder;
import com.viewlift.AppCMSApplication;
import com.viewlift.R;
import com.viewlift.models.data.appcms.api.ContentDatum;
import com.viewlift.models.data.appcms.api.Image_16x9;
import com.viewlift.models.data.appcms.api.Images;
import com.viewlift.models.data.appcms.api.Module;
import com.viewlift.models.data.appcms.search.AppCMSSearchResult;
import com.viewlift.models.data.appcms.ui.AppCMSUIKeyType;
import com.viewlift.models.data.appcms.ui.page.AppCMSPageUI;
import com.viewlift.models.data.appcms.ui.page.Component;
import com.viewlift.models.data.appcms.ui.page.ModuleList;
import com.viewlift.models.network.modules.AppCMSSearchUrlData;
import com.viewlift.models.network.rest.AppCMSSearchCall;
import com.viewlift.presenters.AppCMSPresenter;
import com.viewlift.tv.model.BrowseFragmentRowData;
import com.viewlift.tv.utility.Utils;
import com.viewlift.tv.views.customviews.AppCMSArrayObjectAdaptor;
import com.viewlift.tv.views.customviews.CustomHeaderItem;
import com.viewlift.tv.views.presenter.AppCmsListRowPresenter;
import com.viewlift.tv.views.presenter.CardPresenter;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import rx.Observable;
import rx.functions.Action1;



public class AppCmsLibraryFragment extends BaseFragment {

    private static final String TAG = AppCmsLibraryFragment.class.getName();
    private static final long DELAY = 1000;
    @Inject
    AppCMSSearchUrlData appCMSSearchUrlData;
    @Inject
    AppCMSSearchCall appCMSSearchCall;
    private String lastSearchedString = "";
    private SearchAsyncTask searchTask;
    private ModuleList moduleList;
    private int trayIndex = -1;
    private AppCMSArrayObjectAdaptor mRowsAdapter;
    private AppCMSPresenter appCMSPresenter;
    TextView noSearchTextView;
    TextView searchPrevious;
    private TextView searchOne, searchTwo, searchThree;
    private Button btnClearHistory;
    private EditText editText;
    private LinearLayout llView;
    private boolean clrbtnFlag;

    private ProgressBar progressBar;
    private AppCMSPageUI appCmsPageUI;

    public static AppCmsLibraryFragment appCmsLibraryFragment;

    View librayView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bindSearchComponent();
    }

    public static AppCmsLibraryFragment newInstance() {
        Bundle args = new Bundle();
        args.putString("searchString", "library");
        AppCmsLibraryFragment fragment = new AppCmsLibraryFragment();
        fragment.setArguments(args);
//        appCmsLibraryFragment=fragment;
        return fragment;
    }

    public static AppCmsLibraryFragment getInstance() {
        return appCmsLibraryFragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.appcms_library_view , null);
        librayView=view;
        TextView libraryTextView = (TextView)view.findViewById(R.id.appcms_library);
        libraryTextView.setTypeface(Utils.getSpecificTypeface(mContext,
                appCMSPresenter,
                getString(R.string.app_cms_page_font_regular_key)));
        libraryTextView.setTextColor(Color.parseColor(appCMSPresenter.getAppTextColor()));

        if(moduleList == null){
            System.out.print("moduleList is null *******");
            if(appCMSPresenter.getTemplateType() == AppCMSPresenter.TemplateType.SPORTS) {
                moduleList = new GsonBuilder().create().
                        fromJson(Utils.loadJsonFromAssets(getActivity(), "tray_ftv_component_sports.json"), ModuleList.class);
            }else {
                /*moduleList = new GsonBuilder().create().
                        fromJson(Utils.loadJsonFromAssets(getActivity(),
                                requirelandscapeTray ? "tray_ftv_component_tray02.json" : "tray_ftv_component.json")
                                , ModuleList.class);*/

                moduleList = new GsonBuilder().create().
                        fromJson(Utils.loadJsonFromAssets(getActivity(),
                                "tray_ftv_component_tray02.json")
                                , ModuleList.class);

            }
        }
        return view;
    }

    private boolean searchData(String searchedText){
        if (lastSearchedString.trim().equals(searchedText.trim())) {
            return false;
        }
        if (searchedText.trim().length() > 0){
            if(appCMSPresenter.isNetworkConnected()){
                handler.removeCallbacks(searcRunnable);
                handler.postDelayed(searcRunnable,DELAY);
                progressBar.setVisibility(View.VISIBLE);
            }else{
                appCMSPresenter.searchRetryDialog(searchedText);
            }
        }else{
            lastSearchedString = "";
            if(null != mRowsAdapter){
                mRowsAdapter.clear();
            }
            noSearchTextView.setVisibility(View.GONE);
            progressBar.setVisibility(View.INVISIBLE);
        }
        return false;
    }

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()){
                case R.id.search_history_one :
                case R.id.search_history_two:
                case R.id.search_history_three:
                    TextView textView = (TextView)view;
                    editText.setText(textView.getText());
                    searchData(textView.getText().toString());
                    break;
                case R.id.btn_clear_history:
                    llView.setVisibility(View.INVISIBLE);
                    appCMSPresenter.clearSearchResultsSharePreference();
                    currentString = "";
                    previousString = "";

                  /*  if(clrbtnFlag) {
                        List<String> result = null;
                        result = appCMSPresenter.getSearchResultsFromSharePreference();
                        if(result == null)
                            result = new ArrayList<String>();

                        result.add(lastSearchedString);
                        appCMSPresenter.setSearchResultsOnSharePreference(result);
                    }*/


                    break;
            }

        }
    };

    @Override
    public void onResume() {
        super.onResume();
        List<String> resultForTv = appCMSPresenter.getSearchResultsFromSharePreference();

        if (moduleList.getBlockName().equalsIgnoreCase("search01") ||
                appCMSPresenter.getTemplateType().equals(AppCMSPresenter.TemplateType.ENTERTAINMENT)) {

        }
       /* if(mRowsAdapter == null || (mRowsAdapter != null && mRowsAdapter.size() == 0))
        customKeyboard.requestFocus();*/

    }


    /**
     * Handle the key press event on the keyboard.
     * All the events are handled viz. space, number/alphabets and back presses
     *
     * @param v instance of the view on which the keyPressed is called
     */
    public void keyPressed(View v) {
        if (v instanceof Button) {
            editText.append(" ");
        } else if (v instanceof TextView) {
            CharSequence text = ((TextView) v).getText();
            editText.append(text);
        } else if (v instanceof ImageButton) {
            String s = editText.getText().toString();
            if (s.length() > 0)
                editText.setText(s.substring(0, s.length() - 1));
        }
    }

    private void setFocusSequence(){
        searchOne.setNextFocusRightId(searchTwo.getVisibility() == View.VISIBLE ? R.id.search_history_two : R.id.btn_clear_history);
        searchTwo.setNextFocusRightId(searchThree.getVisibility() == View.VISIBLE? R.id.search_history_three : R.id.btn_clear_history);
        btnClearHistory.setNextFocusLeftId(searchThree.getVisibility() == View.VISIBLE ? R.id.search_history_three
                : ((searchTwo.getVisibility() == View.VISIBLE  ?
                R.id.search_history_two : R.id.search_history_one)));

    }

    private String getUrl(String url){
        return getString(R.string.app_cms_search_api_url_video_only,
                appCMSSearchUrlData.getBaseUrl(),
                appCMSSearchUrlData.getSiteName(),
                url,
                getString(R.string.type_video_only) +","+ getString(R.string.app_cms_series_content_type).toLowerCase()
                        + ","+getString(R.string.media_type_bundle).toLowerCase(),
                appCMSPresenter.getLanguageParamForAPICall());

    }
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }


    private void bindSearchComponent(){
        appCMSPresenter =
                ((AppCMSApplication) getActivity().getApplication()).getAppCMSPresenterComponent().appCMSPresenter();
    }


    String previousString = "" , currentString = "";
    Action1<List<AppCMSSearchResult>> searchDataObserver = new Action1<List<AppCMSSearchResult>>() {
        @Override
        public void call(List<AppCMSSearchResult> appCMSSearchResults) {
            if(null != mRowsAdapter){
                mRowsAdapter.clear();
                mRowsAdapter = null;
            }
            if(null != appCMSSearchResults && appCMSSearchResults.size() > 0){
                clrbtnFlag = true;
                noSearchTextView.setVisibility(View.GONE);

                if(currentString.length() > 0){
                    previousString = currentString;
                }
                currentString = lastSearchedString;

                if(previousString.length() > 0){
                    addSearchValueInSharePref(previousString);
                }
                List<String> resultForTv = appCMSPresenter.getSearchResultsFromSharePreference();
                if (moduleList.getBlockName().equalsIgnoreCase("search01")  || appCMSPresenter.getTemplateType().equals(AppCMSPresenter.TemplateType.ENTERTAINMENT)) {
                    if (resultForTv != null && resultForTv.size() > 0) {
                        if (resultForTv.size() > 0) {
                            llView.setVisibility(View.VISIBLE);
                            setSearchValueOnView(resultForTv, resultForTv.size());

                        } else {
                            llView.setVisibility(View.INVISIBLE);
                        }
                    }
                } else {
                    llView.setVisibility(View.GONE);
                }
//                setAdapter(appCMSSearchResults);
            }else{
                if(!appCMSPresenter.isNetworkConnected()){
                    appCMSPresenter.searchRetryDialog(lastSearchedString.toString());
                }else{
                    clrbtnFlag = false;
                    noSearchTextView.setText(appCMSPresenter.getLanguageResourcesFile().getStringValueWithDoubleQuotes(getString(R.string.app_cms_no_search_result),lastSearchedString).toUpperCase());
                    noSearchTextView.setVisibility(View.VISIBLE);
                }

                List<String> resultForTv = appCMSPresenter.getSearchResultsFromSharePreference();
                if (moduleList.getBlockName().equalsIgnoreCase("search01") || appCMSPresenter.getTemplateType().equals(AppCMSPresenter.TemplateType.ENTERTAINMENT)) {
                    if (resultForTv != null && resultForTv.size() > 0) {
                        llView.setVisibility(View.VISIBLE);
                        if (resultForTv.size() > 3) {
                            resultForTv.remove(resultForTv.iterator().next());
                        }
                        setSearchValueOnView(resultForTv, resultForTv.size());
                    }
                } else {
                    llView.setVisibility(View.GONE);
                }
            }

            setFocusSequence();
            progressBar.setVisibility(View.INVISIBLE);
        }

    };

    @Override
    public void onPause() {
        if(currentString.length() > 0){
            addSearchValueInSharePref(currentString);
        }
        currentString = "";
        previousString = "";
        super.onPause();
    }

    public void hideSoftKeyboard() {
        InputMethodManager inputMethodManager = (InputMethodManager) getActivity().getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), 0);
    }

    private void setSearchValueOnView(List<String> resultForTv, int size){
        for (int i = 0; i < size; i++) {
            if(i == 0){
                searchOne.setText(resultForTv.get(i).trim().toUpperCase());
                searchTwo.setVisibility(View.INVISIBLE);
                searchThree.setVisibility(View.INVISIBLE);
            } else if(i == 1){
                searchTwo.setVisibility(View.VISIBLE);
                searchThree.setVisibility(View.INVISIBLE);
                searchTwo.setText(resultForTv.get(i).trim().toUpperCase());
            } else if(i == 2){
                searchThree.setVisibility(View.VISIBLE);
                searchThree.setText(resultForTv.get(i).trim().toUpperCase());
            }
        }
    }

    private void addSearchValueInSharePref(String valueToBeSaved){
        List<String> result = appCMSPresenter.getSearchResultsFromSharePreference();
        if(result == null) {
            List<String> list = new ArrayList<String>();
            list.add(valueToBeSaved);
            appCMSPresenter.setSearchResultsOnSharePreference(list);
        } else {
            if (!result.isEmpty() && result.size() == 4) {
                result.remove(result.iterator().next());
            }
            for(int i = 0; i < result.size(); i++) {
                if(valueToBeSaved.trim().equalsIgnoreCase(result.get(i).trim())){

                    result.remove(result.get(i));
                    break;
                }
            }
            result.add(valueToBeSaved);
            appCMSPresenter.setSearchResultsOnSharePreference(result);
        }

    }

    Handler handler = new Handler();
    Runnable searcRunnable = new Runnable() {
        @Override
        public void run() {
            if(editText.getText().toString().length() > 0) {
                try {
                    if (searchTask != null && searchTask.getStatus() == AsyncTask.Status.RUNNING) {
                        searchTask.cancel(true);
                    }
                    searchTask = new SearchAsyncTask(searchDataObserver,
                            appCMSSearchCall,
                            appCMSPresenter.getApiKey());
                    String encodedString  = URLEncoder.encode(editText.getText().toString().trim() , "UTF-8");
                    String secondEncoding = URLEncoder.encode(encodedString , "UTF-8");

                    final String url = getUrl(secondEncoding);
                    System.out.println("Search result == " + editText.getText().toString().trim() + "url = " + url);
                    lastSearchedString = editText.getText().toString().trim();
                    searchTask.execute(url);
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }
    };

    private class SearchAsyncTask extends AsyncTask<String, Void, List<AppCMSSearchResult>> {
        final Action1<List<AppCMSSearchResult>> dataReadySubscriber;
        final AppCMSSearchCall appCMSSearchCall;
        final String apiKey;

        SearchAsyncTask(Action1<List<AppCMSSearchResult>> dataReadySubscriber,
                        AppCMSSearchCall appCMSSearchCall,
                        String apiKey) {
            this.dataReadySubscriber = dataReadySubscriber;
            this.appCMSSearchCall = appCMSSearchCall;
            this.apiKey = apiKey;
        }

        @Override
        protected List<AppCMSSearchResult> doInBackground(String... params) {
            if (params.length > 0) {
                try {
                    return appCMSSearchCall.call(apiKey, params[0],appCMSPresenter.getAuthToken());
                } catch (IOException e) {
                    //Log.e(TAG, "I/O DialogType retrieving search data from URL: " + params[0]);
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(List<AppCMSSearchResult> result) {
            if(isAdded()) {
                Observable.just(result).subscribe(dataReadySubscriber);
            }
        }
    }

    private Context mContext;
    @Override
    public void onAttach(Context context) {
        mContext = context;
        super.onAttach(context);
    }

    public void setAdapter(List<ContentDatum> appCMSSearchResults){

        appCMSPresenter.showLoadingDialog(true);

        if(appCMSSearchResults.size()<1){
            noSearchTextView = (TextView)librayView.findViewById(R.id.appcms_no_search_result);
            noSearchTextView.setVisibility(View.VISIBLE);
            noSearchTextView.setTextColor(Color.parseColor(appCMSPresenter.getAppTextColor()));
            appCMSPresenter.showLoadingDialog(false);
            return;
        }

        if(null != moduleList){
            trayIndex = -1;
            for(Component component : moduleList.getComponents()){
                createTrayModule(getActivity() ,
                        component ,
                        appCMSSearchResults,
                        moduleList,
                        appCMSPresenter.getJsonValueKeyMap(),
                        appCMSPresenter,
                        false);
            }
        }

        if(null != mRowsAdapter && mRowsAdapter.size() > 0){
            {
                AppCmsBrowseFragment browseFragment = AppCmsBrowseFragment.newInstance(mContext);
                browseFragment.setScreenName("Search");
                browseFragment.setmRowsAdapter(mRowsAdapter);
                getChildFragmentManager().beginTransaction().replace(R.id.appcms_search_results_container ,browseFragment ,"frag").commitAllowingStateLoss();
                appCMSPresenter.showLoadingDialog(false);

            }
        }
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {

    }

    private CustomHeaderItem customHeaderItem = null;
    private Component mHeaderComponent = null;

    public void createTrayModule(final Context context,
                                 final Component component,
                                 List<ContentDatum> appCMSSearchResults,
                                 final ModuleList moduleUI,
                                 Map<String, AppCMSUIKeyType> jsonValueKeyMap,
                                 final AppCMSPresenter appCMSPresenter,
                                 boolean isCarousel) {
        AppCMSUIKeyType componentType = jsonValueKeyMap.get(component.getType());
        if (componentType == null) {
            componentType = AppCMSUIKeyType.PAGE_EMPTY_KEY;
        }
        AppCMSUIKeyType componentKey = jsonValueKeyMap.get(component.getKey());
        if (componentKey == null) {
            componentKey = AppCMSUIKeyType.PAGE_EMPTY_KEY;
        }
        switch (componentType) {
            case PAGE_LABEL_KEY:
                switch (componentKey) {
                    case PAGE_TRAY_TITLE_KEY:
                        mHeaderComponent = component;
//                        createCustomHeaderItem(context, component, appCMSSearchResults, moduleUI, appCMSPresenter, isCarousel,appCMSPresenter.getLanguageResourcesFile().getUIresource(getString(R.string.content_type_series)));
                        break;
                }
                break;
            case PAGE_COLLECTIONGRID_KEY:
                if (moduleUI.getBlockName().equalsIgnoreCase("search01") || appCMSPresenter.getTemplateType().equals(AppCMSPresenter.TemplateType.ENTERTAINMENT) ) {
                    createRowsForEntertainment(context, component, appCMSSearchResults, moduleUI, jsonValueKeyMap, appCMSPresenter);
                } else {
//                    createRowsForST(context, component, appCMSSearchResults, moduleUI, jsonValueKeyMap, appCMSPresenter);
                }
                break;
        }
    }

    private void createCustomHeaderItem(Context context, Component component, String contentTitle, ModuleList moduleUI, AppCMSPresenter appCMSPresenter, boolean isCarousel, String trayTitle) {
        customHeaderItem = null;
        String title = "";
        title=contentTitle;
        customHeaderItem = new CustomHeaderItem(
                context,
                ++trayIndex,
                title
        );
        customHeaderItem.setmIsCarousal(isCarousel);

        String padding = moduleUI.getLayout().getTv().getPadding();
        customHeaderItem.setmListRowLeftMargin(Integer.valueOf(padding != null ? padding : "0"));
        customHeaderItem.setmListRowRightMargin(Integer.valueOf(padding != null ? padding : "0"));


       /* customHeaderItem.setmListRowLeftMargin(Integer.valueOf(moduleUI.getLayout().getTv().getPadding()));
        customHeaderItem.setmListRowRightMargin(Integer.valueOf(moduleUI.getLayout().getTv().getPadding()));*/
        customHeaderItem.setmBackGroundColor(moduleUI.getLayout().getTv().getBackgroundColor());
        customHeaderItem.setmListRowHeight(Integer.valueOf(moduleUI.getLayout().getTv().getHeight()));
        customHeaderItem.setFontFamily(component.getFontFamily());
        customHeaderItem.setFontWeight(component.getFontWeight());
        customHeaderItem.setFontSize(component.getLayout().getTv().getFontSize());
    }

    private void createRowsForEntertainment(Context context,
                                            Component component,
                                            List<ContentDatum> appCMSSearchResults,
                                            ModuleList moduleUI,
                                            Map<String, AppCMSUIKeyType> jsonValueKeyMap,
                                            AppCMSPresenter appCMSPresenter) {
        if (null == mRowsAdapter) {
            AppCmsListRowPresenter appCmsListRowPresenter = new AppCmsListRowPresenter(context, appCMSPresenter);
            mRowsAdapter = new AppCMSArrayObjectAdaptor(appCmsListRowPresenter);
        }
        CardPresenter trayCardPresenter = new CardPresenter(context, appCMSPresenter,
                Integer.valueOf(component.getLayout().getTv().getHeight()),
                Integer.valueOf(component.getLayout().getTv().getWidth()),
                moduleUI.getSettings() != null && moduleUI.getSettings().isInfoHover(),
                moduleUI.getSettings() != null && moduleUI.getSettings().isNoInfo(),
                component,
                jsonValueKeyMap, null,false);

        ArrayObjectAdapter trayListShowsRowAdapter = new ArrayObjectAdapter(trayCardPresenter);
        ArrayObjectAdapter trayListVideosRowAdapter = new ArrayObjectAdapter(trayCardPresenter);
        ArrayObjectAdapter trayListBundleAdapter = new ArrayObjectAdapter(trayCardPresenter);
        ArrayObjectAdapter trayListEpisodesAdapter = new ArrayObjectAdapter(trayCardPresenter);

       /* for (int i = 0, appCMSSearchResultsSize = appCMSSearchResults.size(); i < appCMSSearchResultsSize; i++) {
            AppCMSSearchResult searchResult = appCMSSearchResults.get(i);
            BrowseFragmentRowData rowData = new BrowseFragmentRowData();
            rowData.isSearchPage = true;
            rowData.contentData = searchResult.getContent();
            rowData.uiComponentList = component.getComponents();
            rowData.action = component.getTrayClickAction();
            rowData.blockName = moduleUI.getBlockName();
            rowData.itemPosition = i;
            rowData.infoHover = moduleUI.getSettings() != null && moduleUI.getSettings().isInfoHover();
            trayListRowAdapter.add(rowData);
            //Log.d(TAG, "NITS header Items ===== " + rowData.contentData.getGist().getTitle());
        }
        mRowsAdapter.add(new ListRow(customHeaderItem, trayListRowAdapter));*/

        //createCustomHeaderItem(context, component, appCMSSearchResults, moduleUI, appCMSPresenter, false,"SERIES");


        createSeriesTray(context, component, appCMSSearchResults, moduleUI, trayListShowsRowAdapter);
        createVideosTray(context, component, appCMSSearchResults, moduleUI, appCMSPresenter, trayListVideosRowAdapter);
        createEpisodesTray(context, component, appCMSSearchResults, moduleUI, appCMSPresenter, trayListEpisodesAdapter);
    }

    private void createEpisodesTray(Context context, Component component, List<ContentDatum> appCMSSearchResults, ModuleList moduleUI, AppCMSPresenter appCMSPresenter, ArrayObjectAdapter trayListVideosRowAdapter) {

        createCustomHeaderItem(context, mHeaderComponent != null ? mHeaderComponent : component, appCMSPresenter.getLanguageResourcesFile().getUIresource(getString(R.string.programs)), moduleUI, appCMSPresenter, false,
                appCMSPresenter.getLanguageResourcesFile().getUIresource(getString(R.string.content_type_videos)));

        for (int i = 0, appCMSSearchResultsSize = appCMSSearchResults.size(); i < appCMSSearchResultsSize; i++) {
            ContentDatum searchResult = appCMSSearchResults.get(i);
            if (searchResult != null && searchResult.getGist() != null
                    && searchResult.getGist().getContentType() != null
                    && searchResult.getGist().getMediaType() != null
                    && (searchResult.getGist().getContentType().equalsIgnoreCase("video")
                    && searchResult.getGist().getMediaType().equalsIgnoreCase("episodic"))
            ) {
                Image_16x9 image_16x9 = new Image_16x9();
                if(searchResult.getGist().getImageGist()!=null && searchResult.getGist().getImageGist().get_16x9()!=null){
                    image_16x9.setUrl(searchResult.getGist().getImageGist().get_16x9());
                    image_16x9.setSecureUrl(searchResult.getGist().getImageGist().get_16x9());
                }
                Images images = new Images();
                images.set_16x9Image(image_16x9);
                images.set_16x9(image_16x9);
                searchResult.setImages(images);

                BrowseFragmentRowData rowData = new BrowseFragmentRowData();
                rowData.contentData = searchResult;
                rowData.uiComponentList = component.getComponents();
                rowData.action = component.getTrayClickAction();
                rowData.blockName = moduleUI.getBlockName();
                rowData.infoHover = moduleUI.getSettings() != null && moduleUI.getSettings().isInfoHover();
                rowData.rowNumber = trayIndex;
                rowData.itemPosition = i;
                rowData.moduleApi = getModuleApi(appCMSSearchResults);
                trayListVideosRowAdapter.add(rowData);
            }
        }

        if (trayListVideosRowAdapter.size() > 0) {
            mRowsAdapter.add(new ListRow(customHeaderItem, trayListVideosRowAdapter));
        } else {
            trayIndex = trayIndex - 1;
        }
    }

    private void createVideosTray(Context context, Component component, List<ContentDatum> appCMSSearchResults, ModuleList moduleUI, AppCMSPresenter appCMSPresenter, ArrayObjectAdapter trayListVideosRowAdapter) {

        createCustomHeaderItem(context, mHeaderComponent != null ? mHeaderComponent : component, "VIDEOS", moduleUI, appCMSPresenter, false,
                appCMSPresenter.getLanguageResourcesFile().getUIresource(getString(R.string.content_type_videos)));

        for (int i = 0, appCMSSearchResultsSize = appCMSSearchResults.size(); i < appCMSSearchResultsSize; i++) {
            ContentDatum searchResult = appCMSSearchResults.get(i);
            if (searchResult != null && searchResult.getGist() != null
                    && searchResult.getGist().getContentType() != null
                    && ((searchResult.getGist().getContentType().equalsIgnoreCase("video") && searchResult.getGist().getMediaType()==null)
                    || (searchResult.getGist().getContentType().equalsIgnoreCase("video") && searchResult.getGist().getMediaType()!=null && searchResult.getGist().getMediaType().equalsIgnoreCase("video")))
            ) {
                Image_16x9 image_16x9 = new Image_16x9();
                if(searchResult.getGist().getImageGist()!=null && searchResult.getGist().getImageGist().get_16x9()!=null){
                    image_16x9.setUrl(searchResult.getGist().getImageGist().get_16x9());
                    image_16x9.setSecureUrl(searchResult.getGist().getImageGist().get_16x9());
                }
                Images images = new Images();
                images.set_16x9Image(image_16x9);
                images.set_16x9(image_16x9);
                searchResult.setImages(images);

                BrowseFragmentRowData rowData = new BrowseFragmentRowData();
                rowData.contentData = searchResult;
                rowData.uiComponentList = component.getComponents();
                rowData.action = component.getTrayClickAction();
                rowData.blockName = moduleUI.getBlockName();
                rowData.infoHover = moduleUI.getSettings() != null && moduleUI.getSettings().isInfoHover();
                rowData.rowNumber = trayIndex;
                rowData.itemPosition = i;
                rowData.moduleApi = getModuleApi(appCMSSearchResults);
                trayListVideosRowAdapter.add(rowData);
            }
        }

        if (trayListVideosRowAdapter.size() > 0) {
            mRowsAdapter.add(new ListRow(customHeaderItem, trayListVideosRowAdapter));
        } else {
            trayIndex = trayIndex - 1;
        }
    }

    private void createSeriesTray(Context context, Component component, List<ContentDatum> appCMSSearchResults, ModuleList moduleUI, ArrayObjectAdapter trayListShowsRowAdapter) {
        createCustomHeaderItem(context, mHeaderComponent != null ? mHeaderComponent : component, appCMSPresenter.getLanguageResourcesFile().getUIresource(context.getString(R.string.app_cms_bundle_series_title)) , moduleUI, appCMSPresenter, false,
                appCMSPresenter.getLanguageResourcesFile().getUIresource(getString(R.string.content_type_series)));

        for (int i = 0, appCMSSearchResultsSize = appCMSSearchResults.size(); i < appCMSSearchResultsSize; i++) {
            ContentDatum searchResult = appCMSSearchResults.get(i);
            if (searchResult != null && searchResult.getGist() != null
                    && searchResult.getGist().getContentType() != null //video, series/show and episodic
                    && (searchResult.getGist().getContentType().equalsIgnoreCase("series")
                    || searchResult.getGist().getContentType().equalsIgnoreCase("show") || searchResult.getGist().getContentType().equalsIgnoreCase("season")
                    || searchResult.getGist().getContentType().toLowerCase().equalsIgnoreCase("bundle")
            )) {

                Image_16x9 image_16x9 = new Image_16x9();
                if(searchResult.getGist().getImageGist()!=null && searchResult.getGist().getImageGist().get_16x9()!=null){
                    image_16x9.setUrl(searchResult.getGist().getImageGist().get_16x9());
                    image_16x9.setSecureUrl(searchResult.getGist().getImageGist().get_16x9());
                }
                Images images = new Images();
                images.set_16x9Image(image_16x9);
                images.set_16x9(image_16x9);
                searchResult.setImages(images);
                BrowseFragmentRowData rowData = new BrowseFragmentRowData();
                rowData.contentData = searchResult;
                rowData.uiComponentList = component.getComponents();
                rowData.action = component.getTrayClickAction();
                rowData.blockName = moduleUI.getBlockName();
                rowData.infoHover = moduleUI.getSettings() != null && moduleUI.getSettings().isInfoHover();
                rowData.rowNumber = trayIndex;
                rowData.itemPosition = i;
                rowData.moduleApi = getModuleApi(appCMSSearchResults);
                trayListShowsRowAdapter.add(rowData);
            }
        }
        if (trayListShowsRowAdapter.size() > 0) {
            mRowsAdapter.add(new ListRow(customHeaderItem, trayListShowsRowAdapter));
        } else {
            trayIndex = trayIndex - 1;
        }
    }

    private Module getModuleApi(List<ContentDatum> searchResult){
        Module moduleApi = new Module();
        moduleApi.setConceptaData(searchResult);
        return moduleApi;
    }

    private void createBundleTray(Context context, Component component, List<ContentDatum> appCMSSearchResults, ModuleList moduleUI, AppCMSPresenter appCMSPresenter, ArrayObjectAdapter trayListBundleAdapter) {

        createCustomHeaderItem(context, mHeaderComponent != null ? mHeaderComponent : component, "BUNDLE", moduleUI, appCMSPresenter, false,
                appCMSPresenter.getLanguageResourcesFile().getUIresource(getString(R.string.media_type_bundle)));for (int i = 0, appCMSSearchResultsSize = appCMSSearchResults.size(); i < appCMSSearchResultsSize; i++) {
            ContentDatum searchResult = appCMSSearchResults.get(i);
            if (searchResult != null && searchResult.getGist() != null
                    && searchResult.getGist().getContentType() != null
                    && (searchResult.getGist().getContentType().toLowerCase().equalsIgnoreCase("bundle"))
            ) {
                Image_16x9 image_16x9 = new Image_16x9();
                if(searchResult.getGist().getImageGist()!=null && searchResult.getGist().getImageGist().get_16x9()!=null){
                    image_16x9.setUrl(searchResult.getGist().getImageGist().get_16x9());
                    image_16x9.setSecureUrl(searchResult.getGist().getImageGist().get_16x9());
                }
                Images images = new Images();
                images.set_16x9Image(image_16x9);
                images.set_16x9(image_16x9);
                searchResult.setImages(images);

                BrowseFragmentRowData rowData = new BrowseFragmentRowData();
                rowData.contentData = searchResult;
                rowData.uiComponentList = component.getComponents();
                rowData.action = component.getTrayClickAction();
                rowData.blockName = moduleUI.getBlockName();
                rowData.infoHover = moduleUI.getSettings() != null && moduleUI.getSettings().isInfoHover();
                rowData.rowNumber = trayIndex;
                rowData.itemPosition = i;
                trayListBundleAdapter.add(rowData);
            }
        }

        if (trayListBundleAdapter.size() > 0) {
            mRowsAdapter.add(new ListRow(customHeaderItem, trayListBundleAdapter));
        } else {
            trayIndex = trayIndex - 1;
        }
    }



    public void searchResult(String searchQuery){
        try {
            handler.removeCallbacks(searcRunnable);
            if (searchTask != null && searchTask.getStatus() == AsyncTask.Status.RUNNING) {
                searchTask.cancel(true);
            }
            searchTask = new SearchAsyncTask(searchDataObserver,
                    appCMSSearchCall,
                    appCMSPresenter.getApiKey());

            String encodedString = URLEncoder.encode(searchQuery.trim(), "UTF-8");
            String secondEncoding = URLEncoder.encode(encodedString, "UTF-8");

            final String url = getUrl(secondEncoding);
            System.out.println("Search result == " + editText.getText().toString().trim());
            lastSearchedString = editText.getText().toString().trim();
            searchTask.execute(url);
        }catch (Exception e){
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


