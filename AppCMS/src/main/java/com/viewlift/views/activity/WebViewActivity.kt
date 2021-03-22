package com.viewlift.views.activity

import android.graphics.Bitmap
import android.net.http.SslError
import android.os.Build
import android.os.Bundle
import android.webkit.*
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.viewlift.AppCMSApplication
import com.viewlift.R
import com.viewlift.extensions.gone
import com.viewlift.presenters.AppCMSPresenter
import kotlinx.android.synthetic.main.activity_web_view.*

class WebViewActivity : AppCompatActivity() {

    private val appCMSPresenter: AppCMSPresenter by lazy {
        (application as AppCMSApplication).appCMSPresenterComponent.appCMSPresenter()
    }

    private val deviceActivatePageEndPoint = "/activate"
    private var isComeFromActivateDevice   = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_web_view)
        val url = intent?.getStringExtra("url") ?: ""
        isComeFromActivateDevice = url.contains(deviceActivatePageEndPoint)

        webView.settings?.let {
            // Enable Javascript to run in WebView
            it.javaScriptEnabled = true

            // Allow Zoom in/out controls
            it.builtInZoomControls = false

            // Zoom out the best fit your screen
            it.loadWithOverviewMode = true
            it.useWideViewPort      = true
            it.domStorageEnabled    = true
        }

        if (!appCMSPresenter.isUserLoggedIn && appCMSPresenter.appPreference != null &&
                appCMSPresenter.appPreference.isbrowserLocalStorage()) {
            removeBrowserData(appCMSPresenter, url)
        } else if (appCMSPresenter.isUserLoggedIn && appCMSPresenter.appPreference != null &&
                !appCMSPresenter.appPreference.isbrowserLocalStorage() && appCMSPresenter.appPreference.isIsbrowserDataAlreadyOpen()) {
            loadURL(url)
        } else {
            webView.webViewClient = InsideWebViewClient()
            webView.loadUrl(url)
        }
    }

    private inner class InsideWebViewClient : WebViewClient() {
        // Force links to be opened inside WebView and not in Default Browser
        override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
            view.loadUrl(url)
            checkForExit(url)
            return true
        }

        override fun onReceivedError(view: WebView, request: WebResourceRequest, error: WebResourceError) {
            super.onReceivedError(view, request, error)
            progressBar.gone()
        }

        override fun onReceivedSslError(view: WebView, handler: SslErrorHandler, error: SslError) {
            super.onReceivedSslError(view, handler, error)
            progressBar.gone()
        }

        override fun onPageStarted(view: WebView, url: String, favicon: Bitmap?) {
            super.onPageStarted(view, url, favicon)
            progressBar.gone()
        }

        override fun onPageFinished(view: WebView, url: String) {
            super.onPageFinished(view, url)
            progressBar.gone()
        }
    }

    private var queryIndex = 0
    private fun loadURL(loadingURL: String) {
        var urlLoad = loadingURL
        if (!loadingURL.contains("https")) {
            urlLoad = loadingURL.replace("http", "https")
        }
        webView.loadUrl(urlLoad)
        webView.webViewClient = object : WebViewClient() {
            override fun onPageStarted(view: WebView, url: String, favicon: Bitmap?) {
                super.onPageStarted(view, url, favicon)
                progressBar.gone()
            }

            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            override fun onPageFinished(view: WebView, url: String) {
                storeDataOnWebView(urlLoad)
            }

            override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
                view.loadUrl(url)
                checkForExit(url)
                return true
            }

            override fun onReceivedError(view: WebView, request: WebResourceRequest, error: WebResourceError) {
                super.onReceivedError(view, request, error)
                progressBar.gone()
            }

            override fun onReceivedSslError(view: WebView, handler: SslErrorHandler, error: SslError) {
                super.onReceivedSslError(view, handler, error)
                progressBar.gone()
            }
        }
    }

    fun storeDataOnWebView(url: String?) {
        val userId = appCMSPresenter.loggedInUser
        val name   = if (appCMSPresenter.loggedInUserName.isNullOrBlank())  "" else appCMSPresenter.loggedInUserName
        val email  = if (appCMSPresenter.loggedInUserEmail.isNullOrBlank()) "" else appCMSPresenter.loggedInUserEmail
        val phone  = if (appCMSPresenter.loggedInPhone.isNullOrBlank())     "" else appCMSPresenter.loggedInPhone.replace("+","")

        val jsonRequest = if (appCMSPresenter.isUserSubscribed) {
            "localStorage.setItem(\"token\",JSON.stringify({\"authorizationToken\":\"${appCMSPresenter.authToken}\",\"refreshToken\":\"${appCMSPresenter.refreshToken}\",\"expiration\":\"1587182340\"}));localStorage.setItem(\"user\",JSON.stringify({\"user\":{\"isSubscribed\":\"${appCMSPresenter.isUserSubscribed}\",\"userId\":\"$userId\",\"name\":\"$name\",\"email\":\"$email\",\"phone\":\"$phone\"}}))"
        } else {
            "localStorage.setItem(\"token\",JSON.stringify({\"authorizationToken\":\"${appCMSPresenter.authToken}\",\"refreshToken\":\"${appCMSPresenter.refreshToken}\",\"expiration\":\"1587182340\"}));localStorage.setItem(\"user\",JSON.stringify({\"user\":"+ "{\"userId\":\"$userId\",\"name\":\"$name\",\"email\":\"$email\",\"phone\":\"$phone\"}}))"
        }

        if (queryIndex < 2) {
            webView?.evaluateJavascript(jsonRequest) {
                queryIndex += 1
                appCMSPresenter.appPreference?.setbrowserLocalStorage(true)
                appCMSPresenter.appPreference?.setIsbrowserDataAlreadyOpen(true)
                webView?.loadUrl(url)
            }
        }
    }

    var evaluateLocalStorage = 0
    private fun removeBrowserData(appCMSPresenter: AppCMSPresenter, loadingURL: String) {
        webView.loadUrl(loadingURL)
        webView.webViewClient = object : WebViewClient() {
            override fun onPageStarted(view: WebView, url: String, favicon: Bitmap?) {
                super.onPageStarted(view, url, favicon)
                progressBar.gone()
            }

            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            override fun onPageFinished(view: WebView, url: String) {
                if (evaluateLocalStorage < 2) {
                    webView?.evaluateJavascript("localStorage.clear()") {
                        evaluateLocalStorage += 1
                        appCMSPresenter.appPreference?.setbrowserLocalStorage(false)
                        appCMSPresenter.appPreference?.setIsbrowserDataAlreadyOpen(true)
                        webView?.loadUrl(loadingURL)
                    }
                }
            }

            override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
                view.loadUrl(url)
                return true
            }
        }
    }

    private fun checkForExit(url: String) {
        if (isComeFromActivateDevice && !url.contains(deviceActivatePageEndPoint)) {
            finish()
        }
    }
}