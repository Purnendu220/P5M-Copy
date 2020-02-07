package com.p5m.me.adapters.viewholder;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.google.gson.internal.LinkedTreeMap;
import com.p5m.me.R;
import com.p5m.me.adapters.AdapterCallbacks;
import com.p5m.me.adapters.ImageListAdapter;
import com.p5m.me.data.ClassRatingListData;
import com.p5m.me.data.ExploreDataList;
import com.p5m.me.data.ExploreDataModel;
import com.p5m.me.data.TryP5MData;
import com.p5m.me.data.TryP5MModel;
import com.p5m.me.data.WorkoutModel;
import com.p5m.me.utils.AppConstants;
import com.p5m.me.utils.DateUtils;
import com.p5m.me.utils.ImageUtils;
import com.p5m.me.utils.LanguageUtils;
import com.p5m.me.utils.ToastUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by MyU10 on 3/10/2018.
 */

public class TryP5MViewHolder extends RecyclerView.ViewHolder {


    private final Context context;
    private final Gson gson;
    @BindView(R.id.textViewTryP5M)
    TextView textViewHeader;

    @BindView(R.id.textViewDescP5M)
    TextView textViewSubHeader;

    @BindView(R.id.explorePlans)
    Button explorePlans;
    @BindView(R.id.layout)
    ConstraintLayout layout;
    private List<TryP5MData> list;

    public TryP5MViewHolder(View itemView) {
        super(itemView);

        context = itemView.getContext();
        gson = new Gson();
        ButterKnife.bind(this, itemView);
    }

    public void bind(final Object data, int shownInScreen, final AdapterCallbacks adapterCallbacks, final int position) {

        if (data != null && data instanceof ExploreDataModel) {
            final ExploreDataModel model = (ExploreDataModel) data;

            if (model.getData() != null) {
                ExploreDataList exploreDataList = new ExploreDataList(model.getData());
                String listString = convertorToModelClassList(exploreDataList);
                list = gson.fromJson(listString, new TypeToken<List<TryP5MData>>() {
                }.getType());
            }
            if (list != null && list.size() > 0) {
                itemView.setVisibility(View.VISIBLE);
                itemView.setLayoutParams(new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                if (LanguageUtils.getLocalLanguage().equalsIgnoreCase("ar"))
                    explorePlans.setText(list.get(0).getButtonTitleAr());
                else
                    explorePlans.setText(list.get(0).getButtonTitle());
                if (model.getHeader() != null) {
                    if (LanguageUtils.getLocalLanguage().equalsIgnoreCase("ar")) {
                        if (!TextUtils.isEmpty(model.getHeader().getTitleAr()))
                            textViewHeader.setText(model.getHeader().getTitleAr());
                        else
                            textViewHeader.setVisibility(View.GONE);
                    } else {
                        if (!TextUtils.isEmpty(model.getHeader().getTitle()))
                            textViewHeader.setText(model.getHeader().getTitle());
                        else
                            textViewHeader.setVisibility(View.GONE);

                    }
                    if (LanguageUtils.getLocalLanguage().equalsIgnoreCase("ar")) {
                        if (!TextUtils.isEmpty(model.getHeader().getSubTitleAr()))
                            textViewSubHeader.setText(model.getHeader().getSubTitleAr());
                        else
                            textViewSubHeader.setVisibility(View.GONE);
                    } else {
                        if (!TextUtils.isEmpty(model.getHeader().getSubTitle()))
                            textViewSubHeader.setText(model.getHeader().getSubTitle());
                        else
                            textViewSubHeader.setVisibility(View.GONE);
                    }
                }


                explorePlans.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        adapterCallbacks.onAdapterItemClick(TryP5MViewHolder.this, explorePlans, data, position);
                    }
                });
                if (model.isShowDivider()) {
                    ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) layout.getLayoutParams();
                    params.topMargin = 10;
                }
            } else {
                itemView.setVisibility(View.GONE);
                itemView.setLayoutParams(new RecyclerView.LayoutParams(0, 0));
            }
        } else {
            itemView.setVisibility(View.GONE);
            itemView.setLayoutParams(new RecyclerView.LayoutParams(0, 0));
        }
    }

    private String convertorToModelClassList(ExploreDataList exploreDataList) {
        return gson.toJson(
                exploreDataList.getexploreDataList(),
                new TypeToken<List<LinkedTreeMap>>() {
                }.getType());
    }
}
