package com.p5m.me.adapters.viewholder;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.p5m.me.R;
import com.p5m.me.adapters.AdapterCallbacks;
import com.p5m.me.data.ListLoader;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by MyU10 on 3/10/2018.
 */

public class LoaderViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.progressBar)
    public ProgressBar progressBar;
    @BindView(R.id.text)
    public TextView text;

    private Context context;

    public LoaderViewHolder(View view) {
        super(view);

        context = view.getContext();

        ButterKnife.bind(this, view);
    }

    public void bind(ListLoader model, final AdapterCallbacks adapterCallbacks) {

        if (model.isFinish()) {
            progressBar.setVisibility(View.GONE);
        } else {
            progressBar.setVisibility(View.VISIBLE);
        }

        if (model.isShowText()) {

            text.setVisibility(View.VISIBLE);

            if (model.isFinish())
                text.setText(model.getFinishText());
            else
                text.setText(model.getLoadingText());

        } else {
            text.setVisibility(View.GONE);
            text.setText("");
        }
    }
}
