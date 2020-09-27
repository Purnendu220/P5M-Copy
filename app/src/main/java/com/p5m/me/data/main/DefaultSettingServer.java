package com.p5m.me.data.main;

import com.p5m.me.data.ValidityPackageList;

import java.util.List;

public class DefaultSettingServer implements java.io.Serializable {
    private static final long serialVersionUID = 6871393648272342621L;

    private String specialClassCancellationPolicy;
    private String cancellationPolicy;
    private List<ValidityPackageList> validityPackageList = null;
    private String popularClassesText;
    private String cancel_5_minPolicy;
    private List<PriceModelMaster> priceModelMaster;
    private float refundAllowedbeforeForPhysical;
    private float refundAllowedbefore;
    private float refundAllowedbeforeForSpecial;
    private String physicalCancellationPolicy;


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

    public String getCancel_5_minPolicy() {
        return cancel_5_minPolicy;
    }

    public void setCancel_5_minPolicy(String cancel_5_minPolicy) {
        this.cancel_5_minPolicy = cancel_5_minPolicy;
    }

    public List<PriceModelMaster> getPriceModelMaster() {
        return priceModelMaster;
    }

    public void setPriceModelMaster(List<PriceModelMaster> priceModelMaster) {
        this.priceModelMaster = priceModelMaster;
    }

    public float getRefundAllowedbeforeForPhysical() {
        return refundAllowedbeforeForPhysical;
    }

    public void setRefundAllowedbeforeForPhysical(float refundAllowedbeforeForPhysical) {
        this.refundAllowedbeforeForPhysical = refundAllowedbeforeForPhysical;
    }

    public String getPhysicalCancellationPolicy() {
        return physicalCancellationPolicy;
    }

    public void setPhysicalCancellationPolicy(String physicalCancellationPolicy) {
        this.physicalCancellationPolicy = physicalCancellationPolicy;
    }
}
