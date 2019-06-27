package com.p5m.me.adapters.viewholder;

import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.p5m.me.R;
import com.p5m.me.adapters.AdapterCallbacks;
import com.p5m.me.data.main.ClassModel;
import com.p5m.me.helper.Helper;
import com.p5m.me.utils.AppConstants;
import com.p5m.me.utils.DateUtils;
import com.p5m.me.utils.ImageUtils;
import com.p5m.me.utils.LanguageUtils;
import com.p5m.me.utils.WordUtils;
import com.p5m.me.view.activity.Main.ClassProfileActivityNew;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.p5m.me.utils.LanguageUtils.matchFitnessWord;

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

    @BindView(R.id.linearLayoutClassRating)
    public LinearLayout linearLayoutClassRating;

    @BindView(R.id.textViewClassRating)
    public TextView textViewClassRating;

    @BindView(R.id.layoutLocation)
    public View layoutLocation;
    @BindView(R.id.layoutFitnessLevel)
    public LinearLayout layoutFitnessLevel;

    @BindView(R.id.imageViewClassFitnessLevel)
    public ImageView imageViewClassFitnessLevel;

    @BindView(R.id.textViewFitnessLevel)
    public TextView textViewFitnessLevel;

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

//            if (model.isUserJoinStatus()) {
//                imageViewOptions.setVisibility(View.GONE);
//            } else {
//                imageViewOptions.setVisibility(View.VISIBLE);
//            }

            if (Helper.isSpecialClass(model)) {
                textViewSpecialClass.setVisibility(View.VISIBLE);
                textViewSpecialClass.setText(Helper.getSpecialClassText(model));

            } else {
                textViewSpecialClass.setVisibility(View.GONE);
            }

            if (model.getClassMedia() != null) {
                ImageUtils.setImage(context,
                        model.getClassMedia().getMediaUrl(),
                        R.drawable.class_holder, imageViewClass);
            } else {
                ImageUtils.clearImage(context, imageViewClass);
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

                textViewLocation.setText(model.getGymBranchDetail().getGymName()+", "+model.getGymBranchDetail().getBranchName()
                +", "+model.getGymBranchDetail().getLocalityName());
            }

            textViewClassName.setText(model.getTitle());
            textViewClassCategory.setText(model.getClassCategory());
            textViewClassDate.setText(DateUtils.getClassDate(model.getClassDate()));

            textViewAvailable.setText(model.getAvailableSeat() + " " +
                    AppConstants.plural(context.getString(R.string.available_seats), model.getAvailableSeat()));

            Helper.setJoinButton(context, buttonJoin, model);

            textViewInfo.setText(model.getShortDesc());

            textViewTime.setText(DateUtils.getClassTime(model.getFromTime(), model.getToTime()));
            textViewGender.setText(Helper.getClassGenderText(model.getClassType()));
            if (model.getRating() != 0.0F && model.getRating() > 0) {
                linearLayoutClassRating.setVisibility(View.VISIBLE);
                textViewClassRating.setText(LanguageUtils.numberConverter(model.getRating()) + "");
            } else {
                linearLayoutClassRating.setVisibility(View.GONE);
            }
            if (model.getFitnessLevel() != null && !model.getFitnessLevel().isEmpty()) {
                layoutFitnessLevel.setVisibility(View.GONE);

                switch (model.getFitnessLevel()) {
                    case AppConstants.FitnessLevel.CLASS_LEVEL_BASIC:
                        imageViewClassFitnessLevel.setImageResource(R.drawable.class_level_get);
                        break;
                    case AppConstants.FitnessLevel.CLASS_LEVEL_INTERMEDIATE:
                        imageViewClassFitnessLevel.setImageResource(R.drawable.class_level_set);

                        break;
                    case AppConstants.FitnessLevel.CLASS_LEVEL_ADVANCED:
                        imageViewClassFitnessLevel.setImageResource(R.drawable.class_level_pro);

                        break;
                    default:
                        layoutFitnessLevel.setVisibility(View.GONE);
                        break;
                }
                setTextFitnessLevel(model);
//                textViewFitnessLevel.setText(WordUtils.capitalize(model.getFitnessLevel().toLowerCase()));
            } else {
                layoutFitnessLevel.setVisibility(View.GONE);
            }

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    adapterCallbacks.onAdapterItemClick(ClassViewHolder.this, imageViewClass, model, position);
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


    private void setTextFitnessLevel(ClassModel model) {
        String fitnessLevel = matchFitnessWord(model.getFitnessLevel(), context);
        textViewFitnessLevel.setText(fitnessLevel);
    }
}
