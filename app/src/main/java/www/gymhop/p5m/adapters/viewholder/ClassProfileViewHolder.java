package www.gymhop.p5m.adapters.viewholder;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.View;
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

public class ClassProfileViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.imageViewClass)
    public ImageView imageViewClass;
    @BindView(R.id.imageViewTrainerProfile)
    public ImageView imageViewTrainerProfile;
    @BindView(R.id.imageViewMap)
    public ImageView imageViewMap;

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

    @BindView(R.id.textViewMap)
    public TextView textViewMap;
    @BindView(R.id.textViewMoreDetails)
    public TextView textViewMoreDetails;

    @BindView(R.id.layoutMap)
    public LinearLayout layoutMap;
    @BindView(R.id.layoutDesc)
    public LinearLayout layoutDesc;
    @BindView(R.id.layoutMoreDetails)
    public LinearLayout layoutMoreDetails;
    @BindView(R.id.layoutTrainer)
    public LinearLayout layoutTrainer;

    @BindView(R.id.layoutLocation)
    public View layoutLocation;

    private final Context context;
    private int shownInScreen;

    public ClassProfileViewHolder(View view) {
        super(view);

        context = view.getContext();

        ButterKnife.bind(this, view);
    }

    public void bind(Object data, final AdapterCallbacks adapterCallbacks, final int position) {
        if (data != null && data instanceof ClassModel) {
            itemView.setVisibility(View.VISIBLE);

            final ClassModel model = (ClassModel) data;

            if (Helper.isSpecialClass(model)) {
                textViewSpecialClass.setVisibility(View.VISIBLE);
                textViewSpecialClass.setText(Helper.getSpecialClassText(model));

            } else {
                textViewSpecialClass.setVisibility(View.GONE);
            }

            if (!model.getDescription().isEmpty()) {

                layoutDesc.setVisibility(View.VISIBLE);
                textViewInfo.setText(model.getDescription());
            } else {
                layoutDesc.setVisibility(View.GONE);
            }

            if (model.getGymBranchDetail() != null) {

                layoutMap.setVisibility(View.VISIBLE);
                ImageUtils.setImage(context, ImageUtils.generateMapImageUrlClassDetail(model.getGymBranchDetail().getLatitude(), model.getGymBranchDetail().getLongitude()),
                        R.drawable.no_map, imageViewMap);
                textViewMap.setText(Html.fromHtml("<b>ADDRESS:</b>" + context.getString(R.string.gaping) + model.getGymBranchDetail().getAddress()));
            } else {
                layoutMap.setVisibility(View.GONE);
            }

            if (model.getSpecialNote().isEmpty() && model.getReminder().isEmpty() && model.getGymBranchDetail().getStudioInstruction().isEmpty()) {
                layoutMoreDetails.setVisibility(View.GONE);
            } else {
                layoutMoreDetails.setVisibility(View.VISIBLE);

                StringBuffer stringBuffer = new StringBuffer("");

                if (!model.getReminder().isEmpty()) {
                    stringBuffer.append("<b>REMINDERS:</b>" + context.getString(R.string.gaping) + model.getReminder());
                }

                if (model.getGymBranchDetail() != null) {
                    if (!model.getGymBranchDetail().getStudioInstruction().isEmpty()) {
                        if (!stringBuffer.toString().isEmpty()) {
                            stringBuffer.append("<br/><br/>");
                        }
                        stringBuffer.append("<b>STUDIO INSTRUCTION:</b>" + context.getString(R.string.gaping) + model.getGymBranchDetail().getStudioInstruction());
                    }
                }

                if (!model.getSpecialNote().isEmpty()) {
                    if (!stringBuffer.toString().isEmpty()) {
                        stringBuffer.append("<br/><br/>");
                    }
                    stringBuffer.append("<b>SPECIAL NOTE BY TRAINER:</b>" + context.getString(R.string.gaping) + model.getSpecialNote());
                }

                textViewMoreDetails.setText(Html.fromHtml(stringBuffer.toString()));

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
                ImageUtils.clearImage(context, imageViewTrainerProfile);
                textViewLocation.setText("");
            }

            if (model.getGymBranchDetail() != null) {
                textViewLocation.setText(model.getGymBranchDetail().getGymName() + ", " + model.getGymBranchDetail().getBranchName());
            } else {
                textViewLocation.setText("");
            }

            textViewClassName.setText(model.getTitle());
            textViewClassCategory.setText(model.getClassCategory());
            textViewClassDate.setText(DateUtils.getClassDate(model.getClassDate()));

            textViewAvailable.setText(model.getAvailableSeat() + " available " +
                    AppConstants.plural("seat", model.getAvailableSeat()));

            textViewTime.setText(DateUtils.getClassTime(model.getFromTime(), model.getToTime()));
            textViewGender.setText(Helper.getClassGenderText(model.getClassType()));

            layoutLocation.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    adapterCallbacks.onAdapterItemClick(ClassProfileViewHolder.this, layoutLocation, model, position);
                }
            });

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    adapterCallbacks.onAdapterItemClick(ClassProfileViewHolder.this, itemView, model, position);
                }
            });

            imageViewClass.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    adapterCallbacks.onAdapterItemClick(ClassProfileViewHolder.this, imageViewClass, model, position);
                }
            });

            imageViewMap.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    adapterCallbacks.onAdapterItemClick(ClassProfileViewHolder.this, imageViewMap, model, position);
                }
            });

            layoutMap.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    adapterCallbacks.onAdapterItemClick(ClassProfileViewHolder.this, layoutMap, model, position);
                }
            });

            textViewLocation.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    adapterCallbacks.onAdapterItemClick(ClassProfileViewHolder.this, textViewLocation, model, position);
                }
            });

            layoutTrainer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    adapterCallbacks.onAdapterItemClick(ClassProfileViewHolder.this, layoutTrainer, model, position);
                }
            });

            layoutMap.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    adapterCallbacks.onAdapterItemClick(ClassProfileViewHolder.this, layoutTrainer, model, position);
                }
            });
        } else {
            itemView.setVisibility(View.GONE);
        }
    }
}
