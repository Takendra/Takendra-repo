package com.viewlift.models.data.appcms.ui.page;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.viewlift.models.data.appcms.ui.android.NavigationPrimary;
import com.vimeo.stag.UseStag;

import java.io.Serializable;
import java.util.List;

@UseStag
public class BottomTab implements Serializable {


    @SerializedName("headers")
    private List<HeadersBean> headersX;


    public List<HeadersBean> getHeadersX() {
        return headersX;
    }

    public void setHeadersX(List<HeadersBean> headersX) {
        this.headersX = headersX;
    }


    public static class HeadersBean {

        private String id;
        private String title;
        private boolean type;
        private String logoURL;


        @SerializedName("data")
        @Expose
        List<NavigationPrimary> navigationPrimary = null;


        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public boolean isType() {
            return type;
        }

        public void setType(boolean type) {
            this.type = type;
        }

        public String getLogoURL() {
            return logoURL;
        }

        public void setLogoURL(String logoURL) {
            this.logoURL = logoURL;
        }

        public List<NavigationPrimary> getNavigationPrimary() {
            return navigationPrimary;
        }

        public void setNavigationPrimary(List<NavigationPrimary> navigationPrimary) {
            this.navigationPrimary = navigationPrimary;
        }


    }
}
