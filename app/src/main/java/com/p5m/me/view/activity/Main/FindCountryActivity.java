package com.p5m.me.view.activity.Main;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.support.design.widget.AppBarLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.p5m.me.R;
import com.p5m.me.data.main.Country;
import com.p5m.me.restapi.NetworkCommunicator;
import com.p5m.me.restapi.ResponseModel;
import com.p5m.me.storage.preferences.MyPreferences;
import com.p5m.me.utils.ToastUtils;
import com.p5m.me.view.activity.base.BaseActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FindCountryActivity extends BaseActivity implements NetworkCommunicator.RequestListener, View.OnClickListener {


    private List<Country> countryList;

    public static void openActivity(Context context) {
        context.startActivity(new Intent(context, FindCountryActivity.class));
    }


    @BindView(R.id.toolbar)
    public Toolbar toolbar;
    @BindView(R.id.spinnerCountry)
    public Spinner spinnerCountry;
    @BindView(R.id.appBarLayout)
    public AppBarLayout appBarLayout;
    @BindView(R.id.buttonOk)
    public Button buttonOk;
    @BindView(R.id.progressBar)
    public ProgressBar progressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_country);
        ButterKnife.bind(activity);

        setToolBar();
        callApiCountry();
        buttonOk.setOnClickListener(this);

    }

    private void setSpinner() {
        List<String> country = new ArrayList<String>();
        int selectedCountryIndex = 0, countryId = 0;
        countryId = MyPreferences.getInstance().getCountryCode();
        for (int i = 0; i < countryList.size(); i++) {
            country.add(countryList.get(i).getName());
            if (countryId == countryList.get(i).getId()) {
                selectedCountryIndex = i;
            }
        }

        ArrayAdapter<String> countryAdapter = new ArrayAdapter<String>(this, R.layout.row_country, country);
        spinnerCountry.setAdapter(countryAdapter);
        spinnerCountry.setSelection(selectedCountryIndex);

    }

    private void setToolBar() {

        BaseActivity activity = (BaseActivity) this.activity;
        activity.setSupportActionBar(toolbar);

        activity.getSupportActionBar().setBackgroundDrawable(new ColorDrawable(ContextCompat.getColor(context, R.color.colorPrimaryDark)));
        activity.getSupportActionBar().setDisplayShowTitleEnabled(false);
        activity.getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        activity.getSupportActionBar().setHomeButtonEnabled(true);
        activity.getSupportActionBar().setDisplayShowHomeEnabled(false);
        activity.getSupportActionBar().setDisplayUseLogoEnabled(true);

        View v = LayoutInflater.from(context).inflate(R.layout.view_tool_normal, null);

        v.findViewById(R.id.imageViewBack).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        ((TextView) v.findViewById(R.id.textViewTitle)).setText(context.getResources().getText(R.string.find_your_country));

        activity.getSupportActionBar().setCustomView(v, new ActionBar.LayoutParams(ActionBar.LayoutParams.MATCH_PARENT,
                ActionBar.LayoutParams.MATCH_PARENT));
        activity.getSupportActionBar().setDisplayShowCustomEnabled(true);
    }

    private void callApiCountry() {
        progressBar.setVisibility(View.VISIBLE);
        networkCommunicator.getCountry("", this, false);
    }


    @Override
    public void onApiSuccess(Object response, int requestCode) {
        progressBar.setVisibility(View.GONE);
        switch (requestCode) {
            case NetworkCommunicator.RequestCode.COUNTRY:
                countryList = ((ResponseModel<List<Country>>) response).data;
                setSpinner();
                break;
        }
    }

    @Override
    public void onApiFailure(String errorMessage, int requestCode) {
        progressBar.setVisibility(View.GONE);
        switch (requestCode) {
            case NetworkCommunicator.RequestCode.COUNTRY:
                ToastUtils.showLong(context, errorMessage);
                break;
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.buttonOk:
                Country selectedCountry = countryList.get(spinnerCountry.getSelectedItemPosition());
                int countryId = selectedCountry.getId();
                MyPreferences.getInstance().setCountryCode(countryId);
                HomeActivity.open(this);
                break;
        }
    }
}
