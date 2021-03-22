package com.viewlift.views.adapters;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Build;
import android.view.ViewGroup;

import com.viewlift.models.data.appcms.ui.CCFontSize;
import com.viewlift.presenters.AppCMSPresenter;
import com.viewlift.utils.CommonUtils;

import java.util.List;

public class ClosedCaptionSizeSelectorAdapter extends AppCMSDownloadRadioAdapter<CCFontSize> {
    private final Context context;
    private List<CCFontSize> textSizes;
    int selectedIndex;
    private AppCMSPresenter appCMSPresenter;

    public ClosedCaptionSizeSelectorAdapter(Context context,
                                            AppCMSPresenter appCMSPresenter,
                                            List<CCFontSize> items) {
        super(context, items, appCMSPresenter);
        this.appCMSPresenter = appCMSPresenter;
        this.context = context;
        this.textSizes = items;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        ViewHolder viewHolder = super.onCreateViewHolder(viewGroup, i);
        if (appCMSPresenter.getPlatformType() == AppCMSPresenter.PlatformType.TV) {
            viewHolder.getmText().setTextColor(Color.parseColor(CommonUtils.getFocusBorderColor(context, appCMSPresenter)));
        } else {
            viewHolder.getmText().setTextColor(appCMSPresenter.getGeneralTextColor());
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (viewHolder.getmRadio().getButtonDrawable() != null) {
                //viewHolder.getmRadio().getButtonDrawable().setColorFilter(Color.parseColor(CommonUtils.getFocusBorderColor(context,appCMSPresenter)),PorterDuff.Mode.MULTIPLY);
                if (appCMSPresenter.getPlatformType().equals(AppCMSPresenter.PlatformType.ANDROID)) {
                    viewHolder.getmRadio().getButtonDrawable().setColorFilter(Color.parseColor(appCMSPresenter.getAppCtaBackgroundColor()), PorterDuff.Mode.MULTIPLY);
                } else {
                    viewHolder.getmRadio().getButtonDrawable().setColorFilter(appCMSPresenter.getBrandPrimaryCtaTextColor(), PorterDuff.Mode.MULTIPLY);
                }

            }
        } else {
            int switchOnColor = appCMSPresenter.getBrandPrimaryCtaTextColor();
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

    @Override
    public void onBindViewHolder(AppCMSDownloadRadioAdapter.ViewHolder viewHolder, int i) {

        super.onBindViewHolder(viewHolder, i);
        viewHolder.getmText().setText(textSizes.get(i).getLabel());
        if (selectedIndex == i) {
            viewHolder.getmRadio().setChecked(true);
            viewHolder.getmRadio().requestFocus();
        } else {
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

    public void setPreSelectedFontPosition() {
        if (appCMSPresenter.getAppPreference() != null) {
            float textSize = appCMSPresenter.getAppPreference().getPreferredSubtitleTextSize(CommonUtils.getPlayerDefaultFontSize(appCMSPresenter));
            if (textSizes != null) {
                for (int i = 0; i < textSizes.size(); i++) {
                    if (textSize == textSizes.get(i).getSize()) {
                        selectedIndex = i;
                        break;
                    }
                }
            }
        }
    }
}
