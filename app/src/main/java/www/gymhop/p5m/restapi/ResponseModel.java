package www.gymhop.p5m.restapi;

public class ResponseModel<T> {

    public String statusCode;
    public String errorMessage;
    public int totalCount;
    public T data;

}
