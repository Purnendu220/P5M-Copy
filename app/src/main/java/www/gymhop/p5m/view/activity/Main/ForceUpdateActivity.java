package www.gymhop.p5m.view.activity.Main;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import www.gymhop.p5m.R;
import www.gymhop.p5m.utils.LogUtils;
import www.gymhop.p5m.view.activity.base.BaseActivity;

public class ForceUpdateActivity extends BaseActivity {

    private static String title;
    private static String message;

    public static void openActivity(Context context, String title, String message) {
        ForceUpdateActivity.title = title;
        ForceUpdateActivity.message = message;
        context.startActivity(new Intent(context, ForceUpdateActivity.class));
    }

    @BindView(R.id.textViewTitle)
    public TextView textViewTitle;
    @BindView(R.id.textViewMessage)
    public TextView textViewMessage;

    @BindView(R.id.textViewUpdate)
    public TextView textViewUpdate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_force_update);

        ButterKnife.bind(activity);

        if (title != null && !title.isEmpty()) {
            textViewTitle.setText(title);
        }

        if (message != null && !message.isEmpty()) {
            textViewMessage.setText(message);
        }

        textViewUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String appPackageName = getPackageName();
                try {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
                } catch (ActivityNotFoundException e) {
                    LogUtils.exception(e);
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
    }
}
