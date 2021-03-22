package com.viewlift.models.data.appcms.ui.page;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.vimeo.stag.UseStag;

import java.io.Serializable;

@UseStag
public class Items implements Serializable {

    @SerializedName("imageUrl")
    @Expose
    String imageUrl;

    @SerializedName("imageURL")
    @Expose
    String imageUrl2;

    @SerializedName("title")
    @Expose
    String title;

    @SerializedName("shortParagraph")
    @Expose
    String shortParagraph;

    @SerializedName("buttonText")
    @Expose
    String buttonText;

    @SerializedName("backgroundColor")
    @Expose
    String backgroundColor;

    @SerializedName("buttonLink")
    @Expose
    String buttonLink;

    @SerializedName("id")
    @Expose
    String id;


    @SerializedName("textColor")
    @Expose
    String textColor;

    public String getImageUrl() {
        return imageUrl;
    }

    public String getImageUrl2() {
        return imageUrl2;
    }

    public void setImageUrl2(String imageUrl2) {
        this.imageUrl2 = imageUrl2;
    }

    public String getTitle() {
        return title;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getShortParagraph() {
        return shortParagraph;
    }

    public void setShortParagraph(String shortParagraph) {
        this.shortParagraph = shortParagraph;
    }

    public String getButtonText() {
        return buttonText;
    }

    public void setButtonText(String buttonText) {
        this.buttonText = buttonText;
    }

    public String getBackgroundColor() {
        return backgroundColor;
    }

    public void setBackgroundColor(String backgroundColor) {
        this.backgroundColor = backgroundColor;
    }

    public String getButtonLink() {
        return buttonLink;
    }

    public void setButtonLink(String buttonLink) {
        this.buttonLink = buttonLink;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTextColor() {
        return textColor;
    }

    public void setTextColor(String textColor) {
        this.textColor = textColor;
    }
}
