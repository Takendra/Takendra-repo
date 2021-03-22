package com.viewlift.models.data.appcms.ui.page;

import androidx.constraintlayout.widget.ConstraintSet;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.vimeo.stag.UseStag;

import java.io.Serializable;

@UseStag
public class Mobile implements Serializable {

    @SerializedName("width")
    @Expose
    float width;

    @SerializedName("height")
    @Expose
    float height;

    @SerializedName("yAxis")
    @Expose
    float yAxis;

    @SerializedName("rightMargin")
    @Expose
    float rightMargin;

    @SerializedName("leftMargin")
    @Expose
    float leftMargin;

    @SerializedName("topMargin")
    @Expose
    float topMargin;

    @SerializedName("bottomMargin")
    @Expose
    float bottomMargin;

    @SerializedName("xAxis")
    @Expose
    float xAxis;

    @SerializedName("gridWidth")
    @Expose
    float gridWidth;

    @SerializedName("gridHeight")
    @Expose
    float gridHeight;

    @SerializedName("fontSize")
    @Expose
    int fontSize;

    @SerializedName("marginBottom")
    @Expose
    float marginBottom;

    @SerializedName("marginTop")
    @Expose
    float marginTop;

    @SerializedName("marginLeft")
    @Expose
    float marginLeft;

    @SerializedName("marginRight")
    @Expose
    float marginRight;

    @SerializedName("trayPadding")
    @Expose
    float trayPadding;

    @SerializedName("fontSizeKey")
    @Expose
    float fontSizeKey;

    @SerializedName("fontSizeValue")
    @Expose
    float fontSizeValue;

    @SerializedName("maximumWidth")
    @Expose
    float maximumWidth;

    @SerializedName("isHorizontalScroll")
    @Expose
    boolean isHorizontalScroll;

    @SerializedName("thumbnailWidth")
    @Expose
    int thumbnailWidth;

    @SerializedName("thumbnailHeight")
    @Expose
    int thumbnailHeight;

    @SerializedName("leftDrawableHeight")
    @Expose
    float leftDrawableHeight;

    @SerializedName("leftDrawableWidth")
    @Expose
    float leftDrawableWidth;

    @SerializedName("leftDrawable")
    @Expose
    String leftDrawable;

    @SerializedName("bellow")
    @Expose
    String bellow;

    @SerializedName("trayPaddingVertical")
    @Expose
    float trayPaddingVertical;

    @SerializedName("gridCorner")
    @Expose
    int gridCorner;

    @SerializedName("ratio")
    @Expose
    String ratio;

    @SerializedName("leftTo_leftOf")
    @Expose
    String leftTo_leftOf;

    @SerializedName("leftTo_rightOf")
    @Expose
    String leftTo_rightOf;

    @SerializedName("rightTo_leftOf")
    @Expose
    String rightTo_leftOf;

    @SerializedName("rightTo_rightOf")
    @Expose
    String rightTo_rightOf;

    @SerializedName("topTo_topOf")
    @Expose
    String topTo_topOf;

    @SerializedName("topTo_bottomOf")
    @Expose
    String topTo_bottomOf;

    @SerializedName("bottomTo_topOf")
    @Expose
    String bottomTo_topOf;

    @SerializedName("bottomTo_bottomOf")
    @Expose
    String bottomTo_bottomOf;

    @SerializedName("baseline_toBaselineOf")
    @Expose
    String baseline_toBaselineOf;

    @SerializedName("start_toEndOf")
    @Expose
    String start_toEndOf;

    @SerializedName("start_toStartOf")
    @Expose
    String start_toStartOf;

    @SerializedName("end_toStartOf")
    @Expose
    String end_toStartOf;

    @SerializedName("end_toEndOf")
    @Expose
    String end_toEndOf;

    @SerializedName("constraint_MarginRight")
    @Expose
    int constraint_MarginRight;

    @SerializedName("constraint_MarginLeft")
    @Expose
    int constraint_MarginLeft;

    @SerializedName("constraint_MarginTop")
    @Expose
    int constraint_MarginTop;

    @SerializedName("constraint_MarginBottom")
    @Expose
    int constraint_MarginBottom;

    /**
     *
     * ConstraintSet.HORIZONTAL_GUIDELINE = 0
     * ConstraintSet.VERTICAL_GUIDELINE = 1
     */

    @SerializedName("guidLineOriantation")
    @Expose
    int guidLineOriantation =-1;

    @SerializedName("guideLinePositionPercent")
    @Expose
    float guideLinePositionPercent;


    private float savedWidth;

    public boolean isHorizontalScroll() {
        return isHorizontalScroll;
    }

    public float getWidth() {
        return width;
    }

    public void setWidth(float width) {
        this.width = width;
    }

    public float getHeight() {
        return height;
    }

    public void setHeight(float height) {
        this.height = height;
    }

