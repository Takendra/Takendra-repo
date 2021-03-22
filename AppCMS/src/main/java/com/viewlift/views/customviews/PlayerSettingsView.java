package com.viewlift.views.customviews;

import android.content.Context;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.exoplayer2.util.Log;
import com.viewlift.AppCMSApplication;
import com.viewlift.R;
import com.viewlift.db.AppPreference;
import com.viewlift.models.data.appcms.ui.main.LocalisedStrings;
import com.viewlift.models.data.appcms.ui.page.Component;
import com.viewlift.models.data.playersettings.PlayerSettingsContent;
import com.viewlift.presenters.AppCMSPresenter;
import com.viewlift.views.activity.AppCMSPageActivity;
import com.viewlift.views.activity.AppCMSPlayVideoActivity;
import com.viewlift.views.adapters.AppCMSDownloadRadioAdapter;
import com.viewlift.views.adapters.ClosedCaptionSelectorAdapter;
import com.viewlift.views.adapters.HLSStreamingQualitySelectorAdapter;
import com.viewlift.views.adapters.LanguageSelectorAdapter;
import com.viewlift.views.adapters.StreamingQualitySelectorAdapter;
import com.viewlift.views.fragments.PlayerSettingContentListFragment;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

public class PlayerSettingsView extends FrameLayout {

    @Inject
    AppPreference appPreference;
    @Inject
    AppCMSPresenter appCMSPresenter;
    @Inject
    LocalisedStrings localisedStrings;

    private VideoPlayerView.VideoPlayerSettingsEvent playerSettingsEvent;
    private ClosedCaptionSelectorAdapter closedCaptionSelectorAdapter;
    private StreamingQualitySelectorAdapter streamingQualitySelectorAdapter;
    private HLSStreamingQualitySelectorAdapter hlsStreamingQualitySelectorAdapter;
    private LanguageSelectorAdapter languageSelectorAdapter;

    int SelectedStreamingQualityIndex = 0;
    int SelectedClosedCaptionIndex = 0;
    int languageSelectorIndex = 0;

    SimpleItemRecyclerViewAdapter simpleItemRecyclerViewAdapter;

    Context mContext;

    ArrayList<PlayerSettingsContent> settingsItem = new ArrayList<>();

    public PlayerSettingsView(@NonNull Context context) {
        super(context);
        mContext = context;
    }

