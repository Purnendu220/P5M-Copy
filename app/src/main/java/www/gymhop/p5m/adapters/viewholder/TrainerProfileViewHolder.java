package www.gymhop.p5m.adapters.viewholder;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import www.gymhop.p5m.R;
import www.gymhop.p5m.adapters.AdapterCallbacks;
import www.gymhop.p5m.adapters.ImageListAdapter;
import www.gymhop.p5m.data.main.GymBranchDetail;
import www.gymhop.p5m.data.main.TrainerDetailModel;
import www.gymhop.p5m.helper.Helper;
import www.gymhop.p5m.helper.TrainerListListenerHelper;
import www.gymhop.p5m.utils.ImageUtils;

/**
 * Created by MyU10 on 3/10/2018.
 */

public class TrainerProfileViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.imageViewProfile)
    public ImageView imageViewProfile;

    @BindView(R.id.button)
    public Button button;

    @BindView(R.id.textViewName)
    public TextView textViewName;
    @BindView(R.id.textViewClassCategory)
    public TextView textViewClassCategory;
    @BindView(R.id.textViewFollowers)
    public TextView textViewFollowers;
    @BindView(R.id.textViewGallery)
    public TextView textViewGallery;

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

    public void bind(Object data, final AdapterCallbacks adapterCallbacks, final int position) {
        if (data != null && data instanceof TrainerDetailModel) {
            itemView.setVisibility(View.VISIBLE);

            final TrainerDetailModel model = (TrainerDetailModel) data;

            if (model.getFollowerCount() == -1) {
                textViewFollowers.setText(Html.fromHtml("followers"));
            } else if (model.getFollowerCount() == 0) {
                textViewFollowers.setText(Html.fromHtml("No followers"));
            } else {
                textViewFollowers.setText(Html.fromHtml("<b>" + (model.getFollowerCount() + "</b>" + " followers")));
            }

            ImageUtils.setImage(context,
                    model.getProfileImage(),
                    R.drawable.class_holder, imageViewProfile);

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

            textViewName.setText(model.getFirstName());

            String categoryList = TrainerListListenerHelper.getCategoryList(model.getCategoryList());

            if (categoryList.isEmpty()) {
                categoryList = TrainerListListenerHelper.getCategoryListFromClassActivity(model.getClassCategoryList());
            }

            if (!categoryList.isEmpty()) {
                textViewClassCategory.setText(categoryList);
            } else {
                textViewClassCategory.setText("");
            }

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
                        textView.setText(Html.fromHtml("Coach at <b>" + gymBranchDetail.getGymName() + "</b>"));
                        textView.setClickable(true);
                        textView.setTextColor(ContextCompat.getColor(context, R.color.theme_dark_text));

                        layoutLocation.addView(textView);

                        textView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                            }
                        });
                    }
                }
            }

            imageViewProfile.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    adapterCallbacks.onAdapterItemClick(TrainerProfileViewHolder.this, imageViewProfile, model, position);
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
