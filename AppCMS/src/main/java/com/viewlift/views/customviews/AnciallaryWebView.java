package com.viewlift.views.customviews;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;

import androidx.core.view.MotionEventCompat;

import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import com.viewlift.presenters.AppCMSPresenter;


/**
 * Created by karan.kaushik on 11/22/2017.
 */

public class AnciallaryWebView extends WebView {

    private Activity context;
    private WebView webView;
    AppCMSPresenter appcmsPresenter;
    public static View mFbLiveView;
    private RelativeLayout mContentView;
    public static FrameLayout mWebFbPlayerView;
    public static boolean isWebVideoFullView = false;
    private WebChromeClient.CustomViewCallback mCustomViewCallback;

    public AnciallaryWebView(Context context, AppCMSPresenter appcmsPresenter) {
        super(context);
        this.context = (Activity) context;
        webView = this;
        this.appcmsPresenter = appcmsPresenter;
        this.getSettings().setJavaScriptEnabled(true);
        this.getSettings().setBuiltInZoomControls(false);
        this.getSettings().setDisplayZoomControls(false);
        this.setBackgroundColor(Color.TRANSPARENT);
        this.getSettings().setAppCacheEnabled(true);
        this.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
        this.getSettings().setLoadWithOverviewMode(true);
        this.getSettings().setMixedContentMode(WebSettings.MIXED_CONTENT_COMPATIBILITY_MODE);
        this.getSettings().setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
    }

    public AnciallaryWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public AnciallaryWebView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        if (MotionEventCompat.findPointerIndex(event, 0) == -1) {
            return super.onTouchEvent(event);
        }

        if (event.getPointerCount() >= 2) {
            requestDisallowInterceptTouchEvent(true);
        } else {
            requestDisallowInterceptTouchEvent(false);
        }
        return super.onTouchEvent(event);
    }


    public void loadAncillaryData(Context mContext, AppCMSPresenter appCMSPresenter, String loadingURL) {
        this.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                System.out.println("override url" + url);
                if ((url != null && !TextUtils.isEmpty(url)) || (url != null && !url.contains("blank"))) {
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                    if (browserIntent.resolveActivity(context.getPackageManager()) != null) {
                        context.startActivity(browserIntent);
                    }

                    return true;

                }
                return false;

            }

            @Override
            public void onPageFinished(WebView view, String url) {
                if (url == null || url.contains("about:blank")) {
                    webView.loadData(loadingURL, "text/html", "UTF-8");
                } else
                    super.onPageFinished(view, url);

            }

            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                super.onReceivedError(view, request, error);
            }
        });

        this.loadData(loadingURL, "text/html", "UTF-8");
    }    //Check if weburl return success


}




