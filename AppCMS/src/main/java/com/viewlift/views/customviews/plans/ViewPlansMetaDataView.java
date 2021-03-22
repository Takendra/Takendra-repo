package com.viewlift.views.customviews.plans;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.viewlift.R;
import com.viewlift.models.data.appcms.api.ContentDatum;
import com.viewlift.models.data.appcms.api.FeatureDetail;
import com.viewlift.models.data.appcms.api.Module;
import com.viewlift.models.data.appcms.ui.AppCMSUIKeyType;
import com.viewlift.models.data.appcms.ui.page.Component;
import com.viewlift.models.data.appcms.ui.page.Layout;
import com.viewlift.models.data.appcms.ui.page.Settings;
import com.viewlift.presenters.AppCMSPresenter;
import com.viewlift.views.customviews.ViewCreator;

import java.util.List;
import java.util.Map;

@SuppressLint("ViewConstructor")
@SuppressWarnings("FieldCanBeLocal, unused")
public class ViewPlansMetaDataView extends LinearLayout {

    private static int viewCreationPlanDetailsIndex = 0;

    private final Component component;
    private final Layout layout;

    private final ViewCreator viewCreator;
    private final Module moduleAPI;
    private final Map<String, AppCMSUIKeyType> jsonValueKeyMap;
    private final AppCMSPresenter appCMSPresenter;
    private final int planDetailsIndex;
    private final Settings moduleSettings;
    private ContentDatum planData;

    public ViewPlansMetaDataView(Context context,
                                 Component component,
                                 Layout layout,
                                 ViewCreator viewCreator,
                                 Module moduleAPI,
                                 Map<String, AppCMSUIKeyType> jsonValueKeyMap,
                                 AppCMSPresenter appCMSPresenter,
                                 Settings moduleSettings) {
        super(context);
        this.component = component;
        this.layout = layout;
        this.viewCreator = viewCreator;
        this.moduleAPI = moduleAPI;
        this.jsonValueKeyMap = jsonValueKeyMap;
        this.appCMSPresenter = appCMSPresenter;
        this.moduleSettings = moduleSettings;
        if (moduleAPI.getContentData() != null) {
            planDetailsIndex = viewCreationPlanDetailsIndex % moduleAPI.getContentData().size();
        } else {
            planDetailsIndex = -1;
        }

        viewCreationPlanDetailsIndex++;
        init();
    }

    public void init() {
        FrameLayout.LayoutParams layoutParams =
                new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT);
        setLayoutParams(layoutParams);
    }

    public void setData(ContentDatum planData) {
        this.planData = planData;
        initViews();
    }

    private void initViews() {
        if (planData != null) {
            /**
             * comment due to MOTVWA-870
             */
            if (planData.getPlanDetails() != null &&
                    planData.getPlanDetails().size() > 0 &&
                    planData.getPlanDetails().get(0) != null &&
                    planData.getPlanDetails().get(0).getFeatureDetails() != null &&
                    planData.getPlanDetails().get(0).getFeatureDetails().size() > 0 &&
                    (planData.getPlanDetails().get(0).getFeatureDetails().get(0).getValue().equalsIgnoreCase("true") ||
                    planData.getPlanDetails().get(0).getFeatureDetails().get(0).getValue().equalsIgnoreCase("Y")  )
            ) {
                List<FeatureDetail> featureDetails =
                        planData.getPlanDetails()
                                .get(0)
                                .getFeatureDetails();
                createPlanDetailsWithFeatures(featureDetails);
            } else if (planData.getPlanDetails() != null &&
                    planData.getPlanDetails().size() > 0 &&
                    planData.getPlanDetails().get(0) != null &&
                    planData.getPlanDetails().get(0).getDescription() != null &&
                    !TextUtils.isEmpty(planData.getPlanDetails().get(0).getDescription())) {
                createPlanDescription(planData.getPlanDetails().get(0).getDescription());
            } else if (planData != null &&
                    planData.getDescription() != null) {
                createPlanDescription(planData.getDescription());
            }
        }
    }

    private void createPlanDescription(String description) {
        removeAllViews();
        LinearLayout planLayout = new LinearLayout(getContext());
        LinearLayout.LayoutParams layoutParams =
                new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT);
        planLayout.setLayoutParams(layoutParams);
        layoutParams.setMargins(5, 5, 8, 0);

        planLayout.setOrientation(LinearLayout.HORIZONTAL);

        TextView featureDetailText = new TextView(getContext());
        featureDetailText.setTextColor(Color.parseColor(appCMSPresenter.getAppCtaTextColor()));

        featureDetailText.setSingleLine(false);
        featureDetailText.setMaxLines(6);
        featureDetailText.setEllipsize(TextUtils.TruncateAt.END);

        featureDetailText.setText(description);
        planLayout.addView(featureDetailText);

        addView(planLayout);
    }

    private void createPlanDetailsWithFeatures(List<FeatureDetail> featureDetails) {
        removeAllViews();
        LinearLayout planLayout = new LinearLayout(getContext());
        planLayout.setId(R.id.planLayout);
        LinearLayout.LayoutParams layoutParams =
                new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT);
        planLayout.setLayoutParams(layoutParams);
        planLayout.setOrientation(LinearLayout.HORIZONTAL);

        TextView featureDetailText = getCommaSeparatedFeatureDetail(featureDetails);

        planLayout.addView(featureDetailText);

        addView(planLayout);
    }

    @NonNull
    private TextView getCommaSeparatedFeatureDetail(List<FeatureDetail> featureDetails) {
        TextView featureDetailText = new TextView(getContext());
        featureDetailText.setId(R.id.featureDetailText);
        featureDetailText.setMaxLines(2);
        featureDetailText.setEllipsize(TextUtils.TruncateAt.END);
        featureDetailText.setTextColor(Color.parseColor(appCMSPresenter.getAppTextColor()));

        for (int featureDetailsIndex = 0; featureDetailsIndex < featureDetails.size();
             featureDetailsIndex++) {

            if (!TextUtils.isEmpty(featureDetails.get(featureDetailsIndex).getValueType())) {
                FeatureDetail featureDetail = featureDetails.get(featureDetailsIndex);
                if (!TextUtils.isEmpty(featureDetail.getValue()) &&
                        ("true".equalsIgnoreCase(featureDetail.getValue())
                                || "Y".equalsIgnoreCase(featureDetail.getValue())
                        )) {
                    featureDetailText.append(featureDetail.getTextToDisplay());

                    if (featureDetailsIndex < featureDetails.size() - 1) {
                        featureDetailText.append(", ");
                    }
                }
            }
        }
        return featureDetailText;
    }
}
