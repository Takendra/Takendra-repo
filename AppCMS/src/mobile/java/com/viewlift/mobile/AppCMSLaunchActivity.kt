package com.viewlift.mobile

import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.ActivityInfo
import android.net.ConnectivityManager
import android.net.Uri
import android.os.Bundle
import android.telephony.TelephonyManager
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging
import com.urbanairship.UAirship
import com.urbanairship.actions.DeepLinkListener
import com.viewlift.AppCMSApplication
import com.viewlift.R
import com.viewlift.Utils
import com.viewlift.presenters.AppCMSPresenter
import com.viewlift.utils.CommonUtils
import com.viewlift.views.binders.AppCMSBinder
import com.viewlift.views.components.AppCMSPresenterComponent
import com.viewlift.views.customviews.BaseView
import java.util.concurrent.TimeUnit
import com.urbanairship.automation.InAppAutomation
import com.viewlift.views.airship.AirshipInAppMessageAdapter
import com.urbanairship.iam.InAppMessage


class AppCMSLaunchActivity : AppCompatActivity(), DeepLinkListener {
    private val TAG = "AppCMSLaunchActivity"

    private var searchQuery: Uri? = null

    private var presenterCloseActionReceiver: BroadcastReceiver? = null
    private var connectivityManager: ConnectivityManager? = null
    private var networkConnectedReceiver: BroadcastReceiver? = null
    private var appStartWithNetworkConnected = false

    private var appCMSPresenterComponent: AppCMSPresenterComponent? = null

    @SuppressLint("SourceLockedOrientationActivity")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

         // android.os.Debug.waitForDebugger()

        if (!BaseView.isTablet(this)) {
            requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        }
        connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        setContentView(R.layout.activity_launch)
//        if (!isTaskRoot()
//                && getIntent().hasCategory(Intent.CATEGORY_LAUNCHER)
//                && getIntent().getAction() != null
//                && getIntent().getAction().equals(Intent.ACTION_MAIN)) {
//            if(intent != null && intent.data != null
//                    &&intent.getAction().equals(Intent.ACTION_VIEW)){
//                this.isDeeplink=true
//                setDeeplinkIntent(intent);
//                handleIntent(intent)
//            }
//            finish();
//            return;
//        }
         var updatedAppCMSBinder: AppCMSBinder? = null
        val args = intent.getBundleExtra(getString(R.string.app_cms_bundle_key))
        if( getIntent().getAction() != null&&getIntent().getAction().equals(Intent.ACTION_VIEW)){
            appCMSPresenterComponent = (application as AppCMSApplication).appCMSPresenterComponent
             updatedAppCMSBinder =appCMSPresenterComponent?.appCMSPresenter()?.currentAppCMSBinder
            if(updatedAppCMSBinder!=null){

            }
        }
        setFullScreenFocus()

