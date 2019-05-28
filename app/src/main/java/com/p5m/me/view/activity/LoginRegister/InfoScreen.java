package com.p5m.me.view.activity.LoginRegister;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Geocoder;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.p5m.me.R;
import com.p5m.me.adapters.InfoScreenAdapter;
import com.p5m.me.data.InfoScreenData;
import com.p5m.me.data.main.Country;
import com.p5m.me.geocoder.GetAddressIntentService;
import com.p5m.me.storage.preferences.MyPreferences;
import com.p5m.me.utils.AppConstants;
import com.p5m.me.utils.DialogUtils;
import com.p5m.me.utils.PermissionUtility;
import com.p5m.me.utils.ToastUtils;
import com.p5m.me.utils.ViewPagerIndicator;
import com.p5m.me.view.activity.Main.FindCountryActivity;
import com.p5m.me.view.activity.Main.HomeActivity;
import com.p5m.me.view.activity.NotInYourCityActivity;
import com.p5m.me.view.activity.base.BaseActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class InfoScreen extends BaseActivity implements ViewPager.OnPageChangeListener,PermissionUtility.LocationUpdater {


    private static final String TAG = "Country Updated:";
    private List<Country> countryList;
    private int countryId = -1;
    private FusedLocationProviderClient fusedLocationClient;
    private String countryName;

    private static final int LOCATION_PERMISSION_REQUEST_CODE = 2;

    private InfoScreen.LocationAddressResultReceiver addressResultReceiver;

    private Location currentLocation;

    private LocationCallback locationCallback;

    @BindView(R.id.viewPager)
    public ViewPager viewPager;
    @BindView(R.id.layoutIndicator)
    public LinearLayout layoutIndicator;

    @BindView(R.id.buttonRegister)
    public Button buttonRegister;

    private InfoScreenAdapter infoScreenAdapter;

    public static void open(Context context) {
        context.startActivity(new Intent(context, InfoScreen.class));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        showActionBar = false;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_screen);

        ButterKnife.bind(activity);

        setViewPagerAdapter();

        viewPager.addOnPageChangeListener(this);
    }

    private void setViewPagerAdapter() {
        // Create data..
        List<InfoScreenData> infoScreenDataList = new ArrayList<>(4);
        infoScreenDataList.add(new InfoScreenData(context.getString(R.string.info_screen_desc_1), R.drawable.info_screen_1));
        infoScreenDataList.add(new InfoScreenData(context.getString(R.string.info_screen_desc_2), R.drawable.info_screen_2));
        infoScreenDataList.add(new InfoScreenData(context.getString(R.string.info_screen_desc_3), R.drawable.info_screen_3));
        infoScreenDataList.add(new InfoScreenData(context.getString(R.string.info_screen_desc_4), R.drawable.info_screen_4));

        // Pager Setup..
        infoScreenAdapter = new InfoScreenAdapter(context, activity, infoScreenDataList);
        viewPager.setAdapter(infoScreenAdapter);

        // Indicator setup..
        new ViewPagerIndicator(context, ViewPagerIndicator.STYLE_SMALL).setup(viewPager, layoutIndicator, R.drawable.circle_black, R.drawable.circle_grey);
    }

    @OnClick(R.id.textViewLogin)
    public void login() {
        LoginActivity.open(context);
    }

    @OnClick(R.id.buttonRegister)
    public void register() {
//        SignUpOptions.open(context);
        handleLocationRequest();
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        if (position == viewPager.getAdapter().getCount() - 1) {
            buttonRegister.setBackgroundResource(R.drawable.join_rect);
            buttonRegister.setTextColor(ContextCompat.getColor(context, R.color.white));
        } else {
            buttonRegister.setBackgroundResource(R.drawable.button_white);
            buttonRegister.setTextColor(ContextCompat.getColor(context, R.color.theme_dark_text));
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
    private void handleLocationRequest() {
        if (Build.VERSION.SDK_INT >= 23) {

//            AppCompatActivity mActivity;
//            mActivity= getAppC;
            if (PermissionUtility.verifyLocationPermissions(this, true)) {
                locationAddress();
            }
        } else {
            FindCountryActivity.openActivity(context,AppConstants.AppNavigation.NAVIGATION_FROM_INFO_SCREEN);
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case PermissionUtility.REQUEST_LOCATION:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    handleLocationRequest();
                } else {
                    FindCountryActivity.openActivity(context,AppConstants.AppNavigation.NAVIGATION_FROM_INFO_SCREEN);

                }
                break;

        }
    }

    private void locationAddress() {

        addressResultReceiver = new LocationAddressResultReceiver(new Handler());

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                currentLocation = locationResult.getLocations().get(0);
                getAddress();
            }

            ;
        };
        startLocationUpdates();
    }

    @SuppressWarnings("MissingPermission")
    private void startLocationUpdates() {

        LocationRequest locationRequest = new LocationRequest();
        locationRequest.setInterval(5000);
        locationRequest.setFastestInterval(5000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        fusedLocationClient.requestLocationUpdates(locationRequest,
                locationCallback,
                null);

    }


    @SuppressWarnings("MissingPermission")
    private void getAddress() {

        if (!Geocoder.isPresent()) {
            Toast.makeText(InfoScreen.this,
                    "Can't find current address, ",
                    Toast.LENGTH_SHORT).show();
            return;
        }

        Intent intent = new Intent(this, GetAddressIntentService.class);
        intent.putExtra("add_receiver", addressResultReceiver);
        intent.putExtra("add_location", currentLocation);
        startService(intent);
    }

    @Override
    public void locationUpdater() {
        handleLocationRequest();
    }




    private class LocationAddressResultReceiver extends ResultReceiver {
        LocationAddressResultReceiver(Handler handler) {
            super(handler);
        }

        @Override
        protected void onReceiveResult(int resultCode, Bundle resultData) {

            if (resultCode == 0) {
                //Last Location can be null for various reasons
                //for example the api is called first time
                //so retry till location is set
                //since intent service runs on background thread, it doesn't block main thread
                Log.d("Address", "Location null retrying");
                getAddress();
            }

            if (resultCode == 1) {
                Toast.makeText(InfoScreen.this,
                        "Address not found, ",
                        Toast.LENGTH_SHORT).show();
            }

            String currentAdd = resultData.getString("address_result");

            showResults(currentAdd);
        }
    }

    private void showResults(String currentAdd) {
        if (currentAdd.equalsIgnoreCase("KUWAIT")) {
            MyPreferences.getInstance().saveCountryId(1);
            SignUpOptions.open(context);

        } else if (currentAdd.equalsIgnoreCase("Saudi Arabia")) {
            MyPreferences.getInstance().saveCountryId(2);
            SignUpOptions.open(context);

        } else {
            NotInYourCityActivity.openActivity(this);
        }
//        ToastUtils.show(this, currentAdd);
    }


    @Override
    protected void onPause() {
        super.onPause();
        if (fusedLocationClient != null)
            fusedLocationClient.removeLocationUpdates(locationCallback);
    }

}
