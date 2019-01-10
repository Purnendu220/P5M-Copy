package com.p5m.me.data;

public class PaymentConfirmationResponse {


        /**
         * referenceId : 1170958047
         * packageName : SPECIAL
         * amount : 10.0
         * date : 1547100322000
         * status : SUCCESS
         * expiryDate : 2019-01-10
         * numberOfClasses : 1
         * classDetailDto : {"classSessionId":45903,"classDate":"2019-01-14","fromTime":"01:05:00","toTime":"02:05:00","title":"Hip Hop Class"}
         */

        private String referenceId;
        private String packageName;
        private double amount;
        private long date;
        private String status;
        private String expiryDate;
        private int numberOfClasses;
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

        public int getNumberOfClasses() {
            return numberOfClasses;
        }

        public void setNumberOfClasses(int numberOfClasses) {
            this.numberOfClasses = numberOfClasses;
        }

        public ClassDetailDtoBean getClassDetailDto() {
            return classDetailDto;
        }

        public void setClassDetailDto(ClassDetailDtoBean classDetailDto) {
            this.classDetailDto = classDetailDto;
        }

        public static class ClassDetailDtoBean {
            /**
             * classSessionId : 45903
             * classDate : 2019-01-14
             * fromTime : 01:05:00
             * toTime : 02:05:00
             * title : Hip Hop Class
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
