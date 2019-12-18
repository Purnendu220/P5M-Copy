package com.p5m.me.adapters.viewholder;

import android.content.Context;
import android.text.Html;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.google.gson.internal.LinkedTreeMap;
import com.p5m.me.R;
import com.p5m.me.adapters.AdapterCallbacks;
import com.p5m.me.adapters.ExplorePageWorkoutListAdapter;
import com.p5m.me.data.ExploreDataList;
import com.p5m.me.data.ExploreDataModel;
import com.p5m.me.data.WorkoutModel;
import com.p5m.me.utils.AppConstants;
import com.p5m.me.utils.LanguageUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by MyU10 on 3/10/2018.
 */

public class ExplorePageWorkoutViewHolder extends RecyclerView.ViewHolder {


    private final Context context;
    private int shownInScreen;
    @BindView(R.id.layout)
    ConstraintLayout layout;
    @BindView(R.id.textViewHeader)
    TextView textViewHeader;
    @BindView(R.id.textViewSubHeader)
    TextView textViewSubHeader;
    @BindView(R.id.textViewMore)
    TextView textViewMore;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    private Gson gson;
    private List<WorkoutModel> list;
    private List<WorkoutModel> listInitial;

    private ExplorePageWorkoutListAdapter adapter;


    public ExplorePageWorkoutViewHolder(View itemView) {
        super(itemView);

        context = itemView.getContext();
        gson = new Gson();
        ButterKnife.bind(this, itemView);
    }

    public void bind(final Object data, final AdapterCallbacks adapterCallbacks, final int position) {

        if (data != null && data instanceof ExploreDataModel) {
            final ExploreDataModel model = (ExploreDataModel) data;
            if (model.getData() != null) {
                ExploreDataList exploreDataList = new ExploreDataList(model.getData());
                String listString = convertorToModelClassList(exploreDataList);
                list = gson.fromJson(listString, new TypeToken<List<WorkoutModel>>() {
                }.getType());
            }
            itemView.setVisibility(View.VISIBLE);
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
            if (model.isShowDivider()) {
                ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) layout.getLayoutParams();
                params.topMargin = 10;
            }

            if (list != null) {
                recyclerView.setVisibility(View.VISIBLE);
                adapter = new ExplorePageWorkoutListAdapter(context, AppConstants.AppNavigation.SHOWN_IN_EXPLORE_PAGE, adapterCallbacks);
                recyclerView.setAdapter(adapter);
                recyclerView.setHasFixedSize(true);
                GridLayoutManager layoutManager = new GridLayoutManager(context, 2, GridLayoutManager.VERTICAL, false);
                recyclerView.setLayoutManager(layoutManager);
             if(!model.isMoreActivityShow()){
                 if (list.size() > 4) {
                     listInitial = list.subList(0,4);
                     setData(listInitial);
                     int count = list.size() - 4;
                     String s = String.valueOf(LanguageUtils.numberConverter(count));
                     textViewMore.setText(Html.fromHtml(String.format( context.getResources().getString(R.string.more_workouts),"<b>"+"+"+ s+"</b>")));
                     textViewMore.setVisibility(View.VISIBLE);
                 } else {
                     setData(list);
                     textViewMore.setVisibility(View.GONE);
                 }
             }else{
                 setData(list);
                 if (list.size() > 4) {
                     textViewMore.setText(context.getResources().getString(R.string.less_workouts));
                     textViewMore.setVisibility(View.VISIBLE);
                 } else {
                     textViewMore.setVisibility(View.GONE);
                 }
             }

                textViewMore.setOnClickListener(v -> {
                    adapterCallbacks.onAdapterItemClick(ExplorePageWorkoutViewHolder.this,textViewMore,model,position);
                });



            } else {
                recyclerView.setVisibility(View.GONE);
            }

        } else {
            itemView.setVisibility(View.GONE);

        }
    }
    private void setData(List<WorkoutModel> list){
        adapter.clearAll();
        adapter.addAllWorkOut(list);
        adapter.notifyDataSetChanged();
    }

    private String convertorToModelClassList(ExploreDataList exploreDataList) {
        return gson.toJson(
                exploreDataList.getexploreDataList(),
                new TypeToken<List<LinkedTreeMap>>() {
                }.getType());
    }
}
