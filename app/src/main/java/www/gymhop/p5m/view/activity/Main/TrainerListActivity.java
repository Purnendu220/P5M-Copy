package www.gymhop.p5m.view.activity.Main;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import butterknife.ButterKnife;
import butterknife.OnClick;
import www.gymhop.p5m.R;
import www.gymhop.p5m.utils.AppConstants;
import www.gymhop.p5m.view.activity.base.BaseActivity;
import www.gymhop.p5m.view.fragment.TrainerList;

public class TrainerListActivity extends BaseActivity {

    public static void open(Context context, int gymId) {
        context.startActivity(new Intent(context, TrainerListActivity.class)
                .putExtra(AppConstants.DataKey.GYM_ID_INT, gymId));
    }

    private int gymId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trainer_list);

        ButterKnife.bind(activity);

        gymId = getIntent().getIntExtra(AppConstants.DataKey.GYM_ID_INT, -1);

        if (gymId == -1) {
            finish();
            return;
        }

        getSupportFragmentManager().beginTransaction().add(R.id.container,
                TrainerList.createFragment(gymId, AppConstants.AppNavigation.SHOWN_IN_GYM_PROFILE_TRAINERS)).commit();

    }

    @OnClick(R.id.imageViewBack)
    public void imageViewBack(View view) {
        finish();
    }

}
