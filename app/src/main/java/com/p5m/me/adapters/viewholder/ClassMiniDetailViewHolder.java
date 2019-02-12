package com.p5m.me.adapters.viewholder;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.p5m.me.R;
import com.p5m.me.adapters.AdapterCallbacks;
import com.p5m.me.data.main.ClassModel;
import com.p5m.me.helper.Helper;
import com.p5m.me.utils.AppConstants;
import com.p5m.me.utils.DateUtils;
import com.p5m.me.utils.ImageUtils;
import com.p5m.me.utils.WordUtils;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.p5m.me.utils.LanguageUtils.matchFitnessWord;

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
    @BindView(R.id.imageViewClassGender)
    public ImageView imageViewClassGender;

    @BindView(R.id.trainerImage)
    public View trainerImage;
    @BindView(R.id.layoutSpecial)
    public View layoutSpecial;

    @BindView(R.id.buttonJoin)
    public Button buttonJoin;

    @BindView(R.id.imageViewChat)
    public ImageView imageViewChat;

    @BindView(R.id.layoutGender)
    public View layoutGender;
    @BindView(R.id.textViewClassGender)
    public TextView textViewClassGender;
    @BindView(R.id.textViewSpecialClass)
    public TextView textViewSpecialClass;

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

    @BindView(R.id.buttonEditRating)
    public Button buttonEditRating;

    @BindView(R.id.buttonClassRating)
    public Button buttonClassRating;

    private final Context context;
    private int shownInScreen;
    @BindView(R.id.relativeLayoutFitnessLevel)
    public RelativeLayout relativeLayoutFitnessLevel;

    @BindView(R.id.imageViewClassFitnessLevel)
    public ImageView imageViewClassFitnessLevel;

    @BindView(R.id.textViewFitnessLevel)
    public TextView textViewFitnessLevel;

    @BindView(R.id.classBookedWithFriend)
    public RelativeLayout classBookedWithFriend;


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

            layoutGender.setVisibility(View.GONE);
            buttonClassRating.setVisibility(View.GONE);
            if (Helper.isSpecialClass(model)) {
                layoutSpecial.setVisibility(View.VISIBLE);
                textViewSpecialClass.setText(Helper.getSpecialClassText(model));
            } else {
                layoutSpecial.setVisibility(View.GONE);
            }

            if (shownInScreen == AppConstants.AppNavigation.SHOWN_IN_SCHEDULE_UPCOMING) {
                imageViewChat.setVisibility(View.VISIBLE);
                imageViewOptions1.setVisibility(View.VISIBLE);
                imageViewOptions2.setVisibility(View.GONE);
                trainerImage.setVisibility(View.VISIBLE);
                textViewTrainerName.setVisibility(View.VISIBLE);
                if (model.getRefBookingId() != null && model.getRefBookingId() > 0) {
                    classBookedWithFriend.setVisibility(View.VISIBLE);
                } else {
                    classBookedWithFriend.setVisibility(View.GONE);

                }

                buttonJoin.setVisibility(View.GONE);
//                imageViewOptions1.setVisibility(View.GONE);
//                imageViewOptions2.setVisibility(View.VISIBLE);

            } else if (shownInScreen == AppConstants.AppNavigation.SHOWN_IN_SCHEDULE_WISH_LIST) {
                trainerImage.setVisibility(View.VISIBLE);
                textViewTrainerName.setVisibility(View.VISIBLE);

                buttonJoin.setVisibility(View.VISIBLE);

            } else if (shownInScreen == AppConstants.AppNavigation.SHOWN_IN_TRAINER_PROFILE) {
                trainerImage.setVisibility(View.GONE);
                textViewTrainerName.setVisibility(View.GONE);

                buttonJoin.setVisibility(View.VISIBLE);
                imageViewOptions1.setVisibility(View.GONE);
                imageViewOptions2.setVisibility(View.VISIBLE);

                layoutGender.setVisibility(View.VISIBLE);
                textViewClassGender.setText(Helper.getClassGenderText(model.getClassType()));

//                if (model.isUserJoinStatus()) {
//                    imageViewOptions1.setVisibility(View.GONE);
//                    imageViewOptions2.setVisibility(View.GONE);
//                }

            } else if (shownInScreen == AppConstants.AppNavigation.SHOWN_IN_GYM_PROFILE) {
                trainerImage.setVisibility(View.VISIBLE);
                textViewTrainerName.setVisibility(View.VISIBLE);

                buttonJoin.setVisibility(View.VISIBLE);
                imageViewOptions1.setVisibility(View.GONE);
                imageViewOptions2.setVisibility(View.VISIBLE);

                layoutGender.setVisibility(View.VISIBLE);
                textViewClassGender.setText(Helper.getClassGenderText(model.getClassType()));

//                if (model.isUserJoinStatus()) {
//                    imageViewOptions1.setVisibility(View.GONE);
//                    imageViewOptions2.setVisibility(View.GONE);
//                }

            } else if (shownInScreen == AppConstants.AppNavigation.SHOWN_IN_MY_PROFILE_FINISHED) {
                trainerImage.setVisibility(View.VISIBLE);
                textViewTrainerName.setVisibility(View.VISIBLE);

                buttonJoin.setVisibility(View.GONE);
                imageViewOptions1.setVisibility(View.GONE);
                imageViewOptions2.setVisibility(View.GONE);
                try {
                    if (model.getRefBookingId() != null && model.getRefBookingId() > 0) {
                        classBookedWithFriend.setVisibility(View.VISIBLE);
                    } else {
                        classBookedWithFriend.setVisibility(View.GONE);

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    classBookedWithFriend.setVisibility(View.GONE);

                }

            } else {
                trainerImage.setVisibility(View.VISIBLE);
                textViewTrainerName.setVisibility(View.VISIBLE);

                buttonJoin.setVisibility(View.VISIBLE);
                imageViewOptions1.setVisibility(View.GONE);
                imageViewOptions2.setVisibility(View.VISIBLE);

//                if (model.isUserJoinStatus()) {
//                    imageViewOptions1.setVisibility(View.GONE);
//                    imageViewOptions2.setVisibility(View.GONE);
//                }
            }

            if (model.getClassMedia() != null) {
                ImageUtils.setImage(context,
                        model.getClassMedia().getMediaThumbNailUrl(),
                        R.drawable.image_holder, imageViewClass);
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
                ImageUtils.clearImage(context, imageViewTrainerProfile);
            }

            if (model.getGymBranchDetail() != null) {
                textViewLocation.setText(model.getGymBranchDetail().getGymName() + ", " + model.getGymBranchDetail().getBranchName());
            } else {
                textViewLocation.setText("");
            }

            textViewClassName.setText(model.getTitle());
            textViewClassCategory.setText(model.getClassCategory());
            textViewClassDate.setText(DateUtils.getClassDate(model.getClassDate()));

            Helper.setJoinButton(context, buttonJoin, model);
            if (shownInScreen == AppConstants.AppNavigation.SHOWN_IN_MY_PROFILE_FINISHED) {
                if (model.getRatingResDto() == null || model.getRatingResDto().getRating() == 0) {
                    buttonClassRating.setVisibility(View.VISIBLE);
                    buttonEditRating.setVisibility(View.GONE);
                } else {
                    buttonClassRating.setVisibility(View.GONE);
                    buttonEditRating.setVisibility(View.VISIBLE);
                }

            }
            if (model.getFitnessLevel() != null && !model.getFitnessLevel().isEmpty()) {
                relativeLayoutFitnessLevel.setVisibility(View.VISIBLE);
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
                        relativeLayoutFitnessLevel.setVisibility(View.GONE);
                        break;
                }
                setTextFitnessLevel(model);
//                textViewFitnessLevel.setText(WordUtils.capitalize(model.getFitnessLevel().toLowerCase()));
            } else {
                relativeLayoutFitnessLevel.setVisibility(View.GONE);
            }

            textViewTime.setText(DateUtils.getClassTime(model.getFromTime(), model.getToTime()));

            buttonJoin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    adapterCallbacks.onAdapterItemClick(ClassMiniDetailViewHolder.this, buttonJoin, model, position);
                }
            });
            imageViewChat.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    adapterCallbacks.onAdapterItemClick(ClassMiniDetailViewHolder.this, imageViewChat, model, position);
                }
            });
            buttonClassRating.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    adapterCallbacks.onAdapterItemClick(ClassMiniDetailViewHolder.this, buttonClassRating, model, position);
                }
            });
            buttonEditRating.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    adapterCallbacks.onAdapterItemClick(ClassMiniDetailViewHolder.this, buttonEditRating, model, position);
                }
            });

            imageViewOptions2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    adapterCallbacks.onAdapterItemClick(ClassMiniDetailViewHolder.this, imageViewOptions2, model, position);
                }
            });

            imageViewOptions1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    adapterCallbacks.onAdapterItemClick(ClassMiniDetailViewHolder.this, imageViewOptions1, model, position);
                }
            });
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    adapterCallbacks.onAdapterItemClick(ClassMiniDetailViewHolder.this, itemView, model, position);
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
