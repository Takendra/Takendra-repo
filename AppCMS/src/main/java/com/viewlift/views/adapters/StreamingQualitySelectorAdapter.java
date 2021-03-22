package com.viewlift.views.adapters;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.PorterDuff;
import android.os.Build;
import android.text.TextUtils;
import android.view.ViewGroup;

import com.viewlift.R;
import com.viewlift.models.data.appcms.ui.page.Component;
import com.viewlift.presenters.AppCMSPresenter;
import com.viewlift.views.customviews.ViewCreator;

import java.util.List;

public class StreamingQualitySelectorAdapter extends AppCMSDownloadRadioAdapter<String>  {
    List<String> availableStreamingQualities;
    int selectedIndex;
    AppCMSPresenter appCMSPresenter;

    public StreamingQualitySelectorAdapter(Context context,
                                           AppCMSPresenter appCMSPresenter,
                                           List<String> items) {
        super(context, items,appCMSPresenter);
        this.appCMSPresenter = appCMSPresenter;
        this.availableStreamingQualities = items;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        ViewHolder viewHolder = super.onCreateViewHolder(viewGroup, i);

        if (appCMSPresenter.getPlatformType() == AppCMSPresenter.PlatformType.TV) {
            viewHolder.getmText().setTextColor(appCMSPresenter.getGeneralTextColor());
        }else {
            viewHolder.getmText().setTextColor(appCMSPresenter.getGeneralTextColor());
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (viewHolder.getmRadio().getButtonDrawable() != null) {
                viewHolder.getmRadio().getButtonDrawable().setColorFilter(appCMSPresenter.getBrandPrimaryCtaColor(),PorterDuff.Mode.MULTIPLY);
                /*
                viewHolder.getmRadio().getButtonDrawable().setColorFilter(Color.parseColor(
                        ViewCreator.getColor(viewGroup.getContext(),
                                        appCMSPresenter.getAppCtaBackgroundColor())),
                        PorterDuff.Mode.MULTIPLY);*/
            }
        } else {
            int switchOnColor = appCMSPresenter.getBrandPrimaryCtaColor();
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

    @Override
    public void onBindViewHolder(AppCMSDownloadRadioAdapter.ViewHolder viewHolder, int i) {
        super.onBindViewHolder(viewHolder, i);
        viewHolder.getmText().setText(availableStreamingQualities.get(i));
        if (selectedIndex == i) {
            viewHolder.getmRadio().setChecked(true);
            viewHolder.getmRadio().requestFocus();
            Component component = new Component();
            component.setFontWeight(viewHolder.getmText().getContext().getString(R.string.app_cms_page_font_bold_key));
            ViewCreator.setTypeFace(viewHolder.getmText().getContext(), appCMSPresenter, appCMSPresenter.getJsonValueKeyMap(), component, viewHolder.getmText());
        } else {
            viewHolder.getmRadio().setChecked(false);
            ViewCreator.setTypeFace(viewHolder.getmText().getContext(), appCMSPresenter, appCMSPresenter.getJsonValueKeyMap(), new Component(), viewHolder.getmText());
        }
        viewHolder.getmRadio().invalidate();
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

    public void setPreSelectedQualityPosition() {
        if (appCMSPresenter.getAppPreference() != null) {
            String quality = appCMSPresenter.getAppPreference().getVideoStreamingQuality();
            if (!TextUtils.isEmpty(quality) && availableStreamingQualities != null) {
                for(int i = 0; i < availableStreamingQualities.size(); i++) {
                    if (quality.equalsIgnoreCase(availableStreamingQualities.get(i))) {
                        selectedIndex = i;
                        break;
                    }
                }
            }
        }
    }
}
