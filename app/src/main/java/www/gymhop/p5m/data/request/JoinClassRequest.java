package www.gymhop.p5m.data.request;

/**
 * Created by Varun John on 4/19/2018.
 */

public class JoinClassRequest {

    private Integer userId;
    private Integer classSessionId;

    public JoinClassRequest(Integer userId, Integer classSessionId) {
        this.userId = userId;
        this.classSessionId = classSessionId;
    }
}
