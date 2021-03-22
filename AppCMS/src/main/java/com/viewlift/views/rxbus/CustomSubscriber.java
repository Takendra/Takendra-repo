package com.viewlift.views.rxbus;

public class CustomSubscriber {
    private int position;

    private boolean isHorizontalScroll;

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public boolean isHorizontalScroll() {
        return isHorizontalScroll;
    }

    public void setHorizontalScroll(boolean horizontalScroll) {
        isHorizontalScroll = horizontalScroll;
    }
}
