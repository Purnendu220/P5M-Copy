package www.gymhop.p5m.adapters.viewholder;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import www.gymhop.p5m.R;
import www.gymhop.p5m.adapters.AdapterCallbacks;
import www.gymhop.p5m.data.main.GymDetailModel;
import www.gymhop.p5m.helper.Helper;
import www.gymhop.p5m.utils.ImageUtils;

/**
 * Created by MyU10 on 3/10/2018.
 */

public class GymListViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.textViewName)
    public TextView textViewName;
    @BindView(R.id.textViewSubtitle)
    public TextView textViewSubtitle;

    @BindView(R.id.layoutLocation)
    public View layoutLocation;

    @BindView(R.id.imageViewProfile)
    public ImageView imageViewProfile;

    private final Context context;
    private int shownInScreen;

    public GymListViewHolder(View itemView, int shownInScreen) {
        super(itemView);

        context = itemView.getContext();

        ButterKnife.bind(this, itemView);

        this.shownInScreen = shownInScreen;

    }

    public void bind(final Object data, final AdapterCallbacks adapterCallbacks, final int position) {

        if (data != null && data instanceof GymDetailModel) {
            itemView.setVisibility(View.VISIBLE);

            final GymDetailModel model = (GymDetailModel) data;

            if (model.getProfileImageThumbnail() != null) {
                ImageUtils.setImage(context,
                        model.getProfileImageThumbnail(),
                        R.drawable.profile_holder, imageViewProfile);
            }

            String branchList = Helper.getBranchList(model.getGymBranchResponseList());

            layoutLocation.setVisibility(branchList.isEmpty() ? View.GONE : View.VISIBLE);
            textViewSubtitle.setText(branchList);

            textViewName.setText(model.getStudioName());

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    adapterCallbacks.onAdapterItemClick(GymListViewHolder.this, itemView, data, position);
                }
            });
        } else {
            itemView.setVisibility(View.GONE);
        }
    }
}
