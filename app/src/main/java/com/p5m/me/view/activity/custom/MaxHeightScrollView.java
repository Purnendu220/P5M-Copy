package com.p5m.me.view.activity.custom;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.widget.ScrollView;

import com.p5m.me.R;

/**
 * Created by Varun John on 5/2/2018.
 */

public class MaxHeightScrollView extends ScrollView {

    private int maxHeight;
    private int defaultValue = 192;

    public MaxHeightScrollView(Context context) {
        super(context);
        init(context);
    }

    public MaxHeightScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.MaxHeightScrollView_Height, 0, 0);
        defaultValue = a.getInt(R.styleable.MaxHeightScrollView_Height_scrollHeight,defaultValue);
        init(context);
    }

    public MaxHeightScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.MaxHeightScrollView_Height, 0, 0);
        defaultValue = a.getInt(R.styleable.MaxHeightScrollView_Height_scrollHeight,defaultValue);
        init(context);
    }

    private void init(Context context) {

        maxHeight = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 1, context.getResources().getDisplayMetrics());
        maxHeight = maxHeight * defaultValue;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        heightMeasureSpec = MeasureSpec.makeMeasureSpec(maxHeight, MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }
}