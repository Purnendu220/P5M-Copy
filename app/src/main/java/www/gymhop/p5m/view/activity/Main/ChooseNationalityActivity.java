package www.gymhop.p5m.view.activity.Main;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import www.gymhop.p5m.R;
import www.gymhop.p5m.adapters.AdapterCallbacks;
import www.gymhop.p5m.adapters.NationalityAdapter;
import www.gymhop.p5m.data.Nationality;
import www.gymhop.p5m.helper.Helper;
import www.gymhop.p5m.utils.AppConstants;
import www.gymhop.p5m.view.activity.base.BaseActivity;

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
