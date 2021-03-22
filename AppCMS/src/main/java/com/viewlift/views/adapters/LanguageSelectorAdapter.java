package com.viewlift.views.adapters;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.PorterDuff;
import android.os.Build;
import android.view.ViewGroup;

import com.viewlift.presenters.AppCMSPresenter;

import java.util.List;

public class LanguageSelectorAdapter extends AppCMSDownloadRadioAdapter<String> {
    List<String> availableLanguages;
    int selectedIndex;
    AppCMSPresenter appCMSPresenter;

    public LanguageSelectorAdapter(Context context,
                                   AppCMSPresenter appCMSPresenter,
                                   List<String> items) {
        super(context, items, appCMSPresenter);
        this.appCMSPresenter = appCMSPresenter;
        this.availableLanguages = items;
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
/*
                viewHolder.getmRadio().getButtonDrawable().setColorFilter(Color.parseColor(
                        ViewCreator.getColor(viewGroup.getContext(),
                                        appCMSPresenter.getAppCtaBackgroundColor())),
                        PorterDuff.Mode.MULTIPLY);*/
            }
        } else {
            int switchOnColor = appCMSPresenter.getBrandPrimaryCtaColor();
            /*int switchOnColor = Color.parseColor(
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
        /*if(i == 0){
            viewHolder.getmText().setText("Default Audio");
        } else {
            viewHolder.getmText().setText(availableLanguages.get(i));
        }*/
        viewHolder.getmText().setText(availableLanguages.get(i));
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

    public List<String> getAvailableLanguages() {
        return availableLanguages;
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
}
