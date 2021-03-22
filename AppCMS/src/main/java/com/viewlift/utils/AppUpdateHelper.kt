package com.viewlift.utils

import android.content.IntentSender
import android.view.View
import com.google.android.exoplayer2.util.Log
import com.google.android.material.snackbar.Snackbar
import com.google.android.play.core.appupdate.AppUpdateInfo
import com.google.android.play.core.appupdate.AppUpdateManagerFactory
import com.google.android.play.core.install.InstallState
import com.google.android.play.core.install.InstallStateUpdatedListener
import com.google.android.play.core.install.model.AppUpdateType
import com.google.android.play.core.install.model.InstallStatus
import com.google.android.play.core.install.model.UpdateAvailability
import com.viewlift.R
import com.viewlift.presenters.AppCMSPresenter

class AppUpdateHelper(private val mAppCMSPresenter: AppCMSPresenter) : InstallStateUpdatedListener {

    private var isProcessOneTime = false

    private val mAppUpdateManager by lazy {
        AppUpdateManagerFactory.create(mAppCMSPresenter.currentActivity)
    }


    fun checkAppUpgradeAvailable() {
        when {
            mAppCMSPresenter.isAppBelowMinVersion -> processAppUpdate()
            mAppCMSPresenter.isAppUpgradeAvailable && !isProcessOneTime -> processAppUpdate()
        }
    }

    private fun processAppUpdate() {
        mAppUpdateManager.appUpdateInfo.addOnSuccessListener { appUpdateInfo: AppUpdateInfo ->
            if (appUpdateInfo.installStatus() == InstallStatus.DOWNLOADED) {
                popupSnackBarForCompleteUpdate()
            } else if (appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE
                    || appUpdateInfo.updateAvailability() == UpdateAvailability.DEVELOPER_TRIGGERED_UPDATE_IN_PROGRESS) {
                // If an in-app update is already running, resume the update.
                startUpdateFlow(appUpdateInfo)
            }
        }.addOnFailureListener { e: Exception -> Log.e("AppUpdate", "error:$e") }
    }

    private fun startUpdateFlow(appUpdateInfo: AppUpdateInfo) {
        try {
            if (mAppCMSPresenter.currentActivity != null) {
                isProcessOneTime = true
                mAppUpdateManager.startUpdateFlowForResult(appUpdateInfo, appUpdateType, mAppCMSPresenter.currentActivity, APP_UPDATE_REQUEST_CODE)
            }
        } catch (e: IntentSender.SendIntentException) {
            e.printStackTrace()
        }
    }

    override fun onStateUpdate(installState: InstallState) {
        if (installState.installStatus() == InstallStatus.DOWNLOADED) {
            // After the update is downloaded, show a notification
            // and request user confirmation to restart the app.
            popupSnackBarForCompleteUpdate()
            isProcessOneTime = false
        }
    }

    private val appUpdateType: Int
        get() {
            if (mAppCMSPresenter.isAppBelowMinVersion) {
                return AppUpdateType.IMMEDIATE
            }
            mAppUpdateManager.registerListener(this)
            return AppUpdateType.FLEXIBLE
        }

    private fun popupSnackBarForCompleteUpdate() {
        if (mAppCMSPresenter.currentActivity != null && mAppCMSPresenter.localisedStrings != null) {
            val parentView = mAppCMSPresenter.currentActivity.findViewById<View>(R.id.content)
            if (parentView != null) {
                Snackbar.make(parentView, mAppCMSPresenter.localisedStrings.inAppUpdateDownloadMsg, Snackbar.LENGTH_INDEFINITE).apply {
                    setAction(mAppCMSPresenter.localisedStrings.inAppUpdateInstallLabel) { mAppUpdateManager.completeUpdate() }
                    setActionTextColor(mAppCMSPresenter.blockTitleTextColor)
                }.show()
            }
        }
        removeCallback()
    }

    private fun removeCallback() {
        try {
            if(mAppCMSPresenter.currentActivity != null)
                mAppUpdateManager.unregisterListener(this)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun onDestroy() {
        isProcessOneTime = false
        removeCallback()
    }

    companion object {
        private const val APP_UPDATE_REQUEST_CODE = 1001
    }
}