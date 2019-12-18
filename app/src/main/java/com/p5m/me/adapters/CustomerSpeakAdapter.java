package com.p5m.me.adapters;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.viewpager.widget.PagerAdapter;

import com.p5m.me.R;
import com.p5m.me.data.BannerData;
import com.p5m.me.data.CustomerSpeaksData;
import com.p5m.me.utils.ImageUtils;
import com.p5m.me.utils.LanguageUtils;

import java.util.List;

/**
 * Created by MyU10 on 3/9/2018.
 */

public class CustomerSpeakAdapter extends PagerAdapter {
    private LayoutInflater layoutInflater;
    private List<CustomerSpeaksData> customerSpeaksData;
    private Context context;

    public CustomerSpeakAdapter(Context context, List<CustomerSpeaksData> customerSpeaksData) {
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.customerSpeaksData = customerSpeaksData;
        this.context = context;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View view = layoutInflater.inflate(R.layout.view_testinomial_explore, container, false);
        container.addView(view);

        ImageView image = view.findViewById(R.id.image);
        TextView textViewName = view.findViewById(R.id.textViewName);
        TextView textViewComment = view.findViewById(R.id.textViewComment);
        CustomerSpeaksData customerSpeak = customerSpeaksData.get(position);
               textViewName.setText(customerSpeak.getName());

        if (LanguageUtils.getLocalLanguage().equalsIgnoreCase("ar")) {
            if (!TextUtils.isEmpty(customerSpeak.getMessage_ar())) {
                textViewComment.setText("\""+customerSpeak.getMessage_ar()+"\"");
                textViewComment.setVisibility(View.VISIBLE);
            }
            else
                textViewComment.setVisibility(View.GONE);
        } else {
            if (!TextUtils.isEmpty(customerSpeak.getMessage_eng())) {
                textViewComment.setText("\""+customerSpeak.getMessage_eng()+"\"");
                textViewComment.setVisibility(View.VISIBLE);
            }
            else
                textViewComment.setVisibility(View.GONE);
        }

        return view;
    }


    @Override
    public int getCount() {
        return customerSpeaksData.size();
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