    public float getYAxis() {
        return yAxis;
    }

    public void setYAxis(float yAxis) {
        this.yAxis = yAxis;
    }

    public float getRightMargin() {
        if (marginRight != 0f) {
            return marginRight;
        }
        return rightMargin;
    }

    public void setRightMargin(float rightMargin) {
        this.rightMargin = rightMargin;
    }

    public float getLeftMargin() {
        if (marginLeft != 0f) {
            return marginLeft;
        }
        return leftMargin;
    }

    public void setLeftMargin(float leftMargin) {
        this.leftMargin = leftMargin;
    }

    public float getTopMargin() {
        if (marginTop != 0f) {
            return marginTop;
        }
        return topMargin;
    }

    public void setTopMargin(float topMargin) {
        this.topMargin = topMargin;
    }

    public float getBottomMargin() {
        if (marginBottom != 0f) {
            return marginBottom;
        }
        return bottomMargin;
    }

    public void setBottomMargin(float bottomMargin) {
        this.bottomMargin = bottomMargin;
    }

    public float getXAxis() {
        return xAxis;
    }

    public void setXAxis(float xAxis) {
        this.xAxis = xAxis;
    }

    public float getyAxis() {
        return yAxis;
    }

    public void setyAxis(float yAxis) {
        this.yAxis = yAxis;
    }

    public float getxAxis() {
        return xAxis;
    }

    public void setxAxis(float xAxis) {
        this.xAxis = xAxis;
    }

    public float getGridWidth() {
        return gridWidth;
    }

    public void setGridWidth(float gridWidth) {
        this.gridWidth = gridWidth;
    }

    public float getGridHeight() {
        return gridHeight;
    }

    public void setGridHeight(float gridHeight) {
        this.gridHeight = gridHeight;
    }

    public int getFontSize() {
        return fontSize;
    }

    public void setFontSize(int fontSize) {
        this.fontSize = fontSize;
    }

    public float getMarginBottom() {
        return marginBottom;
    }

    public void setMarginBottom(float marginBottom) {
        this.marginBottom = marginBottom;
    }

    public float getMarginTop() {
        return marginTop;
    }

    public void setMarginTop(float marginTop) {
        this.marginTop = marginTop;
    }

    public float getMarginLeft() {
        return marginLeft;
    }

    public void setMarginLeft(float marginLeft) {
        this.marginLeft = marginLeft;
    }

    public float getMarginRight() {
        return marginRight;
    }

    public void setMarginRight(float marginRight) {
        this.marginRight = marginRight;
    }

    public float getTrayPadding() {
        return trayPadding;
    }

    public float getTrayPaddingVertical() {
        return trayPaddingVertical;
    }

    public void setTrayPadding(float trayPadding) {
        this.trayPadding = trayPadding;
    }

    public float getFontSizeKey() {
        return fontSizeKey;
    }

    public void setFontSizeKey(float fontSizeKey) {
        this.fontSizeKey = fontSizeKey;
    }

    public float getFontSizeValue() {
        return fontSizeValue;
    }

    public void setFontSizeValue(float fontSizeValue) {
        this.fontSizeValue = fontSizeValue;
    }

    public float getMaximumWidth() {
        return maximumWidth;
    }

    public void setMaximumWidth(float maximumWidth) {
        this.maximumWidth = maximumWidth;
    }

    public float getSavedWidth() {
        return savedWidth;
    }

    public void setSavedWidth(float savedWidth) {
        this.savedWidth = savedWidth;
    }

    public int getThumbnailWidth() {
        return thumbnailWidth;
    }

    public void setThumbnailWidth(int thumbnailWidth) {
        this.thumbnailWidth = thumbnailWidth;
    }

    public int getThumbnailHeight() {
        return thumbnailHeight;
    }

    public void setThumbnailHeight(int thumbnailHeight) {
        this.thumbnailHeight = thumbnailHeight;
    }

    public float getLeftDrawableHeight() {
        return leftDrawableHeight;
    }

    public void setLeftDrawableHeight(float leftDrawableHeight) {
        this.leftDrawableHeight = leftDrawableHeight;
    }

    public float getLeftDrawableWidth() {
        return leftDrawableWidth;
    }

    public void setLeftDrawableWidth(float leftDrawableWidth) {
        this.leftDrawableWidth = leftDrawableWidth;
    }

    public String getLeftDrawable() {
        return leftDrawable;
    }

    public void setLeftDrawable(String leftDrawable) {
        this.leftDrawable = leftDrawable;
    }

    public int getGridCorner() {
        return gridCorner;
    }

    public void setGridCorner(int gridCorner) {
        this.gridCorner = gridCorner;
    }

    public String getBellow() {
        return bellow;
    }

    public void setBellow(String bellow) {
        this.bellow = bellow;
    }

    public String getRatio() {
        return ratio;
    }

