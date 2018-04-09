package www.gymhop.p5m.data;

public class Media implements java.io.Serializable {
    private static final long serialVersionUID = -507624299192461703L;
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
