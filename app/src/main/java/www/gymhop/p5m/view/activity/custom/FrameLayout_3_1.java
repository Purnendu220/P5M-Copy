package www.gymhop.p5m.view.activity.custom;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.FrameLayout;

/**
 * Created by MyU10 on 3/9/2018.
 */

public class FrameLayout_3_1 extends FrameLayout {

    public FrameLayout_3_1(Context context) {
        super(context);
    }

    public FrameLayout_3_1(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public FrameLayout_3_1(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int width = getMeasuredWidth();
        setMeasuredDimension(width, (int) (width * 0.35)); //aspect ratio
    }

}