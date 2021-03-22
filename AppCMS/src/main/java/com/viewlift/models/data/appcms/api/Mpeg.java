package com.viewlift.models.data.appcms.api;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.viewlift.utils.Macros;
import com.vimeo.stag.UseStag;

import java.io.Serializable;

@UseStag
public class Mpeg implements Serializable, BaseInterface {

    @SerializedName("codec")
    @Expose
    String codec;

    @SerializedName("renditionValue")
    @Expose
    String renditionValue;

    @SerializedName("url")
    @Expose
    String url;

    @SerializedName("bitrate")
    @Expose
    int bitrate;

    public String getCodec() {
        return codec;
    }

    public void setCodec(String codec) {
        this.codec = codec;
    }

    public String getRenditionValue() {
        return renditionValue;
    }

    public void setRenditionValue(String renditionValue) {
        this.renditionValue = renditionValue;
    }

    public String getUrl() {
        if (url != null)
            return Macros.INSTANCE.replaceURl(url);
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getBitrate() {
        return bitrate;
    }

    public void setBitrate(int bitrate) {
        this.bitrate = bitrate;
    }
}
