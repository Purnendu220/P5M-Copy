package www.gymhop.p5m.data.request;

/**
 * Created by Varun John on 4/19/2018.
 */

public class PaymentUrlRequest {

    private Integer userId;
    private Integer packageId;
    private Integer sessionId;
    private Integer gymId;
    private Integer promoId;

    public PaymentUrlRequest(Integer userId, Integer packageId) {
        this.userId = userId;
        this.packageId = packageId;
    }

    public PaymentUrlRequest(Integer userId, Integer packageId, Integer sessionId, Integer gymId) {
        this.userId = userId;
        this.packageId = packageId;
        this.sessionId = sessionId;
        this.gymId = gymId;
    }

    public PaymentUrlRequest(Integer userId, Integer packageId, Integer sessionId, Integer gymId, Integer promoId) {
        this.userId = userId;
        this.packageId = packageId;
        this.sessionId = sessionId;
        this.gymId = gymId;
        this.promoId = promoId;
    }
}
