package www.gymhop.p5m.data;

import com.brandongogetap.stickyheaders.exposed.StickyHeader;

public class HeaderSticky implements StickyHeader {

    public String title;

    public HeaderSticky(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
