// Generated code from Butter Knife. Do not modify!
package com.p5m.me.view.activity.Main;

import android.view.View;
import android.webkit.WebView;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.annotation.CallSuper;
import androidx.annotation.UiThread;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import com.p5m.me.R;
import java.lang.IllegalStateException;
import java.lang.Override;

public class PaymentWebViewActivity_ViewBinding implements Unbinder {
  private PaymentWebViewActivity target;

  @UiThread
  public PaymentWebViewActivity_ViewBinding(PaymentWebViewActivity target) {
    this(target, target.getWindow().getDecorView());
  }

  @UiThread
  public PaymentWebViewActivity_ViewBinding(PaymentWebViewActivity target, View source) {
    this.target = target;

    target.webView = Utils.findRequiredViewAsType(source, R.id.webView, "field 'webView'", WebView.class);
    target.progressBar = Utils.findRequiredViewAsType(source, R.id.progressBar, "field 'progressBar'", ProgressBar.class);
    target.layoutProgress = Utils.findRequiredView(source, R.id.layoutProgress, "field 'layoutProgress'");
    target.layoutHide = Utils.findRequiredView(source, R.id.layoutHide, "field 'layoutHide'");
    target.text_timer = Utils.findRequiredViewAsType(source, R.id.text_timer, "field 'text_timer'", TextView.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    PaymentWebViewActivity target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.webView = null;
    target.progressBar = null;
    target.layoutProgress = null;
    target.layoutHide = null;
    target.text_timer = null;
  }
}
