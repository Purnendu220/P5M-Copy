// Generated code from Butter Knife. Do not modify!
package com.p5m.me.view.custom;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.CallSuper;
import androidx.annotation.UiThread;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import com.p5m.me.R;
import java.lang.IllegalStateException;
import java.lang.Override;

public class CustomRateAlertDialog_ViewBinding implements Unbinder {
  private CustomRateAlertDialog target;

  @UiThread
  public CustomRateAlertDialog_ViewBinding(CustomRateAlertDialog target) {
    this(target, target.getWindow().getDecorView());
  }

  @UiThread
  public CustomRateAlertDialog_ViewBinding(CustomRateAlertDialog target, View source) {
    this.target = target;

    target.linearlayoutTopPart = Utils.findRequiredViewAsType(source, R.id.linearlayoutTopPart, "field 'linearlayoutTopPart'", LinearLayout.class);
    target.textViewHowWasClass = Utils.findRequiredViewAsType(source, R.id.textViewHowWasClass, "field 'textViewHowWasClass'", TextView.class);
    target.textViewRatingCount = Utils.findRequiredViewAsType(source, R.id.textViewRatingCount, "field 'textViewRatingCount'", TextView.class);
    target.textViewNotNow = Utils.findRequiredViewAsType(source, R.id.textViewNotNow, "field 'textViewNotNow'", TextView.class);
    target.ratingStar1 = Utils.findRequiredViewAsType(source, R.id.ratingStar1, "field 'ratingStar1'", ImageView.class);
    target.ratingStar2 = Utils.findRequiredViewAsType(source, R.id.ratingStar2, "field 'ratingStar2'", ImageView.class);
    target.ratingStar3 = Utils.findRequiredViewAsType(source, R.id.ratingStar3, "field 'ratingStar3'", ImageView.class);
    target.ratingStar4 = Utils.findRequiredViewAsType(source, R.id.ratingStar4, "field 'ratingStar4'", ImageView.class);
    target.ratingStar5 = Utils.findRequiredViewAsType(source, R.id.ratingStar5, "field 'ratingStar5'", ImageView.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    CustomRateAlertDialog target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.linearlayoutTopPart = null;
    target.textViewHowWasClass = null;
    target.textViewRatingCount = null;
    target.textViewNotNow = null;
    target.ratingStar1 = null;
    target.ratingStar2 = null;
    target.ratingStar3 = null;
    target.ratingStar4 = null;
    target.ratingStar5 = null;
  }
}
