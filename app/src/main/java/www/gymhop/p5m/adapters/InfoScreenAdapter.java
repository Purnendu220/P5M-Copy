package www.gymhop.p5m.adapters;

import android.app.Activity;
import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import www.gymhop.p5m.data.InfoScreenData;
import www.gymhop.p5m.R;

/**
 * Created by MyU10 on 3/9/2018.
 */

public class InfoScreenAdapter extends PagerAdapter {
    private LayoutInflater layoutInflater;
    private List<InfoScreenData> infoScreenDataList;

    public InfoScreenAdapter(Context context, Activity activity, List<InfoScreenData> infoScreenDataList) {
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.infoScreenDataList = infoScreenDataList;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View view = layoutInflater.inflate(R.layout.view_info_screens, container, false);
        container.addView(view);

        ImageView image = view.findViewById(R.id.image);
        TextView text = view.findViewById(R.id.text);
        InfoScreenData infoScreenData = infoScreenDataList.get(position);

        image.setImageResource(infoScreenData.resource);
        text.setText(infoScreenData.text);

        return view;
    }

    @Override
    public int getCount() {
        return infoScreenDataList.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object obj) {
        return view == obj;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        View view = (View) object;
        container.removeView(view);
    }
}
