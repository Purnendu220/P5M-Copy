package com.p5m.me.adapters.viewholder;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.p5m.me.R;
import com.p5m.me.adapters.AdapterCallbacks;
import com.p5m.me.data.FAQ;
import com.p5m.me.utils.LanguageUtils;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * FAQ View Holder
 */

public class FAQViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.textViewQuestions)
    public TextView textViewQuestion;
    @BindView(R.id.textViewAnswer)
    public TextView textViewAnswer;

    private final Context context;

    public FAQViewHolder(View itemView) {
        super(itemView);

        context = itemView.getContext();

        itemView.setClickable(true);

        ButterKnife.bind(this, itemView);
    }

    public void bind(final Object data, final AdapterCallbacks adapterCallbacks, final int position) {
        textViewAnswer.setVisibility(View.GONE);
        if (data != null && data instanceof FAQ) {
            itemView.setVisibility(View.VISIBLE);

            FAQ model = (FAQ) data;
//            if (model.getProfile_img() != null)
//                ImageUtils.setImage(context, model.getProfile_img(), imageViewProfile);
            if (LanguageUtils.getLocalLanguage().equalsIgnoreCase("en")) {
                if (!model.getEnglish_question().isEmpty())
                    textViewQuestion.setText(model.getEnglish_question());
                if (!model.getEnglish_answer().isEmpty())
                    textViewAnswer.setText(model.getEnglish_answer());
            } else if (LanguageUtils.getLocalLanguage().equalsIgnoreCase("ar")) {
                if (!model.getArabic_question().isEmpty())
                    textViewQuestion.setText(model.getArabic_question());
                if (!model.getArabic_answer().isEmpty())
                    textViewAnswer.setText(model.getArabic_answer());
            }
            model.setSelected(true);

            textViewQuestion.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(model.getSelected()) {
                        model.setSelected(false);
                        textViewAnswer.setVisibility(View.GONE);
                    }
                    else {
                        model.setSelected(true);
                        textViewAnswer.setVisibility(View.VISIBLE);
                    }
                    adapterCallbacks.onAdapterItemClick(FAQViewHolder.this, textViewQuestion, model, position);
                }

            });
            textViewAnswer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    adapterCallbacks.onAdapterItemClick(FAQViewHolder.this, textViewAnswer, model, position);
                }
            });


        }
    }
}