    public void setRatio(String ratio) {
        this.ratio = ratio;
    }

    public String getTopTo_bottomOf() {
        return topTo_bottomOf;
    }

    public void setTopTo_bottomOf(String topTo_bottomOf) {
        this.topTo_bottomOf = topTo_bottomOf;
    }

    public void setHorizontalScroll(boolean horizontalScroll) {
        isHorizontalScroll = horizontalScroll;
    }

    public void setTrayPaddingVertical(float trayPaddingVertical) {
        this.trayPaddingVertical = trayPaddingVertical;
    }

    public String getTopTo_topOf() {
        return topTo_topOf;
    }

    public void setTopTo_topOf(String topTo_topOf) {
        this.topTo_topOf = topTo_topOf;
    }

    public String getBottomTo_bottomOf() {
        return bottomTo_bottomOf;
    }

    public void setBottomTo_bottomOf(String bottomTo_bottomOf) {
        this.bottomTo_bottomOf = bottomTo_bottomOf;
    }

    public String getBottomTo_topOf() {
        return bottomTo_topOf;
    }

    public void setBottomTo_topOf(String bottomTo_topOf) {
        this.bottomTo_topOf = bottomTo_topOf;
    }

    public String getRightTo_rightOf() {
        return rightTo_rightOf;
    }

    public void setRightTo_rightOf(String rightTo_rightOf) {
        this.rightTo_rightOf = rightTo_rightOf;
    }

    public String getRightTo_leftOf() {
        return rightTo_leftOf;
    }

    public void setRightTo_leftOf(String rightTo_leftOf) {
        this.rightTo_leftOf = rightTo_leftOf;
    }

    public String getLeftTo_leftOf() {
        return leftTo_leftOf;
    }

    public void setLeftTo_leftOf(String leftTo_leftOf) {
        this.leftTo_leftOf = leftTo_leftOf;
    }

    public String getLeftTo_rightOf() {
        return leftTo_rightOf;
    }

    public void setLeftTo_rightOf(String leftTo_rightOf) {
        this.leftTo_rightOf = leftTo_rightOf;
    }

    public String getBaseline_toBaselineOf() {
        return baseline_toBaselineOf;
    }

    public void setBaseline_toBaselineOf(String baseline_toBaselineOf) {
        this.baseline_toBaselineOf = baseline_toBaselineOf;
    }

    public String getStart_toEndOf() {
        return start_toEndOf;
    }

    public void setStart_toEndOf(String start_toEndOf) {
        this.start_toEndOf = start_toEndOf;
    }

    public String getStart_toStartOf() {
        return start_toStartOf;
    }

    public void setStart_toStartOf(String start_toStartOf) {
        this.start_toStartOf = start_toStartOf;
    }

    public String getEnd_toStartOf() {
        return end_toStartOf;
    }

    public void setEnd_toStartOf(String end_toStartOf) {
        this.end_toStartOf = end_toStartOf;
    }

    public String getEnd_toEndOf() {
        return end_toEndOf;
    }

    public void setEnd_toEndOf(String end_toEndOf) {
        this.end_toEndOf = end_toEndOf;
    }

    public int getConstraint_MarginRight() {
        return constraint_MarginRight;
    }

    public void setConstraint_MarginRight(int constraint_MarginRight) {
        this.constraint_MarginRight = constraint_MarginRight;
    }

    public int getConstraint_MarginLeft() {
        return constraint_MarginLeft;
    }

    public void setConstraint_MarginLeft(int constraint_MarginLeft) {
        this.constraint_MarginLeft = constraint_MarginLeft;
    }

    public int getConstraint_MarginTop() {
        return constraint_MarginTop;
    }

    public void setConstraint_MarginTop(int constraint_MarginTop) {
        this.constraint_MarginTop = constraint_MarginTop;
    }

    public int getConstraint_MarginBottom() {
        return constraint_MarginBottom;
    }

    public void setConstraint_MarginBottom(int constraint_MarginBottom) {
        this.constraint_MarginBottom = constraint_MarginBottom;
    }

    public int getGuidLineOriantation() {
        return guidLineOriantation ==0?ConstraintSet.HORIZONTAL_GUIDELINE:(guidLineOriantation ==1?ConstraintSet.VERTICAL_GUIDELINE:-1);
    }

    public float getGuideLinePositionPercent() {
        return guideLinePositionPercent;
    }

    public void setGuideLinePositionPercent(float guideLinePositionPercent) {
        this.guideLinePositionPercent = guideLinePositionPercent;
    }

    @SerializedName("trayMarginVertical")
    @Expose
    int trayMarginVertical;

    @SerializedName("trayMarginHorizontal")
    @Expose
    int trayMarginHorizontal;

    public int getTrayMarginVertical() {
        return trayMarginVertical;
    }

    public int getTrayMarginHorizontal() {
        return trayMarginHorizontal;
    }
}
