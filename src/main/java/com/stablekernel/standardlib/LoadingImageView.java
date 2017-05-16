package com.stablekernel.standardlib;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;

public class LoadingImageView extends FrameLayout {
    private ImageView imageView;
    private ProgressBar progressBar;

    public LoadingImageView(@NonNull Context context) {
        super(context);

        View view = LayoutInflater.from(context).inflate(R.layout.loading_image_view, this, true);
        imageView = (ImageView) view.findViewById(R.id.product_imageView);
        progressBar = (ProgressBar) view.findViewById(R.id.image_progressBar);
    }

    public LoadingImageView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        throw new RuntimeException("Do not use this constructor");
    }

    public LoadingImageView(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        throw new RuntimeException("Do not use this constructor");
    }

    public ImageView getImageView() {
        return imageView;
    }

    public ProgressBar getProgressBar() {
        return progressBar;
    }

    public void setImageResource(int resourceId) {
        imageView.setImageResource(resourceId);
    }

    public void setImageDrawable(Drawable drawable) {
        imageView.setImageDrawable(drawable);
    }

    public void setProgressVisibility(int visibility) {
        progressBar.setVisibility(visibility);
    }
}
