package com.p5m.me.adapters.viewholder;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.p5m.me.R;
import com.p5m.me.adapters.AdapterCallbacks;
import com.p5m.me.data.ClassRatingListData;
import com.p5m.me.data.ExploreDataModel;
import com.p5m.me.utils.DateUtils;
import com.p5m.me.utils.LanguageUtils;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by MyU10 on 3/10/2018.
 */

public class StillConfusedViewHolder extends RecyclerView.ViewHolder {


    private final Context context;
    private int shownInScreen;
    @BindView(R.id.buttonContactUs)
    Button buttonContactUs;
    @BindView(R.id.layout)
    ConstraintLayout layout;
    @BindView(R.id.textViewHeader)
    TextView textViewHeader;
    @BindView(R.id.textViewSubHeader)
    TextView textViewSubHeader;


    public StillConfusedViewHolder(View itemView) {
        super(itemView);

        context = itemView.getContext();

        ButterKnife.bind(this, itemView);
        this.shownInScreen = shownInScreen;
    }

    public void bind(final Object data, int shownInScreen, final AdapterCallbacks adapterCallbacks, final int position) {

        if (data != null && data instanceof ExploreDataModel) {
            final ExploreDataModel model = (ExploreDataModel) data;
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
            buttonContactUs.setOnClickListener(v -> {
                adapterCallbacks.onAdapterItemClick(StillConfusedViewHolder.this, buttonContactUs, model, position);
            });
            if (model.isShowDivider()) {
                ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) layout.getLayoutParams();
                params.topMargin = 10;
            }
        }
    }
}
