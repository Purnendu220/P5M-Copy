package com.p5m.me.adapters.viewholder;

import android.content.Context;
import android.graphics.Paint;
import android.text.Html;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.p5m.me.R;
import com.p5m.me.adapters.AdapterCallbacks;
import com.p5m.me.adapters.CovidSaftyAdapter;
import com.p5m.me.adapters.ImageListAdapter;
import com.p5m.me.data.main.ClassModel;
import com.p5m.me.data.main.GymBranchDetail;
import com.p5m.me.data.main.GymDetailModel;
import com.p5m.me.helper.Helper;
import com.p5m.me.utils.AppConstants;
import com.p5m.me.utils.ImageUtils;
import com.p5m.me.utils.LanguageUtils;
import com.p5m.me.view.custom.AlertP5MCreditInfo;
import com.p5m.me.view.custom.SafetyByGymPopup;
import com.p5m.me.view.fragment.MembershipFragment;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.p5m.me.utils.LanguageUtils.numberConverter;

/**
 * Created by MyU10 on 3/10/2018.
 */

public class GymProfileViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.imageViewProfile)
    public ImageView imageViewProfile;

    @BindView(R.id.imageViewCover)
    public ImageView imageViewCover;

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

    @BindView(R.id.linearLayoutTotalLocations)
    public LinearLayout linearLayoutTotalLocations;

    @BindView(R.id.textViewTotalLocations)
    public TextView textViewTotalLocations;

    @BindView(R.id.saftyLayout)
    RelativeLayout saftyLayout;
    @BindView(R.id.text_safety_msg)
    TextView textSafetyMsg;

    @BindView(R.id.safetyAnsRecyclerView)
    RecyclerView safetyAnsRecyclerView;

    @BindView(R.id.msgLayout)
    RelativeLayout msgLayout;

    @BindView(R.id.img_safety_arrow)
    ImageView imgSafetyArrow;

    @BindView(R.id.expandLayout)
    LinearLayout expandLayout;

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
                setUpCovidSafty(model,adapterCallbacks);
//                if (!gymBranchDetail.getPhoneNumber().isEmpty()) {
//                    layoutLocationPhone.setVisibility(View.VISIBLE);
//                    textViewLocationPhone.setText(gymBranchDetail.getPhoneNumber());
//                } else {
//                    layoutLocationPhone.setVisibility(View.INVISIBLE);
//                }

                layoutLocationPhone.setVisibility(View.INVISIBLE);

                if (model.getGymBranchResponseList().size() > 1) {
                    textViewMore.setText(Html.fromHtml("<b>+" + numberConverter(model.getGymBranchResponseList().size() - 1) + " " + context.getString(R.string.more) + "</b>"));
                    textViewMore.setVisibility(View.VISIBLE);
                } else {
                    textViewMore.setVisibility(View.GONE);
                }
                try {
                    String locationsString = String.format(context.getString(R.string.total_locations), model.getGymBranchResponseList().size());
                    linearLayoutTotalLocations.setVisibility(View.VISIBLE);
                    textViewTotalLocations.setText(locationsString);
                } catch (Exception e) {
                    linearLayoutTotalLocations.setVisibility(View.GONE);

                }
            } else {
                layoutMap.setVisibility(View.GONE);
            }

            if (model.getNumberOfTrainer() == -1) {
                textViewTrainers.setText(Html.fromHtml(context.getString(R.string.trainer).toLowerCase()));
            } else {
               /* String src = "<b dir=\"ltr\">"+(numberConverter(model.getNumberOfTrainer())) +"</b> ";
                String trainer=AppConstants.plural(context.getString(R.string.trainer), model.getNumberOfTrainer());

//                textViewTrainers.setText(Html.fromHtml("<b>" + (numberConverter(model.getNumberOfTrainer()) + "</b>" +" "+ AppConstants.plural(context.getString(R.string.trainer), model.getNumberOfTrainer()))));
                textViewTrainers.setText(trainer);
                textViewTrainers.append(Html.fromHtml(src));*/
                LanguageUtils.setText(textViewTrainers,model.getNumberOfTrainer() ,AppConstants.plural(context.getString(R.string.trainer), model.getNumberOfTrainer()));



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
                textViewGallery.setText(Html.fromHtml(context.getString(R.string.gallery)+" " + " <b>(" + numberConverter(model.getMediaResponseDtoList().size()) + ")</b>"));

                ImageListAdapter adapter = new ImageListAdapter(context, AppConstants.AppNavigation.SHOWN_IN_GYM_PROFILE, model.getMediaResponseDtoList());
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
            imageViewCover.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    adapterCallbacks.onAdapterItemClick(GymProfileViewHolder.this, imageViewCover, model, position);
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

    public void setUpCovidSafty(GymDetailModel model,AdapterCallbacks adapterCallbacks){
        if(model.getCovidSafetyList()!=null&&model.getCovidSafetyList().size()>0)
        {
            saftyLayout.setVisibility(View.VISIBLE);

            textSafetyMsg.setText(Html.fromHtml(String.format(context.getString(R.string.safty_message),  model.getStudioName() )));
            textSafetyMsg.setPaintFlags(textSafetyMsg.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

            safetyAnsRecyclerView.setLayoutManager(new LinearLayoutManager(context));
            safetyAnsRecyclerView.setHasFixedSize(false);

            CovidSaftyAdapter covidSaftyAdapter = new CovidSaftyAdapter(context, adapterCallbacks,true);
            safetyAnsRecyclerView.setAdapter(covidSaftyAdapter);

            covidSaftyAdapter.addAll(model.getCovidSafetyList());
            covidSaftyAdapter.notifyDataSetChanged();
            msgLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    SafetyByGymPopup alert = new SafetyByGymPopup(context, model, adapterCallbacks);
                    alert.show();

                }
            });
        }
        else{
            saftyLayout.setVisibility(View.GONE);
        }


    }

}
