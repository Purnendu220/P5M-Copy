package com.p5m.me.adapters.viewholder;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.p5m.me.R;
import com.p5m.me.adapters.AdapterCallbacks;
import com.p5m.me.data.main.GymModel;
import com.p5m.me.helper.Helper;
import com.p5m.me.utils.AppConstants;
import com.p5m.me.utils.ImageUtils;
import com.p5m.me.utils.LogUtils;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by MyU10 on 3/10/2018.
 */

public class GymListByCategoryViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.textViewName)
    public TextView textViewTrainerName;
    @BindView(R.id.textViewSubtitle)
    public TextView textViewSubtitle;

    @BindView(R.id.imageViewProfile)
    public ImageView imageViewProfile;
    @BindView(R.id.imageViewSubtitle)
    public ImageView imageViewSubtitle;

    private final Context context;
    private int shownInScreen;

    public GymListByCategoryViewHolder(View itemView, int shownInScreen) {
        super(itemView);

        context = itemView.getContext();

        ButterKnife.bind(this, itemView);

        this.shownInScreen = shownInScreen;

    }

    public void bind(final Object data, final AdapterCallbacks adapterCallbacks, final int position) {

        if (data != null && data instanceof GymModel) {
            itemView.setVisibility(View.VISIBLE);

            final GymModel model = (GymModel) data;

            if (model.getProfileImageThumbnail() != null) {
                ImageUtils.setImage(context,
                        model.getProfileImage(),
                        R.color.white,R.drawable.profile_holder, imageViewProfile);
            }
            textViewTrainerName.setText(model.getGymNames());

                if (model.getTrainerBranchResponseList() != null ) {

                    try {
                        String name = "";
                        for (int index = 0; index < model.getTrainerBranchResponseList().size(); index++) {
                            String value = model.getTrainerBranchResponseList().get(index).getLocalityName();
                            if (!name.contains(value)) {
                                name = index == 0 ? (name += value) : (name += ", " + value);
                            }
                        }
                        textViewSubtitle.setText(name);
                        if(!TextUtils.isEmpty(name)){
                            textViewSubtitle.setVisibility(View.VISIBLE);
                            imageViewSubtitle.setVisibility(View.VISIBLE);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        LogUtils.exception(e);
                        imageViewSubtitle.setVisibility(View.GONE);
                        textViewSubtitle.setVisibility(View.GONE);
                    }
                } else {
                    textViewSubtitle.setVisibility(View.GONE);
                }
            }
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    adapterCallbacks.onAdapterItemClick(GymListByCategoryViewHolder.this, textViewTrainerName, data, position);
                }
            });



    }
}
