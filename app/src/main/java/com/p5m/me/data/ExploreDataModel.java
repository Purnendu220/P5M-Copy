package com.p5m.me.data;

import java.util.List;

public class ExploreDataModel {


    /**
     * widgetType : BANNER_CAROUSEL_WIDGET
     * showDivider : true
     * viewType : BANNER_CAROUSEL_VIEW
     * data : [{"title":"Tittle 1","subtitle":"Sub Tittle 1","url":"","imagePath":"http://d1zfy71n00v47t.cloudfront.net/explore/banner/6f838e7e-d2c7-47ef-8c05-8a8bdf7d51bf.png"},{"title":"Tittle 2","subtitle":"Sub Tittle 2","url":"","imagePath":"http://d1zfy71n00v47t.cloudfront.net/explore/banner/6f838e7e-d2c7-47ef-8c05-8a8bdf7d51xw.jpg"},{"title":"Tittle 3","subtitle":"Sub Tittle 3","url":"","imagePath":"http://d1zfy71n00v47t.cloudfront.net/explore/banner/86b9ec2b-4f6e-4018-9d13-daa0558c832e.png"},{"title":"Tittle 4","subtitle":"Sub Tittle 4","url":"","imagePath":"http://d1zfy71n00v47t.cloudfront.net/explore/banner/efdb80c4-5611-4399-9438-afa3f42df81d.jpg"}]
     * header : {"title":"Try P5M","subTitle":""}
     */

    private String widgetType;
    private boolean showDivider;
    private String viewType;
    private HeaderBean header;
    private List<Object> data;
    private int maxItemNumber;
    private boolean isMoreActivityShow;

    public String getWidgetType() {
        return widgetType;
    }

    public void setWidgetType(String widgetType) {
        this.widgetType = widgetType;
    }

    public boolean isShowDivider() {
        return showDivider;
    }

    public void setShowDivider(boolean showDivider) {
        this.showDivider = showDivider;
    }

    public String getViewType() {
        return viewType;
    }

    public void setViewType(String viewType) {
        this.viewType = viewType;
    }

    public HeaderBean getHeader() {
        return header;
    }

    public void setHeader(HeaderBean header) {
        this.header = header;
    }

    public List<Object> getData() {
        return data;
    }

    public void setData(List<Object> data) {
        this.data = data;
    }

    public int getMaxItemNumber() {
        return maxItemNumber;
    }

    public void setMaxItemNumber(int maxItemNumber) {
        this.maxItemNumber = maxItemNumber;
    }

    public boolean isMoreActivityShow() {
        return isMoreActivityShow;
    }

    public void setMoreActivityShow(boolean moreActivityShow) {
        isMoreActivityShow = moreActivityShow;
    }


    public static class HeaderBean {

        /**
         * title : Activities
         * subTitle : What workout are you in the mood for today?
         * titleAr : null
         * subTitleAr : null
         */

        private String title;
        private String subTitle;
        private String titleAr;
        private String subTitleAr;

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getSubTitle() {
            return subTitle;
        }

        public void setSubTitle(String subTitle) {
            this.subTitle = subTitle;
        }

        public String getTitleAr() {
            return titleAr;
        }

        public void setTitleAr(String titleAr) {
            this.titleAr = titleAr;
        }

        public String getSubTitleAr() {
            return subTitleAr;
        }

        public void setSubTitleAr(String subTitleAr) {
            this.subTitleAr = subTitleAr;
        }
    }

}

