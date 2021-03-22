package com.viewlift.tv.views.customviews;

import com.viewlift.models.data.appcms.api.Module;

public class BrowseFragmentLoaderCard {
    private final Module moduleAPI;

    public BrowseFragmentLoaderCard(Module moduleAPI) {
        this.moduleAPI = moduleAPI;
    }

    public Module getModuleAPI() {
        return moduleAPI;
    }
}