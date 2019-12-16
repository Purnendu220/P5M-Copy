package com.p5m.me.adapters.viewholder;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.p5m.me.R;
import com.p5m.me.adapters.AdapterCallbacks;
import com.p5m.me.adapters.ImageListAdapter;
import com.p5m.me.data.ClassRatingListData;
import com.p5m.me.data.ExploreDataList;
import com.p5m.me.data.ExploreDataModel;
import com.p5m.me.data.TryP5MModel;
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
    @BindView(R.id.textViewTryP5M)
    TextView textViewTryP5M;

    @BindView(R.id.textViewDescP5M)
    TextView textViewDescP5M;

    @BindView(R.id.explorePlans)
    Button explorePlans;
    @BindView(R.id.layout)
    ConstraintLayout layout;

    public TryP5MViewHolder(View itemView) {
        super(itemView);

        context = itemView.getContext();

        ButterKnife.bind(this, itemView);
    }

    public void bind(final Object data, int shownInScreen, final AdapterCallbacks adapterCallbacks, final int position) {

        if (data != null && data instanceof ExploreDataModel) {
            final ExploreDataModel model = (ExploreDataModel) data;

            textViewTryP5M.setText(model.getHeader().getTitle());
            textViewDescP5M.setText(model.getHeader().getSubTitle());

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

        }
    }
}
