package com.viewlift.tv.views.customviews;

import com.viewlift.models.data.appcms.api.Module;

public class BrowseFragmentShimmerCard {
    private final Module moduleAPI;

    public BrowseFragmentShimmerCard(Module moduleAPI) {
        this.moduleAPI = moduleAPI;
    }

    public Module getModuleAPI() {
        return moduleAPI;
    }
}