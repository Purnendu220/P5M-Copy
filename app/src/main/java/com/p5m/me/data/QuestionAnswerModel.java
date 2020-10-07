
package com.p5m.me.data;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;


public class QuestionAnswerModel implements Serializable {

    @SerializedName("answer")
    private String mAnswer;
    @SerializedName("question")
    private String mQuestion;
    @SerializedName("questionId")
    private Long mQuestionId;

    public String getAnswer() {
        return mAnswer;
    }

    public void setAnswer(String answer) {
        mAnswer = answer;
    }

    public String getQuestion() {
        return mQuestion;
    }

    public void setQuestion(String question) {
        mQuestion = question;
    }

    public Long getQuestionId() {
        return mQuestionId;
    }

    public void setQuestionId(Long questionId) {
        mQuestionId = questionId;
    }

}
