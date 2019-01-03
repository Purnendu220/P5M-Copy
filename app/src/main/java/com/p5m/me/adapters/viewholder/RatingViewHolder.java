package com.p5m.me.adapters.viewholder;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.p5m.me.R;
import com.p5m.me.adapters.AdapterCallbacks;
import com.p5m.me.adapters.ImageListAdapter;
import com.p5m.me.data.ClassRatingListData;
import com.p5m.me.data.main.ClassModel;
import com.p5m.me.helper.Helper;
import com.p5m.me.utils.AppConstants;
import com.p5m.me.utils.DateUtils;
import com.p5m.me.utils.ImageUtils;
import com.p5m.me.utils.LogUtils;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.p5m.me.utils.LanguageUtils.currencyConverter;

/**
 * Created by MyU10 on 3/10/2018.
 */

public class RatingViewHolder extends RecyclerView.ViewHolder {


    private final Context context;
    private int shownInScreen;
    @BindView(R.id.imageViewUserProfile)
    ImageView imageViewUserProfile;

    @BindView(R.id.textViewUsername)
    TextView textViewUsername;

    @BindView(R.id.textViewReviewDate)
    TextView textViewReviewDate;

    @BindView(R.id.textViewReviewComment)
    TextView textViewReviewComment;

    @BindView(R.id.textViewClassRating)
    TextView textViewClassRating;

    @BindView(R.id.recyclerViewGallery)
    RecyclerView recyclerViewGallery;

    public RatingViewHolder(View itemView, int shownInScreen) {
        super(itemView);

        context = itemView.getContext();

        ButterKnife.bind(this, itemView);
        this.shownInScreen = shownInScreen;
    }

    public void bind(final Object data, final AdapterCallbacks adapterCallbacks, final int position) {

        if (data != null && data instanceof ClassRatingListData) {
            final ClassRatingListData model = (ClassRatingListData) data;
            itemView.setVisibility(View.VISIBLE);
            textViewUsername.setText(model.getUserDetail().getFirstName());
            textViewReviewDate.setText(DateUtils.getRatingDate(model.getCreateDate()));
            textViewClassRating.setText(currencyConverter(model.getRating())+"");
            if(model.getUserDetail().getProfileImageThumbnail()!=null&&!model.getUserDetail().getProfileImageThumbnail().isEmpty()){
                ImageUtils.setImage(context,
                        model.getUserDetail().getProfileImageThumbnail(),
                        R.drawable.profile_holder, imageViewUserProfile);
            }
            else{
                imageViewUserProfile.setImageResource(R.drawable.profile_holder);
            }

            if(model.getRemark()!=null&&model.getRemark().length()>0){
                 textViewReviewComment.setVisibility(View.VISIBLE);

                textViewReviewComment.setText(model.getRemark());
                }else{
               // textViewReviewComment.setVisibility(View.GONE);

            }
            if (model.getMediaList() != null && !model.getMediaList().isEmpty()) {
                recyclerViewGallery.setVisibility(View.VISIBLE);
                ImageListAdapter adapter = new ImageListAdapter(context, AppConstants.AppNavigation.SHOWN_IN_RATING_LIST, model.getMediaList());
                recyclerViewGallery.setAdapter(adapter);
                recyclerViewGallery.setHasFixedSize(true);
                LinearLayoutManager layoutManager = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);
                recyclerViewGallery.setLayoutManager(layoutManager);


                adapter.notifyDataSetChanged();

            } else {
                recyclerViewGallery.setVisibility(View.GONE);
            }

        } else {
            itemView.setVisibility(View.GONE);

        }
    }
}
