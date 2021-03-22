package com.viewlift.views.dialog;

import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.StateListDrawable;
import android.graphics.drawable.shapes.RoundRectShape;
import android.view.View;

public class CustomShape {
    public static Drawable createRoundedRectangleDrawable(int solidColor) {
        RoundRectShape rectShape = new RoundRectShape(new float[]{
                10, 10, 10, 10,
                10, 10, 10, 10
        }, null, null);
        ShapeDrawable shapeDrawable = new ShapeDrawable(rectShape);
        shapeDrawable.getPaint().setColor(solidColor);
        shapeDrawable.getPaint().setStyle(Paint.Style.FILL);
        shapeDrawable.getPaint().setAntiAlias(true);
        shapeDrawable.getPaint().setFlags(Paint.ANTI_ALIAS_FLAG);
        return shapeDrawable;
    }

    public static Drawable createLeftSideRoundedRectangleDrawable(int solidColor) {
        RoundRectShape rectShape = new RoundRectShape(new float[]{
                10, 10, 0, 0,
                0, 0, 10, 10
        }, null, null);
        ShapeDrawable shapeDrawable = new ShapeDrawable(rectShape);
        shapeDrawable.getPaint().setColor(solidColor);
        shapeDrawable.getPaint().setStyle(Paint.Style.FILL);
        shapeDrawable.getPaint().setAntiAlias(true);
        shapeDrawable.getPaint().setFlags(Paint.ANTI_ALIAS_FLAG);
        return shapeDrawable;
    }

    public static void makeRoundCorner(int bgcolor, int radius, View v, int strokeWidth, int strokeColor) {
        GradientDrawable gdDefault = new GradientDrawable();
        gdDefault.setColor(bgcolor);
        gdDefault.setCornerRadius(radius);
        gdDefault.setStroke(strokeWidth, strokeColor);
        v.setBackground(gdDefault);
    }

    public static void applyBorderToView(View view, int borderColor, int fillColor) {
        GradientDrawable viewBorder = new GradientDrawable();
        viewBorder.setShape(GradientDrawable.RECTANGLE);
        viewBorder.setStroke(4, borderColor);
        viewBorder.setColor(fillColor);
        view.setBackground(viewBorder);
    }

    public static StateListDrawable roundedRectangleSelector(int color) {
        StateListDrawable res = new StateListDrawable();
//        res.setExitFadeDuration(400);
        res.setAlpha(45);
        res.addState(new int[]{android.R.attr.state_pressed}, new ColorDrawable(color));
//        res.addState(new int[]{android.R.attr.state_pressed}, createRoundedRectangleDrawable(color));
        res.addState(new int[]{}, new ColorDrawable(getDarkShadeColor(color)));
        return res;
    }

    public static int getDarkShadeColor(int color) {
        return Color.rgb((int) (3 * Color.red(color)),
                (int) (3 * Color.green(color)),
                (int) (3 * Color.blue(color)));
    }

}
