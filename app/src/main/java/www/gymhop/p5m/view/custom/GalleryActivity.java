package www.gymhop.p5m.view.custom;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;

import butterknife.BindView;
import butterknife.ButterKnife;
import www.gymhop.p5m.R;
import www.gymhop.p5m.view.activity.base.BaseActivity;

public class GalleryActivity extends BaseActivity {

    @BindView(R.id.imageView)
    ImageView imageView;

    public static void openActivity(Context context) {
        context.startActivity(new Intent(context, GalleryActivity.class));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);

        ButterKnife.bind(activity);

    }
}
