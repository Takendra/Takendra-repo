package com.viewlift.views.components;


import com.viewlift.AppCMSApplication;
import com.viewlift.offlinedrm.AppCMSOfflineDrmManager;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component
public interface AppCMSOfflineDrmComponent {
    AppCMSOfflineDrmManager getOfflineDRMManager();

    void inject(AppCMSApplication appCMSApplication);
}
