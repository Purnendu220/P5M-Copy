package www.gymhop.p5m.adapters.viewholder;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import www.gymhop.p5m.R;
import www.gymhop.p5m.adapters.AdapterCallbacks;
import www.gymhop.p5m.data.main.TrainerModel;
import www.gymhop.p5m.helper.Helper;
import www.gymhop.p5m.utils.AppConstants;
import www.gymhop.p5m.utils.ImageUtils;
import www.gymhop.p5m.utils.LogUtils;

/**
 * Created by MyU10 on 3/10/2018.
 */

public class TrainerListViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.textViewTrainerName)
    public TextView textViewTrainerName;
    @BindView(R.id.textViewSubtitle)
    public TextView textViewSubtitle;

    @BindView(R.id.imageViewProfile)
    public ImageView imageViewProfile;
    @BindView(R.id.imageViewSubtitle)
    public ImageView imageViewSubtitle;

    @BindView(R.id.buttonFav)
    public Button buttonFav;

    private final Context context;
    private int shownInScreen;

    public TrainerListViewHolder(View itemView, int shownInScreen) {
        super(itemView);

        context = itemView.getContext();

        ButterKnife.bind(this, itemView);

        this.shownInScreen = shownInScreen;
    }

    public void bind(final Object data, final AdapterCallbacks adapterCallbacks, final int position) {

        if (data != null && data instanceof TrainerModel) {
            itemView.setVisibility(View.VISIBLE);

            final TrainerModel model = (TrainerModel) data;

            if (model.getProfileImageThumbnail() != null) {
                ImageUtils.setImage(context,
                        model.getProfileImageThumbnail(),
                        R.drawable.profile_holder, imageViewProfile);
            }

            Helper.setFavButton(context, buttonFav, model);

            textViewTrainerName.setText(model.getFirstName());

            imageViewSubtitle.setVisibility(View.GONE);

            if (shownInScreen == AppConstants.AppNavigation.SHOWN_IN_HOME_TRAINERS) {

                if (model.getTrainerBranchResponseList() != null && !model.getTrainerBranchResponseList().isEmpty()) {
                    textViewSubtitle.setVisibility(View.VISIBLE);
                    try {
                        String name = "";
                        for (int index = 0; index < model.getTrainerBranchResponseList().size(); index++) {
                            String value = model.getTrainerBranchResponseList().get(index).getGymName();
                            if (!name.contains(value)) {
                                name = index == 0 ? (name += value) : (name += ", " + value);
                            }
                        }
                        textViewSubtitle.setText(name);
                    } catch (Exception e) {
                        e.printStackTrace();
                        LogUtils.exception(e);
                        textViewSubtitle.setVisibility(View.GONE);
                    }
                } else {
                    textViewSubtitle.setVisibility(View.GONE);
                }
            } else if (shownInScreen == AppConstants.AppNavigation.SHOWN_IN_MY_PROFILE_FAV_TRAINERS ||
                    shownInScreen == AppConstants.AppNavigation.SHOWN_IN_GYM_PROFILE_TRAINERS) {

                String categoryList = Helper.getCategoryList(model.getCategoryList());

                if (!categoryList.isEmpty()) {
                    imageViewSubtitle.setVisibility(View.VISIBLE);
                    textViewSubtitle.setVisibility(View.VISIBLE);
                    textViewSubtitle.setText(categoryList);
                } else {
                    textViewSubtitle.setVisibility(View.GONE);
                }
            }

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    adapterCallbacks.onAdapterItemClick(TrainerListViewHolder.this, itemView, data, position);
                }
            });

            buttonFav.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    adapterCallbacks.onAdapterItemClick(TrainerListViewHolder.this, buttonFav, data, position);
                }
            });
        } else {
            itemView.setVisibility(View.GONE);
        }
    }
}
