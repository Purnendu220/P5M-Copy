package com.p5m.me.data;

public class FAQ {

    /**
     * english_question : How many times can I visit a gym with my package?
     * english_answer : View gym visit limits
     * arabic_question : كم مرة يمكنني زيارة نادي رياضي مع اشتراكي؟
     * arabic_answer : ​الاطلاع على حدود زيارة النوادي
     * redirect : gymVisitLimits
     * redirect_android_link :
     */

    private String english_question;
    private String english_answer;
    private String arabic_question;
    private String arabic_answer;
    private String redirect;
    private String redirect_android_link;
    private Boolean isSelected;

    public Boolean getSelected() {
        return isSelected;
    }

    public void setSelected(Boolean selected) {
        isSelected = selected;
    }

    public String getEnglish_question() {
        return english_question;
    }

    public void setEnglish_question(String english_question) {
        this.english_question = english_question;
    }

    public String getEnglish_answer() {
        return english_answer;
    }

    public void setEnglish_answer(String english_answer) {
        this.english_answer = english_answer;
    }

    public String getArabic_question() {
        return arabic_question;
    }

    public void setArabic_question(String arabic_question) {
        this.arabic_question = arabic_question;
    }

    public String getArabic_answer() {
        return arabic_answer;
    }

    public void setArabic_answer(String arabic_answer) {
        this.arabic_answer = arabic_answer;
    }

    public String getRedirect() {
        return redirect;
    }

    public void setRedirect(String redirect) {
        this.redirect = redirect;
    }

    public String getRedirect_android_link() {
        return redirect_android_link;
    }

    public void setRedirect_android_link(String redirect_android_link) {
        this.redirect_android_link = redirect_android_link;
    }
}
