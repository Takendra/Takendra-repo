package com.viewlift.views.customviews;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.VectorDrawable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.viewlift.R;
import com.viewlift.models.data.appcms.ui.page.Component;
import com.viewlift.models.data.appcms.ui.page.ModuleList;
import com.viewlift.presenters.AppCMSPresenter;
import com.viewlift.views.activity.AppCMSPageActivity;

/**
 * Created by viewlift on 5/26/17.
 */

public class NavBarItemView extends LinearLayoutCompat {
    private static final String TAG = "NavBarItemView";
    private AppCompatImageView navImage;
    private AppCompatTextView navLabel;
    private String tag;
    private boolean hasFocus;
    private int highlightColor;
    private String HOME_TAB_ICON_KEY = "icon-home";
    private String SHOW_TAB_ICON_KEY = "icon-grid";
    private String LIVE_TAB_ICON_KEY = "icon-live";
    private String TEAM_TAB_ICON_KEY = "icon-bracket";
    private String MENU_TAB_ICON_KEY = "icon-menu";
    private String SEARCH_TAB_ICON_KEY = "icon-search";
    private ModuleList navTabBar;
    private AppCMSPresenter appCMSPresenter;
    private int weight;

    public NavBarItemView(Context context, ModuleList navigationItem, AppCMSPresenter appCMSPresenter, int weight) {
        super(context);
        this.navTabBar = navigationItem;
        this.appCMSPresenter = appCMSPresenter;
        this.weight = weight;
        init();
    }

    public NavBarItemView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public NavBarItemView(Context context,
                          @Nullable AttributeSet attrs,
                          int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public void init() {
        hasFocus = false;
        setPadding(0, 0, 0, 0);
        setOrientation(VERTICAL);
        createChildren();
    }

    public void select(boolean hasFocus, AppCMSPageActivity.NavTabTag navigationTabBar) {

        this.hasFocus = hasFocus;
        Resources resources = getResources();
        int color = appCMSPresenter.getGeneralTextColor(); // ContextCompat.getColor(getContext(), R.color.colorNavBarText);
        if (hasFocus) {
            color = appCMSPresenter.getBrandPrimaryCtaColor();
            if (navigationTabBar.getNavigationModuleItem().isBackgroundSelectable()) {
                //setTabBg();
            }
        } else {
            this.setBackgroundResource(0);
        }

        for (int i = 0; i < navigationTabBar.getNavigationModuleItem().getComponents().size(); i++) {
            String type = navigationTabBar.getNavigationModuleItem().getComponents().get(i).getType();
            if (navLabel != null && type.equalsIgnoreCase("label") && navigationTabBar.getNavigationModuleItem().getComponents().get(i).isSelectable()) {
                navLabel.setTextColor(color);
                // } else if (navImage != null && type.equalsIgnoreCase("image") &&   // Commented due to somehow  isSelectable returning false all the time
                // navigationTabBar.getNavigationModuleItem().getComponents().get(i).isSelectable()) {
            } else if (navImage != null && type.equalsIgnoreCase("image")) {
                applyTintToDrawable(navImage, color);
            }

        }

    }

    private void setTabBg() {
        int[] ButtonColors = {Color.parseColor("#f4181c"), Color.parseColor("#00000000")};
        GradientDrawable gradientDrawable = new GradientDrawable(
                GradientDrawable.Orientation.BOTTOM_TOP, ButtonColors);
        gradientDrawable.setCornerRadius(0f);

        this.setBackground(gradientDrawable);

    }


    public void createChildren() {

        for (int i = 0; i < navTabBar.getComponents().size(); i++) {

            String componentType = navTabBar.getComponents().get(i).getType();
            int colorTint = appCMSPresenter.getGeneralTextColor();
            switch (componentType) {
                case "image":
                    navImage = new AppCompatImageView(getContext());
                    int navImageWidth =
                            (int) BaseView.convertDpToPixel(getContext().getResources().getDimension(R.dimen.nav_image_width), getContext());
                    int navImageHeight =
                            (int) BaseView.convertDpToPixel(getContext().getResources().getDimension(R.dimen.nav_image_height), getContext());

                    LinearLayoutCompat.LayoutParams navImageLayoutParams =
                            new LinearLayoutCompat.LayoutParams(BaseView.dpToPx(R.dimen.nav_image_width, getContext()), BaseView.dpToPx(R.dimen.nav_image_height, getContext()));
                    navImageLayoutParams.gravity = Gravity.CENTER_HORIZONTAL;
                    navImage.setLayoutParams(navImageLayoutParams);
                    addView(navImage);

                    break;

                case "label":
                    navLabel = new AppCompatTextView(getContext());
                    LinearLayoutCompat.LayoutParams navLabelLayoutParams =
                            new LinearLayoutCompat.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                                    ViewGroup.LayoutParams.WRAP_CONTENT);
                    navLabelLayoutParams.gravity = Gravity.CENTER_HORIZONTAL;
                    if(!appCMSPresenter.isNewsTemplate())
                    navLabelLayoutParams.topMargin = BaseView.dpToPx(5);
                    navLabel.setMaxLines(1);
                    navLabel.setLayoutParams(navLabelLayoutParams);
                    navLabel.setTextSize(TypedValue.COMPLEX_UNIT_PX,
                            getResources().getDimension(R.dimen.bottom_navbar_text_size));
                    //navLabel.setTextColor(ContextCompat.getColor(getContext(), R.color.colorNavBarText));
                    navLabel.setTextColor(0x55000000 + appCMSPresenter.getGeneralTextColor());
                    navLabel.setGravity(Gravity.CENTER_HORIZONTAL);
                    ViewCreator.setTypeFace(getContext(), appCMSPresenter, appCMSPresenter.getJsonValueKeyMap(), new Component(),navLabel);
                    addView(navLabel);

                    break;

            }
        }

        LinearLayoutCompat.LayoutParams parentLayoutParams = null;
        if (BaseView.isTablet(getContext())) {
            parentLayoutParams =
                    new LinearLayoutCompat.LayoutParams(BaseView.dpToPx(R.dimen.nav_item_min_width, getContext()), BaseView.dpToPx(R.dimen.nav_item_large_height, getContext()));
        } else {
            parentLayoutParams =
                    new LinearLayoutCompat.LayoutParams(0, LinearLayoutCompat.LayoutParams.WRAP_CONTENT);
            parentLayoutParams.weight = weight;
            parentLayoutParams.gravity = Gravity.CENTER;

        }
        setLayoutParams(parentLayoutParams);

        int navItemWidth = (int) BaseView.convertDpToPixel(getContext().getResources().getDimension(R.dimen.nav_item_min_width), getContext());

        setPadding(BaseView.dpToPx(R.dimen.nav_item_left_right_padding, getContext()),
                BaseView.dpToPx(R.dimen.nav_item_top_padding, getContext()),
                BaseView.dpToPx(R.dimen.nav_item_left_right_padding, getContext()),
                BaseView.dpToPx(R.dimen.nav_item_bottom_padding, getContext()));
        setMinimumWidth(navItemWidth);


    }

