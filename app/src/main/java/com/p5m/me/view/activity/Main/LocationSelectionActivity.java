package com.p5m.me.view.activity.Main;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.p5m.me.R;
import com.p5m.me.utils.ToastUtils;
import com.p5m.me.view.activity.LoginRegister.LoginActivity;
import com.p5m.me.view.activity.LoginRegister.SignUpOptions;
import com.p5m.me.view.activity.base.BaseActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class LocationSelectionActivity extends BaseActivity implements AdapterView.OnItemSelectedListener, View.OnClickListener {


    private int position;

    public static void open(Context context) {
        context.startActivity(new Intent(context, LocationSelectionActivity.class));
    }

    @BindView(R.id.spinnerCity)
    public Spinner spinnerCity;

    @BindView(R.id.buttonNext)
    public Button buttonNext;

     @BindView(R.id.textViewLogin)
    public TextView textViewLogin;
    Boolean isSelectCountry = false;
    private String item;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_selection);
        ButterKnife.bind(activity);
        buttonNext.setOnClickListener(this);
        textViewLogin.setOnClickListener(this);
        setSpinnerView();
    }

    private void setSpinnerView() {
        spinnerCity.setOnItemSelectedListener(this);

        List<String> categories = new ArrayList<String>();
        categories.add("Kuwait");
        categories.add("Riyadh,Saudi Arabia");
        categories.add("Want us to expand in your city?");
        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, categories);

        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        spinnerCity.setAdapter(dataAdapter);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        item = parent.getItemAtPosition(position).toString();
        this.position = position;
        isSelectCountry = true;

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        isSelectCountry = false;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.buttonNext:
                if (position == 0) {
                    SignUpOptions.open(context);
                } else if (position == 1) {
                    SignUpOptions.open(context);
                } else {
                    ExpandCityActivity.open(context);
                }
                break;
            case R.id.textViewLogin:
                LoginActivity.open(context);
                break;
        }
    }
}
