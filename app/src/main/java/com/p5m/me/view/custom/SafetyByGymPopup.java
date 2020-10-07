package com.p5m.me.view.custom;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.text.Html;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.p5m.me.R;
import com.p5m.me.adapters.AdapterCallbacks;
import com.p5m.me.adapters.CovidSaftyAdapter;

import com.p5m.me.data.main.GymDetailModel;

import com.p5m.me.databinding.ViewSafetyPrecautionBinding;

import butterknife.ButterKnife;


public class SafetyByGymPopup extends Dialog implements View.OnClickListener {
    private Context mContext;
    private ViewSafetyPrecautionBinding dataBinding;
    private GymDetailModel model;
    private AdapterCallbacks mCallbacks;
    public SafetyByGymPopup(@NonNull Context context, GymDetailModel model, AdapterCallbacks callbacks) {
        super(context, R.style.AdvanceDialogTheme);
        this.mContext = context;
        this.model = model;
        this.mCallbacks = callbacks;
        init();
    }
    private void init() {
        dataBinding = ViewSafetyPrecautionBinding.inflate(getLayoutInflater());
        setContentView(dataBinding.getRoot());
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        ButterKnife.bind(this);
        setCancelable(true);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(getWindow().getAttributes());
        lp.width = (int) (mContext.getResources().getDisplayMetrics().widthPixels * 0.90);
        lp.gravity = Gravity.CENTER;
        getWindow().setAttributes(lp);
        setListeners();
        dataBinding.linearLayoutButtons.setVisibility(View.VISIBLE);
        setUpCovidSafty(model,mCallbacks);

    }

    public void setUpCovidSafty(GymDetailModel model, AdapterCallbacks adapterCallbacks){

            dataBinding.textViewTitle.setText(Html.fromHtml(String.format(mContext.getString(R.string.safty_message),  model.getStudioName() )));

            dataBinding.safetyAnsRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
            dataBinding.safetyAnsRecyclerView.setHasFixedSize(false);

            CovidSaftyAdapter covidSaftyAdapter = new CovidSaftyAdapter(mContext, adapterCallbacks,true);
            dataBinding.safetyAnsRecyclerView.setAdapter(covidSaftyAdapter);

            covidSaftyAdapter.addAll(model.getCovidSafetyList());
            covidSaftyAdapter.notifyDataSetChanged();




    }
    private void setListeners() {
        dataBinding.textViewCancel.setOnClickListener(this);
        dataBinding.textViewOk.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.textViewOk:
            case R.id.textViewCancel: {
                dismiss();
            }
            break;


        }

    }



}