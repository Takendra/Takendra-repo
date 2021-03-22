package com.viewlift.views.customviews;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.core.content.res.ResourcesCompat;

import com.viewlift.R;
import com.viewlift.presenters.AppCMSPresenter;

import butterknife.BindView;
import butterknife.ButterKnife;

public class LoginModuleTabButton extends LinearLayout {


    @BindView(R.id.textViewTitle)
    TextView textViewTitle;

    @BindView(R.id.textViewUnderline)
    TextView textViewUnderline;

    ConstraintLayout tabTopView;
    AppCMSPresenter appCMSPresenter;
    Context context;
    ConstraintSet set, setRoot;

    public LoginModuleTabButton(Context context) {
        super(context);
    }

    public LoginModuleTabButton(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public LoginModuleTabButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    public LoginModuleTabButton(Context context, AppCMSPresenter appCMSPresenter) {
        super(context);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (tabTopView == null)
            tabTopView = (ConstraintLayout) inflater.inflate(R.layout.login_module_tab_button, null);
        ButterKnife.bind(this, tabTopView);
        //textViewTitle = (TextView) tabTopView.findViewById(R.id.textViewTitle);
        //textViewUnderline = (TextView) tabTopView.findViewById(R.id.textViewUnderline);

        this.context = context;
        this.appCMSPresenter = appCMSPresenter;
        set = new ConstraintSet();
        setRoot = new ConstraintSet();

        textViewTitle.setTextColor(appCMSPresenter.getGeneralTextColor());
        textViewUnderline.setBackgroundColor(appCMSPresenter.getBrandPrimaryCtaColor());

        textViewTitle.setTypeface(ResourcesCompat.getFont(context, R.font.font_semi_bold));
        addView(tabTopView);

    }

    public void setViewOriantaion(int direction) {
        set.clone(tabTopView);
        set.connect(textViewTitle.getId(), direction, ConstraintSet.PARENT_ID, direction, 20);

        set.applyTo(tabTopView);
    }

    public void setTitle(String title, int color) {
        textViewTitle.setText(title);
        textViewTitle.setTextColor(color);
    }

    public void setUnderlineVisibility(int visibility) {
        textViewUnderline.setVisibility(visibility);
        if (visibility == VISIBLE) {
            setAlphaTextColorForSelector(textViewTitle, 200);
        } else {
            setAlphaTextColorForSelector(textViewTitle, 100);
        }

    }

    void setAlphaTextColorForSelector(TextView textView, int alpha) {
        String textColor = appCMSPresenter.getAppCMSMain().getBrand().getGeneral().getTextColor();
        int color = Color.parseColor(textColor);
        int r = (color >> 16) & 0xFF;
        int g = (color >> 8) & 0xFF;
        int b = (color >> 0) & 0xFF;
        textView.setTextColor(Color.argb(alpha, r, g, b));
    }
}
