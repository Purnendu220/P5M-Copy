package com.p5m.me.adapters.viewholder;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.p5m.me.R;
import com.p5m.me.adapters.AdapterCallbacks;
import com.p5m.me.data.Testimonials;
import com.p5m.me.utils.ImageUtils;
import com.p5m.me.utils.LanguageUtils;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by MyU10 on 3/10/2018.
 */

public class TestimonialsViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.textViewName)
    public TextView textViewName;
    @BindView(R.id.textViewComment)
    public TextView textViewComment;
    @BindView(R.id.imageViewProfile)
    public ImageView imageViewProfile;

    private final Context context;

    public TestimonialsViewHolder(View itemView) {
        super(itemView);

        context = itemView.getContext();

        itemView.setClickable(true);

        ButterKnife.bind(this, itemView);
    }

    public void bind(final Object data, final AdapterCallbacks adapterCallbacks, final int position) {

        if (data != null && data instanceof Testimonials) {
            itemView.setVisibility(View.VISIBLE);

            Testimonials model = (Testimonials) data;
//            if (model.getProfile_img() != null)
//                ImageUtils.setImage(context, model.getProfile_img(), imageViewProfile);
            if (!model.getName().isEmpty())
                textViewName.setText(model.getName());
            if (LanguageUtils.getLocalLanguage().equalsIgnoreCase("en") || !model.getMessage_eng().isEmpty())
                textViewComment.setText("\""+model.getMessage_eng()+"\"");
            else  if (LanguageUtils.getLocalLanguage().equalsIgnoreCase("ar") || !model.getMessage_ar().isEmpty())
                textViewComment.setText("\""+model.getMessage_ar()+"\"");


        }
    }
}
