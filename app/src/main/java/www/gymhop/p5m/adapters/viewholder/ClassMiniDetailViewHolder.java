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
import www.gymhop.p5m.data.gym_class.ClassModel;
import www.gymhop.p5m.utils.AppConstants;
import www.gymhop.p5m.utils.DateUtils;
import www.gymhop.p5m.utils.ImageUtils;

/**
 * Created by MyU10 on 3/10/2018.
 */

public class ClassMiniDetailViewHolder extends RecyclerView.ViewHolder {


    @BindView(R.id.imageViewClass)
    public ImageView imageViewClass;
    @BindView(R.id.imageViewTrainerProfile)
    public ImageView imageViewTrainerProfile;
    @BindView(R.id.imageViewOptions1)
    public ImageView imageViewOptions1;
    @BindView(R.id.imageViewOptions2)
    public ImageView imageViewOptions2;

    @BindView(R.id.buttonJoin)
    public Button buttonJoin;

    @BindView(R.id.textViewClassName)
    public TextView textViewClassName;
    @BindView(R.id.textViewClassCategory)
    public TextView textViewClassCategory;
    @BindView(R.id.textViewClassDate)
    public TextView textViewClassDate;
    @BindView(R.id.textViewTime)
    public TextView textViewTime;
    @BindView(R.id.textViewLocation)
    public TextView textViewLocation;
    @BindView(R.id.textViewTrainerName)
    public TextView textViewTrainerName;

    private final Context context;
    private int shownInScreen;

    public ClassMiniDetailViewHolder(View view, int shownInScreen) {
        super(view);

        context = view.getContext();
        ButterKnife.bind(this, view);

        this.shownInScreen = shownInScreen;
    }

    public void bind(Object data, final AdapterCallbacks adapterCallbacks, final int position) {
        if (data != null && data instanceof ClassModel) {
            itemView.setVisibility(View.VISIBLE);

            final ClassModel model = (ClassModel) data;

            if (shownInScreen == AppConstants.AppNavigation.SHOWN_IN_SCHEDULE_UPCOMING) {
                imageViewTrainerProfile.setVisibility(View.VISIBLE);

                buttonJoin.setVisibility(View.GONE);
                imageViewOptions1.setVisibility(View.VISIBLE);
                imageViewOptions2.setVisibility(View.GONE);

            } else if (shownInScreen == AppConstants.AppNavigation.SHOWN_IN_WISH_LIST) {
                buttonJoin.setVisibility(View.VISIBLE);
                imageViewTrainerProfile.setVisibility(View.VISIBLE);

            } else if (shownInScreen == AppConstants.AppNavigation.SHOWN_IN_TRAINER) {
                imageViewTrainerProfile.setVisibility(View.VISIBLE);
                imageViewTrainerProfile.setVisibility(View.GONE);

                buttonJoin.setVisibility(View.VISIBLE);
                imageViewOptions1.setVisibility(View.GONE);
                imageViewOptions2.setVisibility(View.VISIBLE);

            } else if (shownInScreen == AppConstants.AppNavigation.SHOWN_IN_MY_PROFILE_FINISHED) {
                imageViewTrainerProfile.setVisibility(View.GONE);
                imageViewTrainerProfile.setVisibility(View.GONE);

                buttonJoin.setVisibility(View.GONE);
                imageViewOptions1.setVisibility(View.GONE);
                imageViewOptions2.setVisibility(View.GONE);

            } else {
                imageViewTrainerProfile.setVisibility(View.VISIBLE);

                buttonJoin.setVisibility(View.VISIBLE);
                imageViewOptions1.setVisibility(View.GONE);
                imageViewOptions2.setVisibility(View.VISIBLE);
            }

            if (model.getClassMedia() != null) {
                ImageUtils.setImage(context,
                        model.getClassMedia().getMediaThumbNailUrl(),
                        R.drawable.class_holder, imageViewClass);
            }

            if (model.getTrainerDetail() != null) {

                textViewTrainerName.setText(model.getTrainerDetail().getFirstName());
                ImageUtils.setImage(context,
                        model.getTrainerDetail().getProfileImageThumbnail(),
                        R.drawable.profile_holder, imageViewTrainerProfile);

            } else if (model.getGymBranchDetail() != null) {

                textViewTrainerName.setText(model.getGymBranchDetail().getGymName());
                ImageUtils.setImage(context,
                        model.getGymBranchDetail().getMediaThumbNailUrl(),
                        R.drawable.profile_holder, imageViewTrainerProfile);
                textViewLocation.setText(model.getGymBranchDetail().getAddress());

            } else {

                ImageUtils.setImage(context,
                        R.drawable.profile_holder,
                        R.drawable.profile_holder, imageViewTrainerProfile);
                textViewLocation.setText("");
            }

            textViewClassName.setText(model.getTitle());
            textViewClassCategory.setText(model.getClassCategory());
            textViewClassDate.setText(DateUtils.getClassDate(model.getClassDate()));

            if (model.isUserJoinStatus()) {
                buttonJoin.setText(context.getString(R.string.booked));
                buttonJoin.setBackgroundResource(R.drawable.joined_rect);
            } else if (model.getAvailableSeat() == model.getTotalSeat()) {
                buttonJoin.setText(context.getString(R.string.full));
                buttonJoin.setBackgroundResource(R.drawable.full_rect);
            } else {
                buttonJoin.setText(context.getString(R.string.book));
                buttonJoin.setBackgroundResource(R.drawable.join_rect);
            }

            textViewTime.setText(DateUtils.getClassTime(model.getFromTime(), model.getToTime()));

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    adapterCallbacks.onAdapterItemClick(itemView, itemView, model, position);
                }
            });

        } else {
            itemView.setVisibility(View.GONE);
        }
    }
}
