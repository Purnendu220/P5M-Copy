// Generated code from Butter Knife. Do not modify!
package com.p5m.me.view.activity.Main;

import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import androidx.annotation.CallSuper;
import androidx.annotation.UiThread;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import com.google.android.material.tabs.TabLayout;
import com.p5m.me.R;
import java.lang.IllegalStateException;
import java.lang.Override;

public class SearchActivity_ViewBinding implements Unbinder {
  private SearchActivity target;

  @UiThread
  public SearchActivity_ViewBinding(SearchActivity target) {
    this(target, target.getWindow().getDecorView());
  }

  @UiThread
  public SearchActivity_ViewBinding(SearchActivity target, View source) {
    this.target = target;

    target.imageViewSearchCancel = Utils.findRequiredViewAsType(source, R.id.imageViewSearchCancel, "field 'imageViewSearchCancel'", ImageView.class);
    target.imageViewImageSearch = Utils.findRequiredViewAsType(source, R.id.imageViewImageSearch, "field 'imageViewImageSearch'", ImageView.class);
    target.progressBarDone = Utils.findRequiredViewAsType(source, R.id.progressBarDone, "field 'progressBarDone'", ProgressBar.class);
    target.editTextSearch = Utils.findRequiredViewAsType(source, R.id.editTextSearch, "field 'editTextSearch'", EditText.class);
    target.recyclerViewSearch = Utils.findRequiredViewAsType(source, R.id.recyclerViewSearch, "field 'recyclerViewSearch'", RecyclerView.class);
    target.viewPager = Utils.findRequiredViewAsType(source, R.id.viewPager, "field 'viewPager'", ViewPager.class);
    target.tabLayout = Utils.findRequiredViewAsType(source, R.id.tabs, "field 'tabLayout'", TabLayout.class);
    target.layoutSearchDetails = Utils.findRequiredView(source, R.id.layoutSearchDetails, "field 'layoutSearchDetails'");
    target.layoutSearch = Utils.findRequiredView(source, R.id.layoutSearch, "field 'layoutSearch'");
    target.layoutSearchBar = Utils.findRequiredView(source, R.id.layoutSearchBar, "field 'layoutSearchBar'");
  }

  @Override
  @CallSuper
  public void unbind() {
    SearchActivity target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.imageViewSearchCancel = null;
    target.imageViewImageSearch = null;
    target.progressBarDone = null;
    target.editTextSearch = null;
    target.recyclerViewSearch = null;
    target.viewPager = null;
    target.tabLayout = null;
    target.layoutSearchDetails = null;
    target.layoutSearch = null;
    target.layoutSearchBar = null;
  }
}
