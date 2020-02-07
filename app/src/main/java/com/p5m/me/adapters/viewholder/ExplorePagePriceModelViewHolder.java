package com.p5m.me.adapters.viewholder;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.google.gson.internal.LinkedTreeMap;
import com.p5m.me.R;
import com.p5m.me.adapters.AdapterCallbacks;
import com.p5m.me.adapters.ExplorePagePriceListAdapter;
import com.p5m.me.data.ExploreDataList;
import com.p5m.me.data.ExploreDataModel;
import com.p5m.me.data.PriceModel;
import com.p5m.me.utils.AppConstants;
import com.p5m.me.utils.LanguageUtils;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by MyU10 on 3/10/2018.
 */

public class ExplorePagePriceModelViewHolder extends RecyclerView.ViewHolder {


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
    private List<PriceModel> list;


    public ExplorePagePriceModelViewHolder(View itemView) {
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
                list = gson.fromJson(listString, new TypeToken<List<PriceModel>>() {
                }.getType());
            }
            if (list != null && list.size() > 0) {
                itemView.setVisibility(View.VISIBLE);
                itemView.setLayoutParams(new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
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

                textViewMore.setVisibility(View.GONE);
                Collections.sort(list, new Comparator<PriceModel>() {
                    @Override
                    public int compare(PriceModel u1, PriceModel u2) {
                        return u1.getOrder().compareTo(u2.getOrder());
                    }
                });
                recyclerView.setVisibility(View.VISIBLE);
                ExplorePagePriceListAdapter adapter = new ExplorePagePriceListAdapter(context, AppConstants.AppNavigation.SHOWN_IN_EXPLORE_PAGE, list, adapterCallbacks);
                recyclerView.setAdapter(adapter);
                recyclerView.setHasFixedSize(true);
                LinearLayoutManager layoutManager = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);
                recyclerView.setLayoutManager(layoutManager);
                adapter.notifyDataSetChanged();
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
