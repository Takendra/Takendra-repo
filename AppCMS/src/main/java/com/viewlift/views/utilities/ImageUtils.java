package com.viewlift.views.utilities;

import android.content.Context;
import android.widget.ImageView;

import com.viewlift.R;

/**
 * Created by viewlift on 11/6/17.
 */

public class ImageUtils {
    private static ImageLoader registeredImageLoader;

    public static void registerImageLoader(ImageLoader imageLoader) {
        registeredImageLoader = imageLoader;
    }

    public static ImageView createImageView(Context context) {
        if (registeredImageLoader != null) {
            return registeredImageLoader.createImageView(context);
        }
        return null;
    }

    public static boolean loadImage(ImageView view,
                                    String url,
                                    ImageLoader.ScaleType scaleType) {
        if (registeredImageLoader != null) {
            registeredImageLoader.loadImage(view, url, scaleType);
            return true;
        }
        return false;
    }


    public static boolean loadImageWithLinearGradient(ImageView view,
                                                   String url,
                                                   int imageWidth,
                                                   int imageHeight) {
        if (registeredImageLoader != null) {
            registeredImageLoader.loadImageWithLinearGradient(view,
                    url,
                    imageWidth,
                    imageHeight);
            return true;
        }
        return false;
    }

    public static int getPlaceHolderByRatio(String ratio,boolean isSeason) {
        if(isSeason)
            return R.drawable.vid_image_placeholder_16x9_season;
        switch (ratio) {
            case "1:1":
                return R.drawable.vid_image_placeholder_1x1;
            case "16:9":
                return R.drawable.vid_image_placeholder_16x9;
            case "9:16":
                return R.drawable.vid_image_placeholder_9x16;
            case "32:9":
                //return R.drawable.vid_image_placeholder_16x9;
                return R.drawable.vid_image_placeholder_32x9;
            case "3:4":
                return R.drawable.vid_image_placeholder_3x4;
        }
        return R.drawable.vid_image_placeholder_land;
    }

    public static int getImageResourceId(Context context, String name) {
        try {
            return context.getResources().getIdentifier(name, "drawable", context.getPackageName());
        } catch (Exception e) {
            return R.drawable.vid_image_placeholder_square;
        }


    }
}
