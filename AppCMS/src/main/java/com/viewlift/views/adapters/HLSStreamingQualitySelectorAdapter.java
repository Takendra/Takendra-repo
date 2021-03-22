package com.viewlift.views.adapters;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Build;
import android.text.TextUtils;
import android.view.ViewGroup;

import androidx.appcompat.widget.AppCompatRadioButton;

import com.viewlift.R;
import com.viewlift.models.data.appcms.ui.page.Component;
import com.viewlift.models.data.playersettings.HLSStreamingQuality;
import com.viewlift.presenters.AppCMSPresenter;
import com.viewlift.utils.CommonUtils;
import com.viewlift.views.customviews.ViewCreator;

import java.util.List;

public class HLSStreamingQualitySelectorAdapter extends AppCMSDownloadRadioAdapter<HLSStreamingQuality> {
    List<HLSStreamingQuality> availableStreamingQualities;
    int selectedIndex;
    AppCMSPresenter appCMSPresenter;
    Context mContext;
    boolean isContentTvod;
    boolean isTvodHdStreaming;

    public HLSStreamingQualitySelectorAdapter(Context context,
                                              AppCMSPresenter appCMSPresenter,
                                              List<HLSStreamingQuality> items, boolean isContentTvod,
                                              boolean isTvodHdStreaming) {
        super(context, items, appCMSPresenter,isContentTvod,isTvodHdStreaming);
        mContext = context;
        this.appCMSPresenter = appCMSPresenter;
        this.availableStreamingQualities = items;
        this.isContentTvod = isContentTvod;
        this.isTvodHdStreaming = isTvodHdStreaming;
        getDefaultSelectedQualityPosition();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        ViewHolder viewHolder = super.onCreateViewHolder(viewGroup, i);
        viewHolder.getmText().setTextColor(Color.parseColor(CommonUtils.getFocusBorderColor(mContext, appCMSPresenter)));


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (viewHolder.getmRadio().getButtonDrawable() != null) {
                viewHolder.getmRadio().getButtonDrawable().setColorFilter(appCMSPresenter.getBrandPrimaryCtaTextColor(), PorterDuff.Mode.MULTIPLY);
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
        viewHolder.getmText().setText(availableStreamingQualities.get(i).getValue());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && ((isContentTvod && !isTvodHdStreaming) || !appCMSPresenter.getAppPreference().isUserAllowedHDStreaming())) {
            if (availableStreamingQualities.get(i).getValue() != null) {
                int stremaingQuality = Integer.valueOf((availableStreamingQualities.get(i)).getValue().replace("p", ""));
                if (stremaingQuality >= 720) {
                    viewHolder.getmRadio().getButtonDrawable().setColorFilter(Color.parseColor(CommonUtils.getTextColor(mContext, appCMSPresenter)),
                            PorterDuff.Mode.MULTIPLY);
                    viewHolder.getmText().setTextColor(appCMSPresenter.getGeneralTextColor());
                } else {
                    viewHolder.getmRadio().getButtonDrawable().setColorFilter(Color.parseColor(appCMSPresenter.getAppCtaBackgroundColor()), PorterDuff.Mode.MULTIPLY);
                    viewHolder.getmText().setTextColor(Color.parseColor(appCMSPresenter.getAppCtaBackgroundColor()));
                }
            }
        }
        if (selectedIndex == i) {
            viewHolder.getmRadio().setChecked(true);
            viewHolder.getmRadio().requestFocus();
            Component component = new Component();
            component.setFontWeight(viewHolder.getmText().getContext().getString(R.string.app_cms_page_font_bold_key));
            ViewCreator.setTypeFace(viewHolder.getmText().getContext(), appCMSPresenter, appCMSPresenter.getJsonValueKeyMap(), component, viewHolder.getmText());
        } else {
            ViewCreator.setTypeFace(viewHolder.getmText().getContext(), appCMSPresenter, appCMSPresenter.getJsonValueKeyMap(), new Component(), viewHolder.getmText());
            viewHolder.getmRadio().setChecked(false);
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

    public static void setCheckBoxColor(AppCMSPresenter appCMSPresenter, AppCompatRadioButton radioButton) {

        String focusStateTextColor = null;
        String unFocusStateTextColor = null;
        if (null != appCMSPresenter &&
                null != appCMSPresenter.getAppCMSMain() &&
                null != appCMSPresenter.getAppCMSMain().getBrand() &&
                null != appCMSPresenter.getAppCMSMain().getBrand().getCta() &&
                null != appCMSPresenter.getAppCMSMain().getBrand().getCta().getPrimary()) {
            focusStateTextColor = appCMSPresenter.getAppCMSMain().getBrand().getCta().getPrimary().getTextColor();
            unFocusStateTextColor = appCMSPresenter.getAppCMSMain().getBrand().getCta().getSecondary().getTextColor();
        }

        ColorStateList colorStateList = new ColorStateList(
                new int[][]{
                        new int[]{-android.R.attr.state_checked}, // unchecked
                        new int[]{android.R.attr.state_checked}  // checked
                },
                new int[]{
                        Color.parseColor(unFocusStateTextColor),
                        Color.parseColor(focusStateTextColor)
                }
        );
        radioButton.setButtonTintList(colorStateList);
    }


    public void setPreSelectedQualityPosition() {
        if (appCMSPresenter.getAppPreference() != null) {
            String quality = appCMSPresenter.getAppPreference().getVideoStreamingQuality();
            if (!TextUtils.isEmpty(quality) && availableStreamingQualities != null) {
                for (int i = 0; i < availableStreamingQualities.size(); i++) {
                    if (quality.equalsIgnoreCase(availableStreamingQualities.get(i).getValue())) {
                        selectedIndex = i;
                        downloadQualityPosition = i;
                        if (itemClickListener != null)
                            itemClickListener.onItemClick(availableStreamingQualities.get(i));
                        break;
                    }
                }
            }
        }
    }

    public int getDefaultSelectedQualityPosition() {
        if ((isContentTvod && !isTvodHdStreaming) || !appCMSPresenter.getAppPreference().isUserAllowedHDStreaming()) {
            if (availableStreamingQualities != null && availableStreamingQualities.size() > 1) {
                try {
                    for (int i = 0; i < availableStreamingQualities.size(); i++) {
                        int streamingQuality = Integer.valueOf((availableStreamingQualities.get(i)).getValue().replace("p", ""));
                        if (streamingQuality >= 720) {
                            selectedIndex = i - 1;
                            break;
                        }
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }
        return selectedIndex;
    }
}