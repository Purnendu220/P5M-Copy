package com.p5m.me.helper;

import android.content.Context;
import android.graphics.Color;
import android.text.TextPaint;
import android.text.style.ClickableSpan;
import android.view.View;

public abstract class MyClickSpan extends ClickableSpan {

    Context context;
    TextPaint textpaint;

    public MyClickSpan(Context context) {
        this.context = context;
    }

    @Override
    public void updateDrawState(TextPaint textPaint) {
        this.textpaint = textPaint;
        textPaint.setUnderlineText(true);
        textPaint.setFakeBoldText(true);
        textPaint.bgColor = Color.TRANSPARENT;
    }

    @Override
    public void onClick(View widget) {
        updateDrawState(textpaint);
        widget.invalidate();
        onSpanClicked();
    }

    public abstract void onSpanClicked();
}
