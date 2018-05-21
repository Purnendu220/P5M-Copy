package www.gymhop.p5m.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.v4.content.ContextCompat;
import android.util.TypedValue;

import com.facebook.drawee.drawable.ProgressBarDrawable;

import www.gymhop.p5m.R;

/**
 * Created by Varun John on 4/20/2018.
 */
public class ImageLoadProgressBar extends ProgressBarDrawable {


    float level;

    Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);

    int color;

    final RectF oval = new RectF();

    int radius;

    public ImageLoadProgressBar(Context context) {
        int dp = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 1, context.getResources().getDisplayMetrics());

        radius = dp * 20;
        color = ContextCompat.getColor(context, R.color.colorAccent);

        paint.setStrokeWidth(dp * 8);
        paint.setStyle(Paint.Style.STROKE);
    }

    @Override
    protected boolean onLevelChange(int level) {
        this.level = level;
        invalidateSelf();
        return true;
    }

    @Override
    public void draw(Canvas canvas) {
        oval.set(canvas.getWidth() / 2 - radius, canvas.getHeight() / 2 - radius,
                canvas.getWidth() / 2 + radius, canvas.getHeight() / 2 + radius);

        drawCircle(canvas, level, color);
    }


    private void drawCircle(Canvas canvas, float level, int color) {
        paint.setColor(color);
        float angle;
        angle = 360 / 1f;
        angle = level * angle;
        canvas.drawArc(oval, 0, Math.round(angle), false, paint);
    }
}