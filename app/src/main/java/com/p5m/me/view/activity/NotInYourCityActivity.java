package com.p5m.me.view.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.p5m.me.R;
import com.p5m.me.utils.AppConstants;
import com.p5m.me.view.activity.Main.FindCountryActivity;
import com.p5m.me.view.activity.base.BaseActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

public class NotInYourCityActivity extends BaseActivity {


    public static void openActivity(Context context) {
        context.startActivity(new Intent(context, NotInYourCityActivity.class));
    }

    @BindView(R.id.toolbar)
    public Toolbar toolbar;
    @BindView(R.id.editTextEmailAddress)
    public EditText editTextEmailAddress;
    @BindView(R.id.buttonKeepMeUpdated)
    public Button buttonKeepMeUpdated;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_not_in_your_city);
        ButterKnife.bind(activity);
        setToolBar();
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

        ((TextView) v.findViewById(R.id.textViewTitle)).setText(context.getResources().getText(R.string.we_are_not_in_your_city));
        ((TextView) v.findViewById(R.id.textViewTitle)).setAllCaps(false);
        activity.getSupportActionBar().setCustomView(v, new ActionBar.LayoutParams(ActionBar.LayoutParams.MATCH_PARENT,
                ActionBar.LayoutParams.MATCH_PARENT));
        activity.getSupportActionBar().setDisplayShowCustomEnabled(true);
    }

}
