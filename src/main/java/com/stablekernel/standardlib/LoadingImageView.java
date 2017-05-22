package com.stablekernel.standardlib;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;

import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

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
        int imageHeight = a.getDimensionPixelSize(R.styleable.LoadingImageView_imageHeight, dipsToPix(WRAP_CONTENT));
        int imageWidth = a.getDimensionPixelSize(R.styleable.LoadingImageView_imageWidth, dipsToPix(WRAP_CONTENT));

        imageView.setImageDrawable(image);
        imageView.getLayoutParams().height = imageHeight;
        imageView.getLayoutParams().width = imageWidth;

        a.recycle();
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

    /**
     * Helper method to convert dips to pixels.
     */
    private int dipsToPix(float dps) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dps,
                getResources().getDisplayMetrics());
    }
}
