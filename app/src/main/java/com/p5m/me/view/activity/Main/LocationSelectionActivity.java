package com.p5m.me.view.activity.Main;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.content.ContextCompat;

import com.google.android.material.textfield.TextInputLayout;
import com.p5m.me.R;
import com.p5m.me.data.ExploreDataModel;
import com.p5m.me.data.main.StoreApiModel;
import com.p5m.me.helper.Helper;
import com.p5m.me.restapi.NetworkCommunicator;
import com.p5m.me.restapi.ResponseModel;
import com.p5m.me.storage.TempStorage;
import com.p5m.me.utils.ToastUtils;
import com.p5m.me.view.activity.LoginRegister.LoginActivity;
import com.p5m.me.view.activity.LoginRegister.SignUpOptions;
import com.p5m.me.view.activity.base.BaseActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class LocationSelectionActivity extends BaseActivity implements AdapterView.OnItemSelectedListener, View.OnClickListener, NetworkCommunicator.RequestListener {


    private int position;
    private ArrayList<String> categories;
    private List<StoreApiModel> model;
    private boolean other = false;

    public static void open(Context context) {
        context.startActivity(new Intent(context, LocationSelectionActivity.class));
    }

    @BindView(R.id.spinnerCity)
    public Spinner spinnerCity;

    @BindView(R.id.buttonNext)
    public Button buttonNext;

    @BindView(R.id.textViewCountryName)
    public EditText textViewCountryName;

    @BindView(R.id.textInputLayoutCity)
    public TextInputLayout textInputLayoutCity;

    @BindView(R.id.textViewLogin)
    public TextView textViewLogin;
    Boolean isSelectCountry = false;
    private String item;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_selection);
        ButterKnife.bind(activity);
        categories = new ArrayList<String>();
        buttonNext.setOnClickListener(this);
        textViewLogin.setOnClickListener(this);
        textInputLayoutCity.setVisibility(View.GONE);
        categories.add(getString(R.string.select_city));

        Helper.setupErrorWatcher(textViewCountryName, textInputLayoutCity);
//        categories.add(getResources().getString(R.string.select_city));
      //        spinnerCity.setBackground(getResources().getDrawable(R.drawable.ic_arrow_up));
        callApi();

    }

    private void callApi() {
        networkCommunicator.getStoreData(this, false);
    }

    private void setSpinnerView() {
        spinnerCity.setOnItemSelectedListener(this);
        categories.add(getString(R.string.other_country));

//        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, categories);
        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(
                this, android.R.layout.simple_spinner_dropdown_item, categories) {
            @Override
            public boolean isEnabled(int position) {
                if (position == 0) {
                    // Disable the first item from Spinner
                    // First item will be use for hint
                    return false;
                } else {
                    return true;
                }
            }

            @Override
            public View getDropDownView(int position, View convertView,
                                        ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                TextView tv = (TextView) view;
                if (position == 0) {
                    tv.setTextColor(Color.GRAY);
                } else {
                    tv.setTextColor(Color.BLACK);
                }
                return view;
            }
        };
//        spinnerArrayAdapter.setDropDownViewResource(R.layout.spinner_item);
//        spinner.setAdapter(spinnerArrayAdapter);
        spinnerArrayAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item
        );
        spinnerCity.setAdapter(spinnerArrayAdapter);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if(position>0) {
            item = parent.getItemAtPosition(position).toString();
            this.position = position;
            isSelectCountry = true;
//            spinnerCity.setBackground(getResources().getDrawable(R.drawable.ic_arrow_down));

            other = false;
            textInputLayoutCity.setVisibility(View.GONE);
            if (position > model.size()) {
                other = true;
                textInputLayoutCity.setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        textInputLayoutCity.setVisibility(View.GONE);
        isSelectCountry = false;

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.buttonNext:
                if (!other) {
                    textInputLayoutCity.setVisibility(View.GONE);
                    TempStorage.setCountryId(model.get(position-1).getId());
                    TempStorage.setCountryName(model.get(position-1).getName());
                    SignUpOptions.open(context);
                } else {
                    textInputLayoutCity.setVisibility(View.VISIBLE);
                    if (!isError()) {
                        ExpandCityActivity.open(context);
                    }
                }

                break;
            case R.id.textViewLogin:
                LoginActivity.open(context);
                break;
        }
    }

    private boolean isError() {
        String country = textViewCountryName.getText().toString().trim();
        if (country.isEmpty()) {
            textInputLayoutCity.setError(context.getResources().getString(R.string.country_required));
            return true;
        }
        return false;
    }

    @Override
    public void onApiSuccess(Object response, int requestCode) {
        switch (requestCode) {
            case NetworkCommunicator.RequestCode.GET_STORE_DATA:
                model = ((ResponseModel<List<StoreApiModel>>) response).data;
                for (StoreApiModel data : model) {
                    categories.add(data.getName());
                }
                setSpinnerView();
                break;
        }
    }

    @Override
    public void onApiFailure(String errorMessage, int requestCode) {
        switch (requestCode) {
            case NetworkCommunicator.RequestCode.GET_STORE_DATA:
                ToastUtils.show(context, errorMessage);
                break;
        }
    }


}
