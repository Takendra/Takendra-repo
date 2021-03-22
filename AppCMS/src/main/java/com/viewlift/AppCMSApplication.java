package com.viewlift;

import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.RemoteException;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.ProcessLifecycleOwner;
import androidx.multidex.MultiDexApplication;

import com.amazon.alexa.vsk.clientlib.AlexaClientManager;
import com.amazon.device.messaging.ADM;
import com.android.installreferrer.api.InstallReferrerClient;
import com.android.installreferrer.api.InstallReferrerStateListener;
import com.android.installreferrer.api.ReferrerDetails;
import com.appsflyer.AppsFlyerConversionListener;
import com.appsflyer.AppsFlyerLib;
import com.clevertap.android.sdk.ActivityLifecycleCallback;
import com.clevertap.android.sdk.CleverTapAPI;
import com.clevertap.android.sdk.pushnotification.amp.CTPushAmpListener;
import com.google.android.gms.ads.identifier.AdvertisingIdClient;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.viewlift.models.data.appcms.downloads.DownloadMediaMigration;
import com.viewlift.models.network.modules.AppCMSSiteModule;
import com.viewlift.models.network.modules.AppCMSUIModule;
import com.viewlift.offlinedrm.AppCMSOfflineDrmManager;
import com.viewlift.utils.CommonUtils;
import com.viewlift.utils.Macros;
import com.viewlift.views.components.AppCMSOfflineDrmComponent;
import com.viewlift.views.components.AppCMSPresenterComponent;
import com.viewlift.views.components.DaggerAppCMSOfflineDrmComponent;
import com.viewlift.views.components.DaggerAppCMSPresenterComponent;
import com.viewlift.views.modules.AppCMSPresenterModule;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import rx.functions.Action0;

import static com.viewlift.analytics.AppsFlyerUtils.trackInstallationEvent;

/*
 * Created by viewlift on 5/4/17.
 */

public class AppCMSApplication extends MultiDexApplication implements CTPushAmpListener {
    private static String TAG = "AppCMSApp";

    private AppCMSPresenterComponent appCMSPresenterComponent;
    private AppsFlyerConversionListener conversionDataListener;
    private int openActivities;
    private int visibleActivities;
    private Action0 onActivityResumedAction;
    ///////////// OFFLINE DRM Injection /////////////////////////
    @Inject
    AppCMSOfflineDrmManager appCMSOfflineDrmManager;
    ///////////// OFFLINE DRM Injection /////////////////////////

    private void initRealmonfig() {

        Realm.init(this);
        RealmConfiguration config = new RealmConfiguration
                .Builder()
                .schemaVersion(14)
                .migration(new DownloadMediaMigration())
//                .deleteRealmIfMigrationNeeded()  // for Development purpose
                .build();
        Realm.setDefaultConfiguration(config);
    }

  /*  @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(LanguageHelper.onAttach(base, LanguageHelper.getLanguage(base)));
    }*/



