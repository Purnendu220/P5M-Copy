package www.gymhop.p5m.data.main;

public class DefaultSettingServer implements java.io.Serializable {
    private static final long serialVersionUID = 6871393648272342621L;
    private String specialClassCancellationPolicy;
    private int refundAllowedbefore;
    private String cancellationPolicy;

    public String getSpecialClassCancellationPolicy() {
        return this.specialClassCancellationPolicy;
    }

    public void setSpecialClassCancellationPolicy(String specialClassCancellationPolicy) {
        this.specialClassCancellationPolicy = specialClassCancellationPolicy;
    }

    public int getRefundAllowedbefore() {
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
}
