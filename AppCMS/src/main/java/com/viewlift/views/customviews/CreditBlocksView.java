package com.viewlift.views.customviews;

import android.content.Context;
import android.graphics.Typeface;
import android.text.TextUtils;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.core.content.res.ResourcesCompat;

import com.viewlift.R;
import com.viewlift.utils.CommonUtils;

/**
 * Created by viewlift on 6/7/17.
 */

public class CreditBlocksView extends RelativeLayout {
    private final String fontFamilyKey;
    private final int fontFamilyKeyType;
    private final String fontFamilyValue;
    private final int fontFamilyValueType;
    private final String directorListTitle;
    private final String directorList;
    private final String starringListTitle;
    private final String starringList;
    private final int textColor;
    private final int moreBackgroundColor;
    private final float fontsizeKey;
    private final float fontsizeValue;

    private TextView directorListTitleView;
    private TextView directorListView;
    private TextView starringListTitleView;
    private TextView starringListView;

    Context mContext;

    public CreditBlocksView(Context context,
                            String fontFamilyKey,
                            int fontFamilyKeyType,
                            String fontFamilyValue,
                            int fontFamilyValueType,
                            String directorListTitle,
                            String directorList,
                            String starringListTitle,
                            String starringList,
                            int textColor,
                            int moreBackgroundColor,
                            float fontsizeKey,
                            float fontsizeValue) {
        super(context);
        this.fontFamilyKey = fontFamilyKey;
        this.fontFamilyKeyType = fontFamilyKeyType;
        this.fontFamilyValue = fontFamilyValue;
        this.fontFamilyValueType = fontFamilyValueType;
        this.directorListTitle = directorListTitle;
        this.directorList = directorList;
        this.starringList = starringList;
        this.starringListTitle = starringListTitle;
        this.textColor = textColor;
        this.moreBackgroundColor = moreBackgroundColor;
        this.fontsizeKey = fontsizeKey;
        this.fontsizeValue = fontsizeValue;
        this.mContext = context;
        init();
    }

    private void init() {
        Typeface keyTypeFace = ResourcesCompat.getFont(mContext, R.font.font_extra_bold);
        Typeface valueTypeFace = ResourcesCompat.getFont(mContext, R.font.font_regular);


        directorListTitleView = new TextView(getContext());
        directorListTitleView.setId(R.id.directorListTitleViewId);
        directorListTitleView.setTypeface(keyTypeFace);
        directorListTitleView.setTextColor(textColor);
        if (fontsizeKey != -1.0f) {
            directorListTitleView.setTextSize(fontsizeKey);
        }
        LayoutParams directorListTitleLayoutParams =
                new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        directorListTitleLayoutParams.addRule(ALIGN_PARENT_START);
        directorListTitleView.setLayoutParams(directorListTitleLayoutParams);
        directorListTitleView.setMaxLines(1);
        directorListTitleView.setEllipsize(TextUtils.TruncateAt.END);
        addView(directorListTitleView);

        directorListView = new TextView(getContext());
        directorListView.setId(R.id.directorListViewId);
        directorListView.setTypeface(valueTypeFace);
        directorListView.setTextColor(textColor);
        directorListView.setPadding((int) getContext().getResources().getDimension(R.dimen.castview_padding),
                0,
                0,
                0);
        if (fontsizeValue != -1.0f) {
            directorListView.setTextSize(fontsizeValue);
        }
        LayoutParams directorListLayoutParams =
                new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        directorListLayoutParams.addRule(ALIGN_PARENT_END);
        directorListLayoutParams.addRule(ALIGN_BOTTOM,directorListTitleView.getId());
        directorListLayoutParams.addRule(END_OF, directorListTitleView.getId());
        directorListView.setLayoutParams(directorListLayoutParams);
        directorListView.setMaxLines(1);
        directorListView.setEllipsize(TextUtils.TruncateAt.END);
        addView(directorListView);

        starringListTitleView = new TextView(getContext());
        starringListTitleView.setId(R.id.starringListTitleViewId);
        starringListTitleView.setTypeface(keyTypeFace);
        starringListTitleView.setTextColor(textColor);
        if (fontsizeKey != -1.0f) {
            starringListTitleView.setTextSize(fontsizeKey);
        }
        LayoutParams starringListTitleLayoutParams =
                new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        starringListTitleLayoutParams.addRule(ALIGN_PARENT_START);
        starringListTitleLayoutParams.addRule(BELOW, R.id.directorListTitleViewId);
        starringListTitleView.setLayoutParams(starringListTitleLayoutParams);
        starringListTitleView.setMaxLines(1);
        starringListTitleView.setEllipsize(TextUtils.TruncateAt.END);
        addView(starringListTitleView);

        starringListView = new TextView(getContext());
        starringListView.setId(R.id.starringListViewId);
        starringListView.setTypeface(valueTypeFace);
        starringListView.setTextColor(textColor);
        starringListView.setPadding((int) getContext().getResources().getDimension(R.dimen.castview_padding),
                0,
                0,
                0);
        if (fontsizeValue != -1.0f) {
            starringListView.setTextSize(fontsizeValue);
        }

        LayoutParams starringListLayoutParams =
                new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        starringListLayoutParams.addRule(ALIGN_PARENT_END);

        starringListLayoutParams.addRule(ALIGN_TOP, R.id.starringListTitleViewId);
        starringListLayoutParams.addRule(END_OF, R.id.starringListTitleViewId);
        starringListView.setLayoutParams(starringListLayoutParams);
        starringListView.setMaxLines(2);
        starringListView.setEllipsize(TextUtils.TruncateAt.END);
        addView(starringListView);

        updateText(directorListTitle, directorList, starringListTitle, starringList);
    }

