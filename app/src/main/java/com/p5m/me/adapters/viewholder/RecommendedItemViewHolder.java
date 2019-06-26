package com.p5m.me.adapters.viewholder;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.p5m.me.R;
import com.p5m.me.adapters.AdapterCallbacks;
import com.p5m.me.adapters.RecommendedClassAdapter;
import com.p5m.me.data.RecomendedClassData;
import com.p5m.me.data.main.ClassModel;
import com.p5m.me.remote_config.RemoteConfigConst;
import com.p5m.me.remote_config.RemoteConfigSetUp;
import com.p5m.me.storage.TempStorage;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RecommendedItemViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.recyclerViewRecommendedClass)
    public RecyclerView recyclerViewRecommendedClass;

    @BindView(R.id.textViewRecomended)
    public TextView textViewRecomended;

    private final Context context;
    private int shownInScreen;
    public RecommendedItemViewHolder(View itemView, int shownInScreen) {
        super(itemView);
        context = itemView.getContext();
        ButterKnife.bind(this, itemView);
        this.shownInScreen = shownInScreen;

    }

    public void bind(final Object data, final AdapterCallbacks adapterCallbacks, final int position) {
        if (data != null && data instanceof RecomendedClassData) {

            if(TempStorage.getDefault()!=null && TempStorage.getDefault().getPopularClassesText()!=null){
//                textViewRecomended.setText(TempStorage.getDefault().getPopularClassesText());
                textViewRecomended.setText(RemoteConfigConst.RECOMMENDED_FOR_YOU_VALUE);
                RemoteConfigSetUp.setTextColor(textViewRecomended,RemoteConfigConst.RECOMMENDED_FOR_YOU_COLOR_VALUE,context.getResources().getColor(R.color.theme_dark_light_text));

            }
            final RecomendedClassData model = (RecomendedClassData) data;
            setRecommendedClassView(model.getRecomendedClassesList(),adapterCallbacks);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                   // adapterCallbacks.onAdapterItemClick(RecommendedItemViewHolder.this, itemView, model, position);
                }
            });
        }else{
            itemView.setVisibility(View.GONE);
        }


    }
    private void setRecommendedClassView(List<ClassModel> models,AdapterCallbacks adapterCallbacks) {
        recyclerViewRecommendedClass.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
        recyclerViewRecommendedClass.setHasFixedSize(true);
        RecommendedClassAdapter recommendedListAdapter = new RecommendedClassAdapter(context, shownInScreen, true,adapterCallbacks);
        recyclerViewRecommendedClass.setAdapter(recommendedListAdapter);
        recommendedListAdapter.addAllClass(models);
    }
}

