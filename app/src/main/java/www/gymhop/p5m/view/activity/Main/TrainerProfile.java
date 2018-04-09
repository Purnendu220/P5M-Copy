package www.gymhop.p5m.view.activity.Main;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;

import butterknife.ButterKnife;
import www.gymhop.p5m.R;
import www.gymhop.p5m.view.activity.base.BaseActivity;

public class TrainerProfile extends BaseActivity {

    public static void open(Context context) {
        context.startActivity(new Intent(context, TrainerProfile.class));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        showActionBar = false;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trainer_profile);

        ButterKnife.bind(activity);

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.layoutFragment, new www.gymhop.p5m.view.fragment.TrainerProfile()).commit();
    }
}
