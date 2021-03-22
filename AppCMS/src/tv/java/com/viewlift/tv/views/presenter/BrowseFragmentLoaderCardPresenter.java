package com.viewlift.tv.views.presenter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import androidx.core.content.ContextCompat;
import androidx.leanback.widget.Presenter;

import com.viewlift.R;
import com.viewlift.models.data.appcms.ui.page.Component;
import com.viewlift.models.data.appcms.ui.page.ModuleList;
import com.viewlift.presenters.AppCMSPresenter;

import java.util.ArrayList;

public class BrowseFragmentLoaderCardPresenter extends Presenter {

    private final AppCMSPresenter appCMSPresenter;
    private Component thumbnailImageComponent;

    public BrowseFragmentLoaderCardPresenter(ModuleList moduleUI, AppCMSPresenter appCMSPresenter) {
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

        View view = inflater.inflate(R.layout.browse_fragment_loader_card_layout, null);

        if (thumbnailImageComponent != null) {
            RelativeLayout.LayoutParams layoutParams =
                    new RelativeLayout.LayoutParams(
                            1900,
                            Integer.parseInt(thumbnailImageComponent.getLayout().getTv().getHeight()));
            int padding = 2 * Integer.parseInt(thumbnailImageComponent.getLayout().getTv().getPadding());
            view.setPadding(padding, padding, padding, padding);
            layoutParams.setMargins(padding, padding, padding, padding);
            view.setLayoutParams(layoutParams);
        }

        ProgressBar progressbar = view.findViewById(R.id.progress_bar);
        try {
            progressbar.getIndeterminateDrawable().setTint(Color.parseColor(appCMSPresenter.getAppCMSMain()
                    .getBrand().getCta().getPrimary().getBackgroundColor()));
        } catch (Exception e) {
//                //Log.w(TAG, "Failed to set color for loader: " + e.getMessage());
            progressbar.getIndeterminateDrawable().setTint(ContextCompat.getColor(appCMSPresenter.getCurrentContext(), R.color.colorAccent));
        }

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, Object item) {

    }

    @Override
    public void onUnbindViewHolder(ViewHolder viewHolder) {

    }
}
