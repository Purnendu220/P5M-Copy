package com.p5m.me.adapters.viewholder;

        import android.content.Context;
        import android.text.Html;
        import android.view.View;
        import android.widget.TextView;

        import androidx.recyclerview.widget.RecyclerView;

        import com.p5m.me.R;
        import com.p5m.me.adapters.AdapterCallbacks;
        import com.p5m.me.data.Nationality;
        import com.p5m.me.data.QuestionAnswerModel;
        import com.p5m.me.utils.LanguageUtils;

        import butterknife.BindView;
        import butterknife.ButterKnife;


public class CovidSaftyViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.questionTxt)
    public TextView questionTxt;
    @BindView(R.id.answerTxt)
    public TextView answerTxt;

    private final Context context;

    public CovidSaftyViewHolder(View itemView) {
        super(itemView);

        context = itemView.getContext();

        itemView.setClickable(true);

        ButterKnife.bind(this, itemView);
    }

    public void bind(final Object data, final AdapterCallbacks adapterCallbacks, final int position) {

        if (data != null && data instanceof QuestionAnswerModel) {
            itemView.setVisibility(View.VISIBLE);

            QuestionAnswerModel model = (QuestionAnswerModel) data;
            if(LanguageUtils.getLocalLanguage().equalsIgnoreCase("ar")){
                questionTxt.setText(Html.fromHtml(String.format(context.getString(R.string.question_safety),model.getAnswer(),"<b>"+model.getQuestion()+"</b>")));

            }else{
                questionTxt.setText(Html.fromHtml(String.format(context.getString(R.string.question_safety),"<b>"+model.getQuestion()+"</b>",model.getAnswer())));

            }
            answerTxt.setText(model.getAnswer());





        } else {
            itemView.setVisibility(View.GONE);
        }
    }
}