    public void setTabImage(String tabDisplayPath) {

        /**
         * If Bottom Tab bar image is from backend.
         * Image set through URL.
         * Else set through local drawable.
         */
        if(tabDisplayPath != null && tabDisplayPath.contains("https://")){
            String imageUrl = getContext().getString(R.string.app_cms_image_with_resize_query,
                    tabDisplayPath,
                    BaseView.dpToPx(R.dimen.nav_image_width, getContext()),
                    BaseView.dpToPx(R.dimen.nav_image_height, getContext()));
            try {
                Glide.with(getContext()).load(imageUrl).into(navImage);
            }catch (Exception e){
                e.printStackTrace();
            }
        }else {

            Resources resources = getResources();
            int drawableId;
            String drawableName = null;
            if (tabDisplayPath == null) {
                drawableName = resources.getString(R.string.app_cms_menu_icon_name);
                drawableId = resources.getIdentifier(drawableName,
                        "drawable",
                        getContext().getPackageName());

            } else {
                drawableId = resources.getIdentifier(tabDisplayPath.replace("-", "_"), "drawable", appCMSPresenter.getCurrentActivity().getPackageName());
            }
            if (navImage != null && drawableId != 0) {
                navImage.setImageDrawable(ContextCompat.getDrawable(getContext(), drawableId));
                applyTintToDrawable(navImage, (0x55000000 + appCMSPresenter.getGeneralTextColor()));

            }
        }
    }

    public void setImage(String drawableName) {
        Resources resources = getResources();
        try {
            int drawableId = resources.getIdentifier(drawableName,
                    "drawable",
                    getContext().getPackageName());
            if (navImage != null) {
                navImage.setImageDrawable(ContextCompat.getDrawable(getContext(), drawableId));
            }
        } catch (Exception e) {

        }
    }

    public void setLabel(String label) {
        if (navLabel != null) {
            navLabel.setText(label);
        }
    }

    public void hideLabel() {
        navLabel.setVisibility(GONE);
    }

    public boolean isItemSelected() {
        return hasFocus;
    }

    public void setHighlightColor(int highlightColor) {
        this.highlightColor = highlightColor;
    }

    private void applyTintToDrawable(@NonNull AppCompatImageView imageView, int color) {
        Drawable drawable = imageView.getDrawable();
        if (drawable != null) {
            if (drawable instanceof BitmapDrawable) {

                imageView.setColorFilter(color, android.graphics.PorterDuff.Mode.MULTIPLY);
            } else if (drawable instanceof VectorDrawable) {

                imageView.setColorFilter(color, PorterDuff.Mode.SRC_IN);
            }


        }
    }


}
