package com.viewlift.tv.views.presenter;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import androidx.leanback.widget.Presenter;

import com.viewlift.R;
import com.viewlift.models.data.appcms.ui.page.Component;
import com.viewlift.models.data.appcms.ui.page.ModuleList;
import com.viewlift.presenters.AppCMSPresenter;

import java.util.ArrayList;

import io.supercharge.shimmerlayout.ShimmerLayout;

public class BrowseFragmentShimmerCardPresenter extends Presenter {

    private final String TAG = BrowseFragmentShimmerCardPresenter.class.getSimpleName();
    private final AppCMSPresenter appCMSPresenter;
    private Component thumbnailImageComponent;

    public BrowseFragmentShimmerCardPresenter(ModuleList moduleUI, AppCMSPresenter appCMSPresenter) {
        findThumbnailImageComponent(moduleUI.getComponents());
        this.appCMSPresenter = appCMSPresenter;
    }

    /**
     * A recursive call to find thumbnail image component from UI JSON, which is then used for the
     * height and width of the "See All" card to make it consistent with rest of the trays.
     *
     * @param components components in the tray modules
     */
    private void findThumbnailImageComponent(ArrayList<Component> components) {
        for (Component component : components) {
            if (component.getComponents() != null && component.getComponents().size() > 0) {
                findThumbnailImageComponent(component.getComponents());
            } else {
                if (component.getKey() != null)
                    if (component.getKey().equalsIgnoreCase("thumbnailImage")
                            ||component.getKey().equalsIgnoreCase("wideCarouselImage")
                            ||component.getKey().equalsIgnoreCase("thumbnailImageTall")) {
                        thumbnailImageComponent = component;
                        break;
                    }
            }
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) parent.getContext()
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.browse_fragment_shimmer_card_layout, null);

        ShimmerLayout shimmerLayout = view.findViewById(R.id.shimmer_layout);
        LinearLayout shimmerLinearLayout = view.findViewById(R.id.shimmer_linear_layout);

        if (thumbnailImageComponent != null) {
            int width = Integer.parseInt(thumbnailImageComponent.getLayout().getTv().getWidth());
            int height = Integer.parseInt(thumbnailImageComponent.getLayout().getTv().getHeight());
            int numberOfAssets = getNumberOfAssets(width);

            RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, height);
            if (thumbnailImageComponent.getLayout().getTv().getPadding() != null) {
                int padding = 2 * Integer.parseInt(thumbnailImageComponent.getLayout().getTv().getPadding());
                view.setPadding(padding, padding, padding, padding);
                layoutParams.setMargins(padding, padding, padding, padding);
            }
            view.setLayoutParams(layoutParams);
            for (int i = 0; i < Math.max(numberOfAssets - 1, 1); i++) {
                shimmerLinearLayout.addView(assetView(width, height));
            }
        }
        shimmerLayout.startShimmerAnimation();
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, Object item) {

    }

    @Override
    public void onUnbindViewHolder(ViewHolder viewHolder) {

    }

    private LinearLayout assetView (int width, int height){
        LinearLayout linearLayout = new LinearLayout(appCMSPresenter.getCurrentContext());
        FrameLayout.LayoutParams frameLayoutParams = new FrameLayout.LayoutParams(width, height);
        frameLayoutParams.rightMargin = 20;
        LinearLayout.LayoutParams linearLayoutParams = new LinearLayout.LayoutParams(width, height);
        linearLayout.setLayoutParams(frameLayoutParams);

        View imageView = new View(appCMSPresenter.getCurrentContext());
        imageView.setBackgroundColor(appCMSPresenter.getBrandPrimaryCtaColor());
        imageView.setAlpha(0.5f);
        imageView.setLayoutParams(linearLayoutParams);
        linearLayout.addView(imageView);
        return linearLayout;
    }

    private int getNumberOfAssets (int assetWidth) {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        appCMSPresenter.getCurrentActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int width = displayMetrics.widthPixels;
        return width/assetWidth;
    }
}
