package com.viewlift.utils;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;

import androidx.browser.customtabs.CustomTabsIntent;
import androidx.browser.customtabs.CustomTabsService;

import com.viewlift.views.activity.WebViewActivity;

import java.util.ArrayList;
import java.util.List;

public final class CustomTabsHelper {
    private static final String STABLE_PACKAGE = "com.android.chrome";
    private static final String BETA_PACKAGE = "com.chrome.beta";
    private static final String DEV_PACKAGE = "com.chrome.dev";
    private static final String LOCAL_PACKAGE = "com.google.android.apps.chrome";
    private static final String TAG = CustomTabsHelper.class.getSimpleName();
    private static String packageNameToUse;

    private CustomTabsHelper() {
    }


    public static void openCustomTab(final Context context, final CustomTabsIntent customTabsIntent, String url) {
        if (!url.startsWith("http"))
            url = "https://" + url;

        final Uri uri = Uri.parse(url);
        String packageName = CustomTabsHelper.getPackageNameToUse(context, uri);
        if (!TextUtils.isEmpty(packageName))
            customTabsIntent.intent.setPackage(packageName);
        try {
            customTabsIntent.launchUrl(context, uri);
        } catch (ActivityNotFoundException e) {
            Intent intent = new Intent(context, WebViewActivity.class);
            intent.putExtra("url", url);
            context.startActivity(intent);
        }
    }

    /**
     * Goes through all apps that handle VIEW intents and have a warmup service. Picks
     * the one chosen by the user if there is one, otherwise makes a best effort to return a
     * valid package name.
     * <p>
     * This is <strong>not</strong> threadsafe.
     *
     * @param context {@link Context} to use for accessing {@link PackageManager}.
     * @return The package name recommended to use for connecting to custom tabs related components.
     */
    private static String getPackageNameToUse(final Context context, final Uri uri) {
        if (packageNameToUse != null) {
            return packageNameToUse;
        }

        PackageManager pm = context.getPackageManager();
        // Get default VIEW intent handler.
        Intent activityIntent = new Intent(Intent.ACTION_VIEW, uri);
        ResolveInfo defaultViewHandlerInfo = pm.resolveActivity(activityIntent, 0);
        String defaultViewHandlerPackageName = null;
        if (defaultViewHandlerInfo != null) {
            defaultViewHandlerPackageName = defaultViewHandlerInfo.activityInfo.packageName;
        }

        // Get all apps that can handle VIEW intents.
        List<ResolveInfo> resolvedActivityList;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            resolvedActivityList = pm.queryIntentActivities(activityIntent, PackageManager.MATCH_ALL);
        } else {
            resolvedActivityList = pm.queryIntentActivities(activityIntent, 0);
        }
        List<String> packagesSupportingCustomTabs = new ArrayList<>();
        for (ResolveInfo info : resolvedActivityList) {
            Intent serviceIntent = new Intent();
            serviceIntent.setAction(CustomTabsService.ACTION_CUSTOM_TABS_CONNECTION);
            serviceIntent.setPackage(info.activityInfo.packageName);
            if (pm.resolveService(serviceIntent, 0) != null) {
                packagesSupportingCustomTabs.add(info.activityInfo.packageName);
            }
        }

        // Now packagesSupportingCustomTabs contains all apps that can handle both VIEW intents
        // and service calls.
        if (packagesSupportingCustomTabs.isEmpty()) {
            packageNameToUse = null;
        } else if (packagesSupportingCustomTabs.size() == 1) {
            packageNameToUse = packagesSupportingCustomTabs.get(0);
        } else if (!TextUtils.isEmpty(defaultViewHandlerPackageName)
                && !hasSpecializedHandlerIntents(context, activityIntent)
                && packagesSupportingCustomTabs.contains(defaultViewHandlerPackageName)) {
            packageNameToUse = defaultViewHandlerPackageName;
        } else if (packagesSupportingCustomTabs.contains(STABLE_PACKAGE)) {
            packageNameToUse = STABLE_PACKAGE;
        } else if (packagesSupportingCustomTabs.contains(BETA_PACKAGE)) {
            packageNameToUse = BETA_PACKAGE;
        } else if (packagesSupportingCustomTabs.contains(DEV_PACKAGE)) {
            packageNameToUse = DEV_PACKAGE;
        } else if (packagesSupportingCustomTabs.contains(LOCAL_PACKAGE)) {
            packageNameToUse = LOCAL_PACKAGE;
        }
        return packageNameToUse;
    }

    /**
     * Used to check whether there is a specialized handler for a given intent.
     *
     * @param intent The intent to check with.
     * @return Whether there is a specialized handler for the given intent.
     */
    private static boolean hasSpecializedHandlerIntents(final Context context, final Intent intent) {
        try {
            PackageManager pm = context.getPackageManager();
            List<ResolveInfo> handlers = pm.queryIntentActivities(intent, PackageManager.GET_RESOLVED_FILTER);
            if (handlers == null || handlers.size() == 0) {
                return false;
            }
            for (ResolveInfo resolveInfo : handlers) {
                IntentFilter filter = resolveInfo.filter;
                if (filter == null) continue;
                if (filter.countDataAuthorities() == 0 || filter.countDataPaths() == 0)
                    continue;
                if (resolveInfo.activityInfo == null) continue;
                return true;
            }
        } catch (RuntimeException e) {
            Log.e(TAG, "Runtime exception while getting specialized handlers");
        }
        return false;
    }
}