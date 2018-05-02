package www.gymhop.p5m.view.activity.LoginRegister;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.widget.Button;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import www.gymhop.p5m.R;
import www.gymhop.p5m.adapters.InfoScreenAdapter;
import www.gymhop.p5m.data.InfoScreenData;
import www.gymhop.p5m.utils.ViewPagerIndicator;
import www.gymhop.p5m.view.activity.base.BaseActivity;

public class InfoScreen extends BaseActivity implements ViewPager.OnPageChangeListener {

    @BindView(R.id.viewPager)
    public ViewPager viewPager;
    @BindView(R.id.layoutIndicator)
    public LinearLayout layoutIndicator;

    @BindView(R.id.buttonRegister)
    public Button buttonRegister;

    private InfoScreenAdapter infoScreenAdapter;

    public static void openActivity(Context context) {
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
        SignUpOptions.open(context);
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
}
