package com.viewlift.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.text.Html;

import androidx.appcompat.widget.AppCompatCheckBox;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;
import com.viewlift.R;
import com.viewlift.presenters.AppCMSPresenter;
import com.viewlift.views.customviews.BaseView;

public class ImageGetterFromHTMLText implements Html.ImageGetter {

    private AppCompatCheckBox textView = null;
    private Context mContext;
    AppCMSPresenter appCMSPresenter;

    public ImageGetterFromHTMLText() {

    }

    public ImageGetterFromHTMLText(AppCompatCheckBox target, Context mContext) {
        this.mContext = mContext;
        textView = target;
    }

    @Override
    public Drawable getDrawable(String source) {
        BitmapDrawablePlaceHolder drawable = new BitmapDrawablePlaceHolder();
        Picasso.get().load(source)
                .placeholder(R.drawable.ic_whatsapp)
                .into(drawable);
        return drawable;
    }

    private class BitmapDrawablePlaceHolder extends BitmapDrawable implements Target {

        protected Drawable drawable;

        @Override
        public void draw(final Canvas canvas) {
            if (drawable != null) {
                drawable.draw(canvas);
            }
        }

        public void setDrawable(BitmapDrawable drawable) {
            this.drawable = drawable;
            int width     = BaseView.dpToPx(20); //drawable.getIntrinsicWidth();
            int height    = BaseView.dpToPx(20); //drawable.getIntrinsicHeight();
            setBounds(new Rect(0,0, width, height));
            drawable.setBounds(new Rect(0,0, width, height));
            if (textView != null) {
                textView.setText(textView.getText());
                textView.requestLayout();
            }
        }

        @Override
        public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
            setDrawable(new BitmapDrawable(mContext.getResources(), bitmap));
        }

        @Override
        public void onBitmapFailed(Exception e, Drawable errorDrawable) {

        }


        @Override
        public void onPrepareLoad(Drawable placeHolderDrawable) {

        }

    }
}