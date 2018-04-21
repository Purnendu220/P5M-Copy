package www.gymhop.p5m.data.temp;

public class GymDetailModelMediaResponseDtoList implements java.io.Serializable {
    private static final long serialVersionUID = 5416341011522617250L;
    private String mediaUrl;
    private String mediaThumbNailUrl;
    private int mediaId;

    public String getMediaUrl() {
        return this.mediaUrl;
    }

    public void setMediaUrl(String mediaUrl) {
        this.mediaUrl = mediaUrl;
    }

    public String getMediaThumbNailUrl() {
        return this.mediaThumbNailUrl;
    }

    public void setMediaThumbNailUrl(String mediaThumbNailUrl) {
        this.mediaThumbNailUrl = mediaThumbNailUrl;
    }

    public int getMediaId() {
        return this.mediaId;
    }

    public void setMediaId(int mediaId) {
        this.mediaId = mediaId;
    }
}