    @Override
    public void onCreate() {
        // TODO: always comment for other apps
        super.onCreate();
        long startClver = System.currentTimeMillis();
        ActivityLifecycleCallback.register(this);
        long endClver = System.currentTimeMillis();

        Log.i(TAG, "Time taken in clevertap lifecycle register : " + (endClver - startClver) +" ms");

        long startRealm = System.currentTimeMillis();
        initRealmonfig();
        long emdRealm = System.currentTimeMillis();
        Log.i(TAG, "Time taken in Realm init : " + (emdRealm - startRealm) +" ms");

        openActivities = 0;

        Macros.INSTANCE.setContext(getApplicationContext());

        Log.d(TAG, "checkIsTelevision(): " + checkIsTelevision());
        //checkDeviceProcessor();

        long startDagger = System.currentTimeMillis();
        appCMSPresenterComponent = DaggerAppCMSPresenterComponent
                .builder()
                .appCMSUIModule(new AppCMSUIModule(AppCMSApplication.this))
                .appCMSSiteModule(new AppCMSSiteModule())
                .appCMSPresenterModule(new AppCMSPresenterModule())
                .build();

        appCMSPresenterComponent.appCMSPresenter().setCurrentContext(AppCMSApplication.this);

        //Dependency Injection
        AppCMSOfflineDrmComponent appCMSOfflineDrmComponent = DaggerAppCMSOfflineDrmComponent.create();
        appCMSOfflineDrmComponent.inject(this);
        appCMSOfflineDrmManager.setmContext(AppCMSApplication.this);

        registerActivityLifecycleCallbacks(new ActivityLifecycleCallbacks() {
            @Override
            public void onActivityCreated(@NonNull Activity activity, Bundle savedInstanceState) {
                if (appCMSPresenterComponent != null && appCMSPresenterComponent.appCMSPresenter() != null)
                    appCMSPresenterComponent.appCMSPresenter().setCurrentActivity(activity);
            }

            @Override
            public void onActivityStarted(@NonNull Activity activity) {
                Log.d(TAG, "Activity being started: " + activity.getLocalClassName());
                openActivities++;
                visibleActivities++;
                if (appCMSPresenterComponent != null && appCMSPresenterComponent.appCMSPresenter() != null) {
                    appCMSPresenterComponent.appCMSPresenter().setResumedActivities(visibleActivities);
                }
            }

            @Override
            public void onActivityResumed(@NonNull Activity activity) {
                if (appCMSPresenterComponent != null && appCMSPresenterComponent.appCMSPresenter() != null)
                    appCMSPresenterComponent.appCMSPresenter().setCurrentActivity(activity);

                if (onActivityResumedAction != null) {
                    onActivityResumedAction.call();
                    onActivityResumedAction = null;
                }
            }

            @Override
            public void onActivityPaused(@NonNull Activity activity) {
                Log.d(TAG, "Activity being paused: " + activity.getLocalClassName());
                appCMSPresenterComponent.appCMSPresenter().closeSoftKeyboard();
                visibleActivities--;
                if (appCMSPresenterComponent.appCMSPresenter() != null) {
                    appCMSPresenterComponent.appCMSPresenter().setResumedActivities(visibleActivities);
                }
            }

            @Override
            public void onActivityStopped(@NonNull Activity activity) {
                Log.d(TAG, "Activity being stopped: " + activity.getLocalClassName());
                if (openActivities == 1) {
                    appCMSPresenterComponent.appCMSPresenter().setCancelAllLoads(true);
                }

                openActivities--;
            }

            @Override
            public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

            }

            @Override
            public void onActivityDestroyed(Activity activity) {
                Log.d(TAG, "Activity being destroyed: " + activity.getLocalClassName());
                appCMSPresenterComponent.appCMSPresenter().unsetCurrentActivity(activity);
                appCMSPresenterComponent.appCMSPresenter().closeSoftKeyboard();
            }
        });

        long endDagger = System.currentTimeMillis();

        Log.i(TAG, "Time taken in Dagger init : " + (endDagger - startDagger) +" ms");

