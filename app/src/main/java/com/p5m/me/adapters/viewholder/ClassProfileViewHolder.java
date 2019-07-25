package com.p5m.me.adapters.viewholder;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.Html;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMapOptions;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.p5m.me.R;
import com.p5m.me.adapters.AdapterCallbacks;
import com.p5m.me.data.main.ClassModel;
import com.p5m.me.helper.Helper;
import com.p5m.me.utils.AppConstants;
import com.p5m.me.utils.DateUtils;
import com.p5m.me.utils.ImageUtils;
import com.p5m.me.utils.LanguageUtils;
import com.p5m.me.view.activity.Main.ClassProfileActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.p5m.me.utils.LanguageUtils.matchFitnessWord;

/**
 * Created by MyU10 on 3/10/2018.
 */

public class ClassProfileViewHolder extends RecyclerView.ViewHolder implements
        OnMapReadyCallback {

    @BindView(R.id.imageViewClass)
    public ImageView imageViewClass;
    @BindView(R.id.imageViewTrainerProfile)
    public ImageView imageViewTrainerProfile;
 /*   @BindView(R.id.imageViewMap)
    public ImageView imageViewMap;*/

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

    @BindView(R.id.layoutMapClick)
    public View layoutMapClick;

    @BindView(R.id.layoutLocation)
    public View layoutLocation;
    @BindView(R.id.mapImageView)
    public MapView mapView;

    @BindView(R.id.studioRating)
    RatingBar studioRating;

    @BindView(R.id.textViewRatingCount)
    TextView textViewRatingCount;

    @BindView(R.id.textViewReviewCountText)
    TextView textViewReviewCountText;

    @BindView(R.id.linearLayoutStudioRating)
    LinearLayout linearLayoutStudioRating;

    @BindView(R.id.layoutSeeAllReview)
    LinearLayout layoutSeeAllReview;

    @BindView(R.id.linearLayoutClassRating)
    public LinearLayout linearLayoutClassRating;

    @BindView(R.id.textViewClassRating)
    public TextView textViewClassRating;

    @BindView(R.id.relativeLayoutFitnessLevel)
    public RelativeLayout relativeLayoutFitnessLevel;

    @BindView(R.id.imageViewClassFitnessLevel)
    public ImageView imageViewClassFitnessLevel;

    @BindView(R.id.textViewFitnessLevel)
    public TextView textViewFitnessLevel;

    private final Context context;
    private int shownInScreen;
    private LatLng latlng;
    private GoogleMap mMap;

    public ClassProfileViewHolder(View view) {
        super(view);

        context = view.getContext();

        ButterKnife.bind(this, view);
    }

    @SuppressLint("StringFormatInvalid")
    public void bind(Object data, final AdapterCallbacks adapterCallbacks, final int position) {
        if (data != null && data instanceof ClassModel) {
            itemView.setVisibility(View.VISIBLE);

            final ClassModel model = (ClassModel) data;
            initializeMapView();
            latlng = new LatLng(model.getGymBranchDetail().getLatitude(), model.getGymBranchDetail().getLongitude());
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
               /* ImageUtils.setImage(context, ImageUtils.generateMapImageUrlClassDetail(model.getGymBranchDetail().getLatitude(), model.getGymBranchDetail().getLongitude()),
                        R.drawable.no_map, imageViewMap);*/
                textViewMap.setText(Html.fromHtml(context.getString(R.string.address) + context.getString(R.string.gaping) + model.getGymBranchDetail().getAddress()));
            } else {
                layoutMap.setVisibility(View.GONE);
            }

            String studioInstruction = "";

            if (model.getGymBranchDetail() != null) {
                studioInstruction = model.getGymBranchDetail().getStudioInstruction();
            }

            if (model.getSpecialNote().isEmpty() && model.getReminder().isEmpty() && studioInstruction.isEmpty()) {
                layoutMoreDetails.setVisibility(View.GONE);
            } else {
                layoutMoreDetails.setVisibility(View.VISIBLE);

                StringBuffer stringBuffer = new StringBuffer("");

                if (!model.getReminder().isEmpty()) {
                    stringBuffer.append(context.getString(R.string.remenders) + context.getString(R.string.gaping) + model.getReminder());
                }

                if (model.getGymBranchDetail() != null) {
                    if (!model.getGymBranchDetail().getStudioInstruction().isEmpty()) {
                        if (!stringBuffer.toString().isEmpty()) {
                            stringBuffer.append("<br/><br/>");
                        }
                        stringBuffer.append(context.getString(R.string.studio_instruction) + context.getString(R.string.gaping) + model.getGymBranchDetail().getStudioInstruction());
                    }
                }

                if (!model.getSpecialNote().isEmpty()) {
                    if (!stringBuffer.toString().isEmpty()) {
                        stringBuffer.append("<br/><br/>");
                    }
                    stringBuffer.append(context.getString(R.string.special_note_by_trainer) + context.getString(R.string.gaping) + model.getSpecialNote());
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
                textViewLocation.setText(model.getGymBranchDetail().getGymName() + ", " + model.getGymBranchDetail().getBranchName() + ", " + model.getGymBranchDetail().getLocalityName());
            } else {
                textViewLocation.setText("");
            }
            if (model.getRating() > 0 && model.getNumberOfRating() > 0) {

                CharSequence text = String.format(context.getString(R.string.review_based_on), LanguageUtils.numberConverter(model.getNumberOfRating()) + "");
                linearLayoutStudioRating.setVisibility(View.VISIBLE);
                studioRating.setRating(model.getRating());

                textViewRatingCount.setText(LanguageUtils.numberConverter(model.getRating()) + "/" + LanguageUtils.numberConverter(5.0));
                textViewReviewCountText.setText(text);
                studioRating.setIsIndicator(true);
                linearLayoutClassRating.setVisibility(View.GONE);
                textViewClassRating.setText(model.getRating() + "");
            } else {
                linearLayoutClassRating.setVisibility(View.GONE);
                linearLayoutStudioRating.setVisibility(View.GONE);
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
            } else {
                relativeLayoutFitnessLevel.setVisibility(View.GONE);
            }
            textViewClassName.setText(model.getTitle());
            textViewClassCategory.setText(model.getClassCategory());
            textViewClassDate.setText(DateUtils.getClassDate(model.getClassDate()));
//            textViewAvailable.setText( NumberFormat.getNumberInstance(Constants.LANGUAGE).format(model.getAvailableSeat()) + " " + context.getString(R.string.available_seats) + " ");
//                    +
//                    AppConstants.plural(context.getString(R.string.seat), model.getAvailableSeat()));
            LanguageUtils.setText(textViewAvailable, model.getAvailableSeat(), context.getString(R.string.available_seats) + " ");

            textViewTime.setText(DateUtils.getClassTime(model.getFromTime(), model.getToTime()));
            textViewGender.setText(Helper.getClassGenderText(model.getClassType()));
            layoutSeeAllReview.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    adapterCallbacks.onAdapterItemClick(ClassProfileViewHolder.this, layoutSeeAllReview, model, position);
                }
            });
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

          /*  imageViewMap.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    adapterCallbacks.onAdapterItemClick(ClassProfileViewHolder.this, imageViewMap, model, position);
                }
            });*/

            layoutMapClick.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    adapterCallbacks.onAdapterItemClick(ClassProfileViewHolder.this, layoutMapClick, model, position);
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
        } else {
            itemView.setVisibility(View.GONE);
        }
    }

    private void setTextFitnessLevel(ClassModel model) {
        String fitnessLevel = matchFitnessWord(model.getFitnessLevel(), context);
        textViewFitnessLevel.setText(fitnessLevel);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        MapsInitializer.initialize(context.getApplicationContext());
        mMap = googleMap;
        mMap.getUiSettings().setAllGesturesEnabled(false);
        mMap.getUiSettings().setMapToolbarEnabled(false);
        GoogleMapOptions options = new GoogleMapOptions().liteMode(true);
        addMarker();
    }

    private void addMarker() {
        mMap.addMarker(new MarkerOptions()
                .position(latlng));
        CameraUpdate location = CameraUpdateFactory.newLatLngZoom(
                latlng, 18);
        mMap.animateCamera(location);
    }

    public void initializeMapView() {
        if (mapView != null) {
            mapView.onCreate(null);
            mapView.onResume();
            mapView.getMapAsync(this);
        }
    }
}
