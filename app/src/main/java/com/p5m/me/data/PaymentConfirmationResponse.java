package com.p5m.me.data;

public class PaymentConfirmationResponse {


        /**
         * referenceId : 291855563
         * packageName : Membership extension for 1 week
         * amount : 2.5
         * date : 1546414113000
         * status : SUCCESS
         * expiryDate : 2019-01-08
         * classDetailDto : {"classSessionId":45754,"classDate":"2019-01-31","fromTime":"13:00:00","toTime":"14:00:00","title":"Super Yoga ++"}
         */

        private String referenceId;
        private String packageName;
        private double amount;
        private long date;
        private String status;
        private String expiryDate;
        private String promoCode;
        private ClassDetailDtoBean classDetailDto;

        public String getReferenceId() {
            return referenceId;
        }

        public void setReferenceId(String referenceId) {
            this.referenceId = referenceId;
        }

        public String getPackageName() {
            return packageName;
        }

        public void setPackageName(String packageName) {
            this.packageName = packageName;
        }

        public double getAmount() {
            return amount;
        }

        public void setAmount(double amount) {
            this.amount = amount;
        }

        public long getDate() {
            return date;
        }

        public void setDate(long date) {
            this.date = date;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getExpiryDate() {
            return expiryDate;
        }

        public void setExpiryDate(String expiryDate) {
            this.expiryDate = expiryDate;
        }

        public ClassDetailDtoBean getClassDetailDto() {
            return classDetailDto;
        }

        public void setClassDetailDto(ClassDetailDtoBean classDetailDto) {
            this.classDetailDto = classDetailDto;
        }

    public String getPromoCode() {
        return promoCode;
    }

    public void setPromoCode(String promoCode) {
        this.promoCode = promoCode;
    }

    public static class ClassDetailDtoBean {
            /**
             * classSessionId : 45754
             * classDate : 2019-01-31
             * fromTime : 13:00:00
             * toTime : 14:00:00
             * title : Super Yoga ++
             */

            private int classSessionId;
            private String classDate;
            private String fromTime;
            private String toTime;
            private String title;

            public int getClassSessionId() {
                return classSessionId;
            }

            public void setClassSessionId(int classSessionId) {
                this.classSessionId = classSessionId;
            }

            public String getClassDate() {
                return classDate;
            }

            public void setClassDate(String classDate) {
                this.classDate = classDate;
            }

            public String getFromTime() {
                return fromTime;
            }

            public void setFromTime(String fromTime) {
                this.fromTime = fromTime;
            }

            public String getToTime() {
                return toTime;
            }

            public void setToTime(String toTime) {
                this.toTime = toTime;
            }

            public String getTitle() {
                return title;
            }

            public void setTitle(String title) {
                this.title = title;
            }
        }
}
