package www.gymhop.p5m.adapters.viewholder;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import www.gymhop.p5m.R;
import www.gymhop.p5m.view.activity.custom.MyRecyclerView;

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

    public void bind(MyRecyclerView.LoaderItem loaderItem, MyRecyclerView.Loader loader, View view, View viewRoot, int position) {

        if (loader.enableLoader) {
            progressBar.setVisibility(View.VISIBLE);
        } else {
            progressBar.setVisibility(View.VISIBLE);
        }

        if (loader.enableText) {
            if (loader.enableLoader)
                text.setText(loader.enableStatusText);
            else
                text.setText(loader.disableStatusText);

            text.setVisibility(View.VISIBLE);
        } else {
            text.setVisibility(View.GONE);
        }
    }
}
