package com.p5m.me.adapters.viewholder;

import android.content.Context;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Html;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.p5m.me.R;
import com.p5m.me.adapters.AdapterCallbacks;
import com.p5m.me.adapters.ImageListAdapter;
import com.p5m.me.analytics.MixPanel;
import com.p5m.me.data.main.GymBranchDetail;
import com.p5m.me.data.main.TrainerDetailModel;
import com.p5m.me.helper.Helper;
import com.p5m.me.utils.AppConstants;
import com.p5m.me.utils.ImageUtils;
import com.p5m.me.utils.LanguageUtils;
import com.p5m.me.view.activity.Main.GymProfileActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by MyU10 on 3/10/2018.
 */

public class TrainerProfileViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.imageViewProfile)
    public ImageView imageViewProfile;

    @BindView(R.id.imageViewCover)
    public ImageView imageViewCover;


    @BindView(R.id.button)
    public ImageButton button;





    @BindView(R.id.textViewName)
    public TextView textViewName;
    @BindView(R.id.textViewClassCategory)
    public TextView textViewClassCategory;
    @BindView(R.id.textViewFollowers)
    public TextView textViewFollowers;
    @BindView(R.id.textViewGallery)
    public TextView textViewGallery;
    @BindView(R.id.textViewInfo)
    public TextView textViewInfo;

    @BindView(R.id.layoutDesc)
    public View layoutDesc;
    @BindView(R.id.layoutGallery)
    public View layoutGallery;
    @BindView(R.id.layoutLocation)
    public LinearLayout layoutLocation;

    @BindView(R.id.recyclerViewGallery)
    public RecyclerView recyclerViewGallery;

    private final int dp;
    private Context context;

    public TrainerProfileViewHolder(View view) {
        super(view);

        context = view.getContext();

        ButterKnife.bind(this, view);

        dp = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 1, context.getResources().getDisplayMetrics());
    }

    public void bind(Object data, final AdapterCallbacks adapterCallbacks, final int position, final int shownIn) {
        if (data != null && data instanceof TrainerDetailModel) {
            itemView.setVisibility(View.VISIBLE);

            final TrainerDetailModel model = (TrainerDetailModel) data;

            if (model.getFollowerCount() == -1) {
                textViewFollowers.setText(Html.fromHtml(context.getString(R.string.followers)));
            } else if (model.getFollowerCount() == 0) {
                textViewFollowers.setText(Html.fromHtml(context.getString(R.string.no_followers)));
            } else {
//                textViewFollowers.setText(Html.fromHtml("<b>" + NumberFormat.getNumberInstance(Constants.LANGUAGE).format(model.getFollowerCount()) + "</b>" + " "+context.getString(R.string.followers)));
                LanguageUtils.setText(textViewFollowers,model.getFollowerCount(), " "+context.getString(R.string.followers));
            }

            ImageUtils.setImage(context,
                    model.getProfileImage(),
                    R.drawable.profile_holder, imageViewProfile);

            ImageUtils.setImage(context,
                    model.getCoverImage(),
                    R.drawable.cover_stub, imageViewCover);

            Helper.setFavButton(context, button, model);

            if (model.getMediaResponseDtoList() != null && !model.getMediaResponseDtoList().isEmpty()) {
                layoutGallery.setVisibility(View.VISIBLE);
                textViewGallery.setText(Html.fromHtml(context.getString(R.string.gallery) + " <b>(" + model.getMediaResponseDtoList().size() + ")</b>"));

                ImageListAdapter adapter = new ImageListAdapter(context, AppConstants.AppNavigation.SHOWN_IN_TRAINER_PROFILE, model.getMediaResponseDtoList());
                recyclerViewGallery.setAdapter(adapter);
                recyclerViewGallery.setHasFixedSize(true);

                LinearLayoutManager layoutManager = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);
                recyclerViewGallery.setLayoutManager(layoutManager);

                adapter.notifyDataSetChanged();

            } else {
                layoutGallery.setVisibility(View.GONE);
            }

            if (model.getBio().trim().isEmpty()) {
                layoutDesc.setVisibility(View.GONE);
            } else {
                layoutDesc.setVisibility(View.VISIBLE);
                textViewInfo.setText(model.getBio());
            }

            textViewName.setText(model.getFirstName());

            String categoryList = Helper.getCategoryList(model.getCategoryList());

            if (model.getClassCategoryList() != null) {
                categoryList = Helper.getCategoryListFromClassActivity(model.getClassCategoryList());
            }

            textViewClassCategory.setText(categoryList);

//            if (!categoryList.isEmpty()) {
//            } else {
//                textViewClassCategory.setText("");
//            }

            layoutLocation.removeAllViews();
            if (model.getTrainerBranchResponseList() != null && !model.getTrainerBranchResponseList().isEmpty()) {

                String gymsId = "";

                for (final GymBranchDetail gymBranchDetail : model.getTrainerBranchResponseList()) {

                    if (!gymsId.contains(gymBranchDetail.getGymId() + ",")) {
                        gymsId = gymsId + gymBranchDetail.getGymId() + ",";

                        final TextView textView = new TextView(context);
                        textView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                        textView.setPadding(0, dp * 2, 0, dp * 4);
                        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
                        textView.setText(Html.fromHtml(context.getString(R.string.coach_at)+" <b>" + gymBranchDetail.getGymName() + "</b>"));
                        textView.setClickable(true);
                        textView.setTextColor(ContextCompat.getColor(context, R.color.theme_dark_text));

                        layoutLocation.addView(textView);

                        textView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                GymProfileActivity.open(context, gymBranchDetail.getGymId(), AppConstants.AppNavigation.SHOWN_IN_TRAINER_PROFILE);
                                MixPanel.trackTrainerProfileEvent(AppConstants.Tracker.VISIT_GYM_PROFILE);
                            }
                        });
                    }
                }
            }

            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    adapterCallbacks.onAdapterItemClick(TrainerProfileViewHolder.this, button, model, position);
                }
            });

            imageViewProfile.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    adapterCallbacks.onAdapterItemClick(TrainerProfileViewHolder.this, imageViewProfile, model, position);
                }
            });
            imageViewCover.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    adapterCallbacks.onAdapterItemClick(TrainerProfileViewHolder.this, imageViewCover, model, position);
                }
            });

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    adapterCallbacks.onAdapterItemClick(TrainerProfileViewHolder.this, itemView, model, position);
                }
            });

        } else {
            itemView.setVisibility(View.GONE);
        }
    }
}
