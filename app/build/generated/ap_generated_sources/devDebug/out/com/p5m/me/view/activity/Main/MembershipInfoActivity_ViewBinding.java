// Generated code from Butter Knife. Do not modify!
package com.p5m.me.view.activity.Main;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import androidx.annotation.CallSuper;
import androidx.annotation.UiThread;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import com.p5m.me.R;
import java.lang.IllegalStateException;
import java.lang.Override;

public class MembershipInfoActivity_ViewBinding implements Unbinder {
  private MembershipInfoActivity target;

  @UiThread
  public MembershipInfoActivity_ViewBinding(MembershipInfoActivity target) {
    this(target, target.getWindow().getDecorView());
  }

  @UiThread
  public MembershipInfoActivity_ViewBinding(MembershipInfoActivity target, View source) {
    this.target = target;

    target.toolbar = Utils.findRequiredViewAsType(source, R.id.toolbar, "field 'toolbar'", Toolbar.class);
    target.buyClassesLayout = Utils.findRequiredViewAsType(source, R.id.buyClassesLayout, "field 'buyClassesLayout'", LinearLayout.class);
    target.textViewFrequentlyAskedQuestions = Utils.findRequiredViewAsType(source, R.id.textViewFrequentlyAskedQuestions, "field 'textViewFrequentlyAskedQuestions'", TextView.class);
    target.recyclerView = Utils.findRequiredViewAsType(source, R.id.recyclerViewFAQ, "field 'recyclerView'", RecyclerView.class);
    target.scrollView = Utils.findRequiredViewAsType(source, R.id.scroll_view, "field 'scrollView'", ScrollView.class);
    target.textViewWelcome = Utils.findRequiredViewAsType(source, R.id.textViewWelcome, "field 'textViewWelcome'", TextView.class);
    target.textViewWelcomeDetail = Utils.findRequiredViewAsType(source, R.id.textViewWelcomeDetail, "field 'textViewWelcomeDetail'", TextView.class);
    target.textViewMembershipFeatures = Utils.findRequiredViewAsType(source, R.id.textViewMembershipFeatures, "field 'textViewMembershipFeatures'", TextView.class);
    target.textViewVarietyActivity = Utils.findRequiredViewAsType(source, R.id.textViewVarietyActivity, "field 'textViewVarietyActivity'", TextView.class);
    target.textViewVarietyActivityDetail = Utils.findRequiredViewAsType(source, R.id.textViewVarietyActivityDetail, "field 'textViewVarietyActivityDetail'", TextView.class);
    target.textViewVisitGym = Utils.findRequiredViewAsType(source, R.id.textViewVisitGym, "field 'textViewVisitGym'", TextView.class);
    target.textViewVisitGymDetail = Utils.findRequiredViewAsType(source, R.id.textViewVisitGymDetail, "field 'textViewVisitGymDetail'", TextView.class);
    target.textViewSave = Utils.findRequiredViewAsType(source, R.id.textViewSave, "field 'textViewSave'", TextView.class);
    target.textViewSaveDetail = Utils.findRequiredViewAsType(source, R.id.textViewSaveDetail, "field 'textViewSaveDetail'", TextView.class);
    target.textViewInstantBooking = Utils.findRequiredViewAsType(source, R.id.textViewInstantBooking, "field 'textViewInstantBooking'", TextView.class);
    target.textViewInstantBookingDetail = Utils.findRequiredViewAsType(source, R.id.textViewInstantBookingDetail, "field 'textViewInstantBookingDetail'", TextView.class);
    target.textViewHowItWorks = Utils.findRequiredViewAsType(source, R.id.textViewHowItWorks, "field 'textViewHowItWorks'", TextView.class);
    target.textViewExplore = Utils.findRequiredViewAsType(source, R.id.textViewExplore, "field 'textViewExplore'", TextView.class);
    target.textViewExploreDetail = Utils.findRequiredViewAsType(source, R.id.textViewExploreDetail, "field 'textViewExploreDetail'", TextView.class);
    target.textViewPickPlan = Utils.findRequiredViewAsType(source, R.id.textViewPickPlan, "field 'textViewPickPlan'", TextView.class);
    target.textViewPickPlanDetail = Utils.findRequiredViewAsType(source, R.id.textViewPickPlanDetail, "field 'textViewPickPlanDetail'", TextView.class);
    target.textViewTrain = Utils.findRequiredViewAsType(source, R.id.textViewTrain, "field 'textViewTrain'", TextView.class);
    target.textViewTrainDetail = Utils.findRequiredViewAsType(source, R.id.textViewTrainDetail, "field 'textViewTrainDetail'", TextView.class);
    target.textViewDiscount = Utils.findRequiredViewAsType(source, R.id.textViewDiscount, "field 'textViewDiscount'", TextView.class);
    target.textViewDiscountDetail = Utils.findRequiredViewAsType(source, R.id.textViewDiscountDetail, "field 'textViewDiscountDetail'", TextView.class);
    target.buyClasses = Utils.findRequiredViewAsType(source, R.id.buyClasses, "field 'buyClasses'", TextView.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    MembershipInfoActivity target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.toolbar = null;
    target.buyClassesLayout = null;
    target.textViewFrequentlyAskedQuestions = null;
    target.recyclerView = null;
    target.scrollView = null;
    target.textViewWelcome = null;
    target.textViewWelcomeDetail = null;
    target.textViewMembershipFeatures = null;
    target.textViewVarietyActivity = null;
    target.textViewVarietyActivityDetail = null;
    target.textViewVisitGym = null;
    target.textViewVisitGymDetail = null;
    target.textViewSave = null;
    target.textViewSaveDetail = null;
    target.textViewInstantBooking = null;
    target.textViewInstantBookingDetail = null;
    target.textViewHowItWorks = null;
    target.textViewExplore = null;
    target.textViewExploreDetail = null;
    target.textViewPickPlan = null;
    target.textViewPickPlanDetail = null;
    target.textViewTrain = null;
    target.textViewTrainDetail = null;
    target.textViewDiscount = null;
    target.textViewDiscountDetail = null;
    target.buyClasses = null;
  }
}
