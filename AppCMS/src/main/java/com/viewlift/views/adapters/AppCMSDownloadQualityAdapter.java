package com.viewlift.views.adapters;

import android.content.Context;
import android.graphics.Color;
import android.text.TextUtils;

import com.viewlift.AppCMSApplication;
import com.viewlift.db.AppPreference;
import com.viewlift.models.data.appcms.api.BaseInterface;
import com.viewlift.models.data.appcms.api.Language;
import com.viewlift.models.data.appcms.api.MetadataMap;
import com.viewlift.models.data.appcms.api.Mpeg;
import com.viewlift.models.data.appcms.ui.AppCMSUIKeyType;
import com.viewlift.models.data.appcms.ui.page.Component;
import com.viewlift.presenters.AppCMSPresenter;
import com.viewlift.utils.CommonUtils;
import com.viewlift.utils.LanguageHelper;
import com.viewlift.views.customviews.ViewCreator;

import java.util.List;
import java.util.Map;

import javax.inject.Inject;

/**
 * Created by sandeep.singh on 7/28/2017.
 */

public class AppCMSDownloadQualityAdapter extends AppCMSDownloadRadioAdapter<BaseInterface> {
    protected List<Component> components;
    @Inject
    AppPreference appPreference;
    @Inject
    AppCMSPresenter appCMSPresenter;
    protected Map<String, AppCMSUIKeyType> jsonValueKeyMap;

    private MetadataMap metadataMap;

    public AppCMSDownloadQualityAdapter(Context context, List<BaseInterface> items, List<Component> components,
                                        AppCMSPresenter appCMSPresenter,
                                        Map<String, AppCMSUIKeyType> jsonValueKeyMap, MetadataMap metadataMap) {
        super(context, items, appCMSPresenter);
        ((AppCMSApplication) context.getApplicationContext()).getAppCMSPresenterComponent().inject(this);
        this.components = components;
        this.jsonValueKeyMap = jsonValueKeyMap;
        this.tintColor = appCMSPresenter.getBrandPrimaryCtaColor();
        this.metadataMap = metadataMap;
        if (appPreference.getUserDownloadQualityPref() != null || appCMSPresenter.getLanguage() != null) {
            for (int i = 0; i < items.size(); i++) {
                if (items.get(i) instanceof Mpeg) {
                    Mpeg mpeg = (Mpeg) items.get(i);
                    this.downloadQualityPosition = appPreference.getUserDownloadPositionQualityPref();
                } else if (items.get(i) instanceof Language) {
                    Language language = (Language) items.get(i);
                    if (language.getLanguageCode().equals(appCMSPresenter
                            .getLanguage().getLanguageCode())) {
                        this.downloadQualityPosition = i;
                    }
                }
            }
        }
    }

    @Override
    public void onBindViewHolder(AppCMSDownloadRadioAdapter.ViewHolder viewHolder, int i) {
        super.onBindViewHolder(viewHolder, i);
        String name = "";
        if (mItems.get(i) instanceof Mpeg) {
            name = ((Mpeg) mItems.get(i)).getRenditionValue();
//            if (metadataMap != null) {
//                if ("360p".equalsIgnoreCase(name) && !TextUtils.isEmpty(metadataMap.getDataSaverLabel())) {
//                    name = metadataMap.getDataSaverLabel() + " (360p)";
//                } else if ("720p".equalsIgnoreCase(name) && !TextUtils.isEmpty(metadataMap.getGoodQualityLabel())) {
//                    name = metadataMap.getGoodQualityLabel() + " (720p)";
//                } else if ("1080p".equalsIgnoreCase(name) && !TextUtils.isEmpty(metadataMap.getBestQualityLabel())) {
//                    name = metadataMap.getBestQualityLabel() + " (1080p)";
//                }
//            }
        } else if (mItems.get(i) instanceof Language) {
            /*name = TextUtils.isEmpty(((Language) mItems.get(i)).getLocalizedTitle()) ?
                    LanguageHelper.getLanguageName(((Language) mItems.get(i)).getLanguageCode()) :
                    ((Language) mItems.get(i)).getLocalizedTitle();*/
            name = LanguageHelper.getLanguageName(((Language) mItems.get(i)).getLanguageCode());
        }
        viewHolder.mText.setText(name);
        applyStyles(viewHolder);

    }

    private void applyStyles(AppCMSDownloadRadioAdapter.ViewHolder viewHolder) {
        for (Component component : components) {
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
                case PAGE_TEXTVIEW_KEY:
                    // int textColor = ContextCompat.getColor(viewHolder.itemView.getContext(), R.color.colorAccent);
                    int textColor = appCMSPresenter.getGeneralTextColor();
                    viewHolder.mText.setTextColor(textColor);
                    if (!TextUtils.isEmpty(component.getTextColor())) {
                        textColor = Color.parseColor(CommonUtils.getColor(viewHolder.itemView.getContext(), component.getTextColor()));
                    } else if (component.getStyles() != null) {
                        if (!TextUtils.isEmpty(component.getStyles().getColor())) {
                            textColor = Color.parseColor(CommonUtils.getColor(viewHolder.itemView.getContext(), component.getStyles().getColor()));
                        } else if (!TextUtils.isEmpty(component.getStyles().getTextColor())) {
                            textColor =
                                    Color.parseColor(CommonUtils.getColor(viewHolder.itemView.getContext(), component.getStyles().getTextColor()));
                        }
                    }
                    switch (componentKey) {
                        case PAGE_API_TITLE:

                            if (!TextUtils.isEmpty(component.getBackgroundColor())) {
                                viewHolder.mText.setBackgroundColor(Color.parseColor(CommonUtils.getColor(viewHolder.itemView.getContext(), component.getBackgroundColor())));
                            }
                            viewHolder.mText.setBackgroundColor(Color.parseColor("#00000000"));
                                ViewCreator.setTypeFace(viewHolder.itemView.getContext(),
                                        appCMSPresenter,
                                        jsonValueKeyMap,
                                        component,
                                        viewHolder.mText);
                            if (component.getFontSize() != 0) {
                                viewHolder.mText.setTextSize(component.getFontSize());
                            }

                            break;

                        default:
                    }

                    break;
            }


        }

    }

}
