package com.p5m.me.adapters.viewholder;

import android.content.Context;
import android.text.Html;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.p5m.me.R;
import com.p5m.me.adapters.AdapterCallbacks;
import com.p5m.me.adapters.ExplorePageRatingListAdapter;
import com.p5m.me.adapters.YoutubePlayListAdapter;
import com.p5m.me.data.Item;
import com.p5m.me.data.YoutubeResponse;
import com.p5m.me.remote_config.RemoteConfigConst;
import com.p5m.me.remote_config.RemoteConfigSetUp;
import com.p5m.me.remote_config.RemoteConfigure;
import com.p5m.me.utils.AppConstants;
import com.p5m.me.utils.LanguageUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class YouTubeViewHolder extends RecyclerView.ViewHolder {
    private  Context context;
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

    List<Item> list;
    List<Item> listAll;
    int MINIMUM_ITEMSTO_SHOW=4;


    public YouTubeViewHolder(@NonNull View itemView) {
        super(itemView);
        context = itemView.getContext();
        ButterKnife.bind(this, itemView);


    }

    public void bind(Object response, final AdapterCallbacks adapterCallbacks, final int position){
        if(response!=null&&response instanceof YoutubeResponse){
            itemView.setVisibility(View.VISIBLE);
            YoutubeResponse model = (YoutubeResponse) response;
            listAll = model.getItems();
            textViewMore.setVisibility(View.GONE);
            textViewHeader.setText(RemoteConfigure.getFirebaseRemoteConfig(context).getRemoteConfigValue(RemoteConfigConst.YOUTUBE_SECTION_TITLE));
            textViewSubHeader.setText(RemoteConfigure.getFirebaseRemoteConfig(context).getRemoteConfigValue(RemoteConfigConst.YOUTUBE_SECTION_SUB_TITLE));
            ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) layout.getLayoutParams();
            params.topMargin = 10;
            recyclerView.setVisibility(View.VISIBLE);

            if(model.getItems().size()>MINIMUM_ITEMSTO_SHOW){
                textViewMore.setVisibility(View.VISIBLE);

            }else{
                textViewMore.setVisibility(View.GONE);

            }
            if (!model.isShowMore()) {
                if(model.getItems().size()>MINIMUM_ITEMSTO_SHOW){
                    list = listAll.subList(0,MINIMUM_ITEMSTO_SHOW);
                    int count = model.getItems().size() - MINIMUM_ITEMSTO_SHOW;
                    String s = String.valueOf(LanguageUtils.numberConverter(count));
                    textViewMore.setText(Html.fromHtml(String.format(context.getResources().getString(R.string.more_videos), "<b>" + "+" + s + "</b>")));
                    setAdapter(list,adapterCallbacks);
                }else{
                    setAdapter(listAll,adapterCallbacks);
                }

            }else{
                setAdapter(listAll,adapterCallbacks);
                if (model.getItems().size() > MINIMUM_ITEMSTO_SHOW) {
                    textViewMore.setText(context.getResources().getString(R.string.less_workouts));
                    textViewMore.setVisibility(View.VISIBLE);
                } else {
                    textViewMore.setVisibility(View.GONE);
                }

            }

            textViewMore.setOnClickListener(v -> {
                adapterCallbacks.onAdapterItemClick(YouTubeViewHolder.this, textViewMore, model, position);
            });



        }
        else{
            itemView.setVisibility(View.GONE);
            itemView.setLayoutParams(new RecyclerView.LayoutParams(0, 0));

        }
    }

    private void setAdapter(List<Item> list,AdapterCallbacks adapterCallbacks){
        YoutubePlayListAdapter adapter = new YoutubePlayListAdapter(context, AppConstants.AppNavigation.SHOWN_IN_EXPLORE_PAGE, list, adapterCallbacks);
        recyclerView.setAdapter(adapter);
        recyclerView.setHasFixedSize(true);
        GridLayoutManager layoutManager = new GridLayoutManager(context, 2, GridLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        adapter.notifyDataSetChanged();
    }
}