    public PlayerSettingsView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
    }

    public PlayerSettingsView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
    }

    public PlayerSettingsView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        mContext = context;
    }

    public void initializeView() {
        ((AppCMSApplication) this.mContext.getApplicationContext()).getAppCMSPresenterComponent().inject(this);
        LayoutInflater.from(mContext).inflate(R.layout.player_setting_view, this);

        View recyclerView = findViewById(R.id.item_list);
        View seprator = findViewById(R.id.seprator);
        View rootView = findViewById(R.id.playerSettingRootView);
        AppCompatImageButton buttonPlayerSettingSubmit = findViewById(R.id.buttonPlayerSettingSubmit);
        //int fillColor = appCMSPresenter.getBrandPrimaryCtaColor();
        int fillColor = appCMSPresenter.getGeneralTextColor();
        buttonPlayerSettingSubmit.getDrawable().setColorFilter(new PorterDuffColorFilter(fillColor, PorterDuff.Mode.MULTIPLY));
        buttonPlayerSettingSubmit.setOnClickListener(v -> {

            if (playerSettingsEvent != null) {
                setCCSelectionIndex();
                playerSettingsEvent.finishPlayerSetting();
            }

        });
        seprator.setBackgroundColor(0x55000000 + appCMSPresenter.getGeneralTextColor());
        rootView.setBackgroundColor(appCMSPresenter.getGeneralBackgroundColor());

        assert recyclerView != null;
        setupRecyclerView((RecyclerView) recyclerView);
    }

    public void setCCSelectionIndex() {

        SelectedClosedCaptionIndex = closedCaptionSelectorAdapter != null ? closedCaptionSelectorAdapter.getSelectedIndex() : -1;
        languageSelectorIndex = languageSelectorAdapter != null ? languageSelectorAdapter.getSelectedIndex() : -1;
        if (streamingQualitySelectorAdapter != null) {
            SelectedStreamingQualityIndex = streamingQualitySelectorAdapter.getSelectedIndex();
        } else if (hlsStreamingQualitySelectorAdapter != null) {
            SelectedStreamingQualityIndex = hlsStreamingQualitySelectorAdapter.getSelectedIndex();
        } else {
            SelectedStreamingQualityIndex = -1;
        }
    }

    public SimpleItemRecyclerViewAdapter getSimpleItemRecyclerViewAdapter() {
        return simpleItemRecyclerViewAdapter;
    }

    private void setupRecyclerView(@NonNull RecyclerView recyclerView) {


        try {
            simpleItemRecyclerViewAdapter = new SimpleItemRecyclerViewAdapter(mContext, settingsItem, true, appCMSPresenter);

            LinearLayoutManager llm = new LinearLayoutManager(mContext);
            llm.setOrientation(LinearLayoutManager.VERTICAL);
            recyclerView.setLayoutManager(llm);
            recyclerView.setAdapter(simpleItemRecyclerViewAdapter);


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void updateSettingItems() {
        settingsItem.clear();

        if (getClosedCaptionSelectorAdapter() != null && getClosedCaptionSelectorAdapter().closedCaptionsList != null && getClosedCaptionSelectorAdapter().closedCaptionsList.size() > 1) {
            closedCaptionSelectorAdapter.setItemClickListener(item -> {
                SelectedClosedCaptionIndex = closedCaptionSelectorAdapter.getDownloadQualityPosition();
                closedCaptionSelectorAdapter.setSelectedIndex(SelectedClosedCaptionIndex);
                for (int i = 0; i < closedCaptionSelectorAdapter.closedCaptionsList.size(); i++) {
                    if (closedCaptionSelectorAdapter.closedCaptionsList.get(i).getLanguage().equalsIgnoreCase(closedCaptionSelectorAdapter.closedCaptionsList.get(SelectedClosedCaptionIndex).getLanguage())) {
                        closedCaptionSelectorAdapter.closedCaptionsList.get(i).setIsSelected(true);
                        //    appCMSPresenter.setClosedCaptionPreference(true);
                    } else {
                        closedCaptionSelectorAdapter.closedCaptionsList.get(i).setIsSelected(false);
                        //  appCMSPresenter.setClosedCaptionPreference(false);
                    }
                }
                appPreference.setPreferredSubtitleLanguage(closedCaptionSelectorAdapter.closedCaptionsList.get(SelectedClosedCaptionIndex).getLanguage());
            });

            // settingsItem.add(new PlayerSettingsContent("Closed Captions", closedCaptionSelectorAdapter));
            if (appCMSPresenter.getClosedCaptionPreference())
                settingsItem.add(new PlayerSettingsContent(localisedStrings.getClosedCaptionText(), closedCaptionSelectorAdapter));

        }
        if (getStreamingQualitySelectorAdapter() != null) {
            streamingQualitySelectorAdapter.setItemClickListener(new AppCMSDownloadRadioAdapter.ItemClickListener() {
                @Override
                public void onItemClick(Object item) {
                    SelectedStreamingQualityIndex = streamingQualitySelectorAdapter.getDownloadQualityPosition();
                    streamingQualitySelectorAdapter.setSelectedIndex(SelectedStreamingQualityIndex);
                }
            });
            /*streamingQualitySelectorAdapter.setItemClickListener(item -> {
                SelectedStreamingQualityIndex = streamingQualitySelectorAdapter.getDownloadQualityPosition();
                streamingQualitySelectorAdapter.setSelectedIndex(SelectedStreamingQualityIndex);
            });*/
            //settingsItem.add(new PlayerSettingsContent("Playback Quality", streamingQualitySelectorAdapter));
            settingsItem.add(new PlayerSettingsContent(localisedStrings.getPlaybackQualityText(), streamingQualitySelectorAdapter));
        }

        if (getHlsStreamingQualitySelectorAdapter() != null) {
            hlsStreamingQualitySelectorAdapter.setItemClickListener(item -> {
                SelectedStreamingQualityIndex = hlsStreamingQualitySelectorAdapter.getDownloadQualityPosition();
                hlsStreamingQualitySelectorAdapter.setSelectedIndex(SelectedStreamingQualityIndex);
            });
            //settingsItem.add(new PlayerSettingsContent("Playback Quality", hlsStreamingQualitySelectorAdapter));
            settingsItem.add(new PlayerSettingsContent(localisedStrings.getPlaybackQualityText(), hlsStreamingQualitySelectorAdapter));
        }
        if (getLanguageSelectorAdapter() != null) {
            languageSelectorAdapter.setItemClickListener(item -> {
                languageSelectorIndex = languageSelectorAdapter.getDownloadQualityPosition();
                languageSelectorAdapter.setSelectedIndex(languageSelectorIndex);
            });
            //settingsItem.add(new PlayerSettingsContent("Audio Language", languageSelectorAdapter));
            settingsItem.add(new PlayerSettingsContent(localisedStrings.getAudioLanguageText(), languageSelectorAdapter));
        }

        try {
            simpleItemRecyclerViewAdapter.notifyDataSetChanged();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static class SimpleItemRecyclerViewAdapter
            extends RecyclerView.Adapter<SimpleItemRecyclerViewAdapter.ViewHolder> {

        private final Context mParentActivity;
        private final List<PlayerSettingsContent> mValues;
        private final boolean mTwoPane;
        private int SELECTED_ITEM_POSITION;
        AppCMSPresenter appCMSPresenter;

        SimpleItemRecyclerViewAdapter(Context parent,
                                      List<PlayerSettingsContent> items,
                                      boolean twoPane, AppCMSPresenter appCMSPresenter) {
            mValues = items;
            mParentActivity = parent;
            mTwoPane = twoPane;
            this.appCMSPresenter = appCMSPresenter;
        }

        @Override
        public SimpleItemRecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_list_player_setting_master_content, parent, false);
            return new SimpleItemRecyclerViewAdapter.ViewHolder(view, appCMSPresenter);
        }

        @Override
        public void onBindViewHolder(final SimpleItemRecyclerViewAdapter.ViewHolder holder, int position) {
            holder.mContentView.setText(mValues.get(position).getSettingName());
            System.out.println(mValues.get(position).getSettingName());

            if (position == SELECTED_ITEM_POSITION) {
                if (mTwoPane) {
                    PlayerSettingContentListFragment fragment = PlayerSettingContentListFragment.newInstance(mValues.get(position).getPlayerSettingAdapter());
                    try {
                        if (mParentActivity instanceof AppCMSPlayVideoActivity) {
                            ((AppCMSPlayVideoActivity) mParentActivity).getSupportFragmentManager().beginTransaction()
                                    .replace(R.id.item_detail_container, fragment, mValues.get(position).getSettingName())
                                    .commit();
                        } else if (mParentActivity instanceof AppCMSPageActivity) {
                            ((AppCMSPageActivity) mParentActivity).getSupportFragmentManager().beginTransaction()
                                    .replace(R.id.item_detail_container, fragment, mValues.get(position).getSettingName())
                                    .commit();
                        }
                    } catch (Exception e) {
                        Log.e("TAG", "error"+e);
                    }
                    Component component=new Component();
                    component.setFontWeight(holder.mContentView.getContext().getString(R.string.app_cms_page_font_bold_key));
                    ViewCreator.setTypeFace(holder.mContentView.getContext(), appCMSPresenter, appCMSPresenter.getJsonValueKeyMap(), component, holder.mContentView);
                }
            } else {
                ViewCreator.setTypeFace(holder.mContentView.getContext(), appCMSPresenter, appCMSPresenter.getJsonValueKeyMap(), new Component(), holder.mContentView);
            }

            holder.mContentView.setTag(mValues.get(position));
            holder.mContentView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    SELECTED_ITEM_POSITION = position;
                    notifyDataSetChanged();
                }
            });
        }

        public void setSELECTED_ITEM_POSITION(int SELECTED_ITEM_POSITION) {
            this.SELECTED_ITEM_POSITION = SELECTED_ITEM_POSITION;
        }

        @Override
        public int getItemCount() {
            return mValues.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder {

            final AppCompatTextView mContentView;
            AppCMSPresenter appCMSPresenter;

            ViewHolder(View view, AppCMSPresenter appCMSPresenter) {
                super(view);
                this.appCMSPresenter = appCMSPresenter;
                mContentView = view.findViewById(R.id.content);
                mContentView.setTextColor(appCMSPresenter.getGeneralTextColor());
                //mContentView.setTextColor(Color.WHITE);
                ViewCreator.setTypeFace(mContentView.getContext(), appCMSPresenter, appCMSPresenter.getJsonValueKeyMap(), new Component(), mContentView);
            }
        }

    }

    public int getSelectedStreamingQualityIndex() {
        return SelectedStreamingQualityIndex;
    }

    public void setSelectedStreamingQualityIndex(int selectedStreamingQualityIndex) {
        SelectedStreamingQualityIndex = selectedStreamingQualityIndex;
    }

    public int getLanguageSelectorIndex() {
        return languageSelectorIndex;
    }

    public void setLanguageSelectorIndex(int languageSelectorIndex) {
        languageSelectorIndex = languageSelectorIndex;
    }

    public int getSelectedClosedCaptionIndex() {
        return SelectedClosedCaptionIndex;
    }

    public void setSelectedClosedCaptionIndex(int selectedClosedCaptionIndex) {
        SelectedClosedCaptionIndex = selectedClosedCaptionIndex;
    }

    public ClosedCaptionSelectorAdapter getClosedCaptionSelectorAdapter() {
        return closedCaptionSelectorAdapter;
    }

    public void setClosedCaptionSelectorAdapter(ClosedCaptionSelectorAdapter closedCaptionSelectorAdapter) {
        this.closedCaptionSelectorAdapter = closedCaptionSelectorAdapter;
    }

    public StreamingQualitySelectorAdapter getStreamingQualitySelectorAdapter() {
        return streamingQualitySelectorAdapter;
    }

    public void setStreamingQualitySelectorAdapter(StreamingQualitySelectorAdapter streamingQualitySelectorAdapter) {
        this.streamingQualitySelectorAdapter = streamingQualitySelectorAdapter;
    }

    public HLSStreamingQualitySelectorAdapter getHlsStreamingQualitySelectorAdapter() {
        return hlsStreamingQualitySelectorAdapter;
    }

    public void setHlsStreamingQualitySelectorAdapter(HLSStreamingQualitySelectorAdapter hlsStreamingQualitySelectorAdapter) {
        this.hlsStreamingQualitySelectorAdapter = hlsStreamingQualitySelectorAdapter;
    }

    public LanguageSelectorAdapter getLanguageSelectorAdapter() {
        return languageSelectorAdapter;
    }

    public void setLanguageSelectorAdapter(LanguageSelectorAdapter languageSelectorAdapter) {
        this.languageSelectorAdapter = languageSelectorAdapter;
    }

    @Override
    public void setVisibility(int visibility) {
        super.setVisibility(visibility);
    }

    public void setPlayerSettingsEvent(VideoPlayerView.VideoPlayerSettingsEvent playerSettingsEvent) {
        this.playerSettingsEvent = playerSettingsEvent;
    }

    public VideoPlayerView.VideoPlayerSettingsEvent getPlayerSettingsEvent() {
        return playerSettingsEvent;
    }

    public void clear(){
        languageSelectorAdapter  = null;
    }
}
