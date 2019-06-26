package com.p5m.me.view.activity.custom;

import android.content.Context;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatImageView;

/**
 * Created by MyU10 on 3/9/2018.
 */

public class WideAngleImageView extends AppCompatImageView {

    public WideAngleImageView(Context context) {
        super(context);
    }

    public WideAngleImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public WideAngleImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int width = getMeasuredWidth();
        setMeasuredDimension(width, (int) (width * 0.35)); //aspect ratio
    }

}