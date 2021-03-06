package com.p5m.me.adapters.viewholder;

import android.content.Context;
import android.text.Html;
import android.text.TextUtils;
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
    @BindView(R.id.view)
    public View view;

    private final Context context;

    public FAQViewHolder(View itemView) {
        super(itemView);

        context = itemView.getContext();

        itemView.setClickable(true);

        ButterKnife.bind(this, itemView);
    }

    public void bind(final Object data, final AdapterCallbacks adapterCallbacks, final int position,final  int lastPosition) {
        textViewAnswer.setVisibility(View.GONE);
        if (data != null && data instanceof FAQ) {
            itemView.setVisibility(View.VISIBLE);

            FAQ model = (FAQ) data;
//            if (model.getProfile_img() != null)
//                ImageUtils.setImage(context, model.getProfile_img(), imageViewProfile);
            if (LanguageUtils.getLocalLanguage().equalsIgnoreCase("en")) {
                if (!model.getEnglish_question().isEmpty()) {
                    textViewQuestion.setText(model.getEnglish_question());
                }
                else
                    textViewQuestion.setVisibility(View.GONE);

                if (!model.getEnglish_answer().isEmpty()) {
                    if(model.getRedirect_android_link()!=null && !TextUtils.isEmpty(model.getRedirect_android_link()))
                    {
                        String str= "<u><font color='#3d85ea'>"+  model.getEnglish_answer() +"</font></u>";
                        textViewAnswer.setText(Html.fromHtml(str));
                    }
                    else
                    {
                        textViewAnswer.setText(model.getEnglish_answer());
                    }
                }
                else
                    textViewAnswer.setVisibility(View.GONE);

            } else if (LanguageUtils.getLocalLanguage().equalsIgnoreCase("ar")) {
                if (!model.getArabic_question().isEmpty()) {
                    textViewQuestion.setText(model.getArabic_question());
                }
                else
                    textViewQuestion.setVisibility(View.GONE);

                if (!model.getArabic_answer().isEmpty()) {
                    if(model.getRedirect_android_link()!=null && !TextUtils.isEmpty(model.getRedirect_android_link()))
                    {
                        String str= "<u><font color='#3d85ea'>"+  model.getArabic_answer() +"</font></u>";
                        textViewAnswer.setText(Html.fromHtml(str));
                    }
                    else
                    {
                        textViewAnswer.setText(model.getArabic_answer());
                    }
                }
                else
                    textViewAnswer.setVisibility(View.GONE);
            }
            model.setSelected(true);

            textViewQuestion.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(model.getSelected()) {
                        model.setSelected(false);
                        textViewAnswer.setVisibility(View.VISIBLE);
                        textViewQuestion.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.grey_arrow_up,0);
                    }
                    else {
                        model.setSelected(true);
                        textViewAnswer.setVisibility(View.GONE);
                        textViewQuestion.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.grey_arrow_down,0);

                    }
                    adapterCallbacks.onAdapterItemClick(FAQViewHolder.this, textViewQuestion, model, position);
                }

            });
            if(model.getRedirect_android_link()!=null && !TextUtils.isEmpty(model.getRedirect_android_link())) {
                textViewAnswer.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        adapterCallbacks.onAdapterItemClick(FAQViewHolder.this, textViewAnswer, model, position);
                    }
                });
            }

            if(position+1 == lastPosition)
                view.setVisibility(View.GONE);
        }
    }
}