        init.start()
    }

    fun getCountryCode() : String{
        val manager: TelephonyManager = this.getSystemService(TELEPHONY_SERVICE) as TelephonyManager
        if(!CommonUtils.isEmpty(manager.getSimCountryIso()))
            return manager.getSimCountryIso().toUpperCase()
        else if(!CommonUtils.isEmpty(manager.networkCountryIso))
            return manager.networkCountryIso.toUpperCase()
        else
            return this.getResources().getConfiguration().locale.getCountry()
    }

    var init: Thread = object : Thread() {
        override fun run() {
            super.run()
            Utils.setCountryCode(getCountryCode());
            if (application is AppCMSApplication) {
                appCMSPresenterComponent = (application as AppCMSApplication).appCMSPresenterComponent
                appCMSPresenterComponent?.appCMSPresenter()?.resetLaunched()
                appCMSPresenterComponent?.appCMSPresenter()?.isPinVerified = false
            }
            if (intent != null && intent.data != null) searchQuery = intent.data
            UAirship.shared().deepLinkListener = this@AppCMSLaunchActivity
            presenterCloseActionReceiver = object : BroadcastReceiver() {
                override fun onReceive(context: Context, intent: Intent?) {
                    if ( intent?.getStringExtra(getString(R.string.app_cms_package_name_key)) != null &&
                            intent?.getStringExtra(getString(R.string.app_cms_package_name_key)) != packageName) {
                        return
                    }
                    if (intent?.action == AppCMSPresenter.PRESENTER_CLOSE_SCREEN_ACTION) {
                        finish()
                    }
                }
            }
            registerReceiver(presenterCloseActionReceiver,
                    IntentFilter(AppCMSPresenter.PRESENTER_CLOSE_SCREEN_ACTION))
            networkConnectedReceiver = object : BroadcastReceiver() {
                override fun onReceive(context: Context, intent: Intent?) {
                    if ( intent?.getStringExtra(getString(R.string.app_cms_package_name_key)) != null &&
                            intent?.getStringExtra(getString(R.string.app_cms_package_name_key)) != packageName) {
                        return
                    }
                    val activeNetwork = connectivityManager!!.activeNetworkInfo
                    val isConnected = activeNetwork != null &&
                            activeNetwork.isConnectedOrConnecting
                    if (!appStartWithNetworkConnected && isConnected && appCMSPresenterComponent != null) {
                        appCMSPresenterComponent!!.appCMSPresenter().getAppCMSMain(this@AppCMSLaunchActivity,
                                Utils.getProperty("SiteId", applicationContext),
                                searchQuery,
                                AppCMSPresenter.PlatformType.ANDROID,
                                true) { aBoolean -> if (aBoolean) handleIntent(getIntent()) }
                    } else if (!isConnected) {
                        appStartWithNetworkConnected = false
                    }
                }
            }

            //Firebase Deeplink handlinng
            if (intent.extras != null) {
                val fcmDeepLinkBundle = intent.extras
                for (key in fcmDeepLinkBundle!!.keySet()) {
                    if (key.equals("deeplink", ignoreCase = true)) {
                        val value = intent.extras!![key]
                        Log.d(TAG, "Key: $key Value: $value")
                        searchQuery = Uri.parse(value.toString())
                        appCMSPresenterComponent?.appCMSPresenter()?.sendDeepLinkAction(searchQuery)
                    } else if(key.equals("openBrowser", ignoreCase = true)){
                        val openBrowser = intent.extras!![key]
                        appCMSPresenterComponent?.appCMSPresenter()?.sendOpenBrowserLinkAction(Uri.parse(openBrowser.toString()))
                    }
                }
            }
       // UAirship.shared().channelCapture.enable(180, TimeUnit.SECONDS)
            println("UA Device Channel ID: " + UAirship.shared().channel.id)
          InAppAutomation.shared().getInAppMessageManager().setDisplayInterval(10, TimeUnit.SECONDS);
        //    InAppAutomation.shared().getInAppMessageManager().setAdapterFactory(InAppMessage.TYPE_MODAL, { message: InAppMessage? -> return new CustomInAppMessageAdapter(message); })
            InAppAutomation.shared().getInAppMessageManager().setAdapterFactory(InAppMessage.TYPE_MODAL)  { message -> AirshipInAppMessageAdapter(message) }
            FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
                if (!task.isSuccessful) {
                    Log.w(TAG, "Fetching FCM registration token failed", task.exception)
                    return@OnCompleteListener
                }

                // Get new FCM registration token
                val token = task.result

                // Log and toast
                val msg = "onNewToken :- " + token
                Log.d(TAG, msg)

            })
        }
    }
    var isDeeplink:Boolean=false
    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        handleIntent(intent)
        if(intent != null && intent.data != null
                &&intent.getAction().equals(Intent.ACTION_VIEW)){
            this.isDeeplink=true
           setDeeplinkIntent(intent);
        }
    }
    var deeplinkintent: Intent? = null
    private fun setDeeplinkIntent(intent: Intent?) {
        deeplinkintent=intent
    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        setFullScreenFocus()
        super.onWindowFocusChanged(hasFocus)
    }

    override fun onDestroy() {
        super.onDestroy()
        try {
            unregisterReceiver(presenterCloseActionReceiver)
            UAirship.shared().deepLinkListener = null
        } catch (e: Exception) {
            //Log.e(TAG, "Failed to unregister Close Action Receiver");
        }
    }

    override fun onResume() {
        super.onResume()
        val activeNetwork = connectivityManager!!.activeNetworkInfo
        appStartWithNetworkConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting
        if (appStartWithNetworkConnected) {
            registerReceiver(networkConnectedReceiver,
                    IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION))
        }
        if (appCMSPresenterComponent != null) {
            try {
                if (appCMSPresenterComponent?.appCMSPresenter()?.isLaunched!!) {
                    Log.w(TAG, "Sending close others action")
                    appCMSPresenterComponent?.appCMSPresenter()?.sendCloseOthersAction(null, true, true)
                } else {
                    Log.w(TAG, "Retrieving main.json")

                    appCMSPresenterComponent?.appCMSPresenter()?.getAppCMSMain(this,
                            Utils.getProperty("SiteId", applicationContext),
                            searchQuery,
                            AppCMSPresenter.PlatformType.ANDROID,
                            true) { aBoolean -> if (aBoolean) {
                            if(isDeeplink!!){
                                handleIntent(deeplinkintent)
                                this.isDeeplink=false
                            } else
                            handleIntent(intent)
                        }
                    }
                }
            } catch (e: java.lang.Exception) {
                //Log.e(TAG, "Caught exception retrieving AppCMS data: " + e.getMessage());
            }
        }
    }

    override fun onPause() {
        super.onPause()
        try {
            unregisterReceiver(networkConnectedReceiver)
        } catch (e: java.lang.Exception) {
            //Log.e(TAG, "Failed to unregister network receiver");
        }
    }

    private fun setFullScreenFocus() {
        window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION // hide nav bar
                or View.SYSTEM_UI_FLAG_FULLSCREEN // hide status bar
                or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        try {
            (application as AppCMSApplication).appCMSPresenterComponent.appCMSPresenter().sendCloseOthersAction("Error Screen", false, false)
        } catch (e: java.lang.Exception) {
            //Log.e(TAG, "Caught exception attempting to send close others action: " + e.getMessage());
        }
        finish()
    }

    override fun onDeepLink(deeplink: String): Boolean {
        if (appCMSPresenterComponent != null && deeplink != null) {
            searchQuery = Uri.parse(deeplink)
            appCMSPresenterComponent?.appCMSPresenter()?.appPreference?.setUADeepLink(deeplink)
        }
        return false
    }

    fun handleIntent(intent: Intent?) {
        if (intent != null && intent.data != null && appCMSPresenterComponent != null) {
            searchQuery = intent.data
            if (appCMSPresenterComponent != null) {
                appCMSPresenterComponent!!.appCMSPresenter().sendDeepLinkAction(searchQuery)
                searchQuery = null
            }
        } else {
            if ((appCMSPresenterComponent?.appCMSPresenter()?.appPreference?.getUADeepLink()?.length ?: 0) > 0) {
                searchQuery = Uri.parse(appCMSPresenterComponent!!.appCMSPresenter().appPreference.getUADeepLink())
                appCMSPresenterComponent!!.appCMSPresenter().appPreference.setUADeepLink(null)
                appCMSPresenterComponent!!.appCMSPresenter().sendDeepLinkAction(searchQuery)
                searchQuery = null
            }
        }
    }
}
