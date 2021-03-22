package com.viewlift.views.adapters;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.PorterDuff;
import android.os.Build;
import android.text.TextUtils;
import android.util.Log;
import android.view.ViewGroup;

import com.google.android.exoplayer2.Format;
import com.viewlift.R;
import com.viewlift.models.data.appcms.ui.page.Component;
import com.viewlift.presenters.AppCMSPresenter;
import com.viewlift.views.customviews.ViewCreator;

import java.util.List;

public class AudioSelectorAdapter extends AppCMSDownloadRadioAdapter<Format> {
    public List<Format> audioList;
    int selectedIndex;
    AppCMSPresenter appCMSPresenter;

    public AudioSelectorAdapter(Context context,
                                AppCMSPresenter appCMSPresenter,
                                List<Format> items) {
        super(context, items, appCMSPresenter);
        this.appCMSPresenter = appCMSPresenter;
        this.audioList = items;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        ViewHolder viewHolder = super.onCreateViewHolder(viewGroup, i);
        if (appCMSPresenter.getPlatformType() == AppCMSPresenter.PlatformType.TV) {
            viewHolder.getmText().setTextColor(appCMSPresenter.getBrandPrimaryCtaColor());
        } else {
            viewHolder.getmText().setTextColor(appCMSPresenter.getGeneralTextColor());
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (viewHolder.getmRadio().getButtonDrawable() != null) {
                viewHolder.getmRadio().getButtonDrawable().setColorFilter(
                        appCMSPresenter.getBrandPrimaryCtaColor(), PorterDuff.Mode.MULTIPLY);


            }
        } else {
            int switchOnColor = appCMSPresenter.getBrandPrimaryCtaColor();
           /* int switchOnColor = Color.parseColor(
                    ViewCreator.getColor(viewGroup.getContext(),
                            appCMSPresenter.getAppCtaBackgroundColor()));*/
            ColorStateList colorStateList = new ColorStateList(
                    new int[][]{
                            new int[]{android.R.attr.state_checked},
                            new int[]{}
                    }, new int[]{
                    switchOnColor,
                    switchOnColor
            });

            viewHolder.getmRadio().setButtonTintList(colorStateList);
        }
        return viewHolder;
    }

    /*@Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {
        super.onBindViewHolder(viewHolder, position);

        Format audioName = audioList.get(position);
        viewHolder.getmText().setText(audioName.id);
        viewHolder.getmRadio().setChecked(selectedIndex == position);
        Log.e("SelectedTrack", "Audio Name :" + audioName + ", Selected Index :" + selectedIndex);
    }*/
    @Override
    public void onBindViewHolder(AppCMSDownloadRadioAdapter.ViewHolder viewHolder, int position) {
        super.onBindViewHolder(viewHolder, position);

        Format closedCaptions = audioList.get(position);
        String audioLable = (closedCaptions.label != null && !TextUtils.isEmpty(closedCaptions.label)) ?
                closedCaptions.label :
                closedCaptions.language;
        viewHolder.getmText().setText(audioLable);
        viewHolder.getmRadio().setChecked(selectedIndex == position);
        Log.e("SelectedTrack", "CC Name :" + closedCaptions.id + ", Selected Index :" + selectedIndex);
        if (selectedIndex == position) {
            Component component = new Component();
            component.setFontWeight(viewHolder.getmText().getContext().getString(R.string.app_cms_page_font_bold_key));
            ViewCreator.setTypeFace(viewHolder.getmText().getContext(), appCMSPresenter, appCMSPresenter.getJsonValueKeyMap(), component, viewHolder.getmText());
        } else {
            ViewCreator.setTypeFace(viewHolder.getmText().getContext(), appCMSPresenter, appCMSPresenter.getJsonValueKeyMap(), new Component(), viewHolder.getmText());
        }
    }

    @Override
    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    public void setSelectedIndex(int selectedIndex) {
        this.selectedIndex = selectedIndex;
    }

    public int getSelectedIndex() {
        return selectedIndex;
    }

    public int getDownloadQualityPosition() {
        return downloadQualityPosition;
    }

    @Override
    public int getItemCount() {
        return audioList == null ? 0 : audioList.size();
    }
}