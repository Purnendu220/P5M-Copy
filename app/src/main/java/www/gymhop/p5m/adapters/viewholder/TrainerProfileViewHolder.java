package www.gymhop.p5m.adapters.viewholder;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import butterknife.ButterKnife;

/**
 * Created by MyU10 on 3/10/2018.
 */

public class TrainerProfileViewHolder extends RecyclerView.ViewHolder {

    private Context context;

    public TrainerProfileViewHolder(View view) {
        super(view);

        context = view.getContext();

        ButterKnife.bind(this, view);
    }

    public void bind() {
    }
}
