package com.viewlift.views.customviews;

import android.content.Context;
import android.graphics.drawable.PaintDrawable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.viewlift.R;
import com.viewlift.models.data.appcms.api.ContentDatum;
import com.viewlift.models.data.appcms.ui.page.Component;
import com.viewlift.presenters.AppCMSPresenter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Nullable;

import static com.viewlift.views.customviews.ViewCreator.setTypeFace;

public class CategoryCompTray07 extends LinearLayout {

    Context mContext;
    ContentDatum data;
    AppCMSPresenter appCMSPresenter;
    private List<String> categoryList = new ArrayList<>();
    private Map<Integer,List<String>> categoryMap =  new HashMap<>();
    public CategoryCompTray07(Context context) {
        super(context);
    }

    public CategoryCompTray07(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public CategoryCompTray07(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public CategoryCompTray07(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public CategoryCompTray07(Context context, AppCMSPresenter appCMSPresenter, ContentDatum data){
        super(context);
        this.data = data;
        mContext= context;
        this.setOrientation(VERTICAL);
        this.appCMSPresenter= appCMSPresenter;


    }

    public void updateCategory(ContentDatum contentDatum, int position){
        data= contentDatum;
        if (categoryMap.get(position)==null){
            categoryList = new ArrayList<>();
        }else{
            categoryList = categoryMap.get(position);
        }
        if (data.getCategories() != null) {
            for (int i = 0; i < data.getCategories().size(); i++) {
                if (!containsCategory(data.getCategories().get(i).getTitle())) {
                    categoryList.add(data.getCategories().get(i).getTitle());
                    //createTextviewsForCategoryConceptTray(mContext, data.getCategories().get(i).getTitle(), this, appCMSPresenter);
                }
            }
        }
        if (data.getGist().getPrimaryCategory() != null &&
                data.getGist().getPrimaryCategory().getTitle() != null) {
            if (!containsCategory(data.getGist().getPrimaryCategory().getTitle())) {
                categoryList.add( data.getGist().getPrimaryCategory().getTitle());
                //createTextviewsForCategoryConceptTray(mContext, data.getGist().getPrimaryCategory().getTitle(), this, appCMSPresenter);
            }
        }
        categoryMap.put(position,categoryList);
        removeAllViews();
        categoryList = categoryMap.get(position);
        for (String category: categoryList) {
            createTextviewsForCategoryConceptTray(mContext, category, this, appCMSPresenter);
        }

    }
    private boolean containsCategory(String category){
        return categoryList.contains(category);
    }
    private void createTextviewsForCategoryConceptTray(Context context, String title, LinearLayout view,AppCMSPresenter appCMSPresenter) {
        TextView category = new TextView(context);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(0, 0, 0, 10);
        category.setLayoutParams(layoutParams);
        category.setText(title);
        category.setTextColor(appCMSPresenter.getGeneralTextColor());
        category.setEllipsize(TextUtils.TruncateAt.END);
        category.setGravity(Gravity.CENTER);
        category.setPadding(10, 0, 10, 0);
        PaintDrawable gdDefault = new PaintDrawable(ContextCompat.getColor(context, R.color.backgroundTextViewColor));
        gdDefault.setCornerRadius(10f);
        category.setBackground(gdDefault);
        view.addView(category);
        Component component = new Component();
        setTypeFace(context,
                appCMSPresenter,
                appCMSPresenter.getJsonValueKeyMap(),
                component,
                category);
    }
}
