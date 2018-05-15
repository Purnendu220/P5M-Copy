package www.gymhop.p5m.adapters.viewholder;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import www.gymhop.p5m.R;
import www.gymhop.p5m.adapters.AdapterCallbacks;
import www.gymhop.p5m.adapters.ImageListAdapter;
import www.gymhop.p5m.data.main.GymBranchDetail;
import www.gymhop.p5m.data.main.GymDetailModel;
import www.gymhop.p5m.helper.Helper;
import www.gymhop.p5m.utils.AppConstants;
import www.gymhop.p5m.utils.ImageUtils;

/**
 * Created by MyU10 on 3/10/2018.
 */

public class GymProfileViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.imageViewProfile)
    public ImageView imageViewProfile;
    @BindView(R.id.imageViewMap)
    public ImageView imageViewMap;

    @BindView(R.id.button)
    public Button button;

    @BindView(R.id.textViewName)
    public TextView textViewName;
    @BindView(R.id.textViewClassCategory)
    public TextView textViewClassCategory;
    @BindView(R.id.textViewTrainers)
    public TextView textViewTrainers;
    @BindView(R.id.textViewGallery)
    public TextView textViewGallery;
    @BindView(R.id.textViewLocation)
    public TextView textViewLocation;
    @BindView(R.id.textViewLocationSub)
    public TextView textViewLocationSub;
    @BindView(R.id.textViewLocationPhone)
    public TextView textViewLocationPhone;
    @BindView(R.id.textViewMore)
    public TextView textViewMore;
    @BindView(R.id.textViewInfo)
    public TextView textViewInfo;

    @BindView(R.id.layoutDesc)
    public View layoutDesc;
    @BindView(R.id.layoutGallery)
    public View layoutGallery;
    @BindView(R.id.layoutLocationPhone)
    public View layoutLocationPhone;
    @BindView(R.id.layoutMap)
    public View layoutMap;

    @BindView(R.id.recyclerViewGallery)
    public RecyclerView recyclerViewGallery;

    private final int dp;
    private Context context;

    public GymProfileViewHolder(View view) {
        super(view);

        context = view.getContext();

        ButterKnife.bind(this, view);

        dp = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 1, context.getResources().getDisplayMetrics());

        button.setVisibility(View.GONE);
    }

    public void bind(Object data, final AdapterCallbacks adapterCallbacks, final int position) {
        if (data != null && data instanceof GymDetailModel) {
            itemView.setVisibility(View.VISIBLE);

            final GymDetailModel model = (GymDetailModel) data;

            if (model.getGymBranchResponseList() != null && !model.getGymBranchResponseList().isEmpty()) {
                layoutMap.setVisibility(View.VISIBLE);
                GymBranchDetail gymBranchDetail = model.getGymBranchResponseList().get(0);

                ImageUtils.setImage(context, ImageUtils.generateMapImageUrGymDetail(gymBranchDetail.getLatitude(), gymBranchDetail.getLongitude()),
                        R.drawable.no_map, imageViewMap);

                textViewLocation.setText(gymBranchDetail.getBranchName());
                textViewLocationSub.setText(gymBranchDetail.getAddress());

//                if (!gymBranchDetail.getPhoneNumber().isEmpty()) {
//                    layoutLocationPhone.setVisibility(View.VISIBLE);
//                    textViewLocationPhone.setText(gymBranchDetail.getPhoneNumber());
//                } else {
//                    layoutLocationPhone.setVisibility(View.INVISIBLE);
//                }

                layoutLocationPhone.setVisibility(View.INVISIBLE);

                if (model.getGymBranchResponseList().size() > 1) {
                    textViewMore.setText(Html.fromHtml("<b>+" + (model.getGymBranchResponseList().size() - 1) + " more</b>"));
                    textViewMore.setVisibility(View.VISIBLE);
                } else {
                    textViewMore.setVisibility(View.GONE);
                }

            } else {
                layoutMap.setVisibility(View.GONE);
            }

            if (model.getNumberOfTrainer() == -1) {
                textViewTrainers.setText(Html.fromHtml("trainers"));
            } else {
                textViewTrainers.setText(Html.fromHtml("<b>" + (model.getNumberOfTrainer() + "</b>" + AppConstants.plural(" trainers", model.getNumberOfTrainer()))));
            }

            ImageUtils.setImage(context,
                    model.getProfileImage(),
                    R.drawable.profile_holder_big, imageViewProfile);

            Helper.setFavButton(context, button, model);

            if (model.getMediaResponseDtoList() != null && !model.getMediaResponseDtoList().isEmpty()) {
                layoutGallery.setVisibility(View.VISIBLE);
                textViewGallery.setText(Html.fromHtml("Gallery" + " <b>(" + model.getMediaResponseDtoList().size() + ")</b>"));

                ImageListAdapter adapter = new ImageListAdapter(context, model.getMediaResponseDtoList());
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

            textViewName.setText(model.getStudioName());

//            String categoryList = TrainerListListenerHelper.getCategoryList(model.getClassCategoryList());

//            if (categoryList.isEmpty()) {
//                categoryList = TrainerListListenerHelper.getCategoryListFromClassActivity(model.getClassCategoryList());
//            }

            String categoryList = Helper.getCategoryListFromClassActivity(model.getClassCategoryList());
            textViewClassCategory.setText(categoryList);

//            if (!categoryList.isEmpty()) {
//            } else {
//                textViewClassCategory.setText("");
//            }

            textViewTrainers.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    adapterCallbacks.onAdapterItemClick(GymProfileViewHolder.this, textViewTrainers, model, position);
                }
            });

            layoutMap.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    adapterCallbacks.onAdapterItemClick(GymProfileViewHolder.this, layoutMap, model, position);
                }
            });

            imageViewProfile.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    adapterCallbacks.onAdapterItemClick(GymProfileViewHolder.this, imageViewProfile, model, position);
                }
            });

            textViewMore.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    adapterCallbacks.onAdapterItemClick(GymProfileViewHolder.this, textViewMore, model, position);
                }
            });

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    adapterCallbacks.onAdapterItemClick(GymProfileViewHolder.this, itemView, model, position);
                }
            });

        } else {
            itemView.setVisibility(View.GONE);
        }
    }
}
