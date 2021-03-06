package com.viewlift.tv.views.presenter;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.leanback.widget.ListRow;
import androidx.leanback.widget.Presenter;
import androidx.leanback.widget.RowHeaderPresenter;

import com.viewlift.R;
import com.viewlift.tv.views.customviews.CustomHeaderItem;

/**
 * Created by nitin.tyagi on 7/2/2017.
 */

public class CustomHeaderItemPresenter extends RowHeaderPresenter {


    private Context context;
    TextView titletextView;
    @Override
    public RowHeaderPresenter.ViewHolder onCreateViewHolder(ViewGroup parent) {
        //return super.onCreateViewHolder(parent);


        LinearLayout linearLayout = new LinearLayout(parent.getContext());
        LinearLayout.LayoutParams linearParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);

        linearLayout.setOrientation(LinearLayout.VERTICAL);
        linearLayout.setLayoutParams(linearParams);

        titletextView = new TextView(linearLayout.getContext());
        titletextView.setTextColor(ContextCompat.getColor(parent.getContext() , android.R.color.holo_red_dark));
        titletextView.setId(R.id.appcms_headerTitle);
        linearLayout.addView(titletextView);

        View view = new View(linearLayout.getContext());
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT , 2);
        view.setLayoutParams(params);
        view.setBackgroundColor(Color.RED);
        linearLayout.addView(view);

        return new RowHeaderPresenter.ViewHolder(linearLayout);
    }

    @Override
    public void onBindViewHolder(Presenter.ViewHolder viewHolder, Object item) {

        CustomHeaderItem iconHeaderItem = (CustomHeaderItem) ((ListRow) item).getHeaderItem();

        LinearLayout linearLayout = (LinearLayout) viewHolder.view;
        TextView textView = (TextView)linearLayout.findViewById(R.id.appcms_headerTitle);
        textView.setText(iconHeaderItem.getName());
    }
}
