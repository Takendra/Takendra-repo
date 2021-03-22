package com.viewlift.views.customviews;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Handler;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.view.MotionEventCompat;

import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.JavascriptInterface;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.gson.Gson;
import com.viewlift.R;
import com.viewlift.db.AppPreference;
import com.viewlift.models.data.appcms.user.UserIdentity;
import com.viewlift.presenters.AppCMSPresenter;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import static com.viewlift.views.customviews.BaseView.appCMSPresenter;

/**
 * Created by karan.kaushik on 11/22/2017.
 */

public class CustomWebView extends AppCMSAdvancedWebView {

    public static WebChromeClient mWebChromeClient;
    private Activity context;
    private WebView webView;
    AppCMSPresenter appcmsPresenter;
    public static View mFbLiveView;
    private RelativeLayout mContentView;
    public static FrameLayout mWebFbPlayerView;
    public static boolean isWebVideoFullView = false;
    private WebChromeClient.CustomViewCallback mCustomViewCallback;

    public CustomWebView(Context context, AppCMSPresenter appcmsPresenter) {
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
        this.getSettings().setDomStorageEnabled(true);
        this.getSettings().setDatabaseEnabled(true);
    }

    public CustomWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomWebView(Context context, AttributeSet attrs, int defStyle) {
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


    public void loadURLData(Context mContext, AppCMSPresenter appCMSPresenter, String loadingURL, String cacheKey) {
        this.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                if(url!=null && !TextUtils.isEmpty(url)){
                    Intent browserIntent = new Intent("android.intent.action.VIEW", Uri.parse(url));
                    context.startActivity(browserIntent);
                }
                return true;

            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                if(url!=null && !TextUtils.isEmpty(url)) {
                    appCMSPresenter.setWebViewCache(cacheKey, (CustomWebView) view);
                }
            }

            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                super.onReceivedError(view, request, error);
                appCMSPresenter.clearWebViewCache();
            }
        });

        this.loadData(loadingURL, "text/html", "UTF-8");
    }
    //Check if weburl return success
    public class checkURLAysyncTask extends AsyncTask<String, String, Integer> {

        private String loadwebUrl = "";
        private AppCMSPresenter appCMSPresenter;

        public checkURLAysyncTask(String loadingUrl, AppCMSPresenter appCMSPresenter) {
            this.loadwebUrl = loadingUrl;
            this.appCMSPresenter = appCMSPresenter;
        }

        @Override
        protected void onPreExecute() {
            appcmsPresenter.showLoadingDialog(true);

        }

        @Override
        protected Integer doInBackground(String... arg0) {
            int iHTTPStatus = 0;

            // Making HTTP request
            try {
                // defaultHttpClient
                DefaultHttpClient httpClient = new DefaultHttpClient();
                HttpGet httpRequest = new HttpGet(arg0[0]);

                HttpResponse httpResponse = httpClient.execute(httpRequest);
                iHTTPStatus = httpResponse.getStatusLine().getStatusCode();

            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            } catch (ClientProtocolException e) {
                e.printStackTrace();

            } catch (IOException e) {
                e.printStackTrace();

            } catch (Exception e) {
                e.printStackTrace();

            }

            return iHTTPStatus;
        }

        @Override
        protected void onPostExecute(Integer httpStatusCode) {
            super.onPostExecute(httpStatusCode);
            if (httpStatusCode == 200) {
                loadUrlWithWebViewClient(appcmsPresenter, loadwebUrl);
            } else {
                Toast.makeText(context, "Error while loading page..", Toast.LENGTH_LONG).show();
                CustomWebView.this.loadUrl(loadwebUrl);
                appCMSPresenter.showLoadingDialog(false);
                context.sendBroadcast(new Intent(AppCMSPresenter.PRESENTER_STOP_PAGE_LOADING_ACTION));

            }
        }
    }

    public void loadWebVideoUrl(AppCMSPresenter appCMSPresenter, String loadingUrl) {
        context.sendBroadcast(new Intent(AppCMSPresenter.PRESENTER_PAGE_LOADING_ACTION));
        this.getSettings().setUseWideViewPort(true);
        this.getSettings().setLoadWithOverviewMode(true);
        appCMSPresenter.showLoadingDialog(true);
        this.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.TEXT_AUTOSIZING);
        this.getSettings().setBuiltInZoomControls(true);
        mFbLiveView = null;
        mWebFbPlayerView = null;
        this.setLayerType(View.LAYER_TYPE_NONE, null);
        this.measure(MeasureSpec.UNSPECIFIED, MeasureSpec.UNSPECIFIED);
        this.getSettings().setAllowContentAccess(true);
        this.getSettings().setAllowFileAccess(true);
        setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.HONEYCOMB) {
            // Hide the zoom controls for HONEYCOMB+
            this.getSettings().setDisplayZoomControls(false);
        }
        mWebChromeClient = new MyWebChromeClient();
        this.setWebChromeClient(mWebChromeClient);

        // this.getSettings().setDefaultFontSize(30);
        this.addJavascriptInterface(this, "MyApp");
        this.setWebViewClient(new WebViewClient() {

            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                super.onReceivedError(view, request, error);
                appCMSPresenter.clearWebViewCache();
                appcmsPresenter.showLoadingDialog(false);

            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                appCMSPresenter.showLoadingDialog(true);
                view.loadUrl(url);

                return true;
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                appCMSPresenter.showLoadingDialog(false);
                mFbLiveView=view;
                view.requestLayout();
                context.sendBroadcast(new Intent(AppCMSPresenter.PRESENTER_STOP_PAGE_LOADING_ACTION));
            }

        });
        this.loadUrl(loadingUrl);
    }

    String cachedKey = "";
    public void loadURL(Context mContext, AppCMSPresenter appCMSPresenter, String loadingURL, String cacheKey) {
        if(!loadingURL.contains("https"))
        loadingURL = loadingURL.replace("http", "https");
        cachedKey = cacheKey;
        new checkURLAysyncTask(loadingURL, appCMSPresenter).execute(loadingURL);
    }

    public void loadUrlWithWebViewClient(AppCMSPresenter appCMSPresenter, String loadingURL) {
        try {
        context.sendBroadcast(new Intent(AppCMSPresenter.PRESENTER_PAGE_LOADING_ACTION));
        this.getSettings().setUseWideViewPort(true);
        this.getSettings().setLoadWithOverviewMode(true);
        appCMSPresenter.showLoadingDialog(true);
        webView.getSettings().setPluginState(WebSettings.PluginState.ON);
        webView.getSettings().setPluginState(WebSettings.PluginState.ON_DEMAND);
        this.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.TEXT_AUTOSIZING);
        this.getSettings().setBuiltInZoomControls(true);
        setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.HONEYCOMB) {
            // Hide the zoom controls for HONEYCOMB+
            this.getSettings().setDisplayZoomControls(false);
        }

        // this.getSettings().setDefaultFontSize(30);
        this.addJavascriptInterface(this, "MyApp");
        this.setWebViewClient(new WebViewClient() {

            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                super.onReceivedError(view, request, error);
                appCMSPresenter.clearWebViewCache();
                appcmsPresenter.showLoadingDialog(false);

            }


            @Nullable
            @Override
            public WebResourceResponse shouldInterceptRequest(WebView view, WebResourceRequest request) {
                try {
                    if (request != null && request.getUrl() != null &&
                            request.getUrl().toString().contains("identity/user")) {
                        Log.e("CustomWebView", request.getUrl().toString());
                        if (appcmsPresenter.getAppPreference()!=null&&!appCMSPresenter.getAppPreference().isbrowserLocalStorage() && !appCMSPresenter.isUserLoggedIn())
                            userLogin(loadingURL);

                    }
                }
                catch (Exception e){
                    e.printStackTrace();
                }
                return super.shouldInterceptRequest(view, request);
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                appCMSPresenter.showLoadingDialog(true);

                if (!loadingURL.equalsIgnoreCase(url.replace("https", "http"))) {
//                    appCMSPresenter.showEntitlementDialog(AppCMSPresenter.DialogType.OPEN_URL_IN_BROWSER,
//                            () -> {
//                                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
//                                view.getContext().startActivity(browserIntent);
//                            });
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                    view.getContext().startActivity(browserIntent);
                    return true;
                } else {
                    Log.e("CustomWebView", "Redirected URL :" + url);
                    view.loadUrl(url);
                }
                return false;
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                appCMSPresenter.showLoadingDialog(false);
                if (url.contains("#")) {
                    appCMSPresenter.launchButtonSelectedAction(null,
                            getContext().getString(R.string.app_cms_action_loginfacebook_key),
                            null,
                            null,
                            null,
                            true,
                            0,
                            null);
                }
                appCMSPresenter.setWebViewCache(cachedKey, (CustomWebView) view);
                view.loadUrl("javascript:MyApp.resize(document.body.getBoundingClientRect().height)");

                view.loadUrl("javascript:$(document).ajaxStart(function (event, request, settings) { " +
                        "MyApp.ajaxBegin(); " + // Event called when an AJAX call begins
                        "});");

                view.loadUrl("javascript:$(document).ajaxComplete(function (event, request, settings) { " +
                        "MyApp.ajaxDone(); " + // Event called when an AJAX call ends
                        "});");



                view.requestLayout();
                context.sendBroadcast(new Intent(AppCMSPresenter.PRESENTER_STOP_PAGE_LOADING_ACTION));
            }

        });

            this.loadUrl(loadingURL);
        if ( !appCMSPresenter.isUserLoggedIn() &&appcmsPresenter.getAppPreference()!=null&&
                appcmsPresenter.getAppPreference().isbrowserLocalStorage()) {
            removeBrowserData(appCMSPresenter, loadingURL);
        } else if (appCMSPresenter.isUserLoggedIn() &&appcmsPresenter.getAppPreference()!=null&&
                !appcmsPresenter.getAppPreference().isbrowserLocalStorage() && appcmsPresenter.getAppPreference().isIsbrowserDataAlreadyOpen())
            storeBrowserLocalStorage(appCMSPresenter, loadingURL);
        }
        catch (Exception e) {
           System.out.println( "URLLOADISSUE"+e.toString());
        }
    }


    public void storeBrowserLocalStorage(AppCMSPresenter appCMSPresenter, String loadingURL){
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void run() {
                // Do something after 5s = 5000ms
                loadURL(appCMSPresenter,loadingURL);
            }
        }, 3000);
    }
    int queryIndex = 0;
    public void loadURL(AppCMSPresenter appCMSPresenter, String loadingURL) {
        if(!loadingURL.contains("https"))
            loadingURL = loadingURL.replace("http", "https");
        loadUrl(loadingURL);
        final String urlLoad=loadingURL;
        WebSettings webSettings = getSettings();
        webSettings.setJavaScriptEnabled(true);
        webView.setWebViewClient(new WebViewClient() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onPageFinished(WebView view, final String url) {
                storeDataOnWebView(urlLoad);
                appCMSPresenter.showLoadingDialog(false);
            }
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                appCMSPresenter.showLoadingDialog(true);
                return true;
            }
        });
    }
    public void storeDataOnWebView(String url){
        String jsonRequest=null;
        if(appcmsPresenter.isUserSubscribed())
         jsonRequest = "localStorage.setItem(\"token\",JSON.stringify({\"authorizationToken\":\""+appcmsPresenter.getAuthToken()+"\",\"refreshToken\":\""+appcmsPresenter.getRefreshToken()+"\",\"expiration\":\"1587182340\"}));localStorage.setItem(\"user\",JSON.stringify({\"user\":{\"isSubscribed\":\""+appcmsPresenter.isUserSubscribed()+"\"}}))";
        else
            jsonRequest = "localStorage.setItem(\"token\",JSON.stringify({\"authorizationToken\":\""+appcmsPresenter.getAuthToken()+"\",\"refreshToken\":\""+appcmsPresenter.getRefreshToken()+"\",\"expiration\":\"1587182340\"}));localStorage.setItem(\"user\",JSON.stringify({\"user\":{\"\":\""+""+"\"}}))";

        if (queryIndex < 2 )
        {

            webView.evaluateJavascript(jsonRequest, new ValueCallback<String>() {
                @Override
                public void onReceiveValue(String value) {
                    queryIndex += 1;
                    if(appcmsPresenter.getAppPreference()!=null) {
                        appcmsPresenter.getAppPreference().setbrowserLocalStorage(true);
                        appcmsPresenter.getAppPreference().setIsbrowserDataAlreadyOpen(true);
                    }
                  //  appcmsPresenter.setbrowserLocalStorageSave(true);
                   // appcmsPresenter.setIsbrowserDataAlreadyOpen(true);
                    webView.loadUrl(url);
                }
            });
        }
    }


    int evaluateLocalStorage=0;
    private void removeBrowserData(AppCMSPresenter appCMSPresenter, String loadingURL) {
        try {
            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                @Override
                public void run() {
                    // Do something after 5s = 5000ms
                    removeDATA(appCMSPresenter, loadingURL);
                }
            }, 2000);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public void removeDATA(AppCMSPresenter appCMSPresenter, String loadingURL)
    {
        System.out.println("removeDATA"+loadingURL);
        loadUrl(loadingURL);
        webView.setWebViewClient(new WebViewClient() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onPageFinished(WebView view, final String url) {
                 appCMSPresenter.showLoadingDialog(false);
                if (evaluateLocalStorage < 2) {
                    webView.evaluateJavascript("localStorage.clear()", new ValueCallback<String>() {
                        @Override
                        public void onReceiveValue(String value) {
                            evaluateLocalStorage = evaluateLocalStorage + 1;
                            if(appcmsPresenter.getAppPreference()!=null) {
                                appcmsPresenter.getAppPreference().setbrowserLocalStorage(false);
                                appcmsPresenter.getAppPreference().setIsbrowserDataAlreadyOpen(true);
                            }
                            webView.loadUrl(loadingURL);

                        }
                    });
                }
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                appCMSPresenter.showLoadingDialog(true);
                return true;
            }
        });
    }

    public void userLogin( String loadingURL){
        context.runOnUiThread(new Runnable() {
            public void run() {
                getUserData(loadingURL);
            }
        });
    }
    int getUserqueryIndex = 0;
    public void getUserData( String loadingURL) {
            if (!loadingURL.contains("https"))
                loadingURL = loadingURL.replace("http", "https");
            loadUrl(loadingURL);
            final String urlLoad = loadingURL;
            webView.setWebViewClient(new WebViewClient() {
                @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                @Override
                public void onPageFinished(WebView view, final String url) {
                    excuteGetUserDetails(urlLoad);
                    appCMSPresenter.showLoadingDialog(false);
                }

                @Override
                public boolean shouldOverrideUrlLoading(WebView view, String url) {
                    //  view.loadUrl(url);
                    return true;
                }
            });

    }
    public void excuteGetUserDetails(String url){
        String  jsonRequest = "localStorage.getItem(\"user\");";
        if (getUserqueryIndex < 2 )
        {
            webView.evaluateJavascript(jsonRequest, new ValueCallback<String>() {
                @Override
                public void onReceiveValue(String value) {
                    getUserqueryIndex += 1;
                    try {
                        if(value!=null&&!value.isEmpty()&& !value.equalsIgnoreCase("null")) {
                            saveUserData(value);
                            webView.loadUrl(url);
                        }
                    } catch (Throwable t) {
                        Log.e("My App", "Could not parse malformed JSON: \"" + value + "\"");
                    };
                }
            });
        }
    }

 public void saveUserData(String userResponseData)
 {
     if(userResponseData!=null&&!userResponseData.isEmpty() ) {
         Gson gson = new Gson();
         UserIdentity userDetails = null;

         try {
             String jsonFormattedString = new JSONTokener(userResponseData).nextValue().toString();
             JSONObject object = new JSONObject(jsonFormattedString);
             String str = object.getJSONObject("user").toString();
             userDetails = gson.fromJson(str, UserIdentity.class);
             if (userDetails != null) {
                 if(appcmsPresenter.getAppPreference()!=null) {
                     appcmsPresenter.getAppPreference().setbrowserLocalStorage(true);
                     appcmsPresenter.getAppPreference().setIsbrowserDataAlreadyOpen(true);
                 }
                 AppPreference appPreference = appcmsPresenter.getAppPreference();
                 appPreference.setAuthToken(userDetails.getAuthorizationToken());
                 appPreference.setRefreshToken(userDetails.getRefreshToken());
                 appPreference.setLoggedInUserName(userDetails.getName());
                 appPreference.setLoggedInUserEmail(userDetails.getEmail());
                 appPreference.setLoggedInUser(userDetails.getUserId());
                 appPreference.setIsUserSubscribed(userDetails.isSubscribed());
                 appPreference.setActiveSubscriptionCountryCode(userDetails.getCountry());
                 appPreference.setUserAuthProviderName(userDetails.getProvider());
                 if (userDetails.getPhoneNumber() != null)
                     appPreference.setLoggedInUserPhone(userDetails.getPhoneNumber().toString());
                 //appCMSPresenter.setbrowserLocalStorageSave(true);
             }
         } catch (Exception e) {
             e.printStackTrace();
         }
     }

 }

    public void ajaxBegin() {

        Toast.makeText(context, "AJAX Begin", Toast.LENGTH_SHORT).show();
    }

    public void ajaxDone() {

        Toast.makeText(context, "AJAX Done", Toast.LENGTH_SHORT).show();
    }

    public void showAlert(Context context, String url) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);

        // set title
        alertDialogBuilder.setTitle("Open Link");

        // set dialog message
        AlertDialog dialog = alertDialogBuilder
                .setMessage("Open Link outside?")
                .setCancelable(false)
                .setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // if this button is clicked, close
                        // current activity
                        Intent browserIntent = new Intent("android.intent.action.VIEW", Uri.parse(url));
                        context.startActivity(browserIntent);
                        dialog.dismiss();
                    }
                })
                .create();
        dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(Color.BLACK);
        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.BLACK);
        dialog.show();

    }

    @JavascriptInterface
    public void resize(final float height) {
        context.runOnUiThread(() -> {
            FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
                    getResources().getDisplayMetrics().widthPixels,
                    ViewGroup.LayoutParams.WRAP_CONTENT
            );
            Resources resources = context.getResources();
            DisplayMetrics metrics = resources.getDisplayMetrics();
            params.bottomMargin = (int) (55 * (metrics.densityDpi / 160f));
            webView.setLayoutParams(params);
        });
    }

    /**
     * Handles full screen and exit full screen from web video player
     */
    private class MyWebChromeClient extends WebChromeClient {


        @Override
        public void onShowCustomView(View view, CustomViewCallback callback) {
            FrameLayout.LayoutParams LayoutParameters = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT,
                    FrameLayout.LayoutParams.MATCH_PARENT);
            new Handler().postDelayed(() -> {
                appcmsPresenter.restrictLandscapeOnly();
            }, 200);
            mContentView = context.findViewById(R.id.app_cms_parent_view);
            if (mWebFbPlayerView == null) {
                mWebFbPlayerView = new FrameLayout(context);
                mWebFbPlayerView.setLayoutParams(LayoutParameters);
            }
            isWebVideoFullView = true;
            mWebFbPlayerView.removeAllViews();
            mWebFbPlayerView.setBackgroundResource(android.R.color.black);
            view.setLayoutParams(LayoutParameters);
            mWebFbPlayerView.addView(view);
            mFbLiveView = webView;
            mCustomViewCallback = callback;
            mWebFbPlayerView.setVisibility(View.VISIBLE);
            context.setContentView(mWebFbPlayerView);
            appcmsPresenter.restrictLandscapeOnly();

        }

        @Override
        public void onHideCustomView() {
            if (mFbLiveView == null) {
                return;
            } else {
                isWebVideoFullView = false;

                // Hide the custom view.
                mWebFbPlayerView.setVisibility(View.GONE);
                mCustomViewCallback.onCustomViewHidden();
                // Show the content view.
                mContentView.setVisibility(View.VISIBLE);
                context.setContentView(mContentView);
                appcmsPresenter.setAppOrientation();
                appcmsPresenter.sendRefreshPageAction();
            }
        }
    }
}
