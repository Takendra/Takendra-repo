package com.viewlift.models.data.appcms.api;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.viewlift.utils.Macros;
import com.vimeo.stag.UseStag;

import java.io.Serializable;
import java.util.List;

@UseStag
public class VideoAssets implements Serializable {

    @SerializedName("type")
    @Expose
    String type;

    @SerializedName("mpeg")
    @Expose
    List<Mpeg> mpeg = null;

    @SerializedName("hls")
    @Expose
    String hls;

    @SerializedName("hlsDetail")
    @Expose
    HlsDetail hlsDetail;

    @SerializedName("widevine")
    @Expose
    WideVine wideVine;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<Mpeg> getMpeg() {
        return mpeg;
    }

    public void setMpeg(List<Mpeg> mpeg) {
        this.mpeg = mpeg;
    }

    public String getHls() {
        if (hls != null)
            return Macros.INSTANCE.replaceURl(hls);
        return hls;
    }

    public void setHls(String hls) {
        this.hls = hls;
    }

    public WideVine getWideVine() {
        return wideVine;
    }

    public void setWideVine(WideVine wideVine) {
        this.wideVine = wideVine;
    }

    public HlsDetail getHlsDetail() {
        return hlsDetail;
    }

    public void setHlsDetail(HlsDetail hlsDetail) {
        this.hlsDetail = hlsDetail;
    }
}