        if (Utils.isFireTVDevice(getApplicationContext()) && checkIsTelevision()) {
            try {
                // Initialize the Alexa Video Skills Client Library first.
                initializeAlexaClientLibrary();

                // Initialize ADM.
                initializeAdm();

                AlexaClientManager.getSharedInstance().setAlexaEnabled(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        AppCMSLifeCycleObserver appLifecycleObserver = new AppCMSLifeCycleObserver();
        ProcessLifecycleOwner.get().getLifecycle().addObserver(appLifecycleObserver);
        //Initialize Offline DRM Utils
        initalizationThread.start();
    }



    private void initializeAdm() {
        try {
            final ADM adm = new ADM(this);
            if (adm.isSupported()) {
                if (adm.getRegistrationId() == null) {
                    // ADM is not ready now. You have to start ADM registration by calling
                    // startRegister() API. ADM will calls onRegister() API on your ADM
                    // handler service when ADM registration was completed with registered ADM id.
                    adm.startRegister();
                } else {
                    // [IMPORTANT]
                    // ADM down-channel is already available. This is a common case that your
                    // application restarted. ADM manager on your Fire TV cache the previous
                    // ADM registration info and provide it immediately when your application
                    // is identified as restarted.
                    //
                    // You have to provide the retrieved ADM registration Id to the VSK Client library.
                    final String admRegistrationId = adm.getRegistrationId();
                    Log.i(TAG, "ADM registration Id:" + admRegistrationId);

                    // Provide the acquired ADM registration ID.
                    final AlexaClientManager alexaClientManager = AlexaClientManager.getSharedInstance();
                    alexaClientManager.setDownChannelReady(true, admRegistrationId);
                }
            }
        } catch (Exception ex) {
            Log.e(TAG, "ADM initialization has failed with exception", ex);
        }
    }


    public void initializeAlexaClientLibrary() {
        // Retrieve the shared instance of the AlexaClientManager
        AlexaClientManager clientManager = AlexaClientManager.getSharedInstance();

        // Gather your Skill ID and list of capabilities
        final String alexaSkillId = "amzn1.ask.skill.3cc5691b-cd12-4429-b399-d00e8cb52fae";

        // Create a list of supported capabilities in your skill.
        List<String> capabilities = new ArrayList<>();
        capabilities.add(AlexaClientManager.CAPABILITY_REMOTE_VIDEO_PLAYER);
        capabilities.add(AlexaClientManager.CAPABILITY_PLAY_BACK_CONTROLLER);
        capabilities.add(AlexaClientManager.CAPABILITY_SEEK_CONTROLLER);

        clientManager.initialize(this.getApplicationContext(),
                alexaSkillId,
                AlexaClientManager.SKILL_STAGE_LIVE,
                capabilities);
    }


    public AppCMSPresenterComponent getAppCMSPresenterComponent() {
        return appCMSPresenterComponent;
    }

    public void initAppsFlyer(String appsFlyerKey) {
        try {
            conversionDataListener = new AppsFlyerConversionListener() {

                /* Returns the attribution data. Note - the same conversion data is returned every time per install */

               /* public void onInstallConversionDataLoaded(Map<String, Object> conversionData) {
                    Log.d("AppsFlyerLib", "onInstallConversionDataLoaded");
                   for (String attrName : conversionData.keySet()) {
                        Log.d("AppsFlyerLib", "attribute: " + attrName + " = " + conversionData.get(attrName));
                    }
                    // using iterators
                    Iterator<Map.Entry<String, Object>> itr = conversionData.entrySet().iterator();

                    while(itr.hasNext())
                    {
                        Map.Entry<String, Object> entry = itr.next();
                        System.out.println("AppsFlyerLib"
                                +":- Key = " + entry.getKey()
                                + ", Value = " + entry.getValue());
                    }

                    getAppCMSPresenterComponent().appCMSPresenter().setAppsFlyerConversionData(conversionData);
                }*/

                /*@Override
                public void onInstallConversionFailure(String errorMessage) {
                    Log.d("AppsFlyerLib", "error getting conversion data: " + errorMessage);
                }*/

                @Override
                public void onConversionDataSuccess(Map<String, Object> conversionData) {

                   /* Log.d("AppsFlyerLib", "onConversionDataSuccess");
                    for (String attrName : conversionData.keySet()) {
                        Log.d("AppsFlyerLib", "attribute: " + attrName + " = " + conversionData.get(attrName));
                    }
                    // using iterators
                    Iterator<Map.Entry<String, Object>> itr = conversionData.entrySet().iterator();

                    while(itr.hasNext())
                    {
                        Map.Entry<String, Object> entry = itr.next();
                        System.out.println("AppsFlyerLib"
                                +":- Key = " + entry.getKey()
                                + ", Value = " + entry.getValue());
                    }*/

                    getAppCMSPresenterComponent().appCMSPresenter().setAppsFlyerConversionData(conversionData);
                }

                @Override
                public void onConversionDataFail(String s) {

                }

                /* Called only when a Deep Link is opened */
                @Override
                public void onAppOpenAttribution(Map<String, String> conversionData) {
                    /*for (String attrName : conversionData.keySet()) {
                        Log.d(AppsFlyerLib.LOG_TAG, "attribute: " + attrName + " = " + conversionData.get(attrName));
                    }*/
                }

                @Override
                public void onAttributionFailure(String errorMessage) {
                    Log.d("AppsFlyerLib", "error onAttributionFailure : " + errorMessage);
                }
            };
            AppsFlyerLib.getInstance().init(appsFlyerKey, conversionDataListener, this);
            AppsFlyerLib.getInstance().startTracking(this);
            AppsFlyerLib.getInstance().setCollectIMEI(false);
            AppsFlyerLib.getInstance().setCollectAndroidID(false);

            sendAnalytics();
        } catch (Exception e) {
            e.printStackTrace();

    }}

    private void sendAnalytics() {
        trackInstallationEvent(this);
    }

    public Action0 getOnActivityResumedAction() {
        return onActivityResumedAction;
    }

    public void setOnActivityResumedAction(Action0 onActivityResumedAction) {
        this.onActivityResumedAction = onActivityResumedAction;
    }

    private boolean checkIsTelevision() {
        int uiMode = getResources().getConfiguration().uiMode;
        return (uiMode & Configuration.UI_MODE_TYPE_MASK) == Configuration.UI_MODE_TYPE_TELEVISION;
    }

    /**
     * Retrieve the Android Advertising Id
     * The device must be KitKat (4.4)+
     * This method must be invoked from a background thread.
     */
    public synchronized String getAdId(Context context) {

        if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.KITKAT) {
            return null;
        }
        AdvertisingIdClient.Info idInfo = null;
        String advertId = null;
        try {
            idInfo = AdvertisingIdClient.getAdvertisingIdInfo(context);
            advertId = idInfo.getId();
        } catch (GooglePlayServicesNotAvailableException | GooglePlayServicesRepairableException | IOException e) {
            e.printStackTrace();
        }
//        Log.i(TAG, "***** 123 advertId="+ advertId);
        Utils.setAddId(advertId);
        return advertId;
    }

    void initInstallReferrer() {
        try {
        InstallReferrerClient referrerClient;
        referrerClient = InstallReferrerClient.newBuilder(this).build();


            referrerClient.startConnection(new InstallReferrerStateListener() {
                @Override
                public void onInstallReferrerSetupFinished(int responseCode) {
                    switch (responseCode) {
                        case InstallReferrerClient.InstallReferrerResponse.OK:
                            //  this implementation is done for installreferrer update
                            // TODO : client specific requirement changes for response
                             ReferrerDetails response = null;
                            try {
                                response = referrerClient.getInstallReferrer();
                                String referrerUrl = response.getInstallReferrer();
                                long referrerClickTime = response.getReferrerClickTimestampSeconds();
                                long appInstallTime = response.getInstallBeginTimestampSeconds();
                                boolean instantExperienceLaunched = response.getGooglePlayInstantParam();
                            } catch (RemoteException e) {
                                e.printStackTrace();
                            }catch (NullPointerException ex){
                                ex.printStackTrace();
                            }
                            break;
                        case InstallReferrerClient.InstallReferrerResponse.FEATURE_NOT_SUPPORTED:
                            // API not available on the current Play Store app.
                            break;
                        case InstallReferrerClient.InstallReferrerResponse.SERVICE_UNAVAILABLE:
                            // Connection couldn't be established.
                            break;
                    }
                }

                @Override
                public void onInstallReferrerServiceDisconnected() {
                    // Try to restart the connection on the next request to
                    // Google Play by calling the startConnection() method.
                }
            });
        } catch (RuntimeException e) {
            e.printStackTrace();
        }
    }

    public AppCMSOfflineDrmManager getOfflineDRMManager() {
        return appCMSOfflineDrmManager;
    }

    Thread initalizationThread = new Thread() {
        @Override
        public void run() {
            super.run();

            conversionDataListener = new AppsFlyerConversionListener() {


                @Override
                public void onConversionDataSuccess(Map<String, Object> map) {

                }

                @Override
                public void onConversionDataFail(String s) {

                }

                @Override
                public void onAppOpenAttribution(Map<String, String> map) {

                }

                @Override
                public void onAttributionFailure(String s) {

                }
            };

            try {
                getAdId(getApplicationContext());
            } catch (Exception e) {
                e.printStackTrace();
            }

            initInstallReferrer();

            if(appCMSPresenterComponent != null && appCMSPresenterComponent.appCMSPresenter()!=null
                    && appCMSPresenterComponent.appCMSPresenter().getAppPreference()!=null ){
                if(appCMSPresenterComponent.appCMSPresenter().isUserLoggedIn()){

                    if(appCMSPresenterComponent.appCMSPresenter().getAppPreference().getAuthToken() != null){
                        CommonUtils.setCountryCode(CommonUtils.getCountryCodeFromAuthToken(appCMSPresenterComponent.appCMSPresenter().getAppPreference().getAuthToken()));
                    }
                }else{
                    if(appCMSPresenterComponent.appCMSPresenter().getAppPreference().getAnonymousUserToken() != null){
                        CommonUtils.setCountryCode(CommonUtils.getCountryCodeFromAuthToken(appCMSPresenterComponent.appCMSPresenter().getAppPreference().getAnonymousUserToken()));
                    }
                }
            }

        }

    };

    @Override
    public void onPushAmpPayloadReceived(Bundle extras) {
        CleverTapAPI.createNotification(this, extras);
    }

    /*
    public void checkDeviceProcessor(){
        ActivityManager activityManager = (ActivityManager) this.getSystemService(Context.ACTIVITY_SERVICE);
        if (activityManager.getMemoryClass()>=128 &&
                Runtime.getRuntime().availableProcessors()>=4 &&
                freeRamMemorySize() > 1500){
            //CommonUtils.setHighPerformingDevice(true);
        }
    }
    private long freeRamMemorySize() {
        ActivityManager.MemoryInfo mi = new ActivityManager.MemoryInfo();
        ActivityManager activityManager = (ActivityManager) this.getSystemService(Context.ACTIVITY_SERVICE);
        activityManager.getMemoryInfo(mi);
        long availableMegs = mi.availMem / 1048576L;

        return availableMegs;
    }
     */
}
