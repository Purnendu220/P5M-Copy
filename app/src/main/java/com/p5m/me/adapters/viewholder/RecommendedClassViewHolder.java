package com.p5m.me.adapters.viewholder;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.p5m.me.R;
import com.p5m.me.adapters.AdapterCallbacks;
import com.p5m.me.data.ClassRatingListData;
import com.p5m.me.data.main.ClassModel;
import com.p5m.me.helper.Helper;
import com.p5m.me.utils.AppConstants;
import com.p5m.me.utils.DateUtils;
import com.p5m.me.utils.ImageUtils;
import com.p5m.me.utils.WordUtils;

import butterknife.BindView;
import butterknife.ButterKnife;


public class RecommendedClassViewHolder extends RecyclerView.ViewHolder {

    private final Context context;
    private int shownInScreen;

    public RecommendedClassViewHolder(View view, int shownInScreen) {
        super(view);

        context = view.getContext();
        ButterKnife.bind(this, view);

        this.shownInScreen = shownInScreen;
    }

    public void bind(Object item, AdapterCallbacks<ClassRatingListData> adapterCallbacks, int position) {


    }
}
