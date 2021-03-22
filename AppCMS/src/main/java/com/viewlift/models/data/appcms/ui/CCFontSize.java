package com.viewlift.models.data.appcms.ui;

public class CCFontSize {
    private int index;
    private String label;
    private float size;

    public CCFontSize(int index, String label, float size) {
        this.index = index;
        this.label = label;
        this.size = size;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public float getSize() {
        return size;
    }

    public void setSize(float size) {
        this.size = size;
    }
}
