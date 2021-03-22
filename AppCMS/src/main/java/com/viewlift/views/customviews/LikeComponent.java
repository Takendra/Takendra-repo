package com.viewlift.views.customviews;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.viewlift.R;
import com.viewlift.models.data.appcms.api.ContentDatum;
import com.viewlift.models.data.appcms.ui.AppCMSUIKeyType;
import com.viewlift.models.data.appcms.ui.page.Component;
import com.viewlift.presenters.AppCMSPresenter;

import java.util.Map;

import javax.annotation.Nullable;

import static com.viewlift.views.customviews.BaseView.getFontSize;

public class LikeComponent extends LinearLayout {

    Context mContext;
    AppCMSPresenter appCMSPresenter;

    public LikeComponent(Context context) {
        super(context);

    }

    public LikeComponent(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public LikeComponent(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public LikeComponent(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    ImageButton imageLike;
    TextView textViewLikeCount;
    LayoutParams lp = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
    ContentDatum mData;
    Map<String, AppCMSUIKeyType> jsonValueKeyMap;

    public LikeComponent(Context context, AppCMSPresenter appCMSPresenter, Component component, ContentDatum data,Map<String, AppCMSUIKeyType> jsonValueKeyMap) {
        this(context);
        this.mContext = context;
        this.appCMSPresenter = appCMSPresenter;
        mData = data;
        this.jsonValueKeyMap = jsonValueKeyMap;
        this.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        this.setOrientation(HORIZONTAL);

        imageLike = new ImageButton(mContext);
        textViewLikeCount = new TextView(mContext);
        textViewLikeCount.setTextColor(appCMSPresenter.getGeneralTextColor());
        textViewLikeCount.setTextSize(getFontSize(context, component.getLayout()));
        ViewCreator.setTypeFace(context,appCMSPresenter,jsonValueKeyMap,component,textViewLikeCount);

        lp.gravity = Gravity.CENTER_VERTICAL;

        imageLike.setImageResource(R.drawable.ic_rating_shape);
        imageLike.setBackgroundColor(mContext.getResources().getColor(R.color.transparentColor));

        imageLike.setLayoutParams(lp);
        textViewLikeCount.setLayoutParams(lp);
        textViewLikeCount.setPadding(10, 0, 10, 0);

        addView(imageLike);
        addView(textViewLikeCount);
        //textViewLikeCount.setText("Hello Like");


        updateCount();
        updateUserLikeStatus();

        imageLike.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                appCMSPresenter.showLoadingDialog(true);
                if (isClickToAdd) {

                    appCMSPresenter.userLikeAdd(data, likeResult -> {
                        if (likeResult != null) {
                            updateUserLikeStatus();
                        }
                    });
                } else {
                    appCMSPresenter.userUnlike(data, likeResult -> {

                        if (likeResult != null ) {
                            updateUserLikeStatus();
                        }

                    });
                }
            }
        });
    }

    public void updateCount() {
        appCMSPresenter.countLikes(mData.getGist().getId(), likes -> {

            String like = "-";
            if (likes != null) {
                like = mContext.getResources().getQuantityString(R.plurals.likes_plural,likes.getCount()==0?1:likes.getCount(),likes.getCount());
            }
            textViewLikeCount.setText(like.toUpperCase());
            appCMSPresenter.showLoadingDialog(false);
        });
    }

    public boolean isClickToAdd;

    public void updateUserLikeStatus() {
        appCMSPresenter.userLikeStatus(mData, likeResult -> {
            if (likeResult != null && likeResult.getRecords() != null
                    && likeResult.getRecords().size() > 0) {
                isClickToAdd = false;
                imageLike.setImageResource(R.drawable.ic_rating_shape_fill);
            } else {
                isClickToAdd = true;
                imageLike.setImageResource(R.drawable.ic_rating_shape);
            }
            updateCount();
        });
    }

    public void setLikeCounter(String like) {
        textViewLikeCount.setText(like);
    }


}
