package www.gymhop.p5m.adapters.viewholder;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import www.gymhop.p5m.R;
import www.gymhop.p5m.adapters.AdapterCallbacks;
import www.gymhop.p5m.data.main.ClassModel;
import www.gymhop.p5m.helper.Helper;
import www.gymhop.p5m.utils.AppConstants;
import www.gymhop.p5m.utils.DateUtils;
import www.gymhop.p5m.utils.ImageUtils;

/**
 * Created by MyU10 on 3/10/2018.
 */

public class ClassViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.imageViewClass)
    public ImageView imageViewClass;
    @BindView(R.id.imageViewTrainerProfile)
    public ImageView imageViewTrainerProfile;
    @BindView(R.id.imageViewOptions)
    public ImageView imageViewOptions;

    @BindView(R.id.buttonJoin)
    public Button buttonJoin;

    @BindView(R.id.textViewClassName)
    public TextView textViewClassName;
    @BindView(R.id.textViewClassCategory)
    public TextView textViewClassCategory;
    @BindView(R.id.textViewClassDate)
    public TextView textViewClassDate;
    @BindView(R.id.textViewAvailable)
    public TextView textViewAvailable;
    @BindView(R.id.textViewInfo)
    public TextView textViewInfo;
    @BindView(R.id.textViewTime)
    public TextView textViewTime;
    @BindView(R.id.textViewLocation)
    public TextView textViewLocation;
    @BindView(R.id.textViewGender)
    public TextView textViewGender;
    @BindView(R.id.textViewTrainerName)
    public TextView textViewTrainerName;
    @BindView(R.id.textViewSpecialClass)
    public TextView textViewSpecialClass;

    @BindView(R.id.layoutTrainer)
    public LinearLayout layoutTrainer;

    @BindView(R.id.layoutLocation)
    public View layoutLocation;

    private final Context context;
    private int shownInScreen;

    public ClassViewHolder(View itemView, int shownInScreen) {
        super(itemView);

        context = itemView.getContext();

        ButterKnife.bind(this, itemView);
        this.shownInScreen = shownInScreen;
    }

    public void bind(final Object data, final AdapterCallbacks adapterCallbacks, final int position) {

        if (data != null && data instanceof ClassModel) {
            itemView.setVisibility(View.VISIBLE);

            final ClassModel model = (ClassModel) data;

            if (Helper.isSpecialClass(model)) {
                textViewSpecialClass.setVisibility(View.VISIBLE);
                String text = "Special Class";
                if (!model.getSpecialClassRemark().isEmpty()) {
                    text = model.getSpecialClassRemark();
                }

                textViewSpecialClass.setText(text);

            } else {
                textViewSpecialClass.setVisibility(View.GONE);
            }

            if (model.getClassMedia() != null) {
                ImageUtils.setImage(context,
                        model.getClassMedia().getMediaUrl(),
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

            } else {

                ImageUtils.setImage(context,
                        R.drawable.profile_holder,
                        R.drawable.profile_holder, imageViewTrainerProfile);
                textViewLocation.setText("");
            }

            if (model.getGymBranchDetail() != null) {
                textViewLocation.setText(model.getGymBranchDetail().getGymName() + ", " + model.getGymBranchDetail().getBranchName());
            }

            textViewClassName.setText(model.getTitle());
            textViewClassCategory.setText(model.getClassCategory());
            textViewClassDate.setText(DateUtils.getClassDate(model.getClassDate()));

            if (model.getAvailableSeat() == 0) {
                textViewAvailable.setText("No seats available");
            } else {
                textViewAvailable.setText(model.getAvailableSeat() + " available " +
                        AppConstants.plural("seat", model.getAvailableSeat()));
            }

            Helper.setJoinButton(context, buttonJoin, model);

            textViewInfo.setText(model.getShortDesc());

            textViewTime.setText(DateUtils.getClassTime(model.getFromTime(), model.getToTime()));
            textViewGender.setText(Helper.getClassTypeText(model.getClassType()));

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    adapterCallbacks.onAdapterItemClick(ClassViewHolder.this, itemView, model, position);
                }
            });

            imageViewOptions.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    adapterCallbacks.onAdapterItemClick(ClassViewHolder.this, imageViewOptions, model, position);
                }
            });

            layoutLocation.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    adapterCallbacks.onAdapterItemClick(ClassViewHolder.this, layoutLocation, model, position);
                }
            });

            textViewLocation.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    adapterCallbacks.onAdapterItemClick(ClassViewHolder.this, textViewLocation, model, position);
                }
            });

            layoutTrainer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    adapterCallbacks.onAdapterItemClick(ClassViewHolder.this, layoutTrainer, model, position);
                }
            });

            buttonJoin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    adapterCallbacks.onAdapterItemClick(ClassViewHolder.this, buttonJoin, model, position);
                }
            });
        } else {
            itemView.setVisibility(View.GONE);
        }
    }
}
