package com.p5m.me.view.activity.Main;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import android.view.View;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.p5m.me.R;
import com.p5m.me.adapters.AdapterCallbacks;
import com.p5m.me.adapters.NationalityAdapter;
import com.p5m.me.data.Nationality;
import com.p5m.me.helper.Helper;
import com.p5m.me.utils.AppConstants;
import com.p5m.me.view.activity.base.BaseActivity;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ChooseNationalityActivity extends BaseActivity implements AdapterCallbacks {

    public static void openActivity(Activity activity) {
        activity.startActivityForResult(new Intent(activity, ChooseNationalityActivity.class), AppConstants.ResultCode.CHOOSE_NATIONALITY);
    }

    @BindView(R.id.recyclerView)
    public RecyclerView recyclerView;

    private List<Nationality> nationalities;
    private NationalityAdapter nationalityAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_nationality);

        ButterKnife.bind(activity);

        try {
            nationalities = new Gson().fromJson(Helper.getNationalityFileFromAsset(context), new TypeToken<List<Nationality>>(){}.getType());
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (nationalities == null) {
            finish();
            return;
        }

        recyclerView.setLayoutManager(new LinearLayoutManager(activity));
        recyclerView.setHasFixedSize(false);

        nationalityAdapter = new NationalityAdapter(context, this);
        recyclerView.setAdapter(nationalityAdapter);

        nationalityAdapter.addAll(nationalities);
        nationalityAdapter.notifyDataSetChanged();
        onTrackingNotification();

    }

    @OnClick(R.id.imageViewBack)
    public void imageViewBack(View view) {
        finish();
    }

    @Override
    public void onAdapterItemClick(RecyclerView.ViewHolder viewHolder, View view, Object model, int position) {
        if (model instanceof Nationality) {
            Nationality nationality = (Nationality) model;
            Intent intent = new Intent();
            intent.putExtra(AppConstants.DataKey.NATIONALITY_OBJECT, nationality);

            setResult(RESULT_OK, intent);
            finish();
        }
    }

    @Override
    public void onAdapterItemLongClick(RecyclerView.ViewHolder viewHolder, View view, Object model, int position) {

    }

    @Override
    public void onShowLastItem() {

    }
}
