package com.p5m.me.data.main;

import java.util.List;

public class PaymentInitiateModel {


    /**
     * IsSuccess : true
     * Message : Initiated Successfully!
     * ValidationErrors : null
     * Data : {"PaymentMethods":[{"IsDirectPayment":false,"ServiceCharge":4.319,"CurrencyIso":"SAR","PaymentMethodId":3,"PaymentMethodEn":"AMEX","ImageUrl":"https://demo.myfatoorah.com/imgs/payment-methods/ae.png","TotalAmount":10,"PaymentMethodCode":"ae","PaymentMethodAr":"اميكس"},{"IsDirectPayment":false,"ServiceCharge":4.319,"CurrencyIso":"SAR","PaymentMethodId":4,"PaymentMethodEn":"Sadad","ImageUrl":"https://demo.myfatoorah.com/imgs/payment-methods/s.png","TotalAmount":10,"PaymentMethodCode":"s","PaymentMethodAr":"سداد"},{"IsDirectPayment":false,"ServiceCharge":3.702,"CurrencyIso":"SAR","PaymentMethodId":5,"PaymentMethodEn":"Benefit","ImageUrl":"https://demo.myfatoorah.com/imgs/payment-methods/b.png","TotalAmount":10,"PaymentMethodCode":"b","PaymentMethodAr":"بنفت"},{"IsDirectPayment":false,"ServiceCharge":13.574,"CurrencyIso":"SAR","PaymentMethodId":2,"PaymentMethodEn":"VISA/MASTER","ImageUrl":"https://demo.myfatoorah.com/imgs/payment-methods/vm.png","TotalAmount":10,"PaymentMethodCode":"vm","PaymentMethodAr":"فيزا / ماستر"},{"IsDirectPayment":false,"ServiceCharge":4.319,"CurrencyIso":"SAR","PaymentMethodId":7,"PaymentMethodEn":"Qatar Debit Cards","ImageUrl":"https://demo.myfatoorah.com/imgs/payment-methods/np.png","TotalAmount":10,"PaymentMethodCode":"np","PaymentMethodAr":"البطاقات المدينة - قطر"},{"IsDirectPayment":false,"ServiceCharge":12.34,"CurrencyIso":"SAR","PaymentMethodId":6,"PaymentMethodEn":"MADA","ImageUrl":"https://demo.myfatoorah.com/imgs/payment-methods/md.png","TotalAmount":10,"PaymentMethodCode":"md","PaymentMethodAr":"مدى"},{"IsDirectPayment":false,"ServiceCharge":13.574,"CurrencyIso":"SAR","PaymentMethodId":1,"PaymentMethodEn":"KNET","ImageUrl":"https://demo.myfatoorah.com/imgs/payment-methods/kn.png","TotalAmount":10,"PaymentMethodCode":"kn","PaymentMethodAr":"كي نت"},{"IsDirectPayment":false,"ServiceCharge":13.574,"CurrencyIso":"SAR","PaymentMethodId":11,"PaymentMethodEn":"Apple Pay","ImageUrl":"https://demo.myfatoorah.com/imgs/payment-methods/ap.png","TotalAmount":10,"PaymentMethodCode":"ap","PaymentMethodAr":"أبل الدفع"},{"IsDirectPayment":false,"ServiceCharge":0,"CurrencyIso":"SAR","PaymentMethodId":14,"PaymentMethodEn":"STC Pay","ImageUrl":"https://demo.myfatoorah.com/imgs/payment-methods/stc.png","TotalAmount":10,"PaymentMethodCode":"stc","PaymentMethodAr":"STC Pay"},{"IsDirectPayment":false,"ServiceCharge":4.319,"CurrencyIso":"SAR","PaymentMethodId":8,"PaymentMethodEn":"UAE Debit Cards","ImageUrl":"https://demo.myfatoorah.com/imgs/payment-methods/uaecc.png","TotalAmount":10,"PaymentMethodCode":"uaecc","PaymentMethodAr":"البطاقات المدينة - الامارات"}]}
     */

    private boolean IsSuccess;
    private String Message;
    private Object ValidationErrors;
    private DataBean Data;

    public boolean isIsSuccess() {
        return IsSuccess;
    }

    public void setIsSuccess(boolean IsSuccess) {
        this.IsSuccess = IsSuccess;
    }

    public String getMessage() {
        return Message;
    }

    public void setMessage(String Message) {
        this.Message = Message;
    }

    public Object getValidationErrors() {
        return ValidationErrors;
    }

    public void setValidationErrors(Object ValidationErrors) {
        this.ValidationErrors = ValidationErrors;
    }

    public DataBean getData() {
        return Data;
    }

    public void setData(DataBean Data) {
        this.Data = Data;
    }

    public static class DataBean {
        private List<PaymentMethodsBean> PaymentMethods;

        public List<PaymentMethodsBean> getPaymentMethods() {
            return PaymentMethods;
        }

        public void setPaymentMethods(List<PaymentMethodsBean> PaymentMethods) {
            this.PaymentMethods = PaymentMethods;
        }

        public static class PaymentMethodsBean {
            /**
             * IsDirectPayment : false
             * ServiceCharge : 4.319
             * CurrencyIso : SAR
             * PaymentMethodId : 3
             * PaymentMethodEn : AMEX
             * ImageUrl : https://demo.myfatoorah.com/imgs/payment-methods/ae.png
             * TotalAmount : 10
             * PaymentMethodCode : ae
             * PaymentMethodAr : اميكس
             */

            private boolean IsDirectPayment;
            private double ServiceCharge;
            private String CurrencyIso;
            private int PaymentMethodId;
            private String PaymentMethodEn;
            private String ImageUrl;
            private int TotalAmount;
            private String PaymentMethodCode;
            private String PaymentMethodAr;

            public boolean isIsDirectPayment() {
                return IsDirectPayment;
            }

            public void setIsDirectPayment(boolean IsDirectPayment) {
                this.IsDirectPayment = IsDirectPayment;
            }

            public double getServiceCharge() {
                return ServiceCharge;
            }

            public void setServiceCharge(double ServiceCharge) {
                this.ServiceCharge = ServiceCharge;
            }

            public String getCurrencyIso() {
                return CurrencyIso;
            }

            public void setCurrencyIso(String CurrencyIso) {
                this.CurrencyIso = CurrencyIso;
            }

            public int getPaymentMethodId() {
                return PaymentMethodId;
            }

            public void setPaymentMethodId(int PaymentMethodId) {
                this.PaymentMethodId = PaymentMethodId;
            }

            public String getPaymentMethodEn() {
                return PaymentMethodEn;
            }

            public void setPaymentMethodEn(String PaymentMethodEn) {
                this.PaymentMethodEn = PaymentMethodEn;
            }

            public String getImageUrl() {
                return ImageUrl;
            }

            public void setImageUrl(String ImageUrl) {
                this.ImageUrl = ImageUrl;
            }

            public int getTotalAmount() {
                return TotalAmount;
            }

            public void setTotalAmount(int TotalAmount) {
                this.TotalAmount = TotalAmount;
            }

            public String getPaymentMethodCode() {
                return PaymentMethodCode;
            }

            public void setPaymentMethodCode(String PaymentMethodCode) {
                this.PaymentMethodCode = PaymentMethodCode;
            }

            public String getPaymentMethodAr() {
                return PaymentMethodAr;
            }

            public void setPaymentMethodAr(String PaymentMethodAr) {
                this.PaymentMethodAr = PaymentMethodAr;
            }
        }
    }
}
