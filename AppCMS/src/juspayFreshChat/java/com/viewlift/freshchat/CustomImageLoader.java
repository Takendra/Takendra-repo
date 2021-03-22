package com.viewlift.freshchat;

import android.graphics.Bitmap;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.freshchat.consumer.sdk.FreshchatImageLoader;
import com.freshchat.consumer.sdk.FreshchatImageLoaderRequest;

public class CustomImageLoader implements FreshchatImageLoader {
  
  @Override
  public void load(@NonNull FreshchatImageLoaderRequest request, @NonNull ImageView imageView) {
    // your code to download image and set to imageView
    Glide.with(imageView)
            .load(request.getUri())
            .dontAnimate()
            .apply(new RequestOptions().override(request.getTargetWidth(), request.getTargetHeight()))
            .into(imageView);
  }

  @Nullable
  @Override
  public Bitmap get(@NonNull FreshchatImageLoaderRequest request) {
    // code to download and return bitmap image
    return null;
  }

  @Override
  public void fetch(@NonNull FreshchatImageLoaderRequest request) {
    // code to download image
  }
}