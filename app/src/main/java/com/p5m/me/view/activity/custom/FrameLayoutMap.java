package com.p5m.me.view.activity.custom;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.FrameLayout;

/**
 * Created by MyU10 on 3/9/2018.
 */

public class FrameLayoutMap extends FrameLayout {

    public FrameLayoutMap(Context context) {
        super(context);
    }

    public FrameLayoutMap(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public FrameLayoutMap(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int width = getMeasuredWidth();
        setMeasuredDimension(width, (width * 1)); //aspect ratio
    }

}