package com.stablekernel.standardlib;

import android.content.Context;
import android.content.res.TypedArray;
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

/**
 * LoadingImageView displays an arbitrary image, such as an icon, overlayed with a progressBar.
 * The LoadingImageView class subclasses FrameLayout class, but implements ImageView class to display images.
 * It can load images from various sources (such as resources or content providers). There is also a getter
 * method to access the underlying ImageView so images can be loaded using libraries such as Picasso.
 *
 * @attr ref R.styleable#LoadingImageView_imageSrc
 * @attr ref R.styleable#LoadingImageView_imageScaleType
 */

public class LoadingImageView extends FrameLayout {
    private ImageView imageView;
    private ProgressBar progressBar;

    public LoadingImageView(@NonNull Context context) {
        this(context, null);
    }

    public LoadingImageView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LoadingImageView(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        View view = LayoutInflater.from(context).inflate(R.layout.loading_image_view, this, true);
        imageView = (ImageView) view.findViewById(R.id.imageView);
        progressBar = (ProgressBar) view.findViewById(R.id.progressBar);

        final TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.LoadingImageView);

        Drawable image = a.getDrawable(R.styleable.LoadingImageView_imageSrc);
        int scaleType = a.getInteger(R.styleable.LoadingImageView_imageScaleType, -1);

        imageView.setImageDrawable(image);

        if (scaleType >= 0) {
            ImageView.ScaleType selectedScaleType = ImageView.ScaleType.values()[scaleType];
            imageView.setScaleType(selectedScaleType);
        }

        a.recycle();
    }

    public void setImageResource(int resourceId) {
        imageView.setImageResource(resourceId);
    }

    public void setImageDrawable(Drawable drawable) {
        imageView.setImageDrawable(drawable);
    }

    public void showProgessBar(boolean isVisible) {
        progressBar.setVisibility(isVisible ? VISIBLE : GONE);
    }
    
    public ImageView getImageView() {
        return imageView;
    }
}
