// Generated code from Butter Knife. Do not modify!
package com.p5m.me.view.activity.Main;

import android.view.View;
import androidx.annotation.CallSuper;
import androidx.annotation.UiThread;
import butterknife.Unbinder;
import butterknife.internal.DebouncingOnClickListener;
import butterknife.internal.Utils;
import com.p5m.me.R;
import java.lang.IllegalStateException;
import java.lang.Override;

public class SettingActivity_ViewBinding implements Unbinder {
  private SettingActivity target;

  private View view7f0a017f;

  @UiThread
  public SettingActivity_ViewBinding(SettingActivity target) {
    this(target, target.getWindow().getDecorView());
  }

  @UiThread
  public SettingActivity_ViewBinding(final SettingActivity target, View source) {
    this.target = target;

    View view;
    target.layoutNotification = Utils.findRequiredView(source, R.id.layoutNotification, "field 'layoutNotification'");
    target.layoutMembership = Utils.findRequiredView(source, R.id.layoutMembership, "field 'layoutMembership'");
    target.layoutTransHistory = Utils.findRequiredView(source, R.id.layoutTransHistory, "field 'layoutTransHistory'");
    target.layoutContactUs = Utils.findRequiredView(source, R.id.layoutContactUs, "field 'layoutContactUs'");
    target.layoutChangeCountry = Utils.findRequiredView(source, R.id.layoutChangeCountry, "field 'layoutChangeCountry'");
    target.layoutPrivacyPolicy = Utils.findRequiredView(source, R.id.layoutPrivacyPolicy, "field 'layoutPrivacyPolicy'");
    target.layoutTermsCondition = Utils.findRequiredView(source, R.id.layoutTermsCondition, "field 'layoutTermsCondition'");
    target.layoutAboutUs = Utils.findRequiredView(source, R.id.layoutAboutUs, "field 'layoutAboutUs'");
    target.layoutLogout = Utils.findRequiredView(source, R.id.layoutLogout, "field 'layoutLogout'");
    target.imageViewLogout = Utils.findRequiredView(source, R.id.imageViewLogout, "field 'imageViewLogout'");
    target.progressBarLogout = Utils.findRequiredView(source, R.id.progressBarLogout, "field 'progressBarLogout'");
    view = Utils.findRequiredView(source, R.id.imageViewBack, "method 'imageViewBack'");
    view7f0a017f = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.imageViewBack(p0);
      }
    });
  }

  @Override
  @CallSuper
  public void unbind() {
    SettingActivity target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.layoutNotification = null;
    target.layoutMembership = null;
    target.layoutTransHistory = null;
    target.layoutContactUs = null;
    target.layoutChangeCountry = null;
    target.layoutPrivacyPolicy = null;
    target.layoutTermsCondition = null;
    target.layoutAboutUs = null;
    target.layoutLogout = null;
    target.imageViewLogout = null;
    target.progressBarLogout = null;

    view7f0a017f.setOnClickListener(null);
    view7f0a017f = null;
  }
}
