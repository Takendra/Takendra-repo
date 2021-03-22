package com.viewlift.tv.views.presenter;

import android.content.Context;
import android.graphics.Color;
import androidx.leanback.widget.Presenter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.viewlift.R;
import com.viewlift.models.data.appcms.ui.page.Component;
import com.viewlift.models.data.appcms.ui.page.ModuleList;
import com.viewlift.presenters.AppCMSPresenter;
import com.viewlift.tv.model.SeeAllCard;
import com.viewlift.tv.utility.Utils;

import java.util.ArrayList;

public class SeeAllCardPresenter extends Presenter {

    private final Context context;
    private final AppCMSPresenter appCMSPresenter;
    private Component thumbnailImageComponent;

    public SeeAllCardPresenter(Context context, ModuleList moduleUI, AppCMSPresenter appCMSPresenter) {
        this.context = context;
        this.appCMSPresenter = appCMSPresenter;
        findThumbnailImageComponent(moduleUI.getComponents());
    }

    /**
     * A recursive call to find thumbnail image component from UI JSON, which is then used for the
     * height and width of the "See All" card to make it consistent with rest of the trays.
     * @param components components in the tray modules
     */
    private void findThumbnailImageComponent(ArrayList<Component> components) {
        for (Component component : components) {
            if (component.getComponents() != null && component.getComponents().size() > 0) {
                findThumbnailImageComponent(component.getComponents());
            } else {
                if (component.getKey()!=null && component.getKey().equalsIgnoreCase("thumbnailImage")) {
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

        View view = inflater.inflate(R.layout.see_all_card_layout, null);

        if (thumbnailImageComponent != null) {
            RelativeLayout.LayoutParams layoutParams =
                    new RelativeLayout.LayoutParams(
                            Integer.parseInt(thumbnailImageComponent.getLayout().getTv().getWidth()),
                            Integer.parseInt(thumbnailImageComponent.getLayout().getTv().getHeight()));
            int padding = 2 * Integer.parseInt(thumbnailImageComponent.getLayout().getTv().getPadding());
            view.setPadding(padding, padding, padding, padding);
            layoutParams.setMargins(padding, padding, padding, padding);

            view.setLayoutParams(layoutParams);
        }

        return new SeeAllCardViewHolder(view);
    }
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, Object item) {
        if (viewHolder instanceof SeeAllCardViewHolder && item instanceof SeeAllCard) {
            ((SeeAllCardViewHolder) viewHolder).seeAllTextView.setText(((SeeAllCard) item).getModuleAPI().getTitle());
        }
    }

    @Override
    public void onUnbindViewHolder(ViewHolder viewHolder) {

    }

    private class SeeAllCardViewHolder extends ViewHolder {

        TextView seeAllTextView;
        TextView seeAllText;

        public SeeAllCardViewHolder(View view) {
            super(view);
            seeAllText = view.findViewById(R.id.seeAllText);
            this.seeAllText.setText(appCMSPresenter.getLocalisedStrings().getSeeAllTray());

            seeAllTextView = view.findViewById(R.id.seeAllTextView);
            seeAllTextView.setTextColor(Color.parseColor(appCMSPresenter.getAppTextColor()));
            seeAllText.setTextColor(Color.parseColor(appCMSPresenter.getAppTextColor()));
            view.setBackground(Utils.getTrayBorder(
                    context,
                    Utils.getFocusColor(context, appCMSPresenter),
                    thumbnailImageComponent));
        }
    }
}
