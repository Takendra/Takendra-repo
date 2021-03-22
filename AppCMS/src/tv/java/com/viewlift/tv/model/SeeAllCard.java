package com.viewlift.tv.model;

import com.viewlift.models.data.appcms.api.Module;

public class SeeAllCard {
    private final Module moduleAPI;

    public SeeAllCard(Module moduleAPI) {
        this.moduleAPI = moduleAPI;
    }

    public Module getModuleAPI() {
        return moduleAPI;
    }
}
