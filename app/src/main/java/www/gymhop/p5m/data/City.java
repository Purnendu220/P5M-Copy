package www.gymhop.p5m.data;

import java.util.List;

public class City implements java.io.Serializable {
    private static final long serialVersionUID = 1702340531760181107L;
    private String name;
    private List<CityLocality> locality;
    private int id;
    private boolean status;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<CityLocality> getLocality() {
        return locality;
    }

    public void setLocality(List<CityLocality> locality) {
        this.locality = locality;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }
}
