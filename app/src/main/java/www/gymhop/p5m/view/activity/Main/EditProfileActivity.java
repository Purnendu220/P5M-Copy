package www.gymhop.p5m.view.activity.Main;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import www.gymhop.p5m.R;
import www.gymhop.p5m.view.activity.base.BaseActivity;

public class EditProfileActivity extends BaseActivity {

    public static void openActivity(Context context) {
        context.startActivity(new Intent(context, EditProfileActivity.class));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
    }
}
