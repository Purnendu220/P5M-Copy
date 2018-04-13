package www.gymhop.p5m.data.gym_class;

public class ClassMedia implements java.io.Serializable {
    private static final long serialVersionUID = 7751671452405443847L;
    private String mediaUrl;
    private String mediaThumbNailUrl;
    private int mediaId;

    public String getMediaUrl() {
        return this.mediaUrl == null ? "" : mediaUrl;
    }

    public void setMediaUrl(String mediaUrl) {
        this.mediaUrl = mediaUrl;
    }

    public String getMediaThumbNailUrl() {
        return this.mediaThumbNailUrl == null ? "" : mediaThumbNailUrl;
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
