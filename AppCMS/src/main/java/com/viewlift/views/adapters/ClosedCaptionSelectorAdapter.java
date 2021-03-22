package com.viewlift.views.adapters;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Build;
import android.util.Log;
import android.view.ViewGroup;

import com.viewlift.R;
import com.viewlift.models.data.appcms.api.ClosedCaptions;
import com.viewlift.models.data.appcms.ui.page.Component;
import com.viewlift.presenters.AppCMSPresenter;
import com.viewlift.utils.CommonUtils;
import com.viewlift.views.customviews.ViewCreator;

import java.util.List;

public class ClosedCaptionSelectorAdapter extends AppCMSDownloadRadioAdapter<ClosedCaptions> {
    private Context context;
    public List<ClosedCaptions> closedCaptionsList;
    int selectedIndex;
    AppCMSPresenter appCMSPresenter;

    public ClosedCaptionSelectorAdapter(Context context,
                                        AppCMSPresenter appCMSPresenter,
                                        List<ClosedCaptions> items) {
        super(context, items, appCMSPresenter);
        this.context = context;
        this.appCMSPresenter = appCMSPresenter;
        this.closedCaptionsList = items;
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
            if (appCMSPresenter.getPlatformType().equals(AppCMSPresenter.PlatformType.ANDROID)) {
                viewHolder.getmRadio().getButtonDrawable().setColorFilter(Color.parseColor(appCMSPresenter.getAppCtaBackgroundColor()), PorterDuff.Mode.MULTIPLY);
            } else {
                viewHolder.getmRadio().getButtonDrawable().setColorFilter(appCMSPresenter.getBrandPrimaryCtaTextColor(), PorterDuff.Mode.MULTIPLY);
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
    public void onBindViewHolder(AppCMSDownloadRadioAdapter.ViewHolder viewHolder, int position) {
        super.onBindViewHolder(viewHolder, position);
        ClosedCaptions closedCaptions = closedCaptionsList.get(position);
        viewHolder.getmText().setText(closedCaptions.getLanguage());
        viewHolder.getmRadio().setChecked(selectedIndex == position);
        Log.e("SelectedTrack", "CC Name :" + closedCaptions.getLanguage() + ", Selected Index :" + selectedIndex);
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
        return closedCaptionsList == null ? 0 : closedCaptionsList.size();
    }
}