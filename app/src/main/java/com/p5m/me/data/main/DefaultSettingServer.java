package com.p5m.me.data.main;

import com.p5m.me.data.ValidityPackageList;

import java.util.List;

public class DefaultSettingServer implements java.io.Serializable {
    private static final long serialVersionUID = 6871393648272342621L;

    private String specialClassCancellationPolicy;
    private float refundAllowedbefore;
    private String cancellationPolicy;
    private List<ValidityPackageList> validityPackageList = null;
    private String popularClassesText;
    private float refundAllowedbeforeForSpecial;
    private String cancel_5_minPolicy;

    public String getSpecialClassCancellationPolicy() {
        return this.specialClassCancellationPolicy;
    }

    public void setSpecialClassCancellationPolicy(String specialClassCancellationPolicy) {
        this.specialClassCancellationPolicy = specialClassCancellationPolicy;
    }

    public float getRefundAllowedbefore() {
        return this.refundAllowedbefore;
    }

    public void setRefundAllowedbefore(int refundAllowedbefore) {
        this.refundAllowedbefore = refundAllowedbefore;
    }

    public String getCancellationPolicy() {
        return this.cancellationPolicy;
    }

    public void setCancellationPolicy(String cancellationPolicy) {
        this.cancellationPolicy = cancellationPolicy;
    }

    public void setRefundAllowedbefore(float refundAllowedbefore) {
        this.refundAllowedbefore = refundAllowedbefore;
    }

    public List<ValidityPackageList> getValidityPackageList() {
        return validityPackageList;
    }

    public void setValidityPackageList(List<ValidityPackageList> validityPackageList) {
        this.validityPackageList = validityPackageList;
    }

    public String getPopularClassesText() {
        return popularClassesText;
    }

    public void setPopularClassesText(String popularClassesText) {
        this.popularClassesText = popularClassesText;
    }

    public float getRefundAllowedbeforeForSpecial() {
        return refundAllowedbeforeForSpecial;
    }

    public void setRefundAllowedbeforeForSpecial(float refundAllowedbeforeForSpecial) {
        this.refundAllowedbeforeForSpecial = refundAllowedbeforeForSpecial;
    }
}