    public void updateText(String directorListTitle,
                           String directorList,
                           String starringListTitle,
                           String starringList) {
        if (!TextUtils.isEmpty(directorListTitle) && !TextUtils.isEmpty(directorList) &&
                directorListTitleView != null &&
                directorListView != null) {
            directorListTitleView.setText(directorListTitle.toUpperCase());
            directorListView.setText(directorList);
            //directorListView.setSingleLine();
            //directorListView.setEllipsize(TextUtils.TruncateAt.END);

          /*  ViewTreeObserver directorListVto = directorListView.getViewTreeObserver();
            directorListVto.addOnGlobalLayoutListener(new ViewCreatorMultiLineLayoutListener(directorListView,
                    null,
                    directorList,
                    null,
                    true,
                    moreBackgroundColor,
                    textColor,
                    true));*/

        }else if(CommonUtils.isEmpty(directorList)){
            directorListTitleView.setVisibility(GONE);
            directorListView.setVisibility(GONE);
        }

        if (!TextUtils.isEmpty(starringListTitle) && !TextUtils.isEmpty(starringList) &&
                starringListTitleView != null &&
                starringListView != null) {
            starringListTitleView.setText(starringListTitle.toUpperCase());
            starringListView.setText(starringList);
            // starringListView.setSingleLine();
            // starringListView.setEllipsize(TextUtils.TruncateAt.END);
//            if (BaseView.isTablet(getContext())&&!BaseView.isLandscape(getContext())) {
//                starringListView.setEllipsize(TextUtils.TruncateAt.MARQUEE);
//                starringListView.setSelected(true);
//                starringListView.setFocusable(true);
//                starringListView.setFocusableInTouchMode(true);
//                starringListView.setFreezesText(true);
//                starringListView.setMarqueeRepeatLimit(-1);
//                starringListView.setHorizontallyScrolling(true);
//                starringListView.setSingleLine();
//            }else
            {
               /* ViewTreeObserver starringListVto = starringListView.getViewTreeObserver();
                starringListVto.addOnGlobalLayoutListener(new ViewCreatorMultiLineLayoutListener(starringListView,
                        null,
                        starringList,
                        null,
                        true,
                        moreBackgroundColor,
                        textColor,
                        true));*/
            }
        }
    }
}
